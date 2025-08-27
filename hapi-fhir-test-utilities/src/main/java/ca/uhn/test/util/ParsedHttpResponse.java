package ca.uhn.test.util;

import com.google.common.collect.Multimap;

import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public record ParsedHttpResponse(int statusCode, String statusReason, Multimap<String, String> headers, String body) {

	@Override
	public String toString() {
		StringBuilder logBuilder = new StringBuilder();

		// Log Status Line
		logBuilder.append(statusCode())
			.append(" ").append(statusReason()).append("\n");

		// Log Headers
		for (Map.Entry<String, String> header : headers.entries()) {
			logBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\n");
		}

		logBuilder.append("\n");

		if (isNotBlank(body)) {
			logBuilder.append(body);
		}

		return logBuilder.toString();
	}
}
