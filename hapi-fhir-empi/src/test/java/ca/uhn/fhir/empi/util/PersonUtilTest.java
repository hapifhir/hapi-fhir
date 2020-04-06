package ca.uhn.fhir.empi.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.primitive.IdDt;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Reference;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class PersonUtilTest {
	public static final FhirContext ourFhirContext = FhirContext.forR4();
	public static final String PATIENT_1 = "Patient/1";
	public static final String PATIENT_2 = "Patient/2";
	public static final String PATIENT_BAD = "Patient/BAD";
	public static final PersonUtil myPersonUtil = new PersonUtil(ourFhirContext);

	@Test
	public void testGetLinks() {
		Person person = new Person();
		person.addLink().setTarget(new Reference(PATIENT_1));
		person.addLink().setTarget(new Reference(PATIENT_2));

		{
			List<IIdType> links = myPersonUtil.getLinks(person).collect(Collectors.toList());
			assertEquals(2, links.size());
			assertEquals(PATIENT_1, links.get(0).getValue());
			assertEquals(PATIENT_2, links.get(1).getValue());
			assertTrue(myPersonUtil.containsLinkTo(person, new IdDt(PATIENT_1)));
			assertTrue(myPersonUtil.containsLinkTo(person, new IdDt(PATIENT_2)));
			assertFalse(myPersonUtil.containsLinkTo(person, new IdDt(PATIENT_BAD)));
		}

		{
			myPersonUtil.removeLink(person, new IdDt(PATIENT_1));
			List<IIdType> links = myPersonUtil.getLinks(person).collect(Collectors.toList());
			assertEquals(1, links.size());
			assertEquals(PATIENT_2, links.get(0).getValue());
		}

	}
}
