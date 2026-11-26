package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import net.sf.jsqlparser.JSQLParserException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

// Created by Claude Opus 5.5
class PartitionIdJoinCheckerTest {

	private static final Set<String> PARTITIONED_TABLES = Set.of(
			"TRM_VALUESET",
			"TRM_VALUESET_CONCEPT",
			"TRM_VALUESET_C_DESIGNATION",
			"HFJ_SPIDX_URI",
			"HFJ_SPIDX_STRING",
			"HFJ_SPIDX_TOKEN",
			"HFJ_RESOURCE",
			"HFJ_RES_LINK");
	private static final String INNER_JOIN_ON_TARGET_PARTITION_ID_IN_CORRELATED_SUBQUERY =
			"SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE EXISTS (SELECT s0.PARTITION_ID "
					+ "FROM HFJ_RES_LINK s0 INNER JOIN HFJ_SPIDX_TOKEN s1 ON ((s0.TARGET_RES_PARTITION_ID = s1.PARTITION_ID) "
					+ "AND (s0.TARGET_RESOURCE_ID = s1.RES_ID)) WHERE ((s0.SRC_PATH = ?) AND (s1.HASH_SYS_AND_VALUE = ?) "
					+ "AND (s0.PARTITION_ID = t0.PARTITION_ID) AND (s0.SRC_RESOURCE_ID = t0.RES_ID)))";

	private final PartitionIdJoinChecker myChecker = new PartitionIdJoinChecker(PARTITIONED_TABLES, true);

