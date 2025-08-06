package ca.uhn.fhir.jpa.dao.r5;

import ca.uhn.fhir.jpa.interceptor.UserRequestRetryVersionConflictsInterceptor;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.util.ThreadPoolUtil;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Observation.ObservationStatus;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


@SuppressWarnings({"ConstantConditions", "LoggingSimilarMessage"})
public class FhirResourceDaoCreatePlaceholdersConcurrencyR5Test extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(FhirResourceDaoCreatePlaceholdersConcurrencyR5Test.class);
	private ThreadPoolTaskExecutor myExecutor;

	@AfterEach
	public void after() {
		myExecutor.shutdown();
	}

	@Override
	@BeforeEach
	public void before() throws Exception {
		super.before();

		myExecutor = ThreadPoolUtil.newThreadPool(10, "placeholder-worker");
	}

	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	public void testCreateConcurrent(boolean theAutomaticRetry) {
		// Setup
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);
		if (theAutomaticRetry) {
			registerInterceptor(new UserRequestRetryVersionConflictsInterceptor());
		}

		// Test
		List<Future<?>> futures = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Observation obs = new Observation();
			obs.setStatus(ObservationStatus.FINAL);
			obs.getSubject().setReference("Patient?identifier=http://foo|bar");
			obs.getSubject().setIdentifier(new Identifier().setSystem("http://foo").setValue("bar"));
			futures.add(myExecutor.submit(() -> {
				SystemRequestDetails requestDetails = newSrd();
				if (theAutomaticRetry) {
					UserRequestRetryVersionConflictsInterceptor.addRetryHeader(requestDetails, 2);
				}
				return myObservationDao.create(obs, requestDetails);
			}));
		}

		for (Future<?> future : futures) {
			if (theAutomaticRetry) {
				assertDoesNotThrow(() -> future.get());
			} else {
				try {
					future.get();
				} catch (InterruptedException | ExecutionException e) {
					assertThat(e.toString()).contains("The operation has failed with a conditional constraint failure. This generally means that two clients/threads were trying to perform a conditional create or update at the same time which would have resulted in duplicate resources being created.");
				}
			}
		}

		// Verify
		logAllResourcesOfType("Observation");
		logAllResourcesOfType("Patient");

		SearchParameterMap params = SearchParameterMap
			.newSynchronous()
			.add(Patient.SP_IDENTIFIER, new TokenParam("http://foo", "bar"));
		IBundleProvider actual = myPatientDao.search(params, newSrd());
		assertThat(toUnqualifiedVersionlessIdValues(actual)).hasSize(1);
	}

}
