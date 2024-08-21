package ca.uhn.fhir.jpa.model.pkspike;

import ca.uhn.fhir.jpa.config.r4.FhirContextR4Config;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

/**
 * Spike to assess variable binding against a db.
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
	JPAConfig.class, FhirContextR4Config.class
})
public class JpaBindingTest {
	private static final Logger ourLog = LoggerFactory.getLogger(JpaBindingTest.class);

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

	@AfterEach
	void tearDown() {
		myJdbcTemplate.execute("delete from res_join");
		myJdbcTemplate.execute("delete from res_root");
	}

	@Test
	void roundTripResourceTable() {
		// given
		myJdbcTemplate.execute("insert into res_root values (-1, -1, 'hello!')");

		myTransactionTemplate.execute(status -> {
			var em = getEntityManagerOrThrow();
			long count = em.createQuery("select count(*) from ResRootEntity", Long.class).getSingleResult();
			assertEquals(1, count);

			em.createQuery("from ResRootEntity", ResRootEntity.class).getResultStream().forEach(e-> {
				assertEquals(-1, e.myId);
				assertEquals(-1, e.myPartitionId);
				assertEquals("hello!", e.getString());
			});
			return true;
		});
	}

	@Test
	void roundTripJoin() {
		// given

		myTransactionTemplate.execute(status -> {
			var em = getEntityManagerOrThrow();

			ResRootEntity resRootEntity = new ResRootEntity();
			resRootEntity.setString("hello world!");

			ResJoinEntity join = new ResJoinEntity();
			join.myResource = resRootEntity;
			join.myString="child";

			em.persist(resRootEntity);
			em.persist(join);
			em.flush();
			em.clear();

			ResRootEntity queryBack = em.find(ResRootEntity.class, resRootEntity.myId);
			assertEquals("hello world!", queryBack.myString);
			assertEquals(1, queryBack.myJoinEntities.size());
			ResJoinEntity child = queryBack.myJoinEntities.iterator().next();
			assertEquals(resRootEntity.myId, child.myResource.myId);
			//assertEquals(resRootEntity.myPartitionId, child.myPartitionId);
			assertEquals("child", child.myString);

			long count = em.createQuery("select count(*) from ResRootEntity", Long.class).getSingleResult();
			ourLog.info("found {} roots", count);
			assertEquals(1, count);
			return true;
		});
	}

	@Nonnull EntityManager getEntityManagerOrThrow() {
		return Objects.requireNonNull(EntityManagerFactoryUtils.getTransactionalEntityManager(myEntityManagerFactory));
	}
}
