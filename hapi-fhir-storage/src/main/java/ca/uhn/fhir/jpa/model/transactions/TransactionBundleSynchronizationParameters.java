package ca.uhn.fhir.jpa.model.transactions;

import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;

import java.util.List;

public class TransactionBundleSynchronizationParameters {

	private final IBaseBundle myBundle;

	private final List<IBase> myBundleEntries;

	public TransactionBundleSynchronizationParameters(IBaseBundle theBundle, List<IBase> theBundleEntries) {
		myBundle = theBundle;
		myBundleEntries = theBundleEntries;
	}

	public IBaseBundle getBundle() {
		return myBundle;
	}

	public List<IBase> getBundleEntries() {
		return myBundleEntries;
	}
}
