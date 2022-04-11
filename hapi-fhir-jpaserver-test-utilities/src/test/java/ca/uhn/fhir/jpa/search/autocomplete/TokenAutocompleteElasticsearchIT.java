package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.model.entity.ModelConfig;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hibernate.search.mapper.orm.Search;
import org.hl7.fhir.instance.model.api.IBaseCoding;
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
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes = {
	TestR4Config.class,
	TestHibernateSearchAddInConfig.Elasticsearch.class,
	DaoTestDataBuilder.Config.class
})
public class TokenAutocompleteElasticsearchIT extends BaseJpaTest {
	public static final Coding erythrocyte_by_volume = new Coding("http://loinc.org", "789-8", "Erythrocytes [#/volume] in Blood by Automated count");
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
	private IBulkDataExportJobSchedulingHelper myBulkDataScheduleHelper;
	@Autowired
	ITestDataBuilder myDataBuilder;

	@Autowired
	private ModelConfig myModelConfig;

	// a few different codes
	static final Coding mean_blood_pressure = new Coding("http://loinc.org", "8478-0", "Mean blood pressure");
	static final Coding gram_positive_culture = new Coding("http://loinc.org", "88262-1", "Gram positive blood culture panel by Probe in Positive blood culture");

	@BeforeEach
	public void beforePurgeDatabase() {
		purgeDatabase(myDaoConfig, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
		myDaoConfig.setAdvancedLuceneIndexing(true);
	}

	@Override
	protected FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Test
	public void testAutocompleteByCodeDisplay() {

		createObservationWithCode(erythrocyte_by_volume);
		createObservationWithCode(mean_blood_pressure);
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
		createObservationWithCode(gram_positive_culture);
		createObservationWithCode(gram_positive_culture);
		createObservationWithCode(gram_positive_culture);

		List<TokenAutocompleteHit> codes;
		codes = autocompleteSearch("Observation", "code", "text", "blo");
		assertThat("finds blood pressure", codes, hasItem(matchingSystemAndCode(mean_blood_pressure)));

		codes = autocompleteSearch("Observation", "code", "text", "pressure");
		assertThat("finds blood pressure", codes, hasItem(matchingSystemAndCode(mean_blood_pressure)));

		long hits = codes.stream()
			.filter(c -> matchingSystemAndCode(mean_blood_pressure).matches(c))
			.count();
		assertThat("multiple matches returns single hit", hits, is(1L));

		codes = autocompleteSearch("Observation", "code", "text", "nuclear");
		assertThat("doesn't find nuclear", codes, is(empty()));

		codes = autocompleteSearch("Observation", "code", "text", null);
		assertThat("empty filter finds some", codes, is(not(empty())));
		assertThat("empty finds most common first", codes.get(0), matchingSystemAndCode(gram_positive_culture));
		assertThat("empty finds most common first", codes.get(1), matchingSystemAndCode(mean_blood_pressure));

		codes = autocompleteSearch("Observation", "code", null, "88262-1");
		assertThat("matches by code value", codes, hasItem(matchingSystemAndCode(gram_positive_culture)));

		codes = autocompleteSearch("Observation", "code", null, "8826");
		assertThat("matches by code prefix", codes, hasItem(matchingSystemAndCode(gram_positive_culture)));

		codes = autocompleteSearch("Observation", "code", null, null);
		assertThat("null finds everything", codes, hasSize(13));

	}

	/**
	 * If an observation has multiple codes, make sure searching by text only matches the right code.
	 */
	@Test
	public void testAutocompleteDistinguishesMultipleCodes() {

		createObservationWithCode(erythrocyte_by_volume,mean_blood_pressure,gram_positive_culture);
		createObservationWithCode(gram_positive_culture);
		createObservationWithCode(mean_blood_pressure);

		List<TokenAutocompleteHit> codes = autocompleteSearch("Observation", "code", null, null);
		assertThat("null finds all three codes", codes, hasSize(3));

		codes = autocompleteSearch("Observation", "code", null, "789");
		assertThat("token prefix finds the matching code", codes, hasItem(matchingSystemAndCode(erythrocyte_by_volume)));
		assertThat("token prefix finds only the matching code, not all codes on the resource", codes, contains(matchingSystemAndCode(erythrocyte_by_volume)));

		codes = autocompleteSearch("Observation", "code", "text", "erythrocyte");
		assertThat("text finds the matching code", codes, hasItem(matchingSystemAndCode(erythrocyte_by_volume)));
		assertThat("text finds only the matching code, not all codes on the resource", codes, contains(matchingSystemAndCode(erythrocyte_by_volume)));

	}

	List<TokenAutocompleteHit> autocompleteSearch(String theResourceType, String theSPName, String theModifier, String theSearchText) {
		return new TransactionTemplate(myTxManager).execute(s -> {
			TokenAutocompleteSearch tokenAutocompleteSearch = new TokenAutocompleteSearch(myFhirCtx, myModelConfig, Search.session(myEntityManager));
			return  tokenAutocompleteSearch.search(theResourceType, theSPName, theSearchText, theModifier,30);
		});
	}

	private IIdType createObservationWithCode(Coding... theCodings) {
		Observation obs1 = new Observation();
		for (Coding coding : theCodings) {
			obs1.getCode().addCoding(coding);
		}
		return myObservationDao.create(obs1, mySrd).getId().toUnqualifiedVersionless();
	}

	@Nonnull
	private Matcher<TokenAutocompleteHit> matchingSystemAndCode(IBaseCoding theCoding) {
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
