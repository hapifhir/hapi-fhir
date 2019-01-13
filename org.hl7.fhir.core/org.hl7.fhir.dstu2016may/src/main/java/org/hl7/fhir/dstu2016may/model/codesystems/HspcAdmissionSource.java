package org.hl7.fhir.dstu2016may.model.codesystems;

/*-
 * #%L
 * org.hl7.fhir.dstu2016may
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
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

// Generated on Sun, May 8, 2016 03:05+1000 for FHIR v1.4.0


import org.hl7.fhir.exceptions.FHIRException;

public enum HspcAdmissionSource {

        /**
         * The source of admission is from a clinic.
         */
        _102702, 
        /**
         * The source of admission is from a court or law enforcement
         */
        _17567, 
        /**
         * The source of admission is from an emergency department
         */
        _17566, 
        /**
         * The source of admission is from the individual's place of residence or employment
         */
        _528129525, 
        /**
         * The source of referral is from a health management organization
         */
        _102703, 
        /**
         * The source of admission is a transfer from one area of the hospital to another
         */
        _219107, 
        /**
         * The source of the newborn admission is from a facility not associate with the organization
         */
        _528129526, 
        /**
         * The source of admission is a routine birth of a newborn at an internal birthing facility
         */
        _154642, 
        /**
         * The patient was referred for admission by a physician
         */
        _102701, 
        /**
         * The source of admission is a premature birth at an internal birthing facility
         */
        _528129527, 
        /**
         * The patient was readmitted to the same home health agency as the previous admission
         */
        _510105657, 
        /**
         * The admission is due to an infant becoming ill at an internal neborn facility
         */
        _528129528, 
        /**
         * The source of admission is an external critical care access facility
         */
        _510105655, 
        /**
         * The source of admission is an external hospital
         */
        _102704, 
        /**
         * The source of admission is a transfer from a skilled nursing facility
         */
        _14689616, 
        /**
         * The source of admission is a transfer from an acute care hospital
         */
        _528129529, 
        /**
         * The source of admission is a transfer from an ambulatory surgery facility
         */
        _520442099, 
        /**
         * The source of admission is a transfer from an external facility
         */
        _14690444, 
        /**
         * The source of admission is a transfer from an external home health agency
         */
        _510105656, 
        /**
         * The source of admission is a transfer from a hospice facility
         */
        _528129530, 
        /**
         * The source of admission is a transfer from a psychiatric, substance abuse, or rehabilitation facility
         */
        _510105654, 
        /**
         * added to help the parsers
         */
        NULL;
        public static HspcAdmissionSource fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("102702".equals(codeString))
          return _102702;
        if ("17567".equals(codeString))
          return _17567;
        if ("17566".equals(codeString))
          return _17566;
        if ("528129525".equals(codeString))
          return _528129525;
        if ("102703".equals(codeString))
          return _102703;
        if ("219107".equals(codeString))
          return _219107;
        if ("528129526".equals(codeString))
          return _528129526;
        if ("154642".equals(codeString))
          return _154642;
        if ("102701".equals(codeString))
          return _102701;
        if ("528129527".equals(codeString))
          return _528129527;
        if ("510105657".equals(codeString))
          return _510105657;
        if ("528129528".equals(codeString))
          return _528129528;
        if ("510105655".equals(codeString))
          return _510105655;
        if ("102704".equals(codeString))
          return _102704;
        if ("14689616".equals(codeString))
          return _14689616;
        if ("528129529".equals(codeString))
          return _528129529;
        if ("520442099".equals(codeString))
          return _520442099;
        if ("14690444".equals(codeString))
          return _14690444;
        if ("510105656".equals(codeString))
          return _510105656;
        if ("528129530".equals(codeString))
          return _528129530;
        if ("510105654".equals(codeString))
          return _510105654;
        throw new FHIRException("Unknown HspcAdmissionSource code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _102702: return "102702";
            case _17567: return "17567";
            case _17566: return "17566";
            case _528129525: return "528129525";
            case _102703: return "102703";
            case _219107: return "219107";
            case _528129526: return "528129526";
            case _154642: return "154642";
            case _102701: return "102701";
            case _528129527: return "528129527";
            case _510105657: return "510105657";
            case _528129528: return "528129528";
            case _510105655: return "510105655";
            case _102704: return "102704";
            case _14689616: return "14689616";
            case _528129529: return "528129529";
            case _520442099: return "520442099";
            case _14690444: return "14690444";
            case _510105656: return "510105656";
            case _528129530: return "528129530";
            case _510105654: return "510105654";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/hspc-admissionSource";
        }
        public String getDefinition() {
          switch (this) {
            case _102702: return "The source of admission is from a clinic.";
            case _17567: return "The source of admission is from a court or law enforcement";
            case _17566: return "The source of admission is from an emergency department";
            case _528129525: return "The source of admission is from the individual's place of residence or employment";
            case _102703: return "The source of referral is from a health management organization";
            case _219107: return "The source of admission is a transfer from one area of the hospital to another";
            case _528129526: return "The source of the newborn admission is from a facility not associate with the organization";
            case _154642: return "The source of admission is a routine birth of a newborn at an internal birthing facility";
            case _102701: return "The patient was referred for admission by a physician";
            case _528129527: return "The source of admission is a premature birth at an internal birthing facility";
            case _510105657: return "The patient was readmitted to the same home health agency as the previous admission";
            case _528129528: return "The admission is due to an infant becoming ill at an internal neborn facility";
            case _510105655: return "The source of admission is an external critical care access facility";
            case _102704: return "The source of admission is an external hospital";
            case _14689616: return "The source of admission is a transfer from a skilled nursing facility";
            case _528129529: return "The source of admission is a transfer from an acute care hospital";
            case _520442099: return "The source of admission is a transfer from an ambulatory surgery facility";
            case _14690444: return "The source of admission is a transfer from an external facility";
            case _510105656: return "The source of admission is a transfer from an external home health agency";
            case _528129530: return "The source of admission is a transfer from a hospice facility";
            case _510105654: return "The source of admission is a transfer from a psychiatric, substance abuse, or rehabilitation facility";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _102702: return "Clinic Referral";
            case _17567: return "Court/Law Enforcement";
            case _17566: return "Emergency Room";
            case _528129525: return "From Home Or Work";
            case _102703: return "HMO Referral";
            case _219107: return "In-Hospital Transfer";
            case _528129526: return "Newborn From an Outside Hospital";
            case _154642: return "Normal Newborn";
            case _102701: return "Physician Referral";
            case _528129527: return "Premature newborn within the hospital";
            case _510105657: return "Readmission to Same HHA";
            case _528129528: return "Sick newborn within a hospital";
            case _510105655: return "Transfer From A Critical Access Hospital";
            case _102704: return "Transfer from a Hospital";
            case _14689616: return "Transfer from a SNF";
            case _528129529: return "Transfer From Acute Care Hospital";
            case _520442099: return "Transfer From Ambulatory Surgery Center";
            case _14690444: return "Transfer from Other Facility";
            case _510105656: return "Transfer From Another HHA";
            case _528129530: return "Transfer from hospice";
            case _510105654: return "Transfer From Pysch, Substance Abuse or Rehab Hosp";
            default: return "?";
          }
    }


}

