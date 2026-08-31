package ca.uhn.fhir.jpa.dao.index;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.api.IInterceptorBroadcaster;
import ca.uhn.fhir.interceptor.executor.InterceptorService;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.model.DaoMethodOutcome;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.TransactionDetails;
import ca.uhn.fhir.util.CanonicalIdentifier;
import ca.uhn.fhir.util.UrlUtil;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Reference;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DaoResourceLinkResolverTest {

	@Spy
	private JpaStorageSettings myStorageSettings = new JpaStorageSettings();

	@Spy
	private FhirContext myContext = FhirContext.forR4Cached();

	@Spy
	private DaoRegistry myDaoRegistry = new DaoRegistry(FhirContext.forR4Cached());

	@Spy
	private IInterceptorBroadcaster myInterceptorBroadcaster = new InterceptorService();

	@Mock
	private IIdHelperService<JpaPid> myIdHelperService;

	@Mock
	private IFhirResourceDao<Patient> myPatientDao;

	@InjectMocks
	private DaoResourceLinkResolver<JpaPid> myResolver;

	@ParameterizedTest
	@MethodSource("getLinkResolutionTestCases")
	void testLinkResolution(LinkResolutionTestCase theTestCase) {
		DaoResourceLinkResolver<JpaPid> resolver = new DaoResourceLinkResolver<>();
		List<CanonicalIdentifier> canonicalIdentifier = resolver.extractIdentifierFromUrl(theTestCase.url);
		assertEquals(theTestCase.expectedMatches, canonicalIdentifier);
	}

	@Test
	void testCreatePlaceholder_conditionalReference_passesSearchUrlToCreate() {
		// Setup
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		myStorageSettings.setPopulateIdentifierInAutoCreatedPlaceholderReferenceTargets(true);
		registerPatientDao();
		when(myPatientDao.create(any(Patient.class), any(RequestDetails.class))).thenReturn(outcomeWithCreatedEntity());

		Reference reference = new Reference("Patient?identifier=http://foo|123");

		// Test
		myResolver.createPlaceholderTargetIfConfiguredToDoSo(
				new Observation(), Patient.class, reference, null, new SystemRequestDetails(), new TransactionDetails());

		// Verify - the placeholder passed to the DAO must carry the identifier-based match URL
		// as userdata, so the create path registers a duplicate-create guard record for it
		ArgumentCaptor<Patient> captor = ArgumentCaptor.forClass(Patient.class);
		verify(myPatientDao).create(captor.capture(), any(RequestDetails.class));
		assertThat(captor.getValue().getUserData(JpaConstants.PLACEHOLDER_RESOURCE_SEARCH_URL))
				.isEqualTo("identifier=" + UrlUtil.escapeUrlParam("http://foo") + "|" + UrlUtil.escapeUrlParam("123"));
	}

	@Test
	void testCreatePlaceholder_literalReference_doesNotPassSearchUrl() {
		// Setup
		myStorageSettings.setAutoCreatePlaceholderReferenceTargets(true);
		registerPatientDao();
		when(myPatientDao.update(any(), isNull(), eq(true), eq(false), any(), any()))
				.thenReturn(outcomeWithCreatedEntity());

		Reference reference = new Reference("Patient/ABC");

		// Test
		myResolver.createPlaceholderTargetIfConfiguredToDoSo(
				new Observation(), Patient.class, reference, "ABC", new SystemRequestDetails(), new TransactionDetails());

		// Verify - a literal reference is not a conditional URL, so no match URL is passed along
		ArgumentCaptor<Patient> captor = ArgumentCaptor.forClass(Patient.class);
		verify(myPatientDao).update(captor.capture(), isNull(), eq(true), eq(false), any(), any());
		assertThat(captor.getValue().getUserData(JpaConstants.PLACEHOLDER_RESOURCE_SEARCH_URL)).isNull();
	}

	private void registerPatientDao() {
		when(myPatientDao.getResourceType()).thenReturn(Patient.class);
		when(myIdHelperService.newPid(any())).thenReturn(JpaPid.fromId(123L));
		myDaoRegistry.register(myPatientDao);
	}

	private DaoMethodOutcome outcomeWithCreatedEntity() {
		ResourceTable entity = new ResourceTable();
		entity.setIdForUnitTest(123L);
		entity.setResourceType("Patient");
		entity.setFhirId("123");
		return new DaoMethodOutcome().setEntity(entity);
	}

	static List<LinkResolutionTestCase> getLinkResolutionTestCases() {
		return List.of(

			// One identifier
			new LinkResolutionTestCase(
				"Patient?identifier=http://hapifhir.io/fhir/namingsystem/my_id|123456",
				List.of(new CanonicalIdentifier("http://hapifhir.io/fhir/namingsystem/my_id", "123456"))
			),

			// Multiple identifiers
			new LinkResolutionTestCase(
				"Patient?_tag:not=http://hapifhir.io/fhir/namingsystem/mdm-record-status|GOLDEn_rEcorD&identifier=https://www.my.org/identifiers/memBER|123456",
				List.of(new CanonicalIdentifier("https://www.my.org/identifiers/memBER", "123456"))
			),

			// No identifier
			new LinkResolutionTestCase(
				"Patient?name=smith",
				List.of()
			),

			// Identifier plus other parameters: Should be treated as no identifiers
			// since there are other params in there too
			new LinkResolutionTestCase(
				"Patient?identifier=http://foo|123&name=smith",
				List.of()
			),

			// Identifier with OR values
			new LinkResolutionTestCase(
				"Patient?identifier=http://foo|123,http://foo|456",
				List.of()
			),

			// Identifier with modifier
			new LinkResolutionTestCase(
				"Patient?identifier:not=http://foo|123",
				List.of()
			),

			// Identifier with modifier
			new LinkResolutionTestCase(
				"Patient?identifier:missing=true",
				List.of()
			)

		);
	}

	private record LinkResolutionTestCase(String url, List<CanonicalIdentifier> expectedMatches){}

}


