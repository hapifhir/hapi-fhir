package ca.uhn.fhir.empi.provider;

/*-
 * #%L
 * HAPI FHIR - Enterprise Master Patient Index
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

import ca.uhn.fhir.empi.api.IEmpiMatchFinderSvc;
import ca.uhn.fhir.empi.api.IEmpiPersonMergerSvc;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;
import ca.uhn.fhir.validation.IResourceLoader;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.StringType;

import java.util.Collection;
import java.util.UUID;

public class EmpiProviderR4 extends BaseEmpiProvider {
	private final IEmpiMatchFinderSvc myEmpiMatchFinderSvc;
	private final IEmpiPersonMergerSvc myPersonMergerSvc;
	private final IResourceLoader myResourceLoader;

	/**
	 * Constructor
	 *
	 * Note that this is not a spring bean. Any necessary injections should
	 * happen in the constructor
	 */
	public EmpiProviderR4(IEmpiMatchFinderSvc theEmpiMatchFinderSvc, IEmpiPersonMergerSvc thePersonMergerSvc, IResourceLoader theResourceLoader) {
		myEmpiMatchFinderSvc = theEmpiMatchFinderSvc;
		myPersonMergerSvc = thePersonMergerSvc;
		myResourceLoader = theResourceLoader;
	}

	@Operation(name = ProviderConstants.EMPI_MATCH, type = Patient.class)
	public Bundle match(@OperationParam(name=ProviderConstants.EMPI_MATCH_RESOURCE, min = 1, max = 1) Patient thePatient) {
		if (thePatient == null) {
			throw new InvalidRequestException("resource may not be null");
		}

		Collection<IAnyResource> matches = myEmpiMatchFinderSvc.findMatches("Patient", thePatient);

		Bundle retVal = new Bundle();
		retVal.setType(Bundle.BundleType.SEARCHSET);
		retVal.setId(UUID.randomUUID().toString());
		retVal.getMeta().setLastUpdatedElement(InstantType.now());

		for (IAnyResource next : matches) {
			retVal.addEntry().setResource((Resource) next);
		}

		return retVal;
	}

	@Operation(name = ProviderConstants.EMPI_MERGE_PERSONS, type = Person.class)
	public Person mergePersons(@OperationParam(name=ProviderConstants.EMPI_MERGE_PERSONS_PERSON_ID_TO_DELETE, min = 1, max = 1) StringType thePersonIdToDelete,
									  @OperationParam(name=ProviderConstants.EMPI_MERGE_PERSONS_PERSON_ID_TO_KEEP, min = 1, max = 1) StringType thePersonIdToKeep) {
		validateMergeParameters(thePersonIdToDelete, thePersonIdToKeep);
		IAnyResource personToDelete = getPersonFromId(thePersonIdToDelete.getValue(), "personIdToDelete");
		IAnyResource personToKeep = getPersonFromId(thePersonIdToKeep.getValue(), "personIdToKeep");

		return (Person) myPersonMergerSvc.mergePersons(personToDelete, personToKeep);
	}

	@Override
	protected IAnyResource loadPerson(IdDt thePersonId) {
		return myResourceLoader.load(Person.class, thePersonId);
	}
}
