package ca.uhn.fhir.jpa.migrate.taskdef;

/*-
 * #%L
 * HAPI FHIR JPA Server - Migration
 * %%
 * Copyright (C) 2014 - 2020 University Health Network
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

import ca.uhn.fhir.util.VersionEnum;

public class CalculateOrdinalDatesTask extends BaseColumnCalculatorTask {

	public CalculateOrdinalDatesTask(VersionEnum theRelease, String theVersion) {
		super(theRelease, theVersion);
		setDescription("Calculate SP_LOW_VALUE_DATE and SP_HIGH_VALUE_DATE based on existing SP_LOW and SP_HIGH date values in Date Search Params");
	}
	@Override
	protected boolean shouldSkipTask() {
		return false; // TODO Is there a case where we should just not do this?
	}
}
