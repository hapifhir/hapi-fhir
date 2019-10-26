package ca.uhn.fhir.jpa.searchparam.extractor;

/*
 * #%L
 * HAPI FHIR Search Parameters
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.List;

public class SearchParamExtractorDstu2 extends BaseSearchParamExtractor implements ISearchParamExtractor {


	@Override
	protected IValueExtractor getPathValueExtractor(IBaseResource theResource, String thePaths) {
		return () -> {
			List<IBase> values = new ArrayList<>();
			String[] nextPathsSplit = split(thePaths);
			FhirTerser t = getContext().newTerser();
			for (String nextPath : nextPathsSplit) {
				String nextPathTrimmed = nextPath.trim();
				List<IBase> allValues = t.getValues(theResource, nextPathTrimmed);
				for (IBase next : allValues) {
					if (next instanceof IBaseExtension) {
						IBaseDatatype value = ((IBaseExtension) next).getValue();
						if (value != null) {
							values.add(value);
						}
					} else {
						values.add(next);
					}
				}
			}
			return values;
		};
	}

}
