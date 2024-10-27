/*-
 * #%L
 * HAPI FHIR - Command Line Client - API
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
package ca.uhn.fhir.cli;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.primitive.IdDt;
import jakarta.websocket.ClientEndpoint;
import jakarta.websocket.ContainerProvider;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.net.URI;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class WebsocketSubscribeCommand extends BaseCommand {
	private static final org.slf4j.Logger LOG_RECV = org.slf4j.LoggerFactory.getLogger("websocket.RECV");
	private static final org.slf4j.Logger LOG_SEND = org.slf4j.LoggerFactory.getLogger("websocket.SEND");
	// TODO: Don't use qualified names for loggers in HAPI CLI.
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(WebsocketSubscribeCommand.class);

	private boolean myQuit;
	private Session mySession;

	@Override
	public String getCommandDescription() {
		return "Opens a websocket client connection to a server for the purposes of binding to a Subscription, and then outputs the results";
	}

	@Override
	public String getCommandName() {
		return "subscription-client";
	}

	@Override
	public Options getOptions() {
		Options options = new Options();
		options.addOption("t", "target", true, "Target URL, e.g. \"ws://fhirtest.uhn.ca/websocket/dstu2\"");
		options.addOption("i", "subscriptionid", true, "Subscription ID, e.g. \"5235\"");
		return options;
	}

	@Override
	public void run(CommandLine theCommandLine) throws ParseException {
		String target = theCommandLine.getOptionValue("t");
		if (isBlank(target) || (!target.startsWith("ws://") && !target.startsWith("wss://"))) {
			throw new ParseException(
					Msg.code(1536) + "Target (-t) needs to be in the form \"ws://foo\" or \"wss://foo\"");
		}

		IdDt subsId = new IdDt(theCommandLine.getOptionValue("i"));

		SimpleEchoSocket socket = new SimpleEchoSocket(subsId.getIdPart());
		try {
			URI echoUri = new URI(target);
			ourLog.info("Connecting to : {}", echoUri);

			WebSocketContainer container = ContainerProvider.getWebSocketContainer();
			mySession = container.connectToServer(socket, echoUri);

			while (!myQuit) {
				Thread.sleep(500L);
			}

			ourLog.info("Shutting down websocket client");
		} catch (Exception e) {
			throw new CommandFailureException(Msg.code(1537) + e);
		} finally {
			try {
				if (mySession != null) {
					mySession.close();
				}
			} catch (Exception e) {
				ourLog.error("Failure", e);
			}
		}
	}

	/**
	 * Basic Echo Client Socket
	 */
	@ClientEndpoint
	public class SimpleEchoSocket {

		private String mySubsId;

		@SuppressWarnings("unused")
		private Session session;

		public SimpleEchoSocket(String theSubsId) {
			mySubsId = theSubsId;
		}

		@OnClose
		public void onClose(int statusCode, String reason) {
			ourLog.info("Received CLOSE status={} reason={}", statusCode, reason);
		}

		@OnOpen
		public void onConnect(Session theSession) {
			ourLog.info("Successfully connected");
			this.session = theSession;
			try {
				String sending = "bind " + mySubsId;
				LOG_SEND.info("{}", sending);
				theSession.getBasicRemote().sendText(sending);
			} catch (Throwable t) {
				ourLog.error("Failure", t);
				myQuit = true;
			}
		}

		@OnError
		public void onError(Throwable theError) {
			ourLog.error("Websocket error: ", theError);
			myQuit = true;
		}

		@OnMessage
		public void onMessage(String theMsg) {
			LOG_RECV.info("{}", theMsg);
		}
	}
}
