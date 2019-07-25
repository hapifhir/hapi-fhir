package ca.uhn.fhir.jpa.demo.interceptor;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ca.uhn.fhir.jpa.demo.entity.User;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

public class RequestAuthorizationInterceptor extends InterceptorAdapter {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(RequestAuthorizationInterceptor.class);

	@PersistenceContext()
	private EntityManager myEntityManager;

	@Autowired
	private PlatformTransactionManager myPlatformTransactionManager;

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public boolean incomingRequestPreProcessed(HttpServletRequest theRequest, HttpServletResponse theResponse) {

		String authorization = theResponse.getHeader("Authorization");
		
		TypedQuery<User> q = myEntityManager.createQuery("SELECT u FROM User u WHERE u.myUsername = :username", User.class);
		String username = authorization;
		
		q.setParameter("username", username);
		try {
			User user = q.getSingleResult();
			ourLog.info("Found user [{}]: {}", username, user);
		} catch (NoResultException e) {
			ourLog.info("No user found in user table with username [{}]", username);
		}
		
		return true;
	}

}
