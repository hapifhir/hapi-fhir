package org.hl7.fhir.common.hapi.validation.support;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.metrics.BatchCallback;
import io.opentelemetry.api.metrics.Meter;
import io.opentelemetry.api.metrics.MeterBuilder;
import io.opentelemetry.api.metrics.ObservableLongMeasurement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.opentelemetry.api.common.AttributeKey.stringKey;

public class ValidationSupportChainMetrics {

	public static final String EXPIRING_CACHE_MAXIMUM_SIZE = "expiring_cache.maximum_size";
	public static final String EXPIRING_CACHE_CURRENT_ENTRIES = "expiring_cache.current_entries";
	public static final String NON_EXPIRING_CACHE_CURRENT_ENTRIES = "non_expiring_cache.current_entries";
	private static final String INSTRUMENTATION_NAME = "io.hapifhir.ValidationSupportChain";
	private static final AttributeKey<String> INSTANCE_NAME = stringKey("instance.name");
	private static final Logger ourLog = LoggerFactory.getLogger(ValidationSupportChainMetrics.class);
	private final ValidationSupportChain myValidationSupportChain;
	private BatchCallback myBatchCallback;

	public ValidationSupportChainMetrics(ValidationSupportChain theValidationSupportChain) {
		myValidationSupportChain = theValidationSupportChain;
	}

	public void start() {
		OpenTelemetry openTelemetry = GlobalOpenTelemetry.get();
		MeterBuilder meterBuilder = openTelemetry.getMeterProvider().meterBuilder(INSTRUMENTATION_NAME);
		Meter meter = meterBuilder.build();

		Attributes baseAttribute = Attributes.of(INSTANCE_NAME, myValidationSupportChain.getName());

		ObservableLongMeasurement expiringCacheMaxSize = meter.gaugeBuilder(EXPIRING_CACHE_MAXIMUM_SIZE)
				.ofLongs()
				.setUnit("{entries}")
				.setDescription("The maximum number of cache entries in the expiring cache.")
				.buildObserver();
		ObservableLongMeasurement expiringCacheCurrentEntries = meter.gaugeBuilder(EXPIRING_CACHE_CURRENT_ENTRIES)
				.ofLongs()
				.setUnit("{entries}")
				.setDescription("The current number of cache entries in the expiring cache.")
				.buildObserver();
		ObservableLongMeasurement nonExpiringCacheCurrentEntries = meter.gaugeBuilder(
						NON_EXPIRING_CACHE_CURRENT_ENTRIES)
				.ofLongs()
				.setUnit("{entries}")
				.setDescription("The current number of cache entries in the non-expiring cache.")
				.buildObserver();

		myBatchCallback = meter.batchCallback(
				() -> {
					long expiringCacheEntries = myValidationSupportChain.getMetricExpiringCacheEntries();
					int expiringCacheMaxSizeValue = myValidationSupportChain.getMetricExpiringCacheMaxSize();
					int nonExpiringCacheEntries = myValidationSupportChain.getMetricNonExpiringCacheEntries();
					ourLog.trace(
							"ExpiringMax[{}] ExpiringEntries[{}] NonExpiringEntries[{}]",
							expiringCacheMaxSizeValue,
							expiringCacheEntries,
							nonExpiringCacheEntries);
					expiringCacheMaxSize.record(expiringCacheMaxSizeValue, baseAttribute);
					expiringCacheCurrentEntries.record(expiringCacheEntries, baseAttribute);
					nonExpiringCacheCurrentEntries.record(nonExpiringCacheEntries, baseAttribute);
				},
				expiringCacheMaxSize,
				expiringCacheCurrentEntries,
				nonExpiringCacheCurrentEntries);
	}

	public void stop() {
		myBatchCallback.close();
	}
}
