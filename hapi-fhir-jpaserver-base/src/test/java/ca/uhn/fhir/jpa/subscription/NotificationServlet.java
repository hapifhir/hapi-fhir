package ca.uhn.fhir.jpa.subscription;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Receives subscription notification without payloads.
 */
public class NotificationServlet extends HttpServlet {
	private static final long serialVersionUID = 5957950857980374719L;

	private final AtomicLong receivedNotificationCount = new AtomicLong();

	private final List<String> receivedAuthorizationHeaders = Collections.synchronizedList(new ArrayList<>());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		receivedNotificationCount.incrementAndGet();
		receivedAuthorizationHeaders.add(req.getHeader("Authorization"));
	}

	public long getReceivedNotificationCount() {
		return receivedNotificationCount.get();
	}

	public List<String> getReceivedAuthorizationHeaders() {
		return receivedAuthorizationHeaders;
	}

	public void reset() {
		receivedNotificationCount.set(0);
		receivedAuthorizationHeaders.clear();
	}
}
