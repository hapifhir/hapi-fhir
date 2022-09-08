package ca.uhn.fhir.jpa.packages;

import ca.uhn.fhir.rest.api.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class FakeNpmServlet extends HttpServlet {
	private static final Logger ourLog = LoggerFactory.getLogger(FakeNpmServlet.class);

	final Map<String, byte[]> responses = new HashMap<>();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String requestUrl = req.getRequestURI();
		if (responses.containsKey(requestUrl)) {
			ourLog.info("Responding to request: {}", requestUrl);

			resp.setStatus(200);

			if (StringUtils.countMatches(requestUrl, "/") == 1) {
				resp.setHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_JSON);
			} else {
				resp.setHeader(Constants.HEADER_CONTENT_TYPE, "application/gzip");
			}
			resp.getOutputStream().write(responses.get(requestUrl));
			resp.getOutputStream().close();
		} else {
			ourLog.warn("Unknown request: {}", requestUrl);

			resp.sendError(404);
		}

	}

	public Map<String, byte[]> getResponses() {
		return responses;
	}
}
