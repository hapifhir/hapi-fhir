/*-
 * #%L
 * HAPI-FHIR Storage Batch2 Jobs
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
package ca.uhn.fhir.batch2.jobs.imprt;

import ca.uhn.fhir.model.api.IModelJson;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NdJsonFileJson implements IModelJson {

	@JsonProperty("ndJsonText")
	private String myNdJsonText;

	@JsonProperty("sourceName")
	private String mySourceName;

	@JsonProperty("batchLineCount")
	private Integer myBatchLineCount;

	@JsonProperty("batchLineCountTotal")
	private Integer myBatchLineCountTotal;

	public String getNdJsonText() {
		return myNdJsonText;
	}

	public NdJsonFileJson setNdJsonText(String theNdJsonText) {
		myNdJsonText = theNdJsonText;
		return this;
	}

	public String getSourceName() {
		return mySourceName;
	}

	public void setSourceName(String theSourceName) {
		mySourceName = theSourceName;
	}

	/**
	 * This is the approximate line number out of the total number of lines in the
	 * entire batch load that corresponds to the source of this chunk.
	 * @since 8.2.0
	 */
	public void setBatchLineCount(Integer theBatchLineCount) {
		myBatchLineCount = theBatchLineCount;
	}

	/**
	 * This is the approximate line number out of the total number of lines in the
	 * entire batch load that corresponds to the source of this chunk.
	 * @since 8.2.0
	 */
	public Integer getBatchLineCount() {
		return myBatchLineCount;
	}

	/**
	 * This is total number of lines in the entire batch.
	 * @since 8.2.0
	 */
	public void setBatchLineCountTotal(Integer theBatchLineCountTotal) {
		myBatchLineCountTotal = theBatchLineCountTotal;
	}

	/**
	 * This is total number of lines in the entire batch.
	 * @since 8.2.0
	 */
	public Integer getBatchLineCountTotal() {
		return myBatchLineCountTotal;
	}
}
