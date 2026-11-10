/*-
 * #%L
 * HAPI FHIR - Server Framework
 * %%
 * Copyright (C) 2014 - 2026 Smile CDR, Inc.
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
package ca.uhn.fhir.rest.server.interceptor.validation;

import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.validation.ResultSeverityEnum;
import jakarta.annotation.Nonnull;

import java.util.List;

import static org.hl7.fhir.utilities.i18n.I18nConstants.REFERENCE_REF_QUERY_NOT_TRANSACTION;

/**
 * This interceptor changes the severity of the validation issue reported for a conditional reference (a reference
 * such as {@code Patient?identifier=http://example.org|123}) in a resource that is not inside a transaction Bundle.
 * <p>
 * The FHIR specification only allows conditional references in transactions, and the core validator reports any
 * other conditional reference as an error. HAPI FHIR JPA servers also resolve them on a plain create or update when
 * inline match URL references are allowed (see {@code StorageSettings#setAllowInlineMatchUrlReferences(boolean)},
 * enabled by default). A server that validates requests and relies on this can register this interceptor so that
 * those resources are not rejected.
 * </p>
 *
 * @since 9.2.0
 */
@Interceptor
public class ValidationMessageConditionalReferencePostProcessingInterceptor
		extends ValidationMessagePostProcessingInterceptor {

	/**
	 * Constructor, which reports conditional references outside a transaction as warnings
	 */
	public ValidationMessageConditionalReferencePostProcessingInterceptor() {
		this(ResultSeverityEnum.WARNING);
	}

	/**
	 * Constructor
	 *
	 * @param theDesiredSeverity the severity to report conditional references outside a transaction with
	 */
	public ValidationMessageConditionalReferencePostProcessingInterceptor(@Nonnull ResultSeverityEnum theDesiredSeverity) {
		super.addPostProcessingPatterns(new ValidationPostProcessingRuleJson(
				REFERENCE_REF_QUERY_NOT_TRANSACTION,
				null,
				List.of(ResultSeverityEnum.ERROR, ResultSeverityEnum.WARNING, ResultSeverityEnum.INFORMATION),
				null,
				theDesiredSeverity));
	}
}
