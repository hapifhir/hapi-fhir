/*-
 * #%L
 * hapi-fhir-storage-test-utilities
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
package ca.uhn.fhir.storage.test;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import org.hl7.fhir.instance.model.api.IBaseMetaType;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Meta;
import org.hl7.fhir.r4.model.Patient;

import java.util.List;

import static ca.uhn.fhir.test.utilities.TagTestUtil.assertCodingsEqualAndInOrder;
import static ca.uhn.fhir.test.utilities.TagTestUtil.createMeta;
import static ca.uhn.fhir.test.utilities.TagTestUtil.generateAllCodingPairs;
import static ca.uhn.fhir.test.utilities.TagTestUtil.toStringList;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Contains some test case helper functions for testing the storage of meta properties: tag, security and profile
 */
public class TagTestCasesUtil {

	private IFhirResourceDao<Patient> myPatientDao;

	private IFhirSystemDao<Bundle, Meta> mySystemDao;

	private RequestDetails myRequestDetails;

	private boolean myMetaOperationSupported;

	public TagTestCasesUtil(IFhirResourceDao<Patient> thePatientDao, IFhirSystemDao<Bundle, Meta> theSystemDao, RequestDetails theRequestDetails, boolean theMetaOperationSupported) {
		this.myPatientDao = thePatientDao;
		this.mySystemDao = theSystemDao;
		this.myRequestDetails = theRequestDetails;
		this.myMetaOperationSupported = theMetaOperationSupported;
	}

	/**
	 * Creates a resource with the given Meta and reads the resource back and asserts that the resource
	 * has the specified meta properties for tag, security and profile
	 */
	public IBaseResource createResourceAndVerifyMeta (Meta theMetaInputOnCreate, Meta theExpectedMetaAfterCreate){
		DaoMethodOutcome createOutcome = createPatient(theMetaInputOnCreate);

		IIdType versionlessPatientId = createOutcome.getResource().getIdElement().toVersionless();
		Patient patient = myPatientDao.read(versionlessPatientId, myRequestDetails);

		verifyMeta(theExpectedMetaAfterCreate, patient.getMeta());

		if (myMetaOperationSupported) {
			//test meta get operation with a specific id
			Meta meta = myPatientDao.metaGetOperation(Meta.class, versionlessPatientId, myRequestDetails);
			verifyMeta(theExpectedMetaAfterCreate, meta);

			//test meta get operation for the resource type (without specific id)
			//note: this, and the following system level metaGet operation, assume that the tags created
			//by this function are the only tags in the system, which is true for all test cases that use this function
			//currently, but if changes these checks could be relaxed
			//to check for contains in order rather than equality of the tag lists, or metaGet operations that aren't
			//specific to a particular resource id could be separated into its own test function
			meta = myPatientDao.metaGetOperation(Meta.class, myRequestDetails);
			verifyMeta(theExpectedMetaAfterCreate, meta);

			//test meta operation for system
			meta = mySystemDao.metaGetOperation(myRequestDetails);
			verifyMeta(theExpectedMetaAfterCreate, meta);
		}

		//ensure version endpoint also returns tags as expected
		IIdType versionId = new IdType(String.format("%s/_history/1", patient.getIdElement().toVersionless()));
		patient = myPatientDao.read(versionId, myRequestDetails);
		verifyMeta(theExpectedMetaAfterCreate, patient.getMeta());

		return patient;
	}




	/**
	 * Creates a resource with the given meta properties, then updates the resource with the specified meta properties, then
	 * reads the resource back and asserts that the resource has the specified properties for tag, security and profile
	 */
	public IBaseResource updateResourceAndVerifyMeta(Meta theMetaInputOnCreate, Meta theMetaInputOnUpdate, Meta theExpectedMetaAfterUpdate, boolean theExpectNop) {
		DaoMethodOutcome createOutcome = createPatient(theMetaInputOnCreate);
		IIdType versionlessPatientId = createOutcome.getId().toVersionless();

		DaoMethodOutcome updateOutcome = updatePatient(versionlessPatientId, theMetaInputOnUpdate);
		assertEquals(theExpectNop, updateOutcome.isNop());

		Patient patient = myPatientDao.read(versionlessPatientId, myRequestDetails);
		verifyMeta(theExpectedMetaAfterUpdate, patient.getMeta());

		if (myMetaOperationSupported) {
			Meta meta = myPatientDao.metaGetOperation(Meta.class, versionlessPatientId, myRequestDetails);
			verifyMeta(theExpectedMetaAfterUpdate, meta);
		}

		//ensure version endpoint also returns tags as expected
		IIdType versionId = new IdType(String.format("%s/_history/2", patient.getIdElement().toVersionless()));
		patient = myPatientDao.read(versionId, myRequestDetails);
		verifyMeta(theExpectedMetaAfterUpdate, patient.getMeta());

		return patient;
	}

