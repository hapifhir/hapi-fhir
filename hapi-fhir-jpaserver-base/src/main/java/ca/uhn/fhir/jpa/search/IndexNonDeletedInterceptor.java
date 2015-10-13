package ca.uhn.fhir.jpa.search;

import org.hibernate.search.indexes.interceptor.EntityIndexingInterceptor;
import org.hibernate.search.indexes.interceptor.IndexingOverride;

import ca.uhn.fhir.jpa.entity.ResourceTable;

/**
 * Only store non-deleted resources
 */
public class IndexNonDeletedInterceptor implements EntityIndexingInterceptor<ResourceTable> {

	@Override
	public IndexingOverride onAdd(ResourceTable entity) {
		if (entity.getDeleted() == null) {
			return IndexingOverride.APPLY_DEFAULT;
		}
		return IndexingOverride.SKIP;
	}

	@Override
	public IndexingOverride onUpdate(ResourceTable entity) {
		if (entity.getDeleted() == null) {
			return IndexingOverride.UPDATE;
		}
		return IndexingOverride.REMOVE;
	}

	@Override
	public IndexingOverride onDelete(ResourceTable entity) {
		return IndexingOverride.APPLY_DEFAULT;
	}

	@Override
	public IndexingOverride onCollectionUpdate(ResourceTable entity) {
		return IndexingOverride.APPLY_DEFAULT;
	}
}