/*-
 * #%L
 * HAPI FHIR JPA Server - Batch2 Task Processor
 * %%
 * Copyright (C) 2014 - 2023 Smile CDR, Inc.
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

/**
 * Our distributed batch processing library.
 *
 * A running job corresponds to a {@link ca.uhn.fhir.batch2.model.JobInstance}.
 * Jobs are modeled as a sequence of steps, operating on {@link ca.uhn.fhir.batch2.model.WorkChunk}s
 * containing json data.  The first step is special -- it is empty, and the data is assumed to be the job parameters.
 * A {@link ca.uhn.fhir.batch2.model.JobDefinition} defines the sequence of {@link ca.uhn.fhir.batch2.model.JobDefinitionStep}s.
 * Each step defines the input chunk type, the output chunk type, and a procedure that receives the input and emits 0 or more outputs.
 * We have a special kind of final step called a reducer, which corresponds to the stream Collector concept.
 *
 * Design gaps:
 * <ul>
 *    <li> If the maintenance job is killed while sending notifications about
 *         a gated step advance, remaining chunks will never be notified.  A CREATED state before QUEUED would catch this.
 *         </li>
 * </ul>
 */
package ca.uhn.fhir.batch2;

