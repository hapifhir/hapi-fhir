package ca.uhn.fhir.jpa.model.dialect;

/*-
 * #%L
 * HAPI FHIR JPA Model
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
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

import org.apache.commons.lang3.Validate;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.BulkInsertionCapableIdentifierGenerator;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.id.enhanced.StandardOptimizerDescriptor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.Properties;

@SuppressWarnings("unused")
public class HapiSequenceStyleGenerator implements IdentifierGenerator, PersistentIdentifierGenerator, BulkInsertionCapableIdentifierGenerator {

	@Nonnull
	private static IMassager ourIdMassager;

	static {
		NoopMassager idMassager = new NoopMassager();
		setIdMassager(idMassager);
	}

	private SequenceStyleGenerator myGen = new SequenceStyleGenerator();
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
	public Serializable generate(SharedSessionContractImplementor theSession, Object theObject) throws HibernateException {
		Long next = (Long) myGen.generate(theSession, theObject);
		return ourIdMassager.massage(myGeneratorName, next);
	}

	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
		myConfigured = true;

		myGeneratorName = params.getProperty(IdentifierGenerator.GENERATOR_NAME);
		Validate.notBlank(myGeneratorName, "No generator name found");

		Properties props = new Properties(params);
		props.put(SequenceStyleGenerator.OPT_PARAM, StandardOptimizerDescriptor.POOLED.getExternalName());
		props.put(SequenceStyleGenerator.INITIAL_PARAM, "1");
		props.put(SequenceStyleGenerator.INCREMENT_PARAM, "50");

		myGen.configure(type, props, serviceRegistry);
	}

	@Override
	public void registerExportables(Database database) {
		if (!myConfigured) {
			return;
		}
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

	public interface IMassager {

		Long massage(String theGeneratorName, Long theId);

	}

	public static final class NoopMassager implements IMassager {

		@Override
		public Long massage(String theGeneratorName, Long theId) {
			return theId;
		}
	}

	public static void setIdMassager(@Nonnull NoopMassager theIdMassager) {
		Validate.notNull(theIdMassager);
		ourIdMassager = theIdMassager;
	}

}
