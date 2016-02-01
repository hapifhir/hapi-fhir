package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActCodeEnumFactory implements EnumFactory<V3ActCode> {

  public V3ActCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ActAccountCode".equals(codeString))
      return V3ActCode._ACTACCOUNTCODE;
    if ("ACCTRECEIVABLE".equals(codeString))
      return V3ActCode.ACCTRECEIVABLE;
    if ("CASH".equals(codeString))
      return V3ActCode.CASH;
    if ("CC".equals(codeString))
      return V3ActCode.CC;
    if ("AE".equals(codeString))
      return V3ActCode.AE;
    if ("DN".equals(codeString))
      return V3ActCode.DN;
    if ("DV".equals(codeString))
      return V3ActCode.DV;
    if ("MC".equals(codeString))
      return V3ActCode.MC;
    if ("V".equals(codeString))
      return V3ActCode.V;
    if ("PBILLACCT".equals(codeString))
      return V3ActCode.PBILLACCT;
    if ("_ActAdjudicationCode".equals(codeString))
      return V3ActCode._ACTADJUDICATIONCODE;
    if ("_ActAdjudicationGroupCode".equals(codeString))
      return V3ActCode._ACTADJUDICATIONGROUPCODE;
    if ("CONT".equals(codeString))
      return V3ActCode.CONT;
    if ("DAY".equals(codeString))
      return V3ActCode.DAY;
    if ("LOC".equals(codeString))
      return V3ActCode.LOC;
    if ("MONTH".equals(codeString))
      return V3ActCode.MONTH;
    if ("PERIOD".equals(codeString))
      return V3ActCode.PERIOD;
    if ("PROV".equals(codeString))
      return V3ActCode.PROV;
    if ("WEEK".equals(codeString))
      return V3ActCode.WEEK;
    if ("YEAR".equals(codeString))
      return V3ActCode.YEAR;
    if ("AA".equals(codeString))
      return V3ActCode.AA;
    if ("ANF".equals(codeString))
      return V3ActCode.ANF;
    if ("AR".equals(codeString))
      return V3ActCode.AR;
    if ("AS".equals(codeString))
      return V3ActCode.AS;
    if ("_ActAdjudicationResultActionCode".equals(codeString))
      return V3ActCode._ACTADJUDICATIONRESULTACTIONCODE;
    if ("DISPLAY".equals(codeString))
      return V3ActCode.DISPLAY;
    if ("FORM".equals(codeString))
      return V3ActCode.FORM;
    if ("_ActBillableModifierCode".equals(codeString))
      return V3ActCode._ACTBILLABLEMODIFIERCODE;
    if ("CPTM".equals(codeString))
      return V3ActCode.CPTM;
    if ("HCPCSA".equals(codeString))
      return V3ActCode.HCPCSA;
    if ("_ActBillingArrangementCode".equals(codeString))
      return V3ActCode._ACTBILLINGARRANGEMENTCODE;
    if ("BLK".equals(codeString))
      return V3ActCode.BLK;
    if ("CAP".equals(codeString))
      return V3ActCode.CAP;
    if ("CONTF".equals(codeString))
      return V3ActCode.CONTF;
    if ("FINBILL".equals(codeString))
      return V3ActCode.FINBILL;
    if ("ROST".equals(codeString))
      return V3ActCode.ROST;
    if ("SESS".equals(codeString))
      return V3ActCode.SESS;
    if ("FFS".equals(codeString))
      return V3ActCode.FFS;
    if ("FFPS".equals(codeString))
      return V3ActCode.FFPS;
    if ("FFCS".equals(codeString))
      return V3ActCode.FFCS;
    if ("TFS".equals(codeString))
      return V3ActCode.TFS;
    if ("_ActBoundedROICode".equals(codeString))
      return V3ActCode._ACTBOUNDEDROICODE;
    if ("ROIFS".equals(codeString))
      return V3ActCode.ROIFS;
    if ("ROIPS".equals(codeString))
      return V3ActCode.ROIPS;
    if ("_ActCareProvisionCode".equals(codeString))
      return V3ActCode._ACTCAREPROVISIONCODE;
    if ("_ActCredentialedCareCode".equals(codeString))
      return V3ActCode._ACTCREDENTIALEDCARECODE;
    if ("_ActCredentialedCareProvisionPersonCode".equals(codeString))
      return V3ActCode._ACTCREDENTIALEDCAREPROVISIONPERSONCODE;
    if ("CACC".equals(codeString))
      return V3ActCode.CACC;
    if ("CAIC".equals(codeString))
      return V3ActCode.CAIC;
    if ("CAMC".equals(codeString))
      return V3ActCode.CAMC;
    if ("CANC".equals(codeString))
      return V3ActCode.CANC;
    if ("CAPC".equals(codeString))
      return V3ActCode.CAPC;
    if ("CBGC".equals(codeString))
      return V3ActCode.CBGC;
    if ("CCCC".equals(codeString))
      return V3ActCode.CCCC;
    if ("CCGC".equals(codeString))
      return V3ActCode.CCGC;
    if ("CCPC".equals(codeString))
      return V3ActCode.CCPC;
    if ("CCSC".equals(codeString))
      return V3ActCode.CCSC;
    if ("CDEC".equals(codeString))
      return V3ActCode.CDEC;
    if ("CDRC".equals(codeString))
      return V3ActCode.CDRC;
    if ("CEMC".equals(codeString))
      return V3ActCode.CEMC;
    if ("CFPC".equals(codeString))
      return V3ActCode.CFPC;
    if ("CIMC".equals(codeString))
      return V3ActCode.CIMC;
    if ("CMGC".equals(codeString))
      return V3ActCode.CMGC;
    if ("CNEC".equals(codeString))
      return V3ActCode.CNEC;
    if ("CNMC".equals(codeString))
      return V3ActCode.CNMC;
    if ("CNQC".equals(codeString))
      return V3ActCode.CNQC;
    if ("CNSC".equals(codeString))
      return V3ActCode.CNSC;
    if ("COGC".equals(codeString))
      return V3ActCode.COGC;
    if ("COMC".equals(codeString))
      return V3ActCode.COMC;
    if ("COPC".equals(codeString))
      return V3ActCode.COPC;
    if ("COSC".equals(codeString))
      return V3ActCode.COSC;
    if ("COTC".equals(codeString))
      return V3ActCode.COTC;
    if ("CPEC".equals(codeString))
      return V3ActCode.CPEC;
    if ("CPGC".equals(codeString))
      return V3ActCode.CPGC;
    if ("CPHC".equals(codeString))
      return V3ActCode.CPHC;
    if ("CPRC".equals(codeString))
      return V3ActCode.CPRC;
    if ("CPSC".equals(codeString))
      return V3ActCode.CPSC;
    if ("CPYC".equals(codeString))
      return V3ActCode.CPYC;
    if ("CROC".equals(codeString))
      return V3ActCode.CROC;
    if ("CRPC".equals(codeString))
      return V3ActCode.CRPC;
    if ("CSUC".equals(codeString))
      return V3ActCode.CSUC;
    if ("CTSC".equals(codeString))
      return V3ActCode.CTSC;
    if ("CURC".equals(codeString))
      return V3ActCode.CURC;
    if ("CVSC".equals(codeString))
      return V3ActCode.CVSC;
    if ("LGPC".equals(codeString))
      return V3ActCode.LGPC;
    if ("_ActCredentialedCareProvisionProgramCode".equals(codeString))
      return V3ActCode._ACTCREDENTIALEDCAREPROVISIONPROGRAMCODE;
    if ("AALC".equals(codeString))
      return V3ActCode.AALC;
    if ("AAMC".equals(codeString))
      return V3ActCode.AAMC;
    if ("ABHC".equals(codeString))
      return V3ActCode.ABHC;
    if ("ACAC".equals(codeString))
      return V3ActCode.ACAC;
    if ("ACHC".equals(codeString))
      return V3ActCode.ACHC;
    if ("AHOC".equals(codeString))
      return V3ActCode.AHOC;
    if ("ALTC".equals(codeString))
      return V3ActCode.ALTC;
    if ("AOSC".equals(codeString))
      return V3ActCode.AOSC;
    if ("CACS".equals(codeString))
      return V3ActCode.CACS;
    if ("CAMI".equals(codeString))
      return V3ActCode.CAMI;
    if ("CAST".equals(codeString))
      return V3ActCode.CAST;
    if ("CBAR".equals(codeString))
      return V3ActCode.CBAR;
    if ("CCAD".equals(codeString))
      return V3ActCode.CCAD;
    if ("CCAR".equals(codeString))
      return V3ActCode.CCAR;
    if ("CDEP".equals(codeString))
      return V3ActCode.CDEP;
    if ("CDGD".equals(codeString))
      return V3ActCode.CDGD;
    if ("CDIA".equals(codeString))
      return V3ActCode.CDIA;
    if ("CEPI".equals(codeString))
      return V3ActCode.CEPI;
    if ("CFEL".equals(codeString))
      return V3ActCode.CFEL;
    if ("CHFC".equals(codeString))
      return V3ActCode.CHFC;
    if ("CHRO".equals(codeString))
      return V3ActCode.CHRO;
    if ("CHYP".equals(codeString))
      return V3ActCode.CHYP;
    if ("CMIH".equals(codeString))
      return V3ActCode.CMIH;
    if ("CMSC".equals(codeString))
      return V3ActCode.CMSC;
    if ("COJR".equals(codeString))
      return V3ActCode.COJR;
    if ("CONC".equals(codeString))
      return V3ActCode.CONC;
    if ("COPD".equals(codeString))
      return V3ActCode.COPD;
    if ("CORT".equals(codeString))
      return V3ActCode.CORT;
    if ("CPAD".equals(codeString))
      return V3ActCode.CPAD;
    if ("CPND".equals(codeString))
      return V3ActCode.CPND;
    if ("CPST".equals(codeString))
      return V3ActCode.CPST;
    if ("CSDM".equals(codeString))
      return V3ActCode.CSDM;
    if ("CSIC".equals(codeString))
      return V3ActCode.CSIC;
    if ("CSLD".equals(codeString))
      return V3ActCode.CSLD;
    if ("CSPT".equals(codeString))
      return V3ActCode.CSPT;
    if ("CTBU".equals(codeString))
      return V3ActCode.CTBU;
    if ("CVDC".equals(codeString))
      return V3ActCode.CVDC;
    if ("CWMA".equals(codeString))
      return V3ActCode.CWMA;
    if ("CWOH".equals(codeString))
      return V3ActCode.CWOH;
    if ("_ActEncounterCode".equals(codeString))
      return V3ActCode._ACTENCOUNTERCODE;
    if ("AMB".equals(codeString))
      return V3ActCode.AMB;
    if ("EMER".equals(codeString))
      return V3ActCode.EMER;
    if ("FLD".equals(codeString))
      return V3ActCode.FLD;
    if ("HH".equals(codeString))
      return V3ActCode.HH;
    if ("IMP".equals(codeString))
      return V3ActCode.IMP;
    if ("ACUTE".equals(codeString))
      return V3ActCode.ACUTE;
    if ("NONAC".equals(codeString))
      return V3ActCode.NONAC;
    if ("PRENC".equals(codeString))
      return V3ActCode.PRENC;
    if ("SS".equals(codeString))
      return V3ActCode.SS;
    if ("VR".equals(codeString))
      return V3ActCode.VR;
    if ("_ActMedicalServiceCode".equals(codeString))
      return V3ActCode._ACTMEDICALSERVICECODE;
    if ("ALC".equals(codeString))
      return V3ActCode.ALC;
    if ("CARD".equals(codeString))
      return V3ActCode.CARD;
    if ("CHR".equals(codeString))
      return V3ActCode.CHR;
    if ("DNTL".equals(codeString))
      return V3ActCode.DNTL;
    if ("DRGRHB".equals(codeString))
      return V3ActCode.DRGRHB;
    if ("GENRL".equals(codeString))
      return V3ActCode.GENRL;
    if ("MED".equals(codeString))
      return V3ActCode.MED;
    if ("OBS".equals(codeString))
      return V3ActCode.OBS;
    if ("ONC".equals(codeString))
      return V3ActCode.ONC;
    if ("PALL".equals(codeString))
      return V3ActCode.PALL;
    if ("PED".equals(codeString))
      return V3ActCode.PED;
    if ("PHAR".equals(codeString))
      return V3ActCode.PHAR;
    if ("PHYRHB".equals(codeString))
      return V3ActCode.PHYRHB;
    if ("PSYCH".equals(codeString))
      return V3ActCode.PSYCH;
    if ("SURG".equals(codeString))
      return V3ActCode.SURG;
    if ("_ActClaimAttachmentCategoryCode".equals(codeString))
      return V3ActCode._ACTCLAIMATTACHMENTCATEGORYCODE;
    if ("AUTOATTCH".equals(codeString))
      return V3ActCode.AUTOATTCH;
    if ("DOCUMENT".equals(codeString))
      return V3ActCode.DOCUMENT;
    if ("HEALTHREC".equals(codeString))
      return V3ActCode.HEALTHREC;
    if ("IMG".equals(codeString))
      return V3ActCode.IMG;
    if ("LABRESULTS".equals(codeString))
      return V3ActCode.LABRESULTS;
    if ("MODEL".equals(codeString))
      return V3ActCode.MODEL;
    if ("WIATTCH".equals(codeString))
      return V3ActCode.WIATTCH;
    if ("XRAY".equals(codeString))
      return V3ActCode.XRAY;
    if ("_ActConsentType".equals(codeString))
      return V3ActCode._ACTCONSENTTYPE;
    if ("ICOL".equals(codeString))
      return V3ActCode.ICOL;
    if ("IDSCL".equals(codeString))
      return V3ActCode.IDSCL;
    if ("INFA".equals(codeString))
      return V3ActCode.INFA;
    if ("INFAO".equals(codeString))
      return V3ActCode.INFAO;
    if ("INFASO".equals(codeString))
      return V3ActCode.INFASO;
    if ("IRDSCL".equals(codeString))
      return V3ActCode.IRDSCL;
    if ("RESEARCH".equals(codeString))
      return V3ActCode.RESEARCH;
    if ("RSDID".equals(codeString))
      return V3ActCode.RSDID;
    if ("RSREID".equals(codeString))
      return V3ActCode.RSREID;
    if ("_ActContainerRegistrationCode".equals(codeString))
      return V3ActCode._ACTCONTAINERREGISTRATIONCODE;
    if ("ID".equals(codeString))
      return V3ActCode.ID;
    if ("IP".equals(codeString))
      return V3ActCode.IP;
    if ("L".equals(codeString))
      return V3ActCode.L;
    if ("M".equals(codeString))
      return V3ActCode.M;
    if ("O".equals(codeString))
      return V3ActCode.O;
    if ("R".equals(codeString))
      return V3ActCode.R;
    if ("X".equals(codeString))
      return V3ActCode.X;
    if ("_ActControlVariable".equals(codeString))
      return V3ActCode._ACTCONTROLVARIABLE;
    if ("AUTO".equals(codeString))
      return V3ActCode.AUTO;
    if ("ENDC".equals(codeString))
      return V3ActCode.ENDC;
    if ("REFLEX".equals(codeString))
      return V3ActCode.REFLEX;
    if ("_ActCoverageConfirmationCode".equals(codeString))
      return V3ActCode._ACTCOVERAGECONFIRMATIONCODE;
    if ("_ActCoverageAuthorizationConfirmationCode".equals(codeString))
      return V3ActCode._ACTCOVERAGEAUTHORIZATIONCONFIRMATIONCODE;
    if ("AUTH".equals(codeString))
      return V3ActCode.AUTH;
    if ("NAUTH".equals(codeString))
      return V3ActCode.NAUTH;
    if ("_ActCoverageEligibilityConfirmationCode".equals(codeString))
      return V3ActCode._ACTCOVERAGEELIGIBILITYCONFIRMATIONCODE;
    if ("ELG".equals(codeString))
      return V3ActCode.ELG;
    if ("NELG".equals(codeString))
      return V3ActCode.NELG;
    if ("_ActCoverageLimitCode".equals(codeString))
      return V3ActCode._ACTCOVERAGELIMITCODE;
    if ("_ActCoverageQuantityLimitCode".equals(codeString))
      return V3ActCode._ACTCOVERAGEQUANTITYLIMITCODE;
    if ("COVPRD".equals(codeString))
      return V3ActCode.COVPRD;
    if ("LFEMX".equals(codeString))
      return V3ActCode.LFEMX;
    if ("NETAMT".equals(codeString))
      return V3ActCode.NETAMT;
    if ("PRDMX".equals(codeString))
      return V3ActCode.PRDMX;
    if ("UNITPRICE".equals(codeString))
      return V3ActCode.UNITPRICE;
    if ("UNITQTY".equals(codeString))
      return V3ActCode.UNITQTY;
    if ("COVMX".equals(codeString))
      return V3ActCode.COVMX;
    if ("_ActCoveredPartyLimitCode".equals(codeString))
      return V3ActCode._ACTCOVEREDPARTYLIMITCODE;
    if ("_ActCoverageTypeCode".equals(codeString))
      return V3ActCode._ACTCOVERAGETYPECODE;
    if ("_ActInsurancePolicyCode".equals(codeString))
      return V3ActCode._ACTINSURANCEPOLICYCODE;
    if ("EHCPOL".equals(codeString))
      return V3ActCode.EHCPOL;
    if ("HSAPOL".equals(codeString))
      return V3ActCode.HSAPOL;
    if ("AUTOPOL".equals(codeString))
      return V3ActCode.AUTOPOL;
    if ("COL".equals(codeString))
      return V3ActCode.COL;
    if ("UNINSMOT".equals(codeString))
      return V3ActCode.UNINSMOT;
    if ("PUBLICPOL".equals(codeString))
      return V3ActCode.PUBLICPOL;
    if ("DENTPRG".equals(codeString))
      return V3ActCode.DENTPRG;
    if ("DISEASEPRG".equals(codeString))
      return V3ActCode.DISEASEPRG;
    if ("CANPRG".equals(codeString))
      return V3ActCode.CANPRG;
    if ("ENDRENAL".equals(codeString))
      return V3ActCode.ENDRENAL;
    if ("HIVAIDS".equals(codeString))
      return V3ActCode.HIVAIDS;
    if ("MANDPOL".equals(codeString))
      return V3ActCode.MANDPOL;
    if ("MENTPRG".equals(codeString))
      return V3ActCode.MENTPRG;
    if ("SAFNET".equals(codeString))
      return V3ActCode.SAFNET;
    if ("SUBPRG".equals(codeString))
      return V3ActCode.SUBPRG;
    if ("SUBSIDIZ".equals(codeString))
      return V3ActCode.SUBSIDIZ;
    if ("SUBSIDMC".equals(codeString))
      return V3ActCode.SUBSIDMC;
    if ("SUBSUPP".equals(codeString))
      return V3ActCode.SUBSUPP;
    if ("WCBPOL".equals(codeString))
      return V3ActCode.WCBPOL;
    if ("_ActInsuranceTypeCode".equals(codeString))
      return V3ActCode._ACTINSURANCETYPECODE;
    if ("_ActHealthInsuranceTypeCode".equals(codeString))
      return V3ActCode._ACTHEALTHINSURANCETYPECODE;
    if ("DENTAL".equals(codeString))
      return V3ActCode.DENTAL;
    if ("DISEASE".equals(codeString))
      return V3ActCode.DISEASE;
    if ("DRUGPOL".equals(codeString))
      return V3ActCode.DRUGPOL;
    if ("HIP".equals(codeString))
      return V3ActCode.HIP;
    if ("LTC".equals(codeString))
      return V3ActCode.LTC;
    if ("MCPOL".equals(codeString))
      return V3ActCode.MCPOL;
    if ("POS".equals(codeString))
      return V3ActCode.POS;
    if ("HMO".equals(codeString))
      return V3ActCode.HMO;
    if ("PPO".equals(codeString))
      return V3ActCode.PPO;
    if ("MENTPOL".equals(codeString))
      return V3ActCode.MENTPOL;
    if ("SUBPOL".equals(codeString))
      return V3ActCode.SUBPOL;
    if ("VISPOL".equals(codeString))
      return V3ActCode.VISPOL;
    if ("DIS".equals(codeString))
      return V3ActCode.DIS;
    if ("EWB".equals(codeString))
      return V3ActCode.EWB;
    if ("FLEXP".equals(codeString))
      return V3ActCode.FLEXP;
    if ("LIFE".equals(codeString))
      return V3ActCode.LIFE;
    if ("ANNU".equals(codeString))
      return V3ActCode.ANNU;
    if ("TLIFE".equals(codeString))
      return V3ActCode.TLIFE;
    if ("ULIFE".equals(codeString))
      return V3ActCode.ULIFE;
    if ("PNC".equals(codeString))
      return V3ActCode.PNC;
    if ("REI".equals(codeString))
      return V3ActCode.REI;
    if ("SURPL".equals(codeString))
      return V3ActCode.SURPL;
    if ("UMBRL".equals(codeString))
      return V3ActCode.UMBRL;
    if ("_ActProgramTypeCode".equals(codeString))
      return V3ActCode._ACTPROGRAMTYPECODE;
    if ("CHAR".equals(codeString))
      return V3ActCode.CHAR;
    if ("CRIME".equals(codeString))
      return V3ActCode.CRIME;
    if ("EAP".equals(codeString))
      return V3ActCode.EAP;
    if ("GOVEMP".equals(codeString))
      return V3ActCode.GOVEMP;
    if ("HIRISK".equals(codeString))
      return V3ActCode.HIRISK;
    if ("IND".equals(codeString))
      return V3ActCode.IND;
    if ("MILITARY".equals(codeString))
      return V3ActCode.MILITARY;
    if ("RETIRE".equals(codeString))
      return V3ActCode.RETIRE;
    if ("SOCIAL".equals(codeString))
      return V3ActCode.SOCIAL;
    if ("VET".equals(codeString))
      return V3ActCode.VET;
    if ("_ActDetectedIssueManagementCode".equals(codeString))
      return V3ActCode._ACTDETECTEDISSUEMANAGEMENTCODE;
    if ("_ActAdministrativeDetectedIssueManagementCode".equals(codeString))
      return V3ActCode._ACTADMINISTRATIVEDETECTEDISSUEMANAGEMENTCODE;
    if ("_AuthorizationIssueManagementCode".equals(codeString))
      return V3ActCode._AUTHORIZATIONISSUEMANAGEMENTCODE;
    if ("EMAUTH".equals(codeString))
      return V3ActCode.EMAUTH;
    if ("21".equals(codeString))
      return V3ActCode._21;
    if ("1".equals(codeString))
      return V3ActCode._1;
    if ("19".equals(codeString))
      return V3ActCode._19;
    if ("2".equals(codeString))
      return V3ActCode._2;
    if ("22".equals(codeString))
      return V3ActCode._22;
    if ("23".equals(codeString))
      return V3ActCode._23;
    if ("3".equals(codeString))
      return V3ActCode._3;
    if ("4".equals(codeString))
      return V3ActCode._4;
    if ("5".equals(codeString))
      return V3ActCode._5;
    if ("6".equals(codeString))
      return V3ActCode._6;
    if ("7".equals(codeString))
      return V3ActCode._7;
    if ("14".equals(codeString))
      return V3ActCode._14;
    if ("15".equals(codeString))
      return V3ActCode._15;
    if ("16".equals(codeString))
      return V3ActCode._16;
    if ("17".equals(codeString))
      return V3ActCode._17;
    if ("18".equals(codeString))
      return V3ActCode._18;
    if ("20".equals(codeString))
      return V3ActCode._20;
    if ("8".equals(codeString))
      return V3ActCode._8;
    if ("10".equals(codeString))
      return V3ActCode._10;
    if ("11".equals(codeString))
      return V3ActCode._11;
    if ("12".equals(codeString))
      return V3ActCode._12;
    if ("13".equals(codeString))
      return V3ActCode._13;
    if ("9".equals(codeString))
      return V3ActCode._9;
    if ("_ActExposureCode".equals(codeString))
      return V3ActCode._ACTEXPOSURECODE;
    if ("CHLDCARE".equals(codeString))
      return V3ActCode.CHLDCARE;
    if ("CONVEYNC".equals(codeString))
      return V3ActCode.CONVEYNC;
    if ("HLTHCARE".equals(codeString))
      return V3ActCode.HLTHCARE;
    if ("HOMECARE".equals(codeString))
      return V3ActCode.HOMECARE;
    if ("HOSPPTNT".equals(codeString))
      return V3ActCode.HOSPPTNT;
    if ("HOSPVSTR".equals(codeString))
      return V3ActCode.HOSPVSTR;
    if ("HOUSEHLD".equals(codeString))
      return V3ActCode.HOUSEHLD;
    if ("INMATE".equals(codeString))
      return V3ActCode.INMATE;
    if ("INTIMATE".equals(codeString))
      return V3ActCode.INTIMATE;
    if ("LTRMCARE".equals(codeString))
      return V3ActCode.LTRMCARE;
    if ("PLACE".equals(codeString))
      return V3ActCode.PLACE;
    if ("PTNTCARE".equals(codeString))
      return V3ActCode.PTNTCARE;
    if ("SCHOOL2".equals(codeString))
      return V3ActCode.SCHOOL2;
    if ("SOCIAL2".equals(codeString))
      return V3ActCode.SOCIAL2;
    if ("SUBSTNCE".equals(codeString))
      return V3ActCode.SUBSTNCE;
    if ("TRAVINT".equals(codeString))
      return V3ActCode.TRAVINT;
    if ("WORK2".equals(codeString))
      return V3ActCode.WORK2;
    if ("_ActFinancialTransactionCode".equals(codeString))
      return V3ActCode._ACTFINANCIALTRANSACTIONCODE;
    if ("CHRG".equals(codeString))
      return V3ActCode.CHRG;
    if ("REV".equals(codeString))
      return V3ActCode.REV;
    if ("_ActIncidentCode".equals(codeString))
      return V3ActCode._ACTINCIDENTCODE;
    if ("MVA".equals(codeString))
      return V3ActCode.MVA;
    if ("SCHOOL".equals(codeString))
      return V3ActCode.SCHOOL;
    if ("SPT".equals(codeString))
      return V3ActCode.SPT;
    if ("WPA".equals(codeString))
      return V3ActCode.WPA;
    if ("_ActInformationAccessCode".equals(codeString))
      return V3ActCode._ACTINFORMATIONACCESSCODE;
    if ("ACADR".equals(codeString))
      return V3ActCode.ACADR;
    if ("ACALL".equals(codeString))
      return V3ActCode.ACALL;
    if ("ACALLG".equals(codeString))
      return V3ActCode.ACALLG;
    if ("ACCONS".equals(codeString))
      return V3ActCode.ACCONS;
    if ("ACDEMO".equals(codeString))
      return V3ActCode.ACDEMO;
    if ("ACDI".equals(codeString))
      return V3ActCode.ACDI;
    if ("ACIMMUN".equals(codeString))
      return V3ActCode.ACIMMUN;
    if ("ACLAB".equals(codeString))
      return V3ActCode.ACLAB;
    if ("ACMED".equals(codeString))
      return V3ActCode.ACMED;
    if ("ACMEDC".equals(codeString))
      return V3ActCode.ACMEDC;
    if ("ACMEN".equals(codeString))
      return V3ActCode.ACMEN;
    if ("ACOBS".equals(codeString))
      return V3ActCode.ACOBS;
    if ("ACPOLPRG".equals(codeString))
      return V3ActCode.ACPOLPRG;
    if ("ACPROV".equals(codeString))
      return V3ActCode.ACPROV;
    if ("ACPSERV".equals(codeString))
      return V3ActCode.ACPSERV;
    if ("ACSUBSTAB".equals(codeString))
      return V3ActCode.ACSUBSTAB;
    if ("_ActInformationAccessContextCode".equals(codeString))
      return V3ActCode._ACTINFORMATIONACCESSCONTEXTCODE;
    if ("INFAUT".equals(codeString))
      return V3ActCode.INFAUT;
    if ("INFCON".equals(codeString))
      return V3ActCode.INFCON;
    if ("INFCRT".equals(codeString))
      return V3ActCode.INFCRT;
    if ("INFDNG".equals(codeString))
      return V3ActCode.INFDNG;
    if ("INFEMER".equals(codeString))
      return V3ActCode.INFEMER;
    if ("INFPWR".equals(codeString))
      return V3ActCode.INFPWR;
    if ("INFREG".equals(codeString))
      return V3ActCode.INFREG;
    if ("_ActInformationCategoryCode".equals(codeString))
      return V3ActCode._ACTINFORMATIONCATEGORYCODE;
    if ("ALLCAT".equals(codeString))
      return V3ActCode.ALLCAT;
    if ("ALLGCAT".equals(codeString))
      return V3ActCode.ALLGCAT;
    if ("ARCAT".equals(codeString))
      return V3ActCode.ARCAT;
    if ("COBSCAT".equals(codeString))
      return V3ActCode.COBSCAT;
    if ("DEMOCAT".equals(codeString))
      return V3ActCode.DEMOCAT;
    if ("DICAT".equals(codeString))
      return V3ActCode.DICAT;
    if ("IMMUCAT".equals(codeString))
      return V3ActCode.IMMUCAT;
    if ("LABCAT".equals(codeString))
      return V3ActCode.LABCAT;
    if ("MEDCCAT".equals(codeString))
      return V3ActCode.MEDCCAT;
    if ("MENCAT".equals(codeString))
      return V3ActCode.MENCAT;
    if ("PSVCCAT".equals(codeString))
      return V3ActCode.PSVCCAT;
    if ("RXCAT".equals(codeString))
      return V3ActCode.RXCAT;
    if ("_ActInvoiceElementCode".equals(codeString))
      return V3ActCode._ACTINVOICEELEMENTCODE;
    if ("_ActInvoiceAdjudicationPaymentCode".equals(codeString))
      return V3ActCode._ACTINVOICEADJUDICATIONPAYMENTCODE;
    if ("_ActInvoiceAdjudicationPaymentGroupCode".equals(codeString))
      return V3ActCode._ACTINVOICEADJUDICATIONPAYMENTGROUPCODE;
    if ("ALEC".equals(codeString))
      return V3ActCode.ALEC;
    if ("BONUS".equals(codeString))
      return V3ActCode.BONUS;
    if ("CFWD".equals(codeString))
      return V3ActCode.CFWD;
    if ("EDU".equals(codeString))
      return V3ActCode.EDU;
    if ("EPYMT".equals(codeString))
      return V3ActCode.EPYMT;
    if ("GARN".equals(codeString))
      return V3ActCode.GARN;
    if ("INVOICE".equals(codeString))
      return V3ActCode.INVOICE;
    if ("PINV".equals(codeString))
      return V3ActCode.PINV;
    if ("PPRD".equals(codeString))
      return V3ActCode.PPRD;
    if ("PROA".equals(codeString))
      return V3ActCode.PROA;
    if ("RECOV".equals(codeString))
      return V3ActCode.RECOV;
    if ("RETRO".equals(codeString))
      return V3ActCode.RETRO;
    if ("TRAN".equals(codeString))
      return V3ActCode.TRAN;
    if ("_ActInvoiceAdjudicationPaymentSummaryCode".equals(codeString))
      return V3ActCode._ACTINVOICEADJUDICATIONPAYMENTSUMMARYCODE;
    if ("INVTYPE".equals(codeString))
      return V3ActCode.INVTYPE;
    if ("PAYEE".equals(codeString))
      return V3ActCode.PAYEE;
    if ("PAYOR".equals(codeString))
      return V3ActCode.PAYOR;
    if ("SENDAPP".equals(codeString))
      return V3ActCode.SENDAPP;
    if ("_ActInvoiceDetailCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILCODE;
    if ("_ActInvoiceDetailClinicalProductCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILCLINICALPRODUCTCODE;
    if ("UNSPSC".equals(codeString))
      return V3ActCode.UNSPSC;
    if ("_ActInvoiceDetailDrugProductCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILDRUGPRODUCTCODE;
    if ("GTIN".equals(codeString))
      return V3ActCode.GTIN;
    if ("UPC".equals(codeString))
      return V3ActCode.UPC;
    if ("_ActInvoiceDetailGenericCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILGENERICCODE;
    if ("_ActInvoiceDetailGenericAdjudicatorCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILGENERICADJUDICATORCODE;
    if ("COIN".equals(codeString))
      return V3ActCode.COIN;
    if ("COPAYMENT".equals(codeString))
      return V3ActCode.COPAYMENT;
    if ("DEDUCTIBLE".equals(codeString))
      return V3ActCode.DEDUCTIBLE;
    if ("PAY".equals(codeString))
      return V3ActCode.PAY;
    if ("SPEND".equals(codeString))
      return V3ActCode.SPEND;
    if ("COINS".equals(codeString))
      return V3ActCode.COINS;
    if ("_ActInvoiceDetailGenericModifierCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILGENERICMODIFIERCODE;
    if ("AFTHRS".equals(codeString))
      return V3ActCode.AFTHRS;
    if ("ISOL".equals(codeString))
      return V3ActCode.ISOL;
    if ("OOO".equals(codeString))
      return V3ActCode.OOO;
    if ("_ActInvoiceDetailGenericProviderCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILGENERICPROVIDERCODE;
    if ("CANCAPT".equals(codeString))
      return V3ActCode.CANCAPT;
    if ("DSC".equals(codeString))
      return V3ActCode.DSC;
    if ("ESA".equals(codeString))
      return V3ActCode.ESA;
    if ("FFSTOP".equals(codeString))
      return V3ActCode.FFSTOP;
    if ("FNLFEE".equals(codeString))
      return V3ActCode.FNLFEE;
    if ("FRSTFEE".equals(codeString))
      return V3ActCode.FRSTFEE;
    if ("MARKUP".equals(codeString))
      return V3ActCode.MARKUP;
    if ("MISSAPT".equals(codeString))
      return V3ActCode.MISSAPT;
    if ("PERFEE".equals(codeString))
      return V3ActCode.PERFEE;
    if ("PERMBNS".equals(codeString))
      return V3ActCode.PERMBNS;
    if ("RESTOCK".equals(codeString))
      return V3ActCode.RESTOCK;
    if ("TRAVEL".equals(codeString))
      return V3ActCode.TRAVEL;
    if ("URGENT".equals(codeString))
      return V3ActCode.URGENT;
    if ("_ActInvoiceDetailTaxCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILTAXCODE;
    if ("FST".equals(codeString))
      return V3ActCode.FST;
    if ("HST".equals(codeString))
      return V3ActCode.HST;
    if ("PST".equals(codeString))
      return V3ActCode.PST;
    if ("_ActInvoiceDetailPreferredAccommodationCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILPREFERREDACCOMMODATIONCODE;
    if ("_ActEncounterAccommodationCode".equals(codeString))
      return V3ActCode._ACTENCOUNTERACCOMMODATIONCODE;
    if ("_HL7AccommodationCode".equals(codeString))
      return V3ActCode._HL7ACCOMMODATIONCODE;
    if ("I".equals(codeString))
      return V3ActCode.I;
    if ("P".equals(codeString))
      return V3ActCode.P;
    if ("S".equals(codeString))
      return V3ActCode.S;
    if ("SP".equals(codeString))
      return V3ActCode.SP;
    if ("W".equals(codeString))
      return V3ActCode.W;
    if ("_ActInvoiceDetailClinicalServiceCode".equals(codeString))
      return V3ActCode._ACTINVOICEDETAILCLINICALSERVICECODE;
    if ("_ActInvoiceGroupCode".equals(codeString))
      return V3ActCode._ACTINVOICEGROUPCODE;
    if ("_ActInvoiceInterGroupCode".equals(codeString))
      return V3ActCode._ACTINVOICEINTERGROUPCODE;
    if ("CPNDDRGING".equals(codeString))
      return V3ActCode.CPNDDRGING;
    if ("CPNDINDING".equals(codeString))
      return V3ActCode.CPNDINDING;
    if ("CPNDSUPING".equals(codeString))
      return V3ActCode.CPNDSUPING;
    if ("DRUGING".equals(codeString))
      return V3ActCode.DRUGING;
    if ("FRAMEING".equals(codeString))
      return V3ActCode.FRAMEING;
    if ("LENSING".equals(codeString))
      return V3ActCode.LENSING;
    if ("PRDING".equals(codeString))
      return V3ActCode.PRDING;
    if ("_ActInvoiceRootGroupCode".equals(codeString))
      return V3ActCode._ACTINVOICEROOTGROUPCODE;
    if ("CPINV".equals(codeString))
      return V3ActCode.CPINV;
    if ("CSINV".equals(codeString))
      return V3ActCode.CSINV;
    if ("CSPINV".equals(codeString))
      return V3ActCode.CSPINV;
    if ("FININV".equals(codeString))
      return V3ActCode.FININV;
    if ("OHSINV".equals(codeString))
      return V3ActCode.OHSINV;
    if ("PAINV".equals(codeString))
      return V3ActCode.PAINV;
    if ("RXCINV".equals(codeString))
      return V3ActCode.RXCINV;
    if ("RXDINV".equals(codeString))
      return V3ActCode.RXDINV;
    if ("SBFINV".equals(codeString))
      return V3ActCode.SBFINV;
    if ("VRXINV".equals(codeString))
      return V3ActCode.VRXINV;
    if ("_ActInvoiceElementSummaryCode".equals(codeString))
      return V3ActCode._ACTINVOICEELEMENTSUMMARYCODE;
    if ("_InvoiceElementAdjudicated".equals(codeString))
      return V3ActCode._INVOICEELEMENTADJUDICATED;
    if ("ADNFPPELAT".equals(codeString))
      return V3ActCode.ADNFPPELAT;
    if ("ADNFPPELCT".equals(codeString))
      return V3ActCode.ADNFPPELCT;
    if ("ADNFPPMNAT".equals(codeString))
      return V3ActCode.ADNFPPMNAT;
    if ("ADNFPPMNCT".equals(codeString))
      return V3ActCode.ADNFPPMNCT;
    if ("ADNFSPELAT".equals(codeString))
      return V3ActCode.ADNFSPELAT;
    if ("ADNFSPELCT".equals(codeString))
      return V3ActCode.ADNFSPELCT;
    if ("ADNFSPMNAT".equals(codeString))
      return V3ActCode.ADNFSPMNAT;
    if ("ADNFSPMNCT".equals(codeString))
      return V3ActCode.ADNFSPMNCT;
    if ("ADNPPPELAT".equals(codeString))
      return V3ActCode.ADNPPPELAT;
    if ("ADNPPPELCT".equals(codeString))
      return V3ActCode.ADNPPPELCT;
    if ("ADNPPPMNAT".equals(codeString))
      return V3ActCode.ADNPPPMNAT;
    if ("ADNPPPMNCT".equals(codeString))
      return V3ActCode.ADNPPPMNCT;
    if ("ADNPSPELAT".equals(codeString))
      return V3ActCode.ADNPSPELAT;
    if ("ADNPSPELCT".equals(codeString))
      return V3ActCode.ADNPSPELCT;
    if ("ADNPSPMNAT".equals(codeString))
      return V3ActCode.ADNPSPMNAT;
    if ("ADNPSPMNCT".equals(codeString))
      return V3ActCode.ADNPSPMNCT;
    if ("ADPPPPELAT".equals(codeString))
      return V3ActCode.ADPPPPELAT;
    if ("ADPPPPELCT".equals(codeString))
      return V3ActCode.ADPPPPELCT;
    if ("ADPPPPMNAT".equals(codeString))
      return V3ActCode.ADPPPPMNAT;
    if ("ADPPPPMNCT".equals(codeString))
      return V3ActCode.ADPPPPMNCT;
    if ("ADPPSPELAT".equals(codeString))
      return V3ActCode.ADPPSPELAT;
    if ("ADPPSPELCT".equals(codeString))
      return V3ActCode.ADPPSPELCT;
    if ("ADPPSPMNAT".equals(codeString))
      return V3ActCode.ADPPSPMNAT;
    if ("ADPPSPMNCT".equals(codeString))
      return V3ActCode.ADPPSPMNCT;
    if ("ADRFPPELAT".equals(codeString))
      return V3ActCode.ADRFPPELAT;
    if ("ADRFPPELCT".equals(codeString))
      return V3ActCode.ADRFPPELCT;
    if ("ADRFPPMNAT".equals(codeString))
      return V3ActCode.ADRFPPMNAT;
    if ("ADRFPPMNCT".equals(codeString))
      return V3ActCode.ADRFPPMNCT;
    if ("ADRFSPELAT".equals(codeString))
      return V3ActCode.ADRFSPELAT;
    if ("ADRFSPELCT".equals(codeString))
      return V3ActCode.ADRFSPELCT;
    if ("ADRFSPMNAT".equals(codeString))
      return V3ActCode.ADRFSPMNAT;
    if ("ADRFSPMNCT".equals(codeString))
      return V3ActCode.ADRFSPMNCT;
    if ("_InvoiceElementPaid".equals(codeString))
      return V3ActCode._INVOICEELEMENTPAID;
    if ("PDNFPPELAT".equals(codeString))
      return V3ActCode.PDNFPPELAT;
    if ("PDNFPPELCT".equals(codeString))
      return V3ActCode.PDNFPPELCT;
    if ("PDNFPPMNAT".equals(codeString))
      return V3ActCode.PDNFPPMNAT;
    if ("PDNFPPMNCT".equals(codeString))
      return V3ActCode.PDNFPPMNCT;
    if ("PDNFSPELAT".equals(codeString))
      return V3ActCode.PDNFSPELAT;
    if ("PDNFSPELCT".equals(codeString))
      return V3ActCode.PDNFSPELCT;
    if ("PDNFSPMNAT".equals(codeString))
      return V3ActCode.PDNFSPMNAT;
    if ("PDNFSPMNCT".equals(codeString))
      return V3ActCode.PDNFSPMNCT;
    if ("PDNPPPELAT".equals(codeString))
      return V3ActCode.PDNPPPELAT;
    if ("PDNPPPELCT".equals(codeString))
      return V3ActCode.PDNPPPELCT;
    if ("PDNPPPMNAT".equals(codeString))
      return V3ActCode.PDNPPPMNAT;
    if ("PDNPPPMNCT".equals(codeString))
      return V3ActCode.PDNPPPMNCT;
    if ("PDNPSPELAT".equals(codeString))
      return V3ActCode.PDNPSPELAT;
    if ("PDNPSPELCT".equals(codeString))
      return V3ActCode.PDNPSPELCT;
    if ("PDNPSPMNAT".equals(codeString))
      return V3ActCode.PDNPSPMNAT;
    if ("PDNPSPMNCT".equals(codeString))
      return V3ActCode.PDNPSPMNCT;
    if ("PDPPPPELAT".equals(codeString))
      return V3ActCode.PDPPPPELAT;
    if ("PDPPPPELCT".equals(codeString))
      return V3ActCode.PDPPPPELCT;
    if ("PDPPPPMNAT".equals(codeString))
      return V3ActCode.PDPPPPMNAT;
    if ("PDPPPPMNCT".equals(codeString))
      return V3ActCode.PDPPPPMNCT;
    if ("PDPPSPELAT".equals(codeString))
      return V3ActCode.PDPPSPELAT;
    if ("PDPPSPELCT".equals(codeString))
      return V3ActCode.PDPPSPELCT;
    if ("PDPPSPMNAT".equals(codeString))
      return V3ActCode.PDPPSPMNAT;
    if ("PDPPSPMNCT".equals(codeString))
      return V3ActCode.PDPPSPMNCT;
    if ("_InvoiceElementSubmitted".equals(codeString))
      return V3ActCode._INVOICEELEMENTSUBMITTED;
    if ("SBBLELAT".equals(codeString))
      return V3ActCode.SBBLELAT;
    if ("SBBLELCT".equals(codeString))
      return V3ActCode.SBBLELCT;
    if ("SBNFELAT".equals(codeString))
      return V3ActCode.SBNFELAT;
    if ("SBNFELCT".equals(codeString))
      return V3ActCode.SBNFELCT;
    if ("SBPDELAT".equals(codeString))
      return V3ActCode.SBPDELAT;
    if ("SBPDELCT".equals(codeString))
      return V3ActCode.SBPDELCT;
    if ("_ActInvoiceOverrideCode".equals(codeString))
      return V3ActCode._ACTINVOICEOVERRIDECODE;
    if ("COVGE".equals(codeString))
      return V3ActCode.COVGE;
    if ("EFORM".equals(codeString))
      return V3ActCode.EFORM;
    if ("FAX".equals(codeString))
      return V3ActCode.FAX;
    if ("GFTH".equals(codeString))
      return V3ActCode.GFTH;
    if ("LATE".equals(codeString))
      return V3ActCode.LATE;
    if ("MANUAL".equals(codeString))
      return V3ActCode.MANUAL;
    if ("OOJ".equals(codeString))
      return V3ActCode.OOJ;
    if ("ORTHO".equals(codeString))
      return V3ActCode.ORTHO;
    if ("PAPER".equals(codeString))
      return V3ActCode.PAPER;
    if ("PIE".equals(codeString))
      return V3ActCode.PIE;
    if ("PYRDELAY".equals(codeString))
      return V3ActCode.PYRDELAY;
    if ("REFNR".equals(codeString))
      return V3ActCode.REFNR;
    if ("REPSERV".equals(codeString))
      return V3ActCode.REPSERV;
    if ("UNRELAT".equals(codeString))
      return V3ActCode.UNRELAT;
    if ("VERBAUTH".equals(codeString))
      return V3ActCode.VERBAUTH;
    if ("_ActListCode".equals(codeString))
      return V3ActCode._ACTLISTCODE;
    if ("_ActObservationList".equals(codeString))
      return V3ActCode._ACTOBSERVATIONLIST;
    if ("CARELIST".equals(codeString))
      return V3ActCode.CARELIST;
    if ("CONDLIST".equals(codeString))
      return V3ActCode.CONDLIST;
    if ("INTOLIST".equals(codeString))
      return V3ActCode.INTOLIST;
    if ("PROBLIST".equals(codeString))
      return V3ActCode.PROBLIST;
    if ("RISKLIST".equals(codeString))
      return V3ActCode.RISKLIST;
    if ("GOALLIST".equals(codeString))
      return V3ActCode.GOALLIST;
    if ("_ActTherapyDurationWorkingListCode".equals(codeString))
      return V3ActCode._ACTTHERAPYDURATIONWORKINGLISTCODE;
    if ("_ActMedicationTherapyDurationWorkingListCode".equals(codeString))
      return V3ActCode._ACTMEDICATIONTHERAPYDURATIONWORKINGLISTCODE;
    if ("ACU".equals(codeString))
      return V3ActCode.ACU;
    if ("CHRON".equals(codeString))
      return V3ActCode.CHRON;
    if ("ONET".equals(codeString))
      return V3ActCode.ONET;
    if ("PRN".equals(codeString))
      return V3ActCode.PRN;
    if ("MEDLIST".equals(codeString))
      return V3ActCode.MEDLIST;
    if ("CURMEDLIST".equals(codeString))
      return V3ActCode.CURMEDLIST;
    if ("DISCMEDLIST".equals(codeString))
      return V3ActCode.DISCMEDLIST;
    if ("HISTMEDLIST".equals(codeString))
      return V3ActCode.HISTMEDLIST;
    if ("_ActMonitoringProtocolCode".equals(codeString))
      return V3ActCode._ACTMONITORINGPROTOCOLCODE;
    if ("CTLSUB".equals(codeString))
      return V3ActCode.CTLSUB;
    if ("INV".equals(codeString))
      return V3ActCode.INV;
    if ("LU".equals(codeString))
      return V3ActCode.LU;
    if ("OTC".equals(codeString))
      return V3ActCode.OTC;
    if ("RX".equals(codeString))
      return V3ActCode.RX;
    if ("SA".equals(codeString))
      return V3ActCode.SA;
    if ("SAC".equals(codeString))
      return V3ActCode.SAC;
    if ("_ActNonObservationIndicationCode".equals(codeString))
      return V3ActCode._ACTNONOBSERVATIONINDICATIONCODE;
    if ("IND01".equals(codeString))
      return V3ActCode.IND01;
    if ("IND02".equals(codeString))
      return V3ActCode.IND02;
    if ("IND03".equals(codeString))
      return V3ActCode.IND03;
    if ("IND04".equals(codeString))
      return V3ActCode.IND04;
    if ("IND05".equals(codeString))
      return V3ActCode.IND05;
    if ("_ActObservationVerificationType".equals(codeString))
      return V3ActCode._ACTOBSERVATIONVERIFICATIONTYPE;
    if ("VFPAPER".equals(codeString))
      return V3ActCode.VFPAPER;
    if ("_ActPaymentCode".equals(codeString))
      return V3ActCode._ACTPAYMENTCODE;
    if ("ACH".equals(codeString))
      return V3ActCode.ACH;
    if ("CHK".equals(codeString))
      return V3ActCode.CHK;
    if ("DDP".equals(codeString))
      return V3ActCode.DDP;
    if ("NON".equals(codeString))
      return V3ActCode.NON;
    if ("_ActPharmacySupplyType".equals(codeString))
      return V3ActCode._ACTPHARMACYSUPPLYTYPE;
    if ("DF".equals(codeString))
      return V3ActCode.DF;
    if ("EM".equals(codeString))
      return V3ActCode.EM;
    if ("SO".equals(codeString))
      return V3ActCode.SO;
    if ("FF".equals(codeString))
      return V3ActCode.FF;
    if ("FFC".equals(codeString))
      return V3ActCode.FFC;
    if ("FFP".equals(codeString))
      return V3ActCode.FFP;
    if ("FFSS".equals(codeString))
      return V3ActCode.FFSS;
    if ("TF".equals(codeString))
      return V3ActCode.TF;
    if ("FS".equals(codeString))
      return V3ActCode.FS;
    if ("MS".equals(codeString))
      return V3ActCode.MS;
    if ("RF".equals(codeString))
      return V3ActCode.RF;
    if ("UD".equals(codeString))
      return V3ActCode.UD;
    if ("RFC".equals(codeString))
      return V3ActCode.RFC;
    if ("RFCS".equals(codeString))
      return V3ActCode.RFCS;
    if ("RFF".equals(codeString))
      return V3ActCode.RFF;
    if ("RFFS".equals(codeString))
      return V3ActCode.RFFS;
    if ("RFP".equals(codeString))
      return V3ActCode.RFP;
    if ("RFPS".equals(codeString))
      return V3ActCode.RFPS;
    if ("RFS".equals(codeString))
      return V3ActCode.RFS;
    if ("TB".equals(codeString))
      return V3ActCode.TB;
    if ("TBS".equals(codeString))
      return V3ActCode.TBS;
    if ("UDE".equals(codeString))
      return V3ActCode.UDE;
    if ("_ActPolicyType".equals(codeString))
      return V3ActCode._ACTPOLICYTYPE;
    if ("_ActPrivacyPolicy".equals(codeString))
      return V3ActCode._ACTPRIVACYPOLICY;
    if ("_ActConsentDirective".equals(codeString))
      return V3ActCode._ACTCONSENTDIRECTIVE;
    if ("EMRGONLY".equals(codeString))
      return V3ActCode.EMRGONLY;
    if ("NOPP".equals(codeString))
      return V3ActCode.NOPP;
    if ("OPTIN".equals(codeString))
      return V3ActCode.OPTIN;
    if ("OPTOUT".equals(codeString))
      return V3ActCode.OPTOUT;
    if ("_InformationSensitivityPolicy".equals(codeString))
      return V3ActCode._INFORMATIONSENSITIVITYPOLICY;
    if ("_ActInformationSensitivityPolicy".equals(codeString))
      return V3ActCode._ACTINFORMATIONSENSITIVITYPOLICY;
    if ("ETH".equals(codeString))
      return V3ActCode.ETH;
    if ("GDIS".equals(codeString))
      return V3ActCode.GDIS;
    if ("HIV".equals(codeString))
      return V3ActCode.HIV;
    if ("PSY".equals(codeString))
      return V3ActCode.PSY;
    if ("SCA".equals(codeString))
      return V3ActCode.SCA;
    if ("SDV".equals(codeString))
      return V3ActCode.SDV;
    if ("SEX".equals(codeString))
      return V3ActCode.SEX;
    if ("STD".equals(codeString))
      return V3ActCode.STD;
    if ("TBOO".equals(codeString))
      return V3ActCode.TBOO;
    if ("SICKLE".equals(codeString))
      return V3ActCode.SICKLE;
    if ("_EntitySensitivityPolicyType".equals(codeString))
      return V3ActCode._ENTITYSENSITIVITYPOLICYTYPE;
    if ("DEMO".equals(codeString))
      return V3ActCode.DEMO;
    if ("DOB".equals(codeString))
      return V3ActCode.DOB;
    if ("GENDER".equals(codeString))
      return V3ActCode.GENDER;
    if ("LIVARG".equals(codeString))
      return V3ActCode.LIVARG;
    if ("MARST".equals(codeString))
      return V3ActCode.MARST;
    if ("RACE".equals(codeString))
      return V3ActCode.RACE;
    if ("REL".equals(codeString))
      return V3ActCode.REL;
    if ("_RoleInformationSensitivityPolicy".equals(codeString))
      return V3ActCode._ROLEINFORMATIONSENSITIVITYPOLICY;
    if ("B".equals(codeString))
      return V3ActCode.B;
    if ("EMPL".equals(codeString))
      return V3ActCode.EMPL;
    if ("LOCIS".equals(codeString))
      return V3ActCode.LOCIS;
    if ("SSP".equals(codeString))
      return V3ActCode.SSP;
    if ("ADOL".equals(codeString))
      return V3ActCode.ADOL;
    if ("CEL".equals(codeString))
      return V3ActCode.CEL;
    if ("DIA".equals(codeString))
      return V3ActCode.DIA;
    if ("DRGIS".equals(codeString))
      return V3ActCode.DRGIS;
    if ("EMP".equals(codeString))
      return V3ActCode.EMP;
    if ("PDS".equals(codeString))
      return V3ActCode.PDS;
    if ("PRS".equals(codeString))
      return V3ActCode.PRS;
    if ("COMPT".equals(codeString))
      return V3ActCode.COMPT;
    if ("HRCOMPT".equals(codeString))
      return V3ActCode.HRCOMPT;
    if ("RESCOMPT".equals(codeString))
      return V3ActCode.RESCOMPT;
    if ("RMGTCOMPT".equals(codeString))
      return V3ActCode.RMGTCOMPT;
    if ("ActTrustPolicyType".equals(codeString))
      return V3ActCode.ACTTRUSTPOLICYTYPE;
    if ("TRSTACCRD".equals(codeString))
      return V3ActCode.TRSTACCRD;
    if ("TRSTAGRE".equals(codeString))
      return V3ActCode.TRSTAGRE;
    if ("TRSTASSUR".equals(codeString))
      return V3ActCode.TRSTASSUR;
    if ("TRSTCERT".equals(codeString))
      return V3ActCode.TRSTCERT;
    if ("TRSTFWK".equals(codeString))
      return V3ActCode.TRSTFWK;
    if ("TRSTMEC".equals(codeString))
      return V3ActCode.TRSTMEC;
    if ("COVPOL".equals(codeString))
      return V3ActCode.COVPOL;
    if ("SecurityPolicy".equals(codeString))
      return V3ActCode.SECURITYPOLICY;
    if ("ObligationPolicy".equals(codeString))
      return V3ActCode.OBLIGATIONPOLICY;
    if ("ANONY".equals(codeString))
      return V3ActCode.ANONY;
    if ("AOD".equals(codeString))
      return V3ActCode.AOD;
    if ("AUDIT".equals(codeString))
      return V3ActCode.AUDIT;
    if ("AUDTR".equals(codeString))
      return V3ActCode.AUDTR;
    if ("CPLYCC".equals(codeString))
      return V3ActCode.CPLYCC;
    if ("CPLYCD".equals(codeString))
      return V3ActCode.CPLYCD;
    if ("CPLYJPP".equals(codeString))
      return V3ActCode.CPLYJPP;
    if ("CPLYOPP".equals(codeString))
      return V3ActCode.CPLYOPP;
    if ("CPLYOSP".equals(codeString))
      return V3ActCode.CPLYOSP;
    if ("CPLYPOL".equals(codeString))
      return V3ActCode.CPLYPOL;
    if ("DEID".equals(codeString))
      return V3ActCode.DEID;
    if ("DELAU".equals(codeString))
      return V3ActCode.DELAU;
    if ("ENCRYPT".equals(codeString))
      return V3ActCode.ENCRYPT;
    if ("ENCRYPTR".equals(codeString))
      return V3ActCode.ENCRYPTR;
    if ("ENCRYPTT".equals(codeString))
      return V3ActCode.ENCRYPTT;
    if ("ENCRYPTU".equals(codeString))
      return V3ActCode.ENCRYPTU;
    if ("HUAPRV".equals(codeString))
      return V3ActCode.HUAPRV;
    if ("MASK".equals(codeString))
      return V3ActCode.MASK;
    if ("MINEC".equals(codeString))
      return V3ActCode.MINEC;
    if ("PRIVMARK".equals(codeString))
      return V3ActCode.PRIVMARK;
    if ("PSEUD".equals(codeString))
      return V3ActCode.PSEUD;
    if ("REDACT".equals(codeString))
      return V3ActCode.REDACT;
    if ("RefrainPolicy".equals(codeString))
      return V3ActCode.REFRAINPOLICY;
    if ("NOAUTH".equals(codeString))
      return V3ActCode.NOAUTH;
    if ("NOCOLLECT".equals(codeString))
      return V3ActCode.NOCOLLECT;
    if ("NODSCLCD".equals(codeString))
      return V3ActCode.NODSCLCD;
    if ("NODSCLCDS".equals(codeString))
      return V3ActCode.NODSCLCDS;
    if ("NOINTEGRATE".equals(codeString))
      return V3ActCode.NOINTEGRATE;
    if ("NOLIST".equals(codeString))
      return V3ActCode.NOLIST;
    if ("NOMOU".equals(codeString))
      return V3ActCode.NOMOU;
    if ("NOORGPOL".equals(codeString))
      return V3ActCode.NOORGPOL;
    if ("NOPAT".equals(codeString))
      return V3ActCode.NOPAT;
    if ("NOPERSISTP".equals(codeString))
      return V3ActCode.NOPERSISTP;
    if ("NORDSCLCD".equals(codeString))
      return V3ActCode.NORDSCLCD;
    if ("NORDSCLCDS".equals(codeString))
      return V3ActCode.NORDSCLCDS;
    if ("NORDSCLW".equals(codeString))
      return V3ActCode.NORDSCLW;
    if ("NORELINK".equals(codeString))
      return V3ActCode.NORELINK;
    if ("NOREUSE".equals(codeString))
      return V3ActCode.NOREUSE;
    if ("NOVIP".equals(codeString))
      return V3ActCode.NOVIP;
    if ("ORCON".equals(codeString))
      return V3ActCode.ORCON;
    if ("_ActProductAcquisitionCode".equals(codeString))
      return V3ActCode._ACTPRODUCTACQUISITIONCODE;
    if ("LOAN".equals(codeString))
      return V3ActCode.LOAN;
    if ("RENT".equals(codeString))
      return V3ActCode.RENT;
    if ("TRANSFER".equals(codeString))
      return V3ActCode.TRANSFER;
    if ("SALE".equals(codeString))
      return V3ActCode.SALE;
    if ("_ActSpecimenTransportCode".equals(codeString))
      return V3ActCode._ACTSPECIMENTRANSPORTCODE;
    if ("SREC".equals(codeString))
      return V3ActCode.SREC;
    if ("SSTOR".equals(codeString))
      return V3ActCode.SSTOR;
    if ("STRAN".equals(codeString))
      return V3ActCode.STRAN;
    if ("_ActSpecimenTreatmentCode".equals(codeString))
      return V3ActCode._ACTSPECIMENTREATMENTCODE;
    if ("ACID".equals(codeString))
      return V3ActCode.ACID;
    if ("ALK".equals(codeString))
      return V3ActCode.ALK;
    if ("DEFB".equals(codeString))
      return V3ActCode.DEFB;
    if ("FILT".equals(codeString))
      return V3ActCode.FILT;
    if ("LDLP".equals(codeString))
      return V3ActCode.LDLP;
    if ("NEUT".equals(codeString))
      return V3ActCode.NEUT;
    if ("RECA".equals(codeString))
      return V3ActCode.RECA;
    if ("UFIL".equals(codeString))
      return V3ActCode.UFIL;
    if ("_ActSubstanceAdministrationCode".equals(codeString))
      return V3ActCode._ACTSUBSTANCEADMINISTRATIONCODE;
    if ("DRUG".equals(codeString))
      return V3ActCode.DRUG;
    if ("FD".equals(codeString))
      return V3ActCode.FD;
    if ("IMMUNIZ".equals(codeString))
      return V3ActCode.IMMUNIZ;
    if ("BOOSTER".equals(codeString))
      return V3ActCode.BOOSTER;
    if ("INITIMMUNIZ".equals(codeString))
      return V3ActCode.INITIMMUNIZ;
    if ("_ActTaskCode".equals(codeString))
      return V3ActCode._ACTTASKCODE;
    if ("OE".equals(codeString))
      return V3ActCode.OE;
    if ("LABOE".equals(codeString))
      return V3ActCode.LABOE;
    if ("MEDOE".equals(codeString))
      return V3ActCode.MEDOE;
    if ("PATDOC".equals(codeString))
      return V3ActCode.PATDOC;
    if ("ALLERLREV".equals(codeString))
      return V3ActCode.ALLERLREV;
    if ("CLINNOTEE".equals(codeString))
      return V3ActCode.CLINNOTEE;
    if ("DIAGLISTE".equals(codeString))
      return V3ActCode.DIAGLISTE;
    if ("DISCHINSTE".equals(codeString))
      return V3ActCode.DISCHINSTE;
    if ("DISCHSUME".equals(codeString))
      return V3ActCode.DISCHSUME;
    if ("PATEDUE".equals(codeString))
      return V3ActCode.PATEDUE;
    if ("PATREPE".equals(codeString))
      return V3ActCode.PATREPE;
    if ("PROBLISTE".equals(codeString))
      return V3ActCode.PROBLISTE;
    if ("RADREPE".equals(codeString))
      return V3ActCode.RADREPE;
    if ("IMMLREV".equals(codeString))
      return V3ActCode.IMMLREV;
    if ("REMLREV".equals(codeString))
      return V3ActCode.REMLREV;
    if ("WELLREMLREV".equals(codeString))
      return V3ActCode.WELLREMLREV;
    if ("PATINFO".equals(codeString))
      return V3ActCode.PATINFO;
    if ("ALLERLE".equals(codeString))
      return V3ActCode.ALLERLE;
    if ("CDSREV".equals(codeString))
      return V3ActCode.CDSREV;
    if ("CLINNOTEREV".equals(codeString))
      return V3ActCode.CLINNOTEREV;
    if ("DISCHSUMREV".equals(codeString))
      return V3ActCode.DISCHSUMREV;
    if ("DIAGLISTREV".equals(codeString))
      return V3ActCode.DIAGLISTREV;
    if ("IMMLE".equals(codeString))
      return V3ActCode.IMMLE;
    if ("LABRREV".equals(codeString))
      return V3ActCode.LABRREV;
    if ("MICRORREV".equals(codeString))
      return V3ActCode.MICRORREV;
    if ("MICROORGRREV".equals(codeString))
      return V3ActCode.MICROORGRREV;
    if ("MICROSENSRREV".equals(codeString))
      return V3ActCode.MICROSENSRREV;
    if ("MLREV".equals(codeString))
      return V3ActCode.MLREV;
    if ("MARWLREV".equals(codeString))
      return V3ActCode.MARWLREV;
    if ("OREV".equals(codeString))
      return V3ActCode.OREV;
    if ("PATREPREV".equals(codeString))
      return V3ActCode.PATREPREV;
    if ("PROBLISTREV".equals(codeString))
      return V3ActCode.PROBLISTREV;
    if ("RADREPREV".equals(codeString))
      return V3ActCode.RADREPREV;
    if ("REMLE".equals(codeString))
      return V3ActCode.REMLE;
    if ("WELLREMLE".equals(codeString))
      return V3ActCode.WELLREMLE;
    if ("RISKASSESS".equals(codeString))
      return V3ActCode.RISKASSESS;
    if ("FALLRISK".equals(codeString))
      return V3ActCode.FALLRISK;
    if ("_ActTransportationModeCode".equals(codeString))
      return V3ActCode._ACTTRANSPORTATIONMODECODE;
    if ("_ActPatientTransportationModeCode".equals(codeString))
      return V3ActCode._ACTPATIENTTRANSPORTATIONMODECODE;
    if ("AFOOT".equals(codeString))
      return V3ActCode.AFOOT;
    if ("AMBT".equals(codeString))
      return V3ActCode.AMBT;
    if ("AMBAIR".equals(codeString))
      return V3ActCode.AMBAIR;
    if ("AMBGRND".equals(codeString))
      return V3ActCode.AMBGRND;
    if ("AMBHELO".equals(codeString))
      return V3ActCode.AMBHELO;
    if ("LAWENF".equals(codeString))
      return V3ActCode.LAWENF;
    if ("PRVTRN".equals(codeString))
      return V3ActCode.PRVTRN;
    if ("PUBTRN".equals(codeString))
      return V3ActCode.PUBTRN;
    if ("_ObservationType".equals(codeString))
      return V3ActCode._OBSERVATIONTYPE;
    if ("_ActSpecObsCode".equals(codeString))
      return V3ActCode._ACTSPECOBSCODE;
    if ("ARTBLD".equals(codeString))
      return V3ActCode.ARTBLD;
    if ("DILUTION".equals(codeString))
      return V3ActCode.DILUTION;
    if ("AUTO-HIGH".equals(codeString))
      return V3ActCode.AUTOHIGH;
    if ("AUTO-LOW".equals(codeString))
      return V3ActCode.AUTOLOW;
    if ("PRE".equals(codeString))
      return V3ActCode.PRE;
    if ("RERUN".equals(codeString))
      return V3ActCode.RERUN;
    if ("EVNFCTS".equals(codeString))
      return V3ActCode.EVNFCTS;
    if ("INTFR".equals(codeString))
      return V3ActCode.INTFR;
    if ("FIBRIN".equals(codeString))
      return V3ActCode.FIBRIN;
    if ("HEMOLYSIS".equals(codeString))
      return V3ActCode.HEMOLYSIS;
    if ("ICTERUS".equals(codeString))
      return V3ActCode.ICTERUS;
    if ("LIPEMIA".equals(codeString))
      return V3ActCode.LIPEMIA;
    if ("VOLUME".equals(codeString))
      return V3ActCode.VOLUME;
    if ("AVAILABLE".equals(codeString))
      return V3ActCode.AVAILABLE;
    if ("CONSUMPTION".equals(codeString))
      return V3ActCode.CONSUMPTION;
    if ("CURRENT".equals(codeString))
      return V3ActCode.CURRENT;
    if ("INITIAL".equals(codeString))
      return V3ActCode.INITIAL;
    if ("_AnnotationType".equals(codeString))
      return V3ActCode._ANNOTATIONTYPE;
    if ("_ActPatientAnnotationType".equals(codeString))
      return V3ActCode._ACTPATIENTANNOTATIONTYPE;
    if ("ANNDI".equals(codeString))
      return V3ActCode.ANNDI;
    if ("ANNGEN".equals(codeString))
      return V3ActCode.ANNGEN;
    if ("ANNIMM".equals(codeString))
      return V3ActCode.ANNIMM;
    if ("ANNLAB".equals(codeString))
      return V3ActCode.ANNLAB;
    if ("ANNMED".equals(codeString))
      return V3ActCode.ANNMED;
    if ("_GeneticObservationType".equals(codeString))
      return V3ActCode._GENETICOBSERVATIONTYPE;
    if ("GENE".equals(codeString))
      return V3ActCode.GENE;
    if ("_ImmunizationObservationType".equals(codeString))
      return V3ActCode._IMMUNIZATIONOBSERVATIONTYPE;
    if ("OBSANTC".equals(codeString))
      return V3ActCode.OBSANTC;
    if ("OBSANTV".equals(codeString))
      return V3ActCode.OBSANTV;
    if ("_IndividualCaseSafetyReportType".equals(codeString))
      return V3ActCode._INDIVIDUALCASESAFETYREPORTTYPE;
    if ("PAT_ADV_EVNT".equals(codeString))
      return V3ActCode.PATADVEVNT;
    if ("VAC_PROBLEM".equals(codeString))
      return V3ActCode.VACPROBLEM;
    if ("_LOINCObservationActContextAgeType".equals(codeString))
      return V3ActCode._LOINCOBSERVATIONACTCONTEXTAGETYPE;
    if ("21611-9".equals(codeString))
      return V3ActCode._216119;
    if ("21612-7".equals(codeString))
      return V3ActCode._216127;
    if ("29553-5".equals(codeString))
      return V3ActCode._295535;
    if ("30525-0".equals(codeString))
      return V3ActCode._305250;
    if ("30972-4".equals(codeString))
      return V3ActCode._309724;
    if ("_MedicationObservationType".equals(codeString))
      return V3ActCode._MEDICATIONOBSERVATIONTYPE;
    if ("REP_HALF_LIFE".equals(codeString))
      return V3ActCode.REPHALFLIFE;
    if ("SPLCOATING".equals(codeString))
      return V3ActCode.SPLCOATING;
    if ("SPLCOLOR".equals(codeString))
      return V3ActCode.SPLCOLOR;
    if ("SPLIMAGE".equals(codeString))
      return V3ActCode.SPLIMAGE;
    if ("SPLIMPRINT".equals(codeString))
      return V3ActCode.SPLIMPRINT;
    if ("SPLSCORING".equals(codeString))
      return V3ActCode.SPLSCORING;
    if ("SPLSHAPE".equals(codeString))
      return V3ActCode.SPLSHAPE;
    if ("SPLSIZE".equals(codeString))
      return V3ActCode.SPLSIZE;
    if ("SPLSYMBOL".equals(codeString))
      return V3ActCode.SPLSYMBOL;
    if ("_ObservationIssueTriggerCodedObservationType".equals(codeString))
      return V3ActCode._OBSERVATIONISSUETRIGGERCODEDOBSERVATIONTYPE;
    if ("_CaseTransmissionMode".equals(codeString))
      return V3ActCode._CASETRANSMISSIONMODE;
    if ("AIRTRNS".equals(codeString))
      return V3ActCode.AIRTRNS;
    if ("ANANTRNS".equals(codeString))
      return V3ActCode.ANANTRNS;
    if ("ANHUMTRNS".equals(codeString))
      return V3ActCode.ANHUMTRNS;
    if ("BDYFLDTRNS".equals(codeString))
      return V3ActCode.BDYFLDTRNS;
    if ("BLDTRNS".equals(codeString))
      return V3ActCode.BLDTRNS;
    if ("DERMTRNS".equals(codeString))
      return V3ActCode.DERMTRNS;
    if ("ENVTRNS".equals(codeString))
      return V3ActCode.ENVTRNS;
    if ("FECTRNS".equals(codeString))
      return V3ActCode.FECTRNS;
    if ("FOMTRNS".equals(codeString))
      return V3ActCode.FOMTRNS;
    if ("FOODTRNS".equals(codeString))
      return V3ActCode.FOODTRNS;
    if ("HUMHUMTRNS".equals(codeString))
      return V3ActCode.HUMHUMTRNS;
    if ("INDTRNS".equals(codeString))
      return V3ActCode.INDTRNS;
    if ("LACTTRNS".equals(codeString))
      return V3ActCode.LACTTRNS;
    if ("NOSTRNS".equals(codeString))
      return V3ActCode.NOSTRNS;
    if ("PARTRNS".equals(codeString))
      return V3ActCode.PARTRNS;
    if ("PLACTRNS".equals(codeString))
      return V3ActCode.PLACTRNS;
    if ("SEXTRNS".equals(codeString))
      return V3ActCode.SEXTRNS;
    if ("TRNSFTRNS".equals(codeString))
      return V3ActCode.TRNSFTRNS;
    if ("VECTRNS".equals(codeString))
      return V3ActCode.VECTRNS;
    if ("WATTRNS".equals(codeString))
      return V3ActCode.WATTRNS;
    if ("_ObservationQualityMeasureAttribute".equals(codeString))
      return V3ActCode._OBSERVATIONQUALITYMEASUREATTRIBUTE;
    if ("AGGREGATE".equals(codeString))
      return V3ActCode.AGGREGATE;
    if ("COPY".equals(codeString))
      return V3ActCode.COPY;
    if ("CRS".equals(codeString))
      return V3ActCode.CRS;
    if ("DEF".equals(codeString))
      return V3ActCode.DEF;
    if ("DISC".equals(codeString))
      return V3ActCode.DISC;
    if ("FINALDT".equals(codeString))
      return V3ActCode.FINALDT;
    if ("GUIDE".equals(codeString))
      return V3ActCode.GUIDE;
    if ("IDUR".equals(codeString))
      return V3ActCode.IDUR;
    if ("ITMCNT".equals(codeString))
      return V3ActCode.ITMCNT;
    if ("KEY".equals(codeString))
      return V3ActCode.KEY;
    if ("MEDT".equals(codeString))
      return V3ActCode.MEDT;
    if ("MSD".equals(codeString))
      return V3ActCode.MSD;
    if ("MSRADJ".equals(codeString))
      return V3ActCode.MSRADJ;
    if ("MSRAGG".equals(codeString))
      return V3ActCode.MSRAGG;
    if ("MSRIMPROV".equals(codeString))
      return V3ActCode.MSRIMPROV;
    if ("MSRJUR".equals(codeString))
      return V3ActCode.MSRJUR;
    if ("MSRRPTR".equals(codeString))
      return V3ActCode.MSRRPTR;
    if ("MSRRPTTIME".equals(codeString))
      return V3ActCode.MSRRPTTIME;
    if ("MSRSCORE".equals(codeString))
      return V3ActCode.MSRSCORE;
    if ("MSRSET".equals(codeString))
      return V3ActCode.MSRSET;
    if ("MSRTOPIC".equals(codeString))
      return V3ActCode.MSRTOPIC;
    if ("MSRTP".equals(codeString))
      return V3ActCode.MSRTP;
    if ("MSRTYPE".equals(codeString))
      return V3ActCode.MSRTYPE;
    if ("RAT".equals(codeString))
      return V3ActCode.RAT;
    if ("REF".equals(codeString))
      return V3ActCode.REF;
    if ("SDE".equals(codeString))
      return V3ActCode.SDE;
    if ("STRAT".equals(codeString))
      return V3ActCode.STRAT;
    if ("TRANF".equals(codeString))
      return V3ActCode.TRANF;
    if ("USE".equals(codeString))
      return V3ActCode.USE;
    if ("_ObservationSequenceType".equals(codeString))
      return V3ActCode._OBSERVATIONSEQUENCETYPE;
    if ("TIME_ABSOLUTE".equals(codeString))
      return V3ActCode.TIMEABSOLUTE;
    if ("TIME_RELATIVE".equals(codeString))
      return V3ActCode.TIMERELATIVE;
    if ("_ObservationSeriesType".equals(codeString))
      return V3ActCode._OBSERVATIONSERIESTYPE;
    if ("_ECGObservationSeriesType".equals(codeString))
      return V3ActCode._ECGOBSERVATIONSERIESTYPE;
    if ("REPRESENTATIVE_BEAT".equals(codeString))
      return V3ActCode.REPRESENTATIVEBEAT;
    if ("RHYTHM".equals(codeString))
      return V3ActCode.RHYTHM;
    if ("_PatientImmunizationRelatedObservationType".equals(codeString))
      return V3ActCode._PATIENTIMMUNIZATIONRELATEDOBSERVATIONTYPE;
    if ("CLSSRM".equals(codeString))
      return V3ActCode.CLSSRM;
    if ("GRADE".equals(codeString))
      return V3ActCode.GRADE;
    if ("SCHL".equals(codeString))
      return V3ActCode.SCHL;
    if ("SCHLDIV".equals(codeString))
      return V3ActCode.SCHLDIV;
    if ("TEACHER".equals(codeString))
      return V3ActCode.TEACHER;
    if ("_PopulationInclusionObservationType".equals(codeString))
      return V3ActCode._POPULATIONINCLUSIONOBSERVATIONTYPE;
    if ("DENEX".equals(codeString))
      return V3ActCode.DENEX;
    if ("DENEXCEP".equals(codeString))
      return V3ActCode.DENEXCEP;
    if ("DENOM".equals(codeString))
      return V3ActCode.DENOM;
    if ("IPOP".equals(codeString))
      return V3ActCode.IPOP;
    if ("IPPOP".equals(codeString))
      return V3ActCode.IPPOP;
    if ("MSRPOPL".equals(codeString))
      return V3ActCode.MSRPOPL;
    if ("MSRPOPLEX".equals(codeString))
      return V3ActCode.MSRPOPLEX;
    if ("NUMER".equals(codeString))
      return V3ActCode.NUMER;
    if ("NUMEX".equals(codeString))
      return V3ActCode.NUMEX;
    if ("_PreferenceObservationType".equals(codeString))
      return V3ActCode._PREFERENCEOBSERVATIONTYPE;
    if ("PREFSTRENGTH".equals(codeString))
      return V3ActCode.PREFSTRENGTH;
    if ("ADVERSE_REACTION".equals(codeString))
      return V3ActCode.ADVERSEREACTION;
    if ("ASSERTION".equals(codeString))
      return V3ActCode.ASSERTION;
    if ("CASESER".equals(codeString))
      return V3ActCode.CASESER;
    if ("CDIO".equals(codeString))
      return V3ActCode.CDIO;
    if ("CRIT".equals(codeString))
      return V3ActCode.CRIT;
    if ("CTMO".equals(codeString))
      return V3ActCode.CTMO;
    if ("DX".equals(codeString))
      return V3ActCode.DX;
    if ("ADMDX".equals(codeString))
      return V3ActCode.ADMDX;
    if ("DISDX".equals(codeString))
      return V3ActCode.DISDX;
    if ("INTDX".equals(codeString))
      return V3ActCode.INTDX;
    if ("NOI".equals(codeString))
      return V3ActCode.NOI;
    if ("GISTIER".equals(codeString))
      return V3ActCode.GISTIER;
    if ("HHOBS".equals(codeString))
      return V3ActCode.HHOBS;
    if ("ISSUE".equals(codeString))
      return V3ActCode.ISSUE;
    if ("_ActAdministrativeDetectedIssueCode".equals(codeString))
      return V3ActCode._ACTADMINISTRATIVEDETECTEDISSUECODE;
    if ("_ActAdministrativeAuthorizationDetectedIssueCode".equals(codeString))
      return V3ActCode._ACTADMINISTRATIVEAUTHORIZATIONDETECTEDISSUECODE;
    if ("NAT".equals(codeString))
      return V3ActCode.NAT;
    if ("SUPPRESSED".equals(codeString))
      return V3ActCode.SUPPRESSED;
    if ("VALIDAT".equals(codeString))
      return V3ActCode.VALIDAT;
    if ("KEY204".equals(codeString))
      return V3ActCode.KEY204;
    if ("KEY205".equals(codeString))
      return V3ActCode.KEY205;
    if ("COMPLY".equals(codeString))
      return V3ActCode.COMPLY;
    if ("DUPTHPY".equals(codeString))
      return V3ActCode.DUPTHPY;
    if ("DUPTHPCLS".equals(codeString))
      return V3ActCode.DUPTHPCLS;
    if ("DUPTHPGEN".equals(codeString))
      return V3ActCode.DUPTHPGEN;
    if ("ABUSE".equals(codeString))
      return V3ActCode.ABUSE;
    if ("FRAUD".equals(codeString))
      return V3ActCode.FRAUD;
    if ("PLYDOC".equals(codeString))
      return V3ActCode.PLYDOC;
    if ("PLYPHRM".equals(codeString))
      return V3ActCode.PLYPHRM;
    if ("DOSE".equals(codeString))
      return V3ActCode.DOSE;
    if ("DOSECOND".equals(codeString))
      return V3ActCode.DOSECOND;
    if ("DOSEDUR".equals(codeString))
      return V3ActCode.DOSEDUR;
    if ("DOSEDURH".equals(codeString))
      return V3ActCode.DOSEDURH;
    if ("DOSEDURHIND".equals(codeString))
      return V3ActCode.DOSEDURHIND;
    if ("DOSEDURL".equals(codeString))
      return V3ActCode.DOSEDURL;
    if ("DOSEDURLIND".equals(codeString))
      return V3ActCode.DOSEDURLIND;
    if ("DOSEH".equals(codeString))
      return V3ActCode.DOSEH;
    if ("DOSEHINDA".equals(codeString))
      return V3ActCode.DOSEHINDA;
    if ("DOSEHIND".equals(codeString))
      return V3ActCode.DOSEHIND;
    if ("DOSEHINDSA".equals(codeString))
      return V3ActCode.DOSEHINDSA;
    if ("DOSEHINDW".equals(codeString))
      return V3ActCode.DOSEHINDW;
    if ("DOSEIVL".equals(codeString))
      return V3ActCode.DOSEIVL;
    if ("DOSEIVLIND".equals(codeString))
      return V3ActCode.DOSEIVLIND;
    if ("DOSEL".equals(codeString))
      return V3ActCode.DOSEL;
    if ("DOSELINDA".equals(codeString))
      return V3ActCode.DOSELINDA;
    if ("DOSELIND".equals(codeString))
      return V3ActCode.DOSELIND;
    if ("DOSELINDSA".equals(codeString))
      return V3ActCode.DOSELINDSA;
    if ("DOSELINDW".equals(codeString))
      return V3ActCode.DOSELINDW;
    if ("MDOSE".equals(codeString))
      return V3ActCode.MDOSE;
    if ("OBSA".equals(codeString))
      return V3ActCode.OBSA;
    if ("AGE".equals(codeString))
      return V3ActCode.AGE;
    if ("ADALRT".equals(codeString))
      return V3ActCode.ADALRT;
    if ("GEALRT".equals(codeString))
      return V3ActCode.GEALRT;
    if ("PEALRT".equals(codeString))
      return V3ActCode.PEALRT;
    if ("COND".equals(codeString))
      return V3ActCode.COND;
    if ("HGHT".equals(codeString))
      return V3ActCode.HGHT;
    if ("LACT".equals(codeString))
      return V3ActCode.LACT;
    if ("PREG".equals(codeString))
      return V3ActCode.PREG;
    if ("WGHT".equals(codeString))
      return V3ActCode.WGHT;
    if ("CREACT".equals(codeString))
      return V3ActCode.CREACT;
    if ("GEN".equals(codeString))
      return V3ActCode.GEN;
    if ("GEND".equals(codeString))
      return V3ActCode.GEND;
    if ("LAB".equals(codeString))
      return V3ActCode.LAB;
    if ("REACT".equals(codeString))
      return V3ActCode.REACT;
    if ("ALGY".equals(codeString))
      return V3ActCode.ALGY;
    if ("INT".equals(codeString))
      return V3ActCode.INT;
    if ("RREACT".equals(codeString))
      return V3ActCode.RREACT;
    if ("RALG".equals(codeString))
      return V3ActCode.RALG;
    if ("RAR".equals(codeString))
      return V3ActCode.RAR;
    if ("RINT".equals(codeString))
      return V3ActCode.RINT;
    if ("BUS".equals(codeString))
      return V3ActCode.BUS;
    if ("CODE_INVAL".equals(codeString))
      return V3ActCode.CODEINVAL;
    if ("CODE_DEPREC".equals(codeString))
      return V3ActCode.CODEDEPREC;
    if ("FORMAT".equals(codeString))
      return V3ActCode.FORMAT;
    if ("ILLEGAL".equals(codeString))
      return V3ActCode.ILLEGAL;
    if ("LEN_RANGE".equals(codeString))
      return V3ActCode.LENRANGE;
    if ("LEN_LONG".equals(codeString))
      return V3ActCode.LENLONG;
    if ("LEN_SHORT".equals(codeString))
      return V3ActCode.LENSHORT;
    if ("MISSCOND".equals(codeString))
      return V3ActCode.MISSCOND;
    if ("MISSMAND".equals(codeString))
      return V3ActCode.MISSMAND;
    if ("NODUPS".equals(codeString))
      return V3ActCode.NODUPS;
    if ("NOPERSIST".equals(codeString))
      return V3ActCode.NOPERSIST;
    if ("REP_RANGE".equals(codeString))
      return V3ActCode.REPRANGE;
    if ("MAXOCCURS".equals(codeString))
      return V3ActCode.MAXOCCURS;
    if ("MINOCCURS".equals(codeString))
      return V3ActCode.MINOCCURS;
    if ("_ActAdministrativeRuleDetectedIssueCode".equals(codeString))
      return V3ActCode._ACTADMINISTRATIVERULEDETECTEDISSUECODE;
    if ("KEY206".equals(codeString))
      return V3ActCode.KEY206;
    if ("OBSOLETE".equals(codeString))
      return V3ActCode.OBSOLETE;
    if ("_ActSuppliedItemDetectedIssueCode".equals(codeString))
      return V3ActCode._ACTSUPPLIEDITEMDETECTEDISSUECODE;
    if ("_AdministrationDetectedIssueCode".equals(codeString))
      return V3ActCode._ADMINISTRATIONDETECTEDISSUECODE;
    if ("_AppropriatenessDetectedIssueCode".equals(codeString))
      return V3ActCode._APPROPRIATENESSDETECTEDISSUECODE;
    if ("_InteractionDetectedIssueCode".equals(codeString))
      return V3ActCode._INTERACTIONDETECTEDISSUECODE;
    if ("FOOD".equals(codeString))
      return V3ActCode.FOOD;
    if ("TPROD".equals(codeString))
      return V3ActCode.TPROD;
    if ("DRG".equals(codeString))
      return V3ActCode.DRG;
    if ("NHP".equals(codeString))
      return V3ActCode.NHP;
    if ("NONRX".equals(codeString))
      return V3ActCode.NONRX;
    if ("PREVINEF".equals(codeString))
      return V3ActCode.PREVINEF;
    if ("DACT".equals(codeString))
      return V3ActCode.DACT;
    if ("TIME".equals(codeString))
      return V3ActCode.TIME;
    if ("ALRTENDLATE".equals(codeString))
      return V3ActCode.ALRTENDLATE;
    if ("ALRTSTRTLATE".equals(codeString))
      return V3ActCode.ALRTSTRTLATE;
    if ("_TimingDetectedIssueCode".equals(codeString))
      return V3ActCode._TIMINGDETECTEDISSUECODE;
    if ("ENDLATE".equals(codeString))
      return V3ActCode.ENDLATE;
    if ("STRTLATE".equals(codeString))
      return V3ActCode.STRTLATE;
    if ("_SupplyDetectedIssueCode".equals(codeString))
      return V3ActCode._SUPPLYDETECTEDISSUECODE;
    if ("ALLDONE".equals(codeString))
      return V3ActCode.ALLDONE;
    if ("FULFIL".equals(codeString))
      return V3ActCode.FULFIL;
    if ("NOTACTN".equals(codeString))
      return V3ActCode.NOTACTN;
    if ("NOTEQUIV".equals(codeString))
      return V3ActCode.NOTEQUIV;
    if ("NOTEQUIVGEN".equals(codeString))
      return V3ActCode.NOTEQUIVGEN;
    if ("NOTEQUIVTHER".equals(codeString))
      return V3ActCode.NOTEQUIVTHER;
    if ("TIMING".equals(codeString))
      return V3ActCode.TIMING;
    if ("INTERVAL".equals(codeString))
      return V3ActCode.INTERVAL;
    if ("MINFREQ".equals(codeString))
      return V3ActCode.MINFREQ;
    if ("HELD".equals(codeString))
      return V3ActCode.HELD;
    if ("TOOLATE".equals(codeString))
      return V3ActCode.TOOLATE;
    if ("TOOSOON".equals(codeString))
      return V3ActCode.TOOSOON;
    if ("HISTORIC".equals(codeString))
      return V3ActCode.HISTORIC;
    if ("PATPREF".equals(codeString))
      return V3ActCode.PATPREF;
    if ("PATPREFALT".equals(codeString))
      return V3ActCode.PATPREFALT;
    if ("KSUBJ".equals(codeString))
      return V3ActCode.KSUBJ;
    if ("KSUBT".equals(codeString))
      return V3ActCode.KSUBT;
    if ("OINT".equals(codeString))
      return V3ActCode.OINT;
    if ("ALG".equals(codeString))
      return V3ActCode.ALG;
    if ("DALG".equals(codeString))
      return V3ActCode.DALG;
    if ("EALG".equals(codeString))
      return V3ActCode.EALG;
    if ("FALG".equals(codeString))
      return V3ActCode.FALG;
    if ("DINT".equals(codeString))
      return V3ActCode.DINT;
    if ("DNAINT".equals(codeString))
      return V3ActCode.DNAINT;
    if ("EINT".equals(codeString))
      return V3ActCode.EINT;
    if ("ENAINT".equals(codeString))
      return V3ActCode.ENAINT;
    if ("FINT".equals(codeString))
      return V3ActCode.FINT;
    if ("FNAINT".equals(codeString))
      return V3ActCode.FNAINT;
    if ("NAINT".equals(codeString))
      return V3ActCode.NAINT;
    if ("SEV".equals(codeString))
      return V3ActCode.SEV;
    if ("_FDALabelData".equals(codeString))
      return V3ActCode._FDALABELDATA;
    if ("FDACOATING".equals(codeString))
      return V3ActCode.FDACOATING;
    if ("FDACOLOR".equals(codeString))
      return V3ActCode.FDACOLOR;
    if ("FDAIMPRINTCD".equals(codeString))
      return V3ActCode.FDAIMPRINTCD;
    if ("FDALOGO".equals(codeString))
      return V3ActCode.FDALOGO;
    if ("FDASCORING".equals(codeString))
      return V3ActCode.FDASCORING;
    if ("FDASHAPE".equals(codeString))
      return V3ActCode.FDASHAPE;
    if ("FDASIZE".equals(codeString))
      return V3ActCode.FDASIZE;
    if ("_ROIOverlayShape".equals(codeString))
      return V3ActCode._ROIOVERLAYSHAPE;
    if ("CIRCLE".equals(codeString))
      return V3ActCode.CIRCLE;
    if ("ELLIPSE".equals(codeString))
      return V3ActCode.ELLIPSE;
    if ("POINT".equals(codeString))
      return V3ActCode.POINT;
    if ("POLY".equals(codeString))
      return V3ActCode.POLY;
    if ("C".equals(codeString))
      return V3ActCode.C;
    if ("DIET".equals(codeString))
      return V3ActCode.DIET;
    if ("BR".equals(codeString))
      return V3ActCode.BR;
    if ("DM".equals(codeString))
      return V3ActCode.DM;
    if ("FAST".equals(codeString))
      return V3ActCode.FAST;
    if ("FORMULA".equals(codeString))
      return V3ActCode.FORMULA;
    if ("GF".equals(codeString))
      return V3ActCode.GF;
    if ("LF".equals(codeString))
      return V3ActCode.LF;
    if ("LP".equals(codeString))
      return V3ActCode.LP;
    if ("LQ".equals(codeString))
      return V3ActCode.LQ;
    if ("LS".equals(codeString))
      return V3ActCode.LS;
    if ("N".equals(codeString))
      return V3ActCode.N;
    if ("NF".equals(codeString))
      return V3ActCode.NF;
    if ("PAF".equals(codeString))
      return V3ActCode.PAF;
    if ("PAR".equals(codeString))
      return V3ActCode.PAR;
    if ("RD".equals(codeString))
      return V3ActCode.RD;
    if ("SCH".equals(codeString))
      return V3ActCode.SCH;
    if ("SUPPLEMENT".equals(codeString))
      return V3ActCode.SUPPLEMENT;
    if ("T".equals(codeString))
      return V3ActCode.T;
    if ("VLI".equals(codeString))
      return V3ActCode.VLI;
    if ("DRUGPRG".equals(codeString))
      return V3ActCode.DRUGPRG;
    if ("F".equals(codeString))
      return V3ActCode.F;
    if ("PRLMN".equals(codeString))
      return V3ActCode.PRLMN;
    if ("SECOBS".equals(codeString))
      return V3ActCode.SECOBS;
    if ("SECCATOBS".equals(codeString))
      return V3ActCode.SECCATOBS;
    if ("SECCLASSOBS".equals(codeString))
      return V3ActCode.SECCLASSOBS;
    if ("SECCONOBS".equals(codeString))
      return V3ActCode.SECCONOBS;
    if ("SECINTOBS".equals(codeString))
      return V3ActCode.SECINTOBS;
    if ("SECALTINTOBS".equals(codeString))
      return V3ActCode.SECALTINTOBS;
    if ("SECDATINTOBS".equals(codeString))
      return V3ActCode.SECDATINTOBS;
    if ("SECINTCONOBS".equals(codeString))
      return V3ActCode.SECINTCONOBS;
    if ("SECINTPRVOBS".equals(codeString))
      return V3ActCode.SECINTPRVOBS;
    if ("SECINTPRVABOBS".equals(codeString))
      return V3ActCode.SECINTPRVABOBS;
    if ("SECINTPRVRBOBS".equals(codeString))
      return V3ActCode.SECINTPRVRBOBS;
    if ("SECINTSTOBS".equals(codeString))
      return V3ActCode.SECINTSTOBS;
    if ("SECTRSTOBS".equals(codeString))
      return V3ActCode.SECTRSTOBS;
    if ("TRSTACCRDOBS".equals(codeString))
      return V3ActCode.TRSTACCRDOBS;
    if ("TRSTAGREOBS".equals(codeString))
      return V3ActCode.TRSTAGREOBS;
    if ("TRSTCERTOBS".equals(codeString))
      return V3ActCode.TRSTCERTOBS;
    if ("TRSTFWKOBS".equals(codeString))
      return V3ActCode.TRSTFWKOBS;
    if ("TRSTLOAOBS".equals(codeString))
      return V3ActCode.TRSTLOAOBS;
    if ("TRSTMECOBS".equals(codeString))
      return V3ActCode.TRSTMECOBS;
    if ("SUBSIDFFS".equals(codeString))
      return V3ActCode.SUBSIDFFS;
    if ("WRKCOMP".equals(codeString))
      return V3ActCode.WRKCOMP;
    if ("_ActProcedureCode".equals(codeString))
      return V3ActCode._ACTPROCEDURECODE;
    if ("_ActBillableServiceCode".equals(codeString))
      return V3ActCode._ACTBILLABLESERVICECODE;
    if ("_HL7DefinedActCodes".equals(codeString))
      return V3ActCode._HL7DEFINEDACTCODES;
    if ("COPAY".equals(codeString))
      return V3ActCode.COPAY;
    if ("DEDUCT".equals(codeString))
      return V3ActCode.DEDUCT;
    if ("DOSEIND".equals(codeString))
      return V3ActCode.DOSEIND;
    if ("PRA".equals(codeString))
      return V3ActCode.PRA;
    if ("STORE".equals(codeString))
      return V3ActCode.STORE;
    throw new IllegalArgumentException("Unknown V3ActCode code '"+codeString+"'");
  }

  public String toCode(V3ActCode code) {
    if (code == V3ActCode._ACTACCOUNTCODE)
      return "_ActAccountCode";
    if (code == V3ActCode.ACCTRECEIVABLE)
      return "ACCTRECEIVABLE";
    if (code == V3ActCode.CASH)
      return "CASH";
    if (code == V3ActCode.CC)
      return "CC";
    if (code == V3ActCode.AE)
      return "AE";
    if (code == V3ActCode.DN)
      return "DN";
    if (code == V3ActCode.DV)
      return "DV";
    if (code == V3ActCode.MC)
      return "MC";
    if (code == V3ActCode.V)
      return "V";
    if (code == V3ActCode.PBILLACCT)
      return "PBILLACCT";
    if (code == V3ActCode._ACTADJUDICATIONCODE)
      return "_ActAdjudicationCode";
    if (code == V3ActCode._ACTADJUDICATIONGROUPCODE)
      return "_ActAdjudicationGroupCode";
    if (code == V3ActCode.CONT)
      return "CONT";
    if (code == V3ActCode.DAY)
      return "DAY";
    if (code == V3ActCode.LOC)
      return "LOC";
    if (code == V3ActCode.MONTH)
      return "MONTH";
    if (code == V3ActCode.PERIOD)
      return "PERIOD";
    if (code == V3ActCode.PROV)
      return "PROV";
    if (code == V3ActCode.WEEK)
      return "WEEK";
    if (code == V3ActCode.YEAR)
      return "YEAR";
    if (code == V3ActCode.AA)
      return "AA";
    if (code == V3ActCode.ANF)
      return "ANF";
    if (code == V3ActCode.AR)
      return "AR";
    if (code == V3ActCode.AS)
      return "AS";
    if (code == V3ActCode._ACTADJUDICATIONRESULTACTIONCODE)
      return "_ActAdjudicationResultActionCode";
    if (code == V3ActCode.DISPLAY)
      return "DISPLAY";
    if (code == V3ActCode.FORM)
      return "FORM";
    if (code == V3ActCode._ACTBILLABLEMODIFIERCODE)
      return "_ActBillableModifierCode";
    if (code == V3ActCode.CPTM)
      return "CPTM";
    if (code == V3ActCode.HCPCSA)
      return "HCPCSA";
    if (code == V3ActCode._ACTBILLINGARRANGEMENTCODE)
      return "_ActBillingArrangementCode";
    if (code == V3ActCode.BLK)
      return "BLK";
    if (code == V3ActCode.CAP)
      return "CAP";
    if (code == V3ActCode.CONTF)
      return "CONTF";
    if (code == V3ActCode.FINBILL)
      return "FINBILL";
    if (code == V3ActCode.ROST)
      return "ROST";
    if (code == V3ActCode.SESS)
      return "SESS";
    if (code == V3ActCode.FFS)
      return "FFS";
    if (code == V3ActCode.FFPS)
      return "FFPS";
    if (code == V3ActCode.FFCS)
      return "FFCS";
    if (code == V3ActCode.TFS)
      return "TFS";
    if (code == V3ActCode._ACTBOUNDEDROICODE)
      return "_ActBoundedROICode";
    if (code == V3ActCode.ROIFS)
      return "ROIFS";
    if (code == V3ActCode.ROIPS)
      return "ROIPS";
    if (code == V3ActCode._ACTCAREPROVISIONCODE)
      return "_ActCareProvisionCode";
    if (code == V3ActCode._ACTCREDENTIALEDCARECODE)
      return "_ActCredentialedCareCode";
    if (code == V3ActCode._ACTCREDENTIALEDCAREPROVISIONPERSONCODE)
      return "_ActCredentialedCareProvisionPersonCode";
    if (code == V3ActCode.CACC)
      return "CACC";
    if (code == V3ActCode.CAIC)
      return "CAIC";
    if (code == V3ActCode.CAMC)
      return "CAMC";
    if (code == V3ActCode.CANC)
      return "CANC";
    if (code == V3ActCode.CAPC)
      return "CAPC";
    if (code == V3ActCode.CBGC)
      return "CBGC";
    if (code == V3ActCode.CCCC)
      return "CCCC";
    if (code == V3ActCode.CCGC)
      return "CCGC";
    if (code == V3ActCode.CCPC)
      return "CCPC";
    if (code == V3ActCode.CCSC)
      return "CCSC";
    if (code == V3ActCode.CDEC)
      return "CDEC";
    if (code == V3ActCode.CDRC)
      return "CDRC";
    if (code == V3ActCode.CEMC)
      return "CEMC";
    if (code == V3ActCode.CFPC)
      return "CFPC";
    if (code == V3ActCode.CIMC)
      return "CIMC";
    if (code == V3ActCode.CMGC)
      return "CMGC";
    if (code == V3ActCode.CNEC)
      return "CNEC";
    if (code == V3ActCode.CNMC)
      return "CNMC";
    if (code == V3ActCode.CNQC)
      return "CNQC";
    if (code == V3ActCode.CNSC)
      return "CNSC";
    if (code == V3ActCode.COGC)
      return "COGC";
    if (code == V3ActCode.COMC)
      return "COMC";
    if (code == V3ActCode.COPC)
      return "COPC";
    if (code == V3ActCode.COSC)
      return "COSC";
    if (code == V3ActCode.COTC)
      return "COTC";
    if (code == V3ActCode.CPEC)
      return "CPEC";
    if (code == V3ActCode.CPGC)
      return "CPGC";
    if (code == V3ActCode.CPHC)
      return "CPHC";
    if (code == V3ActCode.CPRC)
      return "CPRC";
    if (code == V3ActCode.CPSC)
      return "CPSC";
    if (code == V3ActCode.CPYC)
      return "CPYC";
    if (code == V3ActCode.CROC)
      return "CROC";
    if (code == V3ActCode.CRPC)
      return "CRPC";
    if (code == V3ActCode.CSUC)
      return "CSUC";
    if (code == V3ActCode.CTSC)
      return "CTSC";
    if (code == V3ActCode.CURC)
      return "CURC";
    if (code == V3ActCode.CVSC)
      return "CVSC";
    if (code == V3ActCode.LGPC)
      return "LGPC";
    if (code == V3ActCode._ACTCREDENTIALEDCAREPROVISIONPROGRAMCODE)
      return "_ActCredentialedCareProvisionProgramCode";
    if (code == V3ActCode.AALC)
      return "AALC";
    if (code == V3ActCode.AAMC)
      return "AAMC";
    if (code == V3ActCode.ABHC)
      return "ABHC";
    if (code == V3ActCode.ACAC)
      return "ACAC";
    if (code == V3ActCode.ACHC)
      return "ACHC";
    if (code == V3ActCode.AHOC)
      return "AHOC";
    if (code == V3ActCode.ALTC)
      return "ALTC";
    if (code == V3ActCode.AOSC)
      return "AOSC";
    if (code == V3ActCode.CACS)
      return "CACS";
    if (code == V3ActCode.CAMI)
      return "CAMI";
    if (code == V3ActCode.CAST)
      return "CAST";
    if (code == V3ActCode.CBAR)
      return "CBAR";
    if (code == V3ActCode.CCAD)
      return "CCAD";
    if (code == V3ActCode.CCAR)
      return "CCAR";
    if (code == V3ActCode.CDEP)
      return "CDEP";
    if (code == V3ActCode.CDGD)
      return "CDGD";
    if (code == V3ActCode.CDIA)
      return "CDIA";
    if (code == V3ActCode.CEPI)
      return "CEPI";
    if (code == V3ActCode.CFEL)
      return "CFEL";
    if (code == V3ActCode.CHFC)
      return "CHFC";
    if (code == V3ActCode.CHRO)
      return "CHRO";
    if (code == V3ActCode.CHYP)
      return "CHYP";
    if (code == V3ActCode.CMIH)
      return "CMIH";
    if (code == V3ActCode.CMSC)
      return "CMSC";
    if (code == V3ActCode.COJR)
      return "COJR";
    if (code == V3ActCode.CONC)
      return "CONC";
    if (code == V3ActCode.COPD)
      return "COPD";
    if (code == V3ActCode.CORT)
      return "CORT";
    if (code == V3ActCode.CPAD)
      return "CPAD";
    if (code == V3ActCode.CPND)
      return "CPND";
    if (code == V3ActCode.CPST)
      return "CPST";
    if (code == V3ActCode.CSDM)
      return "CSDM";
    if (code == V3ActCode.CSIC)
      return "CSIC";
    if (code == V3ActCode.CSLD)
      return "CSLD";
    if (code == V3ActCode.CSPT)
      return "CSPT";
    if (code == V3ActCode.CTBU)
      return "CTBU";
    if (code == V3ActCode.CVDC)
      return "CVDC";
    if (code == V3ActCode.CWMA)
      return "CWMA";
    if (code == V3ActCode.CWOH)
      return "CWOH";
    if (code == V3ActCode._ACTENCOUNTERCODE)
      return "_ActEncounterCode";
    if (code == V3ActCode.AMB)
      return "AMB";
    if (code == V3ActCode.EMER)
      return "EMER";
    if (code == V3ActCode.FLD)
      return "FLD";
    if (code == V3ActCode.HH)
      return "HH";
    if (code == V3ActCode.IMP)
      return "IMP";
    if (code == V3ActCode.ACUTE)
      return "ACUTE";
    if (code == V3ActCode.NONAC)
      return "NONAC";
    if (code == V3ActCode.PRENC)
      return "PRENC";
    if (code == V3ActCode.SS)
      return "SS";
    if (code == V3ActCode.VR)
      return "VR";
    if (code == V3ActCode._ACTMEDICALSERVICECODE)
      return "_ActMedicalServiceCode";
    if (code == V3ActCode.ALC)
      return "ALC";
    if (code == V3ActCode.CARD)
      return "CARD";
    if (code == V3ActCode.CHR)
      return "CHR";
    if (code == V3ActCode.DNTL)
      return "DNTL";
    if (code == V3ActCode.DRGRHB)
      return "DRGRHB";
    if (code == V3ActCode.GENRL)
      return "GENRL";
    if (code == V3ActCode.MED)
      return "MED";
    if (code == V3ActCode.OBS)
      return "OBS";
    if (code == V3ActCode.ONC)
      return "ONC";
    if (code == V3ActCode.PALL)
      return "PALL";
    if (code == V3ActCode.PED)
      return "PED";
    if (code == V3ActCode.PHAR)
      return "PHAR";
    if (code == V3ActCode.PHYRHB)
      return "PHYRHB";
    if (code == V3ActCode.PSYCH)
      return "PSYCH";
    if (code == V3ActCode.SURG)
      return "SURG";
    if (code == V3ActCode._ACTCLAIMATTACHMENTCATEGORYCODE)
      return "_ActClaimAttachmentCategoryCode";
    if (code == V3ActCode.AUTOATTCH)
      return "AUTOATTCH";
    if (code == V3ActCode.DOCUMENT)
      return "DOCUMENT";
    if (code == V3ActCode.HEALTHREC)
      return "HEALTHREC";
    if (code == V3ActCode.IMG)
      return "IMG";
    if (code == V3ActCode.LABRESULTS)
      return "LABRESULTS";
    if (code == V3ActCode.MODEL)
      return "MODEL";
    if (code == V3ActCode.WIATTCH)
      return "WIATTCH";
    if (code == V3ActCode.XRAY)
      return "XRAY";
    if (code == V3ActCode._ACTCONSENTTYPE)
      return "_ActConsentType";
    if (code == V3ActCode.ICOL)
      return "ICOL";
    if (code == V3ActCode.IDSCL)
      return "IDSCL";
    if (code == V3ActCode.INFA)
      return "INFA";
    if (code == V3ActCode.INFAO)
      return "INFAO";
    if (code == V3ActCode.INFASO)
      return "INFASO";
    if (code == V3ActCode.IRDSCL)
      return "IRDSCL";
    if (code == V3ActCode.RESEARCH)
      return "RESEARCH";
    if (code == V3ActCode.RSDID)
      return "RSDID";
    if (code == V3ActCode.RSREID)
      return "RSREID";
    if (code == V3ActCode._ACTCONTAINERREGISTRATIONCODE)
      return "_ActContainerRegistrationCode";
    if (code == V3ActCode.ID)
      return "ID";
    if (code == V3ActCode.IP)
      return "IP";
    if (code == V3ActCode.L)
      return "L";
    if (code == V3ActCode.M)
      return "M";
    if (code == V3ActCode.O)
      return "O";
    if (code == V3ActCode.R)
      return "R";
    if (code == V3ActCode.X)
      return "X";
    if (code == V3ActCode._ACTCONTROLVARIABLE)
      return "_ActControlVariable";
    if (code == V3ActCode.AUTO)
      return "AUTO";
    if (code == V3ActCode.ENDC)
      return "ENDC";
    if (code == V3ActCode.REFLEX)
      return "REFLEX";
    if (code == V3ActCode._ACTCOVERAGECONFIRMATIONCODE)
      return "_ActCoverageConfirmationCode";
    if (code == V3ActCode._ACTCOVERAGEAUTHORIZATIONCONFIRMATIONCODE)
      return "_ActCoverageAuthorizationConfirmationCode";
    if (code == V3ActCode.AUTH)
      return "AUTH";
    if (code == V3ActCode.NAUTH)
      return "NAUTH";
    if (code == V3ActCode._ACTCOVERAGEELIGIBILITYCONFIRMATIONCODE)
      return "_ActCoverageEligibilityConfirmationCode";
    if (code == V3ActCode.ELG)
      return "ELG";
    if (code == V3ActCode.NELG)
      return "NELG";
    if (code == V3ActCode._ACTCOVERAGELIMITCODE)
      return "_ActCoverageLimitCode";
    if (code == V3ActCode._ACTCOVERAGEQUANTITYLIMITCODE)
      return "_ActCoverageQuantityLimitCode";
    if (code == V3ActCode.COVPRD)
      return "COVPRD";
    if (code == V3ActCode.LFEMX)
      return "LFEMX";
    if (code == V3ActCode.NETAMT)
      return "NETAMT";
    if (code == V3ActCode.PRDMX)
      return "PRDMX";
    if (code == V3ActCode.UNITPRICE)
      return "UNITPRICE";
    if (code == V3ActCode.UNITQTY)
      return "UNITQTY";
    if (code == V3ActCode.COVMX)
      return "COVMX";
    if (code == V3ActCode._ACTCOVEREDPARTYLIMITCODE)
      return "_ActCoveredPartyLimitCode";
    if (code == V3ActCode._ACTCOVERAGETYPECODE)
      return "_ActCoverageTypeCode";
    if (code == V3ActCode._ACTINSURANCEPOLICYCODE)
      return "_ActInsurancePolicyCode";
    if (code == V3ActCode.EHCPOL)
      return "EHCPOL";
    if (code == V3ActCode.HSAPOL)
      return "HSAPOL";
    if (code == V3ActCode.AUTOPOL)
      return "AUTOPOL";
    if (code == V3ActCode.COL)
      return "COL";
    if (code == V3ActCode.UNINSMOT)
      return "UNINSMOT";
    if (code == V3ActCode.PUBLICPOL)
      return "PUBLICPOL";
    if (code == V3ActCode.DENTPRG)
      return "DENTPRG";
    if (code == V3ActCode.DISEASEPRG)
      return "DISEASEPRG";
    if (code == V3ActCode.CANPRG)
      return "CANPRG";
    if (code == V3ActCode.ENDRENAL)
      return "ENDRENAL";
    if (code == V3ActCode.HIVAIDS)
      return "HIVAIDS";
    if (code == V3ActCode.MANDPOL)
      return "MANDPOL";
    if (code == V3ActCode.MENTPRG)
      return "MENTPRG";
    if (code == V3ActCode.SAFNET)
      return "SAFNET";
    if (code == V3ActCode.SUBPRG)
      return "SUBPRG";
    if (code == V3ActCode.SUBSIDIZ)
      return "SUBSIDIZ";
    if (code == V3ActCode.SUBSIDMC)
      return "SUBSIDMC";
    if (code == V3ActCode.SUBSUPP)
      return "SUBSUPP";
    if (code == V3ActCode.WCBPOL)
      return "WCBPOL";
    if (code == V3ActCode._ACTINSURANCETYPECODE)
      return "_ActInsuranceTypeCode";
    if (code == V3ActCode._ACTHEALTHINSURANCETYPECODE)
      return "_ActHealthInsuranceTypeCode";
    if (code == V3ActCode.DENTAL)
      return "DENTAL";
    if (code == V3ActCode.DISEASE)
      return "DISEASE";
    if (code == V3ActCode.DRUGPOL)
      return "DRUGPOL";
    if (code == V3ActCode.HIP)
      return "HIP";
    if (code == V3ActCode.LTC)
      return "LTC";
    if (code == V3ActCode.MCPOL)
      return "MCPOL";
    if (code == V3ActCode.POS)
      return "POS";
    if (code == V3ActCode.HMO)
      return "HMO";
    if (code == V3ActCode.PPO)
      return "PPO";
    if (code == V3ActCode.MENTPOL)
      return "MENTPOL";
    if (code == V3ActCode.SUBPOL)
      return "SUBPOL";
    if (code == V3ActCode.VISPOL)
      return "VISPOL";
    if (code == V3ActCode.DIS)
      return "DIS";
    if (code == V3ActCode.EWB)
      return "EWB";
    if (code == V3ActCode.FLEXP)
      return "FLEXP";
    if (code == V3ActCode.LIFE)
      return "LIFE";
    if (code == V3ActCode.ANNU)
      return "ANNU";
    if (code == V3ActCode.TLIFE)
      return "TLIFE";
    if (code == V3ActCode.ULIFE)
      return "ULIFE";
    if (code == V3ActCode.PNC)
      return "PNC";
    if (code == V3ActCode.REI)
      return "REI";
    if (code == V3ActCode.SURPL)
      return "SURPL";
    if (code == V3ActCode.UMBRL)
      return "UMBRL";
    if (code == V3ActCode._ACTPROGRAMTYPECODE)
      return "_ActProgramTypeCode";
    if (code == V3ActCode.CHAR)
      return "CHAR";
    if (code == V3ActCode.CRIME)
      return "CRIME";
    if (code == V3ActCode.EAP)
      return "EAP";
    if (code == V3ActCode.GOVEMP)
      return "GOVEMP";
    if (code == V3ActCode.HIRISK)
      return "HIRISK";
    if (code == V3ActCode.IND)
      return "IND";
    if (code == V3ActCode.MILITARY)
      return "MILITARY";
    if (code == V3ActCode.RETIRE)
      return "RETIRE";
    if (code == V3ActCode.SOCIAL)
      return "SOCIAL";
    if (code == V3ActCode.VET)
      return "VET";
    if (code == V3ActCode._ACTDETECTEDISSUEMANAGEMENTCODE)
      return "_ActDetectedIssueManagementCode";
    if (code == V3ActCode._ACTADMINISTRATIVEDETECTEDISSUEMANAGEMENTCODE)
      return "_ActAdministrativeDetectedIssueManagementCode";
    if (code == V3ActCode._AUTHORIZATIONISSUEMANAGEMENTCODE)
      return "_AuthorizationIssueManagementCode";
    if (code == V3ActCode.EMAUTH)
      return "EMAUTH";
    if (code == V3ActCode._21)
      return "21";
    if (code == V3ActCode._1)
      return "1";
    if (code == V3ActCode._19)
      return "19";
    if (code == V3ActCode._2)
      return "2";
    if (code == V3ActCode._22)
      return "22";
    if (code == V3ActCode._23)
      return "23";
    if (code == V3ActCode._3)
      return "3";
    if (code == V3ActCode._4)
      return "4";
    if (code == V3ActCode._5)
      return "5";
    if (code == V3ActCode._6)
      return "6";
    if (code == V3ActCode._7)
      return "7";
    if (code == V3ActCode._14)
      return "14";
    if (code == V3ActCode._15)
      return "15";
    if (code == V3ActCode._16)
      return "16";
    if (code == V3ActCode._17)
      return "17";
    if (code == V3ActCode._18)
      return "18";
    if (code == V3ActCode._20)
      return "20";
    if (code == V3ActCode._8)
      return "8";
    if (code == V3ActCode._10)
      return "10";
    if (code == V3ActCode._11)
      return "11";
    if (code == V3ActCode._12)
      return "12";
    if (code == V3ActCode._13)
      return "13";
    if (code == V3ActCode._9)
      return "9";
    if (code == V3ActCode._ACTEXPOSURECODE)
      return "_ActExposureCode";
    if (code == V3ActCode.CHLDCARE)
      return "CHLDCARE";
    if (code == V3ActCode.CONVEYNC)
      return "CONVEYNC";
    if (code == V3ActCode.HLTHCARE)
      return "HLTHCARE";
    if (code == V3ActCode.HOMECARE)
      return "HOMECARE";
    if (code == V3ActCode.HOSPPTNT)
      return "HOSPPTNT";
    if (code == V3ActCode.HOSPVSTR)
      return "HOSPVSTR";
    if (code == V3ActCode.HOUSEHLD)
      return "HOUSEHLD";
    if (code == V3ActCode.INMATE)
      return "INMATE";
    if (code == V3ActCode.INTIMATE)
      return "INTIMATE";
    if (code == V3ActCode.LTRMCARE)
      return "LTRMCARE";
    if (code == V3ActCode.PLACE)
      return "PLACE";
    if (code == V3ActCode.PTNTCARE)
      return "PTNTCARE";
    if (code == V3ActCode.SCHOOL2)
      return "SCHOOL2";
    if (code == V3ActCode.SOCIAL2)
      return "SOCIAL2";
    if (code == V3ActCode.SUBSTNCE)
      return "SUBSTNCE";
    if (code == V3ActCode.TRAVINT)
      return "TRAVINT";
    if (code == V3ActCode.WORK2)
      return "WORK2";
    if (code == V3ActCode._ACTFINANCIALTRANSACTIONCODE)
      return "_ActFinancialTransactionCode";
    if (code == V3ActCode.CHRG)
      return "CHRG";
    if (code == V3ActCode.REV)
      return "REV";
    if (code == V3ActCode._ACTINCIDENTCODE)
      return "_ActIncidentCode";
    if (code == V3ActCode.MVA)
      return "MVA";
    if (code == V3ActCode.SCHOOL)
      return "SCHOOL";
    if (code == V3ActCode.SPT)
      return "SPT";
    if (code == V3ActCode.WPA)
      return "WPA";
    if (code == V3ActCode._ACTINFORMATIONACCESSCODE)
      return "_ActInformationAccessCode";
    if (code == V3ActCode.ACADR)
      return "ACADR";
    if (code == V3ActCode.ACALL)
      return "ACALL";
    if (code == V3ActCode.ACALLG)
      return "ACALLG";
    if (code == V3ActCode.ACCONS)
      return "ACCONS";
    if (code == V3ActCode.ACDEMO)
      return "ACDEMO";
    if (code == V3ActCode.ACDI)
      return "ACDI";
    if (code == V3ActCode.ACIMMUN)
      return "ACIMMUN";
    if (code == V3ActCode.ACLAB)
      return "ACLAB";
    if (code == V3ActCode.ACMED)
      return "ACMED";
    if (code == V3ActCode.ACMEDC)
      return "ACMEDC";
    if (code == V3ActCode.ACMEN)
      return "ACMEN";
    if (code == V3ActCode.ACOBS)
      return "ACOBS";
    if (code == V3ActCode.ACPOLPRG)
      return "ACPOLPRG";
    if (code == V3ActCode.ACPROV)
      return "ACPROV";
    if (code == V3ActCode.ACPSERV)
      return "ACPSERV";
    if (code == V3ActCode.ACSUBSTAB)
      return "ACSUBSTAB";
    if (code == V3ActCode._ACTINFORMATIONACCESSCONTEXTCODE)
      return "_ActInformationAccessContextCode";
    if (code == V3ActCode.INFAUT)
      return "INFAUT";
    if (code == V3ActCode.INFCON)
      return "INFCON";
    if (code == V3ActCode.INFCRT)
      return "INFCRT";
    if (code == V3ActCode.INFDNG)
      return "INFDNG";
    if (code == V3ActCode.INFEMER)
      return "INFEMER";
    if (code == V3ActCode.INFPWR)
      return "INFPWR";
    if (code == V3ActCode.INFREG)
      return "INFREG";
    if (code == V3ActCode._ACTINFORMATIONCATEGORYCODE)
      return "_ActInformationCategoryCode";
    if (code == V3ActCode.ALLCAT)
      return "ALLCAT";
    if (code == V3ActCode.ALLGCAT)
      return "ALLGCAT";
    if (code == V3ActCode.ARCAT)
      return "ARCAT";
    if (code == V3ActCode.COBSCAT)
      return "COBSCAT";
    if (code == V3ActCode.DEMOCAT)
      return "DEMOCAT";
    if (code == V3ActCode.DICAT)
      return "DICAT";
    if (code == V3ActCode.IMMUCAT)
      return "IMMUCAT";
    if (code == V3ActCode.LABCAT)
      return "LABCAT";
    if (code == V3ActCode.MEDCCAT)
      return "MEDCCAT";
    if (code == V3ActCode.MENCAT)
      return "MENCAT";
    if (code == V3ActCode.PSVCCAT)
      return "PSVCCAT";
    if (code == V3ActCode.RXCAT)
      return "RXCAT";
    if (code == V3ActCode._ACTINVOICEELEMENTCODE)
      return "_ActInvoiceElementCode";
    if (code == V3ActCode._ACTINVOICEADJUDICATIONPAYMENTCODE)
      return "_ActInvoiceAdjudicationPaymentCode";
    if (code == V3ActCode._ACTINVOICEADJUDICATIONPAYMENTGROUPCODE)
      return "_ActInvoiceAdjudicationPaymentGroupCode";
    if (code == V3ActCode.ALEC)
      return "ALEC";
    if (code == V3ActCode.BONUS)
      return "BONUS";
    if (code == V3ActCode.CFWD)
      return "CFWD";
    if (code == V3ActCode.EDU)
      return "EDU";
    if (code == V3ActCode.EPYMT)
      return "EPYMT";
    if (code == V3ActCode.GARN)
      return "GARN";
    if (code == V3ActCode.INVOICE)
      return "INVOICE";
    if (code == V3ActCode.PINV)
      return "PINV";
    if (code == V3ActCode.PPRD)
      return "PPRD";
    if (code == V3ActCode.PROA)
      return "PROA";
    if (code == V3ActCode.RECOV)
      return "RECOV";
    if (code == V3ActCode.RETRO)
      return "RETRO";
    if (code == V3ActCode.TRAN)
      return "TRAN";
    if (code == V3ActCode._ACTINVOICEADJUDICATIONPAYMENTSUMMARYCODE)
      return "_ActInvoiceAdjudicationPaymentSummaryCode";
    if (code == V3ActCode.INVTYPE)
      return "INVTYPE";
    if (code == V3ActCode.PAYEE)
      return "PAYEE";
    if (code == V3ActCode.PAYOR)
      return "PAYOR";
    if (code == V3ActCode.SENDAPP)
      return "SENDAPP";
    if (code == V3ActCode._ACTINVOICEDETAILCODE)
      return "_ActInvoiceDetailCode";
    if (code == V3ActCode._ACTINVOICEDETAILCLINICALPRODUCTCODE)
      return "_ActInvoiceDetailClinicalProductCode";
    if (code == V3ActCode.UNSPSC)
      return "UNSPSC";
    if (code == V3ActCode._ACTINVOICEDETAILDRUGPRODUCTCODE)
      return "_ActInvoiceDetailDrugProductCode";
    if (code == V3ActCode.GTIN)
      return "GTIN";
    if (code == V3ActCode.UPC)
      return "UPC";
    if (code == V3ActCode._ACTINVOICEDETAILGENERICCODE)
      return "_ActInvoiceDetailGenericCode";
    if (code == V3ActCode._ACTINVOICEDETAILGENERICADJUDICATORCODE)
      return "_ActInvoiceDetailGenericAdjudicatorCode";
    if (code == V3ActCode.COIN)
      return "COIN";
    if (code == V3ActCode.COPAYMENT)
      return "COPAYMENT";
    if (code == V3ActCode.DEDUCTIBLE)
      return "DEDUCTIBLE";
    if (code == V3ActCode.PAY)
      return "PAY";
    if (code == V3ActCode.SPEND)
      return "SPEND";
    if (code == V3ActCode.COINS)
      return "COINS";
    if (code == V3ActCode._ACTINVOICEDETAILGENERICMODIFIERCODE)
      return "_ActInvoiceDetailGenericModifierCode";
    if (code == V3ActCode.AFTHRS)
      return "AFTHRS";
    if (code == V3ActCode.ISOL)
      return "ISOL";
    if (code == V3ActCode.OOO)
      return "OOO";
    if (code == V3ActCode._ACTINVOICEDETAILGENERICPROVIDERCODE)
      return "_ActInvoiceDetailGenericProviderCode";
    if (code == V3ActCode.CANCAPT)
      return "CANCAPT";
    if (code == V3ActCode.DSC)
      return "DSC";
    if (code == V3ActCode.ESA)
      return "ESA";
    if (code == V3ActCode.FFSTOP)
      return "FFSTOP";
    if (code == V3ActCode.FNLFEE)
      return "FNLFEE";
    if (code == V3ActCode.FRSTFEE)
      return "FRSTFEE";
    if (code == V3ActCode.MARKUP)
      return "MARKUP";
    if (code == V3ActCode.MISSAPT)
      return "MISSAPT";
    if (code == V3ActCode.PERFEE)
      return "PERFEE";
    if (code == V3ActCode.PERMBNS)
      return "PERMBNS";
    if (code == V3ActCode.RESTOCK)
      return "RESTOCK";
    if (code == V3ActCode.TRAVEL)
      return "TRAVEL";
    if (code == V3ActCode.URGENT)
      return "URGENT";
    if (code == V3ActCode._ACTINVOICEDETAILTAXCODE)
      return "_ActInvoiceDetailTaxCode";
    if (code == V3ActCode.FST)
      return "FST";
    if (code == V3ActCode.HST)
      return "HST";
    if (code == V3ActCode.PST)
      return "PST";
    if (code == V3ActCode._ACTINVOICEDETAILPREFERREDACCOMMODATIONCODE)
      return "_ActInvoiceDetailPreferredAccommodationCode";
    if (code == V3ActCode._ACTENCOUNTERACCOMMODATIONCODE)
      return "_ActEncounterAccommodationCode";
    if (code == V3ActCode._HL7ACCOMMODATIONCODE)
      return "_HL7AccommodationCode";
    if (code == V3ActCode.I)
      return "I";
    if (code == V3ActCode.P)
      return "P";
    if (code == V3ActCode.S)
      return "S";
    if (code == V3ActCode.SP)
      return "SP";
    if (code == V3ActCode.W)
      return "W";
    if (code == V3ActCode._ACTINVOICEDETAILCLINICALSERVICECODE)
      return "_ActInvoiceDetailClinicalServiceCode";
    if (code == V3ActCode._ACTINVOICEGROUPCODE)
      return "_ActInvoiceGroupCode";
    if (code == V3ActCode._ACTINVOICEINTERGROUPCODE)
      return "_ActInvoiceInterGroupCode";
    if (code == V3ActCode.CPNDDRGING)
      return "CPNDDRGING";
    if (code == V3ActCode.CPNDINDING)
      return "CPNDINDING";
    if (code == V3ActCode.CPNDSUPING)
      return "CPNDSUPING";
    if (code == V3ActCode.DRUGING)
      return "DRUGING";
    if (code == V3ActCode.FRAMEING)
      return "FRAMEING";
    if (code == V3ActCode.LENSING)
      return "LENSING";
    if (code == V3ActCode.PRDING)
      return "PRDING";
    if (code == V3ActCode._ACTINVOICEROOTGROUPCODE)
      return "_ActInvoiceRootGroupCode";
    if (code == V3ActCode.CPINV)
      return "CPINV";
    if (code == V3ActCode.CSINV)
      return "CSINV";
    if (code == V3ActCode.CSPINV)
      return "CSPINV";
    if (code == V3ActCode.FININV)
      return "FININV";
    if (code == V3ActCode.OHSINV)
      return "OHSINV";
    if (code == V3ActCode.PAINV)
      return "PAINV";
    if (code == V3ActCode.RXCINV)
      return "RXCINV";
    if (code == V3ActCode.RXDINV)
      return "RXDINV";
    if (code == V3ActCode.SBFINV)
      return "SBFINV";
    if (code == V3ActCode.VRXINV)
      return "VRXINV";
    if (code == V3ActCode._ACTINVOICEELEMENTSUMMARYCODE)
      return "_ActInvoiceElementSummaryCode";
    if (code == V3ActCode._INVOICEELEMENTADJUDICATED)
      return "_InvoiceElementAdjudicated";
    if (code == V3ActCode.ADNFPPELAT)
      return "ADNFPPELAT";
    if (code == V3ActCode.ADNFPPELCT)
      return "ADNFPPELCT";
    if (code == V3ActCode.ADNFPPMNAT)
      return "ADNFPPMNAT";
    if (code == V3ActCode.ADNFPPMNCT)
      return "ADNFPPMNCT";
    if (code == V3ActCode.ADNFSPELAT)
      return "ADNFSPELAT";
    if (code == V3ActCode.ADNFSPELCT)
      return "ADNFSPELCT";
    if (code == V3ActCode.ADNFSPMNAT)
      return "ADNFSPMNAT";
    if (code == V3ActCode.ADNFSPMNCT)
      return "ADNFSPMNCT";
    if (code == V3ActCode.ADNPPPELAT)
      return "ADNPPPELAT";
    if (code == V3ActCode.ADNPPPELCT)
      return "ADNPPPELCT";
    if (code == V3ActCode.ADNPPPMNAT)
      return "ADNPPPMNAT";
    if (code == V3ActCode.ADNPPPMNCT)
      return "ADNPPPMNCT";
    if (code == V3ActCode.ADNPSPELAT)
      return "ADNPSPELAT";
    if (code == V3ActCode.ADNPSPELCT)
      return "ADNPSPELCT";
    if (code == V3ActCode.ADNPSPMNAT)
      return "ADNPSPMNAT";
    if (code == V3ActCode.ADNPSPMNCT)
      return "ADNPSPMNCT";
    if (code == V3ActCode.ADPPPPELAT)
      return "ADPPPPELAT";
    if (code == V3ActCode.ADPPPPELCT)
      return "ADPPPPELCT";
    if (code == V3ActCode.ADPPPPMNAT)
      return "ADPPPPMNAT";
    if (code == V3ActCode.ADPPPPMNCT)
      return "ADPPPPMNCT";
    if (code == V3ActCode.ADPPSPELAT)
      return "ADPPSPELAT";
    if (code == V3ActCode.ADPPSPELCT)
      return "ADPPSPELCT";
    if (code == V3ActCode.ADPPSPMNAT)
      return "ADPPSPMNAT";
    if (code == V3ActCode.ADPPSPMNCT)
      return "ADPPSPMNCT";
    if (code == V3ActCode.ADRFPPELAT)
      return "ADRFPPELAT";
    if (code == V3ActCode.ADRFPPELCT)
      return "ADRFPPELCT";
    if (code == V3ActCode.ADRFPPMNAT)
      return "ADRFPPMNAT";
    if (code == V3ActCode.ADRFPPMNCT)
      return "ADRFPPMNCT";
    if (code == V3ActCode.ADRFSPELAT)
      return "ADRFSPELAT";
    if (code == V3ActCode.ADRFSPELCT)
      return "ADRFSPELCT";
    if (code == V3ActCode.ADRFSPMNAT)
      return "ADRFSPMNAT";
    if (code == V3ActCode.ADRFSPMNCT)
      return "ADRFSPMNCT";
    if (code == V3ActCode._INVOICEELEMENTPAID)
      return "_InvoiceElementPaid";
    if (code == V3ActCode.PDNFPPELAT)
      return "PDNFPPELAT";
    if (code == V3ActCode.PDNFPPELCT)
      return "PDNFPPELCT";
    if (code == V3ActCode.PDNFPPMNAT)
      return "PDNFPPMNAT";
    if (code == V3ActCode.PDNFPPMNCT)
      return "PDNFPPMNCT";
    if (code == V3ActCode.PDNFSPELAT)
      return "PDNFSPELAT";
    if (code == V3ActCode.PDNFSPELCT)
      return "PDNFSPELCT";
    if (code == V3ActCode.PDNFSPMNAT)
      return "PDNFSPMNAT";
    if (code == V3ActCode.PDNFSPMNCT)
      return "PDNFSPMNCT";
    if (code == V3ActCode.PDNPPPELAT)
      return "PDNPPPELAT";
    if (code == V3ActCode.PDNPPPELCT)
      return "PDNPPPELCT";
    if (code == V3ActCode.PDNPPPMNAT)
      return "PDNPPPMNAT";
    if (code == V3ActCode.PDNPPPMNCT)
      return "PDNPPPMNCT";
    if (code == V3ActCode.PDNPSPELAT)
      return "PDNPSPELAT";
    if (code == V3ActCode.PDNPSPELCT)
      return "PDNPSPELCT";
    if (code == V3ActCode.PDNPSPMNAT)
      return "PDNPSPMNAT";
    if (code == V3ActCode.PDNPSPMNCT)
      return "PDNPSPMNCT";
    if (code == V3ActCode.PDPPPPELAT)
      return "PDPPPPELAT";
    if (code == V3ActCode.PDPPPPELCT)
      return "PDPPPPELCT";
    if (code == V3ActCode.PDPPPPMNAT)
      return "PDPPPPMNAT";
    if (code == V3ActCode.PDPPPPMNCT)
      return "PDPPPPMNCT";
    if (code == V3ActCode.PDPPSPELAT)
      return "PDPPSPELAT";
    if (code == V3ActCode.PDPPSPELCT)
      return "PDPPSPELCT";
    if (code == V3ActCode.PDPPSPMNAT)
      return "PDPPSPMNAT";
    if (code == V3ActCode.PDPPSPMNCT)
      return "PDPPSPMNCT";
    if (code == V3ActCode._INVOICEELEMENTSUBMITTED)
      return "_InvoiceElementSubmitted";
    if (code == V3ActCode.SBBLELAT)
      return "SBBLELAT";
    if (code == V3ActCode.SBBLELCT)
      return "SBBLELCT";
    if (code == V3ActCode.SBNFELAT)
      return "SBNFELAT";
    if (code == V3ActCode.SBNFELCT)
      return "SBNFELCT";
    if (code == V3ActCode.SBPDELAT)
      return "SBPDELAT";
    if (code == V3ActCode.SBPDELCT)
      return "SBPDELCT";
    if (code == V3ActCode._ACTINVOICEOVERRIDECODE)
      return "_ActInvoiceOverrideCode";
    if (code == V3ActCode.COVGE)
      return "COVGE";
    if (code == V3ActCode.EFORM)
      return "EFORM";
    if (code == V3ActCode.FAX)
      return "FAX";
    if (code == V3ActCode.GFTH)
      return "GFTH";
    if (code == V3ActCode.LATE)
      return "LATE";
    if (code == V3ActCode.MANUAL)
      return "MANUAL";
    if (code == V3ActCode.OOJ)
      return "OOJ";
    if (code == V3ActCode.ORTHO)
      return "ORTHO";
    if (code == V3ActCode.PAPER)
      return "PAPER";
    if (code == V3ActCode.PIE)
      return "PIE";
    if (code == V3ActCode.PYRDELAY)
      return "PYRDELAY";
    if (code == V3ActCode.REFNR)
      return "REFNR";
    if (code == V3ActCode.REPSERV)
      return "REPSERV";
    if (code == V3ActCode.UNRELAT)
      return "UNRELAT";
    if (code == V3ActCode.VERBAUTH)
      return "VERBAUTH";
    if (code == V3ActCode._ACTLISTCODE)
      return "_ActListCode";
    if (code == V3ActCode._ACTOBSERVATIONLIST)
      return "_ActObservationList";
    if (code == V3ActCode.CARELIST)
      return "CARELIST";
    if (code == V3ActCode.CONDLIST)
      return "CONDLIST";
    if (code == V3ActCode.INTOLIST)
      return "INTOLIST";
    if (code == V3ActCode.PROBLIST)
      return "PROBLIST";
    if (code == V3ActCode.RISKLIST)
      return "RISKLIST";
    if (code == V3ActCode.GOALLIST)
      return "GOALLIST";
    if (code == V3ActCode._ACTTHERAPYDURATIONWORKINGLISTCODE)
      return "_ActTherapyDurationWorkingListCode";
    if (code == V3ActCode._ACTMEDICATIONTHERAPYDURATIONWORKINGLISTCODE)
      return "_ActMedicationTherapyDurationWorkingListCode";
    if (code == V3ActCode.ACU)
      return "ACU";
    if (code == V3ActCode.CHRON)
      return "CHRON";
    if (code == V3ActCode.ONET)
      return "ONET";
    if (code == V3ActCode.PRN)
      return "PRN";
    if (code == V3ActCode.MEDLIST)
      return "MEDLIST";
    if (code == V3ActCode.CURMEDLIST)
      return "CURMEDLIST";
    if (code == V3ActCode.DISCMEDLIST)
      return "DISCMEDLIST";
    if (code == V3ActCode.HISTMEDLIST)
      return "HISTMEDLIST";
    if (code == V3ActCode._ACTMONITORINGPROTOCOLCODE)
      return "_ActMonitoringProtocolCode";
    if (code == V3ActCode.CTLSUB)
      return "CTLSUB";
    if (code == V3ActCode.INV)
      return "INV";
    if (code == V3ActCode.LU)
      return "LU";
    if (code == V3ActCode.OTC)
      return "OTC";
    if (code == V3ActCode.RX)
      return "RX";
    if (code == V3ActCode.SA)
      return "SA";
    if (code == V3ActCode.SAC)
      return "SAC";
    if (code == V3ActCode._ACTNONOBSERVATIONINDICATIONCODE)
      return "_ActNonObservationIndicationCode";
    if (code == V3ActCode.IND01)
      return "IND01";
    if (code == V3ActCode.IND02)
      return "IND02";
    if (code == V3ActCode.IND03)
      return "IND03";
    if (code == V3ActCode.IND04)
      return "IND04";
    if (code == V3ActCode.IND05)
      return "IND05";
    if (code == V3ActCode._ACTOBSERVATIONVERIFICATIONTYPE)
      return "_ActObservationVerificationType";
    if (code == V3ActCode.VFPAPER)
      return "VFPAPER";
    if (code == V3ActCode._ACTPAYMENTCODE)
      return "_ActPaymentCode";
    if (code == V3ActCode.ACH)
      return "ACH";
    if (code == V3ActCode.CHK)
      return "CHK";
    if (code == V3ActCode.DDP)
      return "DDP";
    if (code == V3ActCode.NON)
      return "NON";
    if (code == V3ActCode._ACTPHARMACYSUPPLYTYPE)
      return "_ActPharmacySupplyType";
    if (code == V3ActCode.DF)
      return "DF";
    if (code == V3ActCode.EM)
      return "EM";
    if (code == V3ActCode.SO)
      return "SO";
    if (code == V3ActCode.FF)
      return "FF";
    if (code == V3ActCode.FFC)
      return "FFC";
    if (code == V3ActCode.FFP)
      return "FFP";
    if (code == V3ActCode.FFSS)
      return "FFSS";
    if (code == V3ActCode.TF)
      return "TF";
    if (code == V3ActCode.FS)
      return "FS";
    if (code == V3ActCode.MS)
      return "MS";
    if (code == V3ActCode.RF)
      return "RF";
    if (code == V3ActCode.UD)
      return "UD";
    if (code == V3ActCode.RFC)
      return "RFC";
    if (code == V3ActCode.RFCS)
      return "RFCS";
    if (code == V3ActCode.RFF)
      return "RFF";
    if (code == V3ActCode.RFFS)
      return "RFFS";
    if (code == V3ActCode.RFP)
      return "RFP";
    if (code == V3ActCode.RFPS)
      return "RFPS";
    if (code == V3ActCode.RFS)
      return "RFS";
    if (code == V3ActCode.TB)
      return "TB";
    if (code == V3ActCode.TBS)
      return "TBS";
    if (code == V3ActCode.UDE)
      return "UDE";
    if (code == V3ActCode._ACTPOLICYTYPE)
      return "_ActPolicyType";
    if (code == V3ActCode._ACTPRIVACYPOLICY)
      return "_ActPrivacyPolicy";
    if (code == V3ActCode._ACTCONSENTDIRECTIVE)
      return "_ActConsentDirective";
    if (code == V3ActCode.EMRGONLY)
      return "EMRGONLY";
    if (code == V3ActCode.NOPP)
      return "NOPP";
    if (code == V3ActCode.OPTIN)
      return "OPTIN";
    if (code == V3ActCode.OPTOUT)
      return "OPTOUT";
    if (code == V3ActCode._INFORMATIONSENSITIVITYPOLICY)
      return "_InformationSensitivityPolicy";
    if (code == V3ActCode._ACTINFORMATIONSENSITIVITYPOLICY)
      return "_ActInformationSensitivityPolicy";
    if (code == V3ActCode.ETH)
      return "ETH";
    if (code == V3ActCode.GDIS)
      return "GDIS";
    if (code == V3ActCode.HIV)
      return "HIV";
    if (code == V3ActCode.PSY)
      return "PSY";
    if (code == V3ActCode.SCA)
      return "SCA";
    if (code == V3ActCode.SDV)
      return "SDV";
    if (code == V3ActCode.SEX)
      return "SEX";
    if (code == V3ActCode.STD)
      return "STD";
    if (code == V3ActCode.TBOO)
      return "TBOO";
    if (code == V3ActCode.SICKLE)
      return "SICKLE";
    if (code == V3ActCode._ENTITYSENSITIVITYPOLICYTYPE)
      return "_EntitySensitivityPolicyType";
    if (code == V3ActCode.DEMO)
      return "DEMO";
    if (code == V3ActCode.DOB)
      return "DOB";
    if (code == V3ActCode.GENDER)
      return "GENDER";
    if (code == V3ActCode.LIVARG)
      return "LIVARG";
    if (code == V3ActCode.MARST)
      return "MARST";
    if (code == V3ActCode.RACE)
      return "RACE";
    if (code == V3ActCode.REL)
      return "REL";
    if (code == V3ActCode._ROLEINFORMATIONSENSITIVITYPOLICY)
      return "_RoleInformationSensitivityPolicy";
    if (code == V3ActCode.B)
      return "B";
    if (code == V3ActCode.EMPL)
      return "EMPL";
    if (code == V3ActCode.LOCIS)
      return "LOCIS";
    if (code == V3ActCode.SSP)
      return "SSP";
    if (code == V3ActCode.ADOL)
      return "ADOL";
    if (code == V3ActCode.CEL)
      return "CEL";
    if (code == V3ActCode.DIA)
      return "DIA";
    if (code == V3ActCode.DRGIS)
      return "DRGIS";
    if (code == V3ActCode.EMP)
      return "EMP";
    if (code == V3ActCode.PDS)
      return "PDS";
    if (code == V3ActCode.PRS)
      return "PRS";
    if (code == V3ActCode.COMPT)
      return "COMPT";
    if (code == V3ActCode.HRCOMPT)
      return "HRCOMPT";
    if (code == V3ActCode.RESCOMPT)
      return "RESCOMPT";
    if (code == V3ActCode.RMGTCOMPT)
      return "RMGTCOMPT";
    if (code == V3ActCode.ACTTRUSTPOLICYTYPE)
      return "ActTrustPolicyType";
    if (code == V3ActCode.TRSTACCRD)
      return "TRSTACCRD";
    if (code == V3ActCode.TRSTAGRE)
      return "TRSTAGRE";
    if (code == V3ActCode.TRSTASSUR)
      return "TRSTASSUR";
    if (code == V3ActCode.TRSTCERT)
      return "TRSTCERT";
    if (code == V3ActCode.TRSTFWK)
      return "TRSTFWK";
    if (code == V3ActCode.TRSTMEC)
      return "TRSTMEC";
    if (code == V3ActCode.COVPOL)
      return "COVPOL";
    if (code == V3ActCode.SECURITYPOLICY)
      return "SecurityPolicy";
    if (code == V3ActCode.OBLIGATIONPOLICY)
      return "ObligationPolicy";
    if (code == V3ActCode.ANONY)
      return "ANONY";
    if (code == V3ActCode.AOD)
      return "AOD";
    if (code == V3ActCode.AUDIT)
      return "AUDIT";
    if (code == V3ActCode.AUDTR)
      return "AUDTR";
    if (code == V3ActCode.CPLYCC)
      return "CPLYCC";
    if (code == V3ActCode.CPLYCD)
      return "CPLYCD";
    if (code == V3ActCode.CPLYJPP)
      return "CPLYJPP";
    if (code == V3ActCode.CPLYOPP)
      return "CPLYOPP";
    if (code == V3ActCode.CPLYOSP)
      return "CPLYOSP";
    if (code == V3ActCode.CPLYPOL)
      return "CPLYPOL";
    if (code == V3ActCode.DEID)
      return "DEID";
    if (code == V3ActCode.DELAU)
      return "DELAU";
    if (code == V3ActCode.ENCRYPT)
      return "ENCRYPT";
    if (code == V3ActCode.ENCRYPTR)
      return "ENCRYPTR";
    if (code == V3ActCode.ENCRYPTT)
      return "ENCRYPTT";
    if (code == V3ActCode.ENCRYPTU)
      return "ENCRYPTU";
    if (code == V3ActCode.HUAPRV)
      return "HUAPRV";
    if (code == V3ActCode.MASK)
      return "MASK";
    if (code == V3ActCode.MINEC)
      return "MINEC";
    if (code == V3ActCode.PRIVMARK)
      return "PRIVMARK";
    if (code == V3ActCode.PSEUD)
      return "PSEUD";
    if (code == V3ActCode.REDACT)
      return "REDACT";
    if (code == V3ActCode.REFRAINPOLICY)
      return "RefrainPolicy";
    if (code == V3ActCode.NOAUTH)
      return "NOAUTH";
    if (code == V3ActCode.NOCOLLECT)
      return "NOCOLLECT";
    if (code == V3ActCode.NODSCLCD)
      return "NODSCLCD";
    if (code == V3ActCode.NODSCLCDS)
      return "NODSCLCDS";
    if (code == V3ActCode.NOINTEGRATE)
      return "NOINTEGRATE";
    if (code == V3ActCode.NOLIST)
      return "NOLIST";
    if (code == V3ActCode.NOMOU)
      return "NOMOU";
    if (code == V3ActCode.NOORGPOL)
      return "NOORGPOL";
    if (code == V3ActCode.NOPAT)
      return "NOPAT";
    if (code == V3ActCode.NOPERSISTP)
      return "NOPERSISTP";
    if (code == V3ActCode.NORDSCLCD)
      return "NORDSCLCD";
    if (code == V3ActCode.NORDSCLCDS)
      return "NORDSCLCDS";
    if (code == V3ActCode.NORDSCLW)
      return "NORDSCLW";
    if (code == V3ActCode.NORELINK)
      return "NORELINK";
    if (code == V3ActCode.NOREUSE)
      return "NOREUSE";
    if (code == V3ActCode.NOVIP)
      return "NOVIP";
    if (code == V3ActCode.ORCON)
      return "ORCON";
    if (code == V3ActCode._ACTPRODUCTACQUISITIONCODE)
      return "_ActProductAcquisitionCode";
    if (code == V3ActCode.LOAN)
      return "LOAN";
    if (code == V3ActCode.RENT)
      return "RENT";
    if (code == V3ActCode.TRANSFER)
      return "TRANSFER";
    if (code == V3ActCode.SALE)
      return "SALE";
    if (code == V3ActCode._ACTSPECIMENTRANSPORTCODE)
      return "_ActSpecimenTransportCode";
    if (code == V3ActCode.SREC)
      return "SREC";
    if (code == V3ActCode.SSTOR)
      return "SSTOR";
    if (code == V3ActCode.STRAN)
      return "STRAN";
    if (code == V3ActCode._ACTSPECIMENTREATMENTCODE)
      return "_ActSpecimenTreatmentCode";
    if (code == V3ActCode.ACID)
      return "ACID";
    if (code == V3ActCode.ALK)
      return "ALK";
    if (code == V3ActCode.DEFB)
      return "DEFB";
    if (code == V3ActCode.FILT)
      return "FILT";
    if (code == V3ActCode.LDLP)
      return "LDLP";
    if (code == V3ActCode.NEUT)
      return "NEUT";
    if (code == V3ActCode.RECA)
      return "RECA";
    if (code == V3ActCode.UFIL)
      return "UFIL";
    if (code == V3ActCode._ACTSUBSTANCEADMINISTRATIONCODE)
      return "_ActSubstanceAdministrationCode";
    if (code == V3ActCode.DRUG)
      return "DRUG";
    if (code == V3ActCode.FD)
      return "FD";
    if (code == V3ActCode.IMMUNIZ)
      return "IMMUNIZ";
    if (code == V3ActCode.BOOSTER)
      return "BOOSTER";
    if (code == V3ActCode.INITIMMUNIZ)
      return "INITIMMUNIZ";
    if (code == V3ActCode._ACTTASKCODE)
      return "_ActTaskCode";
    if (code == V3ActCode.OE)
      return "OE";
    if (code == V3ActCode.LABOE)
      return "LABOE";
    if (code == V3ActCode.MEDOE)
      return "MEDOE";
    if (code == V3ActCode.PATDOC)
      return "PATDOC";
    if (code == V3ActCode.ALLERLREV)
      return "ALLERLREV";
    if (code == V3ActCode.CLINNOTEE)
      return "CLINNOTEE";
    if (code == V3ActCode.DIAGLISTE)
      return "DIAGLISTE";
    if (code == V3ActCode.DISCHINSTE)
      return "DISCHINSTE";
    if (code == V3ActCode.DISCHSUME)
      return "DISCHSUME";
    if (code == V3ActCode.PATEDUE)
      return "PATEDUE";
    if (code == V3ActCode.PATREPE)
      return "PATREPE";
    if (code == V3ActCode.PROBLISTE)
      return "PROBLISTE";
    if (code == V3ActCode.RADREPE)
      return "RADREPE";
    if (code == V3ActCode.IMMLREV)
      return "IMMLREV";
    if (code == V3ActCode.REMLREV)
      return "REMLREV";
    if (code == V3ActCode.WELLREMLREV)
      return "WELLREMLREV";
    if (code == V3ActCode.PATINFO)
      return "PATINFO";
    if (code == V3ActCode.ALLERLE)
      return "ALLERLE";
    if (code == V3ActCode.CDSREV)
      return "CDSREV";
    if (code == V3ActCode.CLINNOTEREV)
      return "CLINNOTEREV";
    if (code == V3ActCode.DISCHSUMREV)
      return "DISCHSUMREV";
    if (code == V3ActCode.DIAGLISTREV)
      return "DIAGLISTREV";
    if (code == V3ActCode.IMMLE)
      return "IMMLE";
    if (code == V3ActCode.LABRREV)
      return "LABRREV";
    if (code == V3ActCode.MICRORREV)
      return "MICRORREV";
    if (code == V3ActCode.MICROORGRREV)
      return "MICROORGRREV";
    if (code == V3ActCode.MICROSENSRREV)
      return "MICROSENSRREV";
    if (code == V3ActCode.MLREV)
      return "MLREV";
    if (code == V3ActCode.MARWLREV)
      return "MARWLREV";
    if (code == V3ActCode.OREV)
      return "OREV";
    if (code == V3ActCode.PATREPREV)
      return "PATREPREV";
    if (code == V3ActCode.PROBLISTREV)
      return "PROBLISTREV";
    if (code == V3ActCode.RADREPREV)
      return "RADREPREV";
    if (code == V3ActCode.REMLE)
      return "REMLE";
    if (code == V3ActCode.WELLREMLE)
      return "WELLREMLE";
    if (code == V3ActCode.RISKASSESS)
      return "RISKASSESS";
    if (code == V3ActCode.FALLRISK)
      return "FALLRISK";
    if (code == V3ActCode._ACTTRANSPORTATIONMODECODE)
      return "_ActTransportationModeCode";
    if (code == V3ActCode._ACTPATIENTTRANSPORTATIONMODECODE)
      return "_ActPatientTransportationModeCode";
    if (code == V3ActCode.AFOOT)
      return "AFOOT";
    if (code == V3ActCode.AMBT)
      return "AMBT";
    if (code == V3ActCode.AMBAIR)
      return "AMBAIR";
    if (code == V3ActCode.AMBGRND)
      return "AMBGRND";
    if (code == V3ActCode.AMBHELO)
      return "AMBHELO";
    if (code == V3ActCode.LAWENF)
      return "LAWENF";
    if (code == V3ActCode.PRVTRN)
      return "PRVTRN";
    if (code == V3ActCode.PUBTRN)
      return "PUBTRN";
    if (code == V3ActCode._OBSERVATIONTYPE)
      return "_ObservationType";
    if (code == V3ActCode._ACTSPECOBSCODE)
      return "_ActSpecObsCode";
    if (code == V3ActCode.ARTBLD)
      return "ARTBLD";
    if (code == V3ActCode.DILUTION)
      return "DILUTION";
    if (code == V3ActCode.AUTOHIGH)
      return "AUTO-HIGH";
    if (code == V3ActCode.AUTOLOW)
      return "AUTO-LOW";
    if (code == V3ActCode.PRE)
      return "PRE";
    if (code == V3ActCode.RERUN)
      return "RERUN";
    if (code == V3ActCode.EVNFCTS)
      return "EVNFCTS";
    if (code == V3ActCode.INTFR)
      return "INTFR";
    if (code == V3ActCode.FIBRIN)
      return "FIBRIN";
    if (code == V3ActCode.HEMOLYSIS)
      return "HEMOLYSIS";
    if (code == V3ActCode.ICTERUS)
      return "ICTERUS";
    if (code == V3ActCode.LIPEMIA)
      return "LIPEMIA";
    if (code == V3ActCode.VOLUME)
      return "VOLUME";
    if (code == V3ActCode.AVAILABLE)
      return "AVAILABLE";
    if (code == V3ActCode.CONSUMPTION)
      return "CONSUMPTION";
    if (code == V3ActCode.CURRENT)
      return "CURRENT";
    if (code == V3ActCode.INITIAL)
      return "INITIAL";
    if (code == V3ActCode._ANNOTATIONTYPE)
      return "_AnnotationType";
    if (code == V3ActCode._ACTPATIENTANNOTATIONTYPE)
      return "_ActPatientAnnotationType";
    if (code == V3ActCode.ANNDI)
      return "ANNDI";
    if (code == V3ActCode.ANNGEN)
      return "ANNGEN";
    if (code == V3ActCode.ANNIMM)
      return "ANNIMM";
    if (code == V3ActCode.ANNLAB)
      return "ANNLAB";
    if (code == V3ActCode.ANNMED)
      return "ANNMED";
    if (code == V3ActCode._GENETICOBSERVATIONTYPE)
      return "_GeneticObservationType";
    if (code == V3ActCode.GENE)
      return "GENE";
    if (code == V3ActCode._IMMUNIZATIONOBSERVATIONTYPE)
      return "_ImmunizationObservationType";
    if (code == V3ActCode.OBSANTC)
      return "OBSANTC";
    if (code == V3ActCode.OBSANTV)
      return "OBSANTV";
    if (code == V3ActCode._INDIVIDUALCASESAFETYREPORTTYPE)
      return "_IndividualCaseSafetyReportType";
    if (code == V3ActCode.PATADVEVNT)
      return "PAT_ADV_EVNT";
    if (code == V3ActCode.VACPROBLEM)
      return "VAC_PROBLEM";
    if (code == V3ActCode._LOINCOBSERVATIONACTCONTEXTAGETYPE)
      return "_LOINCObservationActContextAgeType";
    if (code == V3ActCode._216119)
      return "21611-9";
    if (code == V3ActCode._216127)
      return "21612-7";
    if (code == V3ActCode._295535)
      return "29553-5";
    if (code == V3ActCode._305250)
      return "30525-0";
    if (code == V3ActCode._309724)
      return "30972-4";
    if (code == V3ActCode._MEDICATIONOBSERVATIONTYPE)
      return "_MedicationObservationType";
    if (code == V3ActCode.REPHALFLIFE)
      return "REP_HALF_LIFE";
    if (code == V3ActCode.SPLCOATING)
      return "SPLCOATING";
    if (code == V3ActCode.SPLCOLOR)
      return "SPLCOLOR";
    if (code == V3ActCode.SPLIMAGE)
      return "SPLIMAGE";
    if (code == V3ActCode.SPLIMPRINT)
      return "SPLIMPRINT";
    if (code == V3ActCode.SPLSCORING)
      return "SPLSCORING";
    if (code == V3ActCode.SPLSHAPE)
      return "SPLSHAPE";
    if (code == V3ActCode.SPLSIZE)
      return "SPLSIZE";
    if (code == V3ActCode.SPLSYMBOL)
      return "SPLSYMBOL";
    if (code == V3ActCode._OBSERVATIONISSUETRIGGERCODEDOBSERVATIONTYPE)
      return "_ObservationIssueTriggerCodedObservationType";
    if (code == V3ActCode._CASETRANSMISSIONMODE)
      return "_CaseTransmissionMode";
    if (code == V3ActCode.AIRTRNS)
      return "AIRTRNS";
    if (code == V3ActCode.ANANTRNS)
      return "ANANTRNS";
    if (code == V3ActCode.ANHUMTRNS)
      return "ANHUMTRNS";
    if (code == V3ActCode.BDYFLDTRNS)
      return "BDYFLDTRNS";
    if (code == V3ActCode.BLDTRNS)
      return "BLDTRNS";
    if (code == V3ActCode.DERMTRNS)
      return "DERMTRNS";
    if (code == V3ActCode.ENVTRNS)
      return "ENVTRNS";
    if (code == V3ActCode.FECTRNS)
      return "FECTRNS";
    if (code == V3ActCode.FOMTRNS)
      return "FOMTRNS";
    if (code == V3ActCode.FOODTRNS)
      return "FOODTRNS";
    if (code == V3ActCode.HUMHUMTRNS)
      return "HUMHUMTRNS";
    if (code == V3ActCode.INDTRNS)
      return "INDTRNS";
    if (code == V3ActCode.LACTTRNS)
      return "LACTTRNS";
    if (code == V3ActCode.NOSTRNS)
      return "NOSTRNS";
    if (code == V3ActCode.PARTRNS)
      return "PARTRNS";
    if (code == V3ActCode.PLACTRNS)
      return "PLACTRNS";
    if (code == V3ActCode.SEXTRNS)
      return "SEXTRNS";
    if (code == V3ActCode.TRNSFTRNS)
      return "TRNSFTRNS";
    if (code == V3ActCode.VECTRNS)
      return "VECTRNS";
    if (code == V3ActCode.WATTRNS)
      return "WATTRNS";
    if (code == V3ActCode._OBSERVATIONQUALITYMEASUREATTRIBUTE)
      return "_ObservationQualityMeasureAttribute";
    if (code == V3ActCode.AGGREGATE)
      return "AGGREGATE";
    if (code == V3ActCode.COPY)
      return "COPY";
    if (code == V3ActCode.CRS)
      return "CRS";
    if (code == V3ActCode.DEF)
      return "DEF";
    if (code == V3ActCode.DISC)
      return "DISC";
    if (code == V3ActCode.FINALDT)
      return "FINALDT";
    if (code == V3ActCode.GUIDE)
      return "GUIDE";
    if (code == V3ActCode.IDUR)
      return "IDUR";
    if (code == V3ActCode.ITMCNT)
      return "ITMCNT";
    if (code == V3ActCode.KEY)
      return "KEY";
    if (code == V3ActCode.MEDT)
      return "MEDT";
    if (code == V3ActCode.MSD)
      return "MSD";
    if (code == V3ActCode.MSRADJ)
      return "MSRADJ";
    if (code == V3ActCode.MSRAGG)
      return "MSRAGG";
    if (code == V3ActCode.MSRIMPROV)
      return "MSRIMPROV";
    if (code == V3ActCode.MSRJUR)
      return "MSRJUR";
    if (code == V3ActCode.MSRRPTR)
      return "MSRRPTR";
    if (code == V3ActCode.MSRRPTTIME)
      return "MSRRPTTIME";
    if (code == V3ActCode.MSRSCORE)
      return "MSRSCORE";
    if (code == V3ActCode.MSRSET)
      return "MSRSET";
    if (code == V3ActCode.MSRTOPIC)
      return "MSRTOPIC";
    if (code == V3ActCode.MSRTP)
      return "MSRTP";
    if (code == V3ActCode.MSRTYPE)
      return "MSRTYPE";
    if (code == V3ActCode.RAT)
      return "RAT";
    if (code == V3ActCode.REF)
      return "REF";
    if (code == V3ActCode.SDE)
      return "SDE";
    if (code == V3ActCode.STRAT)
      return "STRAT";
    if (code == V3ActCode.TRANF)
      return "TRANF";
    if (code == V3ActCode.USE)
      return "USE";
    if (code == V3ActCode._OBSERVATIONSEQUENCETYPE)
      return "_ObservationSequenceType";
    if (code == V3ActCode.TIMEABSOLUTE)
      return "TIME_ABSOLUTE";
    if (code == V3ActCode.TIMERELATIVE)
      return "TIME_RELATIVE";
    if (code == V3ActCode._OBSERVATIONSERIESTYPE)
      return "_ObservationSeriesType";
    if (code == V3ActCode._ECGOBSERVATIONSERIESTYPE)
      return "_ECGObservationSeriesType";
    if (code == V3ActCode.REPRESENTATIVEBEAT)
      return "REPRESENTATIVE_BEAT";
    if (code == V3ActCode.RHYTHM)
      return "RHYTHM";
    if (code == V3ActCode._PATIENTIMMUNIZATIONRELATEDOBSERVATIONTYPE)
      return "_PatientImmunizationRelatedObservationType";
    if (code == V3ActCode.CLSSRM)
      return "CLSSRM";
    if (code == V3ActCode.GRADE)
      return "GRADE";
    if (code == V3ActCode.SCHL)
      return "SCHL";
    if (code == V3ActCode.SCHLDIV)
      return "SCHLDIV";
    if (code == V3ActCode.TEACHER)
      return "TEACHER";
    if (code == V3ActCode._POPULATIONINCLUSIONOBSERVATIONTYPE)
      return "_PopulationInclusionObservationType";
    if (code == V3ActCode.DENEX)
      return "DENEX";
    if (code == V3ActCode.DENEXCEP)
      return "DENEXCEP";
    if (code == V3ActCode.DENOM)
      return "DENOM";
    if (code == V3ActCode.IPOP)
      return "IPOP";
    if (code == V3ActCode.IPPOP)
      return "IPPOP";
    if (code == V3ActCode.MSRPOPL)
      return "MSRPOPL";
    if (code == V3ActCode.MSRPOPLEX)
      return "MSRPOPLEX";
    if (code == V3ActCode.NUMER)
      return "NUMER";
    if (code == V3ActCode.NUMEX)
      return "NUMEX";
    if (code == V3ActCode._PREFERENCEOBSERVATIONTYPE)
      return "_PreferenceObservationType";
    if (code == V3ActCode.PREFSTRENGTH)
      return "PREFSTRENGTH";
    if (code == V3ActCode.ADVERSEREACTION)
      return "ADVERSE_REACTION";
    if (code == V3ActCode.ASSERTION)
      return "ASSERTION";
    if (code == V3ActCode.CASESER)
      return "CASESER";
    if (code == V3ActCode.CDIO)
      return "CDIO";
    if (code == V3ActCode.CRIT)
      return "CRIT";
    if (code == V3ActCode.CTMO)
      return "CTMO";
    if (code == V3ActCode.DX)
      return "DX";
    if (code == V3ActCode.ADMDX)
      return "ADMDX";
    if (code == V3ActCode.DISDX)
      return "DISDX";
    if (code == V3ActCode.INTDX)
      return "INTDX";
    if (code == V3ActCode.NOI)
      return "NOI";
    if (code == V3ActCode.GISTIER)
      return "GISTIER";
    if (code == V3ActCode.HHOBS)
      return "HHOBS";
    if (code == V3ActCode.ISSUE)
      return "ISSUE";
    if (code == V3ActCode._ACTADMINISTRATIVEDETECTEDISSUECODE)
      return "_ActAdministrativeDetectedIssueCode";
    if (code == V3ActCode._ACTADMINISTRATIVEAUTHORIZATIONDETECTEDISSUECODE)
      return "_ActAdministrativeAuthorizationDetectedIssueCode";
    if (code == V3ActCode.NAT)
      return "NAT";
    if (code == V3ActCode.SUPPRESSED)
      return "SUPPRESSED";
    if (code == V3ActCode.VALIDAT)
      return "VALIDAT";
    if (code == V3ActCode.KEY204)
      return "KEY204";
    if (code == V3ActCode.KEY205)
      return "KEY205";
    if (code == V3ActCode.COMPLY)
      return "COMPLY";
    if (code == V3ActCode.DUPTHPY)
      return "DUPTHPY";
    if (code == V3ActCode.DUPTHPCLS)
      return "DUPTHPCLS";
    if (code == V3ActCode.DUPTHPGEN)
      return "DUPTHPGEN";
    if (code == V3ActCode.ABUSE)
      return "ABUSE";
    if (code == V3ActCode.FRAUD)
      return "FRAUD";
    if (code == V3ActCode.PLYDOC)
      return "PLYDOC";
    if (code == V3ActCode.PLYPHRM)
      return "PLYPHRM";
    if (code == V3ActCode.DOSE)
      return "DOSE";
    if (code == V3ActCode.DOSECOND)
      return "DOSECOND";
    if (code == V3ActCode.DOSEDUR)
      return "DOSEDUR";
    if (code == V3ActCode.DOSEDURH)
      return "DOSEDURH";
    if (code == V3ActCode.DOSEDURHIND)
      return "DOSEDURHIND";
    if (code == V3ActCode.DOSEDURL)
      return "DOSEDURL";
    if (code == V3ActCode.DOSEDURLIND)
      return "DOSEDURLIND";
    if (code == V3ActCode.DOSEH)
      return "DOSEH";
    if (code == V3ActCode.DOSEHINDA)
      return "DOSEHINDA";
    if (code == V3ActCode.DOSEHIND)
      return "DOSEHIND";
    if (code == V3ActCode.DOSEHINDSA)
      return "DOSEHINDSA";
    if (code == V3ActCode.DOSEHINDW)
      return "DOSEHINDW";
    if (code == V3ActCode.DOSEIVL)
      return "DOSEIVL";
    if (code == V3ActCode.DOSEIVLIND)
      return "DOSEIVLIND";
    if (code == V3ActCode.DOSEL)
      return "DOSEL";
    if (code == V3ActCode.DOSELINDA)
      return "DOSELINDA";
    if (code == V3ActCode.DOSELIND)
      return "DOSELIND";
    if (code == V3ActCode.DOSELINDSA)
      return "DOSELINDSA";
    if (code == V3ActCode.DOSELINDW)
      return "DOSELINDW";
    if (code == V3ActCode.MDOSE)
      return "MDOSE";
    if (code == V3ActCode.OBSA)
      return "OBSA";
    if (code == V3ActCode.AGE)
      return "AGE";
    if (code == V3ActCode.ADALRT)
      return "ADALRT";
    if (code == V3ActCode.GEALRT)
      return "GEALRT";
    if (code == V3ActCode.PEALRT)
      return "PEALRT";
    if (code == V3ActCode.COND)
      return "COND";
    if (code == V3ActCode.HGHT)
      return "HGHT";
    if (code == V3ActCode.LACT)
      return "LACT";
    if (code == V3ActCode.PREG)
      return "PREG";
    if (code == V3ActCode.WGHT)
      return "WGHT";
    if (code == V3ActCode.CREACT)
      return "CREACT";
    if (code == V3ActCode.GEN)
      return "GEN";
    if (code == V3ActCode.GEND)
      return "GEND";
    if (code == V3ActCode.LAB)
      return "LAB";
    if (code == V3ActCode.REACT)
      return "REACT";
    if (code == V3ActCode.ALGY)
      return "ALGY";
    if (code == V3ActCode.INT)
      return "INT";
    if (code == V3ActCode.RREACT)
      return "RREACT";
    if (code == V3ActCode.RALG)
      return "RALG";
    if (code == V3ActCode.RAR)
      return "RAR";
    if (code == V3ActCode.RINT)
      return "RINT";
    if (code == V3ActCode.BUS)
      return "BUS";
    if (code == V3ActCode.CODEINVAL)
      return "CODE_INVAL";
    if (code == V3ActCode.CODEDEPREC)
      return "CODE_DEPREC";
    if (code == V3ActCode.FORMAT)
      return "FORMAT";
    if (code == V3ActCode.ILLEGAL)
      return "ILLEGAL";
    if (code == V3ActCode.LENRANGE)
      return "LEN_RANGE";
    if (code == V3ActCode.LENLONG)
      return "LEN_LONG";
    if (code == V3ActCode.LENSHORT)
      return "LEN_SHORT";
    if (code == V3ActCode.MISSCOND)
      return "MISSCOND";
    if (code == V3ActCode.MISSMAND)
      return "MISSMAND";
    if (code == V3ActCode.NODUPS)
      return "NODUPS";
    if (code == V3ActCode.NOPERSIST)
      return "NOPERSIST";
    if (code == V3ActCode.REPRANGE)
      return "REP_RANGE";
    if (code == V3ActCode.MAXOCCURS)
      return "MAXOCCURS";
    if (code == V3ActCode.MINOCCURS)
      return "MINOCCURS";
    if (code == V3ActCode._ACTADMINISTRATIVERULEDETECTEDISSUECODE)
      return "_ActAdministrativeRuleDetectedIssueCode";
    if (code == V3ActCode.KEY206)
      return "KEY206";
    if (code == V3ActCode.OBSOLETE)
      return "OBSOLETE";
    if (code == V3ActCode._ACTSUPPLIEDITEMDETECTEDISSUECODE)
      return "_ActSuppliedItemDetectedIssueCode";
    if (code == V3ActCode._ADMINISTRATIONDETECTEDISSUECODE)
      return "_AdministrationDetectedIssueCode";
    if (code == V3ActCode._APPROPRIATENESSDETECTEDISSUECODE)
      return "_AppropriatenessDetectedIssueCode";
    if (code == V3ActCode._INTERACTIONDETECTEDISSUECODE)
      return "_InteractionDetectedIssueCode";
    if (code == V3ActCode.FOOD)
      return "FOOD";
    if (code == V3ActCode.TPROD)
      return "TPROD";
    if (code == V3ActCode.DRG)
      return "DRG";
    if (code == V3ActCode.NHP)
      return "NHP";
    if (code == V3ActCode.NONRX)
      return "NONRX";
    if (code == V3ActCode.PREVINEF)
      return "PREVINEF";
    if (code == V3ActCode.DACT)
      return "DACT";
    if (code == V3ActCode.TIME)
      return "TIME";
    if (code == V3ActCode.ALRTENDLATE)
      return "ALRTENDLATE";
    if (code == V3ActCode.ALRTSTRTLATE)
      return "ALRTSTRTLATE";
    if (code == V3ActCode._TIMINGDETECTEDISSUECODE)
      return "_TimingDetectedIssueCode";
    if (code == V3ActCode.ENDLATE)
      return "ENDLATE";
    if (code == V3ActCode.STRTLATE)
      return "STRTLATE";
    if (code == V3ActCode._SUPPLYDETECTEDISSUECODE)
      return "_SupplyDetectedIssueCode";
    if (code == V3ActCode.ALLDONE)
      return "ALLDONE";
    if (code == V3ActCode.FULFIL)
      return "FULFIL";
    if (code == V3ActCode.NOTACTN)
      return "NOTACTN";
    if (code == V3ActCode.NOTEQUIV)
      return "NOTEQUIV";
    if (code == V3ActCode.NOTEQUIVGEN)
      return "NOTEQUIVGEN";
    if (code == V3ActCode.NOTEQUIVTHER)
      return "NOTEQUIVTHER";
    if (code == V3ActCode.TIMING)
      return "TIMING";
    if (code == V3ActCode.INTERVAL)
      return "INTERVAL";
    if (code == V3ActCode.MINFREQ)
      return "MINFREQ";
    if (code == V3ActCode.HELD)
      return "HELD";
    if (code == V3ActCode.TOOLATE)
      return "TOOLATE";
    if (code == V3ActCode.TOOSOON)
      return "TOOSOON";
    if (code == V3ActCode.HISTORIC)
      return "HISTORIC";
    if (code == V3ActCode.PATPREF)
      return "PATPREF";
    if (code == V3ActCode.PATPREFALT)
      return "PATPREFALT";
    if (code == V3ActCode.KSUBJ)
      return "KSUBJ";
    if (code == V3ActCode.KSUBT)
      return "KSUBT";
    if (code == V3ActCode.OINT)
      return "OINT";
    if (code == V3ActCode.ALG)
      return "ALG";
    if (code == V3ActCode.DALG)
      return "DALG";
    if (code == V3ActCode.EALG)
      return "EALG";
    if (code == V3ActCode.FALG)
      return "FALG";
    if (code == V3ActCode.DINT)
      return "DINT";
    if (code == V3ActCode.DNAINT)
      return "DNAINT";
    if (code == V3ActCode.EINT)
      return "EINT";
    if (code == V3ActCode.ENAINT)
      return "ENAINT";
    if (code == V3ActCode.FINT)
      return "FINT";
    if (code == V3ActCode.FNAINT)
      return "FNAINT";
    if (code == V3ActCode.NAINT)
      return "NAINT";
    if (code == V3ActCode.SEV)
      return "SEV";
    if (code == V3ActCode._FDALABELDATA)
      return "_FDALabelData";
    if (code == V3ActCode.FDACOATING)
      return "FDACOATING";
    if (code == V3ActCode.FDACOLOR)
      return "FDACOLOR";
    if (code == V3ActCode.FDAIMPRINTCD)
      return "FDAIMPRINTCD";
    if (code == V3ActCode.FDALOGO)
      return "FDALOGO";
    if (code == V3ActCode.FDASCORING)
      return "FDASCORING";
    if (code == V3ActCode.FDASHAPE)
      return "FDASHAPE";
    if (code == V3ActCode.FDASIZE)
      return "FDASIZE";
    if (code == V3ActCode._ROIOVERLAYSHAPE)
      return "_ROIOverlayShape";
    if (code == V3ActCode.CIRCLE)
      return "CIRCLE";
    if (code == V3ActCode.ELLIPSE)
      return "ELLIPSE";
    if (code == V3ActCode.POINT)
      return "POINT";
    if (code == V3ActCode.POLY)
      return "POLY";
    if (code == V3ActCode.C)
      return "C";
    if (code == V3ActCode.DIET)
      return "DIET";
    if (code == V3ActCode.BR)
      return "BR";
    if (code == V3ActCode.DM)
      return "DM";
    if (code == V3ActCode.FAST)
      return "FAST";
    if (code == V3ActCode.FORMULA)
      return "FORMULA";
    if (code == V3ActCode.GF)
      return "GF";
    if (code == V3ActCode.LF)
      return "LF";
    if (code == V3ActCode.LP)
      return "LP";
    if (code == V3ActCode.LQ)
      return "LQ";
    if (code == V3ActCode.LS)
      return "LS";
    if (code == V3ActCode.N)
      return "N";
    if (code == V3ActCode.NF)
      return "NF";
    if (code == V3ActCode.PAF)
      return "PAF";
    if (code == V3ActCode.PAR)
      return "PAR";
    if (code == V3ActCode.RD)
      return "RD";
    if (code == V3ActCode.SCH)
      return "SCH";
    if (code == V3ActCode.SUPPLEMENT)
      return "SUPPLEMENT";
    if (code == V3ActCode.T)
      return "T";
    if (code == V3ActCode.VLI)
      return "VLI";
    if (code == V3ActCode.DRUGPRG)
      return "DRUGPRG";
    if (code == V3ActCode.F)
      return "F";
    if (code == V3ActCode.PRLMN)
      return "PRLMN";
    if (code == V3ActCode.SECOBS)
      return "SECOBS";
    if (code == V3ActCode.SECCATOBS)
      return "SECCATOBS";
    if (code == V3ActCode.SECCLASSOBS)
      return "SECCLASSOBS";
    if (code == V3ActCode.SECCONOBS)
      return "SECCONOBS";
    if (code == V3ActCode.SECINTOBS)
      return "SECINTOBS";
    if (code == V3ActCode.SECALTINTOBS)
      return "SECALTINTOBS";
    if (code == V3ActCode.SECDATINTOBS)
      return "SECDATINTOBS";
    if (code == V3ActCode.SECINTCONOBS)
      return "SECINTCONOBS";
    if (code == V3ActCode.SECINTPRVOBS)
      return "SECINTPRVOBS";
    if (code == V3ActCode.SECINTPRVABOBS)
      return "SECINTPRVABOBS";
    if (code == V3ActCode.SECINTPRVRBOBS)
      return "SECINTPRVRBOBS";
    if (code == V3ActCode.SECINTSTOBS)
      return "SECINTSTOBS";
    if (code == V3ActCode.SECTRSTOBS)
      return "SECTRSTOBS";
    if (code == V3ActCode.TRSTACCRDOBS)
      return "TRSTACCRDOBS";
    if (code == V3ActCode.TRSTAGREOBS)
      return "TRSTAGREOBS";
    if (code == V3ActCode.TRSTCERTOBS)
      return "TRSTCERTOBS";
    if (code == V3ActCode.TRSTFWKOBS)
      return "TRSTFWKOBS";
    if (code == V3ActCode.TRSTLOAOBS)
      return "TRSTLOAOBS";
    if (code == V3ActCode.TRSTMECOBS)
      return "TRSTMECOBS";
    if (code == V3ActCode.SUBSIDFFS)
      return "SUBSIDFFS";
    if (code == V3ActCode.WRKCOMP)
      return "WRKCOMP";
    if (code == V3ActCode._ACTPROCEDURECODE)
      return "_ActProcedureCode";
    if (code == V3ActCode._ACTBILLABLESERVICECODE)
      return "_ActBillableServiceCode";
    if (code == V3ActCode._HL7DEFINEDACTCODES)
      return "_HL7DefinedActCodes";
    if (code == V3ActCode.COPAY)
      return "COPAY";
    if (code == V3ActCode.DEDUCT)
      return "DEDUCT";
    if (code == V3ActCode.DOSEIND)
      return "DOSEIND";
    if (code == V3ActCode.PRA)
      return "PRA";
    if (code == V3ActCode.STORE)
      return "STORE";
    return "?";
  }

    public String toSystem(V3ActCode code) {
      return code.getSystem();
      }

}

