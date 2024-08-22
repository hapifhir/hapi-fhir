package ca.uhn.fhir.jpa.model.pkspike;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract public class BasicEntityTestTemplate<R extends EntityFixture.IRootEntity<J>,J extends EntityFixture.IJoinEntity<R>> {
	private static final Logger ourLog = LoggerFactory.getLogger(BasicEntityTestTemplate.class);

	@Autowired
	EntityManagerFactory myEntityManagerFactory;

	@Autowired
	TransactionTemplate myTransactionTemplate;

	@Autowired
	JdbcTemplate myJdbcTemplate;

	@RegisterExtension
	SchemaCleanerExtension mySchemaCleanerExtension = new SchemaCleanerExtension();

	final EntityFixture<R,J> myEntityFixture;

	public BasicEntityTestTemplate(EntityFixture<R,J> theEntityFixture) {
		myEntityFixture = theEntityFixture;
	}

	List<Integer> getPartitions() {
		return List.of(null, 12);
	}

	@Test
	void  rootEntityBoundToTable() {
		// given
		myJdbcTemplate.execute("insert into res_root(res_id, partition_id, string_col) values (-1, -1, 'hello!')");

		doInTx(em->{
			long count = queryCountAll(em, myEntityFixture.myRootType);

			assertEquals(1, count);

			CriteriaBuilder cb = em.getCriteriaBuilder();

			CriteriaQuery<R> cr = cb.createQuery(myEntityFixture.myRootType);
			cr.select(cr.from(myEntityFixture.myRootType));
			R readback = em.createQuery(cr).getSingleResult();

			assertEquals(-1, readback.getResId());
			assertEquals(-1, readback.getPartitionId());
			assertEquals("hello!", readback.getString());
		});
	}

	@ParameterizedTest
	@ValueSource(ints = 12)
	@NullSource
	void roundTripResourceTable(Integer thePartitionId) {
		doInTx(em->{
			R root = myEntityFixture.buildRootEntity();
			root.setPartitionId(thePartitionId);
			root.setString("goodbye!");
			em.persist(root);

			em.flush();
			em.clear();

			Object id = myEntityManagerFactory.getPersistenceUnitUtil().getIdentifier(root);
			ourLog.info("flushed root entity.  Id is {}", id);
			R readback = em.find(myEntityFixture.myRootType, id);
			assertNotNull(readback);
			assertEquals(root.getResId(), readback.getResId());
			assertNotNull(readback.getResId());
			assertEquals(thePartitionId, readback.getPartitionId());
			assertEquals("goodbye!", readback.getString());
		});
	}


	@ParameterizedTest
	@ValueSource(ints = 12)
	@NullSource
	void roundTripJoin(Integer thePartitionId) {
		doInTx(em->{
			var root = myEntityFixture.buildRootEntity();
			root.setPartitionId(thePartitionId);
			root.setString("parent");

			var join = myEntityFixture.buildJoinEntity();
			join.setParent(root);
			join.setString("child");
			join.setPartitionId(thePartitionId);
			em.persist(root);
			em.persist(join);

			em.flush();
			em.clear();

			Object id = myEntityManagerFactory.getPersistenceUnitUtil().getIdentifier(root);
			ourLog.info("flushed root entity.  Id is {}", id);
			R readback = em.find(myEntityFixture.myRootType, id);

			assertNotNull(readback);
			assertEquals(root.getResId(), readback.getResId());
			assertNotNull(readback.getResId());
			assertEquals(thePartitionId, readback.getPartitionId());
			assertEquals("parent", readback.getString());

			Collection<J> joins = readback.getJoins();
			assertNotNull(joins);
			assertEquals(1, joins.size());
			J joinReadback = joins.iterator().next();
			assertNotNull(joinReadback);
			assertNotNull(joinReadback.getResId());
			assertEquals(root.getResId(), joinReadback.getResId());
			assertEquals(thePartitionId, joinReadback.getPartitionId());
			assertEquals("child", joinReadback.getString());
		});
	}

	private void doInTx(Consumer<EntityManager> theCallback) {
		myTransactionTemplate.execute(status-> {
			theCallback.accept(getEntityManagerOrThrow());
			return null;
		});
	}

	private static long queryCountAll(EntityManager em, Class<? extends EntityFixture.IRootEntity> rootType) {
		CriteriaBuilder qb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = qb.createQuery(Long.class);
		CriteriaQuery<Long> select = cq.select(qb.count(cq.from(rootType)));
		long count = em.createQuery(select).getSingleResult();
		return count;
	}

	@Nonnull
	EntityManager getEntityManagerOrThrow() {
		return Objects.requireNonNull(EntityManagerFactoryUtils.getTransactionalEntityManager(myEntityManagerFactory));
	}

}
