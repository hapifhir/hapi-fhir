package ca.uhn.fhir.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FileUtilTest {

	@Test
	public void formatFileSize() {
		assertThat(FileUtil.formatFileSize(0).replace(",", ".")).isEqualTo("0 Bytes");
		assertThat(FileUtil.formatFileSize(1).replace(",", ".")).isEqualTo("1 Bytes");
		assertThat(FileUtil.formatFileSize(1234).replace(",", ".")).isEqualTo("1.2 kB");
		assertThat(FileUtil.formatFileSize(12345).replace(",", ".")).isEqualTo("12.1 kB");
		assertThat(FileUtil.formatFileSize(12345678).replace(",", ".")).isEqualTo("11.8 MB");
		assertThat(FileUtil.formatFileSize(111111111111L).replace(",", ".")).isEqualTo("103.5 GB");
		assertThat(FileUtil.formatFileSize(111111111111111L).replace(",", ".")).isEqualTo("101.1 TB");
		assertThat(FileUtil.formatFileSize(11111111111111111L).replace(",", ".")).isEqualTo("10105.5 TB");
	}
}
