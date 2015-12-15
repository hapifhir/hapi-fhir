package ca.uhn.fhir.jpa.dao.dstu2;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Enumeration;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.Before;

import ca.uhn.fhir.provider.dstu2.PatientResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;

public abstract class BaseJpaDstu2SystemTest extends BaseJpaDstu2Test {
	protected ServletRequestDetails myRequestDetails;
	private RestfulServer myServer;

	@SuppressWarnings("unchecked")
	@Before
	public void before() throws ServletException {
		myRequestDetails = mock(ServletRequestDetails.class);

		if (myServer == null) {
			myServer = new RestfulServer(myFhirCtx);

			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setResourceDaoFactory(myDaoFactory);
			myServer.setResourceProviders(patientRp);
			myServer.init(mock(ServletConfig.class));
		}

		when(myRequestDetails.getServer()).thenReturn(myServer);
		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
		when(myRequestDetails.getServletRequest()).thenReturn(servletRequest);
		when(myRequestDetails.getFhirServerBase()).thenReturn("http://example.com/base");
		when(servletRequest.getHeaderNames()).thenReturn(mock(Enumeration.class));
		when(servletRequest.getRequestURL()).thenReturn(new StringBuffer("/Patient"));
	}

}
