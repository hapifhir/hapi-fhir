package org.hl7.fhir.r4.utils.client;


/*
  Copyright (c) 2011+, HL7, Inc.
  All rights reserved.
  
  Redistribution and use in source and binary forms, with or without modification, 
  are permitted provided that the following conditions are met:
  
   * Redistributions of source code must retain the above copyright notice, this 
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice, 
     this list of conditions and the following disclaimer in the documentation 
     and/or other materials provided with the distribution.
   * Neither the name of HL7 nor the names of its contributors may be used to 
     endorse or promote products derived from this software without specific 
     prior written permission.
  
  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
  IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, 
  INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT 
  NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR 
  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
  POSSIBILITY OF SUCH DAMAGE.
  
*/

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.OperationOutcome;

/**
 * FHIR client exception.
 * 
 * FHIR API exception will be wrapped in FHIR client exceptions. OperationOutcome errors
 * resulting from the server can be access by calling:
 * <pre><code>
 * if(e.hasServerErrors()) {
 * 	List<OperationOutcome> errors = e.getServerErrors();
 *  //process errors...
 * }
 * </code></pre>
 * 
 * 
 * 
 * @author Claude Nanjo
 *
 */
public class EFhirClientException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private List<OperationOutcome> errors = new ArrayList<OperationOutcome>();
	 
	public EFhirClientException(String message) {
		super(message);
	}
	
	public EFhirClientException(String message, List<OperationOutcome> serverErrors) {
		super(message);
		if(serverErrors != null && serverErrors.size() > 0) {
			errors.addAll(serverErrors);
		}
	}

	public EFhirClientException(Exception cause) {
		super(cause);
	}
	
	public EFhirClientException(String message, Exception cause) {
		super(message, cause);
	}
	
	/**
	 * Generate EFhirClientException which include a message indicating the cause of the exception
	 * along with any OperationOutcome server error that may have resulted.
	 * 
	 * @param message
	 * @param serverError
	 */
	public EFhirClientException(String message, OperationOutcome serverError) {
		super(message);
		if(serverError != null) {
			errors.add(serverError);
		}
	}
	
	/**
	 * Generate EFhirClientException indicating the cause of the exception
	 * along with any OperationOutcome server error the server may have generated.
	 * 
	 * A default message of "One or more server side errors have occurred during this operation. Refer to e.getServerErrors() for additional details."
	 * will be returned to users.
	 * 
	 * @param message
	 * @param serverError
	 */
	public EFhirClientException(OperationOutcome serverError) {
		super("Error on the server: "+serverError.getText().getDiv().allText()+". Refer to e.getServerErrors() for additional details.");
		if(serverError != null) {
			errors.add(serverError);
		}
	}
	
	/**
	 * Method returns all OperationOutcome server errors that are 
	 * associated with this exception.
	 * 
	 * @return
	 */
	public List<OperationOutcome> getServerErrors() {
		return errors;
	}
	
	/**
	 * Method returns true if exception contains server OperationOutcome errors in payload.
	 * 
	 * @return
	 */
	public boolean hasServerErrors() {
		return errors.size() > 0;
	}

}
