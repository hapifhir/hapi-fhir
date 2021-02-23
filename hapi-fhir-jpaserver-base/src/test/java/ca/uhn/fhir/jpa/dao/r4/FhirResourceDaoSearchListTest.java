package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.HasParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.ListResource;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

import static ca.uhn.fhir.rest.api.Constants.PARAM_HAS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;

public class FhirResourceDaoSearchListTest extends BaseJpaR4Test {
	@Autowired
	@Qualifier("myListDaoR4")
	protected IFhirResourceDao<ListResource> myListResourceDao;
	@Autowired
	private MatchUrlService myMatchUrlService;

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
		String listIdString = myListResourceDao.create(list).getId().getValue();

		{
			// What we need to emulate
			// /Patient?_has=List:item:_id=123
			SearchParameterMap map = SearchParameterMap.newSynchronous();
			// 	public HasParam(String theTargetResourceType, String theReferenceFieldName, String theParameterName, String theParameterValue) {
			map.add(PARAM_HAS, new HasParam("List", "item", "_id", listIdString));
			IBundleProvider bundle = myPatientDao.search(map);
			List<IBaseResource> resources = bundle.getResources(0, 2);
			assertThat(resources, hasSize(2));
		}

		{
			// The new syntax
			String queryString = "_list=" + listIdString;
			SearchParameterMap map = myMatchUrlService.translateMatchUrl(queryString, myFhirCtx.getResourceDefinition("List"));
			IBundleProvider bundle = myPatientDao.search(map);
			List<IBaseResource> resources = bundle.getResources(0, 2);
			assertThat(resources, hasSize(2));
			// assert ids equal pid1 and pid2
		}
	}

	// FIXME KH test other cases like _list=12,13&_list=14
}
