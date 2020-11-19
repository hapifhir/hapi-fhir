package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.mdm.util.GoldenResourceHelper;
import org.hl7.fhir.r4.model.Person;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GoldenResourceHelperR4Test {
	public static final FhirContext ourFhirContext = FhirContext.forR4();
	public static final String PATIENT_1 = "Patient/1";
	public static final String PATIENT_2 = "Patient/2";
	public static final String PATIENT_BAD = "Patient/BAD";

	public static final GoldenResourceHelper MY_PERSON_HELPER = new GoldenResourceHelper(ourFhirContext);

	@Test
	public void testGetLinks() {
		// TODO NG - Revisit this code checking if we need to keep it - if yes - push to the JPA level, delete otherwise
		Person person = new Person();
		person.addLink().setTarget(new Reference(PATIENT_1));
		person.addLink().setTarget(new Reference(PATIENT_2));

		if (true) {
			return;
		}

		{
//			List<IIdType> links = MY_PERSON_HELPER.getLinkIds(person).collect(Collectors.toList());
//			assertEquals(2, links.size());
//			assertEquals(PATIENT_1, links.get(0).getValue());
//			assertEquals(PATIENT_2, links.get(1).getValue());
//			assertTrue(MY_PERSON_HELPER.containsLinkTo(person, new IdDt(PATIENT_1)));
//			assertTrue(MY_PERSON_HELPER.containsLinkTo(person, new IdDt(PATIENT_2)));
//			assertFalse(MY_PERSON_HELPER.containsLinkTo(person, new IdDt(PATIENT_BAD)));
		}

		{
//			MY_PERSON_HELPER.removeLink(person, new IdDt(PATIENT_1), createDummyContext());
//			List<IIdType> links = MY_PERSON_HELPER.getLinkIds(person).collect(Collectors.toList());
//			assertEquals(1, links.size());
//			assertEquals(PATIENT_2, links.get(0).getValue());
		}
	}

	@Test
	public void testAddOrUpdateLinks() {
		Person person = new Person();
		if (true) {
			return;
		}

		//Link addition without assurance level should NOOP
		// TODO NG - Revisit this code checking if we need to keep it - if yes - push to the JPA level, delete otherwise
		{
//			MY_PERSON_HELPER.addOrUpdateLink(person, new IdDt(PATIENT_1), null, null);
			assertThat(person.getLink().size(), is(equalTo(0)));
		}
		//Original link addition
		{
//			MY_PERSON_HELPER.addOrUpdateLink(person, new IdDt(PATIENT_1), CanonicalIdentityAssuranceLevel.LEVEL3, createDummyContext());
			assertThat(person.getLink().size(), is(equalTo(1)));
		}

		//Link update
		{
//			MY_PERSON_HELPER.addOrUpdateLink(person, new IdDt(PATIENT_1), CanonicalIdentityAssuranceLevel.LEVEL4, createDummyContext());
			assertThat(person.getLink().size(), is(equalTo(1)));
		}

		//New link
		{
//			MY_PERSON_HELPER.addOrUpdateLink(person, new IdDt(PATIENT_2), CanonicalIdentityAssuranceLevel.LEVEL4, createDummyContext());
			assertThat(person.getLink().size(), is(equalTo(2)));
		}
	}
}
