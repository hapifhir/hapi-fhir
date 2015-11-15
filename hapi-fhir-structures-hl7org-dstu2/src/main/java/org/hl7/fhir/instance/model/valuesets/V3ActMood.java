package org.hl7.fhir.instance.model.valuesets;

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

// Generated on Wed, Nov 11, 2015 10:54-0500 for FHIR v1.0.2


public enum V3ActMood {

        /**
         * These are moods describing activities as they progress in the business cycle, from defined, through planned and ordered to completed.
         */
        _ACTMOODCOMPLETIONTRACK, 
        /**
         * Definition: A possible act.
         */
        _ACTMOODPOTENTIAL, 
        /**
         * Definition: A definition of a kind of act that can occur .

                        
                           OpenIssue: The semantic constructs embodied in DEF and CRT moods seem indistinguishable, and their uses can readily be determined by the context in which these are used. Therefore, this OpenIssue has been created to declare that it is likely that ActMood.DEF will be "retired" in the future in favor of the more general ActMood.CRT.
         */
        DEF, 
        /**
         * Definition: A kind of act that defines a permission that has been granted.
         */
        PERM, 
        /**
         * Definition: A kind of act that may occur during the specified time period.
         */
        SLOT, 
        /**
         * Definition: An act that actually happens (may be an ongoing act or a documentation of a past act).
         */
        EVN, 
        /**
         * Definition: An intention or plan for an act. 

                        
                           >UsageNotes: The final outcome of the intent, the act that is intended to occur, is always an event. However the final outcome may be reached indirectly via steps through other intents, such as promise, permission request, or an appointment that may lead to an actual event to occur. Alternatively, the intended act may never occur.
         */
        INT, 
        /**
         * Definition:  A desire to have an act occur.
         */
        _ACTMOODDESIRE, 
        /**
         * Definition: A request (or order) for an act that is part of a defined request/fulfillment cycle.

                        
                           UsageNotes: Use of an HL7 defined request/fulfillment framework is not required to use this mood code.
         */
        _ACTMOODACTREQUEST, 
        /**
         * Definition: A request act that is specialized for the appointment scheduling request/fulfillment cycle. An appointment request is fulfilled only and completely by an appointment (APT), i.e., all that the appointment request intends is to create an appointment (the actual act may well not happen if that is the professional decision during the appointment).
         */
        ARQ, 
        /**
         * Definition: A request for a permission to perform the act. Typically a payer (or possibly a supervisor) is being requested to give permission to perform the act. As opposed to the RQO, the requestee is not asked to perform or cause to perform the act but only to give the permission.
         */
        PERMRQ, 
        /**
         * Definition: A request act that is specialized for an event request/fulfillment cycle. 

                        
                           UsageNotes: The fulfillment cycle may involve intermediary fulfilling acts in moods such as PRMS, APT, or even another RQO before being fulfilled by the final event. 

                        
                           UsageNotes: The concepts of a "request" and an "order" are viewed as different, because there is an implication of a mandate associated with order.  In practice, however, this distinction has no general functional value in the inter-operation of health care computing.  "Orders" are commonly refused for a variety of clinical and business reasons, and the notion of a "request" obligates the recipient (the fulfiller) to respond to the sender (the author).  Indeed, in many regions, including Australia and Europe, the common term used is "request."

                        Thus, the concept embodies both notions, as there is no useful distinction to be made.  If a mandate is to be associated with a request, this will be embodied in the "local" business rules applied to the transactions.  Should HL7 desire to provide a distinction between these in the future, the individual concepts could be added as specializations of this concept.

                        The critical distinction here, is the difference between this concept and an "intent", of which it is a specialization.  An intent involves decisions by a single party, the author.  A request, however, involves decisions by two parties, the author and the fulfiller, with an obligation on the part of the fulfiller to respond to the request indicating that the fulfiller will indeed fulfill the request.
         */
        RQO, 
        /**
         * Definition: A suggestion that an act might be performed. Not an explicit request, and professional responsibility may or may not be present.
         */
        PRP, 
        /**
         * Definition: A suggestion that an act should be performed with an acceptance of some degree of professional responsibility for the resulting act. Not an explicit request. .

                        
                           UsageNotes: Where there is no clear definition or applicable concept of "professional responsibilityâ€?, RMD becomes indistinguishable from PRP. .
         */
        RMD, 
        /**
         * Definition: A commitment to perform an act (may be either solicited or unsolicited). The committer becomes responsible to the other party for executing the act, and, as a consequence, the other party may rely on the first party to perform or cause to perform the act.

                        
                           UsageNotes: Commitments may be retracted or cancelled.
         */
        PRMS, 
        /**
         * Definition: An act that has been scheduled to be performed at a specific place and time.
         */
        APT, 
        /**
         * Definition: An act that expresses condition statements for other acts.
         */
        _ACTMOODPREDICATE, 
        /**
         * Deprecation Comment: 
                           This concept This codes should no longer be used.  Instead, set attribute Act.isCriterionInd to "true" and use the desired mood for your criterion.

                        
                           Definition: A condition that must be true for the source act to be considered.
         */
        CRT, 
        /**
         * Deprecation Comment: 
                           This concept This codes should no longer be used.  Instead, set attribute Act.isCriterionInd to "true" and use the desired mood for your criterion.

                        
                           Definition: A criterion (CRT) that has_match = an event (EVN).
         */
        EVN_CRT, 
        /**
         * A criterion expressed over goals (ActMood.GOL).
         */
        GOL_CRT, 
        /**
         * A criterion expressed over intents (ActMood.INT).
         */
        INT_CRT, 
        /**
         * A criterion expressed over promises (ActMood.PRMS).
         */
        PRMS_CRT, 
        /**
         * A criterion expressed over requests or orders (ActMood.RQO).
         */
        RQO_CRT, 
        /**
         * A criterion expressed over risks (ActMood.RSK).
         */
        RSK_CRT, 
        /**
         * Definition: An act that is considered to have some noteworthy likelihood of occurring in the future (has_match = event).

                        
                           Examples:Prognosis of a condition, Expected date of discharge from hospital, patient will likely need an emergency decompression of the intracranial pressure by morning.

                        
                           UsageNotes:INT (intent) reflects a plan for the future, which is a declaration to do something. This contrasts with expectation, which is a prediction that something will happen in the future. GOL (goal) reflects a hope rather than a prediction. RSK (risk) reflects a potential negative event that may or may not be expected to happen.
         */
        EXPEC, 
        /**
         * Definition: An expectation that is considered to be desirable to occur in the future 

                        
                           Examples:Target weight below 80Kg, Stop smoking, Regain ability to walk, goal is to administer thrombolytics to candidate patients presenting with acute myocardial infarction.

                        
                           UsageNotes: INT (intent) reflects a plan for the future, which is a declaration to do something.  This contrasts with goal which doesn't represent an intention to act, merely a hope for an eventual result.  A goal is distinct from the intended actions to reach that goal.  "I will reduce the dose of drug x to 20mg" is an intent.  "I hope to be able to get the patient to the point where I can reduce the dose of drug x to 20mg" is a goal. EXPEC (expectation) reflects a prediction rather than a hope. RSK (risk) reflects a potential negative event rather than a hope.
         */
        GOL, 
        /**
         * Definition:An act that may occur in the future and which is regarded as undesirable 

                        
                           Examples:Increased risk of DVT, at risk for sub-acute bacterial endocarditis.

                        
                           UsageNotes:Note: An observation in RSK mood expresses the undesirable act, and not the underlying risk factor. A risk factor that is present (e.g. obesity, smoking, etc.) should be expressed in event mood. INT (intent) reflects a plan for the future, which is a declaration to do something. This contrasts with RSK (risk), which is the potential that something negative will occur that may or may not ever happen. GOL (goal) reflects a hope to achieve something. EXPEC (expectation) is the prediction of a positive or negative event. This contrasts with RSK (risk), which is the potential that something negative will occur that may or may not ever happen, and may not be expected to happen.
         */
        RSK, 
        /**
         * Definition: One of a set of acts that specify an option for the property values that the parent act may have. Typically used in definitions or orders to describe alternatives. An option can only be used as a group, that is, all assigned values must be used together. The actual mood of the act is the same as the parent act, and they must be linked by an actrelationship with type = OPTN.
         */
        OPT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActMood fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ActMoodCompletionTrack".equals(codeString))
          return _ACTMOODCOMPLETIONTRACK;
        if ("_ActMoodPotential".equals(codeString))
          return _ACTMOODPOTENTIAL;
        if ("DEF".equals(codeString))
          return DEF;
        if ("PERM".equals(codeString))
          return PERM;
        if ("SLOT".equals(codeString))
          return SLOT;
        if ("EVN".equals(codeString))
          return EVN;
        if ("INT".equals(codeString))
          return INT;
        if ("_ActMoodDesire".equals(codeString))
          return _ACTMOODDESIRE;
        if ("_ActMoodActRequest".equals(codeString))
          return _ACTMOODACTREQUEST;
        if ("ARQ".equals(codeString))
          return ARQ;
        if ("PERMRQ".equals(codeString))
          return PERMRQ;
        if ("RQO".equals(codeString))
          return RQO;
        if ("PRP".equals(codeString))
          return PRP;
        if ("RMD".equals(codeString))
          return RMD;
        if ("PRMS".equals(codeString))
          return PRMS;
        if ("APT".equals(codeString))
          return APT;
        if ("_ActMoodPredicate".equals(codeString))
          return _ACTMOODPREDICATE;
        if ("CRT".equals(codeString))
          return CRT;
        if ("EVN.CRT".equals(codeString))
          return EVN_CRT;
        if ("GOL.CRT".equals(codeString))
          return GOL_CRT;
        if ("INT.CRT".equals(codeString))
          return INT_CRT;
        if ("PRMS.CRT".equals(codeString))
          return PRMS_CRT;
        if ("RQO.CRT".equals(codeString))
          return RQO_CRT;
        if ("RSK.CRT".equals(codeString))
          return RSK_CRT;
        if ("EXPEC".equals(codeString))
          return EXPEC;
        if ("GOL".equals(codeString))
          return GOL;
        if ("RSK".equals(codeString))
          return RSK;
        if ("OPT".equals(codeString))
          return OPT;
        throw new Exception("Unknown V3ActMood code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ACTMOODCOMPLETIONTRACK: return "_ActMoodCompletionTrack";
            case _ACTMOODPOTENTIAL: return "_ActMoodPotential";
            case DEF: return "DEF";
            case PERM: return "PERM";
            case SLOT: return "SLOT";
            case EVN: return "EVN";
            case INT: return "INT";
            case _ACTMOODDESIRE: return "_ActMoodDesire";
            case _ACTMOODACTREQUEST: return "_ActMoodActRequest";
            case ARQ: return "ARQ";
            case PERMRQ: return "PERMRQ";
            case RQO: return "RQO";
            case PRP: return "PRP";
            case RMD: return "RMD";
            case PRMS: return "PRMS";
            case APT: return "APT";
            case _ACTMOODPREDICATE: return "_ActMoodPredicate";
            case CRT: return "CRT";
            case EVN_CRT: return "EVN.CRT";
            case GOL_CRT: return "GOL.CRT";
            case INT_CRT: return "INT.CRT";
            case PRMS_CRT: return "PRMS.CRT";
            case RQO_CRT: return "RQO.CRT";
            case RSK_CRT: return "RSK.CRT";
            case EXPEC: return "EXPEC";
            case GOL: return "GOL";
            case RSK: return "RSK";
            case OPT: return "OPT";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActMood";
        }
        public String getDefinition() {
          switch (this) {
            case _ACTMOODCOMPLETIONTRACK: return "These are moods describing activities as they progress in the business cycle, from defined, through planned and ordered to completed.";
            case _ACTMOODPOTENTIAL: return "Definition: A possible act.";
            case DEF: return "Definition: A definition of a kind of act that can occur .\r\n\n                        \n                           OpenIssue: The semantic constructs embodied in DEF and CRT moods seem indistinguishable, and their uses can readily be determined by the context in which these are used. Therefore, this OpenIssue has been created to declare that it is likely that ActMood.DEF will be \"retired\" in the future in favor of the more general ActMood.CRT.";
            case PERM: return "Definition: A kind of act that defines a permission that has been granted.";
            case SLOT: return "Definition: A kind of act that may occur during the specified time period.";
            case EVN: return "Definition: An act that actually happens (may be an ongoing act or a documentation of a past act).";
            case INT: return "Definition: An intention or plan for an act. \r\n\n                        \n                           >UsageNotes: The final outcome of the intent, the act that is intended to occur, is always an event. However the final outcome may be reached indirectly via steps through other intents, such as promise, permission request, or an appointment that may lead to an actual event to occur. Alternatively, the intended act may never occur.";
            case _ACTMOODDESIRE: return "Definition:  A desire to have an act occur.";
            case _ACTMOODACTREQUEST: return "Definition: A request (or order) for an act that is part of a defined request/fulfillment cycle.\r\n\n                        \n                           UsageNotes: Use of an HL7 defined request/fulfillment framework is not required to use this mood code.";
            case ARQ: return "Definition: A request act that is specialized for the appointment scheduling request/fulfillment cycle. An appointment request is fulfilled only and completely by an appointment (APT), i.e., all that the appointment request intends is to create an appointment (the actual act may well not happen if that is the professional decision during the appointment).";
            case PERMRQ: return "Definition: A request for a permission to perform the act. Typically a payer (or possibly a supervisor) is being requested to give permission to perform the act. As opposed to the RQO, the requestee is not asked to perform or cause to perform the act but only to give the permission.";
            case RQO: return "Definition: A request act that is specialized for an event request/fulfillment cycle. \r\n\n                        \n                           UsageNotes: The fulfillment cycle may involve intermediary fulfilling acts in moods such as PRMS, APT, or even another RQO before being fulfilled by the final event. \r\n\n                        \n                           UsageNotes: The concepts of a \"request\" and an \"order\" are viewed as different, because there is an implication of a mandate associated with order.  In practice, however, this distinction has no general functional value in the inter-operation of health care computing.  \"Orders\" are commonly refused for a variety of clinical and business reasons, and the notion of a \"request\" obligates the recipient (the fulfiller) to respond to the sender (the author).  Indeed, in many regions, including Australia and Europe, the common term used is \"request.\"\r\n\n                        Thus, the concept embodies both notions, as there is no useful distinction to be made.  If a mandate is to be associated with a request, this will be embodied in the \"local\" business rules applied to the transactions.  Should HL7 desire to provide a distinction between these in the future, the individual concepts could be added as specializations of this concept.\r\n\n                        The critical distinction here, is the difference between this concept and an \"intent\", of which it is a specialization.  An intent involves decisions by a single party, the author.  A request, however, involves decisions by two parties, the author and the fulfiller, with an obligation on the part of the fulfiller to respond to the request indicating that the fulfiller will indeed fulfill the request.";
            case PRP: return "Definition: A suggestion that an act might be performed. Not an explicit request, and professional responsibility may or may not be present.";
            case RMD: return "Definition: A suggestion that an act should be performed with an acceptance of some degree of professional responsibility for the resulting act. Not an explicit request. .\r\n\n                        \n                           UsageNotes: Where there is no clear definition or applicable concept of \"professional responsibilityâ€?, RMD becomes indistinguishable from PRP. .";
            case PRMS: return "Definition: A commitment to perform an act (may be either solicited or unsolicited). The committer becomes responsible to the other party for executing the act, and, as a consequence, the other party may rely on the first party to perform or cause to perform the act.\r\n\n                        \n                           UsageNotes: Commitments may be retracted or cancelled.";
            case APT: return "Definition: An act that has been scheduled to be performed at a specific place and time.";
            case _ACTMOODPREDICATE: return "Definition: An act that expresses condition statements for other acts.";
            case CRT: return "Deprecation Comment: \n                           This concept This codes should no longer be used.  Instead, set attribute Act.isCriterionInd to \"true\" and use the desired mood for your criterion.\r\n\n                        \n                           Definition: A condition that must be true for the source act to be considered.";
            case EVN_CRT: return "Deprecation Comment: \n                           This concept This codes should no longer be used.  Instead, set attribute Act.isCriterionInd to \"true\" and use the desired mood for your criterion.\r\n\n                        \n                           Definition: A criterion (CRT) that has_match = an event (EVN).";
            case GOL_CRT: return "A criterion expressed over goals (ActMood.GOL).";
            case INT_CRT: return "A criterion expressed over intents (ActMood.INT).";
            case PRMS_CRT: return "A criterion expressed over promises (ActMood.PRMS).";
            case RQO_CRT: return "A criterion expressed over requests or orders (ActMood.RQO).";
            case RSK_CRT: return "A criterion expressed over risks (ActMood.RSK).";
            case EXPEC: return "Definition: An act that is considered to have some noteworthy likelihood of occurring in the future (has_match = event).\r\n\n                        \n                           Examples:Prognosis of a condition, Expected date of discharge from hospital, patient will likely need an emergency decompression of the intracranial pressure by morning.\r\n\n                        \n                           UsageNotes:INT (intent) reflects a plan for the future, which is a declaration to do something. This contrasts with expectation, which is a prediction that something will happen in the future. GOL (goal) reflects a hope rather than a prediction. RSK (risk) reflects a potential negative event that may or may not be expected to happen.";
            case GOL: return "Definition: An expectation that is considered to be desirable to occur in the future \r\n\n                        \n                           Examples:Target weight below 80Kg, Stop smoking, Regain ability to walk, goal is to administer thrombolytics to candidate patients presenting with acute myocardial infarction.\r\n\n                        \n                           UsageNotes: INT (intent) reflects a plan for the future, which is a declaration to do something.  This contrasts with goal which doesn't represent an intention to act, merely a hope for an eventual result.  A goal is distinct from the intended actions to reach that goal.  \"I will reduce the dose of drug x to 20mg\" is an intent.  \"I hope to be able to get the patient to the point where I can reduce the dose of drug x to 20mg\" is a goal. EXPEC (expectation) reflects a prediction rather than a hope. RSK (risk) reflects a potential negative event rather than a hope.";
            case RSK: return "Definition:An act that may occur in the future and which is regarded as undesirable \r\n\n                        \n                           Examples:Increased risk of DVT, at risk for sub-acute bacterial endocarditis.\r\n\n                        \n                           UsageNotes:Note: An observation in RSK mood expresses the undesirable act, and not the underlying risk factor. A risk factor that is present (e.g. obesity, smoking, etc.) should be expressed in event mood. INT (intent) reflects a plan for the future, which is a declaration to do something. This contrasts with RSK (risk), which is the potential that something negative will occur that may or may not ever happen. GOL (goal) reflects a hope to achieve something. EXPEC (expectation) is the prediction of a positive or negative event. This contrasts with RSK (risk), which is the potential that something negative will occur that may or may not ever happen, and may not be expected to happen.";
            case OPT: return "Definition: One of a set of acts that specify an option for the property values that the parent act may have. Typically used in definitions or orders to describe alternatives. An option can only be used as a group, that is, all assigned values must be used together. The actual mood of the act is the same as the parent act, and they must be linked by an actrelationship with type = OPTN.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ACTMOODCOMPLETIONTRACK: return "ActMoodCompletionTrack";
            case _ACTMOODPOTENTIAL: return "potential";
            case DEF: return "definition";
            case PERM: return "permission";
            case SLOT: return "resource slot";
            case EVN: return "event (occurrence)";
            case INT: return "intent";
            case _ACTMOODDESIRE: return "desire";
            case _ACTMOODACTREQUEST: return "act request";
            case ARQ: return "appointment request";
            case PERMRQ: return "permission request";
            case RQO: return "request";
            case PRP: return "proposal";
            case RMD: return "recommendation";
            case PRMS: return "promise";
            case APT: return "appointment";
            case _ACTMOODPREDICATE: return "ActMoodPredicate";
            case CRT: return "criterion";
            case EVN_CRT: return "event criterion";
            case GOL_CRT: return "goal criterion";
            case INT_CRT: return "intent criterion";
            case PRMS_CRT: return "promise criterion";
            case RQO_CRT: return "request criterion";
            case RSK_CRT: return "risk criterion";
            case EXPEC: return "expectation";
            case GOL: return "Goal";
            case RSK: return "risk";
            case OPT: return "option";
            default: return "?";
          }
    }


}

