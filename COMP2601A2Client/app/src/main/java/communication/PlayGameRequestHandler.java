package communication;

import android.util.Log;

import edu.carleton.COMP2601.Constants;
import edu.carleton.COMP2601.MainActivity;

public class PlayGameRequestHandler implements EventHandler {

	EventSource source;
	MainActivity mainActivity;

	public PlayGameRequestHandler(EventSource source, MainActivity mainActivity) {
		this.source = source;
		this.mainActivity = mainActivity;
	}
	
	public void handleEvent(Event event) {
		Log.d("PlayReq/handleEvent", "");
		Log.d("PlayReq/handleEvent", "--------------------------------");
		Log.d("PlayReq/handleEvent", "Message Type: " + event.type);

		String username = (String) event.get(Fields.USERNAME);

		Log.d("PlayReq/handleEvent", "Game Requested By: " + username);

		mainActivity.displayAlertDialog(username);

		Log.d("PlayReq/handleEvent", "Exiting Handler...");
		Log.d("PlayReq/handleEvent", "--------------------------------");
	}

}
