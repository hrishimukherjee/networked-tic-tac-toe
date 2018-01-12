package communication;

import android.util.Log;

import edu.carleton.COMP2601.Connection;

public class MoveMessageHandler implements EventHandler {

	EventSource source;

	public MoveMessageHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		Log.d("MoveMessage/handleEvent", "");
		Log.d("MoveMessage/handleEvent", "--------------------------------");
		Log.d("MoveMessage/handleEvent", "Message Type: " + event.type);

		String username = (String) event.get(Fields.USERNAME);
		String destination = (String) event.get(Fields.DESTINATION);
		int row = (int) event.get(Fields.ROW);
		int column = (int) event.get(Fields.COLUMN);

		Connection.getInstance().getGameActivity().
				receiveMoveMessage(row, column);

		Log.d("MoveMessage/handleEvent", "Exiting Handler...");
		Log.d("MoveMessage/handleEvent", "--------------------------------");
	}

}
