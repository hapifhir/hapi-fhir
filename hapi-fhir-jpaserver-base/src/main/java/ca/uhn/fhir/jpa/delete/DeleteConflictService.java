package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.dao.BaseHapiFhirDao;
import ca.uhn.fhir.jpa.dao.DaoConfig;
import ca.uhn.fhir.jpa.dao.data.IResourceLinkDao;
import ca.uhn.fhir.jpa.model.entity.ResourceLink;
import ca.uhn.fhir.jpa.model.entity.ResourceTable;
import ca.uhn.fhir.jpa.util.DeleteConflict;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class DeleteConflictService {
	private static final Logger ourLog = LoggerFactory.getLogger(DeleteConflictService.class);

	@PersistenceContext(type = PersistenceContextType.TRANSACTION)
	protected EntityManager myEntityManager;
	@Autowired
	DaoConfig myDaoConfig;
	@Autowired
	protected IResourceLinkDao myResourceLinkDao;
	@Autowired
	private FhirContext myFhirContext;

	public void validateOkToDelete(List<DeleteConflict> theDeleteConflicts, ResourceTable theEntity, boolean theForValidate) {
		TypedQuery<ResourceLink> query = myEntityManager.createQuery("SELECT l FROM ResourceLink l WHERE l.myTargetResourcePid = :target_pid", ResourceLink.class);
		query.setParameter("target_pid", theEntity.getId());
		query.setMaxResults(1);
		List<ResourceLink> resultList = query.getResultList();
		if (resultList.isEmpty()) {
			return;
		}

		if (myDaoConfig.isEnforceReferentialIntegrityOnDelete() == false && !theForValidate) {
			ourLog.debug("Deleting {} resource dependencies which can no longer be satisfied", resultList.size());
			myResourceLinkDao.deleteAll(resultList);
			return;
		}

		ResourceLink link = resultList.get(0);
		IdDt targetId = theEntity.getIdDt();
		IdDt sourceId = link.getSourceResource().getIdDt();
		String sourcePath = link.getSourcePath();

		theDeleteConflicts.add(new DeleteConflict(sourceId, sourcePath, targetId));
	}

	public void validateDeleteConflictsEmptyOrThrowException(List<DeleteConflict> theDeleteConflicts) {
		if (theDeleteConflicts.isEmpty()) {
			return;
		}

		IBaseOperationOutcome oo = OperationOutcomeUtil.newInstance(myFhirContext);
		String firstMsg = null;
		for (DeleteConflict next : theDeleteConflicts) {
			StringBuilder b = new StringBuilder();
			b.append("Unable to delete ");
			b.append(next.getTargetId().toUnqualifiedVersionless().getValue());
			b.append(" because at least one resource has a reference to this resource. First reference found was resource ");
			b.append(next.getSourceId().toUnqualifiedVersionless().getValue());
			b.append(" in path ");
			b.append(next.getSourcePath());
			String msg = b.toString();
			if (firstMsg == null) {
				firstMsg = msg;
			}
			OperationOutcomeUtil.addIssue(myFhirContext, oo, BaseHapiFhirDao.OO_SEVERITY_ERROR, msg, null, "processing");
		}

		throw new ResourceVersionConflictException(firstMsg, oo);
	}
}
