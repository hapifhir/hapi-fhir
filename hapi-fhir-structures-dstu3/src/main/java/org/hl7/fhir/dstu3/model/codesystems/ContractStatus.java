package org.hl7.fhir.dstu3.model.codesystems;

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

// Generated on Mon, Apr 17, 2017 17:38-0400 for FHIR v3.0.1


import org.hl7.fhir.exceptions.FHIRException;

public enum ContractStatus {

        /**
         * Contract is augmented with additional information to correct errors in a predecessor or to updated values in a predecessor. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: revised; replaced.
         */
        AMENDED, 
        /**
         * Contract is augmented with additional information that was missing from a predecessor Contract. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: updated, replaced.
         */
        APPENDED, 
        /**
         * Contract is terminated due to failure of the Grantor and/or the Grantee to fulfil one or more contract provisions. Usage: Abnormal contract termination. Precedence Order = 10. Comparable FHIR and v.3 status codes: stopped; failed; aborted.
         */
        CANCELLED, 
        /**
         * Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7.Comparable FHIR and v.3 status codes: on hold; pended; suspended.
         */
        DISPUTED, 
        /**
         * Contract was created in error. No Precedence Order.  Status may be applied to a Contract with any status.
         */
        ENTEREDINERROR, 
        /**
         * Contract execution pending; may be executed when either the Grantor or the Grantee accepts the contract provisions by signing. I.e., where either the Grantor or the Grantee has signed, but not both. E.g., when an insurance applicant signs the insurers application, which references the policy. Usage: Optional first step of contract execution activity.  May be skipped and contracting activity moves directly to executed state. Precedence Order = 3. Comparable FHIR and v.3 status codes: draft; preliminary; planned; intended; active.
         */
        EXECUTABLE, 
        /**
         * Contract is activated for period stipulated when both the Grantor and Grantee have signed it. Usage: Required state for normal completion of contracting activity.  Precedence Order = 6. Comparable FHIR and v.3 status codes: accepted; completed.
         */
        EXECUTED, 
        /**
         * Contract execution is suspended while either or both the Grantor and Grantee propose and consider new or revised contract provisions. I.e., where the party which has not signed proposes changes to the terms.  E .g., a life insurer declines to agree to the signed application because the life insurer has evidence that the applicant, who asserted to being younger or a non-smoker to get a lower premium rate - but offers instead to agree to a higher premium based on the applicants actual age or smoking status. Usage: Optional contract activity between executable and executed state. Precedence Order = 4. Comparable FHIR and v.3 status codes: in progress; review; held.
         */
        NEGOTIABLE, 
        /**
         * Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template','form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. Comparable FHIR and v.3 status codes: requested; new.
         */
        OFFERED, 
        /**
         * Contract template is available as the basis for an application or offer by the Grantor or Grantee. E.g., health insurance policy; consent directive policy.  Usage: Required initial contract activity, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 1. Comparable FHIR and v.3 status codes: proposed; intended.
         */
        POLICY, 
        /**
         *  Execution of the Contract is not completed because either or both the Grantor and Grantee decline to accept some or all of the contract provisions. Usage: Optional contract activity between executable and abnormal termination. Precedence Order = 5. Comparable FHIR and v.3 status codes:  stopped; cancelled.
         */
        REJECTED, 
        /**
         * Beginning of a successor Contract at the termination of predecessor Contract lifecycle. Usage: Follows termination of a preceding Contract that has reached its expiry date. Precedence Order = 13. Comparable FHIR and v.3 status codes: superseded.
         */
        RENEWED, 
        /**
         * A Contract that is rescinded.  May be required prior to replacing with an updated Contract. Comparable FHIR and v.3 status codes: nullified.
         */
        REVOKED, 
        /**
         * Contract is reactivated after being pended because of faulty execution. *E.g., competency of the signer(s), or where the policy is substantially different from and did not accompany the application/form so that the applicant could not compare them. Aka - ''reactivated''. Usage: Optional stage where a pended contract is reactivated. Precedence Order = 8. Comparable FHIR and v.3 status codes: reactivated.
         */
        RESOLVED, 
        /**
         * Contract reaches its expiry date. It may or may not be renewed or renegotiated. Usage: Normal end of contract period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.
         */
        TERMINATED, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractStatus fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("amended".equals(codeString))
          return AMENDED;
        if ("appended".equals(codeString))
          return APPENDED;
        if ("cancelled".equals(codeString))
          return CANCELLED;
        if ("disputed".equals(codeString))
          return DISPUTED;
        if ("entered-in-error".equals(codeString))
          return ENTEREDINERROR;
        if ("executable".equals(codeString))
          return EXECUTABLE;
        if ("executed".equals(codeString))
          return EXECUTED;
        if ("negotiable".equals(codeString))
          return NEGOTIABLE;
        if ("offered".equals(codeString))
          return OFFERED;
        if ("policy".equals(codeString))
          return POLICY;
        if ("rejected".equals(codeString))
          return REJECTED;
        if ("renewed".equals(codeString))
          return RENEWED;
        if ("revoked".equals(codeString))
          return REVOKED;
        if ("resolved".equals(codeString))
          return RESOLVED;
        if ("terminated".equals(codeString))
          return TERMINATED;
        throw new FHIRException("Unknown ContractStatus code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AMENDED: return "amended";
            case APPENDED: return "appended";
            case CANCELLED: return "cancelled";
            case DISPUTED: return "disputed";
            case ENTEREDINERROR: return "entered-in-error";
            case EXECUTABLE: return "executable";
            case EXECUTED: return "executed";
            case NEGOTIABLE: return "negotiable";
            case OFFERED: return "offered";
            case POLICY: return "policy";
            case REJECTED: return "rejected";
            case RENEWED: return "renewed";
            case REVOKED: return "revoked";
            case RESOLVED: return "resolved";
            case TERMINATED: return "terminated";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/contract-status";
        }
        public String getDefinition() {
          switch (this) {
            case AMENDED: return "Contract is augmented with additional information to correct errors in a predecessor or to updated values in a predecessor. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: revised; replaced.";
            case APPENDED: return "Contract is augmented with additional information that was missing from a predecessor Contract. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: updated, replaced.";
            case CANCELLED: return "Contract is terminated due to failure of the Grantor and/or the Grantee to fulfil one or more contract provisions. Usage: Abnormal contract termination. Precedence Order = 10. Comparable FHIR and v.3 status codes: stopped; failed; aborted.";
            case DISPUTED: return "Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7.Comparable FHIR and v.3 status codes: on hold; pended; suspended.";
            case ENTEREDINERROR: return "Contract was created in error. No Precedence Order.  Status may be applied to a Contract with any status.";
            case EXECUTABLE: return "Contract execution pending; may be executed when either the Grantor or the Grantee accepts the contract provisions by signing. I.e., where either the Grantor or the Grantee has signed, but not both. E.g., when an insurance applicant signs the insurers application, which references the policy. Usage: Optional first step of contract execution activity.  May be skipped and contracting activity moves directly to executed state. Precedence Order = 3. Comparable FHIR and v.3 status codes: draft; preliminary; planned; intended; active.";
            case EXECUTED: return "Contract is activated for period stipulated when both the Grantor and Grantee have signed it. Usage: Required state for normal completion of contracting activity.  Precedence Order = 6. Comparable FHIR and v.3 status codes: accepted; completed.";
            case NEGOTIABLE: return "Contract execution is suspended while either or both the Grantor and Grantee propose and consider new or revised contract provisions. I.e., where the party which has not signed proposes changes to the terms.  E .g., a life insurer declines to agree to the signed application because the life insurer has evidence that the applicant, who asserted to being younger or a non-smoker to get a lower premium rate - but offers instead to agree to a higher premium based on the applicants actual age or smoking status. Usage: Optional contract activity between executable and executed state. Precedence Order = 4. Comparable FHIR and v.3 status codes: in progress; review; held.";
            case OFFERED: return "Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template','form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. Comparable FHIR and v.3 status codes: requested; new.";
            case POLICY: return "Contract template is available as the basis for an application or offer by the Grantor or Grantee. E.g., health insurance policy; consent directive policy.  Usage: Required initial contract activity, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 1. Comparable FHIR and v.3 status codes: proposed; intended.";
            case REJECTED: return " Execution of the Contract is not completed because either or both the Grantor and Grantee decline to accept some or all of the contract provisions. Usage: Optional contract activity between executable and abnormal termination. Precedence Order = 5. Comparable FHIR and v.3 status codes:  stopped; cancelled.";
            case RENEWED: return "Beginning of a successor Contract at the termination of predecessor Contract lifecycle. Usage: Follows termination of a preceding Contract that has reached its expiry date. Precedence Order = 13. Comparable FHIR and v.3 status codes: superseded.";
            case REVOKED: return "A Contract that is rescinded.  May be required prior to replacing with an updated Contract. Comparable FHIR and v.3 status codes: nullified.";
            case RESOLVED: return "Contract is reactivated after being pended because of faulty execution. *E.g., competency of the signer(s), or where the policy is substantially different from and did not accompany the application/form so that the applicant could not compare them. Aka - ''reactivated''. Usage: Optional stage where a pended contract is reactivated. Precedence Order = 8. Comparable FHIR and v.3 status codes: reactivated.";
            case TERMINATED: return "Contract reaches its expiry date. It may or may not be renewed or renegotiated. Usage: Normal end of contract period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AMENDED: return "Amended";
            case APPENDED: return "Appended";
            case CANCELLED: return "Cancelled";
            case DISPUTED: return "Disputed";
            case ENTEREDINERROR: return "Entered in Error";
            case EXECUTABLE: return "Executable";
            case EXECUTED: return "Executed";
            case NEGOTIABLE: return "Negotiable";
            case OFFERED: return "Offered";
            case POLICY: return "Policy";
            case REJECTED: return "Rejected";
            case RENEWED: return "Renewed";
            case REVOKED: return "Revoked";
            case RESOLVED: return "Resolved";
            case TERMINATED: return "Terminated";
            default: return "?";
          }
    }


}

