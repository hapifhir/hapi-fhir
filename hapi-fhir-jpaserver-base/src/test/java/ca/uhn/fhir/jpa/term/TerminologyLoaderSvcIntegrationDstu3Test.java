package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.util.TestUtil;
import com.google.common.collect.Lists;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class TerminologyLoaderSvcIntegrationDstu3Test extends BaseJpaDstu3Test {

	private static final Logger ourLog = LoggerFactory.getLogger(TerminologyLoaderSvcIntegrationDstu3Test.class);

	@Autowired
	private ITermLoaderSvc myLoader;

	@After
	public void after() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(new DaoConfig().getDeferIndexingForCodesystemsOfSize());
	}

	@Before
	public void before() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(20000);
	}

	@SuppressWarnings("unchecked")
	private <T extends Type> Optional<T> findProperty(Parameters theParameters, String thePropertyName) {
		return theParameters
			.getParameter()
			.stream()
			.filter(t -> t.getName().equals("property"))
			.filter(t -> ((PrimitiveType<?>) t.getPart().get(0).getValue()).getValueAsString().equals(thePropertyName))
			.map(t -> (T) t.getPart().get(1).getValue())
			.findFirst();
	}

	@Test
	public void testExpandWithPropertyCoding() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		// Search by code
		ValueSet input = new ValueSet();
		input
			.getCompose()
			.addInclude()
			.setSystem(ITermLoaderSvc.LOINC_URI)
			.addFilter()
			.setProperty("SCALE_TYP")
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("LP7753-9");
		ValueSet expanded = myValueSetDao.expand(input, null);
		Set<String> codes = toExpandedCodes(expanded);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded));
		ourLog.info("Codes: {}", codes);
		assertThat(codes, containsInAnyOrder("10019-8", "10013-1", "10014-9", "10016-4", "17788-1", "10000-8", "10017-2", "10015-6", "10020-6", "10018-0"));

		// Search by display name
		input = new ValueSet();
		input
			.getCompose()
			.addInclude()
			.setSystem(ITermLoaderSvc.LOINC_URI)
			.addFilter()
			.setProperty("SCALE_TYP")
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("Qn");
		expanded = myValueSetDao.expand(input, null);
		codes = toExpandedCodes(expanded);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded));
		assertThat(codes, containsInAnyOrder("10019-8", "10013-1", "10014-9", "10016-4", "17788-1", "10000-8", "10017-2", "10015-6", "10020-6", "10018-0"));

		// Search by something that doesn't match
		input = new ValueSet();
		input
			.getCompose()
			.addInclude()
			.setSystem(ITermLoaderSvc.LOINC_URI)
			.addFilter()
			.setProperty("SCALE_TYP")
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("Qn999");
		expanded = myValueSetDao.expand(input, null);
		codes = toExpandedCodes(expanded);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded));
		assertThat(codes, empty());
	}

	@Test
	public void testStoreAndProcessDeferred() throws IOException {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		myTerminologyDeferredStorageSvc.saveDeferred();

		await().until(() -> runInTransaction(() -> myTermConceptMapDao.count()), greaterThan(0L));
	}

	@Test
	public void testExpandWithPropertyString() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		ValueSet input = new ValueSet();
		input
			.getCompose()
			.addInclude()
			.setSystem(ITermLoaderSvc.LOINC_URI)
			.addFilter()
			.setProperty("CLASS")
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("EKG.MEAS");
		ValueSet expanded = myValueSetDao.expand(input, null);
		Set<String> codes = toExpandedCodes(expanded);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded));
		ourLog.info("Codes: {}", codes);
		assertThat(codes, containsInAnyOrder("10019-8", "10013-1", "10014-9", "10000-8", "10016-4", "10017-2", "10015-6", "10020-6", "10018-0"));
	}

	@Test
	public void testLookupWithProperties() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		IValidationSupport.LookupCodeResult result = myCodeSystemDao.lookupCode(new StringType("10013-1"), new StringType(ITermLoaderSvc.LOINC_URI), null, mySrd);
		Parameters parameters = (Parameters) result.toParameters(myFhirCtx, null);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parameters));

		Optional<Coding> propertyValue = findProperty(parameters, "SCALE_TYP");
		assertTrue(propertyValue.isPresent());
		assertEquals(ITermLoaderSvc.LOINC_URI, propertyValue.get().getSystem());
		assertEquals("LP7753-9", propertyValue.get().getCode());
		assertEquals("Qn", propertyValue.get().getDisplay());

		propertyValue = findProperty(parameters, "COMPONENT");
		assertTrue(propertyValue.isPresent());

		Optional<StringType> propertyValueString = findProperty(parameters, "ORDER_OBS");
		assertTrue(propertyValueString.isPresent());
		assertEquals("Observation", propertyValueString.get().getValue());

		propertyValueString = findProperty(parameters, "CLASSTYPE");
		assertTrue(propertyValueString.isPresent());
		assertEquals("2", propertyValueString.get().getValue());

	}

	@Test
	public void testLookupWithProperties2() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		IValidationSupport.LookupCodeResult result = myCodeSystemDao.lookupCode(new StringType("17788-1"), new StringType(ITermLoaderSvc.LOINC_URI), null, mySrd);
		Parameters parameters = (Parameters) result.toParameters(myFhirCtx, null);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parameters));

		Optional<Coding> propertyValue = findProperty(parameters, "COMPONENT");
		assertTrue(propertyValue.isPresent());
		assertEquals(ITermLoaderSvc.LOINC_URI, propertyValue.get().getSystem());
		assertEquals("LP19258-0", propertyValue.get().getCode());
		assertEquals("Large unstained cells/100 leukocytes", propertyValue.get().getDisplay());
	}

	@Test
	public void testLookupWithPropertiesExplicit() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		IValidationSupport.LookupCodeResult result = myCodeSystemDao.lookupCode(new StringType("10013-1"), new StringType(ITermLoaderSvc.LOINC_URI), null, mySrd);
		List<? extends IPrimitiveType<String>> properties = Lists.newArrayList(new CodeType("SCALE_TYP"));
		Parameters parameters = (Parameters) result.toParameters(myFhirCtx, properties);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parameters));

		Optional<Coding> propertyValueCoding = findProperty(parameters, "SCALE_TYP");
		assertTrue(propertyValueCoding.isPresent());
		assertEquals(ITermLoaderSvc.LOINC_URI, propertyValueCoding.get().getSystem());
		assertEquals("LP7753-9", propertyValueCoding.get().getCode());
		assertEquals("Qn", propertyValueCoding.get().getDisplay());

		propertyValueCoding = findProperty(parameters, "COMPONENT");
		assertFalse(propertyValueCoding.isPresent());

	}

	@Test
	public void testValidateCodeFound() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(null, null, new StringType("10013-1"), new StringType(ITermLoaderSvc.LOINC_URI), null, null, null, mySrd);

		assertTrue(result.isResult());
		assertEquals("Found code", result.getMessage());
	}

	@Test
	public void testValidateCodeNotFound() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		IFhirResourceDaoValueSet.ValidateCodeResult result = myValueSetDao.validateCode(null, null, new StringType("10013-1-9999999999"), new StringType(ITermLoaderSvc.LOINC_URI), null, null, null, mySrd);

		assertFalse(result.isResult());
		assertEquals("Code not found", result.getMessage());
	}

	private Set<String> toExpandedCodes(ValueSet theExpanded) {
		return theExpanded
			.getExpansion()
			.getContains()
			.stream()
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
			.collect(Collectors.toSet());
	}

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