	public IBaseResource updateResourceAndVerifyVersion(IIdType theResourceId, Meta theMetaInputOnUpdate, String theExpectedVersion) {
		updatePatient(theResourceId, theMetaInputOnUpdate);

		Patient patient = myPatientDao.read(theResourceId, myRequestDetails);

		assertEquals(theExpectedVersion, patient.getMeta().getVersionId());

		return patient;
	}

	/**
	 * Verifies that tag order doesn't cause a version change for non-inline modes, for which the update behavior is to
	 * take the union of existing and new tags.
	 * The verification consists of 3 parts:
	 * - Part 1: Create a resource with tags and update the resource with same tags in different order, expect version
	 * to remain at 1.
	 * - Part 2: Update the resource with a different set of tags, which would add the new set to the existing set and
	 * increment the version to 2. Then update the resource again with the all the tags the resource current has but in
	 * different order, and expect the version to remain at 2. This part ensures that the storage is able to determine
	 * whether the version should be incremented or not after new tags are added to a resource with
	 * subsequent updates (as opposed to adding tags during resource creation which Part 1 verifies).
	 * - Part 3: Update the resource with a subset of the tags it currently has but in a different order and expect
	 * the version to remain the same.
	 */
	public void updateResourceWithExistingTagsButInDifferentOrderAndExpectVersionToRemainTheSame_NonInlineModes(){

		// Part 1: Create with tags
		Meta metaInputOnCreate = createMeta(
			// generateAllCodingPairs creates a list that has 6 codings in this case in this order:
			// (sys2, c), (sys2, b), (sys2, a), (sys1, c), (sys1, b), (sys1, a)
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "b", "a")), //tag
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "b", "a")), //security
			List.of("c", "b", "a") // profile
		);

		DaoMethodOutcome createOutcome = createPatient(metaInputOnCreate);
		IIdType versionlessPatientId = createOutcome.getId().toVersionless();

		// use the same input on update as the creation but order everything differently
		Meta metaInputOnUpdate = createMeta(
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("b", "c", "a")), //tag
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("b", "c", "a")), //security
			List.of("b", "c", "a") // profile
		);

		//update and assert version remains the same (1)
		updateResourceAndVerifyVersion(versionlessPatientId, metaInputOnUpdate, "1");

		// Part 2: update the resource with a completely different set of tags, which will be added to the existing
		// set by the storage, the resource will have all of a,b,c,aa,bb,cc as tags after the update
		metaInputOnUpdate = createMeta(
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("aa", "bb", "cc")), //tag
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("aa", "bb", "cc")), //security
			List.of("b", "c", "a") // profile
		);

		// expect the version to be incremented
		updateResourceAndVerifyVersion(versionlessPatientId, metaInputOnUpdate, "2");

		// update with all tags the resource has in different order
		metaInputOnUpdate = createMeta(
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "bb", "aa", "b", "cc", "c")), //tag
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "bb", "aa", "b", "cc", "c")), //security
			List.of("b", "c", "a") // profile
		);

		// expect version to remain same before
		updateResourceAndVerifyVersion(versionlessPatientId, metaInputOnUpdate, "2");

		// Part 3: update with a subset of existing tags in random order
		metaInputOnUpdate = createMeta(
			generateAllCodingPairs(List.of("sys2"), List.of("bb", "c", "a")), //tag
			generateAllCodingPairs(List.of("sys2"), List.of("bb", "c", "a")), //security
			List.of("b", "c", "a") // profile
		);

		// expect version to remain same before
		updateResourceAndVerifyVersion(versionlessPatientId, metaInputOnUpdate, "2");
	}

	/**
	 * Verifies that tag order doesn't cause version to increase for inline mode where the update behavior is to
	 * replace the tags completely. This only executes Part 1 of the nonInlineMode test above
	 */
	public void updateResourceWithExistingTagsButInDifferentOrderAndExpectVersionToRemainTheSame_InlineMode(){

		Meta metaInputOnCreate = createMeta(
			// generateAllCodingPairs creates a list that has 6 codings in this case in this order:
			// (sys2, c), (sys2, b), (sys2, a), (sys1, c), (sys1, b), (sys1, a)
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "b", "a")), //tag
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "b", "a")), //security
			List.of("c", "b", "a") // profile
		);

		DaoMethodOutcome createOutcome = createPatient(metaInputOnCreate);
		IIdType versionlessPatientId = createOutcome.getId().toVersionless();

		// use the same input on update as the creation but order everything differently
		Meta metaInputOnUpdate = createMeta(
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("b", "c", "a")), //tag
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("b", "c", "a")), //security
			List.of("b", "c", "a") // profile
		);

		//update and assert version remains the same (1)
		updateResourceAndVerifyVersion(versionlessPatientId, metaInputOnUpdate, "1");
	}

	public void createResourceWithTagsAndExpectToRetrieveThemSorted() {

		Meta metaInputOnCreate = createMeta(
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "b", "a")), //tag
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "b", "a")), //security
			List.of("c", "b", "a") // profile
		);

		//expect properties to be alphabetically sorted
		Meta expectedMetaAfterCreate = createMeta(
			generateAllCodingPairs(List.of("sys1", "sys2"), List.of("a", "b", "c")), //tag (sorted)
			generateAllCodingPairs(List.of("sys1", "sys2"), List.of("a", "b", "c")), //security (sorted)
			List.of("a", "b", "c") //profile (sorted)
		);

		createResourceAndVerifyMeta(metaInputOnCreate, expectedMetaAfterCreate);
	}


	public void updateResourceWithTagsAndExpectToRetrieveTagsSorted_NonInlineModes() {
		// meta input for initial creation
		Meta metaInputOnCreate = createMeta(
			// generateAllCodingPairs creates a list that has 6 codings in this case in this order:
			// (sys2, c), (sys2, b), (sys2, a), (sys1, c), (sys1, b), (sys1, a)
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "b", "a")), //tag
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("c", "b", "a")), //security
			List.of("c", "b", "a") // profile
		);

		// meta input for update (adding new tags)
		Meta metaInputOnUpdate = createMeta(
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("cc", "bb", "aa")), //tag
			generateAllCodingPairs(List.of("sys2", "sys1"), List.of("cc", "bb", "aa")), //security
			List.of("cc", "bb", "aa") //profile
		);

		// the new tags & security must be added to the existing set and must be in alphabetical order
		// the profile will be completely replaced
		Meta expectedMetaAfterUpdate = createMeta(
			generateAllCodingPairs(List.of("sys1", "sys2"), List.of("a", "aa", "b", "bb", "c", "cc")), //tag (added & sorted)
			generateAllCodingPairs(List.of("sys1", "sys2"), List.of("a", "aa", "b", "bb", "c", "cc")), //security (added & sorted)
			List.of("aa", "bb", "cc") //profile (replaced & sorted)
		);

		IBaseResource resource = updateResourceAndVerifyMeta(metaInputOnCreate,  metaInputOnUpdate, expectedMetaAfterUpdate, false);
		// expect the resource version to be 2, since the meta is updated
		assertEquals("2", resource.getMeta().getVersionId());

		//ensure version endpoint also returns tags sorted
		IIdType version2Id = new IdType(String.format("%s/_history/2", resource.getIdElement().toVersionless()));
		resource = myPatientDao.read(version2Id, myRequestDetails);
		verifyMeta(expectedMetaAfterUpdate, resource.getMeta());
	}

	private DaoMethodOutcome createPatient(Meta theMeta) {
		Patient inputPatient = new Patient();
		inputPatient.setMeta(theMeta);
		return myPatientDao.create(inputPatient, myRequestDetails);
	}

	private DaoMethodOutcome updatePatient(IIdType thePatientId, Meta theMeta) {
		Patient inputPatient = new Patient();
		inputPatient.setId(thePatientId);
		inputPatient.setMeta(theMeta);

		return myPatientDao.update(inputPatient, myRequestDetails);
	}

	private void verifyMeta(IBaseMetaType theExpectedMeta, IBaseMetaType theActualMeta) {
		assertCodingsEqualAndInOrder(theExpectedMeta.getTag(), theActualMeta.getTag());
		assertCodingsEqualAndInOrder(theExpectedMeta.getSecurity(), theActualMeta.getSecurity());
		assertEquals(toStringList(theExpectedMeta.getProfile()), toStringList(theActualMeta.getProfile()));
	}

	public void setMetaOperationSupported(boolean theMetaOperationSupported) {
		this.myMetaOperationSupported = theMetaOperationSupported;
	}
}
