/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.replacereferences;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoPatient;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This is class that creates many resources for the ReplaceReferences and Merge tests.
 * The class also contains some methods that makes it easier to validate the results of the operations according to the
 * relationships between the resources it creates.
 */
public class ReplaceReferencesLargeTestData {

	private static final Logger ourLog = LoggerFactory.getLogger(ReplaceReferencesLargeTestData.class);

	static final Identifier pat1IdentifierA =
			new Identifier().setSystem("SYS1A").setValue("VAL1A");
	static final Identifier pat1IdentifierB =
			new Identifier().setSystem("SYS1B").setValue("VAL1B");
	static final Identifier pat2IdentifierA =
			new Identifier().setSystem("SYS2A").setValue("VAL2A");
	static final Identifier pat2IdentifierB =
			new Identifier().setSystem("SYS2B").setValue("VAL2B");
	static final Identifier patBothIdentifierC =
			new Identifier().setSystem("SYSC").setValue("VALC");
	public static final int TOTAL_EXPECTED_PATCHES = 23;
	public static final int SMALL_BATCH_SIZE = 5;
	public static final int EXPECTED_SMALL_BATCHES = (TOTAL_EXPECTED_PATCHES + SMALL_BATCH_SIZE - 1) / SMALL_BATCH_SIZE;
	public static final List<String> RESOURCE_TYPES_EXPECTED_TO_BE_PATCHED =
			List.of("Observation", "Encounter", "CarePlan");
	private final IFhirResourceDaoPatient<Patient> myPatientDao;
	private final IFhirResourceDao<Organization> myOrganizationDao;
	private final IFhirResourceDao<Encounter> myEncounterDao;
	private final IFhirResourceDao<CarePlan> myCarePlanDao;
	private final IFhirResourceDao<Observation> myObservationDao;

	private IIdType myOrgId;

	private IIdType mySourcePatientId;
	private IIdType mySourceCarePlanId;
	private IIdType mySourceEncId1;
	private IIdType mySourceEncId2;
	private ArrayList<IIdType> mySourceObsIds;

	private IIdType myTargetPatientId;
	private IIdType myTargetEnc1;

	private final Set<IIdType> myResourceIdsInitiallyReferencingSource = new HashSet<>();
	private final Set<IIdType> myResourceIdsInitiallyReferencingTarget = new HashSet<>();

	private final Set<IIdType> myResourceIdsInitiallyReferencedBySource = new HashSet<>();
	private final Set<IIdType> myResourceIdsInitiallyReferencedByTarget = new HashSet<>();

	private final SystemRequestDetails mySrd = new SystemRequestDetails();

	public ReplaceReferencesLargeTestData(DaoRegistry theDaoRegistry) {
		myPatientDao = (IFhirResourceDaoPatient<Patient>) theDaoRegistry.getResourceDao(Patient.class);
		myOrganizationDao = theDaoRegistry.getResourceDao(Organization.class);
		myEncounterDao = theDaoRegistry.getResourceDao(Encounter.class);
		myCarePlanDao = theDaoRegistry.getResourceDao(CarePlan.class);
		myObservationDao = theDaoRegistry.getResourceDao(Observation.class);
	}

