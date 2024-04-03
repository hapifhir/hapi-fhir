package ca.uhn.fhir.rest.server.provider;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * This is a generic implementation of the <a href="https://refactoring.guru/design-patterns/observer">Observer Design Pattern</a>.
 * We use this to pass lists of beans from exporting Spring application contexts to importing Spring application contexts. We defer
 * resolving the observed beans via a Supplier to give the exporting context a chance to initialize the beans before they are used.
 * @param <T> the class of the Observer
 *
 * A typical usage pattern would be:
 * <ol>
 *           <li>Create Factory in exporter context.</li>
 *           <li>Add all the suppliers in the exporter context.</li>
 *           <li>Attach the importer to the factory</li>
 *           <li>Importer calls getSupplierResults() and processes all the beans</li>
 *           <li>Some other service beans may add more suppliers later as a part of their initialization and the observer handlers will process them accordingly</li>
 *           <li>Those other service beans should call removeSupplier in a @PreDestroy method so they are properly cleaned up if those services are shut down or restarted</li>
 * </ol>
 *
 */
public class FhirServerExtensionFactory<T extends IFhirServerExtensionFactoryObserver> {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirServerExtensionFactory.class);

	private Set<T> myObservers = Collections.synchronizedSet(new HashSet<>());

	private List<Supplier<Object>> mySuppliers = new ArrayList<>();

	/** Add a supplier and notify all observers
	 *
	 * @param theSupplier supplies the object to be observed
	 */
	public void addSupplier(@Nonnull Supplier<Object> theSupplier) {
		if (mySuppliers.add(theSupplier)) {
			myObservers.forEach(observer -> observer.update(theSupplier));
		}
	}

	/** Remove a supplier and notify all observers.  CAUTION, you might think that this code would work but it does not:
	 * <code>
	 * factory.addSupplier(() -> myBean);
	 * ...
	 * factory.removeSupplier(() -> myBean);
	 * </code>
	 * the removeSupplier in this example would fail because it is a different lambda instance from the first.  Instead,
	 * you need to store the supplier between the add and remove:
	 * <code>
	 * mySupplier = () -> myBean;
	 * factory.addSupplier(mySupplier);
	 * ...
	 * factory.removeSupplier(mySupplier);
	 * </code>
	 *
	 * @param theSupplier the supplier to be removed
	 */
	public void removeSupplier(@Nonnull Supplier<Object> theSupplier) {
		if (mySuppliers.remove(theSupplier)) {
			myObservers.forEach(observer -> observer.remove(theSupplier));
		} else {
			ourLog.warn("Failed to remove Fhir Extension", new RuntimeException());
		}
	}

	/**
	 * Attach an observer to this Factory.  This observer will be notified every time a supplier is added or removed.
	 * @param theObserver
	 */
	public void attach(T theObserver) {
		myObservers.add(theObserver);
	}

	/**
	 * Detach an observer from this Factory so it is no longer notified when suppliers are added and removed.
	 * @param theObserver
	 */
	public void detach(T theObserver) {
		myObservers.remove(theObserver);
	}

	/**
	 *
	 * @return a list of get() being called on all suppliers.
	 */
	protected List<Object> getSupplierResults() {
		List<Object> retVal = new ArrayList<>();
		for (Supplier<Object> next : mySuppliers) {
			Object nextRp = next.get();
			if (nextRp != null) {
				retVal.add(nextRp);
			}
		}
		return retVal;
	}
}
