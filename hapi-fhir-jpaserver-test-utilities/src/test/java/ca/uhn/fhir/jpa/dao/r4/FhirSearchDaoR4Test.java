package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.dao.IFulltextSearchSvc;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.api.SearchTotalModeEnum;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.StringAndListParam;
import ca.uhn.fhir.rest.param.StringOrListParam;
import ca.uhn.fhir.rest.param.StringParam;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.util.ResourceSearchParams;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

public class FhirSearchDaoR4Test extends BaseJpaR4Test {

	@Autowired
	private IFulltextSearchSvc mySearchDao;

	@Test
	public void testDaoCallRequiresTransaction() {

		try {
			myResourceTableDao.count();
		} catch (InvalidDataAccessApiUsageException e) {
			// good
		}

		assert !TransactionSynchronizationManager.isActualTransactionActive();
	}

	@Test
	public void testSearchReturnsExpectedPatientsWhenContentTypeUsed() {
		// setup
		String content = "yui";

		Long id1;
		{
			Patient patient = new Patient();
			patient.addName().addGiven(content).setFamily("hirasawa");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.addName().addGiven("mio").setFamily("akiyama");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap params = new SearchParameterMap();
		params.add("_content", new StringParam(content));

		// test
		List<ResourcePersistentId> ids = mySearchDao.search("Patient",
			params);

		// verify results
		Assertions.assertEquals(1, ids.size());
		Assertions.assertEquals(id1, ids.get(0).getIdAsLong());
	}

	@Test
	public void testSearchesWithAccurateCountReturnOnlyExpectedResults() {
		// create 2 patients
		Patient patient = new Patient();
		patient.addName().setFamily("hirasawa");
		myPatientDao.create(patient);

		Patient patient2 = new Patient();
		patient2.addName().setFamily("akiyama");
		myPatientDao.create(patient2);

		// construct searchmap with Accurate search mode
		SearchParameterMap map = new SearchParameterMap();
		map.add(Constants.PARAM_CONTENT, new StringParam("hirasawa"));
		map.setSearchTotalMode(SearchTotalModeEnum.ACCURATE);

		// test
		IBundleProvider ret = myPatientDao.search(map);

		// only one should be returned
		Assertions.assertEquals(1, ret.size());
		Patient retPatient = (Patient) ret.getAllResources().get(0);
		Assertions.assertEquals(patient.getName().get(0).getFamily(),
			retPatient.getName().get(0).getFamily());
	}

	@Test
	public void testContentSearch() {
		Long id1;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("001");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_h\u00F6ra");
			patient.addName().addGiven("AAAS");
			patient.addName().addGiven("CCC");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.addIdentifier().setSystem("urn:system").setValue("002");
			patient.addName().addGiven("testSearchStringParamWithNonNormalized_HORA");
			patient.addName().addGiven("AAAB");
			patient.addName().addGiven("CCC");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id3;
		{
			Organization org = new Organization();
			org.setName("DDD");
			id3 = myOrganizationDao.create(org, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap map;
		String resourceName = "Patient";

		// One term
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1));
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2));
		}
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1));
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2));
		}
		// All Resource Types
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")).addOr(new StringParam("DDD")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_CONTENT, content);
			List<ResourcePersistentId> found = mySearchDao.search(null, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2, id3));
		}

	}

	@Test
	public void testNarrativeSearch() {
		Long id1;
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAS<p>FOO</p> CCC    </div>");
			id1 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		Long id2;
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>AAAB<p>FOO</p> CCC    </div>");
			id2 = myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}
		{
			Patient patient = new Patient();
			patient.getText().setDivAsString("<div>ZZYZXY</div>");
			myPatientDao.create(patient, mySrd).getId().toUnqualifiedVersionless().getIdPartAsLong();
		}

		SearchParameterMap map;
		String resourceName = "Patient";

		// One term
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1));
		}
		// OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")).addOr(new StringParam("AAAB")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2));
		}
		// AND
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1));
		}
		// AND OR
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("AAAB")).addOr(new StringParam("AAAS")));
			content.addAnd(new StringOrListParam().addOr(new StringParam("CCC")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), containsInAnyOrder(id1, id2));
		}
		// Tag Contents
		{
			StringAndListParam content = new StringAndListParam();
			content.addAnd(new StringOrListParam().addOr(new StringParam("div")));

			map = new SearchParameterMap();
			map.add(Constants.PARAM_TEXT, content);
			List<ResourcePersistentId> found = mySearchDao.search(resourceName, map);
			assertThat(ResourcePersistentId.toLongList(found), empty());
		}
	}

	private void createJankyCustomSp() {
		SearchParameter sp = new SearchParameter();
		sp.setId("condition-risk-scores");
		sp.addExtension().setUrl("http://hapifhir.io/fhir/StructureDefinition/sp-unique").setValue(new BooleanType(false));
		sp.setStatus(Enumerations.PublicationStatus.ACTIVE);
		sp.setCode("risk-scores");
		sp.setBase(Collections.singletonList(new CodeType("Condition")));
		sp.setType(Enumerations.SearchParamType.COMPOSITE);
		sp.setExpression("Condition");
		sp.addComponent()
			.setDefinition("SearchParameter/Condition-clinical-status")
			.setExpression("Condition");
		sp.addComponent()
			.setDefinition("SearchParameter/Condition-verification-status")
			.setExpression("Condition");
		sp.addComponent()
			.setDefinition("SearchParameter/clinical-identifier")
			.setExpression("Condition");
		sp.addComponent()
			.setDefinition("SearchParameter/clinical-patient")
			.setExpression("Condition");
		mySearchParameterDao.update(sp);
		mySearchParamRegistry.forceRefresh();

	}

	@Test
	public void testConditionSearchWithTokenIsIgnored() {
		Patient p = new Patient();
		p.setId("patient");
		myPatientDao.update(p);
		ResourceSearchParams conditions = mySearchParamRegistry.getActiveSearchParams("Condition");
		createJankyCustomSp();
		Condition inactiveUnconfirmedCondition = new Condition();
		inactiveUnconfirmedCondition.setId("inactiveUnconfirmedCondition");
		inactiveUnconfirmedCondition.addIdentifier().setSystem("http://foo").setValue("123");
		inactiveUnconfirmedCondition.setSubject(new Reference("Patient/patient"));
		inactiveUnconfirmedCondition.setClinicalStatus(new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical").setCode("inactive")));
		inactiveUnconfirmedCondition.setVerificationStatus(new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/condition-ver-status").setCode("unconfirmed")));
		myConditionDao.update(inactiveUnconfirmedCondition);

		Condition activeConfirmedCondition = new Condition();
		activeConfirmedCondition.setId("activeConfirmedCondition");
		activeConfirmedCondition.addIdentifier().setSystem("http://foo").setValue("456");
		activeConfirmedCondition.setSubject(new Reference("Patient/patient"));
		activeConfirmedCondition.setClinicalStatus(new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical").setCode("active")));
		activeConfirmedCondition.setVerificationStatus(new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/condition-ver-status").setCode("confirmed")));
		myConditionDao.update(activeConfirmedCondition);

		Condition activeUnconfirmedCondition = new Condition();
		activeUnconfirmedCondition.setId("activeUnconfirmedCondition");
		activeUnconfirmedCondition.addIdentifier().setSystem("http://foo").setValue("456");
		activeUnconfirmedCondition.setSubject(new Reference("Patient/patient"));
		activeUnconfirmedCondition.setClinicalStatus(new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/condition-clinical").setCode("active")));
		activeUnconfirmedCondition.setVerificationStatus(new CodeableConcept().addCoding(new Coding().setSystem("http://terminology.hl7.org/CodeSystem/condition-ver-status").setCode("unconfirmed")));
		myConditionDao.update(activeUnconfirmedCondition);



		TokenParam active = new TokenParam("http://terminology.hl7.org/CodeSystem/condition-clinical", "active");
		TokenParam inactive = new TokenParam("http://terminology.hl7.org/CodeSystem/condition-clinical", "inactive");
		TokenParam confirmed = new TokenParam("http://terminology.hl7.org/CodeSystem/condition-ver-status", "confirmed");
		TokenParam unconfirmed = new TokenParam("http://terminology.hl7.org/CodeSystem/condition-ver-status", "unconfirmed");
		SearchParameterMap spMap = new SearchParameterMap();
		spMap.add(Condition.SP_IDENTIFIER, new TokenParam("http://foo", ""));
		spMap.add(Condition.SP_PATIENT, new ReferenceParam("Patient/patient"));
		IBundleProvider search = myConditionDao.search(spMap);
		assertThat(search.size(), is(equalTo(3)));

		spMap = new SearchParameterMap();
		spMap.add(Condition.SP_IDENTIFIER, new TokenParam("http://foo", ""));
		spMap.add(Condition.SP_CLINICAL_STATUS, active);
		spMap.add(Condition.SP_VERIFICATION_STATUS, unconfirmed);
		search = myConditionDao.search(spMap);
		assertThat(search.size(), is(equalTo(1)));

		spMap = new SearchParameterMap();
		spMap.add(Condition.SP_IDENTIFIER, new TokenParam("http://foo", ""));
		spMap.add(Condition.SP_PATIENT, new ReferenceParam("Patient/patient"));
		spMap.add(Condition.SP_CLINICAL_STATUS, active);
		spMap.add(Condition.SP_VERIFICATION_STATUS, confirmed);
		search = myConditionDao.search(spMap);
		assertThat(search.size(), is(equalTo(1)));

		spMap = new SearchParameterMap();
		spMap.add(Condition.SP_IDENTIFIER, new TokenParam("http://foo", ""));
		spMap.add(Condition.SP_PATIENT, new ReferenceParam("Patient/patient"));
		spMap.add(Condition.SP_CLINICAL_STATUS, inactive);
		spMap.add(Condition.SP_VERIFICATION_STATUS, unconfirmed);
		search = myConditionDao.search(spMap);
		assertThat(search.size(), is(equalTo(1)));


	}

}
