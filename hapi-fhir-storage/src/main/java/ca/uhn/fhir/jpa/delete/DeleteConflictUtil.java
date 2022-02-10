package ca.uhn.fhir.jpa.delete;

/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.i18n.Msg;
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

		throw new ResourceVersionConflictException(Msg.code(515) + firstMsg, oo);
	}
}
