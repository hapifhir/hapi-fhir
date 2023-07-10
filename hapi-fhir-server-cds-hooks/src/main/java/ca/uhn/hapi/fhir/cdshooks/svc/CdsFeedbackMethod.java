/*-
 * #%L
 * Smile CDR - CDR
 * %%
 * Copyright (C) 2016 - 2023 Smile CDR, Inc.
 * %%
 * All rights reserved.
 * #L%
 */
package ca.uhn.hapi.fhir.cdshooks.svc;

import java.lang.reflect.Method;

public class CdsFeedbackMethod extends BaseCdsMethod {
	public CdsFeedbackMethod(Object theServiceBean, Method theMethod) {
		super(theServiceBean, theMethod);
	}
}
