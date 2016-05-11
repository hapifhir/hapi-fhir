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


public enum V3ParticipationFunction {

        /**
         * This code is used to specify the exact function an actor is authorized to have in a service in all necessary detail.
         */
        _AUTHORIZEDPARTICIPATIONFUNCTION, 
        /**
         * This code is used to specify the exact function an actor is authorized to have as a receiver of information that is the subject of a consent directive or consent override.
         */
        _AUTHORIZEDRECEIVERPARTICIPATIONFUNCTION, 
        /**
         * Description:Caregiver authorized to receive patient health information.
         */
        AUCG, 
        /**
         * Description:Provider with legitimate relationship authorized to receive patient health information.
         */
        AULR, 
        /**
         * Description:Member of care team authorized to receive patient health information.
         */
        AUTM, 
        /**
         * Description:Entities within specified work area authorized to receive patient health information.
         */
        AUWA, 
        /**
         * This code is used to specify the exact function an actor is authorized to have in authoring a consent directive.
         */
        _CONSENTERPARTICIPATIONFUNCTION, 
        /**
         * Description:Legal guardian of the subject of consent authorized to author a consent directive for the subject of consent.
         */
        GRDCON, 
        /**
         * Description:Person authorized with healthcare power of attorney to author a  consent directive for the subject of consent.
         */
        POACON, 
        /**
         * Description:Personal representative of the subject of consent authorized to author a consent directive for the subject of consent.
         */
        PRCON, 
        /**
         * Definition:Provider authorized to mask information to protect the patient, a third party, or to ensure that the provider has consulted with the patient prior to release of this information.
         */
        PROMSK, 
        /**
         * Description:Subject of consent authorized to author a consent directive.
         */
        SUBCON, 
        /**
         * This code is used to specify the exact function an actor is authorized to have in authoring a consent override.
         */
        _OVERRIDERPARTICIPATIONFUNCTION, 
        /**
         * Description:Entity authorized to override a consent directive.
         */
        AUCOV, 
        /**
         * Description:Entity  authorized to override a consent directive or privacy policy in an emergency.
         */
        AUEMROV, 
        /**
         * Definition: Set of codes indicating the manner in which sponsors, underwriters, and payers participate in a policy or program.
         */
        _COVERAGEPARTICIPATIONFUNCTION, 
        /**
         * Definition: Set of codes indicating the manner in which payors participate in a policy or program.</
         */
        _PAYORPARTICIPATIONFUNCTION, 
        /**
         * Definition: Manages all operations required to adjudicate fee for service claims or managed care encounter reports.
         */
        CLMADJ, 
        /**
         * Definition: Managing the enrollment of covered parties.
         */
        ENROLL, 
        /**
         * Definition: Managing all operations required to administer a fee for service or indemnity health plan including enrolling covered parties and providing customer service, provider contracting, claims payment, care management and utilization review.
         */
        FFSMGT, 
        /**
         * Definition: Managing all operations required to administer a managed care plan including enrolling covered parties and providing customer service,, provider contracting, claims payment, care management and utilization review.
         */
        MCMGT, 
        /**
         * Definition: Managing provider contracting, provider services, credentialing, profiling, performance measures, and ensuring network adequacy.
         */
        PROVMGT, 
        /**
         * Definition: Managing utilization of services by ensuring that providers adhere to, e.g. payeraTMs clinical protocols for medical appropriateness and standards of medical necessity.  May include management of authorizations for services and referrals.
         */
        UMGT, 
        /**
         * Definition: Set of codes indicating the manner in which sponsors participate in a policy or program. NOTE: use only when the Sponsor is not further specified with a SponsorRoleType as being either a fully insured sponsor or a self insured sponsor.
         */
        _SPONSORPARTICIPATIONFUNCTION, 
        /**
         * Definition: Responsibility taken by a sponsor to contract with one or more underwriters for the assumption of full responsibility for the risk and administration of a policy or program.
         */
        FULINRD, 
        /**
         * Definition: Responsibility taken by a sponsor to organize the underwriting of risk and administration of a policy or program.
         */
        SELFINRD, 
        /**
         * Definition: Set of codes indicating the manner in which underwriters participate in a policy or program.
         */
        _UNDERWRITERPARTICIPATIONFUNCTION, 
        /**
         * Definition: Contracting for the provision and administration of health services to payors while retaining the risk for coverage.  Contracting may be for all provision and administration; or for provision of certain types of services; for provision of services by region; and by types of administration, e.g. claims adjudication, enrollment, provider management, and utilization management.  Typically done by underwriters for sponsors who need coverage provided to covered parties in multiple regions.  The underwriter may act as the payor in some, but not all of the regions in which coverage is provided.
         */
        PAYORCNTR, 
        /**
         * Definition: Underwriting reinsurance for another underwriter for the policy or program.
         */
        REINS, 
        /**
         * Definition: Underwriting reinsurance for another reinsurer.
         */
        RETROCES, 
        /**
         * Definition: Delegating risk for a policy or program to one or more subcontracting underwriters, e.g. a major health insurer may delegate risk for provision of coverage under a national health plan to other underwriters by region .
         */
        SUBCTRT, 
        /**
         * Definition: Provision of underwriting analysis for another underwriter without assumption of risk.
         */
        UNDERWRTNG, 
        /**
         * A physician who admitted a patient to a hospital or other care unit that is the context of this service.
         */
        ADMPHYS, 
        /**
         * In a typical anesthesia setting an anesthesiologist or anesthesia resident in charge of the anesthesia and life support, but only a witness to the surgical procedure itself.  To clarify responsibilities anesthesia should always be represented as a separate service related to the surgery.
         */
        ANEST, 
        /**
         * In a typical anesthesia setting the nurse principally assisting the anesthesiologist during the critical periods.
         */
        ANRS, 
        /**
         * A device that operates independently of an author on custodian's algorithms for data extraction of existing information for purpose of generating a new artifact.
                           UsageConstraint: ASSEMBLER ParticipationFunction should be used with DEV (device) ParticipationType.
         */
        ASSEMBLER, 
        /**
         * A physician who is primarily responsible for a patient during the hospitalization, which is the context of the service.
         */
        ATTPHYS, 
        /**
         * A device used by an author to record new information, which may also be used by the author to select existing information for aggregation with newly recorded information for the purpose of generating a new artifact.
                           UsageConstraint: COMPOSER ParticipationFunction should be used with DEV (device) ParticipationType.

                        
                           Usage Note: This code will enable implementers to more specifically represent the manner in which a Device participated in and facilitated the generation of a CDA Clinical Document or a CDA Entry by the responsible Author, which is comprised of the Author's newly entered content, and may include the pre-existing content selected by the Author, for the purpose of establishing the provenance and accountability for these acts.
         */
        COMPOSER, 
        /**
         * A physician who discharged a patient from a hospital or other care unit that is the context of this service.
         */
        DISPHYS, 
        /**
         * In a typical surgery setting the assistant facing the primary surgeon.  The first assistant performs parts of the operation and assists in others (e.g. incision, approach, electrocoutering, ligatures, sutures).
         */
        FASST, 
        /**
         * A person (usually female) helping a woman deliver a baby. Responsibilities vary locally, ranging from a mere optional assistant to a full required participant, responsible for (normal) births and pre- and post-natal care for both mother and baby.
         */
        MDWF, 
        /**
         * In a typical surgery setting the non-sterile nurse handles material supply from the stock, forwards specimen to pathology, and helps with other non-sterile tasks (e.g. phone calls, etc.).
         */
        NASST, 
        /**
         * The healthcare provider that holds primary responsibility for the overall care of a patient.
         */
        PCP, 
        /**
         * In a typical surgery setting the primary performing surgeon.
         */
        PRISURG, 
        /**
         * A verifier who is accountable for reviewing and asserting that the verification of an Act complies with jurisdictional or organizational policy.

                        
                           UsageConstraint: UsageConstraint:  Specifies the exact function that an actor is authorized to have as a verifier of an Act.  Connotes that a specialized verifier asserts compliance for veracity of the review per jurisdictional or organizational policy; e.g. The Provider who takes responsibility for authenticity of a record submitted to a payer.

                        REVIEW ParticipationFunction should be used with VFR (verifier)
         */
        REVIEWER, 
        /**
         * A physician who made rounds on a patient in a hospital or other care center.
         */
        RNDPHYS, 
        /**
         * In a typical surgery setting the assistant who primarily holds the hooks.
         */
        SASST, 
        /**
         * In a typical surgery setting the nurse in charge of the instrumentation.
         */
        SNRS, 
        /**
         * In a typical surgery setting there is rarely a third assistant (e.g. in some Hip operations the third assistant postures the affected leg).
         */
        TASST, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ParticipationFunction fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_AuthorizedParticipationFunction".equals(codeString))
          return _AUTHORIZEDPARTICIPATIONFUNCTION;
        if ("_AuthorizedReceiverParticipationFunction".equals(codeString))
          return _AUTHORIZEDRECEIVERPARTICIPATIONFUNCTION;
        if ("AUCG".equals(codeString))
          return AUCG;
        if ("AULR".equals(codeString))
          return AULR;
        if ("AUTM".equals(codeString))
          return AUTM;
        if ("AUWA".equals(codeString))
          return AUWA;
        if ("_ConsenterParticipationFunction".equals(codeString))
          return _CONSENTERPARTICIPATIONFUNCTION;
        if ("GRDCON".equals(codeString))
          return GRDCON;
        if ("POACON".equals(codeString))
          return POACON;
        if ("PRCON".equals(codeString))
          return PRCON;
        if ("PROMSK".equals(codeString))
          return PROMSK;
        if ("SUBCON".equals(codeString))
          return SUBCON;
        if ("_OverriderParticipationFunction".equals(codeString))
          return _OVERRIDERPARTICIPATIONFUNCTION;
        if ("AUCOV".equals(codeString))
          return AUCOV;
        if ("AUEMROV".equals(codeString))
          return AUEMROV;
        if ("_CoverageParticipationFunction".equals(codeString))
          return _COVERAGEPARTICIPATIONFUNCTION;
        if ("_PayorParticipationFunction".equals(codeString))
          return _PAYORPARTICIPATIONFUNCTION;
        if ("CLMADJ".equals(codeString))
          return CLMADJ;
        if ("ENROLL".equals(codeString))
          return ENROLL;
        if ("FFSMGT".equals(codeString))
          return FFSMGT;
        if ("MCMGT".equals(codeString))
          return MCMGT;
        if ("PROVMGT".equals(codeString))
          return PROVMGT;
        if ("UMGT".equals(codeString))
          return UMGT;
        if ("_SponsorParticipationFunction".equals(codeString))
          return _SPONSORPARTICIPATIONFUNCTION;
        if ("FULINRD".equals(codeString))
          return FULINRD;
        if ("SELFINRD".equals(codeString))
          return SELFINRD;
        if ("_UnderwriterParticipationFunction".equals(codeString))
          return _UNDERWRITERPARTICIPATIONFUNCTION;
        if ("PAYORCNTR".equals(codeString))
          return PAYORCNTR;
        if ("REINS".equals(codeString))
          return REINS;
        if ("RETROCES".equals(codeString))
          return RETROCES;
        if ("SUBCTRT".equals(codeString))
          return SUBCTRT;
        if ("UNDERWRTNG".equals(codeString))
          return UNDERWRTNG;
        if ("ADMPHYS".equals(codeString))
          return ADMPHYS;
        if ("ANEST".equals(codeString))
          return ANEST;
        if ("ANRS".equals(codeString))
          return ANRS;
        if ("ASSEMBLER".equals(codeString))
          return ASSEMBLER;
        if ("ATTPHYS".equals(codeString))
          return ATTPHYS;
        if ("COMPOSER".equals(codeString))
          return COMPOSER;
        if ("DISPHYS".equals(codeString))
          return DISPHYS;
        if ("FASST".equals(codeString))
          return FASST;
        if ("MDWF".equals(codeString))
          return MDWF;
        if ("NASST".equals(codeString))
          return NASST;
        if ("PCP".equals(codeString))
          return PCP;
        if ("PRISURG".equals(codeString))
          return PRISURG;
        if ("REVIEWER".equals(codeString))
          return REVIEWER;
        if ("RNDPHYS".equals(codeString))
          return RNDPHYS;
        if ("SASST".equals(codeString))
          return SASST;
        if ("SNRS".equals(codeString))
          return SNRS;
        if ("TASST".equals(codeString))
          return TASST;
        throw new Exception("Unknown V3ParticipationFunction code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _AUTHORIZEDPARTICIPATIONFUNCTION: return "_AuthorizedParticipationFunction";
            case _AUTHORIZEDRECEIVERPARTICIPATIONFUNCTION: return "_AuthorizedReceiverParticipationFunction";
            case AUCG: return "AUCG";
            case AULR: return "AULR";
            case AUTM: return "AUTM";
            case AUWA: return "AUWA";
            case _CONSENTERPARTICIPATIONFUNCTION: return "_ConsenterParticipationFunction";
            case GRDCON: return "GRDCON";
            case POACON: return "POACON";
            case PRCON: return "PRCON";
            case PROMSK: return "PROMSK";
            case SUBCON: return "SUBCON";
            case _OVERRIDERPARTICIPATIONFUNCTION: return "_OverriderParticipationFunction";
            case AUCOV: return "AUCOV";
            case AUEMROV: return "AUEMROV";
            case _COVERAGEPARTICIPATIONFUNCTION: return "_CoverageParticipationFunction";
            case _PAYORPARTICIPATIONFUNCTION: return "_PayorParticipationFunction";
            case CLMADJ: return "CLMADJ";
            case ENROLL: return "ENROLL";
            case FFSMGT: return "FFSMGT";
            case MCMGT: return "MCMGT";
            case PROVMGT: return "PROVMGT";
            case UMGT: return "UMGT";
            case _SPONSORPARTICIPATIONFUNCTION: return "_SponsorParticipationFunction";
            case FULINRD: return "FULINRD";
            case SELFINRD: return "SELFINRD";
            case _UNDERWRITERPARTICIPATIONFUNCTION: return "_UnderwriterParticipationFunction";
            case PAYORCNTR: return "PAYORCNTR";
            case REINS: return "REINS";
            case RETROCES: return "RETROCES";
            case SUBCTRT: return "SUBCTRT";
            case UNDERWRTNG: return "UNDERWRTNG";
            case ADMPHYS: return "ADMPHYS";
            case ANEST: return "ANEST";
            case ANRS: return "ANRS";
            case ASSEMBLER: return "ASSEMBLER";
            case ATTPHYS: return "ATTPHYS";
            case COMPOSER: return "COMPOSER";
            case DISPHYS: return "DISPHYS";
            case FASST: return "FASST";
            case MDWF: return "MDWF";
            case NASST: return "NASST";
            case PCP: return "PCP";
            case PRISURG: return "PRISURG";
            case REVIEWER: return "REVIEWER";
            case RNDPHYS: return "RNDPHYS";
            case SASST: return "SASST";
            case SNRS: return "SNRS";
            case TASST: return "TASST";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ParticipationFunction";
        }
        public String getDefinition() {
          switch (this) {
            case _AUTHORIZEDPARTICIPATIONFUNCTION: return "This code is used to specify the exact function an actor is authorized to have in a service in all necessary detail.";
            case _AUTHORIZEDRECEIVERPARTICIPATIONFUNCTION: return "This code is used to specify the exact function an actor is authorized to have as a receiver of information that is the subject of a consent directive or consent override.";
            case AUCG: return "Description:Caregiver authorized to receive patient health information.";
            case AULR: return "Description:Provider with legitimate relationship authorized to receive patient health information.";
            case AUTM: return "Description:Member of care team authorized to receive patient health information.";
            case AUWA: return "Description:Entities within specified work area authorized to receive patient health information.";
            case _CONSENTERPARTICIPATIONFUNCTION: return "This code is used to specify the exact function an actor is authorized to have in authoring a consent directive.";
            case GRDCON: return "Description:Legal guardian of the subject of consent authorized to author a consent directive for the subject of consent.";
            case POACON: return "Description:Person authorized with healthcare power of attorney to author a  consent directive for the subject of consent.";
            case PRCON: return "Description:Personal representative of the subject of consent authorized to author a consent directive for the subject of consent.";
            case PROMSK: return "Definition:Provider authorized to mask information to protect the patient, a third party, or to ensure that the provider has consulted with the patient prior to release of this information.";
            case SUBCON: return "Description:Subject of consent authorized to author a consent directive.";
            case _OVERRIDERPARTICIPATIONFUNCTION: return "This code is used to specify the exact function an actor is authorized to have in authoring a consent override.";
            case AUCOV: return "Description:Entity authorized to override a consent directive.";
            case AUEMROV: return "Description:Entity  authorized to override a consent directive or privacy policy in an emergency.";
            case _COVERAGEPARTICIPATIONFUNCTION: return "Definition: Set of codes indicating the manner in which sponsors, underwriters, and payers participate in a policy or program.";
            case _PAYORPARTICIPATIONFUNCTION: return "Definition: Set of codes indicating the manner in which payors participate in a policy or program.</";
            case CLMADJ: return "Definition: Manages all operations required to adjudicate fee for service claims or managed care encounter reports.";
            case ENROLL: return "Definition: Managing the enrollment of covered parties.";
            case FFSMGT: return "Definition: Managing all operations required to administer a fee for service or indemnity health plan including enrolling covered parties and providing customer service, provider contracting, claims payment, care management and utilization review.";
            case MCMGT: return "Definition: Managing all operations required to administer a managed care plan including enrolling covered parties and providing customer service,, provider contracting, claims payment, care management and utilization review.";
            case PROVMGT: return "Definition: Managing provider contracting, provider services, credentialing, profiling, performance measures, and ensuring network adequacy.";
            case UMGT: return "Definition: Managing utilization of services by ensuring that providers adhere to, e.g. payeraTMs clinical protocols for medical appropriateness and standards of medical necessity.  May include management of authorizations for services and referrals.";
            case _SPONSORPARTICIPATIONFUNCTION: return "Definition: Set of codes indicating the manner in which sponsors participate in a policy or program. NOTE: use only when the Sponsor is not further specified with a SponsorRoleType as being either a fully insured sponsor or a self insured sponsor.";
            case FULINRD: return "Definition: Responsibility taken by a sponsor to contract with one or more underwriters for the assumption of full responsibility for the risk and administration of a policy or program.";
            case SELFINRD: return "Definition: Responsibility taken by a sponsor to organize the underwriting of risk and administration of a policy or program.";
            case _UNDERWRITERPARTICIPATIONFUNCTION: return "Definition: Set of codes indicating the manner in which underwriters participate in a policy or program.";
            case PAYORCNTR: return "Definition: Contracting for the provision and administration of health services to payors while retaining the risk for coverage.  Contracting may be for all provision and administration; or for provision of certain types of services; for provision of services by region; and by types of administration, e.g. claims adjudication, enrollment, provider management, and utilization management.  Typically done by underwriters for sponsors who need coverage provided to covered parties in multiple regions.  The underwriter may act as the payor in some, but not all of the regions in which coverage is provided.";
            case REINS: return "Definition: Underwriting reinsurance for another underwriter for the policy or program.";
            case RETROCES: return "Definition: Underwriting reinsurance for another reinsurer.";
            case SUBCTRT: return "Definition: Delegating risk for a policy or program to one or more subcontracting underwriters, e.g. a major health insurer may delegate risk for provision of coverage under a national health plan to other underwriters by region .";
            case UNDERWRTNG: return "Definition: Provision of underwriting analysis for another underwriter without assumption of risk.";
            case ADMPHYS: return "A physician who admitted a patient to a hospital or other care unit that is the context of this service.";
            case ANEST: return "In a typical anesthesia setting an anesthesiologist or anesthesia resident in charge of the anesthesia and life support, but only a witness to the surgical procedure itself.  To clarify responsibilities anesthesia should always be represented as a separate service related to the surgery.";
            case ANRS: return "In a typical anesthesia setting the nurse principally assisting the anesthesiologist during the critical periods.";
            case ASSEMBLER: return "A device that operates independently of an author on custodian's algorithms for data extraction of existing information for purpose of generating a new artifact.\n                           UsageConstraint: ASSEMBLER ParticipationFunction should be used with DEV (device) ParticipationType.";
            case ATTPHYS: return "A physician who is primarily responsible for a patient during the hospitalization, which is the context of the service.";
            case COMPOSER: return "A device used by an author to record new information, which may also be used by the author to select existing information for aggregation with newly recorded information for the purpose of generating a new artifact.\n                           UsageConstraint: COMPOSER ParticipationFunction should be used with DEV (device) ParticipationType.\r\n\n                        \n                           Usage Note: This code will enable implementers to more specifically represent the manner in which a Device participated in and facilitated the generation of a CDA Clinical Document or a CDA Entry by the responsible Author, which is comprised of the Author's newly entered content, and may include the pre-existing content selected by the Author, for the purpose of establishing the provenance and accountability for these acts.";
            case DISPHYS: return "A physician who discharged a patient from a hospital or other care unit that is the context of this service.";
            case FASST: return "In a typical surgery setting the assistant facing the primary surgeon.  The first assistant performs parts of the operation and assists in others (e.g. incision, approach, electrocoutering, ligatures, sutures).";
            case MDWF: return "A person (usually female) helping a woman deliver a baby. Responsibilities vary locally, ranging from a mere optional assistant to a full required participant, responsible for (normal) births and pre- and post-natal care for both mother and baby.";
            case NASST: return "In a typical surgery setting the non-sterile nurse handles material supply from the stock, forwards specimen to pathology, and helps with other non-sterile tasks (e.g. phone calls, etc.).";
            case PCP: return "The healthcare provider that holds primary responsibility for the overall care of a patient.";
            case PRISURG: return "In a typical surgery setting the primary performing surgeon.";
            case REVIEWER: return "A verifier who is accountable for reviewing and asserting that the verification of an Act complies with jurisdictional or organizational policy.\r\n\n                        \n                           UsageConstraint: UsageConstraint:  Specifies the exact function that an actor is authorized to have as a verifier of an Act.  Connotes that a specialized verifier asserts compliance for veracity of the review per jurisdictional or organizational policy; e.g. The Provider who takes responsibility for authenticity of a record submitted to a payer.\r\n\n                        REVIEW ParticipationFunction should be used with VFR (verifier)";
            case RNDPHYS: return "A physician who made rounds on a patient in a hospital or other care center.";
            case SASST: return "In a typical surgery setting the assistant who primarily holds the hooks.";
            case SNRS: return "In a typical surgery setting the nurse in charge of the instrumentation.";
            case TASST: return "In a typical surgery setting there is rarely a third assistant (e.g. in some Hip operations the third assistant postures the affected leg).";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _AUTHORIZEDPARTICIPATIONFUNCTION: return "AuthorizedParticipationFunction";
            case _AUTHORIZEDRECEIVERPARTICIPATIONFUNCTION: return "AuthorizedReceiverParticipationFunction";
            case AUCG: return "caregiver information receiver";
            case AULR: return "legitimate relationship information receiver";
            case AUTM: return "care team information receiver";
            case AUWA: return "work area information receiver";
            case _CONSENTERPARTICIPATIONFUNCTION: return "ConsenterParticipationFunction";
            case GRDCON: return "legal guardian consent author";
            case POACON: return "healthcare power of attorney consent author";
            case PRCON: return "personal representative consent author";
            case PROMSK: return "authorized provider masking author";
            case SUBCON: return "subject of consent author";
            case _OVERRIDERPARTICIPATIONFUNCTION: return "OverriderParticipationFunction";
            case AUCOV: return "consent overrider";
            case AUEMROV: return "emergency overrider";
            case _COVERAGEPARTICIPATIONFUNCTION: return "CoverageParticipationFunction";
            case _PAYORPARTICIPATIONFUNCTION: return "PayorParticipationFunction";
            case CLMADJ: return "claims adjudication";
            case ENROLL: return "enrollment broker";
            case FFSMGT: return "ffs management";
            case MCMGT: return "managed care management";
            case PROVMGT: return "provider management";
            case UMGT: return "utilization management";
            case _SPONSORPARTICIPATIONFUNCTION: return "SponsorParticipationFunction";
            case FULINRD: return "fully insured";
            case SELFINRD: return "self insured";
            case _UNDERWRITERPARTICIPATIONFUNCTION: return "UnderwriterParticipationFunction";
            case PAYORCNTR: return "payor contracting";
            case REINS: return "reinsures";
            case RETROCES: return "retrocessionaires";
            case SUBCTRT: return "subcontracting risk";
            case UNDERWRTNG: return "underwriting";
            case ADMPHYS: return "admitting physician";
            case ANEST: return "anesthesist";
            case ANRS: return "anesthesia nurse";
            case ASSEMBLER: return "assembly software";
            case ATTPHYS: return "attending physician";
            case COMPOSER: return "composer software";
            case DISPHYS: return "discharging physician";
            case FASST: return "first assistant surgeon";
            case MDWF: return "midwife";
            case NASST: return "nurse assistant";
            case PCP: return "primary care physician";
            case PRISURG: return "primary surgeon";
            case REVIEWER: return "reviewer";
            case RNDPHYS: return "rounding physician";
            case SASST: return "second assistant surgeon";
            case SNRS: return "scrub nurse";
            case TASST: return "third assistant";
            default: return "?";
          }
    }


}

