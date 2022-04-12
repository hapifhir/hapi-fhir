package ca.uhn.fhir.batch2.impl;

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

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.model.api.IModelJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class BaseDataSink<OT extends IModelJson> implements IJobDataSink<OT> {
	private static final Logger ourLog = LoggerFactory.getLogger(BaseDataSink.class);

	private final String myInstanceId;
	private final String myCurrentStepId;
	private int myRecoveredErrorCount;

	protected BaseDataSink(String theInstanceId, String theCurrentStepId) {
		myInstanceId = theInstanceId;
		myCurrentStepId = theCurrentStepId;
	}

	public String getInstanceId() {
		return myInstanceId;
	}

	@Override
	public void recoveredError(String theMessage) {
		ourLog.error("Error during job[{}] step[{}]: {}", myInstanceId, myCurrentStepId, theMessage);
		myRecoveredErrorCount++;
	}

	public int getRecoveredErrorCount() {
		return myRecoveredErrorCount;
	}

	public abstract int getWorkChunkCount();
}
