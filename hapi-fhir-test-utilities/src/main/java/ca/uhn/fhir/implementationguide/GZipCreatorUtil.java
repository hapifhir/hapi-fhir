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
