package ca.uhn.fhir.model.api;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 - 2015 University Health Network
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
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

public enum TemporalPrecisionEnum {

	YEAR(Calendar.YEAR) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addYears(theInput, theAmount);
		}
	},
	
	MONTH(Calendar.MONTH) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addMonths(theInput, theAmount);
		}
	},
	DAY(Calendar.DATE) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addDays(theInput, theAmount);
		}
	},
	SECOND(Calendar.SECOND) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addSeconds(theInput, theAmount);
		}
	},
	
	MILLI(Calendar.MILLISECOND) {
		@Override
		public Date add(Date theInput, int theAmount) {
			return DateUtils.addMilliseconds(theInput, theAmount);
		}
	},
	
	;
	
	private int myCalendarConstant;

	TemporalPrecisionEnum(int theCalendarConstant) {
		myCalendarConstant = theCalendarConstant;
	}

	public abstract Date add(Date theInput, int theAmount);
	
	public int getCalendarConstant() {
		return myCalendarConstant;
	}

}
