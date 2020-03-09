package audio;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;

import utils.FileUtils;

public class VorbisPlayer {

	private static final float DEFAULT_VOLUME = 0.3f;
	private static final Exception UNSUPPORTED_LINE = new Exception("Line Not Supported");
	private static final Exception CRITICAL_ILLEGAL_DATA = new Exception("Corrupted Player Data State Found");

	private enum HeaderState {
		RESTART, FAIL, SUCCESS
	}

	private static final int BITS_PER_SAMPLE = 16;
	private static final int BUFSIZE = 4096 * 2;

	private int convsize = BUFSIZE * 2;
	private byte[] convbuffer = new byte[this.convsize];
	private byte[] buffer = null;
	private int bytes = 0;

	private SyncState sync;
	private StreamState stream;
	private Page page;
	private Packet packet;
	private Info info;
	private Comment comment;
	private DspState dspSate;
	private Block block;

	private int rate = 0;
	private int channels = 0;
	private SourceDataLine outputLine = null;

	private float volume = 0;

	private InputStream bitStream;

	private Object playerData;

	public static void main(String[] args) throws Exception {

		VorbisPlayer player = 
			new VorbisPlayer(FileUtils.readFile("res/lobby (2).ogg"), .5f);
		player.play();

	}

	public VorbisPlayer(String filePath) throws Exception {
		this.init(filePath, DEFAULT_VOLUME);
	}

	public VorbisPlayer(String filePath, float volume) throws Exception {
		this.init(filePath, volume);
	}

	public VorbisPlayer(byte[] fileData) throws Exception {
		this.init(fileData, DEFAULT_VOLUME);
	}

	public VorbisPlayer(byte[] fileData, float volume) throws Exception {
		this.init(fileData, volume);
	}

	public void setVolume(float volume) {

		this.volume = volume;

		FloatControl vol = (FloatControl) this.outputLine.getControl(FloatControl.Type.MASTER_GAIN);
		if (this.volume < 0 || this.volume > 6) {
			this.volume = 1.0f;
		}
		if (this.volume < 1.0f) {
			this.volume = -(80.0f - (this.volume * 80.0f));
		} else {
			this.volume = (this.volume - 1.0f);
		}
		vol.setValue(volume);
	}

	public void play() throws Exception {

		if (this.playerData instanceof String) {
			this.bitStream = new FileInputStream((String) this.playerData);
		} else if (this.playerData instanceof byte[]) {
			this.bitStream = new ByteArrayInputStream((byte[]) this.playerData);
		} else {
			throw CRITICAL_ILLEGAL_DATA;
		}

		boolean chained = false;
		int index = 0;

		this.initJorbis();

		while (true) {

			index = this.sync.buffer(BUFSIZE);
			this.buffer = this.sync.data;
			try {
				this.bytes = this.bitStream.read(this.buffer, index, BUFSIZE);
			} catch (Exception e) {
				System.err.println(e);
				return;
			}
			this.sync.wrote(this.bytes);

			if (chained) {
				chained = false;
			} else {
				if (this.sync.pageout(this.page) != 1) {
					if (this.bytes < BUFSIZE) {
						break;
					}
					System.err.println("Input does not appear to be an Ogg bitstream.");
					return;
				}
			}
			this.stream.init(this.page.serialno());
			this.stream.reset();

			this.info.init();
			this.comment.init();

			switch (this.checkStreamErrors()) {
				case FAIL:
					throw new Exception("Failed to read headers");
				case RESTART:
					break;
				case SUCCESS:
					break;
				default:
					break;
			}

			switch (this.readAudioHeaders(index)) {
				case FAIL:
					break;
				case RESTART:
					continue;
				case SUCCESS:
					break;
				default:
					break;
			}

			this.convsize = BUFSIZE / this.info.channels;

			this.dspSate.synthesis_init(this.info);
			this.block.init(this.dspSate);

			this.initOutputLine(this.info.channels, this.info.rate, 0);
			this.setVolume(this.volume);
			this.setVolume(this.volume);
			this.playData(index);

			this.stream.clear();
			this.block.clear();
			this.dspSate.clear();
			this.info.clear();
		}

		this.sync.clear();

		try {
			if (this.bitStream != null) {
				this.bitStream.close();
			}
		} catch (Exception e) {
		}
	}

	private void init(Object data, float volume) {
		this.volume = volume;
		this.playerData = data;
	}

	private void initAudio(int channels, int rate, int bitsPerSample) throws Exception {

		bitsPerSample = bitsPerSample < 1 ? BITS_PER_SAMPLE : bitsPerSample;

		AudioFormat audioFormat = new AudioFormat((float) rate, BITS_PER_SAMPLE, channels, true, false);

		DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat, AudioSystem.NOT_SPECIFIED);
		if (!AudioSystem.isLineSupported(info)) {
			throw UNSUPPORTED_LINE;
		}

		try {
			this.outputLine = (SourceDataLine) AudioSystem.getLine(info);
			this.outputLine.open(audioFormat);
		} catch (LineUnavailableException ex) {
			throw new Exception("Unable to open the sourceDataLine: " + ex);
		} catch (IllegalArgumentException ex) {
			throw ex;
		}

