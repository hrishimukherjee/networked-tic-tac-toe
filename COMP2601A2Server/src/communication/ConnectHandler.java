package communication;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import edu.carleton.COMP2601.GameServer;

public class ConnectHandler implements EventHandler {
	
	EventSource source;
	
	public ConnectHandler(EventSource source) {
		this.source = source;
	}
	
	public void handleEvent(Event event) {
		System.out.println();
		System.out.println("-----------------------------");
		System.out.println("ConnectHandler/handleEvent()");
		
		String username = (String) event.get(Fields.USERNAME);
		
		System.out.println("Connection requested by: " + username);
		
		// Adding client to list of clients
		GameServer.addClient(username, (ThreadWithReactor) Thread.currentThread());
		
		// Send CONNECTED RESPONSE to the one client who just connected
		System.out.println("Creating Connected Response Event...");
		Event connectedResponse = new Event(Fields.TYPE_CONNECTED_RESPONSE, source);
		connectedResponse.put(Fields.USERNAME, username);
		
		System.out.println("Sending Connected Response Message to: " + username);
		try {
			source.putEvent(connectedResponse);
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
		
		System.out.println("Exiting ConnectHandler...");
		System.out.println("-----------------------------");
		System.out.println();
	}

}
