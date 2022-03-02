package ca.uhn.fhir.jpa.reindex;

import ca.uhn.fhir.jpa.api.svc.IResourceReindexSvc;
import ca.uhn.fhir.jpa.dao.data.IResourceTableDao;
import ca.uhn.fhir.rest.api.server.storage.ResourcePersistentId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	@Transactional
	public IdChunk fetchResourceIdsPage(Date theStart, Date theEnd) {

		Pageable page = Pageable.ofSize(20000);
		Slice<Object[]> slice = myResourceTableDao.findIdsTypesAndUpdateTimesOfResourcesWithinUpdatedRangeOrderedFromOldest(page, theStart, theEnd);

		List<Object[]> content = slice.getContent();
		if (content.isEmpty()) {
			return new IdChunk(Collections.emptyList(), Collections.emptyList(), null);
		}

		List<ResourcePersistentId> ids = content
			.stream()
			.map(t -> new ResourcePersistentId(t[0]))
			.collect(Collectors.toList());

		List<String> types = content
			.stream()
			.map(t -> (String) t[1])
			.collect(Collectors.toList());

		Date lastDate = (Date) content.get(content.size() - 1)[2];

		return new IdChunk(ids, types, lastDate);
	}
}
