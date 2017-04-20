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

public enum ServiceType {

        /**
         * Adoption & permanent care information/support
         */
        _1, 
        /**
         * Aged care assessment
         */
        _2, 
        /**
         * Aged Care information/referral
         */
        _3, 
        /**
         * Aged Residential Care
         */
        _4, 
        /**
         * Case management for older persons
         */
        _5, 
        /**
         * Delivered meals (meals on wheels)
         */
        _6, 
        /**
         * Friendly visiting
         */
        _7, 
        /**
         * Home care/housekeeping assistance
         */
        _8, 
        /**
         * Home maintenance and repair
         */
        _9, 
        /**
         * Personal alarms/alerts
         */
        _10, 
        /**
         * Personal care for older persons
         */
        _11, 
        /**
         * Planned activity groups
         */
        _12, 
        /**
         * Acupuncture
         */
        _13, 
        /**
         * Alexander technique therapy
         */
        _14, 
        /**
         * Aromatherapy
         */
        _15, 
        /**
         * Biorhythm services
         */
        _16, 
        /**
         * Bowen therapy
         */
        _17, 
        /**
         * Chinese herbal medicine
         */
        _18, 
        /**
         * Feldenkrais
         */
        _19, 
        /**
         * Homoeopathy
         */
        _20, 
        /**
         * Hydrotherapy
         */
        _21, 
        /**
         * Hypnotherapy
         */
        _22, 
        /**
         * Kinesiology
         */
        _23, 
        /**
         * Magnetic therapy
         */
        _24, 
        /**
         * Massage therapy
         */
        _25, 
        /**
         * Meditation
         */
        _26, 
        /**
         * Myotherapy
         */
        _27, 
        /**
         * Naturopathy
         */
        _28, 
        /**
         * Reflexology
         */
        _29, 
        /**
         * Reiki
         */
        _30, 
        /**
         * Relaxation therapy
         */
        _31, 
        /**
         * Shiatsu
         */
        _32, 
        /**
         * Western herbal medicine
         */
        _33, 
        /**
         * Family day care
         */
        _34, 
        /**
         * Holiday programs
         */
        _35, 
        /**
         * Kindergarten inclusion support for children with a disability
         */
        _36, 
        /**
         * Kindergarten/preschool
         */
        _37, 
        /**
         * Long day child care
         */
        _38, 
        /**
         * Occasional child care
         */
        _39, 
        /**
         * Outside school hours care
         */
        _40, 
        /**
         * Children's play programs
         */
        _41, 
        /**
         * Parenting & family management support/education
         */
        _42, 
        /**
         * Playgroup
         */
        _43, 
        /**
         * School nursing
         */
        _44, 
        /**
         * Toy library
         */
        _45, 
        /**
         * Child protection/child abuse report
         */
        _46, 
        /**
         * Foster care
         */
        _47, 
        /**
         * Residential/ out of home care
         */
        _48, 
        /**
         * Support for young people leaving care
         */
        _49, 
        /**
         * Audiology
         */
        _50, 
        /**
         * Blood donation
         */
        _51, 
        /**
         * Chiropractic
         */
        _52, 
        /**
         * Dietetics
         */
        _53, 
        /**
         * Family planning
         */
        _54, 
        /**
         * Health advocacy/Liaison service
         */
        _55, 
        /**
         * Health information/referral
         */
        _56, 
        /**
         * Immunization
         */
        _57, 
        /**
         * Maternal & child health
         */
        _58, 
        /**
         * Nursing
         */
        _59, 
        /**
         * Nutrition
         */
        _60, 
        /**
         * Occupational therapy
         */
        _61, 
        /**
         * Optometry
         */
        _62, 
        /**
         * Osteopathy
         */
        _63, 
        /**
         * Pharmacy
         */
        _64, 
        /**
         * Physiotherapy
         */
        _65, 
        /**
         * Podiatry
         */
        _66, 
        /**
         * Sexual health
         */
        _67, 
        /**
         * Speech pathology/therapy
         */
        _68, 
        /**
         * Bereavement counselling
         */
        _69, 
        /**
         * Crisis counselling
         */
        _70, 
        /**
         * Family counselling and/or family therapy
         */
        _71, 
        /**
         * Family violence counselling
         */
        _72, 
        /**
         * Financial counselling
         */
        _73, 
        /**
         * Generalist counselling
         */
        _74, 
        /**
         * Genetic counselling
         */
        _75, 
        /**
         * Health counselling
         */
        _76, 
        /**
         * Mediation
         */
        _77, 
        /**
         * Problem gambling counselling
         */
        _78, 
        /**
         * Relationship counselling
         */
        _79, 
        /**
         * Sexual assault counselling
         */
        _80, 
        /**
         * Trauma counselling
         */
        _81, 
        /**
         * Victims of crime counselling
         */
        _82, 
        /**
         * Cemetery operation
         */
        _83, 
        /**
         * Cremation
         */
        _84, 
        /**
         * Death service information
         */
        _85, 
        /**
         * Funeral services
         */
        _86, 
        /**
         * Endodontic
         */
        _87, 
        /**
         * General dental
         */
        _88, 
        /**
         * Oral medicine
         */
        _89, 
        /**
         * Oral surgery
         */
        _90, 
        /**
         * Orthodontic
         */
        _91, 
        /**
         * Paediatric Dentistry
         */
        _92, 
        /**
         * Periodontic
         */
        _93, 
        /**
         * Prosthodontic
         */
        _94, 
        /**
         * Acquired brain injury information/referral
         */
        _95, 
        /**
         * Disability advocacy
         */
        _96, 
        /**
         * Disability aids & equipment
         */
        _97, 
        /**
         * Disability case management
         */
        _98, 
        /**
         * Disability day programs & activities
         */
        _99, 
        /**
         * Disability information/referral
         */
        _100, 
        /**
         * Disability support packages
         */
        _101, 
        /**
         * Disability supported accommodation
         */
        _102, 
        /**
         * Early childhood intervention
         */
        _103, 
        /**
         * Hearing aids & equipment
         */
        _104, 
        /**
         * Drug and/or alcohol counselling
         */
        _105, 
        /**
         * Drug and/or alcohol information/referral
         */
        _106, 
        /**
         * Needle & Syringe exchange
         */
        _107, 
        /**
         * Non-residential alcohol and/or drug dependence treatment
         */
        _108, 
        /**
         * Pharmacotherapy (eg. methadone) program
         */
        _109, 
        /**
         * Quit program
         */
        _110, 
        /**
         * Residential alcohol and/or drug dependence treatment
         */
        _111, 
        /**
         * Adult/community education
         */
        _112, 
        /**
         * Higher education
         */
        _113, 
        /**
         * Primary education
         */
        _114, 
        /**
         * Secondary education
         */
        _115, 
        /**
         * Training & vocational education
         */
        _116, 
        /**
         * Emergency medical
         */
        _117, 
        /**
         * Employment placement and/or support
         */
        _118, 
        /**
         * Vocational Rehabilitation
         */
        _119, 
        /**
         * Workplace safety and/or accident prevention
         */
        _120, 
        /**
         * Financial assistance
         */
        _121, 
        /**
         * Financial information/advice
         */
        _122, 
        /**
         * Material aid
         */
        _123, 
        /**
         * General Practice/GP (doctor)
         */
        _124, 
        /**
         * Accommodation placement and/or support
         */
        _125, 
        /**
         * Crisis/emergency accommodation
         */
        _126, 
        /**
         * Homelessness support
         */
        _127, 
        /**
         * Housing information/referral
         */
        _128, 
        /**
         * Public rental housing
         */
        _129, 
        /**
         * Interpreting/Multilingual/Language service
         */
        _130, 
        /**
         * Juvenile Justice
         */
        _131, 
        /**
         * Legal advocacy
         */
        _132, 
        /**
         * Legal information/advice/referral
         */
        _133, 
        /**
         * Mental health advocacy
         */
        _134, 
        /**
         * Mental health assessment/triage/crisis response
         */
        _135, 
        /**
         * Mental health case management/continuing care
         */
        _136, 
        /**
         * Mental health information/referral
         */
        _137, 
        /**
         * Mental health inpatient services (hospital psychiatric unit) - requires referral
         */
        _138, 
        /**
         * Mental health non-residential rehabilitation
         */
        _139, 
        /**
         * Mental health residential rehabilitation/community care unit
         */
        _140, 
        /**
         * Psychiatry (requires referral)
         */
        _141, 
        /**
         * Psychology
         */
        _142, 
        /**
         * Martial arts
         */
        _143, 
        /**
         * Personal fitness training
         */
        _144, 
        /**
         * Physical activity group
         */
        _145, 
        /**
         * Physical activity programs
         */
        _146, 
        /**
         * Physical fitness testing
         */
        _147, 
        /**
         * Pilates
         */
        _148, 
        /**
         * Self defence
         */
        _149, 
        /**
         * Sporting club
         */
        _150, 
        /**
         * Yoga
         */
        _151, 
        /**
         * Food safety
         */
        _152, 
        /**
         * Health regulatory, inspection and/or certification
         */
        _153, 
        /**
         * Workplace health and/or safety inspection and/or certification
         */
        _154, 
        /**
         * Carer support
         */
        _155, 
        /**
         * Respite care
         */
        _156, 
        /**
         * Anatomical Pathology (including Cytopathology & Forensic Pathology)
         */
        _157, 
        /**
         * Pathology - Clinical Chemistry
         */
        _158, 
        /**
         * Pathology - General
         */
        _159, 
        /**
         * Pathology - Genetics
         */
        _160, 
        /**
         * Pathology - Haematology
         */
        _161, 
        /**
         * Pathology - Immunology
         */
        _162, 
        /**
         * Pathology - Microbiology
         */
        _163, 
        /**
         * Anaesthesiology - Pain Medicine
         */
        _164, 
        /**
         * Cardiology
         */
        _165, 
        /**
         * Clinical Genetics
         */
        _166, 
        /**
         * Clinical Pharmacology
         */
        _167, 
        /**
         * Dermatology
         */
        _168, 
        /**
         * Endocrinology
         */
        _169, 
        /**
         * Gastroenterology & Hepatology
         */
        _170, 
        /**
         * Geriatric medicine
         */
        _171, 
        /**
         * Immunology & Allergy
         */
        _172, 
        /**
         * Infectious diseases
         */
        _173, 
        /**
         * Intensive care medicine
         */
        _174, 
        /**
         * Medical Oncology
         */
        _175, 
        /**
         * Nephrology
         */
        _176, 
        /**
         * Neurology
         */
        _177, 
        /**
         * Occupational Medicine
         */
        _178, 
        /**
         * Palliative Medicine
         */
        _179, 
        /**
         * Public Health Medicine
         */
        _180, 
        /**
         * Rehabilitation Medicine
         */
        _181, 
        /**
         * Rheumatology
         */
        _182, 
        /**
         * Sleep Medicine
         */
        _183, 
        /**
         * Thoracic medicine
         */
        _184, 
        /**
         * Gynaecological Oncology
         */
        _185, 
        /**
         * Obstetrics & Gynaecology
         */
        _186, 
        /**
         * Reproductive Endocrinology & Infertility
         */
        _187, 
        /**
         * Urogynaecology
         */
        _188, 
        /**
         * Neonatology & Perinatology
         */
        _189, 
        /**
         * Paediatric Cardiology
         */
        _190, 
        /**
         * Paediatric Clinical Genetics
         */
        _191, 
        /**
         * Paediatric Clinical Pharmacology
         */
        _192, 
        /**
         * Paediatric Endocrinology
         */
        _193, 
        /**
         * Paediatric Gastroenterology & Hepatology
         */
        _194, 
        /**
         * Paediatric Haematology
         */
        _195, 
        /**
         * Paediatric Immunology & Allergy
         */
        _196, 
        /**
         * Paediatric Infectious diseases
         */
        _197, 
        /**
         * Paediatric intensive care medicine
         */
        _198, 
        /**
         * Paediatric Medical Oncology
         */
        _199, 
        /**
         * Paediatric Medicine
         */
        _200, 
        /**
         * Paediatric Nephrology
         */
        _201, 
        /**
         * Paediatric Neurology
         */
        _202, 
        /**
         * Paediatric Nuclear Medicine
         */
        _203, 
        /**
         * Paediatric Rehabilitation Medicine
         */
        _204, 
        /**
         * Paediatric Rheumatology
         */
        _205, 
        /**
         * Paediatric Sleep Medicine
         */
        _206, 
        /**
         * Paediatric Surgery
         */
        _207, 
        /**
         * Paediatric Thoracic Medicine
         */
        _208, 
        /**
         * Diagnostic Radiology/Xray/CT/Fluoroscopy
         */
        _209, 
        /**
         * Diagnostic Ultrasound
         */
        _210, 
        /**
         * Magnetic Resonance Imaging (MRI)
         */
        _211, 
        /**
         * Nuclear Medicine
         */
        _212, 
        /**
         * Obstetric & Gynaecological Ultrasound
         */
        _213, 
        /**
         * Radiation oncology
         */
        _214, 
        /**
         * Cardiothoracic surgery
         */
        _215, 
        /**
         * Neurosurgery
         */
        _216, 
        /**
         * Ophthalmology
         */
        _217, 
        /**
         * Orthopaedic surgery
         */
        _218, 
        /**
         * Otolaryngology - Head & Neck Surgery
         */
        _219, 
        /**
         * Plastic & Reconstructive Surgery
         */
        _220, 
        /**
         * Surgery - General
         */
        _221, 
        /**
         * Urology
         */
        _222, 
        /**
         * Vascular surgery
         */
        _223, 
        /**
         * Support groups
         */
        _224, 
        /**
         * Air ambulance
         */
        _225, 
        /**
         * Ambulance
         */
        _226, 
        /**
         * Blood transport
         */
        _227, 
        /**
         * Community bus
         */
        _228, 
        /**
         * Flying doctor service
         */
        _229, 
        /**
         * Patient transport
         */
        _230, 
        /**
         * A&E
         */
        _231, 
        /**
         * A&EP
         */
        _232, 
        /**
         * Abuse
         */
        _233, 
        /**
         * ACAS
         */
        _234, 
        /**
         * Access
         */
        _235, 
        /**
         * Accident
         */
        _236, 
        /**
         * Acute Inpatient Service's
         */
        _237, 
        /**
         * Adult Day Programs
         */
        _238, 
        /**
         * Adult Mental Health Services
         */
        _239, 
        /**
         * Advice
         */
        _240, 
        /**
         * Advocacy
         */
        _241, 
        /**
         * Aged Persons Mental Health Residential Units
         */
        _242, 
        /**
         * Aged Persons Mental Health Services
         */
        _243, 
        /**
         * Aged Persons Mental Health Teams
         */
        _244, 
        /**
         * Aids
         */
        _245, 
        /**
         * Al-Anon
         */
        _246, 
        /**
         * Alcohol
         */
        _247, 
        /**
         * Al-Teen
         */
        _248, 
        /**
         * Antenatal
         */
        _249, 
        /**
         * Anxiety
         */
        _250, 
        /**
         * Arthritis
         */
        _251, 
        /**
         * Assessment
         */
        _252, 
        /**
         * Assistance
         */
        _253, 
        /**
         * Asthma
         */
        _254, 
        /**
         * ATSS
         */
        _255, 
        /**
         * Attendant Care
         */
        _256, 
        /**
         * Babies
         */
        _257, 
        /**
         * Bathroom Modification
         */
        _258, 
        /**
         * Behaviour
         */
        _259, 
        /**
         * Behaviour Intervention
         */
        _260, 
        /**
         * Bereavement
         */
        _261, 
        /**
         * Bipolar
         */
        _262, 
        /**
         * Birth
         */
        _263, 
        /**
         * Birth Control
         */
        _264, 
        /**
         * Birthing Options
         */
        _265, 
        /**
         * BIST
         */
        _266, 
        /**
         * Blood
         */
        _267, 
        /**
         * Bone
         */
        _268, 
        /**
         * Bowel
         */
        _269, 
        /**
         * Brain
         */
        _270, 
        /**
         * Breast Feeding
         */
        _271, 
        /**
         * Breast Screen
         */
        _272, 
        /**
         * Brokerage
         */
        _273, 
        /**
         * Cancer
         */
        _274, 
        /**
         * Cancer Support
         */
        _275, 
        /**
         * Cardiovascular Disease
         */
        _276, 
        /**
         * Care Packages
         */
        _277, 
        /**
         * Carer
         */
        _278, 
        /**
         * Case Management
         */
        _279, 
        /**
         * Casualty
         */
        _280, 
        /**
         * Centrelink
         */
        _281, 
        /**
         * Chemists
         */
        _282, 
        /**
         * Child And Adolescent Mental Health Services
         */
        _283, 
        /**
         * Child Care
         */
        _284, 
        /**
         * Child Services
         */
        _285, 
        /**
         * Children
         */
        _286, 
        /**
         * Children's Services
         */
        _287, 
        /**
         * Cholesterol
         */
        _288, 
        /**
         * Clothing
         */
        _289, 
        /**
         * Community Based Accommodation
         */
        _290, 
        /**
         * Community Care Unit
         */
        _291, 
        /**
         * Community Child And Adolescent Mental Health Services
         */
        _292, 
        /**
         * Community Health
         */
        _293, 
        /**
         * Community Residential Unit
         */
        _294, 
        /**
         * Community Transport
         */
        _295, 
        /**
         * Companion Visiting
         */
        _296, 
        /**
         * Companionship
         */
        _297, 
        /**
         * Consumer Advice
         */
        _298, 
        /**
         * Consumer Issues
         */
        _299, 
        /**
         * Continuing Care Services
         */
        _300, 
        /**
         * Contraception Information
         */
        _301, 
        /**
         * Coordinating Bodies
         */
        _302, 
        /**
         * Correctional Services
         */
        _303, 
        /**
         * Council Environmental Health
         */
        _304, 
        /**
         * Counselling
         */
        _305, 
        /**
         * Criminal
         */
        _306, 
        /**
         * Crises
         */
        _307, 
        /**
         * Crisis Assessment And Treatment Services (Cats)
         */
        _308, 
        /**
         * Crisis Assistance
         */
        _309, 
        /**
         * Crisis Refuge
         */
        _310, 
        /**
         * Day Program
         */
        _311, 
        /**
         * Deaf
         */
        _312, 
        /**
         * Dental Hygiene
         */
        _313, 
        /**
         * Dentistry
         */
        _314, 
        /**
         * Dentures
         */
        _315, 
        /**
         * Depression
         */
        _316, 
        /**
         * Detoxification
         */
        _317, 
        /**
         * Diabetes
         */
        _318, 
        /**
         * Diaphragm Fitting
         */
        _319, 
        /**
         * Dieticians
         */
        _320, 
        /**
         * Disabled Parking
         */
        _321, 
        /**
         * District Nursing
         */
        _322, 
        /**
         * Divorce
         */
        _323, 
        /**
         * Doctors
         */
        _324, 
        /**
         * Drink-Drive
         */
        _325, 
        /**
         * Dual Diagnosis Services
         */
        _326, 
        /**
         * Early Choice
         */
        _327, 
        /**
         * Eating Disorder
         */
        _328, 
        /**
         * Emergency Relief
         */
        _330, 
        /**
         * Employment And Training
         */
        _331, 
        /**
         * Environment
         */
        _332, 
        /**
         * Equipment
         */
        _333, 
        /**
         * Exercise
         */
        _334, 
        /**
         * Facility
         */
        _335, 
        /**
         * Family Choice
         */
        _336, 
        /**
         * Family Law
         */
        _337, 
        /**
         * Family Options
         */
        _338, 
        /**
         * Family Services
         */
        _339, 
        /**
         * FFYA
         */
        _340, 
        /**
         * Financial Aid
         */
        _341, 
        /**
         * Fitness
         */
        _342, 
        /**
         * Flexible Care Packages
         */
        _343, 
        /**
         * Food
         */
        _344, 
        /**
         * Food Vouchers
         */
        _345, 
        /**
         * Forensic Mental Health Services
         */
        _346, 
        /**
         * Futures
         */
        _347, 
        /**
         * Futures For Young Adults
         */
        _348, 
        /**
         * General Practitioners
         */
        _349, 
        /**
         * Grants
         */
        _350, 
        /**
         * Grief
         */
        _351, 
        /**
         * Grief Counselling
         */
        _352, 
        /**
         * HACC
         */
        _353, 
        /**
         * Heart Disease
         */
        _354, 
        /**
         * Help
         */
        _355, 
        /**
         * High Blood Pressure
         */
        _356, 
        /**
         * Home Help
         */
        _357, 
        /**
         * Home Nursing
         */
        _358, 
        /**
         * Homefirst
         */
        _359, 
        /**
         * Hospice Care
         */
        _360, 
        /**
         * Hospital Services
         */
        _361, 
        /**
         * Hospital To Home
         */
        _362, 
        /**
         * Hostel
         */
        _364, 
        /**
         * Hostel Accommodation
         */
        _365, 
        /**
         * Household Items
         */
        _366, 
        /**
         * Hypertension
         */
        _367, 
        /**
         * Illness
         */
        _368, 
        /**
         * Independent Living
         */
        _369, 
        /**
         * Information
         */
        _370, 
        /**
         * Injury
         */
        _371, 
        /**
         * Intake
         */
        _372, 
        /**
         * Intensive Mobile Youth Outreach Services (Imyos)
         */
        _373, 
        /**
         * Intervention
         */
        _374, 
        /**
         * Job Searching
         */
        _375, 
        /**
         * Justice
         */
        _376, 
        /**
         * Leisure
         */
        _377, 
        /**
         * Loans
         */
        _378, 
        /**
         * Low Income Earners
         */
        _379, 
        /**
         * Lung
         */
        _380, 
        /**
         * Making A Difference
         */
        _381, 
        /**
         * Medical Services
         */
        _382, 
        /**
         * Medical Specialists
         */
        _383, 
        /**
         * Medication Administration
         */
        _384, 
        /**
         * Menstrual Information
         */
        _385, 
        /**
         * Methadone
         */
        _386, 
        /**
         * Mobile Support And Treatment Services (MSTS)
         */
        _387, 
        /**
         * Motor Neurone
         */
        _388, 
        /**
         * Multiple Sclerosis
         */
        _389, 
        /**
         * Neighbourhood House
         */
        _390, 
        /**
         * Nursing Home
         */
        _391, 
        /**
         * Nursing Mothers
         */
        _392, 
        /**
         * Obesity
         */
        _393, 
        /**
         * Occupational Health & Safety
         */
        _394, 
        /**
         * Optometrist
         */
        _395, 
        /**
         * Oral Hygiene
         */
        _396, 
        /**
         * Outpatients
         */
        _397, 
        /**
         * Outreach Service
         */
        _398, 
        /**
         * PADP
         */
        _399, 
        /**
         * Pain
         */
        _400, 
        /**
         * Pap Smear
         */
        _401, 
        /**
         * Parenting
         */
        _402, 
        /**
         * Peak Organisations
         */
        _403, 
        /**
         * Personal Care
         */
        _404, 
        /**
         * Pharmacies
         */
        _405, 
        /**
         * Phobias
         */
        _406, 
        /**
         * Physical
         */
        _407, 
        /**
         * Physical Activity
         */
        _408, 
        /**
         * Postnatal
         */
        _409, 
        /**
         * Pregnancy
         */
        _410, 
        /**
         * Pregnancy Tests
         */
        _411, 
        /**
         * Preschool
         */
        _412, 
        /**
         * Prescriptions
         */
        _413, 
        /**
         * Primary Mental Health And Early Intervention Teams
         */
        _414, 
        /**
         * Property Maintenance
         */
        _415, 
        /**
         * Prostate
         */
        _416, 
        /**
         * Psychiatric
         */
        _417, 
        /**
         * Psychiatric Disability Support Services - Home-Based Outreach
         */
        _418, 
        /**
         * Psychiatric Disability Support Services - Planned Respite
         */
        _419, 
        /**
         * Psychiatric Disability Support Services - Residential Rehabilitation
         */
        _420, 
        /**
         * Psychiatric Disability Support Services Home-Based Outreach
         */
        _421, 
        /**
         * Psychiatric Disability Support Services Mutual Support And Self Help
         */
        _422, 
        /**
         * Psychiatric Support
         */
        _423, 
        /**
         * Recreation
         */
        _424, 
        /**
         * Referral
         */
        _425, 
        /**
         * Refuge
         */
        _426, 
        /**
         * Rent Assistance
         */
        _427, 
        /**
         * Residential Facilities
         */
        _428, 
        /**
         * Residential Respite
         */
        _429, 
        /**
         * Respiratory
         */
        _430, 
        /**
         * Response
         */
        _431, 
        /**
         * Rooming Houses
         */
        _432, 
        /**
         * Safe Sex
         */
        _433, 
        /**
         * Secure Extended Care Inpatient Services
         */
        _434, 
        /**
         * Self Help
         */
        _435, 
        /**
         * Separation
         */
        _436, 
        /**
         * Services
         */
        _437, 
        /**
         * Sex Education
         */
        _438, 
        /**
         * Sexual Abuse
         */
        _439, 
        /**
         * Sexual Issues
         */
        _440, 
        /**
         * Sexually Transmitted Diseases
         */
        _441, 
        /**
         * SIDS
         */
        _442, 
        /**
         * Social Support
         */
        _443, 
        /**
         * Socialisation
         */
        _444, 
        /**
         * Special Needs
         */
        _445, 
        /**
         * Speech Therapist
         */
        _446, 
        /**
         * Splinting
         */
        _447, 
        /**
         * Sport
         */
        _448, 
        /**
         * Statewide And Specialist Services
         */
        _449, 
        /**
         * STD
         */
        _450, 
        /**
         * STI
         */
        _451, 
        /**
         * Stillbirth
         */
        _452, 
        /**
         * Stomal Care
         */
        _453, 
        /**
         * Stroke
         */
        _454, 
        /**
         * Substance Abuse
         */
        _455, 
        /**
         * Support
         */
        _456, 
        /**
         * Syringes
         */
        _457, 
        /**
         * Teeth
         */
        _458, 
        /**
         * Tenancy Advice
         */
        _459, 
        /**
         * Terminal Illness
         */
        _460, 
        /**
         * Therapy
         */
        _461, 
        /**
         * Transcription
         */
        _462, 
        /**
         * Translating Services
         */
        _463, 
        /**
         * Translator
         */
        _464, 
        /**
         * Transport
         */
        _465, 
        /**
         * Vertebrae
         */
        _466, 
        /**
         * Violence
         */
        _467, 
        /**
         * Vocational Guidance
         */
        _468, 
        /**
         * Weight
         */
        _469, 
        /**
         * Welfare Assistance
         */
        _470, 
        /**
         * Welfare Counselling
         */
        _471, 
        /**
         * Wheelchairs
         */
        _472, 
        /**
         * Wound Management
         */
        _473, 
        /**
         * Young People At Risk
         */
        _474, 
        /**
         * Further Description - Community Health Care
         */
        _475, 
        /**
         * Library
         */
        _476, 
        /**
         * Community Hours
         */
        _477, 
        /**
         * Further Description - Specialist Medical
         */
        _478, 
        /**
         * Hepatology
         */
        _479, 
        /**
         * Gastroenterology
         */
        _480, 
        /**
         * Gynaecology
         */
        _481, 
        /**
         * Obstetrics
         */
        _482, 
        /**
         * Further Description - Specialist Surgical
         */
        _483, 
        /**
         * Placement Protection
         */
        _484, 
        /**
         * Family Violence
         */
        _485, 
        /**
         * Integrated Family Services
         */
        _486, 
        /**
         * Diabetes Educator
         */
        _488, 
        /**
         * Kinship Care
         */
        _489, 
        /**
         * General Mental Health Services
         */
        _490, 
        /**
         * Exercise Physiology
         */
        _491, 
        /**
         * Medical Research
         */
        _492, 
        /**
         * Youth
         */
        _493, 
        /**
         * Youth Services
         */
        _494, 
        /**
         * Youth Health
         */
        _495, 
        /**
         * Child and Family Services
         */
        _496, 
        /**
         * Home Visits
         */
        _497, 
        /**
         * Mobile Services
         */
        _498, 
        /**
         * Before and/or After School Care
         */
        _500, 
        /**
         * Cancer Services
         */
        _501, 
        /**
         * Integrated Cancer Services
         */
        _502, 
        /**
         * Multidisciplinary Services
         */
        _503, 
        /**
         * Multidisciplinary Cancer Services
         */
        _504, 
        /**
         * Meetings
         */
        _505, 
        /**
         * Blood pressure monitoring
         */
        _506, 
        /**
         * Dose administration aid
         */
        _507, 
        /**
         * Medical Equipment Hire
         */
        _508, 
        /**
         * Parenting & family support/education
         */
        _509, 
        /**
         * Deputising Service
         */
        _510, 
        /**
         * Cancer Support Groups
         */
        _513, 
        /**
         * Community Cancer Services
         */
        _514, 
        /**
         * Disability Care Transport
         */
        _530, 
        /**
         * Aged Care Transport
         */
        _531, 
        /**
         * Diabetes Education service
         */
        _532, 
        /**
         * Cardiac Rehabilitation Service 
         */
        _533, 
        /**
         * Young Adult Diabetes services (YADS)
         */
        _534, 
        /**
         * Pulmonary Rehabilitation Service
         */
        _535, 
        /**
         * Art therapy
         */
        _536, 
        /**
         * Medication Reviews
         */
        _537, 
        /**
         * Telephone Counselling
         */
        _538, 
        /**
         * Telephone Help Line
         */
        _539, 
        /**
         * Online Service
         */
        _540, 
        /**
         * Crisis - Mental Health
         */
        _541, 
        /**
         * Youth Crisis
         */
        _542, 
        /**
         * Sexual Assault
         */
        _543, 
        /**
         * GPAH Other
         */
        _544, 
        /**
         * Paediatric Dermatology
         */
        _545, 
        /**
         * Veterans Services
         */
        _546, 
        /**
         * Veterans
         */
        _547, 
        /**
         * Food Relief/food/meals
         */
        _548, 
        /**
         * Dementia Care
         */
        _550, 
        /**
         * Alzheimer
         */
        _551, 
        /**
         * Drug and/or alcohol support groups
         */
        _552, 
        /**
         * One on One Support/Mentoring/Coaching
         */
        _553, 
        /**
         * Chronic Disease Management
         */
        _554, 
        /**
         * Liaison Services
         */
        _555, 
        /**
         * Walk in Centre / non emergency
         */
        _556, 
        /**
         * Inpatients
         */
        _557, 
        /**
         * Spiritual Counselling
         */
        _558, 
        /**
         * Women's Health
         */
        _559, 
        /**
         * Men's Health
         */
        _560, 
        /**
         * Health education/Health awareness program
         */
        _561, 
        /**
         * Test Message
         */
        _562, 
        /**
         * Remedial Massage
         */
        _563, 
        /**
         * Adolescent Mental Health Services
         */
        _564, 
        /**
         * Youth drop in/assistance/support
         */
        _565, 
        /**
         * Aboriginal Health Worker
         */
        _566, 
        /**
         * Women's Health Clinic
         */
        _567, 
        /**
         * Men's Health Clinic 
         */
        _568, 
        /**
         * Migrant Health Clinic
         */
        _569, 
        /**
         * Refugee Health Clinic
         */
        _570, 
        /**
         * Aboriginal Health Clinic
         */
        _571, 
        /**
         * Nurse Practitioner lead Clinic/s
         */
        _572, 
        /**
         * Nurse lead Clinic/s
         */
        _573, 
        /**
         * Culturally tailored support groups
         */
        _574, 
        /**
         * Culturally tailored health promotion
         */
        _575, 
        /**
         * Rehabilitation
         */
        _576, 
        /**
         * Education information/referral
         */
        _577, 
        /**
         * Social Work
         */
        _580, 
        /**
         * Haematology
         */
        _581, 
        /**
         * Maternity Shared Care
         */
        _582, 
        /**
         * Rehabilitation Service
         */
        _583, 
        /**
         * Cranio-Sacral Therapy
         */
        _584, 
        /**
         * Prosthetics & Orthotics
         */
        _585, 
        /**
         * Home Medicine Review
         */
        _589, 
        /**
         * GPAH - Medical
         */
        _590, 
        /**
         * Music Therapy
         */
        _591, 
        /**
         * Falls Prevention
         */
        _593, 
        /**
         * Accommodation/Tenancy
         */
        _599, 
        /**
         * Assess-Skill, Ability, Needs
         */
        _600, 
        /**
         * Assist Access/Maintain Employ
         */
        _601, 
        /**
         * Assist Prod-Pers Care/Safety
         */
        _602, 
        /**
         * Assist-Integrate School/Ed
         */
        _603, 
        /**
         * Assist-Life Stage, Transition
         */
        _604, 
        /**
         * Assist-Personal Activities
         */
        _605, 
        /**
         * Assist-Travel/Transport
         */
        _606, 
        /**
         * Assistive Equip-General Tasks
         */
        _607, 
        /**
         * Assistive Equip-Recreation
         */
        _608, 
        /**
         * Assistive Prod-Household Task
         */
        _609, 
        /**
         * Behaviour Support
         */
        _610, 
        /**
         * Comms & Info Equipment
         */
        _611, 
        /**
         * Community Nursing Care
         */
        _612, 
        /**
         * Daily Tasks/Shared Living
         */
        _613, 
        /**
         * Development-Life Skills
         */
        _614, 
        /**
         * Early Childhood Supports
         */
        _615, 
        /**
         * Equipment Special Assess Setup
         */
        _616, 
        /**
         * Hearing Equipment
         */
        _617, 
        /**
         * Home Modification
         */
        _618, 
        /**
         * Household Tasks
         */
        _619, 
        /**
         * Interpret/Translate
         */
        _620, 
        /**
         * Other Innovative Supports
         */
        _621, 
        /**
         * Participate Community
         */
        _622, 
        /**
         * Personal Mobility Equipment
         */
        _623, 
        /**
         * Physical Wellbeing
         */
        _624, 
        /**
         * Plan Management
         */
        _625, 
        /**
         * Therapeutic Supports
         */
        _626, 
        /**
         * Training-Travel Independence
         */
        _627, 
        /**
         * Vehicle modifications
         */
        _628, 
        /**
         * Vision Equipment
         */
        _629, 
        /**
         * added to help the parsers
         */
        NULL;
        public static ServiceType fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("1".equals(codeString))
          return _1;
        if ("2".equals(codeString))
          return _2;
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
        if ("8".equals(codeString))
          return _8;
        if ("9".equals(codeString))
          return _9;
        if ("10".equals(codeString))
          return _10;
        if ("11".equals(codeString))
          return _11;
        if ("12".equals(codeString))
          return _12;
        if ("13".equals(codeString))
          return _13;
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
        if ("19".equals(codeString))
          return _19;
        if ("20".equals(codeString))
          return _20;
        if ("21".equals(codeString))
          return _21;
        if ("22".equals(codeString))
          return _22;
        if ("23".equals(codeString))
          return _23;
        if ("24".equals(codeString))
          return _24;
        if ("25".equals(codeString))
          return _25;
        if ("26".equals(codeString))
          return _26;
        if ("27".equals(codeString))
          return _27;
        if ("28".equals(codeString))
          return _28;
        if ("29".equals(codeString))
          return _29;
        if ("30".equals(codeString))
          return _30;
        if ("31".equals(codeString))
          return _31;
        if ("32".equals(codeString))
          return _32;
        if ("33".equals(codeString))
          return _33;
        if ("34".equals(codeString))
          return _34;
        if ("35".equals(codeString))
          return _35;
        if ("36".equals(codeString))
          return _36;
        if ("37".equals(codeString))
          return _37;
        if ("38".equals(codeString))
          return _38;
        if ("39".equals(codeString))
          return _39;
        if ("40".equals(codeString))
          return _40;
        if ("41".equals(codeString))
          return _41;
        if ("42".equals(codeString))
          return _42;
        if ("43".equals(codeString))
          return _43;
        if ("44".equals(codeString))
          return _44;
        if ("45".equals(codeString))
          return _45;
        if ("46".equals(codeString))
          return _46;
        if ("47".equals(codeString))
          return _47;
        if ("48".equals(codeString))
          return _48;
        if ("49".equals(codeString))
          return _49;
        if ("50".equals(codeString))
          return _50;
        if ("51".equals(codeString))
          return _51;
        if ("52".equals(codeString))
          return _52;
        if ("53".equals(codeString))
          return _53;
        if ("54".equals(codeString))
          return _54;
        if ("55".equals(codeString))
          return _55;
        if ("56".equals(codeString))
          return _56;
        if ("57".equals(codeString))
          return _57;
        if ("58".equals(codeString))
          return _58;
        if ("59".equals(codeString))
          return _59;
        if ("60".equals(codeString))
          return _60;
        if ("61".equals(codeString))
          return _61;
        if ("62".equals(codeString))
          return _62;
        if ("63".equals(codeString))
          return _63;
        if ("64".equals(codeString))
          return _64;
        if ("65".equals(codeString))
          return _65;
        if ("66".equals(codeString))
          return _66;
        if ("67".equals(codeString))
          return _67;
        if ("68".equals(codeString))
          return _68;
        if ("69".equals(codeString))
          return _69;
        if ("70".equals(codeString))
          return _70;
        if ("71".equals(codeString))
          return _71;
        if ("72".equals(codeString))
          return _72;
        if ("73".equals(codeString))
          return _73;
        if ("74".equals(codeString))
          return _74;
        if ("75".equals(codeString))
          return _75;
        if ("76".equals(codeString))
          return _76;
        if ("77".equals(codeString))
          return _77;
        if ("78".equals(codeString))
          return _78;
        if ("79".equals(codeString))
          return _79;
        if ("80".equals(codeString))
          return _80;
        if ("81".equals(codeString))
          return _81;
        if ("82".equals(codeString))
          return _82;
        if ("83".equals(codeString))
          return _83;
        if ("84".equals(codeString))
          return _84;
        if ("85".equals(codeString))
          return _85;
        if ("86".equals(codeString))
          return _86;
        if ("87".equals(codeString))
          return _87;
        if ("88".equals(codeString))
          return _88;
        if ("89".equals(codeString))
          return _89;
        if ("90".equals(codeString))
          return _90;
        if ("91".equals(codeString))
          return _91;
        if ("92".equals(codeString))
          return _92;
        if ("93".equals(codeString))
          return _93;
        if ("94".equals(codeString))
          return _94;
        if ("95".equals(codeString))
          return _95;
        if ("96".equals(codeString))
          return _96;
        if ("97".equals(codeString))
          return _97;
        if ("98".equals(codeString))
          return _98;
        if ("99".equals(codeString))
          return _99;
        if ("100".equals(codeString))
          return _100;
        if ("101".equals(codeString))
          return _101;
        if ("102".equals(codeString))
          return _102;
        if ("103".equals(codeString))
          return _103;
        if ("104".equals(codeString))
          return _104;
        if ("105".equals(codeString))
          return _105;
        if ("106".equals(codeString))
          return _106;
        if ("107".equals(codeString))
          return _107;
        if ("108".equals(codeString))
          return _108;
        if ("109".equals(codeString))
          return _109;
        if ("110".equals(codeString))
          return _110;
        if ("111".equals(codeString))
          return _111;
        if ("112".equals(codeString))
          return _112;
        if ("113".equals(codeString))
          return _113;
        if ("114".equals(codeString))
          return _114;
        if ("115".equals(codeString))
          return _115;
        if ("116".equals(codeString))
          return _116;
        if ("117".equals(codeString))
          return _117;
        if ("118".equals(codeString))
          return _118;
        if ("119".equals(codeString))
          return _119;
        if ("120".equals(codeString))
          return _120;
        if ("121".equals(codeString))
          return _121;
        if ("122".equals(codeString))
          return _122;
        if ("123".equals(codeString))
          return _123;
        if ("124".equals(codeString))
          return _124;
        if ("125".equals(codeString))
          return _125;
        if ("126".equals(codeString))
          return _126;
        if ("127".equals(codeString))
          return _127;
        if ("128".equals(codeString))
          return _128;
        if ("129".equals(codeString))
          return _129;
        if ("130".equals(codeString))
          return _130;
        if ("131".equals(codeString))
          return _131;
        if ("132".equals(codeString))
          return _132;
        if ("133".equals(codeString))
          return _133;
        if ("134".equals(codeString))
          return _134;
        if ("135".equals(codeString))
          return _135;
        if ("136".equals(codeString))
          return _136;
        if ("137".equals(codeString))
          return _137;
        if ("138".equals(codeString))
          return _138;
        if ("139".equals(codeString))
          return _139;
        if ("140".equals(codeString))
          return _140;
        if ("141".equals(codeString))
          return _141;
        if ("142".equals(codeString))
          return _142;
        if ("143".equals(codeString))
          return _143;
        if ("144".equals(codeString))
          return _144;
        if ("145".equals(codeString))
          return _145;
        if ("146".equals(codeString))
          return _146;
        if ("147".equals(codeString))
          return _147;
        if ("148".equals(codeString))
          return _148;
        if ("149".equals(codeString))
          return _149;
        if ("150".equals(codeString))
          return _150;
        if ("151".equals(codeString))
          return _151;
        if ("152".equals(codeString))
          return _152;
        if ("153".equals(codeString))
          return _153;
        if ("154".equals(codeString))
          return _154;
        if ("155".equals(codeString))
          return _155;
        if ("156".equals(codeString))
          return _156;
        if ("157".equals(codeString))
          return _157;
        if ("158".equals(codeString))
          return _158;
        if ("159".equals(codeString))
          return _159;
        if ("160".equals(codeString))
          return _160;
        if ("161".equals(codeString))
          return _161;
        if ("162".equals(codeString))
          return _162;
        if ("163".equals(codeString))
          return _163;
        if ("164".equals(codeString))
          return _164;
        if ("165".equals(codeString))
          return _165;
        if ("166".equals(codeString))
          return _166;
        if ("167".equals(codeString))
          return _167;
        if ("168".equals(codeString))
          return _168;
        if ("169".equals(codeString))
          return _169;
        if ("170".equals(codeString))
          return _170;
        if ("171".equals(codeString))
          return _171;
        if ("172".equals(codeString))
          return _172;
        if ("173".equals(codeString))
          return _173;
        if ("174".equals(codeString))
          return _174;
        if ("175".equals(codeString))
          return _175;
        if ("176".equals(codeString))
          return _176;
        if ("177".equals(codeString))
          return _177;
        if ("178".equals(codeString))
          return _178;
        if ("179".equals(codeString))
          return _179;
        if ("180".equals(codeString))
          return _180;
        if ("181".equals(codeString))
          return _181;
        if ("182".equals(codeString))
          return _182;
        if ("183".equals(codeString))
          return _183;
        if ("184".equals(codeString))
          return _184;
        if ("185".equals(codeString))
          return _185;
        if ("186".equals(codeString))
          return _186;
        if ("187".equals(codeString))
          return _187;
        if ("188".equals(codeString))
          return _188;
        if ("189".equals(codeString))
          return _189;
        if ("190".equals(codeString))
          return _190;
        if ("191".equals(codeString))
          return _191;
        if ("192".equals(codeString))
          return _192;
        if ("193".equals(codeString))
          return _193;
        if ("194".equals(codeString))
          return _194;
        if ("195".equals(codeString))
          return _195;
        if ("196".equals(codeString))
          return _196;
        if ("197".equals(codeString))
          return _197;
        if ("198".equals(codeString))
          return _198;
        if ("199".equals(codeString))
          return _199;
        if ("200".equals(codeString))
          return _200;
        if ("201".equals(codeString))
          return _201;
        if ("202".equals(codeString))
          return _202;
        if ("203".equals(codeString))
          return _203;
        if ("204".equals(codeString))
          return _204;
        if ("205".equals(codeString))
          return _205;
        if ("206".equals(codeString))
          return _206;
        if ("207".equals(codeString))
          return _207;
        if ("208".equals(codeString))
          return _208;
        if ("209".equals(codeString))
          return _209;
        if ("210".equals(codeString))
          return _210;
        if ("211".equals(codeString))
          return _211;
        if ("212".equals(codeString))
          return _212;
        if ("213".equals(codeString))
          return _213;
        if ("214".equals(codeString))
          return _214;
        if ("215".equals(codeString))
          return _215;
        if ("216".equals(codeString))
          return _216;
        if ("217".equals(codeString))
          return _217;
        if ("218".equals(codeString))
          return _218;
        if ("219".equals(codeString))
          return _219;
        if ("220".equals(codeString))
          return _220;
        if ("221".equals(codeString))
          return _221;
        if ("222".equals(codeString))
          return _222;
        if ("223".equals(codeString))
          return _223;
        if ("224".equals(codeString))
          return _224;
        if ("225".equals(codeString))
          return _225;
        if ("226".equals(codeString))
          return _226;
        if ("227".equals(codeString))
          return _227;
        if ("228".equals(codeString))
          return _228;
        if ("229".equals(codeString))
          return _229;
        if ("230".equals(codeString))
          return _230;
        if ("231".equals(codeString))
          return _231;
        if ("232".equals(codeString))
          return _232;
        if ("233".equals(codeString))
          return _233;
        if ("234".equals(codeString))
          return _234;
        if ("235".equals(codeString))
          return _235;
        if ("236".equals(codeString))
          return _236;
        if ("237".equals(codeString))
          return _237;
        if ("238".equals(codeString))
          return _238;
        if ("239".equals(codeString))
          return _239;
        if ("240".equals(codeString))
          return _240;
        if ("241".equals(codeString))
          return _241;
        if ("242".equals(codeString))
          return _242;
        if ("243".equals(codeString))
          return _243;
        if ("244".equals(codeString))
          return _244;
        if ("245".equals(codeString))
          return _245;
        if ("246".equals(codeString))
          return _246;
        if ("247".equals(codeString))
          return _247;
        if ("248".equals(codeString))
          return _248;
        if ("249".equals(codeString))
          return _249;
        if ("250".equals(codeString))
          return _250;
        if ("251".equals(codeString))
          return _251;
        if ("252".equals(codeString))
          return _252;
        if ("253".equals(codeString))
          return _253;
        if ("254".equals(codeString))
          return _254;
        if ("255".equals(codeString))
          return _255;
        if ("256".equals(codeString))
          return _256;
        if ("257".equals(codeString))
          return _257;
        if ("258".equals(codeString))
          return _258;
        if ("259".equals(codeString))
          return _259;
        if ("260".equals(codeString))
          return _260;
        if ("261".equals(codeString))
          return _261;
        if ("262".equals(codeString))
          return _262;
        if ("263".equals(codeString))
          return _263;
        if ("264".equals(codeString))
          return _264;
        if ("265".equals(codeString))
          return _265;
        if ("266".equals(codeString))
          return _266;
        if ("267".equals(codeString))
          return _267;
        if ("268".equals(codeString))
          return _268;
        if ("269".equals(codeString))
          return _269;
        if ("270".equals(codeString))
          return _270;
        if ("271".equals(codeString))
          return _271;
        if ("272".equals(codeString))
          return _272;
        if ("273".equals(codeString))
          return _273;
        if ("274".equals(codeString))
          return _274;
        if ("275".equals(codeString))
          return _275;
        if ("276".equals(codeString))
          return _276;
        if ("277".equals(codeString))
          return _277;
        if ("278".equals(codeString))
          return _278;
        if ("279".equals(codeString))
          return _279;
        if ("280".equals(codeString))
          return _280;
        if ("281".equals(codeString))
          return _281;
        if ("282".equals(codeString))
          return _282;
        if ("283".equals(codeString))
          return _283;
        if ("284".equals(codeString))
          return _284;
        if ("285".equals(codeString))
          return _285;
        if ("286".equals(codeString))
          return _286;
        if ("287".equals(codeString))
          return _287;
        if ("288".equals(codeString))
          return _288;
        if ("289".equals(codeString))
          return _289;
        if ("290".equals(codeString))
          return _290;
        if ("291".equals(codeString))
          return _291;
        if ("292".equals(codeString))
          return _292;
        if ("293".equals(codeString))
          return _293;
        if ("294".equals(codeString))
          return _294;
        if ("295".equals(codeString))
          return _295;
        if ("296".equals(codeString))
          return _296;
        if ("297".equals(codeString))
          return _297;
        if ("298".equals(codeString))
          return _298;
        if ("299".equals(codeString))
          return _299;
        if ("300".equals(codeString))
          return _300;
        if ("301".equals(codeString))
          return _301;
        if ("302".equals(codeString))
          return _302;
        if ("303".equals(codeString))
          return _303;
        if ("304".equals(codeString))
          return _304;
        if ("305".equals(codeString))
          return _305;
        if ("306".equals(codeString))
          return _306;
        if ("307".equals(codeString))
          return _307;
        if ("308".equals(codeString))
          return _308;
        if ("309".equals(codeString))
          return _309;
        if ("310".equals(codeString))
          return _310;
        if ("311".equals(codeString))
          return _311;
        if ("312".equals(codeString))
          return _312;
        if ("313".equals(codeString))
          return _313;
        if ("314".equals(codeString))
          return _314;
        if ("315".equals(codeString))
          return _315;
        if ("316".equals(codeString))
          return _316;
        if ("317".equals(codeString))
          return _317;
        if ("318".equals(codeString))
          return _318;
        if ("319".equals(codeString))
          return _319;
        if ("320".equals(codeString))
          return _320;
        if ("321".equals(codeString))
          return _321;
        if ("322".equals(codeString))
          return _322;
        if ("323".equals(codeString))
          return _323;
        if ("324".equals(codeString))
          return _324;
        if ("325".equals(codeString))
          return _325;
        if ("326".equals(codeString))
          return _326;
        if ("327".equals(codeString))
          return _327;
        if ("328".equals(codeString))
          return _328;
        if ("330".equals(codeString))
          return _330;
        if ("331".equals(codeString))
          return _331;
        if ("332".equals(codeString))
          return _332;
        if ("333".equals(codeString))
          return _333;
        if ("334".equals(codeString))
          return _334;
        if ("335".equals(codeString))
          return _335;
        if ("336".equals(codeString))
          return _336;
        if ("337".equals(codeString))
          return _337;
        if ("338".equals(codeString))
          return _338;
        if ("339".equals(codeString))
          return _339;
        if ("340".equals(codeString))
          return _340;
        if ("341".equals(codeString))
          return _341;
        if ("342".equals(codeString))
          return _342;
        if ("343".equals(codeString))
          return _343;
        if ("344".equals(codeString))
          return _344;
        if ("345".equals(codeString))
          return _345;
        if ("346".equals(codeString))
          return _346;
        if ("347".equals(codeString))
          return _347;
        if ("348".equals(codeString))
          return _348;
        if ("349".equals(codeString))
          return _349;
        if ("350".equals(codeString))
          return _350;
        if ("351".equals(codeString))
          return _351;
        if ("352".equals(codeString))
          return _352;
        if ("353".equals(codeString))
          return _353;
        if ("354".equals(codeString))
          return _354;
        if ("355".equals(codeString))
          return _355;
        if ("356".equals(codeString))
          return _356;
        if ("357".equals(codeString))
          return _357;
        if ("358".equals(codeString))
          return _358;
        if ("359".equals(codeString))
          return _359;
        if ("360".equals(codeString))
          return _360;
        if ("361".equals(codeString))
          return _361;
        if ("362".equals(codeString))
          return _362;
        if ("364".equals(codeString))
          return _364;
        if ("365".equals(codeString))
          return _365;
        if ("366".equals(codeString))
          return _366;
        if ("367".equals(codeString))
          return _367;
        if ("368".equals(codeString))
          return _368;
        if ("369".equals(codeString))
          return _369;
        if ("370".equals(codeString))
          return _370;
        if ("371".equals(codeString))
          return _371;
        if ("372".equals(codeString))
          return _372;
        if ("373".equals(codeString))
          return _373;
        if ("374".equals(codeString))
          return _374;
        if ("375".equals(codeString))
          return _375;
        if ("376".equals(codeString))
          return _376;
        if ("377".equals(codeString))
          return _377;
        if ("378".equals(codeString))
          return _378;
        if ("379".equals(codeString))
          return _379;
        if ("380".equals(codeString))
          return _380;
        if ("381".equals(codeString))
          return _381;
        if ("382".equals(codeString))
          return _382;
        if ("383".equals(codeString))
          return _383;
        if ("384".equals(codeString))
          return _384;
        if ("385".equals(codeString))
          return _385;
        if ("386".equals(codeString))
          return _386;
        if ("387".equals(codeString))
          return _387;
        if ("388".equals(codeString))
          return _388;
        if ("389".equals(codeString))
          return _389;
        if ("390".equals(codeString))
          return _390;
        if ("391".equals(codeString))
          return _391;
        if ("392".equals(codeString))
          return _392;
        if ("393".equals(codeString))
          return _393;
        if ("394".equals(codeString))
          return _394;
        if ("395".equals(codeString))
          return _395;
        if ("396".equals(codeString))
          return _396;
        if ("397".equals(codeString))
          return _397;
        if ("398".equals(codeString))
          return _398;
        if ("399".equals(codeString))
          return _399;
        if ("400".equals(codeString))
          return _400;
        if ("401".equals(codeString))
          return _401;
        if ("402".equals(codeString))
          return _402;
        if ("403".equals(codeString))
          return _403;
        if ("404".equals(codeString))
          return _404;
        if ("405".equals(codeString))
          return _405;
        if ("406".equals(codeString))
          return _406;
        if ("407".equals(codeString))
          return _407;
        if ("408".equals(codeString))
          return _408;
        if ("409".equals(codeString))
          return _409;
        if ("410".equals(codeString))
          return _410;
        if ("411".equals(codeString))
          return _411;
        if ("412".equals(codeString))
          return _412;
        if ("413".equals(codeString))
          return _413;
        if ("414".equals(codeString))
          return _414;
        if ("415".equals(codeString))
          return _415;
        if ("416".equals(codeString))
          return _416;
        if ("417".equals(codeString))
          return _417;
        if ("418".equals(codeString))
          return _418;
        if ("419".equals(codeString))
          return _419;
        if ("420".equals(codeString))
          return _420;
        if ("421".equals(codeString))
          return _421;
        if ("422".equals(codeString))
          return _422;
        if ("423".equals(codeString))
          return _423;
        if ("424".equals(codeString))
          return _424;
        if ("425".equals(codeString))
          return _425;
        if ("426".equals(codeString))
          return _426;
        if ("427".equals(codeString))
          return _427;
        if ("428".equals(codeString))
          return _428;
        if ("429".equals(codeString))
          return _429;
        if ("430".equals(codeString))
          return _430;
        if ("431".equals(codeString))
          return _431;
        if ("432".equals(codeString))
          return _432;
        if ("433".equals(codeString))
          return _433;
        if ("434".equals(codeString))
          return _434;
        if ("435".equals(codeString))
          return _435;
        if ("436".equals(codeString))
          return _436;
        if ("437".equals(codeString))
          return _437;
        if ("438".equals(codeString))
          return _438;
        if ("439".equals(codeString))
          return _439;
        if ("440".equals(codeString))
          return _440;
        if ("441".equals(codeString))
          return _441;
        if ("442".equals(codeString))
          return _442;
        if ("443".equals(codeString))
          return _443;
        if ("444".equals(codeString))
          return _444;
        if ("445".equals(codeString))
          return _445;
        if ("446".equals(codeString))
          return _446;
        if ("447".equals(codeString))
          return _447;
        if ("448".equals(codeString))
          return _448;
        if ("449".equals(codeString))
          return _449;
        if ("450".equals(codeString))
          return _450;
        if ("451".equals(codeString))
          return _451;
        if ("452".equals(codeString))
          return _452;
        if ("453".equals(codeString))
          return _453;
        if ("454".equals(codeString))
          return _454;
        if ("455".equals(codeString))
          return _455;
        if ("456".equals(codeString))
          return _456;
        if ("457".equals(codeString))
          return _457;
        if ("458".equals(codeString))
          return _458;
        if ("459".equals(codeString))
          return _459;
        if ("460".equals(codeString))
          return _460;
        if ("461".equals(codeString))
          return _461;
        if ("462".equals(codeString))
          return _462;
        if ("463".equals(codeString))
          return _463;
        if ("464".equals(codeString))
          return _464;
        if ("465".equals(codeString))
          return _465;
        if ("466".equals(codeString))
          return _466;
        if ("467".equals(codeString))
          return _467;
        if ("468".equals(codeString))
          return _468;
        if ("469".equals(codeString))
          return _469;
        if ("470".equals(codeString))
          return _470;
        if ("471".equals(codeString))
          return _471;
        if ("472".equals(codeString))
          return _472;
        if ("473".equals(codeString))
          return _473;
        if ("474".equals(codeString))
          return _474;
        if ("475".equals(codeString))
          return _475;
        if ("476".equals(codeString))
          return _476;
        if ("477".equals(codeString))
          return _477;
        if ("478".equals(codeString))
          return _478;
        if ("479".equals(codeString))
          return _479;
        if ("480".equals(codeString))
          return _480;
        if ("481".equals(codeString))
          return _481;
        if ("482".equals(codeString))
          return _482;
        if ("483".equals(codeString))
          return _483;
        if ("484".equals(codeString))
          return _484;
        if ("485".equals(codeString))
          return _485;
        if ("486".equals(codeString))
          return _486;
        if ("488".equals(codeString))
          return _488;
        if ("489".equals(codeString))
          return _489;
        if ("490".equals(codeString))
          return _490;
        if ("491".equals(codeString))
          return _491;
        if ("492".equals(codeString))
          return _492;
        if ("493".equals(codeString))
          return _493;
        if ("494".equals(codeString))
          return _494;
        if ("495".equals(codeString))
          return _495;
        if ("496".equals(codeString))
          return _496;
        if ("497".equals(codeString))
          return _497;
        if ("498".equals(codeString))
          return _498;
        if ("500".equals(codeString))
          return _500;
        if ("501".equals(codeString))
          return _501;
        if ("502".equals(codeString))
          return _502;
        if ("503".equals(codeString))
          return _503;
        if ("504".equals(codeString))
          return _504;
        if ("505".equals(codeString))
          return _505;
        if ("506".equals(codeString))
          return _506;
        if ("507".equals(codeString))
          return _507;
        if ("508".equals(codeString))
          return _508;
        if ("509".equals(codeString))
          return _509;
        if ("510".equals(codeString))
          return _510;
        if ("513".equals(codeString))
          return _513;
        if ("514".equals(codeString))
          return _514;
        if ("530".equals(codeString))
          return _530;
        if ("531".equals(codeString))
          return _531;
        if ("532".equals(codeString))
          return _532;
        if ("533".equals(codeString))
          return _533;
        if ("534".equals(codeString))
          return _534;
        if ("535".equals(codeString))
          return _535;
        if ("536".equals(codeString))
          return _536;
        if ("537".equals(codeString))
          return _537;
        if ("538".equals(codeString))
          return _538;
        if ("539".equals(codeString))
          return _539;
        if ("540".equals(codeString))
          return _540;
        if ("541".equals(codeString))
          return _541;
        if ("542".equals(codeString))
          return _542;
        if ("543".equals(codeString))
          return _543;
        if ("544".equals(codeString))
          return _544;
        if ("545".equals(codeString))
          return _545;
        if ("546".equals(codeString))
          return _546;
        if ("547".equals(codeString))
          return _547;
        if ("548".equals(codeString))
          return _548;
        if ("550".equals(codeString))
          return _550;
        if ("551".equals(codeString))
          return _551;
        if ("552".equals(codeString))
          return _552;
        if ("553".equals(codeString))
          return _553;
        if ("554".equals(codeString))
          return _554;
        if ("555".equals(codeString))
          return _555;
        if ("556".equals(codeString))
          return _556;
        if ("557".equals(codeString))
          return _557;
        if ("558".equals(codeString))
          return _558;
        if ("559".equals(codeString))
          return _559;
        if ("560".equals(codeString))
          return _560;
        if ("561".equals(codeString))
          return _561;
        if ("562".equals(codeString))
          return _562;
        if ("563".equals(codeString))
          return _563;
        if ("564".equals(codeString))
          return _564;
        if ("565".equals(codeString))
          return _565;
        if ("566".equals(codeString))
          return _566;
        if ("567".equals(codeString))
          return _567;
        if ("568".equals(codeString))
          return _568;
        if ("569".equals(codeString))
          return _569;
        if ("570".equals(codeString))
          return _570;
        if ("571".equals(codeString))
          return _571;
        if ("572".equals(codeString))
          return _572;
        if ("573".equals(codeString))
          return _573;
        if ("574".equals(codeString))
          return _574;
        if ("575".equals(codeString))
          return _575;
        if ("576".equals(codeString))
          return _576;
        if ("577".equals(codeString))
          return _577;
        if ("580".equals(codeString))
          return _580;
        if ("581".equals(codeString))
          return _581;
        if ("582".equals(codeString))
          return _582;
        if ("583".equals(codeString))
          return _583;
        if ("584".equals(codeString))
          return _584;
        if ("585".equals(codeString))
          return _585;
        if ("589".equals(codeString))
          return _589;
        if ("590".equals(codeString))
          return _590;
        if ("591".equals(codeString))
          return _591;
        if ("593".equals(codeString))
          return _593;
        if ("599".equals(codeString))
          return _599;
        if ("600".equals(codeString))
          return _600;
        if ("601".equals(codeString))
          return _601;
        if ("602".equals(codeString))
          return _602;
        if ("603".equals(codeString))
          return _603;
        if ("604".equals(codeString))
          return _604;
        if ("605".equals(codeString))
          return _605;
        if ("606".equals(codeString))
          return _606;
        if ("607".equals(codeString))
          return _607;
        if ("608".equals(codeString))
          return _608;
        if ("609".equals(codeString))
          return _609;
        if ("610".equals(codeString))
          return _610;
        if ("611".equals(codeString))
          return _611;
        if ("612".equals(codeString))
          return _612;
        if ("613".equals(codeString))
          return _613;
        if ("614".equals(codeString))
          return _614;
        if ("615".equals(codeString))
          return _615;
        if ("616".equals(codeString))
          return _616;
        if ("617".equals(codeString))
          return _617;
        if ("618".equals(codeString))
          return _618;
        if ("619".equals(codeString))
          return _619;
        if ("620".equals(codeString))
          return _620;
        if ("621".equals(codeString))
          return _621;
        if ("622".equals(codeString))
          return _622;
        if ("623".equals(codeString))
          return _623;
        if ("624".equals(codeString))
          return _624;
        if ("625".equals(codeString))
          return _625;
        if ("626".equals(codeString))
          return _626;
        if ("627".equals(codeString))
          return _627;
        if ("628".equals(codeString))
          return _628;
        if ("629".equals(codeString))
          return _629;
        throw new FHIRException("Unknown ServiceType code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _1: return "1";
            case _2: return "2";
            case _3: return "3";
            case _4: return "4";
            case _5: return "5";
            case _6: return "6";
            case _7: return "7";
            case _8: return "8";
            case _9: return "9";
            case _10: return "10";
            case _11: return "11";
            case _12: return "12";
            case _13: return "13";
            case _14: return "14";
            case _15: return "15";
            case _16: return "16";
            case _17: return "17";
            case _18: return "18";
            case _19: return "19";
            case _20: return "20";
            case _21: return "21";
            case _22: return "22";
            case _23: return "23";
            case _24: return "24";
            case _25: return "25";
            case _26: return "26";
            case _27: return "27";
            case _28: return "28";
            case _29: return "29";
            case _30: return "30";
            case _31: return "31";
            case _32: return "32";
            case _33: return "33";
            case _34: return "34";
            case _35: return "35";
            case _36: return "36";
            case _37: return "37";
            case _38: return "38";
            case _39: return "39";
            case _40: return "40";
            case _41: return "41";
            case _42: return "42";
            case _43: return "43";
            case _44: return "44";
            case _45: return "45";
            case _46: return "46";
            case _47: return "47";
            case _48: return "48";
            case _49: return "49";
            case _50: return "50";
            case _51: return "51";
            case _52: return "52";
            case _53: return "53";
            case _54: return "54";
            case _55: return "55";
            case _56: return "56";
            case _57: return "57";
            case _58: return "58";
            case _59: return "59";
            case _60: return "60";
            case _61: return "61";
            case _62: return "62";
            case _63: return "63";
            case _64: return "64";
            case _65: return "65";
            case _66: return "66";
            case _67: return "67";
            case _68: return "68";
            case _69: return "69";
            case _70: return "70";
            case _71: return "71";
            case _72: return "72";
            case _73: return "73";
            case _74: return "74";
            case _75: return "75";
            case _76: return "76";
            case _77: return "77";
            case _78: return "78";
            case _79: return "79";
            case _80: return "80";
            case _81: return "81";
            case _82: return "82";
            case _83: return "83";
            case _84: return "84";
            case _85: return "85";
            case _86: return "86";
            case _87: return "87";
            case _88: return "88";
            case _89: return "89";
            case _90: return "90";
            case _91: return "91";
            case _92: return "92";
            case _93: return "93";
            case _94: return "94";
            case _95: return "95";
            case _96: return "96";
            case _97: return "97";
            case _98: return "98";
            case _99: return "99";
            case _100: return "100";
            case _101: return "101";
            case _102: return "102";
            case _103: return "103";
            case _104: return "104";
            case _105: return "105";
            case _106: return "106";
            case _107: return "107";
            case _108: return "108";
            case _109: return "109";
            case _110: return "110";
            case _111: return "111";
            case _112: return "112";
            case _113: return "113";
            case _114: return "114";
            case _115: return "115";
            case _116: return "116";
            case _117: return "117";
            case _118: return "118";
            case _119: return "119";
            case _120: return "120";
            case _121: return "121";
            case _122: return "122";
            case _123: return "123";
            case _124: return "124";
            case _125: return "125";
            case _126: return "126";
            case _127: return "127";
            case _128: return "128";
            case _129: return "129";
            case _130: return "130";
            case _131: return "131";
            case _132: return "132";
            case _133: return "133";
            case _134: return "134";
            case _135: return "135";
            case _136: return "136";
            case _137: return "137";
            case _138: return "138";
            case _139: return "139";
            case _140: return "140";
            case _141: return "141";
            case _142: return "142";
            case _143: return "143";
            case _144: return "144";
            case _145: return "145";
            case _146: return "146";
            case _147: return "147";
            case _148: return "148";
            case _149: return "149";
            case _150: return "150";
            case _151: return "151";
            case _152: return "152";
            case _153: return "153";
            case _154: return "154";
            case _155: return "155";
            case _156: return "156";
            case _157: return "157";
            case _158: return "158";
            case _159: return "159";
            case _160: return "160";
            case _161: return "161";
            case _162: return "162";
            case _163: return "163";
            case _164: return "164";
            case _165: return "165";
            case _166: return "166";
            case _167: return "167";
            case _168: return "168";
            case _169: return "169";
            case _170: return "170";
            case _171: return "171";
            case _172: return "172";
            case _173: return "173";
            case _174: return "174";
            case _175: return "175";
            case _176: return "176";
            case _177: return "177";
            case _178: return "178";
            case _179: return "179";
            case _180: return "180";
            case _181: return "181";
            case _182: return "182";
            case _183: return "183";
            case _184: return "184";
            case _185: return "185";
            case _186: return "186";
            case _187: return "187";
            case _188: return "188";
            case _189: return "189";
            case _190: return "190";
            case _191: return "191";
            case _192: return "192";
            case _193: return "193";
            case _194: return "194";
            case _195: return "195";
            case _196: return "196";
            case _197: return "197";
            case _198: return "198";
            case _199: return "199";
            case _200: return "200";
            case _201: return "201";
            case _202: return "202";
            case _203: return "203";
            case _204: return "204";
            case _205: return "205";
            case _206: return "206";
            case _207: return "207";
            case _208: return "208";
            case _209: return "209";
            case _210: return "210";
            case _211: return "211";
            case _212: return "212";
            case _213: return "213";
            case _214: return "214";
            case _215: return "215";
            case _216: return "216";
            case _217: return "217";
            case _218: return "218";
            case _219: return "219";
            case _220: return "220";
            case _221: return "221";
            case _222: return "222";
            case _223: return "223";
            case _224: return "224";
            case _225: return "225";
            case _226: return "226";
            case _227: return "227";
            case _228: return "228";
            case _229: return "229";
            case _230: return "230";
            case _231: return "231";
            case _232: return "232";
            case _233: return "233";
            case _234: return "234";
            case _235: return "235";
            case _236: return "236";
            case _237: return "237";
            case _238: return "238";
            case _239: return "239";
            case _240: return "240";
            case _241: return "241";
            case _242: return "242";
            case _243: return "243";
            case _244: return "244";
            case _245: return "245";
            case _246: return "246";
            case _247: return "247";
            case _248: return "248";
            case _249: return "249";
            case _250: return "250";
            case _251: return "251";
            case _252: return "252";
            case _253: return "253";
            case _254: return "254";
            case _255: return "255";
            case _256: return "256";
            case _257: return "257";
            case _258: return "258";
            case _259: return "259";
            case _260: return "260";
            case _261: return "261";
            case _262: return "262";
            case _263: return "263";
            case _264: return "264";
            case _265: return "265";
            case _266: return "266";
            case _267: return "267";
            case _268: return "268";
            case _269: return "269";
            case _270: return "270";
            case _271: return "271";
            case _272: return "272";
            case _273: return "273";
            case _274: return "274";
            case _275: return "275";
            case _276: return "276";
            case _277: return "277";
            case _278: return "278";
            case _279: return "279";
            case _280: return "280";
            case _281: return "281";
            case _282: return "282";
            case _283: return "283";
            case _284: return "284";
            case _285: return "285";
            case _286: return "286";
            case _287: return "287";
            case _288: return "288";
            case _289: return "289";
            case _290: return "290";
            case _291: return "291";
            case _292: return "292";
            case _293: return "293";
            case _294: return "294";
            case _295: return "295";
            case _296: return "296";
            case _297: return "297";
            case _298: return "298";
            case _299: return "299";
            case _300: return "300";
            case _301: return "301";
            case _302: return "302";
            case _303: return "303";
            case _304: return "304";
            case _305: return "305";
            case _306: return "306";
            case _307: return "307";
            case _308: return "308";
            case _309: return "309";
            case _310: return "310";
            case _311: return "311";
            case _312: return "312";
            case _313: return "313";
            case _314: return "314";
            case _315: return "315";
            case _316: return "316";
            case _317: return "317";
            case _318: return "318";
            case _319: return "319";
            case _320: return "320";
            case _321: return "321";
            case _322: return "322";
            case _323: return "323";
            case _324: return "324";
            case _325: return "325";
            case _326: return "326";
            case _327: return "327";
            case _328: return "328";
            case _330: return "330";
            case _331: return "331";
            case _332: return "332";
            case _333: return "333";
            case _334: return "334";
            case _335: return "335";
            case _336: return "336";
            case _337: return "337";
            case _338: return "338";
            case _339: return "339";
            case _340: return "340";
            case _341: return "341";
            case _342: return "342";
            case _343: return "343";
            case _344: return "344";
            case _345: return "345";
            case _346: return "346";
            case _347: return "347";
            case _348: return "348";
            case _349: return "349";
            case _350: return "350";
            case _351: return "351";
            case _352: return "352";
            case _353: return "353";
            case _354: return "354";
            case _355: return "355";
            case _356: return "356";
            case _357: return "357";
            case _358: return "358";
            case _359: return "359";
            case _360: return "360";
            case _361: return "361";
            case _362: return "362";
            case _364: return "364";
            case _365: return "365";
            case _366: return "366";
            case _367: return "367";
            case _368: return "368";
            case _369: return "369";
            case _370: return "370";
            case _371: return "371";
            case _372: return "372";
            case _373: return "373";
            case _374: return "374";
            case _375: return "375";
            case _376: return "376";
            case _377: return "377";
            case _378: return "378";
            case _379: return "379";
            case _380: return "380";
            case _381: return "381";
            case _382: return "382";
            case _383: return "383";
            case _384: return "384";
            case _385: return "385";
            case _386: return "386";
            case _387: return "387";
            case _388: return "388";
            case _389: return "389";
            case _390: return "390";
            case _391: return "391";
            case _392: return "392";
            case _393: return "393";
            case _394: return "394";
            case _395: return "395";
            case _396: return "396";
            case _397: return "397";
            case _398: return "398";
            case _399: return "399";
            case _400: return "400";
            case _401: return "401";
            case _402: return "402";
            case _403: return "403";
            case _404: return "404";
            case _405: return "405";
            case _406: return "406";
            case _407: return "407";
            case _408: return "408";
            case _409: return "409";
            case _410: return "410";
            case _411: return "411";
            case _412: return "412";
            case _413: return "413";
            case _414: return "414";
            case _415: return "415";
            case _416: return "416";
            case _417: return "417";
            case _418: return "418";
            case _419: return "419";
            case _420: return "420";
            case _421: return "421";
            case _422: return "422";
            case _423: return "423";
            case _424: return "424";
            case _425: return "425";
            case _426: return "426";
            case _427: return "427";
            case _428: return "428";
            case _429: return "429";
            case _430: return "430";
            case _431: return "431";
            case _432: return "432";
            case _433: return "433";
            case _434: return "434";
            case _435: return "435";
            case _436: return "436";
            case _437: return "437";
            case _438: return "438";
            case _439: return "439";
            case _440: return "440";
            case _441: return "441";
            case _442: return "442";
            case _443: return "443";
            case _444: return "444";
            case _445: return "445";
            case _446: return "446";
            case _447: return "447";
            case _448: return "448";
            case _449: return "449";
            case _450: return "450";
            case _451: return "451";
            case _452: return "452";
            case _453: return "453";
            case _454: return "454";
            case _455: return "455";
            case _456: return "456";
            case _457: return "457";
            case _458: return "458";
            case _459: return "459";
            case _460: return "460";
            case _461: return "461";
            case _462: return "462";
            case _463: return "463";
            case _464: return "464";
            case _465: return "465";
            case _466: return "466";
            case _467: return "467";
            case _468: return "468";
            case _469: return "469";
            case _470: return "470";
            case _471: return "471";
            case _472: return "472";
            case _473: return "473";
            case _474: return "474";
            case _475: return "475";
            case _476: return "476";
            case _477: return "477";
            case _478: return "478";
            case _479: return "479";
            case _480: return "480";
            case _481: return "481";
            case _482: return "482";
            case _483: return "483";
            case _484: return "484";
            case _485: return "485";
            case _486: return "486";
            case _488: return "488";
            case _489: return "489";
            case _490: return "490";
            case _491: return "491";
            case _492: return "492";
            case _493: return "493";
            case _494: return "494";
            case _495: return "495";
            case _496: return "496";
            case _497: return "497";
            case _498: return "498";
            case _500: return "500";
            case _501: return "501";
            case _502: return "502";
            case _503: return "503";
            case _504: return "504";
            case _505: return "505";
            case _506: return "506";
            case _507: return "507";
            case _508: return "508";
            case _509: return "509";
            case _510: return "510";
            case _513: return "513";
            case _514: return "514";
            case _530: return "530";
            case _531: return "531";
            case _532: return "532";
            case _533: return "533";
            case _534: return "534";
            case _535: return "535";
            case _536: return "536";
            case _537: return "537";
            case _538: return "538";
            case _539: return "539";
            case _540: return "540";
            case _541: return "541";
            case _542: return "542";
            case _543: return "543";
            case _544: return "544";
            case _545: return "545";
            case _546: return "546";
            case _547: return "547";
            case _548: return "548";
            case _550: return "550";
            case _551: return "551";
            case _552: return "552";
            case _553: return "553";
            case _554: return "554";
            case _555: return "555";
            case _556: return "556";
            case _557: return "557";
            case _558: return "558";
            case _559: return "559";
            case _560: return "560";
            case _561: return "561";
            case _562: return "562";
            case _563: return "563";
            case _564: return "564";
            case _565: return "565";
            case _566: return "566";
            case _567: return "567";
            case _568: return "568";
            case _569: return "569";
            case _570: return "570";
            case _571: return "571";
            case _572: return "572";
            case _573: return "573";
            case _574: return "574";
            case _575: return "575";
            case _576: return "576";
            case _577: return "577";
            case _580: return "580";
            case _581: return "581";
            case _582: return "582";
            case _583: return "583";
            case _584: return "584";
            case _585: return "585";
            case _589: return "589";
            case _590: return "590";
            case _591: return "591";
            case _593: return "593";
            case _599: return "599";
            case _600: return "600";
            case _601: return "601";
            case _602: return "602";
            case _603: return "603";
            case _604: return "604";
            case _605: return "605";
            case _606: return "606";
            case _607: return "607";
            case _608: return "608";
            case _609: return "609";
            case _610: return "610";
            case _611: return "611";
            case _612: return "612";
            case _613: return "613";
            case _614: return "614";
            case _615: return "615";
            case _616: return "616";
            case _617: return "617";
            case _618: return "618";
            case _619: return "619";
            case _620: return "620";
            case _621: return "621";
            case _622: return "622";
            case _623: return "623";
            case _624: return "624";
            case _625: return "625";
            case _626: return "626";
            case _627: return "627";
            case _628: return "628";
            case _629: return "629";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/service-type";
        }
        public String getDefinition() {
          switch (this) {
            case _1: return "Adoption & permanent care information/support";
            case _2: return "Aged care assessment";
            case _3: return "Aged Care information/referral";
            case _4: return "Aged Residential Care";
            case _5: return "Case management for older persons";
            case _6: return "Delivered meals (meals on wheels)";
            case _7: return "Friendly visiting";
            case _8: return "Home care/housekeeping assistance";
            case _9: return "Home maintenance and repair";
            case _10: return "Personal alarms/alerts";
            case _11: return "Personal care for older persons";
            case _12: return "Planned activity groups";
            case _13: return "Acupuncture";
            case _14: return "Alexander technique therapy";
            case _15: return "Aromatherapy";
            case _16: return "Biorhythm services";
            case _17: return "Bowen therapy";
            case _18: return "Chinese herbal medicine";
            case _19: return "Feldenkrais";
            case _20: return "Homoeopathy";
            case _21: return "Hydrotherapy";
            case _22: return "Hypnotherapy";
            case _23: return "Kinesiology";
            case _24: return "Magnetic therapy";
            case _25: return "Massage therapy";
            case _26: return "Meditation";
            case _27: return "Myotherapy";
            case _28: return "Naturopathy";
            case _29: return "Reflexology";
            case _30: return "Reiki";
            case _31: return "Relaxation therapy";
            case _32: return "Shiatsu";
            case _33: return "Western herbal medicine";
            case _34: return "Family day care";
            case _35: return "Holiday programs";
            case _36: return "Kindergarten inclusion support for children with a disability";
            case _37: return "Kindergarten/preschool";
            case _38: return "Long day child care";
            case _39: return "Occasional child care";
            case _40: return "Outside school hours care";
            case _41: return "Children's play programs";
            case _42: return "Parenting & family management support/education";
            case _43: return "Playgroup";
            case _44: return "School nursing";
            case _45: return "Toy library";
            case _46: return "Child protection/child abuse report";
            case _47: return "Foster care";
            case _48: return "Residential/ out of home care";
            case _49: return "Support for young people leaving care";
            case _50: return "Audiology";
            case _51: return "Blood donation";
            case _52: return "Chiropractic";
            case _53: return "Dietetics";
            case _54: return "Family planning";
            case _55: return "Health advocacy/Liaison service";
            case _56: return "Health information/referral";
            case _57: return "Immunization";
            case _58: return "Maternal & child health";
            case _59: return "Nursing";
            case _60: return "Nutrition";
            case _61: return "Occupational therapy";
            case _62: return "Optometry";
            case _63: return "Osteopathy";
            case _64: return "Pharmacy";
            case _65: return "Physiotherapy";
            case _66: return "Podiatry";
            case _67: return "Sexual health";
            case _68: return "Speech pathology/therapy";
            case _69: return "Bereavement counselling";
            case _70: return "Crisis counselling";
            case _71: return "Family counselling and/or family therapy";
            case _72: return "Family violence counselling";
            case _73: return "Financial counselling";
            case _74: return "Generalist counselling";
            case _75: return "Genetic counselling";
            case _76: return "Health counselling";
            case _77: return "Mediation";
            case _78: return "Problem gambling counselling";
            case _79: return "Relationship counselling";
            case _80: return "Sexual assault counselling";
            case _81: return "Trauma counselling";
            case _82: return "Victims of crime counselling";
            case _83: return "Cemetery operation";
            case _84: return "Cremation";
            case _85: return "Death service information";
            case _86: return "Funeral services";
            case _87: return "Endodontic";
            case _88: return "General dental";
            case _89: return "Oral medicine";
            case _90: return "Oral surgery";
            case _91: return "Orthodontic";
            case _92: return "Paediatric Dentistry";
            case _93: return "Periodontic";
            case _94: return "Prosthodontic";
            case _95: return "Acquired brain injury information/referral";
            case _96: return "Disability advocacy";
            case _97: return "Disability aids & equipment";
            case _98: return "Disability case management";
            case _99: return "Disability day programs & activities";
            case _100: return "Disability information/referral";
            case _101: return "Disability support packages";
            case _102: return "Disability supported accommodation";
            case _103: return "Early childhood intervention";
            case _104: return "Hearing aids & equipment";
            case _105: return "Drug and/or alcohol counselling";
            case _106: return "Drug and/or alcohol information/referral";
            case _107: return "Needle & Syringe exchange";
            case _108: return "Non-residential alcohol and/or drug dependence treatment";
            case _109: return "Pharmacotherapy (eg. methadone) program";
            case _110: return "Quit program";
            case _111: return "Residential alcohol and/or drug dependence treatment";
            case _112: return "Adult/community education";
            case _113: return "Higher education";
            case _114: return "Primary education";
            case _115: return "Secondary education";
            case _116: return "Training & vocational education";
            case _117: return "Emergency medical";
            case _118: return "Employment placement and/or support";
            case _119: return "Vocational Rehabilitation";
            case _120: return "Workplace safety and/or accident prevention";
            case _121: return "Financial assistance";
            case _122: return "Financial information/advice";
            case _123: return "Material aid";
            case _124: return "General Practice/GP (doctor)";
            case _125: return "Accommodation placement and/or support";
            case _126: return "Crisis/emergency accommodation";
            case _127: return "Homelessness support";
            case _128: return "Housing information/referral";
            case _129: return "Public rental housing";
            case _130: return "Interpreting/Multilingual/Language service";
            case _131: return "Juvenile Justice";
            case _132: return "Legal advocacy";
            case _133: return "Legal information/advice/referral";
            case _134: return "Mental health advocacy";
            case _135: return "Mental health assessment/triage/crisis response";
            case _136: return "Mental health case management/continuing care";
            case _137: return "Mental health information/referral";
            case _138: return "Mental health inpatient services (hospital psychiatric unit) - requires referral";
            case _139: return "Mental health non-residential rehabilitation";
            case _140: return "Mental health residential rehabilitation/community care unit";
            case _141: return "Psychiatry (requires referral)";
            case _142: return "Psychology";
            case _143: return "Martial arts";
            case _144: return "Personal fitness training";
            case _145: return "Physical activity group";
            case _146: return "Physical activity programs";
            case _147: return "Physical fitness testing";
            case _148: return "Pilates";
            case _149: return "Self defence";
            case _150: return "Sporting club";
            case _151: return "Yoga";
            case _152: return "Food safety";
            case _153: return "Health regulatory, inspection and/or certification";
            case _154: return "Workplace health and/or safety inspection and/or certification";
            case _155: return "Carer support";
            case _156: return "Respite care";
            case _157: return "Anatomical Pathology (including Cytopathology & Forensic Pathology)";
            case _158: return "Pathology - Clinical Chemistry";
            case _159: return "Pathology - General";
            case _160: return "Pathology - Genetics";
            case _161: return "Pathology - Haematology";
            case _162: return "Pathology - Immunology";
            case _163: return "Pathology - Microbiology";
            case _164: return "Anaesthesiology - Pain Medicine";
            case _165: return "Cardiology";
            case _166: return "Clinical Genetics";
            case _167: return "Clinical Pharmacology";
            case _168: return "Dermatology";
            case _169: return "Endocrinology";
            case _170: return "Gastroenterology & Hepatology";
            case _171: return "Geriatric medicine";
            case _172: return "Immunology & Allergy";
            case _173: return "Infectious diseases";
            case _174: return "Intensive care medicine";
            case _175: return "Medical Oncology";
            case _176: return "Nephrology";
            case _177: return "Neurology";
            case _178: return "Occupational Medicine";
            case _179: return "Palliative Medicine";
            case _180: return "Public Health Medicine";
            case _181: return "Rehabilitation Medicine";
            case _182: return "Rheumatology";
            case _183: return "Sleep Medicine";
            case _184: return "Thoracic medicine";
            case _185: return "Gynaecological Oncology";
            case _186: return "Obstetrics & Gynaecology";
            case _187: return "Reproductive Endocrinology & Infertility";
            case _188: return "Urogynaecology";
            case _189: return "Neonatology & Perinatology";
            case _190: return "Paediatric Cardiology";
            case _191: return "Paediatric Clinical Genetics";
            case _192: return "Paediatric Clinical Pharmacology";
            case _193: return "Paediatric Endocrinology";
            case _194: return "Paediatric Gastroenterology & Hepatology";
            case _195: return "Paediatric Haematology";
            case _196: return "Paediatric Immunology & Allergy";
            case _197: return "Paediatric Infectious diseases";
            case _198: return "Paediatric intensive care medicine";
            case _199: return "Paediatric Medical Oncology";
            case _200: return "Paediatric Medicine";
            case _201: return "Paediatric Nephrology";
            case _202: return "Paediatric Neurology";
            case _203: return "Paediatric Nuclear Medicine";
            case _204: return "Paediatric Rehabilitation Medicine";
            case _205: return "Paediatric Rheumatology";
            case _206: return "Paediatric Sleep Medicine";
            case _207: return "Paediatric Surgery";
            case _208: return "Paediatric Thoracic Medicine";
            case _209: return "Diagnostic Radiology/Xray/CT/Fluoroscopy";
            case _210: return "Diagnostic Ultrasound";
            case _211: return "Magnetic Resonance Imaging (MRI)";
            case _212: return "Nuclear Medicine";
            case _213: return "Obstetric & Gynaecological Ultrasound";
            case _214: return "Radiation oncology";
            case _215: return "Cardiothoracic surgery";
            case _216: return "Neurosurgery";
            case _217: return "Ophthalmology";
            case _218: return "Orthopaedic surgery";
            case _219: return "Otolaryngology - Head & Neck Surgery";
            case _220: return "Plastic & Reconstructive Surgery";
            case _221: return "Surgery - General";
            case _222: return "Urology";
            case _223: return "Vascular surgery";
            case _224: return "Support groups";
            case _225: return "Air ambulance";
            case _226: return "Ambulance";
            case _227: return "Blood transport";
            case _228: return "Community bus";
            case _229: return "Flying doctor service";
            case _230: return "Patient transport";
            case _231: return "A&E";
            case _232: return "A&EP";
            case _233: return "Abuse";
            case _234: return "ACAS";
            case _235: return "Access";
            case _236: return "Accident";
            case _237: return "Acute Inpatient Service's";
            case _238: return "Adult Day Programs";
            case _239: return "Adult Mental Health Services";
            case _240: return "Advice";
            case _241: return "Advocacy";
            case _242: return "Aged Persons Mental Health Residential Units";
            case _243: return "Aged Persons Mental Health Services";
            case _244: return "Aged Persons Mental Health Teams";
            case _245: return "Aids";
            case _246: return "Al-Anon";
            case _247: return "Alcohol";
            case _248: return "Al-Teen";
            case _249: return "Antenatal";
            case _250: return "Anxiety";
            case _251: return "Arthritis";
            case _252: return "Assessment";
            case _253: return "Assistance";
            case _254: return "Asthma";
            case _255: return "ATSS";
            case _256: return "Attendant Care";
            case _257: return "Babies";
            case _258: return "Bathroom Modification";
            case _259: return "Behaviour";
            case _260: return "Behaviour Intervention";
            case _261: return "Bereavement";
            case _262: return "Bipolar";
            case _263: return "Birth";
            case _264: return "Birth Control";
            case _265: return "Birthing Options";
            case _266: return "BIST";
            case _267: return "Blood";
            case _268: return "Bone";
            case _269: return "Bowel";
            case _270: return "Brain";
            case _271: return "Breast Feeding";
            case _272: return "Breast Screen";
            case _273: return "Brokerage";
            case _274: return "Cancer";
            case _275: return "Cancer Support";
            case _276: return "Cardiovascular Disease";
            case _277: return "Care Packages";
            case _278: return "Carer";
            case _279: return "Case Management";
            case _280: return "Casualty";
            case _281: return "Centrelink";
            case _282: return "Chemists";
            case _283: return "Child And Adolescent Mental Health Services";
            case _284: return "Child Care";
            case _285: return "Child Services";
            case _286: return "Children";
            case _287: return "Children's Services";
            case _288: return "Cholesterol";
            case _289: return "Clothing";
            case _290: return "Community Based Accommodation";
            case _291: return "Community Care Unit";
            case _292: return "Community Child And Adolescent Mental Health Services";
            case _293: return "Community Health";
            case _294: return "Community Residential Unit";
            case _295: return "Community Transport";
            case _296: return "Companion Visiting";
            case _297: return "Companionship";
            case _298: return "Consumer Advice";
            case _299: return "Consumer Issues";
            case _300: return "Continuing Care Services";
            case _301: return "Contraception Information";
            case _302: return "Coordinating Bodies";
            case _303: return "Correctional Services";
            case _304: return "Council Environmental Health";
            case _305: return "Counselling";
            case _306: return "Criminal";
            case _307: return "Crises";
            case _308: return "Crisis Assessment And Treatment Services (Cats)";
            case _309: return "Crisis Assistance";
            case _310: return "Crisis Refuge";
            case _311: return "Day Program";
            case _312: return "Deaf";
            case _313: return "Dental Hygiene";
            case _314: return "Dentistry";
            case _315: return "Dentures";
            case _316: return "Depression";
            case _317: return "Detoxification";
            case _318: return "Diabetes";
            case _319: return "Diaphragm Fitting";
            case _320: return "Dieticians";
            case _321: return "Disabled Parking";
            case _322: return "District Nursing";
            case _323: return "Divorce";
            case _324: return "Doctors";
            case _325: return "Drink-Drive";
            case _326: return "Dual Diagnosis Services";
            case _327: return "Early Choice";
            case _328: return "Eating Disorder";
            case _330: return "Emergency Relief";
            case _331: return "Employment And Training";
            case _332: return "Environment";
            case _333: return "Equipment";
            case _334: return "Exercise";
            case _335: return "Facility";
            case _336: return "Family Choice";
            case _337: return "Family Law";
            case _338: return "Family Options";
            case _339: return "Family Services";
            case _340: return "FFYA";
            case _341: return "Financial Aid";
            case _342: return "Fitness";
            case _343: return "Flexible Care Packages";
            case _344: return "Food";
            case _345: return "Food Vouchers";
            case _346: return "Forensic Mental Health Services";
            case _347: return "Futures";
            case _348: return "Futures For Young Adults";
            case _349: return "General Practitioners";
            case _350: return "Grants";
            case _351: return "Grief";
            case _352: return "Grief Counselling";
            case _353: return "HACC";
            case _354: return "Heart Disease";
            case _355: return "Help";
            case _356: return "High Blood Pressure";
            case _357: return "Home Help";
            case _358: return "Home Nursing";
            case _359: return "Homefirst";
            case _360: return "Hospice Care";
            case _361: return "Hospital Services";
            case _362: return "Hospital To Home";
            case _364: return "Hostel";
            case _365: return "Hostel Accommodation";
            case _366: return "Household Items";
            case _367: return "Hypertension";
            case _368: return "Illness";
            case _369: return "Independent Living";
            case _370: return "Information";
            case _371: return "Injury";
            case _372: return "Intake";
            case _373: return "Intensive Mobile Youth Outreach Services (Imyos)";
            case _374: return "Intervention";
            case _375: return "Job Searching";
            case _376: return "Justice";
            case _377: return "Leisure";
            case _378: return "Loans";
            case _379: return "Low Income Earners";
            case _380: return "Lung";
            case _381: return "Making A Difference";
            case _382: return "Medical Services";
            case _383: return "Medical Specialists";
            case _384: return "Medication Administration";
            case _385: return "Menstrual Information";
            case _386: return "Methadone";
            case _387: return "Mobile Support And Treatment Services (MSTS)";
            case _388: return "Motor Neurone";
            case _389: return "Multiple Sclerosis";
            case _390: return "Neighbourhood House";
            case _391: return "Nursing Home";
            case _392: return "Nursing Mothers";
            case _393: return "Obesity";
            case _394: return "Occupational Health & Safety";
            case _395: return "Optometrist";
            case _396: return "Oral Hygiene";
            case _397: return "Outpatients";
            case _398: return "Outreach Service";
            case _399: return "PADP";
            case _400: return "Pain";
            case _401: return "Pap Smear";
            case _402: return "Parenting";
            case _403: return "Peak Organisations";
            case _404: return "Personal Care";
            case _405: return "Pharmacies";
            case _406: return "Phobias";
            case _407: return "Physical";
            case _408: return "Physical Activity";
            case _409: return "Postnatal";
            case _410: return "Pregnancy";
            case _411: return "Pregnancy Tests";
            case _412: return "Preschool";
            case _413: return "Prescriptions";
            case _414: return "Primary Mental Health And Early Intervention Teams";
            case _415: return "Property Maintenance";
            case _416: return "Prostate";
            case _417: return "Psychiatric";
            case _418: return "Psychiatric Disability Support Services - Home-Based Outreach";
            case _419: return "Psychiatric Disability Support Services - Planned Respite";
            case _420: return "Psychiatric Disability Support Services - Residential Rehabilitation";
            case _421: return "Psychiatric Disability Support Services Home-Based Outreach";
            case _422: return "Psychiatric Disability Support Services Mutual Support And Self Help";
            case _423: return "Psychiatric Support";
            case _424: return "Recreation";
            case _425: return "Referral";
            case _426: return "Refuge";
            case _427: return "Rent Assistance";
            case _428: return "Residential Facilities";
            case _429: return "Residential Respite";
            case _430: return "Respiratory";
            case _431: return "Response";
            case _432: return "Rooming Houses";
            case _433: return "Safe Sex";
            case _434: return "Secure Extended Care Inpatient Services";
            case _435: return "Self Help";
            case _436: return "Separation";
            case _437: return "Services";
            case _438: return "Sex Education";
            case _439: return "Sexual Abuse";
            case _440: return "Sexual Issues";
            case _441: return "Sexually Transmitted Diseases";
            case _442: return "SIDS";
            case _443: return "Social Support";
            case _444: return "Socialisation";
            case _445: return "Special Needs";
            case _446: return "Speech Therapist";
            case _447: return "Splinting";
            case _448: return "Sport";
            case _449: return "Statewide And Specialist Services";
            case _450: return "STD";
            case _451: return "STI";
            case _452: return "Stillbirth";
            case _453: return "Stomal Care";
            case _454: return "Stroke";
            case _455: return "Substance Abuse";
            case _456: return "Support";
            case _457: return "Syringes";
            case _458: return "Teeth";
            case _459: return "Tenancy Advice";
            case _460: return "Terminal Illness";
            case _461: return "Therapy";
            case _462: return "Transcription";
            case _463: return "Translating Services";
            case _464: return "Translator";
            case _465: return "Transport";
            case _466: return "Vertebrae";
            case _467: return "Violence";
            case _468: return "Vocational Guidance";
            case _469: return "Weight";
            case _470: return "Welfare Assistance";
            case _471: return "Welfare Counselling";
            case _472: return "Wheelchairs";
            case _473: return "Wound Management";
            case _474: return "Young People At Risk";
            case _475: return "Further Description - Community Health Care";
            case _476: return "Library";
            case _477: return "Community Hours";
            case _478: return "Further Description - Specialist Medical";
            case _479: return "Hepatology";
            case _480: return "Gastroenterology";
            case _481: return "Gynaecology";
            case _482: return "Obstetrics";
            case _483: return "Further Description - Specialist Surgical";
            case _484: return "Placement Protection";
            case _485: return "Family Violence";
            case _486: return "Integrated Family Services";
            case _488: return "Diabetes Educator";
            case _489: return "Kinship Care";
            case _490: return "General Mental Health Services";
            case _491: return "Exercise Physiology";
            case _492: return "Medical Research";
            case _493: return "Youth";
            case _494: return "Youth Services";
            case _495: return "Youth Health";
            case _496: return "Child and Family Services";
            case _497: return "Home Visits";
            case _498: return "Mobile Services";
            case _500: return "Before and/or After School Care";
            case _501: return "Cancer Services";
            case _502: return "Integrated Cancer Services";
            case _503: return "Multidisciplinary Services";
            case _504: return "Multidisciplinary Cancer Services";
            case _505: return "Meetings";
            case _506: return "Blood pressure monitoring";
            case _507: return "Dose administration aid";
            case _508: return "Medical Equipment Hire";
            case _509: return "Parenting & family support/education";
            case _510: return "Deputising Service";
            case _513: return "Cancer Support Groups";
            case _514: return "Community Cancer Services";
            case _530: return "Disability Care Transport";
            case _531: return "Aged Care Transport";
            case _532: return "Diabetes Education service";
            case _533: return "Cardiac Rehabilitation Service ";
            case _534: return "Young Adult Diabetes services (YADS)";
            case _535: return "Pulmonary Rehabilitation Service";
            case _536: return "Art therapy";
            case _537: return "Medication Reviews";
            case _538: return "Telephone Counselling";
            case _539: return "Telephone Help Line";
            case _540: return "Online Service";
            case _541: return "Crisis - Mental Health";
            case _542: return "Youth Crisis";
            case _543: return "Sexual Assault";
            case _544: return "GPAH Other";
            case _545: return "Paediatric Dermatology";
            case _546: return "Veterans Services";
            case _547: return "Veterans";
            case _548: return "Food Relief/food/meals";
            case _550: return "Dementia Care";
            case _551: return "Alzheimer";
            case _552: return "Drug and/or alcohol support groups";
            case _553: return "One on One Support/Mentoring/Coaching";
            case _554: return "Chronic Disease Management";
            case _555: return "Liaison Services";
            case _556: return "Walk in Centre / non emergency";
            case _557: return "Inpatients";
            case _558: return "Spiritual Counselling";
            case _559: return "Women's Health";
            case _560: return "Men's Health";
            case _561: return "Health education/Health awareness program";
            case _562: return "Test Message";
            case _563: return "Remedial Massage";
            case _564: return "Adolescent Mental Health Services";
            case _565: return "Youth drop in/assistance/support";
            case _566: return "Aboriginal Health Worker";
            case _567: return "Women's Health Clinic";
            case _568: return "Men's Health Clinic ";
            case _569: return "Migrant Health Clinic";
            case _570: return "Refugee Health Clinic";
            case _571: return "Aboriginal Health Clinic";
            case _572: return "Nurse Practitioner lead Clinic/s";
            case _573: return "Nurse lead Clinic/s";
            case _574: return "Culturally tailored support groups";
            case _575: return "Culturally tailored health promotion";
            case _576: return "Rehabilitation";
            case _577: return "Education information/referral";
            case _580: return "Social Work";
            case _581: return "Haematology";
            case _582: return "Maternity Shared Care";
            case _583: return "Rehabilitation Service";
            case _584: return "Cranio-Sacral Therapy";
            case _585: return "Prosthetics & Orthotics";
            case _589: return "Home Medicine Review";
            case _590: return "GPAH - Medical";
            case _591: return "Music Therapy";
            case _593: return "Falls Prevention";
            case _599: return "Accommodation/Tenancy";
            case _600: return "Assess-Skill, Ability, Needs";
            case _601: return "Assist Access/Maintain Employ";
            case _602: return "Assist Prod-Pers Care/Safety";
            case _603: return "Assist-Integrate School/Ed";
            case _604: return "Assist-Life Stage, Transition";
            case _605: return "Assist-Personal Activities";
            case _606: return "Assist-Travel/Transport";
            case _607: return "Assistive Equip-General Tasks";
            case _608: return "Assistive Equip-Recreation";
            case _609: return "Assistive Prod-Household Task";
            case _610: return "Behaviour Support";
            case _611: return "Comms & Info Equipment";
            case _612: return "Community Nursing Care";
            case _613: return "Daily Tasks/Shared Living";
            case _614: return "Development-Life Skills";
            case _615: return "Early Childhood Supports";
            case _616: return "Equipment Special Assess Setup";
            case _617: return "Hearing Equipment";
            case _618: return "Home Modification";
            case _619: return "Household Tasks";
            case _620: return "Interpret/Translate";
            case _621: return "Other Innovative Supports";
            case _622: return "Participate Community";
            case _623: return "Personal Mobility Equipment";
            case _624: return "Physical Wellbeing";
            case _625: return "Plan Management";
            case _626: return "Therapeutic Supports";
            case _627: return "Training-Travel Independence";
            case _628: return "Vehicle modifications";
            case _629: return "Vision Equipment";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _1: return "Adoption/Permanent Care Info/Support";
            case _2: return "Aged Care Assessment";
            case _3: return "Aged Care Information/Referral";
            case _4: return "Aged Residential Care";
            case _5: return "Case Management for Older Persons";
            case _6: return "Delivered Meals (Meals On Wheels)";
            case _7: return "Friendly Visiting";
            case _8: return "Home Care/Housekeeping Assistance";
            case _9: return "Home Maintenance and Repair";
            case _10: return "Personal Alarms/Alerts";
            case _11: return "Personal Care for Older Persons";
            case _12: return "Planned Activity Groups";
            case _13: return "Acupuncture";
            case _14: return "Alexander Technique Therapy";
            case _15: return "Aromatherapy";
            case _16: return "Biorhythm Services";
            case _17: return "Bowen Therapy";
            case _18: return "Chinese Herbal Medicine";
            case _19: return "Feldenkrais";
            case _20: return "Homoeopathy";
            case _21: return "Hydrotherapy";
            case _22: return "Hypnotherapy";
            case _23: return "Kinesiology";
            case _24: return "Magnetic Therapy";
            case _25: return "Massage Therapy";
            case _26: return "Meditation";
            case _27: return "Myotherapy";
            case _28: return "Naturopathy";
            case _29: return "Reflexology";
            case _30: return "Reiki";
            case _31: return "Relaxation Therapy";
            case _32: return "Shiatsu";
            case _33: return "Western Herbal Medicine";
            case _34: return "Family Day care";
            case _35: return "Holiday Programs";
            case _36: return "Kindergarten Inclusion Support ";
            case _37: return "Kindergarten/Preschool";
            case _38: return "Long Day Child Care";
            case _39: return "Occasional Child Care";
            case _40: return "Outside School Hours Care";
            case _41: return "Children's Play Programs";
            case _42: return "Parenting/Family Support/Education";
            case _43: return "Playgroup";
            case _44: return "School Nursing";
            case _45: return "Toy Library";
            case _46: return "Child Protection/Child Abuse Report";
            case _47: return "Foster Care";
            case _48: return "Residential/Out-of-Home Care";
            case _49: return "Support - Young People Leaving Care";
            case _50: return "Audiology";
            case _51: return "Blood Donation";
            case _52: return "Chiropractic";
            case _53: return "Dietetics";
            case _54: return "Family Planning";
            case _55: return "Health Advocacy/Liaison Service";
            case _56: return "Health Information/Referral";
            case _57: return "Immunization";
            case _58: return "Maternal & Child Health";
            case _59: return "Nursing";
            case _60: return "Nutrition";
            case _61: return "Occupational Therapy";
            case _62: return "Optometry";
            case _63: return "Osteopathy";
            case _64: return "Pharmacy";
            case _65: return "Physiotherapy";
            case _66: return "Podiatry";
            case _67: return "Sexual Health";
            case _68: return "Speech Pathology/Therapy";
            case _69: return "Bereavement Counselling";
            case _70: return "Crisis Counselling";
            case _71: return "Family Counselling/Therapy";
            case _72: return "Family Violence Counselling";
            case _73: return "Financial Counselling";
            case _74: return "Generalist Counselling";
            case _75: return "Genetic Counselling";
            case _76: return "Health Counselling";
            case _77: return "Mediation";
            case _78: return "Problem Gambling Counselling";
            case _79: return "Relationship Counselling";
            case _80: return "Sexual Assault Counselling";
            case _81: return "Trauma Counselling";
            case _82: return "Victims of Crime Counselling";
            case _83: return "Cemetery Operation";
            case _84: return "Cremation";
            case _85: return "Death Service Information";
            case _86: return "Funeral Services";
            case _87: return "Endodontic";
            case _88: return "General Dental";
            case _89: return "Oral Medicine";
            case _90: return "Oral Surgery";
            case _91: return "Orthodontic";
            case _92: return "Paediatric Dentistry";
            case _93: return "Periodontic";
            case _94: return "Prosthodontic";
            case _95: return "Acquired Brain Injury Info/Referral";
            case _96: return "Disability Advocacy";
            case _97: return "Disability Aids & Equipment";
            case _98: return "Disability Case Management";
            case _99: return "Disability Day Programs/Activities";
            case _100: return "Disability Information/Referral";
            case _101: return "Disability Support Packages";
            case _102: return "Disability Supported Accommodation";
            case _103: return "Early Childhood Intervention";
            case _104: return "Hearing Aids & Equipment";
            case _105: return "Drug and/or Alcohol Counselling";
            case _106: return "Drug/Alcohol Information/Referral";
            case _107: return "Needle & Syringe Exchange";
            case _108: return "Non-resid. Alcohol/Drug Treatment ";
            case _109: return "Pharmacotherapy";
            case _110: return "Quit Program";
            case _111: return "Residential Alcohol/Drug Treatment ";
            case _112: return "Adult/Community Education";
            case _113: return "Higher Education";
            case _114: return "Primary Education";
            case _115: return "Secondary Education";
            case _116: return "Training & Vocational Education";
            case _117: return "Emergency Medical";
            case _118: return "Employment Placement and/or Support";
            case _119: return "Vocational Rehabilitation";
            case _120: return "Work Safety/Accident Prevention";
            case _121: return "Financial Assistance";
            case _122: return "Financial Information/Advice";
            case _123: return "Material Aid";
            case _124: return "General Practice";
            case _125: return "Accommodation Placement/Support";
            case _126: return "Crisis/Emergency Accommodation";
            case _127: return "Homelessness Support";
            case _128: return "Housing Information/Referral";
            case _129: return "Public Rental Housing";
            case _130: return "Interpreting/Multilingual Service";
            case _131: return "Juvenile Justice";
            case _132: return "Legal Advocacy";
            case _133: return "Legal Information/Advice/Referral";
            case _134: return "Mental Health Advocacy";
            case _135: return "Mental Health Assess/Triage/Crisis Response";
            case _136: return "Mental Health Case Management";
            case _137: return "Mental Health Information/Referral";
            case _138: return "Mental Health Inpatient Services";
            case _139: return "Mental Health Non-residential Rehab";
            case _140: return "Mental Health Residential Rehab/CCU";
            case _141: return "Psychiatry (Requires Referral)";
            case _142: return "Psychology";
            case _143: return "Martial Arts";
            case _144: return "Personal Fitness Training";
            case _145: return "Physical Activity Group";
            case _146: return "Physical Activity Programs";
            case _147: return "Physical Fitness Testing";
            case _148: return "Pilates";
            case _149: return "Self-Defence";
            case _150: return "Sporting Club";
            case _151: return "Yoga";
            case _152: return "Food Safety";
            case _153: return "Health Regulatory /Inspection /Cert.";
            case _154: return "Work Health/Safety Inspection/Cert.";
            case _155: return "Carer Support";
            case _156: return "Respite Care";
            case _157: return "Anatomical Pathology ";
            case _158: return "Pathology - Clinical Chemistry";
            case _159: return "Pathology - General";
            case _160: return "Pathology - Genetics";
            case _161: return "Pathology - Haematology";
            case _162: return "Pathology - Immunology";
            case _163: return "Pathology - Microbiology";
            case _164: return "Anaesthesiology - Pain Medicine";
            case _165: return "Cardiology";
            case _166: return "Clinical Genetics";
            case _167: return "Clinical Pharmacology";
            case _168: return "Dermatology";
            case _169: return "Endocrinology";
            case _170: return "Gastroenterology & Hepatology";
            case _171: return "Geriatric Medicine";
            case _172: return "Immunology & Allergy";
            case _173: return "Infectious Diseases";
            case _174: return "Intensive Care Medicine";
            case _175: return "Medical Oncology";
            case _176: return "Nephrology";
            case _177: return "Neurology";
            case _178: return "Occupational Medicine";
            case _179: return "Palliative Medicine";
            case _180: return "Public Health Medicine";
            case _181: return "Rehabilitation Medicine";
            case _182: return "Rheumatology";
            case _183: return "Sleep Medicine";
            case _184: return "Thoracic Medicine";
            case _185: return "Gynaecological Oncology";
            case _186: return "Obstetrics & Gynaecology";
            case _187: return "Reproductive Endocrinology/Infertility";
            case _188: return "Urogynaecology";
            case _189: return "Neonatology & Perinatology";
            case _190: return "Paediatric Cardiology";
            case _191: return "Paediatric Clinical Genetics";
            case _192: return "Paediatric Clinical Pharmacology";
            case _193: return "Paediatric Endocrinology";
            case _194: return "Paed. Gastroenterology/Hepatology";
            case _195: return "Paediatric Haematology";
            case _196: return "Paediatric Immunology & Allergy";
            case _197: return "Paediatric Infectious Diseases";
            case _198: return "Paediatric Intensive Care Medicine";
            case _199: return "Paediatric Medical Oncology";
            case _200: return "Paediatric Medicine";
            case _201: return "Paediatric Nephrology";
            case _202: return "Paediatric Neurology";
            case _203: return "Paediatric Nuclear Medicine";
            case _204: return "Paediatric Rehabilitation Medicine";
            case _205: return "Paediatric Rheumatology";
            case _206: return "Paediatric Sleep Medicine";
            case _207: return "Paediatric Surgery";
            case _208: return "Paediatric Thoracic Medicine";
            case _209: return "Diag. Radiology /Xray /CT /Fluoroscopy";
            case _210: return "Diagnostic Ultrasound";
            case _211: return "Magnetic Resonance Imaging (MRI)";
            case _212: return "Nuclear Medicine";
            case _213: return "Obstetric/Gynaecological Ultrasound";
            case _214: return "Radiation Oncology";
            case _215: return "Cardiothoracic Surgery";
            case _216: return "Neurosurgery";
            case _217: return "Ophthalmology";
            case _218: return "Orthopaedic Surgery";
            case _219: return "Otolaryngology/Head & Neck Surgery";
            case _220: return "Plastic & Reconstructive Surgery";
            case _221: return "Surgery - General";
            case _222: return "Urology";
            case _223: return "Vascular Surgery";
            case _224: return "Support Groups";
            case _225: return "Air ambulance";
            case _226: return "Ambulance";
            case _227: return "Blood Transport";
            case _228: return "Community Bus";
            case _229: return "Flying Doctor Service";
            case _230: return "Patient Transport";
            case _231: return "A&E";
            case _232: return "A&EP";
            case _233: return "Abuse";
            case _234: return "ACAS";
            case _235: return "Access";
            case _236: return "Accident";
            case _237: return "Acute Inpatient Serv";
            case _238: return "Adult Day Programs";
            case _239: return "Adult Mental Health Services";
            case _240: return "Advice";
            case _241: return "Advocacy";
            case _242: return "Aged Persons Mental ";
            case _243: return "Aged Persons Mental ";
            case _244: return "Aged Persons Mental ";
            case _245: return "Aids";
            case _246: return "Al-Anon";
            case _247: return "Alcohol";
            case _248: return "Al-Teen";
            case _249: return "Antenatal";
            case _250: return "Anxiety";
            case _251: return "Arthritis";
            case _252: return "Assessment";
            case _253: return "Assistance";
            case _254: return "Asthma";
            case _255: return "ATSS";
            case _256: return "Attendant Care";
            case _257: return "Babies";
            case _258: return "Bathroom Modificatio";
            case _259: return "Behaviour";
            case _260: return "Behaviour Interventi";
            case _261: return "Bereavement";
            case _262: return "Bipolar";
            case _263: return "Birth";
            case _264: return "Birth Control";
            case _265: return "Birthing Options";
            case _266: return "BIST";
            case _267: return "Blood";
            case _268: return "Bone";
            case _269: return "Bowel";
            case _270: return "Brain";
            case _271: return "Breast Feeding";
            case _272: return "Breast Screen";
            case _273: return "Brokerage";
            case _274: return "Cancer";
            case _275: return "Cancer Support";
            case _276: return "Cardiovascular Disea";
            case _277: return "Care Packages";
            case _278: return "Carer";
            case _279: return "Case Management";
            case _280: return "Casualty";
            case _281: return "Centrelink";
            case _282: return "Chemists";
            case _283: return "Child And Adolescent";
            case _284: return "Child Care";
            case _285: return "Child Services";
            case _286: return "Children";
            case _287: return "Children's Services";
            case _288: return "Cholesterol";
            case _289: return "Clothing";
            case _290: return "Community Based Acco";
            case _291: return "Community Care Unit";
            case _292: return "Community Child And ";
            case _293: return "Community Health";
            case _294: return "Community Residentia";
            case _295: return "Community Transport";
            case _296: return "Companion Visiting";
            case _297: return "Companionship";
            case _298: return "Consumer Advice";
            case _299: return "Consumer Issues";
            case _300: return "Continuing Care Serv";
            case _301: return "Contraception Inform";
            case _302: return "Coordinating Bodies";
            case _303: return "Correctional Service";
            case _304: return "Council Environmenta";
            case _305: return "Counselling";
            case _306: return "Criminal";
            case _307: return "Crises";
            case _308: return "Crisis Assessment An";
            case _309: return "Crisis Assistance";
            case _310: return "Crisis Refuge";
            case _311: return "Day Program";
            case _312: return "Deaf";
            case _313: return "Dental Hygiene";
            case _314: return "Dentistry";
            case _315: return "Dentures";
            case _316: return "Depression";
            case _317: return "Detoxification";
            case _318: return "Diabetes";
            case _319: return "Diaphragm Fitting";
            case _320: return "Dieticians";
            case _321: return "Disabled Parking";
            case _322: return "District Nursing";
            case _323: return "Divorce";
            case _324: return "Doctors";
            case _325: return "Drink-Drive";
            case _326: return "Dual Diagnosis Servi";
            case _327: return "Early Choice";
            case _328: return "Eating Disorder";
            case _330: return "Emergency Relief";
            case _331: return "Employment And Train";
            case _332: return "Environment";
            case _333: return "Equipment";
            case _334: return "Exercise";
            case _335: return "Facility";
            case _336: return "Family Choice";
            case _337: return "Family Law";
            case _338: return "Family Options";
            case _339: return "Family Services";
            case _340: return "FFYA";
            case _341: return "Financial Aid";
            case _342: return "Fitness";
            case _343: return "Flexible Care Packag";
            case _344: return "Food";
            case _345: return "Food Vouchers";
            case _346: return "Forensic Mental Heal";
            case _347: return "Futures";
            case _348: return "Futures For Young Ad";
            case _349: return "General Practitioner";
            case _350: return "Grants";
            case _351: return "Grief";
            case _352: return "Grief Counselling";
            case _353: return "HACC";
            case _354: return "Heart Disease";
            case _355: return "Help";
            case _356: return "High Blood Pressure";
            case _357: return "Home Help";
            case _358: return "Home Nursing";
            case _359: return "Homefirst";
            case _360: return "Hospice Care";
            case _361: return "Hospital Services";
            case _362: return "Hospital To Home";
            case _364: return "Hostel";
            case _365: return "Hostel Accommodation";
            case _366: return "Household Items";
            case _367: return "Hypertension";
            case _368: return "Illness";
            case _369: return "Independent Living";
            case _370: return "Information";
            case _371: return "Injury";
            case _372: return "Intake";
            case _373: return "Intensive Mobile You";
            case _374: return "Intervention";
            case _375: return "Job Searching";
            case _376: return "Justice";
            case _377: return "Leisure";
            case _378: return "Loans";
            case _379: return "Low Income Earners";
            case _380: return "Lung";
            case _381: return "Making A Difference";
            case _382: return "Medical Services";
            case _383: return "Medical Specialists";
            case _384: return "Medication Administr";
            case _385: return "Menstrual Informatio";
            case _386: return "Methadone";
            case _387: return "Mobile Support And T";
            case _388: return "Motor Neurone";
            case _389: return "Multiple Sclerosis";
            case _390: return "Neighbourhood House";
            case _391: return "Nursing Home";
            case _392: return "Nursing Mothers";
            case _393: return "Obesity";
            case _394: return "Occupational Health ";
            case _395: return "Optometrist";
            case _396: return "Oral Hygiene";
            case _397: return "Outpatients";
            case _398: return "Outreach Service";
            case _399: return "PADP";
            case _400: return "Pain";
            case _401: return "Pap Smear";
            case _402: return "Parenting";
            case _403: return "Peak Organisations";
            case _404: return "Personal Care";
            case _405: return "Pharmacies";
            case _406: return "Phobias";
            case _407: return "Physical";
            case _408: return "Physical Activity";
            case _409: return "Postnatal";
            case _410: return "Pregnancy";
            case _411: return "Pregnancy Tests";
            case _412: return "Preschool";
            case _413: return "Prescriptions";
            case _414: return "Primary Mental Healt";
            case _415: return "Property Maintenance";
            case _416: return "Prostate";
            case _417: return "Psychiatric";
            case _418: return "Psychiatric Disabili";
            case _419: return "Psychiatric Disabili";
            case _420: return "Psychiatric Disabili";
            case _421: return "Psychiatric Disabili";
            case _422: return "Psychiatric Disabili";
            case _423: return "Psychiatric Support";
            case _424: return "Recreation";
            case _425: return "Referral";
            case _426: return "Refuge";
            case _427: return "Rent Assistance";
            case _428: return "Residential Faciliti";
            case _429: return "Residential Respite";
            case _430: return "Respiratory";
            case _431: return "Response";
            case _432: return "Rooming Houses";
            case _433: return "Safe Sex";
            case _434: return "Secure Extended Care";
            case _435: return "Self Help";
            case _436: return "Separation";
            case _437: return "Services";
            case _438: return "Sex Education";
            case _439: return "Sexual Abuse";
            case _440: return "Sexual Issues";
            case _441: return "Sexually Transmitted";
            case _442: return "SIDS";
            case _443: return "Social Support";
            case _444: return "Socialisation";
            case _445: return "Special Needs";
            case _446: return "Speech Therapist";
            case _447: return "Splinting";
            case _448: return "Sport";
            case _449: return "Statewide And Specia";
            case _450: return "STD";
            case _451: return "STI";
            case _452: return "Stillbirth";
            case _453: return "Stomal Care";
            case _454: return "Stroke";
            case _455: return "Substance Abuse";
            case _456: return "Support";
            case _457: return "Syringes";
            case _458: return "Teeth";
            case _459: return "Tenancy Advice";
            case _460: return "Terminal Illness";
            case _461: return "Therapy";
            case _462: return "Transcription";
            case _463: return "Translating Services";
            case _464: return "Translator";
            case _465: return "Transport";
            case _466: return "Vertebrae";
            case _467: return "Violence";
            case _468: return "Vocational Guidance";
            case _469: return "Weight";
            case _470: return "Welfare Assistance";
            case _471: return "Welfare Counselling";
            case _472: return "Wheelchairs";
            case _473: return "Wound Management";
            case _474: return "Young People At Risk";
            case _475: return "Further Desc. - Community Health Care";
            case _476: return "Library";
            case _477: return "Community Hours";
            case _478: return "Further Desc. - Specialist Medical";
            case _479: return "Hepatology";
            case _480: return "Gastroenterology ";
            case _481: return "Gynaecology";
            case _482: return "Obstetrics";
            case _483: return "Further Desc. - Specialist Surgical";
            case _484: return "Placement Protection";
            case _485: return "Family Violence";
            case _486: return "Integrated Family Services";
            case _488: return "Diabetes Educator";
            case _489: return "Kinship Care";
            case _490: return "General Mental Health Services";
            case _491: return "Exercise Physiology";
            case _492: return "Medical Research";
            case _493: return "Youth";
            case _494: return "Youth Services";
            case _495: return "Youth Health";
            case _496: return "Child and Family Ser";
            case _497: return "Home Visits";
            case _498: return "Mobile Services";
            case _500: return "Before and/or After ";
            case _501: return "Cancer Services";
            case _502: return "Integrated Cancer Se";
            case _503: return "Multidisciplinary Se";
            case _504: return "Multidisciplinary Ca";
            case _505: return "Meetings";
            case _506: return "Blood pressure monit";
            case _507: return "Dose administration ";
            case _508: return "Medical Equipment Hi";
            case _509: return "Parenting/Family Support/Education";
            case _510: return "Deputising Service";
            case _513: return "Cancer Support Groups";
            case _514: return "Community Cancer Services";
            case _530: return "Disability Care Transport";
            case _531: return "Aged Care Transport";
            case _532: return "Diabetes Education s";
            case _533: return "Cardiac Rehabilitati";
            case _534: return "Young Adult Diabetes";
            case _535: return "Pulmonary Rehabilita";
            case _536: return "Art therapy ";
            case _537: return "Medication Reviews";
            case _538: return "Telephone Counselling";
            case _539: return "Telephone Help Line";
            case _540: return "Online Service";
            case _541: return "Crisis - Mental Health";
            case _542: return "Youth Crisis";
            case _543: return "Sexual Assault";
            case _544: return "GPAH Other";
            case _545: return "Paediatric Dermatology";
            case _546: return "Veterans Services";
            case _547: return "Veterans";
            case _548: return "Food Relief/Food/Meals";
            case _550: return "Dementia Care";
            case _551: return "Alzheimer";
            case _552: return "Drug and/or Alcohol Support Groups";
            case _553: return "1-on-1 Support /Mentoring /Coaching";
            case _554: return "Chronic Disease Management";
            case _555: return "Liaison Services";
            case _556: return "Walk-in Centre /Non-Emergency";
            case _557: return "Inpatients";
            case _558: return "Spiritual Counselling";
            case _559: return "Women's Health";
            case _560: return "Men's Health";
            case _561: return "Health Education/Awareness Program";
            case _562: return "Test Message";
            case _563: return "Remedial Massage";
            case _564: return "Adolescent Mental Health Services";
            case _565: return "Youth Drop In/Assistance/Support";
            case _566: return "Aboriginal Health Worker";
            case _567: return "Women's Health Clinic";
            case _568: return "Men's Health Clinic";
            case _569: return "Migrant Health Clinic";
            case _570: return "Refugee Health Clinic";
            case _571: return "Aboriginal Health Clinic";
            case _572: return "Nurse Practitioner Lead Clinic/s";
            case _573: return "Nurse Lead Clinic/s";
            case _574: return "Culturally Tailored Support Groups";
            case _575: return "Culturally Tailored Health Promotion";
            case _576: return "Rehabilitation";
            case _577: return "Education Information/Referral";
            case _580: return "Social Work";
            case _581: return "Haematology";
            case _582: return "Maternity Shared Car";
            case _583: return "Rehabilitation Servi";
            case _584: return "Cranio-sacral Therapy";
            case _585: return "Prosthetics & Orthotics";
            case _589: return "Home Medicine Review";
            case _590: return "GPAH - Medical";
            case _591: return "Music Therapy";
            case _593: return "Falls Prevention";
            case _599: return "Accommodation/Tenancy";
            case _600: return "Assess-Skill, Ability, Needs";
            case _601: return "Assist Access/Maintain Employ";
            case _602: return "Assist Prod-Pers Care/Safety";
            case _603: return "Assist-Integrate School/Ed";
            case _604: return "Assist-Life Stage, Transition";
            case _605: return "Assist-Personal Activities";
            case _606: return "Assist-Travel/Transport";
            case _607: return "Assistive Equip-General Tasks";
            case _608: return "Assistive Equip-Recreation";
            case _609: return "Assistive Prod-Household Task";
            case _610: return "Behaviour Support";
            case _611: return "Comms & Info Equipment";
            case _612: return "Community Nursing Care";
            case _613: return "Daily Tasks/Shared Living";
            case _614: return "Development-Life Skills";
            case _615: return "Early Childhood Supports";
            case _616: return "Equipment Special Assess Setup";
            case _617: return "Hearing Equipment";
            case _618: return "Home Modification";
            case _619: return "Household Tasks";
            case _620: return "Interpret/Translate";
            case _621: return "Other Innovative Supports";
            case _622: return "Participate Community";
            case _623: return "Personal Mobility Equipment";
            case _624: return "Physical Wellbeing";
            case _625: return "Plan Management";
            case _626: return "Therapeutic Supports";
            case _627: return "Training-Travel Independence";
            case _628: return "Vehicle modifications";
            case _629: return "Vision Equipment";
            default: return "?";
          }
    }


}

