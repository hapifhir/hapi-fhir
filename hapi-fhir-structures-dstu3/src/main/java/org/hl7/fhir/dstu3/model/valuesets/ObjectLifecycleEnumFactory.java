package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class ObjectLifecycleEnumFactory implements EnumFactory<ObjectLifecycle> {

  public ObjectLifecycle fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("1".equals(codeString))
      return ObjectLifecycle._1;
    if ("2".equals(codeString))
      return ObjectLifecycle._2;
    if ("3".equals(codeString))
      return ObjectLifecycle._3;
    if ("4".equals(codeString))
      return ObjectLifecycle._4;
    if ("5".equals(codeString))
      return ObjectLifecycle._5;
    if ("6".equals(codeString))
      return ObjectLifecycle._6;
    if ("7".equals(codeString))
      return ObjectLifecycle._7;
    if ("8".equals(codeString))
      return ObjectLifecycle._8;
    if ("9".equals(codeString))
      return ObjectLifecycle._9;
    if ("10".equals(codeString))
      return ObjectLifecycle._10;
    if ("11".equals(codeString))
      return ObjectLifecycle._11;
    if ("12".equals(codeString))
      return ObjectLifecycle._12;
    if ("13".equals(codeString))
      return ObjectLifecycle._13;
    if ("14".equals(codeString))
      return ObjectLifecycle._14;
    if ("15".equals(codeString))
      return ObjectLifecycle._15;
    throw new IllegalArgumentException("Unknown ObjectLifecycle code '"+codeString+"'");
  }

  public String toCode(ObjectLifecycle code) {
    if (code == ObjectLifecycle._1)
      return "1";
    if (code == ObjectLifecycle._2)
      return "2";
    if (code == ObjectLifecycle._3)
      return "3";
    if (code == ObjectLifecycle._4)
      return "4";
    if (code == ObjectLifecycle._5)
      return "5";
    if (code == ObjectLifecycle._6)
      return "6";
    if (code == ObjectLifecycle._7)
      return "7";
    if (code == ObjectLifecycle._8)
      return "8";
    if (code == ObjectLifecycle._9)
      return "9";
    if (code == ObjectLifecycle._10)
      return "10";
    if (code == ObjectLifecycle._11)
      return "11";
    if (code == ObjectLifecycle._12)
      return "12";
    if (code == ObjectLifecycle._13)
      return "13";
    if (code == ObjectLifecycle._14)
      return "14";
    if (code == ObjectLifecycle._15)
      return "15";
    return "?";
  }

    public String toSystem(ObjectLifecycle code) {
      return code.getSystem();
      }

}

