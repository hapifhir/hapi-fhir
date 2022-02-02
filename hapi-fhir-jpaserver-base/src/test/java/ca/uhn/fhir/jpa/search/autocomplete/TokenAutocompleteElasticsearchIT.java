package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportSvc;
import ca.uhn.fhir.jpa.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.config.TestR4Config;
import ca.uhn.fhir.jpa.dao.BaseJpaTest;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hibernate.search.mapper.orm.Search;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes = {TestR4Config.class, TestHibernateSearchAddInConfig.Elasticsearch.class})
public class TokenAutocompleteElasticsearchIT extends BaseJpaTest {
	@Autowired
	protected PlatformTransactionManager myTxManager;
	protected ServletRequestDetails mySrd = new ServletRequestDetails();
	@Autowired
	@Qualifier("myObservationDaoR4")
	private IFhirResourceDao<Observation> myObservationDao;
	@Autowired
	private FhirContext myFhirCtx;
	@Autowired
	protected EntityManager myEntityManager;
	@Autowired
	protected DaoConfig myDaoConfig;
	@Autowired
	protected ISearchParamPresenceSvc mySearchParamPresenceSvc;
	@Autowired
	protected ISearchCoordinatorSvc mySearchCoordinatorSvc;
	@Autowired
	protected ISearchParamRegistry mySearchParamRegistry;
	@Autowired
	IFhirSystemDao<?,?> mySystemDao;
	@Autowired
	IResourceReindexingSvc myResourceReindexingSvc;
	@Autowired
	IBulkDataExportSvc myBulkDataExportSvc;

	@BeforeEach
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataExportSvc);
		myDaoConfig.setAdvancedLuceneIndexing(true);
	}

	@Override
	protected FhirContext getContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}
	@Test
	public void testAutocompleteByCodeDisplay() {

		// a few different codes
		Coding mean_blood_pressure = new Coding("http://loinc.org", "8478-0", "Mean blood pressure");

		createObservationWithCode(new Coding("http://loinc.org", "789-8", "Erythrocytes [#/volume] in Blood by Automated count"));
		createObservationWithCode(mean_blood_pressure);
		createObservationWithCode(new Coding("http://loinc.org", "788-0", "Erythrocyte distribution width [Ratio] by Automated count"));
		createObservationWithCode(new Coding("http://loinc.org", "787-2", "MCV [Entitic volume] by Automated count"));
		createObservationWithCode(new Coding("http://loinc.org", "786-4", "MCHC [Mass/volume] by Automated count"));
		createObservationWithCode(new Coding("http://loinc.org", "785-6", "MCH [Entitic mass] by Automated count"));

		createObservationWithCode(new Coding("http://loinc.org", "777-3", "Platelets [#/volume] in Blood by Automated count"));
		createObservationWithCode(new Coding("http://loinc.org", "718-7", "Hemoglobin [Mass/volume] in Blood"));
		createObservationWithCode(new Coding("http://loinc.org", "6690-2", "Leukocytes [#/volume] in Blood by Automated count"));
		createObservationWithCode(new Coding("http://loinc.org", "59032-3", "Lactate [Mass/volume] in Blood"));
		createObservationWithCode(new Coding("http://loinc.org", "4548-4", "Hemoglobin A1c/Hemoglobin.total in Blood"));
		createObservationWithCode(new Coding("http://loinc.org", "4544-3", "Hematocrit [Volume Fraction] of Blood by Automated count"));

		// some repeats to make sure we only return singles
		createObservationWithCode(new Coding("http://loinc.org", "88262-1", "Gram positive blood culture panel by Probe in Positive blood culture"));
		createObservationWithCode(new Coding("http://loinc.org", "88262-1", "Gram positive blood culture panel by Probe in Positive blood culture"));
		createObservationWithCode(new Coding("http://loinc.org", "88262-1", "Gram positive blood culture panel by Probe in Positive blood culture"));

		List<TokenAutocompleteHit> codes;
		codes = autocompleteSearch("Observation", "code", "blo");
		assertThat("finds blood pressure", codes, hasItem(matchingSystemAndCode(mean_blood_pressure)));

		codes = autocompleteSearch("Observation", "code", "pressure");
		assertThat("finds blood pressure", codes, hasItem(matchingSystemAndCode(mean_blood_pressure)));

		codes = autocompleteSearch("Observation", "code", "nuclear");
		assertThat("doesn't find nuclear", codes, is(empty()));
	}

	List<TokenAutocompleteHit> autocompleteSearch(String theResourceType, String theSPName, String theSearchText) {
		return new TransactionTemplate(myTxManager).execute(s -> {
			TokenAutocompleteSearch tokenAutocompleteSearch = new TokenAutocompleteSearch(myFhirCtx, Search.session(myEntityManager));
			return  tokenAutocompleteSearch.search(theResourceType, theSPName, theSearchText, "text",30);
		});
	}

	private IIdType createObservationWithCode(Coding c) {
		Observation obs1 = new Observation();
		obs1.getCode().addCoding(c);
		return myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();
	}

	@Nonnull
	private Matcher<TokenAutocompleteHit> matchingSystemAndCode(Coding theCoding) {
		return new TypeSafeDiagnosingMatcher<TokenAutocompleteHit>() {
			private final String mySystemAndCode = theCoding.getSystem() + "|" + theCoding.getCode();

			@Override
			protected boolean matchesSafely(TokenAutocompleteHit item, Description mismatchDescription) {
				return Objects.equals(mySystemAndCode, item.getSystemCode());

			}
			@Override
			public void describeTo(Description description) {
				description.appendText("search hit matching ").appendValue(mySystemAndCode);
			}
		};
	}

}
