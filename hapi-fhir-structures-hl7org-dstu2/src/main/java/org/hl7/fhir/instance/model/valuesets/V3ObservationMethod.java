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


public enum V3ObservationMethod {

        /**
         * Provides codes for decision methods, initially for assessing the causality of events.
         */
        _DECISIONOBSERVATIONMETHOD, 
        /**
         * Reaching a decision through the application of an algorithm designed to weigh the different factors involved.
         */
        ALGM, 
        /**
         * Reaching a decision through the use of Bayesian statistical analysis.
         */
        BYCL, 
        /**
         * Reaching a decision by consideration of the totality of factors involved in order to reach a judgement.
         */
        GINT, 
        /**
         * A code that provides additional detail about the means or technique used to ascertain the genetic analysis. Example, PCR, Micro Array
         */
        _GENETICOBSERVATIONMETHOD, 
        /**
         * Description: Polymerase Chain Reaction
         */
        PCR, 
        /**
         * Provides additional detail about the aggregation methods used to compute the aggregated values for an observation. This is an abstract code.
         */
        _OBSERVATIONMETHODAGGREGATE, 
        /**
         * Average of non-null values in the referenced set of values
         */
        AVERAGE, 
        /**
         * Count of non-null values in the referenced set of values
         */
        COUNT, 
        /**
         * Largest of all non-null values in the referenced set of values.
         */
        MAX, 
        /**
         * The median of all non-null values in the referenced set of values.
         */
        MEDIAN, 
        /**
         * Smallest of all non-null values in the referenced set of values.
         */
        MIN, 
        /**
         * The most common value of all non-null values in the referenced set of values.
         */
        MODE, 
        /**
         * Standard Deviation of the values in the referenced set of values, computed over the population.
         */
        STDEV_P, 
        /**
         * Standard Deviation of the values in the referenced set of values, computed over a sample of the population.
         */
        STDEV_S, 
        /**
         * Sum of non-null values in the referenced set of values
         */
        SUM, 
        /**
         * Variance of the values in the referenced set of values, computed over the population.
         */
        VARIANCE_P, 
        /**
         * Variance of the values in the referenced set of values, computed over a sample of the population.
         */
        VARIANCE_S, 
        /**
         * VerificationMethod
         */
        _VERIFICATIONMETHOD, 
        /**
         * Verification by means of document.

                        
                           Example: Fax, letter, attachment to e-mail.
         */
        VDOC, 
        /**
         * verification by means of  a response to an electronic query

                        
                           Example: query message to a Covered Party registry application or Coverage Administrator.
         */
        VREG, 
        /**
         * Verification by means of electronic token.

                        
                           Example: smartcard, magnetic swipe card, RFID device.
         */
        VTOKEN, 
        /**
         * Verification by means of voice.

                        
                           Example: By speaking with or calling the Coverage Administrator or Covered Party
         */
        VVOICE, 
        /**
         * Complement fixation
         */
        _0001, 
        /**
         * Computed axial tomography
         */
        _0002, 
        /**
         * Susceptibility, High Level Aminoglycoside Resistance agar test
         */
        _0003, 
        /**
         * Visual, Macroscopic observation
         */
        _0004, 
        /**
         * Computed, Magnetic resonance
         */
        _0005, 
        /**
         * Computed, Morphometry
         */
        _0006, 
        /**
         * Computed, Positron emission tomography
         */
        _0007, 
        /**
         * SAMHSA drug assay confirmation
         */
        _0008, 
        /**
         * SAMHSA drug assay screening
         */
        _0009, 
        /**
         * Serum Neutralization
         */
        _0010, 
        /**
         * Titration
         */
        _0011, 
        /**
         * Ultrasound
         */
        _0012, 
        /**
         * X-ray crystallography
         */
        _0013, 
        /**
         * Agglutination
         */
        _0014, 
        /**
         * Agglutination, Buffered acidified plate
         */
        _0015, 
        /**
         * Agglutination, Card
         */
        _0016, 
        /**
         * Agglutination, Hemagglutination
         */
        _0017, 
        /**
         * Agglutination, Hemagglutination inhibition
         */
        _0018, 
        /**
         * Agglutination, Latex
         */
        _0019, 
        /**
         * Agglutination, Plate
         */
        _0020, 
        /**
         * Agglutination, Rapid Plate
         */
        _0021, 
        /**
         * Agglutination, RBC
         */
        _0022, 
        /**
         * Agglutination, Rivanol
         */
        _0023, 
        /**
         * Agglutination, Tube
         */
        _0024, 
        /**
         * Bioassay
         */
        _0025, 
        /**
         * Bioassay, Animal Inoculation
         */
        _0026, 
        /**
         * Bioassay, Cytotoxicity
         */
        _0027, 
        /**
         * Bioassay, Embryo Infective Dose 50
         */
        _0028, 
        /**
         * Bioassay, Embryo Lethal Dose 50
         */
        _0029, 
        /**
         * Bioassay, Mouse intercerebral inoculation
         */
        _0030, 
        /**
         * Bioassay, qualitative
         */
        _0031, 
        /**
         * Bioassay, quantitative
         */
        _0032, 
        /**
         * Chemical
         */
        _0033, 
        /**
         * Chemical, Differential light absorption
         */
        _0034, 
        /**
         * Chemical, Dipstick
         */
        _0035, 
        /**
         * Chemical, Dipstick colorimetric laboratory test
         */
        _0036, 
        /**
         * Chemical, Test strip
         */
        _0037, 
        /**
         * Chromatography
         */
        _0038, 
        /**
         * Chromatography, Affinity
         */
        _0039, 
        /**
         * Chromatography, Gas liquid
         */
        _0040, 
        /**
         * Chromatography, High performance liquid
         */
        _0041, 
        /**
         * Chromatography, Liquid
         */
        _0042, 
        /**
         * Chromatography, Protein A affinity
         */
        _0043, 
        /**
         * Coagulation
         */
        _0044, 
        /**
         * Coagulation, Tilt tube
         */
        _0045, 
        /**
         * Coagulation, Tilt tube reptilase induced
         */
        _0046, 
        /**
         * Count, Automated
         */
        _0047, 
        /**
         * Count, Manual
         */
        _0048, 
        /**
         * Count, Platelet, Rees-Ecker
         */
        _0049, 
        /**
         * Culture, Aerobic
         */
        _0050, 
        /**
         * Culture, Anaerobic
         */
        _0051, 
        /**
         * Culture, Chicken Embryo
         */
        _0052, 
        /**
         * Culture, Delayed secondary enrichment
         */
        _0053, 
        /**
         * Culture, Microaerophilic
         */
        _0054, 
        /**
         * Culture, Quantitative microbial, cup
         */
        _0055, 
        /**
         * Culture, Quantitative microbial, droplet
         */
        _0056, 
        /**
         * Culture, Quantitative microbial, filter paper
         */
        _0057, 
        /**
         * Culture, Quantitative microbial, pad
         */
        _0058, 
        /**
         * Culture, Quantitative microbial, pour plate
         */
        _0059, 
        /**
         * Culture, Quantitative microbial, surface streak
         */
        _0060, 
        /**
         * Culture, Somatic Cell
         */
        _0061, 
        /**
         * Diffusion, Agar
         */
        _0062, 
        /**
         * Diffusion, Agar Gel Immunodiffusion
         */
        _0063, 
        /**
         * Electrophoresis
         */
        _0064, 
        /**
         * Electrophoresis, Agaorse gel
         */
        _0065, 
        /**
         * Electrophoresis, citrate agar
         */
        _0066, 
        /**
         * Electrophoresis, Immuno
         */
        _0067, 
        /**
         * Electrophoresis, Polyacrylamide gel
         */
        _0068, 
        /**
         * Electrophoresis, Starch gel
         */
        _0069, 
        /**
         * ELISA
         */
        _0070, 
        /**
         * ELISA, antigen capture
         */
        _0071, 
        /**
         * ELISA, avidin biotin peroxidase complex
         */
        _0072, 
        /**
         * ELISA, Kinetic
         */
        _0073, 
        /**
         * ELISA, peroxidase-antiperoxidase
         */
        _0074, 
        /**
         * Identification, API 20 Strep
         */
        _0075, 
        /**
         * Identification, API 20A
         */
        _0076, 
        /**
         * Identification, API 20C AUX
         */
        _0077, 
        /**
         * Identification, API 20E
         */
        _0078, 
        /**
         * Identification, API 20NE
         */
        _0079, 
        /**
         * Identification, API 50 CH
         */
        _0080, 
        /**
         * Identification, API An-IDENT
         */
        _0081, 
        /**
         * Identification, API Coryne
         */
        _0082, 
        /**
         * Identification, API Rapid 20E
         */
        _0083, 
        /**
         * Identification, API Staph
         */
        _0084, 
        /**
         * Identification, API ZYM
         */
        _0085, 
        /**
         * Identification, Bacterial
         */
        _0086, 
        /**
         * Identification, mini VIDAS
         */
        _0087, 
        /**
         * Identification, Phage susceptibility typing
         */
        _0088, 
        /**
         * Identification, Quad-FERM+
         */
        _0089, 
        /**
         * Identification, RAPIDEC Staph
         */
        _0090, 
        /**
         * Identification, Staphaurex
         */
        _0091, 
        /**
         * Identification, VIDAS
         */
        _0092, 
        /**
         * Identification, Vitek
         */
        _0093, 
        /**
         * Identification, VITEK 2
         */
        _0094, 
        /**
         * Immune stain
         */
        _0095, 
        /**
         * Immune stain, Immunofluorescent antibody, direct
         */
        _0096, 
        /**
         * Immune stain, Immunofluorescent antibody, indirect
         */
        _0097, 
        /**
         * Immune stain, Immunoperoxidase, Avidin-Biotin Complex
         */
        _0098, 
        /**
         * Immune stain, Immunoperoxidase, Peroxidase anti-peroxidase complex
         */
        _0099, 
        /**
         * Immune stain, Immunoperoxidase, Protein A-peroxidase complex
         */
        _0100, 
        /**
         * Immunoassay
         */
        _0101, 
        /**
         * Immunoassay, qualitative, multiple step
         */
        _0102, 
        /**
         * Immunoassay, qualitative, single step
         */
        _0103, 
        /**
         * Immunoassay, Radioimmunoassay
         */
        _0104, 
        /**
         * Immunoassay, semi-quantitative, multiple step
         */
        _0105, 
        /**
         * Immunoassay, semi-quantitative, single step
         */
        _0106, 
        /**
         * Microscopy
         */
        _0107, 
        /**
         * Microscopy, Darkfield
         */
        _0108, 
        /**
         * Microscopy, Electron
         */
        _0109, 
        /**
         * Microscopy, Electron microscopy tomography
         */
        _0110, 
        /**
         * Microscopy, Electron, negative stain
         */
        _0111, 
        /**
         * Microscopy, Electron, thick section transmission
         */
        _0112, 
        /**
         * Microscopy, Electron, thin section transmission
         */
        _0113, 
        /**
         * Microscopy, Light
         */
        _0114, 
        /**
         * Microscopy, Polarized light
         */
        _0115, 
        /**
         * Microscopy, Scanning electron
         */
        _0116, 
        /**
         * Microscopy, Transmission electron
         */
        _0117, 
        /**
         * Microscopy, Transparent tape direct examination
         */
        _0118, 
        /**
         * Molecular, 3 Self-Sustaining Sequence Replication
         */
        _0119, 
        /**
         * Molecular, Branched Chain DNA
         */
        _0120, 
        /**
         * Molecular, Hybridization Protection Assay
         */
        _0121, 
        /**
         * Molecular, Immune blot
         */
        _0122, 
        /**
         * Molecular, In-situ hybridization
         */
        _0123, 
        /**
         * Molecular, Ligase Chain Reaction
         */
        _0124, 
        /**
         * Molecular, Ligation Activated Transcription
         */
        _0125, 
        /**
         * Molecular, Nucleic Acid Probe
         */
        _0126, 
        /**
         * Molecular, Nucleic acid probe with amplification

                        

                        Rationale: Duplicate of code 0126. Use code 0126 instead.
         */
        _0128, 
        /**
         * Molecular, Nucleic acid probe with target amplification
         */
        _0129, 
        /**
         * Molecular, Nucleic acid reverse transcription
         */
        _0130, 
        /**
         * Molecular, Nucleic Acid Sequence Based Analysis
         */
        _0131, 
        /**
         * Molecular, Polymerase chain reaction
         */
        _0132, 
        /**
         * Molecular, Q-Beta Replicase or probe amplification category method
         */
        _0133, 
        /**
         * Molecular, Restriction Fragment Length Polymorphism
         */
        _0134, 
        /**
         * Molecular, Southern Blot
         */
        _0135, 
        /**
         * Molecular, Strand Displacement Amplification
         */
        _0136, 
        /**
         * Molecular, Transcription Mediated Amplification
         */
        _0137, 
        /**
         * Molecular, Western Blot
         */
        _0138, 
        /**
         * Precipitation, Flocculation
         */
        _0139, 
        /**
         * Precipitation, Immune precipitation
         */
        _0140, 
        /**
         * Precipitation, Milk ring test
         */
        _0141, 
        /**
         * Precipitation, Precipitin
         */
        _0142, 
        /**
         * Stain, Acid fast
         */
        _0143, 
        /**
         * Stain, Acid fast, fluorochrome
         */
        _0144, 
        /**
         * Stain, Acid fast, Kinyoun's cold carbolfuchsin
         */
        _0145, 
        /**
         * Stain, Acid fast, Ziehl-Neelsen
         */
        _0146, 
        /**
         * Stain, Acid phosphatase
         */
        _0147, 
        /**
         * Stain, Acridine orange
         */
        _0148, 
        /**
         * Stain, Active brilliant orange KH
         */
        _0149, 
        /**
         * Stain, Alazarin red S
         */
        _0150, 
        /**
         * Stain, Alcian blue
         */
        _0151, 
        /**
         * Stain, Alcian blue with Periodic acid Schiff
         */
        _0152, 
        /**
         * Stain, Argentaffin
         */
        _0153, 
        /**
         * Stain, Argentaffin silver
         */
        _0154, 
        /**
         * Stain, Azure-eosin
         */
        _0155, 
        /**
         * Stain, Basic Fuschin
         */
        _0156, 
        /**
         * Stain, Bennhold
         */
        _0157, 
        /**
         * Stain, Bennhold's Congo red
         */
        _0158, 
        /**
         * Stain, Bielschowsky
         */
        _0159, 
        /**
         * Stain, Bielschowsky's silver
         */
        _0160, 
        /**
         * Stain, Bleach
         */
        _0161, 
        /**
         * Stain, Bodian
         */
        _0162, 
        /**
         * Stain, Brown-Brenn
         */
        _0163, 
        /**
         * Stain, Butyrate-esterase
         */
        _0164, 
        /**
         * Stain, Calcofluor white fluorescent
         */
        _0165, 
        /**
         * Stain, Carbol-fuchsin
         */
        _0166, 
        /**
         * Stain, Carmine
         */
        _0167, 
        /**
         * Stain, Churukian-Schenk
         */
        _0168, 
        /**
         * Stain, Congo red
         */
        _0169, 
        /**
         * Stain, Cresyl echt violet
         */
        _0170, 
        /**
         * Stain, Crystal violet
         */
        _0171, 
        /**
         * Stain, De Galantha
         */
        _0172, 
        /**
         * Stain, Dieterle silver impregnation
         */
        _0173, 
        /**
         * Stain, Fite-Farco
         */
        _0174, 
        /**
         * Stain, Fontana-Masson silver
         */
        _0175, 
        /**
         * Stain, Fouchet
         */
        _0176, 
        /**
         * Stain, Gomori
         */
        _0177, 
        /**
         * Stain, Gomori methenamine silver
         */
        _0178, 
        /**
         * Stain, Gomori-Wheatly trichrome
         */
        _0179, 
        /**
         * Stain, Gridley
         */
        _0180, 
        /**
         * Stain, Grimelius silver
         */
        _0181, 
        /**
         * Stain, Grocott
         */
        _0182, 
        /**
         * Stain, Grocott methenamine silver
         */
        _0183, 
        /**
         * Stain, Hale's colloidal ferric oxide
         */
        _0184, 
        /**
         * Stain, Hale's colloidal iron
         */
        _0185, 
        /**
         * Stain, Hansel
         */
        _0186, 
        /**
         * Stain, Harris regressive hematoxylin and eosin
         */
        _0187, 
        /**
         * Stain, Hematoxylin and eosin
         */
        _0188, 
        /**
         * Stain, Highman
         */
        _0189, 
        /**
         * Stain, Holzer
         */
        _0190, 
        /**
         * Stain, Iron hematoxylin
         */
        _0191, 
        /**
         * Stain, Jones
         */
        _0192, 
        /**
         * Stain, Jones methenamine silver
         */
        _0193, 
        /**
         * Stain, Kossa
         */
        _0194, 
        /**
         * Stain, Lawson-Van Gieson
         */
        _0195, 
        /**
         * Stain, Loeffler methylene blue
         */
        _0196, 
        /**
         * Stain, Luxol fast blue with cresyl violet
         */
        _0197, 
        /**
         * Stain, Luxol fast blue with Periodic acid-Schiff
         */
        _0198, 
        /**
         * Stain, MacNeal's tetrachrome blood
         */
        _0199, 
        /**
         * Stain, Mallory-Heidenhain
         */
        _0200, 
        /**
         * Stain, Masson trichrome
         */
        _0201, 
        /**
         * Stain, Mayer mucicarmine
         */
        _0202, 
        /**
         * Stain, Mayers progressive hematoxylin and eosin
         */
        _0203, 
        /**
         * Stain, May-Grunwald Giemsa
         */
        _0204, 
        /**
         * Stain, Methyl green
         */
        _0205, 
        /**
         * Stain, Methyl green pyronin
         */
        _0206, 
        /**
         * Stain, Modified Gomori-Wheatly trichrome
         */
        _0207, 
        /**
         * Stain, Modified Masson trichrome
         */
        _0208, 
        /**
         * Stain, Modified trichrome
         */
        _0209, 
        /**
         * Stain, Movat pentachrome
         */
        _0210, 
        /**
         * Stain, Mucicarmine
         */
        _0211, 
        /**
         * Stain, Neutral red
         */
        _0212, 
        /**
         * Stain, Night blue
         */
        _0213, 
        /**
         * Stain, Non-specific esterase
         */
        _0214, 
        /**
         * Stain, Oil red-O
         */
        _0215, 
        /**
         * Stain, Orcein
         */
        _0216, 
        /**
         * Stain, Perls'
         */
        _0217, 
        /**
         * Stain, Phosphotungstic acid-hematoxylin
         */
        _0218, 
        /**
         * Stain, Potassium ferrocyanide
         */
        _0219, 
        /**
         * Stain, Prussian blue
         */
        _0220, 
        /**
         * Stain, Putchler modified Bennhold
         */
        _0221, 
        /**
         * Stain, Quinacrine fluorescent
         */
        _0222, 
        /**
         * Stain, Reticulin
         */
        _0223, 
        /**
         * Stain, Rhodamine
         */
        _0224, 
        /**
         * Stain, Safranin
         */
        _0225, 
        /**
         * Stain, Schmorl
         */
        _0226, 
        /**
         * Stain, Seiver-Munger
         */
        _0227, 
        /**
         * Stain, Silver
         */
        _0228, 
        /**
         * Stain, Specific esterase
         */
        _0229, 
        /**
         * Stain, Steiner silver
         */
        _0230, 
        /**
         * Stain, Sudan III
         */
        _0231, 
        /**
         * Stain, Sudan IVI
         */
        _0232, 
        /**
         * Stain, Sulfated alcian blue
         */
        _0233, 
        /**
         * Stain, Supravital
         */
        _0234, 
        /**
         * Stain, Thioflavine-S
         */
        _0235, 
        /**
         * Stain, Three micron Giemsa
         */
        _0236, 
        /**
         * Stain, Vassar-Culling
         */
        _0237, 
        /**
         * Stain, Vital
         */
        _0238, 
        /**
         * Stain, von Kossa
         */
        _0239, 
        /**
         * Susceptibility, Minimum bactericidal concentration, macrodilution
         */
        _0243, 
        /**
         * Susceptibility, Minimum bactericidal concentration, microdilution
         */
        _0244, 
        /**
         * Turbidometric
         */
        _0247, 
        /**
         * Turbidometric, Refractometric
         */
        _0248, 
        /**
         * Chromatography, Thin Layer
         */
        _0249, 
        /**
         * Immunoassay, enzyme-multiplied technique (EMIT)
         */
        _0250, 
        /**
         * Flow Cytometry
         */
        _0251, 
        /**
         * Radial Immunodiffusion
         */
        _0252, 
        /**
         * Immunoassay, Fluorescence Polarization
         */
        _0253, 
        /**
         * Electrophoresis, Immunofixation
         */
        _0254, 
        /**
         * Dialysis, Direct Equilibrium
         */
        _0255, 
        /**
         * Acid Elution, Kleihauer-Betke Method
         */
        _0256, 
        /**
         * Immunofluorescence, Anti-Complement
         */
        _0257, 
        /**
         * Gas Chromatography/Mass Spectroscopy
         */
        _0258, 
        /**
         * Light Scatter, Nephelometry
         */
        _0259, 
        /**
         * Immunoassay, IgE Antibody Test
         */
        _0260, 
        /**
         * Lymphocyte Microcytotoxicity Assay
         */
        _0261, 
        /**
         * Spectrophotometry
         */
        _0262, 
        /**
         * Spectrophotometry, Atomic Absorption
         */
        _0263, 
        /**
         * Electrochemical, Ion Selective Electrode
         */
        _0264, 
        /**
         * Chromatography, Gas
         */
        _0265, 
        /**
         * Isoelectric Focusing
         */
        _0266, 
        /**
         * Immunoassay, Chemiluminescent
         */
        _0267, 
        /**
         * Immunoassay, Microparticle Enzyme
         */
        _0268, 
        /**
         * Inductively-Coupled Plasma/Mass Spectrometry
         */
        _0269, 
        /**
         * Immunoassay, Immunoradiometric Assay
         */
        _0270, 
        /**
         * Coagulation, Photo Optical Clot Detection
         */
        _0271, 
        /**
         * Test methods designed to determine a microorganismaTMs susceptibility to being killed by an antibiotic.
         */
        _0280, 
        /**
         * Susceptibility, Antibiotic sensitivity, disk
         */
        _0240, 
        /**
         * Susceptibility, BACTEC susceptibility test
         */
        _0241, 
        /**
         * Susceptibility, Disk dilution
         */
        _0242, 
        /**
         * Testing to measure the minimum concentration of the antibacterial agent in a given culture medium below which bacterial growth is not inhibited.
         */
        _0272, 
        /**
         * Susceptibility, Minimum Inhibitory concentration, macrodilution
         */
        _0245, 
        /**
         * Susceptibility, Minimum Inhibitory concentration, microdilution
         */
        _0246, 
        /**
         * Viral Genotype Susceptibility
         */
        _0273, 
        /**
         * Viral Phenotype Susceptibility
         */
        _0274, 
        /**
         * Gradient Strip
         */
        _0275, 
        /**
         * Minimum Lethal Concentration (MLC)
         */
        _0275A, 
        /**
         * Testing to measure the minimum concentration of the antibacterial agent in a given culture medium below which bacterial growth is not inhibited.
         */
        _0276, 
        /**
         * Serum bactericidal titer
         */
        _0277, 
        /**
         * Agar screen
         */
        _0278, 
        /**
         * Disk induction
         */
        _0279, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3ObservationMethod fromCode(String codeString) throws Exception {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_DecisionObservationMethod".equals(codeString))
          return _DECISIONOBSERVATIONMETHOD;
        if ("ALGM".equals(codeString))
          return ALGM;
        if ("BYCL".equals(codeString))
          return BYCL;
        if ("GINT".equals(codeString))
          return GINT;
        if ("_GeneticObservationMethod".equals(codeString))
          return _GENETICOBSERVATIONMETHOD;
        if ("PCR".equals(codeString))
          return PCR;
        if ("_ObservationMethodAggregate".equals(codeString))
          return _OBSERVATIONMETHODAGGREGATE;
        if ("AVERAGE".equals(codeString))
          return AVERAGE;
        if ("COUNT".equals(codeString))
          return COUNT;
        if ("MAX".equals(codeString))
          return MAX;
        if ("MEDIAN".equals(codeString))
          return MEDIAN;
        if ("MIN".equals(codeString))
          return MIN;
        if ("MODE".equals(codeString))
          return MODE;
        if ("STDEV.P".equals(codeString))
          return STDEV_P;
        if ("STDEV.S".equals(codeString))
          return STDEV_S;
        if ("SUM".equals(codeString))
          return SUM;
        if ("VARIANCE.P".equals(codeString))
          return VARIANCE_P;
        if ("VARIANCE.S".equals(codeString))
          return VARIANCE_S;
        if ("_VerificationMethod".equals(codeString))
          return _VERIFICATIONMETHOD;
        if ("VDOC".equals(codeString))
          return VDOC;
        if ("VREG".equals(codeString))
          return VREG;
        if ("VTOKEN".equals(codeString))
          return VTOKEN;
        if ("VVOICE".equals(codeString))
          return VVOICE;
        if ("0001".equals(codeString))
          return _0001;
        if ("0002".equals(codeString))
          return _0002;
        if ("0003".equals(codeString))
          return _0003;
        if ("0004".equals(codeString))
          return _0004;
        if ("0005".equals(codeString))
          return _0005;
        if ("0006".equals(codeString))
          return _0006;
        if ("0007".equals(codeString))
          return _0007;
        if ("0008".equals(codeString))
          return _0008;
        if ("0009".equals(codeString))
          return _0009;
        if ("0010".equals(codeString))
          return _0010;
        if ("0011".equals(codeString))
          return _0011;
        if ("0012".equals(codeString))
          return _0012;
        if ("0013".equals(codeString))
          return _0013;
        if ("0014".equals(codeString))
          return _0014;
        if ("0015".equals(codeString))
          return _0015;
        if ("0016".equals(codeString))
          return _0016;
        if ("0017".equals(codeString))
          return _0017;
        if ("0018".equals(codeString))
          return _0018;
        if ("0019".equals(codeString))
          return _0019;
        if ("0020".equals(codeString))
          return _0020;
        if ("0021".equals(codeString))
          return _0021;
        if ("0022".equals(codeString))
          return _0022;
        if ("0023".equals(codeString))
          return _0023;
        if ("0024".equals(codeString))
          return _0024;
        if ("0025".equals(codeString))
          return _0025;
        if ("0026".equals(codeString))
          return _0026;
        if ("0027".equals(codeString))
          return _0027;
        if ("0028".equals(codeString))
          return _0028;
        if ("0029".equals(codeString))
          return _0029;
        if ("0030".equals(codeString))
          return _0030;
        if ("0031".equals(codeString))
          return _0031;
        if ("0032".equals(codeString))
          return _0032;
        if ("0033".equals(codeString))
          return _0033;
        if ("0034".equals(codeString))
          return _0034;
        if ("0035".equals(codeString))
          return _0035;
        if ("0036".equals(codeString))
          return _0036;
        if ("0037".equals(codeString))
          return _0037;
        if ("0038".equals(codeString))
          return _0038;
        if ("0039".equals(codeString))
          return _0039;
        if ("0040".equals(codeString))
          return _0040;
        if ("0041".equals(codeString))
          return _0041;
        if ("0042".equals(codeString))
          return _0042;
        if ("0043".equals(codeString))
          return _0043;
        if ("0044".equals(codeString))
          return _0044;
        if ("0045".equals(codeString))
          return _0045;
        if ("0046".equals(codeString))
          return _0046;
        if ("0047".equals(codeString))
          return _0047;
        if ("0048".equals(codeString))
          return _0048;
        if ("0049".equals(codeString))
          return _0049;
        if ("0050".equals(codeString))
          return _0050;
        if ("0051".equals(codeString))
          return _0051;
        if ("0052".equals(codeString))
          return _0052;
        if ("0053".equals(codeString))
          return _0053;
        if ("0054".equals(codeString))
          return _0054;
        if ("0055".equals(codeString))
          return _0055;
        if ("0056".equals(codeString))
          return _0056;
        if ("0057".equals(codeString))
          return _0057;
        if ("0058".equals(codeString))
          return _0058;
        if ("0059".equals(codeString))
          return _0059;
        if ("0060".equals(codeString))
          return _0060;
        if ("0061".equals(codeString))
          return _0061;
        if ("0062".equals(codeString))
          return _0062;
        if ("0063".equals(codeString))
          return _0063;
        if ("0064".equals(codeString))
          return _0064;
        if ("0065".equals(codeString))
          return _0065;
        if ("0066".equals(codeString))
          return _0066;
        if ("0067".equals(codeString))
          return _0067;
        if ("0068".equals(codeString))
          return _0068;
        if ("0069".equals(codeString))
          return _0069;
        if ("0070".equals(codeString))
          return _0070;
        if ("0071".equals(codeString))
          return _0071;
        if ("0072".equals(codeString))
          return _0072;
        if ("0073".equals(codeString))
          return _0073;
        if ("0074".equals(codeString))
          return _0074;
        if ("0075".equals(codeString))
          return _0075;
        if ("0076".equals(codeString))
          return _0076;
        if ("0077".equals(codeString))
          return _0077;
        if ("0078".equals(codeString))
          return _0078;
        if ("0079".equals(codeString))
          return _0079;
        if ("0080".equals(codeString))
          return _0080;
        if ("0081".equals(codeString))
          return _0081;
        if ("0082".equals(codeString))
          return _0082;
        if ("0083".equals(codeString))
          return _0083;
        if ("0084".equals(codeString))
          return _0084;
        if ("0085".equals(codeString))
          return _0085;
        if ("0086".equals(codeString))
          return _0086;
        if ("0087".equals(codeString))
          return _0087;
        if ("0088".equals(codeString))
          return _0088;
        if ("0089".equals(codeString))
          return _0089;
        if ("0090".equals(codeString))
          return _0090;
        if ("0091".equals(codeString))
          return _0091;
        if ("0092".equals(codeString))
          return _0092;
        if ("0093".equals(codeString))
          return _0093;
        if ("0094".equals(codeString))
          return _0094;
        if ("0095".equals(codeString))
          return _0095;
        if ("0096".equals(codeString))
          return _0096;
        if ("0097".equals(codeString))
          return _0097;
        if ("0098".equals(codeString))
          return _0098;
        if ("0099".equals(codeString))
          return _0099;
        if ("0100".equals(codeString))
          return _0100;
        if ("0101".equals(codeString))
          return _0101;
        if ("0102".equals(codeString))
          return _0102;
        if ("0103".equals(codeString))
          return _0103;
        if ("0104".equals(codeString))
          return _0104;
        if ("0105".equals(codeString))
          return _0105;
        if ("0106".equals(codeString))
          return _0106;
        if ("0107".equals(codeString))
          return _0107;
        if ("0108".equals(codeString))
          return _0108;
        if ("0109".equals(codeString))
          return _0109;
        if ("0110".equals(codeString))
          return _0110;
        if ("0111".equals(codeString))
          return _0111;
        if ("0112".equals(codeString))
          return _0112;
        if ("0113".equals(codeString))
          return _0113;
        if ("0114".equals(codeString))
          return _0114;
        if ("0115".equals(codeString))
          return _0115;
        if ("0116".equals(codeString))
          return _0116;
        if ("0117".equals(codeString))
          return _0117;
        if ("0118".equals(codeString))
          return _0118;
        if ("0119".equals(codeString))
          return _0119;
        if ("0120".equals(codeString))
          return _0120;
        if ("0121".equals(codeString))
          return _0121;
        if ("0122".equals(codeString))
          return _0122;
        if ("0123".equals(codeString))
          return _0123;
        if ("0124".equals(codeString))
          return _0124;
        if ("0125".equals(codeString))
          return _0125;
        if ("0126".equals(codeString))
          return _0126;
        if ("0128".equals(codeString))
          return _0128;
        if ("0129".equals(codeString))
          return _0129;
        if ("0130".equals(codeString))
          return _0130;
        if ("0131".equals(codeString))
          return _0131;
        if ("0132".equals(codeString))
          return _0132;
        if ("0133".equals(codeString))
          return _0133;
        if ("0134".equals(codeString))
          return _0134;
        if ("0135".equals(codeString))
          return _0135;
        if ("0136".equals(codeString))
          return _0136;
        if ("0137".equals(codeString))
          return _0137;
        if ("0138".equals(codeString))
          return _0138;
        if ("0139".equals(codeString))
          return _0139;
        if ("0140".equals(codeString))
          return _0140;
        if ("0141".equals(codeString))
          return _0141;
        if ("0142".equals(codeString))
          return _0142;
        if ("0143".equals(codeString))
          return _0143;
        if ("0144".equals(codeString))
          return _0144;
        if ("0145".equals(codeString))
          return _0145;
        if ("0146".equals(codeString))
          return _0146;
        if ("0147".equals(codeString))
          return _0147;
        if ("0148".equals(codeString))
          return _0148;
        if ("0149".equals(codeString))
          return _0149;
        if ("0150".equals(codeString))
          return _0150;
        if ("0151".equals(codeString))
          return _0151;
        if ("0152".equals(codeString))
          return _0152;
        if ("0153".equals(codeString))
          return _0153;
        if ("0154".equals(codeString))
          return _0154;
        if ("0155".equals(codeString))
          return _0155;
        if ("0156".equals(codeString))
          return _0156;
        if ("0157".equals(codeString))
          return _0157;
        if ("0158".equals(codeString))
          return _0158;
        if ("0159".equals(codeString))
          return _0159;
        if ("0160".equals(codeString))
          return _0160;
        if ("0161".equals(codeString))
          return _0161;
        if ("0162".equals(codeString))
          return _0162;
        if ("0163".equals(codeString))
          return _0163;
        if ("0164".equals(codeString))
          return _0164;
        if ("0165".equals(codeString))
          return _0165;
        if ("0166".equals(codeString))
          return _0166;
        if ("0167".equals(codeString))
          return _0167;
        if ("0168".equals(codeString))
          return _0168;
        if ("0169".equals(codeString))
          return _0169;
        if ("0170".equals(codeString))
          return _0170;
        if ("0171".equals(codeString))
          return _0171;
        if ("0172".equals(codeString))
          return _0172;
        if ("0173".equals(codeString))
          return _0173;
        if ("0174".equals(codeString))
          return _0174;
        if ("0175".equals(codeString))
          return _0175;
        if ("0176".equals(codeString))
          return _0176;
        if ("0177".equals(codeString))
          return _0177;
        if ("0178".equals(codeString))
          return _0178;
        if ("0179".equals(codeString))
          return _0179;
        if ("0180".equals(codeString))
          return _0180;
        if ("0181".equals(codeString))
          return _0181;
        if ("0182".equals(codeString))
          return _0182;
        if ("0183".equals(codeString))
          return _0183;
        if ("0184".equals(codeString))
          return _0184;
        if ("0185".equals(codeString))
          return _0185;
        if ("0186".equals(codeString))
          return _0186;
        if ("0187".equals(codeString))
          return _0187;
        if ("0188".equals(codeString))
          return _0188;
        if ("0189".equals(codeString))
          return _0189;
        if ("0190".equals(codeString))
          return _0190;
        if ("0191".equals(codeString))
          return _0191;
        if ("0192".equals(codeString))
          return _0192;
        if ("0193".equals(codeString))
          return _0193;
        if ("0194".equals(codeString))
          return _0194;
        if ("0195".equals(codeString))
          return _0195;
        if ("0196".equals(codeString))
          return _0196;
        if ("0197".equals(codeString))
          return _0197;
        if ("0198".equals(codeString))
          return _0198;
        if ("0199".equals(codeString))
          return _0199;
        if ("0200".equals(codeString))
          return _0200;
        if ("0201".equals(codeString))
          return _0201;
        if ("0202".equals(codeString))
          return _0202;
        if ("0203".equals(codeString))
          return _0203;
        if ("0204".equals(codeString))
          return _0204;
        if ("0205".equals(codeString))
          return _0205;
        if ("0206".equals(codeString))
          return _0206;
        if ("0207".equals(codeString))
          return _0207;
        if ("0208".equals(codeString))
          return _0208;
        if ("0209".equals(codeString))
          return _0209;
        if ("0210".equals(codeString))
          return _0210;
        if ("0211".equals(codeString))
          return _0211;
        if ("0212".equals(codeString))
          return _0212;
        if ("0213".equals(codeString))
          return _0213;
        if ("0214".equals(codeString))
          return _0214;
        if ("0215".equals(codeString))
          return _0215;
        if ("0216".equals(codeString))
          return _0216;
        if ("0217".equals(codeString))
          return _0217;
        if ("0218".equals(codeString))
          return _0218;
        if ("0219".equals(codeString))
          return _0219;
        if ("0220".equals(codeString))
          return _0220;
        if ("0221".equals(codeString))
          return _0221;
        if ("0222".equals(codeString))
          return _0222;
        if ("0223".equals(codeString))
          return _0223;
        if ("0224".equals(codeString))
          return _0224;
        if ("0225".equals(codeString))
          return _0225;
        if ("0226".equals(codeString))
          return _0226;
        if ("0227".equals(codeString))
          return _0227;
        if ("0228".equals(codeString))
          return _0228;
        if ("0229".equals(codeString))
          return _0229;
        if ("0230".equals(codeString))
          return _0230;
        if ("0231".equals(codeString))
          return _0231;
        if ("0232".equals(codeString))
          return _0232;
        if ("0233".equals(codeString))
          return _0233;
        if ("0234".equals(codeString))
          return _0234;
        if ("0235".equals(codeString))
          return _0235;
        if ("0236".equals(codeString))
          return _0236;
        if ("0237".equals(codeString))
          return _0237;
        if ("0238".equals(codeString))
          return _0238;
        if ("0239".equals(codeString))
          return _0239;
        if ("0243".equals(codeString))
          return _0243;
        if ("0244".equals(codeString))
          return _0244;
        if ("0247".equals(codeString))
          return _0247;
        if ("0248".equals(codeString))
          return _0248;
        if ("0249".equals(codeString))
          return _0249;
        if ("0250".equals(codeString))
          return _0250;
        if ("0251".equals(codeString))
          return _0251;
        if ("0252".equals(codeString))
          return _0252;
        if ("0253".equals(codeString))
          return _0253;
        if ("0254".equals(codeString))
          return _0254;
        if ("0255".equals(codeString))
          return _0255;
        if ("0256".equals(codeString))
          return _0256;
        if ("0257".equals(codeString))
          return _0257;
        if ("0258".equals(codeString))
          return _0258;
        if ("0259".equals(codeString))
          return _0259;
        if ("0260".equals(codeString))
          return _0260;
        if ("0261".equals(codeString))
          return _0261;
        if ("0262".equals(codeString))
          return _0262;
        if ("0263".equals(codeString))
          return _0263;
        if ("0264".equals(codeString))
          return _0264;
        if ("0265".equals(codeString))
          return _0265;
        if ("0266".equals(codeString))
          return _0266;
        if ("0267".equals(codeString))
          return _0267;
        if ("0268".equals(codeString))
          return _0268;
        if ("0269".equals(codeString))
          return _0269;
        if ("0270".equals(codeString))
          return _0270;
        if ("0271".equals(codeString))
          return _0271;
        if ("0280".equals(codeString))
          return _0280;
        if ("0240".equals(codeString))
          return _0240;
        if ("0241".equals(codeString))
          return _0241;
        if ("0242".equals(codeString))
          return _0242;
        if ("0272".equals(codeString))
          return _0272;
        if ("0245".equals(codeString))
          return _0245;
        if ("0246".equals(codeString))
          return _0246;
        if ("0273".equals(codeString))
          return _0273;
        if ("0274".equals(codeString))
          return _0274;
        if ("0275".equals(codeString))
          return _0275;
        if ("0275a".equals(codeString))
          return _0275A;
        if ("0276".equals(codeString))
          return _0276;
        if ("0277".equals(codeString))
          return _0277;
        if ("0278".equals(codeString))
          return _0278;
        if ("0279".equals(codeString))
          return _0279;
        throw new Exception("Unknown V3ObservationMethod code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _DECISIONOBSERVATIONMETHOD: return "_DecisionObservationMethod";
            case ALGM: return "ALGM";
            case BYCL: return "BYCL";
            case GINT: return "GINT";
            case _GENETICOBSERVATIONMETHOD: return "_GeneticObservationMethod";
            case PCR: return "PCR";
            case _OBSERVATIONMETHODAGGREGATE: return "_ObservationMethodAggregate";
            case AVERAGE: return "AVERAGE";
            case COUNT: return "COUNT";
            case MAX: return "MAX";
            case MEDIAN: return "MEDIAN";
            case MIN: return "MIN";
            case MODE: return "MODE";
            case STDEV_P: return "STDEV.P";
            case STDEV_S: return "STDEV.S";
            case SUM: return "SUM";
            case VARIANCE_P: return "VARIANCE.P";
            case VARIANCE_S: return "VARIANCE.S";
            case _VERIFICATIONMETHOD: return "_VerificationMethod";
            case VDOC: return "VDOC";
            case VREG: return "VREG";
            case VTOKEN: return "VTOKEN";
            case VVOICE: return "VVOICE";
            case _0001: return "0001";
            case _0002: return "0002";
            case _0003: return "0003";
            case _0004: return "0004";
            case _0005: return "0005";
            case _0006: return "0006";
            case _0007: return "0007";
            case _0008: return "0008";
            case _0009: return "0009";
            case _0010: return "0010";
            case _0011: return "0011";
            case _0012: return "0012";
            case _0013: return "0013";
            case _0014: return "0014";
            case _0015: return "0015";
            case _0016: return "0016";
            case _0017: return "0017";
            case _0018: return "0018";
            case _0019: return "0019";
            case _0020: return "0020";
            case _0021: return "0021";
            case _0022: return "0022";
            case _0023: return "0023";
            case _0024: return "0024";
            case _0025: return "0025";
            case _0026: return "0026";
            case _0027: return "0027";
            case _0028: return "0028";
            case _0029: return "0029";
            case _0030: return "0030";
            case _0031: return "0031";
            case _0032: return "0032";
            case _0033: return "0033";
            case _0034: return "0034";
            case _0035: return "0035";
            case _0036: return "0036";
            case _0037: return "0037";
            case _0038: return "0038";
            case _0039: return "0039";
            case _0040: return "0040";
            case _0041: return "0041";
            case _0042: return "0042";
            case _0043: return "0043";
            case _0044: return "0044";
            case _0045: return "0045";
            case _0046: return "0046";
            case _0047: return "0047";
            case _0048: return "0048";
            case _0049: return "0049";
            case _0050: return "0050";
            case _0051: return "0051";
            case _0052: return "0052";
            case _0053: return "0053";
            case _0054: return "0054";
            case _0055: return "0055";
            case _0056: return "0056";
            case _0057: return "0057";
            case _0058: return "0058";
            case _0059: return "0059";
            case _0060: return "0060";
            case _0061: return "0061";
            case _0062: return "0062";
            case _0063: return "0063";
            case _0064: return "0064";
            case _0065: return "0065";
            case _0066: return "0066";
            case _0067: return "0067";
            case _0068: return "0068";
            case _0069: return "0069";
            case _0070: return "0070";
            case _0071: return "0071";
            case _0072: return "0072";
            case _0073: return "0073";
            case _0074: return "0074";
            case _0075: return "0075";
            case _0076: return "0076";
            case _0077: return "0077";
            case _0078: return "0078";
            case _0079: return "0079";
            case _0080: return "0080";
            case _0081: return "0081";
            case _0082: return "0082";
            case _0083: return "0083";
            case _0084: return "0084";
            case _0085: return "0085";
            case _0086: return "0086";
            case _0087: return "0087";
            case _0088: return "0088";
            case _0089: return "0089";
            case _0090: return "0090";
            case _0091: return "0091";
            case _0092: return "0092";
            case _0093: return "0093";
            case _0094: return "0094";
            case _0095: return "0095";
            case _0096: return "0096";
            case _0097: return "0097";
            case _0098: return "0098";
            case _0099: return "0099";
            case _0100: return "0100";
            case _0101: return "0101";
            case _0102: return "0102";
            case _0103: return "0103";
            case _0104: return "0104";
            case _0105: return "0105";
            case _0106: return "0106";
            case _0107: return "0107";
            case _0108: return "0108";
            case _0109: return "0109";
            case _0110: return "0110";
            case _0111: return "0111";
            case _0112: return "0112";
            case _0113: return "0113";
            case _0114: return "0114";
            case _0115: return "0115";
            case _0116: return "0116";
            case _0117: return "0117";
            case _0118: return "0118";
            case _0119: return "0119";
            case _0120: return "0120";
            case _0121: return "0121";
            case _0122: return "0122";
            case _0123: return "0123";
            case _0124: return "0124";
            case _0125: return "0125";
            case _0126: return "0126";
            case _0128: return "0128";
            case _0129: return "0129";
            case _0130: return "0130";
            case _0131: return "0131";
            case _0132: return "0132";
            case _0133: return "0133";
            case _0134: return "0134";
            case _0135: return "0135";
            case _0136: return "0136";
            case _0137: return "0137";
            case _0138: return "0138";
            case _0139: return "0139";
            case _0140: return "0140";
            case _0141: return "0141";
            case _0142: return "0142";
            case _0143: return "0143";
            case _0144: return "0144";
            case _0145: return "0145";
            case _0146: return "0146";
            case _0147: return "0147";
            case _0148: return "0148";
            case _0149: return "0149";
            case _0150: return "0150";
            case _0151: return "0151";
            case _0152: return "0152";
            case _0153: return "0153";
            case _0154: return "0154";
            case _0155: return "0155";
            case _0156: return "0156";
            case _0157: return "0157";
            case _0158: return "0158";
            case _0159: return "0159";
            case _0160: return "0160";
            case _0161: return "0161";
            case _0162: return "0162";
            case _0163: return "0163";
            case _0164: return "0164";
            case _0165: return "0165";
            case _0166: return "0166";
            case _0167: return "0167";
            case _0168: return "0168";
            case _0169: return "0169";
            case _0170: return "0170";
            case _0171: return "0171";
            case _0172: return "0172";
            case _0173: return "0173";
            case _0174: return "0174";
            case _0175: return "0175";
            case _0176: return "0176";
            case _0177: return "0177";
            case _0178: return "0178";
            case _0179: return "0179";
            case _0180: return "0180";
            case _0181: return "0181";
            case _0182: return "0182";
            case _0183: return "0183";
            case _0184: return "0184";
            case _0185: return "0185";
            case _0186: return "0186";
            case _0187: return "0187";
            case _0188: return "0188";
            case _0189: return "0189";
            case _0190: return "0190";
            case _0191: return "0191";
            case _0192: return "0192";
            case _0193: return "0193";
            case _0194: return "0194";
            case _0195: return "0195";
            case _0196: return "0196";
            case _0197: return "0197";
            case _0198: return "0198";
            case _0199: return "0199";
            case _0200: return "0200";
            case _0201: return "0201";
            case _0202: return "0202";
            case _0203: return "0203";
            case _0204: return "0204";
            case _0205: return "0205";
            case _0206: return "0206";
            case _0207: return "0207";
            case _0208: return "0208";
            case _0209: return "0209";
            case _0210: return "0210";
            case _0211: return "0211";
            case _0212: return "0212";
            case _0213: return "0213";
            case _0214: return "0214";
            case _0215: return "0215";
            case _0216: return "0216";
            case _0217: return "0217";
            case _0218: return "0218";
            case _0219: return "0219";
            case _0220: return "0220";
            case _0221: return "0221";
            case _0222: return "0222";
            case _0223: return "0223";
            case _0224: return "0224";
            case _0225: return "0225";
            case _0226: return "0226";
            case _0227: return "0227";
            case _0228: return "0228";
            case _0229: return "0229";
            case _0230: return "0230";
            case _0231: return "0231";
            case _0232: return "0232";
            case _0233: return "0233";
            case _0234: return "0234";
            case _0235: return "0235";
            case _0236: return "0236";
            case _0237: return "0237";
            case _0238: return "0238";
            case _0239: return "0239";
            case _0243: return "0243";
            case _0244: return "0244";
            case _0247: return "0247";
            case _0248: return "0248";
            case _0249: return "0249";
            case _0250: return "0250";
            case _0251: return "0251";
            case _0252: return "0252";
            case _0253: return "0253";
            case _0254: return "0254";
            case _0255: return "0255";
            case _0256: return "0256";
            case _0257: return "0257";
            case _0258: return "0258";
            case _0259: return "0259";
            case _0260: return "0260";
            case _0261: return "0261";
            case _0262: return "0262";
            case _0263: return "0263";
            case _0264: return "0264";
            case _0265: return "0265";
            case _0266: return "0266";
            case _0267: return "0267";
            case _0268: return "0268";
            case _0269: return "0269";
            case _0270: return "0270";
            case _0271: return "0271";
            case _0280: return "0280";
            case _0240: return "0240";
            case _0241: return "0241";
            case _0242: return "0242";
            case _0272: return "0272";
            case _0245: return "0245";
            case _0246: return "0246";
            case _0273: return "0273";
            case _0274: return "0274";
            case _0275: return "0275";
            case _0275A: return "0275a";
            case _0276: return "0276";
            case _0277: return "0277";
            case _0278: return "0278";
            case _0279: return "0279";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/ObservationMethod";
        }
        public String getDefinition() {
          switch (this) {
            case _DECISIONOBSERVATIONMETHOD: return "Provides codes for decision methods, initially for assessing the causality of events.";
            case ALGM: return "Reaching a decision through the application of an algorithm designed to weigh the different factors involved.";
            case BYCL: return "Reaching a decision through the use of Bayesian statistical analysis.";
            case GINT: return "Reaching a decision by consideration of the totality of factors involved in order to reach a judgement.";
            case _GENETICOBSERVATIONMETHOD: return "A code that provides additional detail about the means or technique used to ascertain the genetic analysis. Example, PCR, Micro Array";
            case PCR: return "Description: Polymerase Chain Reaction";
            case _OBSERVATIONMETHODAGGREGATE: return "Provides additional detail about the aggregation methods used to compute the aggregated values for an observation. This is an abstract code.";
            case AVERAGE: return "Average of non-null values in the referenced set of values";
            case COUNT: return "Count of non-null values in the referenced set of values";
            case MAX: return "Largest of all non-null values in the referenced set of values.";
            case MEDIAN: return "The median of all non-null values in the referenced set of values.";
            case MIN: return "Smallest of all non-null values in the referenced set of values.";
            case MODE: return "The most common value of all non-null values in the referenced set of values.";
            case STDEV_P: return "Standard Deviation of the values in the referenced set of values, computed over the population.";
            case STDEV_S: return "Standard Deviation of the values in the referenced set of values, computed over a sample of the population.";
            case SUM: return "Sum of non-null values in the referenced set of values";
            case VARIANCE_P: return "Variance of the values in the referenced set of values, computed over the population.";
            case VARIANCE_S: return "Variance of the values in the referenced set of values, computed over a sample of the population.";
            case _VERIFICATIONMETHOD: return "VerificationMethod";
            case VDOC: return "Verification by means of document.\r\n\n                        \n                           Example: Fax, letter, attachment to e-mail.";
            case VREG: return "verification by means of  a response to an electronic query\r\n\n                        \n                           Example: query message to a Covered Party registry application or Coverage Administrator.";
            case VTOKEN: return "Verification by means of electronic token.\r\n\n                        \n                           Example: smartcard, magnetic swipe card, RFID device.";
            case VVOICE: return "Verification by means of voice.\r\n\n                        \n                           Example: By speaking with or calling the Coverage Administrator or Covered Party";
            case _0001: return "Complement fixation";
            case _0002: return "Computed axial tomography";
            case _0003: return "Susceptibility, High Level Aminoglycoside Resistance agar test";
            case _0004: return "Visual, Macroscopic observation";
            case _0005: return "Computed, Magnetic resonance";
            case _0006: return "Computed, Morphometry";
            case _0007: return "Computed, Positron emission tomography";
            case _0008: return "SAMHSA drug assay confirmation";
            case _0009: return "SAMHSA drug assay screening";
            case _0010: return "Serum Neutralization";
            case _0011: return "Titration";
            case _0012: return "Ultrasound";
            case _0013: return "X-ray crystallography";
            case _0014: return "Agglutination";
            case _0015: return "Agglutination, Buffered acidified plate";
            case _0016: return "Agglutination, Card";
            case _0017: return "Agglutination, Hemagglutination";
            case _0018: return "Agglutination, Hemagglutination inhibition";
            case _0019: return "Agglutination, Latex";
            case _0020: return "Agglutination, Plate";
            case _0021: return "Agglutination, Rapid Plate";
            case _0022: return "Agglutination, RBC";
            case _0023: return "Agglutination, Rivanol";
            case _0024: return "Agglutination, Tube";
            case _0025: return "Bioassay";
            case _0026: return "Bioassay, Animal Inoculation";
            case _0027: return "Bioassay, Cytotoxicity";
            case _0028: return "Bioassay, Embryo Infective Dose 50";
            case _0029: return "Bioassay, Embryo Lethal Dose 50";
            case _0030: return "Bioassay, Mouse intercerebral inoculation";
            case _0031: return "Bioassay, qualitative";
            case _0032: return "Bioassay, quantitative";
            case _0033: return "Chemical";
            case _0034: return "Chemical, Differential light absorption";
            case _0035: return "Chemical, Dipstick";
            case _0036: return "Chemical, Dipstick colorimetric laboratory test";
            case _0037: return "Chemical, Test strip";
            case _0038: return "Chromatography";
            case _0039: return "Chromatography, Affinity";
            case _0040: return "Chromatography, Gas liquid";
            case _0041: return "Chromatography, High performance liquid";
            case _0042: return "Chromatography, Liquid";
            case _0043: return "Chromatography, Protein A affinity";
            case _0044: return "Coagulation";
            case _0045: return "Coagulation, Tilt tube";
            case _0046: return "Coagulation, Tilt tube reptilase induced";
            case _0047: return "Count, Automated";
            case _0048: return "Count, Manual";
            case _0049: return "Count, Platelet, Rees-Ecker";
            case _0050: return "Culture, Aerobic";
            case _0051: return "Culture, Anaerobic";
            case _0052: return "Culture, Chicken Embryo";
            case _0053: return "Culture, Delayed secondary enrichment";
            case _0054: return "Culture, Microaerophilic";
            case _0055: return "Culture, Quantitative microbial, cup";
            case _0056: return "Culture, Quantitative microbial, droplet";
            case _0057: return "Culture, Quantitative microbial, filter paper";
            case _0058: return "Culture, Quantitative microbial, pad";
            case _0059: return "Culture, Quantitative microbial, pour plate";
            case _0060: return "Culture, Quantitative microbial, surface streak";
            case _0061: return "Culture, Somatic Cell";
            case _0062: return "Diffusion, Agar";
            case _0063: return "Diffusion, Agar Gel Immunodiffusion";
            case _0064: return "Electrophoresis";
            case _0065: return "Electrophoresis, Agaorse gel";
            case _0066: return "Electrophoresis, citrate agar";
            case _0067: return "Electrophoresis, Immuno";
            case _0068: return "Electrophoresis, Polyacrylamide gel";
            case _0069: return "Electrophoresis, Starch gel";
            case _0070: return "ELISA";
            case _0071: return "ELISA, antigen capture";
            case _0072: return "ELISA, avidin biotin peroxidase complex";
            case _0073: return "ELISA, Kinetic";
            case _0074: return "ELISA, peroxidase-antiperoxidase";
            case _0075: return "Identification, API 20 Strep";
            case _0076: return "Identification, API 20A";
            case _0077: return "Identification, API 20C AUX";
            case _0078: return "Identification, API 20E";
            case _0079: return "Identification, API 20NE";
            case _0080: return "Identification, API 50 CH";
            case _0081: return "Identification, API An-IDENT";
            case _0082: return "Identification, API Coryne";
            case _0083: return "Identification, API Rapid 20E";
            case _0084: return "Identification, API Staph";
            case _0085: return "Identification, API ZYM";
            case _0086: return "Identification, Bacterial";
            case _0087: return "Identification, mini VIDAS";
            case _0088: return "Identification, Phage susceptibility typing";
            case _0089: return "Identification, Quad-FERM+";
            case _0090: return "Identification, RAPIDEC Staph";
            case _0091: return "Identification, Staphaurex";
            case _0092: return "Identification, VIDAS";
            case _0093: return "Identification, Vitek";
            case _0094: return "Identification, VITEK 2";
            case _0095: return "Immune stain";
            case _0096: return "Immune stain, Immunofluorescent antibody, direct";
            case _0097: return "Immune stain, Immunofluorescent antibody, indirect";
            case _0098: return "Immune stain, Immunoperoxidase, Avidin-Biotin Complex";
            case _0099: return "Immune stain, Immunoperoxidase, Peroxidase anti-peroxidase complex";
            case _0100: return "Immune stain, Immunoperoxidase, Protein A-peroxidase complex";
            case _0101: return "Immunoassay";
            case _0102: return "Immunoassay, qualitative, multiple step";
            case _0103: return "Immunoassay, qualitative, single step";
            case _0104: return "Immunoassay, Radioimmunoassay";
            case _0105: return "Immunoassay, semi-quantitative, multiple step";
            case _0106: return "Immunoassay, semi-quantitative, single step";
            case _0107: return "Microscopy";
            case _0108: return "Microscopy, Darkfield";
            case _0109: return "Microscopy, Electron";
            case _0110: return "Microscopy, Electron microscopy tomography";
            case _0111: return "Microscopy, Electron, negative stain";
            case _0112: return "Microscopy, Electron, thick section transmission";
            case _0113: return "Microscopy, Electron, thin section transmission";
            case _0114: return "Microscopy, Light";
            case _0115: return "Microscopy, Polarized light";
            case _0116: return "Microscopy, Scanning electron";
            case _0117: return "Microscopy, Transmission electron";
            case _0118: return "Microscopy, Transparent tape direct examination";
            case _0119: return "Molecular, 3 Self-Sustaining Sequence Replication";
            case _0120: return "Molecular, Branched Chain DNA";
            case _0121: return "Molecular, Hybridization Protection Assay";
            case _0122: return "Molecular, Immune blot";
            case _0123: return "Molecular, In-situ hybridization";
            case _0124: return "Molecular, Ligase Chain Reaction";
            case _0125: return "Molecular, Ligation Activated Transcription";
            case _0126: return "Molecular, Nucleic Acid Probe";
            case _0128: return "Molecular, Nucleic acid probe with amplification\r\n\n                        \r\n\n                        Rationale: Duplicate of code 0126. Use code 0126 instead.";
            case _0129: return "Molecular, Nucleic acid probe with target amplification";
            case _0130: return "Molecular, Nucleic acid reverse transcription";
            case _0131: return "Molecular, Nucleic Acid Sequence Based Analysis";
            case _0132: return "Molecular, Polymerase chain reaction";
            case _0133: return "Molecular, Q-Beta Replicase or probe amplification category method";
            case _0134: return "Molecular, Restriction Fragment Length Polymorphism";
            case _0135: return "Molecular, Southern Blot";
            case _0136: return "Molecular, Strand Displacement Amplification";
            case _0137: return "Molecular, Transcription Mediated Amplification";
            case _0138: return "Molecular, Western Blot";
            case _0139: return "Precipitation, Flocculation";
            case _0140: return "Precipitation, Immune precipitation";
            case _0141: return "Precipitation, Milk ring test";
            case _0142: return "Precipitation, Precipitin";
            case _0143: return "Stain, Acid fast";
            case _0144: return "Stain, Acid fast, fluorochrome";
            case _0145: return "Stain, Acid fast, Kinyoun's cold carbolfuchsin";
            case _0146: return "Stain, Acid fast, Ziehl-Neelsen";
            case _0147: return "Stain, Acid phosphatase";
            case _0148: return "Stain, Acridine orange";
            case _0149: return "Stain, Active brilliant orange KH";
            case _0150: return "Stain, Alazarin red S";
            case _0151: return "Stain, Alcian blue";
            case _0152: return "Stain, Alcian blue with Periodic acid Schiff";
            case _0153: return "Stain, Argentaffin";
            case _0154: return "Stain, Argentaffin silver";
            case _0155: return "Stain, Azure-eosin";
            case _0156: return "Stain, Basic Fuschin";
            case _0157: return "Stain, Bennhold";
            case _0158: return "Stain, Bennhold's Congo red";
            case _0159: return "Stain, Bielschowsky";
            case _0160: return "Stain, Bielschowsky's silver";
            case _0161: return "Stain, Bleach";
            case _0162: return "Stain, Bodian";
            case _0163: return "Stain, Brown-Brenn";
            case _0164: return "Stain, Butyrate-esterase";
            case _0165: return "Stain, Calcofluor white fluorescent";
            case _0166: return "Stain, Carbol-fuchsin";
            case _0167: return "Stain, Carmine";
            case _0168: return "Stain, Churukian-Schenk";
            case _0169: return "Stain, Congo red";
            case _0170: return "Stain, Cresyl echt violet";
            case _0171: return "Stain, Crystal violet";
            case _0172: return "Stain, De Galantha";
            case _0173: return "Stain, Dieterle silver impregnation";
            case _0174: return "Stain, Fite-Farco";
            case _0175: return "Stain, Fontana-Masson silver";
            case _0176: return "Stain, Fouchet";
            case _0177: return "Stain, Gomori";
            case _0178: return "Stain, Gomori methenamine silver";
            case _0179: return "Stain, Gomori-Wheatly trichrome";
            case _0180: return "Stain, Gridley";
            case _0181: return "Stain, Grimelius silver";
            case _0182: return "Stain, Grocott";
            case _0183: return "Stain, Grocott methenamine silver";
            case _0184: return "Stain, Hale's colloidal ferric oxide";
            case _0185: return "Stain, Hale's colloidal iron";
            case _0186: return "Stain, Hansel";
            case _0187: return "Stain, Harris regressive hematoxylin and eosin";
            case _0188: return "Stain, Hematoxylin and eosin";
            case _0189: return "Stain, Highman";
            case _0190: return "Stain, Holzer";
            case _0191: return "Stain, Iron hematoxylin";
            case _0192: return "Stain, Jones";
            case _0193: return "Stain, Jones methenamine silver";
            case _0194: return "Stain, Kossa";
            case _0195: return "Stain, Lawson-Van Gieson";
            case _0196: return "Stain, Loeffler methylene blue";
            case _0197: return "Stain, Luxol fast blue with cresyl violet";
            case _0198: return "Stain, Luxol fast blue with Periodic acid-Schiff";
            case _0199: return "Stain, MacNeal's tetrachrome blood";
            case _0200: return "Stain, Mallory-Heidenhain";
            case _0201: return "Stain, Masson trichrome";
            case _0202: return "Stain, Mayer mucicarmine";
            case _0203: return "Stain, Mayers progressive hematoxylin and eosin";
            case _0204: return "Stain, May-Grunwald Giemsa";
            case _0205: return "Stain, Methyl green";
            case _0206: return "Stain, Methyl green pyronin";
            case _0207: return "Stain, Modified Gomori-Wheatly trichrome";
            case _0208: return "Stain, Modified Masson trichrome";
            case _0209: return "Stain, Modified trichrome";
            case _0210: return "Stain, Movat pentachrome";
            case _0211: return "Stain, Mucicarmine";
            case _0212: return "Stain, Neutral red";
            case _0213: return "Stain, Night blue";
            case _0214: return "Stain, Non-specific esterase";
            case _0215: return "Stain, Oil red-O";
            case _0216: return "Stain, Orcein";
            case _0217: return "Stain, Perls'";
            case _0218: return "Stain, Phosphotungstic acid-hematoxylin";
            case _0219: return "Stain, Potassium ferrocyanide";
            case _0220: return "Stain, Prussian blue";
            case _0221: return "Stain, Putchler modified Bennhold";
            case _0222: return "Stain, Quinacrine fluorescent";
            case _0223: return "Stain, Reticulin";
            case _0224: return "Stain, Rhodamine";
            case _0225: return "Stain, Safranin";
            case _0226: return "Stain, Schmorl";
            case _0227: return "Stain, Seiver-Munger";
            case _0228: return "Stain, Silver";
            case _0229: return "Stain, Specific esterase";
            case _0230: return "Stain, Steiner silver";
            case _0231: return "Stain, Sudan III";
            case _0232: return "Stain, Sudan IVI";
            case _0233: return "Stain, Sulfated alcian blue";
            case _0234: return "Stain, Supravital";
            case _0235: return "Stain, Thioflavine-S";
            case _0236: return "Stain, Three micron Giemsa";
            case _0237: return "Stain, Vassar-Culling";
            case _0238: return "Stain, Vital";
            case _0239: return "Stain, von Kossa";
            case _0243: return "Susceptibility, Minimum bactericidal concentration, macrodilution";
            case _0244: return "Susceptibility, Minimum bactericidal concentration, microdilution";
            case _0247: return "Turbidometric";
            case _0248: return "Turbidometric, Refractometric";
            case _0249: return "Chromatography, Thin Layer";
            case _0250: return "Immunoassay, enzyme-multiplied technique (EMIT)";
            case _0251: return "Flow Cytometry";
            case _0252: return "Radial Immunodiffusion";
            case _0253: return "Immunoassay, Fluorescence Polarization";
            case _0254: return "Electrophoresis, Immunofixation";
            case _0255: return "Dialysis, Direct Equilibrium";
            case _0256: return "Acid Elution, Kleihauer-Betke Method";
            case _0257: return "Immunofluorescence, Anti-Complement";
            case _0258: return "Gas Chromatography/Mass Spectroscopy";
            case _0259: return "Light Scatter, Nephelometry";
            case _0260: return "Immunoassay, IgE Antibody Test";
            case _0261: return "Lymphocyte Microcytotoxicity Assay";
            case _0262: return "Spectrophotometry";
            case _0263: return "Spectrophotometry, Atomic Absorption";
            case _0264: return "Electrochemical, Ion Selective Electrode";
            case _0265: return "Chromatography, Gas";
            case _0266: return "Isoelectric Focusing";
            case _0267: return "Immunoassay, Chemiluminescent";
            case _0268: return "Immunoassay, Microparticle Enzyme";
            case _0269: return "Inductively-Coupled Plasma/Mass Spectrometry";
            case _0270: return "Immunoassay, Immunoradiometric Assay";
            case _0271: return "Coagulation, Photo Optical Clot Detection";
            case _0280: return "Test methods designed to determine a microorganismaTMs susceptibility to being killed by an antibiotic.";
            case _0240: return "Susceptibility, Antibiotic sensitivity, disk";
            case _0241: return "Susceptibility, BACTEC susceptibility test";
            case _0242: return "Susceptibility, Disk dilution";
            case _0272: return "Testing to measure the minimum concentration of the antibacterial agent in a given culture medium below which bacterial growth is not inhibited.";
            case _0245: return "Susceptibility, Minimum Inhibitory concentration, macrodilution";
            case _0246: return "Susceptibility, Minimum Inhibitory concentration, microdilution";
            case _0273: return "Viral Genotype Susceptibility";
            case _0274: return "Viral Phenotype Susceptibility";
            case _0275: return "Gradient Strip";
            case _0275A: return "Minimum Lethal Concentration (MLC)";
            case _0276: return "Testing to measure the minimum concentration of the antibacterial agent in a given culture medium below which bacterial growth is not inhibited.";
            case _0277: return "Serum bactericidal titer";
            case _0278: return "Agar screen";
            case _0279: return "Disk induction";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _DECISIONOBSERVATIONMETHOD: return "DecisionObservationMethod";
            case ALGM: return "algorithm";
            case BYCL: return "bayesian calculation";
            case GINT: return "global introspection";
            case _GENETICOBSERVATIONMETHOD: return "GeneticObservationMethod";
            case PCR: return "PCR";
            case _OBSERVATIONMETHODAGGREGATE: return "observation method aggregate";
            case AVERAGE: return "average";
            case COUNT: return "count";
            case MAX: return "maxima";
            case MEDIAN: return "median";
            case MIN: return "minima";
            case MODE: return "mode";
            case STDEV_P: return "population standard deviation";
            case STDEV_S: return "sample standard deviation";
            case SUM: return "sum";
            case VARIANCE_P: return "population variance";
            case VARIANCE_S: return "sample variance";
            case _VERIFICATIONMETHOD: return "VerificationMethod";
            case VDOC: return "document verification";
            case VREG: return "registry verification";
            case VTOKEN: return "electronic token verification";
            case VVOICE: return "voice-based verification";
            case _0001: return "Complement fixation";
            case _0002: return "Computed axial tomography";
            case _0003: return "HLAR agar test";
            case _0004: return "Macroscopic observation";
            case _0005: return "Magnetic resonance";
            case _0006: return "Morphometry";
            case _0007: return "Positron emission tomography";
            case _0008: return "SAMHSA confirmation";
            case _0009: return "SAMHSA screening";
            case _0010: return "Serum Neutralization";
            case _0011: return "Titration";
            case _0012: return "Ultrasound";
            case _0013: return "X-ray crystallography";
            case _0014: return "Agglutination";
            case _0015: return "Buffered acidified plate agglutination";
            case _0016: return "Card agglutination";
            case _0017: return "Hemagglutination";
            case _0018: return "Hemagglutination inhibition";
            case _0019: return "Latex agglutination";
            case _0020: return "Plate agglutination";
            case _0021: return "Rapid agglutination";
            case _0022: return "RBC agglutination";
            case _0023: return "Rivanol agglutination";
            case _0024: return "Tube agglutination";
            case _0025: return "Bioassay";
            case _0026: return "Animal Inoculation";
            case _0027: return "Cytotoxicity";
            case _0028: return "Embryo infective dose 50";
            case _0029: return "Embryo lethal dose 50";
            case _0030: return "Mouse intercerebral inoculation";
            case _0031: return "Bioassay, qualitative";
            case _0032: return "Bioassay, quantitative";
            case _0033: return "Chemical method";
            case _0034: return "Differential light absorption chemical test";
            case _0035: return "Dipstick";
            case _0036: return "Dipstick colorimetric laboratory test";
            case _0037: return "Test strip";
            case _0038: return "Chromatography";
            case _0039: return "Affinity chromatography";
            case _0040: return "Gas liquid chromatography";
            case _0041: return "High performance liquid chromatography";
            case _0042: return "Liquid Chromatography";
            case _0043: return "Protein A affinity chromatography";
            case _0044: return "Coagulation";
            case _0045: return "Tilt tube coagulation time";
            case _0046: return "Tilt tube reptilase induced coagulation";
            case _0047: return "Automated count";
            case _0048: return "Manual cell count";
            case _0049: return "Platelet count, Rees-Ecker";
            case _0050: return "Aerobic Culture";
            case _0051: return "Anaerobic Culture";
            case _0052: return "Chicken embryo culture";
            case _0053: return "Delayed secondary enrichment";
            case _0054: return "Microaerophilic Culture";
            case _0055: return "Quantitative microbial culture, cup";
            case _0056: return "Quantitative microbial culture, droplet";
            case _0057: return "Quantitative microbial culture, filter paper";
            case _0058: return "Quantitative microbial culture, pad culture";
            case _0059: return "Quantitative microbial culture, pour plate";
            case _0060: return "Quantitative microbial culture, surface streak";
            case _0061: return "Somatic Cell culture";
            case _0062: return "Agar diffusion";
            case _0063: return "Agar Gel Immunodiffusion";
            case _0064: return "Electrophoresis";
            case _0065: return "Agaorse gel electrophoresis";
            case _0066: return "Electrophoresis, citrate agar";
            case _0067: return "Immunoelectrophoresis";
            case _0068: return "Polyacrylamide gel electrophoresis";
            case _0069: return "Starch gel electrophoresis";
            case _0070: return "ELISA";
            case _0071: return "ELISA, antigen capture";
            case _0072: return "ELISA, avidin biotin peroxidase complex";
            case _0073: return "Kinetic ELISA";
            case _0074: return "ELISA, peroxidase-antiperoxidase";
            case _0075: return "API 20 Strep";
            case _0076: return "API 20A";
            case _0077: return "API 20C AUX";
            case _0078: return "API 20E";
            case _0079: return "API 20NE";
            case _0080: return "API 50 CH";
            case _0081: return "API An-IDENT";
            case _0082: return "API Coryne";
            case _0083: return "API Rapid 20E";
            case _0084: return "API Staph";
            case _0085: return "API ZYM";
            case _0086: return "Bacterial identification";
            case _0087: return "mini VIDAS";
            case _0088: return "Phage susceptibility typing";
            case _0089: return "Quad-FERM+";
            case _0090: return "RAPIDEC Staph";
            case _0091: return "Staphaurex";
            case _0092: return "VIDAS";
            case _0093: return "Vitek";
            case _0094: return "VITEK 2";
            case _0095: return "Immune stain";
            case _0096: return "Immunofluorescent antibody, direct";
            case _0097: return "Immunofluorescent antibody, indirect";
            case _0098: return "Immunoperoxidase, Avidin-Biotin Complex";
            case _0099: return "Immunoperoxidase, Peroxidase anti-peroxidase complex";
            case _0100: return "Immunoperoxidase, Protein A-peroxidase complex";
            case _0101: return "Immunoassay";
            case _0102: return "Immunoassay, qualitative, multiple step";
            case _0103: return "Immunoassay, qualitative, single step";
            case _0104: return "Radioimmunoassay";
            case _0105: return "Immunoassay, semi-quantitative, multiple step";
            case _0106: return "Immunoassay, semi-quantitative, single step";
            case _0107: return "Microscopy";
            case _0108: return "Darkfield microscopy";
            case _0109: return "Electron microscopy";
            case _0110: return "Electron microscopy tomography";
            case _0111: return "Electron microscopy, negative stain";
            case _0112: return "Electron microscopy, thick section";
            case _0113: return "Electron microscopy, thin section";
            case _0114: return "Microscopy, Light";
            case _0115: return "Polarizing light microscopy";
            case _0116: return "Scanning electron microscopy";
            case _0117: return "Transmission electron microscopy";
            case _0118: return "Transparent tape direct examination";
            case _0119: return "3 Self-Sustaining Sequence Replication";
            case _0120: return "Branched Chain DNA";
            case _0121: return "Hybridization Protection Assay";
            case _0122: return "Immune blot";
            case _0123: return "In-situ hybridization";
            case _0124: return "Ligase Chain Reaction";
            case _0125: return "Ligation Activated Transcription";
            case _0126: return "Nucleic Acid Probe";
            case _0128: return "Nucleic acid probe with amplification";
            case _0129: return "Nucleic acid probe with target amplification";
            case _0130: return "Nucleic acid reverse transcription";
            case _0131: return "Nucleic Acid Sequence Based Analysis";
            case _0132: return "Polymerase chain reaction";
            case _0133: return "Q-Beta Replicase or probe amplification category method";
            case _0134: return "Restriction Fragment Length Polymorphism";
            case _0135: return "Southern Blot";
            case _0136: return "Strand Displacement Amplification";
            case _0137: return "Transcription Mediated Amplification";
            case _0138: return "Western Blot";
            case _0139: return "Flocculation";
            case _0140: return "Immune precipitation";
            case _0141: return "Milk ring test";
            case _0142: return "Precipitin";
            case _0143: return "Acid fast stain";
            case _0144: return "Acid fast stain, fluorochrome";
            case _0145: return "Acid fast stain, Kinyoun's cold carbolfuchsin";
            case _0146: return "Acid fast stain, Ziehl-Neelsen";
            case _0147: return "Acid phosphatase stain";
            case _0148: return "Acridine orange stain";
            case _0149: return "Active brilliant orange KH stain";
            case _0150: return "Alazarin red S stain";
            case _0151: return "Alcian blue stain";
            case _0152: return "Alcian blue with Periodic acid Schiff stain";
            case _0153: return "Argentaffin stain";
            case _0154: return "Argentaffin silver stain";
            case _0155: return "Azure-eosin stain";
            case _0156: return "Basic Fuschin stain";
            case _0157: return "Bennhold stain";
            case _0158: return "Bennhold's Congo red stain";
            case _0159: return "Bielschowsky stain";
            case _0160: return "Bielschowsky's silver stain";
            case _0161: return "Bleach stain";
            case _0162: return "Bodian stain";
            case _0163: return "Brown-Brenn stain";
            case _0164: return "Butyrate-esterase stain";
            case _0165: return "Calcofluor white fluorescent stain";
            case _0166: return "Carbol-fuchsin stain";
            case _0167: return "Carmine stain";
            case _0168: return "Churukian-Schenk stain";
            case _0169: return "Congo red stain";
            case _0170: return "Cresyl echt violet stain";
            case _0171: return "Crystal violet stain";
            case _0172: return "De Galantha stain";
            case _0173: return "Dieterle silver impregnation stain";
            case _0174: return "Fite-Farco stain";
            case _0175: return "Fontana-Masson silver stain";
            case _0176: return "Fouchet stain";
            case _0177: return "Gomori stain";
            case _0178: return "Gomori methenamine silver stain";
            case _0179: return "Gomori-Wheatly trichrome stain";
            case _0180: return "Gridley stain";
            case _0181: return "Grimelius silver stain";
            case _0182: return "Grocott stain";
            case _0183: return "Grocott methenamine silver stain";
            case _0184: return "Hale's colloidal ferric oxide stain";
            case _0185: return "Hale's colloidal iron stain";
            case _0186: return "Hansel stain";
            case _0187: return "Harris regressive hematoxylin and eosin stain";
            case _0188: return "Hematoxylin and eosin stain";
            case _0189: return "Highman stain";
            case _0190: return "Holzer stain";
            case _0191: return "Iron hematoxylin stain";
            case _0192: return "Jones stain";
            case _0193: return "Jones methenamine silver stain";
            case _0194: return "Kossa stain";
            case _0195: return "Lawson-Van Gieson stain";
            case _0196: return "Loeffler methylene blue stain";
            case _0197: return "Luxol fast blue with cresyl violet stain";
            case _0198: return "Luxol fast blue with Periodic acid-Schiff stain";
            case _0199: return "MacNeal's tetrachrome blood stain";
            case _0200: return "Mallory-Heidenhain stain";
            case _0201: return "Masson trichrome stain";
            case _0202: return "Mayer mucicarmine stain";
            case _0203: return "Mayers progressive hematoxylin and eosin stain";
            case _0204: return "May-Grunwald Giemsa stain";
            case _0205: return "Methyl green stain";
            case _0206: return "Methyl green pyronin stain";
            case _0207: return "Modified Gomori-Wheatly trichrome stain";
            case _0208: return "Modified Masson trichrome stain";
            case _0209: return "Modified trichrome stain";
            case _0210: return "Movat pentachrome stain";
            case _0211: return "Mucicarmine stain";
            case _0212: return "Neutral red stain";
            case _0213: return "Night blue stain";
            case _0214: return "Non-specific esterase stain";
            case _0215: return "Oil red-O stain";
            case _0216: return "Orcein stain";
            case _0217: return "Perls' stain";
            case _0218: return "Phosphotungstic acid-hematoxylin stain";
            case _0219: return "Potassium ferrocyanide stain";
            case _0220: return "Prussian blue stain";
            case _0221: return "Putchler modified Bennhold stain";
            case _0222: return "Quinacrine fluorescent stain";
            case _0223: return "Reticulin stain";
            case _0224: return "Rhodamine stain";
            case _0225: return "Safranin stain";
            case _0226: return "Schmorl stain";
            case _0227: return "Seiver-Munger stain";
            case _0228: return "Silver stain";
            case _0229: return "Specific esterase stain";
            case _0230: return "Steiner silver stain";
            case _0231: return "Sudan III stain";
            case _0232: return "Sudan IVI stain";
            case _0233: return "Sulfated alcian blue stain";
            case _0234: return "Supravital stain";
            case _0235: return "Thioflavine-S stain";
            case _0236: return "Three micron Giemsa stain";
            case _0237: return "Vassar-Culling stain";
            case _0238: return "Vital Stain";
            case _0239: return "von Kossa stain";
            case _0243: return "Minimum bactericidal concentration test, macrodilution";
            case _0244: return "Minimum bactericidal concentration test, microdilution";
            case _0247: return "Turbidometric";
            case _0248: return "Refractometric";
            case _0249: return "Thin layer chromatography (TLC)";
            case _0250: return "EMIT";
            case _0251: return "Flow cytometry (FC)";
            case _0252: return "Radial immunodiffusion (RID)";
            case _0253: return "Fluorescence polarization immunoassay (FPIA)";
            case _0254: return "Immunofixation electrophoresis (IFE)";
            case _0255: return "Equilibrium dialysis";
            case _0256: return "Kleihauer-Betke acid elution";
            case _0257: return "Anti-complement immunofluorescence (ACIF)";
            case _0258: return "GC/MS";
            case _0259: return "Nephelometry";
            case _0260: return "IgE immunoassay antibody";
            case _0261: return "Lymphocyte Microcytotoxicity Assay";
            case _0262: return "Spectrophotometry";
            case _0263: return "Atomic absorption spectrophotometry (AAS)";
            case _0264: return "Ion selective electrode (ISE)";
            case _0265: return "Gas chromatography (GC)";
            case _0266: return "Isoelectric focusing (IEF)";
            case _0267: return "Immunochemiluminescence";
            case _0268: return "Microparticle enzyme immunoassay (MEIA)";
            case _0269: return "ICP/MS";
            case _0270: return "Immunoradiometric assay (IRMA)";
            case _0271: return "Photo optical clot detection";
            case _0280: return "Susceptibility Testing";
            case _0240: return "Antibiotic sensitivity, disk";
            case _0241: return "BACTEC susceptibility test";
            case _0242: return "Disk dilution";
            case _0272: return "Minimum Inhibitory Concentration";
            case _0245: return "Minimum Inhibitory Concentration, macrodilution";
            case _0246: return "Minimum Inhibitory Concentration, microdilution";
            case _0273: return "Viral Genotype Susceptibility";
            case _0274: return "Viral Phenotype Susceptibility";
            case _0275: return "Gradient Strip";
            case _0275A: return "Minimum Lethal Concentration (MLC)";
            case _0276: return "Slow Mycobacteria Susceptibility";
            case _0277: return "Serum bactericidal titer";
            case _0278: return "Agar screen";
            case _0279: return "Disk induction";
            default: return "?";
          }
    }


}

