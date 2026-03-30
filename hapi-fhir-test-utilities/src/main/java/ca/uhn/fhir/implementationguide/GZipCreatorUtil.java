/*-
 * #%L
 * HAPI FHIR Test Utilities
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
package ca.uhn.fhir.implementationguide;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class GZipCreatorUtil {

	/**
	 * Create a tarball of the provided input and saves it to the provided output.
	 *
	 * @param theSource - the input path to the directory containing all source files to be zipped
	 * @param theOutput - the output path to the gzip file.
	 */
	public static void createTarGz(Path theSource, Path theOutput) throws IOException {
		try (OutputStream fos = Files.newOutputStream(theOutput);
				BufferedOutputStream bos = new BufferedOutputStream(fos);
				GzipCompressorOutputStream gzos = new GzipCompressorOutputStream(bos);
				TarArchiveOutputStream taos = new TarArchiveOutputStream(gzos)) {
			addFilesToTarGz(theSource, "", taos);
		}
	}

	private static void addFilesToTarGz(Path thePath, String theParent, TarArchiveOutputStream theTarballOutputStream)
			throws IOException {
		String entryName = theParent + thePath.getFileName().toString();
		TarArchiveEntry entry = new TarArchiveEntry(thePath.toFile(), entryName);
		theTarballOutputStream.putArchiveEntry(entry);

		if (Files.isRegularFile(thePath)) {
			// add file
			try (InputStream fis = Files.newInputStream(thePath)) {
				byte[] buffer = new byte[1024];
				int len;
				while ((len = fis.read(buffer)) > 0) {
					theTarballOutputStream.write(buffer, 0, len);
				}
			}
			theTarballOutputStream.closeArchiveEntry();
		} else {
			theTarballOutputStream.closeArchiveEntry();
			// walk directory
			try (DirectoryStream<Path> stream = Files.newDirectoryStream(thePath)) {
				for (Path child : stream) {
					addFilesToTarGz(child, entryName + "/", theTarballOutputStream);
				}
			}
		}
	}
}
