package communication;

import android.util.Log;

import edu.carleton.COMP2601.Connection;
import edu.carleton.COMP2601.Constants;
import edu.carleton.COMP2601.MainActivity;

public class GameOnHandler implements EventHandler {

	EventSource source;

	public GameOnHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		Log.d("GameOn/handleEvent", "");
		Log.d("GameOn/handleEvent", "--------------------------------");
		Log.d("GameOn/handleEvent", "Message Type: " + event.type);

		String username = (String) event.get(Fields.USERNAME);
		String destination = (String) event.get(Fields.DESTINATION);

		Connection.getInstance().getGameActivity().
				gameOn(username + " has started a game.");

		Log.d("GameOn/handleEvent", "Exiting Handler...");
		Log.d("GameOn/handleEvent", "--------------------------------");
	}

}
