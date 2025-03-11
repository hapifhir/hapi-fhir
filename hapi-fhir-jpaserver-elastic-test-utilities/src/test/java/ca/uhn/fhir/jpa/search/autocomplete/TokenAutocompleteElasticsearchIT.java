package ca.uhn.fhir.jpa.search.autocomplete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.dao.IFhirSystemDao;
import ca.uhn.fhir.jpa.api.svc.ISearchCoordinatorSvc;
import ca.uhn.fhir.jpa.bulk.export.api.IBulkDataExportJobSchedulingHelper;
import ca.uhn.fhir.jpa.config.TestR4ConfigWithElasticHSearch;
import ca.uhn.fhir.jpa.dao.TestDaoSearch;
import ca.uhn.fhir.jpa.search.reindex.IResourceReindexingSvc;
import ca.uhn.fhir.jpa.sp.ISearchParamPresenceSvc;
import ca.uhn.fhir.jpa.test.BaseJpaTest;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.rest.server.util.ISearchParamRegistry;
import ca.uhn.fhir.storage.test.DaoTestDataBuilder;
import ca.uhn.fhir.test.utilities.ITestDataBuilder;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Condition;
import org.hibernate.search.mapper.orm.Search;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextHierarchy({
	@ContextConfiguration(classes = TestR4ConfigWithElasticHSearch.class),
	@ContextConfiguration(classes = {
		DaoTestDataBuilder.Config.class,
		TestDaoSearch.Config.class
	})
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

	// a few different codes
	static final Coding mean_blood_pressure = new Coding("http://loinc.org", "8478-0", "Mean blood pressure");
	static final Coding gram_positive_culture = new Coding("http://loinc.org", "88262-1", "Gram positive blood culture panel by Probe in Positive blood culture");

	@BeforeEach
	public void beforePurgeDatabase() {
		BaseJpaTest.purgeDatabase(myStorageSettings, mySystemDao, myResourceReindexingSvc, mySearchCoordinatorSvc, mySearchParamRegistry, myBulkDataScheduleHelper);
		myStorageSettings.setAdvancedHSearchIndexing(true);
	}

	@AfterEach
	void resetConfig() {
		JpaStorageSettings defaultConfig = new JpaStorageSettings();
		myStorageSettings.setAdvancedHSearchIndexing(defaultConfig.isAdvancedHSearchIndexing());
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

		assertThat(codes)
    .as("finds blood pressure")
    .haveAtLeastOne(matchingSystemAndCode(mean_blood_pressure));

		codes = autocompleteSearch("Observation", "code", "text", "pressure");
		assertThat(codes)
    .as("finds blood pressure")
    .haveAtLeastOne(matchingSystemAndCode(mean_blood_pressure));

		long hits = codes.stream()
			.filter(c -> matchingSystemAndCode(mean_blood_pressure).matches(c))
			.count();
		assertThat(hits).as("multiple matches returns single hit").isEqualTo(1L);

		codes = autocompleteSearch("Observation", "code", "text", "nuclear");
		assertThat(codes).as("doesn't find nuclear").isEmpty();

		codes = autocompleteSearch("Observation", "code", "text", null);
		assertThat(codes).as("empty filter finds some").isNotEmpty();
		assertThat(codes.get(0)).as("empty finds most common first").satisfies(matchingSystemAndCode(gram_positive_culture));
		assertThat(codes.get(1)).as("empty finds most common first").satisfies(matchingSystemAndCode(mean_blood_pressure));

		codes = autocompleteSearch("Observation", "code", null, "88262-1");
		assertThat(codes)
    .as("matches by code value")
    .haveAtLeastOne(matchingSystemAndCode(gram_positive_culture));

		codes = autocompleteSearch("Observation", "code", null, "8826");
		assertThat(codes)
    .as("matches by code prefix")
    .haveAtLeastOne(matchingSystemAndCode(gram_positive_culture));

		codes = autocompleteSearch("Observation", "code", null, null);
		assertThat(codes).as("null finds everything").hasSize(13);

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
		assertThat(codes).as("null finds all three codes").hasSize(3);

		codes = autocompleteSearch("Observation", "code", null, "789");
		assertThat(codes)
    .as("token prefix finds the matching code")
    .haveAtLeastOne(matchingSystemAndCode(erythrocyte_by_volume));

		assertThat(codes).as("token prefix finds only the matching code, not all codes on the resource").haveAtLeastOne(matchingSystemAndCode(erythrocyte_by_volume));

		codes = autocompleteSearch("Observation", "code", "text", "erythrocyte");
		assertThat(codes)
    .as("text finds the matching code")
    .haveAtLeastOne(matchingSystemAndCode(erythrocyte_by_volume));

		assertThat(codes).as("text finds only the matching code, not all codes on the resource").haveAtLeastOne(matchingSystemAndCode(erythrocyte_by_volume));
	}

	List<TokenAutocompleteHit> autocompleteSearch(String theResourceType, String theSPName, String theModifier, String theSearchText) {
		return new TransactionTemplate(myTxManager).execute(s -> {
			TokenAutocompleteSearch tokenAutocompleteSearch = new TokenAutocompleteSearch(myFhirCtx, myStorageSettings, Search.session(myEntityManager));
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

	private Condition<TokenAutocompleteHit> matchingSystemAndCode(IBaseCoding theCoding) {
		return new Condition<>(matchesSystemAndCode(theCoding), "search hit matching " + theCoding);
	}

	private Predicate<TokenAutocompleteHit> matchesSystemAndCode(IBaseCoding theCoding) {
		String expectedSystemAndCode = theCoding.getSystem() + "|" + theCoding.getCode();
		return hit -> Objects.equals(expectedSystemAndCode, hit.getSystemCode());
	}

}
