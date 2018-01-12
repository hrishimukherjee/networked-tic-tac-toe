package communication;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.carleton.COMP2601.GameServer;

public class PlayGameRequestHandler implements EventHandler {
	
	EventSource source;
	
	public PlayGameRequestHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println("PlayGameRequestHandler/handleEvent()");
		
		String username = (String) event.get(Fields.USERNAME);
		String destination = (String) event.get(Fields.DESTINATION);
		
		System.out.println("Play Game requested by: " + username);
		System.out.println("Play Game request being sent to: " + destination);
		
		// Retrieve Client
		ThreadWithReactor destinationThread = GameServer.getClientThread(destination);
		System.out.println("Destination Thread: " + destinationThread);
		
		// Send PLAY GAME REQUEST to the destination user
		try {
			destinationThread.getEventSource().putEvent(event);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Exiting ConnectHandler...");
		System.out.println("-----------------------------");
		System.out.println();
	}

}
