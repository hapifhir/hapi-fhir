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

public enum V3ObservationValue {

        /**
         * Codes specify the category of observation, evidence, or document used to assess for services, e.g., discharge planning, or to establish eligibility for coverage under a policy or program. The type of evidence is coded as observation values.
         */
        _ACTCOVERAGEASSESSMENTOBSERVATIONVALUE, 
        /**
         * Code specifying financial indicators used to assess or establish eligibility for coverage under a policy or program; e.g., pay stub; tax or income document; asset document; living expenses.
         */
        _ACTFINANCIALSTATUSOBSERVATIONVALUE, 
        /**
         * Codes specifying asset indicators used to assess or establish eligibility for coverage under a policy or program.
         */
        ASSET, 
        /**
         * Indicator of annuity ownership or status as beneficiary.
         */
        ANNUITY, 
        /**
         * Indicator of real property ownership, e.g., deed or real estate contract.
         */
        PROP, 
        /**
         * Indicator of retirement investment account ownership.
         */
        RETACCT, 
        /**
         * Indicator of status as trust beneficiary.
         */
        TRUST, 
        /**
         * Code specifying income indicators used to assess or establish eligibility for coverage under a policy or program; e.g., pay or pension check, child support payments received or provided, and taxes paid.
         */
        INCOME, 
        /**
         * Indicator of child support payments received or provided.
         */
        CHILD, 
        /**
         * Indicator of disability income replacement payment.
         */
        DISABL, 
        /**
         * Indicator of investment income, e.g., dividend check, annuity payment; real estate rent, investment divestiture proceeds; trust or endowment check.
         */
        INVEST, 
        /**
         * Indicator of paid employment, e.g., letter of hire, contract, employer letter; copy of pay check or pay stub.
         */
        PAY, 
        /**
         * Indicator of retirement payment, e.g., pension check.
         */
        RETIRE, 
        /**
         * Indicator of spousal or partner support payments received or provided; e.g., alimony payment; support stipulations in a divorce settlement.
         */
        SPOUSAL, 
        /**
         * Indicator of income supplement, e.g., gifting, parental income support; stipend, or grant.
         */
        SUPPLE, 
        /**
         * Indicator of tax obligation or payment, e.g., statement of taxable income.
         */
        TAX, 
        /**
         * Codes specifying living expense indicators used to assess or establish eligibility for coverage under a policy or program.
         */
        LIVEXP, 
        /**
         * Indicator of clothing expenses.
         */
        CLOTH, 
        /**
         * Indicator of transportation expenses.
         */
        FOOD, 
        /**
         * Indicator of health expenses; including medication costs, health service costs, financial participations, and health coverage premiums.
         */
        HEALTH, 
        /**
         * Indicator of housing expense, e.g., household appliances, fixtures, furnishings, and maintenance and repairs.
         */
        HOUSE, 
        /**
         * Indicator of legal expenses.
         */
        LEGAL, 
        /**
         * Indicator of mortgage amount, interest, and payments.
         */
        MORTG, 
        /**
         * Indicator of rental or lease payments.
         */
        RENT, 
        /**
         * Indicator of transportation expenses.
         */
        SUNDRY, 
        /**
         * Indicator of transportation expenses, e.g., vehicle payments, vehicle insurance, vehicle fuel, and vehicle maintenance and repairs.
         */
        TRANS, 
        /**
         * Indicator of transportation expenses.
         */
        UTIL, 
        /**
         * Code specifying eligibility indicators used to assess or establish eligibility for coverage under a policy or program eligibility status, e.g., certificates of creditable coverage; student enrollment; adoption, marriage or birth certificate.
         */
        ELSTAT, 
        /**
         * Indicator of adoption.
         */
        ADOPT, 
        /**
         * Indicator of birth.
         */
        BTHCERT, 
        /**
         * Indicator of creditable coverage.
         */
        CCOC, 
        /**
         * Indicator of driving status.
         */
        DRLIC, 
        /**
         * Indicator of foster child status.
         */
        FOSTER, 
        /**
         * Indicator of status as covered member under a policy or program, e.g., member id card or coverage document.
         */
        MEMBER, 
        /**
         * Indicator of military status.
         */
        MIL, 
        /**
         * Indicator of marriage status.
         */
        MRGCERT, 
        /**
         * Indicator of citizenship.
         */
        PASSPORT, 
        /**
         * Indicator of student status.
         */
        STUDENRL, 
        /**
         * Code specifying non-clinical indicators related to health status used to assess or establish eligibility for coverage under a policy or program, e.g., pregnancy, disability, drug use, mental health issues.
         */
        HLSTAT, 
        /**
         * Indication of disability.
         */
        DISABLE, 
        /**
         * Indication of drug use.
         */
        DRUG, 
        /**
         * Indication of IV drug use .
         */
        IVDRG, 
        /**
         * Non-clinical report of pregnancy.
         */
        PGNT, 
        /**
         * Code specifying observations related to living dependency, such as dependent upon spouse for activities of daily living.
         */
        LIVDEP, 
        /**
         * Continued living in private residence requires functional and health care assistance from one or more relatives.
         */
        RELDEP, 
        /**
         * Continued living in private residence requires functional and health care assistance from spouse or life partner.
         */
        SPSDEP, 
        /**
         * Continued living in private residence requires functional and health care assistance from one or more unrelated persons.
         */
        URELDEP, 
        /**
         * Code specifying observations related to living situation for a person in a private residence.
         */
        LIVSIT, 
        /**
         * Living alone.  Maps to PD1-2   Living arrangement   (IS)   00742 [A]
         */
        ALONE, 
        /**
         * Living with one or more dependent children requiring moderate supervision.
         */
        DEPCHD, 
        /**
         * Living with disabled spouse requiring functional and health care assistance
         */
        DEPSPS, 
        /**
         * Living with one or more dependent children requiring intensive supervision
         */
        DEPYGCHD, 
        /**
         * Living with family. Maps to PD1-2   Living arrangement   (IS)   00742 [F]
         */
        FAM, 
        /**
         * Living with one or more relatives. Maps to PD1-2   Living arrangement   (IS)   00742 [R]
         */
        RELAT, 
        /**
         * Living only with spouse or life partner. Maps to PD1-2   Living arrangement   (IS)   00742 [S]
         */
        SPS, 
        /**
         * Living with one or more unrelated persons.
         */
        UNREL, 
        /**
         * Code specifying observations or indicators related to socio-economic status used to assess to assess for services, e.g., discharge planning, or to establish eligibility for coverage under a policy or program.
         */
        SOECSTAT, 
        /**
         * Indication of abuse victim.
         */
        ABUSE, 
        /**
         * Indication of status as homeless.
         */
        HMLESS, 
        /**
         * Indication of status as illegal immigrant.
         */
        ILGIM, 
        /**
         * Indication of status as incarcerated.
         */
        INCAR, 
        /**
         * Indication of probation status.
         */
        PROB, 
        /**
         * Indication of refugee status.
         */
        REFUG, 
        /**
         * Indication of unemployed status.
         */
        UNEMPL, 
        /**
         * Indicates the result of a particular allergy test.  E.g. Negative, Mild, Moderate, Severe
         */
        _ALLERGYTESTVALUE, 
        /**
         * Description:Patient exhibits no reaction to the challenge agent.
         */
        A0, 
        /**
         * Description:Patient exhibits a minimal reaction to the challenge agent.
         */
        A1, 
        /**
         * Description:Patient exhibits a mild reaction to the challenge agent.
         */
        A2, 
        /**
         * Description:Patient exhibits moderate reaction to the challenge agent.
         */
        A3, 
        /**
         * Description:Patient exhibits a severe reaction to the challenge agent.
         */
        A4, 
        /**
         * Observation values that communicate the method used in a quality measure to combine the component measure results included in an composite measure.
         */
        _COMPOSITEMEASURESCORING, 
        /**
         * Code specifying that the measure uses all-or-nothing scoring. All-or-nothing scoring places an individual in the numerator of the composite measure if and only if they are in the numerator of all component measures in which they are in the denominator.
         */
        ALLORNONESCR, 
        /**
         * Code specifying that the measure uses linear scoring. Linear scoring computes the fraction of component measures in which the individual appears in the numerator, giving equal weight to each component measure.
         */
        LINEARSCR, 
        /**
         * Code specifying that the measure uses opportunity-based scoring. In opportunity-based scoring the measure score is determined by combining the denominator and numerator of each component measure to determine an overall composite score.
         */
        OPPORSCR, 
        /**
         * Code specifying that the measure uses weighted scoring. Weighted scoring assigns a factor to each component measure to weight that measure's contribution to the overall score.
         */
        WEIGHTSCR, 
        /**
         * Description:Coded observation values for coverage limitations, for e.g., types of claims or types of parties covered under a policy or program.
         */
        _COVERAGELIMITOBSERVATIONVALUE, 
        /**
         * Description:Coded observation values for types of covered parties under a policy or program based on their personal relationships or employment status.
         */
        _COVERAGELEVELOBSERVATIONVALUE, 
        /**
         * Description:Child over an age as specified by coverage policy or program, e.g., student, differently abled, and income dependent.
         */
        ADC, 
        /**
         * Description:Dependent biological, adopted, foster child as specified by coverage policy or program.
         */
        CHD, 
        /**
         * Description:Person requiring functional and/or financial assistance from another person as specified by coverage policy or program.
         */
        DEP, 
        /**
         * Description:Persons registered as a family unit in a domestic partner registry as specified by law and by coverage policy or program.
         */
        DP, 
        /**
         * Description:An individual employed by an employer who receive remuneration in wages, salary, commission, tips, piece-rates, or pay-in-kind through the employeraTMs payment system (i.e., not a contractor) as specified by coverage policy or program.
         */
        ECH, 
        /**
         * Description:As specified by coverage policy or program.
         */
        FLY, 
        /**
         * Description:Person as specified by coverage policy or program.
         */
        IND, 
        /**
         * Description:A pair of people of the same gender who live together as a family as specified by coverage policy or program, e.g., Naomi and Ruth from the Book of Ruth; Socrates and Alcibiades
         */
        SSP, 
        /**
         * A clinical judgment as to the worst case result of a future exposure (including substance administration). When the worst case result is assessed to have a life-threatening or organ system threatening potential, it is considered to be of high criticality.
         */
        _CRITICALITYOBSERVATIONVALUE, 
        /**
         * Worst case result of a future exposure is assessed to be life-threatening or having high potential for organ system failure.
         */
        CRITH, 
        /**
         * Worst case result of a future exposure is not assessed to be life-threatening or having high potential for organ system failure.
         */
        CRITL, 
        /**
         * Unable to assess the worst case result of a future exposure.
         */
        CRITU, 
        /**
         * Description: The domain contains genetic analysis specific observation values, e.g. Homozygote, Heterozygote, etc.
         */
        _GENETICOBSERVATIONVALUE, 
        /**
         * Description: An individual having different alleles at one or more loci regarding a specific character
         */
        HOMOZYGOTE, 
        /**
         * Observation values used to indicate the type of scoring (e.g. proportion, ratio) used by a health quality measure.
         */
        _OBSERVATIONMEASURESCORING, 
        /**
         * A measure in which either short-term cross-section or long-term longitudinal analysis is performed over a group of subjects defined by a set of common properties or defining characteristics (e.g., Male smokers between the ages of 40 and 50 years, exposure to treatment, exposure duration).
         */
        COHORT, 
        /**
         * A measure score in which each individual value for the measure can fall anywhere along a continuous scale (e.g., mean time to thrombolytics which aggregates the time in minutes from a case presenting with chest pain to the time of administration of thrombolytics).
         */
        CONTVAR, 
        /**
         * A score derived by dividing the number of cases that meet a criterion for quality (the numerator) by the number of eligible cases within a given time frame (the denominator) where the numerator cases are a subset of the denominator cases (e.g., percentage of eligible women with a mammogram performed in the last year).
         */
        PROPOR, 
        /**
         * A score that may have a value of zero or greater that is derived by dividing a count of one type of data by a count of another type of data (e.g., the number of patients with central lines who develop infection divided by the number of central line days).
         */
        RATIO, 
        /**
         * Observation values used to indicate what kind of health quality measure is used.
         */
        _OBSERVATIONMEASURETYPE, 
        /**
         * A measure that is composed from one or more other measures and indicates an overall summary of those measures.
         */
        COMPOSITE, 
        /**
         * A measure related to the efficiency of medical treatment.
         */
        EFFICIENCY, 
        /**
         * A measure related to the level of patient engagement or patient experience of care.
         */
        EXPERIENCE, 
        /**
         * A measure that indicates the result of the performance (or non-performance) of a function or process.
         */
        OUTCOME, 
        /**
         * A measure which focuses on a process which leads to a certain outcome, meaning that a scientific basis exists for believing that the process, when executed well, will increase the probability of achieving a desired outcome.
         */
        PROCESS, 
        /**
         * A measure related to the extent of use of clinical resources or cost of care.
         */
        RESOURCE, 
        /**
         * A measure related to the structure of patient care.
         */
        STRUCTURE, 
        /**
         * Observation values used to assert various populations that a subject falls into.
         */
        _OBSERVATIONPOPULATIONINCLUSION, 
        /**
         * Patients who should be removed from the eMeasure population and denominator before determining if numerator criteria are met. Denominator exclusions are used in proportion and ratio measures to help narrow the denominator.
         */
        DENEX, 
        /**
         * Denominator exceptions are those conditions that should remove a patient, procedure or unit of measurement from the denominator only if the numerator criteria are not met. Denominator exceptions allow for adjustment of the calculated score for those providers with higher risk populations. Denominator exceptions are used only in proportion eMeasures. They are not appropriate for ratio or continuous variable eMeasures.  Denominator exceptions allow for the exercise of clinical judgment and should be specifically defined where capturing the information in a structured manner fits the clinical workflow. Generic denominator exception reasons used in proportion eMeasures fall into three general categories:

                        
                           Medical reasons
                           Patient reasons
                           System reasons
         */
        DENEXCEP, 
        /**
         * It can be the same as the initial patient population or a subset of the initial patient population to further constrain the population for the purpose of the eMeasure. Different measures within an eMeasure set may have different Denominators. Continuous Variable eMeasures do not have a Denominator, but instead define a Measure Population.
         */
        DENOM, 
        /**
         * The initial population refers to all entities to be evaluated by a specific quality measure who share a common set of specified characteristics within a specific measurement set to which a given measure belongs.
         */
        IP, 
        /**
         * The initial patient population refers to all patients to be evaluated by a specific quality measure who share a common set of specified characteristics within a specific measurement set to which a given measure belongs. Details often include information based upon specific age groups, diagnoses, diagnostic and procedure codes, and enrollment periods.
         */
        IPP, 
        /**
         * Measure population is used only in continuous variable eMeasures. It is a narrative description of the eMeasure population. 
(e.g., all patients seen in the Emergency Department during the measurement period).
         */
        MSRPOPL, 
        /**
         * Numerators are used in proportion and ratio eMeasures. In proportion measures the numerator criteria are the processes or outcomes expected for each patient, procedure, or other unit of measurement defined in the denominator. In ratio measures the numerator is related, but not directly derived from the denominator (e.g., a numerator listing the number of central line blood stream infections and a denominator indicating the days per thousand of central line usage in a specific time period).
         */
        NUMER, 
        /**
         * Numerator Exclusions are used only in ratio eMeasures to define instances that should not be included in the numerator data. (e.g., if the number of central line blood stream infections per 1000 catheter days were to exclude infections with a specific bacterium, that bacterium would be listed as a numerator exclusion.)
         */
        NUMEX, 
        /**
         * PartialCompletionScale
         */
        _PARTIALCOMPLETIONSCALE, 
        /**
         * Value for Act.partialCompletionCode attribute that implies 81-99% completion
         */
        G, 
        /**
         * Value for Act.partialCompletionCode attribute that implies 61-80% completion
         */
        LE, 
        /**
         * Value for Act.partialCompletionCode attribute that implies 41-60% completion
         */
        ME, 
        /**
         * Value for Act.partialCompletionCode attribute that implies 1-20% completion
         */
        MI, 
        /**
         * Value for Act.partialCompletionCode attribute that implies 0% completion
         */
        N, 
        /**
         * Value for Act.partialCompletionCode attribute that implies 21-40% completion
         */
        S, 
        /**
         * Observation values used to indicate security observation metadata.
         */
        _SECURITYOBSERVATIONVALUE, 
        /**
         * Abstract security observation values used to indicate security integrity metadata.

                        
                           Examples: Codes conveying integrity status, integrity confidence, and provenance.
         */
        _SECINTOBV, 
        /**
         * Abstract security metadata observation values used to indicate mechanism used for authorized alteration of an IT resource (data, information object, service, or system capability)
         */
        _SECALTINTOBV, 
        /**
         * Security metadata observation values used to indicate the use of a more abstract version of the content, e.g., replacing exact value of an age or date field with a range, or remove the left digits of a credit card number or SSN.
         */
        ABSTRED, 
        /**
         * Security metadata observation values used to indicate the use of an algorithmic combination of actual values with the result of an aggregate function, e.g., average, sum, or count in order to limit disclosure of an IT resource (data, information object, service, or system capability) to the minimum necessary.
         */
        AGGRED, 
        /**
         * Security metadata observation value conveying the alteration integrity of an IT resource (data, information object, service, or system capability) by used to indicate the mechanism by which software systems can strip portions of the resource that could allow the identification of the source of the information or the information subject.  No key to relink the data is retained.
         */
        ANONYED, 
        /**
         * Security metadata observation value used to indicate that the IT resource semantic content has been transformed from one encoding to another.

                        
                           Usage Note: "MAP" code does not indicate the semantic fidelity of the transformed content.

                        To indicate semantic fidelity for maps of HL7 to other code systems, this security alteration integrity observation may be further specified using an Act valued with Value Set: MapRelationship (2.16.840.1.113883.1.11.11052).

                        Semantic fidelity of the mapped IT Resource may also be indicated using a SecurityIntegrityConfidenceObservation.
         */
        MAPPED, 
        /**
         * Security metadata observation value conveying the alteration integrity of an IT resource (data, information object, service, or system capability) by indicating the mechanism by which software systems can make data unintelligible (that is, as unreadable and unusable by algorithmically transforming plaintext into ciphertext) such that it can only be accessed or used by authorized users.  An authorized user may be provided a key to decrypt per license or "shared secret".

                        
                           Usage Note: "MASKED" may be used, per applicable policy, as a flag to indicate to a user or receiver that some portion of an IT resource has been further encrypted, and may be accessed only by an authorized user or receiver to which a decryption key is provided.
         */
        MASKED, 
        /**
         * Security metadata observation value conveying the alteration integrity of an IT resource (data, information object, service, or system capability), by indicating the mechanism by which software systems can strip portions of the resource that could allow the identification of the source of the information or the information subject.  Custodian may retain a key to relink data necessary to reidentify the information subject.

                        
                           Rationale: Personal data which has been processed to make it impossible to know whose data it is. Used particularly for secondary use of health data. In some cases, it may be possible for authorized individuals to restore the identity of the individual, e.g.,for public health case management.  Based on ISO/TS 25237:2008 Health informaticsâ€”Pseudonymization
         */
        PSEUDED, 
        /**
         * Security metadata observation value used to indicate the mechanism by which software systems can filter an IT resource (data, information object, service, or system capability) to remove any portion of the resource that is not authorized to be access, used, or disclosed.

                        
                           Usage Note: "REDACTED" may be used, per applicable policy, as a flag to indicate to a user or receiver that some portion of an IT resource has filtered and not included in the content accessed or received.
         */
        REDACTED, 
        /**
         * Metadata observation used to indicate that some information has been removed from the source object when the view this object contains was constructed because of configuration options when the view was created. The content may not be suitable for use as the basis of a record update

                        
                           Usage Note: This is not suitable to be used when information is removed for security reasons - see the code REDACTED for this use.
         */
        SUBSETTED, 
        /**
         * Security metadata observation value used to indicate that the IT resource syntax has been transformed from one syntactical representation to another.  

                        
                           Usage Note: "SYNTAC" code does not indicate the syntactical correctness of the syntactically transformed IT resource.
         */
        SYNTAC, 
        /**
         * Security metadata observation value used to indicate that the IT resource has been translated from one human language to another.  

                        
                           Usage Note: "TRSLT" does not indicate the fidelity of the translation or the languages translated.

                        The fidelity of the IT Resource translation may be indicated using a SecurityIntegrityConfidenceObservation.

                        To indicate languages, use the Value Set:HumanLanguage (2.16.840.1.113883.1.11.11526)
         */
        TRSLT, 
        /**
         * Security metadata observation value conveying the alteration integrity of an IT resource (data, information object, service, or system capability)  which indicates that the resource only retains versions of an IT resource  for access and use per applicable policy

                        
                           Usage Note: When this code is used, expectation is that the system has removed historical versions of the data that falls outside the time period deemed to be the effective time of the applicable version.
         */
        VERSIONED, 
        /**
         * Abstract security observation values used to indicate data integrity metadata.

                        
                           Examples: Codes conveying the mechanism used to preserve the accuracy and consistency of an IT resource such as a digital signature and a cryptographic hash function.
         */
        _SECDATINTOBV, 
        /**
         * Security metadata observation value used to indicate the mechanism by which software systems can establish that data was not modified in transit.

                        
                           Rationale: This definition is intended to align with the ISO 22600-2 3.3.19 definition of cryptographic checkvalue: Information which is derived by performing a cryptographic transformation (see cryptography) on the data unit.  The derivation of the checkvalue may be performed in one or more steps and is a result of a mathematical function of the key and a data unit. It is usually used to check the integrity of a data unit.

                        
                           Examples: 
                        

                        
                           SHA-1
                           SHA-2 (Secure Hash Algorithm)
         */
        CRYTOHASH, 
        /**
         * Security metadata observation value used to indicate the mechanism by which software systems use digital signature to establish that data has not been modified.  

                        
                           Rationale: This definition is intended to align with the ISO 22600-2 3.3.26 definition of digital signature:  Data appended to, or a cryptographic transformation (see cryptography) of, a data unit that allows a recipient of the data unit to prove the source and integrity of the data unit and protect against forgery e.g., by the recipient.
         */
        DIGSIG, 
        /**
         * Abstract security observation value used to indicate integrity confidence metadata.

                        
                           Examples: Codes conveying the level of reliability and trustworthiness of an IT resource.
         */
        _SECINTCONOBV, 
        /**
         * Security metadata observation value used to indicate that the veracity or trustworthiness of an IT resource (data, information object, service, or system capability) for a specified purpose of use is perceived to be or deemed by policy to be very high.
         */
        HRELIABLE, 
        /**
         * Security metadata observation value used to indicate that the veracity or trustworthiness of an IT resource (data, information object, service, or system capability) for a specified purpose of use is perceived to be or deemed by policy to be adequate.
         */
        RELIABLE, 
        /**
         * Security metadata observation value used to indicate that the veracity or trustworthiness of an IT resource (data, information object, service, or system capability) for a specified purpose of use is perceived to be or deemed by policy to be uncertain.
         */
        UNCERTREL, 
        /**
         * Security metadata observation value used to indicate that the veracity or trustworthiness of an IT resource (data, information object, service, or system capability) for a specified purpose of use is perceived to be or deemed by policy to be inadequate.
         */
        UNRELIABLE, 
        /**
         * Abstract security metadata observation value used to indicate the provenance of an IT resource (data, information object, service, or system capability).

                        
                           Examples: Codes conveying the provenance metadata about the entity reporting an IT resource.
         */
        _SECINTPRVOBV, 
        /**
         * Abstract security provenance metadata observation value used to indicate the entity that asserted an IT resource (data, information object, service, or system capability).

                        
                           Examples: Codes conveying the provenance metadata about the entity asserting the resource.
         */
        _SECINTPRVABOBV, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a clinician.
         */
        CLINAST, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a device.
         */
        DEVAST, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a healthcare professional.
         */
        HCPAST, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a patient acquaintance.
         */
        PACQAST, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a patient.
         */
        PATAST, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a payer.
         */
        PAYAST, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a professional.
         */
        PROAST, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a substitute decision maker.
         */
        SDMAST, 
        /**
         * Abstract security provenance metadata observation value used to indicate the entity that reported the resource (data, information object, service, or system capability).

                        
                           Examples: Codes conveying the provenance metadata about the entity reporting an IT resource.
         */
        _SECINTPRVRBOBV, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a clinician.
         */
        CLINRPT, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a device.
         */
        DEVRPT, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a healthcare professional.
         */
        HCPRPT, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a patient acquaintance.
         */
        PACQRPT, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a patient.
         */
        PATRPT, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a payer.
         */
        PAYRPT, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a professional.
         */
        PRORPT, 
        /**
         * Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a substitute decision maker.
         */
        SDMRPT, 
        /**
         * Observation value used to indicate aspects of trust applicable to an IT resource (data, information object, service, or system capability).
         */
        SECTRSTOBV, 
        /**
         * Values for security trust accreditation metadata observation made about the formal declaration by an authority or neutral third party that validates the technical, security, trust, and business practice conformance of Trust Agents to facilitate security, interoperability, and trust among participants within a security domain or trust framework.
         */
        TRSTACCRDOBV, 
        /**
         * Values for security trust agreement metadata observation made about privacy and security requirements with which a security domain must comply. [ISO IEC 10181-1]
[ISO IEC 10181-1]
         */
        TRSTAGREOBV, 
        /**
         * Values for security trust certificate metadata observation made about a set of security-relevant data issued by a security authority or trusted third party, together with security information which is used to provide the integrity and data origin authentication services for an IT resource (data, information object, service, or system capability). [Based on ISO IEC 10181-1]

                        For example, a Certificate Policy (CP), which is a named set of rules that indicates the applicability of a certificate to a particular community and/or class of application with common security requirements.  A particular Certificate Policy might indicate the applicability of a type of certificate to the authentication of electronic data interchange transactions for the trading of goods within a given price range.  Another example is Cross Certification with Federal Bridge.
         */
        TRSTCERTOBV, 
        /**
         * Values for security trust assurance metadata observation made about the digital quality or reliability of a trust assertion, activity, capability, information exchange, mechanism, process, or protocol.
         */
        TRSTLOAOBV, 
        /**
         * The value assigned as the indicator of the digital quality or reliability of the verification and validation process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2]

                        For example, the degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies]
         */
        LOAAN, 
        /**
         * Indicator of low digital quality or reliability of the digital reliability of the verification and validation process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2] 

                        The degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies] 

                        Low authentication level of assurance indicates that the relying party may have little or no confidence in the asserted identity's validity. Level 1 requires little or no confidence in the asserted identity. No identity proofing is required at this level, but the authentication mechanism should provide some assurance that the same claimant is accessing the protected transaction or data. A wide range of available authentication technologies can be employed and any of the token methods of Levels 2, 3, or 4, including Personal Identification Numbers (PINs), may be used. To be authenticated, the claimant must prove control of the token through a secure authentication protocol. At Level 1, long-term shared authentication secrets may be revealed to verifiers.  Assertions issued about claimants as a result of a successful authentication are either cryptographically authenticated by relying parties (using approved methods) or are obtained directly from a trusted party via a secure authentication protocol.   [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAN1, 
        /**
         * Indicator of basic digital quality or reliability of the digital reliability of the verification and validation process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2] 

                        The degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies]

                        Basic authentication level of assurance indicates that the relying party may have some confidence in the asserted identity's validity. Level 2 requires confidence that the asserted identity is accurate. Level 2 provides for single-factor remote network authentication, including identity-proofing requirements for presentation of identifying materials or information. A wide range of available authentication technologies can be employed, including any of the token methods of Levels 3 or 4, as well as passwords. Successful authentication requires that the claimant prove through a secure authentication protocol that the claimant controls the token.  Eavesdropper, replay, and online guessing attacks are prevented.  
Long-term shared authentication secrets, if used, are never revealed to any party except the claimant and verifiers operated by the CSP; however, session (temporary) shared secrets may be provided to independent verifiers by the CSP. Approved cryptographic techniques are required. Assertions issued about claimants as a result of a successful authentication are either cryptographically authenticated by relying parties (using approved methods) or are obtained directly from a trusted party via a secure authentication protocol.   [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAN2, 
        /**
         * Indicator of medium digital quality or reliability of the digital reliability of verification and validation of the process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2] 

                        The degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies] 

                        Medium authentication level of assurance indicates that the relying party may have high confidence in the asserted identity's validity.  Level 3 is appropriate for transactions that need high confidence in the accuracy of the asserted identity. Level 3 provides multifactor remote network authentication. At this level, identity-proofing procedures require verification of identifying materials and information. Authentication is based on proof of possession of a key or password through a cryptographic protocol. Cryptographic strength mechanisms should protect the primary authentication token (a cryptographic key) against compromise by the protocol threats, including eavesdropper, replay, online guessing, verifier impersonation, and man-in-the-middle attacks. A minimum of two authentication factors is required. Three kinds of tokens may be used:

                        
                           "soft" cryptographic token, which has the key stored on a general-purpose computer, 
                           "hard" cryptographic token, which has the key stored on a special hardware device, and 
                           "one-time password" device token, which has symmetric key stored on a personal hardware device that is a cryptographic module validated at FIPS 140-2 Level 1 or higher. Validation testing of cryptographic modules and algorithms for conformance to Federal Information Processing Standard (FIPS) 140-2, Security Requirements for Cryptographic Modules, is managed by NIST.
                        
                        Authentication requires that the claimant prove control of the token through a secure authentication protocol. The token must be unlocked with a password or biometric representation, or a password must be used in a secure authentication protocol, to establish two-factor authentication. Long-term shared authentication secrets, if used, are never revealed to any party except the claimant and verifiers operated directly by the CSP; however, session (temporary) shared secrets may be provided to independent verifiers by the CSP. Approved cryptographic techniques are used for all operations.  Assertions issued about claimants as a result of a successful authentication are either cryptographically authenticated by relying parties (using approved methods) or are obtained directly from a trusted party via a secure authentication protocol.    [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAN3, 
        /**
         * Indicator of high digital quality or reliability of the digital reliability of the verification and validation process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2] 

                        The degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies]

                        High authentication level of assurance indicates that the relying party may have very high confidence in the asserted identity's validity. Level 4 is for transactions that need very high confidence in the accuracy of the asserted identity. Level 4 provides the highest practical assurance of remote network authentication. Authentication is based on proof of possession of a key through a cryptographic protocol. This level is similar to Level 3 except that only â€œhardâ€? cryptographic tokens are allowed, cryptographic module validation requirements are strengthened, and subsequent critical data transfers must be authenticated via a key that is bound to the authentication process. The token should be a hardware cryptographic module validated at FIPS 140-2 Level 2 or higher overall with at least FIPS 140-2 Level 3 physical security. This level requires a physical token, which cannot readily be copied, and operator authentication at Level 2 and higher, and ensures good, two-factor remote authentication.

                        Level 4 requires strong cryptographic authentication of all parties and all sensitive data transfers between the parties. Either public key or symmetric key technology may be used. Authentication requires that the claimant prove through a secure authentication protocol that the claimant controls the token. Eavesdropper, replay, online guessing, verifier impersonation, and man-in-the-middle attacks are prevented. Long-term shared authentication secrets, if used, are never revealed to any party except the claimant and verifiers operated directly by the CSP; however, session (temporary) shared secrets may be provided to independent verifiers by the CSP. Strong approved cryptographic techniques are used for all operations. All sensitive data transfers are cryptographically authenticated using keys bound to the authentication process.   [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAN4, 
        /**
         * The value assigned as the indicator of the digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]
         */
        LOAAP, 
        /**
         * Indicator of the low digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]

                        Low authentication process level of assurance indicates that (1) long-term shared authentication secrets may be revealed to verifiers; and (2) assertions and assertion references require protection from manufacture/modification and reuse attacks.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAP1, 
        /**
         * Indicator of the basic digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]

                        Basic authentication process level of assurance indicates that long-term shared authentication secrets are never revealed to any other party except Credential Service Provider (CSP).  Sessions (temporary) shared secrets may be provided to independent verifiers by CSP. Long-term shared authentication secrets, if used, are never revealed to any other party except Verifiers operated by the Credential Service Provider (CSP); however, session (temporary) shared secrets may be provided to independent Verifiers by the CSP. In addition to Level 1 requirements, assertions are resistant to disclosure, redirection, capture and substitution attacks. Approved cryptographic techniques are required.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAP2, 
        /**
         * Indicator of the medium digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]

                        Medium authentication process level of assurance indicates that the token can be unlocked with password, biometric, or uses a secure multi-token authentication protocol to establish two-factor authentication.  Long-term shared authentication secrets are never revealed to any party except the Claimant and Credential Service Provider (CSP).

                        Authentication requires that the Claimant prove, through a secure authentication protocol, that he or she controls the token. The Claimant unlocks the token with a password or biometric, or uses a secure multi-token authentication protocol to establish two-factor authentication (through proof of possession of a physical or software token in combination with some memorized secret knowledge). Long-term shared authentication secrets, if used, are never revealed to any party except the Claimant and Verifiers operated directly by the CSP; however, session (temporary) shared secrets may be provided to independent Verifiers by the CSP. In addition to Level 2 requirements, assertions are protected against repudiation by the Verifier.
         */
        LOAAP3, 
        /**
         * Indicator of the high digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]

                        High authentication process level of assurance indicates all sensitive data transfer are cryptographically authenticated using keys bound to the authentication process.  Level 4 requires strong cryptographic authentication of all communicating parties and all sensitive data transfers between the parties. Either public key or symmetric key technology may be used. Authentication requires that the Claimant prove through a secure authentication protocol that he or she controls the token. All protocol threats at Level 3 are required to be prevented at Level 4. Protocols shall also be strongly resistant to man-in-the-middle attacks. Long-term shared authentication secrets, if used, are never revealed to any party except the Claimant and Verifiers operated directly by the CSP; however, session (temporary) shared secrets may be provided to independent Verifiers by the CSP. Approved cryptographic techniques are used for all operations. All sensitive data transfers are cryptographically authenticated using keys bound to the authentication process.   [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAP4, 
        /**
         * The value assigned as the indicator of the high quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.
         */
        LOAAS, 
        /**
         * Indicator of the low quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.

                        Assertions and assertion references require protection from modification and reuse attacks.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAS1, 
        /**
         * Indicator of the basic quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.

                        Assertions are resistant to disclosure, redirection, capture and substitution attacks.  Approved cryptographic techniques are required for all assertion protocols.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAS2, 
        /**
         * Indicator of the medium quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.

                        Assertions are protected against repudiation by the verifier.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAS3, 
        /**
         * Indicator of the high quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.

                        Strongly resistant to man-in-the-middle attacks. "Bearer" assertions are not used.  "Holder-of-key" assertions may be used. RP maintains records of the assertions.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]
         */
        LOAAS4, 
        /**
         * Indicator of the digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and its binding to an identity. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOACM, 
        /**
         * Indicator of the low digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and its binding to an identity. Little or no confidence that an individual has maintained control over a token that has been entrusted to him or her and that that token has not been compromised. Characteristics include weak identity binding to tokens and plaintext passwords or secrets not transmitted across a network. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOACM1, 
        /**
         * Indicator of the basic digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and its binding to an identity.  Some confidence that an individual has maintained control over a token that has been entrusted to him or her and that that token has not been compromised. Characteristics include:  Verification must prove claimant controls the token; token resists online guessing, replay, session hijacking, and eavesdropping attacks; and  token is at least weakly resistant to man-in-the middle attacks. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOACM2, 
        /**
         * Indicator of the medium digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and itâ€™s binding to an identity.  High confidence that an individual has maintained control over a token that has been entrusted to him or her and that that token has not been compromised. Characteristics  include: Ownership of token verifiable through security authentication protocol and credential management protects against verifier impersonation attacks. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOACM3, 
        /**
         * Indicator of the high digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and itâ€™s binding to an identity.  Very high confidence that an individual has maintained control over a token that has been entrusted to him or her and that that token has not been compromised. Characteristics include: Verifier can prove control of token through a secure protocol; credential management supports strong cryptographic authentication of all communication parties. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOACM4, 
        /**
         * Indicator of the quality or reliability in the process of ascertaining that an individual is who he or she claims to be.
         */
        LOAID, 
        /**
         * Indicator of low digital quality or reliability in the process of ascertaining that an individual is who he or she claims to be.  Requires that a continuity of identity be maintained but does not require identity proofing. [Based on Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOAID1, 
        /**
         * Indicator of some digital quality or reliability in the process of ascertaining that that an individual is who he or she claims to be. Requires identity proofing via presentation of identifying material or information. [Based on Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOAID2, 
        /**
         * Indicator of high digital quality or reliability in the process of ascertaining that an individual is who he or she claims to be.  Requires identity proofing procedures for verification of identifying materials and information. [Based on Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOAID3, 
        /**
         * Indicator of high digital quality or reliability in the process of ascertaining that an individual is who he or she claims to be.  Requires identity proofing procedures for verification of identifying materials and information. [Based on Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOAID4, 
        /**
         * Indicator of the digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]
         */
        LOANR, 
        /**
         * Indicator of low digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]
         */
        LOANR1, 
        /**
         * Indicator of basic digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]
         */
        LOANR2, 
        /**
         * Indicator of medium digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]
         */
        LOANR3, 
        /**
         * Indicator of high digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]
         */
        LOANR4, 
        /**
         * Indicator of the digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organizationâ€™s security controls. [Based on NIST SP 800-63-2]
         */
        LOARA, 
        /**
         * Indicator of low digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organizationâ€™s security controls. [Based on NIST SP 800-63-2]
         */
        LOARA1, 
        /**
         * Indicator of basic digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organizationâ€™s security controls. [Based on NIST SP 800-63-2]
         */
        LOARA2, 
        /**
         * Indicator of medium digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organizationâ€™s security controls. [Based on NIST SP 800-63-2]
         */
        LOARA3, 
        /**
         * Indicator of high digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organization's security controls. [Based on NIST SP 800-63-2]
         */
        LOARA4, 
        /**
         * Indicator of the digital quality or reliability of single and multi-token authentication. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOATK, 
        /**
         * Indicator of the low digital quality or reliability of single and multi-token authentication. Permits the use of any of the token methods of Levels 2, 3, or 4. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOATK1, 
        /**
         * Indicator of the basic digital quality or reliability of single and multi-token authentication. Requires single factor authentication using memorized secret tokens, pre-registered knowledge tokens, look-up secret tokens, out of band tokens, or single factor one-time password devices. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOATK2, 
        /**
         * Indicator of the medium digital quality or reliability of single and multi-token authentication. Requires two authentication factors. Provides multi-factor remote network authentication. Permits multi-factor software cryptographic token. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOATK3, 
        /**
         * Indicator of the high digital quality or reliability of single and multi-token authentication. Requires token that is a hardware cryptographic module validated at validated at Federal Information Processing Standard (FIPS) 140-2 Level 2 or higher overall with at least FIPS 140-2 Level 3 physical security. Level 4 token requirements can be met by using the PIV authentication key of a FIPS 201 compliant Personal Identity Verification (PIV) Card.  [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]
         */
        LOATK4, 
        /**
         * Values for security trust mechanism metadata observation made about a security architecture system component that supports enforcement of security policies.
         */
        TRSTMECOBV, 
        /**
         * Potential values for observations of severity.
         */
        _SEVERITYOBSERVATION, 
        /**
         * Indicates the condition may be life-threatening or has the potential to cause permanent injury.
         */
        H, 
        /**
         * Indicates the condition may result in some adverse consequences but is unlikely to substantially affect the situation of the subject.
         */
        L, 
        /**
         * Indicates the condition may result in noticable adverse adverse consequences but is unlikely to be life-threatening or cause permanent injury.
         */
        M, 
        /**
         * Contains codes for defining the observed, physical position of a subject, such as during an observation, assessment, collection of a specimen, etc.  ECG waveforms and vital signs, such as blood pressure, are two examples where a general, observed position typically needs to be noted.
         */
        _SUBJECTBODYPOSITION, 
        /**
         * Lying on the left side.
         */
        LLD, 
        /**
         * Lying with the front or ventral surface downward; lying face down.
         */
        PRN, 
        /**
         * Lying on the right side.
         */
        RLD, 
        /**
         * A semi-sitting position in bed with the head of the bed elevated approximately 45 degrees.
         */
        SFWL, 
        /**
         * Resting the body on the buttocks, typically with upper torso erect or semi erect.
         */
        SIT, 
        /**
         * To be stationary, upright, vertical, on one's legs.
         */
        STN, 
        /**
         * supine
         */
        SUP, 
        /**
         * Lying on the back, on an inclined plane, typically about 30-45 degrees with head raised and feet lowered.
         */
        RTRD, 
        /**
         * Lying on the back, on an inclined plane, typically about 30-45 degrees, with  head lowered and feet raised.
         */
        TRD, 
        /**
         * Values for observations of verification act results

                        
                           Examples: Verified, not verified, verified with warning.
         */
        _VERIFICATIONOUTCOMEVALUE, 
        /**
         * Definition: Coverage is in effect for healthcare service(s) and/or product(s).
         */
        ACT, 
        /**
         * Definition: Coverage is in effect for healthcare service(s) and/or product(s) - Pending Investigation
         */
        ACTPEND, 
        /**
         * Definition: Coverage is in effect for healthcare service(s) and/or product(s).
         */
        ELG, 
        /**
         * Definition: Coverage is not in effect for healthcare service(s) and/or product(s).
         */
        INACT, 
        /**
         * Definition: Coverage is not in effect for healthcare service(s) and/or product(s) - Pending Investigation.
         */
        INPNDINV, 
        /**
         * Definition: Coverage is not in effect for healthcare service(s) and/or product(s) - Pending Eligibility Update.
         */
        INPNDUPD, 
        /**
         * Definition: Coverage is not in effect for healthcare service(s) and/or product(s). May optionally include reasons for the ineligibility.
         */
        NELG, 
        /**
         * AnnotationValue
         */
        _ANNOTATIONVALUE, 
        /**
         * Description:Used in a patient care message to value simple clinical (non-lab) observations.
         */
        _COMMONCLINICALOBSERVATIONVALUE, 
        /**
         * This domain is established as a parent to a variety of value domains being defined to support the communication of Individual Case Safety Reports to regulatory bodies. Arguably, this aggregation is not taxonomically pure, but the grouping will facilitate the management of these domains.
         */
        _INDIVIDUALCASESAFETYREPORTVALUEDOMAINS, 
        /**
         * Indicates the specific observation result which is the reason for the action (prescription, lab test, etc.). E.g. Headache, Ear infection, planned diagnostic image (requiring contrast agent), etc.
         */
        _INDICATIONVALUE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ObservationValue fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_ActCoverageAssessmentObservationValue".equals(codeString))
          return _ACTCOVERAGEASSESSMENTOBSERVATIONVALUE;
        if ("_ActFinancialStatusObservationValue".equals(codeString))
          return _ACTFINANCIALSTATUSOBSERVATIONVALUE;
        if ("ASSET".equals(codeString))
          return ASSET;
        if ("ANNUITY".equals(codeString))
          return ANNUITY;
        if ("PROP".equals(codeString))
          return PROP;
        if ("RETACCT".equals(codeString))
          return RETACCT;
        if ("TRUST".equals(codeString))
          return TRUST;
        if ("INCOME".equals(codeString))
          return INCOME;
        if ("CHILD".equals(codeString))
          return CHILD;
        if ("DISABL".equals(codeString))
          return DISABL;
        if ("INVEST".equals(codeString))
          return INVEST;
        if ("PAY".equals(codeString))
          return PAY;
        if ("RETIRE".equals(codeString))
          return RETIRE;
        if ("SPOUSAL".equals(codeString))
          return SPOUSAL;
        if ("SUPPLE".equals(codeString))
          return SUPPLE;
        if ("TAX".equals(codeString))
          return TAX;
        if ("LIVEXP".equals(codeString))
          return LIVEXP;
        if ("CLOTH".equals(codeString))
          return CLOTH;
        if ("FOOD".equals(codeString))
          return FOOD;
        if ("HEALTH".equals(codeString))
          return HEALTH;
        if ("HOUSE".equals(codeString))
          return HOUSE;
        if ("LEGAL".equals(codeString))
          return LEGAL;
        if ("MORTG".equals(codeString))
          return MORTG;
        if ("RENT".equals(codeString))
          return RENT;
        if ("SUNDRY".equals(codeString))
          return SUNDRY;
        if ("TRANS".equals(codeString))
          return TRANS;
        if ("UTIL".equals(codeString))
          return UTIL;
        if ("ELSTAT".equals(codeString))
          return ELSTAT;
        if ("ADOPT".equals(codeString))
          return ADOPT;
        if ("BTHCERT".equals(codeString))
          return BTHCERT;
        if ("CCOC".equals(codeString))
          return CCOC;
        if ("DRLIC".equals(codeString))
          return DRLIC;
        if ("FOSTER".equals(codeString))
          return FOSTER;
        if ("MEMBER".equals(codeString))
          return MEMBER;
        if ("MIL".equals(codeString))
          return MIL;
        if ("MRGCERT".equals(codeString))
          return MRGCERT;
        if ("PASSPORT".equals(codeString))
          return PASSPORT;
        if ("STUDENRL".equals(codeString))
          return STUDENRL;
        if ("HLSTAT".equals(codeString))
          return HLSTAT;
        if ("DISABLE".equals(codeString))
          return DISABLE;
        if ("DRUG".equals(codeString))
          return DRUG;
        if ("IVDRG".equals(codeString))
          return IVDRG;
        if ("PGNT".equals(codeString))
          return PGNT;
        if ("LIVDEP".equals(codeString))
          return LIVDEP;
        if ("RELDEP".equals(codeString))
          return RELDEP;
        if ("SPSDEP".equals(codeString))
          return SPSDEP;
        if ("URELDEP".equals(codeString))
          return URELDEP;
        if ("LIVSIT".equals(codeString))
          return LIVSIT;
        if ("ALONE".equals(codeString))
          return ALONE;
        if ("DEPCHD".equals(codeString))
          return DEPCHD;
        if ("DEPSPS".equals(codeString))
          return DEPSPS;
        if ("DEPYGCHD".equals(codeString))
          return DEPYGCHD;
        if ("FAM".equals(codeString))
          return FAM;
        if ("RELAT".equals(codeString))
          return RELAT;
        if ("SPS".equals(codeString))
          return SPS;
        if ("UNREL".equals(codeString))
          return UNREL;
        if ("SOECSTAT".equals(codeString))
          return SOECSTAT;
        if ("ABUSE".equals(codeString))
          return ABUSE;
        if ("HMLESS".equals(codeString))
          return HMLESS;
        if ("ILGIM".equals(codeString))
          return ILGIM;
        if ("INCAR".equals(codeString))
          return INCAR;
        if ("PROB".equals(codeString))
          return PROB;
        if ("REFUG".equals(codeString))
          return REFUG;
        if ("UNEMPL".equals(codeString))
          return UNEMPL;
        if ("_AllergyTestValue".equals(codeString))
          return _ALLERGYTESTVALUE;
        if ("A0".equals(codeString))
          return A0;
        if ("A1".equals(codeString))
          return A1;
        if ("A2".equals(codeString))
          return A2;
        if ("A3".equals(codeString))
          return A3;
        if ("A4".equals(codeString))
          return A4;
        if ("_CompositeMeasureScoring".equals(codeString))
          return _COMPOSITEMEASURESCORING;
        if ("ALLORNONESCR".equals(codeString))
          return ALLORNONESCR;
        if ("LINEARSCR".equals(codeString))
          return LINEARSCR;
        if ("OPPORSCR".equals(codeString))
          return OPPORSCR;
        if ("WEIGHTSCR".equals(codeString))
          return WEIGHTSCR;
        if ("_CoverageLimitObservationValue".equals(codeString))
          return _COVERAGELIMITOBSERVATIONVALUE;
        if ("_CoverageLevelObservationValue".equals(codeString))
          return _COVERAGELEVELOBSERVATIONVALUE;
        if ("ADC".equals(codeString))
          return ADC;
        if ("CHD".equals(codeString))
          return CHD;
        if ("DEP".equals(codeString))
          return DEP;
        if ("DP".equals(codeString))
          return DP;
        if ("ECH".equals(codeString))
          return ECH;
        if ("FLY".equals(codeString))
          return FLY;
        if ("IND".equals(codeString))
          return IND;
        if ("SSP".equals(codeString))
          return SSP;
        if ("_CriticalityObservationValue".equals(codeString))
          return _CRITICALITYOBSERVATIONVALUE;
        if ("CRITH".equals(codeString))
          return CRITH;
        if ("CRITL".equals(codeString))
          return CRITL;
        if ("CRITU".equals(codeString))
          return CRITU;
        if ("_GeneticObservationValue".equals(codeString))
          return _GENETICOBSERVATIONVALUE;
        if ("Homozygote".equals(codeString))
          return HOMOZYGOTE;
        if ("_ObservationMeasureScoring".equals(codeString))
          return _OBSERVATIONMEASURESCORING;
        if ("COHORT".equals(codeString))
          return COHORT;
        if ("CONTVAR".equals(codeString))
          return CONTVAR;
        if ("PROPOR".equals(codeString))
          return PROPOR;
        if ("RATIO".equals(codeString))
          return RATIO;
        if ("_ObservationMeasureType".equals(codeString))
          return _OBSERVATIONMEASURETYPE;
        if ("COMPOSITE".equals(codeString))
          return COMPOSITE;
        if ("EFFICIENCY".equals(codeString))
          return EFFICIENCY;
        if ("EXPERIENCE".equals(codeString))
          return EXPERIENCE;
        if ("OUTCOME".equals(codeString))
          return OUTCOME;
        if ("PROCESS".equals(codeString))
          return PROCESS;
        if ("RESOURCE".equals(codeString))
          return RESOURCE;
        if ("STRUCTURE".equals(codeString))
          return STRUCTURE;
        if ("_ObservationPopulationInclusion".equals(codeString))
          return _OBSERVATIONPOPULATIONINCLUSION;
        if ("DENEX".equals(codeString))
          return DENEX;
        if ("DENEXCEP".equals(codeString))
          return DENEXCEP;
        if ("DENOM".equals(codeString))
          return DENOM;
        if ("IP".equals(codeString))
          return IP;
        if ("IPP".equals(codeString))
          return IPP;
        if ("MSRPOPL".equals(codeString))
          return MSRPOPL;
        if ("NUMER".equals(codeString))
          return NUMER;
        if ("NUMEX".equals(codeString))
          return NUMEX;
        if ("_PartialCompletionScale".equals(codeString))
          return _PARTIALCOMPLETIONSCALE;
        if ("G".equals(codeString))
          return G;
        if ("LE".equals(codeString))
          return LE;
        if ("ME".equals(codeString))
          return ME;
        if ("MI".equals(codeString))
          return MI;
        if ("N".equals(codeString))
          return N;
        if ("S".equals(codeString))
          return S;
        if ("_SecurityObservationValue".equals(codeString))
          return _SECURITYOBSERVATIONVALUE;
        if ("_SECINTOBV".equals(codeString))
          return _SECINTOBV;
        if ("_SECALTINTOBV".equals(codeString))
          return _SECALTINTOBV;
        if ("ABSTRED".equals(codeString))
          return ABSTRED;
        if ("AGGRED".equals(codeString))
          return AGGRED;
        if ("ANONYED".equals(codeString))
          return ANONYED;
        if ("MAPPED".equals(codeString))
          return MAPPED;
        if ("MASKED".equals(codeString))
          return MASKED;
        if ("PSEUDED".equals(codeString))
          return PSEUDED;
        if ("REDACTED".equals(codeString))
          return REDACTED;
        if ("SUBSETTED".equals(codeString))
          return SUBSETTED;
        if ("SYNTAC".equals(codeString))
          return SYNTAC;
        if ("TRSLT".equals(codeString))
          return TRSLT;
        if ("VERSIONED".equals(codeString))
          return VERSIONED;
        if ("_SECDATINTOBV".equals(codeString))
          return _SECDATINTOBV;
        if ("CRYTOHASH".equals(codeString))
          return CRYTOHASH;
        if ("DIGSIG".equals(codeString))
          return DIGSIG;
        if ("_SECINTCONOBV".equals(codeString))
          return _SECINTCONOBV;
        if ("HRELIABLE".equals(codeString))
          return HRELIABLE;
        if ("RELIABLE".equals(codeString))
          return RELIABLE;
        if ("UNCERTREL".equals(codeString))
          return UNCERTREL;
        if ("UNRELIABLE".equals(codeString))
          return UNRELIABLE;
        if ("_SECINTPRVOBV".equals(codeString))
          return _SECINTPRVOBV;
        if ("_SECINTPRVABOBV".equals(codeString))
          return _SECINTPRVABOBV;
        if ("CLINAST".equals(codeString))
          return CLINAST;
        if ("DEVAST".equals(codeString))
          return DEVAST;
        if ("HCPAST".equals(codeString))
          return HCPAST;
        if ("PACQAST".equals(codeString))
          return PACQAST;
        if ("PATAST".equals(codeString))
          return PATAST;
        if ("PAYAST".equals(codeString))
          return PAYAST;
        if ("PROAST".equals(codeString))
          return PROAST;
        if ("SDMAST".equals(codeString))
          return SDMAST;
        if ("_SECINTPRVRBOBV".equals(codeString))
          return _SECINTPRVRBOBV;
        if ("CLINRPT".equals(codeString))
          return CLINRPT;
        if ("DEVRPT".equals(codeString))
          return DEVRPT;
        if ("HCPRPT".equals(codeString))
          return HCPRPT;
        if ("PACQRPT".equals(codeString))
          return PACQRPT;
        if ("PATRPT".equals(codeString))
          return PATRPT;
        if ("PAYRPT".equals(codeString))
          return PAYRPT;
        if ("PRORPT".equals(codeString))
          return PRORPT;
        if ("SDMRPT".equals(codeString))
          return SDMRPT;
        if ("SECTRSTOBV".equals(codeString))
          return SECTRSTOBV;
        if ("TRSTACCRDOBV".equals(codeString))
          return TRSTACCRDOBV;
        if ("TRSTAGREOBV".equals(codeString))
          return TRSTAGREOBV;
        if ("TRSTCERTOBV".equals(codeString))
          return TRSTCERTOBV;
        if ("TRSTLOAOBV".equals(codeString))
          return TRSTLOAOBV;
        if ("LOAAN".equals(codeString))
          return LOAAN;
        if ("LOAAN1".equals(codeString))
          return LOAAN1;
        if ("LOAAN2".equals(codeString))
          return LOAAN2;
        if ("LOAAN3".equals(codeString))
          return LOAAN3;
        if ("LOAAN4".equals(codeString))
          return LOAAN4;
        if ("LOAAP".equals(codeString))
          return LOAAP;
        if ("LOAAP1".equals(codeString))
          return LOAAP1;
        if ("LOAAP2".equals(codeString))
          return LOAAP2;
        if ("LOAAP3".equals(codeString))
          return LOAAP3;
        if ("LOAAP4".equals(codeString))
          return LOAAP4;
        if ("LOAAS".equals(codeString))
          return LOAAS;
        if ("LOAAS1".equals(codeString))
          return LOAAS1;
        if ("LOAAS2".equals(codeString))
          return LOAAS2;
        if ("LOAAS3".equals(codeString))
          return LOAAS3;
        if ("LOAAS4".equals(codeString))
          return LOAAS4;
        if ("LOACM".equals(codeString))
          return LOACM;
        if ("LOACM1".equals(codeString))
          return LOACM1;
        if ("LOACM2".equals(codeString))
          return LOACM2;
        if ("LOACM3".equals(codeString))
          return LOACM3;
        if ("LOACM4".equals(codeString))
          return LOACM4;
        if ("LOAID".equals(codeString))
          return LOAID;
        if ("LOAID1".equals(codeString))
          return LOAID1;
        if ("LOAID2".equals(codeString))
          return LOAID2;
        if ("LOAID3".equals(codeString))
          return LOAID3;
        if ("LOAID4".equals(codeString))
          return LOAID4;
        if ("LOANR".equals(codeString))
          return LOANR;
        if ("LOANR1".equals(codeString))
          return LOANR1;
        if ("LOANR2".equals(codeString))
          return LOANR2;
        if ("LOANR3".equals(codeString))
          return LOANR3;
        if ("LOANR4".equals(codeString))
          return LOANR4;
        if ("LOARA".equals(codeString))
          return LOARA;
        if ("LOARA1".equals(codeString))
          return LOARA1;
        if ("LOARA2".equals(codeString))
          return LOARA2;
        if ("LOARA3".equals(codeString))
          return LOARA3;
        if ("LOARA4".equals(codeString))
          return LOARA4;
        if ("LOATK".equals(codeString))
          return LOATK;
        if ("LOATK1".equals(codeString))
          return LOATK1;
        if ("LOATK2".equals(codeString))
          return LOATK2;
        if ("LOATK3".equals(codeString))
          return LOATK3;
        if ("LOATK4".equals(codeString))
          return LOATK4;
        if ("TRSTMECOBV".equals(codeString))
          return TRSTMECOBV;
        if ("_SeverityObservation".equals(codeString))
          return _SEVERITYOBSERVATION;
        if ("H".equals(codeString))
          return H;
        if ("L".equals(codeString))
          return L;
        if ("M".equals(codeString))
          return M;
        if ("_SubjectBodyPosition".equals(codeString))
          return _SUBJECTBODYPOSITION;
        if ("LLD".equals(codeString))
          return LLD;
        if ("PRN".equals(codeString))
          return PRN;
        if ("RLD".equals(codeString))
          return RLD;
        if ("SFWL".equals(codeString))
          return SFWL;
        if ("SIT".equals(codeString))
          return SIT;
        if ("STN".equals(codeString))
          return STN;
        if ("SUP".equals(codeString))
          return SUP;
        if ("RTRD".equals(codeString))
          return RTRD;
        if ("TRD".equals(codeString))
          return TRD;
        if ("_VerificationOutcomeValue".equals(codeString))
          return _VERIFICATIONOUTCOMEVALUE;
        if ("ACT".equals(codeString))
          return ACT;
        if ("ACTPEND".equals(codeString))
          return ACTPEND;
        if ("ELG".equals(codeString))
          return ELG;
        if ("INACT".equals(codeString))
          return INACT;
        if ("INPNDINV".equals(codeString))
          return INPNDINV;
        if ("INPNDUPD".equals(codeString))
          return INPNDUPD;
        if ("NELG".equals(codeString))
          return NELG;
        if ("_AnnotationValue".equals(codeString))
          return _ANNOTATIONVALUE;
        if ("_CommonClinicalObservationValue".equals(codeString))
          return _COMMONCLINICALOBSERVATIONVALUE;
        if ("_IndividualCaseSafetyReportValueDomains".equals(codeString))
          return _INDIVIDUALCASESAFETYREPORTVALUEDOMAINS;
        if ("_IndicationValue".equals(codeString))
          return _INDICATIONVALUE;
        throw new FHIRException("Unknown V3ObservationValue code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ACTCOVERAGEASSESSMENTOBSERVATIONVALUE: return "_ActCoverageAssessmentObservationValue";
            case _ACTFINANCIALSTATUSOBSERVATIONVALUE: return "_ActFinancialStatusObservationValue";
            case ASSET: return "ASSET";
            case ANNUITY: return "ANNUITY";
            case PROP: return "PROP";
            case RETACCT: return "RETACCT";
            case TRUST: return "TRUST";
            case INCOME: return "INCOME";
            case CHILD: return "CHILD";
            case DISABL: return "DISABL";
            case INVEST: return "INVEST";
            case PAY: return "PAY";
            case RETIRE: return "RETIRE";
            case SPOUSAL: return "SPOUSAL";
            case SUPPLE: return "SUPPLE";
            case TAX: return "TAX";
            case LIVEXP: return "LIVEXP";
            case CLOTH: return "CLOTH";
            case FOOD: return "FOOD";
            case HEALTH: return "HEALTH";
            case HOUSE: return "HOUSE";
            case LEGAL: return "LEGAL";
            case MORTG: return "MORTG";
            case RENT: return "RENT";
            case SUNDRY: return "SUNDRY";
            case TRANS: return "TRANS";
            case UTIL: return "UTIL";
            case ELSTAT: return "ELSTAT";
            case ADOPT: return "ADOPT";
            case BTHCERT: return "BTHCERT";
            case CCOC: return "CCOC";
            case DRLIC: return "DRLIC";
            case FOSTER: return "FOSTER";
            case MEMBER: return "MEMBER";
            case MIL: return "MIL";
            case MRGCERT: return "MRGCERT";
            case PASSPORT: return "PASSPORT";
            case STUDENRL: return "STUDENRL";
            case HLSTAT: return "HLSTAT";
            case DISABLE: return "DISABLE";
            case DRUG: return "DRUG";
            case IVDRG: return "IVDRG";
            case PGNT: return "PGNT";
            case LIVDEP: return "LIVDEP";
            case RELDEP: return "RELDEP";
            case SPSDEP: return "SPSDEP";
            case URELDEP: return "URELDEP";
            case LIVSIT: return "LIVSIT";
            case ALONE: return "ALONE";
            case DEPCHD: return "DEPCHD";
            case DEPSPS: return "DEPSPS";
            case DEPYGCHD: return "DEPYGCHD";
            case FAM: return "FAM";
            case RELAT: return "RELAT";
            case SPS: return "SPS";
            case UNREL: return "UNREL";
            case SOECSTAT: return "SOECSTAT";
            case ABUSE: return "ABUSE";
            case HMLESS: return "HMLESS";
            case ILGIM: return "ILGIM";
            case INCAR: return "INCAR";
            case PROB: return "PROB";
            case REFUG: return "REFUG";
            case UNEMPL: return "UNEMPL";
            case _ALLERGYTESTVALUE: return "_AllergyTestValue";
            case A0: return "A0";
            case A1: return "A1";
            case A2: return "A2";
            case A3: return "A3";
            case A4: return "A4";
            case _COMPOSITEMEASURESCORING: return "_CompositeMeasureScoring";
            case ALLORNONESCR: return "ALLORNONESCR";
            case LINEARSCR: return "LINEARSCR";
            case OPPORSCR: return "OPPORSCR";
            case WEIGHTSCR: return "WEIGHTSCR";
            case _COVERAGELIMITOBSERVATIONVALUE: return "_CoverageLimitObservationValue";
            case _COVERAGELEVELOBSERVATIONVALUE: return "_CoverageLevelObservationValue";
            case ADC: return "ADC";
            case CHD: return "CHD";
            case DEP: return "DEP";
            case DP: return "DP";
            case ECH: return "ECH";
            case FLY: return "FLY";
            case IND: return "IND";
            case SSP: return "SSP";
            case _CRITICALITYOBSERVATIONVALUE: return "_CriticalityObservationValue";
            case CRITH: return "CRITH";
            case CRITL: return "CRITL";
            case CRITU: return "CRITU";
            case _GENETICOBSERVATIONVALUE: return "_GeneticObservationValue";
            case HOMOZYGOTE: return "Homozygote";
            case _OBSERVATIONMEASURESCORING: return "_ObservationMeasureScoring";
            case COHORT: return "COHORT";
            case CONTVAR: return "CONTVAR";
            case PROPOR: return "PROPOR";
            case RATIO: return "RATIO";
            case _OBSERVATIONMEASURETYPE: return "_ObservationMeasureType";
            case COMPOSITE: return "COMPOSITE";
            case EFFICIENCY: return "EFFICIENCY";
            case EXPERIENCE: return "EXPERIENCE";
            case OUTCOME: return "OUTCOME";
            case PROCESS: return "PROCESS";
            case RESOURCE: return "RESOURCE";
            case STRUCTURE: return "STRUCTURE";
            case _OBSERVATIONPOPULATIONINCLUSION: return "_ObservationPopulationInclusion";
            case DENEX: return "DENEX";
            case DENEXCEP: return "DENEXCEP";
            case DENOM: return "DENOM";
            case IP: return "IP";
            case IPP: return "IPP";
            case MSRPOPL: return "MSRPOPL";
            case NUMER: return "NUMER";
            case NUMEX: return "NUMEX";
            case _PARTIALCOMPLETIONSCALE: return "_PartialCompletionScale";
            case G: return "G";
            case LE: return "LE";
            case ME: return "ME";
            case MI: return "MI";
            case N: return "N";
            case S: return "S";
            case _SECURITYOBSERVATIONVALUE: return "_SecurityObservationValue";
            case _SECINTOBV: return "_SECINTOBV";
            case _SECALTINTOBV: return "_SECALTINTOBV";
            case ABSTRED: return "ABSTRED";
            case AGGRED: return "AGGRED";
            case ANONYED: return "ANONYED";
            case MAPPED: return "MAPPED";
            case MASKED: return "MASKED";
            case PSEUDED: return "PSEUDED";
            case REDACTED: return "REDACTED";
            case SUBSETTED: return "SUBSETTED";
            case SYNTAC: return "SYNTAC";
            case TRSLT: return "TRSLT";
            case VERSIONED: return "VERSIONED";
            case _SECDATINTOBV: return "_SECDATINTOBV";
            case CRYTOHASH: return "CRYTOHASH";
            case DIGSIG: return "DIGSIG";
            case _SECINTCONOBV: return "_SECINTCONOBV";
            case HRELIABLE: return "HRELIABLE";
            case RELIABLE: return "RELIABLE";
            case UNCERTREL: return "UNCERTREL";
            case UNRELIABLE: return "UNRELIABLE";
            case _SECINTPRVOBV: return "_SECINTPRVOBV";
            case _SECINTPRVABOBV: return "_SECINTPRVABOBV";
            case CLINAST: return "CLINAST";
            case DEVAST: return "DEVAST";
            case HCPAST: return "HCPAST";
            case PACQAST: return "PACQAST";
            case PATAST: return "PATAST";
            case PAYAST: return "PAYAST";
            case PROAST: return "PROAST";
            case SDMAST: return "SDMAST";
            case _SECINTPRVRBOBV: return "_SECINTPRVRBOBV";
            case CLINRPT: return "CLINRPT";
            case DEVRPT: return "DEVRPT";
            case HCPRPT: return "HCPRPT";
            case PACQRPT: return "PACQRPT";
            case PATRPT: return "PATRPT";
            case PAYRPT: return "PAYRPT";
            case PRORPT: return "PRORPT";
            case SDMRPT: return "SDMRPT";
            case SECTRSTOBV: return "SECTRSTOBV";
            case TRSTACCRDOBV: return "TRSTACCRDOBV";
            case TRSTAGREOBV: return "TRSTAGREOBV";
            case TRSTCERTOBV: return "TRSTCERTOBV";
            case TRSTLOAOBV: return "TRSTLOAOBV";
            case LOAAN: return "LOAAN";
            case LOAAN1: return "LOAAN1";
            case LOAAN2: return "LOAAN2";
            case LOAAN3: return "LOAAN3";
            case LOAAN4: return "LOAAN4";
            case LOAAP: return "LOAAP";
            case LOAAP1: return "LOAAP1";
            case LOAAP2: return "LOAAP2";
            case LOAAP3: return "LOAAP3";
            case LOAAP4: return "LOAAP4";
            case LOAAS: return "LOAAS";
            case LOAAS1: return "LOAAS1";
            case LOAAS2: return "LOAAS2";
            case LOAAS3: return "LOAAS3";
            case LOAAS4: return "LOAAS4";
            case LOACM: return "LOACM";
            case LOACM1: return "LOACM1";
            case LOACM2: return "LOACM2";
            case LOACM3: return "LOACM3";
            case LOACM4: return "LOACM4";
            case LOAID: return "LOAID";
            case LOAID1: return "LOAID1";
            case LOAID2: return "LOAID2";
            case LOAID3: return "LOAID3";
            case LOAID4: return "LOAID4";
            case LOANR: return "LOANR";
            case LOANR1: return "LOANR1";
            case LOANR2: return "LOANR2";
            case LOANR3: return "LOANR3";
            case LOANR4: return "LOANR4";
            case LOARA: return "LOARA";
            case LOARA1: return "LOARA1";
            case LOARA2: return "LOARA2";
            case LOARA3: return "LOARA3";
            case LOARA4: return "LOARA4";
            case LOATK: return "LOATK";
            case LOATK1: return "LOATK1";
            case LOATK2: return "LOATK2";
            case LOATK3: return "LOATK3";
            case LOATK4: return "LOATK4";
            case TRSTMECOBV: return "TRSTMECOBV";
            case _SEVERITYOBSERVATION: return "_SeverityObservation";
            case H: return "H";
            case L: return "L";
            case M: return "M";
            case _SUBJECTBODYPOSITION: return "_SubjectBodyPosition";
            case LLD: return "LLD";
            case PRN: return "PRN";
            case RLD: return "RLD";
            case SFWL: return "SFWL";
            case SIT: return "SIT";
            case STN: return "STN";
            case SUP: return "SUP";
            case RTRD: return "RTRD";
            case TRD: return "TRD";
            case _VERIFICATIONOUTCOMEVALUE: return "_VerificationOutcomeValue";
            case ACT: return "ACT";
            case ACTPEND: return "ACTPEND";
            case ELG: return "ELG";
            case INACT: return "INACT";
            case INPNDINV: return "INPNDINV";
            case INPNDUPD: return "INPNDUPD";
            case NELG: return "NELG";
            case _ANNOTATIONVALUE: return "_AnnotationValue";
            case _COMMONCLINICALOBSERVATIONVALUE: return "_CommonClinicalObservationValue";
            case _INDIVIDUALCASESAFETYREPORTVALUEDOMAINS: return "_IndividualCaseSafetyReportValueDomains";
            case _INDICATIONVALUE: return "_IndicationValue";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ObservationValue";
        }
        public String getDefinition() {
          switch (this) {
            case _ACTCOVERAGEASSESSMENTOBSERVATIONVALUE: return "Codes specify the category of observation, evidence, or document used to assess for services, e.g., discharge planning, or to establish eligibility for coverage under a policy or program. The type of evidence is coded as observation values.";
            case _ACTFINANCIALSTATUSOBSERVATIONVALUE: return "Code specifying financial indicators used to assess or establish eligibility for coverage under a policy or program; e.g., pay stub; tax or income document; asset document; living expenses.";
            case ASSET: return "Codes specifying asset indicators used to assess or establish eligibility for coverage under a policy or program.";
            case ANNUITY: return "Indicator of annuity ownership or status as beneficiary.";
            case PROP: return "Indicator of real property ownership, e.g., deed or real estate contract.";
            case RETACCT: return "Indicator of retirement investment account ownership.";
            case TRUST: return "Indicator of status as trust beneficiary.";
            case INCOME: return "Code specifying income indicators used to assess or establish eligibility for coverage under a policy or program; e.g., pay or pension check, child support payments received or provided, and taxes paid.";
            case CHILD: return "Indicator of child support payments received or provided.";
            case DISABL: return "Indicator of disability income replacement payment.";
            case INVEST: return "Indicator of investment income, e.g., dividend check, annuity payment; real estate rent, investment divestiture proceeds; trust or endowment check.";
            case PAY: return "Indicator of paid employment, e.g., letter of hire, contract, employer letter; copy of pay check or pay stub.";
            case RETIRE: return "Indicator of retirement payment, e.g., pension check.";
            case SPOUSAL: return "Indicator of spousal or partner support payments received or provided; e.g., alimony payment; support stipulations in a divorce settlement.";
            case SUPPLE: return "Indicator of income supplement, e.g., gifting, parental income support; stipend, or grant.";
            case TAX: return "Indicator of tax obligation or payment, e.g., statement of taxable income.";
            case LIVEXP: return "Codes specifying living expense indicators used to assess or establish eligibility for coverage under a policy or program.";
            case CLOTH: return "Indicator of clothing expenses.";
            case FOOD: return "Indicator of transportation expenses.";
            case HEALTH: return "Indicator of health expenses; including medication costs, health service costs, financial participations, and health coverage premiums.";
            case HOUSE: return "Indicator of housing expense, e.g., household appliances, fixtures, furnishings, and maintenance and repairs.";
            case LEGAL: return "Indicator of legal expenses.";
            case MORTG: return "Indicator of mortgage amount, interest, and payments.";
            case RENT: return "Indicator of rental or lease payments.";
            case SUNDRY: return "Indicator of transportation expenses.";
            case TRANS: return "Indicator of transportation expenses, e.g., vehicle payments, vehicle insurance, vehicle fuel, and vehicle maintenance and repairs.";
            case UTIL: return "Indicator of transportation expenses.";
            case ELSTAT: return "Code specifying eligibility indicators used to assess or establish eligibility for coverage under a policy or program eligibility status, e.g., certificates of creditable coverage; student enrollment; adoption, marriage or birth certificate.";
            case ADOPT: return "Indicator of adoption.";
            case BTHCERT: return "Indicator of birth.";
            case CCOC: return "Indicator of creditable coverage.";
            case DRLIC: return "Indicator of driving status.";
            case FOSTER: return "Indicator of foster child status.";
            case MEMBER: return "Indicator of status as covered member under a policy or program, e.g., member id card or coverage document.";
            case MIL: return "Indicator of military status.";
            case MRGCERT: return "Indicator of marriage status.";
            case PASSPORT: return "Indicator of citizenship.";
            case STUDENRL: return "Indicator of student status.";
            case HLSTAT: return "Code specifying non-clinical indicators related to health status used to assess or establish eligibility for coverage under a policy or program, e.g., pregnancy, disability, drug use, mental health issues.";
            case DISABLE: return "Indication of disability.";
            case DRUG: return "Indication of drug use.";
            case IVDRG: return "Indication of IV drug use .";
            case PGNT: return "Non-clinical report of pregnancy.";
            case LIVDEP: return "Code specifying observations related to living dependency, such as dependent upon spouse for activities of daily living.";
            case RELDEP: return "Continued living in private residence requires functional and health care assistance from one or more relatives.";
            case SPSDEP: return "Continued living in private residence requires functional and health care assistance from spouse or life partner.";
            case URELDEP: return "Continued living in private residence requires functional and health care assistance from one or more unrelated persons.";
            case LIVSIT: return "Code specifying observations related to living situation for a person in a private residence.";
            case ALONE: return "Living alone.  Maps to PD1-2   Living arrangement   (IS)   00742 [A]";
            case DEPCHD: return "Living with one or more dependent children requiring moderate supervision.";
            case DEPSPS: return "Living with disabled spouse requiring functional and health care assistance";
            case DEPYGCHD: return "Living with one or more dependent children requiring intensive supervision";
            case FAM: return "Living with family. Maps to PD1-2   Living arrangement   (IS)   00742 [F]";
            case RELAT: return "Living with one or more relatives. Maps to PD1-2   Living arrangement   (IS)   00742 [R]";
            case SPS: return "Living only with spouse or life partner. Maps to PD1-2   Living arrangement   (IS)   00742 [S]";
            case UNREL: return "Living with one or more unrelated persons.";
            case SOECSTAT: return "Code specifying observations or indicators related to socio-economic status used to assess to assess for services, e.g., discharge planning, or to establish eligibility for coverage under a policy or program.";
            case ABUSE: return "Indication of abuse victim.";
            case HMLESS: return "Indication of status as homeless.";
            case ILGIM: return "Indication of status as illegal immigrant.";
            case INCAR: return "Indication of status as incarcerated.";
            case PROB: return "Indication of probation status.";
            case REFUG: return "Indication of refugee status.";
            case UNEMPL: return "Indication of unemployed status.";
            case _ALLERGYTESTVALUE: return "Indicates the result of a particular allergy test.  E.g. Negative, Mild, Moderate, Severe";
            case A0: return "Description:Patient exhibits no reaction to the challenge agent.";
            case A1: return "Description:Patient exhibits a minimal reaction to the challenge agent.";
            case A2: return "Description:Patient exhibits a mild reaction to the challenge agent.";
            case A3: return "Description:Patient exhibits moderate reaction to the challenge agent.";
            case A4: return "Description:Patient exhibits a severe reaction to the challenge agent.";
            case _COMPOSITEMEASURESCORING: return "Observation values that communicate the method used in a quality measure to combine the component measure results included in an composite measure.";
            case ALLORNONESCR: return "Code specifying that the measure uses all-or-nothing scoring. All-or-nothing scoring places an individual in the numerator of the composite measure if and only if they are in the numerator of all component measures in which they are in the denominator.";
            case LINEARSCR: return "Code specifying that the measure uses linear scoring. Linear scoring computes the fraction of component measures in which the individual appears in the numerator, giving equal weight to each component measure.";
            case OPPORSCR: return "Code specifying that the measure uses opportunity-based scoring. In opportunity-based scoring the measure score is determined by combining the denominator and numerator of each component measure to determine an overall composite score.";
            case WEIGHTSCR: return "Code specifying that the measure uses weighted scoring. Weighted scoring assigns a factor to each component measure to weight that measure's contribution to the overall score.";
            case _COVERAGELIMITOBSERVATIONVALUE: return "Description:Coded observation values for coverage limitations, for e.g., types of claims or types of parties covered under a policy or program.";
            case _COVERAGELEVELOBSERVATIONVALUE: return "Description:Coded observation values for types of covered parties under a policy or program based on their personal relationships or employment status.";
            case ADC: return "Description:Child over an age as specified by coverage policy or program, e.g., student, differently abled, and income dependent.";
            case CHD: return "Description:Dependent biological, adopted, foster child as specified by coverage policy or program.";
            case DEP: return "Description:Person requiring functional and/or financial assistance from another person as specified by coverage policy or program.";
            case DP: return "Description:Persons registered as a family unit in a domestic partner registry as specified by law and by coverage policy or program.";
            case ECH: return "Description:An individual employed by an employer who receive remuneration in wages, salary, commission, tips, piece-rates, or pay-in-kind through the employeraTMs payment system (i.e., not a contractor) as specified by coverage policy or program.";
            case FLY: return "Description:As specified by coverage policy or program.";
            case IND: return "Description:Person as specified by coverage policy or program.";
            case SSP: return "Description:A pair of people of the same gender who live together as a family as specified by coverage policy or program, e.g., Naomi and Ruth from the Book of Ruth; Socrates and Alcibiades";
            case _CRITICALITYOBSERVATIONVALUE: return "A clinical judgment as to the worst case result of a future exposure (including substance administration). When the worst case result is assessed to have a life-threatening or organ system threatening potential, it is considered to be of high criticality.";
            case CRITH: return "Worst case result of a future exposure is assessed to be life-threatening or having high potential for organ system failure.";
            case CRITL: return "Worst case result of a future exposure is not assessed to be life-threatening or having high potential for organ system failure.";
            case CRITU: return "Unable to assess the worst case result of a future exposure.";
            case _GENETICOBSERVATIONVALUE: return "Description: The domain contains genetic analysis specific observation values, e.g. Homozygote, Heterozygote, etc.";
            case HOMOZYGOTE: return "Description: An individual having different alleles at one or more loci regarding a specific character";
            case _OBSERVATIONMEASURESCORING: return "Observation values used to indicate the type of scoring (e.g. proportion, ratio) used by a health quality measure.";
            case COHORT: return "A measure in which either short-term cross-section or long-term longitudinal analysis is performed over a group of subjects defined by a set of common properties or defining characteristics (e.g., Male smokers between the ages of 40 and 50 years, exposure to treatment, exposure duration).";
            case CONTVAR: return "A measure score in which each individual value for the measure can fall anywhere along a continuous scale (e.g., mean time to thrombolytics which aggregates the time in minutes from a case presenting with chest pain to the time of administration of thrombolytics).";
            case PROPOR: return "A score derived by dividing the number of cases that meet a criterion for quality (the numerator) by the number of eligible cases within a given time frame (the denominator) where the numerator cases are a subset of the denominator cases (e.g., percentage of eligible women with a mammogram performed in the last year).";
            case RATIO: return "A score that may have a value of zero or greater that is derived by dividing a count of one type of data by a count of another type of data (e.g., the number of patients with central lines who develop infection divided by the number of central line days).";
            case _OBSERVATIONMEASURETYPE: return "Observation values used to indicate what kind of health quality measure is used.";
            case COMPOSITE: return "A measure that is composed from one or more other measures and indicates an overall summary of those measures.";
            case EFFICIENCY: return "A measure related to the efficiency of medical treatment.";
            case EXPERIENCE: return "A measure related to the level of patient engagement or patient experience of care.";
            case OUTCOME: return "A measure that indicates the result of the performance (or non-performance) of a function or process.";
            case PROCESS: return "A measure which focuses on a process which leads to a certain outcome, meaning that a scientific basis exists for believing that the process, when executed well, will increase the probability of achieving a desired outcome.";
            case RESOURCE: return "A measure related to the extent of use of clinical resources or cost of care.";
            case STRUCTURE: return "A measure related to the structure of patient care.";
            case _OBSERVATIONPOPULATIONINCLUSION: return "Observation values used to assert various populations that a subject falls into.";
            case DENEX: return "Patients who should be removed from the eMeasure population and denominator before determining if numerator criteria are met. Denominator exclusions are used in proportion and ratio measures to help narrow the denominator.";
            case DENEXCEP: return "Denominator exceptions are those conditions that should remove a patient, procedure or unit of measurement from the denominator only if the numerator criteria are not met. Denominator exceptions allow for adjustment of the calculated score for those providers with higher risk populations. Denominator exceptions are used only in proportion eMeasures. They are not appropriate for ratio or continuous variable eMeasures.  Denominator exceptions allow for the exercise of clinical judgment and should be specifically defined where capturing the information in a structured manner fits the clinical workflow. Generic denominator exception reasons used in proportion eMeasures fall into three general categories:\r\n\n                        \n                           Medical reasons\n                           Patient reasons\n                           System reasons";
            case DENOM: return "It can be the same as the initial patient population or a subset of the initial patient population to further constrain the population for the purpose of the eMeasure. Different measures within an eMeasure set may have different Denominators. Continuous Variable eMeasures do not have a Denominator, but instead define a Measure Population.";
            case IP: return "The initial population refers to all entities to be evaluated by a specific quality measure who share a common set of specified characteristics within a specific measurement set to which a given measure belongs.";
            case IPP: return "The initial patient population refers to all patients to be evaluated by a specific quality measure who share a common set of specified characteristics within a specific measurement set to which a given measure belongs. Details often include information based upon specific age groups, diagnoses, diagnostic and procedure codes, and enrollment periods.";
            case MSRPOPL: return "Measure population is used only in continuous variable eMeasures. It is a narrative description of the eMeasure population. \n(e.g., all patients seen in the Emergency Department during the measurement period).";
            case NUMER: return "Numerators are used in proportion and ratio eMeasures. In proportion measures the numerator criteria are the processes or outcomes expected for each patient, procedure, or other unit of measurement defined in the denominator. In ratio measures the numerator is related, but not directly derived from the denominator (e.g., a numerator listing the number of central line blood stream infections and a denominator indicating the days per thousand of central line usage in a specific time period).";
            case NUMEX: return "Numerator Exclusions are used only in ratio eMeasures to define instances that should not be included in the numerator data. (e.g., if the number of central line blood stream infections per 1000 catheter days were to exclude infections with a specific bacterium, that bacterium would be listed as a numerator exclusion.)";
            case _PARTIALCOMPLETIONSCALE: return "PartialCompletionScale";
            case G: return "Value for Act.partialCompletionCode attribute that implies 81-99% completion";
            case LE: return "Value for Act.partialCompletionCode attribute that implies 61-80% completion";
            case ME: return "Value for Act.partialCompletionCode attribute that implies 41-60% completion";
            case MI: return "Value for Act.partialCompletionCode attribute that implies 1-20% completion";
            case N: return "Value for Act.partialCompletionCode attribute that implies 0% completion";
            case S: return "Value for Act.partialCompletionCode attribute that implies 21-40% completion";
            case _SECURITYOBSERVATIONVALUE: return "Observation values used to indicate security observation metadata.";
            case _SECINTOBV: return "Abstract security observation values used to indicate security integrity metadata.\r\n\n                        \n                           Examples: Codes conveying integrity status, integrity confidence, and provenance.";
            case _SECALTINTOBV: return "Abstract security metadata observation values used to indicate mechanism used for authorized alteration of an IT resource (data, information object, service, or system capability)";
            case ABSTRED: return "Security metadata observation values used to indicate the use of a more abstract version of the content, e.g., replacing exact value of an age or date field with a range, or remove the left digits of a credit card number or SSN.";
            case AGGRED: return "Security metadata observation values used to indicate the use of an algorithmic combination of actual values with the result of an aggregate function, e.g., average, sum, or count in order to limit disclosure of an IT resource (data, information object, service, or system capability) to the minimum necessary.";
            case ANONYED: return "Security metadata observation value conveying the alteration integrity of an IT resource (data, information object, service, or system capability) by used to indicate the mechanism by which software systems can strip portions of the resource that could allow the identification of the source of the information or the information subject.  No key to relink the data is retained.";
            case MAPPED: return "Security metadata observation value used to indicate that the IT resource semantic content has been transformed from one encoding to another.\r\n\n                        \n                           Usage Note: \"MAP\" code does not indicate the semantic fidelity of the transformed content.\r\n\n                        To indicate semantic fidelity for maps of HL7 to other code systems, this security alteration integrity observation may be further specified using an Act valued with Value Set: MapRelationship (2.16.840.1.113883.1.11.11052).\r\n\n                        Semantic fidelity of the mapped IT Resource may also be indicated using a SecurityIntegrityConfidenceObservation.";
            case MASKED: return "Security metadata observation value conveying the alteration integrity of an IT resource (data, information object, service, or system capability) by indicating the mechanism by which software systems can make data unintelligible (that is, as unreadable and unusable by algorithmically transforming plaintext into ciphertext) such that it can only be accessed or used by authorized users.  An authorized user may be provided a key to decrypt per license or \"shared secret\".\r\n\n                        \n                           Usage Note: \"MASKED\" may be used, per applicable policy, as a flag to indicate to a user or receiver that some portion of an IT resource has been further encrypted, and may be accessed only by an authorized user or receiver to which a decryption key is provided.";
            case PSEUDED: return "Security metadata observation value conveying the alteration integrity of an IT resource (data, information object, service, or system capability), by indicating the mechanism by which software systems can strip portions of the resource that could allow the identification of the source of the information or the information subject.  Custodian may retain a key to relink data necessary to reidentify the information subject.\r\n\n                        \n                           Rationale: Personal data which has been processed to make it impossible to know whose data it is. Used particularly for secondary use of health data. In some cases, it may be possible for authorized individuals to restore the identity of the individual, e.g.,for public health case management.  Based on ISO/TS 25237:2008 Health informaticsâ€”Pseudonymization";
            case REDACTED: return "Security metadata observation value used to indicate the mechanism by which software systems can filter an IT resource (data, information object, service, or system capability) to remove any portion of the resource that is not authorized to be access, used, or disclosed.\r\n\n                        \n                           Usage Note: \"REDACTED\" may be used, per applicable policy, as a flag to indicate to a user or receiver that some portion of an IT resource has filtered and not included in the content accessed or received.";
            case SUBSETTED: return "Metadata observation used to indicate that some information has been removed from the source object when the view this object contains was constructed because of configuration options when the view was created. The content may not be suitable for use as the basis of a record update\r\n\n                        \n                           Usage Note: This is not suitable to be used when information is removed for security reasons - see the code REDACTED for this use.";
            case SYNTAC: return "Security metadata observation value used to indicate that the IT resource syntax has been transformed from one syntactical representation to another.  \r\n\n                        \n                           Usage Note: \"SYNTAC\" code does not indicate the syntactical correctness of the syntactically transformed IT resource.";
            case TRSLT: return "Security metadata observation value used to indicate that the IT resource has been translated from one human language to another.  \r\n\n                        \n                           Usage Note: \"TRSLT\" does not indicate the fidelity of the translation or the languages translated.\r\n\n                        The fidelity of the IT Resource translation may be indicated using a SecurityIntegrityConfidenceObservation.\r\n\n                        To indicate languages, use the Value Set:HumanLanguage (2.16.840.1.113883.1.11.11526)";
            case VERSIONED: return "Security metadata observation value conveying the alteration integrity of an IT resource (data, information object, service, or system capability)  which indicates that the resource only retains versions of an IT resource  for access and use per applicable policy\r\n\n                        \n                           Usage Note: When this code is used, expectation is that the system has removed historical versions of the data that falls outside the time period deemed to be the effective time of the applicable version.";
            case _SECDATINTOBV: return "Abstract security observation values used to indicate data integrity metadata.\r\n\n                        \n                           Examples: Codes conveying the mechanism used to preserve the accuracy and consistency of an IT resource such as a digital signature and a cryptographic hash function.";
            case CRYTOHASH: return "Security metadata observation value used to indicate the mechanism by which software systems can establish that data was not modified in transit.\r\n\n                        \n                           Rationale: This definition is intended to align with the ISO 22600-2 3.3.19 definition of cryptographic checkvalue: Information which is derived by performing a cryptographic transformation (see cryptography) on the data unit.  The derivation of the checkvalue may be performed in one or more steps and is a result of a mathematical function of the key and a data unit. It is usually used to check the integrity of a data unit.\r\n\n                        \n                           Examples: \n                        \r\n\n                        \n                           SHA-1\n                           SHA-2 (Secure Hash Algorithm)";
            case DIGSIG: return "Security metadata observation value used to indicate the mechanism by which software systems use digital signature to establish that data has not been modified.  \r\n\n                        \n                           Rationale: This definition is intended to align with the ISO 22600-2 3.3.26 definition of digital signature:  Data appended to, or a cryptographic transformation (see cryptography) of, a data unit that allows a recipient of the data unit to prove the source and integrity of the data unit and protect against forgery e.g., by the recipient.";
            case _SECINTCONOBV: return "Abstract security observation value used to indicate integrity confidence metadata.\r\n\n                        \n                           Examples: Codes conveying the level of reliability and trustworthiness of an IT resource.";
            case HRELIABLE: return "Security metadata observation value used to indicate that the veracity or trustworthiness of an IT resource (data, information object, service, or system capability) for a specified purpose of use is perceived to be or deemed by policy to be very high.";
            case RELIABLE: return "Security metadata observation value used to indicate that the veracity or trustworthiness of an IT resource (data, information object, service, or system capability) for a specified purpose of use is perceived to be or deemed by policy to be adequate.";
            case UNCERTREL: return "Security metadata observation value used to indicate that the veracity or trustworthiness of an IT resource (data, information object, service, or system capability) for a specified purpose of use is perceived to be or deemed by policy to be uncertain.";
            case UNRELIABLE: return "Security metadata observation value used to indicate that the veracity or trustworthiness of an IT resource (data, information object, service, or system capability) for a specified purpose of use is perceived to be or deemed by policy to be inadequate.";
            case _SECINTPRVOBV: return "Abstract security metadata observation value used to indicate the provenance of an IT resource (data, information object, service, or system capability).\r\n\n                        \n                           Examples: Codes conveying the provenance metadata about the entity reporting an IT resource.";
            case _SECINTPRVABOBV: return "Abstract security provenance metadata observation value used to indicate the entity that asserted an IT resource (data, information object, service, or system capability).\r\n\n                        \n                           Examples: Codes conveying the provenance metadata about the entity asserting the resource.";
            case CLINAST: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a clinician.";
            case DEVAST: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a device.";
            case HCPAST: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a healthcare professional.";
            case PACQAST: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a patient acquaintance.";
            case PATAST: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a patient.";
            case PAYAST: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a payer.";
            case PROAST: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a professional.";
            case SDMAST: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was asserted by a substitute decision maker.";
            case _SECINTPRVRBOBV: return "Abstract security provenance metadata observation value used to indicate the entity that reported the resource (data, information object, service, or system capability).\r\n\n                        \n                           Examples: Codes conveying the provenance metadata about the entity reporting an IT resource.";
            case CLINRPT: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a clinician.";
            case DEVRPT: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a device.";
            case HCPRPT: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a healthcare professional.";
            case PACQRPT: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a patient acquaintance.";
            case PATRPT: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a patient.";
            case PAYRPT: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a payer.";
            case PRORPT: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a professional.";
            case SDMRPT: return "Security provenance metadata observation value used to indicate that an IT resource (data, information object, service, or system capability) was reported by a substitute decision maker.";
            case SECTRSTOBV: return "Observation value used to indicate aspects of trust applicable to an IT resource (data, information object, service, or system capability).";
            case TRSTACCRDOBV: return "Values for security trust accreditation metadata observation made about the formal declaration by an authority or neutral third party that validates the technical, security, trust, and business practice conformance of Trust Agents to facilitate security, interoperability, and trust among participants within a security domain or trust framework.";
            case TRSTAGREOBV: return "Values for security trust agreement metadata observation made about privacy and security requirements with which a security domain must comply. [ISO IEC 10181-1]\n[ISO IEC 10181-1]";
            case TRSTCERTOBV: return "Values for security trust certificate metadata observation made about a set of security-relevant data issued by a security authority or trusted third party, together with security information which is used to provide the integrity and data origin authentication services for an IT resource (data, information object, service, or system capability). [Based on ISO IEC 10181-1]\r\n\n                        For example, a Certificate Policy (CP), which is a named set of rules that indicates the applicability of a certificate to a particular community and/or class of application with common security requirements.  A particular Certificate Policy might indicate the applicability of a type of certificate to the authentication of electronic data interchange transactions for the trading of goods within a given price range.  Another example is Cross Certification with Federal Bridge.";
            case TRSTLOAOBV: return "Values for security trust assurance metadata observation made about the digital quality or reliability of a trust assertion, activity, capability, information exchange, mechanism, process, or protocol.";
            case LOAAN: return "The value assigned as the indicator of the digital quality or reliability of the verification and validation process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2]\r\n\n                        For example, the degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies]";
            case LOAAN1: return "Indicator of low digital quality or reliability of the digital reliability of the verification and validation process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2] \r\n\n                        The degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies] \r\n\n                        Low authentication level of assurance indicates that the relying party may have little or no confidence in the asserted identity's validity. Level 1 requires little or no confidence in the asserted identity. No identity proofing is required at this level, but the authentication mechanism should provide some assurance that the same claimant is accessing the protected transaction or data. A wide range of available authentication technologies can be employed and any of the token methods of Levels 2, 3, or 4, including Personal Identification Numbers (PINs), may be used. To be authenticated, the claimant must prove control of the token through a secure authentication protocol. At Level 1, long-term shared authentication secrets may be revealed to verifiers.  Assertions issued about claimants as a result of a successful authentication are either cryptographically authenticated by relying parties (using approved methods) or are obtained directly from a trusted party via a secure authentication protocol.   [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAN2: return "Indicator of basic digital quality or reliability of the digital reliability of the verification and validation process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2] \r\n\n                        The degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies]\r\n\n                        Basic authentication level of assurance indicates that the relying party may have some confidence in the asserted identity's validity. Level 2 requires confidence that the asserted identity is accurate. Level 2 provides for single-factor remote network authentication, including identity-proofing requirements for presentation of identifying materials or information. A wide range of available authentication technologies can be employed, including any of the token methods of Levels 3 or 4, as well as passwords. Successful authentication requires that the claimant prove through a secure authentication protocol that the claimant controls the token.  Eavesdropper, replay, and online guessing attacks are prevented.  \nLong-term shared authentication secrets, if used, are never revealed to any party except the claimant and verifiers operated by the CSP; however, session (temporary) shared secrets may be provided to independent verifiers by the CSP. Approved cryptographic techniques are required. Assertions issued about claimants as a result of a successful authentication are either cryptographically authenticated by relying parties (using approved methods) or are obtained directly from a trusted party via a secure authentication protocol.   [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAN3: return "Indicator of medium digital quality or reliability of the digital reliability of verification and validation of the process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2] \r\n\n                        The degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies] \r\n\n                        Medium authentication level of assurance indicates that the relying party may have high confidence in the asserted identity's validity.  Level 3 is appropriate for transactions that need high confidence in the accuracy of the asserted identity. Level 3 provides multifactor remote network authentication. At this level, identity-proofing procedures require verification of identifying materials and information. Authentication is based on proof of possession of a key or password through a cryptographic protocol. Cryptographic strength mechanisms should protect the primary authentication token (a cryptographic key) against compromise by the protocol threats, including eavesdropper, replay, online guessing, verifier impersonation, and man-in-the-middle attacks. A minimum of two authentication factors is required. Three kinds of tokens may be used:\r\n\n                        \n                           \"soft\" cryptographic token, which has the key stored on a general-purpose computer, \n                           \"hard\" cryptographic token, which has the key stored on a special hardware device, and \n                           \"one-time password\" device token, which has symmetric key stored on a personal hardware device that is a cryptographic module validated at FIPS 140-2 Level 1 or higher. Validation testing of cryptographic modules and algorithms for conformance to Federal Information Processing Standard (FIPS) 140-2, Security Requirements for Cryptographic Modules, is managed by NIST.\n                        \n                        Authentication requires that the claimant prove control of the token through a secure authentication protocol. The token must be unlocked with a password or biometric representation, or a password must be used in a secure authentication protocol, to establish two-factor authentication. Long-term shared authentication secrets, if used, are never revealed to any party except the claimant and verifiers operated directly by the CSP; however, session (temporary) shared secrets may be provided to independent verifiers by the CSP. Approved cryptographic techniques are used for all operations.  Assertions issued about claimants as a result of a successful authentication are either cryptographically authenticated by relying parties (using approved methods) or are obtained directly from a trusted party via a secure authentication protocol.    [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAN4: return "Indicator of high digital quality or reliability of the digital reliability of the verification and validation process used to verify the claimed identity of an entity by securely associating an identifier and its authenticator. [Based on ISO 7498-2] \r\n\n                        The degree of confidence in the vetting process used to establish the identity of the individual to whom the credential was issued, and 2) the degree of confidence that the individual who uses the credential is the individual to whom the credential was issued. [OMB M-04-04 E-Authentication Guidance for Federal Agencies]\r\n\n                        High authentication level of assurance indicates that the relying party may have very high confidence in the asserted identity's validity. Level 4 is for transactions that need very high confidence in the accuracy of the asserted identity. Level 4 provides the highest practical assurance of remote network authentication. Authentication is based on proof of possession of a key through a cryptographic protocol. This level is similar to Level 3 except that only â€œhardâ€? cryptographic tokens are allowed, cryptographic module validation requirements are strengthened, and subsequent critical data transfers must be authenticated via a key that is bound to the authentication process. The token should be a hardware cryptographic module validated at FIPS 140-2 Level 2 or higher overall with at least FIPS 140-2 Level 3 physical security. This level requires a physical token, which cannot readily be copied, and operator authentication at Level 2 and higher, and ensures good, two-factor remote authentication.\r\n\n                        Level 4 requires strong cryptographic authentication of all parties and all sensitive data transfers between the parties. Either public key or symmetric key technology may be used. Authentication requires that the claimant prove through a secure authentication protocol that the claimant controls the token. Eavesdropper, replay, online guessing, verifier impersonation, and man-in-the-middle attacks are prevented. Long-term shared authentication secrets, if used, are never revealed to any party except the claimant and verifiers operated directly by the CSP; however, session (temporary) shared secrets may be provided to independent verifiers by the CSP. Strong approved cryptographic techniques are used for all operations. All sensitive data transfers are cryptographically authenticated using keys bound to the authentication process.   [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAP: return "The value assigned as the indicator of the digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]";
            case LOAAP1: return "Indicator of the low digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]\r\n\n                        Low authentication process level of assurance indicates that (1) long-term shared authentication secrets may be revealed to verifiers; and (2) assertions and assertion references require protection from manufacture/modification and reuse attacks.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAP2: return "Indicator of the basic digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]\r\n\n                        Basic authentication process level of assurance indicates that long-term shared authentication secrets are never revealed to any other party except Credential Service Provider (CSP).  Sessions (temporary) shared secrets may be provided to independent verifiers by CSP. Long-term shared authentication secrets, if used, are never revealed to any other party except Verifiers operated by the Credential Service Provider (CSP); however, session (temporary) shared secrets may be provided to independent Verifiers by the CSP. In addition to Level 1 requirements, assertions are resistant to disclosure, redirection, capture and substitution attacks. Approved cryptographic techniques are required.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAP3: return "Indicator of the medium digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]\r\n\n                        Medium authentication process level of assurance indicates that the token can be unlocked with password, biometric, or uses a secure multi-token authentication protocol to establish two-factor authentication.  Long-term shared authentication secrets are never revealed to any party except the Claimant and Credential Service Provider (CSP).\r\n\n                        Authentication requires that the Claimant prove, through a secure authentication protocol, that he or she controls the token. The Claimant unlocks the token with a password or biometric, or uses a secure multi-token authentication protocol to establish two-factor authentication (through proof of possession of a physical or software token in combination with some memorized secret knowledge). Long-term shared authentication secrets, if used, are never revealed to any party except the Claimant and Verifiers operated directly by the CSP; however, session (temporary) shared secrets may be provided to independent Verifiers by the CSP. In addition to Level 2 requirements, assertions are protected against repudiation by the Verifier.";
            case LOAAP4: return "Indicator of the high digital quality or reliability of a defined sequence of messages between a Claimant and a Verifier that demonstrates that the Claimant has possession and control of a valid token to establish his/her identity, and optionally, demonstrates to the Claimant that he or she is communicating with the intended Verifier. [Based on NIST SP 800-63-2]\r\n\n                        High authentication process level of assurance indicates all sensitive data transfer are cryptographically authenticated using keys bound to the authentication process.  Level 4 requires strong cryptographic authentication of all communicating parties and all sensitive data transfers between the parties. Either public key or symmetric key technology may be used. Authentication requires that the Claimant prove through a secure authentication protocol that he or she controls the token. All protocol threats at Level 3 are required to be prevented at Level 4. Protocols shall also be strongly resistant to man-in-the-middle attacks. Long-term shared authentication secrets, if used, are never revealed to any party except the Claimant and Verifiers operated directly by the CSP; however, session (temporary) shared secrets may be provided to independent Verifiers by the CSP. Approved cryptographic techniques are used for all operations. All sensitive data transfers are cryptographically authenticated using keys bound to the authentication process.   [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAS: return "The value assigned as the indicator of the high quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.";
            case LOAAS1: return "Indicator of the low quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.\r\n\n                        Assertions and assertion references require protection from modification and reuse attacks.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAS2: return "Indicator of the basic quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.\r\n\n                        Assertions are resistant to disclosure, redirection, capture and substitution attacks.  Approved cryptographic techniques are required for all assertion protocols.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAS3: return "Indicator of the medium quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.\r\n\n                        Assertions are protected against repudiation by the verifier.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOAAS4: return "Indicator of the high quality or reliability of the statement from a Verifier to a Relying Party (RP) that contains identity information about a Subscriber. Assertions may also contain verified attributes.\r\n\n                        Strongly resistant to man-in-the-middle attacks. \"Bearer\" assertions are not used.  \"Holder-of-key\" assertions may be used. RP maintains records of the assertions.  [Summary of the technical requirements specified in NIST SP 800-63 for the four levels of assurance defined by the December 2003, the Office of Management and Budget (OMB) issued Memorandum M-04-04, E-Authentication Guidance for Federal Agencies.]";
            case LOACM: return "Indicator of the digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and its binding to an identity. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOACM1: return "Indicator of the low digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and its binding to an identity. Little or no confidence that an individual has maintained control over a token that has been entrusted to him or her and that that token has not been compromised. Characteristics include weak identity binding to tokens and plaintext passwords or secrets not transmitted across a network. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOACM2: return "Indicator of the basic digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and its binding to an identity.  Some confidence that an individual has maintained control over a token that has been entrusted to him or her and that that token has not been compromised. Characteristics include:  Verification must prove claimant controls the token; token resists online guessing, replay, session hijacking, and eavesdropping attacks; and  token is at least weakly resistant to man-in-the middle attacks. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOACM3: return "Indicator of the medium digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and itâ€™s binding to an identity.  High confidence that an individual has maintained control over a token that has been entrusted to him or her and that that token has not been compromised. Characteristics  include: Ownership of token verifiable through security authentication protocol and credential management protects against verifier impersonation attacks. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOACM4: return "Indicator of the high digital quality or reliability of the activities performed by the Credential Service Provider (CSP) subsequent to electronic authentication registration, identity proofing and issuance activities to manage and safeguard the integrity of an issued credential and itâ€™s binding to an identity.  Very high confidence that an individual has maintained control over a token that has been entrusted to him or her and that that token has not been compromised. Characteristics include: Verifier can prove control of token through a secure protocol; credential management supports strong cryptographic authentication of all communication parties. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOAID: return "Indicator of the quality or reliability in the process of ascertaining that an individual is who he or she claims to be.";
            case LOAID1: return "Indicator of low digital quality or reliability in the process of ascertaining that an individual is who he or she claims to be.  Requires that a continuity of identity be maintained but does not require identity proofing. [Based on Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOAID2: return "Indicator of some digital quality or reliability in the process of ascertaining that that an individual is who he or she claims to be. Requires identity proofing via presentation of identifying material or information. [Based on Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOAID3: return "Indicator of high digital quality or reliability in the process of ascertaining that an individual is who he or she claims to be.  Requires identity proofing procedures for verification of identifying materials and information. [Based on Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOAID4: return "Indicator of high digital quality or reliability in the process of ascertaining that an individual is who he or she claims to be.  Requires identity proofing procedures for verification of identifying materials and information. [Based on Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOANR: return "Indicator of the digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]";
            case LOANR1: return "Indicator of low digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]";
            case LOANR2: return "Indicator of basic digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]";
            case LOANR3: return "Indicator of medium digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]";
            case LOANR4: return "Indicator of high digital quality or reliability in the process of establishing proof of delivery and proof of origin. [Based on ISO 7498-2]";
            case LOARA: return "Indicator of the digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organizationâ€™s security controls. [Based on NIST SP 800-63-2]";
            case LOARA1: return "Indicator of low digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organizationâ€™s security controls. [Based on NIST SP 800-63-2]";
            case LOARA2: return "Indicator of basic digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organizationâ€™s security controls. [Based on NIST SP 800-63-2]";
            case LOARA3: return "Indicator of medium digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organizationâ€™s security controls. [Based on NIST SP 800-63-2]";
            case LOARA4: return "Indicator of high digital quality or reliability of the information exchange between network-connected devices where the information cannot be reliably protected end-to-end by a single organization's security controls. [Based on NIST SP 800-63-2]";
            case LOATK: return "Indicator of the digital quality or reliability of single and multi-token authentication. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOATK1: return "Indicator of the low digital quality or reliability of single and multi-token authentication. Permits the use of any of the token methods of Levels 2, 3, or 4. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOATK2: return "Indicator of the basic digital quality or reliability of single and multi-token authentication. Requires single factor authentication using memorized secret tokens, pre-registered knowledge tokens, look-up secret tokens, out of band tokens, or single factor one-time password devices. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOATK3: return "Indicator of the medium digital quality or reliability of single and multi-token authentication. Requires two authentication factors. Provides multi-factor remote network authentication. Permits multi-factor software cryptographic token. [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case LOATK4: return "Indicator of the high digital quality or reliability of single and multi-token authentication. Requires token that is a hardware cryptographic module validated at validated at Federal Information Processing Standard (FIPS) 140-2 Level 2 or higher overall with at least FIPS 140-2 Level 3 physical security. Level 4 token requirements can be met by using the PIV authentication key of a FIPS 201 compliant Personal Identity Verification (PIV) Card.  [Electronic Authentication Guideline - Recommendations of the National Institute of Standards and Technology, NIST Special Publication 800-63-1, Dec 2011]";
            case TRSTMECOBV: return "Values for security trust mechanism metadata observation made about a security architecture system component that supports enforcement of security policies.";
            case _SEVERITYOBSERVATION: return "Potential values for observations of severity.";
            case H: return "Indicates the condition may be life-threatening or has the potential to cause permanent injury.";
            case L: return "Indicates the condition may result in some adverse consequences but is unlikely to substantially affect the situation of the subject.";
            case M: return "Indicates the condition may result in noticable adverse adverse consequences but is unlikely to be life-threatening or cause permanent injury.";
            case _SUBJECTBODYPOSITION: return "Contains codes for defining the observed, physical position of a subject, such as during an observation, assessment, collection of a specimen, etc.  ECG waveforms and vital signs, such as blood pressure, are two examples where a general, observed position typically needs to be noted.";
            case LLD: return "Lying on the left side.";
            case PRN: return "Lying with the front or ventral surface downward; lying face down.";
            case RLD: return "Lying on the right side.";
            case SFWL: return "A semi-sitting position in bed with the head of the bed elevated approximately 45 degrees.";
            case SIT: return "Resting the body on the buttocks, typically with upper torso erect or semi erect.";
            case STN: return "To be stationary, upright, vertical, on one's legs.";
            case SUP: return "supine";
            case RTRD: return "Lying on the back, on an inclined plane, typically about 30-45 degrees with head raised and feet lowered.";
            case TRD: return "Lying on the back, on an inclined plane, typically about 30-45 degrees, with  head lowered and feet raised.";
            case _VERIFICATIONOUTCOMEVALUE: return "Values for observations of verification act results\r\n\n                        \n                           Examples: Verified, not verified, verified with warning.";
            case ACT: return "Definition: Coverage is in effect for healthcare service(s) and/or product(s).";
            case ACTPEND: return "Definition: Coverage is in effect for healthcare service(s) and/or product(s) - Pending Investigation";
            case ELG: return "Definition: Coverage is in effect for healthcare service(s) and/or product(s).";
            case INACT: return "Definition: Coverage is not in effect for healthcare service(s) and/or product(s).";
            case INPNDINV: return "Definition: Coverage is not in effect for healthcare service(s) and/or product(s) - Pending Investigation.";
            case INPNDUPD: return "Definition: Coverage is not in effect for healthcare service(s) and/or product(s) - Pending Eligibility Update.";
            case NELG: return "Definition: Coverage is not in effect for healthcare service(s) and/or product(s). May optionally include reasons for the ineligibility.";
            case _ANNOTATIONVALUE: return "AnnotationValue";
            case _COMMONCLINICALOBSERVATIONVALUE: return "Description:Used in a patient care message to value simple clinical (non-lab) observations.";
            case _INDIVIDUALCASESAFETYREPORTVALUEDOMAINS: return "This domain is established as a parent to a variety of value domains being defined to support the communication of Individual Case Safety Reports to regulatory bodies. Arguably, this aggregation is not taxonomically pure, but the grouping will facilitate the management of these domains.";
            case _INDICATIONVALUE: return "Indicates the specific observation result which is the reason for the action (prescription, lab test, etc.). E.g. Headache, Ear infection, planned diagnostic image (requiring contrast agent), etc.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ACTCOVERAGEASSESSMENTOBSERVATIONVALUE: return "ActCoverageAssessmentObservationValue";
            case _ACTFINANCIALSTATUSOBSERVATIONVALUE: return "ActFinancialStatusObservationValue";
            case ASSET: return "asset";
            case ANNUITY: return "annuity";
            case PROP: return "real property";
            case RETACCT: return "retirement investment account";
            case TRUST: return "trust";
            case INCOME: return "income";
            case CHILD: return "child support";
            case DISABL: return "disability pay";
            case INVEST: return "investment income";
            case PAY: return "paid employment";
            case RETIRE: return "retirement pay";
            case SPOUSAL: return "spousal or partner support";
            case SUPPLE: return "income supplement";
            case TAX: return "tax obligation";
            case LIVEXP: return "living expense";
            case CLOTH: return "clothing expense";
            case FOOD: return "food expense";
            case HEALTH: return "health expense";
            case HOUSE: return "household expense";
            case LEGAL: return "legal expense";
            case MORTG: return "mortgage";
            case RENT: return "rent";
            case SUNDRY: return "sundry expense";
            case TRANS: return "transportation expense";
            case UTIL: return "utility expense";
            case ELSTAT: return "eligibility indicator";
            case ADOPT: return "adoption document";
            case BTHCERT: return "birth certificate";
            case CCOC: return "creditable coverage document";
            case DRLIC: return "driver license";
            case FOSTER: return "foster child document";
            case MEMBER: return "program or policy member";
            case MIL: return "military identification";
            case MRGCERT: return "marriage certificate";
            case PASSPORT: return "passport";
            case STUDENRL: return "student enrollment";
            case HLSTAT: return "health status";
            case DISABLE: return "disabled";
            case DRUG: return "drug use";
            case IVDRG: return "IV drug use";
            case PGNT: return "pregnant";
            case LIVDEP: return "living dependency";
            case RELDEP: return "relative dependent";
            case SPSDEP: return "spouse dependent";
            case URELDEP: return "unrelated person dependent";
            case LIVSIT: return "living situation";
            case ALONE: return "alone";
            case DEPCHD: return "dependent children";
            case DEPSPS: return "dependent spouse";
            case DEPYGCHD: return "dependent young children";
            case FAM: return "live with family";
            case RELAT: return "relative";
            case SPS: return "spouse only";
            case UNREL: return "unrelated person";
            case SOECSTAT: return "socio economic status";
            case ABUSE: return "abuse victim";
            case HMLESS: return "homeless";
            case ILGIM: return "illegal immigrant";
            case INCAR: return "incarcerated";
            case PROB: return "probation";
            case REFUG: return "refugee";
            case UNEMPL: return "unemployed";
            case _ALLERGYTESTVALUE: return "AllergyTestValue";
            case A0: return "no reaction";
            case A1: return "minimal reaction";
            case A2: return "mild reaction";
            case A3: return "moderate reaction";
            case A4: return "severe reaction";
            case _COMPOSITEMEASURESCORING: return "CompositeMeasureScoring";
            case ALLORNONESCR: return "All-or-nothing Scoring";
            case LINEARSCR: return "Linear Scoring";
            case OPPORSCR: return "Opportunity Scoring";
            case WEIGHTSCR: return "Weighted Scoring";
            case _COVERAGELIMITOBSERVATIONVALUE: return "CoverageLimitObservationValue";
            case _COVERAGELEVELOBSERVATIONVALUE: return "CoverageLevelObservationValue";
            case ADC: return "adult child";
            case CHD: return "child";
            case DEP: return "dependent";
            case DP: return "domestic partner";
            case ECH: return "employee";
            case FLY: return "family coverage";
            case IND: return "individual";
            case SSP: return "same sex partner";
            case _CRITICALITYOBSERVATIONVALUE: return "CriticalityObservationValue";
            case CRITH: return "high criticality";
            case CRITL: return "low criticality";
            case CRITU: return "unable to assess criticality";
            case _GENETICOBSERVATIONVALUE: return "GeneticObservationValue";
            case HOMOZYGOTE: return "HOMO";
            case _OBSERVATIONMEASURESCORING: return "ObservationMeasureScoring";
            case COHORT: return "cohort measure scoring";
            case CONTVAR: return "continuous variable measure scoring";
            case PROPOR: return "proportion measure scoring";
            case RATIO: return "ratio measure scoring";
            case _OBSERVATIONMEASURETYPE: return "ObservationMeasureType";
            case COMPOSITE: return "composite measure type";
            case EFFICIENCY: return "efficiency measure type";
            case EXPERIENCE: return "experience measure type";
            case OUTCOME: return "outcome measure type";
            case PROCESS: return "process measure type";
            case RESOURCE: return "resource use measure type";
            case STRUCTURE: return "structure measure type";
            case _OBSERVATIONPOPULATIONINCLUSION: return "ObservationPopulationInclusion";
            case DENEX: return "denominator exclusions";
            case DENEXCEP: return "denominator exceptions";
            case DENOM: return "denominator";
            case IP: return "initial population";
            case IPP: return "initial patient population";
            case MSRPOPL: return "measure population";
            case NUMER: return "numerator";
            case NUMEX: return "numerator exclusions";
            case _PARTIALCOMPLETIONSCALE: return "PartialCompletionScale";
            case G: return "Great extent";
            case LE: return "Large extent";
            case ME: return "Medium extent";
            case MI: return "Minimal extent";
            case N: return "None";
            case S: return "Some extent";
            case _SECURITYOBSERVATIONVALUE: return "SecurityObservationValue";
            case _SECINTOBV: return "security integrity";
            case _SECALTINTOBV: return "alteration integrity";
            case ABSTRED: return "abstracted";
            case AGGRED: return "aggregated";
            case ANONYED: return "anonymized";
            case MAPPED: return "mapped";
            case MASKED: return "masked";
            case PSEUDED: return "pseudonymized";
            case REDACTED: return "redacted";
            case SUBSETTED: return "subsetted";
            case SYNTAC: return "syntactic transform";
            case TRSLT: return "translated";
            case VERSIONED: return "versioned";
            case _SECDATINTOBV: return "data integrity";
            case CRYTOHASH: return "cryptographic hash function";
            case DIGSIG: return "digital signature";
            case _SECINTCONOBV: return "integrity confidence";
            case HRELIABLE: return "highly reliable";
            case RELIABLE: return "reliable";
            case UNCERTREL: return "uncertain reliability";
            case UNRELIABLE: return "unreliable";
            case _SECINTPRVOBV: return "provenance";
            case _SECINTPRVABOBV: return "provenance asserted by";
            case CLINAST: return "clinician asserted";
            case DEVAST: return "device asserted";
            case HCPAST: return "healthcare professional asserted";
            case PACQAST: return "patient acquaintance asserted";
            case PATAST: return "patient asserted";
            case PAYAST: return "payer asserted";
            case PROAST: return "professional asserted";
            case SDMAST: return "substitute decision maker asserted";
            case _SECINTPRVRBOBV: return "provenance reported by";
            case CLINRPT: return "clinician reported";
            case DEVRPT: return "device reported";
            case HCPRPT: return "healthcare professional reported";
            case PACQRPT: return "patient acquaintance reported";
            case PATRPT: return "patient reported";
            case PAYRPT: return "payer reported";
            case PRORPT: return "professional reported";
            case SDMRPT: return "substitute decision maker reported";
            case SECTRSTOBV: return "security trust observation";
            case TRSTACCRDOBV: return "trust accreditation observation";
            case TRSTAGREOBV: return "trust agreement observation";
            case TRSTCERTOBV: return "trust certificate observation";
            case TRSTLOAOBV: return "trust assurance observation";
            case LOAAN: return "authentication level of assurance value";
            case LOAAN1: return "low authentication level of assurance";
            case LOAAN2: return "basic authentication level of assurance";
            case LOAAN3: return "medium authentication level of assurance";
            case LOAAN4: return "high authentication level of assurance";
            case LOAAP: return "authentication process level of assurance value";
            case LOAAP1: return "low authentication process level of assurance";
            case LOAAP2: return "basic authentication process level of assurance";
            case LOAAP3: return "medium authentication process level of assurance";
            case LOAAP4: return "high authentication process level of assurance";
            case LOAAS: return "assertion level of assurance value";
            case LOAAS1: return "low assertion level of assurance";
            case LOAAS2: return "basic assertion level of assurance";
            case LOAAS3: return "medium assertion level of assurance";
            case LOAAS4: return "high assertion level of assurance";
            case LOACM: return "token and credential management level of assurance value)";
            case LOACM1: return "low token and credential management level of assurance";
            case LOACM2: return "basic token and credential management level of assurance";
            case LOACM3: return "medium token and credential management level of assurance";
            case LOACM4: return "high token and credential management level of assurance";
            case LOAID: return "identity proofing level of assurance";
            case LOAID1: return "low identity proofing level of assurance";
            case LOAID2: return "basic identity proofing level of assurance";
            case LOAID3: return "medium identity proofing level of assurance";
            case LOAID4: return "high identity proofing level of assurance";
            case LOANR: return "non-repudiation level of assurance value";
            case LOANR1: return "low non-repudiation level of assurance";
            case LOANR2: return "basic non-repudiation level of assurance";
            case LOANR3: return "medium non-repudiation level of assurance";
            case LOANR4: return "high non-repudiation level of assurance";
            case LOARA: return "remote access level of assurance value";
            case LOARA1: return "low remote access level of assurance";
            case LOARA2: return "basic remote access level of assurance";
            case LOARA3: return "medium remote access level of assurance";
            case LOARA4: return "high remote access level of assurance";
            case LOATK: return "token level of assurance value";
            case LOATK1: return "low token level of assurance";
            case LOATK2: return "basic token level of assurance";
            case LOATK3: return "medium token level of assurance";
            case LOATK4: return "high token level of assurance";
            case TRSTMECOBV: return "none supplied 6";
            case _SEVERITYOBSERVATION: return "SeverityObservation";
            case H: return "High";
            case L: return "Low";
            case M: return "Moderate";
            case _SUBJECTBODYPOSITION: return "_SubjectBodyPosition";
            case LLD: return "left lateral decubitus";
            case PRN: return "prone";
            case RLD: return "right lateral decubitus";
            case SFWL: return "Semi-Fowler's";
            case SIT: return "sitting";
            case STN: return "standing";
            case SUP: return "supine";
            case RTRD: return "reverse trendelenburg";
            case TRD: return "trendelenburg";
            case _VERIFICATIONOUTCOMEVALUE: return "verification outcome";
            case ACT: return "active coverage";
            case ACTPEND: return "active - pending investigation";
            case ELG: return "eligible";
            case INACT: return "inactive";
            case INPNDINV: return "inactive - pending investigation";
            case INPNDUPD: return "inactive - pending eligibility update";
            case NELG: return "not eligible";
            case _ANNOTATIONVALUE: return "AnnotationValue";
            case _COMMONCLINICALOBSERVATIONVALUE: return "common clinical observation";
            case _INDIVIDUALCASESAFETYREPORTVALUEDOMAINS: return "Individual Case Safety Report Value Domains";
            case _INDICATIONVALUE: return "IndicationValue";
            default: return "?";
          }
    }


}

