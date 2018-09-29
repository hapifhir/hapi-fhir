package org.hl7.fhir.r4.model.codesystems;

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

// Generated on Thu, Sep 13, 2018 09:04-0400 for FHIR v3.5.0


import org.hl7.fhir.exceptions.FHIRException;

public enum V3Hl7PublishingDomain {

        /**
         * Description: Represents the HL7 content "domain" that supports accounting and billing functions - and "provides support for the creation and management of patient billing accounts and the post of financial transactions against patient billing accounts for the purpose of aggregating financial transactions that will be submitted as claims or invoices for reimbursemen"

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        AB, 
        /**
         * Description: Represents the HL7 content "domain" that supports trigger event control act infrastructure - and "covers the alternate structures of the message Trigger Event Control Acts in the HL7 Composite Message."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        AI, 
        /**
         * Description: Represents the HL7 content "domain" that was defined as an "artificial listing" domain to support publication testing.
         */
        AL, 
        /**
         * Description: Represents the HL7 content "domain" that supports blood tissue and organ domain - and "comprises the models, messages, and other artIfacts that are needed to support messaging related to the process of blood, tissue, and organ banking operations such as donations, eligibility, storage, dispense, administration/transfusion, explantation, and implantation. "

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        BB, 
        /**
         * Description: Represents the HL7 content "domain" that supports the clinical document architecture.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        CD, 
        /**
         * Description: Represents the HL7 content "domain" that supports clinical genomics - and includes " standards to enable the exchange of interrelated clinical and personalized genomic data between interested parties."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        CG, 
        /**
         * Description: Represents the HL7 content "domain" that supports transmission infrastructure - and " is primarily concerned with the data content of exchanges between healthcare applications, the sequence or interrelationships in the flow of messages and the communication of significant application level exceptions or error conditions."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        CI, 
        /**
         * Description: Represents the HL7 content "domain" that supports Coverage - and provides support for managing health care coverage in the reimbursement system(s).

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        CO, 
        /**
         * Description: Represents the HL7 content "domain" that supports the common product model - which "is used to improve the alignment between the different representations of products used within the body of HL7 Version 3 models."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        CP, 
        /**
         * Description: Represents the HL7 content "domain" that supports Claims and Reimbursement - and "provides support for Generic, Pharmacy, Preferred Accommodation, Physician, Oral Health Vision Care and Hospital claims for eligibility, authorization, coverage extension, pre-determination, invoice adjudication, payment advice and Statement of Financial Activity (SOFA) Release 3 of this document adds claims messaging support for Physician, Oral Health Vision Care and Hospital claims."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        CR, 
        /**
         * Description: Represents the HL7 content "domain" that supports a common clinical statement pattern - and "is a 'pattern' designed to be used within multiple HL7 Version 3 domain models. This pattern is intended to facilitate the consistent design of communications that convey clinical information to meet specific use cases."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        CS, 
        /**
         * Description: Represents the HL7 content "domain" that supports common model types - and "are a work product produced by a particular committee for expressing a common, useful and reusable concept."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        CT, 
        /**
         * Description: Represents the HL7 content "domain" that was created to support testing and initial set-up functions.
         */
        DD, 
        /**
         * Description: This domain has been retired in favor of "imaging integration" (II).
         */
        DI, 
        /**
         * Description: Represents the HL7 content "domain" that provides decision support.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        DS, 
        /**
         * Description: Represents the HL7 content "domain" that supports Emergency Medical Services.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        EM, 
        /**
         * Description: Represents the HL7 content "domain" that supports imaging integration - and is "comprises the models, implementation guides, sample documents and images that are needed to illustrate the transformation of DICOM structured reports to CDA Release 2 as well as the creation of CDA diagnostic imaging reports."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        II, 
        /**
         * Description: Represents the HL7 content "domain" that supports immunization - and "describes communication of information about immunization: the administration of vaccines (and/or antisera) to individuals to prevent infectious disease."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        IZ, 
        /**
         * Description: Represents the HL7 content "domain" that supports clinical laboratory functions - and is "comprises the models, messages, and other artifacts that are needed to support messaging related to laboratory tests or observations. "

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        LB, 
        /**
         * Description: Represents the HL7 content "domain" that supports medication - and  "deals with the description of a medicine for the purposes of messaging information about medicines" and the applications of these descriptions.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        ME, 
        /**
         * Description: Represents the HL7 content "domain" that supports master file infrastructure - and is "comprises the classes and attributes needed to support Master Files and Registries."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        MI, 
        /**
         * Description: Represents the HL7 content "domain" that supports Materials Management - and is "supports the simple scenario of a Materials Management application sending requests, notifications and queries to an auxiliary application. The intent is to establish a standard for the minimum functionality that is useful and comprehensive enough to explore the important concepts relative to inventory management."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        MM, 
        /**
         * Description: Represents the HL7 content "domain" that supports medical records - and is "supports clinical document management, and document querying."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        MR, 
        /**
         * Description: Represents the HL7 content "domain" that supports shared messages - and "are a work product produced for expressing common, useful and reusable message types."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        MT, 
        /**
         * Description: Represents the HL7 content "domain" that supports observations - and is "comprises the models, messages, and other artifacts that are needed to support messaging related to resulting basic healthcare diagnostic services. "

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        OB, 
        /**
         * Description: Represents the HL7 content "domain" that supports orders and observations - and will provide over-arching support information for the "Orders" (OR) and "Observations" (OB) domains.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        OO, 
        /**
         * Description: Represents the HL7 content "domain" that supports orders - and "comprises the models, messages, and other artifacts that are needed to support messaging related to ordering basic healthcare services."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        OR, 
        /**
         * Description: Represents the HL7 content "domain" that supports Patient Administration - and "defines person and patient demographics and visit information about patients"

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        PA, 
        /**
         * Description: Represents the HL7 content "domain" that supports Care Provision - and "addresses the information that is needed for the ongoing care of individuals, populations, and other targets of care."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        PC, 
        /**
         * Description: Represents the HL7 content "domain" that supports public health - and is "the source of a number of Common Model Element Types (CMET) designed to meet the needs of public health data exchange."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        PH, 
        /**
         * Description: Represents the HL7 content "domain" that supports Personnel Management - and "spans a variety of clinical-administrative information functions associated with the organizations, individuals, animals and devices involved in the delivery and support of healthcare services."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        PM, 
        /**
         * Description: Represents the HL7 content "domain" that supports query infrastructure - and "specifies the formation of information queries and the responses to these queries to meet the needs of healthcare applications using the HL7 version 3 messaging standard."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        QI, 
        /**
         * Description: Represents the HL7 content "domain" that supports Quality Measures - and "is a standard for representing a health quality measure as an electronic document."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        QM, 
        /**
         * Description: Represents the HL7 content "domain" that supports Registries - and "collects HL7 artifacts for administrative  registries."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        RG, 
        /**
         * Description: Represents the HL7 content "domain" that supports Informative Public Health.

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        RI, 
        /**
         * Description: Represents the HL7 content "domain" that supports Regulated Products - and "includes standards developed as part of the family of messages targeted for the exchange of information about regulated products and the exchange of the data needed to provide approval for such products."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        RP, 
        /**
         * Description: Represents the HL7 content "domain" that supports Public Health Reporting - and "includes messages and documents that are specifically designed to support managment, reporting and investigation in the public health context."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        RR, 
        /**
         * Description: Represents the HL7 content "domain" that supports Regulated Studies - and is "includes standards developed as part of the family of messages targeted for the exchange of information about the conduct of regulated studies, and the exchange of the data collected during those studies."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        RT, 
        /**
         * Description: Represents the HL7 content "domain" that supports pharmacy - and is a "model used to derive message patterns to describe and communicate processes related to medication."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        RX, 
        /**
         * Description: Represents the HL7 content "domain" that supports Scheduling - and "offers a generic set of messages and behavior to implement any number of Scheduling scenarios."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        SC, 
        /**
         * Description: Represents the HL7 content "domain" that supports Specimen - and "comprises the models and artifacts that are needed to support the creation of messaging related to specimen."

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        SP, 
        /**
         * Description: Represents the HL7 content "domain" that supports Therapeutic Devices - and is "comprises the models, messages, and other artifacts that are needed to support messaging related to therapy delivery and observations made by a medical device. "

                        
                           UsageNote: V3 Specifications are published in a set of "domains", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.
         */
        TD, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3Hl7PublishingDomain fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("AB".equals(codeString))
          return AB;
        if ("AI".equals(codeString))
          return AI;
        if ("AL".equals(codeString))
          return AL;
        if ("BB".equals(codeString))
          return BB;
        if ("CD".equals(codeString))
          return CD;
        if ("CG".equals(codeString))
          return CG;
        if ("CI".equals(codeString))
          return CI;
        if ("CO".equals(codeString))
          return CO;
        if ("CP".equals(codeString))
          return CP;
        if ("CR".equals(codeString))
          return CR;
        if ("CS".equals(codeString))
          return CS;
        if ("CT".equals(codeString))
          return CT;
        if ("DD".equals(codeString))
          return DD;
        if ("DI".equals(codeString))
          return DI;
        if ("DS".equals(codeString))
          return DS;
        if ("EM".equals(codeString))
          return EM;
        if ("II".equals(codeString))
          return II;
        if ("IZ".equals(codeString))
          return IZ;
        if ("LB".equals(codeString))
          return LB;
        if ("ME".equals(codeString))
          return ME;
        if ("MI".equals(codeString))
          return MI;
        if ("MM".equals(codeString))
          return MM;
        if ("MR".equals(codeString))
          return MR;
        if ("MT".equals(codeString))
          return MT;
        if ("OB".equals(codeString))
          return OB;
        if ("OO".equals(codeString))
          return OO;
        if ("OR".equals(codeString))
          return OR;
        if ("PA".equals(codeString))
          return PA;
        if ("PC".equals(codeString))
          return PC;
        if ("PH".equals(codeString))
          return PH;
        if ("PM".equals(codeString))
          return PM;
        if ("QI".equals(codeString))
          return QI;
        if ("QM".equals(codeString))
          return QM;
        if ("RG".equals(codeString))
          return RG;
        if ("RI".equals(codeString))
          return RI;
        if ("RP".equals(codeString))
          return RP;
        if ("RR".equals(codeString))
          return RR;
        if ("RT".equals(codeString))
          return RT;
        if ("RX".equals(codeString))
          return RX;
        if ("SC".equals(codeString))
          return SC;
        if ("SP".equals(codeString))
          return SP;
        if ("TD".equals(codeString))
          return TD;
        throw new FHIRException("Unknown V3Hl7PublishingDomain code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case AB: return "AB";
            case AI: return "AI";
            case AL: return "AL";
            case BB: return "BB";
            case CD: return "CD";
            case CG: return "CG";
            case CI: return "CI";
            case CO: return "CO";
            case CP: return "CP";
            case CR: return "CR";
            case CS: return "CS";
            case CT: return "CT";
            case DD: return "DD";
            case DI: return "DI";
            case DS: return "DS";
            case EM: return "EM";
            case II: return "II";
            case IZ: return "IZ";
            case LB: return "LB";
            case ME: return "ME";
            case MI: return "MI";
            case MM: return "MM";
            case MR: return "MR";
            case MT: return "MT";
            case OB: return "OB";
            case OO: return "OO";
            case OR: return "OR";
            case PA: return "PA";
            case PC: return "PC";
            case PH: return "PH";
            case PM: return "PM";
            case QI: return "QI";
            case QM: return "QM";
            case RG: return "RG";
            case RI: return "RI";
            case RP: return "RP";
            case RR: return "RR";
            case RT: return "RT";
            case RX: return "RX";
            case SC: return "SC";
            case SP: return "SP";
            case TD: return "TD";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://terminology.hl7.org/CodeSystem/v3-hl7PublishingDomain";
        }
        public String getDefinition() {
          switch (this) {
            case AB: return "Description: Represents the HL7 content \"domain\" that supports accounting and billing functions - and \"provides support for the creation and management of patient billing accounts and the post of financial transactions against patient billing accounts for the purpose of aggregating financial transactions that will be submitted as claims or invoices for reimbursemen\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case AI: return "Description: Represents the HL7 content \"domain\" that supports trigger event control act infrastructure - and \"covers the alternate structures of the message Trigger Event Control Acts in the HL7 Composite Message.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case AL: return "Description: Represents the HL7 content \"domain\" that was defined as an \"artificial listing\" domain to support publication testing.";
            case BB: return "Description: Represents the HL7 content \"domain\" that supports blood tissue and organ domain - and \"comprises the models, messages, and other artIfacts that are needed to support messaging related to the process of blood, tissue, and organ banking operations such as donations, eligibility, storage, dispense, administration/transfusion, explantation, and implantation. \"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case CD: return "Description: Represents the HL7 content \"domain\" that supports the clinical document architecture.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case CG: return "Description: Represents the HL7 content \"domain\" that supports clinical genomics - and includes \" standards to enable the exchange of interrelated clinical and personalized genomic data between interested parties.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case CI: return "Description: Represents the HL7 content \"domain\" that supports transmission infrastructure - and \" is primarily concerned with the data content of exchanges between healthcare applications, the sequence or interrelationships in the flow of messages and the communication of significant application level exceptions or error conditions.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case CO: return "Description: Represents the HL7 content \"domain\" that supports Coverage - and provides support for managing health care coverage in the reimbursement system(s).\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case CP: return "Description: Represents the HL7 content \"domain\" that supports the common product model - which \"is used to improve the alignment between the different representations of products used within the body of HL7 Version 3 models.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case CR: return "Description: Represents the HL7 content \"domain\" that supports Claims and Reimbursement - and \"provides support for Generic, Pharmacy, Preferred Accommodation, Physician, Oral Health Vision Care and Hospital claims for eligibility, authorization, coverage extension, pre-determination, invoice adjudication, payment advice and Statement of Financial Activity (SOFA) Release 3 of this document adds claims messaging support for Physician, Oral Health Vision Care and Hospital claims.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case CS: return "Description: Represents the HL7 content \"domain\" that supports a common clinical statement pattern - and \"is a 'pattern' designed to be used within multiple HL7 Version 3 domain models. This pattern is intended to facilitate the consistent design of communications that convey clinical information to meet specific use cases.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case CT: return "Description: Represents the HL7 content \"domain\" that supports common model types - and \"are a work product produced by a particular committee for expressing a common, useful and reusable concept.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case DD: return "Description: Represents the HL7 content \"domain\" that was created to support testing and initial set-up functions.";
            case DI: return "Description: This domain has been retired in favor of \"imaging integration\" (II).";
            case DS: return "Description: Represents the HL7 content \"domain\" that provides decision support.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case EM: return "Description: Represents the HL7 content \"domain\" that supports Emergency Medical Services.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case II: return "Description: Represents the HL7 content \"domain\" that supports imaging integration - and is \"comprises the models, implementation guides, sample documents and images that are needed to illustrate the transformation of DICOM structured reports to CDA Release 2 as well as the creation of CDA diagnostic imaging reports.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case IZ: return "Description: Represents the HL7 content \"domain\" that supports immunization - and \"describes communication of information about immunization: the administration of vaccines (and/or antisera) to individuals to prevent infectious disease.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case LB: return "Description: Represents the HL7 content \"domain\" that supports clinical laboratory functions - and is \"comprises the models, messages, and other artifacts that are needed to support messaging related to laboratory tests or observations. \"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case ME: return "Description: Represents the HL7 content \"domain\" that supports medication - and  \"deals with the description of a medicine for the purposes of messaging information about medicines\" and the applications of these descriptions.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case MI: return "Description: Represents the HL7 content \"domain\" that supports master file infrastructure - and is \"comprises the classes and attributes needed to support Master Files and Registries.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case MM: return "Description: Represents the HL7 content \"domain\" that supports Materials Management - and is \"supports the simple scenario of a Materials Management application sending requests, notifications and queries to an auxiliary application. The intent is to establish a standard for the minimum functionality that is useful and comprehensive enough to explore the important concepts relative to inventory management.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case MR: return "Description: Represents the HL7 content \"domain\" that supports medical records - and is \"supports clinical document management, and document querying.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case MT: return "Description: Represents the HL7 content \"domain\" that supports shared messages - and \"are a work product produced for expressing common, useful and reusable message types.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case OB: return "Description: Represents the HL7 content \"domain\" that supports observations - and is \"comprises the models, messages, and other artifacts that are needed to support messaging related to resulting basic healthcare diagnostic services. \"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case OO: return "Description: Represents the HL7 content \"domain\" that supports orders and observations - and will provide over-arching support information for the \"Orders\" (OR) and \"Observations\" (OB) domains.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case OR: return "Description: Represents the HL7 content \"domain\" that supports orders - and \"comprises the models, messages, and other artifacts that are needed to support messaging related to ordering basic healthcare services.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case PA: return "Description: Represents the HL7 content \"domain\" that supports Patient Administration - and \"defines person and patient demographics and visit information about patients\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case PC: return "Description: Represents the HL7 content \"domain\" that supports Care Provision - and \"addresses the information that is needed for the ongoing care of individuals, populations, and other targets of care.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case PH: return "Description: Represents the HL7 content \"domain\" that supports public health - and is \"the source of a number of Common Model Element Types (CMET) designed to meet the needs of public health data exchange.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case PM: return "Description: Represents the HL7 content \"domain\" that supports Personnel Management - and \"spans a variety of clinical-administrative information functions associated with the organizations, individuals, animals and devices involved in the delivery and support of healthcare services.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case QI: return "Description: Represents the HL7 content \"domain\" that supports query infrastructure - and \"specifies the formation of information queries and the responses to these queries to meet the needs of healthcare applications using the HL7 version 3 messaging standard.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case QM: return "Description: Represents the HL7 content \"domain\" that supports Quality Measures - and \"is a standard for representing a health quality measure as an electronic document.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case RG: return "Description: Represents the HL7 content \"domain\" that supports Registries - and \"collects HL7 artifacts for administrative  registries.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case RI: return "Description: Represents the HL7 content \"domain\" that supports Informative Public Health.\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case RP: return "Description: Represents the HL7 content \"domain\" that supports Regulated Products - and \"includes standards developed as part of the family of messages targeted for the exchange of information about regulated products and the exchange of the data needed to provide approval for such products.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case RR: return "Description: Represents the HL7 content \"domain\" that supports Public Health Reporting - and \"includes messages and documents that are specifically designed to support managment, reporting and investigation in the public health context.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case RT: return "Description: Represents the HL7 content \"domain\" that supports Regulated Studies - and is \"includes standards developed as part of the family of messages targeted for the exchange of information about the conduct of regulated studies, and the exchange of the data collected during those studies.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case RX: return "Description: Represents the HL7 content \"domain\" that supports pharmacy - and is a \"model used to derive message patterns to describe and communicate processes related to medication.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case SC: return "Description: Represents the HL7 content \"domain\" that supports Scheduling - and \"offers a generic set of messages and behavior to implement any number of Scheduling scenarios.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case SP: return "Description: Represents the HL7 content \"domain\" that supports Specimen - and \"comprises the models and artifacts that are needed to support the creation of messaging related to specimen.\"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            case TD: return "Description: Represents the HL7 content \"domain\" that supports Therapeutic Devices - and is \"comprises the models, messages, and other artifacts that are needed to support messaging related to therapy delivery and observations made by a medical device. \"\r\n\n                        \n                           UsageNote: V3 Specifications are published in a set of \"domains\", which contain interactions and related specifications for a single area of health care within which can be supported by a single, coherent set of interoperability specifications.";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case AB: return "accounting & billing";
            case AI: return "trigger event control act infrastructure";
            case AL: return "artificial listing for test purposes - faux Domain for testing";
            case BB: return "blood tissue and organ";
            case CD: return "clinical document architecture";
            case CG: return "clinical genomics";
            case CI: return "transmission infrastructure";
            case CO: return "coverage";
            case CP: return "common product model";
            case CR: return "claims and reimbursement";
            case CS: return "clinical statement";
            case CT: return "common types";
            case DD: return "dummy domain";
            case DI: return "diagnostic imaging";
            case DS: return "decision support";
            case EM: return "emergency medical services";
            case II: return "imaging integration";
            case IZ: return "immunization";
            case LB: return "laboratory";
            case ME: return "medication";
            case MI: return "masterfile infrastructure";
            case MM: return "materials management";
            case MR: return "medical records";
            case MT: return "shared messages";
            case OB: return "observations";
            case OO: return "orders & observations";
            case OR: return "orders";
            case PA: return "patient administration";
            case PC: return "care provision";
            case PH: return "public health";
            case PM: return "personnel management";
            case QI: return "query infrastructure";
            case QM: return "quality measures";
            case RG: return "registries";
            case RI: return "informative public health";
            case RP: return "regulated products";
            case RR: return "public health reporting";
            case RT: return "regulated studies";
            case RX: return "pharmacy";
            case SC: return "scheduling";
            case SP: return "specimen";
            case TD: return "therapeutic devices";
            default: return "?";
          }
    }


}

