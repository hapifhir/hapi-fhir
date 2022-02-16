package ca.uhn.fhir.batch2.jobs.imprt;

/*-
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.UrlUtil;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8_CTSUFFIX;
import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class BulkImportFileServlet extends HttpServlet {

	private static final long serialVersionUID = 8302513561762436076L;
	public static final String INDEX_PARAM = "index";
	private static final Logger ourLog = LoggerFactory.getLogger(BulkImportFileServlet.class);
	private final Map<String, IFileSupplier> myFileIds = new HashMap<>();

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
		if (isBlank(indexParam)) {
			throw new ResourceNotFoundException("Missing or invalid index parameter");
		}
		if (!myFileIds.containsKey(indexParam)) {
			throw new ResourceNotFoundException("Invalid index: " + UrlUtil.sanitizeUrlPart(indexParam));
		}

		ourLog.info("Serving Bulk Import NDJSON file index: {}", indexParam);

		theResponse.addHeader(Constants.HEADER_CONTENT_TYPE, CT_FHIR_NDJSON + CHARSET_UTF8_CTSUFFIX);

		IFileSupplier supplier = myFileIds.get(indexParam);
		try (Reader reader = supplier.get()) {
			IOUtils.copy(reader, theResponse.getWriter());
		}

	}

	public void clearFiles() {
		myFileIds.clear();
	}

	/**
	 * Registers a file, and returns the index descriptor
	 */
	public String registerFile(IFileSupplier theFile) {
		String index = UUID.randomUUID().toString();
		myFileIds.put(index, theFile);
		return index;
	}

	@FunctionalInterface
	public interface IFileSupplier {

		Reader get() throws IOException;

	}


}
