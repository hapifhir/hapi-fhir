/*
 *  Copyright 2017 Cognitive Medical Systems, Inc (http://www.cognitivemedicine.com).
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  @author Jeff Chung
 */

package ca.uhn.fhir.jpa.demo.subscription;

import ca.uhn.fhir.rest.server.EncodingEnum;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;

@WebSocket
public class SocketImplementation {

    private String myCriteria;
    private Session session;

    protected String myError;
    protected boolean myGotBound;
    protected int myPingCount;
    protected String mySubsId;

    private static final Logger ourLog = org.slf4j.LoggerFactory.getLogger(SocketImplementation.class);

    public SocketImplementation(String theCriteria, EncodingEnum theEncoding) {
        myCriteria = theCriteria;
    }

    /**
     * This method is executed when the client is connecting to the server.
     * In this case, we are sending a message to create the subscription dynamiclly
     *
     * @param session
     */
    @OnWebSocketConnect
    public void onConnect(Session session) {
        ourLog.info("Got connect: {}", session);
        this.session = session;
        try {
            String sending = "bind " + myCriteria;
            ourLog.info("Sending: {}", sending);
            session.getRemote().sendString(sending);

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
    @OnWebSocketMessage
    public void onMessage(String theMsg) {
        ourLog.info("Got msg: " + theMsg);

        if (theMsg.startsWith("bound ")) {
            myGotBound = true;
            mySubsId = (theMsg.substring("bound ".length()));
            myPingCount++;
        } else if (myGotBound && theMsg.startsWith("add " + mySubsId + "\n")) {
            String text = theMsg.substring(("add " + mySubsId + "\n").length());
            ourLog.info("text: " + text);
            myPingCount++;
        } else {
            myError = "Unexpected message: " + theMsg;
        }
    }

    public void keepAlive() {
        if (this.session != null) {
            try {
                session.getRemote().sendString("keep alive");
            } catch (Throwable t) {
                ourLog.error("Failure", t);
            }
        }
    }
}