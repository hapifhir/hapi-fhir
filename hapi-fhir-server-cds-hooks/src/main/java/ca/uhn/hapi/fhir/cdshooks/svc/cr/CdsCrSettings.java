/*-
 * #%L
 * HAPI FHIR - CDS Hooks
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
package ca.uhn.hapi.fhir.cdshooks.svc.cr;

public class CdsCrSettings {
	private final String DEFAULT_CLIENT_ID_HEADER_NAME = "client_id";
	private String myClientIdHeaderName;

	public static CdsCrSettings getDefault() {
		CdsCrSettings settings = new CdsCrSettings();
		settings.setClientIdHeaderName(settings.DEFAULT_CLIENT_ID_HEADER_NAME);
		return settings;
	}

	public void setClientIdHeaderName(String theName) {
		myClientIdHeaderName = theName;
	}

	public String getClientIdHeaderName() {
		return myClientIdHeaderName;
	}

	public CdsCrSettings withClientIdHeaderName(String theName) {
		myClientIdHeaderName = theName;
		return this;
	}
}
