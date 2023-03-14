package ca.uhn.fhir.jpa.util;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Utility class that provides a proxied entityManager.  It can be directly injected or
 * used as part of a bean creation process to provide a proxied entityManager through the constructor.
 */
public class PersistenceContextProvider {

	@PersistenceContext
	private EntityManager myEntityManager;

	public EntityManager getEntityManager() {
		return myEntityManager;
	}
}
