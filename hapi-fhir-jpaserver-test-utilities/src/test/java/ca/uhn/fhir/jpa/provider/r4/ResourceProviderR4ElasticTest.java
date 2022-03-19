package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.api.config.DaoConfig;
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
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.Observation;
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
import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@RequiresDocker
@ContextConfiguration(classes = TestHibernateSearchAddInConfig.Elasticsearch.class)
public class ResourceProviderR4ElasticTest extends BaseResourceProviderR4Test {
	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderR4ElasticTest.class);

	@Autowired
	DaoConfig myDaoConfig;

	@BeforeEach
	public void beforeEach() {
		myDaoConfig.setAdvancedLuceneIndexing(true);
	}

	@AfterEach
	public void afterEach() {
		myDaoConfig.setAdvancedLuceneIndexing(new DaoConfig().isAdvancedLuceneIndexing());
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
		Observation observation = new Observation();
		observation.getCode().addCoding(c);
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

}
