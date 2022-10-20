package ca.uhn.fhir.jpa.migrate;

import org.flywaydb.core.api.ClassProvider;
import org.flywaydb.core.api.FlywayException;
import org.flywaydb.core.api.ResourceProvider;
import org.flywaydb.core.api.callback.Callback;
import org.flywaydb.core.api.callback.Event;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.flywaydb.core.api.exception.FlywayValidateException;
import org.flywaydb.core.api.migration.JavaMigration;
import org.flywaydb.core.api.output.BaselineResult;
import org.flywaydb.core.api.output.MigrateResult;
import org.flywaydb.core.api.output.ValidateResult;
import org.flywaydb.core.api.pattern.ValidatePattern;
import org.flywaydb.core.internal.callback.CallbackExecutor;
import org.flywaydb.core.internal.callback.DefaultCallbackExecutor;
import org.flywaydb.core.internal.callback.NoopCallbackExecutor;
import org.flywaydb.core.internal.callback.SqlScriptCallbackFactory;
import org.flywaydb.core.internal.clazz.NoopClassProvider;
import org.flywaydb.core.internal.command.DbBaseline;
import org.flywaydb.core.internal.command.DbMigrate;
import org.flywaydb.core.internal.command.DbSchemas;
import org.flywaydb.core.internal.command.DbValidate;
import org.flywaydb.core.internal.database.DatabaseType;
import org.flywaydb.core.internal.database.base.Database;
import org.flywaydb.core.internal.database.base.Schema;
import org.flywaydb.core.internal.jdbc.JdbcConnectionFactory;
import org.flywaydb.core.internal.jdbc.StatementInterceptor;
import org.flywaydb.core.internal.license.VersionPrinter;
import org.flywaydb.core.internal.parser.ParsingContext;
import org.flywaydb.core.internal.resolver.CompositeMigrationResolver;
import org.flywaydb.core.internal.resource.NoopResourceProvider;
import org.flywaydb.core.internal.resource.StringResource;
import org.flywaydb.core.internal.schemahistory.SchemaHistory;
import org.flywaydb.core.internal.schemahistory.SchemaHistoryFactory;
import org.flywaydb.core.internal.sqlscript.SqlScript;
import org.flywaydb.core.internal.sqlscript.SqlScriptExecutorFactory;
import org.flywaydb.core.internal.sqlscript.SqlScriptFactory;
import org.flywaydb.core.internal.strategy.RetryStrategy;
import org.flywaydb.core.internal.util.IOUtils;
import org.flywaydb.core.internal.util.Pair;
import org.flywaydb.core.internal.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleFlywayExecutor {
	private static final Logger ourLog = LoggerFactory.getLogger(SimpleFlywayExecutor.class);
	private final DataSource myDataSource;
	private final DriverTypeEnum myH2Embedded;
	private final String myTableName;
	private OutputStream myOutputStream;
	private FluentConfiguration myConfiguration;

	public SimpleFlywayExecutor(DataSource theDataSource, DriverTypeEnum theDriverType, String theTableName) {
		this(theDataSource, theDriverType, theTableName, false);
	}

	public SimpleFlywayExecutor(DataSource theDataSource, DriverTypeEnum theDriverType, String theTableName, boolean theDryRun) {
		myDataSource = theDataSource;
		myH2Embedded = theDriverType;
		myTableName = theTableName;
		myConfiguration = new FluentConfiguration()
			.dataSource(myDataSource)
			.table(theTableName);
		if (theDryRun) {
			myOutputStream = new ByteArrayOutputStream();
			myConfiguration.dryRunOutput(myOutputStream);
		}
	}

	public MigrationResult migrate(MigrationTaskList theTaskList) {
		MigrationResult result;

		VersionPrinter.printVersion();

		StatementInterceptor statementInterceptor = null;

		final ParsingContext parsingContext = new ParsingContext();

		JdbcConnectionFactory jdbcConnectionFactory = new JdbcConnectionFactory(myConfiguration.getDataSource(), myConfiguration, statementInterceptor);

		final DatabaseType databaseType = jdbcConnectionFactory.getDatabaseType();
		final SqlScriptFactory sqlScriptFactory = databaseType.createSqlScriptFactory(myConfiguration, parsingContext);
		RetryStrategy.setNumberOfRetries(myConfiguration.getLockRetryCount());

		final SqlScriptExecutorFactory noCallbackSqlScriptExecutorFactory = databaseType.createSqlScriptExecutorFactory(
			jdbcConnectionFactory, NoopCallbackExecutor.INSTANCE, null);

		ResourceProvider resourceProvider = NoopResourceProvider.INSTANCE;

		jdbcConnectionFactory.setConnectionInitializer((jdbcConnectionFactory1, connection) -> {
			if (myConfiguration.getInitSql() == null) {
				return;
			}
			StringResource resource = new StringResource(myConfiguration.getInitSql());

			SqlScript sqlScript = sqlScriptFactory.createSqlScript(resource, true, resourceProvider);

			boolean outputQueryResults = false;


			noCallbackSqlScriptExecutorFactory.createSqlScriptExecutor(connection, false, false, outputQueryResults).execute(sqlScript);
		});

		Database database = null;
		try {
			database = databaseType.createDatabase(myConfiguration, false, jdbcConnectionFactory, statementInterceptor);
			databaseType.printMessages();
			ourLog.debug("DDL Transactions Supported: " + database.supportsDdlTransactions());

			Pair<Schema, List<Schema>> schemas = SchemaHistoryFactory.prepareSchemas(myConfiguration, database);
			Schema defaultSchema = schemas.getLeft();


			parsingContext.populate(database, myConfiguration);

			database.ensureSupported();

			DefaultCallbackExecutor callbackExecutor = new DefaultCallbackExecutor(myConfiguration, database, defaultSchema, prepareCallbacks(
				database, resourceProvider, jdbcConnectionFactory, sqlScriptFactory, statementInterceptor, defaultSchema, parsingContext));

			SqlScriptExecutorFactory sqlScriptExecutorFactory = databaseType.createSqlScriptExecutorFactory(jdbcConnectionFactory, callbackExecutor, statementInterceptor);

			SchemaHistory schemaHistory = SchemaHistoryFactory.getSchemaHistory(
				myConfiguration,
				noCallbackSqlScriptExecutorFactory,
				sqlScriptFactory,
				database,
				defaultSchema,
				statementInterceptor);


			result = doMigrate(
				createMigrationResolver(resourceProvider, NoopClassProvider.INSTANCE, sqlScriptExecutorFactory, sqlScriptFactory, parsingContext, statementInterceptor, theTaskList),
				schemaHistory,
				database,
				defaultSchema,
				schemas.getRight().toArray(new Schema[0]),
				callbackExecutor,
				statementInterceptor);
		} finally {
			IOUtils.close(database);
		}
		return result;
	}

	private MigrationResult doMigrate(CompositeMigrationResolver migrationResolver, SchemaHistory schemaHistory, Database database, Schema defaultSchema, Schema[] schemas, DefaultCallbackExecutor callbackExecutor, StatementInterceptor statementInterceptor) {
		if (myConfiguration.isValidateOnMigrate()) {
			List<ValidatePattern> ignorePatterns = new ArrayList<>(Arrays.asList(myConfiguration.getIgnoreMigrationPatterns()));
			ignorePatterns.add(ValidatePattern.fromPattern("*:pending"));
			ValidateResult validateResult = doValidate(database, migrationResolver, schemaHistory, defaultSchema, schemas, callbackExecutor, ignorePatterns.toArray(new ValidatePattern[0]));
			if (!validateResult.validationSuccessful && !myConfiguration.isCleanOnValidationError()) {
				throw new FlywayValidateException(validateResult.errorDetails, validateResult.getAllErrorMessages());
			}
		}

		if (myConfiguration.isCreateSchemas()) {
			new DbSchemas(database, schemas, schemaHistory, callbackExecutor).create(false);
		} else if (!defaultSchema.exists()) {
			ourLog.warn("The configuration option 'createSchemas' is false.\n" +
				"However, the schema history table still needs a schema to reside in.\n" +
				"You must manually create a schema for the schema history table to reside in.\n" +
				"See https://flywaydb.org/documentation/concepts/migrations.html#the-createschemas-option-and-the-schema-history-table");
		}

		if (!schemaHistory.exists()) {
			List<Schema> nonEmptySchemas = new ArrayList<>();
			for (Schema schema : schemas) {
				if (schema.exists() && !schema.empty()) {
					nonEmptySchemas.add(schema);
				}
			}

			if (!nonEmptySchemas.isEmpty()


			) {
				if (myConfiguration.isBaselineOnMigrate()) {
					doBaseline(schemaHistory, callbackExecutor, database);
				} else {
					// Second check for MySQL which is sometimes flaky otherwise
					if (!schemaHistory.exists()) {
						throw new FlywayException("Found non-empty schema(s) "
							+ StringUtils.collectionToCommaDelimitedString(nonEmptySchemas)
							+ " but no schema history table. Use baseline()"
							+ " or set baselineOnMigrate to true to initialize the schema history table.");
					}
				}
			}

			schemaHistory.create(false);
		}

		MigrateResult result = new DbMigrate(database, schemaHistory, defaultSchema, migrationResolver, myConfiguration, callbackExecutor).migrate();

		callbackExecutor.onOperationFinishEvent(Event.AFTER_MIGRATE_OPERATION_FINISH, result);

		return MigrationResult.fromFlywayResult(result);
	}

	private ValidateResult doValidate(Database database, CompositeMigrationResolver migrationResolver, SchemaHistory schemaHistory,
												 Schema defaultSchema, Schema[] schemas, CallbackExecutor callbackExecutor, ValidatePattern[] ignorePatterns) {

		return new DbValidate(database, schemaHistory, defaultSchema, migrationResolver, myConfiguration, callbackExecutor, ignorePatterns).validate();
	}
	private BaselineResult doBaseline(SchemaHistory schemaHistory, CallbackExecutor callbackExecutor, Database database) {
		return new DbBaseline(schemaHistory, myConfiguration.getBaselineVersion(), myConfiguration.getBaselineDescription(), callbackExecutor, database).baseline();
	}

	private List<Callback> prepareCallbacks(Database database, ResourceProvider resourceProvider,
														 JdbcConnectionFactory jdbcConnectionFactory,
														 SqlScriptFactory sqlScriptFactory, StatementInterceptor statementInterceptor,
														 Schema schema, ParsingContext parsingContext) {
		List<Callback> effectiveCallbacks = new ArrayList<>();
		CallbackExecutor callbackExecutor = NoopCallbackExecutor.INSTANCE;


		effectiveCallbacks.addAll(Arrays.asList(myConfiguration.getCallbacks()));


		if (!myConfiguration.isSkipDefaultCallbacks()) {
			SqlScriptExecutorFactory sqlScriptExecutorFactory = jdbcConnectionFactory.getDatabaseType().createSqlScriptExecutorFactory(
				jdbcConnectionFactory, callbackExecutor, statementInterceptor);

			effectiveCallbacks.addAll(new SqlScriptCallbackFactory(resourceProvider, sqlScriptExecutorFactory, sqlScriptFactory, myConfiguration).getCallbacks());
		}


		return effectiveCallbacks;
	}

	// FIXME KHS remove
	private CompositeMigrationResolver createMigrationResolver(ResourceProvider resourceProvider,
																				  ClassProvider<JavaMigration> classProvider,
																				  SqlScriptExecutorFactory sqlScriptExecutorFactory,
																				  SqlScriptFactory sqlScriptFactory,
																				  ParsingContext parsingContext,
																				  StatementInterceptor statementInterceptor, MigrationTaskList theTaskList) {
		return new HapiFhirCompositeMigrationResolver(resourceProvider, classProvider, myConfiguration, sqlScriptExecutorFactory, sqlScriptFactory, parsingContext, statementInterceptor, myConfiguration.getResolvers(), theTaskList);
	}


	public void validate() {
		// FIXME KHS
	}

	public void dropTable() {
		// FIXME KHS
	}
}
