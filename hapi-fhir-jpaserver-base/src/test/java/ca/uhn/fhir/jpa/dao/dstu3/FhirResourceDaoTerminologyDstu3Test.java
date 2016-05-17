package ca.uhn.fhir.jpa.dao.dstu3;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.util.Set;

import org.hl7.fhir.dstu3.model.AuditEvent;
import org.hl7.fhir.dstu3.model.CodeSystem;
import org.hl7.fhir.dstu3.model.CodeSystem.CodeSystemContentMode;
import org.hl7.fhir.dstu3.model.CodeSystem.ConceptDefinitionComponent;
import org.hl7.fhir.dstu3.model.Observation;
import org.hl7.fhir.dstu3.model.Observation.ObservationStatus;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import ca.uhn.fhir.jpa.dao.SearchParameterMap;
import ca.uhn.fhir.jpa.entity.TermConcept;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.param.TokenParamModifier;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public class FhirResourceDaoTerminologyDstu3Test extends BaseJpaDstu3Test {

	private static final String URL_MY_VALUE_SET = "http://example.com/my_value_set";
	private static final String URL_MY_CODE_SYSTEM = "http://example.com/my_code_system";
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoTerminologyDstu3Test.class);

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@Test
	public void testCodeSystemWithDefinedCodes() {
		//@formatter:off
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);		
		codeSystem
			.addConcept().setCode("A").setDisplay("Code A")
				.addConcept(new ConceptDefinitionComponent().setCode("AA").setDisplay("Code AA"))
				.addConcept(new ConceptDefinitionComponent().setCode("AB").setDisplay("Code AB"));
		codeSystem
			.addConcept().setCode("B").setDisplay("Code A")
				.addConcept(new ConceptDefinitionComponent().setCode("BA").setDisplay("Code AA"))
				.addConcept(new ConceptDefinitionComponent().setCode("BB").setDisplay("Code AB"));
		//@formatter:on
		
		IIdType id = myCodeSystemDao.create(codeSystem, new ServletRequestDetails()).getId().toUnqualified();
		
		Set<TermConcept> codes = myTermSvc.findCodesBelow(id.getIdPartAsLong(), id.getVersionIdPartAsLong(), "A");
		assertThat(toCodes(codes), containsInAnyOrder("A", "AA", "AB"));
		
	}

	@Test
	@Ignore
	public void testSearchCodeInEmptyValueSet() {
		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		myValueSetDao.create(valueSet, mySrd);
		
		Observation obsAA = new Observation();
		obsAA.setStatus(ObservationStatus.FINAL);
		obsAA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("AA");
		myObservationDao.create(obsAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsBA = new Observation();
		obsBA.setStatus(ObservationStatus.FINAL);
		obsBA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("BA");
		myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.setStatus(ObservationStatus.FINAL);
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params;
		
		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());
		
		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		params.add(Observation.SP_STATUS, new TokenParam(null, "final"));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());
	}

	@Test
	public void testSearchCodeInLocalCodesystem() {
		createLocalCsAndVs();
		
		Observation obsAA = new Observation();
		obsAA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("AA");
		IIdType idAA = myObservationDao.create(obsAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsBA = new Observation();
		obsBA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("BA");
		IIdType idBA = myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		IIdType idCA = myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(null, URL_MY_VALUE_SET).setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAA.getValue(), idBA.getValue()));
		
	}

	@Test
	public void testSearchCodeInFhirCodesystem() {
		createLocalCsAndVs();
		
		AuditEvent aeIn1 = new AuditEvent();
		aeIn1.getType().setSystem("http://nema.org/dicom/dicm").setCode("110102");
		IIdType idIn1 = myAuditEventDao.create(aeIn1, mySrd).getId().toUnqualifiedVersionless();
		
		AuditEvent aeIn2 = new AuditEvent();
		aeIn2.getType().setSystem("http://hl7.org/fhir/audit-event-type").setCode("rest");
		IIdType idIn2 = myAuditEventDao.create(aeIn2, mySrd).getId().toUnqualifiedVersionless();

		AuditEvent aeOut1 = new AuditEvent();
		aeOut1.getType().setSystem("http://example.com").setCode("foo");
		IIdType idOut1 = myAuditEventDao.create(aeOut1, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(AuditEvent.SP_TYPE, new TokenParam(null, "http://hl7.org/fhir/ValueSet/audit-event-type").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAuditEventDao.search(params)), containsInAnyOrder(idIn1.getValue(), idIn2.getValue()));		

		params = new SearchParameterMap();
		params.add(AuditEvent.SP_TYPE, new TokenParam(null, "http://hl7.org/fhir/ValueSet/v3-PurposeOfUse").setModifier(TokenParamModifier.IN));
		assertThat(toUnqualifiedVersionlessIdValues(myAuditEventDao.search(params)), empty());		
	}

	/**
	 * Can't currently abort costly 
	 */
	@Test
	@Ignore
	public void testRefuseCostlyExpansionFhirCodesystem() {
		createLocalCsAndVs();
		myDaoConfig.setMaximumExpansionSize(1);
		
		SearchParameterMap params = new SearchParameterMap();
		params.add(AuditEvent.SP_TYPE, new TokenParam(null, "http://hl7.org/fhir/ValueSet/audit-event-type").setModifier(TokenParamModifier.IN));
		try {
			myAuditEventDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("", e.getMessage());
		}
	}

	@Test
	public void testRefuseCostlyExpansionLocalCodesystem() {
		createLocalCsAndVs();
		myDaoConfig.setMaximumExpansionSize(1);
		
		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "AAA").setModifier(TokenParamModifier.ABOVE));
		try {
			myObservationDao.search(params);
			fail();
		} catch (InvalidRequestException e) {
			assertEquals("Expansion of ValueSet produced too many codes (maximum 1) - Operation aborted!", e.getMessage());
		}
	}

	@Test
	public void testSearchCodeAboveLocalCodesystem() {
		createLocalCsAndVs();
		
		Observation obsAA = new Observation();
		obsAA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("AA");
		IIdType idAA = myObservationDao.create(obsAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsBA = new Observation();
		obsBA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("BA");
		IIdType idBA = myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		IIdType idCA = myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "AAA").setModifier(TokenParamModifier.ABOVE));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAA.getValue()));
		
		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "A").setModifier(TokenParamModifier.ABOVE));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());
		
	}

	@Before
	public void before() {
		myDaoConfig.setMaximumExpansionSize(5000);
	}
	
	@Test
	public void testSearchCodeBelowLocalCodesystem() {
		createLocalCsAndVs();
		
		Observation obsAA = new Observation();
		obsAA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("AA");
		IIdType idAA = myObservationDao.create(obsAA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsBA = new Observation();
		obsBA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("BA");
		IIdType idBA = myObservationDao.create(obsBA, mySrd).getId().toUnqualifiedVersionless();

		Observation obsCA = new Observation();
		obsCA.getCode().addCoding().setSystem(URL_MY_CODE_SYSTEM).setCode("CA");
		IIdType idCA = myObservationDao.create(obsCA, mySrd).getId().toUnqualifiedVersionless();

		SearchParameterMap params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "A").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), containsInAnyOrder(idAA.getValue()));
		
		params = new SearchParameterMap();
		params.add(Observation.SP_CODE, new TokenParam(URL_MY_CODE_SYSTEM, "AAA").setModifier(TokenParamModifier.BELOW));
		assertThat(toUnqualifiedVersionlessIdValues(myObservationDao.search(params)), empty());

	}

	private void createLocalCsAndVs() {
		//@formatter:off
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);		
		codeSystem
			.addConcept().setCode("A").setDisplay("Code A")
				.addConcept(new ConceptDefinitionComponent().setCode("AA").setDisplay("Code AA")
					.addConcept(new ConceptDefinitionComponent().setCode("AAA").setDisplay("Code AAA"))
				)
				.addConcept(new ConceptDefinitionComponent().setCode("AB").setDisplay("Code AB"));
		codeSystem
			.addConcept().setCode("B").setDisplay("Code B")
				.addConcept(new ConceptDefinitionComponent().setCode("BA").setDisplay("Code BA"))
				.addConcept(new ConceptDefinitionComponent().setCode("BB").setDisplay("Code BB"));
		//@formatter:on
		myCodeSystemDao.create(codeSystem, new ServletRequestDetails());

		ValueSet valueSet = new ValueSet();
		valueSet.setUrl(URL_MY_VALUE_SET);
		valueSet.getCompose().addInclude().setSystem(codeSystem.getUrl());
		myValueSetDao.create(valueSet, mySrd);
	}

	
	@Test
	public void testCodeSystemCreateDuplicateFails() {
		CodeSystem codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);		
		IIdType id = myCodeSystemDao.create(codeSystem, new ServletRequestDetails()).getId().toUnqualified();

		codeSystem = new CodeSystem();
		codeSystem.setUrl(URL_MY_CODE_SYSTEM);
		codeSystem.setContent(CodeSystemContentMode.COMPLETE);		
		try {
			myCodeSystemDao.create(codeSystem, new ServletRequestDetails());
			fail();
		} catch (UnprocessableEntityException e) {
			assertEquals("Can not create multiple code systems with URI \"http://example.com/my_code_system\", already have one with resource ID: CodeSystem/" + id.getIdPart(), e.getMessage());
		}
	}

	
	
	
}
