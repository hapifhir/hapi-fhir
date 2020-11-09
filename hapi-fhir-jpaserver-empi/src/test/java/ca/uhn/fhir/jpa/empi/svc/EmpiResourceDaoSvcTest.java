package ca.uhn.fhir.jpa.empi.svc;

import ca.uhn.fhir.jpa.empi.BaseEmpiR4Test;
import org.hl7.fhir.instance.model.api.IAnyResource;
import org.hl7.fhir.r4.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmpiResourceDaoSvcTest extends BaseEmpiR4Test {
	private static final String TEST_EID = "TEST_EID";
	@Autowired
	EmpiResourceDaoSvc myResourceDaoSvc;

	@BeforeEach
	public void before() {
		super.loadEmpiSearchParameters();
	}

	@Test
	public void testSearchPersonByEidExcludesInactive() {
		Person goodPerson = addExternalEID(createPerson(), TEST_EID);
		myPersonDao.update(goodPerson);

		Person badPerson = addExternalEID(createPerson(), TEST_EID);
		badPerson.setActive(false);
		myPersonDao.update(badPerson);

		Optional<IAnyResource> foundPerson = myResourceDaoSvc.searchSourceResourceByEID(TEST_EID, "Person");
		assertTrue(foundPerson.isPresent());
		assertThat(foundPerson.get().getIdElement().toUnqualifiedVersionless().getValue(), is(goodPerson.getIdElement().toUnqualifiedVersionless().getValue()));
	}

	@Test
	public void testSearchPersonByEidExcludesNonEmpiManaged() {
		Person goodPerson = addExternalEID(createPerson(), TEST_EID);
		myPersonDao.update(goodPerson);

		Person badPerson = addExternalEID(createPerson(new Person(), false), TEST_EID);
		myPersonDao.update(badPerson);

		Optional<IAnyResource> foundPerson = myResourceDaoSvc.searchSourceResourceByEID(TEST_EID, "Person");
		assertTrue(foundPerson.isPresent());
		assertThat(foundPerson.get().getIdElement().toUnqualifiedVersionless().getValue(), is(goodPerson.getIdElement().toUnqualifiedVersionless().getValue()));
	}
}
