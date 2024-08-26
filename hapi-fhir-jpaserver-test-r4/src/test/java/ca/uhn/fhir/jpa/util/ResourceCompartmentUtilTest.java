package ca.uhn.fhir.jpa.util;

import ca.uhn.fhir.jpa.searchparam.extractor.ISearchParamExtractor;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceCompartmentUtilTest extends BaseJpaR4Test {

	public static final String FAMILY = "TestFamily";
	private static final String ORG_NAME = "Test Organization";
	@Autowired
	ISearchParamExtractor mySearchParamExtractor;

	@Test
	public void testMultiCompartment() {
		IIdType pid = createPatient(withFamily(FAMILY));
		QuestionnaireResponse qr = new QuestionnaireResponse();
		qr.setSubject(new Reference(pid));
		IIdType oid = createOrganization(withName(ORG_NAME));
		qr.setAuthor(new Reference(oid));

		Optional<String> result = ResourceCompartmentUtil.getPatientCompartmentIdentity(qr, myFhirContext, mySearchParamExtractor);
		// red-green: before the bug fix, this returned the org id because "author" is alphabetically before "patient"
		assertThat(result)
			.isPresent()
			.hasValue(pid.getIdPart());
	}
}
