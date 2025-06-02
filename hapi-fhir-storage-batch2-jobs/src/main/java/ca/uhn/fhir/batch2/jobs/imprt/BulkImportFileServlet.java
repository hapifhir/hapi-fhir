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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.HeaderConstants;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.util.UrlUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.ReaderInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static ca.uhn.fhir.rest.api.Constants.CHARSET_UTF8_CTSUFFIX;
import static ca.uhn.fhir.rest.api.Constants.CT_FHIR_NDJSON;
import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.apache.commons.lang3.StringUtils.isBlank;

public class BulkImportFileServlet extends HttpServlet {

	public static final String INDEX_PARAM = "index";
	public static final String DEFAULT_HEADER_CONTENT_TYPE = CT_FHIR_NDJSON + CHARSET_UTF8_CTSUFFIX;
	private static final long serialVersionUID = 8302513561762436076L;
	private final Map<String, IFileSupplier> myFileIds = new HashMap<>();
	private Logger myLog = LoggerFactory.getLogger(BulkImportFileServlet.class);
	private String myBasicAuth;

	public BulkImportFileServlet() {}

	public BulkImportFileServlet(String theBasicAuthUsername, String theBasicAuthPassword) {
		setBasicAuth(theBasicAuthUsername, theBasicAuthPassword);
	}

	public void setLog(Logger theLog) {
		myLog = theLog;
	}

	public void setBasicAuth(String username, String password) {
		String auth = username + ":" + password;
		String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
		myBasicAuth = "Basic " + encodedAuth;
	}

	public void checkBasicAuthAndMaybeThrow403(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// Check if the myBasicAuth variable is set, ignore if not.
		if (myBasicAuth == null || myBasicAuth.isEmpty()) {
			return;
		}

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.equals(myBasicAuth)) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid authentication credentials.");
		}
	}

	@Override
	protected void doGet(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
		checkBasicAuthAndMaybeThrow403(theRequest, theResponse);
		try {
			String servletPath = theRequest.getServletPath();
			String requestUri = theRequest.getRequestURI();
			String contextPath = theRequest.getContextPath();
			String requestPath = requestUri.substring(contextPath.length() + servletPath.length());

			if ("/download".equals(requestPath)) {
				handleDownload(theRequest, theResponse);
				return;
			}

			throw new ResourceNotFoundException(Msg.code(2049) + "Invalid request path: " + requestPath);
		} catch (Exception e) {
			myLog.warn("Failure serving file", e);
			int responseCode = 500;
			if (e instanceof BaseServerResponseException) {
				responseCode = ((BaseServerResponseException) e).getStatusCode();
			}

			theResponse.setStatus(responseCode);
			theResponse.addHeader(HeaderConstants.CONTENT_TYPE, Constants.CT_TEXT);
			theResponse.getWriter().print("Failed to handle response. See server logs for details.");
			theResponse.getWriter().close();
		}
	}

	private void handleDownload(HttpServletRequest theRequest, HttpServletResponse theResponse) throws IOException {
		String indexParam = defaultString(theRequest.getParameter(INDEX_PARAM));
		if (isBlank(indexParam)) {
			throw new ResourceNotFoundException(Msg.code(2050) + "Missing or invalid index parameter");
		}
		if (!myFileIds.containsKey(indexParam)) {
			throw new ResourceNotFoundException(
					Msg.code(2051) + "Invalid index: " + UrlUtil.sanitizeUrlPart(indexParam));
		}

		myLog.info("Serving Bulk Import NDJSON file index: {}", indexParam);

		theResponse.addHeader(HeaderConstants.CONTENT_TYPE, getHeaderContentType());

		IFileSupplier supplier = myFileIds.get(indexParam);
		if (supplier.isGzip()) {
			theResponse.addHeader(HeaderConstants.CONTENT_ENCODING, Constants.ENCODING_GZIP);
		}

		if (myLog.isDebugEnabled()) {
			try (Reader reader = new InputStreamReader(supplier.openStream())) {
				String string = IOUtils.toString(reader);
				myLog.debug("file content: {}", string);
			}
		}

		try (InputStream reader = supplier.openStream()) {
			IOUtils.copy(reader, theResponse.getOutputStream());
		}
	}

	public String getHeaderContentType() {
		return DEFAULT_HEADER_CONTENT_TYPE;
	}

	public void clearFiles() {
		myFileIds.clear();
	}

	/**
	 * Registers a file, and returns the index descriptor
	 */
	public String registerFile(IFileSupplier theFile) {
		String indexPrefix = "";
		if (theFile.getName() != null) {
			indexPrefix = theFile.getName().replaceAll("^[^a-zA-Z0-9._-]", "") + "-";
		}

		String index = indexPrefix + UUID.randomUUID();
		myFileIds.put(index, theFile);
		return index;
	}

	/**
	 * Mostly intended for unit tests, registers a file
	 * using raw file contents.
	 */
	public String registerFileByContents(String theFileContents) {
		return registerFileByContents(theFileContents, null);
	}

	/**
	 * Registers a file using file contents as a String for service up to the bulk import job.
	 */
	public String registerFileByContents(String theFileContents, String theSourceName) {
		return registerFile(new IFileSupplier() {
			@Override
			public boolean isGzip() {
				return false;
			}

			@Override
			public InputStream openStream() throws IOException {
				return ReaderInputStream.builder()
						.setReader(new StringReader(theFileContents))
						.setCharset(StandardCharsets.UTF_8)
						.get();
			}

			@Nullable
			@Override
			public String getName() {
				return theSourceName;
			}
		});
	}

	/**
	 * Registers a file on disk for service up to the bulk import job.
	 */
	public String registerFile(File theFile) {
		return registerFile(new IFileSupplier() {
			@Override
			public boolean isGzip() {
				return theFile.getName().toLowerCase(Locale.ROOT).endsWith(".gz");
			}

			@Override
			public InputStream openStream() throws IOException {
				return new FileInputStream(theFile);
			}

			@Override
			public String getName() {
				return theFile.getName();
			}
		});
	}

	public interface IFileSupplier {

		boolean isGzip();

		InputStream openStream() throws IOException;

		// TODO: drop the default method once all implementations implement it
		@Nullable
		default String getName() {
			return null;
		}
	}
}
