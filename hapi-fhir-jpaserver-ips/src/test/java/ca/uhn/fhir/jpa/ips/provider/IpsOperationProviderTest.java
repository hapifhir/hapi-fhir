package ca.uhn.fhir.jpa.ips.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.ips.generator.IIpsGeneratorSvc;
import ca.uhn.fhir.rest.param.TokenParam;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.UriType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IpsOperationProviderTest {

	@Mock
	private IIpsGeneratorSvc myIpsGeneratorSvc;

	@RegisterExtension
	private RestfulServerExtension myServer = new RestfulServerExtension(FhirContext.forR4Cached())
		.withServer(t -> t.registerProviders(new IpsOperationProvider(myIpsGeneratorSvc)));

	@Captor
	private ArgumentCaptor<String> myProfileCaptor;
	@Captor
	private ArgumentCaptor<IIdType> myIdTypeCaptor;
	@Captor
	private ArgumentCaptor<TokenParam> myTokenCaptor;

	@Test
	public void testGenerateById() {
		// setup

		Bundle expected = new Bundle();
		expected.setType(Bundle.BundleType.DOCUMENT);
		when(myIpsGeneratorSvc.generateIps(any(), any(IIdType.class), any())).thenReturn(expected);

		// test

		Bundle actual = myServer
			.getFhirClient()
			.operation()
			.onInstance(new IdType("Patient/123"))
			.named("$summary")
			.withNoParameters(Parameters.class)
			.returnResourceType(Bundle.class)
			.execute();

		// verify
		assertEquals(Bundle.BundleType.DOCUMENT, actual.getType());
		verify(myIpsGeneratorSvc, times(1)).generateIps(any(), myIdTypeCaptor.capture(), myProfileCaptor.capture());
		assertEquals("Patient/123", myIdTypeCaptor.getValue().getValue());
		assertEquals(null, myProfileCaptor.getValue());
	}

	@Test
	public void testGenerateById_WithProfile() {
		// setup

		Bundle expected = new Bundle();
		expected.setType(Bundle.BundleType.DOCUMENT);
		when(myIpsGeneratorSvc.generateIps(any(), any(IIdType.class), any())).thenReturn(expected);

		// test

		Bundle actual = myServer
			.getFhirClient()
			.operation()
			.onInstance(new IdType("Patient/123"))
			.named("$summary")
			.withParameter(Parameters.class, "profile", new UriType("http://foo"))
			.returnResourceType(Bundle.class)
			.execute();

		// verify
		assertEquals(Bundle.BundleType.DOCUMENT, actual.getType());
		verify(myIpsGeneratorSvc, times(1)).generateIps(any(), myIdTypeCaptor.capture(), myProfileCaptor.capture());
		assertEquals("Patient/123", myIdTypeCaptor.getValue().getValue());
		assertEquals("http://foo", myProfileCaptor.getValue());
	}

	@Test
	public void testGenerateByIdentifier() {
		// setup

		Bundle expected = new Bundle();
		expected.setType(Bundle.BundleType.DOCUMENT);
		when(myIpsGeneratorSvc.generateIps(any(), any(TokenParam.class), any())).thenReturn(expected);

		// test

		Bundle actual = myServer
			.getFhirClient()
			.operation()
			.onType("Patient")
			.named("$summary")
			.withParameter(Parameters.class, "identifier", new Identifier().setSystem("http://system").setValue("value"))
			.returnResourceType(Bundle.class)
			.execute();

		// verify
		assertEquals(Bundle.BundleType.DOCUMENT, actual.getType());
		verify(myIpsGeneratorSvc, times(1)).generateIps(any(), myTokenCaptor.capture(), myProfileCaptor.capture());
		assertEquals("http://system", myTokenCaptor.getValue().getSystem());
		assertEquals("value", myTokenCaptor.getValue().getValue());
		assertEquals(null, myProfileCaptor.getValue());
	}

	@Test
	public void testGenerateByIdentifier_WithProfile() {
		// setup

		Bundle expected = new Bundle();
		expected.setType(Bundle.BundleType.DOCUMENT);
		when(myIpsGeneratorSvc.generateIps(any(), any(TokenParam.class), any())).thenReturn(expected);

		// test

		Bundle actual = myServer
			.getFhirClient()
			.operation()
			.onType("Patient")
			.named("$summary")
			.withParameter(Parameters.class, "identifier", new Identifier().setSystem("http://system").setValue("value"))
			.andParameter("profile", new UriType("http://foo"))
			.returnResourceType(Bundle.class)
			.execute();

		// verify

		assertEquals(Bundle.BundleType.DOCUMENT, actual.getType());
		verify(myIpsGeneratorSvc, times(1)).generateIps(any(), myTokenCaptor.capture(), myProfileCaptor.capture());
		assertEquals("http://system", myTokenCaptor.getValue().getSystem());
		assertEquals("value", myTokenCaptor.getValue().getValue());
		assertEquals("http://foo", myProfileCaptor.getValue());
	}

}
