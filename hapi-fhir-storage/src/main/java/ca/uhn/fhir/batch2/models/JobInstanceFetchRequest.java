package ca.uhn.fhir.batch2.models;

/*-
 * #%L
 * HAPI FHIR Storage api
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

import org.springframework.data.domain.Sort;

public class JobInstanceFetchRequest {

	/**
	 * Page index to start from.
	 */
	private int myPageStart;

	/**
	 * Page size (number of elements to return)
	 */
	private int myBatchSize;

	private Sort mySort;

	public int getPageStart() {
		return myPageStart;
	}

	public void setPageStart(int thePageStart) {
		myPageStart = thePageStart;
	}

	public int getBatchSize() {
		return myBatchSize;
	}

	public void setBatchSize(int theBatchSize) {
		myBatchSize = theBatchSize;
	}

	public Sort getSort() {
		return mySort;
	}

	public void setSort(Sort theSort) {
		mySort = theSort;
	}
}
