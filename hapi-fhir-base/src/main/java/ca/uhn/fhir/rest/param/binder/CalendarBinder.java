package ca.uhn.fhir.rest.param.binder;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2019 University Health Network
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

import java.util.Calendar;

import ca.uhn.fhir.model.primitive.InstantDt;

public final class CalendarBinder extends BaseJavaPrimitiveBinder<Calendar> {
	public CalendarBinder() {
	}

	@Override
	protected String doEncode(Calendar theString) {
		return new InstantDt(theString).getValueAsString();
	}

	@Override
	protected Calendar doParse(String theString) {
		return new InstantDt(theString).getValueAsCalendar();
	}


}
