package ca.uhn.fhir.jpa.dao.r4;

import ca.uhn.fhir.jpa.rp.r4.PatientResourceProvider;
import ca.uhn.fhir.rest.server.RestfulServer;
import org.junit.jupiter.api.BeforeEach;

import javax.servlet.ServletConfig;

import static org.mockito.Mockito.mock;

public abstract class BaseJpaR4SystemTest extends BaseJpaR4Test {
	private RestfulServer myServer;

	@Override
	@SuppressWarnings("unchecked")
	@BeforeEach
	public void beforeInitMocks() throws Exception {
		super.beforeInitMocks();

		// FIXME: remove comments
//		mySrd = mock(ServletRequestDetails.class);

		if (myServer == null) {
			myServer = new RestfulServer(myFhirCtx, mySrdInterceptorService);

			PatientResourceProvider patientRp = new PatientResourceProvider();
			patientRp.setDao(myPatientDao);
			myServer.setResourceProviders(patientRp);
			myServer.init(mock(ServletConfig.class));
		}

		// FIXME: remove comments
//		when(mySrd.getServer()).thenReturn(myServer);
//		HttpServletRequest servletRequest = mock(HttpServletRequest.class);
//		when(mySrd.getServletRequest()).thenReturn(servletRequest);
//		when(mySrd.getFhirServerBase()).thenReturn("http://example.com/base");
//		when(servletRequest.getHeaderNames()).thenReturn(mock(Enumeration.class));
//		when(servletRequest.getRequestURL()).thenReturn(new StringBuffer("/Patient"));
	}

}
