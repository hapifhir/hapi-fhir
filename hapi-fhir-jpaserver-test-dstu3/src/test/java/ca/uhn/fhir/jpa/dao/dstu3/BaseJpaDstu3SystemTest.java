package ca.uhn.fhir.jpa.dao.dstu3;

import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.jpa.rp.dstu3.PatientResourceProvider;
import ca.uhn.fhir.jpa.test.BaseJpaDstu3Test;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.junit.jupiter.api.BeforeEach;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseJpaDstu3SystemTest extends BaseJpaDstu3Test {
	protected ServletRequestDetails mySrd;
	private RestfulServer myServer;

	@SuppressWarnings("unchecked")
	@BeforeEach
	public void before() throws ServletException {
		mySrd = mock(ServletRequestDetails.class);
		when(mySrd.getInterceptorBroadcaster()).thenReturn(mock(IInterceptorBroadcaster.class));

		if (myServer == null) {
			myServer = new RestfulServer(myFhirContext);

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
