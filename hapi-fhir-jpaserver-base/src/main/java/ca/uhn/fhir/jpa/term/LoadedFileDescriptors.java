package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LoadedFileDescriptors implements Closeable {
	private static final Logger ourLog = LoggerFactory.getLogger(LoadedFileDescriptors.class);

	private List<File> myTemporaryFiles = new ArrayList<>();
	private List<ITermLoaderSvc.FileDescriptor> myUncompressedFileDescriptors = new ArrayList<>();

	public LoadedFileDescriptors(List<ITermLoaderSvc.FileDescriptor> theFileDescriptors) {
		try {
			for (ITermLoaderSvc.FileDescriptor next : theFileDescriptors) {
				if (next.getFilename().toLowerCase().endsWith(".zip")) {
					ourLog.info("Uncompressing {} into temporary files", next.getFilename());
					try (InputStream inputStream = next.getInputStream()) {
						ZipInputStream zis = new ZipInputStream(new BufferedInputStream(inputStream));
						for (ZipEntry nextEntry; (nextEntry = zis.getNextEntry()) != null; ) {
							BOMInputStream fis = new BOMInputStream(zis);
							File nextTemporaryFile = File.createTempFile("hapifhir", ".tmp");
							ourLog.info("Creating temporary file: {}", nextTemporaryFile.getAbsolutePath());
							nextTemporaryFile.deleteOnExit();
							FileOutputStream fos = new FileOutputStream(nextTemporaryFile, false);
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
										throw new InternalErrorException(e);
									}
								}
							});
							myTemporaryFiles.add(nextTemporaryFile);
						}
					}
				} else {
					myUncompressedFileDescriptors.add(next);
				}

			}
		} catch (Exception e) {
			close();
			throw new InternalErrorException(e);
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

	public void verifyMandatoryFilesExist(List<String> theExpectedFilenameFragments) {
		List<String> notFound = notFound(theExpectedFilenameFragments);
		if (!notFound.isEmpty()) {
			throw new UnprocessableEntityException("Could not find the following mandatory files in input: " + notFound);
		}
	}

	public void verifyOptionalFilesExist(List<String> theExpectedFilenameFragments) {
		List<String> notFound = notFound(theExpectedFilenameFragments);
		if (!notFound.isEmpty()) {
			ourLog.warn("Could not find the following optional files: " + notFound);
		}
	}


}
