package ca.uhn.fhir.batch2.api;

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

import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.model.api.IModelJson;

public interface IJobDataSink<OT extends IModelJson> {

	default void accept(OT theData) {
		accept(new WorkChunkData<>(theData));
	}

	void accept(WorkChunkData<OT> theData);

	/**
	 * TODO JA: what is this?
	 */
	int getWorkChunkCount();

}
