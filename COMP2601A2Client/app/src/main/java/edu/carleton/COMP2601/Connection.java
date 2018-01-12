package edu.carleton.COMP2601;

import android.app.ListActivity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import communication.ConnectedResponseHandler;
import communication.DisconnectResponseHandler;
import communication.Event;
import communication.EventSource;
import communication.Fields;
import communication.GameOnHandler;
import communication.GameOverHandler;
import communication.JSONEventSource;
import communication.MoveMessageHandler;
import communication.PlayGameRequestHandler;
import communication.PlayGameResponseHandler;
import communication.Reactor;
import communication.ThreadWithReactor;
import communication.UpdatedUsersHandler;

/**
 * Created by Hrishi Mukherjee on 2017-02-15.
 */

public class Connection {

    // Server Specs
    private String address;
    private int port;

    // Network Components
    private Socket socket;

    // Reactor Components
    Reactor reactor;
    EventSource source;

    // Functions
    private static final String CONNECTION = "CONNECTION/";
    private static final String START_UP = "startUp";
    private static final String CONNECT = "connect";
    private static final String PLAY_GAME_REQ = "playReq";
    private static final String PLAY_GAME_RES = "playRes";
    private static final String GAME_ON = "gameOn";
    private static final String MOVE_MESSAGE = "moveMessage";
    private static final String GAME_OVER = "gameOver";
    private static final String DISCONNECT = "disconnect";

    // List Activity
    private MainActivity userListActivity;
    private GameActivity gameActivity;

    // Singleton
    private static Connection connection;

    private Connection(String address, int port,
                      MainActivity userListActivity) {
        this.userListActivity = userListActivity;

        this.address = address;
        this.port = port;
        startUp();
    }

    private Connection(String address, int port) {
        this.address = address;
        this.port = port;
    }

    public static Connection getInstance() {
        if(connection == null) {
            connection = new Connection(Constants.SERVER_ADDRESS,
                    Constants.SERVER_PORT);
        }

        return connection;
    }

    public GameActivity getGameActivity() { return this.gameActivity; }

    public void setGameActivity(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
    }

    public void setUserListActivity(MainActivity mainActivity) {
        this.userListActivity = mainActivity;
    }

    /**
     * Initialize the components
     * for the network connection.
     */
    public void startUp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(CONNECTION + START_UP, "********************");
                    Log.d(CONNECTION + START_UP, "Starting Up...");

                    Log.d(CONNECTION + START_UP, "Opening Socket...");
                    socket = new Socket(address, port);

                    Log.d(CONNECTION + START_UP, "Creating a Reactor...");
                    reactor = new Reactor();

                    Log.d(CONNECTION + START_UP, "Opening Up Streams...");
                    OutputStream os = socket.getOutputStream();
                    InputStream is = socket.getInputStream();
                    Log.d(CONNECTION + START_UP, "Streams Opened Successfully!");

                    Log.d(CONNECTION + START_UP, "Creating Event Source...");
                    source = new JSONEventSource(os, is);
                    Log.d(CONNECTION + START_UP, "Event Source Up and Running!");

                    Log.d(CONNECTION + START_UP, "Creating a new TWR...");
                    ThreadWithReactor twr = new ThreadWithReactor(source, reactor);

                    twr.register(Fields.TYPE_CONNECTED_RESPONSE,
                            new ConnectedResponseHandler(source, userListActivity));

                    twr.register(Fields.TYPE_USERS_UPDATED,
                            new UpdatedUsersHandler(source, userListActivity));

                    twr.register(Fields.TYPE_PLAY_GAME_REQUEST,
                            new PlayGameRequestHandler(source, userListActivity));

                    twr.register(Fields.TYPE_PLAY_GAME_RESPONSE,
                            new PlayGameResponseHandler(source, userListActivity));

                    twr.register(Fields.TYPE_GAME_ON,
                            new GameOnHandler(source));

                    twr.register(Fields.TYPE_MOVE_MESSAGE,
                            new MoveMessageHandler(source));

                    twr.register(Fields.TYPE_GAME_OVER,
                            new GameOverHandler(source));

                    twr.register(Fields.TYPE_DISCONNECT_RESPONSE,
                            new DisconnectResponseHandler(source));

                    Log.d(CONNECTION + START_UP, "Starting TWR...");
                    twr.start();
                    Log.d(CONNECTION + START_UP, "TWR Started!");
                    Log.d(CONNECTION + START_UP, "********************");
                    Log.d(CONNECTION + START_UP, "");

