package ca.uhn.fhir.jpa.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.engine.spi.SessionImplementor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;
import org.springframework.transaction.support.ResourceHolderSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;

import static org.springframework.orm.jpa.EntityManagerFactoryUtils.ENTITY_MANAGER_SYNCHRONIZATION_ORDER;
import static org.springframework.util.ReflectionUtils.invokeMethod;

/**
 * Hibernate's {@link StatelessSession} factory which will be bound to the
 * current transaction. This factory returns a Proxy which delegates method
 * calls to the underlying {@link StatelessSession} bound to transaction. At the
 * end of the transaction the session is automatically closed. This class
 * borrows idea's from {@link DataSourceUtils},
 * {@link EntityManagerFactoryUtils}, {@link ResourceHolderSynchronization} and
 * {@link LocalEntityManagerFactoryBean}.
 */
public class StatelessSessionFactoryBean implements FactoryBean<StatelessSession> {

	private SessionFactory sessionFactory;

	@Autowired
	public StatelessSessionFactoryBean(SessionFactory theSessionFactory) {
		this.sessionFactory = theSessionFactory;
	}

	@Override
	public StatelessSession getObject() throws Exception {
		StatelessSessionInterceptor statelessSessionInterceptor = new StatelessSessionInterceptor(
			 sessionFactory);
		return ProxyFactory.getProxy(StatelessSession.class, statelessSessionInterceptor);
	}

	@Override
	public Class<?> getObjectType() {
		return StatelessSession.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	/**
	 * Use this to override the {@link SessionFactory} obtained from the
	 * {@link EntityManagerFactory}. Please note that the connection will still
	 * be used from the {@link EntityManager}.
	 */
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	private static class StatelessSessionInterceptor implements MethodInterceptor {

		private final SessionFactory sessionFactory;

		public StatelessSessionInterceptor(
													  SessionFactory sessionFactory) {
			this.sessionFactory = sessionFactory;
		}

		private void bindWithTransaction(StatelessSession statelessSession) {
			TransactionSynchronizationManager
				.registerSynchronization(new StatelessSessionSynchronization(sessionFactory,
					statelessSession));
			TransactionSynchronizationManager.bindResource(sessionFactory, statelessSession);
		}

		private StatelessSession getCurrentSession() {
			if (!TransactionSynchronizationManager.isActualTransactionActive()) {
				throw new IllegalStateException(
					"There should be an active transaction for the current thread.");
			}
			StatelessSession statelessSession = (StatelessSession) TransactionSynchronizationManager
				.getResource(sessionFactory);
			if (statelessSession == null) {
				statelessSession = openNewStatelessSession();
				bindWithTransaction(statelessSession);
			}
			return statelessSession;
		}

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			StatelessSession statelessSession = getCurrentSession();
			return invokeMethod(invocation.getMethod(), statelessSession, invocation.getArguments());
		}

		/**
		 * It is important we obtain the physical (real) connection otherwise it
		 * will be double proxied and there will be problems releasing the
		 * connection.
		 */
		private Connection obtainPhysicalConnection() {
			EntityManager entityManager = EntityManagerFactoryUtils
				.getTransactionalEntityManager(sessionFactory);
			SessionImplementor sessionImplementor = (SessionImplementor) entityManager
				.getDelegate();
//			return sessionImplementor.getTransactionCoordinator().getJdbcCoordinator()
//				.getLogicalConnection().getConnection();
			return null;
		}

		private StatelessSession openNewStatelessSession() {
			Connection connection = obtainPhysicalConnection();
			return sessionFactory.openStatelessSession(connection);
		}
	}

	private static class StatelessSessionSynchronization extends TransactionSynchronizationAdapter {

		private final SessionFactory sessionFactory;
		private final StatelessSession statelessSession;

		public StatelessSessionSynchronization(SessionFactory sessionFactory,
															StatelessSession statelessSession) {
			this.sessionFactory = sessionFactory;
			this.statelessSession = statelessSession;
		}

		@Override
		public void beforeCommit(boolean readOnly) {
			if (!readOnly) {
//				((TransactionContext) statelessSession).flu
			}
		}

		@Override
		public void beforeCompletion() {
			TransactionSynchronizationManager.unbindResource(sessionFactory);
			statelessSession.close();
		}

		@Override
		public int getOrder() {
			return ENTITY_MANAGER_SYNCHRONIZATION_ORDER - 100;
		}

	}

}
