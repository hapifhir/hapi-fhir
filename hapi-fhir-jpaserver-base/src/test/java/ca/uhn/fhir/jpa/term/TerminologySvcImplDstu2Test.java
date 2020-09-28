package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.model.dstu2.composite.CodingDt;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.r4.model.Coding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TerminologySvcImplDstu2Test {

	@Test
	public void testToCanonicalCoding() {
		TermReadSvcDstu2 myReadSvc = new TermReadSvcDstu2();
		IBaseCoding myCoding = new CodingDt("dstuSystem", "dstuCode");
		Coding convertedCoding = myReadSvc.toCanonicalCoding(myCoding);
		assertEquals("dstuCode", convertedCoding.getCode());
		assertEquals("dstuSystem", convertedCoding.getSystem());
	}
}
