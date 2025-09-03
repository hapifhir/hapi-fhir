package ca.uhn.fhir.mdm.svc;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.interceptor.model.RequestPartitionId;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import ca.uhn.fhir.jpa.api.svc.IIdHelperService;
import ca.uhn.fhir.jpa.api.svc.ResolveIdentityMode;
import ca.uhn.fhir.jpa.model.dao.JpaPid;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.util.FhirTerser;
import org.hl7.fhir.instance.model.api.IBase;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BulkExportMDMEidMatchOnlyResourceExpander implements IBulkExportMDMResourceExpander {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Autowired
	private MdmEidMatchOnlyLinkExpandSvc myMdmEidMatchOnlyLinkExpandSvc;

	@Autowired
	private FhirContext myFhirContext;

	@Autowired
	private IIdHelperService<JpaPid> myIdHelperService;

	@Override
	public Set<JpaPid> expandGroup(String groupResourceId, RequestPartitionId requestPartitionId) {
		// Read the Group resource
		SystemRequestDetails srd = SystemRequestDetails.forRequestPartitionId(requestPartitionId);
		IIdType groupId = myFhirContext.getVersion().newIdType(groupResourceId);
		IFhirResourceDao<?> groupDao = myDaoRegistry.getResourceDao("Group");
		IBaseResource groupResource = groupDao.read(groupId, srd);

		Set<String> allResourceIds = new HashSet<>();
		FhirTerser terser = myFhirContext.newTerser();
		// Extract all member.entity references from the Group resource
		List<IBase> memberEntities = terser.getValues(groupResource, "Group.member.entity");
		for (IBase entity : memberEntities) {
			if (entity instanceof IBaseReference entityRef) {
				if (!entityRef.getReferenceElement().isEmpty()) {
					IIdType memberId = entityRef.getReferenceElement();
					Set<String> expanded =
							myMdmEidMatchOnlyLinkExpandSvc.expandMdmBySourceResourceId(requestPartitionId, memberId);
					allResourceIds.addAll(expanded);
				}
			}
		}
		// Convert all resourceIds to IIdType and resolve in batch
		List<IIdType> idTypes = allResourceIds.stream()
				.map(id -> myFhirContext.getVersion().newIdType(id))
				.collect(Collectors.toList());
		List<JpaPid> pidList = myIdHelperService.resolveResourcePids(
				requestPartitionId,
				idTypes,
				ResolveIdentityMode.excludeDeleted().cacheOk());
		return new HashSet<>(pidList);
	}

	@Override
	public void annotateResource(IBaseResource resource) {
		// nothing to do
	}
}
