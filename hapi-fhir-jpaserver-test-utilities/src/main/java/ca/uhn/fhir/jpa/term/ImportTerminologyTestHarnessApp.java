/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package ca.uhn.fhir.jpa.term;

import ca.uhn.fhir.batch2.api.IJobMaintenanceService;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.model.dialect.HapiFhirPostgresDialect;
import ca.uhn.fhir.jpa.provider.TerminologyUploaderProvider;
import ca.uhn.fhir.jpa.test.config.TestR4Config;
import ca.uhn.fhir.rest.server.provider.ResourceProviderFactory;
import ca.uhn.fhir.test.utilities.server.RestfulServerExtension;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.time.Duration;

public class ImportTerminologyTestHarnessApp {
	private static final Logger ourLog = LoggerFactory.getLogger(ImportTerminologyTestHarnessApp.class);

	public static void main(String[] args) throws Exception {
		File targetDb = new File("target/term-loading-test-db/");
		FileUtils.deleteDirectory(targetDb);

		AnnotationConfigApplicationContext appCtx = new AnnotationConfigApplicationContext(MyConfig.class);
		appCtx.start();

		FhirContext fhirCtx = appCtx.getBean(FhirContext.class);

		RestfulServerExtension server = new RestfulServerExtension(fhirCtx);
		server.withPort(8000);
		for (Object provider : appCtx.getBean(ResourceProviderFactory.class).createProviders()) {
			server.registerProvider(provider);
		}
		server.registerProvider(appCtx.getBean(TerminologyUploaderProvider.class));
		server.beforeEach(null);

		ourLog.info("Server is running with base: {}", server.getBaseUrl());

		while (true) {
			Thread.sleep(1000);
			appCtx.getBean(IJobMaintenanceService.class).forceMaintenancePass();
		}
	}

	@Configuration
	public static class MyConfig extends TestR4Config {

		@Override
		public void setConnectionProperties(BasicDataSource theDataSource) {
			//			theDataSource.setDriver(new org.h2.Driver());
			//			theDataSource.setUrl("jdbc:h2:file:./target/term-loading-test-db/db");
			theDataSource.setDriver(new org.postgresql.Driver());
			theDataSource.setUrl("jdbc:postgresql://localhost:5432/cdr");
			theDataSource.setMaxWait(Duration.ofSeconds(30));
			theDataSource.setUsername("cdr");
			theDataSource.setPassword("cdr");
			theDataSource.setMaxTotal(ourMaxThreads);
		}

		@Override
		public String getHibernateDialect() {
			return HapiFhirPostgresDialect.class.getName();
		}
	}
}
