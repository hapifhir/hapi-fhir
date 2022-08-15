package ca.uhn.fhir.interceptor.model;

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

import java.util.List;

/**
 * This is an experimental API, use with caution
 */
public class TransactionWriteOperationsDetails {

	private List<String> myConditionalCreateRequestUrls;
	private List<String> myUpdateRequestUrls;

	public List<String> getConditionalCreateRequestUrls() {
		return myConditionalCreateRequestUrls;
	}

	public void setConditionalCreateRequestUrls(List<String> theConditionalCreateRequestUrls) {
		myConditionalCreateRequestUrls = theConditionalCreateRequestUrls;
	}

	public List<String> getUpdateRequestUrls() {
		return myUpdateRequestUrls;
	}

	public void setUpdateRequestUrls(List<String> theUpdateRequestUrls) {
		myUpdateRequestUrls = theUpdateRequestUrls;
	}

}
