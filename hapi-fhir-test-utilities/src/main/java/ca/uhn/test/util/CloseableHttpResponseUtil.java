package ca.uhn.test.util;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class CloseableHttpResponseUtil {

	/**
	 * Non instantiable
	 */
	private CloseableHttpResponseUtil() {
		super();
	}

	/**
	 * Parses an HTTP response to make it easier to work with for tests
	 */
	public static ParsedHttpResponse parse(CloseableHttpResponse response) throws IOException {
		Multimap<String, String> headers = MultimapBuilder.hashKeys().arrayListValues().build();

		for (Header header : response.getAllHeaders()) {
			headers.put(header.getName(), header.getValue());
		}

		String body = IOUtils.toString(response.getEntity().getContent(), StandardCharsets.UTF_8);
		return new ParsedHttpResponse(response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase(), headers, body);
	}

}
