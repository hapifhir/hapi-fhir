package ca.uhn.fhir.jpa.delete;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.jpa.api.model.DeleteConflict;
import ca.uhn.fhir.jpa.api.model.DeleteConflictList;
import ca.uhn.fhir.jpa.dao.BaseStorageDao;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
import ca.uhn.fhir.util.OperationOutcomeUtil;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

public final class DeleteConflictUtil {
	private DeleteConflictUtil() {
	}

	public static void validateDeleteConflictsEmptyOrThrowException(FhirContext theFhirContext, DeleteConflictList theDeleteConflicts) {
		IBaseOperationOutcome oo = null;
		String firstMsg = null;

		for (DeleteConflict next : theDeleteConflicts) {

			if (theDeleteConflicts.isResourceIdToIgnoreConflict(next.getTargetId())) {
				continue;
			}

			String msg = "Unable to delete " +
				next.getTargetId().toUnqualifiedVersionless().getValue() +
				" because at least one resource has a reference to this resource. First reference found was resource " +
				next.getSourceId().toUnqualifiedVersionless().getValue() +
				" in path " +
				next.getSourcePath();

			if (firstMsg == null) {
				firstMsg = msg;
				oo = OperationOutcomeUtil.newInstance(theFhirContext);
			}
			OperationOutcomeUtil.addIssue(theFhirContext, oo, BaseStorageDao.OO_SEVERITY_ERROR, msg, null, "processing");
		}

		if (firstMsg == null) {
			return;
		}

		throw new ResourceVersionConflictException(firstMsg, oo);
	}
}
