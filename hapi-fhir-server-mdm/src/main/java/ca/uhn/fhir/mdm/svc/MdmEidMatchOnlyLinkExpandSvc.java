package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.mdm.api.IMdmLinkExpandSvc;
import ca.uhn.fhir.mdm.model.CanonicalEID;
import ca.uhn.fhir.mdm.util.EIDHelper;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.api.server.storage.IResourcePersistentId;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;

import java.util.*;

public class MdmEidMatchOnlyLinkExpandSvc implements IMdmLinkExpandSvc {

	private DaoRegistry myDaoRegistry;
	private EIDHelper myEidHelper;

	// create constructor with args to initialize fields
	public MdmEidMatchOnlyLinkExpandSvc(DaoRegistry theDaoRegistry, EIDHelper theEidHelper) {
		myDaoRegistry = theDaoRegistry;
		myEidHelper = theEidHelper;
	}

	@Override
	public Set<String> expandMdmBySourceResourceId(RequestPartitionId theRequestPartitionId, IIdType theId) {
		// 1. Resolve resource
		String resourceType = theId.getResourceType();
		SystemRequestDetails srd = SystemRequestDetails.forRequestPartitionId(theRequestPartitionId);
		IBaseResource resource = myDaoRegistry.getResourceDao(resourceType).read(theId, srd);
		// 2. Extract EIDs from resource using EIDHelper
		List<CanonicalEID> eids = myEidHelper.getExternalEid(resource);
		// 3. Search for resources of same type with any of those EIDs
		Set<String> result = new HashSet<>();
		for (CanonicalEID eid : eids) {
			var map = new ca.uhn.fhir.jpa.searchparam.SearchParameterMap();
			map.add("identifier", new ca.uhn.fhir.rest.param.TokenParam(eid.getSystem(), eid.getValue()));
			List<IIdType> ids = myDaoRegistry.getResourceDao(resourceType).searchForResourceIds(map, srd);
			for (IIdType id : ids) {
				result.add(id.toUnqualifiedVersionless().getValue());
			}
		}
		// Always include the original resource
		result.add(theId.getValue());
		return result;
	}

	// Other IMdmLinkExpandSvc methods can throw UnsupportedOperationException for now
	@Override
	public Set<String> expandMdmBySourceResource(RequestPartitionId theRequestPartitionId, IBaseResource theResource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> expandMdmBySourceResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theSourceResourcePid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> expandMdmByGoldenResourceId(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> expandMdmByGoldenResourcePid(
			RequestPartitionId theRequestPartitionId, IResourcePersistentId<?> theGoldenResourcePid) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> expandMdmByGoldenResourceId(RequestPartitionId theRequestPartitionId, IIdType theId) {
		throw new UnsupportedOperationException();
	}
}
