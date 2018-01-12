package communication;

import android.util.Log;

import edu.carleton.COMP2601.Connection;

public class DisconnectResponseHandler implements EventHandler {

	EventSource source;

	public DisconnectResponseHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		Log.d("Disconnect/handleEvent", "");
		Log.d("Disconnect/handleEvent", "--------------------------------");
		Log.d("Disconnect/handleEvent", "Message Type: " + event.type);

		Log.d("Disconnect/handleEvent", "SUCCESSFULLY DISCONNECTED FROM SERVER!");

		Log.d("Disconnect/handleEvent", "Exiting Handler...");
		Log.d("Disconnect/handleEvent", "--------------------------------");
	}

}
