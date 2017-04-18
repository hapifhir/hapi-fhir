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

public enum V3ActRelationshipType {

        /**
         * Description: A directed association between a source Act and a target Act.

                        
                           Usage Note: This code should never be transmitted in an instance as the value of ActRelationship.typeCode (attribute)
         */
        ART, 
        /**
         * ActClassTemporallyPertains
         */
        _ACTCLASSTEMPORALLYPERTAINS, 
        /**
         * Codes that describe the relationship between an Act and a financial instrument such as a financial transaction, account or invoice element.
         */
        _ACTRELATIONSHIPACCOUNTING, 
        /**
         * Expresses values for describing the relationship relationship between an InvoiceElement or InvoiceElementGroup and a billable act.
         */
        _ACTRELATIONSHIPCOSTTRACKING, 
        /**
         * A relationship that provides an ability to associate a financial transaction (target) as a charge to a clinical act (source).  A clinical act may have a charge associated with the execution or delivery of the service.

                        The financial transaction will define the charge (bill) for delivery or performance of the service.

                        Charges and costs are distinct terms.  A charge defines what is charged or billed to another organization or entity within an organization.  The cost defines what it costs an organization to perform or deliver a service or product.
         */
        CHRG, 
        /**
         * A relationship that provides an ability to associate a financial transaction (target) as a cost to a clinical act (source).  A clinical act may have an inherit cost associated with the execution or delivery of the service.

                        The financial transaction will define the cost of delivery or performance of the service.

                        Charges and costs are distinct terms.  A charge defines what is charged or billed to another organization or entity within an organization.  The cost defines what it costs an organization to perform or deliver a service or product.
         */
        COST, 
        /**
         * Expresses values for describing the relationship between a FinancialTransaction and an Account.
         */
        _ACTRELATIONSHIPPOSTING, 
        /**
         * A credit relationship ties a financial transaction (target) to an account (source). A credit, once applied (posted), may have either a positive or negative effect on the account balance, depending on the type of account. An asset account credit will decrease the account balance. A non-asset account credit will decrease the account balance.
         */
        CREDIT, 
        /**
         * A debit relationship ties a financial transaction (target) to an account (source).  A debit, once applied (posted), may have either a positive or negative effect on the account balance, depending on the type of account.  An asset account debit will increase the account balance.  A non-asset account debit will decrease the account balance.
         */
        DEBIT, 
        /**
         * Specifies under what circumstances (target Act) the source-Act may, must, must not or has occurred
         */
        _ACTRELATIONSHIPCONDITIONAL, 
        /**
         * A contraindication is just a negation of a reason, i.e. it gives a condition under which the action is not to be done. Both, source and target can be any kind of service; target service is in criterion mood. How the strength of a contraindication is expressed (e.g., relative, absolute) is left as an open issue. The priorityNumber attribute could be used.
         */
        CIND, 
        /**
         * A requirement to be true before a service is performed. The target can be any service in criterion mood.  For multiple pre-conditions a conjunction attribute (AND, OR, XOR) is applicable.
         */
        PRCN, 
        /**
         * Description: The reason or rationale for a service. A reason link is weaker than a trigger, it only suggests that some service may be or might have been a reason for some action, but not that this reason requires/required the action to be taken. Also, as opposed to the trigger, there is no strong timely relation between the reason and the action.  As well as providing various types of information about the rationale for a service, the RSON act relationship is routinely used between a SBADM act and an OBS act to describe the indication for use of a medication.  Child concepts may be used to describe types of indication. 

                        
                           Discussion: In prior releases, the code "SUGG" (suggests) was expressed as "an inversion of the reason link." That code has been retired in favor of the inversion indicator that is an attribute of ActRelationship.
         */
        RSON, 
        /**
         * Definition: The source act is performed to block the effects of the target act.  This act relationship should be used when describing near miss type incidents where potential harm could have occurred, but the action described in the source act blocked the potential harmful effects of the incident actually occurring.
         */
        BLOCK, 
        /**
         * Description: The source act is intended to help establish the presence of a (an adverse) situation described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.
         */
        DIAG, 
        /**
         * Description: The source act is intented to provide immunity against the effects of the target act (the target act describes an infectious disease)
         */
        IMM, 
        /**
         * Description: The source act is intended to provide active immunity against the effects of the target act (the target act describes an infectious disease)
         */
        ACTIMM, 
        /**
         * Description: The source act is intended to provide passive immunity against the effects of the target act (the target act describes an infectious disease).
         */
        PASSIMM, 
        /**
         * The source act removes or lessens the occurrence or effect of the target act.
         */
        MITGT, 
        /**
         * Definition: The source act is performed to recover from the effects of the target act.
         */
        RCVY, 
        /**
         * Description: The source act is intended to reduce the risk of of an adverse situation to emerge as described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.
         */
        PRYLX, 
        /**
         * Description: The source act is intended to improve a pre-existing adverse situation described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.
         */
        TREAT, 
        /**
         * Description: The source act is intended to offer an additional treatment for the management or cure of a pre-existing adverse situation described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.  It is not a requirement that the non-adjunctive treatment is explicitly specified.
         */
        ADJUNCT, 
        /**
         * Description: The source act is intended to provide long term maintenance improvement or management of a pre-existing adverse situation described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.
         */
        MTREAT, 
        /**
         * Description: The source act is intended to provide palliation for the effects of the target act.
         */
        PALLTREAT, 
        /**
         * Description: The source act is intented to provide symptomatic relief for the effects of the target act.
         */
        SYMP, 
        /**
         * A pre-condition that if true should result in the source Act being executed.  The target is in typically in criterion mood.  When reported after the fact (i.e. the criterion has been met) it may be in Event mood.  A delay between the trigger and the triggered action can be specified.

                        
                           Discussion: This includes the concept of a  required act for a service or financial instrument such as an insurance plan or policy. In such cases, the trigger is the occurrence of a specific condition such as coverage limits being exceeded.
         */
        TRIG, 
        /**
         * Abstract collector for ActRelationhsip types that relate two acts by their timing.
         */
        _ACTRELATIONSHIPTEMPORALLYPERTAINS, 
        /**
         * Abstract collector for ActRelationship types that relate two acts by their approximate timing.
         */
        _ACTRELATIONSHIPTEMPORALLYPERTAINSAPPROXIMATES, 
        /**
         * A relationship in which the source act's effective time ends near the end of the target act's effective time. Near is defined separately as a time interval.

                        
                           Usage Note: Inverse code is ENS
         */
        ENE, 
        /**
         * A relationship in which the source act's effective time ends with the end of the target act's effective time.

                        
                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.
         */
        ECW, 
        /**
         * A relationship in which the source act's effective time is the same as the target act's effective time.

                        
                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.
         */
        CONCURRENT, 
        /**
         * The source Act starts before the start of the target Act, and ends with the target Act.

                        
                           UsageNote: Inverse code is SASECWE
         */
        SBSECWE, 
        /**
         * A relationship in which the source act's effective time ends near the start of the target act's effective time. Near is defined separately as a time interval.

                        
                           Usage Note: Inverse code is ENE
         */
        ENS, 
        /**
         * The source Act ends when the target act starts (i.e. if we say "ActOne ECWS ActTwo", it means that ActOne ends when ActTwo starts, therefore ActOne is the source and ActTwo is the target).

                        
                           UsageNote: Inverse code is SCWE
         */
        ECWS, 
        /**
         * A relationship in which the source act's effective time starts near the end of the target act's effective time. Near is defined separately as a time interval.

                        
                           Usage Note: Inverse code is SNS
         */
        SNE, 
        /**
         * The source Act starts when the target act ends (i.e. if we say "ActOne SCWE ActTwo", it means that ActOne starts when ActTwo ends, therefore ActOne is the source and ActTwo is the target).

                        
                           UsageNote: Inverse code is SBSECWS
         */
        SCWE, 
        /**
         * A relationship in which the source act's effective time starts near the start of the target act's effective time. Near is defined separately as a time interval.

                        
                           Usage Note: Inverse code is SNE
         */
        SNS, 
        /**
         * A relationship in which the source act's effective time starts with the start of the target act's effective time.

                        
                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.
         */
        SCW, 
        /**
         * The source Act starts with.the target Act and ends before the end of the target Act.

                        
                           UsageNote: Inverse code is SCWSEAE
         */
        SCWSEBE, 
        /**
         * The source Act starts with the target Act, and ends after the end of the target Act.
         */
        SCWSEAE, 
        /**
         * A relationship in which the source act ends after the target act starts.

                        
                           UsageNote: Inverse code is SBE
         */
        EAS, 
        /**
         * A relationship in which the source act ends after the target act ends.

                        
                           UsageNote: Inverse code is EBE
         */
        EAE, 
        /**
         * The source Act starts after start of the target Act and ends after end of the target Act.

                        
                           UsageNote: Inverse code is SBSEBE
         */
        SASEAE, 
        /**
         * The source Act contains the end of the target Act.

                        
                           UsageNote: Inverse code is EDU
         */
        SBEEAE, 
        /**
         * The source Act start after the start of the target Act, and contains the end of the target Act.

                        
                           UsageNote: Inverse code is SBSEASEBE
         */
        SASSBEEAS, 
        /**
         * The source Act contains the time of the target Act.

                        
                           UsageNote: Inverse code is DURING
         */
        SBSEAE, 
        /**
         * The source Act starts after the start of the target Act (i.e. if we say "ActOne SAS ActTwo", it means that ActOne starts after the start of ActTwo, therefore ActOne is the source and ActTwo is the target).

                        
                           UsageNote: Inverse code is SBS
         */
        SAS, 
        /**
         * A relationship in which the source act starts after the target act ends.

                        
                           UsageNote: Inverse code is EBS
         */
        SAE, 
        /**
         * A relationship in which the source act's effective time is wholly within the target act's effective time (including end points, as defined in the act's effective times)

                        
                           UsageNote: Inverse code is SBSEAE
         */
        DURING, 
        /**
         * The source Act starts after start of the target Act, and ends with the target Act.

                        
                           UsageNote: Inverse code is SBSECWE
         */
        SASECWE, 
        /**
         * A relationship in which the source act's effective time ends after or concurrent with the start of the target act's effective time.

                        
                           Usage Note: Inverse code is EBSORECWS
         */
        EASORECWS, 
        /**
         * A relationship in which the source act's effective time ends after or concurrent with the end of the target act's effective time.

                        
                           Usage Note: Inverse code is EBEORECW
         */
        EAEORECW, 
        /**
         * The source Act is independent of the time of the target Act.

                        
                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.
         */
        INDEPENDENT, 
        /**
         * A relationship in which the source act's effective time starts after or concurrent with the end of the target act's effective time.

                        
                           Usage Note: Inverse code is SBEORSCWE
         */
        SAEORSCWE, 
        /**
         * A relationship in which the source act's effective time starts after or concurrent with the start of the target act's effective time.

                        
                           Usage Note: Inverse code is SBSORSCW
         */
        SASORSCW, 
        /**
         * A relationship in which the source act's effective time starts before or concurrent with the end of the target act's effective time.

                        
                           Usage Note: Inverse code is SAEORSCWE
         */
        SBEORSCWE, 
        /**
         * A relationship in which the source act's effective time overlaps the target act's effective time in any way.

                        
                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.
         */
        OVERLAP, 
        /**
         * A relationship in which the source act ends within the target act's effective time (including end points, as defined in the act's effective times)

                        
                           UsageNote: Inverse code is SBEEAE
         */
        EDU, 
        /**
         * The source Act contains the start of the target Act,  and ends before the end of the target Act.

                        
                           UsageNote: Inverse code is SASSBEEAS
         */
        SBSEASEBE, 
        /**
         * The source Act contains the start of the target Act.

                        
                           UsageNote: Inverse code is SDU
         */
        SBSEAS, 
        /**
         * A relationship in which the source act starts within the target act's effective time (including end points, as defined in the act's effective times)

                        
                           UsageNote: Inverse code is SBSEAS
         */
        SDU, 
        /**
         * The source Act starts before the end of the target Act (i.e. if we say "ActOne SBE ActTwo", it means that ActOne starts before the end of ActTwo, therefore ActOne is the source and ActTwo is the target).

                        
                           UsageNote: Inverse code is EAS
         */
        SBE, 
        /**
         * The source Act ends before the end of the target Act (i.e. if we say "ActOne EBE ActTwo", it means that ActOne ends before the end of ActTwo, therefore ActOne is the source and ActTwo is the target).

                        
                           UsageNote: Inverse code is EAE
         */
        EBE, 
        /**
         * The source Act starts before the start of the target Act, and ends before the end of the target Act.

                        
                           UsageNote: Inverse code is SASEAE
         */
        SBSEBE, 
        /**
         * A relationship in which the source act's effective time ends before or concurrent with the start of the target act's effective time.

                        
                           Usage Note: Inverse code is EASORECWS
         */
        EBSORECWS, 
        /**
         * A relationship in which the source act ends before the target act starts.

                        
                           UsageNote: Inverse code is SAE
         */
        EBS, 
        /**
         * A relationship in which the source act's effective time ends before or concurrent with the end of the target act's effective time.

                        
                           Usage Note: Inverse code is EAEORECW
         */
        EBEORECW, 
        /**
         * A relationship in which the source act's effective time starts before or concurrent with the start of the target act's effective time.

                        
                           Usage Note: Inverse code is SASORSCW
         */
        SBSORSCW, 
        /**
         * A relationship in which the source act begins before the target act begins.

                        
                           UsageNote: Inverse code is SAS
         */
        SBS, 
        /**
         * A relationship in which the target act authorizes or certifies the source act.
         */
        AUTH, 
        /**
         * Description: An assertion that an act was the cause of another act.This is stronger and more specific than the support link. The source (cause) is typically an observation, but may be any act, while the target may be any act.

                        
                           Examples:
                        

                        
                           a growth of Staphylococcus aureus may be considered the cause of an abscess
                           contamination of the infusion bag was deemed to be the cause of the infection that the patient experienced
                           lack of staff on the shift was deemed to be a supporting factor (proximal factor) causing the patient safety incident where the patient fell out of bed because the  bed-sides had not been put up which caused the night patient to fall out of bed
         */
        CAUS, 
        /**
         * The target act is a component of the source act, with no semantics regarding composition or aggregation implied.
         */
        COMP, 
        /**
         * A relationship from an Act to a Control Variable.  For example, if a Device makes an Observation, this relates the Observation to its Control Variables documenting  the device's settings that influenced the observation.
         */
        CTRLV, 
        /**
         * The target Acts are aggregated by the source Act.  Target Acts may have independent existence, participate in multiple ActRelationships, and do not contribute to the meaning of the source.

                        
                           UsageNotes: This explicitly represents the conventional notion of aggregation.  The target Act is part of a collection of Acts (no implication is made of cardinality, a source of Acts may contain zero, one, or more member target Acts).

                        It is expected that this will be primarily used with _ActClassRecordOrganizer, BATTERY, and LIST
         */
        MBR, 
        /**
         * A collection of sub-services as steps or subtasks performed for the source service. Services may be performed sequentially or concurrently.

                        
                           UsageNotes: Sequence of steps may be indicated by use of _ActRelationshipTemporallyPertains, as well as via  ActRelationship.sequenceNumber, ActRelationship.pauseQuantity, Target.priorityCode.

                        
                           OpenIssue: Need Additional guidelines on when each approach should be used.
         */
        STEP, 
        /**
         * The relationship that links to a Transportation Act (target) from another Act (source) indicating that the subject of the source Act entered into the source Act by means of the target Transportation act.
         */
        ARR, 
        /**
         * The relationship that links to a Transportation Act (target) from another Act (source) indicating that the subject of the source Act departed from the source Act by means of the target Transportation act.
         */
        DEP, 
        /**
         * The source Act is a composite of the target Acts. The target Acts do not have an existence independent of the source Act.

                        
                           UsageNote: In UML 1.1, this is a "composition" defined as: 
                           "A form of aggregation with strong ownership and coincident lifetime as part of the whole. Parts with non-fixed multiplicity may be created after the composite itself, but once created they live and die with it (i.e., they share lifetimes). Such parts can also be explicitly removed before the death of the composite. Composition may be recursive."
         */
        PART, 
        /**
         * A relationship in which the source act is covered by or is under the authority of a target act.  A financial instrument such as an Invoice Element is covered by one or more specific instances of an Insurance Policy.
         */
        COVBY, 
        /**
         * Associates a derived Act with its input parameters. E.G., an anion-gap observation can be associated as being derived from given sodium-, (potassium-,), chloride-, and bicarbonate-observations. The narrative content (Act.text) of a source act is wholly machine-derived from the collection of target acts.
         */
        DRIV, 
        /**
         * Expresses an association that links two instances of the same act over time, indicating that the instance are part of the same episode, e.g. linking two condition nodes for episode of illness; linking two encounters for episode of encounter.
         */
        ELNK, 
        /**
         * Indicates that the target Act provides evidence in support of the action represented by the source Act. The target is not a 'reason' for the source act, but rather gives supporting information on why the source act is an appropriate course of action. Possible targets might be clinical trial results, journal articles, similar successful therapies, etc.

                        
                           Rationale: Provides a mechanism for conveying clinical justification for non-approved or otherwise non-traditional therapies.
         */
        EVID, 
        /**
         * Description:The source act is aggravated by the target act. (Example "chest pain" EXACBY "exercise")
         */
        EXACBY, 
        /**
         * This is the inversion of support.  Used to indicate that a given observation is explained by another observation or condition.
         */
        EXPL, 
        /**
         * the target act documents a set of circumstances (events, risks) which prevent successful completion, or degradation of quality of, the source Act.

                        
                           UsageNote: This provides the semantics to document barriers to care
         */
        INTF, 
        /**
         * Items located
         */
        ITEMSLOC, 
        /**
         * A relationship that limits or restricts the source act by the elements of the target act.  For example, an authorization may be limited by a financial amount (up to $500). Target Act must be in EVN.CRIT mood.
         */
        LIMIT, 
        /**
         * Definition: Indicates that the attributes and associations of the target act provide metadata (for example, identifiers, authorship, etc.) for the source act.

                        
                           Constraint:  Source act must have either a mood code that is not "EVN" (event) or its "isCriterion" attribute must set to "true".  Target act must be an Act with a mood code of EVN and with isCriterionInd attribute set to "true".
         */
        META, 
        /**
         * An assertion that a new observation may be the manifestation of another existing observation or action.  This assumption is attributed to the same actor who asserts the manifestation.  This is stronger and more specific than an inverted support link.  For example, an agitated appearance can be asserted to be the manifestation (effect) of a known hyperthyroxia.  This expresses that one might not have realized a symptom if it would not be a common manifestation of a known condition.  The target (cause) may be any service, while the source (manifestation) must be an observation.
         */
        MFST, 
        /**
         * Used to assign a "name" to a condition thread. Source is a condition node, target can be any service.
         */
        NAME, 
        /**
         * An observation that should follow or does actually follow as a result or consequence of a condition or action (sometimes called "post-conditional".) Target must be an observation as a goal, risk or any criterion. For complex outcomes a conjunction attribute (AND, OR, XOR) can be used.  An outcome link is often inverted to describe an outcome assessment.
         */
        OUTC, 
        /**
         * The target act is a desired outcome of the source act. Source is any act (typically an intervention). Target must be an observation in criterion mood.
         */
        _ACTRELATIONSIPOBJECTIVE, 
        /**
         * A desired state that a service action aims to maintain.  E.g., keep systolic blood pressure between 90 and 110 mm Hg.  Source is an intervention service.  Target must be an observation in criterion mood.
         */
        OBJC, 
        /**
         * A desired outcome that a service action aims to meet finally.  Source is any service (typically an intervention).  Target must be an observation in criterion mood.
         */
        OBJF, 
        /**
         * A goal that one defines given a patient's health condition.  Subsequently planned actions aim to meet that goal.  Source is an observation or condition node, target must be an observation in goal mood.
         */
        GOAL, 
        /**
         * A noteworthy undesired outcome of a patient's condition that is either likely enough to become an issue or is less likely but dangerous enough to be addressed.
         */
        RISK, 
        /**
         * This is a very unspecific relationship from one item of clinical information to another.  It does not judge about the role the pertinent information plays.
         */
        PERT, 
        /**
         * A relationship in which the target act is a predecessor instance to the source act.  Generally each of these instances is similar, but no identical.  In healthcare coverage it is used to link a claim item to a previous claim item that might have claimed for the same set of services.
         */
        PREV, 
        /**
         * A relationship in which the target act is referred to by the source act.  This permits a simple reference relationship that distinguishes between the referent and the referee.
         */
        REFR, 
        /**
         * Indicates that the source act makes use of (or will make use of) the information content of the target act.

                        
                           UsageNotes: A usage relationship only makes sense if the target act is authored and occurs independently of the source act.  Otherwise a simpler relationship such as COMP would be appropriate.

                        
                           Rationale: There is a need when defining a clinical trial protocol to indicate that the protocol makes use of other protocol or treatment specifications.  This is stronger than the assertion of "references".  References may exist without usage, and in a clinical trial protocol is common to assert both: what other specifications does this trial use and what other specifications does it merely reference.
         */
        USE, 
        /**
         * Reference ranges are essentially descriptors of a class of result values assumed to be "normal", "abnormal", or "critical."  Those can vary by sex, age, or any other criterion. Source and target are observations, the target is in criterion mood.  This link type can act as a trigger in case of alarms being triggered by critical results.
         */
        REFV, 
        /**
         * Description:The source act is wholly or partially alleviated by the target act. (Example "chest pain" RELVBY "sublingual nitroglycerin administration")
         */
        RELVBY, 
        /**
         * An act relationship indicating that the source act follows the target act. The source act should in principle represent the same kind of act as the target. Source and target need not have the same mood code (mood will often differ). The target of a sequel is called antecedent. Examples for sequel relationships are: revision, transformation, derivation from a prototype (as a specialization is a derivation of a generalization), followup, realization, instantiation.
         */
        SEQL, 
        /**
         * An addendum (source) to an existing service object (target), containing supplemental information.  The addendum is itself an original service object linked to the supplemented service object.  The supplemented service object remains in place and its content and status are unaltered.
         */
        APND, 
        /**
         * Indicates that the target observation(s) provide an initial reference for the source observation or observation group.

                        
                           UsageConstraints: Both source and target must be Observations or specializations thereof.
         */
        BSLN, 
        /**
         * Description:The source act complies with, adheres to, conforms to, or is permissible under (in whole or in part) the policy, contract, agreement, law, conformance criteria, certification guidelines or requirement conveyed by the target act.

                        Examples for compliance relationships are: audits of adherence with a security policy, certificate of conformance to system certification requirements, or consent directive in compliance with or permissible under a privacy policy.
         */
        COMPLY, 
        /**
         * The source act documents the target act.
         */
        DOC, 
        /**
         * The source act fulfills (in whole or in part) the target act. Source act must be in a mood equal or more actual than the target act.
         */
        FLFS, 
        /**
         * The source act is a single occurrence of a repeatable target act. The source and target act can be in any mood on the "completion track" but the source act must be as far as or further along the track than the target act (i.e., the occurrence of an intent can be an event but not vice versa).
         */
        OCCR, 
        /**
         * Relates either an appointment request or an appointment to the order for the service being scheduled.
         */
        OREF, 
        /**
         * Associates a specific time (and associated resources) with a scheduling request or other intent.
         */
        SCH, 
        /**
         * The generalization relationship can be used to express categorical knowledge about services (e.g., amilorid, triamterene, and spironolactone have the common generalization potassium sparing diuretic).
         */
        GEN, 
        /**
         * A goal-evaluation links an observation (intent or actual) to a goal to indicate that the observation evaluates the goal. Given the goal and the observation, a "goal distance" (e.g., goal to observation) can be "calculated" and need not be sent explicitly.
         */
        GEVL, 
        /**
         * Used to capture the link between a potential service ("master" or plan) and an actual service, where the actual service instantiates the potential service. The instantiation may override the master's defaults.
         */
        INST, 
        /**
         * Definition: Used to link a newer version or 'snapshot' of a business object (source) to an older version or 'snapshot' of the same business object (target).

                        
                           Usage:The identifier of the Act should be the same for both source and target. If the identifiers are distinct, RPLC should be used instead.

                        Name from source to target = "modifiesPrior"

                        Name from target to source = "modifiesByNew"
         */
        MOD, 
        /**
         * A trigger-match links an actual service (e.g., an observation or procedure that took place) with a service in criterion mood.  For example if the trigger is "observation of pain" and pain is actually observed, and if that pain-observation caused the trigger to fire, that pain-observation can be linked with the trigger.
         */
        MTCH, 
        /**
         * A relationship between a source Act that provides more detailed properties to the target Act.

                        The source act thus is a specialization of the target act, but instead of mentioning all the inherited properties it only mentions new property bindings or refinements.

                        The typical use case is to specify certain alternative variants of one kind of Act. The priorityNumber attribute is used to weigh refinements as preferred over other alternative refinements.

                        Example: several routing options for a drug are specified as one SubstanceAdministration for the general treatment with attached refinements for the various routing options.
         */
        OPTN, 
        /**
         * Description:A relationship in which the target act is carried out to determine whether an effect attributed to the source act can be recreated.
         */
        RCHAL, 
        /**
         * A relationship between a source Act that seeks to reverse or undo the action of the prior target Act.

                        Example: A posted financial transaction (e.g., a debit transaction) was applied in error and must be reversed (e.g., by a credit transaction) the credit transaction is identified as an undo (or reversal) of the prior target transaction.

                        Constraints: the "completion track" mood of the target Act must be equally or more "actual" than the source act. I.e., when the target act is EVN the source act can be EVN, or any INT. If the target act is INT, the source act can be INT.
         */
        REV, 
        /**
         * A replacement source act replaces an existing target act. The state of the target act being replaced becomes obselete, but the act is typically still retained in the system for historical reference.  The source and target must be of the same type.
         */
        RPLC, 
        /**
         * Definition:  A new act that carries forward the intention of the original act, but does not completely replace it.  The status of the predecessor act must be 'completed'.  The original act is the target act and the successor is the source act.
         */
        SUCC, 
        /**
         * A condition thread relationship specifically links condition nodes together to form a condition thread. The source is the new condition node and the target links to the most recent node of the existing condition thread.
         */
        UPDT, 
        /**
         * The source is an excerpt from the target.
         */
        XCRPT, 
        /**
         * The source is a direct quote from the target.
         */
        VRXCRPT, 
        /**
         * Used when the target Act is a transformation of the source Act. (For instance, used to show that a CDA document is a transformation of a DICOM SR document.)
         */
        XFRM, 
        /**
         * Used to indicate that an existing service is suggesting evidence for a new observation. The assumption of support is attributed to the same actor who asserts the observation. Source must be an observation, target may be any service  (e.g., to indicate a status post).
         */
        SPRT, 
        /**
         * A specialization of "has support" (SPRT), used to relate a secondary observation to a Region of Interest on a multidimensional observation, if the ROI specifies the true boundaries of the secondary observation as opposed to only marking the approximate area.  For example, if the start and end of an ST elevation episode is visible in an EKG, this relation would indicate the ROI bounds the  "ST elevation" observation -- the ROI defines the true beginning and ending of the episode.  Conversely, if a ROI simply contains ST elevation, but it does not define the bounds (start and end) of the episode, the more general "has support" relation is used.  Likewise, if a ROI on an image defines the true bounds of a "1st degree burn", the relation "has bounded support" is used; but if the ROI only points to the approximate area of the burn, the general "has support" relation is used.
         */
        SPRTBND, 
        /**
         * Relates an Act to its subject Act that the first Act is primarily concerned with.

                        Examples

                        
                           
                              The first Act may be a ControlAct manipulating the subject Act 

                           
                           
                              The first act is a region of interest (ROI) that defines a region within the subject Act.

                           
                           
                              The first act is a reporting or notification Act, that echos the subject Act for a specific new purpose.

                           
                        
                        Constraints

                        An Act may have multiple subject acts.

                        Rationale

                        The ActRelationshipType "has subject" is similar to the ParticipationType "subject", Acts that primarily operate on physical subjects use the Participation, those Acts that primarily operate on other Acts (other information) use the ActRelationship.
         */
        SUBJ, 
        /**
         * The target observation qualifies (refines) the semantics of the source observation.

                        
                           UsageNote: This is not intended to replace concept refinement and qualification via vocabulary.  It is used when there are multiple components which together provide the complete understanding of the source Act.
         */
        QUALF, 
        /**
         * An act that contains summary values for a list or set of subordinate acts.  For example, a summary of transactions for a particular accounting period.
         */
        SUMM, 
        /**
         * Description:Indicates that the target Act represents the result of the source observation Act.

                        
                           FormalConstraint: Source Act must be an Observation or specialization there-of. Source Act must not have the value attribute specified

                        
                           UsageNote: This relationship allows the result of an observation to be fully expressed as RIM acts as opposed to being embedded in the value attribute.  For example, sending a Document act as the result of an imaging observation, sending a list of Procedures and/or other acts as the result of a medical history observation.

                        The valueNegationInd attribute on the source Act has the same semantics of "negated finding" when it applies to the target of a VALUE ActRelationship as it does to the value attribute.  On the other hand, if the ActRelationship.negationInd is true for a VALUE ActRelationship, that means the specified observation does not have the indicated value but does not imply a negated finding.  Because the semantics are extremely close, it is recommended that Observation.valueNegationInd be used, not ActRelationship.negationInd.

                        
                           OpenIssue: The implications of negationInd on ActRelationship and the valueNegationind on Observation.
         */
        VALUE, 
        /**
         * curative indication
         */
        CURE, 
        /**
         * adjunct curative indication
         */
        CURE_ADJ, 
        /**
         * adjunct mitigation
         */
        MTGT_ADJ, 
        /**
         * null
         */
        RACT, 
        /**
         * null
         */
        SUGG, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActRelationshipType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ART".equals(codeString))
          return ART;
        if ("_ActClassTemporallyPertains".equals(codeString))
          return _ACTCLASSTEMPORALLYPERTAINS;
        if ("_ActRelationshipAccounting".equals(codeString))
          return _ACTRELATIONSHIPACCOUNTING;
        if ("_ActRelationshipCostTracking".equals(codeString))
          return _ACTRELATIONSHIPCOSTTRACKING;
        if ("CHRG".equals(codeString))
          return CHRG;
        if ("COST".equals(codeString))
          return COST;
        if ("_ActRelationshipPosting".equals(codeString))
          return _ACTRELATIONSHIPPOSTING;
        if ("CREDIT".equals(codeString))
          return CREDIT;
        if ("DEBIT".equals(codeString))
          return DEBIT;
        if ("_ActRelationshipConditional".equals(codeString))
          return _ACTRELATIONSHIPCONDITIONAL;
        if ("CIND".equals(codeString))
          return CIND;
        if ("PRCN".equals(codeString))
          return PRCN;
        if ("RSON".equals(codeString))
          return RSON;
        if ("BLOCK".equals(codeString))
          return BLOCK;
        if ("DIAG".equals(codeString))
          return DIAG;
        if ("IMM".equals(codeString))
          return IMM;
        if ("ACTIMM".equals(codeString))
          return ACTIMM;
        if ("PASSIMM".equals(codeString))
          return PASSIMM;
        if ("MITGT".equals(codeString))
          return MITGT;
        if ("RCVY".equals(codeString))
          return RCVY;
        if ("PRYLX".equals(codeString))
          return PRYLX;
        if ("TREAT".equals(codeString))
          return TREAT;
        if ("ADJUNCT".equals(codeString))
          return ADJUNCT;
        if ("MTREAT".equals(codeString))
          return MTREAT;
        if ("PALLTREAT".equals(codeString))
          return PALLTREAT;
        if ("SYMP".equals(codeString))
          return SYMP;
        if ("TRIG".equals(codeString))
          return TRIG;
        if ("_ActRelationshipTemporallyPertains".equals(codeString))
          return _ACTRELATIONSHIPTEMPORALLYPERTAINS;
        if ("_ActRelationshipTemporallyPertainsApproximates".equals(codeString))
          return _ACTRELATIONSHIPTEMPORALLYPERTAINSAPPROXIMATES;
        if ("ENE".equals(codeString))
          return ENE;
        if ("ECW".equals(codeString))
          return ECW;
        if ("CONCURRENT".equals(codeString))
          return CONCURRENT;
        if ("SBSECWE".equals(codeString))
          return SBSECWE;
        if ("ENS".equals(codeString))
          return ENS;
        if ("ECWS".equals(codeString))
          return ECWS;
        if ("SNE".equals(codeString))
          return SNE;
        if ("SCWE".equals(codeString))
          return SCWE;
        if ("SNS".equals(codeString))
          return SNS;
        if ("SCW".equals(codeString))
          return SCW;
        if ("SCWSEBE".equals(codeString))
          return SCWSEBE;
        if ("SCWSEAE".equals(codeString))
          return SCWSEAE;
        if ("EAS".equals(codeString))
          return EAS;
        if ("EAE".equals(codeString))
          return EAE;
        if ("SASEAE".equals(codeString))
          return SASEAE;
        if ("SBEEAE".equals(codeString))
          return SBEEAE;
        if ("SASSBEEAS".equals(codeString))
          return SASSBEEAS;
        if ("SBSEAE".equals(codeString))
          return SBSEAE;
        if ("SAS".equals(codeString))
          return SAS;
        if ("SAE".equals(codeString))
          return SAE;
        if ("DURING".equals(codeString))
          return DURING;
        if ("SASECWE".equals(codeString))
          return SASECWE;
        if ("EASORECWS".equals(codeString))
          return EASORECWS;
        if ("EAEORECW".equals(codeString))
          return EAEORECW;
        if ("INDEPENDENT".equals(codeString))
          return INDEPENDENT;
        if ("SAEORSCWE".equals(codeString))
          return SAEORSCWE;
        if ("SASORSCW".equals(codeString))
          return SASORSCW;
        if ("SBEORSCWE".equals(codeString))
          return SBEORSCWE;
        if ("OVERLAP".equals(codeString))
          return OVERLAP;
        if ("EDU".equals(codeString))
          return EDU;
        if ("SBSEASEBE".equals(codeString))
          return SBSEASEBE;
        if ("SBSEAS".equals(codeString))
          return SBSEAS;
        if ("SDU".equals(codeString))
          return SDU;
        if ("SBE".equals(codeString))
          return SBE;
        if ("EBE".equals(codeString))
          return EBE;
        if ("SBSEBE".equals(codeString))
          return SBSEBE;
        if ("EBSORECWS".equals(codeString))
          return EBSORECWS;
        if ("EBS".equals(codeString))
          return EBS;
        if ("EBEORECW".equals(codeString))
          return EBEORECW;
        if ("SBSORSCW".equals(codeString))
          return SBSORSCW;
        if ("SBS".equals(codeString))
          return SBS;
        if ("AUTH".equals(codeString))
          return AUTH;
        if ("CAUS".equals(codeString))
          return CAUS;
        if ("COMP".equals(codeString))
          return COMP;
        if ("CTRLV".equals(codeString))
          return CTRLV;
        if ("MBR".equals(codeString))
          return MBR;
        if ("STEP".equals(codeString))
          return STEP;
        if ("ARR".equals(codeString))
          return ARR;
        if ("DEP".equals(codeString))
          return DEP;
        if ("PART".equals(codeString))
          return PART;
        if ("COVBY".equals(codeString))
          return COVBY;
        if ("DRIV".equals(codeString))
          return DRIV;
        if ("ELNK".equals(codeString))
          return ELNK;
        if ("EVID".equals(codeString))
          return EVID;
        if ("EXACBY".equals(codeString))
          return EXACBY;
        if ("EXPL".equals(codeString))
          return EXPL;
        if ("INTF".equals(codeString))
          return INTF;
        if ("ITEMSLOC".equals(codeString))
          return ITEMSLOC;
        if ("LIMIT".equals(codeString))
          return LIMIT;
        if ("META".equals(codeString))
          return META;
        if ("MFST".equals(codeString))
          return MFST;
        if ("NAME".equals(codeString))
          return NAME;
        if ("OUTC".equals(codeString))
          return OUTC;
        if ("_ActRelationsipObjective".equals(codeString))
          return _ACTRELATIONSIPOBJECTIVE;
        if ("OBJC".equals(codeString))
          return OBJC;
        if ("OBJF".equals(codeString))
          return OBJF;
        if ("GOAL".equals(codeString))
          return GOAL;
        if ("RISK".equals(codeString))
          return RISK;
        if ("PERT".equals(codeString))
          return PERT;
        if ("PREV".equals(codeString))
          return PREV;
        if ("REFR".equals(codeString))
          return REFR;
        if ("USE".equals(codeString))
          return USE;
        if ("REFV".equals(codeString))
          return REFV;
        if ("RELVBY".equals(codeString))
          return RELVBY;
        if ("SEQL".equals(codeString))
          return SEQL;
        if ("APND".equals(codeString))
          return APND;
        if ("BSLN".equals(codeString))
          return BSLN;
        if ("COMPLY".equals(codeString))
          return COMPLY;
        if ("DOC".equals(codeString))
          return DOC;
        if ("FLFS".equals(codeString))
          return FLFS;
        if ("OCCR".equals(codeString))
          return OCCR;
        if ("OREF".equals(codeString))
          return OREF;
        if ("SCH".equals(codeString))
          return SCH;
        if ("GEN".equals(codeString))
          return GEN;
        if ("GEVL".equals(codeString))
          return GEVL;
        if ("INST".equals(codeString))
          return INST;
        if ("MOD".equals(codeString))
          return MOD;
        if ("MTCH".equals(codeString))
          return MTCH;
        if ("OPTN".equals(codeString))
          return OPTN;
        if ("RCHAL".equals(codeString))
          return RCHAL;
        if ("REV".equals(codeString))
          return REV;
        if ("RPLC".equals(codeString))
          return RPLC;
        if ("SUCC".equals(codeString))
          return SUCC;
        if ("UPDT".equals(codeString))
          return UPDT;
        if ("XCRPT".equals(codeString))
          return XCRPT;
        if ("VRXCRPT".equals(codeString))
          return VRXCRPT;
        if ("XFRM".equals(codeString))
          return XFRM;
        if ("SPRT".equals(codeString))
          return SPRT;
        if ("SPRTBND".equals(codeString))
          return SPRTBND;
        if ("SUBJ".equals(codeString))
          return SUBJ;
        if ("QUALF".equals(codeString))
          return QUALF;
        if ("SUMM".equals(codeString))
          return SUMM;
        if ("VALUE".equals(codeString))
          return VALUE;
        if ("CURE".equals(codeString))
          return CURE;
        if ("CURE.ADJ".equals(codeString))
          return CURE_ADJ;
        if ("MTGT.ADJ".equals(codeString))
          return MTGT_ADJ;
        if ("RACT".equals(codeString))
          return RACT;
        if ("SUGG".equals(codeString))
          return SUGG;
        throw new FHIRException("Unknown V3ActRelationshipType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ART: return "ART";
            case _ACTCLASSTEMPORALLYPERTAINS: return "_ActClassTemporallyPertains";
            case _ACTRELATIONSHIPACCOUNTING: return "_ActRelationshipAccounting";
            case _ACTRELATIONSHIPCOSTTRACKING: return "_ActRelationshipCostTracking";
            case CHRG: return "CHRG";
            case COST: return "COST";
            case _ACTRELATIONSHIPPOSTING: return "_ActRelationshipPosting";
            case CREDIT: return "CREDIT";
            case DEBIT: return "DEBIT";
            case _ACTRELATIONSHIPCONDITIONAL: return "_ActRelationshipConditional";
            case CIND: return "CIND";
            case PRCN: return "PRCN";
            case RSON: return "RSON";
            case BLOCK: return "BLOCK";
            case DIAG: return "DIAG";
            case IMM: return "IMM";
            case ACTIMM: return "ACTIMM";
            case PASSIMM: return "PASSIMM";
            case MITGT: return "MITGT";
            case RCVY: return "RCVY";
            case PRYLX: return "PRYLX";
            case TREAT: return "TREAT";
            case ADJUNCT: return "ADJUNCT";
            case MTREAT: return "MTREAT";
            case PALLTREAT: return "PALLTREAT";
            case SYMP: return "SYMP";
            case TRIG: return "TRIG";
            case _ACTRELATIONSHIPTEMPORALLYPERTAINS: return "_ActRelationshipTemporallyPertains";
            case _ACTRELATIONSHIPTEMPORALLYPERTAINSAPPROXIMATES: return "_ActRelationshipTemporallyPertainsApproximates";
            case ENE: return "ENE";
            case ECW: return "ECW";
            case CONCURRENT: return "CONCURRENT";
            case SBSECWE: return "SBSECWE";
            case ENS: return "ENS";
            case ECWS: return "ECWS";
            case SNE: return "SNE";
            case SCWE: return "SCWE";
            case SNS: return "SNS";
            case SCW: return "SCW";
            case SCWSEBE: return "SCWSEBE";
            case SCWSEAE: return "SCWSEAE";
            case EAS: return "EAS";
            case EAE: return "EAE";
            case SASEAE: return "SASEAE";
            case SBEEAE: return "SBEEAE";
            case SASSBEEAS: return "SASSBEEAS";
            case SBSEAE: return "SBSEAE";
            case SAS: return "SAS";
            case SAE: return "SAE";
            case DURING: return "DURING";
            case SASECWE: return "SASECWE";
            case EASORECWS: return "EASORECWS";
            case EAEORECW: return "EAEORECW";
            case INDEPENDENT: return "INDEPENDENT";
            case SAEORSCWE: return "SAEORSCWE";
            case SASORSCW: return "SASORSCW";
            case SBEORSCWE: return "SBEORSCWE";
            case OVERLAP: return "OVERLAP";
            case EDU: return "EDU";
            case SBSEASEBE: return "SBSEASEBE";
            case SBSEAS: return "SBSEAS";
            case SDU: return "SDU";
            case SBE: return "SBE";
            case EBE: return "EBE";
            case SBSEBE: return "SBSEBE";
            case EBSORECWS: return "EBSORECWS";
            case EBS: return "EBS";
            case EBEORECW: return "EBEORECW";
            case SBSORSCW: return "SBSORSCW";
            case SBS: return "SBS";
            case AUTH: return "AUTH";
            case CAUS: return "CAUS";
            case COMP: return "COMP";
            case CTRLV: return "CTRLV";
            case MBR: return "MBR";
            case STEP: return "STEP";
            case ARR: return "ARR";
            case DEP: return "DEP";
            case PART: return "PART";
            case COVBY: return "COVBY";
            case DRIV: return "DRIV";
            case ELNK: return "ELNK";
            case EVID: return "EVID";
            case EXACBY: return "EXACBY";
            case EXPL: return "EXPL";
            case INTF: return "INTF";
            case ITEMSLOC: return "ITEMSLOC";
            case LIMIT: return "LIMIT";
            case META: return "META";
            case MFST: return "MFST";
            case NAME: return "NAME";
            case OUTC: return "OUTC";
            case _ACTRELATIONSIPOBJECTIVE: return "_ActRelationsipObjective";
            case OBJC: return "OBJC";
            case OBJF: return "OBJF";
            case GOAL: return "GOAL";
            case RISK: return "RISK";
            case PERT: return "PERT";
            case PREV: return "PREV";
            case REFR: return "REFR";
            case USE: return "USE";
            case REFV: return "REFV";
            case RELVBY: return "RELVBY";
            case SEQL: return "SEQL";
            case APND: return "APND";
            case BSLN: return "BSLN";
            case COMPLY: return "COMPLY";
            case DOC: return "DOC";
            case FLFS: return "FLFS";
            case OCCR: return "OCCR";
            case OREF: return "OREF";
            case SCH: return "SCH";
            case GEN: return "GEN";
            case GEVL: return "GEVL";
            case INST: return "INST";
            case MOD: return "MOD";
            case MTCH: return "MTCH";
            case OPTN: return "OPTN";
            case RCHAL: return "RCHAL";
            case REV: return "REV";
            case RPLC: return "RPLC";
            case SUCC: return "SUCC";
            case UPDT: return "UPDT";
            case XCRPT: return "XCRPT";
            case VRXCRPT: return "VRXCRPT";
            case XFRM: return "XFRM";
            case SPRT: return "SPRT";
            case SPRTBND: return "SPRTBND";
            case SUBJ: return "SUBJ";
            case QUALF: return "QUALF";
            case SUMM: return "SUMM";
            case VALUE: return "VALUE";
            case CURE: return "CURE";
            case CURE_ADJ: return "CURE.ADJ";
            case MTGT_ADJ: return "MTGT.ADJ";
            case RACT: return "RACT";
            case SUGG: return "SUGG";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActRelationshipType";
        }
        public String getDefinition() {
          switch (this) {
            case ART: return "Description: A directed association between a source Act and a target Act.\r\n\n                        \n                           Usage Note: This code should never be transmitted in an instance as the value of ActRelationship.typeCode (attribute)";
            case _ACTCLASSTEMPORALLYPERTAINS: return "ActClassTemporallyPertains";
            case _ACTRELATIONSHIPACCOUNTING: return "Codes that describe the relationship between an Act and a financial instrument such as a financial transaction, account or invoice element.";
            case _ACTRELATIONSHIPCOSTTRACKING: return "Expresses values for describing the relationship relationship between an InvoiceElement or InvoiceElementGroup and a billable act.";
            case CHRG: return "A relationship that provides an ability to associate a financial transaction (target) as a charge to a clinical act (source).  A clinical act may have a charge associated with the execution or delivery of the service.\r\n\n                        The financial transaction will define the charge (bill) for delivery or performance of the service.\r\n\n                        Charges and costs are distinct terms.  A charge defines what is charged or billed to another organization or entity within an organization.  The cost defines what it costs an organization to perform or deliver a service or product.";
            case COST: return "A relationship that provides an ability to associate a financial transaction (target) as a cost to a clinical act (source).  A clinical act may have an inherit cost associated with the execution or delivery of the service.\r\n\n                        The financial transaction will define the cost of delivery or performance of the service.\r\n\n                        Charges and costs are distinct terms.  A charge defines what is charged or billed to another organization or entity within an organization.  The cost defines what it costs an organization to perform or deliver a service or product.";
            case _ACTRELATIONSHIPPOSTING: return "Expresses values for describing the relationship between a FinancialTransaction and an Account.";
            case CREDIT: return "A credit relationship ties a financial transaction (target) to an account (source). A credit, once applied (posted), may have either a positive or negative effect on the account balance, depending on the type of account. An asset account credit will decrease the account balance. A non-asset account credit will decrease the account balance.";
            case DEBIT: return "A debit relationship ties a financial transaction (target) to an account (source).  A debit, once applied (posted), may have either a positive or negative effect on the account balance, depending on the type of account.  An asset account debit will increase the account balance.  A non-asset account debit will decrease the account balance.";
            case _ACTRELATIONSHIPCONDITIONAL: return "Specifies under what circumstances (target Act) the source-Act may, must, must not or has occurred";
            case CIND: return "A contraindication is just a negation of a reason, i.e. it gives a condition under which the action is not to be done. Both, source and target can be any kind of service; target service is in criterion mood. How the strength of a contraindication is expressed (e.g., relative, absolute) is left as an open issue. The priorityNumber attribute could be used.";
            case PRCN: return "A requirement to be true before a service is performed. The target can be any service in criterion mood.  For multiple pre-conditions a conjunction attribute (AND, OR, XOR) is applicable.";
            case RSON: return "Description: The reason or rationale for a service. A reason link is weaker than a trigger, it only suggests that some service may be or might have been a reason for some action, but not that this reason requires/required the action to be taken. Also, as opposed to the trigger, there is no strong timely relation between the reason and the action.  As well as providing various types of information about the rationale for a service, the RSON act relationship is routinely used between a SBADM act and an OBS act to describe the indication for use of a medication.  Child concepts may be used to describe types of indication. \r\n\n                        \n                           Discussion: In prior releases, the code \"SUGG\" (suggests) was expressed as \"an inversion of the reason link.\" That code has been retired in favor of the inversion indicator that is an attribute of ActRelationship.";
            case BLOCK: return "Definition: The source act is performed to block the effects of the target act.  This act relationship should be used when describing near miss type incidents where potential harm could have occurred, but the action described in the source act blocked the potential harmful effects of the incident actually occurring.";
            case DIAG: return "Description: The source act is intended to help establish the presence of a (an adverse) situation described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.";
            case IMM: return "Description: The source act is intented to provide immunity against the effects of the target act (the target act describes an infectious disease)";
            case ACTIMM: return "Description: The source act is intended to provide active immunity against the effects of the target act (the target act describes an infectious disease)";
            case PASSIMM: return "Description: The source act is intended to provide passive immunity against the effects of the target act (the target act describes an infectious disease).";
            case MITGT: return "The source act removes or lessens the occurrence or effect of the target act.";
            case RCVY: return "Definition: The source act is performed to recover from the effects of the target act.";
            case PRYLX: return "Description: The source act is intended to reduce the risk of of an adverse situation to emerge as described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.";
            case TREAT: return "Description: The source act is intended to improve a pre-existing adverse situation described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.";
            case ADJUNCT: return "Description: The source act is intended to offer an additional treatment for the management or cure of a pre-existing adverse situation described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.  It is not a requirement that the non-adjunctive treatment is explicitly specified.";
            case MTREAT: return "Description: The source act is intended to provide long term maintenance improvement or management of a pre-existing adverse situation described by the target act. This is not limited to diseases but can apply to any adverse situation or condition of medical or technical nature.";
            case PALLTREAT: return "Description: The source act is intended to provide palliation for the effects of the target act.";
            case SYMP: return "Description: The source act is intented to provide symptomatic relief for the effects of the target act.";
            case TRIG: return "A pre-condition that if true should result in the source Act being executed.  The target is in typically in criterion mood.  When reported after the fact (i.e. the criterion has been met) it may be in Event mood.  A delay between the trigger and the triggered action can be specified.\r\n\n                        \n                           Discussion: This includes the concept of a  required act for a service or financial instrument such as an insurance plan or policy. In such cases, the trigger is the occurrence of a specific condition such as coverage limits being exceeded.";
            case _ACTRELATIONSHIPTEMPORALLYPERTAINS: return "Abstract collector for ActRelationhsip types that relate two acts by their timing.";
            case _ACTRELATIONSHIPTEMPORALLYPERTAINSAPPROXIMATES: return "Abstract collector for ActRelationship types that relate two acts by their approximate timing.";
            case ENE: return "A relationship in which the source act's effective time ends near the end of the target act's effective time. Near is defined separately as a time interval.\r\n\n                        \n                           Usage Note: Inverse code is ENS";
            case ECW: return "A relationship in which the source act's effective time ends with the end of the target act's effective time.\r\n\n                        \n                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.";
            case CONCURRENT: return "A relationship in which the source act's effective time is the same as the target act's effective time.\r\n\n                        \n                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.";
            case SBSECWE: return "The source Act starts before the start of the target Act, and ends with the target Act.\r\n\n                        \n                           UsageNote: Inverse code is SASECWE";
            case ENS: return "A relationship in which the source act's effective time ends near the start of the target act's effective time. Near is defined separately as a time interval.\r\n\n                        \n                           Usage Note: Inverse code is ENE";
            case ECWS: return "The source Act ends when the target act starts (i.e. if we say \"ActOne ECWS ActTwo\", it means that ActOne ends when ActTwo starts, therefore ActOne is the source and ActTwo is the target).\r\n\n                        \n                           UsageNote: Inverse code is SCWE";
            case SNE: return "A relationship in which the source act's effective time starts near the end of the target act's effective time. Near is defined separately as a time interval.\r\n\n                        \n                           Usage Note: Inverse code is SNS";
            case SCWE: return "The source Act starts when the target act ends (i.e. if we say \"ActOne SCWE ActTwo\", it means that ActOne starts when ActTwo ends, therefore ActOne is the source and ActTwo is the target).\r\n\n                        \n                           UsageNote: Inverse code is SBSECWS";
            case SNS: return "A relationship in which the source act's effective time starts near the start of the target act's effective time. Near is defined separately as a time interval.\r\n\n                        \n                           Usage Note: Inverse code is SNE";
            case SCW: return "A relationship in which the source act's effective time starts with the start of the target act's effective time.\r\n\n                        \n                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.";
            case SCWSEBE: return "The source Act starts with.the target Act and ends before the end of the target Act.\r\n\n                        \n                           UsageNote: Inverse code is SCWSEAE";
            case SCWSEAE: return "The source Act starts with the target Act, and ends after the end of the target Act.";
            case EAS: return "A relationship in which the source act ends after the target act starts.\r\n\n                        \n                           UsageNote: Inverse code is SBE";
            case EAE: return "A relationship in which the source act ends after the target act ends.\r\n\n                        \n                           UsageNote: Inverse code is EBE";
            case SASEAE: return "The source Act starts after start of the target Act and ends after end of the target Act.\r\n\n                        \n                           UsageNote: Inverse code is SBSEBE";
            case SBEEAE: return "The source Act contains the end of the target Act.\r\n\n                        \n                           UsageNote: Inverse code is EDU";
            case SASSBEEAS: return "The source Act start after the start of the target Act, and contains the end of the target Act.\r\n\n                        \n                           UsageNote: Inverse code is SBSEASEBE";
            case SBSEAE: return "The source Act contains the time of the target Act.\r\n\n                        \n                           UsageNote: Inverse code is DURING";
            case SAS: return "The source Act starts after the start of the target Act (i.e. if we say \"ActOne SAS ActTwo\", it means that ActOne starts after the start of ActTwo, therefore ActOne is the source and ActTwo is the target).\r\n\n                        \n                           UsageNote: Inverse code is SBS";
            case SAE: return "A relationship in which the source act starts after the target act ends.\r\n\n                        \n                           UsageNote: Inverse code is EBS";
            case DURING: return "A relationship in which the source act's effective time is wholly within the target act's effective time (including end points, as defined in the act's effective times)\r\n\n                        \n                           UsageNote: Inverse code is SBSEAE";
            case SASECWE: return "The source Act starts after start of the target Act, and ends with the target Act.\r\n\n                        \n                           UsageNote: Inverse code is SBSECWE";
            case EASORECWS: return "A relationship in which the source act's effective time ends after or concurrent with the start of the target act's effective time.\r\n\n                        \n                           Usage Note: Inverse code is EBSORECWS";
            case EAEORECW: return "A relationship in which the source act's effective time ends after or concurrent with the end of the target act's effective time.\r\n\n                        \n                           Usage Note: Inverse code is EBEORECW";
            case INDEPENDENT: return "The source Act is independent of the time of the target Act.\r\n\n                        \n                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.";
            case SAEORSCWE: return "A relationship in which the source act's effective time starts after or concurrent with the end of the target act's effective time.\r\n\n                        \n                           Usage Note: Inverse code is SBEORSCWE";
            case SASORSCW: return "A relationship in which the source act's effective time starts after or concurrent with the start of the target act's effective time.\r\n\n                        \n                           Usage Note: Inverse code is SBSORSCW";
            case SBEORSCWE: return "A relationship in which the source act's effective time starts before or concurrent with the end of the target act's effective time.\r\n\n                        \n                           Usage Note: Inverse code is SAEORSCWE";
            case OVERLAP: return "A relationship in which the source act's effective time overlaps the target act's effective time in any way.\r\n\n                        \n                           UsageNote: This code is reflexive.  Therefore its inverse code is itself.";
            case EDU: return "A relationship in which the source act ends within the target act's effective time (including end points, as defined in the act's effective times)\r\n\n                        \n                           UsageNote: Inverse code is SBEEAE";
            case SBSEASEBE: return "The source Act contains the start of the target Act,  and ends before the end of the target Act.\r\n\n                        \n                           UsageNote: Inverse code is SASSBEEAS";
            case SBSEAS: return "The source Act contains the start of the target Act.\r\n\n                        \n                           UsageNote: Inverse code is SDU";
            case SDU: return "A relationship in which the source act starts within the target act's effective time (including end points, as defined in the act's effective times)\r\n\n                        \n                           UsageNote: Inverse code is SBSEAS";
            case SBE: return "The source Act starts before the end of the target Act (i.e. if we say \"ActOne SBE ActTwo\", it means that ActOne starts before the end of ActTwo, therefore ActOne is the source and ActTwo is the target).\r\n\n                        \n                           UsageNote: Inverse code is EAS";
            case EBE: return "The source Act ends before the end of the target Act (i.e. if we say \"ActOne EBE ActTwo\", it means that ActOne ends before the end of ActTwo, therefore ActOne is the source and ActTwo is the target).\r\n\n                        \n                           UsageNote: Inverse code is EAE";
            case SBSEBE: return "The source Act starts before the start of the target Act, and ends before the end of the target Act.\r\n\n                        \n                           UsageNote: Inverse code is SASEAE";
            case EBSORECWS: return "A relationship in which the source act's effective time ends before or concurrent with the start of the target act's effective time.\r\n\n                        \n                           Usage Note: Inverse code is EASORECWS";
            case EBS: return "A relationship in which the source act ends before the target act starts.\r\n\n                        \n                           UsageNote: Inverse code is SAE";
            case EBEORECW: return "A relationship in which the source act's effective time ends before or concurrent with the end of the target act's effective time.\r\n\n                        \n                           Usage Note: Inverse code is EAEORECW";
            case SBSORSCW: return "A relationship in which the source act's effective time starts before or concurrent with the start of the target act's effective time.\r\n\n                        \n                           Usage Note: Inverse code is SASORSCW";
            case SBS: return "A relationship in which the source act begins before the target act begins.\r\n\n                        \n                           UsageNote: Inverse code is SAS";
            case AUTH: return "A relationship in which the target act authorizes or certifies the source act.";
            case CAUS: return "Description: An assertion that an act was the cause of another act.This is stronger and more specific than the support link. The source (cause) is typically an observation, but may be any act, while the target may be any act.\r\n\n                        \n                           Examples:\n                        \r\n\n                        \n                           a growth of Staphylococcus aureus may be considered the cause of an abscess\n                           contamination of the infusion bag was deemed to be the cause of the infection that the patient experienced\n                           lack of staff on the shift was deemed to be a supporting factor (proximal factor) causing the patient safety incident where the patient fell out of bed because the  bed-sides had not been put up which caused the night patient to fall out of bed";
            case COMP: return "The target act is a component of the source act, with no semantics regarding composition or aggregation implied.";
            case CTRLV: return "A relationship from an Act to a Control Variable.  For example, if a Device makes an Observation, this relates the Observation to its Control Variables documenting  the device's settings that influenced the observation.";
            case MBR: return "The target Acts are aggregated by the source Act.  Target Acts may have independent existence, participate in multiple ActRelationships, and do not contribute to the meaning of the source.\r\n\n                        \n                           UsageNotes: This explicitly represents the conventional notion of aggregation.  The target Act is part of a collection of Acts (no implication is made of cardinality, a source of Acts may contain zero, one, or more member target Acts).\r\n\n                        It is expected that this will be primarily used with _ActClassRecordOrganizer, BATTERY, and LIST";
            case STEP: return "A collection of sub-services as steps or subtasks performed for the source service. Services may be performed sequentially or concurrently.\r\n\n                        \n                           UsageNotes: Sequence of steps may be indicated by use of _ActRelationshipTemporallyPertains, as well as via  ActRelationship.sequenceNumber, ActRelationship.pauseQuantity, Target.priorityCode.\r\n\n                        \n                           OpenIssue: Need Additional guidelines on when each approach should be used.";
            case ARR: return "The relationship that links to a Transportation Act (target) from another Act (source) indicating that the subject of the source Act entered into the source Act by means of the target Transportation act.";
            case DEP: return "The relationship that links to a Transportation Act (target) from another Act (source) indicating that the subject of the source Act departed from the source Act by means of the target Transportation act.";
            case PART: return "The source Act is a composite of the target Acts. The target Acts do not have an existence independent of the source Act.\r\n\n                        \n                           UsageNote: In UML 1.1, this is a \"composition\" defined as: \n                           \"A form of aggregation with strong ownership and coincident lifetime as part of the whole. Parts with non-fixed multiplicity may be created after the composite itself, but once created they live and die with it (i.e., they share lifetimes). Such parts can also be explicitly removed before the death of the composite. Composition may be recursive.\"";
            case COVBY: return "A relationship in which the source act is covered by or is under the authority of a target act.  A financial instrument such as an Invoice Element is covered by one or more specific instances of an Insurance Policy.";
            case DRIV: return "Associates a derived Act with its input parameters. E.G., an anion-gap observation can be associated as being derived from given sodium-, (potassium-,), chloride-, and bicarbonate-observations. The narrative content (Act.text) of a source act is wholly machine-derived from the collection of target acts.";
            case ELNK: return "Expresses an association that links two instances of the same act over time, indicating that the instance are part of the same episode, e.g. linking two condition nodes for episode of illness; linking two encounters for episode of encounter.";
            case EVID: return "Indicates that the target Act provides evidence in support of the action represented by the source Act. The target is not a 'reason' for the source act, but rather gives supporting information on why the source act is an appropriate course of action. Possible targets might be clinical trial results, journal articles, similar successful therapies, etc.\r\n\n                        \n                           Rationale: Provides a mechanism for conveying clinical justification for non-approved or otherwise non-traditional therapies.";
            case EXACBY: return "Description:The source act is aggravated by the target act. (Example \"chest pain\" EXACBY \"exercise\")";
            case EXPL: return "This is the inversion of support.  Used to indicate that a given observation is explained by another observation or condition.";
            case INTF: return "the target act documents a set of circumstances (events, risks) which prevent successful completion, or degradation of quality of, the source Act.\r\n\n                        \n                           UsageNote: This provides the semantics to document barriers to care";
            case ITEMSLOC: return "Items located";
            case LIMIT: return "A relationship that limits or restricts the source act by the elements of the target act.  For example, an authorization may be limited by a financial amount (up to $500). Target Act must be in EVN.CRIT mood.";
            case META: return "Definition: Indicates that the attributes and associations of the target act provide metadata (for example, identifiers, authorship, etc.) for the source act.\r\n\n                        \n                           Constraint:  Source act must have either a mood code that is not \"EVN\" (event) or its \"isCriterion\" attribute must set to \"true\".  Target act must be an Act with a mood code of EVN and with isCriterionInd attribute set to \"true\".";
            case MFST: return "An assertion that a new observation may be the manifestation of another existing observation or action.  This assumption is attributed to the same actor who asserts the manifestation.  This is stronger and more specific than an inverted support link.  For example, an agitated appearance can be asserted to be the manifestation (effect) of a known hyperthyroxia.  This expresses that one might not have realized a symptom if it would not be a common manifestation of a known condition.  The target (cause) may be any service, while the source (manifestation) must be an observation.";
            case NAME: return "Used to assign a \"name\" to a condition thread. Source is a condition node, target can be any service.";
            case OUTC: return "An observation that should follow or does actually follow as a result or consequence of a condition or action (sometimes called \"post-conditional\".) Target must be an observation as a goal, risk or any criterion. For complex outcomes a conjunction attribute (AND, OR, XOR) can be used.  An outcome link is often inverted to describe an outcome assessment.";
            case _ACTRELATIONSIPOBJECTIVE: return "The target act is a desired outcome of the source act. Source is any act (typically an intervention). Target must be an observation in criterion mood.";
            case OBJC: return "A desired state that a service action aims to maintain.  E.g., keep systolic blood pressure between 90 and 110 mm Hg.  Source is an intervention service.  Target must be an observation in criterion mood.";
            case OBJF: return "A desired outcome that a service action aims to meet finally.  Source is any service (typically an intervention).  Target must be an observation in criterion mood.";
            case GOAL: return "A goal that one defines given a patient's health condition.  Subsequently planned actions aim to meet that goal.  Source is an observation or condition node, target must be an observation in goal mood.";
            case RISK: return "A noteworthy undesired outcome of a patient's condition that is either likely enough to become an issue or is less likely but dangerous enough to be addressed.";
            case PERT: return "This is a very unspecific relationship from one item of clinical information to another.  It does not judge about the role the pertinent information plays.";
            case PREV: return "A relationship in which the target act is a predecessor instance to the source act.  Generally each of these instances is similar, but no identical.  In healthcare coverage it is used to link a claim item to a previous claim item that might have claimed for the same set of services.";
            case REFR: return "A relationship in which the target act is referred to by the source act.  This permits a simple reference relationship that distinguishes between the referent and the referee.";
            case USE: return "Indicates that the source act makes use of (or will make use of) the information content of the target act.\r\n\n                        \n                           UsageNotes: A usage relationship only makes sense if the target act is authored and occurs independently of the source act.  Otherwise a simpler relationship such as COMP would be appropriate.\r\n\n                        \n                           Rationale: There is a need when defining a clinical trial protocol to indicate that the protocol makes use of other protocol or treatment specifications.  This is stronger than the assertion of \"references\".  References may exist without usage, and in a clinical trial protocol is common to assert both: what other specifications does this trial use and what other specifications does it merely reference.";
            case REFV: return "Reference ranges are essentially descriptors of a class of result values assumed to be \"normal\", \"abnormal\", or \"critical.\"  Those can vary by sex, age, or any other criterion. Source and target are observations, the target is in criterion mood.  This link type can act as a trigger in case of alarms being triggered by critical results.";
            case RELVBY: return "Description:The source act is wholly or partially alleviated by the target act. (Example \"chest pain\" RELVBY \"sublingual nitroglycerin administration\")";
            case SEQL: return "An act relationship indicating that the source act follows the target act. The source act should in principle represent the same kind of act as the target. Source and target need not have the same mood code (mood will often differ). The target of a sequel is called antecedent. Examples for sequel relationships are: revision, transformation, derivation from a prototype (as a specialization is a derivation of a generalization), followup, realization, instantiation.";
            case APND: return "An addendum (source) to an existing service object (target), containing supplemental information.  The addendum is itself an original service object linked to the supplemented service object.  The supplemented service object remains in place and its content and status are unaltered.";
            case BSLN: return "Indicates that the target observation(s) provide an initial reference for the source observation or observation group.\r\n\n                        \n                           UsageConstraints: Both source and target must be Observations or specializations thereof.";
            case COMPLY: return "Description:The source act complies with, adheres to, conforms to, or is permissible under (in whole or in part) the policy, contract, agreement, law, conformance criteria, certification guidelines or requirement conveyed by the target act.\r\n\n                        Examples for compliance relationships are: audits of adherence with a security policy, certificate of conformance to system certification requirements, or consent directive in compliance with or permissible under a privacy policy.";
            case DOC: return "The source act documents the target act.";
            case FLFS: return "The source act fulfills (in whole or in part) the target act. Source act must be in a mood equal or more actual than the target act.";
            case OCCR: return "The source act is a single occurrence of a repeatable target act. The source and target act can be in any mood on the \"completion track\" but the source act must be as far as or further along the track than the target act (i.e., the occurrence of an intent can be an event but not vice versa).";
            case OREF: return "Relates either an appointment request or an appointment to the order for the service being scheduled.";
            case SCH: return "Associates a specific time (and associated resources) with a scheduling request or other intent.";
            case GEN: return "The generalization relationship can be used to express categorical knowledge about services (e.g., amilorid, triamterene, and spironolactone have the common generalization potassium sparing diuretic).";
            case GEVL: return "A goal-evaluation links an observation (intent or actual) to a goal to indicate that the observation evaluates the goal. Given the goal and the observation, a \"goal distance\" (e.g., goal to observation) can be \"calculated\" and need not be sent explicitly.";
            case INST: return "Used to capture the link between a potential service (\"master\" or plan) and an actual service, where the actual service instantiates the potential service. The instantiation may override the master's defaults.";
            case MOD: return "Definition: Used to link a newer version or 'snapshot' of a business object (source) to an older version or 'snapshot' of the same business object (target).\r\n\n                        \n                           Usage:The identifier of the Act should be the same for both source and target. If the identifiers are distinct, RPLC should be used instead.\r\n\n                        Name from source to target = \"modifiesPrior\"\r\n\n                        Name from target to source = \"modifiesByNew\"";
            case MTCH: return "A trigger-match links an actual service (e.g., an observation or procedure that took place) with a service in criterion mood.  For example if the trigger is \"observation of pain\" and pain is actually observed, and if that pain-observation caused the trigger to fire, that pain-observation can be linked with the trigger.";
            case OPTN: return "A relationship between a source Act that provides more detailed properties to the target Act.\r\n\n                        The source act thus is a specialization of the target act, but instead of mentioning all the inherited properties it only mentions new property bindings or refinements.\r\n\n                        The typical use case is to specify certain alternative variants of one kind of Act. The priorityNumber attribute is used to weigh refinements as preferred over other alternative refinements.\r\n\n                        Example: several routing options for a drug are specified as one SubstanceAdministration for the general treatment with attached refinements for the various routing options.";
            case RCHAL: return "Description:A relationship in which the target act is carried out to determine whether an effect attributed to the source act can be recreated.";
            case REV: return "A relationship between a source Act that seeks to reverse or undo the action of the prior target Act.\r\n\n                        Example: A posted financial transaction (e.g., a debit transaction) was applied in error and must be reversed (e.g., by a credit transaction) the credit transaction is identified as an undo (or reversal) of the prior target transaction.\r\n\n                        Constraints: the \"completion track\" mood of the target Act must be equally or more \"actual\" than the source act. I.e., when the target act is EVN the source act can be EVN, or any INT. If the target act is INT, the source act can be INT.";
            case RPLC: return "A replacement source act replaces an existing target act. The state of the target act being replaced becomes obselete, but the act is typically still retained in the system for historical reference.  The source and target must be of the same type.";
            case SUCC: return "Definition:  A new act that carries forward the intention of the original act, but does not completely replace it.  The status of the predecessor act must be 'completed'.  The original act is the target act and the successor is the source act.";
            case UPDT: return "A condition thread relationship specifically links condition nodes together to form a condition thread. The source is the new condition node and the target links to the most recent node of the existing condition thread.";
            case XCRPT: return "The source is an excerpt from the target.";
            case VRXCRPT: return "The source is a direct quote from the target.";
            case XFRM: return "Used when the target Act is a transformation of the source Act. (For instance, used to show that a CDA document is a transformation of a DICOM SR document.)";
            case SPRT: return "Used to indicate that an existing service is suggesting evidence for a new observation. The assumption of support is attributed to the same actor who asserts the observation. Source must be an observation, target may be any service  (e.g., to indicate a status post).";
            case SPRTBND: return "A specialization of \"has support\" (SPRT), used to relate a secondary observation to a Region of Interest on a multidimensional observation, if the ROI specifies the true boundaries of the secondary observation as opposed to only marking the approximate area.  For example, if the start and end of an ST elevation episode is visible in an EKG, this relation would indicate the ROI bounds the  \"ST elevation\" observation -- the ROI defines the true beginning and ending of the episode.  Conversely, if a ROI simply contains ST elevation, but it does not define the bounds (start and end) of the episode, the more general \"has support\" relation is used.  Likewise, if a ROI on an image defines the true bounds of a \"1st degree burn\", the relation \"has bounded support\" is used; but if the ROI only points to the approximate area of the burn, the general \"has support\" relation is used.";
            case SUBJ: return "Relates an Act to its subject Act that the first Act is primarily concerned with.\r\n\n                        Examples\r\n\n                        \n                           \n                              The first Act may be a ControlAct manipulating the subject Act \r\n\n                           \n                           \n                              The first act is a region of interest (ROI) that defines a region within the subject Act.\r\n\n                           \n                           \n                              The first act is a reporting or notification Act, that echos the subject Act for a specific new purpose.\r\n\n                           \n                        \n                        Constraints\r\n\n                        An Act may have multiple subject acts.\r\n\n                        Rationale\r\n\n                        The ActRelationshipType \"has subject\" is similar to the ParticipationType \"subject\", Acts that primarily operate on physical subjects use the Participation, those Acts that primarily operate on other Acts (other information) use the ActRelationship.";
            case QUALF: return "The target observation qualifies (refines) the semantics of the source observation.\r\n\n                        \n                           UsageNote: This is not intended to replace concept refinement and qualification via vocabulary.  It is used when there are multiple components which together provide the complete understanding of the source Act.";
            case SUMM: return "An act that contains summary values for a list or set of subordinate acts.  For example, a summary of transactions for a particular accounting period.";
            case VALUE: return "Description:Indicates that the target Act represents the result of the source observation Act.\r\n\n                        \n                           FormalConstraint: Source Act must be an Observation or specialization there-of. Source Act must not have the value attribute specified\r\n\n                        \n                           UsageNote: This relationship allows the result of an observation to be fully expressed as RIM acts as opposed to being embedded in the value attribute.  For example, sending a Document act as the result of an imaging observation, sending a list of Procedures and/or other acts as the result of a medical history observation.\r\n\n                        The valueNegationInd attribute on the source Act has the same semantics of \"negated finding\" when it applies to the target of a VALUE ActRelationship as it does to the value attribute.  On the other hand, if the ActRelationship.negationInd is true for a VALUE ActRelationship, that means the specified observation does not have the indicated value but does not imply a negated finding.  Because the semantics are extremely close, it is recommended that Observation.valueNegationInd be used, not ActRelationship.negationInd.\r\n\n                        \n                           OpenIssue: The implications of negationInd on ActRelationship and the valueNegationind on Observation.";
            case CURE: return "curative indication";
            case CURE_ADJ: return "adjunct curative indication";
            case MTGT_ADJ: return "adjunct mitigation";
            case RACT: return "";
            case SUGG: return "";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ART: return "act relationship type";
            case _ACTCLASSTEMPORALLYPERTAINS: return "ActClassTemporallyPertains";
            case _ACTRELATIONSHIPACCOUNTING: return "ActRelationshipAccounting";
            case _ACTRELATIONSHIPCOSTTRACKING: return "ActRelationshipCostTracking";
            case CHRG: return "has charge";
            case COST: return "has cost";
            case _ACTRELATIONSHIPPOSTING: return "ActRelationshipPosting";
            case CREDIT: return "has credit";
            case DEBIT: return "has debit";
            case _ACTRELATIONSHIPCONDITIONAL: return "ActRelationshipConditional";
            case CIND: return "has contra-indication";
            case PRCN: return "has pre-condition";
            case RSON: return "has reason";
            case BLOCK: return "blocks";
            case DIAG: return "diagnoses";
            case IMM: return "immunization against";
            case ACTIMM: return "active immunization against";
            case PASSIMM: return "passive immunization against";
            case MITGT: return "mitigates";
            case RCVY: return "recovers";
            case PRYLX: return "prophylaxis of";
            case TREAT: return "treats";
            case ADJUNCT: return "adjunctive treatment";
            case MTREAT: return "maintenance treatment";
            case PALLTREAT: return "palliates";
            case SYMP: return "symptomatic relief";
            case TRIG: return "has trigger";
            case _ACTRELATIONSHIPTEMPORALLYPERTAINS: return "ActRelationshipTemporallyPertains";
            case _ACTRELATIONSHIPTEMPORALLYPERTAINSAPPROXIMATES: return "ActRelationshipTemporallyPertainsApproximates";
            case ENE: return "ends near end";
            case ECW: return "ends concurrent with";
            case CONCURRENT: return "concurrent with";
            case SBSECWE: return "starts before start of, ends with";
            case ENS: return "ends near start";
            case ECWS: return "ends concurrent with start of";
            case SNE: return "starts near end";
            case SCWE: return "starts concurrent with end of";
            case SNS: return "starts near start";
            case SCW: return "starts concurrent with";
            case SCWSEBE: return "starts with. ends before end of";
            case SCWSEAE: return "starts with, ends after end of";
            case EAS: return "ends after start of";
            case EAE: return "ends after end of";
            case SASEAE: return "starts after start of, ends after end of";
            case SBEEAE: return "contains end of";
            case SASSBEEAS: return "start after start of, contains end of";
            case SBSEAE: return "contains time of";
            case SAS: return "starts after start of";
            case SAE: return "starts after end of";
            case DURING: return "occurs during";
            case SASECWE: return "starts after start of, ends with";
            case EASORECWS: return "ends after or concurrent with start of";
            case EAEORECW: return "ends after or concurrent with end of";
            case INDEPENDENT: return "independent of time of";
            case SAEORSCWE: return "starts after or concurrent with end of";
            case SASORSCW: return "starts after or concurrent with start of";
            case SBEORSCWE: return "starts before or concurrent with end of";
            case OVERLAP: return "overlaps with";
            case EDU: return "ends during";
            case SBSEASEBE: return "contains start of, ends before end of";
            case SBSEAS: return "contains start of";
            case SDU: return "starts during";
            case SBE: return "starts before end of";
            case EBE: return "ends before end of";
            case SBSEBE: return "starts before start of, ends before end of";
            case EBSORECWS: return "ends before or concurrent with start of";
            case EBS: return "ends before start of";
            case EBEORECW: return "ends before or concurrent with end of";
            case SBSORSCW: return "starts before or concurrent with start of";
            case SBS: return "starts before start of";
            case AUTH: return "authorized by";
            case CAUS: return "is etiology for";
            case COMP: return "has component";
            case CTRLV: return "has control variable";
            case MBR: return "has member";
            case STEP: return "has step";
            case ARR: return "arrival";
            case DEP: return "departure";
            case PART: return "has part";
            case COVBY: return "covered by";
            case DRIV: return "is derived from";
            case ELNK: return "episodeLink";
            case EVID: return "provides evidence for";
            case EXACBY: return "exacerbated by";
            case EXPL: return "has explanation";
            case INTF: return "interfered by";
            case ITEMSLOC: return "items located";
            case LIMIT: return "limited by";
            case META: return "has metadata";
            case MFST: return "is manifestation of";
            case NAME: return "assigns name";
            case OUTC: return "has outcome";
            case _ACTRELATIONSIPOBJECTIVE: return "Act Relationsip Objective";
            case OBJC: return "has continuing objective";
            case OBJF: return "has final objective";
            case GOAL: return "has goal";
            case RISK: return "has risk";
            case PERT: return "has pertinent information";
            case PREV: return "has previous instance";
            case REFR: return "refers to";
            case USE: return "uses";
            case REFV: return "has reference values";
            case RELVBY: return "relieved by";
            case SEQL: return "is sequel";
            case APND: return "is appendage";
            case BSLN: return "has baseline";
            case COMPLY: return "complies with";
            case DOC: return "documents";
            case FLFS: return "fulfills";
            case OCCR: return "occurrence";
            case OREF: return "references order";
            case SCH: return "schedules request";
            case GEN: return "has generalization";
            case GEVL: return "evaluates (goal)";
            case INST: return "instantiates (master)";
            case MOD: return "modifies";
            case MTCH: return "matches (trigger)";
            case OPTN: return "has option";
            case RCHAL: return "re-challenge";
            case REV: return "reverses";
            case RPLC: return "replaces";
            case SUCC: return "succeeds";
            case UPDT: return "updates (condition)";
            case XCRPT: return "Excerpts";
            case VRXCRPT: return "Excerpt verbatim";
            case XFRM: return "transformation";
            case SPRT: return "has support";
            case SPRTBND: return "has bounded support";
            case SUBJ: return "has subject";
            case QUALF: return "has qualifier";
            case SUMM: return "summarized by";
            case VALUE: return "has value";
            case CURE: return "curative indication";
            case CURE_ADJ: return "adjunct curative indication";
            case MTGT_ADJ: return "adjunct mitigation";
            case RACT: return "RACT";
            case SUGG: return "SUGG";
            default: return "?";
          }
    }


}

