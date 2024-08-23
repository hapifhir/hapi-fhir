package ca.uhn.fhir.jpa.model.pkspike;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
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

	static List<Integer> getPartitions(EntityFixture theFixture) {
		var result = new ArrayList<Integer>();
		if (theFixture.isSupportNullPartitionId()) {
			result.add(null);
		}
		result.add(12);
		return result;
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
	@MethodSource("getPartitions")
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
	@MethodSource("getPartitions")
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

	@ParameterizedTest
	@MethodSource("getPartitions")
	void fetchJoinQuery(Integer thePartitionId) {
		doInTx(em -> {
			var root0 = myEntityFixture.buildRootEntity();
			root0.setPartitionId(thePartitionId);
			root0.setString("parent");

			var join0 = myEntityFixture.buildJoinEntity();
			join0.setParent(root0);
			join0.setString("child");
			join0.setPartitionId(thePartitionId);

			var join1 = myEntityFixture.buildJoinEntity();
			join1.setParent(root0);
			join1.setString("child1");
			join1.setPartitionId(thePartitionId);

			em.persist(root0);
			em.persist(join0);
			em.persist(join1);

			em.flush();
			em.clear();


			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<R> cr = cb.createQuery(myEntityFixture.myRootType);
			Root<R> from = cr.from(myEntityFixture.myRootType);
			from.fetch("myJoinEntities");
			cr.select(from);

			em.createQuery(cr).getResultStream()
				.forEach(e-> ourLog.info("e: {}", e));

		});
	}


	private void doInTx(Consumer<EntityManager> theCallback) {
		myTransactionTemplate.execute(status-> {
			theCallback.accept(getEntityManagerOrThrow());
			return null;
		});
	}

	private long queryCountAll(EntityManager em, Class<R> rootType) {
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
