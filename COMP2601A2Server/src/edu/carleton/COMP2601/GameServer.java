package edu.carleton.COMP2601;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import common.Message;
import communication.ChatHandler;
import communication.ConnectHandler;
import communication.DisconnectHandler;
import communication.Event;
import communication.EventHandler;
import communication.EventSource;
import communication.EventSourceImpl;
import communication.Fields;
import communication.GameOnHandler;
import communication.GameOverHandler;
import communication.JSONEventSource;
import communication.MoveMessageHandler;
import communication.PlayGameRequestHandler;
import communication.PlayGameResponseHandler;
import communication.Reactor;
import communication.ThreadWithReactor;

/**
 * The Main Server Class which facilitates a multi-client
 * game of Tic-Tac-Toe.
 * 
 * @author Hrishi Mukherjee (100888108)
 *
 */

public class GameServer {
	
	/*
	 *  Server Specs
	 */
	public static int PORT = 7004;
	public static boolean RUNNING = true;
	
	/*
	 *  Server Components
	 */
	public static ServerSocket listener;
	public static Reactor reactor;
	public static EventSource eventSource;
	
	public static Socket socket;
	public static ObjectInputStream ois;
	public static ObjectOutputStream oos;
	
	/*
	 * Clients
	 */
	private static ConcurrentHashMap<String, ThreadWithReactor> clients;
	
	public static void main(String[] args) {
		System.out.println("********************");
		System.out.println("WELCOME TO THE TIC TAC TOE GAME SERVER!");
		
		GameServer server = new GameServer();
		
		System.out.println("Initializing Game Server...");
		System.out.println();
		
		server.init();
		
		System.out.println("Firing Up Server...");
		System.out.println();
		
		server.run();
	}
	
	/**
	 * Runs the main server loop.
	 * The loop sits and waits for connections.
	 * When a connection is requested by a client,
	 * the server creates a new thread with reactor 
	 * and assigns it to the respective client.
	 */
	public void run() {
		System.out.println("********************");
		System.out.println("GameServer/run()");
		
		while(RUNNING) {
			// Create Event Source
			try {
				System.out.println("Waiting for connections...");
				eventSource = new JSONEventSource(listener.accept());
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			System.out.println("Creating TWR...");
			// Create a TWR
			ThreadWithReactor twr = new ThreadWithReactor(eventSource, reactor);

			System.out.println("Registering Handlers...");
			
			twr.register(Fields.TYPE_CONNECT_REQUEST, new ConnectHandler(eventSource));
			twr.register(Fields.TYPE_PLAY_GAME_REQUEST, new PlayGameRequestHandler(eventSource));
			twr.register(Fields.TYPE_PLAY_GAME_RESPONSE, new PlayGameResponseHandler(eventSource));
			twr.register(Fields.TYPE_GAME_ON, new GameOnHandler(eventSource));
			twr.register(Fields.TYPE_MOVE_MESSAGE, new MoveMessageHandler(eventSource));
			twr.register(Fields.TYPE_GAME_OVER, new GameOverHandler(eventSource));
			twr.register(Fields.TYPE_DISCONNECT_REQUEST, new DisconnectHandler(eventSource));
			
			System.out.println("Starting TWR...");
			
			// Start TWR
			twr.start();
		}
		
		System.out.println("Closing Up Server... Bye!");
		System.out.println("********************");
	}
	
	/**
	 * Initializes the server
	 * components.
	 */
	public void init() {
		System.out.println("********************");
		System.out.println("GameServer/init()");
		
		System.out.println("Creating a new reactor...");
		reactor = new Reactor();
		
		System.out.println("Initializing clients map...");
		clients = new ConcurrentHashMap<String, ThreadWithReactor>();
		
		System.out.println("Creating server socket...");
		try {
			listener = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Done Initializing!");
		System.out.println("********************");
		System.out.println();
	}
	
	/**
	 * Adds a new client with the given
	 * username and thread with reactor.
	 * 
	 * @param username the username of the client
	 * @param twr the thread associated with the client
	 */
	public static void addClient(String username, ThreadWithReactor twr) {
		System.out.println();
		System.out.println("********************");
		System.out.println("GameServer/addClient()");
		System.out.println("Adding " + username + "--> " + twr);
		
		clients.put(username, twr);
		
		System.out.println(username + " successfully added!");
		System.out.println("Updated Clients: " + clients);
		System.out.println("********************");
		System.out.println();
	}
	
	/**
	 * Removes the given username from the
	 * clients list.
	 * 
	 * @param username the client to remove
	 * @return true if removed, false if not found
	 */
	public static boolean removeClient(String username) {
		System.out.println();
		System.out.println("********************");
		System.out.println("GameServer/removeClient()");
		
		if(clients.containsKey(username)) {
			System.out.println("Removing " + username + "...");
			
			clients.remove(username);
			
			System.out.println(username + " successfully removed!");
			System.out.println("Updated Clients: " + clients);
			System.out.println("********************");
			System.out.println();
		} else {
			System.out.println(username + " does not exist...");
			System.out.println("Clients unchanged: " + clients);
			System.out.println("********************");
			System.out.println();
			
			return false;
		}
		
		
		
		return true;
	}
	
	/**
	 * Returns the thread associated with the
	 * given username.
	 * 
	 * @param username the username of the client
	 * @return the associated thread with the client
	 */
	public static ThreadWithReactor getClientThread(String username) {
		System.out.println();
		System.out.println("********************");
		System.out.println("GameServer/getClientThread()");
		
		if(clients.containsKey(username)) {
			System.out.println("Returning " + username + "'s thread...");
			System.out.println("********************");
			System.out.println();
			
			return clients.get(username);
		} else {
			System.out.println(username + " does not exist...");
			System.out.println("********************");
			System.out.println();
			
			return null;
		}
	}
	
	public static List<String> getAllConnectedClients() {
		System.out.println();
		System.out.println("********************");
		System.out.println("GameServer/getAllConnectedClients()");
		
		List<String> connectedClients = new ArrayList<String>();
		
		System.out.println("Creating list of clients...");
		Enumeration<String> enumeration = clients.keys();
		
		while(enumeration.hasMoreElements()) {
			connectedClients.add(enumeration.nextElement());
		}
		
		System.out.println("List of connected clients: " + connectedClients);
		System.out.println("********************");
		System.out.println();
		
		return connectedClients;
	}
	
}
