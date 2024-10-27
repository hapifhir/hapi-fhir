/*-
 * #%L
 * HAPI FHIR Storage api
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
package ca.uhn.fhir.util;

import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.Comparator;
import java.util.List;

/**
 *  Contains methods to sort resource meta fields that are sets (i.e., tags, security labels and profiles) in alphabetical order.
 *  It sorts the Coding type sets (tags and security labels) based on the (system, code) pair.
 *  The system field has higher priority on sorting than the code field so the Coding set will be sorted first by system
 *  and then by code for each system.
 */
public class MetaTagSorterAlphabetical implements IMetaTagSorter {

	private final Comparator<String> nullFirstStringComparator = Comparator.nullsFirst(Comparator.naturalOrder());

	private final Comparator<IBaseCoding> myCodingAlphabeticalComparator = Comparator.comparing(
					IBaseCoding::getSystem, nullFirstStringComparator)
			.thenComparing(IBaseCoding::getCode, nullFirstStringComparator);

	private final Comparator<IPrimitiveType<String>> myPrimitiveStringAlphabeticalComparator =
			Comparator.comparing(IPrimitiveType::getValue, nullFirstStringComparator);

	public void sortCodings(List<? extends IBaseCoding> theCodings) {
		theCodings.sort(myCodingAlphabeticalComparator);
	}

	public void sortPrimitiveStrings(List<? extends IPrimitiveType<String>> theList) {
		theList.sort(myPrimitiveStringAlphabeticalComparator);
	}

	public void sort(IBaseMetaType theMeta) {
		sortCodings(theMeta.getTag());
		sortCodings(theMeta.getSecurity());
		sortPrimitiveStrings(theMeta.getProfile());
	}
}
