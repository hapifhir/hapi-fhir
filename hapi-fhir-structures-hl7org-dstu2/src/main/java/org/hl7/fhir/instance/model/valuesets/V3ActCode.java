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


public enum V3ActCode {

        /**
         * An account represents a grouping of financial transactions that are tracked and reported together with a single balance. 	 	Examples of account codes (types) are Patient billing accounts (collection of charges), Cost centers; Cash.
         */
        _ACTACCOUNTCODE, 
        /**
         * An account for collecting charges, reversals, adjustments and payments, including deductibles, copayments, coinsurance (financial transactions) credited or debited to the account receivable account for a patient's encounter.
         */
        ACCTRECEIVABLE, 
        /**
         * Cash
         */
        CASH, 
        /**
         * Description: Types of advance payment to be made on a plastic card usually issued by a financial institution used of purchasing services and/or products.
         */
        CC, 
        /**
         * American Express
         */
        AE, 
        /**
         * Diner's Club
         */
        DN, 
        /**
         * Discover Card
         */
        DV, 
        /**
         * Master Card
         */
        MC, 
        /**
         * Visa
         */
        V, 
        /**
         * An account representing charges and credits (financial transactions) for a patient's encounter.
         */
        PBILLACCT, 
        /**
         * Includes coded responses that will occur as a result of the adjudication of an electronic invoice at a summary level and provides guidance on interpretation of the referenced adjudication results.
         */
        _ACTADJUDICATIONCODE, 
        /**
         * Catagorization of grouping criteria for the associated transactions and/or summary (totals, subtotals).
         */
        _ACTADJUDICATIONGROUPCODE, 
        /**
         * Transaction counts and value totals by Contract Identifier.
         */
        CONT, 
        /**
         * Transaction counts and value totals for each calendar day within the date range specified.
         */
        DAY, 
        /**
         * Transaction counts and value totals by service location (e.g clinic).
         */
        LOC, 
        /**
         * Transaction counts and value totals for each calendar month within the date range specified.
         */
        MONTH, 
        /**
         * Transaction counts and value totals for the date range specified.
         */
        PERIOD, 
        /**
         * Transaction counts and value totals by Provider Identifier.
         */
        PROV, 
        /**
         * Transaction counts and value totals for each calendar week within the date range specified.
         */
        WEEK, 
        /**
         * Transaction counts and value totals for each calendar year within the date range specified.
         */
        YEAR, 
        /**
         * The invoice element has been accepted for payment but one or more adjustment(s) have been made to one or more invoice element line items (component charges).  

                        Also includes the concept 'Adjudicate as zero' and items not covered under a particular Policy.  

                        Invoice element can be reversed (nullified).  

                        Recommend that the invoice element is saved for DUR (Drug Utilization Reporting).
         */
        AA, 
        /**
         * The invoice element has been accepted for payment but one or more adjustment(s) have been made to one or more invoice element line items (component charges) without changing the amount.  

                        Invoice element can be reversed (nullified).  

                        Recommend that the invoice element is saved for DUR (Drug Utilization Reporting).
         */
        ANF, 
        /**
         * The invoice element has passed through the adjudication process but payment is refused due to one or more reasons.

                        Includes items such as patient not covered, or invoice element is not constructed according to payer rules (e.g. 'invoice submitted too late').

                        If one invoice element line item in the invoice element structure is rejected, the remaining line items may not be adjudicated and the complete group is treated as rejected.

                        A refused invoice element can be forwarded to the next payer (for Coordination of Benefits) or modified and resubmitted to refusing payer.

                        Invoice element cannot be reversed (nullified) as there is nothing to reverse.  

                        Recommend that the invoice element is not saved for DUR (Drug Utilization Reporting).
         */
        AR, 
        /**
         * The invoice element was/will be paid exactly as submitted, without financial adjustment(s).

                        If the dollar amount stays the same, but the billing codes have been amended or financial adjustments have been applied through the adjudication process, the invoice element is treated as "Adjudicated with Adjustment".

                        If information items are included in the adjudication results that do not affect the monetary amounts paid, then this is still Adjudicated as Submitted (e.g. 'reached Plan Maximum on this Claim').  

                        Invoice element can be reversed (nullified).  

                        Recommend that the invoice element is saved for DUR (Drug Utilization Reporting).
         */
        AS, 
        /**
         * Actions to be carried out by the recipient of the Adjudication Result information.
         */
        _ACTADJUDICATIONRESULTACTIONCODE, 
        /**
         * The adjudication result associated is to be displayed to the receiver of the adjudication result.
         */
        DISPLAY, 
        /**
         * The adjudication result associated is to be printed on the specified form, which is then provided to the covered party.
         */
        FORM, 
        /**
         * Definition:An identifying modifier code for healthcare interventions or procedures.
         */
        _ACTBILLABLEMODIFIERCODE, 
        /**
         * Description:CPT modifier codes are found in Appendix A of CPT 2000 Standard Edition.
         */
        CPTM, 
        /**
         * Description:HCPCS Level II (HCFA-assigned) and Carrier-assigned (Level III) modifiers are reported in Appendix A of CPT 2000 Standard Edition and in the Medicare Bulletin.
         */
        HCPCSA, 
        /**
         * The type of provision(s)  made for reimbursing for the deliver of healthcare services and/or goods provided by a Provider, over a specified period.
         */
        _ACTBILLINGARRANGEMENTCODE, 
        /**
         * A billing arrangement where a Provider charges a lump sum to provide a prescribed group (volume) of services to a single patient which occur over a period of time.  Services included in the block may vary.  

                        This billing arrangement is also known as Program of Care for some specific Payors and Program Fees for other Payors.
         */
        BLK, 
        /**
         * A billing arrangement where the payment made to a Provider is determined by analyzing one or more demographic attributes about the persons/patients who are enrolled with the Provider (in their practice).
         */
        CAP, 
        /**
         * A billing arrangement where a Provider charges a lump sum to provide a particular volume of one or more interventions/procedures or groups of interventions/procedures.
         */
        CONTF, 
        /**
         * A billing arrangement where a Provider charges for non-clinical items.  This includes interest in arrears, mileage, etc.  Clinical content is not 	included in Invoices submitted with this type of billing arrangement.
         */
        FINBILL, 
        /**
         * A billing arrangement where funding is based on a list of individuals registered as patients of the Provider.
         */
        ROST, 
        /**
         * A billing arrangement where a Provider charges a sum to provide a group (volume) of interventions/procedures to one or more patients within a defined period of time, typically on the same date.  Interventions/procedures included in the session may vary.
         */
        SESS, 
        /**
         * Type of bounded ROI.
         */
        _ACTBOUNDEDROICODE, 
        /**
         * A fully specified bounded Region of Interest (ROI) delineates a ROI in which only those dimensions participate that are specified by boundary criteria, whereas all other dimensions are excluded.  For example a ROI to mark an episode of "ST elevation" in a subset of the EKG leads V2, V3, and V4 would include 4 boundaries, one each for time, V2, V3, and V4.
         */
        ROIFS, 
        /**
         * A partially specified bounded Region of Interest (ROI) specifies a ROI in which at least all values in the dimensions specified by the boundary criteria participate. For example, if an episode of ventricular fibrillations (VFib) is observed, it usually doesn't make sense to exclude any EKG leads from the observation and the partially specified ROI would contain only one boundary for time indicating the time interval where VFib was observed.
         */
        ROIPS, 
        /**
         * Description:The type and scope of responsibility taken-on by the performer of the Act for a specific subject of care.
         */
        _ACTCAREPROVISIONCODE, 
        /**
         * Description:The type and scope of legal and/or professional responsibility taken-on by the performer of the Act for a specific subject of care as described by a credentialing agency, i.e. government or non-government agency. Failure in executing this Act may result in loss of credential to the person or organization who participates as performer of the Act. Excludes employment agreements.

                        
                           Example:Hospital license; physician license; clinic accreditation.
         */
        _ACTCREDENTIALEDCARECODE, 
        /**
         * Description:The type and scope of legal and/or professional responsibility taken-on by the performer of the Act for a specific subject of care as described by an agency for credentialing individuals.
         */
        _ACTCREDENTIALEDCAREPROVISIONPERSONCODE, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CACC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CAIC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CAMC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CANC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CAPC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CBGC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CCCC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CCGC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CCPC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CCSC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CDEC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CDRC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CEMC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CFPC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CIMC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CMGC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board
         */
        CNEC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CNMC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CNQC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CNSC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        COGC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        COMC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        COPC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        COSC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        COTC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CPEC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CPGC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CPHC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CPRC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CPSC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CPYC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CROC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CRPC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CSUC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CTSC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CURC, 
        /**
         * Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.
         */
        CVSC, 
        /**
         * Description:Scope of responsibility taken-on for physician care of a patient as defined by a governmental licensing agency.
         */
        LGPC, 
        /**
         * Description:The type and scope of legal and/or professional responsibility taken-on by the performer of the Act for a specific subject of care as described by an agency for credentialing programs within organizations.
         */
        _ACTCREDENTIALEDCAREPROVISIONPROGRAMCODE, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.
         */
        AALC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.
         */
        AAMC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.
         */
        ABHC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.
         */
        ACAC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.
         */
        ACHC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.
         */
        AHOC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.
         */
        ALTC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.
         */
        AOSC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CACS, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CAMI, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CAST, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CBAR, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CCAD, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CCAR, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CDEP, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CDGD, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CDIA, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CEPI, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CFEL, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CHFC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CHRO, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CHYP, 
        /**
         * Description:.
         */
        CMIH, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CMSC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        COJR, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CONC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        COPD, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CORT, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CPAD, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CPND, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CPST, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CSDM, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CSIC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CSLD, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CSPT, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CTBU, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CVDC, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CWMA, 
        /**
         * Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.
         */
        CWOH, 
        /**
         * Domain provides codes that qualify the ActEncounterClass (ENC)
         */
        _ACTENCOUNTERCODE, 
        /**
         * A comprehensive term for health care provided in a healthcare facility (e.g. a practitioneraTMs office, clinic setting, or hospital) on a nonresident basis. The term ambulatory usually implies that the patient has come to the location and is not assigned to a bed. Sometimes referred to as an outpatient encounter.
         */
        AMB, 
        /**
         * A patient encounter that takes place at a dedicated healthcare service delivery location where the patient receives immediate evaluation and treatment, provided until the patient can be discharged or responsibility for the patient's care is transferred elsewhere (for example, the patient could be admitted as an inpatient or transferred to another facility.)
         */
        EMER, 
        /**
         * A patient encounter that takes place both outside a dedicated service delivery location and outside a patient's residence. Example locations might include an accident site and at a supermarket.
         */
        FLD, 
        /**
         * Healthcare encounter that takes place in the residence of the patient or a designee
         */
        HH, 
        /**
         * A patient encounter where a patient is admitted by a hospital or equivalent facility, assigned to a location where patients generally stay at least overnight and provided with room, board, and continuous nursing service.
         */
        IMP, 
        /**
         * An acute inpatient encounter.
         */
        ACUTE, 
        /**
         * Any category of inpatient encounter except 'acute'
         */
        NONAC, 
        /**
         * A patient encounter where patient is scheduled or planned to receive service delivery in the future, and the patient is given a pre-admission account number. When the patient comes back for subsequent service, the pre-admission encounter is selected and is encapsulated into the service registration, and a new account number is generated.

                        
                           Usage Note: This is intended to be used in advance of encounter types such as ambulatory, inpatient encounter, virtual, etc.
         */
        PRENC, 
        /**
         * An encounter where the patient is admitted to a health care facility for a predetermined length of time, usually less than 24 hours.
         */
        SS, 
        /**
         * A patient encounter where the patient and the practitioner(s) are not in the same physical location. Examples include telephone conference, email exchange, robotic surgery, and televideo conference.
         */
        VR, 
        /**
         * General category of medical service provided to the patient during their encounter.
         */
        _ACTMEDICALSERVICECODE, 
        /**
         * Provision of Alternate Level of Care to a patient in an acute bed.  Patient is waiting for placement in a long-term care facility and is unable to return home.
         */
        ALC, 
        /**
         * Provision of diagnosis and treatment of diseases and disorders affecting the heart
         */
        CARD, 
        /**
         * Provision of recurring care for chronic illness.
         */
        CHR, 
        /**
         * Provision of treatment for oral health and/or dental surgery.
         */
        DNTL, 
        /**
         * Provision of treatment for drug abuse.
         */
        DRGRHB, 
        /**
         * General care performed by a general practitioner or family doctor as a responsible provider for a patient.
         */
        GENRL, 
        /**
         * Provision of diagnostic and/or therapeutic treatment.
         */
        MED, 
        /**
         * Provision of care of women during pregnancy, childbirth and immediate postpartum period.  Also known as Maternity.
         */
        OBS, 
        /**
         * Provision of treatment and/or diagnosis related to tumors and/or cancer.
         */
        ONC, 
        /**
         * Provision of care for patients who are living or dying from an advanced illness.
         */
        PALL, 
        /**
         * Provision of diagnosis and treatment of diseases and disorders affecting children.
         */
        PED, 
        /**
         * Pharmaceutical care performed by a pharmacist.
         */
        PHAR, 
        /**
         * Provision of treatment for physical injury.
         */
        PHYRHB, 
        /**
         * Provision of treatment of psychiatric disorder relating to mental illness.
         */
        PSYCH, 
        /**
         * Provision of surgical treatment.
         */
        SURG, 
        /**
         * Description: Coded types of attachments included to support a healthcare claim.
         */
        _ACTCLAIMATTACHMENTCATEGORYCODE, 
        /**
         * Description: Automobile Information Attachment
         */
        AUTOATTCH, 
        /**
         * Description: Document Attachment
         */
        DOCUMENT, 
        /**
         * Description: Health Record Attachment
         */
        HEALTHREC, 
        /**
         * Description: Image Attachment
         */
        IMG, 
        /**
         * Description: Lab Results Attachment
         */
        LABRESULTS, 
        /**
         * Description: Digital Model Attachment
         */
        MODEL, 
        /**
         * Description: Work Injury related additional Information Attachment
         */
        WIATTCH, 
        /**
         * Description: Digital X-Ray Attachment
         */
        XRAY, 
        /**
         * Definition: The type of consent directive, e.g., to consent or dissent to collect, access, or use in specific ways within an EHRS or for health information exchange; or to disclose  health information  for purposes such as research.
         */
        _ACTCONSENTTYPE, 
        /**
         * Definition: Consent to have healthcare information collected in an electronic health record.  This entails that the information may be used in analysis, modified, updated.
         */
        ICOL, 
        /**
         * Definition: Consent to have collected healthcare information disclosed.
         */
        IDSCL, 
        /**
         * Definition: Consent to access healthcare information.
         */
        INFA, 
        /**
         * Definition: Consent to access or "read" only, which entails that the information is not to be copied, screen printed, saved, emailed, stored, re-disclosed or altered in any way.  This level ensures that data which is masked or to which access is restricted will not be.

                        
                           Example: Opened and then emailed or screen printed for use outside of the consent directive purpose.
         */
        INFAO, 
        /**
         * Definition: Consent to access and save only, which entails that access to the saved copy will remain locked.
         */
        INFASO, 
        /**
         * Definition: Information re-disclosed without the patient's consent.
         */
        IRDSCL, 
        /**
         * Definition: Consent to have healthcare information in an electronic health record accessed for research purposes.
         */
        RESEARCH, 
        /**
         * Definition: Consent to have de-identified healthcare information in an electronic health record that is accessed for research purposes, but without consent to re-identify the information under any circumstance.
         */
        RSDID, 
        /**
         * Definition: Consent to have de-identified healthcare information in an electronic health record that is accessed for research purposes re-identified under specific circumstances outlined in the consent.

                        
                           Example:: Where there is a need to inform the subject of potential health issues.
         */
        RSREID, 
        /**
         * Constrains the ActCode to the domain of Container Registration
         */
        _ACTCONTAINERREGISTRATIONCODE, 
        /**
         * Used by one system to inform another that it has received a container.
         */
        ID, 
        /**
         * Used by one system to inform another that the container is in position for specimen transfer (e.g., container removal from track, pipetting, etc.).
         */
        IP, 
        /**
         * Used by one system to inform another that the container has been released from that system.
         */
        L, 
        /**
         * Used by one system to inform another that the container did not arrive at its next expected location.
         */
        M, 
        /**
         * Used by one system to inform another that the specific container is being processed by the equipment. It is useful as a response to a query about Container Status, when the specific step of the process is not relevant.
         */
        O, 
        /**
         * Status is used by one system to inform another that the processing has been completed, but the container has not been released from that system.
         */
        R, 
        /**
         * Used by one system to inform another that the container is no longer available within the scope of the system (e.g., tube broken or discarded).
         */
        X, 
        /**
         * An observation form that determines parameters or attributes of an Act. Examples are the settings of a ventilator machine as parameters of a ventilator treatment act; the controls on dillution factors of a chemical analyzer as a parameter of a laboratory observation act; the settings of a physiologic measurement assembly (e.g., time skew) or the position of the body while measuring blood pressure.

                        Control variables are forms of observations because just as with clinical observations, the Observation.code determines the parameter and the Observation.value assigns the value. While control variables sometimes can be observed (by noting the control settings or an actually measured feedback loop) they are not primary observations, in the sense that a control variable without a primary act is of no use (e.g., it makes no sense to record a blood pressure position without recording a blood pressure, whereas it does make sense to record a systolic blood pressure without a diastolic blood pressure).
         */
        _ACTCONTROLVARIABLE, 
        /**
         * Specifies whether or not automatic repeat testing is to be initiated on specimens.
         */
        AUTO, 
        /**
         * A baseline value for the measured test that is inherently contained in the diluent.  In the calculation of the actual result for the measured test, this baseline value is normally considered.
         */
        ENDC, 
        /**
         * Specifies whether or not further testing may be automatically or manually initiated on specimens.
         */
        REFLEX, 
        /**
         * Response to an insurance coverage eligibility query or authorization request.
         */
        _ACTCOVERAGECONFIRMATIONCODE, 
        /**
         * Indication of authorization for healthcare service(s) and/or product(s).  If authorization is approved, funds are set aside.
         */
        _ACTCOVERAGEAUTHORIZATIONCONFIRMATIONCODE, 
        /**
         * Authorization approved and funds have been set aside to pay for specified healthcare service(s) and/or product(s) within defined criteria for the authorization.
         */
        AUTH, 
        /**
         * Authorization for specified healthcare service(s) and/or product(s) denied.
         */
        NAUTH, 
        /**
         * Criteria that are applicable to the authorized coverage.
         */
        _ACTCOVERAGELIMITCODE, 
        /**
         * Maximum amount paid or maximum number of services/products covered; or maximum amount or number covered during a specified time period under the policy or program.
         */
        _ACTCOVERAGEQUANTITYLIMITCODE, 
        /**
         * Codes representing the time period during which coverage is available; or financial participation requirements are in effect.
         */
        COVPRD, 
        /**
         * Definition: Maximum amount paid by payer or covered party; or maximum number of services or products covered under the policy or program during a covered party's lifetime.
         */
        LFEMX, 
        /**
         * Maximum net amount that will be covered for the product or service specified.
         */
        NETAMT, 
        /**
         * Definition: Maximum amount paid by payer or covered party; or maximum number of services/products covered under the policy or program by time period specified by the effective time on the act.
         */
        PRDMX, 
        /**
         * Maximum unit price that will be covered for the authorized product or service.
         */
        UNITPRICE, 
        /**
         * Maximum number of items that will be covered of the product or service specified.
         */
        UNITQTY, 
        /**
         * Definition: Codes representing the maximum coverate or financial participation requirements.
         */
        COVMX, 
        /**
         * Definition: Set of codes indicating the type of insurance policy or program that pays for the cost of benefits provided to covered parties.
         */
        _ACTCOVERAGETYPECODE, 
        /**
         * Set of codes indicating the type of insurance policy or other source of funds to cover healthcare costs.
         */
        _ACTINSURANCEPOLICYCODE, 
        /**
         * Private insurance policy that provides coverage in addition to other policies (e.g. in addition to a Public Healthcare insurance policy).
         */
        EHCPOL, 
        /**
         * Insurance policy that provides for an allotment of funds replenished on a periodic (e.g. annual) basis. The use of the funds under this policy is at the 	discretion of the covered party.
         */
        HSAPOL, 
        /**
         * Insurance policy for injuries sustained in an automobile accident.  Will also typically covered non-named parties to the policy, such as pedestrians 	and passengers.
         */
        AUTOPOL, 
        /**
         * Definition: An automobile insurance policy under which the insurance company will cover the cost of damages to an automobile owned by the named insured that are caused by accident or intentionally by another party.
         */
        COL, 
        /**
         * Definition: An automobile insurance policy under which the insurance company will indemnify a loss for which another motorist is liable if that motorist is unable to pay because he or she is uninsured.  Coverage under the policy applies to bodily injury damages only.  Injuries to the covered party caused by a hit-and-run driver are also covered.
         */
        UNINSMOT, 
        /**
         * Insurance policy funded by a public health system such as a provincial or national health plan.  Examples include BC MSP (British Columbia 	Medical Services Plan) OHIP (Ontario Health Insurance Plan), NHS (National Health Service).
         */
        PUBLICPOL, 
        /**
         * Definition: A public or government health program that administers and funds coverage for dental care to assist program eligible who meet financial and health status criteria.
         */
        DENTPRG, 
        /**
         * Definition: A public or government health program that administers and funds coverage for health and social services to assist program eligible who meet financial and health status criteria related to a particular disease.

                        
                           Example: Reproductive health, sexually transmitted disease, and end renal disease programs.
         */
        DISEASEPRG, 
        /**
         * Definition: A program that provides low-income, uninsured, and underserved women access to timely, high-quality screening and diagnostic services, to detect breast and cervical cancer at the earliest stages.

                        
                           Example: To improve women's access to screening for breast and cervical cancers, Congress passed the Breast and Cervical Cancer Mortality Prevention Act of 1990, which guided CDC in creating the National Breast and Cervical Cancer Early Detection Program (NBCCEDP), which  provides access to critical breast and cervical cancer screening services for underserved women in the United States.  An estimated 7 to 10% of U.S. women of screening age are eligible to receive NBCCEDP services. Federal guidelines establish an eligibility baseline to direct services to uninsured and underinsured women at or below 250% of federal poverty level; ages 18 to 64 for cervical screening; ages 40 to 64 for breast screening.
         */
        CANPRG, 
        /**
         * Definition: A public or government program that administers publicly funded coverage of kidney dialysis and kidney transplant services.

                        Example: In the U.S., the Medicare End-stage Renal Disease program (ESRD), the National Kidney Foundation (NKF) American Kidney Fund (AKF) The Organ Transplant Fund.
         */
        ENDRENAL, 
        /**
         * Definition: Government administered and funded HIV-AIDS program for beneficiaries meeting financial and health status criteria.  Administration, funding levels, eligibility criteria, covered benefits, provider types, and financial participation are typically set by a regulatory process.  Payer responsibilities for administering the program may be delegated to contractors.

                        
                           Example: In the U.S., the Ryan White program, which is administered by the Health Resources and Services Administration.
         */
        HIVAIDS, 
        /**
         * mandatory health program
         */
        MANDPOL, 
        /**
         * Definition: Government administered and funded mental health program for beneficiaries meeting financial and mental health status criteria.  Administration, funding levels, eligibility criteria, covered benefits, provider types, and financial participation are typically set by a regulatory process.  Payer responsibilities for administering the program may be delegated to contractors.

                        
                           Example: In the U.S., states receive funding for substance use programs from the Substance Abuse Mental Health Administration (SAMHSA).
         */
        MENTPRG, 
        /**
         * Definition: Government administered and funded program to support provision of care to underserved populations through safety net clinics.

                        
                           Example: In the U.S., safety net providers such as federally qualified health centers (FQHC) receive funding under PHSA Section 330 grants administered by the Health Resources and Services Administration.
         */
        SAFNET, 
        /**
         * Definition: Government administered and funded substance use program for beneficiaries meeting financial, substance use behavior, and health status criteria.  Beneficiaries may be required to enroll as a result of legal proceedings.  Administration, funding levels, eligibility criteria, covered benefits, provider types, and financial participation are typically set by a regulatory process.  Payer responsibilities for administering the program may be delegated to contractors.

                        
                           Example: In the U.S., states receive funding for substance use programs from the Substance Abuse Mental Health Administration (SAMHSA).
         */
        SUBPRG, 
        /**
         * Definition: A government health program that provides coverage for health services to persons meeting eligibility criteria such as income, location of residence, access to other coverages, health condition, and age, the cost of which is to some extent subsidized by public funds.
         */
        SUBSIDIZ, 
        /**
         * Definition: A government health program that provides coverage through managed care contracts for health services to persons meeting eligibility criteria such as income, location of residence, access to other coverages, health condition, and age, the cost of which is to some extent subsidized by public funds. 

                        
                           Discussion: The structure and business processes for underwriting and administering a subsidized managed care program is further specified by the Underwriter and Payer Role.class and Role.code.
         */
        SUBSIDMC, 
        /**
         * Definition: A government health program that provides coverage for health services to persons meeting eligibility criteria for a supplemental health policy or program such as income, location of residence, access to other coverages, health condition, and age, the cost of which is to some extent subsidized by public funds.

                        
                           Example:  Supplemental health coverage program may cover the cost of a health program or policy financial participations, such as the copays and the premiums, and may provide coverage for services in addition to those covered under the supplemented health program or policy.  In the U.S., Medicaid programs may pay the premium for a covered party who is also covered under the  Medicare program or a private health policy.

                        
                           Discussion: The structure and business processes for underwriting and administering a subsidized supplemental retiree health program is further specified by the Underwriter and Payer Role.class and Role.code.
         */
        SUBSUPP, 
        /**
         * Insurance policy for injuries sustained in the work place or in the course of employment.
         */
        WCBPOL, 
        /**
         * Definition: Set of codes indicating the type of insurance policy.  Insurance, in law and economics, is a form of risk management primarily used to hedge against the risk of potential financial loss. Insurance is defined as the equitable transfer of the risk of a potential loss, from one entity to another, in exchange for a premium and duty of care. A policy holder is an individual or an organization enters into a contract with an underwriter which stipulates that, in exchange for payment of a sum of money (a premium), one or more covered parties (insureds) is guaranteed compensation for losses resulting from certain perils under specified conditions.  The underwriter analyzes the risk of loss, makes a decision as to whether the risk is insurable, and prices the premium accordingly.  A policy provides benefits that indemnify or cover the cost of a loss incurred by a covered party, and may include coverage for services required to remediate a loss.  An insurance policy contains pertinent facts about the policy holder, the insurance coverage, the covered parties, and the insurer.  A policy may include exemptions and provisions specifying the extent to which the indemnification clause cannot be enforced for intentional tortious conduct of a covered party, e.g., whether the covered parties are jointly or severably insured.

                        
                           Discussion: In contrast to programs, an insurance policy has one or more policy holders, who own the policy.  The policy holder may be the covered party, a relative of the covered party, a partnership, or a corporation, e.g., an employer.  A subscriber of a self-insured health insurance policy is a policy holder.  A subscriber of an employer sponsored health insurance policy is holds a certificate of coverage, but is not a policy holder; the policy holder is the employer.  See CoveredRoleType.
         */
        _ACTINSURANCETYPECODE, 
        /**
         * Definition: Set of codes indicating the type of health insurance policy that covers health services provided to covered parties.  A health insurance policy is a written contract for insurance between the insurance company and the policyholder, and contains pertinent facts about the policy owner (the policy holder), the health insurance coverage, the insured subscribers and dependents, and the insurer.  Health insurance is typically administered in accordance with a plan, which specifies (1) the type of health services and health conditions that will be covered under what circumstances (e.g., exclusion of a pre-existing condition, service must be deemed medically necessary; service must not be experimental; service must provided in accordance with a protocol; drug must be on a formulary; service must be prior authorized; or be a referral from a primary care provider); (2) the type and affiliation of providers (e.g., only allopathic physicians, only in network, only providers employed by an HMO); (3) financial participations required of covered parties (e.g., co-pays, coinsurance, deductibles, out-of-pocket); and (4) the manner in which services will be paid (e.g., under indemnity or fee-for-service health plans, the covered party typically pays out-of-pocket and then file a claim for reimbursement, while health plans that have contractual relationships with providers, i.e., network providers, typically do not allow the providers to bill the covered party for the cost of the service until after filing a claim with the payer and receiving reimbursement).
         */
        _ACTHEALTHINSURANCETYPECODE, 
        /**
         * Definition: A health insurance policy that that covers benefits for dental services.
         */
        DENTAL, 
        /**
         * Definition: A health insurance policy that covers benefits for healthcare services provided for named conditions under the policy, e.g., cancer, diabetes, or HIV-AIDS.
         */
        DISEASE, 
        /**
         * Definition: A health insurance policy that covers benefits for prescription drugs, pharmaceuticals, and supplies.
         */
        DRUGPOL, 
        /**
         * Definition: A health insurance policy that covers healthcare benefits by protecting covered parties from medical expenses arising from health conditions, sickness, or accidental injury as well as preventive care. Health insurance policies explicitly exclude coverage for losses insured under a disability policy, workers' compensation program, liability insurance (including automobile insurance); or for medical expenses, coverage for on-site medical clinics or for limited dental or vision benefits when these are provided under a separate policy.

                        
                           Discussion: Health insurance policies are offered by health insurance plans that typically reimburse providers for covered services on a fee-for-service basis, that is, a fee that is the allowable amount that a provider may charge.  This is in contrast to managed care plans, which typically prepay providers a per-member/per-month amount or capitation as reimbursement for all covered services rendered.  Health insurance plans include indemnity and healthcare services plans.
         */
        HIP, 
        /**
         * Definition: An insurance policy that covers benefits for long-term care services people need when they no longer can care for themselves. This may be due to an accident, disability, prolonged illness or the simple process of aging. Long-term care services assist with activities of daily living including:

                        
                           
                              Help at home with day-to-day activities, such as cooking, cleaning, bathing and dressing

                           
                           
                              Care in the community, such as in an adult day care facility

                           
                           
                              Supervised care provided in an assisted living facility

                           
                           
                              Skilled care provided in a nursing home
         */
        LTC, 
        /**
         * Definition: Government mandated program providing coverage, disability income, and vocational rehabilitation for injuries sustained in the work place or in the course of employment.  Employers may either self-fund the program, purchase commercial coverage, or pay a premium to a government entity that administers the program.  Employees may be required to pay premiums toward the cost of coverage as well.

                        Managed care policies specifically exclude coverage for losses insured under a disability policy, workers' compensation program, liability insurance (including automobile insurance); or for medical expenses, coverage for on-site medical clinics or for limited dental or vision benefits when these are provided under a separate policy.

                        
                           Discussion: Managed care policies are offered by managed care plans that contract with selected providers or health care organizations to provide comprehensive health care at a discount to covered parties and coordinate the financing and delivery of health care. Managed care uses medical protocols and procedures agreed on by the medical profession to be cost effective, also known as medical practice guidelines. Providers are typically reimbursed for covered services by a capitated amount on a per member per month basis that may reflect difference in the health status and level of services anticipated to be needed by the member.
         */
        MCPOL, 
        /**
         * Definition: A policy for a health plan that has features of both an HMO and a FFS plan.  Like an HMO, a POS plan encourages the use its HMO network to maintain discounted fees with participating providers, but recognizes that sometimes covered parties want to choose their own provider.  The POS plan allows a covered party to use providers who are not part of the HMO network (non-participating providers).  However, there is a greater cost associated with choosing these non-network providers. A covered party will usually pay deductibles and coinsurances that are substantially higher than the payments when he or she uses a plan provider. Use of non-participating providers often requires the covered party to pay the provider directly and then to file a claim for reimbursement, like in an FFS plan.
         */
        POS, 
        /**
         * Definition: A policy for a health plan that provides coverage for health care only through contracted or employed physicians and hospitals located in particular geographic or service areas.  HMOs emphasize prevention and early detection of illness. Eligibility to enroll in an HMO is determined by where a covered party lives or works.
         */
        HMO, 
        /**
         * Definition: A network-based, managed care plan that allows a covered party to choose any health care provider. However, if care is received from a "preferred" (participating in-network) provider, there are generally higher benefit coverage and lower deductibles.
         */
        PPO, 
        /**
         * Definition: A health insurance policy that covers benefits for mental health services and prescriptions.
         */
        MENTPOL, 
        /**
         * Definition: A health insurance policy that covers benefits for substance use services.
         */
        SUBPOL, 
        /**
         * Definition: Set of codes for a policy that provides coverage for health care expenses arising from vision services.

                        A health insurance policy that covers benefits for vision care services, prescriptions, and products.
         */
        VISPOL, 
        /**
         * Definition: An insurance policy that provides a regular payment to compensate for income lost due to the covered party's inability to work because of illness or injury.
         */
        DIS, 
        /**
         * Definition: An insurance policy under a benefit plan run by an employer or employee organization for the purpose of providing benefits other than pension-related to employees and their families. Typically provides health-related benefits, benefits for disability, disease or unemployment, or day care and scholarship benefits, among others.  An employer sponsored health policy includes coverage of health care expenses arising from sickness or accidental injury, coverage for on-site medical clinics or for dental or vision benefits, which are typically provided under a separate policy.  Coverage excludes health care expenses covered by accident or disability, workers' compensation, liability or automobile insurance.
         */
        EWB, 
        /**
         * Definition:  An insurance policy that covers qualified benefits under a Flexible Benefit plan such as group medical insurance, long and short term disability income insurance, group term life insurance for employees only up to $50,000 face amount, specified disease coverage such as a cancer policy, dental and/or vision insurance, hospital indemnity insurance, accidental death and dismemberment insurance, a medical expense reimbursement plan and a dependent care reimbursement plan.

                        
                            Discussion: See UnderwriterRoleTypeCode flexible benefit plan which is defined as a benefit plan that allows employees to choose from several life, health, disability, dental, and other insurance plans according to their individual needs. Also known as cafeteria plans.  Authorized under Section 125 of the Revenue Act of 1978.
         */
        FLEXP, 
        /**
         * Definition: A policy under which the insurer agrees to pay a sum of money upon the occurrence of the covered partys death. In return, the policyholder agrees to pay a stipulated amount called a premium at regular intervals.  Life insurance indemnifies the beneficiary for the loss of the insurable interest that a beneficiary has in the life of a covered party.  For persons related by blood, a substantial interest established through love and affection, and for all other persons, a lawful and substantial economic interest in having the life of the insured continue. An insurable interest is required when purchasing life insurance on another person. Specific exclusions are often written into the contract to limit the liability of the insurer; for example claims resulting from suicide or relating to war, riot and civil commotion.

                        
                           Discussion:A life insurance policy may be used by the covered party as a source of health care coverage in the case of  a viatical settlement, which is the sale of a life insurance policy by the policy owner, before the policy matures. Such a sale, at a price discounted from the face amount of the policy but usually in excess of the premiums paid or current cash surrender value, provides the seller an immediate cash settlement. Generally, viatical settlements involve insured individuals with a life expectancy of less than two years. In countries without state-subsidized healthcare and high healthcare costs (e.g. United States), this is a practical way to pay extremely high health insurance premiums that severely ill people face. Some people are also familiar with life settlements, which are similar transactions but involve insureds with longer life expectancies (two to fifteen years).
         */
        LIFE, 
        /**
         * Definition: A policy that, after an initial premium or premiums, pays out a sum at pre-determined intervals.

                        For example, a policy holder may pay $10,000, and in return receive $150 each month until he dies; or $1,000 for each of 14 years or death benefits if he dies before the full term of the annuity has elapsed.
         */
        ANNU, 
        /**
         * Definition: Life insurance under which the benefit is payable only if the insured dies during a specified period. If an insured dies during that period, the beneficiary receives the death payments. If the insured survives, the policy ends and the beneficiary receives nothing.
         */
        TLIFE, 
        /**
         * Definition: Life insurance under which the benefit is payable upon the insuredaTMs death or diagnosis of a terminal illness.  If an insured dies during that period, the beneficiary receives the death payments. If the insured survives, the policy ends and the beneficiary receives nothing
         */
        ULIFE, 
        /**
         * Definition: A type of insurance that covers damage to or loss of the policyholderaTMs property by providing payments for damages to property damage or the injury or death of living subjects.  The terms "casualty" and "liability" insurance are often used interchangeably. Both cover the policyholder's legal liability for damages caused to other persons and/or their property.
         */
        PNC, 
        /**
         * Definition: An agreement between two or more insurance companies by which the risk of loss is proportioned. Thus the risk of loss is spread and a disproportionately large loss under a single policy does not fall on one insurance company. Acceptance by an insurer, called a reinsurer, of all or part of the risk of loss of another insurance company.

                        
                           Discussion: Reinsurance is a means by which an insurance company can protect itself against the risk of losses with other insurance companies. Individuals and corporations obtain insurance policies to provide protection for various risks (hurricanes, earthquakes, lawsuits, collisions, sickness and death, etc.). Reinsurers, in turn, provide insurance to insurance companies.

                        For example, an HMO may purchase a reinsurance policy to protect itself from losing too much money from one insured's particularly expensive health care costs. An insurance company issuing an automobile liability policy, with a limit of $100,000 per accident may reinsure its liability in excess of $10,000. A fire insurance company which issues a large policy generally reinsures a portion of the risk with one or several other companies. Also called risk control insurance or stop-loss insurance.
         */
        REI, 
        /**
         * Definition: 
                        

                        
                           
                              A risk or part of a risk for which there is no normal insurance market available.

                           
                           
                              Insurance written by unauthorized insurance companies. Surplus lines insurance is insurance placed with unauthorized insurance companies through licensed surplus lines agents or brokers.
         */
        SURPL, 
        /**
         * Definition: A form of insurance protection that provides additional liability coverage after the limits of your underlying policy are reached. An umbrella liability policy also protects you (the insured) in many situations not covered by the usual liability policies.
         */
        UMBRL, 
        /**
         * Definition: A set of codes used to indicate coverage under a program.  A program is an organized structure for administering and funding coverage of a benefit package for covered parties meeting eligibility criteria, typically related to employment, health, financial, and demographic status. Programs are typically established or permitted by legislation with provisions for ongoing government oversight.  Regulations may mandate the structure of the program, the manner in which it is funded and administered, covered benefits, provider types, eligibility criteria and financial participation. A government agency may be charged with implementing the program in accordance to the regulation.  Risk of loss under a program in most cases would not meet what an underwriter would consider an insurable risk, i.e., the risk is not random in nature, not financially measurable, and likely requires subsidization with government funds.

                        
                           Discussion: Programs do not have policy holders or subscribers.  Program eligibles are enrolled based on health status, statutory eligibility, financial status, or age.  Program eligibles who are covered parties under the program may be referred to as members, beneficiaries, eligibles, or recipients.  Programs risk are underwritten by not for profit organizations such as governmental entities, and the beneficiaries typically do not pay for any or some portion of the cost of coverage.  See CoveredPartyRoleType.
         */
        _ACTPROGRAMTYPECODE, 
        /**
         * Definition: A program that covers the cost of services provided directly to a beneficiary who typically has no other source of coverage without charge.
         */
        CHAR, 
        /**
         * Definition: A program that covers the cost of services provided to crime victims for injuries or losses related to the occurrence of a crime.
         */
        CRIME, 
        /**
         * Definition: An employee assistance program is run by an employer or employee organization for the purpose of providing benefits and covering all or part of the cost for employees to receive counseling, referrals, and advice in dealing with stressful issues in their lives. These may include substance abuse, bereavement, marital problems, weight issues, or general wellness issues.  The services are usually provided by a third-party, rather than the company itself, and the company receives only summary statistical data from the service provider. Employee's names and services received are kept confidential.
         */
        EAP, 
        /**
         * Definition: A set of codes used to indicate a government program that is an organized structure for administering and funding coverage of a benefit package for covered parties meeting eligibility criteria, typically related to employment, health and financial status. Government programs are established or permitted by legislation with provisions for ongoing government oversight.  Regulation mandates the structure of the program, the manner in which it is funded and administered, covered benefits, provider types, eligibility criteria and financial participation. A government agency is charged with implementing the program in accordance to the regulation

                        
                           Example: Federal employee health benefit program in the U.S.
         */
        GOVEMP, 
        /**
         * Definition: A government program that provides health coverage to individuals who are considered medically uninsurable or high risk, and who have been denied health insurance due to a serious health condition. In certain cases, it also applies to those who have been quoted very high premiums a" again, due to a serious health condition.  The pool charges premiums for coverage.  Because the pool covers high-risk people, it incurs a higher level of claims than premiums can cover. The insurance industry pays into the pool to make up the difference and help it remain viable.
         */
        HIRISK, 
        /**
         * Definition: Services provided directly and through contracted and operated indigenous peoples health programs.

                        
                           Example: Indian Health Service in the U.S.
         */
        IND, 
        /**
         * Definition: A government program that provides coverage for health services to military personnel, retirees, and dependents.  A covered party who is a subscriber can choose from among Fee-for-Service (FFS) plans, and their Preferred Provider Organizations (PPO), or Plans offering a Point of Service (POS) Product, or Health Maintenance Organizations.

                        
                           Example: In the U.S., TRICARE, CHAMPUS.
         */
        MILITARY, 
        /**
         * Definition: A government mandated program with specific eligibility requirements based on premium contributions made during employment, length of employment, age, and employment status, e.g., being retired, disabled, or a dependent of a covered party under this program.   Benefits typically include ambulatory, inpatient, and long-term care, such as hospice care, home health care and respite care.
         */
        RETIRE, 
        /**
         * Definition: A social service program funded by a public or governmental entity.

                        
                           Example: Programs providing habilitation, food, lodging, medicine, transportation, equipment, devices, products, education, training, counseling, alteration of living or work space, and other resources to persons meeting eligibility criteria.
         */
        SOCIAL, 
        /**
         * Definition: Services provided directly and through contracted and operated veteran health programs.
         */
        VET, 
        /**
         * Codes dealing with the management of Detected Issue observations
         */
        _ACTDETECTEDISSUEMANAGEMENTCODE, 
        /**
         * Codes dealing with the management of Detected Issue observations for the administrative and patient administrative acts domains.
         */
        _ACTADMINISTRATIVEDETECTEDISSUEMANAGEMENTCODE, 
        /**
         * Authorization Issue Management Code
         */
        _AUTHORIZATIONISSUEMANAGEMENTCODE, 
        /**
         * Used to temporarily override normal authorization rules to gain access to data in a case of emergency. Use of this override code will typically be monitored, and a procedure to verify its proper use may be triggered when used.
         */
        EMAUTH, 
        /**
         * Description: Indicates that the permissions have been externally verified and the request should be processed.
         */
        _21, 
        /**
         * Confirmed drug therapy appropriate
         */
        _1, 
        /**
         * Consulted other supplier/pharmacy, therapy confirmed
         */
        _19, 
        /**
         * Assessed patient, therapy is appropriate
         */
        _2, 
        /**
         * Description: The patient has the appropriate indication or diagnosis for the action to be taken.
         */
        _22, 
        /**
         * Description: It has been confirmed that the appropriate pre-requisite therapy has been tried.
         */
        _23, 
        /**
         * Patient gave adequate explanation
         */
        _3, 
        /**
         * Consulted other supply source, therapy still appropriate
         */
        _4, 
        /**
         * Consulted prescriber, therapy confirmed
         */
        _5, 
        /**
         * Consulted prescriber and recommended change, prescriber declined
         */
        _6, 
        /**
         * Concurrent therapy triggering alert is no longer on-going or planned
         */
        _7, 
        /**
         * Confirmed supply action appropriate
         */
        _14, 
        /**
         * Patient's existing supply was lost/wasted
         */
        _15, 
        /**
         * Supply date is due to patient vacation
         */
        _16, 
        /**
         * Supply date is intended to carry patient over weekend
         */
        _17, 
        /**
         * Supply is intended for use during a leave of absence from an institution.
         */
        _18, 
        /**
         * Description: Supply is different than expected as an additional quantity has been supplied in a separate dispense.
         */
        _20, 
        /**
         * Order is performed as issued, but other action taken to mitigate potential adverse effects
         */
        _8, 
        /**
         * Provided education or training to the patient on appropriate therapy use
         */
        _10, 
        /**
         * Instituted an additional therapy to mitigate potential negative effects
         */
        _11, 
        /**
         * Suspended existing therapy that triggered interaction for the duration of this therapy
         */
        _12, 
        /**
         * Aborted existing therapy that triggered interaction.
         */
        _13, 
        /**
         * Arranged to monitor patient for adverse effects
         */
        _9, 
        /**
         * Concepts that identify the type or nature of exposure interaction.  Examples include "household", "care giver", "intimate partner", "common space", "common substance", etc. to further describe the nature of interaction.
         */
        _ACTEXPOSURECODE, 
        /**
         * Description: Exposure participants' interaction occurred in a child care setting
         */
        CHLDCARE, 
        /**
         * Description: An interaction where the exposure participants traveled in/on the same vehicle (not necessarily concurrently, e.g. both are passengers of the same plane, but on different flights of that plane).
         */
        CONVEYNC, 
        /**
         * Description: Exposure participants' interaction occurred during the course of health care delivery or in a health care delivery setting, but did not involve the direct provision of care (e.g. a janitor cleaning a patient's hospital room).
         */
        HLTHCARE, 
        /**
         * Description: Exposure interaction occurred in context of one providing care for the other, i.e. a babysitter providing care for a child, a home-care aide providing assistance to a paraplegic.
         */
        HOMECARE, 
        /**
         * Description: Exposure participants' interaction occurred when both were patients being treated in the same (acute) health care delivery facility.
         */
        HOSPPTNT, 
        /**
         * Description: Exposure participants' interaction occurred when one visited the other who was a patient being treated in a health care delivery facility.
         */
        HOSPVSTR, 
        /**
         * Description: Exposure interaction occurred in context of domestic interaction, i.e. both participants reside in the same household.
         */
        HOUSEHLD, 
        /**
         * Description: Exposure participants' interaction occurred in the course of one or both participants being incarcerated at a correctional facility
         */
        INMATE, 
        /**
         * Description: Exposure interaction was intimate, i.e. participants are intimate companions (e.g. spouses, domestic partners).
         */
        INTIMATE, 
        /**
         * Description: Exposure participants' interaction occurred in the course of one or both participants being resident at a long term care facility (second participant may be a visitor, worker, resident or a physical place or object within the facility).
         */
        LTRMCARE, 
        /**
         * Description: An interaction where the exposure participants were both present in the same location/place/space.
         */
        PLACE, 
        /**
         * Description: Exposure participants' interaction occurred during the course of  health care delivery by a provider (e.g. a physician treating a patient in her office).
         */
        PTNTCARE, 
        /**
         * Description: Exposure participants' interaction occurred in an academic setting (e.g., participants are fellow students, or student and teacher).
         */
        SCHOOL2, 
        /**
         * Description: An interaction where the exposure participants are social associates or members of the same extended family
         */
        SOCIAL2, 
        /**
         * Description: An interaction where the exposure participants shared or co-used a common substance (e.g. drugs, needles, or common food item).
         */
        SUBSTNCE, 
        /**
         * Description: An interaction where the exposure participants traveled together in/on the same vehicle/trip (e.g. concurrent co-passengers).
         */
        TRAVINT, 
        /**
         * Description: Exposure interaction occurred in a work setting, i.e. participants are co-workers.
         */
        WORK2, 
        /**
         * ActFinancialTransactionCode
         */
        _ACTFINANCIALTRANSACTIONCODE, 
        /**
         * A type of transaction that represents a charge for a service or product.  Expressed in monetary terms.
         */
        CHRG, 
        /**
         * A type of transaction that represents a reversal of a previous charge for a service or product. Expressed in monetary terms.  It has the opposite effect of a standard charge.
         */
        REV, 
        /**
         * Set of codes indicating the type of incident or accident.
         */
        _ACTINCIDENTCODE, 
        /**
         * Incident or accident as the result of a motor vehicle accident
         */
        MVA, 
        /**
         * Incident or accident is the result of a school place accident.
         */
        SCHOOL, 
        /**
         * Incident or accident is the result of a sporting accident.
         */
        SPT, 
        /**
         * Incident or accident is the result of a work place accident
         */
        WPA, 
        /**
         * Description: The type of health information to which the subject of the information or the subject's delegate consents or dissents.
         */
        _ACTINFORMATIONACCESSCODE, 
        /**
         * Description: Provide consent to collect, use, disclose, or access adverse drug reaction information for a patient.
         */
        ACADR, 
        /**
         * Description: Provide consent to collect, use, disclose, or access all information for a patient.
         */
        ACALL, 
        /**
         * Description: Provide consent to collect, use, disclose, or access allergy information for a patient.
         */
        ACALLG, 
        /**
         * Description: Provide consent to collect, use, disclose, or access informational consent information for a patient.
         */
        ACCONS, 
        /**
         * Description: Provide consent to collect, use, disclose, or access demographics information for a patient.
         */
        ACDEMO, 
        /**
         * Description: Provide consent to collect, use, disclose, or access diagnostic imaging information for a patient.
         */
        ACDI, 
        /**
         * Description: Provide consent to collect, use, disclose, or access immunization information for a patient.
         */
        ACIMMUN, 
        /**
         * Description: Provide consent to collect, use, disclose, or access lab test result information for a patient.
         */
        ACLAB, 
        /**
         * Description: Provide consent to collect, use, disclose, or access medical condition information for a patient.
         */
        ACMED, 
        /**
         * Definition: Provide consent to view or access medical condition information for a patient.
         */
        ACMEDC, 
        /**
         * Description:Provide consent to collect, use, disclose, or access mental health information for a patient.
         */
        ACMEN, 
        /**
         * Description: Provide consent to collect, use, disclose, or access common observation information for a patient.
         */
        ACOBS, 
        /**
         * Description: Provide consent to collect, use, disclose, or access coverage policy or program for a patient.
         */
        ACPOLPRG, 
        /**
         * Description: Provide consent to collect, use, disclose, or access provider information for a patient.
         */
        ACPROV, 
        /**
         * Description: Provide consent to collect, use, disclose, or access professional service information for a patient.
         */
        ACPSERV, 
        /**
         * Description:Provide consent to collect, use, disclose, or access substance abuse information for a patient.
         */
        ACSUBSTAB, 
        /**
         * Concepts conveying the context in which consent to transfer specified patient health information for collection, access, use or disclosure applies.
         */
        _ACTINFORMATIONACCESSCONTEXTCODE, 
        /**
         * Description: Information transfer in accordance with subjectaTMs consent directive.
         */
        INFAUT, 
        /**
         * Consent to collect, access, use, or disclose specified patient health information only after explicit consent.
         */
        INFCON, 
        /**
         * Description: Information transfer in accordance with judicial system protocol.
         */
        INFCRT, 
        /**
         * Consent to collect, access, use, or disclose specified patient health information only if necessary to avert potential danger to other persons.
         */
        INFDNG, 
        /**
         * Description: Information transfer in accordance with emergency information transfer protocol.
         */
        INFEMER, 
        /**
         * Consent to collect, access, use, or disclose specified patient health information only if necessary to avert potential public welfare risk.
         */
        INFPWR, 
        /**
         * Description: Information transfer in accordance with regulatory protocol, e.g., for public health, welfare, and safety.
         */
        INFREG, 
        /**
         * Definition:Indicates the set of information types which may be manipulated or referenced, such as for recommending access restrictions.
         */
        _ACTINFORMATIONCATEGORYCODE, 
        /**
         * Description: All patient information.
         */
        ALLCAT, 
        /**
         * Definition:All information pertaining to a patient's allergy and intolerance records.
         */
        ALLGCAT, 
        /**
         * Description: All information pertaining to a patient's adverse drug reactions.
         */
        ARCAT, 
        /**
         * Definition:All information pertaining to a patient's common observation records (height, weight, blood pressure, temperature, etc.).
         */
        COBSCAT, 
        /**
         * Definition:All information pertaining to a patient's demographics (such as name, date of birth, gender, address, etc).
         */
        DEMOCAT, 
        /**
         * Definition:All information pertaining to a patient's diagnostic image records (orders & results).
         */
        DICAT, 
        /**
         * Definition:All information pertaining to a patient's vaccination records.
         */
        IMMUCAT, 
        /**
         * Description: All information pertaining to a patient's lab test records (orders & results)
         */
        LABCAT, 
        /**
         * Definition:All information pertaining to a patient's medical condition records.
         */
        MEDCCAT, 
        /**
         * Description: All information pertaining to a patient's mental health records.
         */
        MENCAT, 
        /**
         * Definition:All information pertaining to a patient's professional service records (such as smoking cessation, counseling, medication review, mental health).
         */
        PSVCCAT, 
        /**
         * Definition:All information pertaining to a patient's medication records (orders, dispenses and other active medications).
         */
        RXCAT, 
        /**
         * Type of invoice element that is used to assist in describing an Invoice that is either submitted for adjudication or for which is returned on adjudication results.
         */
        _ACTINVOICEELEMENTCODE, 
        /**
         * Codes representing a grouping of invoice elements (totals, sub-totals), reported through a Payment Advice or a Statement of Financial Activity (SOFA).  The code can represent summaries by day, location, payee and other cost elements such as bonus, retroactive adjustment and transaction fees.
         */
        _ACTINVOICEADJUDICATIONPAYMENTCODE, 
        /**
         * Codes representing adjustments to a Payment Advice such as retroactive, clawback, garnishee, etc.
         */
        _ACTINVOICEADJUDICATIONPAYMENTGROUPCODE, 
        /**
         * Payment initiated by the payor as the result of adjudicating a submitted invoice that arrived to the payor from an electronic source that did not provide a conformant set of HL7 messages (e.g. web claim submission).
         */
        ALEC, 
        /**
         * Bonus payments based on performance, volume, etc. as agreed to by the payor.
         */
        BONUS, 
        /**
         * An amount still owing to the payor but the payment is 0$ and this cannot be settled until a future payment is made.
         */
        CFWD, 
        /**
         * Fees deducted on behalf of a payee for tuition and continuing education.
         */
        EDU, 
        /**
         * Fees deducted on behalf of a payee for charges based on a shorter payment frequency (i.e. next day versus biweekly payments.
         */
        EPYMT, 
        /**
         * Fees deducted on behalf of a payee for charges based on a per-transaction or time-period (e.g. monthly) fee.
         */
        GARN, 
        /**
         * Payment is based on a payment intent for a previously submitted Invoice, based on formal adjudication results..
         */
        INVOICE, 
        /**
         * Payment initiated by the payor as the result of adjudicating a paper (original, may have been faxed) invoice.
         */
        PINV, 
        /**
         * An amount that was owed to the payor as indicated, by a carry forward adjusment, in a previous payment advice
         */
        PPRD, 
        /**
         * Professional association fee that is collected by the payor from the practitioner/provider on behalf of the association
         */
        PROA, 
        /**
         * Retroactive adjustment such as fee rate adjustment due to contract negotiations.
         */
        RECOV, 
        /**
         * Bonus payments based on performance, volume, etc. as agreed to by the payor.
         */
        RETRO, 
        /**
         * Fees deducted on behalf of a payee for charges based on a per-transaction or time-period (e.g. monthly) fee.
         */
        TRAN, 
        /**
         * Codes representing a grouping of invoice elements (totals, sub-totals), reported through a Payment Advice or a Statement of Financial Activity (SOFA).  The code can represent summaries by day, location, payee, etc.
         */
        _ACTINVOICEADJUDICATIONPAYMENTSUMMARYCODE, 
        /**
         * Transaction counts and value totals by invoice type (e.g. RXDINV - Pharmacy Dispense)
         */
        INVTYPE, 
        /**
         * Transaction counts and value totals by each instance of an invoice payee.
         */
        PAYEE, 
        /**
         * Transaction counts and value totals by each instance of an invoice payor.
         */
        PAYOR, 
        /**
         * Transaction counts and value totals by each instance of a messaging application on a single processor. It is a registered identifier known to the receivers.
         */
        SENDAPP, 
        /**
         * Codes representing a service or product that is being invoiced (billed).  The code can represent such concepts as "office visit", "drug X", "wheelchair" and other billable items such as taxes, service charges and discounts.
         */
        _ACTINVOICEDETAILCODE, 
        /**
         * An identifying data string for healthcare products.
         */
        _ACTINVOICEDETAILCLINICALPRODUCTCODE, 
        /**
         * Description:United Nations Standard Products and Services Classification, managed by Uniform Code Council (UCC): www.unspsc.org
         */
        UNSPSC, 
        /**
         * An identifying data string for A substance used as a medication or in the preparation of medication.
         */
        _ACTINVOICEDETAILDRUGPRODUCTCODE, 
        /**
         * Description:Global Trade Item Number is an identifier for trade items developed by GS1 (comprising the former EAN International and Uniform Code Council).
         */
        GTIN, 
        /**
         * Description:Universal Product Code is one of a wide variety of bar code languages widely used in the United States and Canada for items in stores.
         */
        UPC, 
        /**
         * The detail item codes to identify charges or changes to the total billing of a claim due to insurance rules and payments.
         */
        _ACTINVOICEDETAILGENERICCODE, 
        /**
         * The billable item codes to identify adjudicator specified components to the total billing of a claim.
         */
        _ACTINVOICEDETAILGENERICADJUDICATORCODE, 
        /**
         * That portion of the eligible charges which a covered party must pay for each service and/or product. It is a percentage of the eligible amount for the service/product that is typically charged after the covered party has met the policy deductible.  This amount represents the covered party's coinsurance that is applied to a particular adjudication result. It is expressed as a negative dollar amount in adjudication results.
         */
        COIN, 
        /**
         * That portion of the eligible charges which a covered party must pay for each service and/or product. It is a defined amount per service/product of the eligible amount for the service/product. This amount represents the covered party's copayment that is applied to a particular adjudication result. It is expressed as a negative dollar amount in adjudication results.
         */
        COPAYMENT, 
        /**
         * That portion of the eligible charges which a covered party must pay in a particular period (e.g. annual) before the benefits are payable by the adjudicator. This amount represents the covered party's deductible that is applied to a particular adjudication result. It is expressed as a negative dollar amount in adjudication results.
         */
        DEDUCTIBLE, 
        /**
         * The guarantor, who may be the patient, pays the entire charge for a service. Reasons for such action may include: there is no insurance coverage for the service (e.g. cosmetic surgery); the patient wishes to self-pay for the service; or the insurer denies payment for the service due to contractual provisions such as the need for prior authorization.
         */
        PAY, 
        /**
         * That total amount of the eligible charges which a covered party must periodically pay for services and/or products prior to the Medicaid program providing any coverage. This amount represents the covered party's spend down that is applied to a particular adjudication result. It is expressed as a negative dollar amount in adjudication results
         */
        SPEND, 
        /**
         * The billable item codes to identify modifications to a billable item charge. As for example after hours increase in the office visit fee.
         */
        _ACTINVOICEDETAILGENERICMODIFIERCODE, 
        /**
         * Premium paid on service fees in compensation for practicing outside of normal working hours.
         */
        AFTHRS, 
        /**
         * Premium paid on service fees in compensation for practicing in a remote location.
         */
        ISOL, 
        /**
         * Premium paid on service fees in compensation for practicing at a location other than normal working location.
         */
        OOO, 
        /**
         * The billable item codes to identify provider supplied charges or changes to the total billing of a claim.
         */
        _ACTINVOICEDETAILGENERICPROVIDERCODE, 
        /**
         * A charge to compensate the provider when a patient cancels an appointment with insufficient time for the provider to make another appointment with another patient.
         */
        CANCAPT, 
        /**
         * A reduction in the amount charged as a percentage of the amount. For example a 5% discount for volume purchase.
         */
        DSC, 
        /**
         * A premium on a service fee is requested because, due to extenuating circumstances, the service took an extraordinary amount of time or supplies.
         */
        ESA, 
        /**
         * Under agreement between the parties (payor and provider), a guaranteed level of income is established for the provider over a specific, pre-determined period of time. The normal course of business for the provider is submission of fee-for-service claims. Should the fee-for-service income during the specified period of time be less than the agreed to amount, a top-up amount is paid to the provider equal to the difference between the fee-for-service total and the guaranteed income amount for that period of time. The details of the agreement may specify (or not) a requirement for repayment to the payor in the event that the fee-for-service income exceeds the guaranteed amount.
         */
        FFSTOP, 
        /**
         * Anticipated or actual final fee associated with treating a patient.
         */
        FNLFEE, 
        /**
         * Anticipated or actual initial fee associated with treating a patient.
         */
        FRSTFEE, 
        /**
         * An increase in the amount charged as a percentage of the amount. For example, 12% markup on product cost.
         */
        MARKUP, 
        /**
         * A charge to compensate the provider when a patient does not show for an appointment.
         */
        MISSAPT, 
        /**
         * Anticipated or actual periodic fee associated with treating a patient. For example, expected billing cycle such as monthly, quarterly. The actual period (e.g. monthly, quarterly) is specified in the unit quantity of the Invoice Element.
         */
        PERFEE, 
        /**
         * The amount for a performance bonus that is being requested from a payor for the performance of certain services (childhood immunizations, influenza immunizations, mammograms, pap smears) on a sliding scale. That is, for 90% of childhood immunizations to a maximum of $2200/yr. An invoice is created at the end of the service period (one year) and a code is submitted indicating the percentage achieved and the dollar amount claimed.
         */
        PERMBNS, 
        /**
         * A charge is requested because the patient failed to pick up the item and it took an amount of time to return it to stock for future use.
         */
        RESTOCK, 
        /**
         * A charge to cover the cost of travel time and/or cost in conjuction with providing a service or product. It may be charged per kilometer or per hour based on the effective agreement.
         */
        TRAVEL, 
        /**
         * Premium paid on service fees in compensation for providing an expedited response to an urgent situation.
         */
        URGENT, 
        /**
         * The billable item codes to identify modifications to a billable item charge by a tax factor applied to the amount. As for example 7% provincial sales tax.
         */
        _ACTINVOICEDETAILTAXCODE, 
        /**
         * Federal tax on transactions such as the Goods and Services Tax (GST)
         */
        FST, 
        /**
         * Joint Federal/Provincial Sales Tax
         */
        HST, 
        /**
         * Tax levied by the provincial or state jurisdiction such as Provincial Sales Tax
         */
        PST, 
        /**
         * An identifying data string for medical facility accommodations.
         */
        _ACTINVOICEDETAILPREFERREDACCOMMODATIONCODE, 
        /**
         * Accommodation type.  In Intent mood, represents the accommodation type requested.  In Event mood, represents accommodation assigned/used.  In Definition mood, represents the available accommodation type.
         */
        _ACTENCOUNTERACCOMMODATIONCODE, 
        /**
         * Description:Accommodation type. In Intent mood, represents the accommodation type requested. In Event mood, represents accommodation assigned/used. In Definition mood, represents the available accommodation type.
         */
        _HL7ACCOMMODATIONCODE, 
        /**
         * Accommodations used in the care of diseases that are transmitted through casual contact or respiratory transmission.
         */
        I, 
        /**
         * Accommodations in which there is only 1 bed.
         */
        P, 
        /**
         * Uniquely designed and elegantly decorated accommodations with many amenities available for an additional charge.
         */
        S, 
        /**
         * Accommodations in which there are 2 beds.
         */
        SP, 
        /**
         * Accommodations in which there are 3 or more beds.
         */
        W, 
        /**
         * Type of invoice element that is used to assist in describing an Invoice that is either submitted for adjudication or for which is returned on adjudication results.

                        Invoice elements of this type signify a grouping of one or more children (detail) invoice elements.  They do not have intrinsic costing associated with them, but merely reflect the sum of all costing for it's immediate children invoice elements.
         */
        _ACTINVOICEGROUPCODE, 
        /**
         * Type of invoice element that is used to assist in describing an Invoice that is either submitted for adjudication or for which is returned on adjudication results.

                        Invoice elements of this type signify a grouping of one or more children (detail) invoice elements.  They do not have intrinsic costing associated with them, but merely reflect the sum of all costing for it's immediate children invoice elements.

                        The domain is only specified for an intermediate invoice element group (non-root or non-top level) for an Invoice.
         */
        _ACTINVOICEINTERGROUPCODE, 
        /**
         * A grouping of invoice element groups and details including the ones specifying the compound ingredients being invoiced. It may also contain generic detail items such as markup.
         */
        CPNDDRGING, 
        /**
         * A grouping of invoice element details including the one specifying an ingredient drug being invoiced. It may also contain generic detail items such as tax or markup.
         */
        CPNDINDING, 
        /**
         * A grouping of invoice element groups and details including the ones specifying the compound supplies being invoiced. It may also contain generic detail items such as markup.
         */
        CPNDSUPING, 
        /**
         * A grouping of invoice element details including the one specifying the drug being invoiced. It may also contain generic detail items such as markup.
         */
        DRUGING, 
        /**
         * A grouping of invoice element details including the ones specifying the frame fee and the frame dispensing cost that are being invoiced.
         */
        FRAMEING, 
        /**
         * A grouping of invoice element details including the ones specifying the lens fee and the lens dispensing cost that are being invoiced.
         */
        LENSING, 
        /**
         * A grouping of invoice element details including the one specifying the product (good or supply) being invoiced. It may also contain generic detail items such as tax or discount.
         */
        PRDING, 
        /**
         * Type of invoice element that is used to assist in describing an Invoice that is either submitted for adjudication or for which is returned on adjudication results.

                        Invoice elements of this type signify a grouping of one or more children (detail) invoice elements.  They do not have intrinsic costing associated with them, but merely reflect the sum of all costing for it's immediate children invoice elements.

                        Codes from this domain reflect the type of Invoice such as Pharmacy Dispense, Clinical Service and Clinical Product.  The domain is only specified for the root (top level) invoice element group for an Invoice.
         */
        _ACTINVOICEROOTGROUPCODE, 
        /**
         * Clinical product invoice where the Invoice Grouping contains one or more billable item and is supported by clinical product(s).

                        For example, a crutch or a wheelchair.
         */
        CPINV, 
        /**
         * Clinical Services Invoice which can be used to describe a single service, multiple services or repeated services.

                        [1] Single Clinical services invoice where the Invoice Grouping contains one billable item and is supported by one clinical service.

                        For example, a single service for an office visit or simple clinical procedure (e.g. knee mobilization).

                        [2] Multiple Clinical services invoice where the Invoice Grouping contains more than one billable item, supported by one or more clinical services.  The services can be distinct and over multiple dates, but for the same patient. This type of invoice includes a series of treatments which must be adjudicated together.

                        For example, an adjustment and ultrasound for a chiropractic session where fees are associated for each of the services and adjudicated (invoiced) together.

                        [3] Repeated Clinical services invoice where the Invoice Grouping contains one or more billable item, supported by the same clinical service repeated over a period of time.

                        For example, the same Chiropractic adjustment (service or treatment) delivered on 3 separate occasions over a period of time at the discretion of the provider (e.g. month).
         */
        CSINV, 
        /**
         * A clinical Invoice Grouping consisting of one or more services and one or more product.  Billing for these service(s) and product(s) are supported by multiple clinical billable events (acts).

                        All items in the Invoice Grouping must be adjudicated together to be acceptable to the Adjudicator.

                        For example , a brace (product) invoiced together with the fitting (service).
         */
        CSPINV, 
        /**
         * Invoice Grouping without clinical justification.  These will not require identification of participants and associations from a clinical context such as patient and provider.

                        Examples are interest charges and mileage.
         */
        FININV, 
        /**
         * A clinical Invoice Grouping consisting of one or more oral health services. Billing for these service(s) are supported by multiple clinical billable events (acts).

                        All items in the Invoice Grouping must be adjudicated together to be acceptable to the Adjudicator.
         */
        OHSINV, 
        /**
         * HealthCare facility preferred accommodation invoice.
         */
        PAINV, 
        /**
         * Pharmacy dispense invoice for a compound.
         */
        RXCINV, 
        /**
         * Pharmacy dispense invoice not involving a compound
         */
        RXDINV, 
        /**
         * Clinical services invoice where the Invoice Group contains one billable item for multiple clinical services in one or more sessions.
         */
        SBFINV, 
        /**
         * Vision dispense invoice for up to 2 lens (left and right), frame and optional discount.  Eye exams are invoiced as a clinical service invoice.
         */
        VRXINV, 
        /**
         * Identifies the different types of summary information that can be reported by queries dealing with Statement of Financial Activity (SOFA).  The summary information is generally used to help resolve balance discrepancies between providers and payors.
         */
        _ACTINVOICEELEMENTSUMMARYCODE, 
        /**
         * Total counts and total net amounts adjudicated for all  Invoice Groupings that were adjudicated within a time period based on the adjudication date of the Invoice Grouping.
         */
        _INVOICEELEMENTADJUDICATED, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted electronically.
         */
        ADNFPPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted electronically.
         */
        ADNFPPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted manually.
         */
        ADNFPPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted manually.
         */
        ADNFPPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date), subsequently nullified in the specified period and submitted electronically.
         */
        ADNFSPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date), subsequently nullified in the specified period and submitted electronically.
         */
        ADNFSPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted manually.
         */
        ADNFSPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted manually.
         */
        ADNFSPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted electronically.
         */
        ADNPPPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted electronically.
         */
        ADNPPPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted manually.
         */
        ADNPPPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted manually.
         */
        ADNPPPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted electronically.
         */
        ADNPSPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted electronically.
         */
        ADNPSPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted manually.
         */
        ADNPSPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted manually.
         */
        ADNPSPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted electronically.
         */
        ADPPPPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted electronically.
         */
        ADPPPPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted manually.
         */
        ADPPPPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted manually.
         */
        ADPPPPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted electronically.
         */
        ADPPSPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted electronically.
         */
        ADPPSPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted manually.
         */
        ADPPSPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted manually.
         */
        ADPPSPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as refused prior to the specified time period (based on adjudication date) and submitted electronically.
         */
        ADRFPPELAT, 
        /**
         * Identifies the  total number of all  Invoice Groupings that were adjudicated as refused prior to the specified time period (based on adjudication date) and submitted electronically.
         */
        ADRFPPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as refused prior to the specified time period (based on adjudication date) and submitted manually.
         */
        ADRFPPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as refused prior to the specified time period (based on adjudication date) and submitted manually.
         */
        ADRFPPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as refused during the specified time period (based on adjudication date) and submitted electronically.
         */
        ADRFSPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as refused during the specified time period (based on adjudication date) and submitted electronically.
         */
        ADRFSPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were adjudicated as refused during the specified time period (based on adjudication date) and submitted manually.
         */
        ADRFSPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were adjudicated as refused during the specified time period (based on adjudication date) and submitted manually.
         */
        ADRFSPMNCT, 
        /**
         * Total counts and total net amounts paid for all  Invoice Groupings that were paid within a time period based on the payment date.
         */
        _INVOICEELEMENTPAID, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date), subsequently nullified in the specified period and submitted electronically.
         */
        PDNFPPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date), subsequently nullified in the specified period and submitted electronically.
         */
        PDNFPPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date), subsequently nullified in the specified period and submitted manually.
         */
        PDNFPPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date), subsequently nullified in the specified period and submitted manually.
         */
        PDNFPPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date), subsequently nullified in the specified period and submitted electronically.
         */
        PDNFSPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date), subsequently cancelled in the specified period and submitted electronically.
         */
        PDNFSPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date), subsequently nullified in the specified period and submitted manually.
         */
        PDNFSPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date), subsequently nullified in the specified period and submitted manually.
         */
        PDNFSPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted electronically.
         */
        PDNPPPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted electronically.
         */
        PDNPPPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted manually.
         */
        PDNPPPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted manually.
         */
        PDNPPPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted electronically.
         */
        PDNPSPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted electronically.
         */
        PDNPSPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted manually.
         */
        PDNPSPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted manually.
         */
        PDNPSPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted electronically.
         */
        PDPPPPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted electronically.
         */
        PDPPPPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted manually.
         */
        PDPPPPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted manually.
         */
        PDPPPPMNCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted electronically.
         */
        PDPPSPELAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted electronically.
         */
        PDPPSPELCT, 
        /**
         * Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted manually.
         */
        PDPPSPMNAT, 
        /**
         * Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted manually.
         */
        PDPPSPMNCT, 
        /**
         * Total counts and total net amounts billed for all Invoice Groupings that were submitted within a time period.  Adjudicated invoice elements are included.
         */
        _INVOICEELEMENTSUBMITTED, 
        /**
         * Identifies the total net amount billed for all submitted Invoice Groupings within a time period and submitted electronically.  Adjudicated invoice elements are included.
         */
        SBBLELAT, 
        /**
         * Identifies the total number of submitted Invoice Groupings within a time period and submitted electronically.  Adjudicated invoice elements are included.
         */
        SBBLELCT, 
        /**
         * Identifies the total net amount billed for all submitted  Invoice Groupings that were nullified within a time period and submitted electronically.  Adjudicated invoice elements are included.
         */
        SBNFELAT, 
        /**
         * Identifies the total number of submitted  Invoice Groupings that were nullified within a time period and submitted electronically.  Adjudicated invoice elements are included.
         */
        SBNFELCT, 
        /**
         * Identifies the total net amount billed for all submitted  Invoice Groupings that are pended or held by the payor, within a time period and submitted electronically.  Adjudicated invoice elements are not included.
         */
        SBPDELAT, 
        /**
         * Identifies the total number of submitted  Invoice Groupings that are pended or held by the payor, within a time period and submitted electronically.  Adjudicated invoice elements are not included.
         */
        SBPDELCT, 
        /**
         * Includes coded responses that will occur as a result of the adjudication of an electronic invoice at a summary level and provides guidance on interpretation of the referenced adjudication results.
         */
        _ACTINVOICEOVERRIDECODE, 
        /**
         * Insurance coverage problems have been encountered. Additional explanation information to be supplied.
         */
        COVGE, 
        /**
         * Electronic form with supporting or additional information to follow.
         */
        EFORM, 
        /**
         * Fax with supporting or additional information to follow.
         */
        FAX, 
        /**
         * The medical service was provided to a patient in good faith that they had medical coverage, although no evidence of coverage was available before service was rendered.
         */
        GFTH, 
        /**
         * Knowingly over the payor's published time limit for this invoice possibly due to a previous payor's delays in processing. Additional reason information will be supplied.
         */
        LATE, 
        /**
         * Manual review of the invoice is requested.  Additional information to be supplied.  This may be used in the case of an appeal.
         */
        MANUAL, 
        /**
         * The medical service and/or product was provided to a patient that has coverage in another jurisdiction.
         */
        OOJ, 
        /**
         * The service provided is required for orthodontic purposes. If the covered party has orthodontic coverage, then the service may be paid.
         */
        ORTHO, 
        /**
         * Paper documentation (or other physical format) with supporting or additional information to follow.
         */
        PAPER, 
        /**
         * Public Insurance has been exhausted.  Invoice has not been sent to Public Insuror and therefore no Explanation Of Benefits (EOB) is provided with this Invoice submission.
         */
        PIE, 
        /**
         * Allows provider to explain lateness of invoice to a subsequent payor.
         */
        PYRDELAY, 
        /**
         * Rules of practice do not require a physician's referral for the provider to perform a billable service.
         */
        REFNR, 
        /**
         * The same service was delivered within a time period that would usually indicate a duplicate billing.  However, the repeated service is a medical 	necessity and therefore not a duplicate.
         */
        REPSERV, 
        /**
         * The service provided is not related to another billed service. For example, 2 unrelated services provided on the same day to the same patient which may normally result in a refused payment for one of the items.
         */
        UNRELAT, 
        /**
         * The provider has received a verbal permission from an authoritative source to perform the service or supply the item being invoiced.
         */
        VERBAUTH, 
        /**
         * Provides codes associated with ActClass value of LIST (working list)
         */
        _ACTLISTCODE, 
        /**
         * ActObservationList
         */
        _ACTOBSERVATIONLIST, 
        /**
         * List of acts representing a care plan.  The acts can be in a varierty of moods including event (EVN) to record acts that have been carried out as part of the care plan.
         */
        CARELIST, 
        /**
         * List of condition observations.
         */
        CONDLIST, 
        /**
         * List of intolerance observations.
         */
        INTOLIST, 
        /**
         * List of problem observations.
         */
        PROBLIST, 
        /**
         * List of risk factor observations.
         */
        RISKLIST, 
        /**
         * List of observations in goal mood.
         */
        GOALLIST, 
        /**
         * Codes used to identify different types of 'duration-based' working lists.  Examples include "Continuous/Chronic", "Short-Term" and "As-Needed".
         */
        _ACTTHERAPYDURATIONWORKINGLISTCODE, 
        /**
         * Definition:A collection of concepts that identifies different types of 'duration-based' mediation working lists.

                        
                           Examples:"Continuous/Chronic" "Short-Term" and "As Needed"
         */
        _ACTMEDICATIONTHERAPYDURATIONWORKINGLISTCODE, 
        /**
         * Definition:A list of medications which the patient is only expected to consume for the duration of the current order or limited set of orders and which is not expected to be renewed.
         */
        ACU, 
        /**
         * Definition:A list of medications which are expected to be continued beyond the present order and which the patient should be assumed to be taking unless explicitly stopped.
         */
        CHRON, 
        /**
         * Definition:A list of medications which the patient is intended to be administered only once.
         */
        ONET, 
        /**
         * Definition:A list of medications which the patient will consume intermittently based on the behavior of the condition for which the medication is indicated.
         */
        PRN, 
        /**
         * List of medications.
         */
        MEDLIST, 
        /**
         * List of current medications.
         */
        CURMEDLIST, 
        /**
         * List of discharge medications.
         */
        DISCMEDLIST, 
        /**
         * Historical list of medications.
         */
        HISTMEDLIST, 
        /**
         * Identifies types of monitoring programs
         */
        _ACTMONITORINGPROTOCOLCODE, 
        /**
         * A monitoring program that focuses on narcotics and/or commonly abused substances that are subject to legal restriction.
         */
        CTLSUB, 
        /**
         * Definition:A monitoring program that focuses on a drug which is under investigation and has not received regulatory approval for the condition being investigated
         */
        INV, 
        /**
         * Description:A drug that can be prescribed (and reimbursed) only if it meets certain criteria.
         */
        LU, 
        /**
         * Medicines designated in this way may be supplied for patient use without a prescription.  The exact form of categorisation will vary in different realms.
         */
        OTC, 
        /**
         * Some form of prescription is required before the related medicine can be supplied for a patient.  The exact form of regulation will vary in different realms.
         */
        RX, 
        /**
         * Definition:A drug that requires prior approval (to be reimbursed) before being dispensed
         */
        SA, 
        /**
         * Description:A drug that requires special access permission to be prescribed and dispensed.
         */
        SAC, 
        /**
         * Description:Concepts representing indications (reasons for clinical action) other than diagnosis and symptoms.
         */
        _ACTNONOBSERVATIONINDICATIONCODE, 
        /**
         * Description:Contrast agent required for imaging study.
         */
        IND01, 
        /**
         * Description:Provision of prescription or direction to consume a product for purposes of bowel clearance in preparation for a colonoscopy.
         */
        IND02, 
        /**
         * Description:Provision of medication as a preventative measure during a treatment or other period of increased risk.
         */
        IND03, 
        /**
         * Description:Provision of medication during pre-operative phase; e.g., antibiotics before dental surgery or bowel prep before colon surgery.
         */
        IND04, 
        /**
         * Description:Provision of medication for pregnancy --e.g., vitamins, antibiotic treatments for vaginal tract colonization, etc.
         */
        IND05, 
        /**
         * Identifies the type of verification investigation being undertaken with respect to the subject of the verification activity.

                        
                           Examples:
                        

                        
                           
                              Verification of eligibility for coverage under a policy or program - aka enrolled/covered by a policy or program

                           
                           
                              Verification of record - e.g., person has record in an immunization registry

                           
                           
                              Verification of enumeration - e.g. NPI

                           
                           
                              Verification of Board Certification - provider specific

                           
                           
                              Verification of Certification - e.g. JAHCO, NCQA, URAC

                           
                           
                              Verification of Conformance - e.g. entity use with HIPAA, conformant to the CCHIT EHR system criteria

                           
                           
                              Verification of Provider Credentials

                           
                           
                              Verification of no adverse findings - e.g. on National Provider Data Bank, Health Integrity Protection Data Base (HIPDB)
         */
        _ACTOBSERVATIONVERIFICATIONTYPE, 
        /**
         * Definition:Indicates that the paper version of the record has, should be or is being verified against the electronic version.
         */
        VFPAPER, 
        /**
         * Code identifying the method or the movement of payment instructions.

                        Codes are drawn from X12 data element 591 (PaymentMethodCode)
         */
        _ACTPAYMENTCODE, 
        /**
         * Automated Clearing House (ACH).
         */
        ACH, 
        /**
         * A written order to a bank to pay the amount specified from funds on deposit.
         */
        CHK, 
        /**
         * Electronic Funds Transfer (EFT) deposit into the payee's bank account
         */
        DDP, 
        /**
         * Non-Payment Data.
         */
        NON, 
        /**
         * Identifies types of dispensing events
         */
        _ACTPHARMACYSUPPLYTYPE, 
        /**
         * A fill providing sufficient supply for one day
         */
        DF, 
        /**
         * A supply action where there is no 'valid' order for the supplied medication.  E.g. Emergency vacation supply, weekend supply (when prescriber is unavailable to provide a renewal prescription)
         */
        EM, 
        /**
         * An emergency supply where the expectation is that a formal order authorizing the supply will be provided at a later date.
         */
        SO, 
        /**
         * The initial fill against an order.  (This includes initial fills against refill orders.)
         */
        FF, 
        /**
         * A first fill where the quantity supplied is equal to one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a complete fill would be for the full 90 tablets).
         */
        FFC, 
        /**
         * A first fill where the quantity supplied is equal to one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a complete fill would be for the full 90 tablets) and also where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).
         */
        FFCS, 
        /**
         * A first fill where the quantity supplied is less than one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a partial fill might be for only 30 tablets.)
         */
        FFP, 
        /**
         * A first fill where the quantity supplied is less than one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a partial fill might be for only 30 tablets.) and also where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets)
         */
        FFPS, 
        /**
         * A first fill where the strength supplied is less than the ordered strength. (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).
         */
        FFSS, 
        /**
         * A fill where a small portion is provided to allow for determination of the therapy effectiveness and patient tolerance and also where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).
         */
        TFS, 
        /**
         * A fill where a small portion is provided to allow for determination of the therapy effectiveness and patient tolerance.
         */
        TF, 
        /**
         * A supply action to restock a smaller more local dispensary.
         */
        FS, 
        /**
         * A supply of a manufacturer sample
         */
        MS, 
        /**
         * A fill against an order that has already been filled (or partially filled) at least once.
         */
        RF, 
        /**
         * A supply action that provides sufficient material for a single dose.
         */
        UD, 
        /**
         * A refill where the quantity supplied is equal to one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a complete fill would be for the full 90 tablets.)
         */
        RFC, 
        /**
         * A refill where the quantity supplied is equal to one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a complete fill would be for the full 90 tablets.) and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).
         */
        RFCS, 
        /**
         * The first fill against an order that has already been filled at least once at another facility.
         */
        RFF, 
        /**
         * The first fill against an order that has already been filled at least once at another facility and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).
         */
        RFFS, 
        /**
         * A refill where the quantity supplied is less than one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a partial fill might be for only 30 tablets.)
         */
        RFP, 
        /**
         * A refill where the quantity supplied is less than one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a partial fill might be for only 30 tablets.) and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).
         */
        RFPS, 
        /**
         * A fill against an order that has already been filled (or partially filled) at least once and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).
         */
        RFS, 
        /**
         * A fill where the remainder of a 'complete' fill is provided after a trial fill has been provided.
         */
        TB, 
        /**
         * A fill where the remainder of a 'complete' fill is provided after a trial fill has been provided and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).
         */
        TBS, 
        /**
         * A supply action that provides sufficient material for a single dose via multiple products.  E.g. 2 50mg tablets for a 100mg unit dose.
         */
        UDE, 
        /**
         * Description:Types of policies that further specify the ActClassPolicy value set.
         */
        _ACTPOLICYTYPE, 
        /**
         * A policy deeming certain information to be private to an individual or organization.

                        
                           Definition: A mandate, obligation, requirement, rule, or expectation relating to privacy.

                        
                           Discussion: ActPrivacyPolicyType codes support the designation of the 1..* policies that are applicable to an Act such as a Consent Directive, a Role such as a VIP Patient, or an Entity such as a patient who is a minor.  1..* ActPrivacyPolicyType values may be associated with an Act or Role to indicate the policies that govern the assignment of an Act or Role confidentialityCode.  Use of multiple ActPrivacyPolicyType values enables fine grain specification of applicable policies, but must be carefully assigned to ensure cogency and avoid creation of conflicting policy mandates.

                        
                           Usage Note: Statutory title may be named in the ActClassPolicy Act Act.title to specify which privacy policy is being referenced.
         */
        _ACTPRIVACYPOLICY, 
        /**
         * Definition: Specifies the type of consent directive indicated by an ActClassPolicy e.g., a 3rd party authorization to disclose or consent for a substitute decision maker (SDM) or a notice of privacy policy.

                        
                           Usage Note: ActConsentDirective codes are used to specify the type of Consent Directive to which a Consent Directive Act conforms.
         */
        _ACTCONSENTDIRECTIVE, 
        /**
         * This general consent directive specifically limits disclosure of health information for purpose of emergency treatment. Additional parameters may further limit the disclosure to specific users, roles, duration, types of information, and impose uses obligations.

                        
                           Definition: Opt-in to disclosure of health information for emergency only consent directive.
         */
        EMRGONLY, 
        /**
         * Acknowledgement of custodian notice of privacy practices.

                        
                           Usage Notes: This type of consent directive acknowledges a custodian's notice of privacy practices including its permitted collection, access, use and disclosure of health information to users and for purposes of use specified.
         */
        NOPP, 
        /**
         * This general consent directive permits disclosure of health information.  Additional parameter may limit authorized users, purpose of use, user obligations, duration, or information types permitted to be disclosed, and impose uses obligations.

                        
                           Definition: Opt-in to disclosure of health information consent directive.
         */
        OPTIN, 
        /**
         * This general consent directive prohibits disclosure of health information.  Additional parameters may permit access to some information types by certain users, roles, purposes of use, durations and impose user obligations.

                        
                           Definition: Opt-out of disclosure of health information consent directive.
         */
        OPTOUT, 
        /**
         * A mandate, obligation, requirement, rule, or expectation characterizing the value or importance of a resource and may include its vulnerability. (Based on ISO7498-2:1989. Note: The vulnerability of personally identifiable sensitive information may be based on concerns that the unauthorized disclosure may result in social stigmatization or discrimination.) Description:  Types of Sensitivity policy that apply to Acts or Roles.  A sensitivity policy is adopted by an enterprise or group of enterprises (a 'policy domain') through a formal data use agreement that stipulates the value, importance, and vulnerability of information. A sensitivity code representing a sensitivity policy may be associated with criteria such as categories of information or sets of information identifiers (e.g., a value set of clinical codes or branch in a code system hierarchy).   These criteria may in turn be used for the Policy Decision Point in a Security Engine.  A sensitivity code may be used to set the confidentiality code used on information about Acts and Roles to trigger the security mechanisms required to control how security principals (i.e., a person, a machine, a software application) may act on the information (e.g., collection, access, use, or disclosure). Sensitivity codes are never assigned to the transport or business envelope containing patient specific information being exchanged outside of a policy domain as this would disclose the information intended to be protected by the policy.  When sensitive information is exchanged with others outside of a policy domain, the confidentiality code on the transport or business envelope conveys the receiver's responsibilities and indicates the how the information is to be safeguarded without unauthorized disclosure of the sensitive information.  This ensures that sensitive information is treated by receivers as the sender intends, accomplishing interoperability without point to point negotiations.

                        
                           Usage Note: Sensitivity codes are not useful for interoperability outside of a policy domain because sensitivity policies are typically localized and vary drastically across policy domains even for the same information category because of differing organizational business rules, security policies, and jurisdictional requirements.  For example, an employee's sensitivity code would make little sense for use outside of a policy domain.   'Taboo' would rarely be useful outside of a policy domain unless there are jurisdictional requirements requiring that a provider disclose sensitive information to a patient directly.  Sensitivity codes may be more appropriate in a legacy system's Master Files in order to notify those who access a patient's orders and observations about the sensitivity policies that apply.  Newer systems may have a security engine that uses a sensitivity policy's criteria directly.  The specializable InformationSensitivityPolicy Act.code may be useful in some scenarios if used in combination with a sensitivity identifier and/or Act.title.
         */
        _INFORMATIONSENSITIVITYPOLICY, 
        /**
         * Types of sensitivity policies that apply to Acts.  Act.confidentialityCode is defined in the RIM as "constraints around appropriate disclosure of information about this Act, regardless of mood."

                        
                           Usage Note: ActSensitivity codes are used to bind information to an Act.confidentialityCode according to local sensitivity policy so that those confidentiality codes can then govern its handling across enterprises.  Internally to a policy domain, however, local policies guide the access control system on how end users in that policy domain are  able to use information tagged with these sensitivity values.
         */
        _ACTINFORMATIONSENSITIVITYPOLICY, 
        /**
         * Policy for handling alcohol or drug-abuse information, which will be afforded heightened confidentiality.  Information handling protocols based on organizational policies related to alcohol or drug-abuse information that is deemed sensitive.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        ETH, 
        /**
         * Policy for handling genetic disease information, which will be afforded heightened confidentiality. Information handling protocols based on organizational policies related to genetic disease information that is deemed sensitive.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        GDIS, 
        /**
         * Policy for handling HIV or AIDS information, which will be afforded heightened confidentiality. Information handling protocols based on organizational policies related to HIV or AIDS information that is deemed sensitive.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        HIV, 
        /**
         * Policy for handling psychiatry information, which will be afforded heightened confidentiality. Information handling protocols based on organizational policies related to psychiatry information that is deemed sensitive.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        PSY, 
        /**
         * Policy for handling sickle cell disease information, which is afforded heightened confidentiality.  Information handling protocols are based on organizational policies related to sickle cell disease information, which is deemed sensitive.

                        
                           Usage Note: If there is a jurisdictional mandate, then the Act valued with this ActCode should be associated with an Act valued with any applicable laws from the ActPrivacyLaw code system.
         */
        SCA, 
        /**
         * Policy for handling sexual assault, abuse, or domestic violence information, which will be afforded heightened confidentiality. Information handling protocols based on organizational policies related to sexual assault, abuse, or domestic violence information that is deemed sensitive.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        SDV, 
        /**
         * Policy for handling sexuality and reproductive health information, which will be afforded heightened confidentiality.  Information handling protocols based on organizational policies related to sexuality and reproductive health information that is deemed sensitive.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        SEX, 
        /**
         * Policy for handling sexually transmitted disease information, which will be afforded heightened confidentiality.
 Information handling protocols based on organizational policies related to sexually transmitted disease information that is deemed sensitive.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        STD, 
        /**
         * Policy for handling information not to be initially disclosed or discussed with patient except by a physician assigned to patient in this case. Information handling protocols based on organizational policies related to sensitive patient information that must be initially discussed with the patient by an attending physician before being disclosed to the patient.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.

                        
                           Open Issue: This definition conflates a rule and a characteristic, and there may be a similar issue with ts sibling codes.
         */
        TBOO, 
        /**
         * Types of sensitivity policies that may apply to a sensitive attribute on an Entity.

                        
                           Usage Note: EntitySensitivity codes are used to convey a policy that is applicable to sensitive information conveyed by an entity attribute.  May be used to bind a Role.confidentialityCode associated with an Entity per organizational policy.  Role.confidentialityCode is defined in the RIM as "an indication of the appropriate disclosure of information about this Role with respect to the playing Entity."
         */
        _ENTITYSENSITIVITYPOLICYTYPE, 
        /**
         * Policy for handling all demographic information about an information subject, which will be afforded heightened confidentiality. Policies may govern sensitivity of information related to all demographic about an information subject, the disclosure of which could impact the privacy, well-being, or safety of that subject.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        DEMO, 
        /**
         * Policy for handling information related to an information subject's date of birth, which will be afforded heightened confidentiality.Policies may govern sensitivity of information related to an information subject's date of birth, the disclosure of which could impact the privacy, well-being, or safety of that subject.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        DOB, 
        /**
         * Policy for handling information related to an information subject's gender and sexual orientation, which will be afforded heightened confidentiality.  Policies may govern sensitivity of information related to an information subject's gender and sexual orientation, the disclosure of which could impact the privacy, well-being, or safety of that subject.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        GENDER, 
        /**
         * Policy for handling information related to an information subject's living arrangement, which will be afforded heightened confidentiality.  Policies may govern sensitivity of information related to an information subject's living arrangement, the disclosure of which could impact the privacy, well-being, or safety of that subject.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        LIVARG, 
        /**
         * Policy for handling information related to an information subject's marital status, which will be afforded heightened confidentiality. Policies may govern sensitivity of information related to an information subject's marital status, the disclosure of which could impact the privacy, well-being, or safety of that subject.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        MARST, 
        /**
         * Policy for handling information related to an information subject's race, which will be afforded heightened confidentiality.  Policies may govern sensitivity of information related to an information subject's race, the disclosure of which could impact the privacy, well-being, or safety of that subject.

                        
                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        RACE, 
        /**
         * Policy for handling information related to an information subject's religious affiliation, which will be afforded heightened confidentiality.  Policies may govern sensitivity of information related to an information subject's religion, the disclosure of which could impact the privacy, well-being, or safety of that subject.

                        
                           Usage Notes: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        REL, 
        /**
         * Types of sensitivity policies that apply to Roles.

                        
                           Usage Notes: RoleSensitivity codes are used to bind information to a Role.confidentialityCode per organizational policy.  Role.confidentialityCode is defined in the RIM as "an indication of the appropriate disclosure of information about this Role with respect to the playing Entity."
         */
        _ROLEINFORMATIONSENSITIVITYPOLICY, 
        /**
         * Policy for handling trade secrets such as financial information or intellectual property, which will be afforded heightened confidentiality.  Description:  Since the service class can represent knowledge structures that may be considered a trade or business secret, there is sometimes (though rarely) the need to flag those items as of business level confidentiality.

                        
                           Usage Notes: No patient related information may ever be of this confidentiality level.   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        B, 
        /**
         * Policy for handling information related to an employer which is deemed classified to protect an employee who is the information subject, and which will be afforded heightened confidentiality.  Description:  Policies may govern sensitivity of information related to an employer, such as law enforcement or national security, the identity of which could impact the privacy, well-being, or safety of an information subject who is an employee.

                        
                           Usage Notes: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        EMPL, 
        /**
         * Policy for handling information related to the location of the information subject, which will be afforded heightened confidentiality.  Description:  Policies may govern sensitivity of information related to the location of the information subject, the disclosure of which could impact the privacy, well-being, or safety of that subject.

                        
                           Usage Notes: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        LOCIS, 
        /**
         * Policy for handling information related to a provider of sensitive services, which will be afforded heightened confidentiality.  Description:  Policies may govern sensitivity of information related to providers who deliver sensitive healthcare services in order to protect the privacy, well-being, and safety of the provider and of patients receiving sensitive services.

                        
                           Usage Notes: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        SSP, 
        /**
         * Policy for handling information related to an adolescent, which will be afforded heightened confidentiality per applicable organizational or jurisdictional policy.  An enterprise may have a policy that requires that adolescent patient information be provided heightened confidentiality.  Information deemed sensitive typically includes health information and patient role information including patient status, demographics, next of kin, and location.

                        
                           Usage Note: For use within an enterprise in which an adolescent is the information subject.  If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        ADOL, 
        /**
         * Policy for handling information related to a celebrity (people of public interest (VIP), which will be afforded heightened confidentiality.  Celebrities are people of public interest (VIP) about whose information an enterprise may have a policy that requires heightened confidentiality.  Information deemed sensitive may include health information and patient role information including patient status, demographics, next of kin, and location.

                        
                           Usage Note:  For use within an enterprise in which the information subject is deemed a celebrity or very important person.  If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        CEL, 
        /**
         * Policy for handling information related to a diagnosis, health condition or health problem, which will be afforded heightened confidentiality.  Diagnostic, health condition or health problem related information may be deemed sensitive by organizational policy, and require heightened confidentiality.

                        
                           Usage Note: For use within an enterprise that provides heightened confidentiality to  diagnostic, health condition or health problem related information deemed sensitive.   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        DIA, 
        /**
         * Policy for handling information related to a drug, which will be afforded heightened confidentiality. Drug information may be deemed sensitive by organizational policy, and require heightened confidentiality.

                        
                           Usage Note: For use within an enterprise that provides heightened confidentiality to drug information deemed sensitive.   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        DRGIS, 
        /**
         * Policy for handling information related to an employee, which will be afforded heightened confidentiality. When a patient is an employee, an enterprise may have a policy that requires heightened confidentiality.  Information deemed sensitive typically includes health information and patient role information including patient status, demographics, next of kin, and location.

                        
                           Usage Note: Policy for handling information related to an employee, which will be afforded heightened confidentiality.  Description:  When a patient is an employee, an enterprise may have a policy that requires heightened confidentiality.  Information deemed sensitive typically includes health information and patient role information including patient status, demographics, next of kin, and location.
         */
        EMP, 
        /**
         * Policy for handling information reported by the patient about another person, e.g., a family member, which will be afforded heightened confidentiality. Sensitive information reported by the patient about another person, e.g., family members may be deemed sensitive by default.  The flag may be set or cleared on patient's request.  

                        
                           Usage Note: For sensitive information relayed by or about a patient, which is deemed sensitive within the enterprise (i.e., by default regardless of whether the patient requested that the information be deemed sensitive.)   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        PDS, 
        /**
         * For sensitive information relayed by or about a patient, which is deemed sensitive within the enterprise (i.e., by default regardless of whether the patient requested that the information be deemed sensitive.)   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.

                        
                           Usage Note: For use within an enterprise that provides heightened confidentiality to certain types of information designated by a patient as sensitive.   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.
         */
        PRS, 
        /**
         * This is the healthcare analog to the US Intelligence Community's concept of a Special Access Program.  Compartment codes may be used in as a field value in an initiator's clearance to indicate permission to access and use an IT Resource with a security label having the same compartment value in security category label field.

                        Map: Aligns with ISO 2382-8 definition of Compartment - "A division of data into isolated blocks with separate security controls for the purpose of reducing risk."
         */
        COMPT, 
        /**
         * A security category label field value, which indicates that access and use of an IT resource is restricted to members of human resources department or workflow.
         */
        HRCOMPT, 
        /**
         * A security category label field value, which indicates that access and use of an IT resource is restricted to members of a research project.
         */
        RESCOMPT, 
        /**
         * A security category label field value, which indicates that access and use of an IT resource is restricted to members of records management department or workflow.
         */
        RMGTCOMPT, 
        /**
         * A mandate, obligation, requirement, rule, or expectation conveyed as security metadata between senders and receivers required to establish the reliability, authenticity, and trustworthiness of their transactions.

                        Trust security metadata are observation made about aspects of trust applicable to an IT resource (data, information object, service, or system capability).

                        Trust applicable to IT resources is established and maintained in and among security domains, and may be comprised of observations about the domain's trust authority, trust framework, trust policy, trust interaction rules, means for assessing and monitoring adherence to trust policies, mechanisms that enforce trust, and quality and reliability measures of assurance in those mechanisms. [Based on ISO IEC 10181-1 and NIST SP 800-63-2]

                        For example, identity proofing , level of assurance, and Trust Framework.
         */
        ACTTRUSTPOLICYTYPE, 
        /**
         * Type of security metadata about the formal declaration by an authority or neutral third party that validates the technical, security, trust, and business practice conformance of Trust Agents to facilitate security, interoperability, and trust among participants within a security domain or trust framework.
         */
        TRSTACCRD, 
        /**
         * Type of security metadata about privacy and security requirements with which a security domain must comply. [ISO IEC 10181-1]
         */
        TRSTAGRE, 
        /**
         * Type of security metadata about the digital quality or reliability of a trust assertion, activity, capability, information exchange, mechanism, process, or protocol.
         */
        TRSTASSUR, 
        /**
         * Type of security metadata about a set of security-relevant data issued by a security authority or trusted third party, together with security information which is used to provide the integrity and data origin authentication services for an IT resource (data, information object, service, or system capability). [Based on ISO IEC 10181-1]
         */
        TRSTCERT, 
        /**
         * Type of security metadata about a complete set of contracts, regulations, or commitments that enable participating actors to rely on certain assertions by other actors to fulfill their information security requirements. [Kantara Initiative]
         */
        TRSTFWK, 
        /**
         * Type of security metadata about a security architecture system component that supports enforcement of security policies.
         */
        TRSTMEC, 
        /**
         * Description:A mandate, obligation, requirement, rule, or expectation unilaterally imposed on benefit coverage under a policy or program by a sponsor, underwriter or payor on:

                        
                           
                              The activity of another party

                           
                           
                              The behavior of another party

                           
                           
                              The manner in which an act is executed

                           
                        
                        
                           Examples:A clinical protocol imposed by a payer to which a provider must adhere in order to be paid for providing the service.  A formulary from which a provider must select prescribed drugs in order for the patient to incur a lower copay.
         */
        COVPOL, 
        /**
         * Types of security policies that further specify the ActClassPolicy value set.

                        
                           Examples:
                        

                        
                           obligation to encrypt
                           refrain from redisclosure without consent
         */
        SECURITYPOLICY, 
        /**
         * Conveys the mandated workflow action that an information custodian, receiver, or user must perform.  

                        
                           Usage Notes: Per ISO 22600-2, ObligationPolicy instances 'are event-triggered and define actions to be performed by manager agent'. Per HL7 Composite Security and Privacy Domain Analysis Model:  This value set refers to the action required to receive the permission specified in the privacy rule. Per OASIS XACML, an obligation is an operation specified in a policy or policy that is performed in conjunction with the enforcement of an access control decision.
         */
        OBLIGATIONPOLICY, 
        /**
         * Custodian system must remove any information that could result in identifying the information subject.
         */
        ANONY, 
        /**
         * Custodian system must make available to an information subject upon request an accounting of certain disclosures of the individualâ€™s protected health information over a period of time.  Policy may dictate that the accounting include information about the information disclosed,  the date of disclosure, the identification of the receiver, the purpose of the disclosure, the time in which the disclosing entity must provide a response and the time period for which accountings of disclosure can be requested.
         */
        AOD, 
        /**
         * Custodian system must monitor systems to ensure that all users are authorized to operate on information objects.
         */
        AUDIT, 
        /**
         * Custodian system must monitor and maintain retrievable log for each user and operation on information.
         */
        AUDTR, 
        /**
         * Custodian security system must retrieve, evaluate, and comply with the information handling directions of the Confidentiality Code associated with an information target.
         */
        CPLYCC, 
        /**
         * Custodian security system must retrieve, evaluate, and comply with applicable information subject consent directives.
         */
        CPLYCD, 
        /**
         * Custodian security system must retrieve, evaluate, and comply with applicable jurisdictional privacy policies associated with the target information.
         */
        CPLYJPP, 
        /**
         * Custodian security system must retrieve, evaluate, and comply with applicable organizational privacy policies associated with the target information.
         */
        CPLYOPP, 
        /**
         * Custodian security system must retrieve, evaluate, and comply with the organizational security policies associated with the target information.
         */
        CPLYOSP, 
        /**
         * Custodian security system must retrieve, evaluate, and comply with applicable policies associated with the target information.
         */
        CPLYPOL, 
        /**
         * Custodian system must strip information of data that would allow the identification of the source of the information or the information subject.
         */
        DEID, 
        /**
         * Custodian system must remove target information from access after use.
         */
        DELAU, 
        /**
         * Custodian system must render information unreadable by algorithmically transforming plaintext into ciphertext.  

                        

                        
                           Usage Notes: A mathematical transposition of a file or data stream so that it cannot be deciphered at the receiving end without the proper key. Encryption is a security feature that assures that only the parties who are supposed to be participating in a videoconference or data transfer are able to do so. It can include a password, public and private keys, or a complex combination of all.  (Per Infoway.)
         */
        ENCRYPT, 
        /**
         * Custodian system must render information unreadable and unusable by algorithmically transforming plaintext into ciphertext when "at rest" or in storage.
         */
        ENCRYPTR, 
        /**
         * Custodian system must render information unreadable and unusable by algorithmically transforming plaintext into ciphertext while "in transit" or being transported by any means.
         */
        ENCRYPTT, 
        /**
         * Custodian system must render information unreadable and unusable by algorithmically transforming plaintext into ciphertext while in use such that operations permitted on the target information are limited by the license granted to the end user.
         */
        ENCRYPTU, 
        /**
         * Custodian system must require human review and approval for permission requested.
         */
        HUAPRV, 
        /**
         * Custodian system must render information unreadable and unusable by algorithmically transforming plaintext into ciphertext.  User may be provided a key to decrypt per license or "shared secret".
         */
        MASK, 
        /**
         * Custodian must limit access and disclosure to the minimum information required to support an authorized user's purpose of use.  

                        
                           Usage Note: Limiting the information available for access and disclosure to that an authorized user or receiver "needs to know" in order to perform permitted workflow or purpose of use.
         */
        MINEC, 
        /**
         * Custodian must create and/or maintain human readable security label tags as required by policy.

                        Map:  Aligns with ISO 22600-3 Section A.3.4.3 description of privacy mark:  "If present, the privacy-mark is not used for access control. The content of the privacy-mark may be defined by the security policy in force (identified by the security-policy-identifier) which may define a list of values to be used. Alternately, the value may be determined by the originator of the security-label."
         */
        PRIVMARK, 
        /**
         * Custodian system must strip information of data that would allow the identification of the source of the information or the information subject.  Custodian may retain a key to relink data necessary to reidentify the information subject.
         */
        PSEUD, 
        /**
         * Custodian system must remove information, which is not authorized to be access, used, or disclosed from records made available to otherwise authorized users.
         */
        REDACT, 
        /**
         * Conveys prohibited actions which an information custodian, receiver, or user is not permitted to perform unless otherwise authorized or permitted under specified circumstances.

                        

                        
                           Usage Notes: ISO 22600-2 species that a Refrain Policy "defines actions the subjects must refrain from performing".  Per HL7 Composite Security and Privacy Domain Analysis Model:  May be used to indicate that a specific action is prohibited based on specific access control attributes e.g., purpose of use, information type, user role, etc.
         */
        REFRAINPOLICY, 
        /**
         * Prohibition on disclosure without information subject's authorization.
         */
        NOAUTH, 
        /**
         * Prohibition on collection or storage of the information.
         */
        NOCOLLECT, 
        /**
         * Prohibition on disclosure without organizational approved patient restriction.
         */
        NODSCLCD, 
        /**
         * Prohibition on disclosure without a consent directive from the information subject.
         */
        NODSCLCDS, 
        /**
         * Prohibition on Integration into other records.
         */
        NOINTEGRATE, 
        /**
         * Prohibition on disclosure except to entities on specific access list.
         */
        NOLIST, 
        /**
         * Prohibition on disclosure without an interagency service agreement or memorandum of understanding (MOU).
         */
        NOMOU, 
        /**
         * Prohibition on disclosure without organizational authorization.
         */
        NOORGPOL, 
        /**
         * Prohibition on disclosing information to patient, family or caregivers without attending provider's authorization.

                        
                           Usage Note: The information may be labeled with the ActInformationSensitivity TBOO code, triggering application of this RefrainPolicy code as a handling caveat controlling access.

                        Maps to FHIR NOPAT: Typically, this is used on an Alert resource, when the alert records information on patient abuse or non-compliance.

                        FHIR print name is "keep information from patient". Maps to the French realm - code: INVISIBLE_PATIENT.

                        
                           displayName: Document non visible par le patient
                           codingScheme: 1.2.250.1.213.1.1.4.13
                        
                        French use case:  A label for documents that the author  chose to hide from the patient until the content can be disclose to the patient in a face to face meeting between a healthcare professional and the patient (in French law some results like cancer diagnosis or AIDS diagnosis must be announced to the patient by a healthcare professional and should not be find out by the patient alone).
         */
        NOPAT, 
        /**
         * Prohibition on collection of the information beyond time necessary to accomplish authorized purpose of use is prohibited.
         */
        NOPERSISTP, 
        /**
         * Prohibition on redisclosure without patient consent directive.
         */
        NORDSCLCD, 
        /**
         * Prohibition on redisclosure without a consent directive from the information subject.
         */
        NORDSCLCDS, 
        /**
         * Prohibition on disclosure without authorization under jurisdictional law.
         */
        NORDSCLW, 
        /**
         * Prohibition on associating de-identified or pseudonymized information with other information in a manner that could or does result in disclosing information intended to be masked.
         */
        NORELINK, 
        /**
         * Prohibition on use of the information beyond the purpose of use initially authorized.
         */
        NOREUSE, 
        /**
         * Prohibition on disclosure except to principals with access permission to specific VIP information.
         */
        NOVIP, 
        /**
         * Prohibition on disclosure except as permitted by the information originator.
         */
        ORCON, 
        /**
         * The method that a product is obtained for use by the subject of the supply act (e.g. patient).  Product examples are consumable or durable goods.
         */
        _ACTPRODUCTACQUISITIONCODE, 
        /**
         * Temporary supply of a product without transfer of ownership for the product.
         */
        LOAN, 
        /**
         * Temporary supply of a product with financial compensation, without transfer of ownership for the product.
         */
        RENT, 
        /**
         * Transfer of ownership for a product.
         */
        TRANSFER, 
        /**
         * Transfer of ownership for a product for financial compensation.
         */
        SALE, 
        /**
         * Transportation of a specimen.
         */
        _ACTSPECIMENTRANSPORTCODE, 
        /**
         * Description:Specimen has been received by the participating organization/department.
         */
        SREC, 
        /**
         * Description:Specimen has been placed into storage at a participating location.
         */
        SSTOR, 
        /**
         * Description:Specimen has been put in transit to a participating receiver.
         */
        STRAN, 
        /**
         * Set of codes related to specimen treatments
         */
        _ACTSPECIMENTREATMENTCODE, 
        /**
         * The lowering of specimen pH through the addition of an acid
         */
        ACID, 
        /**
         * The act rendering alkaline by impregnating with an alkali; a conferring of alkaline qualities.
         */
        ALK, 
        /**
         * The removal of fibrin from whole blood or plasma through physical or chemical means
         */
        DEFB, 
        /**
         * The passage of a liquid through a filter, accomplished by gravity, pressure or vacuum (suction).
         */
        FILT, 
        /**
         * LDL Precipitation
         */
        LDLP, 
        /**
         * The act or process by which an acid and a base are combined in such proportions that the resulting compound is neutral.
         */
        NEUT, 
        /**
         * The addition of calcium back to a specimen after it was removed by chelating agents
         */
        RECA, 
        /**
         * The filtration of a colloidal substance through a semipermeable medium that allows only the passage of small molecules.
         */
        UFIL, 
        /**
         * Description: Describes the type of substance administration being performed.  This should not be used to carry codes for identification of products.  Use an associated role or entity to carry such information.
         */
        _ACTSUBSTANCEADMINISTRATIONCODE, 
        /**
         * The introduction of a drug into a subject with the intention of altering its biologic state with the intent of improving its health status.
         */
        DRUG, 
        /**
         * Description: The introduction of material into a subject with the intent of providing nutrition or other dietary supplements (e.g. minerals or vitamins).
         */
        FD, 
        /**
         * The introduction of an immunogen with the intent of stimulating an immune response, aimed at preventing subsequent infections by more viable agents.
         */
        IMMUNIZ, 
        /**
         * Description: A task or action that a user may perform in a clinical information system (e.g., medication order entry, laboratory test results review, problem list entry).
         */
        _ACTTASKCODE, 
        /**
         * A clinician creates a request for a service to be performed for a given patient.
         */
        OE, 
        /**
         * A clinician creates a request for a laboratory test to be done for a given patient.
         */
        LABOE, 
        /**
         * A clinician creates a request for the administration of one or more medications to a given patient.
         */
        MEDOE, 
        /**
         * A person enters documentation about a given patient.
         */
        PATDOC, 
        /**
         * Description: A person reviews a list of known allergies of a given patient.
         */
        ALLERLREV, 
        /**
         * A clinician enters a clinical note about a given patient
         */
        CLINNOTEE, 
        /**
         * A clinician enters a diagnosis for a given patient.
         */
        DIAGLISTE, 
        /**
         * A person provides a discharge instruction to a patient.
         */
        DISCHINSTE, 
        /**
         * A clinician enters a discharge summary for a given patient.
         */
        DISCHSUME, 
        /**
         * A person provides a patient-specific education handout to a patient.
         */
        PATEDUE, 
        /**
         * A pathologist enters a report for a given patient.
         */
        PATREPE, 
        /**
         * A clinician enters a problem for a given patient.
         */
        PROBLISTE, 
        /**
         * A radiologist enters a report for a given patient.
         */
        RADREPE, 
        /**
         * Description: A person reviews a list of immunizations due or received for a given patient.
         */
        IMMLREV, 
        /**
         * Description: A person reviews a list of health care reminders for a given patient.
         */
        REMLREV, 
        /**
         * Description: A person reviews a list of wellness or preventive care reminders for a given patient.
         */
        WELLREMLREV, 
        /**
         * A person (e.g., clinician, the patient herself) reviews patient information in the electronic medical record.
         */
        PATINFO, 
        /**
         * Description: A person enters a known allergy for a given patient.
         */
        ALLERLE, 
        /**
         * A person reviews a recommendation/assessment provided automatically by a clinical decision support application for a given patient.
         */
        CDSREV, 
        /**
         * A person reviews a clinical note of a given patient.
         */
        CLINNOTEREV, 
        /**
         * A person reviews a discharge summary of a given patient.
         */
        DISCHSUMREV, 
        /**
         * A person reviews a list of diagnoses of a given patient.
         */
        DIAGLISTREV, 
        /**
         * Description: A person enters an immunization due or received for a given patient.
         */
        IMMLE, 
        /**
         * A person reviews a list of laboratory results of a given patient.
         */
        LABRREV, 
        /**
         * A person reviews a list of microbiology results of a given patient.
         */
        MICRORREV, 
        /**
         * A person reviews organisms of microbiology results of a given patient.
         */
        MICROORGRREV, 
        /**
         * A person reviews the sensitivity test of microbiology results of a given patient.
         */
        MICROSENSRREV, 
        /**
         * A person reviews a list of medication orders submitted to a given patient
         */
        MLREV, 
        /**
         * A clinician reviews a work list of medications to be administered to a given patient.
         */
        MARWLREV, 
        /**
         * A person reviews a list of orders submitted to a given patient.
         */
        OREV, 
        /**
         * A person reviews a pathology report of a given patient.
         */
        PATREPREV, 
        /**
         * A person reviews a list of problems of a given patient.
         */
        PROBLISTREV, 
        /**
         * A person reviews a radiology report of a given patient.
         */
        RADREPREV, 
        /**
         * Description: A person enters a health care reminder for a given patient.
         */
        REMLE, 
        /**
         * Description: A person enters a wellness or preventive care reminder for a given patient.
         */
        WELLREMLE, 
        /**
         * A person reviews a Risk Assessment Instrument report of a given patient.
         */
        RISKASSESS, 
        /**
         * A person reviews a Falls Risk Assessment Instrument report of a given patient.
         */
        FALLRISK, 
        /**
         * Characterizes how a transportation act was or will be carried out.

                        
                           Examples: Via private transport, via public transit, via courier.
         */
        _ACTTRANSPORTATIONMODECODE, 
        /**
         * Definition: Characterizes how a patient was or will be transported to the site of a patient encounter.

                        
                           Examples: Via ambulance, via public transit, on foot.
         */
        _ACTPATIENTTRANSPORTATIONMODECODE, 
        /**
         * pedestrian transport
         */
        AFOOT, 
        /**
         * ambulance transport
         */
        AMBT, 
        /**
         * fixed-wing ambulance transport
         */
        AMBAIR, 
        /**
         * ground ambulance transport
         */
        AMBGRND, 
        /**
         * helicopter ambulance transport
         */
        AMBHELO, 
        /**
         * law enforcement transport
         */
        LAWENF, 
        /**
         * private transport
         */
        PRVTRN, 
        /**
         * public transport
         */
        PUBTRN, 
        /**
         * Identifies the kinds of observations that can be performed
         */
        _OBSERVATIONTYPE, 
        /**
         * Identifies the type of observation that is made about a specimen that may affect its processing, analysis or further result interpretation
         */
        _ACTSPECOBSCODE, 
        /**
         * Describes the artificial blood identifier that is associated with the specimen.
         */
        ARTBLD, 
        /**
         * An observation that reports the dilution of a sample.
         */
        DILUTION, 
        /**
         * The dilution of a sample performed by automated equipment.  The value is specified by the equipment
         */
        AUTOHIGH, 
        /**
         * The dilution of a sample performed by automated equipment.  The value is specified by the equipment
         */
        AUTOLOW, 
        /**
         * The dilution of the specimen made prior to being loaded onto analytical equipment
         */
        PRE, 
        /**
         * The value of the dilution of a sample after it had been analyzed at a prior dilution value
         */
        RERUN, 
        /**
         * Domain provides codes that qualify the ActLabObsEnvfctsCode domain. (Environmental Factors)
         */
        EVNFCTS, 
        /**
         * An observation that relates to factors that may potentially cause interference with the observation
         */
        INTFR, 
        /**
         * The Fibrin Index of the specimen. In the case of only differentiating between Absent and Present, recommend using 0 and 1
         */
        FIBRIN, 
        /**
         * An observation of the hemolysis index of the specimen in g/L
         */
        HEMOLYSIS, 
        /**
         * An observation that describes the icterus index of the specimen.  It is recommended to use mMol/L of bilirubin
         */
        ICTERUS, 
        /**
         * An observation used to describe the Lipemia Index of the specimen. It is recommended to use the optical turbidity at 600 nm (in absorbance units).
         */
        LIPEMIA, 
        /**
         * An observation that reports the volume of a sample.
         */
        VOLUME, 
        /**
         * The available quantity of specimen.   This is the current quantity minus any planned consumption (e.g., tests that are planned)
         */
        AVAILABLE, 
        /**
         * The quantity of specimen that is used each time the equipment uses this substance
         */
        CONSUMPTION, 
        /**
         * The current quantity of the specimen, i.e., initial quantity minus what has been actually used.
         */
        CURRENT, 
        /**
         * The initial quantity of the specimen in inventory
         */
        INITIAL, 
        /**
         * AnnotationType
         */
        _ANNOTATIONTYPE, 
        /**
         * Description:Provides a categorization for annotations recorded directly against the patient .
         */
        _ACTPATIENTANNOTATIONTYPE, 
        /**
         * Description:A note that is specific to a patient's diagnostic images, either historical, current or planned.
         */
        ANNDI, 
        /**
         * Description:A general or uncategorized note.
         */
        ANNGEN, 
        /**
         * A note that is specific to a patient's immunizations, either historical, current or planned.
         */
        ANNIMM, 
        /**
         * Description:A note that is specific to a patient's laboratory results, either historical, current or planned.
         */
        ANNLAB, 
        /**
         * Description:A note that is specific to a patient's medications, either historical, current or planned.
         */
        ANNMED, 
        /**
         * Description: None provided
         */
        _GENETICOBSERVATIONTYPE, 
        /**
         * Description: A DNA segment that contributes to phenotype/function. In the absence of demonstrated function a gene may be characterized by sequence, transcription or homology
         */
        GENE, 
        /**
         * Description: Observation codes which describe characteristics of the immunization material.
         */
        _IMMUNIZATIONOBSERVATIONTYPE, 
        /**
         * Description: Indicates the valid antigen count.
         */
        OBSANTC, 
        /**
         * Description: Indicates whether an antigen is valid or invalid.
         */
        OBSANTV, 
        /**
         * A code that is used to indicate the type of case safety report received from sender. The current code example reference is from the International Conference on Harmonisation (ICH) Expert Workgroup guideline on Clinical Safety Data Management: Data Elements for Transmission of Individual Case Safety Reports. The unknown/unavailable option allows the transmission of information from a secondary sender where the initial sender did not specify the type of report.

                        Example concepts include: Spontaneous, Report from study, Other.
         */
        _INDIVIDUALCASESAFETYREPORTTYPE, 
        /**
         * Indicates that the ICSR is describing problems that a patient experienced after receiving a vaccine product.
         */
        PATADVEVNT, 
        /**
         * Indicates that the ICSR is describing a problem with the actual vaccine product such as physical defects (cloudy, particulate matter) or inability to confer immunity.
         */
        VACPROBLEM, 
        /**
         * Definition:The set of LOINC codes for the act of determining the period of time that has elapsed since an entity was born or created.
         */
        _LOINCOBSERVATIONACTCONTEXTAGETYPE, 
        /**
         * Definition:Estimated age.
         */
        _216119, 
        /**
         * Definition:Reported age.
         */
        _216127, 
        /**
         * Definition:Calculated age.
         */
        _295535, 
        /**
         * Definition:General specification of age with no implied method of determination.
         */
        _305250, 
        /**
         * Definition:Age at onset of associated adverse event; no implied method of determination.
         */
        _309724, 
        /**
         * MedicationObservationType
         */
        _MEDICATIONOBSERVATIONTYPE, 
        /**
         * Description:This observation represents an 'average' or 'expected' half-life typical of the product.
         */
        REPHALFLIFE, 
        /**
         * Definition: A characteristic of an oral solid dosage form of a medicinal product, indicating whether it has one or more coatings such as sugar coating, film coating, or enteric coating.  Only coatings to the external surface or the dosage form should be considered (for example, coatings to individual pellets or granules inside a capsule or tablet are excluded from consideration).

                        
                           Constraints: The Observation.value must be a Boolean (BL) with true for the presence or false for the absence of one or more coatings on a solid dosage form.
         */
        SPLCOATING, 
        /**
         * Definition:  A characteristic of an oral solid dosage form of a medicinal product, specifying the color or colors that most predominantly define the appearance of the dose form. SPLCOLOR is not an FDA specification for the actual color of solid dosage forms or the names of colors that can appear in labeling.

                        
                           Constraints: The Observation.value must be a single coded value or a list of multiple coded values, specifying one or more distinct colors that approximate of the color(s) of distinct areas of the solid dosage form, such as the different sides of a tablet or one-part capsule, or the different halves of a two-part capsule.  Bands on banded capsules, regardless of the color, are not considered when assigning an SPLCOLOR. Imprints on the dosage form, regardless of their color are not considered when assigning an SPLCOLOR. If more than one color exists on a particular side or half, then the most predominant color on that side or half is recorded.  If the gelatin capsule shell is colorless and transparent, use the predominant color of the contents that appears through the colorless and transparent capsule shell. Colors can include: Black;Gray;White;Red;Pink;Purple;Green;Yellow;Orange;Brown;Blue;Turquoise.
         */
        SPLCOLOR, 
        /**
         * Description: A characteristic representing a single file reference that contains two or more views of the same dosage form of the product; in most cases this should represent front and back views of the dosage form, but occasionally additional views might be needed in order to capture all of the important physical characteristics of the dosage form.  Any imprint and/or symbol should be clearly identifiable, and the viewer should not normally need to rotate the image in order to read it.  Images that are submitted with SPL should be included in the same directory as the SPL file.
         */
        SPLIMAGE, 
        /**
         * Definition:  A characteristic of an oral solid dosage form of a medicinal product, specifying the alphanumeric text that appears on the solid dosage form, including text that is embossed, debossed, engraved or printed with ink. The presence of other non-textual distinguishing marks or symbols is recorded by SPLSYMBOL.

                        
                           Examples: Included in SPLIMPRINT are alphanumeric text that appears on the bands of banded capsules and logos and other symbols that can be interpreted as letters or numbers.

                        
                           Constraints: The Observation.value must be of type Character String (ST). Excluded from SPLIMPRINT are internal and external cut-outs in the form of alphanumeric text and the letter 'R' with a circle around it (when referring to a registered trademark) and the letters 'TM' (when referring to a 'trade mark').  To record text, begin on either side or part of the dosage form. Start at the top left and progress as one would normally read a book.  Enter a semicolon to show separation between words or line divisions.
         */
        SPLIMPRINT, 
        /**
         * Definition: A characteristic of an oral solid dosage form of a medicinal product, specifying the number of equal pieces that the solid dosage form can be divided into using score line(s). 

                        
                           Example: One score line creating two equal pieces is given a value of 2, two parallel score lines creating three equal pieces is given a value of 3.

                        
                           Constraints: Whether three parallel score lines create four equal pieces or two intersecting score lines create two equal pieces using one score line and four equal pieces using both score lines, both have the scoring value of 4. Solid dosage forms that are not scored are given a value of 1. Solid dosage forms that can only be divided into unequal pieces are given a null-value with nullFlavor other (OTH).
         */
        SPLSCORING, 
        /**
         * Description: A characteristic of an oral solid dosage form of a medicinal product, specifying the two dimensional representation of the solid dose form, in terms of the outside perimeter of a solid dosage form when the dosage form, resting on a flat surface, is viewed from directly above, including slight rounding of corners. SPLSHAPE does not include embossing, scoring, debossing, or internal cut-outs.  SPLSHAPE is independent of the orientation of the imprint and logo. Shapes can include: Triangle (3 sided); Square; Round; Semicircle; Pentagon (5 sided); Diamond; Double circle; Bullet; Hexagon (6 sided); Rectangle; Gear; Capsule; Heptagon (7 sided); Trapezoid; Oval; Clover; Octagon (8 sided); Tear; Freeform.
         */
        SPLSHAPE, 
        /**
         * Definition: A characteristic of an oral solid dosage form of a medicinal product, specifying the longest single dimension of the solid dosage form as a physical quantity in the dimension of length (e.g., 3 mm). The length is should be specified in millimeters and should be rounded to the nearest whole millimeter.

                        
                           Example: SPLSIZE for a rectangular shaped tablet is the length and SPLSIZE for a round shaped tablet is the diameter.
         */
        SPLSIZE, 
        /**
         * Definition: A characteristic of an oral solid dosage form of a medicinal product, to describe whether or not the medicinal product has a mark or symbol appearing on it for easy and definite recognition.  Score lines, letters, numbers, and internal and external cut-outs are not considered marks or symbols. See SPLSCORING and SPLIMPRINT for these characteristics.

                        
                           Constraints: The Observation.value must be a Boolean (BL) with <u>true</u> indicating the presence and <u>false</u> for the absence of marks or symbols.

                        
                           Example:
         */
        SPLSYMBOL, 
        /**
         * Distinguishes the kinds of coded observations that could be the trigger for clinical issue detection. These are observations that are not measurable, but instead can be defined with codes. Coded observation types include: Allergy, Intolerance, Medical Condition, Pregnancy status, etc.
         */
        _OBSERVATIONISSUETRIGGERCODEDOBSERVATIONTYPE, 
        /**
         * Code for the mechanism by which disease was acquired by the living subject involved in the public health case. Includes sexually transmitted, airborne, bloodborne, vectorborne, foodborne, zoonotic, nosocomial, mechanical, dermal, congenital, environmental exposure, indeterminate.
         */
        _CASETRANSMISSIONMODE, 
        /**
         * Communication of an agent from a living subject or environmental source to a living subject through indirect contact via oral or nasal inhalation.
         */
        AIRTRNS, 
        /**
         * Communication of an agent from one animal to another proximate animal.
         */
        ANANTRNS, 
        /**
         * Communication of an agent from an animal to a proximate person.
         */
        ANHUMTRNS, 
        /**
         * Communication of an agent from one living subject to another living subject through direct contact with any body fluid.
         */
        BDYFLDTRNS, 
        /**
         * Communication of an agent to a living subject through direct contact with blood or blood products whether the contact with blood is part of  a therapeutic procedure or not.
         */
        BLDTRNS, 
        /**
         * Communication of an agent from a living subject or environmental source to a living subject via agent migration through intact skin.
         */
        DERMTRNS, 
        /**
         * Communication of an agent from an environmental surface or source to a living subject by direct contact.
         */
        ENVTRNS, 
        /**
         * Communication of an agent from a living subject or environmental source to a living subject through oral contact with material contaminated by person or animal fecal material.
         */
        FECTRNS, 
        /**
         * Communication of an agent from an non-living material to a living subject through direct contact.
         */
        FOMTRNS, 
        /**
         * Communication of an agent from a food source to a living subject via oral consumption.
         */
        FOODTRNS, 
        /**
         * Communication of an agent from a person to a proximate person.
         */
        HUMHUMTRNS, 
        /**
         * Communication of an agent to a living subject via an undetermined route.
         */
        INDTRNS, 
        /**
         * Communication of an agent from one living subject to another living subject through direct contact with mammalian milk or colostrum.
         */
        LACTTRNS, 
        /**
         * Communication of an agent from any entity to a living subject while the living subject is in the patient role in a healthcare facility.
         */
        NOSTRNS, 
        /**
         * Communication of an agent from a living subject or environmental source to a living subject where the acquisition of the agent is not via the alimentary canal.
         */
        PARTRNS, 
        /**
         * Communication of an agent from a living subject to the progeny of that living subject via agent migration across the maternal-fetal placental membranes while in utero.
         */
        PLACTRNS, 
        /**
         * Communication of an agent from one living subject to another living subject through direct contact with genital or oral tissues as part of a sexual act.
         */
        SEXTRNS, 
        /**
         * Communication of an agent from one living subject to another living subject through direct contact with blood or blood products where the contact with blood is part of  a therapeutic procedure.
         */
        TRNSFTRNS, 
        /**
         * Communication of an agent from a living subject acting as a required intermediary in the agent transmission process to a recipient living subject via direct contact.
         */
        VECTRNS, 
        /**
         * Communication of an agent from a contaminated water source to a living subject whether the water is ingested as a food or not. The route of entry of the water may be through any bodily orifice.
         */
        WATTRNS, 
        /**
         * Codes used to define various metadata aspects of a health quality measure.
         */
        _OBSERVATIONQUALITYMEASUREATTRIBUTE, 
        /**
         * Indicates that the observation is carrying out an aggregation calculation, contained in the value element.
         */
        AGGREGATE, 
        /**
         * Identifies the organization(s) who own the intellectual property represented by the eMeasure.
         */
        COPY, 
        /**
         * Summary of relevant clinical guidelines or other clinical recommendations supporting this eMeasure.
         */
        CRS, 
        /**
         * Description of individual terms, provided as needed.
         */
        DEF, 
        /**
         * Disclaimer information for the eMeasure.
         */
        DISC, 
        /**
         * The timestamp when the eMeasure was last packaged in the Measure Authoring Tool.
         */
        FINALDT, 
        /**
         * Used to allow measure developers to provide additional guidance for implementers to understand greater specificity than could be provided in the logic for data criteria.
         */
        GUIDE, 
        /**
         * Information on whether an increase or decrease in score is the preferred result 
(e.g., a higher score indicates better quality OR a lower score indicates better quality OR quality is within a range).
         */
        IDUR, 
        /**
         * Describes the items counted by the measure (e.g., patients, encounters, procedures, etc.)
         */
        ITMCNT, 
        /**
         * A significant word that aids in discoverability.
         */
        KEY, 
        /**
         * The end date of the measurement period.
         */
        MEDT, 
        /**
         * The start date of the measurement period.
         */
        MSD, 
        /**
         * The method of adjusting for clinical severity and conditions present at the start of care that can influence patient outcomes for making valid comparisons of outcome measures across providers. Indicates whether an eMeasure is subject to the statistical process for reducing, removing, or clarifying the influences of confounding factors to allow more useful comparisons.
         */
        MSRADJ, 
        /**
         * Describes how to combine information calculated based on logic in each of several populations into one summarized result. It can also be used to describe how to risk adjust the data based on supplemental data elements described in the eMeasure. (e.g., pneumonia hospital measures antibiotic selection in the ICU versus non-ICU and then the roll-up of the two). 

                        
                           Open Issue: The description does NOT align well with the definition used in the HQMF specfication; correct the MSGAGG definition, and the possible distinction of MSRAGG as a child of AGGREGATE.
         */
        MSRAGG, 
        /**
         * Information on whether an increase or decrease in score is the preferred result. This should reflect information on which way is better, an increase or decrease in score.
         */
        MSRIMPROV, 
        /**
         * The list of jurisdiction(s) for which the measure applies.
         */
        MSRJUR, 
        /**
         * Type of person or organization that is expected to report the issue.
         */
        MSRRPTR, 
        /**
         * The maximum time that may elapse following completion of the measure until the measure report must be sent to the receiver.
         */
        MSRRPTTIME, 
        /**
         * Indicates how the calculation is performed for the eMeasure 
(e.g., proportion, continuous variable, ratio)
         */
        MSRSCORE, 
        /**
         * Location(s) in which care being measured is rendered

                        Usage Note: MSRSET is used rather than RoleCode because the setting applies to what is being measured, as opposed to participating directly in the health quality measure documantion itself).
         */
        MSRSET, 
        /**
         * health quality measure topic type
         */
        MSRTOPIC, 
        /**
         * The time period for which the eMeasure applies.
         */
        MSRTP, 
        /**
         * Indicates whether the eMeasure is used to examine a process or an outcome over time 
(e.g., Structure, Process, Outcome).
         */
        MSRTYPE, 
        /**
         * Succinct statement of the need for the measure. Usually includes statements pertaining to Importance criterion: impact, gap in care and evidence.
         */
        RAT, 
        /**
         * Identifies bibliographic citations or references to clinical practice guidelines, sources of evidence, or other relevant materials supporting the intent and rationale of the eMeasure.
         */
        REF, 
        /**
         * Comparison of results across strata can be used to show where disparities exist or where there is a need to expose differences in results. For example, Centers for Medicare & Medicaid Services (CMS) in the U.S. defines four required Supplemental Data Elements (payer, ethnicity, race, and gender), which are variables used to aggregate data into various subgroups. Additional supplemental data elements required for risk adjustment or other purposes of data aggregation can be included in the Supplemental Data Element section.
         */
        SDE, 
        /**
         * Describes the strata for which the measure is to be evaluated. There are three examples of reasons for stratification based on existing work. These include: (1) evaluate the measure based on different age groupings within the population described in the measure (e.g., evaluate the whole [age 14-25] and each sub-stratum [14-19] and [20-25]); (2) evaluate the eMeasure based on either a specific condition, a specific discharge location, or both; (3) evaluate the eMeasure based on different locations within a facility (e.g., evaluate the overall rate for all intensive care units and also some strata include additional findings [specific birth weights for neonatal intensive care units]).
         */
        STRAT, 
        /**
         * Can be a URL or hyperlinks that link to the transmission formats that are specified for a particular reporting program.
         */
        TRANF, 
        /**
         * Usage notes.
         */
        USE, 
        /**
         * ObservationSequenceType
         */
        _OBSERVATIONSEQUENCETYPE, 
        /**
         * A sequence of values in the "absolute" time domain.  This is the same time domain that all HL7 timestamps use.  It is time as measured by the Gregorian calendar
         */
        TIMEABSOLUTE, 
        /**
         * A sequence of values in a "relative" time domain.  The time is measured relative to the earliest effective time in the Observation Series containing this sequence.
         */
        TIMERELATIVE, 
        /**
         * ObservationSeriesType
         */
        _OBSERVATIONSERIESTYPE, 
        /**
         * ECGObservationSeriesType
         */
        _ECGOBSERVATIONSERIESTYPE, 
        /**
         * This Observation Series type contains waveforms of a "representative beat" (a.k.a. "median beat" or "average beat").  The waveform samples are measured in relative time, relative to the beginning of the beat as defined by the Observation Series effective time.  The waveforms are not directly acquired from the subject, but rather algorithmically derived from the "rhythm" waveforms.
         */
        REPRESENTATIVEBEAT, 
        /**
         * This Observation type contains ECG "rhythm" waveforms.  The waveform samples are measured in absolute time (a.k.a. "subject time" or "effective time").  These waveforms are usually "raw" with some minimal amount of noise reduction and baseline filtering applied.
         */
        RHYTHM, 
        /**
         * Description: Reporting codes that are related to an immunization event.
         */
        _PATIENTIMMUNIZATIONRELATEDOBSERVATIONTYPE, 
        /**
         * Description: The class room associated with the patient during the immunization event.
         */
        CLSSRM, 
        /**
         * Description: The school grade or level the patient was in when immunized.
         */
        GRADE, 
        /**
         * Description: The school the patient attended when immunized.
         */
        SCHL, 
        /**
         * Description: The school division or district associated with the patient during the immunization event.
         */
        SCHLDIV, 
        /**
         * Description: The patient's teacher when immunized.
         */
        TEACHER, 
        /**
         * Observation types for specifying criteria used to assert that a subject is included in a particular population.
         */
        _POPULATIONINCLUSIONOBSERVATIONTYPE, 
        /**
         * Criteria which specify subjects who should be removed from the eMeasure population and denominator before determining if numerator criteria are met. Denominator exclusions are used in proportion and ratio measures to help narrow the denominator.
         */
        DENEX, 
        /**
         * Criteria which specify the removal of a subject, procedure or unit of measurement from the denominator, only if the numerator criteria are not met. Denominator exceptions allow for adjustment of the calculated score for those providers with higher risk populations. Denominator exceptions are used only in proportion eMeasures. They are not appropriate for ratio or continuous variable eMeasures. Denominator exceptions allow for the exercise of clinical judgment and should be specifically defined where capturing the information in a structured manner fits the clinical workflow. Generic denominator exception reasons used in proportion eMeasures fall into three general categories:

                        
                           Medical reasons
                           Patient (or subject) reasons
                           System reasons
         */
        DENEXCEP, 
        /**
         * Criteria for specifying the entities to be evaluated by a specific quality measure, based on a shared common set of characteristics (within a specific measurement set to which a given measure belongs).  The denominator can be the same as the initial population, or it may be a subset of the initial population to further constrain it for the purpose of the eMeasure. Different measures within an eMeasure set may have different denominators. Continuous Variable eMeasures do not have a denominator, but instead define a measure population.
         */
        DENOM, 
        /**
         * Criteria for specifying the entities to be evaluated by a specific quality measure, based on a shared common set of characteristics (within a specific measurement set to which a given measure belongs).
         */
        IPOP, 
        /**
         * Criteria for specifying the patients to be evaluated by a specific quality measure, based on a shared common set of characteristics (within a specific measurement set to which a given measure belongs). Details often include information based upon specific age groups, diagnoses, diagnostic and procedure codes, and enrollment periods.
         */
        IPPOP, 
        /**
         * Criteria for specifying
the measure population as a narrative description (e.g., all patients seen in the Emergency Department during the measurement period).  This is used only in continuous variable eMeasures.
         */
        MSRPOPL, 
        /**
         * Criteria for specifying subjects who should be removed from the eMeasure's Initial Population and Measure Population. Measure Population Exclusions are used in Continuous Variable measures to help narrow the Measure Population before determining the value(s) of the continuous variable(s).
         */
        MSRPOPLEX, 
        /**
         * Criteria for specifying the processes or outcomes expected for each patient, procedure, or other unit of measurement defined in the denominator for proportion measures, or related to (but not directly derived from) the denominator for ratio measures (e.g., a numerator listing the number of central line blood stream infections and a denominator indicating the days per thousand of central line usage in a specific time period).
         */
        NUMER, 
        /**
         * Criteria for specifying instances that should not be included in the numerator data. (e.g., if the number of central line blood stream infections per 1000 catheter days were to exclude infections with a specific bacterium, that bacterium would be listed as a numerator exclusion).  Numerator Exclusions are used only in ratio eMeasures.
         */
        NUMEX, 
        /**
         * Types of observations that can be made about Preferences.
         */
        _PREFERENCEOBSERVATIONTYPE, 
        /**
         * An observation about how important a preference is to the target of the preference.
         */
        PREFSTRENGTH, 
        /**
         * Indicates that the observation is of an unexpected negative occurrence in the subject suspected to result from the subject's exposure to one or more agents.  Observation values would be the symptom resulting from the reaction.
         */
        ADVERSEREACTION, 
        /**
         * Description:Refines classCode OBS to indicate an observation in which observation.value contains a finding or other nominalized statement, where the encoded information in Observation.value is not altered by Observation.code.  For instance, observation.code="ASSERTION" and observation.value="fracture of femur present" is an assertion of a clinical finding of femur fracture.
         */
        ASSERTION, 
        /**
         * Definition:An observation that provides a characterization of the level of harm to an investigation subject as a result of a reaction or event.
         */
        CASESER, 
        /**
         * An observation that states whether the disease was likely acquired outside the jurisdiction of observation, and if so, the nature of the inter-jurisdictional relationship.

                        
                           OpenIssue: This code could be moved to LOINC if it can be done before there are significant implemenations using it.
         */
        CDIO, 
        /**
         * A clinical judgment as to the worst case result of a future exposure (including substance administration). When the worst case result is assessed to have a life-threatening or organ system threatening potential, it is considered to be of high criticality.
         */
        CRIT, 
        /**
         * An observation that states the mechanism by which disease was acquired by the living subject involved in the public health case.

                        
                           OpenIssue: This code could be moved to LOINC if it can be done before there are significant implemenations using it.
         */
        CTMO, 
        /**
         * Includes all codes defining types of indications such as diagnosis, symptom and other indications such as contrast agents for lab tests.
         */
        DX, 
        /**
         * Admitting diagnosis are the diagnoses documented  for administrative purposes as the basis for a hospital admission.
         */
        ADMDX, 
        /**
         * Discharge diagnosis are the diagnoses documented for administrative purposes as the time of hospital discharge.
         */
        DISDX, 
        /**
         * Intermediate diagnoses are those diagnoses documented for administrative purposes during the course of a hospital stay.
         */
        INTDX, 
        /**
         * The type of injury that the injury coding specifies.
         */
        NOI, 
        /**
         * Description: Accuracy determined as per the GIS tier code system.
         */
        GISTIER, 
        /**
         * Indicates that the observation is of a person’s living situation in a household including the household composition and circumstances.
         */
        HHOBS, 
        /**
         * There is a clinical issue for the therapy that makes continuation of the therapy inappropriate.

                        
                           Open Issue: The definition of this code does not correctly represent the concept space of its specializations (children)
         */
        ISSUE, 
        /**
         * Identifies types of detectyed issues for Act class "ALRT" for the administrative and patient administrative acts domains.
         */
        _ACTADMINISTRATIVEDETECTEDISSUECODE, 
        /**
         * ActAdministrativeAuthorizationDetectedIssueCode
         */
        _ACTADMINISTRATIVEAUTHORIZATIONDETECTEDISSUECODE, 
        /**
         * The requesting party has insufficient authorization to invoke the interaction.
         */
        NAT, 
        /**
         * Description: One or more records in the query response have been suppressed due to consent or privacy restrictions.
         */
        SUPPRESSED, 
        /**
         * Description:The specified element did not pass business-rule validation.
         */
        VALIDAT, 
        /**
         * The ID of the patient, order, etc., was not found. Used for transactions other than additions, e.g. transfer of a non-existent patient.
         */
        KEY204, 
        /**
         * The ID of the patient, order, etc., already exists. Used in response to addition transactions (Admit, New Order, etc.).
         */
        KEY205, 
        /**
         * There may be an issue with the patient complying with the intentions of the proposed therapy
         */
        COMPLY, 
        /**
         * The proposed therapy appears to duplicate an existing therapy
         */
        DUPTHPY, 
        /**
         * Description:The proposed therapy appears to have the same intended therapeutic benefit as an existing therapy, though the specific mechanisms of action vary.
         */
        DUPTHPCLS, 
        /**
         * Description:The proposed therapy appears to have the same intended therapeutic benefit as an existing therapy and uses the same mechanisms of action as the existing therapy.
         */
        DUPTHPGEN, 
        /**
         * Description:The proposed therapy is frequently misused or abused and therefore should be used with caution and/or monitoring.
         */
        ABUSE, 
        /**
         * Description:The request is suspected to have a fraudulent basis.
         */
        FRAUD, 
        /**
         * A similar or identical therapy was recently ordered by a different practitioner.
         */
        PLYDOC, 
        /**
         * This patient was recently supplied a similar or identical therapy from a different pharmacy or supplier.
         */
        PLYPHRM, 
        /**
         * Proposed dosage instructions for therapy differ from standard practice.
         */
        DOSE, 
        /**
         * Description:Proposed dosage is inappropriate due to patient's medical condition.
         */
        DOSECOND, 
        /**
         * Proposed length of therapy differs from standard practice.
         */
        DOSEDUR, 
        /**
         * Proposed length of therapy is longer than standard practice
         */
        DOSEDURH, 
        /**
         * Proposed length of therapy is longer than standard practice for the identified indication or diagnosis
         */
        DOSEDURHIND, 
        /**
         * Proposed length of therapy is shorter than that necessary for therapeutic effect
         */
        DOSEDURL, 
        /**
         * Proposed length of therapy is shorter than standard practice for the identified indication or diagnosis
         */
        DOSEDURLIND, 
        /**
         * Proposed dosage exceeds standard practice
         */
        DOSEH, 
        /**
         * Proposed dosage exceeds standard practice for the patient's age
         */
        DOSEHINDA, 
        /**
         * High Dose for Indication Alert
         */
        DOSEHIND, 
        /**
         * Proposed dosage exceeds standard practice for the patient's height or body surface area
         */
        DOSEHINDSA, 
        /**
         * Proposed dosage exceeds standard practice for the patient's weight
         */
        DOSEHINDW, 
        /**
         * Proposed dosage interval/timing differs from standard practice
         */
        DOSEIVL, 
        /**
         * Proposed dosage interval/timing differs from standard practice for the identified indication or diagnosis
         */
        DOSEIVLIND, 
        /**
         * Proposed dosage is below suggested therapeutic levels
         */
        DOSEL, 
        /**
         * Proposed dosage is below suggested therapeutic levels for the patient's age
         */
        DOSELINDA, 
        /**
         * Low Dose for Indication Alert
         */
        DOSELIND, 
        /**
         * Proposed dosage is below suggested therapeutic levels for the patient's height or body surface area
         */
        DOSELINDSA, 
        /**
         * Proposed dosage is below suggested therapeutic levels for the patient's weight
         */
        DOSELINDW, 
        /**
         * Description:The maximum quantity of this drug allowed to be administered within a particular time-range (month, year, lifetime) has been reached or exceeded.
         */
        MDOSE, 
        /**
         * Proposed therapy may be inappropriate or contraindicated due to conditions or characteristics of the patient
         */
        OBSA, 
        /**
         * Proposed therapy may be inappropriate or contraindicated due to patient age
         */
        AGE, 
        /**
         * Proposed therapy is outside of the standard practice for an adult patient.
         */
        ADALRT, 
        /**
         * Proposed therapy is outside of standard practice for a geriatric patient.
         */
        GEALRT, 
        /**
         * Proposed therapy is outside of the standard practice for a pediatric patient.
         */
        PEALRT, 
        /**
         * Proposed therapy may be inappropriate or contraindicated due to an existing/recent patient condition or diagnosis
         */
        COND, 
        /**
         * null
         */
        HGHT, 
        /**
         * Proposed therapy may be inappropriate or contraindicated when breast-feeding
         */
        LACT, 
        /**
         * Proposed therapy may be inappropriate or contraindicated during pregnancy
         */
        PREG, 
        /**
         * null
         */
        WGHT, 
        /**
         * Description:Proposed therapy may be inappropriate or contraindicated because of a common but non-patient specific reaction to the product.

                        
                           Example:There is no record of a specific sensitivity for the patient, but the presence of the sensitivity is common and therefore caution is warranted.
         */
        CREACT, 
        /**
         * Proposed therapy may be inappropriate or contraindicated due to patient genetic indicators.
         */
        GEN, 
        /**
         * Proposed therapy may be inappropriate or contraindicated due to patient gender.
         */
        GEND, 
        /**
         * Proposed therapy may be inappropriate or contraindicated due to recent lab test results
         */
        LAB, 
        /**
         * Proposed therapy may be inappropriate or contraindicated based on the potential for a patient reaction to the proposed product
         */
        REACT, 
        /**
         * Proposed therapy may be inappropriate or contraindicated because of a recorded patient allergy to the proposed product.  (Allergies are immune based reactions.)
         */
        ALGY, 
        /**
         * Proposed therapy may be inappropriate or contraindicated because of a recorded patient intolerance to the proposed product.  (Intolerances are non-immune based sensitivities.)
         */
        INT, 
        /**
         * Proposed therapy may be inappropriate or contraindicated because of a potential patient reaction to a cross-sensitivity related product.
         */
        RREACT, 
        /**
         * Proposed therapy may be inappropriate or contraindicated because of a recorded patient allergy to a cross-sensitivity related product.  (Allergies are immune based reactions.)
         */
        RALG, 
        /**
         * Proposed therapy may be inappropriate or contraindicated because of a recorded prior adverse reaction to a cross-sensitivity related product.
         */
        RAR, 
        /**
         * Proposed therapy may be inappropriate or contraindicated because of a recorded patient intolerance to a cross-sensitivity related product.  (Intolerances are non-immune based sensitivities.)
         */
        RINT, 
        /**
         * Description:A local business rule relating multiple elements has been violated.
         */
        BUS, 
        /**
         * Description:The specified code is not valid against the list of codes allowed for the element.
         */
        CODEINVAL, 
        /**
         * Description:The specified code has been deprecated and should no longer be used.  Select another code from the code system.
         */
        CODEDEPREC, 
        /**
         * Description:The element does not follow the formatting or type rules defined for the field.
         */
        FORMAT, 
        /**
         * Description:The request is missing elements or contains elements which cause it to not meet the legal standards for actioning.
         */
        ILLEGAL, 
        /**
         * Description:The length of the data specified falls out of the range defined for the element.
         */
        LENRANGE, 
        /**
         * Description:The length of the data specified is greater than the maximum length defined for the element.
         */
        LENLONG, 
        /**
         * Description:The length of the data specified is less than the minimum length defined for the element.
         */
        LENSHORT, 
        /**
         * Description:The specified element must be specified with a non-null value under certain conditions.  In this case, the conditions are true but the element is still missing or null.
         */
        MISSCOND, 
        /**
         * Description:The specified element is mandatory and was not included in the instance.
         */
        MISSMAND, 
        /**
         * Description:More than one element with the same value exists in the set.  Duplicates not permission in this set in a set.
         */
        NODUPS, 
        /**
         * Description: Element in submitted message will not persist in data storage based on detected issue.
         */
        NOPERSIST, 
        /**
         * Description:The number of repeating elements falls outside the range of the allowed number of repetitions.
         */
        REPRANGE, 
        /**
         * Description:The number of repeating elements is above the maximum number of repetitions allowed.
         */
        MAXOCCURS, 
        /**
         * Description:The number of repeating elements is below the minimum number of repetitions allowed.
         */
        MINOCCURS, 
        /**
         * ActAdministrativeRuleDetectedIssueCode
         */
        _ACTADMINISTRATIVERULEDETECTEDISSUECODE, 
        /**
         * Description: Metadata associated with the identification (e.g. name or gender) does not match the identification being verified.
         */
        KEY206, 
        /**
         * Description: One or more records in the query response have a status of 'obsolete'.
         */
        OBSOLETE, 
        /**
         * Identifies types of detected issues regarding the administration or supply of an item to a patient.
         */
        _ACTSUPPLIEDITEMDETECTEDISSUECODE, 
        /**
         * Administration of the proposed therapy may be inappropriate or contraindicated as proposed
         */
        _ADMINISTRATIONDETECTEDISSUECODE, 
        /**
         * AppropriatenessDetectedIssueCode
         */
        _APPROPRIATENESSDETECTEDISSUECODE, 
        /**
         * InteractionDetectedIssueCode
         */
        _INTERACTIONDETECTEDISSUECODE, 
        /**
         * Proposed therapy may interact with certain foods
         */
        FOOD, 
        /**
         * Proposed therapy may interact with an existing or recent therapeutic product
         */
        TPROD, 
        /**
         * Proposed therapy may interact with an existing or recent drug therapy
         */
        DRG, 
        /**
         * Proposed therapy may interact with existing or recent natural health product therapy
         */
        NHP, 
        /**
         * Proposed therapy may interact with a non-prescription drug (e.g. alcohol, tobacco, Aspirin)
         */
        NONRX, 
        /**
         * Definition:The same or similar treatment has previously been attempted with the patient without achieving a positive effect.
         */
        PREVINEF, 
        /**
         * Description:Proposed therapy may be contraindicated or ineffective based on an existing or recent drug therapy.
         */
        DACT, 
        /**
         * Description:Proposed therapy may be inappropriate or ineffective based on the proposed start or end time.
         */
        TIME, 
        /**
         * Definition:Proposed therapy may be inappropriate or ineffective because the end of administration is too close to another planned therapy.
         */
        ALRTENDLATE, 
        /**
         * Definition:Proposed therapy may be inappropriate or ineffective because the start of administration is too late after the onset of the condition.
         */
        ALRTSTRTLATE, 
        /**
         * Supplying the product at this time may be inappropriate or indicate compliance issues with the associated therapy
         */
        _SUPPLYDETECTEDISSUECODE, 
        /**
         * Definition:The requested action has already been performed and so this request has no effect
         */
        ALLDONE, 
        /**
         * Definition:The therapy being performed is in some way out of alignment with the requested therapy.
         */
        FULFIL, 
        /**
         * Definition:The status of the request being fulfilled has changed such that it is no longer actionable.  This may be because the request has expired, has already been completely fulfilled or has been otherwise stopped or disabled.  (Not used for 'suspended' orders.)
         */
        NOTACTN, 
        /**
         * Definition:The therapy being performed is not sufficiently equivalent to the therapy which was requested.
         */
        NOTEQUIV, 
        /**
         * Definition:The therapy being performed is not generically equivalent (having the identical biological action) to the therapy which was requested.
         */
        NOTEQUIVGEN, 
        /**
         * Definition:The therapy being performed is not therapeutically equivalent (having the same overall patient effect) to the therapy which was requested.
         */
        NOTEQUIVTHER, 
        /**
         * Definition:The therapy is being performed at a time which diverges from the time the therapy was requested
         */
        TIMING, 
        /**
         * Definition:The therapy action is being performed outside the bounds of the time period requested
         */
        INTERVAL, 
        /**
         * Definition:The therapy action is being performed too soon after the previous occurrence based on the requested frequency
         */
        MINFREQ, 
        /**
         * Definition:There should be no actions taken in fulfillment of a request that has been held or suspended.
         */
        HELD, 
        /**
         * The patient is receiving a subsequent fill significantly later than would be expected based on the amount previously supplied and the therapy dosage instructions
         */
        TOOLATE, 
        /**
         * The patient is receiving a subsequent fill significantly earlier than would be expected based on the amount previously supplied and the therapy dosage instructions
         */
        TOOSOON, 
        /**
         * Description: While the record was accepted in the repository, there is a more recent version of a record of this type.
         */
        HISTORIC, 
        /**
         * Definition:The proposed therapy goes against preferences or consent constraints recorded in the patient's record.
         */
        PATPREF, 
        /**
         * Definition:The proposed therapy goes against preferences or consent constraints recorded in the patient's record.  An alternate therapy meeting those constraints is available.
         */
        PATPREFALT, 
        /**
         * Categorization of types of observation that capture the main clinical knowledge subject which may be a medication, a laboratory test, a disease.
         */
        KSUBJ, 
        /**
         * Categorization of types of observation that capture a knowledge subtopic which might be treatment, etiology, or prognosis.
         */
        KSUBT, 
        /**
         * Hypersensitivity resulting in an adverse reaction upon exposure to an agent.
         */
        OINT, 
        /**
         * Hypersensitivity to an agent caused by an immunologic response to an initial exposure
         */
        ALG, 
        /**
         * An allergy to a pharmaceutical product.
         */
        DALG, 
        /**
         * An allergy to a substance other than a drug or a food.  E.g. Latex, pollen, etc.
         */
        EALG, 
        /**
         * An allergy to a substance generally consumed for nutritional purposes.
         */
        FALG, 
        /**
         * Hypersensitivity resulting in an adverse reaction upon exposure to a drug.
         */
        DINT, 
        /**
         * Hypersensitivity to an agent caused by a mechanism other than an immunologic response to an initial exposure
         */
        DNAINT, 
        /**
         * Hypersensitivity resulting in an adverse reaction upon exposure to environmental conditions.
         */
        EINT, 
        /**
         * Hypersensitivity to an agent caused by a mechanism other than an immunologic response to an initial exposure
         */
        ENAINT, 
        /**
         * Hypersensitivity resulting in an adverse reaction upon exposure to food.
         */
        FINT, 
        /**
         * Hypersensitivity to an agent caused by a mechanism other than an immunologic response to an initial exposure
         */
        FNAINT, 
        /**
         * Hypersensitivity to an agent caused by a mechanism other than an immunologic response to an initial exposure
         */
        NAINT, 
        /**
         * A subjective evaluation of the seriousness or intensity associated with another observation.
         */
        SEV, 
        /**
         * Shape of the region on the object being referenced
         */
        _ROIOVERLAYSHAPE, 
        /**
         * A circle defined by two (column,row) pairs. The first point is the center of the circle and the second point is a point on the perimeter of the circle.
         */
        CIRCLE, 
        /**
         * An ellipse defined by four (column,row) pairs, the first two points specifying the endpoints of the major axis and the second two points specifying the endpoints of the minor axis.
         */
        ELLIPSE, 
        /**
         * A single point denoted by a single (column,row) pair, or multiple points each denoted by a (column,row) pair.
         */
        POINT, 
        /**
         * A series of connected line segments with ordered vertices denoted by (column,row) pairs; if the first and last vertices are the same, it is a closed polygon.
         */
        POLY, 
        /**
         * Description:Indicates that result data has been corrected.
         */
        C, 
        /**
         * Code set to define specialized/allowed diets
         */
        DIET, 
        /**
         * A diet exclusively composed of oatmeal, semolina, or rice, to be extremely easy to eat and digest.
         */
        BR, 
        /**
         * A diet that uses carbohydrates sparingly.  Typically with a restriction in daily energy content (e.g. 1600-2000 kcal).
         */
        DM, 
        /**
         * No enteral intake of foot or liquids  whatsoever, no smoking.  Typically 6 to 8 hours before anesthesia.
         */
        FAST, 
        /**
         * A diet consisting of a formula feeding, either for an infant or an adult, to provide nutrition either orally or through the gastrointestinal tract via tube, catheter or stoma.
         */
        FORMULA, 
        /**
         * Gluten free diet for celiac disease.
         */
        GF, 
        /**
         * A diet low in fat, particularly to patients with hepatic diseases.
         */
        LF, 
        /**
         * A low protein diet for patients with renal failure.
         */
        LP, 
        /**
         * A strictly liquid diet, that can be fully absorbed in the intestine, and therefore may not contain fiber.  Used before enteral surgeries.
         */
        LQ, 
        /**
         * A diet low in sodium for patients with congestive heart failure and/or renal failure.
         */
        LS, 
        /**
         * A normal diet, i.e. no special preparations or restrictions for medical reasons. This is notwithstanding any preferences the patient might have regarding special foods, such as vegetarian, kosher, etc.
         */
        N, 
        /**
         * A no fat diet for acute hepatic diseases.
         */
        NF, 
        /**
         * Phenylketonuria diet.
         */
        PAF, 
        /**
         * Patient is supplied with parenteral nutrition, typically described in terms of i.v. medications.
         */
        PAR, 
        /**
         * A diet that seeks to reduce body fat, typically low energy content (800-1600 kcal).
         */
        RD, 
        /**
         * A diet that avoids ingredients that might cause digestion problems, e.g., avoid excessive fat, avoid too much fiber (cabbage, peas, beans).
         */
        SCH, 
        /**
         * A diet that is not intended to be complete but is added to other diets.
         */
        SUPPLEMENT, 
        /**
         * This is not really a diet, since it contains little nutritional value, but is essentially just water.  Used before coloscopy examinations.
         */
        T, 
        /**
         * Diet with low content of the amino-acids valin, leucin, and isoleucin, for "maple syrup disease."
         */
        VLI, 
        /**
         * Definition: A public or government health program that administers and funds coverage for prescription drugs to assist program eligible who meet financial and health status criteria.
         */
        DRUGPRG, 
        /**
         * Description:Indicates that a result is complete.  No further results are to come.  This maps to the 'complete' state in the observation result status code.
         */
        F, 
        /**
         * Description:Indicates that a result is incomplete.  There are further results to come.  This maps to the 'active' state in the observation result status code.
         */
        PRLMN, 
        /**
         * An observation identifying security metadata about an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.  Security metadata are used to name security labels.  

                        
                           Rationale: According to ISO/TS 22600-3:2009(E) A.9.1.7 SECURITY LABEL MATCHING, Security label matching compares the initiator's clearance to the target's security label.  All of the following must be true for authorization to be granted:

                        
                           The security policy identifiers shall be identical
                           The classification level of the initiator shall be greater than or equal to that of the target (that is, there shall be at least one value in the classification list of the clearance greater than or equal to the classification of the target), and 
                           For each security category in the target label, there shall be a security category of the same type in the initiator's clearance and the initiator's classification level shall dominate that of the target.
                        
                        
                           Examples: SecurityObservationType  security label fields include:

                        
                           Confidentiality classification
                           Compartment category
                           Sensitivity category
                           Security mechanisms used to ensure data integrity or to perform authorized data transformation
                           Indicators of an IT resource completeness, veracity, reliability, trustworthiness, or provenance.
                        
                        
                           Usage Note: SecurityObservationType codes designate security label field types, which are valued with an applicable SecurityObservationValue code as the "security label tag".
         */
        SECOBS, 
        /**
         * Type of security metadata observation made about the category of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions. Security category metadata is defined by ISO/IEC 2382-8:1998(E/F)/ T-REC-X.812-1995 as: "A nonhierarchical grouping of sensitive information used to control access to data more finely than with hierarchical security classification alone."

                        
                           Rationale: A security category observation supports requirement to specify the type of IT resource to facilitate application of appropriate levels of information security according to a range of levels of impact or consequences that might result from the unauthorized disclosure, modification, or use of the information or information system.  A resource is assigned to a specific category of information (e.g., privacy, medical, proprietary, financial, investigative, contractor sensitive, security management) defined by an organization or in some instances, by a specific law, Executive Order, directive, policy, or regulation. [FIPS 199]

                        
                           Examples: Types of security categories include:

                        
                           Compartment:  A division of data into isolated blocks with separate security controls for the purpose of reducing risk. (ISO 2382-8).  A security label tag that "segments" an IT resource by indicating that access and use is restricted to members of a defined community or project. (HL7 Healthcare Classification System)  
                           Sensitivity:  The characteristic of an IT resource which implies its value or importance and may include its vulnerability. (ISO 7492-2)  Privacy metadata for information perceived as undesirable to share.  (HL7 Healthcare Classification System)
         */
        SECCATOBS, 
        /**
         * Type of security metadata observation made about the classification of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.  Security classification is defined by ISO/IEC 2382-8:1998(E/F)/ T-REC-X.812-1995 as: "The determination of which specific degree of protection against access the data or information requires, together with a designation of that degree of protection."  Security classification metadata is based on an analysis of applicable policies and the risk of financial, reputational, or other harm that could result from unauthorized disclosure.

                        
                           Rationale: A security classification observation may indicate that the confidentiality level indicated by an Act or Role confidentiality attribute has been overridden by the entity responsible for ascribing the SecurityClassificationObservationValue.  This supports the business requirement for increasing or decreasing the level of confidentiality (classification or declassification) based on parameters beyond the original assignment of an Act or Role confidentiality.

                        
                           Examples: Types of security classification include: HL7 Confidentiality Codes such as very restricted, unrestricted, and normal.  Intelligence community examples include top secret, secret, and confidential.

                        
                           Usage Note: Security classification observation type codes designate security label field types, which are valued with an applicable SecurityClassificationObservationValue code as the "security label tag".
         */
        SECCLASSOBS, 
        /**
         * Type of security metadata observation made about the control of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.  Security control metadata convey instructions to users and receivers for secure distribution, transmission, and storage; dictate obligations or mandated actions; specify any action prohibited by refrain policy such as dissemination controls; and stipulate the permissible purpose of use of an IT resource.  

                        
                           Rationale: A security control observation supports requirement to specify applicable management, operational, and technical controls (i.e., safeguards or countermeasures) prescribed for an information system to protect the confidentiality, integrity, and availability of the system and its information. [FIPS 199]

                        
                           Examples: Types of security control metadata include: 

                        
                           handling caveats
                           dissemination controls
                           obligations
                           refrain policies
                           purpose of use constraints
         */
        SECCONOBS, 
        /**
         * Type of security metadata observation made about the integrity of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.

                        
                           Rationale: A security integrity observation supports the requirement to guard against improper information modification or destruction, and includes ensuring information non-repudiation and authenticity. (44 U.S.C., SEC. 3542)

                        
                           Examples: Types of security integrity metadata include: 

                        
                           Integrity status, which indicates the completeness or workflow status of an IT resource (data, information object, service, or system capability)
                           Integrity confidence, which indicates the reliability and trustworthiness of an IT resource
                           Integrity control, which indicates pertinent handling caveats, obligations, refrain policies, and purpose of use for  the resource
                           Data integrity, which indicate the security mechanisms used to ensure that the accuracy and consistency are preserved regardless of changes made (ISO/IEC DIS 2382-8)
                           Alteration integrity, which indicate the security mechanisms used for authorized transformations of the resource
                           Integrity provenance, which indicates the entity responsible for a report or assertion relayed "second-hand" about an IT resource
         */
        SECINTOBS, 
        /**
         * Type of security metadata observation made about the alteration integrity of an IT resource (data, information object, service, or system capability), which indicates the mechanism used for authorized transformations of the resource.

                        
                           Examples: Types of security alteration integrity observation metadata, which may value the observation with a code used to indicate the mechanism used for authorized transformation of an IT resource, including: 

                        
                           translation
                           syntactic transformation
                           semantic mapping
                           redaction
                           masking
                           pseudonymization
                           anonymization
         */
        SECALTINTOBS, 
        /**
         * Type of security metadata observation made about the data integrity of an IT resource (data, information object, service, or system capability), which indicates the security mechanism used to preserve resource accuracy and consistency.  Data integrity is defined by ISO 22600-23.3.21 as: "The property that data has not been altered or destroyed in an unauthorized manner", and by ISO/IEC 2382-8:  The property of data whose accuracy and consistency are preserved regardless of changes made."

                        
                           Examples: Types of security data integrity observation metadata, which may value the observation, include cryptographic hash function and digital signature.
         */
        SECDATINTOBS, 
        /**
         * Type of security metadata observation made about the integrity confidence of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.

                        
                           Examples: Types of security integrity confidence observation metadata, which may value the observation, include highly reliable, uncertain reliability, and not reliable.

                        
                           Usage Note: A security integrity confidence observation on an Act may indicate that a valued Act.uncertaintycode attribute has been overridden by the entity responsible for ascribing the SecurityIntegrityConfidenceObservationValue.  This supports the business requirements for increasing or decreasing the assessment of the reliability or trustworthiness of an IT resource based on parameters beyond the original assignment of an Act statement level of uncertainty.
         */
        SECINTCONOBS, 
        /**
         * Type of security metadata observation made about the provenance integrity of an IT resource (data, information object, service, or system capability), which indicates the lifecycle completeness of an IT resource in terms of workflow status such as its creation, modification, suspension, and deletion; locations in which the resource has been collected or archived, from which it may be retrieved, and the history of its distribution and disclosure.  Integrity provenance metadata about an IT resource may be used to assess its veracity, reliability, and trustworthiness.

                        
                           Examples: Types of security integrity provenance observation metadata, which may value the observation about an IT resource, include: 

                        
                           completeness or workflow status, such as authentication
                           the entity responsible for original authoring or informing about an IT resource
                           the entity responsible for a report or assertion about an IT resource relayed â€œsecond-handâ€?
                           the entity responsible for excerpting, transforming, or compiling an IT resource
         */
        SECINTPRVOBS, 
        /**
         * Type of security metadata observation made about the integrity provenance of an IT resource (data, information object, service, or system capability), which indicates the entity that made assertions about the resource.  The asserting entity may not be the original informant about the resource.

                        
                           Examples: Types of security integrity provenance asserted by observation metadata, which may value the observation, including: 

                        
                           assertions about an IT resource by a patient
                           assertions about an IT resource by a clinician
                           assertions about an IT resource by a device
         */
        SECINTPRVABOBS, 
        /**
         * Type of security metadata observation made about the integrity provenance of an IT resource (data, information object, service, or system capability), which indicates the entity that reported the existence of the resource.  The reporting entity may not be the original author of the resource.

                        
                           Examples: Types of security integrity provenance reported by observation metadata, which may value the observation, include: 

                        
                           reports about an IT resource by a patient
                           reports about an IT resource by a clinician
                           reports about an IT resource by a device
         */
        SECINTPRVRBOBS, 
        /**
         * Type of security metadata observation made about the integrity status of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.  Indicates the completeness of an IT resource in terms of workflow status, which may impact users that are authorized to access and use the resource.

                        
                           Examples: Types of security integrity status observation metadata, which may value the observation, include codes from the HL7 DocumentCompletion code system such as legally authenticated, in progress, and incomplete.
         */
        SECINTSTOBS, 
        /**
         * An observation identifying trust metadata about an IT resource (data, information object, service, or system capability), which may be used as a trust attribute to populate a computable trust policy, trust credential, trust assertion, or trust label field in a security label or trust policy, which are principally used for authentication, authorization, and access control decisions.
         */
        SECTRSTOBS, 
        /**
         * Type of security metadata observation made about the formal declaration by an authority or neutral third party that validates the technical, security, trust, and business practice conformance of Trust Agents to facilitate security, interoperability, and trust among participants within a security domain or trust framework.
         */
        TRSTACCRDOBS, 
        /**
         * Type of security metadata observation made about privacy and security requirements with which a security domain must comply. [ISO IEC 10181-1]
         */
        TRSTAGREOBS, 
        /**
         * Type of security metadata observation made about a set of security-relevant data issued by a security authority or trusted third party, together with security information which is used to provide the integrity and data origin authentication services for an IT resource (data, information object, service, or system capability). [Based on ISO IEC 10181-1]

                        
                           For example,
                        

                        
                           A Certificate Policy (CP), which is a named set of rules that indicates the applicability of a certificate to a particular community and/or class of application with common security requirements. For example, a particular Certificate Policy might indicate the applicability of a type of certificate to the authentication of electronic data interchange transactions for the trading of goods within a given price range. [Trust Service Principles and Criteria for Certification Authorities Version 2.0 March 2011 Copyright 2011 by Canadian Institute of Chartered Accountants.
                           A Certificate Practice Statement (CSP), which is a statement of the practices which an Authority employs in issuing and managing certificates. [Trust Service Principles and Criteria for Certification Authorities Version 2.0 March 2011 Copyright 2011 by Canadian Institute of Chartered Accountants.]
         */
        TRSTCERTOBS, 
        /**
         * Type of security metadata observation made about a complete set of contracts, regulations or commitments that enable participating actors to rely on certain assertions by other actors to fulfill their information security requirements. [Kantara Initiative]
         */
        TRSTFWKOBS, 
        /**
         * Type of security metadata observation made about the digital quality or reliability of a trust assertion, activity, capability, information exchange, mechanism, process, or protocol.
         */
        TRSTLOAOBS, 
        /**
         * Type of security metadata observation made about a security architecture system component that supports enforcement of security policies.
         */
        TRSTMECOBS, 
        /**
         * Definition: A government health program that provides coverage on a fee for service basis for health services to persons meeting eligibility criteria such as income, location of residence, access to other coverages, health condition, and age, the cost of which is to some extent subsidized by public funds.

                        
                           Discussion: The structure and business processes for underwriting and administering a subsidized fee for service program is further specified by the Underwriter and Payer Role.class and Role.code.
         */
        SUBSIDFFS, 
        /**
         * Definition: Government mandated program providing coverage, disability income, and vocational rehabilitation for injuries sustained in the work place or in the course of employment.  Employers may either self-fund the program, purchase commercial coverage, or pay a premium to a government entity that administers the program.  Employees may be required to pay premiums toward the cost of coverage as well.
         */
        WRKCOMP, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActCode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ActAccountCode".equals(codeString))
          return _ACTACCOUNTCODE;
        if ("ACCTRECEIVABLE".equals(codeString))
          return ACCTRECEIVABLE;
        if ("CASH".equals(codeString))
          return CASH;
        if ("CC".equals(codeString))
          return CC;
        if ("AE".equals(codeString))
          return AE;
        if ("DN".equals(codeString))
          return DN;
        if ("DV".equals(codeString))
          return DV;
        if ("MC".equals(codeString))
          return MC;
        if ("V".equals(codeString))
          return V;
        if ("PBILLACCT".equals(codeString))
          return PBILLACCT;
        if ("_ActAdjudicationCode".equals(codeString))
          return _ACTADJUDICATIONCODE;
        if ("_ActAdjudicationGroupCode".equals(codeString))
          return _ACTADJUDICATIONGROUPCODE;
        if ("CONT".equals(codeString))
          return CONT;
        if ("DAY".equals(codeString))
          return DAY;
        if ("LOC".equals(codeString))
          return LOC;
        if ("MONTH".equals(codeString))
          return MONTH;
        if ("PERIOD".equals(codeString))
          return PERIOD;
        if ("PROV".equals(codeString))
          return PROV;
        if ("WEEK".equals(codeString))
          return WEEK;
        if ("YEAR".equals(codeString))
          return YEAR;
        if ("AA".equals(codeString))
          return AA;
        if ("ANF".equals(codeString))
          return ANF;
        if ("AR".equals(codeString))
          return AR;
        if ("AS".equals(codeString))
          return AS;
        if ("_ActAdjudicationResultActionCode".equals(codeString))
          return _ACTADJUDICATIONRESULTACTIONCODE;
        if ("DISPLAY".equals(codeString))
          return DISPLAY;
        if ("FORM".equals(codeString))
          return FORM;
        if ("_ActBillableModifierCode".equals(codeString))
          return _ACTBILLABLEMODIFIERCODE;
        if ("CPTM".equals(codeString))
          return CPTM;
        if ("HCPCSA".equals(codeString))
          return HCPCSA;
        if ("_ActBillingArrangementCode".equals(codeString))
          return _ACTBILLINGARRANGEMENTCODE;
        if ("BLK".equals(codeString))
          return BLK;
        if ("CAP".equals(codeString))
          return CAP;
        if ("CONTF".equals(codeString))
          return CONTF;
        if ("FINBILL".equals(codeString))
          return FINBILL;
        if ("ROST".equals(codeString))
          return ROST;
        if ("SESS".equals(codeString))
          return SESS;
        if ("_ActBoundedROICode".equals(codeString))
          return _ACTBOUNDEDROICODE;
        if ("ROIFS".equals(codeString))
          return ROIFS;
        if ("ROIPS".equals(codeString))
          return ROIPS;
        if ("_ActCareProvisionCode".equals(codeString))
          return _ACTCAREPROVISIONCODE;
        if ("_ActCredentialedCareCode".equals(codeString))
          return _ACTCREDENTIALEDCARECODE;
        if ("_ActCredentialedCareProvisionPersonCode".equals(codeString))
          return _ACTCREDENTIALEDCAREPROVISIONPERSONCODE;
        if ("CACC".equals(codeString))
          return CACC;
        if ("CAIC".equals(codeString))
          return CAIC;
        if ("CAMC".equals(codeString))
          return CAMC;
        if ("CANC".equals(codeString))
          return CANC;
        if ("CAPC".equals(codeString))
          return CAPC;
        if ("CBGC".equals(codeString))
          return CBGC;
        if ("CCCC".equals(codeString))
          return CCCC;
        if ("CCGC".equals(codeString))
          return CCGC;
        if ("CCPC".equals(codeString))
          return CCPC;
        if ("CCSC".equals(codeString))
          return CCSC;
        if ("CDEC".equals(codeString))
          return CDEC;
        if ("CDRC".equals(codeString))
          return CDRC;
        if ("CEMC".equals(codeString))
          return CEMC;
        if ("CFPC".equals(codeString))
          return CFPC;
        if ("CIMC".equals(codeString))
          return CIMC;
        if ("CMGC".equals(codeString))
          return CMGC;
        if ("CNEC".equals(codeString))
          return CNEC;
        if ("CNMC".equals(codeString))
          return CNMC;
        if ("CNQC".equals(codeString))
          return CNQC;
        if ("CNSC".equals(codeString))
          return CNSC;
        if ("COGC".equals(codeString))
          return COGC;
        if ("COMC".equals(codeString))
          return COMC;
        if ("COPC".equals(codeString))
          return COPC;
        if ("COSC".equals(codeString))
          return COSC;
        if ("COTC".equals(codeString))
          return COTC;
        if ("CPEC".equals(codeString))
          return CPEC;
        if ("CPGC".equals(codeString))
          return CPGC;
        if ("CPHC".equals(codeString))
          return CPHC;
        if ("CPRC".equals(codeString))
          return CPRC;
        if ("CPSC".equals(codeString))
          return CPSC;
        if ("CPYC".equals(codeString))
          return CPYC;
        if ("CROC".equals(codeString))
          return CROC;
        if ("CRPC".equals(codeString))
          return CRPC;
        if ("CSUC".equals(codeString))
          return CSUC;
        if ("CTSC".equals(codeString))
          return CTSC;
        if ("CURC".equals(codeString))
          return CURC;
        if ("CVSC".equals(codeString))
          return CVSC;
        if ("LGPC".equals(codeString))
          return LGPC;
        if ("_ActCredentialedCareProvisionProgramCode".equals(codeString))
          return _ACTCREDENTIALEDCAREPROVISIONPROGRAMCODE;
        if ("AALC".equals(codeString))
          return AALC;
        if ("AAMC".equals(codeString))
          return AAMC;
        if ("ABHC".equals(codeString))
          return ABHC;
        if ("ACAC".equals(codeString))
          return ACAC;
        if ("ACHC".equals(codeString))
          return ACHC;
        if ("AHOC".equals(codeString))
          return AHOC;
        if ("ALTC".equals(codeString))
          return ALTC;
        if ("AOSC".equals(codeString))
          return AOSC;
        if ("CACS".equals(codeString))
          return CACS;
        if ("CAMI".equals(codeString))
          return CAMI;
        if ("CAST".equals(codeString))
          return CAST;
        if ("CBAR".equals(codeString))
          return CBAR;
        if ("CCAD".equals(codeString))
          return CCAD;
        if ("CCAR".equals(codeString))
          return CCAR;
        if ("CDEP".equals(codeString))
          return CDEP;
        if ("CDGD".equals(codeString))
          return CDGD;
        if ("CDIA".equals(codeString))
          return CDIA;
        if ("CEPI".equals(codeString))
          return CEPI;
        if ("CFEL".equals(codeString))
          return CFEL;
        if ("CHFC".equals(codeString))
          return CHFC;
        if ("CHRO".equals(codeString))
          return CHRO;
        if ("CHYP".equals(codeString))
          return CHYP;
        if ("CMIH".equals(codeString))
          return CMIH;
        if ("CMSC".equals(codeString))
          return CMSC;
        if ("COJR".equals(codeString))
          return COJR;
        if ("CONC".equals(codeString))
          return CONC;
        if ("COPD".equals(codeString))
          return COPD;
        if ("CORT".equals(codeString))
          return CORT;
        if ("CPAD".equals(codeString))
          return CPAD;
        if ("CPND".equals(codeString))
          return CPND;
        if ("CPST".equals(codeString))
          return CPST;
        if ("CSDM".equals(codeString))
          return CSDM;
        if ("CSIC".equals(codeString))
          return CSIC;
        if ("CSLD".equals(codeString))
          return CSLD;
        if ("CSPT".equals(codeString))
          return CSPT;
        if ("CTBU".equals(codeString))
          return CTBU;
        if ("CVDC".equals(codeString))
          return CVDC;
        if ("CWMA".equals(codeString))
          return CWMA;
        if ("CWOH".equals(codeString))
          return CWOH;
        if ("_ActEncounterCode".equals(codeString))
          return _ACTENCOUNTERCODE;
        if ("AMB".equals(codeString))
          return AMB;
        if ("EMER".equals(codeString))
          return EMER;
        if ("FLD".equals(codeString))
          return FLD;
        if ("HH".equals(codeString))
          return HH;
        if ("IMP".equals(codeString))
          return IMP;
        if ("ACUTE".equals(codeString))
          return ACUTE;
        if ("NONAC".equals(codeString))
          return NONAC;
        if ("PRENC".equals(codeString))
          return PRENC;
        if ("SS".equals(codeString))
          return SS;
        if ("VR".equals(codeString))
          return VR;
        if ("_ActMedicalServiceCode".equals(codeString))
          return _ACTMEDICALSERVICECODE;
        if ("ALC".equals(codeString))
          return ALC;
        if ("CARD".equals(codeString))
          return CARD;
        if ("CHR".equals(codeString))
          return CHR;
        if ("DNTL".equals(codeString))
          return DNTL;
        if ("DRGRHB".equals(codeString))
          return DRGRHB;
        if ("GENRL".equals(codeString))
          return GENRL;
        if ("MED".equals(codeString))
          return MED;
        if ("OBS".equals(codeString))
          return OBS;
        if ("ONC".equals(codeString))
          return ONC;
        if ("PALL".equals(codeString))
          return PALL;
        if ("PED".equals(codeString))
          return PED;
        if ("PHAR".equals(codeString))
          return PHAR;
        if ("PHYRHB".equals(codeString))
          return PHYRHB;
        if ("PSYCH".equals(codeString))
          return PSYCH;
        if ("SURG".equals(codeString))
          return SURG;
        if ("_ActClaimAttachmentCategoryCode".equals(codeString))
          return _ACTCLAIMATTACHMENTCATEGORYCODE;
        if ("AUTOATTCH".equals(codeString))
          return AUTOATTCH;
        if ("DOCUMENT".equals(codeString))
          return DOCUMENT;
        if ("HEALTHREC".equals(codeString))
          return HEALTHREC;
        if ("IMG".equals(codeString))
          return IMG;
        if ("LABRESULTS".equals(codeString))
          return LABRESULTS;
        if ("MODEL".equals(codeString))
          return MODEL;
        if ("WIATTCH".equals(codeString))
          return WIATTCH;
        if ("XRAY".equals(codeString))
          return XRAY;
        if ("_ActConsentType".equals(codeString))
          return _ACTCONSENTTYPE;
        if ("ICOL".equals(codeString))
          return ICOL;
        if ("IDSCL".equals(codeString))
          return IDSCL;
        if ("INFA".equals(codeString))
          return INFA;
        if ("INFAO".equals(codeString))
          return INFAO;
        if ("INFASO".equals(codeString))
          return INFASO;
        if ("IRDSCL".equals(codeString))
          return IRDSCL;
        if ("RESEARCH".equals(codeString))
          return RESEARCH;
        if ("RSDID".equals(codeString))
          return RSDID;
        if ("RSREID".equals(codeString))
          return RSREID;
        if ("_ActContainerRegistrationCode".equals(codeString))
          return _ACTCONTAINERREGISTRATIONCODE;
        if ("ID".equals(codeString))
          return ID;
        if ("IP".equals(codeString))
          return IP;
        if ("L".equals(codeString))
          return L;
        if ("M".equals(codeString))
          return M;
        if ("O".equals(codeString))
          return O;
        if ("R".equals(codeString))
          return R;
        if ("X".equals(codeString))
          return X;
        if ("_ActControlVariable".equals(codeString))
          return _ACTCONTROLVARIABLE;
        if ("AUTO".equals(codeString))
          return AUTO;
        if ("ENDC".equals(codeString))
          return ENDC;
        if ("REFLEX".equals(codeString))
          return REFLEX;
        if ("_ActCoverageConfirmationCode".equals(codeString))
          return _ACTCOVERAGECONFIRMATIONCODE;
        if ("_ActCoverageAuthorizationConfirmationCode".equals(codeString))
          return _ACTCOVERAGEAUTHORIZATIONCONFIRMATIONCODE;
        if ("AUTH".equals(codeString))
          return AUTH;
        if ("NAUTH".equals(codeString))
          return NAUTH;
        if ("_ActCoverageLimitCode".equals(codeString))
          return _ACTCOVERAGELIMITCODE;
        if ("_ActCoverageQuantityLimitCode".equals(codeString))
          return _ACTCOVERAGEQUANTITYLIMITCODE;
        if ("COVPRD".equals(codeString))
          return COVPRD;
        if ("LFEMX".equals(codeString))
          return LFEMX;
        if ("NETAMT".equals(codeString))
          return NETAMT;
        if ("PRDMX".equals(codeString))
          return PRDMX;
        if ("UNITPRICE".equals(codeString))
          return UNITPRICE;
        if ("UNITQTY".equals(codeString))
          return UNITQTY;
        if ("COVMX".equals(codeString))
          return COVMX;
        if ("_ActCoverageTypeCode".equals(codeString))
          return _ACTCOVERAGETYPECODE;
        if ("_ActInsurancePolicyCode".equals(codeString))
          return _ACTINSURANCEPOLICYCODE;
        if ("EHCPOL".equals(codeString))
          return EHCPOL;
        if ("HSAPOL".equals(codeString))
          return HSAPOL;
        if ("AUTOPOL".equals(codeString))
          return AUTOPOL;
        if ("COL".equals(codeString))
          return COL;
        if ("UNINSMOT".equals(codeString))
          return UNINSMOT;
        if ("PUBLICPOL".equals(codeString))
          return PUBLICPOL;
        if ("DENTPRG".equals(codeString))
          return DENTPRG;
        if ("DISEASEPRG".equals(codeString))
          return DISEASEPRG;
        if ("CANPRG".equals(codeString))
          return CANPRG;
        if ("ENDRENAL".equals(codeString))
          return ENDRENAL;
        if ("HIVAIDS".equals(codeString))
          return HIVAIDS;
        if ("MANDPOL".equals(codeString))
          return MANDPOL;
        if ("MENTPRG".equals(codeString))
          return MENTPRG;
        if ("SAFNET".equals(codeString))
          return SAFNET;
        if ("SUBPRG".equals(codeString))
          return SUBPRG;
        if ("SUBSIDIZ".equals(codeString))
          return SUBSIDIZ;
        if ("SUBSIDMC".equals(codeString))
          return SUBSIDMC;
        if ("SUBSUPP".equals(codeString))
          return SUBSUPP;
        if ("WCBPOL".equals(codeString))
          return WCBPOL;
        if ("_ActInsuranceTypeCode".equals(codeString))
          return _ACTINSURANCETYPECODE;
        if ("_ActHealthInsuranceTypeCode".equals(codeString))
          return _ACTHEALTHINSURANCETYPECODE;
        if ("DENTAL".equals(codeString))
          return DENTAL;
        if ("DISEASE".equals(codeString))
          return DISEASE;
        if ("DRUGPOL".equals(codeString))
          return DRUGPOL;
        if ("HIP".equals(codeString))
          return HIP;
        if ("LTC".equals(codeString))
          return LTC;
        if ("MCPOL".equals(codeString))
          return MCPOL;
        if ("POS".equals(codeString))
          return POS;
        if ("HMO".equals(codeString))
          return HMO;
        if ("PPO".equals(codeString))
          return PPO;
        if ("MENTPOL".equals(codeString))
          return MENTPOL;
        if ("SUBPOL".equals(codeString))
          return SUBPOL;
        if ("VISPOL".equals(codeString))
          return VISPOL;
        if ("DIS".equals(codeString))
          return DIS;
        if ("EWB".equals(codeString))
          return EWB;
        if ("FLEXP".equals(codeString))
          return FLEXP;
        if ("LIFE".equals(codeString))
          return LIFE;
        if ("ANNU".equals(codeString))
          return ANNU;
        if ("TLIFE".equals(codeString))
          return TLIFE;
        if ("ULIFE".equals(codeString))
          return ULIFE;
        if ("PNC".equals(codeString))
          return PNC;
        if ("REI".equals(codeString))
          return REI;
        if ("SURPL".equals(codeString))
          return SURPL;
        if ("UMBRL".equals(codeString))
          return UMBRL;
        if ("_ActProgramTypeCode".equals(codeString))
          return _ACTPROGRAMTYPECODE;
        if ("CHAR".equals(codeString))
          return CHAR;
        if ("CRIME".equals(codeString))
          return CRIME;
        if ("EAP".equals(codeString))
          return EAP;
        if ("GOVEMP".equals(codeString))
          return GOVEMP;
        if ("HIRISK".equals(codeString))
          return HIRISK;
        if ("IND".equals(codeString))
          return IND;
        if ("MILITARY".equals(codeString))
          return MILITARY;
        if ("RETIRE".equals(codeString))
          return RETIRE;
        if ("SOCIAL".equals(codeString))
          return SOCIAL;
        if ("VET".equals(codeString))
          return VET;
        if ("_ActDetectedIssueManagementCode".equals(codeString))
          return _ACTDETECTEDISSUEMANAGEMENTCODE;
        if ("_ActAdministrativeDetectedIssueManagementCode".equals(codeString))
          return _ACTADMINISTRATIVEDETECTEDISSUEMANAGEMENTCODE;
        if ("_AuthorizationIssueManagementCode".equals(codeString))
          return _AUTHORIZATIONISSUEMANAGEMENTCODE;
        if ("EMAUTH".equals(codeString))
          return EMAUTH;
        if ("21".equals(codeString))
          return _21;
        if ("1".equals(codeString))
          return _1;
        if ("19".equals(codeString))
          return _19;
        if ("2".equals(codeString))
          return _2;
        if ("22".equals(codeString))
          return _22;
        if ("23".equals(codeString))
          return _23;
        if ("3".equals(codeString))
          return _3;
        if ("4".equals(codeString))
          return _4;
        if ("5".equals(codeString))
          return _5;
        if ("6".equals(codeString))
          return _6;
        if ("7".equals(codeString))
          return _7;
        if ("14".equals(codeString))
          return _14;
        if ("15".equals(codeString))
          return _15;
        if ("16".equals(codeString))
          return _16;
        if ("17".equals(codeString))
          return _17;
        if ("18".equals(codeString))
          return _18;
        if ("20".equals(codeString))
          return _20;
        if ("8".equals(codeString))
          return _8;
        if ("10".equals(codeString))
          return _10;
        if ("11".equals(codeString))
          return _11;
        if ("12".equals(codeString))
          return _12;
        if ("13".equals(codeString))
          return _13;
        if ("9".equals(codeString))
          return _9;
        if ("_ActExposureCode".equals(codeString))
          return _ACTEXPOSURECODE;
        if ("CHLDCARE".equals(codeString))
          return CHLDCARE;
        if ("CONVEYNC".equals(codeString))
          return CONVEYNC;
        if ("HLTHCARE".equals(codeString))
          return HLTHCARE;
        if ("HOMECARE".equals(codeString))
          return HOMECARE;
        if ("HOSPPTNT".equals(codeString))
          return HOSPPTNT;
        if ("HOSPVSTR".equals(codeString))
          return HOSPVSTR;
        if ("HOUSEHLD".equals(codeString))
          return HOUSEHLD;
        if ("INMATE".equals(codeString))
          return INMATE;
        if ("INTIMATE".equals(codeString))
          return INTIMATE;
        if ("LTRMCARE".equals(codeString))
          return LTRMCARE;
        if ("PLACE".equals(codeString))
          return PLACE;
        if ("PTNTCARE".equals(codeString))
          return PTNTCARE;
        if ("SCHOOL2".equals(codeString))
          return SCHOOL2;
        if ("SOCIAL2".equals(codeString))
          return SOCIAL2;
        if ("SUBSTNCE".equals(codeString))
          return SUBSTNCE;
        if ("TRAVINT".equals(codeString))
          return TRAVINT;
        if ("WORK2".equals(codeString))
          return WORK2;
        if ("_ActFinancialTransactionCode".equals(codeString))
          return _ACTFINANCIALTRANSACTIONCODE;
        if ("CHRG".equals(codeString))
          return CHRG;
        if ("REV".equals(codeString))
          return REV;
        if ("_ActIncidentCode".equals(codeString))
          return _ACTINCIDENTCODE;
        if ("MVA".equals(codeString))
          return MVA;
        if ("SCHOOL".equals(codeString))
          return SCHOOL;
        if ("SPT".equals(codeString))
          return SPT;
        if ("WPA".equals(codeString))
          return WPA;
        if ("_ActInformationAccessCode".equals(codeString))
          return _ACTINFORMATIONACCESSCODE;
        if ("ACADR".equals(codeString))
          return ACADR;
        if ("ACALL".equals(codeString))
          return ACALL;
        if ("ACALLG".equals(codeString))
          return ACALLG;
        if ("ACCONS".equals(codeString))
          return ACCONS;
        if ("ACDEMO".equals(codeString))
          return ACDEMO;
        if ("ACDI".equals(codeString))
          return ACDI;
        if ("ACIMMUN".equals(codeString))
          return ACIMMUN;
        if ("ACLAB".equals(codeString))
          return ACLAB;
        if ("ACMED".equals(codeString))
          return ACMED;
        if ("ACMEDC".equals(codeString))
          return ACMEDC;
        if ("ACMEN".equals(codeString))
          return ACMEN;
        if ("ACOBS".equals(codeString))
          return ACOBS;
        if ("ACPOLPRG".equals(codeString))
          return ACPOLPRG;
        if ("ACPROV".equals(codeString))
          return ACPROV;
        if ("ACPSERV".equals(codeString))
          return ACPSERV;
        if ("ACSUBSTAB".equals(codeString))
          return ACSUBSTAB;
        if ("_ActInformationAccessContextCode".equals(codeString))
          return _ACTINFORMATIONACCESSCONTEXTCODE;
        if ("INFAUT".equals(codeString))
          return INFAUT;
        if ("INFCON".equals(codeString))
          return INFCON;
        if ("INFCRT".equals(codeString))
          return INFCRT;
        if ("INFDNG".equals(codeString))
          return INFDNG;
        if ("INFEMER".equals(codeString))
          return INFEMER;
        if ("INFPWR".equals(codeString))
          return INFPWR;
        if ("INFREG".equals(codeString))
          return INFREG;
        if ("_ActInformationCategoryCode".equals(codeString))
          return _ACTINFORMATIONCATEGORYCODE;
        if ("ALLCAT".equals(codeString))
          return ALLCAT;
        if ("ALLGCAT".equals(codeString))
          return ALLGCAT;
        if ("ARCAT".equals(codeString))
          return ARCAT;
        if ("COBSCAT".equals(codeString))
          return COBSCAT;
        if ("DEMOCAT".equals(codeString))
          return DEMOCAT;
        if ("DICAT".equals(codeString))
          return DICAT;
        if ("IMMUCAT".equals(codeString))
          return IMMUCAT;
        if ("LABCAT".equals(codeString))
          return LABCAT;
        if ("MEDCCAT".equals(codeString))
          return MEDCCAT;
        if ("MENCAT".equals(codeString))
          return MENCAT;
        if ("PSVCCAT".equals(codeString))
          return PSVCCAT;
        if ("RXCAT".equals(codeString))
          return RXCAT;
        if ("_ActInvoiceElementCode".equals(codeString))
          return _ACTINVOICEELEMENTCODE;
        if ("_ActInvoiceAdjudicationPaymentCode".equals(codeString))
          return _ACTINVOICEADJUDICATIONPAYMENTCODE;
        if ("_ActInvoiceAdjudicationPaymentGroupCode".equals(codeString))
          return _ACTINVOICEADJUDICATIONPAYMENTGROUPCODE;
        if ("ALEC".equals(codeString))
          return ALEC;
        if ("BONUS".equals(codeString))
          return BONUS;
        if ("CFWD".equals(codeString))
          return CFWD;
        if ("EDU".equals(codeString))
          return EDU;
        if ("EPYMT".equals(codeString))
          return EPYMT;
        if ("GARN".equals(codeString))
          return GARN;
        if ("INVOICE".equals(codeString))
          return INVOICE;
        if ("PINV".equals(codeString))
          return PINV;
        if ("PPRD".equals(codeString))
          return PPRD;
        if ("PROA".equals(codeString))
          return PROA;
        if ("RECOV".equals(codeString))
          return RECOV;
        if ("RETRO".equals(codeString))
          return RETRO;
        if ("TRAN".equals(codeString))
          return TRAN;
        if ("_ActInvoiceAdjudicationPaymentSummaryCode".equals(codeString))
          return _ACTINVOICEADJUDICATIONPAYMENTSUMMARYCODE;
        if ("INVTYPE".equals(codeString))
          return INVTYPE;
        if ("PAYEE".equals(codeString))
          return PAYEE;
        if ("PAYOR".equals(codeString))
          return PAYOR;
        if ("SENDAPP".equals(codeString))
          return SENDAPP;
        if ("_ActInvoiceDetailCode".equals(codeString))
          return _ACTINVOICEDETAILCODE;
        if ("_ActInvoiceDetailClinicalProductCode".equals(codeString))
          return _ACTINVOICEDETAILCLINICALPRODUCTCODE;
        if ("UNSPSC".equals(codeString))
          return UNSPSC;
        if ("_ActInvoiceDetailDrugProductCode".equals(codeString))
          return _ACTINVOICEDETAILDRUGPRODUCTCODE;
        if ("GTIN".equals(codeString))
          return GTIN;
        if ("UPC".equals(codeString))
          return UPC;
        if ("_ActInvoiceDetailGenericCode".equals(codeString))
          return _ACTINVOICEDETAILGENERICCODE;
        if ("_ActInvoiceDetailGenericAdjudicatorCode".equals(codeString))
          return _ACTINVOICEDETAILGENERICADJUDICATORCODE;
        if ("COIN".equals(codeString))
          return COIN;
        if ("COPAYMENT".equals(codeString))
          return COPAYMENT;
        if ("DEDUCTIBLE".equals(codeString))
          return DEDUCTIBLE;
        if ("PAY".equals(codeString))
          return PAY;
        if ("SPEND".equals(codeString))
          return SPEND;
        if ("_ActInvoiceDetailGenericModifierCode".equals(codeString))
          return _ACTINVOICEDETAILGENERICMODIFIERCODE;
        if ("AFTHRS".equals(codeString))
          return AFTHRS;
        if ("ISOL".equals(codeString))
          return ISOL;
        if ("OOO".equals(codeString))
          return OOO;
        if ("_ActInvoiceDetailGenericProviderCode".equals(codeString))
          return _ACTINVOICEDETAILGENERICPROVIDERCODE;
        if ("CANCAPT".equals(codeString))
          return CANCAPT;
        if ("DSC".equals(codeString))
          return DSC;
        if ("ESA".equals(codeString))
          return ESA;
        if ("FFSTOP".equals(codeString))
          return FFSTOP;
        if ("FNLFEE".equals(codeString))
          return FNLFEE;
        if ("FRSTFEE".equals(codeString))
          return FRSTFEE;
        if ("MARKUP".equals(codeString))
          return MARKUP;
        if ("MISSAPT".equals(codeString))
          return MISSAPT;
        if ("PERFEE".equals(codeString))
          return PERFEE;
        if ("PERMBNS".equals(codeString))
          return PERMBNS;
        if ("RESTOCK".equals(codeString))
          return RESTOCK;
        if ("TRAVEL".equals(codeString))
          return TRAVEL;
        if ("URGENT".equals(codeString))
          return URGENT;
        if ("_ActInvoiceDetailTaxCode".equals(codeString))
          return _ACTINVOICEDETAILTAXCODE;
        if ("FST".equals(codeString))
          return FST;
        if ("HST".equals(codeString))
          return HST;
        if ("PST".equals(codeString))
          return PST;
        if ("_ActInvoiceDetailPreferredAccommodationCode".equals(codeString))
          return _ACTINVOICEDETAILPREFERREDACCOMMODATIONCODE;
        if ("_ActEncounterAccommodationCode".equals(codeString))
          return _ACTENCOUNTERACCOMMODATIONCODE;
        if ("_HL7AccommodationCode".equals(codeString))
          return _HL7ACCOMMODATIONCODE;
        if ("I".equals(codeString))
          return I;
        if ("P".equals(codeString))
          return P;
        if ("S".equals(codeString))
          return S;
        if ("SP".equals(codeString))
          return SP;
        if ("W".equals(codeString))
          return W;
        if ("_ActInvoiceGroupCode".equals(codeString))
          return _ACTINVOICEGROUPCODE;
        if ("_ActInvoiceInterGroupCode".equals(codeString))
          return _ACTINVOICEINTERGROUPCODE;
        if ("CPNDDRGING".equals(codeString))
          return CPNDDRGING;
        if ("CPNDINDING".equals(codeString))
          return CPNDINDING;
        if ("CPNDSUPING".equals(codeString))
          return CPNDSUPING;
        if ("DRUGING".equals(codeString))
          return DRUGING;
        if ("FRAMEING".equals(codeString))
          return FRAMEING;
        if ("LENSING".equals(codeString))
          return LENSING;
        if ("PRDING".equals(codeString))
          return PRDING;
        if ("_ActInvoiceRootGroupCode".equals(codeString))
          return _ACTINVOICEROOTGROUPCODE;
        if ("CPINV".equals(codeString))
          return CPINV;
        if ("CSINV".equals(codeString))
          return CSINV;
        if ("CSPINV".equals(codeString))
          return CSPINV;
        if ("FININV".equals(codeString))
          return FININV;
        if ("OHSINV".equals(codeString))
          return OHSINV;
        if ("PAINV".equals(codeString))
          return PAINV;
        if ("RXCINV".equals(codeString))
          return RXCINV;
        if ("RXDINV".equals(codeString))
          return RXDINV;
        if ("SBFINV".equals(codeString))
          return SBFINV;
        if ("VRXINV".equals(codeString))
          return VRXINV;
        if ("_ActInvoiceElementSummaryCode".equals(codeString))
          return _ACTINVOICEELEMENTSUMMARYCODE;
        if ("_InvoiceElementAdjudicated".equals(codeString))
          return _INVOICEELEMENTADJUDICATED;
        if ("ADNFPPELAT".equals(codeString))
          return ADNFPPELAT;
        if ("ADNFPPELCT".equals(codeString))
          return ADNFPPELCT;
        if ("ADNFPPMNAT".equals(codeString))
          return ADNFPPMNAT;
        if ("ADNFPPMNCT".equals(codeString))
          return ADNFPPMNCT;
        if ("ADNFSPELAT".equals(codeString))
          return ADNFSPELAT;
        if ("ADNFSPELCT".equals(codeString))
          return ADNFSPELCT;
        if ("ADNFSPMNAT".equals(codeString))
          return ADNFSPMNAT;
        if ("ADNFSPMNCT".equals(codeString))
          return ADNFSPMNCT;
        if ("ADNPPPELAT".equals(codeString))
          return ADNPPPELAT;
        if ("ADNPPPELCT".equals(codeString))
          return ADNPPPELCT;
        if ("ADNPPPMNAT".equals(codeString))
          return ADNPPPMNAT;
        if ("ADNPPPMNCT".equals(codeString))
          return ADNPPPMNCT;
        if ("ADNPSPELAT".equals(codeString))
          return ADNPSPELAT;
        if ("ADNPSPELCT".equals(codeString))
          return ADNPSPELCT;
        if ("ADNPSPMNAT".equals(codeString))
          return ADNPSPMNAT;
        if ("ADNPSPMNCT".equals(codeString))
          return ADNPSPMNCT;
        if ("ADPPPPELAT".equals(codeString))
          return ADPPPPELAT;
        if ("ADPPPPELCT".equals(codeString))
          return ADPPPPELCT;
        if ("ADPPPPMNAT".equals(codeString))
          return ADPPPPMNAT;
        if ("ADPPPPMNCT".equals(codeString))
          return ADPPPPMNCT;
        if ("ADPPSPELAT".equals(codeString))
          return ADPPSPELAT;
        if ("ADPPSPELCT".equals(codeString))
          return ADPPSPELCT;
        if ("ADPPSPMNAT".equals(codeString))
          return ADPPSPMNAT;
        if ("ADPPSPMNCT".equals(codeString))
          return ADPPSPMNCT;
        if ("ADRFPPELAT".equals(codeString))
          return ADRFPPELAT;
        if ("ADRFPPELCT".equals(codeString))
          return ADRFPPELCT;
        if ("ADRFPPMNAT".equals(codeString))
          return ADRFPPMNAT;
        if ("ADRFPPMNCT".equals(codeString))
          return ADRFPPMNCT;
        if ("ADRFSPELAT".equals(codeString))
          return ADRFSPELAT;
        if ("ADRFSPELCT".equals(codeString))
          return ADRFSPELCT;
        if ("ADRFSPMNAT".equals(codeString))
          return ADRFSPMNAT;
        if ("ADRFSPMNCT".equals(codeString))
          return ADRFSPMNCT;
        if ("_InvoiceElementPaid".equals(codeString))
          return _INVOICEELEMENTPAID;
        if ("PDNFPPELAT".equals(codeString))
          return PDNFPPELAT;
        if ("PDNFPPELCT".equals(codeString))
          return PDNFPPELCT;
        if ("PDNFPPMNAT".equals(codeString))
          return PDNFPPMNAT;
        if ("PDNFPPMNCT".equals(codeString))
          return PDNFPPMNCT;
        if ("PDNFSPELAT".equals(codeString))
          return PDNFSPELAT;
        if ("PDNFSPELCT".equals(codeString))
          return PDNFSPELCT;
        if ("PDNFSPMNAT".equals(codeString))
          return PDNFSPMNAT;
        if ("PDNFSPMNCT".equals(codeString))
          return PDNFSPMNCT;
        if ("PDNPPPELAT".equals(codeString))
          return PDNPPPELAT;
        if ("PDNPPPELCT".equals(codeString))
          return PDNPPPELCT;
        if ("PDNPPPMNAT".equals(codeString))
          return PDNPPPMNAT;
        if ("PDNPPPMNCT".equals(codeString))
          return PDNPPPMNCT;
        if ("PDNPSPELAT".equals(codeString))
          return PDNPSPELAT;
        if ("PDNPSPELCT".equals(codeString))
          return PDNPSPELCT;
        if ("PDNPSPMNAT".equals(codeString))
          return PDNPSPMNAT;
        if ("PDNPSPMNCT".equals(codeString))
          return PDNPSPMNCT;
        if ("PDPPPPELAT".equals(codeString))
          return PDPPPPELAT;
        if ("PDPPPPELCT".equals(codeString))
          return PDPPPPELCT;
        if ("PDPPPPMNAT".equals(codeString))
          return PDPPPPMNAT;
        if ("PDPPPPMNCT".equals(codeString))
          return PDPPPPMNCT;
        if ("PDPPSPELAT".equals(codeString))
          return PDPPSPELAT;
        if ("PDPPSPELCT".equals(codeString))
          return PDPPSPELCT;
        if ("PDPPSPMNAT".equals(codeString))
          return PDPPSPMNAT;
        if ("PDPPSPMNCT".equals(codeString))
          return PDPPSPMNCT;
        if ("_InvoiceElementSubmitted".equals(codeString))
          return _INVOICEELEMENTSUBMITTED;
        if ("SBBLELAT".equals(codeString))
          return SBBLELAT;
        if ("SBBLELCT".equals(codeString))
          return SBBLELCT;
        if ("SBNFELAT".equals(codeString))
          return SBNFELAT;
        if ("SBNFELCT".equals(codeString))
          return SBNFELCT;
        if ("SBPDELAT".equals(codeString))
          return SBPDELAT;
        if ("SBPDELCT".equals(codeString))
          return SBPDELCT;
        if ("_ActInvoiceOverrideCode".equals(codeString))
          return _ACTINVOICEOVERRIDECODE;
        if ("COVGE".equals(codeString))
          return COVGE;
        if ("EFORM".equals(codeString))
          return EFORM;
        if ("FAX".equals(codeString))
          return FAX;
        if ("GFTH".equals(codeString))
          return GFTH;
        if ("LATE".equals(codeString))
          return LATE;
        if ("MANUAL".equals(codeString))
          return MANUAL;
        if ("OOJ".equals(codeString))
          return OOJ;
        if ("ORTHO".equals(codeString))
          return ORTHO;
        if ("PAPER".equals(codeString))
          return PAPER;
        if ("PIE".equals(codeString))
          return PIE;
        if ("PYRDELAY".equals(codeString))
          return PYRDELAY;
        if ("REFNR".equals(codeString))
          return REFNR;
        if ("REPSERV".equals(codeString))
          return REPSERV;
        if ("UNRELAT".equals(codeString))
          return UNRELAT;
        if ("VERBAUTH".equals(codeString))
          return VERBAUTH;
        if ("_ActListCode".equals(codeString))
          return _ACTLISTCODE;
        if ("_ActObservationList".equals(codeString))
          return _ACTOBSERVATIONLIST;
        if ("CARELIST".equals(codeString))
          return CARELIST;
        if ("CONDLIST".equals(codeString))
          return CONDLIST;
        if ("INTOLIST".equals(codeString))
          return INTOLIST;
        if ("PROBLIST".equals(codeString))
          return PROBLIST;
        if ("RISKLIST".equals(codeString))
          return RISKLIST;
        if ("GOALLIST".equals(codeString))
          return GOALLIST;
        if ("_ActTherapyDurationWorkingListCode".equals(codeString))
          return _ACTTHERAPYDURATIONWORKINGLISTCODE;
        if ("_ActMedicationTherapyDurationWorkingListCode".equals(codeString))
          return _ACTMEDICATIONTHERAPYDURATIONWORKINGLISTCODE;
        if ("ACU".equals(codeString))
          return ACU;
        if ("CHRON".equals(codeString))
          return CHRON;
        if ("ONET".equals(codeString))
          return ONET;
        if ("PRN".equals(codeString))
          return PRN;
        if ("MEDLIST".equals(codeString))
          return MEDLIST;
        if ("CURMEDLIST".equals(codeString))
          return CURMEDLIST;
        if ("DISCMEDLIST".equals(codeString))
          return DISCMEDLIST;
        if ("HISTMEDLIST".equals(codeString))
          return HISTMEDLIST;
        if ("_ActMonitoringProtocolCode".equals(codeString))
          return _ACTMONITORINGPROTOCOLCODE;
        if ("CTLSUB".equals(codeString))
          return CTLSUB;
        if ("INV".equals(codeString))
          return INV;
        if ("LU".equals(codeString))
          return LU;
        if ("OTC".equals(codeString))
          return OTC;
        if ("RX".equals(codeString))
          return RX;
        if ("SA".equals(codeString))
          return SA;
        if ("SAC".equals(codeString))
          return SAC;
        if ("_ActNonObservationIndicationCode".equals(codeString))
          return _ACTNONOBSERVATIONINDICATIONCODE;
        if ("IND01".equals(codeString))
          return IND01;
        if ("IND02".equals(codeString))
          return IND02;
        if ("IND03".equals(codeString))
          return IND03;
        if ("IND04".equals(codeString))
          return IND04;
        if ("IND05".equals(codeString))
          return IND05;
        if ("_ActObservationVerificationType".equals(codeString))
          return _ACTOBSERVATIONVERIFICATIONTYPE;
        if ("VFPAPER".equals(codeString))
          return VFPAPER;
        if ("_ActPaymentCode".equals(codeString))
          return _ACTPAYMENTCODE;
        if ("ACH".equals(codeString))
          return ACH;
        if ("CHK".equals(codeString))
          return CHK;
        if ("DDP".equals(codeString))
          return DDP;
        if ("NON".equals(codeString))
          return NON;
        if ("_ActPharmacySupplyType".equals(codeString))
          return _ACTPHARMACYSUPPLYTYPE;
        if ("DF".equals(codeString))
          return DF;
        if ("EM".equals(codeString))
          return EM;
        if ("SO".equals(codeString))
          return SO;
        if ("FF".equals(codeString))
          return FF;
        if ("FFC".equals(codeString))
          return FFC;
        if ("FFCS".equals(codeString))
          return FFCS;
        if ("FFP".equals(codeString))
          return FFP;
        if ("FFPS".equals(codeString))
          return FFPS;
        if ("FFSS".equals(codeString))
          return FFSS;
        if ("TFS".equals(codeString))
          return TFS;
        if ("TF".equals(codeString))
          return TF;
        if ("FS".equals(codeString))
          return FS;
        if ("MS".equals(codeString))
          return MS;
        if ("RF".equals(codeString))
          return RF;
        if ("UD".equals(codeString))
          return UD;
        if ("RFC".equals(codeString))
          return RFC;
        if ("RFCS".equals(codeString))
          return RFCS;
        if ("RFF".equals(codeString))
          return RFF;
        if ("RFFS".equals(codeString))
          return RFFS;
        if ("RFP".equals(codeString))
          return RFP;
        if ("RFPS".equals(codeString))
          return RFPS;
        if ("RFS".equals(codeString))
          return RFS;
        if ("TB".equals(codeString))
          return TB;
        if ("TBS".equals(codeString))
          return TBS;
        if ("UDE".equals(codeString))
          return UDE;
        if ("_ActPolicyType".equals(codeString))
          return _ACTPOLICYTYPE;
        if ("_ActPrivacyPolicy".equals(codeString))
          return _ACTPRIVACYPOLICY;
        if ("_ActConsentDirective".equals(codeString))
          return _ACTCONSENTDIRECTIVE;
        if ("EMRGONLY".equals(codeString))
          return EMRGONLY;
        if ("NOPP".equals(codeString))
          return NOPP;
        if ("OPTIN".equals(codeString))
          return OPTIN;
        if ("OPTOUT".equals(codeString))
          return OPTOUT;
        if ("_InformationSensitivityPolicy".equals(codeString))
          return _INFORMATIONSENSITIVITYPOLICY;
        if ("_ActInformationSensitivityPolicy".equals(codeString))
          return _ACTINFORMATIONSENSITIVITYPOLICY;
        if ("ETH".equals(codeString))
          return ETH;
        if ("GDIS".equals(codeString))
          return GDIS;
        if ("HIV".equals(codeString))
          return HIV;
        if ("PSY".equals(codeString))
          return PSY;
        if ("SCA".equals(codeString))
          return SCA;
        if ("SDV".equals(codeString))
          return SDV;
        if ("SEX".equals(codeString))
          return SEX;
        if ("STD".equals(codeString))
          return STD;
        if ("TBOO".equals(codeString))
          return TBOO;
        if ("_EntitySensitivityPolicyType".equals(codeString))
          return _ENTITYSENSITIVITYPOLICYTYPE;
        if ("DEMO".equals(codeString))
          return DEMO;
        if ("DOB".equals(codeString))
          return DOB;
        if ("GENDER".equals(codeString))
          return GENDER;
        if ("LIVARG".equals(codeString))
          return LIVARG;
        if ("MARST".equals(codeString))
          return MARST;
        if ("RACE".equals(codeString))
          return RACE;
        if ("REL".equals(codeString))
          return REL;
        if ("_RoleInformationSensitivityPolicy".equals(codeString))
          return _ROLEINFORMATIONSENSITIVITYPOLICY;
        if ("B".equals(codeString))
          return B;
        if ("EMPL".equals(codeString))
          return EMPL;
        if ("LOCIS".equals(codeString))
          return LOCIS;
        if ("SSP".equals(codeString))
          return SSP;
        if ("ADOL".equals(codeString))
          return ADOL;
        if ("CEL".equals(codeString))
          return CEL;
        if ("DIA".equals(codeString))
          return DIA;
        if ("DRGIS".equals(codeString))
          return DRGIS;
        if ("EMP".equals(codeString))
          return EMP;
        if ("PDS".equals(codeString))
          return PDS;
        if ("PRS".equals(codeString))
          return PRS;
        if ("COMPT".equals(codeString))
          return COMPT;
        if ("HRCOMPT".equals(codeString))
          return HRCOMPT;
        if ("RESCOMPT".equals(codeString))
          return RESCOMPT;
        if ("RMGTCOMPT".equals(codeString))
          return RMGTCOMPT;
        if ("ActTrustPolicyType".equals(codeString))
          return ACTTRUSTPOLICYTYPE;
        if ("TRSTACCRD".equals(codeString))
          return TRSTACCRD;
        if ("TRSTAGRE".equals(codeString))
          return TRSTAGRE;
        if ("TRSTASSUR".equals(codeString))
          return TRSTASSUR;
        if ("TRSTCERT".equals(codeString))
          return TRSTCERT;
        if ("TRSTFWK".equals(codeString))
          return TRSTFWK;
        if ("TRSTMEC".equals(codeString))
          return TRSTMEC;
        if ("COVPOL".equals(codeString))
          return COVPOL;
        if ("SecurityPolicy".equals(codeString))
          return SECURITYPOLICY;
        if ("ObligationPolicy".equals(codeString))
          return OBLIGATIONPOLICY;
        if ("ANONY".equals(codeString))
          return ANONY;
        if ("AOD".equals(codeString))
          return AOD;
        if ("AUDIT".equals(codeString))
          return AUDIT;
        if ("AUDTR".equals(codeString))
          return AUDTR;
        if ("CPLYCC".equals(codeString))
          return CPLYCC;
        if ("CPLYCD".equals(codeString))
          return CPLYCD;
        if ("CPLYJPP".equals(codeString))
          return CPLYJPP;
        if ("CPLYOPP".equals(codeString))
          return CPLYOPP;
        if ("CPLYOSP".equals(codeString))
          return CPLYOSP;
        if ("CPLYPOL".equals(codeString))
          return CPLYPOL;
        if ("DEID".equals(codeString))
          return DEID;
        if ("DELAU".equals(codeString))
          return DELAU;
        if ("ENCRYPT".equals(codeString))
          return ENCRYPT;
        if ("ENCRYPTR".equals(codeString))
          return ENCRYPTR;
        if ("ENCRYPTT".equals(codeString))
          return ENCRYPTT;
        if ("ENCRYPTU".equals(codeString))
          return ENCRYPTU;
        if ("HUAPRV".equals(codeString))
          return HUAPRV;
        if ("MASK".equals(codeString))
          return MASK;
        if ("MINEC".equals(codeString))
          return MINEC;
        if ("PRIVMARK".equals(codeString))
          return PRIVMARK;
        if ("PSEUD".equals(codeString))
          return PSEUD;
        if ("REDACT".equals(codeString))
          return REDACT;
        if ("RefrainPolicy".equals(codeString))
          return REFRAINPOLICY;
        if ("NOAUTH".equals(codeString))
          return NOAUTH;
        if ("NOCOLLECT".equals(codeString))
          return NOCOLLECT;
        if ("NODSCLCD".equals(codeString))
          return NODSCLCD;
        if ("NODSCLCDS".equals(codeString))
          return NODSCLCDS;
        if ("NOINTEGRATE".equals(codeString))
          return NOINTEGRATE;
        if ("NOLIST".equals(codeString))
          return NOLIST;
        if ("NOMOU".equals(codeString))
          return NOMOU;
        if ("NOORGPOL".equals(codeString))
          return NOORGPOL;
        if ("NOPAT".equals(codeString))
          return NOPAT;
        if ("NOPERSISTP".equals(codeString))
          return NOPERSISTP;
        if ("NORDSCLCD".equals(codeString))
          return NORDSCLCD;
        if ("NORDSCLCDS".equals(codeString))
          return NORDSCLCDS;
        if ("NORDSCLW".equals(codeString))
          return NORDSCLW;
        if ("NORELINK".equals(codeString))
          return NORELINK;
        if ("NOREUSE".equals(codeString))
          return NOREUSE;
        if ("NOVIP".equals(codeString))
          return NOVIP;
        if ("ORCON".equals(codeString))
          return ORCON;
        if ("_ActProductAcquisitionCode".equals(codeString))
          return _ACTPRODUCTACQUISITIONCODE;
        if ("LOAN".equals(codeString))
          return LOAN;
        if ("RENT".equals(codeString))
          return RENT;
        if ("TRANSFER".equals(codeString))
          return TRANSFER;
        if ("SALE".equals(codeString))
          return SALE;
        if ("_ActSpecimenTransportCode".equals(codeString))
          return _ACTSPECIMENTRANSPORTCODE;
        if ("SREC".equals(codeString))
          return SREC;
        if ("SSTOR".equals(codeString))
          return SSTOR;
        if ("STRAN".equals(codeString))
          return STRAN;
        if ("_ActSpecimenTreatmentCode".equals(codeString))
          return _ACTSPECIMENTREATMENTCODE;
        if ("ACID".equals(codeString))
          return ACID;
        if ("ALK".equals(codeString))
          return ALK;
        if ("DEFB".equals(codeString))
          return DEFB;
        if ("FILT".equals(codeString))
          return FILT;
        if ("LDLP".equals(codeString))
          return LDLP;
        if ("NEUT".equals(codeString))
          return NEUT;
        if ("RECA".equals(codeString))
          return RECA;
        if ("UFIL".equals(codeString))
          return UFIL;
        if ("_ActSubstanceAdministrationCode".equals(codeString))
          return _ACTSUBSTANCEADMINISTRATIONCODE;
        if ("DRUG".equals(codeString))
          return DRUG;
        if ("FD".equals(codeString))
          return FD;
        if ("IMMUNIZ".equals(codeString))
          return IMMUNIZ;
        if ("_ActTaskCode".equals(codeString))
          return _ACTTASKCODE;
        if ("OE".equals(codeString))
          return OE;
        if ("LABOE".equals(codeString))
          return LABOE;
        if ("MEDOE".equals(codeString))
          return MEDOE;
        if ("PATDOC".equals(codeString))
          return PATDOC;
        if ("ALLERLREV".equals(codeString))
          return ALLERLREV;
        if ("CLINNOTEE".equals(codeString))
          return CLINNOTEE;
        if ("DIAGLISTE".equals(codeString))
          return DIAGLISTE;
        if ("DISCHINSTE".equals(codeString))
          return DISCHINSTE;
        if ("DISCHSUME".equals(codeString))
          return DISCHSUME;
        if ("PATEDUE".equals(codeString))
          return PATEDUE;
        if ("PATREPE".equals(codeString))
          return PATREPE;
        if ("PROBLISTE".equals(codeString))
          return PROBLISTE;
        if ("RADREPE".equals(codeString))
          return RADREPE;
        if ("IMMLREV".equals(codeString))
          return IMMLREV;
        if ("REMLREV".equals(codeString))
          return REMLREV;
        if ("WELLREMLREV".equals(codeString))
          return WELLREMLREV;
        if ("PATINFO".equals(codeString))
          return PATINFO;
        if ("ALLERLE".equals(codeString))
          return ALLERLE;
        if ("CDSREV".equals(codeString))
          return CDSREV;
        if ("CLINNOTEREV".equals(codeString))
          return CLINNOTEREV;
        if ("DISCHSUMREV".equals(codeString))
          return DISCHSUMREV;
        if ("DIAGLISTREV".equals(codeString))
          return DIAGLISTREV;
        if ("IMMLE".equals(codeString))
          return IMMLE;
        if ("LABRREV".equals(codeString))
          return LABRREV;
        if ("MICRORREV".equals(codeString))
          return MICRORREV;
        if ("MICROORGRREV".equals(codeString))
          return MICROORGRREV;
        if ("MICROSENSRREV".equals(codeString))
          return MICROSENSRREV;
        if ("MLREV".equals(codeString))
          return MLREV;
        if ("MARWLREV".equals(codeString))
          return MARWLREV;
        if ("OREV".equals(codeString))
          return OREV;
        if ("PATREPREV".equals(codeString))
          return PATREPREV;
        if ("PROBLISTREV".equals(codeString))
          return PROBLISTREV;
        if ("RADREPREV".equals(codeString))
          return RADREPREV;
        if ("REMLE".equals(codeString))
          return REMLE;
        if ("WELLREMLE".equals(codeString))
          return WELLREMLE;
        if ("RISKASSESS".equals(codeString))
          return RISKASSESS;
        if ("FALLRISK".equals(codeString))
          return FALLRISK;
        if ("_ActTransportationModeCode".equals(codeString))
          return _ACTTRANSPORTATIONMODECODE;
        if ("_ActPatientTransportationModeCode".equals(codeString))
          return _ACTPATIENTTRANSPORTATIONMODECODE;
        if ("AFOOT".equals(codeString))
          return AFOOT;
        if ("AMBT".equals(codeString))
          return AMBT;
        if ("AMBAIR".equals(codeString))
          return AMBAIR;
        if ("AMBGRND".equals(codeString))
          return AMBGRND;
        if ("AMBHELO".equals(codeString))
          return AMBHELO;
        if ("LAWENF".equals(codeString))
          return LAWENF;
        if ("PRVTRN".equals(codeString))
          return PRVTRN;
        if ("PUBTRN".equals(codeString))
          return PUBTRN;
        if ("_ObservationType".equals(codeString))
          return _OBSERVATIONTYPE;
        if ("_ActSpecObsCode".equals(codeString))
          return _ACTSPECOBSCODE;
        if ("ARTBLD".equals(codeString))
          return ARTBLD;
        if ("DILUTION".equals(codeString))
          return DILUTION;
        if ("AUTO-HIGH".equals(codeString))
          return AUTOHIGH;
        if ("AUTO-LOW".equals(codeString))
          return AUTOLOW;
        if ("PRE".equals(codeString))
          return PRE;
        if ("RERUN".equals(codeString))
          return RERUN;
        if ("EVNFCTS".equals(codeString))
          return EVNFCTS;
        if ("INTFR".equals(codeString))
          return INTFR;
        if ("FIBRIN".equals(codeString))
          return FIBRIN;
        if ("HEMOLYSIS".equals(codeString))
          return HEMOLYSIS;
        if ("ICTERUS".equals(codeString))
          return ICTERUS;
        if ("LIPEMIA".equals(codeString))
          return LIPEMIA;
        if ("VOLUME".equals(codeString))
          return VOLUME;
        if ("AVAILABLE".equals(codeString))
          return AVAILABLE;
        if ("CONSUMPTION".equals(codeString))
          return CONSUMPTION;
        if ("CURRENT".equals(codeString))
          return CURRENT;
        if ("INITIAL".equals(codeString))
          return INITIAL;
        if ("_AnnotationType".equals(codeString))
          return _ANNOTATIONTYPE;
        if ("_ActPatientAnnotationType".equals(codeString))
          return _ACTPATIENTANNOTATIONTYPE;
        if ("ANNDI".equals(codeString))
          return ANNDI;
        if ("ANNGEN".equals(codeString))
          return ANNGEN;
        if ("ANNIMM".equals(codeString))
          return ANNIMM;
        if ("ANNLAB".equals(codeString))
          return ANNLAB;
        if ("ANNMED".equals(codeString))
          return ANNMED;
        if ("_GeneticObservationType".equals(codeString))
          return _GENETICOBSERVATIONTYPE;
        if ("GENE".equals(codeString))
          return GENE;
        if ("_ImmunizationObservationType".equals(codeString))
          return _IMMUNIZATIONOBSERVATIONTYPE;
        if ("OBSANTC".equals(codeString))
          return OBSANTC;
        if ("OBSANTV".equals(codeString))
          return OBSANTV;
        if ("_IndividualCaseSafetyReportType".equals(codeString))
          return _INDIVIDUALCASESAFETYREPORTTYPE;
        if ("PAT_ADV_EVNT".equals(codeString))
          return PATADVEVNT;
        if ("VAC_PROBLEM".equals(codeString))
          return VACPROBLEM;
        if ("_LOINCObservationActContextAgeType".equals(codeString))
          return _LOINCOBSERVATIONACTCONTEXTAGETYPE;
        if ("21611-9".equals(codeString))
          return _216119;
        if ("21612-7".equals(codeString))
          return _216127;
        if ("29553-5".equals(codeString))
          return _295535;
        if ("30525-0".equals(codeString))
          return _305250;
        if ("30972-4".equals(codeString))
          return _309724;
        if ("_MedicationObservationType".equals(codeString))
          return _MEDICATIONOBSERVATIONTYPE;
        if ("REP_HALF_LIFE".equals(codeString))
          return REPHALFLIFE;
        if ("SPLCOATING".equals(codeString))
          return SPLCOATING;
        if ("SPLCOLOR".equals(codeString))
          return SPLCOLOR;
        if ("SPLIMAGE".equals(codeString))
          return SPLIMAGE;
        if ("SPLIMPRINT".equals(codeString))
          return SPLIMPRINT;
        if ("SPLSCORING".equals(codeString))
          return SPLSCORING;
        if ("SPLSHAPE".equals(codeString))
          return SPLSHAPE;
        if ("SPLSIZE".equals(codeString))
          return SPLSIZE;
        if ("SPLSYMBOL".equals(codeString))
          return SPLSYMBOL;
        if ("_ObservationIssueTriggerCodedObservationType".equals(codeString))
          return _OBSERVATIONISSUETRIGGERCODEDOBSERVATIONTYPE;
        if ("_CaseTransmissionMode".equals(codeString))
          return _CASETRANSMISSIONMODE;
        if ("AIRTRNS".equals(codeString))
          return AIRTRNS;
        if ("ANANTRNS".equals(codeString))
          return ANANTRNS;
        if ("ANHUMTRNS".equals(codeString))
          return ANHUMTRNS;
        if ("BDYFLDTRNS".equals(codeString))
          return BDYFLDTRNS;
        if ("BLDTRNS".equals(codeString))
          return BLDTRNS;
        if ("DERMTRNS".equals(codeString))
          return DERMTRNS;
        if ("ENVTRNS".equals(codeString))
          return ENVTRNS;
        if ("FECTRNS".equals(codeString))
          return FECTRNS;
        if ("FOMTRNS".equals(codeString))
          return FOMTRNS;
        if ("FOODTRNS".equals(codeString))
          return FOODTRNS;
        if ("HUMHUMTRNS".equals(codeString))
          return HUMHUMTRNS;
        if ("INDTRNS".equals(codeString))
          return INDTRNS;
        if ("LACTTRNS".equals(codeString))
          return LACTTRNS;
        if ("NOSTRNS".equals(codeString))
          return NOSTRNS;
        if ("PARTRNS".equals(codeString))
          return PARTRNS;
        if ("PLACTRNS".equals(codeString))
          return PLACTRNS;
        if ("SEXTRNS".equals(codeString))
          return SEXTRNS;
        if ("TRNSFTRNS".equals(codeString))
          return TRNSFTRNS;
        if ("VECTRNS".equals(codeString))
          return VECTRNS;
        if ("WATTRNS".equals(codeString))
          return WATTRNS;
        if ("_ObservationQualityMeasureAttribute".equals(codeString))
          return _OBSERVATIONQUALITYMEASUREATTRIBUTE;
        if ("AGGREGATE".equals(codeString))
          return AGGREGATE;
        if ("COPY".equals(codeString))
          return COPY;
        if ("CRS".equals(codeString))
          return CRS;
        if ("DEF".equals(codeString))
          return DEF;
        if ("DISC".equals(codeString))
          return DISC;
        if ("FINALDT".equals(codeString))
          return FINALDT;
        if ("GUIDE".equals(codeString))
          return GUIDE;
        if ("IDUR".equals(codeString))
          return IDUR;
        if ("ITMCNT".equals(codeString))
          return ITMCNT;
        if ("KEY".equals(codeString))
          return KEY;
        if ("MEDT".equals(codeString))
          return MEDT;
        if ("MSD".equals(codeString))
          return MSD;
        if ("MSRADJ".equals(codeString))
          return MSRADJ;
        if ("MSRAGG".equals(codeString))
          return MSRAGG;
        if ("MSRIMPROV".equals(codeString))
          return MSRIMPROV;
        if ("MSRJUR".equals(codeString))
          return MSRJUR;
        if ("MSRRPTR".equals(codeString))
          return MSRRPTR;
        if ("MSRRPTTIME".equals(codeString))
          return MSRRPTTIME;
        if ("MSRSCORE".equals(codeString))
          return MSRSCORE;
        if ("MSRSET".equals(codeString))
          return MSRSET;
        if ("MSRTOPIC".equals(codeString))
          return MSRTOPIC;
        if ("MSRTP".equals(codeString))
          return MSRTP;
        if ("MSRTYPE".equals(codeString))
          return MSRTYPE;
        if ("RAT".equals(codeString))
          return RAT;
        if ("REF".equals(codeString))
          return REF;
        if ("SDE".equals(codeString))
          return SDE;
        if ("STRAT".equals(codeString))
          return STRAT;
        if ("TRANF".equals(codeString))
          return TRANF;
        if ("USE".equals(codeString))
          return USE;
        if ("_ObservationSequenceType".equals(codeString))
          return _OBSERVATIONSEQUENCETYPE;
        if ("TIME_ABSOLUTE".equals(codeString))
          return TIMEABSOLUTE;
        if ("TIME_RELATIVE".equals(codeString))
          return TIMERELATIVE;
        if ("_ObservationSeriesType".equals(codeString))
          return _OBSERVATIONSERIESTYPE;
        if ("_ECGObservationSeriesType".equals(codeString))
          return _ECGOBSERVATIONSERIESTYPE;
        if ("REPRESENTATIVE_BEAT".equals(codeString))
          return REPRESENTATIVEBEAT;
        if ("RHYTHM".equals(codeString))
          return RHYTHM;
        if ("_PatientImmunizationRelatedObservationType".equals(codeString))
          return _PATIENTIMMUNIZATIONRELATEDOBSERVATIONTYPE;
        if ("CLSSRM".equals(codeString))
          return CLSSRM;
        if ("GRADE".equals(codeString))
          return GRADE;
        if ("SCHL".equals(codeString))
          return SCHL;
        if ("SCHLDIV".equals(codeString))
          return SCHLDIV;
        if ("TEACHER".equals(codeString))
          return TEACHER;
        if ("_PopulationInclusionObservationType".equals(codeString))
          return _POPULATIONINCLUSIONOBSERVATIONTYPE;
        if ("DENEX".equals(codeString))
          return DENEX;
        if ("DENEXCEP".equals(codeString))
          return DENEXCEP;
        if ("DENOM".equals(codeString))
          return DENOM;
        if ("IPOP".equals(codeString))
          return IPOP;
        if ("IPPOP".equals(codeString))
          return IPPOP;
        if ("MSRPOPL".equals(codeString))
          return MSRPOPL;
        if ("MSRPOPLEX".equals(codeString))
          return MSRPOPLEX;
        if ("NUMER".equals(codeString))
          return NUMER;
        if ("NUMEX".equals(codeString))
          return NUMEX;
        if ("_PreferenceObservationType".equals(codeString))
          return _PREFERENCEOBSERVATIONTYPE;
        if ("PREFSTRENGTH".equals(codeString))
          return PREFSTRENGTH;
        if ("ADVERSE_REACTION".equals(codeString))
          return ADVERSEREACTION;
        if ("ASSERTION".equals(codeString))
          return ASSERTION;
        if ("CASESER".equals(codeString))
          return CASESER;
        if ("CDIO".equals(codeString))
          return CDIO;
        if ("CRIT".equals(codeString))
          return CRIT;
        if ("CTMO".equals(codeString))
          return CTMO;
        if ("DX".equals(codeString))
          return DX;
        if ("ADMDX".equals(codeString))
          return ADMDX;
        if ("DISDX".equals(codeString))
          return DISDX;
        if ("INTDX".equals(codeString))
          return INTDX;
        if ("NOI".equals(codeString))
          return NOI;
        if ("GISTIER".equals(codeString))
          return GISTIER;
        if ("HHOBS".equals(codeString))
          return HHOBS;
        if ("ISSUE".equals(codeString))
          return ISSUE;
        if ("_ActAdministrativeDetectedIssueCode".equals(codeString))
          return _ACTADMINISTRATIVEDETECTEDISSUECODE;
        if ("_ActAdministrativeAuthorizationDetectedIssueCode".equals(codeString))
          return _ACTADMINISTRATIVEAUTHORIZATIONDETECTEDISSUECODE;
        if ("NAT".equals(codeString))
          return NAT;
        if ("SUPPRESSED".equals(codeString))
          return SUPPRESSED;
        if ("VALIDAT".equals(codeString))
          return VALIDAT;
        if ("KEY204".equals(codeString))
          return KEY204;
        if ("KEY205".equals(codeString))
          return KEY205;
        if ("COMPLY".equals(codeString))
          return COMPLY;
        if ("DUPTHPY".equals(codeString))
          return DUPTHPY;
        if ("DUPTHPCLS".equals(codeString))
          return DUPTHPCLS;
        if ("DUPTHPGEN".equals(codeString))
          return DUPTHPGEN;
        if ("ABUSE".equals(codeString))
          return ABUSE;
        if ("FRAUD".equals(codeString))
          return FRAUD;
        if ("PLYDOC".equals(codeString))
          return PLYDOC;
        if ("PLYPHRM".equals(codeString))
          return PLYPHRM;
        if ("DOSE".equals(codeString))
          return DOSE;
        if ("DOSECOND".equals(codeString))
          return DOSECOND;
        if ("DOSEDUR".equals(codeString))
          return DOSEDUR;
        if ("DOSEDURH".equals(codeString))
          return DOSEDURH;
        if ("DOSEDURHIND".equals(codeString))
          return DOSEDURHIND;
        if ("DOSEDURL".equals(codeString))
          return DOSEDURL;
        if ("DOSEDURLIND".equals(codeString))
          return DOSEDURLIND;
        if ("DOSEH".equals(codeString))
          return DOSEH;
        if ("DOSEHINDA".equals(codeString))
          return DOSEHINDA;
        if ("DOSEHIND".equals(codeString))
          return DOSEHIND;
        if ("DOSEHINDSA".equals(codeString))
          return DOSEHINDSA;
        if ("DOSEHINDW".equals(codeString))
          return DOSEHINDW;
        if ("DOSEIVL".equals(codeString))
          return DOSEIVL;
        if ("DOSEIVLIND".equals(codeString))
          return DOSEIVLIND;
        if ("DOSEL".equals(codeString))
          return DOSEL;
        if ("DOSELINDA".equals(codeString))
          return DOSELINDA;
        if ("DOSELIND".equals(codeString))
          return DOSELIND;
        if ("DOSELINDSA".equals(codeString))
          return DOSELINDSA;
        if ("DOSELINDW".equals(codeString))
          return DOSELINDW;
        if ("MDOSE".equals(codeString))
          return MDOSE;
        if ("OBSA".equals(codeString))
          return OBSA;
        if ("AGE".equals(codeString))
          return AGE;
        if ("ADALRT".equals(codeString))
          return ADALRT;
        if ("GEALRT".equals(codeString))
          return GEALRT;
        if ("PEALRT".equals(codeString))
          return PEALRT;
        if ("COND".equals(codeString))
          return COND;
        if ("HGHT".equals(codeString))
          return HGHT;
        if ("LACT".equals(codeString))
          return LACT;
        if ("PREG".equals(codeString))
          return PREG;
        if ("WGHT".equals(codeString))
          return WGHT;
        if ("CREACT".equals(codeString))
          return CREACT;
        if ("GEN".equals(codeString))
          return GEN;
        if ("GEND".equals(codeString))
          return GEND;
        if ("LAB".equals(codeString))
          return LAB;
        if ("REACT".equals(codeString))
          return REACT;
        if ("ALGY".equals(codeString))
          return ALGY;
        if ("INT".equals(codeString))
          return INT;
        if ("RREACT".equals(codeString))
          return RREACT;
        if ("RALG".equals(codeString))
          return RALG;
        if ("RAR".equals(codeString))
          return RAR;
        if ("RINT".equals(codeString))
          return RINT;
        if ("BUS".equals(codeString))
          return BUS;
        if ("CODE_INVAL".equals(codeString))
          return CODEINVAL;
        if ("CODE_DEPREC".equals(codeString))
          return CODEDEPREC;
        if ("FORMAT".equals(codeString))
          return FORMAT;
        if ("ILLEGAL".equals(codeString))
          return ILLEGAL;
        if ("LEN_RANGE".equals(codeString))
          return LENRANGE;
        if ("LEN_LONG".equals(codeString))
          return LENLONG;
        if ("LEN_SHORT".equals(codeString))
          return LENSHORT;
        if ("MISSCOND".equals(codeString))
          return MISSCOND;
        if ("MISSMAND".equals(codeString))
          return MISSMAND;
        if ("NODUPS".equals(codeString))
          return NODUPS;
        if ("NOPERSIST".equals(codeString))
          return NOPERSIST;
        if ("REP_RANGE".equals(codeString))
          return REPRANGE;
        if ("MAXOCCURS".equals(codeString))
          return MAXOCCURS;
        if ("MINOCCURS".equals(codeString))
          return MINOCCURS;
        if ("_ActAdministrativeRuleDetectedIssueCode".equals(codeString))
          return _ACTADMINISTRATIVERULEDETECTEDISSUECODE;
        if ("KEY206".equals(codeString))
          return KEY206;
        if ("OBSOLETE".equals(codeString))
          return OBSOLETE;
        if ("_ActSuppliedItemDetectedIssueCode".equals(codeString))
          return _ACTSUPPLIEDITEMDETECTEDISSUECODE;
        if ("_AdministrationDetectedIssueCode".equals(codeString))
          return _ADMINISTRATIONDETECTEDISSUECODE;
        if ("_AppropriatenessDetectedIssueCode".equals(codeString))
          return _APPROPRIATENESSDETECTEDISSUECODE;
        if ("_InteractionDetectedIssueCode".equals(codeString))
          return _INTERACTIONDETECTEDISSUECODE;
        if ("FOOD".equals(codeString))
          return FOOD;
        if ("TPROD".equals(codeString))
          return TPROD;
        if ("DRG".equals(codeString))
          return DRG;
        if ("NHP".equals(codeString))
          return NHP;
        if ("NONRX".equals(codeString))
          return NONRX;
        if ("PREVINEF".equals(codeString))
          return PREVINEF;
        if ("DACT".equals(codeString))
          return DACT;
        if ("TIME".equals(codeString))
          return TIME;
        if ("ALRTENDLATE".equals(codeString))
          return ALRTENDLATE;
        if ("ALRTSTRTLATE".equals(codeString))
          return ALRTSTRTLATE;
        if ("_SupplyDetectedIssueCode".equals(codeString))
          return _SUPPLYDETECTEDISSUECODE;
        if ("ALLDONE".equals(codeString))
          return ALLDONE;
        if ("FULFIL".equals(codeString))
          return FULFIL;
        if ("NOTACTN".equals(codeString))
          return NOTACTN;
        if ("NOTEQUIV".equals(codeString))
          return NOTEQUIV;
        if ("NOTEQUIVGEN".equals(codeString))
          return NOTEQUIVGEN;
        if ("NOTEQUIVTHER".equals(codeString))
          return NOTEQUIVTHER;
        if ("TIMING".equals(codeString))
          return TIMING;
        if ("INTERVAL".equals(codeString))
          return INTERVAL;
        if ("MINFREQ".equals(codeString))
          return MINFREQ;
        if ("HELD".equals(codeString))
          return HELD;
        if ("TOOLATE".equals(codeString))
          return TOOLATE;
        if ("TOOSOON".equals(codeString))
          return TOOSOON;
        if ("HISTORIC".equals(codeString))
          return HISTORIC;
        if ("PATPREF".equals(codeString))
          return PATPREF;
        if ("PATPREFALT".equals(codeString))
          return PATPREFALT;
        if ("KSUBJ".equals(codeString))
          return KSUBJ;
        if ("KSUBT".equals(codeString))
          return KSUBT;
        if ("OINT".equals(codeString))
          return OINT;
        if ("ALG".equals(codeString))
          return ALG;
        if ("DALG".equals(codeString))
          return DALG;
        if ("EALG".equals(codeString))
          return EALG;
        if ("FALG".equals(codeString))
          return FALG;
        if ("DINT".equals(codeString))
          return DINT;
        if ("DNAINT".equals(codeString))
          return DNAINT;
        if ("EINT".equals(codeString))
          return EINT;
        if ("ENAINT".equals(codeString))
          return ENAINT;
        if ("FINT".equals(codeString))
          return FINT;
        if ("FNAINT".equals(codeString))
          return FNAINT;
        if ("NAINT".equals(codeString))
          return NAINT;
        if ("SEV".equals(codeString))
          return SEV;
        if ("_ROIOverlayShape".equals(codeString))
          return _ROIOVERLAYSHAPE;
        if ("CIRCLE".equals(codeString))
          return CIRCLE;
        if ("ELLIPSE".equals(codeString))
          return ELLIPSE;
        if ("POINT".equals(codeString))
          return POINT;
        if ("POLY".equals(codeString))
          return POLY;
        if ("C".equals(codeString))
          return C;
        if ("DIET".equals(codeString))
          return DIET;
        if ("BR".equals(codeString))
          return BR;
        if ("DM".equals(codeString))
          return DM;
        if ("FAST".equals(codeString))
          return FAST;
        if ("FORMULA".equals(codeString))
          return FORMULA;
        if ("GF".equals(codeString))
          return GF;
        if ("LF".equals(codeString))
          return LF;
        if ("LP".equals(codeString))
          return LP;
        if ("LQ".equals(codeString))
          return LQ;
        if ("LS".equals(codeString))
          return LS;
        if ("N".equals(codeString))
          return N;
        if ("NF".equals(codeString))
          return NF;
        if ("PAF".equals(codeString))
          return PAF;
        if ("PAR".equals(codeString))
          return PAR;
        if ("RD".equals(codeString))
          return RD;
        if ("SCH".equals(codeString))
          return SCH;
        if ("SUPPLEMENT".equals(codeString))
          return SUPPLEMENT;
        if ("T".equals(codeString))
          return T;
        if ("VLI".equals(codeString))
          return VLI;
        if ("DRUGPRG".equals(codeString))
          return DRUGPRG;
        if ("F".equals(codeString))
          return F;
        if ("PRLMN".equals(codeString))
          return PRLMN;
        if ("SECOBS".equals(codeString))
          return SECOBS;
        if ("SECCATOBS".equals(codeString))
          return SECCATOBS;
        if ("SECCLASSOBS".equals(codeString))
          return SECCLASSOBS;
        if ("SECCONOBS".equals(codeString))
          return SECCONOBS;
        if ("SECINTOBS".equals(codeString))
          return SECINTOBS;
        if ("SECALTINTOBS".equals(codeString))
          return SECALTINTOBS;
        if ("SECDATINTOBS".equals(codeString))
          return SECDATINTOBS;
        if ("SECINTCONOBS".equals(codeString))
          return SECINTCONOBS;
        if ("SECINTPRVOBS".equals(codeString))
          return SECINTPRVOBS;
        if ("SECINTPRVABOBS".equals(codeString))
          return SECINTPRVABOBS;
        if ("SECINTPRVRBOBS".equals(codeString))
          return SECINTPRVRBOBS;
        if ("SECINTSTOBS".equals(codeString))
          return SECINTSTOBS;
        if ("SECTRSTOBS".equals(codeString))
          return SECTRSTOBS;
        if ("TRSTACCRDOBS".equals(codeString))
          return TRSTACCRDOBS;
        if ("TRSTAGREOBS".equals(codeString))
          return TRSTAGREOBS;
        if ("TRSTCERTOBS".equals(codeString))
          return TRSTCERTOBS;
        if ("TRSTFWKOBS".equals(codeString))
          return TRSTFWKOBS;
        if ("TRSTLOAOBS".equals(codeString))
          return TRSTLOAOBS;
        if ("TRSTMECOBS".equals(codeString))
          return TRSTMECOBS;
        if ("SUBSIDFFS".equals(codeString))
          return SUBSIDFFS;
        if ("WRKCOMP".equals(codeString))
          return WRKCOMP;
        throw new Exception("Unknown V3ActCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ACTACCOUNTCODE: return "_ActAccountCode";
            case ACCTRECEIVABLE: return "ACCTRECEIVABLE";
            case CASH: return "CASH";
            case CC: return "CC";
            case AE: return "AE";
            case DN: return "DN";
            case DV: return "DV";
            case MC: return "MC";
            case V: return "V";
            case PBILLACCT: return "PBILLACCT";
            case _ACTADJUDICATIONCODE: return "_ActAdjudicationCode";
            case _ACTADJUDICATIONGROUPCODE: return "_ActAdjudicationGroupCode";
            case CONT: return "CONT";
            case DAY: return "DAY";
            case LOC: return "LOC";
            case MONTH: return "MONTH";
            case PERIOD: return "PERIOD";
            case PROV: return "PROV";
            case WEEK: return "WEEK";
            case YEAR: return "YEAR";
            case AA: return "AA";
            case ANF: return "ANF";
            case AR: return "AR";
            case AS: return "AS";
            case _ACTADJUDICATIONRESULTACTIONCODE: return "_ActAdjudicationResultActionCode";
            case DISPLAY: return "DISPLAY";
            case FORM: return "FORM";
            case _ACTBILLABLEMODIFIERCODE: return "_ActBillableModifierCode";
            case CPTM: return "CPTM";
            case HCPCSA: return "HCPCSA";
            case _ACTBILLINGARRANGEMENTCODE: return "_ActBillingArrangementCode";
            case BLK: return "BLK";
            case CAP: return "CAP";
            case CONTF: return "CONTF";
            case FINBILL: return "FINBILL";
            case ROST: return "ROST";
            case SESS: return "SESS";
            case _ACTBOUNDEDROICODE: return "_ActBoundedROICode";
            case ROIFS: return "ROIFS";
            case ROIPS: return "ROIPS";
            case _ACTCAREPROVISIONCODE: return "_ActCareProvisionCode";
            case _ACTCREDENTIALEDCARECODE: return "_ActCredentialedCareCode";
            case _ACTCREDENTIALEDCAREPROVISIONPERSONCODE: return "_ActCredentialedCareProvisionPersonCode";
            case CACC: return "CACC";
            case CAIC: return "CAIC";
            case CAMC: return "CAMC";
            case CANC: return "CANC";
            case CAPC: return "CAPC";
            case CBGC: return "CBGC";
            case CCCC: return "CCCC";
            case CCGC: return "CCGC";
            case CCPC: return "CCPC";
            case CCSC: return "CCSC";
            case CDEC: return "CDEC";
            case CDRC: return "CDRC";
            case CEMC: return "CEMC";
            case CFPC: return "CFPC";
            case CIMC: return "CIMC";
            case CMGC: return "CMGC";
            case CNEC: return "CNEC";
            case CNMC: return "CNMC";
            case CNQC: return "CNQC";
            case CNSC: return "CNSC";
            case COGC: return "COGC";
            case COMC: return "COMC";
            case COPC: return "COPC";
            case COSC: return "COSC";
            case COTC: return "COTC";
            case CPEC: return "CPEC";
            case CPGC: return "CPGC";
            case CPHC: return "CPHC";
            case CPRC: return "CPRC";
            case CPSC: return "CPSC";
            case CPYC: return "CPYC";
            case CROC: return "CROC";
            case CRPC: return "CRPC";
            case CSUC: return "CSUC";
            case CTSC: return "CTSC";
            case CURC: return "CURC";
            case CVSC: return "CVSC";
            case LGPC: return "LGPC";
            case _ACTCREDENTIALEDCAREPROVISIONPROGRAMCODE: return "_ActCredentialedCareProvisionProgramCode";
            case AALC: return "AALC";
            case AAMC: return "AAMC";
            case ABHC: return "ABHC";
            case ACAC: return "ACAC";
            case ACHC: return "ACHC";
            case AHOC: return "AHOC";
            case ALTC: return "ALTC";
            case AOSC: return "AOSC";
            case CACS: return "CACS";
            case CAMI: return "CAMI";
            case CAST: return "CAST";
            case CBAR: return "CBAR";
            case CCAD: return "CCAD";
            case CCAR: return "CCAR";
            case CDEP: return "CDEP";
            case CDGD: return "CDGD";
            case CDIA: return "CDIA";
            case CEPI: return "CEPI";
            case CFEL: return "CFEL";
            case CHFC: return "CHFC";
            case CHRO: return "CHRO";
            case CHYP: return "CHYP";
            case CMIH: return "CMIH";
            case CMSC: return "CMSC";
            case COJR: return "COJR";
            case CONC: return "CONC";
            case COPD: return "COPD";
            case CORT: return "CORT";
            case CPAD: return "CPAD";
            case CPND: return "CPND";
            case CPST: return "CPST";
            case CSDM: return "CSDM";
            case CSIC: return "CSIC";
            case CSLD: return "CSLD";
            case CSPT: return "CSPT";
            case CTBU: return "CTBU";
            case CVDC: return "CVDC";
            case CWMA: return "CWMA";
            case CWOH: return "CWOH";
            case _ACTENCOUNTERCODE: return "_ActEncounterCode";
            case AMB: return "AMB";
            case EMER: return "EMER";
            case FLD: return "FLD";
            case HH: return "HH";
            case IMP: return "IMP";
            case ACUTE: return "ACUTE";
            case NONAC: return "NONAC";
            case PRENC: return "PRENC";
            case SS: return "SS";
            case VR: return "VR";
            case _ACTMEDICALSERVICECODE: return "_ActMedicalServiceCode";
            case ALC: return "ALC";
            case CARD: return "CARD";
            case CHR: return "CHR";
            case DNTL: return "DNTL";
            case DRGRHB: return "DRGRHB";
            case GENRL: return "GENRL";
            case MED: return "MED";
            case OBS: return "OBS";
            case ONC: return "ONC";
            case PALL: return "PALL";
            case PED: return "PED";
            case PHAR: return "PHAR";
            case PHYRHB: return "PHYRHB";
            case PSYCH: return "PSYCH";
            case SURG: return "SURG";
            case _ACTCLAIMATTACHMENTCATEGORYCODE: return "_ActClaimAttachmentCategoryCode";
            case AUTOATTCH: return "AUTOATTCH";
            case DOCUMENT: return "DOCUMENT";
            case HEALTHREC: return "HEALTHREC";
            case IMG: return "IMG";
            case LABRESULTS: return "LABRESULTS";
            case MODEL: return "MODEL";
            case WIATTCH: return "WIATTCH";
            case XRAY: return "XRAY";
            case _ACTCONSENTTYPE: return "_ActConsentType";
            case ICOL: return "ICOL";
            case IDSCL: return "IDSCL";
            case INFA: return "INFA";
            case INFAO: return "INFAO";
            case INFASO: return "INFASO";
            case IRDSCL: return "IRDSCL";
            case RESEARCH: return "RESEARCH";
            case RSDID: return "RSDID";
            case RSREID: return "RSREID";
            case _ACTCONTAINERREGISTRATIONCODE: return "_ActContainerRegistrationCode";
            case ID: return "ID";
            case IP: return "IP";
            case L: return "L";
            case M: return "M";
            case O: return "O";
            case R: return "R";
            case X: return "X";
            case _ACTCONTROLVARIABLE: return "_ActControlVariable";
            case AUTO: return "AUTO";
            case ENDC: return "ENDC";
            case REFLEX: return "REFLEX";
            case _ACTCOVERAGECONFIRMATIONCODE: return "_ActCoverageConfirmationCode";
            case _ACTCOVERAGEAUTHORIZATIONCONFIRMATIONCODE: return "_ActCoverageAuthorizationConfirmationCode";
            case AUTH: return "AUTH";
            case NAUTH: return "NAUTH";
            case _ACTCOVERAGELIMITCODE: return "_ActCoverageLimitCode";
            case _ACTCOVERAGEQUANTITYLIMITCODE: return "_ActCoverageQuantityLimitCode";
            case COVPRD: return "COVPRD";
            case LFEMX: return "LFEMX";
            case NETAMT: return "NETAMT";
            case PRDMX: return "PRDMX";
            case UNITPRICE: return "UNITPRICE";
            case UNITQTY: return "UNITQTY";
            case COVMX: return "COVMX";
            case _ACTCOVERAGETYPECODE: return "_ActCoverageTypeCode";
            case _ACTINSURANCEPOLICYCODE: return "_ActInsurancePolicyCode";
            case EHCPOL: return "EHCPOL";
            case HSAPOL: return "HSAPOL";
            case AUTOPOL: return "AUTOPOL";
            case COL: return "COL";
            case UNINSMOT: return "UNINSMOT";
            case PUBLICPOL: return "PUBLICPOL";
            case DENTPRG: return "DENTPRG";
            case DISEASEPRG: return "DISEASEPRG";
            case CANPRG: return "CANPRG";
            case ENDRENAL: return "ENDRENAL";
            case HIVAIDS: return "HIVAIDS";
            case MANDPOL: return "MANDPOL";
            case MENTPRG: return "MENTPRG";
            case SAFNET: return "SAFNET";
            case SUBPRG: return "SUBPRG";
            case SUBSIDIZ: return "SUBSIDIZ";
            case SUBSIDMC: return "SUBSIDMC";
            case SUBSUPP: return "SUBSUPP";
            case WCBPOL: return "WCBPOL";
            case _ACTINSURANCETYPECODE: return "_ActInsuranceTypeCode";
            case _ACTHEALTHINSURANCETYPECODE: return "_ActHealthInsuranceTypeCode";
            case DENTAL: return "DENTAL";
            case DISEASE: return "DISEASE";
            case DRUGPOL: return "DRUGPOL";
            case HIP: return "HIP";
            case LTC: return "LTC";
            case MCPOL: return "MCPOL";
            case POS: return "POS";
            case HMO: return "HMO";
            case PPO: return "PPO";
            case MENTPOL: return "MENTPOL";
            case SUBPOL: return "SUBPOL";
            case VISPOL: return "VISPOL";
            case DIS: return "DIS";
            case EWB: return "EWB";
            case FLEXP: return "FLEXP";
            case LIFE: return "LIFE";
            case ANNU: return "ANNU";
            case TLIFE: return "TLIFE";
            case ULIFE: return "ULIFE";
            case PNC: return "PNC";
            case REI: return "REI";
            case SURPL: return "SURPL";
            case UMBRL: return "UMBRL";
            case _ACTPROGRAMTYPECODE: return "_ActProgramTypeCode";
            case CHAR: return "CHAR";
            case CRIME: return "CRIME";
            case EAP: return "EAP";
            case GOVEMP: return "GOVEMP";
            case HIRISK: return "HIRISK";
            case IND: return "IND";
            case MILITARY: return "MILITARY";
            case RETIRE: return "RETIRE";
            case SOCIAL: return "SOCIAL";
            case VET: return "VET";
            case _ACTDETECTEDISSUEMANAGEMENTCODE: return "_ActDetectedIssueManagementCode";
            case _ACTADMINISTRATIVEDETECTEDISSUEMANAGEMENTCODE: return "_ActAdministrativeDetectedIssueManagementCode";
            case _AUTHORIZATIONISSUEMANAGEMENTCODE: return "_AuthorizationIssueManagementCode";
            case EMAUTH: return "EMAUTH";
            case _21: return "21";
            case _1: return "1";
            case _19: return "19";
            case _2: return "2";
            case _22: return "22";
            case _23: return "23";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case _6: return "6";
            case _7: return "7";
            case _14: return "14";
            case _15: return "15";
            case _16: return "16";
            case _17: return "17";
            case _18: return "18";
            case _20: return "20";
            case _8: return "8";
            case _10: return "10";
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _9: return "9";
            case _ACTEXPOSURECODE: return "_ActExposureCode";
            case CHLDCARE: return "CHLDCARE";
            case CONVEYNC: return "CONVEYNC";
            case HLTHCARE: return "HLTHCARE";
            case HOMECARE: return "HOMECARE";
            case HOSPPTNT: return "HOSPPTNT";
            case HOSPVSTR: return "HOSPVSTR";
            case HOUSEHLD: return "HOUSEHLD";
            case INMATE: return "INMATE";
            case INTIMATE: return "INTIMATE";
            case LTRMCARE: return "LTRMCARE";
            case PLACE: return "PLACE";
            case PTNTCARE: return "PTNTCARE";
            case SCHOOL2: return "SCHOOL2";
            case SOCIAL2: return "SOCIAL2";
            case SUBSTNCE: return "SUBSTNCE";
            case TRAVINT: return "TRAVINT";
            case WORK2: return "WORK2";
            case _ACTFINANCIALTRANSACTIONCODE: return "_ActFinancialTransactionCode";
            case CHRG: return "CHRG";
            case REV: return "REV";
            case _ACTINCIDENTCODE: return "_ActIncidentCode";
            case MVA: return "MVA";
            case SCHOOL: return "SCHOOL";
            case SPT: return "SPT";
            case WPA: return "WPA";
            case _ACTINFORMATIONACCESSCODE: return "_ActInformationAccessCode";
            case ACADR: return "ACADR";
            case ACALL: return "ACALL";
            case ACALLG: return "ACALLG";
            case ACCONS: return "ACCONS";
            case ACDEMO: return "ACDEMO";
            case ACDI: return "ACDI";
            case ACIMMUN: return "ACIMMUN";
            case ACLAB: return "ACLAB";
            case ACMED: return "ACMED";
            case ACMEDC: return "ACMEDC";
            case ACMEN: return "ACMEN";
            case ACOBS: return "ACOBS";
            case ACPOLPRG: return "ACPOLPRG";
            case ACPROV: return "ACPROV";
            case ACPSERV: return "ACPSERV";
            case ACSUBSTAB: return "ACSUBSTAB";
            case _ACTINFORMATIONACCESSCONTEXTCODE: return "_ActInformationAccessContextCode";
            case INFAUT: return "INFAUT";
            case INFCON: return "INFCON";
            case INFCRT: return "INFCRT";
            case INFDNG: return "INFDNG";
            case INFEMER: return "INFEMER";
            case INFPWR: return "INFPWR";
            case INFREG: return "INFREG";
            case _ACTINFORMATIONCATEGORYCODE: return "_ActInformationCategoryCode";
            case ALLCAT: return "ALLCAT";
            case ALLGCAT: return "ALLGCAT";
            case ARCAT: return "ARCAT";
            case COBSCAT: return "COBSCAT";
            case DEMOCAT: return "DEMOCAT";
            case DICAT: return "DICAT";
            case IMMUCAT: return "IMMUCAT";
            case LABCAT: return "LABCAT";
            case MEDCCAT: return "MEDCCAT";
            case MENCAT: return "MENCAT";
            case PSVCCAT: return "PSVCCAT";
            case RXCAT: return "RXCAT";
            case _ACTINVOICEELEMENTCODE: return "_ActInvoiceElementCode";
            case _ACTINVOICEADJUDICATIONPAYMENTCODE: return "_ActInvoiceAdjudicationPaymentCode";
            case _ACTINVOICEADJUDICATIONPAYMENTGROUPCODE: return "_ActInvoiceAdjudicationPaymentGroupCode";
            case ALEC: return "ALEC";
            case BONUS: return "BONUS";
            case CFWD: return "CFWD";
            case EDU: return "EDU";
            case EPYMT: return "EPYMT";
            case GARN: return "GARN";
            case INVOICE: return "INVOICE";
            case PINV: return "PINV";
            case PPRD: return "PPRD";
            case PROA: return "PROA";
            case RECOV: return "RECOV";
            case RETRO: return "RETRO";
            case TRAN: return "TRAN";
            case _ACTINVOICEADJUDICATIONPAYMENTSUMMARYCODE: return "_ActInvoiceAdjudicationPaymentSummaryCode";
            case INVTYPE: return "INVTYPE";
            case PAYEE: return "PAYEE";
            case PAYOR: return "PAYOR";
            case SENDAPP: return "SENDAPP";
            case _ACTINVOICEDETAILCODE: return "_ActInvoiceDetailCode";
            case _ACTINVOICEDETAILCLINICALPRODUCTCODE: return "_ActInvoiceDetailClinicalProductCode";
            case UNSPSC: return "UNSPSC";
            case _ACTINVOICEDETAILDRUGPRODUCTCODE: return "_ActInvoiceDetailDrugProductCode";
            case GTIN: return "GTIN";
            case UPC: return "UPC";
            case _ACTINVOICEDETAILGENERICCODE: return "_ActInvoiceDetailGenericCode";
            case _ACTINVOICEDETAILGENERICADJUDICATORCODE: return "_ActInvoiceDetailGenericAdjudicatorCode";
            case COIN: return "COIN";
            case COPAYMENT: return "COPAYMENT";
            case DEDUCTIBLE: return "DEDUCTIBLE";
            case PAY: return "PAY";
            case SPEND: return "SPEND";
            case _ACTINVOICEDETAILGENERICMODIFIERCODE: return "_ActInvoiceDetailGenericModifierCode";
            case AFTHRS: return "AFTHRS";
            case ISOL: return "ISOL";
            case OOO: return "OOO";
            case _ACTINVOICEDETAILGENERICPROVIDERCODE: return "_ActInvoiceDetailGenericProviderCode";
            case CANCAPT: return "CANCAPT";
            case DSC: return "DSC";
            case ESA: return "ESA";
            case FFSTOP: return "FFSTOP";
            case FNLFEE: return "FNLFEE";
            case FRSTFEE: return "FRSTFEE";
            case MARKUP: return "MARKUP";
            case MISSAPT: return "MISSAPT";
            case PERFEE: return "PERFEE";
            case PERMBNS: return "PERMBNS";
            case RESTOCK: return "RESTOCK";
            case TRAVEL: return "TRAVEL";
            case URGENT: return "URGENT";
            case _ACTINVOICEDETAILTAXCODE: return "_ActInvoiceDetailTaxCode";
            case FST: return "FST";
            case HST: return "HST";
            case PST: return "PST";
            case _ACTINVOICEDETAILPREFERREDACCOMMODATIONCODE: return "_ActInvoiceDetailPreferredAccommodationCode";
            case _ACTENCOUNTERACCOMMODATIONCODE: return "_ActEncounterAccommodationCode";
            case _HL7ACCOMMODATIONCODE: return "_HL7AccommodationCode";
            case I: return "I";
            case P: return "P";
            case S: return "S";
            case SP: return "SP";
            case W: return "W";
            case _ACTINVOICEGROUPCODE: return "_ActInvoiceGroupCode";
            case _ACTINVOICEINTERGROUPCODE: return "_ActInvoiceInterGroupCode";
            case CPNDDRGING: return "CPNDDRGING";
            case CPNDINDING: return "CPNDINDING";
            case CPNDSUPING: return "CPNDSUPING";
            case DRUGING: return "DRUGING";
            case FRAMEING: return "FRAMEING";
            case LENSING: return "LENSING";
            case PRDING: return "PRDING";
            case _ACTINVOICEROOTGROUPCODE: return "_ActInvoiceRootGroupCode";
            case CPINV: return "CPINV";
            case CSINV: return "CSINV";
            case CSPINV: return "CSPINV";
            case FININV: return "FININV";
            case OHSINV: return "OHSINV";
            case PAINV: return "PAINV";
            case RXCINV: return "RXCINV";
            case RXDINV: return "RXDINV";
            case SBFINV: return "SBFINV";
            case VRXINV: return "VRXINV";
            case _ACTINVOICEELEMENTSUMMARYCODE: return "_ActInvoiceElementSummaryCode";
            case _INVOICEELEMENTADJUDICATED: return "_InvoiceElementAdjudicated";
            case ADNFPPELAT: return "ADNFPPELAT";
            case ADNFPPELCT: return "ADNFPPELCT";
            case ADNFPPMNAT: return "ADNFPPMNAT";
            case ADNFPPMNCT: return "ADNFPPMNCT";
            case ADNFSPELAT: return "ADNFSPELAT";
            case ADNFSPELCT: return "ADNFSPELCT";
            case ADNFSPMNAT: return "ADNFSPMNAT";
            case ADNFSPMNCT: return "ADNFSPMNCT";
            case ADNPPPELAT: return "ADNPPPELAT";
            case ADNPPPELCT: return "ADNPPPELCT";
            case ADNPPPMNAT: return "ADNPPPMNAT";
            case ADNPPPMNCT: return "ADNPPPMNCT";
            case ADNPSPELAT: return "ADNPSPELAT";
            case ADNPSPELCT: return "ADNPSPELCT";
            case ADNPSPMNAT: return "ADNPSPMNAT";
            case ADNPSPMNCT: return "ADNPSPMNCT";
            case ADPPPPELAT: return "ADPPPPELAT";
            case ADPPPPELCT: return "ADPPPPELCT";
            case ADPPPPMNAT: return "ADPPPPMNAT";
            case ADPPPPMNCT: return "ADPPPPMNCT";
            case ADPPSPELAT: return "ADPPSPELAT";
            case ADPPSPELCT: return "ADPPSPELCT";
            case ADPPSPMNAT: return "ADPPSPMNAT";
            case ADPPSPMNCT: return "ADPPSPMNCT";
            case ADRFPPELAT: return "ADRFPPELAT";
            case ADRFPPELCT: return "ADRFPPELCT";
            case ADRFPPMNAT: return "ADRFPPMNAT";
            case ADRFPPMNCT: return "ADRFPPMNCT";
            case ADRFSPELAT: return "ADRFSPELAT";
            case ADRFSPELCT: return "ADRFSPELCT";
            case ADRFSPMNAT: return "ADRFSPMNAT";
            case ADRFSPMNCT: return "ADRFSPMNCT";
            case _INVOICEELEMENTPAID: return "_InvoiceElementPaid";
            case PDNFPPELAT: return "PDNFPPELAT";
            case PDNFPPELCT: return "PDNFPPELCT";
            case PDNFPPMNAT: return "PDNFPPMNAT";
            case PDNFPPMNCT: return "PDNFPPMNCT";
            case PDNFSPELAT: return "PDNFSPELAT";
            case PDNFSPELCT: return "PDNFSPELCT";
            case PDNFSPMNAT: return "PDNFSPMNAT";
            case PDNFSPMNCT: return "PDNFSPMNCT";
            case PDNPPPELAT: return "PDNPPPELAT";
            case PDNPPPELCT: return "PDNPPPELCT";
            case PDNPPPMNAT: return "PDNPPPMNAT";
            case PDNPPPMNCT: return "PDNPPPMNCT";
            case PDNPSPELAT: return "PDNPSPELAT";
            case PDNPSPELCT: return "PDNPSPELCT";
            case PDNPSPMNAT: return "PDNPSPMNAT";
            case PDNPSPMNCT: return "PDNPSPMNCT";
            case PDPPPPELAT: return "PDPPPPELAT";
            case PDPPPPELCT: return "PDPPPPELCT";
            case PDPPPPMNAT: return "PDPPPPMNAT";
            case PDPPPPMNCT: return "PDPPPPMNCT";
            case PDPPSPELAT: return "PDPPSPELAT";
            case PDPPSPELCT: return "PDPPSPELCT";
            case PDPPSPMNAT: return "PDPPSPMNAT";
            case PDPPSPMNCT: return "PDPPSPMNCT";
            case _INVOICEELEMENTSUBMITTED: return "_InvoiceElementSubmitted";
            case SBBLELAT: return "SBBLELAT";
            case SBBLELCT: return "SBBLELCT";
            case SBNFELAT: return "SBNFELAT";
            case SBNFELCT: return "SBNFELCT";
            case SBPDELAT: return "SBPDELAT";
            case SBPDELCT: return "SBPDELCT";
            case _ACTINVOICEOVERRIDECODE: return "_ActInvoiceOverrideCode";
            case COVGE: return "COVGE";
            case EFORM: return "EFORM";
            case FAX: return "FAX";
            case GFTH: return "GFTH";
            case LATE: return "LATE";
            case MANUAL: return "MANUAL";
            case OOJ: return "OOJ";
            case ORTHO: return "ORTHO";
            case PAPER: return "PAPER";
            case PIE: return "PIE";
            case PYRDELAY: return "PYRDELAY";
            case REFNR: return "REFNR";
            case REPSERV: return "REPSERV";
            case UNRELAT: return "UNRELAT";
            case VERBAUTH: return "VERBAUTH";
            case _ACTLISTCODE: return "_ActListCode";
            case _ACTOBSERVATIONLIST: return "_ActObservationList";
            case CARELIST: return "CARELIST";
            case CONDLIST: return "CONDLIST";
            case INTOLIST: return "INTOLIST";
            case PROBLIST: return "PROBLIST";
            case RISKLIST: return "RISKLIST";
            case GOALLIST: return "GOALLIST";
            case _ACTTHERAPYDURATIONWORKINGLISTCODE: return "_ActTherapyDurationWorkingListCode";
            case _ACTMEDICATIONTHERAPYDURATIONWORKINGLISTCODE: return "_ActMedicationTherapyDurationWorkingListCode";
            case ACU: return "ACU";
            case CHRON: return "CHRON";
            case ONET: return "ONET";
            case PRN: return "PRN";
            case MEDLIST: return "MEDLIST";
            case CURMEDLIST: return "CURMEDLIST";
            case DISCMEDLIST: return "DISCMEDLIST";
            case HISTMEDLIST: return "HISTMEDLIST";
            case _ACTMONITORINGPROTOCOLCODE: return "_ActMonitoringProtocolCode";
            case CTLSUB: return "CTLSUB";
            case INV: return "INV";
            case LU: return "LU";
            case OTC: return "OTC";
            case RX: return "RX";
            case SA: return "SA";
            case SAC: return "SAC";
            case _ACTNONOBSERVATIONINDICATIONCODE: return "_ActNonObservationIndicationCode";
            case IND01: return "IND01";
            case IND02: return "IND02";
            case IND03: return "IND03";
            case IND04: return "IND04";
            case IND05: return "IND05";
            case _ACTOBSERVATIONVERIFICATIONTYPE: return "_ActObservationVerificationType";
            case VFPAPER: return "VFPAPER";
            case _ACTPAYMENTCODE: return "_ActPaymentCode";
            case ACH: return "ACH";
            case CHK: return "CHK";
            case DDP: return "DDP";
            case NON: return "NON";
            case _ACTPHARMACYSUPPLYTYPE: return "_ActPharmacySupplyType";
            case DF: return "DF";
            case EM: return "EM";
            case SO: return "SO";
            case FF: return "FF";
            case FFC: return "FFC";
            case FFCS: return "FFCS";
            case FFP: return "FFP";
            case FFPS: return "FFPS";
            case FFSS: return "FFSS";
            case TFS: return "TFS";
            case TF: return "TF";
            case FS: return "FS";
            case MS: return "MS";
            case RF: return "RF";
            case UD: return "UD";
            case RFC: return "RFC";
            case RFCS: return "RFCS";
            case RFF: return "RFF";
            case RFFS: return "RFFS";
            case RFP: return "RFP";
            case RFPS: return "RFPS";
            case RFS: return "RFS";
            case TB: return "TB";
            case TBS: return "TBS";
            case UDE: return "UDE";
            case _ACTPOLICYTYPE: return "_ActPolicyType";
            case _ACTPRIVACYPOLICY: return "_ActPrivacyPolicy";
            case _ACTCONSENTDIRECTIVE: return "_ActConsentDirective";
            case EMRGONLY: return "EMRGONLY";
            case NOPP: return "NOPP";
            case OPTIN: return "OPTIN";
            case OPTOUT: return "OPTOUT";
            case _INFORMATIONSENSITIVITYPOLICY: return "_InformationSensitivityPolicy";
            case _ACTINFORMATIONSENSITIVITYPOLICY: return "_ActInformationSensitivityPolicy";
            case ETH: return "ETH";
            case GDIS: return "GDIS";
            case HIV: return "HIV";
            case PSY: return "PSY";
            case SCA: return "SCA";
            case SDV: return "SDV";
            case SEX: return "SEX";
            case STD: return "STD";
            case TBOO: return "TBOO";
            case _ENTITYSENSITIVITYPOLICYTYPE: return "_EntitySensitivityPolicyType";
            case DEMO: return "DEMO";
            case DOB: return "DOB";
            case GENDER: return "GENDER";
            case LIVARG: return "LIVARG";
            case MARST: return "MARST";
            case RACE: return "RACE";
            case REL: return "REL";
            case _ROLEINFORMATIONSENSITIVITYPOLICY: return "_RoleInformationSensitivityPolicy";
            case B: return "B";
            case EMPL: return "EMPL";
            case LOCIS: return "LOCIS";
            case SSP: return "SSP";
            case ADOL: return "ADOL";
            case CEL: return "CEL";
            case DIA: return "DIA";
            case DRGIS: return "DRGIS";
            case EMP: return "EMP";
            case PDS: return "PDS";
            case PRS: return "PRS";
            case COMPT: return "COMPT";
            case HRCOMPT: return "HRCOMPT";
            case RESCOMPT: return "RESCOMPT";
            case RMGTCOMPT: return "RMGTCOMPT";
            case ACTTRUSTPOLICYTYPE: return "ActTrustPolicyType";
            case TRSTACCRD: return "TRSTACCRD";
            case TRSTAGRE: return "TRSTAGRE";
            case TRSTASSUR: return "TRSTASSUR";
            case TRSTCERT: return "TRSTCERT";
            case TRSTFWK: return "TRSTFWK";
            case TRSTMEC: return "TRSTMEC";
            case COVPOL: return "COVPOL";
            case SECURITYPOLICY: return "SecurityPolicy";
            case OBLIGATIONPOLICY: return "ObligationPolicy";
            case ANONY: return "ANONY";
            case AOD: return "AOD";
            case AUDIT: return "AUDIT";
            case AUDTR: return "AUDTR";
            case CPLYCC: return "CPLYCC";
            case CPLYCD: return "CPLYCD";
            case CPLYJPP: return "CPLYJPP";
            case CPLYOPP: return "CPLYOPP";
            case CPLYOSP: return "CPLYOSP";
            case CPLYPOL: return "CPLYPOL";
            case DEID: return "DEID";
            case DELAU: return "DELAU";
            case ENCRYPT: return "ENCRYPT";
            case ENCRYPTR: return "ENCRYPTR";
            case ENCRYPTT: return "ENCRYPTT";
            case ENCRYPTU: return "ENCRYPTU";
            case HUAPRV: return "HUAPRV";
            case MASK: return "MASK";
            case MINEC: return "MINEC";
            case PRIVMARK: return "PRIVMARK";
            case PSEUD: return "PSEUD";
            case REDACT: return "REDACT";
            case REFRAINPOLICY: return "RefrainPolicy";
            case NOAUTH: return "NOAUTH";
            case NOCOLLECT: return "NOCOLLECT";
            case NODSCLCD: return "NODSCLCD";
            case NODSCLCDS: return "NODSCLCDS";
            case NOINTEGRATE: return "NOINTEGRATE";
            case NOLIST: return "NOLIST";
            case NOMOU: return "NOMOU";
            case NOORGPOL: return "NOORGPOL";
            case NOPAT: return "NOPAT";
            case NOPERSISTP: return "NOPERSISTP";
            case NORDSCLCD: return "NORDSCLCD";
            case NORDSCLCDS: return "NORDSCLCDS";
            case NORDSCLW: return "NORDSCLW";
            case NORELINK: return "NORELINK";
            case NOREUSE: return "NOREUSE";
            case NOVIP: return "NOVIP";
            case ORCON: return "ORCON";
            case _ACTPRODUCTACQUISITIONCODE: return "_ActProductAcquisitionCode";
            case LOAN: return "LOAN";
            case RENT: return "RENT";
            case TRANSFER: return "TRANSFER";
            case SALE: return "SALE";
            case _ACTSPECIMENTRANSPORTCODE: return "_ActSpecimenTransportCode";
            case SREC: return "SREC";
            case SSTOR: return "SSTOR";
            case STRAN: return "STRAN";
            case _ACTSPECIMENTREATMENTCODE: return "_ActSpecimenTreatmentCode";
            case ACID: return "ACID";
            case ALK: return "ALK";
            case DEFB: return "DEFB";
            case FILT: return "FILT";
            case LDLP: return "LDLP";
            case NEUT: return "NEUT";
            case RECA: return "RECA";
            case UFIL: return "UFIL";
            case _ACTSUBSTANCEADMINISTRATIONCODE: return "_ActSubstanceAdministrationCode";
            case DRUG: return "DRUG";
            case FD: return "FD";
            case IMMUNIZ: return "IMMUNIZ";
            case _ACTTASKCODE: return "_ActTaskCode";
            case OE: return "OE";
            case LABOE: return "LABOE";
            case MEDOE: return "MEDOE";
            case PATDOC: return "PATDOC";
            case ALLERLREV: return "ALLERLREV";
            case CLINNOTEE: return "CLINNOTEE";
            case DIAGLISTE: return "DIAGLISTE";
            case DISCHINSTE: return "DISCHINSTE";
            case DISCHSUME: return "DISCHSUME";
            case PATEDUE: return "PATEDUE";
            case PATREPE: return "PATREPE";
            case PROBLISTE: return "PROBLISTE";
            case RADREPE: return "RADREPE";
            case IMMLREV: return "IMMLREV";
            case REMLREV: return "REMLREV";
            case WELLREMLREV: return "WELLREMLREV";
            case PATINFO: return "PATINFO";
            case ALLERLE: return "ALLERLE";
            case CDSREV: return "CDSREV";
            case CLINNOTEREV: return "CLINNOTEREV";
            case DISCHSUMREV: return "DISCHSUMREV";
            case DIAGLISTREV: return "DIAGLISTREV";
            case IMMLE: return "IMMLE";
            case LABRREV: return "LABRREV";
            case MICRORREV: return "MICRORREV";
            case MICROORGRREV: return "MICROORGRREV";
            case MICROSENSRREV: return "MICROSENSRREV";
            case MLREV: return "MLREV";
            case MARWLREV: return "MARWLREV";
            case OREV: return "OREV";
            case PATREPREV: return "PATREPREV";
            case PROBLISTREV: return "PROBLISTREV";
            case RADREPREV: return "RADREPREV";
            case REMLE: return "REMLE";
            case WELLREMLE: return "WELLREMLE";
            case RISKASSESS: return "RISKASSESS";
            case FALLRISK: return "FALLRISK";
            case _ACTTRANSPORTATIONMODECODE: return "_ActTransportationModeCode";
            case _ACTPATIENTTRANSPORTATIONMODECODE: return "_ActPatientTransportationModeCode";
            case AFOOT: return "AFOOT";
            case AMBT: return "AMBT";
            case AMBAIR: return "AMBAIR";
            case AMBGRND: return "AMBGRND";
            case AMBHELO: return "AMBHELO";
            case LAWENF: return "LAWENF";
            case PRVTRN: return "PRVTRN";
            case PUBTRN: return "PUBTRN";
            case _OBSERVATIONTYPE: return "_ObservationType";
            case _ACTSPECOBSCODE: return "_ActSpecObsCode";
            case ARTBLD: return "ARTBLD";
            case DILUTION: return "DILUTION";
            case AUTOHIGH: return "AUTO-HIGH";
            case AUTOLOW: return "AUTO-LOW";
            case PRE: return "PRE";
            case RERUN: return "RERUN";
            case EVNFCTS: return "EVNFCTS";
            case INTFR: return "INTFR";
            case FIBRIN: return "FIBRIN";
            case HEMOLYSIS: return "HEMOLYSIS";
            case ICTERUS: return "ICTERUS";
            case LIPEMIA: return "LIPEMIA";
            case VOLUME: return "VOLUME";
            case AVAILABLE: return "AVAILABLE";
            case CONSUMPTION: return "CONSUMPTION";
            case CURRENT: return "CURRENT";
            case INITIAL: return "INITIAL";
            case _ANNOTATIONTYPE: return "_AnnotationType";
            case _ACTPATIENTANNOTATIONTYPE: return "_ActPatientAnnotationType";
            case ANNDI: return "ANNDI";
            case ANNGEN: return "ANNGEN";
            case ANNIMM: return "ANNIMM";
            case ANNLAB: return "ANNLAB";
            case ANNMED: return "ANNMED";
            case _GENETICOBSERVATIONTYPE: return "_GeneticObservationType";
            case GENE: return "GENE";
            case _IMMUNIZATIONOBSERVATIONTYPE: return "_ImmunizationObservationType";
            case OBSANTC: return "OBSANTC";
            case OBSANTV: return "OBSANTV";
            case _INDIVIDUALCASESAFETYREPORTTYPE: return "_IndividualCaseSafetyReportType";
            case PATADVEVNT: return "PAT_ADV_EVNT";
            case VACPROBLEM: return "VAC_PROBLEM";
            case _LOINCOBSERVATIONACTCONTEXTAGETYPE: return "_LOINCObservationActContextAgeType";
            case _216119: return "21611-9";
            case _216127: return "21612-7";
            case _295535: return "29553-5";
            case _305250: return "30525-0";
            case _309724: return "30972-4";
            case _MEDICATIONOBSERVATIONTYPE: return "_MedicationObservationType";
            case REPHALFLIFE: return "REP_HALF_LIFE";
            case SPLCOATING: return "SPLCOATING";
            case SPLCOLOR: return "SPLCOLOR";
            case SPLIMAGE: return "SPLIMAGE";
            case SPLIMPRINT: return "SPLIMPRINT";
            case SPLSCORING: return "SPLSCORING";
            case SPLSHAPE: return "SPLSHAPE";
            case SPLSIZE: return "SPLSIZE";
            case SPLSYMBOL: return "SPLSYMBOL";
            case _OBSERVATIONISSUETRIGGERCODEDOBSERVATIONTYPE: return "_ObservationIssueTriggerCodedObservationType";
            case _CASETRANSMISSIONMODE: return "_CaseTransmissionMode";
            case AIRTRNS: return "AIRTRNS";
            case ANANTRNS: return "ANANTRNS";
            case ANHUMTRNS: return "ANHUMTRNS";
            case BDYFLDTRNS: return "BDYFLDTRNS";
            case BLDTRNS: return "BLDTRNS";
            case DERMTRNS: return "DERMTRNS";
            case ENVTRNS: return "ENVTRNS";
            case FECTRNS: return "FECTRNS";
            case FOMTRNS: return "FOMTRNS";
            case FOODTRNS: return "FOODTRNS";
            case HUMHUMTRNS: return "HUMHUMTRNS";
            case INDTRNS: return "INDTRNS";
            case LACTTRNS: return "LACTTRNS";
            case NOSTRNS: return "NOSTRNS";
            case PARTRNS: return "PARTRNS";
            case PLACTRNS: return "PLACTRNS";
            case SEXTRNS: return "SEXTRNS";
            case TRNSFTRNS: return "TRNSFTRNS";
            case VECTRNS: return "VECTRNS";
            case WATTRNS: return "WATTRNS";
            case _OBSERVATIONQUALITYMEASUREATTRIBUTE: return "_ObservationQualityMeasureAttribute";
            case AGGREGATE: return "AGGREGATE";
            case COPY: return "COPY";
            case CRS: return "CRS";
            case DEF: return "DEF";
            case DISC: return "DISC";
            case FINALDT: return "FINALDT";
            case GUIDE: return "GUIDE";
            case IDUR: return "IDUR";
            case ITMCNT: return "ITMCNT";
            case KEY: return "KEY";
            case MEDT: return "MEDT";
            case MSD: return "MSD";
            case MSRADJ: return "MSRADJ";
            case MSRAGG: return "MSRAGG";
            case MSRIMPROV: return "MSRIMPROV";
            case MSRJUR: return "MSRJUR";
            case MSRRPTR: return "MSRRPTR";
            case MSRRPTTIME: return "MSRRPTTIME";
            case MSRSCORE: return "MSRSCORE";
            case MSRSET: return "MSRSET";
            case MSRTOPIC: return "MSRTOPIC";
            case MSRTP: return "MSRTP";
            case MSRTYPE: return "MSRTYPE";
            case RAT: return "RAT";
            case REF: return "REF";
            case SDE: return "SDE";
            case STRAT: return "STRAT";
            case TRANF: return "TRANF";
            case USE: return "USE";
            case _OBSERVATIONSEQUENCETYPE: return "_ObservationSequenceType";
            case TIMEABSOLUTE: return "TIME_ABSOLUTE";
            case TIMERELATIVE: return "TIME_RELATIVE";
            case _OBSERVATIONSERIESTYPE: return "_ObservationSeriesType";
            case _ECGOBSERVATIONSERIESTYPE: return "_ECGObservationSeriesType";
            case REPRESENTATIVEBEAT: return "REPRESENTATIVE_BEAT";
            case RHYTHM: return "RHYTHM";
            case _PATIENTIMMUNIZATIONRELATEDOBSERVATIONTYPE: return "_PatientImmunizationRelatedObservationType";
            case CLSSRM: return "CLSSRM";
            case GRADE: return "GRADE";
            case SCHL: return "SCHL";
            case SCHLDIV: return "SCHLDIV";
            case TEACHER: return "TEACHER";
            case _POPULATIONINCLUSIONOBSERVATIONTYPE: return "_PopulationInclusionObservationType";
            case DENEX: return "DENEX";
            case DENEXCEP: return "DENEXCEP";
            case DENOM: return "DENOM";
            case IPOP: return "IPOP";
            case IPPOP: return "IPPOP";
            case MSRPOPL: return "MSRPOPL";
            case MSRPOPLEX: return "MSRPOPLEX";
            case NUMER: return "NUMER";
            case NUMEX: return "NUMEX";
            case _PREFERENCEOBSERVATIONTYPE: return "_PreferenceObservationType";
            case PREFSTRENGTH: return "PREFSTRENGTH";
            case ADVERSEREACTION: return "ADVERSE_REACTION";
            case ASSERTION: return "ASSERTION";
            case CASESER: return "CASESER";
            case CDIO: return "CDIO";
            case CRIT: return "CRIT";
            case CTMO: return "CTMO";
            case DX: return "DX";
            case ADMDX: return "ADMDX";
            case DISDX: return "DISDX";
            case INTDX: return "INTDX";
            case NOI: return "NOI";
            case GISTIER: return "GISTIER";
            case HHOBS: return "HHOBS";
            case ISSUE: return "ISSUE";
            case _ACTADMINISTRATIVEDETECTEDISSUECODE: return "_ActAdministrativeDetectedIssueCode";
            case _ACTADMINISTRATIVEAUTHORIZATIONDETECTEDISSUECODE: return "_ActAdministrativeAuthorizationDetectedIssueCode";
            case NAT: return "NAT";
            case SUPPRESSED: return "SUPPRESSED";
            case VALIDAT: return "VALIDAT";
            case KEY204: return "KEY204";
            case KEY205: return "KEY205";
            case COMPLY: return "COMPLY";
            case DUPTHPY: return "DUPTHPY";
            case DUPTHPCLS: return "DUPTHPCLS";
            case DUPTHPGEN: return "DUPTHPGEN";
            case ABUSE: return "ABUSE";
            case FRAUD: return "FRAUD";
            case PLYDOC: return "PLYDOC";
            case PLYPHRM: return "PLYPHRM";
            case DOSE: return "DOSE";
            case DOSECOND: return "DOSECOND";
            case DOSEDUR: return "DOSEDUR";
            case DOSEDURH: return "DOSEDURH";
            case DOSEDURHIND: return "DOSEDURHIND";
            case DOSEDURL: return "DOSEDURL";
            case DOSEDURLIND: return "DOSEDURLIND";
            case DOSEH: return "DOSEH";
            case DOSEHINDA: return "DOSEHINDA";
            case DOSEHIND: return "DOSEHIND";
            case DOSEHINDSA: return "DOSEHINDSA";
            case DOSEHINDW: return "DOSEHINDW";
            case DOSEIVL: return "DOSEIVL";
            case DOSEIVLIND: return "DOSEIVLIND";
            case DOSEL: return "DOSEL";
            case DOSELINDA: return "DOSELINDA";
            case DOSELIND: return "DOSELIND";
            case DOSELINDSA: return "DOSELINDSA";
            case DOSELINDW: return "DOSELINDW";
            case MDOSE: return "MDOSE";
            case OBSA: return "OBSA";
            case AGE: return "AGE";
            case ADALRT: return "ADALRT";
            case GEALRT: return "GEALRT";
            case PEALRT: return "PEALRT";
            case COND: return "COND";
            case HGHT: return "HGHT";
            case LACT: return "LACT";
            case PREG: return "PREG";
            case WGHT: return "WGHT";
            case CREACT: return "CREACT";
            case GEN: return "GEN";
            case GEND: return "GEND";
            case LAB: return "LAB";
            case REACT: return "REACT";
            case ALGY: return "ALGY";
            case INT: return "INT";
            case RREACT: return "RREACT";
            case RALG: return "RALG";
            case RAR: return "RAR";
            case RINT: return "RINT";
            case BUS: return "BUS";
            case CODEINVAL: return "CODE_INVAL";
            case CODEDEPREC: return "CODE_DEPREC";
            case FORMAT: return "FORMAT";
            case ILLEGAL: return "ILLEGAL";
            case LENRANGE: return "LEN_RANGE";
            case LENLONG: return "LEN_LONG";
            case LENSHORT: return "LEN_SHORT";
            case MISSCOND: return "MISSCOND";
            case MISSMAND: return "MISSMAND";
            case NODUPS: return "NODUPS";
            case NOPERSIST: return "NOPERSIST";
            case REPRANGE: return "REP_RANGE";
            case MAXOCCURS: return "MAXOCCURS";
            case MINOCCURS: return "MINOCCURS";
            case _ACTADMINISTRATIVERULEDETECTEDISSUECODE: return "_ActAdministrativeRuleDetectedIssueCode";
            case KEY206: return "KEY206";
            case OBSOLETE: return "OBSOLETE";
            case _ACTSUPPLIEDITEMDETECTEDISSUECODE: return "_ActSuppliedItemDetectedIssueCode";
            case _ADMINISTRATIONDETECTEDISSUECODE: return "_AdministrationDetectedIssueCode";
            case _APPROPRIATENESSDETECTEDISSUECODE: return "_AppropriatenessDetectedIssueCode";
            case _INTERACTIONDETECTEDISSUECODE: return "_InteractionDetectedIssueCode";
            case FOOD: return "FOOD";
            case TPROD: return "TPROD";
            case DRG: return "DRG";
            case NHP: return "NHP";
            case NONRX: return "NONRX";
            case PREVINEF: return "PREVINEF";
            case DACT: return "DACT";
            case TIME: return "TIME";
            case ALRTENDLATE: return "ALRTENDLATE";
            case ALRTSTRTLATE: return "ALRTSTRTLATE";
            case _SUPPLYDETECTEDISSUECODE: return "_SupplyDetectedIssueCode";
            case ALLDONE: return "ALLDONE";
            case FULFIL: return "FULFIL";
            case NOTACTN: return "NOTACTN";
            case NOTEQUIV: return "NOTEQUIV";
            case NOTEQUIVGEN: return "NOTEQUIVGEN";
            case NOTEQUIVTHER: return "NOTEQUIVTHER";
            case TIMING: return "TIMING";
            case INTERVAL: return "INTERVAL";
            case MINFREQ: return "MINFREQ";
            case HELD: return "HELD";
            case TOOLATE: return "TOOLATE";
            case TOOSOON: return "TOOSOON";
            case HISTORIC: return "HISTORIC";
            case PATPREF: return "PATPREF";
            case PATPREFALT: return "PATPREFALT";
            case KSUBJ: return "KSUBJ";
            case KSUBT: return "KSUBT";
            case OINT: return "OINT";
            case ALG: return "ALG";
            case DALG: return "DALG";
            case EALG: return "EALG";
            case FALG: return "FALG";
            case DINT: return "DINT";
            case DNAINT: return "DNAINT";
            case EINT: return "EINT";
            case ENAINT: return "ENAINT";
            case FINT: return "FINT";
            case FNAINT: return "FNAINT";
            case NAINT: return "NAINT";
            case SEV: return "SEV";
            case _ROIOVERLAYSHAPE: return "_ROIOverlayShape";
            case CIRCLE: return "CIRCLE";
            case ELLIPSE: return "ELLIPSE";
            case POINT: return "POINT";
            case POLY: return "POLY";
            case C: return "C";
            case DIET: return "DIET";
            case BR: return "BR";
            case DM: return "DM";
            case FAST: return "FAST";
            case FORMULA: return "FORMULA";
            case GF: return "GF";
            case LF: return "LF";
            case LP: return "LP";
            case LQ: return "LQ";
            case LS: return "LS";
            case N: return "N";
            case NF: return "NF";
            case PAF: return "PAF";
            case PAR: return "PAR";
            case RD: return "RD";
            case SCH: return "SCH";
            case SUPPLEMENT: return "SUPPLEMENT";
            case T: return "T";
            case VLI: return "VLI";
            case DRUGPRG: return "DRUGPRG";
            case F: return "F";
            case PRLMN: return "PRLMN";
            case SECOBS: return "SECOBS";
            case SECCATOBS: return "SECCATOBS";
            case SECCLASSOBS: return "SECCLASSOBS";
            case SECCONOBS: return "SECCONOBS";
            case SECINTOBS: return "SECINTOBS";
            case SECALTINTOBS: return "SECALTINTOBS";
            case SECDATINTOBS: return "SECDATINTOBS";
            case SECINTCONOBS: return "SECINTCONOBS";
            case SECINTPRVOBS: return "SECINTPRVOBS";
            case SECINTPRVABOBS: return "SECINTPRVABOBS";
            case SECINTPRVRBOBS: return "SECINTPRVRBOBS";
            case SECINTSTOBS: return "SECINTSTOBS";
            case SECTRSTOBS: return "SECTRSTOBS";
            case TRSTACCRDOBS: return "TRSTACCRDOBS";
            case TRSTAGREOBS: return "TRSTAGREOBS";
            case TRSTCERTOBS: return "TRSTCERTOBS";
            case TRSTFWKOBS: return "TRSTFWKOBS";
            case TRSTLOAOBS: return "TRSTLOAOBS";
            case TRSTMECOBS: return "TRSTMECOBS";
            case SUBSIDFFS: return "SUBSIDFFS";
            case WRKCOMP: return "WRKCOMP";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActCode";
        }
        public String getDefinition() {
          switch (this) {
            case _ACTACCOUNTCODE: return "An account represents a grouping of financial transactions that are tracked and reported together with a single balance. 	 	Examples of account codes (types) are Patient billing accounts (collection of charges), Cost centers; Cash.";
            case ACCTRECEIVABLE: return "An account for collecting charges, reversals, adjustments and payments, including deductibles, copayments, coinsurance (financial transactions) credited or debited to the account receivable account for a patient's encounter.";
            case CASH: return "Cash";
            case CC: return "Description: Types of advance payment to be made on a plastic card usually issued by a financial institution used of purchasing services and/or products.";
            case AE: return "American Express";
            case DN: return "Diner's Club";
            case DV: return "Discover Card";
            case MC: return "Master Card";
            case V: return "Visa";
            case PBILLACCT: return "An account representing charges and credits (financial transactions) for a patient's encounter.";
            case _ACTADJUDICATIONCODE: return "Includes coded responses that will occur as a result of the adjudication of an electronic invoice at a summary level and provides guidance on interpretation of the referenced adjudication results.";
            case _ACTADJUDICATIONGROUPCODE: return "Catagorization of grouping criteria for the associated transactions and/or summary (totals, subtotals).";
            case CONT: return "Transaction counts and value totals by Contract Identifier.";
            case DAY: return "Transaction counts and value totals for each calendar day within the date range specified.";
            case LOC: return "Transaction counts and value totals by service location (e.g clinic).";
            case MONTH: return "Transaction counts and value totals for each calendar month within the date range specified.";
            case PERIOD: return "Transaction counts and value totals for the date range specified.";
            case PROV: return "Transaction counts and value totals by Provider Identifier.";
            case WEEK: return "Transaction counts and value totals for each calendar week within the date range specified.";
            case YEAR: return "Transaction counts and value totals for each calendar year within the date range specified.";
            case AA: return "The invoice element has been accepted for payment but one or more adjustment(s) have been made to one or more invoice element line items (component charges).  \r\n\n                        Also includes the concept 'Adjudicate as zero' and items not covered under a particular Policy.  \r\n\n                        Invoice element can be reversed (nullified).  \r\n\n                        Recommend that the invoice element is saved for DUR (Drug Utilization Reporting).";
            case ANF: return "The invoice element has been accepted for payment but one or more adjustment(s) have been made to one or more invoice element line items (component charges) without changing the amount.  \r\n\n                        Invoice element can be reversed (nullified).  \r\n\n                        Recommend that the invoice element is saved for DUR (Drug Utilization Reporting).";
            case AR: return "The invoice element has passed through the adjudication process but payment is refused due to one or more reasons.\r\n\n                        Includes items such as patient not covered, or invoice element is not constructed according to payer rules (e.g. 'invoice submitted too late').\r\n\n                        If one invoice element line item in the invoice element structure is rejected, the remaining line items may not be adjudicated and the complete group is treated as rejected.\r\n\n                        A refused invoice element can be forwarded to the next payer (for Coordination of Benefits) or modified and resubmitted to refusing payer.\r\n\n                        Invoice element cannot be reversed (nullified) as there is nothing to reverse.  \r\n\n                        Recommend that the invoice element is not saved for DUR (Drug Utilization Reporting).";
            case AS: return "The invoice element was/will be paid exactly as submitted, without financial adjustment(s).\r\n\n                        If the dollar amount stays the same, but the billing codes have been amended or financial adjustments have been applied through the adjudication process, the invoice element is treated as \"Adjudicated with Adjustment\".\r\n\n                        If information items are included in the adjudication results that do not affect the monetary amounts paid, then this is still Adjudicated as Submitted (e.g. 'reached Plan Maximum on this Claim').  \r\n\n                        Invoice element can be reversed (nullified).  \r\n\n                        Recommend that the invoice element is saved for DUR (Drug Utilization Reporting).";
            case _ACTADJUDICATIONRESULTACTIONCODE: return "Actions to be carried out by the recipient of the Adjudication Result information.";
            case DISPLAY: return "The adjudication result associated is to be displayed to the receiver of the adjudication result.";
            case FORM: return "The adjudication result associated is to be printed on the specified form, which is then provided to the covered party.";
            case _ACTBILLABLEMODIFIERCODE: return "Definition:An identifying modifier code for healthcare interventions or procedures.";
            case CPTM: return "Description:CPT modifier codes are found in Appendix A of CPT 2000 Standard Edition.";
            case HCPCSA: return "Description:HCPCS Level II (HCFA-assigned) and Carrier-assigned (Level III) modifiers are reported in Appendix A of CPT 2000 Standard Edition and in the Medicare Bulletin.";
            case _ACTBILLINGARRANGEMENTCODE: return "The type of provision(s)  made for reimbursing for the deliver of healthcare services and/or goods provided by a Provider, over a specified period.";
            case BLK: return "A billing arrangement where a Provider charges a lump sum to provide a prescribed group (volume) of services to a single patient which occur over a period of time.  Services included in the block may vary.  \r\n\n                        This billing arrangement is also known as Program of Care for some specific Payors and Program Fees for other Payors.";
            case CAP: return "A billing arrangement where the payment made to a Provider is determined by analyzing one or more demographic attributes about the persons/patients who are enrolled with the Provider (in their practice).";
            case CONTF: return "A billing arrangement where a Provider charges a lump sum to provide a particular volume of one or more interventions/procedures or groups of interventions/procedures.";
            case FINBILL: return "A billing arrangement where a Provider charges for non-clinical items.  This includes interest in arrears, mileage, etc.  Clinical content is not 	included in Invoices submitted with this type of billing arrangement.";
            case ROST: return "A billing arrangement where funding is based on a list of individuals registered as patients of the Provider.";
            case SESS: return "A billing arrangement where a Provider charges a sum to provide a group (volume) of interventions/procedures to one or more patients within a defined period of time, typically on the same date.  Interventions/procedures included in the session may vary.";
            case _ACTBOUNDEDROICODE: return "Type of bounded ROI.";
            case ROIFS: return "A fully specified bounded Region of Interest (ROI) delineates a ROI in which only those dimensions participate that are specified by boundary criteria, whereas all other dimensions are excluded.  For example a ROI to mark an episode of \"ST elevation\" in a subset of the EKG leads V2, V3, and V4 would include 4 boundaries, one each for time, V2, V3, and V4.";
            case ROIPS: return "A partially specified bounded Region of Interest (ROI) specifies a ROI in which at least all values in the dimensions specified by the boundary criteria participate. For example, if an episode of ventricular fibrillations (VFib) is observed, it usually doesn't make sense to exclude any EKG leads from the observation and the partially specified ROI would contain only one boundary for time indicating the time interval where VFib was observed.";
            case _ACTCAREPROVISIONCODE: return "Description:The type and scope of responsibility taken-on by the performer of the Act for a specific subject of care.";
            case _ACTCREDENTIALEDCARECODE: return "Description:The type and scope of legal and/or professional responsibility taken-on by the performer of the Act for a specific subject of care as described by a credentialing agency, i.e. government or non-government agency. Failure in executing this Act may result in loss of credential to the person or organization who participates as performer of the Act. Excludes employment agreements.\r\n\n                        \n                           Example:Hospital license; physician license; clinic accreditation.";
            case _ACTCREDENTIALEDCAREPROVISIONPERSONCODE: return "Description:The type and scope of legal and/or professional responsibility taken-on by the performer of the Act for a specific subject of care as described by an agency for credentialing individuals.";
            case CACC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CAIC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CAMC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CANC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CAPC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CBGC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CCCC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CCGC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CCPC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CCSC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CDEC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CDRC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CEMC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CFPC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CIMC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CMGC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CNEC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board";
            case CNMC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CNQC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CNSC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case COGC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case COMC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case COPC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case COSC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case COTC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CPEC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CPGC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CPHC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CPRC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CPSC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CPYC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CROC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CRPC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CSUC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CTSC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CURC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case CVSC: return "Description:Scope of responsibility taken on for specialty care as defined by the respective Specialty Board.";
            case LGPC: return "Description:Scope of responsibility taken-on for physician care of a patient as defined by a governmental licensing agency.";
            case _ACTCREDENTIALEDCAREPROVISIONPROGRAMCODE: return "Description:The type and scope of legal and/or professional responsibility taken-on by the performer of the Act for a specific subject of care as described by an agency for credentialing programs within organizations.";
            case AALC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.";
            case AAMC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.";
            case ABHC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.";
            case ACAC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.";
            case ACHC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.";
            case AHOC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.";
            case ALTC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.";
            case AOSC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the respective accreditation agency.";
            case CACS: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CAMI: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CAST: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CBAR: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CCAD: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CCAR: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CDEP: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CDGD: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CDIA: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CEPI: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CFEL: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CHFC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CHRO: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CHYP: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CMIH: return "Description:.";
            case CMSC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case COJR: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CONC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case COPD: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CORT: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CPAD: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CPND: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CPST: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CSDM: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CSIC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CSLD: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CSPT: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CTBU: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CVDC: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CWMA: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case CWOH: return "Description:Scope of responsibility taken on by an organization for care of a patient as defined by the disease management certification agency.";
            case _ACTENCOUNTERCODE: return "Domain provides codes that qualify the ActEncounterClass (ENC)";
            case AMB: return "A comprehensive term for health care provided in a healthcare facility (e.g. a practitioneraTMs office, clinic setting, or hospital) on a nonresident basis. The term ambulatory usually implies that the patient has come to the location and is not assigned to a bed. Sometimes referred to as an outpatient encounter.";
            case EMER: return "A patient encounter that takes place at a dedicated healthcare service delivery location where the patient receives immediate evaluation and treatment, provided until the patient can be discharged or responsibility for the patient's care is transferred elsewhere (for example, the patient could be admitted as an inpatient or transferred to another facility.)";
            case FLD: return "A patient encounter that takes place both outside a dedicated service delivery location and outside a patient's residence. Example locations might include an accident site and at a supermarket.";
            case HH: return "Healthcare encounter that takes place in the residence of the patient or a designee";
            case IMP: return "A patient encounter where a patient is admitted by a hospital or equivalent facility, assigned to a location where patients generally stay at least overnight and provided with room, board, and continuous nursing service.";
            case ACUTE: return "An acute inpatient encounter.";
            case NONAC: return "Any category of inpatient encounter except 'acute'";
            case PRENC: return "A patient encounter where patient is scheduled or planned to receive service delivery in the future, and the patient is given a pre-admission account number. When the patient comes back for subsequent service, the pre-admission encounter is selected and is encapsulated into the service registration, and a new account number is generated.\r\n\n                        \n                           Usage Note: This is intended to be used in advance of encounter types such as ambulatory, inpatient encounter, virtual, etc.";
            case SS: return "An encounter where the patient is admitted to a health care facility for a predetermined length of time, usually less than 24 hours.";
            case VR: return "A patient encounter where the patient and the practitioner(s) are not in the same physical location. Examples include telephone conference, email exchange, robotic surgery, and televideo conference.";
            case _ACTMEDICALSERVICECODE: return "General category of medical service provided to the patient during their encounter.";
            case ALC: return "Provision of Alternate Level of Care to a patient in an acute bed.  Patient is waiting for placement in a long-term care facility and is unable to return home.";
            case CARD: return "Provision of diagnosis and treatment of diseases and disorders affecting the heart";
            case CHR: return "Provision of recurring care for chronic illness.";
            case DNTL: return "Provision of treatment for oral health and/or dental surgery.";
            case DRGRHB: return "Provision of treatment for drug abuse.";
            case GENRL: return "General care performed by a general practitioner or family doctor as a responsible provider for a patient.";
            case MED: return "Provision of diagnostic and/or therapeutic treatment.";
            case OBS: return "Provision of care of women during pregnancy, childbirth and immediate postpartum period.  Also known as Maternity.";
            case ONC: return "Provision of treatment and/or diagnosis related to tumors and/or cancer.";
            case PALL: return "Provision of care for patients who are living or dying from an advanced illness.";
            case PED: return "Provision of diagnosis and treatment of diseases and disorders affecting children.";
            case PHAR: return "Pharmaceutical care performed by a pharmacist.";
            case PHYRHB: return "Provision of treatment for physical injury.";
            case PSYCH: return "Provision of treatment of psychiatric disorder relating to mental illness.";
            case SURG: return "Provision of surgical treatment.";
            case _ACTCLAIMATTACHMENTCATEGORYCODE: return "Description: Coded types of attachments included to support a healthcare claim.";
            case AUTOATTCH: return "Description: Automobile Information Attachment";
            case DOCUMENT: return "Description: Document Attachment";
            case HEALTHREC: return "Description: Health Record Attachment";
            case IMG: return "Description: Image Attachment";
            case LABRESULTS: return "Description: Lab Results Attachment";
            case MODEL: return "Description: Digital Model Attachment";
            case WIATTCH: return "Description: Work Injury related additional Information Attachment";
            case XRAY: return "Description: Digital X-Ray Attachment";
            case _ACTCONSENTTYPE: return "Definition: The type of consent directive, e.g., to consent or dissent to collect, access, or use in specific ways within an EHRS or for health information exchange; or to disclose  health information  for purposes such as research.";
            case ICOL: return "Definition: Consent to have healthcare information collected in an electronic health record.  This entails that the information may be used in analysis, modified, updated.";
            case IDSCL: return "Definition: Consent to have collected healthcare information disclosed.";
            case INFA: return "Definition: Consent to access healthcare information.";
            case INFAO: return "Definition: Consent to access or \"read\" only, which entails that the information is not to be copied, screen printed, saved, emailed, stored, re-disclosed or altered in any way.  This level ensures that data which is masked or to which access is restricted will not be.\r\n\n                        \n                           Example: Opened and then emailed or screen printed for use outside of the consent directive purpose.";
            case INFASO: return "Definition: Consent to access and save only, which entails that access to the saved copy will remain locked.";
            case IRDSCL: return "Definition: Information re-disclosed without the patient's consent.";
            case RESEARCH: return "Definition: Consent to have healthcare information in an electronic health record accessed for research purposes.";
            case RSDID: return "Definition: Consent to have de-identified healthcare information in an electronic health record that is accessed for research purposes, but without consent to re-identify the information under any circumstance.";
            case RSREID: return "Definition: Consent to have de-identified healthcare information in an electronic health record that is accessed for research purposes re-identified under specific circumstances outlined in the consent.\r\n\n                        \n                           Example:: Where there is a need to inform the subject of potential health issues.";
            case _ACTCONTAINERREGISTRATIONCODE: return "Constrains the ActCode to the domain of Container Registration";
            case ID: return "Used by one system to inform another that it has received a container.";
            case IP: return "Used by one system to inform another that the container is in position for specimen transfer (e.g., container removal from track, pipetting, etc.).";
            case L: return "Used by one system to inform another that the container has been released from that system.";
            case M: return "Used by one system to inform another that the container did not arrive at its next expected location.";
            case O: return "Used by one system to inform another that the specific container is being processed by the equipment. It is useful as a response to a query about Container Status, when the specific step of the process is not relevant.";
            case R: return "Status is used by one system to inform another that the processing has been completed, but the container has not been released from that system.";
            case X: return "Used by one system to inform another that the container is no longer available within the scope of the system (e.g., tube broken or discarded).";
            case _ACTCONTROLVARIABLE: return "An observation form that determines parameters or attributes of an Act. Examples are the settings of a ventilator machine as parameters of a ventilator treatment act; the controls on dillution factors of a chemical analyzer as a parameter of a laboratory observation act; the settings of a physiologic measurement assembly (e.g., time skew) or the position of the body while measuring blood pressure.\r\n\n                        Control variables are forms of observations because just as with clinical observations, the Observation.code determines the parameter and the Observation.value assigns the value. While control variables sometimes can be observed (by noting the control settings or an actually measured feedback loop) they are not primary observations, in the sense that a control variable without a primary act is of no use (e.g., it makes no sense to record a blood pressure position without recording a blood pressure, whereas it does make sense to record a systolic blood pressure without a diastolic blood pressure).";
            case AUTO: return "Specifies whether or not automatic repeat testing is to be initiated on specimens.";
            case ENDC: return "A baseline value for the measured test that is inherently contained in the diluent.  In the calculation of the actual result for the measured test, this baseline value is normally considered.";
            case REFLEX: return "Specifies whether or not further testing may be automatically or manually initiated on specimens.";
            case _ACTCOVERAGECONFIRMATIONCODE: return "Response to an insurance coverage eligibility query or authorization request.";
            case _ACTCOVERAGEAUTHORIZATIONCONFIRMATIONCODE: return "Indication of authorization for healthcare service(s) and/or product(s).  If authorization is approved, funds are set aside.";
            case AUTH: return "Authorization approved and funds have been set aside to pay for specified healthcare service(s) and/or product(s) within defined criteria for the authorization.";
            case NAUTH: return "Authorization for specified healthcare service(s) and/or product(s) denied.";
            case _ACTCOVERAGELIMITCODE: return "Criteria that are applicable to the authorized coverage.";
            case _ACTCOVERAGEQUANTITYLIMITCODE: return "Maximum amount paid or maximum number of services/products covered; or maximum amount or number covered during a specified time period under the policy or program.";
            case COVPRD: return "Codes representing the time period during which coverage is available; or financial participation requirements are in effect.";
            case LFEMX: return "Definition: Maximum amount paid by payer or covered party; or maximum number of services or products covered under the policy or program during a covered party's lifetime.";
            case NETAMT: return "Maximum net amount that will be covered for the product or service specified.";
            case PRDMX: return "Definition: Maximum amount paid by payer or covered party; or maximum number of services/products covered under the policy or program by time period specified by the effective time on the act.";
            case UNITPRICE: return "Maximum unit price that will be covered for the authorized product or service.";
            case UNITQTY: return "Maximum number of items that will be covered of the product or service specified.";
            case COVMX: return "Definition: Codes representing the maximum coverate or financial participation requirements.";
            case _ACTCOVERAGETYPECODE: return "Definition: Set of codes indicating the type of insurance policy or program that pays for the cost of benefits provided to covered parties.";
            case _ACTINSURANCEPOLICYCODE: return "Set of codes indicating the type of insurance policy or other source of funds to cover healthcare costs.";
            case EHCPOL: return "Private insurance policy that provides coverage in addition to other policies (e.g. in addition to a Public Healthcare insurance policy).";
            case HSAPOL: return "Insurance policy that provides for an allotment of funds replenished on a periodic (e.g. annual) basis. The use of the funds under this policy is at the 	discretion of the covered party.";
            case AUTOPOL: return "Insurance policy for injuries sustained in an automobile accident.  Will also typically covered non-named parties to the policy, such as pedestrians 	and passengers.";
            case COL: return "Definition: An automobile insurance policy under which the insurance company will cover the cost of damages to an automobile owned by the named insured that are caused by accident or intentionally by another party.";
            case UNINSMOT: return "Definition: An automobile insurance policy under which the insurance company will indemnify a loss for which another motorist is liable if that motorist is unable to pay because he or she is uninsured.  Coverage under the policy applies to bodily injury damages only.  Injuries to the covered party caused by a hit-and-run driver are also covered.";
            case PUBLICPOL: return "Insurance policy funded by a public health system such as a provincial or national health plan.  Examples include BC MSP (British Columbia 	Medical Services Plan) OHIP (Ontario Health Insurance Plan), NHS (National Health Service).";
            case DENTPRG: return "Definition: A public or government health program that administers and funds coverage for dental care to assist program eligible who meet financial and health status criteria.";
            case DISEASEPRG: return "Definition: A public or government health program that administers and funds coverage for health and social services to assist program eligible who meet financial and health status criteria related to a particular disease.\r\n\n                        \n                           Example: Reproductive health, sexually transmitted disease, and end renal disease programs.";
            case CANPRG: return "Definition: A program that provides low-income, uninsured, and underserved women access to timely, high-quality screening and diagnostic services, to detect breast and cervical cancer at the earliest stages.\r\n\n                        \n                           Example: To improve women's access to screening for breast and cervical cancers, Congress passed the Breast and Cervical Cancer Mortality Prevention Act of 1990, which guided CDC in creating the National Breast and Cervical Cancer Early Detection Program (NBCCEDP), which  provides access to critical breast and cervical cancer screening services for underserved women in the United States.  An estimated 7 to 10% of U.S. women of screening age are eligible to receive NBCCEDP services. Federal guidelines establish an eligibility baseline to direct services to uninsured and underinsured women at or below 250% of federal poverty level; ages 18 to 64 for cervical screening; ages 40 to 64 for breast screening.";
            case ENDRENAL: return "Definition: A public or government program that administers publicly funded coverage of kidney dialysis and kidney transplant services.\r\n\n                        Example: In the U.S., the Medicare End-stage Renal Disease program (ESRD), the National Kidney Foundation (NKF) American Kidney Fund (AKF) The Organ Transplant Fund.";
            case HIVAIDS: return "Definition: Government administered and funded HIV-AIDS program for beneficiaries meeting financial and health status criteria.  Administration, funding levels, eligibility criteria, covered benefits, provider types, and financial participation are typically set by a regulatory process.  Payer responsibilities for administering the program may be delegated to contractors.\r\n\n                        \n                           Example: In the U.S., the Ryan White program, which is administered by the Health Resources and Services Administration.";
            case MANDPOL: return "mandatory health program";
            case MENTPRG: return "Definition: Government administered and funded mental health program for beneficiaries meeting financial and mental health status criteria.  Administration, funding levels, eligibility criteria, covered benefits, provider types, and financial participation are typically set by a regulatory process.  Payer responsibilities for administering the program may be delegated to contractors.\r\n\n                        \n                           Example: In the U.S., states receive funding for substance use programs from the Substance Abuse Mental Health Administration (SAMHSA).";
            case SAFNET: return "Definition: Government administered and funded program to support provision of care to underserved populations through safety net clinics.\r\n\n                        \n                           Example: In the U.S., safety net providers such as federally qualified health centers (FQHC) receive funding under PHSA Section 330 grants administered by the Health Resources and Services Administration.";
            case SUBPRG: return "Definition: Government administered and funded substance use program for beneficiaries meeting financial, substance use behavior, and health status criteria.  Beneficiaries may be required to enroll as a result of legal proceedings.  Administration, funding levels, eligibility criteria, covered benefits, provider types, and financial participation are typically set by a regulatory process.  Payer responsibilities for administering the program may be delegated to contractors.\r\n\n                        \n                           Example: In the U.S., states receive funding for substance use programs from the Substance Abuse Mental Health Administration (SAMHSA).";
            case SUBSIDIZ: return "Definition: A government health program that provides coverage for health services to persons meeting eligibility criteria such as income, location of residence, access to other coverages, health condition, and age, the cost of which is to some extent subsidized by public funds.";
            case SUBSIDMC: return "Definition: A government health program that provides coverage through managed care contracts for health services to persons meeting eligibility criteria such as income, location of residence, access to other coverages, health condition, and age, the cost of which is to some extent subsidized by public funds. \r\n\n                        \n                           Discussion: The structure and business processes for underwriting and administering a subsidized managed care program is further specified by the Underwriter and Payer Role.class and Role.code.";
            case SUBSUPP: return "Definition: A government health program that provides coverage for health services to persons meeting eligibility criteria for a supplemental health policy or program such as income, location of residence, access to other coverages, health condition, and age, the cost of which is to some extent subsidized by public funds.\r\n\n                        \n                           Example:  Supplemental health coverage program may cover the cost of a health program or policy financial participations, such as the copays and the premiums, and may provide coverage for services in addition to those covered under the supplemented health program or policy.  In the U.S., Medicaid programs may pay the premium for a covered party who is also covered under the  Medicare program or a private health policy.\r\n\n                        \n                           Discussion: The structure and business processes for underwriting and administering a subsidized supplemental retiree health program is further specified by the Underwriter and Payer Role.class and Role.code.";
            case WCBPOL: return "Insurance policy for injuries sustained in the work place or in the course of employment.";
            case _ACTINSURANCETYPECODE: return "Definition: Set of codes indicating the type of insurance policy.  Insurance, in law and economics, is a form of risk management primarily used to hedge against the risk of potential financial loss. Insurance is defined as the equitable transfer of the risk of a potential loss, from one entity to another, in exchange for a premium and duty of care. A policy holder is an individual or an organization enters into a contract with an underwriter which stipulates that, in exchange for payment of a sum of money (a premium), one or more covered parties (insureds) is guaranteed compensation for losses resulting from certain perils under specified conditions.  The underwriter analyzes the risk of loss, makes a decision as to whether the risk is insurable, and prices the premium accordingly.  A policy provides benefits that indemnify or cover the cost of a loss incurred by a covered party, and may include coverage for services required to remediate a loss.  An insurance policy contains pertinent facts about the policy holder, the insurance coverage, the covered parties, and the insurer.  A policy may include exemptions and provisions specifying the extent to which the indemnification clause cannot be enforced for intentional tortious conduct of a covered party, e.g., whether the covered parties are jointly or severably insured.\r\n\n                        \n                           Discussion: In contrast to programs, an insurance policy has one or more policy holders, who own the policy.  The policy holder may be the covered party, a relative of the covered party, a partnership, or a corporation, e.g., an employer.  A subscriber of a self-insured health insurance policy is a policy holder.  A subscriber of an employer sponsored health insurance policy is holds a certificate of coverage, but is not a policy holder; the policy holder is the employer.  See CoveredRoleType.";
            case _ACTHEALTHINSURANCETYPECODE: return "Definition: Set of codes indicating the type of health insurance policy that covers health services provided to covered parties.  A health insurance policy is a written contract for insurance between the insurance company and the policyholder, and contains pertinent facts about the policy owner (the policy holder), the health insurance coverage, the insured subscribers and dependents, and the insurer.  Health insurance is typically administered in accordance with a plan, which specifies (1) the type of health services and health conditions that will be covered under what circumstances (e.g., exclusion of a pre-existing condition, service must be deemed medically necessary; service must not be experimental; service must provided in accordance with a protocol; drug must be on a formulary; service must be prior authorized; or be a referral from a primary care provider); (2) the type and affiliation of providers (e.g., only allopathic physicians, only in network, only providers employed by an HMO); (3) financial participations required of covered parties (e.g., co-pays, coinsurance, deductibles, out-of-pocket); and (4) the manner in which services will be paid (e.g., under indemnity or fee-for-service health plans, the covered party typically pays out-of-pocket and then file a claim for reimbursement, while health plans that have contractual relationships with providers, i.e., network providers, typically do not allow the providers to bill the covered party for the cost of the service until after filing a claim with the payer and receiving reimbursement).";
            case DENTAL: return "Definition: A health insurance policy that that covers benefits for dental services.";
            case DISEASE: return "Definition: A health insurance policy that covers benefits for healthcare services provided for named conditions under the policy, e.g., cancer, diabetes, or HIV-AIDS.";
            case DRUGPOL: return "Definition: A health insurance policy that covers benefits for prescription drugs, pharmaceuticals, and supplies.";
            case HIP: return "Definition: A health insurance policy that covers healthcare benefits by protecting covered parties from medical expenses arising from health conditions, sickness, or accidental injury as well as preventive care. Health insurance policies explicitly exclude coverage for losses insured under a disability policy, workers' compensation program, liability insurance (including automobile insurance); or for medical expenses, coverage for on-site medical clinics or for limited dental or vision benefits when these are provided under a separate policy.\r\n\n                        \n                           Discussion: Health insurance policies are offered by health insurance plans that typically reimburse providers for covered services on a fee-for-service basis, that is, a fee that is the allowable amount that a provider may charge.  This is in contrast to managed care plans, which typically prepay providers a per-member/per-month amount or capitation as reimbursement for all covered services rendered.  Health insurance plans include indemnity and healthcare services plans.";
            case LTC: return "Definition: An insurance policy that covers benefits for long-term care services people need when they no longer can care for themselves. This may be due to an accident, disability, prolonged illness or the simple process of aging. Long-term care services assist with activities of daily living including:\r\n\n                        \n                           \n                              Help at home with day-to-day activities, such as cooking, cleaning, bathing and dressing\r\n\n                           \n                           \n                              Care in the community, such as in an adult day care facility\r\n\n                           \n                           \n                              Supervised care provided in an assisted living facility\r\n\n                           \n                           \n                              Skilled care provided in a nursing home";
            case MCPOL: return "Definition: Government mandated program providing coverage, disability income, and vocational rehabilitation for injuries sustained in the work place or in the course of employment.  Employers may either self-fund the program, purchase commercial coverage, or pay a premium to a government entity that administers the program.  Employees may be required to pay premiums toward the cost of coverage as well.\r\n\n                        Managed care policies specifically exclude coverage for losses insured under a disability policy, workers' compensation program, liability insurance (including automobile insurance); or for medical expenses, coverage for on-site medical clinics or for limited dental or vision benefits when these are provided under a separate policy.\r\n\n                        \n                           Discussion: Managed care policies are offered by managed care plans that contract with selected providers or health care organizations to provide comprehensive health care at a discount to covered parties and coordinate the financing and delivery of health care. Managed care uses medical protocols and procedures agreed on by the medical profession to be cost effective, also known as medical practice guidelines. Providers are typically reimbursed for covered services by a capitated amount on a per member per month basis that may reflect difference in the health status and level of services anticipated to be needed by the member.";
            case POS: return "Definition: A policy for a health plan that has features of both an HMO and a FFS plan.  Like an HMO, a POS plan encourages the use its HMO network to maintain discounted fees with participating providers, but recognizes that sometimes covered parties want to choose their own provider.  The POS plan allows a covered party to use providers who are not part of the HMO network (non-participating providers).  However, there is a greater cost associated with choosing these non-network providers. A covered party will usually pay deductibles and coinsurances that are substantially higher than the payments when he or she uses a plan provider. Use of non-participating providers often requires the covered party to pay the provider directly and then to file a claim for reimbursement, like in an FFS plan.";
            case HMO: return "Definition: A policy for a health plan that provides coverage for health care only through contracted or employed physicians and hospitals located in particular geographic or service areas.  HMOs emphasize prevention and early detection of illness. Eligibility to enroll in an HMO is determined by where a covered party lives or works.";
            case PPO: return "Definition: A network-based, managed care plan that allows a covered party to choose any health care provider. However, if care is received from a \"preferred\" (participating in-network) provider, there are generally higher benefit coverage and lower deductibles.";
            case MENTPOL: return "Definition: A health insurance policy that covers benefits for mental health services and prescriptions.";
            case SUBPOL: return "Definition: A health insurance policy that covers benefits for substance use services.";
            case VISPOL: return "Definition: Set of codes for a policy that provides coverage for health care expenses arising from vision services.\r\n\n                        A health insurance policy that covers benefits for vision care services, prescriptions, and products.";
            case DIS: return "Definition: An insurance policy that provides a regular payment to compensate for income lost due to the covered party's inability to work because of illness or injury.";
            case EWB: return "Definition: An insurance policy under a benefit plan run by an employer or employee organization for the purpose of providing benefits other than pension-related to employees and their families. Typically provides health-related benefits, benefits for disability, disease or unemployment, or day care and scholarship benefits, among others.  An employer sponsored health policy includes coverage of health care expenses arising from sickness or accidental injury, coverage for on-site medical clinics or for dental or vision benefits, which are typically provided under a separate policy.  Coverage excludes health care expenses covered by accident or disability, workers' compensation, liability or automobile insurance.";
            case FLEXP: return "Definition:  An insurance policy that covers qualified benefits under a Flexible Benefit plan such as group medical insurance, long and short term disability income insurance, group term life insurance for employees only up to $50,000 face amount, specified disease coverage such as a cancer policy, dental and/or vision insurance, hospital indemnity insurance, accidental death and dismemberment insurance, a medical expense reimbursement plan and a dependent care reimbursement plan.\r\n\n                        \n                            Discussion: See UnderwriterRoleTypeCode flexible benefit plan which is defined as a benefit plan that allows employees to choose from several life, health, disability, dental, and other insurance plans according to their individual needs. Also known as cafeteria plans.  Authorized under Section 125 of the Revenue Act of 1978.";
            case LIFE: return "Definition: A policy under which the insurer agrees to pay a sum of money upon the occurrence of the covered partys death. In return, the policyholder agrees to pay a stipulated amount called a premium at regular intervals.  Life insurance indemnifies the beneficiary for the loss of the insurable interest that a beneficiary has in the life of a covered party.  For persons related by blood, a substantial interest established through love and affection, and for all other persons, a lawful and substantial economic interest in having the life of the insured continue. An insurable interest is required when purchasing life insurance on another person. Specific exclusions are often written into the contract to limit the liability of the insurer; for example claims resulting from suicide or relating to war, riot and civil commotion.\r\n\n                        \n                           Discussion:A life insurance policy may be used by the covered party as a source of health care coverage in the case of  a viatical settlement, which is the sale of a life insurance policy by the policy owner, before the policy matures. Such a sale, at a price discounted from the face amount of the policy but usually in excess of the premiums paid or current cash surrender value, provides the seller an immediate cash settlement. Generally, viatical settlements involve insured individuals with a life expectancy of less than two years. In countries without state-subsidized healthcare and high healthcare costs (e.g. United States), this is a practical way to pay extremely high health insurance premiums that severely ill people face. Some people are also familiar with life settlements, which are similar transactions but involve insureds with longer life expectancies (two to fifteen years).";
            case ANNU: return "Definition: A policy that, after an initial premium or premiums, pays out a sum at pre-determined intervals.\r\n\n                        For example, a policy holder may pay $10,000, and in return receive $150 each month until he dies; or $1,000 for each of 14 years or death benefits if he dies before the full term of the annuity has elapsed.";
            case TLIFE: return "Definition: Life insurance under which the benefit is payable only if the insured dies during a specified period. If an insured dies during that period, the beneficiary receives the death payments. If the insured survives, the policy ends and the beneficiary receives nothing.";
            case ULIFE: return "Definition: Life insurance under which the benefit is payable upon the insuredaTMs death or diagnosis of a terminal illness.  If an insured dies during that period, the beneficiary receives the death payments. If the insured survives, the policy ends and the beneficiary receives nothing";
            case PNC: return "Definition: A type of insurance that covers damage to or loss of the policyholderaTMs property by providing payments for damages to property damage or the injury or death of living subjects.  The terms \"casualty\" and \"liability\" insurance are often used interchangeably. Both cover the policyholder's legal liability for damages caused to other persons and/or their property.";
            case REI: return "Definition: An agreement between two or more insurance companies by which the risk of loss is proportioned. Thus the risk of loss is spread and a disproportionately large loss under a single policy does not fall on one insurance company. Acceptance by an insurer, called a reinsurer, of all or part of the risk of loss of another insurance company.\r\n\n                        \n                           Discussion: Reinsurance is a means by which an insurance company can protect itself against the risk of losses with other insurance companies. Individuals and corporations obtain insurance policies to provide protection for various risks (hurricanes, earthquakes, lawsuits, collisions, sickness and death, etc.). Reinsurers, in turn, provide insurance to insurance companies.\r\n\n                        For example, an HMO may purchase a reinsurance policy to protect itself from losing too much money from one insured's particularly expensive health care costs. An insurance company issuing an automobile liability policy, with a limit of $100,000 per accident may reinsure its liability in excess of $10,000. A fire insurance company which issues a large policy generally reinsures a portion of the risk with one or several other companies. Also called risk control insurance or stop-loss insurance.";
            case SURPL: return "Definition: \n                        \r\n\n                        \n                           \n                              A risk or part of a risk for which there is no normal insurance market available.\r\n\n                           \n                           \n                              Insurance written by unauthorized insurance companies. Surplus lines insurance is insurance placed with unauthorized insurance companies through licensed surplus lines agents or brokers.";
            case UMBRL: return "Definition: A form of insurance protection that provides additional liability coverage after the limits of your underlying policy are reached. An umbrella liability policy also protects you (the insured) in many situations not covered by the usual liability policies.";
            case _ACTPROGRAMTYPECODE: return "Definition: A set of codes used to indicate coverage under a program.  A program is an organized structure for administering and funding coverage of a benefit package for covered parties meeting eligibility criteria, typically related to employment, health, financial, and demographic status. Programs are typically established or permitted by legislation with provisions for ongoing government oversight.  Regulations may mandate the structure of the program, the manner in which it is funded and administered, covered benefits, provider types, eligibility criteria and financial participation. A government agency may be charged with implementing the program in accordance to the regulation.  Risk of loss under a program in most cases would not meet what an underwriter would consider an insurable risk, i.e., the risk is not random in nature, not financially measurable, and likely requires subsidization with government funds.\r\n\n                        \n                           Discussion: Programs do not have policy holders or subscribers.  Program eligibles are enrolled based on health status, statutory eligibility, financial status, or age.  Program eligibles who are covered parties under the program may be referred to as members, beneficiaries, eligibles, or recipients.  Programs risk are underwritten by not for profit organizations such as governmental entities, and the beneficiaries typically do not pay for any or some portion of the cost of coverage.  See CoveredPartyRoleType.";
            case CHAR: return "Definition: A program that covers the cost of services provided directly to a beneficiary who typically has no other source of coverage without charge.";
            case CRIME: return "Definition: A program that covers the cost of services provided to crime victims for injuries or losses related to the occurrence of a crime.";
            case EAP: return "Definition: An employee assistance program is run by an employer or employee organization for the purpose of providing benefits and covering all or part of the cost for employees to receive counseling, referrals, and advice in dealing with stressful issues in their lives. These may include substance abuse, bereavement, marital problems, weight issues, or general wellness issues.  The services are usually provided by a third-party, rather than the company itself, and the company receives only summary statistical data from the service provider. Employee's names and services received are kept confidential.";
            case GOVEMP: return "Definition: A set of codes used to indicate a government program that is an organized structure for administering and funding coverage of a benefit package for covered parties meeting eligibility criteria, typically related to employment, health and financial status. Government programs are established or permitted by legislation with provisions for ongoing government oversight.  Regulation mandates the structure of the program, the manner in which it is funded and administered, covered benefits, provider types, eligibility criteria and financial participation. A government agency is charged with implementing the program in accordance to the regulation\r\n\n                        \n                           Example: Federal employee health benefit program in the U.S.";
            case HIRISK: return "Definition: A government program that provides health coverage to individuals who are considered medically uninsurable or high risk, and who have been denied health insurance due to a serious health condition. In certain cases, it also applies to those who have been quoted very high premiums a\" again, due to a serious health condition.  The pool charges premiums for coverage.  Because the pool covers high-risk people, it incurs a higher level of claims than premiums can cover. The insurance industry pays into the pool to make up the difference and help it remain viable.";
            case IND: return "Definition: Services provided directly and through contracted and operated indigenous peoples health programs.\r\n\n                        \n                           Example: Indian Health Service in the U.S.";
            case MILITARY: return "Definition: A government program that provides coverage for health services to military personnel, retirees, and dependents.  A covered party who is a subscriber can choose from among Fee-for-Service (FFS) plans, and their Preferred Provider Organizations (PPO), or Plans offering a Point of Service (POS) Product, or Health Maintenance Organizations.\r\n\n                        \n                           Example: In the U.S., TRICARE, CHAMPUS.";
            case RETIRE: return "Definition: A government mandated program with specific eligibility requirements based on premium contributions made during employment, length of employment, age, and employment status, e.g., being retired, disabled, or a dependent of a covered party under this program.   Benefits typically include ambulatory, inpatient, and long-term care, such as hospice care, home health care and respite care.";
            case SOCIAL: return "Definition: A social service program funded by a public or governmental entity.\r\n\n                        \n                           Example: Programs providing habilitation, food, lodging, medicine, transportation, equipment, devices, products, education, training, counseling, alteration of living or work space, and other resources to persons meeting eligibility criteria.";
            case VET: return "Definition: Services provided directly and through contracted and operated veteran health programs.";
            case _ACTDETECTEDISSUEMANAGEMENTCODE: return "Codes dealing with the management of Detected Issue observations";
            case _ACTADMINISTRATIVEDETECTEDISSUEMANAGEMENTCODE: return "Codes dealing with the management of Detected Issue observations for the administrative and patient administrative acts domains.";
            case _AUTHORIZATIONISSUEMANAGEMENTCODE: return "Authorization Issue Management Code";
            case EMAUTH: return "Used to temporarily override normal authorization rules to gain access to data in a case of emergency. Use of this override code will typically be monitored, and a procedure to verify its proper use may be triggered when used.";
            case _21: return "Description: Indicates that the permissions have been externally verified and the request should be processed.";
            case _1: return "Confirmed drug therapy appropriate";
            case _19: return "Consulted other supplier/pharmacy, therapy confirmed";
            case _2: return "Assessed patient, therapy is appropriate";
            case _22: return "Description: The patient has the appropriate indication or diagnosis for the action to be taken.";
            case _23: return "Description: It has been confirmed that the appropriate pre-requisite therapy has been tried.";
            case _3: return "Patient gave adequate explanation";
            case _4: return "Consulted other supply source, therapy still appropriate";
            case _5: return "Consulted prescriber, therapy confirmed";
            case _6: return "Consulted prescriber and recommended change, prescriber declined";
            case _7: return "Concurrent therapy triggering alert is no longer on-going or planned";
            case _14: return "Confirmed supply action appropriate";
            case _15: return "Patient's existing supply was lost/wasted";
            case _16: return "Supply date is due to patient vacation";
            case _17: return "Supply date is intended to carry patient over weekend";
            case _18: return "Supply is intended for use during a leave of absence from an institution.";
            case _20: return "Description: Supply is different than expected as an additional quantity has been supplied in a separate dispense.";
            case _8: return "Order is performed as issued, but other action taken to mitigate potential adverse effects";
            case _10: return "Provided education or training to the patient on appropriate therapy use";
            case _11: return "Instituted an additional therapy to mitigate potential negative effects";
            case _12: return "Suspended existing therapy that triggered interaction for the duration of this therapy";
            case _13: return "Aborted existing therapy that triggered interaction.";
            case _9: return "Arranged to monitor patient for adverse effects";
            case _ACTEXPOSURECODE: return "Concepts that identify the type or nature of exposure interaction.  Examples include \"household\", \"care giver\", \"intimate partner\", \"common space\", \"common substance\", etc. to further describe the nature of interaction.";
            case CHLDCARE: return "Description: Exposure participants' interaction occurred in a child care setting";
            case CONVEYNC: return "Description: An interaction where the exposure participants traveled in/on the same vehicle (not necessarily concurrently, e.g. both are passengers of the same plane, but on different flights of that plane).";
            case HLTHCARE: return "Description: Exposure participants' interaction occurred during the course of health care delivery or in a health care delivery setting, but did not involve the direct provision of care (e.g. a janitor cleaning a patient's hospital room).";
            case HOMECARE: return "Description: Exposure interaction occurred in context of one providing care for the other, i.e. a babysitter providing care for a child, a home-care aide providing assistance to a paraplegic.";
            case HOSPPTNT: return "Description: Exposure participants' interaction occurred when both were patients being treated in the same (acute) health care delivery facility.";
            case HOSPVSTR: return "Description: Exposure participants' interaction occurred when one visited the other who was a patient being treated in a health care delivery facility.";
            case HOUSEHLD: return "Description: Exposure interaction occurred in context of domestic interaction, i.e. both participants reside in the same household.";
            case INMATE: return "Description: Exposure participants' interaction occurred in the course of one or both participants being incarcerated at a correctional facility";
            case INTIMATE: return "Description: Exposure interaction was intimate, i.e. participants are intimate companions (e.g. spouses, domestic partners).";
            case LTRMCARE: return "Description: Exposure participants' interaction occurred in the course of one or both participants being resident at a long term care facility (second participant may be a visitor, worker, resident or a physical place or object within the facility).";
            case PLACE: return "Description: An interaction where the exposure participants were both present in the same location/place/space.";
            case PTNTCARE: return "Description: Exposure participants' interaction occurred during the course of  health care delivery by a provider (e.g. a physician treating a patient in her office).";
            case SCHOOL2: return "Description: Exposure participants' interaction occurred in an academic setting (e.g., participants are fellow students, or student and teacher).";
            case SOCIAL2: return "Description: An interaction where the exposure participants are social associates or members of the same extended family";
            case SUBSTNCE: return "Description: An interaction where the exposure participants shared or co-used a common substance (e.g. drugs, needles, or common food item).";
            case TRAVINT: return "Description: An interaction where the exposure participants traveled together in/on the same vehicle/trip (e.g. concurrent co-passengers).";
            case WORK2: return "Description: Exposure interaction occurred in a work setting, i.e. participants are co-workers.";
            case _ACTFINANCIALTRANSACTIONCODE: return "ActFinancialTransactionCode";
            case CHRG: return "A type of transaction that represents a charge for a service or product.  Expressed in monetary terms.";
            case REV: return "A type of transaction that represents a reversal of a previous charge for a service or product. Expressed in monetary terms.  It has the opposite effect of a standard charge.";
            case _ACTINCIDENTCODE: return "Set of codes indicating the type of incident or accident.";
            case MVA: return "Incident or accident as the result of a motor vehicle accident";
            case SCHOOL: return "Incident or accident is the result of a school place accident.";
            case SPT: return "Incident or accident is the result of a sporting accident.";
            case WPA: return "Incident or accident is the result of a work place accident";
            case _ACTINFORMATIONACCESSCODE: return "Description: The type of health information to which the subject of the information or the subject's delegate consents or dissents.";
            case ACADR: return "Description: Provide consent to collect, use, disclose, or access adverse drug reaction information for a patient.";
            case ACALL: return "Description: Provide consent to collect, use, disclose, or access all information for a patient.";
            case ACALLG: return "Description: Provide consent to collect, use, disclose, or access allergy information for a patient.";
            case ACCONS: return "Description: Provide consent to collect, use, disclose, or access informational consent information for a patient.";
            case ACDEMO: return "Description: Provide consent to collect, use, disclose, or access demographics information for a patient.";
            case ACDI: return "Description: Provide consent to collect, use, disclose, or access diagnostic imaging information for a patient.";
            case ACIMMUN: return "Description: Provide consent to collect, use, disclose, or access immunization information for a patient.";
            case ACLAB: return "Description: Provide consent to collect, use, disclose, or access lab test result information for a patient.";
            case ACMED: return "Description: Provide consent to collect, use, disclose, or access medical condition information for a patient.";
            case ACMEDC: return "Definition: Provide consent to view or access medical condition information for a patient.";
            case ACMEN: return "Description:Provide consent to collect, use, disclose, or access mental health information for a patient.";
            case ACOBS: return "Description: Provide consent to collect, use, disclose, or access common observation information for a patient.";
            case ACPOLPRG: return "Description: Provide consent to collect, use, disclose, or access coverage policy or program for a patient.";
            case ACPROV: return "Description: Provide consent to collect, use, disclose, or access provider information for a patient.";
            case ACPSERV: return "Description: Provide consent to collect, use, disclose, or access professional service information for a patient.";
            case ACSUBSTAB: return "Description:Provide consent to collect, use, disclose, or access substance abuse information for a patient.";
            case _ACTINFORMATIONACCESSCONTEXTCODE: return "Concepts conveying the context in which consent to transfer specified patient health information for collection, access, use or disclosure applies.";
            case INFAUT: return "Description: Information transfer in accordance with subjectaTMs consent directive.";
            case INFCON: return "Consent to collect, access, use, or disclose specified patient health information only after explicit consent.";
            case INFCRT: return "Description: Information transfer in accordance with judicial system protocol.";
            case INFDNG: return "Consent to collect, access, use, or disclose specified patient health information only if necessary to avert potential danger to other persons.";
            case INFEMER: return "Description: Information transfer in accordance with emergency information transfer protocol.";
            case INFPWR: return "Consent to collect, access, use, or disclose specified patient health information only if necessary to avert potential public welfare risk.";
            case INFREG: return "Description: Information transfer in accordance with regulatory protocol, e.g., for public health, welfare, and safety.";
            case _ACTINFORMATIONCATEGORYCODE: return "Definition:Indicates the set of information types which may be manipulated or referenced, such as for recommending access restrictions.";
            case ALLCAT: return "Description: All patient information.";
            case ALLGCAT: return "Definition:All information pertaining to a patient's allergy and intolerance records.";
            case ARCAT: return "Description: All information pertaining to a patient's adverse drug reactions.";
            case COBSCAT: return "Definition:All information pertaining to a patient's common observation records (height, weight, blood pressure, temperature, etc.).";
            case DEMOCAT: return "Definition:All information pertaining to a patient's demographics (such as name, date of birth, gender, address, etc).";
            case DICAT: return "Definition:All information pertaining to a patient's diagnostic image records (orders & results).";
            case IMMUCAT: return "Definition:All information pertaining to a patient's vaccination records.";
            case LABCAT: return "Description: All information pertaining to a patient's lab test records (orders & results)";
            case MEDCCAT: return "Definition:All information pertaining to a patient's medical condition records.";
            case MENCAT: return "Description: All information pertaining to a patient's mental health records.";
            case PSVCCAT: return "Definition:All information pertaining to a patient's professional service records (such as smoking cessation, counseling, medication review, mental health).";
            case RXCAT: return "Definition:All information pertaining to a patient's medication records (orders, dispenses and other active medications).";
            case _ACTINVOICEELEMENTCODE: return "Type of invoice element that is used to assist in describing an Invoice that is either submitted for adjudication or for which is returned on adjudication results.";
            case _ACTINVOICEADJUDICATIONPAYMENTCODE: return "Codes representing a grouping of invoice elements (totals, sub-totals), reported through a Payment Advice or a Statement of Financial Activity (SOFA).  The code can represent summaries by day, location, payee and other cost elements such as bonus, retroactive adjustment and transaction fees.";
            case _ACTINVOICEADJUDICATIONPAYMENTGROUPCODE: return "Codes representing adjustments to a Payment Advice such as retroactive, clawback, garnishee, etc.";
            case ALEC: return "Payment initiated by the payor as the result of adjudicating a submitted invoice that arrived to the payor from an electronic source that did not provide a conformant set of HL7 messages (e.g. web claim submission).";
            case BONUS: return "Bonus payments based on performance, volume, etc. as agreed to by the payor.";
            case CFWD: return "An amount still owing to the payor but the payment is 0$ and this cannot be settled until a future payment is made.";
            case EDU: return "Fees deducted on behalf of a payee for tuition and continuing education.";
            case EPYMT: return "Fees deducted on behalf of a payee for charges based on a shorter payment frequency (i.e. next day versus biweekly payments.";
            case GARN: return "Fees deducted on behalf of a payee for charges based on a per-transaction or time-period (e.g. monthly) fee.";
            case INVOICE: return "Payment is based on a payment intent for a previously submitted Invoice, based on formal adjudication results..";
            case PINV: return "Payment initiated by the payor as the result of adjudicating a paper (original, may have been faxed) invoice.";
            case PPRD: return "An amount that was owed to the payor as indicated, by a carry forward adjusment, in a previous payment advice";
            case PROA: return "Professional association fee that is collected by the payor from the practitioner/provider on behalf of the association";
            case RECOV: return "Retroactive adjustment such as fee rate adjustment due to contract negotiations.";
            case RETRO: return "Bonus payments based on performance, volume, etc. as agreed to by the payor.";
            case TRAN: return "Fees deducted on behalf of a payee for charges based on a per-transaction or time-period (e.g. monthly) fee.";
            case _ACTINVOICEADJUDICATIONPAYMENTSUMMARYCODE: return "Codes representing a grouping of invoice elements (totals, sub-totals), reported through a Payment Advice or a Statement of Financial Activity (SOFA).  The code can represent summaries by day, location, payee, etc.";
            case INVTYPE: return "Transaction counts and value totals by invoice type (e.g. RXDINV - Pharmacy Dispense)";
            case PAYEE: return "Transaction counts and value totals by each instance of an invoice payee.";
            case PAYOR: return "Transaction counts and value totals by each instance of an invoice payor.";
            case SENDAPP: return "Transaction counts and value totals by each instance of a messaging application on a single processor. It is a registered identifier known to the receivers.";
            case _ACTINVOICEDETAILCODE: return "Codes representing a service or product that is being invoiced (billed).  The code can represent such concepts as \"office visit\", \"drug X\", \"wheelchair\" and other billable items such as taxes, service charges and discounts.";
            case _ACTINVOICEDETAILCLINICALPRODUCTCODE: return "An identifying data string for healthcare products.";
            case UNSPSC: return "Description:United Nations Standard Products and Services Classification, managed by Uniform Code Council (UCC): www.unspsc.org";
            case _ACTINVOICEDETAILDRUGPRODUCTCODE: return "An identifying data string for A substance used as a medication or in the preparation of medication.";
            case GTIN: return "Description:Global Trade Item Number is an identifier for trade items developed by GS1 (comprising the former EAN International and Uniform Code Council).";
            case UPC: return "Description:Universal Product Code is one of a wide variety of bar code languages widely used in the United States and Canada for items in stores.";
            case _ACTINVOICEDETAILGENERICCODE: return "The detail item codes to identify charges or changes to the total billing of a claim due to insurance rules and payments.";
            case _ACTINVOICEDETAILGENERICADJUDICATORCODE: return "The billable item codes to identify adjudicator specified components to the total billing of a claim.";
            case COIN: return "That portion of the eligible charges which a covered party must pay for each service and/or product. It is a percentage of the eligible amount for the service/product that is typically charged after the covered party has met the policy deductible.  This amount represents the covered party's coinsurance that is applied to a particular adjudication result. It is expressed as a negative dollar amount in adjudication results.";
            case COPAYMENT: return "That portion of the eligible charges which a covered party must pay for each service and/or product. It is a defined amount per service/product of the eligible amount for the service/product. This amount represents the covered party's copayment that is applied to a particular adjudication result. It is expressed as a negative dollar amount in adjudication results.";
            case DEDUCTIBLE: return "That portion of the eligible charges which a covered party must pay in a particular period (e.g. annual) before the benefits are payable by the adjudicator. This amount represents the covered party's deductible that is applied to a particular adjudication result. It is expressed as a negative dollar amount in adjudication results.";
            case PAY: return "The guarantor, who may be the patient, pays the entire charge for a service. Reasons for such action may include: there is no insurance coverage for the service (e.g. cosmetic surgery); the patient wishes to self-pay for the service; or the insurer denies payment for the service due to contractual provisions such as the need for prior authorization.";
            case SPEND: return "That total amount of the eligible charges which a covered party must periodically pay for services and/or products prior to the Medicaid program providing any coverage. This amount represents the covered party's spend down that is applied to a particular adjudication result. It is expressed as a negative dollar amount in adjudication results";
            case _ACTINVOICEDETAILGENERICMODIFIERCODE: return "The billable item codes to identify modifications to a billable item charge. As for example after hours increase in the office visit fee.";
            case AFTHRS: return "Premium paid on service fees in compensation for practicing outside of normal working hours.";
            case ISOL: return "Premium paid on service fees in compensation for practicing in a remote location.";
            case OOO: return "Premium paid on service fees in compensation for practicing at a location other than normal working location.";
            case _ACTINVOICEDETAILGENERICPROVIDERCODE: return "The billable item codes to identify provider supplied charges or changes to the total billing of a claim.";
            case CANCAPT: return "A charge to compensate the provider when a patient cancels an appointment with insufficient time for the provider to make another appointment with another patient.";
            case DSC: return "A reduction in the amount charged as a percentage of the amount. For example a 5% discount for volume purchase.";
            case ESA: return "A premium on a service fee is requested because, due to extenuating circumstances, the service took an extraordinary amount of time or supplies.";
            case FFSTOP: return "Under agreement between the parties (payor and provider), a guaranteed level of income is established for the provider over a specific, pre-determined period of time. The normal course of business for the provider is submission of fee-for-service claims. Should the fee-for-service income during the specified period of time be less than the agreed to amount, a top-up amount is paid to the provider equal to the difference between the fee-for-service total and the guaranteed income amount for that period of time. The details of the agreement may specify (or not) a requirement for repayment to the payor in the event that the fee-for-service income exceeds the guaranteed amount.";
            case FNLFEE: return "Anticipated or actual final fee associated with treating a patient.";
            case FRSTFEE: return "Anticipated or actual initial fee associated with treating a patient.";
            case MARKUP: return "An increase in the amount charged as a percentage of the amount. For example, 12% markup on product cost.";
            case MISSAPT: return "A charge to compensate the provider when a patient does not show for an appointment.";
            case PERFEE: return "Anticipated or actual periodic fee associated with treating a patient. For example, expected billing cycle such as monthly, quarterly. The actual period (e.g. monthly, quarterly) is specified in the unit quantity of the Invoice Element.";
            case PERMBNS: return "The amount for a performance bonus that is being requested from a payor for the performance of certain services (childhood immunizations, influenza immunizations, mammograms, pap smears) on a sliding scale. That is, for 90% of childhood immunizations to a maximum of $2200/yr. An invoice is created at the end of the service period (one year) and a code is submitted indicating the percentage achieved and the dollar amount claimed.";
            case RESTOCK: return "A charge is requested because the patient failed to pick up the item and it took an amount of time to return it to stock for future use.";
            case TRAVEL: return "A charge to cover the cost of travel time and/or cost in conjuction with providing a service or product. It may be charged per kilometer or per hour based on the effective agreement.";
            case URGENT: return "Premium paid on service fees in compensation for providing an expedited response to an urgent situation.";
            case _ACTINVOICEDETAILTAXCODE: return "The billable item codes to identify modifications to a billable item charge by a tax factor applied to the amount. As for example 7% provincial sales tax.";
            case FST: return "Federal tax on transactions such as the Goods and Services Tax (GST)";
            case HST: return "Joint Federal/Provincial Sales Tax";
            case PST: return "Tax levied by the provincial or state jurisdiction such as Provincial Sales Tax";
            case _ACTINVOICEDETAILPREFERREDACCOMMODATIONCODE: return "An identifying data string for medical facility accommodations.";
            case _ACTENCOUNTERACCOMMODATIONCODE: return "Accommodation type.  In Intent mood, represents the accommodation type requested.  In Event mood, represents accommodation assigned/used.  In Definition mood, represents the available accommodation type.";
            case _HL7ACCOMMODATIONCODE: return "Description:Accommodation type. In Intent mood, represents the accommodation type requested. In Event mood, represents accommodation assigned/used. In Definition mood, represents the available accommodation type.";
            case I: return "Accommodations used in the care of diseases that are transmitted through casual contact or respiratory transmission.";
            case P: return "Accommodations in which there is only 1 bed.";
            case S: return "Uniquely designed and elegantly decorated accommodations with many amenities available for an additional charge.";
            case SP: return "Accommodations in which there are 2 beds.";
            case W: return "Accommodations in which there are 3 or more beds.";
            case _ACTINVOICEGROUPCODE: return "Type of invoice element that is used to assist in describing an Invoice that is either submitted for adjudication or for which is returned on adjudication results.\r\n\n                        Invoice elements of this type signify a grouping of one or more children (detail) invoice elements.  They do not have intrinsic costing associated with them, but merely reflect the sum of all costing for it's immediate children invoice elements.";
            case _ACTINVOICEINTERGROUPCODE: return "Type of invoice element that is used to assist in describing an Invoice that is either submitted for adjudication or for which is returned on adjudication results.\r\n\n                        Invoice elements of this type signify a grouping of one or more children (detail) invoice elements.  They do not have intrinsic costing associated with them, but merely reflect the sum of all costing for it's immediate children invoice elements.\r\n\n                        The domain is only specified for an intermediate invoice element group (non-root or non-top level) for an Invoice.";
            case CPNDDRGING: return "A grouping of invoice element groups and details including the ones specifying the compound ingredients being invoiced. It may also contain generic detail items such as markup.";
            case CPNDINDING: return "A grouping of invoice element details including the one specifying an ingredient drug being invoiced. It may also contain generic detail items such as tax or markup.";
            case CPNDSUPING: return "A grouping of invoice element groups and details including the ones specifying the compound supplies being invoiced. It may also contain generic detail items such as markup.";
            case DRUGING: return "A grouping of invoice element details including the one specifying the drug being invoiced. It may also contain generic detail items such as markup.";
            case FRAMEING: return "A grouping of invoice element details including the ones specifying the frame fee and the frame dispensing cost that are being invoiced.";
            case LENSING: return "A grouping of invoice element details including the ones specifying the lens fee and the lens dispensing cost that are being invoiced.";
            case PRDING: return "A grouping of invoice element details including the one specifying the product (good or supply) being invoiced. It may also contain generic detail items such as tax or discount.";
            case _ACTINVOICEROOTGROUPCODE: return "Type of invoice element that is used to assist in describing an Invoice that is either submitted for adjudication or for which is returned on adjudication results.\r\n\n                        Invoice elements of this type signify a grouping of one or more children (detail) invoice elements.  They do not have intrinsic costing associated with them, but merely reflect the sum of all costing for it's immediate children invoice elements.\r\n\n                        Codes from this domain reflect the type of Invoice such as Pharmacy Dispense, Clinical Service and Clinical Product.  The domain is only specified for the root (top level) invoice element group for an Invoice.";
            case CPINV: return "Clinical product invoice where the Invoice Grouping contains one or more billable item and is supported by clinical product(s).\r\n\n                        For example, a crutch or a wheelchair.";
            case CSINV: return "Clinical Services Invoice which can be used to describe a single service, multiple services or repeated services.\r\n\n                        [1] Single Clinical services invoice where the Invoice Grouping contains one billable item and is supported by one clinical service.\r\n\n                        For example, a single service for an office visit or simple clinical procedure (e.g. knee mobilization).\r\n\n                        [2] Multiple Clinical services invoice where the Invoice Grouping contains more than one billable item, supported by one or more clinical services.  The services can be distinct and over multiple dates, but for the same patient. This type of invoice includes a series of treatments which must be adjudicated together.\r\n\n                        For example, an adjustment and ultrasound for a chiropractic session where fees are associated for each of the services and adjudicated (invoiced) together.\r\n\n                        [3] Repeated Clinical services invoice where the Invoice Grouping contains one or more billable item, supported by the same clinical service repeated over a period of time.\r\n\n                        For example, the same Chiropractic adjustment (service or treatment) delivered on 3 separate occasions over a period of time at the discretion of the provider (e.g. month).";
            case CSPINV: return "A clinical Invoice Grouping consisting of one or more services and one or more product.  Billing for these service(s) and product(s) are supported by multiple clinical billable events (acts).\r\n\n                        All items in the Invoice Grouping must be adjudicated together to be acceptable to the Adjudicator.\r\n\n                        For example , a brace (product) invoiced together with the fitting (service).";
            case FININV: return "Invoice Grouping without clinical justification.  These will not require identification of participants and associations from a clinical context such as patient and provider.\r\n\n                        Examples are interest charges and mileage.";
            case OHSINV: return "A clinical Invoice Grouping consisting of one or more oral health services. Billing for these service(s) are supported by multiple clinical billable events (acts).\r\n\n                        All items in the Invoice Grouping must be adjudicated together to be acceptable to the Adjudicator.";
            case PAINV: return "HealthCare facility preferred accommodation invoice.";
            case RXCINV: return "Pharmacy dispense invoice for a compound.";
            case RXDINV: return "Pharmacy dispense invoice not involving a compound";
            case SBFINV: return "Clinical services invoice where the Invoice Group contains one billable item for multiple clinical services in one or more sessions.";
            case VRXINV: return "Vision dispense invoice for up to 2 lens (left and right), frame and optional discount.  Eye exams are invoiced as a clinical service invoice.";
            case _ACTINVOICEELEMENTSUMMARYCODE: return "Identifies the different types of summary information that can be reported by queries dealing with Statement of Financial Activity (SOFA).  The summary information is generally used to help resolve balance discrepancies between providers and payors.";
            case _INVOICEELEMENTADJUDICATED: return "Total counts and total net amounts adjudicated for all  Invoice Groupings that were adjudicated within a time period based on the adjudication date of the Invoice Grouping.";
            case ADNFPPELAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted electronically.";
            case ADNFPPELCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted electronically.";
            case ADNFPPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted manually.";
            case ADNFPPMNCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted manually.";
            case ADNFSPELAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date), subsequently nullified in the specified period and submitted electronically.";
            case ADNFSPELCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date), subsequently nullified in the specified period and submitted electronically.";
            case ADNFSPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted manually.";
            case ADNFSPMNCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date), subsequently cancelled in the specified period and submitted manually.";
            case ADNPPPELAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted electronically.";
            case ADNPPPELCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted electronically.";
            case ADNPPPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted manually.";
            case ADNPPPMNCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted manually.";
            case ADNPSPELAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted electronically.";
            case ADNPSPELCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted electronically.";
            case ADNPSPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted manually.";
            case ADNPSPMNCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that do not match a specified payee (e.g. pay patient) and submitted manually.";
            case ADPPPPELAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted electronically.";
            case ADPPPPELCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted electronically.";
            case ADPPPPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted manually.";
            case ADPPPPMNCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable prior to the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted manually.";
            case ADPPSPELAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted electronically.";
            case ADPPSPELCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted electronically.";
            case ADPPSPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted manually.";
            case ADPPSPMNCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as payable during the specified time period (based on adjudication date) that match a specified payee (e.g. pay provider) and submitted manually.";
            case ADRFPPELAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as refused prior to the specified time period (based on adjudication date) and submitted electronically.";
            case ADRFPPELCT: return "Identifies the  total number of all  Invoice Groupings that were adjudicated as refused prior to the specified time period (based on adjudication date) and submitted electronically.";
            case ADRFPPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as refused prior to the specified time period (based on adjudication date) and submitted manually.";
            case ADRFPPMNCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as refused prior to the specified time period (based on adjudication date) and submitted manually.";
            case ADRFSPELAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as refused during the specified time period (based on adjudication date) and submitted electronically.";
            case ADRFSPELCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as refused during the specified time period (based on adjudication date) and submitted electronically.";
            case ADRFSPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were adjudicated as refused during the specified time period (based on adjudication date) and submitted manually.";
            case ADRFSPMNCT: return "Identifies the total number of all  Invoice Groupings that were adjudicated as refused during the specified time period (based on adjudication date) and submitted manually.";
            case _INVOICEELEMENTPAID: return "Total counts and total net amounts paid for all  Invoice Groupings that were paid within a time period based on the payment date.";
            case PDNFPPELAT: return "Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date), subsequently nullified in the specified period and submitted electronically.";
            case PDNFPPELCT: return "Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date), subsequently nullified in the specified period and submitted electronically.";
            case PDNFPPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date), subsequently nullified in the specified period and submitted manually.";
            case PDNFPPMNCT: return "Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date), subsequently nullified in the specified period and submitted manually.";
            case PDNFSPELAT: return "Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date), subsequently nullified in the specified period and submitted electronically.";
            case PDNFSPELCT: return "Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date), subsequently cancelled in the specified period and submitted electronically.";
            case PDNFSPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date), subsequently nullified in the specified period and submitted manually.";
            case PDNFSPMNCT: return "Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date), subsequently nullified in the specified period and submitted manually.";
            case PDNPPPELAT: return "Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted electronically.";
            case PDNPPPELCT: return "Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted electronically.";
            case PDNPPPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted manually.";
            case PDNPPPMNCT: return "Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted manually.";
            case PDNPSPELAT: return "Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted electronically.";
            case PDNPSPELCT: return "Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted electronically.";
            case PDNPSPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted manually.";
            case PDNPSPMNCT: return "Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date) that do not match a specified payee (e.g. pay patient) and submitted manually.";
            case PDPPPPELAT: return "Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted electronically.";
            case PDPPPPELCT: return "Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted electronically.";
            case PDPPPPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted manually.";
            case PDPPPPMNCT: return "Identifies the total number of all  Invoice Groupings that were paid prior to the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted manually.";
            case PDPPSPELAT: return "Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted electronically.";
            case PDPPSPELCT: return "Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted electronically.";
            case PDPPSPMNAT: return "Identifies the total net amount of all  Invoice Groupings that were paid during the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted manually.";
            case PDPPSPMNCT: return "Identifies the total number of all  Invoice Groupings that were paid during the specified time period (based on payment date) that match a specified payee (e.g. pay provider) and submitted manually.";
            case _INVOICEELEMENTSUBMITTED: return "Total counts and total net amounts billed for all Invoice Groupings that were submitted within a time period.  Adjudicated invoice elements are included.";
            case SBBLELAT: return "Identifies the total net amount billed for all submitted Invoice Groupings within a time period and submitted electronically.  Adjudicated invoice elements are included.";
            case SBBLELCT: return "Identifies the total number of submitted Invoice Groupings within a time period and submitted electronically.  Adjudicated invoice elements are included.";
            case SBNFELAT: return "Identifies the total net amount billed for all submitted  Invoice Groupings that were nullified within a time period and submitted electronically.  Adjudicated invoice elements are included.";
            case SBNFELCT: return "Identifies the total number of submitted  Invoice Groupings that were nullified within a time period and submitted electronically.  Adjudicated invoice elements are included.";
            case SBPDELAT: return "Identifies the total net amount billed for all submitted  Invoice Groupings that are pended or held by the payor, within a time period and submitted electronically.  Adjudicated invoice elements are not included.";
            case SBPDELCT: return "Identifies the total number of submitted  Invoice Groupings that are pended or held by the payor, within a time period and submitted electronically.  Adjudicated invoice elements are not included.";
            case _ACTINVOICEOVERRIDECODE: return "Includes coded responses that will occur as a result of the adjudication of an electronic invoice at a summary level and provides guidance on interpretation of the referenced adjudication results.";
            case COVGE: return "Insurance coverage problems have been encountered. Additional explanation information to be supplied.";
            case EFORM: return "Electronic form with supporting or additional information to follow.";
            case FAX: return "Fax with supporting or additional information to follow.";
            case GFTH: return "The medical service was provided to a patient in good faith that they had medical coverage, although no evidence of coverage was available before service was rendered.";
            case LATE: return "Knowingly over the payor's published time limit for this invoice possibly due to a previous payor's delays in processing. Additional reason information will be supplied.";
            case MANUAL: return "Manual review of the invoice is requested.  Additional information to be supplied.  This may be used in the case of an appeal.";
            case OOJ: return "The medical service and/or product was provided to a patient that has coverage in another jurisdiction.";
            case ORTHO: return "The service provided is required for orthodontic purposes. If the covered party has orthodontic coverage, then the service may be paid.";
            case PAPER: return "Paper documentation (or other physical format) with supporting or additional information to follow.";
            case PIE: return "Public Insurance has been exhausted.  Invoice has not been sent to Public Insuror and therefore no Explanation Of Benefits (EOB) is provided with this Invoice submission.";
            case PYRDELAY: return "Allows provider to explain lateness of invoice to a subsequent payor.";
            case REFNR: return "Rules of practice do not require a physician's referral for the provider to perform a billable service.";
            case REPSERV: return "The same service was delivered within a time period that would usually indicate a duplicate billing.  However, the repeated service is a medical 	necessity and therefore not a duplicate.";
            case UNRELAT: return "The service provided is not related to another billed service. For example, 2 unrelated services provided on the same day to the same patient which may normally result in a refused payment for one of the items.";
            case VERBAUTH: return "The provider has received a verbal permission from an authoritative source to perform the service or supply the item being invoiced.";
            case _ACTLISTCODE: return "Provides codes associated with ActClass value of LIST (working list)";
            case _ACTOBSERVATIONLIST: return "ActObservationList";
            case CARELIST: return "List of acts representing a care plan.  The acts can be in a varierty of moods including event (EVN) to record acts that have been carried out as part of the care plan.";
            case CONDLIST: return "List of condition observations.";
            case INTOLIST: return "List of intolerance observations.";
            case PROBLIST: return "List of problem observations.";
            case RISKLIST: return "List of risk factor observations.";
            case GOALLIST: return "List of observations in goal mood.";
            case _ACTTHERAPYDURATIONWORKINGLISTCODE: return "Codes used to identify different types of 'duration-based' working lists.  Examples include \"Continuous/Chronic\", \"Short-Term\" and \"As-Needed\".";
            case _ACTMEDICATIONTHERAPYDURATIONWORKINGLISTCODE: return "Definition:A collection of concepts that identifies different types of 'duration-based' mediation working lists.\r\n\n                        \n                           Examples:\"Continuous/Chronic\" \"Short-Term\" and \"As Needed\"";
            case ACU: return "Definition:A list of medications which the patient is only expected to consume for the duration of the current order or limited set of orders and which is not expected to be renewed.";
            case CHRON: return "Definition:A list of medications which are expected to be continued beyond the present order and which the patient should be assumed to be taking unless explicitly stopped.";
            case ONET: return "Definition:A list of medications which the patient is intended to be administered only once.";
            case PRN: return "Definition:A list of medications which the patient will consume intermittently based on the behavior of the condition for which the medication is indicated.";
            case MEDLIST: return "List of medications.";
            case CURMEDLIST: return "List of current medications.";
            case DISCMEDLIST: return "List of discharge medications.";
            case HISTMEDLIST: return "Historical list of medications.";
            case _ACTMONITORINGPROTOCOLCODE: return "Identifies types of monitoring programs";
            case CTLSUB: return "A monitoring program that focuses on narcotics and/or commonly abused substances that are subject to legal restriction.";
            case INV: return "Definition:A monitoring program that focuses on a drug which is under investigation and has not received regulatory approval for the condition being investigated";
            case LU: return "Description:A drug that can be prescribed (and reimbursed) only if it meets certain criteria.";
            case OTC: return "Medicines designated in this way may be supplied for patient use without a prescription.  The exact form of categorisation will vary in different realms.";
            case RX: return "Some form of prescription is required before the related medicine can be supplied for a patient.  The exact form of regulation will vary in different realms.";
            case SA: return "Definition:A drug that requires prior approval (to be reimbursed) before being dispensed";
            case SAC: return "Description:A drug that requires special access permission to be prescribed and dispensed.";
            case _ACTNONOBSERVATIONINDICATIONCODE: return "Description:Concepts representing indications (reasons for clinical action) other than diagnosis and symptoms.";
            case IND01: return "Description:Contrast agent required for imaging study.";
            case IND02: return "Description:Provision of prescription or direction to consume a product for purposes of bowel clearance in preparation for a colonoscopy.";
            case IND03: return "Description:Provision of medication as a preventative measure during a treatment or other period of increased risk.";
            case IND04: return "Description:Provision of medication during pre-operative phase; e.g., antibiotics before dental surgery or bowel prep before colon surgery.";
            case IND05: return "Description:Provision of medication for pregnancy --e.g., vitamins, antibiotic treatments for vaginal tract colonization, etc.";
            case _ACTOBSERVATIONVERIFICATIONTYPE: return "Identifies the type of verification investigation being undertaken with respect to the subject of the verification activity.\r\n\n                        \n                           Examples:\n                        \r\n\n                        \n                           \n                              Verification of eligibility for coverage under a policy or program - aka enrolled/covered by a policy or program\r\n\n                           \n                           \n                              Verification of record - e.g., person has record in an immunization registry\r\n\n                           \n                           \n                              Verification of enumeration - e.g. NPI\r\n\n                           \n                           \n                              Verification of Board Certification - provider specific\r\n\n                           \n                           \n                              Verification of Certification - e.g. JAHCO, NCQA, URAC\r\n\n                           \n                           \n                              Verification of Conformance - e.g. entity use with HIPAA, conformant to the CCHIT EHR system criteria\r\n\n                           \n                           \n                              Verification of Provider Credentials\r\n\n                           \n                           \n                              Verification of no adverse findings - e.g. on National Provider Data Bank, Health Integrity Protection Data Base (HIPDB)";
            case VFPAPER: return "Definition:Indicates that the paper version of the record has, should be or is being verified against the electronic version.";
            case _ACTPAYMENTCODE: return "Code identifying the method or the movement of payment instructions.\r\n\n                        Codes are drawn from X12 data element 591 (PaymentMethodCode)";
            case ACH: return "Automated Clearing House (ACH).";
            case CHK: return "A written order to a bank to pay the amount specified from funds on deposit.";
            case DDP: return "Electronic Funds Transfer (EFT) deposit into the payee's bank account";
            case NON: return "Non-Payment Data.";
            case _ACTPHARMACYSUPPLYTYPE: return "Identifies types of dispensing events";
            case DF: return "A fill providing sufficient supply for one day";
            case EM: return "A supply action where there is no 'valid' order for the supplied medication.  E.g. Emergency vacation supply, weekend supply (when prescriber is unavailable to provide a renewal prescription)";
            case SO: return "An emergency supply where the expectation is that a formal order authorizing the supply will be provided at a later date.";
            case FF: return "The initial fill against an order.  (This includes initial fills against refill orders.)";
            case FFC: return "A first fill where the quantity supplied is equal to one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a complete fill would be for the full 90 tablets).";
            case FFCS: return "A first fill where the quantity supplied is equal to one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a complete fill would be for the full 90 tablets) and also where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).";
            case FFP: return "A first fill where the quantity supplied is less than one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a partial fill might be for only 30 tablets.)";
            case FFPS: return "A first fill where the quantity supplied is less than one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a partial fill might be for only 30 tablets.) and also where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets)";
            case FFSS: return "A first fill where the strength supplied is less than the ordered strength. (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).";
            case TFS: return "A fill where a small portion is provided to allow for determination of the therapy effectiveness and patient tolerance and also where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).";
            case TF: return "A fill where a small portion is provided to allow for determination of the therapy effectiveness and patient tolerance.";
            case FS: return "A supply action to restock a smaller more local dispensary.";
            case MS: return "A supply of a manufacturer sample";
            case RF: return "A fill against an order that has already been filled (or partially filled) at least once.";
            case UD: return "A supply action that provides sufficient material for a single dose.";
            case RFC: return "A refill where the quantity supplied is equal to one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a complete fill would be for the full 90 tablets.)";
            case RFCS: return "A refill where the quantity supplied is equal to one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a complete fill would be for the full 90 tablets.) and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).";
            case RFF: return "The first fill against an order that has already been filled at least once at another facility.";
            case RFFS: return "The first fill against an order that has already been filled at least once at another facility and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).";
            case RFP: return "A refill where the quantity supplied is less than one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a partial fill might be for only 30 tablets.)";
            case RFPS: return "A refill where the quantity supplied is less than one full repetition of the ordered amount. (e.g. If the order was 90 tablets, 3 refills, a partial fill might be for only 30 tablets.) and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).";
            case RFS: return "A fill against an order that has already been filled (or partially filled) at least once and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).";
            case TB: return "A fill where the remainder of a 'complete' fill is provided after a trial fill has been provided.";
            case TBS: return "A fill where the remainder of a 'complete' fill is provided after a trial fill has been provided and where the strength supplied is less than the ordered strength (e.g. 10mg for an order of 50mg where a subsequent fill will dispense 40mg tablets).";
            case UDE: return "A supply action that provides sufficient material for a single dose via multiple products.  E.g. 2 50mg tablets for a 100mg unit dose.";
            case _ACTPOLICYTYPE: return "Description:Types of policies that further specify the ActClassPolicy value set.";
            case _ACTPRIVACYPOLICY: return "A policy deeming certain information to be private to an individual or organization.\r\n\n                        \n                           Definition: A mandate, obligation, requirement, rule, or expectation relating to privacy.\r\n\n                        \n                           Discussion: ActPrivacyPolicyType codes support the designation of the 1..* policies that are applicable to an Act such as a Consent Directive, a Role such as a VIP Patient, or an Entity such as a patient who is a minor.  1..* ActPrivacyPolicyType values may be associated with an Act or Role to indicate the policies that govern the assignment of an Act or Role confidentialityCode.  Use of multiple ActPrivacyPolicyType values enables fine grain specification of applicable policies, but must be carefully assigned to ensure cogency and avoid creation of conflicting policy mandates.\r\n\n                        \n                           Usage Note: Statutory title may be named in the ActClassPolicy Act Act.title to specify which privacy policy is being referenced.";
            case _ACTCONSENTDIRECTIVE: return "Definition: Specifies the type of consent directive indicated by an ActClassPolicy e.g., a 3rd party authorization to disclose or consent for a substitute decision maker (SDM) or a notice of privacy policy.\r\n\n                        \n                           Usage Note: ActConsentDirective codes are used to specify the type of Consent Directive to which a Consent Directive Act conforms.";
            case EMRGONLY: return "This general consent directive specifically limits disclosure of health information for purpose of emergency treatment. Additional parameters may further limit the disclosure to specific users, roles, duration, types of information, and impose uses obligations.\r\n\n                        \n                           Definition: Opt-in to disclosure of health information for emergency only consent directive.";
            case NOPP: return "Acknowledgement of custodian notice of privacy practices.\r\n\n                        \n                           Usage Notes: This type of consent directive acknowledges a custodian's notice of privacy practices including its permitted collection, access, use and disclosure of health information to users and for purposes of use specified.";
            case OPTIN: return "This general consent directive permits disclosure of health information.  Additional parameter may limit authorized users, purpose of use, user obligations, duration, or information types permitted to be disclosed, and impose uses obligations.\r\n\n                        \n                           Definition: Opt-in to disclosure of health information consent directive.";
            case OPTOUT: return "This general consent directive prohibits disclosure of health information.  Additional parameters may permit access to some information types by certain users, roles, purposes of use, durations and impose user obligations.\r\n\n                        \n                           Definition: Opt-out of disclosure of health information consent directive.";
            case _INFORMATIONSENSITIVITYPOLICY: return "A mandate, obligation, requirement, rule, or expectation characterizing the value or importance of a resource and may include its vulnerability. (Based on ISO7498-2:1989. Note: The vulnerability of personally identifiable sensitive information may be based on concerns that the unauthorized disclosure may result in social stigmatization or discrimination.) Description:  Types of Sensitivity policy that apply to Acts or Roles.  A sensitivity policy is adopted by an enterprise or group of enterprises (a 'policy domain') through a formal data use agreement that stipulates the value, importance, and vulnerability of information. A sensitivity code representing a sensitivity policy may be associated with criteria such as categories of information or sets of information identifiers (e.g., a value set of clinical codes or branch in a code system hierarchy).   These criteria may in turn be used for the Policy Decision Point in a Security Engine.  A sensitivity code may be used to set the confidentiality code used on information about Acts and Roles to trigger the security mechanisms required to control how security principals (i.e., a person, a machine, a software application) may act on the information (e.g., collection, access, use, or disclosure). Sensitivity codes are never assigned to the transport or business envelope containing patient specific information being exchanged outside of a policy domain as this would disclose the information intended to be protected by the policy.  When sensitive information is exchanged with others outside of a policy domain, the confidentiality code on the transport or business envelope conveys the receiver's responsibilities and indicates the how the information is to be safeguarded without unauthorized disclosure of the sensitive information.  This ensures that sensitive information is treated by receivers as the sender intends, accomplishing interoperability without point to point negotiations.\r\n\n                        \n                           Usage Note: Sensitivity codes are not useful for interoperability outside of a policy domain because sensitivity policies are typically localized and vary drastically across policy domains even for the same information category because of differing organizational business rules, security policies, and jurisdictional requirements.  For example, an employee's sensitivity code would make little sense for use outside of a policy domain.   'Taboo' would rarely be useful outside of a policy domain unless there are jurisdictional requirements requiring that a provider disclose sensitive information to a patient directly.  Sensitivity codes may be more appropriate in a legacy system's Master Files in order to notify those who access a patient's orders and observations about the sensitivity policies that apply.  Newer systems may have a security engine that uses a sensitivity policy's criteria directly.  The specializable InformationSensitivityPolicy Act.code may be useful in some scenarios if used in combination with a sensitivity identifier and/or Act.title.";
            case _ACTINFORMATIONSENSITIVITYPOLICY: return "Types of sensitivity policies that apply to Acts.  Act.confidentialityCode is defined in the RIM as \"constraints around appropriate disclosure of information about this Act, regardless of mood.\"\r\n\n                        \n                           Usage Note: ActSensitivity codes are used to bind information to an Act.confidentialityCode according to local sensitivity policy so that those confidentiality codes can then govern its handling across enterprises.  Internally to a policy domain, however, local policies guide the access control system on how end users in that policy domain are  able to use information tagged with these sensitivity values.";
            case ETH: return "Policy for handling alcohol or drug-abuse information, which will be afforded heightened confidentiality.  Information handling protocols based on organizational policies related to alcohol or drug-abuse information that is deemed sensitive.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case GDIS: return "Policy for handling genetic disease information, which will be afforded heightened confidentiality. Information handling protocols based on organizational policies related to genetic disease information that is deemed sensitive.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case HIV: return "Policy for handling HIV or AIDS information, which will be afforded heightened confidentiality. Information handling protocols based on organizational policies related to HIV or AIDS information that is deemed sensitive.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case PSY: return "Policy for handling psychiatry information, which will be afforded heightened confidentiality. Information handling protocols based on organizational policies related to psychiatry information that is deemed sensitive.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case SCA: return "Policy for handling sickle cell disease information, which is afforded heightened confidentiality.  Information handling protocols are based on organizational policies related to sickle cell disease information, which is deemed sensitive.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then the Act valued with this ActCode should be associated with an Act valued with any applicable laws from the ActPrivacyLaw code system.";
            case SDV: return "Policy for handling sexual assault, abuse, or domestic violence information, which will be afforded heightened confidentiality. Information handling protocols based on organizational policies related to sexual assault, abuse, or domestic violence information that is deemed sensitive.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case SEX: return "Policy for handling sexuality and reproductive health information, which will be afforded heightened confidentiality.  Information handling protocols based on organizational policies related to sexuality and reproductive health information that is deemed sensitive.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case STD: return "Policy for handling sexually transmitted disease information, which will be afforded heightened confidentiality.\n Information handling protocols based on organizational policies related to sexually transmitted disease information that is deemed sensitive.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case TBOO: return "Policy for handling information not to be initially disclosed or discussed with patient except by a physician assigned to patient in this case. Information handling protocols based on organizational policies related to sensitive patient information that must be initially discussed with the patient by an attending physician before being disclosed to the patient.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.\r\n\n                        \n                           Open Issue: This definition conflates a rule and a characteristic, and there may be a similar issue with ts sibling codes.";
            case _ENTITYSENSITIVITYPOLICYTYPE: return "Types of sensitivity policies that may apply to a sensitive attribute on an Entity.\r\n\n                        \n                           Usage Note: EntitySensitivity codes are used to convey a policy that is applicable to sensitive information conveyed by an entity attribute.  May be used to bind a Role.confidentialityCode associated with an Entity per organizational policy.  Role.confidentialityCode is defined in the RIM as \"an indication of the appropriate disclosure of information about this Role with respect to the playing Entity.\"";
            case DEMO: return "Policy for handling all demographic information about an information subject, which will be afforded heightened confidentiality. Policies may govern sensitivity of information related to all demographic about an information subject, the disclosure of which could impact the privacy, well-being, or safety of that subject.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case DOB: return "Policy for handling information related to an information subject's date of birth, which will be afforded heightened confidentiality.Policies may govern sensitivity of information related to an information subject's date of birth, the disclosure of which could impact the privacy, well-being, or safety of that subject.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case GENDER: return "Policy for handling information related to an information subject's gender and sexual orientation, which will be afforded heightened confidentiality.  Policies may govern sensitivity of information related to an information subject's gender and sexual orientation, the disclosure of which could impact the privacy, well-being, or safety of that subject.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case LIVARG: return "Policy for handling information related to an information subject's living arrangement, which will be afforded heightened confidentiality.  Policies may govern sensitivity of information related to an information subject's living arrangement, the disclosure of which could impact the privacy, well-being, or safety of that subject.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case MARST: return "Policy for handling information related to an information subject's marital status, which will be afforded heightened confidentiality. Policies may govern sensitivity of information related to an information subject's marital status, the disclosure of which could impact the privacy, well-being, or safety of that subject.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case RACE: return "Policy for handling information related to an information subject's race, which will be afforded heightened confidentiality.  Policies may govern sensitivity of information related to an information subject's race, the disclosure of which could impact the privacy, well-being, or safety of that subject.\r\n\n                        \n                           Usage Note: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case REL: return "Policy for handling information related to an information subject's religious affiliation, which will be afforded heightened confidentiality.  Policies may govern sensitivity of information related to an information subject's religion, the disclosure of which could impact the privacy, well-being, or safety of that subject.\r\n\n                        \n                           Usage Notes: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case _ROLEINFORMATIONSENSITIVITYPOLICY: return "Types of sensitivity policies that apply to Roles.\r\n\n                        \n                           Usage Notes: RoleSensitivity codes are used to bind information to a Role.confidentialityCode per organizational policy.  Role.confidentialityCode is defined in the RIM as \"an indication of the appropriate disclosure of information about this Role with respect to the playing Entity.\"";
            case B: return "Policy for handling trade secrets such as financial information or intellectual property, which will be afforded heightened confidentiality.  Description:  Since the service class can represent knowledge structures that may be considered a trade or business secret, there is sometimes (though rarely) the need to flag those items as of business level confidentiality.\r\n\n                        \n                           Usage Notes: No patient related information may ever be of this confidentiality level.   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case EMPL: return "Policy for handling information related to an employer which is deemed classified to protect an employee who is the information subject, and which will be afforded heightened confidentiality.  Description:  Policies may govern sensitivity of information related to an employer, such as law enforcement or national security, the identity of which could impact the privacy, well-being, or safety of an information subject who is an employee.\r\n\n                        \n                           Usage Notes: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case LOCIS: return "Policy for handling information related to the location of the information subject, which will be afforded heightened confidentiality.  Description:  Policies may govern sensitivity of information related to the location of the information subject, the disclosure of which could impact the privacy, well-being, or safety of that subject.\r\n\n                        \n                           Usage Notes: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case SSP: return "Policy for handling information related to a provider of sensitive services, which will be afforded heightened confidentiality.  Description:  Policies may govern sensitivity of information related to providers who deliver sensitive healthcare services in order to protect the privacy, well-being, and safety of the provider and of patients receiving sensitive services.\r\n\n                        \n                           Usage Notes: If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case ADOL: return "Policy for handling information related to an adolescent, which will be afforded heightened confidentiality per applicable organizational or jurisdictional policy.  An enterprise may have a policy that requires that adolescent patient information be provided heightened confidentiality.  Information deemed sensitive typically includes health information and patient role information including patient status, demographics, next of kin, and location.\r\n\n                        \n                           Usage Note: For use within an enterprise in which an adolescent is the information subject.  If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case CEL: return "Policy for handling information related to a celebrity (people of public interest (VIP), which will be afforded heightened confidentiality.  Celebrities are people of public interest (VIP) about whose information an enterprise may have a policy that requires heightened confidentiality.  Information deemed sensitive may include health information and patient role information including patient status, demographics, next of kin, and location.\r\n\n                        \n                           Usage Note:  For use within an enterprise in which the information subject is deemed a celebrity or very important person.  If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case DIA: return "Policy for handling information related to a diagnosis, health condition or health problem, which will be afforded heightened confidentiality.  Diagnostic, health condition or health problem related information may be deemed sensitive by organizational policy, and require heightened confidentiality.\r\n\n                        \n                           Usage Note: For use within an enterprise that provides heightened confidentiality to  diagnostic, health condition or health problem related information deemed sensitive.   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case DRGIS: return "Policy for handling information related to a drug, which will be afforded heightened confidentiality. Drug information may be deemed sensitive by organizational policy, and require heightened confidentiality.\r\n\n                        \n                           Usage Note: For use within an enterprise that provides heightened confidentiality to drug information deemed sensitive.   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case EMP: return "Policy for handling information related to an employee, which will be afforded heightened confidentiality. When a patient is an employee, an enterprise may have a policy that requires heightened confidentiality.  Information deemed sensitive typically includes health information and patient role information including patient status, demographics, next of kin, and location.\r\n\n                        \n                           Usage Note: Policy for handling information related to an employee, which will be afforded heightened confidentiality.  Description:  When a patient is an employee, an enterprise may have a policy that requires heightened confidentiality.  Information deemed sensitive typically includes health information and patient role information including patient status, demographics, next of kin, and location.";
            case PDS: return "Policy for handling information reported by the patient about another person, e.g., a family member, which will be afforded heightened confidentiality. Sensitive information reported by the patient about another person, e.g., family members may be deemed sensitive by default.  The flag may be set or cleared on patient's request.  \r\n\n                        \n                           Usage Note: For sensitive information relayed by or about a patient, which is deemed sensitive within the enterprise (i.e., by default regardless of whether the patient requested that the information be deemed sensitive.)   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case PRS: return "For sensitive information relayed by or about a patient, which is deemed sensitive within the enterprise (i.e., by default regardless of whether the patient requested that the information be deemed sensitive.)   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.\r\n\n                        \n                           Usage Note: For use within an enterprise that provides heightened confidentiality to certain types of information designated by a patient as sensitive.   If there is a jurisdictional mandate, then use the applicable ActPrivacyLaw code system, and specify the law rather than or in addition to this more generic code.";
            case COMPT: return "This is the healthcare analog to the US Intelligence Community's concept of a Special Access Program.  Compartment codes may be used in as a field value in an initiator's clearance to indicate permission to access and use an IT Resource with a security label having the same compartment value in security category label field.\r\n\n                        Map: Aligns with ISO 2382-8 definition of Compartment - \"A division of data into isolated blocks with separate security controls for the purpose of reducing risk.\"";
            case HRCOMPT: return "A security category label field value, which indicates that access and use of an IT resource is restricted to members of human resources department or workflow.";
            case RESCOMPT: return "A security category label field value, which indicates that access and use of an IT resource is restricted to members of a research project.";
            case RMGTCOMPT: return "A security category label field value, which indicates that access and use of an IT resource is restricted to members of records management department or workflow.";
            case ACTTRUSTPOLICYTYPE: return "A mandate, obligation, requirement, rule, or expectation conveyed as security metadata between senders and receivers required to establish the reliability, authenticity, and trustworthiness of their transactions.\r\n\n                        Trust security metadata are observation made about aspects of trust applicable to an IT resource (data, information object, service, or system capability).\r\n\n                        Trust applicable to IT resources is established and maintained in and among security domains, and may be comprised of observations about the domain's trust authority, trust framework, trust policy, trust interaction rules, means for assessing and monitoring adherence to trust policies, mechanisms that enforce trust, and quality and reliability measures of assurance in those mechanisms. [Based on ISO IEC 10181-1 and NIST SP 800-63-2]\r\n\n                        For example, identity proofing , level of assurance, and Trust Framework.";
            case TRSTACCRD: return "Type of security metadata about the formal declaration by an authority or neutral third party that validates the technical, security, trust, and business practice conformance of Trust Agents to facilitate security, interoperability, and trust among participants within a security domain or trust framework.";
            case TRSTAGRE: return "Type of security metadata about privacy and security requirements with which a security domain must comply. [ISO IEC 10181-1]";
            case TRSTASSUR: return "Type of security metadata about the digital quality or reliability of a trust assertion, activity, capability, information exchange, mechanism, process, or protocol.";
            case TRSTCERT: return "Type of security metadata about a set of security-relevant data issued by a security authority or trusted third party, together with security information which is used to provide the integrity and data origin authentication services for an IT resource (data, information object, service, or system capability). [Based on ISO IEC 10181-1]";
            case TRSTFWK: return "Type of security metadata about a complete set of contracts, regulations, or commitments that enable participating actors to rely on certain assertions by other actors to fulfill their information security requirements. [Kantara Initiative]";
            case TRSTMEC: return "Type of security metadata about a security architecture system component that supports enforcement of security policies.";
            case COVPOL: return "Description:A mandate, obligation, requirement, rule, or expectation unilaterally imposed on benefit coverage under a policy or program by a sponsor, underwriter or payor on:\r\n\n                        \n                           \n                              The activity of another party\r\n\n                           \n                           \n                              The behavior of another party\r\n\n                           \n                           \n                              The manner in which an act is executed\r\n\n                           \n                        \n                        \n                           Examples:A clinical protocol imposed by a payer to which a provider must adhere in order to be paid for providing the service.  A formulary from which a provider must select prescribed drugs in order for the patient to incur a lower copay.";
            case SECURITYPOLICY: return "Types of security policies that further specify the ActClassPolicy value set.\r\n\n                        \n                           Examples:\n                        \r\n\n                        \n                           obligation to encrypt\n                           refrain from redisclosure without consent";
            case OBLIGATIONPOLICY: return "Conveys the mandated workflow action that an information custodian, receiver, or user must perform.  \r\n\n                        \n                           Usage Notes: Per ISO 22600-2, ObligationPolicy instances 'are event-triggered and define actions to be performed by manager agent'. Per HL7 Composite Security and Privacy Domain Analysis Model:  This value set refers to the action required to receive the permission specified in the privacy rule. Per OASIS XACML, an obligation is an operation specified in a policy or policy that is performed in conjunction with the enforcement of an access control decision.";
            case ANONY: return "Custodian system must remove any information that could result in identifying the information subject.";
            case AOD: return "Custodian system must make available to an information subject upon request an accounting of certain disclosures of the individualâ€™s protected health information over a period of time.  Policy may dictate that the accounting include information about the information disclosed,  the date of disclosure, the identification of the receiver, the purpose of the disclosure, the time in which the disclosing entity must provide a response and the time period for which accountings of disclosure can be requested.";
            case AUDIT: return "Custodian system must monitor systems to ensure that all users are authorized to operate on information objects.";
            case AUDTR: return "Custodian system must monitor and maintain retrievable log for each user and operation on information.";
            case CPLYCC: return "Custodian security system must retrieve, evaluate, and comply with the information handling directions of the Confidentiality Code associated with an information target.";
            case CPLYCD: return "Custodian security system must retrieve, evaluate, and comply with applicable information subject consent directives.";
            case CPLYJPP: return "Custodian security system must retrieve, evaluate, and comply with applicable jurisdictional privacy policies associated with the target information.";
            case CPLYOPP: return "Custodian security system must retrieve, evaluate, and comply with applicable organizational privacy policies associated with the target information.";
            case CPLYOSP: return "Custodian security system must retrieve, evaluate, and comply with the organizational security policies associated with the target information.";
            case CPLYPOL: return "Custodian security system must retrieve, evaluate, and comply with applicable policies associated with the target information.";
            case DEID: return "Custodian system must strip information of data that would allow the identification of the source of the information or the information subject.";
            case DELAU: return "Custodian system must remove target information from access after use.";
            case ENCRYPT: return "Custodian system must render information unreadable by algorithmically transforming plaintext into ciphertext.  \r\n\n                        \r\n\n                        \n                           Usage Notes: A mathematical transposition of a file or data stream so that it cannot be deciphered at the receiving end without the proper key. Encryption is a security feature that assures that only the parties who are supposed to be participating in a videoconference or data transfer are able to do so. It can include a password, public and private keys, or a complex combination of all.  (Per Infoway.)";
            case ENCRYPTR: return "Custodian system must render information unreadable and unusable by algorithmically transforming plaintext into ciphertext when \"at rest\" or in storage.";
            case ENCRYPTT: return "Custodian system must render information unreadable and unusable by algorithmically transforming plaintext into ciphertext while \"in transit\" or being transported by any means.";
            case ENCRYPTU: return "Custodian system must render information unreadable and unusable by algorithmically transforming plaintext into ciphertext while in use such that operations permitted on the target information are limited by the license granted to the end user.";
            case HUAPRV: return "Custodian system must require human review and approval for permission requested.";
            case MASK: return "Custodian system must render information unreadable and unusable by algorithmically transforming plaintext into ciphertext.  User may be provided a key to decrypt per license or \"shared secret\".";
            case MINEC: return "Custodian must limit access and disclosure to the minimum information required to support an authorized user's purpose of use.  \r\n\n                        \n                           Usage Note: Limiting the information available for access and disclosure to that an authorized user or receiver \"needs to know\" in order to perform permitted workflow or purpose of use.";
            case PRIVMARK: return "Custodian must create and/or maintain human readable security label tags as required by policy.\r\n\n                        Map:  Aligns with ISO 22600-3 Section A.3.4.3 description of privacy mark:  \"If present, the privacy-mark is not used for access control. The content of the privacy-mark may be defined by the security policy in force (identified by the security-policy-identifier) which may define a list of values to be used. Alternately, the value may be determined by the originator of the security-label.\"";
            case PSEUD: return "Custodian system must strip information of data that would allow the identification of the source of the information or the information subject.  Custodian may retain a key to relink data necessary to reidentify the information subject.";
            case REDACT: return "Custodian system must remove information, which is not authorized to be access, used, or disclosed from records made available to otherwise authorized users.";
            case REFRAINPOLICY: return "Conveys prohibited actions which an information custodian, receiver, or user is not permitted to perform unless otherwise authorized or permitted under specified circumstances.\r\n\n                        \r\n\n                        \n                           Usage Notes: ISO 22600-2 species that a Refrain Policy \"defines actions the subjects must refrain from performing\".  Per HL7 Composite Security and Privacy Domain Analysis Model:  May be used to indicate that a specific action is prohibited based on specific access control attributes e.g., purpose of use, information type, user role, etc.";
            case NOAUTH: return "Prohibition on disclosure without information subject's authorization.";
            case NOCOLLECT: return "Prohibition on collection or storage of the information.";
            case NODSCLCD: return "Prohibition on disclosure without organizational approved patient restriction.";
            case NODSCLCDS: return "Prohibition on disclosure without a consent directive from the information subject.";
            case NOINTEGRATE: return "Prohibition on Integration into other records.";
            case NOLIST: return "Prohibition on disclosure except to entities on specific access list.";
            case NOMOU: return "Prohibition on disclosure without an interagency service agreement or memorandum of understanding (MOU).";
            case NOORGPOL: return "Prohibition on disclosure without organizational authorization.";
            case NOPAT: return "Prohibition on disclosing information to patient, family or caregivers without attending provider's authorization.\r\n\n                        \n                           Usage Note: The information may be labeled with the ActInformationSensitivity TBOO code, triggering application of this RefrainPolicy code as a handling caveat controlling access.\r\n\n                        Maps to FHIR NOPAT: Typically, this is used on an Alert resource, when the alert records information on patient abuse or non-compliance.\r\n\n                        FHIR print name is \"keep information from patient\". Maps to the French realm - code: INVISIBLE_PATIENT.\r\n\n                        \n                           displayName: Document non visible par le patient\n                           codingScheme: 1.2.250.1.213.1.1.4.13\n                        \n                        French use case:  A label for documents that the author  chose to hide from the patient until the content can be disclose to the patient in a face to face meeting between a healthcare professional and the patient (in French law some results like cancer diagnosis or AIDS diagnosis must be announced to the patient by a healthcare professional and should not be find out by the patient alone).";
            case NOPERSISTP: return "Prohibition on collection of the information beyond time necessary to accomplish authorized purpose of use is prohibited.";
            case NORDSCLCD: return "Prohibition on redisclosure without patient consent directive.";
            case NORDSCLCDS: return "Prohibition on redisclosure without a consent directive from the information subject.";
            case NORDSCLW: return "Prohibition on disclosure without authorization under jurisdictional law.";
            case NORELINK: return "Prohibition on associating de-identified or pseudonymized information with other information in a manner that could or does result in disclosing information intended to be masked.";
            case NOREUSE: return "Prohibition on use of the information beyond the purpose of use initially authorized.";
            case NOVIP: return "Prohibition on disclosure except to principals with access permission to specific VIP information.";
            case ORCON: return "Prohibition on disclosure except as permitted by the information originator.";
            case _ACTPRODUCTACQUISITIONCODE: return "The method that a product is obtained for use by the subject of the supply act (e.g. patient).  Product examples are consumable or durable goods.";
            case LOAN: return "Temporary supply of a product without transfer of ownership for the product.";
            case RENT: return "Temporary supply of a product with financial compensation, without transfer of ownership for the product.";
            case TRANSFER: return "Transfer of ownership for a product.";
            case SALE: return "Transfer of ownership for a product for financial compensation.";
            case _ACTSPECIMENTRANSPORTCODE: return "Transportation of a specimen.";
            case SREC: return "Description:Specimen has been received by the participating organization/department.";
            case SSTOR: return "Description:Specimen has been placed into storage at a participating location.";
            case STRAN: return "Description:Specimen has been put in transit to a participating receiver.";
            case _ACTSPECIMENTREATMENTCODE: return "Set of codes related to specimen treatments";
            case ACID: return "The lowering of specimen pH through the addition of an acid";
            case ALK: return "The act rendering alkaline by impregnating with an alkali; a conferring of alkaline qualities.";
            case DEFB: return "The removal of fibrin from whole blood or plasma through physical or chemical means";
            case FILT: return "The passage of a liquid through a filter, accomplished by gravity, pressure or vacuum (suction).";
            case LDLP: return "LDL Precipitation";
            case NEUT: return "The act or process by which an acid and a base are combined in such proportions that the resulting compound is neutral.";
            case RECA: return "The addition of calcium back to a specimen after it was removed by chelating agents";
            case UFIL: return "The filtration of a colloidal substance through a semipermeable medium that allows only the passage of small molecules.";
            case _ACTSUBSTANCEADMINISTRATIONCODE: return "Description: Describes the type of substance administration being performed.  This should not be used to carry codes for identification of products.  Use an associated role or entity to carry such information.";
            case DRUG: return "The introduction of a drug into a subject with the intention of altering its biologic state with the intent of improving its health status.";
            case FD: return "Description: The introduction of material into a subject with the intent of providing nutrition or other dietary supplements (e.g. minerals or vitamins).";
            case IMMUNIZ: return "The introduction of an immunogen with the intent of stimulating an immune response, aimed at preventing subsequent infections by more viable agents.";
            case _ACTTASKCODE: return "Description: A task or action that a user may perform in a clinical information system (e.g., medication order entry, laboratory test results review, problem list entry).";
            case OE: return "A clinician creates a request for a service to be performed for a given patient.";
            case LABOE: return "A clinician creates a request for a laboratory test to be done for a given patient.";
            case MEDOE: return "A clinician creates a request for the administration of one or more medications to a given patient.";
            case PATDOC: return "A person enters documentation about a given patient.";
            case ALLERLREV: return "Description: A person reviews a list of known allergies of a given patient.";
            case CLINNOTEE: return "A clinician enters a clinical note about a given patient";
            case DIAGLISTE: return "A clinician enters a diagnosis for a given patient.";
            case DISCHINSTE: return "A person provides a discharge instruction to a patient.";
            case DISCHSUME: return "A clinician enters a discharge summary for a given patient.";
            case PATEDUE: return "A person provides a patient-specific education handout to a patient.";
            case PATREPE: return "A pathologist enters a report for a given patient.";
            case PROBLISTE: return "A clinician enters a problem for a given patient.";
            case RADREPE: return "A radiologist enters a report for a given patient.";
            case IMMLREV: return "Description: A person reviews a list of immunizations due or received for a given patient.";
            case REMLREV: return "Description: A person reviews a list of health care reminders for a given patient.";
            case WELLREMLREV: return "Description: A person reviews a list of wellness or preventive care reminders for a given patient.";
            case PATINFO: return "A person (e.g., clinician, the patient herself) reviews patient information in the electronic medical record.";
            case ALLERLE: return "Description: A person enters a known allergy for a given patient.";
            case CDSREV: return "A person reviews a recommendation/assessment provided automatically by a clinical decision support application for a given patient.";
            case CLINNOTEREV: return "A person reviews a clinical note of a given patient.";
            case DISCHSUMREV: return "A person reviews a discharge summary of a given patient.";
            case DIAGLISTREV: return "A person reviews a list of diagnoses of a given patient.";
            case IMMLE: return "Description: A person enters an immunization due or received for a given patient.";
            case LABRREV: return "A person reviews a list of laboratory results of a given patient.";
            case MICRORREV: return "A person reviews a list of microbiology results of a given patient.";
            case MICROORGRREV: return "A person reviews organisms of microbiology results of a given patient.";
            case MICROSENSRREV: return "A person reviews the sensitivity test of microbiology results of a given patient.";
            case MLREV: return "A person reviews a list of medication orders submitted to a given patient";
            case MARWLREV: return "A clinician reviews a work list of medications to be administered to a given patient.";
            case OREV: return "A person reviews a list of orders submitted to a given patient.";
            case PATREPREV: return "A person reviews a pathology report of a given patient.";
            case PROBLISTREV: return "A person reviews a list of problems of a given patient.";
            case RADREPREV: return "A person reviews a radiology report of a given patient.";
            case REMLE: return "Description: A person enters a health care reminder for a given patient.";
            case WELLREMLE: return "Description: A person enters a wellness or preventive care reminder for a given patient.";
            case RISKASSESS: return "A person reviews a Risk Assessment Instrument report of a given patient.";
            case FALLRISK: return "A person reviews a Falls Risk Assessment Instrument report of a given patient.";
            case _ACTTRANSPORTATIONMODECODE: return "Characterizes how a transportation act was or will be carried out.\r\n\n                        \n                           Examples: Via private transport, via public transit, via courier.";
            case _ACTPATIENTTRANSPORTATIONMODECODE: return "Definition: Characterizes how a patient was or will be transported to the site of a patient encounter.\r\n\n                        \n                           Examples: Via ambulance, via public transit, on foot.";
            case AFOOT: return "pedestrian transport";
            case AMBT: return "ambulance transport";
            case AMBAIR: return "fixed-wing ambulance transport";
            case AMBGRND: return "ground ambulance transport";
            case AMBHELO: return "helicopter ambulance transport";
            case LAWENF: return "law enforcement transport";
            case PRVTRN: return "private transport";
            case PUBTRN: return "public transport";
            case _OBSERVATIONTYPE: return "Identifies the kinds of observations that can be performed";
            case _ACTSPECOBSCODE: return "Identifies the type of observation that is made about a specimen that may affect its processing, analysis or further result interpretation";
            case ARTBLD: return "Describes the artificial blood identifier that is associated with the specimen.";
            case DILUTION: return "An observation that reports the dilution of a sample.";
            case AUTOHIGH: return "The dilution of a sample performed by automated equipment.  The value is specified by the equipment";
            case AUTOLOW: return "The dilution of a sample performed by automated equipment.  The value is specified by the equipment";
            case PRE: return "The dilution of the specimen made prior to being loaded onto analytical equipment";
            case RERUN: return "The value of the dilution of a sample after it had been analyzed at a prior dilution value";
            case EVNFCTS: return "Domain provides codes that qualify the ActLabObsEnvfctsCode domain. (Environmental Factors)";
            case INTFR: return "An observation that relates to factors that may potentially cause interference with the observation";
            case FIBRIN: return "The Fibrin Index of the specimen. In the case of only differentiating between Absent and Present, recommend using 0 and 1";
            case HEMOLYSIS: return "An observation of the hemolysis index of the specimen in g/L";
            case ICTERUS: return "An observation that describes the icterus index of the specimen.  It is recommended to use mMol/L of bilirubin";
            case LIPEMIA: return "An observation used to describe the Lipemia Index of the specimen. It is recommended to use the optical turbidity at 600 nm (in absorbance units).";
            case VOLUME: return "An observation that reports the volume of a sample.";
            case AVAILABLE: return "The available quantity of specimen.   This is the current quantity minus any planned consumption (e.g., tests that are planned)";
            case CONSUMPTION: return "The quantity of specimen that is used each time the equipment uses this substance";
            case CURRENT: return "The current quantity of the specimen, i.e., initial quantity minus what has been actually used.";
            case INITIAL: return "The initial quantity of the specimen in inventory";
            case _ANNOTATIONTYPE: return "AnnotationType";
            case _ACTPATIENTANNOTATIONTYPE: return "Description:Provides a categorization for annotations recorded directly against the patient .";
            case ANNDI: return "Description:A note that is specific to a patient's diagnostic images, either historical, current or planned.";
            case ANNGEN: return "Description:A general or uncategorized note.";
            case ANNIMM: return "A note that is specific to a patient's immunizations, either historical, current or planned.";
            case ANNLAB: return "Description:A note that is specific to a patient's laboratory results, either historical, current or planned.";
            case ANNMED: return "Description:A note that is specific to a patient's medications, either historical, current or planned.";
            case _GENETICOBSERVATIONTYPE: return "Description: None provided";
            case GENE: return "Description: A DNA segment that contributes to phenotype/function. In the absence of demonstrated function a gene may be characterized by sequence, transcription or homology";
            case _IMMUNIZATIONOBSERVATIONTYPE: return "Description: Observation codes which describe characteristics of the immunization material.";
            case OBSANTC: return "Description: Indicates the valid antigen count.";
            case OBSANTV: return "Description: Indicates whether an antigen is valid or invalid.";
            case _INDIVIDUALCASESAFETYREPORTTYPE: return "A code that is used to indicate the type of case safety report received from sender. The current code example reference is from the International Conference on Harmonisation (ICH) Expert Workgroup guideline on Clinical Safety Data Management: Data Elements for Transmission of Individual Case Safety Reports. The unknown/unavailable option allows the transmission of information from a secondary sender where the initial sender did not specify the type of report.\r\n\n                        Example concepts include: Spontaneous, Report from study, Other.";
            case PATADVEVNT: return "Indicates that the ICSR is describing problems that a patient experienced after receiving a vaccine product.";
            case VACPROBLEM: return "Indicates that the ICSR is describing a problem with the actual vaccine product such as physical defects (cloudy, particulate matter) or inability to confer immunity.";
            case _LOINCOBSERVATIONACTCONTEXTAGETYPE: return "Definition:The set of LOINC codes for the act of determining the period of time that has elapsed since an entity was born or created.";
            case _216119: return "Definition:Estimated age.";
            case _216127: return "Definition:Reported age.";
            case _295535: return "Definition:Calculated age.";
            case _305250: return "Definition:General specification of age with no implied method of determination.";
            case _309724: return "Definition:Age at onset of associated adverse event; no implied method of determination.";
            case _MEDICATIONOBSERVATIONTYPE: return "MedicationObservationType";
            case REPHALFLIFE: return "Description:This observation represents an 'average' or 'expected' half-life typical of the product.";
            case SPLCOATING: return "Definition: A characteristic of an oral solid dosage form of a medicinal product, indicating whether it has one or more coatings such as sugar coating, film coating, or enteric coating.  Only coatings to the external surface or the dosage form should be considered (for example, coatings to individual pellets or granules inside a capsule or tablet are excluded from consideration).\r\n\n                        \n                           Constraints: The Observation.value must be a Boolean (BL) with true for the presence or false for the absence of one or more coatings on a solid dosage form.";
            case SPLCOLOR: return "Definition:  A characteristic of an oral solid dosage form of a medicinal product, specifying the color or colors that most predominantly define the appearance of the dose form. SPLCOLOR is not an FDA specification for the actual color of solid dosage forms or the names of colors that can appear in labeling.\r\n\n                        \n                           Constraints: The Observation.value must be a single coded value or a list of multiple coded values, specifying one or more distinct colors that approximate of the color(s) of distinct areas of the solid dosage form, such as the different sides of a tablet or one-part capsule, or the different halves of a two-part capsule.  Bands on banded capsules, regardless of the color, are not considered when assigning an SPLCOLOR. Imprints on the dosage form, regardless of their color are not considered when assigning an SPLCOLOR. If more than one color exists on a particular side or half, then the most predominant color on that side or half is recorded.  If the gelatin capsule shell is colorless and transparent, use the predominant color of the contents that appears through the colorless and transparent capsule shell. Colors can include: Black;Gray;White;Red;Pink;Purple;Green;Yellow;Orange;Brown;Blue;Turquoise.";
            case SPLIMAGE: return "Description: A characteristic representing a single file reference that contains two or more views of the same dosage form of the product; in most cases this should represent front and back views of the dosage form, but occasionally additional views might be needed in order to capture all of the important physical characteristics of the dosage form.  Any imprint and/or symbol should be clearly identifiable, and the viewer should not normally need to rotate the image in order to read it.  Images that are submitted with SPL should be included in the same directory as the SPL file.";
            case SPLIMPRINT: return "Definition:  A characteristic of an oral solid dosage form of a medicinal product, specifying the alphanumeric text that appears on the solid dosage form, including text that is embossed, debossed, engraved or printed with ink. The presence of other non-textual distinguishing marks or symbols is recorded by SPLSYMBOL.\r\n\n                        \n                           Examples: Included in SPLIMPRINT are alphanumeric text that appears on the bands of banded capsules and logos and other symbols that can be interpreted as letters or numbers.\r\n\n                        \n                           Constraints: The Observation.value must be of type Character String (ST). Excluded from SPLIMPRINT are internal and external cut-outs in the form of alphanumeric text and the letter 'R' with a circle around it (when referring to a registered trademark) and the letters 'TM' (when referring to a 'trade mark').  To record text, begin on either side or part of the dosage form. Start at the top left and progress as one would normally read a book.  Enter a semicolon to show separation between words or line divisions.";
            case SPLSCORING: return "Definition: A characteristic of an oral solid dosage form of a medicinal product, specifying the number of equal pieces that the solid dosage form can be divided into using score line(s). \r\n\n                        \n                           Example: One score line creating two equal pieces is given a value of 2, two parallel score lines creating three equal pieces is given a value of 3.\r\n\n                        \n                           Constraints: Whether three parallel score lines create four equal pieces or two intersecting score lines create two equal pieces using one score line and four equal pieces using both score lines, both have the scoring value of 4. Solid dosage forms that are not scored are given a value of 1. Solid dosage forms that can only be divided into unequal pieces are given a null-value with nullFlavor other (OTH).";
            case SPLSHAPE: return "Description: A characteristic of an oral solid dosage form of a medicinal product, specifying the two dimensional representation of the solid dose form, in terms of the outside perimeter of a solid dosage form when the dosage form, resting on a flat surface, is viewed from directly above, including slight rounding of corners. SPLSHAPE does not include embossing, scoring, debossing, or internal cut-outs.  SPLSHAPE is independent of the orientation of the imprint and logo. Shapes can include: Triangle (3 sided); Square; Round; Semicircle; Pentagon (5 sided); Diamond; Double circle; Bullet; Hexagon (6 sided); Rectangle; Gear; Capsule; Heptagon (7 sided); Trapezoid; Oval; Clover; Octagon (8 sided); Tear; Freeform.";
            case SPLSIZE: return "Definition: A characteristic of an oral solid dosage form of a medicinal product, specifying the longest single dimension of the solid dosage form as a physical quantity in the dimension of length (e.g., 3 mm). The length is should be specified in millimeters and should be rounded to the nearest whole millimeter.\r\n\n                        \n                           Example: SPLSIZE for a rectangular shaped tablet is the length and SPLSIZE for a round shaped tablet is the diameter.";
            case SPLSYMBOL: return "Definition: A characteristic of an oral solid dosage form of a medicinal product, to describe whether or not the medicinal product has a mark or symbol appearing on it for easy and definite recognition.  Score lines, letters, numbers, and internal and external cut-outs are not considered marks or symbols. See SPLSCORING and SPLIMPRINT for these characteristics.\r\n\n                        \n                           Constraints: The Observation.value must be a Boolean (BL) with <u>true</u> indicating the presence and <u>false</u> for the absence of marks or symbols.\r\n\n                        \n                           Example:";
            case _OBSERVATIONISSUETRIGGERCODEDOBSERVATIONTYPE: return "Distinguishes the kinds of coded observations that could be the trigger for clinical issue detection. These are observations that are not measurable, but instead can be defined with codes. Coded observation types include: Allergy, Intolerance, Medical Condition, Pregnancy status, etc.";
            case _CASETRANSMISSIONMODE: return "Code for the mechanism by which disease was acquired by the living subject involved in the public health case. Includes sexually transmitted, airborne, bloodborne, vectorborne, foodborne, zoonotic, nosocomial, mechanical, dermal, congenital, environmental exposure, indeterminate.";
            case AIRTRNS: return "Communication of an agent from a living subject or environmental source to a living subject through indirect contact via oral or nasal inhalation.";
            case ANANTRNS: return "Communication of an agent from one animal to another proximate animal.";
            case ANHUMTRNS: return "Communication of an agent from an animal to a proximate person.";
            case BDYFLDTRNS: return "Communication of an agent from one living subject to another living subject through direct contact with any body fluid.";
            case BLDTRNS: return "Communication of an agent to a living subject through direct contact with blood or blood products whether the contact with blood is part of  a therapeutic procedure or not.";
            case DERMTRNS: return "Communication of an agent from a living subject or environmental source to a living subject via agent migration through intact skin.";
            case ENVTRNS: return "Communication of an agent from an environmental surface or source to a living subject by direct contact.";
            case FECTRNS: return "Communication of an agent from a living subject or environmental source to a living subject through oral contact with material contaminated by person or animal fecal material.";
            case FOMTRNS: return "Communication of an agent from an non-living material to a living subject through direct contact.";
            case FOODTRNS: return "Communication of an agent from a food source to a living subject via oral consumption.";
            case HUMHUMTRNS: return "Communication of an agent from a person to a proximate person.";
            case INDTRNS: return "Communication of an agent to a living subject via an undetermined route.";
            case LACTTRNS: return "Communication of an agent from one living subject to another living subject through direct contact with mammalian milk or colostrum.";
            case NOSTRNS: return "Communication of an agent from any entity to a living subject while the living subject is in the patient role in a healthcare facility.";
            case PARTRNS: return "Communication of an agent from a living subject or environmental source to a living subject where the acquisition of the agent is not via the alimentary canal.";
            case PLACTRNS: return "Communication of an agent from a living subject to the progeny of that living subject via agent migration across the maternal-fetal placental membranes while in utero.";
            case SEXTRNS: return "Communication of an agent from one living subject to another living subject through direct contact with genital or oral tissues as part of a sexual act.";
            case TRNSFTRNS: return "Communication of an agent from one living subject to another living subject through direct contact with blood or blood products where the contact with blood is part of  a therapeutic procedure.";
            case VECTRNS: return "Communication of an agent from a living subject acting as a required intermediary in the agent transmission process to a recipient living subject via direct contact.";
            case WATTRNS: return "Communication of an agent from a contaminated water source to a living subject whether the water is ingested as a food or not. The route of entry of the water may be through any bodily orifice.";
            case _OBSERVATIONQUALITYMEASUREATTRIBUTE: return "Codes used to define various metadata aspects of a health quality measure.";
            case AGGREGATE: return "Indicates that the observation is carrying out an aggregation calculation, contained in the value element.";
            case COPY: return "Identifies the organization(s) who own the intellectual property represented by the eMeasure.";
            case CRS: return "Summary of relevant clinical guidelines or other clinical recommendations supporting this eMeasure.";
            case DEF: return "Description of individual terms, provided as needed.";
            case DISC: return "Disclaimer information for the eMeasure.";
            case FINALDT: return "The timestamp when the eMeasure was last packaged in the Measure Authoring Tool.";
            case GUIDE: return "Used to allow measure developers to provide additional guidance for implementers to understand greater specificity than could be provided in the logic for data criteria.";
            case IDUR: return "Information on whether an increase or decrease in score is the preferred result \n(e.g., a higher score indicates better quality OR a lower score indicates better quality OR quality is within a range).";
            case ITMCNT: return "Describes the items counted by the measure (e.g., patients, encounters, procedures, etc.)";
            case KEY: return "A significant word that aids in discoverability.";
            case MEDT: return "The end date of the measurement period.";
            case MSD: return "The start date of the measurement period.";
            case MSRADJ: return "The method of adjusting for clinical severity and conditions present at the start of care that can influence patient outcomes for making valid comparisons of outcome measures across providers. Indicates whether an eMeasure is subject to the statistical process for reducing, removing, or clarifying the influences of confounding factors to allow more useful comparisons.";
            case MSRAGG: return "Describes how to combine information calculated based on logic in each of several populations into one summarized result. It can also be used to describe how to risk adjust the data based on supplemental data elements described in the eMeasure. (e.g., pneumonia hospital measures antibiotic selection in the ICU versus non-ICU and then the roll-up of the two). \r\n\n                        \n                           Open Issue: The description does NOT align well with the definition used in the HQMF specfication; correct the MSGAGG definition, and the possible distinction of MSRAGG as a child of AGGREGATE.";
            case MSRIMPROV: return "Information on whether an increase or decrease in score is the preferred result. This should reflect information on which way is better, an increase or decrease in score.";
            case MSRJUR: return "The list of jurisdiction(s) for which the measure applies.";
            case MSRRPTR: return "Type of person or organization that is expected to report the issue.";
            case MSRRPTTIME: return "The maximum time that may elapse following completion of the measure until the measure report must be sent to the receiver.";
            case MSRSCORE: return "Indicates how the calculation is performed for the eMeasure \n(e.g., proportion, continuous variable, ratio)";
            case MSRSET: return "Location(s) in which care being measured is rendered\r\n\n                        Usage Note: MSRSET is used rather than RoleCode because the setting applies to what is being measured, as opposed to participating directly in the health quality measure documantion itself).";
            case MSRTOPIC: return "health quality measure topic type";
            case MSRTP: return "The time period for which the eMeasure applies.";
            case MSRTYPE: return "Indicates whether the eMeasure is used to examine a process or an outcome over time \n(e.g., Structure, Process, Outcome).";
            case RAT: return "Succinct statement of the need for the measure. Usually includes statements pertaining to Importance criterion: impact, gap in care and evidence.";
            case REF: return "Identifies bibliographic citations or references to clinical practice guidelines, sources of evidence, or other relevant materials supporting the intent and rationale of the eMeasure.";
            case SDE: return "Comparison of results across strata can be used to show where disparities exist or where there is a need to expose differences in results. For example, Centers for Medicare & Medicaid Services (CMS) in the U.S. defines four required Supplemental Data Elements (payer, ethnicity, race, and gender), which are variables used to aggregate data into various subgroups. Additional supplemental data elements required for risk adjustment or other purposes of data aggregation can be included in the Supplemental Data Element section.";
            case STRAT: return "Describes the strata for which the measure is to be evaluated. There are three examples of reasons for stratification based on existing work. These include: (1) evaluate the measure based on different age groupings within the population described in the measure (e.g., evaluate the whole [age 14-25] and each sub-stratum [14-19] and [20-25]); (2) evaluate the eMeasure based on either a specific condition, a specific discharge location, or both; (3) evaluate the eMeasure based on different locations within a facility (e.g., evaluate the overall rate for all intensive care units and also some strata include additional findings [specific birth weights for neonatal intensive care units]).";
            case TRANF: return "Can be a URL or hyperlinks that link to the transmission formats that are specified for a particular reporting program.";
            case USE: return "Usage notes.";
            case _OBSERVATIONSEQUENCETYPE: return "ObservationSequenceType";
            case TIMEABSOLUTE: return "A sequence of values in the \"absolute\" time domain.  This is the same time domain that all HL7 timestamps use.  It is time as measured by the Gregorian calendar";
            case TIMERELATIVE: return "A sequence of values in a \"relative\" time domain.  The time is measured relative to the earliest effective time in the Observation Series containing this sequence.";
            case _OBSERVATIONSERIESTYPE: return "ObservationSeriesType";
            case _ECGOBSERVATIONSERIESTYPE: return "ECGObservationSeriesType";
            case REPRESENTATIVEBEAT: return "This Observation Series type contains waveforms of a \"representative beat\" (a.k.a. \"median beat\" or \"average beat\").  The waveform samples are measured in relative time, relative to the beginning of the beat as defined by the Observation Series effective time.  The waveforms are not directly acquired from the subject, but rather algorithmically derived from the \"rhythm\" waveforms.";
            case RHYTHM: return "This Observation type contains ECG \"rhythm\" waveforms.  The waveform samples are measured in absolute time (a.k.a. \"subject time\" or \"effective time\").  These waveforms are usually \"raw\" with some minimal amount of noise reduction and baseline filtering applied.";
            case _PATIENTIMMUNIZATIONRELATEDOBSERVATIONTYPE: return "Description: Reporting codes that are related to an immunization event.";
            case CLSSRM: return "Description: The class room associated with the patient during the immunization event.";
            case GRADE: return "Description: The school grade or level the patient was in when immunized.";
            case SCHL: return "Description: The school the patient attended when immunized.";
            case SCHLDIV: return "Description: The school division or district associated with the patient during the immunization event.";
            case TEACHER: return "Description: The patient's teacher when immunized.";
            case _POPULATIONINCLUSIONOBSERVATIONTYPE: return "Observation types for specifying criteria used to assert that a subject is included in a particular population.";
            case DENEX: return "Criteria which specify subjects who should be removed from the eMeasure population and denominator before determining if numerator criteria are met. Denominator exclusions are used in proportion and ratio measures to help narrow the denominator.";
            case DENEXCEP: return "Criteria which specify the removal of a subject, procedure or unit of measurement from the denominator, only if the numerator criteria are not met. Denominator exceptions allow for adjustment of the calculated score for those providers with higher risk populations. Denominator exceptions are used only in proportion eMeasures. They are not appropriate for ratio or continuous variable eMeasures. Denominator exceptions allow for the exercise of clinical judgment and should be specifically defined where capturing the information in a structured manner fits the clinical workflow. Generic denominator exception reasons used in proportion eMeasures fall into three general categories:\r\n\n                        \n                           Medical reasons\n                           Patient (or subject) reasons\n                           System reasons";
            case DENOM: return "Criteria for specifying the entities to be evaluated by a specific quality measure, based on a shared common set of characteristics (within a specific measurement set to which a given measure belongs).  The denominator can be the same as the initial population, or it may be a subset of the initial population to further constrain it for the purpose of the eMeasure. Different measures within an eMeasure set may have different denominators. Continuous Variable eMeasures do not have a denominator, but instead define a measure population.";
            case IPOP: return "Criteria for specifying the entities to be evaluated by a specific quality measure, based on a shared common set of characteristics (within a specific measurement set to which a given measure belongs).";
            case IPPOP: return "Criteria for specifying the patients to be evaluated by a specific quality measure, based on a shared common set of characteristics (within a specific measurement set to which a given measure belongs). Details often include information based upon specific age groups, diagnoses, diagnostic and procedure codes, and enrollment periods.";
            case MSRPOPL: return "Criteria for specifying\nthe measure population as a narrative description (e.g., all patients seen in the Emergency Department during the measurement period).  This is used only in continuous variable eMeasures.";
            case MSRPOPLEX: return "Criteria for specifying subjects who should be removed from the eMeasure's Initial Population and Measure Population. Measure Population Exclusions are used in Continuous Variable measures to help narrow the Measure Population before determining the value(s) of the continuous variable(s).";
            case NUMER: return "Criteria for specifying the processes or outcomes expected for each patient, procedure, or other unit of measurement defined in the denominator for proportion measures, or related to (but not directly derived from) the denominator for ratio measures (e.g., a numerator listing the number of central line blood stream infections and a denominator indicating the days per thousand of central line usage in a specific time period).";
            case NUMEX: return "Criteria for specifying instances that should not be included in the numerator data. (e.g., if the number of central line blood stream infections per 1000 catheter days were to exclude infections with a specific bacterium, that bacterium would be listed as a numerator exclusion).  Numerator Exclusions are used only in ratio eMeasures.";
            case _PREFERENCEOBSERVATIONTYPE: return "Types of observations that can be made about Preferences.";
            case PREFSTRENGTH: return "An observation about how important a preference is to the target of the preference.";
            case ADVERSEREACTION: return "Indicates that the observation is of an unexpected negative occurrence in the subject suspected to result from the subject's exposure to one or more agents.  Observation values would be the symptom resulting from the reaction.";
            case ASSERTION: return "Description:Refines classCode OBS to indicate an observation in which observation.value contains a finding or other nominalized statement, where the encoded information in Observation.value is not altered by Observation.code.  For instance, observation.code=\"ASSERTION\" and observation.value=\"fracture of femur present\" is an assertion of a clinical finding of femur fracture.";
            case CASESER: return "Definition:An observation that provides a characterization of the level of harm to an investigation subject as a result of a reaction or event.";
            case CDIO: return "An observation that states whether the disease was likely acquired outside the jurisdiction of observation, and if so, the nature of the inter-jurisdictional relationship.\r\n\n                        \n                           OpenIssue: This code could be moved to LOINC if it can be done before there are significant implemenations using it.";
            case CRIT: return "A clinical judgment as to the worst case result of a future exposure (including substance administration). When the worst case result is assessed to have a life-threatening or organ system threatening potential, it is considered to be of high criticality.";
            case CTMO: return "An observation that states the mechanism by which disease was acquired by the living subject involved in the public health case.\r\n\n                        \n                           OpenIssue: This code could be moved to LOINC if it can be done before there are significant implemenations using it.";
            case DX: return "Includes all codes defining types of indications such as diagnosis, symptom and other indications such as contrast agents for lab tests.";
            case ADMDX: return "Admitting diagnosis are the diagnoses documented  for administrative purposes as the basis for a hospital admission.";
            case DISDX: return "Discharge diagnosis are the diagnoses documented for administrative purposes as the time of hospital discharge.";
            case INTDX: return "Intermediate diagnoses are those diagnoses documented for administrative purposes during the course of a hospital stay.";
            case NOI: return "The type of injury that the injury coding specifies.";
            case GISTIER: return "Description: Accuracy determined as per the GIS tier code system.";
            case HHOBS: return "Indicates that the observation is of a person’s living situation in a household including the household composition and circumstances.";
            case ISSUE: return "There is a clinical issue for the therapy that makes continuation of the therapy inappropriate.\r\n\n                        \n                           Open Issue: The definition of this code does not correctly represent the concept space of its specializations (children)";
            case _ACTADMINISTRATIVEDETECTEDISSUECODE: return "Identifies types of detectyed issues for Act class \"ALRT\" for the administrative and patient administrative acts domains.";
            case _ACTADMINISTRATIVEAUTHORIZATIONDETECTEDISSUECODE: return "ActAdministrativeAuthorizationDetectedIssueCode";
            case NAT: return "The requesting party has insufficient authorization to invoke the interaction.";
            case SUPPRESSED: return "Description: One or more records in the query response have been suppressed due to consent or privacy restrictions.";
            case VALIDAT: return "Description:The specified element did not pass business-rule validation.";
            case KEY204: return "The ID of the patient, order, etc., was not found. Used for transactions other than additions, e.g. transfer of a non-existent patient.";
            case KEY205: return "The ID of the patient, order, etc., already exists. Used in response to addition transactions (Admit, New Order, etc.).";
            case COMPLY: return "There may be an issue with the patient complying with the intentions of the proposed therapy";
            case DUPTHPY: return "The proposed therapy appears to duplicate an existing therapy";
            case DUPTHPCLS: return "Description:The proposed therapy appears to have the same intended therapeutic benefit as an existing therapy, though the specific mechanisms of action vary.";
            case DUPTHPGEN: return "Description:The proposed therapy appears to have the same intended therapeutic benefit as an existing therapy and uses the same mechanisms of action as the existing therapy.";
            case ABUSE: return "Description:The proposed therapy is frequently misused or abused and therefore should be used with caution and/or monitoring.";
            case FRAUD: return "Description:The request is suspected to have a fraudulent basis.";
            case PLYDOC: return "A similar or identical therapy was recently ordered by a different practitioner.";
            case PLYPHRM: return "This patient was recently supplied a similar or identical therapy from a different pharmacy or supplier.";
            case DOSE: return "Proposed dosage instructions for therapy differ from standard practice.";
            case DOSECOND: return "Description:Proposed dosage is inappropriate due to patient's medical condition.";
            case DOSEDUR: return "Proposed length of therapy differs from standard practice.";
            case DOSEDURH: return "Proposed length of therapy is longer than standard practice";
            case DOSEDURHIND: return "Proposed length of therapy is longer than standard practice for the identified indication or diagnosis";
            case DOSEDURL: return "Proposed length of therapy is shorter than that necessary for therapeutic effect";
            case DOSEDURLIND: return "Proposed length of therapy is shorter than standard practice for the identified indication or diagnosis";
            case DOSEH: return "Proposed dosage exceeds standard practice";
            case DOSEHINDA: return "Proposed dosage exceeds standard practice for the patient's age";
            case DOSEHIND: return "High Dose for Indication Alert";
            case DOSEHINDSA: return "Proposed dosage exceeds standard practice for the patient's height or body surface area";
            case DOSEHINDW: return "Proposed dosage exceeds standard practice for the patient's weight";
            case DOSEIVL: return "Proposed dosage interval/timing differs from standard practice";
            case DOSEIVLIND: return "Proposed dosage interval/timing differs from standard practice for the identified indication or diagnosis";
            case DOSEL: return "Proposed dosage is below suggested therapeutic levels";
            case DOSELINDA: return "Proposed dosage is below suggested therapeutic levels for the patient's age";
            case DOSELIND: return "Low Dose for Indication Alert";
            case DOSELINDSA: return "Proposed dosage is below suggested therapeutic levels for the patient's height or body surface area";
            case DOSELINDW: return "Proposed dosage is below suggested therapeutic levels for the patient's weight";
            case MDOSE: return "Description:The maximum quantity of this drug allowed to be administered within a particular time-range (month, year, lifetime) has been reached or exceeded.";
            case OBSA: return "Proposed therapy may be inappropriate or contraindicated due to conditions or characteristics of the patient";
            case AGE: return "Proposed therapy may be inappropriate or contraindicated due to patient age";
            case ADALRT: return "Proposed therapy is outside of the standard practice for an adult patient.";
            case GEALRT: return "Proposed therapy is outside of standard practice for a geriatric patient.";
            case PEALRT: return "Proposed therapy is outside of the standard practice for a pediatric patient.";
            case COND: return "Proposed therapy may be inappropriate or contraindicated due to an existing/recent patient condition or diagnosis";
            case HGHT: return "";
            case LACT: return "Proposed therapy may be inappropriate or contraindicated when breast-feeding";
            case PREG: return "Proposed therapy may be inappropriate or contraindicated during pregnancy";
            case WGHT: return "";
            case CREACT: return "Description:Proposed therapy may be inappropriate or contraindicated because of a common but non-patient specific reaction to the product.\r\n\n                        \n                           Example:There is no record of a specific sensitivity for the patient, but the presence of the sensitivity is common and therefore caution is warranted.";
            case GEN: return "Proposed therapy may be inappropriate or contraindicated due to patient genetic indicators.";
            case GEND: return "Proposed therapy may be inappropriate or contraindicated due to patient gender.";
            case LAB: return "Proposed therapy may be inappropriate or contraindicated due to recent lab test results";
            case REACT: return "Proposed therapy may be inappropriate or contraindicated based on the potential for a patient reaction to the proposed product";
            case ALGY: return "Proposed therapy may be inappropriate or contraindicated because of a recorded patient allergy to the proposed product.  (Allergies are immune based reactions.)";
            case INT: return "Proposed therapy may be inappropriate or contraindicated because of a recorded patient intolerance to the proposed product.  (Intolerances are non-immune based sensitivities.)";
            case RREACT: return "Proposed therapy may be inappropriate or contraindicated because of a potential patient reaction to a cross-sensitivity related product.";
            case RALG: return "Proposed therapy may be inappropriate or contraindicated because of a recorded patient allergy to a cross-sensitivity related product.  (Allergies are immune based reactions.)";
            case RAR: return "Proposed therapy may be inappropriate or contraindicated because of a recorded prior adverse reaction to a cross-sensitivity related product.";
            case RINT: return "Proposed therapy may be inappropriate or contraindicated because of a recorded patient intolerance to a cross-sensitivity related product.  (Intolerances are non-immune based sensitivities.)";
            case BUS: return "Description:A local business rule relating multiple elements has been violated.";
            case CODEINVAL: return "Description:The specified code is not valid against the list of codes allowed for the element.";
            case CODEDEPREC: return "Description:The specified code has been deprecated and should no longer be used.  Select another code from the code system.";
            case FORMAT: return "Description:The element does not follow the formatting or type rules defined for the field.";
            case ILLEGAL: return "Description:The request is missing elements or contains elements which cause it to not meet the legal standards for actioning.";
            case LENRANGE: return "Description:The length of the data specified falls out of the range defined for the element.";
            case LENLONG: return "Description:The length of the data specified is greater than the maximum length defined for the element.";
            case LENSHORT: return "Description:The length of the data specified is less than the minimum length defined for the element.";
            case MISSCOND: return "Description:The specified element must be specified with a non-null value under certain conditions.  In this case, the conditions are true but the element is still missing or null.";
            case MISSMAND: return "Description:The specified element is mandatory and was not included in the instance.";
            case NODUPS: return "Description:More than one element with the same value exists in the set.  Duplicates not permission in this set in a set.";
            case NOPERSIST: return "Description: Element in submitted message will not persist in data storage based on detected issue.";
            case REPRANGE: return "Description:The number of repeating elements falls outside the range of the allowed number of repetitions.";
            case MAXOCCURS: return "Description:The number of repeating elements is above the maximum number of repetitions allowed.";
            case MINOCCURS: return "Description:The number of repeating elements is below the minimum number of repetitions allowed.";
            case _ACTADMINISTRATIVERULEDETECTEDISSUECODE: return "ActAdministrativeRuleDetectedIssueCode";
            case KEY206: return "Description: Metadata associated with the identification (e.g. name or gender) does not match the identification being verified.";
            case OBSOLETE: return "Description: One or more records in the query response have a status of 'obsolete'.";
            case _ACTSUPPLIEDITEMDETECTEDISSUECODE: return "Identifies types of detected issues regarding the administration or supply of an item to a patient.";
            case _ADMINISTRATIONDETECTEDISSUECODE: return "Administration of the proposed therapy may be inappropriate or contraindicated as proposed";
            case _APPROPRIATENESSDETECTEDISSUECODE: return "AppropriatenessDetectedIssueCode";
            case _INTERACTIONDETECTEDISSUECODE: return "InteractionDetectedIssueCode";
            case FOOD: return "Proposed therapy may interact with certain foods";
            case TPROD: return "Proposed therapy may interact with an existing or recent therapeutic product";
            case DRG: return "Proposed therapy may interact with an existing or recent drug therapy";
            case NHP: return "Proposed therapy may interact with existing or recent natural health product therapy";
            case NONRX: return "Proposed therapy may interact with a non-prescription drug (e.g. alcohol, tobacco, Aspirin)";
            case PREVINEF: return "Definition:The same or similar treatment has previously been attempted with the patient without achieving a positive effect.";
            case DACT: return "Description:Proposed therapy may be contraindicated or ineffective based on an existing or recent drug therapy.";
            case TIME: return "Description:Proposed therapy may be inappropriate or ineffective based on the proposed start or end time.";
            case ALRTENDLATE: return "Definition:Proposed therapy may be inappropriate or ineffective because the end of administration is too close to another planned therapy.";
            case ALRTSTRTLATE: return "Definition:Proposed therapy may be inappropriate or ineffective because the start of administration is too late after the onset of the condition.";
            case _SUPPLYDETECTEDISSUECODE: return "Supplying the product at this time may be inappropriate or indicate compliance issues with the associated therapy";
            case ALLDONE: return "Definition:The requested action has already been performed and so this request has no effect";
            case FULFIL: return "Definition:The therapy being performed is in some way out of alignment with the requested therapy.";
            case NOTACTN: return "Definition:The status of the request being fulfilled has changed such that it is no longer actionable.  This may be because the request has expired, has already been completely fulfilled or has been otherwise stopped or disabled.  (Not used for 'suspended' orders.)";
            case NOTEQUIV: return "Definition:The therapy being performed is not sufficiently equivalent to the therapy which was requested.";
            case NOTEQUIVGEN: return "Definition:The therapy being performed is not generically equivalent (having the identical biological action) to the therapy which was requested.";
            case NOTEQUIVTHER: return "Definition:The therapy being performed is not therapeutically equivalent (having the same overall patient effect) to the therapy which was requested.";
            case TIMING: return "Definition:The therapy is being performed at a time which diverges from the time the therapy was requested";
            case INTERVAL: return "Definition:The therapy action is being performed outside the bounds of the time period requested";
            case MINFREQ: return "Definition:The therapy action is being performed too soon after the previous occurrence based on the requested frequency";
            case HELD: return "Definition:There should be no actions taken in fulfillment of a request that has been held or suspended.";
            case TOOLATE: return "The patient is receiving a subsequent fill significantly later than would be expected based on the amount previously supplied and the therapy dosage instructions";
            case TOOSOON: return "The patient is receiving a subsequent fill significantly earlier than would be expected based on the amount previously supplied and the therapy dosage instructions";
            case HISTORIC: return "Description: While the record was accepted in the repository, there is a more recent version of a record of this type.";
            case PATPREF: return "Definition:The proposed therapy goes against preferences or consent constraints recorded in the patient's record.";
            case PATPREFALT: return "Definition:The proposed therapy goes against preferences or consent constraints recorded in the patient's record.  An alternate therapy meeting those constraints is available.";
            case KSUBJ: return "Categorization of types of observation that capture the main clinical knowledge subject which may be a medication, a laboratory test, a disease.";
            case KSUBT: return "Categorization of types of observation that capture a knowledge subtopic which might be treatment, etiology, or prognosis.";
            case OINT: return "Hypersensitivity resulting in an adverse reaction upon exposure to an agent.";
            case ALG: return "Hypersensitivity to an agent caused by an immunologic response to an initial exposure";
            case DALG: return "An allergy to a pharmaceutical product.";
            case EALG: return "An allergy to a substance other than a drug or a food.  E.g. Latex, pollen, etc.";
            case FALG: return "An allergy to a substance generally consumed for nutritional purposes.";
            case DINT: return "Hypersensitivity resulting in an adverse reaction upon exposure to a drug.";
            case DNAINT: return "Hypersensitivity to an agent caused by a mechanism other than an immunologic response to an initial exposure";
            case EINT: return "Hypersensitivity resulting in an adverse reaction upon exposure to environmental conditions.";
            case ENAINT: return "Hypersensitivity to an agent caused by a mechanism other than an immunologic response to an initial exposure";
            case FINT: return "Hypersensitivity resulting in an adverse reaction upon exposure to food.";
            case FNAINT: return "Hypersensitivity to an agent caused by a mechanism other than an immunologic response to an initial exposure";
            case NAINT: return "Hypersensitivity to an agent caused by a mechanism other than an immunologic response to an initial exposure";
            case SEV: return "A subjective evaluation of the seriousness or intensity associated with another observation.";
            case _ROIOVERLAYSHAPE: return "Shape of the region on the object being referenced";
            case CIRCLE: return "A circle defined by two (column,row) pairs. The first point is the center of the circle and the second point is a point on the perimeter of the circle.";
            case ELLIPSE: return "An ellipse defined by four (column,row) pairs, the first two points specifying the endpoints of the major axis and the second two points specifying the endpoints of the minor axis.";
            case POINT: return "A single point denoted by a single (column,row) pair, or multiple points each denoted by a (column,row) pair.";
            case POLY: return "A series of connected line segments with ordered vertices denoted by (column,row) pairs; if the first and last vertices are the same, it is a closed polygon.";
            case C: return "Description:Indicates that result data has been corrected.";
            case DIET: return "Code set to define specialized/allowed diets";
            case BR: return "A diet exclusively composed of oatmeal, semolina, or rice, to be extremely easy to eat and digest.";
            case DM: return "A diet that uses carbohydrates sparingly.  Typically with a restriction in daily energy content (e.g. 1600-2000 kcal).";
            case FAST: return "No enteral intake of foot or liquids  whatsoever, no smoking.  Typically 6 to 8 hours before anesthesia.";
            case FORMULA: return "A diet consisting of a formula feeding, either for an infant or an adult, to provide nutrition either orally or through the gastrointestinal tract via tube, catheter or stoma.";
            case GF: return "Gluten free diet for celiac disease.";
            case LF: return "A diet low in fat, particularly to patients with hepatic diseases.";
            case LP: return "A low protein diet for patients with renal failure.";
            case LQ: return "A strictly liquid diet, that can be fully absorbed in the intestine, and therefore may not contain fiber.  Used before enteral surgeries.";
            case LS: return "A diet low in sodium for patients with congestive heart failure and/or renal failure.";
            case N: return "A normal diet, i.e. no special preparations or restrictions for medical reasons. This is notwithstanding any preferences the patient might have regarding special foods, such as vegetarian, kosher, etc.";
            case NF: return "A no fat diet for acute hepatic diseases.";
            case PAF: return "Phenylketonuria diet.";
            case PAR: return "Patient is supplied with parenteral nutrition, typically described in terms of i.v. medications.";
            case RD: return "A diet that seeks to reduce body fat, typically low energy content (800-1600 kcal).";
            case SCH: return "A diet that avoids ingredients that might cause digestion problems, e.g., avoid excessive fat, avoid too much fiber (cabbage, peas, beans).";
            case SUPPLEMENT: return "A diet that is not intended to be complete but is added to other diets.";
            case T: return "This is not really a diet, since it contains little nutritional value, but is essentially just water.  Used before coloscopy examinations.";
            case VLI: return "Diet with low content of the amino-acids valin, leucin, and isoleucin, for \"maple syrup disease.\"";
            case DRUGPRG: return "Definition: A public or government health program that administers and funds coverage for prescription drugs to assist program eligible who meet financial and health status criteria.";
            case F: return "Description:Indicates that a result is complete.  No further results are to come.  This maps to the 'complete' state in the observation result status code.";
            case PRLMN: return "Description:Indicates that a result is incomplete.  There are further results to come.  This maps to the 'active' state in the observation result status code.";
            case SECOBS: return "An observation identifying security metadata about an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.  Security metadata are used to name security labels.  \r\n\n                        \n                           Rationale: According to ISO/TS 22600-3:2009(E) A.9.1.7 SECURITY LABEL MATCHING, Security label matching compares the initiator's clearance to the target's security label.  All of the following must be true for authorization to be granted:\r\n\n                        \n                           The security policy identifiers shall be identical\n                           The classification level of the initiator shall be greater than or equal to that of the target (that is, there shall be at least one value in the classification list of the clearance greater than or equal to the classification of the target), and \n                           For each security category in the target label, there shall be a security category of the same type in the initiator's clearance and the initiator's classification level shall dominate that of the target.\n                        \n                        \n                           Examples: SecurityObservationType  security label fields include:\r\n\n                        \n                           Confidentiality classification\n                           Compartment category\n                           Sensitivity category\n                           Security mechanisms used to ensure data integrity or to perform authorized data transformation\n                           Indicators of an IT resource completeness, veracity, reliability, trustworthiness, or provenance.\n                        \n                        \n                           Usage Note: SecurityObservationType codes designate security label field types, which are valued with an applicable SecurityObservationValue code as the \"security label tag\".";
            case SECCATOBS: return "Type of security metadata observation made about the category of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions. Security category metadata is defined by ISO/IEC 2382-8:1998(E/F)/ T-REC-X.812-1995 as: \"A nonhierarchical grouping of sensitive information used to control access to data more finely than with hierarchical security classification alone.\"\r\n\n                        \n                           Rationale: A security category observation supports requirement to specify the type of IT resource to facilitate application of appropriate levels of information security according to a range of levels of impact or consequences that might result from the unauthorized disclosure, modification, or use of the information or information system.  A resource is assigned to a specific category of information (e.g., privacy, medical, proprietary, financial, investigative, contractor sensitive, security management) defined by an organization or in some instances, by a specific law, Executive Order, directive, policy, or regulation. [FIPS 199]\r\n\n                        \n                           Examples: Types of security categories include:\r\n\n                        \n                           Compartment:  A division of data into isolated blocks with separate security controls for the purpose of reducing risk. (ISO 2382-8).  A security label tag that \"segments\" an IT resource by indicating that access and use is restricted to members of a defined community or project. (HL7 Healthcare Classification System)  \n                           Sensitivity:  The characteristic of an IT resource which implies its value or importance and may include its vulnerability. (ISO 7492-2)  Privacy metadata for information perceived as undesirable to share.  (HL7 Healthcare Classification System)";
            case SECCLASSOBS: return "Type of security metadata observation made about the classification of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.  Security classification is defined by ISO/IEC 2382-8:1998(E/F)/ T-REC-X.812-1995 as: \"The determination of which specific degree of protection against access the data or information requires, together with a designation of that degree of protection.\"  Security classification metadata is based on an analysis of applicable policies and the risk of financial, reputational, or other harm that could result from unauthorized disclosure.\r\n\n                        \n                           Rationale: A security classification observation may indicate that the confidentiality level indicated by an Act or Role confidentiality attribute has been overridden by the entity responsible for ascribing the SecurityClassificationObservationValue.  This supports the business requirement for increasing or decreasing the level of confidentiality (classification or declassification) based on parameters beyond the original assignment of an Act or Role confidentiality.\r\n\n                        \n                           Examples: Types of security classification include: HL7 Confidentiality Codes such as very restricted, unrestricted, and normal.  Intelligence community examples include top secret, secret, and confidential.\r\n\n                        \n                           Usage Note: Security classification observation type codes designate security label field types, which are valued with an applicable SecurityClassificationObservationValue code as the \"security label tag\".";
            case SECCONOBS: return "Type of security metadata observation made about the control of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.  Security control metadata convey instructions to users and receivers for secure distribution, transmission, and storage; dictate obligations or mandated actions; specify any action prohibited by refrain policy such as dissemination controls; and stipulate the permissible purpose of use of an IT resource.  \r\n\n                        \n                           Rationale: A security control observation supports requirement to specify applicable management, operational, and technical controls (i.e., safeguards or countermeasures) prescribed for an information system to protect the confidentiality, integrity, and availability of the system and its information. [FIPS 199]\r\n\n                        \n                           Examples: Types of security control metadata include: \r\n\n                        \n                           handling caveats\n                           dissemination controls\n                           obligations\n                           refrain policies\n                           purpose of use constraints";
            case SECINTOBS: return "Type of security metadata observation made about the integrity of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.\r\n\n                        \n                           Rationale: A security integrity observation supports the requirement to guard against improper information modification or destruction, and includes ensuring information non-repudiation and authenticity. (44 U.S.C., SEC. 3542)\r\n\n                        \n                           Examples: Types of security integrity metadata include: \r\n\n                        \n                           Integrity status, which indicates the completeness or workflow status of an IT resource (data, information object, service, or system capability)\n                           Integrity confidence, which indicates the reliability and trustworthiness of an IT resource\n                           Integrity control, which indicates pertinent handling caveats, obligations, refrain policies, and purpose of use for  the resource\n                           Data integrity, which indicate the security mechanisms used to ensure that the accuracy and consistency are preserved regardless of changes made (ISO/IEC DIS 2382-8)\n                           Alteration integrity, which indicate the security mechanisms used for authorized transformations of the resource\n                           Integrity provenance, which indicates the entity responsible for a report or assertion relayed \"second-hand\" about an IT resource";
            case SECALTINTOBS: return "Type of security metadata observation made about the alteration integrity of an IT resource (data, information object, service, or system capability), which indicates the mechanism used for authorized transformations of the resource.\r\n\n                        \n                           Examples: Types of security alteration integrity observation metadata, which may value the observation with a code used to indicate the mechanism used for authorized transformation of an IT resource, including: \r\n\n                        \n                           translation\n                           syntactic transformation\n                           semantic mapping\n                           redaction\n                           masking\n                           pseudonymization\n                           anonymization";
            case SECDATINTOBS: return "Type of security metadata observation made about the data integrity of an IT resource (data, information object, service, or system capability), which indicates the security mechanism used to preserve resource accuracy and consistency.  Data integrity is defined by ISO 22600-23.3.21 as: \"The property that data has not been altered or destroyed in an unauthorized manner\", and by ISO/IEC 2382-8:  The property of data whose accuracy and consistency are preserved regardless of changes made.\"\r\n\n                        \n                           Examples: Types of security data integrity observation metadata, which may value the observation, include cryptographic hash function and digital signature.";
            case SECINTCONOBS: return "Type of security metadata observation made about the integrity confidence of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.\r\n\n                        \n                           Examples: Types of security integrity confidence observation metadata, which may value the observation, include highly reliable, uncertain reliability, and not reliable.\r\n\n                        \n                           Usage Note: A security integrity confidence observation on an Act may indicate that a valued Act.uncertaintycode attribute has been overridden by the entity responsible for ascribing the SecurityIntegrityConfidenceObservationValue.  This supports the business requirements for increasing or decreasing the assessment of the reliability or trustworthiness of an IT resource based on parameters beyond the original assignment of an Act statement level of uncertainty.";
            case SECINTPRVOBS: return "Type of security metadata observation made about the provenance integrity of an IT resource (data, information object, service, or system capability), which indicates the lifecycle completeness of an IT resource in terms of workflow status such as its creation, modification, suspension, and deletion; locations in which the resource has been collected or archived, from which it may be retrieved, and the history of its distribution and disclosure.  Integrity provenance metadata about an IT resource may be used to assess its veracity, reliability, and trustworthiness.\r\n\n                        \n                           Examples: Types of security integrity provenance observation metadata, which may value the observation about an IT resource, include: \r\n\n                        \n                           completeness or workflow status, such as authentication\n                           the entity responsible for original authoring or informing about an IT resource\n                           the entity responsible for a report or assertion about an IT resource relayed â€œsecond-handâ€?\n                           the entity responsible for excerpting, transforming, or compiling an IT resource";
            case SECINTPRVABOBS: return "Type of security metadata observation made about the integrity provenance of an IT resource (data, information object, service, or system capability), which indicates the entity that made assertions about the resource.  The asserting entity may not be the original informant about the resource.\r\n\n                        \n                           Examples: Types of security integrity provenance asserted by observation metadata, which may value the observation, including: \r\n\n                        \n                           assertions about an IT resource by a patient\n                           assertions about an IT resource by a clinician\n                           assertions about an IT resource by a device";
            case SECINTPRVRBOBS: return "Type of security metadata observation made about the integrity provenance of an IT resource (data, information object, service, or system capability), which indicates the entity that reported the existence of the resource.  The reporting entity may not be the original author of the resource.\r\n\n                        \n                           Examples: Types of security integrity provenance reported by observation metadata, which may value the observation, include: \r\n\n                        \n                           reports about an IT resource by a patient\n                           reports about an IT resource by a clinician\n                           reports about an IT resource by a device";
            case SECINTSTOBS: return "Type of security metadata observation made about the integrity status of an IT resource (data, information object, service, or system capability), which may be used to make access control decisions.  Indicates the completeness of an IT resource in terms of workflow status, which may impact users that are authorized to access and use the resource.\r\n\n                        \n                           Examples: Types of security integrity status observation metadata, which may value the observation, include codes from the HL7 DocumentCompletion code system such as legally authenticated, in progress, and incomplete.";
            case SECTRSTOBS: return "An observation identifying trust metadata about an IT resource (data, information object, service, or system capability), which may be used as a trust attribute to populate a computable trust policy, trust credential, trust assertion, or trust label field in a security label or trust policy, which are principally used for authentication, authorization, and access control decisions.";
            case TRSTACCRDOBS: return "Type of security metadata observation made about the formal declaration by an authority or neutral third party that validates the technical, security, trust, and business practice conformance of Trust Agents to facilitate security, interoperability, and trust among participants within a security domain or trust framework.";
            case TRSTAGREOBS: return "Type of security metadata observation made about privacy and security requirements with which a security domain must comply. [ISO IEC 10181-1]";
            case TRSTCERTOBS: return "Type of security metadata observation made about a set of security-relevant data issued by a security authority or trusted third party, together with security information which is used to provide the integrity and data origin authentication services for an IT resource (data, information object, service, or system capability). [Based on ISO IEC 10181-1]\r\n\n                        \n                           For example,\n                        \r\n\n                        \n                           A Certificate Policy (CP), which is a named set of rules that indicates the applicability of a certificate to a particular community and/or class of application with common security requirements. For example, a particular Certificate Policy might indicate the applicability of a type of certificate to the authentication of electronic data interchange transactions for the trading of goods within a given price range. [Trust Service Principles and Criteria for Certification Authorities Version 2.0 March 2011 Copyright 2011 by Canadian Institute of Chartered Accountants.\n                           A Certificate Practice Statement (CSP), which is a statement of the practices which an Authority employs in issuing and managing certificates. [Trust Service Principles and Criteria for Certification Authorities Version 2.0 March 2011 Copyright 2011 by Canadian Institute of Chartered Accountants.]";
            case TRSTFWKOBS: return "Type of security metadata observation made about a complete set of contracts, regulations or commitments that enable participating actors to rely on certain assertions by other actors to fulfill their information security requirements. [Kantara Initiative]";
            case TRSTLOAOBS: return "Type of security metadata observation made about the digital quality or reliability of a trust assertion, activity, capability, information exchange, mechanism, process, or protocol.";
            case TRSTMECOBS: return "Type of security metadata observation made about a security architecture system component that supports enforcement of security policies.";
            case SUBSIDFFS: return "Definition: A government health program that provides coverage on a fee for service basis for health services to persons meeting eligibility criteria such as income, location of residence, access to other coverages, health condition, and age, the cost of which is to some extent subsidized by public funds.\r\n\n                        \n                           Discussion: The structure and business processes for underwriting and administering a subsidized fee for service program is further specified by the Underwriter and Payer Role.class and Role.code.";
            case WRKCOMP: return "Definition: Government mandated program providing coverage, disability income, and vocational rehabilitation for injuries sustained in the work place or in the course of employment.  Employers may either self-fund the program, purchase commercial coverage, or pay a premium to a government entity that administers the program.  Employees may be required to pay premiums toward the cost of coverage as well.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ACTACCOUNTCODE: return "ActAccountCode";
            case ACCTRECEIVABLE: return "account receivable";
            case CASH: return "Cash";
            case CC: return "credit card";
            case AE: return "American Express";
            case DN: return "Diner's Club";
            case DV: return "Discover Card";
            case MC: return "Master Card";
            case V: return "Visa";
            case PBILLACCT: return "patient billing account";
            case _ACTADJUDICATIONCODE: return "ActAdjudicationCode";
            case _ACTADJUDICATIONGROUPCODE: return "ActAdjudicationGroupCode";
            case CONT: return "contract";
            case DAY: return "day";
            case LOC: return "location";
            case MONTH: return "month";
            case PERIOD: return "period";
            case PROV: return "provider";
            case WEEK: return "week";
            case YEAR: return "year";
            case AA: return "adjudicated with adjustments";
            case ANF: return "adjudicated with adjustments and no financial impact";
            case AR: return "adjudicated as refused";
            case AS: return "adjudicated as submitted";
            case _ACTADJUDICATIONRESULTACTIONCODE: return "ActAdjudicationResultActionCode";
            case DISPLAY: return "Display";
            case FORM: return "Print on Form";
            case _ACTBILLABLEMODIFIERCODE: return "ActBillableModifierCode";
            case CPTM: return "CPT modifier codes";
            case HCPCSA: return "HCPCS Level II and Carrier-assigned";
            case _ACTBILLINGARRANGEMENTCODE: return "ActBillingArrangementCode";
            case BLK: return "block funding";
            case CAP: return "capitation funding";
            case CONTF: return "contract funding";
            case FINBILL: return "financial";
            case ROST: return "roster funding";
            case SESS: return "sessional funding";
            case _ACTBOUNDEDROICODE: return "ActBoundedROICode";
            case ROIFS: return "fully specified ROI";
            case ROIPS: return "partially specified ROI";
            case _ACTCAREPROVISIONCODE: return "act care provision";
            case _ACTCREDENTIALEDCARECODE: return "act credentialed care";
            case _ACTCREDENTIALEDCAREPROVISIONPERSONCODE: return "act credentialed care provision peron";
            case CACC: return "certified anatomic pathology and clinical pathology care";
            case CAIC: return "certified allergy and immunology care";
            case CAMC: return "certified aerospace medicine care";
            case CANC: return "certified anesthesiology care";
            case CAPC: return "certified anatomic pathology care";
            case CBGC: return "certified clinical biochemical genetics care";
            case CCCC: return "certified clinical cytogenetics care";
            case CCGC: return "certified clinical genetics (M.D.) care";
            case CCPC: return "certified clinical pathology care";
            case CCSC: return "certified colon and rectal surgery care";
            case CDEC: return "certified dermatology care";
            case CDRC: return "certified diagnostic radiology care";
            case CEMC: return "certified emergency medicine care";
            case CFPC: return "certified family practice care";
            case CIMC: return "certified internal medicine care";
            case CMGC: return "certified clinical molecular genetics care";
            case CNEC: return "certified neurology care";
            case CNMC: return "certified nuclear medicine care";
            case CNQC: return "certified neurology with special qualifications in child neurology care";
            case CNSC: return "certified neurological surgery care";
            case COGC: return "certified obstetrics and gynecology care";
            case COMC: return "certified occupational medicine care";
            case COPC: return "certified ophthalmology care";
            case COSC: return "certified orthopaedic surgery care";
            case COTC: return "certified otolaryngology care";
            case CPEC: return "certified pediatrics care";
            case CPGC: return "certified Ph.D. medical genetics care";
            case CPHC: return "certified public health and general preventive medicine care";
            case CPRC: return "certified physical medicine and rehabilitation care";
            case CPSC: return "certified plastic surgery care";
            case CPYC: return "certified psychiatry care";
            case CROC: return "certified radiation oncology care";
            case CRPC: return "certified radiological physics care";
            case CSUC: return "certified surgery care";
            case CTSC: return "certified thoracic surgery care";
            case CURC: return "certified urology care";
            case CVSC: return "certified vascular surgery care";
            case LGPC: return "licensed general physician care";
            case _ACTCREDENTIALEDCAREPROVISIONPROGRAMCODE: return "act credentialed care provision program";
            case AALC: return "accredited assisted living care";
            case AAMC: return "accredited ambulatory care";
            case ABHC: return "accredited behavioral health care";
            case ACAC: return "accredited critical access hospital care";
            case ACHC: return "accredited hospital care";
            case AHOC: return "accredited home care";
            case ALTC: return "accredited long term care";
            case AOSC: return "accredited office-based surgery care";
            case CACS: return "certified acute coronary syndrome care";
            case CAMI: return "certified acute myocardial infarction care";
            case CAST: return "certified asthma care";
            case CBAR: return "certified bariatric surgery care";
            case CCAD: return "certified coronary artery disease care";
            case CCAR: return "certified cardiac care";
            case CDEP: return "certified depression care";
            case CDGD: return "certified digestive/gastrointestinal disorders care";
            case CDIA: return "certified diabetes care";
            case CEPI: return "certified epilepsy care";
            case CFEL: return "certified frail elderly care";
            case CHFC: return "certified heart failure care";
            case CHRO: return "certified high risk obstetrics care";
            case CHYP: return "certified hyperlipidemia care";
            case CMIH: return "certified migraine headache care";
            case CMSC: return "certified multiple sclerosis care";
            case COJR: return "certified orthopedic joint replacement care";
            case CONC: return "certified oncology care";
            case COPD: return "certified chronic obstructive pulmonary disease care";
            case CORT: return "certified organ transplant care";
            case CPAD: return "certified parkinsons disease care";
            case CPND: return "certified pneumonia disease care";
            case CPST: return "certified primary stroke center care";
            case CSDM: return "certified stroke disease management care";
            case CSIC: return "certified sickle cell care";
            case CSLD: return "certified sleep disorders care";
            case CSPT: return "certified spine treatment care";
            case CTBU: return "certified trauma/burn center care";
            case CVDC: return "certified vascular diseases care";
            case CWMA: return "certified wound management care";
            case CWOH: return "certified women's health care";
            case _ACTENCOUNTERCODE: return "ActEncounterCode";
            case AMB: return "ambulatory";
            case EMER: return "emergency";
            case FLD: return "field";
            case HH: return "home health";
            case IMP: return "inpatient encounter";
            case ACUTE: return "inpatient acute";
            case NONAC: return "inpatient non-acute";
            case PRENC: return "pre-admission";
            case SS: return "short stay";
            case VR: return "virtual";
            case _ACTMEDICALSERVICECODE: return "ActMedicalServiceCode";
            case ALC: return "Alternative Level of Care";
            case CARD: return "Cardiology";
            case CHR: return "Chronic";
            case DNTL: return "Dental";
            case DRGRHB: return "Drug Rehab";
            case GENRL: return "General";
            case MED: return "Medical";
            case OBS: return "Obstetrics";
            case ONC: return "Oncology";
            case PALL: return "Palliative";
            case PED: return "Pediatrics";
            case PHAR: return "Pharmaceutical";
            case PHYRHB: return "Physical Rehab";
            case PSYCH: return "Psychiatric";
            case SURG: return "Surgical";
            case _ACTCLAIMATTACHMENTCATEGORYCODE: return "ActClaimAttachmentCategoryCode";
            case AUTOATTCH: return "auto attachment";
            case DOCUMENT: return "document";
            case HEALTHREC: return "health record";
            case IMG: return "image attachment";
            case LABRESULTS: return "lab results";
            case MODEL: return "model";
            case WIATTCH: return "work injury report attachment";
            case XRAY: return "x-ray";
            case _ACTCONSENTTYPE: return "ActConsentType";
            case ICOL: return "information collection";
            case IDSCL: return "information disclosure";
            case INFA: return "information access";
            case INFAO: return "access only";
            case INFASO: return "access and save only";
            case IRDSCL: return "information redisclosure";
            case RESEARCH: return "research information access";
            case RSDID: return "de-identified information access";
            case RSREID: return "re-identifiable information access";
            case _ACTCONTAINERREGISTRATIONCODE: return "ActContainerRegistrationCode";
            case ID: return "Identified";
            case IP: return "In Position";
            case L: return "Left Equipment";
            case M: return "Missing";
            case O: return "In Process";
            case R: return "Process Completed";
            case X: return "Container Unavailable";
            case _ACTCONTROLVARIABLE: return "ActControlVariable";
            case AUTO: return "auto-repeat permission";
            case ENDC: return "endogenous content";
            case REFLEX: return "reflex permission";
            case _ACTCOVERAGECONFIRMATIONCODE: return "ActCoverageConfirmationCode";
            case _ACTCOVERAGEAUTHORIZATIONCONFIRMATIONCODE: return "ActCoverageAuthorizationConfirmationCode";
            case AUTH: return "Authorized";
            case NAUTH: return "Not Authorized";
            case _ACTCOVERAGELIMITCODE: return "ActCoverageLimitCode";
            case _ACTCOVERAGEQUANTITYLIMITCODE: return "ActCoverageQuantityLimitCode";
            case COVPRD: return "coverage period";
            case LFEMX: return "life time maximum";
            case NETAMT: return "Net Amount";
            case PRDMX: return "period maximum";
            case UNITPRICE: return "Unit Price";
            case UNITQTY: return "Unit Quantity";
            case COVMX: return "coverage maximum";
            case _ACTCOVERAGETYPECODE: return "ActCoverageTypeCode";
            case _ACTINSURANCEPOLICYCODE: return "ActInsurancePolicyCode";
            case EHCPOL: return "extended healthcare";
            case HSAPOL: return "health spending account";
            case AUTOPOL: return "automobile";
            case COL: return "collision coverage policy";
            case UNINSMOT: return "uninsured motorist policy";
            case PUBLICPOL: return "public healthcare";
            case DENTPRG: return "dental program";
            case DISEASEPRG: return "public health program";
            case CANPRG: return "women's cancer detection program";
            case ENDRENAL: return "end renal program";
            case HIVAIDS: return "HIV-AIDS program";
            case MANDPOL: return "mandatory health program";
            case MENTPRG: return "mental health program";
            case SAFNET: return "safety net clinic program";
            case SUBPRG: return "substance use program";
            case SUBSIDIZ: return "subsidized health program";
            case SUBSIDMC: return "subsidized managed care program";
            case SUBSUPP: return "subsidized supplemental health program";
            case WCBPOL: return "worker's compensation";
            case _ACTINSURANCETYPECODE: return "ActInsuranceTypeCode";
            case _ACTHEALTHINSURANCETYPECODE: return "ActHealthInsuranceTypeCode";
            case DENTAL: return "dental care policy";
            case DISEASE: return "disease specific policy";
            case DRUGPOL: return "drug policy";
            case HIP: return "health insurance plan policy";
            case LTC: return "long term care policy";
            case MCPOL: return "managed care policy";
            case POS: return "point of service policy";
            case HMO: return "health maintenance organization policy";
            case PPO: return "preferred provider organization policy";
            case MENTPOL: return "mental health policy";
            case SUBPOL: return "substance use policy";
            case VISPOL: return "vision care policy";
            case DIS: return "disability insurance policy";
            case EWB: return "employee welfare benefit plan policy";
            case FLEXP: return "flexible benefit plan policy";
            case LIFE: return "life insurance policy";
            case ANNU: return "annuity policy";
            case TLIFE: return "term life insurance policy";
            case ULIFE: return "universal life insurance policy";
            case PNC: return "property and casualty insurance policy";
            case REI: return "reinsurance policy";
            case SURPL: return "surplus line insurance policy";
            case UMBRL: return "umbrella liability insurance policy";
            case _ACTPROGRAMTYPECODE: return "ActProgramTypeCode";
            case CHAR: return "charity program";
            case CRIME: return "crime victim program";
            case EAP: return "employee assistance program";
            case GOVEMP: return "government employee health program";
            case HIRISK: return "high risk pool program";
            case IND: return "indigenous peoples health program";
            case MILITARY: return "military health program";
            case RETIRE: return "retiree health program";
            case SOCIAL: return "social service program";
            case VET: return "veteran health program";
            case _ACTDETECTEDISSUEMANAGEMENTCODE: return "ActDetectedIssueManagementCode";
            case _ACTADMINISTRATIVEDETECTEDISSUEMANAGEMENTCODE: return "ActAdministrativeDetectedIssueManagementCode";
            case _AUTHORIZATIONISSUEMANAGEMENTCODE: return "Authorization Issue Management Code";
            case EMAUTH: return "emergency authorization override";
            case _21: return "authorization confirmed";
            case _1: return "Therapy Appropriate";
            case _19: return "Consulted Supplier";
            case _2: return "Assessed Patient";
            case _22: return "appropriate indication or diagnosis";
            case _23: return "prior therapy documented";
            case _3: return "Patient Explanation";
            case _4: return "Consulted Other Source";
            case _5: return "Consulted Prescriber";
            case _6: return "Prescriber Declined Change";
            case _7: return "Interacting Therapy No Longer Active/Planned";
            case _14: return "Supply Appropriate";
            case _15: return "Replacement";
            case _16: return "Vacation Supply";
            case _17: return "Weekend Supply";
            case _18: return "Leave of Absence";
            case _20: return "additional quantity on separate dispense";
            case _8: return "Other Action Taken";
            case _10: return "Provided Patient Education";
            case _11: return "Added Concurrent Therapy";
            case _12: return "Temporarily Suspended Concurrent Therapy";
            case _13: return "Stopped Concurrent Therapy";
            case _9: return "Instituted Ongoing Monitoring Program";
            case _ACTEXPOSURECODE: return "ActExposureCode";
            case CHLDCARE: return "Day care - Child care Interaction";
            case CONVEYNC: return "Common Conveyance Interaction";
            case HLTHCARE: return "Health Care Interaction - Not Patient Care";
            case HOMECARE: return "Care Giver Interaction";
            case HOSPPTNT: return "Hospital Patient Interaction";
            case HOSPVSTR: return "Hospital Visitor Interaction";
            case HOUSEHLD: return "Household Interaction";
            case INMATE: return "Inmate Interaction";
            case INTIMATE: return "Intimate Interaction";
            case LTRMCARE: return "Long Term Care Facility Interaction";
            case PLACE: return "Common Space Interaction";
            case PTNTCARE: return "Health Care Interaction - Patient Care";
            case SCHOOL2: return "School Interaction";
            case SOCIAL2: return "Social/Extended Family Interaction";
            case SUBSTNCE: return "Common Substance Interaction";
            case TRAVINT: return "Common Travel Interaction";
            case WORK2: return "Work Interaction";
            case _ACTFINANCIALTRANSACTIONCODE: return "ActFinancialTransactionCode";
            case CHRG: return "Standard Charge";
            case REV: return "Standard Charge Reversal";
            case _ACTINCIDENTCODE: return "ActIncidentCode";
            case MVA: return "Motor vehicle accident";
            case SCHOOL: return "School Accident";
            case SPT: return "Sporting Accident";
            case WPA: return "Workplace accident";
            case _ACTINFORMATIONACCESSCODE: return "ActInformationAccessCode";
            case ACADR: return "adverse drug reaction access";
            case ACALL: return "all access";
            case ACALLG: return "allergy access";
            case ACCONS: return "informational consent access";
            case ACDEMO: return "demographics access";
            case ACDI: return "diagnostic imaging access";
            case ACIMMUN: return "immunization access";
            case ACLAB: return "lab test result access";
            case ACMED: return "medication access";
            case ACMEDC: return "medical condition access";
            case ACMEN: return "mental health access";
            case ACOBS: return "common observations access";
            case ACPOLPRG: return "policy or program information access";
            case ACPROV: return "provider information access";
            case ACPSERV: return "professional service access";
            case ACSUBSTAB: return "substance abuse access";
            case _ACTINFORMATIONACCESSCONTEXTCODE: return "ActInformationAccessContextCode";
            case INFAUT: return "authorized information transfer";
            case INFCON: return "after explicit consent";
            case INFCRT: return "only on court order";
            case INFDNG: return "only if danger to others";
            case INFEMER: return "only in an emergency";
            case INFPWR: return "only if public welfare risk";
            case INFREG: return "regulatory information transfer";
            case _ACTINFORMATIONCATEGORYCODE: return "ActInformationCategoryCode";
            case ALLCAT: return "all categories";
            case ALLGCAT: return "allergy category";
            case ARCAT: return "adverse drug reaction category";
            case COBSCAT: return "common observation category";
            case DEMOCAT: return "demographics category";
            case DICAT: return "diagnostic image category";
            case IMMUCAT: return "immunization category";
            case LABCAT: return "lab test category";
            case MEDCCAT: return "medical condition category";
            case MENCAT: return "mental health category";
            case PSVCCAT: return "professional service category";
            case RXCAT: return "medication category";
            case _ACTINVOICEELEMENTCODE: return "ActInvoiceElementCode";
            case _ACTINVOICEADJUDICATIONPAYMENTCODE: return "ActInvoiceAdjudicationPaymentCode";
            case _ACTINVOICEADJUDICATIONPAYMENTGROUPCODE: return "ActInvoiceAdjudicationPaymentGroupCode";
            case ALEC: return "alternate electronic";
            case BONUS: return "bonus";
            case CFWD: return "carry forward adjusment";
            case EDU: return "education fees";
            case EPYMT: return "early payment fee";
            case GARN: return "garnishee";
            case INVOICE: return "submitted invoice";
            case PINV: return "paper invoice";
            case PPRD: return "prior period adjustment";
            case PROA: return "professional association deduction";
            case RECOV: return "recovery";
            case RETRO: return "retro adjustment";
            case TRAN: return "transaction fee";
            case _ACTINVOICEADJUDICATIONPAYMENTSUMMARYCODE: return "ActInvoiceAdjudicationPaymentSummaryCode";
            case INVTYPE: return "invoice type";
            case PAYEE: return "payee";
            case PAYOR: return "payor";
            case SENDAPP: return "sending application";
            case _ACTINVOICEDETAILCODE: return "ActInvoiceDetailCode";
            case _ACTINVOICEDETAILCLINICALPRODUCTCODE: return "ActInvoiceDetailClinicalProductCode";
            case UNSPSC: return "United Nations Standard Products and Services Classification";
            case _ACTINVOICEDETAILDRUGPRODUCTCODE: return "ActInvoiceDetailDrugProductCode";
            case GTIN: return "Global Trade Item Number";
            case UPC: return "Universal Product Code";
            case _ACTINVOICEDETAILGENERICCODE: return "ActInvoiceDetailGenericCode";
            case _ACTINVOICEDETAILGENERICADJUDICATORCODE: return "ActInvoiceDetailGenericAdjudicatorCode";
            case COIN: return "coinsurance";
            case COPAYMENT: return "patient co-pay";
            case DEDUCTIBLE: return "deductible";
            case PAY: return "payment";
            case SPEND: return "spend down";
            case _ACTINVOICEDETAILGENERICMODIFIERCODE: return "ActInvoiceDetailGenericModifierCode";
            case AFTHRS: return "non-normal hours";
            case ISOL: return "isolation allowance";
            case OOO: return "out of office";
            case _ACTINVOICEDETAILGENERICPROVIDERCODE: return "ActInvoiceDetailGenericProviderCode";
            case CANCAPT: return "cancelled appointment";
            case DSC: return "discount";
            case ESA: return "extraordinary service assessment";
            case FFSTOP: return "fee for service top off";
            case FNLFEE: return "final fee";
            case FRSTFEE: return "first fee";
            case MARKUP: return "markup or up-charge";
            case MISSAPT: return "missed appointment";
            case PERFEE: return "periodic fee";
            case PERMBNS: return "performance bonus";
            case RESTOCK: return "restocking fee";
            case TRAVEL: return "travel";
            case URGENT: return "urgent";
            case _ACTINVOICEDETAILTAXCODE: return "ActInvoiceDetailTaxCode";
            case FST: return "federal sales tax";
            case HST: return "harmonized sales Tax";
            case PST: return "provincial/state sales tax";
            case _ACTINVOICEDETAILPREFERREDACCOMMODATIONCODE: return "ActInvoiceDetailPreferredAccommodationCode";
            case _ACTENCOUNTERACCOMMODATIONCODE: return "ActEncounterAccommodationCode";
            case _HL7ACCOMMODATIONCODE: return "HL7AccommodationCode";
            case I: return "Isolation";
            case P: return "Private";
            case S: return "Suite";
            case SP: return "Semi-private";
            case W: return "Ward";
            case _ACTINVOICEGROUPCODE: return "ActInvoiceGroupCode";
            case _ACTINVOICEINTERGROUPCODE: return "ActInvoiceInterGroupCode";
            case CPNDDRGING: return "compound drug invoice group";
            case CPNDINDING: return "compound ingredient invoice group";
            case CPNDSUPING: return "compound supply invoice group";
            case DRUGING: return "drug invoice group";
            case FRAMEING: return "frame invoice group";
            case LENSING: return "lens invoice group";
            case PRDING: return "product invoice group";
            case _ACTINVOICEROOTGROUPCODE: return "ActInvoiceRootGroupCode";
            case CPINV: return "clinical product invoice";
            case CSINV: return "clinical service invoice";
            case CSPINV: return "clinical service and product";
            case FININV: return "financial invoice";
            case OHSINV: return "oral health service";
            case PAINV: return "preferred accommodation invoice";
            case RXCINV: return "Rx compound invoice";
            case RXDINV: return "Rx dispense invoice";
            case SBFINV: return "sessional or block fee invoice";
            case VRXINV: return "vision dispense invoice";
            case _ACTINVOICEELEMENTSUMMARYCODE: return "ActInvoiceElementSummaryCode";
            case _INVOICEELEMENTADJUDICATED: return "InvoiceElementAdjudicated";
            case ADNFPPELAT: return "adjud. nullified prior-period electronic amount";
            case ADNFPPELCT: return "adjud. nullified prior-period electronic count";
            case ADNFPPMNAT: return "adjud. nullified prior-period manual amount";
            case ADNFPPMNCT: return "adjud. nullified prior-period manual count";
            case ADNFSPELAT: return "adjud. nullified same-period electronic amount";
            case ADNFSPELCT: return "adjud. nullified same-period electronic count";
            case ADNFSPMNAT: return "adjud. nullified same-period manual amount";
            case ADNFSPMNCT: return "adjud. nullified same-period manual count";
            case ADNPPPELAT: return "adjud. non-payee payable prior-period electronic amount";
            case ADNPPPELCT: return "adjud. non-payee payable prior-period electronic count";
            case ADNPPPMNAT: return "adjud. non-payee payable prior-period manual amount";
            case ADNPPPMNCT: return "adjud. non-payee payable prior-period manual count";
            case ADNPSPELAT: return "adjud. non-payee payable same-period electronic amount";
            case ADNPSPELCT: return "adjud. non-payee payable same-period electronic count";
            case ADNPSPMNAT: return "adjud. non-payee payable same-period manual amount";
            case ADNPSPMNCT: return "adjud. non-payee payable same-period manual count";
            case ADPPPPELAT: return "adjud. payee payable prior-period electronic amount";
            case ADPPPPELCT: return "adjud. payee payable prior-period electronic count";
            case ADPPPPMNAT: return "adjud. payee payable prior-period manual amout";
            case ADPPPPMNCT: return "adjud. payee payable prior-period manual count";
            case ADPPSPELAT: return "adjud. payee payable same-period electronic amount";
            case ADPPSPELCT: return "adjud. payee payable same-period electronic count";
            case ADPPSPMNAT: return "adjud. payee payable same-period manual amount";
            case ADPPSPMNCT: return "adjud. payee payable same-period manual count";
            case ADRFPPELAT: return "adjud. refused prior-period electronic amount";
            case ADRFPPELCT: return "adjud. refused prior-period electronic count";
            case ADRFPPMNAT: return "adjud. refused prior-period manual amount";
            case ADRFPPMNCT: return "adjud. refused prior-period manual count";
            case ADRFSPELAT: return "adjud. refused same-period electronic amount";
            case ADRFSPELCT: return "adjud. refused same-period electronic count";
            case ADRFSPMNAT: return "adjud. refused same-period manual amount";
            case ADRFSPMNCT: return "adjud. refused same-period manual count";
            case _INVOICEELEMENTPAID: return "InvoiceElementPaid";
            case PDNFPPELAT: return "paid nullified prior-period electronic amount";
            case PDNFPPELCT: return "paid nullified prior-period electronic count";
            case PDNFPPMNAT: return "paid nullified prior-period manual amount";
            case PDNFPPMNCT: return "paid nullified prior-period manual count";
            case PDNFSPELAT: return "paid nullified same-period electronic amount";
            case PDNFSPELCT: return "paid nullified same-period electronic count";
            case PDNFSPMNAT: return "paid nullified same-period manual amount";
            case PDNFSPMNCT: return "paid nullified same-period manual count";
            case PDNPPPELAT: return "paid non-payee payable prior-period electronic amount";
            case PDNPPPELCT: return "paid non-payee payable prior-period electronic count";
            case PDNPPPMNAT: return "paid non-payee payable prior-period manual amount";
            case PDNPPPMNCT: return "paid non-payee payable prior-period manual count";
            case PDNPSPELAT: return "paid non-payee payable same-period electronic amount";
            case PDNPSPELCT: return "paid non-payee payable same-period electronic count";
            case PDNPSPMNAT: return "paid non-payee payable same-period manual amount";
            case PDNPSPMNCT: return "paid non-payee payable same-period manual count";
            case PDPPPPELAT: return "paid payee payable prior-period electronic amount";
            case PDPPPPELCT: return "paid payee payable prior-period electronic count";
            case PDPPPPMNAT: return "paid payee payable prior-period manual amount";
            case PDPPPPMNCT: return "paid payee payable prior-period manual count";
            case PDPPSPELAT: return "paid payee payable same-period electronic amount";
            case PDPPSPELCT: return "paid payee payable same-period electronic count";
            case PDPPSPMNAT: return "paid payee payable same-period manual amount";
            case PDPPSPMNCT: return "paid payee payable same-period manual count";
            case _INVOICEELEMENTSUBMITTED: return "InvoiceElementSubmitted";
            case SBBLELAT: return "submitted billed electronic amount";
            case SBBLELCT: return "submitted billed electronic count";
            case SBNFELAT: return "submitted nullified electronic amount";
            case SBNFELCT: return "submitted cancelled electronic count";
            case SBPDELAT: return "submitted pending electronic amount";
            case SBPDELCT: return "submitted pending electronic count";
            case _ACTINVOICEOVERRIDECODE: return "ActInvoiceOverrideCode";
            case COVGE: return "coverage problem";
            case EFORM: return "electronic form to follow";
            case FAX: return "fax to follow";
            case GFTH: return "good faith indicator";
            case LATE: return "late invoice";
            case MANUAL: return "manual review";
            case OOJ: return "out of jurisdiction";
            case ORTHO: return "orthodontic service";
            case PAPER: return "paper documentation to follow";
            case PIE: return "public insurance exhausted";
            case PYRDELAY: return "delayed by a previous payor";
            case REFNR: return "referral not required";
            case REPSERV: return "repeated service";
            case UNRELAT: return "unrelated service";
            case VERBAUTH: return "verbal authorization";
            case _ACTLISTCODE: return "ActListCode";
            case _ACTOBSERVATIONLIST: return "ActObservationList";
            case CARELIST: return "care plan";
            case CONDLIST: return "condition list";
            case INTOLIST: return "intolerance list";
            case PROBLIST: return "problem list";
            case RISKLIST: return "risk factors";
            case GOALLIST: return "goal list";
            case _ACTTHERAPYDURATIONWORKINGLISTCODE: return "ActTherapyDurationWorkingListCode";
            case _ACTMEDICATIONTHERAPYDURATIONWORKINGLISTCODE: return "act medication therapy duration working list";
            case ACU: return "short term/acute";
            case CHRON: return "continuous/chronic";
            case ONET: return "one time";
            case PRN: return "as needed";
            case MEDLIST: return "medication list";
            case CURMEDLIST: return "current medication list";
            case DISCMEDLIST: return "discharge medication list";
            case HISTMEDLIST: return "medication history";
            case _ACTMONITORINGPROTOCOLCODE: return "ActMonitoringProtocolCode";
            case CTLSUB: return "Controlled Substance";
            case INV: return "investigational";
            case LU: return "limited use";
            case OTC: return "non prescription medicine";
            case RX: return "prescription only medicine";
            case SA: return "special authorization";
            case SAC: return "special access";
            case _ACTNONOBSERVATIONINDICATIONCODE: return "ActNonObservationIndicationCode";
            case IND01: return "imaging study requiring contrast";
            case IND02: return "colonoscopy prep";
            case IND03: return "prophylaxis";
            case IND04: return "surgical prophylaxis";
            case IND05: return "pregnancy prophylaxis";
            case _ACTOBSERVATIONVERIFICATIONTYPE: return "act observation verification";
            case VFPAPER: return "verify paper";
            case _ACTPAYMENTCODE: return "ActPaymentCode";
            case ACH: return "Automated Clearing House";
            case CHK: return "Cheque";
            case DDP: return "Direct Deposit";
            case NON: return "Non-Payment Data";
            case _ACTPHARMACYSUPPLYTYPE: return "ActPharmacySupplyType";
            case DF: return "Daily Fill";
            case EM: return "Emergency Supply";
            case SO: return "Script Owing";
            case FF: return "First Fill";
            case FFC: return "First Fill - Complete";
            case FFCS: return "first fill complete, partial strength";
            case FFP: return "First Fill - Part Fill";
            case FFPS: return "first fill, part fill, partial strength";
            case FFSS: return "first fill, partial strength";
            case TFS: return "trial fill partial strength";
            case TF: return "Trial Fill";
            case FS: return "Floor stock";
            case MS: return "Manufacturer Sample";
            case RF: return "Refill";
            case UD: return "Unit Dose";
            case RFC: return "Refill - Complete";
            case RFCS: return "refill complete partial strength";
            case RFF: return "Refill (First fill this facility)";
            case RFFS: return "refill partial strength (first fill this facility)";
            case RFP: return "Refill - Part Fill";
            case RFPS: return "refill part fill partial strength";
            case RFS: return "refill partial strength";
            case TB: return "Trial Balance";
            case TBS: return "trial balance partial strength";
            case UDE: return "unit dose equivalent";
            case _ACTPOLICYTYPE: return "ActPolicyType";
            case _ACTPRIVACYPOLICY: return "ActPrivacyPolicy";
            case _ACTCONSENTDIRECTIVE: return "ActConsentDirective";
            case EMRGONLY: return "emergency only";
            case NOPP: return "notice of privacy practices";
            case OPTIN: return "opt-in";
            case OPTOUT: return "op-out";
            case _INFORMATIONSENSITIVITYPOLICY: return "InformationSensitivityPolicy";
            case _ACTINFORMATIONSENSITIVITYPOLICY: return "ActInformationSensitivityPolicy";
            case ETH: return "substance abuse information sensitivity";
            case GDIS: return "genetic disease information sensitivity";
            case HIV: return "HIV/AIDS information sensitivity";
            case PSY: return "psychiatry information sensitivity";
            case SCA: return "sickle cell anemia";
            case SDV: return "sexual assault, abuse, or domestic violence information sensitivity";
            case SEX: return "sexuality and reproductive health information sensitivity";
            case STD: return "sexually transmitted disease information sensitivity";
            case TBOO: return "taboo";
            case _ENTITYSENSITIVITYPOLICYTYPE: return "EntityInformationSensitivityPolicy";
            case DEMO: return "all demographic information sensitivity";
            case DOB: return "date of birth information sensitivity";
            case GENDER: return "gender and sexual orientation information sensitivity";
            case LIVARG: return "living arrangement information sensitivity";
            case MARST: return "marital status information sensitivity";
            case RACE: return "race information sensitivity";
            case REL: return "religion information sensitivity";
            case _ROLEINFORMATIONSENSITIVITYPOLICY: return "RoleInformationSensitivityPolicy";
            case B: return "business information sensitivity";
            case EMPL: return "employer information sensitivity";
            case LOCIS: return "location information sensitivity";
            case SSP: return "sensitive service provider information sensitivity";
            case ADOL: return "adolescent information sensitivity";
            case CEL: return "celebrity information sensitivity";
            case DIA: return "diagnosis information sensitivity";
            case DRGIS: return "drug information sensitivity";
            case EMP: return "employee information sensitivity";
            case PDS: return "patient default sensitivity";
            case PRS: return "patient requested sensitivity";
            case COMPT: return "compartment";
            case HRCOMPT: return "human resource compartment";
            case RESCOMPT: return "research project compartment";
            case RMGTCOMPT: return "records management compartment";
            case ACTTRUSTPOLICYTYPE: return "trust policy";
            case TRSTACCRD: return "trust accreditation";
            case TRSTAGRE: return "trust agreement";
            case TRSTASSUR: return "trust assurance";
            case TRSTCERT: return "trust certificate";
            case TRSTFWK: return "trust framework";
            case TRSTMEC: return "trust mechanism";
            case COVPOL: return "benefit policy";
            case SECURITYPOLICY: return "security policy";
            case OBLIGATIONPOLICY: return "obligation policy";
            case ANONY: return "anonymize";
            case AOD: return "accounting of disclosure";
            case AUDIT: return "audit";
            case AUDTR: return "audit trail";
            case CPLYCC: return "comply with confidentiality code";
            case CPLYCD: return "comply with consent directive";
            case CPLYJPP: return "comply with jurisdictional privacy policy";
            case CPLYOPP: return "comply with organizational privacy policy";
            case CPLYOSP: return "comply with organizational security policy";
            case CPLYPOL: return "comply with policy";
            case DEID: return "deidentify";
            case DELAU: return "delete after use";
            case ENCRYPT: return "encrypt";
            case ENCRYPTR: return "encrypt at rest";
            case ENCRYPTT: return "encrypt in transit";
            case ENCRYPTU: return "encrypt in use";
            case HUAPRV: return "human approval";
            case MASK: return "mask";
            case MINEC: return "minimum necessary";
            case PRIVMARK: return "privacy mark";
            case PSEUD: return "pseudonymize";
            case REDACT: return "redact";
            case REFRAINPOLICY: return "refrain policy";
            case NOAUTH: return "no disclosure without subject authorization";
            case NOCOLLECT: return "no collection";
            case NODSCLCD: return "no disclosure without consent directive";
            case NODSCLCDS: return "no disclosure without information subject's consent directive";
            case NOINTEGRATE: return "no integration";
            case NOLIST: return "no unlisted entity disclosure";
            case NOMOU: return "no disclosure without MOU";
            case NOORGPOL: return "no disclosure without organizational authorization";
            case NOPAT: return "no disclosure to patient, family or caregivers without attending provider's authorization";
            case NOPERSISTP: return "no collection beyond purpose of use";
            case NORDSCLCD: return "no redisclosure without consent directive";
            case NORDSCLCDS: return "no redisclosure without information subject's consent directive";
            case NORDSCLW: return "no disclosure without jurisdictional authorization";
            case NORELINK: return "no relinking";
            case NOREUSE: return "no reuse beyond purpose of use";
            case NOVIP: return "no unauthorized VIP disclosure";
            case ORCON: return "no disclosure without originator authorization";
            case _ACTPRODUCTACQUISITIONCODE: return "ActProductAcquisitionCode";
            case LOAN: return "Loan";
            case RENT: return "Rent";
            case TRANSFER: return "Transfer";
            case SALE: return "Sale";
            case _ACTSPECIMENTRANSPORTCODE: return "ActSpecimenTransportCode";
            case SREC: return "specimen received";
            case SSTOR: return "specimen in storage";
            case STRAN: return "specimen in transit";
            case _ACTSPECIMENTREATMENTCODE: return "ActSpecimenTreatmentCode";
            case ACID: return "Acidification";
            case ALK: return "Alkalization";
            case DEFB: return "Defibrination";
            case FILT: return "Filtration";
            case LDLP: return "LDL Precipitation";
            case NEUT: return "Neutralization";
            case RECA: return "Recalcification";
            case UFIL: return "Ultrafiltration";
            case _ACTSUBSTANCEADMINISTRATIONCODE: return "ActSubstanceAdministrationCode";
            case DRUG: return "Drug therapy";
            case FD: return "food";
            case IMMUNIZ: return "Immunization";
            case _ACTTASKCODE: return "ActTaskCode";
            case OE: return "order entry task";
            case LABOE: return "laboratory test order entry task";
            case MEDOE: return "medication order entry task";
            case PATDOC: return "patient documentation task";
            case ALLERLREV: return "allergy list review";
            case CLINNOTEE: return "clinical note entry task";
            case DIAGLISTE: return "diagnosis list entry task";
            case DISCHINSTE: return "discharge instruction entry";
            case DISCHSUME: return "discharge summary entry task";
            case PATEDUE: return "patient education entry";
            case PATREPE: return "pathology report entry task";
            case PROBLISTE: return "problem list entry task";
            case RADREPE: return "radiology report entry task";
            case IMMLREV: return "immunization list review";
            case REMLREV: return "reminder list review";
            case WELLREMLREV: return "wellness reminder list review";
            case PATINFO: return "patient information review task";
            case ALLERLE: return "allergy list entry";
            case CDSREV: return "clinical decision support intervention review";
            case CLINNOTEREV: return "clinical note review task";
            case DISCHSUMREV: return "discharge summary review task";
            case DIAGLISTREV: return "diagnosis list review task";
            case IMMLE: return "immunization list entry";
            case LABRREV: return "laboratory results review task";
            case MICRORREV: return "microbiology results review task";
            case MICROORGRREV: return "microbiology organisms results review task";
            case MICROSENSRREV: return "microbiology sensitivity test results review task";
            case MLREV: return "medication list review task";
            case MARWLREV: return "medication administration record work list review task";
            case OREV: return "orders review task";
            case PATREPREV: return "pathology report review task";
            case PROBLISTREV: return "problem list review task";
            case RADREPREV: return "radiology report review task";
            case REMLE: return "reminder list entry";
            case WELLREMLE: return "wellness reminder list entry";
            case RISKASSESS: return "risk assessment instrument task";
            case FALLRISK: return "falls risk assessment instrument task";
            case _ACTTRANSPORTATIONMODECODE: return "ActTransportationModeCode";
            case _ACTPATIENTTRANSPORTATIONMODECODE: return "ActPatientTransportationModeCode";
            case AFOOT: return "pedestrian transport";
            case AMBT: return "ambulance transport";
            case AMBAIR: return "fixed-wing ambulance transport";
            case AMBGRND: return "ground ambulance transport";
            case AMBHELO: return "helicopter ambulance transport";
            case LAWENF: return "law enforcement transport";
            case PRVTRN: return "private transport";
            case PUBTRN: return "public transport";
            case _OBSERVATIONTYPE: return "ObservationType";
            case _ACTSPECOBSCODE: return "ActSpecObsCode";
            case ARTBLD: return "ActSpecObsArtBldCode";
            case DILUTION: return "ActSpecObsDilutionCode";
            case AUTOHIGH: return "Auto-High Dilution";
            case AUTOLOW: return "Auto-Low Dilution";
            case PRE: return "Pre-Dilution";
            case RERUN: return "Rerun Dilution";
            case EVNFCTS: return "ActSpecObsEvntfctsCode";
            case INTFR: return "ActSpecObsInterferenceCode";
            case FIBRIN: return "Fibrin";
            case HEMOLYSIS: return "Hemolysis";
            case ICTERUS: return "Icterus";
            case LIPEMIA: return "Lipemia";
            case VOLUME: return "ActSpecObsVolumeCode";
            case AVAILABLE: return "Available Volume";
            case CONSUMPTION: return "Consumption Volume";
            case CURRENT: return "Current Volume";
            case INITIAL: return "Initial Volume";
            case _ANNOTATIONTYPE: return "AnnotationType";
            case _ACTPATIENTANNOTATIONTYPE: return "ActPatientAnnotationType";
            case ANNDI: return "diagnostic image note";
            case ANNGEN: return "general note";
            case ANNIMM: return "immunization note";
            case ANNLAB: return "laboratory note";
            case ANNMED: return "medication note";
            case _GENETICOBSERVATIONTYPE: return "GeneticObservationType";
            case GENE: return "gene";
            case _IMMUNIZATIONOBSERVATIONTYPE: return "ImmunizationObservationType";
            case OBSANTC: return "antigen count";
            case OBSANTV: return "antigen validity";
            case _INDIVIDUALCASESAFETYREPORTTYPE: return "Individual Case Safety Report Type";
            case PATADVEVNT: return "patient adverse event";
            case VACPROBLEM: return "vaccine product problem";
            case _LOINCOBSERVATIONACTCONTEXTAGETYPE: return "LOINCObservationActContextAgeType";
            case _216119: return "age patient qn est";
            case _216127: return "age patient qn reported";
            case _295535: return "age patient qn calc";
            case _305250: return "age patient qn definition";
            case _309724: return "age at onset of adverse event";
            case _MEDICATIONOBSERVATIONTYPE: return "MedicationObservationType";
            case REPHALFLIFE: return "representative half-life";
            case SPLCOATING: return "coating";
            case SPLCOLOR: return "color";
            case SPLIMAGE: return "image";
            case SPLIMPRINT: return "imprint";
            case SPLSCORING: return "scoring";
            case SPLSHAPE: return "shape";
            case SPLSIZE: return "size";
            case SPLSYMBOL: return "symbol";
            case _OBSERVATIONISSUETRIGGERCODEDOBSERVATIONTYPE: return "ObservationIssueTriggerCodedObservationType";
            case _CASETRANSMISSIONMODE: return "case transmission mode";
            case AIRTRNS: return "airborne transmission";
            case ANANTRNS: return "animal to animal transmission";
            case ANHUMTRNS: return "animal to human transmission";
            case BDYFLDTRNS: return "body fluid contact transmission";
            case BLDTRNS: return "blood borne transmission";
            case DERMTRNS: return "transdermal transmission";
            case ENVTRNS: return "environmental exposure transmission";
            case FECTRNS: return "fecal-oral transmission";
            case FOMTRNS: return "fomite transmission";
            case FOODTRNS: return "food-borne transmission";
            case HUMHUMTRNS: return "human to human transmission";
            case INDTRNS: return "indeterminate disease transmission mode";
            case LACTTRNS: return "lactation transmission";
            case NOSTRNS: return "nosocomial transmission";
            case PARTRNS: return "parenteral transmission";
            case PLACTRNS: return "transplacental transmission";
            case SEXTRNS: return "sexual transmission";
            case TRNSFTRNS: return "transfusion transmission";
            case VECTRNS: return "vector-borne transmission";
            case WATTRNS: return "water-borne transmission";
            case _OBSERVATIONQUALITYMEASUREATTRIBUTE: return "ObservationQualityMeasureAttribute";
            case AGGREGATE: return "aggregate measure observation";
            case COPY: return "copyright";
            case CRS: return "clinical recommendation statement";
            case DEF: return "definition";
            case DISC: return "disclaimer";
            case FINALDT: return "finalized date/time";
            case GUIDE: return "guidance";
            case IDUR: return "improvement notation";
            case ITMCNT: return "items counted";
            case KEY: return "keyword";
            case MEDT: return "measurement end date";
            case MSD: return "measurement start date";
            case MSRADJ: return "risk adjustment";
            case MSRAGG: return "rate aggregation";
            case MSRIMPROV: return "health quality measure improvement notation";
            case MSRJUR: return "jurisdiction";
            case MSRRPTR: return "reporter type";
            case MSRRPTTIME: return "timeframe for reporting";
            case MSRSCORE: return "measure scoring";
            case MSRSET: return "health quality measure care setting";
            case MSRTOPIC: return "health quality measure topic type";
            case MSRTP: return "measurement period";
            case MSRTYPE: return "measure type";
            case RAT: return "rationale";
            case REF: return "reference";
            case SDE: return "supplemental data elements";
            case STRAT: return "stratification";
            case TRANF: return "transmission format";
            case USE: return "notice of use";
            case _OBSERVATIONSEQUENCETYPE: return "ObservationSequenceType";
            case TIMEABSOLUTE: return "absolute time sequence";
            case TIMERELATIVE: return "relative time sequence";
            case _OBSERVATIONSERIESTYPE: return "ObservationSeriesType";
            case _ECGOBSERVATIONSERIESTYPE: return "ECGObservationSeriesType";
            case REPRESENTATIVEBEAT: return "ECG representative beat waveforms";
            case RHYTHM: return "ECG rhythm waveforms";
            case _PATIENTIMMUNIZATIONRELATEDOBSERVATIONTYPE: return "PatientImmunizationRelatedObservationType";
            case CLSSRM: return "classroom";
            case GRADE: return "grade";
            case SCHL: return "school";
            case SCHLDIV: return "school division";
            case TEACHER: return "teacher";
            case _POPULATIONINCLUSIONOBSERVATIONTYPE: return "PopulationInclusionObservationType";
            case DENEX: return "denominator exclusions";
            case DENEXCEP: return "denominator exceptions";
            case DENOM: return "denominator";
            case IPOP: return "initial population";
            case IPPOP: return "initial patient population";
            case MSRPOPL: return "measure population";
            case MSRPOPLEX: return "measure population exclusions";
            case NUMER: return "numerator";
            case NUMEX: return "numerator exclusions";
            case _PREFERENCEOBSERVATIONTYPE: return "_PreferenceObservationType";
            case PREFSTRENGTH: return "preference strength";
            case ADVERSEREACTION: return "Adverse Reaction";
            case ASSERTION: return "Assertion";
            case CASESER: return "case seriousness criteria";
            case CDIO: return "case disease imported observation";
            case CRIT: return "criticality";
            case CTMO: return "case transmission mode observation";
            case DX: return "ObservationDiagnosisTypes";
            case ADMDX: return "admitting diagnosis";
            case DISDX: return "discharge diagnosis";
            case INTDX: return "intermediate diagnosis";
            case NOI: return "nature of injury";
            case GISTIER: return "GIS tier";
            case HHOBS: return "household situation observation";
            case ISSUE: return "detected issue";
            case _ACTADMINISTRATIVEDETECTEDISSUECODE: return "ActAdministrativeDetectedIssueCode";
            case _ACTADMINISTRATIVEAUTHORIZATIONDETECTEDISSUECODE: return "ActAdministrativeAuthorizationDetectedIssueCode";
            case NAT: return "Insufficient authorization";
            case SUPPRESSED: return "record suppressed";
            case VALIDAT: return "validation issue";
            case KEY204: return "Unknown key identifier";
            case KEY205: return "Duplicate key identifier";
            case COMPLY: return "Compliance Alert";
            case DUPTHPY: return "Duplicate Therapy Alert";
            case DUPTHPCLS: return "duplicate therapeutic alass alert";
            case DUPTHPGEN: return "duplicate generic alert";
            case ABUSE: return "commonly abused/misused alert";
            case FRAUD: return "potential fraud";
            case PLYDOC: return "Poly-orderer Alert";
            case PLYPHRM: return "Poly-supplier Alert";
            case DOSE: return "Dosage problem";
            case DOSECOND: return "dosage-condition alert";
            case DOSEDUR: return "Dose-Duration Alert";
            case DOSEDURH: return "Dose-Duration High Alert";
            case DOSEDURHIND: return "Dose-Duration High for Indication Alert";
            case DOSEDURL: return "Dose-Duration Low Alert";
            case DOSEDURLIND: return "Dose-Duration Low for Indication Alert";
            case DOSEH: return "High Dose Alert";
            case DOSEHINDA: return "High Dose for Age Alert";
            case DOSEHIND: return "High Dose for Indication Alert";
            case DOSEHINDSA: return "High Dose for Height/Surface Area Alert";
            case DOSEHINDW: return "High Dose for Weight Alert";
            case DOSEIVL: return "Dose-Interval Alert";
            case DOSEIVLIND: return "Dose-Interval for Indication Alert";
            case DOSEL: return "Low Dose Alert";
            case DOSELINDA: return "Low Dose for Age Alert";
            case DOSELIND: return "Low Dose for Indication Alert";
            case DOSELINDSA: return "Low Dose for Height/Surface Area Alert";
            case DOSELINDW: return "Low Dose for Weight Alert";
            case MDOSE: return "maximum dosage reached";
            case OBSA: return "Observation Alert";
            case AGE: return "Age Alert";
            case ADALRT: return "adult alert";
            case GEALRT: return "geriatric alert";
            case PEALRT: return "pediatric alert";
            case COND: return "Condition Alert";
            case HGHT: return "HGHT";
            case LACT: return "Lactation Alert";
            case PREG: return "Pregnancy Alert";
            case WGHT: return "WGHT";
            case CREACT: return "common reaction alert";
            case GEN: return "Genetic Alert";
            case GEND: return "Gender Alert";
            case LAB: return "Lab Alert";
            case REACT: return "Reaction Alert";
            case ALGY: return "Allergy Alert";
            case INT: return "Intolerance Alert";
            case RREACT: return "Related Reaction Alert";
            case RALG: return "Related Allergy Alert";
            case RAR: return "Related Prior Reaction Alert";
            case RINT: return "Related Intolerance Alert";
            case BUS: return "business constraint violation";
            case CODEINVAL: return "code is not valid";
            case CODEDEPREC: return "code has been deprecated";
            case FORMAT: return "invalid format";
            case ILLEGAL: return "illegal";
            case LENRANGE: return "length out of range";
            case LENLONG: return "length is too long";
            case LENSHORT: return "length is too short";
            case MISSCOND: return "conditional element missing";
            case MISSMAND: return "mandatory element missing";
            case NODUPS: return "duplicate values are not permitted";
            case NOPERSIST: return "element will not be persisted";
            case REPRANGE: return "repetitions out of range";
            case MAXOCCURS: return "repetitions above maximum";
            case MINOCCURS: return "repetitions below minimum";
            case _ACTADMINISTRATIVERULEDETECTEDISSUECODE: return "ActAdministrativeRuleDetectedIssueCode";
            case KEY206: return "non-matching identification";
            case OBSOLETE: return "obsolete record returned";
            case _ACTSUPPLIEDITEMDETECTEDISSUECODE: return "ActSuppliedItemDetectedIssueCode";
            case _ADMINISTRATIONDETECTEDISSUECODE: return "AdministrationDetectedIssueCode";
            case _APPROPRIATENESSDETECTEDISSUECODE: return "AppropriatenessDetectedIssueCode";
            case _INTERACTIONDETECTEDISSUECODE: return "InteractionDetectedIssueCode";
            case FOOD: return "Food Interaction Alert";
            case TPROD: return "Therapeutic Product Alert";
            case DRG: return "Drug Interaction Alert";
            case NHP: return "Natural Health Product Alert";
            case NONRX: return "Non-Prescription Interaction Alert";
            case PREVINEF: return "previously ineffective";
            case DACT: return "drug action detected issue";
            case TIME: return "timing detected issue";
            case ALRTENDLATE: return "end too late alert";
            case ALRTSTRTLATE: return "start too late alert";
            case _SUPPLYDETECTEDISSUECODE: return "SupplyDetectedIssueCode";
            case ALLDONE: return "already performed";
            case FULFIL: return "fulfillment alert";
            case NOTACTN: return "no longer actionable";
            case NOTEQUIV: return "not equivalent alert";
            case NOTEQUIVGEN: return "not generically equivalent alert";
            case NOTEQUIVTHER: return "not therapeutically equivalent alert";
            case TIMING: return "event timing incorrect alert";
            case INTERVAL: return "outside requested time";
            case MINFREQ: return "too soon within frequency based on the usage";
            case HELD: return "held/suspended alert";
            case TOOLATE: return "Refill Too Late Alert";
            case TOOSOON: return "Refill Too Soon Alert";
            case HISTORIC: return "record recorded as historical";
            case PATPREF: return "violates stated preferences";
            case PATPREFALT: return "violates stated preferences, alternate available";
            case KSUBJ: return "knowledge subject";
            case KSUBT: return "knowledge subtopic";
            case OINT: return "intolerance";
            case ALG: return "Allergy";
            case DALG: return "Drug Allergy";
            case EALG: return "Environmental Allergy";
            case FALG: return "Food Allergy";
            case DINT: return "Drug Intolerance";
            case DNAINT: return "Drug Non-Allergy Intolerance";
            case EINT: return "Environmental Intolerance";
            case ENAINT: return "Environmental Non-Allergy Intolerance";
            case FINT: return "Food Intolerance";
            case FNAINT: return "Food Non-Allergy Intolerance";
            case NAINT: return "Non-Allergy Intolerance";
            case SEV: return "Severity Observation";
            case _ROIOVERLAYSHAPE: return "ROIOverlayShape";
            case CIRCLE: return "circle";
            case ELLIPSE: return "ellipse";
            case POINT: return "point";
            case POLY: return "polyline";
            case C: return "corrected";
            case DIET: return "Diet";
            case BR: return "breikost (GE)";
            case DM: return "diabetes mellitus diet";
            case FAST: return "fasting";
            case FORMULA: return "formula diet";
            case GF: return "gluten free";
            case LF: return "low fat";
            case LP: return "low protein";
            case LQ: return "liquid";
            case LS: return "low sodium";
            case N: return "normal diet";
            case NF: return "no fat";
            case PAF: return "phenylalanine free";
            case PAR: return "parenteral";
            case RD: return "reduction diet";
            case SCH: return "schonkost (GE)";
            case SUPPLEMENT: return "nutritional supplement";
            case T: return "tea only";
            case VLI: return "low valin, leucin, isoleucin";
            case DRUGPRG: return "drug program";
            case F: return "final";
            case PRLMN: return "preliminary";
            case SECOBS: return "SecurityObservationType";
            case SECCATOBS: return "security category observation";
            case SECCLASSOBS: return "security classification observation";
            case SECCONOBS: return "security control observation";
            case SECINTOBS: return "security integrity observation";
            case SECALTINTOBS: return "security alteration integrity observation";
            case SECDATINTOBS: return "security data integrity observation";
            case SECINTCONOBS: return "security integrity confidence observation";
            case SECINTPRVOBS: return "security integrity provenance observation";
            case SECINTPRVABOBS: return "security integrity provenance asserted by observation";
            case SECINTPRVRBOBS: return "security integrity provenance reported by observation";
            case SECINTSTOBS: return "security integrity status observation";
            case SECTRSTOBS: return "SECTRSTOBS";
            case TRSTACCRDOBS: return "trust accreditation observation";
            case TRSTAGREOBS: return "trust agreement observation";
            case TRSTCERTOBS: return "trust certificate observation";
            case TRSTFWKOBS: return "trust framework observation";
            case TRSTLOAOBS: return "trust assurance observation";
            case TRSTMECOBS: return "trust mechanism observation";
            case SUBSIDFFS: return "subsidized fee for service program";
            case WRKCOMP: return "(workers compensation program";
            default: return "?";
          }
    }


}

