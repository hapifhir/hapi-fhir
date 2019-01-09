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

public enum Iso21089Lifecycle {

        /**
         * Occurs when an agent causes the system to obtain and open a record entry for inspection or review.
         */
        ACCESS, 
        /**
         * Occurs when an agent causes the system to tag or otherwise indicate special access management and suspension of record entry deletion/destruction, if deemed relevant to a lawsuit or which are reasonably anticipated to be relevant or to fulfill organizational policy under the legal doctrine of “duty to preserve”.
         */
        HOLD, 
        /**
         * Occurs when an agent makes any change to record entry content currently residing in storage considered permanent (persistent).
         */
        AMEND, 
        /**
         * Occurs when an agent causes the system to create and move archive artifacts containing record entry content, typically to long-term offline storage.
         */
        ARCHIVE, 
        /**
         * Occurs when an agent causes the system to capture the agent’s digital signature (or equivalent indication) during formal validation of record entry content.
         */
        ATTEST, 
        /**
         * Occurs when an agent causes the system to decode record entry content from a cipher.
         */
        DECRYPT, 
        /**
         * Occurs when an agent causes the system to scrub record entry content to reduce the association between a set of identifying data and the data subject in a way that might or might not be reversible.
         */
        DEIDENTIFY, 
        /**
         * Occurs when an agent causes the system to tag record entry(ies) as obsolete, erroneous or untrustworthy, to warn against its future use.
         */
        DEPRECATE, 
        /**
         * Occurs when an agent causes the system to permanently erase record entry content from the system.
         */
        DESTROY, 
        /**
         * Occurs when an agent causes the system to release, transfer, provision access to, or otherwise divulge record entry content.
         */
        DISCLOSE, 
        /**
         * Occurs when an agent causes the system to encode record entry content in a cipher.
         */
        ENCRYPT, 
        /**
         * Occurs when an agent causes the system to selectively pull out a subset of record entry content, based on explicit criteria.
         */
        EXTRACT, 
        /**
         * Occurs when an agent causes the system to connect related record entries.
         */
        LINK, 
        /**
         * Occurs when an agent causes the system to combine or join content from two or more record entries, resulting in a single logical record entry.
         */
        MERGE, 
        /**
         * Occurs when an agent causes the system to: a) initiate capture of potential record content, and b) incorporate that content into the storage considered a permanent part of the health record.
         */
        ORIGINATE, 
        /**
         * Occurs when an agent causes the system to remove record entry content to reduce the association between a set of identifying data and the data subject in a way that may be reversible.
         */
        PSEUDONYMIZE, 
        /**
         * Occurs when an agent causes the system to recreate or restore full status to record entries previously deleted or deprecated.
         */
        REACTIVATE, 
        /**
         * Occurs when an agent causes the system to a) initiate capture of data content from elsewhere, and b) incorporate that content into the storage considered a permanent part of the health record.
         */
        RECEIVE, 
        /**
         * Occurs when an agent causes the system to restore information to data that allows identification of information source and/or information subject.
         */
        REIDENTIFY, 
        /**
         * Occurs when an agent causes the system to remove a tag or other cues for special access management had required to fulfill organizational policy under the legal doctrine of “duty to preserve”.
         */
        UNHOLD, 
        /**
         * Occurs when an agent causes the system to produce and deliver record entry content in a particular form and manner.
         */
        REPORT, 
        /**
         * Occurs when an agent causes the system to recreate record entries and their content from a previous created archive artefact.
         */
        RESTORE, 
        /**
         * Occurs when an agent causes the system to change the form, language or code system used to represent record entry content.
         */
        TRANSFORM, 
        /**
         * Occurs when an agent causes the system to send record entry content from one (EHR/PHR/other) system to another.
         */
        TRANSMIT, 
        /**
         * Occurs when an agent causes the system to disconnect two or more record entries previously connected, rendering them separate (disconnected) again.
         */
        UNLINK, 
        /**
         * Occurs when an agent causes the system to reverse a previous record entry merge operation, rendering them separate again.
         */
        UNMERGE, 
        /**
         * Occurs when an agent causes the system to confirm compliance of data or data objects with regulations, requirements, specifications, or other imposed conditions based on organizational policy.
         */
        VERIFY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static Iso21089Lifecycle fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("access".equals(codeString))
          return ACCESS;
        if ("hold".equals(codeString))
          return HOLD;
        if ("amend".equals(codeString))
          return AMEND;
        if ("archive".equals(codeString))
          return ARCHIVE;
        if ("attest".equals(codeString))
          return ATTEST;
        if ("decrypt".equals(codeString))
          return DECRYPT;
        if ("deidentify".equals(codeString))
          return DEIDENTIFY;
        if ("deprecate".equals(codeString))
          return DEPRECATE;
        if ("destroy".equals(codeString))
          return DESTROY;
        if ("disclose".equals(codeString))
          return DISCLOSE;
        if ("encrypt".equals(codeString))
          return ENCRYPT;
        if ("extract".equals(codeString))
          return EXTRACT;
        if ("link".equals(codeString))
          return LINK;
        if ("merge".equals(codeString))
          return MERGE;
        if ("originate".equals(codeString))
          return ORIGINATE;
        if ("pseudonymize".equals(codeString))
          return PSEUDONYMIZE;
        if ("reactivate".equals(codeString))
          return REACTIVATE;
        if ("receive".equals(codeString))
          return RECEIVE;
        if ("reidentify".equals(codeString))
          return REIDENTIFY;
        if ("unhold".equals(codeString))
          return UNHOLD;
        if ("report".equals(codeString))
          return REPORT;
        if ("restore".equals(codeString))
          return RESTORE;
        if ("transform".equals(codeString))
          return TRANSFORM;
        if ("transmit".equals(codeString))
          return TRANSMIT;
        if ("unlink".equals(codeString))
          return UNLINK;
        if ("unmerge".equals(codeString))
          return UNMERGE;
        if ("verify".equals(codeString))
          return VERIFY;
        throw new FHIRException("Unknown Iso21089Lifecycle code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACCESS: return "access";
            case HOLD: return "hold";
            case AMEND: return "amend";
            case ARCHIVE: return "archive";
            case ATTEST: return "attest";
            case DECRYPT: return "decrypt";
            case DEIDENTIFY: return "deidentify";
            case DEPRECATE: return "deprecate";
            case DESTROY: return "destroy";
            case DISCLOSE: return "disclose";
            case ENCRYPT: return "encrypt";
            case EXTRACT: return "extract";
            case LINK: return "link";
            case MERGE: return "merge";
            case ORIGINATE: return "originate";
            case PSEUDONYMIZE: return "pseudonymize";
            case REACTIVATE: return "reactivate";
            case RECEIVE: return "receive";
            case REIDENTIFY: return "reidentify";
            case UNHOLD: return "unhold";
            case REPORT: return "report";
            case RESTORE: return "restore";
            case TRANSFORM: return "transform";
            case TRANSMIT: return "transmit";
            case UNLINK: return "unlink";
            case UNMERGE: return "unmerge";
            case VERIFY: return "verify";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/iso-21089-lifecycle";
        }
        public String getDefinition() {
          switch (this) {
            case ACCESS: return "Occurs when an agent causes the system to obtain and open a record entry for inspection or review.";
            case HOLD: return "Occurs when an agent causes the system to tag or otherwise indicate special access management and suspension of record entry deletion/destruction, if deemed relevant to a lawsuit or which are reasonably anticipated to be relevant or to fulfill organizational policy under the legal doctrine of “duty to preserve”.";
            case AMEND: return "Occurs when an agent makes any change to record entry content currently residing in storage considered permanent (persistent).";
            case ARCHIVE: return "Occurs when an agent causes the system to create and move archive artifacts containing record entry content, typically to long-term offline storage.";
            case ATTEST: return "Occurs when an agent causes the system to capture the agent’s digital signature (or equivalent indication) during formal validation of record entry content.";
            case DECRYPT: return "Occurs when an agent causes the system to decode record entry content from a cipher.";
            case DEIDENTIFY: return "Occurs when an agent causes the system to scrub record entry content to reduce the association between a set of identifying data and the data subject in a way that might or might not be reversible.";
            case DEPRECATE: return "Occurs when an agent causes the system to tag record entry(ies) as obsolete, erroneous or untrustworthy, to warn against its future use.";
            case DESTROY: return "Occurs when an agent causes the system to permanently erase record entry content from the system.";
            case DISCLOSE: return "Occurs when an agent causes the system to release, transfer, provision access to, or otherwise divulge record entry content.";
            case ENCRYPT: return "Occurs when an agent causes the system to encode record entry content in a cipher.";
            case EXTRACT: return "Occurs when an agent causes the system to selectively pull out a subset of record entry content, based on explicit criteria.";
            case LINK: return "Occurs when an agent causes the system to connect related record entries.";
            case MERGE: return "Occurs when an agent causes the system to combine or join content from two or more record entries, resulting in a single logical record entry.";
            case ORIGINATE: return "Occurs when an agent causes the system to: a) initiate capture of potential record content, and b) incorporate that content into the storage considered a permanent part of the health record.";
            case PSEUDONYMIZE: return "Occurs when an agent causes the system to remove record entry content to reduce the association between a set of identifying data and the data subject in a way that may be reversible.";
            case REACTIVATE: return "Occurs when an agent causes the system to recreate or restore full status to record entries previously deleted or deprecated.";
            case RECEIVE: return "Occurs when an agent causes the system to a) initiate capture of data content from elsewhere, and b) incorporate that content into the storage considered a permanent part of the health record.";
            case REIDENTIFY: return "Occurs when an agent causes the system to restore information to data that allows identification of information source and/or information subject.";
            case UNHOLD: return "Occurs when an agent causes the system to remove a tag or other cues for special access management had required to fulfill organizational policy under the legal doctrine of “duty to preserve”.";
            case REPORT: return "Occurs when an agent causes the system to produce and deliver record entry content in a particular form and manner.";
            case RESTORE: return "Occurs when an agent causes the system to recreate record entries and their content from a previous created archive artefact.";
            case TRANSFORM: return "Occurs when an agent causes the system to change the form, language or code system used to represent record entry content.";
            case TRANSMIT: return "Occurs when an agent causes the system to send record entry content from one (EHR/PHR/other) system to another.";
            case UNLINK: return "Occurs when an agent causes the system to disconnect two or more record entries previously connected, rendering them separate (disconnected) again.";
            case UNMERGE: return "Occurs when an agent causes the system to reverse a previous record entry merge operation, rendering them separate again.";
            case VERIFY: return "Occurs when an agent causes the system to confirm compliance of data or data objects with regulations, requirements, specifications, or other imposed conditions based on organizational policy.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACCESS: return "Access/View Record Lifecycle Event";
            case HOLD: return "Add Legal Hold Record Lifecycle Event";
            case AMEND: return "Amend (Update) Record Lifecycle Event";
            case ARCHIVE: return "Archive Record Lifecycle Event";
            case ATTEST: return "Attest Record Lifecycle Event";
            case DECRYPT: return "Decrypt Record Lifecycle Event";
            case DEIDENTIFY: return "De-Identify (Anononymize) Record Lifecycle Event";
            case DEPRECATE: return "Deprecate Record Lifecycle Event";
            case DESTROY: return "Destroy/Delete Record Lifecycle Event";
            case DISCLOSE: return "Disclose Record Lifecycle Event";
            case ENCRYPT: return "Encrypt Record Lifecycle Event";
            case EXTRACT: return "Extract Record Lifecycle Event";
            case LINK: return "Link Record Lifecycle Event";
            case MERGE: return "Merge Record Lifecycle Event";
            case ORIGINATE: return "Originate/Retain Record Lifecycle Event";
            case PSEUDONYMIZE: return "Pseudonymize Record Lifecycle Event";
            case REACTIVATE: return "Re-activate Record Lifecycle Event";
            case RECEIVE: return "Receive/Retain Record Lifecycle Event";
            case REIDENTIFY: return "Re-identify Record Lifecycle Event";
            case UNHOLD: return "Remove Legal Hold Record Lifecycle Event";
            case REPORT: return "Report (Output) Record Lifecycle Event";
            case RESTORE: return "Restore Record Lifecycle Event";
            case TRANSFORM: return "Transform/Translate Record Lifecycle Event";
            case TRANSMIT: return "Transmit Record Lifecycle Event";
            case UNLINK: return "Unlink Record Lifecycle Event";
            case UNMERGE: return "Unmerge Record Lifecycle Event";
            case VERIFY: return "Verify Record Lifecycle Event";
            default: return "?";
          }
    }


}