	@Test
	void findViolations_outerJoinWithoutPartitionId_isReported() throws JSQLParserException {
		// Setup
		String sql = "select v.PID from ( SELECT vsc.PID AS PID FROM TRM_VALUESET_CONCEPT vsc "
				+ "LEFT OUTER JOIN TRM_VALUESET_C_DESIGNATION vscd ON vsc.PID = vscd.VALUESET_CONCEPT_PID ) v "
				+ "where v.PID=?";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).singleElement().asString()
				.contains("outer join")
				.contains("TRM_VALUESET_C_DESIGNATION")
				.contains("TRM_VALUESET_CONCEPT");
	}

	@Test
	void findViolations_outerJoinWithPartitionId_isAccepted() throws JSQLParserException {
		// Setup
		String sql = "select v.PID from ( SELECT vsc.PID AS PID FROM TRM_VALUESET_CONCEPT vsc "
				+ "LEFT OUTER JOIN TRM_VALUESET_C_DESIGNATION vscd "
				+ "ON vsc.PID = vscd.VALUESET_CONCEPT_PID AND vsc.PARTITION_ID = vscd.PARTITION_ID ) v "
				+ "where v.PID=?";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).isEmpty();
	}

	@Test
	void findViolations_outerJoinOnTargetPartitionId_isAccepted() throws JSQLParserException {
		// Setup: a chained sort, which distributed PostgreSQL runs
		String sql = "SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 LEFT OUTER JOIN HFJ_RES_LINK t1 "
				+ "ON ((t0.PARTITION_ID = t1.PARTITION_ID) AND (t0.RES_ID = t1.SRC_RESOURCE_ID) AND (t1.SRC_PATH = ?)) "
				+ "LEFT OUTER JOIN HFJ_SPIDX_STRING t2 ON ((t1.TARGET_RES_PARTITION_ID = t2.PARTITION_ID) "
				+ "AND (t1.TARGET_RESOURCE_ID = t2.RES_ID) AND (t2.HASH_IDENTITY = ?)) WHERE (t0.PARTITION_ID = ?)";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).isEmpty();
	}

	@Test
	void findViolations_correlatedSubqueryOnTargetPartitionId_isReported() throws JSQLParserException {
		// Setup: a chained :not, which distributed PostgreSQL rejects
		String sql = "SELECT t0.PARTITION_ID,t0.SRC_RESOURCE_ID FROM HFJ_RES_LINK t0 WHERE ((t0.SRC_PATH = ?) "
				+ "AND (NOT (EXISTS (SELECT s0.PARTITION_ID,s0.RES_ID FROM HFJ_SPIDX_TOKEN s0 WHERE ((s0.HASH_VALUE = ?) "
				+ "AND (s0.PARTITION_ID = t0.TARGET_RES_PARTITION_ID) AND (s0.RES_ID = t0.TARGET_RESOURCE_ID))))))";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).singleElement().asString()
				.contains("correlated subquery")
				.contains("HFJ_SPIDX_TOKEN")
				.contains("HFJ_RES_LINK");
	}

	@Test
	void findViolations_correlatedSubqueryWithoutPartitionId_isReported() throws JSQLParserException {
		// Setup
		String sql = "select tvs1_0.PID from TRM_VALUESET tvs1_0 where tvs1_0.EXPANSION_STATUS=? "
				+ "and exists(select 1 from HFJ_SPIDX_URI rispu1_0 "
				+ "where rispu1_0.HASH_URI=? and rispu1_0.RES_ID=tvs1_0.RES_ID) "
				+ "and not exists(select 1 from TRM_VALUESET_CONCEPT tvsc1_0 "
				+ "where (tvsc1_0.VALUESET_PID,tvsc1_0.PARTITION_ID)=(tvs1_0.PID,tvs1_0.PARTITION_ID) "
				+ "and tvsc1_0.SYSTEM_URL=?)";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).singleElement().asString()
				.contains("correlated subquery")
				.contains("HFJ_SPIDX_URI")
				.contains("TRM_VALUESET");
	}

	@Test
	void findViolations_correlatedSubqueryWithTuplePartitionId_isAccepted() throws JSQLParserException {
		// Setup
		String sql = "select tvs1_0.PID from TRM_VALUESET tvs1_0 where tvs1_0.EXPANSION_STATUS=? "
				+ "and exists(select 1 from HFJ_SPIDX_URI rispu1_0 where rispu1_0.HASH_URI=? "
				+ "and (rispu1_0.RES_ID,rispu1_0.PARTITION_ID)=(tvs1_0.RES_ID,tvs1_0.PARTITION_ID))";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).isEmpty();
	}

	@Test
	void findViolations_innerJoinWithoutPartitionId_isAccepted() throws JSQLParserException {
		// Setup: distributed PostgreSQL runs inner joins between partitioned tables as repartition joins
		String sql = "select r.RES_ID from HFJ_RES_LINK l "
				+ "join HFJ_RESOURCE r on r.RES_ID = l.TARGET_RESOURCE_ID where l.SRC_RESOURCE_ID=?";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).isEmpty();
	}

	@Test
	void findViolations_innerJoinOnTargetPartitionIdInCorrelatedSubquery_isReported() throws JSQLParserException {
		// Setup: a chained search with contained resource indexing on a database other than PostgreSQL, which
		// distributed PostgreSQL rejects because it cannot repartition a join inside a correlated subquery
		String sql = INNER_JOIN_ON_TARGET_PARTITION_ID_IN_CORRELATED_SUBQUERY;

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).singleElement().asString()
				.contains("join of HFJ_SPIDX_TOKEN")
				.contains("correlated subquery");
	}

	@Test
	void findViolations_innerJoinOnTargetPartitionIdInCorrelatedSubqueryNotChecked_isAccepted()
			throws JSQLParserException {
		// Setup
		PartitionIdJoinChecker checker = new PartitionIdJoinChecker(PARTITIONED_TABLES, false);

		// Test
		List<String> violations = checker.findViolations(INNER_JOIN_ON_TARGET_PARTITION_ID_IN_CORRELATED_SUBQUERY);

		// Verify
		assertThat(violations).isEmpty();
	}

	@Test
	void findViolations_innerJoinOnPartitionIdInCorrelatedSubquery_isAccepted() throws JSQLParserException {
		// Setup
		String sql = "SELECT t0.PARTITION_ID,t0.RES_ID FROM HFJ_RESOURCE t0 WHERE EXISTS (SELECT s0.PARTITION_ID "
				+ "FROM HFJ_RES_LINK s0 INNER JOIN HFJ_SPIDX_TOKEN s1 ON ((s0.PARTITION_ID = s1.PARTITION_ID) "
				+ "AND (s0.SRC_RESOURCE_ID = s1.RES_ID)) WHERE ((s0.SRC_PATH = ?) AND (s1.HASH_SYS_AND_VALUE = ?) "
				+ "AND (s0.PARTITION_ID = t0.PARTITION_ID) AND (s0.SRC_RESOURCE_ID = t0.RES_ID)))";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).isEmpty();
	}

	@Test
	void findViolations_uncorrelatedSubquery_isAccepted() throws JSQLParserException {
		// Setup
		String sql = "select r.RES_ID from HFJ_RESOURCE r where r.RES_ID in "
				+ "(select l.TARGET_RESOURCE_ID from HFJ_RES_LINK l where l.SRC_PATH=?)";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).isEmpty();
	}

	@Test
	void findViolations_outerJoinToTableNotPartitioned_isAccepted() throws JSQLParserException {
		// Setup
		String sql = "select r.RES_ID from HFJ_RESOURCE r left outer join HFJ_PARTITION p on p.PART_ID = r.PARTITION_ID";

		// Test
		List<String> violations = myChecker.findViolations(sql);

		// Verify
		assertThat(violations).isEmpty();
	}
}
