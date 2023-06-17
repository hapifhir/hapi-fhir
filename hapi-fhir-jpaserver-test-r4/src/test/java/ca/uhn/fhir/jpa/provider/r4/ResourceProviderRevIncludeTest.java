package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.model.api.Include;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.CareTeam;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.DetectedIssue;
import org.hl7.fhir.r4.model.Group;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceProviderRevIncludeTest extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderRevIncludeTest.class);



	class SqlCapturingInterceptor {
		SqlQueryList queryList = null;
		@Hook(Pointcut.JPA_PERFTRACE_RAW_SQL)
		public void trackRequest(RequestDetails theRequestDetails, SqlQueryList theQueryList) {
			if (queryList == null) {
				queryList = theQueryList;
			} else {
				queryList.addAll(theQueryList);
			}
		}

		public SqlQueryList getQueryList() {
			return queryList;
		}
	}
	@Test
	public void testRevIncludeSql() {
		final String methodName = "testSearchWithRevIncludes";
		Patient p = new Patient();
		p.addName().setFamily(methodName);
		IIdType pid = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();

		Condition c = new Condition();
		c.getSubject().setReferenceElement(pid);
		IIdType cid = myClient.create().resource(c).execute().getId().toUnqualifiedVersionless();

		myCaptureQueriesListener.clear();
		Bundle bundle = myClient.search().forResource(Patient.class)
			.where(Patient.NAME.matches().value(methodName))
			.revInclude(Condition.INCLUDE_PATIENT)
			.cacheControl(new CacheControlDirective().setNoStore(true).setNoCache(true))
			.returnBundle(Bundle.class)
			.execute();
		List<IBaseResource> foundResources = BundleUtil.toListOfResources(myFhirContext, bundle);

		myCaptureQueriesListener.logSelectQueries();
		List<SqlQuery> selectQueries = myCaptureQueriesListener.getSelectQueries();
		assertEquals(3, selectQueries.size());
		assertSqlContains(selectQueries.get(0).toString(), "FROM\\s+HFJ_SPIDX_STRING");
		assertSqlContains(selectQueries.get(1).toString(), "WHERE\\s+r.src_path = 'Condition.subject.where\\(resolve\\(\\) is Patient\\)'");
		assertThat(foundResources, hasSize(2));
		Patient patient = (Patient) foundResources.get(0);
		Condition condition = (Condition) foundResources.get(1);
		assertEquals(pid.getIdPart(), patient.getIdElement().getIdPart());
		assertEquals(cid.getIdPart(), condition.getIdElement().getIdPart());
		assertEquals(methodName, patient.getName().get(0).getFamily());
	}

	@Test
	public void testRevincludeIterate() {
		// /Patient?_id=123&_revinclude:iterate=CareTeam:subject:Group&_revinclude=Group:member:Patient&_revinclude=DetectedIssue:patient

		Patient p = new Patient();
		String methodName = "testRevincludeIterate";
		p.addName().setFamily(methodName);
		IIdType pid = myClient.create().resource(p).execute().getId().toUnqualifiedVersionless();
		ourLog.info("Created patient: {}", pid.getValue()); // 1

		Group g = new Group();
		g.addMember().setEntity(new Reference(pid));
		IIdType gid = myClient.create().resource(g).execute().getId().toUnqualifiedVersionless();
		ourLog.info("Created group: {}", gid.getValue()); // 2

		CareTeam ct = new CareTeam();
		ct.getSubject().setReferenceElement(gid);
		IIdType ctid = myClient.create().resource(ct).execute().getId().toUnqualifiedVersionless();
		ourLog.info("Created care team: {}", ctid.getValue()); // 3

		List<IIdType> dids = new ArrayList<>();
		for (int i = 0; i < 100; ++i){
			DetectedIssue di = new DetectedIssue();
			di.getPatient().setReferenceElement(pid);
			IIdType diid = myClient.create().resource(di).execute().getId().toUnqualifiedVersionless();
			ourLog.info("Created detected issue: {}", diid.getValue()); // 4...103
			dids.add(diid);
		}
		SqlCapturingInterceptor sqlCapturingInterceptor = new SqlCapturingInterceptor();
		myCaptureQueriesListener.clear();
		myInterceptorRegistry.registerInterceptor(sqlCapturingInterceptor);
		Bundle bundle = myClient.search()
			.forResource(Patient.class)
			.count(200)
			.where(Patient.RES_ID.exactly().codes(pid.getIdPart()))
			.revInclude(new Include("CareTeam:subject:Group").setRecurse(true))
//			.revInclude(new Include("CareTeam:subject:Group"))
			.revInclude(new Include("Group:member:Patient"))
			.revInclude(DetectedIssue.INCLUDE_PATIENT)
			.returnBundle(Bundle.class)
			.execute();

		List<IBaseResource> foundResources = BundleUtil.toListOfResources(myFhirContext, bundle);
		Patient patient = (Patient) foundResources.get(0);
		Group group = (Group) foundResources.get(1);
		CareTeam careTeam = (CareTeam) foundResources.get(2);
		DetectedIssue detectedIssue = (DetectedIssue) foundResources.get(3);
		assertEquals(pid.getIdPart(), patient.getIdElement().getIdPart());
		assertEquals(gid.getIdPart(), group.getIdElement().getIdPart());
		assertEquals(ctid.getIdPart(), careTeam.getIdElement().getIdPart());
		assertEquals(methodName, patient.getName().get(0).getFamily());

		//Ensure that the revincludes are included in the query list of the sql trace.
		//TODO GGG/KHS reduce this to something less than 6 by smarter iterating and getting the resource types earlier when needed.
		assertEquals(6, sqlCapturingInterceptor.getQueryList().size());
		myInterceptorRegistry.unregisterInterceptor(sqlCapturingInterceptor);
	}

	private static void assertSqlContains(String theSql, String theExpectedSql) {
		assertThat(theSql, matchesPattern(Pattern.compile(".*" + theExpectedSql + ".*", Pattern.DOTALL)));
	}

}
