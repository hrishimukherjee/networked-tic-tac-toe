package communication;

import java.io.IOException;

public interface EventSource {
	public Event getEvent() throws IOException, ClassNotFoundException;
	public void putEvent(Event e) throws IOException, ClassNotFoundException;
}
