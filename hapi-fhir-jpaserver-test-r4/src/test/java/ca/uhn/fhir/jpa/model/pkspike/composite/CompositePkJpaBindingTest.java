package ca.uhn.fhir.jpa.model.pkspike.composite;

import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.PKSpikeDefaultJPAConfig;
import ca.uhn.fhir.jpa.model.pkspike.SchemaCleanerExtension;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Spike to assess variable binding against a db.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	CompositeKeyTypesConfig.class, PKSpikeDefaultJPAConfig.class, FhirContextR4Config.class
})
public class CompositePkJpaBindingTest {
	private static final Logger ourLog = LoggerFactory.getLogger(CompositePkJpaBindingTest.class);

	@RegisterExtension
	SchemaCleanerExtension myCleanerExtension = new SchemaCleanerExtension();

	@Inject
	DataSource myDataSource;

	@Inject
	EntityManagerFactory myEntityManagerFactory;

	@Inject
	TransactionTemplate myTransactionTemplate;

	JdbcTemplate myJdbcTemplate;

	@BeforeEach
	void setUp() {
		myJdbcTemplate = new JdbcTemplate(myDataSource);
	}

	@Test
	void roundTripResourceTable() {
		// given

		myTransactionTemplate.execute(status -> {
			var em = getEntityManagerOrThrow();
			var root = new ResRootCompositeEntity();
			root.myPartitionId=12;
			root.myString="foo";

			em.persist(root);
			em.flush();
			em.clear();

			Object rawPk = myEntityManagerFactory.getPersistenceUnitUtil().getIdentifier(root);
			assertInstanceOf(ResRootCompositeEntity.ResRootPK.class, rawPk);
			var pk = (ResRootCompositeEntity.ResRootPK) rawPk;
			assertEquals(root.myId, pk.myId);
			assertEquals(12, pk.myPartitionId);

			var readback = em.find(ResRootCompositeEntity.class, pk);
			assertEquals(12, readback.myPartitionId);
			assertEquals("foo", readback.myString);
			return true;
		});
	}


	@ParameterizedTest
	@ValueSource(ints = {12})
	@NullSource
	void roundTripJoin(Integer thePartitionId) {
		// given

		myTransactionTemplate.execute(status -> {
			var em = getEntityManagerOrThrow();

			ResRootCompositeEntity resRootCompositeEntity = new ResRootCompositeEntity();
			resRootCompositeEntity.setString("hello world!");
			resRootCompositeEntity.myPartitionId = thePartitionId;

			ResJoinCompositeEntity join = new ResJoinCompositeEntity();
			join.myResource = resRootCompositeEntity;
			join.myString="child";

			em.persist(resRootCompositeEntity);
			em.persist(join);
			em.flush();
			em.clear();

			var sqlCount = myJdbcTemplate.queryForObject("select count(1) from RES_ROOT", Integer.class);
			assertEquals(1, sqlCount);

			var rootCount =
				em.createQuery("select count(*) from ResRootCompositeEntity", Long.class)
				.getSingleResult();
			assertEquals(1, rootCount);

			ourLog.info("find rows in res_root:");

			myJdbcTemplate.query("select res_id, partition_id from RES_ROOT", new RowMapper<Object>() {
				@Override
				public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
					ourLog.info("find row in res_root ({} {})", rs.getObject(1, Long.class), rs.getObject(2, Integer.class));
					return null;
				}
			});
			ourLog.info("end:find rows in res_root:");

			var readAll =
				em.createQuery("from ResRootCompositeEntity", ResRootCompositeEntity.class)
					.getResultList();
			assertEquals(1, readAll.size());
			var read2 = readAll.get(0);
			assertNotNull(read2);
			ourLog.info("readback {}", resRootCompositeEntity);
			assertEquals(resRootCompositeEntity.myId, read2.myId);

			ResRootCompositeEntity queryBack = em
					.createQuery("from ResRootCompositeEntity where myId = :id",  ResRootCompositeEntity.class)
					.setParameter("id", resRootCompositeEntity.myId)
				.getSingleResult();

			assertEquals("hello world!", queryBack.myString);
			assertEquals(1, queryBack.myJoinEntities.size());
			ResJoinCompositeEntity child = queryBack.myJoinEntities.iterator().next();
			assertEquals(queryBack.myId, child.myResource.myId);
			assertEquals(thePartitionId, queryBack.myPartitionId);
			assertEquals(thePartitionId, child.myPartitionId);
			assertEquals("child", child.myString);

			long count = em.createQuery("select count(*) from ResRootCompositeEntity ", Long.class).getSingleResult();
			ourLog.info("found {} roots", count);
			assertEquals(1, count);
			return true;
		});
	}

	private static @Nonnull ResRootCompositeEntity.ResRootPK getPrimaryKey(ResRootCompositeEntity resRootCompositeEntity) {
		return new ResRootCompositeEntity.ResRootPK(resRootCompositeEntity.myId, resRootCompositeEntity.myPartitionId);
	}

	@Nonnull EntityManager getEntityManagerOrThrow() {
		return Objects.requireNonNull(EntityManagerFactoryUtils.getTransactionalEntityManager(myEntityManagerFactory));
	}
}
