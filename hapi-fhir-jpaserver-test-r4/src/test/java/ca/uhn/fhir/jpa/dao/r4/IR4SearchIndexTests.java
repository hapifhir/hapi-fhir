package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.IInterceptorService;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.model.search.SearchRuntimeDetails;
import ca.uhn.fhir.jpa.searchparam.SearchParameterMap;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.param.DateParam;
import ca.uhn.fhir.rest.param.DateRangeParam;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public interface IR4SearchIndexTests {

	IInterceptorService getInterceptorService();

	DaoRegistry getDaoRegistry();

	DataSource getDataSource();

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> IFhirResourceDao<T> getResourceDao(String theResourceType) {
		return getDaoRegistry()
			.getResourceDao(theResourceType);
	}

	@ParameterizedTest
	@CsvSource(value = {
		// actual, lowerbound, upperbound
		"1999-12-31,1999-01-01,2000-12-31",
		"1999-12-31,1999-01-01,",
		"1999-12-31,,2000-12-31",
		"1999-12-31,1999-12-31,1999-12-31"
		// TODO - add times; not realistic for bdays, but the same issue arises
	})
	default void search_dateValues_usesWhereToHelpQueryPlans(String theBirthdate, String theLowerBound, String theUpperBound) {
		// setup
		Pattern pattern = Pattern.compile("(\\(\\w+\\.\\w+\\s*(?:\\Q>=\\E|\\Q<=\\E)\\s*\\'[0-9-]*\\s\\d{2}:\\d{2}:\\d{2}\\.\\d+\\'\\))");
		RequestDetails rd = new SystemRequestDetails();
		DateTimeType birthdayDateTime = new DateTimeType();
		birthdayDateTime.setValueAsString(theBirthdate);

		IFhirResourceDao<Patient> patientDao = getResourceDao("Patient");
		IFhirResourceDao<Observation> observationDao = getResourceDao("Observation");

		// create some patients (a few so we have a few to scan through)
		for (int i = 0; i < 100; i++) {
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addName()
				.setFamily("simpson")
				.addGiven("homer" + i);
			if (i < 5) {
				patient.setBirthDate(birthdayDateTime.getValue());
			} else {
				Date d = birthdayDateTime.getValue();
//				d.setMonth(i);
				d.setYear(2000 - i);
				patient.setBirthDate(d);
			}
			patientDao.create(patient, rd);
		}

		// add a bunch of things with recent dates
		Date now = new Date();
		for (int i = 0; i < 200; i++) {
			Observation obs = new Observation();
			now.setDate(i % 28); // 28 because that's the shortest month
			obs.setIssued(now);
			obs.setStatus(Observation.ObservationStatus.CORRECTED);
			observationDao.create(obs, rd);
		}

		SearchParameterMap searchParameterMap = new SearchParameterMap();
		searchParameterMap.setLoadSynchronous(true);

		DateRangeParam birthdayParamRange = new DateRangeParam();
		if (isNotBlank(theLowerBound)) {
			birthdayParamRange.setLowerBound(new DateParam(theLowerBound));
		}
		if (isNotBlank(theUpperBound)) {
			birthdayParamRange.setUpperBound(new DateParam(theUpperBound));
		}
		searchParameterMap.add("birthdate", birthdayParamRange);

		/*
		 * the searches are very fast, regardless.
		 * so we'll be checking the actual query plan instead
		 */
		Object interceptor = new Object() {
			@Hook(Pointcut.JPA_PERFTRACE_RAW_SQL)
			public void captureSql(ServletRequestDetails theRequestDetails, SqlQueryList theQueries) {
				for (SqlQuery q : theQueries) {
					String sql = q.getSql(true, false);
//					log(theRequestDetails, sql);
//					Matcher matcher = pattern.matcher(sql);
//					List<String> whereClauseParts = new ArrayList<>();
//					while (matcher.find()) {
//						whereClauseParts.add(matcher.group());
//					}
//
//					// it has date param calcs means it's a date query
//					if (!whereClauseParts.isEmpty()) {
					try (Connection connection = getDataSource().getConnection()) {
						try (Statement stmt = connection.createStatement()) {
							ResultSet results = stmt.executeQuery("explain analyze " + sql);
							System.out.println("hi");
							while (results.next()) {
								System.out.println(results.getString(1));
							}
							System.out.println("----");
						}
					} catch (SQLException theE) {
						throw new RuntimeException(theE);
					}
//					} else {
//						System.out.println("blah");
//					}
				}
			}

			@Hook(Pointcut.JPA_PERFTRACE_SEARCH_FIRST_RESULT_LOADED)
			public void firstResultLoaded(ServletRequestDetails theRequestDetails, SearchRuntimeDetails theSearchRuntimeDetails) {
				String msg = "SQL statement returned first result in "
					+ theSearchRuntimeDetails.getQueryStopwatch().toString();
				log(theRequestDetails, msg);
			}

			@Hook(Pointcut.JPA_PERFTRACE_SEARCH_SELECT_COMPLETE)
			public void selectComplete(ServletRequestDetails theRequestDetails, SearchRuntimeDetails theSearchRuntimeDetails) {
				String msg = "SQL statement execution complete in "
					+ theSearchRuntimeDetails.getQueryStopwatch().toString() + " - Returned "
					+ theSearchRuntimeDetails.getFoundMatchesCount() + " results";
				log(theRequestDetails, msg);
			}

			private void log(ServletRequestDetails theRequestDetails, String theMsg) {
				System.out.println(theMsg);
			}
		};
		try {
			getInterceptorService().registerInterceptor(interceptor);

			IBundleProvider results = patientDao.search(searchParameterMap, rd);

			// verify
			assertNotNull(results);
		} finally {
			// remove the interceptor
			getInterceptorService().unregisterInterceptor(interceptor);
		}
	}
}
