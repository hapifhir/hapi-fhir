/*-
 * #%L
 * hapi-fhir-storage-batch2-jobs
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.context.RuntimeResourceDefinition;
import ca.uhn.fhir.context.RuntimeSearchParam;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.client.impl.HttpBasicAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.FileUtil;
import ca.uhn.fhir.util.JsonUtil;
import ca.uhn.fhir.util.StopWatch;
import com.google.common.annotations.VisibleForTesting;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Autowired
	private FhirContext myFhirContext;

	@VisibleForTesting
	public void setFhirContext(FhirContext theFhirContext) {
		myFhirContext = theFhirContext;
	}

	@Nonnull
	@Override
	public RunOutcome run(
			@Nonnull StepExecutionDetails<BulkImportJobParameters, VoidModel> theStepExecutionDetails,
			@Nonnull IJobDataSink<NdJsonFileJson> theDataSink) {

		Integer maxBatchResourceCount = theStepExecutionDetails.getParameters().getMaxBatchResourceCount();
		if (maxBatchResourceCount == null || maxBatchResourceCount <= 0) {
			maxBatchResourceCount = BulkImportAppCtx.PARAM_MAXIMUM_BATCH_SIZE_DEFAULT;
		}

		Map<String, FileBuffer> groupToBuffer = new HashMap<>();

		String chunkByCompartmentName = theStepExecutionDetails.getParameters().getChunkByCompartmentName();
		if (isNotBlank(chunkByCompartmentName)) {
			ourLog.info("Will group resources by compartment: {}", chunkByCompartmentName);
		}

		try (CloseableHttpClient httpClient = newHttpClient(theStepExecutionDetails)) {

			StopWatch outerSw = new StopWatch();
			List<String> urls = theStepExecutionDetails.getParameters().getNdJsonUrls();

			int lineCountOverallTotal = 0;
			int lineCountOverallProgress = 0;
			for (String url : urls) {
				lineCountOverallTotal += fetchAllFilesAndCountLines(url, httpClient);
			}

			for (String url : urls) {

				ourLog.info("Fetching URL (pass 2 for processing): {}", url);
				StopWatch urlSw = new StopWatch();
				int chunkCount = 0;

				try (CloseableHttpResponse response = httpClient.execute(new HttpGet(url))) {
					validateStatusCodeAndContentType(url, response);

					try (InputStream inputStream = response.getEntity().getContent()) {
						try (LineIterator lineIterator =
								new LineIterator(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

							while (lineIterator.hasNext()) {

								String groupName = "";
								String nextLine = lineIterator.nextLine();
								if (isNotBlank(nextLine)) {
									lineCountOverallProgress++;

									if (isNotBlank(chunkByCompartmentName)) {
										groupName = determineCompartmentGroup(chunkByCompartmentName, nextLine);
									}

									FileBuffer buffer = groupToBuffer.computeIfAbsent(groupName, p -> new FileBuffer());
									buffer.addResource(nextLine);

									int batchSizeChars = (int) (2 * FileUtils.ONE_MB);
									if (buffer.getResourceCount() >= maxBatchResourceCount
											|| buffer.getFileSize() >= batchSizeChars) {
										transmitBuffer(
												theDataSink,
												url,
												chunkCount,
												buffer,
												lineCountOverallProgress,
												lineCountOverallTotal);
										chunkCount++;
									}
								}
							}
						}
					}
				}

				/*
				 * Send any lingering data in buffers. In the code above we are going through each resource and builing
				 * up a series of buffers, which are transmitted when they are full. The following loop transmits any
				 * data that remains in any of the buffers because they did not reach capacity.
				 */
				for (FileBuffer buffer : groupToBuffer.values()) {
					if (buffer.getResourceCount() > 0) {
						transmitBuffer(
								theDataSink, url, chunkCount, buffer, lineCountOverallProgress, lineCountOverallTotal);
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

	private static int fetchAllFilesAndCountLines(String nextUrl, CloseableHttpClient httpClient) throws IOException {
		ourLog.info("Fetching URL (pass 1 for analysis): {}", nextUrl);
		int lineCountTotal = 0;
		try (CloseableHttpResponse response = httpClient.execute(new HttpGet(nextUrl))) {
			validateStatusCodeAndContentType(nextUrl, response);

			try (InputStream inputStream = response.getEntity().getContent()) {
				try (LineIterator lineIterator =
						new LineIterator(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
					while (lineIterator.hasNext()) {
						String next = lineIterator.next();
						if (isNotBlank(next)) {
							lineCountTotal++;
						}
					}
				}
			}
		}
		return lineCountTotal;
	}

	/**
	 * For a given compartment name (e.g. "Patient"), determines which compartment (if any)
	 * a given resource is in and returns a numbered compartment which is a hash-modulus
	 * of the compartment.
	 */
	@SuppressWarnings("unchecked")
	private String determineCompartmentGroup(String theCompartmentName, String theEncodedFile) {

		/*
		 * Instead of using HAPI's parser we just use a basic JSON parser for speed. This should be
		 * good enough for what we need to do here.
		 */
		Map<String, Object> parsed = JsonUtil.deserialize(theEncodedFile, Map.class);
		String resourceType = parsed.get("resourceType").toString();

		RuntimeResourceDefinition def = myFhirContext.getResourceDefinition(resourceType);
		for (RuntimeSearchParam param : def.getSearchParamsForCompartmentName(theCompartmentName)) {
			for (String path : param.getPathsSplit()) {
				String[] pathSegments = StringUtils.split(path, '.');
				if (pathSegments.length > 1) {
					String elementName = pathSegments[1];

					Object object = parsed.get(elementName);
					if (object instanceof List) {
						List<Object> list = (List<Object>) object;
						object = null;
						if (!list.isEmpty()) {
							object = list.get(0);
						}
					}

					if (object instanceof Map) {
						Map<String, Object> reference = (Map<String, Object>) object;
						String referenceVal = (String) reference.get("reference");
						if (referenceVal != null) {
							return Integer.toString(referenceVal.hashCode() % 100);
						}
					}
				}
			}
		}

		return "";
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

	private static class FileBuffer {

		private final StringBuilder myStringBuilder = new StringBuilder();
		private int myResourceCount;

		public void addResource(String theResource) {
			String file = theResource.trim();
			assert !file.contains("\n");

			myResourceCount++;
			myStringBuilder.append(file).append("\n");
		}

		public String toString() {
			return myStringBuilder.toString();
		}

		public int getFileSize() {
			return myStringBuilder.length();
		}

		public int getResourceCount() {
			return myResourceCount;
		}

		public void clear() {
			myStringBuilder.setLength(0);
			myResourceCount = 0;
		}
	}

	private static void validateStatusCodeAndContentType(String nextUrl, CloseableHttpResponse response) {
		int statusCode = response.getStatusLine().getStatusCode();
		String contentType = response.getEntity().getContentType().getValue();
		if (statusCode >= 400) {
			throw new JobExecutionFailedException(
					Msg.code(2056) + "Received HTTP " + statusCode + " from URL: " + nextUrl);
		}

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
	}

	private static void transmitBuffer(
			@javax.annotation.Nonnull IJobDataSink<NdJsonFileJson> theDataSink,
			String nextUrl,
			int chunkCount,
			FileBuffer buffer,
			int theLineCount,
			int theLineCountTotal) {

		if (theLineCountTotal > 0) {
			int linePercentage = (int) (100.0 * ((double) theLineCount / theLineCountTotal));
			ourLog.info(
					"Loaded chunk {} of {} NDJSON file (overall progress {}% line {} / {}) with {} resources from URL: {}",
					chunkCount,
					FileUtil.formatFileSize(buffer.getFileSize()),
					linePercentage,
					theLineCount,
					theLineCountTotal,
					buffer.getResourceCount(),
					nextUrl);
		}

		NdJsonFileJson data = new NdJsonFileJson();
		data.setNdJsonText(buffer.toString());
		data.setSourceName(nextUrl);
		data.setBatchLineCount(theLineCount);
		data.setBatchLineCountTotal(theLineCount);
		theDataSink.accept(data);

		buffer.clear();
	}

	private static boolean hasMatchingSubstring(String str, List<String> substrings) {
		return substrings.stream().anyMatch(str::contains);
	}

	private static String getContentTypesString() {
		return String.join(", ", ourValidContentTypes);
	}
}
