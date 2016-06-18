package ca.uhn.fhir.jpa.search;

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

import ca.uhn.fhir.jpa.entity.TermConcept;

public class DeferConceptIndexingInterceptor  implements EntityIndexingInterceptor<TermConcept> {

	@Override
	public IndexingOverride onAdd(TermConcept theEntity) {
		if (theEntity.getIndexStatus() == null) {
			return IndexingOverride.SKIP;
		}
		return IndexingOverride.APPLY_DEFAULT;
	}

	@Override
	public IndexingOverride onUpdate(TermConcept theEntity) {
		return onAdd(theEntity);
	}

	@Override
	public IndexingOverride onDelete(TermConcept theEntity) {
		return IndexingOverride.APPLY_DEFAULT;
	}

	@Override
	public IndexingOverride onCollectionUpdate(TermConcept theEntity) {
		return IndexingOverride.APPLY_DEFAULT;
	}
}
