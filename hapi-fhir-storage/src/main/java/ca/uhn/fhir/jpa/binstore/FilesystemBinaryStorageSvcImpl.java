/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.binstore;

import ca.uhn.fhir.context.ConfigurationException;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.binary.api.StoredDetails;
import ca.uhn.fhir.jpa.binary.svc.BaseBinaryStorageSvcImpl;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Charsets;
import com.google.common.hash.HashingInputStream;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.CountingInputStream;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IIdType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Date;

public class FilesystemBinaryStorageSvcImpl extends BaseBinaryStorageSvcImpl {

	private static final Logger ourLog = LoggerFactory.getLogger(FilesystemBinaryStorageSvcImpl.class);
	private final File myBasePath;
	private final ObjectMapper myJsonSerializer;

	public FilesystemBinaryStorageSvcImpl(String theBasePath) {
		Validate.notBlank(theBasePath);

		myBasePath = new File(theBasePath);

		myJsonSerializer = new ObjectMapper();
		myJsonSerializer.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		myJsonSerializer.enable(SerializationFeature.INDENT_OUTPUT);

		createBasePathDirectory();
	}

	private void createBasePathDirectory() {
		ourLog.info("Starting binary storage service with base path: {}", myBasePath);

		mkdir(myBasePath);
	}

	/**
	 * This implementation prevents: \ / | .
	 */
	@Override
	public boolean isValidBinaryContentId(String theNewBinaryContentId) {
		return !StringUtils.containsAny(theNewBinaryContentId, '\\', '/', '|', '.');
	}

	@Nonnull
	@Override
	public StoredDetails storeBinaryContent(
			IIdType theResourceId,
			String theBlobIdOrNull,
			String theContentType,
			InputStream theInputStream,
			RequestDetails theRequestDetails)
			throws IOException {

		String id = super.provideIdForNewBinaryContent(theBlobIdOrNull, null, theRequestDetails, theContentType);
		File storagePath = getStoragePath(id, true);

		// Write binary file
		File storageFilename = getStorageFilename(storagePath, theResourceId, id);
		ourLog.info("Writing to file: {}", storageFilename.getAbsolutePath());
		CountingInputStream countingInputStream = createCountingInputStream(theInputStream);
		HashingInputStream hashingInputStream = createHashingInputStream(countingInputStream);
		try (FileOutputStream outputStream = new FileOutputStream(storageFilename)) {
			IOUtils.copy(hashingInputStream, outputStream);
		}

		// Write descriptor file
		long count = countingInputStream.getByteCount();
		StoredDetails details = new StoredDetails(id, count, theContentType, hashingInputStream, new Date());
		File descriptorFilename = getDescriptorFilename(storagePath, theResourceId, id);
		ourLog.info("Writing to file: {}", descriptorFilename.getAbsolutePath());
		try (FileWriter writer = new FileWriter(descriptorFilename)) {
			myJsonSerializer.writeValue(writer, details);
		}

		ourLog.info(
				"Stored binary blob with {} bytes and ContentType {} for resource {}",
				count,
				theContentType,
				theResourceId);

		return details;
	}

	@Override
	public StoredDetails fetchBinaryContentDetails(IIdType theResourceId, String theBlobId) throws IOException {
		StoredDetails retVal = null;

		File storagePath = getStoragePath(theBlobId, false);
		if (storagePath != null) {
			File file = getDescriptorFilename(storagePath, theResourceId, theBlobId);
			if (file.exists()) {
				try (InputStream inputStream = new FileInputStream(file)) {
					try (Reader reader = new InputStreamReader(inputStream, Charsets.UTF_8)) {
						retVal = myJsonSerializer.readValue(reader, StoredDetails.class);
					}
				}
			}
		}

		return retVal;
	}

	@Override
	public boolean writeBinaryContent(IIdType theResourceId, String theBlobId, OutputStream theOutputStream)
			throws IOException {
		InputStream inputStream = getInputStream(theResourceId, theBlobId);

		if (inputStream != null) {
			try (inputStream) {
				IOUtils.copy(inputStream, theOutputStream);
				theOutputStream.close();
			}
		}

		return false;
	}

	@Nullable
	private InputStream getInputStream(IIdType theResourceId, String theBlobId) throws FileNotFoundException {
		File storagePath = getStoragePath(theBlobId, false);
		InputStream inputStream = null;
		if (storagePath != null) {
			File file = getStorageFilename(storagePath, theResourceId, theBlobId);
			if (file.exists()) {
				inputStream = new FileInputStream(file);
			}
		}
		return inputStream;
	}

	@Override
	public void expungeBinaryContent(IIdType theResourceId, String theBlobId) {
		File storagePath = getStoragePath(theBlobId, false);
		if (storagePath != null) {
			File storageFile = getStorageFilename(storagePath, theResourceId, theBlobId);
			if (storageFile.exists()) {
				delete(storageFile, theBlobId);
			}
			File descriptorFile = getDescriptorFilename(storagePath, theResourceId, theBlobId);
			if (descriptorFile.exists()) {
				delete(descriptorFile, theBlobId);
			}
		}
	}

	@Override
	public byte[] fetchBinaryContent(IIdType theResourceId, String theBlobId) throws IOException {
		StoredDetails details = fetchBinaryContentDetails(theResourceId, theBlobId);
		try (InputStream inputStream = getInputStream(theResourceId, theBlobId)) {

			if (inputStream != null) {
				return IOUtils.toByteArray(inputStream, details.getBytes());
			}
		}

		throw new ResourceNotFoundException(
				Msg.code(1327) + "Unknown blob ID: " + theBlobId + " for resource ID " + theResourceId);
	}

	private void delete(File theStorageFile, String theBlobId) {
		Validate.isTrue(theStorageFile.delete(), "Failed to delete file for blob %s", theBlobId);
	}

	@Nonnull
	private File getDescriptorFilename(File theStoragePath, IIdType theResourceId, String theId) {
		return getStorageFilename(theStoragePath, theResourceId, theId, ".json");
	}

	@Nonnull
	private File getStorageFilename(File theStoragePath, IIdType theResourceId, String theId) {
		return getStorageFilename(theStoragePath, theResourceId, theId, ".bin");
	}

	private File getStorageFilename(File theStoragePath, IIdType theResourceId, String theId, String theExtension) {
		Validate.notBlank(theResourceId.getResourceType());
		Validate.notBlank(theResourceId.getIdPart());

		String filename = theResourceId.getResourceType() + "_" + theResourceId.getIdPart() + "_" + theId;
		return new File(theStoragePath, filename + theExtension);
	}

	private File getStoragePath(String theId, boolean theCreate) {
		File path = myBasePath;
		for (int i = 0; i < 10; i++) {
			path = new File(path, theId.substring(i, i + 1));
			if (!path.exists()) {
				if (theCreate) {
					mkdir(path);
				} else {
					return null;
				}
			}
		}
		return path;
	}

	private void mkdir(File theBasePath) {
		try {
			FileUtils.forceMkdir(theBasePath);
		} catch (IOException e) {
			throw new ConfigurationException(Msg.code(1328) + "Unable to create path " + myBasePath + ": " + e);
		}
	}
}
