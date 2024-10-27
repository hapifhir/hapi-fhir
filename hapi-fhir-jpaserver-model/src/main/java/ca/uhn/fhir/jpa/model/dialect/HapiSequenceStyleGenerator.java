/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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

import ca.uhn.fhir.jpa.model.entity.StorageSettings;
import ca.uhn.fhir.jpa.util.ISequenceValueMassager;
import org.apache.commons.lang3.Validate;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.ExportableProducer;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.BulkInsertionCapableIdentifierGenerator;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.id.enhanced.StandardOptimizerDescriptor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Properties;

/**
 * This is a sequence generator that wraps the Hibernate default sequence generator {@link SequenceStyleGenerator}
 * and by default will therefore work exactly as the default would, but allows for customization.
 */
@SuppressWarnings("unused")
public class HapiSequenceStyleGenerator
		implements PersistentIdentifierGenerator, BulkInsertionCapableIdentifierGenerator, ExportableProducer {
	public static final String ID_MASSAGER_TYPE_KEY = "hapi_fhir.sequence_generator_massager";
	private final SequenceStyleGenerator myGen = new SequenceStyleGenerator();

	@Autowired
	private StorageSettings myStorageSettings;

	private ISequenceValueMassager myIdMassager;
	private boolean myConfigured;
	private String myGeneratorName;

	@Override
	public boolean supportsBulkInsertionIdentifierGeneration() {
		return myGen.supportsBulkInsertionIdentifierGeneration();
	}

	@Override
	public String determineBulkInsertionIdentifierGenerationSelectFragment(SqlStringGenerationContext theContext) {
		return myGen.determineBulkInsertionIdentifierGenerationSelectFragment(theContext);
	}

	@Override
	public Serializable generate(SharedSessionContractImplementor theSession, Object theObject)
			throws HibernateException {
		Long retVal = myIdMassager != null ? myIdMassager.generate(myGeneratorName) : null;
		if (retVal == null) {
			Long next = (Long) myGen.generate(theSession, theObject);
			retVal = myIdMassager.massage(myGeneratorName, next);
		}
		return retVal;
	}

	@Override
	public void configure(Type theType, Properties theParams, ServiceRegistry theServiceRegistry)
			throws MappingException {

		myIdMassager = theServiceRegistry.getService(ISequenceValueMassager.class);
		if (myIdMassager == null) {
			myIdMassager = new ISequenceValueMassager.NoopSequenceValueMassager();
		}

		// Create a HAPI FHIR sequence style generator
		myGeneratorName = theParams.getProperty(IdentifierGenerator.GENERATOR_NAME);
		Validate.notBlank(myGeneratorName, "No generator name found");

		Properties props = new Properties(theParams);
		props.put(OptimizableGenerator.OPT_PARAM, StandardOptimizerDescriptor.POOLED.getExternalName());
		props.put(OptimizableGenerator.INITIAL_PARAM, "1");
		props.put(OptimizableGenerator.INCREMENT_PARAM, "50");
		props.put(GENERATOR_NAME, myGeneratorName);

		myGen.configure(theType, props, theServiceRegistry);

		myConfigured = true;
	}

	@Override
	public void registerExportables(Database database) {
		myGen.registerExportables(database);
	}

	@Override
	public void initialize(SqlStringGenerationContext context) {
		myGen.initialize(context);
	}

	@Override
	public boolean supportsJdbcBatchInserts() {
		return myGen.supportsJdbcBatchInserts();
	}

	@Override
	public Optimizer getOptimizer() {
		return myGen.getOptimizer();
	}
}
