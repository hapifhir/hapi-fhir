package ca.uhn.fhir.rest.server.interceptor;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.server.interceptor.consent.ConsentInterceptor;
import ca.uhn.fhir.rest.server.interceptor.consent.IConsentService;
import ca.uhn.fhir.rest.server.provider.HashMapResourceProvider;
import ca.uhn.fhir.test.utilities.HttpClientExtension;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import ca.uhn.fhir.util.BundleUtil;
import jakarta.annotation.Nullable;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Answers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;

import static ca.uhn.fhir.model.valueset.BundleTypeEnum.SEARCHSET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConsentInterceptorSkipResourceTest {
	@RegisterExtension
	protected final HttpClientExtension myClient = new HttpClientExtension();
	protected static final FhirContext ourCtx = FhirContext.forR4Cached();
	protected int myPort;
	protected static final HashMapResourceProvider<Patient> ourPatientProvider = new HashMapResourceProvider<>(ourCtx, Patient.class);
	protected static final HashMapResourceProvider<Bundle> ourBundleProvider = new HashMapResourceProvider<>(ourCtx, Bundle.class);

	@RegisterExtension
	static final RestfulServerExtension ourServer = new RestfulServerExtension(ourCtx)
			.registerProvider(ourPatientProvider)
			.registerProvider(ourBundleProvider);

	@Mock(answer = Answers.CALLS_REAL_METHODS)
	protected IConsentService myConsentSvc;
	protected ConsentInterceptor myInterceptor;

	private static class CustomConsentInterceptor extends ConsentInterceptor {
		CustomConsentInterceptor(IConsentService theConsentService) {
			super(theConsentService);
		}
		@Override
		protected boolean isSkipServiceForResource(@Nullable IBaseResource theResource) {
			if (theResource == null) {
				return true;
			}
			if (theResource.fhirType().equals("Bundle")
					&& SEARCHSET.equals(BundleUtil.getBundleTypeEnum(ourCtx, (IBaseBundle) theResource))) {
				return true;
			}
			return theResource.fhirType().equals("Composition");
		}
	}

	@AfterEach
	public void after() {
		ourServer.unregisterInterceptor(myInterceptor);
	}

	@BeforeEach
	public void before() {
		myPort = ourServer.getPort();

		myInterceptor = new CustomConsentInterceptor(myConsentSvc);

		ourServer.registerInterceptor(myInterceptor);
		ourPatientProvider.clear();
		ourBundleProvider.clear();
	}

	@Test
	void testRead_CanSeeAndWillSeeAreNotCalled() throws IOException {
		Patient patient = new Patient();
		patient.setId("P1");
		ourPatientProvider.store(patient);

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
		}

		verify(myConsentSvc, times(1)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, times(1)).canSeeResource(any(), isA(Patient.class), any());
		verify(myConsentSvc, times(1)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, times(1)).willSeeResource(any(), isA(Patient.class), any());
	}

	@Test
	void testGetBundle_WhenResourceTypeBundleSearchSet_CanSeeAndWillSeeAreNotCalled() throws IOException {
		Patient patient = new Patient();
		patient.setId("P1");
		ourPatientProvider.store(patient);

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Patient?id=P1");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
		}

		verify(myConsentSvc, times(1)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, times(0)).canSeeResource(any(), isA(Bundle.class), any());
		verify(myConsentSvc, times(1)).canSeeResource(any(), isA(Patient.class), any());
		verify(myConsentSvc, times(1)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, times(0)).willSeeResource(any(), isA(Bundle.class), any());
		verify(myConsentSvc, times(1)).willSeeResource(any(), isA(Patient.class), any());
	}

	@Test
	void testGetBundle_WithChildResourceTypeComposition_WillSeeIsNotCalled() throws IOException {
		ourBundleProvider.store(createDocumentBundle());

		HttpGet httpGet = new HttpGet("http://localhost:" + myPort + "/Bundle/test-bundle-id");
		try (CloseableHttpResponse status = myClient.execute(httpGet)) {
			assertEquals(200, status.getStatusLine().getStatusCode());
		}

		verify(myConsentSvc, times(1)).canSeeResource(any(), any(), any());
		verify(myConsentSvc, times(1)).canSeeResource(any(), isA(Bundle.class), any());
		verify(myConsentSvc, times(2)).willSeeResource(any(), any(), any());
		verify(myConsentSvc, times(1)).willSeeResource(any(), isA(Bundle.class), any());
		verify(myConsentSvc, times(1)).willSeeResource(any(), isA(Patient.class), any());
		verify(myConsentSvc, times(0)).willSeeResource(any(), isA(Composition.class), any());
	}

	protected Bundle createDocumentBundle() {
		Bundle bundle = new Bundle();
		bundle.setType(Bundle.BundleType.DOCUMENT);
		bundle.setId("test-bundle-id");
		Composition composition = new Composition();
		composition.setId("composition-in-bundle");

		Patient patient = new Patient();
		patient.setId("patient-in-bundle");

		bundle.addEntry().setResource(composition);
		bundle.addEntry().setResource(patient);
		return bundle;
	}
}
