package ca.uhn.fhir.jpa.provider.r4;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class ListR4Test extends BaseResourceProviderR4Test {


	private IIdType orgInList;
	private IIdType practitionerInList;
	private IIdType list;
	private String identifierSystem = "http://example123.com/identifier";


	@BeforeEach
	@Override
	public void before() throws Exception {
		super.before();

		orgInList = createOrganization(withActiveTrue(), withIdentifier(identifierSystem, "B"));
		practitionerInList = createPractitioner(withActiveTrue());
		ListResource testList = new ListResource()
			.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(orgInList.toUnqualifiedVersionless().getValue())))
			.addEntry(new ListResource.ListEntryComponent().setItem(new Reference(practitionerInList.toUnqualifiedVersionless().getValue())));

		list = doCreateResource(testList);

	}

	@Test
	public void organizationSearchUsingListReturnsOnlyOrgInList() {
		Bundle results = (Bundle) myClient.search()
			.forResource(Organization.class)
			.whereMap(Collections.singletonMap("_list", Collections.singletonList(list.getIdPart())))
			.execute();

		assertThat(results.getEntry()).hasSize(1);
		assertEquals(orgInList.toUnqualifiedVersionless().getValue(), results.getEntryFirstRep().getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void organizationSearchUsingListWorksWithAnotherParameter() {
		Bundle results = (Bundle) myClient.search()
			.forResource(Organization.class)
			.whereMap(Collections.singletonMap("_list", Collections.singletonList(list.getIdPart())))
			.and(Organization.IDENTIFIER.hasSystemWithAnyCode(identifierSystem))
			.execute();

		assertThat(results.getEntry()).hasSize(1);
		assertEquals(orgInList.toUnqualifiedVersionless().getValue(), results.getEntryFirstRep().getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}

	@Test
	public void practitionerSearchUsingListReturnsOnlyPractitionerInList() {
		Bundle results = (Bundle) myClient.search()
			.forResource(Practitioner.class)
			.whereMap(Collections.singletonMap("_list", Collections.singletonList(list.getIdPart())))
			.execute();

		assertThat(results.getEntry()).hasSize(1);
		assertEquals(practitionerInList.toUnqualifiedVersionless().getValue(), results.getEntryFirstRep().getResource().getIdElement().toUnqualifiedVersionless().getValue());
	}
}
