package ca.uhn.fhir.jpa.term;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import com.google.common.collect.Lists;
import org.hl7.fhir.dstu3.model.CodeType;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.Parameters;
import org.hl7.fhir.dstu3.model.PrimitiveType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.dstu3.model.UriType;
import org.hl7.fhir.dstu3.model.ValueSet;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

public class TerminologyLoaderSvcIntegrationDstu3Test extends BaseJpaDstu3Test {

	private static final Logger ourLog = LoggerFactory.getLogger(TerminologyLoaderSvcIntegrationDstu3Test.class);

	@Autowired
	private ITermLoaderSvc myLoader;

	@AfterEach
	public void after() {
		myStorageSettings.setDeferIndexingForCodesystemsOfSize(new JpaStorageSettings().getDeferIndexingForCodesystemsOfSize());
	}

	@BeforeEach
	public void before() {
		myStorageSettings.setDeferIndexingForCodesystemsOfSize(20000);
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
		TermTestUtil.addLoincMandatoryFilesToZip(files);
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
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded));
		ourLog.info("Codes: {}", codes);
		assertThat(codes).containsExactlyInAnyOrder("10013-1");

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
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded));
		assertThat(codes).containsExactlyInAnyOrder("10013-1");

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
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded));
		assertThat(codes).isEmpty();
	}

	@Test
	public void testStoreAndProcessDeferred() throws IOException {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		myTerminologyDeferredStorageSvc.saveDeferred();

		await().untilAsserted(() -> runInTransaction(() -> assertThat(myTermConceptMapDao.count()).isGreaterThan(0L)));
	}

	@Test
	public void testExpandWithPropertyString() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesToZip(files);
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
		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded));
		ourLog.info("Codes: {}", codes);
		assertThat(codes).containsExactlyInAnyOrder("10013-1");
	}

	@Test
	public void testLookupWithProperties() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		IValidationSupport.LookupCodeResult result = myCodeSystemDao.lookupCode(new StringType("10013-1"), new StringType(ITermLoaderSvc.LOINC_URI), null, mySrd);
		Parameters parameters = (Parameters) result.toParameters(myFhirContext, null);

		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(parameters));

		Optional<Coding> propertyValue = findProperty(parameters, "SCALE_TYP");
		assertThat(propertyValue).isPresent();
		assertEquals(ITermLoaderSvc.LOINC_URI, propertyValue.get().getSystem());
		assertEquals("LP7753-9", propertyValue.get().getCode());
		assertEquals("Qn", propertyValue.get().getDisplay());

		propertyValue = findProperty(parameters, "COMPONENT");
		assertThat(propertyValue).isPresent();

		Optional<StringType> propertyValueString = findProperty(parameters, "ORDER_OBS");
		assertThat(propertyValueString).isPresent();
		assertEquals("Observation", propertyValueString.get().getValue());

		propertyValueString = findProperty(parameters, "CLASSTYPE");
		assertThat(propertyValueString).isPresent();
		assertEquals("2", propertyValueString.get().getValue());

	}

	@Test
	public void testLookupWithProperties2() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		IValidationSupport.LookupCodeResult result = myCodeSystemDao.lookupCode(new StringType("10013-1"), new StringType(ITermLoaderSvc.LOINC_URI), null, mySrd);
		Parameters parameters = (Parameters) result.toParameters(myFhirContext, null);

		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(parameters));

		Optional<Coding> propertyValue = findProperty(parameters, "COMPONENT");
		assertThat(propertyValue).isPresent();
		assertEquals(ITermLoaderSvc.LOINC_URI, propertyValue.get().getSystem());
		assertEquals("LP31101-6", propertyValue.get().getCode());
		assertEquals("R' wave amplitude.lead I", propertyValue.get().getDisplay());
	}

	@Test
	public void testLookupWithPropertiesExplicit() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		IValidationSupport.LookupCodeResult result = myCodeSystemDao.lookupCode(new StringType("10013-1"), new StringType(ITermLoaderSvc.LOINC_URI), null, mySrd);
		List<? extends IPrimitiveType<String>> properties = Lists.newArrayList(new CodeType("SCALE_TYP"));
		Parameters parameters = (Parameters) result.toParameters(myFhirContext, properties);

		ourLog.debug(myFhirContext.newXmlParser().setPrettyPrint(true).encodeResourceToString(parameters));

		Optional<Coding> propertyValueCoding = findProperty(parameters, "SCALE_TYP");
		assertThat(propertyValueCoding).isPresent();
		assertEquals(ITermLoaderSvc.LOINC_URI, propertyValueCoding.get().getSystem());
		assertEquals("LP7753-9", propertyValueCoding.get().getCode());
		assertEquals("Qn", propertyValueCoding.get().getDisplay());

		propertyValueCoding = findProperty(parameters, "COMPONENT");
		assertFalse(propertyValueCoding.isPresent());

	}

	@Test
	public void testValidateCodeFound() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(new UriType("http://loinc.org/vs"), null, new StringType("10013-1"), new StringType(ITermLoaderSvc.LOINC_URI), null, null, null, mySrd);

		assertTrue(result.isOk());
		assertEquals("R' wave amplitude in lead I", result.getDisplay());
	}

	@Test
	public void testValidateCodeNotFound() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TermTestUtil.addLoincMandatoryFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);
		myTerminologyDeferredStorageSvc.saveDeferred();
		myTerminologyDeferredStorageSvc.saveDeferred();

		IValidationSupport.CodeValidationResult result = myValueSetDao.validateCode(new UriType("http://loinc.org/vs"), null, new StringType("10013-1-9999999999"), new StringType(ITermLoaderSvc.LOINC_URI), null, null, null, mySrd);
		assertFalse(result.isOk());
		assertEquals("Unknown code 'http://loinc.org#10013-1-9999999999' for in-memory expansion of ValueSet 'http://loinc.org/vs'", result.getMessage());
	}

	private Set<String> toExpandedCodes(ValueSet theExpanded) {
		return theExpanded
			.getExpansion()
			.getContains()
			.stream()
			.map(ValueSet.ValueSetExpansionContainsComponent::getCode)
			.collect(Collectors.toSet());
	}


}
