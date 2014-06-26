package ca.uhn.fhir.jpa.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

import ca.uhn.fhir.parser.DataFormatException;

public class GZipUtil {

	public static String decompress(byte[] theResource) {
		GZIPInputStream is;
		try {
			is = new GZIPInputStream(new ByteArrayInputStream(theResource));
			return IOUtils.toString(is, "UTF-8");
		} catch (IOException e) {
			throw new DataFormatException("Failed to decompress contents", e);
		}
	}

	public static byte[] compress(String theEncoded) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			GZIPOutputStream gos = new GZIPOutputStream(os);
			IOUtils.write(theEncoded, gos, "UTF-8");
			gos.close();
			os.close();
			byte[] retVal = os.toByteArray();
			return retVal;
		} catch (IOException e) {
			throw new DataFormatException("Compress contents", e);
		}
	}

}