		this.rate = rate;
		this.channels = channels;
	}

	private void playData(int index) {
		float[][][] pcmfFloats = new float[1][][];
		int[] infoChannels = new int[this.info.channels];
		int eos = 0;

		while (eos == 0) {
			while (eos == 0) {

				int result = this.sync.pageout(this.page);
				if (result == 0) {
					break;
				}
				this.decodeSlice(result, eos, pcmfFloats, infoChannels);
			}

			if (eos == 0) {
				index = this.sync.buffer(BUFSIZE);
				this.buffer = this.sync.data;
				try {
					this.bytes = this.bitStream.read(this.buffer, index, BUFSIZE);
				} catch (Exception e) {
					System.err.println(e);
					return;
				}
				if (this.bytes == -1) {
					break;
				}
				this.sync.wrote(this.bytes);
				if (this.bytes == 0) {
					eos = 1;
				}
			}

		}
	}

	private int decodeSlice(int result, int eos, float[][][] pcmfFloats, int[] infoChannels) {
		if (result != -1) {
			this.stream.pagein(this.page);
			if (this.page.granulepos() == 0) {
				eos = 1;
				return eos;
			}
			while (true) {
				result = this.stream.packetout(this.packet);
				if (result == 0) {
					break;
				}
				if (result != -1) {
					this.decodeAudioPacket(pcmfFloats, infoChannels);
				}
			}
			if (this.page.eos() != 0) {
				eos = 1;
			}
		}
		return eos;
	}

	private void decodeAudioPacket(float[][][] pcmfFloats, int[] infoChannels) {

		int samples;
		if (this.block.synthesis(this.packet) == 0) {
			this.dspSate.synthesis_blockin(this.block);
		}

		while ((samples = this.dspSate.synthesis_pcmout(pcmfFloats, infoChannels)) > 0) {
			float[][] pcmf = pcmfFloats[0];
			int bout = (samples < convsize ? samples : convsize);

			for (int i = 0; i < this.info.channels; i++) {
				int ptr = i * 2;
				int mono = infoChannels[i];
				for (int j = 0; j < bout; j++) {
					int val = (int) (pcmf[i][mono + j] * 32767.);
					if (val > Short.MAX_VALUE) {
						val = Short.MAX_VALUE;
					}
					if (val < Short.MIN_VALUE) {
						val = Short.MIN_VALUE;
					}
					if (val < 0) {
						val = val | 0x8000;
					}
					this.convbuffer[ptr] = (byte) (val);
					this.convbuffer[ptr + 1] = (byte) (val >>> 8);
					ptr += 2 * (this.info.channels);
				}
			}
			this.outputLine.write(this.convbuffer, 0, 2 * this.info.channels * bout);
			this.dspSate.synthesis_read(bout);
		}

	}

	private void initOutputLine(int channels, int rate, int bitsPerSample) throws Exception {
		if (this.outputLine == null || this.rate != rate || this.channels != channels) {
			if (this.outputLine != null) {
				this.outputLine.drain();
				this.outputLine.stop();
				this.outputLine.close();
			}
			this.initAudio(channels, rate, bitsPerSample);
			this.outputLine.start();
		}
	}

	private HeaderState readAudioHeaders(int index) {
		int i = 0;
		while (i < 2) {
			while (i < 2) {
				int result = this.sync.pageout(this.page);
				if (result == 0) {
					break;
				}
				if (result == 1) {
					this.stream.pagein(this.page);
					while (i < 2) {
						result = this.stream.packetout(this.packet);
						if (result == 0) {
							break;
						}
						if (result == -1) {
							System.err.println("Corrupt secondary header.  Exiting.");
							return HeaderState.RESTART;
						}
						this.info.synthesis_headerin(this.comment, this.packet);
						i++;
					}
				}
			}

			index = this.sync.buffer(BUFSIZE);
			HeaderState result = this.incrementResources(i, index);
			if (result != HeaderState.SUCCESS) {
				return result;
			}
		}

		return HeaderState.SUCCESS;
	}

	private HeaderState incrementResources(int loc, int index) {
		this.buffer = this.sync.data;
		try {
			this.bytes = this.bitStream.read(this.buffer, index, BUFSIZE);
		} catch (Exception e) {
			System.err.println(e);
			return HeaderState.FAIL;
		}
		if (this.bytes == 0 && loc < 2) {
			System.err.println("End of file before finding all Vorbis headers!");
			return HeaderState.FAIL;
		}
		this.sync.wrote(this.bytes);
		return HeaderState.SUCCESS;
	}

	private void initJorbis() {
		this.sync = new SyncState();
		this.stream = new StreamState();
		this.page = new Page();
		this.packet = new Packet();

		this.info = new Info();
		this.comment = new Comment();
		this.dspSate = new DspState();
		this.block = new Block(this.dspSate);

		this.buffer = null;
		this.bytes = 0;

		this.sync.init();
	}

	private HeaderState checkStreamErrors() {
		if (this.stream.pagein(this.page) < 0) {
			System.err.println("Error reading first page of Ogg bitstream data.");
			return HeaderState.FAIL;
		}

		if (this.stream.packetout(this.packet) != 1) {
			System.err.println("Error reading initial header packet.");
			return HeaderState.RESTART;
		}

		if (this.info.synthesis_headerin(this.comment, this.packet) < 0) {
			System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
			return HeaderState.FAIL;
		}

		return HeaderState.SUCCESS;
	}

}
