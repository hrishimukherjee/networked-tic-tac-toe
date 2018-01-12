package communication;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.carleton.COMP2601.Constants;
import edu.carleton.COMP2601.MainActivity;
import edu.carleton.COMP2601.ServerObserver;

/**
 * Created by Hrishi Mukherjee on 2017-02-18.
 */

public class UpdatedUsersHandler implements EventHandler {

    EventSource source;
    MainActivity observer;

    public UpdatedUsersHandler(EventSource source,
                               MainActivity observer) {
        this.source = source;
        this.observer = observer;
    }

    public void handleEvent(Event event) {
        Log.d("UpdateUsers/handleEvent", "");
        Log.d("UpdateUsers/handleEvent", "--------------------------------");
        Log.d("UpdateUsers/handleEvent", "Server Response: " + event.type);

        ArrayList<String> connectedClients =
                (ArrayList<String>) event.get(Fields.CONNECTED_CLIENTS);

        Log.d("UpdateUsers/handleEvent", "Sending Updated Clients to Main Activity: " +
                connectedClients);

        observer.update(connectedClients);

        Log.d("UpdateUsers/handleEvent", "Exiting Handler...");
        Log.d("UpdateUsers/handleEvent", "--------------------------------");
    }


}
