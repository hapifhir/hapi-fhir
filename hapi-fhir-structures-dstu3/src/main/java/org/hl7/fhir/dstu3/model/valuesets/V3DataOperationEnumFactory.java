package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class V3DataOperationEnumFactory implements EnumFactory<V3DataOperation> {

  public V3DataOperation fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("OPERATE".equals(codeString))
      return V3DataOperation.OPERATE;
    if ("CREATE".equals(codeString))
      return V3DataOperation.CREATE;
    if ("DELETE".equals(codeString))
      return V3DataOperation.DELETE;
    if ("EXECUTE".equals(codeString))
      return V3DataOperation.EXECUTE;
    if ("READ".equals(codeString))
      return V3DataOperation.READ;
    if ("UPDATE".equals(codeString))
      return V3DataOperation.UPDATE;
    if ("APPEND".equals(codeString))
      return V3DataOperation.APPEND;
    if ("MODIFYSTATUS".equals(codeString))
      return V3DataOperation.MODIFYSTATUS;
    if ("ABORT".equals(codeString))
      return V3DataOperation.ABORT;
    if ("ACTIVATE".equals(codeString))
      return V3DataOperation.ACTIVATE;
    if ("CANCEL".equals(codeString))
      return V3DataOperation.CANCEL;
    if ("COMPLETE".equals(codeString))
      return V3DataOperation.COMPLETE;
    if ("HOLD".equals(codeString))
      return V3DataOperation.HOLD;
    if ("JUMP".equals(codeString))
      return V3DataOperation.JUMP;
    if ("NULLIFY".equals(codeString))
      return V3DataOperation.NULLIFY;
    if ("OBSOLETE".equals(codeString))
      return V3DataOperation.OBSOLETE;
    if ("REACTIVATE".equals(codeString))
      return V3DataOperation.REACTIVATE;
    if ("RELEASE".equals(codeString))
      return V3DataOperation.RELEASE;
    if ("RESUME".equals(codeString))
      return V3DataOperation.RESUME;
    if ("SUSPEND".equals(codeString))
      return V3DataOperation.SUSPEND;
    throw new IllegalArgumentException("Unknown V3DataOperation code '"+codeString+"'");
  }

  public String toCode(V3DataOperation code) {
    if (code == V3DataOperation.OPERATE)
      return "OPERATE";
    if (code == V3DataOperation.CREATE)
      return "CREATE";
    if (code == V3DataOperation.DELETE)
      return "DELETE";
    if (code == V3DataOperation.EXECUTE)
      return "EXECUTE";
    if (code == V3DataOperation.READ)
      return "READ";
    if (code == V3DataOperation.UPDATE)
      return "UPDATE";
    if (code == V3DataOperation.APPEND)
      return "APPEND";
    if (code == V3DataOperation.MODIFYSTATUS)
      return "MODIFYSTATUS";
    if (code == V3DataOperation.ABORT)
      return "ABORT";
    if (code == V3DataOperation.ACTIVATE)
      return "ACTIVATE";
    if (code == V3DataOperation.CANCEL)
      return "CANCEL";
    if (code == V3DataOperation.COMPLETE)
      return "COMPLETE";
    if (code == V3DataOperation.HOLD)
      return "HOLD";
    if (code == V3DataOperation.JUMP)
      return "JUMP";
    if (code == V3DataOperation.NULLIFY)
      return "NULLIFY";
    if (code == V3DataOperation.OBSOLETE)
      return "OBSOLETE";
    if (code == V3DataOperation.REACTIVATE)
      return "REACTIVATE";
    if (code == V3DataOperation.RELEASE)
      return "RELEASE";
    if (code == V3DataOperation.RESUME)
      return "RESUME";
    if (code == V3DataOperation.SUSPEND)
      return "SUSPEND";
    return "?";
  }

    public String toSystem(V3DataOperation code) {
      return code.getSystem();
      }

}

