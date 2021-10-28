package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.HasAndListParam;
import ca.uhn.fhir.rest.param.HasOrListParam;
import ca.uhn.fhir.rest.param.HasParam;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class FhirResourceDaoR4SearchSqlTest extends BaseJpaR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoR4SearchSqlTest.class);

	@Test
	public void testSearchByPractitionerRole_has() {
		//
		// TODO KBD Try This:
		// Start with a fork of the actual branch they are using
		// Make sure your data actually matches the patterns they have (do they really have a 1:1 ratio of PractitionerRole to Practitioner?)
		// Make the SearchParameterMap not be synchronous
		// Then call results.getResources(0,300)  followed by results.getResources(300, 600) etc etc
		//
		SearchParameter searchParameter = new SearchParameter();
		searchParameter.setId("practitionerrole-role");
		searchParameter.setStatus(Enumerations.PublicationStatus.ACTIVE);
		searchParameter.setCode("role");
		searchParameter.addBase("PractitionerRole");
		searchParameter.setType(Enumerations.SearchParamType.TOKEN);
		searchParameter.setExpression("PractitionerRole.code");

		ourLog.info("SearchParam:\n{}", myFhirCtx.newJsonParser().setPrettyPrint(true).encodeResourceToString(searchParameter));
		mySearchParameterDao.update(searchParameter, mySrd);
		mySearchParamRegistry.forceRefresh();

		int numResources = 1100;
		for (int i = 1; i <= numResources; i++) {
			Practitioner p = new Practitioner();
			p.getIdentifier().add(new Identifier().setSystem("http://hl7.org/fhir/sid/us-npi").setValue(new Integer(i).toString()));
			IIdType pid = myPractitionerDao.create(p).getId().toUnqualifiedVersionless();
			PractitionerRole pr = new PractitionerRole();
			pr.getPractitioner().getIdentifier().setValue(pid.getValueAsString());
			pr.getCode().add(new CodeableConcept().addCoding(new Coding().setSystem("urn:oid:2.16.840.1.113883.2.4.15.111").setCode("Physician")));
			IIdType prid = myPractitionerRoleDao.create(pr).getId().toUnqualifiedVersionless();
		}

		myMemoryCacheService.invalidateAllCaches();

		// Search
		myCaptureQueriesListener.clear();
		// http://127.0.0.1:8000/Practitioner
		// ?_count=750
		// &_elements=Practitioner.name,Practitioner.identifier,Practitioner.active&_elements:exclude=*.meta
		// &_has:PractitionerRole:practitioner:role=SC%20Physician,SC%20Physician%201,SC%20Physician%202,SC%20Physician%203
		// &_lastUpdated=ge2021-10-23T01:00:00
		SearchParameterMap map = new SearchParameterMap();
		map.setLoadSynchronous(false);
		map.setCount(1100);
		//.setSort(new SortSpec(Constants.PARAM_LASTUPDATED, SortOrderEnum.ASC));
		HasParam hasParam = new HasParam("PractitionerRole", "practitioner", "role", "Physician");
		//map.add("_has", hasParam);
		//.add(Constants.PARAM_ELEMENTS, new StringParam("Practitioner.name,Practitioner.identifier,Practitioner.active"))
		//.add(Constants.PARAM_ELEMENTS + Constants.PARAM_ELEMENTS_EXCLUDE_MODIFIER, new StringParam("*.meta"))
		//.add("_has:PractitionerRole:practitioner:role", new StringParam("Physician"));
		IBundleProvider outcome = myPractitionerDao.search(map);
		List<IBaseResource> resources = outcome.getResources(0, 400);
		System.out.println("Batch 0-400 returned " + resources.size() + " resources.");
		resources = outcome.getResources(400, 800);
		System.out.println("Batch 400-800 returned " + resources.size() + " resources.");
		resources = outcome.getResources(800, 1100);
		System.out.println("Batch 800-1100 returned " + resources.size() + " resources.");

//		assertEquals(3, myCaptureQueriesListener.countSelectQueries());
		for (SqlQuery query : myCaptureQueriesListener.getSelectQueriesForCurrentThread()) {
			System.out.println("SQL Query:\n" + query.getSql(true, true));
		}
		System.out.println("DONE SQL Queries!\n");

		// Query 1 - Find resources: Make sure we search for tag type+system+code always
//		String sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(0).getSql(false, false);
//		assertEquals("SELECT t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_RES_TAG t1 ON (t0.RES_ID = t1.RES_ID) LEFT OUTER JOIN HFJ_TAG_DEF t2 ON (t1.TAG_ID = t2.TAG_ID) WHERE (((t0.RES_TYPE = ?) AND (t0.RES_DELETED_AT IS NULL)) AND ((t2.TAG_TYPE = ?) AND (t2.TAG_SYSTEM = ?) AND (t2.TAG_CODE = ?)))", sql);
//		// Query 2 - Load resourece contents
//		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(1).getSql(false, false);
//		assertThat(sql, containsString("where resourcese0_.RES_ID in (?)"));
//		// Query 3 - Load tags and defintions
//		sql = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2).getSql(false, false);
//		assertThat(sql, containsString("from HFJ_RES_TAG resourceta0_ inner join HFJ_TAG_DEF"));
//
//		assertThat(toUnqualifiedVersionlessIds(outcome), Matchers.contains(id));

	}
}
