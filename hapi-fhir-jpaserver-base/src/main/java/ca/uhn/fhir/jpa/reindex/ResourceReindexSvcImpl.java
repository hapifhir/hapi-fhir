package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.Date;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class ResourceReindexSvcImpl implements IResourceReindexSvc {

	@Autowired
	private IResourceTableDao myResourceTableDao;

	@Override
	@Transactional
	public Date getOldestTimestamp(@Nullable String theResourceType) {
		Slice<Date> slice;
		Pageable page = Pageable.ofSize(1);
		if (isBlank(theResourceType)) {
			slice = myResourceTableDao.findUpdatedDatesOfResourcesOrderedFromOldest(page);
		} else {
			slice = myResourceTableDao.findUpdatedDatesOfResourcesOrderedFromOldest(page, theResourceType);
		}

		return slice.get().findFirst().map(t -> new Date(t.getTime())).orElse(null);
	}
}
