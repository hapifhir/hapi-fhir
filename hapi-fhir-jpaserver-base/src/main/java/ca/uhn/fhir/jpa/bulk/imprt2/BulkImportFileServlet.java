package ca.uhn.fhir.jpa.bulk.imprt2;

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8_CTSUFFIX;
import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON;
import static org.apache.commons.lang3.StringUtils.defaultString;

public class BulkImportFileServlet extends HttpServlet {

	public static final String INDEX_PARAM = "index";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportFileServlet.class);
	private final List<Supplier<Reader>> myFiles = new ArrayList<>();

	@Override
	protected void doGet(HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException, IOException {
		try {
			String servletPath = theRequest.getServletPath();
			String requestUri = theRequest.getRequestURI();
			String contextPath = theRequest.getContextPath();
			String requestPath = requestUri.substring(contextPath.length() + servletPath.length());

			if ("/download".equals(requestPath)) {
				handleDownload(theRequest, theResponse);
				return;
			}

			throw new ResourceNotFoundException("Invalid request path: " + requestPath);
		} catch (Exception e) {
			ourLog.warn("Failure serving file", e);
			int responseCode = 500;
			if (e instanceof BaseServerResponseException) {
				responseCode = ((BaseServerResponseException) e).getStatusCode();
			}

			theResponse.setStatus(responseCode);
			theResponse.addHeader(Constants.HEADER_CONTENT_TYPE, Constants.CT_TEXT);
			theResponse.getWriter().print(e.getMessage());
			theResponse.getWriter().close();
		}
	}

	private void handleDownload(HttpServletRequest theRequest, HttpServletResponse theResponse) throws ServletException, IOException {
		String indexParam = defaultString(theRequest.getParameter(INDEX_PARAM));
		int index;
		try {
			index = Integer.parseInt(indexParam);
		} catch (NumberFormatException e) {
			throw new ResourceNotFoundException("Missing or invalid index parameter");
		}

		if (index < 0 || index >= myFiles.size()) {
			throw new ResourceNotFoundException("Invalid index: " + index);
		}

		ourLog.info("Serving Bulk Import NDJSON file index: {}", index);

		theResponse.addHeader(Constants.HEADER_CONTENT_TYPE, CT_FHIR_NDJSON + CHARSET_UTF8_CTSUFFIX);

		Supplier<Reader> supplier = myFiles.get(index);
		try (Reader reader = supplier.get()) {
			IOUtils.copy(reader, theResponse.getWriter());
		}

	}

	public void clearFiles() {
		myFiles.clear();
	}

	public void registerFile(Supplier<Reader> theFile) {
		myFiles.add(theFile);
	}

}
