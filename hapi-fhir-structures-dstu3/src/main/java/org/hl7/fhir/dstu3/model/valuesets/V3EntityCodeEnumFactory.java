package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3EntityCodeEnumFactory implements EnumFactory<V3EntityCode> {

  public V3EntityCode fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_MaterialEntityClassType".equals(codeString))
      return V3EntityCode._MATERIALENTITYCLASSTYPE;
    if ("_ContainerEntityType".equals(codeString))
      return V3EntityCode._CONTAINERENTITYTYPE;
    if ("PKG".equals(codeString))
      return V3EntityCode.PKG;
    if ("_NonRigidContainerEntityType".equals(codeString))
      return V3EntityCode._NONRIGIDCONTAINERENTITYTYPE;
    if ("BAG".equals(codeString))
      return V3EntityCode.BAG;
    if ("PACKT".equals(codeString))
      return V3EntityCode.PACKT;
    if ("PCH".equals(codeString))
      return V3EntityCode.PCH;
    if ("SACH".equals(codeString))
      return V3EntityCode.SACH;
    if ("_RigidContainerEntityType".equals(codeString))
      return V3EntityCode._RIGIDCONTAINERENTITYTYPE;
    if ("_IndividualPackageEntityType".equals(codeString))
      return V3EntityCode._INDIVIDUALPACKAGEENTITYTYPE;
    if ("AMP".equals(codeString))
      return V3EntityCode.AMP;
    if ("MINIM".equals(codeString))
      return V3EntityCode.MINIM;
    if ("NEBAMP".equals(codeString))
      return V3EntityCode.NEBAMP;
    if ("OVUL".equals(codeString))
      return V3EntityCode.OVUL;
    if ("_MultiUseContainerEntityType".equals(codeString))
      return V3EntityCode._MULTIUSECONTAINERENTITYTYPE;
    if ("BOT".equals(codeString))
      return V3EntityCode.BOT;
    if ("BOTA".equals(codeString))
      return V3EntityCode.BOTA;
    if ("BOTD".equals(codeString))
      return V3EntityCode.BOTD;
    if ("BOTG".equals(codeString))
      return V3EntityCode.BOTG;
    if ("BOTP".equals(codeString))
      return V3EntityCode.BOTP;
    if ("BOTPLY".equals(codeString))
      return V3EntityCode.BOTPLY;
    if ("BOX".equals(codeString))
      return V3EntityCode.BOX;
    if ("CAN".equals(codeString))
      return V3EntityCode.CAN;
    if ("CART".equals(codeString))
      return V3EntityCode.CART;
    if ("CNSTR".equals(codeString))
      return V3EntityCode.CNSTR;
    if ("JAR".equals(codeString))
      return V3EntityCode.JAR;
    if ("JUG".equals(codeString))
      return V3EntityCode.JUG;
    if ("TIN".equals(codeString))
      return V3EntityCode.TIN;
    if ("TUB".equals(codeString))
      return V3EntityCode.TUB;
    if ("TUBE".equals(codeString))
      return V3EntityCode.TUBE;
    if ("VIAL".equals(codeString))
      return V3EntityCode.VIAL;
    if ("BLSTRPK".equals(codeString))
      return V3EntityCode.BLSTRPK;
    if ("CARD".equals(codeString))
      return V3EntityCode.CARD;
    if ("COMPPKG".equals(codeString))
      return V3EntityCode.COMPPKG;
    if ("DIALPK".equals(codeString))
      return V3EntityCode.DIALPK;
    if ("DISK".equals(codeString))
      return V3EntityCode.DISK;
    if ("DOSET".equals(codeString))
      return V3EntityCode.DOSET;
    if ("STRIP".equals(codeString))
      return V3EntityCode.STRIP;
    if ("KIT".equals(codeString))
      return V3EntityCode.KIT;
    if ("SYSTM".equals(codeString))
      return V3EntityCode.SYSTM;
    if ("_MedicalDevice".equals(codeString))
      return V3EntityCode._MEDICALDEVICE;
    if ("_AccessMedicalDevice".equals(codeString))
      return V3EntityCode._ACCESSMEDICALDEVICE;
    if ("LINE".equals(codeString))
      return V3EntityCode.LINE;
    if ("IALINE".equals(codeString))
      return V3EntityCode.IALINE;
    if ("IVLINE".equals(codeString))
      return V3EntityCode.IVLINE;
    if ("_AdministrationMedicalDevice".equals(codeString))
      return V3EntityCode._ADMINISTRATIONMEDICALDEVICE;
    if ("_InjectionMedicalDevice".equals(codeString))
      return V3EntityCode._INJECTIONMEDICALDEVICE;
    if ("AINJ".equals(codeString))
      return V3EntityCode.AINJ;
    if ("PEN".equals(codeString))
      return V3EntityCode.PEN;
    if ("SYR".equals(codeString))
      return V3EntityCode.SYR;
    if ("APLCTR".equals(codeString))
      return V3EntityCode.APLCTR;
    if ("INH".equals(codeString))
      return V3EntityCode.INH;
    if ("DSKS".equals(codeString))
      return V3EntityCode.DSKS;
    if ("DSKUNH".equals(codeString))
      return V3EntityCode.DSKUNH;
    if ("TRBINH".equals(codeString))
      return V3EntityCode.TRBINH;
    if ("PMP".equals(codeString))
      return V3EntityCode.PMP;
    if ("_SpecimenAdditiveEntity".equals(codeString))
      return V3EntityCode._SPECIMENADDITIVEENTITY;
    if ("ACDA".equals(codeString))
      return V3EntityCode.ACDA;
    if ("ACDB".equals(codeString))
      return V3EntityCode.ACDB;
    if ("ACET".equals(codeString))
      return V3EntityCode.ACET;
    if ("AMIES".equals(codeString))
      return V3EntityCode.AMIES;
    if ("BACTM".equals(codeString))
      return V3EntityCode.BACTM;
    if ("BF10".equals(codeString))
      return V3EntityCode.BF10;
    if ("BOR".equals(codeString))
      return V3EntityCode.BOR;
    if ("BOUIN".equals(codeString))
      return V3EntityCode.BOUIN;
    if ("BSKM".equals(codeString))
      return V3EntityCode.BSKM;
    if ("C32".equals(codeString))
      return V3EntityCode.C32;
    if ("C38".equals(codeString))
      return V3EntityCode.C38;
    if ("CARS".equals(codeString))
      return V3EntityCode.CARS;
    if ("CARY".equals(codeString))
      return V3EntityCode.CARY;
    if ("CHLTM".equals(codeString))
      return V3EntityCode.CHLTM;
    if ("CTAD".equals(codeString))
      return V3EntityCode.CTAD;
    if ("EDTK15".equals(codeString))
      return V3EntityCode.EDTK15;
    if ("EDTK75".equals(codeString))
      return V3EntityCode.EDTK75;
    if ("EDTN".equals(codeString))
      return V3EntityCode.EDTN;
    if ("ENT".equals(codeString))
      return V3EntityCode.ENT;
    if ("F10".equals(codeString))
      return V3EntityCode.F10;
    if ("FDP".equals(codeString))
      return V3EntityCode.FDP;
    if ("FL10".equals(codeString))
      return V3EntityCode.FL10;
    if ("FL100".equals(codeString))
      return V3EntityCode.FL100;
    if ("HCL6".equals(codeString))
      return V3EntityCode.HCL6;
    if ("HEPA".equals(codeString))
      return V3EntityCode.HEPA;
    if ("HEPL".equals(codeString))
      return V3EntityCode.HEPL;
    if ("HEPN".equals(codeString))
      return V3EntityCode.HEPN;
    if ("HNO3".equals(codeString))
      return V3EntityCode.HNO3;
    if ("JKM".equals(codeString))
      return V3EntityCode.JKM;
    if ("KARN".equals(codeString))
      return V3EntityCode.KARN;
    if ("KOX".equals(codeString))
      return V3EntityCode.KOX;
    if ("LIA".equals(codeString))
      return V3EntityCode.LIA;
    if ("M4".equals(codeString))
      return V3EntityCode.M4;
    if ("M4RT".equals(codeString))
      return V3EntityCode.M4RT;
    if ("M5".equals(codeString))
      return V3EntityCode.M5;
    if ("MICHTM".equals(codeString))
      return V3EntityCode.MICHTM;
    if ("MMDTM".equals(codeString))
      return V3EntityCode.MMDTM;
    if ("NAF".equals(codeString))
      return V3EntityCode.NAF;
    if ("NONE".equals(codeString))
      return V3EntityCode.NONE;
    if ("PAGE".equals(codeString))
      return V3EntityCode.PAGE;
    if ("PHENOL".equals(codeString))
      return V3EntityCode.PHENOL;
    if ("PVA".equals(codeString))
      return V3EntityCode.PVA;
    if ("RLM".equals(codeString))
      return V3EntityCode.RLM;
    if ("SILICA".equals(codeString))
      return V3EntityCode.SILICA;
    if ("SPS".equals(codeString))
      return V3EntityCode.SPS;
    if ("SST".equals(codeString))
      return V3EntityCode.SST;
    if ("STUTM".equals(codeString))
      return V3EntityCode.STUTM;
    if ("THROM".equals(codeString))
      return V3EntityCode.THROM;
    if ("THYMOL".equals(codeString))
      return V3EntityCode.THYMOL;
    if ("THYO".equals(codeString))
      return V3EntityCode.THYO;
    if ("TOLU".equals(codeString))
      return V3EntityCode.TOLU;
    if ("URETM".equals(codeString))
      return V3EntityCode.URETM;
    if ("VIRTM".equals(codeString))
      return V3EntityCode.VIRTM;
    if ("WEST".equals(codeString))
      return V3EntityCode.WEST;
    if ("BLDPRD".equals(codeString))
      return V3EntityCode.BLDPRD;
    if ("VCCNE".equals(codeString))
      return V3EntityCode.VCCNE;
    if ("_DrugEntity".equals(codeString))
      return V3EntityCode._DRUGENTITY;
    if ("_ClinicalDrug".equals(codeString))
      return V3EntityCode._CLINICALDRUG;
    if ("_NonDrugAgentEntity".equals(codeString))
      return V3EntityCode._NONDRUGAGENTENTITY;
    if ("NDA01".equals(codeString))
      return V3EntityCode.NDA01;
    if ("NDA02".equals(codeString))
      return V3EntityCode.NDA02;
    if ("NDA03".equals(codeString))
      return V3EntityCode.NDA03;
    if ("NDA04".equals(codeString))
      return V3EntityCode.NDA04;
    if ("NDA05".equals(codeString))
      return V3EntityCode.NDA05;
    if ("NDA06".equals(codeString))
      return V3EntityCode.NDA06;
    if ("NDA07".equals(codeString))
      return V3EntityCode.NDA07;
    if ("NDA08".equals(codeString))
      return V3EntityCode.NDA08;
    if ("NDA09".equals(codeString))
      return V3EntityCode.NDA09;
    if ("NDA10".equals(codeString))
      return V3EntityCode.NDA10;
    if ("NDA11".equals(codeString))
      return V3EntityCode.NDA11;
    if ("NDA12".equals(codeString))
      return V3EntityCode.NDA12;
    if ("NDA13".equals(codeString))
      return V3EntityCode.NDA13;
    if ("NDA14".equals(codeString))
      return V3EntityCode.NDA14;
    if ("NDA15".equals(codeString))
      return V3EntityCode.NDA15;
    if ("NDA16".equals(codeString))
      return V3EntityCode.NDA16;
    if ("NDA17".equals(codeString))
      return V3EntityCode.NDA17;
    if ("_OrganizationEntityType".equals(codeString))
      return V3EntityCode._ORGANIZATIONENTITYTYPE;
    if ("HHOLD".equals(codeString))
      return V3EntityCode.HHOLD;
    if ("NAT".equals(codeString))
      return V3EntityCode.NAT;
    if ("RELIG".equals(codeString))
      return V3EntityCode.RELIG;
    if ("_PlaceEntityType".equals(codeString))
      return V3EntityCode._PLACEENTITYTYPE;
    if ("BED".equals(codeString))
      return V3EntityCode.BED;
    if ("BLDG".equals(codeString))
      return V3EntityCode.BLDG;
    if ("FLOOR".equals(codeString))
      return V3EntityCode.FLOOR;
    if ("ROOM".equals(codeString))
      return V3EntityCode.ROOM;
    if ("WING".equals(codeString))
      return V3EntityCode.WING;
    if ("_ResourceGroupEntityType".equals(codeString))
      return V3EntityCode._RESOURCEGROUPENTITYTYPE;
    if ("PRAC".equals(codeString))
      return V3EntityCode.PRAC;
    throw new IllegalArgumentException("Unknown V3EntityCode code '"+codeString+"'");
  }

  public String toCode(V3EntityCode code) {
    if (code == V3EntityCode._MATERIALENTITYCLASSTYPE)
      return "_MaterialEntityClassType";
    if (code == V3EntityCode._CONTAINERENTITYTYPE)
      return "_ContainerEntityType";
    if (code == V3EntityCode.PKG)
      return "PKG";
    if (code == V3EntityCode._NONRIGIDCONTAINERENTITYTYPE)
      return "_NonRigidContainerEntityType";
    if (code == V3EntityCode.BAG)
      return "BAG";
    if (code == V3EntityCode.PACKT)
      return "PACKT";
    if (code == V3EntityCode.PCH)
      return "PCH";
    if (code == V3EntityCode.SACH)
      return "SACH";
    if (code == V3EntityCode._RIGIDCONTAINERENTITYTYPE)
      return "_RigidContainerEntityType";
    if (code == V3EntityCode._INDIVIDUALPACKAGEENTITYTYPE)
      return "_IndividualPackageEntityType";
    if (code == V3EntityCode.AMP)
      return "AMP";
    if (code == V3EntityCode.MINIM)
      return "MINIM";
    if (code == V3EntityCode.NEBAMP)
      return "NEBAMP";
    if (code == V3EntityCode.OVUL)
      return "OVUL";
    if (code == V3EntityCode._MULTIUSECONTAINERENTITYTYPE)
      return "_MultiUseContainerEntityType";
    if (code == V3EntityCode.BOT)
      return "BOT";
    if (code == V3EntityCode.BOTA)
      return "BOTA";
    if (code == V3EntityCode.BOTD)
      return "BOTD";
    if (code == V3EntityCode.BOTG)
      return "BOTG";
    if (code == V3EntityCode.BOTP)
      return "BOTP";
    if (code == V3EntityCode.BOTPLY)
      return "BOTPLY";
    if (code == V3EntityCode.BOX)
      return "BOX";
    if (code == V3EntityCode.CAN)
      return "CAN";
    if (code == V3EntityCode.CART)
      return "CART";
    if (code == V3EntityCode.CNSTR)
      return "CNSTR";
    if (code == V3EntityCode.JAR)
      return "JAR";
    if (code == V3EntityCode.JUG)
      return "JUG";
    if (code == V3EntityCode.TIN)
      return "TIN";
    if (code == V3EntityCode.TUB)
      return "TUB";
    if (code == V3EntityCode.TUBE)
      return "TUBE";
    if (code == V3EntityCode.VIAL)
      return "VIAL";
    if (code == V3EntityCode.BLSTRPK)
      return "BLSTRPK";
    if (code == V3EntityCode.CARD)
      return "CARD";
    if (code == V3EntityCode.COMPPKG)
      return "COMPPKG";
    if (code == V3EntityCode.DIALPK)
      return "DIALPK";
    if (code == V3EntityCode.DISK)
      return "DISK";
    if (code == V3EntityCode.DOSET)
      return "DOSET";
    if (code == V3EntityCode.STRIP)
      return "STRIP";
    if (code == V3EntityCode.KIT)
      return "KIT";
    if (code == V3EntityCode.SYSTM)
      return "SYSTM";
    if (code == V3EntityCode._MEDICALDEVICE)
      return "_MedicalDevice";
    if (code == V3EntityCode._ACCESSMEDICALDEVICE)
      return "_AccessMedicalDevice";
    if (code == V3EntityCode.LINE)
      return "LINE";
    if (code == V3EntityCode.IALINE)
      return "IALINE";
    if (code == V3EntityCode.IVLINE)
      return "IVLINE";
    if (code == V3EntityCode._ADMINISTRATIONMEDICALDEVICE)
      return "_AdministrationMedicalDevice";
    if (code == V3EntityCode._INJECTIONMEDICALDEVICE)
      return "_InjectionMedicalDevice";
    if (code == V3EntityCode.AINJ)
      return "AINJ";
    if (code == V3EntityCode.PEN)
      return "PEN";
    if (code == V3EntityCode.SYR)
      return "SYR";
    if (code == V3EntityCode.APLCTR)
      return "APLCTR";
    if (code == V3EntityCode.INH)
      return "INH";
    if (code == V3EntityCode.DSKS)
      return "DSKS";
    if (code == V3EntityCode.DSKUNH)
      return "DSKUNH";
    if (code == V3EntityCode.TRBINH)
      return "TRBINH";
    if (code == V3EntityCode.PMP)
      return "PMP";
    if (code == V3EntityCode._SPECIMENADDITIVEENTITY)
      return "_SpecimenAdditiveEntity";
    if (code == V3EntityCode.ACDA)
      return "ACDA";
    if (code == V3EntityCode.ACDB)
      return "ACDB";
    if (code == V3EntityCode.ACET)
      return "ACET";
    if (code == V3EntityCode.AMIES)
      return "AMIES";
    if (code == V3EntityCode.BACTM)
      return "BACTM";
    if (code == V3EntityCode.BF10)
      return "BF10";
    if (code == V3EntityCode.BOR)
      return "BOR";
    if (code == V3EntityCode.BOUIN)
      return "BOUIN";
    if (code == V3EntityCode.BSKM)
      return "BSKM";
    if (code == V3EntityCode.C32)
      return "C32";
    if (code == V3EntityCode.C38)
      return "C38";
    if (code == V3EntityCode.CARS)
      return "CARS";
    if (code == V3EntityCode.CARY)
      return "CARY";
    if (code == V3EntityCode.CHLTM)
      return "CHLTM";
    if (code == V3EntityCode.CTAD)
      return "CTAD";
    if (code == V3EntityCode.EDTK15)
      return "EDTK15";
    if (code == V3EntityCode.EDTK75)
      return "EDTK75";
    if (code == V3EntityCode.EDTN)
      return "EDTN";
    if (code == V3EntityCode.ENT)
      return "ENT";
    if (code == V3EntityCode.F10)
      return "F10";
    if (code == V3EntityCode.FDP)
      return "FDP";
    if (code == V3EntityCode.FL10)
      return "FL10";
    if (code == V3EntityCode.FL100)
      return "FL100";
    if (code == V3EntityCode.HCL6)
      return "HCL6";
    if (code == V3EntityCode.HEPA)
      return "HEPA";
    if (code == V3EntityCode.HEPL)
      return "HEPL";
    if (code == V3EntityCode.HEPN)
      return "HEPN";
    if (code == V3EntityCode.HNO3)
      return "HNO3";
    if (code == V3EntityCode.JKM)
      return "JKM";
    if (code == V3EntityCode.KARN)
      return "KARN";
    if (code == V3EntityCode.KOX)
      return "KOX";
    if (code == V3EntityCode.LIA)
      return "LIA";
    if (code == V3EntityCode.M4)
      return "M4";
    if (code == V3EntityCode.M4RT)
      return "M4RT";
    if (code == V3EntityCode.M5)
      return "M5";
    if (code == V3EntityCode.MICHTM)
      return "MICHTM";
    if (code == V3EntityCode.MMDTM)
      return "MMDTM";
    if (code == V3EntityCode.NAF)
      return "NAF";
    if (code == V3EntityCode.NONE)
      return "NONE";
    if (code == V3EntityCode.PAGE)
      return "PAGE";
    if (code == V3EntityCode.PHENOL)
      return "PHENOL";
    if (code == V3EntityCode.PVA)
      return "PVA";
    if (code == V3EntityCode.RLM)
      return "RLM";
    if (code == V3EntityCode.SILICA)
      return "SILICA";
    if (code == V3EntityCode.SPS)
      return "SPS";
    if (code == V3EntityCode.SST)
      return "SST";
    if (code == V3EntityCode.STUTM)
      return "STUTM";
    if (code == V3EntityCode.THROM)
      return "THROM";
    if (code == V3EntityCode.THYMOL)
      return "THYMOL";
    if (code == V3EntityCode.THYO)
      return "THYO";
    if (code == V3EntityCode.TOLU)
      return "TOLU";
    if (code == V3EntityCode.URETM)
      return "URETM";
    if (code == V3EntityCode.VIRTM)
      return "VIRTM";
    if (code == V3EntityCode.WEST)
      return "WEST";
    if (code == V3EntityCode.BLDPRD)
      return "BLDPRD";
    if (code == V3EntityCode.VCCNE)
      return "VCCNE";
    if (code == V3EntityCode._DRUGENTITY)
      return "_DrugEntity";
    if (code == V3EntityCode._CLINICALDRUG)
      return "_ClinicalDrug";
    if (code == V3EntityCode._NONDRUGAGENTENTITY)
      return "_NonDrugAgentEntity";
    if (code == V3EntityCode.NDA01)
      return "NDA01";
    if (code == V3EntityCode.NDA02)
      return "NDA02";
    if (code == V3EntityCode.NDA03)
      return "NDA03";
    if (code == V3EntityCode.NDA04)
      return "NDA04";
    if (code == V3EntityCode.NDA05)
      return "NDA05";
    if (code == V3EntityCode.NDA06)
      return "NDA06";
    if (code == V3EntityCode.NDA07)
      return "NDA07";
    if (code == V3EntityCode.NDA08)
      return "NDA08";
    if (code == V3EntityCode.NDA09)
      return "NDA09";
    if (code == V3EntityCode.NDA10)
      return "NDA10";
    if (code == V3EntityCode.NDA11)
      return "NDA11";
    if (code == V3EntityCode.NDA12)
      return "NDA12";
    if (code == V3EntityCode.NDA13)
      return "NDA13";
    if (code == V3EntityCode.NDA14)
      return "NDA14";
    if (code == V3EntityCode.NDA15)
      return "NDA15";
    if (code == V3EntityCode.NDA16)
      return "NDA16";
    if (code == V3EntityCode.NDA17)
      return "NDA17";
    if (code == V3EntityCode._ORGANIZATIONENTITYTYPE)
      return "_OrganizationEntityType";
    if (code == V3EntityCode.HHOLD)
      return "HHOLD";
    if (code == V3EntityCode.NAT)
      return "NAT";
    if (code == V3EntityCode.RELIG)
      return "RELIG";
    if (code == V3EntityCode._PLACEENTITYTYPE)
      return "_PlaceEntityType";
    if (code == V3EntityCode.BED)
      return "BED";
    if (code == V3EntityCode.BLDG)
      return "BLDG";
    if (code == V3EntityCode.FLOOR)
      return "FLOOR";
    if (code == V3EntityCode.ROOM)
      return "ROOM";
    if (code == V3EntityCode.WING)
      return "WING";
    if (code == V3EntityCode._RESOURCEGROUPENTITYTYPE)
      return "_ResourceGroupEntityType";
    if (code == V3EntityCode.PRAC)
      return "PRAC";
    return "?";
  }

    public String toSystem(V3EntityCode code) {
      return code.getSystem();
      }

}

