/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.common.AttributesBuilder;
import io.opentelemetry.api.trace.Span;
import jakarta.annotation.Nullable;

public class BatchJobOpenTelemetryUtils {

	public static final String JOB_STEP_EXECUTION_SPAN_NAME = "hapifhir.batch_job.execute";
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
}
