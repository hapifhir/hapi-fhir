package ca.uhn.fhir.jpa.model.sched;

/*-
 * #%L
 * hapi-fhir-jpa
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

public interface ISmartLifecyclePhase {
	// POST_CONSTRUCT is here as a marker for where post-construct fits into the smart lifecycle.  Beans with negative phases
	// will be started before @PostConstruct are called
	int POST_CONSTRUCT = 0;

	// We want to start scheduled tasks fairly late in the startup process
	int SCHEDULER_1000 = 1000;
}
