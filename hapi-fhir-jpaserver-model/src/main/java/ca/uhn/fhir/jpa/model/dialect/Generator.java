package ca.uhn.fhir.jpa.model.dialect;

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

import java.io.Serializable;
import java.util.Properties;

public class Generator implements IdentifierGenerator, PersistentIdentifierGenerator, BulkInsertionCapableIdentifierGenerator {

	private SequenceStyleGenerator myGen = new SequenceStyleGenerator();

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
		return myGen.generate(theSession, theObject);
	}


	@Override
	public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {

		Properties props = new Properties(params);
		props.put(SequenceStyleGenerator.OPT_PARAM, StandardOptimizerDescriptor.POOLED);
		props.put(SequenceStyleGenerator.INITIAL_PARAM, "1");
		props.put(SequenceStyleGenerator.INCREMENT_PARAM, "50");

		myGen.configure(type, props, serviceRegistry);
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
}
