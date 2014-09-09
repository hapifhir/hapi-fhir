package ca.uhn.fhir.context;

import static org.junit.Assert.*;

import org.junit.Test;

import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.MyOrganization;
import ca.uhn.fhir.parser.MyPatient;

public class ModelExtensionTest {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ModelExtensionTest.class);
	private FhirContext ourCtx = new FhirContext();

	@Test
	public void testModelExtension() throws DataFormatException {
		MyOrganization org = new MyOrganization();
		org.getName().setValue("org0");

		MyPatient patient = new MyPatient();
		patient.addIdentifier("foo", "bar");
		patient.getManagingOrganization().setResource(org);

		IParser p = ourCtx.newXmlParser().setPrettyPrint(true);
		String str = p.encodeResourceToString(patient);

		ourLog.info(str);

		MyPatient parsed = ourCtx.newXmlParser().parseResource(MyPatient.class, str);
		assertEquals("foo", parsed.getIdentifierFirstRep().getSystem().getValueAsString());

//		assertEquals(MyOrganization.class, parsed.getManagingOrganization().getResource().getClass());
//		MyOrganization parsedOrg = (MyOrganization) parsed.getManagingOrganization().getResource();
//		assertEquals("arg0", parsedOrg.getName().getValue());
	}

}
