package ca.uhn.fhir.jpa.model.pkspike;

import jakarta.annotation.Nonnull;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Objects;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

abstract public class BasicEntityTestTemplate<R extends EntityFixture.IRootEntity<J>,J extends EntityFixture.IJoinEntity<R>> {
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

	@Test
	void roundTripResourceTable() {
		doInTx(em->{
			R root = myEntityFixture.buildRootEntity();
			root.setPartitionId(12);
			root.setString("goodbye!");
			em.persist(root);

			em.flush();
			em.clear();

			Object id = myEntityManagerFactory.getPersistenceUnitUtil().getIdentifier(root);
			R readback = em.find(myEntityFixture.myRootType, id);
			assertEquals(root.getResId(), readback.getResId());
			assertNotNull(readback.getResId());
			assertEquals(12, readback.getPartitionId());
			assertEquals("goodbye!", readback.getString());
		});
	}


	@Test
	void roundTripJoin() {
		doInTx(em->{
			var root = myEntityFixture.buildRootEntity();
			root.setPartitionId(12);
			root.setString("parent");

			var join = myEntityFixture.buildJoinEntity();
			join.setParent(root);
			join.setString("child");
			join.setPartitionId(12);
			em.persist(root);
			em.persist(join);

			em.flush();
			em.clear();

			Object id = myEntityManagerFactory.getPersistenceUnitUtil().getIdentifier(root);
			R readback = em.find(myEntityFixture.myRootType, id);

			assertEquals(root.getResId(), readback.getResId());
			assertNotNull(readback.getResId());
			assertEquals(12, readback.getPartitionId());
			assertEquals("parent", readback.getString());

			assertEquals(1, readback.getJoins().size());
			J joinReadback = readback.getJoins().iterator().next();
			assertNotNull(joinReadback);
			assertNotNull(joinReadback.getResId());
			assertEquals(root.getResId(), joinReadback.getResId());
			assertEquals(12, joinReadback.getPartitionId());
			assertEquals("child", joinReadback.getString());
		});

//
//			ResRootEntity queryBack = em.find(ResRootEntity.class, resRootEntity.myId);
//			assertEquals("hello world!", queryBack.myString);
//			assertEquals(1, queryBack.myJoinEntities.size());
//			ResJoinEntity child = queryBack.myJoinEntities.iterator().next();
//			assertEquals(resRootEntity.myId, child.myResource.myId);
//			assertEquals(resRootEntity.myPartitionId, child.myPartitionId);
//			assertEquals("child", child.myString);
//
//			long count = em.createQuery("select count(*) from ResRootPartitionEntity", Long.class).getSingleResult();
//			ourLog.info("found {} roots", count);
//			assertEquals(1, count);
//			return true;
//		});
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
