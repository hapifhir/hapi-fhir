package org.hl7.fhir.r4.model.codesystems;

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


import org.hl7.fhir.exceptions.FHIRException;

public enum V3Hl7VoteResolution {

        /**
         * Description: An abstract concept grouping resolutions that can be applied to affirmative ballot comments.
         */
        AFFIRMATIVERESOLUTION, 
        /**
         * Description: The recommended change has been deferred to consideration for a future release.
         */
        AFFDEF, 
        /**
         * Description: The recommended change has been incorporated or identified issue has been answered.
         */
        AFFI, 
        /**
         * Description: The recommended change has been refused and is not expected to be incorporated.
         */
        AFFR, 
        /**
         * Description: An abstract concept grouping resolutions that can be applied to negative ballot comments.
         */
        NEGATIVERESOLUTION, 
        /**
         * Description: Responsible group has recommended that the negative vote be considered non-substantive.  (Issue raised does not provide sufficiently convincing reason to make changes to the item under ballot, or otherwise impede its adoption.)
         */
        NONSUBP, 
        /**
         * Description: Ballot group has voted and declared the negative vote non-substantive.
         */
        NONSUBV, 
        /**
         * Description: Responsible group has recommended that the negative vote be considered not-related.  (Issue raised is not related to the current scope of the item under ballot, or does not prevent the item under ballot for being used for its defined intent.  Recommended changes may be considered as part of future versions.)  (Perhaps after further reading or explanation).
         */
        NOTRELP, 
        /**
         * Description: Ballot group has voted and declared the negative vote non-related.
         */
        NOTRELV, 
        /**
         * Description: Committee identifies that the same issue has been raised as part of a previous ballot on the same element version and was found by the ballot group to be non-substantive or not related.)
         */
        PREVCONS, 
        /**
         * Description: Voter has formally withdrawn their vote or comment as having been in error.  (Perhaps after further reading or explanation).
         */
        RETRACT, 
        /**
         * Description: Vote has not yet gone through resolution.
         */
        UNRESOLVED, 
        /**
         * Description: Voter has formally withdrawn their vote or comment on the basis of agreed changes or proposed future changes.
         */
        WITHDRAW, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Hl7VoteResolution fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("affirmativeResolution".equals(codeString))
          return AFFIRMATIVERESOLUTION;
        if ("affdef".equals(codeString))
          return AFFDEF;
        if ("affi".equals(codeString))
          return AFFI;
        if ("affr".equals(codeString))
          return AFFR;
        if ("negativeResolution".equals(codeString))
          return NEGATIVERESOLUTION;
        if ("nonsubp".equals(codeString))
          return NONSUBP;
        if ("nonsubv".equals(codeString))
          return NONSUBV;
        if ("notrelp".equals(codeString))
          return NOTRELP;
        if ("notrelv".equals(codeString))
          return NOTRELV;
        if ("prevcons".equals(codeString))
          return PREVCONS;
        if ("retract".equals(codeString))
          return RETRACT;
        if ("unresolved".equals(codeString))
          return UNRESOLVED;
        if ("withdraw".equals(codeString))
          return WITHDRAW;
        throw new FHIRException("Unknown V3Hl7VoteResolution code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AFFIRMATIVERESOLUTION: return "affirmativeResolution";
            case AFFDEF: return "affdef";
            case AFFI: return "affi";
            case AFFR: return "affr";
            case NEGATIVERESOLUTION: return "negativeResolution";
            case NONSUBP: return "nonsubp";
            case NONSUBV: return "nonsubv";
            case NOTRELP: return "notrelp";
            case NOTRELV: return "notrelv";
            case PREVCONS: return "prevcons";
            case RETRACT: return "retract";
            case UNRESOLVED: return "unresolved";
            case WITHDRAW: return "withdraw";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-hl7VoteResolution";
        }
        public String getDefinition() {
          switch (this) {
            case AFFIRMATIVERESOLUTION: return "Description: An abstract concept grouping resolutions that can be applied to affirmative ballot comments.";
            case AFFDEF: return "Description: The recommended change has been deferred to consideration for a future release.";
            case AFFI: return "Description: The recommended change has been incorporated or identified issue has been answered.";
            case AFFR: return "Description: The recommended change has been refused and is not expected to be incorporated.";
            case NEGATIVERESOLUTION: return "Description: An abstract concept grouping resolutions that can be applied to negative ballot comments.";
            case NONSUBP: return "Description: Responsible group has recommended that the negative vote be considered non-substantive.  (Issue raised does not provide sufficiently convincing reason to make changes to the item under ballot, or otherwise impede its adoption.)";
            case NONSUBV: return "Description: Ballot group has voted and declared the negative vote non-substantive.";
            case NOTRELP: return "Description: Responsible group has recommended that the negative vote be considered not-related.  (Issue raised is not related to the current scope of the item under ballot, or does not prevent the item under ballot for being used for its defined intent.  Recommended changes may be considered as part of future versions.)  (Perhaps after further reading or explanation).";
            case NOTRELV: return "Description: Ballot group has voted and declared the negative vote non-related.";
            case PREVCONS: return "Description: Committee identifies that the same issue has been raised as part of a previous ballot on the same element version and was found by the ballot group to be non-substantive or not related.)";
            case RETRACT: return "Description: Voter has formally withdrawn their vote or comment as having been in error.  (Perhaps after further reading or explanation).";
            case UNRESOLVED: return "Description: Vote has not yet gone through resolution.";
            case WITHDRAW: return "Description: Voter has formally withdrawn their vote or comment on the basis of agreed changes or proposed future changes.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AFFIRMATIVERESOLUTION: return "affirmative resolution";
            case AFFDEF: return "affirmative-deferred";
            case AFFI: return "affirmative-incorporated";
            case AFFR: return "affirmative-rejected";
            case NEGATIVERESOLUTION: return "negative resolution";
            case NONSUBP: return "non-substantive proposed";
            case NONSUBV: return "non-substantive voted";
            case NOTRELP: return "not related proposed";
            case NOTRELV: return "not related voted";
            case PREVCONS: return "previously considered";
            case RETRACT: return "retracted";
            case UNRESOLVED: return "unresolved";
            case WITHDRAW: return "withdrawn";
            default: return "?";
          }
    }


}

