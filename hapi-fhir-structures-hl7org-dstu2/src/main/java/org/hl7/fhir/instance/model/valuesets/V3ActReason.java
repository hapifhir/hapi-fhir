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

// Generated on Sat, Aug 22, 2015 23:00-0400 for FHIR v0.5.0


public enum V3ActReason {

        /**
         * Identifies the reason the patient is assigned to this accommodation type
         */
        _ACTACCOMMODATIONREASON, 
        /**
         * Accommodation requested is not available.
         */
        ACCREQNA, 
        /**
         * Accommodation is assigned for floor convenience.
         */
        FLRCNV, 
        /**
         * Required for medical reasons(s).
         */
        MEDNEC, 
        /**
         * The Patient requested the action
         */
        PAT, 
        /**
         * Description:Codes used to specify reasons or criteria relating to coverage provided under a policy or program.  May be used to convey reasons pertaining to coverage contractual provisions, including criteria for eligibility, coverage limitations, coverage maximums, or financial participation required of covered parties.
         */
        _ACTCOVERAGEREASON, 
        /**
         * Identifies the reason or rational for why a person is eligibile for benefits under an insurance policy or progam. 

                        
                           Examples:  A person is a claimant under an automobile insurance policy are client deceased & adopted client has been given a new policy identifier.  A new employee is eligible for health insurance as an employment benefit.  A person meets a government program eligibility criteria for financial, age or health status.
         */
        _ELIGIBILITYACTREASONCODE, 
        /**
         * Identifies the reason or rational for why a person is not eligibile for benefits under an insurance policy.

                        Examples are client deceased & adopted client has been given a new policy identifier.
         */
        _ACTINELIGIBILITYREASON, 
        /**
         * When a client has no contact with the health system for an extended period, coverage is suspended.  Client will be reinstated to original start date upon proof of identification, residency etc.

                        Example: Coverage may be suspended during a strike situation, when employer benefits for employees are not covered (i.e. not in effect).
         */
        COVSUS, 
        /**
         * Client deceased.
         */
        DECSD, 
        /**
         * Client was registered in error.
         */
        REGERR, 
        /**
         * Definition: Identifies the reason or rational for why a person is eligibile for benefits under an insurance policy or progam. 

                        
                           Examples:  A person is a claimant under an automobile insurance policy are client deceased & adopted client has been given a new policy identifier.  A new employee is eligible for health insurance as an employment benefit.  A person meets a government program eligibility criteria for financial, age or health status.
         */
        _COVERAGEELIGIBILITYREASON, 
        /**
         * A person becomes eligible for a program based on age.

                        
                           Example:  In the U.S., a person who is 65 years of age or older is eligible for Medicare.
         */
        AGE, 
        /**
         * A person becomes eligible for insurance or a program because of crime related health condition or injury. 

                        
                           Example:  A person is a claimant under the U.S. Crime Victims Compensation program.
         */
        CRIME, 
        /**
         * A person becomes a claimant under a disability income insurance policy or a disability rehabilitation program because of a health condition or injury which limits the person's ability to earn an income or function without institutionalization.
         */
        DIS, 
        /**
         * A person becomes eligible for insurance provided as an employment benefit based on employment status.
         */
        EMPLOY, 
        /**
         * A person becomes eligible for a program based on financial criteria.

                        
                           Example:  A person whose family income is below a financial threshold for eligibility for Medicaid or SCHIP.
         */
        FINAN, 
        /**
         * A person becomes eligible for a program because of a qualifying health condition or injury. 

                        
                           Examples:  A person is determined to have a qualifying health conditions include pregnancy, HIV/AIDs, tuberculosis, end stage renal disease, breast or cervical cancer, or other condition requiring specialized health services, hospice, institutional or community based care provided under a program
         */
        HEALTH, 
        /**
         * A person becomes eligible for a program based on more than one criterion.

                        
                           Examples:  In the U.S., a child whose familiy income meets Medicaid financial thresholds and whose age is less than 18 is eligible for the Early and Periodic Screening, Diagnostic, and Treatment program (EPSDT).  A person whose family income meets Medicaid financial thresholds and whose age is 65 years or older is eligible for Medicaid and Medicare, and are referred to as dual eligibles.
         */
        MULTI, 
        /**
         * A person becomes a claimant under a property and casualty insurance policy because of a related health condition or injury resulting from a circumstance covered under the terms of the policy. 

                        
                           Example:  A person is a claimant under a homeowners insurance policy because of an injury sustained on the policyholderaTMs premises.
         */
        PNC, 
        /**
         * A person becomes eligible for a program based on statutory criteria.

                        
                           Examples:  A person is a member of an indigenous group, a veteran of military service, or  in the U.S., a recipient of adoption assistance and foster care under Title IV-E of the Social Security.
         */
        STATUTORY, 
        /**
         * A person becomes a claimant under a motor vehicle accident insurance because of a motor vehicle accident related health condition or injury.
         */
        VEHIC, 
        /**
         * A person becomes eligible for insurance or a program because of a work related health condition or injury. 

                        
                           Example:  A person is a claimant under the U.S. Black Lung Program.
         */
        WORK, 
        /**
         * Description:The rationale or purpose for an act relating to information management, such as archiving information for the purpose of complying with an enterprise data retention policy.
         */
        _ACTINFORMATIONMANAGEMENTREASON, 
        /**
         * Description:The rationale or purpose for an act relating to health information management, such as archiving information for the purpose of complying with an organization policy or jurisdictional law relating to  data retention.
         */
        _ACTHEALTHINFORMATIONMANAGEMENTREASON, 
        /**
         * To perform one or more operations on information to which the patient has not consented as deemed necessary by authorized entities for providing care in the best interest of the patient; providing immediately needed health care for an emergent condition;  or for protecting public or third party safety.

                        
                           Usage Notes: Used to convey the reason that a provider or other entity may or has accessed personal healthcare information.  Typically, this involves overriding the subject's consent directives.
         */
        _ACTCONSENTINFORMATIONACCESSOVERRIDEREASON, 
        /**
         * To perform one or more operations on information to which the patient has not consented by authorized entities for treating a condition which poses an immediate threat to the patient's health and which requires immediate medical intervention.

                        
                           Usage Notes: The patient is unable to provide consent, but the provider determines they have an urgent healthcare related reason to access the record.
         */
        OVRER, 
        /**
         * To perform one or more operations on information to which the patient declined to consent for providing health care.

                        
                           Usage Notes: The patient, while able to give consent, has not.  However the provider believes it is in the patient's interest to access the record without patient consent.
         */
        OVRPJ, 
        /**
         * To perform one or more operations on information to which the patient has not consented for public safety reasons.

                        
                           Usage Notes: The patient, while able to give consent, has not.  However, the provider believes that access to masked patient information is justified because of concerns related to public safety.
         */
        OVRPS, 
        /**
         * To perform one or more operations on information to which the patient has not consented for third party safety.  

                        
                           Usage Notes: The patient, while able to give consent, has not.  However, the provider believes that access to masked patient information is justified because of concerns related to the health and safety of one or more third parties.
         */
        OVRTPS, 
        /**
         * Reason for performing one or more operations on information, which may be permitted by source system's security policy in accordance with one or more privacy policies and consent directives.

                        
                           Usage Notes: The rationale or purpose for an act relating to the management of personal health information, such as collecting personal health information for research or public health purposes.
         */
        PURPOSEOFUSE, 
        /**
         * To perform one or more operations on information for marketing services and products related to health care.
         */
        HMARKT, 
        /**
         * To perform one or more operations on information used for conducting administrative and contractual activities related to the provision of health care.
         */
        HOPERAT, 
        /**
         * To perform one or more operations on information used for cadaveric organ, eye or tissue donation.
         */
        DONAT, 
        /**
         * To perform one or more operations on information used for fraud detection and prevention processes.
         */
        FRAUD, 
        /**
         * To perform one or more operations on information used within government processes.
         */
        GOV, 
        /**
         * To perform one or more operations on information for conducting activities related to meeting accreditation criteria.
         */
        HACCRED, 
        /**
         * To perform one or more operations on information used for conducting activities required to meet a mandate.
         */
        HCOMPL, 
        /**
         * To perform one or more operations on information used for handling deceased patient matters.
         */
        HDECD, 
        /**
         * To perform one or more operation operations on information used to manage a patient directory.

                        
                           Examples: 
                        

                        
                           facility
                           enterprise
                           payer
                           health information exchange patient directory
         */
        HDIRECT, 
        /**
         * To perform one or more operations on information for conducting activities required by legal proceeding.
         */
        HLEGAL, 
        /**
         * To perform one or more operations on information used for assessing results and comparative effectiveness achieved by health care practices and interventions.
         */
        HOUTCOMS, 
        /**
         * To perform one or more operations on information used for conducting activities to meet program accounting requirements.
         */
        HPRGRP, 
        /**
         * To perform one or more operations on information used for conducting administrative activities to improve health care quality.
         */
        HQUALIMP, 
        /**
         * To perform one or more operations on information to administer the electronic systems used for the delivery of health care.
         */
        HSYSADMIN, 
        /**
         * To perform one or more operations on information to administer health care coverage to an enrollee under a policy or program.
         */
        MEMADMIN, 
        /**
         * To perform one or more operations on information used for operational activities conducted to administer the delivery of health care to a patient.
         */
        PATADMIN, 
        /**
         * To perform one or more operations on information in processes related to ensuring the safety of health care.
         */
        PATSFTY, 
        /**
         * To perform one or more operations on information used for monitoring performance of recommended health care practices and interventions.
         */
        PERFMSR, 
        /**
         * To perform one or more operations on information used within the health records management process.
         */
        RECORDMGT, 
        /**
         * To perform one or more operations on information used in training and education.
         */
        TRAIN, 
        /**
         * To perform one or more operations on information for conducting financial or contractual activities related to payment for provision of health care.
         */
        HPAYMT, 
        /**
         * To perform one or more operations on information for provision of additional clinical evidence in support of a request for coverage or payment for health services.
         */
        CLMATTCH, 
        /**
         * To perform one or more operations on information for conducting prior authorization or predetermination of coverage for services.
         */
        COVAUTH, 
        /**
         * To perform one or more operations on information for conducting activities related to coverage under a program or policy.
         */
        COVERAGE, 
        /**
         * To perform one or more operations on information used for conducting eligibility determination for coverage in a program or policy.  May entail review of financial status or disability assessment.
         */
        ELIGDTRM, 
        /**
         * To perform one or more operations on information used for conducting eligibility verification of coverage in a program or policy.  May entail provider contacting coverage source (e.g., government health program such as workers compensation or health plan) for confirmation of enrollment, eligibility for specific services, and any applicable copays.
         */
        ELIGVER, 
        /**
         * To perform one or more operations on information used for enrolling a covered party in a program or policy.  May entail recording of covered party's and any dependent's demographic information and benefit choices.
         */
        ENROLLM, 
        /**
         * To perform one or more operations on information about the amount remitted for a health care claim.
         */
        REMITADV, 
        /**
         * To perform one or more operations on information for conducting scientific investigations to obtain health care knowledge.
         */
        HRESCH, 
        /**
         * To perform one or more operations on information for conducting scientific investigations in accordance with clinical trial protocols to obtain health care knowledge.
         */
        CLINTRCH, 
        /**
         * To perform one or more operations on information in response to a patient's request.
         */
        PATRQT, 
        /**
         * To perform one or more operations on information in response to a request by a family member authorized by the patient.
         */
        FAMRQT, 
        /**
         * To perform one or more operations on information in response to a request by a person appointed as the patient's legal representative.
         */
        PWATRNY, 
        /**
         * To perform one or more operations on information in response to a request by a person authorized by the patient.
         */
        SUPNWK, 
        /**
         * To perform one or more operations on information for conducting public health activities, such as the reporting of notifiable conditions.
         */
        PUBHLTH, 
        /**
         * To perform one or more operations on information used for provision of immediately needed health care to a population of living subjects located in a disaster zone.
         */
        DISASTER, 
        /**
         * To perform one or more operations on information used to prevent injury or disease to living subjects who may be the target of violence.
         */
        THREAT, 
        /**
         * To perform one or more operations on information for provision of health care.
         */
        TREAT, 
        /**
         * To perform one or more operations on information for provision of health care coordination.
         */
        CAREMGT, 
        /**
         * To perform health care as part of the clinical trial protocol.
         */
        CLINTRL, 
        /**
         * To perform one or more operations on information for provision of immediately needed health care for an emergent condition.
         */
        ETREAT, 
        /**
         * To perform one or more operations on information for provision of health care to a population of living subjects, e.g., needle exchange program.
         */
        POPHLTH, 
        /**
         * Description:The rationale or purpose for an act relating to the management of personal information, such as disclosing personal tax information for the purpose of complying with a court order.
         */
        _ACTINFORMATIONPRIVACYREASON, 
        /**
         * Description:
         */
        MARKT, 
        /**
         * Description:Administrative and contractual processes required to support an activity, product, or service
         */
        OPERAT, 
        /**
         * Definition:To provide information as a result of a subpoena.
         */
        LEGAL, 
        /**
         * Description:Operational activities conducted for the purposes of meeting of criteria defined by an accrediting entity for an activity, product, or service
         */
        ACCRED, 
        /**
         * Description:Operational activities required to meet a mandate related to an activity, product, or service
         */
        COMPL, 
        /**
         * Description:Operational activities conducted to administer information relating to entities involves with an activity, product, or service
         */
        ENADMIN, 
        /**
         * Description:Operational activities conducted for the purposes of assessing the results of an activity, product, or service
         */
        OUTCOMS, 
        /**
         * Description:Operational activities conducted to meet program accounting requirements related to an activity, product, or service
         */
        PRGRPT, 
        /**
         * Description:Operational activities conducted for the purposes of improving the quality of an activity, product, or service
         */
        QUALIMP, 
        /**
         * Description:Operational activities conducted to administer the electronic systems used for an activity, product, or service
         */
        SYSADMN, 
        /**
         * Description:Administrative, financial, and contractual processes related to payment for an activity, product, or service
         */
        PAYMT, 
        /**
         * Description:Investigative activities conducted for the purposes of obtaining knowledge
         */
        RESCH, 
        /**
         * Description:Provision of a service, product, or capability to an individual or organization
         */
        SRVC, 
        /**
         * Description: Types of reasons why a substance is invalid for use.
         */
        _ACTINVALIDREASON, 
        /**
         * Description: Storage conditions caused the substance to be ineffective.
         */
        ADVSTORAGE, 
        /**
         * Description: Cold chain was not maintained for the substance.
         */
        COLDCHNBRK, 
        /**
         * Description: The lot from which the substance was drawn was expired.
         */
        EXPLOT, 
        /**
         * The substance was administered outside of the recommended schedule or practice.
         */
        OUTSIDESCHED, 
        /**
         * Description: The substance was recalled by the manufacturer.
         */
        PRODRECALL, 
        /**
         * Domain specifies the codes used to describe reasons why a Provider is cancelling an Invoice or Invoice Grouping.
         */
        _ACTINVOICECANCELREASON, 
        /**
         * The covered party (patient) specified with the Invoice is not correct.
         */
        INCCOVPTY, 
        /**
         * The billing information, specified in the Invoice Elements, is not correct.  This could include incorrect costing for items included in the Invoice.
         */
        INCINVOICE, 
        /**
         * The policy specified with the Invoice is not correct.  For example, it may belong to another Adjudicator or Covered Party.
         */
        INCPOLICY, 
        /**
         * The provider specified with the Invoice is not correct.
         */
        INCPROV, 
        /**
         * A coded description of the reason for why a patient did not receive a scheduled immunization.

                        (important for public health strategy
         */
        _ACTNOIMMUNIZATIONREASON, 
        /**
         * Definition:Testing has shown that the patient already has immunity to the agent targeted by the immunization.
         */
        IMMUNE, 
        /**
         * Definition:The patient currently has a medical condition for which the vaccine is contraindicated or for which precaution is warranted.
         */
        MEDPREC, 
        /**
         * Definition:There was no supply of the product on hand to perform the service.
         */
        OSTOCK, 
        /**
         * Definition:The patient or their guardian objects to receiving the vaccine.
         */
        PATOBJ, 
        /**
         * Definition:The patient or their guardian objects to receiving the vaccine because of philosophical beliefs.
         */
        PHILISOP, 
        /**
         * Definition:The patient or their guardian objects to receiving the vaccine on religious grounds.
         */
        RELIG, 
        /**
         * Definition:The intended vaccine has expired or is otherwise believed to no longer be effective.

                        
                           Example:Due to temperature exposure.
         */
        VACEFF, 
        /**
         * Definition:The patient or their guardian objects to receiving the vaccine because of concerns over its safety.
         */
        VACSAF, 
        /**
         * Indicates why a fulfiller refused to fulfill a supply order, and considered it important to notify other providers of their decision.  E.g. "Suspect fraud", "Possible abuse", "Contraindicated".

                        (used when capturing 'refusal to fill' annotations)
         */
        _ACTSUPPLYFULFILLMENTREFUSALREASON, 
        /**
         * Definition:The order has been stopped by the prescriber but this fact has not necessarily captured electronically.

                        
                           Example:A verbal stop, a fax, etc.
         */
        FRR01, 
        /**
         * Definition:Order has not been fulfilled within a reasonable amount of time, and may not be current.
         */
        FRR02, 
        /**
         * Definition:Data needed to safely act on the order which was expected to become available independent of the order is not yet available

                        
                           Example:Lab results, diagnostic imaging, etc.
         */
        FRR03, 
        /**
         * Definition:Product not available or manufactured. Cannot supply.
         */
        FRR04, 
        /**
         * Definition:The dispenser has ethical, religious or moral objections to fulfilling the order/dispensing the product.
         */
        FRR05, 
        /**
         * Definition:Fulfiller not able to provide appropriate care associated with fulfilling the order.

                        
                           Example:Therapy requires ongoing monitoring by fulfiller and fulfiller will be ending practice, leaving town, unable to schedule necessary time, etc.
         */
        FRR06, 
        /**
         * Definition:Specifies the reason that an event occurred in a clinical research study.
         */
        _CLINICALRESEARCHEVENTREASON, 
        /**
         * Definition:The event occurred so that a test or observation performed at a prior event could be performed again due to conditions set forth in the protocol.
         */
        RET, 
        /**
         * Definition:The event occurred due to it being scheduled in the research protocol.
         */
        SCH, 
        /**
         * Definition:The event occurred in order to terminate the subject's participation in the study.
         */
        TRM, 
        /**
         * Definition:The event that occurred was initiated by a study participant (e.g. the subject or the investigator), and did not occur for protocol reasons.
         */
        UNS, 
        /**
         * Definition:SSpecifies the reason that a test was performed or observation collected in a clinical research study.

                        
                           Note:This set of codes are not strictly reasons, but are used in the currently Normative standard.  Future revisions of the specification will model these as ActRelationships and thes codes may subsequently be retired.  Thus, these codes should not be used for new specifications.
         */
        _CLINICALRESEARCHOBSERVATIONREASON, 
        /**
         * Definition:The observation or test was neither defined or scheduled in the study protocol.
         */
        NPT, 
        /**
         * Definition:The observation or test occurred due to it being defined in the research protocol, and during an activity or event that was scheduled in the protocol.
         */
        PPT, 
        /**
         * :The observation or test occurred as defined in the research protocol, but at a point in time not specified in the study protocol.
         */
        UPT, 
        /**
         * Description:Indicates why the prescription should be suspended.
         */
        _COMBINEDPHARMACYORDERSUSPENDREASONCODE, 
        /**
         * Description:This therapy has been ordered as a backup to a preferred therapy.  This order will be released when and if the preferred therapy is unsuccessful.
         */
        ALTCHOICE, 
        /**
         * Description:Clarification is required before the order can be acted upon.
         */
        CLARIF, 
        /**
         * Description:The current level of the medication in the patient's system is too high.  The medication is suspended to allow the level to subside to a safer level.
         */
        DRUGHIGH, 
        /**
         * Description:The patient has been admitted to a care facility and their community medications are suspended until hospital discharge.
         */
        HOSPADM, 
        /**
         * Description:The therapy would interfere with a planned lab test and the therapy is being withdrawn until the test is completed.
         */
        LABINT, 
        /**
         * Description:Patient not available for a period of time due to a scheduled therapy, leave of absence or other reason.
         */
        NONAVAIL, 
        /**
         * Description:The patient is pregnant or breast feeding.  The therapy will be resumed when the pregnancy is complete and the patient is no longer breastfeeding.
         */
        PREG, 
        /**
         * Description:The patient is believed to be allergic to a substance that is part of the therapy and the therapy is being temporarily withdrawn to confirm.
         */
        SALG, 
        /**
         * Description:The drug interacts with a short-term treatment that is more urgently required.  This order will be resumed when the short-term treatment is complete.
         */
        SDDI, 
        /**
         * Description:Another short-term co-occurring therapy fulfills the same purpose as this therapy.  This therapy will be resumed when the co-occuring therapy is complete.
         */
        SDUPTHER, 
        /**
         * Description:The patient is believed to have an intolerance to a substance that is part of the therapy and the therapy is being temporarily withdrawn to confirm.
         */
        SINTOL, 
        /**
         * Description:The drug is contraindicated for patients receiving surgery and the patient is scheduled to be admitted for surgery in the near future.  The drug will be resumed when the patient has sufficiently recovered from the surgery.
         */
        SURG, 
        /**
         * Description:The patient was previously receiving a medication contraindicated with the current medication.  The current medication will remain on hold until the prior medication has been cleansed from their system.
         */
        WASHOUT, 
        /**
         * Description:Identifies reasons for nullifying (retracting) a particular control act.
         */
        _CONTROLACTNULLIFICATIONREASONCODE, 
        /**
         * Description:The decision on which the recorded information was based was changed before the decision had an effect.

                        
                           Example:Aborted prescription before patient left office, released prescription before suspend took effect.
         */
        ALTD, 
        /**
         * Description:The information was recorded incorrectly or was recorded in the wrong record.
         */
        EIE, 
        /**
         * Description: There is no match for the record in the database.
         */
        NORECMTCH, 
        /**
         * Description: Reasons to refuse a transaction to be undone.
         */
        _CONTROLACTNULLIFICATIONREFUSALREASONTYPE, 
        /**
         * The record is already in the requested state.
         */
        INRQSTATE, 
        /**
         * Description: There is no match.
         */
        NOMATCH, 
        /**
         * Description: There is no match for the product in the master file repository.
         */
        NOPRODMTCH, 
        /**
         * Description: There is no match for the service in the master file repository.
         */
        NOSERMTCH, 
        /**
         * Description: There is no match for the record and version.
         */
        NOVERMTCH, 
        /**
         * Description: There is no permission.
         */
        NOPERM, 
        /**
         * Definition:The user does not have permission
         */
        NOUSERPERM, 
        /**
         * Description: The agent does not have permission.
         */
        NOAGNTPERM, 
        /**
         * Description: The record and version requested to update is not the current version.
         */
        WRNGVER, 
        /**
         * Identifies why a specific query, request, or other trigger event occurred.
         */
        _CONTROLACTREASON, 
        /**
         * Description:Indicates the reason the medication order should be aborted.
         */
        _MEDICATIONORDERABORTREASONCODE, 
        /**
         * Description:The medication is no longer being manufactured or is otherwise no longer available.
         */
        DISCONT, 
        /**
         * Description:The therapy has been found to not have the desired therapeutic benefit on the patient.
         */
        INEFFECT, 
        /**
         * Description:Monitoring the patient while taking the medication, the decision has been made that the therapy is no longer appropriate.
         */
        MONIT, 
        /**
         * Description:The underlying condition has been resolved or has evolved such that a different treatment is no longer needed.
         */
        NOREQ, 
        /**
         * Description:The product does not have (or no longer has) coverage under the patientaTMs insurance policy.
         */
        NOTCOVER, 
        /**
         * Description:The patient refused to take the product.
         */
        PREFUS, 
        /**
         * Description:The manufacturer or other agency has requested that stocks of a medication be removed from circulation.
         */
        RECALL, 
        /**
         * Description:Item in current order is no longer in use as requested and a new one has/will be created to replace it.
         */
        REPLACE, 
        /**
         * Description:The medication is being re-prescribed at a different dosage.
         */
        DOSECHG, 
        /**
         * Description:Current order was issued with incorrect data and a new order has/will be created to replace it.
         */
        REPLACEFIX, 
        /**
         * Description:<The patient is not (or is no longer) able to use the medication in a manner prescribed.

                        
                           Example:CanaTMt swallow.
         */
        UNABLE, 
        /**
         * Definition:A collection of concepts that indicate why the prescription should be released from suspended state.
         */
        _MEDICATIONORDERRELEASEREASONCODE, 
        /**
         * Definition:The original reason for suspending the medication has ended.
         */
        HOLDDONE, 
        /**
         * Definition:
         */
        HOLDINAP, 
        /**
         * Types of reason why a prescription is being changed.
         */
        _MODIFYPRESCRIPTIONREASONTYPE, 
        /**
         * Order was created with incorrect data and is changed to reflect the intended accuracy of the order.
         */
        ADMINERROR, 
        /**
         * Order is changed based on a clinical reason.
         */
        CLINMOD, 
        /**
         * Definition:Identifies why the dispense event was not completed.
         */
        _PHARMACYSUPPLYEVENTABORTREASON, 
        /**
         * Definition:Contraindication identified
         */
        CONTRA, 
        /**
         * Definition:Order to be fulfilled was aborted
         */
        FOABORT, 
        /**
         * Definition:Order to be fulfilled was suspended
         */
        FOSUSP, 
        /**
         * Definition:Patient did not come to get medication
         */
        NOPICK, 
        /**
         * Definition:Patient changed their mind regarding obtaining medication
         */
        PATDEC, 
        /**
         * Definition:Patient requested a revised quantity of medication
         */
        QUANTCHG, 
        /**
         * Definition:A collection of concepts that indicates the reason for a "bulk supply" of medication.
         */
        _PHARMACYSUPPLYEVENTSTOCKREASONCODE, 
        /**
         * Definition:The bulk supply is issued to replenish a ward for local dispensing.  (Includes both mobile and fixed-location ward stocks.)
         */
        FLRSTCK, 
        /**
         * Definition:The bulk supply will be administered within a long term care facility.
         */
        LTC, 
        /**
         * Definition:The bulk supply is intended for general clinician office use.
         */
        OFFICE, 
        /**
         * Definition:The bulk supply is being transferred to another dispensing facility to.

                        
                           Example:Alleviate a temporary shortage.
         */
        PHARM, 
        /**
         * Definition:The bulk supply is intended for dispensing according to a specific program.

                        
                           Example:Mass immunization.
         */
        PROG, 
        /**
         * Definition:A collection of concepts that identifies why a renewal prescription has been refused.
         */
        _PHARMACYSUPPLYREQUESTRENEWALREFUSALREASONCODE, 
        /**
         * Definition:Patient has already been given a new (renewal) prescription.
         */
        ALREADYRX, 
        /**
         * Definition:Request for further authorization must be done through patient's family physician.
         */
        FAMPHYS, 
        /**
         * Definition:Therapy has been changed and new prescription issued
         */
        MODIFY, 
        /**
         * Definition:Patient must see prescriber prior to further fills.
         */
        NEEDAPMT, 
        /**
         * Definition:Original prescriber is no longer available to prescribe and no other prescriber has taken responsibility for the patient.
         */
        NOTAVAIL, 
        /**
         * Definition:Patient no longer or has never been under this prescribers care.
         */
        NOTPAT, 
        /**
         * Definition:This medication is on hold.
         */
        ONHOLD, 
        /**
         * Description:This product is not available or manufactured.
         */
        PRNA, 
        /**
         * Renewing or original prescriber informed patient to stop using the medication.
         */
        STOPMED, 
        /**
         * Definition:The patient should have medication remaining.
         */
        TOOEARLY, 
        /**
         * Definition:A collection of concepts that indicates why the prescription should no longer be allowed to be dispensed (but can still administer what is already being dispensed).
         */
        _SUPPLYORDERABORTREASONCODE, 
        /**
         * Definition:The patient's medical condition has nearly abated.
         */
        IMPROV, 
        /**
         * Description:The patient has an intolerance to the medication.
         */
        INTOL, 
        /**
         * Definition:The current medication will be replaced by a new strength of the same medication.
         */
        NEWSTR, 
        /**
         * Definition:A new therapy will be commenced when current supply exhausted.
         */
        NEWTHER, 
        /**
         * Description:Identifies why a change is being made to a  record.
         */
        _GENERICUPDATEREASONCODE, 
        /**
         * Description:Information has changed since the record was created.
         */
        CHGDATA, 
        /**
         * Description:Previously recorded information was erroneous and is being corrected.
         */
        FIXDATA, 
        /**
         * Information is combined into the record.
         */
        MDATA, 
        /**
         * Description:New information has become available to supplement the record.
         */
        NEWDATA, 
        /**
         * Information is separated from the record.
         */
        UMDATA, 
        /**
         * Definition:A collection of concepts identifying why the patient's profile is being queried.
         */
        _PATIENTPROFILEQUERYREASONCODE, 
        /**
         * Definition: To evaluate for service authorization, payment, reporting, or performance/outcome measures.
         */
        ADMREV, 
        /**
         * Definition:To obtain records as part of patient care.
         */
        PATCAR, 
        /**
         * Definition:Patient requests information from their profile.
         */
        PATREQ, 
        /**
         * Definition:To evaluate the provider's current practice for professional-improvement reasons.
         */
        PRCREV, 
        /**
         * Description:Review for the purpose of regulatory compliance.
         */
        REGUL, 
        /**
         * Definition:To provide research data, as authorized by the patient.
         */
        RSRCH, 
        /**
         * Description:To validate the patient's record.

                        
                           Example:Merging or unmerging records.
         */
        VALIDATION, 
        /**
         * Definition:Indicates why the request to transfer a prescription from one dispensing facility to another has been refused.
         */
        _PHARMACYSUPPLYREQUESTFULFILLERREVISIONREFUSALREASONCODE, 
        /**
         * Definition:The prescription may not be reassigned from the original pharmacy.
         */
        LOCKED, 
        /**
         * Definition:The target facility does not recognize the dispensing facility.
         */
        UNKWNTARGET, 
        /**
         * Description: Identifies why a request to add (or activate) a record is being refused.  Examples include the receiving system not able to match the identifier and find that record in the receiving system, having no permission, or a detected issue exists which precludes the requested action.
         */
        _REFUSALREASONCODE, 
        /**
         * Reasons for cancelling or rescheduling an Appointment
         */
        _SCHEDULINGACTREASON, 
        /**
         * The time slots previously allocated are now blocked and no longer available for booking Appointments
         */
        BLK, 
        /**
         * The Patient is deceased
         */
        DEC, 
        /**
         * Patient unable to pay and not covered by insurance
         */
        FIN, 
        /**
         * The medical condition of the Patient has changed
         */
        MED, 
        /**
         * The Physician is in a meeting.  For example, he/she may request administrative time to talk to family after appointment
         */
        MTG, 
        /**
         * The Physician requested the action
         */
        PHY, 
        /**
         * Indicates why the act revision (status update) is being refused.
         */
        _STATUSREVISIONREFUSALREASONCODE, 
        /**
         * Ordered quantity has already been completely fulfilled.
         */
        FILLED, 
        /**
         * Definition:Indicates why the requested authorization to prescribe or dispense a medication has been refused.
         */
        _SUBSTANCEADMINISTRATIONPERMISSIONREFUSALREASONCODE, 
        /**
         * Definition:Patient not eligible for drug
         */
        PATINELIG, 
        /**
         * Definition:Patient does not meet required protocol
         */
        PROTUNMET, 
        /**
         * Definition:Provider is not authorized to prescribe or dispense
         */
        PROVUNAUTH, 
        /**
         * Reasons why substitution of a substance administration request is not permitted.
         */
        _SUBSTANCEADMINSUBSTITUTIONNOTALLOWEDREASON, 
        /**
         * Definition: Patient has had a prior allergic intolerance response to alternate product or one of its components.
         */
        ALGINT, 
        /**
         * Definition: Patient has compliance issues with medication such as differing appearance, flavor, size, shape or consistency.
         */
        COMPCON, 
        /**
         * The prescribed product has specific clinical release or other therapeutic characteristics not shared by other substitutable medications.
         */
        THERCHAR, 
        /**
         * Definition: The specific manufactured drug is part of a clinical trial.
         */
        TRIAL, 
        /**
         * SubstanceAdminSubstitutionReason
         */
        _SUBSTANCEADMINSUBSTITUTIONREASON, 
        /**
         * Indicates that the decision to substitute or to not substitute was driven by a desire to maintain consistency with a pre-existing therapy.  I.e. The performer provided the same item/service as had been previously provided rather than providing exactly what was ordered, or rather than substituting with a lower-cost equivalent.
         */
        CT, 
        /**
         * Indicates that the decision to substitute or to not substitute was driven by a policy expressed within the formulary.
         */
        FP, 
        /**
         * In the case of 'substitution', indicates that the substitution occurred because the ordered item was not in stock.  In the case of 'no substitution', indicates that a cheaper equivalent was not substituted because it was not in stock.
         */
        OS, 
        /**
         * Indicates that the decision to substitute or to not substitute was driven by a jurisdictional regulatory requirement mandating or prohibiting substitution.
         */
        RR, 
        /**
         * The explanation for why a patient is moved from one location to another within the organization
         */
        _TRANSFERACTREASON, 
        /**
         * Moved to an error in placing the patient in the original location.
         */
        ER, 
        /**
         * Moved at the request of the patient.
         */
        RQ, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActReason fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ActAccommodationReason".equals(codeString))
          return _ACTACCOMMODATIONREASON;
        if ("ACCREQNA".equals(codeString))
          return ACCREQNA;
        if ("FLRCNV".equals(codeString))
          return FLRCNV;
        if ("MEDNEC".equals(codeString))
          return MEDNEC;
        if ("PAT".equals(codeString))
          return PAT;
        if ("_ActCoverageReason".equals(codeString))
          return _ACTCOVERAGEREASON;
        if ("_EligibilityActReasonCode".equals(codeString))
          return _ELIGIBILITYACTREASONCODE;
        if ("_ActIneligibilityReason".equals(codeString))
          return _ACTINELIGIBILITYREASON;
        if ("COVSUS".equals(codeString))
          return COVSUS;
        if ("DECSD".equals(codeString))
          return DECSD;
        if ("REGERR".equals(codeString))
          return REGERR;
        if ("_CoverageEligibilityReason".equals(codeString))
          return _COVERAGEELIGIBILITYREASON;
        if ("AGE".equals(codeString))
          return AGE;
        if ("CRIME".equals(codeString))
          return CRIME;
        if ("DIS".equals(codeString))
          return DIS;
        if ("EMPLOY".equals(codeString))
          return EMPLOY;
        if ("FINAN".equals(codeString))
          return FINAN;
        if ("HEALTH".equals(codeString))
          return HEALTH;
        if ("MULTI".equals(codeString))
          return MULTI;
        if ("PNC".equals(codeString))
          return PNC;
        if ("STATUTORY".equals(codeString))
          return STATUTORY;
        if ("VEHIC".equals(codeString))
          return VEHIC;
        if ("WORK".equals(codeString))
          return WORK;
        if ("_ActInformationManagementReason".equals(codeString))
          return _ACTINFORMATIONMANAGEMENTREASON;
        if ("_ActHealthInformationManagementReason".equals(codeString))
          return _ACTHEALTHINFORMATIONMANAGEMENTREASON;
        if ("_ActConsentInformationAccessOverrideReason".equals(codeString))
          return _ACTCONSENTINFORMATIONACCESSOVERRIDEREASON;
        if ("OVRER".equals(codeString))
          return OVRER;
        if ("OVRPJ".equals(codeString))
          return OVRPJ;
        if ("OVRPS".equals(codeString))
          return OVRPS;
        if ("OVRTPS".equals(codeString))
          return OVRTPS;
        if ("PurposeOfUse".equals(codeString))
          return PURPOSEOFUSE;
        if ("HMARKT".equals(codeString))
          return HMARKT;
        if ("HOPERAT".equals(codeString))
          return HOPERAT;
        if ("DONAT".equals(codeString))
          return DONAT;
        if ("FRAUD".equals(codeString))
          return FRAUD;
        if ("GOV".equals(codeString))
          return GOV;
        if ("HACCRED".equals(codeString))
          return HACCRED;
        if ("HCOMPL".equals(codeString))
          return HCOMPL;
        if ("HDECD".equals(codeString))
          return HDECD;
        if ("HDIRECT".equals(codeString))
          return HDIRECT;
        if ("HLEGAL".equals(codeString))
          return HLEGAL;
        if ("HOUTCOMS".equals(codeString))
          return HOUTCOMS;
        if ("HPRGRP".equals(codeString))
          return HPRGRP;
        if ("HQUALIMP".equals(codeString))
          return HQUALIMP;
        if ("HSYSADMIN".equals(codeString))
          return HSYSADMIN;
        if ("MEMADMIN".equals(codeString))
          return MEMADMIN;
        if ("PATADMIN".equals(codeString))
          return PATADMIN;
        if ("PATSFTY".equals(codeString))
          return PATSFTY;
        if ("PERFMSR".equals(codeString))
          return PERFMSR;
        if ("RECORDMGT".equals(codeString))
          return RECORDMGT;
        if ("TRAIN".equals(codeString))
          return TRAIN;
        if ("HPAYMT".equals(codeString))
          return HPAYMT;
        if ("CLMATTCH".equals(codeString))
          return CLMATTCH;
        if ("COVAUTH".equals(codeString))
          return COVAUTH;
        if ("COVERAGE".equals(codeString))
          return COVERAGE;
        if ("ELIGDTRM".equals(codeString))
          return ELIGDTRM;
        if ("ELIGVER".equals(codeString))
          return ELIGVER;
        if ("ENROLLM".equals(codeString))
          return ENROLLM;
        if ("REMITADV".equals(codeString))
          return REMITADV;
        if ("HRESCH".equals(codeString))
          return HRESCH;
        if ("CLINTRCH".equals(codeString))
          return CLINTRCH;
        if ("PATRQT".equals(codeString))
          return PATRQT;
        if ("FAMRQT".equals(codeString))
          return FAMRQT;
        if ("PWATRNY".equals(codeString))
          return PWATRNY;
        if ("SUPNWK".equals(codeString))
          return SUPNWK;
        if ("PUBHLTH".equals(codeString))
          return PUBHLTH;
        if ("DISASTER".equals(codeString))
          return DISASTER;
        if ("THREAT".equals(codeString))
          return THREAT;
        if ("TREAT".equals(codeString))
          return TREAT;
        if ("CAREMGT".equals(codeString))
          return CAREMGT;
        if ("CLINTRL".equals(codeString))
          return CLINTRL;
        if ("ETREAT".equals(codeString))
          return ETREAT;
        if ("POPHLTH".equals(codeString))
          return POPHLTH;
        if ("_ActInformationPrivacyReason".equals(codeString))
          return _ACTINFORMATIONPRIVACYREASON;
        if ("MARKT".equals(codeString))
          return MARKT;
        if ("OPERAT".equals(codeString))
          return OPERAT;
        if ("LEGAL".equals(codeString))
          return LEGAL;
        if ("ACCRED".equals(codeString))
          return ACCRED;
        if ("COMPL".equals(codeString))
          return COMPL;
        if ("ENADMIN".equals(codeString))
          return ENADMIN;
        if ("OUTCOMS".equals(codeString))
          return OUTCOMS;
        if ("PRGRPT".equals(codeString))
          return PRGRPT;
        if ("QUALIMP".equals(codeString))
          return QUALIMP;
        if ("SYSADMN".equals(codeString))
          return SYSADMN;
        if ("PAYMT".equals(codeString))
          return PAYMT;
        if ("RESCH".equals(codeString))
          return RESCH;
        if ("SRVC".equals(codeString))
          return SRVC;
        if ("_ActInvalidReason".equals(codeString))
          return _ACTINVALIDREASON;
        if ("ADVSTORAGE".equals(codeString))
          return ADVSTORAGE;
        if ("COLDCHNBRK".equals(codeString))
          return COLDCHNBRK;
        if ("EXPLOT".equals(codeString))
          return EXPLOT;
        if ("OUTSIDESCHED".equals(codeString))
          return OUTSIDESCHED;
        if ("PRODRECALL".equals(codeString))
          return PRODRECALL;
        if ("_ActInvoiceCancelReason".equals(codeString))
          return _ACTINVOICECANCELREASON;
        if ("INCCOVPTY".equals(codeString))
          return INCCOVPTY;
        if ("INCINVOICE".equals(codeString))
          return INCINVOICE;
        if ("INCPOLICY".equals(codeString))
          return INCPOLICY;
        if ("INCPROV".equals(codeString))
          return INCPROV;
        if ("_ActNoImmunizationReason".equals(codeString))
          return _ACTNOIMMUNIZATIONREASON;
        if ("IMMUNE".equals(codeString))
          return IMMUNE;
        if ("MEDPREC".equals(codeString))
          return MEDPREC;
        if ("OSTOCK".equals(codeString))
          return OSTOCK;
        if ("PATOBJ".equals(codeString))
          return PATOBJ;
        if ("PHILISOP".equals(codeString))
          return PHILISOP;
        if ("RELIG".equals(codeString))
          return RELIG;
        if ("VACEFF".equals(codeString))
          return VACEFF;
        if ("VACSAF".equals(codeString))
          return VACSAF;
        if ("_ActSupplyFulfillmentRefusalReason".equals(codeString))
          return _ACTSUPPLYFULFILLMENTREFUSALREASON;
        if ("FRR01".equals(codeString))
          return FRR01;
        if ("FRR02".equals(codeString))
          return FRR02;
        if ("FRR03".equals(codeString))
          return FRR03;
        if ("FRR04".equals(codeString))
          return FRR04;
        if ("FRR05".equals(codeString))
          return FRR05;
        if ("FRR06".equals(codeString))
          return FRR06;
        if ("_ClinicalResearchEventReason".equals(codeString))
          return _CLINICALRESEARCHEVENTREASON;
        if ("RET".equals(codeString))
          return RET;
        if ("SCH".equals(codeString))
          return SCH;
        if ("TRM".equals(codeString))
          return TRM;
        if ("UNS".equals(codeString))
          return UNS;
        if ("_ClinicalResearchObservationReason".equals(codeString))
          return _CLINICALRESEARCHOBSERVATIONREASON;
        if ("NPT".equals(codeString))
          return NPT;
        if ("PPT".equals(codeString))
          return PPT;
        if ("UPT".equals(codeString))
          return UPT;
        if ("_CombinedPharmacyOrderSuspendReasonCode".equals(codeString))
          return _COMBINEDPHARMACYORDERSUSPENDREASONCODE;
        if ("ALTCHOICE".equals(codeString))
          return ALTCHOICE;
        if ("CLARIF".equals(codeString))
          return CLARIF;
        if ("DRUGHIGH".equals(codeString))
          return DRUGHIGH;
        if ("HOSPADM".equals(codeString))
          return HOSPADM;
        if ("LABINT".equals(codeString))
          return LABINT;
        if ("NON-AVAIL".equals(codeString))
          return NONAVAIL;
        if ("PREG".equals(codeString))
          return PREG;
        if ("SALG".equals(codeString))
          return SALG;
        if ("SDDI".equals(codeString))
          return SDDI;
        if ("SDUPTHER".equals(codeString))
          return SDUPTHER;
        if ("SINTOL".equals(codeString))
          return SINTOL;
        if ("SURG".equals(codeString))
          return SURG;
        if ("WASHOUT".equals(codeString))
          return WASHOUT;
        if ("_ControlActNullificationReasonCode".equals(codeString))
          return _CONTROLACTNULLIFICATIONREASONCODE;
        if ("ALTD".equals(codeString))
          return ALTD;
        if ("EIE".equals(codeString))
          return EIE;
        if ("NORECMTCH".equals(codeString))
          return NORECMTCH;
        if ("_ControlActNullificationRefusalReasonType".equals(codeString))
          return _CONTROLACTNULLIFICATIONREFUSALREASONTYPE;
        if ("INRQSTATE".equals(codeString))
          return INRQSTATE;
        if ("NOMATCH".equals(codeString))
          return NOMATCH;
        if ("NOPRODMTCH".equals(codeString))
          return NOPRODMTCH;
        if ("NOSERMTCH".equals(codeString))
          return NOSERMTCH;
        if ("NOVERMTCH".equals(codeString))
          return NOVERMTCH;
        if ("NOPERM".equals(codeString))
          return NOPERM;
        if ("NOUSERPERM".equals(codeString))
          return NOUSERPERM;
        if ("NOAGNTPERM".equals(codeString))
          return NOAGNTPERM;
        if ("WRNGVER".equals(codeString))
          return WRNGVER;
        if ("_ControlActReason".equals(codeString))
          return _CONTROLACTREASON;
        if ("_MedicationOrderAbortReasonCode".equals(codeString))
          return _MEDICATIONORDERABORTREASONCODE;
        if ("DISCONT".equals(codeString))
          return DISCONT;
        if ("INEFFECT".equals(codeString))
          return INEFFECT;
        if ("MONIT".equals(codeString))
          return MONIT;
        if ("NOREQ".equals(codeString))
          return NOREQ;
        if ("NOTCOVER".equals(codeString))
          return NOTCOVER;
        if ("PREFUS".equals(codeString))
          return PREFUS;
        if ("RECALL".equals(codeString))
          return RECALL;
        if ("REPLACE".equals(codeString))
          return REPLACE;
        if ("DOSECHG".equals(codeString))
          return DOSECHG;
        if ("REPLACEFIX".equals(codeString))
          return REPLACEFIX;
        if ("UNABLE".equals(codeString))
          return UNABLE;
        if ("_MedicationOrderReleaseReasonCode".equals(codeString))
          return _MEDICATIONORDERRELEASEREASONCODE;
        if ("HOLDDONE".equals(codeString))
          return HOLDDONE;
        if ("HOLDINAP".equals(codeString))
          return HOLDINAP;
        if ("_ModifyPrescriptionReasonType".equals(codeString))
          return _MODIFYPRESCRIPTIONREASONTYPE;
        if ("ADMINERROR".equals(codeString))
          return ADMINERROR;
        if ("CLINMOD".equals(codeString))
          return CLINMOD;
        if ("_PharmacySupplyEventAbortReason".equals(codeString))
          return _PHARMACYSUPPLYEVENTABORTREASON;
        if ("CONTRA".equals(codeString))
          return CONTRA;
        if ("FOABORT".equals(codeString))
          return FOABORT;
        if ("FOSUSP".equals(codeString))
          return FOSUSP;
        if ("NOPICK".equals(codeString))
          return NOPICK;
        if ("PATDEC".equals(codeString))
          return PATDEC;
        if ("QUANTCHG".equals(codeString))
          return QUANTCHG;
        if ("_PharmacySupplyEventStockReasonCode".equals(codeString))
          return _PHARMACYSUPPLYEVENTSTOCKREASONCODE;
        if ("FLRSTCK".equals(codeString))
          return FLRSTCK;
        if ("LTC".equals(codeString))
          return LTC;
        if ("OFFICE".equals(codeString))
          return OFFICE;
        if ("PHARM".equals(codeString))
          return PHARM;
        if ("PROG".equals(codeString))
          return PROG;
        if ("_PharmacySupplyRequestRenewalRefusalReasonCode".equals(codeString))
          return _PHARMACYSUPPLYREQUESTRENEWALREFUSALREASONCODE;
        if ("ALREADYRX".equals(codeString))
          return ALREADYRX;
        if ("FAMPHYS".equals(codeString))
          return FAMPHYS;
        if ("MODIFY".equals(codeString))
          return MODIFY;
        if ("NEEDAPMT".equals(codeString))
          return NEEDAPMT;
        if ("NOTAVAIL".equals(codeString))
          return NOTAVAIL;
        if ("NOTPAT".equals(codeString))
          return NOTPAT;
        if ("ONHOLD".equals(codeString))
          return ONHOLD;
        if ("PRNA".equals(codeString))
          return PRNA;
        if ("STOPMED".equals(codeString))
          return STOPMED;
        if ("TOOEARLY".equals(codeString))
          return TOOEARLY;
        if ("_SupplyOrderAbortReasonCode".equals(codeString))
          return _SUPPLYORDERABORTREASONCODE;
        if ("IMPROV".equals(codeString))
          return IMPROV;
        if ("INTOL".equals(codeString))
          return INTOL;
        if ("NEWSTR".equals(codeString))
          return NEWSTR;
        if ("NEWTHER".equals(codeString))
          return NEWTHER;
        if ("_GenericUpdateReasonCode".equals(codeString))
          return _GENERICUPDATEREASONCODE;
        if ("CHGDATA".equals(codeString))
          return CHGDATA;
        if ("FIXDATA".equals(codeString))
          return FIXDATA;
        if ("MDATA".equals(codeString))
          return MDATA;
        if ("NEWDATA".equals(codeString))
          return NEWDATA;
        if ("UMDATA".equals(codeString))
          return UMDATA;
        if ("_PatientProfileQueryReasonCode".equals(codeString))
          return _PATIENTPROFILEQUERYREASONCODE;
        if ("ADMREV".equals(codeString))
          return ADMREV;
        if ("PATCAR".equals(codeString))
          return PATCAR;
        if ("PATREQ".equals(codeString))
          return PATREQ;
        if ("PRCREV".equals(codeString))
          return PRCREV;
        if ("REGUL".equals(codeString))
          return REGUL;
        if ("RSRCH".equals(codeString))
          return RSRCH;
        if ("VALIDATION".equals(codeString))
          return VALIDATION;
        if ("_PharmacySupplyRequestFulfillerRevisionRefusalReasonCode".equals(codeString))
          return _PHARMACYSUPPLYREQUESTFULFILLERREVISIONREFUSALREASONCODE;
        if ("LOCKED".equals(codeString))
          return LOCKED;
        if ("UNKWNTARGET".equals(codeString))
          return UNKWNTARGET;
        if ("_RefusalReasonCode".equals(codeString))
          return _REFUSALREASONCODE;
        if ("_SchedulingActReason".equals(codeString))
          return _SCHEDULINGACTREASON;
        if ("BLK".equals(codeString))
          return BLK;
        if ("DEC".equals(codeString))
          return DEC;
        if ("FIN".equals(codeString))
          return FIN;
        if ("MED".equals(codeString))
          return MED;
        if ("MTG".equals(codeString))
          return MTG;
        if ("PHY".equals(codeString))
          return PHY;
        if ("_StatusRevisionRefusalReasonCode".equals(codeString))
          return _STATUSREVISIONREFUSALREASONCODE;
        if ("FILLED".equals(codeString))
          return FILLED;
        if ("_SubstanceAdministrationPermissionRefusalReasonCode".equals(codeString))
          return _SUBSTANCEADMINISTRATIONPERMISSIONREFUSALREASONCODE;
        if ("PATINELIG".equals(codeString))
          return PATINELIG;
        if ("PROTUNMET".equals(codeString))
          return PROTUNMET;
        if ("PROVUNAUTH".equals(codeString))
          return PROVUNAUTH;
        if ("_SubstanceAdminSubstitutionNotAllowedReason".equals(codeString))
          return _SUBSTANCEADMINSUBSTITUTIONNOTALLOWEDREASON;
        if ("ALGINT".equals(codeString))
          return ALGINT;
        if ("COMPCON".equals(codeString))
          return COMPCON;
        if ("THERCHAR".equals(codeString))
          return THERCHAR;
        if ("TRIAL".equals(codeString))
          return TRIAL;
        if ("_SubstanceAdminSubstitutionReason".equals(codeString))
          return _SUBSTANCEADMINSUBSTITUTIONREASON;
        if ("CT".equals(codeString))
          return CT;
        if ("FP".equals(codeString))
          return FP;
        if ("OS".equals(codeString))
          return OS;
        if ("RR".equals(codeString))
          return RR;
        if ("_TransferActReason".equals(codeString))
          return _TRANSFERACTREASON;
        if ("ER".equals(codeString))
          return ER;
        if ("RQ".equals(codeString))
          return RQ;
        throw new Exception("Unknown V3ActReason code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ACTACCOMMODATIONREASON: return "_ActAccommodationReason";
            case ACCREQNA: return "ACCREQNA";
            case FLRCNV: return "FLRCNV";
            case MEDNEC: return "MEDNEC";
            case PAT: return "PAT";
            case _ACTCOVERAGEREASON: return "_ActCoverageReason";
            case _ELIGIBILITYACTREASONCODE: return "_EligibilityActReasonCode";
            case _ACTINELIGIBILITYREASON: return "_ActIneligibilityReason";
            case COVSUS: return "COVSUS";
            case DECSD: return "DECSD";
            case REGERR: return "REGERR";
            case _COVERAGEELIGIBILITYREASON: return "_CoverageEligibilityReason";
            case AGE: return "AGE";
            case CRIME: return "CRIME";
            case DIS: return "DIS";
            case EMPLOY: return "EMPLOY";
            case FINAN: return "FINAN";
            case HEALTH: return "HEALTH";
            case MULTI: return "MULTI";
            case PNC: return "PNC";
            case STATUTORY: return "STATUTORY";
            case VEHIC: return "VEHIC";
            case WORK: return "WORK";
            case _ACTINFORMATIONMANAGEMENTREASON: return "_ActInformationManagementReason";
            case _ACTHEALTHINFORMATIONMANAGEMENTREASON: return "_ActHealthInformationManagementReason";
            case _ACTCONSENTINFORMATIONACCESSOVERRIDEREASON: return "_ActConsentInformationAccessOverrideReason";
            case OVRER: return "OVRER";
            case OVRPJ: return "OVRPJ";
            case OVRPS: return "OVRPS";
            case OVRTPS: return "OVRTPS";
            case PURPOSEOFUSE: return "PurposeOfUse";
            case HMARKT: return "HMARKT";
            case HOPERAT: return "HOPERAT";
            case DONAT: return "DONAT";
            case FRAUD: return "FRAUD";
            case GOV: return "GOV";
            case HACCRED: return "HACCRED";
            case HCOMPL: return "HCOMPL";
            case HDECD: return "HDECD";
            case HDIRECT: return "HDIRECT";
            case HLEGAL: return "HLEGAL";
            case HOUTCOMS: return "HOUTCOMS";
            case HPRGRP: return "HPRGRP";
            case HQUALIMP: return "HQUALIMP";
            case HSYSADMIN: return "HSYSADMIN";
            case MEMADMIN: return "MEMADMIN";
            case PATADMIN: return "PATADMIN";
            case PATSFTY: return "PATSFTY";
            case PERFMSR: return "PERFMSR";
            case RECORDMGT: return "RECORDMGT";
            case TRAIN: return "TRAIN";
            case HPAYMT: return "HPAYMT";
            case CLMATTCH: return "CLMATTCH";
            case COVAUTH: return "COVAUTH";
            case COVERAGE: return "COVERAGE";
            case ELIGDTRM: return "ELIGDTRM";
            case ELIGVER: return "ELIGVER";
            case ENROLLM: return "ENROLLM";
            case REMITADV: return "REMITADV";
            case HRESCH: return "HRESCH";
            case CLINTRCH: return "CLINTRCH";
            case PATRQT: return "PATRQT";
            case FAMRQT: return "FAMRQT";
            case PWATRNY: return "PWATRNY";
            case SUPNWK: return "SUPNWK";
            case PUBHLTH: return "PUBHLTH";
            case DISASTER: return "DISASTER";
            case THREAT: return "THREAT";
            case TREAT: return "TREAT";
            case CAREMGT: return "CAREMGT";
            case CLINTRL: return "CLINTRL";
            case ETREAT: return "ETREAT";
            case POPHLTH: return "POPHLTH";
            case _ACTINFORMATIONPRIVACYREASON: return "_ActInformationPrivacyReason";
            case MARKT: return "MARKT";
            case OPERAT: return "OPERAT";
            case LEGAL: return "LEGAL";
            case ACCRED: return "ACCRED";
            case COMPL: return "COMPL";
            case ENADMIN: return "ENADMIN";
            case OUTCOMS: return "OUTCOMS";
            case PRGRPT: return "PRGRPT";
            case QUALIMP: return "QUALIMP";
            case SYSADMN: return "SYSADMN";
            case PAYMT: return "PAYMT";
            case RESCH: return "RESCH";
            case SRVC: return "SRVC";
            case _ACTINVALIDREASON: return "_ActInvalidReason";
            case ADVSTORAGE: return "ADVSTORAGE";
            case COLDCHNBRK: return "COLDCHNBRK";
            case EXPLOT: return "EXPLOT";
            case OUTSIDESCHED: return "OUTSIDESCHED";
            case PRODRECALL: return "PRODRECALL";
            case _ACTINVOICECANCELREASON: return "_ActInvoiceCancelReason";
            case INCCOVPTY: return "INCCOVPTY";
            case INCINVOICE: return "INCINVOICE";
            case INCPOLICY: return "INCPOLICY";
            case INCPROV: return "INCPROV";
            case _ACTNOIMMUNIZATIONREASON: return "_ActNoImmunizationReason";
            case IMMUNE: return "IMMUNE";
            case MEDPREC: return "MEDPREC";
            case OSTOCK: return "OSTOCK";
            case PATOBJ: return "PATOBJ";
            case PHILISOP: return "PHILISOP";
            case RELIG: return "RELIG";
            case VACEFF: return "VACEFF";
            case VACSAF: return "VACSAF";
            case _ACTSUPPLYFULFILLMENTREFUSALREASON: return "_ActSupplyFulfillmentRefusalReason";
            case FRR01: return "FRR01";
            case FRR02: return "FRR02";
            case FRR03: return "FRR03";
            case FRR04: return "FRR04";
            case FRR05: return "FRR05";
            case FRR06: return "FRR06";
            case _CLINICALRESEARCHEVENTREASON: return "_ClinicalResearchEventReason";
            case RET: return "RET";
            case SCH: return "SCH";
            case TRM: return "TRM";
            case UNS: return "UNS";
            case _CLINICALRESEARCHOBSERVATIONREASON: return "_ClinicalResearchObservationReason";
            case NPT: return "NPT";
            case PPT: return "PPT";
            case UPT: return "UPT";
            case _COMBINEDPHARMACYORDERSUSPENDREASONCODE: return "_CombinedPharmacyOrderSuspendReasonCode";
            case ALTCHOICE: return "ALTCHOICE";
            case CLARIF: return "CLARIF";
            case DRUGHIGH: return "DRUGHIGH";
            case HOSPADM: return "HOSPADM";
            case LABINT: return "LABINT";
            case NONAVAIL: return "NON-AVAIL";
            case PREG: return "PREG";
            case SALG: return "SALG";
            case SDDI: return "SDDI";
            case SDUPTHER: return "SDUPTHER";
            case SINTOL: return "SINTOL";
            case SURG: return "SURG";
            case WASHOUT: return "WASHOUT";
            case _CONTROLACTNULLIFICATIONREASONCODE: return "_ControlActNullificationReasonCode";
            case ALTD: return "ALTD";
            case EIE: return "EIE";
            case NORECMTCH: return "NORECMTCH";
            case _CONTROLACTNULLIFICATIONREFUSALREASONTYPE: return "_ControlActNullificationRefusalReasonType";
            case INRQSTATE: return "INRQSTATE";
            case NOMATCH: return "NOMATCH";
            case NOPRODMTCH: return "NOPRODMTCH";
            case NOSERMTCH: return "NOSERMTCH";
            case NOVERMTCH: return "NOVERMTCH";
            case NOPERM: return "NOPERM";
            case NOUSERPERM: return "NOUSERPERM";
            case NOAGNTPERM: return "NOAGNTPERM";
            case WRNGVER: return "WRNGVER";
            case _CONTROLACTREASON: return "_ControlActReason";
            case _MEDICATIONORDERABORTREASONCODE: return "_MedicationOrderAbortReasonCode";
            case DISCONT: return "DISCONT";
            case INEFFECT: return "INEFFECT";
            case MONIT: return "MONIT";
            case NOREQ: return "NOREQ";
            case NOTCOVER: return "NOTCOVER";
            case PREFUS: return "PREFUS";
            case RECALL: return "RECALL";
            case REPLACE: return "REPLACE";
            case DOSECHG: return "DOSECHG";
            case REPLACEFIX: return "REPLACEFIX";
            case UNABLE: return "UNABLE";
            case _MEDICATIONORDERRELEASEREASONCODE: return "_MedicationOrderReleaseReasonCode";
            case HOLDDONE: return "HOLDDONE";
            case HOLDINAP: return "HOLDINAP";
            case _MODIFYPRESCRIPTIONREASONTYPE: return "_ModifyPrescriptionReasonType";
            case ADMINERROR: return "ADMINERROR";
            case CLINMOD: return "CLINMOD";
            case _PHARMACYSUPPLYEVENTABORTREASON: return "_PharmacySupplyEventAbortReason";
            case CONTRA: return "CONTRA";
            case FOABORT: return "FOABORT";
            case FOSUSP: return "FOSUSP";
            case NOPICK: return "NOPICK";
            case PATDEC: return "PATDEC";
            case QUANTCHG: return "QUANTCHG";
            case _PHARMACYSUPPLYEVENTSTOCKREASONCODE: return "_PharmacySupplyEventStockReasonCode";
            case FLRSTCK: return "FLRSTCK";
            case LTC: return "LTC";
            case OFFICE: return "OFFICE";
            case PHARM: return "PHARM";
            case PROG: return "PROG";
            case _PHARMACYSUPPLYREQUESTRENEWALREFUSALREASONCODE: return "_PharmacySupplyRequestRenewalRefusalReasonCode";
            case ALREADYRX: return "ALREADYRX";
            case FAMPHYS: return "FAMPHYS";
            case MODIFY: return "MODIFY";
            case NEEDAPMT: return "NEEDAPMT";
            case NOTAVAIL: return "NOTAVAIL";
            case NOTPAT: return "NOTPAT";
            case ONHOLD: return "ONHOLD";
            case PRNA: return "PRNA";
            case STOPMED: return "STOPMED";
            case TOOEARLY: return "TOOEARLY";
            case _SUPPLYORDERABORTREASONCODE: return "_SupplyOrderAbortReasonCode";
            case IMPROV: return "IMPROV";
            case INTOL: return "INTOL";
            case NEWSTR: return "NEWSTR";
            case NEWTHER: return "NEWTHER";
            case _GENERICUPDATEREASONCODE: return "_GenericUpdateReasonCode";
            case CHGDATA: return "CHGDATA";
            case FIXDATA: return "FIXDATA";
            case MDATA: return "MDATA";
            case NEWDATA: return "NEWDATA";
            case UMDATA: return "UMDATA";
            case _PATIENTPROFILEQUERYREASONCODE: return "_PatientProfileQueryReasonCode";
            case ADMREV: return "ADMREV";
            case PATCAR: return "PATCAR";
            case PATREQ: return "PATREQ";
            case PRCREV: return "PRCREV";
            case REGUL: return "REGUL";
            case RSRCH: return "RSRCH";
            case VALIDATION: return "VALIDATION";
            case _PHARMACYSUPPLYREQUESTFULFILLERREVISIONREFUSALREASONCODE: return "_PharmacySupplyRequestFulfillerRevisionRefusalReasonCode";
            case LOCKED: return "LOCKED";
            case UNKWNTARGET: return "UNKWNTARGET";
            case _REFUSALREASONCODE: return "_RefusalReasonCode";
            case _SCHEDULINGACTREASON: return "_SchedulingActReason";
            case BLK: return "BLK";
            case DEC: return "DEC";
            case FIN: return "FIN";
            case MED: return "MED";
            case MTG: return "MTG";
            case PHY: return "PHY";
            case _STATUSREVISIONREFUSALREASONCODE: return "_StatusRevisionRefusalReasonCode";
            case FILLED: return "FILLED";
            case _SUBSTANCEADMINISTRATIONPERMISSIONREFUSALREASONCODE: return "_SubstanceAdministrationPermissionRefusalReasonCode";
            case PATINELIG: return "PATINELIG";
            case PROTUNMET: return "PROTUNMET";
            case PROVUNAUTH: return "PROVUNAUTH";
            case _SUBSTANCEADMINSUBSTITUTIONNOTALLOWEDREASON: return "_SubstanceAdminSubstitutionNotAllowedReason";
            case ALGINT: return "ALGINT";
            case COMPCON: return "COMPCON";
            case THERCHAR: return "THERCHAR";
            case TRIAL: return "TRIAL";
            case _SUBSTANCEADMINSUBSTITUTIONREASON: return "_SubstanceAdminSubstitutionReason";
            case CT: return "CT";
            case FP: return "FP";
            case OS: return "OS";
            case RR: return "RR";
            case _TRANSFERACTREASON: return "_TransferActReason";
            case ER: return "ER";
            case RQ: return "RQ";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActReason";
        }
        public String getDefinition() {
          switch (this) {
            case _ACTACCOMMODATIONREASON: return "Identifies the reason the patient is assigned to this accommodation type";
            case ACCREQNA: return "Accommodation requested is not available.";
            case FLRCNV: return "Accommodation is assigned for floor convenience.";
            case MEDNEC: return "Required for medical reasons(s).";
            case PAT: return "The Patient requested the action";
            case _ACTCOVERAGEREASON: return "Description:Codes used to specify reasons or criteria relating to coverage provided under a policy or program.  May be used to convey reasons pertaining to coverage contractual provisions, including criteria for eligibility, coverage limitations, coverage maximums, or financial participation required of covered parties.";
            case _ELIGIBILITYACTREASONCODE: return "Identifies the reason or rational for why a person is eligibile for benefits under an insurance policy or progam. \r\n\n                        \n                           Examples:  A person is a claimant under an automobile insurance policy are client deceased & adopted client has been given a new policy identifier.  A new employee is eligible for health insurance as an employment benefit.  A person meets a government program eligibility criteria for financial, age or health status.";
            case _ACTINELIGIBILITYREASON: return "Identifies the reason or rational for why a person is not eligibile for benefits under an insurance policy.\r\n\n                        Examples are client deceased & adopted client has been given a new policy identifier.";
            case COVSUS: return "When a client has no contact with the health system for an extended period, coverage is suspended.  Client will be reinstated to original start date upon proof of identification, residency etc.\r\n\n                        Example: Coverage may be suspended during a strike situation, when employer benefits for employees are not covered (i.e. not in effect).";
            case DECSD: return "Client deceased.";
            case REGERR: return "Client was registered in error.";
            case _COVERAGEELIGIBILITYREASON: return "Definition: Identifies the reason or rational for why a person is eligibile for benefits under an insurance policy or progam. \r\n\n                        \n                           Examples:  A person is a claimant under an automobile insurance policy are client deceased & adopted client has been given a new policy identifier.  A new employee is eligible for health insurance as an employment benefit.  A person meets a government program eligibility criteria for financial, age or health status.";
            case AGE: return "A person becomes eligible for a program based on age.\r\n\n                        \n                           Example:  In the U.S., a person who is 65 years of age or older is eligible for Medicare.";
            case CRIME: return "A person becomes eligible for insurance or a program because of crime related health condition or injury. \r\n\n                        \n                           Example:  A person is a claimant under the U.S. Crime Victims Compensation program.";
            case DIS: return "A person becomes a claimant under a disability income insurance policy or a disability rehabilitation program because of a health condition or injury which limits the person's ability to earn an income or function without institutionalization.";
            case EMPLOY: return "A person becomes eligible for insurance provided as an employment benefit based on employment status.";
            case FINAN: return "A person becomes eligible for a program based on financial criteria.\r\n\n                        \n                           Example:  A person whose family income is below a financial threshold for eligibility for Medicaid or SCHIP.";
            case HEALTH: return "A person becomes eligible for a program because of a qualifying health condition or injury. \r\n\n                        \n                           Examples:  A person is determined to have a qualifying health conditions include pregnancy, HIV/AIDs, tuberculosis, end stage renal disease, breast or cervical cancer, or other condition requiring specialized health services, hospice, institutional or community based care provided under a program";
            case MULTI: return "A person becomes eligible for a program based on more than one criterion.\r\n\n                        \n                           Examples:  In the U.S., a child whose familiy income meets Medicaid financial thresholds and whose age is less than 18 is eligible for the Early and Periodic Screening, Diagnostic, and Treatment program (EPSDT).  A person whose family income meets Medicaid financial thresholds and whose age is 65 years or older is eligible for Medicaid and Medicare, and are referred to as dual eligibles.";
            case PNC: return "A person becomes a claimant under a property and casualty insurance policy because of a related health condition or injury resulting from a circumstance covered under the terms of the policy. \r\n\n                        \n                           Example:  A person is a claimant under a homeowners insurance policy because of an injury sustained on the policyholderaTMs premises.";
            case STATUTORY: return "A person becomes eligible for a program based on statutory criteria.\r\n\n                        \n                           Examples:  A person is a member of an indigenous group, a veteran of military service, or  in the U.S., a recipient of adoption assistance and foster care under Title IV-E of the Social Security.";
            case VEHIC: return "A person becomes a claimant under a motor vehicle accident insurance because of a motor vehicle accident related health condition or injury.";
            case WORK: return "A person becomes eligible for insurance or a program because of a work related health condition or injury. \r\n\n                        \n                           Example:  A person is a claimant under the U.S. Black Lung Program.";
            case _ACTINFORMATIONMANAGEMENTREASON: return "Description:The rationale or purpose for an act relating to information management, such as archiving information for the purpose of complying with an enterprise data retention policy.";
            case _ACTHEALTHINFORMATIONMANAGEMENTREASON: return "Description:The rationale or purpose for an act relating to health information management, such as archiving information for the purpose of complying with an organization policy or jurisdictional law relating to  data retention.";
            case _ACTCONSENTINFORMATIONACCESSOVERRIDEREASON: return "To perform one or more operations on information to which the patient has not consented as deemed necessary by authorized entities for providing care in the best interest of the patient; providing immediately needed health care for an emergent condition;  or for protecting public or third party safety.\r\n\n                        \n                           Usage Notes: Used to convey the reason that a provider or other entity may or has accessed personal healthcare information.  Typically, this involves overriding the subject's consent directives.";
            case OVRER: return "To perform one or more operations on information to which the patient has not consented by authorized entities for treating a condition which poses an immediate threat to the patient's health and which requires immediate medical intervention.\r\n\n                        \n                           Usage Notes: The patient is unable to provide consent, but the provider determines they have an urgent healthcare related reason to access the record.";
            case OVRPJ: return "To perform one or more operations on information to which the patient declined to consent for providing health care.\r\n\n                        \n                           Usage Notes: The patient, while able to give consent, has not.  However the provider believes it is in the patient's interest to access the record without patient consent.";
            case OVRPS: return "To perform one or more operations on information to which the patient has not consented for public safety reasons.\r\n\n                        \n                           Usage Notes: The patient, while able to give consent, has not.  However, the provider believes that access to masked patient information is justified because of concerns related to public safety.";
            case OVRTPS: return "To perform one or more operations on information to which the patient has not consented for third party safety.  \r\n\n                        \n                           Usage Notes: The patient, while able to give consent, has not.  However, the provider believes that access to masked patient information is justified because of concerns related to the health and safety of one or more third parties.";
            case PURPOSEOFUSE: return "Reason for performing one or more operations on information, which may be permitted by source system's security policy in accordance with one or more privacy policies and consent directives.\r\n\n                        \n                           Usage Notes: The rationale or purpose for an act relating to the management of personal health information, such as collecting personal health information for research or public health purposes.";
            case HMARKT: return "To perform one or more operations on information for marketing services and products related to health care.";
            case HOPERAT: return "To perform one or more operations on information used for conducting administrative and contractual activities related to the provision of health care.";
            case DONAT: return "To perform one or more operations on information used for cadaveric organ, eye or tissue donation.";
            case FRAUD: return "To perform one or more operations on information used for fraud detection and prevention processes.";
            case GOV: return "To perform one or more operations on information used within government processes.";
            case HACCRED: return "To perform one or more operations on information for conducting activities related to meeting accreditation criteria.";
            case HCOMPL: return "To perform one or more operations on information used for conducting activities required to meet a mandate.";
            case HDECD: return "To perform one or more operations on information used for handling deceased patient matters.";
            case HDIRECT: return "To perform one or more operation operations on information used to manage a patient directory.\r\n\n                        \n                           Examples: \n                        \r\n\n                        \n                           facility\n                           enterprise\n                           payer\n                           health information exchange patient directory";
            case HLEGAL: return "To perform one or more operations on information for conducting activities required by legal proceeding.";
            case HOUTCOMS: return "To perform one or more operations on information used for assessing results and comparative effectiveness achieved by health care practices and interventions.";
            case HPRGRP: return "To perform one or more operations on information used for conducting activities to meet program accounting requirements.";
            case HQUALIMP: return "To perform one or more operations on information used for conducting administrative activities to improve health care quality.";
            case HSYSADMIN: return "To perform one or more operations on information to administer the electronic systems used for the delivery of health care.";
            case MEMADMIN: return "To perform one or more operations on information to administer health care coverage to an enrollee under a policy or program.";
            case PATADMIN: return "To perform one or more operations on information used for operational activities conducted to administer the delivery of health care to a patient.";
            case PATSFTY: return "To perform one or more operations on information in processes related to ensuring the safety of health care.";
            case PERFMSR: return "To perform one or more operations on information used for monitoring performance of recommended health care practices and interventions.";
            case RECORDMGT: return "To perform one or more operations on information used within the health records management process.";
            case TRAIN: return "To perform one or more operations on information used in training and education.";
            case HPAYMT: return "To perform one or more operations on information for conducting financial or contractual activities related to payment for provision of health care.";
            case CLMATTCH: return "To perform one or more operations on information for provision of additional clinical evidence in support of a request for coverage or payment for health services.";
            case COVAUTH: return "To perform one or more operations on information for conducting prior authorization or predetermination of coverage for services.";
            case COVERAGE: return "To perform one or more operations on information for conducting activities related to coverage under a program or policy.";
            case ELIGDTRM: return "To perform one or more operations on information used for conducting eligibility determination for coverage in a program or policy.  May entail review of financial status or disability assessment.";
            case ELIGVER: return "To perform one or more operations on information used for conducting eligibility verification of coverage in a program or policy.  May entail provider contacting coverage source (e.g., government health program such as workers compensation or health plan) for confirmation of enrollment, eligibility for specific services, and any applicable copays.";
            case ENROLLM: return "To perform one or more operations on information used for enrolling a covered party in a program or policy.  May entail recording of covered party's and any dependent's demographic information and benefit choices.";
            case REMITADV: return "To perform one or more operations on information about the amount remitted for a health care claim.";
            case HRESCH: return "To perform one or more operations on information for conducting scientific investigations to obtain health care knowledge.";
            case CLINTRCH: return "To perform one or more operations on information for conducting scientific investigations in accordance with clinical trial protocols to obtain health care knowledge.";
            case PATRQT: return "To perform one or more operations on information in response to a patient's request.";
            case FAMRQT: return "To perform one or more operations on information in response to a request by a family member authorized by the patient.";
            case PWATRNY: return "To perform one or more operations on information in response to a request by a person appointed as the patient's legal representative.";
            case SUPNWK: return "To perform one or more operations on information in response to a request by a person authorized by the patient.";
            case PUBHLTH: return "To perform one or more operations on information for conducting public health activities, such as the reporting of notifiable conditions.";
            case DISASTER: return "To perform one or more operations on information used for provision of immediately needed health care to a population of living subjects located in a disaster zone.";
            case THREAT: return "To perform one or more operations on information used to prevent injury or disease to living subjects who may be the target of violence.";
            case TREAT: return "To perform one or more operations on information for provision of health care.";
            case CAREMGT: return "To perform one or more operations on information for provision of health care coordination.";
            case CLINTRL: return "To perform health care as part of the clinical trial protocol.";
            case ETREAT: return "To perform one or more operations on information for provision of immediately needed health care for an emergent condition.";
            case POPHLTH: return "To perform one or more operations on information for provision of health care to a population of living subjects, e.g., needle exchange program.";
            case _ACTINFORMATIONPRIVACYREASON: return "Description:The rationale or purpose for an act relating to the management of personal information, such as disclosing personal tax information for the purpose of complying with a court order.";
            case MARKT: return "Description:";
            case OPERAT: return "Description:Administrative and contractual processes required to support an activity, product, or service";
            case LEGAL: return "Definition:To provide information as a result of a subpoena.";
            case ACCRED: return "Description:Operational activities conducted for the purposes of meeting of criteria defined by an accrediting entity for an activity, product, or service";
            case COMPL: return "Description:Operational activities required to meet a mandate related to an activity, product, or service";
            case ENADMIN: return "Description:Operational activities conducted to administer information relating to entities involves with an activity, product, or service";
            case OUTCOMS: return "Description:Operational activities conducted for the purposes of assessing the results of an activity, product, or service";
            case PRGRPT: return "Description:Operational activities conducted to meet program accounting requirements related to an activity, product, or service";
            case QUALIMP: return "Description:Operational activities conducted for the purposes of improving the quality of an activity, product, or service";
            case SYSADMN: return "Description:Operational activities conducted to administer the electronic systems used for an activity, product, or service";
            case PAYMT: return "Description:Administrative, financial, and contractual processes related to payment for an activity, product, or service";
            case RESCH: return "Description:Investigative activities conducted for the purposes of obtaining knowledge";
            case SRVC: return "Description:Provision of a service, product, or capability to an individual or organization";
            case _ACTINVALIDREASON: return "Description: Types of reasons why a substance is invalid for use.";
            case ADVSTORAGE: return "Description: Storage conditions caused the substance to be ineffective.";
            case COLDCHNBRK: return "Description: Cold chain was not maintained for the substance.";
            case EXPLOT: return "Description: The lot from which the substance was drawn was expired.";
            case OUTSIDESCHED: return "The substance was administered outside of the recommended schedule or practice.";
            case PRODRECALL: return "Description: The substance was recalled by the manufacturer.";
            case _ACTINVOICECANCELREASON: return "Domain specifies the codes used to describe reasons why a Provider is cancelling an Invoice or Invoice Grouping.";
            case INCCOVPTY: return "The covered party (patient) specified with the Invoice is not correct.";
            case INCINVOICE: return "The billing information, specified in the Invoice Elements, is not correct.  This could include incorrect costing for items included in the Invoice.";
            case INCPOLICY: return "The policy specified with the Invoice is not correct.  For example, it may belong to another Adjudicator or Covered Party.";
            case INCPROV: return "The provider specified with the Invoice is not correct.";
            case _ACTNOIMMUNIZATIONREASON: return "A coded description of the reason for why a patient did not receive a scheduled immunization.\r\n\n                        (important for public health strategy";
            case IMMUNE: return "Definition:Testing has shown that the patient already has immunity to the agent targeted by the immunization.";
            case MEDPREC: return "Definition:The patient currently has a medical condition for which the vaccine is contraindicated or for which precaution is warranted.";
            case OSTOCK: return "Definition:There was no supply of the product on hand to perform the service.";
            case PATOBJ: return "Definition:The patient or their guardian objects to receiving the vaccine.";
            case PHILISOP: return "Definition:The patient or their guardian objects to receiving the vaccine because of philosophical beliefs.";
            case RELIG: return "Definition:The patient or their guardian objects to receiving the vaccine on religious grounds.";
            case VACEFF: return "Definition:The intended vaccine has expired or is otherwise believed to no longer be effective.\r\n\n                        \n                           Example:Due to temperature exposure.";
            case VACSAF: return "Definition:The patient or their guardian objects to receiving the vaccine because of concerns over its safety.";
            case _ACTSUPPLYFULFILLMENTREFUSALREASON: return "Indicates why a fulfiller refused to fulfill a supply order, and considered it important to notify other providers of their decision.  E.g. \"Suspect fraud\", \"Possible abuse\", \"Contraindicated\".\r\n\n                        (used when capturing 'refusal to fill' annotations)";
            case FRR01: return "Definition:The order has been stopped by the prescriber but this fact has not necessarily captured electronically.\r\n\n                        \n                           Example:A verbal stop, a fax, etc.";
            case FRR02: return "Definition:Order has not been fulfilled within a reasonable amount of time, and may not be current.";
            case FRR03: return "Definition:Data needed to safely act on the order which was expected to become available independent of the order is not yet available\r\n\n                        \n                           Example:Lab results, diagnostic imaging, etc.";
            case FRR04: return "Definition:Product not available or manufactured. Cannot supply.";
            case FRR05: return "Definition:The dispenser has ethical, religious or moral objections to fulfilling the order/dispensing the product.";
            case FRR06: return "Definition:Fulfiller not able to provide appropriate care associated with fulfilling the order.\r\n\n                        \n                           Example:Therapy requires ongoing monitoring by fulfiller and fulfiller will be ending practice, leaving town, unable to schedule necessary time, etc.";
            case _CLINICALRESEARCHEVENTREASON: return "Definition:Specifies the reason that an event occurred in a clinical research study.";
            case RET: return "Definition:The event occurred so that a test or observation performed at a prior event could be performed again due to conditions set forth in the protocol.";
            case SCH: return "Definition:The event occurred due to it being scheduled in the research protocol.";
            case TRM: return "Definition:The event occurred in order to terminate the subject's participation in the study.";
            case UNS: return "Definition:The event that occurred was initiated by a study participant (e.g. the subject or the investigator), and did not occur for protocol reasons.";
            case _CLINICALRESEARCHOBSERVATIONREASON: return "Definition:SSpecifies the reason that a test was performed or observation collected in a clinical research study.\r\n\n                        \n                           Note:This set of codes are not strictly reasons, but are used in the currently Normative standard.  Future revisions of the specification will model these as ActRelationships and thes codes may subsequently be retired.  Thus, these codes should not be used for new specifications.";
            case NPT: return "Definition:The observation or test was neither defined or scheduled in the study protocol.";
            case PPT: return "Definition:The observation or test occurred due to it being defined in the research protocol, and during an activity or event that was scheduled in the protocol.";
            case UPT: return ":The observation or test occurred as defined in the research protocol, but at a point in time not specified in the study protocol.";
            case _COMBINEDPHARMACYORDERSUSPENDREASONCODE: return "Description:Indicates why the prescription should be suspended.";
            case ALTCHOICE: return "Description:This therapy has been ordered as a backup to a preferred therapy.  This order will be released when and if the preferred therapy is unsuccessful.";
            case CLARIF: return "Description:Clarification is required before the order can be acted upon.";
            case DRUGHIGH: return "Description:The current level of the medication in the patient's system is too high.  The medication is suspended to allow the level to subside to a safer level.";
            case HOSPADM: return "Description:The patient has been admitted to a care facility and their community medications are suspended until hospital discharge.";
            case LABINT: return "Description:The therapy would interfere with a planned lab test and the therapy is being withdrawn until the test is completed.";
            case NONAVAIL: return "Description:Patient not available for a period of time due to a scheduled therapy, leave of absence or other reason.";
            case PREG: return "Description:The patient is pregnant or breast feeding.  The therapy will be resumed when the pregnancy is complete and the patient is no longer breastfeeding.";
            case SALG: return "Description:The patient is believed to be allergic to a substance that is part of the therapy and the therapy is being temporarily withdrawn to confirm.";
            case SDDI: return "Description:The drug interacts with a short-term treatment that is more urgently required.  This order will be resumed when the short-term treatment is complete.";
            case SDUPTHER: return "Description:Another short-term co-occurring therapy fulfills the same purpose as this therapy.  This therapy will be resumed when the co-occuring therapy is complete.";
            case SINTOL: return "Description:The patient is believed to have an intolerance to a substance that is part of the therapy and the therapy is being temporarily withdrawn to confirm.";
            case SURG: return "Description:The drug is contraindicated for patients receiving surgery and the patient is scheduled to be admitted for surgery in the near future.  The drug will be resumed when the patient has sufficiently recovered from the surgery.";
            case WASHOUT: return "Description:The patient was previously receiving a medication contraindicated with the current medication.  The current medication will remain on hold until the prior medication has been cleansed from their system.";
            case _CONTROLACTNULLIFICATIONREASONCODE: return "Description:Identifies reasons for nullifying (retracting) a particular control act.";
            case ALTD: return "Description:The decision on which the recorded information was based was changed before the decision had an effect.\r\n\n                        \n                           Example:Aborted prescription before patient left office, released prescription before suspend took effect.";
            case EIE: return "Description:The information was recorded incorrectly or was recorded in the wrong record.";
            case NORECMTCH: return "Description: There is no match for the record in the database.";
            case _CONTROLACTNULLIFICATIONREFUSALREASONTYPE: return "Description: Reasons to refuse a transaction to be undone.";
            case INRQSTATE: return "The record is already in the requested state.";
            case NOMATCH: return "Description: There is no match.";
            case NOPRODMTCH: return "Description: There is no match for the product in the master file repository.";
            case NOSERMTCH: return "Description: There is no match for the service in the master file repository.";
            case NOVERMTCH: return "Description: There is no match for the record and version.";
            case NOPERM: return "Description: There is no permission.";
            case NOUSERPERM: return "Definition:The user does not have permission";
            case NOAGNTPERM: return "Description: The agent does not have permission.";
            case WRNGVER: return "Description: The record and version requested to update is not the current version.";
            case _CONTROLACTREASON: return "Identifies why a specific query, request, or other trigger event occurred.";
            case _MEDICATIONORDERABORTREASONCODE: return "Description:Indicates the reason the medication order should be aborted.";
            case DISCONT: return "Description:The medication is no longer being manufactured or is otherwise no longer available.";
            case INEFFECT: return "Description:The therapy has been found to not have the desired therapeutic benefit on the patient.";
            case MONIT: return "Description:Monitoring the patient while taking the medication, the decision has been made that the therapy is no longer appropriate.";
            case NOREQ: return "Description:The underlying condition has been resolved or has evolved such that a different treatment is no longer needed.";
            case NOTCOVER: return "Description:The product does not have (or no longer has) coverage under the patientaTMs insurance policy.";
            case PREFUS: return "Description:The patient refused to take the product.";
            case RECALL: return "Description:The manufacturer or other agency has requested that stocks of a medication be removed from circulation.";
            case REPLACE: return "Description:Item in current order is no longer in use as requested and a new one has/will be created to replace it.";
            case DOSECHG: return "Description:The medication is being re-prescribed at a different dosage.";
            case REPLACEFIX: return "Description:Current order was issued with incorrect data and a new order has/will be created to replace it.";
            case UNABLE: return "Description:<The patient is not (or is no longer) able to use the medication in a manner prescribed.\r\n\n                        \n                           Example:CanaTMt swallow.";
            case _MEDICATIONORDERRELEASEREASONCODE: return "Definition:A collection of concepts that indicate why the prescription should be released from suspended state.";
            case HOLDDONE: return "Definition:The original reason for suspending the medication has ended.";
            case HOLDINAP: return "Definition:";
            case _MODIFYPRESCRIPTIONREASONTYPE: return "Types of reason why a prescription is being changed.";
            case ADMINERROR: return "Order was created with incorrect data and is changed to reflect the intended accuracy of the order.";
            case CLINMOD: return "Order is changed based on a clinical reason.";
            case _PHARMACYSUPPLYEVENTABORTREASON: return "Definition:Identifies why the dispense event was not completed.";
            case CONTRA: return "Definition:Contraindication identified";
            case FOABORT: return "Definition:Order to be fulfilled was aborted";
            case FOSUSP: return "Definition:Order to be fulfilled was suspended";
            case NOPICK: return "Definition:Patient did not come to get medication";
            case PATDEC: return "Definition:Patient changed their mind regarding obtaining medication";
            case QUANTCHG: return "Definition:Patient requested a revised quantity of medication";
            case _PHARMACYSUPPLYEVENTSTOCKREASONCODE: return "Definition:A collection of concepts that indicates the reason for a \"bulk supply\" of medication.";
            case FLRSTCK: return "Definition:The bulk supply is issued to replenish a ward for local dispensing.  (Includes both mobile and fixed-location ward stocks.)";
            case LTC: return "Definition:The bulk supply will be administered within a long term care facility.";
            case OFFICE: return "Definition:The bulk supply is intended for general clinician office use.";
            case PHARM: return "Definition:The bulk supply is being transferred to another dispensing facility to.\r\n\n                        \n                           Example:Alleviate a temporary shortage.";
            case PROG: return "Definition:The bulk supply is intended for dispensing according to a specific program.\r\n\n                        \n                           Example:Mass immunization.";
            case _PHARMACYSUPPLYREQUESTRENEWALREFUSALREASONCODE: return "Definition:A collection of concepts that identifies why a renewal prescription has been refused.";
            case ALREADYRX: return "Definition:Patient has already been given a new (renewal) prescription.";
            case FAMPHYS: return "Definition:Request for further authorization must be done through patient's family physician.";
            case MODIFY: return "Definition:Therapy has been changed and new prescription issued";
            case NEEDAPMT: return "Definition:Patient must see prescriber prior to further fills.";
            case NOTAVAIL: return "Definition:Original prescriber is no longer available to prescribe and no other prescriber has taken responsibility for the patient.";
            case NOTPAT: return "Definition:Patient no longer or has never been under this prescribers care.";
            case ONHOLD: return "Definition:This medication is on hold.";
            case PRNA: return "Description:This product is not available or manufactured.";
            case STOPMED: return "Renewing or original prescriber informed patient to stop using the medication.";
            case TOOEARLY: return "Definition:The patient should have medication remaining.";
            case _SUPPLYORDERABORTREASONCODE: return "Definition:A collection of concepts that indicates why the prescription should no longer be allowed to be dispensed (but can still administer what is already being dispensed).";
            case IMPROV: return "Definition:The patient's medical condition has nearly abated.";
            case INTOL: return "Description:The patient has an intolerance to the medication.";
            case NEWSTR: return "Definition:The current medication will be replaced by a new strength of the same medication.";
            case NEWTHER: return "Definition:A new therapy will be commenced when current supply exhausted.";
            case _GENERICUPDATEREASONCODE: return "Description:Identifies why a change is being made to a  record.";
            case CHGDATA: return "Description:Information has changed since the record was created.";
            case FIXDATA: return "Description:Previously recorded information was erroneous and is being corrected.";
            case MDATA: return "Information is combined into the record.";
            case NEWDATA: return "Description:New information has become available to supplement the record.";
            case UMDATA: return "Information is separated from the record.";
            case _PATIENTPROFILEQUERYREASONCODE: return "Definition:A collection of concepts identifying why the patient's profile is being queried.";
            case ADMREV: return "Definition: To evaluate for service authorization, payment, reporting, or performance/outcome measures.";
            case PATCAR: return "Definition:To obtain records as part of patient care.";
            case PATREQ: return "Definition:Patient requests information from their profile.";
            case PRCREV: return "Definition:To evaluate the provider's current practice for professional-improvement reasons.";
            case REGUL: return "Description:Review for the purpose of regulatory compliance.";
            case RSRCH: return "Definition:To provide research data, as authorized by the patient.";
            case VALIDATION: return "Description:To validate the patient's record.\r\n\n                        \n                           Example:Merging or unmerging records.";
            case _PHARMACYSUPPLYREQUESTFULFILLERREVISIONREFUSALREASONCODE: return "Definition:Indicates why the request to transfer a prescription from one dispensing facility to another has been refused.";
            case LOCKED: return "Definition:The prescription may not be reassigned from the original pharmacy.";
            case UNKWNTARGET: return "Definition:The target facility does not recognize the dispensing facility.";
            case _REFUSALREASONCODE: return "Description: Identifies why a request to add (or activate) a record is being refused.  Examples include the receiving system not able to match the identifier and find that record in the receiving system, having no permission, or a detected issue exists which precludes the requested action.";
            case _SCHEDULINGACTREASON: return "Reasons for cancelling or rescheduling an Appointment";
            case BLK: return "The time slots previously allocated are now blocked and no longer available for booking Appointments";
            case DEC: return "The Patient is deceased";
            case FIN: return "Patient unable to pay and not covered by insurance";
            case MED: return "The medical condition of the Patient has changed";
            case MTG: return "The Physician is in a meeting.  For example, he/she may request administrative time to talk to family after appointment";
            case PHY: return "The Physician requested the action";
            case _STATUSREVISIONREFUSALREASONCODE: return "Indicates why the act revision (status update) is being refused.";
            case FILLED: return "Ordered quantity has already been completely fulfilled.";
            case _SUBSTANCEADMINISTRATIONPERMISSIONREFUSALREASONCODE: return "Definition:Indicates why the requested authorization to prescribe or dispense a medication has been refused.";
            case PATINELIG: return "Definition:Patient not eligible for drug";
            case PROTUNMET: return "Definition:Patient does not meet required protocol";
            case PROVUNAUTH: return "Definition:Provider is not authorized to prescribe or dispense";
            case _SUBSTANCEADMINSUBSTITUTIONNOTALLOWEDREASON: return "Reasons why substitution of a substance administration request is not permitted.";
            case ALGINT: return "Definition: Patient has had a prior allergic intolerance response to alternate product or one of its components.";
            case COMPCON: return "Definition: Patient has compliance issues with medication such as differing appearance, flavor, size, shape or consistency.";
            case THERCHAR: return "The prescribed product has specific clinical release or other therapeutic characteristics not shared by other substitutable medications.";
            case TRIAL: return "Definition: The specific manufactured drug is part of a clinical trial.";
            case _SUBSTANCEADMINSUBSTITUTIONREASON: return "SubstanceAdminSubstitutionReason";
            case CT: return "Indicates that the decision to substitute or to not substitute was driven by a desire to maintain consistency with a pre-existing therapy.  I.e. The performer provided the same item/service as had been previously provided rather than providing exactly what was ordered, or rather than substituting with a lower-cost equivalent.";
            case FP: return "Indicates that the decision to substitute or to not substitute was driven by a policy expressed within the formulary.";
            case OS: return "In the case of 'substitution', indicates that the substitution occurred because the ordered item was not in stock.  In the case of 'no substitution', indicates that a cheaper equivalent was not substituted because it was not in stock.";
            case RR: return "Indicates that the decision to substitute or to not substitute was driven by a jurisdictional regulatory requirement mandating or prohibiting substitution.";
            case _TRANSFERACTREASON: return "The explanation for why a patient is moved from one location to another within the organization";
            case ER: return "Moved to an error in placing the patient in the original location.";
            case RQ: return "Moved at the request of the patient.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ACTACCOMMODATIONREASON: return "ActAccommodationReason";
            case ACCREQNA: return "Accommodation Requested Not Available";
            case FLRCNV: return "Floor Convenience";
            case MEDNEC: return "Medical Necessity";
            case PAT: return "Patient request";
            case _ACTCOVERAGEREASON: return "ActCoverageReason";
            case _ELIGIBILITYACTREASONCODE: return "EligibilityActReasonCode";
            case _ACTINELIGIBILITYREASON: return "ActIneligibilityReason";
            case COVSUS: return "coverage suspended";
            case DECSD: return "deceased";
            case REGERR: return "registered in error";
            case _COVERAGEELIGIBILITYREASON: return "CoverageEligibilityReason";
            case AGE: return "age eligibility";
            case CRIME: return "crime victim";
            case DIS: return "disability";
            case EMPLOY: return "employment benefit";
            case FINAN: return "financial eligibility";
            case HEALTH: return "health status";
            case MULTI: return "multiple criteria eligibility";
            case PNC: return "property and casualty condition";
            case STATUTORY: return "statutory eligibility";
            case VEHIC: return "motor vehicle accident victim";
            case WORK: return "work related";
            case _ACTINFORMATIONMANAGEMENTREASON: return "ActInformationManagementReason";
            case _ACTHEALTHINFORMATIONMANAGEMENTREASON: return "ActHealthInformationManagementReason";
            case _ACTCONSENTINFORMATIONACCESSOVERRIDEREASON: return "ActConsentInformationAccessOverrideReason";
            case OVRER: return "emergency treatment override";
            case OVRPJ: return "professional judgment override";
            case OVRPS: return "public safety override";
            case OVRTPS: return "third party safety override";
            case PURPOSEOFUSE: return "purpose of use";
            case HMARKT: return "healthcare marketing";
            case HOPERAT: return "healthcare operations";
            case DONAT: return "donation";
            case FRAUD: return "fraud";
            case GOV: return "government";
            case HACCRED: return "health accreditation";
            case HCOMPL: return "health compliance";
            case HDECD: return "decedent";
            case HDIRECT: return "directory";
            case HLEGAL: return "legal";
            case HOUTCOMS: return "health outcome measure";
            case HPRGRP: return "health program reporting";
            case HQUALIMP: return "health quality improvement";
            case HSYSADMIN: return "health system administration";
            case MEMADMIN: return "member administration";
            case PATADMIN: return "patient administration";
            case PATSFTY: return "patient safety";
            case PERFMSR: return "performance measure";
            case RECORDMGT: return "records management";
            case TRAIN: return "training";
            case HPAYMT: return "healthcare payment";
            case CLMATTCH: return "claim attachment";
            case COVAUTH: return "coverage authorization";
            case COVERAGE: return "coverage under policy or program";
            case ELIGDTRM: return "eligibility determination";
            case ELIGVER: return "eligibility verification";
            case ENROLLM: return "enrollment";
            case REMITADV: return "remittance advice";
            case HRESCH: return "healthcare research";
            case CLINTRCH: return "clinical trial research";
            case PATRQT: return "patient requested";
            case FAMRQT: return "family requested";
            case PWATRNY: return "power of attorney";
            case SUPNWK: return "support network";
            case PUBHLTH: return "public health";
            case DISASTER: return "disaster";
            case THREAT: return "threat";
            case TREAT: return "treatment";
            case CAREMGT: return "Care Management";
            case CLINTRL: return "clinical trial";
            case ETREAT: return "Emergency Treatment";
            case POPHLTH: return "population health";
            case _ACTINFORMATIONPRIVACYREASON: return "ActInformationPrivacyReason";
            case MARKT: return "marketing";
            case OPERAT: return "operations";
            case LEGAL: return "subpoena";
            case ACCRED: return "accreditation";
            case COMPL: return "compliance";
            case ENADMIN: return "entity administration";
            case OUTCOMS: return "outcome measure";
            case PRGRPT: return "program reporting";
            case QUALIMP: return "quality improvement";
            case SYSADMN: return "system administration";
            case PAYMT: return "payment";
            case RESCH: return "research";
            case SRVC: return "service";
            case _ACTINVALIDREASON: return "ActInvalidReason";
            case ADVSTORAGE: return "adverse storage condition";
            case COLDCHNBRK: return "cold chain break";
            case EXPLOT: return "expired lot";
            case OUTSIDESCHED: return "administered outside recommended schedule or practice";
            case PRODRECALL: return "product recall";
            case _ACTINVOICECANCELREASON: return "ActInvoiceCancelReason";
            case INCCOVPTY: return "incorrect covered party as patient";
            case INCINVOICE: return "incorrect billing";
            case INCPOLICY: return "incorrect policy";
            case INCPROV: return "incorrect provider";
            case _ACTNOIMMUNIZATIONREASON: return "ActNoImmunizationReason";
            case IMMUNE: return "immunity";
            case MEDPREC: return "medical precaution";
            case OSTOCK: return "product out of stock";
            case PATOBJ: return "patient objection";
            case PHILISOP: return "philosophical objection";
            case RELIG: return "religious objection";
            case VACEFF: return "vaccine efficacy concerns";
            case VACSAF: return "vaccine safety concerns";
            case _ACTSUPPLYFULFILLMENTREFUSALREASON: return "ActSupplyFulfillmentRefusalReason";
            case FRR01: return "order stopped";
            case FRR02: return "stale-dated order";
            case FRR03: return "incomplete data";
            case FRR04: return "product unavailable";
            case FRR05: return "ethical/religious";
            case FRR06: return "unable to provide care";
            case _CLINICALRESEARCHEVENTREASON: return "ClinicalResearchEventReason";
            case RET: return "retest";
            case SCH: return "scheduled";
            case TRM: return "termination";
            case UNS: return "unscheduled";
            case _CLINICALRESEARCHOBSERVATIONREASON: return "ClinicalResearchObservationReason";
            case NPT: return "non-protocol";
            case PPT: return "per protocol";
            case UPT: return "per definition";
            case _COMBINEDPHARMACYORDERSUSPENDREASONCODE: return "CombinedPharmacyOrderSuspendReasonCode";
            case ALTCHOICE: return "try another treatment first";
            case CLARIF: return "prescription requires clarification";
            case DRUGHIGH: return "drug level too high";
            case HOSPADM: return "admission to hospital";
            case LABINT: return "lab interference issues";
            case NONAVAIL: return "patient not-available";
            case PREG: return "parent is pregnant/breast feeding";
            case SALG: return "allergy";
            case SDDI: return "drug interacts with another drug";
            case SDUPTHER: return "duplicate therapy";
            case SINTOL: return "suspected intolerance";
            case SURG: return "patient scheduled for surgery";
            case WASHOUT: return "waiting for old drug to wash out";
            case _CONTROLACTNULLIFICATIONREASONCODE: return "ControlActNullificationReasonCode";
            case ALTD: return "altered decision";
            case EIE: return "entered in error";
            case NORECMTCH: return "no record match";
            case _CONTROLACTNULLIFICATIONREFUSALREASONTYPE: return "ControlActNullificationRefusalReasonType";
            case INRQSTATE: return "in requested state";
            case NOMATCH: return "no match";
            case NOPRODMTCH: return "no product match";
            case NOSERMTCH: return "no service match";
            case NOVERMTCH: return "no version match";
            case NOPERM: return "no permission";
            case NOUSERPERM: return "no user permission";
            case NOAGNTPERM: return "no agent permission";
            case WRNGVER: return "wrong version";
            case _CONTROLACTREASON: return "ControlActReason";
            case _MEDICATIONORDERABORTREASONCODE: return "medication order abort reason";
            case DISCONT: return "product discontinued";
            case INEFFECT: return "ineffective";
            case MONIT: return "response to monitoring";
            case NOREQ: return "no longer required for treatment";
            case NOTCOVER: return "not covered";
            case PREFUS: return "patient refuse";
            case RECALL: return "product recalled";
            case REPLACE: return "change in order";
            case DOSECHG: return "change in medication/dose";
            case REPLACEFIX: return "error in order";
            case UNABLE: return "unable to use";
            case _MEDICATIONORDERRELEASEREASONCODE: return "medication order release reason";
            case HOLDDONE: return "suspend reason no longer applies";
            case HOLDINAP: return "suspend reason inappropriate";
            case _MODIFYPRESCRIPTIONREASONTYPE: return "ModifyPrescriptionReasonType";
            case ADMINERROR: return "administrative error in order";
            case CLINMOD: return "clinical modification";
            case _PHARMACYSUPPLYEVENTABORTREASON: return "PharmacySupplyEventAbortReason";
            case CONTRA: return "contraindication";
            case FOABORT: return "order aborted";
            case FOSUSP: return "order suspended";
            case NOPICK: return "not picked up";
            case PATDEC: return "patient changed mind";
            case QUANTCHG: return "change supply quantity";
            case _PHARMACYSUPPLYEVENTSTOCKREASONCODE: return "pharmacy supply event stock reason";
            case FLRSTCK: return "floor stock";
            case LTC: return "long term care use";
            case OFFICE: return "office use";
            case PHARM: return "pharmacy transfer";
            case PROG: return "program use";
            case _PHARMACYSUPPLYREQUESTRENEWALREFUSALREASONCODE: return "pharmacy supply request renewal refusal reason";
            case ALREADYRX: return "new prescription exists";
            case FAMPHYS: return "family physician must authorize further fills";
            case MODIFY: return "modified prescription exists";
            case NEEDAPMT: return "patient must make appointment";
            case NOTAVAIL: return "prescriber not available";
            case NOTPAT: return "patient no longer in this practice";
            case ONHOLD: return "medication on hold";
            case PRNA: return "product not available";
            case STOPMED: return "prescriber stopped medication for patient";
            case TOOEARLY: return "too early";
            case _SUPPLYORDERABORTREASONCODE: return "supply order abort reason";
            case IMPROV: return "condition improved";
            case INTOL: return "intolerance";
            case NEWSTR: return "new strength";
            case NEWTHER: return "new therapy";
            case _GENERICUPDATEREASONCODE: return "GenericUpdateReasonCode";
            case CHGDATA: return "information change";
            case FIXDATA: return "error correction";
            case MDATA: return "merge data";
            case NEWDATA: return "new information";
            case UMDATA: return "unmerge data";
            case _PATIENTPROFILEQUERYREASONCODE: return "patient profile query reason";
            case ADMREV: return "administrative review";
            case PATCAR: return "patient care";
            case PATREQ: return "patient request query";
            case PRCREV: return "practice review";
            case REGUL: return "regulatory review";
            case RSRCH: return "research";
            case VALIDATION: return "validation review";
            case _PHARMACYSUPPLYREQUESTFULFILLERREVISIONREFUSALREASONCODE: return "PharmacySupplyRequestFulfillerRevisionRefusalReasonCode";
            case LOCKED: return "locked";
            case UNKWNTARGET: return "unknown target";
            case _REFUSALREASONCODE: return "RefusalReasonCode";
            case _SCHEDULINGACTREASON: return "SchedulingActReason";
            case BLK: return "Unexpected Block (of Schedule)";
            case DEC: return "Patient Deceased";
            case FIN: return "No Financial Backing";
            case MED: return "Medical Status Altered";
            case MTG: return "In an outside meeting";
            case PHY: return "Physician request";
            case _STATUSREVISIONREFUSALREASONCODE: return "StatusRevisionRefusalReasonCode";
            case FILLED: return "fully filled";
            case _SUBSTANCEADMINISTRATIONPERMISSIONREFUSALREASONCODE: return "SubstanceAdministrationPermissionRefusalReasonCode";
            case PATINELIG: return "patient not eligible";
            case PROTUNMET: return "protocol not met";
            case PROVUNAUTH: return "provider not authorized";
            case _SUBSTANCEADMINSUBSTITUTIONNOTALLOWEDREASON: return "SubstanceAdminSubstitutionNotAllowedReason";
            case ALGINT: return "allergy intolerance";
            case COMPCON: return "compliance concern";
            case THERCHAR: return "therapeutic characteristics";
            case TRIAL: return "clinical trial drug";
            case _SUBSTANCEADMINSUBSTITUTIONREASON: return "SubstanceAdminSubstitutionReason";
            case CT: return "continuing therapy";
            case FP: return "formulary policy";
            case OS: return "out of stock";
            case RR: return "regulatory requirement";
            case _TRANSFERACTREASON: return "TransferActReason";
            case ER: return "Error";
            case RQ: return "Request";
            default: return "?";
          }
    }


}

