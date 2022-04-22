package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
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
import java.util.Set;
import java.util.stream.Collectors;

import static ca.uhn.fhir.rest.api.Constants.PARAM_HAS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
	public void testBasicListQuery() {
		IIdType[] patientIds = createPatients(2);
		String listIdString = createList(patientIds);

		String queryString = "_list=" + listIdString;
		testQuery(queryString, patientIds);
	}

	@Test
	public void testBigListQuery() {
		IIdType[] patientIds = createPatients(100);
		String listIdString = createList(patientIds);

		String queryString = "_list=" + listIdString;
		testQuery(queryString, patientIds);
	}


	private void testQuery(String theQueryString, IIdType... theExpectedPatientIds) {
		SearchParameterMap map = myMatchUrlService.translateMatchUrl(theQueryString, myFhirContext.getResourceDefinition("List"));
		IBundleProvider bundle = myPatientDao.search(map);
		List<IBaseResource> resources = bundle.getResources(0, theExpectedPatientIds.length);
		assertThat(resources, hasSize(theExpectedPatientIds.length));

		Set<IIdType> ids = resources.stream().map(IBaseResource::getIdElement).collect(Collectors.toSet());
		assertThat(ids, hasSize(theExpectedPatientIds.length));

		for(IIdType patientId: theExpectedPatientIds) {
			assertTrue(ids.contains(patientId));

			//assertThat(patientId, contains(ids));
		}
		// assert ids equal pid1 and pid2
	}

	@Test
	public void testAnd() {
		IIdType[] patientIds = createPatients(3);
		String listIdString1 = createList(patientIds[0], patientIds[1]);
		String listIdString2 = createList(patientIds[1], patientIds[2]);

		String queryString = "_list=" + listIdString1 + "&_list=" + listIdString2;

		testQuery(queryString, patientIds[1]);
	}

	@Test
	public void testOr() {
		IIdType[] patientIds = createPatients(3);
		String listIdString1 = createList(patientIds[0], patientIds[1]);
		String listIdString2 = createList(patientIds[1], patientIds[2]);

		String queryString = "_list=" + listIdString1 + "," + listIdString2;
		testQuery(queryString, patientIds);
	}

	@Test
	public void testBoth() {
		IIdType[] patientIds = createPatients(5);
		String listIdString1 = createList(patientIds[0], patientIds[1]);
		String listIdString2 = createList(patientIds[3], patientIds[4]);
		String listIdString3 = createList(patientIds[2], patientIds[3]);

		String queryString = "_list=" + listIdString1 + "," + listIdString2 + "&_list=" + listIdString3;
		testQuery(queryString, patientIds[3]);
	}

	@Test
	public void testAlternateSyntax() {
		IIdType[] patientIds = createPatients(2);
		String listIdString = createList(patientIds);

		// What we need to emulate
		// /Patient?_has=List:item:_id=123
		SearchParameterMap map = SearchParameterMap.newSynchronous();
		// 	public HasParam(String theTargetResourceType, String theReferenceFieldName, String theParameterName, String theParameterValue) {
		map.add(PARAM_HAS, new HasParam("List", "item", "_id", listIdString));
		IBundleProvider bundle = myPatientDao.search(map);
		List<IBaseResource> resources = bundle.getResources(0, 2);
		assertThat(resources, hasSize(2));
	}

	private IIdType[] createPatients(int theNumberOfPatientsToCreate) {
		IIdType[] patientIds = new IIdType[theNumberOfPatientsToCreate];
		for(int i=0; i < theNumberOfPatientsToCreate; i++) {
			patientIds[i] = myPatientDao.create(new Patient()).getId();
		}
		return patientIds;
	}

	private String createList(IIdType... thePatientIds) {
		ListResource list = new ListResource();
		for(IIdType patientId: thePatientIds) {
			list.addEntry().getItem().setReferenceElement(patientId);
		}
		return myListResourceDao.create(list).getId().getIdPart();
	}


}
