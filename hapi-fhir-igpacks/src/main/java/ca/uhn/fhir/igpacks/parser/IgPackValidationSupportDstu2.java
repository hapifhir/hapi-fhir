package ca.uhn.fhir.igpacks.parser;

/*-
 * #%L
 * hapi-fhir-igpacks
 * %%
 * Copyright (C) 2014 - 2018 University Health Network
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
import org.hl7.fhir.instance.hapi.validation.IValidationSupport;
import org.hl7.fhir.instance.model.ConceptMap;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class IgPackValidationSupportDstu2 implements IValidationSupport {
	private final Map<IIdType, IBaseResource> myIgResources;

	public IgPackValidationSupportDstu2(Map<IIdType, IBaseResource> theIgResources) {
		myIgResources = theIgResources;
	}

	@Override
	public List<StructureDefinition> allStructures() {
		ArrayList<StructureDefinition> retVal = new ArrayList<>();

		for (Map.Entry<IIdType, IBaseResource> next : myIgResources.entrySet()) {
			if (next.getKey().getResourceType().equals("StructureDefinition")) {
				retVal.add((StructureDefinition) next.getValue());
			}
		}
		return retVal;
	}

	@Override
	public ValueSet.ValueSetExpansionComponent expandValueSet(FhirContext theContext, ValueSet.ConceptSetComponent theInclude) {
		return null;
	}

	@Override
	public ValueSet fetchCodeSystem(FhirContext theContext, String theSystem) {
		for (Map.Entry<IIdType, IBaseResource> next : myIgResources.entrySet()) {
			if (next.getKey().getResourceType().equals("ValueSet")) {
				ValueSet nextVs = (ValueSet) next.getValue();
				if (theSystem.equals(nextVs.getUrl())) {
					return nextVs;
				}
			}
		}
		return null;
	}


	@Override
	public <T extends IBaseResource> T fetchResource(FhirContext theContext, Class<T> theClass, String theUri) {
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
	public boolean isCodeSystemSupported(FhirContext theContext, String theSystem) {
		return false;
	}

	@Override
	public CodeValidationResult validateCode(FhirContext theContext, String theCodeSystem, String theCode, String theDisplay) {
		return null;
	}
}
