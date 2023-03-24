/*-
 * #%L
 * HAPI FHIR JPA Server Test Utilities
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.embedded;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import javax.sql.DataSource;
import java.util.Set;
import java.util.stream.Stream;

public class HapiEmbeddedDatabasesExtension implements AfterAllCallback {

	private final JpaEmbeddedDatabase myH2EmbeddedDatabase;
	private final JpaEmbeddedDatabase myPostgresEmbeddedDatabase;
	private final JpaEmbeddedDatabase myMsSqlEmbeddedDatabase;
	// TODO add Oracle

	public HapiEmbeddedDatabasesExtension(){
		myH2EmbeddedDatabase = new H2EmbeddedDatabase();
		myPostgresEmbeddedDatabase = new PostgresEmbeddedDatabase();
		myMsSqlEmbeddedDatabase = new MsSqlEmbeddedDatabase();
	}

	@Override
	public void afterAll(ExtensionContext theExtensionContext) throws Exception {
		for(JpaEmbeddedDatabase database : getAllEmbeddedDatabases()){
			database.stop();
		}
	}

	public JpaEmbeddedDatabase getEmbeddedDatabase(DriverTypeEnum theDriverType){
		switch (theDriverType) {
			case H2_EMBEDDED:
				return myH2EmbeddedDatabase;
			case POSTGRES_9_4:
				return myPostgresEmbeddedDatabase;
			case MSSQL_2012:
				return myMsSqlEmbeddedDatabase;
			default:
				throw new IllegalArgumentException("Driver type not supported: " + theDriverType);
		}
	}
	
	public void clearDatabases(){
		for(JpaEmbeddedDatabase database : getAllEmbeddedDatabases()){
			database.clearDatabase();
		}
	}

	public DataSource getDataSource(DriverTypeEnum theDriverTypeEnum){
		return getEmbeddedDatabase(theDriverTypeEnum).getDataSource();
	}

	private Set<JpaEmbeddedDatabase> getAllEmbeddedDatabases(){
		return Sets.newHashSet(myH2EmbeddedDatabase, myPostgresEmbeddedDatabase, myMsSqlEmbeddedDatabase);
	}

	public static class DatabaseVendorProvider implements ArgumentsProvider {
		@Override
		public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
			return Stream.of(
				Arguments.of(DriverTypeEnum.H2_EMBEDDED),
				Arguments.of(DriverTypeEnum.POSTGRES_9_4),
				Arguments.of(DriverTypeEnum.MSSQL_2012)
			);
		}
	}
}
