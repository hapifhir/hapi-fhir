package ca.uhn.fhirtest.config;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.util.SqlQuery;
import ca.uhn.fhir.jpa.util.SqlQueryList;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.StopWatch;
import ca.uhn.fhir.util.UrlUtil;

/**
 * At some point we should move this to somewhere accessible to other projects,
 * and document it.
 */
public class SqlCaptureInterceptor {

	@Hook(Pointcut.JPA_PERFTRACE_RAW_SQL)
	public void captureSql(ServletRequestDetails theRequestDetails, SqlQueryList theQueries) {
		if (theRequestDetails != null) {
			String[] captureSqls = theRequestDetails.getParameters().get("_captureSql");
			if (captureSqls == null || !captureSqls[0].equals("true")) {
				return;
			}
			for (int i = 0; i < theQueries.size(); i++) {
				SqlQuery nextQuery = theQueries.get(i);
				String sql = nextQuery.getSql(true, false);
				sql = UrlUtil.sanitizeUrlPart(sql);

				theRequestDetails.getResponse().addHeader("X-Executed-SQL", sql);
				theRequestDetails.getResponse().addHeader("X-Executed-SQL-Outcome", "Returned " + nextQuery.getSize() + " in " + StopWatch.formatMillis(nextQuery.getElapsedTime()));

			}
		}

	}

}
