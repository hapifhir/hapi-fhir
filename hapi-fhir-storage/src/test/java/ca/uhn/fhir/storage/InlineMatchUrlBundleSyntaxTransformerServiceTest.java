package ca.uhn.fhir.storage;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.r4.TransactionProcessorVersionAdapterR4;
import ca.uhn.fhir.jpa.searchparam.MatchUrlService;
import ca.uhn.fhir.rest.server.util.FhirContextSearchParamRegistry;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Resource;
import org.hl7.fhir.r4.model.ResourceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import static ca.uhn.fhir.util.HapiExtensions.EXT_RESOURCE_PLACEHOLDER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// Created by claude-opus-4-7
class InlineMatchUrlBundleSyntaxTransformerServiceTest {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(InlineMatchUrlBundleSyntaxTransformerServiceTest.class);

	private static final FhirContext ourFhirContext = FhirContext.forR4Cached();

	private InlineMatchUrlBundleSyntaxTransformerService mySvc;

	@BeforeEach
	void setUp() {
		FhirContextSearchParamRegistry searchParamRegistry = new FhirContextSearchParamRegistry(ourFhirContext);
		MatchUrlService matchUrlService = new MatchUrlService(ourFhirContext, searchParamRegistry);
		mySvc = new InlineMatchUrlBundleSyntaxTransformerService(
				ourFhirContext, matchUrlService, new TransactionProcessorVersionAdapterR4());
	}

	@ParameterizedTest
	@ArgumentsSource(SingleResourceRefScenarios.class)
	void testTransaction_singleResourceRefScenarios(
			String theComment, String theBundle, int theExpectedSyntheticCount, Consumer<Bundle> theAssertions) {
		// fixed setup
		Bundle requestBundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);

		// then
		int actualSyntheticCount = mySvc.transform(requestBundle);

		// expectations
		ourLog.info(ourFhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(requestBundle));
		assertThat(actualSyntheticCount).isEqualTo(theExpectedSyntheticCount);
		assertNotNull(requestBundle);
		assertNotNull(theAssertions);
		theAssertions.accept(requestBundle);
	}

	@ParameterizedTest
	@ArgumentsSource(SingleResourceRefThrowScenarios.class)
	void testTransaction_singleResourceRefThrowScenarios(
			String theComment,
			String theBundle,
			Class<? extends Throwable> theExpectedException,
			String theExpectedMessage) {
		Bundle requestBundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);

		assertThatThrownBy(() -> mySvc.transform(requestBundle))
				.isInstanceOf(theExpectedException)
				.hasMessage(theExpectedMessage);
	}

	@ParameterizedTest
	@ArgumentsSource(MultiResourceRefScenarios.class)
	void testTransaction_multiResourceRefScenarios(
			String theComment, String theBundle, int theExpectedSyntheticCount, Consumer<Bundle> theAssertions) {
		Bundle requestBundle = ourFhirContext.newJsonParser().parseResource(Bundle.class, theBundle);

		int actualSyntheticCount = mySvc.transform(requestBundle);

		assertThat(actualSyntheticCount).isEqualTo(theExpectedSyntheticCount);
		assertNotNull(requestBundle);
		assertNotNull(theAssertions);
		theAssertions.accept(requestBundle);
	}

	@SafeVarargs
	static Consumer<Bundle> bundleAssert(int theExpectedSize, Consumer<Bundle>... theOtherAssertions) {
		return theBundle -> {
			assertThat(theBundle.getEntry()).size().isEqualTo(theExpectedSize);
			for (Consumer<Bundle> theAssertion : theOtherAssertions) {
				theAssertion.accept(theBundle);
			}
		};
	}

	static int findSyntheticEntryIndex(
			List<Bundle.BundleEntryComponent> theEntries, String theMatchUrl) {
		for (int i = 0; i < theEntries.size(); i++) {
			if (theMatchUrl.equals(theEntries.get(i).getRequest().getIfNoneExist())) {
				return i;
			}
		}
		throw new AssertionError("No synthetic entry found for match URL: " + theMatchUrl);
	}

	/**
	 * Assert that the source entry at the given index is of the expected class and that
	 * the given reference accessor returns the expected reference value.
	 */
	static <T extends Resource> void assertSourceEntryAt(
			Bundle theBundle,
			int theSourceIndex,
			Class<T> theSourceClass,
			String theExpectedReference,
			Function<T, String> theReferenceAccessor) {
		Bundle.BundleEntryComponent source = theBundle.getEntry().get(theSourceIndex);
		assertThat(source.getResource()).isInstanceOf(theSourceClass);

		T sourceResource = theSourceClass.cast(source.getResource());
		assertEquals(theExpectedReference, theReferenceAccessor.apply(sourceResource));
	}

	/**
	 * Expected (system, value) pair for an identifier on a synthetic placeholder.
	 */
	record SysVal(String system, String value) {}

	/**
	 * Convenience overload for the common single-identifier case.
	 */
	static String assertSyntheticEntryAt(
			Bundle theBundle,
			int theIndex,
			ResourceType theExpectedResourceType,
			String theExpectedMatchUrl,
			String theExpectedSystem,
			String theExpectedValue) {
		return assertSyntheticEntryAt(
				theBundle,
				theIndex,
				theExpectedResourceType,
				theExpectedMatchUrl,
				List.of(new SysVal(theExpectedSystem, theExpectedValue)));
	}

	/**
	 * Assert that the synthetic entry at the given index is correctly created, and return its fullUrl.
	 */
	static String assertSyntheticEntryAt(
			Bundle theBundle,
			int theIndex,
			ResourceType theExpectedResourceType,
			String theExpectedMatchUrl,
			List<SysVal> theExpectedIdentifiers) {
		Bundle.BundleEntryComponent entry = theBundle.getEntry().get(theIndex);

		// assert resource type
		assertThat(entry.getResource().getResourceType()).isEqualTo(theExpectedResourceType);

		// assert conditional create
		Bundle.BundleEntryRequestComponent request = entry.getRequest();
		assertThat(request.getMethod()).isEqualTo(Bundle.HTTPVerb.POST);
		assertThat(request.getIfNoneExist()).isEqualTo(theExpectedMatchUrl);

		// assert fullUrl is present
		assertThat(entry.getFullUrl()).startsWith("urn:uuid:");

		// assert placeholder extension
		DomainResource resource = (DomainResource) entry.getResource();
		assertThat(resource.getExtensionByUrl(EXT_RESOURCE_PLACEHOLDER)
			.getValueAsPrimitive()
			.getValueAsString())
			.isEqualTo("true");

		// assert identifiers — count matches list size, each pair present (order-agnostic)
		List<Identifier> identifiers =
			ourFhirContext.newTerser().getAllPopulatedChildElementsOfType(resource, Identifier.class);
		assertThat(identifiers).hasSize(theExpectedIdentifiers.size());
		List<SysVal> actualIdentifiers = identifiers.stream()
				.map(id -> new SysVal(id.getSystem(), id.getValue()))
				.toList();
		assertThat(actualIdentifiers).containsExactlyInAnyOrderElementsOf(theExpectedIdentifiers);

		return entry.getFullUrl();
	}


}
