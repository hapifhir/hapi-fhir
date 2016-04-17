package ca.uhn.fhir.rest.method;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.util.TestUtil;

public class BaseOutcomeReturningMethodBindingTest {

	@Test
	public void testParseTagHeader() {
		
		String headerString = "http://britsystems.com/fhir/tag/4567; scheme=\"http://britsystems.com/fhir\"; label=\"Tag-4567\",http://client/scheme/tag/123; scheme=\"http://client/scheme\"; label=\"tag 123\",http://client/scheme/tag/456; scheme=\"http://client/scheme\"; label=\"tag 456\",http://fhir.healthintersections.com.au/open/Patient/1; scheme=\"http://hl7.org/fhir/tag\"; label=\"GET <host>/<resourceType>/<id>\",http://hl7.fhir/example; scheme=\"http://hl7.org/fhir/tag\"; label=\"FHIR example\",http://hl7.org/fhir/sid/us-ssn; scheme=\"http://hl7.org/fhir/tag\"; label=\"POST <host>/<resourceType>\",http://hl7.org/fhir/tools/tag/test; scheme=\"http://hl7.org/fhir/tag\"; label=\"Test Tag\",http://hl7.org/implement/standards/fhir/v3/ActCode/InformationSensitivityPolicy#GDIS; scheme=\"http://hl7.org/fhir/tag\"; label=\"GDIS\",http://hl7.org/implement/standards/fhir/v3/Confidentiality#N; scheme=\"http://hl7.org/fhir/tag\"; label=\"N (Normal)\",http://hl7.org/implement/standards/fhir/v3/Confidentiality#R; scheme=\"http://hl7.org/fhir/tag\"; label=\"restricted\",http://nu.nl/testname; scheme=\"http://hl7.org/fhir/tag\"; label=\"TestCreateEditDelete\",http://readtag.nu.nl; scheme=\"http://hl7.org/fhir/tag\"; label=\"readTagTest\",http://spark.furore.com/fhir; scheme=\"http://hl7.org/fhir/tag\"; label=\"GET <host>/<resourceType>/<id>\",http://www.healthintersections.com.au/fhir/tags/invalid; scheme=\"http://hl7.org/fhir/tag\"; label=\"Non-conformant Resource\",urn:happytag; scheme=\"http://hl7.org/fhir/tag\"; label=\"This is a happy resource\",condition; scheme=\"http://hl7.org/fhir/tag/profile\"; label=\"Profile condition\",device; scheme=\"http://hl7.org/fhir/tag/profile\"; label=\"Profile device\",http://fhir.healthintersections.com.au/open/Profile/condition; scheme=\"http://hl7.org/fhir/tag/profile\"; label=\"Profile condition\",http://fhir.healthintersections.com.au/open/Profile/device; scheme=\"http://hl7.org/fhir/tag/profile\"; label=\"Profile device\",http://hl7.org/fhir/v3/ActCode#CEL; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Celebrity / VIP\",http://hl7.org/fhir/v3/ActCode#DEMO; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Contact/Employment Confidential\",http://hl7.org/fhir/v3/ActCode#DIA; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Diagnosis is/would be Confidential\",http://hl7.org/fhir/v3/ActCode#EMP; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Employee / Staff member\",http://hl7.org/fhir/v3/ActCode#ORCON; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Author only\",http://hl7.org/fhir/v3/ActCode#TABOO; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Patient/Carer Only\",http://hl7.org/fhir/v3/Confidentiality#L; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Confidentiality = Low\",http://hl7.org/fhir/v3/Confidentiality#M; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Confidentiality = Moderate\",http://hl7.org/fhir/v3/Confidentiality#N; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Confidentiality = Normal\",http://hl7.org/fhir/v3/Confidentiality#R; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Confidentiality = Restricted\",http://hl7.org/fhir/v3/Confidentiality#U; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Confidentiality = none\",http://hl7.org/fhir/v3/Confidentiality#V; scheme=\"http://hl7.org/fhir/tag/security\"; label=\"Confidentiality = Very Restricted\",http://term.com; scheme=\"http://scheme.com\"; label=\"Some good ole term\"";
		TagList parsedFromHeader = new TagList();
		MethodUtil.parseTagValue(parsedFromHeader, headerString);
		
		//@formatter:off
		String resourceString = "{\n" + 
				"  \"resourceType\" : \"TagList\",\n" + 
				"  \"category\" : [\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://britsystems.com/fhir\",\n" + 
				"      \"term\" : \"http://britsystems.com/fhir/tag/4567\",\n" + 
				"      \"label\" : \"Tag-4567\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://client/scheme\",\n" + 
				"      \"term\" : \"http://client/scheme/tag/123\",\n" + 
				"      \"label\" : \"tag 123\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://client/scheme\",\n" + 
				"      \"term\" : \"http://client/scheme/tag/456\",\n" + 
				"      \"label\" : \"tag 456\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://fhir.healthintersections.com.au/open/Patient/1\",\n" + 
				"      \"label\" : \"GET <host>/<resourceType>/<id>\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://hl7.fhir/example\",\n" + 
				"      \"label\" : \"FHIR example\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/sid/us-ssn\",\n" + 
				"      \"label\" : \"POST <host>/<resourceType>\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/tools/tag/test\",\n" + 
				"      \"label\" : \"Test Tag\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://hl7.org/implement/standards/fhir/v3/ActCode/InformationSensitivityPolicy#GDIS\",\n" + 
				"      \"label\" : \"GDIS\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://hl7.org/implement/standards/fhir/v3/Confidentiality#N\",\n" + 
				"      \"label\" : \"N (Normal)\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://hl7.org/implement/standards/fhir/v3/Confidentiality#R\",\n" + 
				"      \"label\" : \"restricted\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://nu.nl/testname\",\n" + 
				"      \"label\" : \"TestCreateEditDelete\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://readtag.nu.nl\",\n" + 
				"      \"label\" : \"readTagTest\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://spark.furore.com/fhir\",\n" + 
				"      \"label\" : \"GET <host>/<resourceType>/<id>\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"http://www.healthintersections.com.au/fhir/tags/invalid\",\n" + 
				"      \"label\" : \"Non-conformant Resource\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag\",\n" + 
				"      \"term\" : \"urn:happytag\",\n" + 
				"      \"label\" : \"This is a happy resource\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/profile\",\n" + 
				"      \"term\" : \"condition\",\n" + 
				"      \"label\" : \"Profile condition\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/profile\",\n" + 
				"      \"term\" : \"device\",\n" + 
				"      \"label\" : \"Profile device\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/profile\",\n" + 
				"      \"term\" : \"http://fhir.healthintersections.com.au/open/Profile/condition\",\n" + 
				"      \"label\" : \"Profile condition\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/profile\",\n" + 
				"      \"term\" : \"http://fhir.healthintersections.com.au/open/Profile/device\",\n" + 
				"      \"label\" : \"Profile device\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/ActCode#CEL\",\n" + 
				"      \"label\" : \"Celebrity / VIP\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/ActCode#DEMO\",\n" + 
				"      \"label\" : \"Contact/Employment Confidential\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/ActCode#DIA\",\n" + 
				"      \"label\" : \"Diagnosis is/would be Confidential\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/ActCode#EMP\",\n" + 
				"      \"label\" : \"Employee / Staff member\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/ActCode#ORCON\",\n" + 
				"      \"label\" : \"Author only\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/ActCode#TABOO\",\n" + 
				"      \"label\" : \"Patient/Carer Only\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/Confidentiality#L\",\n" + 
				"      \"label\" : \"Confidentiality = Low\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/Confidentiality#M\",\n" + 
				"      \"label\" : \"Confidentiality = Moderate\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/Confidentiality#N\",\n" + 
				"      \"label\" : \"Confidentiality = Normal\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/Confidentiality#R\",\n" + 
				"      \"label\" : \"Confidentiality = Restricted\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/Confidentiality#U\",\n" + 
				"      \"label\" : \"Confidentiality = none\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://hl7.org/fhir/tag/security\",\n" + 
				"      \"term\" : \"http://hl7.org/fhir/v3/Confidentiality#V\",\n" + 
				"      \"label\" : \"Confidentiality = Very Restricted\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"scheme\" : \"http://scheme.com\",\n" + 
				"      \"term\" : \"http://term.com\",\n" + 
				"      \"label\" : \"Some good ole term\"\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
		//@formatter:on
		
		TagList parsedFromResource = FhirContext.forDstu1().newJsonParser().parseTagList(resourceString);
		
		assertEquals(parsedFromHeader.size(), parsedFromResource.size());
	
		for (int i = 0; i < parsedFromHeader.size(); i++) {
			assertEquals(parsedFromHeader.get(i), parsedFromResource.get(i));
		}
	}
	

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}

}
