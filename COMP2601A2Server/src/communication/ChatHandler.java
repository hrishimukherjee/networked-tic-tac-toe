package communication;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

import edu.carleton.COMP2601.GameServer;

public class ChatHandler implements EventHandler {
	
	EventSource source;
	
	public ChatHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		
	}

}
