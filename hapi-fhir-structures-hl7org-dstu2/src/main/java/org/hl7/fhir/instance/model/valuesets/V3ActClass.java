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


public enum V3ActClass {

        /**
         * A record of something that is being done, has been done, can be done, or is intended or requested to be done.

                        
                           Examples:The kinds of acts that are common in health care are (1) a clinical observation, (2) an assessment of health condition (such as problems and diagnoses), (3) healthcare goals, (4) treatment services (such as medication, surgery, physical and psychological therapy), (5) assisting, monitoring or attending, (6) training and education services to patients and their next of kin, (7) and notary services (such as advanced directives or living will), (8)  editing and maintaining documents, and many others.

                        
                           Discussion and Rationale: Acts are the pivot of the RIM; all domain information and processes are represented primarily in Acts. Any profession or business, including healthcare, is primarily constituted of intentional and occasionally non-intentional actions, performed and recorded by responsible actors. An Act-instance is a record of such an action.

                        Acts connect to Entities in their Roles through Participations and connect to other Acts through ActRelationships. Participations are the authors, performers and other responsible parties as well as subjects and beneficiaries (which includes tools and material used in the performance of the act, which are also subjects). The moodCode distinguishes between Acts that are meant as factual records, vs. records of intended or ordered services, and the other modalities in which act can appear.

                        One of the Participations that all acts have (at least implicitly) is a primary author, who is responsible of the Act and who "owns" the act. Responsibility for the act means responsibility for what is being stated in the Act and as what it is stated. Ownership of the act is assumed in the sense of who may operationally modify the same act. Ownership and responsibility of the Act is not the same as ownership or responsibility of what the Act-object refers to in the real world. The same real world activity can be described by two people, each being the author of their Act, describing the same real world activity. Yet one can be a witness while the other can be a principal performer. The performer has responsibilities for the physical actions; the witness only has responsibility for making a true statement to the best of his or her ability. The two Act-instances may even disagree, but because each is properly attributed to its author, such disagreements can exist side by side and left to arbitration by a recipient of these Act-instances.

                        In this sense, an Act-instance represents a "statement" according to Rector and Nowlan (1991) [Foundations for an electronic medical record. Methods Inf Med. 30.]  Rector and Nowlan have emphasized the importance of understanding the medical record not as a collection of facts, but "a faithful record of what clinicians have heard, seen, thought, and done." Rector and Nowlan go on saying that "the other requirements for a medical record, e.g., that it be attributable and permanent, follow naturally from this view." Indeed the Act class is this attributable statement, and the rules of updating acts (discussed in the state-transition model, see Act.statusCode) versus generating new Act-instances are designed according to this principle of permanent attributable statements.

                        Rector and Nolan focus on the electronic medical record as a collection of statements, while attributed statements, these are still mostly factual statements. However, the Act class goes beyond this limitation to attributed factual statements, representing what is known as "speech-acts" in linguistics and philosophy.  The notion of speech-act includes that there is pragmatic meaning in language utterances, aside from just factual statements; and that these utterances interact with the real world to change the state of affairs, even directly cause physical activities to happen. For example, an order is a speech act that (provided it is issued adequately) will cause the ordered action to be physically performed. The speech act theory has culminated in the seminal work by Austin (1962) [How to do things with words. Oxford University Press].

                        An activity in the real world may progress from defined, through planned and ordered to executed, which is represented as the mood of the Act. Even though one might think of a single activity as progressing from planned to executed, this progression is reflected by multiple Act-instances, each having one and only one mood that will not change along the Act-instance life cycle.  This is because the attribution and content of speech acts along this progression of an activity may be different, and it is often critical that a permanent and faithful record be maintained of this progression. The specification of orders or promises or plans must not be overwritten by the specification of what was actually done, so as to allow comparing actions with their earlier specifications. Act-instances that describe this progression of the same real world activity are linked through the ActRelationships (of the relationship category "sequel").

                        Act as statements or speech-acts are the only representation of real world facts or processes in the HL7 RIM. The truth about the real world is constructed through a combination (and arbitration) of such attributed statements only, and there is no class in the RIM whose objects represent "objective state of affairs" or "real processes" independent from attributed statements. As such, there is no distinction between an activity and its documentation. Every Act includes both to varying degrees. For example, a factual statement made about recent (but past) activities, authored (and signed) by the performer of such activities, is commonly known as a procedure report or original documentation (e.g., surgical procedure report, clinic note etc.). Conversely, a status update on an activity that is presently in progress, authored by the performer (or a close observer) is considered to capture that activity (and is later superceded by a full procedure report). However, both status update and procedure report are acts of the same kind, only distinguished by mood and state (see statusCode) and completeness of the information.
         */
        ACT, 
        /**
         * Used to group a set of acts sharing a common context. Organizer structures can nest within other context structures - such as where a document is contained within a folder, or a folder is contained within an EHR extract.
         */
        _ACTCLASSRECORDORGANIZER, 
        /**
         * A context representing a grouped commitment of information to the EHR. It is considered the unit of modification of the record, the unit of transmission in record extracts, and the unit of attestation by authorizing clinicians.

                        A composition represents part of a patient record originating from a single interaction between an authenticator and the record.

                        Unless otherwise stated all statements within a composition have the same authenticator, apply to the same patient and were recorded in a single session of use of a single application.

                        A composition contains organizers and entries.
         */
        COMPOSITION, 
        /**
         * The notion of a document comes particularly from the paper world, where it corresponds to the contents recorded on discrete pieces of paper. In the electronic world, a document is a kind of composition that bears resemblance to their paper world counter-parts. Documents typically are meant to be human-readable.

                        HL7's notion of document differs from that described in the W3C XML Recommendation, in which a document refers specifically to the contents that fall between the root element's start-tag and end-tag. Not all XML documents are HL7 documents.
         */
        DOC, 
        /**
         * A clinical document is a documentation of clinical observations and services, with the following characteristics:

                        
                           
                              Persistence - A clinical document continues to exist in an unaltered state, for a time period defined by local and regulatory requirements; 

                           
                           
                              Stewardship - A clinical document is maintained by a person or organization entrusted with its care; 

                           
                           
                              Potential for authentication - A clinical document is an assemblage of information that is intended to be legally authenticated; 

                           
                           
                              Wholeness - Authentication of a clinical document applies to the whole and does not apply to portions of the document without the full context of the document;

                           
                           
                              Human readability - A clinical document is human readable.
         */
        DOCCLIN, 
        /**
         * A clinical document that conforms to Level One of the HL7 Clinical Document Architecture (CDA)
         */
        CDALVLONE, 
        /**
         * Description: Container of clinical statements. Navigational. No semantic content. Knowledge of the section code is not required to interpret contained observations. Represents a heading in a heading structure, or "container tree".

                        The record entries relating to a single clinical session are usually grouped under headings that represent phases of the encounter, or assist with layout and navigation. Clinical headings usually reflect the clinical workflow during a care session, and might also reflect the main author's reasoning processes. Much research has demonstrated that headings are used differently by different professional groups and specialties, and that headings are not used consistently enough to support safe automatic processing of the E H R.
         */
        CONTAINER, 
        /**
         * A group of entries within a composition or topic that have a common characteristic - for example, Examination, Diagnosis, Management OR Subjective, Objective, Analysis, Plan.

                        The distinction from Topic relates to value sets. For Category there is a bounded list of things like "Examination", "Diagnosis" or SOAP categories. For Topic the list is wide open to any clinical condition or reason for a part of an encounter.

                        A CATEGORY MAY CONTAIN ENTRIES.
         */
        CATEGORY, 
        /**
         * A context that distinguishes the body of a document from the document header. This is seen, for instance, in HTML documents, which have discrete <head> and <body> elements.
         */
        DOCBODY, 
        /**
         * A context that subdivides the body of a document. Document sections are typically used for human navigation, to give a reader a clue as to the expected content. Document sections are used to organize and provide consistency to the contents of a document body. Document sections can contain document sections and can contain entries.
         */
        DOCSECT, 
        /**
         * A group of entries within a composition that are related to a common clinical theme - such as a specific disorder or problem, prevention, screening and provision of contraceptive services.

                        A topic may contain categories and entries.
         */
        TOPIC, 
        /**
         * This context represents the part of a patient record conveyed in a single communication. It is drawn from a providing system for the purposes of communication to a requesting process (which might be another repository, a client application or a middleware service such as an electronic guideline engine), and supporting the faithful inclusion of the communicated data in the receiving system.

                        An extract may be the entirety of the patient record as held by the sender or it may be a part of that record (e.g. changes since a specified date).

                        An extract contains folders or compositions.

                        An extract cannot contain another extract.
         */
        EXTRACT, 
        /**
         * A context that comprises all compositions. The EHR is an extract that includes the entire chart.

                        
                           NOTE: In an exchange scenario, an EHR is a specialization of an extract.
         */
        EHR, 
        /**
         * A context representing the high-level organization of an extract e.g. to group parts of the record by episode, care team, clinical specialty, clinical condition, or source application. Internationally, this kind of organizing structure is used variably: in some centers and systems the folder is treated as an informal compartmentalization of the overall health record; in others it might represent a significant legal portion of the EHR relating to the originating enterprise or team.

                        A folder contains compositions.

                        Folders may be nested within folders.
         */
        FOLDER, 
        /**
         * Definition: An ACT that organizes a set of component acts into a semantic grouping that share a particular context such as timeframe, patient, etc.

                        
                           UsageNotes: The focus in a GROUPER act is the grouping of the contained acts.  For example "a request to group" (RQO), "a type of grouping that is allowed to occur" (DEF), etc.

                        Unlike WorkingList, which represents a dynamic, shared, continuously updated collection to provide a "view" of a set of objects, GROUPER collections tend to be static and simply indicate a shared set of semantics.  Note that sharing of semantics can be achieved using ACT as well.  However, with GROUPER, the sole semantic is of grouping.
         */
        GROUPER, 
        /**
         * Description:An ACT that organizes a set of component acts into a semantic grouping that have a shared subject. The subject may be either a subject participation (SBJ), subject act relationship (SUBJ), or child participation/act relationship types.

                        
                           Discussion: The focus in a CLUSTER act is the grouping of the contained acts.  For example "a request to cluster" (RQO), "a type of cluster that is allowed to occur" (DEF), etc.

                        
                           Examples: 
                        

                        
                           
                              Radiologic investigations that might include administration of a dye, followed by radiographic observations;

                           
                           
                              "Isolate cluster" which includes all testing and specimen processing performed on a specific isolate;

                           
                           
                              a set of actions to perform at a particular stage in a clinical trial.
         */
        CLUSTER, 
        /**
         * An accommodation is a service provided for a Person or other LivingSubject in which a place is provided for the subject to reside for a period of time.  Commonly used to track the provision of ward, private and semi-private accommodations for a patient.
         */
        ACCM, 
        /**
         * A financial account established to track the net result of financial acts.
         */
        ACCT, 
        /**
         * A unit of work, a grouper of work items as defined by the system performing that work. Typically some laboratory order fulfillers communicate references to accessions in their communications regarding laboratory orders. Often one or more specimens are related to an accession such that in some environments the accession number is taken as an identifier for a specimen (group).
         */
        ACSN, 
        /**
         * A transformation process where a requested invoice is transformed into an agreed invoice.  Represents the adjudication processing of an invoice (claim).  Adjudication results can be adjudicated as submitted, with adjustments or refused.

                        Adjudication results comprise 2 components: the adjudication processing results and a restated (or adjudicated) invoice or claim
         */
        ADJUD, 
        /**
         * An act representing a system action such as the change of state of another act or the initiation of a query.  All control acts represent trigger events in the HL7 context.  ControlActs may occur in different moods.
         */
        CACT, 
        /**
         * Sender asks addressee to do something depending on the focal Act of the payload.  An example is "fulfill this order".  Addressee has responsibilities to either reject the message or to act on it in an appropriate way (specified by the specific receiver responsibilities for the interaction).
         */
        ACTN, 
        /**
         * Sender sends payload to addressee as information.  Addressee does not have responsibilities beyond serving addressee's own interest (i.e., read and memorize if you see fit).  This is equivalent to an FYI on a memo.
         */
        INFO, 
        /**
         * Description: Sender transmits a status change pertaining to the focal act of the payload. This status of the focal act is the final state of the state transition. This can be either a request or an event, according to the mood of the control act.
         */
        STC, 
        /**
         * An agreement of obligation between two or more parties that is subject to contractual law and enforcement.
         */
        CNTRCT, 
        /**
         * A contract whose value is measured in monetary terms.
         */
        FCNTRCT, 
        /**
         * When used in the EVN mood, this concept means with respect to a covered party:

                        
                           
                              A health care insurance policy or plan that is contractually binding between two or more parties; or 

                           
                           
                              A health care program, usually administered by government entities, that provides coverage to persons determined eligible under the terms of the program.

                           
                        
                        
                           
                              When used in the definition (DEF) mood, COV means potential coverage for a patient who may or may not be a covered party.

                           
                           
                              The concept's meaning is fully specified by the choice of ActCoverageTypeCode (abstract) ActProgramCode or ActInsurancePolicyCode.
         */
        COV, 
        /**
         * Definition: A worry that tends to persist over time and has as its subject a state or process. The subject of the worry has the potential to require intervention or management.

                        
                           Examples: an observation result, procedure, substance administration, equipment repair status, device recall status, a health risk, a financial risk, public health risk, pregnancy, health maintenance, allergy, and acute or chronic illness.
         */
        CONC, 
        /**
         * A public health case is a Concern about an observation or event that has a specific significance for public health. The creation of a PublicHealthCase initiates the tracking of the object of concern.  The decision to track is related to but somewhat independent of the underlying event or observation.

                        
                           UsageNotes: Typically a Public Health Case involves an instance or instances of a reportable infectious disease or other condition. The public health case can include a health-related event concerning a single individual or it may refer to multiple health-related events that are occurrences of the same disease or condition of interest to public health.

                        A public health case definition (Act.moodCode = "definition") includes the description of the clinical, laboratory, and epidemiologic indicators associated with a disease or condition of interest to public health. There are case definitions for conditions that are reportable, as well as for those that are not. A public health case definition is a construct used by public health for the purpose of counting cases, and should not be used as clinical indications for treatment. Examples include AIDS, toxic-shock syndrome, and salmonellosis and their associated indicators that are used to define a case.
         */
        HCASE, 
        /**
         * An Outbreak is a concern resulting from a series of public health cases.

                        
                           UsageNotes: The date on which an outbreak starts is the earliest date of onset among the cases assigned to the outbreak and its ending date is the last date of onset among the cases assigned to the outbreak. The effectiveTime attribute is used to convey the relevant dates for the case. An outbreak definition (Act.moodCode = "definition" includes the criteria for the number, types and occurrence pattern of cases necessary to declare an outbreak and to judge the severity of an outbreak.
         */
        OUTBR, 
        /**
         * The Consent class represents informed consents and all similar medico-legal transactions between the patient (or his legal guardian) and the provider. Examples are informed consent for surgical procedures, informed consent for clinical trials, advanced beneficiary notice, against medical advice decline from service, release of information agreement, etc.

                        The details of consents vary. Often an institution has a number of different consent forms for various purposes, including reminding the physician about the topics to mention. Such forms also include patient education material. In electronic medical record communication, consents thus are information-generating acts on their own and need to be managed similar to medical activities. Thus, Consent is modeled as a special class of Act.

                        The "signatures" to the consent document are represented electronically through Participation instances to the consent object. Typically an informed consent has Participation.typeCode of "performer", the healthcare provider informing the patient, and "consenter", the patient or legal guardian. Some consent may associate a witness or a notary public (e.g., living wills, advanced directives). In consents where a healthcare provider is not required (e.g. living will), the performer may be the patient himself or a notary public.

                        Some consent has a minimum required delay between the consent and the service, so as to allow the patient to rethink his decisions. This minimum delay can be expressed in the act definition by the ActRelationship.pauseQuantity attribute that delays the service until the pause time has elapsed after the consent has been completed.
         */
        CONS, 
        /**
         * An Act where a container is registered either via an automated sensor, such as a barcode reader,  or by manual receipt
         */
        CONTREG, 
        /**
         * An identified point during a clinical trial at which one or more actions are scheduled to be performed (definition mood), or are actually performed (event mood).  The actions may or may not involve an encounter between the subject and a healthcare professional.
         */
        CTTEVENT, 
        /**
         * An action taken with respect to a subject Entity by a regulatory or authoritative body with supervisory capacity over that entity. The action is taken in response to behavior by the subject Entity that body finds to be 							undesirable.

                        Suspension, license restrictions, monetary fine, letter of reprimand, mandated training, mandated supervision, etc.Examples:
         */
        DISPACT, 
        /**
         * An interaction between entities that provides opportunity for transmission of a physical, chemical, or biological agent from an exposure source entity to an exposure target entity.

                        
                           Examples:  The following examples are provided to indicate what interactions are considered exposures rather than other types of Acts:

                        
                           
                              A patient accidentally receives three times the recommended dose of their medication due to a dosing error. 

                              
                                 
                                    This is a substance administration.  Public health and/or safety authorities may also be interested in documenting this with an associated exposure.

                                 
                              
                           
                           
                              A patient accidentally is dispensed an incorrect medicine (e.g., clomiphene instead of clomipramine).  They have taken several doses before the mistake is detected.  They are therefore "exposed" to a medicine that there was no therapeutic indication for them to receive. 

                              
                                 
                                    There are several substance administrations in this example.  Public health and/or safety authorities may also be interested in documenting this with associated exposures.

                                 
                              
                           
                           
                              In a busy medical ward, a patient is receiving chemotherapy for a lymphoma.  Unfortunately, the IV infusion bag containing the medicine splits, spraying cytotoxic medication over the patient being treated and the patient in the adjacent bed. 

                              
                                 
                                    There are three substance administrations in this example.  The first is the intended one (IV infusion) with its associated (implicit) exposure.  There is an incident with an associated substance administration to the same patient involving the medication sprayed over the patient as well as an associated exposure.  Additionally, the incident includes a substance administration involving the spraying of medication on the adjacent patient, also with an associated exposure.

                                 
                              
                           
                           
                              A patient who is a refugee from a war-torn African nation arrives in a busy inner city A&E department suffering from a cough with bloody sputum.  Not understanding the registration and triage process, they sit in the waiting room for several hours before it is noticed that they have not booked in.  As soon as they are being processed, it is suspected that they are suffering from TB.  Vulnerable (immunosuppressed) patients who were sharing the waiting room with this patient may have been exposed to the tubercule bacillus, and must be traced for investigation. 

                              
                                 
                                    This is an exposure (or possibly multiple exposures) in the waiting room involving the refugee and everyone else in the waiting room during the period.  There might also be a number of known or presumed substance administrations (coughing) via several possible routes.  The substance administrations are only hypotheses until confirmed by further testing.

                                 
                              
                           
                           
                              A patient who has received an elective total hip replacement procedure suffers a prolonged stay in hospital, due to contracting an MRSA infection in the surgical wound site after the surgery. 

                              
                                 
                                    This is an exposure to MRSA.  Although there was some sort of substance administration, it's possible the exact mechanism for introduction of the MRSA into the wound will not be identified.

                                 
                              
                           
                           
                              Routine maintenance of the X-ray machines at a local hospital reveals a serious breach of the shielding on one of the machines.  Patients who have undergone investigations using that machine in the last month are likely to have been exposed to significantly higher doses of X-rays than was intended, and must be tracked for possible adverse effects. 

                              
                                 
                                    There has been an exposure of each patient who used the machine in the past 30 days. Some patients may have had substance administrations.

                                 
                              
                           
                           
                              A new member of staff is employed in the laundry processing room of a small cottage hospital, and a misreading of the instructions for adding detergents results in fifty times the usual concentration of cleaning materials being added to a batch of hospital bedding.  As a result, several patients have been exposed to very high levels of detergents still present in the "clean" bedding, and have experienced dermatological reactions to this. 

                              
                                 
                                    There has been an incident with multiple exposures to several patients.  Although there are substance administrations involving the application of the detergent to the skin of the patients, it is expected that the substance administrations would not be directly documented.

                                 
                              
                           
                           
                              Seven patients who are residents in a health care facility for the elderly mentally ill have developed respiratory problems. After several months of various tests having been performed and various medications prescribed to these patients, the problem is traced to their being "sensitive" to a new fungicide used in the wall plaster of the ward where these patients reside.

                              
                                 
                                    The patients have been continuously exposed to the fungicide.  Although there have been continuous substance administrations (via breathing) this would not normally be documented as a substance administration.

                                 
                              
                           
                           
                              A patient with osteoarthritis of the knees is treated symptomatically using analgesia, paracetamol (acetaminophen) 1g up to four times a day for pain relief.  His GP does not realize that the patient has, 20 years previously (while at college) had severe alcohol addiction problems, and now, although this is completely under control, his liver has suffered significantly, leaving him more sensitive to hepatic toxicity from paracetamol use.  Later that year, the patient returns with a noticeable level of jaundice.  Paracetamol is immediately withdrawn and alternative solutions for the knee pain are sought.  The jaundice gradually subsides with conservative management, but referral to the gastroenterologist is required for advice and monitoring. 

                              
                                 
                                    There is a substance administration with an associated exposure.  The exposure component is based on the relative toxic level of the substance to a patient with a compromised liver function.

                                 
                              
                           
                           
                              A patient goes to their GP complaining of abdominal pain, having been discharged from the local hospital ten days' previously after an emergency appendectomy.  The GP can find nothing particularly amiss, and presumes it is post operative surgical pain that will resolve.  The patient returns a fortnight later, when the GP prescribes further analgesia, but does decide to request an outpatient surgical follow-up appointment.  At this post-surgical outpatient review, the registrar decides to order an ultrasound, which, when performed three weeks later, shows a small faint inexplicable mass.  A laparoscopy is then performed, as a day case procedure, and a piece of a surgical swab is removed from the patient's abdominal cavity.  Thankfully, a full recovery then takes place. 

                              
                                 
                                    This is a procedural sequelae.  There may be an Incident recorded for this also.

                                 
                              
                           
                           
                              A patient is slightly late for a regular pacemaker battery check in the Cardiology department of the local hospital.  They are hurrying down the second floor corridor.  A sudden summer squall has recently passed over the area, and rain has come in through an open corridor window leaving a small puddle on the corridor floor.  In their haste, the patient slips in the puddle and falls so badly that they have to be taken to the A&E department, where it is discovered on investigation they have slightly torn the cruciate ligament in their left knee. 

                              
                                 
                                    This is not an exposure.  There has been an incident.  

                                 
                              
                           
                        
                        
                           Usage Notes: This class deals only with opportunity and not the outcome of the exposure; i.e. not all exposed parties will necessarily experience actual harm or benefit.

                        Exposure differs from Substance Administration by the absence of the participation of a performer in the act. 

                        The following participations SHOULD be used with the following participations to distinguish the specific entities:

                        
                           
                              The exposed entity participates via the "exposure target" (EXPTRGT) participation.

                           
                           
                              An entity that has carried the agent transmitted in the exposure participates via the "exposure source" (EXSRC) participation.  For example: 

                              
                                 
                                    a person or animal who carried an infectious disease and interacts (EXSRC) with another person or animal (EXPTRGT) transmitting the disease agent;

                                 
                                 
                                    a place or other environment (EXSRC) and a person or animal (EXPTRGT) who is exposed in the presence of this environment.

                                 
                              
                           
                           
                              When it is unknown whether a participating entity is the source of the agent (EXSRC) or the target of the transmission (EXPTRGT), the "exposure participant" (EXPART) is used.

                           
                           
                              The physical (including energy), chemical or biological substance which is participating in the exposure uses the "exposure agent" (EXPAGNT) participation.  There are at least three scenarios:

                              
                                 
                                    the player of the Role that participates as EXPAGNT is the chemical or biological substance mixed or carried by the scoper-entity of the Role (e.g., ingredient role); or 

                                 
                                 
                                    the player of the Role that participates as EXPAGNT is a mixture known to contain the chemical, radiological or biological substance of interest; or 

                                 
                                 
                                    the player of the Role that participates as a EXPAGNT is known to carry the agent (i.e., the player is a fomite, vector, etc.).

                                 
                              
                           
                        
                        The Exposure.statusCode attribute should be interpreted as the state of the Exposure business object (e.g., active, aborted, completed) and not the clinical status of the exposure (e.g., probable, confirmed).  The clinical status of the exposure should be associated with the exposure via a subject observation.

                        
                           Design Comment: The usage notes require a clear criterion for determining whether an act is an exposure or substance administration-deleterious potential, uncertainty of actual transmission, or otherwise. SBADM states that the criterion is the presence of a performer-but there are examples above that call this criterion into question (e.g., the first one, concerning a dosing error).
         */
        EXPOS, 
        /**
         * Description: 
                        

                        An acquisition exposure act describes the proximity (location and time) through which the participating entity was potentially exposed to a physical (including energy), chemical or biological agent from another entity.  The acquisition exposure act is used in conjunction with transmission exposure acts as part of an analysis technique for contact tracing.  Although an exposure can be decomposed into transmission and acquisition exposures, there is no requirement that all exposures be treated in this fashion.

                        
                           Constraints:  The Acquisition Exposure inherits the participation constraints that apply to Exposure with the following exception.  The EXPSRC (exposure source) participation must never be associated with the Transmission Exposure either directly or via context conduction.
         */
        AEXPOS, 
        /**
         * Description: 
                        

                        A transmission exposure act describes the proximity (time and location) over which the participating source entity was capable of transmitting a physical (including energy), chemical or biological substance agent to another entity.  The transmission exposure act is used in conjunction with acquisition exposure acts as part of an analysis technique for contact tracing.  Although an exposure can be decomposed into transmission and acquisition exposures, there is no requirement that all exposures be treated in this fashion.

                        
                           Constraints:  The Transmission Exposure inherits the participation constraints that apply to Exposure with the following exception.  The EXPTRGT (exposure target) participation must never be associated with the Transmission Exposure either directly or via context conduction.
         */
        TEXPOS, 
        /**
         * An event that occurred outside of the control of one or more of the parties involved.  Includes the concept of an accident.
         */
        INC, 
        /**
         * The act  of transmitting information and understanding about a topic to a subject where the participation association must be SBJ.

                        
                           Discussion: This act may be used to request that a patient or provider be informed about an Act, or to indicate that a person was informed about a particular act.
         */
        INFRM, 
        /**
         * Represents concepts related to invoice processing in health care
         */
        INVE, 
        /**
         * Working list collects a dynamic list of individual instances of Act via ActRelationship which reflects the need of an individual worker, team of workers, or an organization to manage lists of acts for many different clinical and administrative reasons. Examples of working lists include problem lists, goal lists, allergy lists, and to-do lists.
         */
        LIST, 
        /**
         * An officially or unofficially instituted program to track acts of a particular type or categorization.
         */
        MPROT, 
        /**
         * Description:An act that is intended to result in new information about a subject. The main difference between Observations and other Acts is that Observations have a value attribute. The code attribute of Observation and the value attribute of Observation must be considered in combination to determine the semantics of the observation.

                        
                           Discussion:
                        

                        Structurally, many observations are name-value-pairs, where the Observation.code (inherited from Act) is the name and the Observation.value is the value of the property. Such a construct is also known as a  variable (a named feature that can assume a value) hence, the Observation class is always used to hold generic name-value-pairs or variables, even though the variable valuation may not be the result of an elaborate observation method. It may be a simple answer to a question or it may be an assertion or setting of a parameter.

                        As with all Act statements, Observation statements describe what was done, and in the case of Observations, this includes a description of what was actually observed (results or answers); and those results or answers are part of the observation and not split off into other objects. 

                        The method of action is asserted by the Observation classCode or its subclasses at the least granular level, by the Observation.code attribute value at the medium level of granularity, and by the attribute value of observation.methodCode when a finer level of granularity is required. The method in whole or in part may also appear in the attribute value of Observation.value when using coded data types to express the value of the attribute. Relevant aspects of methodology may also be restated in value when the results themselves imply or state a methodology.

                        An observation may consist of component observations each having their own Observation.code and Observation.value. In this case, the composite observation may not have an Observation.value for itself. For instance, a white blood cell count consists of the sub-observations for the counts of the various granulocytes, lymphocytes and other normal or abnormal blood cells (e.g., blasts). The overall white blood cell count Observation itself may therefore not have a value by itself (even though it could have one, e.g., the sum total of white blood cells). Thus, as long as an Act is essentially an Act of recognizing and noting information about a subject, it is an Observation, regardless of whether it has a simple value by itself or whether it has sub-observations.

                        Even though observations are professional acts (see Act) and as such are intentional actions, this does not require that every possible outcome of an observation be pondered in advance of it being actually made. For instance, differential white blood cell counts (WBC) rarely show blasts, but if they do, this is part of the WBC observation even though blasts might not be predefined in the structure of a normal WBC. 

                        Clinical documents commonly have Subjective and Objective findings, both of which are kinds of Observations. In addition, clinical documents commonly contain Assessments, which are also kinds of Observations. Thus, the establishment of a diagnosis is an Observation. 

                        
                           Examples:
                        

                        
                           
                              Recording the results of a Family History Assessment

                           
                           
                              Laboratory test and associated result

                           
                           
                              Physical exam test and associated result

                           
                           
                              Device temperature

                           
                           
                              Soil lead level
         */
        OBS, 
        /**
         * Regions of Interest (ROI) within a subject Act. Primarily used for making secondary observations on a subset of a subject observation. The relationship between a ROI and its referenced Act is specified through an ActRelationship of type "subject" (SUBJ), which must always be present.
         */
        _ACTCLASSROI, 
        /**
         * A Region of Interest (ROI) specified for a multidimensional observation, such as an Observation Series (OBSSER). The ROI is specified using a set of observation criteria, each delineating the boundary of the region in one of the dimensions in the multidimensional observation. The relationship between a ROI and its referenced Act is specified through an ActRelationship of type subject (SUBJ), which must always be present. Each of the boundary criteria observations is connected with the ROI using ActRelationships of type "has component" (COMP). In each boundary criterion, the Act.code names the dimension and the Observation.value specifies the range of values inside the region. Typically the bounded dimension is continuous, and so the Observation.value will be an interval (IVL) data type. The Observation.value need not be specified if the respective dimension is only named but not constrained. For example, an ROI for the QT interval of a certain beat in ECG Lead II would contain 2 boundary criteria, one naming the interval in time (constrained), and the other naming the interval in ECG Lead II (only named, but not constrained).
         */
        ROIBND, 
        /**
         * A Region of Interest (ROI) specified for an image using an overlay shape. Typically used to make reference to specific regions in images, e.g., to specify the location of a radiologic finding in an image or to specify the site of a physical finding by "circling" a region in a schematic picture of a human body. The units of the coordinate values are in pixels.  The origin is in the upper left hand corner, with positive X values going to the right and positive Y values going down. The relationship between a ROI and its referenced Act is specified through an ActRelationship of type "subject" (SUBJ), which must always be present.
         */
        ROIOVL, 
        /**
         * The spatial relationship of a subject whether human, other animal, or plant, to a frame of reference such as gravity or a collection device.
         */
        _SUBJECTPHYSICALPOSITION, 
        /**
         * Contains codes for defining the observed, physical position of a subject, such as during an observation, assessment, collection of a specimen, etc.  ECG waveforms and vital signs, such as blood pressure, are two examples where a general, observed position typically needs to be noted.

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        _SUBJECTBODYPOSITION, 
        /**
         * Lying on the left side.

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        LLD, 
        /**
         * Lying with the front or ventral surface downward; lying face down.

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        PRN, 
        /**
         * Lying on the right side.

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        RLD, 
        /**
         * A semi-sitting position in bed with the head of the bed elevated approximately 45 degrees.

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        SFWL, 
        /**
         * Resting the body on the buttocks, typically with upper torso erect or semi erect.

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        SIT, 
        /**
         * To be stationary, upright, vertical, on one's legs.

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        STN, 
        /**
         * Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        SUP, 
        /**
         * Lying on the back, on an inclined plane, typically about 30-45 degrees with head raised and feet lowered.

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        RTRD, 
        /**
         * Lying on the back, on an inclined plane, typically about 30-45 degrees, with  head lowered and feet raised.

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.
         */
        TRD, 
        /**
         * An observation identifying a potential adverse outcome as a result of an Act or combination of Acts.

                        
                           Examples: Detection of a drug-drug interaction; Identification of a late-submission for an invoice; Requesting discharge for a patient who does not meet hospital-defined discharge criteria.

                        
                           Discussion: This class is commonly used for identifying 'business rule' or 'process' problems that may result in a refusal to carry out a particular request. In some circumstances it may be possible to 'bypass' a problem by modifying the request to acknowledge the issue and/or by providing some form of mitigation.

                        
                           Constraints: the Act or Acts that may cause the the adverse outcome are the target of a subject ActRelationship. The subbtypes of this concept indicate the type of problem being detected (e.g. drug-drug interaction) while the Observation.value is used to repesent a specific problem code (e.g. specific drug-drug interaction id).
         */
        ALRT, 
        /**
         * Definition: An observation that is composed of a set of observations. These observations typically have a logical or practical grouping for generally accepted clinical or functional purposes, such as observations that are run together because of automation. A battery can define required and optional component observations and, in some cases, will define complex rules that determine whether or not a particular observation is made. BATTERY is a constraint on the Observation class in that it is understood to always be composed of component observations.

                        
                           UsageNotes: The focus in a BATTERY is that it is composed of individual observations. In request (RQO) mood, a battery is a request to perform the component observations. In event (EVN) mood a battery is a reporting of associated set of observation events. In definition mood a battery is the definition of the associated set of observations.

                        
                           Examples: Vital signs, Full blood count, Chemistry panel.
         */
        BATTERY, 
        /**
         * The set of actions that define an experiment to assess the effectiveness and/or safety of a biopharmaceutical product (food, drug, device, etc.).  In definition mood, this set of actions is often embodied in a clinical trial protocol; in event mood, this designates the aggregate act of applying the actions to one or more subjects.
         */
        CLNTRL, 
        /**
         * An instance of Observation of a Condition at a point in time that includes any Observations or Procedures associated with that Condition as well as links to previous instances of Condition Node for the same Condition

                        
                           
                              Deprecation Comment: 
                           This concept has been deprecated because an alternative structure for tracking the evolution of a problem has been presented and adopted by the Care Provision Work Group.
         */
        CNOD, 
        /**
         * An observable finding or state that persists over time and tends to require intervention or management, and, therefore, distinguished from an Observation made at a point in time; may exist before an Observation of the Condition is made or after interventions to manage the Condition are undertaken. Examples: equipment repair status, device recall status, a health risk, a financial risk, public health risk, pregnancy, health maintenance, chronic illness
         */
        COND, 
        /**
         * A public health case is an Observation representing a condition or event that has a specific significance for public health. Typically it involves an instance or instances of a reportable infectious disease or other condition. The public health case can include a health-related event concerning a single individual or it may refer to multiple health-related events that are occurrences of the same disease or condition of interest to public health. An outbreak involving multiple individuals may be considered as a type of public health case. A public health case definition (Act.moodCode = "definition") includes the description of the clinical, laboratory, and epidemiologic indicators associated with a disease or condition of interest to public health. There are case definitions for conditions that are reportable, as well as for those that are not. There are also case definitions for outbreaks. A public health case definition is a construct used by public health for the purpose of counting cases, and should not be used as clinical indications for treatment. Examples include AIDS, toxic-shock syndrome, and salmonellosis and their associated indicators that are used to define a case.
         */
        CASE, 
        /**
         * An outbreak represents a series of public health cases. The date on which an outbreak starts is the earliest date of onset among the cases assigned to the outbreak, and its ending date is the last date of onset among the cases assigned to the outbreak.
         */
        OUTB, 
        /**
         * Class for holding attributes unique to diagnostic images.
         */
        DGIMG, 
        /**
         * Description:An observation of genomic phenomena.
         */
        GEN, 
        /**
         * Description:A determinant peptide in a polypeptide as described by polypeptide.
         */
        DETPOL, 
        /**
         * Description:An expression level of genes/proteins or other expressed genomic entities.
         */
        EXP, 
        /**
         * Description:The position of a gene (or other significant sequence) on the genome.
         */
        LOC, 
        /**
         * Description:A genomic phenomenon that is expressed externally in the organism.
         */
        PHN, 
        /**
         * Description:A polypeptide resulting from the translation of a gene.
         */
        POL, 
        /**
         * Description:A sequence of biomolecule like the DNA, RNA, protein and the like.
         */
        SEQ, 
        /**
         * Description:A variation in a sequence as described by BioSequence.
         */
        SEQVAR, 
        /**
         * An formalized inquiry into the circumstances surrounding a particular unplanned event or potential event for the purposes of identifying possible causes and contributing factors for the event. This investigation could be conducted at a local institutional level or at the level of a local or national government.
         */
        INVSTG, 
        /**
         * Container for Correlated Observation Sequences sharing a common frame of reference.  All Observations of the same cd must be comparable and relative to the common frame of reference.  For example, a 3-channel ECG device records a 12-lead ECG in 4 steps (3 leads at a time).  Each of the separate 3-channel recordings would be in their own "OBSCOR".  And, all 4 OBSCOR would be contained in one OBSSER because all the times are relative to the same origin (beginning of the recording) and all the ECG signals were from a fixed set of electrodes.
         */
        OBSSER, 
        /**
         * Container for Observation Sequences (Observations whose values are contained in LIST<>'s) having values correlated with each other.  Each contained Observation Sequence LIST<> must be the same length.  Values in the LIST<>'s are correlated based on index.  E.g. the values in position 2 in all the LIST<>'s are correlated.  This is analogous to a table where each column is an Observation Sequence with a LIST<> of values, and each row in the table is a correlation between the columns.  For example, a 12-lead ECG would contain 13 sequences: one sequence for time, and a sequence for each of the 12 leads.
         */
        OBSCOR, 
        /**
         * An observation denoting the physical location of a person or thing based on a reference coordinate system.
         */
        POS, 
        /**
         * Description:An observation representing the degree to which the assignment of the spatial coordinates, based on a matching algorithm by a geocoding engine against a reference spatial database, matches true or accepted values.
         */
        POSACC, 
        /**
         * Description:An observation representing one of a set of numerical values used to determine the position of a place.  The name of the coordinate value is determined by the reference coordinate system.
         */
        POSCOORD, 
        /**
         * An observation on a specimen in a laboratory environment that may affect processing, analysis or result interpretation
         */
        SPCOBS, 
        /**
         * An act which describes the process whereby a 'verifying party' validates either the existence of the Role attested to by some Credential or the actual Vetting act and its details.
         */
        VERIF, 
        /**
         * An Act that of taking on whole or partial responsibility for, or attention to, safety and well-being of a subject of care. 

                        
                           Discussion: A care provision event may exist without any other care actions taking place. For example, when a patient is assigned to the care of a particular health professional.

                        In request (RQO) mood care provision communicates a referral, which is a request:

                        
                           
                              from one party (linked as a participant of type author (AUT)),

                           
                           
                              to another party (linked as a participant of type performer (PRF),

                           
                           
                              to take responsibility for a scope specified by the code attribute, 

                           
                           
                              for an entity (linked as a participant of type subject (SBJ)).

                           
                        
                        The scope of the care for which responsibility is taken is identified by code attribute.

                        In event (EVN) mood care provision indicates the effective time interval of a specified scope of responsibility by a performer (PRF) or set of performers (PRF) for a subject (SBJ).

                        
                           Examples:
                        

                        
                           
                              Referral from GP to a specialist.

                           
                           
                              Assignment of a patient or group of patients to the case list of a health professional.

                           
                           
                              Assignment of inpatients to the care of particular nurses for a working shift.
         */
        PCPR, 
        /**
         * An interaction between a patient and healthcare participant(s) for the purpose of providing patient service(s) or assessing the health status of a patient.  For example, outpatient visit to multiple departments, home health support (including physical therapy), inpatient hospital stay, emergency room visit, field visit (e.g., traffic accident), office visit, occupational therapy, telephone call.
         */
        ENC, 
        /**
         * Description:A mandate, regulation, obligation, requirement, rule, or expectation unilaterally imposed by one party on:

                        
                           
                              The activity of another party

                           
                           
                              The behavior of another party

                           
                           
                              The manner in which an act is executed
         */
        POLICY, 
        /**
         * Description:A mandate, regulation, obligation, requirement, rule, or expectation unilaterally imposed by a jurisdiction on:

                        
                           
                              The activity of another party

                           
                           
                              The behavior of another party

                           
                           
                              The manner in which an act is executed

                           
                        
                        
                           Examples:A jurisdictional mandate regarding the prescribing and dispensing of a particular medication.  A jurisdictional privacy or security regulation dictating the manner in which personal health information is disclosed.  A jurisdictional requirement that certain services or health conditions are reported to a monitoring program, e.g., immunizations, methadone treatment, or cancer registries.
         */
        JURISPOL, 
        /**
         * Description:A mandate, obligation, requirement, rule, or expectation unilaterally imposed by an organization on:

                        
                           
                              The activity of another party

                           
                           
                              The behavior of another party

                           
                           
                              The manner in which an act is executed

                           
                        
                        
                           Examples:A clinical or research protocols imposed by a payer, a malpractice insurer, or an institution to which a provider must adhere.  A mandate imposed by a denominational institution for a provider to provide or withhold certain information from the patient about treatment options.
         */
        ORGPOL, 
        /**
         * Description:An ethical or clinical obligation, requirement, rule, or expectation imposed or strongly encouraged by organizations that oversee particular clinical domains or provider certification which define the boundaries within which a provider may practice and which may have legal basis or ramifications on:

                        
                           
                              The activity of another party

                           
                           
                              The behavior of another party

                           
                           
                              The manner in which an act is executed

                           
                        
                        
                           Examples:An ethical obligation for a provider to fully inform a patient about all treatment options.  An ethical obligation for a provider not to disclose personal health information that meets certain criteria, e.g., where disclosure might result in harm to the patient or another person.  The set of health care services which a provider is credentialed or privileged to provide.
         */
        SCOPOL, 
        /**
         * Description:A requirement, rule, or expectation typically documented as guidelines, protocols, or formularies imposed or strongly encouraged by an organization that oversees or has authority over the practices within a domain, and which may have legal basis or ramifications on:

                        
                           
                              The activity of another party

                           
                           
                              The behavior of another party

                           
                           
                              The manner in which an act is executed

                           
                        
                        
                           Examples:A payer may require a prescribing provider to adhere to formulary guidelines.  An institution may adopt clinical guidelines and protocols and implement these within its electronic health record and decision support systems.
         */
        STDPOL, 
        /**
         * An Act whose immediate and primary outcome (post-condition) is the alteration of the physical condition of the subject.

                        
                           Examples: : Procedures may involve the disruption of some body surface (e.g. an incision in a surgical procedure), but they also include conservative procedures such as reduction of a luxated join, chiropractic treatment, massage, balneotherapy, acupuncture, shiatsu, etc. Outside of clinical medicine, procedures may be such things as alteration of environments (e.g. straightening rivers, draining swamps, building dams) or the repair or change of machinery etc.
         */
        PROC, 
        /**
         * The act of introducing or otherwise applying a substance to the subject.

                        
                           Discussion: The effect of the substance is typically established on a biochemical basis, however, that is not a requirement. For example, radiotherapy can largely be described in the same way, especially if it is a systemic therapy such as radio-iodine.  This class also includes the application of chemical treatments to an area.

                        
                           Examples: Chemotherapy protocol; Drug prescription; Vaccination record
         */
        SBADM, 
        /**
         * Description: The act of removing a substance from the subject.
         */
        SBEXT, 
        /**
         * A procedure for obtaining a specimen from a source entity.
         */
        SPECCOLLECT, 
        /**
         * Represents the act of maintaining information about the registration of its associated registered subject. The subject can be either an Act or a Role, and includes subjects such as lab exam definitions, drug protocol definitions, prescriptions, persons, patients, practitioners, and equipment.

                        The registration may have a unique identifier - separate from the unique identification of the subject - as well as a core set of related participations and act relationships that characterize the registration event and aid in the disposition of the subject information by a receiving system.
         */
        REG, 
        /**
         * The act of examining and evaluating the subject, usually another act. For example, "This prescription needs to be reviewed in 2 months."
         */
        REV, 
        /**
         * A procedure or treatment performed on a specimen to prepare it for analysis
         */
        SPCTRT, 
        /**
         * Supply orders and deliveries are simple Acts that focus on the delivered product. The product is associated with the Supply Act via Participation.typeCode="product". With general Supply Acts, the precise identification of the Material (manufacturer, serial numbers, etc.) is important.  Most of the detailed information about the Supply should be represented using the Material class.  If delivery needs to be scheduled, tracked, and billed separately, one can associate a Transportation Act with the Supply Act.  Pharmacy dispense services are represented as Supply Acts, associated with a SubstanceAdministration  Act. The SubstanceAdministration class represents the administration of medication, while dispensing is supply.
         */
        SPLY, 
        /**
         * Diet services are supply services, with some aspects resembling Medication services: the detail of the diet is given as a description of the Material associated via Participation.typeCode="product". Medically relevant diet types may be communicated in the Diet.code attribute using domain ActDietCode, however, the detail of the food supplied and the various combinations of dishes should be communicated as Material instances.

                        
                           Deprecation Note
                        

                        
                           Class: Use either the Supply class (if dealing with what should be given to the patient) or SubstanceAdministration class (if dealing with what the patient should consume)

                        
                           energyQuantity: This quantity can be conveyed by using a Content relationship with a quantity attribute expressing the calories

                        
                           carbohydrateQuantity:This quantity can be conveyed using a Content relationship to an Entity with a code of  carbohydrate and a quantity attribute on the content relationship.
         */
        DIET, 
        /**
         * The act of putting something away for safe keeping. The "something" may be physical object such as a specimen, or information, such as observations regarding a specimen.
         */
        STORE, 
        /**
         * Definition: Indicates that the subject Act has undergone or should undergo substitution of a type indicated by Act.code.

                        Rationale: Used to specify "allowed" substitution when creating orders, "actual" susbstitution when sending events, as well as the reason for the substitution and who was responsible for it.
         */
        SUBST, 
        /**
         * Definition: The act of transferring information without the intent of imparting understanding about a topic to the subject that is the recipient or holder of the transferred information where the participation association must be RCV or HLD.
         */
        TRFR, 
        /**
         * Transportation is the moving of a payload (people or material) from a location of origin to a destination location.  Thus, any transport service has the three target instances of type payload, origin, and destination, besides the targets that are generally used for any service (i.e., performer, device, etc.)
         */
        TRNS, 
        /**
         * A sub-class of Act representing any transaction between two accounts whose value is measured in monetary terms.

                        In the "intent" mood, communicates a request for a transaction to be initiated, or communicates a transfer of value between two accounts.

                        In the "event" mood, communicates the posting of a transaction to an account.
         */
        XACT, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ActClass fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ACT".equals(codeString))
          return ACT;
        if ("_ActClassRecordOrganizer".equals(codeString))
          return _ACTCLASSRECORDORGANIZER;
        if ("COMPOSITION".equals(codeString))
          return COMPOSITION;
        if ("DOC".equals(codeString))
          return DOC;
        if ("DOCCLIN".equals(codeString))
          return DOCCLIN;
        if ("CDALVLONE".equals(codeString))
          return CDALVLONE;
        if ("CONTAINER".equals(codeString))
          return CONTAINER;
        if ("CATEGORY".equals(codeString))
          return CATEGORY;
        if ("DOCBODY".equals(codeString))
          return DOCBODY;
        if ("DOCSECT".equals(codeString))
          return DOCSECT;
        if ("TOPIC".equals(codeString))
          return TOPIC;
        if ("EXTRACT".equals(codeString))
          return EXTRACT;
        if ("EHR".equals(codeString))
          return EHR;
        if ("FOLDER".equals(codeString))
          return FOLDER;
        if ("GROUPER".equals(codeString))
          return GROUPER;
        if ("CLUSTER".equals(codeString))
          return CLUSTER;
        if ("ACCM".equals(codeString))
          return ACCM;
        if ("ACCT".equals(codeString))
          return ACCT;
        if ("ACSN".equals(codeString))
          return ACSN;
        if ("ADJUD".equals(codeString))
          return ADJUD;
        if ("CACT".equals(codeString))
          return CACT;
        if ("ACTN".equals(codeString))
          return ACTN;
        if ("INFO".equals(codeString))
          return INFO;
        if ("STC".equals(codeString))
          return STC;
        if ("CNTRCT".equals(codeString))
          return CNTRCT;
        if ("FCNTRCT".equals(codeString))
          return FCNTRCT;
        if ("COV".equals(codeString))
          return COV;
        if ("CONC".equals(codeString))
          return CONC;
        if ("HCASE".equals(codeString))
          return HCASE;
        if ("OUTBR".equals(codeString))
          return OUTBR;
        if ("CONS".equals(codeString))
          return CONS;
        if ("CONTREG".equals(codeString))
          return CONTREG;
        if ("CTTEVENT".equals(codeString))
          return CTTEVENT;
        if ("DISPACT".equals(codeString))
          return DISPACT;
        if ("EXPOS".equals(codeString))
          return EXPOS;
        if ("AEXPOS".equals(codeString))
          return AEXPOS;
        if ("TEXPOS".equals(codeString))
          return TEXPOS;
        if ("INC".equals(codeString))
          return INC;
        if ("INFRM".equals(codeString))
          return INFRM;
        if ("INVE".equals(codeString))
          return INVE;
        if ("LIST".equals(codeString))
          return LIST;
        if ("MPROT".equals(codeString))
          return MPROT;
        if ("OBS".equals(codeString))
          return OBS;
        if ("_ActClassROI".equals(codeString))
          return _ACTCLASSROI;
        if ("ROIBND".equals(codeString))
          return ROIBND;
        if ("ROIOVL".equals(codeString))
          return ROIOVL;
        if ("_SubjectPhysicalPosition".equals(codeString))
          return _SUBJECTPHYSICALPOSITION;
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
        if ("ALRT".equals(codeString))
          return ALRT;
        if ("BATTERY".equals(codeString))
          return BATTERY;
        if ("CLNTRL".equals(codeString))
          return CLNTRL;
        if ("CNOD".equals(codeString))
          return CNOD;
        if ("COND".equals(codeString))
          return COND;
        if ("CASE".equals(codeString))
          return CASE;
        if ("OUTB".equals(codeString))
          return OUTB;
        if ("DGIMG".equals(codeString))
          return DGIMG;
        if ("GEN".equals(codeString))
          return GEN;
        if ("DETPOL".equals(codeString))
          return DETPOL;
        if ("EXP".equals(codeString))
          return EXP;
        if ("LOC".equals(codeString))
          return LOC;
        if ("PHN".equals(codeString))
          return PHN;
        if ("POL".equals(codeString))
          return POL;
        if ("SEQ".equals(codeString))
          return SEQ;
        if ("SEQVAR".equals(codeString))
          return SEQVAR;
        if ("INVSTG".equals(codeString))
          return INVSTG;
        if ("OBSSER".equals(codeString))
          return OBSSER;
        if ("OBSCOR".equals(codeString))
          return OBSCOR;
        if ("POS".equals(codeString))
          return POS;
        if ("POSACC".equals(codeString))
          return POSACC;
        if ("POSCOORD".equals(codeString))
          return POSCOORD;
        if ("SPCOBS".equals(codeString))
          return SPCOBS;
        if ("VERIF".equals(codeString))
          return VERIF;
        if ("PCPR".equals(codeString))
          return PCPR;
        if ("ENC".equals(codeString))
          return ENC;
        if ("POLICY".equals(codeString))
          return POLICY;
        if ("JURISPOL".equals(codeString))
          return JURISPOL;
        if ("ORGPOL".equals(codeString))
          return ORGPOL;
        if ("SCOPOL".equals(codeString))
          return SCOPOL;
        if ("STDPOL".equals(codeString))
          return STDPOL;
        if ("PROC".equals(codeString))
          return PROC;
        if ("SBADM".equals(codeString))
          return SBADM;
        if ("SBEXT".equals(codeString))
          return SBEXT;
        if ("SPECCOLLECT".equals(codeString))
          return SPECCOLLECT;
        if ("REG".equals(codeString))
          return REG;
        if ("REV".equals(codeString))
          return REV;
        if ("SPCTRT".equals(codeString))
          return SPCTRT;
        if ("SPLY".equals(codeString))
          return SPLY;
        if ("DIET".equals(codeString))
          return DIET;
        if ("STORE".equals(codeString))
          return STORE;
        if ("SUBST".equals(codeString))
          return SUBST;
        if ("TRFR".equals(codeString))
          return TRFR;
        if ("TRNS".equals(codeString))
          return TRNS;
        if ("XACT".equals(codeString))
          return XACT;
        throw new Exception("Unknown V3ActClass code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ACT: return "ACT";
            case _ACTCLASSRECORDORGANIZER: return "_ActClassRecordOrganizer";
            case COMPOSITION: return "COMPOSITION";
            case DOC: return "DOC";
            case DOCCLIN: return "DOCCLIN";
            case CDALVLONE: return "CDALVLONE";
            case CONTAINER: return "CONTAINER";
            case CATEGORY: return "CATEGORY";
            case DOCBODY: return "DOCBODY";
            case DOCSECT: return "DOCSECT";
            case TOPIC: return "TOPIC";
            case EXTRACT: return "EXTRACT";
            case EHR: return "EHR";
            case FOLDER: return "FOLDER";
            case GROUPER: return "GROUPER";
            case CLUSTER: return "CLUSTER";
            case ACCM: return "ACCM";
            case ACCT: return "ACCT";
            case ACSN: return "ACSN";
            case ADJUD: return "ADJUD";
            case CACT: return "CACT";
            case ACTN: return "ACTN";
            case INFO: return "INFO";
            case STC: return "STC";
            case CNTRCT: return "CNTRCT";
            case FCNTRCT: return "FCNTRCT";
            case COV: return "COV";
            case CONC: return "CONC";
            case HCASE: return "HCASE";
            case OUTBR: return "OUTBR";
            case CONS: return "CONS";
            case CONTREG: return "CONTREG";
            case CTTEVENT: return "CTTEVENT";
            case DISPACT: return "DISPACT";
            case EXPOS: return "EXPOS";
            case AEXPOS: return "AEXPOS";
            case TEXPOS: return "TEXPOS";
            case INC: return "INC";
            case INFRM: return "INFRM";
            case INVE: return "INVE";
            case LIST: return "LIST";
            case MPROT: return "MPROT";
            case OBS: return "OBS";
            case _ACTCLASSROI: return "_ActClassROI";
            case ROIBND: return "ROIBND";
            case ROIOVL: return "ROIOVL";
            case _SUBJECTPHYSICALPOSITION: return "_SubjectPhysicalPosition";
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
            case ALRT: return "ALRT";
            case BATTERY: return "BATTERY";
            case CLNTRL: return "CLNTRL";
            case CNOD: return "CNOD";
            case COND: return "COND";
            case CASE: return "CASE";
            case OUTB: return "OUTB";
            case DGIMG: return "DGIMG";
            case GEN: return "GEN";
            case DETPOL: return "DETPOL";
            case EXP: return "EXP";
            case LOC: return "LOC";
            case PHN: return "PHN";
            case POL: return "POL";
            case SEQ: return "SEQ";
            case SEQVAR: return "SEQVAR";
            case INVSTG: return "INVSTG";
            case OBSSER: return "OBSSER";
            case OBSCOR: return "OBSCOR";
            case POS: return "POS";
            case POSACC: return "POSACC";
            case POSCOORD: return "POSCOORD";
            case SPCOBS: return "SPCOBS";
            case VERIF: return "VERIF";
            case PCPR: return "PCPR";
            case ENC: return "ENC";
            case POLICY: return "POLICY";
            case JURISPOL: return "JURISPOL";
            case ORGPOL: return "ORGPOL";
            case SCOPOL: return "SCOPOL";
            case STDPOL: return "STDPOL";
            case PROC: return "PROC";
            case SBADM: return "SBADM";
            case SBEXT: return "SBEXT";
            case SPECCOLLECT: return "SPECCOLLECT";
            case REG: return "REG";
            case REV: return "REV";
            case SPCTRT: return "SPCTRT";
            case SPLY: return "SPLY";
            case DIET: return "DIET";
            case STORE: return "STORE";
            case SUBST: return "SUBST";
            case TRFR: return "TRFR";
            case TRNS: return "TRNS";
            case XACT: return "XACT";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ActClass";
        }
        public String getDefinition() {
          switch (this) {
            case ACT: return "A record of something that is being done, has been done, can be done, or is intended or requested to be done.\r\n\n                        \n                           Examples:The kinds of acts that are common in health care are (1) a clinical observation, (2) an assessment of health condition (such as problems and diagnoses), (3) healthcare goals, (4) treatment services (such as medication, surgery, physical and psychological therapy), (5) assisting, monitoring or attending, (6) training and education services to patients and their next of kin, (7) and notary services (such as advanced directives or living will), (8)  editing and maintaining documents, and many others.\r\n\n                        \n                           Discussion and Rationale: Acts are the pivot of the RIM; all domain information and processes are represented primarily in Acts. Any profession or business, including healthcare, is primarily constituted of intentional and occasionally non-intentional actions, performed and recorded by responsible actors. An Act-instance is a record of such an action.\r\n\n                        Acts connect to Entities in their Roles through Participations and connect to other Acts through ActRelationships. Participations are the authors, performers and other responsible parties as well as subjects and beneficiaries (which includes tools and material used in the performance of the act, which are also subjects). The moodCode distinguishes between Acts that are meant as factual records, vs. records of intended or ordered services, and the other modalities in which act can appear.\r\n\n                        One of the Participations that all acts have (at least implicitly) is a primary author, who is responsible of the Act and who \"owns\" the act. Responsibility for the act means responsibility for what is being stated in the Act and as what it is stated. Ownership of the act is assumed in the sense of who may operationally modify the same act. Ownership and responsibility of the Act is not the same as ownership or responsibility of what the Act-object refers to in the real world. The same real world activity can be described by two people, each being the author of their Act, describing the same real world activity. Yet one can be a witness while the other can be a principal performer. The performer has responsibilities for the physical actions; the witness only has responsibility for making a true statement to the best of his or her ability. The two Act-instances may even disagree, but because each is properly attributed to its author, such disagreements can exist side by side and left to arbitration by a recipient of these Act-instances.\r\n\n                        In this sense, an Act-instance represents a \"statement\" according to Rector and Nowlan (1991) [Foundations for an electronic medical record. Methods Inf Med. 30.]  Rector and Nowlan have emphasized the importance of understanding the medical record not as a collection of facts, but \"a faithful record of what clinicians have heard, seen, thought, and done.\" Rector and Nowlan go on saying that \"the other requirements for a medical record, e.g., that it be attributable and permanent, follow naturally from this view.\" Indeed the Act class is this attributable statement, and the rules of updating acts (discussed in the state-transition model, see Act.statusCode) versus generating new Act-instances are designed according to this principle of permanent attributable statements.\r\n\n                        Rector and Nolan focus on the electronic medical record as a collection of statements, while attributed statements, these are still mostly factual statements. However, the Act class goes beyond this limitation to attributed factual statements, representing what is known as \"speech-acts\" in linguistics and philosophy.  The notion of speech-act includes that there is pragmatic meaning in language utterances, aside from just factual statements; and that these utterances interact with the real world to change the state of affairs, even directly cause physical activities to happen. For example, an order is a speech act that (provided it is issued adequately) will cause the ordered action to be physically performed. The speech act theory has culminated in the seminal work by Austin (1962) [How to do things with words. Oxford University Press].\r\n\n                        An activity in the real world may progress from defined, through planned and ordered to executed, which is represented as the mood of the Act. Even though one might think of a single activity as progressing from planned to executed, this progression is reflected by multiple Act-instances, each having one and only one mood that will not change along the Act-instance life cycle.  This is because the attribution and content of speech acts along this progression of an activity may be different, and it is often critical that a permanent and faithful record be maintained of this progression. The specification of orders or promises or plans must not be overwritten by the specification of what was actually done, so as to allow comparing actions with their earlier specifications. Act-instances that describe this progression of the same real world activity are linked through the ActRelationships (of the relationship category \"sequel\").\r\n\n                        Act as statements or speech-acts are the only representation of real world facts or processes in the HL7 RIM. The truth about the real world is constructed through a combination (and arbitration) of such attributed statements only, and there is no class in the RIM whose objects represent \"objective state of affairs\" or \"real processes\" independent from attributed statements. As such, there is no distinction between an activity and its documentation. Every Act includes both to varying degrees. For example, a factual statement made about recent (but past) activities, authored (and signed) by the performer of such activities, is commonly known as a procedure report or original documentation (e.g., surgical procedure report, clinic note etc.). Conversely, a status update on an activity that is presently in progress, authored by the performer (or a close observer) is considered to capture that activity (and is later superceded by a full procedure report). However, both status update and procedure report are acts of the same kind, only distinguished by mood and state (see statusCode) and completeness of the information.";
            case _ACTCLASSRECORDORGANIZER: return "Used to group a set of acts sharing a common context. Organizer structures can nest within other context structures - such as where a document is contained within a folder, or a folder is contained within an EHR extract.";
            case COMPOSITION: return "A context representing a grouped commitment of information to the EHR. It is considered the unit of modification of the record, the unit of transmission in record extracts, and the unit of attestation by authorizing clinicians.\r\n\n                        A composition represents part of a patient record originating from a single interaction between an authenticator and the record.\r\n\n                        Unless otherwise stated all statements within a composition have the same authenticator, apply to the same patient and were recorded in a single session of use of a single application.\r\n\n                        A composition contains organizers and entries.";
            case DOC: return "The notion of a document comes particularly from the paper world, where it corresponds to the contents recorded on discrete pieces of paper. In the electronic world, a document is a kind of composition that bears resemblance to their paper world counter-parts. Documents typically are meant to be human-readable.\r\n\n                        HL7's notion of document differs from that described in the W3C XML Recommendation, in which a document refers specifically to the contents that fall between the root element's start-tag and end-tag. Not all XML documents are HL7 documents.";
            case DOCCLIN: return "A clinical document is a documentation of clinical observations and services, with the following characteristics:\r\n\n                        \n                           \n                              Persistence - A clinical document continues to exist in an unaltered state, for a time period defined by local and regulatory requirements; \r\n\n                           \n                           \n                              Stewardship - A clinical document is maintained by a person or organization entrusted with its care; \r\n\n                           \n                           \n                              Potential for authentication - A clinical document is an assemblage of information that is intended to be legally authenticated; \r\n\n                           \n                           \n                              Wholeness - Authentication of a clinical document applies to the whole and does not apply to portions of the document without the full context of the document;\r\n\n                           \n                           \n                              Human readability - A clinical document is human readable.";
            case CDALVLONE: return "A clinical document that conforms to Level One of the HL7 Clinical Document Architecture (CDA)";
            case CONTAINER: return "Description: Container of clinical statements. Navigational. No semantic content. Knowledge of the section code is not required to interpret contained observations. Represents a heading in a heading structure, or \"container tree\".\r\n\n                        The record entries relating to a single clinical session are usually grouped under headings that represent phases of the encounter, or assist with layout and navigation. Clinical headings usually reflect the clinical workflow during a care session, and might also reflect the main author's reasoning processes. Much research has demonstrated that headings are used differently by different professional groups and specialties, and that headings are not used consistently enough to support safe automatic processing of the E H R.";
            case CATEGORY: return "A group of entries within a composition or topic that have a common characteristic - for example, Examination, Diagnosis, Management OR Subjective, Objective, Analysis, Plan.\r\n\n                        The distinction from Topic relates to value sets. For Category there is a bounded list of things like \"Examination\", \"Diagnosis\" or SOAP categories. For Topic the list is wide open to any clinical condition or reason for a part of an encounter.\r\n\n                        A CATEGORY MAY CONTAIN ENTRIES.";
            case DOCBODY: return "A context that distinguishes the body of a document from the document header. This is seen, for instance, in HTML documents, which have discrete <head> and <body> elements.";
            case DOCSECT: return "A context that subdivides the body of a document. Document sections are typically used for human navigation, to give a reader a clue as to the expected content. Document sections are used to organize and provide consistency to the contents of a document body. Document sections can contain document sections and can contain entries.";
            case TOPIC: return "A group of entries within a composition that are related to a common clinical theme - such as a specific disorder or problem, prevention, screening and provision of contraceptive services.\r\n\n                        A topic may contain categories and entries.";
            case EXTRACT: return "This context represents the part of a patient record conveyed in a single communication. It is drawn from a providing system for the purposes of communication to a requesting process (which might be another repository, a client application or a middleware service such as an electronic guideline engine), and supporting the faithful inclusion of the communicated data in the receiving system.\r\n\n                        An extract may be the entirety of the patient record as held by the sender or it may be a part of that record (e.g. changes since a specified date).\r\n\n                        An extract contains folders or compositions.\r\n\n                        An extract cannot contain another extract.";
            case EHR: return "A context that comprises all compositions. The EHR is an extract that includes the entire chart.\r\n\n                        \n                           NOTE: In an exchange scenario, an EHR is a specialization of an extract.";
            case FOLDER: return "A context representing the high-level organization of an extract e.g. to group parts of the record by episode, care team, clinical specialty, clinical condition, or source application. Internationally, this kind of organizing structure is used variably: in some centers and systems the folder is treated as an informal compartmentalization of the overall health record; in others it might represent a significant legal portion of the EHR relating to the originating enterprise or team.\r\n\n                        A folder contains compositions.\r\n\n                        Folders may be nested within folders.";
            case GROUPER: return "Definition: An ACT that organizes a set of component acts into a semantic grouping that share a particular context such as timeframe, patient, etc.\r\n\n                        \n                           UsageNotes: The focus in a GROUPER act is the grouping of the contained acts.  For example \"a request to group\" (RQO), \"a type of grouping that is allowed to occur\" (DEF), etc.\r\n\n                        Unlike WorkingList, which represents a dynamic, shared, continuously updated collection to provide a \"view\" of a set of objects, GROUPER collections tend to be static and simply indicate a shared set of semantics.  Note that sharing of semantics can be achieved using ACT as well.  However, with GROUPER, the sole semantic is of grouping.";
            case CLUSTER: return "Description:An ACT that organizes a set of component acts into a semantic grouping that have a shared subject. The subject may be either a subject participation (SBJ), subject act relationship (SUBJ), or child participation/act relationship types.\r\n\n                        \n                           Discussion: The focus in a CLUSTER act is the grouping of the contained acts.  For example \"a request to cluster\" (RQO), \"a type of cluster that is allowed to occur\" (DEF), etc.\r\n\n                        \n                           Examples: \n                        \r\n\n                        \n                           \n                              Radiologic investigations that might include administration of a dye, followed by radiographic observations;\r\n\n                           \n                           \n                              \"Isolate cluster\" which includes all testing and specimen processing performed on a specific isolate;\r\n\n                           \n                           \n                              a set of actions to perform at a particular stage in a clinical trial.";
            case ACCM: return "An accommodation is a service provided for a Person or other LivingSubject in which a place is provided for the subject to reside for a period of time.  Commonly used to track the provision of ward, private and semi-private accommodations for a patient.";
            case ACCT: return "A financial account established to track the net result of financial acts.";
            case ACSN: return "A unit of work, a grouper of work items as defined by the system performing that work. Typically some laboratory order fulfillers communicate references to accessions in their communications regarding laboratory orders. Often one or more specimens are related to an accession such that in some environments the accession number is taken as an identifier for a specimen (group).";
            case ADJUD: return "A transformation process where a requested invoice is transformed into an agreed invoice.  Represents the adjudication processing of an invoice (claim).  Adjudication results can be adjudicated as submitted, with adjustments or refused.\r\n\n                        Adjudication results comprise 2 components: the adjudication processing results and a restated (or adjudicated) invoice or claim";
            case CACT: return "An act representing a system action such as the change of state of another act or the initiation of a query.  All control acts represent trigger events in the HL7 context.  ControlActs may occur in different moods.";
            case ACTN: return "Sender asks addressee to do something depending on the focal Act of the payload.  An example is \"fulfill this order\".  Addressee has responsibilities to either reject the message or to act on it in an appropriate way (specified by the specific receiver responsibilities for the interaction).";
            case INFO: return "Sender sends payload to addressee as information.  Addressee does not have responsibilities beyond serving addressee's own interest (i.e., read and memorize if you see fit).  This is equivalent to an FYI on a memo.";
            case STC: return "Description: Sender transmits a status change pertaining to the focal act of the payload. This status of the focal act is the final state of the state transition. This can be either a request or an event, according to the mood of the control act.";
            case CNTRCT: return "An agreement of obligation between two or more parties that is subject to contractual law and enforcement.";
            case FCNTRCT: return "A contract whose value is measured in monetary terms.";
            case COV: return "When used in the EVN mood, this concept means with respect to a covered party:\r\n\n                        \n                           \n                              A health care insurance policy or plan that is contractually binding between two or more parties; or \r\n\n                           \n                           \n                              A health care program, usually administered by government entities, that provides coverage to persons determined eligible under the terms of the program.\r\n\n                           \n                        \n                        \n                           \n                              When used in the definition (DEF) mood, COV means potential coverage for a patient who may or may not be a covered party.\r\n\n                           \n                           \n                              The concept's meaning is fully specified by the choice of ActCoverageTypeCode (abstract) ActProgramCode or ActInsurancePolicyCode.";
            case CONC: return "Definition: A worry that tends to persist over time and has as its subject a state or process. The subject of the worry has the potential to require intervention or management.\r\n\n                        \n                           Examples: an observation result, procedure, substance administration, equipment repair status, device recall status, a health risk, a financial risk, public health risk, pregnancy, health maintenance, allergy, and acute or chronic illness.";
            case HCASE: return "A public health case is a Concern about an observation or event that has a specific significance for public health. The creation of a PublicHealthCase initiates the tracking of the object of concern.  The decision to track is related to but somewhat independent of the underlying event or observation.\r\n\n                        \n                           UsageNotes: Typically a Public Health Case involves an instance or instances of a reportable infectious disease or other condition. The public health case can include a health-related event concerning a single individual or it may refer to multiple health-related events that are occurrences of the same disease or condition of interest to public health.\r\n\n                        A public health case definition (Act.moodCode = \"definition\") includes the description of the clinical, laboratory, and epidemiologic indicators associated with a disease or condition of interest to public health. There are case definitions for conditions that are reportable, as well as for those that are not. A public health case definition is a construct used by public health for the purpose of counting cases, and should not be used as clinical indications for treatment. Examples include AIDS, toxic-shock syndrome, and salmonellosis and their associated indicators that are used to define a case.";
            case OUTBR: return "An Outbreak is a concern resulting from a series of public health cases.\r\n\n                        \n                           UsageNotes: The date on which an outbreak starts is the earliest date of onset among the cases assigned to the outbreak and its ending date is the last date of onset among the cases assigned to the outbreak. The effectiveTime attribute is used to convey the relevant dates for the case. An outbreak definition (Act.moodCode = \"definition\" includes the criteria for the number, types and occurrence pattern of cases necessary to declare an outbreak and to judge the severity of an outbreak.";
            case CONS: return "The Consent class represents informed consents and all similar medico-legal transactions between the patient (or his legal guardian) and the provider. Examples are informed consent for surgical procedures, informed consent for clinical trials, advanced beneficiary notice, against medical advice decline from service, release of information agreement, etc.\r\n\n                        The details of consents vary. Often an institution has a number of different consent forms for various purposes, including reminding the physician about the topics to mention. Such forms also include patient education material. In electronic medical record communication, consents thus are information-generating acts on their own and need to be managed similar to medical activities. Thus, Consent is modeled as a special class of Act.\r\n\n                        The \"signatures\" to the consent document are represented electronically through Participation instances to the consent object. Typically an informed consent has Participation.typeCode of \"performer\", the healthcare provider informing the patient, and \"consenter\", the patient or legal guardian. Some consent may associate a witness or a notary public (e.g., living wills, advanced directives). In consents where a healthcare provider is not required (e.g. living will), the performer may be the patient himself or a notary public.\r\n\n                        Some consent has a minimum required delay between the consent and the service, so as to allow the patient to rethink his decisions. This minimum delay can be expressed in the act definition by the ActRelationship.pauseQuantity attribute that delays the service until the pause time has elapsed after the consent has been completed.";
            case CONTREG: return "An Act where a container is registered either via an automated sensor, such as a barcode reader,  or by manual receipt";
            case CTTEVENT: return "An identified point during a clinical trial at which one or more actions are scheduled to be performed (definition mood), or are actually performed (event mood).  The actions may or may not involve an encounter between the subject and a healthcare professional.";
            case DISPACT: return "An action taken with respect to a subject Entity by a regulatory or authoritative body with supervisory capacity over that entity. The action is taken in response to behavior by the subject Entity that body finds to be 							undesirable.\r\n\n                        Suspension, license restrictions, monetary fine, letter of reprimand, mandated training, mandated supervision, etc.Examples:";
            case EXPOS: return "An interaction between entities that provides opportunity for transmission of a physical, chemical, or biological agent from an exposure source entity to an exposure target entity.\r\n\n                        \n                           Examples:  The following examples are provided to indicate what interactions are considered exposures rather than other types of Acts:\r\n\n                        \n                           \n                              A patient accidentally receives three times the recommended dose of their medication due to a dosing error. \r\n\n                              \n                                 \n                                    This is a substance administration.  Public health and/or safety authorities may also be interested in documenting this with an associated exposure.\r\n\n                                 \n                              \n                           \n                           \n                              A patient accidentally is dispensed an incorrect medicine (e.g., clomiphene instead of clomipramine).  They have taken several doses before the mistake is detected.  They are therefore \"exposed\" to a medicine that there was no therapeutic indication for them to receive. \r\n\n                              \n                                 \n                                    There are several substance administrations in this example.  Public health and/or safety authorities may also be interested in documenting this with associated exposures.\r\n\n                                 \n                              \n                           \n                           \n                              In a busy medical ward, a patient is receiving chemotherapy for a lymphoma.  Unfortunately, the IV infusion bag containing the medicine splits, spraying cytotoxic medication over the patient being treated and the patient in the adjacent bed. \r\n\n                              \n                                 \n                                    There are three substance administrations in this example.  The first is the intended one (IV infusion) with its associated (implicit) exposure.  There is an incident with an associated substance administration to the same patient involving the medication sprayed over the patient as well as an associated exposure.  Additionally, the incident includes a substance administration involving the spraying of medication on the adjacent patient, also with an associated exposure.\r\n\n                                 \n                              \n                           \n                           \n                              A patient who is a refugee from a war-torn African nation arrives in a busy inner city A&E department suffering from a cough with bloody sputum.  Not understanding the registration and triage process, they sit in the waiting room for several hours before it is noticed that they have not booked in.  As soon as they are being processed, it is suspected that they are suffering from TB.  Vulnerable (immunosuppressed) patients who were sharing the waiting room with this patient may have been exposed to the tubercule bacillus, and must be traced for investigation. \r\n\n                              \n                                 \n                                    This is an exposure (or possibly multiple exposures) in the waiting room involving the refugee and everyone else in the waiting room during the period.  There might also be a number of known or presumed substance administrations (coughing) via several possible routes.  The substance administrations are only hypotheses until confirmed by further testing.\r\n\n                                 \n                              \n                           \n                           \n                              A patient who has received an elective total hip replacement procedure suffers a prolonged stay in hospital, due to contracting an MRSA infection in the surgical wound site after the surgery. \r\n\n                              \n                                 \n                                    This is an exposure to MRSA.  Although there was some sort of substance administration, it's possible the exact mechanism for introduction of the MRSA into the wound will not be identified.\r\n\n                                 \n                              \n                           \n                           \n                              Routine maintenance of the X-ray machines at a local hospital reveals a serious breach of the shielding on one of the machines.  Patients who have undergone investigations using that machine in the last month are likely to have been exposed to significantly higher doses of X-rays than was intended, and must be tracked for possible adverse effects. \r\n\n                              \n                                 \n                                    There has been an exposure of each patient who used the machine in the past 30 days. Some patients may have had substance administrations.\r\n\n                                 \n                              \n                           \n                           \n                              A new member of staff is employed in the laundry processing room of a small cottage hospital, and a misreading of the instructions for adding detergents results in fifty times the usual concentration of cleaning materials being added to a batch of hospital bedding.  As a result, several patients have been exposed to very high levels of detergents still present in the \"clean\" bedding, and have experienced dermatological reactions to this. \r\n\n                              \n                                 \n                                    There has been an incident with multiple exposures to several patients.  Although there are substance administrations involving the application of the detergent to the skin of the patients, it is expected that the substance administrations would not be directly documented.\r\n\n                                 \n                              \n                           \n                           \n                              Seven patients who are residents in a health care facility for the elderly mentally ill have developed respiratory problems. After several months of various tests having been performed and various medications prescribed to these patients, the problem is traced to their being \"sensitive\" to a new fungicide used in the wall plaster of the ward where these patients reside.\r\n\n                              \n                                 \n                                    The patients have been continuously exposed to the fungicide.  Although there have been continuous substance administrations (via breathing) this would not normally be documented as a substance administration.\r\n\n                                 \n                              \n                           \n                           \n                              A patient with osteoarthritis of the knees is treated symptomatically using analgesia, paracetamol (acetaminophen) 1g up to four times a day for pain relief.  His GP does not realize that the patient has, 20 years previously (while at college) had severe alcohol addiction problems, and now, although this is completely under control, his liver has suffered significantly, leaving him more sensitive to hepatic toxicity from paracetamol use.  Later that year, the patient returns with a noticeable level of jaundice.  Paracetamol is immediately withdrawn and alternative solutions for the knee pain are sought.  The jaundice gradually subsides with conservative management, but referral to the gastroenterologist is required for advice and monitoring. \r\n\n                              \n                                 \n                                    There is a substance administration with an associated exposure.  The exposure component is based on the relative toxic level of the substance to a patient with a compromised liver function.\r\n\n                                 \n                              \n                           \n                           \n                              A patient goes to their GP complaining of abdominal pain, having been discharged from the local hospital ten days' previously after an emergency appendectomy.  The GP can find nothing particularly amiss, and presumes it is post operative surgical pain that will resolve.  The patient returns a fortnight later, when the GP prescribes further analgesia, but does decide to request an outpatient surgical follow-up appointment.  At this post-surgical outpatient review, the registrar decides to order an ultrasound, which, when performed three weeks later, shows a small faint inexplicable mass.  A laparoscopy is then performed, as a day case procedure, and a piece of a surgical swab is removed from the patient's abdominal cavity.  Thankfully, a full recovery then takes place. \r\n\n                              \n                                 \n                                    This is a procedural sequelae.  There may be an Incident recorded for this also.\r\n\n                                 \n                              \n                           \n                           \n                              A patient is slightly late for a regular pacemaker battery check in the Cardiology department of the local hospital.  They are hurrying down the second floor corridor.  A sudden summer squall has recently passed over the area, and rain has come in through an open corridor window leaving a small puddle on the corridor floor.  In their haste, the patient slips in the puddle and falls so badly that they have to be taken to the A&E department, where it is discovered on investigation they have slightly torn the cruciate ligament in their left knee. \r\n\n                              \n                                 \n                                    This is not an exposure.  There has been an incident.  \r\n\n                                 \n                              \n                           \n                        \n                        \n                           Usage Notes: This class deals only with opportunity and not the outcome of the exposure; i.e. not all exposed parties will necessarily experience actual harm or benefit.\r\n\n                        Exposure differs from Substance Administration by the absence of the participation of a performer in the act. \r\n\n                        The following participations SHOULD be used with the following participations to distinguish the specific entities:\r\n\n                        \n                           \n                              The exposed entity participates via the \"exposure target\" (EXPTRGT) participation.\r\n\n                           \n                           \n                              An entity that has carried the agent transmitted in the exposure participates via the \"exposure source\" (EXSRC) participation.  For example: \r\n\n                              \n                                 \n                                    a person or animal who carried an infectious disease and interacts (EXSRC) with another person or animal (EXPTRGT) transmitting the disease agent;\r\n\n                                 \n                                 \n                                    a place or other environment (EXSRC) and a person or animal (EXPTRGT) who is exposed in the presence of this environment.\r\n\n                                 \n                              \n                           \n                           \n                              When it is unknown whether a participating entity is the source of the agent (EXSRC) or the target of the transmission (EXPTRGT), the \"exposure participant\" (EXPART) is used.\r\n\n                           \n                           \n                              The physical (including energy), chemical or biological substance which is participating in the exposure uses the \"exposure agent\" (EXPAGNT) participation.  There are at least three scenarios:\r\n\n                              \n                                 \n                                    the player of the Role that participates as EXPAGNT is the chemical or biological substance mixed or carried by the scoper-entity of the Role (e.g., ingredient role); or \r\n\n                                 \n                                 \n                                    the player of the Role that participates as EXPAGNT is a mixture known to contain the chemical, radiological or biological substance of interest; or \r\n\n                                 \n                                 \n                                    the player of the Role that participates as a EXPAGNT is known to carry the agent (i.e., the player is a fomite, vector, etc.).\r\n\n                                 \n                              \n                           \n                        \n                        The Exposure.statusCode attribute should be interpreted as the state of the Exposure business object (e.g., active, aborted, completed) and not the clinical status of the exposure (e.g., probable, confirmed).  The clinical status of the exposure should be associated with the exposure via a subject observation.\r\n\n                        \n                           Design Comment: The usage notes require a clear criterion for determining whether an act is an exposure or substance administration-deleterious potential, uncertainty of actual transmission, or otherwise. SBADM states that the criterion is the presence of a performer-but there are examples above that call this criterion into question (e.g., the first one, concerning a dosing error).";
            case AEXPOS: return "Description: \n                        \r\n\n                        An acquisition exposure act describes the proximity (location and time) through which the participating entity was potentially exposed to a physical (including energy), chemical or biological agent from another entity.  The acquisition exposure act is used in conjunction with transmission exposure acts as part of an analysis technique for contact tracing.  Although an exposure can be decomposed into transmission and acquisition exposures, there is no requirement that all exposures be treated in this fashion.\r\n\n                        \n                           Constraints:  The Acquisition Exposure inherits the participation constraints that apply to Exposure with the following exception.  The EXPSRC (exposure source) participation must never be associated with the Transmission Exposure either directly or via context conduction.";
            case TEXPOS: return "Description: \n                        \r\n\n                        A transmission exposure act describes the proximity (time and location) over which the participating source entity was capable of transmitting a physical (including energy), chemical or biological substance agent to another entity.  The transmission exposure act is used in conjunction with acquisition exposure acts as part of an analysis technique for contact tracing.  Although an exposure can be decomposed into transmission and acquisition exposures, there is no requirement that all exposures be treated in this fashion.\r\n\n                        \n                           Constraints:  The Transmission Exposure inherits the participation constraints that apply to Exposure with the following exception.  The EXPTRGT (exposure target) participation must never be associated with the Transmission Exposure either directly or via context conduction.";
            case INC: return "An event that occurred outside of the control of one or more of the parties involved.  Includes the concept of an accident.";
            case INFRM: return "The act  of transmitting information and understanding about a topic to a subject where the participation association must be SBJ.\r\n\n                        \n                           Discussion: This act may be used to request that a patient or provider be informed about an Act, or to indicate that a person was informed about a particular act.";
            case INVE: return "Represents concepts related to invoice processing in health care";
            case LIST: return "Working list collects a dynamic list of individual instances of Act via ActRelationship which reflects the need of an individual worker, team of workers, or an organization to manage lists of acts for many different clinical and administrative reasons. Examples of working lists include problem lists, goal lists, allergy lists, and to-do lists.";
            case MPROT: return "An officially or unofficially instituted program to track acts of a particular type or categorization.";
            case OBS: return "Description:An act that is intended to result in new information about a subject. The main difference between Observations and other Acts is that Observations have a value attribute. The code attribute of Observation and the value attribute of Observation must be considered in combination to determine the semantics of the observation.\r\n\n                        \n                           Discussion:\n                        \r\n\n                        Structurally, many observations are name-value-pairs, where the Observation.code (inherited from Act) is the name and the Observation.value is the value of the property. Such a construct is also known as a  variable (a named feature that can assume a value) hence, the Observation class is always used to hold generic name-value-pairs or variables, even though the variable valuation may not be the result of an elaborate observation method. It may be a simple answer to a question or it may be an assertion or setting of a parameter.\r\n\n                        As with all Act statements, Observation statements describe what was done, and in the case of Observations, this includes a description of what was actually observed (results or answers); and those results or answers are part of the observation and not split off into other objects. \r\n\n                        The method of action is asserted by the Observation classCode or its subclasses at the least granular level, by the Observation.code attribute value at the medium level of granularity, and by the attribute value of observation.methodCode when a finer level of granularity is required. The method in whole or in part may also appear in the attribute value of Observation.value when using coded data types to express the value of the attribute. Relevant aspects of methodology may also be restated in value when the results themselves imply or state a methodology.\r\n\n                        An observation may consist of component observations each having their own Observation.code and Observation.value. In this case, the composite observation may not have an Observation.value for itself. For instance, a white blood cell count consists of the sub-observations for the counts of the various granulocytes, lymphocytes and other normal or abnormal blood cells (e.g., blasts). The overall white blood cell count Observation itself may therefore not have a value by itself (even though it could have one, e.g., the sum total of white blood cells). Thus, as long as an Act is essentially an Act of recognizing and noting information about a subject, it is an Observation, regardless of whether it has a simple value by itself or whether it has sub-observations.\r\n\n                        Even though observations are professional acts (see Act) and as such are intentional actions, this does not require that every possible outcome of an observation be pondered in advance of it being actually made. For instance, differential white blood cell counts (WBC) rarely show blasts, but if they do, this is part of the WBC observation even though blasts might not be predefined in the structure of a normal WBC. \r\n\n                        Clinical documents commonly have Subjective and Objective findings, both of which are kinds of Observations. In addition, clinical documents commonly contain Assessments, which are also kinds of Observations. Thus, the establishment of a diagnosis is an Observation. \r\n\n                        \n                           Examples:\n                        \r\n\n                        \n                           \n                              Recording the results of a Family History Assessment\r\n\n                           \n                           \n                              Laboratory test and associated result\r\n\n                           \n                           \n                              Physical exam test and associated result\r\n\n                           \n                           \n                              Device temperature\r\n\n                           \n                           \n                              Soil lead level";
            case _ACTCLASSROI: return "Regions of Interest (ROI) within a subject Act. Primarily used for making secondary observations on a subset of a subject observation. The relationship between a ROI and its referenced Act is specified through an ActRelationship of type \"subject\" (SUBJ), which must always be present.";
            case ROIBND: return "A Region of Interest (ROI) specified for a multidimensional observation, such as an Observation Series (OBSSER). The ROI is specified using a set of observation criteria, each delineating the boundary of the region in one of the dimensions in the multidimensional observation. The relationship between a ROI and its referenced Act is specified through an ActRelationship of type subject (SUBJ), which must always be present. Each of the boundary criteria observations is connected with the ROI using ActRelationships of type \"has component\" (COMP). In each boundary criterion, the Act.code names the dimension and the Observation.value specifies the range of values inside the region. Typically the bounded dimension is continuous, and so the Observation.value will be an interval (IVL) data type. The Observation.value need not be specified if the respective dimension is only named but not constrained. For example, an ROI for the QT interval of a certain beat in ECG Lead II would contain 2 boundary criteria, one naming the interval in time (constrained), and the other naming the interval in ECG Lead II (only named, but not constrained).";
            case ROIOVL: return "A Region of Interest (ROI) specified for an image using an overlay shape. Typically used to make reference to specific regions in images, e.g., to specify the location of a radiologic finding in an image or to specify the site of a physical finding by \"circling\" a region in a schematic picture of a human body. The units of the coordinate values are in pixels.  The origin is in the upper left hand corner, with positive X values going to the right and positive Y values going down. The relationship between a ROI and its referenced Act is specified through an ActRelationship of type \"subject\" (SUBJ), which must always be present.";
            case _SUBJECTPHYSICALPOSITION: return "The spatial relationship of a subject whether human, other animal, or plant, to a frame of reference such as gravity or a collection device.";
            case _SUBJECTBODYPOSITION: return "Contains codes for defining the observed, physical position of a subject, such as during an observation, assessment, collection of a specimen, etc.  ECG waveforms and vital signs, such as blood pressure, are two examples where a general, observed position typically needs to be noted.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case LLD: return "Lying on the left side.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case PRN: return "Lying with the front or ventral surface downward; lying face down.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case RLD: return "Lying on the right side.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case SFWL: return "A semi-sitting position in bed with the head of the bed elevated approximately 45 degrees.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case SIT: return "Resting the body on the buttocks, typically with upper torso erect or semi erect.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case STN: return "To be stationary, upright, vertical, on one's legs.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case SUP: return "Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case RTRD: return "Lying on the back, on an inclined plane, typically about 30-45 degrees with head raised and feet lowered.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case TRD: return "Lying on the back, on an inclined plane, typically about 30-45 degrees, with  head lowered and feet raised.\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because it does not describe a type of Act (as it should in the ActClass code system), but rather encodes the result or value of an observation.  The same code has been added to the ObservationValue code system.";
            case ALRT: return "An observation identifying a potential adverse outcome as a result of an Act or combination of Acts.\r\n\n                        \n                           Examples: Detection of a drug-drug interaction; Identification of a late-submission for an invoice; Requesting discharge for a patient who does not meet hospital-defined discharge criteria.\r\n\n                        \n                           Discussion: This class is commonly used for identifying 'business rule' or 'process' problems that may result in a refusal to carry out a particular request. In some circumstances it may be possible to 'bypass' a problem by modifying the request to acknowledge the issue and/or by providing some form of mitigation.\r\n\n                        \n                           Constraints: the Act or Acts that may cause the the adverse outcome are the target of a subject ActRelationship. The subbtypes of this concept indicate the type of problem being detected (e.g. drug-drug interaction) while the Observation.value is used to repesent a specific problem code (e.g. specific drug-drug interaction id).";
            case BATTERY: return "Definition: An observation that is composed of a set of observations. These observations typically have a logical or practical grouping for generally accepted clinical or functional purposes, such as observations that are run together because of automation. A battery can define required and optional component observations and, in some cases, will define complex rules that determine whether or not a particular observation is made. BATTERY is a constraint on the Observation class in that it is understood to always be composed of component observations.\r\n\n                        \n                           UsageNotes: The focus in a BATTERY is that it is composed of individual observations. In request (RQO) mood, a battery is a request to perform the component observations. In event (EVN) mood a battery is a reporting of associated set of observation events. In definition mood a battery is the definition of the associated set of observations.\r\n\n                        \n                           Examples: Vital signs, Full blood count, Chemistry panel.";
            case CLNTRL: return "The set of actions that define an experiment to assess the effectiveness and/or safety of a biopharmaceutical product (food, drug, device, etc.).  In definition mood, this set of actions is often embodied in a clinical trial protocol; in event mood, this designates the aggregate act of applying the actions to one or more subjects.";
            case CNOD: return "An instance of Observation of a Condition at a point in time that includes any Observations or Procedures associated with that Condition as well as links to previous instances of Condition Node for the same Condition\r\n\n                        \n                           \n                              Deprecation Comment: \n                           This concept has been deprecated because an alternative structure for tracking the evolution of a problem has been presented and adopted by the Care Provision Work Group.";
            case COND: return "An observable finding or state that persists over time and tends to require intervention or management, and, therefore, distinguished from an Observation made at a point in time; may exist before an Observation of the Condition is made or after interventions to manage the Condition are undertaken. Examples: equipment repair status, device recall status, a health risk, a financial risk, public health risk, pregnancy, health maintenance, chronic illness";
            case CASE: return "A public health case is an Observation representing a condition or event that has a specific significance for public health. Typically it involves an instance or instances of a reportable infectious disease or other condition. The public health case can include a health-related event concerning a single individual or it may refer to multiple health-related events that are occurrences of the same disease or condition of interest to public health. An outbreak involving multiple individuals may be considered as a type of public health case. A public health case definition (Act.moodCode = \"definition\") includes the description of the clinical, laboratory, and epidemiologic indicators associated with a disease or condition of interest to public health. There are case definitions for conditions that are reportable, as well as for those that are not. There are also case definitions for outbreaks. A public health case definition is a construct used by public health for the purpose of counting cases, and should not be used as clinical indications for treatment. Examples include AIDS, toxic-shock syndrome, and salmonellosis and their associated indicators that are used to define a case.";
            case OUTB: return "An outbreak represents a series of public health cases. The date on which an outbreak starts is the earliest date of onset among the cases assigned to the outbreak, and its ending date is the last date of onset among the cases assigned to the outbreak.";
            case DGIMG: return "Class for holding attributes unique to diagnostic images.";
            case GEN: return "Description:An observation of genomic phenomena.";
            case DETPOL: return "Description:A determinant peptide in a polypeptide as described by polypeptide.";
            case EXP: return "Description:An expression level of genes/proteins or other expressed genomic entities.";
            case LOC: return "Description:The position of a gene (or other significant sequence) on the genome.";
            case PHN: return "Description:A genomic phenomenon that is expressed externally in the organism.";
            case POL: return "Description:A polypeptide resulting from the translation of a gene.";
            case SEQ: return "Description:A sequence of biomolecule like the DNA, RNA, protein and the like.";
            case SEQVAR: return "Description:A variation in a sequence as described by BioSequence.";
            case INVSTG: return "An formalized inquiry into the circumstances surrounding a particular unplanned event or potential event for the purposes of identifying possible causes and contributing factors for the event. This investigation could be conducted at a local institutional level or at the level of a local or national government.";
            case OBSSER: return "Container for Correlated Observation Sequences sharing a common frame of reference.  All Observations of the same cd must be comparable and relative to the common frame of reference.  For example, a 3-channel ECG device records a 12-lead ECG in 4 steps (3 leads at a time).  Each of the separate 3-channel recordings would be in their own \"OBSCOR\".  And, all 4 OBSCOR would be contained in one OBSSER because all the times are relative to the same origin (beginning of the recording) and all the ECG signals were from a fixed set of electrodes.";
            case OBSCOR: return "Container for Observation Sequences (Observations whose values are contained in LIST<>'s) having values correlated with each other.  Each contained Observation Sequence LIST<> must be the same length.  Values in the LIST<>'s are correlated based on index.  E.g. the values in position 2 in all the LIST<>'s are correlated.  This is analogous to a table where each column is an Observation Sequence with a LIST<> of values, and each row in the table is a correlation between the columns.  For example, a 12-lead ECG would contain 13 sequences: one sequence for time, and a sequence for each of the 12 leads.";
            case POS: return "An observation denoting the physical location of a person or thing based on a reference coordinate system.";
            case POSACC: return "Description:An observation representing the degree to which the assignment of the spatial coordinates, based on a matching algorithm by a geocoding engine against a reference spatial database, matches true or accepted values.";
            case POSCOORD: return "Description:An observation representing one of a set of numerical values used to determine the position of a place.  The name of the coordinate value is determined by the reference coordinate system.";
            case SPCOBS: return "An observation on a specimen in a laboratory environment that may affect processing, analysis or result interpretation";
            case VERIF: return "An act which describes the process whereby a 'verifying party' validates either the existence of the Role attested to by some Credential or the actual Vetting act and its details.";
            case PCPR: return "An Act that of taking on whole or partial responsibility for, or attention to, safety and well-being of a subject of care. \r\n\n                        \n                           Discussion: A care provision event may exist without any other care actions taking place. For example, when a patient is assigned to the care of a particular health professional.\r\n\n                        In request (RQO) mood care provision communicates a referral, which is a request:\r\n\n                        \n                           \n                              from one party (linked as a participant of type author (AUT)),\r\n\n                           \n                           \n                              to another party (linked as a participant of type performer (PRF),\r\n\n                           \n                           \n                              to take responsibility for a scope specified by the code attribute, \r\n\n                           \n                           \n                              for an entity (linked as a participant of type subject (SBJ)).\r\n\n                           \n                        \n                        The scope of the care for which responsibility is taken is identified by code attribute.\r\n\n                        In event (EVN) mood care provision indicates the effective time interval of a specified scope of responsibility by a performer (PRF) or set of performers (PRF) for a subject (SBJ).\r\n\n                        \n                           Examples:\n                        \r\n\n                        \n                           \n                              Referral from GP to a specialist.\r\n\n                           \n                           \n                              Assignment of a patient or group of patients to the case list of a health professional.\r\n\n                           \n                           \n                              Assignment of inpatients to the care of particular nurses for a working shift.";
            case ENC: return "An interaction between a patient and healthcare participant(s) for the purpose of providing patient service(s) or assessing the health status of a patient.  For example, outpatient visit to multiple departments, home health support (including physical therapy), inpatient hospital stay, emergency room visit, field visit (e.g., traffic accident), office visit, occupational therapy, telephone call.";
            case POLICY: return "Description:A mandate, regulation, obligation, requirement, rule, or expectation unilaterally imposed by one party on:\r\n\n                        \n                           \n                              The activity of another party\r\n\n                           \n                           \n                              The behavior of another party\r\n\n                           \n                           \n                              The manner in which an act is executed";
            case JURISPOL: return "Description:A mandate, regulation, obligation, requirement, rule, or expectation unilaterally imposed by a jurisdiction on:\r\n\n                        \n                           \n                              The activity of another party\r\n\n                           \n                           \n                              The behavior of another party\r\n\n                           \n                           \n                              The manner in which an act is executed\r\n\n                           \n                        \n                        \n                           Examples:A jurisdictional mandate regarding the prescribing and dispensing of a particular medication.  A jurisdictional privacy or security regulation dictating the manner in which personal health information is disclosed.  A jurisdictional requirement that certain services or health conditions are reported to a monitoring program, e.g., immunizations, methadone treatment, or cancer registries.";
            case ORGPOL: return "Description:A mandate, obligation, requirement, rule, or expectation unilaterally imposed by an organization on:\r\n\n                        \n                           \n                              The activity of another party\r\n\n                           \n                           \n                              The behavior of another party\r\n\n                           \n                           \n                              The manner in which an act is executed\r\n\n                           \n                        \n                        \n                           Examples:A clinical or research protocols imposed by a payer, a malpractice insurer, or an institution to which a provider must adhere.  A mandate imposed by a denominational institution for a provider to provide or withhold certain information from the patient about treatment options.";
            case SCOPOL: return "Description:An ethical or clinical obligation, requirement, rule, or expectation imposed or strongly encouraged by organizations that oversee particular clinical domains or provider certification which define the boundaries within which a provider may practice and which may have legal basis or ramifications on:\r\n\n                        \n                           \n                              The activity of another party\r\n\n                           \n                           \n                              The behavior of another party\r\n\n                           \n                           \n                              The manner in which an act is executed\r\n\n                           \n                        \n                        \n                           Examples:An ethical obligation for a provider to fully inform a patient about all treatment options.  An ethical obligation for a provider not to disclose personal health information that meets certain criteria, e.g., where disclosure might result in harm to the patient or another person.  The set of health care services which a provider is credentialed or privileged to provide.";
            case STDPOL: return "Description:A requirement, rule, or expectation typically documented as guidelines, protocols, or formularies imposed or strongly encouraged by an organization that oversees or has authority over the practices within a domain, and which may have legal basis or ramifications on:\r\n\n                        \n                           \n                              The activity of another party\r\n\n                           \n                           \n                              The behavior of another party\r\n\n                           \n                           \n                              The manner in which an act is executed\r\n\n                           \n                        \n                        \n                           Examples:A payer may require a prescribing provider to adhere to formulary guidelines.  An institution may adopt clinical guidelines and protocols and implement these within its electronic health record and decision support systems.";
            case PROC: return "An Act whose immediate and primary outcome (post-condition) is the alteration of the physical condition of the subject.\r\n\n                        \n                           Examples: : Procedures may involve the disruption of some body surface (e.g. an incision in a surgical procedure), but they also include conservative procedures such as reduction of a luxated join, chiropractic treatment, massage, balneotherapy, acupuncture, shiatsu, etc. Outside of clinical medicine, procedures may be such things as alteration of environments (e.g. straightening rivers, draining swamps, building dams) or the repair or change of machinery etc.";
            case SBADM: return "The act of introducing or otherwise applying a substance to the subject.\r\n\n                        \n                           Discussion: The effect of the substance is typically established on a biochemical basis, however, that is not a requirement. For example, radiotherapy can largely be described in the same way, especially if it is a systemic therapy such as radio-iodine.  This class also includes the application of chemical treatments to an area.\r\n\n                        \n                           Examples: Chemotherapy protocol; Drug prescription; Vaccination record";
            case SBEXT: return "Description: The act of removing a substance from the subject.";
            case SPECCOLLECT: return "A procedure for obtaining a specimen from a source entity.";
            case REG: return "Represents the act of maintaining information about the registration of its associated registered subject. The subject can be either an Act or a Role, and includes subjects such as lab exam definitions, drug protocol definitions, prescriptions, persons, patients, practitioners, and equipment.\r\n\n                        The registration may have a unique identifier - separate from the unique identification of the subject - as well as a core set of related participations and act relationships that characterize the registration event and aid in the disposition of the subject information by a receiving system.";
            case REV: return "The act of examining and evaluating the subject, usually another act. For example, \"This prescription needs to be reviewed in 2 months.\"";
            case SPCTRT: return "A procedure or treatment performed on a specimen to prepare it for analysis";
            case SPLY: return "Supply orders and deliveries are simple Acts that focus on the delivered product. The product is associated with the Supply Act via Participation.typeCode=\"product\". With general Supply Acts, the precise identification of the Material (manufacturer, serial numbers, etc.) is important.  Most of the detailed information about the Supply should be represented using the Material class.  If delivery needs to be scheduled, tracked, and billed separately, one can associate a Transportation Act with the Supply Act.  Pharmacy dispense services are represented as Supply Acts, associated with a SubstanceAdministration  Act. The SubstanceAdministration class represents the administration of medication, while dispensing is supply.";
            case DIET: return "Diet services are supply services, with some aspects resembling Medication services: the detail of the diet is given as a description of the Material associated via Participation.typeCode=\"product\". Medically relevant diet types may be communicated in the Diet.code attribute using domain ActDietCode, however, the detail of the food supplied and the various combinations of dishes should be communicated as Material instances.\r\n\n                        \n                           Deprecation Note\n                        \r\n\n                        \n                           Class: Use either the Supply class (if dealing with what should be given to the patient) or SubstanceAdministration class (if dealing with what the patient should consume)\r\n\n                        \n                           energyQuantity: This quantity can be conveyed by using a Content relationship with a quantity attribute expressing the calories\r\n\n                        \n                           carbohydrateQuantity:This quantity can be conveyed using a Content relationship to an Entity with a code of  carbohydrate and a quantity attribute on the content relationship.";
            case STORE: return "The act of putting something away for safe keeping. The \"something\" may be physical object such as a specimen, or information, such as observations regarding a specimen.";
            case SUBST: return "Definition: Indicates that the subject Act has undergone or should undergo substitution of a type indicated by Act.code.\r\n\n                        Rationale: Used to specify \"allowed\" substitution when creating orders, \"actual\" susbstitution when sending events, as well as the reason for the substitution and who was responsible for it.";
            case TRFR: return "Definition: The act of transferring information without the intent of imparting understanding about a topic to the subject that is the recipient or holder of the transferred information where the participation association must be RCV or HLD.";
            case TRNS: return "Transportation is the moving of a payload (people or material) from a location of origin to a destination location.  Thus, any transport service has the three target instances of type payload, origin, and destination, besides the targets that are generally used for any service (i.e., performer, device, etc.)";
            case XACT: return "A sub-class of Act representing any transaction between two accounts whose value is measured in monetary terms.\r\n\n                        In the \"intent\" mood, communicates a request for a transaction to be initiated, or communicates a transfer of value between two accounts.\r\n\n                        In the \"event\" mood, communicates the posting of a transaction to an account.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ACT: return "act";
            case _ACTCLASSRECORDORGANIZER: return "record organizer";
            case COMPOSITION: return "composition";
            case DOC: return "document";
            case DOCCLIN: return "clinical document";
            case CDALVLONE: return "CDA Level One clinical document";
            case CONTAINER: return "record container";
            case CATEGORY: return "category";
            case DOCBODY: return "document body";
            case DOCSECT: return "document section";
            case TOPIC: return "topic";
            case EXTRACT: return "extract";
            case EHR: return "electronic health record";
            case FOLDER: return "folder";
            case GROUPER: return "grouper";
            case CLUSTER: return "Cluster";
            case ACCM: return "accommodation";
            case ACCT: return "account";
            case ACSN: return "accession";
            case ADJUD: return "financial adjudication";
            case CACT: return "control act";
            case ACTN: return "action";
            case INFO: return "information";
            case STC: return "state transition control";
            case CNTRCT: return "contract";
            case FCNTRCT: return "financial contract";
            case COV: return "coverage";
            case CONC: return "concern";
            case HCASE: return "public health case";
            case OUTBR: return "outbreak";
            case CONS: return "consent";
            case CONTREG: return "container registration";
            case CTTEVENT: return "clinical trial timepoint event";
            case DISPACT: return "disciplinary action";
            case EXPOS: return "exposure";
            case AEXPOS: return "acquisition exposure";
            case TEXPOS: return "transmission exposure";
            case INC: return "incident";
            case INFRM: return "inform";
            case INVE: return "invoice element";
            case LIST: return "working list";
            case MPROT: return "monitoring program";
            case OBS: return "observation";
            case _ACTCLASSROI: return "ActClassROI";
            case ROIBND: return "bounded ROI";
            case ROIOVL: return "overlay ROI";
            case _SUBJECTPHYSICALPOSITION: return "subject physical position";
            case _SUBJECTBODYPOSITION: return "subject body position";
            case LLD: return "left lateral decubitus";
            case PRN: return "prone";
            case RLD: return "right lateral decubitus";
            case SFWL: return "Semi-Fowler's";
            case SIT: return "sitting";
            case STN: return "standing";
            case SUP: return "supine";
            case RTRD: return "reverse trendelenburg";
            case TRD: return "trendelenburg";
            case ALRT: return "detected issue";
            case BATTERY: return "battery";
            case CLNTRL: return "clinical trial";
            case CNOD: return "Condition Node";
            case COND: return "Condition";
            case CASE: return "public health case";
            case OUTB: return "outbreak";
            case DGIMG: return "diagnostic image";
            case GEN: return "genomic observation";
            case DETPOL: return "determinant peptide";
            case EXP: return "expression level";
            case LOC: return "locus";
            case PHN: return "phenotype";
            case POL: return "polypeptide";
            case SEQ: return "bio sequence";
            case SEQVAR: return "bio sequence variation";
            case INVSTG: return "investigation";
            case OBSSER: return "observation series";
            case OBSCOR: return "correlated observation sequences";
            case POS: return "position";
            case POSACC: return "position accuracy";
            case POSCOORD: return "position coordinate";
            case SPCOBS: return "specimen observation";
            case VERIF: return "Verification";
            case PCPR: return "care provision";
            case ENC: return "encounter";
            case POLICY: return "policy";
            case JURISPOL: return "jurisdictional policy";
            case ORGPOL: return "organizational policy";
            case SCOPOL: return "scope of practice policy";
            case STDPOL: return "standard of practice policy";
            case PROC: return "procedure";
            case SBADM: return "substance administration";
            case SBEXT: return "Substance Extraction";
            case SPECCOLLECT: return "Specimen Collection";
            case REG: return "registration";
            case REV: return "review";
            case SPCTRT: return "specimen treatment";
            case SPLY: return "supply";
            case DIET: return "diet";
            case STORE: return "storage";
            case SUBST: return "Substitution";
            case TRFR: return "transfer";
            case TRNS: return "transportation";
            case XACT: return "financial transaction";
            default: return "?";
          }
    }


}

