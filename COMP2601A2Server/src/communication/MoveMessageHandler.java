package communication;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.carleton.COMP2601.GameServer;

public class MoveMessageHandler implements EventHandler {
	
	EventSource source;
	
	public MoveMessageHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println("MoveMessageHandler/handleEvent()");
		
		String username = (String) event.get(Fields.USERNAME);
		String destination = (String) event.get(Fields.DESTINATION);
		int row = (int) event.get(Fields.ROW);
		int column = (int) event.get(Fields.COLUMN);
		
		System.out.println("Move made by: " + username);
		System.out.println("Move being sent to: " + destination);
		System.out.println("Row: " + row);
		System.out.println("Column: " + column);
		
		// Retrieve Client
		ThreadWithReactor destinationThread = GameServer.getClientThread(destination);
		System.out.println("Destination Thread: " + destinationThread);
		
		// Send PLAY GAME RESPONSE to the destination user
		try {
			destinationThread.getEventSource().putEvent(event);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Exiting MoveMessage Handler...");
		System.out.println("-----------------------------");
		System.out.println();
	}

}
