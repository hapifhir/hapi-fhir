package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.context.BaseRuntimeChildDefinition;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.jpa.model.transactions.TransactionBundleSynchronizationParameters;
import ca.uhn.fhir.util.TerserUtil;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TransactionSynchronizer {

	private static class Entry {
		private String myKey;
		private IBase myBase;

		public String getKey() {
			return myKey;
		}

		public void setKey(String theKey) {
			myKey = theKey;
		}

		public IBase getBase() {
			return myBase;
		}

		public void setBase(IBase theBase) {
			myBase = theBase;
		}
	}

	private final FhirContext myContext;

	@SuppressWarnings("rawtypes")
	private final ITransactionProcessorVersionAdapter myAdapter;

	private final ConcurrentHashMap<String, Set<IBaseBundle>> myKeyToListOfBundles = new ConcurrentHashMap<>();

	@SuppressWarnings("rawtypes")
	public TransactionSynchronizer(
		FhirContext theFhirContext,
		ITransactionProcessorVersionAdapter theAdapter
	) {
		myContext = theFhirContext;
		myAdapter = theAdapter;
	}

	/**
	 * Synchronize the transaction iff the transaction bundle contains
	 * an entry that duplicates an entry in another already processing
	 * transaction bundle (on another thread).
	 * <p>
	 * Because transaction bundles are processed in a single transaction
	 * (ie, all contained resources or none), we cannot sync on individual
	 * resources, but must sync on the entire transaction bundle itself!
	 * <p>
	 * For small bundles, they are unlikely to ever be similar.
	 * But large bundles with many entires are much more likely to run into
	 * multi-thread issues.
	 * <p>
	 * Since we cannot break up the transaction bundle without breaking up the
	 * transaction itself, we will try our best to sync the transaction on the
	 * bundle only when it's absolutely necessary (ie, another bundle is currently
	 * being processes that contains duplicated resources).
	 * <p>
	 * @param theParameters - parameters containing the bundle and the entries
	 * @param theCallback - the callback to invoke the actual transaction processing
	 * @return - returns the results of the callback it processes
	 */
	public EntriesToProcessMap syncTransactionIfNecessary(
		TransactionBundleSynchronizationParameters theParameters,
		Supplier<EntriesToProcessMap> theCallback
	) {
		IBaseBundle bundle = theParameters.getBundle();
		List<IBase> baseEntries = theParameters.getBundleEntries();

		List<Entry> entries = baseEntries.stream().map(e -> {
			Entry entry = new Entry();
			entry.setBase(e);
			entry.setKey(getKeyFromEntry(e));
			return entry;
		}).collect(Collectors.toList());

		synchronized (containsAny(entries, bundle) ? this : new Object()) {
			// process transaction
			EntriesToProcessMap map = theCallback.get();

			// remove from map
			removeBundleFromLists(entries, bundle);
			return map;
		}
	}

	private boolean containsAny(List<Entry> theBaseResources, IBaseBundle theBundle) {
		boolean contains = false;
		for (Entry entry : theBaseResources) {
			String key = entry.getKey();

			System.out.println("\n" + key);
			if (addBundleToMapAndReturnIfAlreadyContained(key, theBundle)) {
				contains = true;
				// we still continue, because we need to add all entries to map
			}
		}

		return contains;
	}

	private void removeBundleFromLists(List<Entry> theEntries, IBaseBundle theBundle) {
		for (Entry entry : theEntries) {
			String key = entry.getKey();
			// should always be true, but just to be safe
			if (myKeyToListOfBundles.contains(key)) {
				myKeyToListOfBundles.get(key).remove(theBundle);
			}
		}
	}

	private boolean addBundleToMapAndReturnIfAlreadyContained(String theKey, IBaseBundle theBundle) {
		boolean contains = false;

		myKeyToListOfBundles.putIfAbsent(theKey, new HashSet<>());
		Set<IBaseBundle> bundles = myKeyToListOfBundles.get(theKey);
		if (!bundles.isEmpty()) {
			contains = true;
		}
		bundles.add(theBundle);

		return contains;
	}

	@SuppressWarnings("unchecked")
	private String getKeyFromEntry(IBase theEntry) {
		// ideally this would be in the IBase class directly
		// allowing each resource type to create their own unique key
		// based on what fields are unique in the db

		StringBuilder sb = new StringBuilder();
		IBaseResource resource = myAdapter.getResource(theEntry);
		RuntimeResourceDefinition resdef = myContext.getResourceDefinition(resource);
		for (BaseRuntimeChildDefinition child : resdef.getChildren()) {
			if (child.getMin() > 0) {
				// required field?
				List<IBase> values = TerserUtil.getValues(myContext, resource, child.getElementName());

				// we'll stringify the required values
				// (we have no idea if these are unique fields in the db
				// but they are at least going to be required fields
				if (values != null && !values.isEmpty()
						&& IPrimitiveType.class.isAssignableFrom(values.get(0).getClass())) {
					IPrimitiveType<?> first = (IPrimitiveType<?>) values.get(0);
					sb.append(first.getValueAsString());
				}
			}
		}

		return sb.toString();
	}
}
