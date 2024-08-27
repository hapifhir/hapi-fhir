package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.search.SearchCoordinatorSvcImpl;
import ca.uhn.fhir.jpa.util.QueryParameterUtils;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.param.NumberParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Device;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.InstantType;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Location;
import org.hl7.fhir.r4.model.Medication;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Provenance;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.UnsignedIntType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceProviderR4EverythingTest extends BaseResourceProviderR4Test {

	public static final int LARGE_NUMBER = 77;
	private SearchCoordinatorSvcImpl mySearchCoordinatorSvcImpl;

	@BeforeEach
	void beforeEach() {
		mySearchCoordinatorSvcImpl = (SearchCoordinatorSvcImpl)mySearchCoordinatorSvc;
	}

	@Override
	@AfterEach
	public void after() throws Exception {
		super.after();
		mySearchCoordinatorSvcImpl.setSyncSizeForUnitTests(QueryParameterUtils.DEFAULT_SYNC_SIZE);
		mySearchCoordinatorSvcImpl.setLoadingThrottleForUnitTests(null);
		mySearchCoordinatorSvcImpl.setNeverUseLocalSearchForUnitTests(false);
	}

	@Test
	public void testEverythingEncounterInstance() {
		String methodName = "testEverythingEncounterInstance";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = myClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = myClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		IIdType orgId2 = createOrganization(methodName, "1");

		Device dev = new Device();
		dev.setManufacturer(methodName);
		dev.getOwner().setReferenceElement(orgId2);
		IIdType devId = myClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setName(methodName + "Parent");
		IIdType locPId = myClient.create().resource(locParent).execute().getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setName(methodName);
		locChild.getPartOf().setReferenceElement(locPId);
		IIdType locCId = myClient.create().resource(locChild).execute().getId().toUnqualifiedVersionless();

		Encounter encU = new Encounter();
		encU.getSubject().setReferenceElement(patientId);
		encU.addLocation().getLocation().setReferenceElement(locCId);
		IIdType encUId = myClient.create().resource(encU).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getSubject().setReferenceElement(patientId);
		enc.addLocation().getLocation().setReferenceElement(locCId);
		IIdType encId = myClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientId);
		obs.getDevice().setReferenceElement(devId);
		obs.getEncounter().setReferenceElement(encId);
		IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		ourLog.info("IDs: EncU:" + encUId.getIdPart() + " Enc:" + encId.getIdPart() + "  " + patientId.toUnqualifiedVersionless());

		Parameters output = myClient.operation().onInstance(encId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids).containsExactlyInAnyOrder(patientId, encId, orgId1, orgId2, orgId1parent, locPId, locCId, obsId, devId);
		assertThat(ids).doesNotContainSubsequence(encUId);

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingEncounterType() {
		String methodName = "testEverythingEncounterInstance";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = myClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = myClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		IIdType orgId2 = createOrganization(methodName, "1");

		Device dev = new Device();
		dev.setManufacturer(methodName);
		dev.getOwner().setReferenceElement(orgId2);
		IIdType devId = myClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Location locParent = new Location();
		locParent.setName(methodName + "Parent");
		IIdType locPId = myClient.create().resource(locParent).execute().getId().toUnqualifiedVersionless();

		Location locChild = new Location();
		locChild.setName(methodName);
		locChild.getPartOf().setReferenceElement(locPId);
		IIdType locCId = myClient.create().resource(locChild).execute().getId().toUnqualifiedVersionless();

		Encounter encU = new Encounter();
		encU.addIdentifier().setValue(methodName);
		IIdType encUId = myClient.create().resource(encU).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getSubject().setReferenceElement(patientId);
		enc.addLocation().getLocation().setReferenceElement(locCId);
		IIdType encId = myClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientId);
		obs.getDevice().setReferenceElement(devId);
		obs.getEncounter().setReferenceElement(encId);
		IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Parameters output = myClient.operation().onType(Encounter.class).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids).containsExactlyInAnyOrder(patientId, encUId, encId, orgId1, orgId2, orgId1parent, locPId, locCId, obsId, devId);

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingInstanceWithContentFilter() {
		Patient pt1 = new Patient();
		pt1.addName().setFamily("Everything").addGiven("Arthur");
		IIdType ptId1 = myPatientDao.create(pt1, mySrd).getId().toUnqualifiedVersionless();

		Patient pt2 = new Patient();
		pt2.addName().setFamily("Everything").addGiven("Arthur");
		IIdType ptId2 = myPatientDao.create(pt2, mySrd).getId().toUnqualifiedVersionless();

		Device dev1 = new Device();
		dev1.setManufacturer("Some Manufacturer");
		IIdType devId1 = myDeviceDao.create(dev1, mySrd).getId().toUnqualifiedVersionless();

		Device dev2 = new Device();
		dev2.setManufacturer("Some Manufacturer 2");
		myDeviceDao.create(dev2, mySrd).getId().toUnqualifiedVersionless();

		Observation obs1 = new Observation();
		obs1.getText().setDivAsString("<div>OBSTEXT1</div>");
		obs1.getSubject().setReferenceElement(ptId1);
		obs1.getCode().addCoding().setCode("CODE1");
		obs1.setValue(new StringType("obsvalue1"));
		obs1.getDevice().setReferenceElement(devId1);
		IIdType obsId1 = myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();

		Observation obs2 = new Observation();
		obs2.getSubject().setReferenceElement(ptId1);
		obs2.getCode().addCoding().setCode("CODE2");
		obs2.setValue(new StringType("obsvalue2"));
		IIdType obsId2 = myObservationDao.create(obs2, mySrd).getId().toUnqualifiedVersionless();

		Observation obs3 = new Observation();
		obs3.getSubject().setReferenceElement(ptId2);
		obs3.getCode().addCoding().setCode("CODE3");
		obs3.setValue(new StringType("obsvalue3"));
		IIdType obsId3 = myObservationDao.create(obs3, mySrd).getId().toUnqualifiedVersionless();

		List<IIdType> actual;
		StringAndListParam param;

		ourLog.info("Pt1:{} Pt2:{} Obs1:{} Obs2:{} Obs3:{}", ptId1.getIdPart(), ptId2.getIdPart(), obsId1.getIdPart(), obsId2.getIdPart(), obsId3.getIdPart());

		param = new StringAndListParam();
		param.addAnd(new StringOrListParam().addOr(new StringParam("obsvalue1")));

		//@formatter:off
		Parameters response = myClient
			.operation()
			.onInstance(ptId1)
			.named("everything")
			.withParameter(Parameters.class, Constants.PARAM_CONTENT, new StringType("obsvalue1"))
			.execute();
		//@formatter:on

		actual = toUnqualifiedVersionlessIds((Bundle) response.getParameter().get(0).getResource());
		assertThat(actual).containsExactlyInAnyOrder(ptId1, obsId1, devId1);

	}

	/**
	 * See #147
	 */
	@Test
	public void testEverythingPatientDoesntRepeatPatient() {
		Bundle b;
		IParser parser = myFhirContext.newJsonParser();
		b = parser.parseResource(Bundle.class, new InputStreamReader(ResourceProviderR4Test.class.getResourceAsStream("/r4/bug147-bundle.json")));

		Bundle resp = myClient.transaction().withBundle(b).execute();
		List<IdType> ids = new ArrayList<>();
		for (Bundle.BundleEntryComponent next : resp.getEntry()) {
			IdType toAdd = new IdType(next.getResponse().getLocation()).toUnqualifiedVersionless();
			ids.add(toAdd);
		}
		ourLog.info("Created: " + ids.toString());

		IdType patientId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient", patientId.getResourceType());

		{
			Parameters output = myClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
			b = (Bundle) output.getParameter().get(0).getResource();

			ids = new ArrayList<>();
			boolean dupes = false;
			for (Bundle.BundleEntryComponent next : b.getEntry()) {
				IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
				dupes = dupes | ids.contains(toAdd);
				ids.add(toAdd);
			}
			ourLog.info("$everything: " + ids.toString());

			assertThat(dupes).as(ids.toString()).isFalse();
		}

		/*
		 * Now try with a size specified
		 */
		{
			Parameters input = new Parameters();
			input.addParameter().setName(Constants.PARAM_COUNT).setValue(new UnsignedIntType(100));
			Parameters output = myClient.operation().onInstance(patientId).named("everything").withParameters(input).execute();
			b = (Bundle) output.getParameter().get(0).getResource();

			ids = new ArrayList<>();
			boolean dupes = false;
			for (Bundle.BundleEntryComponent next : b.getEntry()) {
				IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
				dupes = dupes | ids.contains(toAdd);
				ids.add(toAdd);
			}
			ourLog.info("$everything: " + ids.toString());

			assertThat(dupes).as(ids.toString()).isFalse();
			assertThat(ids.toString()).contains("Condition");
			assertThat(ids.size()).isGreaterThan(10);
		}
	}

	/**
	 * Test for #226
	 */
	@Test
	public void testEverythingPatientIncludesBackReferences() {
		String methodName = "testEverythingIncludesBackReferences";

		Medication med = new Medication();
		med.getCode().setText(methodName);
		IIdType medId = myMedicationDao.create(med, mySrd).getId().toUnqualifiedVersionless();

		Patient pat = new Patient();
		pat.addAddress().addLine(methodName);
		IIdType patId = myPatientDao.create(pat, mySrd).getId().toUnqualifiedVersionless();

		MedicationRequest mo = new MedicationRequest();
		mo.getSubject().setReferenceElement(patId);
		mo.setMedication(new Reference(medId));
		IIdType moId = myMedicationRequestDao.create(mo, mySrd).getId().toUnqualifiedVersionless();

		Parameters output = myClient.operation().onInstance(patId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		ourLog.info(ids.toString());
		assertThat(ids).containsExactlyInAnyOrder(patId, medId, moId);
	}

	/**
	 * See #148
	 */
	@Test
	public void testEverythingPatientIncludesCondition() {
		Bundle b = new Bundle();
		Patient p = new Patient();
		p.setId("1");
		b.addEntry().setResource(p).getRequest().setMethod(Bundle.HTTPVerb.POST);

		Condition c = new Condition();
		c.getSubject().setReference("Patient/1");
		b.addEntry().setResource(c).getRequest().setMethod(Bundle.HTTPVerb.POST);

		Bundle resp = myClient.transaction().withBundle(b).execute();

		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(resp));

		IdType patientId = new IdType(resp.getEntry().get(0).getResponse().getLocation());
		assertEquals("Patient", patientId.getResourceType());

		Parameters output = myClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		b = (Bundle) output.getParameter().get(0).getResource();

		List<IdType> ids = new ArrayList<>();
		for (Bundle.BundleEntryComponent next : b.getEntry()) {
			IdType toAdd = next.getResource().getIdElement().toUnqualifiedVersionless();
			ids.add(toAdd);
		}

		assertThat(ids.toString()).contains("Patient/");
		assertThat(ids.toString()).contains("Condition/");

	}

	@Test
	public void testEverythingPatientOperation() {
		String methodName = "testEverythingOperation";

		Organization org1parent = new Organization();
		org1parent.setId("org1parent");
		org1parent.setName(methodName + "1parent");
		IIdType orgId1parent = myClient.update().resource(org1parent).execute().getId().toUnqualifiedVersionless();

		Organization org1 = new Organization();
		org1.setName(methodName + "1");
		org1.getPartOf().setReferenceElement(orgId1parent);
		IIdType orgId1 = myClient.create().resource(org1).execute().getId().toUnqualifiedVersionless();

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(orgId1);
		IIdType patientId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		IIdType orgId2 = createOrganization(methodName, "1");

		Device dev = new Device();
		dev.setManufacturer(methodName);
		dev.getOwner().setReferenceElement(orgId2);
		IIdType devId = myClient.create().resource(dev).execute().getId().toUnqualifiedVersionless();

		Observation obs = new Observation();
		obs.getSubject().setReferenceElement(patientId);
		obs.getDevice().setReferenceElement(devId);
		IIdType obsId = myClient.create().resource(obs).execute().getId().toUnqualifiedVersionless();

		Encounter enc = new Encounter();
		enc.getSubject().setReferenceElement(patientId);
		IIdType encId = myClient.create().resource(enc).execute().getId().toUnqualifiedVersionless();

		Parameters output = myClient.operation().onInstance(patientId).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);
		assertThat(ids).containsExactlyInAnyOrder(patientId, devId, obsId, encId, orgId1, orgId2, orgId1parent);

		ourLog.info(ids.toString());
	}

	@Test
	public void testEverythingPatientType() {
		String methodName = "testEverythingPatientType";

		IIdType o1Id = createOrganization(methodName, "1");
		IIdType o2Id = createOrganization(methodName, "2");

		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);

		IIdType p2Id = createPatientWithIndexAtOrganization(methodName, "2", o2Id);
		IIdType c2Id = createConditionForPatient(methodName, "2", p2Id);

		IIdType c3Id = createConditionForPatient(methodName, "3", null);

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withNoParameters(Parameters.class).execute();
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(o1Id, o2Id, p1Id, p2Id, c1Id, c2Id);
		assertThat(ids).doesNotContainSubsequence(c3Id);
	}

	@Test
	public void testEverythingPatientTypeWithIdParameter() {
		String methodName = "testEverythingPatientTypeWithIdParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);

		//Patient 2 stuff.
		IIdType o2Id = createOrganization(methodName, "2");
		IIdType p2Id = createPatientWithIndexAtOrganization(methodName, "2", o2Id);
		IIdType c2Id = createConditionForPatient(methodName, "2", p2Id);

		//Patient 3 stuff.
		IIdType o3Id = createOrganization(methodName, "3");
		IIdType p3Id = createPatientWithIndexAtOrganization(methodName, "3", o3Id);
		IIdType c3Id = createConditionForPatient(methodName, "3", p3Id);

		//Patient 4 stuff.
		IIdType o4Id = createOrganization(methodName, "4");
		IIdType p4Id = createPatientWithIndexAtOrganization(methodName, "4", o4Id);
		IIdType c4Id = createConditionForPatient(methodName, "4", p4Id);

		//No Patient Stuff
		IIdType c5Id = createConditionForPatient(methodName, "4", null);


		{
			//Test for only one patient
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", p1Id.getIdPart());

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);

			assertThat(ids).containsExactlyInAnyOrder(o1Id, p1Id, c1Id);
			assertThat(ids).isNotEqualTo((o2Id));
			assertThat(ids).doesNotContain(c2Id);
			assertThat(ids).doesNotContain(p2Id);
		}

		{
			// Test for Patient 1 and 2
			// e.g. _id=1&_id=2
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", p1Id.getIdPart());
			parameters.addParameter("_id", p2Id.getIdPart());

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);

			assertThat(ids).containsExactlyInAnyOrder(o1Id, p1Id, c1Id, o2Id, c2Id, p2Id);
		}

		{
			// Test for both patients using orList
			// e.g. _id=1,2
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", p1Id.getIdPart() + "," + p2Id.getIdPart());

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);

			assertThat(ids).containsExactlyInAnyOrder(o1Id, p1Id, c1Id, o2Id, c2Id, p2Id);
			assertThat(ids).doesNotContain(c5Id);
		}

		{
			// Test combining 2 or-listed params
			// e.g. _id=1,2&_id=3,4
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", "Patient/" + p1Id.getIdPart() + "," + p2Id.getIdPart());
			parameters.addParameter("_id", p3Id.getIdPart() + "," + p4Id.getIdPart());
			parameters.addParameter(new Parameters.ParametersParameterComponent().setName("_count").setValue(new UnsignedIntType(20)));

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);

			assertThat(ids).containsExactlyInAnyOrder(o1Id, p1Id, c1Id, o2Id, c2Id, p2Id, p3Id, o3Id, c3Id, p4Id, c4Id, o4Id);
			assertThat(ids).doesNotContain(c5Id);
		}

		{
			// Test paging works.
			// There are 12 results, lets make 2 pages of 6.
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", "Patient/" + p1Id.getIdPart() + "," + p2Id.getIdPart());
			parameters.addParameter("_id", p3Id.getIdPart() + "," + p4Id.getIdPart());
			parameters.addParameter(new Parameters.ParametersParameterComponent().setName("_count").setValue(new UnsignedIntType(6)));

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle bundle = (Bundle) output.getParameter().get(0).getResource();

			String next = bundle.getLink("next").getUrl();
			Bundle nextBundle = myClient.loadPage().byUrl(next).andReturnBundle(Bundle.class).execute();
			assertEquals(Bundle.BundleType.SEARCHSET, bundle.getType());

			assertThat(bundle.getEntry()).hasSize(6);
			assertThat(nextBundle.getEntry()).hasSize(6);

			List<IIdType> firstBundle = toUnqualifiedVersionlessIds(bundle);
			List<IIdType> secondBundle = toUnqualifiedVersionlessIds(nextBundle);
			List<IIdType> allresults = new ArrayList<>();
			allresults.addAll(firstBundle);
			allresults.addAll(secondBundle);

			assertThat(allresults).containsExactlyInAnyOrder(o1Id, p1Id, c1Id, o2Id, c2Id, p2Id, p3Id, o3Id, c3Id, p4Id, c4Id, o4Id);
			assertThat(allresults).doesNotContain(c5Id);
		}
	}

	@Test
	public void testEverythingPatientInstanceWithTypeParameter() {
		String methodName = "testEverythingPatientInstanceWithTypeParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Test for only one patient
		Parameters parameters = new Parameters();
		parameters.addParameter("_type", "Condition, Observation");

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onInstance(p1Id).named("everything").withParameters(parameters).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
	}

	@Test
	public void testEverythingPatientTypeWithTypeParameter() {
		String methodName = "testEverythingPatientTypeWithTypeParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Test for only one patient
		Parameters parameters = new Parameters();
		parameters.addParameter("_type", "Condition, Observation");

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
	}

	@Test
	public void testEverythingPatientTypeWithTypeAndIdParameter() {
		String methodName = "testEverythingPatientTypeWithTypeAndIdParameter";

		//Patient 1 stuff.
		IIdType o1Id = createOrganization(methodName, "1");
		IIdType p1Id = createPatientWithIndexAtOrganization(methodName, "1", o1Id);
		IIdType c1Id = createConditionForPatient(methodName, "1", p1Id);
		IIdType obs1Id = createObservationForPatient(p1Id, "1");
		IIdType m1Id = createMedicationRequestForPatient(p1Id, "1");

		//Patient 2 stuff.
		IIdType o2Id = createOrganization(methodName, "2");
		IIdType p2Id = createPatientWithIndexAtOrganization(methodName, "2", o2Id);
		IIdType c2Id = createConditionForPatient(methodName, "2", p2Id);
		IIdType obs2Id = createObservationForPatient(p2Id, "2");
		IIdType m2Id = createMedicationRequestForPatient(p2Id, "2");

		//Test for only patient 1
		Parameters parameters = new Parameters();
		parameters.addParameter("_type", "Condition, Observation");
		parameters.addParameter("_id", p1Id.getIdPart());

		myCaptureQueriesListener.clear();

		Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
		ourLog.debug(myFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(output));
		Bundle b = (Bundle) output.getParameter().get(0).getResource();

		myCaptureQueriesListener.logSelectQueries();

		assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
		List<IIdType> ids = toUnqualifiedVersionlessIds(b);

		assertThat(ids).containsExactlyInAnyOrder(p1Id, c1Id, obs1Id);
		assertThat(ids).doesNotContain(o1Id);
		assertThat(ids).doesNotContain(m1Id);
		assertThat(ids).doesNotContain(p2Id);
		assertThat(ids).doesNotContain(o2Id);
	}

	@Test
	public void testEverythingPatientWorksWithForcedId() {
		String methodName = "testEverythingPatientType";

		//Given
		IIdType o1Id = createOrganization(methodName, "1");
		//Patient ABC stuff.
		Patient patientABC = new Patient();
		patientABC.setId("abc");
		patientABC.setManagingOrganization(new Reference(o1Id));
		IIdType pabcId = myPatientDao.update(patientABC).getId().toUnqualifiedVersionless();
		IIdType c1Id = createConditionForPatient(methodName, "1", pabcId);

		//Patient DEF stuff.
		IIdType o2Id = createOrganization(methodName, "2");
		Patient patientDEF = new Patient();
		patientDEF.setId("def");
		patientDEF.setManagingOrganization(new Reference(o2Id));
		IIdType pdefId = myPatientDao.update(patientDEF).getId().toUnqualifiedVersionless();
		IIdType c2Id = createConditionForPatient(methodName, "2", pdefId);

		IIdType c3Id = createConditionForPatient(methodName, "2", null);

		{
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", "Patient/abc,Patient/def");

			//When
			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle b = (Bundle) output.getParameter().get(0).getResource();

			//Then
			assertEquals(Bundle.BundleType.SEARCHSET, b.getType());
			List<IIdType> ids = toUnqualifiedVersionlessIds(b);
			assertThat(ids).containsExactlyInAnyOrder(o1Id, pabcId, c1Id, pdefId, o2Id, c2Id);
			assertThat(ids).doesNotContain(c3Id);
		}


	}


	// retest
	@Test
	public void testEverythingPatientWithLastUpdatedAndSort() throws Exception {
		String methodName = "testEverythingWithLastUpdatedAndSort";

		Organization org = new Organization();
		org.setName(methodName);
		IIdType oId = myClient.create().resource(org).execute().getId().toUnqualifiedVersionless();

		long time1 = System.currentTimeMillis();
		Thread.sleep(10);

		Patient p = new Patient();
		p.addName().setFamily(methodName);
		p.getManagingOrganization().setReferenceElement(oId);
		IIdType pId = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		long time2 = System.currentTimeMillis();
		Thread.sleep(10);

		Condition c = new Condition();
		c.getCode().setText(methodName);
		c.getSubject().setReferenceElement(pId);
		IIdType cId = myClient.create().resource(c).execute().getId().toUnqualifiedVersionless();

		ourLog.info("Resource IDs:\n * {}\n * {}\n * {}", oId, pId, cId);
		runInTransaction(() -> {
			ourLog.info("Resource Links:\n * {}", myResourceLinkDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
			ourLog.info("Resources:\n * {}", myResourceTableDao.findAll().stream().map(t -> t.toString()).collect(Collectors.joining("\n * ")));
		});

		Thread.sleep(10);
		long time3 = System.currentTimeMillis();

		// %3E=> %3C=<

		myCaptureQueriesListener.clear();
		HttpGet get = new HttpGet(myServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time1)).getValueAsString());
		CloseableHttpResponse response = ourHttpClient.execute(get);
		myCaptureQueriesListener.logSelectQueries();
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids).containsExactlyInAnyOrder(pId, cId, oId);
		} finally {
			response.close();
		}

		get = new HttpGet(myServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time2)).getValueAsString() + "&_lastUpdated=%3C"
			+ new InstantType(new Date(time3)).getValueAsString());
		response = ourHttpClient.execute(get);
		try {
			assertEquals(200, response.getStatusLine().getStatusCode());
			String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			response.getEntity().getContent().close();
			ourLog.info(output);
			List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirContext.newXmlParser().parseResource(Bundle.class, output));
			ourLog.info(ids.toString());
			assertThat(ids).containsExactlyInAnyOrder(pId, cId, oId);
		} finally {
			response.close();
		}

		/*
		 * Sorting is not working since the performance enhancements in 2.4 but
		 * sorting for lastupdated is non-standard anyhow.. Hopefully at some point
		 * we can bring this back
		 */
		// get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_lastUpdated=%3E" + new InstantType(new Date(time1)).getValueAsString() + "&_sort=_lastUpdated");
		// response = ourHttpClient.execute(get);
		// try {
		// assertEquals(200, response.getStatusLine().getStatusCode());
		// String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		// response.getEntity().getContent().close();
		// ourLog.info(output);
		// List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
		// ourLog.info(ids.toString());
		// assertThat(ids).contains(pId, cId);
		// } finally {
		// response.close();
		// }
		//
		// get = new HttpGet(ourServerBase + "/Patient/" + pId.getIdPart() + "/$everything?_sort:desc=_lastUpdated");
		// response = ourHttpClient.execute(get);
		// try {
		// assertEquals(200, response.getStatusLine().getStatusCode());
		// String output = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		// response.getEntity().getContent().close();
		// ourLog.info(output);
		// List<IIdType> ids = toUnqualifiedVersionlessIds(myFhirCtx.newXmlParser().parseResource(Bundle.class, output));
		// ourLog.info(ids.toString());
		// assertThat(ids).contains(cId, pId, oId);
		// } finally {
		// response.close();
		// }

	}

	/**
	 * Per message from David Hay on Skype
	 */
	@Test
	@Disabled
	public void testEverythingWithLargeSet() throws Exception {

		String inputString = IOUtils.toString(getClass().getResourceAsStream("/david_big_bundle.json"), StandardCharsets.UTF_8);
		Bundle inputBundle = myFhirContext.newJsonParser().parseResource(Bundle.class, inputString);
		inputBundle.setType(Bundle.BundleType.TRANSACTION);

		assertThat(inputBundle.getEntry()).hasSize(53);

		Set<String> allIds = new TreeSet<>();
		for (Bundle.BundleEntryComponent nextEntry : inputBundle.getEntry()) {
			nextEntry.getRequest().setMethod(Bundle.HTTPVerb.PUT);
			nextEntry.getRequest().setUrl(nextEntry.getResource().getId());
			allIds.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}

		assertThat(allIds).hasSize(53);

		mySystemDao.transaction(mySrd, inputBundle);

		Bundle responseBundle = myClient
			.operation()
			.onInstance(new IdType("Patient/A161443"))
			.named("everything")
			.withParameter(Parameters.class, "_count", new IntegerType(20))
			.useHttpGet()
			.returnResourceType(Bundle.class)
			.execute();

		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(responseBundle));

		List<String> ids = new ArrayList<>();
		for (Bundle.BundleEntryComponent nextEntry : responseBundle.getEntry()) {
			ids.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
		}
		Collections.sort(ids);
		ourLog.info("{} ids: {}", ids.size(), ids);

		assertThat(responseBundle.getEntry().size()).isLessThanOrEqualTo(25);

		TreeSet<String> idsSet = new TreeSet<>();
		for (int i = 0; i < responseBundle.getEntry().size(); i++) {
			for (Bundle.BundleEntryComponent nextEntry : responseBundle.getEntry()) {
				idsSet.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
			}
		}

		String nextUrl = responseBundle.getLink("next").getUrl();
		responseBundle = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);
		for (int i = 0; i < responseBundle.getEntry().size(); i++) {
			for (Bundle.BundleEntryComponent nextEntry : responseBundle.getEntry()) {
				idsSet.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
			}
		}

		nextUrl = responseBundle.getLink("next").getUrl();
		responseBundle = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);
		for (int i = 0; i < responseBundle.getEntry().size(); i++) {
			for (Bundle.BundleEntryComponent nextEntry : responseBundle.getEntry()) {
				idsSet.add(nextEntry.getResource().getIdElement().toUnqualifiedVersionless().getValue());
			}
		}

		assertNull(responseBundle.getLink("next"));

		assertThat(idsSet).contains("List/A161444");
		assertThat(idsSet).contains("List/A161468");
		assertThat(idsSet).contains("List/A161500");

		ourLog.info("Expected {} - {}", allIds.size(), allIds);
		ourLog.info("Actual   {} - {}", idsSet.size(), idsSet);
		assertEquals(allIds, idsSet);

	}

	/**
	 * Per message from David Hay on Skype
	 */
	@Test
	public void testEverythingWithLargeSet2() {
		myStorageSettings.setSearchPreFetchThresholds(Arrays.asList(15, 30, -1));
		myPagingProvider.setDefaultPageSize(500);
		myPagingProvider.setMaximumPageSize(1000);

		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		for (int i = 1; i < LARGE_NUMBER; i++) {
			Observation obs = new Observation();
			obs.setId("A" + StringUtils.leftPad(Integer.toString(i), 2, '0'));
			obs.setSubject(new Reference(id));
			myClient.update().resource(obs).execute();
		}

		Bundle responseBundle = myClient
			.operation()
			.onInstance(id)
			.named("everything")
			.withParameter(Parameters.class, "_count", new IntegerType(50))
			.useHttpGet()
			.returnResourceType(Bundle.class)
			.execute();

		ArrayList<String> ids = new ArrayList<>();
		for (int i = 0; i < responseBundle.getEntry().size(); i++) {
			Bundle.BundleEntryComponent nextEntry = responseBundle.getEntry().get(i);
			ids.add(nextEntry.getResource().getIdElement().getIdPart());
		}

		Bundle.BundleLinkComponent nextLink = responseBundle.getLink("next");
		ourLog.info("Have {} IDs with next link[{}] : {}", ids.size(), nextLink.getUrl(), ids);

		while (nextLink != null) {
			String nextUrl = nextLink.getUrl();
			responseBundle = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);
			for (int i = 0; i < responseBundle.getEntry().size(); i++) {
				Bundle.BundleEntryComponent nextEntry = responseBundle.getEntry().get(i);
				ids.add(nextEntry.getResource().getIdElement().getIdPart());
			}

			nextLink = responseBundle.getLink("next");
			if (nextLink == null) {
				ourLog.info("Have {} IDs with no next link : {}", ids.size(), ids);
			} else {
				ourLog.info("Have {} IDs with next link[{}] : {}", ids.size(), nextLink.getUrl(), ids);
			}
		}

		assertThat(ids).contains(id.getIdPart());

		// TODO KHS this fails intermittently with 53 instead of 77.
		// This can happen if a previous test set mySearchCoordinatorSvcImpl.setSyncSizeForUnitTests to a lower value
		assertThat(ids).hasSize(LARGE_NUMBER);
		for (int i = 1; i < LARGE_NUMBER; i++) {
			assertThat(ids).as(ids.size() + " ids: " + ids).contains("A" + StringUtils.leftPad(Integer.toString(i), 2, '0'));
		}
	}

	@Test
	public void testEverythingWithOnlyPatient() {
		Patient p = new Patient();
		p.setActive(true);
		IIdType id = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		myFhirContext.getRestfulClientFactory().setSocketTimeout(300 * 1000);

		Bundle response = executeEverythingOperationOnInstance(id);

		assertThat(response.getEntry()).hasSize(1);
	}

	@Test
	public void testFulltextEverythingWithIdAndContent() throws IOException {
		Patient p = new Patient();
		p.setId("FOO");
		p.addName().setFamily("FAMILY");
		myClient.update().resource(p).execute();

		p = new Patient();
		p.setId("BAR");
		p.addName().setFamily("HELLO");
		myClient.update().resource(p).execute();

		Observation o = new Observation();
		o.setId("BAZ");
		o.getSubject().setReference("Patient/FOO");
		o.getCode().setText("GOODBYE");
		myClient.update().resource(o).execute();

		List<String> ids = searchAndReturnUnqualifiedVersionlessIdValues(myServerBase + "/Patient/FOO/$everything?_content=White");
		assertThat(ids).containsExactly("Patient/FOO");

		ids = searchAndReturnUnqualifiedVersionlessIdValues(myServerBase + "/Patient/FOO/$everything?_content=HELLO");
		assertThat(ids).containsExactly("Patient/FOO");

		ids = searchAndReturnUnqualifiedVersionlessIdValues(myServerBase + "/Patient/FOO/$everything?_content=GOODBYE");
		assertThat(ids).containsExactlyInAnyOrder("Patient/FOO", "Observation/BAZ");
	}

	@Test
	public void testPagingOverEverythingSet() throws InterruptedException {
		Patient p = new Patient();
		p.setActive(true);
		String pid = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		for (int i = 0; i < 20; i++) {
			Observation o = new Observation();
			o.getSubject().setReference(pid);
			o.addIdentifier().setSystem("foo").setValue(Integer.toString(i));
			myObservationDao.create(o);
		}

		mySearchCoordinatorSvcImpl.setLoadingThrottleForUnitTests(50);
		mySearchCoordinatorSvcImpl.setSyncSizeForUnitTests(10);
		mySearchCoordinatorSvcImpl.setNeverUseLocalSearchForUnitTests(true);

		Bundle response = myClient
			.operation()
			.onInstance(new IdType(pid))
			.named("everything")
			.withSearchParameter(Parameters.class, "_count", new NumberParam(10))
			.returnResourceType(Bundle.class)
			.useHttpGet()
			.execute();

		assertThat(response.getEntry()).hasSize(10);
		if (response.getTotalElement().getValueAsString() != null) {
			assertEquals("21", response.getTotalElement().getValueAsString());
		}
		assertThat(response.getLink("next").getUrl()).isNotEmpty();

		// Load page 2

		String nextUrl = response.getLink("next").getUrl();
		response = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);

		assertThat(response.getEntry()).hasSize(10);
		if (response.getTotalElement().getValueAsString() != null) {
			assertEquals("21", response.getTotalElement().getValueAsString());
		}
		assertThat(response.getLink("next").getUrl()).isNotEmpty();

		// Load page 3
		Thread.sleep(2000);

		nextUrl = response.getLink("next").getUrl();
		response = myClient.fetchResourceFromUrl(Bundle.class, nextUrl);

		assertThat(response.getEntry()).hasSize(1);
		assertEquals("21", response.getTotalElement().getValueAsString());
		assertNull(response.getLink("next"));

	}

	@Disabled
	@Test
	public void testEverythingWithNoPagingProvider() {
		myServer.getRestfulServer().setPagingProvider(null);

		Patient p = new Patient();
		p.setActive(true);
		String pid = myPatientDao.create(p).getId().toUnqualifiedVersionless().getValue();

		for (int i = 0; i < 20; i++) {
			Observation o = new Observation();
			o.getSubject().setReference(pid);
			o.addIdentifier().setSystem("foo").setValue(Integer.toString(i));
			myObservationDao.create(o);
		}

		mySearchCoordinatorSvcImpl.setLoadingThrottleForUnitTests(50);
		mySearchCoordinatorSvcImpl.setSyncSizeForUnitTests(10);
		mySearchCoordinatorSvcImpl.setNeverUseLocalSearchForUnitTests(true);

		Bundle response = myClient
			.operation()
			.onInstance(new IdType(pid))
			.named("everything")
			.withSearchParameter(Parameters.class, "_count", new NumberParam(10))
			.returnResourceType(Bundle.class)
			.useHttpGet()
			.execute();

		assertThat(response.getEntry()).hasSize(10);
		assertNull(response.getTotalElement().getValue());
		assertNull(response.getLink("next"));
	}

	@Test
	public void testEverythingDoesNotEnterSecondPatient() {
		Patient goodPatient = new Patient();
		goodPatient.setActive(true);
		String goodPid = myPatientDao.create(goodPatient, mySrd).getId().toUnqualifiedVersionless().getValue();

		Patient badPatient = new Patient();
		badPatient.setActive(true);
		String badPid = myPatientDao.create(badPatient, mySrd).getId().toUnqualifiedVersionless().getValue();

		Observation o = new Observation();
		o.getSubject().setReference(goodPid);
		o.addIdentifier().setSystem("foo").setValue("1");
		String oid = myObservationDao.create(o, mySrd).getId().toUnqualifiedVersionless().getValue();

		Provenance prov = new Provenance();
		prov.addTarget().setReference(goodPid);
		prov.addTarget().setReference(badPid);
		String provid = myProvenanceDao.create(prov, mySrd).getId().toUnqualifiedVersionless().getValue();

		Bundle response = executeEverythingOperationOnInstance(new IdType(goodPid));

		List<String> ids = toUnqualifiedVersionlessIdValues(response);
		// We should not pick up other resources via the provenance
		assertThat(ids).containsExactlyInAnyOrder(goodPid, oid, provid);
	}

	@Test
	public void testIncludeRecurseFromProvenanceDoesTraverse() {
		Patient goodPatient = new Patient();
		goodPatient.setActive(true);
		String goodPid = myPatientDao.create(goodPatient, mySrd).getId().toUnqualifiedVersionless().getValue();

		Practitioner prac = new Practitioner();
		prac.addName().setFamily("FAM");
		String pracid = myPractitionerDao.create(prac, mySrd).getId().toUnqualifiedVersionless().getValue();

		Patient otherPatient = new Patient();
		otherPatient.setActive(true);
		otherPatient.addGeneralPractitioner().setReference(pracid);
		String otherPid = myPatientDao.create(otherPatient, mySrd).getId().toUnqualifiedVersionless().getValue();

		Provenance prov = new Provenance();
		prov.addTarget().setReference(goodPid);
		prov.addTarget().setReference(otherPid);
		String provid = myProvenanceDao.create(prov, mySrd).getId().toUnqualifiedVersionless().getValue();

		Bundle response = myClient
			.search()
			.forResource("Provenance")
			.where(Provenance.TARGET.hasId(goodPid))
			.include(new Include("*", true))
			.returnBundle(Bundle.class)
			.execute();

		List<String> ids = toUnqualifiedVersionlessIdValues(response);
		// We should not pick up other resources via the provenance
		assertThat(ids).containsExactlyInAnyOrder(goodPid, otherPid, pracid, provid);
	}

	@Test
	public void testEverythingDoesNotEnterSecondPatientLinkedByProvenanceAndComposition() {
		final Patient desiredPatient = new Patient();
		desiredPatient.setActive(true);
		final String desiredPid = myPatientDao.create(desiredPatient, mySrd).getId().toUnqualifiedVersionless().getValue();

		final Patient notDesiredPatient = new Patient();
		desiredPatient.setActive(true);
		final String notDesiredPid = myPatientDao.create(notDesiredPatient, mySrd).getId().toUnqualifiedVersionless().getValue();

		final Observation desiredObservation = new Observation();
		desiredObservation.getSubject().setReference(desiredPid);
		desiredObservation.addIdentifier().setSystem("foo").setValue("1");
		final String desiredObservationId = myObservationDao.create(desiredObservation, mySrd).getId().toUnqualifiedVersionless().getValue();

		final Observation notDesiredObservation = new Observation();
		notDesiredObservation.getSubject().setReference(notDesiredPid);
		notDesiredObservation.addIdentifier().setSystem("foo").setValue("1");
		final String notDesiredObservationId = myObservationDao.create(notDesiredObservation, mySrd).getId().toUnqualifiedVersionless().getValue();

		final Composition composition = new Composition();
		final Reference referenceToNotDesiredPatient = new Reference();
		referenceToNotDesiredPatient.setReference(notDesiredPid);
		composition.setSubject(referenceToNotDesiredPatient);
		final String compositionId = myCompositionDao.create(composition, mySrd).getId().toUnqualifiedVersionless().getValue();

		final Provenance desiredProvenance = new Provenance();
		desiredProvenance.addTarget().setReference(desiredPid);
		desiredProvenance.addTarget().setReference(compositionId);
		final String desiredProvenanceId = myProvenanceDao.create(desiredProvenance, mySrd).getId().toUnqualifiedVersionless().getValue();

		final Provenance notDesiredProvenance = new Provenance();
		notDesiredProvenance.addTarget().setReference(notDesiredPid);
		notDesiredProvenance.addTarget().setReference(compositionId);
		final String notDesiredProvenanceId = myProvenanceDao.create(notDesiredProvenance, mySrd).getId().toUnqualifiedVersionless().getValue();

		final Bundle response = executeEverythingOperationOnInstance(new IdType(desiredPid));

		final List<String> actualResourceIds = toUnqualifiedVersionlessIdValues(response);
		// We should not pick up other resources via the notDesiredProvenance
		assertThat(actualResourceIds).containsExactlyInAnyOrder(desiredPid, desiredObservationId, desiredProvenanceId);
	}

	@Test
	public void testEverything_withPatientLinkedByList_returnOnlyDesiredResources() {
		// setup
		IIdType desiredPid = createPatient(withActiveTrue());
		IIdType desiredObservationId = createObservationForPatient(desiredPid, "1");

		IIdType notDesiredPid = createPatient(withActiveTrue());
		IIdType notDesiredObservationId = createObservationForPatient(notDesiredPid, "1");

		ListResource list = new ListResource();
		Arrays.asList(desiredPid, desiredObservationId, notDesiredPid, notDesiredObservationId)
			.forEach(resourceIdType -> list.addEntry().getItem().setReferenceElement(resourceIdType));

		IIdType listId = myListDao.create(list).getId().toUnqualifiedVersionless();

		// execute
		Bundle response = executeEverythingOperationOnInstance(desiredPid);

		List<IIdType> actualResourceIds = toUnqualifiedVersionlessIds(response);
		// verify - we should not pick up other resources linked by List
		assertThat(actualResourceIds).containsExactlyInAnyOrder(desiredPid, desiredObservationId, listId);
	}

	@Test
	public void testEverything_withPatientLinkedByGroup_returnOnlyDesiredResources() {
		// setup
		IIdType desiredPractitionerId = createPractitioner(withActiveTrue());
		IIdType desiredPid = createPatient(withActiveTrue(), withReference("generalPractitioner", desiredPractitionerId));

		IIdType notDesiredPractitionerId = createPractitioner(withActiveTrue());
		IIdType notDesiredPid = createPatient(withActiveTrue(), withReference("generalPractitioner", notDesiredPractitionerId));

		Group group = new Group();
		Arrays.asList(desiredPid, desiredPractitionerId, notDesiredPid, notDesiredPractitionerId)
			.forEach(resourceIdType -> group.addMember().getEntity().setReferenceElement(resourceIdType));

		IIdType groupId = myGroupDao.create(group).getId().toUnqualifiedVersionless();

		// execute
		Bundle response = executeEverythingOperationOnInstance(desiredPid);

		List<IIdType> actualResourceIds = toUnqualifiedVersionlessIds(response);
		// verify - we should not pick up other resources linked by Group
		assertThat(actualResourceIds).containsExactlyInAnyOrder(desiredPid, desiredPractitionerId, groupId);
	}

	@Nested
	public class ResourceProviderR4EverythingWithOffsetTest {

		// Patient 1 resources
		private IIdType o1Id;
		private IIdType e1Id;
		private IIdType p1Id;
		private IIdType c1Id;

		// Patient 2 resources
		private IIdType o2Id;
		private IIdType p2Id;
		private IIdType c2Id;

		// Patient 3 resources
		private IIdType o3Id;
		private IIdType p3Id;
		private IIdType c3Id;

		// Patient 4 resources
		private IIdType o4Id;
		private IIdType p4Id;
		private IIdType c4Id;

		// Resource id not linked to any Patient
		private IIdType c5Id;

		@Test
		public void testPagingOverEverything_onPatientInstance_returnsCorrectBundles() {
			String methodName = "testEverythingPatientInstanceOffset";
			createPatientResources(methodName);

			// There are 4 results, lets make 4 pages of 1.
			Parameters parameters = new Parameters();
			addOffsetAndCount(parameters, 0, 1);

			// first page
			Parameters output = myClient.operation().onInstance(p1Id).named("everything").withParameters(parameters).execute();
			Bundle bundle = (Bundle) output.getParameter().get(0).getResource();
			List<IIdType> results = new ArrayList<>(validateAndGetIdListFromBundle(bundle, 1));

			// second page
			Bundle nextBundle = getNextBundle(bundle);
			results.addAll(validateAndGetIdListFromBundle(nextBundle, 1));

			// third page
			nextBundle = getNextBundle(nextBundle);
			results.addAll(validateAndGetIdListFromBundle(nextBundle, 1));

			// fourth page
			nextBundle = getNextBundle(nextBundle);
			results.addAll(validateAndGetIdListFromBundle(nextBundle, 1));

			// no next link
			assertNull(nextBundle.getLink("next"));
			assertThat(results).containsOnly(p1Id, c1Id, o1Id, e1Id);
		}

		@Test
		public void testPagingOverEverything_onPatientType_returnsCorrectBundles() {
			String methodName = "testEverythingPatientTypeOffset";
			createPatientResources(methodName);

			// Test paging works.
			// There are 13 results, lets make 3 pages of 4 and 1 page of 1.
			Parameters parameters = new Parameters();
			addOffsetAndCount(parameters, 0, 4);

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			validateEverythingBundle(output);
		}

		@Test
		public void testPagingOverEverything_onPatientTypeWithAndIdParameter_returnsCorrectBundles() {
			String methodName = "testEverythingPatientTypeWithAndIdParameter";
			createPatientResources(methodName);

			// Test 4 patients using and List
			// e.g. _id=1&_id=2&_id=3&_id=4
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", p1Id.getIdPart());
			parameters.addParameter("_id", p2Id.getIdPart());
			parameters.addParameter("_id", p3Id.getIdPart());
			parameters.addParameter("_id", p4Id.getIdPart());
			// Test for Patient 1-4 with _count=4 and _offset=0 with paging
			addOffsetAndCount(parameters, 0, 4);

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			validateEverythingBundle(output);
		}

		@Test
		public void testPagingOverEverything_onPatientTypeWithOrIdParameter_returnsCorrectBundles() {
			String methodName = "testEverythingPatientTypeWithOrIdParameter";
			createPatientResources(methodName);

			// Test 4 patients using or List
			// e.g. _id=1,2,3,4
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", p1Id.getIdPart() + "," + p2Id.getIdPart() + "," + p3Id.getIdPart() + "," + p4Id.getIdPart());
			// Test for Patient 1-4 with _count=4 and _offset=0 with paging
			addOffsetAndCount(parameters, 0, 4);

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			validateEverythingBundle(output);
		}

		@Test
		public void testPagingOverEverything_onPatientTypeWithOrAndIdParameter_returnsCorrectBundles() {
			String methodName = "testEverythingPatientTypeWithOrAndIdParameter";
			createPatientResources(methodName);

			// Test combining 2 or-listed params
			// e.g. _id=1,2&_id=3,4
			Parameters parameters = new Parameters();
			parameters.addParameter("_id", p1Id.getIdPart() + "," + p2Id.getIdPart());
			parameters.addParameter("_id", p3Id.getIdPart() + "," + p4Id.getIdPart());
			// Test for Patient 1-4 with _count=4 and _offset=0 with paging
			addOffsetAndCount(parameters, 0, 4);

			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			validateEverythingBundle(output);
		}

		@Test
		public void testPagingOverEverything_onPatientTypeWithNotLinkedPatients_returnsCorrectBundles() {
			String methodName = "testEverythingPatientTypeWithNotLinkedPatients";

			// setup
			p1Id = createPatient(methodName, "1");
			p2Id = createPatient(methodName, "2");
			p3Id = createPatient(methodName, "3");
			p4Id = createPatient(methodName, "4");

			// Test patients without links
			Parameters parameters = new Parameters();
			addOffsetAndCount(parameters, 0, 1);

			// first page
			Parameters output = myClient.operation().onType(Patient.class).named("everything").withParameters(parameters).execute();
			Bundle bundle = (Bundle) output.getParameter().get(0).getResource();
			List<IIdType> results = new ArrayList<>(validateAndGetIdListFromBundle(bundle, 1));

			// second page
			Bundle nextBundle = getNextBundle(bundle);
			results.addAll(validateAndGetIdListFromBundle(nextBundle, 1));

			// third page
			nextBundle = getNextBundle(nextBundle);
			results.addAll(validateAndGetIdListFromBundle(nextBundle, 1));

			// fourth page
			nextBundle = getNextBundle(nextBundle);
			results.addAll(validateAndGetIdListFromBundle(nextBundle, 1));

			// no next link
			assertNull(nextBundle.getLink("next"));
			assertThat(results).containsOnly(p1Id, p2Id, p3Id, p4Id);
		}

		private void addOffsetAndCount(Parameters theParameters, int theOffset, int theCount) {
			theParameters.addParameter(new Parameters.ParametersParameterComponent()
				.setName("_count").setValue(new UnsignedIntType(theCount)));
			theParameters.addParameter(new Parameters.ParametersParameterComponent()
				.setName("_offset").setValue(new UnsignedIntType(theOffset)));
		}

		private void validateEverythingBundle(Parameters theParameters) {
			// first page
			Bundle bundle = (Bundle) theParameters.getParameter().get(0).getResource();
			List<IIdType> results = new ArrayList<>(validateAndGetIdListFromBundle(bundle, 4));

			// second page
			Bundle nextBundle = getNextBundle(bundle);
			results.addAll(validateAndGetIdListFromBundle(nextBundle, 4));

			// third page
			nextBundle = getNextBundle(nextBundle);
			results.addAll(validateAndGetIdListFromBundle(nextBundle, 4));

			// fourth page
			nextBundle = getNextBundle(nextBundle);
			results.addAll(validateAndGetIdListFromBundle(nextBundle, 1));

			// no next link
			assertNull(nextBundle.getLink("next"));
			// make sure all resources are returned, order doesn't matter
			assertThat(results).containsOnly(p1Id, p2Id, p3Id, p4Id, c1Id, c2Id, c3Id, c4Id, o1Id, e1Id, o2Id, o3Id, o4Id);
		}

		private Bundle getNextBundle(Bundle theBundle) {
			String next = theBundle.getLink("next").getUrl();
			return myClient.loadPage().byUrl(next).andReturnBundle(Bundle.class).execute();
		}

		private List<IIdType> validateAndGetIdListFromBundle(Bundle theBundle, int theSize) {
			assertEquals(Bundle.BundleType.SEARCHSET, theBundle.getType());
			assertThat(theBundle.getEntry()).hasSize(theSize);
			return toUnqualifiedVersionlessIds(theBundle);
		}

		private void createPatientResources(String theMethodName) {
			// Patient 1 resources
			o1Id = createOrganization(theMethodName, "1");
			e1Id = createEncounter(theMethodName, "1");
			p1Id = createPatientWithIndexAtOrganization(theMethodName, "1", o1Id);
			c1Id = createConditionWithEncounterForPatient(theMethodName, "1", p1Id, e1Id);

			// Patient 2 resources
			o2Id = createOrganization(theMethodName, "2");
			p2Id = createPatientWithIndexAtOrganization(theMethodName, "2", o2Id);
			c2Id = createConditionForPatient(theMethodName, "2", p2Id);

			// Patient 3 resources
			o3Id = createOrganization(theMethodName, "3");
			p3Id = createPatientWithIndexAtOrganization(theMethodName, "3", o3Id);
			c3Id = createConditionForPatient(theMethodName, "3", p3Id);

			// Patient 4 resources
			o4Id = createOrganization(theMethodName, "4");
			p4Id = createPatientWithIndexAtOrganization(theMethodName, "4", o4Id);
			c4Id = createConditionForPatient(theMethodName, "4", p4Id);

			// Resource not linked to any Patient
			c5Id = createConditionForPatient(theMethodName, "5", null);
		}
	}

	private Bundle executeEverythingOperationOnInstance(IIdType theInstanceIdType) {
		return myClient
			.operation()
			.onInstance(theInstanceIdType)
			.named("everything")
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();
	}

	private IIdType createOrganization(String methodName, String theIndex) {
		Organization o1 = new Organization();
		o1.setName(methodName + theIndex);
		return myClient.create().resource(o1).execute().getId().toUnqualifiedVersionless();
	}

	private IIdType createEncounter(String methodName, String theIndex) {
		Encounter e1 = new Encounter();
		e1.setLanguage(methodName + theIndex);
		return myClient.create().resource(e1).execute().getId().toUnqualifiedVersionless();
	}

	public IIdType createPatient(String theMethodName, String theIndex) {
		return createPatientWithIndexAtOrganization(theMethodName, theIndex, null);
	}

	public IIdType createPatientWithIndexAtOrganization(String theMethodName, String theIndex, IIdType theOrganizationId) {
		Patient p1 = new Patient();
		p1.addName().setFamily(theMethodName + theIndex);
		p1.getManagingOrganization().setReferenceElement(theOrganizationId);
		IIdType p1Id = myClient.create().resource(p1).execute().getId().toUnqualifiedVersionless();
		return p1Id;
	}

	public IIdType createConditionForPatient(String theMethodName, String theIndex, IIdType thePatientId) {
		return createConditionWithEncounterForPatient(theMethodName, theIndex, thePatientId, null);
	}

	public IIdType createConditionWithEncounterForPatient(String theMethodName,
														  String theIndex,
														  IIdType thePatientId,
														  IIdType theEncounterId) {
		Condition c = new Condition();
		c.addIdentifier().setValue(theMethodName + theIndex);
		if (thePatientId != null) {
			c.getSubject().setReferenceElement(thePatientId);
		}
		if (theEncounterId != null) {
			c.setEncounter(new Reference(theEncounterId));
		}
		return myClient.create().resource(c).execute().getId().toUnqualifiedVersionless();
	}

	private IIdType createMedicationRequestForPatient(IIdType thePatientId, String theIndex) {
		MedicationRequest m = new MedicationRequest();
		m.addIdentifier().setValue(theIndex);
		if (thePatientId != null) {
			m.getSubject().setReferenceElement(thePatientId);
		}
		IIdType mId = myClient.create().resource(m).execute().getId().toUnqualifiedVersionless();
		return mId;
	}

	private IIdType createObservationForPatient(IIdType thePatientId, String theIndex) {
		Observation o = new Observation();
		o.addIdentifier().setValue(theIndex);
		if (thePatientId != null) {
			o.getSubject().setReferenceElement(thePatientId);
		}
		IIdType oId = myClient.create().resource(o).execute().getId().toUnqualifiedVersionless();
		return oId;
	}
}
