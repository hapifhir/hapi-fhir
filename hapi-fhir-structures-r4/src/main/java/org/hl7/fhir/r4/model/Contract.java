package org.hl7.fhir.r4.model;

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

// Generated on Thu, Dec 27, 2018 10:06-0500 for FHIR v4.0.0

import java.util.*;

import java.math.*;
import org.hl7.fhir.utilities.Utilities;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.api.annotation.SearchParamDefinition;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ChildOrder;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.Block;
import org.hl7.fhir.instance.model.api.*;
import org.hl7.fhir.exceptions.FHIRException;
/**
 * Legally enforceable, formally recorded unilateral or bilateral directive i.e., a policy or agreement.
 */
@ResourceDef(name="Contract", profile="http://hl7.org/fhir/StructureDefinition/Contract")
public class Contract extends DomainResource {

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
         * Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7. Comparable FHIR and v.3 status codes: on hold; pended; suspended.
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
         * Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template', 'form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. Comparable FHIR and v.3 status codes: requested; new.
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
         * Contract reaches its expiry date. It might or might not be renewed or renegotiated. Usage: Normal end of contract period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.
         */
        TERMINATED, 
        /**
         * added to help the parsers with the generic types
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
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
          switch (this) {
            case AMENDED: return "http://hl7.org/fhir/contract-status";
            case APPENDED: return "http://hl7.org/fhir/contract-status";
            case CANCELLED: return "http://hl7.org/fhir/contract-status";
            case DISPUTED: return "http://hl7.org/fhir/contract-status";
            case ENTEREDINERROR: return "http://hl7.org/fhir/contract-status";
            case EXECUTABLE: return "http://hl7.org/fhir/contract-status";
            case EXECUTED: return "http://hl7.org/fhir/contract-status";
            case NEGOTIABLE: return "http://hl7.org/fhir/contract-status";
            case OFFERED: return "http://hl7.org/fhir/contract-status";
            case POLICY: return "http://hl7.org/fhir/contract-status";
            case REJECTED: return "http://hl7.org/fhir/contract-status";
            case RENEWED: return "http://hl7.org/fhir/contract-status";
            case REVOKED: return "http://hl7.org/fhir/contract-status";
            case RESOLVED: return "http://hl7.org/fhir/contract-status";
            case TERMINATED: return "http://hl7.org/fhir/contract-status";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AMENDED: return "Contract is augmented with additional information to correct errors in a predecessor or to updated values in a predecessor. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: revised; replaced.";
            case APPENDED: return "Contract is augmented with additional information that was missing from a predecessor Contract. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: updated, replaced.";
            case CANCELLED: return "Contract is terminated due to failure of the Grantor and/or the Grantee to fulfil one or more contract provisions. Usage: Abnormal contract termination. Precedence Order = 10. Comparable FHIR and v.3 status codes: stopped; failed; aborted.";
            case DISPUTED: return "Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7. Comparable FHIR and v.3 status codes: on hold; pended; suspended.";
            case ENTEREDINERROR: return "Contract was created in error. No Precedence Order.  Status may be applied to a Contract with any status.";
            case EXECUTABLE: return "Contract execution pending; may be executed when either the Grantor or the Grantee accepts the contract provisions by signing. I.e., where either the Grantor or the Grantee has signed, but not both. E.g., when an insurance applicant signs the insurers application, which references the policy. Usage: Optional first step of contract execution activity.  May be skipped and contracting activity moves directly to executed state. Precedence Order = 3. Comparable FHIR and v.3 status codes: draft; preliminary; planned; intended; active.";
            case EXECUTED: return "Contract is activated for period stipulated when both the Grantor and Grantee have signed it. Usage: Required state for normal completion of contracting activity.  Precedence Order = 6. Comparable FHIR and v.3 status codes: accepted; completed.";
            case NEGOTIABLE: return "Contract execution is suspended while either or both the Grantor and Grantee propose and consider new or revised contract provisions. I.e., where the party which has not signed proposes changes to the terms.  E .g., a life insurer declines to agree to the signed application because the life insurer has evidence that the applicant, who asserted to being younger or a non-smoker to get a lower premium rate - but offers instead to agree to a higher premium based on the applicants actual age or smoking status. Usage: Optional contract activity between executable and executed state. Precedence Order = 4. Comparable FHIR and v.3 status codes: in progress; review; held.";
            case OFFERED: return "Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template', 'form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. Comparable FHIR and v.3 status codes: requested; new.";
            case POLICY: return "Contract template is available as the basis for an application or offer by the Grantor or Grantee. E.g., health insurance policy; consent directive policy.  Usage: Required initial contract activity, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 1. Comparable FHIR and v.3 status codes: proposed; intended.";
            case REJECTED: return " Execution of the Contract is not completed because either or both the Grantor and Grantee decline to accept some or all of the contract provisions. Usage: Optional contract activity between executable and abnormal termination. Precedence Order = 5. Comparable FHIR and v.3 status codes:  stopped; cancelled.";
            case RENEWED: return "Beginning of a successor Contract at the termination of predecessor Contract lifecycle. Usage: Follows termination of a preceding Contract that has reached its expiry date. Precedence Order = 13. Comparable FHIR and v.3 status codes: superseded.";
            case REVOKED: return "A Contract that is rescinded.  May be required prior to replacing with an updated Contract. Comparable FHIR and v.3 status codes: nullified.";
            case RESOLVED: return "Contract is reactivated after being pended because of faulty execution. *E.g., competency of the signer(s), or where the policy is substantially different from and did not accompany the application/form so that the applicant could not compare them. Aka - ''reactivated''. Usage: Optional stage where a pended contract is reactivated. Precedence Order = 8. Comparable FHIR and v.3 status codes: reactivated.";
            case TERMINATED: return "Contract reaches its expiry date. It might or might not be renewed or renegotiated. Usage: Normal end of contract period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.";
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

  public static class ContractStatusEnumFactory implements EnumFactory<ContractStatus> {
    public ContractStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("amended".equals(codeString))
          return ContractStatus.AMENDED;
        if ("appended".equals(codeString))
          return ContractStatus.APPENDED;
        if ("cancelled".equals(codeString))
          return ContractStatus.CANCELLED;
        if ("disputed".equals(codeString))
          return ContractStatus.DISPUTED;
        if ("entered-in-error".equals(codeString))
          return ContractStatus.ENTEREDINERROR;
        if ("executable".equals(codeString))
          return ContractStatus.EXECUTABLE;
        if ("executed".equals(codeString))
          return ContractStatus.EXECUTED;
        if ("negotiable".equals(codeString))
          return ContractStatus.NEGOTIABLE;
        if ("offered".equals(codeString))
          return ContractStatus.OFFERED;
        if ("policy".equals(codeString))
          return ContractStatus.POLICY;
        if ("rejected".equals(codeString))
          return ContractStatus.REJECTED;
        if ("renewed".equals(codeString))
          return ContractStatus.RENEWED;
        if ("revoked".equals(codeString))
          return ContractStatus.REVOKED;
        if ("resolved".equals(codeString))
          return ContractStatus.RESOLVED;
        if ("terminated".equals(codeString))
          return ContractStatus.TERMINATED;
        throw new IllegalArgumentException("Unknown ContractStatus code '"+codeString+"'");
        }
        public Enumeration<ContractStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ContractStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("amended".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.AMENDED);
        if ("appended".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.APPENDED);
        if ("cancelled".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.CANCELLED);
        if ("disputed".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.DISPUTED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.ENTEREDINERROR);
        if ("executable".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.EXECUTABLE);
        if ("executed".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.EXECUTED);
        if ("negotiable".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.NEGOTIABLE);
        if ("offered".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.OFFERED);
        if ("policy".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.POLICY);
        if ("rejected".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.REJECTED);
        if ("renewed".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.RENEWED);
        if ("revoked".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.REVOKED);
        if ("resolved".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.RESOLVED);
        if ("terminated".equals(codeString))
          return new Enumeration<ContractStatus>(this, ContractStatus.TERMINATED);
        throw new FHIRException("Unknown ContractStatus code '"+codeString+"'");
        }
    public String toCode(ContractStatus code) {
      if (code == ContractStatus.AMENDED)
        return "amended";
      if (code == ContractStatus.APPENDED)
        return "appended";
      if (code == ContractStatus.CANCELLED)
        return "cancelled";
      if (code == ContractStatus.DISPUTED)
        return "disputed";
      if (code == ContractStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ContractStatus.EXECUTABLE)
        return "executable";
      if (code == ContractStatus.EXECUTED)
        return "executed";
      if (code == ContractStatus.NEGOTIABLE)
        return "negotiable";
      if (code == ContractStatus.OFFERED)
        return "offered";
      if (code == ContractStatus.POLICY)
        return "policy";
      if (code == ContractStatus.REJECTED)
        return "rejected";
      if (code == ContractStatus.RENEWED)
        return "renewed";
      if (code == ContractStatus.REVOKED)
        return "revoked";
      if (code == ContractStatus.RESOLVED)
        return "resolved";
      if (code == ContractStatus.TERMINATED)
        return "terminated";
      return "?";
      }
    public String toSystem(ContractStatus code) {
      return code.getSystem();
      }
    }

    public enum ContractPublicationStatus {
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
         * Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7. Comparable FHIR and v.3 status codes: on hold; pended; suspended.
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
         * Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template', 'form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. Comparable FHIR and v.3 status codes: requested; new.
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
         * Contract reaches its expiry date. It might or might not be renewed or renegotiated. Usage: Normal end of contract period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.
         */
        TERMINATED, 
        /**
         * added to help the parsers with the generic types
         */
        NULL;
        public static ContractPublicationStatus fromCode(String codeString) throws FHIRException {
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
        if (Configuration.isAcceptInvalidEnums())
          return null;
        else
          throw new FHIRException("Unknown ContractPublicationStatus code '"+codeString+"'");
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
          switch (this) {
            case AMENDED: return "http://hl7.org/fhir/contract-publicationstatus";
            case APPENDED: return "http://hl7.org/fhir/contract-publicationstatus";
            case CANCELLED: return "http://hl7.org/fhir/contract-publicationstatus";
            case DISPUTED: return "http://hl7.org/fhir/contract-publicationstatus";
            case ENTEREDINERROR: return "http://hl7.org/fhir/contract-publicationstatus";
            case EXECUTABLE: return "http://hl7.org/fhir/contract-publicationstatus";
            case EXECUTED: return "http://hl7.org/fhir/contract-publicationstatus";
            case NEGOTIABLE: return "http://hl7.org/fhir/contract-publicationstatus";
            case OFFERED: return "http://hl7.org/fhir/contract-publicationstatus";
            case POLICY: return "http://hl7.org/fhir/contract-publicationstatus";
            case REJECTED: return "http://hl7.org/fhir/contract-publicationstatus";
            case RENEWED: return "http://hl7.org/fhir/contract-publicationstatus";
            case REVOKED: return "http://hl7.org/fhir/contract-publicationstatus";
            case RESOLVED: return "http://hl7.org/fhir/contract-publicationstatus";
            case TERMINATED: return "http://hl7.org/fhir/contract-publicationstatus";
            default: return "?";
          }
        }
        public String getDefinition() {
          switch (this) {
            case AMENDED: return "Contract is augmented with additional information to correct errors in a predecessor or to updated values in a predecessor. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: revised; replaced.";
            case APPENDED: return "Contract is augmented with additional information that was missing from a predecessor Contract. Usage: Contract altered within effective time. Precedence Order = 9. Comparable FHIR and v.3 status codes: updated, replaced.";
            case CANCELLED: return "Contract is terminated due to failure of the Grantor and/or the Grantee to fulfil one or more contract provisions. Usage: Abnormal contract termination. Precedence Order = 10. Comparable FHIR and v.3 status codes: stopped; failed; aborted.";
            case DISPUTED: return "Contract is pended to rectify failure of the Grantor or the Grantee to fulfil contract provision(s). E.g., Grantee complaint about Grantor's failure to comply with contract provisions. Usage: Contract pended. Precedence Order = 7. Comparable FHIR and v.3 status codes: on hold; pended; suspended.";
            case ENTEREDINERROR: return "Contract was created in error. No Precedence Order.  Status may be applied to a Contract with any status.";
            case EXECUTABLE: return "Contract execution pending; may be executed when either the Grantor or the Grantee accepts the contract provisions by signing. I.e., where either the Grantor or the Grantee has signed, but not both. E.g., when an insurance applicant signs the insurers application, which references the policy. Usage: Optional first step of contract execution activity.  May be skipped and contracting activity moves directly to executed state. Precedence Order = 3. Comparable FHIR and v.3 status codes: draft; preliminary; planned; intended; active.";
            case EXECUTED: return "Contract is activated for period stipulated when both the Grantor and Grantee have signed it. Usage: Required state for normal completion of contracting activity.  Precedence Order = 6. Comparable FHIR and v.3 status codes: accepted; completed.";
            case NEGOTIABLE: return "Contract execution is suspended while either or both the Grantor and Grantee propose and consider new or revised contract provisions. I.e., where the party which has not signed proposes changes to the terms.  E .g., a life insurer declines to agree to the signed application because the life insurer has evidence that the applicant, who asserted to being younger or a non-smoker to get a lower premium rate - but offers instead to agree to a higher premium based on the applicants actual age or smoking status. Usage: Optional contract activity between executable and executed state. Precedence Order = 4. Comparable FHIR and v.3 status codes: in progress; review; held.";
            case OFFERED: return "Contract is a proposal by either the Grantor or the Grantee. Aka - A Contract hard copy or electronic 'template', 'form' or 'application'. E.g., health insurance application; consent directive form. Usage: Beginning of contract negotiation, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 2. Comparable FHIR and v.3 status codes: requested; new.";
            case POLICY: return "Contract template is available as the basis for an application or offer by the Grantor or Grantee. E.g., health insurance policy; consent directive policy.  Usage: Required initial contract activity, which may have been completed as a precondition because used for 0..* contracts. Precedence Order = 1. Comparable FHIR and v.3 status codes: proposed; intended.";
            case REJECTED: return " Execution of the Contract is not completed because either or both the Grantor and Grantee decline to accept some or all of the contract provisions. Usage: Optional contract activity between executable and abnormal termination. Precedence Order = 5. Comparable FHIR and v.3 status codes:  stopped; cancelled.";
            case RENEWED: return "Beginning of a successor Contract at the termination of predecessor Contract lifecycle. Usage: Follows termination of a preceding Contract that has reached its expiry date. Precedence Order = 13. Comparable FHIR and v.3 status codes: superseded.";
            case REVOKED: return "A Contract that is rescinded.  May be required prior to replacing with an updated Contract. Comparable FHIR and v.3 status codes: nullified.";
            case RESOLVED: return "Contract is reactivated after being pended because of faulty execution. *E.g., competency of the signer(s), or where the policy is substantially different from and did not accompany the application/form so that the applicant could not compare them. Aka - ''reactivated''. Usage: Optional stage where a pended contract is reactivated. Precedence Order = 8. Comparable FHIR and v.3 status codes: reactivated.";
            case TERMINATED: return "Contract reaches its expiry date. It might or might not be renewed or renegotiated. Usage: Normal end of contract period. Precedence Order = 12. Comparable FHIR and v.3 status codes: Obsoleted.";
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

  public static class ContractPublicationStatusEnumFactory implements EnumFactory<ContractPublicationStatus> {
    public ContractPublicationStatus fromCode(String codeString) throws IllegalArgumentException {
      if (codeString == null || "".equals(codeString))
            if (codeString == null || "".equals(codeString))
                return null;
        if ("amended".equals(codeString))
          return ContractPublicationStatus.AMENDED;
        if ("appended".equals(codeString))
          return ContractPublicationStatus.APPENDED;
        if ("cancelled".equals(codeString))
          return ContractPublicationStatus.CANCELLED;
        if ("disputed".equals(codeString))
          return ContractPublicationStatus.DISPUTED;
        if ("entered-in-error".equals(codeString))
          return ContractPublicationStatus.ENTEREDINERROR;
        if ("executable".equals(codeString))
          return ContractPublicationStatus.EXECUTABLE;
        if ("executed".equals(codeString))
          return ContractPublicationStatus.EXECUTED;
        if ("negotiable".equals(codeString))
          return ContractPublicationStatus.NEGOTIABLE;
        if ("offered".equals(codeString))
          return ContractPublicationStatus.OFFERED;
        if ("policy".equals(codeString))
          return ContractPublicationStatus.POLICY;
        if ("rejected".equals(codeString))
          return ContractPublicationStatus.REJECTED;
        if ("renewed".equals(codeString))
          return ContractPublicationStatus.RENEWED;
        if ("revoked".equals(codeString))
          return ContractPublicationStatus.REVOKED;
        if ("resolved".equals(codeString))
          return ContractPublicationStatus.RESOLVED;
        if ("terminated".equals(codeString))
          return ContractPublicationStatus.TERMINATED;
        throw new IllegalArgumentException("Unknown ContractPublicationStatus code '"+codeString+"'");
        }
        public Enumeration<ContractPublicationStatus> fromType(Base code) throws FHIRException {
          if (code == null)
            return null;
          if (code.isEmpty())
            return new Enumeration<ContractPublicationStatus>(this);
          String codeString = ((PrimitiveType) code).asStringValue();
          if (codeString == null || "".equals(codeString))
            return null;
        if ("amended".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.AMENDED);
        if ("appended".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.APPENDED);
        if ("cancelled".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.CANCELLED);
        if ("disputed".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.DISPUTED);
        if ("entered-in-error".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.ENTEREDINERROR);
        if ("executable".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.EXECUTABLE);
        if ("executed".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.EXECUTED);
        if ("negotiable".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.NEGOTIABLE);
        if ("offered".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.OFFERED);
        if ("policy".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.POLICY);
        if ("rejected".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.REJECTED);
        if ("renewed".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.RENEWED);
        if ("revoked".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.REVOKED);
        if ("resolved".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.RESOLVED);
        if ("terminated".equals(codeString))
          return new Enumeration<ContractPublicationStatus>(this, ContractPublicationStatus.TERMINATED);
        throw new FHIRException("Unknown ContractPublicationStatus code '"+codeString+"'");
        }
    public String toCode(ContractPublicationStatus code) {
      if (code == ContractPublicationStatus.AMENDED)
        return "amended";
      if (code == ContractPublicationStatus.APPENDED)
        return "appended";
      if (code == ContractPublicationStatus.CANCELLED)
        return "cancelled";
      if (code == ContractPublicationStatus.DISPUTED)
        return "disputed";
      if (code == ContractPublicationStatus.ENTEREDINERROR)
        return "entered-in-error";
      if (code == ContractPublicationStatus.EXECUTABLE)
        return "executable";
      if (code == ContractPublicationStatus.EXECUTED)
        return "executed";
      if (code == ContractPublicationStatus.NEGOTIABLE)
        return "negotiable";
      if (code == ContractPublicationStatus.OFFERED)
        return "offered";
      if (code == ContractPublicationStatus.POLICY)
        return "policy";
      if (code == ContractPublicationStatus.REJECTED)
        return "rejected";
      if (code == ContractPublicationStatus.RENEWED)
        return "renewed";
      if (code == ContractPublicationStatus.REVOKED)
        return "revoked";
      if (code == ContractPublicationStatus.RESOLVED)
        return "resolved";
      if (code == ContractPublicationStatus.TERMINATED)
        return "terminated";
      return "?";
      }
    public String toSystem(ContractPublicationStatus code) {
      return code.getSystem();
      }
    }

    @Block()
    public static class ContentDefinitionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Precusory content structure and use, i.e., a boilerplate, template, application for a contract such as an insurance policy or benefits under a program, e.g., workers compensation.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Content structure and use", formalDefinition="Precusory content structure and use, i.e., a boilerplate, template, application for a contract such as an insurance policy or benefits under a program, e.g., workers compensation." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-definition-type")
        protected CodeableConcept type;

        /**
         * Detailed Precusory content type.
         */
        @Child(name = "subType", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Detailed Content Type Definition", formalDefinition="Detailed Precusory content type." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-definition-subtype")
        protected CodeableConcept subType;

        /**
         * The  individual or organization that published the Contract precursor content.
         */
        @Child(name = "publisher", type = {Practitioner.class, PractitionerRole.class, Organization.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Publisher Entity", formalDefinition="The  individual or organization that published the Contract precursor content." )
        protected Reference publisher;

        /**
         * The actual object that is the target of the reference (The  individual or organization that published the Contract precursor content.)
         */
        protected Resource publisherTarget;

        /**
         * The date (and optionally time) when the contract was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the contract changes.
         */
        @Child(name = "publicationDate", type = {DateTimeType.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When published", formalDefinition="The date (and optionally time) when the contract was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the contract changes." )
        protected DateTimeType publicationDate;

        /**
         * draft | active | retired | unknown.
         */
        @Child(name = "publicationStatus", type = {CodeType.class}, order=5, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="draft | active | retired | unknown", formalDefinition="draft | active | retired | unknown." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-publicationstatus")
        protected Enumeration<ContractPublicationStatus> publicationStatus;

        /**
         * A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on the use and publishing of the Contract precursor content.
         */
        @Child(name = "copyright", type = {MarkdownType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Publication Ownership", formalDefinition="A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on the use and publishing of the Contract precursor content." )
        protected MarkdownType copyright;

        private static final long serialVersionUID = -699592864L;

    /**
     * Constructor
     */
      public ContentDefinitionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ContentDefinitionComponent(CodeableConcept type, Enumeration<ContractPublicationStatus> publicationStatus) {
        super();
        this.type = type;
        this.publicationStatus = publicationStatus;
      }

        /**
         * @return {@link #type} (Precusory content structure and use, i.e., a boilerplate, template, application for a contract such as an insurance policy or benefits under a program, e.g., workers compensation.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContentDefinitionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Precusory content structure and use, i.e., a boilerplate, template, application for a contract such as an insurance policy or benefits under a program, e.g., workers compensation.)
         */
        public ContentDefinitionComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #subType} (Detailed Precusory content type.)
         */
        public CodeableConcept getSubType() { 
          if (this.subType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContentDefinitionComponent.subType");
            else if (Configuration.doAutoCreate())
              this.subType = new CodeableConcept(); // cc
          return this.subType;
        }

        public boolean hasSubType() { 
          return this.subType != null && !this.subType.isEmpty();
        }

        /**
         * @param value {@link #subType} (Detailed Precusory content type.)
         */
        public ContentDefinitionComponent setSubType(CodeableConcept value) { 
          this.subType = value;
          return this;
        }

        /**
         * @return {@link #publisher} (The  individual or organization that published the Contract precursor content.)
         */
        public Reference getPublisher() { 
          if (this.publisher == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContentDefinitionComponent.publisher");
            else if (Configuration.doAutoCreate())
              this.publisher = new Reference(); // cc
          return this.publisher;
        }

        public boolean hasPublisher() { 
          return this.publisher != null && !this.publisher.isEmpty();
        }

        /**
         * @param value {@link #publisher} (The  individual or organization that published the Contract precursor content.)
         */
        public ContentDefinitionComponent setPublisher(Reference value) { 
          this.publisher = value;
          return this;
        }

        /**
         * @return {@link #publisher} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The  individual or organization that published the Contract precursor content.)
         */
        public Resource getPublisherTarget() { 
          return this.publisherTarget;
        }

        /**
         * @param value {@link #publisher} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The  individual or organization that published the Contract precursor content.)
         */
        public ContentDefinitionComponent setPublisherTarget(Resource value) { 
          this.publisherTarget = value;
          return this;
        }

        /**
         * @return {@link #publicationDate} (The date (and optionally time) when the contract was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the contract changes.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
         */
        public DateTimeType getPublicationDateElement() { 
          if (this.publicationDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContentDefinitionComponent.publicationDate");
            else if (Configuration.doAutoCreate())
              this.publicationDate = new DateTimeType(); // bb
          return this.publicationDate;
        }

        public boolean hasPublicationDateElement() { 
          return this.publicationDate != null && !this.publicationDate.isEmpty();
        }

        public boolean hasPublicationDate() { 
          return this.publicationDate != null && !this.publicationDate.isEmpty();
        }

        /**
         * @param value {@link #publicationDate} (The date (and optionally time) when the contract was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the contract changes.). This is the underlying object with id, value and extensions. The accessor "getPublicationDate" gives direct access to the value
         */
        public ContentDefinitionComponent setPublicationDateElement(DateTimeType value) { 
          this.publicationDate = value;
          return this;
        }

        /**
         * @return The date (and optionally time) when the contract was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the contract changes.
         */
        public Date getPublicationDate() { 
          return this.publicationDate == null ? null : this.publicationDate.getValue();
        }

        /**
         * @param value The date (and optionally time) when the contract was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the contract changes.
         */
        public ContentDefinitionComponent setPublicationDate(Date value) { 
          if (value == null)
            this.publicationDate = null;
          else {
            if (this.publicationDate == null)
              this.publicationDate = new DateTimeType();
            this.publicationDate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #publicationStatus} (draft | active | retired | unknown.). This is the underlying object with id, value and extensions. The accessor "getPublicationStatus" gives direct access to the value
         */
        public Enumeration<ContractPublicationStatus> getPublicationStatusElement() { 
          if (this.publicationStatus == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContentDefinitionComponent.publicationStatus");
            else if (Configuration.doAutoCreate())
              this.publicationStatus = new Enumeration<ContractPublicationStatus>(new ContractPublicationStatusEnumFactory()); // bb
          return this.publicationStatus;
        }

        public boolean hasPublicationStatusElement() { 
          return this.publicationStatus != null && !this.publicationStatus.isEmpty();
        }

        public boolean hasPublicationStatus() { 
          return this.publicationStatus != null && !this.publicationStatus.isEmpty();
        }

        /**
         * @param value {@link #publicationStatus} (draft | active | retired | unknown.). This is the underlying object with id, value and extensions. The accessor "getPublicationStatus" gives direct access to the value
         */
        public ContentDefinitionComponent setPublicationStatusElement(Enumeration<ContractPublicationStatus> value) { 
          this.publicationStatus = value;
          return this;
        }

        /**
         * @return draft | active | retired | unknown.
         */
        public ContractPublicationStatus getPublicationStatus() { 
          return this.publicationStatus == null ? null : this.publicationStatus.getValue();
        }

        /**
         * @param value draft | active | retired | unknown.
         */
        public ContentDefinitionComponent setPublicationStatus(ContractPublicationStatus value) { 
            if (this.publicationStatus == null)
              this.publicationStatus = new Enumeration<ContractPublicationStatus>(new ContractPublicationStatusEnumFactory());
            this.publicationStatus.setValue(value);
          return this;
        }

        /**
         * @return {@link #copyright} (A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on the use and publishing of the Contract precursor content.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
         */
        public MarkdownType getCopyrightElement() { 
          if (this.copyright == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContentDefinitionComponent.copyright");
            else if (Configuration.doAutoCreate())
              this.copyright = new MarkdownType(); // bb
          return this.copyright;
        }

        public boolean hasCopyrightElement() { 
          return this.copyright != null && !this.copyright.isEmpty();
        }

        public boolean hasCopyright() { 
          return this.copyright != null && !this.copyright.isEmpty();
        }

        /**
         * @param value {@link #copyright} (A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on the use and publishing of the Contract precursor content.). This is the underlying object with id, value and extensions. The accessor "getCopyright" gives direct access to the value
         */
        public ContentDefinitionComponent setCopyrightElement(MarkdownType value) { 
          this.copyright = value;
          return this;
        }

        /**
         * @return A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on the use and publishing of the Contract precursor content.
         */
        public String getCopyright() { 
          return this.copyright == null ? null : this.copyright.getValue();
        }

        /**
         * @param value A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on the use and publishing of the Contract precursor content.
         */
        public ContentDefinitionComponent setCopyright(String value) { 
          if (value == null)
            this.copyright = null;
          else {
            if (this.copyright == null)
              this.copyright = new MarkdownType();
            this.copyright.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "CodeableConcept", "Precusory content structure and use, i.e., a boilerplate, template, application for a contract such as an insurance policy or benefits under a program, e.g., workers compensation.", 0, 1, type));
          children.add(new Property("subType", "CodeableConcept", "Detailed Precusory content type.", 0, 1, subType));
          children.add(new Property("publisher", "Reference(Practitioner|PractitionerRole|Organization)", "The  individual or organization that published the Contract precursor content.", 0, 1, publisher));
          children.add(new Property("publicationDate", "dateTime", "The date (and optionally time) when the contract was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the contract changes.", 0, 1, publicationDate));
          children.add(new Property("publicationStatus", "code", "draft | active | retired | unknown.", 0, 1, publicationStatus));
          children.add(new Property("copyright", "markdown", "A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on the use and publishing of the Contract precursor content.", 0, 1, copyright));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Precusory content structure and use, i.e., a boilerplate, template, application for a contract such as an insurance policy or benefits under a program, e.g., workers compensation.", 0, 1, type);
          case -1868521062: /*subType*/  return new Property("subType", "CodeableConcept", "Detailed Precusory content type.", 0, 1, subType);
          case 1447404028: /*publisher*/  return new Property("publisher", "Reference(Practitioner|PractitionerRole|Organization)", "The  individual or organization that published the Contract precursor content.", 0, 1, publisher);
          case 1470566394: /*publicationDate*/  return new Property("publicationDate", "dateTime", "The date (and optionally time) when the contract was published. The date must change when the business version changes and it must change if the status code changes. In addition, it should change when the substantive content of the contract changes.", 0, 1, publicationDate);
          case 616500542: /*publicationStatus*/  return new Property("publicationStatus", "code", "draft | active | retired | unknown.", 0, 1, publicationStatus);
          case 1522889671: /*copyright*/  return new Property("copyright", "markdown", "A copyright statement relating to Contract precursor content. Copyright statements are generally legal restrictions on the use and publishing of the Contract precursor content.", 0, 1, copyright);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : new Base[] {this.subType}; // CodeableConcept
        case 1447404028: /*publisher*/ return this.publisher == null ? new Base[0] : new Base[] {this.publisher}; // Reference
        case 1470566394: /*publicationDate*/ return this.publicationDate == null ? new Base[0] : new Base[] {this.publicationDate}; // DateTimeType
        case 616500542: /*publicationStatus*/ return this.publicationStatus == null ? new Base[0] : new Base[] {this.publicationStatus}; // Enumeration<ContractPublicationStatus>
        case 1522889671: /*copyright*/ return this.copyright == null ? new Base[0] : new Base[] {this.copyright}; // MarkdownType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868521062: // subType
          this.subType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 1447404028: // publisher
          this.publisher = castToReference(value); // Reference
          return value;
        case 1470566394: // publicationDate
          this.publicationDate = castToDateTime(value); // DateTimeType
          return value;
        case 616500542: // publicationStatus
          value = new ContractPublicationStatusEnumFactory().fromType(castToCode(value));
          this.publicationStatus = (Enumeration) value; // Enumeration<ContractPublicationStatus>
          return value;
        case 1522889671: // copyright
          this.copyright = castToMarkdown(value); // MarkdownType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subType")) {
          this.subType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("publisher")) {
          this.publisher = castToReference(value); // Reference
        } else if (name.equals("publicationDate")) {
          this.publicationDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("publicationStatus")) {
          value = new ContractPublicationStatusEnumFactory().fromType(castToCode(value));
          this.publicationStatus = (Enumeration) value; // Enumeration<ContractPublicationStatus>
        } else if (name.equals("copyright")) {
          this.copyright = castToMarkdown(value); // MarkdownType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case -1868521062:  return getSubType(); 
        case 1447404028:  return getPublisher(); 
        case 1470566394:  return getPublicationDateElement();
        case 616500542:  return getPublicationStatusElement();
        case 1522889671:  return getCopyrightElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1868521062: /*subType*/ return new String[] {"CodeableConcept"};
        case 1447404028: /*publisher*/ return new String[] {"Reference"};
        case 1470566394: /*publicationDate*/ return new String[] {"dateTime"};
        case 616500542: /*publicationStatus*/ return new String[] {"code"};
        case 1522889671: /*copyright*/ return new String[] {"markdown"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          this.subType = new CodeableConcept();
          return this.subType;
        }
        else if (name.equals("publisher")) {
          this.publisher = new Reference();
          return this.publisher;
        }
        else if (name.equals("publicationDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.publicationDate");
        }
        else if (name.equals("publicationStatus")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.publicationStatus");
        }
        else if (name.equals("copyright")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.copyright");
        }
        else
          return super.addChild(name);
      }

      public ContentDefinitionComponent copy() {
        ContentDefinitionComponent dst = new ContentDefinitionComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.subType = subType == null ? null : subType.copy();
        dst.publisher = publisher == null ? null : publisher.copy();
        dst.publicationDate = publicationDate == null ? null : publicationDate.copy();
        dst.publicationStatus = publicationStatus == null ? null : publicationStatus.copy();
        dst.copyright = copyright == null ? null : copyright.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ContentDefinitionComponent))
          return false;
        ContentDefinitionComponent o = (ContentDefinitionComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(subType, o.subType, true) && compareDeep(publisher, o.publisher, true)
           && compareDeep(publicationDate, o.publicationDate, true) && compareDeep(publicationStatus, o.publicationStatus, true)
           && compareDeep(copyright, o.copyright, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ContentDefinitionComponent))
          return false;
        ContentDefinitionComponent o = (ContentDefinitionComponent) other_;
        return compareValues(publicationDate, o.publicationDate, true) && compareValues(publicationStatus, o.publicationStatus, true)
           && compareValues(copyright, o.copyright, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, subType, publisher
          , publicationDate, publicationStatus, copyright);
      }

  public String fhirType() {
    return "Contract.contentDefinition";

  }

  }

    @Block()
    public static class TermComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Unique identifier for this particular Contract Provision.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contract Term Number", formalDefinition="Unique identifier for this particular Contract Provision." )
        protected Identifier identifier;

        /**
         * When this Contract Provision was issued.
         */
        @Child(name = "issued", type = {DateTimeType.class}, order=2, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contract Term Issue Date Time", formalDefinition="When this Contract Provision was issued." )
        protected DateTimeType issued;

        /**
         * Relevant time or time-period when this Contract Provision is applicable.
         */
        @Child(name = "applies", type = {Period.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Contract Term Effective Time", formalDefinition="Relevant time or time-period when this Contract Provision is applicable." )
        protected Period applies;

        /**
         * The entity that the term applies to.
         */
        @Child(name = "topic", type = {CodeableConcept.class, Reference.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Term Concern", formalDefinition="The entity that the term applies to." )
        protected Type topic;

        /**
         * A legal clause or condition contained within a contract that requires one or both parties to perform a particular requirement by some specified time or prevents one or both parties from performing a particular requirement by some specified time.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Type or Form", formalDefinition="A legal clause or condition contained within a contract that requires one or both parties to perform a particular requirement by some specified time or prevents one or both parties from performing a particular requirement by some specified time." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-term-type")
        protected CodeableConcept type;

        /**
         * A specialized legal clause or condition based on overarching contract type.
         */
        @Child(name = "subType", type = {CodeableConcept.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Type specific classification", formalDefinition="A specialized legal clause or condition based on overarching contract type." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-term-subtype")
        protected CodeableConcept subType;

        /**
         * Statement of a provision in a policy or a contract.
         */
        @Child(name = "text", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Term Statement", formalDefinition="Statement of a provision in a policy or a contract." )
        protected StringType text;

        /**
         * Security labels that protect the handling of information about the term and its elements, which may be specifically identified..
         */
        @Child(name = "securityLabel", type = {}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Protection for the Term", formalDefinition="Security labels that protect the handling of information about the term and its elements, which may be specifically identified.." )
        protected List<SecurityLabelComponent> securityLabel;

        /**
         * The matter of concern in the context of this provision of the agrement.
         */
        @Child(name = "offer", type = {}, order=9, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Context of the Contract term", formalDefinition="The matter of concern in the context of this provision of the agrement." )
        protected ContractOfferComponent offer;

        /**
         * Contract Term Asset List.
         */
        @Child(name = "asset", type = {}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Term Asset List", formalDefinition="Contract Term Asset List." )
        protected List<ContractAssetComponent> asset;

        /**
         * An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.
         */
        @Child(name = "action", type = {}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Entity being ascribed responsibility", formalDefinition="An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place." )
        protected List<ActionComponent> action;

        /**
         * Nested group of Contract Provisions.
         */
        @Child(name = "group", type = {TermComponent.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Nested Contract Term Group", formalDefinition="Nested group of Contract Provisions." )
        protected List<TermComponent> group;

        private static final long serialVersionUID = -460907186L;

    /**
     * Constructor
     */
      public TermComponent() {
        super();
      }

    /**
     * Constructor
     */
      public TermComponent(ContractOfferComponent offer) {
        super();
        this.offer = offer;
      }

        /**
         * @return {@link #identifier} (Unique identifier for this particular Contract Provision.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Unique identifier for this particular Contract Provision.)
         */
        public TermComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #issued} (When this Contract Provision was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
         */
        public DateTimeType getIssuedElement() { 
          if (this.issued == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.issued");
            else if (Configuration.doAutoCreate())
              this.issued = new DateTimeType(); // bb
          return this.issued;
        }

        public boolean hasIssuedElement() { 
          return this.issued != null && !this.issued.isEmpty();
        }

        public boolean hasIssued() { 
          return this.issued != null && !this.issued.isEmpty();
        }

        /**
         * @param value {@link #issued} (When this Contract Provision was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
         */
        public TermComponent setIssuedElement(DateTimeType value) { 
          this.issued = value;
          return this;
        }

        /**
         * @return When this Contract Provision was issued.
         */
        public Date getIssued() { 
          return this.issued == null ? null : this.issued.getValue();
        }

        /**
         * @param value When this Contract Provision was issued.
         */
        public TermComponent setIssued(Date value) { 
          if (value == null)
            this.issued = null;
          else {
            if (this.issued == null)
              this.issued = new DateTimeType();
            this.issued.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #applies} (Relevant time or time-period when this Contract Provision is applicable.)
         */
        public Period getApplies() { 
          if (this.applies == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.applies");
            else if (Configuration.doAutoCreate())
              this.applies = new Period(); // cc
          return this.applies;
        }

        public boolean hasApplies() { 
          return this.applies != null && !this.applies.isEmpty();
        }

        /**
         * @param value {@link #applies} (Relevant time or time-period when this Contract Provision is applicable.)
         */
        public TermComponent setApplies(Period value) { 
          this.applies = value;
          return this;
        }

        /**
         * @return {@link #topic} (The entity that the term applies to.)
         */
        public Type getTopic() { 
          return this.topic;
        }

        /**
         * @return {@link #topic} (The entity that the term applies to.)
         */
        public CodeableConcept getTopicCodeableConcept() throws FHIRException { 
          if (this.topic == null)
            this.topic = new CodeableConcept();
          if (!(this.topic instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.topic.getClass().getName()+" was encountered");
          return (CodeableConcept) this.topic;
        }

        public boolean hasTopicCodeableConcept() { 
          return this != null && this.topic instanceof CodeableConcept;
        }

        /**
         * @return {@link #topic} (The entity that the term applies to.)
         */
        public Reference getTopicReference() throws FHIRException { 
          if (this.topic == null)
            this.topic = new Reference();
          if (!(this.topic instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.topic.getClass().getName()+" was encountered");
          return (Reference) this.topic;
        }

        public boolean hasTopicReference() { 
          return this != null && this.topic instanceof Reference;
        }

        public boolean hasTopic() { 
          return this.topic != null && !this.topic.isEmpty();
        }

        /**
         * @param value {@link #topic} (The entity that the term applies to.)
         */
        public TermComponent setTopic(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for Contract.term.topic[x]: "+value.fhirType());
          this.topic = value;
          return this;
        }

        /**
         * @return {@link #type} (A legal clause or condition contained within a contract that requires one or both parties to perform a particular requirement by some specified time or prevents one or both parties from performing a particular requirement by some specified time.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (A legal clause or condition contained within a contract that requires one or both parties to perform a particular requirement by some specified time or prevents one or both parties from performing a particular requirement by some specified time.)
         */
        public TermComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #subType} (A specialized legal clause or condition based on overarching contract type.)
         */
        public CodeableConcept getSubType() { 
          if (this.subType == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.subType");
            else if (Configuration.doAutoCreate())
              this.subType = new CodeableConcept(); // cc
          return this.subType;
        }

        public boolean hasSubType() { 
          return this.subType != null && !this.subType.isEmpty();
        }

        /**
         * @param value {@link #subType} (A specialized legal clause or condition based on overarching contract type.)
         */
        public TermComponent setSubType(CodeableConcept value) { 
          this.subType = value;
          return this;
        }

        /**
         * @return {@link #text} (Statement of a provision in a policy or a contract.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Statement of a provision in a policy or a contract.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public TermComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Statement of a provision in a policy or a contract.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Statement of a provision in a policy or a contract.
         */
        public TermComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #securityLabel} (Security labels that protect the handling of information about the term and its elements, which may be specifically identified..)
         */
        public List<SecurityLabelComponent> getSecurityLabel() { 
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<SecurityLabelComponent>();
          return this.securityLabel;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TermComponent setSecurityLabel(List<SecurityLabelComponent> theSecurityLabel) { 
          this.securityLabel = theSecurityLabel;
          return this;
        }

        public boolean hasSecurityLabel() { 
          if (this.securityLabel == null)
            return false;
          for (SecurityLabelComponent item : this.securityLabel)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public SecurityLabelComponent addSecurityLabel() { //3
          SecurityLabelComponent t = new SecurityLabelComponent();
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<SecurityLabelComponent>();
          this.securityLabel.add(t);
          return t;
        }

        public TermComponent addSecurityLabel(SecurityLabelComponent t) { //3
          if (t == null)
            return this;
          if (this.securityLabel == null)
            this.securityLabel = new ArrayList<SecurityLabelComponent>();
          this.securityLabel.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #securityLabel}, creating it if it does not already exist
         */
        public SecurityLabelComponent getSecurityLabelFirstRep() { 
          if (getSecurityLabel().isEmpty()) {
            addSecurityLabel();
          }
          return getSecurityLabel().get(0);
        }

        /**
         * @return {@link #offer} (The matter of concern in the context of this provision of the agrement.)
         */
        public ContractOfferComponent getOffer() { 
          if (this.offer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create TermComponent.offer");
            else if (Configuration.doAutoCreate())
              this.offer = new ContractOfferComponent(); // cc
          return this.offer;
        }

        public boolean hasOffer() { 
          return this.offer != null && !this.offer.isEmpty();
        }

        /**
         * @param value {@link #offer} (The matter of concern in the context of this provision of the agrement.)
         */
        public TermComponent setOffer(ContractOfferComponent value) { 
          this.offer = value;
          return this;
        }

        /**
         * @return {@link #asset} (Contract Term Asset List.)
         */
        public List<ContractAssetComponent> getAsset() { 
          if (this.asset == null)
            this.asset = new ArrayList<ContractAssetComponent>();
          return this.asset;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TermComponent setAsset(List<ContractAssetComponent> theAsset) { 
          this.asset = theAsset;
          return this;
        }

        public boolean hasAsset() { 
          if (this.asset == null)
            return false;
          for (ContractAssetComponent item : this.asset)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ContractAssetComponent addAsset() { //3
          ContractAssetComponent t = new ContractAssetComponent();
          if (this.asset == null)
            this.asset = new ArrayList<ContractAssetComponent>();
          this.asset.add(t);
          return t;
        }

        public TermComponent addAsset(ContractAssetComponent t) { //3
          if (t == null)
            return this;
          if (this.asset == null)
            this.asset = new ArrayList<ContractAssetComponent>();
          this.asset.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #asset}, creating it if it does not already exist
         */
        public ContractAssetComponent getAssetFirstRep() { 
          if (getAsset().isEmpty()) {
            addAsset();
          }
          return getAsset().get(0);
        }

        /**
         * @return {@link #action} (An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.)
         */
        public List<ActionComponent> getAction() { 
          if (this.action == null)
            this.action = new ArrayList<ActionComponent>();
          return this.action;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TermComponent setAction(List<ActionComponent> theAction) { 
          this.action = theAction;
          return this;
        }

        public boolean hasAction() { 
          if (this.action == null)
            return false;
          for (ActionComponent item : this.action)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ActionComponent addAction() { //3
          ActionComponent t = new ActionComponent();
          if (this.action == null)
            this.action = new ArrayList<ActionComponent>();
          this.action.add(t);
          return t;
        }

        public TermComponent addAction(ActionComponent t) { //3
          if (t == null)
            return this;
          if (this.action == null)
            this.action = new ArrayList<ActionComponent>();
          this.action.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #action}, creating it if it does not already exist
         */
        public ActionComponent getActionFirstRep() { 
          if (getAction().isEmpty()) {
            addAction();
          }
          return getAction().get(0);
        }

        /**
         * @return {@link #group} (Nested group of Contract Provisions.)
         */
        public List<TermComponent> getGroup() { 
          if (this.group == null)
            this.group = new ArrayList<TermComponent>();
          return this.group;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public TermComponent setGroup(List<TermComponent> theGroup) { 
          this.group = theGroup;
          return this;
        }

        public boolean hasGroup() { 
          if (this.group == null)
            return false;
          for (TermComponent item : this.group)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public TermComponent addGroup() { //3
          TermComponent t = new TermComponent();
          if (this.group == null)
            this.group = new ArrayList<TermComponent>();
          this.group.add(t);
          return t;
        }

        public TermComponent addGroup(TermComponent t) { //3
          if (t == null)
            return this;
          if (this.group == null)
            this.group = new ArrayList<TermComponent>();
          this.group.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #group}, creating it if it does not already exist
         */
        public TermComponent getGroupFirstRep() { 
          if (getGroup().isEmpty()) {
            addGroup();
          }
          return getGroup().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("identifier", "Identifier", "Unique identifier for this particular Contract Provision.", 0, 1, identifier));
          children.add(new Property("issued", "dateTime", "When this Contract Provision was issued.", 0, 1, issued));
          children.add(new Property("applies", "Period", "Relevant time or time-period when this Contract Provision is applicable.", 0, 1, applies));
          children.add(new Property("topic[x]", "CodeableConcept|Reference(Any)", "The entity that the term applies to.", 0, 1, topic));
          children.add(new Property("type", "CodeableConcept", "A legal clause or condition contained within a contract that requires one or both parties to perform a particular requirement by some specified time or prevents one or both parties from performing a particular requirement by some specified time.", 0, 1, type));
          children.add(new Property("subType", "CodeableConcept", "A specialized legal clause or condition based on overarching contract type.", 0, 1, subType));
          children.add(new Property("text", "string", "Statement of a provision in a policy or a contract.", 0, 1, text));
          children.add(new Property("securityLabel", "", "Security labels that protect the handling of information about the term and its elements, which may be specifically identified..", 0, java.lang.Integer.MAX_VALUE, securityLabel));
          children.add(new Property("offer", "", "The matter of concern in the context of this provision of the agrement.", 0, 1, offer));
          children.add(new Property("asset", "", "Contract Term Asset List.", 0, java.lang.Integer.MAX_VALUE, asset));
          children.add(new Property("action", "", "An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.", 0, java.lang.Integer.MAX_VALUE, action));
          children.add(new Property("group", "@Contract.term", "Nested group of Contract Provisions.", 0, java.lang.Integer.MAX_VALUE, group));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier for this particular Contract Provision.", 0, 1, identifier);
          case -1179159893: /*issued*/  return new Property("issued", "dateTime", "When this Contract Provision was issued.", 0, 1, issued);
          case -793235316: /*applies*/  return new Property("applies", "Period", "Relevant time or time-period when this Contract Provision is applicable.", 0, 1, applies);
          case -957295375: /*topic[x]*/  return new Property("topic[x]", "CodeableConcept|Reference(Any)", "The entity that the term applies to.", 0, 1, topic);
          case 110546223: /*topic*/  return new Property("topic[x]", "CodeableConcept|Reference(Any)", "The entity that the term applies to.", 0, 1, topic);
          case 777778802: /*topicCodeableConcept*/  return new Property("topic[x]", "CodeableConcept|Reference(Any)", "The entity that the term applies to.", 0, 1, topic);
          case -343345444: /*topicReference*/  return new Property("topic[x]", "CodeableConcept|Reference(Any)", "The entity that the term applies to.", 0, 1, topic);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "A legal clause or condition contained within a contract that requires one or both parties to perform a particular requirement by some specified time or prevents one or both parties from performing a particular requirement by some specified time.", 0, 1, type);
          case -1868521062: /*subType*/  return new Property("subType", "CodeableConcept", "A specialized legal clause or condition based on overarching contract type.", 0, 1, subType);
          case 3556653: /*text*/  return new Property("text", "string", "Statement of a provision in a policy or a contract.", 0, 1, text);
          case -722296940: /*securityLabel*/  return new Property("securityLabel", "", "Security labels that protect the handling of information about the term and its elements, which may be specifically identified..", 0, java.lang.Integer.MAX_VALUE, securityLabel);
          case 105650780: /*offer*/  return new Property("offer", "", "The matter of concern in the context of this provision of the agrement.", 0, 1, offer);
          case 93121264: /*asset*/  return new Property("asset", "", "Contract Term Asset List.", 0, java.lang.Integer.MAX_VALUE, asset);
          case -1422950858: /*action*/  return new Property("action", "", "An actor taking a role in an activity for which it can be assigned some degree of responsibility for the activity taking place.", 0, java.lang.Integer.MAX_VALUE, action);
          case 98629247: /*group*/  return new Property("group", "@Contract.term", "Nested group of Contract Provisions.", 0, java.lang.Integer.MAX_VALUE, group);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -1179159893: /*issued*/ return this.issued == null ? new Base[0] : new Base[] {this.issued}; // DateTimeType
        case -793235316: /*applies*/ return this.applies == null ? new Base[0] : new Base[] {this.applies}; // Period
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : new Base[] {this.topic}; // Type
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : new Base[] {this.subType}; // CodeableConcept
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -722296940: /*securityLabel*/ return this.securityLabel == null ? new Base[0] : this.securityLabel.toArray(new Base[this.securityLabel.size()]); // SecurityLabelComponent
        case 105650780: /*offer*/ return this.offer == null ? new Base[0] : new Base[] {this.offer}; // ContractOfferComponent
        case 93121264: /*asset*/ return this.asset == null ? new Base[0] : this.asset.toArray(new Base[this.asset.size()]); // ContractAssetComponent
        case -1422950858: /*action*/ return this.action == null ? new Base[0] : this.action.toArray(new Base[this.action.size()]); // ActionComponent
        case 98629247: /*group*/ return this.group == null ? new Base[0] : this.group.toArray(new Base[this.group.size()]); // TermComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -1179159893: // issued
          this.issued = castToDateTime(value); // DateTimeType
          return value;
        case -793235316: // applies
          this.applies = castToPeriod(value); // Period
          return value;
        case 110546223: // topic
          this.topic = castToType(value); // Type
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868521062: // subType
          this.subType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case -722296940: // securityLabel
          this.getSecurityLabel().add((SecurityLabelComponent) value); // SecurityLabelComponent
          return value;
        case 105650780: // offer
          this.offer = (ContractOfferComponent) value; // ContractOfferComponent
          return value;
        case 93121264: // asset
          this.getAsset().add((ContractAssetComponent) value); // ContractAssetComponent
          return value;
        case -1422950858: // action
          this.getAction().add((ActionComponent) value); // ActionComponent
          return value;
        case 98629247: // group
          this.getGroup().add((TermComponent) value); // TermComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("issued")) {
          this.issued = castToDateTime(value); // DateTimeType
        } else if (name.equals("applies")) {
          this.applies = castToPeriod(value); // Period
        } else if (name.equals("topic[x]")) {
          this.topic = castToType(value); // Type
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subType")) {
          this.subType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("securityLabel")) {
          this.getSecurityLabel().add((SecurityLabelComponent) value);
        } else if (name.equals("offer")) {
          this.offer = (ContractOfferComponent) value; // ContractOfferComponent
        } else if (name.equals("asset")) {
          this.getAsset().add((ContractAssetComponent) value);
        } else if (name.equals("action")) {
          this.getAction().add((ActionComponent) value);
        } else if (name.equals("group")) {
          this.getGroup().add((TermComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return getIdentifier(); 
        case -1179159893:  return getIssuedElement();
        case -793235316:  return getApplies(); 
        case -957295375:  return getTopic(); 
        case 110546223:  return getTopic(); 
        case 3575610:  return getType(); 
        case -1868521062:  return getSubType(); 
        case 3556653:  return getTextElement();
        case -722296940:  return addSecurityLabel(); 
        case 105650780:  return getOffer(); 
        case 93121264:  return addAsset(); 
        case -1422950858:  return addAction(); 
        case 98629247:  return addGroup(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -1179159893: /*issued*/ return new String[] {"dateTime"};
        case -793235316: /*applies*/ return new String[] {"Period"};
        case 110546223: /*topic*/ return new String[] {"CodeableConcept", "Reference"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1868521062: /*subType*/ return new String[] {"CodeableConcept"};
        case 3556653: /*text*/ return new String[] {"string"};
        case -722296940: /*securityLabel*/ return new String[] {};
        case 105650780: /*offer*/ return new String[] {};
        case 93121264: /*asset*/ return new String[] {};
        case -1422950858: /*action*/ return new String[] {};
        case 98629247: /*group*/ return new String[] {"@Contract.term"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.issued");
        }
        else if (name.equals("applies")) {
          this.applies = new Period();
          return this.applies;
        }
        else if (name.equals("topicCodeableConcept")) {
          this.topic = new CodeableConcept();
          return this.topic;
        }
        else if (name.equals("topicReference")) {
          this.topic = new Reference();
          return this.topic;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          this.subType = new CodeableConcept();
          return this.subType;
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.text");
        }
        else if (name.equals("securityLabel")) {
          return addSecurityLabel();
        }
        else if (name.equals("offer")) {
          this.offer = new ContractOfferComponent();
          return this.offer;
        }
        else if (name.equals("asset")) {
          return addAsset();
        }
        else if (name.equals("action")) {
          return addAction();
        }
        else if (name.equals("group")) {
          return addGroup();
        }
        else
          return super.addChild(name);
      }

      public TermComponent copy() {
        TermComponent dst = new TermComponent();
        copyValues(dst);
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.applies = applies == null ? null : applies.copy();
        dst.topic = topic == null ? null : topic.copy();
        dst.type = type == null ? null : type.copy();
        dst.subType = subType == null ? null : subType.copy();
        dst.text = text == null ? null : text.copy();
        if (securityLabel != null) {
          dst.securityLabel = new ArrayList<SecurityLabelComponent>();
          for (SecurityLabelComponent i : securityLabel)
            dst.securityLabel.add(i.copy());
        };
        dst.offer = offer == null ? null : offer.copy();
        if (asset != null) {
          dst.asset = new ArrayList<ContractAssetComponent>();
          for (ContractAssetComponent i : asset)
            dst.asset.add(i.copy());
        };
        if (action != null) {
          dst.action = new ArrayList<ActionComponent>();
          for (ActionComponent i : action)
            dst.action.add(i.copy());
        };
        if (group != null) {
          dst.group = new ArrayList<TermComponent>();
          for (TermComponent i : group)
            dst.group.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof TermComponent))
          return false;
        TermComponent o = (TermComponent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(issued, o.issued, true) && compareDeep(applies, o.applies, true)
           && compareDeep(topic, o.topic, true) && compareDeep(type, o.type, true) && compareDeep(subType, o.subType, true)
           && compareDeep(text, o.text, true) && compareDeep(securityLabel, o.securityLabel, true) && compareDeep(offer, o.offer, true)
           && compareDeep(asset, o.asset, true) && compareDeep(action, o.action, true) && compareDeep(group, o.group, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof TermComponent))
          return false;
        TermComponent o = (TermComponent) other_;
        return compareValues(issued, o.issued, true) && compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, issued, applies
          , topic, type, subType, text, securityLabel, offer, asset, action, group);
      }

  public String fhirType() {
    return "Contract.term";

  }

  }

    @Block()
    public static class SecurityLabelComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Number used to link this term or term element to the applicable Security Label.
         */
        @Child(name = "number", type = {UnsignedIntType.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Link to Security Labels", formalDefinition="Number used to link this term or term element to the applicable Security Label." )
        protected List<UnsignedIntType> number;

        /**
         * Security label privacy tag that species the level of confidentiality protection required for this term and/or term elements.
         */
        @Child(name = "classification", type = {Coding.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Confidentiality Protection", formalDefinition="Security label privacy tag that species the level of confidentiality protection required for this term and/or term elements." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-security-classification")
        protected Coding classification;

        /**
         * Security label privacy tag that species the applicable privacy and security policies governing this term and/or term elements.
         */
        @Child(name = "category", type = {Coding.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Applicable Policy", formalDefinition="Security label privacy tag that species the applicable privacy and security policies governing this term and/or term elements." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-security-category")
        protected List<Coding> category;

        /**
         * Security label privacy tag that species the manner in which term and/or term elements are to be protected.
         */
        @Child(name = "control", type = {Coding.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Handling Instructions", formalDefinition="Security label privacy tag that species the manner in which term and/or term elements are to be protected." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-security-control")
        protected List<Coding> control;

        private static final long serialVersionUID = 788281758L;

    /**
     * Constructor
     */
      public SecurityLabelComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SecurityLabelComponent(Coding classification) {
        super();
        this.classification = classification;
      }

        /**
         * @return {@link #number} (Number used to link this term or term element to the applicable Security Label.)
         */
        public List<UnsignedIntType> getNumber() { 
          if (this.number == null)
            this.number = new ArrayList<UnsignedIntType>();
          return this.number;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SecurityLabelComponent setNumber(List<UnsignedIntType> theNumber) { 
          this.number = theNumber;
          return this;
        }

        public boolean hasNumber() { 
          if (this.number == null)
            return false;
          for (UnsignedIntType item : this.number)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #number} (Number used to link this term or term element to the applicable Security Label.)
         */
        public UnsignedIntType addNumberElement() {//2 
          UnsignedIntType t = new UnsignedIntType();
          if (this.number == null)
            this.number = new ArrayList<UnsignedIntType>();
          this.number.add(t);
          return t;
        }

        /**
         * @param value {@link #number} (Number used to link this term or term element to the applicable Security Label.)
         */
        public SecurityLabelComponent addNumber(int value) { //1
          UnsignedIntType t = new UnsignedIntType();
          t.setValue(value);
          if (this.number == null)
            this.number = new ArrayList<UnsignedIntType>();
          this.number.add(t);
          return this;
        }

        /**
         * @param value {@link #number} (Number used to link this term or term element to the applicable Security Label.)
         */
        public boolean hasNumber(int value) { 
          if (this.number == null)
            return false;
          for (UnsignedIntType v : this.number)
            if (v.getValue().equals(value)) // unsignedInt
              return true;
          return false;
        }

        /**
         * @return {@link #classification} (Security label privacy tag that species the level of confidentiality protection required for this term and/or term elements.)
         */
        public Coding getClassification() { 
          if (this.classification == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SecurityLabelComponent.classification");
            else if (Configuration.doAutoCreate())
              this.classification = new Coding(); // cc
          return this.classification;
        }

        public boolean hasClassification() { 
          return this.classification != null && !this.classification.isEmpty();
        }

        /**
         * @param value {@link #classification} (Security label privacy tag that species the level of confidentiality protection required for this term and/or term elements.)
         */
        public SecurityLabelComponent setClassification(Coding value) { 
          this.classification = value;
          return this;
        }

        /**
         * @return {@link #category} (Security label privacy tag that species the applicable privacy and security policies governing this term and/or term elements.)
         */
        public List<Coding> getCategory() { 
          if (this.category == null)
            this.category = new ArrayList<Coding>();
          return this.category;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SecurityLabelComponent setCategory(List<Coding> theCategory) { 
          this.category = theCategory;
          return this;
        }

        public boolean hasCategory() { 
          if (this.category == null)
            return false;
          for (Coding item : this.category)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addCategory() { //3
          Coding t = new Coding();
          if (this.category == null)
            this.category = new ArrayList<Coding>();
          this.category.add(t);
          return t;
        }

        public SecurityLabelComponent addCategory(Coding t) { //3
          if (t == null)
            return this;
          if (this.category == null)
            this.category = new ArrayList<Coding>();
          this.category.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #category}, creating it if it does not already exist
         */
        public Coding getCategoryFirstRep() { 
          if (getCategory().isEmpty()) {
            addCategory();
          }
          return getCategory().get(0);
        }

        /**
         * @return {@link #control} (Security label privacy tag that species the manner in which term and/or term elements are to be protected.)
         */
        public List<Coding> getControl() { 
          if (this.control == null)
            this.control = new ArrayList<Coding>();
          return this.control;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SecurityLabelComponent setControl(List<Coding> theControl) { 
          this.control = theControl;
          return this;
        }

        public boolean hasControl() { 
          if (this.control == null)
            return false;
          for (Coding item : this.control)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Coding addControl() { //3
          Coding t = new Coding();
          if (this.control == null)
            this.control = new ArrayList<Coding>();
          this.control.add(t);
          return t;
        }

        public SecurityLabelComponent addControl(Coding t) { //3
          if (t == null)
            return this;
          if (this.control == null)
            this.control = new ArrayList<Coding>();
          this.control.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #control}, creating it if it does not already exist
         */
        public Coding getControlFirstRep() { 
          if (getControl().isEmpty()) {
            addControl();
          }
          return getControl().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("number", "unsignedInt", "Number used to link this term or term element to the applicable Security Label.", 0, java.lang.Integer.MAX_VALUE, number));
          children.add(new Property("classification", "Coding", "Security label privacy tag that species the level of confidentiality protection required for this term and/or term elements.", 0, 1, classification));
          children.add(new Property("category", "Coding", "Security label privacy tag that species the applicable privacy and security policies governing this term and/or term elements.", 0, java.lang.Integer.MAX_VALUE, category));
          children.add(new Property("control", "Coding", "Security label privacy tag that species the manner in which term and/or term elements are to be protected.", 0, java.lang.Integer.MAX_VALUE, control));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1034364087: /*number*/  return new Property("number", "unsignedInt", "Number used to link this term or term element to the applicable Security Label.", 0, java.lang.Integer.MAX_VALUE, number);
          case 382350310: /*classification*/  return new Property("classification", "Coding", "Security label privacy tag that species the level of confidentiality protection required for this term and/or term elements.", 0, 1, classification);
          case 50511102: /*category*/  return new Property("category", "Coding", "Security label privacy tag that species the applicable privacy and security policies governing this term and/or term elements.", 0, java.lang.Integer.MAX_VALUE, category);
          case 951543133: /*control*/  return new Property("control", "Coding", "Security label privacy tag that species the manner in which term and/or term elements are to be protected.", 0, java.lang.Integer.MAX_VALUE, control);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return this.number == null ? new Base[0] : this.number.toArray(new Base[this.number.size()]); // UnsignedIntType
        case 382350310: /*classification*/ return this.classification == null ? new Base[0] : new Base[] {this.classification}; // Coding
        case 50511102: /*category*/ return this.category == null ? new Base[0] : this.category.toArray(new Base[this.category.size()]); // Coding
        case 951543133: /*control*/ return this.control == null ? new Base[0] : this.control.toArray(new Base[this.control.size()]); // Coding
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1034364087: // number
          this.getNumber().add(castToUnsignedInt(value)); // UnsignedIntType
          return value;
        case 382350310: // classification
          this.classification = castToCoding(value); // Coding
          return value;
        case 50511102: // category
          this.getCategory().add(castToCoding(value)); // Coding
          return value;
        case 951543133: // control
          this.getControl().add(castToCoding(value)); // Coding
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("number")) {
          this.getNumber().add(castToUnsignedInt(value));
        } else if (name.equals("classification")) {
          this.classification = castToCoding(value); // Coding
        } else if (name.equals("category")) {
          this.getCategory().add(castToCoding(value));
        } else if (name.equals("control")) {
          this.getControl().add(castToCoding(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087:  return addNumberElement();
        case 382350310:  return getClassification(); 
        case 50511102:  return addCategory(); 
        case 951543133:  return addControl(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1034364087: /*number*/ return new String[] {"unsignedInt"};
        case 382350310: /*classification*/ return new String[] {"Coding"};
        case 50511102: /*category*/ return new String[] {"Coding"};
        case 951543133: /*control*/ return new String[] {"Coding"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("number")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.number");
        }
        else if (name.equals("classification")) {
          this.classification = new Coding();
          return this.classification;
        }
        else if (name.equals("category")) {
          return addCategory();
        }
        else if (name.equals("control")) {
          return addControl();
        }
        else
          return super.addChild(name);
      }

      public SecurityLabelComponent copy() {
        SecurityLabelComponent dst = new SecurityLabelComponent();
        copyValues(dst);
        if (number != null) {
          dst.number = new ArrayList<UnsignedIntType>();
          for (UnsignedIntType i : number)
            dst.number.add(i.copy());
        };
        dst.classification = classification == null ? null : classification.copy();
        if (category != null) {
          dst.category = new ArrayList<Coding>();
          for (Coding i : category)
            dst.category.add(i.copy());
        };
        if (control != null) {
          dst.control = new ArrayList<Coding>();
          for (Coding i : control)
            dst.control.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SecurityLabelComponent))
          return false;
        SecurityLabelComponent o = (SecurityLabelComponent) other_;
        return compareDeep(number, o.number, true) && compareDeep(classification, o.classification, true)
           && compareDeep(category, o.category, true) && compareDeep(control, o.control, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SecurityLabelComponent))
          return false;
        SecurityLabelComponent o = (SecurityLabelComponent) other_;
        return compareValues(number, o.number, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(number, classification, category
          , control);
      }

  public String fhirType() {
    return "Contract.term.securityLabel";

  }

  }

    @Block()
    public static class ContractOfferComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Unique identifier for this particular Contract Provision.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=1, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Offer business ID", formalDefinition="Unique identifier for this particular Contract Provision." )
        protected List<Identifier> identifier;

        /**
         * Offer Recipient.
         */
        @Child(name = "party", type = {}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Offer Recipient", formalDefinition="Offer Recipient." )
        protected List<ContractPartyComponent> party;

        /**
         * The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).
         */
        @Child(name = "topic", type = {Reference.class}, order=3, min=0, max=1, modifier=false, summary=true)
        @Description(shortDefinition="Negotiable offer asset", formalDefinition="The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30)." )
        protected Reference topic;

        /**
         * The actual object that is the target of the reference (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        protected Resource topicTarget;

        /**
         * Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Offer Type or Form", formalDefinition="Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-term-type")
        protected CodeableConcept type;

        /**
         * Type of choice made by accepting party with respect to an offer made by an offeror/ grantee.
         */
        @Child(name = "decision", type = {CodeableConcept.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Accepting party choice", formalDefinition="Type of choice made by accepting party with respect to an offer made by an offeror/ grantee." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://terminology.hl7.org/ValueSet/v3-ActConsentDirective")
        protected CodeableConcept decision;

        /**
         * How the decision about a Contract was conveyed.
         */
        @Child(name = "decisionMode", type = {CodeableConcept.class}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="How decision is conveyed", formalDefinition="How the decision about a Contract was conveyed." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-decision-mode")
        protected List<CodeableConcept> decisionMode;

        /**
         * Response to offer text.
         */
        @Child(name = "answer", type = {}, order=7, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Response to offer text", formalDefinition="Response to offer text." )
        protected List<AnswerComponent> answer;

        /**
         * Human readable form of this Contract Offer.
         */
        @Child(name = "text", type = {StringType.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Human readable offer text", formalDefinition="Human readable form of this Contract Offer." )
        protected StringType text;

        /**
         * The id of the clause or question text of the offer in the referenced questionnaire/response.
         */
        @Child(name = "linkId", type = {StringType.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to text", formalDefinition="The id of the clause or question text of the offer in the referenced questionnaire/response." )
        protected List<StringType> linkId;

        /**
         * Security labels that protects the offer.
         */
        @Child(name = "securityLabelNumber", type = {UnsignedIntType.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Offer restriction numbers", formalDefinition="Security labels that protects the offer." )
        protected List<UnsignedIntType> securityLabelNumber;

        private static final long serialVersionUID = -395674449L;

    /**
     * Constructor
     */
      public ContractOfferComponent() {
        super();
      }

        /**
         * @return {@link #identifier} (Unique identifier for this particular Contract Provision.)
         */
        public List<Identifier> getIdentifier() { 
          if (this.identifier == null)
            this.identifier = new ArrayList<Identifier>();
          return this.identifier;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractOfferComponent setIdentifier(List<Identifier> theIdentifier) { 
          this.identifier = theIdentifier;
          return this;
        }

        public boolean hasIdentifier() { 
          if (this.identifier == null)
            return false;
          for (Identifier item : this.identifier)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Identifier addIdentifier() { //3
          Identifier t = new Identifier();
          if (this.identifier == null)
            this.identifier = new ArrayList<Identifier>();
          this.identifier.add(t);
          return t;
        }

        public ContractOfferComponent addIdentifier(Identifier t) { //3
          if (t == null)
            return this;
          if (this.identifier == null)
            this.identifier = new ArrayList<Identifier>();
          this.identifier.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
         */
        public Identifier getIdentifierFirstRep() { 
          if (getIdentifier().isEmpty()) {
            addIdentifier();
          }
          return getIdentifier().get(0);
        }

        /**
         * @return {@link #party} (Offer Recipient.)
         */
        public List<ContractPartyComponent> getParty() { 
          if (this.party == null)
            this.party = new ArrayList<ContractPartyComponent>();
          return this.party;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractOfferComponent setParty(List<ContractPartyComponent> theParty) { 
          this.party = theParty;
          return this;
        }

        public boolean hasParty() { 
          if (this.party == null)
            return false;
          for (ContractPartyComponent item : this.party)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ContractPartyComponent addParty() { //3
          ContractPartyComponent t = new ContractPartyComponent();
          if (this.party == null)
            this.party = new ArrayList<ContractPartyComponent>();
          this.party.add(t);
          return t;
        }

        public ContractOfferComponent addParty(ContractPartyComponent t) { //3
          if (t == null)
            return this;
          if (this.party == null)
            this.party = new ArrayList<ContractPartyComponent>();
          this.party.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #party}, creating it if it does not already exist
         */
        public ContractPartyComponent getPartyFirstRep() { 
          if (getParty().isEmpty()) {
            addParty();
          }
          return getParty().get(0);
        }

        /**
         * @return {@link #topic} (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        public Reference getTopic() { 
          if (this.topic == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractOfferComponent.topic");
            else if (Configuration.doAutoCreate())
              this.topic = new Reference(); // cc
          return this.topic;
        }

        public boolean hasTopic() { 
          return this.topic != null && !this.topic.isEmpty();
        }

        /**
         * @param value {@link #topic} (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        public ContractOfferComponent setTopic(Reference value) { 
          this.topic = value;
          return this;
        }

        /**
         * @return {@link #topic} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        public Resource getTopicTarget() { 
          return this.topicTarget;
        }

        /**
         * @param value {@link #topic} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).)
         */
        public ContractOfferComponent setTopicTarget(Resource value) { 
          this.topicTarget = value;
          return this;
        }

        /**
         * @return {@link #type} (Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractOfferComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.)
         */
        public ContractOfferComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #decision} (Type of choice made by accepting party with respect to an offer made by an offeror/ grantee.)
         */
        public CodeableConcept getDecision() { 
          if (this.decision == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractOfferComponent.decision");
            else if (Configuration.doAutoCreate())
              this.decision = new CodeableConcept(); // cc
          return this.decision;
        }

        public boolean hasDecision() { 
          return this.decision != null && !this.decision.isEmpty();
        }

        /**
         * @param value {@link #decision} (Type of choice made by accepting party with respect to an offer made by an offeror/ grantee.)
         */
        public ContractOfferComponent setDecision(CodeableConcept value) { 
          this.decision = value;
          return this;
        }

        /**
         * @return {@link #decisionMode} (How the decision about a Contract was conveyed.)
         */
        public List<CodeableConcept> getDecisionMode() { 
          if (this.decisionMode == null)
            this.decisionMode = new ArrayList<CodeableConcept>();
          return this.decisionMode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractOfferComponent setDecisionMode(List<CodeableConcept> theDecisionMode) { 
          this.decisionMode = theDecisionMode;
          return this;
        }

        public boolean hasDecisionMode() { 
          if (this.decisionMode == null)
            return false;
          for (CodeableConcept item : this.decisionMode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addDecisionMode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.decisionMode == null)
            this.decisionMode = new ArrayList<CodeableConcept>();
          this.decisionMode.add(t);
          return t;
        }

        public ContractOfferComponent addDecisionMode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.decisionMode == null)
            this.decisionMode = new ArrayList<CodeableConcept>();
          this.decisionMode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #decisionMode}, creating it if it does not already exist
         */
        public CodeableConcept getDecisionModeFirstRep() { 
          if (getDecisionMode().isEmpty()) {
            addDecisionMode();
          }
          return getDecisionMode().get(0);
        }

        /**
         * @return {@link #answer} (Response to offer text.)
         */
        public List<AnswerComponent> getAnswer() { 
          if (this.answer == null)
            this.answer = new ArrayList<AnswerComponent>();
          return this.answer;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractOfferComponent setAnswer(List<AnswerComponent> theAnswer) { 
          this.answer = theAnswer;
          return this;
        }

        public boolean hasAnswer() { 
          if (this.answer == null)
            return false;
          for (AnswerComponent item : this.answer)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AnswerComponent addAnswer() { //3
          AnswerComponent t = new AnswerComponent();
          if (this.answer == null)
            this.answer = new ArrayList<AnswerComponent>();
          this.answer.add(t);
          return t;
        }

        public ContractOfferComponent addAnswer(AnswerComponent t) { //3
          if (t == null)
            return this;
          if (this.answer == null)
            this.answer = new ArrayList<AnswerComponent>();
          this.answer.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #answer}, creating it if it does not already exist
         */
        public AnswerComponent getAnswerFirstRep() { 
          if (getAnswer().isEmpty()) {
            addAnswer();
          }
          return getAnswer().get(0);
        }

        /**
         * @return {@link #text} (Human readable form of this Contract Offer.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractOfferComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Human readable form of this Contract Offer.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public ContractOfferComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Human readable form of this Contract Offer.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Human readable form of this Contract Offer.
         */
        public ContractOfferComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #linkId} (The id of the clause or question text of the offer in the referenced questionnaire/response.)
         */
        public List<StringType> getLinkId() { 
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          return this.linkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractOfferComponent setLinkId(List<StringType> theLinkId) { 
          this.linkId = theLinkId;
          return this;
        }

        public boolean hasLinkId() { 
          if (this.linkId == null)
            return false;
          for (StringType item : this.linkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #linkId} (The id of the clause or question text of the offer in the referenced questionnaire/response.)
         */
        public StringType addLinkIdElement() {//2 
          StringType t = new StringType();
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          this.linkId.add(t);
          return t;
        }

        /**
         * @param value {@link #linkId} (The id of the clause or question text of the offer in the referenced questionnaire/response.)
         */
        public ContractOfferComponent addLinkId(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          this.linkId.add(t);
          return this;
        }

        /**
         * @param value {@link #linkId} (The id of the clause or question text of the offer in the referenced questionnaire/response.)
         */
        public boolean hasLinkId(String value) { 
          if (this.linkId == null)
            return false;
          for (StringType v : this.linkId)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #securityLabelNumber} (Security labels that protects the offer.)
         */
        public List<UnsignedIntType> getSecurityLabelNumber() { 
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          return this.securityLabelNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractOfferComponent setSecurityLabelNumber(List<UnsignedIntType> theSecurityLabelNumber) { 
          this.securityLabelNumber = theSecurityLabelNumber;
          return this;
        }

        public boolean hasSecurityLabelNumber() { 
          if (this.securityLabelNumber == null)
            return false;
          for (UnsignedIntType item : this.securityLabelNumber)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #securityLabelNumber} (Security labels that protects the offer.)
         */
        public UnsignedIntType addSecurityLabelNumberElement() {//2 
          UnsignedIntType t = new UnsignedIntType();
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          this.securityLabelNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #securityLabelNumber} (Security labels that protects the offer.)
         */
        public ContractOfferComponent addSecurityLabelNumber(int value) { //1
          UnsignedIntType t = new UnsignedIntType();
          t.setValue(value);
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          this.securityLabelNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #securityLabelNumber} (Security labels that protects the offer.)
         */
        public boolean hasSecurityLabelNumber(int value) { 
          if (this.securityLabelNumber == null)
            return false;
          for (UnsignedIntType v : this.securityLabelNumber)
            if (v.getValue().equals(value)) // unsignedInt
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("identifier", "Identifier", "Unique identifier for this particular Contract Provision.", 0, java.lang.Integer.MAX_VALUE, identifier));
          children.add(new Property("party", "", "Offer Recipient.", 0, java.lang.Integer.MAX_VALUE, party));
          children.add(new Property("topic", "Reference(Any)", "The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).", 0, 1, topic));
          children.add(new Property("type", "CodeableConcept", "Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.", 0, 1, type));
          children.add(new Property("decision", "CodeableConcept", "Type of choice made by accepting party with respect to an offer made by an offeror/ grantee.", 0, 1, decision));
          children.add(new Property("decisionMode", "CodeableConcept", "How the decision about a Contract was conveyed.", 0, java.lang.Integer.MAX_VALUE, decisionMode));
          children.add(new Property("answer", "", "Response to offer text.", 0, java.lang.Integer.MAX_VALUE, answer));
          children.add(new Property("text", "string", "Human readable form of this Contract Offer.", 0, 1, text));
          children.add(new Property("linkId", "string", "The id of the clause or question text of the offer in the referenced questionnaire/response.", 0, java.lang.Integer.MAX_VALUE, linkId));
          children.add(new Property("securityLabelNumber", "unsignedInt", "Security labels that protects the offer.", 0, java.lang.Integer.MAX_VALUE, securityLabelNumber));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier for this particular Contract Provision.", 0, java.lang.Integer.MAX_VALUE, identifier);
          case 106437350: /*party*/  return new Property("party", "", "Offer Recipient.", 0, java.lang.Integer.MAX_VALUE, party);
          case 110546223: /*topic*/  return new Property("topic", "Reference(Any)", "The owner of an asset has the residual control rights over the asset: the right to decide all usages of the asset in any way not inconsistent with a prior contract, custom, or law (Hart, 1995, p. 30).", 0, 1, topic);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Type of Contract Provision such as specific requirements, purposes for actions, obligations, prohibitions, e.g. life time maximum benefit.", 0, 1, type);
          case 565719004: /*decision*/  return new Property("decision", "CodeableConcept", "Type of choice made by accepting party with respect to an offer made by an offeror/ grantee.", 0, 1, decision);
          case 675909535: /*decisionMode*/  return new Property("decisionMode", "CodeableConcept", "How the decision about a Contract was conveyed.", 0, java.lang.Integer.MAX_VALUE, decisionMode);
          case -1412808770: /*answer*/  return new Property("answer", "", "Response to offer text.", 0, java.lang.Integer.MAX_VALUE, answer);
          case 3556653: /*text*/  return new Property("text", "string", "Human readable form of this Contract Offer.", 0, 1, text);
          case -1102667083: /*linkId*/  return new Property("linkId", "string", "The id of the clause or question text of the offer in the referenced questionnaire/response.", 0, java.lang.Integer.MAX_VALUE, linkId);
          case -149460995: /*securityLabelNumber*/  return new Property("securityLabelNumber", "unsignedInt", "Security labels that protects the offer.", 0, java.lang.Integer.MAX_VALUE, securityLabelNumber);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 106437350: /*party*/ return this.party == null ? new Base[0] : this.party.toArray(new Base[this.party.size()]); // ContractPartyComponent
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : new Base[] {this.topic}; // Reference
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case 565719004: /*decision*/ return this.decision == null ? new Base[0] : new Base[] {this.decision}; // CodeableConcept
        case 675909535: /*decisionMode*/ return this.decisionMode == null ? new Base[0] : this.decisionMode.toArray(new Base[this.decisionMode.size()]); // CodeableConcept
        case -1412808770: /*answer*/ return this.answer == null ? new Base[0] : this.answer.toArray(new Base[this.answer.size()]); // AnswerComponent
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -1102667083: /*linkId*/ return this.linkId == null ? new Base[0] : this.linkId.toArray(new Base[this.linkId.size()]); // StringType
        case -149460995: /*securityLabelNumber*/ return this.securityLabelNumber == null ? new Base[0] : this.securityLabelNumber.toArray(new Base[this.securityLabelNumber.size()]); // UnsignedIntType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 106437350: // party
          this.getParty().add((ContractPartyComponent) value); // ContractPartyComponent
          return value;
        case 110546223: // topic
          this.topic = castToReference(value); // Reference
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 565719004: // decision
          this.decision = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 675909535: // decisionMode
          this.getDecisionMode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1412808770: // answer
          this.getAnswer().add((AnswerComponent) value); // AnswerComponent
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case -1102667083: // linkId
          this.getLinkId().add(castToString(value)); // StringType
          return value;
        case -149460995: // securityLabelNumber
          this.getSecurityLabelNumber().add(castToUnsignedInt(value)); // UnsignedIntType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("party")) {
          this.getParty().add((ContractPartyComponent) value);
        } else if (name.equals("topic")) {
          this.topic = castToReference(value); // Reference
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("decision")) {
          this.decision = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("decisionMode")) {
          this.getDecisionMode().add(castToCodeableConcept(value));
        } else if (name.equals("answer")) {
          this.getAnswer().add((AnswerComponent) value);
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("linkId")) {
          this.getLinkId().add(castToString(value));
        } else if (name.equals("securityLabelNumber")) {
          this.getSecurityLabelNumber().add(castToUnsignedInt(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 106437350:  return addParty(); 
        case 110546223:  return getTopic(); 
        case 3575610:  return getType(); 
        case 565719004:  return getDecision(); 
        case 675909535:  return addDecisionMode(); 
        case -1412808770:  return addAnswer(); 
        case 3556653:  return getTextElement();
        case -1102667083:  return addLinkIdElement();
        case -149460995:  return addSecurityLabelNumberElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 106437350: /*party*/ return new String[] {};
        case 110546223: /*topic*/ return new String[] {"Reference"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 565719004: /*decision*/ return new String[] {"CodeableConcept"};
        case 675909535: /*decisionMode*/ return new String[] {"CodeableConcept"};
        case -1412808770: /*answer*/ return new String[] {};
        case 3556653: /*text*/ return new String[] {"string"};
        case -1102667083: /*linkId*/ return new String[] {"string"};
        case -149460995: /*securityLabelNumber*/ return new String[] {"unsignedInt"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("party")) {
          return addParty();
        }
        else if (name.equals("topic")) {
          this.topic = new Reference();
          return this.topic;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("decision")) {
          this.decision = new CodeableConcept();
          return this.decision;
        }
        else if (name.equals("decisionMode")) {
          return addDecisionMode();
        }
        else if (name.equals("answer")) {
          return addAnswer();
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.text");
        }
        else if (name.equals("linkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.linkId");
        }
        else if (name.equals("securityLabelNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.securityLabelNumber");
        }
        else
          return super.addChild(name);
      }

      public ContractOfferComponent copy() {
        ContractOfferComponent dst = new ContractOfferComponent();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        if (party != null) {
          dst.party = new ArrayList<ContractPartyComponent>();
          for (ContractPartyComponent i : party)
            dst.party.add(i.copy());
        };
        dst.topic = topic == null ? null : topic.copy();
        dst.type = type == null ? null : type.copy();
        dst.decision = decision == null ? null : decision.copy();
        if (decisionMode != null) {
          dst.decisionMode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : decisionMode)
            dst.decisionMode.add(i.copy());
        };
        if (answer != null) {
          dst.answer = new ArrayList<AnswerComponent>();
          for (AnswerComponent i : answer)
            dst.answer.add(i.copy());
        };
        dst.text = text == null ? null : text.copy();
        if (linkId != null) {
          dst.linkId = new ArrayList<StringType>();
          for (StringType i : linkId)
            dst.linkId.add(i.copy());
        };
        if (securityLabelNumber != null) {
          dst.securityLabelNumber = new ArrayList<UnsignedIntType>();
          for (UnsignedIntType i : securityLabelNumber)
            dst.securityLabelNumber.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ContractOfferComponent))
          return false;
        ContractOfferComponent o = (ContractOfferComponent) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(party, o.party, true) && compareDeep(topic, o.topic, true)
           && compareDeep(type, o.type, true) && compareDeep(decision, o.decision, true) && compareDeep(decisionMode, o.decisionMode, true)
           && compareDeep(answer, o.answer, true) && compareDeep(text, o.text, true) && compareDeep(linkId, o.linkId, true)
           && compareDeep(securityLabelNumber, o.securityLabelNumber, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ContractOfferComponent))
          return false;
        ContractOfferComponent o = (ContractOfferComponent) other_;
        return compareValues(text, o.text, true) && compareValues(linkId, o.linkId, true) && compareValues(securityLabelNumber, o.securityLabelNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, party, topic
          , type, decision, decisionMode, answer, text, linkId, securityLabelNumber);
      }

  public String fhirType() {
    return "Contract.term.offer";

  }

  }

    @Block()
    public static class ContractPartyComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Participant in the offer.
         */
        @Child(name = "reference", type = {Patient.class, RelatedPerson.class, Practitioner.class, PractitionerRole.class, Device.class, Group.class, Organization.class}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Referenced entity", formalDefinition="Participant in the offer." )
        protected List<Reference> reference;
        /**
         * The actual objects that are the target of the reference (Participant in the offer.)
         */
        protected List<Resource> referenceTarget;


        /**
         * How the party participates in the offer.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Participant engagement type", formalDefinition="How the party participates in the offer." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-party-role")
        protected CodeableConcept role;

        private static final long serialVersionUID = 128949255L;

    /**
     * Constructor
     */
      public ContractPartyComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ContractPartyComponent(CodeableConcept role) {
        super();
        this.role = role;
      }

        /**
         * @return {@link #reference} (Participant in the offer.)
         */
        public List<Reference> getReference() { 
          if (this.reference == null)
            this.reference = new ArrayList<Reference>();
          return this.reference;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractPartyComponent setReference(List<Reference> theReference) { 
          this.reference = theReference;
          return this;
        }

        public boolean hasReference() { 
          if (this.reference == null)
            return false;
          for (Reference item : this.reference)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addReference() { //3
          Reference t = new Reference();
          if (this.reference == null)
            this.reference = new ArrayList<Reference>();
          this.reference.add(t);
          return t;
        }

        public ContractPartyComponent addReference(Reference t) { //3
          if (t == null)
            return this;
          if (this.reference == null)
            this.reference = new ArrayList<Reference>();
          this.reference.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #reference}, creating it if it does not already exist
         */
        public Reference getReferenceFirstRep() { 
          if (getReference().isEmpty()) {
            addReference();
          }
          return getReference().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getReferenceTarget() { 
          if (this.referenceTarget == null)
            this.referenceTarget = new ArrayList<Resource>();
          return this.referenceTarget;
        }

        /**
         * @return {@link #role} (How the party participates in the offer.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractPartyComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (How the party participates in the offer.)
         */
        public ContractPartyComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("reference", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole|Device|Group|Organization)", "Participant in the offer.", 0, java.lang.Integer.MAX_VALUE, reference));
          children.add(new Property("role", "CodeableConcept", "How the party participates in the offer.", 0, 1, role));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -925155509: /*reference*/  return new Property("reference", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole|Device|Group|Organization)", "Participant in the offer.", 0, java.lang.Integer.MAX_VALUE, reference);
          case 3506294: /*role*/  return new Property("role", "CodeableConcept", "How the party participates in the offer.", 0, 1, role);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : this.reference.toArray(new Base[this.reference.size()]); // Reference
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -925155509: // reference
          this.getReference().add(castToReference(value)); // Reference
          return value;
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference")) {
          this.getReference().add(castToReference(value));
        } else if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509:  return addReference(); 
        case 3506294:  return getRole(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return new String[] {"Reference"};
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          return addReference();
        }
        else if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else
          return super.addChild(name);
      }

      public ContractPartyComponent copy() {
        ContractPartyComponent dst = new ContractPartyComponent();
        copyValues(dst);
        if (reference != null) {
          dst.reference = new ArrayList<Reference>();
          for (Reference i : reference)
            dst.reference.add(i.copy());
        };
        dst.role = role == null ? null : role.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ContractPartyComponent))
          return false;
        ContractPartyComponent o = (ContractPartyComponent) other_;
        return compareDeep(reference, o.reference, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ContractPartyComponent))
          return false;
        ContractPartyComponent o = (ContractPartyComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(reference, role);
      }

  public String fhirType() {
    return "Contract.term.offer.party";

  }

  }

    @Block()
    public static class AnswerComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.
         */
        @Child(name = "value", type = {BooleanType.class, DecimalType.class, IntegerType.class, DateType.class, DateTimeType.class, TimeType.class, StringType.class, UriType.class, Attachment.class, Coding.class, Quantity.class, Reference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="The actual answer response", formalDefinition="Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research." )
        protected Type value;

        private static final long serialVersionUID = -732981989L;

    /**
     * Constructor
     */
      public AnswerComponent() {
        super();
      }

    /**
     * Constructor
     */
      public AnswerComponent(Type value) {
        super();
        this.value = value;
      }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public Type getValue() { 
          return this.value;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public BooleanType getValueBooleanType() throws FHIRException { 
          if (this.value == null)
            this.value = new BooleanType();
          if (!(this.value instanceof BooleanType))
            throw new FHIRException("Type mismatch: the type BooleanType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (BooleanType) this.value;
        }

        public boolean hasValueBooleanType() { 
          return this != null && this.value instanceof BooleanType;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public DecimalType getValueDecimalType() throws FHIRException { 
          if (this.value == null)
            this.value = new DecimalType();
          if (!(this.value instanceof DecimalType))
            throw new FHIRException("Type mismatch: the type DecimalType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DecimalType) this.value;
        }

        public boolean hasValueDecimalType() { 
          return this != null && this.value instanceof DecimalType;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public IntegerType getValueIntegerType() throws FHIRException { 
          if (this.value == null)
            this.value = new IntegerType();
          if (!(this.value instanceof IntegerType))
            throw new FHIRException("Type mismatch: the type IntegerType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (IntegerType) this.value;
        }

        public boolean hasValueIntegerType() { 
          return this != null && this.value instanceof IntegerType;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public DateType getValueDateType() throws FHIRException { 
          if (this.value == null)
            this.value = new DateType();
          if (!(this.value instanceof DateType))
            throw new FHIRException("Type mismatch: the type DateType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateType) this.value;
        }

        public boolean hasValueDateType() { 
          return this != null && this.value instanceof DateType;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public DateTimeType getValueDateTimeType() throws FHIRException { 
          if (this.value == null)
            this.value = new DateTimeType();
          if (!(this.value instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (DateTimeType) this.value;
        }

        public boolean hasValueDateTimeType() { 
          return this != null && this.value instanceof DateTimeType;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public TimeType getValueTimeType() throws FHIRException { 
          if (this.value == null)
            this.value = new TimeType();
          if (!(this.value instanceof TimeType))
            throw new FHIRException("Type mismatch: the type TimeType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (TimeType) this.value;
        }

        public boolean hasValueTimeType() { 
          return this != null && this.value instanceof TimeType;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public StringType getValueStringType() throws FHIRException { 
          if (this.value == null)
            this.value = new StringType();
          if (!(this.value instanceof StringType))
            throw new FHIRException("Type mismatch: the type StringType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (StringType) this.value;
        }

        public boolean hasValueStringType() { 
          return this != null && this.value instanceof StringType;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public UriType getValueUriType() throws FHIRException { 
          if (this.value == null)
            this.value = new UriType();
          if (!(this.value instanceof UriType))
            throw new FHIRException("Type mismatch: the type UriType was expected, but "+this.value.getClass().getName()+" was encountered");
          return (UriType) this.value;
        }

        public boolean hasValueUriType() { 
          return this != null && this.value instanceof UriType;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public Attachment getValueAttachment() throws FHIRException { 
          if (this.value == null)
            this.value = new Attachment();
          if (!(this.value instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Attachment) this.value;
        }

        public boolean hasValueAttachment() { 
          return this != null && this.value instanceof Attachment;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public Coding getValueCoding() throws FHIRException { 
          if (this.value == null)
            this.value = new Coding();
          if (!(this.value instanceof Coding))
            throw new FHIRException("Type mismatch: the type Coding was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Coding) this.value;
        }

        public boolean hasValueCoding() { 
          return this != null && this.value instanceof Coding;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public Quantity getValueQuantity() throws FHIRException { 
          if (this.value == null)
            this.value = new Quantity();
          if (!(this.value instanceof Quantity))
            throw new FHIRException("Type mismatch: the type Quantity was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Quantity) this.value;
        }

        public boolean hasValueQuantity() { 
          return this != null && this.value instanceof Quantity;
        }

        /**
         * @return {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public Reference getValueReference() throws FHIRException { 
          if (this.value == null)
            this.value = new Reference();
          if (!(this.value instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.value.getClass().getName()+" was encountered");
          return (Reference) this.value;
        }

        public boolean hasValueReference() { 
          return this != null && this.value instanceof Reference;
        }

        public boolean hasValue() { 
          return this.value != null && !this.value.isEmpty();
        }

        /**
         * @param value {@link #value} (Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.)
         */
        public AnswerComponent setValue(Type value) { 
          if (value != null && !(value instanceof BooleanType || value instanceof DecimalType || value instanceof IntegerType || value instanceof DateType || value instanceof DateTimeType || value instanceof TimeType || value instanceof StringType || value instanceof UriType || value instanceof Attachment || value instanceof Coding || value instanceof Quantity || value instanceof Reference))
            throw new Error("Not the right type for Contract.term.offer.answer.value[x]: "+value.fhirType());
          this.value = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1410166417: /*value[x]*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case 111972721: /*value*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case 733421943: /*valueBoolean*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case -2083993440: /*valueDecimal*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case -1668204915: /*valueInteger*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case -766192449: /*valueDate*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case 1047929900: /*valueDateTime*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case -765708322: /*valueTime*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case -1424603934: /*valueString*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case -1410172357: /*valueUri*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case -475566732: /*valueAttachment*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case -1887705029: /*valueCoding*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case -2029823716: /*valueQuantity*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          case 1755241690: /*valueReference*/  return new Property("value[x]", "boolean|decimal|integer|date|dateTime|time|string|uri|Attachment|Coding|Quantity|Reference(Any)", "Response to an offer clause or question text,  which enables selection of values to be agreed to, e.g., the period of participation, the date of occupancy of a rental, warrently duration, or whether biospecimen may be used for further research.", 0, 1, value);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return this.value == null ? new Base[0] : new Base[] {this.value}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 111972721: // value
          this.value = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("value[x]")) {
          this.value = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1410166417:  return getValue(); 
        case 111972721:  return getValue(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 111972721: /*value*/ return new String[] {"boolean", "decimal", "integer", "date", "dateTime", "time", "string", "uri", "Attachment", "Coding", "Quantity", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("valueBoolean")) {
          this.value = new BooleanType();
          return this.value;
        }
        else if (name.equals("valueDecimal")) {
          this.value = new DecimalType();
          return this.value;
        }
        else if (name.equals("valueInteger")) {
          this.value = new IntegerType();
          return this.value;
        }
        else if (name.equals("valueDate")) {
          this.value = new DateType();
          return this.value;
        }
        else if (name.equals("valueDateTime")) {
          this.value = new DateTimeType();
          return this.value;
        }
        else if (name.equals("valueTime")) {
          this.value = new TimeType();
          return this.value;
        }
        else if (name.equals("valueString")) {
          this.value = new StringType();
          return this.value;
        }
        else if (name.equals("valueUri")) {
          this.value = new UriType();
          return this.value;
        }
        else if (name.equals("valueAttachment")) {
          this.value = new Attachment();
          return this.value;
        }
        else if (name.equals("valueCoding")) {
          this.value = new Coding();
          return this.value;
        }
        else if (name.equals("valueQuantity")) {
          this.value = new Quantity();
          return this.value;
        }
        else if (name.equals("valueReference")) {
          this.value = new Reference();
          return this.value;
        }
        else
          return super.addChild(name);
      }

      public AnswerComponent copy() {
        AnswerComponent dst = new AnswerComponent();
        copyValues(dst);
        dst.value = value == null ? null : value.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AnswerComponent))
          return false;
        AnswerComponent o = (AnswerComponent) other_;
        return compareDeep(value, o.value, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AnswerComponent))
          return false;
        AnswerComponent o = (AnswerComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(value);
      }

  public String fhirType() {
    return "Contract.term.offer.answer";

  }

  }

    @Block()
    public static class ContractAssetComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Differentiates the kind of the asset .
         */
        @Child(name = "scope", type = {CodeableConcept.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Range of asset", formalDefinition="Differentiates the kind of the asset ." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-assetscope")
        protected CodeableConcept scope;

        /**
         * Target entity type about which the term may be concerned.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Asset category", formalDefinition="Target entity type about which the term may be concerned." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-assettype")
        protected List<CodeableConcept> type;

        /**
         * Associated entities.
         */
        @Child(name = "typeReference", type = {Reference.class}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Associated entities", formalDefinition="Associated entities." )
        protected List<Reference> typeReference;
        /**
         * The actual objects that are the target of the reference (Associated entities.)
         */
        protected List<Resource> typeReferenceTarget;


        /**
         * May be a subtype or part of an offered asset.
         */
        @Child(name = "subtype", type = {CodeableConcept.class}, order=4, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Asset sub-category", formalDefinition="May be a subtype or part of an offered asset." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-assetsubtype")
        protected List<CodeableConcept> subtype;

        /**
         * Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that refer to it, and/or are owned by the offeree.
         */
        @Child(name = "relationship", type = {Coding.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Kinship of the asset", formalDefinition="Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that refer to it, and/or are owned by the offeree." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/consent-content-class")
        protected Coding relationship;

        /**
         * Circumstance of the asset.
         */
        @Child(name = "context", type = {}, order=6, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Circumstance of the asset", formalDefinition="Circumstance of the asset." )
        protected List<AssetContextComponent> context;

        /**
         * Description of the quality and completeness of the asset that imay be a factor in its valuation.
         */
        @Child(name = "condition", type = {StringType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Quality desctiption of asset", formalDefinition="Description of the quality and completeness of the asset that imay be a factor in its valuation." )
        protected StringType condition;

        /**
         * Type of Asset availability for use or ownership.
         */
        @Child(name = "periodType", type = {CodeableConcept.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Asset availability types", formalDefinition="Type of Asset availability for use or ownership." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/asset-availability")
        protected List<CodeableConcept> periodType;

        /**
         * Asset relevant contractual time period.
         */
        @Child(name = "period", type = {Period.class}, order=9, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Time period of the asset", formalDefinition="Asset relevant contractual time period." )
        protected List<Period> period;

        /**
         * Time period of asset use.
         */
        @Child(name = "usePeriod", type = {Period.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Time period", formalDefinition="Time period of asset use." )
        protected List<Period> usePeriod;

        /**
         * Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in the formation of the contract.
         */
        @Child(name = "text", type = {StringType.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Asset clause or question text", formalDefinition="Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in the formation of the contract." )
        protected StringType text;

        /**
         * Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.
         */
        @Child(name = "linkId", type = {StringType.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to asset text", formalDefinition="Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse." )
        protected List<StringType> linkId;

        /**
         * Response to assets.
         */
        @Child(name = "answer", type = {AnswerComponent.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Response to assets", formalDefinition="Response to assets." )
        protected List<AnswerComponent> answer;

        /**
         * Security labels that protects the asset.
         */
        @Child(name = "securityLabelNumber", type = {UnsignedIntType.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Asset restriction numbers", formalDefinition="Security labels that protects the asset." )
        protected List<UnsignedIntType> securityLabelNumber;

        /**
         * Contract Valued Item List.
         */
        @Child(name = "valuedItem", type = {}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item List", formalDefinition="Contract Valued Item List." )
        protected List<ValuedItemComponent> valuedItem;

        private static final long serialVersionUID = -1080398792L;

    /**
     * Constructor
     */
      public ContractAssetComponent() {
        super();
      }

        /**
         * @return {@link #scope} (Differentiates the kind of the asset .)
         */
        public CodeableConcept getScope() { 
          if (this.scope == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractAssetComponent.scope");
            else if (Configuration.doAutoCreate())
              this.scope = new CodeableConcept(); // cc
          return this.scope;
        }

        public boolean hasScope() { 
          return this.scope != null && !this.scope.isEmpty();
        }

        /**
         * @param value {@link #scope} (Differentiates the kind of the asset .)
         */
        public ContractAssetComponent setScope(CodeableConcept value) { 
          this.scope = value;
          return this;
        }

        /**
         * @return {@link #type} (Target entity type about which the term may be concerned.)
         */
        public List<CodeableConcept> getType() { 
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          return this.type;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setType(List<CodeableConcept> theType) { 
          this.type = theType;
          return this;
        }

        public boolean hasType() { 
          if (this.type == null)
            return false;
          for (CodeableConcept item : this.type)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return t;
        }

        public ContractAssetComponent addType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.type == null)
            this.type = new ArrayList<CodeableConcept>();
          this.type.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #type}, creating it if it does not already exist
         */
        public CodeableConcept getTypeFirstRep() { 
          if (getType().isEmpty()) {
            addType();
          }
          return getType().get(0);
        }

        /**
         * @return {@link #typeReference} (Associated entities.)
         */
        public List<Reference> getTypeReference() { 
          if (this.typeReference == null)
            this.typeReference = new ArrayList<Reference>();
          return this.typeReference;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setTypeReference(List<Reference> theTypeReference) { 
          this.typeReference = theTypeReference;
          return this;
        }

        public boolean hasTypeReference() { 
          if (this.typeReference == null)
            return false;
          for (Reference item : this.typeReference)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addTypeReference() { //3
          Reference t = new Reference();
          if (this.typeReference == null)
            this.typeReference = new ArrayList<Reference>();
          this.typeReference.add(t);
          return t;
        }

        public ContractAssetComponent addTypeReference(Reference t) { //3
          if (t == null)
            return this;
          if (this.typeReference == null)
            this.typeReference = new ArrayList<Reference>();
          this.typeReference.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #typeReference}, creating it if it does not already exist
         */
        public Reference getTypeReferenceFirstRep() { 
          if (getTypeReference().isEmpty()) {
            addTypeReference();
          }
          return getTypeReference().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getTypeReferenceTarget() { 
          if (this.typeReferenceTarget == null)
            this.typeReferenceTarget = new ArrayList<Resource>();
          return this.typeReferenceTarget;
        }

        /**
         * @return {@link #subtype} (May be a subtype or part of an offered asset.)
         */
        public List<CodeableConcept> getSubtype() { 
          if (this.subtype == null)
            this.subtype = new ArrayList<CodeableConcept>();
          return this.subtype;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setSubtype(List<CodeableConcept> theSubtype) { 
          this.subtype = theSubtype;
          return this;
        }

        public boolean hasSubtype() { 
          if (this.subtype == null)
            return false;
          for (CodeableConcept item : this.subtype)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addSubtype() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.subtype == null)
            this.subtype = new ArrayList<CodeableConcept>();
          this.subtype.add(t);
          return t;
        }

        public ContractAssetComponent addSubtype(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.subtype == null)
            this.subtype = new ArrayList<CodeableConcept>();
          this.subtype.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #subtype}, creating it if it does not already exist
         */
        public CodeableConcept getSubtypeFirstRep() { 
          if (getSubtype().isEmpty()) {
            addSubtype();
          }
          return getSubtype().get(0);
        }

        /**
         * @return {@link #relationship} (Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that refer to it, and/or are owned by the offeree.)
         */
        public Coding getRelationship() { 
          if (this.relationship == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractAssetComponent.relationship");
            else if (Configuration.doAutoCreate())
              this.relationship = new Coding(); // cc
          return this.relationship;
        }

        public boolean hasRelationship() { 
          return this.relationship != null && !this.relationship.isEmpty();
        }

        /**
         * @param value {@link #relationship} (Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that refer to it, and/or are owned by the offeree.)
         */
        public ContractAssetComponent setRelationship(Coding value) { 
          this.relationship = value;
          return this;
        }

        /**
         * @return {@link #context} (Circumstance of the asset.)
         */
        public List<AssetContextComponent> getContext() { 
          if (this.context == null)
            this.context = new ArrayList<AssetContextComponent>();
          return this.context;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setContext(List<AssetContextComponent> theContext) { 
          this.context = theContext;
          return this;
        }

        public boolean hasContext() { 
          if (this.context == null)
            return false;
          for (AssetContextComponent item : this.context)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AssetContextComponent addContext() { //3
          AssetContextComponent t = new AssetContextComponent();
          if (this.context == null)
            this.context = new ArrayList<AssetContextComponent>();
          this.context.add(t);
          return t;
        }

        public ContractAssetComponent addContext(AssetContextComponent t) { //3
          if (t == null)
            return this;
          if (this.context == null)
            this.context = new ArrayList<AssetContextComponent>();
          this.context.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #context}, creating it if it does not already exist
         */
        public AssetContextComponent getContextFirstRep() { 
          if (getContext().isEmpty()) {
            addContext();
          }
          return getContext().get(0);
        }

        /**
         * @return {@link #condition} (Description of the quality and completeness of the asset that imay be a factor in its valuation.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
         */
        public StringType getConditionElement() { 
          if (this.condition == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractAssetComponent.condition");
            else if (Configuration.doAutoCreate())
              this.condition = new StringType(); // bb
          return this.condition;
        }

        public boolean hasConditionElement() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        public boolean hasCondition() { 
          return this.condition != null && !this.condition.isEmpty();
        }

        /**
         * @param value {@link #condition} (Description of the quality and completeness of the asset that imay be a factor in its valuation.). This is the underlying object with id, value and extensions. The accessor "getCondition" gives direct access to the value
         */
        public ContractAssetComponent setConditionElement(StringType value) { 
          this.condition = value;
          return this;
        }

        /**
         * @return Description of the quality and completeness of the asset that imay be a factor in its valuation.
         */
        public String getCondition() { 
          return this.condition == null ? null : this.condition.getValue();
        }

        /**
         * @param value Description of the quality and completeness of the asset that imay be a factor in its valuation.
         */
        public ContractAssetComponent setCondition(String value) { 
          if (Utilities.noString(value))
            this.condition = null;
          else {
            if (this.condition == null)
              this.condition = new StringType();
            this.condition.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #periodType} (Type of Asset availability for use or ownership.)
         */
        public List<CodeableConcept> getPeriodType() { 
          if (this.periodType == null)
            this.periodType = new ArrayList<CodeableConcept>();
          return this.periodType;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setPeriodType(List<CodeableConcept> thePeriodType) { 
          this.periodType = thePeriodType;
          return this;
        }

        public boolean hasPeriodType() { 
          if (this.periodType == null)
            return false;
          for (CodeableConcept item : this.periodType)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addPeriodType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.periodType == null)
            this.periodType = new ArrayList<CodeableConcept>();
          this.periodType.add(t);
          return t;
        }

        public ContractAssetComponent addPeriodType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.periodType == null)
            this.periodType = new ArrayList<CodeableConcept>();
          this.periodType.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #periodType}, creating it if it does not already exist
         */
        public CodeableConcept getPeriodTypeFirstRep() { 
          if (getPeriodType().isEmpty()) {
            addPeriodType();
          }
          return getPeriodType().get(0);
        }

        /**
         * @return {@link #period} (Asset relevant contractual time period.)
         */
        public List<Period> getPeriod() { 
          if (this.period == null)
            this.period = new ArrayList<Period>();
          return this.period;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setPeriod(List<Period> thePeriod) { 
          this.period = thePeriod;
          return this;
        }

        public boolean hasPeriod() { 
          if (this.period == null)
            return false;
          for (Period item : this.period)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Period addPeriod() { //3
          Period t = new Period();
          if (this.period == null)
            this.period = new ArrayList<Period>();
          this.period.add(t);
          return t;
        }

        public ContractAssetComponent addPeriod(Period t) { //3
          if (t == null)
            return this;
          if (this.period == null)
            this.period = new ArrayList<Period>();
          this.period.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #period}, creating it if it does not already exist
         */
        public Period getPeriodFirstRep() { 
          if (getPeriod().isEmpty()) {
            addPeriod();
          }
          return getPeriod().get(0);
        }

        /**
         * @return {@link #usePeriod} (Time period of asset use.)
         */
        public List<Period> getUsePeriod() { 
          if (this.usePeriod == null)
            this.usePeriod = new ArrayList<Period>();
          return this.usePeriod;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setUsePeriod(List<Period> theUsePeriod) { 
          this.usePeriod = theUsePeriod;
          return this;
        }

        public boolean hasUsePeriod() { 
          if (this.usePeriod == null)
            return false;
          for (Period item : this.usePeriod)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Period addUsePeriod() { //3
          Period t = new Period();
          if (this.usePeriod == null)
            this.usePeriod = new ArrayList<Period>();
          this.usePeriod.add(t);
          return t;
        }

        public ContractAssetComponent addUsePeriod(Period t) { //3
          if (t == null)
            return this;
          if (this.usePeriod == null)
            this.usePeriod = new ArrayList<Period>();
          this.usePeriod.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #usePeriod}, creating it if it does not already exist
         */
        public Period getUsePeriodFirstRep() { 
          if (getUsePeriod().isEmpty()) {
            addUsePeriod();
          }
          return getUsePeriod().get(0);
        }

        /**
         * @return {@link #text} (Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in the formation of the contract.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ContractAssetComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in the formation of the contract.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public ContractAssetComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in the formation of the contract.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in the formation of the contract.
         */
        public ContractAssetComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #linkId} (Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.)
         */
        public List<StringType> getLinkId() { 
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          return this.linkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setLinkId(List<StringType> theLinkId) { 
          this.linkId = theLinkId;
          return this;
        }

        public boolean hasLinkId() { 
          if (this.linkId == null)
            return false;
          for (StringType item : this.linkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #linkId} (Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.)
         */
        public StringType addLinkIdElement() {//2 
          StringType t = new StringType();
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          this.linkId.add(t);
          return t;
        }

        /**
         * @param value {@link #linkId} (Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.)
         */
        public ContractAssetComponent addLinkId(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          this.linkId.add(t);
          return this;
        }

        /**
         * @param value {@link #linkId} (Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.)
         */
        public boolean hasLinkId(String value) { 
          if (this.linkId == null)
            return false;
          for (StringType v : this.linkId)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #answer} (Response to assets.)
         */
        public List<AnswerComponent> getAnswer() { 
          if (this.answer == null)
            this.answer = new ArrayList<AnswerComponent>();
          return this.answer;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setAnswer(List<AnswerComponent> theAnswer) { 
          this.answer = theAnswer;
          return this;
        }

        public boolean hasAnswer() { 
          if (this.answer == null)
            return false;
          for (AnswerComponent item : this.answer)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public AnswerComponent addAnswer() { //3
          AnswerComponent t = new AnswerComponent();
          if (this.answer == null)
            this.answer = new ArrayList<AnswerComponent>();
          this.answer.add(t);
          return t;
        }

        public ContractAssetComponent addAnswer(AnswerComponent t) { //3
          if (t == null)
            return this;
          if (this.answer == null)
            this.answer = new ArrayList<AnswerComponent>();
          this.answer.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #answer}, creating it if it does not already exist
         */
        public AnswerComponent getAnswerFirstRep() { 
          if (getAnswer().isEmpty()) {
            addAnswer();
          }
          return getAnswer().get(0);
        }

        /**
         * @return {@link #securityLabelNumber} (Security labels that protects the asset.)
         */
        public List<UnsignedIntType> getSecurityLabelNumber() { 
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          return this.securityLabelNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setSecurityLabelNumber(List<UnsignedIntType> theSecurityLabelNumber) { 
          this.securityLabelNumber = theSecurityLabelNumber;
          return this;
        }

        public boolean hasSecurityLabelNumber() { 
          if (this.securityLabelNumber == null)
            return false;
          for (UnsignedIntType item : this.securityLabelNumber)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #securityLabelNumber} (Security labels that protects the asset.)
         */
        public UnsignedIntType addSecurityLabelNumberElement() {//2 
          UnsignedIntType t = new UnsignedIntType();
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          this.securityLabelNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #securityLabelNumber} (Security labels that protects the asset.)
         */
        public ContractAssetComponent addSecurityLabelNumber(int value) { //1
          UnsignedIntType t = new UnsignedIntType();
          t.setValue(value);
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          this.securityLabelNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #securityLabelNumber} (Security labels that protects the asset.)
         */
        public boolean hasSecurityLabelNumber(int value) { 
          if (this.securityLabelNumber == null)
            return false;
          for (UnsignedIntType v : this.securityLabelNumber)
            if (v.getValue().equals(value)) // unsignedInt
              return true;
          return false;
        }

        /**
         * @return {@link #valuedItem} (Contract Valued Item List.)
         */
        public List<ValuedItemComponent> getValuedItem() { 
          if (this.valuedItem == null)
            this.valuedItem = new ArrayList<ValuedItemComponent>();
          return this.valuedItem;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ContractAssetComponent setValuedItem(List<ValuedItemComponent> theValuedItem) { 
          this.valuedItem = theValuedItem;
          return this;
        }

        public boolean hasValuedItem() { 
          if (this.valuedItem == null)
            return false;
          for (ValuedItemComponent item : this.valuedItem)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ValuedItemComponent addValuedItem() { //3
          ValuedItemComponent t = new ValuedItemComponent();
          if (this.valuedItem == null)
            this.valuedItem = new ArrayList<ValuedItemComponent>();
          this.valuedItem.add(t);
          return t;
        }

        public ContractAssetComponent addValuedItem(ValuedItemComponent t) { //3
          if (t == null)
            return this;
          if (this.valuedItem == null)
            this.valuedItem = new ArrayList<ValuedItemComponent>();
          this.valuedItem.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #valuedItem}, creating it if it does not already exist
         */
        public ValuedItemComponent getValuedItemFirstRep() { 
          if (getValuedItem().isEmpty()) {
            addValuedItem();
          }
          return getValuedItem().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("scope", "CodeableConcept", "Differentiates the kind of the asset .", 0, 1, scope));
          children.add(new Property("type", "CodeableConcept", "Target entity type about which the term may be concerned.", 0, java.lang.Integer.MAX_VALUE, type));
          children.add(new Property("typeReference", "Reference(Any)", "Associated entities.", 0, java.lang.Integer.MAX_VALUE, typeReference));
          children.add(new Property("subtype", "CodeableConcept", "May be a subtype or part of an offered asset.", 0, java.lang.Integer.MAX_VALUE, subtype));
          children.add(new Property("relationship", "Coding", "Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that refer to it, and/or are owned by the offeree.", 0, 1, relationship));
          children.add(new Property("context", "", "Circumstance of the asset.", 0, java.lang.Integer.MAX_VALUE, context));
          children.add(new Property("condition", "string", "Description of the quality and completeness of the asset that imay be a factor in its valuation.", 0, 1, condition));
          children.add(new Property("periodType", "CodeableConcept", "Type of Asset availability for use or ownership.", 0, java.lang.Integer.MAX_VALUE, periodType));
          children.add(new Property("period", "Period", "Asset relevant contractual time period.", 0, java.lang.Integer.MAX_VALUE, period));
          children.add(new Property("usePeriod", "Period", "Time period of asset use.", 0, java.lang.Integer.MAX_VALUE, usePeriod));
          children.add(new Property("text", "string", "Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in the formation of the contract.", 0, 1, text));
          children.add(new Property("linkId", "string", "Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, linkId));
          children.add(new Property("answer", "@Contract.term.offer.answer", "Response to assets.", 0, java.lang.Integer.MAX_VALUE, answer));
          children.add(new Property("securityLabelNumber", "unsignedInt", "Security labels that protects the asset.", 0, java.lang.Integer.MAX_VALUE, securityLabelNumber));
          children.add(new Property("valuedItem", "", "Contract Valued Item List.", 0, java.lang.Integer.MAX_VALUE, valuedItem));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 109264468: /*scope*/  return new Property("scope", "CodeableConcept", "Differentiates the kind of the asset .", 0, 1, scope);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Target entity type about which the term may be concerned.", 0, java.lang.Integer.MAX_VALUE, type);
          case 2074825009: /*typeReference*/  return new Property("typeReference", "Reference(Any)", "Associated entities.", 0, java.lang.Integer.MAX_VALUE, typeReference);
          case -1867567750: /*subtype*/  return new Property("subtype", "CodeableConcept", "May be a subtype or part of an offered asset.", 0, java.lang.Integer.MAX_VALUE, subtype);
          case -261851592: /*relationship*/  return new Property("relationship", "Coding", "Specifies the applicability of the term to an asset resource instance, and instances it refers to orinstances that refer to it, and/or are owned by the offeree.", 0, 1, relationship);
          case 951530927: /*context*/  return new Property("context", "", "Circumstance of the asset.", 0, java.lang.Integer.MAX_VALUE, context);
          case -861311717: /*condition*/  return new Property("condition", "string", "Description of the quality and completeness of the asset that imay be a factor in its valuation.", 0, 1, condition);
          case 384348315: /*periodType*/  return new Property("periodType", "CodeableConcept", "Type of Asset availability for use or ownership.", 0, java.lang.Integer.MAX_VALUE, periodType);
          case -991726143: /*period*/  return new Property("period", "Period", "Asset relevant contractual time period.", 0, java.lang.Integer.MAX_VALUE, period);
          case -628382168: /*usePeriod*/  return new Property("usePeriod", "Period", "Time period of asset use.", 0, java.lang.Integer.MAX_VALUE, usePeriod);
          case 3556653: /*text*/  return new Property("text", "string", "Clause or question text (Prose Object) concerning the asset in a linked form, such as a QuestionnaireResponse used in the formation of the contract.", 0, 1, text);
          case -1102667083: /*linkId*/  return new Property("linkId", "string", "Id [identifier??] of the clause or question text about the asset in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, linkId);
          case -1412808770: /*answer*/  return new Property("answer", "@Contract.term.offer.answer", "Response to assets.", 0, java.lang.Integer.MAX_VALUE, answer);
          case -149460995: /*securityLabelNumber*/  return new Property("securityLabelNumber", "unsignedInt", "Security labels that protects the asset.", 0, java.lang.Integer.MAX_VALUE, securityLabelNumber);
          case 2046675654: /*valuedItem*/  return new Property("valuedItem", "", "Contract Valued Item List.", 0, java.lang.Integer.MAX_VALUE, valuedItem);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 109264468: /*scope*/ return this.scope == null ? new Base[0] : new Base[] {this.scope}; // CodeableConcept
        case 3575610: /*type*/ return this.type == null ? new Base[0] : this.type.toArray(new Base[this.type.size()]); // CodeableConcept
        case 2074825009: /*typeReference*/ return this.typeReference == null ? new Base[0] : this.typeReference.toArray(new Base[this.typeReference.size()]); // Reference
        case -1867567750: /*subtype*/ return this.subtype == null ? new Base[0] : this.subtype.toArray(new Base[this.subtype.size()]); // CodeableConcept
        case -261851592: /*relationship*/ return this.relationship == null ? new Base[0] : new Base[] {this.relationship}; // Coding
        case 951530927: /*context*/ return this.context == null ? new Base[0] : this.context.toArray(new Base[this.context.size()]); // AssetContextComponent
        case -861311717: /*condition*/ return this.condition == null ? new Base[0] : new Base[] {this.condition}; // StringType
        case 384348315: /*periodType*/ return this.periodType == null ? new Base[0] : this.periodType.toArray(new Base[this.periodType.size()]); // CodeableConcept
        case -991726143: /*period*/ return this.period == null ? new Base[0] : this.period.toArray(new Base[this.period.size()]); // Period
        case -628382168: /*usePeriod*/ return this.usePeriod == null ? new Base[0] : this.usePeriod.toArray(new Base[this.usePeriod.size()]); // Period
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        case -1102667083: /*linkId*/ return this.linkId == null ? new Base[0] : this.linkId.toArray(new Base[this.linkId.size()]); // StringType
        case -1412808770: /*answer*/ return this.answer == null ? new Base[0] : this.answer.toArray(new Base[this.answer.size()]); // AnswerComponent
        case -149460995: /*securityLabelNumber*/ return this.securityLabelNumber == null ? new Base[0] : this.securityLabelNumber.toArray(new Base[this.securityLabelNumber.size()]); // UnsignedIntType
        case 2046675654: /*valuedItem*/ return this.valuedItem == null ? new Base[0] : this.valuedItem.toArray(new Base[this.valuedItem.size()]); // ValuedItemComponent
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 109264468: // scope
          this.scope = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 3575610: // type
          this.getType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 2074825009: // typeReference
          this.getTypeReference().add(castToReference(value)); // Reference
          return value;
        case -1867567750: // subtype
          this.getSubtype().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -261851592: // relationship
          this.relationship = castToCoding(value); // Coding
          return value;
        case 951530927: // context
          this.getContext().add((AssetContextComponent) value); // AssetContextComponent
          return value;
        case -861311717: // condition
          this.condition = castToString(value); // StringType
          return value;
        case 384348315: // periodType
          this.getPeriodType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -991726143: // period
          this.getPeriod().add(castToPeriod(value)); // Period
          return value;
        case -628382168: // usePeriod
          this.getUsePeriod().add(castToPeriod(value)); // Period
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        case -1102667083: // linkId
          this.getLinkId().add(castToString(value)); // StringType
          return value;
        case -1412808770: // answer
          this.getAnswer().add((AnswerComponent) value); // AnswerComponent
          return value;
        case -149460995: // securityLabelNumber
          this.getSecurityLabelNumber().add(castToUnsignedInt(value)); // UnsignedIntType
          return value;
        case 2046675654: // valuedItem
          this.getValuedItem().add((ValuedItemComponent) value); // ValuedItemComponent
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("scope")) {
          this.scope = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("type")) {
          this.getType().add(castToCodeableConcept(value));
        } else if (name.equals("typeReference")) {
          this.getTypeReference().add(castToReference(value));
        } else if (name.equals("subtype")) {
          this.getSubtype().add(castToCodeableConcept(value));
        } else if (name.equals("relationship")) {
          this.relationship = castToCoding(value); // Coding
        } else if (name.equals("context")) {
          this.getContext().add((AssetContextComponent) value);
        } else if (name.equals("condition")) {
          this.condition = castToString(value); // StringType
        } else if (name.equals("periodType")) {
          this.getPeriodType().add(castToCodeableConcept(value));
        } else if (name.equals("period")) {
          this.getPeriod().add(castToPeriod(value));
        } else if (name.equals("usePeriod")) {
          this.getUsePeriod().add(castToPeriod(value));
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else if (name.equals("linkId")) {
          this.getLinkId().add(castToString(value));
        } else if (name.equals("answer")) {
          this.getAnswer().add((AnswerComponent) value);
        } else if (name.equals("securityLabelNumber")) {
          this.getSecurityLabelNumber().add(castToUnsignedInt(value));
        } else if (name.equals("valuedItem")) {
          this.getValuedItem().add((ValuedItemComponent) value);
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109264468:  return getScope(); 
        case 3575610:  return addType(); 
        case 2074825009:  return addTypeReference(); 
        case -1867567750:  return addSubtype(); 
        case -261851592:  return getRelationship(); 
        case 951530927:  return addContext(); 
        case -861311717:  return getConditionElement();
        case 384348315:  return addPeriodType(); 
        case -991726143:  return addPeriod(); 
        case -628382168:  return addUsePeriod(); 
        case 3556653:  return getTextElement();
        case -1102667083:  return addLinkIdElement();
        case -1412808770:  return addAnswer(); 
        case -149460995:  return addSecurityLabelNumberElement();
        case 2046675654:  return addValuedItem(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 109264468: /*scope*/ return new String[] {"CodeableConcept"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case 2074825009: /*typeReference*/ return new String[] {"Reference"};
        case -1867567750: /*subtype*/ return new String[] {"CodeableConcept"};
        case -261851592: /*relationship*/ return new String[] {"Coding"};
        case 951530927: /*context*/ return new String[] {};
        case -861311717: /*condition*/ return new String[] {"string"};
        case 384348315: /*periodType*/ return new String[] {"CodeableConcept"};
        case -991726143: /*period*/ return new String[] {"Period"};
        case -628382168: /*usePeriod*/ return new String[] {"Period"};
        case 3556653: /*text*/ return new String[] {"string"};
        case -1102667083: /*linkId*/ return new String[] {"string"};
        case -1412808770: /*answer*/ return new String[] {"@Contract.term.offer.answer"};
        case -149460995: /*securityLabelNumber*/ return new String[] {"unsignedInt"};
        case 2046675654: /*valuedItem*/ return new String[] {};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("scope")) {
          this.scope = new CodeableConcept();
          return this.scope;
        }
        else if (name.equals("type")) {
          return addType();
        }
        else if (name.equals("typeReference")) {
          return addTypeReference();
        }
        else if (name.equals("subtype")) {
          return addSubtype();
        }
        else if (name.equals("relationship")) {
          this.relationship = new Coding();
          return this.relationship;
        }
        else if (name.equals("context")) {
          return addContext();
        }
        else if (name.equals("condition")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.condition");
        }
        else if (name.equals("periodType")) {
          return addPeriodType();
        }
        else if (name.equals("period")) {
          return addPeriod();
        }
        else if (name.equals("usePeriod")) {
          return addUsePeriod();
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.text");
        }
        else if (name.equals("linkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.linkId");
        }
        else if (name.equals("answer")) {
          return addAnswer();
        }
        else if (name.equals("securityLabelNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.securityLabelNumber");
        }
        else if (name.equals("valuedItem")) {
          return addValuedItem();
        }
        else
          return super.addChild(name);
      }

      public ContractAssetComponent copy() {
        ContractAssetComponent dst = new ContractAssetComponent();
        copyValues(dst);
        dst.scope = scope == null ? null : scope.copy();
        if (type != null) {
          dst.type = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : type)
            dst.type.add(i.copy());
        };
        if (typeReference != null) {
          dst.typeReference = new ArrayList<Reference>();
          for (Reference i : typeReference)
            dst.typeReference.add(i.copy());
        };
        if (subtype != null) {
          dst.subtype = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subtype)
            dst.subtype.add(i.copy());
        };
        dst.relationship = relationship == null ? null : relationship.copy();
        if (context != null) {
          dst.context = new ArrayList<AssetContextComponent>();
          for (AssetContextComponent i : context)
            dst.context.add(i.copy());
        };
        dst.condition = condition == null ? null : condition.copy();
        if (periodType != null) {
          dst.periodType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : periodType)
            dst.periodType.add(i.copy());
        };
        if (period != null) {
          dst.period = new ArrayList<Period>();
          for (Period i : period)
            dst.period.add(i.copy());
        };
        if (usePeriod != null) {
          dst.usePeriod = new ArrayList<Period>();
          for (Period i : usePeriod)
            dst.usePeriod.add(i.copy());
        };
        dst.text = text == null ? null : text.copy();
        if (linkId != null) {
          dst.linkId = new ArrayList<StringType>();
          for (StringType i : linkId)
            dst.linkId.add(i.copy());
        };
        if (answer != null) {
          dst.answer = new ArrayList<AnswerComponent>();
          for (AnswerComponent i : answer)
            dst.answer.add(i.copy());
        };
        if (securityLabelNumber != null) {
          dst.securityLabelNumber = new ArrayList<UnsignedIntType>();
          for (UnsignedIntType i : securityLabelNumber)
            dst.securityLabelNumber.add(i.copy());
        };
        if (valuedItem != null) {
          dst.valuedItem = new ArrayList<ValuedItemComponent>();
          for (ValuedItemComponent i : valuedItem)
            dst.valuedItem.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ContractAssetComponent))
          return false;
        ContractAssetComponent o = (ContractAssetComponent) other_;
        return compareDeep(scope, o.scope, true) && compareDeep(type, o.type, true) && compareDeep(typeReference, o.typeReference, true)
           && compareDeep(subtype, o.subtype, true) && compareDeep(relationship, o.relationship, true) && compareDeep(context, o.context, true)
           && compareDeep(condition, o.condition, true) && compareDeep(periodType, o.periodType, true) && compareDeep(period, o.period, true)
           && compareDeep(usePeriod, o.usePeriod, true) && compareDeep(text, o.text, true) && compareDeep(linkId, o.linkId, true)
           && compareDeep(answer, o.answer, true) && compareDeep(securityLabelNumber, o.securityLabelNumber, true)
           && compareDeep(valuedItem, o.valuedItem, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ContractAssetComponent))
          return false;
        ContractAssetComponent o = (ContractAssetComponent) other_;
        return compareValues(condition, o.condition, true) && compareValues(text, o.text, true) && compareValues(linkId, o.linkId, true)
           && compareValues(securityLabelNumber, o.securityLabelNumber, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(scope, type, typeReference
          , subtype, relationship, context, condition, periodType, period, usePeriod, text
          , linkId, answer, securityLabelNumber, valuedItem);
      }

  public String fhirType() {
    return "Contract.term.asset";

  }

  }

    @Block()
    public static class AssetContextComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository),  location held, e.g., building,  jurisdiction.
         */
        @Child(name = "reference", type = {Reference.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Creator,custodian or owner", formalDefinition="Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository),  location held, e.g., building,  jurisdiction." )
        protected Reference reference;

        /**
         * The actual object that is the target of the reference (Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository),  location held, e.g., building,  jurisdiction.)
         */
        protected Resource referenceTarget;

        /**
         * Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.
         */
        @Child(name = "code", type = {CodeableConcept.class}, order=2, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Codeable asset context", formalDefinition="Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-assetcontext")
        protected List<CodeableConcept> code;

        /**
         * Context description.
         */
        @Child(name = "text", type = {StringType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Context description", formalDefinition="Context description." )
        protected StringType text;

        private static final long serialVersionUID = -634115628L;

    /**
     * Constructor
     */
      public AssetContextComponent() {
        super();
      }

        /**
         * @return {@link #reference} (Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository),  location held, e.g., building,  jurisdiction.)
         */
        public Reference getReference() { 
          if (this.reference == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AssetContextComponent.reference");
            else if (Configuration.doAutoCreate())
              this.reference = new Reference(); // cc
          return this.reference;
        }

        public boolean hasReference() { 
          return this.reference != null && !this.reference.isEmpty();
        }

        /**
         * @param value {@link #reference} (Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository),  location held, e.g., building,  jurisdiction.)
         */
        public AssetContextComponent setReference(Reference value) { 
          this.reference = value;
          return this;
        }

        /**
         * @return {@link #reference} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository),  location held, e.g., building,  jurisdiction.)
         */
        public Resource getReferenceTarget() { 
          return this.referenceTarget;
        }

        /**
         * @param value {@link #reference} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository),  location held, e.g., building,  jurisdiction.)
         */
        public AssetContextComponent setReferenceTarget(Resource value) { 
          this.referenceTarget = value;
          return this;
        }

        /**
         * @return {@link #code} (Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.)
         */
        public List<CodeableConcept> getCode() { 
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          return this.code;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public AssetContextComponent setCode(List<CodeableConcept> theCode) { 
          this.code = theCode;
          return this;
        }

        public boolean hasCode() { 
          if (this.code == null)
            return false;
          for (CodeableConcept item : this.code)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return t;
        }

        public AssetContextComponent addCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.code == null)
            this.code = new ArrayList<CodeableConcept>();
          this.code.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #code}, creating it if it does not already exist
         */
        public CodeableConcept getCodeFirstRep() { 
          if (getCode().isEmpty()) {
            addCode();
          }
          return getCode().get(0);
        }

        /**
         * @return {@link #text} (Context description.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public StringType getTextElement() { 
          if (this.text == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create AssetContextComponent.text");
            else if (Configuration.doAutoCreate())
              this.text = new StringType(); // bb
          return this.text;
        }

        public boolean hasTextElement() { 
          return this.text != null && !this.text.isEmpty();
        }

        public boolean hasText() { 
          return this.text != null && !this.text.isEmpty();
        }

        /**
         * @param value {@link #text} (Context description.). This is the underlying object with id, value and extensions. The accessor "getText" gives direct access to the value
         */
        public AssetContextComponent setTextElement(StringType value) { 
          this.text = value;
          return this;
        }

        /**
         * @return Context description.
         */
        public String getText() { 
          return this.text == null ? null : this.text.getValue();
        }

        /**
         * @param value Context description.
         */
        public AssetContextComponent setText(String value) { 
          if (Utilities.noString(value))
            this.text = null;
          else {
            if (this.text == null)
              this.text = new StringType();
            this.text.setValue(value);
          }
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("reference", "Reference(Any)", "Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository),  location held, e.g., building,  jurisdiction.", 0, 1, reference));
          children.add(new Property("code", "CodeableConcept", "Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.", 0, java.lang.Integer.MAX_VALUE, code));
          children.add(new Property("text", "string", "Context description.", 0, 1, text));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -925155509: /*reference*/  return new Property("reference", "Reference(Any)", "Asset context reference may include the creator, custodian, or owning Person or Organization (e.g., bank, repository),  location held, e.g., building,  jurisdiction.", 0, 1, reference);
          case 3059181: /*code*/  return new Property("code", "CodeableConcept", "Coded representation of the context generally or of the Referenced entity, such as the asset holder type or location.", 0, java.lang.Integer.MAX_VALUE, code);
          case 3556653: /*text*/  return new Property("text", "string", "Context description.", 0, 1, text);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : new Base[] {this.reference}; // Reference
        case 3059181: /*code*/ return this.code == null ? new Base[0] : this.code.toArray(new Base[this.code.size()]); // CodeableConcept
        case 3556653: /*text*/ return this.text == null ? new Base[0] : new Base[] {this.text}; // StringType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -925155509: // reference
          this.reference = castToReference(value); // Reference
          return value;
        case 3059181: // code
          this.getCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 3556653: // text
          this.text = castToString(value); // StringType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = castToReference(value); // Reference
        } else if (name.equals("code")) {
          this.getCode().add(castToCodeableConcept(value));
        } else if (name.equals("text")) {
          this.text = castToString(value); // StringType
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509:  return getReference(); 
        case 3059181:  return addCode(); 
        case 3556653:  return getTextElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return new String[] {"Reference"};
        case 3059181: /*code*/ return new String[] {"CodeableConcept"};
        case 3556653: /*text*/ return new String[] {"string"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          this.reference = new Reference();
          return this.reference;
        }
        else if (name.equals("code")) {
          return addCode();
        }
        else if (name.equals("text")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.text");
        }
        else
          return super.addChild(name);
      }

      public AssetContextComponent copy() {
        AssetContextComponent dst = new AssetContextComponent();
        copyValues(dst);
        dst.reference = reference == null ? null : reference.copy();
        if (code != null) {
          dst.code = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : code)
            dst.code.add(i.copy());
        };
        dst.text = text == null ? null : text.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof AssetContextComponent))
          return false;
        AssetContextComponent o = (AssetContextComponent) other_;
        return compareDeep(reference, o.reference, true) && compareDeep(code, o.code, true) && compareDeep(text, o.text, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof AssetContextComponent))
          return false;
        AssetContextComponent o = (AssetContextComponent) other_;
        return compareValues(text, o.text, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(reference, code, text);
      }

  public String fhirType() {
    return "Contract.term.asset.context";

  }

  }

    @Block()
    public static class ValuedItemComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Specific type of Contract Valued Item that may be priced.
         */
        @Child(name = "entity", type = {CodeableConcept.class, Reference.class}, order=1, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Type", formalDefinition="Specific type of Contract Valued Item that may be priced." )
        protected Type entity;

        /**
         * Identifies a Contract Valued Item instance.
         */
        @Child(name = "identifier", type = {Identifier.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Number", formalDefinition="Identifies a Contract Valued Item instance." )
        protected Identifier identifier;

        /**
         * Indicates the time during which this Contract ValuedItem information is effective.
         */
        @Child(name = "effectiveTime", type = {DateTimeType.class}, order=3, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Effective Tiem", formalDefinition="Indicates the time during which this Contract ValuedItem information is effective." )
        protected DateTimeType effectiveTime;

        /**
         * Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.
         */
        @Child(name = "quantity", type = {Quantity.class}, order=4, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Count of Contract Valued Items", formalDefinition="Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances." )
        protected Quantity quantity;

        /**
         * A Contract Valued Item unit valuation measure.
         */
        @Child(name = "unitPrice", type = {Money.class}, order=5, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item fee, charge, or cost", formalDefinition="A Contract Valued Item unit valuation measure." )
        protected Money unitPrice;

        /**
         * A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        @Child(name = "factor", type = {DecimalType.class}, order=6, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Price Scaling Factor", formalDefinition="A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount." )
        protected DecimalType factor;

        /**
         * An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        @Child(name = "points", type = {DecimalType.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Valued Item Difficulty Scaling Factor", formalDefinition="An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point." )
        protected DecimalType points;

        /**
         * Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.
         */
        @Child(name = "net", type = {Money.class}, order=8, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Total Contract Valued Item Value", formalDefinition="Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied." )
        protected Money net;

        /**
         * Terms of valuation.
         */
        @Child(name = "payment", type = {StringType.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Terms of valuation", formalDefinition="Terms of valuation." )
        protected StringType payment;

        /**
         * When payment is due.
         */
        @Child(name = "paymentDate", type = {DateTimeType.class}, order=10, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When payment is due", formalDefinition="When payment is due." )
        protected DateTimeType paymentDate;

        /**
         * Who will make payment.
         */
        @Child(name = "responsible", type = {Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class}, order=11, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who will make payment", formalDefinition="Who will make payment." )
        protected Reference responsible;

        /**
         * The actual object that is the target of the reference (Who will make payment.)
         */
        protected Resource responsibleTarget;

        /**
         * Who will receive payment.
         */
        @Child(name = "recipient", type = {Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class}, order=12, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Who will receive payment", formalDefinition="Who will receive payment." )
        protected Reference recipient;

        /**
         * The actual object that is the target of the reference (Who will receive payment.)
         */
        protected Resource recipientTarget;

        /**
         * Id  of the clause or question text related to the context of this valuedItem in the referenced form or QuestionnaireResponse.
         */
        @Child(name = "linkId", type = {StringType.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to specific item", formalDefinition="Id  of the clause or question text related to the context of this valuedItem in the referenced form or QuestionnaireResponse." )
        protected List<StringType> linkId;

        /**
         * A set of security labels that define which terms are controlled by this condition.
         */
        @Child(name = "securityLabelNumber", type = {UnsignedIntType.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Security Labels that define affected terms", formalDefinition="A set of security labels that define which terms are controlled by this condition." )
        protected List<UnsignedIntType> securityLabelNumber;

        private static final long serialVersionUID = 1894951601L;

    /**
     * Constructor
     */
      public ValuedItemComponent() {
        super();
      }

        /**
         * @return {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public Type getEntity() { 
          return this.entity;
        }

        /**
         * @return {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public CodeableConcept getEntityCodeableConcept() throws FHIRException { 
          if (this.entity == null)
            this.entity = new CodeableConcept();
          if (!(this.entity instanceof CodeableConcept))
            throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.entity.getClass().getName()+" was encountered");
          return (CodeableConcept) this.entity;
        }

        public boolean hasEntityCodeableConcept() { 
          return this != null && this.entity instanceof CodeableConcept;
        }

        /**
         * @return {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public Reference getEntityReference() throws FHIRException { 
          if (this.entity == null)
            this.entity = new Reference();
          if (!(this.entity instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.entity.getClass().getName()+" was encountered");
          return (Reference) this.entity;
        }

        public boolean hasEntityReference() { 
          return this != null && this.entity instanceof Reference;
        }

        public boolean hasEntity() { 
          return this.entity != null && !this.entity.isEmpty();
        }

        /**
         * @param value {@link #entity} (Specific type of Contract Valued Item that may be priced.)
         */
        public ValuedItemComponent setEntity(Type value) { 
          if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
            throw new Error("Not the right type for Contract.term.asset.valuedItem.entity[x]: "+value.fhirType());
          this.entity = value;
          return this;
        }

        /**
         * @return {@link #identifier} (Identifies a Contract Valued Item instance.)
         */
        public Identifier getIdentifier() { 
          if (this.identifier == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.identifier");
            else if (Configuration.doAutoCreate())
              this.identifier = new Identifier(); // cc
          return this.identifier;
        }

        public boolean hasIdentifier() { 
          return this.identifier != null && !this.identifier.isEmpty();
        }

        /**
         * @param value {@link #identifier} (Identifies a Contract Valued Item instance.)
         */
        public ValuedItemComponent setIdentifier(Identifier value) { 
          this.identifier = value;
          return this;
        }

        /**
         * @return {@link #effectiveTime} (Indicates the time during which this Contract ValuedItem information is effective.). This is the underlying object with id, value and extensions. The accessor "getEffectiveTime" gives direct access to the value
         */
        public DateTimeType getEffectiveTimeElement() { 
          if (this.effectiveTime == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.effectiveTime");
            else if (Configuration.doAutoCreate())
              this.effectiveTime = new DateTimeType(); // bb
          return this.effectiveTime;
        }

        public boolean hasEffectiveTimeElement() { 
          return this.effectiveTime != null && !this.effectiveTime.isEmpty();
        }

        public boolean hasEffectiveTime() { 
          return this.effectiveTime != null && !this.effectiveTime.isEmpty();
        }

        /**
         * @param value {@link #effectiveTime} (Indicates the time during which this Contract ValuedItem information is effective.). This is the underlying object with id, value and extensions. The accessor "getEffectiveTime" gives direct access to the value
         */
        public ValuedItemComponent setEffectiveTimeElement(DateTimeType value) { 
          this.effectiveTime = value;
          return this;
        }

        /**
         * @return Indicates the time during which this Contract ValuedItem information is effective.
         */
        public Date getEffectiveTime() { 
          return this.effectiveTime == null ? null : this.effectiveTime.getValue();
        }

        /**
         * @param value Indicates the time during which this Contract ValuedItem information is effective.
         */
        public ValuedItemComponent setEffectiveTime(Date value) { 
          if (value == null)
            this.effectiveTime = null;
          else {
            if (this.effectiveTime == null)
              this.effectiveTime = new DateTimeType();
            this.effectiveTime.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #quantity} (Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.)
         */
        public Quantity getQuantity() { 
          if (this.quantity == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.quantity");
            else if (Configuration.doAutoCreate())
              this.quantity = new Quantity(); // cc
          return this.quantity;
        }

        public boolean hasQuantity() { 
          return this.quantity != null && !this.quantity.isEmpty();
        }

        /**
         * @param value {@link #quantity} (Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.)
         */
        public ValuedItemComponent setQuantity(Quantity value) { 
          this.quantity = value;
          return this;
        }

        /**
         * @return {@link #unitPrice} (A Contract Valued Item unit valuation measure.)
         */
        public Money getUnitPrice() { 
          if (this.unitPrice == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.unitPrice");
            else if (Configuration.doAutoCreate())
              this.unitPrice = new Money(); // cc
          return this.unitPrice;
        }

        public boolean hasUnitPrice() { 
          return this.unitPrice != null && !this.unitPrice.isEmpty();
        }

        /**
         * @param value {@link #unitPrice} (A Contract Valued Item unit valuation measure.)
         */
        public ValuedItemComponent setUnitPrice(Money value) { 
          this.unitPrice = value;
          return this;
        }

        /**
         * @return {@link #factor} (A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public DecimalType getFactorElement() { 
          if (this.factor == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.factor");
            else if (Configuration.doAutoCreate())
              this.factor = new DecimalType(); // bb
          return this.factor;
        }

        public boolean hasFactorElement() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        public boolean hasFactor() { 
          return this.factor != null && !this.factor.isEmpty();
        }

        /**
         * @param value {@link #factor} (A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.). This is the underlying object with id, value and extensions. The accessor "getFactor" gives direct access to the value
         */
        public ValuedItemComponent setFactorElement(DecimalType value) { 
          this.factor = value;
          return this;
        }

        /**
         * @return A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public BigDecimal getFactor() { 
          return this.factor == null ? null : this.factor.getValue();
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ValuedItemComponent setFactor(BigDecimal value) { 
          if (value == null)
            this.factor = null;
          else {
            if (this.factor == null)
              this.factor = new DecimalType();
            this.factor.setValue(value);
          }
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ValuedItemComponent setFactor(long value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @param value A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.
         */
        public ValuedItemComponent setFactor(double value) { 
              this.factor = new DecimalType();
            this.factor.setValue(value);
          return this;
        }

        /**
         * @return {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public DecimalType getPointsElement() { 
          if (this.points == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.points");
            else if (Configuration.doAutoCreate())
              this.points = new DecimalType(); // bb
          return this.points;
        }

        public boolean hasPointsElement() { 
          return this.points != null && !this.points.isEmpty();
        }

        public boolean hasPoints() { 
          return this.points != null && !this.points.isEmpty();
        }

        /**
         * @param value {@link #points} (An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.). This is the underlying object with id, value and extensions. The accessor "getPoints" gives direct access to the value
         */
        public ValuedItemComponent setPointsElement(DecimalType value) { 
          this.points = value;
          return this;
        }

        /**
         * @return An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public BigDecimal getPoints() { 
          return this.points == null ? null : this.points.getValue();
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public ValuedItemComponent setPoints(BigDecimal value) { 
          if (value == null)
            this.points = null;
          else {
            if (this.points == null)
              this.points = new DecimalType();
            this.points.setValue(value);
          }
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public ValuedItemComponent setPoints(long value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @param value An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.
         */
        public ValuedItemComponent setPoints(double value) { 
              this.points = new DecimalType();
            this.points.setValue(value);
          return this;
        }

        /**
         * @return {@link #net} (Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public Money getNet() { 
          if (this.net == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.net");
            else if (Configuration.doAutoCreate())
              this.net = new Money(); // cc
          return this.net;
        }

        public boolean hasNet() { 
          return this.net != null && !this.net.isEmpty();
        }

        /**
         * @param value {@link #net} (Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.)
         */
        public ValuedItemComponent setNet(Money value) { 
          this.net = value;
          return this;
        }

        /**
         * @return {@link #payment} (Terms of valuation.). This is the underlying object with id, value and extensions. The accessor "getPayment" gives direct access to the value
         */
        public StringType getPaymentElement() { 
          if (this.payment == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.payment");
            else if (Configuration.doAutoCreate())
              this.payment = new StringType(); // bb
          return this.payment;
        }

        public boolean hasPaymentElement() { 
          return this.payment != null && !this.payment.isEmpty();
        }

        public boolean hasPayment() { 
          return this.payment != null && !this.payment.isEmpty();
        }

        /**
         * @param value {@link #payment} (Terms of valuation.). This is the underlying object with id, value and extensions. The accessor "getPayment" gives direct access to the value
         */
        public ValuedItemComponent setPaymentElement(StringType value) { 
          this.payment = value;
          return this;
        }

        /**
         * @return Terms of valuation.
         */
        public String getPayment() { 
          return this.payment == null ? null : this.payment.getValue();
        }

        /**
         * @param value Terms of valuation.
         */
        public ValuedItemComponent setPayment(String value) { 
          if (Utilities.noString(value))
            this.payment = null;
          else {
            if (this.payment == null)
              this.payment = new StringType();
            this.payment.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #paymentDate} (When payment is due.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
         */
        public DateTimeType getPaymentDateElement() { 
          if (this.paymentDate == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.paymentDate");
            else if (Configuration.doAutoCreate())
              this.paymentDate = new DateTimeType(); // bb
          return this.paymentDate;
        }

        public boolean hasPaymentDateElement() { 
          return this.paymentDate != null && !this.paymentDate.isEmpty();
        }

        public boolean hasPaymentDate() { 
          return this.paymentDate != null && !this.paymentDate.isEmpty();
        }

        /**
         * @param value {@link #paymentDate} (When payment is due.). This is the underlying object with id, value and extensions. The accessor "getPaymentDate" gives direct access to the value
         */
        public ValuedItemComponent setPaymentDateElement(DateTimeType value) { 
          this.paymentDate = value;
          return this;
        }

        /**
         * @return When payment is due.
         */
        public Date getPaymentDate() { 
          return this.paymentDate == null ? null : this.paymentDate.getValue();
        }

        /**
         * @param value When payment is due.
         */
        public ValuedItemComponent setPaymentDate(Date value) { 
          if (value == null)
            this.paymentDate = null;
          else {
            if (this.paymentDate == null)
              this.paymentDate = new DateTimeType();
            this.paymentDate.setValue(value);
          }
          return this;
        }

        /**
         * @return {@link #responsible} (Who will make payment.)
         */
        public Reference getResponsible() { 
          if (this.responsible == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.responsible");
            else if (Configuration.doAutoCreate())
              this.responsible = new Reference(); // cc
          return this.responsible;
        }

        public boolean hasResponsible() { 
          return this.responsible != null && !this.responsible.isEmpty();
        }

        /**
         * @param value {@link #responsible} (Who will make payment.)
         */
        public ValuedItemComponent setResponsible(Reference value) { 
          this.responsible = value;
          return this;
        }

        /**
         * @return {@link #responsible} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who will make payment.)
         */
        public Resource getResponsibleTarget() { 
          return this.responsibleTarget;
        }

        /**
         * @param value {@link #responsible} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who will make payment.)
         */
        public ValuedItemComponent setResponsibleTarget(Resource value) { 
          this.responsibleTarget = value;
          return this;
        }

        /**
         * @return {@link #recipient} (Who will receive payment.)
         */
        public Reference getRecipient() { 
          if (this.recipient == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ValuedItemComponent.recipient");
            else if (Configuration.doAutoCreate())
              this.recipient = new Reference(); // cc
          return this.recipient;
        }

        public boolean hasRecipient() { 
          return this.recipient != null && !this.recipient.isEmpty();
        }

        /**
         * @param value {@link #recipient} (Who will receive payment.)
         */
        public ValuedItemComponent setRecipient(Reference value) { 
          this.recipient = value;
          return this;
        }

        /**
         * @return {@link #recipient} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Who will receive payment.)
         */
        public Resource getRecipientTarget() { 
          return this.recipientTarget;
        }

        /**
         * @param value {@link #recipient} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Who will receive payment.)
         */
        public ValuedItemComponent setRecipientTarget(Resource value) { 
          this.recipientTarget = value;
          return this;
        }

        /**
         * @return {@link #linkId} (Id  of the clause or question text related to the context of this valuedItem in the referenced form or QuestionnaireResponse.)
         */
        public List<StringType> getLinkId() { 
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          return this.linkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ValuedItemComponent setLinkId(List<StringType> theLinkId) { 
          this.linkId = theLinkId;
          return this;
        }

        public boolean hasLinkId() { 
          if (this.linkId == null)
            return false;
          for (StringType item : this.linkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #linkId} (Id  of the clause or question text related to the context of this valuedItem in the referenced form or QuestionnaireResponse.)
         */
        public StringType addLinkIdElement() {//2 
          StringType t = new StringType();
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          this.linkId.add(t);
          return t;
        }

        /**
         * @param value {@link #linkId} (Id  of the clause or question text related to the context of this valuedItem in the referenced form or QuestionnaireResponse.)
         */
        public ValuedItemComponent addLinkId(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          this.linkId.add(t);
          return this;
        }

        /**
         * @param value {@link #linkId} (Id  of the clause or question text related to the context of this valuedItem in the referenced form or QuestionnaireResponse.)
         */
        public boolean hasLinkId(String value) { 
          if (this.linkId == null)
            return false;
          for (StringType v : this.linkId)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #securityLabelNumber} (A set of security labels that define which terms are controlled by this condition.)
         */
        public List<UnsignedIntType> getSecurityLabelNumber() { 
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          return this.securityLabelNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ValuedItemComponent setSecurityLabelNumber(List<UnsignedIntType> theSecurityLabelNumber) { 
          this.securityLabelNumber = theSecurityLabelNumber;
          return this;
        }

        public boolean hasSecurityLabelNumber() { 
          if (this.securityLabelNumber == null)
            return false;
          for (UnsignedIntType item : this.securityLabelNumber)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #securityLabelNumber} (A set of security labels that define which terms are controlled by this condition.)
         */
        public UnsignedIntType addSecurityLabelNumberElement() {//2 
          UnsignedIntType t = new UnsignedIntType();
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          this.securityLabelNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #securityLabelNumber} (A set of security labels that define which terms are controlled by this condition.)
         */
        public ValuedItemComponent addSecurityLabelNumber(int value) { //1
          UnsignedIntType t = new UnsignedIntType();
          t.setValue(value);
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          this.securityLabelNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #securityLabelNumber} (A set of security labels that define which terms are controlled by this condition.)
         */
        public boolean hasSecurityLabelNumber(int value) { 
          if (this.securityLabelNumber == null)
            return false;
          for (UnsignedIntType v : this.securityLabelNumber)
            if (v.getValue().equals(value)) // unsignedInt
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity));
          children.add(new Property("identifier", "Identifier", "Identifies a Contract Valued Item instance.", 0, 1, identifier));
          children.add(new Property("effectiveTime", "dateTime", "Indicates the time during which this Contract ValuedItem information is effective.", 0, 1, effectiveTime));
          children.add(new Property("quantity", "SimpleQuantity", "Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.", 0, 1, quantity));
          children.add(new Property("unitPrice", "Money", "A Contract Valued Item unit valuation measure.", 0, 1, unitPrice));
          children.add(new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor));
          children.add(new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.", 0, 1, points));
          children.add(new Property("net", "Money", "Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, 1, net));
          children.add(new Property("payment", "string", "Terms of valuation.", 0, 1, payment));
          children.add(new Property("paymentDate", "dateTime", "When payment is due.", 0, 1, paymentDate));
          children.add(new Property("responsible", "Reference(Organization|Patient|Practitioner|PractitionerRole|RelatedPerson)", "Who will make payment.", 0, 1, responsible));
          children.add(new Property("recipient", "Reference(Organization|Patient|Practitioner|PractitionerRole|RelatedPerson)", "Who will receive payment.", 0, 1, recipient));
          children.add(new Property("linkId", "string", "Id  of the clause or question text related to the context of this valuedItem in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, linkId));
          children.add(new Property("securityLabelNumber", "unsignedInt", "A set of security labels that define which terms are controlled by this condition.", 0, java.lang.Integer.MAX_VALUE, securityLabelNumber));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -740568643: /*entity[x]*/  return new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity);
          case -1298275357: /*entity*/  return new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity);
          case 924197182: /*entityCodeableConcept*/  return new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity);
          case -356635992: /*entityReference*/  return new Property("entity[x]", "CodeableConcept|Reference(Any)", "Specific type of Contract Valued Item that may be priced.", 0, 1, entity);
          case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Identifies a Contract Valued Item instance.", 0, 1, identifier);
          case -929905388: /*effectiveTime*/  return new Property("effectiveTime", "dateTime", "Indicates the time during which this Contract ValuedItem information is effective.", 0, 1, effectiveTime);
          case -1285004149: /*quantity*/  return new Property("quantity", "SimpleQuantity", "Specifies the units by which the Contract Valued Item is measured or counted, and quantifies the countable or measurable Contract Valued Item instances.", 0, 1, quantity);
          case -486196699: /*unitPrice*/  return new Property("unitPrice", "Money", "A Contract Valued Item unit valuation measure.", 0, 1, unitPrice);
          case -1282148017: /*factor*/  return new Property("factor", "decimal", "A real number that represents a multiplier used in determining the overall value of the Contract Valued Item delivered. The concept of a Factor allows for a discount or surcharge multiplier to be applied to a monetary amount.", 0, 1, factor);
          case -982754077: /*points*/  return new Property("points", "decimal", "An amount that expresses the weighting (based on difficulty, cost and/or resource intensiveness) associated with the Contract Valued Item delivered. The concept of Points allows for assignment of point values for a Contract Valued Item, such that a monetary amount can be assigned to each point.", 0, 1, points);
          case 108957: /*net*/  return new Property("net", "Money", "Expresses the product of the Contract Valued Item unitQuantity and the unitPriceAmt. For example, the formula: unit Quantity * unit Price (Cost per Point) * factor Number  * points = net Amount. Quantity, factor and points are assumed to be 1 if not supplied.", 0, 1, net);
          case -786681338: /*payment*/  return new Property("payment", "string", "Terms of valuation.", 0, 1, payment);
          case -1540873516: /*paymentDate*/  return new Property("paymentDate", "dateTime", "When payment is due.", 0, 1, paymentDate);
          case 1847674614: /*responsible*/  return new Property("responsible", "Reference(Organization|Patient|Practitioner|PractitionerRole|RelatedPerson)", "Who will make payment.", 0, 1, responsible);
          case 820081177: /*recipient*/  return new Property("recipient", "Reference(Organization|Patient|Practitioner|PractitionerRole|RelatedPerson)", "Who will receive payment.", 0, 1, recipient);
          case -1102667083: /*linkId*/  return new Property("linkId", "string", "Id  of the clause or question text related to the context of this valuedItem in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, linkId);
          case -149460995: /*securityLabelNumber*/  return new Property("securityLabelNumber", "unsignedInt", "A set of security labels that define which terms are controlled by this condition.", 0, java.lang.Integer.MAX_VALUE, securityLabelNumber);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1298275357: /*entity*/ return this.entity == null ? new Base[0] : new Base[] {this.entity}; // Type
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : new Base[] {this.identifier}; // Identifier
        case -929905388: /*effectiveTime*/ return this.effectiveTime == null ? new Base[0] : new Base[] {this.effectiveTime}; // DateTimeType
        case -1285004149: /*quantity*/ return this.quantity == null ? new Base[0] : new Base[] {this.quantity}; // Quantity
        case -486196699: /*unitPrice*/ return this.unitPrice == null ? new Base[0] : new Base[] {this.unitPrice}; // Money
        case -1282148017: /*factor*/ return this.factor == null ? new Base[0] : new Base[] {this.factor}; // DecimalType
        case -982754077: /*points*/ return this.points == null ? new Base[0] : new Base[] {this.points}; // DecimalType
        case 108957: /*net*/ return this.net == null ? new Base[0] : new Base[] {this.net}; // Money
        case -786681338: /*payment*/ return this.payment == null ? new Base[0] : new Base[] {this.payment}; // StringType
        case -1540873516: /*paymentDate*/ return this.paymentDate == null ? new Base[0] : new Base[] {this.paymentDate}; // DateTimeType
        case 1847674614: /*responsible*/ return this.responsible == null ? new Base[0] : new Base[] {this.responsible}; // Reference
        case 820081177: /*recipient*/ return this.recipient == null ? new Base[0] : new Base[] {this.recipient}; // Reference
        case -1102667083: /*linkId*/ return this.linkId == null ? new Base[0] : this.linkId.toArray(new Base[this.linkId.size()]); // StringType
        case -149460995: /*securityLabelNumber*/ return this.securityLabelNumber == null ? new Base[0] : this.securityLabelNumber.toArray(new Base[this.securityLabelNumber.size()]); // UnsignedIntType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1298275357: // entity
          this.entity = castToType(value); // Type
          return value;
        case -1618432855: // identifier
          this.identifier = castToIdentifier(value); // Identifier
          return value;
        case -929905388: // effectiveTime
          this.effectiveTime = castToDateTime(value); // DateTimeType
          return value;
        case -1285004149: // quantity
          this.quantity = castToQuantity(value); // Quantity
          return value;
        case -486196699: // unitPrice
          this.unitPrice = castToMoney(value); // Money
          return value;
        case -1282148017: // factor
          this.factor = castToDecimal(value); // DecimalType
          return value;
        case -982754077: // points
          this.points = castToDecimal(value); // DecimalType
          return value;
        case 108957: // net
          this.net = castToMoney(value); // Money
          return value;
        case -786681338: // payment
          this.payment = castToString(value); // StringType
          return value;
        case -1540873516: // paymentDate
          this.paymentDate = castToDateTime(value); // DateTimeType
          return value;
        case 1847674614: // responsible
          this.responsible = castToReference(value); // Reference
          return value;
        case 820081177: // recipient
          this.recipient = castToReference(value); // Reference
          return value;
        case -1102667083: // linkId
          this.getLinkId().add(castToString(value)); // StringType
          return value;
        case -149460995: // securityLabelNumber
          this.getSecurityLabelNumber().add(castToUnsignedInt(value)); // UnsignedIntType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("entity[x]")) {
          this.entity = castToType(value); // Type
        } else if (name.equals("identifier")) {
          this.identifier = castToIdentifier(value); // Identifier
        } else if (name.equals("effectiveTime")) {
          this.effectiveTime = castToDateTime(value); // DateTimeType
        } else if (name.equals("quantity")) {
          this.quantity = castToQuantity(value); // Quantity
        } else if (name.equals("unitPrice")) {
          this.unitPrice = castToMoney(value); // Money
        } else if (name.equals("factor")) {
          this.factor = castToDecimal(value); // DecimalType
        } else if (name.equals("points")) {
          this.points = castToDecimal(value); // DecimalType
        } else if (name.equals("net")) {
          this.net = castToMoney(value); // Money
        } else if (name.equals("payment")) {
          this.payment = castToString(value); // StringType
        } else if (name.equals("paymentDate")) {
          this.paymentDate = castToDateTime(value); // DateTimeType
        } else if (name.equals("responsible")) {
          this.responsible = castToReference(value); // Reference
        } else if (name.equals("recipient")) {
          this.recipient = castToReference(value); // Reference
        } else if (name.equals("linkId")) {
          this.getLinkId().add(castToString(value));
        } else if (name.equals("securityLabelNumber")) {
          this.getSecurityLabelNumber().add(castToUnsignedInt(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -740568643:  return getEntity(); 
        case -1298275357:  return getEntity(); 
        case -1618432855:  return getIdentifier(); 
        case -929905388:  return getEffectiveTimeElement();
        case -1285004149:  return getQuantity(); 
        case -486196699:  return getUnitPrice(); 
        case -1282148017:  return getFactorElement();
        case -982754077:  return getPointsElement();
        case 108957:  return getNet(); 
        case -786681338:  return getPaymentElement();
        case -1540873516:  return getPaymentDateElement();
        case 1847674614:  return getResponsible(); 
        case 820081177:  return getRecipient(); 
        case -1102667083:  return addLinkIdElement();
        case -149460995:  return addSecurityLabelNumberElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1298275357: /*entity*/ return new String[] {"CodeableConcept", "Reference"};
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case -929905388: /*effectiveTime*/ return new String[] {"dateTime"};
        case -1285004149: /*quantity*/ return new String[] {"SimpleQuantity"};
        case -486196699: /*unitPrice*/ return new String[] {"Money"};
        case -1282148017: /*factor*/ return new String[] {"decimal"};
        case -982754077: /*points*/ return new String[] {"decimal"};
        case 108957: /*net*/ return new String[] {"Money"};
        case -786681338: /*payment*/ return new String[] {"string"};
        case -1540873516: /*paymentDate*/ return new String[] {"dateTime"};
        case 1847674614: /*responsible*/ return new String[] {"Reference"};
        case 820081177: /*recipient*/ return new String[] {"Reference"};
        case -1102667083: /*linkId*/ return new String[] {"string"};
        case -149460995: /*securityLabelNumber*/ return new String[] {"unsignedInt"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("entityCodeableConcept")) {
          this.entity = new CodeableConcept();
          return this.entity;
        }
        else if (name.equals("entityReference")) {
          this.entity = new Reference();
          return this.entity;
        }
        else if (name.equals("identifier")) {
          this.identifier = new Identifier();
          return this.identifier;
        }
        else if (name.equals("effectiveTime")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.effectiveTime");
        }
        else if (name.equals("quantity")) {
          this.quantity = new Quantity();
          return this.quantity;
        }
        else if (name.equals("unitPrice")) {
          this.unitPrice = new Money();
          return this.unitPrice;
        }
        else if (name.equals("factor")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.factor");
        }
        else if (name.equals("points")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.points");
        }
        else if (name.equals("net")) {
          this.net = new Money();
          return this.net;
        }
        else if (name.equals("payment")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.payment");
        }
        else if (name.equals("paymentDate")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.paymentDate");
        }
        else if (name.equals("responsible")) {
          this.responsible = new Reference();
          return this.responsible;
        }
        else if (name.equals("recipient")) {
          this.recipient = new Reference();
          return this.recipient;
        }
        else if (name.equals("linkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.linkId");
        }
        else if (name.equals("securityLabelNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.securityLabelNumber");
        }
        else
          return super.addChild(name);
      }

      public ValuedItemComponent copy() {
        ValuedItemComponent dst = new ValuedItemComponent();
        copyValues(dst);
        dst.entity = entity == null ? null : entity.copy();
        dst.identifier = identifier == null ? null : identifier.copy();
        dst.effectiveTime = effectiveTime == null ? null : effectiveTime.copy();
        dst.quantity = quantity == null ? null : quantity.copy();
        dst.unitPrice = unitPrice == null ? null : unitPrice.copy();
        dst.factor = factor == null ? null : factor.copy();
        dst.points = points == null ? null : points.copy();
        dst.net = net == null ? null : net.copy();
        dst.payment = payment == null ? null : payment.copy();
        dst.paymentDate = paymentDate == null ? null : paymentDate.copy();
        dst.responsible = responsible == null ? null : responsible.copy();
        dst.recipient = recipient == null ? null : recipient.copy();
        if (linkId != null) {
          dst.linkId = new ArrayList<StringType>();
          for (StringType i : linkId)
            dst.linkId.add(i.copy());
        };
        if (securityLabelNumber != null) {
          dst.securityLabelNumber = new ArrayList<UnsignedIntType>();
          for (UnsignedIntType i : securityLabelNumber)
            dst.securityLabelNumber.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ValuedItemComponent))
          return false;
        ValuedItemComponent o = (ValuedItemComponent) other_;
        return compareDeep(entity, o.entity, true) && compareDeep(identifier, o.identifier, true) && compareDeep(effectiveTime, o.effectiveTime, true)
           && compareDeep(quantity, o.quantity, true) && compareDeep(unitPrice, o.unitPrice, true) && compareDeep(factor, o.factor, true)
           && compareDeep(points, o.points, true) && compareDeep(net, o.net, true) && compareDeep(payment, o.payment, true)
           && compareDeep(paymentDate, o.paymentDate, true) && compareDeep(responsible, o.responsible, true)
           && compareDeep(recipient, o.recipient, true) && compareDeep(linkId, o.linkId, true) && compareDeep(securityLabelNumber, o.securityLabelNumber, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ValuedItemComponent))
          return false;
        ValuedItemComponent o = (ValuedItemComponent) other_;
        return compareValues(effectiveTime, o.effectiveTime, true) && compareValues(factor, o.factor, true)
           && compareValues(points, o.points, true) && compareValues(payment, o.payment, true) && compareValues(paymentDate, o.paymentDate, true)
           && compareValues(linkId, o.linkId, true) && compareValues(securityLabelNumber, o.securityLabelNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(entity, identifier, effectiveTime
          , quantity, unitPrice, factor, points, net, payment, paymentDate, responsible
          , recipient, linkId, securityLabelNumber);
      }

  public String fhirType() {
    return "Contract.term.asset.valuedItem";

  }

  }

    @Block()
    public static class ActionComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * True if the term prohibits the  action.
         */
        @Child(name = "doNotPerform", type = {BooleanType.class}, order=1, min=0, max=1, modifier=true, summary=false)
        @Description(shortDefinition="True if the term prohibits the  action", formalDefinition="True if the term prohibits the  action." )
        protected BooleanType doNotPerform;

        /**
         * Activity or service obligation to be done or not done, performed or not performed, effectuated or not by this Contract term.
         */
        @Child(name = "type", type = {CodeableConcept.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Type or form of the action", formalDefinition="Activity or service obligation to be done or not done, performed or not performed, effectuated or not by this Contract term." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-action")
        protected CodeableConcept type;

        /**
         * Entity of the action.
         */
        @Child(name = "subject", type = {}, order=3, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Entity of the action", formalDefinition="Entity of the action." )
        protected List<ActionSubjectComponent> subject;

        /**
         * Reason or purpose for the action stipulated by this Contract Provision.
         */
        @Child(name = "intent", type = {CodeableConcept.class}, order=4, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Purpose for the Contract Term Action", formalDefinition="Reason or purpose for the action stipulated by this Contract Provision." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://terminology.hl7.org/ValueSet/v3-PurposeOfUse")
        protected CodeableConcept intent;

        /**
         * Id [identifier??] of the clause or question text related to this action in the referenced form or QuestionnaireResponse.
         */
        @Child(name = "linkId", type = {StringType.class}, order=5, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to specific item", formalDefinition="Id [identifier??] of the clause or question text related to this action in the referenced form or QuestionnaireResponse." )
        protected List<StringType> linkId;

        /**
         * Current state of the term action.
         */
        @Child(name = "status", type = {CodeableConcept.class}, order=6, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="State of the action", formalDefinition="Current state of the term action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-actionstatus")
        protected CodeableConcept status;

        /**
         * Encounter or Episode with primary association to specified term activity.
         */
        @Child(name = "context", type = {Encounter.class, EpisodeOfCare.class}, order=7, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Episode associated with action", formalDefinition="Encounter or Episode with primary association to specified term activity." )
        protected Reference context;

        /**
         * The actual object that is the target of the reference (Encounter or Episode with primary association to specified term activity.)
         */
        protected Resource contextTarget;

        /**
         * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.
         */
        @Child(name = "contextLinkId", type = {StringType.class}, order=8, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to specific item", formalDefinition="Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse." )
        protected List<StringType> contextLinkId;

        /**
         * When action happens.
         */
        @Child(name = "occurrence", type = {DateTimeType.class, Period.class, Timing.class}, order=9, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="When action happens", formalDefinition="When action happens." )
        protected Type occurrence;

        /**
         * Who or what initiated the action and has responsibility for its activation.
         */
        @Child(name = "requester", type = {Patient.class, RelatedPerson.class, Practitioner.class, PractitionerRole.class, Device.class, Group.class, Organization.class}, order=10, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Who asked for action", formalDefinition="Who or what initiated the action and has responsibility for its activation." )
        protected List<Reference> requester;
        /**
         * The actual objects that are the target of the reference (Who or what initiated the action and has responsibility for its activation.)
         */
        protected List<Resource> requesterTarget;


        /**
         * Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.
         */
        @Child(name = "requesterLinkId", type = {StringType.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to specific item", formalDefinition="Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse." )
        protected List<StringType> requesterLinkId;

        /**
         * The type of individual that is desired or required to perform or not perform the action.
         */
        @Child(name = "performerType", type = {CodeableConcept.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Kind of service performer", formalDefinition="The type of individual that is desired or required to perform or not perform the action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/provenance-agent-type")
        protected List<CodeableConcept> performerType;

        /**
         * The type of role or competency of an individual desired or required to perform or not perform the action.
         */
        @Child(name = "performerRole", type = {CodeableConcept.class}, order=13, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Competency of the performer", formalDefinition="The type of role or competency of an individual desired or required to perform or not perform the action." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/provenance-agent-role")
        protected CodeableConcept performerRole;

        /**
         * Indicates who or what is being asked to perform (or not perform) the ction.
         */
        @Child(name = "performer", type = {RelatedPerson.class, Patient.class, Practitioner.class, PractitionerRole.class, CareTeam.class, Device.class, Substance.class, Organization.class, Location.class}, order=14, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Actor that wil execute (or not) the action", formalDefinition="Indicates who or what is being asked to perform (or not perform) the ction." )
        protected Reference performer;

        /**
         * The actual object that is the target of the reference (Indicates who or what is being asked to perform (or not perform) the ction.)
         */
        protected Resource performerTarget;

        /**
         * Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.
         */
        @Child(name = "performerLinkId", type = {StringType.class}, order=15, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to specific item", formalDefinition="Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse." )
        protected List<StringType> performerLinkId;

        /**
         * Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.
         */
        @Child(name = "reasonCode", type = {CodeableConcept.class}, order=16, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Why is action (not) needed?", formalDefinition="Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://terminology.hl7.org/ValueSet/v3-PurposeOfUse")
        protected List<CodeableConcept> reasonCode;

        /**
         * Indicates another resource whose existence justifies permitting or not permitting this action.
         */
        @Child(name = "reasonReference", type = {Condition.class, Observation.class, DiagnosticReport.class, DocumentReference.class, Questionnaire.class, QuestionnaireResponse.class}, order=17, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Why is action (not) needed?", formalDefinition="Indicates another resource whose existence justifies permitting or not permitting this action." )
        protected List<Reference> reasonReference;
        /**
         * The actual objects that are the target of the reference (Indicates another resource whose existence justifies permitting or not permitting this action.)
         */
        protected List<Resource> reasonReferenceTarget;


        /**
         * Describes why the action is to be performed or not performed in textual form.
         */
        @Child(name = "reason", type = {StringType.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Why action is to be performed", formalDefinition="Describes why the action is to be performed or not performed in textual form." )
        protected List<StringType> reason;

        /**
         * Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.
         */
        @Child(name = "reasonLinkId", type = {StringType.class}, order=19, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Pointer to specific item", formalDefinition="Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse." )
        protected List<StringType> reasonLinkId;

        /**
         * Comments made about the term action made by the requester, performer, subject or other participants.
         */
        @Child(name = "note", type = {Annotation.class}, order=20, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Comments about the action", formalDefinition="Comments made about the term action made by the requester, performer, subject or other participants." )
        protected List<Annotation> note;

        /**
         * Security labels that protects the action.
         */
        @Child(name = "securityLabelNumber", type = {UnsignedIntType.class}, order=21, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Action restriction numbers", formalDefinition="Security labels that protects the action." )
        protected List<UnsignedIntType> securityLabelNumber;

        private static final long serialVersionUID = -178728180L;

    /**
     * Constructor
     */
      public ActionComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ActionComponent(CodeableConcept type, CodeableConcept intent, CodeableConcept status) {
        super();
        this.type = type;
        this.intent = intent;
        this.status = status;
      }

        /**
         * @return {@link #doNotPerform} (True if the term prohibits the  action.). This is the underlying object with id, value and extensions. The accessor "getDoNotPerform" gives direct access to the value
         */
        public BooleanType getDoNotPerformElement() { 
          if (this.doNotPerform == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionComponent.doNotPerform");
            else if (Configuration.doAutoCreate())
              this.doNotPerform = new BooleanType(); // bb
          return this.doNotPerform;
        }

        public boolean hasDoNotPerformElement() { 
          return this.doNotPerform != null && !this.doNotPerform.isEmpty();
        }

        public boolean hasDoNotPerform() { 
          return this.doNotPerform != null && !this.doNotPerform.isEmpty();
        }

        /**
         * @param value {@link #doNotPerform} (True if the term prohibits the  action.). This is the underlying object with id, value and extensions. The accessor "getDoNotPerform" gives direct access to the value
         */
        public ActionComponent setDoNotPerformElement(BooleanType value) { 
          this.doNotPerform = value;
          return this;
        }

        /**
         * @return True if the term prohibits the  action.
         */
        public boolean getDoNotPerform() { 
          return this.doNotPerform == null || this.doNotPerform.isEmpty() ? false : this.doNotPerform.getValue();
        }

        /**
         * @param value True if the term prohibits the  action.
         */
        public ActionComponent setDoNotPerform(boolean value) { 
            if (this.doNotPerform == null)
              this.doNotPerform = new BooleanType();
            this.doNotPerform.setValue(value);
          return this;
        }

        /**
         * @return {@link #type} (Activity or service obligation to be done or not done, performed or not performed, effectuated or not by this Contract term.)
         */
        public CodeableConcept getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new CodeableConcept(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Activity or service obligation to be done or not done, performed or not performed, effectuated or not by this Contract term.)
         */
        public ActionComponent setType(CodeableConcept value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #subject} (Entity of the action.)
         */
        public List<ActionSubjectComponent> getSubject() { 
          if (this.subject == null)
            this.subject = new ArrayList<ActionSubjectComponent>();
          return this.subject;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setSubject(List<ActionSubjectComponent> theSubject) { 
          this.subject = theSubject;
          return this;
        }

        public boolean hasSubject() { 
          if (this.subject == null)
            return false;
          for (ActionSubjectComponent item : this.subject)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public ActionSubjectComponent addSubject() { //3
          ActionSubjectComponent t = new ActionSubjectComponent();
          if (this.subject == null)
            this.subject = new ArrayList<ActionSubjectComponent>();
          this.subject.add(t);
          return t;
        }

        public ActionComponent addSubject(ActionSubjectComponent t) { //3
          if (t == null)
            return this;
          if (this.subject == null)
            this.subject = new ArrayList<ActionSubjectComponent>();
          this.subject.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #subject}, creating it if it does not already exist
         */
        public ActionSubjectComponent getSubjectFirstRep() { 
          if (getSubject().isEmpty()) {
            addSubject();
          }
          return getSubject().get(0);
        }

        /**
         * @return {@link #intent} (Reason or purpose for the action stipulated by this Contract Provision.)
         */
        public CodeableConcept getIntent() { 
          if (this.intent == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionComponent.intent");
            else if (Configuration.doAutoCreate())
              this.intent = new CodeableConcept(); // cc
          return this.intent;
        }

        public boolean hasIntent() { 
          return this.intent != null && !this.intent.isEmpty();
        }

        /**
         * @param value {@link #intent} (Reason or purpose for the action stipulated by this Contract Provision.)
         */
        public ActionComponent setIntent(CodeableConcept value) { 
          this.intent = value;
          return this;
        }

        /**
         * @return {@link #linkId} (Id [identifier??] of the clause or question text related to this action in the referenced form or QuestionnaireResponse.)
         */
        public List<StringType> getLinkId() { 
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          return this.linkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setLinkId(List<StringType> theLinkId) { 
          this.linkId = theLinkId;
          return this;
        }

        public boolean hasLinkId() { 
          if (this.linkId == null)
            return false;
          for (StringType item : this.linkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #linkId} (Id [identifier??] of the clause or question text related to this action in the referenced form or QuestionnaireResponse.)
         */
        public StringType addLinkIdElement() {//2 
          StringType t = new StringType();
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          this.linkId.add(t);
          return t;
        }

        /**
         * @param value {@link #linkId} (Id [identifier??] of the clause or question text related to this action in the referenced form or QuestionnaireResponse.)
         */
        public ActionComponent addLinkId(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.linkId == null)
            this.linkId = new ArrayList<StringType>();
          this.linkId.add(t);
          return this;
        }

        /**
         * @param value {@link #linkId} (Id [identifier??] of the clause or question text related to this action in the referenced form or QuestionnaireResponse.)
         */
        public boolean hasLinkId(String value) { 
          if (this.linkId == null)
            return false;
          for (StringType v : this.linkId)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #status} (Current state of the term action.)
         */
        public CodeableConcept getStatus() { 
          if (this.status == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionComponent.status");
            else if (Configuration.doAutoCreate())
              this.status = new CodeableConcept(); // cc
          return this.status;
        }

        public boolean hasStatus() { 
          return this.status != null && !this.status.isEmpty();
        }

        /**
         * @param value {@link #status} (Current state of the term action.)
         */
        public ActionComponent setStatus(CodeableConcept value) { 
          this.status = value;
          return this;
        }

        /**
         * @return {@link #context} (Encounter or Episode with primary association to specified term activity.)
         */
        public Reference getContext() { 
          if (this.context == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionComponent.context");
            else if (Configuration.doAutoCreate())
              this.context = new Reference(); // cc
          return this.context;
        }

        public boolean hasContext() { 
          return this.context != null && !this.context.isEmpty();
        }

        /**
         * @param value {@link #context} (Encounter or Episode with primary association to specified term activity.)
         */
        public ActionComponent setContext(Reference value) { 
          this.context = value;
          return this;
        }

        /**
         * @return {@link #context} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Encounter or Episode with primary association to specified term activity.)
         */
        public Resource getContextTarget() { 
          return this.contextTarget;
        }

        /**
         * @param value {@link #context} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Encounter or Episode with primary association to specified term activity.)
         */
        public ActionComponent setContextTarget(Resource value) { 
          this.contextTarget = value;
          return this;
        }

        /**
         * @return {@link #contextLinkId} (Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.)
         */
        public List<StringType> getContextLinkId() { 
          if (this.contextLinkId == null)
            this.contextLinkId = new ArrayList<StringType>();
          return this.contextLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setContextLinkId(List<StringType> theContextLinkId) { 
          this.contextLinkId = theContextLinkId;
          return this;
        }

        public boolean hasContextLinkId() { 
          if (this.contextLinkId == null)
            return false;
          for (StringType item : this.contextLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #contextLinkId} (Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.)
         */
        public StringType addContextLinkIdElement() {//2 
          StringType t = new StringType();
          if (this.contextLinkId == null)
            this.contextLinkId = new ArrayList<StringType>();
          this.contextLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #contextLinkId} (Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.)
         */
        public ActionComponent addContextLinkId(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.contextLinkId == null)
            this.contextLinkId = new ArrayList<StringType>();
          this.contextLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #contextLinkId} (Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.)
         */
        public boolean hasContextLinkId(String value) { 
          if (this.contextLinkId == null)
            return false;
          for (StringType v : this.contextLinkId)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #occurrence} (When action happens.)
         */
        public Type getOccurrence() { 
          return this.occurrence;
        }

        /**
         * @return {@link #occurrence} (When action happens.)
         */
        public DateTimeType getOccurrenceDateTimeType() throws FHIRException { 
          if (this.occurrence == null)
            this.occurrence = new DateTimeType();
          if (!(this.occurrence instanceof DateTimeType))
            throw new FHIRException("Type mismatch: the type DateTimeType was expected, but "+this.occurrence.getClass().getName()+" was encountered");
          return (DateTimeType) this.occurrence;
        }

        public boolean hasOccurrenceDateTimeType() { 
          return this != null && this.occurrence instanceof DateTimeType;
        }

        /**
         * @return {@link #occurrence} (When action happens.)
         */
        public Period getOccurrencePeriod() throws FHIRException { 
          if (this.occurrence == null)
            this.occurrence = new Period();
          if (!(this.occurrence instanceof Period))
            throw new FHIRException("Type mismatch: the type Period was expected, but "+this.occurrence.getClass().getName()+" was encountered");
          return (Period) this.occurrence;
        }

        public boolean hasOccurrencePeriod() { 
          return this != null && this.occurrence instanceof Period;
        }

        /**
         * @return {@link #occurrence} (When action happens.)
         */
        public Timing getOccurrenceTiming() throws FHIRException { 
          if (this.occurrence == null)
            this.occurrence = new Timing();
          if (!(this.occurrence instanceof Timing))
            throw new FHIRException("Type mismatch: the type Timing was expected, but "+this.occurrence.getClass().getName()+" was encountered");
          return (Timing) this.occurrence;
        }

        public boolean hasOccurrenceTiming() { 
          return this != null && this.occurrence instanceof Timing;
        }

        public boolean hasOccurrence() { 
          return this.occurrence != null && !this.occurrence.isEmpty();
        }

        /**
         * @param value {@link #occurrence} (When action happens.)
         */
        public ActionComponent setOccurrence(Type value) { 
          if (value != null && !(value instanceof DateTimeType || value instanceof Period || value instanceof Timing))
            throw new Error("Not the right type for Contract.term.action.occurrence[x]: "+value.fhirType());
          this.occurrence = value;
          return this;
        }

        /**
         * @return {@link #requester} (Who or what initiated the action and has responsibility for its activation.)
         */
        public List<Reference> getRequester() { 
          if (this.requester == null)
            this.requester = new ArrayList<Reference>();
          return this.requester;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setRequester(List<Reference> theRequester) { 
          this.requester = theRequester;
          return this;
        }

        public boolean hasRequester() { 
          if (this.requester == null)
            return false;
          for (Reference item : this.requester)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addRequester() { //3
          Reference t = new Reference();
          if (this.requester == null)
            this.requester = new ArrayList<Reference>();
          this.requester.add(t);
          return t;
        }

        public ActionComponent addRequester(Reference t) { //3
          if (t == null)
            return this;
          if (this.requester == null)
            this.requester = new ArrayList<Reference>();
          this.requester.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #requester}, creating it if it does not already exist
         */
        public Reference getRequesterFirstRep() { 
          if (getRequester().isEmpty()) {
            addRequester();
          }
          return getRequester().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getRequesterTarget() { 
          if (this.requesterTarget == null)
            this.requesterTarget = new ArrayList<Resource>();
          return this.requesterTarget;
        }

        /**
         * @return {@link #requesterLinkId} (Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.)
         */
        public List<StringType> getRequesterLinkId() { 
          if (this.requesterLinkId == null)
            this.requesterLinkId = new ArrayList<StringType>();
          return this.requesterLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setRequesterLinkId(List<StringType> theRequesterLinkId) { 
          this.requesterLinkId = theRequesterLinkId;
          return this;
        }

        public boolean hasRequesterLinkId() { 
          if (this.requesterLinkId == null)
            return false;
          for (StringType item : this.requesterLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #requesterLinkId} (Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.)
         */
        public StringType addRequesterLinkIdElement() {//2 
          StringType t = new StringType();
          if (this.requesterLinkId == null)
            this.requesterLinkId = new ArrayList<StringType>();
          this.requesterLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #requesterLinkId} (Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.)
         */
        public ActionComponent addRequesterLinkId(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.requesterLinkId == null)
            this.requesterLinkId = new ArrayList<StringType>();
          this.requesterLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #requesterLinkId} (Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.)
         */
        public boolean hasRequesterLinkId(String value) { 
          if (this.requesterLinkId == null)
            return false;
          for (StringType v : this.requesterLinkId)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #performerType} (The type of individual that is desired or required to perform or not perform the action.)
         */
        public List<CodeableConcept> getPerformerType() { 
          if (this.performerType == null)
            this.performerType = new ArrayList<CodeableConcept>();
          return this.performerType;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setPerformerType(List<CodeableConcept> thePerformerType) { 
          this.performerType = thePerformerType;
          return this;
        }

        public boolean hasPerformerType() { 
          if (this.performerType == null)
            return false;
          for (CodeableConcept item : this.performerType)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addPerformerType() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.performerType == null)
            this.performerType = new ArrayList<CodeableConcept>();
          this.performerType.add(t);
          return t;
        }

        public ActionComponent addPerformerType(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.performerType == null)
            this.performerType = new ArrayList<CodeableConcept>();
          this.performerType.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #performerType}, creating it if it does not already exist
         */
        public CodeableConcept getPerformerTypeFirstRep() { 
          if (getPerformerType().isEmpty()) {
            addPerformerType();
          }
          return getPerformerType().get(0);
        }

        /**
         * @return {@link #performerRole} (The type of role or competency of an individual desired or required to perform or not perform the action.)
         */
        public CodeableConcept getPerformerRole() { 
          if (this.performerRole == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionComponent.performerRole");
            else if (Configuration.doAutoCreate())
              this.performerRole = new CodeableConcept(); // cc
          return this.performerRole;
        }

        public boolean hasPerformerRole() { 
          return this.performerRole != null && !this.performerRole.isEmpty();
        }

        /**
         * @param value {@link #performerRole} (The type of role or competency of an individual desired or required to perform or not perform the action.)
         */
        public ActionComponent setPerformerRole(CodeableConcept value) { 
          this.performerRole = value;
          return this;
        }

        /**
         * @return {@link #performer} (Indicates who or what is being asked to perform (or not perform) the ction.)
         */
        public Reference getPerformer() { 
          if (this.performer == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionComponent.performer");
            else if (Configuration.doAutoCreate())
              this.performer = new Reference(); // cc
          return this.performer;
        }

        public boolean hasPerformer() { 
          return this.performer != null && !this.performer.isEmpty();
        }

        /**
         * @param value {@link #performer} (Indicates who or what is being asked to perform (or not perform) the ction.)
         */
        public ActionComponent setPerformer(Reference value) { 
          this.performer = value;
          return this;
        }

        /**
         * @return {@link #performer} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Indicates who or what is being asked to perform (or not perform) the ction.)
         */
        public Resource getPerformerTarget() { 
          return this.performerTarget;
        }

        /**
         * @param value {@link #performer} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Indicates who or what is being asked to perform (or not perform) the ction.)
         */
        public ActionComponent setPerformerTarget(Resource value) { 
          this.performerTarget = value;
          return this;
        }

        /**
         * @return {@link #performerLinkId} (Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.)
         */
        public List<StringType> getPerformerLinkId() { 
          if (this.performerLinkId == null)
            this.performerLinkId = new ArrayList<StringType>();
          return this.performerLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setPerformerLinkId(List<StringType> thePerformerLinkId) { 
          this.performerLinkId = thePerformerLinkId;
          return this;
        }

        public boolean hasPerformerLinkId() { 
          if (this.performerLinkId == null)
            return false;
          for (StringType item : this.performerLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #performerLinkId} (Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.)
         */
        public StringType addPerformerLinkIdElement() {//2 
          StringType t = new StringType();
          if (this.performerLinkId == null)
            this.performerLinkId = new ArrayList<StringType>();
          this.performerLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #performerLinkId} (Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.)
         */
        public ActionComponent addPerformerLinkId(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.performerLinkId == null)
            this.performerLinkId = new ArrayList<StringType>();
          this.performerLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #performerLinkId} (Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.)
         */
        public boolean hasPerformerLinkId(String value) { 
          if (this.performerLinkId == null)
            return false;
          for (StringType v : this.performerLinkId)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #reasonCode} (Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.)
         */
        public List<CodeableConcept> getReasonCode() { 
          if (this.reasonCode == null)
            this.reasonCode = new ArrayList<CodeableConcept>();
          return this.reasonCode;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setReasonCode(List<CodeableConcept> theReasonCode) { 
          this.reasonCode = theReasonCode;
          return this;
        }

        public boolean hasReasonCode() { 
          if (this.reasonCode == null)
            return false;
          for (CodeableConcept item : this.reasonCode)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public CodeableConcept addReasonCode() { //3
          CodeableConcept t = new CodeableConcept();
          if (this.reasonCode == null)
            this.reasonCode = new ArrayList<CodeableConcept>();
          this.reasonCode.add(t);
          return t;
        }

        public ActionComponent addReasonCode(CodeableConcept t) { //3
          if (t == null)
            return this;
          if (this.reasonCode == null)
            this.reasonCode = new ArrayList<CodeableConcept>();
          this.reasonCode.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #reasonCode}, creating it if it does not already exist
         */
        public CodeableConcept getReasonCodeFirstRep() { 
          if (getReasonCode().isEmpty()) {
            addReasonCode();
          }
          return getReasonCode().get(0);
        }

        /**
         * @return {@link #reasonReference} (Indicates another resource whose existence justifies permitting or not permitting this action.)
         */
        public List<Reference> getReasonReference() { 
          if (this.reasonReference == null)
            this.reasonReference = new ArrayList<Reference>();
          return this.reasonReference;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setReasonReference(List<Reference> theReasonReference) { 
          this.reasonReference = theReasonReference;
          return this;
        }

        public boolean hasReasonReference() { 
          if (this.reasonReference == null)
            return false;
          for (Reference item : this.reasonReference)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addReasonReference() { //3
          Reference t = new Reference();
          if (this.reasonReference == null)
            this.reasonReference = new ArrayList<Reference>();
          this.reasonReference.add(t);
          return t;
        }

        public ActionComponent addReasonReference(Reference t) { //3
          if (t == null)
            return this;
          if (this.reasonReference == null)
            this.reasonReference = new ArrayList<Reference>();
          this.reasonReference.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #reasonReference}, creating it if it does not already exist
         */
        public Reference getReasonReferenceFirstRep() { 
          if (getReasonReference().isEmpty()) {
            addReasonReference();
          }
          return getReasonReference().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getReasonReferenceTarget() { 
          if (this.reasonReferenceTarget == null)
            this.reasonReferenceTarget = new ArrayList<Resource>();
          return this.reasonReferenceTarget;
        }

        /**
         * @return {@link #reason} (Describes why the action is to be performed or not performed in textual form.)
         */
        public List<StringType> getReason() { 
          if (this.reason == null)
            this.reason = new ArrayList<StringType>();
          return this.reason;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setReason(List<StringType> theReason) { 
          this.reason = theReason;
          return this;
        }

        public boolean hasReason() { 
          if (this.reason == null)
            return false;
          for (StringType item : this.reason)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #reason} (Describes why the action is to be performed or not performed in textual form.)
         */
        public StringType addReasonElement() {//2 
          StringType t = new StringType();
          if (this.reason == null)
            this.reason = new ArrayList<StringType>();
          this.reason.add(t);
          return t;
        }

        /**
         * @param value {@link #reason} (Describes why the action is to be performed or not performed in textual form.)
         */
        public ActionComponent addReason(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.reason == null)
            this.reason = new ArrayList<StringType>();
          this.reason.add(t);
          return this;
        }

        /**
         * @param value {@link #reason} (Describes why the action is to be performed or not performed in textual form.)
         */
        public boolean hasReason(String value) { 
          if (this.reason == null)
            return false;
          for (StringType v : this.reason)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #reasonLinkId} (Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.)
         */
        public List<StringType> getReasonLinkId() { 
          if (this.reasonLinkId == null)
            this.reasonLinkId = new ArrayList<StringType>();
          return this.reasonLinkId;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setReasonLinkId(List<StringType> theReasonLinkId) { 
          this.reasonLinkId = theReasonLinkId;
          return this;
        }

        public boolean hasReasonLinkId() { 
          if (this.reasonLinkId == null)
            return false;
          for (StringType item : this.reasonLinkId)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #reasonLinkId} (Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.)
         */
        public StringType addReasonLinkIdElement() {//2 
          StringType t = new StringType();
          if (this.reasonLinkId == null)
            this.reasonLinkId = new ArrayList<StringType>();
          this.reasonLinkId.add(t);
          return t;
        }

        /**
         * @param value {@link #reasonLinkId} (Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.)
         */
        public ActionComponent addReasonLinkId(String value) { //1
          StringType t = new StringType();
          t.setValue(value);
          if (this.reasonLinkId == null)
            this.reasonLinkId = new ArrayList<StringType>();
          this.reasonLinkId.add(t);
          return this;
        }

        /**
         * @param value {@link #reasonLinkId} (Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.)
         */
        public boolean hasReasonLinkId(String value) { 
          if (this.reasonLinkId == null)
            return false;
          for (StringType v : this.reasonLinkId)
            if (v.getValue().equals(value)) // string
              return true;
          return false;
        }

        /**
         * @return {@link #note} (Comments made about the term action made by the requester, performer, subject or other participants.)
         */
        public List<Annotation> getNote() { 
          if (this.note == null)
            this.note = new ArrayList<Annotation>();
          return this.note;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setNote(List<Annotation> theNote) { 
          this.note = theNote;
          return this;
        }

        public boolean hasNote() { 
          if (this.note == null)
            return false;
          for (Annotation item : this.note)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Annotation addNote() { //3
          Annotation t = new Annotation();
          if (this.note == null)
            this.note = new ArrayList<Annotation>();
          this.note.add(t);
          return t;
        }

        public ActionComponent addNote(Annotation t) { //3
          if (t == null)
            return this;
          if (this.note == null)
            this.note = new ArrayList<Annotation>();
          this.note.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #note}, creating it if it does not already exist
         */
        public Annotation getNoteFirstRep() { 
          if (getNote().isEmpty()) {
            addNote();
          }
          return getNote().get(0);
        }

        /**
         * @return {@link #securityLabelNumber} (Security labels that protects the action.)
         */
        public List<UnsignedIntType> getSecurityLabelNumber() { 
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          return this.securityLabelNumber;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionComponent setSecurityLabelNumber(List<UnsignedIntType> theSecurityLabelNumber) { 
          this.securityLabelNumber = theSecurityLabelNumber;
          return this;
        }

        public boolean hasSecurityLabelNumber() { 
          if (this.securityLabelNumber == null)
            return false;
          for (UnsignedIntType item : this.securityLabelNumber)
            if (!item.isEmpty())
              return true;
          return false;
        }

        /**
         * @return {@link #securityLabelNumber} (Security labels that protects the action.)
         */
        public UnsignedIntType addSecurityLabelNumberElement() {//2 
          UnsignedIntType t = new UnsignedIntType();
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          this.securityLabelNumber.add(t);
          return t;
        }

        /**
         * @param value {@link #securityLabelNumber} (Security labels that protects the action.)
         */
        public ActionComponent addSecurityLabelNumber(int value) { //1
          UnsignedIntType t = new UnsignedIntType();
          t.setValue(value);
          if (this.securityLabelNumber == null)
            this.securityLabelNumber = new ArrayList<UnsignedIntType>();
          this.securityLabelNumber.add(t);
          return this;
        }

        /**
         * @param value {@link #securityLabelNumber} (Security labels that protects the action.)
         */
        public boolean hasSecurityLabelNumber(int value) { 
          if (this.securityLabelNumber == null)
            return false;
          for (UnsignedIntType v : this.securityLabelNumber)
            if (v.getValue().equals(value)) // unsignedInt
              return true;
          return false;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("doNotPerform", "boolean", "True if the term prohibits the  action.", 0, 1, doNotPerform));
          children.add(new Property("type", "CodeableConcept", "Activity or service obligation to be done or not done, performed or not performed, effectuated or not by this Contract term.", 0, 1, type));
          children.add(new Property("subject", "", "Entity of the action.", 0, java.lang.Integer.MAX_VALUE, subject));
          children.add(new Property("intent", "CodeableConcept", "Reason or purpose for the action stipulated by this Contract Provision.", 0, 1, intent));
          children.add(new Property("linkId", "string", "Id [identifier??] of the clause or question text related to this action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, linkId));
          children.add(new Property("status", "CodeableConcept", "Current state of the term action.", 0, 1, status));
          children.add(new Property("context", "Reference(Encounter|EpisodeOfCare)", "Encounter or Episode with primary association to specified term activity.", 0, 1, context));
          children.add(new Property("contextLinkId", "string", "Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, contextLinkId));
          children.add(new Property("occurrence[x]", "dateTime|Period|Timing", "When action happens.", 0, 1, occurrence));
          children.add(new Property("requester", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole|Device|Group|Organization)", "Who or what initiated the action and has responsibility for its activation.", 0, java.lang.Integer.MAX_VALUE, requester));
          children.add(new Property("requesterLinkId", "string", "Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, requesterLinkId));
          children.add(new Property("performerType", "CodeableConcept", "The type of individual that is desired or required to perform or not perform the action.", 0, java.lang.Integer.MAX_VALUE, performerType));
          children.add(new Property("performerRole", "CodeableConcept", "The type of role or competency of an individual desired or required to perform or not perform the action.", 0, 1, performerRole));
          children.add(new Property("performer", "Reference(RelatedPerson|Patient|Practitioner|PractitionerRole|CareTeam|Device|Substance|Organization|Location)", "Indicates who or what is being asked to perform (or not perform) the ction.", 0, 1, performer));
          children.add(new Property("performerLinkId", "string", "Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, performerLinkId));
          children.add(new Property("reasonCode", "CodeableConcept", "Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.", 0, java.lang.Integer.MAX_VALUE, reasonCode));
          children.add(new Property("reasonReference", "Reference(Condition|Observation|DiagnosticReport|DocumentReference|Questionnaire|QuestionnaireResponse)", "Indicates another resource whose existence justifies permitting or not permitting this action.", 0, java.lang.Integer.MAX_VALUE, reasonReference));
          children.add(new Property("reason", "string", "Describes why the action is to be performed or not performed in textual form.", 0, java.lang.Integer.MAX_VALUE, reason));
          children.add(new Property("reasonLinkId", "string", "Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, reasonLinkId));
          children.add(new Property("note", "Annotation", "Comments made about the term action made by the requester, performer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note));
          children.add(new Property("securityLabelNumber", "unsignedInt", "Security labels that protects the action.", 0, java.lang.Integer.MAX_VALUE, securityLabelNumber));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -1788508167: /*doNotPerform*/  return new Property("doNotPerform", "boolean", "True if the term prohibits the  action.", 0, 1, doNotPerform);
          case 3575610: /*type*/  return new Property("type", "CodeableConcept", "Activity or service obligation to be done or not done, performed or not performed, effectuated or not by this Contract term.", 0, 1, type);
          case -1867885268: /*subject*/  return new Property("subject", "", "Entity of the action.", 0, java.lang.Integer.MAX_VALUE, subject);
          case -1183762788: /*intent*/  return new Property("intent", "CodeableConcept", "Reason or purpose for the action stipulated by this Contract Provision.", 0, 1, intent);
          case -1102667083: /*linkId*/  return new Property("linkId", "string", "Id [identifier??] of the clause or question text related to this action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, linkId);
          case -892481550: /*status*/  return new Property("status", "CodeableConcept", "Current state of the term action.", 0, 1, status);
          case 951530927: /*context*/  return new Property("context", "Reference(Encounter|EpisodeOfCare)", "Encounter or Episode with primary association to specified term activity.", 0, 1, context);
          case -288783036: /*contextLinkId*/  return new Property("contextLinkId", "string", "Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, contextLinkId);
          case -2022646513: /*occurrence[x]*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When action happens.", 0, 1, occurrence);
          case 1687874001: /*occurrence*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When action happens.", 0, 1, occurrence);
          case -298443636: /*occurrenceDateTime*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When action happens.", 0, 1, occurrence);
          case 1397156594: /*occurrencePeriod*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When action happens.", 0, 1, occurrence);
          case 1515218299: /*occurrenceTiming*/  return new Property("occurrence[x]", "dateTime|Period|Timing", "When action happens.", 0, 1, occurrence);
          case 693933948: /*requester*/  return new Property("requester", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole|Device|Group|Organization)", "Who or what initiated the action and has responsibility for its activation.", 0, java.lang.Integer.MAX_VALUE, requester);
          case -1468032687: /*requesterLinkId*/  return new Property("requesterLinkId", "string", "Id [identifier??] of the clause or question text related to the requester of this action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, requesterLinkId);
          case -901444568: /*performerType*/  return new Property("performerType", "CodeableConcept", "The type of individual that is desired or required to perform or not perform the action.", 0, java.lang.Integer.MAX_VALUE, performerType);
          case -901513884: /*performerRole*/  return new Property("performerRole", "CodeableConcept", "The type of role or competency of an individual desired or required to perform or not perform the action.", 0, 1, performerRole);
          case 481140686: /*performer*/  return new Property("performer", "Reference(RelatedPerson|Patient|Practitioner|PractitionerRole|CareTeam|Device|Substance|Organization|Location)", "Indicates who or what is being asked to perform (or not perform) the ction.", 0, 1, performer);
          case 1051302947: /*performerLinkId*/  return new Property("performerLinkId", "string", "Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, performerLinkId);
          case 722137681: /*reasonCode*/  return new Property("reasonCode", "CodeableConcept", "Rationale for the action to be performed or not performed. Describes why the action is permitted or prohibited.", 0, java.lang.Integer.MAX_VALUE, reasonCode);
          case -1146218137: /*reasonReference*/  return new Property("reasonReference", "Reference(Condition|Observation|DiagnosticReport|DocumentReference|Questionnaire|QuestionnaireResponse)", "Indicates another resource whose existence justifies permitting or not permitting this action.", 0, java.lang.Integer.MAX_VALUE, reasonReference);
          case -934964668: /*reason*/  return new Property("reason", "string", "Describes why the action is to be performed or not performed in textual form.", 0, java.lang.Integer.MAX_VALUE, reason);
          case -1557963239: /*reasonLinkId*/  return new Property("reasonLinkId", "string", "Id [identifier??] of the clause or question text related to the reason type or reference of this  action in the referenced form or QuestionnaireResponse.", 0, java.lang.Integer.MAX_VALUE, reasonLinkId);
          case 3387378: /*note*/  return new Property("note", "Annotation", "Comments made about the term action made by the requester, performer, subject or other participants.", 0, java.lang.Integer.MAX_VALUE, note);
          case -149460995: /*securityLabelNumber*/  return new Property("securityLabelNumber", "unsignedInt", "Security labels that protects the action.", 0, java.lang.Integer.MAX_VALUE, securityLabelNumber);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1788508167: /*doNotPerform*/ return this.doNotPerform == null ? new Base[0] : new Base[] {this.doNotPerform}; // BooleanType
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : this.subject.toArray(new Base[this.subject.size()]); // ActionSubjectComponent
        case -1183762788: /*intent*/ return this.intent == null ? new Base[0] : new Base[] {this.intent}; // CodeableConcept
        case -1102667083: /*linkId*/ return this.linkId == null ? new Base[0] : this.linkId.toArray(new Base[this.linkId.size()]); // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // CodeableConcept
        case 951530927: /*context*/ return this.context == null ? new Base[0] : new Base[] {this.context}; // Reference
        case -288783036: /*contextLinkId*/ return this.contextLinkId == null ? new Base[0] : this.contextLinkId.toArray(new Base[this.contextLinkId.size()]); // StringType
        case 1687874001: /*occurrence*/ return this.occurrence == null ? new Base[0] : new Base[] {this.occurrence}; // Type
        case 693933948: /*requester*/ return this.requester == null ? new Base[0] : this.requester.toArray(new Base[this.requester.size()]); // Reference
        case -1468032687: /*requesterLinkId*/ return this.requesterLinkId == null ? new Base[0] : this.requesterLinkId.toArray(new Base[this.requesterLinkId.size()]); // StringType
        case -901444568: /*performerType*/ return this.performerType == null ? new Base[0] : this.performerType.toArray(new Base[this.performerType.size()]); // CodeableConcept
        case -901513884: /*performerRole*/ return this.performerRole == null ? new Base[0] : new Base[] {this.performerRole}; // CodeableConcept
        case 481140686: /*performer*/ return this.performer == null ? new Base[0] : new Base[] {this.performer}; // Reference
        case 1051302947: /*performerLinkId*/ return this.performerLinkId == null ? new Base[0] : this.performerLinkId.toArray(new Base[this.performerLinkId.size()]); // StringType
        case 722137681: /*reasonCode*/ return this.reasonCode == null ? new Base[0] : this.reasonCode.toArray(new Base[this.reasonCode.size()]); // CodeableConcept
        case -1146218137: /*reasonReference*/ return this.reasonReference == null ? new Base[0] : this.reasonReference.toArray(new Base[this.reasonReference.size()]); // Reference
        case -934964668: /*reason*/ return this.reason == null ? new Base[0] : this.reason.toArray(new Base[this.reason.size()]); // StringType
        case -1557963239: /*reasonLinkId*/ return this.reasonLinkId == null ? new Base[0] : this.reasonLinkId.toArray(new Base[this.reasonLinkId.size()]); // StringType
        case 3387378: /*note*/ return this.note == null ? new Base[0] : this.note.toArray(new Base[this.note.size()]); // Annotation
        case -149460995: /*securityLabelNumber*/ return this.securityLabelNumber == null ? new Base[0] : this.securityLabelNumber.toArray(new Base[this.securityLabelNumber.size()]); // UnsignedIntType
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1788508167: // doNotPerform
          this.doNotPerform = castToBoolean(value); // BooleanType
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.getSubject().add((ActionSubjectComponent) value); // ActionSubjectComponent
          return value;
        case -1183762788: // intent
          this.intent = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1102667083: // linkId
          this.getLinkId().add(castToString(value)); // StringType
          return value;
        case -892481550: // status
          this.status = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 951530927: // context
          this.context = castToReference(value); // Reference
          return value;
        case -288783036: // contextLinkId
          this.getContextLinkId().add(castToString(value)); // StringType
          return value;
        case 1687874001: // occurrence
          this.occurrence = castToType(value); // Type
          return value;
        case 693933948: // requester
          this.getRequester().add(castToReference(value)); // Reference
          return value;
        case -1468032687: // requesterLinkId
          this.getRequesterLinkId().add(castToString(value)); // StringType
          return value;
        case -901444568: // performerType
          this.getPerformerType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -901513884: // performerRole
          this.performerRole = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 481140686: // performer
          this.performer = castToReference(value); // Reference
          return value;
        case 1051302947: // performerLinkId
          this.getPerformerLinkId().add(castToString(value)); // StringType
          return value;
        case 722137681: // reasonCode
          this.getReasonCode().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case -1146218137: // reasonReference
          this.getReasonReference().add(castToReference(value)); // Reference
          return value;
        case -934964668: // reason
          this.getReason().add(castToString(value)); // StringType
          return value;
        case -1557963239: // reasonLinkId
          this.getReasonLinkId().add(castToString(value)); // StringType
          return value;
        case 3387378: // note
          this.getNote().add(castToAnnotation(value)); // Annotation
          return value;
        case -149460995: // securityLabelNumber
          this.getSecurityLabelNumber().add(castToUnsignedInt(value)); // UnsignedIntType
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("doNotPerform")) {
          this.doNotPerform = castToBoolean(value); // BooleanType
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.getSubject().add((ActionSubjectComponent) value);
        } else if (name.equals("intent")) {
          this.intent = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("linkId")) {
          this.getLinkId().add(castToString(value));
        } else if (name.equals("status")) {
          this.status = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("context")) {
          this.context = castToReference(value); // Reference
        } else if (name.equals("contextLinkId")) {
          this.getContextLinkId().add(castToString(value));
        } else if (name.equals("occurrence[x]")) {
          this.occurrence = castToType(value); // Type
        } else if (name.equals("requester")) {
          this.getRequester().add(castToReference(value));
        } else if (name.equals("requesterLinkId")) {
          this.getRequesterLinkId().add(castToString(value));
        } else if (name.equals("performerType")) {
          this.getPerformerType().add(castToCodeableConcept(value));
        } else if (name.equals("performerRole")) {
          this.performerRole = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("performer")) {
          this.performer = castToReference(value); // Reference
        } else if (name.equals("performerLinkId")) {
          this.getPerformerLinkId().add(castToString(value));
        } else if (name.equals("reasonCode")) {
          this.getReasonCode().add(castToCodeableConcept(value));
        } else if (name.equals("reasonReference")) {
          this.getReasonReference().add(castToReference(value));
        } else if (name.equals("reason")) {
          this.getReason().add(castToString(value));
        } else if (name.equals("reasonLinkId")) {
          this.getReasonLinkId().add(castToString(value));
        } else if (name.equals("note")) {
          this.getNote().add(castToAnnotation(value));
        } else if (name.equals("securityLabelNumber")) {
          this.getSecurityLabelNumber().add(castToUnsignedInt(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1788508167:  return getDoNotPerformElement();
        case 3575610:  return getType(); 
        case -1867885268:  return addSubject(); 
        case -1183762788:  return getIntent(); 
        case -1102667083:  return addLinkIdElement();
        case -892481550:  return getStatus(); 
        case 951530927:  return getContext(); 
        case -288783036:  return addContextLinkIdElement();
        case -2022646513:  return getOccurrence(); 
        case 1687874001:  return getOccurrence(); 
        case 693933948:  return addRequester(); 
        case -1468032687:  return addRequesterLinkIdElement();
        case -901444568:  return addPerformerType(); 
        case -901513884:  return getPerformerRole(); 
        case 481140686:  return getPerformer(); 
        case 1051302947:  return addPerformerLinkIdElement();
        case 722137681:  return addReasonCode(); 
        case -1146218137:  return addReasonReference(); 
        case -934964668:  return addReasonElement();
        case -1557963239:  return addReasonLinkIdElement();
        case 3387378:  return addNote(); 
        case -149460995:  return addSecurityLabelNumberElement();
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1788508167: /*doNotPerform*/ return new String[] {"boolean"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {};
        case -1183762788: /*intent*/ return new String[] {"CodeableConcept"};
        case -1102667083: /*linkId*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"CodeableConcept"};
        case 951530927: /*context*/ return new String[] {"Reference"};
        case -288783036: /*contextLinkId*/ return new String[] {"string"};
        case 1687874001: /*occurrence*/ return new String[] {"dateTime", "Period", "Timing"};
        case 693933948: /*requester*/ return new String[] {"Reference"};
        case -1468032687: /*requesterLinkId*/ return new String[] {"string"};
        case -901444568: /*performerType*/ return new String[] {"CodeableConcept"};
        case -901513884: /*performerRole*/ return new String[] {"CodeableConcept"};
        case 481140686: /*performer*/ return new String[] {"Reference"};
        case 1051302947: /*performerLinkId*/ return new String[] {"string"};
        case 722137681: /*reasonCode*/ return new String[] {"CodeableConcept"};
        case -1146218137: /*reasonReference*/ return new String[] {"Reference"};
        case -934964668: /*reason*/ return new String[] {"string"};
        case -1557963239: /*reasonLinkId*/ return new String[] {"string"};
        case 3387378: /*note*/ return new String[] {"Annotation"};
        case -149460995: /*securityLabelNumber*/ return new String[] {"unsignedInt"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("doNotPerform")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.doNotPerform");
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subject")) {
          return addSubject();
        }
        else if (name.equals("intent")) {
          this.intent = new CodeableConcept();
          return this.intent;
        }
        else if (name.equals("linkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.linkId");
        }
        else if (name.equals("status")) {
          this.status = new CodeableConcept();
          return this.status;
        }
        else if (name.equals("context")) {
          this.context = new Reference();
          return this.context;
        }
        else if (name.equals("contextLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.contextLinkId");
        }
        else if (name.equals("occurrenceDateTime")) {
          this.occurrence = new DateTimeType();
          return this.occurrence;
        }
        else if (name.equals("occurrencePeriod")) {
          this.occurrence = new Period();
          return this.occurrence;
        }
        else if (name.equals("occurrenceTiming")) {
          this.occurrence = new Timing();
          return this.occurrence;
        }
        else if (name.equals("requester")) {
          return addRequester();
        }
        else if (name.equals("requesterLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.requesterLinkId");
        }
        else if (name.equals("performerType")) {
          return addPerformerType();
        }
        else if (name.equals("performerRole")) {
          this.performerRole = new CodeableConcept();
          return this.performerRole;
        }
        else if (name.equals("performer")) {
          this.performer = new Reference();
          return this.performer;
        }
        else if (name.equals("performerLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.performerLinkId");
        }
        else if (name.equals("reasonCode")) {
          return addReasonCode();
        }
        else if (name.equals("reasonReference")) {
          return addReasonReference();
        }
        else if (name.equals("reason")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.reason");
        }
        else if (name.equals("reasonLinkId")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.reasonLinkId");
        }
        else if (name.equals("note")) {
          return addNote();
        }
        else if (name.equals("securityLabelNumber")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.securityLabelNumber");
        }
        else
          return super.addChild(name);
      }

      public ActionComponent copy() {
        ActionComponent dst = new ActionComponent();
        copyValues(dst);
        dst.doNotPerform = doNotPerform == null ? null : doNotPerform.copy();
        dst.type = type == null ? null : type.copy();
        if (subject != null) {
          dst.subject = new ArrayList<ActionSubjectComponent>();
          for (ActionSubjectComponent i : subject)
            dst.subject.add(i.copy());
        };
        dst.intent = intent == null ? null : intent.copy();
        if (linkId != null) {
          dst.linkId = new ArrayList<StringType>();
          for (StringType i : linkId)
            dst.linkId.add(i.copy());
        };
        dst.status = status == null ? null : status.copy();
        dst.context = context == null ? null : context.copy();
        if (contextLinkId != null) {
          dst.contextLinkId = new ArrayList<StringType>();
          for (StringType i : contextLinkId)
            dst.contextLinkId.add(i.copy());
        };
        dst.occurrence = occurrence == null ? null : occurrence.copy();
        if (requester != null) {
          dst.requester = new ArrayList<Reference>();
          for (Reference i : requester)
            dst.requester.add(i.copy());
        };
        if (requesterLinkId != null) {
          dst.requesterLinkId = new ArrayList<StringType>();
          for (StringType i : requesterLinkId)
            dst.requesterLinkId.add(i.copy());
        };
        if (performerType != null) {
          dst.performerType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : performerType)
            dst.performerType.add(i.copy());
        };
        dst.performerRole = performerRole == null ? null : performerRole.copy();
        dst.performer = performer == null ? null : performer.copy();
        if (performerLinkId != null) {
          dst.performerLinkId = new ArrayList<StringType>();
          for (StringType i : performerLinkId)
            dst.performerLinkId.add(i.copy());
        };
        if (reasonCode != null) {
          dst.reasonCode = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : reasonCode)
            dst.reasonCode.add(i.copy());
        };
        if (reasonReference != null) {
          dst.reasonReference = new ArrayList<Reference>();
          for (Reference i : reasonReference)
            dst.reasonReference.add(i.copy());
        };
        if (reason != null) {
          dst.reason = new ArrayList<StringType>();
          for (StringType i : reason)
            dst.reason.add(i.copy());
        };
        if (reasonLinkId != null) {
          dst.reasonLinkId = new ArrayList<StringType>();
          for (StringType i : reasonLinkId)
            dst.reasonLinkId.add(i.copy());
        };
        if (note != null) {
          dst.note = new ArrayList<Annotation>();
          for (Annotation i : note)
            dst.note.add(i.copy());
        };
        if (securityLabelNumber != null) {
          dst.securityLabelNumber = new ArrayList<UnsignedIntType>();
          for (UnsignedIntType i : securityLabelNumber)
            dst.securityLabelNumber.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ActionComponent))
          return false;
        ActionComponent o = (ActionComponent) other_;
        return compareDeep(doNotPerform, o.doNotPerform, true) && compareDeep(type, o.type, true) && compareDeep(subject, o.subject, true)
           && compareDeep(intent, o.intent, true) && compareDeep(linkId, o.linkId, true) && compareDeep(status, o.status, true)
           && compareDeep(context, o.context, true) && compareDeep(contextLinkId, o.contextLinkId, true) && compareDeep(occurrence, o.occurrence, true)
           && compareDeep(requester, o.requester, true) && compareDeep(requesterLinkId, o.requesterLinkId, true)
           && compareDeep(performerType, o.performerType, true) && compareDeep(performerRole, o.performerRole, true)
           && compareDeep(performer, o.performer, true) && compareDeep(performerLinkId, o.performerLinkId, true)
           && compareDeep(reasonCode, o.reasonCode, true) && compareDeep(reasonReference, o.reasonReference, true)
           && compareDeep(reason, o.reason, true) && compareDeep(reasonLinkId, o.reasonLinkId, true) && compareDeep(note, o.note, true)
           && compareDeep(securityLabelNumber, o.securityLabelNumber, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ActionComponent))
          return false;
        ActionComponent o = (ActionComponent) other_;
        return compareValues(doNotPerform, o.doNotPerform, true) && compareValues(linkId, o.linkId, true) && compareValues(contextLinkId, o.contextLinkId, true)
           && compareValues(requesterLinkId, o.requesterLinkId, true) && compareValues(performerLinkId, o.performerLinkId, true)
           && compareValues(reason, o.reason, true) && compareValues(reasonLinkId, o.reasonLinkId, true) && compareValues(securityLabelNumber, o.securityLabelNumber, true)
          ;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(doNotPerform, type, subject
          , intent, linkId, status, context, contextLinkId, occurrence, requester, requesterLinkId
          , performerType, performerRole, performer, performerLinkId, reasonCode, reasonReference
          , reason, reasonLinkId, note, securityLabelNumber);
      }

  public String fhirType() {
    return "Contract.term.action";

  }

  }

    @Block()
    public static class ActionSubjectComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * The entity the action is performed or not performed on or for.
         */
        @Child(name = "reference", type = {Patient.class, RelatedPerson.class, Practitioner.class, PractitionerRole.class, Device.class, Group.class, Organization.class}, order=1, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Entity of the action", formalDefinition="The entity the action is performed or not performed on or for." )
        protected List<Reference> reference;
        /**
         * The actual objects that are the target of the reference (The entity the action is performed or not performed on or for.)
         */
        protected List<Resource> referenceTarget;


        /**
         * Role type of agent assigned roles in this Contract.
         */
        @Child(name = "role", type = {CodeableConcept.class}, order=2, min=0, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Role type of the agent", formalDefinition="Role type of agent assigned roles in this Contract." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-actorrole")
        protected CodeableConcept role;

        private static final long serialVersionUID = 128949255L;

    /**
     * Constructor
     */
      public ActionSubjectComponent() {
        super();
      }

        /**
         * @return {@link #reference} (The entity the action is performed or not performed on or for.)
         */
        public List<Reference> getReference() { 
          if (this.reference == null)
            this.reference = new ArrayList<Reference>();
          return this.reference;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public ActionSubjectComponent setReference(List<Reference> theReference) { 
          this.reference = theReference;
          return this;
        }

        public boolean hasReference() { 
          if (this.reference == null)
            return false;
          for (Reference item : this.reference)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Reference addReference() { //3
          Reference t = new Reference();
          if (this.reference == null)
            this.reference = new ArrayList<Reference>();
          this.reference.add(t);
          return t;
        }

        public ActionSubjectComponent addReference(Reference t) { //3
          if (t == null)
            return this;
          if (this.reference == null)
            this.reference = new ArrayList<Reference>();
          this.reference.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #reference}, creating it if it does not already exist
         */
        public Reference getReferenceFirstRep() { 
          if (getReference().isEmpty()) {
            addReference();
          }
          return getReference().get(0);
        }

        /**
         * @deprecated Use Reference#setResource(IBaseResource) instead
         */
        @Deprecated
        public List<Resource> getReferenceTarget() { 
          if (this.referenceTarget == null)
            this.referenceTarget = new ArrayList<Resource>();
          return this.referenceTarget;
        }

        /**
         * @return {@link #role} (Role type of agent assigned roles in this Contract.)
         */
        public CodeableConcept getRole() { 
          if (this.role == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create ActionSubjectComponent.role");
            else if (Configuration.doAutoCreate())
              this.role = new CodeableConcept(); // cc
          return this.role;
        }

        public boolean hasRole() { 
          return this.role != null && !this.role.isEmpty();
        }

        /**
         * @param value {@link #role} (Role type of agent assigned roles in this Contract.)
         */
        public ActionSubjectComponent setRole(CodeableConcept value) { 
          this.role = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("reference", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole|Device|Group|Organization)", "The entity the action is performed or not performed on or for.", 0, java.lang.Integer.MAX_VALUE, reference));
          children.add(new Property("role", "CodeableConcept", "Role type of agent assigned roles in this Contract.", 0, 1, role));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case -925155509: /*reference*/  return new Property("reference", "Reference(Patient|RelatedPerson|Practitioner|PractitionerRole|Device|Group|Organization)", "The entity the action is performed or not performed on or for.", 0, java.lang.Integer.MAX_VALUE, reference);
          case 3506294: /*role*/  return new Property("role", "CodeableConcept", "Role type of agent assigned roles in this Contract.", 0, 1, role);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return this.reference == null ? new Base[0] : this.reference.toArray(new Base[this.reference.size()]); // Reference
        case 3506294: /*role*/ return this.role == null ? new Base[0] : new Base[] {this.role}; // CodeableConcept
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -925155509: // reference
          this.getReference().add(castToReference(value)); // Reference
          return value;
        case 3506294: // role
          this.role = castToCodeableConcept(value); // CodeableConcept
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("reference")) {
          this.getReference().add(castToReference(value));
        } else if (name.equals("role")) {
          this.role = castToCodeableConcept(value); // CodeableConcept
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509:  return addReference(); 
        case 3506294:  return getRole(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -925155509: /*reference*/ return new String[] {"Reference"};
        case 3506294: /*role*/ return new String[] {"CodeableConcept"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("reference")) {
          return addReference();
        }
        else if (name.equals("role")) {
          this.role = new CodeableConcept();
          return this.role;
        }
        else
          return super.addChild(name);
      }

      public ActionSubjectComponent copy() {
        ActionSubjectComponent dst = new ActionSubjectComponent();
        copyValues(dst);
        if (reference != null) {
          dst.reference = new ArrayList<Reference>();
          for (Reference i : reference)
            dst.reference.add(i.copy());
        };
        dst.role = role == null ? null : role.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ActionSubjectComponent))
          return false;
        ActionSubjectComponent o = (ActionSubjectComponent) other_;
        return compareDeep(reference, o.reference, true) && compareDeep(role, o.role, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ActionSubjectComponent))
          return false;
        ActionSubjectComponent o = (ActionSubjectComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(reference, role);
      }

  public String fhirType() {
    return "Contract.term.action.subject";

  }

  }

    @Block()
    public static class SignatoryComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Role of this Contract signer, e.g. notary, grantee.
         */
        @Child(name = "type", type = {Coding.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Signatory Role", formalDefinition="Role of this Contract signer, e.g. notary, grantee." )
        @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-signer-type")
        protected Coding type;

        /**
         * Party which is a signator to this Contract.
         */
        @Child(name = "party", type = {Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class}, order=2, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Signatory Party", formalDefinition="Party which is a signator to this Contract." )
        protected Reference party;

        /**
         * The actual object that is the target of the reference (Party which is a signator to this Contract.)
         */
        protected Resource partyTarget;

        /**
         * Legally binding Contract DSIG signature contents in Base64.
         */
        @Child(name = "signature", type = {Signature.class}, order=3, min=1, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
        @Description(shortDefinition="Contract Documentation Signature", formalDefinition="Legally binding Contract DSIG signature contents in Base64." )
        protected List<Signature> signature;

        private static final long serialVersionUID = 1948139228L;

    /**
     * Constructor
     */
      public SignatoryComponent() {
        super();
      }

    /**
     * Constructor
     */
      public SignatoryComponent(Coding type, Reference party) {
        super();
        this.type = type;
        this.party = party;
      }

        /**
         * @return {@link #type} (Role of this Contract signer, e.g. notary, grantee.)
         */
        public Coding getType() { 
          if (this.type == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SignatoryComponent.type");
            else if (Configuration.doAutoCreate())
              this.type = new Coding(); // cc
          return this.type;
        }

        public boolean hasType() { 
          return this.type != null && !this.type.isEmpty();
        }

        /**
         * @param value {@link #type} (Role of this Contract signer, e.g. notary, grantee.)
         */
        public SignatoryComponent setType(Coding value) { 
          this.type = value;
          return this;
        }

        /**
         * @return {@link #party} (Party which is a signator to this Contract.)
         */
        public Reference getParty() { 
          if (this.party == null)
            if (Configuration.errorOnAutoCreate())
              throw new Error("Attempt to auto-create SignatoryComponent.party");
            else if (Configuration.doAutoCreate())
              this.party = new Reference(); // cc
          return this.party;
        }

        public boolean hasParty() { 
          return this.party != null && !this.party.isEmpty();
        }

        /**
         * @param value {@link #party} (Party which is a signator to this Contract.)
         */
        public SignatoryComponent setParty(Reference value) { 
          this.party = value;
          return this;
        }

        /**
         * @return {@link #party} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (Party which is a signator to this Contract.)
         */
        public Resource getPartyTarget() { 
          return this.partyTarget;
        }

        /**
         * @param value {@link #party} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (Party which is a signator to this Contract.)
         */
        public SignatoryComponent setPartyTarget(Resource value) { 
          this.partyTarget = value;
          return this;
        }

        /**
         * @return {@link #signature} (Legally binding Contract DSIG signature contents in Base64.)
         */
        public List<Signature> getSignature() { 
          if (this.signature == null)
            this.signature = new ArrayList<Signature>();
          return this.signature;
        }

        /**
         * @return Returns a reference to <code>this</code> for easy method chaining
         */
        public SignatoryComponent setSignature(List<Signature> theSignature) { 
          this.signature = theSignature;
          return this;
        }

        public boolean hasSignature() { 
          if (this.signature == null)
            return false;
          for (Signature item : this.signature)
            if (!item.isEmpty())
              return true;
          return false;
        }

        public Signature addSignature() { //3
          Signature t = new Signature();
          if (this.signature == null)
            this.signature = new ArrayList<Signature>();
          this.signature.add(t);
          return t;
        }

        public SignatoryComponent addSignature(Signature t) { //3
          if (t == null)
            return this;
          if (this.signature == null)
            this.signature = new ArrayList<Signature>();
          this.signature.add(t);
          return this;
        }

        /**
         * @return The first repetition of repeating field {@link #signature}, creating it if it does not already exist
         */
        public Signature getSignatureFirstRep() { 
          if (getSignature().isEmpty()) {
            addSignature();
          }
          return getSignature().get(0);
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("type", "Coding", "Role of this Contract signer, e.g. notary, grantee.", 0, 1, type));
          children.add(new Property("party", "Reference(Organization|Patient|Practitioner|PractitionerRole|RelatedPerson)", "Party which is a signator to this Contract.", 0, 1, party));
          children.add(new Property("signature", "Signature", "Legally binding Contract DSIG signature contents in Base64.", 0, java.lang.Integer.MAX_VALUE, signature));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 3575610: /*type*/  return new Property("type", "Coding", "Role of this Contract signer, e.g. notary, grantee.", 0, 1, type);
          case 106437350: /*party*/  return new Property("party", "Reference(Organization|Patient|Practitioner|PractitionerRole|RelatedPerson)", "Party which is a signator to this Contract.", 0, 1, party);
          case 1073584312: /*signature*/  return new Property("signature", "Signature", "Legally binding Contract DSIG signature contents in Base64.", 0, java.lang.Integer.MAX_VALUE, signature);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // Coding
        case 106437350: /*party*/ return this.party == null ? new Base[0] : new Base[] {this.party}; // Reference
        case 1073584312: /*signature*/ return this.signature == null ? new Base[0] : this.signature.toArray(new Base[this.signature.size()]); // Signature
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 3575610: // type
          this.type = castToCoding(value); // Coding
          return value;
        case 106437350: // party
          this.party = castToReference(value); // Reference
          return value;
        case 1073584312: // signature
          this.getSignature().add(castToSignature(value)); // Signature
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("type")) {
          this.type = castToCoding(value); // Coding
        } else if (name.equals("party")) {
          this.party = castToReference(value); // Reference
        } else if (name.equals("signature")) {
          this.getSignature().add(castToSignature(value));
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610:  return getType(); 
        case 106437350:  return getParty(); 
        case 1073584312:  return addSignature(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 3575610: /*type*/ return new String[] {"Coding"};
        case 106437350: /*party*/ return new String[] {"Reference"};
        case 1073584312: /*signature*/ return new String[] {"Signature"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("type")) {
          this.type = new Coding();
          return this.type;
        }
        else if (name.equals("party")) {
          this.party = new Reference();
          return this.party;
        }
        else if (name.equals("signature")) {
          return addSignature();
        }
        else
          return super.addChild(name);
      }

      public SignatoryComponent copy() {
        SignatoryComponent dst = new SignatoryComponent();
        copyValues(dst);
        dst.type = type == null ? null : type.copy();
        dst.party = party == null ? null : party.copy();
        if (signature != null) {
          dst.signature = new ArrayList<Signature>();
          for (Signature i : signature)
            dst.signature.add(i.copy());
        };
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof SignatoryComponent))
          return false;
        SignatoryComponent o = (SignatoryComponent) other_;
        return compareDeep(type, o.type, true) && compareDeep(party, o.party, true) && compareDeep(signature, o.signature, true)
          ;
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof SignatoryComponent))
          return false;
        SignatoryComponent o = (SignatoryComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(type, party, signature);
      }

  public String fhirType() {
    return "Contract.signer";

  }

  }

    @Block()
    public static class FriendlyLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.
         */
        @Child(name = "content", type = {Attachment.class, Composition.class, DocumentReference.class, QuestionnaireResponse.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Easily comprehended representation of this Contract", formalDefinition="Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public FriendlyLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public FriendlyLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (this.content == null)
            this.content = new Attachment();
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this != null && this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public Reference getContentReference() throws FHIRException { 
          if (this.content == null)
            this.content = new Reference();
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this != null && this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.)
         */
        public FriendlyLanguageComponent setContent(Type value) { 
          if (value != null && !(value instanceof Attachment || value instanceof Reference))
            throw new Error("Not the right type for Contract.friendly.content[x]: "+value.fhirType());
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 264548711: /*content[x]*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content);
          case 951530617: /*content*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content);
          case -702028164: /*contentAttachment*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content);
          case 1193747154: /*contentReference*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Human readable rendering of this Contract in a format and representation intended to enhance comprehension and ensure understandability.", 0, 1, content);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530617: // content
          this.content = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]")) {
          this.content = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 264548711:  return getContent(); 
        case 951530617:  return getContent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return new String[] {"Attachment", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public FriendlyLanguageComponent copy() {
        FriendlyLanguageComponent dst = new FriendlyLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof FriendlyLanguageComponent))
          return false;
        FriendlyLanguageComponent o = (FriendlyLanguageComponent) other_;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof FriendlyLanguageComponent))
          return false;
        FriendlyLanguageComponent o = (FriendlyLanguageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(content);
      }

  public String fhirType() {
    return "Contract.friendly";

  }

  }

    @Block()
    public static class LegalLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Contract legal text in human renderable form.
         */
        @Child(name = "content", type = {Attachment.class, Composition.class, DocumentReference.class, QuestionnaireResponse.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Contract Legal Text", formalDefinition="Contract legal text in human renderable form." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public LegalLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public LegalLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Contract legal text in human renderable form.)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Contract legal text in human renderable form.)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (this.content == null)
            this.content = new Attachment();
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this != null && this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Contract legal text in human renderable form.)
         */
        public Reference getContentReference() throws FHIRException { 
          if (this.content == null)
            this.content = new Reference();
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this != null && this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Contract legal text in human renderable form.)
         */
        public LegalLanguageComponent setContent(Type value) { 
          if (value != null && !(value instanceof Attachment || value instanceof Reference))
            throw new Error("Not the right type for Contract.legal.content[x]: "+value.fhirType());
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 264548711: /*content[x]*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content);
          case 951530617: /*content*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content);
          case -702028164: /*contentAttachment*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content);
          case 1193747154: /*contentReference*/  return new Property("content[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse)", "Contract legal text in human renderable form.", 0, 1, content);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530617: // content
          this.content = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]")) {
          this.content = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 264548711:  return getContent(); 
        case 951530617:  return getContent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return new String[] {"Attachment", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public LegalLanguageComponent copy() {
        LegalLanguageComponent dst = new LegalLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof LegalLanguageComponent))
          return false;
        LegalLanguageComponent o = (LegalLanguageComponent) other_;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof LegalLanguageComponent))
          return false;
        LegalLanguageComponent o = (LegalLanguageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(content);
      }

  public String fhirType() {
    return "Contract.legal";

  }

  }

    @Block()
    public static class ComputableLanguageComponent extends BackboneElement implements IBaseBackboneElement {
        /**
         * Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).
         */
        @Child(name = "content", type = {Attachment.class, DocumentReference.class}, order=1, min=1, max=1, modifier=false, summary=false)
        @Description(shortDefinition="Computable Contract Rules", formalDefinition="Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal)." )
        protected Type content;

        private static final long serialVersionUID = -1763459053L;

    /**
     * Constructor
     */
      public ComputableLanguageComponent() {
        super();
      }

    /**
     * Constructor
     */
      public ComputableLanguageComponent(Type content) {
        super();
        this.content = content;
      }

        /**
         * @return {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public Type getContent() { 
          return this.content;
        }

        /**
         * @return {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public Attachment getContentAttachment() throws FHIRException { 
          if (this.content == null)
            this.content = new Attachment();
          if (!(this.content instanceof Attachment))
            throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Attachment) this.content;
        }

        public boolean hasContentAttachment() { 
          return this != null && this.content instanceof Attachment;
        }

        /**
         * @return {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public Reference getContentReference() throws FHIRException { 
          if (this.content == null)
            this.content = new Reference();
          if (!(this.content instanceof Reference))
            throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.content.getClass().getName()+" was encountered");
          return (Reference) this.content;
        }

        public boolean hasContentReference() { 
          return this != null && this.content instanceof Reference;
        }

        public boolean hasContent() { 
          return this.content != null && !this.content.isEmpty();
        }

        /**
         * @param value {@link #content} (Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).)
         */
        public ComputableLanguageComponent setContent(Type value) { 
          if (value != null && !(value instanceof Attachment || value instanceof Reference))
            throw new Error("Not the right type for Contract.rule.content[x]: "+value.fhirType());
          this.content = value;
          return this;
        }

        protected void listChildren(List<Property> children) {
          super.listChildren(children);
          children.add(new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content));
        }

        @Override
        public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
          switch (_hash) {
          case 264548711: /*content[x]*/  return new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content);
          case 951530617: /*content*/  return new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content);
          case -702028164: /*contentAttachment*/  return new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content);
          case 1193747154: /*contentReference*/  return new Property("content[x]", "Attachment|Reference(DocumentReference)", "Computable Contract conveyed using a policy rule language (e.g. XACML, DKAL, SecPal).", 0, 1, content);
          default: return super.getNamedProperty(_hash, _name, _checkValid);
          }

        }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return this.content == null ? new Base[0] : new Base[] {this.content}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case 951530617: // content
          this.content = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("content[x]")) {
          this.content = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 264548711:  return getContent(); 
        case 951530617:  return getContent(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case 951530617: /*content*/ return new String[] {"Attachment", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("contentAttachment")) {
          this.content = new Attachment();
          return this.content;
        }
        else if (name.equals("contentReference")) {
          this.content = new Reference();
          return this.content;
        }
        else
          return super.addChild(name);
      }

      public ComputableLanguageComponent copy() {
        ComputableLanguageComponent dst = new ComputableLanguageComponent();
        copyValues(dst);
        dst.content = content == null ? null : content.copy();
        return dst;
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof ComputableLanguageComponent))
          return false;
        ComputableLanguageComponent o = (ComputableLanguageComponent) other_;
        return compareDeep(content, o.content, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof ComputableLanguageComponent))
          return false;
        ComputableLanguageComponent o = (ComputableLanguageComponent) other_;
        return true;
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(content);
      }

  public String fhirType() {
    return "Contract.rule";

  }

  }

    /**
     * Unique identifier for this Contract or a derivative that references a Source Contract.
     */
    @Child(name = "identifier", type = {Identifier.class}, order=0, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contract number", formalDefinition="Unique identifier for this Contract or a derivative that references a Source Contract." )
    protected List<Identifier> identifier;

    /**
     * Canonical identifier for this contract, represented as a URI (globally unique).
     */
    @Child(name = "url", type = {UriType.class}, order=1, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Basal definition", formalDefinition="Canonical identifier for this contract, represented as a URI (globally unique)." )
    protected UriType url;

    /**
     * An edition identifier used for business purposes to label business significant variants.
     */
    @Child(name = "version", type = {StringType.class}, order=2, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Business edition", formalDefinition="An edition identifier used for business purposes to label business significant variants." )
    protected StringType version;

    /**
     * The status of the resource instance.
     */
    @Child(name = "status", type = {CodeType.class}, order=3, min=0, max=1, modifier=true, summary=true)
    @Description(shortDefinition="draft | active | suspended | cancelled | completed | entered-in-error | unknown", formalDefinition="The status of the resource instance." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-status")
    protected Enumeration<ContractStatus> status;

    /**
     * Legal states of the formation of a legal instrument, which is a formally executed written document that can be formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual duty, obligation, or right, and therefore evidences that act, process, or agreement.
     */
    @Child(name = "legalState", type = {CodeableConcept.class}, order=4, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Negotiation status", formalDefinition="Legal states of the formation of a legal instrument, which is a formally executed written document that can be formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual duty, obligation, or right, and therefore evidences that act, process, or agreement." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-legalstate")
    protected CodeableConcept legalState;

    /**
     * The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.
     */
    @Child(name = "instantiatesCanonical", type = {Contract.class}, order=5, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Source Contract Definition", formalDefinition="The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract." )
    protected Reference instantiatesCanonical;

    /**
     * The actual object that is the target of the reference (The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.)
     */
    protected Contract instantiatesCanonicalTarget;

    /**
     * The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.
     */
    @Child(name = "instantiatesUri", type = {UriType.class}, order=6, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="External Contract Definition", formalDefinition="The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract." )
    protected UriType instantiatesUri;

    /**
     * The minimal content derived from the basal information source at a specific stage in its lifecycle.
     */
    @Child(name = "contentDerivative", type = {CodeableConcept.class}, order=7, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Content derived from the basal information", formalDefinition="The minimal content derived from the basal information source at a specific stage in its lifecycle." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-content-derivative")
    protected CodeableConcept contentDerivative;

    /**
     * When this  Contract was issued.
     */
    @Child(name = "issued", type = {DateTimeType.class}, order=8, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="When this Contract was issued", formalDefinition="When this  Contract was issued." )
    protected DateTimeType issued;

    /**
     * Relevant time or time-period when this Contract is applicable.
     */
    @Child(name = "applies", type = {Period.class}, order=9, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Effective time", formalDefinition="Relevant time or time-period when this Contract is applicable." )
    protected Period applies;

    /**
     * Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract.
     */
    @Child(name = "expirationType", type = {CodeableConcept.class}, order=10, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Contract cessation cause", formalDefinition="Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-expiration-type")
    protected CodeableConcept expirationType;

    /**
     * The target entity impacted by or of interest to parties to the agreement.
     */
    @Child(name = "subject", type = {Reference.class}, order=11, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Contract Target Entity", formalDefinition="The target entity impacted by or of interest to parties to the agreement." )
    protected List<Reference> subject;
    /**
     * The actual objects that are the target of the reference (The target entity impacted by or of interest to parties to the agreement.)
     */
    protected List<Resource> subjectTarget;


    /**
     * A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.
     */
    @Child(name = "authority", type = {Organization.class}, order=12, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Authority under which this Contract has standing", formalDefinition="A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies." )
    protected List<Reference> authority;
    /**
     * The actual objects that are the target of the reference (A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.)
     */
    protected List<Organization> authorityTarget;


    /**
     * Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.
     */
    @Child(name = "domain", type = {Location.class}, order=13, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="A sphere of control governed by an authoritative jurisdiction, organization, or person", formalDefinition="Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources." )
    protected List<Reference> domain;
    /**
     * The actual objects that are the target of the reference (Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    protected List<Location> domainTarget;


    /**
     * Sites in which the contract is complied with,  exercised, or in force.
     */
    @Child(name = "site", type = {Location.class}, order=14, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Specific Location", formalDefinition="Sites in which the contract is complied with,  exercised, or in force." )
    protected List<Reference> site;
    /**
     * The actual objects that are the target of the reference (Sites in which the contract is complied with,  exercised, or in force.)
     */
    protected List<Location> siteTarget;


    /**
     * A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides additional information about its content. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    @Child(name = "name", type = {StringType.class}, order=15, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Computer friendly designation", formalDefinition="A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides additional information about its content. This name should be usable as an identifier for the module by machine processing applications such as code generation." )
    protected StringType name;

    /**
     * A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.
     */
    @Child(name = "title", type = {StringType.class}, order=16, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Human Friendly name", formalDefinition="A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content." )
    protected StringType title;

    /**
     * An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.
     */
    @Child(name = "subtitle", type = {StringType.class}, order=17, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Subordinate Friendly name", formalDefinition="An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content." )
    protected StringType subtitle;

    /**
     * Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.g., a domain specific contract number related to legislation.
     */
    @Child(name = "alias", type = {StringType.class}, order=18, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Acronym or short name", formalDefinition="Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.g., a domain specific contract number related to legislation." )
    protected List<StringType> alias;

    /**
     * The individual or organization that authored the Contract definition, derivative, or instance in any legal state.
     */
    @Child(name = "author", type = {Patient.class, Practitioner.class, PractitionerRole.class, Organization.class}, order=19, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Source of Contract", formalDefinition="The individual or organization that authored the Contract definition, derivative, or instance in any legal state." )
    protected Reference author;

    /**
     * The actual object that is the target of the reference (The individual or organization that authored the Contract definition, derivative, or instance in any legal state.)
     */
    protected Resource authorTarget;

    /**
     * A selector of legal concerns for this Contract definition, derivative, or instance in any legal state.
     */
    @Child(name = "scope", type = {CodeableConcept.class}, order=20, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Range of Legal Concerns", formalDefinition="A selector of legal concerns for this Contract definition, derivative, or instance in any legal state." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-scope")
    protected CodeableConcept scope;

    /**
     * Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.
     */
    @Child(name = "topic", type = {CodeableConcept.class, Reference.class}, order=21, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Focus of contract interest", formalDefinition="Narrows the range of legal concerns to focus on the achievement of specific contractual objectives." )
    protected Type topic;

    /**
     * A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance in any legal state.  Provides additional information about its content within the context of the Contract's scope to distinguish the kinds of systems that would be interested in the contract.
     */
    @Child(name = "type", type = {CodeableConcept.class}, order=22, min=0, max=1, modifier=false, summary=true)
    @Description(shortDefinition="Legal instrument category", formalDefinition="A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance in any legal state.  Provides additional information about its content within the context of the Contract's scope to distinguish the kinds of systems that would be interested in the contract." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-type")
    protected CodeableConcept type;

    /**
     * Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within the context of the Contract's scope.
     */
    @Child(name = "subType", type = {CodeableConcept.class}, order=23, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=true)
    @Description(shortDefinition="Subtype within the context of type", formalDefinition="Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within the context of the Contract's scope." )
    @ca.uhn.fhir.model.api.annotation.Binding(valueSet="http://hl7.org/fhir/ValueSet/contract-subtype")
    protected List<CodeableConcept> subType;

    /**
     * Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be associated with and transformable into a Contract.
     */
    @Child(name = "contentDefinition", type = {}, order=24, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Contract precursor content", formalDefinition="Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be associated with and transformable into a Contract." )
    protected ContentDefinitionComponent contentDefinition;

    /**
     * One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.
     */
    @Child(name = "term", type = {}, order=25, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Term List", formalDefinition="One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups." )
    protected List<TermComponent> term;

    /**
     * Information that may be needed by/relevant to the performer in their execution of this term action.
     */
    @Child(name = "supportingInfo", type = {Reference.class}, order=26, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Extra Information", formalDefinition="Information that may be needed by/relevant to the performer in their execution of this term action." )
    protected List<Reference> supportingInfo;
    /**
     * The actual objects that are the target of the reference (Information that may be needed by/relevant to the performer in their execution of this term action.)
     */
    protected List<Resource> supportingInfoTarget;


    /**
     * Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract.  The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.html#Provenance.entity.
     */
    @Child(name = "relevantHistory", type = {Provenance.class}, order=27, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Key event in Contract History", formalDefinition="Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract.  The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.html#Provenance.entity." )
    protected List<Reference> relevantHistory;
    /**
     * The actual objects that are the target of the reference (Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract.  The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.html#Provenance.entity.)
     */
    protected List<Provenance> relevantHistoryTarget;


    /**
     * Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness.
     */
    @Child(name = "signer", type = {}, order=28, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Signatory", formalDefinition="Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness." )
    protected List<SignatoryComponent> signer;

    /**
     * The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.
     */
    @Child(name = "friendly", type = {}, order=29, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Friendly Language", formalDefinition="The \"patient friendly language\" versionof the Contract in whole or in parts. \"Patient friendly language\" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement." )
    protected List<FriendlyLanguageComponent> friendly;

    /**
     * List of Legal expressions or representations of this Contract.
     */
    @Child(name = "legal", type = {}, order=30, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Contract Legal Language", formalDefinition="List of Legal expressions or representations of this Contract." )
    protected List<LegalLanguageComponent> legal;

    /**
     * List of Computable Policy Rule Language Representations of this Contract.
     */
    @Child(name = "rule", type = {}, order=31, min=0, max=Child.MAX_UNLIMITED, modifier=false, summary=false)
    @Description(shortDefinition="Computable Contract Language", formalDefinition="List of Computable Policy Rule Language Representations of this Contract." )
    protected List<ComputableLanguageComponent> rule;

    /**
     * Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.
     */
    @Child(name = "legallyBinding", type = {Attachment.class, Composition.class, DocumentReference.class, QuestionnaireResponse.class, Contract.class}, order=32, min=0, max=1, modifier=false, summary=false)
    @Description(shortDefinition="Binding Contract", formalDefinition="Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract." )
    protected Type legallyBinding;

    private static final long serialVersionUID = -1388892487L;

  /**
   * Constructor
   */
    public Contract() {
      super();
    }

    /**
     * @return {@link #identifier} (Unique identifier for this Contract or a derivative that references a Source Contract.)
     */
    public List<Identifier> getIdentifier() { 
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      return this.identifier;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setIdentifier(List<Identifier> theIdentifier) { 
      this.identifier = theIdentifier;
      return this;
    }

    public boolean hasIdentifier() { 
      if (this.identifier == null)
        return false;
      for (Identifier item : this.identifier)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Identifier addIdentifier() { //3
      Identifier t = new Identifier();
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return t;
    }

    public Contract addIdentifier(Identifier t) { //3
      if (t == null)
        return this;
      if (this.identifier == null)
        this.identifier = new ArrayList<Identifier>();
      this.identifier.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #identifier}, creating it if it does not already exist
     */
    public Identifier getIdentifierFirstRep() { 
      if (getIdentifier().isEmpty()) {
        addIdentifier();
      }
      return getIdentifier().get(0);
    }

    /**
     * @return {@link #url} (Canonical identifier for this contract, represented as a URI (globally unique).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public UriType getUrlElement() { 
      if (this.url == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.url");
        else if (Configuration.doAutoCreate())
          this.url = new UriType(); // bb
      return this.url;
    }

    public boolean hasUrlElement() { 
      return this.url != null && !this.url.isEmpty();
    }

    public boolean hasUrl() { 
      return this.url != null && !this.url.isEmpty();
    }

    /**
     * @param value {@link #url} (Canonical identifier for this contract, represented as a URI (globally unique).). This is the underlying object with id, value and extensions. The accessor "getUrl" gives direct access to the value
     */
    public Contract setUrlElement(UriType value) { 
      this.url = value;
      return this;
    }

    /**
     * @return Canonical identifier for this contract, represented as a URI (globally unique).
     */
    public String getUrl() { 
      return this.url == null ? null : this.url.getValue();
    }

    /**
     * @param value Canonical identifier for this contract, represented as a URI (globally unique).
     */
    public Contract setUrl(String value) { 
      if (Utilities.noString(value))
        this.url = null;
      else {
        if (this.url == null)
          this.url = new UriType();
        this.url.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #version} (An edition identifier used for business purposes to label business significant variants.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public StringType getVersionElement() { 
      if (this.version == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.version");
        else if (Configuration.doAutoCreate())
          this.version = new StringType(); // bb
      return this.version;
    }

    public boolean hasVersionElement() { 
      return this.version != null && !this.version.isEmpty();
    }

    public boolean hasVersion() { 
      return this.version != null && !this.version.isEmpty();
    }

    /**
     * @param value {@link #version} (An edition identifier used for business purposes to label business significant variants.). This is the underlying object with id, value and extensions. The accessor "getVersion" gives direct access to the value
     */
    public Contract setVersionElement(StringType value) { 
      this.version = value;
      return this;
    }

    /**
     * @return An edition identifier used for business purposes to label business significant variants.
     */
    public String getVersion() { 
      return this.version == null ? null : this.version.getValue();
    }

    /**
     * @param value An edition identifier used for business purposes to label business significant variants.
     */
    public Contract setVersion(String value) { 
      if (Utilities.noString(value))
        this.version = null;
      else {
        if (this.version == null)
          this.version = new StringType();
        this.version.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Enumeration<ContractStatus> getStatusElement() { 
      if (this.status == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.status");
        else if (Configuration.doAutoCreate())
          this.status = new Enumeration<ContractStatus>(new ContractStatusEnumFactory()); // bb
      return this.status;
    }

    public boolean hasStatusElement() { 
      return this.status != null && !this.status.isEmpty();
    }

    public boolean hasStatus() { 
      return this.status != null && !this.status.isEmpty();
    }

    /**
     * @param value {@link #status} (The status of the resource instance.). This is the underlying object with id, value and extensions. The accessor "getStatus" gives direct access to the value
     */
    public Contract setStatusElement(Enumeration<ContractStatus> value) { 
      this.status = value;
      return this;
    }

    /**
     * @return The status of the resource instance.
     */
    public ContractStatus getStatus() { 
      return this.status == null ? null : this.status.getValue();
    }

    /**
     * @param value The status of the resource instance.
     */
    public Contract setStatus(ContractStatus value) { 
      if (value == null)
        this.status = null;
      else {
        if (this.status == null)
          this.status = new Enumeration<ContractStatus>(new ContractStatusEnumFactory());
        this.status.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #legalState} (Legal states of the formation of a legal instrument, which is a formally executed written document that can be formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual duty, obligation, or right, and therefore evidences that act, process, or agreement.)
     */
    public CodeableConcept getLegalState() { 
      if (this.legalState == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.legalState");
        else if (Configuration.doAutoCreate())
          this.legalState = new CodeableConcept(); // cc
      return this.legalState;
    }

    public boolean hasLegalState() { 
      return this.legalState != null && !this.legalState.isEmpty();
    }

    /**
     * @param value {@link #legalState} (Legal states of the formation of a legal instrument, which is a formally executed written document that can be formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual duty, obligation, or right, and therefore evidences that act, process, or agreement.)
     */
    public Contract setLegalState(CodeableConcept value) { 
      this.legalState = value;
      return this;
    }

    /**
     * @return {@link #instantiatesCanonical} (The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.)
     */
    public Reference getInstantiatesCanonical() { 
      if (this.instantiatesCanonical == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.instantiatesCanonical");
        else if (Configuration.doAutoCreate())
          this.instantiatesCanonical = new Reference(); // cc
      return this.instantiatesCanonical;
    }

    public boolean hasInstantiatesCanonical() { 
      return this.instantiatesCanonical != null && !this.instantiatesCanonical.isEmpty();
    }

    /**
     * @param value {@link #instantiatesCanonical} (The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.)
     */
    public Contract setInstantiatesCanonical(Reference value) { 
      this.instantiatesCanonical = value;
      return this;
    }

    /**
     * @return {@link #instantiatesCanonical} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.)
     */
    public Contract getInstantiatesCanonicalTarget() { 
      if (this.instantiatesCanonicalTarget == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.instantiatesCanonical");
        else if (Configuration.doAutoCreate())
          this.instantiatesCanonicalTarget = new Contract(); // aa
      return this.instantiatesCanonicalTarget;
    }

    /**
     * @param value {@link #instantiatesCanonical} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.)
     */
    public Contract setInstantiatesCanonicalTarget(Contract value) { 
      this.instantiatesCanonicalTarget = value;
      return this;
    }

    /**
     * @return {@link #instantiatesUri} (The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.). This is the underlying object with id, value and extensions. The accessor "getInstantiatesUri" gives direct access to the value
     */
    public UriType getInstantiatesUriElement() { 
      if (this.instantiatesUri == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.instantiatesUri");
        else if (Configuration.doAutoCreate())
          this.instantiatesUri = new UriType(); // bb
      return this.instantiatesUri;
    }

    public boolean hasInstantiatesUriElement() { 
      return this.instantiatesUri != null && !this.instantiatesUri.isEmpty();
    }

    public boolean hasInstantiatesUri() { 
      return this.instantiatesUri != null && !this.instantiatesUri.isEmpty();
    }

    /**
     * @param value {@link #instantiatesUri} (The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.). This is the underlying object with id, value and extensions. The accessor "getInstantiatesUri" gives direct access to the value
     */
    public Contract setInstantiatesUriElement(UriType value) { 
      this.instantiatesUri = value;
      return this;
    }

    /**
     * @return The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.
     */
    public String getInstantiatesUri() { 
      return this.instantiatesUri == null ? null : this.instantiatesUri.getValue();
    }

    /**
     * @param value The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.
     */
    public Contract setInstantiatesUri(String value) { 
      if (Utilities.noString(value))
        this.instantiatesUri = null;
      else {
        if (this.instantiatesUri == null)
          this.instantiatesUri = new UriType();
        this.instantiatesUri.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #contentDerivative} (The minimal content derived from the basal information source at a specific stage in its lifecycle.)
     */
    public CodeableConcept getContentDerivative() { 
      if (this.contentDerivative == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.contentDerivative");
        else if (Configuration.doAutoCreate())
          this.contentDerivative = new CodeableConcept(); // cc
      return this.contentDerivative;
    }

    public boolean hasContentDerivative() { 
      return this.contentDerivative != null && !this.contentDerivative.isEmpty();
    }

    /**
     * @param value {@link #contentDerivative} (The minimal content derived from the basal information source at a specific stage in its lifecycle.)
     */
    public Contract setContentDerivative(CodeableConcept value) { 
      this.contentDerivative = value;
      return this;
    }

    /**
     * @return {@link #issued} (When this  Contract was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public DateTimeType getIssuedElement() { 
      if (this.issued == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.issued");
        else if (Configuration.doAutoCreate())
          this.issued = new DateTimeType(); // bb
      return this.issued;
    }

    public boolean hasIssuedElement() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    public boolean hasIssued() { 
      return this.issued != null && !this.issued.isEmpty();
    }

    /**
     * @param value {@link #issued} (When this  Contract was issued.). This is the underlying object with id, value and extensions. The accessor "getIssued" gives direct access to the value
     */
    public Contract setIssuedElement(DateTimeType value) { 
      this.issued = value;
      return this;
    }

    /**
     * @return When this  Contract was issued.
     */
    public Date getIssued() { 
      return this.issued == null ? null : this.issued.getValue();
    }

    /**
     * @param value When this  Contract was issued.
     */
    public Contract setIssued(Date value) { 
      if (value == null)
        this.issued = null;
      else {
        if (this.issued == null)
          this.issued = new DateTimeType();
        this.issued.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #applies} (Relevant time or time-period when this Contract is applicable.)
     */
    public Period getApplies() { 
      if (this.applies == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.applies");
        else if (Configuration.doAutoCreate())
          this.applies = new Period(); // cc
      return this.applies;
    }

    public boolean hasApplies() { 
      return this.applies != null && !this.applies.isEmpty();
    }

    /**
     * @param value {@link #applies} (Relevant time or time-period when this Contract is applicable.)
     */
    public Contract setApplies(Period value) { 
      this.applies = value;
      return this;
    }

    /**
     * @return {@link #expirationType} (Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract.)
     */
    public CodeableConcept getExpirationType() { 
      if (this.expirationType == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.expirationType");
        else if (Configuration.doAutoCreate())
          this.expirationType = new CodeableConcept(); // cc
      return this.expirationType;
    }

    public boolean hasExpirationType() { 
      return this.expirationType != null && !this.expirationType.isEmpty();
    }

    /**
     * @param value {@link #expirationType} (Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract.)
     */
    public Contract setExpirationType(CodeableConcept value) { 
      this.expirationType = value;
      return this;
    }

    /**
     * @return {@link #subject} (The target entity impacted by or of interest to parties to the agreement.)
     */
    public List<Reference> getSubject() { 
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      return this.subject;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setSubject(List<Reference> theSubject) { 
      this.subject = theSubject;
      return this;
    }

    public boolean hasSubject() { 
      if (this.subject == null)
        return false;
      for (Reference item : this.subject)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSubject() { //3
      Reference t = new Reference();
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return t;
    }

    public Contract addSubject(Reference t) { //3
      if (t == null)
        return this;
      if (this.subject == null)
        this.subject = new ArrayList<Reference>();
      this.subject.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subject}, creating it if it does not already exist
     */
    public Reference getSubjectFirstRep() { 
      if (getSubject().isEmpty()) {
        addSubject();
      }
      return getSubject().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSubjectTarget() { 
      if (this.subjectTarget == null)
        this.subjectTarget = new ArrayList<Resource>();
      return this.subjectTarget;
    }

    /**
     * @return {@link #authority} (A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.)
     */
    public List<Reference> getAuthority() { 
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      return this.authority;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setAuthority(List<Reference> theAuthority) { 
      this.authority = theAuthority;
      return this;
    }

    public boolean hasAuthority() { 
      if (this.authority == null)
        return false;
      for (Reference item : this.authority)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addAuthority() { //3
      Reference t = new Reference();
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      this.authority.add(t);
      return t;
    }

    public Contract addAuthority(Reference t) { //3
      if (t == null)
        return this;
      if (this.authority == null)
        this.authority = new ArrayList<Reference>();
      this.authority.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #authority}, creating it if it does not already exist
     */
    public Reference getAuthorityFirstRep() { 
      if (getAuthority().isEmpty()) {
        addAuthority();
      }
      return getAuthority().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Organization> getAuthorityTarget() { 
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      return this.authorityTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Organization addAuthorityTarget() { 
      Organization r = new Organization();
      if (this.authorityTarget == null)
        this.authorityTarget = new ArrayList<Organization>();
      this.authorityTarget.add(r);
      return r;
    }

    /**
     * @return {@link #domain} (Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.)
     */
    public List<Reference> getDomain() { 
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      return this.domain;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setDomain(List<Reference> theDomain) { 
      this.domain = theDomain;
      return this;
    }

    public boolean hasDomain() { 
      if (this.domain == null)
        return false;
      for (Reference item : this.domain)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addDomain() { //3
      Reference t = new Reference();
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      this.domain.add(t);
      return t;
    }

    public Contract addDomain(Reference t) { //3
      if (t == null)
        return this;
      if (this.domain == null)
        this.domain = new ArrayList<Reference>();
      this.domain.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #domain}, creating it if it does not already exist
     */
    public Reference getDomainFirstRep() { 
      if (getDomain().isEmpty()) {
        addDomain();
      }
      return getDomain().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Location> getDomainTarget() { 
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      return this.domainTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Location addDomainTarget() { 
      Location r = new Location();
      if (this.domainTarget == null)
        this.domainTarget = new ArrayList<Location>();
      this.domainTarget.add(r);
      return r;
    }

    /**
     * @return {@link #site} (Sites in which the contract is complied with,  exercised, or in force.)
     */
    public List<Reference> getSite() { 
      if (this.site == null)
        this.site = new ArrayList<Reference>();
      return this.site;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setSite(List<Reference> theSite) { 
      this.site = theSite;
      return this;
    }

    public boolean hasSite() { 
      if (this.site == null)
        return false;
      for (Reference item : this.site)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSite() { //3
      Reference t = new Reference();
      if (this.site == null)
        this.site = new ArrayList<Reference>();
      this.site.add(t);
      return t;
    }

    public Contract addSite(Reference t) { //3
      if (t == null)
        return this;
      if (this.site == null)
        this.site = new ArrayList<Reference>();
      this.site.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #site}, creating it if it does not already exist
     */
    public Reference getSiteFirstRep() { 
      if (getSite().isEmpty()) {
        addSite();
      }
      return getSite().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Location> getSiteTarget() { 
      if (this.siteTarget == null)
        this.siteTarget = new ArrayList<Location>();
      return this.siteTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Location addSiteTarget() { 
      Location r = new Location();
      if (this.siteTarget == null)
        this.siteTarget = new ArrayList<Location>();
      this.siteTarget.add(r);
      return r;
    }

    /**
     * @return {@link #name} (A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides additional information about its content. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public StringType getNameElement() { 
      if (this.name == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.name");
        else if (Configuration.doAutoCreate())
          this.name = new StringType(); // bb
      return this.name;
    }

    public boolean hasNameElement() { 
      return this.name != null && !this.name.isEmpty();
    }

    public boolean hasName() { 
      return this.name != null && !this.name.isEmpty();
    }

    /**
     * @param value {@link #name} (A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides additional information about its content. This name should be usable as an identifier for the module by machine processing applications such as code generation.). This is the underlying object with id, value and extensions. The accessor "getName" gives direct access to the value
     */
    public Contract setNameElement(StringType value) { 
      this.name = value;
      return this;
    }

    /**
     * @return A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides additional information about its content. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public String getName() { 
      return this.name == null ? null : this.name.getValue();
    }

    /**
     * @param value A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides additional information about its content. This name should be usable as an identifier for the module by machine processing applications such as code generation.
     */
    public Contract setName(String value) { 
      if (Utilities.noString(value))
        this.name = null;
      else {
        if (this.name == null)
          this.name = new StringType();
        this.name.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #title} (A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public StringType getTitleElement() { 
      if (this.title == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.title");
        else if (Configuration.doAutoCreate())
          this.title = new StringType(); // bb
      return this.title;
    }

    public boolean hasTitleElement() { 
      return this.title != null && !this.title.isEmpty();
    }

    public boolean hasTitle() { 
      return this.title != null && !this.title.isEmpty();
    }

    /**
     * @param value {@link #title} (A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.). This is the underlying object with id, value and extensions. The accessor "getTitle" gives direct access to the value
     */
    public Contract setTitleElement(StringType value) { 
      this.title = value;
      return this;
    }

    /**
     * @return A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.
     */
    public String getTitle() { 
      return this.title == null ? null : this.title.getValue();
    }

    /**
     * @param value A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.
     */
    public Contract setTitle(String value) { 
      if (Utilities.noString(value))
        this.title = null;
      else {
        if (this.title == null)
          this.title = new StringType();
        this.title.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #subtitle} (An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.). This is the underlying object with id, value and extensions. The accessor "getSubtitle" gives direct access to the value
     */
    public StringType getSubtitleElement() { 
      if (this.subtitle == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.subtitle");
        else if (Configuration.doAutoCreate())
          this.subtitle = new StringType(); // bb
      return this.subtitle;
    }

    public boolean hasSubtitleElement() { 
      return this.subtitle != null && !this.subtitle.isEmpty();
    }

    public boolean hasSubtitle() { 
      return this.subtitle != null && !this.subtitle.isEmpty();
    }

    /**
     * @param value {@link #subtitle} (An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.). This is the underlying object with id, value and extensions. The accessor "getSubtitle" gives direct access to the value
     */
    public Contract setSubtitleElement(StringType value) { 
      this.subtitle = value;
      return this;
    }

    /**
     * @return An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.
     */
    public String getSubtitle() { 
      return this.subtitle == null ? null : this.subtitle.getValue();
    }

    /**
     * @param value An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.
     */
    public Contract setSubtitle(String value) { 
      if (Utilities.noString(value))
        this.subtitle = null;
      else {
        if (this.subtitle == null)
          this.subtitle = new StringType();
        this.subtitle.setValue(value);
      }
      return this;
    }

    /**
     * @return {@link #alias} (Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.g., a domain specific contract number related to legislation.)
     */
    public List<StringType> getAlias() { 
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      return this.alias;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setAlias(List<StringType> theAlias) { 
      this.alias = theAlias;
      return this;
    }

    public boolean hasAlias() { 
      if (this.alias == null)
        return false;
      for (StringType item : this.alias)
        if (!item.isEmpty())
          return true;
      return false;
    }

    /**
     * @return {@link #alias} (Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.g., a domain specific contract number related to legislation.)
     */
    public StringType addAliasElement() {//2 
      StringType t = new StringType();
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return t;
    }

    /**
     * @param value {@link #alias} (Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.g., a domain specific contract number related to legislation.)
     */
    public Contract addAlias(String value) { //1
      StringType t = new StringType();
      t.setValue(value);
      if (this.alias == null)
        this.alias = new ArrayList<StringType>();
      this.alias.add(t);
      return this;
    }

    /**
     * @param value {@link #alias} (Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.g., a domain specific contract number related to legislation.)
     */
    public boolean hasAlias(String value) { 
      if (this.alias == null)
        return false;
      for (StringType v : this.alias)
        if (v.getValue().equals(value)) // string
          return true;
      return false;
    }

    /**
     * @return {@link #author} (The individual or organization that authored the Contract definition, derivative, or instance in any legal state.)
     */
    public Reference getAuthor() { 
      if (this.author == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.author");
        else if (Configuration.doAutoCreate())
          this.author = new Reference(); // cc
      return this.author;
    }

    public boolean hasAuthor() { 
      return this.author != null && !this.author.isEmpty();
    }

    /**
     * @param value {@link #author} (The individual or organization that authored the Contract definition, derivative, or instance in any legal state.)
     */
    public Contract setAuthor(Reference value) { 
      this.author = value;
      return this;
    }

    /**
     * @return {@link #author} The actual object that is the target of the reference. The reference library doesn't populate this, but you can use it to hold the resource if you resolve it. (The individual or organization that authored the Contract definition, derivative, or instance in any legal state.)
     */
    public Resource getAuthorTarget() { 
      return this.authorTarget;
    }

    /**
     * @param value {@link #author} The actual object that is the target of the reference. The reference library doesn't use these, but you can use it to hold the resource if you resolve it. (The individual or organization that authored the Contract definition, derivative, or instance in any legal state.)
     */
    public Contract setAuthorTarget(Resource value) { 
      this.authorTarget = value;
      return this;
    }

    /**
     * @return {@link #scope} (A selector of legal concerns for this Contract definition, derivative, or instance in any legal state.)
     */
    public CodeableConcept getScope() { 
      if (this.scope == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.scope");
        else if (Configuration.doAutoCreate())
          this.scope = new CodeableConcept(); // cc
      return this.scope;
    }

    public boolean hasScope() { 
      return this.scope != null && !this.scope.isEmpty();
    }

    /**
     * @param value {@link #scope} (A selector of legal concerns for this Contract definition, derivative, or instance in any legal state.)
     */
    public Contract setScope(CodeableConcept value) { 
      this.scope = value;
      return this;
    }

    /**
     * @return {@link #topic} (Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.)
     */
    public Type getTopic() { 
      return this.topic;
    }

    /**
     * @return {@link #topic} (Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.)
     */
    public CodeableConcept getTopicCodeableConcept() throws FHIRException { 
      if (this.topic == null)
        this.topic = new CodeableConcept();
      if (!(this.topic instanceof CodeableConcept))
        throw new FHIRException("Type mismatch: the type CodeableConcept was expected, but "+this.topic.getClass().getName()+" was encountered");
      return (CodeableConcept) this.topic;
    }

    public boolean hasTopicCodeableConcept() { 
      return this != null && this.topic instanceof CodeableConcept;
    }

    /**
     * @return {@link #topic} (Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.)
     */
    public Reference getTopicReference() throws FHIRException { 
      if (this.topic == null)
        this.topic = new Reference();
      if (!(this.topic instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.topic.getClass().getName()+" was encountered");
      return (Reference) this.topic;
    }

    public boolean hasTopicReference() { 
      return this != null && this.topic instanceof Reference;
    }

    public boolean hasTopic() { 
      return this.topic != null && !this.topic.isEmpty();
    }

    /**
     * @param value {@link #topic} (Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.)
     */
    public Contract setTopic(Type value) { 
      if (value != null && !(value instanceof CodeableConcept || value instanceof Reference))
        throw new Error("Not the right type for Contract.topic[x]: "+value.fhirType());
      this.topic = value;
      return this;
    }

    /**
     * @return {@link #type} (A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance in any legal state.  Provides additional information about its content within the context of the Contract's scope to distinguish the kinds of systems that would be interested in the contract.)
     */
    public CodeableConcept getType() { 
      if (this.type == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.type");
        else if (Configuration.doAutoCreate())
          this.type = new CodeableConcept(); // cc
      return this.type;
    }

    public boolean hasType() { 
      return this.type != null && !this.type.isEmpty();
    }

    /**
     * @param value {@link #type} (A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance in any legal state.  Provides additional information about its content within the context of the Contract's scope to distinguish the kinds of systems that would be interested in the contract.)
     */
    public Contract setType(CodeableConcept value) { 
      this.type = value;
      return this;
    }

    /**
     * @return {@link #subType} (Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within the context of the Contract's scope.)
     */
    public List<CodeableConcept> getSubType() { 
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      return this.subType;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setSubType(List<CodeableConcept> theSubType) { 
      this.subType = theSubType;
      return this;
    }

    public boolean hasSubType() { 
      if (this.subType == null)
        return false;
      for (CodeableConcept item : this.subType)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public CodeableConcept addSubType() { //3
      CodeableConcept t = new CodeableConcept();
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return t;
    }

    public Contract addSubType(CodeableConcept t) { //3
      if (t == null)
        return this;
      if (this.subType == null)
        this.subType = new ArrayList<CodeableConcept>();
      this.subType.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #subType}, creating it if it does not already exist
     */
    public CodeableConcept getSubTypeFirstRep() { 
      if (getSubType().isEmpty()) {
        addSubType();
      }
      return getSubType().get(0);
    }

    /**
     * @return {@link #contentDefinition} (Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be associated with and transformable into a Contract.)
     */
    public ContentDefinitionComponent getContentDefinition() { 
      if (this.contentDefinition == null)
        if (Configuration.errorOnAutoCreate())
          throw new Error("Attempt to auto-create Contract.contentDefinition");
        else if (Configuration.doAutoCreate())
          this.contentDefinition = new ContentDefinitionComponent(); // cc
      return this.contentDefinition;
    }

    public boolean hasContentDefinition() { 
      return this.contentDefinition != null && !this.contentDefinition.isEmpty();
    }

    /**
     * @param value {@link #contentDefinition} (Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be associated with and transformable into a Contract.)
     */
    public Contract setContentDefinition(ContentDefinitionComponent value) { 
      this.contentDefinition = value;
      return this;
    }

    /**
     * @return {@link #term} (One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.)
     */
    public List<TermComponent> getTerm() { 
      if (this.term == null)
        this.term = new ArrayList<TermComponent>();
      return this.term;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setTerm(List<TermComponent> theTerm) { 
      this.term = theTerm;
      return this;
    }

    public boolean hasTerm() { 
      if (this.term == null)
        return false;
      for (TermComponent item : this.term)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public TermComponent addTerm() { //3
      TermComponent t = new TermComponent();
      if (this.term == null)
        this.term = new ArrayList<TermComponent>();
      this.term.add(t);
      return t;
    }

    public Contract addTerm(TermComponent t) { //3
      if (t == null)
        return this;
      if (this.term == null)
        this.term = new ArrayList<TermComponent>();
      this.term.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #term}, creating it if it does not already exist
     */
    public TermComponent getTermFirstRep() { 
      if (getTerm().isEmpty()) {
        addTerm();
      }
      return getTerm().get(0);
    }

    /**
     * @return {@link #supportingInfo} (Information that may be needed by/relevant to the performer in their execution of this term action.)
     */
    public List<Reference> getSupportingInfo() { 
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      return this.supportingInfo;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setSupportingInfo(List<Reference> theSupportingInfo) { 
      this.supportingInfo = theSupportingInfo;
      return this;
    }

    public boolean hasSupportingInfo() { 
      if (this.supportingInfo == null)
        return false;
      for (Reference item : this.supportingInfo)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addSupportingInfo() { //3
      Reference t = new Reference();
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      this.supportingInfo.add(t);
      return t;
    }

    public Contract addSupportingInfo(Reference t) { //3
      if (t == null)
        return this;
      if (this.supportingInfo == null)
        this.supportingInfo = new ArrayList<Reference>();
      this.supportingInfo.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #supportingInfo}, creating it if it does not already exist
     */
    public Reference getSupportingInfoFirstRep() { 
      if (getSupportingInfo().isEmpty()) {
        addSupportingInfo();
      }
      return getSupportingInfo().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Resource> getSupportingInfoTarget() { 
      if (this.supportingInfoTarget == null)
        this.supportingInfoTarget = new ArrayList<Resource>();
      return this.supportingInfoTarget;
    }

    /**
     * @return {@link #relevantHistory} (Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract.  The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.html#Provenance.entity.)
     */
    public List<Reference> getRelevantHistory() { 
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      return this.relevantHistory;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setRelevantHistory(List<Reference> theRelevantHistory) { 
      this.relevantHistory = theRelevantHistory;
      return this;
    }

    public boolean hasRelevantHistory() { 
      if (this.relevantHistory == null)
        return false;
      for (Reference item : this.relevantHistory)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public Reference addRelevantHistory() { //3
      Reference t = new Reference();
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      this.relevantHistory.add(t);
      return t;
    }

    public Contract addRelevantHistory(Reference t) { //3
      if (t == null)
        return this;
      if (this.relevantHistory == null)
        this.relevantHistory = new ArrayList<Reference>();
      this.relevantHistory.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #relevantHistory}, creating it if it does not already exist
     */
    public Reference getRelevantHistoryFirstRep() { 
      if (getRelevantHistory().isEmpty()) {
        addRelevantHistory();
      }
      return getRelevantHistory().get(0);
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public List<Provenance> getRelevantHistoryTarget() { 
      if (this.relevantHistoryTarget == null)
        this.relevantHistoryTarget = new ArrayList<Provenance>();
      return this.relevantHistoryTarget;
    }

    /**
     * @deprecated Use Reference#setResource(IBaseResource) instead
     */
    @Deprecated
    public Provenance addRelevantHistoryTarget() { 
      Provenance r = new Provenance();
      if (this.relevantHistoryTarget == null)
        this.relevantHistoryTarget = new ArrayList<Provenance>();
      this.relevantHistoryTarget.add(r);
      return r;
    }

    /**
     * @return {@link #signer} (Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness.)
     */
    public List<SignatoryComponent> getSigner() { 
      if (this.signer == null)
        this.signer = new ArrayList<SignatoryComponent>();
      return this.signer;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setSigner(List<SignatoryComponent> theSigner) { 
      this.signer = theSigner;
      return this;
    }

    public boolean hasSigner() { 
      if (this.signer == null)
        return false;
      for (SignatoryComponent item : this.signer)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public SignatoryComponent addSigner() { //3
      SignatoryComponent t = new SignatoryComponent();
      if (this.signer == null)
        this.signer = new ArrayList<SignatoryComponent>();
      this.signer.add(t);
      return t;
    }

    public Contract addSigner(SignatoryComponent t) { //3
      if (t == null)
        return this;
      if (this.signer == null)
        this.signer = new ArrayList<SignatoryComponent>();
      this.signer.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #signer}, creating it if it does not already exist
     */
    public SignatoryComponent getSignerFirstRep() { 
      if (getSigner().isEmpty()) {
        addSigner();
      }
      return getSigner().get(0);
    }

    /**
     * @return {@link #friendly} (The "patient friendly language" versionof the Contract in whole or in parts. "Patient friendly language" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.)
     */
    public List<FriendlyLanguageComponent> getFriendly() { 
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      return this.friendly;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setFriendly(List<FriendlyLanguageComponent> theFriendly) { 
      this.friendly = theFriendly;
      return this;
    }

    public boolean hasFriendly() { 
      if (this.friendly == null)
        return false;
      for (FriendlyLanguageComponent item : this.friendly)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public FriendlyLanguageComponent addFriendly() { //3
      FriendlyLanguageComponent t = new FriendlyLanguageComponent();
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      this.friendly.add(t);
      return t;
    }

    public Contract addFriendly(FriendlyLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.friendly == null)
        this.friendly = new ArrayList<FriendlyLanguageComponent>();
      this.friendly.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #friendly}, creating it if it does not already exist
     */
    public FriendlyLanguageComponent getFriendlyFirstRep() { 
      if (getFriendly().isEmpty()) {
        addFriendly();
      }
      return getFriendly().get(0);
    }

    /**
     * @return {@link #legal} (List of Legal expressions or representations of this Contract.)
     */
    public List<LegalLanguageComponent> getLegal() { 
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      return this.legal;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setLegal(List<LegalLanguageComponent> theLegal) { 
      this.legal = theLegal;
      return this;
    }

    public boolean hasLegal() { 
      if (this.legal == null)
        return false;
      for (LegalLanguageComponent item : this.legal)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public LegalLanguageComponent addLegal() { //3
      LegalLanguageComponent t = new LegalLanguageComponent();
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      this.legal.add(t);
      return t;
    }

    public Contract addLegal(LegalLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.legal == null)
        this.legal = new ArrayList<LegalLanguageComponent>();
      this.legal.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #legal}, creating it if it does not already exist
     */
    public LegalLanguageComponent getLegalFirstRep() { 
      if (getLegal().isEmpty()) {
        addLegal();
      }
      return getLegal().get(0);
    }

    /**
     * @return {@link #rule} (List of Computable Policy Rule Language Representations of this Contract.)
     */
    public List<ComputableLanguageComponent> getRule() { 
      if (this.rule == null)
        this.rule = new ArrayList<ComputableLanguageComponent>();
      return this.rule;
    }

    /**
     * @return Returns a reference to <code>this</code> for easy method chaining
     */
    public Contract setRule(List<ComputableLanguageComponent> theRule) { 
      this.rule = theRule;
      return this;
    }

    public boolean hasRule() { 
      if (this.rule == null)
        return false;
      for (ComputableLanguageComponent item : this.rule)
        if (!item.isEmpty())
          return true;
      return false;
    }

    public ComputableLanguageComponent addRule() { //3
      ComputableLanguageComponent t = new ComputableLanguageComponent();
      if (this.rule == null)
        this.rule = new ArrayList<ComputableLanguageComponent>();
      this.rule.add(t);
      return t;
    }

    public Contract addRule(ComputableLanguageComponent t) { //3
      if (t == null)
        return this;
      if (this.rule == null)
        this.rule = new ArrayList<ComputableLanguageComponent>();
      this.rule.add(t);
      return this;
    }

    /**
     * @return The first repetition of repeating field {@link #rule}, creating it if it does not already exist
     */
    public ComputableLanguageComponent getRuleFirstRep() { 
      if (getRule().isEmpty()) {
        addRule();
      }
      return getRule().get(0);
    }

    /**
     * @return {@link #legallyBinding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Type getLegallyBinding() { 
      return this.legallyBinding;
    }

    /**
     * @return {@link #legallyBinding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Attachment getLegallyBindingAttachment() throws FHIRException { 
      if (this.legallyBinding == null)
        this.legallyBinding = new Attachment();
      if (!(this.legallyBinding instanceof Attachment))
        throw new FHIRException("Type mismatch: the type Attachment was expected, but "+this.legallyBinding.getClass().getName()+" was encountered");
      return (Attachment) this.legallyBinding;
    }

    public boolean hasLegallyBindingAttachment() { 
      return this != null && this.legallyBinding instanceof Attachment;
    }

    /**
     * @return {@link #legallyBinding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Reference getLegallyBindingReference() throws FHIRException { 
      if (this.legallyBinding == null)
        this.legallyBinding = new Reference();
      if (!(this.legallyBinding instanceof Reference))
        throw new FHIRException("Type mismatch: the type Reference was expected, but "+this.legallyBinding.getClass().getName()+" was encountered");
      return (Reference) this.legallyBinding;
    }

    public boolean hasLegallyBindingReference() { 
      return this != null && this.legallyBinding instanceof Reference;
    }

    public boolean hasLegallyBinding() { 
      return this.legallyBinding != null && !this.legallyBinding.isEmpty();
    }

    /**
     * @param value {@link #legallyBinding} (Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the "source of truth" and which would be the basis for legal action related to enforcement of this Contract.)
     */
    public Contract setLegallyBinding(Type value) { 
      if (value != null && !(value instanceof Attachment || value instanceof Reference))
        throw new Error("Not the right type for Contract.legallyBinding[x]: "+value.fhirType());
      this.legallyBinding = value;
      return this;
    }

      protected void listChildren(List<Property> children) {
        super.listChildren(children);
        children.add(new Property("identifier", "Identifier", "Unique identifier for this Contract or a derivative that references a Source Contract.", 0, java.lang.Integer.MAX_VALUE, identifier));
        children.add(new Property("url", "uri", "Canonical identifier for this contract, represented as a URI (globally unique).", 0, 1, url));
        children.add(new Property("version", "string", "An edition identifier used for business purposes to label business significant variants.", 0, 1, version));
        children.add(new Property("status", "code", "The status of the resource instance.", 0, 1, status));
        children.add(new Property("legalState", "CodeableConcept", "Legal states of the formation of a legal instrument, which is a formally executed written document that can be formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual duty, obligation, or right, and therefore evidences that act, process, or agreement.", 0, 1, legalState));
        children.add(new Property("instantiatesCanonical", "Reference(Contract)", "The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.", 0, 1, instantiatesCanonical));
        children.add(new Property("instantiatesUri", "uri", "The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.", 0, 1, instantiatesUri));
        children.add(new Property("contentDerivative", "CodeableConcept", "The minimal content derived from the basal information source at a specific stage in its lifecycle.", 0, 1, contentDerivative));
        children.add(new Property("issued", "dateTime", "When this  Contract was issued.", 0, 1, issued));
        children.add(new Property("applies", "Period", "Relevant time or time-period when this Contract is applicable.", 0, 1, applies));
        children.add(new Property("expirationType", "CodeableConcept", "Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract.", 0, 1, expirationType));
        children.add(new Property("subject", "Reference(Any)", "The target entity impacted by or of interest to parties to the agreement.", 0, java.lang.Integer.MAX_VALUE, subject));
        children.add(new Property("authority", "Reference(Organization)", "A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.", 0, java.lang.Integer.MAX_VALUE, authority));
        children.add(new Property("domain", "Reference(Location)", "Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.", 0, java.lang.Integer.MAX_VALUE, domain));
        children.add(new Property("site", "Reference(Location)", "Sites in which the contract is complied with,  exercised, or in force.", 0, java.lang.Integer.MAX_VALUE, site));
        children.add(new Property("name", "string", "A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides additional information about its content. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name));
        children.add(new Property("title", "string", "A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.", 0, 1, title));
        children.add(new Property("subtitle", "string", "An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.", 0, 1, subtitle));
        children.add(new Property("alias", "string", "Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.g., a domain specific contract number related to legislation.", 0, java.lang.Integer.MAX_VALUE, alias));
        children.add(new Property("author", "Reference(Patient|Practitioner|PractitionerRole|Organization)", "The individual or organization that authored the Contract definition, derivative, or instance in any legal state.", 0, 1, author));
        children.add(new Property("scope", "CodeableConcept", "A selector of legal concerns for this Contract definition, derivative, or instance in any legal state.", 0, 1, scope));
        children.add(new Property("topic[x]", "CodeableConcept|Reference(Any)", "Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.", 0, 1, topic));
        children.add(new Property("type", "CodeableConcept", "A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance in any legal state.  Provides additional information about its content within the context of the Contract's scope to distinguish the kinds of systems that would be interested in the contract.", 0, 1, type));
        children.add(new Property("subType", "CodeableConcept", "Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within the context of the Contract's scope.", 0, java.lang.Integer.MAX_VALUE, subType));
        children.add(new Property("contentDefinition", "", "Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be associated with and transformable into a Contract.", 0, 1, contentDefinition));
        children.add(new Property("term", "", "One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.", 0, java.lang.Integer.MAX_VALUE, term));
        children.add(new Property("supportingInfo", "Reference(Any)", "Information that may be needed by/relevant to the performer in their execution of this term action.", 0, java.lang.Integer.MAX_VALUE, supportingInfo));
        children.add(new Property("relevantHistory", "Reference(Provenance)", "Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract.  The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.html#Provenance.entity.", 0, java.lang.Integer.MAX_VALUE, relevantHistory));
        children.add(new Property("signer", "", "Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness.", 0, java.lang.Integer.MAX_VALUE, signer));
        children.add(new Property("friendly", "", "The \"patient friendly language\" versionof the Contract in whole or in parts. \"Patient friendly language\" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.", 0, java.lang.Integer.MAX_VALUE, friendly));
        children.add(new Property("legal", "", "List of Legal expressions or representations of this Contract.", 0, java.lang.Integer.MAX_VALUE, legal));
        children.add(new Property("rule", "", "List of Computable Policy Rule Language Representations of this Contract.", 0, java.lang.Integer.MAX_VALUE, rule));
        children.add(new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding));
      }

      @Override
      public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
        switch (_hash) {
        case -1618432855: /*identifier*/  return new Property("identifier", "Identifier", "Unique identifier for this Contract or a derivative that references a Source Contract.", 0, java.lang.Integer.MAX_VALUE, identifier);
        case 116079: /*url*/  return new Property("url", "uri", "Canonical identifier for this contract, represented as a URI (globally unique).", 0, 1, url);
        case 351608024: /*version*/  return new Property("version", "string", "An edition identifier used for business purposes to label business significant variants.", 0, 1, version);
        case -892481550: /*status*/  return new Property("status", "code", "The status of the resource instance.", 0, 1, status);
        case 568606040: /*legalState*/  return new Property("legalState", "CodeableConcept", "Legal states of the formation of a legal instrument, which is a formally executed written document that can be formally attributed to its author, records and formally expresses a legally enforceable act, process, or contractual duty, obligation, or right, and therefore evidences that act, process, or agreement.", 0, 1, legalState);
        case 8911915: /*instantiatesCanonical*/  return new Property("instantiatesCanonical", "Reference(Contract)", "The URL pointing to a FHIR-defined Contract Definition that is adhered to in whole or part by this Contract.", 0, 1, instantiatesCanonical);
        case -1926393373: /*instantiatesUri*/  return new Property("instantiatesUri", "uri", "The URL pointing to an externally maintained definition that is adhered to in whole or in part by this Contract.", 0, 1, instantiatesUri);
        case -92412192: /*contentDerivative*/  return new Property("contentDerivative", "CodeableConcept", "The minimal content derived from the basal information source at a specific stage in its lifecycle.", 0, 1, contentDerivative);
        case -1179159893: /*issued*/  return new Property("issued", "dateTime", "When this  Contract was issued.", 0, 1, issued);
        case -793235316: /*applies*/  return new Property("applies", "Period", "Relevant time or time-period when this Contract is applicable.", 0, 1, applies);
        case -668311927: /*expirationType*/  return new Property("expirationType", "CodeableConcept", "Event resulting in discontinuation or termination of this Contract instance by one or more parties to the contract.", 0, 1, expirationType);
        case -1867885268: /*subject*/  return new Property("subject", "Reference(Any)", "The target entity impacted by or of interest to parties to the agreement.", 0, java.lang.Integer.MAX_VALUE, subject);
        case 1475610435: /*authority*/  return new Property("authority", "Reference(Organization)", "A formally or informally recognized grouping of people, principals, organizations, or jurisdictions formed for the purpose of achieving some form of collective action such as the promulgation, administration and enforcement of contracts and policies.", 0, java.lang.Integer.MAX_VALUE, authority);
        case -1326197564: /*domain*/  return new Property("domain", "Reference(Location)", "Recognized governance framework or system operating with a circumscribed scope in accordance with specified principles, policies, processes or procedures for managing rights, actions, or behaviors of parties or principals relative to resources.", 0, java.lang.Integer.MAX_VALUE, domain);
        case 3530567: /*site*/  return new Property("site", "Reference(Location)", "Sites in which the contract is complied with,  exercised, or in force.", 0, java.lang.Integer.MAX_VALUE, site);
        case 3373707: /*name*/  return new Property("name", "string", "A natural language name identifying this Contract definition, derivative, or instance in any legal state. Provides additional information about its content. This name should be usable as an identifier for the module by machine processing applications such as code generation.", 0, 1, name);
        case 110371416: /*title*/  return new Property("title", "string", "A short, descriptive, user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.", 0, 1, title);
        case -2060497896: /*subtitle*/  return new Property("subtitle", "string", "An explanatory or alternate user-friendly title for this Contract definition, derivative, or instance in any legal state.t giving additional information about its content.", 0, 1, subtitle);
        case 92902992: /*alias*/  return new Property("alias", "string", "Alternative representation of the title for this Contract definition, derivative, or instance in any legal state., e.g., a domain specific contract number related to legislation.", 0, java.lang.Integer.MAX_VALUE, alias);
        case -1406328437: /*author*/  return new Property("author", "Reference(Patient|Practitioner|PractitionerRole|Organization)", "The individual or organization that authored the Contract definition, derivative, or instance in any legal state.", 0, 1, author);
        case 109264468: /*scope*/  return new Property("scope", "CodeableConcept", "A selector of legal concerns for this Contract definition, derivative, or instance in any legal state.", 0, 1, scope);
        case -957295375: /*topic[x]*/  return new Property("topic[x]", "CodeableConcept|Reference(Any)", "Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.", 0, 1, topic);
        case 110546223: /*topic*/  return new Property("topic[x]", "CodeableConcept|Reference(Any)", "Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.", 0, 1, topic);
        case 777778802: /*topicCodeableConcept*/  return new Property("topic[x]", "CodeableConcept|Reference(Any)", "Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.", 0, 1, topic);
        case -343345444: /*topicReference*/  return new Property("topic[x]", "CodeableConcept|Reference(Any)", "Narrows the range of legal concerns to focus on the achievement of specific contractual objectives.", 0, 1, topic);
        case 3575610: /*type*/  return new Property("type", "CodeableConcept", "A high-level category for the legal instrument, whether constructed as a Contract definition, derivative, or instance in any legal state.  Provides additional information about its content within the context of the Contract's scope to distinguish the kinds of systems that would be interested in the contract.", 0, 1, type);
        case -1868521062: /*subType*/  return new Property("subType", "CodeableConcept", "Sub-category for the Contract that distinguishes the kinds of systems that would be interested in the Contract within the context of the Contract's scope.", 0, java.lang.Integer.MAX_VALUE, subType);
        case 247055020: /*contentDefinition*/  return new Property("contentDefinition", "", "Precusory content developed with a focus and intent of supporting the formation a Contract instance, which may be associated with and transformable into a Contract.", 0, 1, contentDefinition);
        case 3556460: /*term*/  return new Property("term", "", "One or more Contract Provisions, which may be related and conveyed as a group, and may contain nested groups.", 0, java.lang.Integer.MAX_VALUE, term);
        case 1922406657: /*supportingInfo*/  return new Property("supportingInfo", "Reference(Any)", "Information that may be needed by/relevant to the performer in their execution of this term action.", 0, java.lang.Integer.MAX_VALUE, supportingInfo);
        case 1538891575: /*relevantHistory*/  return new Property("relevantHistory", "Reference(Provenance)", "Links to Provenance records for past versions of this Contract definition, derivative, or instance, which identify key state transitions or updates that are likely to be relevant to a user looking at the current version of the Contract.  The Provence.entity indicates the target that was changed in the update. http://build.fhir.org/provenance-definitions.html#Provenance.entity.", 0, java.lang.Integer.MAX_VALUE, relevantHistory);
        case -902467798: /*signer*/  return new Property("signer", "", "Parties with legal standing in the Contract, including the principal parties, the grantor(s) and grantee(s), which are any person or organization bound by the contract, and any ancillary parties, which facilitate the execution of the contract such as a notary or witness.", 0, java.lang.Integer.MAX_VALUE, signer);
        case -1423054677: /*friendly*/  return new Property("friendly", "", "The \"patient friendly language\" versionof the Contract in whole or in parts. \"Patient friendly language\" means the representation of the Contract and Contract Provisions in a manner that is readily accessible and understandable by a layperson in accordance with best practices for communication styles that ensure that those agreeing to or signing the Contract understand the roles, actions, obligations, responsibilities, and implication of the agreement.", 0, java.lang.Integer.MAX_VALUE, friendly);
        case 102851257: /*legal*/  return new Property("legal", "", "List of Legal expressions or representations of this Contract.", 0, java.lang.Integer.MAX_VALUE, legal);
        case 3512060: /*rule*/  return new Property("rule", "", "List of Computable Policy Rule Language Representations of this Contract.", 0, java.lang.Integer.MAX_VALUE, rule);
        case -772497791: /*legallyBinding[x]*/  return new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding);
        case -126751329: /*legallyBinding*/  return new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding);
        case 344057890: /*legallyBindingAttachment*/  return new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding);
        case -296528788: /*legallyBindingReference*/  return new Property("legallyBinding[x]", "Attachment|Reference(Composition|DocumentReference|QuestionnaireResponse|Contract)", "Legally binding Contract: This is the signed and legally recognized representation of the Contract, which is considered the \"source of truth\" and which would be the basis for legal action related to enforcement of this Contract.", 0, 1, legallyBinding);
        default: return super.getNamedProperty(_hash, _name, _checkValid);
        }

      }

      @Override
      public Base[] getProperty(int hash, String name, boolean checkValid) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return this.identifier == null ? new Base[0] : this.identifier.toArray(new Base[this.identifier.size()]); // Identifier
        case 116079: /*url*/ return this.url == null ? new Base[0] : new Base[] {this.url}; // UriType
        case 351608024: /*version*/ return this.version == null ? new Base[0] : new Base[] {this.version}; // StringType
        case -892481550: /*status*/ return this.status == null ? new Base[0] : new Base[] {this.status}; // Enumeration<ContractStatus>
        case 568606040: /*legalState*/ return this.legalState == null ? new Base[0] : new Base[] {this.legalState}; // CodeableConcept
        case 8911915: /*instantiatesCanonical*/ return this.instantiatesCanonical == null ? new Base[0] : new Base[] {this.instantiatesCanonical}; // Reference
        case -1926393373: /*instantiatesUri*/ return this.instantiatesUri == null ? new Base[0] : new Base[] {this.instantiatesUri}; // UriType
        case -92412192: /*contentDerivative*/ return this.contentDerivative == null ? new Base[0] : new Base[] {this.contentDerivative}; // CodeableConcept
        case -1179159893: /*issued*/ return this.issued == null ? new Base[0] : new Base[] {this.issued}; // DateTimeType
        case -793235316: /*applies*/ return this.applies == null ? new Base[0] : new Base[] {this.applies}; // Period
        case -668311927: /*expirationType*/ return this.expirationType == null ? new Base[0] : new Base[] {this.expirationType}; // CodeableConcept
        case -1867885268: /*subject*/ return this.subject == null ? new Base[0] : this.subject.toArray(new Base[this.subject.size()]); // Reference
        case 1475610435: /*authority*/ return this.authority == null ? new Base[0] : this.authority.toArray(new Base[this.authority.size()]); // Reference
        case -1326197564: /*domain*/ return this.domain == null ? new Base[0] : this.domain.toArray(new Base[this.domain.size()]); // Reference
        case 3530567: /*site*/ return this.site == null ? new Base[0] : this.site.toArray(new Base[this.site.size()]); // Reference
        case 3373707: /*name*/ return this.name == null ? new Base[0] : new Base[] {this.name}; // StringType
        case 110371416: /*title*/ return this.title == null ? new Base[0] : new Base[] {this.title}; // StringType
        case -2060497896: /*subtitle*/ return this.subtitle == null ? new Base[0] : new Base[] {this.subtitle}; // StringType
        case 92902992: /*alias*/ return this.alias == null ? new Base[0] : this.alias.toArray(new Base[this.alias.size()]); // StringType
        case -1406328437: /*author*/ return this.author == null ? new Base[0] : new Base[] {this.author}; // Reference
        case 109264468: /*scope*/ return this.scope == null ? new Base[0] : new Base[] {this.scope}; // CodeableConcept
        case 110546223: /*topic*/ return this.topic == null ? new Base[0] : new Base[] {this.topic}; // Type
        case 3575610: /*type*/ return this.type == null ? new Base[0] : new Base[] {this.type}; // CodeableConcept
        case -1868521062: /*subType*/ return this.subType == null ? new Base[0] : this.subType.toArray(new Base[this.subType.size()]); // CodeableConcept
        case 247055020: /*contentDefinition*/ return this.contentDefinition == null ? new Base[0] : new Base[] {this.contentDefinition}; // ContentDefinitionComponent
        case 3556460: /*term*/ return this.term == null ? new Base[0] : this.term.toArray(new Base[this.term.size()]); // TermComponent
        case 1922406657: /*supportingInfo*/ return this.supportingInfo == null ? new Base[0] : this.supportingInfo.toArray(new Base[this.supportingInfo.size()]); // Reference
        case 1538891575: /*relevantHistory*/ return this.relevantHistory == null ? new Base[0] : this.relevantHistory.toArray(new Base[this.relevantHistory.size()]); // Reference
        case -902467798: /*signer*/ return this.signer == null ? new Base[0] : this.signer.toArray(new Base[this.signer.size()]); // SignatoryComponent
        case -1423054677: /*friendly*/ return this.friendly == null ? new Base[0] : this.friendly.toArray(new Base[this.friendly.size()]); // FriendlyLanguageComponent
        case 102851257: /*legal*/ return this.legal == null ? new Base[0] : this.legal.toArray(new Base[this.legal.size()]); // LegalLanguageComponent
        case 3512060: /*rule*/ return this.rule == null ? new Base[0] : this.rule.toArray(new Base[this.rule.size()]); // ComputableLanguageComponent
        case -126751329: /*legallyBinding*/ return this.legallyBinding == null ? new Base[0] : new Base[] {this.legallyBinding}; // Type
        default: return super.getProperty(hash, name, checkValid);
        }

      }

      @Override
      public Base setProperty(int hash, String name, Base value) throws FHIRException {
        switch (hash) {
        case -1618432855: // identifier
          this.getIdentifier().add(castToIdentifier(value)); // Identifier
          return value;
        case 116079: // url
          this.url = castToUri(value); // UriType
          return value;
        case 351608024: // version
          this.version = castToString(value); // StringType
          return value;
        case -892481550: // status
          value = new ContractStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ContractStatus>
          return value;
        case 568606040: // legalState
          this.legalState = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 8911915: // instantiatesCanonical
          this.instantiatesCanonical = castToReference(value); // Reference
          return value;
        case -1926393373: // instantiatesUri
          this.instantiatesUri = castToUri(value); // UriType
          return value;
        case -92412192: // contentDerivative
          this.contentDerivative = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1179159893: // issued
          this.issued = castToDateTime(value); // DateTimeType
          return value;
        case -793235316: // applies
          this.applies = castToPeriod(value); // Period
          return value;
        case -668311927: // expirationType
          this.expirationType = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1867885268: // subject
          this.getSubject().add(castToReference(value)); // Reference
          return value;
        case 1475610435: // authority
          this.getAuthority().add(castToReference(value)); // Reference
          return value;
        case -1326197564: // domain
          this.getDomain().add(castToReference(value)); // Reference
          return value;
        case 3530567: // site
          this.getSite().add(castToReference(value)); // Reference
          return value;
        case 3373707: // name
          this.name = castToString(value); // StringType
          return value;
        case 110371416: // title
          this.title = castToString(value); // StringType
          return value;
        case -2060497896: // subtitle
          this.subtitle = castToString(value); // StringType
          return value;
        case 92902992: // alias
          this.getAlias().add(castToString(value)); // StringType
          return value;
        case -1406328437: // author
          this.author = castToReference(value); // Reference
          return value;
        case 109264468: // scope
          this.scope = castToCodeableConcept(value); // CodeableConcept
          return value;
        case 110546223: // topic
          this.topic = castToType(value); // Type
          return value;
        case 3575610: // type
          this.type = castToCodeableConcept(value); // CodeableConcept
          return value;
        case -1868521062: // subType
          this.getSubType().add(castToCodeableConcept(value)); // CodeableConcept
          return value;
        case 247055020: // contentDefinition
          this.contentDefinition = (ContentDefinitionComponent) value; // ContentDefinitionComponent
          return value;
        case 3556460: // term
          this.getTerm().add((TermComponent) value); // TermComponent
          return value;
        case 1922406657: // supportingInfo
          this.getSupportingInfo().add(castToReference(value)); // Reference
          return value;
        case 1538891575: // relevantHistory
          this.getRelevantHistory().add(castToReference(value)); // Reference
          return value;
        case -902467798: // signer
          this.getSigner().add((SignatoryComponent) value); // SignatoryComponent
          return value;
        case -1423054677: // friendly
          this.getFriendly().add((FriendlyLanguageComponent) value); // FriendlyLanguageComponent
          return value;
        case 102851257: // legal
          this.getLegal().add((LegalLanguageComponent) value); // LegalLanguageComponent
          return value;
        case 3512060: // rule
          this.getRule().add((ComputableLanguageComponent) value); // ComputableLanguageComponent
          return value;
        case -126751329: // legallyBinding
          this.legallyBinding = castToType(value); // Type
          return value;
        default: return super.setProperty(hash, name, value);
        }

      }

      @Override
      public Base setProperty(String name, Base value) throws FHIRException {
        if (name.equals("identifier")) {
          this.getIdentifier().add(castToIdentifier(value));
        } else if (name.equals("url")) {
          this.url = castToUri(value); // UriType
        } else if (name.equals("version")) {
          this.version = castToString(value); // StringType
        } else if (name.equals("status")) {
          value = new ContractStatusEnumFactory().fromType(castToCode(value));
          this.status = (Enumeration) value; // Enumeration<ContractStatus>
        } else if (name.equals("legalState")) {
          this.legalState = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("instantiatesCanonical")) {
          this.instantiatesCanonical = castToReference(value); // Reference
        } else if (name.equals("instantiatesUri")) {
          this.instantiatesUri = castToUri(value); // UriType
        } else if (name.equals("contentDerivative")) {
          this.contentDerivative = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("issued")) {
          this.issued = castToDateTime(value); // DateTimeType
        } else if (name.equals("applies")) {
          this.applies = castToPeriod(value); // Period
        } else if (name.equals("expirationType")) {
          this.expirationType = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subject")) {
          this.getSubject().add(castToReference(value));
        } else if (name.equals("authority")) {
          this.getAuthority().add(castToReference(value));
        } else if (name.equals("domain")) {
          this.getDomain().add(castToReference(value));
        } else if (name.equals("site")) {
          this.getSite().add(castToReference(value));
        } else if (name.equals("name")) {
          this.name = castToString(value); // StringType
        } else if (name.equals("title")) {
          this.title = castToString(value); // StringType
        } else if (name.equals("subtitle")) {
          this.subtitle = castToString(value); // StringType
        } else if (name.equals("alias")) {
          this.getAlias().add(castToString(value));
        } else if (name.equals("author")) {
          this.author = castToReference(value); // Reference
        } else if (name.equals("scope")) {
          this.scope = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("topic[x]")) {
          this.topic = castToType(value); // Type
        } else if (name.equals("type")) {
          this.type = castToCodeableConcept(value); // CodeableConcept
        } else if (name.equals("subType")) {
          this.getSubType().add(castToCodeableConcept(value));
        } else if (name.equals("contentDefinition")) {
          this.contentDefinition = (ContentDefinitionComponent) value; // ContentDefinitionComponent
        } else if (name.equals("term")) {
          this.getTerm().add((TermComponent) value);
        } else if (name.equals("supportingInfo")) {
          this.getSupportingInfo().add(castToReference(value));
        } else if (name.equals("relevantHistory")) {
          this.getRelevantHistory().add(castToReference(value));
        } else if (name.equals("signer")) {
          this.getSigner().add((SignatoryComponent) value);
        } else if (name.equals("friendly")) {
          this.getFriendly().add((FriendlyLanguageComponent) value);
        } else if (name.equals("legal")) {
          this.getLegal().add((LegalLanguageComponent) value);
        } else if (name.equals("rule")) {
          this.getRule().add((ComputableLanguageComponent) value);
        } else if (name.equals("legallyBinding[x]")) {
          this.legallyBinding = castToType(value); // Type
        } else
          return super.setProperty(name, value);
        return value;
      }

      @Override
      public Base makeProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855:  return addIdentifier(); 
        case 116079:  return getUrlElement();
        case 351608024:  return getVersionElement();
        case -892481550:  return getStatusElement();
        case 568606040:  return getLegalState(); 
        case 8911915:  return getInstantiatesCanonical(); 
        case -1926393373:  return getInstantiatesUriElement();
        case -92412192:  return getContentDerivative(); 
        case -1179159893:  return getIssuedElement();
        case -793235316:  return getApplies(); 
        case -668311927:  return getExpirationType(); 
        case -1867885268:  return addSubject(); 
        case 1475610435:  return addAuthority(); 
        case -1326197564:  return addDomain(); 
        case 3530567:  return addSite(); 
        case 3373707:  return getNameElement();
        case 110371416:  return getTitleElement();
        case -2060497896:  return getSubtitleElement();
        case 92902992:  return addAliasElement();
        case -1406328437:  return getAuthor(); 
        case 109264468:  return getScope(); 
        case -957295375:  return getTopic(); 
        case 110546223:  return getTopic(); 
        case 3575610:  return getType(); 
        case -1868521062:  return addSubType(); 
        case 247055020:  return getContentDefinition(); 
        case 3556460:  return addTerm(); 
        case 1922406657:  return addSupportingInfo(); 
        case 1538891575:  return addRelevantHistory(); 
        case -902467798:  return addSigner(); 
        case -1423054677:  return addFriendly(); 
        case 102851257:  return addLegal(); 
        case 3512060:  return addRule(); 
        case -772497791:  return getLegallyBinding(); 
        case -126751329:  return getLegallyBinding(); 
        default: return super.makeProperty(hash, name);
        }

      }

      @Override
      public String[] getTypesForProperty(int hash, String name) throws FHIRException {
        switch (hash) {
        case -1618432855: /*identifier*/ return new String[] {"Identifier"};
        case 116079: /*url*/ return new String[] {"uri"};
        case 351608024: /*version*/ return new String[] {"string"};
        case -892481550: /*status*/ return new String[] {"code"};
        case 568606040: /*legalState*/ return new String[] {"CodeableConcept"};
        case 8911915: /*instantiatesCanonical*/ return new String[] {"Reference"};
        case -1926393373: /*instantiatesUri*/ return new String[] {"uri"};
        case -92412192: /*contentDerivative*/ return new String[] {"CodeableConcept"};
        case -1179159893: /*issued*/ return new String[] {"dateTime"};
        case -793235316: /*applies*/ return new String[] {"Period"};
        case -668311927: /*expirationType*/ return new String[] {"CodeableConcept"};
        case -1867885268: /*subject*/ return new String[] {"Reference"};
        case 1475610435: /*authority*/ return new String[] {"Reference"};
        case -1326197564: /*domain*/ return new String[] {"Reference"};
        case 3530567: /*site*/ return new String[] {"Reference"};
        case 3373707: /*name*/ return new String[] {"string"};
        case 110371416: /*title*/ return new String[] {"string"};
        case -2060497896: /*subtitle*/ return new String[] {"string"};
        case 92902992: /*alias*/ return new String[] {"string"};
        case -1406328437: /*author*/ return new String[] {"Reference"};
        case 109264468: /*scope*/ return new String[] {"CodeableConcept"};
        case 110546223: /*topic*/ return new String[] {"CodeableConcept", "Reference"};
        case 3575610: /*type*/ return new String[] {"CodeableConcept"};
        case -1868521062: /*subType*/ return new String[] {"CodeableConcept"};
        case 247055020: /*contentDefinition*/ return new String[] {};
        case 3556460: /*term*/ return new String[] {};
        case 1922406657: /*supportingInfo*/ return new String[] {"Reference"};
        case 1538891575: /*relevantHistory*/ return new String[] {"Reference"};
        case -902467798: /*signer*/ return new String[] {};
        case -1423054677: /*friendly*/ return new String[] {};
        case 102851257: /*legal*/ return new String[] {};
        case 3512060: /*rule*/ return new String[] {};
        case -126751329: /*legallyBinding*/ return new String[] {"Attachment", "Reference"};
        default: return super.getTypesForProperty(hash, name);
        }

      }

      @Override
      public Base addChild(String name) throws FHIRException {
        if (name.equals("identifier")) {
          return addIdentifier();
        }
        else if (name.equals("url")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.url");
        }
        else if (name.equals("version")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.version");
        }
        else if (name.equals("status")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.status");
        }
        else if (name.equals("legalState")) {
          this.legalState = new CodeableConcept();
          return this.legalState;
        }
        else if (name.equals("instantiatesCanonical")) {
          this.instantiatesCanonical = new Reference();
          return this.instantiatesCanonical;
        }
        else if (name.equals("instantiatesUri")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.instantiatesUri");
        }
        else if (name.equals("contentDerivative")) {
          this.contentDerivative = new CodeableConcept();
          return this.contentDerivative;
        }
        else if (name.equals("issued")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.issued");
        }
        else if (name.equals("applies")) {
          this.applies = new Period();
          return this.applies;
        }
        else if (name.equals("expirationType")) {
          this.expirationType = new CodeableConcept();
          return this.expirationType;
        }
        else if (name.equals("subject")) {
          return addSubject();
        }
        else if (name.equals("authority")) {
          return addAuthority();
        }
        else if (name.equals("domain")) {
          return addDomain();
        }
        else if (name.equals("site")) {
          return addSite();
        }
        else if (name.equals("name")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.name");
        }
        else if (name.equals("title")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.title");
        }
        else if (name.equals("subtitle")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.subtitle");
        }
        else if (name.equals("alias")) {
          throw new FHIRException("Cannot call addChild on a primitive type Contract.alias");
        }
        else if (name.equals("author")) {
          this.author = new Reference();
          return this.author;
        }
        else if (name.equals("scope")) {
          this.scope = new CodeableConcept();
          return this.scope;
        }
        else if (name.equals("topicCodeableConcept")) {
          this.topic = new CodeableConcept();
          return this.topic;
        }
        else if (name.equals("topicReference")) {
          this.topic = new Reference();
          return this.topic;
        }
        else if (name.equals("type")) {
          this.type = new CodeableConcept();
          return this.type;
        }
        else if (name.equals("subType")) {
          return addSubType();
        }
        else if (name.equals("contentDefinition")) {
          this.contentDefinition = new ContentDefinitionComponent();
          return this.contentDefinition;
        }
        else if (name.equals("term")) {
          return addTerm();
        }
        else if (name.equals("supportingInfo")) {
          return addSupportingInfo();
        }
        else if (name.equals("relevantHistory")) {
          return addRelevantHistory();
        }
        else if (name.equals("signer")) {
          return addSigner();
        }
        else if (name.equals("friendly")) {
          return addFriendly();
        }
        else if (name.equals("legal")) {
          return addLegal();
        }
        else if (name.equals("rule")) {
          return addRule();
        }
        else if (name.equals("legallyBindingAttachment")) {
          this.legallyBinding = new Attachment();
          return this.legallyBinding;
        }
        else if (name.equals("legallyBindingReference")) {
          this.legallyBinding = new Reference();
          return this.legallyBinding;
        }
        else
          return super.addChild(name);
      }

  public String fhirType() {
    return "Contract";

  }

      public Contract copy() {
        Contract dst = new Contract();
        copyValues(dst);
        if (identifier != null) {
          dst.identifier = new ArrayList<Identifier>();
          for (Identifier i : identifier)
            dst.identifier.add(i.copy());
        };
        dst.url = url == null ? null : url.copy();
        dst.version = version == null ? null : version.copy();
        dst.status = status == null ? null : status.copy();
        dst.legalState = legalState == null ? null : legalState.copy();
        dst.instantiatesCanonical = instantiatesCanonical == null ? null : instantiatesCanonical.copy();
        dst.instantiatesUri = instantiatesUri == null ? null : instantiatesUri.copy();
        dst.contentDerivative = contentDerivative == null ? null : contentDerivative.copy();
        dst.issued = issued == null ? null : issued.copy();
        dst.applies = applies == null ? null : applies.copy();
        dst.expirationType = expirationType == null ? null : expirationType.copy();
        if (subject != null) {
          dst.subject = new ArrayList<Reference>();
          for (Reference i : subject)
            dst.subject.add(i.copy());
        };
        if (authority != null) {
          dst.authority = new ArrayList<Reference>();
          for (Reference i : authority)
            dst.authority.add(i.copy());
        };
        if (domain != null) {
          dst.domain = new ArrayList<Reference>();
          for (Reference i : domain)
            dst.domain.add(i.copy());
        };
        if (site != null) {
          dst.site = new ArrayList<Reference>();
          for (Reference i : site)
            dst.site.add(i.copy());
        };
        dst.name = name == null ? null : name.copy();
        dst.title = title == null ? null : title.copy();
        dst.subtitle = subtitle == null ? null : subtitle.copy();
        if (alias != null) {
          dst.alias = new ArrayList<StringType>();
          for (StringType i : alias)
            dst.alias.add(i.copy());
        };
        dst.author = author == null ? null : author.copy();
        dst.scope = scope == null ? null : scope.copy();
        dst.topic = topic == null ? null : topic.copy();
        dst.type = type == null ? null : type.copy();
        if (subType != null) {
          dst.subType = new ArrayList<CodeableConcept>();
          for (CodeableConcept i : subType)
            dst.subType.add(i.copy());
        };
        dst.contentDefinition = contentDefinition == null ? null : contentDefinition.copy();
        if (term != null) {
          dst.term = new ArrayList<TermComponent>();
          for (TermComponent i : term)
            dst.term.add(i.copy());
        };
        if (supportingInfo != null) {
          dst.supportingInfo = new ArrayList<Reference>();
          for (Reference i : supportingInfo)
            dst.supportingInfo.add(i.copy());
        };
        if (relevantHistory != null) {
          dst.relevantHistory = new ArrayList<Reference>();
          for (Reference i : relevantHistory)
            dst.relevantHistory.add(i.copy());
        };
        if (signer != null) {
          dst.signer = new ArrayList<SignatoryComponent>();
          for (SignatoryComponent i : signer)
            dst.signer.add(i.copy());
        };
        if (friendly != null) {
          dst.friendly = new ArrayList<FriendlyLanguageComponent>();
          for (FriendlyLanguageComponent i : friendly)
            dst.friendly.add(i.copy());
        };
        if (legal != null) {
          dst.legal = new ArrayList<LegalLanguageComponent>();
          for (LegalLanguageComponent i : legal)
            dst.legal.add(i.copy());
        };
        if (rule != null) {
          dst.rule = new ArrayList<ComputableLanguageComponent>();
          for (ComputableLanguageComponent i : rule)
            dst.rule.add(i.copy());
        };
        dst.legallyBinding = legallyBinding == null ? null : legallyBinding.copy();
        return dst;
      }

      protected Contract typedCopy() {
        return copy();
      }

      @Override
      public boolean equalsDeep(Base other_) {
        if (!super.equalsDeep(other_))
          return false;
        if (!(other_ instanceof Contract))
          return false;
        Contract o = (Contract) other_;
        return compareDeep(identifier, o.identifier, true) && compareDeep(url, o.url, true) && compareDeep(version, o.version, true)
           && compareDeep(status, o.status, true) && compareDeep(legalState, o.legalState, true) && compareDeep(instantiatesCanonical, o.instantiatesCanonical, true)
           && compareDeep(instantiatesUri, o.instantiatesUri, true) && compareDeep(contentDerivative, o.contentDerivative, true)
           && compareDeep(issued, o.issued, true) && compareDeep(applies, o.applies, true) && compareDeep(expirationType, o.expirationType, true)
           && compareDeep(subject, o.subject, true) && compareDeep(authority, o.authority, true) && compareDeep(domain, o.domain, true)
           && compareDeep(site, o.site, true) && compareDeep(name, o.name, true) && compareDeep(title, o.title, true)
           && compareDeep(subtitle, o.subtitle, true) && compareDeep(alias, o.alias, true) && compareDeep(author, o.author, true)
           && compareDeep(scope, o.scope, true) && compareDeep(topic, o.topic, true) && compareDeep(type, o.type, true)
           && compareDeep(subType, o.subType, true) && compareDeep(contentDefinition, o.contentDefinition, true)
           && compareDeep(term, o.term, true) && compareDeep(supportingInfo, o.supportingInfo, true) && compareDeep(relevantHistory, o.relevantHistory, true)
           && compareDeep(signer, o.signer, true) && compareDeep(friendly, o.friendly, true) && compareDeep(legal, o.legal, true)
           && compareDeep(rule, o.rule, true) && compareDeep(legallyBinding, o.legallyBinding, true);
      }

      @Override
      public boolean equalsShallow(Base other_) {
        if (!super.equalsShallow(other_))
          return false;
        if (!(other_ instanceof Contract))
          return false;
        Contract o = (Contract) other_;
        return compareValues(url, o.url, true) && compareValues(version, o.version, true) && compareValues(status, o.status, true)
           && compareValues(instantiatesUri, o.instantiatesUri, true) && compareValues(issued, o.issued, true)
           && compareValues(name, o.name, true) && compareValues(title, o.title, true) && compareValues(subtitle, o.subtitle, true)
           && compareValues(alias, o.alias, true);
      }

      public boolean isEmpty() {
        return super.isEmpty() && ca.uhn.fhir.util.ElementUtil.isEmpty(identifier, url, version
          , status, legalState, instantiatesCanonical, instantiatesUri, contentDerivative, issued
          , applies, expirationType, subject, authority, domain, site, name, title, subtitle
          , alias, author, scope, topic, type, subType, contentDefinition, term, supportingInfo
          , relevantHistory, signer, friendly, legal, rule, legallyBinding);
      }

  @Override
  public ResourceType getResourceType() {
    return ResourceType.Contract;
   }

 /**
   * Search parameter: <b>identifier</b>
   * <p>
   * Description: <b>The identity of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.identifier</b><br>
   * </p>
   */
  @SearchParamDefinition(name="identifier", path="Contract.identifier", description="The identity of the contract", type="token" )
  public static final String SP_IDENTIFIER = "identifier";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>identifier</b>
   * <p>
   * Description: <b>The identity of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.identifier</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam IDENTIFIER = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_IDENTIFIER);

 /**
   * Search parameter: <b>instantiates</b>
   * <p>
   * Description: <b>A source definition of the contract</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Contract.instantiatesUri</b><br>
   * </p>
   */
  @SearchParamDefinition(name="instantiates", path="Contract.instantiatesUri", description="A source definition of the contract", type="uri" )
  public static final String SP_INSTANTIATES = "instantiates";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>instantiates</b>
   * <p>
   * Description: <b>A source definition of the contract</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Contract.instantiatesUri</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam INSTANTIATES = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_INSTANTIATES);

 /**
   * Search parameter: <b>patient</b>
   * <p>
   * Description: <b>The identity of the subject of the contract (if a patient)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="patient", path="Contract.subject.where(resolve() is Patient)", description="The identity of the subject of the contract (if a patient)", type="reference", target={Patient.class } )
  public static final String SP_PATIENT = "patient";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>patient</b>
   * <p>
   * Description: <b>The identity of the subject of the contract (if a patient)</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam PATIENT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_PATIENT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:patient</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_PATIENT = new ca.uhn.fhir.model.api.Include("Contract:patient").toLocked();

 /**
   * Search parameter: <b>subject</b>
   * <p>
   * Description: <b>The identity of the subject of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  @SearchParamDefinition(name="subject", path="Contract.subject", description="The identity of the subject of the contract", type="reference" )
  public static final String SP_SUBJECT = "subject";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>subject</b>
   * <p>
   * Description: <b>The identity of the subject of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.subject</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SUBJECT = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SUBJECT);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:subject</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SUBJECT = new ca.uhn.fhir.model.api.Include("Contract:subject").toLocked();

 /**
   * Search parameter: <b>authority</b>
   * <p>
   * Description: <b>The authority of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.authority</b><br>
   * </p>
   */
  @SearchParamDefinition(name="authority", path="Contract.authority", description="The authority of the contract", type="reference", target={Organization.class } )
  public static final String SP_AUTHORITY = "authority";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>authority</b>
   * <p>
   * Description: <b>The authority of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.authority</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam AUTHORITY = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_AUTHORITY);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:authority</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_AUTHORITY = new ca.uhn.fhir.model.api.Include("Contract:authority").toLocked();

 /**
   * Search parameter: <b>domain</b>
   * <p>
   * Description: <b>The domain of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.domain</b><br>
   * </p>
   */
  @SearchParamDefinition(name="domain", path="Contract.domain", description="The domain of the contract", type="reference", target={Location.class } )
  public static final String SP_DOMAIN = "domain";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>domain</b>
   * <p>
   * Description: <b>The domain of the contract</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.domain</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam DOMAIN = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_DOMAIN);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:domain</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_DOMAIN = new ca.uhn.fhir.model.api.Include("Contract:domain").toLocked();

 /**
   * Search parameter: <b>issued</b>
   * <p>
   * Description: <b>The date/time the contract was issued</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Contract.issued</b><br>
   * </p>
   */
  @SearchParamDefinition(name="issued", path="Contract.issued", description="The date/time the contract was issued", type="date" )
  public static final String SP_ISSUED = "issued";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>issued</b>
   * <p>
   * Description: <b>The date/time the contract was issued</b><br>
   * Type: <b>date</b><br>
   * Path: <b>Contract.issued</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.DateClientParam ISSUED = new ca.uhn.fhir.rest.gclient.DateClientParam(SP_ISSUED);

 /**
   * Search parameter: <b>url</b>
   * <p>
   * Description: <b>The basal contract definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Contract.url</b><br>
   * </p>
   */
  @SearchParamDefinition(name="url", path="Contract.url", description="The basal contract definition", type="uri" )
  public static final String SP_URL = "url";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>url</b>
   * <p>
   * Description: <b>The basal contract definition</b><br>
   * Type: <b>uri</b><br>
   * Path: <b>Contract.url</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.UriClientParam URL = new ca.uhn.fhir.rest.gclient.UriClientParam(SP_URL);

 /**
   * Search parameter: <b>signer</b>
   * <p>
   * Description: <b>Contract Signatory Party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.signer.party</b><br>
   * </p>
   */
  @SearchParamDefinition(name="signer", path="Contract.signer.party", description="Contract Signatory Party", type="reference", target={Organization.class, Patient.class, Practitioner.class, PractitionerRole.class, RelatedPerson.class } )
  public static final String SP_SIGNER = "signer";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>signer</b>
   * <p>
   * Description: <b>Contract Signatory Party</b><br>
   * Type: <b>reference</b><br>
   * Path: <b>Contract.signer.party</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.ReferenceClientParam SIGNER = new ca.uhn.fhir.rest.gclient.ReferenceClientParam(SP_SIGNER);

/**
   * Constant for fluent queries to be used to add include statements. Specifies
   * the path value of "<b>Contract:signer</b>".
   */
  public static final ca.uhn.fhir.model.api.Include INCLUDE_SIGNER = new ca.uhn.fhir.model.api.Include("Contract:signer").toLocked();

 /**
   * Search parameter: <b>status</b>
   * <p>
   * Description: <b>The status of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.status</b><br>
   * </p>
   */
  @SearchParamDefinition(name="status", path="Contract.status", description="The status of the contract", type="token" )
  public static final String SP_STATUS = "status";
 /**
   * <b>Fluent Client</b> search parameter constant for <b>status</b>
   * <p>
   * Description: <b>The status of the contract</b><br>
   * Type: <b>token</b><br>
   * Path: <b>Contract.status</b><br>
   * </p>
   */
  public static final ca.uhn.fhir.rest.gclient.TokenClientParam STATUS = new ca.uhn.fhir.rest.gclient.TokenClientParam(SP_STATUS);


}