	public void createTestResources() {

		Organization org = new Organization();
		org.setName("an org");
		myOrgId = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless();
		ourLog.info("OrgId: {}", myOrgId);

		Patient patient1 = new Patient();
		patient1.getManagingOrganization().setReferenceElement(myOrgId);
		patient1.addIdentifier(pat1IdentifierA);
		patient1.addIdentifier(pat1IdentifierB);
		patient1.addIdentifier(patBothIdentifierC);
		mySourcePatientId = myPatientDao.create(patient1, mySrd).getId().toUnqualifiedVersionless();
		myResourceIdsInitiallyReferencedBySource.add(myOrgId);

		Patient patient2 = new Patient();
		patient2.addIdentifier(pat2IdentifierA);
		patient2.addIdentifier(pat2IdentifierB);
		patient2.addIdentifier(patBothIdentifierC);
		patient2.getManagingOrganization().setReferenceElement(myOrgId);
		myTargetPatientId = myPatientDao.create(patient2, mySrd).getId().toUnqualifiedVersionless();
		myResourceIdsInitiallyReferencedByTarget.add(myOrgId);

		Encounter enc1 = new Encounter();
		enc1.setStatus(Encounter.EncounterStatus.CANCELLED);
		enc1.getSubject().setReferenceElement(mySourcePatientId);
		enc1.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId1 = myEncounterDao.create(enc1, mySrd).getId().toUnqualifiedVersionless();
		myResourceIdsInitiallyReferencingSource.add(mySourceEncId1);

		Encounter enc2 = new Encounter();
		enc2.setStatus(Encounter.EncounterStatus.ARRIVED);
		enc2.getSubject().setReferenceElement(mySourcePatientId);
		enc2.getServiceProvider().setReferenceElement(myOrgId);
		mySourceEncId2 = myEncounterDao.create(enc2, mySrd).getId().toUnqualifiedVersionless();
		myResourceIdsInitiallyReferencingSource.add(mySourceEncId2);

		CarePlan carePlan = new CarePlan();
		carePlan.setStatus(CarePlan.CarePlanStatus.ACTIVE);
		carePlan.getSubject().setReferenceElement(mySourcePatientId);
		mySourceCarePlanId = myCarePlanDao.create(carePlan, mySrd).getId().toUnqualifiedVersionless();
		myResourceIdsInitiallyReferencingSource.add(mySourceCarePlanId);

		Encounter targetEnc1 = new Encounter();
		targetEnc1.setStatus(Encounter.EncounterStatus.ARRIVED);
		targetEnc1.getSubject().setReferenceElement(myTargetPatientId);
		targetEnc1.getServiceProvider().setReferenceElement(myOrgId);
		this.myTargetEnc1 = myEncounterDao.create(targetEnc1, mySrd).getId().toUnqualifiedVersionless();
		myResourceIdsInitiallyReferencingTarget.add(myTargetEnc1);

		mySourceObsIds = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			Observation obs = new Observation();
			obs.getSubject().setReferenceElement(mySourcePatientId);
			obs.setStatus(Observation.ObservationStatus.FINAL);
			IIdType obsId = myObservationDao.create(obs, mySrd).getId().toUnqualifiedVersionless();
			mySourceObsIds.add(obsId);
		}
		myResourceIdsInitiallyReferencingSource.addAll(mySourceObsIds);
	}

	public Patient createResultPatientInput(boolean theDeleteSource) {
		Patient resultPatient = new Patient();
		resultPatient.setIdElement((IdType) myTargetPatientId);
		resultPatient.addIdentifier(pat1IdentifierA);
		if (!theDeleteSource) {
			// add the link only if we are not deleting the source
			Patient.PatientLinkComponent link = resultPatient.addLink();
			link.setOther(new Reference(mySourcePatientId));
			link.setType(Patient.LinkType.REPLACES);
		}
		return resultPatient;
	}

	public List<Identifier> getExpectedIdentifiersForTargetAfterMerge(boolean theWithInputResultPatient) {

		List<Identifier> expectedIdentifiersOnTargetAfterMerge;
		if (theWithInputResultPatient) {
			expectedIdentifiersOnTargetAfterMerge =
					List.of(new Identifier().setSystem("SYS1A").setValue("VAL1A"));
		} else {
			// the identifiers copied over from source should be marked as old
			expectedIdentifiersOnTargetAfterMerge = List.of(
					new Identifier().setSystem("SYS2A").setValue("VAL2A"),
					new Identifier().setSystem("SYS2B").setValue("VAL2B"),
					new Identifier().setSystem("SYSC").setValue("VALC"),
					new Identifier().setSystem("SYS1A").setValue("VAL1A").copy().setUse(Identifier.IdentifierUse.OLD),
					new Identifier().setSystem("SYS1B").setValue("VAL1B").copy().setUse(Identifier.IdentifierUse.OLD));
		}
		return expectedIdentifiersOnTargetAfterMerge;
	}

	public IIdType getSourcePatientId() {
		return mySourcePatientId;
	}

	public IIdType getTargetPatientId() {
		return myTargetPatientId;
	}

	public Set<IIdType> getResourceIdsInitiallyReferencingSource() {
		return Collections.unmodifiableSet(myResourceIdsInitiallyReferencingSource);
	}

	public Set<IIdType> getResourceIdsInitiallyReferencingTarget() {
		return Collections.unmodifiableSet(myResourceIdsInitiallyReferencingTarget);
	}

	public Set<IIdType> getResourceIdsInitiallyReferencedByTarget() {
		return Collections.unmodifiableSet(myResourceIdsInitiallyReferencedByTarget);
	}

	public Set<IIdType> getResourceIdsInitiallyReferencedBySource() {
		return Collections.unmodifiableSet(myResourceIdsInitiallyReferencedBySource);
	}

	public Set<String> getExpectedProvenanceTargetsForPatchedResources() {
		return myResourceIdsInitiallyReferencingSource.stream()
				.map(resId -> (resId.withVersion("2").toString()))
				.collect(Collectors.toSet());
	}

	public Identifier getIdentifierCommonToBothResources() {
		return patBothIdentifierC;
	}
}
