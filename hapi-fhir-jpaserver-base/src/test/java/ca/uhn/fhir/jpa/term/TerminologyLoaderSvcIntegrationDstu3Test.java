package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.IFhirResourceDaoCodeSystem;
import ca.uhn.fhir.jpa.dao.dstu3.BaseJpaDstu3Test;
import ca.uhn.fhir.util.TestUtil;
import org.hl7.fhir.convertors.VersionConvertor_30_40;
import org.hl7.fhir.dstu3.model.*;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class TerminologyLoaderSvcIntegrationDstu3Test extends BaseJpaDstu3Test {

	private static final Logger ourLog = LoggerFactory.getLogger(TerminologyLoaderSvcIntegrationDstu3Test.class);

	@Autowired
	private IHapiTerminologyLoaderSvc myLoader;

	@After
	public void after() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(new DaoConfig().getDeferIndexingForCodesystemsOfSize());
	}

	@Before
	public void before() {
		myDaoConfig.setDeferIndexingForCodesystemsOfSize(20000);
	}

	@Test
	public void testExpandWithProperty() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		TerminologyLoaderSvcLoincTest.addLoincOptionalFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		ValueSet input = new ValueSet();
		input
			.getCompose()
			.addInclude()
			.setSystem(IHapiTerminologyLoaderSvc.LOINC_URI)
			.addFilter()
			.setProperty("SCALE_TYP")
			.setOp(ValueSet.FilterOperator.EQUAL)
			.setValue("Ord");
		ValueSet expanded = myValueSetDao.expand(input, null);
		Set<String> codes = toExpandedCodes(expanded);
		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(expanded));
		assertThat(codes, containsInAnyOrder("1001-7", "61438-8"));
	}

	@Test
	public void testLookupWithProperties() throws Exception {
		ZipCollectionBuilder files = new ZipCollectionBuilder();
		TerminologyLoaderSvcLoincTest.addLoincMandatoryFilesToZip(files);
		TerminologyLoaderSvcLoincTest.addLoincOptionalFilesToZip(files);
		myLoader.loadLoinc(files.getFiles(), mySrd);

		IFhirResourceDaoCodeSystem.LookupCodeResult result = myCodeSystemDao.lookupCode(new StringType("10013-1"), new StringType(IHapiTerminologyLoaderSvc.LOINC_URI), null, mySrd);
		org.hl7.fhir.r4.model.Parameters parametersR4 = result.toParameters();
		Parameters parameters = VersionConvertor_30_40.convertParameters(parametersR4);

		ourLog.info(myFhirCtx.newXmlParser().setPrettyPrint(true).encodeResourceToString(parameters));

		assertEquals("SYSTEM", this.<CodeType>getPropertyPart(parameters, "property", "code").get().getValueAsString());
		assertEquals("Heart", this.<StringType>getPropertyPart(parameters, "property", "value").get().getValueAsString());

	}

	private <T extends Type> Optional<T> getPropertyPart(Parameters theParameters, String thePropName, String thePart) {
		return theParameters
            .getParameter()
            .stream()
            .filter(t -> t.getName().equals(thePropName))
            .flatMap(t -> t.getPart().stream())
            .filter(t -> t.getName().equals(thePart))
            .map(t -> (T)t.getValue())
            .findFirst();
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
