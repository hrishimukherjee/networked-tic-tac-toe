package communication;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.carleton.COMP2601.GameServer;

public class PlayGameResponseHandler implements EventHandler {
	
	EventSource source;
	
	public PlayGameResponseHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println("PlayGameResponseHandler/handleEvent()");
		
		String username = (String) event.get(Fields.USERNAME);
		String destination = (String) event.get(Fields.DESTINATION);
		boolean playStatus = (boolean) event.get(Fields.PLAY_STATUS);
		
		System.out.println("Play Game response by: " + username);
		System.out.println("Play Game response being sent to: " + destination);
		System.out.println("Play Status: " + playStatus);
		
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
		
		System.out.println("Exiting PlayGameResponse Handler...");
		System.out.println("-----------------------------");
		System.out.println();
	}

}
