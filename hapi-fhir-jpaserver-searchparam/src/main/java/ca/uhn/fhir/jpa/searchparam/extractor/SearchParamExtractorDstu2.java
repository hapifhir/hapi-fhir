package ca.uhn.fhir.jpa.searchparam.extractor;

/*
 * #%L
 * HAPI FHIR Search Parameters
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
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseDatatype;
import org.hl7.fhir.instance.model.api.IBaseExtension;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.ArrayList;
import java.util.List;

public class SearchParamExtractorDstu2 extends BaseSearchParamExtractor implements ISearchParamExtractor {

	public SearchParamExtractorDstu2() {
	}

	/**
	 * Constructor for unit tests
	 */
	SearchParamExtractorDstu2(ModelConfig theModelConfig, PartitionSettings thePartitionSettings, FhirContext theCtx, ISearchParamRegistry theSearchParamRegistry) {
		super(theModelConfig, thePartitionSettings, theCtx, theSearchParamRegistry);
		start();
	}

	@Override
	public IValueExtractor getPathValueExtractor(IBaseResource theResource, String theSinglePath) {
		return () -> {
			String path = theSinglePath;

			String needContactPointSystem = null;
			if (path.endsWith("(system=phone)")) {
				path = path.substring(0, path.length() - "(system=phone)".length());
				needContactPointSystem = "phone";
			}
			if (path.endsWith("(system=email)")) {
				path = path.substring(0, path.length() - "(system=email)".length());
				needContactPointSystem = "email";
			}

			List<IBase> values = new ArrayList<>();
			FhirTerser t = getContext().newTerser();
			List<IBase> allValues = t.getValues(theResource, path);
			for (IBase next : allValues) {
				if (next instanceof IBaseExtension) {
					IBaseDatatype value = ((IBaseExtension) next).getValue();
					if (value != null) {
						values.add(value);
					}
				} else {

					if (needContactPointSystem != null) {
						if (next instanceof ContactPointDt) {
							if (!needContactPointSystem.equals(((ContactPointDt) next).getSystem())) {
								continue;
							}
						}
					}

					values.add(next);
				}
			}
			return values;
		};
	}

}
