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


public enum V3RoleCode {

        /**
         * Concepts characterizing the type of association formed by player and scoper when there is a recognized Affiliate role by which the two parties are related.

                        
                           Examples: Business Partner, Business Associate, Colleague
         */
        _AFFILIATIONROLETYPE, 
        /**
         * Description:Codes that indicate a specific type of sponsor.  Used when the sponsor's role is only either as a fully insured sponsor or only as a self-insured sponsor.  NOTE: Where a sponsor may be either, use the SponsorParticipationFunction.code (fully insured or self insured) to indicate the type of responsibility. (CO6-0057)
         */
        _COVERAGESPONSORROLETYPE, 
        /**
         * Description:An employer or organization that contracts with an underwriter to assumes the financial risk and administrative responsibility for coverage of health services for covered parties.
         */
        FULLINS, 
        /**
         * Description:An employer or organization that assumes the financial risk and administrative responsibility for coverage of health services for covered parties.
         */
        SELFINS, 
        /**
         * Description:PayorRoleType for a particular type of policy or program benefit package or plan where more detail about the coverage administration role of the Payor is required.  The functions performed by a Payor qualified by a PayorRoleType may be specified by the PayorParticpationFunction value set.

                        
                           Examples:A Payor that is a TPA may administer a managed care plan without underwriting the risk.
         */
        _PAYORROLETYPE, 
        /**
         * Description:A payor that is responsible for functions related to the enrollment of covered parties.
         */
        ENROLBKR, 
        /**
         * Description:Third party administrator (TPA) is a payor organization that processes health care claims without carrying insurance risk. Third party administrators are prominent players in the managed care industry and have the expertise and capability to administer all or a portion of the claims process. They are normally contracted by a health insurer or self-insuring companies to administer services, including claims administration, premium collection, enrollment and other administrative activities.

                        Self-insured employers often contract with third party administrator to handle their insurance functions. Insurance companies oftentimes outsource the claims, utilization review or membership functions to a TPA. Sometimes TPAs only manage provider networks, only claims or only utilization review.

                        While some third-party administrators may operate as units of insurance companies, they are often independent. However, hospitals or provider organizations desiring to set up their own health plans will often outsource certain responsibilities to TPAs.  TPAs may perform one or several payor functions, specified by the PayorParticipationFunction value set, such as provider management, enrollment, utilization management, and fee for service claims adjudication management.
         */
        TPA, 
        /**
         * Description:A payor that is responsible for review and case management of health services covered under a policy or program.
         */
        UMO, 
        /**
         * The role played by a party who has legal responsibility for another party.
         */
        RESPRSN, 
        /**
         * The role played by a person acting as the estate executor for a deceased subscriber or policyholder who was the responsible party
         */
        EXCEST, 
        /**
         * The role played by a person appointed by the court to look out for the best interests of a minor child during the course of legal proceedings.
         */
        GUADLTM, 
        /**
         * The role played by a person or institution legally empowered with responsibility for the care of a ward.
         */
        GUARD, 
        /**
         * A relationship between two people in which one person authorizes another to act for him in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts.
         */
        POWATT, 
        /**
         * A relationship between two people in which one person authorizes another, usually a family member or relative, to act for him or her in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts that is often limited in the kinds of powers that can be assigned.  Unlike ordinary powers of attorney, durable powers can survive for long periods of time, and again, unlike standard powers of attorney, durable powers can continue after incompetency.
         */
        DPOWATT, 
        /**
         * A relationship between two people in which one person authorizes another to act for him or her in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts that continues (by its terms) to be effective even though the grantor has become mentally incompetent after signing the document.
         */
        HPOWATT, 
        /**
         * A relationship between two people in which one person authorizes another to act for him or her in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts that is often limited in the kinds of powers that can be assigned.
         */
        SPOWATT, 
        /**
         * AssignedRoleType
         */
        _ASSIGNEDROLETYPE, 
        /**
         * Description:A role type that is used to further qualify a non-person subject playing a role where the role class attribute is set to RoleClass AssignedEntity
         */
        _ASSIGNEDNONPERSONLIVINGSUBJECTROLETYPE, 
        /**
         * Description:Dogs trained to assist the ill or physically challenged.
         */
        ASSIST, 
        /**
         * Description:Animals, including fish and insects, and microorganisms which may participate as assigned entities in biotherapies.
         */
        BIOTH, 
        /**
         * Description:Non-person living subject used as antibiotic.

                        
                           Examples:Bacteriophage, is a virus that infects bacteria.
         */
        ANTIBIOT, 
        /**
         * Description:Maggots raised for biodebridement.

                        
                           Discussion: Maggot Debridement Therapy is the medical use of live maggots for cleaning non-healing wounds.

                        
                           Examples:Removal of burnt skin.
         */
        DEBR, 
        /**
         * Description:Companion animals, such as dogs, cats, and rabbits, which may be provided to patients to improve general mood, decrease depression and loneliness, and distract from stress-inducing concerns to improve quality of life.
         */
        CCO, 
        /**
         * Description:Dogs trained to assist persons who are seeing impaired or blind.
         */
        SEE, 
        /**
         * Description:Dogs trained or having the ability to detect imminent seizures or cancers in humans, probably as a result of volatile chemical (odors) given off by the malignancy of the host.
         */
        SNIFF, 
        /**
         * A role type used to qualify a person's legal status within a country or nation.
         */
        _CITIZENROLETYPE, 
        /**
         * A person who has fled his or her home country to find a safe place elsewhere.
         */
        CAS, 
        /**
         * A person who is someone of below legal age who has fled his or her home country, without his or her parents, to find a safe place elsewhere at time of categorization.
         */
        CASM, 
        /**
         * A person who is legally recognized as a member of a nation or country, with associated rights and obligations.
         */
        CN, 
        /**
         * A foreigner who is present in a country (which is foreign to him/her) unlawfully or without the country's authorization (may be called an illegal alien).
         */
        CNRP, 
        /**
         * A person who is below legal age present in a country, without his or her parents, (which is foreign to him/her) unlawfully or without the country's authorization.
         */
        CNRPM, 
        /**
         * A non-country member admitted to the territory of a nation or country as a non-resident for an explicit purpose.
         */
        CPCA, 
        /**
         * A foreigner who is a resident of the country but does not have citizenship.
         */
        CRP, 
        /**
         * A person who is a resident below legal age of the country without his or her parents and does not have citizenship.
         */
        CRPM, 
        /**
         * Types of contact for Role code "CON"
         */
        _CONTACTROLETYPE, 
        /**
         * A contact role used for business and/or administrative purposes.
         */
        _ADMINISTRATIVECONTACTROLETYPE, 
        /**
         * A contact role used to identify a person within a Provider organization that can be contacted for billing purposes (e.g. about the content of the Invoice).
         */
        BILL, 
        /**
         * A contact for an organization for administrative purposes. Contact role specifies a person acting as a liason for the organization.

                        Example: HR Department representative.
         */
        ORG, 
        /**
         * A contact role used to identify a person within a Payor organization to whom this communication is addressed.
         */
        PAYOR, 
        /**
         * A contact designated for contact in emergent situations.
         */
        ECON, 
        /**
         * Played by an individual who is designated as the next of kin for another individual which scopes the role.
         */
        NOK, 
        /**
         * Definition: A code representing the type of identifier that has been assigned to the identified entity (IDENT).

                        
                           Examples: Example values include Social Insurance Number, Product Catalog ID, Product Model Number.
         */
        _IDENTIFIEDENTITYTYPE, 
        /**
         * Description:Describes types of identifiers other than the primary location registry identifier for a service delivery location.  Identifiers may be assigned by a local service delivery organization, a formal body capable of accrediting the location for the capability to provide specific services or the identifier may be assigned at a jurisdictional level.
         */
        _LOCATIONIDENTIFIEDENTITYROLECODE, 
        /**
         * Description:Identifier assigned to a  location by the organization responsible for accrediting the location.
         */
        ACHFID, 
        /**
         * Description:Identifier assigned to a location by a jurisdiction.
         */
        JURID, 
        /**
         * Description:Identifier assigned to a  location by a local party (which could be the facility itself or organization overseeing a group of facilities).
         */
        LOCHFID, 
        /**
         * Code indicating the primary use for which a living subject is bred or grown
         */
        _LIVINGSUBJECTPRODUCTIONCLASS, 
        /**
         * Cattle used for meat production
         */
        BF, 
        /**
         * Chickens raised for meat
         */
        BL, 
        /**
         * Breeding/genetic stock
         */
        BR, 
        /**
         * Companion animals
         */
        CO, 
        /**
         * Milk production
         */
        DA, 
        /**
         * Draft animals
         */
        DR, 
        /**
         * Dual purpose.  Defined purposes based on species and breed
         */
        DU, 
        /**
         * Animals raised for their fur, hair or skins
         */
        FI, 
        /**
         * Chickens raised for egg production
         */
        LY, 
        /**
         * Animals raised for meat production
         */
        MT, 
        /**
         * Poultry flocks used for chick/poult production
         */
        MU, 
        /**
         * Animals rasied for recreation
         */
        PL, 
        /**
         * Animals raised for racing perfomance
         */
        RC, 
        /**
         * Animals raised for shows
         */
        SH, 
        /**
         * Cattle raised for veal meat production.  Implicit is the husbandry method.
         */
        VL, 
        /**
         * Sheep, goats and other mammals raised for their fiber
         */
        WL, 
        /**
         * Animals used to perform work
         */
        WO, 
        /**
         * Identifies the specific hierarchical relationship between the playing and scoping medications. 

                        
                           Examples: Generic, Generic Formulation, Therapeutic Class, etc.
         */
        _MEDICATIONGENERALIZATIONROLETYPE, 
        /**
         * Description:A categorization of medicinal products by their therapeutic properties and/or main therapeutic use.
         */
        DC, 
        /**
         * Relates a manufactured drug product to the non-proprietary (generic) representation of its ingredients independent of strength, and form.

                        The scoping entity identifies a unique combination of medicine ingredients; sometimes referred to as "ingredient set".
         */
        GD, 
        /**
         * Relates a manufactured drug product to the non-proprietary (generic) representation of its ingredients and dose form, independent of strength of the ingredients. The scoping entity identifies a unique combination of medicine ingredients in a specific dose form.
         */
        GDF, 
        /**
         * Relates a manufactured drug product to the non-proprietary (generic) representation of is ingredients with their strength.  The scoping entity identifies a unique combination of medicine ingredients with their strength.
         */
        GDS, 
        /**
         * Relates a manufactured drug product to the non-proprietary (generic) representation of its ingredients with their strength in a specific dose form. The scoping entity identifies a unique combination of medicine ingredients with their strength in a single dose form.
         */
        GDSF, 
        /**
         * Relates a manufactured drug product to the non-proprietary (generic) representation of its ingredients with their strength in a specific dose form. The scoping entity identifies a unique combination of medicine ingredients with their strength in a single dose form.
         */
        MGDSF, 
        /**
         * Types of membership for Role code "MBR"
         */
        _MEMBERROLETYPE, 
        /**
         * A person who is a member of a tribe.
         */
        TRB, 
        /**
         * PersonalRelationshipRoleType
         */
        _PERSONALRELATIONSHIPROLETYPE, 
        /**
         * A relationship between two people characterizing their "familial" relationship
         */
        FAMMEMB, 
        /**
         * The player of the role is a child of the scoping entity.
         */
        CHILD, 
        /**
         * The player of the role is a child taken into a family through legal means and raised by the scoping person (parent) as his or her own child.
         */
        CHLDADOPT, 
        /**
         * The player of the role is a female child taken into a family through legal means and raised by the scoping person (parent) as his or her own child.
         */
        DAUADOPT, 
        /**
         * The player of the role is a male child taken into a family through legal means and raised by the scoping person (parent) as his or her own child.
         */
        SONADOPT, 
        /**
         * The player of the role is a child receiving parental care and nurture from the scoping person (parent) but not related to him or her through legal or blood ties.
         */
        CHLDFOST, 
        /**
         * The player of the role is a female child receiving parental care and nurture from the scoping person (parent) but not related to him or her through legal or blood ties.
         */
        DAUFOST, 
        /**
         * The player of the role is a male child receiving parental care and nurture from the scoping person (parent) but not related to him or her through legal or blood ties.
         */
        SONFOST, 
        /**
         * Description: The player of the role is a female child (of any type) of scoping entity (parent)
         */
        DAUC, 
        /**
         * The player of the role is a female offspring of the scoping entity (parent).
         */
        DAU, 
        /**
         * The player of the role is a daughter of the scoping person's spouse by a previous union.
         */
        STPDAU, 
        /**
         * The player of the role is an offspring of the scoping entity as determined by birth.
         */
        NCHILD, 
        /**
         * The player of the role is a male offspring of the scoping entity (parent).
         */
        SON, 
        /**
         * Description: The player of the role is a male child (of any type) of scoping entity (parent)
         */
        SONC, 
        /**
         * The player of the role is a son of the scoping person's spouse by a previous union.
         */
        STPSON, 
        /**
         * The player of the role is a child of the scoping person's spouse by a previous union.
         */
        STPCHLD, 
        /**
         * Description: A family member not having an immediate genetic or legal relationship e.g. Aunt, cousin, great grandparent, grandchild, grandparent, niece, nephew or uncle.
         */
        EXT, 
        /**
         * The player of the role is a sister of the scoping person's mother or father.
         */
        AUNT, 
        /**
         * Description:The player of the role is a biological sister of the scoping person's biological mother.
         */
        MAUNT, 
        /**
         * Description:The player of the role is a biological sister of the scoping person's biological father.
         */
        PAUNT, 
        /**
         * The player of the role is a relative of the scoping person descended from a common ancestor, such as a 	grandparent, by two or more steps in a diverging line.
         */
        COUSN, 
        /**
         * Description:The player of the role is a biological relative of the scoping person descended from a common ancestor on the player's mother's side, such as a grandparent, by two or more steps in a diverging line.
         */
        MCOUSN, 
        /**
         * Description:The player of the role is a biological relative of the scoping person descended from a common ancestor on the player's father's side, such as a grandparent, by two or more steps in a diverging line.
         */
        PCOUSN, 
        /**
         * The player of the role is a parent of the scoping person's grandparent.
         */
        GGRPRN, 
        /**
         * The player of the role is the father of the scoping person's grandparent.
         */
        GGRFTH, 
        /**
         * Description:The player of the role is the biological father of the scoping person's biological mother's parent.
         */
        MGGRFTH, 
        /**
         * Description:The player of the role is the biological father of the scoping person's biological father's parent.
         */
        PGGRFTH, 
        /**
         * The player of the role is the mother of the scoping person's grandparent.
         */
        GGRMTH, 
        /**
         * Description:The player of the role is the biological mother of the scoping person's biological mother's parent.
         */
        MGGRMTH, 
        /**
         * Description:The player of the role is the biological mother of the scoping person's biological father's parent.
         */
        PGGRMTH, 
        /**
         * Description:The player of the role is a biological parent of the scoping person's biological mother's parent.
         */
        MGGRPRN, 
        /**
         * Description:The player of the role is a biological parent of the scoping person's biological father's parent.
         */
        PGGRPRN, 
        /**
         * The player of the role is a child of the scoping person's son or daughter.
         */
        GRNDCHILD, 
        /**
         * The player of the role is a daughter of the scoping person's son or daughter.
         */
        GRNDDAU, 
        /**
         * The player of the role is a son of the scoping person's son or daughter.
         */
        GRNDSON, 
        /**
         * The player of the role is a parent of the scoping person's mother or father.
         */
        GRPRN, 
        /**
         * The player of the role is the father of the scoping person's mother or father.
         */
        GRFTH, 
        /**
         * Description:The player of the role is the biological father of the scoping person's biological mother.
         */
        MGRFTH, 
        /**
         * Description:The player of the role is the biological father of the scoping person's biological father.
         */
        PGRFTH, 
        /**
         * The player of the role is the mother of the scoping person's mother or father.
         */
        GRMTH, 
        /**
         * Description:The player of the role is the biological mother of the scoping person's biological mother.
         */
        MGRMTH, 
        /**
         * Description:The player of the role is the biological mother of the scoping person's biological father.
         */
        PGRMTH, 
        /**
         * Description:The player of the role is the biological parent of the scoping person's biological mother.
         */
        MGRPRN, 
        /**
         * Description:The player of the role is the biological parent of the scoping person's biological father.
         */
        PGRPRN, 
        /**
         * A relationship between an individual and a member of their spousal partner's immediate family.
         */
        INLAW, 
        /**
         * The player of the role is the spouse of scoping person's child.
         */
        CHLDINLAW, 
        /**
         * The player of the role is the wife of scoping person's son.
         */
        DAUINLAW, 
        /**
         * The player of the role is the husband of scoping person's daughter.
         */
        SONINLAW, 
        /**
         * The player of the role is the parent of scoping person's husband or wife.
         */
        PRNINLAW, 
        /**
         * The player of the role is the father of the scoping person's husband or wife.
         */
        FTHINLAW, 
        /**
         * The player of the role is the mother of the scoping person's husband or wife.
         */
        MTHINLAW, 
        /**
         * The player of the role is: (1) a sibling of the scoping person's spouse, or (2) the spouse of the scoping person's sibling, or (3) the spouse of a sibling of the scoping person's spouse.
         */
        SIBINLAW, 
        /**
         * The player of the role is: (1) a brother of the scoping person's spouse, or (2) the husband of the scoping person's sister, or (3) the husband of a sister of the scoping person's spouse.
         */
        BROINLAW, 
        /**
         * The player of the role is: (1) a sister of the scoping person's spouse, or (2) the wife of the scoping person's brother, or (3) the wife of a brother of the scoping person's spouse.
         */
        SISINLAW, 
        /**
         * The player of the role is a child of scoping person's brother or sister or of the brother or sister of the 	scoping person's spouse.
         */
        NIENEPH, 
        /**
         * The player of the role is a son of the scoping person's brother or sister or of the brother or sister of the 	scoping person's spouse.
         */
        NEPHEW, 
        /**
         * The player of the role is a daughter of the scoping person's brother or sister or of the brother or sister of the 	scoping person's spouse.
         */
        NIECE, 
        /**
         * The player of the role is a brother of the scoping person's mother or father.
         */
        UNCLE, 
        /**
         * Description:The player of the role is a biological brother of the scoping person's biological mother.
         */
        MUNCLE, 
        /**
         * Description:The player of the role is a biological brother of the scoping person's biological father.
         */
        PUNCLE, 
        /**
         * The player of the role is one who begets, gives birth to, or nurtures and raises the scoping entity (child).
         */
        PRN, 
        /**
         * The player of the role (parent) has taken the scoper (child) into their family through legal means and raises them as his or her own child.
         */
        ADOPTP, 
        /**
         * The player of the role (father) is a male who has taken the scoper (child) into their family through legal means and raises them as his own child.
         */
        ADOPTF, 
        /**
         * The player of the role (father) is a female who has taken the scoper (child) into their family through legal means and raises them as her own child.
         */
        ADOPTM, 
        /**
         * The player of the role is a male who begets or raises or nurtures the scoping entity (child).
         */
        FTH, 
        /**
         * The player of the role (parent) who is a male state-certified caregiver responsible for the scoper (child) who has been placed in the parent's care. The placement of the child is usually arranged through the government or a social-service agency, and temporary.

                        The state, via a jurisdiction recognized child protection agency, stands as in loco parentis to the child, making all legal decisions while the foster parent is responsible for the day-to-day care of the specified child.
         */
        FTHFOST, 
        /**
         * The player of the role is a male who begets the scoping entity (child).
         */
        NFTH, 
        /**
         * Indicates the biologic male parent of a fetus.
         */
        NFTHF, 
        /**
         * The player of the role is the husband of scoping person's mother and not the scoping person's natural father.
         */
        STPFTH, 
        /**
         * The player of the role is a female who conceives, gives birth to, or raises and nurtures the scoping entity (child).
         */
        MTH, 
        /**
         * The player is a female whose womb carries the fetus of the scoper.  Generally used when the gestational mother and natural mother are not the same.
         */
        GESTM, 
        /**
         * The player of the role (parent) who is a female state-certified caregiver responsible for the scoper (child) who has been placed in the parent's care. The placement of the child is usually arranged through the government or a social-service agency, and temporary.

                        The state, via a jurisdiction recognized child protection agency, stands as in loco parentis to the child, making all legal decisions while the foster parent is responsible for the day-to-day care of the specified child.
         */
        MTHFOST, 
        /**
         * The player of the role is a female who conceives or gives birth to the scoping entity (child).
         */
        NMTH, 
        /**
         * The player is the biologic female parent of the scoping fetus.
         */
        NMTHF, 
        /**
         * The player of the role is the wife of scoping person's father and not the scoping person's natural mother.
         */
        STPMTH, 
        /**
         * natural parent
         */
        NPRN, 
        /**
         * The player of the role (parent) who is a state-certified caregiver responsible for the scoper (child) who has been placed in the parent's care. The placement of the child is usually arranged through the government or a social-service agency, and temporary.

                        The state, via a jurisdiction recognized child protection agency, stands as in loco parentis to the child, making all legal decisions while the foster parent is responsible for the day-to-day care of the specified child.
         */
        PRNFOST, 
        /**
         * The player of the role is the spouse of the scoping person's parent and not the scoping person's natural parent.
         */
        STPPRN, 
        /**
         * The player of the role shares one or both parents in common with the scoping entity.
         */
        SIB, 
        /**
         * The player of the role is a male sharing one or both parents in common with the scoping entity.
         */
        BRO, 
        /**
         * The player of the role is a male related to the scoping entity by sharing only one biological parent.
         */
        HBRO, 
        /**
         * The player of the role is a male having the same biological parents as the scoping entity.
         */
        NBRO, 
        /**
         * The scoper was carried in the same womb as the male player and shares common biological parents.
         */
        TWINBRO, 
        /**
         * The scoper was carried in the same womb as the male player and shares common biological parents but is the product of a distinct egg/sperm pair.
         */
        FTWINBRO, 
        /**
         * The male scoper is an offspring of the same egg-sperm pair as the male player.
         */
        ITWINBRO, 
        /**
         * The player of the role is a son of the scoping person's stepparent.
         */
        STPBRO, 
        /**
         * The player of the role is related to the scoping entity by sharing only one biological parent.
         */
        HSIB, 
        /**
         * The player of the role is a female related to the scoping entity by sharing only one biological parent.
         */
        HSIS, 
        /**
         * The player of the role has both biological parents in common with the scoping entity.
         */
        NSIB, 
        /**
         * The player of the role is a female having the same biological parents as the scoping entity.
         */
        NSIS, 
        /**
         * The scoper was carried in the same womb as the female player and shares common biological parents.
         */
        TWINSIS, 
        /**
         * The scoper was carried in the same womb as the female player and shares common biological parents but is the product of a distinct egg/sperm pair.
         */
        FTWINSIS, 
        /**
         * The female scoper is an offspring of the same egg-sperm pair as the female player.
         */
        ITWINSIS, 
        /**
         * The scoper and player were carried in the same womb and shared common biological parents.
         */
        TWIN, 
        /**
         * The scoper and player were carried in the same womb and share common biological parents but are the product of distinct egg/sperm pairs.
         */
        FTWIN, 
        /**
         * The scoper and player are offspring of the same egg-sperm pair.
         */
        ITWIN, 
        /**
         * The player of the role is a female sharing one or both parents in common with the scoping entity.
         */
        SIS, 
        /**
         * The player of the role is a daughter of the scoping person's stepparent.
         */
        STPSIS, 
        /**
         * The player of the role is a child of the scoping person's stepparent.
         */
        STPSIB, 
        /**
         * A person who is important to one's well being; especially a spouse or one in a similar relationship.  (The player is the one who is important)
         */
        SIGOTHR, 
        /**
         * The player of the role cohabits with the scoping person but is not the scoping person's spouse.
         */
        DOMPART, 
        /**
         * Player of the role was previously joined to the scoping person in marriage and this marriage is now dissolved and inactive.

                        
                           Usage Note: This is significant to indicate as some jurisdictions have different legal requirements for former spouse to access the patient's record, from a general friend.
         */
        FMRSPS, 
        /**
         * The player of the role is a marriage partner of the scoping person.
         */
        SPS, 
        /**
         * The player of the role is a man joined to a woman (scoping person) in marriage.
         */
        HUSB, 
        /**
         * The player of the role is a woman joined to a man (scoping person) in marriage.
         */
        WIFE, 
        /**
         * The player of the role is a person who is known, liked, and trusted by the scoping person.
         */
        FRND, 
        /**
         * The player of the role lives near or next to the 	scoping person.
         */
        NBOR, 
        /**
         * The relationship that a person has with his or her self.
         */
        ONESELF, 
        /**
         * One who shares living quarters with the subject.
         */
        ROOM, 
        /**
         * Description: A role recognized through the eligibility of an identified party for benefits covered under an insurance policy or a program based on meeting eligibility criteria.

                        Eligibility as a covered party may be conditioned on the party meeting criteria to qualify for coverage under a policy or program, which may be mandated by law.  These criteria may be: 

                        
                           
                              The sole basis for coverage, e.g., being differently abled may qualify a person for disability coverage

                           
                           
                              May more fully qualify a covered party role e.g, being differently abled may qualify an adult child as a dependent

                           
                           
                              May impact the level of coverage for a covered party under a policy or program, e.g., being differently abled may qualify a program eligible for additional benefits.

                           
                        
                        
                           Discussion:  The Abstract Value Set "CoverageRoleType", which was developed for use in the Canadian realm "pre-coordinate" coverage roles with other roles that a covered party must play in order to be eligible for coverage, e.g., "handicapped dependent".   These role.codes may only be used with COVPTY to avoid overlapping concepts that would result from using them to specify the specializations of COVPTY, e.g., the role.class DEPEN should not be used with the role.code family dependent because that relationship has overlapping concepts due to the role.code precoodination and is conveyed in FICO with the personal relationship role that has a PART role link to the covered party role.  For the same reasons, the role.class DEPEN should not be used with the role.code HANDIC (handicapped dependent); the role.code DIFFABLE (differently abled) should be used instead.

                        In summary, the coded concepts in the Abstract Value Set "CoveredPartyRoleType" can be "post-coordinated" with the "RoleClassCoveredParty" Abstract Value Set.  Decoupling these concepts is intended to support an expansive range of covered party concepts and their semantic comparability.
         */
        _POLICYORPROGRAMCOVERAGEROLETYPE, 
        /**
         * Role recognized through the issuance of insurance coverage to an identified covered party who has this relationship with the policy holder such as the policy holder themselves (self), spouse, child, etc
         */
        _COVERAGEROLETYPE, 
        /**
         * The player of the role is dependent of the scoping entity.
         */
        FAMDEP, 
        /**
         * Covered party is a dependent of the policy holder with a physical or mental disability causing a disadvantage that makes independent achievement unusually difficult.
         */
        HANDIC, 
        /**
         * Covered party is an injured party with a legal claim for compensation against a policy holder under an insurance policy.
         */
        INJ, 
        /**
         * Covered party is the policy holder.  Also known as the subscriber.
         */
        SELF, 
        /**
         * Covered party is an individual that the policy holder has assumed responsibility for, such as foster child or legal ward.
         */
        SPON, 
        /**
         * Covered party to an insurance policy has coverage through full-time or part-time attendance at a recognized educational institution as defined by a particular insurance policy.
         */
        STUD, 
        /**
         * Covered party to an insurance policy has coverage through full-time attendance at a recognized educational institution as defined by a particular insurance policy.
         */
        FSTUD, 
        /**
         * Covered party to an insurance policy has coverage through part-time attendance at a recognized educational institution as defined by a particular insurance policy.
         */
        PSTUD, 
        /**
         * A role recognized through the eligibility of an identified living subject for benefits covered under an insurance policy or a program.  Eligibility as a covered party may be conditioned on a relationship with (1) the policy holder such as the policy holder who is covered as an individual under a poliy or as a party sponsored for coverage by the policy holder.

                        
                           Example:An employee as a subscriber; or (2) on being scoped another covered party such as the subscriber, as in the case of a dependent. 

                        
                           Discussion:  The Abstract Value Set "CoverageRoleType", which was developed for use in the Canadian realm "pre-coordinate" coverage roles with other roles that a covered party must play in order to be eligible for coverage, e.g., "handicapped dependent".  Other codes in the Abstract Value Set CoveredPartyRoleType domain can be "post-coordinated" with the EligiblePartyRoleType codes to denote comparable concepts.  Decoupling the concepts is intended to support a wider range of concepts and semantic comparability of coded concepts.
         */
        _COVEREDPARTYROLETYPE, 
        /**
         * DescriptionA role recognized through the eligibility of a party play a claimant for benefits covered or provided under an insurance policy.
         */
        _CLAIMANTCOVEREDPARTYROLETYPE, 
        /**
         * Description: A person playing the role of program eligible under a program based on allegations of being the victim of a crime.

                        
                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is "program eligible" and the person's status as a crime victim meets jurisdictional or program criteria.
         */
        CRIMEVIC, 
        /**
         * Description: A person playing the role of program eligible under a workers compensation program based on the filing of work-related injury claim.

                        
                           Discussion:  This CoveredPartyRoleType.code is used when the CoveredPartyRole class code is either "program eligible", a "named insured", and "individual insured",  or "dependent", and the person's status as differently abled or "handicapped" meets jurisdictional, policy, or program criteria.
         */
        INJWKR, 
        /**
         * Description: A role recognized through the eligibility of a party to play a dependent for benefits covered or provided under a health insurance policy because of an association with the subscriber that is recognized by the policy underwriter.
         */
        _DEPENDENTCOVEREDPARTYROLETYPE, 
        /**
         * Description: A person playing the role of an individual insured with continuity of coverage under a policy which is being terminated based on loss of original status that was the basis for coverage.  Criteria for qualifying for continuity of coverage may be set by law.

                        
                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either "program eligible" or "subscriber" and the person's status as a continuity of coverage beneficiary meets jurisdictional or policy criteria.
         */
        COCBEN, 
        /**
         * Description: A person playing the role of program eligible under a program based on meeting criteria for health or functional limitation set by law or by the program.

                        
                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either "program eligible", "named insured", "individual insured", or "dependent", and the person's status as differently abled meets jurisdictional, policy, or program criteria.
         */
        DIFFABL, 
        /**
         * Description: A person, who is a minor or is deemed incompetent, who plays the role of a program eligible where eligibility for coverage is based on meeting program eligibility criteria for status as a ward of a court or jurisdiction.

                        
                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is a "claimant", "program eligible", a "named insured", an "individual Insured" or a "dependent", and the person's status as a ward meets program or policy criteria. In the case of a ward covered under a program providing financial or health benefits, a governmental agency may take temporary custody of a minor or incompetent for his/her protection and care, e.g., if the ward is suffering from neglect or abuse, or has been in trouble with the law.
         */
        WARD, 
        /**
         * A role recognized through the eligibility of a party to play an individual insured for benefits covered or provided under an insurance policy where the party is also the policy holder.
         */
        _INDIVIDUALINSUREDPARTYROLETYPE, 
        /**
         * Description: A person playing the role of an individual insured under a policy based on meeting criteria for the employment status of retired set by law or the policy.

                        
                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either "program eligible" or "subscriber" and the person's status as a retiree meets jurisdictional or policy criteria.
         */
        RETIREE, 
        /**
         * Description:A role recognized through the eligibility of a party to play a program eligible for benefits covered or provided under a program.
         */
        _PROGRAMELIGIBLEPARTYROLETYPE, 
        /**
         * Description: A person playing the role of program eligible under a program based on aboriginal ancestry or as a member of an aboriginal community.

                        
                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is "program eligible" and the person's status as a member of an indigenous people meets jurisdictional or program criteria.
         */
        INDIG, 
        /**
         * Definition: A person playing the role of program eligible under a program based on military status.

                        
                           Discussion:  This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either "program eligible" or "subscriber" and the person's status as a member of the military meets jurisdictional or program criteria
         */
        MIL, 
        /**
         * Description: A person playing the role of program eligible under a program based on active military status.

                        
                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either "program eligible" or "subscriber" and the persons status as active duty military meets jurisdictional or program criteria.
         */
        ACTMIL, 
        /**
         * Description: A person playing the role of program eligible under a program based on retired military status.

                        
                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either "program eligible" or "subscriber" and the persons status as retired military meets jurisdictional or program criteria.
         */
        RETMIL, 
        /**
         * Description: A person playing the role of program eligible under a program based on status as a military veteran.

                        
                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either "program eligible" or "subscriber" and the persons status as a veteran meets jurisdictional or program criteria.
         */
        VET, 
        /**
         * Description: A role recognized through the eligibility of a party to play a subscriber for benefits covered or provided under a health insurance policy.
         */
        _SUBSCRIBERCOVEREDPARTYROLETYPE, 
        /**
         * Specifies the administrative functionality within a formal experimental design for which the ResearchSubject role was established.  Examples: screening - role is used for pre-enrollment evaluation portion of the design; enrolled - role is used for subjects admitted to the active treatment portion of the design.
         */
        _RESEARCHSUBJECTROLEBASIS, 
        /**
         * Definition:The specific role being played by a research subject participating in the active treatment or primary data collection portion of a research study.
         */
        ERL, 
        /**
         * Definition:The specific role being played by a research subject participating in the pre-enrollment evaluation portion of  a research study.
         */
        SCN, 
        /**
         * A role of a place that further classifies the setting (e.g., accident site, road side, work site, community location) in which services are delivered.
         */
        _SERVICEDELIVERYLOCATIONROLETYPE, 
        /**
         * A role of a place that further classifies a setting that is intended to house the provision of services.
         */
        _DEDICATEDSERVICEDELIVERYLOCATIONROLETYPE, 
        /**
         * A role of a place that further classifies the clinical setting (e.g., cardiology clinic, primary care clinic, rehabilitation hospital, skilled nursing facility) in which care is delivered during an encounter.
         */
        _DEDICATEDCLINICALLOCATIONROLETYPE, 
        /**
         * A practice setting where diagnostic procedures or therapeutic interventions are performed
         */
        DX, 
        /**
         * A practice setting where cardiovascular diagnostic procedures or therapeutic interventions are performed (e.g., cardiac catheterization lab, echocardiography suite)
         */
        CVDX, 
        /**
         * Cardiac catheterization lab
         */
        CATH, 
        /**
         * Echocardiography lab
         */
        ECHO, 
        /**
         * A practice setting where GI procedures (such as endoscopies) are performed
         */
        GIDX, 
        /**
         * (X12N 261QD0000N)
         */
        ENDOS, 
        /**
         * A practice setting where radiology services (diagnostic or therapeutic) are provided            (X12N 261QR0200N)
         */
        RADDX, 
        /**
         * (X12N 261QX0203N)
         */
        RADO, 
        /**
         * Neuroradiology unit
         */
        RNEU, 
        /**
         * An acute care institution that provides medical, surgical, or psychiatric care and treatment for the sick or the injured.
         */
        HOSP, 
        /**
         * (1) A hospital including a physical plant and personnel that provides multidisciplinary diagnosis and treatment for diseases that have one or more of the following characteristics: is permanent; leaves residual disability; is caused by nonreversible pathological alteration; requires special training of the patient for rehabilitation; and/or may be expected to require a long period of supervision or care. In addition, patients require the safety, security, and shelter of these specialized inpatient or partial hospitalization settings. (2) A hospital that provides medical and skilled nursing services to patients with long-term illnesses who are not in an acute phase but who require an intensity of services not available in nursing homes
         */
        CHR, 
        /**
         * (X12N 282N00000N)
         */
        GACH, 
        /**
         * A health care facility operated by the Department of Defense or other military operation.
         */
        MHSP, 
        /**
         * Healthcare facility that cares for patients with psychiatric illness(s).
         */
        PSYCHF, 
        /**
         * (X12N 283X00000N)
         */
        RH, 
        /**
         * Description: A location that plays the role of delivering services which may include life training and/or social support to people with addictions.
         */
        RHAT, 
        /**
         * Description: A location that plays the role of delivering services which may include adaptation, rehabilitation and social integration services for people with intellectual and/or pervasive development disorders such as autism or severe behaviour disorder.
         */
        RHII, 
        /**
         * Description: A location that plays the role of delivering services which may social support services for adolescents who are pregnant or have child and are experiencing adaptation issues/difficulties in their current or eventual parenting role.
         */
        RHMAD, 
        /**
         * Description: A location that plays the role of delivering services which may include adaptation, rehabilitation and social integration services for people with physical impairments.
         */
        RHPI, 
        /**
         * Description: A location that plays the role of delivering services for people with hearing impairments.
         */
        RHPIH, 
        /**
         * Description: A location that plays the role of delivering services for people with motor skill impairments.
         */
        RHPIMS, 
        /**
         * Description: A location that plays the role of delivering services for people with visual skill impairments.
         */
        RHPIVS, 
        /**
         * Description: A location that plays the role of delivering services which may include life training and/or social support services for the adaption, rehabilitation and social integration of youths with adjustment difficulties.
         */
        RHYAD, 
        /**
         * Hospital unit
         */
        HU, 
        /**
         * Bone marrow transplant unit
         */
        BMTU, 
        /**
         * Coronary care unit
         */
        CCU, 
        /**
         * A specialty unit in hospital that focuses on chronic respirator patients and pulmonary failure
         */
        CHEST, 
        /**
         * Epilepsy unit
         */
        EPIL, 
        /**
         * The section of a health care facility for providing rapid treatment to victims of sudden illness or trauma.
         */
        ER, 
        /**
         * Emergency trauma unit
         */
        ETU, 
        /**
         * Hemodialysis unit
         */
        HD, 
        /**
         * Description: A location that plays the role of delivering services which may include tests done based on clinical specimens to get health information about a patient as pertaining to the diagnosis, treatment and prevention of disease.  Hospital laboratories may be further divided into specialized units such as Anatomic Pathology, Microbiology, and Biochemistry.
         */
        HLAB, 
        /**
         * Description: A location that plays the role of delivering services which may include tests are done on clinical specimens to get health information about a patient pertaining to the diagnosis, treatment, and prevention of disease for a hospital visit longer than one day.
         */
        INLAB, 
        /**
         * Description: A location that plays the role of delivering services which may include tests are done on clinical specimens to get health information about a patient pertaining to the diagnosis, treatment, and prevention of disease for same day visits.
         */
        OUTLAB, 
        /**
         * Description: A location that plays the role of delivering services which may include the branch of medicine that uses ionizing and non-ionizing radiation to diagnose and treat diseases.  The radiology unit may be further divided into subspecialties such as Imaging, Cardiovascular, Thoracic, and Ultrasound.
         */
        HRAD, 
        /**
         * Description: A location that plays the role of delivering services which may include collecting specimens and/or samples from patients for laboratory testing purposes, but does not perform any tests or analysis functions.
         */
        HUSCS, 
        /**
         * Intensive care unit
         */
        ICU, 
        /**
         * Pediatric intensive care unit
         */
        PEDICU, 
        /**
         * Pediatric neonatal intensive care unit
         */
        PEDNICU, 
        /**
         * Description: A location that plays the role of delivering services which may include providing judicious, safe, efficacious, appropriate and cost effective use of medicines for treatment of patients for visits longer than one day. The distinction between inpatient pharmacies and retail (or outpatient) pharmacies is that they are part of a patient's continuity of care while staying in the hospital.
         */
        INPHARM, 
        /**
         * Description: A location that plays the role of delivering services which include biochemistry, hematology, microbiology, immunochemistry, and toxicology.
         */
        MBL, 
        /**
         * Neurology critical care and stroke unit
         */
        NCCS, 
        /**
         * Neurosurgery unit
         */
        NS, 
        /**
         * Description: A location that plays the role of delivering services which may include providing judicious, safe, efficacious, appropriate and cost effective use of medicines for treatment of patients for outpatient visits and may also be used for discharge prescriptions.
         */
        OUTPHARM, 
        /**
         * Pediatric unit
         */
        PEDU, 
        /**
         * (X12N 273R00000N)
         */
        PHU, 
        /**
         * Rehabilitation hospital unit
         */
        RHU, 
        /**
         * (X12N 261QA1200N)
         */
        SLEEP, 
        /**
         * Nursing or custodial care facility
         */
        NCCF, 
        /**
         * (X12N 314000000N)
         */
        SNF, 
        /**
         * Outpatient facility
         */
        OF, 
        /**
         * Allergy clinic
         */
        ALL, 
        /**
         * Amputee clinic
         */
        AMPUT, 
        /**
         * Bone marrow transplant clinic
         */
        BMTC, 
        /**
         * Breast clinic
         */
        BREAST, 
        /**
         * Child and adolescent neurology clinic
         */
        CANC, 
        /**
         * Child and adolescent psychiatry clinic
         */
        CAPC, 
        /**
         * Ambulatory Health Care Facilities; Clinic/Center; Rehabilitation: Cardiac Facilities
         */
        CARD, 
        /**
         * Pediatric cardiology clinic
         */
        PEDCARD, 
        /**
         * Coagulation clinic
         */
        COAG, 
        /**
         * Colon and rectal surgery clinic
         */
        CRS, 
        /**
         * Dermatology clinic
         */
        DERM, 
        /**
         * Endocrinology clinic
         */
        ENDO, 
        /**
         * Pediatric endocrinology clinic
         */
        PEDE, 
        /**
         * Otorhinolaryngology clinic
         */
        ENT, 
        /**
         * Family medicine clinic
         */
        FMC, 
        /**
         * Gastroenterology clinic
         */
        GI, 
        /**
         * Pediatric gastroenterology clinic
         */
        PEDGI, 
        /**
         * General internal medicine clinic
         */
        GIM, 
        /**
         * Gynecology clinic
         */
        GYN, 
        /**
         * Hematology clinic
         */
        HEM, 
        /**
         * Pediatric hematology clinic
         */
        PEDHEM, 
        /**
         * Hypertension clinic
         */
        HTN, 
        /**
         * Focuses on assessing disability
         */
        IEC, 
        /**
         * Infectious disease clinic
         */
        INFD, 
        /**
         * Pediatric infectious disease clinic
         */
        PEDID, 
        /**
         * Infertility clinic
         */
        INV, 
        /**
         * Lympedema clinic
         */
        LYMPH, 
        /**
         * Medical genetics clinic
         */
        MGEN, 
        /**
         * Nephrology clinic
         */
        NEPH, 
        /**
         * Pediatric nephrology clinic
         */
        PEDNEPH, 
        /**
         * Neurology clinic
         */
        NEUR, 
        /**
         * Obstetrics clinic
         */
        OB, 
        /**
         * Oral and maxillofacial surgery clinic
         */
        OMS, 
        /**
         * Medical oncology clinic
         */
        ONCL, 
        /**
         * Pediatric oncology clinic
         */
        PEDHO, 
        /**
         * Opthalmology clinic
         */
        OPH, 
        /**
         * Description: A location that plays the role of delivering services which may include examination, diagnosis, treatment, management, and prevention of diseases and disorders of the eye as well as prescribing and fitting appropriate corrective lenses (glasses or contact lenses) as needed.  Optometry clinics may also provide tests for visual field screening, measuring intra-ocular pressure and ophthalmoscopy, as and when required.
         */
        OPTC, 
        /**
         * Orthopedics clinic
         */
        ORTHO, 
        /**
         * Hand clinic
         */
        HAND, 
        /**
         * (X12N 261QP3300N)
         */
        PAINCL, 
        /**
         * (X12N 261QP2300N)
         */
        PC, 
        /**
         * Pediatrics clinic
         */
        PEDC, 
        /**
         * Pediatric rheumatology clinic
         */
        PEDRHEUM, 
        /**
         * (X12N 261QP1100N)
         */
        POD, 
        /**
         * Preventive medicine clinic
         */
        PREV, 
        /**
         * Proctology clinic
         */
        PROCTO, 
        /**
         * Location where healthcare service was delivered, identified as the healthcare provider's practice office.
         */
        PROFF, 
        /**
         * Prosthodontics clinic
         */
        PROS, 
        /**
         * Psychology clinic
         */
        PSI, 
        /**
         * Psychiatry clinic
         */
        PSY, 
        /**
         * Rheumatology clinic
         */
        RHEUM, 
        /**
         * Sports medicine clinic
         */
        SPMED, 
        /**
         * Surgery clinic
         */
        SU, 
        /**
         * Plastic surgery clinic
         */
        PLS, 
        /**
         * Urology clinic
         */
        URO, 
        /**
         * Transplant clinic
         */
        TR, 
        /**
         * Travel and geographic medicine clinic
         */
        TRAVEL, 
        /**
         * Wound clinic
         */
        WND, 
        /**
         * Residential treatment facility
         */
        RTF, 
        /**
         * Pain rehabilitation center
         */
        PRC, 
        /**
         * (X12N 324500000N)
         */
        SURF, 
        /**
         * A role of a place that further classifies a setting that is intended to house the provision of non-clinical services.
         */
        _DEDICATEDNONCLINICALLOCATIONROLETYPE, 
        /**
         * Location address where medical supplies were transported to for use.
         */
        DADDR, 
        /**
         * Location (mobile) where healthcare service was delivered.
         */
        MOBL, 
        /**
         * Location (mobile) where healthcare service was delivered, identified specifically as an ambulance.
         */
        AMB, 
        /**
         * Location where healthcare service was delivered, identified as a pharmacy.
         */
        PHARM, 
        /**
         * IncidentalServiceDeliveryLocationRoleType
         */
        _INCIDENTALSERVICEDELIVERYLOCATIONROLETYPE, 
        /**
         * Location of an accident where healthcare service was delivered, such as a roadside.
         */
        ACC, 
        /**
         * Community location where healthcare is delivered.
         */
        COMM, 
        /**
         * Description: A location that plays the role of delivering services which may include providing front-line services to the population of a defined geographic area such as: healthcare services and social services, preventive or curative, rehabilitation or reintegration.
         */
        CSC, 
        /**
         * location where healthcare was delivered which is the residence of the Patient.
         */
        PTRES, 
        /**
         * Location where healthcare service was delivered, identified as a school or educational facility.
         */
        SCHOOL, 
        /**
         * Description: A location that plays the role of delivering services which may include: social emergency services required for a young person as required under any jurisdictional youth laws, child placement, and family mediation in the defined geographical area the SDL is responsible for. It may provide expertise in a judiciary setting on child custody, adoption and biological history research.
         */
        UPC, 
        /**
         * Location where healthcare service was delivered, identified as a work place.
         */
        WORK, 
        /**
         * SpecimenRoleType
         */
        _SPECIMENROLETYPE, 
        /**
         * A specimen used for initial calibration settings of an instrument
         */
        C, 
        /**
         * A set of patient samples in which the individuals of the group may or may not be identified.
         */
        G, 
        /**
         * Aliquots of individual specimens combined to form a single specimen representing all of the included individuals.
         */
        L, 
        /**
         * A specimen that has been collected from a patient.
         */
        P, 
        /**
         * A specimen specifically used to verify the sensitivity, specificity, accuracy or other perfomance parameter of a diagnostic test.
         */
        Q, 
        /**
         * Quality Control specimen submitted to the lab whose identity and composition is not known to the lab.
         */
        B, 
        /**
         * An electronically simulated QC specimen
         */
        E, 
        /**
         * Specimen used for testing proficiency of an organization performing testing (how does this differ from O?)
         */
        F, 
        /**
         * A specimen used for evaluation of operator proficiency (operator in what context?)
         */
        O, 
        /**
         * A specimen used for periodic calibration checks of instruments
         */
        V, 
        /**
         * A portion of an original patent sample that is tested at the same time as the original sample
         */
        R, 
        /**
         * A party that makes a claim for coverage under a policy.
         */
        CLAIM, 
        /**
         * Community Laboratory
         */
        COMMUNITYLABORATORY, 
        /**
         * An individual or organization that makes or gives a promise, assurance, pledge to pay or has paid the healthcare service provider.
         */
        GT, 
        /**
         * Home Health
         */
        HOMEHEALTH, 
        /**
         * Laboratory
         */
        LABORATORY, 
        /**
         * Pathologist
         */
        PATHOLOGIST, 
        /**
         * Policy holder for the insurance policy.
         */
        PH, 
        /**
         * Phlebotomist
         */
        PHLEBOTOMIST, 
        /**
         * A party that meets the eligibility criteria for coverage under a program.
         */
        PROG, 
        /**
         * The recipient for the service(s) and/or product(s) when they are not the covered party.
         */
        PT, 
        /**
         * Self
         */
        SUBJECT, 
        /**
         * Third Party
         */
        THIRDPARTY, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3RoleCode fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_AffiliationRoleType".equals(codeString))
          return _AFFILIATIONROLETYPE;
        if ("_CoverageSponsorRoleType".equals(codeString))
          return _COVERAGESPONSORROLETYPE;
        if ("FULLINS".equals(codeString))
          return FULLINS;
        if ("SELFINS".equals(codeString))
          return SELFINS;
        if ("_PayorRoleType".equals(codeString))
          return _PAYORROLETYPE;
        if ("ENROLBKR".equals(codeString))
          return ENROLBKR;
        if ("TPA".equals(codeString))
          return TPA;
        if ("UMO".equals(codeString))
          return UMO;
        if ("RESPRSN".equals(codeString))
          return RESPRSN;
        if ("EXCEST".equals(codeString))
          return EXCEST;
        if ("GUADLTM".equals(codeString))
          return GUADLTM;
        if ("GUARD".equals(codeString))
          return GUARD;
        if ("POWATT".equals(codeString))
          return POWATT;
        if ("DPOWATT".equals(codeString))
          return DPOWATT;
        if ("HPOWATT".equals(codeString))
          return HPOWATT;
        if ("SPOWATT".equals(codeString))
          return SPOWATT;
        if ("_AssignedRoleType".equals(codeString))
          return _ASSIGNEDROLETYPE;
        if ("_AssignedNonPersonLivingSubjectRoleType".equals(codeString))
          return _ASSIGNEDNONPERSONLIVINGSUBJECTROLETYPE;
        if ("ASSIST".equals(codeString))
          return ASSIST;
        if ("BIOTH".equals(codeString))
          return BIOTH;
        if ("ANTIBIOT".equals(codeString))
          return ANTIBIOT;
        if ("DEBR".equals(codeString))
          return DEBR;
        if ("CCO".equals(codeString))
          return CCO;
        if ("SEE".equals(codeString))
          return SEE;
        if ("SNIFF".equals(codeString))
          return SNIFF;
        if ("_CitizenRoleType".equals(codeString))
          return _CITIZENROLETYPE;
        if ("CAS".equals(codeString))
          return CAS;
        if ("CASM".equals(codeString))
          return CASM;
        if ("CN".equals(codeString))
          return CN;
        if ("CNRP".equals(codeString))
          return CNRP;
        if ("CNRPM".equals(codeString))
          return CNRPM;
        if ("CPCA".equals(codeString))
          return CPCA;
        if ("CRP".equals(codeString))
          return CRP;
        if ("CRPM".equals(codeString))
          return CRPM;
        if ("_ContactRoleType".equals(codeString))
          return _CONTACTROLETYPE;
        if ("_AdministrativeContactRoleType".equals(codeString))
          return _ADMINISTRATIVECONTACTROLETYPE;
        if ("BILL".equals(codeString))
          return BILL;
        if ("ORG".equals(codeString))
          return ORG;
        if ("PAYOR".equals(codeString))
          return PAYOR;
        if ("ECON".equals(codeString))
          return ECON;
        if ("NOK".equals(codeString))
          return NOK;
        if ("_IdentifiedEntityType".equals(codeString))
          return _IDENTIFIEDENTITYTYPE;
        if ("_LocationIdentifiedEntityRoleCode".equals(codeString))
          return _LOCATIONIDENTIFIEDENTITYROLECODE;
        if ("ACHFID".equals(codeString))
          return ACHFID;
        if ("JURID".equals(codeString))
          return JURID;
        if ("LOCHFID".equals(codeString))
          return LOCHFID;
        if ("_LivingSubjectProductionClass".equals(codeString))
          return _LIVINGSUBJECTPRODUCTIONCLASS;
        if ("BF".equals(codeString))
          return BF;
        if ("BL".equals(codeString))
          return BL;
        if ("BR".equals(codeString))
          return BR;
        if ("CO".equals(codeString))
          return CO;
        if ("DA".equals(codeString))
          return DA;
        if ("DR".equals(codeString))
          return DR;
        if ("DU".equals(codeString))
          return DU;
        if ("FI".equals(codeString))
          return FI;
        if ("LY".equals(codeString))
          return LY;
        if ("MT".equals(codeString))
          return MT;
        if ("MU".equals(codeString))
          return MU;
        if ("PL".equals(codeString))
          return PL;
        if ("RC".equals(codeString))
          return RC;
        if ("SH".equals(codeString))
          return SH;
        if ("VL".equals(codeString))
          return VL;
        if ("WL".equals(codeString))
          return WL;
        if ("WO".equals(codeString))
          return WO;
        if ("_MedicationGeneralizationRoleType".equals(codeString))
          return _MEDICATIONGENERALIZATIONROLETYPE;
        if ("DC".equals(codeString))
          return DC;
        if ("GD".equals(codeString))
          return GD;
        if ("GDF".equals(codeString))
          return GDF;
        if ("GDS".equals(codeString))
          return GDS;
        if ("GDSF".equals(codeString))
          return GDSF;
        if ("MGDSF".equals(codeString))
          return MGDSF;
        if ("_MemberRoleType".equals(codeString))
          return _MEMBERROLETYPE;
        if ("TRB".equals(codeString))
          return TRB;
        if ("_PersonalRelationshipRoleType".equals(codeString))
          return _PERSONALRELATIONSHIPROLETYPE;
        if ("FAMMEMB".equals(codeString))
          return FAMMEMB;
        if ("CHILD".equals(codeString))
          return CHILD;
        if ("CHLDADOPT".equals(codeString))
          return CHLDADOPT;
        if ("DAUADOPT".equals(codeString))
          return DAUADOPT;
        if ("SONADOPT".equals(codeString))
          return SONADOPT;
        if ("CHLDFOST".equals(codeString))
          return CHLDFOST;
        if ("DAUFOST".equals(codeString))
          return DAUFOST;
        if ("SONFOST".equals(codeString))
          return SONFOST;
        if ("DAUC".equals(codeString))
          return DAUC;
        if ("DAU".equals(codeString))
          return DAU;
        if ("STPDAU".equals(codeString))
          return STPDAU;
        if ("NCHILD".equals(codeString))
          return NCHILD;
        if ("SON".equals(codeString))
          return SON;
        if ("SONC".equals(codeString))
          return SONC;
        if ("STPSON".equals(codeString))
          return STPSON;
        if ("STPCHLD".equals(codeString))
          return STPCHLD;
        if ("EXT".equals(codeString))
          return EXT;
        if ("AUNT".equals(codeString))
          return AUNT;
        if ("MAUNT".equals(codeString))
          return MAUNT;
        if ("PAUNT".equals(codeString))
          return PAUNT;
        if ("COUSN".equals(codeString))
          return COUSN;
        if ("MCOUSN".equals(codeString))
          return MCOUSN;
        if ("PCOUSN".equals(codeString))
          return PCOUSN;
        if ("GGRPRN".equals(codeString))
          return GGRPRN;
        if ("GGRFTH".equals(codeString))
          return GGRFTH;
        if ("MGGRFTH".equals(codeString))
          return MGGRFTH;
        if ("PGGRFTH".equals(codeString))
          return PGGRFTH;
        if ("GGRMTH".equals(codeString))
          return GGRMTH;
        if ("MGGRMTH".equals(codeString))
          return MGGRMTH;
        if ("PGGRMTH".equals(codeString))
          return PGGRMTH;
        if ("MGGRPRN".equals(codeString))
          return MGGRPRN;
        if ("PGGRPRN".equals(codeString))
          return PGGRPRN;
        if ("GRNDCHILD".equals(codeString))
          return GRNDCHILD;
        if ("GRNDDAU".equals(codeString))
          return GRNDDAU;
        if ("GRNDSON".equals(codeString))
          return GRNDSON;
        if ("GRPRN".equals(codeString))
          return GRPRN;
        if ("GRFTH".equals(codeString))
          return GRFTH;
        if ("MGRFTH".equals(codeString))
          return MGRFTH;
        if ("PGRFTH".equals(codeString))
          return PGRFTH;
        if ("GRMTH".equals(codeString))
          return GRMTH;
        if ("MGRMTH".equals(codeString))
          return MGRMTH;
        if ("PGRMTH".equals(codeString))
          return PGRMTH;
        if ("MGRPRN".equals(codeString))
          return MGRPRN;
        if ("PGRPRN".equals(codeString))
          return PGRPRN;
        if ("INLAW".equals(codeString))
          return INLAW;
        if ("CHLDINLAW".equals(codeString))
          return CHLDINLAW;
        if ("DAUINLAW".equals(codeString))
          return DAUINLAW;
        if ("SONINLAW".equals(codeString))
          return SONINLAW;
        if ("PRNINLAW".equals(codeString))
          return PRNINLAW;
        if ("FTHINLAW".equals(codeString))
          return FTHINLAW;
        if ("MTHINLAW".equals(codeString))
          return MTHINLAW;
        if ("SIBINLAW".equals(codeString))
          return SIBINLAW;
        if ("BROINLAW".equals(codeString))
          return BROINLAW;
        if ("SISINLAW".equals(codeString))
          return SISINLAW;
        if ("NIENEPH".equals(codeString))
          return NIENEPH;
        if ("NEPHEW".equals(codeString))
          return NEPHEW;
        if ("NIECE".equals(codeString))
          return NIECE;
        if ("UNCLE".equals(codeString))
          return UNCLE;
        if ("MUNCLE".equals(codeString))
          return MUNCLE;
        if ("PUNCLE".equals(codeString))
          return PUNCLE;
        if ("PRN".equals(codeString))
          return PRN;
        if ("ADOPTP".equals(codeString))
          return ADOPTP;
        if ("ADOPTF".equals(codeString))
          return ADOPTF;
        if ("ADOPTM".equals(codeString))
          return ADOPTM;
        if ("FTH".equals(codeString))
          return FTH;
        if ("FTHFOST".equals(codeString))
          return FTHFOST;
        if ("NFTH".equals(codeString))
          return NFTH;
        if ("NFTHF".equals(codeString))
          return NFTHF;
        if ("STPFTH".equals(codeString))
          return STPFTH;
        if ("MTH".equals(codeString))
          return MTH;
        if ("GESTM".equals(codeString))
          return GESTM;
        if ("MTHFOST".equals(codeString))
          return MTHFOST;
        if ("NMTH".equals(codeString))
          return NMTH;
        if ("NMTHF".equals(codeString))
          return NMTHF;
        if ("STPMTH".equals(codeString))
          return STPMTH;
        if ("NPRN".equals(codeString))
          return NPRN;
        if ("PRNFOST".equals(codeString))
          return PRNFOST;
        if ("STPPRN".equals(codeString))
          return STPPRN;
        if ("SIB".equals(codeString))
          return SIB;
        if ("BRO".equals(codeString))
          return BRO;
        if ("HBRO".equals(codeString))
          return HBRO;
        if ("NBRO".equals(codeString))
          return NBRO;
        if ("TWINBRO".equals(codeString))
          return TWINBRO;
        if ("FTWINBRO".equals(codeString))
          return FTWINBRO;
        if ("ITWINBRO".equals(codeString))
          return ITWINBRO;
        if ("STPBRO".equals(codeString))
          return STPBRO;
        if ("HSIB".equals(codeString))
          return HSIB;
        if ("HSIS".equals(codeString))
          return HSIS;
        if ("NSIB".equals(codeString))
          return NSIB;
        if ("NSIS".equals(codeString))
          return NSIS;
        if ("TWINSIS".equals(codeString))
          return TWINSIS;
        if ("FTWINSIS".equals(codeString))
          return FTWINSIS;
        if ("ITWINSIS".equals(codeString))
          return ITWINSIS;
        if ("TWIN".equals(codeString))
          return TWIN;
        if ("FTWIN".equals(codeString))
          return FTWIN;
        if ("ITWIN".equals(codeString))
          return ITWIN;
        if ("SIS".equals(codeString))
          return SIS;
        if ("STPSIS".equals(codeString))
          return STPSIS;
        if ("STPSIB".equals(codeString))
          return STPSIB;
        if ("SIGOTHR".equals(codeString))
          return SIGOTHR;
        if ("DOMPART".equals(codeString))
          return DOMPART;
        if ("FMRSPS".equals(codeString))
          return FMRSPS;
        if ("SPS".equals(codeString))
          return SPS;
        if ("HUSB".equals(codeString))
          return HUSB;
        if ("WIFE".equals(codeString))
          return WIFE;
        if ("FRND".equals(codeString))
          return FRND;
        if ("NBOR".equals(codeString))
          return NBOR;
        if ("ONESELF".equals(codeString))
          return ONESELF;
        if ("ROOM".equals(codeString))
          return ROOM;
        if ("_PolicyOrProgramCoverageRoleType".equals(codeString))
          return _POLICYORPROGRAMCOVERAGEROLETYPE;
        if ("_CoverageRoleType".equals(codeString))
          return _COVERAGEROLETYPE;
        if ("FAMDEP".equals(codeString))
          return FAMDEP;
        if ("HANDIC".equals(codeString))
          return HANDIC;
        if ("INJ".equals(codeString))
          return INJ;
        if ("SELF".equals(codeString))
          return SELF;
        if ("SPON".equals(codeString))
          return SPON;
        if ("STUD".equals(codeString))
          return STUD;
        if ("FSTUD".equals(codeString))
          return FSTUD;
        if ("PSTUD".equals(codeString))
          return PSTUD;
        if ("_CoveredPartyRoleType".equals(codeString))
          return _COVEREDPARTYROLETYPE;
        if ("_ClaimantCoveredPartyRoleType".equals(codeString))
          return _CLAIMANTCOVEREDPARTYROLETYPE;
        if ("CRIMEVIC".equals(codeString))
          return CRIMEVIC;
        if ("INJWKR".equals(codeString))
          return INJWKR;
        if ("_DependentCoveredPartyRoleType".equals(codeString))
          return _DEPENDENTCOVEREDPARTYROLETYPE;
        if ("COCBEN".equals(codeString))
          return COCBEN;
        if ("DIFFABL".equals(codeString))
          return DIFFABL;
        if ("WARD".equals(codeString))
          return WARD;
        if ("_IndividualInsuredPartyRoleType".equals(codeString))
          return _INDIVIDUALINSUREDPARTYROLETYPE;
        if ("RETIREE".equals(codeString))
          return RETIREE;
        if ("_ProgramEligiblePartyRoleType".equals(codeString))
          return _PROGRAMELIGIBLEPARTYROLETYPE;
        if ("INDIG".equals(codeString))
          return INDIG;
        if ("MIL".equals(codeString))
          return MIL;
        if ("ACTMIL".equals(codeString))
          return ACTMIL;
        if ("RETMIL".equals(codeString))
          return RETMIL;
        if ("VET".equals(codeString))
          return VET;
        if ("_SubscriberCoveredPartyRoleType".equals(codeString))
          return _SUBSCRIBERCOVEREDPARTYROLETYPE;
        if ("_ResearchSubjectRoleBasis".equals(codeString))
          return _RESEARCHSUBJECTROLEBASIS;
        if ("ERL".equals(codeString))
          return ERL;
        if ("SCN".equals(codeString))
          return SCN;
        if ("_ServiceDeliveryLocationRoleType".equals(codeString))
          return _SERVICEDELIVERYLOCATIONROLETYPE;
        if ("_DedicatedServiceDeliveryLocationRoleType".equals(codeString))
          return _DEDICATEDSERVICEDELIVERYLOCATIONROLETYPE;
        if ("_DedicatedClinicalLocationRoleType".equals(codeString))
          return _DEDICATEDCLINICALLOCATIONROLETYPE;
        if ("DX".equals(codeString))
          return DX;
        if ("CVDX".equals(codeString))
          return CVDX;
        if ("CATH".equals(codeString))
          return CATH;
        if ("ECHO".equals(codeString))
          return ECHO;
        if ("GIDX".equals(codeString))
          return GIDX;
        if ("ENDOS".equals(codeString))
          return ENDOS;
        if ("RADDX".equals(codeString))
          return RADDX;
        if ("RADO".equals(codeString))
          return RADO;
        if ("RNEU".equals(codeString))
          return RNEU;
        if ("HOSP".equals(codeString))
          return HOSP;
        if ("CHR".equals(codeString))
          return CHR;
        if ("GACH".equals(codeString))
          return GACH;
        if ("MHSP".equals(codeString))
          return MHSP;
        if ("PSYCHF".equals(codeString))
          return PSYCHF;
        if ("RH".equals(codeString))
          return RH;
        if ("RHAT".equals(codeString))
          return RHAT;
        if ("RHII".equals(codeString))
          return RHII;
        if ("RHMAD".equals(codeString))
          return RHMAD;
        if ("RHPI".equals(codeString))
          return RHPI;
        if ("RHPIH".equals(codeString))
          return RHPIH;
        if ("RHPIMS".equals(codeString))
          return RHPIMS;
        if ("RHPIVS".equals(codeString))
          return RHPIVS;
        if ("RHYAD".equals(codeString))
          return RHYAD;
        if ("HU".equals(codeString))
          return HU;
        if ("BMTU".equals(codeString))
          return BMTU;
        if ("CCU".equals(codeString))
          return CCU;
        if ("CHEST".equals(codeString))
          return CHEST;
        if ("EPIL".equals(codeString))
          return EPIL;
        if ("ER".equals(codeString))
          return ER;
        if ("ETU".equals(codeString))
          return ETU;
        if ("HD".equals(codeString))
          return HD;
        if ("HLAB".equals(codeString))
          return HLAB;
        if ("INLAB".equals(codeString))
          return INLAB;
        if ("OUTLAB".equals(codeString))
          return OUTLAB;
        if ("HRAD".equals(codeString))
          return HRAD;
        if ("HUSCS".equals(codeString))
          return HUSCS;
        if ("ICU".equals(codeString))
          return ICU;
        if ("PEDICU".equals(codeString))
          return PEDICU;
        if ("PEDNICU".equals(codeString))
          return PEDNICU;
        if ("INPHARM".equals(codeString))
          return INPHARM;
        if ("MBL".equals(codeString))
          return MBL;
        if ("NCCS".equals(codeString))
          return NCCS;
        if ("NS".equals(codeString))
          return NS;
        if ("OUTPHARM".equals(codeString))
          return OUTPHARM;
        if ("PEDU".equals(codeString))
          return PEDU;
        if ("PHU".equals(codeString))
          return PHU;
        if ("RHU".equals(codeString))
          return RHU;
        if ("SLEEP".equals(codeString))
          return SLEEP;
        if ("NCCF".equals(codeString))
          return NCCF;
        if ("SNF".equals(codeString))
          return SNF;
        if ("OF".equals(codeString))
          return OF;
        if ("ALL".equals(codeString))
          return ALL;
        if ("AMPUT".equals(codeString))
          return AMPUT;
        if ("BMTC".equals(codeString))
          return BMTC;
        if ("BREAST".equals(codeString))
          return BREAST;
        if ("CANC".equals(codeString))
          return CANC;
        if ("CAPC".equals(codeString))
          return CAPC;
        if ("CARD".equals(codeString))
          return CARD;
        if ("PEDCARD".equals(codeString))
          return PEDCARD;
        if ("COAG".equals(codeString))
          return COAG;
        if ("CRS".equals(codeString))
          return CRS;
        if ("DERM".equals(codeString))
          return DERM;
        if ("ENDO".equals(codeString))
          return ENDO;
        if ("PEDE".equals(codeString))
          return PEDE;
        if ("ENT".equals(codeString))
          return ENT;
        if ("FMC".equals(codeString))
          return FMC;
        if ("GI".equals(codeString))
          return GI;
        if ("PEDGI".equals(codeString))
          return PEDGI;
        if ("GIM".equals(codeString))
          return GIM;
        if ("GYN".equals(codeString))
          return GYN;
        if ("HEM".equals(codeString))
          return HEM;
        if ("PEDHEM".equals(codeString))
          return PEDHEM;
        if ("HTN".equals(codeString))
          return HTN;
        if ("IEC".equals(codeString))
          return IEC;
        if ("INFD".equals(codeString))
          return INFD;
        if ("PEDID".equals(codeString))
          return PEDID;
        if ("INV".equals(codeString))
          return INV;
        if ("LYMPH".equals(codeString))
          return LYMPH;
        if ("MGEN".equals(codeString))
          return MGEN;
        if ("NEPH".equals(codeString))
          return NEPH;
        if ("PEDNEPH".equals(codeString))
          return PEDNEPH;
        if ("NEUR".equals(codeString))
          return NEUR;
        if ("OB".equals(codeString))
          return OB;
        if ("OMS".equals(codeString))
          return OMS;
        if ("ONCL".equals(codeString))
          return ONCL;
        if ("PEDHO".equals(codeString))
          return PEDHO;
        if ("OPH".equals(codeString))
          return OPH;
        if ("OPTC".equals(codeString))
          return OPTC;
        if ("ORTHO".equals(codeString))
          return ORTHO;
        if ("HAND".equals(codeString))
          return HAND;
        if ("PAINCL".equals(codeString))
          return PAINCL;
        if ("PC".equals(codeString))
          return PC;
        if ("PEDC".equals(codeString))
          return PEDC;
        if ("PEDRHEUM".equals(codeString))
          return PEDRHEUM;
        if ("POD".equals(codeString))
          return POD;
        if ("PREV".equals(codeString))
          return PREV;
        if ("PROCTO".equals(codeString))
          return PROCTO;
        if ("PROFF".equals(codeString))
          return PROFF;
        if ("PROS".equals(codeString))
          return PROS;
        if ("PSI".equals(codeString))
          return PSI;
        if ("PSY".equals(codeString))
          return PSY;
        if ("RHEUM".equals(codeString))
          return RHEUM;
        if ("SPMED".equals(codeString))
          return SPMED;
        if ("SU".equals(codeString))
          return SU;
        if ("PLS".equals(codeString))
          return PLS;
        if ("URO".equals(codeString))
          return URO;
        if ("TR".equals(codeString))
          return TR;
        if ("TRAVEL".equals(codeString))
          return TRAVEL;
        if ("WND".equals(codeString))
          return WND;
        if ("RTF".equals(codeString))
          return RTF;
        if ("PRC".equals(codeString))
          return PRC;
        if ("SURF".equals(codeString))
          return SURF;
        if ("_DedicatedNonClinicalLocationRoleType".equals(codeString))
          return _DEDICATEDNONCLINICALLOCATIONROLETYPE;
        if ("DADDR".equals(codeString))
          return DADDR;
        if ("MOBL".equals(codeString))
          return MOBL;
        if ("AMB".equals(codeString))
          return AMB;
        if ("PHARM".equals(codeString))
          return PHARM;
        if ("_IncidentalServiceDeliveryLocationRoleType".equals(codeString))
          return _INCIDENTALSERVICEDELIVERYLOCATIONROLETYPE;
        if ("ACC".equals(codeString))
          return ACC;
        if ("COMM".equals(codeString))
          return COMM;
        if ("CSC".equals(codeString))
          return CSC;
        if ("PTRES".equals(codeString))
          return PTRES;
        if ("SCHOOL".equals(codeString))
          return SCHOOL;
        if ("UPC".equals(codeString))
          return UPC;
        if ("WORK".equals(codeString))
          return WORK;
        if ("_SpecimenRoleType".equals(codeString))
          return _SPECIMENROLETYPE;
        if ("C".equals(codeString))
          return C;
        if ("G".equals(codeString))
          return G;
        if ("L".equals(codeString))
          return L;
        if ("P".equals(codeString))
          return P;
        if ("Q".equals(codeString))
          return Q;
        if ("B".equals(codeString))
          return B;
        if ("E".equals(codeString))
          return E;
        if ("F".equals(codeString))
          return F;
        if ("O".equals(codeString))
          return O;
        if ("V".equals(codeString))
          return V;
        if ("R".equals(codeString))
          return R;
        if ("CLAIM".equals(codeString))
          return CLAIM;
        if ("communityLaboratory".equals(codeString))
          return COMMUNITYLABORATORY;
        if ("GT".equals(codeString))
          return GT;
        if ("homeHealth".equals(codeString))
          return HOMEHEALTH;
        if ("laboratory".equals(codeString))
          return LABORATORY;
        if ("pathologist".equals(codeString))
          return PATHOLOGIST;
        if ("PH".equals(codeString))
          return PH;
        if ("phlebotomist".equals(codeString))
          return PHLEBOTOMIST;
        if ("PROG".equals(codeString))
          return PROG;
        if ("PT".equals(codeString))
          return PT;
        if ("subject".equals(codeString))
          return SUBJECT;
        if ("thirdParty".equals(codeString))
          return THIRDPARTY;
        throw new Exception("Unknown V3RoleCode code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _AFFILIATIONROLETYPE: return "_AffiliationRoleType";
            case _COVERAGESPONSORROLETYPE: return "_CoverageSponsorRoleType";
            case FULLINS: return "FULLINS";
            case SELFINS: return "SELFINS";
            case _PAYORROLETYPE: return "_PayorRoleType";
            case ENROLBKR: return "ENROLBKR";
            case TPA: return "TPA";
            case UMO: return "UMO";
            case RESPRSN: return "RESPRSN";
            case EXCEST: return "EXCEST";
            case GUADLTM: return "GUADLTM";
            case GUARD: return "GUARD";
            case POWATT: return "POWATT";
            case DPOWATT: return "DPOWATT";
            case HPOWATT: return "HPOWATT";
            case SPOWATT: return "SPOWATT";
            case _ASSIGNEDROLETYPE: return "_AssignedRoleType";
            case _ASSIGNEDNONPERSONLIVINGSUBJECTROLETYPE: return "_AssignedNonPersonLivingSubjectRoleType";
            case ASSIST: return "ASSIST";
            case BIOTH: return "BIOTH";
            case ANTIBIOT: return "ANTIBIOT";
            case DEBR: return "DEBR";
            case CCO: return "CCO";
            case SEE: return "SEE";
            case SNIFF: return "SNIFF";
            case _CITIZENROLETYPE: return "_CitizenRoleType";
            case CAS: return "CAS";
            case CASM: return "CASM";
            case CN: return "CN";
            case CNRP: return "CNRP";
            case CNRPM: return "CNRPM";
            case CPCA: return "CPCA";
            case CRP: return "CRP";
            case CRPM: return "CRPM";
            case _CONTACTROLETYPE: return "_ContactRoleType";
            case _ADMINISTRATIVECONTACTROLETYPE: return "_AdministrativeContactRoleType";
            case BILL: return "BILL";
            case ORG: return "ORG";
            case PAYOR: return "PAYOR";
            case ECON: return "ECON";
            case NOK: return "NOK";
            case _IDENTIFIEDENTITYTYPE: return "_IdentifiedEntityType";
            case _LOCATIONIDENTIFIEDENTITYROLECODE: return "_LocationIdentifiedEntityRoleCode";
            case ACHFID: return "ACHFID";
            case JURID: return "JURID";
            case LOCHFID: return "LOCHFID";
            case _LIVINGSUBJECTPRODUCTIONCLASS: return "_LivingSubjectProductionClass";
            case BF: return "BF";
            case BL: return "BL";
            case BR: return "BR";
            case CO: return "CO";
            case DA: return "DA";
            case DR: return "DR";
            case DU: return "DU";
            case FI: return "FI";
            case LY: return "LY";
            case MT: return "MT";
            case MU: return "MU";
            case PL: return "PL";
            case RC: return "RC";
            case SH: return "SH";
            case VL: return "VL";
            case WL: return "WL";
            case WO: return "WO";
            case _MEDICATIONGENERALIZATIONROLETYPE: return "_MedicationGeneralizationRoleType";
            case DC: return "DC";
            case GD: return "GD";
            case GDF: return "GDF";
            case GDS: return "GDS";
            case GDSF: return "GDSF";
            case MGDSF: return "MGDSF";
            case _MEMBERROLETYPE: return "_MemberRoleType";
            case TRB: return "TRB";
            case _PERSONALRELATIONSHIPROLETYPE: return "_PersonalRelationshipRoleType";
            case FAMMEMB: return "FAMMEMB";
            case CHILD: return "CHILD";
            case CHLDADOPT: return "CHLDADOPT";
            case DAUADOPT: return "DAUADOPT";
            case SONADOPT: return "SONADOPT";
            case CHLDFOST: return "CHLDFOST";
            case DAUFOST: return "DAUFOST";
            case SONFOST: return "SONFOST";
            case DAUC: return "DAUC";
            case DAU: return "DAU";
            case STPDAU: return "STPDAU";
            case NCHILD: return "NCHILD";
            case SON: return "SON";
            case SONC: return "SONC";
            case STPSON: return "STPSON";
            case STPCHLD: return "STPCHLD";
            case EXT: return "EXT";
            case AUNT: return "AUNT";
            case MAUNT: return "MAUNT";
            case PAUNT: return "PAUNT";
            case COUSN: return "COUSN";
            case MCOUSN: return "MCOUSN";
            case PCOUSN: return "PCOUSN";
            case GGRPRN: return "GGRPRN";
            case GGRFTH: return "GGRFTH";
            case MGGRFTH: return "MGGRFTH";
            case PGGRFTH: return "PGGRFTH";
            case GGRMTH: return "GGRMTH";
            case MGGRMTH: return "MGGRMTH";
            case PGGRMTH: return "PGGRMTH";
            case MGGRPRN: return "MGGRPRN";
            case PGGRPRN: return "PGGRPRN";
            case GRNDCHILD: return "GRNDCHILD";
            case GRNDDAU: return "GRNDDAU";
            case GRNDSON: return "GRNDSON";
            case GRPRN: return "GRPRN";
            case GRFTH: return "GRFTH";
            case MGRFTH: return "MGRFTH";
            case PGRFTH: return "PGRFTH";
            case GRMTH: return "GRMTH";
            case MGRMTH: return "MGRMTH";
            case PGRMTH: return "PGRMTH";
            case MGRPRN: return "MGRPRN";
            case PGRPRN: return "PGRPRN";
            case INLAW: return "INLAW";
            case CHLDINLAW: return "CHLDINLAW";
            case DAUINLAW: return "DAUINLAW";
            case SONINLAW: return "SONINLAW";
            case PRNINLAW: return "PRNINLAW";
            case FTHINLAW: return "FTHINLAW";
            case MTHINLAW: return "MTHINLAW";
            case SIBINLAW: return "SIBINLAW";
            case BROINLAW: return "BROINLAW";
            case SISINLAW: return "SISINLAW";
            case NIENEPH: return "NIENEPH";
            case NEPHEW: return "NEPHEW";
            case NIECE: return "NIECE";
            case UNCLE: return "UNCLE";
            case MUNCLE: return "MUNCLE";
            case PUNCLE: return "PUNCLE";
            case PRN: return "PRN";
            case ADOPTP: return "ADOPTP";
            case ADOPTF: return "ADOPTF";
            case ADOPTM: return "ADOPTM";
            case FTH: return "FTH";
            case FTHFOST: return "FTHFOST";
            case NFTH: return "NFTH";
            case NFTHF: return "NFTHF";
            case STPFTH: return "STPFTH";
            case MTH: return "MTH";
            case GESTM: return "GESTM";
            case MTHFOST: return "MTHFOST";
            case NMTH: return "NMTH";
            case NMTHF: return "NMTHF";
            case STPMTH: return "STPMTH";
            case NPRN: return "NPRN";
            case PRNFOST: return "PRNFOST";
            case STPPRN: return "STPPRN";
            case SIB: return "SIB";
            case BRO: return "BRO";
            case HBRO: return "HBRO";
            case NBRO: return "NBRO";
            case TWINBRO: return "TWINBRO";
            case FTWINBRO: return "FTWINBRO";
            case ITWINBRO: return "ITWINBRO";
            case STPBRO: return "STPBRO";
            case HSIB: return "HSIB";
            case HSIS: return "HSIS";
            case NSIB: return "NSIB";
            case NSIS: return "NSIS";
            case TWINSIS: return "TWINSIS";
            case FTWINSIS: return "FTWINSIS";
            case ITWINSIS: return "ITWINSIS";
            case TWIN: return "TWIN";
            case FTWIN: return "FTWIN";
            case ITWIN: return "ITWIN";
            case SIS: return "SIS";
            case STPSIS: return "STPSIS";
            case STPSIB: return "STPSIB";
            case SIGOTHR: return "SIGOTHR";
            case DOMPART: return "DOMPART";
            case FMRSPS: return "FMRSPS";
            case SPS: return "SPS";
            case HUSB: return "HUSB";
            case WIFE: return "WIFE";
            case FRND: return "FRND";
            case NBOR: return "NBOR";
            case ONESELF: return "ONESELF";
            case ROOM: return "ROOM";
            case _POLICYORPROGRAMCOVERAGEROLETYPE: return "_PolicyOrProgramCoverageRoleType";
            case _COVERAGEROLETYPE: return "_CoverageRoleType";
            case FAMDEP: return "FAMDEP";
            case HANDIC: return "HANDIC";
            case INJ: return "INJ";
            case SELF: return "SELF";
            case SPON: return "SPON";
            case STUD: return "STUD";
            case FSTUD: return "FSTUD";
            case PSTUD: return "PSTUD";
            case _COVEREDPARTYROLETYPE: return "_CoveredPartyRoleType";
            case _CLAIMANTCOVEREDPARTYROLETYPE: return "_ClaimantCoveredPartyRoleType";
            case CRIMEVIC: return "CRIMEVIC";
            case INJWKR: return "INJWKR";
            case _DEPENDENTCOVEREDPARTYROLETYPE: return "_DependentCoveredPartyRoleType";
            case COCBEN: return "COCBEN";
            case DIFFABL: return "DIFFABL";
            case WARD: return "WARD";
            case _INDIVIDUALINSUREDPARTYROLETYPE: return "_IndividualInsuredPartyRoleType";
            case RETIREE: return "RETIREE";
            case _PROGRAMELIGIBLEPARTYROLETYPE: return "_ProgramEligiblePartyRoleType";
            case INDIG: return "INDIG";
            case MIL: return "MIL";
            case ACTMIL: return "ACTMIL";
            case RETMIL: return "RETMIL";
            case VET: return "VET";
            case _SUBSCRIBERCOVEREDPARTYROLETYPE: return "_SubscriberCoveredPartyRoleType";
            case _RESEARCHSUBJECTROLEBASIS: return "_ResearchSubjectRoleBasis";
            case ERL: return "ERL";
            case SCN: return "SCN";
            case _SERVICEDELIVERYLOCATIONROLETYPE: return "_ServiceDeliveryLocationRoleType";
            case _DEDICATEDSERVICEDELIVERYLOCATIONROLETYPE: return "_DedicatedServiceDeliveryLocationRoleType";
            case _DEDICATEDCLINICALLOCATIONROLETYPE: return "_DedicatedClinicalLocationRoleType";
            case DX: return "DX";
            case CVDX: return "CVDX";
            case CATH: return "CATH";
            case ECHO: return "ECHO";
            case GIDX: return "GIDX";
            case ENDOS: return "ENDOS";
            case RADDX: return "RADDX";
            case RADO: return "RADO";
            case RNEU: return "RNEU";
            case HOSP: return "HOSP";
            case CHR: return "CHR";
            case GACH: return "GACH";
            case MHSP: return "MHSP";
            case PSYCHF: return "PSYCHF";
            case RH: return "RH";
            case RHAT: return "RHAT";
            case RHII: return "RHII";
            case RHMAD: return "RHMAD";
            case RHPI: return "RHPI";
            case RHPIH: return "RHPIH";
            case RHPIMS: return "RHPIMS";
            case RHPIVS: return "RHPIVS";
            case RHYAD: return "RHYAD";
            case HU: return "HU";
            case BMTU: return "BMTU";
            case CCU: return "CCU";
            case CHEST: return "CHEST";
            case EPIL: return "EPIL";
            case ER: return "ER";
            case ETU: return "ETU";
            case HD: return "HD";
            case HLAB: return "HLAB";
            case INLAB: return "INLAB";
            case OUTLAB: return "OUTLAB";
            case HRAD: return "HRAD";
            case HUSCS: return "HUSCS";
            case ICU: return "ICU";
            case PEDICU: return "PEDICU";
            case PEDNICU: return "PEDNICU";
            case INPHARM: return "INPHARM";
            case MBL: return "MBL";
            case NCCS: return "NCCS";
            case NS: return "NS";
            case OUTPHARM: return "OUTPHARM";
            case PEDU: return "PEDU";
            case PHU: return "PHU";
            case RHU: return "RHU";
            case SLEEP: return "SLEEP";
            case NCCF: return "NCCF";
            case SNF: return "SNF";
            case OF: return "OF";
            case ALL: return "ALL";
            case AMPUT: return "AMPUT";
            case BMTC: return "BMTC";
            case BREAST: return "BREAST";
            case CANC: return "CANC";
            case CAPC: return "CAPC";
            case CARD: return "CARD";
            case PEDCARD: return "PEDCARD";
            case COAG: return "COAG";
            case CRS: return "CRS";
            case DERM: return "DERM";
            case ENDO: return "ENDO";
            case PEDE: return "PEDE";
            case ENT: return "ENT";
            case FMC: return "FMC";
            case GI: return "GI";
            case PEDGI: return "PEDGI";
            case GIM: return "GIM";
            case GYN: return "GYN";
            case HEM: return "HEM";
            case PEDHEM: return "PEDHEM";
            case HTN: return "HTN";
            case IEC: return "IEC";
            case INFD: return "INFD";
            case PEDID: return "PEDID";
            case INV: return "INV";
            case LYMPH: return "LYMPH";
            case MGEN: return "MGEN";
            case NEPH: return "NEPH";
            case PEDNEPH: return "PEDNEPH";
            case NEUR: return "NEUR";
            case OB: return "OB";
            case OMS: return "OMS";
            case ONCL: return "ONCL";
            case PEDHO: return "PEDHO";
            case OPH: return "OPH";
            case OPTC: return "OPTC";
            case ORTHO: return "ORTHO";
            case HAND: return "HAND";
            case PAINCL: return "PAINCL";
            case PC: return "PC";
            case PEDC: return "PEDC";
            case PEDRHEUM: return "PEDRHEUM";
            case POD: return "POD";
            case PREV: return "PREV";
            case PROCTO: return "PROCTO";
            case PROFF: return "PROFF";
            case PROS: return "PROS";
            case PSI: return "PSI";
            case PSY: return "PSY";
            case RHEUM: return "RHEUM";
            case SPMED: return "SPMED";
            case SU: return "SU";
            case PLS: return "PLS";
            case URO: return "URO";
            case TR: return "TR";
            case TRAVEL: return "TRAVEL";
            case WND: return "WND";
            case RTF: return "RTF";
            case PRC: return "PRC";
            case SURF: return "SURF";
            case _DEDICATEDNONCLINICALLOCATIONROLETYPE: return "_DedicatedNonClinicalLocationRoleType";
            case DADDR: return "DADDR";
            case MOBL: return "MOBL";
            case AMB: return "AMB";
            case PHARM: return "PHARM";
            case _INCIDENTALSERVICEDELIVERYLOCATIONROLETYPE: return "_IncidentalServiceDeliveryLocationRoleType";
            case ACC: return "ACC";
            case COMM: return "COMM";
            case CSC: return "CSC";
            case PTRES: return "PTRES";
            case SCHOOL: return "SCHOOL";
            case UPC: return "UPC";
            case WORK: return "WORK";
            case _SPECIMENROLETYPE: return "_SpecimenRoleType";
            case C: return "C";
            case G: return "G";
            case L: return "L";
            case P: return "P";
            case Q: return "Q";
            case B: return "B";
            case E: return "E";
            case F: return "F";
            case O: return "O";
            case V: return "V";
            case R: return "R";
            case CLAIM: return "CLAIM";
            case COMMUNITYLABORATORY: return "communityLaboratory";
            case GT: return "GT";
            case HOMEHEALTH: return "homeHealth";
            case LABORATORY: return "laboratory";
            case PATHOLOGIST: return "pathologist";
            case PH: return "PH";
            case PHLEBOTOMIST: return "phlebotomist";
            case PROG: return "PROG";
            case PT: return "PT";
            case SUBJECT: return "subject";
            case THIRDPARTY: return "thirdParty";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/RoleCode";
        }
        public String getDefinition() {
          switch (this) {
            case _AFFILIATIONROLETYPE: return "Concepts characterizing the type of association formed by player and scoper when there is a recognized Affiliate role by which the two parties are related.\r\n\n                        \n                           Examples: Business Partner, Business Associate, Colleague";
            case _COVERAGESPONSORROLETYPE: return "Description:Codes that indicate a specific type of sponsor.  Used when the sponsor's role is only either as a fully insured sponsor or only as a self-insured sponsor.  NOTE: Where a sponsor may be either, use the SponsorParticipationFunction.code (fully insured or self insured) to indicate the type of responsibility. (CO6-0057)";
            case FULLINS: return "Description:An employer or organization that contracts with an underwriter to assumes the financial risk and administrative responsibility for coverage of health services for covered parties.";
            case SELFINS: return "Description:An employer or organization that assumes the financial risk and administrative responsibility for coverage of health services for covered parties.";
            case _PAYORROLETYPE: return "Description:PayorRoleType for a particular type of policy or program benefit package or plan where more detail about the coverage administration role of the Payor is required.  The functions performed by a Payor qualified by a PayorRoleType may be specified by the PayorParticpationFunction value set.\r\n\n                        \n                           Examples:A Payor that is a TPA may administer a managed care plan without underwriting the risk.";
            case ENROLBKR: return "Description:A payor that is responsible for functions related to the enrollment of covered parties.";
            case TPA: return "Description:Third party administrator (TPA) is a payor organization that processes health care claims without carrying insurance risk. Third party administrators are prominent players in the managed care industry and have the expertise and capability to administer all or a portion of the claims process. They are normally contracted by a health insurer or self-insuring companies to administer services, including claims administration, premium collection, enrollment and other administrative activities.\r\n\n                        Self-insured employers often contract with third party administrator to handle their insurance functions. Insurance companies oftentimes outsource the claims, utilization review or membership functions to a TPA. Sometimes TPAs only manage provider networks, only claims or only utilization review.\r\n\n                        While some third-party administrators may operate as units of insurance companies, they are often independent. However, hospitals or provider organizations desiring to set up their own health plans will often outsource certain responsibilities to TPAs.  TPAs may perform one or several payor functions, specified by the PayorParticipationFunction value set, such as provider management, enrollment, utilization management, and fee for service claims adjudication management.";
            case UMO: return "Description:A payor that is responsible for review and case management of health services covered under a policy or program.";
            case RESPRSN: return "The role played by a party who has legal responsibility for another party.";
            case EXCEST: return "The role played by a person acting as the estate executor for a deceased subscriber or policyholder who was the responsible party";
            case GUADLTM: return "The role played by a person appointed by the court to look out for the best interests of a minor child during the course of legal proceedings.";
            case GUARD: return "The role played by a person or institution legally empowered with responsibility for the care of a ward.";
            case POWATT: return "A relationship between two people in which one person authorizes another to act for him in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts.";
            case DPOWATT: return "A relationship between two people in which one person authorizes another, usually a family member or relative, to act for him or her in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts that is often limited in the kinds of powers that can be assigned.  Unlike ordinary powers of attorney, durable powers can survive for long periods of time, and again, unlike standard powers of attorney, durable powers can continue after incompetency.";
            case HPOWATT: return "A relationship between two people in which one person authorizes another to act for him or her in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts that continues (by its terms) to be effective even though the grantor has become mentally incompetent after signing the document.";
            case SPOWATT: return "A relationship between two people in which one person authorizes another to act for him or her in a manner which is a legally binding upon the person giving such authority as if he or she personally were to do the acts that is often limited in the kinds of powers that can be assigned.";
            case _ASSIGNEDROLETYPE: return "AssignedRoleType";
            case _ASSIGNEDNONPERSONLIVINGSUBJECTROLETYPE: return "Description:A role type that is used to further qualify a non-person subject playing a role where the role class attribute is set to RoleClass AssignedEntity";
            case ASSIST: return "Description:Dogs trained to assist the ill or physically challenged.";
            case BIOTH: return "Description:Animals, including fish and insects, and microorganisms which may participate as assigned entities in biotherapies.";
            case ANTIBIOT: return "Description:Non-person living subject used as antibiotic.\r\n\n                        \n                           Examples:Bacteriophage, is a virus that infects bacteria.";
            case DEBR: return "Description:Maggots raised for biodebridement.\r\n\n                        \n                           Discussion: Maggot Debridement Therapy is the medical use of live maggots for cleaning non-healing wounds.\r\n\n                        \n                           Examples:Removal of burnt skin.";
            case CCO: return "Description:Companion animals, such as dogs, cats, and rabbits, which may be provided to patients to improve general mood, decrease depression and loneliness, and distract from stress-inducing concerns to improve quality of life.";
            case SEE: return "Description:Dogs trained to assist persons who are seeing impaired or blind.";
            case SNIFF: return "Description:Dogs trained or having the ability to detect imminent seizures or cancers in humans, probably as a result of volatile chemical (odors) given off by the malignancy of the host.";
            case _CITIZENROLETYPE: return "A role type used to qualify a person's legal status within a country or nation.";
            case CAS: return "A person who has fled his or her home country to find a safe place elsewhere.";
            case CASM: return "A person who is someone of below legal age who has fled his or her home country, without his or her parents, to find a safe place elsewhere at time of categorization.";
            case CN: return "A person who is legally recognized as a member of a nation or country, with associated rights and obligations.";
            case CNRP: return "A foreigner who is present in a country (which is foreign to him/her) unlawfully or without the country's authorization (may be called an illegal alien).";
            case CNRPM: return "A person who is below legal age present in a country, without his or her parents, (which is foreign to him/her) unlawfully or without the country's authorization.";
            case CPCA: return "A non-country member admitted to the territory of a nation or country as a non-resident for an explicit purpose.";
            case CRP: return "A foreigner who is a resident of the country but does not have citizenship.";
            case CRPM: return "A person who is a resident below legal age of the country without his or her parents and does not have citizenship.";
            case _CONTACTROLETYPE: return "Types of contact for Role code \"CON\"";
            case _ADMINISTRATIVECONTACTROLETYPE: return "A contact role used for business and/or administrative purposes.";
            case BILL: return "A contact role used to identify a person within a Provider organization that can be contacted for billing purposes (e.g. about the content of the Invoice).";
            case ORG: return "A contact for an organization for administrative purposes. Contact role specifies a person acting as a liason for the organization.\r\n\n                        Example: HR Department representative.";
            case PAYOR: return "A contact role used to identify a person within a Payor organization to whom this communication is addressed.";
            case ECON: return "A contact designated for contact in emergent situations.";
            case NOK: return "Played by an individual who is designated as the next of kin for another individual which scopes the role.";
            case _IDENTIFIEDENTITYTYPE: return "Definition: A code representing the type of identifier that has been assigned to the identified entity (IDENT).\r\n\n                        \n                           Examples: Example values include Social Insurance Number, Product Catalog ID, Product Model Number.";
            case _LOCATIONIDENTIFIEDENTITYROLECODE: return "Description:Describes types of identifiers other than the primary location registry identifier for a service delivery location.  Identifiers may be assigned by a local service delivery organization, a formal body capable of accrediting the location for the capability to provide specific services or the identifier may be assigned at a jurisdictional level.";
            case ACHFID: return "Description:Identifier assigned to a  location by the organization responsible for accrediting the location.";
            case JURID: return "Description:Identifier assigned to a location by a jurisdiction.";
            case LOCHFID: return "Description:Identifier assigned to a  location by a local party (which could be the facility itself or organization overseeing a group of facilities).";
            case _LIVINGSUBJECTPRODUCTIONCLASS: return "Code indicating the primary use for which a living subject is bred or grown";
            case BF: return "Cattle used for meat production";
            case BL: return "Chickens raised for meat";
            case BR: return "Breeding/genetic stock";
            case CO: return "Companion animals";
            case DA: return "Milk production";
            case DR: return "Draft animals";
            case DU: return "Dual purpose.  Defined purposes based on species and breed";
            case FI: return "Animals raised for their fur, hair or skins";
            case LY: return "Chickens raised for egg production";
            case MT: return "Animals raised for meat production";
            case MU: return "Poultry flocks used for chick/poult production";
            case PL: return "Animals rasied for recreation";
            case RC: return "Animals raised for racing perfomance";
            case SH: return "Animals raised for shows";
            case VL: return "Cattle raised for veal meat production.  Implicit is the husbandry method.";
            case WL: return "Sheep, goats and other mammals raised for their fiber";
            case WO: return "Animals used to perform work";
            case _MEDICATIONGENERALIZATIONROLETYPE: return "Identifies the specific hierarchical relationship between the playing and scoping medications. \r\n\n                        \n                           Examples: Generic, Generic Formulation, Therapeutic Class, etc.";
            case DC: return "Description:A categorization of medicinal products by their therapeutic properties and/or main therapeutic use.";
            case GD: return "Relates a manufactured drug product to the non-proprietary (generic) representation of its ingredients independent of strength, and form.\r\n\n                        The scoping entity identifies a unique combination of medicine ingredients; sometimes referred to as \"ingredient set\".";
            case GDF: return "Relates a manufactured drug product to the non-proprietary (generic) representation of its ingredients and dose form, independent of strength of the ingredients. The scoping entity identifies a unique combination of medicine ingredients in a specific dose form.";
            case GDS: return "Relates a manufactured drug product to the non-proprietary (generic) representation of is ingredients with their strength.  The scoping entity identifies a unique combination of medicine ingredients with their strength.";
            case GDSF: return "Relates a manufactured drug product to the non-proprietary (generic) representation of its ingredients with their strength in a specific dose form. The scoping entity identifies a unique combination of medicine ingredients with their strength in a single dose form.";
            case MGDSF: return "Relates a manufactured drug product to the non-proprietary (generic) representation of its ingredients with their strength in a specific dose form. The scoping entity identifies a unique combination of medicine ingredients with their strength in a single dose form.";
            case _MEMBERROLETYPE: return "Types of membership for Role code \"MBR\"";
            case TRB: return "A person who is a member of a tribe.";
            case _PERSONALRELATIONSHIPROLETYPE: return "PersonalRelationshipRoleType";
            case FAMMEMB: return "A relationship between two people characterizing their \"familial\" relationship";
            case CHILD: return "The player of the role is a child of the scoping entity.";
            case CHLDADOPT: return "The player of the role is a child taken into a family through legal means and raised by the scoping person (parent) as his or her own child.";
            case DAUADOPT: return "The player of the role is a female child taken into a family through legal means and raised by the scoping person (parent) as his or her own child.";
            case SONADOPT: return "The player of the role is a male child taken into a family through legal means and raised by the scoping person (parent) as his or her own child.";
            case CHLDFOST: return "The player of the role is a child receiving parental care and nurture from the scoping person (parent) but not related to him or her through legal or blood ties.";
            case DAUFOST: return "The player of the role is a female child receiving parental care and nurture from the scoping person (parent) but not related to him or her through legal or blood ties.";
            case SONFOST: return "The player of the role is a male child receiving parental care and nurture from the scoping person (parent) but not related to him or her through legal or blood ties.";
            case DAUC: return "Description: The player of the role is a female child (of any type) of scoping entity (parent)";
            case DAU: return "The player of the role is a female offspring of the scoping entity (parent).";
            case STPDAU: return "The player of the role is a daughter of the scoping person's spouse by a previous union.";
            case NCHILD: return "The player of the role is an offspring of the scoping entity as determined by birth.";
            case SON: return "The player of the role is a male offspring of the scoping entity (parent).";
            case SONC: return "Description: The player of the role is a male child (of any type) of scoping entity (parent)";
            case STPSON: return "The player of the role is a son of the scoping person's spouse by a previous union.";
            case STPCHLD: return "The player of the role is a child of the scoping person's spouse by a previous union.";
            case EXT: return "Description: A family member not having an immediate genetic or legal relationship e.g. Aunt, cousin, great grandparent, grandchild, grandparent, niece, nephew or uncle.";
            case AUNT: return "The player of the role is a sister of the scoping person's mother or father.";
            case MAUNT: return "Description:The player of the role is a biological sister of the scoping person's biological mother.";
            case PAUNT: return "Description:The player of the role is a biological sister of the scoping person's biological father.";
            case COUSN: return "The player of the role is a relative of the scoping person descended from a common ancestor, such as a 	grandparent, by two or more steps in a diverging line.";
            case MCOUSN: return "Description:The player of the role is a biological relative of the scoping person descended from a common ancestor on the player's mother's side, such as a grandparent, by two or more steps in a diverging line.";
            case PCOUSN: return "Description:The player of the role is a biological relative of the scoping person descended from a common ancestor on the player's father's side, such as a grandparent, by two or more steps in a diverging line.";
            case GGRPRN: return "The player of the role is a parent of the scoping person's grandparent.";
            case GGRFTH: return "The player of the role is the father of the scoping person's grandparent.";
            case MGGRFTH: return "Description:The player of the role is the biological father of the scoping person's biological mother's parent.";
            case PGGRFTH: return "Description:The player of the role is the biological father of the scoping person's biological father's parent.";
            case GGRMTH: return "The player of the role is the mother of the scoping person's grandparent.";
            case MGGRMTH: return "Description:The player of the role is the biological mother of the scoping person's biological mother's parent.";
            case PGGRMTH: return "Description:The player of the role is the biological mother of the scoping person's biological father's parent.";
            case MGGRPRN: return "Description:The player of the role is a biological parent of the scoping person's biological mother's parent.";
            case PGGRPRN: return "Description:The player of the role is a biological parent of the scoping person's biological father's parent.";
            case GRNDCHILD: return "The player of the role is a child of the scoping person's son or daughter.";
            case GRNDDAU: return "The player of the role is a daughter of the scoping person's son or daughter.";
            case GRNDSON: return "The player of the role is a son of the scoping person's son or daughter.";
            case GRPRN: return "The player of the role is a parent of the scoping person's mother or father.";
            case GRFTH: return "The player of the role is the father of the scoping person's mother or father.";
            case MGRFTH: return "Description:The player of the role is the biological father of the scoping person's biological mother.";
            case PGRFTH: return "Description:The player of the role is the biological father of the scoping person's biological father.";
            case GRMTH: return "The player of the role is the mother of the scoping person's mother or father.";
            case MGRMTH: return "Description:The player of the role is the biological mother of the scoping person's biological mother.";
            case PGRMTH: return "Description:The player of the role is the biological mother of the scoping person's biological father.";
            case MGRPRN: return "Description:The player of the role is the biological parent of the scoping person's biological mother.";
            case PGRPRN: return "Description:The player of the role is the biological parent of the scoping person's biological father.";
            case INLAW: return "A relationship between an individual and a member of their spousal partner's immediate family.";
            case CHLDINLAW: return "The player of the role is the spouse of scoping person's child.";
            case DAUINLAW: return "The player of the role is the wife of scoping person's son.";
            case SONINLAW: return "The player of the role is the husband of scoping person's daughter.";
            case PRNINLAW: return "The player of the role is the parent of scoping person's husband or wife.";
            case FTHINLAW: return "The player of the role is the father of the scoping person's husband or wife.";
            case MTHINLAW: return "The player of the role is the mother of the scoping person's husband or wife.";
            case SIBINLAW: return "The player of the role is: (1) a sibling of the scoping person's spouse, or (2) the spouse of the scoping person's sibling, or (3) the spouse of a sibling of the scoping person's spouse.";
            case BROINLAW: return "The player of the role is: (1) a brother of the scoping person's spouse, or (2) the husband of the scoping person's sister, or (3) the husband of a sister of the scoping person's spouse.";
            case SISINLAW: return "The player of the role is: (1) a sister of the scoping person's spouse, or (2) the wife of the scoping person's brother, or (3) the wife of a brother of the scoping person's spouse.";
            case NIENEPH: return "The player of the role is a child of scoping person's brother or sister or of the brother or sister of the 	scoping person's spouse.";
            case NEPHEW: return "The player of the role is a son of the scoping person's brother or sister or of the brother or sister of the 	scoping person's spouse.";
            case NIECE: return "The player of the role is a daughter of the scoping person's brother or sister or of the brother or sister of the 	scoping person's spouse.";
            case UNCLE: return "The player of the role is a brother of the scoping person's mother or father.";
            case MUNCLE: return "Description:The player of the role is a biological brother of the scoping person's biological mother.";
            case PUNCLE: return "Description:The player of the role is a biological brother of the scoping person's biological father.";
            case PRN: return "The player of the role is one who begets, gives birth to, or nurtures and raises the scoping entity (child).";
            case ADOPTP: return "The player of the role (parent) has taken the scoper (child) into their family through legal means and raises them as his or her own child.";
            case ADOPTF: return "The player of the role (father) is a male who has taken the scoper (child) into their family through legal means and raises them as his own child.";
            case ADOPTM: return "The player of the role (father) is a female who has taken the scoper (child) into their family through legal means and raises them as her own child.";
            case FTH: return "The player of the role is a male who begets or raises or nurtures the scoping entity (child).";
            case FTHFOST: return "The player of the role (parent) who is a male state-certified caregiver responsible for the scoper (child) who has been placed in the parent's care. The placement of the child is usually arranged through the government or a social-service agency, and temporary.\r\n\n                        The state, via a jurisdiction recognized child protection agency, stands as in loco parentis to the child, making all legal decisions while the foster parent is responsible for the day-to-day care of the specified child.";
            case NFTH: return "The player of the role is a male who begets the scoping entity (child).";
            case NFTHF: return "Indicates the biologic male parent of a fetus.";
            case STPFTH: return "The player of the role is the husband of scoping person's mother and not the scoping person's natural father.";
            case MTH: return "The player of the role is a female who conceives, gives birth to, or raises and nurtures the scoping entity (child).";
            case GESTM: return "The player is a female whose womb carries the fetus of the scoper.  Generally used when the gestational mother and natural mother are not the same.";
            case MTHFOST: return "The player of the role (parent) who is a female state-certified caregiver responsible for the scoper (child) who has been placed in the parent's care. The placement of the child is usually arranged through the government or a social-service agency, and temporary.\r\n\n                        The state, via a jurisdiction recognized child protection agency, stands as in loco parentis to the child, making all legal decisions while the foster parent is responsible for the day-to-day care of the specified child.";
            case NMTH: return "The player of the role is a female who conceives or gives birth to the scoping entity (child).";
            case NMTHF: return "The player is the biologic female parent of the scoping fetus.";
            case STPMTH: return "The player of the role is the wife of scoping person's father and not the scoping person's natural mother.";
            case NPRN: return "natural parent";
            case PRNFOST: return "The player of the role (parent) who is a state-certified caregiver responsible for the scoper (child) who has been placed in the parent's care. The placement of the child is usually arranged through the government or a social-service agency, and temporary.\r\n\n                        The state, via a jurisdiction recognized child protection agency, stands as in loco parentis to the child, making all legal decisions while the foster parent is responsible for the day-to-day care of the specified child.";
            case STPPRN: return "The player of the role is the spouse of the scoping person's parent and not the scoping person's natural parent.";
            case SIB: return "The player of the role shares one or both parents in common with the scoping entity.";
            case BRO: return "The player of the role is a male sharing one or both parents in common with the scoping entity.";
            case HBRO: return "The player of the role is a male related to the scoping entity by sharing only one biological parent.";
            case NBRO: return "The player of the role is a male having the same biological parents as the scoping entity.";
            case TWINBRO: return "The scoper was carried in the same womb as the male player and shares common biological parents.";
            case FTWINBRO: return "The scoper was carried in the same womb as the male player and shares common biological parents but is the product of a distinct egg/sperm pair.";
            case ITWINBRO: return "The male scoper is an offspring of the same egg-sperm pair as the male player.";
            case STPBRO: return "The player of the role is a son of the scoping person's stepparent.";
            case HSIB: return "The player of the role is related to the scoping entity by sharing only one biological parent.";
            case HSIS: return "The player of the role is a female related to the scoping entity by sharing only one biological parent.";
            case NSIB: return "The player of the role has both biological parents in common with the scoping entity.";
            case NSIS: return "The player of the role is a female having the same biological parents as the scoping entity.";
            case TWINSIS: return "The scoper was carried in the same womb as the female player and shares common biological parents.";
            case FTWINSIS: return "The scoper was carried in the same womb as the female player and shares common biological parents but is the product of a distinct egg/sperm pair.";
            case ITWINSIS: return "The female scoper is an offspring of the same egg-sperm pair as the female player.";
            case TWIN: return "The scoper and player were carried in the same womb and shared common biological parents.";
            case FTWIN: return "The scoper and player were carried in the same womb and share common biological parents but are the product of distinct egg/sperm pairs.";
            case ITWIN: return "The scoper and player are offspring of the same egg-sperm pair.";
            case SIS: return "The player of the role is a female sharing one or both parents in common with the scoping entity.";
            case STPSIS: return "The player of the role is a daughter of the scoping person's stepparent.";
            case STPSIB: return "The player of the role is a child of the scoping person's stepparent.";
            case SIGOTHR: return "A person who is important to one's well being; especially a spouse or one in a similar relationship.  (The player is the one who is important)";
            case DOMPART: return "The player of the role cohabits with the scoping person but is not the scoping person's spouse.";
            case FMRSPS: return "Player of the role was previously joined to the scoping person in marriage and this marriage is now dissolved and inactive.\r\n\n                        \n                           Usage Note: This is significant to indicate as some jurisdictions have different legal requirements for former spouse to access the patient's record, from a general friend.";
            case SPS: return "The player of the role is a marriage partner of the scoping person.";
            case HUSB: return "The player of the role is a man joined to a woman (scoping person) in marriage.";
            case WIFE: return "The player of the role is a woman joined to a man (scoping person) in marriage.";
            case FRND: return "The player of the role is a person who is known, liked, and trusted by the scoping person.";
            case NBOR: return "The player of the role lives near or next to the 	scoping person.";
            case ONESELF: return "The relationship that a person has with his or her self.";
            case ROOM: return "One who shares living quarters with the subject.";
            case _POLICYORPROGRAMCOVERAGEROLETYPE: return "Description: A role recognized through the eligibility of an identified party for benefits covered under an insurance policy or a program based on meeting eligibility criteria.\r\n\n                        Eligibility as a covered party may be conditioned on the party meeting criteria to qualify for coverage under a policy or program, which may be mandated by law.  These criteria may be: \r\n\n                        \n                           \n                              The sole basis for coverage, e.g., being differently abled may qualify a person for disability coverage\r\n\n                           \n                           \n                              May more fully qualify a covered party role e.g, being differently abled may qualify an adult child as a dependent\r\n\n                           \n                           \n                              May impact the level of coverage for a covered party under a policy or program, e.g., being differently abled may qualify a program eligible for additional benefits.\r\n\n                           \n                        \n                        \n                           Discussion:  The Abstract Value Set \"CoverageRoleType\", which was developed for use in the Canadian realm \"pre-coordinate\" coverage roles with other roles that a covered party must play in order to be eligible for coverage, e.g., \"handicapped dependent\".   These role.codes may only be used with COVPTY to avoid overlapping concepts that would result from using them to specify the specializations of COVPTY, e.g., the role.class DEPEN should not be used with the role.code family dependent because that relationship has overlapping concepts due to the role.code precoodination and is conveyed in FICO with the personal relationship role that has a PART role link to the covered party role.  For the same reasons, the role.class DEPEN should not be used with the role.code HANDIC (handicapped dependent); the role.code DIFFABLE (differently abled) should be used instead.\r\n\n                        In summary, the coded concepts in the Abstract Value Set \"CoveredPartyRoleType\" can be \"post-coordinated\" with the \"RoleClassCoveredParty\" Abstract Value Set.  Decoupling these concepts is intended to support an expansive range of covered party concepts and their semantic comparability.";
            case _COVERAGEROLETYPE: return "Role recognized through the issuance of insurance coverage to an identified covered party who has this relationship with the policy holder such as the policy holder themselves (self), spouse, child, etc";
            case FAMDEP: return "The player of the role is dependent of the scoping entity.";
            case HANDIC: return "Covered party is a dependent of the policy holder with a physical or mental disability causing a disadvantage that makes independent achievement unusually difficult.";
            case INJ: return "Covered party is an injured party with a legal claim for compensation against a policy holder under an insurance policy.";
            case SELF: return "Covered party is the policy holder.  Also known as the subscriber.";
            case SPON: return "Covered party is an individual that the policy holder has assumed responsibility for, such as foster child or legal ward.";
            case STUD: return "Covered party to an insurance policy has coverage through full-time or part-time attendance at a recognized educational institution as defined by a particular insurance policy.";
            case FSTUD: return "Covered party to an insurance policy has coverage through full-time attendance at a recognized educational institution as defined by a particular insurance policy.";
            case PSTUD: return "Covered party to an insurance policy has coverage through part-time attendance at a recognized educational institution as defined by a particular insurance policy.";
            case _COVEREDPARTYROLETYPE: return "A role recognized through the eligibility of an identified living subject for benefits covered under an insurance policy or a program.  Eligibility as a covered party may be conditioned on a relationship with (1) the policy holder such as the policy holder who is covered as an individual under a poliy or as a party sponsored for coverage by the policy holder.\r\n\n                        \n                           Example:An employee as a subscriber; or (2) on being scoped another covered party such as the subscriber, as in the case of a dependent. \r\n\n                        \n                           Discussion:  The Abstract Value Set \"CoverageRoleType\", which was developed for use in the Canadian realm \"pre-coordinate\" coverage roles with other roles that a covered party must play in order to be eligible for coverage, e.g., \"handicapped dependent\".  Other codes in the Abstract Value Set CoveredPartyRoleType domain can be \"post-coordinated\" with the EligiblePartyRoleType codes to denote comparable concepts.  Decoupling the concepts is intended to support a wider range of concepts and semantic comparability of coded concepts.";
            case _CLAIMANTCOVEREDPARTYROLETYPE: return "DescriptionA role recognized through the eligibility of a party play a claimant for benefits covered or provided under an insurance policy.";
            case CRIMEVIC: return "Description: A person playing the role of program eligible under a program based on allegations of being the victim of a crime.\r\n\n                        \n                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is \"program eligible\" and the person's status as a crime victim meets jurisdictional or program criteria.";
            case INJWKR: return "Description: A person playing the role of program eligible under a workers compensation program based on the filing of work-related injury claim.\r\n\n                        \n                           Discussion:  This CoveredPartyRoleType.code is used when the CoveredPartyRole class code is either \"program eligible\", a \"named insured\", and \"individual insured\",  or \"dependent\", and the person's status as differently abled or \"handicapped\" meets jurisdictional, policy, or program criteria.";
            case _DEPENDENTCOVEREDPARTYROLETYPE: return "Description: A role recognized through the eligibility of a party to play a dependent for benefits covered or provided under a health insurance policy because of an association with the subscriber that is recognized by the policy underwriter.";
            case COCBEN: return "Description: A person playing the role of an individual insured with continuity of coverage under a policy which is being terminated based on loss of original status that was the basis for coverage.  Criteria for qualifying for continuity of coverage may be set by law.\r\n\n                        \n                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either \"program eligible\" or \"subscriber\" and the person's status as a continuity of coverage beneficiary meets jurisdictional or policy criteria.";
            case DIFFABL: return "Description: A person playing the role of program eligible under a program based on meeting criteria for health or functional limitation set by law or by the program.\r\n\n                        \n                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either \"program eligible\", \"named insured\", \"individual insured\", or \"dependent\", and the person's status as differently abled meets jurisdictional, policy, or program criteria.";
            case WARD: return "Description: A person, who is a minor or is deemed incompetent, who plays the role of a program eligible where eligibility for coverage is based on meeting program eligibility criteria for status as a ward of a court or jurisdiction.\r\n\n                        \n                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is a \"claimant\", \"program eligible\", a \"named insured\", an \"individual Insured\" or a \"dependent\", and the person's status as a ward meets program or policy criteria. In the case of a ward covered under a program providing financial or health benefits, a governmental agency may take temporary custody of a minor or incompetent for his/her protection and care, e.g., if the ward is suffering from neglect or abuse, or has been in trouble with the law.";
            case _INDIVIDUALINSUREDPARTYROLETYPE: return "A role recognized through the eligibility of a party to play an individual insured for benefits covered or provided under an insurance policy where the party is also the policy holder.";
            case RETIREE: return "Description: A person playing the role of an individual insured under a policy based on meeting criteria for the employment status of retired set by law or the policy.\r\n\n                        \n                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either \"program eligible\" or \"subscriber\" and the person's status as a retiree meets jurisdictional or policy criteria.";
            case _PROGRAMELIGIBLEPARTYROLETYPE: return "Description:A role recognized through the eligibility of a party to play a program eligible for benefits covered or provided under a program.";
            case INDIG: return "Description: A person playing the role of program eligible under a program based on aboriginal ancestry or as a member of an aboriginal community.\r\n\n                        \n                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is \"program eligible\" and the person's status as a member of an indigenous people meets jurisdictional or program criteria.";
            case MIL: return "Definition: A person playing the role of program eligible under a program based on military status.\r\n\n                        \n                           Discussion:  This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either \"program eligible\" or \"subscriber\" and the person's status as a member of the military meets jurisdictional or program criteria";
            case ACTMIL: return "Description: A person playing the role of program eligible under a program based on active military status.\r\n\n                        \n                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either \"program eligible\" or \"subscriber\" and the persons status as active duty military meets jurisdictional or program criteria.";
            case RETMIL: return "Description: A person playing the role of program eligible under a program based on retired military status.\r\n\n                        \n                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either \"program eligible\" or \"subscriber\" and the persons status as retired military meets jurisdictional or program criteria.";
            case VET: return "Description: A person playing the role of program eligible under a program based on status as a military veteran.\r\n\n                        \n                           Discussion: This CoveredPartyRoleType.code is typically used when the CoveredPartyRole class code is either \"program eligible\" or \"subscriber\" and the persons status as a veteran meets jurisdictional or program criteria.";
            case _SUBSCRIBERCOVEREDPARTYROLETYPE: return "Description: A role recognized through the eligibility of a party to play a subscriber for benefits covered or provided under a health insurance policy.";
            case _RESEARCHSUBJECTROLEBASIS: return "Specifies the administrative functionality within a formal experimental design for which the ResearchSubject role was established.  Examples: screening - role is used for pre-enrollment evaluation portion of the design; enrolled - role is used for subjects admitted to the active treatment portion of the design.";
            case ERL: return "Definition:The specific role being played by a research subject participating in the active treatment or primary data collection portion of a research study.";
            case SCN: return "Definition:The specific role being played by a research subject participating in the pre-enrollment evaluation portion of  a research study.";
            case _SERVICEDELIVERYLOCATIONROLETYPE: return "A role of a place that further classifies the setting (e.g., accident site, road side, work site, community location) in which services are delivered.";
            case _DEDICATEDSERVICEDELIVERYLOCATIONROLETYPE: return "A role of a place that further classifies a setting that is intended to house the provision of services.";
            case _DEDICATEDCLINICALLOCATIONROLETYPE: return "A role of a place that further classifies the clinical setting (e.g., cardiology clinic, primary care clinic, rehabilitation hospital, skilled nursing facility) in which care is delivered during an encounter.";
            case DX: return "A practice setting where diagnostic procedures or therapeutic interventions are performed";
            case CVDX: return "A practice setting where cardiovascular diagnostic procedures or therapeutic interventions are performed (e.g., cardiac catheterization lab, echocardiography suite)";
            case CATH: return "Cardiac catheterization lab";
            case ECHO: return "Echocardiography lab";
            case GIDX: return "A practice setting where GI procedures (such as endoscopies) are performed";
            case ENDOS: return "(X12N 261QD0000N)";
            case RADDX: return "A practice setting where radiology services (diagnostic or therapeutic) are provided            (X12N 261QR0200N)";
            case RADO: return "(X12N 261QX0203N)";
            case RNEU: return "Neuroradiology unit";
            case HOSP: return "An acute care institution that provides medical, surgical, or psychiatric care and treatment for the sick or the injured.";
            case CHR: return "(1) A hospital including a physical plant and personnel that provides multidisciplinary diagnosis and treatment for diseases that have one or more of the following characteristics: is permanent; leaves residual disability; is caused by nonreversible pathological alteration; requires special training of the patient for rehabilitation; and/or may be expected to require a long period of supervision or care. In addition, patients require the safety, security, and shelter of these specialized inpatient or partial hospitalization settings. (2) A hospital that provides medical and skilled nursing services to patients with long-term illnesses who are not in an acute phase but who require an intensity of services not available in nursing homes";
            case GACH: return "(X12N 282N00000N)";
            case MHSP: return "A health care facility operated by the Department of Defense or other military operation.";
            case PSYCHF: return "Healthcare facility that cares for patients with psychiatric illness(s).";
            case RH: return "(X12N 283X00000N)";
            case RHAT: return "Description: A location that plays the role of delivering services which may include life training and/or social support to people with addictions.";
            case RHII: return "Description: A location that plays the role of delivering services which may include adaptation, rehabilitation and social integration services for people with intellectual and/or pervasive development disorders such as autism or severe behaviour disorder.";
            case RHMAD: return "Description: A location that plays the role of delivering services which may social support services for adolescents who are pregnant or have child and are experiencing adaptation issues/difficulties in their current or eventual parenting role.";
            case RHPI: return "Description: A location that plays the role of delivering services which may include adaptation, rehabilitation and social integration services for people with physical impairments.";
            case RHPIH: return "Description: A location that plays the role of delivering services for people with hearing impairments.";
            case RHPIMS: return "Description: A location that plays the role of delivering services for people with motor skill impairments.";
            case RHPIVS: return "Description: A location that plays the role of delivering services for people with visual skill impairments.";
            case RHYAD: return "Description: A location that plays the role of delivering services which may include life training and/or social support services for the adaption, rehabilitation and social integration of youths with adjustment difficulties.";
            case HU: return "Hospital unit";
            case BMTU: return "Bone marrow transplant unit";
            case CCU: return "Coronary care unit";
            case CHEST: return "A specialty unit in hospital that focuses on chronic respirator patients and pulmonary failure";
            case EPIL: return "Epilepsy unit";
            case ER: return "The section of a health care facility for providing rapid treatment to victims of sudden illness or trauma.";
            case ETU: return "Emergency trauma unit";
            case HD: return "Hemodialysis unit";
            case HLAB: return "Description: A location that plays the role of delivering services which may include tests done based on clinical specimens to get health information about a patient as pertaining to the diagnosis, treatment and prevention of disease.  Hospital laboratories may be further divided into specialized units such as Anatomic Pathology, Microbiology, and Biochemistry.";
            case INLAB: return "Description: A location that plays the role of delivering services which may include tests are done on clinical specimens to get health information about a patient pertaining to the diagnosis, treatment, and prevention of disease for a hospital visit longer than one day.";
            case OUTLAB: return "Description: A location that plays the role of delivering services which may include tests are done on clinical specimens to get health information about a patient pertaining to the diagnosis, treatment, and prevention of disease for same day visits.";
            case HRAD: return "Description: A location that plays the role of delivering services which may include the branch of medicine that uses ionizing and non-ionizing radiation to diagnose and treat diseases.  The radiology unit may be further divided into subspecialties such as Imaging, Cardiovascular, Thoracic, and Ultrasound.";
            case HUSCS: return "Description: A location that plays the role of delivering services which may include collecting specimens and/or samples from patients for laboratory testing purposes, but does not perform any tests or analysis functions.";
            case ICU: return "Intensive care unit";
            case PEDICU: return "Pediatric intensive care unit";
            case PEDNICU: return "Pediatric neonatal intensive care unit";
            case INPHARM: return "Description: A location that plays the role of delivering services which may include providing judicious, safe, efficacious, appropriate and cost effective use of medicines for treatment of patients for visits longer than one day. The distinction between inpatient pharmacies and retail (or outpatient) pharmacies is that they are part of a patient's continuity of care while staying in the hospital.";
            case MBL: return "Description: A location that plays the role of delivering services which include biochemistry, hematology, microbiology, immunochemistry, and toxicology.";
            case NCCS: return "Neurology critical care and stroke unit";
            case NS: return "Neurosurgery unit";
            case OUTPHARM: return "Description: A location that plays the role of delivering services which may include providing judicious, safe, efficacious, appropriate and cost effective use of medicines for treatment of patients for outpatient visits and may also be used for discharge prescriptions.";
            case PEDU: return "Pediatric unit";
            case PHU: return "(X12N 273R00000N)";
            case RHU: return "Rehabilitation hospital unit";
            case SLEEP: return "(X12N 261QA1200N)";
            case NCCF: return "Nursing or custodial care facility";
            case SNF: return "(X12N 314000000N)";
            case OF: return "Outpatient facility";
            case ALL: return "Allergy clinic";
            case AMPUT: return "Amputee clinic";
            case BMTC: return "Bone marrow transplant clinic";
            case BREAST: return "Breast clinic";
            case CANC: return "Child and adolescent neurology clinic";
            case CAPC: return "Child and adolescent psychiatry clinic";
            case CARD: return "Ambulatory Health Care Facilities; Clinic/Center; Rehabilitation: Cardiac Facilities";
            case PEDCARD: return "Pediatric cardiology clinic";
            case COAG: return "Coagulation clinic";
            case CRS: return "Colon and rectal surgery clinic";
            case DERM: return "Dermatology clinic";
            case ENDO: return "Endocrinology clinic";
            case PEDE: return "Pediatric endocrinology clinic";
            case ENT: return "Otorhinolaryngology clinic";
            case FMC: return "Family medicine clinic";
            case GI: return "Gastroenterology clinic";
            case PEDGI: return "Pediatric gastroenterology clinic";
            case GIM: return "General internal medicine clinic";
            case GYN: return "Gynecology clinic";
            case HEM: return "Hematology clinic";
            case PEDHEM: return "Pediatric hematology clinic";
            case HTN: return "Hypertension clinic";
            case IEC: return "Focuses on assessing disability";
            case INFD: return "Infectious disease clinic";
            case PEDID: return "Pediatric infectious disease clinic";
            case INV: return "Infertility clinic";
            case LYMPH: return "Lympedema clinic";
            case MGEN: return "Medical genetics clinic";
            case NEPH: return "Nephrology clinic";
            case PEDNEPH: return "Pediatric nephrology clinic";
            case NEUR: return "Neurology clinic";
            case OB: return "Obstetrics clinic";
            case OMS: return "Oral and maxillofacial surgery clinic";
            case ONCL: return "Medical oncology clinic";
            case PEDHO: return "Pediatric oncology clinic";
            case OPH: return "Opthalmology clinic";
            case OPTC: return "Description: A location that plays the role of delivering services which may include examination, diagnosis, treatment, management, and prevention of diseases and disorders of the eye as well as prescribing and fitting appropriate corrective lenses (glasses or contact lenses) as needed.  Optometry clinics may also provide tests for visual field screening, measuring intra-ocular pressure and ophthalmoscopy, as and when required.";
            case ORTHO: return "Orthopedics clinic";
            case HAND: return "Hand clinic";
            case PAINCL: return "(X12N 261QP3300N)";
            case PC: return "(X12N 261QP2300N)";
            case PEDC: return "Pediatrics clinic";
            case PEDRHEUM: return "Pediatric rheumatology clinic";
            case POD: return "(X12N 261QP1100N)";
            case PREV: return "Preventive medicine clinic";
            case PROCTO: return "Proctology clinic";
            case PROFF: return "Location where healthcare service was delivered, identified as the healthcare provider's practice office.";
            case PROS: return "Prosthodontics clinic";
            case PSI: return "Psychology clinic";
            case PSY: return "Psychiatry clinic";
            case RHEUM: return "Rheumatology clinic";
            case SPMED: return "Sports medicine clinic";
            case SU: return "Surgery clinic";
            case PLS: return "Plastic surgery clinic";
            case URO: return "Urology clinic";
            case TR: return "Transplant clinic";
            case TRAVEL: return "Travel and geographic medicine clinic";
            case WND: return "Wound clinic";
            case RTF: return "Residential treatment facility";
            case PRC: return "Pain rehabilitation center";
            case SURF: return "(X12N 324500000N)";
            case _DEDICATEDNONCLINICALLOCATIONROLETYPE: return "A role of a place that further classifies a setting that is intended to house the provision of non-clinical services.";
            case DADDR: return "Location address where medical supplies were transported to for use.";
            case MOBL: return "Location (mobile) where healthcare service was delivered.";
            case AMB: return "Location (mobile) where healthcare service was delivered, identified specifically as an ambulance.";
            case PHARM: return "Location where healthcare service was delivered, identified as a pharmacy.";
            case _INCIDENTALSERVICEDELIVERYLOCATIONROLETYPE: return "IncidentalServiceDeliveryLocationRoleType";
            case ACC: return "Location of an accident where healthcare service was delivered, such as a roadside.";
            case COMM: return "Community location where healthcare is delivered.";
            case CSC: return "Description: A location that plays the role of delivering services which may include providing front-line services to the population of a defined geographic area such as: healthcare services and social services, preventive or curative, rehabilitation or reintegration.";
            case PTRES: return "location where healthcare was delivered which is the residence of the Patient.";
            case SCHOOL: return "Location where healthcare service was delivered, identified as a school or educational facility.";
            case UPC: return "Description: A location that plays the role of delivering services which may include: social emergency services required for a young person as required under any jurisdictional youth laws, child placement, and family mediation in the defined geographical area the SDL is responsible for. It may provide expertise in a judiciary setting on child custody, adoption and biological history research.";
            case WORK: return "Location where healthcare service was delivered, identified as a work place.";
            case _SPECIMENROLETYPE: return "SpecimenRoleType";
            case C: return "A specimen used for initial calibration settings of an instrument";
            case G: return "A set of patient samples in which the individuals of the group may or may not be identified.";
            case L: return "Aliquots of individual specimens combined to form a single specimen representing all of the included individuals.";
            case P: return "A specimen that has been collected from a patient.";
            case Q: return "A specimen specifically used to verify the sensitivity, specificity, accuracy or other perfomance parameter of a diagnostic test.";
            case B: return "Quality Control specimen submitted to the lab whose identity and composition is not known to the lab.";
            case E: return "An electronically simulated QC specimen";
            case F: return "Specimen used for testing proficiency of an organization performing testing (how does this differ from O?)";
            case O: return "A specimen used for evaluation of operator proficiency (operator in what context?)";
            case V: return "A specimen used for periodic calibration checks of instruments";
            case R: return "A portion of an original patent sample that is tested at the same time as the original sample";
            case CLAIM: return "A party that makes a claim for coverage under a policy.";
            case COMMUNITYLABORATORY: return "Community Laboratory";
            case GT: return "An individual or organization that makes or gives a promise, assurance, pledge to pay or has paid the healthcare service provider.";
            case HOMEHEALTH: return "Home Health";
            case LABORATORY: return "Laboratory";
            case PATHOLOGIST: return "Pathologist";
            case PH: return "Policy holder for the insurance policy.";
            case PHLEBOTOMIST: return "Phlebotomist";
            case PROG: return "A party that meets the eligibility criteria for coverage under a program.";
            case PT: return "The recipient for the service(s) and/or product(s) when they are not the covered party.";
            case SUBJECT: return "Self";
            case THIRDPARTY: return "Third Party";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _AFFILIATIONROLETYPE: return "AffiliationRoleType";
            case _COVERAGESPONSORROLETYPE: return "CoverageSponsorRoleType";
            case FULLINS: return "Fully insured coverage sponsor";
            case SELFINS: return "Self insured coverage sponsor";
            case _PAYORROLETYPE: return "PayorRoleType";
            case ENROLBKR: return "Enrollment Broker";
            case TPA: return "Third party administrator";
            case UMO: return "Utilization management organization";
            case RESPRSN: return "responsible party";
            case EXCEST: return "executor of estate";
            case GUADLTM: return "guardian ad lidem";
            case GUARD: return "guardian";
            case POWATT: return "power of attorney";
            case DPOWATT: return "durable power of attorney";
            case HPOWATT: return "healthcare power of attorney";
            case SPOWATT: return "special power of attorney";
            case _ASSIGNEDROLETYPE: return "AssignedRoleType";
            case _ASSIGNEDNONPERSONLIVINGSUBJECTROLETYPE: return "AssignedNonPersonLivingSubjectRoleType";
            case ASSIST: return "Assistive non-person living subject";
            case BIOTH: return "Biotherapeutic non-person living subject";
            case ANTIBIOT: return "Antibiotic";
            case DEBR: return "Debridement";
            case CCO: return "Clinical Companion";
            case SEE: return "Seeing";
            case SNIFF: return "Sniffing";
            case _CITIZENROLETYPE: return "CitizenRoleType";
            case CAS: return "asylum seeker";
            case CASM: return "single minor asylum seeker";
            case CN: return "national";
            case CNRP: return "non-country member without residence permit";
            case CNRPM: return "non-country member minor without residence permit";
            case CPCA: return "permit card applicant";
            case CRP: return "non-country member with residence permit";
            case CRPM: return "non-country member minor with residence permit";
            case _CONTACTROLETYPE: return "ContactRoleType";
            case _ADMINISTRATIVECONTACTROLETYPE: return "AdministrativeContactRoleType";
            case BILL: return "Billing Contact";
            case ORG: return "organizational contact";
            case PAYOR: return "Payor Contact";
            case ECON: return "emergency contact";
            case NOK: return "next of kin";
            case _IDENTIFIEDENTITYTYPE: return "IdentifiedEntityType";
            case _LOCATIONIDENTIFIEDENTITYROLECODE: return "LocationIdentifiedEntityRoleCode";
            case ACHFID: return "accreditation location identifier";
            case JURID: return "jurisdiction location identifier";
            case LOCHFID: return "local location identifier";
            case _LIVINGSUBJECTPRODUCTIONCLASS: return "LivingSubjectProductionClass";
            case BF: return "Beef";
            case BL: return "Broiler";
            case BR: return "Breeder";
            case CO: return "Companion";
            case DA: return "Dairy";
            case DR: return "Draft";
            case DU: return "Dual";
            case FI: return "Fiber";
            case LY: return "Layer";
            case MT: return "Meat";
            case MU: return "Multiplier";
            case PL: return "Pleasure";
            case RC: return "Racing";
            case SH: return "Show";
            case VL: return "Veal";
            case WL: return "Wool";
            case WO: return "Working";
            case _MEDICATIONGENERALIZATIONROLETYPE: return "MedicationGeneralizationRoleType";
            case DC: return "therapeutic class";
            case GD: return "generic drug";
            case GDF: return "generic drug form";
            case GDS: return "generic drug strength";
            case GDSF: return "generic drug strength form";
            case MGDSF: return "manufactured drug strength form";
            case _MEMBERROLETYPE: return "MemberRoleType";
            case TRB: return "Tribal Member";
            case _PERSONALRELATIONSHIPROLETYPE: return "PersonalRelationshipRoleType";
            case FAMMEMB: return "family member";
            case CHILD: return "child";
            case CHLDADOPT: return "adopted child";
            case DAUADOPT: return "adopted daughter";
            case SONADOPT: return "adopted son";
            case CHLDFOST: return "foster child";
            case DAUFOST: return "foster daughter";
            case SONFOST: return "foster son";
            case DAUC: return "daughter";
            case DAU: return "natural daughter";
            case STPDAU: return "stepdaughter";
            case NCHILD: return "natural child";
            case SON: return "natural son";
            case SONC: return "son";
            case STPSON: return "stepson";
            case STPCHLD: return "step child";
            case EXT: return "extended family member";
            case AUNT: return "aunt";
            case MAUNT: return "maternal aunt";
            case PAUNT: return "paternal aunt";
            case COUSN: return "cousin";
            case MCOUSN: return "maternal cousin";
            case PCOUSN: return "paternal cousin";
            case GGRPRN: return "great grandparent";
            case GGRFTH: return "great grandfather";
            case MGGRFTH: return "maternal great-grandfather";
            case PGGRFTH: return "paternal great-grandfather";
            case GGRMTH: return "great grandmother";
            case MGGRMTH: return "maternal great-grandmother";
            case PGGRMTH: return "paternal great-grandmother";
            case MGGRPRN: return "maternal great-grandparent";
            case PGGRPRN: return "paternal great-grandparent";
            case GRNDCHILD: return "grandchild";
            case GRNDDAU: return "granddaughter";
            case GRNDSON: return "grandson";
            case GRPRN: return "grandparent";
            case GRFTH: return "grandfather";
            case MGRFTH: return "maternal grandfather";
            case PGRFTH: return "paternal grandfather";
            case GRMTH: return "grandmother";
            case MGRMTH: return "maternal grandmother";
            case PGRMTH: return "paternal grandmother";
            case MGRPRN: return "maternal grandparent";
            case PGRPRN: return "paternal grandparent";
            case INLAW: return "inlaw";
            case CHLDINLAW: return "child-in-law";
            case DAUINLAW: return "daughter in-law";
            case SONINLAW: return "son in-law";
            case PRNINLAW: return "parent in-law";
            case FTHINLAW: return "father-in-law";
            case MTHINLAW: return "mother-in-law";
            case SIBINLAW: return "sibling in-law";
            case BROINLAW: return "brother-in-law";
            case SISINLAW: return "sister-in-law";
            case NIENEPH: return "niece/nephew";
            case NEPHEW: return "nephew";
            case NIECE: return "niece";
            case UNCLE: return "uncle";
            case MUNCLE: return "maternal uncle";
            case PUNCLE: return "paternal uncle";
            case PRN: return "parent";
            case ADOPTP: return "adoptive parent";
            case ADOPTF: return "adoptive father";
            case ADOPTM: return "adoptive mother";
            case FTH: return "father";
            case FTHFOST: return "foster father";
            case NFTH: return "natural father";
            case NFTHF: return "natural father of fetus";
            case STPFTH: return "stepfather";
            case MTH: return "mother";
            case GESTM: return "gestational mother";
            case MTHFOST: return "foster mother";
            case NMTH: return "natural mother";
            case NMTHF: return "natural mother of fetus";
            case STPMTH: return "stepmother";
            case NPRN: return "natural parent";
            case PRNFOST: return "foster parent";
            case STPPRN: return "step parent";
            case SIB: return "sibling";
            case BRO: return "brother";
            case HBRO: return "half-brother";
            case NBRO: return "natural brother";
            case TWINBRO: return "twin brother";
            case FTWINBRO: return "fraternal twin brother";
            case ITWINBRO: return "identical twin brother";
            case STPBRO: return "stepbrother";
            case HSIB: return "half-sibling";
            case HSIS: return "half-sister";
            case NSIB: return "natural sibling";
            case NSIS: return "natural sister";
            case TWINSIS: return "twin sister";
            case FTWINSIS: return "fraternal twin sister";
            case ITWINSIS: return "identical twin sister";
            case TWIN: return "twin";
            case FTWIN: return "fraternal twin";
            case ITWIN: return "identical twin";
            case SIS: return "sister";
            case STPSIS: return "stepsister";
            case STPSIB: return "step sibling";
            case SIGOTHR: return "significant other";
            case DOMPART: return "domestic partner";
            case FMRSPS: return "former spouse";
            case SPS: return "spouse";
            case HUSB: return "husband";
            case WIFE: return "wife";
            case FRND: return "unrelated friend";
            case NBOR: return "neighbor";
            case ONESELF: return "self";
            case ROOM: return "Roommate";
            case _POLICYORPROGRAMCOVERAGEROLETYPE: return "PolicyOrProgramCoverageRoleType";
            case _COVERAGEROLETYPE: return "CoverageRoleType";
            case FAMDEP: return "family dependent";
            case HANDIC: return "handicapped dependent";
            case INJ: return "injured plaintiff";
            case SELF: return "self";
            case SPON: return "sponsored dependent";
            case STUD: return "student";
            case FSTUD: return "full-time student";
            case PSTUD: return "part-time student";
            case _COVEREDPARTYROLETYPE: return "covered party role type";
            case _CLAIMANTCOVEREDPARTYROLETYPE: return "ClaimantCoveredPartyRoleType";
            case CRIMEVIC: return "crime victim";
            case INJWKR: return "injured worker";
            case _DEPENDENTCOVEREDPARTYROLETYPE: return "DependentCoveredPartyRoleType";
            case COCBEN: return "continuity of coverage beneficiary";
            case DIFFABL: return "differently abled";
            case WARD: return "ward";
            case _INDIVIDUALINSUREDPARTYROLETYPE: return "IndividualInsuredPartyRoleType";
            case RETIREE: return "retiree";
            case _PROGRAMELIGIBLEPARTYROLETYPE: return "ProgramEligiblePartyRoleType";
            case INDIG: return "member of an indigenous people";
            case MIL: return "military";
            case ACTMIL: return "active duty military";
            case RETMIL: return "retired military";
            case VET: return "veteran";
            case _SUBSCRIBERCOVEREDPARTYROLETYPE: return "SubscriberCoveredPartyRoleType";
            case _RESEARCHSUBJECTROLEBASIS: return "ResearchSubjectRoleBasis";
            case ERL: return "enrollment";
            case SCN: return "screening";
            case _SERVICEDELIVERYLOCATIONROLETYPE: return "ServiceDeliveryLocationRoleType";
            case _DEDICATEDSERVICEDELIVERYLOCATIONROLETYPE: return "DedicatedServiceDeliveryLocationRoleType";
            case _DEDICATEDCLINICALLOCATIONROLETYPE: return "DedicatedClinicalLocationRoleType";
            case DX: return "Diagnostics or therapeutics unit";
            case CVDX: return "Cardiovascular diagnostics or therapeutics unit";
            case CATH: return "Cardiac catheterization lab";
            case ECHO: return "Echocardiography lab";
            case GIDX: return "Gastroenterology diagnostics or therapeutics lab";
            case ENDOS: return "Endoscopy lab";
            case RADDX: return "Radiology diagnostics or therapeutics unit";
            case RADO: return "Radiation oncology unit";
            case RNEU: return "Neuroradiology unit";
            case HOSP: return "Hospital";
            case CHR: return "Chronic Care Facility";
            case GACH: return "Hospitals; General Acute Care Hospital";
            case MHSP: return "Military Hospital";
            case PSYCHF: return "Psychatric Care Facility";
            case RH: return "Rehabilitation hospital";
            case RHAT: return "addiction treatment center";
            case RHII: return "intellectual impairment center";
            case RHMAD: return "parents with adjustment difficulties center";
            case RHPI: return "physical impairment center";
            case RHPIH: return "physical impairment - hearing center";
            case RHPIMS: return "physical impairment - motor skills center";
            case RHPIVS: return "physical impairment - visual skills center";
            case RHYAD: return "youths with adjustment difficulties center";
            case HU: return "Hospital unit";
            case BMTU: return "Bone marrow transplant unit";
            case CCU: return "Coronary care unit";
            case CHEST: return "Chest unit";
            case EPIL: return "Epilepsy unit";
            case ER: return "Emergency room";
            case ETU: return "Emergency trauma unit";
            case HD: return "Hemodialysis unit";
            case HLAB: return "hospital laboratory";
            case INLAB: return "inpatient laboratory";
            case OUTLAB: return "outpatient laboratory";
            case HRAD: return "radiology unit";
            case HUSCS: return "specimen collection site";
            case ICU: return "Intensive care unit";
            case PEDICU: return "Pediatric intensive care unit";
            case PEDNICU: return "Pediatric neonatal intensive care unit";
            case INPHARM: return "inpatient pharmacy";
            case MBL: return "medical laboratory";
            case NCCS: return "Neurology critical care and stroke unit";
            case NS: return "Neurosurgery unit";
            case OUTPHARM: return "outpatient pharmacy";
            case PEDU: return "Pediatric unit";
            case PHU: return "Psychiatric hospital unit";
            case RHU: return "Rehabilitation hospital unit";
            case SLEEP: return "Sleep disorders unit";
            case NCCF: return "Nursing or custodial care facility";
            case SNF: return "Skilled nursing facility";
            case OF: return "Outpatient facility";
            case ALL: return "Allergy clinic";
            case AMPUT: return "Amputee clinic";
            case BMTC: return "Bone marrow transplant clinic";
            case BREAST: return "Breast clinic";
            case CANC: return "Child and adolescent neurology clinic";
            case CAPC: return "Child and adolescent psychiatry clinic";
            case CARD: return "Ambulatory Health Care Facilities; Clinic/Center; Rehabilitation: Cardiac Facilities";
            case PEDCARD: return "Pediatric cardiology clinic";
            case COAG: return "Coagulation clinic";
            case CRS: return "Colon and rectal surgery clinic";
            case DERM: return "Dermatology clinic";
            case ENDO: return "Endocrinology clinic";
            case PEDE: return "Pediatric endocrinology clinic";
            case ENT: return "Otorhinolaryngology clinic";
            case FMC: return "Family medicine clinic";
            case GI: return "Gastroenterology clinic";
            case PEDGI: return "Pediatric gastroenterology clinic";
            case GIM: return "General internal medicine clinic";
            case GYN: return "Gynecology clinic";
            case HEM: return "Hematology clinic";
            case PEDHEM: return "Pediatric hematology clinic";
            case HTN: return "Hypertension clinic";
            case IEC: return "Impairment evaluation center";
            case INFD: return "Infectious disease clinic";
            case PEDID: return "Pediatric infectious disease clinic";
            case INV: return "Infertility clinic";
            case LYMPH: return "Lympedema clinic";
            case MGEN: return "Medical genetics clinic";
            case NEPH: return "Nephrology clinic";
            case PEDNEPH: return "Pediatric nephrology clinic";
            case NEUR: return "Neurology clinic";
            case OB: return "Obstetrics clinic";
            case OMS: return "Oral and maxillofacial surgery clinic";
            case ONCL: return "Medical oncology clinic";
            case PEDHO: return "Pediatric oncology clinic";
            case OPH: return "Opthalmology clinic";
            case OPTC: return "optometry clinic";
            case ORTHO: return "Orthopedics clinic";
            case HAND: return "Hand clinic";
            case PAINCL: return "Pain clinic";
            case PC: return "Primary care clinic";
            case PEDC: return "Pediatrics clinic";
            case PEDRHEUM: return "Pediatric rheumatology clinic";
            case POD: return "Podiatry clinic";
            case PREV: return "Preventive medicine clinic";
            case PROCTO: return "Proctology clinic";
            case PROFF: return "Provider's Office";
            case PROS: return "Prosthodontics clinic";
            case PSI: return "Psychology clinic";
            case PSY: return "Psychiatry clinic";
            case RHEUM: return "Rheumatology clinic";
            case SPMED: return "Sports medicine clinic";
            case SU: return "Surgery clinic";
            case PLS: return "Plastic surgery clinic";
            case URO: return "Urology clinic";
            case TR: return "Transplant clinic";
            case TRAVEL: return "Travel and geographic medicine clinic";
            case WND: return "Wound clinic";
            case RTF: return "Residential treatment facility";
            case PRC: return "Pain rehabilitation center";
            case SURF: return "Substance use rehabilitation facility";
            case _DEDICATEDNONCLINICALLOCATIONROLETYPE: return "DedicatedNonClinicalLocationRoleType";
            case DADDR: return "Delivery Address";
            case MOBL: return "Mobile Unit";
            case AMB: return "Ambulance";
            case PHARM: return "Pharmacy";
            case _INCIDENTALSERVICEDELIVERYLOCATIONROLETYPE: return "IncidentalServiceDeliveryLocationRoleType";
            case ACC: return "accident site";
            case COMM: return "Community Location";
            case CSC: return "community service center";
            case PTRES: return "Patient's Residence";
            case SCHOOL: return "school";
            case UPC: return "underage protection center";
            case WORK: return "work site";
            case _SPECIMENROLETYPE: return "SpecimenRoleType";
            case C: return "Calibrator";
            case G: return "Group";
            case L: return "Pool";
            case P: return "Patient";
            case Q: return "Quality Control";
            case B: return "Blind";
            case E: return "Electronic QC";
            case F: return "Filler Proficiency";
            case O: return "Operator Proficiency";
            case V: return "Verifying";
            case R: return "Replicate";
            case CLAIM: return "claimant";
            case COMMUNITYLABORATORY: return "Community Laboratory";
            case GT: return "Guarantor";
            case HOMEHEALTH: return "Home Health";
            case LABORATORY: return "Laboratory";
            case PATHOLOGIST: return "Pathologist";
            case PH: return "Policy Holder";
            case PHLEBOTOMIST: return "Phlebotomist";
            case PROG: return "program eligible";
            case PT: return "Patient";
            case SUBJECT: return "Self";
            case THIRDPARTY: return "Third Party";
            default: return "?";
          }
    }


}

