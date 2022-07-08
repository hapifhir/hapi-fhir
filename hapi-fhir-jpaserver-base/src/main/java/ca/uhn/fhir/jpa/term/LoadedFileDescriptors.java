package ca.uhn.fhir.jpa.term;

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

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.jpa.util.LogicUtil;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LoadedFileDescriptors implements Closeable {
	private static final Logger ourLog = LoggerFactory.getLogger(LoadedFileDescriptors.class);
	private List<File> myTemporaryFiles = new ArrayList<>();
	private List<ITermLoaderSvc.FileDescriptor> myUncompressedFileDescriptors = new ArrayList<>();

	LoadedFileDescriptors(List<ITermLoaderSvc.FileDescriptor> theFileDescriptors) {
		try {
			for (ITermLoaderSvc.FileDescriptor next : theFileDescriptors) {
				if (next.getFilename().toLowerCase().endsWith(".zip")) {
					ourLog.info("Uncompressing {} into temporary files", next.getFilename());
					try (InputStream inputStream = next.getInputStream()) {
						try (BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {
							try (ZipInputStream zis = new ZipInputStream(bufferedInputStream)) {
								for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null; ) {
									try (BOMInputStream fis = new NonClosableBOMInputStream(zis)) {
										File nextTemporaryFile = File.createTempFile("hapifhir", ".tmp");
										ourLog.info("Creating temporary file: {}", nextTemporaryFile.getAbsolutePath());
										nextTemporaryFile.deleteOnExit();
										try (FileOutputStream fos = new FileOutputStream(nextTemporaryFile, false)) {
											IOUtils.copy(fis, fos);
											String nextEntryFileName = nextEntry.getName();
											myUncompressedFileDescriptors.add(new ITermLoaderSvc.FileDescriptor() {
												@Override
												public String getFilename() {
													return nextEntryFileName;
												}

												@Override
												public InputStream getInputStream() {
													try {
														return new FileInputStream(nextTemporaryFile);
													} catch (FileNotFoundException e) {
														throw new InternalErrorException(Msg.code(860) + e);
													}
												}
											});
											myTemporaryFiles.add(nextTemporaryFile);
										}
									}
								}
							}
						}
					}
				} else {
					myUncompressedFileDescriptors.add(next);
				}

			}
		} catch (IOException e) {
			throw new InternalErrorException(Msg.code(861) + e);
		}

	}

	public boolean hasFile(String theFilename) {
		return myUncompressedFileDescriptors
			.stream()
			.map(t -> t.getFilename().replaceAll(".*[\\\\/]", "")) // Strip the path from the filename
			.anyMatch(t -> t.equals(theFilename));
	}

	@Override
	public void close() {
		for (File next : myTemporaryFiles) {
			ourLog.info("Deleting temporary file: {}", next.getAbsolutePath());
			FileUtils.deleteQuietly(next);
		}
	}

	List<ITermLoaderSvc.FileDescriptor> getUncompressedFileDescriptors() {
		return myUncompressedFileDescriptors;
	}

	private List<String> notFound(List<String> theExpectedFilenameFragments) {
		Set<String> foundFragments = new HashSet<>();
		for (String nextExpected : theExpectedFilenameFragments) {
			for (ITermLoaderSvc.FileDescriptor next : myUncompressedFileDescriptors) {
				if (next.getFilename().contains(nextExpected)) {
					foundFragments.add(nextExpected);
					break;
				}
			}
		}

		ArrayList<String> notFoundFileNameFragments = new ArrayList<>(theExpectedFilenameFragments);
		notFoundFileNameFragments.removeAll(foundFragments);
		return notFoundFileNameFragments;
	}

	void verifyMandatoryFilesExist(List<String> theExpectedFilenameFragments) {
		List<String> notFound = notFound(theExpectedFilenameFragments);
		if (!notFound.isEmpty()) {
			throw new UnprocessableEntityException(Msg.code(862) + "Could not find the following mandatory files in input: " + notFound);
		}
	}

	void verifyOptionalFilesExist(List<String> theExpectedFilenameFragments) {
		List<String> notFound = notFound(theExpectedFilenameFragments);
		if (!notFound.isEmpty()) {
			ourLog.warn("Could not find the following optional files: " + notFound);
		}
	}

	void verifyPartLinkFilesExist(List<String> theMultiPartLinkFiles, String theSinglePartLinkFile) {
		List<String> notFoundMulti = notFound(theMultiPartLinkFiles);
		List<String> notFoundSingle = notFound(Arrays.asList(theSinglePartLinkFile));
		// Expect all of the files in theMultiPartLinkFiles to be found and theSinglePartLinkFile to not be found,
		// or none of the files in theMultiPartLinkFiles to be found and the SinglePartLinkFile to be found.
		boolean multiPartFilesFound = notFoundMulti.isEmpty();
		boolean singlePartFilesFound = notFoundSingle.isEmpty();
		if (!LogicUtil.multiXor(multiPartFilesFound, singlePartFilesFound)) {
			String msg;
			if (!multiPartFilesFound && !singlePartFilesFound) {
				msg = "Could not find any of the PartLink files: " + notFoundMulti + " nor " + notFoundSingle;
			} else {
				msg = "Only either the single PartLink file or the split PartLink files can be present. Found both the single PartLink file, " + theSinglePartLinkFile + ", and the split PartLink files: " + theMultiPartLinkFiles;
			}
			throw new UnprocessableEntityException(Msg.code(863) + msg);
		}
	}


	private static class NonClosableBOMInputStream extends BOMInputStream {
		NonClosableBOMInputStream(InputStream theWrap) {
			super(theWrap);
		}

		@Override
		public void close() {
			// nothing
		}
	}
}
