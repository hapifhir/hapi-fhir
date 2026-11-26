package ca.uhn.fhir.jpa.dao.r5.dbpartitionmode;

import ca.uhn.fhir.jpa.config.util.PartitionedIdJpaRepository;
import jakarta.persistence.EntityManager;
import org.springframework.data.envers.repository.support.EnversRevisionRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * Trial of a repository factory bean that makes {@code save()} persist new entities whose key is
 * {@link ca.uhn.fhir.jpa.model.entity.IdAndPartitionId}, instead of merging them.
 * <p>
 * The Envers repository factory is a private class, so its entity information can't be overridden. This factory bean
 * keeps that factory and gives it {@link PartitionedIdJpaRepository} as the repository base class instead.
 * </p>
 */
// Created by claude-fable-5-2
public class PartitionedIdRepositoryFactoryBean<
				T extends RevisionRepository<S, ID, N>, S, ID, N extends Number & Comparable<N>>
		extends EnversRevisionRepositoryFactoryBean<T, S, ID, N> {

	public PartitionedIdRepositoryFactoryBean(Class<? extends T> theRepositoryInterface) {
		super(theRepositoryInterface);
	}

	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager theEntityManager) {
		RepositoryFactorySupport retVal = super.createRepositoryFactory(theEntityManager);
		retVal.setRepositoryBaseClass(PartitionedIdJpaRepository.class);
		return retVal;
	}
}
