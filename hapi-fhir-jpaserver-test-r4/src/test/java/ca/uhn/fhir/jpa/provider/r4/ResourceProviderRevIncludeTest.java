package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.jpa.provider.BaseResourceProviderR4Test;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.regex.Pattern;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.matchesPattern;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceProviderRevIncludeTest extends BaseResourceProviderR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(ResourceProviderRevIncludeTest.class);


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
			.cacheControl(new CacheControlDirective().setNoStore(true))
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

	private static void assertSqlContains(String theSql, String theExpectedSql) {
		assertThat(theSql, matchesPattern(Pattern.compile(".*" + theExpectedSql + ".*", Pattern.DOTALL)));
	}

}
