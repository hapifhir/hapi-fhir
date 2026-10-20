package ca.uhn.fhir.jpa.test.config;

import net.ttddyy.dsproxy.support.ProxyDataSource;
import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.sql.DataSource;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-5-5
class TestConfigDatabaseUrlTest {

	static Stream<Arguments> configs() {
		return Stream.of(
			Arguments.of("jdbc:h2:mem:testdb_r5_", poolUrl(TestR5Config::new, TestR5Config::dataSource)),
			Arguments.of("jdbc:h2:mem:testdb_r4_", poolUrl(TestR4Config::new, TestR4Config::dataSource)),
			Arguments.of("jdbc:h2:mem:testdb_r4_", poolUrl(TestR4WithDelayConfig::new, TestR4WithDelayConfig::dataSource)),
			Arguments.of("jdbc:h2:mem:testdb_r4b_", poolUrl(TestR4BConfig::new, TestR4BConfig::dataSource)),
			Arguments.of("jdbc:h2:mem:testdb_dstu3_", poolUrl(TestDstu3Config::new, TestDstu3Config::dataSource)),
			Arguments.of("jdbc:h2:mem:testdb_dstu2_", poolUrl(TestDstu2Config::new, TestDstu2Config::dataSource)));
	}

	/**
	 * @return a factory that creates a config and returns a reader of the URL its data source bean puts on the pool
	 */
	private static <T> Supplier<Supplier<String>> poolUrl(Supplier<T> theNewConfig, Function<T, DataSource> theDataSource) {
		return () -> {
			T config = theNewConfig.get();
			return () -> {
				ProxyDataSource proxy = (ProxyDataSource) theDataSource.apply(config);
				return ((BasicDataSource) proxy.getDataSource()).getUrl();
			};
		};
	}

	@ParameterizedTest
	@MethodSource("configs")
	void dataSource_perConfigInstance_usesItsOwnDatabase(String thePrefix, Supplier<Supplier<String>> theNewConfig) {
		// setup
		Supplier<String> firstConfig = theNewConfig.get();
		Supplier<String> secondConfig = theNewConfig.get();

		// execute
		String first = firstConfig.get();
		String firstAgain = firstConfig.get();
		String second = secondConfig.get();

		// verify
		assertThat(first).startsWith(thePrefix);
		assertThat(firstAgain).isEqualTo(first);
		assertThat(second).startsWith(thePrefix).isNotEqualTo(first);
	}
}
