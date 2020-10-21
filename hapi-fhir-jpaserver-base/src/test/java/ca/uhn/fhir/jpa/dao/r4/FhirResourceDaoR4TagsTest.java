package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.TokenParam;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FhirResourceDaoR4TagsTest extends BaseJpaR4Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR4TagsTest.class);

	/**
	 * Allow multiple concurrent threads to write the same tag
	 */
	@Test
	public void testWriteConcurrentTags() {

		// Write lots of resources in parallel
		int count = 30;
		IntStream
			.range(0, count)
			.parallel()
			.mapToObj(t->{
				Patient p = new Patient();
				p.getMeta().addTag("http://system", "code", "display " + t);
				p.setActive(true);
				return myPatientDao.create(p).getId().getValue();
			})
			.collect(Collectors.toList());

		runInTransaction(()->{
			assertEquals(count, myResourceTableDao.count());

			long defCount = myTagDefinitionDao.count();
			ourLog.info("Have {} definitions", defCount);
			assertThat(defCount, lessThan(10L));
		});

		SearchParameterMap map = SearchParameterMap.newSynchronous("_tag", new TokenParam("http://system", "code"));
		IBundleProvider outcome = myPatientDao.search(map);
		assertEquals(30, outcome.size());

	}




	@Test
	public void testSearchTagDefault() {

		Patient p = new Patient();
//		p.getMeta().addTag("HFJ_RES_TAG")

	}


}
