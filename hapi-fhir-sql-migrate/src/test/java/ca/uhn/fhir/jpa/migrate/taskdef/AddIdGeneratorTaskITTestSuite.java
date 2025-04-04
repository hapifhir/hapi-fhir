package ca.uhn.fhir.jpa.migrate.taskdef;

import ca.uhn.fhir.jpa.migrate.JdbcUtils;
import ca.uhn.fhir.jpa.migrate.taskdef.containertests.BaseMigrationTaskTestSuite;
import ca.uhn.fhir.jpa.migrate.tasks.api.Builder;
import org.hibernate.tool.schema.extract.spi.SequenceInformation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for AddIdGeneratorTask.
 */
public interface AddIdGeneratorTaskITTestSuite extends BaseMigrationTaskTestSuite {

	int EXPECTED_DEFAULT_INCREMENT = 50;

	@Test
	default void testAddIdGenerator_addsGenerator() throws SQLException {
		// setup
		Builder builder = getSupport().getBuilder();
		String sequenceName = "seq_test" + System.currentTimeMillis();
		builder.addIdGenerator("1", sequenceName);

		getSupport().executeAndClearPendingTasks();
		List<SequenceInformation> sequenceInformation = getSequence(sequenceName);

		// verify
		assertEquals(1, sequenceInformation.size());
		assertEquals(sequenceName, sequenceInformation.get(0).getSequenceName().getSequenceName().getText().toLowerCase());
		assertEquals(EXPECTED_DEFAULT_INCREMENT, sequenceInformation.get(0).getIncrementValue().intValue());
	}

	@ParameterizedTest
	@ValueSource(ints = {1, 10})
	default void testAddIdGeneratorWithIncrement_addsGeneratorCorrectly(int theIncrement) throws SQLException {
		// setup
		Builder builder = getSupport().getBuilder();
		String sequenceName = "seq_test_increment" + System.currentTimeMillis();
		builder.addIdGenerator("1", sequenceName, theIncrement);

		getSupport().executeAndClearPendingTasks();
		List<SequenceInformation> sequenceInformation = getSequence(sequenceName);

		// verify
		assertEquals(1, sequenceInformation.size());
		assertEquals(sequenceName, sequenceInformation.get(0).getSequenceName().getSequenceName().getText().toLowerCase());
		assertEquals(theIncrement, sequenceInformation.get(0).getIncrementValue().intValue());
	}

	private List<SequenceInformation> getSequence(String theSequenceName) throws SQLException {
		return JdbcUtils.getSequenceInformation(getSupport().getConnectionProperties())
			.stream()
			.filter(si -> theSequenceName.equalsIgnoreCase(si.getSequenceName().getSequenceName().getText()))
			.toList();
	}
}
