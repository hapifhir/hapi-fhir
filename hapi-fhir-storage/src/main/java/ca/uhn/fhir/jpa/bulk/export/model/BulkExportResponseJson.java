/*-
 * #%L
 * HAPI FHIR Storage api
 * %%
 * Copyright (C) 2014 - 2024 Smile CDR, Inc.
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
package ca.uhn.fhir.jpa.bulk.export.model;

import ca.uhn.fhir.model.api.IModelJson;
import ca.uhn.fhir.rest.server.util.JsonDateDeserializer;
import ca.uhn.fhir.rest.server.util.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
@JsonAutoDetect(
		creatorVisibility = JsonAutoDetect.Visibility.NONE,
		fieldVisibility = JsonAutoDetect.Visibility.NONE,
		getterVisibility = JsonAutoDetect.Visibility.NONE,
		isGetterVisibility = JsonAutoDetect.Visibility.NONE,
		setterVisibility = JsonAutoDetect.Visibility.NONE)
public class BulkExportResponseJson {

	@JsonProperty("transactionTime")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date myTransactionTime;

	@JsonProperty("request")
	private String myRequest;

	@JsonProperty("requiresAccessToken")
	private Boolean myRequiresAccessToken;

	@JsonProperty("output")
	private List<Output> myOutput;

	/*
	 * Note that we override the include here as ONC regulations require that we actually serialize the empty error array.
	 */
	@JsonInclude
	@JsonProperty("error")
	private List<Output> myError = new ArrayList<>();

	@JsonProperty("message")
	private String myMsg;

	public Date getTransactionTime() {
		return myTransactionTime;
	}

	public BulkExportResponseJson setTransactionTime(Date theTransactionTime) {
		myTransactionTime = theTransactionTime;
		return this;
	}

	public String getRequest() {
		return myRequest;
	}

	public BulkExportResponseJson setRequest(String theRequest) {
		myRequest = theRequest;
		return this;
	}

	public Boolean getRequiresAccessToken() {
		return myRequiresAccessToken;
	}

	public BulkExportResponseJson setRequiresAccessToken(Boolean theRequiresAccessToken) {
		myRequiresAccessToken = theRequiresAccessToken;
		return this;
	}

	public List<Output> getOutput() {
		if (myOutput == null) {
			myOutput = new ArrayList<>();
		}
		return myOutput;
	}

	public List<Output> getError() {
		if (myError == null) {
			myError = new ArrayList<>();
		}
		return myError;
	}

	public Output addOutput() {
		Output retVal = new Output();
		getOutput().add(retVal);
		return retVal;
	}

	public String getMsg() {
		return myMsg;
	}

	public void setMsg(String theMsg) {
		myMsg = theMsg;
	}

	public static class Output implements IModelJson {

		@JsonProperty("type")
		private String myType;

		@JsonProperty("url")
		private String myUrl;

		public String getType() {
			return myType;
		}

		public Output setType(String theType) {
			myType = theType;
			return this;
		}

		public String getUrl() {
			return myUrl;
		}

		public Output setUrl(String theUrl) {
			myUrl = theUrl;
			return this;
		}
	}
}
