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

	/**
	 * Step workers may call this 0..* times in order to provide output work chunks that
	 * will be passed to subsequent steps. Multiple invocations will result in multiple
	 * discrete chunks of work, each of which will be processed separately (and potentially
	 * in parallel) by the next step in the job definition.
	 * <p>
	 * This method may not be called by the final step worker and will result in an
	 * error.
	 * </p>
	 *
	 * @param theData The data to pass to the next step worker
	 */
	default void accept(OT theData) {
		accept(new WorkChunkData<>(theData));
	}

	/**
	 * Step workers may call this 0..* times in order to provide output work chunks that
	 * will be passed to subsequent steps. Multiple invocations will result in multiple
	 * discrete chunks of work, each of which will be processed separately (and potentially
	 * in parallel) by the next step in the job definition.
	 * <p>
	 * This method is not currently any different to calling {@link #accept(IModelJson)} other than
	 * the fact that it adds a wrapper object, but additional fields may be added to the
	 * wrapper in the future.
	 * </p>
	 * <p>
	 * This method may not be called by the final step worker and will result in an
	 * error.
	 * </p>
	 *
	 * @param theData The data to pass to the next step worker
	 */
	void accept(WorkChunkData<OT> theData);

	/**
	 * Step workers may invoke this method to indicate that an error occurred during
	 * processing but that it was successfully recovered, or it does not need to be
	 * recovered, or at least that it does not mean that processing should stop.
	 *
	 * @param theMessage An error message. This will be logged, and in the future it may be stored
	 */
	void recoveredError(String theMessage);

}
