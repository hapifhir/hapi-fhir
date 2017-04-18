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

public enum V3RoleClass {

        /**
         * Corresponds to the Role class
         */
        ROL, 
        /**
         * A general association between two entities that is neither partitive nor ontological.
         */
        _ROLECLASSASSOCIATIVE, 
        /**
         * A relationship that is based on mutual behavior of the two Entities as being related. The basis of such relationship may be agreements (e.g., spouses, contract parties) or they may be de facto behavior (e.g. friends) or may be an incidental involvement with each other (e.g. parties over a dispute, siblings, children).
         */
        _ROLECLASSMUTUALRELATIONSHIP, 
        /**
         * A relationship between two entities that is formally recognized, frequently by a contract or similar agreement.
         */
        _ROLECLASSRELATIONSHIPFORMAL, 
        /**
         * Player of the Affiliate role has a business/professional relationship with scoper.  Player and scoper may be persons or organization.  The Affiliate relationship does not imply membership in a group, nor does it exist for resource scheduling purposes.

                        
                           Example: A healthcare provider is affiliated with another provider as a business associate.
         */
        AFFL, 
        /**
         * An entity (player) that acts or is authorized to act on behalf of another entity (scoper).
         */
        AGNT, 
        /**
         * An agent role in which the agent is an Entity acting in the employ of an organization.  The focus is on functional role on behalf of the organization, unlike the Employee role where the focus is on the 'Human Resources' relationship between the employee and the organization.
         */
        ASSIGNED, 
        /**
         * An Entity that is authorized to issue or instantiate permissions, privileges, credentials or other formal/legal authorizations.
         */
        COMPAR, 
        /**
         * The role of a person (player) who is the officer or signature authority for of a scoping entity, usually an organization (scoper).
         */
        SGNOFF, 
        /**
         * A person or an organization (player) which provides or receives information regarding another entity (scoper).  Examples; patient NOK and emergency contacts; guarantor contact; employer contact.
         */
        CON, 
        /**
         * An entity to be contacted in the event of an emergency.
         */
        ECON, 
        /**
         * An individual designated for notification as the next of kin for a given entity.
         */
        NOK, 
        /**
         * Guardian of a ward
         */
        GUARD, 
        /**
         * Citizen of apolitical entity
         */
        CIT, 
        /**
         * A role class played by a person who receives benefit coverage under the terms of a particular insurance policy.  The underwriter of that policy is the scoping entity.  The covered party receives coverage because of some contractual or other relationship with the holder of that policy.

                        
                           Discussion:This reason for coverage is captured in 'Role.code' and a relationship link with type code of indirect authority should be included using the policy holder role as the source, and the covered party role as the target.

                        Note that a particular policy may cover several individuals one of whom may be, but need not be, the policy holder.  Thus the notion of covered party is a role that is distinct from that of the policy holder.
         */
        COVPTY, 
        /**
         * Description: A role played by a party making a claim for coverage under a policy or program.  A claimant must be either a person or organization, or a group of persons or organizations.  A claimant is not a named insured or a program eligible.

                        
                           Discussion: With respect to liability insurance such as property and casualty insurance, a claimant must file a claim requesting indemnification for a loss that the claimant considers covered under the policy of a named insured.  The claims adjuster for the policy underwriter will review the claim to determine whether the loss meets the benefit coverage criteria under a policy, and base any indemnification or coverage payment on that review.  If a third party is liable in whole or part for the loss, the underwriter may pursue third party liability recovery.  A claimant may be involved in civil or criminal legal proceedings involving claims against a defendant party that is indemnified by an insurance policy or to protest the finding of a claims adjustor. With respect to life insurance, a beneficiary designated by a named insured becomes a claimant of the proceeds of coverage, as in the case of a life insurance policy.  However, a claimant for coverage under life insurance is not necessarily a designated beneficiary.

                        
                           Note: A claimant is not a named insured.  However, a named insured may make a claim under a policy, e.g., an insured driver may make a claim for an injury under his or her comprehensive automobile insurance policy.  Similarly, a program eligible may make a claim under program, e.g., an unemployed worker may claim benefits under an unemployment insurance program, but parties playing these covered party role classes are not, for purposes of this vocabulary and in an effort to clearly distinguish role classes, considered claimants.

                        In the case of a named insured making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that either a named insured or an individual insured has filed a claim for a loss.  In the case of a program eligible, a role type code INJWKR (injured worker) subtypes the class to indicate that the covered party in a workers compensation program is an injured worker, and as such, has filed a "claim" under the program for benefits.  Likewise, a covered role type code UNEMP (unemployed worker) subtypes the program eligible class to indicate that the covered party in an unemployment insurance program has filed a claim for unemployment benefits.

                        
                           Example: A claimant under automobile policy that is not the named insured.
         */
        CLAIM, 
        /**
         * Description: A role played by a party to an insurance policy to which the insurer agrees to indemnify for losses, provides benefits for, or renders services.  A named insured may be either a person, non-person living subject, or an organization, or a group of persons, non-person living subjects, or organizations.

                        
                           Discussion: The coded concept NAMED should not be used where a more specific child concept in this Specializable value set applies.  In some cases, the named insured may not be the policy holder, e.g., where a policy holder purchases life insurance policy in which another party is the named insured and the policy holder is the beneficiary of the policy.

                        
                           Note: The party playing the role of a named insured is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However, a named insured may make a claim under a policy, e.g., e.g., a party that is the named insured and policy holder under a comprehensive automobile insurance policy may become the claimant for coverage under that policy e.g., if injured in an automobile accident and there is no liable third party.  In the case of a named insured making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that a named insured has filed a claim for a loss.

                        
                           Example: The named insured under a comprehensive automobile, disability, or property and casualty policy that is the named insured and may or may not be the policy holder.
         */
        NAMED, 
        /**
         * Description: A role played by a person covered under a policy or program based on an association with a subscriber, which is recognized by the policy holder.

                        
                           Note:  The party playing the role of a dependent is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However, a dependent may make a claim under a policy, e.g., a dependent under a health insurance policy may become the claimant for coverage under that policy for wellness examines or if injured and there is no liable third party.  In the case of a dependent making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that the dependent has filed a claim for services covered under the health insurance policy.

                        
                           Example: The dependent has an association with the subscriber such as a financial dependency or personal relationship such as that of a spouse, or a natural or adopted child.  The policy holder may be required by law to recognize certain associations or may have discretion about the associations.  For example, a policy holder may dictate the criteria for the dependent status of adult children who are students, such as requiring full time enrollment, or may recognize domestic partners as dependents.  Under certain circumstances, the dependent may be under the indirect authority of a responsible party acting as a surrogate for the subscriber, for example, if the subscriber is differently abled or deceased, a guardian ad Lidem or estate executor may be appointed to assume the subscriberaTMs legal standing in the relationship with the dependent.
         */
        DEPEN, 
        /**
         * Description: A role played by a party covered under a policy as the policy holder.  An individual may be either a person or an organization.

                        
                           Note: The party playing the role of an individual insured is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However, a named insured may make a claim under a policy, e.g., a party that is the named insured and policy holder under a comprehensive automobile insurance policy may become the claimant for coverage under that policy if injured in an automobile accident and there is no liable third party.  In the case of an individual insured making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that an individual insured has filed a claim for a loss.

                        
                           Example: The individual insured under a comprehensive automobile, disability, or property and casualty policy that is the policy holder.
         */
        INDIV, 
        /**
         * Description: A role played by a person covered under a policy based on association with a sponsor who is the policy holder, and whose association may provide for the eligibility of dependents for coverage.

                        
                           Discussion: The policy holder holds the contract with the policy or program underwriter.  The subscriber holds a certificate of coverage under the contract.  In legal proceedings concerning the policy or program, the terms of the contract takes precedence over the terms of the certificate of coverage if there are any inconsistencies.

                        
                           Note: The party playing the role of a subscriber is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However, a subscriber may make a claim under a policy, e.g., a subscriber under a health insurance policy may become the claimant for coverage under that policy for wellness examines or if injured and there is no liable third party.  In the case of a subscriber making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that the subscriber has filed a claim for services covered under the health insurance policy.

                        
                           Example: An employee or a member of an association.
         */
        SUBSCR, 
        /**
         * Description: A role played by a party that meets the eligibility criteria for coverage under a program.  A program eligible may be either a person, non-person living subject, or an organization, or a group of persons, non-person living subjects, or organizations.

                        
                           Discussion: A program as typically government administered coverage for parties determined eligible under the terms of the program.

                        
                           Note: The party playing a program eligible is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However a program eligible may make a claim under program, e.g., an unemployed worker may claim benefits under an unemployment insurance program, but parties playing these covered party role classes are not, for purposes of this vocabulary and in an effort to clearly distinguish role classes, considered claimants.

                        In the case of a program eligible, a role type code INJWKR (injured worker) subtypes the class to indicate that the covered party in a workers compensation program is an injured worker, and as such, has filed a "claim" under the program for benefits.  Likewise, a covered role type code UNEMP (unemployed worker) subtypes the program eligible class to indicate that the covered party in an unemployment insurance program has filed a claim for unemployment benefits.

                        
                           Example: A party meeting eligibility criteria related to health or financial status, e.g., in the U.S., persons meeting health, demographic, or financial criteria established by state and federal law are eligible for Medicaid.
         */
        PROG, 
        /**
         * A role played by a provider, always a person, who has agency authority from a Clinical Research Sponsor to direct the conduct of a clinical research trial or study on behalf of the sponsor.
         */
        CRINV, 
        /**
         * A role played by an entity, usually an organization, that is the sponsor of a clinical research trial or study.  The sponsor commissions the study, bears the expenses, is responsible for satisfying all legal requirements concerning subject safety and privacy, and is generally responsible for collection, storage and analysis of the data generated during the trial.  No scoper is necessary, as a clinical research sponsor undertakes the role on its own authority and declaration. Clinical research sponsors are usually educational or other research organizations, government agencies or biopharmaceutical companies.
         */
        CRSPNSR, 
        /**
         * A relationship between a person or organization and a person or organization formed for the purpose of exchanging work for compensation.  The purpose of the role is to identify the type of relationship the employee has to the employer, rather than the nature of the work actually performed.  (Contrast with AssignedEntity.)
         */
        EMP, 
        /**
         * A role played by a member of a military service. Scoper is the military service (e.g. Army, Navy, Air Force, etc.) or, more specifically, the unit (e.g. Company C, 3rd Battalion, 4th Division, etc.)
         */
        MIL, 
        /**
         * A person or organization (player) that serves as a financial guarantor for another person or organization (scoper).
         */
        GUAR, 
        /**
         * An entity that is the subject of an investigation. This role is scoped by the party responsible for the investigation.
         */
        INVSBJ, 
        /**
         * A person, non-person living subject, or place that is the subject of an investigation related to a notifiable condition (health circumstance that is reportable within the applicable public health jurisdiction)
         */
        CASEBJ, 
        /**
         * Definition:Specifies the administrative functionality within a formal experimental design for which the ResearchSubject role was established.

                        
                           Examples: Screening - role is used for pre-enrollment evaluation portion of the design; enrolled - role is used for subjects admitted to the experimental portion of the design.
         */
        RESBJ, 
        /**
         * A relationship in which the scoper certifies the player ( e. g. a medical care giver, a medical device or a provider organization) to perform certain activities that fall under the jurisdiction of the scoper (e.g., a health authority licensing healthcare providers, a medical quality authority certifying healthcare professionals).
         */
        LIC, 
        /**
         * notary public
         */
        NOT, 
        /**
         * An Entity (player) that is authorized to provide health care services by some authorizing agency (scoper).
         */
        PROV, 
        /**
         * A Role of a LivingSubject (player) as an actual or potential recipient of health care services from a healthcare provider organization (scoper).

                        
                           Usage Note: Communication about relationships between patients and specific healthcare practitioners (people) is not done via scoper.  Instead this is generally done using the CareProvision act.  This allows linkage between patient and a particular healthcare practitioner role and also allows description of the type of care involved in the relationship.
         */
        PAT, 
        /**
         * The role of an organization or individual designated to receive payment for a claim against a particular coverage. The scoping entity is the organization that is the submitter of the invoice in question.
         */
        PAYEE, 
        /**
         * The role of an organization that undertakes to accept claims invoices, assess the coverage or payments due for those invoices and pay to the designated payees for those invoices.  This role may be either the underwriter or a third-party organization authorized by the underwriter.  The scoping entity is the organization that underwrites the claimed coverage.
         */
        PAYOR, 
        /**
         * A role played by a person or organization that holds an insurance policy.  The underwriter of that policy is the scoping entity.

                        
                           Discussion:The identifier of the policy is captured in 'Role.id' when the Role is a policy holder.

                        A particular policy may cover several individuals one of whom may be, but need not be, the policy holder.  Thus the notion of covered party is a role that is distinct from that of the policy holder.
         */
        POLHOLD, 
        /**
         * An entity (player) that has been recognized as having certain training/experience or other characteristics that would make said entity an appropriate performer for a certain activity. The scoper is an organization that educates or qualifies entities.
         */
        QUAL, 
        /**
         * A role played by an entity, usually an organization that is the sponsor of an insurance plan or a health program. A sponsor is the party that is ultimately accountable for the coverage by employment contract or by law.  A sponsor can be an employer, union, government agency, or association.  Fully insured sponsors establish the terms of the plan and contract with health insurance plans to assume the risk and to administer the plan.  Self-insured sponsors delegate coverage administration, but not risk, to third-party administrators.  Program sponsors designate services to be covered in accordance with statute.   Program sponsors may administer the coverage themselves, delegate coverage administration, but not risk to third-party administrators, or contract with health insurance plans to assume the risk and administrator a program. Sponsors qualify individuals who may become 

                        
                           
                              a policy holder of the plan;

                           
                           
                              where the sponsor is the policy holder, who may become a subscriber or a dependent to a policy under the plan; or

                           
                           
                              where the sponsor is a government agency, who may become program eligibles under a program. 

                           
                        
                        The sponsor role may be further qualified by the SponsorRole.code.  Entities playing the sponsor role may also play the role of a Coverage Administrator.

                        
                           Example: An employer, union, government agency, or association.
         */
        SPNSR, 
        /**
         * A role played by an individual who is a student of a school, which is the scoping entity.
         */
        STD, 
        /**
         * A role played by a person or an organization.  It is the party that 

                        
                           
                              accepts fiscal responsibility for insurance plans and the policies created under those plans;

                           
                           
                              administers and accepts fiscal responsibility for a program that provides coverage for services to eligible individuals; and/or

                           
                           
                              has the responsibility to assess the merits of each risk and decide a suitable premium for accepting all or part of the risk.  If played by an organization, this role may be further specified by an appropriate RoleCode.

                           
                        
                        
                           Example:
                        

                        
                           
                              A health insurer; 

                           
                           
                              Medicaid Program;

                           
                           
                              Lloyd's of London
         */
        UNDWRT, 
        /**
         * A person responsible for the primary care of a patient at home.
         */
        CAREGIVER, 
        /**
         * Links two entities with classCode PSN (person) in a personal relationship. The character of the relationship must be defined by a PersonalRelationshipRoleType code. The player and scoper are determined by PersonalRelationshipRoleType code as well.
         */
        PRS, 
        /**
         * The "same" roleclass asserts an identity between playing and scoping entities: that they are in fact instances of the same entity and, in the case of discrepancies (e.g different DOB, gender), that one or both are in error.

                        
                           Usage:
                        

                        playing and scoping entities must have same classcode, but need not have identical attributes or values. 

                        
                           Example: 
                        

                        a provider registry maintains sets of conflicting demographic data for what is reported to be the same individual.
         */
        SELF, 
        /**
         * An association for a playing Entity that is used, known, treated, handled, built, or destroyed, etc. under the auspices of the scoping Entity. The playing Entity is passive in these roles (even though it may be active in other roles), in the sense that the kinds of things done to it in this role happen without an agreement from the playing Entity.
         */
        _ROLECLASSPASSIVE, 
        /**
         * A role in which the playing entity (material) provides access to another entity. The principal use case is intravenous (or other bodily) access lines that preexist and need to be referred to for medication routing instructions.
         */
        ACCESS, 
        /**
         * A physical association whereby two Entities are in some (even lose) spatial relationship with each other such that they touch each other in some way.

                        
                           Examples: the colon is connected (and therefore adjacent) to the jejunum; the colon is adjacent to the liver (even if not actually connected.)

                        
                           UsageConstraints: Adjacency is in principle a symmetrical connection, but scoper and player of the role should, where applicable, be assigned to have scoper be the larger, more central Entity and player the smaller, more distant, appendage.
         */
        ADJY, 
        /**
         * An adjacency of two Entities held together by a bond which attaches to each of the two entities. 

                        
                           Examples: biceps brachii muscle connected to the radius bone, port 3 on a network switch connected to port 5 on a patch panel.

                        
                           UsageConstraints: See Adjacency for the assignment of scoper (larger, more central) and player (smaller, more distant).
         */
        CONC, 
        /**
         * A connection between two atoms of a molecule.

                        
                           Examples: double bond between first and second C in ethane, peptide bond between two amino-acid, disulfide bridge between two proteins, chelate and ion associations, even the much weaker van-der-Waals bonds can be considered molecular bonds.

                        
                           UsageConstraints: See connection and adjacency for the assignment of player and scoper.
         */
        BOND, 
        /**
         * A connection between two regional parts.

                        
                           Examples:  the connection between ascending aorta and the aortic arc, connection between descending colon and sigmoid.

                        
                           UsageConstraints: See connection and adjacency for the assignment of player and scoper.
         */
        CONY, 
        /**
         * A material (player) that can be administered to an Entity (scoper).
         */
        ADMM, 
        /**
         * Relates a place (playing Entity) as the location where a living subject (scoping Entity) was born.
         */
        BIRTHPL, 
        /**
         * Definition: Relates a place (playing Entity) as the location where a living subject (scoping Entity) died.
         */
        DEATHPLC, 
        /**
         * A material (player) distributed by a distributor (scoper) who functions between a manufacturer and a buyer or retailer.
         */
        DST, 
        /**
         * Material (player) sold by a retailer (scoper), who also give advice to prospective buyers.
         */
        RET, 
        /**
         * A role played by a place at which the location of an event may be recorded.
         */
        EXLOC, 
        /**
         * A role played by a place at which services may be provided.
         */
        SDLOC, 
        /**
         * A role of a place (player) that is intended to house the provision of services. Scoper is the Entity (typically Organization) that provides these services. This is not synonymous with "ownership."
         */
        DSDLOC, 
        /**
         * A role played by a place at which health care services may be provided without prior designation or authorization.
         */
        ISDLOC, 
        /**
         * A role played by an entity that has been exposed to a person or animal suffering a contagious disease, or with a location from which a toxin has been distributed.  The player of the role is normally a person or animal, but it is possible that other entity types could become exposed.  The role is scoped by the source of the exposure, and it is quite possible for a person playing the role of exposed party to also become the scoper a role played by another person.  That is to say, once a person has become infected, it is possible, perhaps likely, for that person to infect others.

                        Management of exposures and tracking exposed parties is a key function within public health, and within most public health contexts - exposed parties are known as "contacts."
         */
        EXPR, 
        /**
         * Entity that is currently in the possession of a holder (scoper), who holds, or uses it, usually based on some agreement with the owner.
         */
        HLD, 
        /**
         * The role of a material (player) that is the physical health chart belonging to an organization (scoper).
         */
        HLTHCHRT, 
        /**
         * A role in which the scoping entity designates an identifier for a playing entity.
         */
        IDENT, 
        /**
         * Scoped by the manufacturer
         */
        MANU, 
        /**
         * A manufactured material (player) that is used for its therapeutic properties.  The manufacturer is the scoper.
         */
        THER, 
        /**
         * An entity (player) that is maintained by another entity (scoper).  This is typical role held by durable equipment. The scoper assumes responsibility for proper operation, quality, and safety.
         */
        MNT, 
        /**
         * An Entity (player) for which someone (scoper) is granted by law the right to call the material (player) his own.  This entitles the scoper to make decisions about the disposition of that material.
         */
        OWN, 
        /**
         * A product regulated by some governmentatl orgnization.  The role is played by Material and scoped by Organization.

                        Rationale: To support an entity clone used to identify the NDC number for a drug product.
         */
        RGPR, 
        /**
         * Relates a place entity (player) as the region over which the scoper (typically an Organization) has certain authority (jurisdiction). For example, the Calgary Regional Health Authority (scoper) has authority over the territory "Region 4 of Alberta" (player) in matters of health.
         */
        TERR, 
        /**
         * Description:An entity (player) that is used by another entity (scoper)
         */
        USED, 
        /**
         * A role a product plays when a guarantee is given to the purchaser by the seller (scoping entity) stating that the product is reliable and free from known defects and that the seller will repair or replace defective parts within a given time limit and under certain conditions.
         */
        WRTE, 
        /**
         * A relationship in which the scoping Entity defines or specifies what the playing Entity is.  Thus, the player's "being" (Greek: ontos) is specified.
         */
        _ROLECLASSONTOLOGICAL, 
        /**
         * Description: Specifies the player Entity (the equivalent Entity) as an Entity that is considered to be equivalent to a reference Entity (scoper).  The equivalence is in principle a symmetric relationship, however, it is expected that the scoper is a reference entity which serves as reference entity for multiple different equivalent entities. 

                        
                           Examples: An innovator's medicine formulation is the reference for "generics", i.e., formulations manufactured differently but having been proven to be biologically equivalent to the reference medicine. Another example is a reference ingredient that serves as basis for quantity specifications (basis of strength, e.g., metoprolol succinate specified in terms of metoprolol tartrate.)
         */
        EQUIV, 
        /**
         * The "same" role asserts an identity between playing and scoping entities, i.e., that they are in fact two records of the same entity instance, and, in the case of discrepancies (e.g different DOB, gender), that one or both are in error.

                        
                           Usage:
                        

                        playing and scoping entities must have same classCode, but need not have identical attributes or values.

                        
                           Example: 
                        

                        a provider registry maintains sets of conflicting demographic data for what is reported to be the same individual.
         */
        SAME, 
        /**
         * Relates a prevailing record of an Entity (scoper) with another record (player) that it subsumes.

                        
                           Examples: Show a correct new Person object (scoper) that subsumes one or more duplicate Person objects that had accidentally been created for the same physical person.

                        
                           Constraints: Both the player and scoper must have the same classCode.
         */
        SUBY, 
        /**
         * Relates a specialized material concept (player) to its generalization (scoper).
         */
        GEN, 
        /**
         * A special link between pharmaceuticals indicating that the target (scoper) is a generic for the source (player).
         */
        GRIC, 
        /**
         * An individual piece of material (player) instantiating a class of material (scoper).
         */
        INST, 
        /**
         * An entity that subsumes the identity of another.  Used in the context of merging documented entity instances. Both the player and scoper must have the same classCode.

                        The use of this code is deprecated in favor of the term SUBY which is its inverse and is more ontologically correct.
         */
        SUBS, 
        /**
         * An association between two Entities where the playing Entity is considered in some way "part" of the scoping Entity, e.g., as a member, component, ingredient, or content. Being "part" in the broadest sense of the word can mean anything from being an integral structural component to a mere incidental temporary association of a playing Entity with a (generally larger) scoping Entity.
         */
        _ROLECLASSPARTITIVE, 
        /**
         * Relates a material as the content (player) to a container (scoper).  Unlike ingredients, the content and a container remain separate (not mixed) and the content can be removed from the container.  A content is not part of an empty container.
         */
        CONT, 
        /**
         * An exposure agent carrier is an entity that is capable of conveying an exposure agent from one entity to another.  The scoper of the role must be the exposure agent (e.g., pathogen).
         */
        EXPAGTCAR, 
        /**
         * Description: A vector is a living subject that carries an exposure agent.  The vector does not cause the disease itself, but exposes targets to the exposure agent.  A mosquito carrying malaria is an example of a vector.  The scoper of the role must be the exposure agent (e.g., pathogen).
         */
        EXPVECTOR, 
        /**
         * Description: A fomite is a non-living entity that is capable of conveying exposure agent from one entity to another.  A doorknob contaminated with a Norovirus is an example of a fomite.  Anyone touching the doorknob would be exposed to the virus.  The scoper of the role must be the exposure agent (e.g., pathogen).
         */
        FOMITE, 
        /**
         * Relates a component (player) to a mixture (scoper). E.g., Glucose and Water are ingredients of D5W, latex may be an ingredient in a tracheal tube.
         */
        INGR, 
        /**
         * Definition: a therapeutically active ingredient (player) in a mixture (scoper), where the mixture is typically a manufactured pharmaceutical.  It is unknown if the quantity of such an ingredient is expressed precisely in terms of the playing ingredient substance, or, if it is specified in terms of a closely related substance (active moiety or reference substance).
         */
        ACTI, 
        /**
         * Description:  Active ingredient, where the ingredient substance (player) is itself the "basis of strength", i.e., where the Role.quantity specifies exactly the quantity of the player substance in the medicine formulation. 

                        
                           Examples: Lopressor 50 mg actually contains 50 mg of metoprolol succinate, even though the active moiety is metoprolol,  but also: Tenormin 50 mg contain 50 mg of atenolol, as free base, i.e., where the active ingredient atenolol is also the active moiety.
         */
        ACTIB, 
        /**
         * Description: Active ingredient, where not the ingredient substance (player), but itaTMs active moiety is the "basis of strength", i.e., where the Role.quantity specifies the quantity of the player substance's active moiety in the medicine formulation.

                        
                           Examples: 1 mL of Betopic 5mg/mL eye drops contains 5.6 mg betaxolol hydrochloride equivalent to betaxolol base 5 mg.
         */
        ACTIM, 
        /**
         * Description: Active ingredient, where not the ingredient substance (player) but another reference substance with the same active moiety, is the "basis of strength", i.e., where the Role.quantity specifies the quantity of a reference substance, similar but different from the player substance's in the medicine formulation.

                        
                           Examples: Toprol-XL 50 mg contains 47.5 mg of metoprolol succinate equivalent to 50 mg of metoprolol tartrate.
         */
        ACTIR, 
        /**
         * A component (player) added to enhance the action of an active ingredient (scoper) (in the manner of a catalyst) but which has no active effect in and of itself.  Such ingredients are significant in defining equivalence of products in a way that inactive ingredients are not.
         */
        ADJV, 
        /**
         * An ingredient (player)  that is added to a base (scoper), that amounts to a minor part of the overall mixture.
         */
        ADTV, 
        /**
         * A base ingredient (player) is what comprises the major part of a mixture (scoper). E.g., Water in most i.v. solutions, or Vaseline in salves. Among all ingredients of a material, there should be only one base. A base substance can, in turn, be a mixture.
         */
        BASE, 
        /**
         * An ingredient whose presence is not intended but may not be reasonably avoided given the circumstances of the mixture's nature or origin.
         */
        CNTM, 
        /**
         * An ingredient which is not considered therapeutically active, e.g., colors, flavors, stabilizers, or preservatives, fillers, or structural components added to an active ingredient in order to facilitate administration of the active ingredient but without being considered therapeutically active. An inactive ingredient need not be biologically inert, e.g., might be active as an allergen or might have a pleasant taste, but is not an essential constituent delivering the therapeutic effect.
         */
        IACT, 
        /**
         * A substance (player) influencing the optical aspect of material (scoper).
         */
        COLR, 
        /**
         * A substance (player) added to a mixture (scoper) to make it taste a certain way.  In food the use is obvious, in pharmaceuticals flavors can hide disgusting taste of the active ingredient (important in pediatric treatments).
         */
        FLVR, 
        /**
         * A substance (player) added to a mixture (scoper) to prevent microorganisms (fungi, bacteria) to spoil the mixture.
         */
        PRSV, 
        /**
         * A stabilizer (player) added to a mixture (scoper) in order to prevent the molecular disintegration of the main substance.
         */
        STBL, 
        /**
         * An ingredient (player) of a medication (scoper) that is inseparable from the active ingredients, but has no intended chemical or pharmaceutical effect itself, but which may have some systemic effect on the patient.

                        An example is a collagen matrix used as a base for transplanting skin cells.  The collagen matrix can be left permanently in the graft site.  Because it is of bovine origin, the patient may exhibit allergies or may have cultural objections to its use.
         */
        MECH, 
        /**
         * Relates an entity (player) to a location (scoper) at which it is present in some way. This presence may be limited in time.
         */
        LOCE, 
        /**
         * Relates an entity (player) (e.g. a device) to a location (scoper) at which it is normally found or stored when not used.
         */
        STOR, 
        /**
         * A role played by an entity that is a member of a group.  The group provides the scope for this role.

                        Among other uses, groups as used in insurance (groups of covered individuals) and in scheduling where resources may be grouped for scheduling and logistical purposes.
         */
        MBR, 
        /**
         * Definition:  an association between two Entities where the playing Entity (the part) is a component of the whole (scoper) in the sense of an integral structural component, that is distinct from other parts in the same whole, has a distinct function in the whole, and, as an effect, the full integrity of the whole depends (to some degree) on the presence of this part, even though the part may often be separable from the whole.

                        
                           Discussion: Part is defined in opposition to (a) ingredient (not separable), (b) content (not a functional component), and (c) member (not functionally distinct from other members).
         */
        PART, 
        /**
         * The molecule or ion that is responsible for the intended pharmacological action of the drug substance, excluding those appended or associated parts of the molecule that make the molecule an ester, salt (including a salt with hydrogen or coordination bonds), or other noncovalent derivative (such as a complex, chelate, or clathrate).

                        Examples: heparin-sodium and heparin-potassium have the same active moiety, heparin; the active moiety of morphine-hydrochloride is morphine.
         */
        ACTM, 
        /**
         * A role played by a material entity that is a specimen for an act. It is scoped by the source of the specimen.
         */
        SPEC, 
        /**
         * A portion (player) of an original or source specimen (scoper) used for testing or transportation.
         */
        ALQT, 
        /**
         * A microorganism that has been isolated from other microorganisms or a source matrix.
         */
        ISLT, 
        /**
         * The player of the role is a child of the scoping entity, in a generic sense.
         */
        CHILD, 
        /**
         * A role played by an entity that receives credentials from the scoping entity.
         */
        CRED, 
        /**
         * nurse practitioner
         */
        NURPRAC, 
        /**
         * nurse
         */
        NURS, 
        /**
         * physician assistant
         */
        PA, 
        /**
         * physician
         */
        PHYS, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3RoleClass fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("ROL".equals(codeString))
          return ROL;
        if ("_RoleClassAssociative".equals(codeString))
          return _ROLECLASSASSOCIATIVE;
        if ("_RoleClassMutualRelationship".equals(codeString))
          return _ROLECLASSMUTUALRELATIONSHIP;
        if ("_RoleClassRelationshipFormal".equals(codeString))
          return _ROLECLASSRELATIONSHIPFORMAL;
        if ("AFFL".equals(codeString))
          return AFFL;
        if ("AGNT".equals(codeString))
          return AGNT;
        if ("ASSIGNED".equals(codeString))
          return ASSIGNED;
        if ("COMPAR".equals(codeString))
          return COMPAR;
        if ("SGNOFF".equals(codeString))
          return SGNOFF;
        if ("CON".equals(codeString))
          return CON;
        if ("ECON".equals(codeString))
          return ECON;
        if ("NOK".equals(codeString))
          return NOK;
        if ("GUARD".equals(codeString))
          return GUARD;
        if ("CIT".equals(codeString))
          return CIT;
        if ("COVPTY".equals(codeString))
          return COVPTY;
        if ("CLAIM".equals(codeString))
          return CLAIM;
        if ("NAMED".equals(codeString))
          return NAMED;
        if ("DEPEN".equals(codeString))
          return DEPEN;
        if ("INDIV".equals(codeString))
          return INDIV;
        if ("SUBSCR".equals(codeString))
          return SUBSCR;
        if ("PROG".equals(codeString))
          return PROG;
        if ("CRINV".equals(codeString))
          return CRINV;
        if ("CRSPNSR".equals(codeString))
          return CRSPNSR;
        if ("EMP".equals(codeString))
          return EMP;
        if ("MIL".equals(codeString))
          return MIL;
        if ("GUAR".equals(codeString))
          return GUAR;
        if ("INVSBJ".equals(codeString))
          return INVSBJ;
        if ("CASEBJ".equals(codeString))
          return CASEBJ;
        if ("RESBJ".equals(codeString))
          return RESBJ;
        if ("LIC".equals(codeString))
          return LIC;
        if ("NOT".equals(codeString))
          return NOT;
        if ("PROV".equals(codeString))
          return PROV;
        if ("PAT".equals(codeString))
          return PAT;
        if ("PAYEE".equals(codeString))
          return PAYEE;
        if ("PAYOR".equals(codeString))
          return PAYOR;
        if ("POLHOLD".equals(codeString))
          return POLHOLD;
        if ("QUAL".equals(codeString))
          return QUAL;
        if ("SPNSR".equals(codeString))
          return SPNSR;
        if ("STD".equals(codeString))
          return STD;
        if ("UNDWRT".equals(codeString))
          return UNDWRT;
        if ("CAREGIVER".equals(codeString))
          return CAREGIVER;
        if ("PRS".equals(codeString))
          return PRS;
        if ("SELF".equals(codeString))
          return SELF;
        if ("_RoleClassPassive".equals(codeString))
          return _ROLECLASSPASSIVE;
        if ("ACCESS".equals(codeString))
          return ACCESS;
        if ("ADJY".equals(codeString))
          return ADJY;
        if ("CONC".equals(codeString))
          return CONC;
        if ("BOND".equals(codeString))
          return BOND;
        if ("CONY".equals(codeString))
          return CONY;
        if ("ADMM".equals(codeString))
          return ADMM;
        if ("BIRTHPL".equals(codeString))
          return BIRTHPL;
        if ("DEATHPLC".equals(codeString))
          return DEATHPLC;
        if ("DST".equals(codeString))
          return DST;
        if ("RET".equals(codeString))
          return RET;
        if ("EXLOC".equals(codeString))
          return EXLOC;
        if ("SDLOC".equals(codeString))
          return SDLOC;
        if ("DSDLOC".equals(codeString))
          return DSDLOC;
        if ("ISDLOC".equals(codeString))
          return ISDLOC;
        if ("EXPR".equals(codeString))
          return EXPR;
        if ("HLD".equals(codeString))
          return HLD;
        if ("HLTHCHRT".equals(codeString))
          return HLTHCHRT;
        if ("IDENT".equals(codeString))
          return IDENT;
        if ("MANU".equals(codeString))
          return MANU;
        if ("THER".equals(codeString))
          return THER;
        if ("MNT".equals(codeString))
          return MNT;
        if ("OWN".equals(codeString))
          return OWN;
        if ("RGPR".equals(codeString))
          return RGPR;
        if ("TERR".equals(codeString))
          return TERR;
        if ("USED".equals(codeString))
          return USED;
        if ("WRTE".equals(codeString))
          return WRTE;
        if ("_RoleClassOntological".equals(codeString))
          return _ROLECLASSONTOLOGICAL;
        if ("EQUIV".equals(codeString))
          return EQUIV;
        if ("SAME".equals(codeString))
          return SAME;
        if ("SUBY".equals(codeString))
          return SUBY;
        if ("GEN".equals(codeString))
          return GEN;
        if ("GRIC".equals(codeString))
          return GRIC;
        if ("INST".equals(codeString))
          return INST;
        if ("SUBS".equals(codeString))
          return SUBS;
        if ("_RoleClassPartitive".equals(codeString))
          return _ROLECLASSPARTITIVE;
        if ("CONT".equals(codeString))
          return CONT;
        if ("EXPAGTCAR".equals(codeString))
          return EXPAGTCAR;
        if ("EXPVECTOR".equals(codeString))
          return EXPVECTOR;
        if ("FOMITE".equals(codeString))
          return FOMITE;
        if ("INGR".equals(codeString))
          return INGR;
        if ("ACTI".equals(codeString))
          return ACTI;
        if ("ACTIB".equals(codeString))
          return ACTIB;
        if ("ACTIM".equals(codeString))
          return ACTIM;
        if ("ACTIR".equals(codeString))
          return ACTIR;
        if ("ADJV".equals(codeString))
          return ADJV;
        if ("ADTV".equals(codeString))
          return ADTV;
        if ("BASE".equals(codeString))
          return BASE;
        if ("CNTM".equals(codeString))
          return CNTM;
        if ("IACT".equals(codeString))
          return IACT;
        if ("COLR".equals(codeString))
          return COLR;
        if ("FLVR".equals(codeString))
          return FLVR;
        if ("PRSV".equals(codeString))
          return PRSV;
        if ("STBL".equals(codeString))
          return STBL;
        if ("MECH".equals(codeString))
          return MECH;
        if ("LOCE".equals(codeString))
          return LOCE;
        if ("STOR".equals(codeString))
          return STOR;
        if ("MBR".equals(codeString))
          return MBR;
        if ("PART".equals(codeString))
          return PART;
        if ("ACTM".equals(codeString))
          return ACTM;
        if ("SPEC".equals(codeString))
          return SPEC;
        if ("ALQT".equals(codeString))
          return ALQT;
        if ("ISLT".equals(codeString))
          return ISLT;
        if ("CHILD".equals(codeString))
          return CHILD;
        if ("CRED".equals(codeString))
          return CRED;
        if ("NURPRAC".equals(codeString))
          return NURPRAC;
        if ("NURS".equals(codeString))
          return NURS;
        if ("PA".equals(codeString))
          return PA;
        if ("PHYS".equals(codeString))
          return PHYS;
        throw new FHIRException("Unknown V3RoleClass code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case ROL: return "ROL";
            case _ROLECLASSASSOCIATIVE: return "_RoleClassAssociative";
            case _ROLECLASSMUTUALRELATIONSHIP: return "_RoleClassMutualRelationship";
            case _ROLECLASSRELATIONSHIPFORMAL: return "_RoleClassRelationshipFormal";
            case AFFL: return "AFFL";
            case AGNT: return "AGNT";
            case ASSIGNED: return "ASSIGNED";
            case COMPAR: return "COMPAR";
            case SGNOFF: return "SGNOFF";
            case CON: return "CON";
            case ECON: return "ECON";
            case NOK: return "NOK";
            case GUARD: return "GUARD";
            case CIT: return "CIT";
            case COVPTY: return "COVPTY";
            case CLAIM: return "CLAIM";
            case NAMED: return "NAMED";
            case DEPEN: return "DEPEN";
            case INDIV: return "INDIV";
            case SUBSCR: return "SUBSCR";
            case PROG: return "PROG";
            case CRINV: return "CRINV";
            case CRSPNSR: return "CRSPNSR";
            case EMP: return "EMP";
            case MIL: return "MIL";
            case GUAR: return "GUAR";
            case INVSBJ: return "INVSBJ";
            case CASEBJ: return "CASEBJ";
            case RESBJ: return "RESBJ";
            case LIC: return "LIC";
            case NOT: return "NOT";
            case PROV: return "PROV";
            case PAT: return "PAT";
            case PAYEE: return "PAYEE";
            case PAYOR: return "PAYOR";
            case POLHOLD: return "POLHOLD";
            case QUAL: return "QUAL";
            case SPNSR: return "SPNSR";
            case STD: return "STD";
            case UNDWRT: return "UNDWRT";
            case CAREGIVER: return "CAREGIVER";
            case PRS: return "PRS";
            case SELF: return "SELF";
            case _ROLECLASSPASSIVE: return "_RoleClassPassive";
            case ACCESS: return "ACCESS";
            case ADJY: return "ADJY";
            case CONC: return "CONC";
            case BOND: return "BOND";
            case CONY: return "CONY";
            case ADMM: return "ADMM";
            case BIRTHPL: return "BIRTHPL";
            case DEATHPLC: return "DEATHPLC";
            case DST: return "DST";
            case RET: return "RET";
            case EXLOC: return "EXLOC";
            case SDLOC: return "SDLOC";
            case DSDLOC: return "DSDLOC";
            case ISDLOC: return "ISDLOC";
            case EXPR: return "EXPR";
            case HLD: return "HLD";
            case HLTHCHRT: return "HLTHCHRT";
            case IDENT: return "IDENT";
            case MANU: return "MANU";
            case THER: return "THER";
            case MNT: return "MNT";
            case OWN: return "OWN";
            case RGPR: return "RGPR";
            case TERR: return "TERR";
            case USED: return "USED";
            case WRTE: return "WRTE";
            case _ROLECLASSONTOLOGICAL: return "_RoleClassOntological";
            case EQUIV: return "EQUIV";
            case SAME: return "SAME";
            case SUBY: return "SUBY";
            case GEN: return "GEN";
            case GRIC: return "GRIC";
            case INST: return "INST";
            case SUBS: return "SUBS";
            case _ROLECLASSPARTITIVE: return "_RoleClassPartitive";
            case CONT: return "CONT";
            case EXPAGTCAR: return "EXPAGTCAR";
            case EXPVECTOR: return "EXPVECTOR";
            case FOMITE: return "FOMITE";
            case INGR: return "INGR";
            case ACTI: return "ACTI";
            case ACTIB: return "ACTIB";
            case ACTIM: return "ACTIM";
            case ACTIR: return "ACTIR";
            case ADJV: return "ADJV";
            case ADTV: return "ADTV";
            case BASE: return "BASE";
            case CNTM: return "CNTM";
            case IACT: return "IACT";
            case COLR: return "COLR";
            case FLVR: return "FLVR";
            case PRSV: return "PRSV";
            case STBL: return "STBL";
            case MECH: return "MECH";
            case LOCE: return "LOCE";
            case STOR: return "STOR";
            case MBR: return "MBR";
            case PART: return "PART";
            case ACTM: return "ACTM";
            case SPEC: return "SPEC";
            case ALQT: return "ALQT";
            case ISLT: return "ISLT";
            case CHILD: return "CHILD";
            case CRED: return "CRED";
            case NURPRAC: return "NURPRAC";
            case NURS: return "NURS";
            case PA: return "PA";
            case PHYS: return "PHYS";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/RoleClass";
        }
        public String getDefinition() {
          switch (this) {
            case ROL: return "Corresponds to the Role class";
            case _ROLECLASSASSOCIATIVE: return "A general association between two entities that is neither partitive nor ontological.";
            case _ROLECLASSMUTUALRELATIONSHIP: return "A relationship that is based on mutual behavior of the two Entities as being related. The basis of such relationship may be agreements (e.g., spouses, contract parties) or they may be de facto behavior (e.g. friends) or may be an incidental involvement with each other (e.g. parties over a dispute, siblings, children).";
            case _ROLECLASSRELATIONSHIPFORMAL: return "A relationship between two entities that is formally recognized, frequently by a contract or similar agreement.";
            case AFFL: return "Player of the Affiliate role has a business/professional relationship with scoper.  Player and scoper may be persons or organization.  The Affiliate relationship does not imply membership in a group, nor does it exist for resource scheduling purposes.\r\n\n                        \n                           Example: A healthcare provider is affiliated with another provider as a business associate.";
            case AGNT: return "An entity (player) that acts or is authorized to act on behalf of another entity (scoper).";
            case ASSIGNED: return "An agent role in which the agent is an Entity acting in the employ of an organization.  The focus is on functional role on behalf of the organization, unlike the Employee role where the focus is on the 'Human Resources' relationship between the employee and the organization.";
            case COMPAR: return "An Entity that is authorized to issue or instantiate permissions, privileges, credentials or other formal/legal authorizations.";
            case SGNOFF: return "The role of a person (player) who is the officer or signature authority for of a scoping entity, usually an organization (scoper).";
            case CON: return "A person or an organization (player) which provides or receives information regarding another entity (scoper).  Examples; patient NOK and emergency contacts; guarantor contact; employer contact.";
            case ECON: return "An entity to be contacted in the event of an emergency.";
            case NOK: return "An individual designated for notification as the next of kin for a given entity.";
            case GUARD: return "Guardian of a ward";
            case CIT: return "Citizen of apolitical entity";
            case COVPTY: return "A role class played by a person who receives benefit coverage under the terms of a particular insurance policy.  The underwriter of that policy is the scoping entity.  The covered party receives coverage because of some contractual or other relationship with the holder of that policy.\r\n\n                        \n                           Discussion:This reason for coverage is captured in 'Role.code' and a relationship link with type code of indirect authority should be included using the policy holder role as the source, and the covered party role as the target.\r\n\n                        Note that a particular policy may cover several individuals one of whom may be, but need not be, the policy holder.  Thus the notion of covered party is a role that is distinct from that of the policy holder.";
            case CLAIM: return "Description: A role played by a party making a claim for coverage under a policy or program.  A claimant must be either a person or organization, or a group of persons or organizations.  A claimant is not a named insured or a program eligible.\r\n\n                        \n                           Discussion: With respect to liability insurance such as property and casualty insurance, a claimant must file a claim requesting indemnification for a loss that the claimant considers covered under the policy of a named insured.  The claims adjuster for the policy underwriter will review the claim to determine whether the loss meets the benefit coverage criteria under a policy, and base any indemnification or coverage payment on that review.  If a third party is liable in whole or part for the loss, the underwriter may pursue third party liability recovery.  A claimant may be involved in civil or criminal legal proceedings involving claims against a defendant party that is indemnified by an insurance policy or to protest the finding of a claims adjustor. With respect to life insurance, a beneficiary designated by a named insured becomes a claimant of the proceeds of coverage, as in the case of a life insurance policy.  However, a claimant for coverage under life insurance is not necessarily a designated beneficiary.\r\n\n                        \n                           Note: A claimant is not a named insured.  However, a named insured may make a claim under a policy, e.g., an insured driver may make a claim for an injury under his or her comprehensive automobile insurance policy.  Similarly, a program eligible may make a claim under program, e.g., an unemployed worker may claim benefits under an unemployment insurance program, but parties playing these covered party role classes are not, for purposes of this vocabulary and in an effort to clearly distinguish role classes, considered claimants.\r\n\n                        In the case of a named insured making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that either a named insured or an individual insured has filed a claim for a loss.  In the case of a program eligible, a role type code INJWKR (injured worker) subtypes the class to indicate that the covered party in a workers compensation program is an injured worker, and as such, has filed a \"claim\" under the program for benefits.  Likewise, a covered role type code UNEMP (unemployed worker) subtypes the program eligible class to indicate that the covered party in an unemployment insurance program has filed a claim for unemployment benefits.\r\n\n                        \n                           Example: A claimant under automobile policy that is not the named insured.";
            case NAMED: return "Description: A role played by a party to an insurance policy to which the insurer agrees to indemnify for losses, provides benefits for, or renders services.  A named insured may be either a person, non-person living subject, or an organization, or a group of persons, non-person living subjects, or organizations.\r\n\n                        \n                           Discussion: The coded concept NAMED should not be used where a more specific child concept in this Specializable value set applies.  In some cases, the named insured may not be the policy holder, e.g., where a policy holder purchases life insurance policy in which another party is the named insured and the policy holder is the beneficiary of the policy.\r\n\n                        \n                           Note: The party playing the role of a named insured is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However, a named insured may make a claim under a policy, e.g., e.g., a party that is the named insured and policy holder under a comprehensive automobile insurance policy may become the claimant for coverage under that policy e.g., if injured in an automobile accident and there is no liable third party.  In the case of a named insured making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that a named insured has filed a claim for a loss.\r\n\n                        \n                           Example: The named insured under a comprehensive automobile, disability, or property and casualty policy that is the named insured and may or may not be the policy holder.";
            case DEPEN: return "Description: A role played by a person covered under a policy or program based on an association with a subscriber, which is recognized by the policy holder.\r\n\n                        \n                           Note:  The party playing the role of a dependent is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However, a dependent may make a claim under a policy, e.g., a dependent under a health insurance policy may become the claimant for coverage under that policy for wellness examines or if injured and there is no liable third party.  In the case of a dependent making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that the dependent has filed a claim for services covered under the health insurance policy.\r\n\n                        \n                           Example: The dependent has an association with the subscriber such as a financial dependency or personal relationship such as that of a spouse, or a natural or adopted child.  The policy holder may be required by law to recognize certain associations or may have discretion about the associations.  For example, a policy holder may dictate the criteria for the dependent status of adult children who are students, such as requiring full time enrollment, or may recognize domestic partners as dependents.  Under certain circumstances, the dependent may be under the indirect authority of a responsible party acting as a surrogate for the subscriber, for example, if the subscriber is differently abled or deceased, a guardian ad Lidem or estate executor may be appointed to assume the subscriberaTMs legal standing in the relationship with the dependent.";
            case INDIV: return "Description: A role played by a party covered under a policy as the policy holder.  An individual may be either a person or an organization.\r\n\n                        \n                           Note: The party playing the role of an individual insured is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However, a named insured may make a claim under a policy, e.g., a party that is the named insured and policy holder under a comprehensive automobile insurance policy may become the claimant for coverage under that policy if injured in an automobile accident and there is no liable third party.  In the case of an individual insured making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that an individual insured has filed a claim for a loss.\r\n\n                        \n                           Example: The individual insured under a comprehensive automobile, disability, or property and casualty policy that is the policy holder.";
            case SUBSCR: return "Description: A role played by a person covered under a policy based on association with a sponsor who is the policy holder, and whose association may provide for the eligibility of dependents for coverage.\r\n\n                        \n                           Discussion: The policy holder holds the contract with the policy or program underwriter.  The subscriber holds a certificate of coverage under the contract.  In legal proceedings concerning the policy or program, the terms of the contract takes precedence over the terms of the certificate of coverage if there are any inconsistencies.\r\n\n                        \n                           Note: The party playing the role of a subscriber is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However, a subscriber may make a claim under a policy, e.g., a subscriber under a health insurance policy may become the claimant for coverage under that policy for wellness examines or if injured and there is no liable third party.  In the case of a subscriber making a claim, a role type code INSCLM (insured claimant) subtypes the class to indicate that the subscriber has filed a claim for services covered under the health insurance policy.\r\n\n                        \n                           Example: An employee or a member of an association.";
            case PROG: return "Description: A role played by a party that meets the eligibility criteria for coverage under a program.  A program eligible may be either a person, non-person living subject, or an organization, or a group of persons, non-person living subjects, or organizations.\r\n\n                        \n                           Discussion: A program as typically government administered coverage for parties determined eligible under the terms of the program.\r\n\n                        \n                           Note: The party playing a program eligible is not a claimant in the sense conveyed by the RoleClassCoveredParty CLAIM (claimant).  However a program eligible may make a claim under program, e.g., an unemployed worker may claim benefits under an unemployment insurance program, but parties playing these covered party role classes are not, for purposes of this vocabulary and in an effort to clearly distinguish role classes, considered claimants.\r\n\n                        In the case of a program eligible, a role type code INJWKR (injured worker) subtypes the class to indicate that the covered party in a workers compensation program is an injured worker, and as such, has filed a \"claim\" under the program for benefits.  Likewise, a covered role type code UNEMP (unemployed worker) subtypes the program eligible class to indicate that the covered party in an unemployment insurance program has filed a claim for unemployment benefits.\r\n\n                        \n                           Example: A party meeting eligibility criteria related to health or financial status, e.g., in the U.S., persons meeting health, demographic, or financial criteria established by state and federal law are eligible for Medicaid.";
            case CRINV: return "A role played by a provider, always a person, who has agency authority from a Clinical Research Sponsor to direct the conduct of a clinical research trial or study on behalf of the sponsor.";
            case CRSPNSR: return "A role played by an entity, usually an organization, that is the sponsor of a clinical research trial or study.  The sponsor commissions the study, bears the expenses, is responsible for satisfying all legal requirements concerning subject safety and privacy, and is generally responsible for collection, storage and analysis of the data generated during the trial.  No scoper is necessary, as a clinical research sponsor undertakes the role on its own authority and declaration. Clinical research sponsors are usually educational or other research organizations, government agencies or biopharmaceutical companies.";
            case EMP: return "A relationship between a person or organization and a person or organization formed for the purpose of exchanging work for compensation.  The purpose of the role is to identify the type of relationship the employee has to the employer, rather than the nature of the work actually performed.  (Contrast with AssignedEntity.)";
            case MIL: return "A role played by a member of a military service. Scoper is the military service (e.g. Army, Navy, Air Force, etc.) or, more specifically, the unit (e.g. Company C, 3rd Battalion, 4th Division, etc.)";
            case GUAR: return "A person or organization (player) that serves as a financial guarantor for another person or organization (scoper).";
            case INVSBJ: return "An entity that is the subject of an investigation. This role is scoped by the party responsible for the investigation.";
            case CASEBJ: return "A person, non-person living subject, or place that is the subject of an investigation related to a notifiable condition (health circumstance that is reportable within the applicable public health jurisdiction)";
            case RESBJ: return "Definition:Specifies the administrative functionality within a formal experimental design for which the ResearchSubject role was established.\r\n\n                        \n                           Examples: Screening - role is used for pre-enrollment evaluation portion of the design; enrolled - role is used for subjects admitted to the experimental portion of the design.";
            case LIC: return "A relationship in which the scoper certifies the player ( e. g. a medical care giver, a medical device or a provider organization) to perform certain activities that fall under the jurisdiction of the scoper (e.g., a health authority licensing healthcare providers, a medical quality authority certifying healthcare professionals).";
            case NOT: return "notary public";
            case PROV: return "An Entity (player) that is authorized to provide health care services by some authorizing agency (scoper).";
            case PAT: return "A Role of a LivingSubject (player) as an actual or potential recipient of health care services from a healthcare provider organization (scoper).\r\n\n                        \n                           Usage Note: Communication about relationships between patients and specific healthcare practitioners (people) is not done via scoper.  Instead this is generally done using the CareProvision act.  This allows linkage between patient and a particular healthcare practitioner role and also allows description of the type of care involved in the relationship.";
            case PAYEE: return "The role of an organization or individual designated to receive payment for a claim against a particular coverage. The scoping entity is the organization that is the submitter of the invoice in question.";
            case PAYOR: return "The role of an organization that undertakes to accept claims invoices, assess the coverage or payments due for those invoices and pay to the designated payees for those invoices.  This role may be either the underwriter or a third-party organization authorized by the underwriter.  The scoping entity is the organization that underwrites the claimed coverage.";
            case POLHOLD: return "A role played by a person or organization that holds an insurance policy.  The underwriter of that policy is the scoping entity.\r\n\n                        \n                           Discussion:The identifier of the policy is captured in 'Role.id' when the Role is a policy holder.\r\n\n                        A particular policy may cover several individuals one of whom may be, but need not be, the policy holder.  Thus the notion of covered party is a role that is distinct from that of the policy holder.";
            case QUAL: return "An entity (player) that has been recognized as having certain training/experience or other characteristics that would make said entity an appropriate performer for a certain activity. The scoper is an organization that educates or qualifies entities.";
            case SPNSR: return "A role played by an entity, usually an organization that is the sponsor of an insurance plan or a health program. A sponsor is the party that is ultimately accountable for the coverage by employment contract or by law.  A sponsor can be an employer, union, government agency, or association.  Fully insured sponsors establish the terms of the plan and contract with health insurance plans to assume the risk and to administer the plan.  Self-insured sponsors delegate coverage administration, but not risk, to third-party administrators.  Program sponsors designate services to be covered in accordance with statute.   Program sponsors may administer the coverage themselves, delegate coverage administration, but not risk to third-party administrators, or contract with health insurance plans to assume the risk and administrator a program. Sponsors qualify individuals who may become \r\n\n                        \n                           \n                              a policy holder of the plan;\r\n\n                           \n                           \n                              where the sponsor is the policy holder, who may become a subscriber or a dependent to a policy under the plan; or\r\n\n                           \n                           \n                              where the sponsor is a government agency, who may become program eligibles under a program. \r\n\n                           \n                        \n                        The sponsor role may be further qualified by the SponsorRole.code.  Entities playing the sponsor role may also play the role of a Coverage Administrator.\r\n\n                        \n                           Example: An employer, union, government agency, or association.";
            case STD: return "A role played by an individual who is a student of a school, which is the scoping entity.";
            case UNDWRT: return "A role played by a person or an organization.  It is the party that \r\n\n                        \n                           \n                              accepts fiscal responsibility for insurance plans and the policies created under those plans;\r\n\n                           \n                           \n                              administers and accepts fiscal responsibility for a program that provides coverage for services to eligible individuals; and/or\r\n\n                           \n                           \n                              has the responsibility to assess the merits of each risk and decide a suitable premium for accepting all or part of the risk.  If played by an organization, this role may be further specified by an appropriate RoleCode.\r\n\n                           \n                        \n                        \n                           Example:\n                        \r\n\n                        \n                           \n                              A health insurer; \r\n\n                           \n                           \n                              Medicaid Program;\r\n\n                           \n                           \n                              Lloyd's of London";
            case CAREGIVER: return "A person responsible for the primary care of a patient at home.";
            case PRS: return "Links two entities with classCode PSN (person) in a personal relationship. The character of the relationship must be defined by a PersonalRelationshipRoleType code. The player and scoper are determined by PersonalRelationshipRoleType code as well.";
            case SELF: return "The \"same\" roleclass asserts an identity between playing and scoping entities: that they are in fact instances of the same entity and, in the case of discrepancies (e.g different DOB, gender), that one or both are in error.\r\n\n                        \n                           Usage:\n                        \r\n\n                        playing and scoping entities must have same classcode, but need not have identical attributes or values. \r\n\n                        \n                           Example: \n                        \r\n\n                        a provider registry maintains sets of conflicting demographic data for what is reported to be the same individual.";
            case _ROLECLASSPASSIVE: return "An association for a playing Entity that is used, known, treated, handled, built, or destroyed, etc. under the auspices of the scoping Entity. The playing Entity is passive in these roles (even though it may be active in other roles), in the sense that the kinds of things done to it in this role happen without an agreement from the playing Entity.";
            case ACCESS: return "A role in which the playing entity (material) provides access to another entity. The principal use case is intravenous (or other bodily) access lines that preexist and need to be referred to for medication routing instructions.";
            case ADJY: return "A physical association whereby two Entities are in some (even lose) spatial relationship with each other such that they touch each other in some way.\r\n\n                        \n                           Examples: the colon is connected (and therefore adjacent) to the jejunum; the colon is adjacent to the liver (even if not actually connected.)\r\n\n                        \n                           UsageConstraints: Adjacency is in principle a symmetrical connection, but scoper and player of the role should, where applicable, be assigned to have scoper be the larger, more central Entity and player the smaller, more distant, appendage.";
            case CONC: return "An adjacency of two Entities held together by a bond which attaches to each of the two entities. \r\n\n                        \n                           Examples: biceps brachii muscle connected to the radius bone, port 3 on a network switch connected to port 5 on a patch panel.\r\n\n                        \n                           UsageConstraints: See Adjacency for the assignment of scoper (larger, more central) and player (smaller, more distant).";
            case BOND: return "A connection between two atoms of a molecule.\r\n\n                        \n                           Examples: double bond between first and second C in ethane, peptide bond between two amino-acid, disulfide bridge between two proteins, chelate and ion associations, even the much weaker van-der-Waals bonds can be considered molecular bonds.\r\n\n                        \n                           UsageConstraints: See connection and adjacency for the assignment of player and scoper.";
            case CONY: return "A connection between two regional parts.\r\n\n                        \n                           Examples:  the connection between ascending aorta and the aortic arc, connection between descending colon and sigmoid.\r\n\n                        \n                           UsageConstraints: See connection and adjacency for the assignment of player and scoper.";
            case ADMM: return "A material (player) that can be administered to an Entity (scoper).";
            case BIRTHPL: return "Relates a place (playing Entity) as the location where a living subject (scoping Entity) was born.";
            case DEATHPLC: return "Definition: Relates a place (playing Entity) as the location where a living subject (scoping Entity) died.";
            case DST: return "A material (player) distributed by a distributor (scoper) who functions between a manufacturer and a buyer or retailer.";
            case RET: return "Material (player) sold by a retailer (scoper), who also give advice to prospective buyers.";
            case EXLOC: return "A role played by a place at which the location of an event may be recorded.";
            case SDLOC: return "A role played by a place at which services may be provided.";
            case DSDLOC: return "A role of a place (player) that is intended to house the provision of services. Scoper is the Entity (typically Organization) that provides these services. This is not synonymous with \"ownership.\"";
            case ISDLOC: return "A role played by a place at which health care services may be provided without prior designation or authorization.";
            case EXPR: return "A role played by an entity that has been exposed to a person or animal suffering a contagious disease, or with a location from which a toxin has been distributed.  The player of the role is normally a person or animal, but it is possible that other entity types could become exposed.  The role is scoped by the source of the exposure, and it is quite possible for a person playing the role of exposed party to also become the scoper a role played by another person.  That is to say, once a person has become infected, it is possible, perhaps likely, for that person to infect others.\r\n\n                        Management of exposures and tracking exposed parties is a key function within public health, and within most public health contexts - exposed parties are known as \"contacts.\"";
            case HLD: return "Entity that is currently in the possession of a holder (scoper), who holds, or uses it, usually based on some agreement with the owner.";
            case HLTHCHRT: return "The role of a material (player) that is the physical health chart belonging to an organization (scoper).";
            case IDENT: return "A role in which the scoping entity designates an identifier for a playing entity.";
            case MANU: return "Scoped by the manufacturer";
            case THER: return "A manufactured material (player) that is used for its therapeutic properties.  The manufacturer is the scoper.";
            case MNT: return "An entity (player) that is maintained by another entity (scoper).  This is typical role held by durable equipment. The scoper assumes responsibility for proper operation, quality, and safety.";
            case OWN: return "An Entity (player) for which someone (scoper) is granted by law the right to call the material (player) his own.  This entitles the scoper to make decisions about the disposition of that material.";
            case RGPR: return "A product regulated by some governmentatl orgnization.  The role is played by Material and scoped by Organization.\r\n\n                        Rationale: To support an entity clone used to identify the NDC number for a drug product.";
            case TERR: return "Relates a place entity (player) as the region over which the scoper (typically an Organization) has certain authority (jurisdiction). For example, the Calgary Regional Health Authority (scoper) has authority over the territory \"Region 4 of Alberta\" (player) in matters of health.";
            case USED: return "Description:An entity (player) that is used by another entity (scoper)";
            case WRTE: return "A role a product plays when a guarantee is given to the purchaser by the seller (scoping entity) stating that the product is reliable and free from known defects and that the seller will repair or replace defective parts within a given time limit and under certain conditions.";
            case _ROLECLASSONTOLOGICAL: return "A relationship in which the scoping Entity defines or specifies what the playing Entity is.  Thus, the player's \"being\" (Greek: ontos) is specified.";
            case EQUIV: return "Description: Specifies the player Entity (the equivalent Entity) as an Entity that is considered to be equivalent to a reference Entity (scoper).  The equivalence is in principle a symmetric relationship, however, it is expected that the scoper is a reference entity which serves as reference entity for multiple different equivalent entities. \r\n\n                        \n                           Examples: An innovator's medicine formulation is the reference for \"generics\", i.e., formulations manufactured differently but having been proven to be biologically equivalent to the reference medicine. Another example is a reference ingredient that serves as basis for quantity specifications (basis of strength, e.g., metoprolol succinate specified in terms of metoprolol tartrate.)";
            case SAME: return "The \"same\" role asserts an identity between playing and scoping entities, i.e., that they are in fact two records of the same entity instance, and, in the case of discrepancies (e.g different DOB, gender), that one or both are in error.\r\n\n                        \n                           Usage:\n                        \r\n\n                        playing and scoping entities must have same classCode, but need not have identical attributes or values.\r\n\n                        \n                           Example: \n                        \r\n\n                        a provider registry maintains sets of conflicting demographic data for what is reported to be the same individual.";
            case SUBY: return "Relates a prevailing record of an Entity (scoper) with another record (player) that it subsumes.\r\n\n                        \n                           Examples: Show a correct new Person object (scoper) that subsumes one or more duplicate Person objects that had accidentally been created for the same physical person.\r\n\n                        \n                           Constraints: Both the player and scoper must have the same classCode.";
            case GEN: return "Relates a specialized material concept (player) to its generalization (scoper).";
            case GRIC: return "A special link between pharmaceuticals indicating that the target (scoper) is a generic for the source (player).";
            case INST: return "An individual piece of material (player) instantiating a class of material (scoper).";
            case SUBS: return "An entity that subsumes the identity of another.  Used in the context of merging documented entity instances. Both the player and scoper must have the same classCode.\r\n\n                        The use of this code is deprecated in favor of the term SUBY which is its inverse and is more ontologically correct.";
            case _ROLECLASSPARTITIVE: return "An association between two Entities where the playing Entity is considered in some way \"part\" of the scoping Entity, e.g., as a member, component, ingredient, or content. Being \"part\" in the broadest sense of the word can mean anything from being an integral structural component to a mere incidental temporary association of a playing Entity with a (generally larger) scoping Entity.";
            case CONT: return "Relates a material as the content (player) to a container (scoper).  Unlike ingredients, the content and a container remain separate (not mixed) and the content can be removed from the container.  A content is not part of an empty container.";
            case EXPAGTCAR: return "An exposure agent carrier is an entity that is capable of conveying an exposure agent from one entity to another.  The scoper of the role must be the exposure agent (e.g., pathogen).";
            case EXPVECTOR: return "Description: A vector is a living subject that carries an exposure agent.  The vector does not cause the disease itself, but exposes targets to the exposure agent.  A mosquito carrying malaria is an example of a vector.  The scoper of the role must be the exposure agent (e.g., pathogen).";
            case FOMITE: return "Description: A fomite is a non-living entity that is capable of conveying exposure agent from one entity to another.  A doorknob contaminated with a Norovirus is an example of a fomite.  Anyone touching the doorknob would be exposed to the virus.  The scoper of the role must be the exposure agent (e.g., pathogen).";
            case INGR: return "Relates a component (player) to a mixture (scoper). E.g., Glucose and Water are ingredients of D5W, latex may be an ingredient in a tracheal tube.";
            case ACTI: return "Definition: a therapeutically active ingredient (player) in a mixture (scoper), where the mixture is typically a manufactured pharmaceutical.  It is unknown if the quantity of such an ingredient is expressed precisely in terms of the playing ingredient substance, or, if it is specified in terms of a closely related substance (active moiety or reference substance).";
            case ACTIB: return "Description:  Active ingredient, where the ingredient substance (player) is itself the \"basis of strength\", i.e., where the Role.quantity specifies exactly the quantity of the player substance in the medicine formulation. \r\n\n                        \n                           Examples: Lopressor 50 mg actually contains 50 mg of metoprolol succinate, even though the active moiety is metoprolol,  but also: Tenormin 50 mg contain 50 mg of atenolol, as free base, i.e., where the active ingredient atenolol is also the active moiety.";
            case ACTIM: return "Description: Active ingredient, where not the ingredient substance (player), but itaTMs active moiety is the \"basis of strength\", i.e., where the Role.quantity specifies the quantity of the player substance's active moiety in the medicine formulation.\r\n\n                        \n                           Examples: 1 mL of Betopic 5mg/mL eye drops contains 5.6 mg betaxolol hydrochloride equivalent to betaxolol base 5 mg.";
            case ACTIR: return "Description: Active ingredient, where not the ingredient substance (player) but another reference substance with the same active moiety, is the \"basis of strength\", i.e., where the Role.quantity specifies the quantity of a reference substance, similar but different from the player substance's in the medicine formulation.\r\n\n                        \n                           Examples: Toprol-XL 50 mg contains 47.5 mg of metoprolol succinate equivalent to 50 mg of metoprolol tartrate.";
            case ADJV: return "A component (player) added to enhance the action of an active ingredient (scoper) (in the manner of a catalyst) but which has no active effect in and of itself.  Such ingredients are significant in defining equivalence of products in a way that inactive ingredients are not.";
            case ADTV: return "An ingredient (player)  that is added to a base (scoper), that amounts to a minor part of the overall mixture.";
            case BASE: return "A base ingredient (player) is what comprises the major part of a mixture (scoper). E.g., Water in most i.v. solutions, or Vaseline in salves. Among all ingredients of a material, there should be only one base. A base substance can, in turn, be a mixture.";
            case CNTM: return "An ingredient whose presence is not intended but may not be reasonably avoided given the circumstances of the mixture's nature or origin.";
            case IACT: return "An ingredient which is not considered therapeutically active, e.g., colors, flavors, stabilizers, or preservatives, fillers, or structural components added to an active ingredient in order to facilitate administration of the active ingredient but without being considered therapeutically active. An inactive ingredient need not be biologically inert, e.g., might be active as an allergen or might have a pleasant taste, but is not an essential constituent delivering the therapeutic effect.";
            case COLR: return "A substance (player) influencing the optical aspect of material (scoper).";
            case FLVR: return "A substance (player) added to a mixture (scoper) to make it taste a certain way.  In food the use is obvious, in pharmaceuticals flavors can hide disgusting taste of the active ingredient (important in pediatric treatments).";
            case PRSV: return "A substance (player) added to a mixture (scoper) to prevent microorganisms (fungi, bacteria) to spoil the mixture.";
            case STBL: return "A stabilizer (player) added to a mixture (scoper) in order to prevent the molecular disintegration of the main substance.";
            case MECH: return "An ingredient (player) of a medication (scoper) that is inseparable from the active ingredients, but has no intended chemical or pharmaceutical effect itself, but which may have some systemic effect on the patient.\r\n\n                        An example is a collagen matrix used as a base for transplanting skin cells.  The collagen matrix can be left permanently in the graft site.  Because it is of bovine origin, the patient may exhibit allergies or may have cultural objections to its use.";
            case LOCE: return "Relates an entity (player) to a location (scoper) at which it is present in some way. This presence may be limited in time.";
            case STOR: return "Relates an entity (player) (e.g. a device) to a location (scoper) at which it is normally found or stored when not used.";
            case MBR: return "A role played by an entity that is a member of a group.  The group provides the scope for this role.\r\n\n                        Among other uses, groups as used in insurance (groups of covered individuals) and in scheduling where resources may be grouped for scheduling and logistical purposes.";
            case PART: return "Definition:  an association between two Entities where the playing Entity (the part) is a component of the whole (scoper) in the sense of an integral structural component, that is distinct from other parts in the same whole, has a distinct function in the whole, and, as an effect, the full integrity of the whole depends (to some degree) on the presence of this part, even though the part may often be separable from the whole.\r\n\n                        \n                           Discussion: Part is defined in opposition to (a) ingredient (not separable), (b) content (not a functional component), and (c) member (not functionally distinct from other members).";
            case ACTM: return "The molecule or ion that is responsible for the intended pharmacological action of the drug substance, excluding those appended or associated parts of the molecule that make the molecule an ester, salt (including a salt with hydrogen or coordination bonds), or other noncovalent derivative (such as a complex, chelate, or clathrate).\r\n\n                        Examples: heparin-sodium and heparin-potassium have the same active moiety, heparin; the active moiety of morphine-hydrochloride is morphine.";
            case SPEC: return "A role played by a material entity that is a specimen for an act. It is scoped by the source of the specimen.";
            case ALQT: return "A portion (player) of an original or source specimen (scoper) used for testing or transportation.";
            case ISLT: return "A microorganism that has been isolated from other microorganisms or a source matrix.";
            case CHILD: return "The player of the role is a child of the scoping entity, in a generic sense.";
            case CRED: return "A role played by an entity that receives credentials from the scoping entity.";
            case NURPRAC: return "nurse practitioner";
            case NURS: return "nurse";
            case PA: return "physician assistant";
            case PHYS: return "physician";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case ROL: return "role";
            case _ROLECLASSASSOCIATIVE: return "RoleClassAssociative";
            case _ROLECLASSMUTUALRELATIONSHIP: return "RoleClassMutualRelationship";
            case _ROLECLASSRELATIONSHIPFORMAL: return "RoleClassRelationshipFormal";
            case AFFL: return "affiliate";
            case AGNT: return "agent";
            case ASSIGNED: return "assigned entity";
            case COMPAR: return "commissioning party";
            case SGNOFF: return "signing authority or officer";
            case CON: return "contact";
            case ECON: return "emergency contact";
            case NOK: return "next of kin";
            case GUARD: return "guardian";
            case CIT: return "citizen";
            case COVPTY: return "covered party";
            case CLAIM: return "claimant";
            case NAMED: return "named insured";
            case DEPEN: return "dependent";
            case INDIV: return "individual";
            case SUBSCR: return "subscriber";
            case PROG: return "program eligible";
            case CRINV: return "clinical research investigator";
            case CRSPNSR: return "clinical research sponsor";
            case EMP: return "employee";
            case MIL: return "military person";
            case GUAR: return "guarantor";
            case INVSBJ: return "Investigation Subject";
            case CASEBJ: return "Case Subject";
            case RESBJ: return "research subject";
            case LIC: return "licensed entity";
            case NOT: return "notary public";
            case PROV: return "healthcare provider";
            case PAT: return "patient";
            case PAYEE: return "payee";
            case PAYOR: return "invoice payor";
            case POLHOLD: return "policy holder";
            case QUAL: return "qualified entity";
            case SPNSR: return "coverage sponsor";
            case STD: return "student";
            case UNDWRT: return "underwriter";
            case CAREGIVER: return "caregiver";
            case PRS: return "personal relationship";
            case SELF: return "self";
            case _ROLECLASSPASSIVE: return "RoleClassPassive";
            case ACCESS: return "access";
            case ADJY: return "adjacency";
            case CONC: return "connection";
            case BOND: return "molecular bond";
            case CONY: return "continuity";
            case ADMM: return "Administerable Material";
            case BIRTHPL: return "birthplace";
            case DEATHPLC: return "place of death";
            case DST: return "distributed material";
            case RET: return "retailed material";
            case EXLOC: return "event location";
            case SDLOC: return "service delivery location";
            case DSDLOC: return "dedicated service delivery location";
            case ISDLOC: return "incidental service delivery location";
            case EXPR: return "exposed entity";
            case HLD: return "held entity";
            case HLTHCHRT: return "health chart";
            case IDENT: return "identified entity";
            case MANU: return "manufactured product";
            case THER: return "therapeutic agent";
            case MNT: return "maintained entity";
            case OWN: return "owned entity";
            case RGPR: return "regulated product";
            case TERR: return "territory of authority";
            case USED: return "used entity";
            case WRTE: return "warranted product";
            case _ROLECLASSONTOLOGICAL: return "RoleClassOntological";
            case EQUIV: return "equivalent entity";
            case SAME: return "same";
            case SUBY: return "subsumed by";
            case GEN: return "has generalization";
            case GRIC: return "has generic";
            case INST: return "instance";
            case SUBS: return "subsumer";
            case _ROLECLASSPARTITIVE: return "RoleClassPartitive";
            case CONT: return "content";
            case EXPAGTCAR: return "exposure agent carrier";
            case EXPVECTOR: return "exposure vector";
            case FOMITE: return "fomite";
            case INGR: return "ingredient";
            case ACTI: return "active ingredient";
            case ACTIB: return "active ingredient - basis of strength";
            case ACTIM: return "active ingredient - moiety is basis of strength";
            case ACTIR: return "active ingredient - reference substance is basis of strength";
            case ADJV: return "adjuvant";
            case ADTV: return "additive";
            case BASE: return "base";
            case CNTM: return "contaminant ingredient";
            case IACT: return "inactive ingredient";
            case COLR: return "color additive";
            case FLVR: return "flavor additive";
            case PRSV: return "preservative";
            case STBL: return "stabilizer";
            case MECH: return "mechanical ingredient";
            case LOCE: return "located entity";
            case STOR: return "stored entity";
            case MBR: return "member";
            case PART: return "part";
            case ACTM: return "active moiety";
            case SPEC: return "specimen";
            case ALQT: return "aliquot";
            case ISLT: return "isolate";
            case CHILD: return "child";
            case CRED: return "credentialed entity";
            case NURPRAC: return "nurse practitioner";
            case NURS: return "nurse";
            case PA: return "physician assistant";
            case PHYS: return "physician";
            default: return "?";
          }
    }


}

