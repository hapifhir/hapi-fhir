package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ObservationInterpretationEnumFactory implements EnumFactory<V3ObservationInterpretation> {

  public V3ObservationInterpretation fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_GeneticObservationInterpretation".equals(codeString))
      return V3ObservationInterpretation._GENETICOBSERVATIONINTERPRETATION;
    if ("CAR".equals(codeString))
      return V3ObservationInterpretation.CAR;
    if ("Carrier".equals(codeString))
      return V3ObservationInterpretation.CARRIER;
    if ("_ObservationInterpretationChange".equals(codeString))
      return V3ObservationInterpretation._OBSERVATIONINTERPRETATIONCHANGE;
    if ("B".equals(codeString))
      return V3ObservationInterpretation.B;
    if ("D".equals(codeString))
      return V3ObservationInterpretation.D;
    if ("U".equals(codeString))
      return V3ObservationInterpretation.U;
    if ("W".equals(codeString))
      return V3ObservationInterpretation.W;
    if ("_ObservationInterpretationExceptions".equals(codeString))
      return V3ObservationInterpretation._OBSERVATIONINTERPRETATIONEXCEPTIONS;
    if ("<".equals(codeString))
      return V3ObservationInterpretation.LESS_THAN;
    if (">".equals(codeString))
      return V3ObservationInterpretation.GREATER_THAN;
    if ("AC".equals(codeString))
      return V3ObservationInterpretation.AC;
    if ("IE".equals(codeString))
      return V3ObservationInterpretation.IE;
    if ("QCF".equals(codeString))
      return V3ObservationInterpretation.QCF;
    if ("TOX".equals(codeString))
      return V3ObservationInterpretation.TOX;
    if ("_ObservationInterpretationNormality".equals(codeString))
      return V3ObservationInterpretation._OBSERVATIONINTERPRETATIONNORMALITY;
    if ("A".equals(codeString))
      return V3ObservationInterpretation.A;
    if ("AA".equals(codeString))
      return V3ObservationInterpretation.AA;
    if ("HH".equals(codeString))
      return V3ObservationInterpretation.HH;
    if ("LL".equals(codeString))
      return V3ObservationInterpretation.LL;
    if ("H".equals(codeString))
      return V3ObservationInterpretation.H;
    if ("H>".equals(codeString))
      return V3ObservationInterpretation.H_;
    if ("HU".equals(codeString))
      return V3ObservationInterpretation.HU;
    if ("L".equals(codeString))
      return V3ObservationInterpretation.L;
    if ("L<".equals(codeString))
      return V3ObservationInterpretation.L_;
    if ("LU".equals(codeString))
      return V3ObservationInterpretation.LU;
    if ("N".equals(codeString))
      return V3ObservationInterpretation.N;
    if ("_ObservationInterpretationSusceptibility".equals(codeString))
      return V3ObservationInterpretation._OBSERVATIONINTERPRETATIONSUSCEPTIBILITY;
    if ("I".equals(codeString))
      return V3ObservationInterpretation.I;
    if ("MS".equals(codeString))
      return V3ObservationInterpretation.MS;
    if ("NS".equals(codeString))
      return V3ObservationInterpretation.NS;
    if ("R".equals(codeString))
      return V3ObservationInterpretation.R;
    if ("SYN-R".equals(codeString))
      return V3ObservationInterpretation.SYNR;
    if ("S".equals(codeString))
      return V3ObservationInterpretation.S;
    if ("SDD".equals(codeString))
      return V3ObservationInterpretation.SDD;
    if ("SYN-S".equals(codeString))
      return V3ObservationInterpretation.SYNS;
    if ("VS".equals(codeString))
      return V3ObservationInterpretation.VS;
    if ("EX".equals(codeString))
      return V3ObservationInterpretation.EX;
    if ("HX".equals(codeString))
      return V3ObservationInterpretation.HX;
    if ("LX".equals(codeString))
      return V3ObservationInterpretation.LX;
    if ("ObservationInterpretationDetection".equals(codeString))
      return V3ObservationInterpretation.OBSERVATIONINTERPRETATIONDETECTION;
    if ("IND".equals(codeString))
      return V3ObservationInterpretation.IND;
    if ("E".equals(codeString))
      return V3ObservationInterpretation.E;
    if ("NEG".equals(codeString))
      return V3ObservationInterpretation.NEG;
    if ("ND".equals(codeString))
      return V3ObservationInterpretation.ND;
    if ("POS".equals(codeString))
      return V3ObservationInterpretation.POS;
    if ("DET".equals(codeString))
      return V3ObservationInterpretation.DET;
    if ("ObservationInterpretationExpectation".equals(codeString))
      return V3ObservationInterpretation.OBSERVATIONINTERPRETATIONEXPECTATION;
    if ("EXP".equals(codeString))
      return V3ObservationInterpretation.EXP;
    if ("UNE".equals(codeString))
      return V3ObservationInterpretation.UNE;
    if ("ReactivityObservationInterpretation".equals(codeString))
      return V3ObservationInterpretation.REACTIVITYOBSERVATIONINTERPRETATION;
    if ("NR".equals(codeString))
      return V3ObservationInterpretation.NR;
    if ("RR".equals(codeString))
      return V3ObservationInterpretation.RR;
    if ("WR".equals(codeString))
      return V3ObservationInterpretation.WR;
    throw new IllegalArgumentException("Unknown V3ObservationInterpretation code '"+codeString+"'");
  }

  public String toCode(V3ObservationInterpretation code) {
    if (code == V3ObservationInterpretation._GENETICOBSERVATIONINTERPRETATION)
      return "_GeneticObservationInterpretation";
    if (code == V3ObservationInterpretation.CAR)
      return "CAR";
    if (code == V3ObservationInterpretation.CARRIER)
      return "Carrier";
    if (code == V3ObservationInterpretation._OBSERVATIONINTERPRETATIONCHANGE)
      return "_ObservationInterpretationChange";
    if (code == V3ObservationInterpretation.B)
      return "B";
    if (code == V3ObservationInterpretation.D)
      return "D";
    if (code == V3ObservationInterpretation.U)
      return "U";
    if (code == V3ObservationInterpretation.W)
      return "W";
    if (code == V3ObservationInterpretation._OBSERVATIONINTERPRETATIONEXCEPTIONS)
      return "_ObservationInterpretationExceptions";
    if (code == V3ObservationInterpretation.LESS_THAN)
      return "<";
    if (code == V3ObservationInterpretation.GREATER_THAN)
      return ">";
    if (code == V3ObservationInterpretation.AC)
      return "AC";
    if (code == V3ObservationInterpretation.IE)
      return "IE";
    if (code == V3ObservationInterpretation.QCF)
      return "QCF";
    if (code == V3ObservationInterpretation.TOX)
      return "TOX";
    if (code == V3ObservationInterpretation._OBSERVATIONINTERPRETATIONNORMALITY)
      return "_ObservationInterpretationNormality";
    if (code == V3ObservationInterpretation.A)
      return "A";
    if (code == V3ObservationInterpretation.AA)
      return "AA";
    if (code == V3ObservationInterpretation.HH)
      return "HH";
    if (code == V3ObservationInterpretation.LL)
      return "LL";
    if (code == V3ObservationInterpretation.H)
      return "H";
    if (code == V3ObservationInterpretation.H_)
      return "H>";
    if (code == V3ObservationInterpretation.HU)
      return "HU";
    if (code == V3ObservationInterpretation.L)
      return "L";
    if (code == V3ObservationInterpretation.L_)
      return "L<";
    if (code == V3ObservationInterpretation.LU)
      return "LU";
    if (code == V3ObservationInterpretation.N)
      return "N";
    if (code == V3ObservationInterpretation._OBSERVATIONINTERPRETATIONSUSCEPTIBILITY)
      return "_ObservationInterpretationSusceptibility";
    if (code == V3ObservationInterpretation.I)
      return "I";
    if (code == V3ObservationInterpretation.MS)
      return "MS";
    if (code == V3ObservationInterpretation.NS)
      return "NS";
    if (code == V3ObservationInterpretation.R)
      return "R";
    if (code == V3ObservationInterpretation.SYNR)
      return "SYN-R";
    if (code == V3ObservationInterpretation.S)
      return "S";
    if (code == V3ObservationInterpretation.SDD)
      return "SDD";
    if (code == V3ObservationInterpretation.SYNS)
      return "SYN-S";
    if (code == V3ObservationInterpretation.VS)
      return "VS";
    if (code == V3ObservationInterpretation.EX)
      return "EX";
    if (code == V3ObservationInterpretation.HX)
      return "HX";
    if (code == V3ObservationInterpretation.LX)
      return "LX";
    if (code == V3ObservationInterpretation.OBSERVATIONINTERPRETATIONDETECTION)
      return "ObservationInterpretationDetection";
    if (code == V3ObservationInterpretation.IND)
      return "IND";
    if (code == V3ObservationInterpretation.E)
      return "E";
    if (code == V3ObservationInterpretation.NEG)
      return "NEG";
    if (code == V3ObservationInterpretation.ND)
      return "ND";
    if (code == V3ObservationInterpretation.POS)
      return "POS";
    if (code == V3ObservationInterpretation.DET)
      return "DET";
    if (code == V3ObservationInterpretation.OBSERVATIONINTERPRETATIONEXPECTATION)
      return "ObservationInterpretationExpectation";
    if (code == V3ObservationInterpretation.EXP)
      return "EXP";
    if (code == V3ObservationInterpretation.UNE)
      return "UNE";
    if (code == V3ObservationInterpretation.REACTIVITYOBSERVATIONINTERPRETATION)
      return "ReactivityObservationInterpretation";
    if (code == V3ObservationInterpretation.NR)
      return "NR";
    if (code == V3ObservationInterpretation.RR)
      return "RR";
    if (code == V3ObservationInterpretation.WR)
      return "WR";
    return "?";
  }

    public String toSystem(V3ObservationInterpretation code) {
      return code.getSystem();
      }

}

