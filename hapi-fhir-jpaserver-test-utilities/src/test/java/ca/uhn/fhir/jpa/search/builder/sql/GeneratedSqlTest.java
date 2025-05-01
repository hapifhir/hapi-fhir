package ca.uhn.fhir.jpa.search.builder.sql;

import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class GeneratedSqlTest {

	@Test
	public void testBlockInlineNonBoundParameters() {
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> new GeneratedSql(false, "SELECT * FROM FOO WHERE t = '123'", Collections.emptyList()));
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> new GeneratedSql(false, "SELECT * FROM FOO WHERE t='123'", Collections.emptyList()));
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> new GeneratedSql(false, "SELECT * FROM FOO WHERE t in ('123')", Collections.emptyList()));
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> new GeneratedSql(false, "SELECT * FROM FOO WHERE t IN ('123')", Collections.emptyList()));
		assertThatExceptionOfType(AssertionError.class).isThrownBy(() -> new GeneratedSql(false, "SELECT * FROM FOO WHERE t IN('123')", Collections.emptyList()));
	}

}
