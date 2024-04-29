/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.subscription;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
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
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		receivedNotificationCount.incrementAndGet();
		receivedAuthorizationHeaders.add(req.getHeader("Authorization"));
	}

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
