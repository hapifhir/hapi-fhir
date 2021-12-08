package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.jpa.term.api.ITermLoaderSvc;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.Validate;
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

	/**
	 * Constructor
	 */
	public ZipCollectionBuilder() {
		myFiles = new ArrayList<>();
	}

	/**
	 * Add file as a raw file
	 */
	public void addFilePlain(String theClasspathPrefix, String theClasspathFileName) throws IOException {
		byte[] file = readFile(theClasspathPrefix, theClasspathFileName);
		myFiles.add(new ITermLoaderSvc.FileDescriptor() {
			@Override
			public String getFilename() {
				return theClasspathFileName;
			}

			@Override
			public InputStream getInputStream() {
				return new ByteArrayInputStream(file);
			}
		});
	}

	/**
	 * Add file as an entry inside a ZIP file
	 */
	public void addFileZip(String theClasspathPrefix, String theClasspathFileName) throws IOException {
		addFileZip(theClasspathPrefix, theClasspathFileName, theClasspathFileName);
	}

	public void addFileZip(String theClasspathPrefix, String theClasspathFileName, String theOutputFilename) throws IOException {
		ByteArrayOutputStream bos;
		bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		ourLog.info("Adding {} to test zip", theClasspathFileName);
		zos.putNextEntry(new ZipEntry(ZIP_ENTRY_PREFIX + theOutputFilename));
		zos.write(readFile(theClasspathPrefix, theClasspathFileName));
		zos.closeEntry();
		zos.close();
		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);
		myFiles.add(new ITermLoaderSvc.FileDescriptor() {
			@Override
			public String getFilename() {
				return "AAA.zip";
			}

			@Override
			public InputStream getInputStream() {
				return new ByteArrayInputStream(bos.toByteArray());
			}
		});
	}

	public void addPropertiesZip(Properties properties, String theOutputFilename) throws IOException {

		ByteArrayOutputStream bos;
		bos = new ByteArrayOutputStream();
		ZipOutputStream zos = new ZipOutputStream(bos);
		ourLog.info("Adding properties to test zip");
		zos.putNextEntry(new ZipEntry(ZIP_ENTRY_PREFIX + theOutputFilename));
		zos.write(getPropertiesBytes(properties));
		zos.closeEntry();
		zos.close();
		ourLog.info("ZIP file has {} bytes", bos.toByteArray().length);
		myFiles.add(new ITermLoaderSvc.FileDescriptor() {
			@Override
			public String getFilename() {
				return "AAA.zip";
			}

			@Override
			public InputStream getInputStream() {
				return new ByteArrayInputStream(bos.toByteArray());
			}
		});
	}

	private byte[] getPropertiesBytes(Properties theProperties) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		theProperties.store(byteArrayOutputStream, "");
		return byteArrayOutputStream.toByteArray();
	}

	private byte[] readFile(String theClasspathPrefix, String theClasspathFileName) throws IOException {
		String classpathName = theClasspathPrefix + theClasspathFileName;
		InputStream stream = getClass().getResourceAsStream(classpathName);
		Validate.notNull(stream, "Couldn't load " + classpathName);
		byte[] byteArray = IOUtils.toByteArray(stream);
		Validate.notNull(byteArray);
		return byteArray;
	}

	public List<ITermLoaderSvc.FileDescriptor> getFiles() {
		return myFiles;
	}

	public void addFileText(String theText, String theFilename) {
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
