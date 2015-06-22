package ca.uhn.fhir.rest.server;

import static org.mockito.Mockito.*;

import java.util.Arrays;

import javax.servlet.ServletException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.DiagnosticReport;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Destroy;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.api.MethodOutcome;

/**
 * Created by Bill de Beaubien on 11/10/2014.
 */

@RunWith(MockitoJUnitRunner.class)
public class DestroyTest {

	private static final FhirContext ourCtx = FhirContext.forDstu1();

	@Test
	public void testDestroyCallsAnnotatedMethodsOnProviders() throws ServletException {
		RestfulServer servlet = new RestfulServer(ourCtx);
		DiagnosticReportProvider provider = spy(new DiagnosticReportProvider());
		servlet.setResourceProviders(Arrays.asList((IResourceProvider) provider));
		servlet.init();
		servlet.destroy();
		verify(provider).destroy();
	}

	@Test
	public void testChainsUpThroughSuperclasses() throws ServletException {
		RestfulServer servlet = new RestfulServer(ourCtx);
		DerivedDiagnosticReportProvider provider = spy(new DerivedDiagnosticReportProvider());
		servlet.setResourceProviders(Arrays.asList((IResourceProvider) provider));
		servlet.init();
		servlet.destroy();
		verify(provider).destroy();
	}

	@Test
	public void testNoDestroyDoesNotCauseInfiniteRecursion() throws ServletException {
		RestfulServer servlet = new RestfulServer(ourCtx);
		DiagnosticReportProviderSansDestroy provider = new DiagnosticReportProviderSansDestroy();
		servlet.setResourceProviders(Arrays.asList((IResourceProvider) provider));
		servlet.init();
		servlet.destroy();
		// nothing to verify other than the test didn't hang forever
	}

	public class DiagnosticReportProvider implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return DiagnosticReport.class;
		}

		@Create
		public MethodOutcome createResource(@ResourceParam DiagnosticReport theDiagnosticReport) {
			// do nothing
			return new MethodOutcome();
		}

		@Destroy
		public void destroy() {
			// do nothing
		}
	}

	public class DerivedDiagnosticReportProvider extends DiagnosticReportProvider {
		// move along, nothing to see here
	}

	public class DiagnosticReportProviderSansDestroy implements IResourceProvider {

		@Override
		public Class<? extends IResource> getResourceType() {
			return DiagnosticReport.class;
		}

		@Create
		public MethodOutcome createResource(@ResourceParam DiagnosticReport theDiagnosticReport) {
			// do nothing
			return new MethodOutcome();
		}
	}
}
