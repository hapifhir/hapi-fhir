package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ObservationMethodEnumFactory implements EnumFactory<V3ObservationMethod> {

  public V3ObservationMethod fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_DecisionObservationMethod".equals(codeString))
      return V3ObservationMethod._DECISIONOBSERVATIONMETHOD;
    if ("ALGM".equals(codeString))
      return V3ObservationMethod.ALGM;
    if ("BYCL".equals(codeString))
      return V3ObservationMethod.BYCL;
    if ("GINT".equals(codeString))
      return V3ObservationMethod.GINT;
    if ("_GeneticObservationMethod".equals(codeString))
      return V3ObservationMethod._GENETICOBSERVATIONMETHOD;
    if ("PCR".equals(codeString))
      return V3ObservationMethod.PCR;
    if ("_ObservationMethodAggregate".equals(codeString))
      return V3ObservationMethod._OBSERVATIONMETHODAGGREGATE;
    if ("AVERAGE".equals(codeString))
      return V3ObservationMethod.AVERAGE;
    if ("COUNT".equals(codeString))
      return V3ObservationMethod.COUNT;
    if ("MAX".equals(codeString))
      return V3ObservationMethod.MAX;
    if ("MEDIAN".equals(codeString))
      return V3ObservationMethod.MEDIAN;
    if ("MIN".equals(codeString))
      return V3ObservationMethod.MIN;
    if ("MODE".equals(codeString))
      return V3ObservationMethod.MODE;
    if ("STDEV.P".equals(codeString))
      return V3ObservationMethod.STDEV_P;
    if ("STDEV.S".equals(codeString))
      return V3ObservationMethod.STDEV_S;
    if ("SUM".equals(codeString))
      return V3ObservationMethod.SUM;
    if ("VARIANCE.P".equals(codeString))
      return V3ObservationMethod.VARIANCE_P;
    if ("VARIANCE.S".equals(codeString))
      return V3ObservationMethod.VARIANCE_S;
    if ("_VerificationMethod".equals(codeString))
      return V3ObservationMethod._VERIFICATIONMETHOD;
    if ("VDOC".equals(codeString))
      return V3ObservationMethod.VDOC;
    if ("VREG".equals(codeString))
      return V3ObservationMethod.VREG;
    if ("VTOKEN".equals(codeString))
      return V3ObservationMethod.VTOKEN;
    if ("VVOICE".equals(codeString))
      return V3ObservationMethod.VVOICE;
    if ("0001".equals(codeString))
      return V3ObservationMethod._0001;
    if ("0002".equals(codeString))
      return V3ObservationMethod._0002;
    if ("0003".equals(codeString))
      return V3ObservationMethod._0003;
    if ("0004".equals(codeString))
      return V3ObservationMethod._0004;
    if ("0005".equals(codeString))
      return V3ObservationMethod._0005;
    if ("0006".equals(codeString))
      return V3ObservationMethod._0006;
    if ("0007".equals(codeString))
      return V3ObservationMethod._0007;
    if ("0008".equals(codeString))
      return V3ObservationMethod._0008;
    if ("0009".equals(codeString))
      return V3ObservationMethod._0009;
    if ("0010".equals(codeString))
      return V3ObservationMethod._0010;
    if ("0011".equals(codeString))
      return V3ObservationMethod._0011;
    if ("0012".equals(codeString))
      return V3ObservationMethod._0012;
    if ("0013".equals(codeString))
      return V3ObservationMethod._0013;
    if ("0014".equals(codeString))
      return V3ObservationMethod._0014;
    if ("0015".equals(codeString))
      return V3ObservationMethod._0015;
    if ("0016".equals(codeString))
      return V3ObservationMethod._0016;
    if ("0017".equals(codeString))
      return V3ObservationMethod._0017;
    if ("0018".equals(codeString))
      return V3ObservationMethod._0018;
    if ("0019".equals(codeString))
      return V3ObservationMethod._0019;
    if ("0020".equals(codeString))
      return V3ObservationMethod._0020;
    if ("0021".equals(codeString))
      return V3ObservationMethod._0021;
    if ("0022".equals(codeString))
      return V3ObservationMethod._0022;
    if ("0023".equals(codeString))
      return V3ObservationMethod._0023;
    if ("0024".equals(codeString))
      return V3ObservationMethod._0024;
    if ("0025".equals(codeString))
      return V3ObservationMethod._0025;
    if ("0026".equals(codeString))
      return V3ObservationMethod._0026;
    if ("0027".equals(codeString))
      return V3ObservationMethod._0027;
    if ("0028".equals(codeString))
      return V3ObservationMethod._0028;
    if ("0029".equals(codeString))
      return V3ObservationMethod._0029;
    if ("0030".equals(codeString))
      return V3ObservationMethod._0030;
    if ("0031".equals(codeString))
      return V3ObservationMethod._0031;
    if ("0032".equals(codeString))
      return V3ObservationMethod._0032;
    if ("0033".equals(codeString))
      return V3ObservationMethod._0033;
    if ("0034".equals(codeString))
      return V3ObservationMethod._0034;
    if ("0035".equals(codeString))
      return V3ObservationMethod._0035;
    if ("0036".equals(codeString))
      return V3ObservationMethod._0036;
    if ("0037".equals(codeString))
      return V3ObservationMethod._0037;
    if ("0038".equals(codeString))
      return V3ObservationMethod._0038;
    if ("0039".equals(codeString))
      return V3ObservationMethod._0039;
    if ("0040".equals(codeString))
      return V3ObservationMethod._0040;
    if ("0041".equals(codeString))
      return V3ObservationMethod._0041;
    if ("0042".equals(codeString))
      return V3ObservationMethod._0042;
    if ("0043".equals(codeString))
      return V3ObservationMethod._0043;
    if ("0044".equals(codeString))
      return V3ObservationMethod._0044;
    if ("0045".equals(codeString))
      return V3ObservationMethod._0045;
    if ("0046".equals(codeString))
      return V3ObservationMethod._0046;
    if ("0047".equals(codeString))
      return V3ObservationMethod._0047;
    if ("0048".equals(codeString))
      return V3ObservationMethod._0048;
    if ("0049".equals(codeString))
      return V3ObservationMethod._0049;
    if ("0050".equals(codeString))
      return V3ObservationMethod._0050;
    if ("0051".equals(codeString))
      return V3ObservationMethod._0051;
    if ("0052".equals(codeString))
      return V3ObservationMethod._0052;
    if ("0053".equals(codeString))
      return V3ObservationMethod._0053;
    if ("0054".equals(codeString))
      return V3ObservationMethod._0054;
    if ("0055".equals(codeString))
      return V3ObservationMethod._0055;
    if ("0056".equals(codeString))
      return V3ObservationMethod._0056;
    if ("0057".equals(codeString))
      return V3ObservationMethod._0057;
    if ("0058".equals(codeString))
      return V3ObservationMethod._0058;
    if ("0059".equals(codeString))
      return V3ObservationMethod._0059;
    if ("0060".equals(codeString))
      return V3ObservationMethod._0060;
    if ("0061".equals(codeString))
      return V3ObservationMethod._0061;
    if ("0062".equals(codeString))
      return V3ObservationMethod._0062;
    if ("0063".equals(codeString))
      return V3ObservationMethod._0063;
    if ("0064".equals(codeString))
      return V3ObservationMethod._0064;
    if ("0065".equals(codeString))
      return V3ObservationMethod._0065;
    if ("0066".equals(codeString))
      return V3ObservationMethod._0066;
    if ("0067".equals(codeString))
      return V3ObservationMethod._0067;
    if ("0068".equals(codeString))
      return V3ObservationMethod._0068;
    if ("0069".equals(codeString))
      return V3ObservationMethod._0069;
    if ("0070".equals(codeString))
      return V3ObservationMethod._0070;
    if ("0071".equals(codeString))
      return V3ObservationMethod._0071;
    if ("0072".equals(codeString))
      return V3ObservationMethod._0072;
    if ("0073".equals(codeString))
      return V3ObservationMethod._0073;
    if ("0074".equals(codeString))
      return V3ObservationMethod._0074;
    if ("0075".equals(codeString))
      return V3ObservationMethod._0075;
    if ("0076".equals(codeString))
      return V3ObservationMethod._0076;
    if ("0077".equals(codeString))
      return V3ObservationMethod._0077;
    if ("0078".equals(codeString))
      return V3ObservationMethod._0078;
    if ("0079".equals(codeString))
      return V3ObservationMethod._0079;
    if ("0080".equals(codeString))
      return V3ObservationMethod._0080;
    if ("0081".equals(codeString))
      return V3ObservationMethod._0081;
    if ("0082".equals(codeString))
      return V3ObservationMethod._0082;
    if ("0083".equals(codeString))
      return V3ObservationMethod._0083;
    if ("0084".equals(codeString))
      return V3ObservationMethod._0084;
    if ("0085".equals(codeString))
      return V3ObservationMethod._0085;
    if ("0086".equals(codeString))
      return V3ObservationMethod._0086;
    if ("0087".equals(codeString))
      return V3ObservationMethod._0087;
    if ("0088".equals(codeString))
      return V3ObservationMethod._0088;
    if ("0089".equals(codeString))
      return V3ObservationMethod._0089;
    if ("0090".equals(codeString))
      return V3ObservationMethod._0090;
    if ("0091".equals(codeString))
      return V3ObservationMethod._0091;
    if ("0092".equals(codeString))
      return V3ObservationMethod._0092;
    if ("0093".equals(codeString))
      return V3ObservationMethod._0093;
    if ("0094".equals(codeString))
      return V3ObservationMethod._0094;
    if ("0095".equals(codeString))
      return V3ObservationMethod._0095;
    if ("0096".equals(codeString))
      return V3ObservationMethod._0096;
    if ("0097".equals(codeString))
      return V3ObservationMethod._0097;
    if ("0098".equals(codeString))
      return V3ObservationMethod._0098;
    if ("0099".equals(codeString))
      return V3ObservationMethod._0099;
    if ("0100".equals(codeString))
      return V3ObservationMethod._0100;
    if ("0101".equals(codeString))
      return V3ObservationMethod._0101;
    if ("0102".equals(codeString))
      return V3ObservationMethod._0102;
    if ("0103".equals(codeString))
      return V3ObservationMethod._0103;
    if ("0104".equals(codeString))
      return V3ObservationMethod._0104;
    if ("0105".equals(codeString))
      return V3ObservationMethod._0105;
    if ("0106".equals(codeString))
      return V3ObservationMethod._0106;
    if ("0107".equals(codeString))
      return V3ObservationMethod._0107;
    if ("0108".equals(codeString))
      return V3ObservationMethod._0108;
    if ("0109".equals(codeString))
      return V3ObservationMethod._0109;
    if ("0110".equals(codeString))
      return V3ObservationMethod._0110;
    if ("0111".equals(codeString))
      return V3ObservationMethod._0111;
    if ("0112".equals(codeString))
      return V3ObservationMethod._0112;
    if ("0113".equals(codeString))
      return V3ObservationMethod._0113;
    if ("0114".equals(codeString))
      return V3ObservationMethod._0114;
    if ("0115".equals(codeString))
      return V3ObservationMethod._0115;
    if ("0116".equals(codeString))
      return V3ObservationMethod._0116;
    if ("0117".equals(codeString))
      return V3ObservationMethod._0117;
    if ("0118".equals(codeString))
      return V3ObservationMethod._0118;
    if ("0119".equals(codeString))
      return V3ObservationMethod._0119;
    if ("0120".equals(codeString))
      return V3ObservationMethod._0120;
    if ("0121".equals(codeString))
      return V3ObservationMethod._0121;
    if ("0122".equals(codeString))
      return V3ObservationMethod._0122;
    if ("0123".equals(codeString))
      return V3ObservationMethod._0123;
    if ("0124".equals(codeString))
      return V3ObservationMethod._0124;
    if ("0125".equals(codeString))
      return V3ObservationMethod._0125;
    if ("0126".equals(codeString))
      return V3ObservationMethod._0126;
    if ("0128".equals(codeString))
      return V3ObservationMethod._0128;
    if ("0129".equals(codeString))
      return V3ObservationMethod._0129;
    if ("0130".equals(codeString))
      return V3ObservationMethod._0130;
    if ("0131".equals(codeString))
      return V3ObservationMethod._0131;
    if ("0132".equals(codeString))
      return V3ObservationMethod._0132;
    if ("0133".equals(codeString))
      return V3ObservationMethod._0133;
    if ("0134".equals(codeString))
      return V3ObservationMethod._0134;
    if ("0135".equals(codeString))
      return V3ObservationMethod._0135;
    if ("0136".equals(codeString))
      return V3ObservationMethod._0136;
    if ("0137".equals(codeString))
      return V3ObservationMethod._0137;
    if ("0138".equals(codeString))
      return V3ObservationMethod._0138;
    if ("0139".equals(codeString))
      return V3ObservationMethod._0139;
    if ("0140".equals(codeString))
      return V3ObservationMethod._0140;
    if ("0141".equals(codeString))
      return V3ObservationMethod._0141;
    if ("0142".equals(codeString))
      return V3ObservationMethod._0142;
    if ("0143".equals(codeString))
      return V3ObservationMethod._0143;
    if ("0144".equals(codeString))
      return V3ObservationMethod._0144;
    if ("0145".equals(codeString))
      return V3ObservationMethod._0145;
    if ("0146".equals(codeString))
      return V3ObservationMethod._0146;
    if ("0147".equals(codeString))
      return V3ObservationMethod._0147;
    if ("0148".equals(codeString))
      return V3ObservationMethod._0148;
    if ("0149".equals(codeString))
      return V3ObservationMethod._0149;
    if ("0150".equals(codeString))
      return V3ObservationMethod._0150;
    if ("0151".equals(codeString))
      return V3ObservationMethod._0151;
    if ("0152".equals(codeString))
      return V3ObservationMethod._0152;
    if ("0153".equals(codeString))
      return V3ObservationMethod._0153;
    if ("0154".equals(codeString))
      return V3ObservationMethod._0154;
    if ("0155".equals(codeString))
      return V3ObservationMethod._0155;
    if ("0156".equals(codeString))
      return V3ObservationMethod._0156;
    if ("0157".equals(codeString))
      return V3ObservationMethod._0157;
    if ("0158".equals(codeString))
      return V3ObservationMethod._0158;
    if ("0159".equals(codeString))
      return V3ObservationMethod._0159;
    if ("0160".equals(codeString))
      return V3ObservationMethod._0160;
    if ("0161".equals(codeString))
      return V3ObservationMethod._0161;
    if ("0162".equals(codeString))
      return V3ObservationMethod._0162;
    if ("0163".equals(codeString))
      return V3ObservationMethod._0163;
    if ("0164".equals(codeString))
      return V3ObservationMethod._0164;
    if ("0165".equals(codeString))
      return V3ObservationMethod._0165;
    if ("0166".equals(codeString))
      return V3ObservationMethod._0166;
    if ("0167".equals(codeString))
      return V3ObservationMethod._0167;
    if ("0168".equals(codeString))
      return V3ObservationMethod._0168;
    if ("0169".equals(codeString))
      return V3ObservationMethod._0169;
    if ("0170".equals(codeString))
      return V3ObservationMethod._0170;
    if ("0171".equals(codeString))
      return V3ObservationMethod._0171;
    if ("0172".equals(codeString))
      return V3ObservationMethod._0172;
    if ("0173".equals(codeString))
      return V3ObservationMethod._0173;
    if ("0174".equals(codeString))
      return V3ObservationMethod._0174;
    if ("0175".equals(codeString))
      return V3ObservationMethod._0175;
    if ("0176".equals(codeString))
      return V3ObservationMethod._0176;
    if ("0177".equals(codeString))
      return V3ObservationMethod._0177;
    if ("0178".equals(codeString))
      return V3ObservationMethod._0178;
    if ("0179".equals(codeString))
      return V3ObservationMethod._0179;
    if ("0180".equals(codeString))
      return V3ObservationMethod._0180;
    if ("0181".equals(codeString))
      return V3ObservationMethod._0181;
    if ("0182".equals(codeString))
      return V3ObservationMethod._0182;
    if ("0183".equals(codeString))
      return V3ObservationMethod._0183;
    if ("0184".equals(codeString))
      return V3ObservationMethod._0184;
    if ("0185".equals(codeString))
      return V3ObservationMethod._0185;
    if ("0186".equals(codeString))
      return V3ObservationMethod._0186;
    if ("0187".equals(codeString))
      return V3ObservationMethod._0187;
    if ("0188".equals(codeString))
      return V3ObservationMethod._0188;
    if ("0189".equals(codeString))
      return V3ObservationMethod._0189;
    if ("0190".equals(codeString))
      return V3ObservationMethod._0190;
    if ("0191".equals(codeString))
      return V3ObservationMethod._0191;
    if ("0192".equals(codeString))
      return V3ObservationMethod._0192;
    if ("0193".equals(codeString))
      return V3ObservationMethod._0193;
    if ("0194".equals(codeString))
      return V3ObservationMethod._0194;
    if ("0195".equals(codeString))
      return V3ObservationMethod._0195;
    if ("0196".equals(codeString))
      return V3ObservationMethod._0196;
    if ("0197".equals(codeString))
      return V3ObservationMethod._0197;
    if ("0198".equals(codeString))
      return V3ObservationMethod._0198;
    if ("0199".equals(codeString))
      return V3ObservationMethod._0199;
    if ("0200".equals(codeString))
      return V3ObservationMethod._0200;
    if ("0201".equals(codeString))
      return V3ObservationMethod._0201;
    if ("0202".equals(codeString))
      return V3ObservationMethod._0202;
    if ("0203".equals(codeString))
      return V3ObservationMethod._0203;
    if ("0204".equals(codeString))
      return V3ObservationMethod._0204;
    if ("0205".equals(codeString))
      return V3ObservationMethod._0205;
    if ("0206".equals(codeString))
      return V3ObservationMethod._0206;
    if ("0207".equals(codeString))
      return V3ObservationMethod._0207;
    if ("0208".equals(codeString))
      return V3ObservationMethod._0208;
    if ("0209".equals(codeString))
      return V3ObservationMethod._0209;
    if ("0210".equals(codeString))
      return V3ObservationMethod._0210;
    if ("0211".equals(codeString))
      return V3ObservationMethod._0211;
    if ("0212".equals(codeString))
      return V3ObservationMethod._0212;
    if ("0213".equals(codeString))
      return V3ObservationMethod._0213;
    if ("0214".equals(codeString))
      return V3ObservationMethod._0214;
    if ("0215".equals(codeString))
      return V3ObservationMethod._0215;
    if ("0216".equals(codeString))
      return V3ObservationMethod._0216;
    if ("0217".equals(codeString))
      return V3ObservationMethod._0217;
    if ("0218".equals(codeString))
      return V3ObservationMethod._0218;
    if ("0219".equals(codeString))
      return V3ObservationMethod._0219;
    if ("0220".equals(codeString))
      return V3ObservationMethod._0220;
    if ("0221".equals(codeString))
      return V3ObservationMethod._0221;
    if ("0222".equals(codeString))
      return V3ObservationMethod._0222;
    if ("0223".equals(codeString))
      return V3ObservationMethod._0223;
    if ("0224".equals(codeString))
      return V3ObservationMethod._0224;
    if ("0225".equals(codeString))
      return V3ObservationMethod._0225;
    if ("0226".equals(codeString))
      return V3ObservationMethod._0226;
    if ("0227".equals(codeString))
      return V3ObservationMethod._0227;
    if ("0228".equals(codeString))
      return V3ObservationMethod._0228;
    if ("0229".equals(codeString))
      return V3ObservationMethod._0229;
    if ("0230".equals(codeString))
      return V3ObservationMethod._0230;
    if ("0231".equals(codeString))
      return V3ObservationMethod._0231;
    if ("0232".equals(codeString))
      return V3ObservationMethod._0232;
    if ("0233".equals(codeString))
      return V3ObservationMethod._0233;
    if ("0234".equals(codeString))
      return V3ObservationMethod._0234;
    if ("0235".equals(codeString))
      return V3ObservationMethod._0235;
    if ("0236".equals(codeString))
      return V3ObservationMethod._0236;
    if ("0237".equals(codeString))
      return V3ObservationMethod._0237;
    if ("0238".equals(codeString))
      return V3ObservationMethod._0238;
    if ("0239".equals(codeString))
      return V3ObservationMethod._0239;
    if ("0243".equals(codeString))
      return V3ObservationMethod._0243;
    if ("0244".equals(codeString))
      return V3ObservationMethod._0244;
    if ("0247".equals(codeString))
      return V3ObservationMethod._0247;
    if ("0248".equals(codeString))
      return V3ObservationMethod._0248;
    if ("0249".equals(codeString))
      return V3ObservationMethod._0249;
    if ("0250".equals(codeString))
      return V3ObservationMethod._0250;
    if ("0251".equals(codeString))
      return V3ObservationMethod._0251;
    if ("0252".equals(codeString))
      return V3ObservationMethod._0252;
    if ("0253".equals(codeString))
      return V3ObservationMethod._0253;
    if ("0254".equals(codeString))
      return V3ObservationMethod._0254;
    if ("0255".equals(codeString))
      return V3ObservationMethod._0255;
    if ("0256".equals(codeString))
      return V3ObservationMethod._0256;
    if ("0257".equals(codeString))
      return V3ObservationMethod._0257;
    if ("0258".equals(codeString))
      return V3ObservationMethod._0258;
    if ("0259".equals(codeString))
      return V3ObservationMethod._0259;
    if ("0260".equals(codeString))
      return V3ObservationMethod._0260;
    if ("0261".equals(codeString))
      return V3ObservationMethod._0261;
    if ("0262".equals(codeString))
      return V3ObservationMethod._0262;
    if ("0263".equals(codeString))
      return V3ObservationMethod._0263;
    if ("0264".equals(codeString))
      return V3ObservationMethod._0264;
    if ("0265".equals(codeString))
      return V3ObservationMethod._0265;
    if ("0266".equals(codeString))
      return V3ObservationMethod._0266;
    if ("0267".equals(codeString))
      return V3ObservationMethod._0267;
    if ("0268".equals(codeString))
      return V3ObservationMethod._0268;
    if ("0269".equals(codeString))
      return V3ObservationMethod._0269;
    if ("0270".equals(codeString))
      return V3ObservationMethod._0270;
    if ("0271".equals(codeString))
      return V3ObservationMethod._0271;
    if ("0280".equals(codeString))
      return V3ObservationMethod._0280;
    if ("0240".equals(codeString))
      return V3ObservationMethod._0240;
    if ("0241".equals(codeString))
      return V3ObservationMethod._0241;
    if ("0242".equals(codeString))
      return V3ObservationMethod._0242;
    if ("0272".equals(codeString))
      return V3ObservationMethod._0272;
    if ("0245".equals(codeString))
      return V3ObservationMethod._0245;
    if ("0246".equals(codeString))
      return V3ObservationMethod._0246;
    if ("0273".equals(codeString))
      return V3ObservationMethod._0273;
    if ("0274".equals(codeString))
      return V3ObservationMethod._0274;
    if ("0275".equals(codeString))
      return V3ObservationMethod._0275;
    if ("0275a".equals(codeString))
      return V3ObservationMethod._0275A;
    if ("0276".equals(codeString))
      return V3ObservationMethod._0276;
    if ("0277".equals(codeString))
      return V3ObservationMethod._0277;
    if ("0278".equals(codeString))
      return V3ObservationMethod._0278;
    if ("0279".equals(codeString))
      return V3ObservationMethod._0279;
    if ("0127".equals(codeString))
      return V3ObservationMethod._0127;
    throw new IllegalArgumentException("Unknown V3ObservationMethod code '"+codeString+"'");
  }

  public String toCode(V3ObservationMethod code) {
    if (code == V3ObservationMethod._DECISIONOBSERVATIONMETHOD)
      return "_DecisionObservationMethod";
    if (code == V3ObservationMethod.ALGM)
      return "ALGM";
    if (code == V3ObservationMethod.BYCL)
      return "BYCL";
    if (code == V3ObservationMethod.GINT)
      return "GINT";
    if (code == V3ObservationMethod._GENETICOBSERVATIONMETHOD)
      return "_GeneticObservationMethod";
    if (code == V3ObservationMethod.PCR)
      return "PCR";
    if (code == V3ObservationMethod._OBSERVATIONMETHODAGGREGATE)
      return "_ObservationMethodAggregate";
    if (code == V3ObservationMethod.AVERAGE)
      return "AVERAGE";
    if (code == V3ObservationMethod.COUNT)
      return "COUNT";
    if (code == V3ObservationMethod.MAX)
      return "MAX";
    if (code == V3ObservationMethod.MEDIAN)
      return "MEDIAN";
    if (code == V3ObservationMethod.MIN)
      return "MIN";
    if (code == V3ObservationMethod.MODE)
      return "MODE";
    if (code == V3ObservationMethod.STDEV_P)
      return "STDEV.P";
    if (code == V3ObservationMethod.STDEV_S)
      return "STDEV.S";
    if (code == V3ObservationMethod.SUM)
      return "SUM";
    if (code == V3ObservationMethod.VARIANCE_P)
      return "VARIANCE.P";
    if (code == V3ObservationMethod.VARIANCE_S)
      return "VARIANCE.S";
    if (code == V3ObservationMethod._VERIFICATIONMETHOD)
      return "_VerificationMethod";
    if (code == V3ObservationMethod.VDOC)
      return "VDOC";
    if (code == V3ObservationMethod.VREG)
      return "VREG";
    if (code == V3ObservationMethod.VTOKEN)
      return "VTOKEN";
    if (code == V3ObservationMethod.VVOICE)
      return "VVOICE";
    if (code == V3ObservationMethod._0001)
      return "0001";
    if (code == V3ObservationMethod._0002)
      return "0002";
    if (code == V3ObservationMethod._0003)
      return "0003";
    if (code == V3ObservationMethod._0004)
      return "0004";
    if (code == V3ObservationMethod._0005)
      return "0005";
    if (code == V3ObservationMethod._0006)
      return "0006";
    if (code == V3ObservationMethod._0007)
      return "0007";
    if (code == V3ObservationMethod._0008)
      return "0008";
    if (code == V3ObservationMethod._0009)
      return "0009";
    if (code == V3ObservationMethod._0010)
      return "0010";
    if (code == V3ObservationMethod._0011)
      return "0011";
    if (code == V3ObservationMethod._0012)
      return "0012";
    if (code == V3ObservationMethod._0013)
      return "0013";
    if (code == V3ObservationMethod._0014)
      return "0014";
    if (code == V3ObservationMethod._0015)
      return "0015";
    if (code == V3ObservationMethod._0016)
      return "0016";
    if (code == V3ObservationMethod._0017)
      return "0017";
    if (code == V3ObservationMethod._0018)
      return "0018";
    if (code == V3ObservationMethod._0019)
      return "0019";
    if (code == V3ObservationMethod._0020)
      return "0020";
    if (code == V3ObservationMethod._0021)
      return "0021";
    if (code == V3ObservationMethod._0022)
      return "0022";
    if (code == V3ObservationMethod._0023)
      return "0023";
    if (code == V3ObservationMethod._0024)
      return "0024";
    if (code == V3ObservationMethod._0025)
      return "0025";
    if (code == V3ObservationMethod._0026)
      return "0026";
    if (code == V3ObservationMethod._0027)
      return "0027";
    if (code == V3ObservationMethod._0028)
      return "0028";
    if (code == V3ObservationMethod._0029)
      return "0029";
    if (code == V3ObservationMethod._0030)
      return "0030";
    if (code == V3ObservationMethod._0031)
      return "0031";
    if (code == V3ObservationMethod._0032)
      return "0032";
    if (code == V3ObservationMethod._0033)
      return "0033";
    if (code == V3ObservationMethod._0034)
      return "0034";
    if (code == V3ObservationMethod._0035)
      return "0035";
    if (code == V3ObservationMethod._0036)
      return "0036";
    if (code == V3ObservationMethod._0037)
      return "0037";
    if (code == V3ObservationMethod._0038)
      return "0038";
    if (code == V3ObservationMethod._0039)
      return "0039";
    if (code == V3ObservationMethod._0040)
      return "0040";
    if (code == V3ObservationMethod._0041)
      return "0041";
    if (code == V3ObservationMethod._0042)
      return "0042";
    if (code == V3ObservationMethod._0043)
      return "0043";
    if (code == V3ObservationMethod._0044)
      return "0044";
    if (code == V3ObservationMethod._0045)
      return "0045";
    if (code == V3ObservationMethod._0046)
      return "0046";
    if (code == V3ObservationMethod._0047)
      return "0047";
    if (code == V3ObservationMethod._0048)
      return "0048";
    if (code == V3ObservationMethod._0049)
      return "0049";
    if (code == V3ObservationMethod._0050)
      return "0050";
    if (code == V3ObservationMethod._0051)
      return "0051";
    if (code == V3ObservationMethod._0052)
      return "0052";
    if (code == V3ObservationMethod._0053)
      return "0053";
    if (code == V3ObservationMethod._0054)
      return "0054";
    if (code == V3ObservationMethod._0055)
      return "0055";
    if (code == V3ObservationMethod._0056)
      return "0056";
    if (code == V3ObservationMethod._0057)
      return "0057";
    if (code == V3ObservationMethod._0058)
      return "0058";
    if (code == V3ObservationMethod._0059)
      return "0059";
    if (code == V3ObservationMethod._0060)
      return "0060";
    if (code == V3ObservationMethod._0061)
      return "0061";
    if (code == V3ObservationMethod._0062)
      return "0062";
    if (code == V3ObservationMethod._0063)
      return "0063";
    if (code == V3ObservationMethod._0064)
      return "0064";
    if (code == V3ObservationMethod._0065)
      return "0065";
    if (code == V3ObservationMethod._0066)
      return "0066";
    if (code == V3ObservationMethod._0067)
      return "0067";
    if (code == V3ObservationMethod._0068)
      return "0068";
    if (code == V3ObservationMethod._0069)
      return "0069";
    if (code == V3ObservationMethod._0070)
      return "0070";
    if (code == V3ObservationMethod._0071)
      return "0071";
    if (code == V3ObservationMethod._0072)
      return "0072";
    if (code == V3ObservationMethod._0073)
      return "0073";
    if (code == V3ObservationMethod._0074)
      return "0074";
    if (code == V3ObservationMethod._0075)
      return "0075";
    if (code == V3ObservationMethod._0076)
      return "0076";
    if (code == V3ObservationMethod._0077)
      return "0077";
    if (code == V3ObservationMethod._0078)
      return "0078";
    if (code == V3ObservationMethod._0079)
      return "0079";
    if (code == V3ObservationMethod._0080)
      return "0080";
    if (code == V3ObservationMethod._0081)
      return "0081";
    if (code == V3ObservationMethod._0082)
      return "0082";
    if (code == V3ObservationMethod._0083)
      return "0083";
    if (code == V3ObservationMethod._0084)
      return "0084";
    if (code == V3ObservationMethod._0085)
      return "0085";
    if (code == V3ObservationMethod._0086)
      return "0086";
    if (code == V3ObservationMethod._0087)
      return "0087";
    if (code == V3ObservationMethod._0088)
      return "0088";
    if (code == V3ObservationMethod._0089)
      return "0089";
    if (code == V3ObservationMethod._0090)
      return "0090";
    if (code == V3ObservationMethod._0091)
      return "0091";
    if (code == V3ObservationMethod._0092)
      return "0092";
    if (code == V3ObservationMethod._0093)
      return "0093";
    if (code == V3ObservationMethod._0094)
      return "0094";
    if (code == V3ObservationMethod._0095)
      return "0095";
    if (code == V3ObservationMethod._0096)
      return "0096";
    if (code == V3ObservationMethod._0097)
      return "0097";
    if (code == V3ObservationMethod._0098)
      return "0098";
    if (code == V3ObservationMethod._0099)
      return "0099";
    if (code == V3ObservationMethod._0100)
      return "0100";
    if (code == V3ObservationMethod._0101)
      return "0101";
    if (code == V3ObservationMethod._0102)
      return "0102";
    if (code == V3ObservationMethod._0103)
      return "0103";
    if (code == V3ObservationMethod._0104)
      return "0104";
    if (code == V3ObservationMethod._0105)
      return "0105";
    if (code == V3ObservationMethod._0106)
      return "0106";
    if (code == V3ObservationMethod._0107)
      return "0107";
    if (code == V3ObservationMethod._0108)
      return "0108";
    if (code == V3ObservationMethod._0109)
      return "0109";
    if (code == V3ObservationMethod._0110)
      return "0110";
    if (code == V3ObservationMethod._0111)
      return "0111";
    if (code == V3ObservationMethod._0112)
      return "0112";
    if (code == V3ObservationMethod._0113)
      return "0113";
    if (code == V3ObservationMethod._0114)
      return "0114";
    if (code == V3ObservationMethod._0115)
      return "0115";
    if (code == V3ObservationMethod._0116)
      return "0116";
    if (code == V3ObservationMethod._0117)
      return "0117";
    if (code == V3ObservationMethod._0118)
      return "0118";
    if (code == V3ObservationMethod._0119)
      return "0119";
    if (code == V3ObservationMethod._0120)
      return "0120";
    if (code == V3ObservationMethod._0121)
      return "0121";
    if (code == V3ObservationMethod._0122)
      return "0122";
    if (code == V3ObservationMethod._0123)
      return "0123";
    if (code == V3ObservationMethod._0124)
      return "0124";
    if (code == V3ObservationMethod._0125)
      return "0125";
    if (code == V3ObservationMethod._0126)
      return "0126";
    if (code == V3ObservationMethod._0128)
      return "0128";
    if (code == V3ObservationMethod._0129)
      return "0129";
    if (code == V3ObservationMethod._0130)
      return "0130";
    if (code == V3ObservationMethod._0131)
      return "0131";
    if (code == V3ObservationMethod._0132)
      return "0132";
    if (code == V3ObservationMethod._0133)
      return "0133";
    if (code == V3ObservationMethod._0134)
      return "0134";
    if (code == V3ObservationMethod._0135)
      return "0135";
    if (code == V3ObservationMethod._0136)
      return "0136";
    if (code == V3ObservationMethod._0137)
      return "0137";
    if (code == V3ObservationMethod._0138)
      return "0138";
    if (code == V3ObservationMethod._0139)
      return "0139";
    if (code == V3ObservationMethod._0140)
      return "0140";
    if (code == V3ObservationMethod._0141)
      return "0141";
    if (code == V3ObservationMethod._0142)
      return "0142";
    if (code == V3ObservationMethod._0143)
      return "0143";
    if (code == V3ObservationMethod._0144)
      return "0144";
    if (code == V3ObservationMethod._0145)
      return "0145";
    if (code == V3ObservationMethod._0146)
      return "0146";
    if (code == V3ObservationMethod._0147)
      return "0147";
    if (code == V3ObservationMethod._0148)
      return "0148";
    if (code == V3ObservationMethod._0149)
      return "0149";
    if (code == V3ObservationMethod._0150)
      return "0150";
    if (code == V3ObservationMethod._0151)
      return "0151";
    if (code == V3ObservationMethod._0152)
      return "0152";
    if (code == V3ObservationMethod._0153)
      return "0153";
    if (code == V3ObservationMethod._0154)
      return "0154";
    if (code == V3ObservationMethod._0155)
      return "0155";
    if (code == V3ObservationMethod._0156)
      return "0156";
    if (code == V3ObservationMethod._0157)
      return "0157";
    if (code == V3ObservationMethod._0158)
      return "0158";
    if (code == V3ObservationMethod._0159)
      return "0159";
    if (code == V3ObservationMethod._0160)
      return "0160";
    if (code == V3ObservationMethod._0161)
      return "0161";
    if (code == V3ObservationMethod._0162)
      return "0162";
    if (code == V3ObservationMethod._0163)
      return "0163";
    if (code == V3ObservationMethod._0164)
      return "0164";
    if (code == V3ObservationMethod._0165)
      return "0165";
    if (code == V3ObservationMethod._0166)
      return "0166";
    if (code == V3ObservationMethod._0167)
      return "0167";
    if (code == V3ObservationMethod._0168)
      return "0168";
    if (code == V3ObservationMethod._0169)
      return "0169";
    if (code == V3ObservationMethod._0170)
      return "0170";
    if (code == V3ObservationMethod._0171)
      return "0171";
    if (code == V3ObservationMethod._0172)
      return "0172";
    if (code == V3ObservationMethod._0173)
      return "0173";
    if (code == V3ObservationMethod._0174)
      return "0174";
    if (code == V3ObservationMethod._0175)
      return "0175";
    if (code == V3ObservationMethod._0176)
      return "0176";
    if (code == V3ObservationMethod._0177)
      return "0177";
    if (code == V3ObservationMethod._0178)
      return "0178";
    if (code == V3ObservationMethod._0179)
      return "0179";
    if (code == V3ObservationMethod._0180)
      return "0180";
    if (code == V3ObservationMethod._0181)
      return "0181";
    if (code == V3ObservationMethod._0182)
      return "0182";
    if (code == V3ObservationMethod._0183)
      return "0183";
    if (code == V3ObservationMethod._0184)
      return "0184";
    if (code == V3ObservationMethod._0185)
      return "0185";
    if (code == V3ObservationMethod._0186)
      return "0186";
    if (code == V3ObservationMethod._0187)
      return "0187";
    if (code == V3ObservationMethod._0188)
      return "0188";
    if (code == V3ObservationMethod._0189)
      return "0189";
    if (code == V3ObservationMethod._0190)
      return "0190";
    if (code == V3ObservationMethod._0191)
      return "0191";
    if (code == V3ObservationMethod._0192)
      return "0192";
    if (code == V3ObservationMethod._0193)
      return "0193";
    if (code == V3ObservationMethod._0194)
      return "0194";
    if (code == V3ObservationMethod._0195)
      return "0195";
    if (code == V3ObservationMethod._0196)
      return "0196";
    if (code == V3ObservationMethod._0197)
      return "0197";
    if (code == V3ObservationMethod._0198)
      return "0198";
    if (code == V3ObservationMethod._0199)
      return "0199";
    if (code == V3ObservationMethod._0200)
      return "0200";
    if (code == V3ObservationMethod._0201)
      return "0201";
    if (code == V3ObservationMethod._0202)
      return "0202";
    if (code == V3ObservationMethod._0203)
      return "0203";
    if (code == V3ObservationMethod._0204)
      return "0204";
    if (code == V3ObservationMethod._0205)
      return "0205";
    if (code == V3ObservationMethod._0206)
      return "0206";
    if (code == V3ObservationMethod._0207)
      return "0207";
    if (code == V3ObservationMethod._0208)
      return "0208";
    if (code == V3ObservationMethod._0209)
      return "0209";
    if (code == V3ObservationMethod._0210)
      return "0210";
    if (code == V3ObservationMethod._0211)
      return "0211";
    if (code == V3ObservationMethod._0212)
      return "0212";
    if (code == V3ObservationMethod._0213)
      return "0213";
    if (code == V3ObservationMethod._0214)
      return "0214";
    if (code == V3ObservationMethod._0215)
      return "0215";
    if (code == V3ObservationMethod._0216)
      return "0216";
    if (code == V3ObservationMethod._0217)
      return "0217";
    if (code == V3ObservationMethod._0218)
      return "0218";
    if (code == V3ObservationMethod._0219)
      return "0219";
    if (code == V3ObservationMethod._0220)
      return "0220";
    if (code == V3ObservationMethod._0221)
      return "0221";
    if (code == V3ObservationMethod._0222)
      return "0222";
    if (code == V3ObservationMethod._0223)
      return "0223";
    if (code == V3ObservationMethod._0224)
      return "0224";
    if (code == V3ObservationMethod._0225)
      return "0225";
    if (code == V3ObservationMethod._0226)
      return "0226";
    if (code == V3ObservationMethod._0227)
      return "0227";
    if (code == V3ObservationMethod._0228)
      return "0228";
    if (code == V3ObservationMethod._0229)
      return "0229";
    if (code == V3ObservationMethod._0230)
      return "0230";
    if (code == V3ObservationMethod._0231)
      return "0231";
    if (code == V3ObservationMethod._0232)
      return "0232";
    if (code == V3ObservationMethod._0233)
      return "0233";
    if (code == V3ObservationMethod._0234)
      return "0234";
    if (code == V3ObservationMethod._0235)
      return "0235";
    if (code == V3ObservationMethod._0236)
      return "0236";
    if (code == V3ObservationMethod._0237)
      return "0237";
    if (code == V3ObservationMethod._0238)
      return "0238";
    if (code == V3ObservationMethod._0239)
      return "0239";
    if (code == V3ObservationMethod._0243)
      return "0243";
    if (code == V3ObservationMethod._0244)
      return "0244";
    if (code == V3ObservationMethod._0247)
      return "0247";
    if (code == V3ObservationMethod._0248)
      return "0248";
    if (code == V3ObservationMethod._0249)
      return "0249";
    if (code == V3ObservationMethod._0250)
      return "0250";
    if (code == V3ObservationMethod._0251)
      return "0251";
    if (code == V3ObservationMethod._0252)
      return "0252";
    if (code == V3ObservationMethod._0253)
      return "0253";
    if (code == V3ObservationMethod._0254)
      return "0254";
    if (code == V3ObservationMethod._0255)
      return "0255";
    if (code == V3ObservationMethod._0256)
      return "0256";
    if (code == V3ObservationMethod._0257)
      return "0257";
    if (code == V3ObservationMethod._0258)
      return "0258";
    if (code == V3ObservationMethod._0259)
      return "0259";
    if (code == V3ObservationMethod._0260)
      return "0260";
    if (code == V3ObservationMethod._0261)
      return "0261";
    if (code == V3ObservationMethod._0262)
      return "0262";
    if (code == V3ObservationMethod._0263)
      return "0263";
    if (code == V3ObservationMethod._0264)
      return "0264";
    if (code == V3ObservationMethod._0265)
      return "0265";
    if (code == V3ObservationMethod._0266)
      return "0266";
    if (code == V3ObservationMethod._0267)
      return "0267";
    if (code == V3ObservationMethod._0268)
      return "0268";
    if (code == V3ObservationMethod._0269)
      return "0269";
    if (code == V3ObservationMethod._0270)
      return "0270";
    if (code == V3ObservationMethod._0271)
      return "0271";
    if (code == V3ObservationMethod._0280)
      return "0280";
    if (code == V3ObservationMethod._0240)
      return "0240";
    if (code == V3ObservationMethod._0241)
      return "0241";
    if (code == V3ObservationMethod._0242)
      return "0242";
    if (code == V3ObservationMethod._0272)
      return "0272";
    if (code == V3ObservationMethod._0245)
      return "0245";
    if (code == V3ObservationMethod._0246)
      return "0246";
    if (code == V3ObservationMethod._0273)
      return "0273";
    if (code == V3ObservationMethod._0274)
      return "0274";
    if (code == V3ObservationMethod._0275)
      return "0275";
    if (code == V3ObservationMethod._0275A)
      return "0275a";
    if (code == V3ObservationMethod._0276)
      return "0276";
    if (code == V3ObservationMethod._0277)
      return "0277";
    if (code == V3ObservationMethod._0278)
      return "0278";
    if (code == V3ObservationMethod._0279)
      return "0279";
    if (code == V3ObservationMethod._0127)
      return "0127";
    return "?";
  }

    public String toSystem(V3ObservationMethod code) {
      return code.getSystem();
      }

}

