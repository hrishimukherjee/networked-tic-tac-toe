package communication;

import android.util.Log;

import edu.carleton.COMP2601.Constants;
import edu.carleton.COMP2601.MainActivity;

public class ConnectedResponseHandler implements EventHandler {
	
	EventSource source;
	MainActivity mainActivity;
	
	public ConnectedResponseHandler(EventSource source, MainActivity mainActivity) {
		this.source = source;
		this.mainActivity = mainActivity;
	}
	
	public void handleEvent(Event event) {
		Log.d("ConnHandler/handleEvent", "");
		Log.d("ConnHandler/handleEvent", "--------------------------------");
		Log.d("ConnHandler/handleEvent", "Server Response: " + event.type);

		mainActivity.displayConnectedToast();
		mainActivity.toggleSpinner(Constants.HIDE_SPINNER);

		Log.d("ConnHandler/handleEvent", "Exiting Handler...");
		Log.d("ConnHandler/handleEvent", "--------------------------------");
	}

}
