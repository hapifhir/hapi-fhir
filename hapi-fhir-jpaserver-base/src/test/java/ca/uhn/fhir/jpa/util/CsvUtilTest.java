package ca.uhn.fhir.jpa.util;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilTest {

	@Test
	void testWriteCsvToByteArray() {

		String[] headers = new String[]{"Name", "Gender"};
		CsvUtil.ICsvProducer producer = printer -> {
			printer.printRecord("Homer", "M");
			printer.printRecord("Marge", "F");
		};
		byte[] bytes = CsvUtil.writeCsvToByteArray(headers, producer);

		String actual = new String(bytes, StandardCharsets.UTF_8);
		String expected = """
			Name,Gender
			Homer,M
			Marge,F
			""";
		assertEquals(expected, actual);

	}

}
