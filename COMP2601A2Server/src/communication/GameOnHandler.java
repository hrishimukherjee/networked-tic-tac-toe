package communication;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.carleton.COMP2601.GameServer;

public class GameOnHandler implements EventHandler {
	
	EventSource source;
	
	public GameOnHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println("GameOnHandler/handleEvent()");
		
		String username = (String) event.get(Fields.USERNAME);
		String destination = (String) event.get(Fields.DESTINATION);
		
		System.out.println("Game on request by: " + username);
		System.out.println("Game on request being sent to: " + destination);
		
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
		
		System.out.println("Exiting Game On Handler...");
		System.out.println("-----------------------------");
		System.out.println();
	}

}
