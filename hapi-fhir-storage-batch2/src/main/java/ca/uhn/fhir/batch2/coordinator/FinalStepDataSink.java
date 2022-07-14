package ca.uhn.fhir.batch2.coordinator;

/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
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

import ca.uhn.fhir.batch2.api.JobExecutionFailedException;
import ca.uhn.fhir.batch2.api.VoidModel;
import ca.uhn.fhir.batch2.model.JobWorkCursor;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.model.api.IModelJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

class FinalStepDataSink<PT extends IModelJson, IT extends IModelJson> extends BaseDataSink<PT,IT,VoidModel> {
	private static final Logger ourLog = LoggerFactory.getLogger(FinalStepDataSink.class);

	/**
	 * Constructor
	 */
	FinalStepDataSink(@Nonnull String theJobDefinitionId, @Nonnull String theInstanceId, @Nonnull JobWorkCursor<PT,IT,VoidModel> theJobWorkCursor) {
		super(theInstanceId, theJobWorkCursor);
	}

	@Override
	public void accept(WorkChunkData<VoidModel> theData) {
		String msg = "Illegal attempt to store data during final step of job " + myJobDefinitionId;
		ourLog.error(msg);
		throw new JobExecutionFailedException(Msg.code(2045) + msg);
	}

	@Override
	public int getWorkChunkCount() {
		return 0;
	}
}
