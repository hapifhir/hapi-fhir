package org.hl7.fhir.dstu3.model.valuesets;

import org.hl7.fhir.dstu3.model.EnumFactory;

public class TestscriptOperationCodesEnumFactory implements EnumFactory<TestscriptOperationCodes> {

  public TestscriptOperationCodes fromCode(String codeString) throws IllegalArgumentException {
    if (codeString == null || "".equals(codeString))
      return null;
    if ("read".equals(codeString))
      return TestscriptOperationCodes.READ;
    if ("vread".equals(codeString))
      return TestscriptOperationCodes.VREAD;
    if ("update".equals(codeString))
      return TestscriptOperationCodes.UPDATE;
    if ("delete".equals(codeString))
      return TestscriptOperationCodes.DELETE;
    if ("history".equals(codeString))
      return TestscriptOperationCodes.HISTORY;
    if ("create".equals(codeString))
      return TestscriptOperationCodes.CREATE;
    if ("search".equals(codeString))
      return TestscriptOperationCodes.SEARCH;
    if ("transaction".equals(codeString))
      return TestscriptOperationCodes.TRANSACTION;
    if ("conformance".equals(codeString))
      return TestscriptOperationCodes.CONFORMANCE;
    if ("closure".equals(codeString))
      return TestscriptOperationCodes.CLOSURE;
    if ("document".equals(codeString))
      return TestscriptOperationCodes.DOCUMENT;
    if ("everything".equals(codeString))
      return TestscriptOperationCodes.EVERYTHING;
    if ("expand".equals(codeString))
      return TestscriptOperationCodes.EXPAND;
    if ("find".equals(codeString))
      return TestscriptOperationCodes.FIND;
    if ("lookup".equals(codeString))
      return TestscriptOperationCodes.LOOKUP;
    if ("meta".equals(codeString))
      return TestscriptOperationCodes.META;
    if ("meta-add".equals(codeString))
      return TestscriptOperationCodes.METAADD;
    if ("meta-delete".equals(codeString))
      return TestscriptOperationCodes.METADELETE;
    if ("populate".equals(codeString))
      return TestscriptOperationCodes.POPULATE;
    if ("process-message".equals(codeString))
      return TestscriptOperationCodes.PROCESSMESSAGE;
    if ("questionnaire".equals(codeString))
      return TestscriptOperationCodes.QUESTIONNAIRE;
    if ("translate".equals(codeString))
      return TestscriptOperationCodes.TRANSLATE;
    if ("validate".equals(codeString))
      return TestscriptOperationCodes.VALIDATE;
    if ("validate-code".equals(codeString))
      return TestscriptOperationCodes.VALIDATECODE;
    throw new IllegalArgumentException("Unknown TestscriptOperationCodes code '"+codeString+"'");
  }

  public String toCode(TestscriptOperationCodes code) {
    if (code == TestscriptOperationCodes.READ)
      return "read";
    if (code == TestscriptOperationCodes.VREAD)
      return "vread";
    if (code == TestscriptOperationCodes.UPDATE)
      return "update";
    if (code == TestscriptOperationCodes.DELETE)
      return "delete";
    if (code == TestscriptOperationCodes.HISTORY)
      return "history";
    if (code == TestscriptOperationCodes.CREATE)
      return "create";
    if (code == TestscriptOperationCodes.SEARCH)
      return "search";
    if (code == TestscriptOperationCodes.TRANSACTION)
      return "transaction";
    if (code == TestscriptOperationCodes.CONFORMANCE)
      return "conformance";
    if (code == TestscriptOperationCodes.CLOSURE)
      return "closure";
    if (code == TestscriptOperationCodes.DOCUMENT)
      return "document";
    if (code == TestscriptOperationCodes.EVERYTHING)
      return "everything";
    if (code == TestscriptOperationCodes.EXPAND)
      return "expand";
    if (code == TestscriptOperationCodes.FIND)
      return "find";
    if (code == TestscriptOperationCodes.LOOKUP)
      return "lookup";
    if (code == TestscriptOperationCodes.META)
      return "meta";
    if (code == TestscriptOperationCodes.METAADD)
      return "meta-add";
    if (code == TestscriptOperationCodes.METADELETE)
      return "meta-delete";
    if (code == TestscriptOperationCodes.POPULATE)
      return "populate";
    if (code == TestscriptOperationCodes.PROCESSMESSAGE)
      return "process-message";
    if (code == TestscriptOperationCodes.QUESTIONNAIRE)
      return "questionnaire";
    if (code == TestscriptOperationCodes.TRANSLATE)
      return "translate";
    if (code == TestscriptOperationCodes.VALIDATE)
      return "validate";
    if (code == TestscriptOperationCodes.VALIDATECODE)
      return "validate-code";
    return "?";
  }

    public String toSystem(TestscriptOperationCodes code) {
      return code.getSystem();
      }

}

