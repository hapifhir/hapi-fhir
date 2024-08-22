package ca.uhn.fhir.jpa.model.pkspike.composite;

import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import ca.uhn.fhir.jpa.model.pkspike.PKSpikeDefaultJPAConfig;
import ca.uhn.fhir.jpa.model.pkspike.SchemaCleanerExtension;
import ca.uhn.fhir.jpa.model.pkspike.primitive.SimpleTypesConfig;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

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


	@Test
	void roundTripJoin() {
		// given

		myTransactionTemplate.execute(status -> {
			var em = getEntityManagerOrThrow();

			ResRootCompositeEntity resRootCompositeEntity = new ResRootCompositeEntity();
			resRootCompositeEntity.setString("hello world!");
			resRootCompositeEntity.myPartitionId = 12;

			ResJoinCompositeEntity join = new ResJoinCompositeEntity();
			join.myResource = resRootCompositeEntity;
			join.myString="child";

			em.persist(resRootCompositeEntity);
			em.persist(join);
			em.flush();
			em.clear();

			ResRootCompositeEntity queryBack = em.find(ResRootCompositeEntity.class, getPrimaryKey(resRootCompositeEntity));
			assertEquals("hello world!", queryBack.myString);
			//assertEquals(1, queryBack.myJoinEntities.size());
//			ResJoinEntity child = queryBack.myJoinEntities.iterator().next();
//			assertEquals(resRootEntity.myId, child.myResource.myId);
//			//assertEquals(resRootEntity.myPartitionId, child.myPartitionId);
//			assertEquals("child", child.myString);
//
//			long count = em.createQuery("select count(*) from ResRootEntity", Long.class).getSingleResult();
//			ourLog.info("found {} roots", count);
//			assertEquals(1, count);
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
