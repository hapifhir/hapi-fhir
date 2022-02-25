package ca.uhn.fhir.test.utilities;

/*-
 * #%L
 * HAPI FHIR Test Utilities
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchTestUtil {

	private SearchTestUtil() {
	}

	public static List<IIdType> toUnqualifiedVersionlessIds(IBaseBundle theFound) {
		FhirContext ctx = FhirContext.forCached(theFound.getStructureFhirVersionEnum());

		List<IIdType> retVal = new ArrayList<>();
		for (IBaseResource next : BundleUtil.toListOfResources(ctx, theFound)) {
			if (next != null) {
				retVal.add(next.getIdElement().toUnqualifiedVersionless());
			}
		}
		return retVal;
	}

	public static List<String> toUnqualifiedVersionlessIdValues(IBaseBundle theFound) {
		return toUnqualifiedVersionlessIds(theFound)
			.stream()
			.map(t -> t.getValue())
			.collect(Collectors.toList());
	}

}
