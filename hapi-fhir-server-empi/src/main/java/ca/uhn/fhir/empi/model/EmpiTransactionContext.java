package ca.uhn.fhir.empi.model;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
 * %%
 * Copyright (C) 2014 - 2021 University Health Network
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

import ca.uhn.fhir.rest.server.TransactionLogMessages;

public class EmpiTransactionContext {

	/**
	 * Any EMPI methods may add transaction log messages.
	 */
	private TransactionLogMessages myTransactionLogMessages;

	private OperationType myRestOperation;


	public enum OperationType {
		CREATE_RESOURCE,
		UPDATE_RESOURCE,
		SUBMIT_RESOURCE_TO_EMPI,
		QUERY_LINKS,
		UPDATE_LINK,
		DUPLICATE_PERSONS,
		NOT_DUPLICATE,
		MERGE_PERSONS
	}
	public TransactionLogMessages getTransactionLogMessages() {
		return myTransactionLogMessages;
	}

	public EmpiTransactionContext() {
	}

	public EmpiTransactionContext(TransactionLogMessages theTransactionLogMessages, OperationType theRestOperation) {
		myTransactionLogMessages = theTransactionLogMessages;
		myRestOperation = theRestOperation;
	}

	public void addTransactionLogMessage(String theMessage) {
		if (myTransactionLogMessages == null) {
			return;
		}
		this.myTransactionLogMessages.addMessage(myTransactionLogMessages, theMessage);
	}

	public OperationType getRestOperation() {
		return myRestOperation;
	}

	public void setTransactionLogMessages(TransactionLogMessages theTransactionLogMessages) {
		myTransactionLogMessages = theTransactionLogMessages;
	}

	public void setRestOperation(OperationType theRestOperation) {
		myRestOperation = theRestOperation;
	}
}
