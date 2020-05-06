package ca.uhn.fhir.jpa.model.util;

import ca.uhn.fhir.jpa.model.cross.ResourcePersistentId;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TransactionDetails {

	private final Date myTransactionDate;
	private Map<IIdType, ResourcePersistentId> myPreResolvedResourceIds = Collections.emptyMap();

	/**
	 * Constructor
	 */
	public TransactionDetails() {
		myTransactionDate = new Date();
	}

	/**
	 * Constructor
	 */
	public TransactionDetails(Date theTransactionDate) {
		myTransactionDate = theTransactionDate;
	}

	public Map<IIdType, ResourcePersistentId> getPreResolvedResourceIds() {
		return myPreResolvedResourceIds;
	}

	public void addPreResolvedResourceId(IIdType theResourceId, ResourcePersistentId thePersistentId) {
		assert theResourceId != null;
		assert thePersistentId != null;

		if (myPreResolvedResourceIds.isEmpty()) {
			myPreResolvedResourceIds = new HashMap<>();
		}
		myPreResolvedResourceIds.put(theResourceId, thePersistentId);
	}

	public Date getTransactionDate() {
		return myTransactionDate;
	}

}
