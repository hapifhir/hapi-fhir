package ca.uhn.fhir.rest.server.provider;

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

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ResourceProviderFactory {
	private Set<IResourceProviderFactoryObserver> myObservers = Collections.synchronizedSet(new HashSet<>());
	private List<Supplier<Object>> mySuppliers = new ArrayList<>();

	public void addSupplier(@Nonnull Supplier<Object> theSupplier) {
		mySuppliers.add(theSupplier);
		myObservers.forEach(observer -> observer.update(theSupplier));
	}

	public void removeSupplier(@Nonnull Supplier<Object> theSupplier) {
		mySuppliers.remove(theSupplier);
		myObservers.forEach(observer -> observer.remove(theSupplier));
	}

	public List<Object> createProviders() {
		List<Object> retVal = new ArrayList<>();
		for (Supplier<Object> next : mySuppliers) {
			Object nextRp = next.get();
			if (nextRp != null) {
				retVal.add(nextRp);
			}
		}
		return retVal;
	}

	public void attach(IResourceProviderFactoryObserver theObserver) {
		myObservers.add(theObserver);
	}

	public void detach(IResourceProviderFactoryObserver theObserver) {
		myObservers.remove(theObserver);
	}
}
