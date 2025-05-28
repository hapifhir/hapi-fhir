package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.HookParams;
import ca.uhn.fhir.interceptor.api.IAnonymousInterceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.search.StorageProcessingMessage;
import ca.uhn.fhir.jpa.search.PersistedJpaSearchFirstPageBundleProvider;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.param.ParamPrefixEnum;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.rest.server.SimpleBundleProvider;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.BodyStructure;
import org.hl7.fhir.r4.model.CarePlan;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.Organization;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Questionnaire;
import org.hl7.fhir.r4.model.QuestionnaireResponse;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.SearchParameter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@SuppressWarnings({"Duplicates"})
public class FhirResourceDaoR4SearchIncludeTest extends BaseJpaR4Test {

	// FIXME: delete this class, moved to R5

	@Mock
	private IAnonymousInterceptor myAnonymousInterceptor;
	@Captor
	private ArgumentCaptor<HookParams> myParamsCaptor;

	@AfterEach
	public void afterEach() {
		myStorageSettings.setMaximumIncludesToLoadPerPage(JpaStorageSettings.DEFAULT_MAXIMUM_INCLUDES_TO_LOAD_PER_PAGE);
		myInterceptorRegistry.unregisterInterceptor(myAnonymousInterceptor);
	}

	@ParameterizedTest
	@CsvSource({
		"QuestionnaireResponse/qr   , false,      true",
	})
	public void testIncludeCanonicalReference(String theQuestionnaireRespId, boolean theReverse, boolean theMatchAll) {
		Questionnaire qWrongVersion = new Questionnaire();
		qWrongVersion.setId("qWrongVersion");
		qWrongVersion.setUrl("http://foo");
		qWrongVersion.setVersion("99.0");
		myQuestionnaireDao.update(qWrongVersion, mySrd);

		Questionnaire q = new Questionnaire();
		q.setId("q");
		q.setUrl("http://foo");
		q.setVersion("1.0");
		myQuestionnaireDao.update(q, mySrd);

		if (theQuestionnaireRespId.equals("QuestionnaireResponse/qr")) {
			QuestionnaireResponse qr = new QuestionnaireResponse();
			qr.setId("qr");
			qr.setQuestionnaire("http://foo");
			myQuestionnaireResponseDao.update(qr, mySrd);
		} else {
			QuestionnaireResponse qr2 = new QuestionnaireResponse();
			qr2.setId("qr2");
			qr2.setQuestionnaire("http://foo|1.0");
			myQuestionnaireResponseDao.update(qr2, mySrd);
		}

		logAllUriIndexes();
		logAllResourceLinks();

		// Create a QR and Q that have other URLs and shouldn't be turned up in searches here
		Questionnaire qIrrelevant = new Questionnaire();
		qIrrelevant.setId("qIrrelevant");
		qIrrelevant.setUrl("http://fooIrrelevant");
		qIrrelevant.setVersion("1.0");
		myQuestionnaireDao.update(qIrrelevant, mySrd);

		QuestionnaireResponse qrIrrelevant = new QuestionnaireResponse();
		qrIrrelevant.setId("qrIrrelevant");
		qrIrrelevant.setQuestionnaire("http://fooIrrelevant");
		myQuestionnaireResponseDao.update(qrIrrelevant, mySrd);

		IBundleProvider outcome;
		IFhirResourceDao<?> dao;
		SearchParameterMap map;
		String expectWarning = null;
		if (theReverse) {
			map = new SearchParameterMap();
			map.add("_id", new TokenParam("Questionnaire/q"));
			if (theMatchAll) {
				map.addRevInclude(IBaseResource.INCLUDE_ALL);
			} else {
				map.addRevInclude(QuestionnaireResponse.INCLUDE_QUESTIONNAIRE);
			}
			dao = myQuestionnaireDao;
		} else {
			map = new SearchParameterMap();
			map.add("_id", new TokenParam(theQuestionnaireRespId));
			if (theMatchAll) {
				map.addInclude(IBaseResource.INCLUDE_ALL);
			} else {
				map.addInclude(QuestionnaireResponse.INCLUDE_QUESTIONNAIRE);
			}
			dao = myQuestionnaireResponseDao;
		}

		if (theMatchAll) {
			expectWarning = "Search with _include=* can be inefficient";
		}

		myCaptureQueriesListener.clear();
		myInterceptorRegistry.registerAnonymousInterceptor(Pointcut.JPA_PERFTRACE_WARNING, myAnonymousInterceptor);

		map.setLoadSynchronous(true);
		outcome = dao.search(map, mySrd);
		List<String> outcomeValues = toUnqualifiedVersionlessIdValues(outcome);
		myCaptureQueriesListener.logSelectQueries();

		if (theReverse) {
			assertThat(outcomeValues).as(outcomeValues.toString()).containsExactlyInAnyOrder(
				theQuestionnaireRespId, "Questionnaire/q"
			);
		} else {
			assertThat(outcomeValues).as(outcomeValues.toString()).containsExactlyInAnyOrder(
				theQuestionnaireRespId, "Questionnaire/q", "Questionnaire/qWrongVersion"
			);
		}

		if (expectWarning == null) {
			verify(myAnonymousInterceptor, never()).invoke(eq(Pointcut.JPA_PERFTRACE_WARNING), myParamsCaptor.capture());
		} else {
			verify(myAnonymousInterceptor, times(1)).invoke(eq(Pointcut.JPA_PERFTRACE_WARNING), myParamsCaptor.capture());
			HookParams params = myParamsCaptor.getValue();
			assertThat(params.get(StorageProcessingMessage.class).getMessage()).contains(expectWarning);
		}

		if (!theReverse && theMatchAll) {
			myCaptureQueriesListener.logSelectQueries();
			SqlQuery searchForCanonicalReferencesQuery = myCaptureQueriesListener.getSelectQueriesForCurrentThread().get(2);
			// Make sure we have the right query - If this ever fails, maybe we have optimized the queries
			// (or somehow made things worse) and the search for the canonical target is no longer the 4th
			// SQL query
			assertThat(searchForCanonicalReferencesQuery.getSql(true, false)).contains("rispu1_0.HASH_IDENTITY in ('-600769180185160063')");
			assertTrue(
				searchForCanonicalReferencesQuery.getSql(true, false).contains("rispu1_0.SP_URI in ('http://foo')")
				|| searchForCanonicalReferencesQuery.getSql(true, false).contains("rispu1_0.SP_URI in ('http://foo','http://foo|1.0')"),
				searchForCanonicalReferencesQuery.getSql(true, false)
			);
		}

	}

}
