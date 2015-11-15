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


public enum V3ActRelationshipSubset {

        /**
         * Used to indicate that the participation is a filtered subset of the total participations of the same type owned by the Act. 

                        Used when there is a need to limit the participations to the first, the last, the next or some other filtered subset.
         */
        _PARTICIPATIONSUBSET, 
        /**
         * An occurrence that is scheduled to occur in the future. An Act whose effective time is greater than 'now', where 'now' is the time the instance is authored.
         */
        FUTURE, 
        /**
         * Represents a 'summary' of all acts that are scheduled to occur in the future (whose effective time is greater than 'now' where is the time the instance is authored.). The effectiveTime represents the outer boundary of all occurrences, repeatNumber represents the total number of repetitions, etc.
         */
        FUTSUM, 
        /**
         * Restricted to the latest known occurrence that is scheduled to occur. The Act with the highest known effective time.
         */
        LAST, 
        /**
         * Restricted to the nearest recent known occurrence scheduled to occur in the future. The Act with the lowest effective time, still greater than 'now'. ('now' is the time the instance is authored.)
         */
        NEXT, 
        /**
         * An occurrence that occurred or was scheduled to occur in the past. An Act whose effective time is less than 'now'. ('now' is the time the instance is authored.)
         */
        PAST, 
        /**
         * Restricted to the earliest known occurrence that occurred or was scheduled to occur in the past. The Act with the lowest effective time. ('now' is the time the instance is authored.)
         */
        FIRST, 
        /**
         * Represents a 'summary' of all acts that previously occurred or were scheduled to occur. The effectiveTime represents the outer boundary of all occurrences, repeatNumber represents the total number of repetitions, etc. ('now' is the time the instance is authored.)
         */
        PREVSUM, 
        /**
         * Restricted to the most recent known occurrence that occurred or was scheduled to occur in the past. The Act with the most recent effective time, still less than 'now'. ('now' is the time the instance is authored.)
         */
        RECENT, 
        /**
         * Represents a 'summary' of all acts that have occurred or were scheduled to occur and which are scheduled to occur in the future. The effectiveTime represents the outer boundary of all occurrences, repeatNumber represents the total number of repetitions, etc.
         */
        SUM, 
        /**
         * ActRelationshipExpectedSubset
         */
        ACTRELATIONSHIPEXPECTEDSUBSET, 
        /**
         * ActRelationshipPastSubset
         */
        ACTRELATIONSHIPPASTSUBSET, 
        /**
         * The occurrence whose value attribute is greater than all other occurrences at the time the instance is created.
         */
        MAX, 
        /**
         * The occurrence whose value attribute is less than all other occurrences at the time the instance is created.
         */
        MIN, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActRelationshipSubset fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ParticipationSubset".equals(codeString))
          return _PARTICIPATIONSUBSET;
        if ("FUTURE".equals(codeString))
          return FUTURE;
        if ("FUTSUM".equals(codeString))
          return FUTSUM;
        if ("LAST".equals(codeString))
          return LAST;
        if ("NEXT".equals(codeString))
          return NEXT;
        if ("PAST".equals(codeString))
          return PAST;
        if ("FIRST".equals(codeString))
          return FIRST;
        if ("PREVSUM".equals(codeString))
          return PREVSUM;
        if ("RECENT".equals(codeString))
          return RECENT;
        if ("SUM".equals(codeString))
          return SUM;
        if ("ActRelationshipExpectedSubset".equals(codeString))
          return ACTRELATIONSHIPEXPECTEDSUBSET;
        if ("ActRelationshipPastSubset".equals(codeString))
          return ACTRELATIONSHIPPASTSUBSET;
        if ("MAX".equals(codeString))
          return MAX;
        if ("MIN".equals(codeString))
          return MIN;
        throw new Exception("Unknown V3ActRelationshipSubset code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _PARTICIPATIONSUBSET: return "_ParticipationSubset";
            case FUTURE: return "FUTURE";
            case FUTSUM: return "FUTSUM";
            case LAST: return "LAST";
            case NEXT: return "NEXT";
            case PAST: return "PAST";
            case FIRST: return "FIRST";
            case PREVSUM: return "PREVSUM";
            case RECENT: return "RECENT";
            case SUM: return "SUM";
            case ACTRELATIONSHIPEXPECTEDSUBSET: return "ActRelationshipExpectedSubset";
            case ACTRELATIONSHIPPASTSUBSET: return "ActRelationshipPastSubset";
            case MAX: return "MAX";
            case MIN: return "MIN";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActRelationshipSubset";
        }
        public String getDefinition() {
          switch (this) {
            case _PARTICIPATIONSUBSET: return "Used to indicate that the participation is a filtered subset of the total participations of the same type owned by the Act. \r\n\n                        Used when there is a need to limit the participations to the first, the last, the next or some other filtered subset.";
            case FUTURE: return "An occurrence that is scheduled to occur in the future. An Act whose effective time is greater than 'now', where 'now' is the time the instance is authored.";
            case FUTSUM: return "Represents a 'summary' of all acts that are scheduled to occur in the future (whose effective time is greater than 'now' where is the time the instance is authored.). The effectiveTime represents the outer boundary of all occurrences, repeatNumber represents the total number of repetitions, etc.";
            case LAST: return "Restricted to the latest known occurrence that is scheduled to occur. The Act with the highest known effective time.";
            case NEXT: return "Restricted to the nearest recent known occurrence scheduled to occur in the future. The Act with the lowest effective time, still greater than 'now'. ('now' is the time the instance is authored.)";
            case PAST: return "An occurrence that occurred or was scheduled to occur in the past. An Act whose effective time is less than 'now'. ('now' is the time the instance is authored.)";
            case FIRST: return "Restricted to the earliest known occurrence that occurred or was scheduled to occur in the past. The Act with the lowest effective time. ('now' is the time the instance is authored.)";
            case PREVSUM: return "Represents a 'summary' of all acts that previously occurred or were scheduled to occur. The effectiveTime represents the outer boundary of all occurrences, repeatNumber represents the total number of repetitions, etc. ('now' is the time the instance is authored.)";
            case RECENT: return "Restricted to the most recent known occurrence that occurred or was scheduled to occur in the past. The Act with the most recent effective time, still less than 'now'. ('now' is the time the instance is authored.)";
            case SUM: return "Represents a 'summary' of all acts that have occurred or were scheduled to occur and which are scheduled to occur in the future. The effectiveTime represents the outer boundary of all occurrences, repeatNumber represents the total number of repetitions, etc.";
            case ACTRELATIONSHIPEXPECTEDSUBSET: return "ActRelationshipExpectedSubset";
            case ACTRELATIONSHIPPASTSUBSET: return "ActRelationshipPastSubset";
            case MAX: return "The occurrence whose value attribute is greater than all other occurrences at the time the instance is created.";
            case MIN: return "The occurrence whose value attribute is less than all other occurrences at the time the instance is created.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _PARTICIPATIONSUBSET: return "ParticipationSubset";
            case FUTURE: return "expected future";
            case FUTSUM: return "future summary";
            case LAST: return "expected last";
            case NEXT: return "expected next";
            case PAST: return "previous";
            case FIRST: return "first known";
            case PREVSUM: return "previous summary";
            case RECENT: return "most recent";
            case SUM: return "summary";
            case ACTRELATIONSHIPEXPECTEDSUBSET: return "ActRelationshipExpectedSubset";
            case ACTRELATIONSHIPPASTSUBSET: return "ActRelationshipPastSubset";
            case MAX: return "maximum";
            case MIN: return "minimum";
            default: return "?";
          }
    }


}

