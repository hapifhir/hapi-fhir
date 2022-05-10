package ca.uhn.fhir.validation;

import org.hl7.fhir.instance.model.api.IBaseResource;


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

/**
 * An individual validation module, which applies validation rules against
 * resources and adds failure/informational messages as it goes.
 * 
 * See <a href="https://hapifhir.io/hapi-fhir/docs/validation/introduction.html">Validation</a>
 * for a list of available modules. You may also create your own.
 */
public interface IValidatorModule {

	/**
	 * Validate the actual resource.
	 * 
	 * The {@link IValidationContext} can be used to access the resource being validated,
	 * and is populated with the results.
	 */
	void validateResource(IValidationContext<IBaseResource> theCtx);

}
