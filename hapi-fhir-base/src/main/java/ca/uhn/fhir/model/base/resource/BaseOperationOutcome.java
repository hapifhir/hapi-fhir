package ca.uhn.fhir.model.base.resource;

/*
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

import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.IResourceBlock;
import ca.uhn.fhir.model.primitive.CodeDt;
import ca.uhn.fhir.model.primitive.StringDt;

public interface BaseOperationOutcome extends IResource, IBaseOperationOutcome {

	public abstract BaseIssue addIssue();

	public abstract List<? extends BaseIssue> getIssue();

	public abstract BaseIssue getIssueFirstRep();

	public static abstract class BaseIssue extends BaseIdentifiableElement implements IResourceBlock {
	
		private static final long serialVersionUID = 6700020892151450738L;

		public abstract CodeDt getSeverityElement();
		
		public abstract StringDt getDetailsElement();

		public abstract BaseIssue addLocation(String theString);

		/**
		 * @deprecated Use {@link #setDiagnostics(String)} instead - Field was renamed in DSTU2
		 */
		@Deprecated
		public abstract BaseIssue setDetails(String theString);

		public abstract BaseIssue setDiagnostics(String theString);

		public abstract StringDt getLocationFirstRep();
	}


	
}
