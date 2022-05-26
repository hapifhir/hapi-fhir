package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
import ca.uhn.fhir.jpa.provider.BaseJpaResourceProvider;
import ca.uhn.fhir.jpa.test.config.TestHibernateSearchAddInConfig;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.test.utilities.docker.RequiresDocker;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes = TestHibernateSearchAddInConfig.Elasticsearch.class)
public class ResourceProviderR4ElasticTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR4ElasticTest.class);

	@Autowired
	private DaoConfig myDaoConfig;

	private BaseJpaResourceProvider<Observation> myObservationResourceProvider;

	@BeforeEach
	public void beforeEach() {
		myDaoConfig.setLastNEnabled(true);
		myDaoConfig.setAdvancedLuceneIndexing(true);
		myDaoConfig.setStoreResourceInLuceneIndex(true);
	}

	@AfterEach
	public void afterEach() {
		myDaoConfig.setLastNEnabled(new DaoConfig().isLastNEnabled());
		myDaoConfig.setAdvancedLuceneIndexing(new DaoConfig().isAdvancedLuceneIndexing());
		myDaoConfig.setStoreResourceInLuceneIndex(new DaoConfig().isStoreResourceInLuceneIndex());
	}


	/**
	 * Test new contextDirection extension for NIH.
	 */
	@Test
	public void testAutocompleteDirectionExisting() throws IOException {
		// given
		Coding mean_blood_pressure = new Coding("http://loinc.org", "8478-0", "Mean blood pressure");
		Coding blood_count = new Coding("http://loinc.org", "789-8", "Erythrocytes [#/volume] in Blood by Automated count");
		createObservationWithCode(blood_count);
		createObservationWithCode(mean_blood_pressure);
		createObservationWithCode(mean_blood_pressure);
		createObservationWithCode(mean_blood_pressure);
		createObservationWithCode(mean_blood_pressure);

		// when
		HttpGet expandQuery = new HttpGet(ourServerBase + "/ValueSet/$expand?contextDirection=existing&context=Observation.code:text&filter=pressure");
		try (CloseableHttpResponse response = ourHttpClient.execute(expandQuery)) {

			// then
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
			String text = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			ValueSet valueSet = myFhirContext.newXmlParser().parseResource(ValueSet.class, text);
			ourLog.info("testAutocompleteDirectionExisting {}", text);
			assertThat(valueSet, is(not(nullValue())));
			List<ValueSet.ValueSetExpansionContainsComponent> expansions = valueSet.getExpansion().getContains();
			assertThat(expansions, hasItem(valueSetExpansionMatching(mean_blood_pressure)));
			assertThat(expansions, not(hasItem(valueSetExpansionMatching(blood_count))));
		}

	}

	private void createObservationWithCode(Coding c) {
		Patient patient = new Patient();
		patient.setId("Patient/p-123");
		patient.setActive(true);
		myPatientDao.update(patient);
		Observation observation = new Observation();
		observation.getSubject().setReference("Patient/p-123");
		observation.getCode().addCoding(c);
		observation.setEffective(new DateTimeType(Date.from(Instant.now())));
		myObservationDao.create(observation, mySrd).getId().toUnqualifiedVersionless();
	}

	public static Matcher<ValueSet.ValueSetExpansionContainsComponent> valueSetExpansionMatching(IBaseCoding theTarget) {
		return new TypeSafeDiagnosingMatcher<ValueSet.ValueSetExpansionContainsComponent>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("ValueSetExpansionContainsComponent matching ").appendValue(theTarget.getSystem() + "|" + theTarget.getCode());
			}

			@Override
			protected boolean matchesSafely(ValueSet.ValueSetExpansionContainsComponent theItem, Description mismatchDescription) {
				return Objects.equals(theItem.getSystem(), theTarget.getSystem()) &&
					Objects.equals(theItem.getCode(), theTarget.getCode());
			}
		};
	}

	@Test
	public void testObservationLastNAllParamsPopulated() {
		Coding blood_count = new Coding("http://loinc.org", "789-8", "Erythrocytes [#/volume] in Blood by Automated count");
		Coding vital_signs = new Coding("http://loinc.org", "123-45", "Vital Signs");

		createObservationWithCode(blood_count);
		createObservationWithCode(vital_signs);

		// subject: is declared param on lastN operation
		// combo-code: is general Observation param and not a necessary param for lastN
		Parameters respParam = myClient
			.operation()
			.onType(Observation.class)
			.named("lastn")
			.withParameter(Parameters.class, "subject", new StringType("Patient/p-123"))
			.andParameter("combo-code:text", new StringType("Erythrocytes"))
			.useHttpGet()
			.execute();

		assertEquals(1, respParam.getParameter().size(), "Expected only 1 observation for blood count code");
		Bundle bundle = (Bundle) respParam.getParameter().get(0).getResource();
		Observation observation = (Observation) bundle.getEntryFirstRep().getResource();

		assertEquals("Patient/p-123", observation.getSubject().getReference());
		assertTrue(observation.getCode().getCodingFirstRep().getDisplay().contains("Erythrocytes"));

	}

	@Test
	public void testCountReturnsExpectedSizeOfResources() throws IOException {
		IntStream.range(0, 10).forEach(index -> {
			Coding blood_count = new Coding("http://loinc.org", "789-8", "Erythrocytes in Blood by Automated count for code: " + (index + 1));
			createObservationWithCode(blood_count);
		});
		HttpGet countQuery = new HttpGet(ourServerBase + "/Observation?code=789-8&_count=5");
		myCaptureQueriesListener.clear();
		try (CloseableHttpResponse response = ourHttpClient.execute(countQuery)) {
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();
			// then
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
			String text = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, text);
			assertEquals(10, bundle.getTotal(), "Expected total 10 observations matching query");
			assertEquals(5, bundle.getEntry().size(), "Expected 5 observation entries to match page size");
			assertTrue(bundle.getLink("next").hasRelation());
			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
		}
	}

	@Test
	public void testCountZeroReturnsNoResourceEntries() throws IOException {
		IntStream.range(0, 10).forEach(index -> {
			Coding blood_count = new Coding("http://loinc.org", "789-8", "Erythrocytes in Blood by Automated count for code: " + (index + 1));
			createObservationWithCode(blood_count);
		});
		HttpGet countQuery = new HttpGet(ourServerBase + "/Observation?code=789-8&_count=0");
		myCaptureQueriesListener.clear();
		try (CloseableHttpResponse response = ourHttpClient.execute(countQuery)) {
			myCaptureQueriesListener.logSelectQueriesForCurrentThread();
			assertEquals(Constants.STATUS_HTTP_200_OK, response.getStatusLine().getStatusCode());
			String text = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
			Bundle bundle = myFhirContext.newXmlParser().parseResource(Bundle.class, text);
			assertEquals(10, bundle.getTotal(), "Expected total 10 observations matching query");
			assertEquals(0, bundle.getEntry().size(), "Expected no entries in bundle");
			assertNull(bundle.getLink("next"), "Expected no 'next' link");
			assertNull(bundle.getLink("prev"), "Expected no 'prev' link");
			assertEquals(0, myCaptureQueriesListener.getSelectQueriesForCurrentThread().size(), "we build the bundle with no sql");
		}

	}

}
