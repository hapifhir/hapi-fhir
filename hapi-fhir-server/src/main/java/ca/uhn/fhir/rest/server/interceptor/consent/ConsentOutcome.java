package ca.uhn.fhir.rest.server.interceptor.consent;

/*-
 * #%L
 * HAPI FHIR - Server Framework
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

import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.hl7.fhir.instance.model.api.IBaseResource;

public class ConsentOutcome {

	/**
	 * Convenience constant containing <code>new ConsentOutcome(ConsentOperationStatusEnum.PROCEED)</code>
	 */
	public static final ConsentOutcome PROCEED = new ConsentOutcome(ConsentOperationStatusEnum.PROCEED);
	/**
	 * Convenience constant containing <code>new ConsentOutcome(ConsentOperationStatusEnum.REJECT)</code>
	 */
	public static final ConsentOutcome REJECT = new ConsentOutcome(ConsentOperationStatusEnum.REJECT);
	/**
	 * Convenience constant containing <code>new ConsentOutcome(ConsentOperationStatusEnum.AUTHORIZED)</code>
	 */
	public static final ConsentOutcome AUTHORIZED = new ConsentOutcome(ConsentOperationStatusEnum.AUTHORIZED);

	private final ConsentOperationStatusEnum myStatus;
	private final IBaseOperationOutcome myOperationOutcome;
	private final IBaseResource myResource;

	public ConsentOutcome(ConsentOperationStatusEnum theStatus) {
		this(theStatus, null, null);
	}

	public ConsentOutcome(ConsentOperationStatusEnum theStatus, IBaseOperationOutcome theOperationOutcome) {
		this(theStatus, theOperationOutcome, null);
	}

	public ConsentOutcome(ConsentOperationStatusEnum theStatus, IBaseResource theResource) {
		this(theStatus, null, theResource);
	}

	private ConsentOutcome(ConsentOperationStatusEnum theStatus, IBaseOperationOutcome theOperationOutcome, IBaseResource theResource) {
		Validate.notNull(theStatus, "theStatus must not be null");
		Validate.isTrue(!(theOperationOutcome != null && theResource != null), "theOperationOutcome and theResource must not both be null");
		myStatus = theStatus;
		myOperationOutcome = theOperationOutcome;
		myResource = theResource;
	}

	public ConsentOperationStatusEnum getStatus() {
		return myStatus;
	}

	public IBaseOperationOutcome getOperationOutcome() {
		return myOperationOutcome;
	}

	public IBaseResource getResource() {
		return myResource;
	}

}
