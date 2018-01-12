package communication;

import android.util.Log;

import edu.carleton.COMP2601.Constants;
import edu.carleton.COMP2601.MainActivity;

public class PlayGameResponseHandler implements EventHandler {

	EventSource source;
	MainActivity mainActivity;

	public PlayGameResponseHandler(EventSource source, MainActivity mainActivity) {
		this.source = source;
		this.mainActivity = mainActivity;
	}
	
	public void handleEvent(Event event) {
		Log.d("PlayRes/handleEvent", "");
		Log.d("PlayRes/handleEvent", "--------------------------------");
		Log.d("PlayRes/handleEvent", "Message Type: " + event.type);

		String username = (String) event.get(Fields.USERNAME);
		String destination = (String) event.get(Fields.DESTINATION);
		boolean playStatus = (boolean) event.get(Fields.PLAY_STATUS);

		if(playStatus == true) {
			Log.d("PlayRes/handleEvent", username +
					" accepted your game request!");

			// Start a new game activity with symbol X and opponent's username
			mainActivity.startGameActivity(Constants.PLAYER_X, username);
		} else {
			Log.d("PlayRes/handleEvent", username +
					" declined your game request...");

			// Update list activity status text
			mainActivity.setDeclinedListStatus(username);
		}

		Log.d("PlayRes/handleEvent", "Exiting Handler...");
		Log.d("PlayRes/handleEvent", "--------------------------------");
	}

}
