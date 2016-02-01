package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3ActRelationshipSubsetEnumFactory implements EnumFactory<V3ActRelationshipSubset> {

  public V3ActRelationshipSubset fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("_ParticipationSubset".equals(codeString))
      return V3ActRelationshipSubset._PARTICIPATIONSUBSET;
    if ("FUTURE".equals(codeString))
      return V3ActRelationshipSubset.FUTURE;
    if ("FUTSUM".equals(codeString))
      return V3ActRelationshipSubset.FUTSUM;
    if ("LAST".equals(codeString))
      return V3ActRelationshipSubset.LAST;
    if ("NEXT".equals(codeString))
      return V3ActRelationshipSubset.NEXT;
    if ("PAST".equals(codeString))
      return V3ActRelationshipSubset.PAST;
    if ("FIRST".equals(codeString))
      return V3ActRelationshipSubset.FIRST;
    if ("PREVSUM".equals(codeString))
      return V3ActRelationshipSubset.PREVSUM;
    if ("RECENT".equals(codeString))
      return V3ActRelationshipSubset.RECENT;
    if ("SUM".equals(codeString))
      return V3ActRelationshipSubset.SUM;
    if ("ActRelationshipExpectedSubset".equals(codeString))
      return V3ActRelationshipSubset.ACTRELATIONSHIPEXPECTEDSUBSET;
    if ("ActRelationshipPastSubset".equals(codeString))
      return V3ActRelationshipSubset.ACTRELATIONSHIPPASTSUBSET;
    if ("MAX".equals(codeString))
      return V3ActRelationshipSubset.MAX;
    if ("MIN".equals(codeString))
      return V3ActRelationshipSubset.MIN;
    throw new IllegalArgumentException("Unknown V3ActRelationshipSubset code '"+codeString+"'");
  }

  public String toCode(V3ActRelationshipSubset code) {
    if (code == V3ActRelationshipSubset._PARTICIPATIONSUBSET)
      return "_ParticipationSubset";
    if (code == V3ActRelationshipSubset.FUTURE)
      return "FUTURE";
    if (code == V3ActRelationshipSubset.FUTSUM)
      return "FUTSUM";
    if (code == V3ActRelationshipSubset.LAST)
      return "LAST";
    if (code == V3ActRelationshipSubset.NEXT)
      return "NEXT";
    if (code == V3ActRelationshipSubset.PAST)
      return "PAST";
    if (code == V3ActRelationshipSubset.FIRST)
      return "FIRST";
    if (code == V3ActRelationshipSubset.PREVSUM)
      return "PREVSUM";
    if (code == V3ActRelationshipSubset.RECENT)
      return "RECENT";
    if (code == V3ActRelationshipSubset.SUM)
      return "SUM";
    if (code == V3ActRelationshipSubset.ACTRELATIONSHIPEXPECTEDSUBSET)
      return "ActRelationshipExpectedSubset";
    if (code == V3ActRelationshipSubset.ACTRELATIONSHIPPASTSUBSET)
      return "ActRelationshipPastSubset";
    if (code == V3ActRelationshipSubset.MAX)
      return "MAX";
    if (code == V3ActRelationshipSubset.MIN)
      return "MIN";
    return "?";
  }

    public String toSystem(V3ActRelationshipSubset code) {
      return code.getSystem();
      }

}

