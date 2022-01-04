package ca.uhn.fhir.jpa.term.job;

/*-
 * #%L
 * HAPI FHIR JPA Server
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;

import static ca.uhn.fhir.jpa.batch.config.BatchConstants.JOB_PARAM_CODE_SYSTEM_VERSION_ID;

/**
 * This reader works as a pass-through by passing the received parameter once to the writer,
 * in order to share the writer functionality between two jobs
 */
public class BatchTermCodeSystemUniqueVersionDeleteReader implements ItemReader<Long> {
	private static final Logger ourLog = LoggerFactory.getLogger(BatchTermCodeSystemUniqueVersionDeleteReader.class);

	@Value("#{jobParameters['" + JOB_PARAM_CODE_SYSTEM_VERSION_ID  + "']}")
	private Long myTermCodeSystemVersionPid;

	// indicates if the parameter was already passed once to the writer, which indicates end of task
	private boolean myParameterPassed;


	@Override
	public Long read() throws Exception {
		if ( ! myParameterPassed) {
			myParameterPassed = true;
			return myTermCodeSystemVersionPid;
		}

		return null;
	}

}
