package ca.uhn.fhir.tinder.ddl;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;
import ca.uhn.fhir.jpa.migrate.util.SqlUtil;
import jakarta.annotation.Nonnull;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.defaultString;
import static org.springframework.util.ResourceUtils.CLASSPATH_URL_PREFIX;
import static org.springframework.util.ResourceUtils.FILE_URL_PREFIX;

/**
 * This Maven Plugin takes a schema initialization script as input and produces a
 * version that activates partitioning. This is intended only to produce an example
 * partitioning script as opposed to a production-ready one. We use this for
 * unit tests and examples.
 */
@Mojo(
		name = "partition-ddl",
		defaultPhase = LifecyclePhase.PROCESS_CLASSES,
		requiresDependencyCollection = ResolutionScope.COMPILE_PLUS_RUNTIME,
		requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME,
		threadSafe = true,
		requiresProject = true)
public class PartitionDdlMojo extends AbstractMojo {

	private static final Logger ourLog = LoggerFactory.getLogger(PartitionDdlMojo.class);

	@Parameter(required = true)
	public String inputFile;

	@Parameter(required = true)
	public File outputFile;

	@Parameter(required = true)
	public String tuningStatementsFile;

	@Parameter(required = true)
	public DriverTypeEnum driverType;

	@Parameter(defaultValue = "5", required = false)
	public int numPartitions = 5;

	private List<String> myStatsStatements;

	@Override
	public void execute() throws MojoExecutionException {
		// Maybe in the future we can support more?
		Validate.isTrue(driverType == DriverTypeEnum.POSTGRES_9_4, "Only POSTGRES driver is supported");

		String tuningStatements = loadResource(tuningStatementsFile);
		myStatsStatements = SqlUtil.splitSqlFileIntoStatements(tuningStatements);

		String input = loadResource(inputFile);
		List<String> originalStatements = SqlUtil.splitSqlFileIntoStatements(input);
		List<String> newStatements = applyPostgresPartitioning(originalStatements);
		writeStatementsToTarget(newStatements);
	}

	@Nonnull
	private static String loadResource(String theResource) throws MojoExecutionException {
		try {
			String path = theResource;
			if (!path.startsWith(CLASSPATH_URL_PREFIX) && !path.startsWith(FILE_URL_PREFIX)) {
				path = FILE_URL_PREFIX + path;
			}
			Resource tuningStatementsResource = new DefaultResourceLoader().getResource(path);
			return tuningStatementsResource.getContentAsString(StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new MojoExecutionException(e);
		}
	}

	private void writeStatementsToTarget(List<String> theStatements) throws MojoExecutionException {
		try {
			String parent = outputFile.getParent();
			File parentDir = new File(parent);
			if (!parentDir.exists()) {
				FileUtils.forceMkdir(parentDir);
			}

			ourLog.info("Writing {} DDL lines to file: {}", theStatements.size(), outputFile);

			try (FileWriter fw = new FileWriter(outputFile, StandardCharsets.UTF_8, false)) {
				for (String statement : theStatements) {
					fw.append(defaultString(statement)).append('\n');
				}
			}

		} catch (IOException e) {
			throw new MojoExecutionException(e);
		}
	}

	/**
	 * For every CREATE TABLE statement in the DDL which is creating
	 * a partitioned table, also add the relevant SQL tuning statements.
	 */
	private List<String> applyPostgresPartitioning(List<String> statements) {
		List<String> retVal = new ArrayList<>();
		for (String statement : statements) {
			Optional<SqlUtil.CreateTablePrimaryKey> createTable =
					SqlUtil.parseCreateTableStatementPrimaryKey(statement);

			boolean partitioned = false;
			if (createTable.isPresent()) {
				String tableName = createTable.get().getTableName();
				List<String> primaryKeyColumns = createTable.get().getPrimaryKeyColumns();

				if (primaryKeyColumns.contains("PARTITION_ID")) {
					partitioned = true;

					retVal.add("-- Create partitioned table " + tableName);

					String replacement = statement + " PARTITION BY HASH (PARTITION_ID)";
					retVal.add(replacement + ";");

					retVal.add("-- Create " + numPartitions + " partitions for table " + tableName);
					for (int partIndex = 0; partIndex < numPartitions; partIndex++) {
						String newTableName = createPartitionedTableName(tableName, partIndex);
						String partitionedTable = "CREATE TABLE " + newTableName + " PARTITION OF " + tableName + " "
								+ "FOR VALUES WITH (MODULUS " + numPartitions + ", REMAINDER " + partIndex + ")";
						retVal.add(partitionedTable + ";");
					}

					boolean tuneCommentAdded = false;
					for (int partIndex = 0; partIndex < numPartitions; partIndex++) {
						for (String nextStatsStatement : myStatsStatements) {
							if (nextStatsStatement
									.toUpperCase(Locale.US)
									.startsWith("ALTER TABLE " + tableName.toUpperCase(Locale.US) + " ")) {
								if (!tuneCommentAdded) {
									retVal.add("-- Tune " + numPartitions + " partitions for table " + tableName);
									tuneCommentAdded = true;
								}
								String newTableName = createPartitionedTableName(tableName, partIndex);
								String modifiedStatsStatement =
										nextStatsStatement.replace(" " + tableName + " ", " " + newTableName + " ");
								retVal.add(modifiedStatsStatement + ";");
							}
						}
					}
				}
			}

			if (!partitioned) {
				retVal.add(statement + ";");
			}

			// Add an empty line between unrelated statements
			retVal.add(null);
		}
		return retVal;
	}

	@Nonnull
	private static String createPartitionedTableName(String tableName, int partIndex) {
		return (tableName + "_PART" + partIndex).toUpperCase(Locale.US);
	}
}
