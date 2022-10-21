package ca.uhn.fhir.jpa.provider;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.provider.r4.IConsentExtensionProvider;
import ca.uhn.fhir.jpa.provider.r4.MemberMatcherR4Helper;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Consent;
import org.hl7.fhir.r4.model.Coverage;
import org.hl7.fhir.r4.model.Extension;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.StringType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberMatcherR4HelperTest {

	@Mock
	private IFhirResourceDao<Coverage> myCoverageDao;

	@Mock
	private IFhirResourceDao<Patient> myPatientDao;

	@Mock
	private IFhirResourceDao<Consent> myConsentDao;

	@Spy
	private FhirContext myFhirContext = FhirContext.forR4Cached();

	@InjectMocks
	private MemberMatcherR4Helper myHelper;

	@Nested
	public class MemberMatchWithoutConsentProvider {

		@BeforeEach
		public void before() {
			MockitoAnnotations.openMocks(this);
		}

		@Test
		public void addClientIdAsExtensionToConsentIfAvailable_noProvider_doesNothing() {
			// setup
			Consent consent = new Consent();

			// test
			myHelper.addClientIdAsExtensionToConsentIfAvailable(consent);

			// verify
			verify(myConsentDao, Mockito.never())
				.create(any(Consent.class));
		}
	}

	@Nested
	public class MemberMatchWithConsentProvider {
		@Mock
		private IConsentExtensionProvider myExtensionProvider;

		@BeforeEach
		public void before() {
			myHelper.setConsentExtensionProvider(myExtensionProvider);
		}

		@Test
		public void addClientIdAsExtensionToConsentIfAvailable_withProvider_addsExtensionAndSaves() {
			// setup
			Consent consent = new Consent();
			consent.setId("Consent/RED");
			Extension ext = new Extension();
			ext.setUrl("http://example.com");
			ext.setValue(new StringType("value"));

			// when
			when(myExtensionProvider.getConsentExtension(any(IBaseResource.class)))
				.thenReturn(Collections.singleton(ext));

			// test
			myHelper.addClientIdAsExtensionToConsentIfAvailable(consent);

			// verify
			ArgumentCaptor<Consent> consentArgumentCaptor = ArgumentCaptor.forClass(Consent.class);
			verify(myConsentDao).create(consentArgumentCaptor.capture());
			Consent saved = consentArgumentCaptor.getValue();
			assertEquals(consent.getId(), saved.getId());
			assertNotNull(saved.getExtension());
			assertEquals(1, saved.getExtension().size());
			Extension savedExt = saved.getExtension().get(0);
			assertEquals(ext.getUrl(), savedExt.getUrl());
			assertEquals(ext.getValue(), savedExt.getValue());
		}
	}
}
