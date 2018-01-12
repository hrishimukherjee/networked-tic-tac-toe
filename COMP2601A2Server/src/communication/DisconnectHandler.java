package communication;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.carleton.COMP2601.GameServer;

public class DisconnectHandler implements EventHandler {
	
	EventSource source;
	
	public DisconnectHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println("DisconnectHandler/handleEvent()");
		
		String username = (String) event.get(Fields.USERNAME);
		
		System.out.println("Disconnection request by: " + username);
		
		// Retrieve Thread of Client to Disconnect
		ThreadWithReactor disconnectThread = GameServer.getClientThread(username);
		System.out.println("Client Thread: " + disconnectThread);
		
		// Remove client from list of active clients
		System.out.println("Removing client from list...");
		GameServer.removeClient(username);
		
		// Reply to client
		System.out.println("Replying back to client...");
		Event response = new Event(Fields.TYPE_DISCONNECT_RESPONSE, source);
		
		try {
			disconnectThread.getEventSource().putEvent(response);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Send USERS UPDATED message to all users
		System.out.println("Creating Users Updated Event...");
		Event updatedUsersResponse = new Event(Fields.TYPE_USERS_UPDATED, source);
		
		List<String> connectedClients = GameServer.getAllConnectedClients();
		
		updatedUsersResponse.put(Fields.CONNECTED_CLIENTS, (Serializable) connectedClients);
		
		System.out.println("Sending Users Updated Message...");
		
		// Send the USERS UPDATED message to each client thread
		for(String client: connectedClients) {
			ThreadWithReactor clientThread = GameServer.getClientThread(client);
			
			try {
				clientThread.getEventSource().putEvent(updatedUsersResponse);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Exiting Disconnect Handler...");
		System.out.println("-----------------------------");
		System.out.println();
	}

}
