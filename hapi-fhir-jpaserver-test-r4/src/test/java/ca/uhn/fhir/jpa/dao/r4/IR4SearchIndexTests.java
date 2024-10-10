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
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public interface IR4SearchIndexTests {

	IInterceptorService getInterceptorService();

	DaoRegistry getDaoRegistry();

	DataSource getDataSource();

	Logger getLogger();

	@SuppressWarnings("unchecked")
	private <T extends IBaseResource> IFhirResourceDao<T> getResourceDao(String theResourceType) {
		return getDaoRegistry()
			.getResourceDao(theResourceType);
	}

	@Test
	default void search_dateValue_withEquals() {
		// setup
		RequestDetails rd = new SystemRequestDetails();
		DateTimeType birthdayDateTime = new DateTimeType();
		birthdayDateTime.setValueAsString("1999-12-31");

		IFhirResourceDao<Patient> patientDao = getResourceDao("Patient");
		IFhirResourceDao<Observation> observationDao = getResourceDao("Observation");

		// create some patients (a few so we have a few to scan through)
		int birthYear = birthdayDateTime.getYear();
		for (int i = 0; i < 10; i++) {
			Patient patient = new Patient();
			patient.setActive(true);
			patient.addName()
				.setFamily("simpson")
				.addGiven("homer" + i);
			// i = 0 will give us the resource we're looking for
			// all other dates will be a new resource
			Date d = birthdayDateTime.getValue();
			int adjustment = -i;
			d.setYear((birthYear + adjustment) - 1900);
			patient.setBirthDate(d);

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

		DateParam birthdayParam = new DateParam();
		birthdayParam.setValueAsString("1999-12-31");
		searchParameterMap.add("birthdate", birthdayParam);

		/*
		 * the searches are very fast, regardless.
		 * so we'll be checking the actual query plan instead
		 */
		Object interceptor = new Object() {
			@Hook(Pointcut.JPA_PERFTRACE_RAW_SQL)
			public void captureSql(ServletRequestDetails theRequestDetails, SqlQueryList theQueries) {
				for (SqlQuery q : theQueries) {
					String sql = q.getSql(true, false);

					StringBuilder sb = new StringBuilder();
					try (Connection connection = getDataSource().getConnection()) {
						try (Statement stmt = connection.createStatement()) {
							ResultSet results = stmt.executeQuery("explain analyze " + sql);
							while (results.next()) {
								sb.append(results.getString(1));
							}
						}
					} catch (SQLException theE) {
						throw new RuntimeException(theE);
					}
					log(theRequestDetails, sb.toString());
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
				getLogger().info(theMsg);
			}
		};

		try {
			getInterceptorService().registerInterceptor(interceptor);

			// test
			IBundleProvider results = patientDao.search(searchParameterMap, rd);

			// verify
			assertNotNull(results);
			assertEquals(1, results.size());
		} finally {
			// remove the interceptor
			getInterceptorService().unregisterInterceptor(interceptor);
		}
	}
}
