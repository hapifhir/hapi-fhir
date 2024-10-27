/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr.discovery;

import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

public class PrefetchUrlList extends CopyOnWriteArrayList<String> {

	@Override
	public boolean add(String theElement) {
		for (String s : this) {
			if (s.equals(theElement)) return false;
			if (theElement.startsWith(s)) return false;
		}
		return super.add(theElement);
	}

	@Override
	public boolean addAll(Collection<? extends String> theAdd) {
		if (theAdd != null) {
			for (String s : theAdd) {
				add(s);
			}
		}
		return true;
	}
}
