/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.provider;

import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * This is a generic implementation of the <a href="https://refactoring.guru/design-patterns/observer">Observer Design Pattern</a>.
 * We use this to pass sets of beans from exporting Spring application contexts to importing Spring application contexts. We defer
 * resolving the observed beans via a Supplier to give the exporting context a chance to initialize the beans before they are used.
 * @param <T> the class of the Observer
 * <p>
 * A typical usage pattern would be:
 * <ol>
 *           <li>Create {@link ObservableSupplierSet} in exporter context.</li>
 *           <li>Add all the suppliers in the exporter context.</li>
 *           <li>Attach the importer to the {@link ObservableSupplierSet}</li>
 *           <li>Importer calls {@link ObservableSupplierSet#getSupplierResults} and processes all the beans</li>
 *           <li>Some other service beans may add more suppliers later as a part of their initialization and the observer handlers will process them accordingly</li>
 *           <li>Those other service beans should call {@link ObservableSupplierSet#removeSupplier(Supplier)} in a @PreDestroy method so they are properly cleaned up if those services are shut down or restarted</li>
 * </ol>
 *
 */
public class ObservableSupplierSet<T extends IObservableSupplierSetObserver> {
	private static final Logger ourLog = LoggerFactory.getLogger(ObservableSupplierSet.class);

	private final Set<T> myObservers = Collections.synchronizedSet(new HashSet<>());

	private final Set<Supplier<Object>> mySuppliers = new LinkedHashSet<>();

	/** Add a supplier and notify all observers
	 *
	 * @param theSupplier supplies the object to be observed
	 */
	public void addSupplier(@Nonnull Supplier<Object> theSupplier) {
		if (mySuppliers.add(theSupplier)) {
			myObservers.forEach(observer -> observer.update(theSupplier));
		}
	}

	/** Remove a supplier and notify all observers.  CAUTION, you might think that this code would work, but it does not:
	 * <code>
	 * observableSupplierSet.addSupplier(() -> myBean);
	 * ...
	 * observableSupplierSet.removeSupplier(() -> myBean);
	 * </code>
	 * the removeSupplier in this example would fail because it is a different lambda instance from the first.  Instead,
	 * you need to store the supplier between the add and remove:
	 * <code>
	 * mySupplier = () -> myBean;
	 * observableSupplierSet.addSupplier(mySupplier);
	 * ...
	 * observableSupplierSet.removeSupplier(mySupplier);
	 * </code>
	 *
	 * @param theSupplier the supplier to be removed
	 */
	public void removeSupplier(@Nonnull Supplier<Object> theSupplier) {
		if (mySuppliers.remove(theSupplier)) {
			myObservers.forEach(observer -> observer.remove(theSupplier));
		} else {
			ourLog.warn("Failed to remove supplier", new RuntimeException());
		}
	}

	/**
	 * Attach an observer to this observableSupplierSet.  This observer will be notified every time a supplier is added or removed.
	 * @param theObserver the observer to be notified
	 */
	public void attach(T theObserver) {
		myObservers.add(theObserver);
	}

	/**
	 * Detach an observer from this observableSupplierSet, so it is no longer notified when suppliers are added and removed.
	 * @param theObserver the observer to be removed
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
