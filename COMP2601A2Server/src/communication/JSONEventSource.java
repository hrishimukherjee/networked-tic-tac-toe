package communication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.Socket;
import java.util.Iterator;
import java.util.Map;

import javax.swing.plaf.synth.SynthSpinnerUI;

import org.json.JSONException;
import org.json.JSONObject;

import common.Message;

public class JSONEventSource implements EventSource {
	
	BufferedReader reader;
	BufferedWriter writer;
	
	Socket socket;
	
	/*
	 * Allows streams to be created: input followed by output
	 */
	public JSONEventSource(InputStream is, OutputStream os) throws IOException {
		InputStreamReader isr = new InputStreamReader(is);
		reader = new BufferedReader(isr);
		
		OutputStreamWriter osw = new OutputStreamWriter(os);
		writer = new BufferedWriter(osw);
	}
	
	/*
	 * Allows streams to be created: output followed by input
	 */
	public JSONEventSource(OutputStream os, InputStream is) throws IOException {
		OutputStreamWriter osw = new OutputStreamWriter(os);
		writer = new BufferedWriter(osw);
		
		InputStreamReader isr = new InputStreamReader(is);
		reader = new BufferedReader(isr);
	}
	/*
	 * Designed for server-side usage when a socket has been accepted
	 */
	public JSONEventSource(Socket s) throws IOException {
		this(s.getInputStream(), s.getOutputStream());
		this.socket = s;
	}
	
	@Override
	public Event getEvent() throws IOException, ClassNotFoundException {
		StringBuffer buffer = new StringBuffer();
		boolean done = false;
		
		// Read the input stream
		while(!done) {
			String line = reader.readLine();

			if(line == null || line.isEmpty()) {
				done = true;
			} else {
				buffer.append(line);
			}
		}
		
		// Create a JSON object
		JSONObject inbound = null;
		
		try {
			inbound = new JSONObject(buffer.toString());
		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		// Transform the JSON to an event
		Event event = jsonToEvent(inbound);
		
		return event;
	}
	
	@SuppressWarnings("unchecked")
	public void putEvent(Event e) throws IOException {
		// Transform Event to JSON
		JSONObject outbound = eventToJSON(e);
		
		System.out.println("Outbound: " + outbound);
		
		// Write the JSON to output stream
		writer.write(outbound.toString() + "\n");
		writer.write("\n");
		writer.flush();
	}

	public void close() {
		try {
			if (socket != null) 
				socket.close();
			if (writer != null)
				writer.close();
			if (reader != null) 
				reader.close();
			socket = null;
			writer = null;
			reader = null;
		} catch (IOException e) {
			// Fail quietly
		}
	}
	
	/**
	 * Creates a new Event using
	 * the data in the 
	 * provided JSON Message.
	 * 
	 * @param json the message to be converted to an Event
	 * @return a new Event object
	 */
	public Event jsonToEvent(JSONObject json) {
		Event event = null;
		
		try {
			event = new Event(json.getString(Fields.TYPE), this);
			
			// Put all key-value pairs from JSON to event map
			Iterator iterator = json.keys();
			
			while(iterator.hasNext()) {
				String key = (String) iterator.next();
				
				if(!key.equals(Fields.TYPE)) {
					try {
						Serializable value = (Serializable) json.get(key);
						
						event.put(key, value);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
		
		return event;
	}
	
	/**
	 * Transforms an event to a
	 * JSON Object.
	 * 
	 * @param event the event to use for the transformation
	 * @return a new JSON Object
	 */
	public JSONObject eventToJSON(Event event) {
		JSONObject json = new JSONObject();
		
		// Add message type to JSON
		try {
			json.put(Fields.TYPE, event.type);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// Add all hash map key-value pairs to JSON
		for(String key: event.getMap().keySet()) {
			if(!key.equals(Fields.TYPE)) {
				Serializable value = event.get(key);
				
				try {
					json.put(key, value);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		
		return json;
	}
}
