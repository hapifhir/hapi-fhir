package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import org.flywaydb.core.api.ClassProvider;
import org.flywaydb.core.api.ResourceProvider;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.resolver.MigrationResolver;
import org.flywaydb.core.api.resolver.ResolvedMigration;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.resolver.CompositeMigrationResolver;
import org.flywaydb.core.internal.sqlscript.SqlScriptExecutorFactory;
import org.flywaydb.core.internal.sqlscript.SqlScriptFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class HapiFhirCompositeMigrationResolver extends CompositeMigrationResolver {
	private final MigrationTaskList myTaskList;

	public HapiFhirCompositeMigrationResolver(ResourceProvider theResourceProvider, ClassProvider<JavaMigration> theClassProvider, FluentConfiguration theConfiguration, SqlScriptExecutorFactory theSqlScriptExecutorFactory, SqlScriptFactory theSqlScriptFactory, ParsingContext theParsingContext, StatementInterceptor theStatementInterceptor, MigrationResolver[] theResolvers, MigrationTaskList theTaskList) {
		super(theResourceProvider, theClassProvider, theConfiguration, theSqlScriptExecutorFactory, theSqlScriptFactory, theParsingContext, theStatementInterceptor, theResolvers);

		myTaskList = theTaskList;
	}

	@Override
	public Collection<ResolvedMigration> resolveMigrations(Context context) {
		return toResolvedMigrationList(myTaskList);
	}

	private List<ResolvedMigration> toResolvedMigrationList(MigrationTaskList theTaskList) {
		List<ResolvedMigration> retval = new ArrayList<>();
		for (BaseTask task : theTaskList) {
			ResolvedMigration resolvedMigration = new HapiFhirResolvedMigration(task);
			retval.add(resolvedMigration);
		}
		return retval;
	}
}
