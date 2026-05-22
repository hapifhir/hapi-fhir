/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.batch2.util;

import ca.uhn.fhir.batch2.model.JobInstance;
import ca.uhn.fhir.batch2.progress.InstanceProgress;
import ca.uhn.fhir.batch2.progress.StepProgressData;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.trace.Span;
import jakarta.annotation.Nullable;

import java.util.Map;

public class BatchJobOpenTelemetryUtils {

	public static final String JOB_STEP_EXECUTION_SPAN_NAME = "hapifhir.batch_job.execute";
	public static final String JOB_PROGRESS_EVENT_NAME = "hapifhir.batch_job.progress";

	private static final AttributeKey<String> OTEL_JOB_DEF_ID_ATT_KEY =
			AttributeKey.stringKey("hapifhir.batch_job.definition_id");
	private static final AttributeKey<String> OTEL_JOB_DEF_VER_ATT_KEY =
			AttributeKey.stringKey("hapifhir.batch_job.definition_version");
	private static final AttributeKey<String> OTEL_JOB_STEP_ID_ATT_KEY =
			AttributeKey.stringKey("hapifhir.batch_job.step_id");
	private static final AttributeKey<String> OTEL_JOB_INSTANCE_ID_ATT_KEY =
			AttributeKey.stringKey("hapifhir.batch_job.instance_id");
	private static final AttributeKey<String> OTEL_JOB_CHUNK_ID_ATT_KEY =
			AttributeKey.stringKey("hapifhir.batch_job.chunk_id");
	private static final AttributeKey<Double> OTEL_JOB_PROGRESS_ATT_KEY =
			AttributeKey.doubleKey("hapifhir.batch_job.progress");
	private static final AttributeKey<String> OTEL_JOB_STATUS_ATT_KEY =
			AttributeKey.stringKey("hapifhir.batch_job.status");
	private static final AttributeKey<Long> OTEL_JOB_RECORDS_PROCESSED_ATT_KEY =
			AttributeKey.longKey("hapifhir.batch_job.records_processed");
	private static final AttributeKey<Double> OTEL_JOB_THROUGHPUT_ATT_KEY =
			AttributeKey.doubleKey("hapifhir.batch_job.throughput_per_sec");
	private static final AttributeKey<String> OTEL_STEP_PROGRESS_SUMMARY_ATT_KEY =
			AttributeKey.stringKey("hapifhir.batch_job.step_progress_summary");

	private BatchJobOpenTelemetryUtils() {}

	public static void addAttributesToCurrentSpan(
			String theJobDefinitionId,
			int theJobDefinitionVersion,
			String theJobInstanceId,
			String theStepId,
			@Nullable String theChunkId) {

		Span currentSpan = Span.current();
		AttributesBuilder attBuilder = Attributes.builder()
				.put(OTEL_JOB_DEF_ID_ATT_KEY, theJobDefinitionId)
				.put(OTEL_JOB_DEF_VER_ATT_KEY, Integer.toString(theJobDefinitionVersion))
				.put(OTEL_JOB_STEP_ID_ATT_KEY, theStepId)
				.put(OTEL_JOB_INSTANCE_ID_ATT_KEY, theJobInstanceId)
				// it is ok to put null values, builder ignores them
				.put(OTEL_JOB_CHUNK_ID_ATT_KEY, theChunkId);

		currentSpan.setAllAttributes(attBuilder.build());
	}

	/**
	 * Adds a progress event to the current span with per-step throughput data.
	 * This enables operators to observe job progress and identify bottleneck steps
	 * via distributed tracing systems.
	 *
	 * @param theJobInstance the current job instance
	 * @param theInstanceProgress the calculated progress including per-step data
	 */
	public static void addProgressEventToCurrentSpan(
			JobInstance theJobInstance,
			InstanceProgress theInstanceProgress) {

		Span currentSpan = Span.current();
		if (!currentSpan.isRecording()) {
			return;
		}

		AttributesBuilder attBuilder = Attributes.builder()
				.put(OTEL_JOB_DEF_ID_ATT_KEY, theJobInstance.getJobDefinitionId())
				.put(OTEL_JOB_INSTANCE_ID_ATT_KEY, theJobInstance.getInstanceId())
				.put(OTEL_JOB_PROGRESS_ATT_KEY, theJobInstance.getProgress())
				.put(OTEL_JOB_STATUS_ATT_KEY, theJobInstance.getStatus().name());

		if (theJobInstance.getCombinedRecordsProcessed() != null) {
			attBuilder.put(OTEL_JOB_RECORDS_PROCESSED_ATT_KEY,
					theJobInstance.getCombinedRecordsProcessed().longValue());
		}
		if (theJobInstance.getCombinedRecordsProcessedPerSecond() != null) {
			attBuilder.put(OTEL_JOB_THROUGHPUT_ATT_KEY, theJobInstance.getCombinedRecordsProcessedPerSecond());
		}

		// Build per-step summary
		Map<String, StepProgressData> stepProgressMap = theInstanceProgress.getStepProgressMap();
		if (!stepProgressMap.isEmpty()) {
			StringBuilder summary = new StringBuilder();
			for (Map.Entry<String, StepProgressData> entry : stepProgressMap.entrySet()) {
				StepProgressData stepData = entry.getValue();
				if (summary.length() > 0) {
					summary.append("; ");
				}
				summary.append(entry.getKey())
						.append("[")
						.append(String.format("%.0f%%", stepData.getCompletionPercentage() * 100))
						.append(", ")
						.append(stepData.getRecordsProcessed()).append("rec")
						.append(", ")
						.append(String.format("%.1f", stepData.getThroughputPerSecond())).append("/s")
						.append("]");
			}
			attBuilder.put(OTEL_STEP_PROGRESS_SUMMARY_ATT_KEY, summary.toString());
		}

		currentSpan.addEvent(JOB_PROGRESS_EVENT_NAME, attBuilder.build());
	}
}
