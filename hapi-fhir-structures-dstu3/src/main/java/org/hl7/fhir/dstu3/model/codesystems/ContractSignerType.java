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

public enum ContractSignerType {

        /**
         * A person who has corrected, edited, or amended pre-existing information.
         */
        AMENDER, 
        /**
         * A person in the role of verifier who attests to the accuracy of an act, but who does not have privileges to legally authenticate information content. An example would be a resident physician who sees a patient and dictates a note, then later signs it. The resident's signature constitutes an authentication.
         */
        AUTHN, 
        /**
         * An entity that authored specific content. There can be multiple authors of content, which may take such forms as a contract, a healthcare record entry or document, a policy, or a consent directive.
         */
        AUT, 
        /**
         * An entity that has a business or professional relationship with another entity in accordance with an agreement.
         */
        AFFL, 
        /**
         * An entity that acts or is authorized to act on behalf of another entity in accordance with an agreement.
         */
        AGNT, 
        /**
         * An agent role in which the agent is an Entity acting in the employ of an organization. The focus is on functional role on behalf of the organization, unlike the Employee role where the focus is on the 'Human Resources' relationship between the employee and the organization.
         */
        ASSIGNED, 
        /**
         * The member of a jurisdiction afforded certain rights and encumbered with certain obligation in accordance with jurisdictional policy.
         */
        CIT, 
        /**
         * A party that makes a claim for coverage under a policy.
         */
        CLAIMANT, 
        /**
         * The entity that co-authored content. There can be multiple co-authors of content,which may take such forms as a  such as a contract, a healthcare record entry or document, a policy, or a consent directive.
         */
        COAUTH, 
        /**
         * A patient or patient representative who is the grantee in a healthcare related agreement such as a consent for healthcare services, advanced directive, or a privacy consent directive in accordance with jurisdictional, organizational, or patient policy.
         */
        CONSENTER, 
        /**
         * A person who has witnessed and attests to observing a patient being counseled about a healthcare related agreement such as a consent for healthcare services, advanced directive, or a privacy consent directive.
         */
        CONSWIT, 
        /**
         * A person or an organization that provides or receives information regarding another entity. Examples; patient NOK and emergency contacts; guarantor contact; employer contact.
         */
        CONT, 
        /**
         * A person who participates in the generation of and attest to veracity of content, but is not an author or co-author. For example a surgeon who is required by institutional, regulatory, or legal rules to sign an operative report, but who was not involved in the authorship of that report.
         */
        COPART, 
        /**
         * An entity, which is the insured, that receives benefits such as healthcare services, reimbursement for out-of-pocket expenses, or compensation for losses through coverage under the terms of an insurance policy. The underwriter of that policy is the scoping entity. The covered party receives coverage because of some contractual or other relationship with the holder of that policy. Note that a particular policy may cover several individuals one of whom may be, but need not be, the policy holder. Thus the notion of covered party is a role that is distinct from that of the policy holder.
         */
        COVPTY, 
        /**
         * A party to whom some right or authority is delegated by a delegator.
         */
        DELEGATEE, 
        /**
         * A party that delegates a right or authority to another party.
         */
        DELEGATOR, 
        /**
         * A person covered under an insurance policy or program based on an association with a subscriber, which is recognized by the policy holder. The dependent has an association with the subscriber such as a financial dependency or personal relationship such as that of a spouse, or a natural or adopted child. The policy holder may be required by law to recognize certain associations or may have discretion about the associations. For example, a policy holder may dictate the criteria for the dependent status of adult children who are students, such as requiring full time enrollment, or may recognize domestic partners as dependents. Under certain circumstances, the dependent may be under the indirect authority of a responsible party acting as a surrogate for the subscriber, for example, if the subscriber is differently-abled or deceased, a guardian ad Lidem or estate executor may be appointed to assume the subscriber's legal standing in the relationship with the dependent.
         */
        DEPEND, 
        /**
         * A person who has been granted the authority to represent or act on another's behalf generally in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts. Unlike ordinary powers of attorney, durable powers can survive for long periods of time, and again, unlike standard powers of attorney, durable powers can continue after incompetency.
         */
        DPOWATT, 
        /**
         * An entity to be contacted in the event of an emergency
         */
        EMGCON, 
        /**
         * A person who attests to observing an occurrence.  For example, the witness has observed a procedure and is attesting to this fact.
         */
        EVTWIT, 
        /**
         * A person who has been granted the authority to act as an estate executor for a deceased person who was the responsible party.
         */
        EXCEST, 
        /**
         * A person who grants to another person the authority to represent or act on that person's behalf.  Examples include (1) exercising specific rights belonging to the grantee; (2) performing specific duties on behalf of a grantee; and (3) making specific decisions concerning a grantee.
         */
        GRANTEE, 
        /**
         * A person who has been granted the authority to represent or act on another's behalf. Examples include (1) exercising specific rights belonging to the grantee; (2) performing specific duties on behalf of a grantee; and (3) making specific decisions concerning a grantee.
         */
        GRANTOR, 
        /**
         * A person or organization contractually recognized by the issuer as an entity that has assumed fiscal responsibility (e.g., by making or giving a promise, assurance, or pledge) for another entity's financial obligations by guaranteeing to pay for amounts owed to a particular account.  In a healthcare context, the account may be a patient's billing account for services rendered by a provider or a health plan premium account.
         */
        GUAR, 
        /**
         * A person or organization legally empowered with responsibility for the care of a ward.
         */
        GUARD, 
        /**
         * A person appointed by the court to look out for the best interests of a minor child during the course of legal proceedings.
         */
        GUADLTM, 
        /**
         * An entity that is the source of reported information (e.g., a next of kin who answers questions about the patient's history). For history questions, the patient is logically an informant, yet the informant of history questions is implicitly the subject.
         */
        INF, 
        /**
         * A person who converts spoken or written language into the language of key participants in an event such as when a provider is obtaining a patient's consent to treatment or permission to disclose information.
         */
        INTPRT, 
        /**
         * An entity that is the subject of an investigation. This role is scoped by the party responsible for the investigation.
         */
        INSBJ, 
        /**
         * A person who has been granted the authority to represent or act on another's behalf for healthcare related matters in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts. Examples include (1) exercising specific healthcare legal rights belonging to the grantee such as signing a consent directive; (2) performing specific healthcare related legal duties on behalf of a grantee such as claims payment; and (3) making specific healthcare legal decisions concerning a grantee such as consenting to healthcare services.
         */
        HPOWATT, 
        /**
         * An entity that is authorized to provide health care services by an authorizing organization or jurisdiction.
         */
        HPROV, 
        /**
         * A person in the role of verifier who attests to the accuracy of information content, and who has privileges to certify the legal authenticity of that content with a signature that constitutes a legal authentication.  For example, a licensed physician who signs a consult authored by a resident physician who authenticated it.
         */
        LEGAUTHN, 
        /**
         * A party to an insurance policy under which the insurer agrees to indemnify for losses, provides benefits for, or renders services. A named insured may be either a person, non-person living subject, or an organization, or a group of persons, non-person living subject that is the named insured under a comprehensive automobile, disability, or property and casualty policy.  The named insured and may or may not be the policy holder.
         */
        NMDINS, 
        /**
         * A person, who is a type of contact, designated to receive notifications on behalf of another person who is a relative.
         */
        NOK, 
        /**
         * The party credentialed to legally attest to the contract binding by verifying the identity and capacity of the grantor and grantee, and witnessing their signing of the contract or agreement such as a real estate transaction, pre-nuptial agreement, or a will.
         */
        NOTARY, 
        /**
         * A person, animal, or other living subject that is the actual or potential recipient of health care services.
         */
        PAT, 
        /**
         * A person who has been granted the authority to represent or act on another's behalf generally in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts. Examples include (1) exercising specific legal rights belonging to the grantee such as signing a contract; (2) performing specific legal duties on behalf of a grantee such as making loan payments; and (3) making specific legal decisions concerning a grantee such as financial investment decisions.
         */
        POWATT, 
        /**
         * An entity that is the primary or sole author of information content.  In the healthcare context, there can be only one primary author of health information content in a record entry or document.
         */
        PRIMAUTH, 
        /**
         * An entity that may, should receive, or has received information or an object to which it was primarily addressed.
         */
        PRIRECIP, 
        /**
         * An entity that may, should receive, or has received information or an object, which may not have been primarily addressed to it. For example, the staff of a provider, a clearinghouse, or other intermediary.
         */
        RECIP, 
        /**
         * An entity that has legal responsibility for another party.
         */
        RESPRSN, 
        /**
         * A person, device, or algorithm that has used approved criteria for filtered data for inclusion into the patient record.  Examples: (1) a medical records clerk who scans a document for inclusion in the medical record, enters header information, or catalogues and classifies the data, or a combination thereof; (2) a gateway that receives data from another computer system and interprets that data or changes its format, or both, before entering it into the patient record.
         */
        REVIEWER, 
        /**
         * An entity entering the data into the originating system. This includes the transcriptionist for dictated text transcribed into electronic form.
         */
        TRANS, 
        /**
         * An automated data source that generates a signature along with content. Examples: (1) the signature for an image that is generated by a device for inclusion in the patient record; (2) the signature for an ECG derived by an ECG system for inclusion in the patient record; (3) the data from a biomedical monitoring device or system that is for inclusion in the patient record.
         */
        SOURCE, 
        /**
         * A person who has been granted the authority to represent or act on another's behalf for a limited set of specific matters in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts. Examples include (1) exercising specific legal rights belonging to the grantee such as drafting a will; (2) performing specific legal duties on behalf of a grantee such as making a reversible mortgage to pay for end of life expenses; and (3) making specific legal decisions concerning a grantee such as managing a trust.
         */
        SPOWATT, 
        /**
         * A person who validates a health information document for inclusion in the patient record. For example, a medical student or resident is credentialed to perform history or physical examinations and to write progress notes. The attending physician signs the history and physical examination to validate the entry for inclusion in the patient's medical record.
         */
        VALID, 
        /**
         * A person who asserts the correctness and appropriateness of an act or the recording of the act, and is accountable for the assertion that the act or the recording of the act complies with jurisdictional or organizational policy. For example, a physician is required to countersign a verbal order that has previously been recorded in the medical record by a registered nurse who has carried out the verbal order.
         */
        VERF, 
        /**
         * A person witnessing the signature of another party. A witness is not knowledgeable about the content being signed, much less approves of anything stated in the content. For example, an advanced directive witness or a witness that a party to a contract signed that certain demographic or financial information is truthful.
         */
        WIT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ContractSignerType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AMENDER".equals(codeString))
          return AMENDER;
        if ("AUTHN".equals(codeString))
          return AUTHN;
        if ("AUT".equals(codeString))
          return AUT;
        if ("AFFL".equals(codeString))
          return AFFL;
        if ("AGNT".equals(codeString))
          return AGNT;
        if ("ASSIGNED".equals(codeString))
          return ASSIGNED;
        if ("CIT".equals(codeString))
          return CIT;
        if ("CLAIMANT".equals(codeString))
          return CLAIMANT;
        if ("COAUTH".equals(codeString))
          return COAUTH;
        if ("CONSENTER".equals(codeString))
          return CONSENTER;
        if ("CONSWIT".equals(codeString))
          return CONSWIT;
        if ("CONT".equals(codeString))
          return CONT;
        if ("COPART".equals(codeString))
          return COPART;
        if ("COVPTY".equals(codeString))
          return COVPTY;
        if ("DELEGATEE".equals(codeString))
          return DELEGATEE;
        if ("delegator".equals(codeString))
          return DELEGATOR;
        if ("DEPEND".equals(codeString))
          return DEPEND;
        if ("DPOWATT".equals(codeString))
          return DPOWATT;
        if ("EMGCON".equals(codeString))
          return EMGCON;
        if ("EVTWIT".equals(codeString))
          return EVTWIT;
        if ("EXCEST".equals(codeString))
          return EXCEST;
        if ("GRANTEE".equals(codeString))
          return GRANTEE;
        if ("GRANTOR".equals(codeString))
          return GRANTOR;
        if ("GUAR".equals(codeString))
          return GUAR;
        if ("GUARD".equals(codeString))
          return GUARD;
        if ("GUADLTM".equals(codeString))
          return GUADLTM;
        if ("INF".equals(codeString))
          return INF;
        if ("INTPRT".equals(codeString))
          return INTPRT;
        if ("INSBJ".equals(codeString))
          return INSBJ;
        if ("HPOWATT".equals(codeString))
          return HPOWATT;
        if ("HPROV".equals(codeString))
          return HPROV;
        if ("LEGAUTHN".equals(codeString))
          return LEGAUTHN;
        if ("NMDINS".equals(codeString))
          return NMDINS;
        if ("NOK".equals(codeString))
          return NOK;
        if ("NOTARY".equals(codeString))
          return NOTARY;
        if ("PAT".equals(codeString))
          return PAT;
        if ("POWATT".equals(codeString))
          return POWATT;
        if ("PRIMAUTH".equals(codeString))
          return PRIMAUTH;
        if ("PRIRECIP".equals(codeString))
          return PRIRECIP;
        if ("RECIP".equals(codeString))
          return RECIP;
        if ("RESPRSN".equals(codeString))
          return RESPRSN;
        if ("REVIEWER".equals(codeString))
          return REVIEWER;
        if ("TRANS".equals(codeString))
          return TRANS;
        if ("SOURCE".equals(codeString))
          return SOURCE;
        if ("SPOWATT".equals(codeString))
          return SPOWATT;
        if ("VALID".equals(codeString))
          return VALID;
        if ("VERF".equals(codeString))
          return VERF;
        if ("WIT".equals(codeString))
          return WIT;
        throw new FHIRException("Unknown ContractSignerType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AMENDER: return "AMENDER";
            case AUTHN: return "AUTHN";
            case AUT: return "AUT";
            case AFFL: return "AFFL";
            case AGNT: return "AGNT";
            case ASSIGNED: return "ASSIGNED";
            case CIT: return "CIT";
            case CLAIMANT: return "CLAIMANT";
            case COAUTH: return "COAUTH";
            case CONSENTER: return "CONSENTER";
            case CONSWIT: return "CONSWIT";
            case CONT: return "CONT";
            case COPART: return "COPART";
            case COVPTY: return "COVPTY";
            case DELEGATEE: return "DELEGATEE";
            case DELEGATOR: return "delegator";
            case DEPEND: return "DEPEND";
            case DPOWATT: return "DPOWATT";
            case EMGCON: return "EMGCON";
            case EVTWIT: return "EVTWIT";
            case EXCEST: return "EXCEST";
            case GRANTEE: return "GRANTEE";
            case GRANTOR: return "GRANTOR";
            case GUAR: return "GUAR";
            case GUARD: return "GUARD";
            case GUADLTM: return "GUADLTM";
            case INF: return "INF";
            case INTPRT: return "INTPRT";
            case INSBJ: return "INSBJ";
            case HPOWATT: return "HPOWATT";
            case HPROV: return "HPROV";
            case LEGAUTHN: return "LEGAUTHN";
            case NMDINS: return "NMDINS";
            case NOK: return "NOK";
            case NOTARY: return "NOTARY";
            case PAT: return "PAT";
            case POWATT: return "POWATT";
            case PRIMAUTH: return "PRIMAUTH";
            case PRIRECIP: return "PRIRECIP";
            case RECIP: return "RECIP";
            case RESPRSN: return "RESPRSN";
            case REVIEWER: return "REVIEWER";
            case TRANS: return "TRANS";
            case SOURCE: return "SOURCE";
            case SPOWATT: return "SPOWATT";
            case VALID: return "VALID";
            case VERF: return "VERF";
            case WIT: return "WIT";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://www.hl7.org/fhir/contractsignertypecodes";
        }
        public String getDefinition() {
          switch (this) {
            case AMENDER: return "A person who has corrected, edited, or amended pre-existing information.";
            case AUTHN: return "A person in the role of verifier who attests to the accuracy of an act, but who does not have privileges to legally authenticate information content. An example would be a resident physician who sees a patient and dictates a note, then later signs it. The resident's signature constitutes an authentication.";
            case AUT: return "An entity that authored specific content. There can be multiple authors of content, which may take such forms as a contract, a healthcare record entry or document, a policy, or a consent directive.";
            case AFFL: return "An entity that has a business or professional relationship with another entity in accordance with an agreement.";
            case AGNT: return "An entity that acts or is authorized to act on behalf of another entity in accordance with an agreement.";
            case ASSIGNED: return "An agent role in which the agent is an Entity acting in the employ of an organization. The focus is on functional role on behalf of the organization, unlike the Employee role where the focus is on the 'Human Resources' relationship between the employee and the organization.";
            case CIT: return "The member of a jurisdiction afforded certain rights and encumbered with certain obligation in accordance with jurisdictional policy.";
            case CLAIMANT: return "A party that makes a claim for coverage under a policy.";
            case COAUTH: return "The entity that co-authored content. There can be multiple co-authors of content,which may take such forms as a  such as a contract, a healthcare record entry or document, a policy, or a consent directive.";
            case CONSENTER: return "A patient or patient representative who is the grantee in a healthcare related agreement such as a consent for healthcare services, advanced directive, or a privacy consent directive in accordance with jurisdictional, organizational, or patient policy.";
            case CONSWIT: return "A person who has witnessed and attests to observing a patient being counseled about a healthcare related agreement such as a consent for healthcare services, advanced directive, or a privacy consent directive.";
            case CONT: return "A person or an organization that provides or receives information regarding another entity. Examples; patient NOK and emergency contacts; guarantor contact; employer contact.";
            case COPART: return "A person who participates in the generation of and attest to veracity of content, but is not an author or co-author. For example a surgeon who is required by institutional, regulatory, or legal rules to sign an operative report, but who was not involved in the authorship of that report.";
            case COVPTY: return "An entity, which is the insured, that receives benefits such as healthcare services, reimbursement for out-of-pocket expenses, or compensation for losses through coverage under the terms of an insurance policy. The underwriter of that policy is the scoping entity. The covered party receives coverage because of some contractual or other relationship with the holder of that policy. Note that a particular policy may cover several individuals one of whom may be, but need not be, the policy holder. Thus the notion of covered party is a role that is distinct from that of the policy holder.";
            case DELEGATEE: return "A party to whom some right or authority is delegated by a delegator.";
            case DELEGATOR: return "A party that delegates a right or authority to another party.";
            case DEPEND: return "A person covered under an insurance policy or program based on an association with a subscriber, which is recognized by the policy holder. The dependent has an association with the subscriber such as a financial dependency or personal relationship such as that of a spouse, or a natural or adopted child. The policy holder may be required by law to recognize certain associations or may have discretion about the associations. For example, a policy holder may dictate the criteria for the dependent status of adult children who are students, such as requiring full time enrollment, or may recognize domestic partners as dependents. Under certain circumstances, the dependent may be under the indirect authority of a responsible party acting as a surrogate for the subscriber, for example, if the subscriber is differently-abled or deceased, a guardian ad Lidem or estate executor may be appointed to assume the subscriber's legal standing in the relationship with the dependent.";
            case DPOWATT: return "A person who has been granted the authority to represent or act on another's behalf generally in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts. Unlike ordinary powers of attorney, durable powers can survive for long periods of time, and again, unlike standard powers of attorney, durable powers can continue after incompetency.";
            case EMGCON: return "An entity to be contacted in the event of an emergency";
            case EVTWIT: return "A person who attests to observing an occurrence.  For example, the witness has observed a procedure and is attesting to this fact.";
            case EXCEST: return "A person who has been granted the authority to act as an estate executor for a deceased person who was the responsible party.";
            case GRANTEE: return "A person who grants to another person the authority to represent or act on that person's behalf.  Examples include (1) exercising specific rights belonging to the grantee; (2) performing specific duties on behalf of a grantee; and (3) making specific decisions concerning a grantee.";
            case GRANTOR: return "A person who has been granted the authority to represent or act on another's behalf. Examples include (1) exercising specific rights belonging to the grantee; (2) performing specific duties on behalf of a grantee; and (3) making specific decisions concerning a grantee.";
            case GUAR: return "A person or organization contractually recognized by the issuer as an entity that has assumed fiscal responsibility (e.g., by making or giving a promise, assurance, or pledge) for another entity's financial obligations by guaranteeing to pay for amounts owed to a particular account.  In a healthcare context, the account may be a patient's billing account for services rendered by a provider or a health plan premium account.";
            case GUARD: return "A person or organization legally empowered with responsibility for the care of a ward.";
            case GUADLTM: return "A person appointed by the court to look out for the best interests of a minor child during the course of legal proceedings.";
            case INF: return "An entity that is the source of reported information (e.g., a next of kin who answers questions about the patient's history). For history questions, the patient is logically an informant, yet the informant of history questions is implicitly the subject.";
            case INTPRT: return "A person who converts spoken or written language into the language of key participants in an event such as when a provider is obtaining a patient's consent to treatment or permission to disclose information.";
            case INSBJ: return "An entity that is the subject of an investigation. This role is scoped by the party responsible for the investigation.";
            case HPOWATT: return "A person who has been granted the authority to represent or act on another's behalf for healthcare related matters in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts. Examples include (1) exercising specific healthcare legal rights belonging to the grantee such as signing a consent directive; (2) performing specific healthcare related legal duties on behalf of a grantee such as claims payment; and (3) making specific healthcare legal decisions concerning a grantee such as consenting to healthcare services.";
            case HPROV: return "An entity that is authorized to provide health care services by an authorizing organization or jurisdiction.";
            case LEGAUTHN: return "A person in the role of verifier who attests to the accuracy of information content, and who has privileges to certify the legal authenticity of that content with a signature that constitutes a legal authentication.  For example, a licensed physician who signs a consult authored by a resident physician who authenticated it.";
            case NMDINS: return "A party to an insurance policy under which the insurer agrees to indemnify for losses, provides benefits for, or renders services. A named insured may be either a person, non-person living subject, or an organization, or a group of persons, non-person living subject that is the named insured under a comprehensive automobile, disability, or property and casualty policy.  The named insured and may or may not be the policy holder.";
            case NOK: return "A person, who is a type of contact, designated to receive notifications on behalf of another person who is a relative.";
            case NOTARY: return "The party credentialed to legally attest to the contract binding by verifying the identity and capacity of the grantor and grantee, and witnessing their signing of the contract or agreement such as a real estate transaction, pre-nuptial agreement, or a will.";
            case PAT: return "A person, animal, or other living subject that is the actual or potential recipient of health care services.";
            case POWATT: return "A person who has been granted the authority to represent or act on another's behalf generally in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts. Examples include (1) exercising specific legal rights belonging to the grantee such as signing a contract; (2) performing specific legal duties on behalf of a grantee such as making loan payments; and (3) making specific legal decisions concerning a grantee such as financial investment decisions.";
            case PRIMAUTH: return "An entity that is the primary or sole author of information content.  In the healthcare context, there can be only one primary author of health information content in a record entry or document.";
            case PRIRECIP: return "An entity that may, should receive, or has received information or an object to which it was primarily addressed.";
            case RECIP: return "An entity that may, should receive, or has received information or an object, which may not have been primarily addressed to it. For example, the staff of a provider, a clearinghouse, or other intermediary.";
            case RESPRSN: return "An entity that has legal responsibility for another party.";
            case REVIEWER: return "A person, device, or algorithm that has used approved criteria for filtered data for inclusion into the patient record.  Examples: (1) a medical records clerk who scans a document for inclusion in the medical record, enters header information, or catalogues and classifies the data, or a combination thereof; (2) a gateway that receives data from another computer system and interprets that data or changes its format, or both, before entering it into the patient record.";
            case TRANS: return "An entity entering the data into the originating system. This includes the transcriptionist for dictated text transcribed into electronic form.";
            case SOURCE: return "An automated data source that generates a signature along with content. Examples: (1) the signature for an image that is generated by a device for inclusion in the patient record; (2) the signature for an ECG derived by an ECG system for inclusion in the patient record; (3) the data from a biomedical monitoring device or system that is for inclusion in the patient record.";
            case SPOWATT: return "A person who has been granted the authority to represent or act on another's behalf for a limited set of specific matters in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts. Examples include (1) exercising specific legal rights belonging to the grantee such as drafting a will; (2) performing specific legal duties on behalf of a grantee such as making a reversible mortgage to pay for end of life expenses; and (3) making specific legal decisions concerning a grantee such as managing a trust.";
            case VALID: return "A person who validates a health information document for inclusion in the patient record. For example, a medical student or resident is credentialed to perform history or physical examinations and to write progress notes. The attending physician signs the history and physical examination to validate the entry for inclusion in the patient's medical record.";
            case VERF: return "A person who asserts the correctness and appropriateness of an act or the recording of the act, and is accountable for the assertion that the act or the recording of the act complies with jurisdictional or organizational policy. For example, a physician is required to countersign a verbal order that has previously been recorded in the medical record by a registered nurse who has carried out the verbal order.";
            case WIT: return "A person witnessing the signature of another party. A witness is not knowledgeable about the content being signed, much less approves of anything stated in the content. For example, an advanced directive witness or a witness that a party to a contract signed that certain demographic or financial information is truthful.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AMENDER: return "Amender";
            case AUTHN: return "Authenticator";
            case AUT: return "Author";
            case AFFL: return "Affiliate";
            case AGNT: return "Agent";
            case ASSIGNED: return "Assigned Entity";
            case CIT: return "Citizen";
            case CLAIMANT: return "Claimant";
            case COAUTH: return "Co-Author";
            case CONSENTER: return "Consenter";
            case CONSWIT: return "Consent Witness";
            case CONT: return "Contact";
            case COPART: return "Co-Participant";
            case COVPTY: return "Covered Party";
            case DELEGATEE: return "Delegatee";
            case DELEGATOR: return "Delegator";
            case DEPEND: return "Dependent";
            case DPOWATT: return "Durable Power of Attorney";
            case EMGCON: return "Emergency Contact";
            case EVTWIT: return "Event Witness";
            case EXCEST: return "Executor of Estate";
            case GRANTEE: return "Grantee";
            case GRANTOR: return "Grantor";
            case GUAR: return "Guarantor";
            case GUARD: return "Guardian";
            case GUADLTM: return "Guardian ad lidem";
            case INF: return "Informant";
            case INTPRT: return "Interpreter";
            case INSBJ: return "Investigation Subject";
            case HPOWATT: return "Healthcare Power of Attorney";
            case HPROV: return "Healthcare Provider";
            case LEGAUTHN: return "Legal Authenticator";
            case NMDINS: return "Named Insured";
            case NOK: return "Next of Kin";
            case NOTARY: return "Notary";
            case PAT: return "Patient";
            case POWATT: return "Power of Attorney";
            case PRIMAUTH: return "Primary Author";
            case PRIRECIP: return "Primary Responsible Party ";
            case RECIP: return "Recipient";
            case RESPRSN: return "Responsible Party";
            case REVIEWER: return "Reviewer";
            case TRANS: return "Transcriber";
            case SOURCE: return "Source";
            case SPOWATT: return "Apecial Power of Attorney";
            case VALID: return "Validator";
            case VERF: return "Verifier";
            case WIT: return "Witness";
            default: return "?";
          }
    }


}

