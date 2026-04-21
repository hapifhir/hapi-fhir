package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.context.support.ConceptValidationOptions;
import ca.uhn.fhir.context.support.IValidationSupport;
import ca.uhn.fhir.context.support.LookupCodeRequest;
import ca.uhn.fhir.context.support.ValidationSupportContext;
import ca.uhn.fhir.jpa.term.custom.CustomTerminologySet;
import ca.uhn.fhir.jpa.test.BaseJpaR4Test;
import ca.uhn.fhir.util.StopWatch;
import org.hl7.fhir.r4.model.CodeSystem;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-4-6
@Disabled("Performance test — run manually to compare cache implementations")
class TerminologyLookupCachePerformanceTest extends BaseJpaR4Test {

	private static final Logger ourLog = LoggerFactory.getLogger(TerminologyLookupCachePerformanceTest.class);

	private static final int NUM_CODE_SYSTEMS = 100;
	private static final int NUM_CONCEPTS_PER_CS = 100;
	private static final int LOOKUP_ITERATIONS = 500;
	private static final String CS_URL_PREFIX = "http://perf-test/cs-";

	@Test
	void lookupCode_warmCache_throughput() {
		List<String> codeSystemUrls = createCodeSystems();

		// Warm: one lookup per code system
		lookupFirstConcept(codeSystemUrls);

		// Measure: repeated cache-hit lookups
		int totalLookups = LOOKUP_ITERATIONS * codeSystemUrls.size();
		StopWatch sw = new StopWatch();
		for (int i = 0; i < LOOKUP_ITERATIONS; i++) {
			lookupFirstConcept(codeSystemUrls);
		}
		long elapsedMs = sw.getMillis();

		ourLog.info("Warm cache lookupCode: {} lookups in {}ms ({}/sec)",
			totalLookups, elapsedMs, sw.formatThroughput(totalLookups, TimeUnit.SECONDS));
	}

	@Test
	void lookupCode_coldVsWarm_comparison() {
		List<String> codeSystemUrls = createCodeSystems();

		// Cold: first lookup per code system forces DB fetch
		StopWatch coldSw = new StopWatch();
		lookupFirstConcept(codeSystemUrls);
		long coldMs = coldSw.getMillis();

		// Warm: second lookup hits cache
		StopWatch warmSw = new StopWatch();
		lookupFirstConcept(codeSystemUrls);
		long warmMs = warmSw.getMillis();

		ourLog.info("Cold: {}ms, Warm: {}ms (for {} code systems)",
			coldMs, warmMs, codeSystemUrls.size());
	}

	@Test
	void lookupCode_afterInvalidation_repopulationCost() {
		List<String> codeSystemUrls = createCodeSystems();

		// Warm the cache
		lookupFirstConcept(codeSystemUrls);

		// Measure: repeated invalidate + re-lookup cycles
		int cycles = 100;
		StopWatch sw = new StopWatch();
		for (int i = 0; i < cycles; i++) {
			myTermSvc.invalidateCaches();
			lookupFirstConcept(codeSystemUrls);
		}
		long elapsedMs = sw.getMillis();

		ourLog.info("Invalidation cycles: {} cycles in {}ms ({}ms per cycle, {} code systems per cycle)",
			cycles, elapsedMs, elapsedMs / cycles, codeSystemUrls.size());
	}

	@Test
	void validateCode_warmCache_throughput() {
		String url = CS_URL_PREFIX + "validate";
		createNotPresentCodeSystem(url);
		CustomTerminologySet delta = new CustomTerminologySet();
		delta.addRootConcept("codeA", "Display A");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(url, delta);

		ValidationSupportContext valCtx = new ValidationSupportContext(myValidationSupport);
		ConceptValidationOptions opts = new ConceptValidationOptions();

		// Warm
		myTermSvc.validateCode(valCtx, opts, url, "codeA", null, null);

		// Measure
		StopWatch sw = new StopWatch();
		for (int i = 0; i < LOOKUP_ITERATIONS; i++) {
			IValidationSupport.CodeValidationResult result =
				myTermSvc.validateCode(valCtx, opts, url, "codeA", null, null);
			assertThat(result).isNotNull();
			assertThat(result.isOk()).isTrue();
		}
		long elapsedMs = sw.getMillis();

		ourLog.info("Warm cache validateCode: {} validations in {}ms ({}/sec)",
			LOOKUP_ITERATIONS, elapsedMs, sw.formatThroughput(LOOKUP_ITERATIONS, TimeUnit.SECONDS));
	}

