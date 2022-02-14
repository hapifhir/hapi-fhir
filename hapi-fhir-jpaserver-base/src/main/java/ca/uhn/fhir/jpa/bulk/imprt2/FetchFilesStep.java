package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.client.impl.HttpBasicAuthInterceptor;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.FileUtil;
import ca.uhn.fhir.util.StopWatch;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FetchFilesStep implements IJobStepWorker {
	public static final String KEY_NDJSON = "ndjson";
	public static final String KEY_SOURCE_NAME = "sourceName";
	private static final Logger ourLog = LoggerFactory.getLogger(FetchFilesStep.class);

	@Override
	public RunOutcome run(StepExecutionDetails theStepExecutionDetails, IJobDataSink theDataSink) {

		int batchSizeLines = theStepExecutionDetails
			.getParameterValueInteger(BulkImport2AppCtx.PARAM_MAXIMUM_BATCH_SIZE)
			.orElse(BulkImport2AppCtx.PARAM_MAXIMUM_BATCH_SIZE_DEFAULT);

		try (CloseableHttpClient httpClient = newHttpClient(theStepExecutionDetails)) {

			StopWatch outerSw = new StopWatch();
			List<String> urls = theStepExecutionDetails.getParameterValues(BulkImport2AppCtx.PARAM_NDJSON_URL);

			for (String nextUrl : urls) {

				ourLog.info("Fetching URL: {}", nextUrl);
				StopWatch urlSw = new StopWatch();

				try (CloseableHttpResponse response = httpClient.execute(new HttpGet(nextUrl))) {
					Validate.isTrue(response.getStatusLine().getStatusCode() == 200, "Received HTTP %d response from URL: %s", response.getStatusLine().getStatusCode(), nextUrl);

					String contentType = response.getEntity().getContentType().getValue();
					EncodingEnum encoding = EncodingEnum.forContentType(contentType);
					Validate.isTrue(encoding == EncodingEnum.NDJSON, "Received non-NDJSON content type \"%s\" from URL: %s", contentType, nextUrl);

					try (InputStream inputStream = response.getEntity().getContent()) {
						LineIterator lineIterator = new LineIterator(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

						int chunkCount = 0;
						int lineCount = 0;
						StringBuilder builder = new StringBuilder();

						while (lineIterator.hasNext()) {

							String nextLine = lineIterator.nextLine();
							builder.append(nextLine).append('\n');

							lineCount++;
							int charCount = builder.length();
							int batchSizeChars = (int) (20 * FileUtils.ONE_MB);
							if (lineCount >= batchSizeLines || charCount >= batchSizeChars || !lineIterator.hasNext()) {

								ourLog.info("Loaded chunk {} of {} NDJSON file with {} resources from URL: {}", chunkCount, FileUtil.formatFileSize(charCount), lineCount, nextUrl);
								Map<String, Object> data = new HashMap<>();
								data.put(KEY_NDJSON, builder.toString());
								data.put(KEY_SOURCE_NAME, nextUrl);
								theDataSink.accept(data);

								builder.setLength(0);
								lineCount = 0;
								chunkCount++;
							}

						}


					}
				}

				ourLog.info("Loaded and processed URL in {}", urlSw);

			}

			ourLog.info("Loaded and processed {} URLs in {}", urls.size(), outerSw);

			return new RunOutcome(0);

		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
	}

	private CloseableHttpClient newHttpClient(StepExecutionDetails theStepExecutionDetails) {
		HttpClientBuilder builder = HttpClientBuilder.create();

		Optional<String> httpBasicCredentials = theStepExecutionDetails.getParameterValue(BulkImport2AppCtx.PARAM_HTTP_BASIC_CREDENTIALS);
		if (httpBasicCredentials.isPresent()) {
			String credentials = httpBasicCredentials.get();
			int colonIdx = credentials.indexOf(':');
			if (colonIdx == -1) {
				throw new JobExecutionFailedException("Invalid credential parameter provided. Must be in the form \"username:password\".");
			}
			String username = credentials.substring(0, colonIdx);
			String password = credentials.substring(colonIdx + 1);
			builder.addInterceptorFirst(new HttpBasicAuthInterceptor(username, password));
		}

		return builder.build();
	}

}
