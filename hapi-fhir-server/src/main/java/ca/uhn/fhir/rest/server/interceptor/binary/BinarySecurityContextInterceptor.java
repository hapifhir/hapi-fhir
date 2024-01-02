/*-
 * #%L
 * HAPI FHIR - Server Framework
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
package ca.uhn.fhir.rest.server.interceptor.binary;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.api.server.IPreResourceShowDetails;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.api.server.SystemRequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationConstants;
import ca.uhn.fhir.util.FhirTerser;
import org.apache.commons.lang3.Validate;
import org.hl7.fhir.instance.model.api.IBaseBinary;
import org.hl7.fhir.instance.model.api.IBaseResource;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * This security interceptor checks any Binary resources that are being exposed to
 * a user and can forbid the user from accessing them based on the security context
 * found in <code>Binary.securityContext.identifier</code>.
 * <p>
 * This interceptor is intended to be subclassed. The default implementation if it
 * is not subclassed will reject any access to a Binary resource unless the
 * request is a system request (using {@link SystemRequestDetails} or the Binary
 * resource has no value in <code>Binary.securityContext.identifier</code>.
 * </p>
 * <p>
 * Override {@link #securityContextIdentifierAllowed(String, String, RequestDetails)} in order
 * to allow the user to access specific context values.
 * </p>
 *
 * @since 6.8.0
 */
@SuppressWarnings("unused")
@Interceptor(order = AuthorizationConstants.ORDER_BINARY_SECURITY_INTERCEPTOR)
public class BinarySecurityContextInterceptor {

	private final FhirContext myFhirContext;

	/**
	 * Constructor
	 *
	 * @param theFhirContext The FHIR context
	 */
	public BinarySecurityContextInterceptor(FhirContext theFhirContext) {
		Validate.notNull(theFhirContext, "theFhirContext must not be null");
		myFhirContext = theFhirContext;
	}

	/**
	 * Interceptor hook method. Do not call this method directly.
	 */
	@Hook(Pointcut.STORAGE_PRESHOW_RESOURCES)
	public void preShowResources(IPreResourceShowDetails theShowDetails, RequestDetails theRequestDetails) {
		for (IBaseResource nextResource : theShowDetails.getAllResources()) {
			if (nextResource instanceof IBaseBinary) {
				applyAccessControl((IBaseBinary) nextResource, theRequestDetails);
			}
		}
	}

	/**
	 * Interceptor hook method. Do not call this method directly.
	 */
	@Hook(Pointcut.STORAGE_PRECOMMIT_RESOURCE_UPDATED)
	public void preShowResources(
			IBaseResource theOldValue, IBaseResource theNewValue, RequestDetails theRequestDetails) {
		if (theOldValue instanceof IBaseBinary) {
			applyAccessControl((IBaseBinary) theOldValue, theRequestDetails);
		}
	}

	/**
	 * This method applies security to a given Binary resource. It is not typically
	 * overridden but you could override it if you wanted to completely replace the
	 * security logic in this interceptor.
	 *
	 * @param theBinary         The Binary resource being checked
	 * @param theRequestDetails The request details associated with this request
	 */
	protected void applyAccessControl(IBaseBinary theBinary, RequestDetails theRequestDetails) {
		FhirTerser terser = myFhirContext.newTerser();
		String securityContextSystem =
				terser.getSinglePrimitiveValueOrNull(theBinary, "securityContext.identifier.system");
		String securityContextValue =
				terser.getSinglePrimitiveValueOrNull(theBinary, "securityContext.identifier.value");

		if (isNotBlank(securityContextSystem) || isNotBlank(securityContextValue)) {
			applyAccessControl(theBinary, securityContextSystem, securityContextValue, theRequestDetails);
		}
	}

	/**
	 * This method applies access controls to a Binary resource containing the
	 * given identifier system and value in the Binary.securityContext element.
	 *
	 * @param theBinary                The binary resource
	 * @param theSecurityContextSystem The identifier system
	 * @param theSecurityContextValue  The identifier value
	 * @param theRequestDetails        The request details
	 */
	protected void applyAccessControl(
			IBaseBinary theBinary,
			String theSecurityContextSystem,
			String theSecurityContextValue,
			RequestDetails theRequestDetails) {
		if (theRequestDetails instanceof SystemRequestDetails) {
			return;
		}
		if (securityContextIdentifierAllowed(theSecurityContextSystem, theSecurityContextValue, theRequestDetails)) {
			return;
		}

		handleForbidden(theBinary);
	}

	/**
	 * Handles a non-permitted operation. This method throws a {@link ForbiddenOperationException}
	 * but you could override it to change that behaviour.
	 */
	protected void handleForbidden(IBaseBinary theBinary) {
		throw new ForbiddenOperationException(Msg.code(2369) + "Security context not permitted");
	}

	/**
	 * Determines whether the current user has access to the given security
	 * context identifier. This method is intended to be overridden, the default
	 * implementation simply always returns <code>false</code>.
	 *
	 * @param theSecurityContextSystem The <code>Binary.securityContext.identifier.system</code> value
	 * @param theSecurityContextValue  The <code>Binary.securityContext.identifier.value</code> value
	 * @param theRequestDetails        The request details associated with this request
	 * @return Returns <code>true</code> if the request should be permitted, and <code>false</code> otherwise
	 */
	protected boolean securityContextIdentifierAllowed(
			String theSecurityContextSystem, String theSecurityContextValue, RequestDetails theRequestDetails) {
		return false;
	}
}
