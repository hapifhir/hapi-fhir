package ca.uhn.fhir.jpa.dao.dstu3;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.AfterClass;
import org.junit.Before;

import ca.uhn.fhir.jpa.rp.dstu3.PatientResourceProvider;
import ca.uhn.fhir.rest.method.IRequestOperationCallback;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import ca.uhn.fhir.util.TestUtil;

public abstract class BaseJpaDstu3SystemTest extends BaseJpaDstu3Test {
	protected ServletRequestDetails mySrd;
	private RestfulServer myServer;

	@AfterClass
	public static void afterClassClearContext() {
		TestUtil.clearAllStaticFieldsForUnitTest();
	}


	@SuppressWarnings("unchecked")
	@Before
	public void before() throws ServletException {
		mySrd = mock(ServletRequestDetails.class);
		when(mySrd.getRequestOperationCallback()).thenReturn(mock(IRequestOperationCallback.class));

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
