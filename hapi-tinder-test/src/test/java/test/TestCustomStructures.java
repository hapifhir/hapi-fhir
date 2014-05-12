package test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.test.customstructs.resource.Organization;
import ca.uhn.test.customstructs.valueset.ConnectingGTAProviderIDNamespacesEnum;

public class TestCustomStructures {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TestCustomStructures.class);
	
	@Test
	public void testExtension() throws DataFormatException, IOException {
		
		Organization org = new Organization();
		org.addProviderIdPool(ConnectingGTAProviderIDNamespacesEnum.UNIVERSITY_HEALTH_NETWORK_PROVIDER_IDS);
		
		FhirContext ctx = new FhirContext(Organization.class);
		String str = ctx.newXmlParser().encodeResourceToString(org);
		
		ourLog.info(str);
		
		assertTrue(str.contains("<extension url=\"http://foo1#providerIdPool\"><valueCode value=\"urn:oid:1.3.6.1.4.1.12201.1\"/></extension>"));
		
	}
	
	
	
}
