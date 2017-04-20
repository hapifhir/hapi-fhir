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

public enum ChargeitemBillingcodes {

        /**
         * From German EBM billing system:
Unvorhergesehene Inanspruchnahme des Vertragsarztes durch einen Patienten;zwischen 19:00 und 22:00 Uhr;an Samstagen, Sonntagen und gesetzlichen Feiertagen, am 24.12. und 31.12. zwischen 07:00 und 19:00 Uhr
         */
        _1100, 
        /**
         * From German EBM billing system:
Notfallpauschale im organisierten Not(-fall)dienst und für nicht an der vertragsärztlichen Versorgung teilnehmende Ärzte, Institute und Krankenhäuser bei Inanspruchnahme;zwischen 07:00 und 19:00 Uhr
         */
        _1210, 
        /**
         * From German EBM billing system:
Grundpauschale für Ärzte, Institute und Krankenhäuser, die zur Erbringung von Leistungen innerhalb mindestens eines der Fachgebiete Anästhesiologie, Frauenheilkunde und Geburtshilfe, Haut- und Geschlechtskrankheiten, Mund-, Kiefer- und Gesichtschirurgie und Humangenetik ermächtigt sind
         */
        _1320, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ChargeitemBillingcodes fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1100".equals(codeString))
          return _1100;
        if ("1210".equals(codeString))
          return _1210;
        if ("1320".equals(codeString))
          return _1320;
        throw new FHIRException("Unknown ChargeitemBillingcodes code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1100: return "1100";
            case _1210: return "1210";
            case _1320: return "1320";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/chargeitem-billingcodes";
        }
        public String getDefinition() {
          switch (this) {
            case _1100: return "From German EBM billing system:\nUnvorhergesehene Inanspruchnahme des Vertragsarztes durch einen Patienten;zwischen 19:00 und 22:00 Uhr;an Samstagen, Sonntagen und gesetzlichen Feiertagen, am 24.12. und 31.12. zwischen 07:00 und 19:00 Uhr";
            case _1210: return "From German EBM billing system:\nNotfallpauschale im organisierten Not(-fall)dienst und für nicht an der vertragsärztlichen Versorgung teilnehmende Ärzte, Institute und Krankenhäuser bei Inanspruchnahme;zwischen 07:00 und 19:00 Uhr";
            case _1320: return "From German EBM billing system:\nGrundpauschale für Ärzte, Institute und Krankenhäuser, die zur Erbringung von Leistungen innerhalb mindestens eines der Fachgebiete Anästhesiologie, Frauenheilkunde und Geburtshilfe, Haut- und Geschlechtskrankheiten, Mund-, Kiefer- und Gesichtschirurgie und Humangenetik ermächtigt sind";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1100: return "Unvorhergesehene Inanspruchnahme";
            case _1210: return "Notfallpauschale";
            case _1320: return "Grundpauschale";
            default: return "?";
          }
    }


}

