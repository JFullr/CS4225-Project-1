package project.client;

public class NetworkData {
	
	public static final transient NetworkData HEART_BEAT = new NetworkData(NetworkGameState.HEART_BEAT);
	
	private NetworkGameState state;
	private Object[] data;
	
	public NetworkData(NetworkGameState state, Object... data) {
		this.state = state;
		this.data = data;
	}
	
	public NetworkGameState getState() {
		return this.state;
	}
	
	public Object[] getData() {
		return this.data;
	}
	
}