	@Test
	void isCodeSystemSupported_warmCache_throughput() {
		List<String> codeSystemUrls = createCodeSystems();
		ValidationSupportContext valCtx = new ValidationSupportContext(myValidationSupport);

		// Warm
		for (String url : codeSystemUrls) {
			myTermSvc.isCodeSystemSupported(valCtx, url);
		}

		// Measure
		int totalChecks = LOOKUP_ITERATIONS * codeSystemUrls.size();
		StopWatch sw = new StopWatch();
		for (int i = 0; i < LOOKUP_ITERATIONS; i++) {
			for (String url : codeSystemUrls) {
				assertThat(myTermSvc.isCodeSystemSupported(valCtx, url)).isTrue();
			}
		}
		long elapsedMs = sw.getMillis();

		ourLog.info("Warm cache isCodeSystemSupported: {} checks in {}ms ({}/sec)",
			totalChecks, elapsedMs, sw.formatThroughput(totalChecks, TimeUnit.SECONDS));
	}

	@Test
	void deltaAdd_invalidateAndLookup_throughput() {
		// Setup: code system with initial concept
		createNotPresentCodeSystem(CS_URL_PREFIX + "delta");
		CustomTerminologySet delta = new CustomTerminologySet();
		delta.addRootConcept("initial", "Initial");
		myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(CS_URL_PREFIX + "delta", delta);

		// Warm
		myTermSvc.lookupCode(
			new ValidationSupportContext(myValidationSupport),
			new LookupCodeRequest(CS_URL_PREFIX + "delta", "initial"));

		// Measure: delta-add + lookup cycles (exercises invalidation path)
		int cycles = 50;
		StopWatch sw = new StopWatch();
		for (int i = 0; i < cycles; i++) {
			CustomTerminologySet addDelta = new CustomTerminologySet();
			addDelta.addRootConcept("code-" + i, "Display " + i);
			myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(CS_URL_PREFIX + "delta", addDelta);

			IValidationSupport.LookupCodeResult result = myTermSvc.lookupCode(
				new ValidationSupportContext(myValidationSupport),
				new LookupCodeRequest(CS_URL_PREFIX + "delta", "code-" + i));
			assertThat(result).isNotNull();
			assertThat(result.isFound()).isTrue();
		}
		long elapsedMs = sw.getMillis();

		ourLog.info("Delta-add + lookup: {} cycles in {}ms ({}ms per cycle)",
			cycles, elapsedMs, elapsedMs / cycles);
	}

	private List<String> createCodeSystems() {
		List<String> urls = new ArrayList<>();
		for (int i = 0; i < NUM_CODE_SYSTEMS; i++) {
			String url = CS_URL_PREFIX + i;
			CodeSystem cs = new CodeSystem();
			cs.setUrl(url);
			cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
			myCodeSystemDao.create(cs, mySrd);

			CustomTerminologySet delta = new CustomTerminologySet();
			for (int j = 0; j < NUM_CONCEPTS_PER_CS; j++) {
				delta.addRootConcept("concept-" + j, "Display " + j);
			}
			myTermCodeSystemStorageSvc.applyDeltaCodeSystemsAdd(url, delta);
			urls.add(url);
		}
		return urls;
	}

	private void createNotPresentCodeSystem(String theUrl) {
		CodeSystem cs = new CodeSystem();
		cs.setUrl(theUrl);
		cs.setContent(CodeSystem.CodeSystemContentMode.NOTPRESENT);
		myCodeSystemDao.create(cs, mySrd);
	}

	private void lookupFirstConcept(List<String> theCodeSystemUrls) {
		for (String url : theCodeSystemUrls) {
			IValidationSupport.LookupCodeResult result = myTermSvc.lookupCode(
				new ValidationSupportContext(myValidationSupport),
				new LookupCodeRequest(url, "concept-0"));
			assertThat(result).isNotNull();
			assertThat(result.isFound()).isTrue();
		}
	}
}
