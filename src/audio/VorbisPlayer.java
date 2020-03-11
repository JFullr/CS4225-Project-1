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

/**
 * The Class VorbisPlayer is a simplified, reversed engineered version of the
 * jorbis-0.0.17.jar.
 * 
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class VorbisPlayer {
	
	public static final int REPEAT_FOREVER = -1;
	
	
	private static final float DEFAULT_VOLUME = 0.3f;

	private static final Exception UNSUPPORTED_LINE = new Exception("Line Not Supported");
	private static final Exception CRITICAL_ILLEGAL_DATA = new Exception("Corrupted Player Data State Found");
	private static final Exception HEADER_INIT_FAILURE = new Exception("Failed to init headers");
	private static final Exception HEADER_READ_FAILURE = new Exception("Failed to read headers");

	private static final int BITS_PER_SAMPLE = 16;
	private static final int BUFSIZE = 4096 * 2;
	private enum HeaderState {

		RESTART, FAIL, SUCCESS
	}

	private int convsize = BUFSIZE * 2;
	private int channels = 0;
	private int bytes = 0;
	private int rate = 0;
	private byte[] convbuffer = new byte[this.convsize];
	private byte[] buffer = null;
	private float volume = 0;

	private SyncState sync;
	private StreamState stream;
	private Page page;
	private Packet packet;
	private Info info;
	private Comment comment;
	private DspState dspSate;
	private Block block;
	private SourceDataLine outputLine = null;
	private InputStream bitStream;
	private Object playerData;
	private volatile Thread playerThread;
	
	/**
	 * Instantiates a new vorbis player.
	 *
	 * @param filePath the file path
	 * @throws Exception the exception
	 */
	public VorbisPlayer(String filePath) {
		this.init(filePath, DEFAULT_VOLUME);
	}

	/**
	 * Instantiates a new vorbis player.
	 *
	 * @param filePath the file path
	 * @param volume   the volume
	 * @throws Exception the exception
	 */
	public VorbisPlayer(String filePath, float volume) {
		this.init(filePath, volume);
	}

	/**
	 * Instantiates a new vorbis player.
	 *
	 * @param fileData the file data
	 * @throws Exception the exception
	 */
	public VorbisPlayer(byte[] fileData) {
		this.init(fileData, DEFAULT_VOLUME);
	}

	/**
	 * Instantiates a new vorbis player.
	 *
	 * @param fileData the file data
	 * @param volume   the volume
	 * @throws Exception the exception
	 */
	public VorbisPlayer(byte[] fileData, float volume) {
		this.init(fileData, volume);
	}

	/**
	 * Sets the volume.
	 *
	 * @param volume the new volume
	 */
	public void setVolume(float volume) {

		this.volume = volume;
		
		if(this.outputLine == null) {
			return;
		}

		FloatControl vol = (FloatControl) this.outputLine.getControl(FloatControl.Type.MASTER_GAIN);
		if (this.volume < 0 || this.volume > 6) {
			this.volume = 1.0f;
		}
		if (this.volume < 1.0f) {
			this.volume = -(80.0f - (this.volume * 80.0f));
		} else {
			this.volume = (this.volume - 1.0f);
		}
		vol.setValue(this.volume);
	}

	/**
	 * Play.
	 *
	 * @throws Exception the exception
	 */
	public void play() throws Exception {
		this.play(0);
	}
	
	private void play(int repeatCount) throws Exception {
		synchronized (this) {

			if (this.playerThread != null) {
				return;
			}

			this.playerThread = new Thread(() -> {
				try {
					this.playThread(repeatCount);
					this.bitStream.close();
				} catch (Exception e) { }//e.printStackTrace(); }
			});
			this.playerThread.start();
			
		}
		
	}
	
	@SuppressWarnings("deprecation")
	public boolean end() {
		synchronized (this) {
			try {

				if (this.playerThread == null) {
					return true;
				}
				
				this.playerThread.stop();
				
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}
	}
	
	private void playThread(int repeat) throws Exception {

		this.initBitStream();

		boolean chained = false;
		int index = 0;

		this.initJorbis();

		while (true) {

			index = this.sync.buffer(BUFSIZE);
			HeaderState state = null;
			
			if(this.initStream(index, chained) == HeaderState.RESTART) {
				break;
			}
			
			state = this.checkStreamErrors();
			if (state == HeaderState.RESTART) {
				break;
			}

			switch (this.readAudioHeaders(index)) {
				case RESTART:
					continue;
				default:
					break;
			}

			this.processHeaders(index);
		}
		
		this.cleanupPlaying(repeat);

	}

	private void cleanupPlaying(int repeat) throws Exception {
		this.sync.clear();

		try {
			if (this.bitStream != null) {
				this.bitStream.close();
			}
		} catch (Exception e) {
		}
		if (repeat != 0) {
			if (repeat > 0) {
				repeat--;
			}
			this.playerThread = null;
			this.play(repeat);
		}
		
	}

	private void processHeaders(int index) throws Exception {
		this.convsize = BUFSIZE / this.info.channels;

		this.dspSate.synthesis_init(this.info);
		this.block.init(this.dspSate);

		this.initOutputLine(this.info.channels, this.info.rate, 0);
		this.setVolume(this.volume);
		this.playData(index);
		
		if(this.playerThread == null) {
			return;
		}

		this.stream.clear();
		this.block.clear();
		this.dspSate.clear();
		this.info.clear();
	}

	private void initBitStream() throws Exception {
		if (this.playerData instanceof String) {
			this.bitStream = new FileInputStream((String) this.playerData);
		} else if (this.playerData instanceof byte[]) {
			this.bitStream = new ByteArrayInputStream((byte[]) this.playerData);
		} else {
			throw CRITICAL_ILLEGAL_DATA;
		}
	}

	private HeaderState initStream(int index, boolean chained) throws Exception {
		this.buffer = this.sync.data;
		try {
			this.bytes = this.bitStream.read(this.buffer, index, BUFSIZE);
		} catch (Exception e) {
			System.err.println(e);
			throw HEADER_INIT_FAILURE;
		}
		this.sync.wrote(this.bytes);

		if (chained) {
			chained = false;
		} else {
			if (this.sync.pageout(this.page) != 1) {
				if (this.bytes < BUFSIZE) {
					return HeaderState.RESTART;
				}
				System.err.println("Input does not appear to be an Ogg bitstream.");
				return HeaderState.SUCCESS;
			}
		}
		this.stream.init(this.page.serialno());
		this.stream.reset();

		this.info.init();
		this.comment.init();

		return HeaderState.SUCCESS;
	}

	private void init(Object data, float volume) {
		this.volume = volume;
		this.playerData = data;
		this.playerThread = null;
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
				eos = this.decodeSlice(result, eos, pcmfFloats, infoChannels);
				
				if(this.playerThread == null) {
					return;
				}
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

	private HeaderState checkStreamErrors() throws Exception {
		
		if (this.stream.pagein(this.page) < 0) {
			System.err.println("Error reading first page of Ogg bitstream data.");
			throw HEADER_READ_FAILURE;
		}

		if (this.stream.packetout(this.packet) != 1) {
			System.err.println("Error reading initial header packet.");
			return HeaderState.RESTART;
		}

		if (this.info.synthesis_headerin(this.comment, this.packet) < 0) {
			//System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
			throw HEADER_READ_FAILURE;
		}

		return HeaderState.SUCCESS;
	}

}
