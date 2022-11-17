package ca.uhn.fhir.cr.common.interceptor;

/*-
 * #%L
 * HAPI FHIR - Clinical Reasoning
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

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.cr.common.behavior.DaoRegistryUser;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.jpa.api.dao.DaoRegistry;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import org.hl7.fhir.instance.model.api.IBaseOperationOutcome;
import org.opencds.cqf.cql.engine.exception.CqlException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
/**
 * This class represents clinical reasoning interceptor used for cql exception handling and logging
 **/
@Interceptor
public class CqlExceptionHandlingInterceptor implements DaoRegistryUser {

	@Autowired
	private DaoRegistry myDaoRegistry;

	@Override
	public DaoRegistry getDaoRegistry() {
		return myDaoRegistry;
	}

	@Override
	public FhirContext getFhirContext() {
		return DaoRegistryUser.super.getFhirContext();
	}

	@Hook(Pointcut.SERVER_HANDLE_EXCEPTION)
	public boolean handleException(RequestDetails theRequestDetails, BaseServerResponseException theException,
			HttpServletRequest theServletRequest, HttpServletResponse theServletResponse) throws IOException {

		CqlException cqlException = getCqlException(theException);
		if (cqlException == null) {
			return true;
		}

		IBaseOperationOutcome operationOutcome = theException.getOperationOutcome();
		if (operationOutcome != null) {
			String cqlMessage = this.getCqlMessage(cqlException);
			switch (operationOutcome.getStructureFhirVersionEnum()) {
				case DSTU3:
					updateOutcome((org.hl7.fhir.dstu3.model.OperationOutcome) operationOutcome, cqlMessage);
					break;
				case R4:
					updateOutcome((org.hl7.fhir.r4.model.OperationOutcome) operationOutcome, cqlMessage);
					break;
				case R5:
					updateOutcome((org.hl7.fhir.r5.model.OperationOutcome) operationOutcome, cqlMessage);
					break;
				default:
					break;
			}
		}

		return true;
	}

	private void updateOutcome(org.hl7.fhir.dstu3.model.OperationOutcome operationOutcome, String cqlCause) {
		operationOutcome.getIssueFirstRep().setDiagnostics(cqlCause);
	}

	private void updateOutcome(org.hl7.fhir.r4.model.OperationOutcome operationOutcome, String cqlCause) {
		operationOutcome.getIssueFirstRep().setDiagnostics(cqlCause);
	}

	private void updateOutcome(org.hl7.fhir.r5.model.OperationOutcome operationOutcome, String cqlCause) {
		operationOutcome.getIssueFirstRep().setDiagnostics(cqlCause);
	}

	private String getCqlMessage(CqlException cqlException) {
		String message = cqlException.getMessage();

		if (cqlException.getSourceLocator() != null) {
			message += "\nat CQL source location: " + cqlException.getSourceLocator().toString();
		}

		if (cqlException.getCause() != null) {
			message += "\ncaused by: " + cqlException.getCause().getMessage();
		}

		return message;
	}

	private CqlException getCqlException(BaseServerResponseException theException) {
		if (theException.getCause() instanceof CqlException) {
			return (CqlException) theException.getCause();
		} else if (theException.getCause() instanceof InvocationTargetException) {
			InvocationTargetException ite = (InvocationTargetException) theException.getCause();

			if (ite.getCause() instanceof CqlException) {
				return (CqlException) ite.getCause();
			}

			if (ite.getTargetException() instanceof CqlException) {
				return (CqlException) ite.getTargetException();
			}
		}

		return null;
	}
}
