package ca.uhn.fhir.jpa.dao.dstu2;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.rp.dstu2.PatientResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Before;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseJpaDstu2SystemTest extends BaseJpaDstu2Test {
	private RestfulServer myServer;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@SuppressWarnings("unchecked")
	@Before
	public void before() throws ServletException {
		mySrd = mock(ServletRequestDetails.class);
		when(mySrd.getInterceptorBroadcaster()).thenReturn(mock(IInterceptorBroadcaster.class));

		if (myServer == null) {
			myServer = new RestfulServer(myFhirCtx);

			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(myPatientDao);
			myServer.setResourceProviders(patientRp);
			myServer.init(mock(ServletConfig.class));
		}

		when(mySrd.getServer()).thenReturn(myServer);
		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
		when(mySrd.getServletRequest()).thenReturn(servletRequest);
		when(mySrd.getFhirServerBase()).thenReturn("http://example.com/base");
		when(servletRequest.getHeaderNames()).thenReturn(mock(Enumeration.class));
		when(servletRequest.getRequestURL()).thenReturn(new StringBuffer("/Patient"));
	}

}
