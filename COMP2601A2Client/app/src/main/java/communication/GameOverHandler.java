package communication;

import android.util.Log;

import edu.carleton.COMP2601.Connection;

public class GameOverHandler implements EventHandler {

	EventSource source;

	public GameOverHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		Log.d("GameOver/handleEvent", "");
		Log.d("GameOver/handleEvent", "--------------------------------");
		Log.d("GameOver/handleEvent", "Message Type: " + event.type);

		String username = (String) event.get(Fields.USERNAME);
		String destination = (String) event.get(Fields.DESTINATION);
		String reason = (String) event.get(Fields.REASON);

		Connection.getInstance().getGameActivity().updateStartButtonText("START");
		Connection.getInstance().getGameActivity().updateStatusBar(reason);

		Log.d("GameOver/handleEvent", "Exiting Handler...");
		Log.d("GameOver/handleEvent", "--------------------------------");
	}

}
