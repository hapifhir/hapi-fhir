/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 specification tests
 * %%
 * Copyright (C) 2014 - 2025 Smile CDR, Inc.
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
package ca.uhn.hapi.fhir.batch2.test.inline;

import ca.uhn.fhir.batch2.api.IJobDataSink;
import ca.uhn.fhir.batch2.api.IWarningProcessor;
import ca.uhn.fhir.batch2.model.WorkChunkData;
import ca.uhn.fhir.model.api.IModelJson;
import java.util.List;

/**
 * {@link IJobDataSink} implementation that collects the output of a batch job step into a list in a lightweight way.
 *
 * @param <OutputType> the type of the job step output
 */
class InlineJobDataSink<OutputType extends IModelJson> implements IJobDataSink<OutputType> {
    private final List<WorkChunkData<OutputType>> myCurrentOutput;

    InlineJobDataSink(List<WorkChunkData<OutputType>> theCurrentOutput) {
        myCurrentOutput = theCurrentOutput;
    }

	@Override
    public void accept(WorkChunkData<OutputType> theData) {
        myCurrentOutput.add(theData);
    }

    @Override
    public void recoveredError(String theMessage) {}

    @Override
    public void setWarningProcessor(IWarningProcessor theWarningProcessor) {}
}
