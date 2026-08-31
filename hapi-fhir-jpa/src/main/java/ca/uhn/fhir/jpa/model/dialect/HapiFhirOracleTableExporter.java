/*-
 * #%L
 * HAPI FHIR JPA Model
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
package ca.uhn.fhir.jpa.model.dialect;

import ca.uhn.hapi.fhir.sql.hibernatesvc.OracleIOT;
import jakarta.persistence.Table;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.dialect.Dialect;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.tool.schema.internal.StandardTableExporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Oracle-specific {@link StandardTableExporter} that adds {@code ORGANIZATION INDEX}
 * syntax to {@code CREATE TABLE} statements for entities annotated with {@link OracleIOT}.
 *
 * <p>The overridden {@link #getSqlCreateStrings} API is stable across Hibernate 6.x and 7.x.
 * If a future Hibernate version changes this API, the same IOT injection can be done
 * via post-processing in {@link ca.uhn.fhir.tinder.ddl.DdlGeneratorHibernate61#generateDdl}.
 */
public class HapiFhirOracleTableExporter extends StandardTableExporter {

	private static final Logger ourLog = LoggerFactory.getLogger(HapiFhirOracleTableExporter.class);

	private final boolean myDatabasePartitionMode;
	// Built on first use and cached to avoid rescanning entity bindings for every table
	private Map<String, Integer> myIotTableMap;

	public HapiFhirOracleTableExporter(Dialect theDialect, boolean theDatabasePartitionMode) {
		super(theDialect);
		myDatabasePartitionMode = theDatabasePartitionMode;
	}

	/**
	 * Hibernate has no built-in support for Oracle Index-Organized Tables, so we intercept
	 * the standard DDL output and append {@code ORGANIZATION INDEX COMPRESS n} for entities
	 * annotated with {@link OracleIOT}.
	 */
	@Override
	public String[] getSqlCreateStrings(
			org.hibernate.mapping.Table theTable, Metadata theMetadata, SqlStringGenerationContext theContext) {
		String[] result = super.getSqlCreateStrings(theTable, theMetadata, theContext);

		Integer compressionLevel = getIotCompressionLevel(theTable, theMetadata);
		if (compressionLevel != null && result.length > 0) {
			String createStmt = result[0];
			int lastParen = createStmt.lastIndexOf(')');
			if (lastParen >= 0) {
				StringBuilder iotClause = new StringBuilder(" ORGANIZATION INDEX");
				if (compressionLevel > 0) {
					iotClause.append(" COMPRESS ").append(compressionLevel);
				}
				result[0] = createStmt.substring(0, lastParen + 1) + iotClause + createStmt.substring(lastParen + 1);
				ourLog.info("Added ORGANIZATION INDEX COMPRESS {} to table {}", compressionLevel, theTable.getName());
			}
		}

		return result;
	}

	/**
	 * Returns the IOT compression level for the given table.
	 */
	private Integer getIotCompressionLevel(org.hibernate.mapping.Table theTable, Metadata theMetadata) {
		if (myIotTableMap == null) {
			myIotTableMap = buildIotTableMap(theMetadata);
		}
		return myIotTableMap.get(theTable.getName().toUpperCase());
	}

	/**
	 * Scans all entity bindings in the Hibernate metadata for classes annotated with
	 * {@link OracleIOT} and {@link Table}, building a map of uppercase table names
	 * to their compression levels based on the current partition mode.
	 */
	private Map<String, Integer> buildIotTableMap(Metadata theMetadata) {
		Map<String, Integer> iotTableMap = new HashMap<>();
		for (PersistentClass persistentClass : theMetadata.getEntityBindings()) {
			Class<?> mappedClass = persistentClass.getMappedClass();
			if (mappedClass == null) {
				continue;
			}
			OracleIOT iotAnnotation = mappedClass.getAnnotation(OracleIOT.class);
			Table tableAnnotation = mappedClass.getAnnotation(Table.class);
			if (iotAnnotation != null && tableAnnotation != null) {
				// Partition mode may alter the PK composition (by adding PARTITION_ID)
				// This shifts the optimal compression prefix
				// Separate DDL is generated for each mode, so each needs its own compression level
				int compressionLevel = myDatabasePartitionMode
						? iotAnnotation.partitionedCompressionLevel()
						: iotAnnotation.nonPartitionedCompressionLevel();
				iotTableMap.put(tableAnnotation.name().toUpperCase(), compressionLevel);
			}
		}
		return iotTableMap;
	}
}
