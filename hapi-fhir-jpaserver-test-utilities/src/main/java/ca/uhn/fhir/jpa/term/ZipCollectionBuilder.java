/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.util.ClasspathUtil;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCollectionBuilder {

	public static final String ZIP_ENTRY_PREFIX = "SnomedCT_Release_INT_20160131_Full/Terminology/";

	private static final Logger ourLog = LoggerFactory.getLogger(ZipCollectionBuilder.class);
	private final ArrayList<ITermLoaderSvc.FileDescriptor> myFiles;
	private final ByteArrayOutputStream mySingleZipBytes;
	private final ZipOutputStream mySingleZipStream;

	/**
	 * Constructor
	 */
	public ZipCollectionBuilder() {
		this(false);
	}

	public ZipCollectionBuilder(boolean theSingleZipFile) {
		if (theSingleZipFile) {
			myFiles = null;
			mySingleZipBytes = new ByteArrayOutputStream();
			mySingleZipStream = new ZipOutputStream(mySingleZipBytes);
		} else {
			myFiles = new ArrayList<>();
			mySingleZipBytes = null;
			mySingleZipStream = null;
		}
	}

	public ZipCollectionBuilder(byte[] theZipBytes) throws IOException {
		mySingleZipBytes = new ByteArrayOutputStream();
		mySingleZipBytes.write(theZipBytes);
		myFiles = null;
		mySingleZipStream = null;
	}

	/**
	 * Add file as an entry inside a ZIP file
	 */
	public void addFileZip(String theClasspathPrefix, String theClasspathFileName) throws IOException {
		addFileZip(theClasspathPrefix, theClasspathFileName, theClasspathFileName);
	}

	public void addFileZip(String theClasspathPrefix, String theClasspathFileName, String theOutputFilename)
			throws IOException {
		byte[] bytes = readFile(theClasspathPrefix, theClasspathFileName);
		addBytes(theClasspathFileName, theOutputFilename, bytes);
	}

	public void addPropertiesZip(Properties properties, String theOutputFilename) throws IOException {
		byte[] bytes = getPropertiesBytes(properties);
		addBytes(theOutputFilename, theOutputFilename, bytes);
	}

	private void addBytes(String theClasspathFileName, String theOutputFilename, byte[] theBytes) throws IOException {
		if (mySingleZipStream != null) {
			mySingleZipStream.putNextEntry(new ZipEntry(ZIP_ENTRY_PREFIX + theOutputFilename));
			mySingleZipStream.write(theBytes);
			mySingleZipStream.closeEntry();
		} else {
			myFiles.add(new ITermLoaderSvc.FileDescriptor() {
				@Override
				public String getFilename() {
					return theOutputFilename;
				}

				@Override
				public InputStream getInputStream() {
					return new ByteArrayInputStream(theBytes);
				}
			});
		}
	}

	private byte[] getPropertiesBytes(Properties theProperties) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		theProperties.store(byteArrayOutputStream, "");
		return byteArrayOutputStream.toByteArray();
	}

	private byte[] readFile(String theClasspathPrefix, String theClasspathFileName) {
		String classpathName = theClasspathPrefix + theClasspathFileName;
		return ClasspathUtil.loadResourceAsByteArray(classpathName);
	}

	public List<ITermLoaderSvc.FileDescriptor> getFiles() {
		return myFiles;
	}

	public void addFileText(String theText, String theFilename) throws IOException {
		if (mySingleZipStream != null) {
			mySingleZipStream.putNextEntry(new ZipEntry(ZIP_ENTRY_PREFIX + theFilename));
			mySingleZipStream.write(theText.getBytes(Charsets.UTF_8));
			mySingleZipStream.closeEntry();
		} else {
			myFiles.add(new ITermLoaderSvc.FileDescriptor() {
				@Override
				public String getFilename() {
					return theFilename;
				}

				@Override
				public InputStream getInputStream() {
					return new ByteArrayInputStream(theText.getBytes(Charsets.UTF_8));
				}
			});
		}
	}

	public boolean isSingleZip() {
		return mySingleZipBytes != null;
	}

	public byte[] getZipBytes() {
		assert mySingleZipBytes != null;
		IOUtils.closeQuietly(mySingleZipStream);
		IOUtils.closeQuietly(mySingleZipBytes);
		return mySingleZipBytes.toByteArray();
	}

}
