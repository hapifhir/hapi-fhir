package ca.uhn.fhir.rest.api;

/*-
 * #%L
 * HAPI FHIR - Core Library
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

import javax.annotation.Nullable;

public class PreferHeader {

	private PreferReturnEnum myReturn;
	private boolean myRespondAsync;
	private PreferHandlingEnum myHanding;

	@Nullable
	public PreferReturnEnum getReturn() {
		return myReturn;
	}

	public PreferHeader setReturn(PreferReturnEnum theReturn) {
		myReturn = theReturn;
		return this;
	}

	public boolean getRespondAsync() {
		return myRespondAsync;
	}

	public PreferHeader setRespondAsync(boolean theRespondAsync) {
		myRespondAsync = theRespondAsync;
		return this;
	}

	@Nullable
	public PreferHandlingEnum getHanding() {
		return myHanding;
	}

	public void setHanding(PreferHandlingEnum theHanding) {
		myHanding = theHanding;
	}
}
