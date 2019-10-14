package ca.uhn.fhir.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileUtilTest {

	@Test
	public void formatFileSize() {
		assertEquals("0 Bytes", FileUtil.formatFileSize(0));
		assertEquals("1 Bytes", FileUtil.formatFileSize(1));
		assertEquals("1.2 kB", FileUtil.formatFileSize(1234));
		assertEquals("12.1 kB", FileUtil.formatFileSize(12345));
		assertEquals("11.8 MB", FileUtil.formatFileSize(12345678));
		assertEquals("103.5 GB", FileUtil.formatFileSize(111111111111L));
		assertEquals("101.1 TB", FileUtil.formatFileSize(111111111111111L));
		assertEquals("10105.5 TB", FileUtil.formatFileSize(11111111111111111L));
	}
}
