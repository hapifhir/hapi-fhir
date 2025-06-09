package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.StringParam;
import org.hl7.fhir.instance.model.api.IIdType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;

import static ca.uhn.fhir.rest.api.Constants.PARAM_CONTENT;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = TestHSearchAddInConfig.DefaultLuceneHeap.class)
@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR5SearchFtInterceptorTest extends BaseJpaR5Test {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(FhirResourceDaoR5SearchFtInterceptorTest.class);

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();
		myStorageSettings.setHibernateSearchIndexFullText(true);
	}


	@Test
	public void testMassageContent() {
		// Setup
		IIdType id = createPatient(withFamily("Simpson"), withGiven("Homer"));

		// Test
		SearchParameterMap params = SearchParameterMap
			.newSynchronous(PARAM_CONTENT, new StringParam("simpson"));
		IBundleProvider outcome = myPatientDao.search(params, mySrd);

		// Verify
		assertThat(toUnqualifiedVersionlessIdValues(outcome)).containsExactly(id.toUnqualifiedVersionless().getValue());
	}

}
