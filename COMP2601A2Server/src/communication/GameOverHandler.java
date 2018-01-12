package communication;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.carleton.COMP2601.GameServer;

public class GameOverHandler implements EventHandler {
	
	EventSource source;
	
	public GameOverHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println("GameOverHandler/handleEvent()");
		
		String username = (String) event.get(Fields.USERNAME);
		String destination = (String) event.get(Fields.DESTINATION);
		String reason = (String) event.get(Fields.REASON);
		
		System.out.println("Game ended by: " + username);
		System.out.println("Game over being sent to: " + destination);
		System.out.println("Reason: " + reason);
		
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
		
		System.out.println("Exiting GameOver Handler...");
		System.out.println("-----------------------------");
		System.out.println();
	}

}
