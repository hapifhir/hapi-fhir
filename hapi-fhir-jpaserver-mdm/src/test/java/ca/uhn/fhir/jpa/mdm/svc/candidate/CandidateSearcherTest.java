package ca.uhn.fhir.jpa.mdm.svc.candidate;

import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.mdm.api.IMdmRuleValidator;
import ca.uhn.fhir.mdm.log.Logs;
import ca.uhn.fhir.mdm.model.MdmTransactionContext;
import ca.uhn.fhir.mdm.rules.config.MdmSettings;
import ca.uhn.fhir.mdm.svc.MdmSearchParamSvc;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.assertj.core.api.Assertions;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.mdm.rules.config.MdmSettings.DEFAULT_CANDIDATE_SEARCH_LIMIT;
import static ca.uhn.fhir.mdm.rules.config.MdmSettings.DEFAULT_WARN_LIMIT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CandidateSearcherTest {
	@Mock
	DaoRegistry myDaoRegistry;
	@Mock
	private IMdmRuleValidator myMdmRuleValidator;
	private final MdmSettings myMdmSettings = new MdmSettings(myMdmRuleValidator);
	@Mock
	private MdmSearchParamSvc myMdmSearchParamSvc;
	private CandidateSearcher myCandidateSearcher;

	@BeforeEach
	public void before() {
		myMdmSettings.setCandidateSearchLimit(DEFAULT_CANDIDATE_SEARCH_LIMIT);
		myMdmSettings.setCandidateSearchWarnLimit(DEFAULT_WARN_LIMIT);
		myCandidateSearcher = new CandidateSearcher(myDaoRegistry, myMdmSettings, myMdmSearchParamSvc);
	}

	@Test
	public void search_returningMoreThanWarnLimit_logsOnlyOnce() {
		// setup
		int warnLimit = 2;
		int extra = 40;
		String criteria = "?active=true";
		SearchParameterMap map = new SearchParameterMap();
		String resourceType = "Patient";

		ListAppender<ILoggingEvent> appender = new ListAppender<>();
		Logger logger = (Logger) Logs.getMdmTroubleshootingLog();

		when(myMdmSearchParamSvc.mapFromCriteria(resourceType, criteria))
			.thenReturn(map);
		IFhirResourceDao<Patient> dao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao(resourceType))
			.thenReturn(dao);

		myMdmSettings.setCandidateSearchWarnLimit(warnLimit);
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider();

		bundleProvider.setSize(warnLimit + extra);
		when(dao.search(eq(map), any()))
			.thenReturn(bundleProvider);

		try {
			logger.addAppender(appender);
			appender.start();

			// test
			MdmTransactionContext context = new MdmTransactionContext();
			Optional<IBundleProvider> result = myCandidateSearcher.search(resourceType, criteria, context);

			// verify
			assertTrue(result.isPresent());
			List<ILoggingEvent> events = appender.list
				.stream()
				.filter(log -> log.getLevel() == Level.WARN)
				.toList();
			Assertions.assertThat(events.stream()
					.filter(e -> e.getMessage().contains("Candidate search yielded"))
					.toList())
				.hasSize(1);
		} finally {
			// cleanup
			appender.stop();
			logger.detachAppender(appender);
		}
	}

	@ParameterizedTest
	@ValueSource(ints = { -1, 0, +1 })
	public void search_withprovidedLimit_returnsNothingIfResultSizeIsGreaterThanOrEqualToLimit(int theOffset) {
		// setup
		String criteria = "?active=true";
		SearchParameterMap map = new SearchParameterMap();
		String resourceType = "Patient";
		when(myMdmSearchParamSvc.mapFromCriteria(resourceType, criteria)).thenReturn(map);
		IFhirResourceDao<Patient> dao = mock(IFhirResourceDao.class);
		when(myDaoRegistry.getResourceDao(resourceType)).thenReturn(dao);
		int candidateSearchLimit = 2401;

		myMdmSettings.setCandidateSearchLimit(candidateSearchLimit);
		SimpleBundleProvider bundleProvider = new SimpleBundleProvider();

		bundleProvider.setSize(candidateSearchLimit + theOffset);
		when(dao.search(eq(map), any())).thenReturn(bundleProvider);

		// test
		MdmTransactionContext context = new MdmTransactionContext();
		Optional<IBundleProvider> result = myCandidateSearcher.search(resourceType, criteria, context);

		// validate
		assertTrue(map.isLoadSynchronous());
		assertEquals(candidateSearchLimit, map.getLoadSynchronousUpTo());
		boolean shouldNotFailBecauseOfTooManyMatches = theOffset < 0;
		assertEquals(shouldNotFailBecauseOfTooManyMatches, !context.isTooManyCandidatesMatched());
		assertEquals(result.isPresent(), shouldNotFailBecauseOfTooManyMatches);
	}
}
