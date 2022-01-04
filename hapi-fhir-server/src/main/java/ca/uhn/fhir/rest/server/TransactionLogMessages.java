package ca.uhn.fhir.rest.server;

/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TransactionLogMessages {
	private List<String> myMessages;
	private final String myTransactionGuid;

	private TransactionLogMessages(String theTransactionGuid) {
		myTransactionGuid = theTransactionGuid;
	}

	public static TransactionLogMessages createNew() {
		return new TransactionLogMessages(UUID.randomUUID().toString());
	}

	public static TransactionLogMessages createFromTransactionGuid(String theTransactionGuid) {
		return new TransactionLogMessages(theTransactionGuid);
	}

	private void addMessage(String theMessage) {
		if (myMessages == null) {
			myMessages = new ArrayList<>();
		}
		myMessages.add(theMessage);
	}

	public List<String> getValues() {
		return myMessages;
	}

	public static void addMessage(TransactionLogMessages theTransactionLogMessages, String theMessage) {
		if (theTransactionLogMessages == null) {
			return;
		}
		theTransactionLogMessages.addMessage(theMessage);
	}

	public String getTransactionGuid() {
		return myTransactionGuid;
	}
}
