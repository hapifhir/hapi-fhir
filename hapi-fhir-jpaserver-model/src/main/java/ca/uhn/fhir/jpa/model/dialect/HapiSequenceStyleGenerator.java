package ca.uhn.fhir.jpa.model.dialect;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.util.ISequenceValueMassager;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.Validate;
import org.hibernate.HibernateException;
import org.hibernate.boot.model.relational.Database;
import org.hibernate.boot.model.relational.ExportableProducer;
import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.BulkInsertionCapableIdentifierGenerator;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.PersistentIdentifierGenerator;
import org.hibernate.id.enhanced.Optimizer;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.id.enhanced.StandardOptimizerDescriptor;

import java.io.Serializable;
import java.util.Properties;

import static ca.uhn.fhir.jpa.model.util.JpaConstants.NO_MORE_PID;

/**
 * Hibernate 7.1.1 compatible sequence generator that wraps SequenceStyleGenerator and
 * optionally massages values via ISequenceValueMassager.
 */
@SuppressWarnings("unused")
public class HapiSequenceStyleGenerator
		implements PersistentIdentifierGenerator, BulkInsertionCapableIdentifierGenerator, ExportableProducer {

	public static final String ID_MASSAGER_TYPE_KEY = "hapi_fhir.sequence_generator_massager";

	private final SequenceStyleGenerator myGen = new SequenceStyleGenerator();

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

			if (NO_MORE_PID.equals(next) || NO_MORE_PID.equals(retVal)) {
				throw new InternalErrorException(
						Msg.code(2791) + "Resource ID generator provided illegal value: " + next + " / " + retVal);
			}
		}
		return retVal;
	}

	@Override
	public void configure(GeneratorCreationContext creationContext, Properties parameters) {
		myIdMassager = creationContext.getServiceRegistry().getService(ISequenceValueMassager.class);
		if (myIdMassager == null) {
			myIdMassager = new ISequenceValueMassager.NoopSequenceValueMassager();
		}
		myGeneratorName = parameters.getProperty(IdentifierGenerator.GENERATOR_NAME);
		Validate.notBlank(myGeneratorName, "No generator name found");

		Properties props = new Properties(parameters);
		props.put(OptimizableGenerator.OPT_PARAM, StandardOptimizerDescriptor.POOLED.getExternalName());
		props.put(OptimizableGenerator.INITIAL_PARAM, "1");
		props.put(OptimizableGenerator.INCREMENT_PARAM, "50");
		props.put(IdentifierGenerator.GENERATOR_NAME, myGeneratorName);

		myGen.configure(creationContext, props);
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
	public Optimizer getOptimizer() {
		return myGen.getOptimizer();
	}
}
