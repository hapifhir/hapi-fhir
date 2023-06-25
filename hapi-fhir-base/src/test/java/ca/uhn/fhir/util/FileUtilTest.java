package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileUtilTest {

	@Test
	public void formatFileSize() {
		assertEquals("0 Bytes", FileUtil.formatFileSize(0).replace(",", "."));
		assertEquals("1 Bytes", FileUtil.formatFileSize(1).replace(",", "."));
		assertEquals("1.2 kB", FileUtil.formatFileSize(1234).replace(",", "."));
		assertEquals("12.1 kB", FileUtil.formatFileSize(12345).replace(",", "."));
		assertEquals("11.8 MB", FileUtil.formatFileSize(12345678).replace(",", "."));
		assertEquals("103.5 GB", FileUtil.formatFileSize(111111111111L).replace(",", "."));
		assertEquals("101.1 TB", FileUtil.formatFileSize(111111111111111L).replace(",", "."));
		assertEquals("10105.5 TB", FileUtil.formatFileSize(11111111111111111L).replace(",", "."));
	}
}
