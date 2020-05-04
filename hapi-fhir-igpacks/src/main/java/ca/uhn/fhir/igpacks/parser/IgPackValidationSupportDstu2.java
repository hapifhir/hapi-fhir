package ca.uhn.fhir.igpacks.parser;

/*-
 * #%L
 * hapi-fhir-igpacks
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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
import ca.uhn.fhir.context.support.IValidationSupport;
import org.hl7.fhir.dstu2.model.ConceptMap;
import org.hl7.fhir.dstu2.model.StructureDefinition;
import org.hl7.fhir.dstu2.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.Map;

public class IgPackValidationSupportDstu2 implements IValidationSupport {
	private final Map<IIdType, IBaseResource> myIgResources;
	private FhirContext myCtx;

	public IgPackValidationSupportDstu2(FhirContext theCtx, Map<IIdType, IBaseResource> theIgResources) {
		myCtx = theCtx;
		myIgResources = theIgResources;
	}



	@Override
	public <T extends IBaseResource> T fetchResource(Class<T> theClass, String theUri) {
		for (Map.Entry<IIdType, IBaseResource> next : myIgResources.entrySet()) {
			if (theClass.equals(ConceptMap.class)) {
				if (theClass.isAssignableFrom(next.getValue().getClass())) {
					ConceptMap sd = ((ConceptMap) next.getValue());
					if (sd.getUrl().equals(theUri)) {
						return (T) sd;
					}
				}
			}
			if (theClass.equals(StructureDefinition.class)) {
				if (theClass.isAssignableFrom(next.getValue().getClass())) {
					StructureDefinition sd = ((StructureDefinition) next.getValue());
					if (sd.getUrl().equals(theUri)) {
						return (T) sd;
					}
				}
			}
			if (theClass.equals(ValueSet.class)) {
				if (theClass.isAssignableFrom(next.getValue().getClass())) {
					ValueSet sd = ((ValueSet) next.getValue());
					if (sd.getUrl().equals(theUri)) {
						return (T) sd;
					}
				}
			}
		}

		return null;
	}


	@Override
	public boolean isCodeSystemSupported(IValidationSupport theRootValidationSupport, String theSystem) {
		return false;
	}

	@Override
	public FhirContext getFhirContext() {
		return myCtx;
	}

}
