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

import ca.uhn.fhir.rest.api.EncodingEnum;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ClientEndpoint
public class SocketImplementation {

	private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(SocketImplementation.class);
	private String myCriteria;
	protected String myError;
	protected boolean myGotBound;
	private List<String> myMessages = Collections.synchronizedList(new ArrayList<>());
	protected int myPingCount;
	protected String mySubsId;
	private Session session;

	public SocketImplementation(String theCriteria, EncodingEnum theEncoding) {
		myCriteria = theCriteria;
	}

	public List<String> getMessages() {
		return myMessages;
	}

	public void keepAlive() {
		if (this.session != null) {
			try {
				session.getBasicRemote().sendText("keep alive");
			} catch (Throwable t) {
				ourLog.error("Failure", t);
			}
		}
	}

	/**
	 * This method is executed when the client is connecting to the server.
	 * In this case, we are sending a message to create the subscription dynamically
	 *
	 * @param session
	 */
	@OnOpen
	public void onConnect(Session session) {
		ourLog.info("Got connect: {}", session);
		this.session = session;
		try {
			String sending = "bind " + myCriteria;
			ourLog.info("Sending: {}", sending);
			session.getBasicRemote().sendText(sending);

			ourLog.info("Connection: DONE");
		} catch (Throwable t) {
			t.printStackTrace();
			ourLog.error("Failure", t);
		}
	}

	/**
	 * This is the message handler for the client
	 *
	 * @param theMsg
	 */
	@OnMessage
	public void onMessage(String theMsg) {
		ourLog.info("Got msg: " + theMsg);
		myMessages.add(theMsg);

		if (theMsg.startsWith("bound ")) {
			myGotBound = true;
			mySubsId = (theMsg.substring("bound ".length()));
		} else if (myGotBound && theMsg.startsWith("add " + mySubsId + "\n")) {
			String text = theMsg.substring(("add " + mySubsId + "\n").length());
			ourLog.info("text: " + text);
		} else {
			myError = "Unexpected message: " + theMsg;
		}
	}
}
