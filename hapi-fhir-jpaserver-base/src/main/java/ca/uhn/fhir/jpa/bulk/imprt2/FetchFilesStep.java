package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IJobStepWorker;
import ca.uhn.fhir.batch2.api.StepExecutionDetails;
import ca.uhn.fhir.rest.api.EncodingEnum;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.FileUtil;
import com.github.dnault.xmlpatch.repackaged.org.apache.commons.io.IOUtils;
import com.github.jsonldjava.shaded.com.google.common.base.Charsets;
import org.apache.commons.lang3.Validate;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FetchFilesStep implements IJobStepWorker {
	public static final String KEY_NDJSON = "ndjson";
	private static final Logger ourLog = LoggerFactory.getLogger(FetchFilesStep.class);
	public static final String KEY_SOURCE_NAME = "sourceName";

	@Override
	public void run(StepExecutionDetails theStepExecutionDetails, IJobDataSink theDataSink) {
		try (CloseableHttpClient httpClient = newHttpClient()) {

			List<String> urls = theStepExecutionDetails.getParameterValues(BulkImport2AppCtx.PARAM_NDJSON_URL);
			for (String nextUrl : urls) {

				ourLog.info("Fetching URL: {}", nextUrl);
				String content;
				try (CloseableHttpResponse response = httpClient.execute(new HttpGet(nextUrl))) {
					Validate.isTrue(response.getStatusLine().getStatusCode() == 200, "Received HTTP %d response from URL: %s", response.getStatusLine().getStatusCode(), nextUrl);

					String contentType = response.getEntity().getContentType().getValue();
					EncodingEnum encoding = EncodingEnum.forContentType(contentType);
					Validate.isTrue(encoding == EncodingEnum.NDJSON, "Received non-NDJSON content type \"%s\" from URL: %s", contentType, nextUrl);

					content = IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
				}

				ourLog.info("Loaded {} NDJSON file from URL: {}", FileUtil.formatFileSize(content.length()), nextUrl);

				Map<String, Object> data = new HashMap<>();
				data.put(KEY_NDJSON, content);
				data.put(KEY_SOURCE_NAME, nextUrl);
				theDataSink.accept(data);

			}

		} catch (IOException e) {
			throw new InternalErrorException(e);
		}
	}

	private CloseableHttpClient newHttpClient() {
		return HttpClientBuilder.create().build();
	}

}
