package ca.uhn.fhir.jpa.dao.dstu2;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.model.api.ResourceMetadataKeyEnum;
import ca.uhn.fhir.model.api.Tag;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.model.base.composite.BaseCodingDt;
import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import ca.uhn.fhir.model.dstu2.resource.Conformance;
import ca.uhn.fhir.model.dstu2.resource.Organization;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class FhirResourceDaoDstu2UpdateTest extends BaseJpaDstu2Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoDstu2UpdateTest.class);


	@Test
	public void testUpdateByUrl() {
		String methodName = "testUpdateByUrl";

		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		IIdType id = myPatientDao.create(p, mySrd).getId();
		ourLog.info("Created patient, got it: {}", id);

		p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);

		myPatientDao.update(p, "Patient?identifier=urn%3Asystem%7C" + methodName, mySrd);

		p = myPatientDao.read(id.toVersionless(), mySrd);
		assertThat(p.getId().toVersionless().toString(), not(containsString("test")));
		assertEquals(id.toVersionless(), p.getId().toVersionless());
		assertNotEquals(id, p.getId());
		assertThat(p.getId().toString(), endsWith("/_history/2"));

	}

	@Test
	public void testUpdateCreatesTextualIdIfItDoesntAlreadyExist() {
		Patient p = new Patient();
		String methodName = "testUpdateCreatesTextualIdIfItDoesntAlreadyExist";
		p.addIdentifier().setSystem("urn:system").setValue(methodName);
		p.addName().addFamily("Hello");
		p.setId("Patient/" + methodName);

		IIdType id = myPatientDao.update(p, mySrd).getId();
		assertEquals("Patient/" + methodName, id.toUnqualifiedVersionless().getValue());

		p = myPatientDao.read(id, mySrd);
		assertEquals(methodName, p.getIdentifierFirstRep().getValue());
	}

	@Test
	public void testUpdateDoesntFailForUnknownIdWithNumberThenText() {
		String methodName = "testUpdateFailsForUnknownIdWithNumberThenText";
		Patient p = new Patient();
		p.setId("0" + methodName);
		p.addName().addFamily(methodName);

		myPatientDao.update(p, mySrd);
	}

	/**
	 * Make sure we can upload a real example resource from the DSTU2 argonaut example pack
	 */
	@Test
	public void testUploadArgonautExampleConformance() throws IOException {
		Conformance conformance = loadResourceFromClasspath(Conformance.class, "/dstu2/Conformance-server.json");
		conformance.setId("");
		myConformanceDao.create(conformance);

		assertEquals(1, myConformanceDao.search(new SearchParameterMap().setLoadSynchronous(true)).sizeOrThrowNpe());
	}

	/**
	 * Per the spec, update should preserve tags and security labels but not profiles
	 */
	@Test
	public void testUpdateMaintainsTagsAndSecurityLabels() {
		String methodName = "testUpdateMaintainsTagsAndSecurityLabels";

		IIdType p1id;
		{
			Patient p1 = new Patient();
			p1.addName().addFamily(methodName);

			TagList tagList = new TagList();
			tagList.addTag("tag_scheme1", "tag_term1");
			ResourceMetadataKeyEnum.TAG_LIST.put(p1, tagList);
			List<BaseCodingDt> secList = new ArrayList<>();
			secList.add(new CodingDt("sec_scheme1", "sec_term1"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(p1, secList);
			List<IdDt> profileList = new ArrayList<>();
			profileList.add(new IdDt("http://foo1"));
			ResourceMetadataKeyEnum.PROFILES.put(p1, profileList);

			p1id = myPatientDao.create(p1, mySrd).getId().toUnqualifiedVersionless();
		}
		{
			Patient p1 = new Patient();
			p1.setId(p1id.getValue());
			p1.addName().addFamily(methodName);

			TagList tagList = new TagList();
			tagList.addTag("tag_scheme2", "tag_term2");
			ResourceMetadataKeyEnum.TAG_LIST.put(p1, tagList);
			List<BaseCodingDt> secList = new ArrayList<>();
			secList.add(new CodingDt("sec_scheme2", "sec_term2"));
			ResourceMetadataKeyEnum.SECURITY_LABELS.put(p1, secList);
			List<IdDt> profileList = new ArrayList<>();
			profileList.add(new IdDt("http://foo2"));
			ResourceMetadataKeyEnum.PROFILES.put(p1, profileList);

			myPatientDao.update(p1, mySrd);
		}
		{
			Patient p1 = myPatientDao.read(p1id, mySrd);
			TagList tagList = ResourceMetadataKeyEnum.TAG_LIST.get(p1);
			assertThat(tagList, containsInAnyOrder(new Tag("tag_scheme1", "tag_term1"), new Tag("tag_scheme2", "tag_term2")));
			List<BaseCodingDt> secList = ResourceMetadataKeyEnum.SECURITY_LABELS.get(p1);
			Set<String> secListValues = new HashSet<>();
			for (BaseCodingDt next : secList) {
				secListValues.add(next.getSystemElement().getValue() + "|" + next.getCodeElement().getValue());
			}
			assertThat(secListValues, containsInAnyOrder("sec_scheme1|sec_term1", "sec_scheme2|sec_term2"));
			List<IdDt> profileList = ResourceMetadataKeyEnum.PROFILES.get(p1);
			assertThat(profileList, contains(new IdDt("http://foo2"))); // no foo1
		}
	}

	@Test
	public void testUpdateMaintainsSearchParams() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2AAA");
		p1.addName().addFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2AAA");
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		Patient p2 = new Patient();
		p2.addIdentifier().setSystem("urn:system").setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		p2.addName().addFamily("Tester").addGiven("testUpdateMaintainsSearchParamsDstu2BBB");
		myPatientDao.create(p2, mySrd);

		List<ResourcePersistentId> ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsDstu2AAA")), null);
		assertEquals(1, ids.size());
		assertThat(ResourcePersistentId.toLongList(ids), contains(p1id.getIdPartAsLong()));

		// Update the name
		p1.getNameFirstRep().getGivenFirstRep().setValue("testUpdateMaintainsSearchParamsDstu2BBB");
		MethodOutcome update2 = myPatientDao.update(p1, mySrd);
		IIdType p1id2 = update2.getId();

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsDstu2AAA")), null);
		assertEquals(0, ids.size());

		ids = myPatientDao.searchForIds(new SearchParameterMap(Patient.SP_GIVEN, new StringDt("testUpdateMaintainsSearchParamsDstu2BBB")), null);
		assertEquals(2, ids.size());

		// Make sure vreads work
		p1 = myPatientDao.read(p1id, mySrd);
		assertEquals("testUpdateMaintainsSearchParamsDstu2AAA", p1.getNameFirstRep().getGivenAsSingleString());

		p1 = myPatientDao.read(p1id2, mySrd);
		assertEquals("testUpdateMaintainsSearchParamsDstu2BBB", p1.getNameFirstRep().getGivenAsSingleString());

	}

	@Test
	public void testUpdateRejectsInvalidTypes() {
		Patient p1 = new Patient();
		p1.addIdentifier().setSystem("urn:system").setValue("testUpdateRejectsInvalidTypes");
		p1.addName().addFamily("Tester").addGiven("testUpdateRejectsInvalidTypes");
		IIdType p1id = myPatientDao.create(p1, mySrd).getId();

		Organization p2 = new Organization();
		p2.getNameElement().setValue("testUpdateRejectsInvalidTypes");
		try {
			p2.setId(new IdDt("Organization/" + p1id.getIdPart()));
			myOrganizationDao.update(p2, mySrd);
			fail();
		} catch (UnprocessableEntityException e) {
			// good
		}

		try {
			p2.setId(new IdDt("Patient/" + p1id.getIdPart()));
			myOrganizationDao.update(p2, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			ourLog.error("Good", e);
		}

	}

	@Test
	public void testDuplicateProfilesIgnored() {
		String name = "testDuplicateProfilesIgnored";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().addFamily(name);

			List<IdDt> tl = new ArrayList<>();
			tl.add(new IdDt("http://foo/bar"));
			tl.add(new IdDt("http://foo/bar"));
			tl.add(new IdDt("http://foo/bar"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, tl);

			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<IdDt> tl = ResourceMetadataKeyEnum.PROFILES.get(patient);
			assertEquals(1, tl.size());
			assertEquals("http://foo/bar", tl.get(0).getValue());
		}

	}

	@Test
	public void testUpdateModifiesProfiles() {
		String name = "testUpdateModifiesProfiles";
		IIdType id;
		{
			Patient patient = new Patient();
			patient.addName().addFamily(name);

			List<IdDt> tl = new ArrayList<>();
			tl.add(new IdDt("http://foo/bar"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, tl);

			id = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<IdDt> tl = ResourceMetadataKeyEnum.PROFILES.get(patient);
			assertEquals(1, tl.size());
			assertEquals("http://foo/bar", tl.get(0).getValue());
		}

		// Update
		{
			Patient patient = new Patient();
			patient.setId(id);
			patient.addName().addFamily(name);

			List<IdDt> tl = new ArrayList<>();
			tl.add(new IdDt("http://foo/baz"));
			ResourceMetadataKeyEnum.PROFILES.put(patient, tl);

			id = myPatientDao.update(patient, mySrd).getId().toUnqualifiedVersionless();
		}

		// Do a read
		{
			Patient patient = myPatientDao.read(id, mySrd);
			List<IdDt> tl = ResourceMetadataKeyEnum.PROFILES.get(patient);
			assertEquals(1, tl.size());
			assertEquals("http://foo/baz", tl.get(0).getValue());
		}

	}

	@Test
	public void testUpdateUnknownNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/9999999999999999");
		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("Can not create resource with ID[9999999999999999], no resource with this ID exists and clients may only"));
		}
	}

	@Test
	public void testUpdateWithInvalidIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/123:456");
		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals(Msg.code(521) + "Can not process entity with ID[123:456], this is not a valid FHIR ID", e.getMessage());
		}
	}

	@Test
	public void testUpdateWithNumericIdFails() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/123");
		try {
			myPatientDao.update(p, mySrd);
			fail();
		} catch (InvalidRequestException e) {
			assertThat(e.getMessage(), containsString("clients may only assign IDs which contain at least one non-numeric"));
		}
	}

	@Test
	public void testUpdateWithNumericThenTextIdSucceeds() {
		Patient p = new Patient();
		p.addIdentifier().setSystem("urn:system").setValue("testCreateNumericIdFails");
		p.addName().addFamily("Hello");
		p.setId("Patient/123abc");
		IIdType id = myPatientDao.update(p, mySrd).getId();
		assertEquals("123abc", id.getIdPart());
		assertEquals("1", id.getVersionIdPart());

		p = myPatientDao.read(id.toUnqualifiedVersionless(), mySrd);
		assertEquals("Patient/123abc", p.getId().toUnqualifiedVersionless().getValue());
		assertEquals("Hello", p.getName().get(0).getFamily().get(0).getValue());

	}

}