                    // Send initial CONNECT message to SERVER
                    connect(Constants.USERNAME);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Establishes a connection with the server
     * using the given username.
     *
     * @param username the username to be used to connect to
     *                 the server
     */
    public void connect(final String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(CONNECTION + CONNECT, "********************");
                    Log.d(CONNECTION + CONNECT, "Preparing Connection Message...");

                    // Send Connect Request
                    Event connect = new Event(Fields.TYPE_CONNECT_REQUEST, source);
                    connect.put(Fields.USERNAME, username);

                    Log.d(CONNECTION + CONNECT, "Sending Connection Message...");

                    // Send Connection Request to Server
                    source.putEvent(connect);

                    Log.d(CONNECTION + CONNECT, "********************");
                } catch(IOException e) {
                    e.printStackTrace();
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Sends a play game request to
     * another active user.
     *
     * @param otherUser the username of the user to send the request to
     */
    public void playGameRequest(final String thisUser, final String otherUser) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(CONNECTION + PLAY_GAME_REQ, "********************");
                    Log.d(CONNECTION + PLAY_GAME_REQ, "Preparing Play Game Request...");

                    // Send Play Game Request
                    Event playGameReq = new Event(Fields.TYPE_PLAY_GAME_REQUEST, source);
                    playGameReq.put(Fields.USERNAME, thisUser);
                    playGameReq.put(Fields.DESTINATION, otherUser);

                    Log.d(CONNECTION + PLAY_GAME_REQ, "Sending Play Game Request to: " + otherUser);

                    source.putEvent(playGameReq);

                    Log.d(CONNECTION + PLAY_GAME_REQ, "********************");
                } catch(IOException e) {
                    e.printStackTrace();
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void playGameResponse(final String thisUser, final String otherUser,
                                 final boolean playStatus) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(CONNECTION + PLAY_GAME_RES, "********************");
                    Log.d(CONNECTION + PLAY_GAME_RES, "Preparing Play Game Response...");

                    // Send Play Game Response
                    Event playGameRes = new Event(Fields.TYPE_PLAY_GAME_RESPONSE, source);
                    playGameRes.put(Fields.USERNAME, thisUser);
                    playGameRes.put(Fields.DESTINATION, otherUser);
                    playGameRes.put(Fields.PLAY_STATUS, playStatus);

                    Log.d(CONNECTION + PLAY_GAME_RES, "Sending Play Game Response to: " + otherUser);

                    source.putEvent(playGameRes);

                    Log.d(CONNECTION + PLAY_GAME_RES, "********************");
                } catch(IOException e) {
                    e.printStackTrace();
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendGameOn(final String username, final String destination) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(CONNECTION + GAME_ON, "********************");
                    Log.d(CONNECTION + GAME_ON, "Preparing GAME ON Request...");

                    // Send Game On Request
                    Event gameOn = new Event(Fields.TYPE_GAME_ON, source);

                    gameOn.put(Fields.USERNAME, username);
                    gameOn.put(Fields.DESTINATION, destination);

                    Log.d(CONNECTION + GAME_ON, "Sending GAME ON Request...");

                    source.putEvent(gameOn);

                    Log.d(CONNECTION + GAME_ON, "********************");
                } catch(IOException e) {
                    e.printStackTrace();
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMoveMessage(final String username, final String destination,
                                final int i, final int j) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(CONNECTION + MOVE_MESSAGE, "********************");
                    Log.d(CONNECTION + MOVE_MESSAGE, "Preparing MOVE MESSAGE Request...");

                    // Send Move Message Request
                    Event moveMessage = new Event(Fields.TYPE_MOVE_MESSAGE, source);

                    moveMessage.put(Fields.USERNAME, username);
                    moveMessage.put(Fields.DESTINATION, destination);
                    moveMessage.put(Fields.ROW, i);
                    moveMessage.put(Fields.COLUMN, j);

                    Log.d(CONNECTION + MOVE_MESSAGE, "Sending MOVE MESSAGE Request...");

                    source.putEvent(moveMessage);

                    Log.d(CONNECTION + MOVE_MESSAGE, "********************");
                } catch(IOException e) {
                    e.printStackTrace();
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendGameOver(final String username, final String destination,
                                final String reason) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(CONNECTION + GAME_OVER, "********************");
                    Log.d(CONNECTION + GAME_OVER, "Preparing GAME OVER Request...");

                    // Send Game Over Request
                    Event gameOver = new Event(Fields.TYPE_GAME_OVER, source);

                    gameOver.put(Fields.USERNAME, username);
                    gameOver.put(Fields.DESTINATION, destination);
                    gameOver.put(Fields.REASON, reason);

                    Log.d(CONNECTION + GAME_OVER, "Sending GAME OVER Request...");

                    source.putEvent(gameOver);

                    Log.d(CONNECTION + GAME_OVER, "********************");
                } catch(IOException e) {
                    e.printStackTrace();
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendDisconnect(final String username) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d(CONNECTION + DISCONNECT, "********************");
                    Log.d(CONNECTION + DISCONNECT, "Preparing DISCONNECT Request...");

                    // Send Disconnect Request
                    Event disconnect = new Event(Fields.TYPE_DISCONNECT_REQUEST, source);

                    disconnect.put(Fields.USERNAME, username);

                    Log.d(CONNECTION + DISCONNECT, "Sending DISCONNECT Request...");

                    source.putEvent(disconnect);

                    Log.d(CONNECTION + DISCONNECT, "********************");
                } catch(IOException e) {
                    e.printStackTrace();
                } catch(ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
