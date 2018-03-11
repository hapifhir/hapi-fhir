package ca.uhn.fhir.jpa.term;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCollectionBuilder {

	private static final Logger ourLog = LoggerFactory.getLogger(ZipCollectionBuilder.class);
	private final ArrayList<byte[]> myFiles;

	public ZipCollectionBuilder() {
		myFiles = new ArrayList<>();
	}

	public void addFile(String theClasspathPrefix, String theClasspathFileName) throws IOException {
		addFile(theClasspathPrefix, theClasspathFileName, theClasspathFileName);
	}

	public void addFile(String theClasspathPrefix, String theClasspathFileName, String theOutputFilename) throws IOException {
		ByteArrayOutputStream bos;
		bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		ourLog.info("Adding {} to test zip", theClasspathFileName);
		zos.putNextEntry(new ZipEntry("SnomedCT_Release_INT_20160131_Full/Terminology/" + theOutputFilename));
		String classpathName = theClasspathPrefix + theClasspathFileName;
		InputStream stream = getClass().getResourceAsStream(classpathName);
		Validate.notNull(stream, "Couldn't load " + classpathName);
		byte[] byteArray = IOUtils.toByteArray(stream);
		Validate.notNull(byteArray);
		zos.write(byteArray);
		zos.closeEntry();
		zos.close();
		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);
		myFiles.add(bos.toByteArray());
	}

	public ArrayList<byte[]> getFiles() {
		return myFiles;
	}

}
