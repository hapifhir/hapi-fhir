package ca.uhn.fhir.test.utilities.server;

import ca.uhn.fhir.rest.client.api.Header;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class RequestCaptureServlet extends HttpServlet {

	public int ourResponseCount = 0;
	public String[] ourResponseBodies;
	public String ourResponseBody;
	public String ourResponseContentType;
	public int ourResponseStatus;
	public String ourRequestUri;
	public List<String> ourRequestUriAll;
	public String ourRequestMethod;
	public String ourRequestContentType;
	public byte[] ourRequestBodyBytes;
	public String ourRequestBodyString;
	public ArrayListMultimap<String, Header> ourRequestHeaders;
	public List<ArrayListMultimap<String, Header>> ourRequestHeadersAll;
	public Map<String, Header> ourRequestFirstHeaders;

	@Override
	protected void service(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
		ourRequestUri = theRequest.getRequestURL().toString();
		if (isNotBlank(theRequest.getQueryString())) {
			ourRequestUri += "?" + theRequest.getQueryString();
		}
		ourRequestUriAll.add(ourRequestUri);
		ourRequestMethod = theRequest.getMethod();
		ourRequestContentType = theRequest.getContentType();
		ourRequestBodyBytes = IOUtils.toByteArray(theRequest.getInputStream());
		ourRequestBodyString = new String(ourRequestBodyBytes, StandardCharsets.UTF_8);

		ourRequestHeaders = ArrayListMultimap.create();
		ourRequestHeadersAll.add(ourRequestHeaders);
		ourRequestFirstHeaders = Maps.newHashMap();

		for (Enumeration<String> headerNameEnum = theRequest.getHeaderNames(); headerNameEnum.hasMoreElements(); ) {
			String nextName = headerNameEnum.nextElement();
			for (Enumeration<String> headerValueEnum = theRequest.getHeaders(nextName); headerValueEnum.hasMoreElements(); ) {
				String nextValue = headerValueEnum.nextElement();
				if (ourRequestFirstHeaders.containsKey(nextName) == false) {
					ourRequestFirstHeaders.put(nextName, new Header(nextName, nextValue));
				}
				ourRequestHeaders.put(nextName, new Header(nextName, nextValue));
			}
		}

		theResponse.setStatus(ourResponseStatus);

		if (ourResponseBody != null) {
			theResponse.setContentType(ourResponseContentType);
			theResponse.getWriter().write(ourResponseBody);
		} else if (ourResponseBodies != null) {
			theResponse.setContentType(ourResponseContentType);
			theResponse.getWriter().write(ourResponseBodies[ourResponseCount]);
		}

		ourResponseCount++;
	}

	public void reset() {
		ourResponseCount = 0;
		ourRequestUri = null;
		ourRequestUriAll = Lists.newArrayList();
		ourResponseStatus = 200;
		ourResponseBody = null;
		ourResponseBodies = null;

		ourResponseContentType = null;
		ourRequestContentType = null;
		ourRequestBodyBytes = null;
		ourRequestBodyString = null;
		ourRequestHeaders = null;
		ourRequestFirstHeaders = null;
		ourRequestMethod = null;
		ourRequestHeadersAll = Lists.newArrayList();
	}
}
