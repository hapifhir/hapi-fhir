package ca.uhn.fhir.jpa.bulk.export.job;

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
import ca.uhn.fhir.jpa.batch.config.BatchConstants;
import org.slf4j.Logger;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;

import static org.slf4j.LoggerFactory.getLogger;

public class GroupIdPresentValidator implements JobParametersValidator {
	private static final Logger ourLog = getLogger(GroupIdPresentValidator.class);

	@Override
	public void validate(JobParameters theJobParameters) throws JobParametersInvalidException {

		if (theJobParameters == null || theJobParameters.getString(BatchConstants.BULK_EXPORT_GROUP_ID_PARAMETER) == null) {
			throw new JobParametersInvalidException(Msg.code(514) + "Group Bulk Export jobs must have a " + BatchConstants.BULK_EXPORT_GROUP_ID_PARAMETER + " attribute");
		} else {
			ourLog.debug("detected we are running in group mode with group id [{}]", theJobParameters.getString(BatchConstants.BULK_EXPORT_GROUP_ID_PARAMETER));
		}
	}
}
