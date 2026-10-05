package ca.uhn.fhir.jpa.test.config;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-5-5
class TestConfigDatabaseUrlTest {

	static Stream<Arguments> configs() {
		return Stream.of(
			Arguments.of("jdbc:h2:mem:testdb_r5_", (Supplier<String>) () -> new TestR5Config().getDatabaseUrl()),
			Arguments.of("jdbc:h2:mem:testdb_r4b_", (Supplier<String>) () -> new TestR4BConfig().getDatabaseUrl()),
			Arguments.of("jdbc:h2:mem:testdb_dstu3_", (Supplier<String>) () -> new TestDstu3Config().getDatabaseUrl()),
			Arguments.of("jdbc:h2:mem:testdb_dstu2_", (Supplier<String>) () -> new TestDstu2Config().getDatabaseUrl()));
	}

	@ParameterizedTest
	@MethodSource("configs")
	void getDatabaseUrl_twoConfigInstances_returnsDifferentDatabases(String thePrefix, Supplier<String> theNewConfigUrl) {
		// execute
		String first = theNewConfigUrl.get();
		String second = theNewConfigUrl.get();

		// verify
		assertThat(first).startsWith(thePrefix);
		assertThat(second).startsWith(thePrefix);
		assertThat(first).isNotEqualTo(second);
	}
}
