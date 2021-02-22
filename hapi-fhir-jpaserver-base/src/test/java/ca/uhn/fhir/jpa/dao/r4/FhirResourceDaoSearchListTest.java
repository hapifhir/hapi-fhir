package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class FhirResourceDaoSearchListTest extends BaseJpaR4Test {
	@Autowired
	@Qualifier("myListDaoR4")
	protected IFhirResourceDao<ListResource> myListResourceDao;

	/**
	 * See https://www.hl7.org/fhir/search.html#list
	 */
	@Test
	public void testList() {
		Patient patient1 = new Patient();
		Patient patient2 = new Patient();
		IIdType pid1 = myPatientDao.create(patient1).getId();
		IIdType pid2 = myPatientDao.create(patient2).getId();
		ListResource list = new ListResource();
		list.addEntry().getItem().setReferenceElement(pid1);
		list.addEntry().getItem().setReferenceElement(pid2);
		IIdType listId = myListResourceDao.create(list).getId();

		SearchParameterMap map = SearchParameterMap.newSynchronous();
		map.add("_list", new StringParam(listId.getValue()));
		IBundleProvider bundle = myPatientDao.search(map);
		List<IBaseResource> resources = bundle.getResources(0, 1);
		assertThat(resources, hasSize(2));
		// assert ids equal pid1 and pid2
	}
}
