package ca.uhn.fhir.rest.server.provider;

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

import ca.uhn.fhir.rest.annotation.Operation;
import ca.uhn.fhir.rest.annotation.OperationParam;
import ca.uhn.fhir.rest.api.Constants;
import ca.uhn.fhir.rest.server.servlet.ServletRequestDetails;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseCoding;
import org.hl7.fhir.instance.model.api.IBaseReference;
import org.hl7.fhir.instance.model.api.IPrimitiveType;

import java.util.List;

/**
 * This class implements the Observation
 * <a href="http://hl7.org/fhir/observation-operation-lastn.html">$lastn</a> operation.
 * <p>
 * It is does not implement the actual storage logic for this operation, but can be
 * subclassed to provide this functionality.
 * </p>
 *
 * @since 4.1.0
 */
public abstract class BaseLastNProvider {

	@Operation(name = Constants.OPERATION_LASTN, typeName = "Observation", idempotent = true)
	public IBaseBundle lastN(
		ServletRequestDetails theRequestDetails,
		@OperationParam(name = "subject", typeName = "reference", min = 0, max = 1) IBaseReference theSubject,
		@OperationParam(name = "category", typeName = "coding", min = 0, max = OperationParam.MAX_UNLIMITED) List<IBaseCoding> theCategories,
		@OperationParam(name = "code", typeName = "coding", min = 0, max = OperationParam.MAX_UNLIMITED) List<IBaseCoding> theCodes,
		@OperationParam(name = "max", typeName = "integer", min = 0, max = 1) IPrimitiveType<Integer> theMax
	) {
		return processLastN(theSubject, theCategories, theCodes, theMax);
	}

	/**
	 * Subclasses should implement this method
	 */
	protected abstract IBaseBundle processLastN(IBaseReference theSubject, List<IBaseCoding> theCategories, List<IBaseCoding> theCodes, IPrimitiveType<Integer> theMax);


}
