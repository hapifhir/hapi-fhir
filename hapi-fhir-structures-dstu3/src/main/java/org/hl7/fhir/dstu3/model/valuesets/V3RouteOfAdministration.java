package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.exceptions.FHIRException;

public enum V3RouteOfAdministration {

        /**
         * Route of substance administration classified by administration method.
         */
        _ROUTEBYMETHOD, 
        /**
         * Immersion (soak)
         */
        SOAK, 
        /**
         * Shampoo
         */
        SHAMPOO, 
        /**
         * Translingual
         */
        TRNSLING, 
        /**
         * Swallow, oral
         */
        PO, 
        /**
         * Gargle
         */
        GARGLE, 
        /**
         * Suck, oromucosal
         */
        SUCK, 
        /**
         * Chew
         */
        _CHEW, 
        /**
         * Chew, oral
         */
        CHEW, 
        /**
         * Diffusion
         */
        _DIFFUSION, 
        /**
         * Diffusion, extracorporeal
         */
        EXTCORPDIF, 
        /**
         * Diffusion, hemodialysis
         */
        HEMODIFF, 
        /**
         * Diffusion, transdermal
         */
        TRNSDERMD, 
        /**
         * Dissolve
         */
        _DISSOLVE, 
        /**
         * Dissolve, oral
         */
        DISSOLVE, 
        /**
         * Dissolve, sublingual
         */
        SL, 
        /**
         * Douche
         */
        _DOUCHE, 
        /**
         * Douche, vaginal
         */
        DOUCHE, 
        /**
         * Electro-osmosis
         */
        _ELECTROOSMOSISROUTE, 
        /**
         * Electro-osmosis
         */
        ELECTOSMOS, 
        /**
         * Enema
         */
        _ENEMA, 
        /**
         * Enema, rectal
         */
        ENEMA, 
        /**
         * Enema, rectal retention
         */
        RETENEMA, 
        /**
         * Flush
         */
        _FLUSH, 
        /**
         * Flush, intravenous catheter
         */
        IVFLUSH, 
        /**
         * Implantation
         */
        _IMPLANTATION, 
        /**
         * Implantation, intradermal
         */
        IDIMPLNT, 
        /**
         * Implantation, intravitreal
         */
        IVITIMPLNT, 
        /**
         * Implantation, subcutaneous
         */
        SQIMPLNT, 
        /**
         * Infusion
         */
        _INFUSION, 
        /**
         * Infusion, epidural
         */
        EPI, 
        /**
         * Infusion, intraarterial catheter
         */
        IA, 
        /**
         * Infusion, intracardiac
         */
        IC, 
        /**
         * Infusion, intracoronary
         */
        ICOR, 
        /**
         * Infusion, intraosseous, continuous
         */
        IOSSC, 
        /**
         * Infusion, intrathecal
         */
        IT, 
        /**
         * Infusion, intravenous
         */
        IV, 
        /**
         * Infusion, intravenous catheter
         */
        IVC, 
        /**
         * Infusion, intravenous catheter, continuous
         */
        IVCC, 
        /**
         * Infusion, intravenous catheter, intermittent
         */
        IVCI, 
        /**
         * Infusion, intravenous catheter, pca pump
         */
        PCA, 
        /**
         * Infusion, intravascular
         */
        IVASCINFUS, 
        /**
         * Infusion, subcutaneous
         */
        SQINFUS, 
        /**
         * Inhalation
         */
        _INHALATION, 
        /**
         * Inhalation, oral
         */
        IPINHL, 
        /**
         * Inhalation, oral intermittent flow
         */
        ORIFINHL, 
        /**
         * Inhalation, oral rebreather mask
         */
        REBREATH, 
        /**
         * Inhalation, intermittent positive pressure breathing (ippb)
         */
        IPPB, 
        /**
         * Inhalation, nasal
         */
        NASINHL, 
        /**
         * Inhalation, nasal, prongs
         */
        NASINHLC, 
        /**
         * Inhalation, nebulization
         */
        NEB, 
        /**
         * Inhalation, nebulization, nasal
         */
        NASNEB, 
        /**
         * Inhalation, nebulization, oral
         */
        ORNEB, 
        /**
         * Inhalation, tracheostomy
         */
        TRACH, 
        /**
         * Inhalation, ventilator
         */
        VENT, 
        /**
         * Inhalation, ventimask
         */
        VENTMASK, 
        /**
         * Injection
         */
        _INJECTION, 
        /**
         * Injection, amniotic fluid
         */
        AMNINJ, 
        /**
         * Injection, biliary tract
         */
        BILINJ, 
        /**
         * Injection, for cholangiography
         */
        CHOLINJ, 
        /**
         * Injection, cervical
         */
        CERVINJ, 
        /**
         * Injection, epidural
         */
        EPIDURINJ, 
        /**
         * Injection, epidural, push
         */
        EPIINJ, 
        /**
         * Injection, epidural, slow push
         */
        EPINJSP, 
        /**
         * Injection, extra-amniotic
         */
        EXTRAMNINJ, 
        /**
         * Injection, extracorporeal
         */
        EXTCORPINJ, 
        /**
         * Injection, gastric button
         */
        GBINJ, 
        /**
         * Injection, gingival
         */
        GINGINJ, 
        /**
         * Injection, urinary bladder
         */
        BLADINJ, 
        /**
         * Injection, endosinusial
         */
        ENDOSININJ, 
        /**
         * Injection, hemodialysis port
         */
        HEMOPORT, 
        /**
         * Injection, intra-abdominal
         */
        IABDINJ, 
        /**
         * Injection, intraarterial
         */
        IAINJ, 
        /**
         * Injection, intraarterial, push
         */
        IAINJP, 
        /**
         * Injection, intraarterial, slow push
         */
        IAINJSP, 
        /**
         * Injection, intraarticular
         */
        IARTINJ, 
        /**
         * Injection, intrabursal
         */
        IBURSINJ, 
        /**
         * Injection, intracardiac
         */
        ICARDINJ, 
        /**
         * Injection, intracardiac, rapid push
         */
        ICARDINJRP, 
        /**
         * Injection, intracardiac, slow push
         */
        ICARDINJSP, 
        /**
         * Injection, intracardiac, push
         */
        ICARINJP, 
        /**
         * Injection, intracartilaginous
         */
        ICARTINJ, 
        /**
         * Injection, intracaudal
         */
        ICAUDINJ, 
        /**
         * Injection, intracavernous
         */
        ICAVINJ, 
        /**
         * Injection, intracavitary
         */
        ICAVITINJ, 
        /**
         * Injection, intracerebral
         */
        ICEREBINJ, 
        /**
         * Injection, intracisternal
         */
        ICISTERNINJ, 
        /**
         * Injection, intracoronary
         */
        ICORONINJ, 
        /**
         * Injection, intracoronary, push
         */
        ICORONINJP, 
        /**
         * Injection, intracorpus cavernosum
         */
        ICORPCAVINJ, 
        /**
         * Injection, intradermal
         */
        IDINJ, 
        /**
         * Injection, intradiscal
         */
        IDISCINJ, 
        /**
         * Injection, intraductal
         */
        IDUCTINJ, 
        /**
         * Injection, intradural
         */
        IDURINJ, 
        /**
         * Injection, intraepidermal
         */
        IEPIDINJ, 
        /**
         * Injection, intraepithelial
         */
        IEPITHINJ, 
        /**
         * Injection, intralesional
         */
        ILESINJ, 
        /**
         * Injection, intraluminal
         */
        ILUMINJ, 
        /**
         * Injection, intralymphatic
         */
        ILYMPJINJ, 
        /**
         * Injection, intramuscular
         */
        IM, 
        /**
         * Injection, intramuscular, deep
         */
        IMD, 
        /**
         * Injection, intramuscular, z track
         */
        IMZ, 
        /**
         * Injection, intramedullary
         */
        IMEDULINJ, 
        /**
         * Injection, interameningeal
         */
        INTERMENINJ, 
        /**
         * Injection, interstitial
         */
        INTERSTITINJ, 
        /**
         * Injection, intraocular
         */
        IOINJ, 
        /**
         * Injection, intraosseous
         */
        IOSSINJ, 
        /**
         * Injection, intraovarian
         */
        IOVARINJ, 
        /**
         * Injection, intrapericardial
         */
        IPCARDINJ, 
        /**
         * Injection, intraperitoneal
         */
        IPERINJ, 
        /**
         * Injection, intrapulmonary
         */
        IPINJ, 
        /**
         * Injection, intrapleural
         */
        IPLRINJ, 
        /**
         * Injection, intraprostatic
         */
        IPROSTINJ, 
        /**
         * Injection, insulin pump
         */
        IPUMPINJ, 
        /**
         * Injection, intraspinal
         */
        ISINJ, 
        /**
         * Injection, intrasternal
         */
        ISTERINJ, 
        /**
         * Injection, intrasynovial
         */
        ISYNINJ, 
        /**
         * Injection, intratendinous
         */
        ITENDINJ, 
        /**
         * Injection, intratesticular
         */
        ITESTINJ, 
        /**
         * Injection, intrathoracic
         */
        ITHORINJ, 
        /**
         * Injection, intrathecal
         */
        ITINJ, 
        /**
         * Injection, intratubular
         */
        ITUBINJ, 
        /**
         * Injection, intratumor
         */
        ITUMINJ, 
        /**
         * Injection, intratympanic
         */
        ITYMPINJ, 
        /**
         * Injection, intracervical (uterus)
         */
        IUINJ, 
        /**
         * Injection, intracervical (uterus)
         */
        IUINJC, 
        /**
         * Injection, intraureteral, retrograde
         */
        IURETINJ, 
        /**
         * Injection, intravascular
         */
        IVASCINJ, 
        /**
         * Injection, intraventricular (heart)
         */
        IVENTINJ, 
        /**
         * Injection, intravesicle
         */
        IVESINJ, 
        /**
         * Injection, intravenous
         */
        IVINJ, 
        /**
         * Injection, intravenous, bolus
         */
        IVINJBOL, 
        /**
         * Injection, intravenous, push
         */
        IVPUSH, 
        /**
         * Injection, intravenous, rapid push
         */
        IVRPUSH, 
        /**
         * Injection, intravenous, slow push
         */
        IVSPUSH, 
        /**
         * Injection, intravitreal
         */
        IVITINJ, 
        /**
         * Injection, periarticular
         */
        PAINJ, 
        /**
         * Injection, parenteral
         */
        PARENTINJ, 
        /**
         * Injection, periodontal
         */
        PDONTINJ, 
        /**
         * Injection, peritoneal dialysis port
         */
        PDPINJ, 
        /**
         * Injection, peridural
         */
        PDURINJ, 
        /**
         * Injection, perineural
         */
        PNINJ, 
        /**
         * Injection, paranasal sinuses
         */
        PNSINJ, 
        /**
         * Injection, retrobulbar
         */
        RBINJ, 
        /**
         * Injection, subconjunctival
         */
        SCINJ, 
        /**
         * Injection, sublesional
         */
        SLESINJ, 
        /**
         * Injection, soft tissue
         */
        SOFTISINJ, 
        /**
         * Injection, subcutaneous
         */
        SQ, 
        /**
         * Injection, subarachnoid
         */
        SUBARACHINJ, 
        /**
         * Injection, submucosal
         */
        SUBMUCINJ, 
        /**
         * Injection, transplacental
         */
        TRPLACINJ, 
        /**
         * Injection, transtracheal
         */
        TRTRACHINJ, 
        /**
         * Injection, urethral
         */
        URETHINJ, 
        /**
         * Injection, ureteral
         */
        URETINJ, 
        /**
         * Insertion
         */
        _INSERTION, 
        /**
         * Insertion, cervical (uterine)
         */
        CERVINS, 
        /**
         * Insertion, intraocular, surgical
         */
        IOSURGINS, 
        /**
         * Insertion, intrauterine
         */
        IU, 
        /**
         * Insertion, lacrimal puncta
         */
        LPINS, 
        /**
         * Insertion, rectal
         */
        PR, 
        /**
         * Insertion, subcutaneous, surgical
         */
        SQSURGINS, 
        /**
         * Insertion, urethral
         */
        URETHINS, 
        /**
         * Insertion, vaginal
         */
        VAGINSI, 
        /**
         * Instillation
         */
        _INSTILLATION, 
        /**
         * Instillation, cecostomy
         */
        CECINSTL, 
        /**
         * Instillation, enteral feeding tube
         */
        EFT, 
        /**
         * Instillation, enteral
         */
        ENTINSTL, 
        /**
         * Instillation, gastrostomy tube
         */
        GT, 
        /**
         * Instillation, nasogastric tube
         */
        NGT, 
        /**
         * Instillation, orogastric tube
         */
        OGT, 
        /**
         * Instillation, urinary catheter
         */
        BLADINSTL, 
        /**
         * Instillation, continuous ambulatory peritoneal dialysis port
         */
        CAPDINSTL, 
        /**
         * Instillation, chest tube
         */
        CTINSTL, 
        /**
         * Instillation, endotracheal tube
         */
        ETINSTL, 
        /**
         * Instillation, gastro-jejunostomy tube
         */
        GJT, 
        /**
         * Instillation, intrabronchial
         */
        IBRONCHINSTIL, 
        /**
         * Instillation, intraduodenal
         */
        IDUODINSTIL, 
        /**
         * Instillation, intraesophageal
         */
        IESOPHINSTIL, 
        /**
         * Instillation, intragastric
         */
        IGASTINSTIL, 
        /**
         * Instillation, intraileal
         */
        IILEALINJ, 
        /**
         * Instillation, intraocular
         */
        IOINSTL, 
        /**
         * Instillation, intrasinal
         */
        ISININSTIL, 
        /**
         * Instillation, intratracheal
         */
        ITRACHINSTIL, 
        /**
         * Instillation, intrauterine
         */
        IUINSTL, 
        /**
         * Instillation, jejunostomy tube
         */
        JJTINSTL, 
        /**
         * Instillation, laryngeal
         */
        LARYNGINSTIL, 
        /**
         * Instillation, nasal
         */
        NASALINSTIL, 
        /**
         * Instillation, nasogastric
         */
        NASOGASINSTIL, 
        /**
         * Instillation, nasotracheal tube
         */
        NTT, 
        /**
         * Instillation, orojejunum tube
         */
        OJJ, 
        /**
         * Instillation, otic
         */
        OT, 
        /**
         * Instillation, peritoneal dialysis port
         */
        PDPINSTL, 
        /**
         * Instillation, paranasal sinuses
         */
        PNSINSTL, 
        /**
         * Instillation, rectal
         */
        RECINSTL, 
        /**
         * Instillation, rectal tube
         */
        RECTINSTL, 
        /**
         * Instillation, sinus, unspecified
         */
        SININSTIL, 
        /**
         * Instillation, soft tissue
         */
        SOFTISINSTIL, 
        /**
         * Instillation, tracheostomy
         */
        TRACHINSTL, 
        /**
         * Instillation, transtympanic
         */
        TRTYMPINSTIL, 
        /**
         * Instillation, urethral
         */
        URETHINSTL, 
        /**
         * Iontophoresis
         */
        _IONTOPHORESISROUTE, 
        /**
         * Topical application, iontophoresis
         */
        IONTO, 
        /**
         * Irrigation
         */
        _IRRIGATION, 
        /**
         * Irrigation, genitourinary
         */
        GUIRR, 
        /**
         * Irrigation, intragastric
         */
        IGASTIRR, 
        /**
         * Irrigation, intralesional
         */
        ILESIRR, 
        /**
         * Irrigation, intraocular
         */
        IOIRR, 
        /**
         * Irrigation, urinary bladder
         */
        BLADIRR, 
        /**
         * Irrigation, urinary bladder, continuous
         */
        BLADIRRC, 
        /**
         * Irrigation, urinary bladder, tidal
         */
        BLADIRRT, 
        /**
         * Irrigation, rectal
         */
        RECIRR, 
        /**
         * Lavage
         */
        _LAVAGEROUTE, 
        /**
         * Lavage, intragastric
         */
        IGASTLAV, 
        /**
         * Mucosal absorption
         */
        _MUCOSALABSORPTIONROUTE, 
        /**
         * Mucosal absorption, intraduodenal
         */
        IDOUDMAB, 
        /**
         * Mucosal absorption, intratracheal
         */
        ITRACHMAB, 
        /**
         * Mucosal absorption, submucosal
         */
        SMUCMAB, 
        /**
         * Nebulization
         */
        _NEBULIZATION, 
        /**
         * Nebulization, endotracheal tube
         */
        ETNEB, 
        /**
         * Rinse
         */
        _RINSE, 
        /**
         * Rinse, dental
         */
        DENRINSE, 
        /**
         * Rinse, oral
         */
        ORRINSE, 
        /**
         * Suppository
         */
        _SUPPOSITORYROUTE, 
        /**
         * Suppository, urethral
         */
        URETHSUP, 
        /**
         * Swish
         */
        _SWISH, 
        /**
         * Swish and spit out, oromucosal
         */
        SWISHSPIT, 
        /**
         * Swish and swallow, oromucosal
         */
        SWISHSWAL, 
        /**
         * Topical absorption
         */
        _TOPICALABSORPTIONROUTE, 
        /**
         * Topical absorption, transtympanic
         */
        TTYMPTABSORP, 
        /**
         * Topical application
         */
        _TOPICALAPPLICATION, 
        /**
         * Topical application, soaked dressing
         */
        DRESS, 
        /**
         * Topical application, swab
         */
        SWAB, 
        /**
         * Topical
         */
        TOPICAL, 
        /**
         * Topical application, buccal
         */
        BUC, 
        /**
         * Topical application, cervical
         */
        CERV, 
        /**
         * Topical application, dental
         */
        DEN, 
        /**
         * Topical application, gingival
         */
        GIN, 
        /**
         * Topical application, hair
         */
        HAIR, 
        /**
         * Topical application, intracorneal
         */
        ICORNTA, 
        /**
         * Topical application, intracoronal (dental)
         */
        ICORONTA, 
        /**
         * Topical application, intraesophageal
         */
        IESOPHTA, 
        /**
         * Topical application, intraileal
         */
        IILEALTA, 
        /**
         * Topical application, intralesional
         */
        ILTOP, 
        /**
         * Topical application, intraluminal
         */
        ILUMTA, 
        /**
         * Topical application, intraocular
         */
        IOTOP, 
        /**
         * Topical application, laryngeal
         */
        LARYNGTA, 
        /**
         * Topical application, mucous membrane
         */
        MUC, 
        /**
         * Topical application, nail
         */
        NAIL, 
        /**
         * Topical application, nasal
         */
        NASAL, 
        /**
         * Topical application, ophthalmic
         */
        OPTHALTA, 
        /**
         * Topical application, oral
         */
        ORALTA, 
        /**
         * Topical application, oromucosal
         */
        ORMUC, 
        /**
         * Topical application, oropharyngeal
         */
        OROPHARTA, 
        /**
         * Topical application, perianal
         */
        PERIANAL, 
        /**
         * Topical application, perineal
         */
        PERINEAL, 
        /**
         * Topical application, periodontal
         */
        PDONTTA, 
        /**
         * Topical application, rectal
         */
        RECTAL, 
        /**
         * Topical application, scalp
         */
        SCALP, 
        /**
         * Occlusive dressing technique
         */
        OCDRESTA, 
        /**
         * Topical application, skin
         */
        SKIN, 
        /**
         * Subconjunctival
         */
        SUBCONJTA, 
        /**
         * Topical application, transmucosal
         */
        TMUCTA, 
        /**
         * Insertion, vaginal
         */
        VAGINS, 
        /**
         * Insufflation
         */
        INSUF, 
        /**
         * Transdermal
         */
        TRNSDERM, 
        /**
         * Route of substance administration classified by site.
         */
        _ROUTEBYSITE, 
        /**
         * Amniotic fluid sac
         */
        _AMNIOTICFLUIDSACROUTE, 
        /**
         * Biliary tract
         */
        _BILIARYROUTE, 
        /**
         * Body surface
         */
        _BODYSURFACEROUTE, 
        /**
         * Buccal mucosa
         */
        _BUCCALMUCOSAROUTE, 
        /**
         * Cecostomy
         */
        _CECOSTOMYROUTE, 
        /**
         * Cervix of the uterus
         */
        _CERVICALROUTE, 
        /**
         * Endocervical
         */
        _ENDOCERVICALROUTE, 
        /**
         * Enteral
         */
        _ENTERALROUTE, 
        /**
         * Epidural
         */
        _EPIDURALROUTE, 
        /**
         * Extra-amniotic
         */
        _EXTRAAMNIOTICROUTE, 
        /**
         * Extracorporeal circulation
         */
        _EXTRACORPOREALCIRCULATIONROUTE, 
        /**
         * Gastric
         */
        _GASTRICROUTE, 
        /**
         * Genitourinary
         */
        _GENITOURINARYROUTE, 
        /**
         * Gingival
         */
        _GINGIVALROUTE, 
        /**
         * Hair
         */
        _HAIRROUTE, 
        /**
         * Interameningeal
         */
        _INTERAMENINGEALROUTE, 
        /**
         * Interstitial
         */
        _INTERSTITIALROUTE, 
        /**
         * Intra-abdominal
         */
        _INTRAABDOMINALROUTE, 
        /**
         * Intra-arterial
         */
        _INTRAARTERIALROUTE, 
        /**
         * Intraarticular
         */
        _INTRAARTICULARROUTE, 
        /**
         * Intrabronchial
         */
        _INTRABRONCHIALROUTE, 
        /**
         * Intrabursal
         */
        _INTRABURSALROUTE, 
        /**
         * Intracardiac
         */
        _INTRACARDIACROUTE, 
        /**
         * Intracartilaginous
         */
        _INTRACARTILAGINOUSROUTE, 
        /**
         * Intracaudal
         */
        _INTRACAUDALROUTE, 
        /**
         * Intracavernosal
         */
        _INTRACAVERNOSALROUTE, 
        /**
         * Intracavitary
         */
        _INTRACAVITARYROUTE, 
        /**
         * Intracerebral
         */
        _INTRACEREBRALROUTE, 
        /**
         * Intracervical
         */
        _INTRACERVICALROUTE, 
        /**
         * Intracisternal
         */
        _INTRACISTERNALROUTE, 
        /**
         * Intracorneal
         */
        _INTRACORNEALROUTE, 
        /**
         * Intracoronal (dental)
         */
        _INTRACORONALROUTE, 
        /**
         * Intracoronary
         */
        _INTRACORONARYROUTE, 
        /**
         * Intracorpus cavernosum
         */
        _INTRACORPUSCAVERNOSUMROUTE, 
        /**
         * Intradermal
         */
        _INTRADERMALROUTE, 
        /**
         * Intradiscal
         */
        _INTRADISCALROUTE, 
        /**
         * Intraductal
         */
        _INTRADUCTALROUTE, 
        /**
         * Intraduodenal
         */
        _INTRADUODENALROUTE, 
        /**
         * Intradural
         */
        _INTRADURALROUTE, 
        /**
         * Intraepidermal
         */
        _INTRAEPIDERMALROUTE, 
        /**
         * Intraepithelial
         */
        _INTRAEPITHELIALROUTE, 
        /**
         * Intraesophageal
         */
        _INTRAESOPHAGEALROUTE, 
        /**
         * Intragastric
         */
        _INTRAGASTRICROUTE, 
        /**
         * Intraileal
         */
        _INTRAILEALROUTE, 
        /**
         * Intralesional
         */
        _INTRALESIONALROUTE, 
        /**
         * Intraluminal
         */
        _INTRALUMINALROUTE, 
        /**
         * Intralymphatic
         */
        _INTRALYMPHATICROUTE, 
        /**
         * Intramedullary
         */
        _INTRAMEDULLARYROUTE, 
        /**
         * Intramuscular
         */
        _INTRAMUSCULARROUTE, 
        /**
         * Intraocular
         */
        _INTRAOCULARROUTE, 
        /**
         * Intraosseous
         */
        _INTRAOSSEOUSROUTE, 
        /**
         * Intraovarian
         */
        _INTRAOVARIANROUTE, 
        /**
         * Intrapericardial
         */
        _INTRAPERICARDIALROUTE, 
        /**
         * Intraperitoneal
         */
        _INTRAPERITONEALROUTE, 
        /**
         * Intrapleural
         */
        _INTRAPLEURALROUTE, 
        /**
         * Intraprostatic
         */
        _INTRAPROSTATICROUTE, 
        /**
         * Intrapulmonary
         */
        _INTRAPULMONARYROUTE, 
        /**
         * Intrasinal
         */
        _INTRASINALROUTE, 
        /**
         * Intraspinal
         */
        _INTRASPINALROUTE, 
        /**
         * Intrasternal
         */
        _INTRASTERNALROUTE, 
        /**
         * Intrasynovial
         */
        _INTRASYNOVIALROUTE, 
        /**
         * Intratendinous
         */
        _INTRATENDINOUSROUTE, 
        /**
         * Intratesticular
         */
        _INTRATESTICULARROUTE, 
        /**
         * Intrathecal
         */
        _INTRATHECALROUTE, 
        /**
         * Intrathoracic
         */
        _INTRATHORACICROUTE, 
        /**
         * Intratracheal
         */
        _INTRATRACHEALROUTE, 
        /**
         * Intratubular
         */
        _INTRATUBULARROUTE, 
        /**
         * Intratumor
         */
        _INTRATUMORROUTE, 
        /**
         * Intratympanic
         */
        _INTRATYMPANICROUTE, 
        /**
         * Intrauterine
         */
        _INTRAUTERINEROUTE, 
        /**
         * Intravascular
         */
        _INTRAVASCULARROUTE, 
        /**
         * Intravenous
         */
        _INTRAVENOUSROUTE, 
        /**
         * Intraventricular
         */
        _INTRAVENTRICULARROUTE, 
        /**
         * Intravesicle
         */
        _INTRAVESICLEROUTE, 
        /**
         * Intravitreal
         */
        _INTRAVITREALROUTE, 
        /**
         * Jejunum
         */
        _JEJUNUMROUTE, 
        /**
         * Lacrimal puncta
         */
        _LACRIMALPUNCTAROUTE, 
        /**
         * Laryngeal
         */
        _LARYNGEALROUTE, 
        /**
         * Lingual
         */
        _LINGUALROUTE, 
        /**
         * Mucous membrane
         */
        _MUCOUSMEMBRANEROUTE, 
        /**
         * Nail
         */
        _NAILROUTE, 
        /**
         * Nasal
         */
        _NASALROUTE, 
        /**
         * Ophthalmic
         */
        _OPHTHALMICROUTE, 
        /**
         * Oral
         */
        _ORALROUTE, 
        /**
         * Oromucosal
         */
        _OROMUCOSALROUTE, 
        /**
         * Oropharyngeal
         */
        _OROPHARYNGEALROUTE, 
        /**
         * Otic
         */
        _OTICROUTE, 
        /**
         * Paranasal sinuses
         */
        _PARANASALSINUSESROUTE, 
        /**
         * Parenteral
         */
        _PARENTERALROUTE, 
        /**
         * Perianal
         */
        _PERIANALROUTE, 
        /**
         * Periarticular
         */
        _PERIARTICULARROUTE, 
        /**
         * Peridural
         */
        _PERIDURALROUTE, 
        /**
         * Perineal
         */
        _PERINEALROUTE, 
        /**
         * Perineural
         */
        _PERINEURALROUTE, 
        /**
         * Periodontal
         */
        _PERIODONTALROUTE, 
        /**
         * Pulmonary
         */
        _PULMONARYROUTE, 
        /**
         * Rectal
         */
        _RECTALROUTE, 
        /**
         * Respiratory tract
         */
        _RESPIRATORYTRACTROUTE, 
        /**
         * Retrobulbar
         */
        _RETROBULBARROUTE, 
        /**
         * Scalp
         */
        _SCALPROUTE, 
        /**
         * Sinus, unspecified
         */
        _SINUSUNSPECIFIEDROUTE, 
        /**
         * Skin
         */
        _SKINROUTE, 
        /**
         * Soft tissue
         */
        _SOFTTISSUEROUTE, 
        /**
         * Subarachnoid
         */
        _SUBARACHNOIDROUTE, 
        /**
         * Subconjunctival
         */
        _SUBCONJUNCTIVALROUTE, 
        /**
         * Subcutaneous
         */
        _SUBCUTANEOUSROUTE, 
        /**
         * Sublesional
         */
        _SUBLESIONALROUTE, 
        /**
         * Sublingual
         */
        _SUBLINGUALROUTE, 
        /**
         * Submucosal
         */
        _SUBMUCOSALROUTE, 
        /**
         * Tracheostomy
         */
        _TRACHEOSTOMYROUTE, 
        /**
         * Transmucosal
         */
        _TRANSMUCOSALROUTE, 
        /**
         * Transplacental
         */
        _TRANSPLACENTALROUTE, 
        /**
         * Transtracheal
         */
        _TRANSTRACHEALROUTE, 
        /**
         * Transtympanic
         */
        _TRANSTYMPANICROUTE, 
        /**
         * Ureteral
         */
        _URETERALROUTE, 
        /**
         * Urethral
         */
        _URETHRALROUTE, 
        /**
         * Urinary bladder
         */
        _URINARYBLADDERROUTE, 
        /**
         * Urinary tract
         */
        _URINARYTRACTROUTE, 
        /**
         * Vaginal
         */
        _VAGINALROUTE, 
        /**
         * Vitreous humour
         */
        _VITREOUSHUMOURROUTE, 
        /**
         * added to help the parsers
         */
        NULL;
        public static V3RouteOfAdministration fromCode(String codeString) throws FHIRException {
            if (codeString == null || "".equals(codeString))
                return null;
        if ("_RouteByMethod".equals(codeString))
          return _ROUTEBYMETHOD;
        if ("SOAK".equals(codeString))
          return SOAK;
        if ("SHAMPOO".equals(codeString))
          return SHAMPOO;
        if ("TRNSLING".equals(codeString))
          return TRNSLING;
        if ("PO".equals(codeString))
          return PO;
        if ("GARGLE".equals(codeString))
          return GARGLE;
        if ("SUCK".equals(codeString))
          return SUCK;
        if ("_Chew".equals(codeString))
          return _CHEW;
        if ("CHEW".equals(codeString))
          return CHEW;
        if ("_Diffusion".equals(codeString))
          return _DIFFUSION;
        if ("EXTCORPDIF".equals(codeString))
          return EXTCORPDIF;
        if ("HEMODIFF".equals(codeString))
          return HEMODIFF;
        if ("TRNSDERMD".equals(codeString))
          return TRNSDERMD;
        if ("_Dissolve".equals(codeString))
          return _DISSOLVE;
        if ("DISSOLVE".equals(codeString))
          return DISSOLVE;
        if ("SL".equals(codeString))
          return SL;
        if ("_Douche".equals(codeString))
          return _DOUCHE;
        if ("DOUCHE".equals(codeString))
          return DOUCHE;
        if ("_ElectroOsmosisRoute".equals(codeString))
          return _ELECTROOSMOSISROUTE;
        if ("ELECTOSMOS".equals(codeString))
          return ELECTOSMOS;
        if ("_Enema".equals(codeString))
          return _ENEMA;
        if ("ENEMA".equals(codeString))
          return ENEMA;
        if ("RETENEMA".equals(codeString))
          return RETENEMA;
        if ("_Flush".equals(codeString))
          return _FLUSH;
        if ("IVFLUSH".equals(codeString))
          return IVFLUSH;
        if ("_Implantation".equals(codeString))
          return _IMPLANTATION;
        if ("IDIMPLNT".equals(codeString))
          return IDIMPLNT;
        if ("IVITIMPLNT".equals(codeString))
          return IVITIMPLNT;
        if ("SQIMPLNT".equals(codeString))
          return SQIMPLNT;
        if ("_Infusion".equals(codeString))
          return _INFUSION;
        if ("EPI".equals(codeString))
          return EPI;
        if ("IA".equals(codeString))
          return IA;
        if ("IC".equals(codeString))
          return IC;
        if ("ICOR".equals(codeString))
          return ICOR;
        if ("IOSSC".equals(codeString))
          return IOSSC;
        if ("IT".equals(codeString))
          return IT;
        if ("IV".equals(codeString))
          return IV;
        if ("IVC".equals(codeString))
          return IVC;
        if ("IVCC".equals(codeString))
          return IVCC;
        if ("IVCI".equals(codeString))
          return IVCI;
        if ("PCA".equals(codeString))
          return PCA;
        if ("IVASCINFUS".equals(codeString))
          return IVASCINFUS;
        if ("SQINFUS".equals(codeString))
          return SQINFUS;
        if ("_Inhalation".equals(codeString))
          return _INHALATION;
        if ("IPINHL".equals(codeString))
          return IPINHL;
        if ("ORIFINHL".equals(codeString))
          return ORIFINHL;
        if ("REBREATH".equals(codeString))
          return REBREATH;
        if ("IPPB".equals(codeString))
          return IPPB;
        if ("NASINHL".equals(codeString))
          return NASINHL;
        if ("NASINHLC".equals(codeString))
          return NASINHLC;
        if ("NEB".equals(codeString))
          return NEB;
        if ("NASNEB".equals(codeString))
          return NASNEB;
        if ("ORNEB".equals(codeString))
          return ORNEB;
        if ("TRACH".equals(codeString))
          return TRACH;
        if ("VENT".equals(codeString))
          return VENT;
        if ("VENTMASK".equals(codeString))
          return VENTMASK;
        if ("_Injection".equals(codeString))
          return _INJECTION;
        if ("AMNINJ".equals(codeString))
          return AMNINJ;
        if ("BILINJ".equals(codeString))
          return BILINJ;
        if ("CHOLINJ".equals(codeString))
          return CHOLINJ;
        if ("CERVINJ".equals(codeString))
          return CERVINJ;
        if ("EPIDURINJ".equals(codeString))
          return EPIDURINJ;
        if ("EPIINJ".equals(codeString))
          return EPIINJ;
        if ("EPINJSP".equals(codeString))
          return EPINJSP;
        if ("EXTRAMNINJ".equals(codeString))
          return EXTRAMNINJ;
        if ("EXTCORPINJ".equals(codeString))
          return EXTCORPINJ;
        if ("GBINJ".equals(codeString))
          return GBINJ;
        if ("GINGINJ".equals(codeString))
          return GINGINJ;
        if ("BLADINJ".equals(codeString))
          return BLADINJ;
        if ("ENDOSININJ".equals(codeString))
          return ENDOSININJ;
        if ("HEMOPORT".equals(codeString))
          return HEMOPORT;
        if ("IABDINJ".equals(codeString))
          return IABDINJ;
        if ("IAINJ".equals(codeString))
          return IAINJ;
        if ("IAINJP".equals(codeString))
          return IAINJP;
        if ("IAINJSP".equals(codeString))
          return IAINJSP;
        if ("IARTINJ".equals(codeString))
          return IARTINJ;
        if ("IBURSINJ".equals(codeString))
          return IBURSINJ;
        if ("ICARDINJ".equals(codeString))
          return ICARDINJ;
        if ("ICARDINJRP".equals(codeString))
          return ICARDINJRP;
        if ("ICARDINJSP".equals(codeString))
          return ICARDINJSP;
        if ("ICARINJP".equals(codeString))
          return ICARINJP;
        if ("ICARTINJ".equals(codeString))
          return ICARTINJ;
        if ("ICAUDINJ".equals(codeString))
          return ICAUDINJ;
        if ("ICAVINJ".equals(codeString))
          return ICAVINJ;
        if ("ICAVITINJ".equals(codeString))
          return ICAVITINJ;
        if ("ICEREBINJ".equals(codeString))
          return ICEREBINJ;
        if ("ICISTERNINJ".equals(codeString))
          return ICISTERNINJ;
        if ("ICORONINJ".equals(codeString))
          return ICORONINJ;
        if ("ICORONINJP".equals(codeString))
          return ICORONINJP;
        if ("ICORPCAVINJ".equals(codeString))
          return ICORPCAVINJ;
        if ("IDINJ".equals(codeString))
          return IDINJ;
        if ("IDISCINJ".equals(codeString))
          return IDISCINJ;
        if ("IDUCTINJ".equals(codeString))
          return IDUCTINJ;
        if ("IDURINJ".equals(codeString))
          return IDURINJ;
        if ("IEPIDINJ".equals(codeString))
          return IEPIDINJ;
        if ("IEPITHINJ".equals(codeString))
          return IEPITHINJ;
        if ("ILESINJ".equals(codeString))
          return ILESINJ;
        if ("ILUMINJ".equals(codeString))
          return ILUMINJ;
        if ("ILYMPJINJ".equals(codeString))
          return ILYMPJINJ;
        if ("IM".equals(codeString))
          return IM;
        if ("IMD".equals(codeString))
          return IMD;
        if ("IMZ".equals(codeString))
          return IMZ;
        if ("IMEDULINJ".equals(codeString))
          return IMEDULINJ;
        if ("INTERMENINJ".equals(codeString))
          return INTERMENINJ;
        if ("INTERSTITINJ".equals(codeString))
          return INTERSTITINJ;
        if ("IOINJ".equals(codeString))
          return IOINJ;
        if ("IOSSINJ".equals(codeString))
          return IOSSINJ;
        if ("IOVARINJ".equals(codeString))
          return IOVARINJ;
        if ("IPCARDINJ".equals(codeString))
          return IPCARDINJ;
        if ("IPERINJ".equals(codeString))
          return IPERINJ;
        if ("IPINJ".equals(codeString))
          return IPINJ;
        if ("IPLRINJ".equals(codeString))
          return IPLRINJ;
        if ("IPROSTINJ".equals(codeString))
          return IPROSTINJ;
        if ("IPUMPINJ".equals(codeString))
          return IPUMPINJ;
        if ("ISINJ".equals(codeString))
          return ISINJ;
        if ("ISTERINJ".equals(codeString))
          return ISTERINJ;
        if ("ISYNINJ".equals(codeString))
          return ISYNINJ;
        if ("ITENDINJ".equals(codeString))
          return ITENDINJ;
        if ("ITESTINJ".equals(codeString))
          return ITESTINJ;
        if ("ITHORINJ".equals(codeString))
          return ITHORINJ;
        if ("ITINJ".equals(codeString))
          return ITINJ;
        if ("ITUBINJ".equals(codeString))
          return ITUBINJ;
        if ("ITUMINJ".equals(codeString))
          return ITUMINJ;
        if ("ITYMPINJ".equals(codeString))
          return ITYMPINJ;
        if ("IUINJ".equals(codeString))
          return IUINJ;
        if ("IUINJC".equals(codeString))
          return IUINJC;
        if ("IURETINJ".equals(codeString))
          return IURETINJ;
        if ("IVASCINJ".equals(codeString))
          return IVASCINJ;
        if ("IVENTINJ".equals(codeString))
          return IVENTINJ;
        if ("IVESINJ".equals(codeString))
          return IVESINJ;
        if ("IVINJ".equals(codeString))
          return IVINJ;
        if ("IVINJBOL".equals(codeString))
          return IVINJBOL;
        if ("IVPUSH".equals(codeString))
          return IVPUSH;
        if ("IVRPUSH".equals(codeString))
          return IVRPUSH;
        if ("IVSPUSH".equals(codeString))
          return IVSPUSH;
        if ("IVITINJ".equals(codeString))
          return IVITINJ;
        if ("PAINJ".equals(codeString))
          return PAINJ;
        if ("PARENTINJ".equals(codeString))
          return PARENTINJ;
        if ("PDONTINJ".equals(codeString))
          return PDONTINJ;
        if ("PDPINJ".equals(codeString))
          return PDPINJ;
        if ("PDURINJ".equals(codeString))
          return PDURINJ;
        if ("PNINJ".equals(codeString))
          return PNINJ;
        if ("PNSINJ".equals(codeString))
          return PNSINJ;
        if ("RBINJ".equals(codeString))
          return RBINJ;
        if ("SCINJ".equals(codeString))
          return SCINJ;
        if ("SLESINJ".equals(codeString))
          return SLESINJ;
        if ("SOFTISINJ".equals(codeString))
          return SOFTISINJ;
        if ("SQ".equals(codeString))
          return SQ;
        if ("SUBARACHINJ".equals(codeString))
          return SUBARACHINJ;
        if ("SUBMUCINJ".equals(codeString))
          return SUBMUCINJ;
        if ("TRPLACINJ".equals(codeString))
          return TRPLACINJ;
        if ("TRTRACHINJ".equals(codeString))
          return TRTRACHINJ;
        if ("URETHINJ".equals(codeString))
          return URETHINJ;
        if ("URETINJ".equals(codeString))
          return URETINJ;
        if ("_Insertion".equals(codeString))
          return _INSERTION;
        if ("CERVINS".equals(codeString))
          return CERVINS;
        if ("IOSURGINS".equals(codeString))
          return IOSURGINS;
        if ("IU".equals(codeString))
          return IU;
        if ("LPINS".equals(codeString))
          return LPINS;
        if ("PR".equals(codeString))
          return PR;
        if ("SQSURGINS".equals(codeString))
          return SQSURGINS;
        if ("URETHINS".equals(codeString))
          return URETHINS;
        if ("VAGINSI".equals(codeString))
          return VAGINSI;
        if ("_Instillation".equals(codeString))
          return _INSTILLATION;
        if ("CECINSTL".equals(codeString))
          return CECINSTL;
        if ("EFT".equals(codeString))
          return EFT;
        if ("ENTINSTL".equals(codeString))
          return ENTINSTL;
        if ("GT".equals(codeString))
          return GT;
        if ("NGT".equals(codeString))
          return NGT;
        if ("OGT".equals(codeString))
          return OGT;
        if ("BLADINSTL".equals(codeString))
          return BLADINSTL;
        if ("CAPDINSTL".equals(codeString))
          return CAPDINSTL;
        if ("CTINSTL".equals(codeString))
          return CTINSTL;
        if ("ETINSTL".equals(codeString))
          return ETINSTL;
        if ("GJT".equals(codeString))
          return GJT;
        if ("IBRONCHINSTIL".equals(codeString))
          return IBRONCHINSTIL;
        if ("IDUODINSTIL".equals(codeString))
          return IDUODINSTIL;
        if ("IESOPHINSTIL".equals(codeString))
          return IESOPHINSTIL;
        if ("IGASTINSTIL".equals(codeString))
          return IGASTINSTIL;
        if ("IILEALINJ".equals(codeString))
          return IILEALINJ;
        if ("IOINSTL".equals(codeString))
          return IOINSTL;
        if ("ISININSTIL".equals(codeString))
          return ISININSTIL;
        if ("ITRACHINSTIL".equals(codeString))
          return ITRACHINSTIL;
        if ("IUINSTL".equals(codeString))
          return IUINSTL;
        if ("JJTINSTL".equals(codeString))
          return JJTINSTL;
        if ("LARYNGINSTIL".equals(codeString))
          return LARYNGINSTIL;
        if ("NASALINSTIL".equals(codeString))
          return NASALINSTIL;
        if ("NASOGASINSTIL".equals(codeString))
          return NASOGASINSTIL;
        if ("NTT".equals(codeString))
          return NTT;
        if ("OJJ".equals(codeString))
          return OJJ;
        if ("OT".equals(codeString))
          return OT;
        if ("PDPINSTL".equals(codeString))
          return PDPINSTL;
        if ("PNSINSTL".equals(codeString))
          return PNSINSTL;
        if ("RECINSTL".equals(codeString))
          return RECINSTL;
        if ("RECTINSTL".equals(codeString))
          return RECTINSTL;
        if ("SININSTIL".equals(codeString))
          return SININSTIL;
        if ("SOFTISINSTIL".equals(codeString))
          return SOFTISINSTIL;
        if ("TRACHINSTL".equals(codeString))
          return TRACHINSTL;
        if ("TRTYMPINSTIL".equals(codeString))
          return TRTYMPINSTIL;
        if ("URETHINSTL".equals(codeString))
          return URETHINSTL;
        if ("_IontophoresisRoute".equals(codeString))
          return _IONTOPHORESISROUTE;
        if ("IONTO".equals(codeString))
          return IONTO;
        if ("_Irrigation".equals(codeString))
          return _IRRIGATION;
        if ("GUIRR".equals(codeString))
          return GUIRR;
        if ("IGASTIRR".equals(codeString))
          return IGASTIRR;
        if ("ILESIRR".equals(codeString))
          return ILESIRR;
        if ("IOIRR".equals(codeString))
          return IOIRR;
        if ("BLADIRR".equals(codeString))
          return BLADIRR;
        if ("BLADIRRC".equals(codeString))
          return BLADIRRC;
        if ("BLADIRRT".equals(codeString))
          return BLADIRRT;
        if ("RECIRR".equals(codeString))
          return RECIRR;
        if ("_LavageRoute".equals(codeString))
          return _LAVAGEROUTE;
        if ("IGASTLAV".equals(codeString))
          return IGASTLAV;
        if ("_MucosalAbsorptionRoute".equals(codeString))
          return _MUCOSALABSORPTIONROUTE;
        if ("IDOUDMAB".equals(codeString))
          return IDOUDMAB;
        if ("ITRACHMAB".equals(codeString))
          return ITRACHMAB;
        if ("SMUCMAB".equals(codeString))
          return SMUCMAB;
        if ("_Nebulization".equals(codeString))
          return _NEBULIZATION;
        if ("ETNEB".equals(codeString))
          return ETNEB;
        if ("_Rinse".equals(codeString))
          return _RINSE;
        if ("DENRINSE".equals(codeString))
          return DENRINSE;
        if ("ORRINSE".equals(codeString))
          return ORRINSE;
        if ("_SuppositoryRoute".equals(codeString))
          return _SUPPOSITORYROUTE;
        if ("URETHSUP".equals(codeString))
          return URETHSUP;
        if ("_Swish".equals(codeString))
          return _SWISH;
        if ("SWISHSPIT".equals(codeString))
          return SWISHSPIT;
        if ("SWISHSWAL".equals(codeString))
          return SWISHSWAL;
        if ("_TopicalAbsorptionRoute".equals(codeString))
          return _TOPICALABSORPTIONROUTE;
        if ("TTYMPTABSORP".equals(codeString))
          return TTYMPTABSORP;
        if ("_TopicalApplication".equals(codeString))
          return _TOPICALAPPLICATION;
        if ("DRESS".equals(codeString))
          return DRESS;
        if ("SWAB".equals(codeString))
          return SWAB;
        if ("TOPICAL".equals(codeString))
          return TOPICAL;
        if ("BUC".equals(codeString))
          return BUC;
        if ("CERV".equals(codeString))
          return CERV;
        if ("DEN".equals(codeString))
          return DEN;
        if ("GIN".equals(codeString))
          return GIN;
        if ("HAIR".equals(codeString))
          return HAIR;
        if ("ICORNTA".equals(codeString))
          return ICORNTA;
        if ("ICORONTA".equals(codeString))
          return ICORONTA;
        if ("IESOPHTA".equals(codeString))
          return IESOPHTA;
        if ("IILEALTA".equals(codeString))
          return IILEALTA;
        if ("ILTOP".equals(codeString))
          return ILTOP;
        if ("ILUMTA".equals(codeString))
          return ILUMTA;
        if ("IOTOP".equals(codeString))
          return IOTOP;
        if ("LARYNGTA".equals(codeString))
          return LARYNGTA;
        if ("MUC".equals(codeString))
          return MUC;
        if ("NAIL".equals(codeString))
          return NAIL;
        if ("NASAL".equals(codeString))
          return NASAL;
        if ("OPTHALTA".equals(codeString))
          return OPTHALTA;
        if ("ORALTA".equals(codeString))
          return ORALTA;
        if ("ORMUC".equals(codeString))
          return ORMUC;
        if ("OROPHARTA".equals(codeString))
          return OROPHARTA;
        if ("PERIANAL".equals(codeString))
          return PERIANAL;
        if ("PERINEAL".equals(codeString))
          return PERINEAL;
        if ("PDONTTA".equals(codeString))
          return PDONTTA;
        if ("RECTAL".equals(codeString))
          return RECTAL;
        if ("SCALP".equals(codeString))
          return SCALP;
        if ("OCDRESTA".equals(codeString))
          return OCDRESTA;
        if ("SKIN".equals(codeString))
          return SKIN;
        if ("SUBCONJTA".equals(codeString))
          return SUBCONJTA;
        if ("TMUCTA".equals(codeString))
          return TMUCTA;
        if ("VAGINS".equals(codeString))
          return VAGINS;
        if ("INSUF".equals(codeString))
          return INSUF;
        if ("TRNSDERM".equals(codeString))
          return TRNSDERM;
        if ("_RouteBySite".equals(codeString))
          return _ROUTEBYSITE;
        if ("_AmnioticFluidSacRoute".equals(codeString))
          return _AMNIOTICFLUIDSACROUTE;
        if ("_BiliaryRoute".equals(codeString))
          return _BILIARYROUTE;
        if ("_BodySurfaceRoute".equals(codeString))
          return _BODYSURFACEROUTE;
        if ("_BuccalMucosaRoute".equals(codeString))
          return _BUCCALMUCOSAROUTE;
        if ("_CecostomyRoute".equals(codeString))
          return _CECOSTOMYROUTE;
        if ("_CervicalRoute".equals(codeString))
          return _CERVICALROUTE;
        if ("_EndocervicalRoute".equals(codeString))
          return _ENDOCERVICALROUTE;
        if ("_EnteralRoute".equals(codeString))
          return _ENTERALROUTE;
        if ("_EpiduralRoute".equals(codeString))
          return _EPIDURALROUTE;
        if ("_ExtraAmnioticRoute".equals(codeString))
          return _EXTRAAMNIOTICROUTE;
        if ("_ExtracorporealCirculationRoute".equals(codeString))
          return _EXTRACORPOREALCIRCULATIONROUTE;
        if ("_GastricRoute".equals(codeString))
          return _GASTRICROUTE;
        if ("_GenitourinaryRoute".equals(codeString))
          return _GENITOURINARYROUTE;
        if ("_GingivalRoute".equals(codeString))
          return _GINGIVALROUTE;
        if ("_HairRoute".equals(codeString))
          return _HAIRROUTE;
        if ("_InterameningealRoute".equals(codeString))
          return _INTERAMENINGEALROUTE;
        if ("_InterstitialRoute".equals(codeString))
          return _INTERSTITIALROUTE;
        if ("_IntraabdominalRoute".equals(codeString))
          return _INTRAABDOMINALROUTE;
        if ("_IntraarterialRoute".equals(codeString))
          return _INTRAARTERIALROUTE;
        if ("_IntraarticularRoute".equals(codeString))
          return _INTRAARTICULARROUTE;
        if ("_IntrabronchialRoute".equals(codeString))
          return _INTRABRONCHIALROUTE;
        if ("_IntrabursalRoute".equals(codeString))
          return _INTRABURSALROUTE;
        if ("_IntracardiacRoute".equals(codeString))
          return _INTRACARDIACROUTE;
        if ("_IntracartilaginousRoute".equals(codeString))
          return _INTRACARTILAGINOUSROUTE;
        if ("_IntracaudalRoute".equals(codeString))
          return _INTRACAUDALROUTE;
        if ("_IntracavernosalRoute".equals(codeString))
          return _INTRACAVERNOSALROUTE;
        if ("_IntracavitaryRoute".equals(codeString))
          return _INTRACAVITARYROUTE;
        if ("_IntracerebralRoute".equals(codeString))
          return _INTRACEREBRALROUTE;
        if ("_IntracervicalRoute".equals(codeString))
          return _INTRACERVICALROUTE;
        if ("_IntracisternalRoute".equals(codeString))
          return _INTRACISTERNALROUTE;
        if ("_IntracornealRoute".equals(codeString))
          return _INTRACORNEALROUTE;
        if ("_IntracoronalRoute".equals(codeString))
          return _INTRACORONALROUTE;
        if ("_IntracoronaryRoute".equals(codeString))
          return _INTRACORONARYROUTE;
        if ("_IntracorpusCavernosumRoute".equals(codeString))
          return _INTRACORPUSCAVERNOSUMROUTE;
        if ("_IntradermalRoute".equals(codeString))
          return _INTRADERMALROUTE;
        if ("_IntradiscalRoute".equals(codeString))
          return _INTRADISCALROUTE;
        if ("_IntraductalRoute".equals(codeString))
          return _INTRADUCTALROUTE;
        if ("_IntraduodenalRoute".equals(codeString))
          return _INTRADUODENALROUTE;
        if ("_IntraduralRoute".equals(codeString))
          return _INTRADURALROUTE;
        if ("_IntraepidermalRoute".equals(codeString))
          return _INTRAEPIDERMALROUTE;
        if ("_IntraepithelialRoute".equals(codeString))
          return _INTRAEPITHELIALROUTE;
        if ("_IntraesophagealRoute".equals(codeString))
          return _INTRAESOPHAGEALROUTE;
        if ("_IntragastricRoute".equals(codeString))
          return _INTRAGASTRICROUTE;
        if ("_IntrailealRoute".equals(codeString))
          return _INTRAILEALROUTE;
        if ("_IntralesionalRoute".equals(codeString))
          return _INTRALESIONALROUTE;
        if ("_IntraluminalRoute".equals(codeString))
          return _INTRALUMINALROUTE;
        if ("_IntralymphaticRoute".equals(codeString))
          return _INTRALYMPHATICROUTE;
        if ("_IntramedullaryRoute".equals(codeString))
          return _INTRAMEDULLARYROUTE;
        if ("_IntramuscularRoute".equals(codeString))
          return _INTRAMUSCULARROUTE;
        if ("_IntraocularRoute".equals(codeString))
          return _INTRAOCULARROUTE;
        if ("_IntraosseousRoute".equals(codeString))
          return _INTRAOSSEOUSROUTE;
        if ("_IntraovarianRoute".equals(codeString))
          return _INTRAOVARIANROUTE;
        if ("_IntrapericardialRoute".equals(codeString))
          return _INTRAPERICARDIALROUTE;
        if ("_IntraperitonealRoute".equals(codeString))
          return _INTRAPERITONEALROUTE;
        if ("_IntrapleuralRoute".equals(codeString))
          return _INTRAPLEURALROUTE;
        if ("_IntraprostaticRoute".equals(codeString))
          return _INTRAPROSTATICROUTE;
        if ("_IntrapulmonaryRoute".equals(codeString))
          return _INTRAPULMONARYROUTE;
        if ("_IntrasinalRoute".equals(codeString))
          return _INTRASINALROUTE;
        if ("_IntraspinalRoute".equals(codeString))
          return _INTRASPINALROUTE;
        if ("_IntrasternalRoute".equals(codeString))
          return _INTRASTERNALROUTE;
        if ("_IntrasynovialRoute".equals(codeString))
          return _INTRASYNOVIALROUTE;
        if ("_IntratendinousRoute".equals(codeString))
          return _INTRATENDINOUSROUTE;
        if ("_IntratesticularRoute".equals(codeString))
          return _INTRATESTICULARROUTE;
        if ("_IntrathecalRoute".equals(codeString))
          return _INTRATHECALROUTE;
        if ("_IntrathoracicRoute".equals(codeString))
          return _INTRATHORACICROUTE;
        if ("_IntratrachealRoute".equals(codeString))
          return _INTRATRACHEALROUTE;
        if ("_IntratubularRoute".equals(codeString))
          return _INTRATUBULARROUTE;
        if ("_IntratumorRoute".equals(codeString))
          return _INTRATUMORROUTE;
        if ("_IntratympanicRoute".equals(codeString))
          return _INTRATYMPANICROUTE;
        if ("_IntrauterineRoute".equals(codeString))
          return _INTRAUTERINEROUTE;
        if ("_IntravascularRoute".equals(codeString))
          return _INTRAVASCULARROUTE;
        if ("_IntravenousRoute".equals(codeString))
          return _INTRAVENOUSROUTE;
        if ("_IntraventricularRoute".equals(codeString))
          return _INTRAVENTRICULARROUTE;
        if ("_IntravesicleRoute".equals(codeString))
          return _INTRAVESICLEROUTE;
        if ("_IntravitrealRoute".equals(codeString))
          return _INTRAVITREALROUTE;
        if ("_JejunumRoute".equals(codeString))
          return _JEJUNUMROUTE;
        if ("_LacrimalPunctaRoute".equals(codeString))
          return _LACRIMALPUNCTAROUTE;
        if ("_LaryngealRoute".equals(codeString))
          return _LARYNGEALROUTE;
        if ("_LingualRoute".equals(codeString))
          return _LINGUALROUTE;
        if ("_MucousMembraneRoute".equals(codeString))
          return _MUCOUSMEMBRANEROUTE;
        if ("_NailRoute".equals(codeString))
          return _NAILROUTE;
        if ("_NasalRoute".equals(codeString))
          return _NASALROUTE;
        if ("_OphthalmicRoute".equals(codeString))
          return _OPHTHALMICROUTE;
        if ("_OralRoute".equals(codeString))
          return _ORALROUTE;
        if ("_OromucosalRoute".equals(codeString))
          return _OROMUCOSALROUTE;
        if ("_OropharyngealRoute".equals(codeString))
          return _OROPHARYNGEALROUTE;
        if ("_OticRoute".equals(codeString))
          return _OTICROUTE;
        if ("_ParanasalSinusesRoute".equals(codeString))
          return _PARANASALSINUSESROUTE;
        if ("_ParenteralRoute".equals(codeString))
          return _PARENTERALROUTE;
        if ("_PerianalRoute".equals(codeString))
          return _PERIANALROUTE;
        if ("_PeriarticularRoute".equals(codeString))
          return _PERIARTICULARROUTE;
        if ("_PeriduralRoute".equals(codeString))
          return _PERIDURALROUTE;
        if ("_PerinealRoute".equals(codeString))
          return _PERINEALROUTE;
        if ("_PerineuralRoute".equals(codeString))
          return _PERINEURALROUTE;
        if ("_PeriodontalRoute".equals(codeString))
          return _PERIODONTALROUTE;
        if ("_PulmonaryRoute".equals(codeString))
          return _PULMONARYROUTE;
        if ("_RectalRoute".equals(codeString))
          return _RECTALROUTE;
        if ("_RespiratoryTractRoute".equals(codeString))
          return _RESPIRATORYTRACTROUTE;
        if ("_RetrobulbarRoute".equals(codeString))
          return _RETROBULBARROUTE;
        if ("_ScalpRoute".equals(codeString))
          return _SCALPROUTE;
        if ("_SinusUnspecifiedRoute".equals(codeString))
          return _SINUSUNSPECIFIEDROUTE;
        if ("_SkinRoute".equals(codeString))
          return _SKINROUTE;
        if ("_SoftTissueRoute".equals(codeString))
          return _SOFTTISSUEROUTE;
        if ("_SubarachnoidRoute".equals(codeString))
          return _SUBARACHNOIDROUTE;
        if ("_SubconjunctivalRoute".equals(codeString))
          return _SUBCONJUNCTIVALROUTE;
        if ("_SubcutaneousRoute".equals(codeString))
          return _SUBCUTANEOUSROUTE;
        if ("_SublesionalRoute".equals(codeString))
          return _SUBLESIONALROUTE;
        if ("_SublingualRoute".equals(codeString))
          return _SUBLINGUALROUTE;
        if ("_SubmucosalRoute".equals(codeString))
          return _SUBMUCOSALROUTE;
        if ("_TracheostomyRoute".equals(codeString))
          return _TRACHEOSTOMYROUTE;
        if ("_TransmucosalRoute".equals(codeString))
          return _TRANSMUCOSALROUTE;
        if ("_TransplacentalRoute".equals(codeString))
          return _TRANSPLACENTALROUTE;
        if ("_TranstrachealRoute".equals(codeString))
          return _TRANSTRACHEALROUTE;
        if ("_TranstympanicRoute".equals(codeString))
          return _TRANSTYMPANICROUTE;
        if ("_UreteralRoute".equals(codeString))
          return _URETERALROUTE;
        if ("_UrethralRoute".equals(codeString))
          return _URETHRALROUTE;
        if ("_UrinaryBladderRoute".equals(codeString))
          return _URINARYBLADDERROUTE;
        if ("_UrinaryTractRoute".equals(codeString))
          return _URINARYTRACTROUTE;
        if ("_VaginalRoute".equals(codeString))
          return _VAGINALROUTE;
        if ("_VitreousHumourRoute".equals(codeString))
          return _VITREOUSHUMOURROUTE;
        throw new FHIRException("Unknown V3RouteOfAdministration code '"+codeString+"'");
        }
        public String toCode() {
          switch (this) {
            case _ROUTEBYMETHOD: return "_RouteByMethod";
            case SOAK: return "SOAK";
            case SHAMPOO: return "SHAMPOO";
            case TRNSLING: return "TRNSLING";
            case PO: return "PO";
            case GARGLE: return "GARGLE";
            case SUCK: return "SUCK";
            case _CHEW: return "_Chew";
            case CHEW: return "CHEW";
            case _DIFFUSION: return "_Diffusion";
            case EXTCORPDIF: return "EXTCORPDIF";
            case HEMODIFF: return "HEMODIFF";
            case TRNSDERMD: return "TRNSDERMD";
            case _DISSOLVE: return "_Dissolve";
            case DISSOLVE: return "DISSOLVE";
            case SL: return "SL";
            case _DOUCHE: return "_Douche";
            case DOUCHE: return "DOUCHE";
            case _ELECTROOSMOSISROUTE: return "_ElectroOsmosisRoute";
            case ELECTOSMOS: return "ELECTOSMOS";
            case _ENEMA: return "_Enema";
            case ENEMA: return "ENEMA";
            case RETENEMA: return "RETENEMA";
            case _FLUSH: return "_Flush";
            case IVFLUSH: return "IVFLUSH";
            case _IMPLANTATION: return "_Implantation";
            case IDIMPLNT: return "IDIMPLNT";
            case IVITIMPLNT: return "IVITIMPLNT";
            case SQIMPLNT: return "SQIMPLNT";
            case _INFUSION: return "_Infusion";
            case EPI: return "EPI";
            case IA: return "IA";
            case IC: return "IC";
            case ICOR: return "ICOR";
            case IOSSC: return "IOSSC";
            case IT: return "IT";
            case IV: return "IV";
            case IVC: return "IVC";
            case IVCC: return "IVCC";
            case IVCI: return "IVCI";
            case PCA: return "PCA";
            case IVASCINFUS: return "IVASCINFUS";
            case SQINFUS: return "SQINFUS";
            case _INHALATION: return "_Inhalation";
            case IPINHL: return "IPINHL";
            case ORIFINHL: return "ORIFINHL";
            case REBREATH: return "REBREATH";
            case IPPB: return "IPPB";
            case NASINHL: return "NASINHL";
            case NASINHLC: return "NASINHLC";
            case NEB: return "NEB";
            case NASNEB: return "NASNEB";
            case ORNEB: return "ORNEB";
            case TRACH: return "TRACH";
            case VENT: return "VENT";
            case VENTMASK: return "VENTMASK";
            case _INJECTION: return "_Injection";
            case AMNINJ: return "AMNINJ";
            case BILINJ: return "BILINJ";
            case CHOLINJ: return "CHOLINJ";
            case CERVINJ: return "CERVINJ";
            case EPIDURINJ: return "EPIDURINJ";
            case EPIINJ: return "EPIINJ";
            case EPINJSP: return "EPINJSP";
            case EXTRAMNINJ: return "EXTRAMNINJ";
            case EXTCORPINJ: return "EXTCORPINJ";
            case GBINJ: return "GBINJ";
            case GINGINJ: return "GINGINJ";
            case BLADINJ: return "BLADINJ";
            case ENDOSININJ: return "ENDOSININJ";
            case HEMOPORT: return "HEMOPORT";
            case IABDINJ: return "IABDINJ";
            case IAINJ: return "IAINJ";
            case IAINJP: return "IAINJP";
            case IAINJSP: return "IAINJSP";
            case IARTINJ: return "IARTINJ";
            case IBURSINJ: return "IBURSINJ";
            case ICARDINJ: return "ICARDINJ";
            case ICARDINJRP: return "ICARDINJRP";
            case ICARDINJSP: return "ICARDINJSP";
            case ICARINJP: return "ICARINJP";
            case ICARTINJ: return "ICARTINJ";
            case ICAUDINJ: return "ICAUDINJ";
            case ICAVINJ: return "ICAVINJ";
            case ICAVITINJ: return "ICAVITINJ";
            case ICEREBINJ: return "ICEREBINJ";
            case ICISTERNINJ: return "ICISTERNINJ";
            case ICORONINJ: return "ICORONINJ";
            case ICORONINJP: return "ICORONINJP";
            case ICORPCAVINJ: return "ICORPCAVINJ";
            case IDINJ: return "IDINJ";
            case IDISCINJ: return "IDISCINJ";
            case IDUCTINJ: return "IDUCTINJ";
            case IDURINJ: return "IDURINJ";
            case IEPIDINJ: return "IEPIDINJ";
            case IEPITHINJ: return "IEPITHINJ";
            case ILESINJ: return "ILESINJ";
            case ILUMINJ: return "ILUMINJ";
            case ILYMPJINJ: return "ILYMPJINJ";
            case IM: return "IM";
            case IMD: return "IMD";
            case IMZ: return "IMZ";
            case IMEDULINJ: return "IMEDULINJ";
            case INTERMENINJ: return "INTERMENINJ";
            case INTERSTITINJ: return "INTERSTITINJ";
            case IOINJ: return "IOINJ";
            case IOSSINJ: return "IOSSINJ";
            case IOVARINJ: return "IOVARINJ";
            case IPCARDINJ: return "IPCARDINJ";
            case IPERINJ: return "IPERINJ";
            case IPINJ: return "IPINJ";
            case IPLRINJ: return "IPLRINJ";
            case IPROSTINJ: return "IPROSTINJ";
            case IPUMPINJ: return "IPUMPINJ";
            case ISINJ: return "ISINJ";
            case ISTERINJ: return "ISTERINJ";
            case ISYNINJ: return "ISYNINJ";
            case ITENDINJ: return "ITENDINJ";
            case ITESTINJ: return "ITESTINJ";
            case ITHORINJ: return "ITHORINJ";
            case ITINJ: return "ITINJ";
            case ITUBINJ: return "ITUBINJ";
            case ITUMINJ: return "ITUMINJ";
            case ITYMPINJ: return "ITYMPINJ";
            case IUINJ: return "IUINJ";
            case IUINJC: return "IUINJC";
            case IURETINJ: return "IURETINJ";
            case IVASCINJ: return "IVASCINJ";
            case IVENTINJ: return "IVENTINJ";
            case IVESINJ: return "IVESINJ";
            case IVINJ: return "IVINJ";
            case IVINJBOL: return "IVINJBOL";
            case IVPUSH: return "IVPUSH";
            case IVRPUSH: return "IVRPUSH";
            case IVSPUSH: return "IVSPUSH";
            case IVITINJ: return "IVITINJ";
            case PAINJ: return "PAINJ";
            case PARENTINJ: return "PARENTINJ";
            case PDONTINJ: return "PDONTINJ";
            case PDPINJ: return "PDPINJ";
            case PDURINJ: return "PDURINJ";
            case PNINJ: return "PNINJ";
            case PNSINJ: return "PNSINJ";
            case RBINJ: return "RBINJ";
            case SCINJ: return "SCINJ";
            case SLESINJ: return "SLESINJ";
            case SOFTISINJ: return "SOFTISINJ";
            case SQ: return "SQ";
            case SUBARACHINJ: return "SUBARACHINJ";
            case SUBMUCINJ: return "SUBMUCINJ";
            case TRPLACINJ: return "TRPLACINJ";
            case TRTRACHINJ: return "TRTRACHINJ";
            case URETHINJ: return "URETHINJ";
            case URETINJ: return "URETINJ";
            case _INSERTION: return "_Insertion";
            case CERVINS: return "CERVINS";
            case IOSURGINS: return "IOSURGINS";
            case IU: return "IU";
            case LPINS: return "LPINS";
            case PR: return "PR";
            case SQSURGINS: return "SQSURGINS";
            case URETHINS: return "URETHINS";
            case VAGINSI: return "VAGINSI";
            case _INSTILLATION: return "_Instillation";
            case CECINSTL: return "CECINSTL";
            case EFT: return "EFT";
            case ENTINSTL: return "ENTINSTL";
            case GT: return "GT";
            case NGT: return "NGT";
            case OGT: return "OGT";
            case BLADINSTL: return "BLADINSTL";
            case CAPDINSTL: return "CAPDINSTL";
            case CTINSTL: return "CTINSTL";
            case ETINSTL: return "ETINSTL";
            case GJT: return "GJT";
            case IBRONCHINSTIL: return "IBRONCHINSTIL";
            case IDUODINSTIL: return "IDUODINSTIL";
            case IESOPHINSTIL: return "IESOPHINSTIL";
            case IGASTINSTIL: return "IGASTINSTIL";
            case IILEALINJ: return "IILEALINJ";
            case IOINSTL: return "IOINSTL";
            case ISININSTIL: return "ISININSTIL";
            case ITRACHINSTIL: return "ITRACHINSTIL";
            case IUINSTL: return "IUINSTL";
            case JJTINSTL: return "JJTINSTL";
            case LARYNGINSTIL: return "LARYNGINSTIL";
            case NASALINSTIL: return "NASALINSTIL";
            case NASOGASINSTIL: return "NASOGASINSTIL";
            case NTT: return "NTT";
            case OJJ: return "OJJ";
            case OT: return "OT";
            case PDPINSTL: return "PDPINSTL";
            case PNSINSTL: return "PNSINSTL";
            case RECINSTL: return "RECINSTL";
            case RECTINSTL: return "RECTINSTL";
            case SININSTIL: return "SININSTIL";
            case SOFTISINSTIL: return "SOFTISINSTIL";
            case TRACHINSTL: return "TRACHINSTL";
            case TRTYMPINSTIL: return "TRTYMPINSTIL";
            case URETHINSTL: return "URETHINSTL";
            case _IONTOPHORESISROUTE: return "_IontophoresisRoute";
            case IONTO: return "IONTO";
            case _IRRIGATION: return "_Irrigation";
            case GUIRR: return "GUIRR";
            case IGASTIRR: return "IGASTIRR";
            case ILESIRR: return "ILESIRR";
            case IOIRR: return "IOIRR";
            case BLADIRR: return "BLADIRR";
            case BLADIRRC: return "BLADIRRC";
            case BLADIRRT: return "BLADIRRT";
            case RECIRR: return "RECIRR";
            case _LAVAGEROUTE: return "_LavageRoute";
            case IGASTLAV: return "IGASTLAV";
            case _MUCOSALABSORPTIONROUTE: return "_MucosalAbsorptionRoute";
            case IDOUDMAB: return "IDOUDMAB";
            case ITRACHMAB: return "ITRACHMAB";
            case SMUCMAB: return "SMUCMAB";
            case _NEBULIZATION: return "_Nebulization";
            case ETNEB: return "ETNEB";
            case _RINSE: return "_Rinse";
            case DENRINSE: return "DENRINSE";
            case ORRINSE: return "ORRINSE";
            case _SUPPOSITORYROUTE: return "_SuppositoryRoute";
            case URETHSUP: return "URETHSUP";
            case _SWISH: return "_Swish";
            case SWISHSPIT: return "SWISHSPIT";
            case SWISHSWAL: return "SWISHSWAL";
            case _TOPICALABSORPTIONROUTE: return "_TopicalAbsorptionRoute";
            case TTYMPTABSORP: return "TTYMPTABSORP";
            case _TOPICALAPPLICATION: return "_TopicalApplication";
            case DRESS: return "DRESS";
            case SWAB: return "SWAB";
            case TOPICAL: return "TOPICAL";
            case BUC: return "BUC";
            case CERV: return "CERV";
            case DEN: return "DEN";
            case GIN: return "GIN";
            case HAIR: return "HAIR";
            case ICORNTA: return "ICORNTA";
            case ICORONTA: return "ICORONTA";
            case IESOPHTA: return "IESOPHTA";
            case IILEALTA: return "IILEALTA";
            case ILTOP: return "ILTOP";
            case ILUMTA: return "ILUMTA";
            case IOTOP: return "IOTOP";
            case LARYNGTA: return "LARYNGTA";
            case MUC: return "MUC";
            case NAIL: return "NAIL";
            case NASAL: return "NASAL";
            case OPTHALTA: return "OPTHALTA";
            case ORALTA: return "ORALTA";
            case ORMUC: return "ORMUC";
            case OROPHARTA: return "OROPHARTA";
            case PERIANAL: return "PERIANAL";
            case PERINEAL: return "PERINEAL";
            case PDONTTA: return "PDONTTA";
            case RECTAL: return "RECTAL";
            case SCALP: return "SCALP";
            case OCDRESTA: return "OCDRESTA";
            case SKIN: return "SKIN";
            case SUBCONJTA: return "SUBCONJTA";
            case TMUCTA: return "TMUCTA";
            case VAGINS: return "VAGINS";
            case INSUF: return "INSUF";
            case TRNSDERM: return "TRNSDERM";
            case _ROUTEBYSITE: return "_RouteBySite";
            case _AMNIOTICFLUIDSACROUTE: return "_AmnioticFluidSacRoute";
            case _BILIARYROUTE: return "_BiliaryRoute";
            case _BODYSURFACEROUTE: return "_BodySurfaceRoute";
            case _BUCCALMUCOSAROUTE: return "_BuccalMucosaRoute";
            case _CECOSTOMYROUTE: return "_CecostomyRoute";
            case _CERVICALROUTE: return "_CervicalRoute";
            case _ENDOCERVICALROUTE: return "_EndocervicalRoute";
            case _ENTERALROUTE: return "_EnteralRoute";
            case _EPIDURALROUTE: return "_EpiduralRoute";
            case _EXTRAAMNIOTICROUTE: return "_ExtraAmnioticRoute";
            case _EXTRACORPOREALCIRCULATIONROUTE: return "_ExtracorporealCirculationRoute";
            case _GASTRICROUTE: return "_GastricRoute";
            case _GENITOURINARYROUTE: return "_GenitourinaryRoute";
            case _GINGIVALROUTE: return "_GingivalRoute";
            case _HAIRROUTE: return "_HairRoute";
            case _INTERAMENINGEALROUTE: return "_InterameningealRoute";
            case _INTERSTITIALROUTE: return "_InterstitialRoute";
            case _INTRAABDOMINALROUTE: return "_IntraabdominalRoute";
            case _INTRAARTERIALROUTE: return "_IntraarterialRoute";
            case _INTRAARTICULARROUTE: return "_IntraarticularRoute";
            case _INTRABRONCHIALROUTE: return "_IntrabronchialRoute";
            case _INTRABURSALROUTE: return "_IntrabursalRoute";
            case _INTRACARDIACROUTE: return "_IntracardiacRoute";
            case _INTRACARTILAGINOUSROUTE: return "_IntracartilaginousRoute";
            case _INTRACAUDALROUTE: return "_IntracaudalRoute";
            case _INTRACAVERNOSALROUTE: return "_IntracavernosalRoute";
            case _INTRACAVITARYROUTE: return "_IntracavitaryRoute";
            case _INTRACEREBRALROUTE: return "_IntracerebralRoute";
            case _INTRACERVICALROUTE: return "_IntracervicalRoute";
            case _INTRACISTERNALROUTE: return "_IntracisternalRoute";
            case _INTRACORNEALROUTE: return "_IntracornealRoute";
            case _INTRACORONALROUTE: return "_IntracoronalRoute";
            case _INTRACORONARYROUTE: return "_IntracoronaryRoute";
            case _INTRACORPUSCAVERNOSUMROUTE: return "_IntracorpusCavernosumRoute";
            case _INTRADERMALROUTE: return "_IntradermalRoute";
            case _INTRADISCALROUTE: return "_IntradiscalRoute";
            case _INTRADUCTALROUTE: return "_IntraductalRoute";
            case _INTRADUODENALROUTE: return "_IntraduodenalRoute";
            case _INTRADURALROUTE: return "_IntraduralRoute";
            case _INTRAEPIDERMALROUTE: return "_IntraepidermalRoute";
            case _INTRAEPITHELIALROUTE: return "_IntraepithelialRoute";
            case _INTRAESOPHAGEALROUTE: return "_IntraesophagealRoute";
            case _INTRAGASTRICROUTE: return "_IntragastricRoute";
            case _INTRAILEALROUTE: return "_IntrailealRoute";
            case _INTRALESIONALROUTE: return "_IntralesionalRoute";
            case _INTRALUMINALROUTE: return "_IntraluminalRoute";
            case _INTRALYMPHATICROUTE: return "_IntralymphaticRoute";
            case _INTRAMEDULLARYROUTE: return "_IntramedullaryRoute";
            case _INTRAMUSCULARROUTE: return "_IntramuscularRoute";
            case _INTRAOCULARROUTE: return "_IntraocularRoute";
            case _INTRAOSSEOUSROUTE: return "_IntraosseousRoute";
            case _INTRAOVARIANROUTE: return "_IntraovarianRoute";
            case _INTRAPERICARDIALROUTE: return "_IntrapericardialRoute";
            case _INTRAPERITONEALROUTE: return "_IntraperitonealRoute";
            case _INTRAPLEURALROUTE: return "_IntrapleuralRoute";
            case _INTRAPROSTATICROUTE: return "_IntraprostaticRoute";
            case _INTRAPULMONARYROUTE: return "_IntrapulmonaryRoute";
            case _INTRASINALROUTE: return "_IntrasinalRoute";
            case _INTRASPINALROUTE: return "_IntraspinalRoute";
            case _INTRASTERNALROUTE: return "_IntrasternalRoute";
            case _INTRASYNOVIALROUTE: return "_IntrasynovialRoute";
            case _INTRATENDINOUSROUTE: return "_IntratendinousRoute";
            case _INTRATESTICULARROUTE: return "_IntratesticularRoute";
            case _INTRATHECALROUTE: return "_IntrathecalRoute";
            case _INTRATHORACICROUTE: return "_IntrathoracicRoute";
            case _INTRATRACHEALROUTE: return "_IntratrachealRoute";
            case _INTRATUBULARROUTE: return "_IntratubularRoute";
            case _INTRATUMORROUTE: return "_IntratumorRoute";
            case _INTRATYMPANICROUTE: return "_IntratympanicRoute";
            case _INTRAUTERINEROUTE: return "_IntrauterineRoute";
            case _INTRAVASCULARROUTE: return "_IntravascularRoute";
            case _INTRAVENOUSROUTE: return "_IntravenousRoute";
            case _INTRAVENTRICULARROUTE: return "_IntraventricularRoute";
            case _INTRAVESICLEROUTE: return "_IntravesicleRoute";
            case _INTRAVITREALROUTE: return "_IntravitrealRoute";
            case _JEJUNUMROUTE: return "_JejunumRoute";
            case _LACRIMALPUNCTAROUTE: return "_LacrimalPunctaRoute";
            case _LARYNGEALROUTE: return "_LaryngealRoute";
            case _LINGUALROUTE: return "_LingualRoute";
            case _MUCOUSMEMBRANEROUTE: return "_MucousMembraneRoute";
            case _NAILROUTE: return "_NailRoute";
            case _NASALROUTE: return "_NasalRoute";
            case _OPHTHALMICROUTE: return "_OphthalmicRoute";
            case _ORALROUTE: return "_OralRoute";
            case _OROMUCOSALROUTE: return "_OromucosalRoute";
            case _OROPHARYNGEALROUTE: return "_OropharyngealRoute";
            case _OTICROUTE: return "_OticRoute";
            case _PARANASALSINUSESROUTE: return "_ParanasalSinusesRoute";
            case _PARENTERALROUTE: return "_ParenteralRoute";
            case _PERIANALROUTE: return "_PerianalRoute";
            case _PERIARTICULARROUTE: return "_PeriarticularRoute";
            case _PERIDURALROUTE: return "_PeriduralRoute";
            case _PERINEALROUTE: return "_PerinealRoute";
            case _PERINEURALROUTE: return "_PerineuralRoute";
            case _PERIODONTALROUTE: return "_PeriodontalRoute";
            case _PULMONARYROUTE: return "_PulmonaryRoute";
            case _RECTALROUTE: return "_RectalRoute";
            case _RESPIRATORYTRACTROUTE: return "_RespiratoryTractRoute";
            case _RETROBULBARROUTE: return "_RetrobulbarRoute";
            case _SCALPROUTE: return "_ScalpRoute";
            case _SINUSUNSPECIFIEDROUTE: return "_SinusUnspecifiedRoute";
            case _SKINROUTE: return "_SkinRoute";
            case _SOFTTISSUEROUTE: return "_SoftTissueRoute";
            case _SUBARACHNOIDROUTE: return "_SubarachnoidRoute";
            case _SUBCONJUNCTIVALROUTE: return "_SubconjunctivalRoute";
            case _SUBCUTANEOUSROUTE: return "_SubcutaneousRoute";
            case _SUBLESIONALROUTE: return "_SublesionalRoute";
            case _SUBLINGUALROUTE: return "_SublingualRoute";
            case _SUBMUCOSALROUTE: return "_SubmucosalRoute";
            case _TRACHEOSTOMYROUTE: return "_TracheostomyRoute";
            case _TRANSMUCOSALROUTE: return "_TransmucosalRoute";
            case _TRANSPLACENTALROUTE: return "_TransplacentalRoute";
            case _TRANSTRACHEALROUTE: return "_TranstrachealRoute";
            case _TRANSTYMPANICROUTE: return "_TranstympanicRoute";
            case _URETERALROUTE: return "_UreteralRoute";
            case _URETHRALROUTE: return "_UrethralRoute";
            case _URINARYBLADDERROUTE: return "_UrinaryBladderRoute";
            case _URINARYTRACTROUTE: return "_UrinaryTractRoute";
            case _VAGINALROUTE: return "_VaginalRoute";
            case _VITREOUSHUMOURROUTE: return "_VitreousHumourRoute";
            default: return "?";
          }
        }
        public String getSystem() {
          return "http://hl7.org/fhir/v3/RouteOfAdministration";
        }
        public String getDefinition() {
          switch (this) {
            case _ROUTEBYMETHOD: return "Route of substance administration classified by administration method.";
            case SOAK: return "Immersion (soak)";
            case SHAMPOO: return "Shampoo";
            case TRNSLING: return "Translingual";
            case PO: return "Swallow, oral";
            case GARGLE: return "Gargle";
            case SUCK: return "Suck, oromucosal";
            case _CHEW: return "Chew";
            case CHEW: return "Chew, oral";
            case _DIFFUSION: return "Diffusion";
            case EXTCORPDIF: return "Diffusion, extracorporeal";
            case HEMODIFF: return "Diffusion, hemodialysis";
            case TRNSDERMD: return "Diffusion, transdermal";
            case _DISSOLVE: return "Dissolve";
            case DISSOLVE: return "Dissolve, oral";
            case SL: return "Dissolve, sublingual";
            case _DOUCHE: return "Douche";
            case DOUCHE: return "Douche, vaginal";
            case _ELECTROOSMOSISROUTE: return "Electro-osmosis";
            case ELECTOSMOS: return "Electro-osmosis";
            case _ENEMA: return "Enema";
            case ENEMA: return "Enema, rectal";
            case RETENEMA: return "Enema, rectal retention";
            case _FLUSH: return "Flush";
            case IVFLUSH: return "Flush, intravenous catheter";
            case _IMPLANTATION: return "Implantation";
            case IDIMPLNT: return "Implantation, intradermal";
            case IVITIMPLNT: return "Implantation, intravitreal";
            case SQIMPLNT: return "Implantation, subcutaneous";
            case _INFUSION: return "Infusion";
            case EPI: return "Infusion, epidural";
            case IA: return "Infusion, intraarterial catheter";
            case IC: return "Infusion, intracardiac";
            case ICOR: return "Infusion, intracoronary";
            case IOSSC: return "Infusion, intraosseous, continuous";
            case IT: return "Infusion, intrathecal";
            case IV: return "Infusion, intravenous";
            case IVC: return "Infusion, intravenous catheter";
            case IVCC: return "Infusion, intravenous catheter, continuous";
            case IVCI: return "Infusion, intravenous catheter, intermittent";
            case PCA: return "Infusion, intravenous catheter, pca pump";
            case IVASCINFUS: return "Infusion, intravascular";
            case SQINFUS: return "Infusion, subcutaneous";
            case _INHALATION: return "Inhalation";
            case IPINHL: return "Inhalation, oral";
            case ORIFINHL: return "Inhalation, oral intermittent flow";
            case REBREATH: return "Inhalation, oral rebreather mask";
            case IPPB: return "Inhalation, intermittent positive pressure breathing (ippb)";
            case NASINHL: return "Inhalation, nasal";
            case NASINHLC: return "Inhalation, nasal, prongs";
            case NEB: return "Inhalation, nebulization";
            case NASNEB: return "Inhalation, nebulization, nasal";
            case ORNEB: return "Inhalation, nebulization, oral";
            case TRACH: return "Inhalation, tracheostomy";
            case VENT: return "Inhalation, ventilator";
            case VENTMASK: return "Inhalation, ventimask";
            case _INJECTION: return "Injection";
            case AMNINJ: return "Injection, amniotic fluid";
            case BILINJ: return "Injection, biliary tract";
            case CHOLINJ: return "Injection, for cholangiography";
            case CERVINJ: return "Injection, cervical";
            case EPIDURINJ: return "Injection, epidural";
            case EPIINJ: return "Injection, epidural, push";
            case EPINJSP: return "Injection, epidural, slow push";
            case EXTRAMNINJ: return "Injection, extra-amniotic";
            case EXTCORPINJ: return "Injection, extracorporeal";
            case GBINJ: return "Injection, gastric button";
            case GINGINJ: return "Injection, gingival";
            case BLADINJ: return "Injection, urinary bladder";
            case ENDOSININJ: return "Injection, endosinusial";
            case HEMOPORT: return "Injection, hemodialysis port";
            case IABDINJ: return "Injection, intra-abdominal";
            case IAINJ: return "Injection, intraarterial";
            case IAINJP: return "Injection, intraarterial, push";
            case IAINJSP: return "Injection, intraarterial, slow push";
            case IARTINJ: return "Injection, intraarticular";
            case IBURSINJ: return "Injection, intrabursal";
            case ICARDINJ: return "Injection, intracardiac";
            case ICARDINJRP: return "Injection, intracardiac, rapid push";
            case ICARDINJSP: return "Injection, intracardiac, slow push";
            case ICARINJP: return "Injection, intracardiac, push";
            case ICARTINJ: return "Injection, intracartilaginous";
            case ICAUDINJ: return "Injection, intracaudal";
            case ICAVINJ: return "Injection, intracavernous";
            case ICAVITINJ: return "Injection, intracavitary";
            case ICEREBINJ: return "Injection, intracerebral";
            case ICISTERNINJ: return "Injection, intracisternal";
            case ICORONINJ: return "Injection, intracoronary";
            case ICORONINJP: return "Injection, intracoronary, push";
            case ICORPCAVINJ: return "Injection, intracorpus cavernosum";
            case IDINJ: return "Injection, intradermal";
            case IDISCINJ: return "Injection, intradiscal";
            case IDUCTINJ: return "Injection, intraductal";
            case IDURINJ: return "Injection, intradural";
            case IEPIDINJ: return "Injection, intraepidermal";
            case IEPITHINJ: return "Injection, intraepithelial";
            case ILESINJ: return "Injection, intralesional";
            case ILUMINJ: return "Injection, intraluminal";
            case ILYMPJINJ: return "Injection, intralymphatic";
            case IM: return "Injection, intramuscular";
            case IMD: return "Injection, intramuscular, deep";
            case IMZ: return "Injection, intramuscular, z track";
            case IMEDULINJ: return "Injection, intramedullary";
            case INTERMENINJ: return "Injection, interameningeal";
            case INTERSTITINJ: return "Injection, interstitial";
            case IOINJ: return "Injection, intraocular";
            case IOSSINJ: return "Injection, intraosseous";
            case IOVARINJ: return "Injection, intraovarian";
            case IPCARDINJ: return "Injection, intrapericardial";
            case IPERINJ: return "Injection, intraperitoneal";
            case IPINJ: return "Injection, intrapulmonary";
            case IPLRINJ: return "Injection, intrapleural";
            case IPROSTINJ: return "Injection, intraprostatic";
            case IPUMPINJ: return "Injection, insulin pump";
            case ISINJ: return "Injection, intraspinal";
            case ISTERINJ: return "Injection, intrasternal";
            case ISYNINJ: return "Injection, intrasynovial";
            case ITENDINJ: return "Injection, intratendinous";
            case ITESTINJ: return "Injection, intratesticular";
            case ITHORINJ: return "Injection, intrathoracic";
            case ITINJ: return "Injection, intrathecal";
            case ITUBINJ: return "Injection, intratubular";
            case ITUMINJ: return "Injection, intratumor";
            case ITYMPINJ: return "Injection, intratympanic";
            case IUINJ: return "Injection, intracervical (uterus)";
            case IUINJC: return "Injection, intracervical (uterus)";
            case IURETINJ: return "Injection, intraureteral, retrograde";
            case IVASCINJ: return "Injection, intravascular";
            case IVENTINJ: return "Injection, intraventricular (heart)";
            case IVESINJ: return "Injection, intravesicle";
            case IVINJ: return "Injection, intravenous";
            case IVINJBOL: return "Injection, intravenous, bolus";
            case IVPUSH: return "Injection, intravenous, push";
            case IVRPUSH: return "Injection, intravenous, rapid push";
            case IVSPUSH: return "Injection, intravenous, slow push";
            case IVITINJ: return "Injection, intravitreal";
            case PAINJ: return "Injection, periarticular";
            case PARENTINJ: return "Injection, parenteral";
            case PDONTINJ: return "Injection, periodontal";
            case PDPINJ: return "Injection, peritoneal dialysis port";
            case PDURINJ: return "Injection, peridural";
            case PNINJ: return "Injection, perineural";
            case PNSINJ: return "Injection, paranasal sinuses";
            case RBINJ: return "Injection, retrobulbar";
            case SCINJ: return "Injection, subconjunctival";
            case SLESINJ: return "Injection, sublesional";
            case SOFTISINJ: return "Injection, soft tissue";
            case SQ: return "Injection, subcutaneous";
            case SUBARACHINJ: return "Injection, subarachnoid";
            case SUBMUCINJ: return "Injection, submucosal";
            case TRPLACINJ: return "Injection, transplacental";
            case TRTRACHINJ: return "Injection, transtracheal";
            case URETHINJ: return "Injection, urethral";
            case URETINJ: return "Injection, ureteral";
            case _INSERTION: return "Insertion";
            case CERVINS: return "Insertion, cervical (uterine)";
            case IOSURGINS: return "Insertion, intraocular, surgical";
            case IU: return "Insertion, intrauterine";
            case LPINS: return "Insertion, lacrimal puncta";
            case PR: return "Insertion, rectal";
            case SQSURGINS: return "Insertion, subcutaneous, surgical";
            case URETHINS: return "Insertion, urethral";
            case VAGINSI: return "Insertion, vaginal";
            case _INSTILLATION: return "Instillation";
            case CECINSTL: return "Instillation, cecostomy";
            case EFT: return "Instillation, enteral feeding tube";
            case ENTINSTL: return "Instillation, enteral";
            case GT: return "Instillation, gastrostomy tube";
            case NGT: return "Instillation, nasogastric tube";
            case OGT: return "Instillation, orogastric tube";
            case BLADINSTL: return "Instillation, urinary catheter";
            case CAPDINSTL: return "Instillation, continuous ambulatory peritoneal dialysis port";
            case CTINSTL: return "Instillation, chest tube";
            case ETINSTL: return "Instillation, endotracheal tube";
            case GJT: return "Instillation, gastro-jejunostomy tube";
            case IBRONCHINSTIL: return "Instillation, intrabronchial";
            case IDUODINSTIL: return "Instillation, intraduodenal";
            case IESOPHINSTIL: return "Instillation, intraesophageal";
            case IGASTINSTIL: return "Instillation, intragastric";
            case IILEALINJ: return "Instillation, intraileal";
            case IOINSTL: return "Instillation, intraocular";
            case ISININSTIL: return "Instillation, intrasinal";
            case ITRACHINSTIL: return "Instillation, intratracheal";
            case IUINSTL: return "Instillation, intrauterine";
            case JJTINSTL: return "Instillation, jejunostomy tube";
            case LARYNGINSTIL: return "Instillation, laryngeal";
            case NASALINSTIL: return "Instillation, nasal";
            case NASOGASINSTIL: return "Instillation, nasogastric";
            case NTT: return "Instillation, nasotracheal tube";
            case OJJ: return "Instillation, orojejunum tube";
            case OT: return "Instillation, otic";
            case PDPINSTL: return "Instillation, peritoneal dialysis port";
            case PNSINSTL: return "Instillation, paranasal sinuses";
            case RECINSTL: return "Instillation, rectal";
            case RECTINSTL: return "Instillation, rectal tube";
            case SININSTIL: return "Instillation, sinus, unspecified";
            case SOFTISINSTIL: return "Instillation, soft tissue";
            case TRACHINSTL: return "Instillation, tracheostomy";
            case TRTYMPINSTIL: return "Instillation, transtympanic";
            case URETHINSTL: return "Instillation, urethral";
            case _IONTOPHORESISROUTE: return "Iontophoresis";
            case IONTO: return "Topical application, iontophoresis";
            case _IRRIGATION: return "Irrigation";
            case GUIRR: return "Irrigation, genitourinary";
            case IGASTIRR: return "Irrigation, intragastric";
            case ILESIRR: return "Irrigation, intralesional";
            case IOIRR: return "Irrigation, intraocular";
            case BLADIRR: return "Irrigation, urinary bladder";
            case BLADIRRC: return "Irrigation, urinary bladder, continuous";
            case BLADIRRT: return "Irrigation, urinary bladder, tidal";
            case RECIRR: return "Irrigation, rectal";
            case _LAVAGEROUTE: return "Lavage";
            case IGASTLAV: return "Lavage, intragastric";
            case _MUCOSALABSORPTIONROUTE: return "Mucosal absorption";
            case IDOUDMAB: return "Mucosal absorption, intraduodenal";
            case ITRACHMAB: return "Mucosal absorption, intratracheal";
            case SMUCMAB: return "Mucosal absorption, submucosal";
            case _NEBULIZATION: return "Nebulization";
            case ETNEB: return "Nebulization, endotracheal tube";
            case _RINSE: return "Rinse";
            case DENRINSE: return "Rinse, dental";
            case ORRINSE: return "Rinse, oral";
            case _SUPPOSITORYROUTE: return "Suppository";
            case URETHSUP: return "Suppository, urethral";
            case _SWISH: return "Swish";
            case SWISHSPIT: return "Swish and spit out, oromucosal";
            case SWISHSWAL: return "Swish and swallow, oromucosal";
            case _TOPICALABSORPTIONROUTE: return "Topical absorption";
            case TTYMPTABSORP: return "Topical absorption, transtympanic";
            case _TOPICALAPPLICATION: return "Topical application";
            case DRESS: return "Topical application, soaked dressing";
            case SWAB: return "Topical application, swab";
            case TOPICAL: return "Topical";
            case BUC: return "Topical application, buccal";
            case CERV: return "Topical application, cervical";
            case DEN: return "Topical application, dental";
            case GIN: return "Topical application, gingival";
            case HAIR: return "Topical application, hair";
            case ICORNTA: return "Topical application, intracorneal";
            case ICORONTA: return "Topical application, intracoronal (dental)";
            case IESOPHTA: return "Topical application, intraesophageal";
            case IILEALTA: return "Topical application, intraileal";
            case ILTOP: return "Topical application, intralesional";
            case ILUMTA: return "Topical application, intraluminal";
            case IOTOP: return "Topical application, intraocular";
            case LARYNGTA: return "Topical application, laryngeal";
            case MUC: return "Topical application, mucous membrane";
            case NAIL: return "Topical application, nail";
            case NASAL: return "Topical application, nasal";
            case OPTHALTA: return "Topical application, ophthalmic";
            case ORALTA: return "Topical application, oral";
            case ORMUC: return "Topical application, oromucosal";
            case OROPHARTA: return "Topical application, oropharyngeal";
            case PERIANAL: return "Topical application, perianal";
            case PERINEAL: return "Topical application, perineal";
            case PDONTTA: return "Topical application, periodontal";
            case RECTAL: return "Topical application, rectal";
            case SCALP: return "Topical application, scalp";
            case OCDRESTA: return "Occlusive dressing technique";
            case SKIN: return "Topical application, skin";
            case SUBCONJTA: return "Subconjunctival";
            case TMUCTA: return "Topical application, transmucosal";
            case VAGINS: return "Insertion, vaginal";
            case INSUF: return "Insufflation";
            case TRNSDERM: return "Transdermal";
            case _ROUTEBYSITE: return "Route of substance administration classified by site.";
            case _AMNIOTICFLUIDSACROUTE: return "Amniotic fluid sac";
            case _BILIARYROUTE: return "Biliary tract";
            case _BODYSURFACEROUTE: return "Body surface";
            case _BUCCALMUCOSAROUTE: return "Buccal mucosa";
            case _CECOSTOMYROUTE: return "Cecostomy";
            case _CERVICALROUTE: return "Cervix of the uterus";
            case _ENDOCERVICALROUTE: return "Endocervical";
            case _ENTERALROUTE: return "Enteral";
            case _EPIDURALROUTE: return "Epidural";
            case _EXTRAAMNIOTICROUTE: return "Extra-amniotic";
            case _EXTRACORPOREALCIRCULATIONROUTE: return "Extracorporeal circulation";
            case _GASTRICROUTE: return "Gastric";
            case _GENITOURINARYROUTE: return "Genitourinary";
            case _GINGIVALROUTE: return "Gingival";
            case _HAIRROUTE: return "Hair";
            case _INTERAMENINGEALROUTE: return "Interameningeal";
            case _INTERSTITIALROUTE: return "Interstitial";
            case _INTRAABDOMINALROUTE: return "Intra-abdominal";
            case _INTRAARTERIALROUTE: return "Intra-arterial";
            case _INTRAARTICULARROUTE: return "Intraarticular";
            case _INTRABRONCHIALROUTE: return "Intrabronchial";
            case _INTRABURSALROUTE: return "Intrabursal";
            case _INTRACARDIACROUTE: return "Intracardiac";
            case _INTRACARTILAGINOUSROUTE: return "Intracartilaginous";
            case _INTRACAUDALROUTE: return "Intracaudal";
            case _INTRACAVERNOSALROUTE: return "Intracavernosal";
            case _INTRACAVITARYROUTE: return "Intracavitary";
            case _INTRACEREBRALROUTE: return "Intracerebral";
            case _INTRACERVICALROUTE: return "Intracervical";
            case _INTRACISTERNALROUTE: return "Intracisternal";
            case _INTRACORNEALROUTE: return "Intracorneal";
            case _INTRACORONALROUTE: return "Intracoronal (dental)";
            case _INTRACORONARYROUTE: return "Intracoronary";
            case _INTRACORPUSCAVERNOSUMROUTE: return "Intracorpus cavernosum";
            case _INTRADERMALROUTE: return "Intradermal";
            case _INTRADISCALROUTE: return "Intradiscal";
            case _INTRADUCTALROUTE: return "Intraductal";
            case _INTRADUODENALROUTE: return "Intraduodenal";
            case _INTRADURALROUTE: return "Intradural";
            case _INTRAEPIDERMALROUTE: return "Intraepidermal";
            case _INTRAEPITHELIALROUTE: return "Intraepithelial";
            case _INTRAESOPHAGEALROUTE: return "Intraesophageal";
            case _INTRAGASTRICROUTE: return "Intragastric";
            case _INTRAILEALROUTE: return "Intraileal";
            case _INTRALESIONALROUTE: return "Intralesional";
            case _INTRALUMINALROUTE: return "Intraluminal";
            case _INTRALYMPHATICROUTE: return "Intralymphatic";
            case _INTRAMEDULLARYROUTE: return "Intramedullary";
            case _INTRAMUSCULARROUTE: return "Intramuscular";
            case _INTRAOCULARROUTE: return "Intraocular";
            case _INTRAOSSEOUSROUTE: return "Intraosseous";
            case _INTRAOVARIANROUTE: return "Intraovarian";
            case _INTRAPERICARDIALROUTE: return "Intrapericardial";
            case _INTRAPERITONEALROUTE: return "Intraperitoneal";
            case _INTRAPLEURALROUTE: return "Intrapleural";
            case _INTRAPROSTATICROUTE: return "Intraprostatic";
            case _INTRAPULMONARYROUTE: return "Intrapulmonary";
            case _INTRASINALROUTE: return "Intrasinal";
            case _INTRASPINALROUTE: return "Intraspinal";
            case _INTRASTERNALROUTE: return "Intrasternal";
            case _INTRASYNOVIALROUTE: return "Intrasynovial";
            case _INTRATENDINOUSROUTE: return "Intratendinous";
            case _INTRATESTICULARROUTE: return "Intratesticular";
            case _INTRATHECALROUTE: return "Intrathecal";
            case _INTRATHORACICROUTE: return "Intrathoracic";
            case _INTRATRACHEALROUTE: return "Intratracheal";
            case _INTRATUBULARROUTE: return "Intratubular";
            case _INTRATUMORROUTE: return "Intratumor";
            case _INTRATYMPANICROUTE: return "Intratympanic";
            case _INTRAUTERINEROUTE: return "Intrauterine";
            case _INTRAVASCULARROUTE: return "Intravascular";
            case _INTRAVENOUSROUTE: return "Intravenous";
            case _INTRAVENTRICULARROUTE: return "Intraventricular";
            case _INTRAVESICLEROUTE: return "Intravesicle";
            case _INTRAVITREALROUTE: return "Intravitreal";
            case _JEJUNUMROUTE: return "Jejunum";
            case _LACRIMALPUNCTAROUTE: return "Lacrimal puncta";
            case _LARYNGEALROUTE: return "Laryngeal";
            case _LINGUALROUTE: return "Lingual";
            case _MUCOUSMEMBRANEROUTE: return "Mucous membrane";
            case _NAILROUTE: return "Nail";
            case _NASALROUTE: return "Nasal";
            case _OPHTHALMICROUTE: return "Ophthalmic";
            case _ORALROUTE: return "Oral";
            case _OROMUCOSALROUTE: return "Oromucosal";
            case _OROPHARYNGEALROUTE: return "Oropharyngeal";
            case _OTICROUTE: return "Otic";
            case _PARANASALSINUSESROUTE: return "Paranasal sinuses";
            case _PARENTERALROUTE: return "Parenteral";
            case _PERIANALROUTE: return "Perianal";
            case _PERIARTICULARROUTE: return "Periarticular";
            case _PERIDURALROUTE: return "Peridural";
            case _PERINEALROUTE: return "Perineal";
            case _PERINEURALROUTE: return "Perineural";
            case _PERIODONTALROUTE: return "Periodontal";
            case _PULMONARYROUTE: return "Pulmonary";
            case _RECTALROUTE: return "Rectal";
            case _RESPIRATORYTRACTROUTE: return "Respiratory tract";
            case _RETROBULBARROUTE: return "Retrobulbar";
            case _SCALPROUTE: return "Scalp";
            case _SINUSUNSPECIFIEDROUTE: return "Sinus, unspecified";
            case _SKINROUTE: return "Skin";
            case _SOFTTISSUEROUTE: return "Soft tissue";
            case _SUBARACHNOIDROUTE: return "Subarachnoid";
            case _SUBCONJUNCTIVALROUTE: return "Subconjunctival";
            case _SUBCUTANEOUSROUTE: return "Subcutaneous";
            case _SUBLESIONALROUTE: return "Sublesional";
            case _SUBLINGUALROUTE: return "Sublingual";
            case _SUBMUCOSALROUTE: return "Submucosal";
            case _TRACHEOSTOMYROUTE: return "Tracheostomy";
            case _TRANSMUCOSALROUTE: return "Transmucosal";
            case _TRANSPLACENTALROUTE: return "Transplacental";
            case _TRANSTRACHEALROUTE: return "Transtracheal";
            case _TRANSTYMPANICROUTE: return "Transtympanic";
            case _URETERALROUTE: return "Ureteral";
            case _URETHRALROUTE: return "Urethral";
            case _URINARYBLADDERROUTE: return "Urinary bladder";
            case _URINARYTRACTROUTE: return "Urinary tract";
            case _VAGINALROUTE: return "Vaginal";
            case _VITREOUSHUMOURROUTE: return "Vitreous humour";
            default: return "?";
          }
        }
        public String getDisplay() {
          switch (this) {
            case _ROUTEBYMETHOD: return "RouteByMethod";
            case SOAK: return "Immersion (soak)";
            case SHAMPOO: return "Shampoo";
            case TRNSLING: return "Translingual";
            case PO: return "Swallow, oral";
            case GARGLE: return "Gargle";
            case SUCK: return "Suck, oromucosal";
            case _CHEW: return "Chew";
            case CHEW: return "Chew, oral";
            case _DIFFUSION: return "Diffusion";
            case EXTCORPDIF: return "Diffusion, extracorporeal";
            case HEMODIFF: return "Diffusion, hemodialysis";
            case TRNSDERMD: return "Diffusion, transdermal";
            case _DISSOLVE: return "Dissolve";
            case DISSOLVE: return "Dissolve, oral";
            case SL: return "Dissolve, sublingual";
            case _DOUCHE: return "Douche";
            case DOUCHE: return "Douche, vaginal";
            case _ELECTROOSMOSISROUTE: return "ElectroOsmosisRoute";
            case ELECTOSMOS: return "Electro-osmosis";
            case _ENEMA: return "Enema";
            case ENEMA: return "Enema, rectal";
            case RETENEMA: return "Enema, rectal retention";
            case _FLUSH: return "Flush";
            case IVFLUSH: return "Flush, intravenous catheter";
            case _IMPLANTATION: return "Implantation";
            case IDIMPLNT: return "Implantation, intradermal";
            case IVITIMPLNT: return "Implantation, intravitreal";
            case SQIMPLNT: return "Implantation, subcutaneous";
            case _INFUSION: return "Infusion";
            case EPI: return "Infusion, epidural";
            case IA: return "Infusion, intraarterial catheter";
            case IC: return "Infusion, intracardiac";
            case ICOR: return "Infusion, intracoronary";
            case IOSSC: return "Infusion, intraosseous, continuous";
            case IT: return "Infusion, intrathecal";
            case IV: return "Infusion, intravenous";
            case IVC: return "Infusion, intravenous catheter";
            case IVCC: return "Infusion, intravenous catheter, continuous";
            case IVCI: return "Infusion, intravenous catheter, intermittent";
            case PCA: return "Infusion, intravenous catheter, pca pump";
            case IVASCINFUS: return "Infusion, intravascular";
            case SQINFUS: return "Infusion, subcutaneous";
            case _INHALATION: return "Inhalation";
            case IPINHL: return "Inhalation, respiratory";
            case ORIFINHL: return "Inhalation, oral intermittent flow";
            case REBREATH: return "Inhalation, oral rebreather mask";
            case IPPB: return "Inhalation, intermittent positive pressure breathing (ippb)";
            case NASINHL: return "Inhalation, nasal";
            case NASINHLC: return "Inhalation, nasal cannula";
            case NEB: return "Inhalation, nebulization";
            case NASNEB: return "Inhalation, nebulization, nasal";
            case ORNEB: return "Inhalation, nebulization, oral";
            case TRACH: return "Inhalation, tracheostomy";
            case VENT: return "Inhalation, ventilator";
            case VENTMASK: return "Inhalation, ventimask";
            case _INJECTION: return "Injection";
            case AMNINJ: return "Injection, amniotic fluid";
            case BILINJ: return "Injection, biliary tract";
            case CHOLINJ: return "Injection, for cholangiography";
            case CERVINJ: return "Injection, cervical";
            case EPIDURINJ: return "Injection, epidural";
            case EPIINJ: return "Injection, epidural, push";
            case EPINJSP: return "Injection, epidural, slow push";
            case EXTRAMNINJ: return "Injection, extra-amniotic";
            case EXTCORPINJ: return "Injection, extracorporeal";
            case GBINJ: return "Injection, gastric button";
            case GINGINJ: return "Injection, gingival";
            case BLADINJ: return "Injection, urinary bladder";
            case ENDOSININJ: return "Injection, endosinusial";
            case HEMOPORT: return "Injection, hemodialysis port";
            case IABDINJ: return "Injection, intra-abdominal";
            case IAINJ: return "Injection, intraarterial";
            case IAINJP: return "Injection, intraarterial, push";
            case IAINJSP: return "Injection, intraarterial, slow push";
            case IARTINJ: return "Injection, intraarticular";
            case IBURSINJ: return "Injection, intrabursal";
            case ICARDINJ: return "Injection, intracardiac";
            case ICARDINJRP: return "Injection, intracardiac, rapid push";
            case ICARDINJSP: return "Injection, intracardiac, slow push";
            case ICARINJP: return "Injection, intracardiac, push";
            case ICARTINJ: return "Injection, intracartilaginous";
            case ICAUDINJ: return "Injection, intracaudal";
            case ICAVINJ: return "Injection, intracavernous";
            case ICAVITINJ: return "Injection, intracavitary";
            case ICEREBINJ: return "Injection, intracerebral";
            case ICISTERNINJ: return "Injection, intracisternal";
            case ICORONINJ: return "Injection, intracoronary";
            case ICORONINJP: return "Injection, intracoronary, push";
            case ICORPCAVINJ: return "Injection, intracorpus cavernosum";
            case IDINJ: return "Injection, intradermal";
            case IDISCINJ: return "Injection, intradiscal";
            case IDUCTINJ: return "Injection, intraductal";
            case IDURINJ: return "Injection, intradural";
            case IEPIDINJ: return "Injection, intraepidermal";
            case IEPITHINJ: return "Injection, intraepithelial";
            case ILESINJ: return "Injection, intralesional";
            case ILUMINJ: return "Injection, intraluminal";
            case ILYMPJINJ: return "Injection, intralymphatic";
            case IM: return "Injection, intramuscular";
            case IMD: return "Injection, intramuscular, deep";
            case IMZ: return "Injection, intramuscular, z track";
            case IMEDULINJ: return "Injection, intramedullary";
            case INTERMENINJ: return "Injection, interameningeal";
            case INTERSTITINJ: return "Injection, interstitial";
            case IOINJ: return "Injection, intraocular";
            case IOSSINJ: return "Injection, intraosseous";
            case IOVARINJ: return "Injection, intraovarian";
            case IPCARDINJ: return "Injection, intrapericardial";
            case IPERINJ: return "Injection, intraperitoneal";
            case IPINJ: return "Injection, intrapulmonary";
            case IPLRINJ: return "Injection, intrapleural";
            case IPROSTINJ: return "Injection, intraprostatic";
            case IPUMPINJ: return "Injection, insulin pump";
            case ISINJ: return "Injection, intraspinal";
            case ISTERINJ: return "Injection, intrasternal";
            case ISYNINJ: return "Injection, intrasynovial";
            case ITENDINJ: return "Injection, intratendinous";
            case ITESTINJ: return "Injection, intratesticular";
            case ITHORINJ: return "Injection, intrathoracic";
            case ITINJ: return "Injection, intrathecal";
            case ITUBINJ: return "Injection, intratubular";
            case ITUMINJ: return "Injection, intratumor";
            case ITYMPINJ: return "Injection, intratympanic";
            case IUINJ: return "Injection, intrauterine";
            case IUINJC: return "Injection, intracervical (uterus)";
            case IURETINJ: return "Injection, intraureteral, retrograde";
            case IVASCINJ: return "Injection, intravascular";
            case IVENTINJ: return "Injection, intraventricular (heart)";
            case IVESINJ: return "Injection, intravesicle";
            case IVINJ: return "Injection, intravenous";
            case IVINJBOL: return "Injection, intravenous, bolus";
            case IVPUSH: return "Injection, intravenous, push";
            case IVRPUSH: return "Injection, intravenous, rapid push";
            case IVSPUSH: return "Injection, intravenous, slow push";
            case IVITINJ: return "Injection, intravitreal";
            case PAINJ: return "Injection, periarticular";
            case PARENTINJ: return "Injection, parenteral";
            case PDONTINJ: return "Injection, periodontal";
            case PDPINJ: return "Injection, peritoneal dialysis port";
            case PDURINJ: return "Injection, peridural";
            case PNINJ: return "Injection, perineural";
            case PNSINJ: return "Injection, paranasal sinuses";
            case RBINJ: return "Injection, retrobulbar";
            case SCINJ: return "Injection, subconjunctival";
            case SLESINJ: return "Injection, sublesional";
            case SOFTISINJ: return "Injection, soft tissue";
            case SQ: return "Injection, subcutaneous";
            case SUBARACHINJ: return "Injection, subarachnoid";
            case SUBMUCINJ: return "Injection, submucosal";
            case TRPLACINJ: return "Injection, transplacental";
            case TRTRACHINJ: return "Injection, transtracheal";
            case URETHINJ: return "Injection, urethral";
            case URETINJ: return "Injection, ureteral";
            case _INSERTION: return "Insertion";
            case CERVINS: return "Insertion, cervical (uterine)";
            case IOSURGINS: return "Insertion, intraocular, surgical";
            case IU: return "Insertion, intrauterine";
            case LPINS: return "Insertion, lacrimal puncta";
            case PR: return "Insertion, rectal";
            case SQSURGINS: return "Insertion, subcutaneous, surgical";
            case URETHINS: return "Insertion, urethral";
            case VAGINSI: return "Insertion, vaginal";
            case _INSTILLATION: return "Instillation";
            case CECINSTL: return "Instillation, cecostomy";
            case EFT: return "Instillation, enteral feeding tube";
            case ENTINSTL: return "Instillation, enteral";
            case GT: return "Instillation, gastrostomy tube";
            case NGT: return "Instillation, nasogastric tube";
            case OGT: return "Instillation, orogastric tube";
            case BLADINSTL: return "Instillation, urinary catheter";
            case CAPDINSTL: return "Instillation, continuous ambulatory peritoneal dialysis port";
            case CTINSTL: return "Instillation, chest tube";
            case ETINSTL: return "Instillation, endotracheal tube";
            case GJT: return "Instillation, gastro-jejunostomy tube";
            case IBRONCHINSTIL: return "Instillation, intrabronchial";
            case IDUODINSTIL: return "Instillation, intraduodenal";
            case IESOPHINSTIL: return "Instillation, intraesophageal";
            case IGASTINSTIL: return "Instillation, intragastric";
            case IILEALINJ: return "Instillation, intraileal";
            case IOINSTL: return "Instillation, intraocular";
            case ISININSTIL: return "Instillation, intrasinal";
            case ITRACHINSTIL: return "Instillation, intratracheal";
            case IUINSTL: return "Instillation, intrauterine";
            case JJTINSTL: return "Instillation, jejunostomy tube";
            case LARYNGINSTIL: return "Instillation, laryngeal";
            case NASALINSTIL: return "Instillation, nasal";
            case NASOGASINSTIL: return "Instillation, nasogastric";
            case NTT: return "Instillation, nasotracheal tube";
            case OJJ: return "Instillation, orojejunum tube";
            case OT: return "Instillation, otic";
            case PDPINSTL: return "Instillation, peritoneal dialysis port";
            case PNSINSTL: return "Instillation, paranasal sinuses";
            case RECINSTL: return "Instillation, rectal";
            case RECTINSTL: return "Instillation, rectal tube";
            case SININSTIL: return "Instillation, sinus, unspecified";
            case SOFTISINSTIL: return "Instillation, soft tissue";
            case TRACHINSTL: return "Instillation, tracheostomy";
            case TRTYMPINSTIL: return "Instillation, transtympanic";
            case URETHINSTL: return "instillation, urethral";
            case _IONTOPHORESISROUTE: return "IontophoresisRoute";
            case IONTO: return "Topical application, iontophoresis";
            case _IRRIGATION: return "Irrigation";
            case GUIRR: return "Irrigation, genitourinary";
            case IGASTIRR: return "Irrigation, intragastric";
            case ILESIRR: return "Irrigation, intralesional";
            case IOIRR: return "Irrigation, intraocular";
            case BLADIRR: return "Irrigation, urinary bladder";
            case BLADIRRC: return "Irrigation, urinary bladder, continuous";
            case BLADIRRT: return "Irrigation, urinary bladder, tidal";
            case RECIRR: return "Irrigation, rectal";
            case _LAVAGEROUTE: return "LavageRoute";
            case IGASTLAV: return "Lavage, intragastric";
            case _MUCOSALABSORPTIONROUTE: return "MucosalAbsorptionRoute";
            case IDOUDMAB: return "Mucosal absorption, intraduodenal";
            case ITRACHMAB: return "Mucosal absorption, intratracheal";
            case SMUCMAB: return "Mucosal absorption, submucosal";
            case _NEBULIZATION: return "Nebulization";
            case ETNEB: return "Nebulization, endotracheal tube";
            case _RINSE: return "Rinse";
            case DENRINSE: return "Rinse, dental";
            case ORRINSE: return "Rinse, oral";
            case _SUPPOSITORYROUTE: return "SuppositoryRoute";
            case URETHSUP: return "Suppository, urethral";
            case _SWISH: return "Swish";
            case SWISHSPIT: return "Swish and spit out, oromucosal";
            case SWISHSWAL: return "Swish and swallow, oromucosal";
            case _TOPICALABSORPTIONROUTE: return "TopicalAbsorptionRoute";
            case TTYMPTABSORP: return "Topical absorption, transtympanic";
            case _TOPICALAPPLICATION: return "TopicalApplication";
            case DRESS: return "Topical application, soaked dressing";
            case SWAB: return "Topical application, swab";
            case TOPICAL: return "Topical";
            case BUC: return "Topical application, buccal";
            case CERV: return "Topical application, cervical";
            case DEN: return "Topical application, dental";
            case GIN: return "Topical application, gingival";
            case HAIR: return "Topical application, hair";
            case ICORNTA: return "Topical application, intracorneal";
            case ICORONTA: return "Topical application, intracoronal (dental)";
            case IESOPHTA: return "Topical application, intraesophageal";
            case IILEALTA: return "Topical application, intraileal";
            case ILTOP: return "Topical application, intralesional";
            case ILUMTA: return "Topical application, intraluminal";
            case IOTOP: return "Topical application, intraocular";
            case LARYNGTA: return "Topical application, laryngeal";
            case MUC: return "Topical application, mucous membrane";
            case NAIL: return "Topical application, nail";
            case NASAL: return "Topical application, nasal";
            case OPTHALTA: return "Topical application, ophthalmic";
            case ORALTA: return "Topical application, oral";
            case ORMUC: return "Topical application, oromucosal";
            case OROPHARTA: return "Topical application, oropharyngeal";
            case PERIANAL: return "Topical application, perianal";
            case PERINEAL: return "Topical application, perineal";
            case PDONTTA: return "Topical application, periodontal";
            case RECTAL: return "Topical application, rectal";
            case SCALP: return "Topical application, scalp";
            case OCDRESTA: return "Occlusive dressing technique";
            case SKIN: return "Topical application, skin";
            case SUBCONJTA: return "Subconjunctival";
            case TMUCTA: return "Topical application, transmucosal";
            case VAGINS: return "Topical application, vaginal";
            case INSUF: return "Insufflation";
            case TRNSDERM: return "Transdermal";
            case _ROUTEBYSITE: return "RouteBySite";
            case _AMNIOTICFLUIDSACROUTE: return "AmnioticFluidSacRoute";
            case _BILIARYROUTE: return "BiliaryRoute";
            case _BODYSURFACEROUTE: return "BodySurfaceRoute";
            case _BUCCALMUCOSAROUTE: return "BuccalMucosaRoute";
            case _CECOSTOMYROUTE: return "CecostomyRoute";
            case _CERVICALROUTE: return "CervicalRoute";
            case _ENDOCERVICALROUTE: return "EndocervicalRoute";
            case _ENTERALROUTE: return "EnteralRoute";
            case _EPIDURALROUTE: return "EpiduralRoute";
            case _EXTRAAMNIOTICROUTE: return "ExtraAmnioticRoute";
            case _EXTRACORPOREALCIRCULATIONROUTE: return "ExtracorporealCirculationRoute";
            case _GASTRICROUTE: return "GastricRoute";
            case _GENITOURINARYROUTE: return "GenitourinaryRoute";
            case _GINGIVALROUTE: return "GingivalRoute";
            case _HAIRROUTE: return "HairRoute";
            case _INTERAMENINGEALROUTE: return "InterameningealRoute";
            case _INTERSTITIALROUTE: return "InterstitialRoute";
            case _INTRAABDOMINALROUTE: return "IntraabdominalRoute";
            case _INTRAARTERIALROUTE: return "IntraarterialRoute";
            case _INTRAARTICULARROUTE: return "IntraarticularRoute";
            case _INTRABRONCHIALROUTE: return "IntrabronchialRoute";
            case _INTRABURSALROUTE: return "IntrabursalRoute";
            case _INTRACARDIACROUTE: return "IntracardiacRoute";
            case _INTRACARTILAGINOUSROUTE: return "IntracartilaginousRoute";
            case _INTRACAUDALROUTE: return "IntracaudalRoute";
            case _INTRACAVERNOSALROUTE: return "IntracavernosalRoute";
            case _INTRACAVITARYROUTE: return "IntracavitaryRoute";
            case _INTRACEREBRALROUTE: return "IntracerebralRoute";
            case _INTRACERVICALROUTE: return "IntracervicalRoute";
            case _INTRACISTERNALROUTE: return "IntracisternalRoute";
            case _INTRACORNEALROUTE: return "IntracornealRoute";
            case _INTRACORONALROUTE: return "IntracoronalRoute";
            case _INTRACORONARYROUTE: return "IntracoronaryRoute";
            case _INTRACORPUSCAVERNOSUMROUTE: return "IntracorpusCavernosumRoute";
            case _INTRADERMALROUTE: return "IntradermalRoute";
            case _INTRADISCALROUTE: return "IntradiscalRoute";
            case _INTRADUCTALROUTE: return "IntraductalRoute";
            case _INTRADUODENALROUTE: return "IntraduodenalRoute";
            case _INTRADURALROUTE: return "IntraduralRoute";
            case _INTRAEPIDERMALROUTE: return "IntraepidermalRoute";
            case _INTRAEPITHELIALROUTE: return "IntraepithelialRoute";
            case _INTRAESOPHAGEALROUTE: return "IntraesophagealRoute";
            case _INTRAGASTRICROUTE: return "IntragastricRoute";
            case _INTRAILEALROUTE: return "IntrailealRoute";
            case _INTRALESIONALROUTE: return "IntralesionalRoute";
            case _INTRALUMINALROUTE: return "IntraluminalRoute";
            case _INTRALYMPHATICROUTE: return "IntralymphaticRoute";
            case _INTRAMEDULLARYROUTE: return "IntramedullaryRoute";
            case _INTRAMUSCULARROUTE: return "IntramuscularRoute";
            case _INTRAOCULARROUTE: return "IntraocularRoute";
            case _INTRAOSSEOUSROUTE: return "IntraosseousRoute";
            case _INTRAOVARIANROUTE: return "IntraovarianRoute";
            case _INTRAPERICARDIALROUTE: return "IntrapericardialRoute";
            case _INTRAPERITONEALROUTE: return "IntraperitonealRoute";
            case _INTRAPLEURALROUTE: return "IntrapleuralRoute";
            case _INTRAPROSTATICROUTE: return "IntraprostaticRoute";
            case _INTRAPULMONARYROUTE: return "IntrapulmonaryRoute";
            case _INTRASINALROUTE: return "IntrasinalRoute";
            case _INTRASPINALROUTE: return "IntraspinalRoute";
            case _INTRASTERNALROUTE: return "IntrasternalRoute";
            case _INTRASYNOVIALROUTE: return "IntrasynovialRoute";
            case _INTRATENDINOUSROUTE: return "IntratendinousRoute";
            case _INTRATESTICULARROUTE: return "IntratesticularRoute";
            case _INTRATHECALROUTE: return "IntrathecalRoute";
            case _INTRATHORACICROUTE: return "IntrathoracicRoute";
            case _INTRATRACHEALROUTE: return "IntratrachealRoute";
            case _INTRATUBULARROUTE: return "IntratubularRoute";
            case _INTRATUMORROUTE: return "IntratumorRoute";
            case _INTRATYMPANICROUTE: return "IntratympanicRoute";
            case _INTRAUTERINEROUTE: return "IntrauterineRoute";
            case _INTRAVASCULARROUTE: return "IntravascularRoute";
            case _INTRAVENOUSROUTE: return "IntravenousRoute";
            case _INTRAVENTRICULARROUTE: return "IntraventricularRoute";
            case _INTRAVESICLEROUTE: return "IntravesicleRoute";
            case _INTRAVITREALROUTE: return "IntravitrealRoute";
            case _JEJUNUMROUTE: return "JejunumRoute";
            case _LACRIMALPUNCTAROUTE: return "LacrimalPunctaRoute";
            case _LARYNGEALROUTE: return "LaryngealRoute";
            case _LINGUALROUTE: return "LingualRoute";
            case _MUCOUSMEMBRANEROUTE: return "MucousMembraneRoute";
            case _NAILROUTE: return "NailRoute";
            case _NASALROUTE: return "NasalRoute";
            case _OPHTHALMICROUTE: return "OphthalmicRoute";
            case _ORALROUTE: return "OralRoute";
            case _OROMUCOSALROUTE: return "OromucosalRoute";
            case _OROPHARYNGEALROUTE: return "OropharyngealRoute";
            case _OTICROUTE: return "OticRoute";
            case _PARANASALSINUSESROUTE: return "ParanasalSinusesRoute";
            case _PARENTERALROUTE: return "ParenteralRoute";
            case _PERIANALROUTE: return "PerianalRoute";
            case _PERIARTICULARROUTE: return "PeriarticularRoute";
            case _PERIDURALROUTE: return "PeriduralRoute";
            case _PERINEALROUTE: return "PerinealRoute";
            case _PERINEURALROUTE: return "PerineuralRoute";
            case _PERIODONTALROUTE: return "PeriodontalRoute";
            case _PULMONARYROUTE: return "PulmonaryRoute";
            case _RECTALROUTE: return "RectalRoute";
            case _RESPIRATORYTRACTROUTE: return "RespiratoryTractRoute";
            case _RETROBULBARROUTE: return "RetrobulbarRoute";
            case _SCALPROUTE: return "ScalpRoute";
            case _SINUSUNSPECIFIEDROUTE: return "SinusUnspecifiedRoute";
            case _SKINROUTE: return "SkinRoute";
            case _SOFTTISSUEROUTE: return "SoftTissueRoute";
            case _SUBARACHNOIDROUTE: return "SubarachnoidRoute";
            case _SUBCONJUNCTIVALROUTE: return "SubconjunctivalRoute";
            case _SUBCUTANEOUSROUTE: return "SubcutaneousRoute";
            case _SUBLESIONALROUTE: return "SublesionalRoute";
            case _SUBLINGUALROUTE: return "SublingualRoute";
            case _SUBMUCOSALROUTE: return "SubmucosalRoute";
            case _TRACHEOSTOMYROUTE: return "TracheostomyRoute";
            case _TRANSMUCOSALROUTE: return "TransmucosalRoute";
            case _TRANSPLACENTALROUTE: return "TransplacentalRoute";
            case _TRANSTRACHEALROUTE: return "TranstrachealRoute";
            case _TRANSTYMPANICROUTE: return "TranstympanicRoute";
            case _URETERALROUTE: return "UreteralRoute";
            case _URETHRALROUTE: return "UrethralRoute";
            case _URINARYBLADDERROUTE: return "UrinaryBladderRoute";
            case _URINARYTRACTROUTE: return "UrinaryTractRoute";
            case _VAGINALROUTE: return "VaginalRoute";
            case _VITREOUSHUMOURROUTE: return "VitreousHumourRoute";
            default: return "?";
          }
    }


}

