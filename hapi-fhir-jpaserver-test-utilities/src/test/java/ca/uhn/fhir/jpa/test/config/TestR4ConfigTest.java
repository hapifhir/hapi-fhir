package ca.uhn.fhir.jpa.test.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

// Created by claude-opus-5-5
class TestR4ConfigTest {

	@Test
	void setConnectionProperties_twoConfigInstances_useDifferentDatabases() {
		// setup
		BasicDataSource first = new BasicDataSource();
		BasicDataSource second = new BasicDataSource();

		// execute
		new TestR4Config().setConnectionProperties(first);
		new TestR4Config().setConnectionProperties(second);

		// verify
		assertThat(first.getUrl()).startsWith("jdbc:h2:mem:testdb_r4_");
		assertThat(second.getUrl()).startsWith("jdbc:h2:mem:testdb_r4_");
		assertThat(first.getUrl()).isNotEqualTo(second.getUrl());
	}

	@Test
	void setConnectionProperties_sameConfigInstance_usesSameDatabase() {
		// setup
		TestR4Config config = new TestR4Config();
		BasicDataSource first = new BasicDataSource();
		BasicDataSource second = new BasicDataSource();

		// execute
		config.setConnectionProperties(first);
		config.setConnectionProperties(second);

		// verify
		assertThat(first.getUrl()).isEqualTo(second.getUrl());
	}
}
