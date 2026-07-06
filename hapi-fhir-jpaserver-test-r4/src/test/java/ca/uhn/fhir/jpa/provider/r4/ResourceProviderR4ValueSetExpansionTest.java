package ca.uhn.fhir.jpa.provider.r4;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDaoValueSet;
import ca.uhn.fhir.jpa.dao.data.ITermValueSetDao;
import ca.uhn.fhir.jpa.entity.TermValueSet;
import ca.uhn.fhir.jpa.provider.ValueSetOperationProvider;
import ca.uhn.fhir.jpa.search.DatabaseBackedPagingProvider;
import ca.uhn.fhir.jpa.term.api.ITermDeferredStorageSvc;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.jpa.test.config.TestHSearchAddInConfig;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.hl7.fhir.r4.model.CodeType;
import org.hl7.fhir.r4.model.Enumerations;
import org.hl7.fhir.r4.model.IntegerType;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StringType;
import org.hl7.fhir.r4.model.ValueSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;
import java.util.Optional;

import static ca.uhn.fhir.jpa.entity.TermValueSetPreExpansionStatusEnum.FAILED_TO_EXPAND;
import static ca.uhn.fhir.jpa.term.api.ITermValueSetExpansionSvc.ERROR_MESSAGE;
import static ca.uhn.fhir.jpa.term.api.ITermValueSetExpansionSvc.EXPANSION_STATUS;
import static ca.uhn.fhir.jpa.term.api.ITermValueSetExpansionSvc.HAS_MORE;
import static ca.uhn.fhir.jpa.term.api.ITermValueSetExpansionSvc.SUMMARY;
import static ca.uhn.fhir.jpa.term.api.ITermValueSetExpansionSvc.TOTAL;
import static ca.uhn.fhir.jpa.term.api.ITermValueSetExpansionSvc.URL;
import static ca.uhn.fhir.jpa.term.api.ITermValueSetExpansionSvc.VALUESET;
import static ca.uhn.fhir.rest.server.provider.ProviderConstants.OPERATION_EXPANSION_STATUS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Integration tests for the HAPI-specific ValueSet expansion operations
 * e.g. ({@code $hapi.fhir.expansion-status}, {@code $invalidate-expansion}).
 */
// Created by claude-opus-4-8
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestR4Config.class, TestHSearchAddInConfig.NoFT.class})
public class ResourceProviderR4ValueSetExpansionTest extends BaseJpaR4Test {

	@Autowired
	private FhirContext myFhirCtx;

	@Autowired
	private PlatformTransactionManager myTxManager;

	@Autowired
	@Qualifier("myValueSetDaoR4")
	private IFhirResourceDaoValueSet<ValueSet> myValueSetDao;

	@Autowired
	@Qualifier("myResourceProvidersR4")
	private ResourceProviderFactory myResourceProviders;

	@Autowired
	private ApplicationContext myAppCtx;

	@Autowired
	private ITermValueSetDao myTermValueSetDao;

	@Autowired
	private ITermDeferredStorageSvc myTerminologyDeferredStorageSvc;

	@SuppressWarnings("JUnitMalformedDeclaration")
	@RegisterExtension
	private final RestfulServerExtension myServer = new RestfulServerExtension(FhirContext.forR4Cached())
			.withServer(t -> t.registerProviders(myResourceProviders.createProviders()))
			.withServer(t -> t.registerProvider(myAppCtx.getBean(ValueSetOperationProvider.class)))
			.withServer(t -> t.setDefaultResponseEncoding(EncodingEnum.XML))
			.withServer(t -> t.setPagingProvider(myAppCtx.getBean(DatabaseBackedPagingProvider.class)));

	@Override
	public FhirContext getFhirContext() {
		return myFhirCtx;
	}

	@Override
	protected PlatformTransactionManager getTxManager() {
		return myTxManager;
	}

	@Test
	void expansionStatus_withNoFilters_returnsSummaryContainingTotalAndValueSetEntries() {
		createAndStoreTermValueSet("http://example.org/vs/es-basic-1", "Basic VS 1");
		createAndStoreTermValueSet("http://example.org/vs/es-basic-2", "Basic VS 2");

		Parameters response = myServer.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_EXPANSION_STATUS)
			.withNoParameters(Parameters.class)
			.execute();

