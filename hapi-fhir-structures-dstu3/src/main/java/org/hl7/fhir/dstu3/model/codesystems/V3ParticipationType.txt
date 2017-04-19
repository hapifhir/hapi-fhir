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

public enum V3ParticipationType {

        /**
         * Indicates that the target of the participation is involved in some manner in the act, but does not qualify how.
         */
        PART, 
        /**
         * Participations related, but not primary to an act. The Referring, Admitting, and Discharging practitioners must be the same person as those authoring the ControlAct event for their respective trigger events.
         */
        _PARTICIPATIONANCILLARY, 
        /**
         * The practitioner who is responsible for admitting a patient to a patient encounter.
         */
        ADM, 
        /**
         * The practitioner that has responsibility for overseeing a patient's care during a patient encounter.
         */
        ATND, 
        /**
         * A person or organization who should be contacted for follow-up questions about the act in place of the author.
         */
        CALLBCK, 
        /**
         * An advisor participating in the service by performing evaluations and making recommendations.
         */
        CON, 
        /**
         * The practitioner who is responsible for the discharge of a patient from a patient encounter.
         */
        DIS, 
        /**
         * Only with Transportation services.  A person who escorts the patient.
         */
        ESC, 
        /**
         * A person having referred the subject of the service to the performer (referring physician).  Typically, a referring physician will receive a report.
         */
        REF, 
        /**
         * Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g., consultant), information that the Act itself seeks to reveal (e.g., informant of clinical history), or information about what Act was performed (e.g., informant witness).
         */
        _PARTICIPATIONINFORMATIONGENERATOR, 
        /**
         * Definition: A party that originates the Act and therefore has responsibility for the information given in the Act and ownership of this Act.

                        
                           Example: the report writer, the person writing the act definition, the guideline author, the placer of an order, the EKG cart (device) creating a report etc. Every Act should have an author. Authorship is regardless of mood always actual authorship. 

                        Examples of such policies might include:

                        
                           
                              The author and anyone they explicitly delegate may update the report;

                           
                           
                              All administrators within the same clinic may cancel and reschedule appointments created by other administrators within that clinic;

                           
                        
                        A party that is neither an author nor a party who is extended authorship maintenance rights by policy, may only amend, reverse, override, replace, or follow up in other ways on this Act, whereby the Act remains intact and is linked to another Act authored by that other party.
         */
        AUT, 
        /**
         * A source of reported information (e.g., a next of kin who answers questions about the patient's history).  For history questions, the patient is logically an informant, yet the informant of history questions is implicitly the subject.
         */
        INF, 
        /**
         * An entity entering the data into the originating system. The data entry entity is collected optionally for internal quality control purposes. This includes the transcriptionist for dictated text transcribed into electronic form.
         */
        TRANS, 
        /**
         * A person entering the data into the originating system.  The data entry person is collected optionally for internal quality control purposes.  This includes the transcriptionist for dictated text.
         */
        ENT, 
        /**
         * Only with service events.  A person witnessing the action happening without doing anything.  A witness is not necessarily aware, much less approves of anything stated in the service event.  Example for a witness is students watching an operation or an advanced directive witness.
         */
        WIT, 
        /**
         * An entity (person, organization or device) that is in charge of maintaining the information of this act (e.g., who maintains the report or the master service catalog item, etc.).
         */
        CST, 
        /**
         * Target participant  that is substantially present in the act  and which is directly involved in the action (includes consumed material, devices, etc.).
         */
        DIR, 
        /**
         * The target of an Observation action. Links an observation to a Role whose player is the substance or most specific component entity (material, micro-organism, etc.) being measured within the subject.

                        
                           Examples: A "plasma porcelain substance concentration" has analyte a Role with player substance Entity "porcelain".

                        
                           UsageNotes: The Role that this participation connects to may be any Role whose player is that substance measured. Very often, the scoper may indicate the system in which the component is being measured. E.g., for "plasma porcelain" the scoper could be "Plasma".
         */
        ALY, 
        /**
         * In an obstetric service, the baby.
         */
        BBY, 
        /**
         * The catalyst of a chemical reaction, such as an enzyme or a platinum surface. In biochemical reactions, connects the enzyme with the molecular interaction
         */
        CAT, 
        /**
         * Participant material that is taken up, diminished, altered, or disappears in the act.
         */
        CSM, 
        /**
         * Something incorporated in the subject of a therapy service to achieve a physiologic effect (e.g., heal, relieve, provoke a condition, etc.) on the subject.  In an administration service the therapeutic agent is a consumable, in a preparation or dispense service, it is a product.  Thus, consumable or product must be specified in accordance with the kind of service.
         */
        TPA, 
        /**
         * Participant used in performing the act without being substantially affected by the act (i.e. durable or inert with respect to that particular service).

                        
                           Examples: monitoring equipment, tools, but also access/drainage lines, prostheses, pace maker, etc.
         */
        DEV, 
        /**
         * A device that changes ownership due to the service, e.g., a pacemaker, a prosthesis, an insulin injection equipment (pen), etc.  Such material may need to be restocked after he service.
         */
        NRD, 
        /**
         * A device that does not change ownership due to the service, i.e., a surgical instrument or tool or an endoscope.  The distinction between reuseable and non-reuseable must be made in order to know whether material must be re-stocked.
         */
        RDV, 
        /**
         * In some organ transplantation services and rarely in transfusion services a donor will be a target participant in the service.  However, in most cases transplantation is decomposed in three services: explantation, transport, and implantation.  The identity of the donor (recipient) is often irrelevant for the explantation (implantation) service.
         */
        DON, 
        /**
         * Description: The entity playing the associated role is the physical (including energy), chemical or biological substance that is participating in the exposure.  For example in communicable diseases, the associated playing entity is the disease causing pathogen.
         */
        EXPAGNT, 
        /**
         * Description:Direct participation in an exposure act where it is unknown that the participant is the source or subject of the exposure.  If the participant is known to be the contact of an exposure then the SBJ participation type should be used.  If the participant is known to be the source then the EXSRC participation type should be used.
         */
        EXPART, 
        /**
         * Description: The entity playing the associated role is the target (contact) of exposure.
         */
        EXPTRGT, 
        /**
         * Description:The entity playing the associated role is the source of exposure.
         */
        EXSRC, 
        /**
         * Participant material that is brought forth (produced) in the act (e.g., specimen in a specimen collection, access or drainage in a placement service, medication package in a dispense service). It does not matter whether the material produced had existence prior to the service, or whether it is created in the service (e.g., in supply services the product is taken from a stock).
         */
        PRD, 
        /**
         * The principle target on which the action happens.

                        
                           Examples: The patient in physical examination, a specimen in a lab observation. May also be a patient's family member (teaching) or a device or room (cleaning, disinfecting, housekeeping). 

                        
                           UsageNotes: Not all direct targets are subjects. Consumables and devices used as tools for an act are not subjects. However, a device may be a subject of a maintenance action.
         */
        SBJ, 
        /**
         * The subject of non-clinical (e.g. laboratory) observation services is a specimen.
         */
        SPC, 
        /**
         * Target that is not substantially present in the act and which is not directly affected by the act, but which will be a focus of the record or documentation of the act.
         */
        IND, 
        /**
         * Target on behalf of whom the service happens, but that is not necessarily present in the service.  Can occur together with direct target to indicate that a target is both, as in the case where the patient is the indirect beneficiary of a service rendered to a family member, e.g. counseling or given home care instructions.  This concept includes a participant, such as a covered party, who derives benefits from a service act covered by a coverage act.

                        Note that the semantic role of the intended recipient who benefits from the happening denoted by the verb in the clause.  Thus, a patient who has no coverage under a policy or program may be a beneficiary of a health service while not being the beneficiary of coverage for that service.
         */
        BEN, 
        /**
         * Definition: A factor, such as a microorganism, chemical substance, or form of radiation, whose presence, excessive presence, or (in deficiency diseases) relative absence is essential, in whole or in part, for the occurrence of a condition.

                        Constraint:  The use of this participation is limited to observations.
         */
        CAGNT, 
        /**
         * The target participation for an individual in a health care coverage act in which the target role is either the policy holder of the coverage, or a covered party under the coverage.
         */
        COV, 
        /**
         * The target person or organization contractually recognized by the issuer as a participant who has assumed fiscal responsibility for another personaTMs financial obligations by guaranteeing to pay for amounts owed to a particular account

                        
                           Example:The subscriber of the patientaTMs health insurance policy signs a contract with the provider to be fiscally responsible for the patient billing account balance amount owed.
         */
        GUAR, 
        /**
         * Participant who posses an instrument such as a financial contract (insurance policy) usually based on some agreement with the author.
         */
        HLD, 
        /**
         * The record target indicates whose medical record holds the documentation of this act.  This is especially important when the subject of a service is not the patient himself.
         */
        RCT, 
        /**
         * The person (or organization) who receives the product of an Act.
         */
        RCV, 
        /**
         * A party, who may or should receive or who has recieved the Act or subsequent or derivative information of that Act. Information recipient is inert, i.e., independent of mood." Rationale: this is a generalization of a too diverse family that the definition can't be any more specific, and the concept is abstract so one of the specializations should be used.
         */
        IRCP, 
        /**
         * An information recipient to notify for urgent matters about this Act. (e.g., in a laboratory order, critical results are being called by phone right away, this is the contact to call; or for an inpatient encounter, a next of kin to notify when the patient becomes critically ill).
         */
        NOT, 
        /**
         * Information recipient to whom an act statement is primarily directed. E.g., a primary care provider receiving a discharge letter from a hospitalist, a health department receiving information on a suspected case of infectious disease. Multiple of these participations may exist on the same act without requiring that recipients be ranked as primary vs. secondary.
         */
        PRCP, 
        /**
         * A participant (e.g. provider) who has referred the subject of an act (e.g. patient).

                        Typically, a referred by participant will provide a report (e.g. referral).
         */
        REFB, 
        /**
         * The person who receives the patient
         */
        REFT, 
        /**
         * A secondary information recipient, who receives copies (e.g., a primary care provider receiving copies of results as ordered by specialist).
         */
        TRC, 
        /**
         * The facility where the service is done.  May be a static building (or room therein) or a moving location (e.g., ambulance, helicopter, aircraft, train, truck, ship, etc.)
         */
        LOC, 
        /**
         * The destination for services.  May be a static building (or room therein) or a movable facility (e.g., ship).
         */
        DST, 
        /**
         * A location where data about an Act was entered.
         */
        ELOC, 
        /**
         * The location of origin for services.  May be a static building (or room therein) or a movable facility (e.g., ship).
         */
        ORG, 
        /**
         * Some services take place at multiple concurrent locations (e.g., telemedicine, telephone consultation).  The location where the principal performing actor is located is taken as the primary location (LOC) while the other location(s) are considered "remote."
         */
        RML, 
        /**
         * For services, an intermediate location that specifies a path between origin an destination.
         */
        VIA, 
        /**
         * Definition: A person, non-person living subject, organization or device that who actually and principally carries out the action. Device should only be assigned as a performer in circumstances where the device is performing independent of human intervention.  Need not be the principal responsible actor.

                        
                           Exampe: A surgery resident operating under supervision of attending surgeon, a search and rescue dog locating survivors, an electronic laboratory analyzer or the laboratory discipline requested to perform a laboratory test. The performer may also be the patient in self-care, e.g. fingerstick blood sugar. The traditional order filler is a performer. This information should accompany every service event.

                        
                           Note: that existing HL7 designs assign an organization as the playing entity of the Role that is the performer.  These designs should be revised in subsequent releases to make this the scooping entity for the role involved.
         */
        PRF, 
        /**
         * Distributes material used in or generated during the act.
         */
        DIST, 
        /**
         * The principal or primary performer of the act.
         */
        PPRF, 
        /**
         * A person assisting in an act through his substantial presence and involvement   This includes: assistants, technicians, associates, or whatever the job titles may be.
         */
        SPRF, 
        /**
         * The person or organization that has primary responsibility for the act.  The responsible party is not necessarily present in an action, but is accountable for the action through the power to delegate, and the duty to review actions with the performing actor after the fact.  This responsibility may be ethical, legal, contractual, fiscal, or fiduciary in nature.

                        
                           Example: A person who is the head of a biochemical laboratory; a sponsor for a policy or government program.
         */
        RESP, 
        /**
         * A person who verifies the correctness and appropriateness of the service (plan, order, event, etc.) and hence takes on accountability.
         */
        VRF, 
        /**
         * A verifier who attests to the accuracy of an act, but who does not have privileges to legally authenticate the act. An example would be a resident physician who sees a patient and dictates a note, then later signs it. Their signature constitutes an authentication.
         */
        AUTHEN, 
        /**
         * A verifier who legally authenticates the accuracy of an act. An example would be a staff physician who sees a patient and dictates a note, then later signs it. Their signature constitutes a legal authentication.
         */
        LA, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ParticipationType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("PART".equals(codeString))
          return PART;
        if ("_ParticipationAncillary".equals(codeString))
          return _PARTICIPATIONANCILLARY;
        if ("ADM".equals(codeString))
          return ADM;
        if ("ATND".equals(codeString))
          return ATND;
        if ("CALLBCK".equals(codeString))
          return CALLBCK;
        if ("CON".equals(codeString))
          return CON;
        if ("DIS".equals(codeString))
          return DIS;
        if ("ESC".equals(codeString))
          return ESC;
        if ("REF".equals(codeString))
          return REF;
        if ("_ParticipationInformationGenerator".equals(codeString))
          return _PARTICIPATIONINFORMATIONGENERATOR;
        if ("AUT".equals(codeString))
          return AUT;
        if ("INF".equals(codeString))
          return INF;
        if ("TRANS".equals(codeString))
          return TRANS;
        if ("ENT".equals(codeString))
          return ENT;
        if ("WIT".equals(codeString))
          return WIT;
        if ("CST".equals(codeString))
          return CST;
        if ("DIR".equals(codeString))
          return DIR;
        if ("ALY".equals(codeString))
          return ALY;
        if ("BBY".equals(codeString))
          return BBY;
        if ("CAT".equals(codeString))
          return CAT;
        if ("CSM".equals(codeString))
          return CSM;
        if ("TPA".equals(codeString))
          return TPA;
        if ("DEV".equals(codeString))
          return DEV;
        if ("NRD".equals(codeString))
          return NRD;
        if ("RDV".equals(codeString))
          return RDV;
        if ("DON".equals(codeString))
          return DON;
        if ("EXPAGNT".equals(codeString))
          return EXPAGNT;
        if ("EXPART".equals(codeString))
          return EXPART;
        if ("EXPTRGT".equals(codeString))
          return EXPTRGT;
        if ("EXSRC".equals(codeString))
          return EXSRC;
        if ("PRD".equals(codeString))
          return PRD;
        if ("SBJ".equals(codeString))
          return SBJ;
        if ("SPC".equals(codeString))
          return SPC;
        if ("IND".equals(codeString))
          return IND;
        if ("BEN".equals(codeString))
          return BEN;
        if ("CAGNT".equals(codeString))
          return CAGNT;
        if ("COV".equals(codeString))
          return COV;
        if ("GUAR".equals(codeString))
          return GUAR;
        if ("HLD".equals(codeString))
          return HLD;
        if ("RCT".equals(codeString))
          return RCT;
        if ("RCV".equals(codeString))
          return RCV;
        if ("IRCP".equals(codeString))
          return IRCP;
        if ("NOT".equals(codeString))
          return NOT;
        if ("PRCP".equals(codeString))
          return PRCP;
        if ("REFB".equals(codeString))
          return REFB;
        if ("REFT".equals(codeString))
          return REFT;
        if ("TRC".equals(codeString))
          return TRC;
        if ("LOC".equals(codeString))
          return LOC;
        if ("DST".equals(codeString))
          return DST;
        if ("ELOC".equals(codeString))
          return ELOC;
        if ("ORG".equals(codeString))
          return ORG;
        if ("RML".equals(codeString))
          return RML;
        if ("VIA".equals(codeString))
          return VIA;
        if ("PRF".equals(codeString))
          return PRF;
        if ("DIST".equals(codeString))
          return DIST;
        if ("PPRF".equals(codeString))
          return PPRF;
        if ("SPRF".equals(codeString))
          return SPRF;
        if ("RESP".equals(codeString))
          return RESP;
        if ("VRF".equals(codeString))
          return VRF;
        if ("AUTHEN".equals(codeString))
          return AUTHEN;
        if ("LA".equals(codeString))
          return LA;
        throw new FHIRException("Unknown V3ParticipationType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case PART: return "PART";
            case _PARTICIPATIONANCILLARY: return "_ParticipationAncillary";
            case ADM: return "ADM";
            case ATND: return "ATND";
            case CALLBCK: return "CALLBCK";
            case CON: return "CON";
            case DIS: return "DIS";
            case ESC: return "ESC";
            case REF: return "REF";
            case _PARTICIPATIONINFORMATIONGENERATOR: return "_ParticipationInformationGenerator";
            case AUT: return "AUT";
            case INF: return "INF";
            case TRANS: return "TRANS";
            case ENT: return "ENT";
            case WIT: return "WIT";
            case CST: return "CST";
            case DIR: return "DIR";
            case ALY: return "ALY";
            case BBY: return "BBY";
            case CAT: return "CAT";
            case CSM: return "CSM";
            case TPA: return "TPA";
            case DEV: return "DEV";
            case NRD: return "NRD";
            case RDV: return "RDV";
            case DON: return "DON";
            case EXPAGNT: return "EXPAGNT";
            case EXPART: return "EXPART";
            case EXPTRGT: return "EXPTRGT";
            case EXSRC: return "EXSRC";
            case PRD: return "PRD";
            case SBJ: return "SBJ";
            case SPC: return "SPC";
            case IND: return "IND";
            case BEN: return "BEN";
            case CAGNT: return "CAGNT";
            case COV: return "COV";
            case GUAR: return "GUAR";
            case HLD: return "HLD";
            case RCT: return "RCT";
            case RCV: return "RCV";
            case IRCP: return "IRCP";
            case NOT: return "NOT";
            case PRCP: return "PRCP";
            case REFB: return "REFB";
            case REFT: return "REFT";
            case TRC: return "TRC";
            case LOC: return "LOC";
            case DST: return "DST";
            case ELOC: return "ELOC";
            case ORG: return "ORG";
            case RML: return "RML";
            case VIA: return "VIA";
            case PRF: return "PRF";
            case DIST: return "DIST";
            case PPRF: return "PPRF";
            case SPRF: return "SPRF";
            case RESP: return "RESP";
            case VRF: return "VRF";
            case AUTHEN: return "AUTHEN";
            case LA: return "LA";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ParticipationType";
        }
        public String getDefinition() {
          switch (this) {
            case PART: return "Indicates that the target of the participation is involved in some manner in the act, but does not qualify how.";
            case _PARTICIPATIONANCILLARY: return "Participations related, but not primary to an act. The Referring, Admitting, and Discharging practitioners must be the same person as those authoring the ControlAct event for their respective trigger events.";
            case ADM: return "The practitioner who is responsible for admitting a patient to a patient encounter.";
            case ATND: return "The practitioner that has responsibility for overseeing a patient's care during a patient encounter.";
            case CALLBCK: return "A person or organization who should be contacted for follow-up questions about the act in place of the author.";
            case CON: return "An advisor participating in the service by performing evaluations and making recommendations.";
            case DIS: return "The practitioner who is responsible for the discharge of a patient from a patient encounter.";
            case ESC: return "Only with Transportation services.  A person who escorts the patient.";
            case REF: return "A person having referred the subject of the service to the performer (referring physician).  Typically, a referring physician will receive a report.";
            case _PARTICIPATIONINFORMATIONGENERATOR: return "Parties that may or should contribute or have contributed information to the Act. Such information includes information leading to the decision to perform the Act and how to perform the Act (e.g., consultant), information that the Act itself seeks to reveal (e.g., informant of clinical history), or information about what Act was performed (e.g., informant witness).";
            case AUT: return "Definition: A party that originates the Act and therefore has responsibility for the information given in the Act and ownership of this Act.\r\n\n                        \n                           Example: the report writer, the person writing the act definition, the guideline author, the placer of an order, the EKG cart (device) creating a report etc. Every Act should have an author. Authorship is regardless of mood always actual authorship. \r\n\n                        Examples of such policies might include:\r\n\n                        \n                           \n                              The author and anyone they explicitly delegate may update the report;\r\n\n                           \n                           \n                              All administrators within the same clinic may cancel and reschedule appointments created by other administrators within that clinic;\r\n\n                           \n                        \n                        A party that is neither an author nor a party who is extended authorship maintenance rights by policy, may only amend, reverse, override, replace, or follow up in other ways on this Act, whereby the Act remains intact and is linked to another Act authored by that other party.";
            case INF: return "A source of reported information (e.g., a next of kin who answers questions about the patient's history).  For history questions, the patient is logically an informant, yet the informant of history questions is implicitly the subject.";
            case TRANS: return "An entity entering the data into the originating system. The data entry entity is collected optionally for internal quality control purposes. This includes the transcriptionist for dictated text transcribed into electronic form.";
            case ENT: return "A person entering the data into the originating system.  The data entry person is collected optionally for internal quality control purposes.  This includes the transcriptionist for dictated text.";
            case WIT: return "Only with service events.  A person witnessing the action happening without doing anything.  A witness is not necessarily aware, much less approves of anything stated in the service event.  Example for a witness is students watching an operation or an advanced directive witness.";
            case CST: return "An entity (person, organization or device) that is in charge of maintaining the information of this act (e.g., who maintains the report or the master service catalog item, etc.).";
            case DIR: return "Target participant  that is substantially present in the act  and which is directly involved in the action (includes consumed material, devices, etc.).";
            case ALY: return "The target of an Observation action. Links an observation to a Role whose player is the substance or most specific component entity (material, micro-organism, etc.) being measured within the subject.\r\n\n                        \n                           Examples: A \"plasma porcelain substance concentration\" has analyte a Role with player substance Entity \"porcelain\".\r\n\n                        \n                           UsageNotes: The Role that this participation connects to may be any Role whose player is that substance measured. Very often, the scoper may indicate the system in which the component is being measured. E.g., for \"plasma porcelain\" the scoper could be \"Plasma\".";
            case BBY: return "In an obstetric service, the baby.";
            case CAT: return "The catalyst of a chemical reaction, such as an enzyme or a platinum surface. In biochemical reactions, connects the enzyme with the molecular interaction";
            case CSM: return "Participant material that is taken up, diminished, altered, or disappears in the act.";
            case TPA: return "Something incorporated in the subject of a therapy service to achieve a physiologic effect (e.g., heal, relieve, provoke a condition, etc.) on the subject.  In an administration service the therapeutic agent is a consumable, in a preparation or dispense service, it is a product.  Thus, consumable or product must be specified in accordance with the kind of service.";
            case DEV: return "Participant used in performing the act without being substantially affected by the act (i.e. durable or inert with respect to that particular service).\r\n\n                        \n                           Examples: monitoring equipment, tools, but also access/drainage lines, prostheses, pace maker, etc.";
            case NRD: return "A device that changes ownership due to the service, e.g., a pacemaker, a prosthesis, an insulin injection equipment (pen), etc.  Such material may need to be restocked after he service.";
            case RDV: return "A device that does not change ownership due to the service, i.e., a surgical instrument or tool or an endoscope.  The distinction between reuseable and non-reuseable must be made in order to know whether material must be re-stocked.";
            case DON: return "In some organ transplantation services and rarely in transfusion services a donor will be a target participant in the service.  However, in most cases transplantation is decomposed in three services: explantation, transport, and implantation.  The identity of the donor (recipient) is often irrelevant for the explantation (implantation) service.";
            case EXPAGNT: return "Description: The entity playing the associated role is the physical (including energy), chemical or biological substance that is participating in the exposure.  For example in communicable diseases, the associated playing entity is the disease causing pathogen.";
            case EXPART: return "Description:Direct participation in an exposure act where it is unknown that the participant is the source or subject of the exposure.  If the participant is known to be the contact of an exposure then the SBJ participation type should be used.  If the participant is known to be the source then the EXSRC participation type should be used.";
            case EXPTRGT: return "Description: The entity playing the associated role is the target (contact) of exposure.";
            case EXSRC: return "Description:The entity playing the associated role is the source of exposure.";
            case PRD: return "Participant material that is brought forth (produced) in the act (e.g., specimen in a specimen collection, access or drainage in a placement service, medication package in a dispense service). It does not matter whether the material produced had existence prior to the service, or whether it is created in the service (e.g., in supply services the product is taken from a stock).";
            case SBJ: return "The principle target on which the action happens.\r\n\n                        \n                           Examples: The patient in physical examination, a specimen in a lab observation. May also be a patient's family member (teaching) or a device or room (cleaning, disinfecting, housekeeping). \r\n\n                        \n                           UsageNotes: Not all direct targets are subjects. Consumables and devices used as tools for an act are not subjects. However, a device may be a subject of a maintenance action.";
            case SPC: return "The subject of non-clinical (e.g. laboratory) observation services is a specimen.";
            case IND: return "Target that is not substantially present in the act and which is not directly affected by the act, but which will be a focus of the record or documentation of the act.";
            case BEN: return "Target on behalf of whom the service happens, but that is not necessarily present in the service.  Can occur together with direct target to indicate that a target is both, as in the case where the patient is the indirect beneficiary of a service rendered to a family member, e.g. counseling or given home care instructions.  This concept includes a participant, such as a covered party, who derives benefits from a service act covered by a coverage act.\r\n\n                        Note that the semantic role of the intended recipient who benefits from the happening denoted by the verb in the clause.  Thus, a patient who has no coverage under a policy or program may be a beneficiary of a health service while not being the beneficiary of coverage for that service.";
            case CAGNT: return "Definition: A factor, such as a microorganism, chemical substance, or form of radiation, whose presence, excessive presence, or (in deficiency diseases) relative absence is essential, in whole or in part, for the occurrence of a condition.\r\n\n                        Constraint:  The use of this participation is limited to observations.";
            case COV: return "The target participation for an individual in a health care coverage act in which the target role is either the policy holder of the coverage, or a covered party under the coverage.";
            case GUAR: return "The target person or organization contractually recognized by the issuer as a participant who has assumed fiscal responsibility for another personaTMs financial obligations by guaranteeing to pay for amounts owed to a particular account\r\n\n                        \n                           Example:The subscriber of the patientaTMs health insurance policy signs a contract with the provider to be fiscally responsible for the patient billing account balance amount owed.";
            case HLD: return "Participant who posses an instrument such as a financial contract (insurance policy) usually based on some agreement with the author.";
            case RCT: return "The record target indicates whose medical record holds the documentation of this act.  This is especially important when the subject of a service is not the patient himself.";
            case RCV: return "The person (or organization) who receives the product of an Act.";
            case IRCP: return "A party, who may or should receive or who has recieved the Act or subsequent or derivative information of that Act. Information recipient is inert, i.e., independent of mood.\" Rationale: this is a generalization of a too diverse family that the definition can't be any more specific, and the concept is abstract so one of the specializations should be used.";
            case NOT: return "An information recipient to notify for urgent matters about this Act. (e.g., in a laboratory order, critical results are being called by phone right away, this is the contact to call; or for an inpatient encounter, a next of kin to notify when the patient becomes critically ill).";
            case PRCP: return "Information recipient to whom an act statement is primarily directed. E.g., a primary care provider receiving a discharge letter from a hospitalist, a health department receiving information on a suspected case of infectious disease. Multiple of these participations may exist on the same act without requiring that recipients be ranked as primary vs. secondary.";
            case REFB: return "A participant (e.g. provider) who has referred the subject of an act (e.g. patient).\r\n\n                        Typically, a referred by participant will provide a report (e.g. referral).";
            case REFT: return "The person who receives the patient";
            case TRC: return "A secondary information recipient, who receives copies (e.g., a primary care provider receiving copies of results as ordered by specialist).";
            case LOC: return "The facility where the service is done.  May be a static building (or room therein) or a moving location (e.g., ambulance, helicopter, aircraft, train, truck, ship, etc.)";
            case DST: return "The destination for services.  May be a static building (or room therein) or a movable facility (e.g., ship).";
            case ELOC: return "A location where data about an Act was entered.";
            case ORG: return "The location of origin for services.  May be a static building (or room therein) or a movable facility (e.g., ship).";
            case RML: return "Some services take place at multiple concurrent locations (e.g., telemedicine, telephone consultation).  The location where the principal performing actor is located is taken as the primary location (LOC) while the other location(s) are considered \"remote.\"";
            case VIA: return "For services, an intermediate location that specifies a path between origin an destination.";
            case PRF: return "Definition: A person, non-person living subject, organization or device that who actually and principally carries out the action. Device should only be assigned as a performer in circumstances where the device is performing independent of human intervention.  Need not be the principal responsible actor.\r\n\n                        \n                           Exampe: A surgery resident operating under supervision of attending surgeon, a search and rescue dog locating survivors, an electronic laboratory analyzer or the laboratory discipline requested to perform a laboratory test. The performer may also be the patient in self-care, e.g. fingerstick blood sugar. The traditional order filler is a performer. This information should accompany every service event.\r\n\n                        \n                           Note: that existing HL7 designs assign an organization as the playing entity of the Role that is the performer.  These designs should be revised in subsequent releases to make this the scooping entity for the role involved.";
            case DIST: return "Distributes material used in or generated during the act.";
            case PPRF: return "The principal or primary performer of the act.";
            case SPRF: return "A person assisting in an act through his substantial presence and involvement   This includes: assistants, technicians, associates, or whatever the job titles may be.";
            case RESP: return "The person or organization that has primary responsibility for the act.  The responsible party is not necessarily present in an action, but is accountable for the action through the power to delegate, and the duty to review actions with the performing actor after the fact.  This responsibility may be ethical, legal, contractual, fiscal, or fiduciary in nature.\r\n\n                        \n                           Example: A person who is the head of a biochemical laboratory; a sponsor for a policy or government program.";
            case VRF: return "A person who verifies the correctness and appropriateness of the service (plan, order, event, etc.) and hence takes on accountability.";
            case AUTHEN: return "A verifier who attests to the accuracy of an act, but who does not have privileges to legally authenticate the act. An example would be a resident physician who sees a patient and dictates a note, then later signs it. Their signature constitutes an authentication.";
            case LA: return "A verifier who legally authenticates the accuracy of an act. An example would be a staff physician who sees a patient and dictates a note, then later signs it. Their signature constitutes a legal authentication.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case PART: return "Participation";
            case _PARTICIPATIONANCILLARY: return "ParticipationAncillary";
            case ADM: return "admitter";
            case ATND: return "attender";
            case CALLBCK: return "callback contact";
            case CON: return "consultant";
            case DIS: return "discharger";
            case ESC: return "escort";
            case REF: return "referrer";
            case _PARTICIPATIONINFORMATIONGENERATOR: return "ParticipationInformationGenerator";
            case AUT: return "author (originator)";
            case INF: return "informant";
            case TRANS: return "Transcriber";
            case ENT: return "data entry person";
            case WIT: return "witness";
            case CST: return "custodian";
            case DIR: return "direct target";
            case ALY: return "analyte";
            case BBY: return "baby";
            case CAT: return "catalyst";
            case CSM: return "consumable";
            case TPA: return "therapeutic agent";
            case DEV: return "device";
            case NRD: return "non-reuseable device";
            case RDV: return "reusable device";
            case DON: return "donor";
            case EXPAGNT: return "ExposureAgent";
            case EXPART: return "ExposureParticipation";
            case EXPTRGT: return "ExposureTarget";
            case EXSRC: return "ExposureSource";
            case PRD: return "product";
            case SBJ: return "subject";
            case SPC: return "specimen";
            case IND: return "indirect target";
            case BEN: return "beneficiary";
            case CAGNT: return "causative agent";
            case COV: return "coverage target";
            case GUAR: return "guarantor party";
            case HLD: return "holder";
            case RCT: return "record target";
            case RCV: return "receiver";
            case IRCP: return "information recipient";
            case NOT: return "ugent notification contact";
            case PRCP: return "primary information recipient";
            case REFB: return "Referred By";
            case REFT: return "Referred to";
            case TRC: return "tracker";
            case LOC: return "location";
            case DST: return "destination";
            case ELOC: return "entry location";
            case ORG: return "origin";
            case RML: return "remote";
            case VIA: return "via";
            case PRF: return "performer";
            case DIST: return "distributor";
            case PPRF: return "primary performer";
            case SPRF: return "secondary performer";
            case RESP: return "responsible party";
            case VRF: return "verifier";
            case AUTHEN: return "authenticator";
            case LA: return "legal authenticator";
            default: return "?";
          }
    }


}

