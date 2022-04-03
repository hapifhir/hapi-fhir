package ca.uhn.fhir.mdm.model;

/*-
 * #%L
 * HAPI FHIR - Master Data Management
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

import ca.uhn.fhir.mdm.api.IMdmLink;
import ca.uhn.fhir.mdm.api.MdmLinkEvent;
import ca.uhn.fhir.rest.server.TransactionLogMessages;

import java.util.ArrayList;
import java.util.List;

public class MdmTransactionContext {

	public enum OperationType {
		CREATE_RESOURCE,
		UPDATE_RESOURCE,
		SUBMIT_RESOURCE_TO_MDM,
		QUERY_LINKS,
		UPDATE_LINK,
		CREATE_LINK,
		DUPLICATE_GOLDEN_RESOURCES,
		NOT_DUPLICATE,
		MERGE_GOLDEN_RESOURCES,
		MANUAL_MERGE_GOLDEN_RESOURCES
	}

	/**
	 * Any MDM methods may add transaction log messages.
	 */
	private TransactionLogMessages myTransactionLogMessages;

	private OperationType myRestOperation;

	private String myResourceType;

	private List<IMdmLink> myMdmLinkEvents = new ArrayList<>();

	public TransactionLogMessages getTransactionLogMessages() {
		return myTransactionLogMessages;
	}

	public MdmTransactionContext() {
	}

	public MdmTransactionContext(OperationType theRestOperation) {
		myRestOperation = theRestOperation;
	}

	public MdmTransactionContext(TransactionLogMessages theTransactionLogMessages, OperationType theRestOperation) {
		this(theRestOperation);
		myTransactionLogMessages = theTransactionLogMessages;
	}

	public MdmTransactionContext(TransactionLogMessages theTransactionLogMessages, OperationType theRestOperation, String theResourceType) {
		this(theTransactionLogMessages, theRestOperation);
		setResourceType(theResourceType);
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

	public String getResourceType() {
		return myResourceType;
	}

	public void setResourceType(String myResourceType) {
		this.myResourceType = myResourceType;
	}

	public List<IMdmLink> getMdmLinks() {
		return myMdmLinkEvents;
	}

	public MdmTransactionContext addMdmLink(IMdmLink theMdmLinkEvent) {
		getMdmLinks().add(theMdmLinkEvent);
		return this;
	}

	public void setMdmLinks(List<IMdmLink> theMdmLinkEvents) {
		myMdmLinkEvents = theMdmLinkEvents;
	}
}
