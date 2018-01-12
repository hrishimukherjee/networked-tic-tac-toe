package edu.carleton.COMP2601;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // Server Utils
    Connection connection;

    public ArrayList<String> players;
    private ArrayAdapter adapter;
    private PlayerAdapter playerAdapter;
    private TextView listStatus;
    private ProgressBar spinner;
    ListView listView;
    private int testToggle = Constants.HIDE_SPINNER;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Spinner
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        spinner.setVisibility(View.GONE);

        //Set the text at the bottom of the screen
        listStatus = (TextView) findViewById(R.id.listStatus);
        listStatus.setText(R.string.choose_opponent);

        //Initialize our list of players
        players = new ArrayList<String>();

        //Initializing custom adapter for List Activity
        playerAdapter = new PlayerAdapter(MainActivity.this, R.layout.list_row, players);

        //Initializing the list view
        listView = (ListView) findViewById(R.id.list_files);
        listView.setAdapter(playerAdapter);

        // SETUP CONNECTION
        connection = Connection.getInstance();
        connection.setUserListActivity(this);
        connection.startUp();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("MA/onItemClick", players.get(position));
                connection.playGameRequest(Constants.USERNAME, players.get(position));
            }
        });

        // Display Spinner
        toggleSpinner(Constants.DISPLAY_SPINNER);

        // MARK: testing
        //adding players to the list
        //players.add("Player 1");
        //players.add("Player 2");
    }

    public void onDestroy() {
        super.onDestroy();

        // Disconnect from the server on app termination
        Connection.getInstance().sendDisconnect(Constants.USERNAME);
    }

    /*
    This method is called whenever the server sends the client
    a message through the Connection class. The message contains
    an ArrayList of Strings that equate to the other connected
    users' aliases.
     */
    public void update(final ArrayList<String> updatedUsers)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerAdapter = null;//remove old player adapter
                players.clear();//remove old list of players
                players = updatedUsers;//set the new list of players
                //give the adapter our new list of players
                playerAdapter = new PlayerAdapter(MainActivity.this, R.layout.list_row, players);
                listView.setAdapter(playerAdapter);
            }
        });
    }


    /*
    This method is called when the user is connecting to the server.
    It displays a spinner while they are connecting and hides it
    when the user is finally connected to the server
     */
    public void toggleSpinner(int toggle)
    {
        if (toggle == Constants.DISPLAY_SPINNER)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    spinner.setVisibility(View.VISIBLE);
                }
            });
        }
        else if (toggle == Constants.HIDE_SPINNER)
        {
            runOnUiThread(new Runnable()
            {
                public void run()
                {
                    spinner.setVisibility(View.GONE);
                }
            });
        }
    }

    /*
    This method is called whenever the user successfully connects to the server
     */
    public void displayConnectedToast()
    {
        runOnUiThread(new Runnable()
        {
            public void run()
            {
                Toast.makeText(MainActivity.this, "Connected!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    /*
    This method is called whenever a user wants to play with you
     */
    public void displayAlertDialog(final String opponent)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle(opponent + " would like to play");
                // alert.setMessage("Message");

                alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("MA/reqDialog", "Game was accepted! Sending response...");

                        connection.playGameResponse(Constants.USERNAME, opponent, true);

                        Log.d("MA/reqDialog", "Starting new game activity...");
                        startGameActivity(Constants.PLAYER_O, opponent);
                    }
                });

                alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d("MA/reqDialog", "Game was declined! Sending response...");

                        connection.playGameResponse(Constants.USERNAME, opponent, false);
                    }
                });

                alert.show();
            }
        });
    }

    /*
    Start a new game activity.
     */
    public void startGameActivity(int playerSymbol, String opponentName) {
        Intent intent = new Intent(getApplicationContext(),
                GameActivity.class);

        intent.putExtra("playerSymbol", playerSymbol);
        intent.putExtra("opponentName", opponentName);

        startActivity(intent);
    }

    public void setDeclinedListStatus(final String opponent)
    {
        Log.d("MA/updateText", "Updating status text...");
        runOnUiThread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    listStatus.setText(opponent + " does not want to play");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }
}