/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
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
package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.batch2.api.IFirstJobStepWorker;
import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.RunOutcome;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.impl.HttpBasicAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.FileUtil;
import ca.uhn.fhir.util.StopWatch;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class FetchFilesStep implements IFirstJobStepWorker<BulkImportJobParameters, NdJsonFileJson> {
	private static final Logger ourLog = LoggerFactory.getLogger(FetchFilesStep.class);
	private static final List<String> ourValidContentTypes = Arrays.asList(
			Constants.CT_APP_NDJSON,
			Constants.CT_FHIR_NDJSON,
			Constants.CT_FHIR_JSON,
			Constants.CT_FHIR_JSON_NEW,
			Constants.CT_JSON,
			Constants.CT_TEXT);
	private static final List<String> ourValidNonNdJsonContentTypes =
			Arrays.asList(Constants.CT_FHIR_JSON, Constants.CT_FHIR_JSON_NEW, Constants.CT_JSON, Constants.CT_TEXT);

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkImportJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<NdJsonFileJson> theDataSink) {

		Integer maxBatchResourceCount = theStepExecutionDetails.getParameters().getMaxBatchResourceCount();
		if (maxBatchResourceCount == null || maxBatchResourceCount <= 0) {
			maxBatchResourceCount = BulkImportAppCtx.PARAM_MAXIMUM_BATCH_SIZE_DEFAULT;
		}

		try (CloseableHttpClient httpClient = newHttpClient(theStepExecutionDetails)) {

			StopWatch outerSw = new StopWatch();
			List<String> urls = theStepExecutionDetails.getParameters().getNdJsonUrls();

			for (String nextUrl : urls) {

				ourLog.info("Fetching URL: {}", nextUrl);
				StopWatch urlSw = new StopWatch();

				try (CloseableHttpResponse response = httpClient.execute(new HttpGet(nextUrl))) {
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode >= 400) {
						throw new JobExecutionFailedException(
								Msg.code(2056) + "Received HTTP " + statusCode + " from URL: " + nextUrl);
					}

					String contentType = response.getEntity().getContentType().getValue();
					Validate.isTrue(
							hasMatchingSubstring(contentType, ourValidContentTypes),
							"Received content type \"%s\" from URL: %s. This format is not one of the supported content type: %s",
							contentType,
							nextUrl,
							getContentTypesString());
					if (hasMatchingSubstring(contentType, ourValidNonNdJsonContentTypes)) {
						ourLog.info(
								"Received non-NDJSON content type \"{}\" from URL: {}. It will be processed but it may not complete correctly if the actual data is not NDJSON.",
								contentType,
								nextUrl);
					}

					try (InputStream inputStream = response.getEntity().getContent()) {
						try (LineIterator lineIterator =
								new LineIterator(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

							int chunkCount = 0;
							int lineCount = 0;
							StringBuilder builder = new StringBuilder();

							while (lineIterator.hasNext()) {

								String nextLine = lineIterator.nextLine();
								builder.append(nextLine).append('\n');

								lineCount++;
								int charCount = builder.length();
								int batchSizeChars = (int) (20 * FileUtils.ONE_MB);
								if (lineCount >= maxBatchResourceCount
										|| charCount >= batchSizeChars
										|| !lineIterator.hasNext()) {

									ourLog.info(
											"Loaded chunk {} of {} NDJSON file with {} resources from URL: {}",
											chunkCount,
											FileUtil.formatFileSize(charCount),
											lineCount,
											nextUrl);

									NdJsonFileJson data = new NdJsonFileJson();
									data.setNdJsonText(builder.toString());
									data.setSourceName(nextUrl);
									theDataSink.accept(data);

									builder.setLength(0);
									lineCount = 0;
									chunkCount++;
								}
							}
						}
					}
				}

				ourLog.info("Loaded and processed URL in {}", urlSw);
			}

			ourLog.info("Loaded and processed {} URLs in {}", urls.size(), outerSw);

			return new RunOutcome(0);

		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(2054) + e.getMessage(), e);
		}
	}

	private CloseableHttpClient newHttpClient(
			StepExecutionDetails<BulkImportJobParameters, ?> theStepExecutionDetails) {
		HttpClientBuilder builder = HttpClientBuilder.create();

		String httpBasicCredentials = theStepExecutionDetails.getParameters().getHttpBasicCredentials();
		if (isNotBlank(httpBasicCredentials)) {
			int colonIdx = httpBasicCredentials.indexOf(':');
			if (colonIdx == -1) {
				throw new JobExecutionFailedException(Msg.code(2055)
						+ "Invalid credential parameter provided. Must be in the form \"username:password\".");
			}
			String username = httpBasicCredentials.substring(0, colonIdx);
			String password = httpBasicCredentials.substring(colonIdx + 1);
			builder.addInterceptorFirst(new HttpBasicAuthInterceptor(username, password));
		}

		return builder.build();
	}

	private static boolean hasMatchingSubstring(String str, List<String> substrings) {
		return substrings.stream().anyMatch(str::contains);
	}

	private static String getContentTypesString() {
		return String.join(", ", ourValidContentTypes);
	}
}
