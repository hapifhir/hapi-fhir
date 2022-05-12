package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.rp.r4.PatientResourceProvider;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.junit.jupiter.api.BeforeEach;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class BaseJpaR4SystemTest extends BaseJpaR4Test {
	private RestfulServer myServer;

	@Override
	@SuppressWarnings("unchecked")
	@BeforeEach
	public void beforeInitMocks() throws Exception {
		super.beforeInitMocks();

		if (myServer == null) {
			myServer = new RestfulServer(myFhirContext, mySrdInterceptorService);

			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(myPatientDao);
			myServer.setResourceProviders(patientRp);
			myServer.init(mock(ServletConfig.class));
			myServer.setPagingProvider(myPagingProvider);
		}

		when(mySrd.getServer()).thenReturn(myServer);
		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
		when(mySrd.getServletRequest()).thenReturn(servletRequest);
		when(mySrd.getFhirServerBase()).thenReturn("http://example.com/base");
		when(servletRequest.getHeaderNames()).thenReturn(mock(Enumeration.class));
		when(servletRequest.getRequestURL()).thenReturn(new StringBuffer("/Patient"));
	}

}
