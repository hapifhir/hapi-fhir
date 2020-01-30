package ca.uhn.fhir.jpa.migrate;

import ca.uhn.fhir.jpa.migrate.taskdef.BaseTask;
import ca.uhn.fhir.jpa.migrate.taskdef.DropIndexTask;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class MigrationTaskSkipperTest {
	public static final String RELEASE = "4_1_0";
	public static final String DATE_PREFIX = "20191214.";
	private static final String VERSION_PREFIX = RELEASE + "." + DATE_PREFIX;
	private List<BaseTask> myTasks;

	@Before
	public void before() {
		myTasks = new ArrayList<>();
		myTasks.add(new DropIndexTask(RELEASE, DATE_PREFIX + 1));
		myTasks.add(new DropIndexTask(RELEASE, DATE_PREFIX + 2));
		myTasks.add(new DropIndexTask(RELEASE, DATE_PREFIX + 3));
		myTasks.add(new DropIndexTask(RELEASE, DATE_PREFIX + 4));
		myTasks.add(new DropIndexTask(RELEASE, DATE_PREFIX + 5));
		myTasks.add(new DropIndexTask(RELEASE, DATE_PREFIX + 6));
	}

	@Test
	public void skipNull() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, null);
		assertSkipped(myTasks);
	}

	@Test
	public void skipAll() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, makeSkipString(1, 2, 3, 4, 5, 6));
		assertSkipped(myTasks, 1, 2, 3, 4, 5, 6);
	}

	@Test
	public void skipOne() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, makeSkipString(4));
		assertSkipped(myTasks, 4);
	}

	@Test
	public void skipTwo() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, VERSION_PREFIX + 2 + "," + VERSION_PREFIX + 3);
		assertSkipped(myTasks, 2, 3);
	}

	@Test
	public void skipWeirdSpacing() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, "   " + VERSION_PREFIX + 2 + "     ,     " + VERSION_PREFIX + 3 + "   ");
		assertSkipped(myTasks, 2, 3);
	}

	@Test
	public void testDoubleComma() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, VERSION_PREFIX + 2 + ",," + VERSION_PREFIX + 3);
		assertSkipped(myTasks, 2, 3);
	}

	@Test
	public void startComma() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, "," + VERSION_PREFIX + 2 + "," + VERSION_PREFIX + 3);
		assertSkipped(myTasks, 2, 3);
	}

	@Test
	public void quoted() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, "\"" + VERSION_PREFIX + 2 + "," + VERSION_PREFIX + 3 + "\"");
		assertSkipped(myTasks, 2, 3);
	}

	@Test
	public void allQuoted() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, "\"" + VERSION_PREFIX + 2 + "\",\"" + VERSION_PREFIX + 3 + "\"");
		assertSkipped(myTasks, 2, 3);
	}

	@Test
	public void oneQuoted() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, "\"" + VERSION_PREFIX + 2 + "\"" );
		assertSkipped(myTasks, 2);
	}

	@Test
	public void endComma() {
		MigrationTaskSkipper.setDoNothingOnSkippedTasks(myTasks, VERSION_PREFIX + 2 + "," + VERSION_PREFIX + 3 + ",");
		assertSkipped(myTasks, 2, 3);
	}

	private String makeSkipString(Integer... theVersions) {
		return integersToVersions(theVersions).collect(Collectors.joining(","));
	}

	private void assertSkipped(List<BaseTask> theTasks, Integer... theVersions) {
		Set<String> expectedVersions = integersToVersions(theVersions).collect(Collectors.toSet());
		Set<String> taskVersions = theTasks.stream().filter(BaseTask::isDoNothing).map(BaseTask::getFlywayVersion).collect(Collectors.toSet());
		assertThat(taskVersions, equalTo(expectedVersions));
	}

	@NotNull
	private Stream<String> integersToVersions(Integer[] theVersions) {
		return Stream.of(theVersions).map(s -> VERSION_PREFIX + s);
	}
}