		Parameters.ParametersParameterComponent summary = response.getParameter(SUMMARY);
		assertThat(summary).isNotNull();
		Optional<String> total = summary.getPart().stream()
			.filter(p -> TOTAL.equals(p.getName()))
			.map(p -> p.getValue().primitiveValue())
			.findFirst();
		assertThat(total).isPresent();
		assertThat(Integer.parseInt(total.get())).isGreaterThanOrEqualTo(2);

		List<Parameters.ParametersParameterComponent> valuesets = response.getParameter().stream()
			.filter(p -> VALUESET.equals(p.getName()))
			.toList();
		assertThat(valuesets).hasSizeGreaterThanOrEqualTo(2);
		assertThat(valuesets.get(0).getPart().stream().anyMatch(p -> EXPANSION_STATUS.equals(p.getName()))).isTrue();
	}

	@Test
	void expansionStatus_withFailedValueSet_includesErrorMessageInResponse() {
		createAndStoreTermValueSet("http://example.org/vs/es-failed-1", "Failed VS");
		updateTermValueSetStatusFailedWithError(
			"http://example.org/vs/es-failed-1",
			"CodeSystem not found: http://example.org/cs");

		Parameters inParams = new Parameters();
		inParams.addParameter(EXPANSION_STATUS, new CodeType(FAILED_TO_EXPAND.name()));

		Parameters response = myServer.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_EXPANSION_STATUS)
			.withParameters(inParams)
			.execute();

		// Find the specific valueset entry by URL
		Optional<Parameters.ParametersParameterComponent> failedEntry = response.getParameter().stream()
			.filter(p -> VALUESET.equals(p.getName()))
			.filter(vs -> vs.getPart().stream()
				.anyMatch(p -> URL.equals(p.getName())
					&& "http://example.org/vs/es-failed-1".equals(p.getValue().primitiveValue())))
			.findFirst();
		assertThat(failedEntry).isPresent();

		Optional<String> errorMessage = failedEntry.get().getPart().stream()
			.filter(p -> ERROR_MESSAGE.equals(p.getName()))
			.map(p -> p.getValue().primitiveValue())
			.findFirst();
		assertThat(errorMessage).contains("CodeSystem not found: http://example.org/cs");

		Optional<String> status = failedEntry.get().getPart().stream()
			.filter(p -> EXPANSION_STATUS.equals(p.getName()))
			.map(p -> p.getValue().primitiveValue())
			.findFirst();
		assertThat(status).contains(FAILED_TO_EXPAND.name());
	}

	@Test
	void expansionStatus_withStatusFilter_returnsOnlyMatchingValueSets() {
		createAndStoreTermValueSet("http://example.org/vs/es-filter-not-expanded", "Not Expanded VS");
		createAndStoreTermValueSet("http://example.org/vs/es-filter-failed", "Failed VS");
		updateTermValueSetStatusFailedWithError(
			"http://example.org/vs/es-filter-failed",
			"some error");

		Parameters inParams = new Parameters();
		inParams.addParameter(EXPANSION_STATUS, new CodeType(FAILED_TO_EXPAND.name()));

		Parameters response = myServer.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_EXPANSION_STATUS)
			.withParameters(inParams)
			.execute();

		// All returned entries must have FAILED_TO_EXPAND status (status filter applied correctly)
		List<String> returnedStatuses = response.getParameter().stream()
			.filter(p -> VALUESET.equals(p.getName()))
			.flatMap(vs -> vs.getPart().stream())
			.filter(p -> EXPANSION_STATUS.equals(p.getName()))
			.map(p -> p.getValue().primitiveValue())
			.toList();
		assertThat(returnedStatuses).isNotEmpty()
			.allSatisfy(s -> assertThat(s).isEqualTo(FAILED_TO_EXPAND.name()));

		// The NOT_EXPANDED ValueSet must not appear in results
		List<String> returnedUrls = response.getParameter().stream()
			.filter(p -> VALUESET.equals(p.getName()))
			.flatMap(vs -> vs.getPart().stream())
			.filter(p -> URL.equals(p.getName()))
			.map(p -> p.getValue().primitiveValue())
			.toList();
		assertThat(returnedUrls).doesNotContain("http://example.org/vs/es-filter-not-expanded")
			.contains("http://example.org/vs/es-filter-failed");
	}

	@Test
	void expansionStatus_withInvalidStatus_returns400() {
		Parameters inParams = new Parameters();
		inParams.addParameter("expansionStatus", new CodeType("NOT_A_REAL_STATUS"));

		var operation = myServer.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_EXPANSION_STATUS)
			.withParameters(inParams);
		assertThatThrownBy(operation::execute)
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("NOT_A_REAL_STATUS");
	}

	@Test
	void expansionStatus_withUrlStartsWithFilter_returnsOnlyMatchingValueSets() {
		createAndStoreTermValueSet("http://example.org/vs/es-sw-loinc", "SW Loinc");
		createAndStoreTermValueSet("http://example.org/vs/es-sw-snomed", "SW Snomed");

		// A bare "url" part (POST body) binds with no qualifier, i.e. a starts-with match.
		Parameters inParams = new Parameters();
		inParams.addParameter(URL, new StringType("http://example.org/vs/es-sw-l"));

		Parameters response = myServer.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_EXPANSION_STATUS)
			.withParameters(inParams)
			.execute();

		List<String> urls = getReturnedValueSetUrls(response);
		assertThat(urls).contains("http://example.org/vs/es-sw-loinc")
			.doesNotContain("http://example.org/vs/es-sw-snomed");
	}

	@Test
	void expansionStatus_withUrlContainsFilter_returnsOnlyMatchingValueSets() {
		createAndStoreTermValueSet("http://example.org/vs/es-co-target", "CO Target");
		createAndStoreTermValueSet("http://example.org/vs/es-co-other", "CO Other");

		// The :contains qualifier can only be expressed in the query string, so invoke via GET.
		// "co-target" is a substring (not a prefix), so a starts-with match would not find it.
		Parameters response = fetchExpansionStatus("url:contains=co-target");

		List<String> urls = getReturnedValueSetUrls(response);
		assertThat(urls).contains("http://example.org/vs/es-co-target")
			.doesNotContain("http://example.org/vs/es-co-other");
	}

	@Test
	void expansionStatus_withUrlExactFilter_returnsOnlyExactMatch() {
		createAndStoreTermValueSet("http://example.org/vs/es-exact", "Exact VS");
		createAndStoreTermValueSet("http://example.org/vs/es-exact-suffix", "Exact Suffix VS");

		// es-exact is a prefix of es-exact-suffix, so :exact must return only the exact match.
		Parameters response = fetchExpansionStatus("url:exact=http://example.org/vs/es-exact");

		List<String> urls = getReturnedValueSetUrls(response);
		assertThat(urls).contains("http://example.org/vs/es-exact")
			.doesNotContain("http://example.org/vs/es-exact-suffix");
	}

	@Test
	void expansionStatus_withNameContainsFilter_returnsOnlyMatchingValueSets() {
		createAndStoreTermValueSet("http://example.org/vs/es-name-1", "Cholesterol Panel");
		createAndStoreTermValueSet("http://example.org/vs/es-name-2", "Glucose Test");

		// "Panel" is a trailing substring of the first name, so :contains matches it but not the second.
		Parameters response = fetchExpansionStatus("name:contains=Panel");

		List<String> urls = getReturnedValueSetUrls(response);
		assertThat(urls).contains("http://example.org/vs/es-name-1")
			.doesNotContain("http://example.org/vs/es-name-2");
	}

	@Test
	void expansionStatus_withBothUrlAndName_returns400() {
		Parameters inParams = new Parameters();
		inParams.addParameter("url", new StringType("http://example.org/vs/es-both"));
		inParams.addParameter("name", new StringType("Whatever"));

		var operation = myServer.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_EXPANSION_STATUS)
			.withParameters(inParams);
		assertThatThrownBy(operation::execute)
			.isInstanceOf(InvalidRequestException.class)
			.hasMessageContaining("Only one of 'url' or 'name'");
	}

	@Test
	void expansionStatus_withCountAndOffset_limitsPageAndSetsHasMore() {
		createAndStoreTermValueSet("http://example.org/vs/es-page-1", "Page VS 1");
		createAndStoreTermValueSet("http://example.org/vs/es-page-2", "Page VS 2");
		createAndStoreTermValueSet("http://example.org/vs/es-page-3", "Page VS 3");

		Parameters inParams = new Parameters();
		inParams.addParameter("_count", new IntegerType(1));
		inParams.addParameter("_offset", new IntegerType(0));

		Parameters response = myServer.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_EXPANSION_STATUS)
			.withParameters(inParams)
			.execute();

		long valueSetCount = response.getParameter().stream()
			.filter(p -> VALUESET.equals(p.getName()))
			.count();
		assertThat(valueSetCount).isEqualTo(1);

		Optional<String> hasMore = response.getParameter(SUMMARY).getPart().stream()
			.filter(p -> HAS_MORE.equals(p.getName()))
			.map(p -> p.getValue().primitiveValue())
			.findFirst();
		assertThat(hasMore).contains("true");
	}

	@Test
	void expansionStatus_withNoPaging_usesDefaultPageSize() {
		for (int i = 0; i < 100; i++) {
			createAndStoreTermValueSet("http://example.org/vs/es-" + i, "Example VS");
		}

		Parameters response = myServer.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_EXPANSION_STATUS)
			.withNoParameters(Parameters.class)
			.execute();

		assertThat(response.getParameter().stream().filter(p -> VALUESET.equals(p.getName()))).hasSize(10);
	}

	@Test
	void expansionStatus_withLargeMaxPageSize_usesDefaultMaxPageSize() {
		for (int i = 0; i < 100; i++) {
			createAndStoreTermValueSet("http://example.org/vs/es-" + i, "Example VS");
		}

		Parameters inParams = new Parameters();
		inParams.addParameter("_count", new IntegerType(1000));

		Parameters response = myServer.getFhirClient()
			.operation()
			.onType(ValueSet.class)
			.named(OPERATION_EXPANSION_STATUS)
			.withParameters(inParams)
			.execute();

		assertThat(response.getParameter().stream().filter(p -> VALUESET.equals(p.getName()))).hasSize(50);
	}

	private Parameters fetchExpansionStatus(String theQuery) {
		return myServer.getFhirClient()
			.fetchResourceFromUrl(
				Parameters.class, myServer.getBaseUrl() + "/ValueSet/$hapi.fhir.expansion-status?" + theQuery);
	}

	private List<String> getReturnedValueSetUrls(Parameters theResponse) {
		return theResponse.getParameter().stream()
			.filter(p -> VALUESET.equals(p.getName()))
			.flatMap(vs -> vs.getPart().stream())
			.filter(p -> URL.equals(p.getName()))
			.map(p -> p.getValue().primitiveValue())
			.toList();
	}

	private void createAndStoreTermValueSet(String theUrl, String theName) {
		ValueSet vs = new ValueSet();
		vs.setUrl(theUrl);
		vs.setName(theName);
		vs.setStatus(Enumerations.PublicationStatus.ACTIVE);
		vs.getCompose().addInclude().setSystem("http://example.org/cs");
		myValueSetDao.create(vs, mySrd);
		myTerminologyDeferredStorageSvc.saveAllDeferred();
	}

	private void updateTermValueSetStatusFailedWithError(
		String theUrl, String theError) {
		runInTransaction(() -> {
			TermValueSet tvs = myTermValueSetDao.findTermValueSetByUrlAndNullVersion(theUrl)
				.orElseThrow(() -> new IllegalStateException("TermValueSet not found: " + theUrl));
			tvs.setExpansionStatus(FAILED_TO_EXPAND);
			tvs.setExpansionError(theError);
			myTermValueSetDao.save(tvs);
		});
	}
}
