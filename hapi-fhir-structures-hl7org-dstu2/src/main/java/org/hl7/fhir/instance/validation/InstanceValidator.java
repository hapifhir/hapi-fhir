package org.hl7.fhir.instance.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.hl7.fhir.instance.formats.FormatUtilities;
import org.hl7.fhir.instance.model.Address;
import org.hl7.fhir.instance.model.Attachment;
import org.hl7.fhir.instance.model.CodeableConcept;
import org.hl7.fhir.instance.model.Coding;
import org.hl7.fhir.instance.model.ContactPoint;
import org.hl7.fhir.instance.model.ElementDefinition;
import org.hl7.fhir.instance.model.ElementDefinition.ElementDefinitionBindingComponent;
import org.hl7.fhir.instance.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.instance.model.Enumerations.BindingStrength;
import org.hl7.fhir.instance.model.Extension;
import org.hl7.fhir.instance.model.HumanName;
import org.hl7.fhir.instance.model.Identifier;
import org.hl7.fhir.instance.model.OperationOutcome.IssueSeverity;
import org.hl7.fhir.instance.model.OperationOutcome.IssueType;
import org.hl7.fhir.instance.model.Period;
import org.hl7.fhir.instance.model.Quantity;
import org.hl7.fhir.instance.model.Range;
import org.hl7.fhir.instance.model.Ratio;
import org.hl7.fhir.instance.model.Reference;
import org.hl7.fhir.instance.model.Resource;
import org.hl7.fhir.instance.model.SampledData;
import org.hl7.fhir.instance.model.StringType;
import org.hl7.fhir.instance.model.StructureDefinition;
import org.hl7.fhir.instance.model.StructureDefinition.ExtensionContext;
import org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.instance.model.StructureDefinition.StructureDefinitionSnapshotComponent;
import org.hl7.fhir.instance.model.Timing;
import org.hl7.fhir.instance.model.Type;
import org.hl7.fhir.instance.model.UriType;
import org.hl7.fhir.instance.model.ValueSet;
import org.hl7.fhir.instance.model.ValueSet.ConceptDefinitionComponent;
import org.hl7.fhir.instance.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.instance.terminologies.ValueSetExpander.ETooCostly;
import org.hl7.fhir.instance.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.instance.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.instance.utilities.Utilities;
import org.hl7.fhir.instance.utilities.xml.XMLUtil;
import org.hl7.fhir.instance.terminologies.ValueSetExpanderFactory;
import org.hl7.fhir.instance.terminologies.ValueSetExpansionCache;
import org.hl7.fhir.instance.utils.EOperationOutcome;
import org.hl7.fhir.instance.utils.IWorkerContext;
import org.hl7.fhir.instance.utils.IWorkerContext.ValidationResult;
import org.hl7.fhir.instance.utils.ProfileUtilities;
import org.hl7.fhir.instance.validation.ValidationMessage.Source;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;


/* 
 * todo:
 * check urn's don't start oid: or uuid: 
 */
public class InstanceValidator extends BaseValidator implements IResourceValidator {

  private boolean anyExtensionsAllowed;

  private BestPracticeWarningLevel bpWarnings;
  // configuration items
  private CheckDisplayOption checkDisplay;
  private IWorkerContext context;

  private List<String> extensionDomains = new ArrayList<String>();

  private boolean requiresResourceId;

  // used during the build process to keep the overall volume of messages down
  private boolean suppressLoincSnomedMessages;

  public InstanceValidator(IWorkerContext theContext) throws Exception {
    super();
    this.context = theContext;
    source = Source.InstanceValidator;
  }

  private boolean allowUnknownExtension(String url) {
    if (url.contains("example.org") || url.contains("acme.com") || url.contains("nema.org"))
      return true;
    for (String s : extensionDomains)
      if (url.startsWith(s))
        return true;
    return anyExtensionsAllowed;
  }

  private void bpCheck(List<ValidationMessage> errors, IssueType invalid, int line, int col, String literalPath, boolean test, String message) {
    if (bpWarnings != null) {
      switch (bpWarnings) {
      case Error:
        rule(errors, invalid, line, col, literalPath, test, message);
      case Warning:
        warning(errors, invalid, line, col, literalPath, test, message);
      case Hint:
        hint(errors, invalid, line, col, literalPath, test, message);
      default: // do nothing
      }
    }
  }

  private boolean check(String v1, String v2) {
    return v1 == null ? Utilities.noString(v1) : v1.equals(v2);
  }

  private void checkAddress(List<ValidationMessage> errors, String path, WrapperElement focus, Address fixed) {
    checkFixedValue(errors, path + ".use", focus.getNamedChild("use"), fixed.getUseElement(), "use");
    checkFixedValue(errors, path + ".text", focus.getNamedChild("text"), fixed.getTextElement(), "text");
    checkFixedValue(errors, path + ".city", focus.getNamedChild("city"), fixed.getCityElement(), "city");
    checkFixedValue(errors, path + ".state", focus.getNamedChild("state"), fixed.getStateElement(), "state");
    checkFixedValue(errors, path + ".country", focus.getNamedChild("country"), fixed.getCountryElement(), "country");
    checkFixedValue(errors, path + ".zip", focus.getNamedChild("zip"), fixed.getPostalCodeElement(), "postalCode");

    List<WrapperElement> lines = new ArrayList<WrapperElement>();
    focus.getNamedChildren("line", lines);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, lines.size() == fixed.getLine().size(),
        "Expected " + Integer.toString(fixed.getLine().size()) + " but found " + Integer.toString(lines.size()) + " line elements")) {
      for (int i = 0; i < lines.size(); i++)
        checkFixedValue(errors, path + ".coding", lines.get(i), fixed.getLine().get(i), "coding");
    }
  }

  private void checkAttachment(List<ValidationMessage> errors, String path, WrapperElement focus, Attachment fixed) {
    checkFixedValue(errors, path + ".contentType", focus.getNamedChild("contentType"), fixed.getContentTypeElement(), "contentType");
    checkFixedValue(errors, path + ".language", focus.getNamedChild("language"), fixed.getLanguageElement(), "language");
    checkFixedValue(errors, path + ".data", focus.getNamedChild("data"), fixed.getDataElement(), "data");
    checkFixedValue(errors, path + ".url", focus.getNamedChild("url"), fixed.getUrlElement(), "url");
    checkFixedValue(errors, path + ".size", focus.getNamedChild("size"), fixed.getSizeElement(), "size");
    checkFixedValue(errors, path + ".hash", focus.getNamedChild("hash"), fixed.getHashElement(), "hash");
    checkFixedValue(errors, path + ".title", focus.getNamedChild("title"), fixed.getTitleElement(), "title");
  }

  // public API

  private boolean checkCode(List<ValidationMessage> errors, WrapperElement element, String path, String code, String system, String display) throws Exception {
    if (context.supportsSystem(system)) {
      ValidationResult s = context.validateCode(system, code, display);
      if (s == null || s.isOk())
        return true;
      if (s.getSeverity() == IssueSeverity.INFORMATION)
        hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, s == null, s.getMessage());
      else if (s.getSeverity() == IssueSeverity.WARNING)
        warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, s == null, s.getMessage());
      else
        return rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, s == null, s.getMessage());
      return true;
    } else if (system.startsWith("http://hl7.org/fhir")) {
      if (system.equals("http://hl7.org/fhir/sid/icd-10"))
        return true; // else don't check ICD-10 (for now)
      else {
        ValueSet vs = getValueSet(system);
        if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, vs != null, "Unknown Code System " + system)) {
          ConceptDefinitionComponent def = getCodeDefinition(vs, code);
          if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, def != null, "Unknown Code (" + system + "#" + code + ")"))
            return warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, display == null || display.equals(def.getDisplay()), "Display should be '" + def.getDisplay() + "'");
        }
        return false;
      }
    } else if (system.startsWith("http://loinc.org")) {
      return true;
    } else if (system.startsWith("http://unitsofmeasure.org")) {
      return true;
    } else
      return true;
  }

  private void checkCodeableConcept(List<ValidationMessage> errors, String path, WrapperElement focus, CodeableConcept fixed) {
    checkFixedValue(errors, path + ".text", focus.getNamedChild("text"), fixed.getTextElement(), "text");
    List<WrapperElement> codings = new ArrayList<WrapperElement>();
    focus.getNamedChildren("coding", codings);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, codings.size() == fixed.getCoding().size(),
        "Expected " + Integer.toString(fixed.getCoding().size()) + " but found " + Integer.toString(codings.size()) + " coding elements")) {
      for (int i = 0; i < codings.size(); i++)
        checkFixedValue(errors, path + ".coding", codings.get(i), fixed.getCoding().get(i), "coding");
    }
  }

  private void checkCodeableConcept(List<ValidationMessage> errors, String path, WrapperElement element, StructureDefinition profile, ElementDefinition theElementCntext) throws EOperationOutcome, Exception {
    if (theElementCntext != null && theElementCntext.hasBinding()) {
      ElementDefinitionBindingComponent binding = theElementCntext.getBinding();
      if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, binding != null, "Binding for " + path + " missing (cc)")) {
        if (binding.hasValueSet() && binding.getValueSet() instanceof Reference) {
          ValueSet valueset = resolveBindingReference(binding.getValueSet());
          if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, valueset != null, "ValueSet " + describeReference(binding.getValueSet()) + " not found")) {
          	CodeableConcept cc = null;
          	try {
          		cc = readAsCodeableConcept(element);
              if (!cc.hasCoding()) {
                if (binding.getStrength() == BindingStrength.REQUIRED)
                  rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "No code provided, and a code is required from the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl());
                else if (binding.getStrength() == BindingStrength.EXTENSIBLE)
                  warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "No code provided, and a code should be provided from the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl());
              } else {
          		ValidationResult vr = context.validateCode(cc, valueset);
            	if (!vr.isOk()) {
                if (binding.getStrength() == BindingStrength.REQUIRED)
                    rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "None of the codes provided are in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl()+", and a code from this value set is required");
                else if (binding.getStrength() == BindingStrength.EXTENSIBLE)
                    warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "None of the codes provided are in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code should come from this value set unless it has no suitable code");
                else if (binding.getStrength() == BindingStrength.PREFERRED)
                    hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "None of the codes provided are in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code is recommended to come from this value set");
                }
            	}
          	} catch (Exception e) {
          		warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Error "+e.getMessage()+" validating CodeableConcept");
          	}
          }
        } else if (binding.hasValueSet()) {
          hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Binding by URI reference cannot be checked");
        } else {
          hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Binding for path " + path + " has no source, so can't be checked");
        }
      }
    }
  }

  private CodeableConcept readAsCodeableConcept(WrapperElement element) {
	  CodeableConcept cc = new CodeableConcept();
	  List<WrapperElement> list = new ArrayList<WrapperElement>();
	  element.getNamedChildren("coding", list);
	  for (WrapperElement item : list)
	  	cc.addCoding(readAsCoding(item));
    cc.setText(element.getNamedChildValue("text"));
	  return cc;
	}

	private Coding readAsCoding(WrapperElement item) {
		Coding c = new Coding();
		c.setSystem(item.getNamedChildValue("system"));
		c.setVersion(item.getNamedChildValue("version"));
		c.setCode(item.getNamedChildValue("code"));
		c.setDisplay(item.getNamedChildValue("display"));
  	return c;
	}

	private void checkCoding(List<ValidationMessage> errors, String path, WrapperElement focus, Coding fixed) {
    checkFixedValue(errors, path + ".system", focus.getNamedChild("system"), fixed.getSystemElement(), "system");
    checkFixedValue(errors, path + ".code", focus.getNamedChild("code"), fixed.getCodeElement(), "code");
    checkFixedValue(errors, path + ".display", focus.getNamedChild("display"), fixed.getDisplayElement(), "display");
    checkFixedValue(errors, path + ".userSelected", focus.getNamedChild("userSelected"), fixed.getUserSelectedElement(), "userSelected");
  }

  private void checkCoding(List<ValidationMessage> errors, String path, WrapperElement element, StructureDefinition profile, ElementDefinition theElementCntext, boolean inCodeableConcept) throws EOperationOutcome, Exception {
    String code = element.getNamedChildValue("code");
    String system = element.getNamedChildValue("system");
    String display = element.getNamedChildValue("display");
    rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, isAbsolute(system), "Coding.system must be an absolute reference, not a local reference");

    if (system != null && code != null) {
      if (checkCode(errors, element, path, code, system, display))
        if (theElementCntext != null && theElementCntext.getBinding() != null) {
          ElementDefinitionBindingComponent binding = theElementCntext.getBinding();
          if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, binding != null, "Binding for " + path + " missing")) {
            if (binding.hasValueSet() && binding.getValueSet() instanceof Reference) {
              ValueSet valueset = resolveBindingReference(binding.getValueSet());
              if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, valueset != null, "ValueSet " + describeReference(binding.getValueSet()) + " not found")) {
              	Coding c = null;
              	try {
              		c = readAsCoding(element);
              		ValidationResult vr = context.validateCode(c, valueset);
                	if (!vr.isOk()) {
                    if (binding.getStrength() == BindingStrength.REQUIRED)
                      warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The value provided is not in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code is required from this value set");
                    else if (binding.getStrength() == BindingStrength.EXTENSIBLE)
                      warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The value provided is not in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code should come from this value set unless it has no suitable code");
                    else if (binding.getStrength() == BindingStrength.PREFERRED)
                    	hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "The value provided is not in the value set " + describeReference(binding.getValueSet()) + " (" + valueset.getUrl() + ", and a code is recommended to come from this value set");
                	}
              	} catch (Exception e) {
              		warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Error "+e.getMessage()+" validating CodeableConcept");
              	}
              }
            } else if (binding.hasValueSet()) {
              hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Binding by URI reference cannot be checked");
            } else if (!inCodeableConcept) {
              hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Binding for path " + path + " has no source, so can't be checked");
            }
          }
        }
    }
  }

  private void checkContactPoint(List<ValidationMessage> errors, String path, WrapperElement focus, ContactPoint fixed) {
    checkFixedValue(errors, path + ".system", focus.getNamedChild("system"), fixed.getSystemElement(), "system");
    checkFixedValue(errors, path + ".value", focus.getNamedChild("value"), fixed.getValueElement(), "value");
    checkFixedValue(errors, path + ".use", focus.getNamedChild("use"), fixed.getUseElement(), "use");
    checkFixedValue(errors, path + ".period", focus.getNamedChild("period"), fixed.getPeriod(), "period");

  }

  private void checkDeclaredProfiles(List<ValidationMessage> errors, WrapperElement element, NodeStack stack) throws Exception {
    WrapperElement meta = element.getNamedChild("meta");
    if (meta != null) {
      List<WrapperElement> profiles = new ArrayList<InstanceValidator.WrapperElement>();
      meta.getNamedChildren("profile", profiles);
      int i = 0;
      for (WrapperElement profile : profiles) {
        String ref = profile.getAttribute("value");
        String p = stack.addToLiteralPath("meta", "profile", ":" + Integer.toString(i));
        if (rule(errors, IssueType.INVALID, element.line(), element.col(), p, !Utilities.noString(ref), "StructureDefinition reference invalid")) {
          StructureDefinition pr = context.fetchResource(StructureDefinition.class, ref);
          if (warning(errors, IssueType.INVALID, element.line(), element.col(), p, pr != null, "StructureDefinition reference \"{0}\" could not be resolved", ref)) {
            if (rule(errors, IssueType.STRUCTURE, element.line(), element.col(), p, pr.hasSnapshot(),
                "StructureDefinition has no snapshot - validation is against the snapshot, so it must be provided")) {
              validateElement(errors, pr, pr.getSnapshot().getElement().get(0), null, null, element, element.getName(), stack, false);
            }
          }
          i++;
        }
      }
    }
  }

  private StructureDefinition checkExtension(List<ValidationMessage> errors, String path, WrapperElement element, ElementDefinition def, StructureDefinition profile, NodeStack stack)
      throws Exception {
    String url = element.getAttribute("url");
    boolean isModifier = element.getName().equals("modifierExtension");

    StructureDefinition ex = context.fetchResource(StructureDefinition.class, url);
    if (ex == null) {
      if (!rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, allowUnknownExtension(url), "The extension " + url + " is unknown, and not allowed here"))
        warning(errors, IssueType.STRUCTURE, element.line(), element.col(), path, allowUnknownExtension(url), "Unknown extension " + url);
    } else {
      if (def.getIsModifier())
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", ex.getSnapshot().getElement().get(0).getIsModifier(),
            "Extension modifier mismatch: the extension element is labelled as a modifier, but the underlying extension is not");
      else
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", !ex.getSnapshot().getElement().get(0).getIsModifier(),
            "Extension modifier mismatch: the extension element is not labelled as a modifier, but the underlying extension is");

      // two questions
      // 1. can this extension be used here?
      checkExtensionContext(errors, element, /* path+"[url='"+url+"']", */ ex, stack, ex.getUrl());

      if (isModifier)
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", ex.getSnapshot().getElement().get(0).getIsModifier(),
            "The Extension '" + url + "' must be used as a modifierExtension");
      else
        rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path + "[url='" + url + "']", !ex.getSnapshot().getElement().get(0).getIsModifier(),
            "The Extension '" + url + "' must not be used as an extension (it's a modifierExtension)");

      // 2. is the content of the extension valid?

    }
    return ex;
  }

  private boolean checkExtensionContext(List<ValidationMessage> errors, WrapperElement element, StructureDefinition definition, NodeStack stack, String extensionParent) {
    String extUrl = definition.getUrl();
    CommaSeparatedStringBuilder p = new CommaSeparatedStringBuilder();
    for (String lp : stack.getLogicalPaths())
      p.append(lp);
    if (definition.getContextType() == ExtensionContext.DATATYPE) {
      boolean ok = false;
      CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
      for (StringType ct : definition.getContext()) {
        b.append(ct.getValue());
        if (ct.getValue().equals("*") || stack.getLogicalPaths().contains(ct.getValue() + ".extension"))
          ok = true;
      }
      return rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), ok,
          "The extension " + extUrl + " is not allowed to be used on the logical path set [" + p.toString() + "] (allowed: datatype=" + b.toString() + ")");
    } else if (definition.getContextType() == ExtensionContext.EXTENSION) {
      boolean ok = false;
      for (StringType ct : definition.getContext())
        if (ct.getValue().equals("*") || ct.getValue().equals(extensionParent))
          ok = true;
      return rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), ok,
          "The extension " + extUrl + " is not allowed to be used with the extension '" + extensionParent + "'");
    } else if (definition.getContextType() == ExtensionContext.MAPPING) {
      throw new Error("Not handled yet (extensionContext)");
    } else if (definition.getContextType() == ExtensionContext.RESOURCE) {
      boolean ok = false;
      // String simplePath = container.getPath();
      // System.out.println(simplePath);
      // if (effetive.endsWith(".extension") || simplePath.endsWith(".modifierExtension"))
      // simplePath = simplePath.substring(0, simplePath.lastIndexOf('.'));
      CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
      for (StringType ct : definition.getContext()) {
        String c = ct.getValue();
        b.append(c);
        if (c.equals("*") || stack.getLogicalPaths().contains(c + ".extension") || (c.startsWith("@") && stack.getLogicalPaths().contains(c.substring(1) + ".extension")))
          ;
        ok = true;
      }
      return rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), ok,
          "The extension " + extUrl + " is not allowed to be used on the logical path set " + p.toString() + " (allowed: resource=" + b.toString() + ")");
    } else
      throw new Error("Unknown context type");
  }
  //
  // private String simplifyPath(String path) {
  // String s = path.replace("/f:", ".");
  // while (s.contains("["))
  // s = s.substring(0, s.indexOf("["))+s.substring(s.indexOf("]")+1);
  // String[] parts = s.split("\\.");
  // int i = 0;
  // while (i < parts.length && !context.getProfiles().containsKey(parts[i].toLowerCase()))
  // i++;
  // if (i >= parts.length)
  // throw new Error("Unable to process part "+path);
  // int j = parts.length - 1;
  // while (j > 0 && (parts[j].equals("extension") || parts[j].equals("modifierExtension")))
  // j--;
  // StringBuilder b = new StringBuilder();
  // boolean first = true;
  // for (int k = i; k <= j; k++) {
  // if (k == j || !parts[k].equals(parts[k+1])) {
  // if (first)
  // first = false;
  // else
  // b.append(".");
  // b.append(parts[k]);
  // }
  // }
  // return b.toString();
  // }
  //

  private void checkFixedValue(List<ValidationMessage> errors, String path, WrapperElement focus, org.hl7.fhir.instance.model.Element fixed, String propName) {
    if (fixed == null && focus == null)
      ; // this is all good
    else if (fixed == null && focus != null)
      rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, false, "Unexpected element " + focus.getName());
    else if (fixed != null && focus == null)
      rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, false, "Mising element " + propName);
    else {
      String value = focus.getAttribute("value");
      if (fixed instanceof org.hl7.fhir.instance.model.BooleanType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.BooleanType) fixed).asStringValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.BooleanType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.IntegerType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.IntegerType) fixed).asStringValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.IntegerType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.DecimalType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.DecimalType) fixed).asStringValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.DecimalType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.Base64BinaryType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.Base64BinaryType) fixed).asStringValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.Base64BinaryType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.InstantType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.InstantType) fixed).getValue().toString(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.InstantType) fixed).asStringValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.StringType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.StringType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.StringType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.UriType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.UriType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.UriType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.DateType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.DateType) fixed).getValue().toString(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.DateType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.DateTimeType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.DateTimeType) fixed).getValue().toString(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.DateTimeType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.OidType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.OidType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.OidType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.UuidType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.UuidType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.UuidType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.CodeType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.CodeType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.CodeType) fixed).getValue() + "'");
      else if (fixed instanceof org.hl7.fhir.instance.model.IdType)
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, check(((org.hl7.fhir.instance.model.IdType) fixed).getValue(), value),
            "Value is '" + value + "' but must be '" + ((org.hl7.fhir.instance.model.IdType) fixed).getValue() + "'");
      else if (fixed instanceof Quantity)
        checkQuantity(errors, path, focus, (Quantity) fixed);
      else if (fixed instanceof Address)
        checkAddress(errors, path, focus, (Address) fixed);
      else if (fixed instanceof ContactPoint)
        checkContactPoint(errors, path, focus, (ContactPoint) fixed);
      else if (fixed instanceof Attachment)
        checkAttachment(errors, path, focus, (Attachment) fixed);
      else if (fixed instanceof Identifier)
        checkIdentifier(errors, path, focus, (Identifier) fixed);
      else if (fixed instanceof Coding)
        checkCoding(errors, path, focus, (Coding) fixed);
      else if (fixed instanceof HumanName)
        checkHumanName(errors, path, focus, (HumanName) fixed);
      else if (fixed instanceof CodeableConcept)
        checkCodeableConcept(errors, path, focus, (CodeableConcept) fixed);
      else if (fixed instanceof Timing)
        checkTiming(errors, path, focus, (Timing) fixed);
      else if (fixed instanceof Period)
        checkPeriod(errors, path, focus, (Period) fixed);
      else if (fixed instanceof Range)
        checkRange(errors, path, focus, (Range) fixed);
      else if (fixed instanceof Ratio)
        checkRatio(errors, path, focus, (Ratio) fixed);
      else if (fixed instanceof SampledData)
        checkSampledData(errors, path, focus, (SampledData) fixed);

      else
        rule(errors, IssueType.EXCEPTION, focus.line(), focus.col(), path, false, "Unhandled fixed value type " + fixed.getClass().getName());
      List<WrapperElement> extensions = new ArrayList<WrapperElement>();
      focus.getNamedChildren("extension", extensions);
      if (fixed.getExtension().size() == 0) {
        rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, extensions.size() == 0, "No extensions allowed");
      } else if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, extensions.size() == fixed.getExtension().size(),
          "Extensions count mismatch: expected " + Integer.toString(fixed.getExtension().size()) + " but found " + Integer.toString(extensions.size()))) {
        for (Extension e : fixed.getExtension()) {
          WrapperElement ex = getExtensionByUrl(extensions, e.getUrl());
          if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, ex != null, "Extension count mismatch: unable to find extension: " + e.getUrl())) {
            checkFixedValue(errors, path, ex.getFirstChild().getNextSibling(), e.getValue(), "extension.value");
          }
        }
      }
    }
  }

  private void checkForProcessingInstruction(List<ValidationMessage> errors, Document document) {
    Node node = document.getFirstChild();
    while (node != null) {
      rule(errors, IssueType.INVALID, -1, -1, "(document)", node.getNodeType() != Node.PROCESSING_INSTRUCTION_NODE, "No processing instructions allowed in resources");
      node = node.getNextSibling();
    }
  }

  private void checkHumanName(List<ValidationMessage> errors, String path, WrapperElement focus, HumanName fixed) {
    checkFixedValue(errors, path + ".use", focus.getNamedChild("use"), fixed.getUseElement(), "use");
    checkFixedValue(errors, path + ".text", focus.getNamedChild("text"), fixed.getTextElement(), "text");
    checkFixedValue(errors, path + ".period", focus.getNamedChild("period"), fixed.getPeriod(), "period");

    List<WrapperElement> parts = new ArrayList<WrapperElement>();
    focus.getNamedChildren("family", parts);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, parts.size() == fixed.getFamily().size(),
        "Expected " + Integer.toString(fixed.getFamily().size()) + " but found " + Integer.toString(parts.size()) + " family elements")) {
      for (int i = 0; i < parts.size(); i++)
        checkFixedValue(errors, path + ".family", parts.get(i), fixed.getFamily().get(i), "family");
    }
    focus.getNamedChildren("given", parts);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, parts.size() == fixed.getGiven().size(),
        "Expected " + Integer.toString(fixed.getGiven().size()) + " but found " + Integer.toString(parts.size()) + " given elements")) {
      for (int i = 0; i < parts.size(); i++)
        checkFixedValue(errors, path + ".given", parts.get(i), fixed.getGiven().get(i), "given");
    }
    focus.getNamedChildren("prefix", parts);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, parts.size() == fixed.getPrefix().size(),
        "Expected " + Integer.toString(fixed.getPrefix().size()) + " but found " + Integer.toString(parts.size()) + " prefix elements")) {
      for (int i = 0; i < parts.size(); i++)
        checkFixedValue(errors, path + ".prefix", parts.get(i), fixed.getPrefix().get(i), "prefix");
    }
    focus.getNamedChildren("suffix", parts);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, parts.size() == fixed.getSuffix().size(),
        "Expected " + Integer.toString(fixed.getSuffix().size()) + " but found " + Integer.toString(parts.size()) + " suffix elements")) {
      for (int i = 0; i < parts.size(); i++)
        checkFixedValue(errors, path + ".suffix", parts.get(i), fixed.getSuffix().get(i), "suffix");
    }
  }

  private void checkIdentifier(List<ValidationMessage> errors, String path, WrapperElement element, ElementDefinition context) {
    String system = element.getNamedChildValue("system");
    rule(errors, IssueType.CODEINVALID, element.line(), element.col(), path, isAbsolute(system), "Identifier.system must be an absolute reference, not a local reference");
  }

  private void checkIdentifier(List<ValidationMessage> errors, String path, WrapperElement focus, Identifier fixed) {
    checkFixedValue(errors, path + ".use", focus.getNamedChild("use"), fixed.getUseElement(), "use");
    checkFixedValue(errors, path + ".type", focus.getNamedChild("type"), fixed.getType(), "type");
    checkFixedValue(errors, path + ".system", focus.getNamedChild("system"), fixed.getSystemElement(), "system");
    checkFixedValue(errors, path + ".value", focus.getNamedChild("value"), fixed.getValueElement(), "value");
    checkFixedValue(errors, path + ".period", focus.getNamedChild("period"), fixed.getPeriod(), "period");
    checkFixedValue(errors, path + ".assigner", focus.getNamedChild("assigner"), fixed.getAssigner(), "assigner");
  }

  private void checkPeriod(List<ValidationMessage> errors, String path, WrapperElement focus, Period fixed) {
    checkFixedValue(errors, path + ".start", focus.getNamedChild("start"), fixed.getStartElement(), "start");
    checkFixedValue(errors, path + ".end", focus.getNamedChild("end"), fixed.getEndElement(), "end");
  }

  private void checkPrimitive(List<ValidationMessage> errors, String path, String type, ElementDefinition context, WrapperElement e) throws Exception {
    if (type.equals("uri")) {
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !e.getAttribute("value").startsWith("oid:"), "URI values cannot start with oid:");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !e.getAttribute("value").startsWith("uuid:"), "URI values cannot start with uuid:");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, e.getAttribute("value").equals(e.getAttribute("value").trim()), "URI values cannot have leading or trailing whitespace");
    }
    if (!type.equalsIgnoreCase("string") && e.hasAttribute("value")) {
      if (rule(errors, IssueType.INVALID, e.line(), e.col(), path, e.getAttribute("value").length() > 0, "@value cannot be empty")) {
        warning(errors, IssueType.INVALID, e.line(), e.col(), path, e.getAttribute("value").trim().equals(e.getAttribute("value")), "value should not start or finish with whitespace");
      }
    }
    if (type.equals("dateTime")) {
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, yearIsValid(e.getAttribute("value")), "The value '" + e.getAttribute("value") + "' does not have a valid year");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path,
          e.getAttribute("value")
          .matches("-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?)?)?)?"),
          "Not a valid date time");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, !hasTime(e.getAttribute("value")) || hasTimeZone(e.getAttribute("value")), "if a date has a time, it must have a timezone");

    }
    if (type.equals("instant")) {
      rule(errors, IssueType.INVALID, e.line(), e.col(), path,
          e.getAttribute("value").matches("-?[0-9]{4}-(0[1-9]|1[0-2])-(0[0-9]|[1-2][0-9]|3[0-1])T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))"),
          "The instant '" + e.getAttribute("value") + "' is not valid (by regex)");
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, yearIsValid(e.getAttribute("value")), "The value '" + e.getAttribute("value") + "' does not have a valid year");
    }

    if (type.equals("code")) {
      // Technically, a code is restricted to string which has at least one character and no leading or trailing whitespace, and where there is no whitespace
      // other than single spaces in the contents
      rule(errors, IssueType.INVALID, e.line(), e.col(), path, passesCodeWhitespaceRules(e.getAttribute("value")), "The code '" + e.getAttribute("value") + "' is not valid (whitespace rules)");
    }

    if (context.hasBinding()) {
      checkPrimitiveBinding(errors, path, type, context, e);
    }
    // for nothing to check
  }

  // note that we don't check the type here; it could be string, uri or code.
  private void checkPrimitiveBinding(List<ValidationMessage> errors, String path, String type, ElementDefinition elementContext, WrapperElement element) throws Exception {
    if (!element.hasAttribute("value"))
      return;

    String value = element.getAttribute("value");
    String system = null;
    String display = null;
    // System.out.println("check "+value+" in "+path);

    // firstly, resolve the value set
    ElementDefinitionBindingComponent binding = elementContext.getBinding();
    if (binding.hasValueSet() && binding.getValueSet() instanceof Reference) {
      ValueSet vs = resolveBindingReference(binding.getValueSet());
      if (warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, vs != null, "ValueSet {0} not found", describeReference(binding.getValueSet()))) {
      	ValidationResult vr = context.validateCode(system, value, display, vs);
      	if (!vr.isOk()) {
      		if (binding.getStrength() == BindingStrength.REQUIRED)
      			warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The value provided is not in the value set " + describeReference(binding.getValueSet()) + " (" + vs.getUrl() + ", and a code is required from this value set");
      		else if (binding.getStrength() == BindingStrength.EXTENSIBLE)
      			warning(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "The value provided is not in the value set " + describeReference(binding.getValueSet()) + " (" + vs.getUrl() + ", and a code should come from this value set unless it has no suitable code");
      		else if (binding.getStrength() == BindingStrength.PREFERRED)
      			hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false,  "The value provided is not in the value set " + describeReference(binding.getValueSet()) + " (" + vs.getUrl() + ", and a code is recommended to come from this value set");
      	}
      }
    } else
      hint(errors, IssueType.CODEINVALID, element.line(), element.col(), path, false, "Binding has no source, so can't be checked");
  }

  private void checkQuantity(List<ValidationMessage> errors, String path, WrapperElement focus, Quantity fixed) {
    checkFixedValue(errors, path + ".value", focus.getNamedChild("value"), fixed.getValueElement(), "value");
    checkFixedValue(errors, path + ".comparator", focus.getNamedChild("comparator"), fixed.getComparatorElement(), "comparator");
    checkFixedValue(errors, path + ".units", focus.getNamedChild("unit"), fixed.getUnitElement(), "units");
    checkFixedValue(errors, path + ".system", focus.getNamedChild("system"), fixed.getSystemElement(), "system");
    checkFixedValue(errors, path + ".code", focus.getNamedChild("code"), fixed.getCodeElement(), "code");
  }

  // implementation

  private void checkRange(List<ValidationMessage> errors, String path, WrapperElement focus, Range fixed) {
    checkFixedValue(errors, path + ".low", focus.getNamedChild("low"), fixed.getLow(), "low");
    checkFixedValue(errors, path + ".high", focus.getNamedChild("high"), fixed.getHigh(), "high");

  }

  private void checkRatio(List<ValidationMessage> errors, String path, WrapperElement focus, Ratio fixed) {
    checkFixedValue(errors, path + ".numerator", focus.getNamedChild("numerator"), fixed.getNumerator(), "numerator");
    checkFixedValue(errors, path + ".denominator", focus.getNamedChild("denominator"), fixed.getDenominator(), "denominator");
  }

  private void checkReference(List<ValidationMessage> errors, String path, WrapperElement element, StructureDefinition profile, ElementDefinition container, String parentType, NodeStack stack)
      throws Exception {
    String ref = element.getNamedChildValue("reference");
    if (Utilities.noString(ref)) {
      // todo - what should we do in this case?
      hint(errors, IssueType.STRUCTURE, element.line(), element.col(), path, !Utilities.noString(element.getNamedChildValue("display")),
          "A Reference without an actual reference should have a display");
      return;
    }

    WrapperElement we = resolve(ref, stack);
    String ft;
    if (we != null)
      ft = we.getResourceType();
    else
      ft = tryParse(ref);
    if (hint(errors, IssueType.STRUCTURE, element.line(), element.col(), path, ft != null, "Unable to determine type of target resource")) {
      boolean ok = false;
      CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
      for (TypeRefComponent type : container.getType()) {
        if (!ok && type.getCode().equals("Reference")) {
          // we validate as much as we can. First, can we infer a type from the profile?
          if (!type.hasProfile() || type.getProfile().get(0).getValue().equals("http://hl7.org/fhir/StructureDefinition/Resource"))
            ok = true;
          else {
            String pr = type.getProfile().get(0).getValue();

            String bt = getBaseType(profile, pr);
            if (rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, bt != null, "Unable to resolve the profile reference '" + pr + "'")) {
              b.append(bt);
              ok = bt.equals(ft);
            } else
              ok = true; // suppress following check
          }
        }
        if (!ok && type.getCode().equals("*")) {
          ok = true; // can refer to anything
        }
      }
      rule(errors, IssueType.STRUCTURE, element.line(), element.col(), path, ok, "Invalid Resource target type. Found " + ft + ", but expected one of (" + b.toString() + ")");
    }
  }

  private String checkResourceType(String type) throws EOperationOutcome, Exception {
    if (context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + type) != null)
      return type;
    else
      return null;
  }

  private void checkSampledData(List<ValidationMessage> errors, String path, WrapperElement focus, SampledData fixed) {
    checkFixedValue(errors, path + ".origin", focus.getNamedChild("origin"), fixed.getOrigin(), "origin");
    checkFixedValue(errors, path + ".period", focus.getNamedChild("period"), fixed.getPeriodElement(), "period");
    checkFixedValue(errors, path + ".factor", focus.getNamedChild("factor"), fixed.getFactorElement(), "factor");
    checkFixedValue(errors, path + ".lowerLimit", focus.getNamedChild("lowerLimit"), fixed.getLowerLimitElement(), "lowerLimit");
    checkFixedValue(errors, path + ".upperLimit", focus.getNamedChild("upperLimit"), fixed.getUpperLimitElement(), "upperLimit");
    checkFixedValue(errors, path + ".dimensions", focus.getNamedChild("dimensions"), fixed.getDimensionsElement(), "dimensions");
    checkFixedValue(errors, path + ".data", focus.getNamedChild("data"), fixed.getDataElement(), "data");
  }

  private void checkTiming(List<ValidationMessage> errors, String path, WrapperElement focus, Timing fixed) {
    checkFixedValue(errors, path + ".repeat", focus.getNamedChild("repeat"), fixed.getRepeat(), "value");

    List<WrapperElement> events = new ArrayList<WrapperElement>();
    focus.getNamedChildren("event", events);
    if (rule(errors, IssueType.VALUE, focus.line(), focus.col(), path, events.size() == fixed.getEvent().size(),
        "Expected " + Integer.toString(fixed.getEvent().size()) + " but found " + Integer.toString(events.size()) + " event elements")) {
      for (int i = 0; i < events.size(); i++)
        checkFixedValue(errors, path + ".event", events.get(i), fixed.getEvent().get(i), "event");
    }
  }

  private boolean codeinExpansion(ValueSetExpansionContainsComponent cnt, String system, String code) {
    for (ValueSetExpansionContainsComponent c : cnt.getContains()) {
      if (code.equals(c.getCode()) && system.equals(c.getSystem().toString()))
        return true;
      if (codeinExpansion(c, system, code))
        return true;
    }    
    return false;
  }

  private boolean codeInExpansion(ValueSet vs, String system, String code) {
    for (ValueSetExpansionContainsComponent c : vs.getExpansion().getContains()) {
      if (code.equals(c.getCode()) && (system == null || system.equals(c.getSystem())))
        return true;
      if (codeinExpansion(c, system, code))
        return true;
    }
    return false;
  }

  private String describeReference(Type reference) {
    if (reference == null)
      return "null";
    if (reference instanceof UriType)
      return ((UriType) reference).getValue();
    if (reference instanceof Reference)
      return ((Reference) reference).getReference();
    return "??";
  }

  private String describeTypes(List<TypeRefComponent> types) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (TypeRefComponent t : types) {
      b.append(t.getCode());
    }
    return b.toString();
  }

  private boolean empty(WrapperElement element) {
    if (element.hasAttribute("value"))
      return false;
    if (element.hasAttribute("xml:id"))
      return false;
    WrapperElement child = element.getFirstChild();
    while (child != null) {
      if (!child.isXml() || FormatUtilities.FHIR_NS.equals(child.getNamespace())) {
        return false;
      }
      child = child.getNextSibling();
    }
    return true;
  }

  private ElementDefinition findElement(StructureDefinition profile, String name) {
    for (ElementDefinition c : profile.getSnapshot().getElement()) {
      if (c.getPath().equals(name)) {
        return c;
      }
    }
    return null;
  }

  private String genFullUrl(String bundleBase, String entryBase, String type, String id) {
    String base = Utilities.noString(entryBase) ? bundleBase : entryBase;
    if (Utilities.noString(base)) {
      return type + "/" + id;
    } else if ("urn:uuid".equals(base) || "urn:oid".equals(base))
      return base + id;
    else
      return Utilities.appendSlash(base) + type + "/" + id;
  }  

  public BestPracticeWarningLevel getBasePracticeWarningLevel() {
    return bpWarnings;
  }  

  private String getBaseType(StructureDefinition profile, String pr) throws EOperationOutcome, Exception {
    // if (pr.startsWith("http://hl7.org/fhir/StructureDefinition/")) {
    // // this just has to be a base type
    // return pr.substring(40);
    // } else {
    StructureDefinition p = resolveProfile(profile, pr);
    if (p == null)
      return null;
    else if (p.getKind() == StructureDefinitionKind.RESOURCE)
      return p.getSnapshot().getElement().get(0).getPath();
    else
      return p.getSnapshot().getElement().get(0).getType().get(0).getCode();
    // }
  }

  @Override
  public CheckDisplayOption getCheckDisplay() {
    return checkDisplay;
  }

  //	private String findProfileTag(WrapperElement element) {
  //  	String uri = null;
  //	  List<WrapperElement> list = new ArrayList<WrapperElement>();
  //	  element.getNamedChildren("category", list);
  //	  for (WrapperElement c : list) {
  //	  	if ("http://hl7.org/fhir/tag/profile".equals(c.getAttribute("scheme"))) {
  //	  		uri = c.getAttribute("term");
  //	  	}
  //	  }
  //	  return uri;
  //  }

  private ConceptDefinitionComponent getCodeDefinition(ConceptDefinitionComponent c, String code) {
    if (code.equals(c.getCode()))
      return c;
    for (ConceptDefinitionComponent g : c.getConcept()) {
      ConceptDefinitionComponent r = getCodeDefinition(g, code);
      if (r != null)
        return r;
    }
    return null;
  }

  private ConceptDefinitionComponent getCodeDefinition(ValueSet vs, String code) {
    for (ConceptDefinitionComponent c : vs.getCodeSystem().getConcept()) {
      ConceptDefinitionComponent r = getCodeDefinition(c, code);
      if (r != null)
        return r;
    }
    return null;
  }

  private WrapperElement getContainedById(WrapperElement container, String id) {
    List<WrapperElement> contained = new ArrayList<WrapperElement>();
    container.getNamedChildren("contained", contained);
    for (WrapperElement we : contained) {
      WrapperElement res = we.isXml() ? we.getFirstChild() : we;
      if (id.equals(res.getNamedChildValue("id")))
        return res;
    }   
    return null;
  }

  public IWorkerContext getContext() {
    return context;
  }

  private ElementDefinition getCriteriaForDiscriminator(String path, ElementDefinition ed, String discriminator, StructureDefinition profile) throws Exception {
    List<ElementDefinition> childDefinitions = ProfileUtilities.getChildMap(profile, ed);
    List<ElementDefinition> snapshot = null;
    if (childDefinitions.isEmpty()) {
      // going to look at the type
      if (ed.getType().size() == 0)
        throw new Exception("Error in profile for " + path + " no children, no type");
      if (ed.getType().size() > 1)
        throw new Exception("Error in profile for " + path + " multiple types defined in slice discriminator");
      StructureDefinition type;
      if (ed.getType().get(0).hasProfile()) {
        // need to do some special processing for reference here...
        if (ed.getType().get(0).getCode().equals("Reference"))
          discriminator = discriminator.substring(discriminator.indexOf(".")+1);
        type = context.fetchResource(StructureDefinition.class, ed.getType().get(0).getProfile().get(0).getValue());
      } else
        type = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + ed.getType().get(0).getCode());
      snapshot = type.getSnapshot().getElement();
      ed = snapshot.get(0);
    } else {
      snapshot = profile.getSnapshot().getElement();
    }
    String originalPath = ed.getPath();
    String goal = originalPath + "." + discriminator;

    int index = snapshot.indexOf(ed);
    assert(index > -1);
    index++;
    while (index < snapshot.size() && !snapshot.get(index).getPath().equals(originalPath)) {
      if (snapshot.get(index).getPath().equals(goal))
        return snapshot.get(index);
      index++;
    }
    throw new Error("Unable to find discriminator definition for " + goal + " in " + discriminator + " at " + path);
  }

  private WrapperElement getExtensionByUrl(List<WrapperElement> extensions, String urlSimple) {
    for (WrapperElement e : extensions) {
      if (urlSimple.equals(e.getNamedChildValue("url")))
        return e;
    }
    return null;
  }

  public List<String> getExtensionDomains() {
    return extensionDomains;
  }

  private WrapperElement getFromBundle(WrapperElement bundle, String ref) {
    List<WrapperElement> entries = new ArrayList<WrapperElement>();
    bundle.getNamedChildren("entry", entries);
    for (WrapperElement we : entries) {
      WrapperElement res = we.getNamedChild("resource").getFirstChild();
      if (res != null) {
        String url = genFullUrl(bundle.getNamedChildValue("base"), we.getNamedChildValue("base"), res.getName(), res.getNamedChildValue("id"));
        if (url.endsWith(ref))
          return res;
      }
    }
    return null;
  }  

  private StructureDefinition getProfileForType(String type) throws Exception {
    return context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + type);
  }

  private Element getValueForDiscriminator(WrapperElement element, String discriminator, ElementDefinition criteria) {
    // throw new Error("validation of slices not done yet");
    return null;
  }

  private ValueSet getValueSet(String system) throws Exception {
    return context.fetchCodeSystem(system);
  }

  private boolean hasTime(String fmt) {
    return fmt.contains("T");
  }

  private boolean hasTimeZone(String fmt) {
    return fmt.length() > 10 && (fmt.substring(10).contains("-") || fmt.substring(10).contains("+") || fmt.substring(10).contains("Z"));
  }

  private boolean isAbsolute(String uri) {
    return Utilities.noString(uri) || uri.startsWith("http:") || uri.startsWith("https:") || uri.startsWith("urn:uuid:") || uri.startsWith("urn:oid:") || uri.startsWith("urn:ietf:")
        || uri.startsWith("urn:iso:") || isValidFHIRUrn(uri);
  }

  private boolean isValidFHIRUrn(String uri) {
    return (uri.equals("urn:x-fhir:uk:id:nhs-number"));
  }

  public boolean isAnyExtensionsAllowed() {
    return anyExtensionsAllowed;
  }

  private boolean isParametersEntry(String path) {
    String[] parts = path.split("\\/");
    if (path.startsWith("/f:"))
      return parts.length == 4 && parts[parts.length-3].equals("f:Parameters") && parts[parts.length-2].startsWith("f:parameter") && parts[parts.length-1].startsWith("f:resource");
    else
      return parts.length == 4 && parts[parts.length-3].equals("Parameters") && parts[parts.length-2].startsWith("parameter") && parts[parts.length-1].startsWith("resource");
  }
  
  private boolean isBundleEntry(String path) {
    String[] parts = path.split("\\/");
    if (path.startsWith("/f:"))
      return parts.length > 2 && parts[parts.length - 1].startsWith("f:resource") && (parts[parts.length - 2].equals("f:entry") || parts[parts.length - 2].startsWith("f:entry["));
      else
        return parts.length > 2 && parts[parts.length - 1].equals("resource") && ((parts.length > 2 && parts[parts.length - 3].equals("entry")) || parts[parts.length - 2].equals("entry"));
  }

  private boolean isPrimitiveType(String type) {
    return type.equalsIgnoreCase("boolean") || type.equalsIgnoreCase("integer") || type.equalsIgnoreCase("string") || type.equalsIgnoreCase("decimal") || type.equalsIgnoreCase("uri")
        || type.equalsIgnoreCase("base64Binary") || type.equalsIgnoreCase("instant") || type.equalsIgnoreCase("date") || type.equalsIgnoreCase("uuid") || type.equalsIgnoreCase("id")
        || type.equalsIgnoreCase("xhtml") || type.equalsIgnoreCase("markdown") || type.equalsIgnoreCase("dateTime") || type.equalsIgnoreCase("time") || type.equalsIgnoreCase("code")
        || type.equalsIgnoreCase("oid") || type.equalsIgnoreCase("id");
  }

  public boolean isRequireResourceId() {
    return requiresResourceId;
  }

  public boolean isSuppressLoincSnomedMessages() {
    return suppressLoincSnomedMessages;
  }

  private boolean nameMatches(String name, String tail) {
    if (tail.endsWith("[x]"))
      return name.startsWith(tail.substring(0, tail.length() - 3));
    else
      return (name.equals(tail));
  }

  // private String mergePath(String path1, String path2) {
  // // path1 is xpath path
  // // path2 is dotted path
  // String[] parts = path2.split("\\.");
  // StringBuilder b = new StringBuilder(path1);
  // for (int i = 1; i < parts.length -1; i++)
  // b.append("/f:"+parts[i]);
  // return b.toString();
  // }

  private boolean passesCodeWhitespaceRules(String v) {
    if (!v.trim().equals(v))
      return false;
    boolean lastWasSpace = true;
    for (char c : v.toCharArray()) {
      if (c == ' ') {
        if (lastWasSpace)
          return false;
        else
          lastWasSpace = true;
      } else if (Character.isWhitespace(c))
        return false;
      else
        lastWasSpace = false;
    }
    return true;
  }

  private WrapperElement resolve(String ref, NodeStack stack) {
    if (ref.startsWith("#")) {
      // work back through the contained list.
      // really, there should only be one level for this (contained resources cannot contain
      // contained resources), but we'll leave that to some other code to worry about
      while (stack != null && stack.getElement() != null) {
        WrapperElement res = getContainedById(stack.getElement(), ref.substring(1));
        if (res != null)
          return res;
        stack = stack.parent;
      }
      return null;
    } else {
      // work back through the contained list - if any of them are bundles, try to resolve
      // the resource in the bundle
      while (stack != null && stack.getElement() != null) {
        if ("Bundle".equals(stack.getElement().getResourceType())) {
          WrapperElement res = getFromBundle(stack.getElement(), ref.substring(1));
          if (res != null)
            return res;
        }
        stack = stack.parent;
      }

      // todo: consult the external host for resolution
      return null;

    }
  }

  private ValueSet resolveBindingReference(Type reference) throws EOperationOutcome, Exception {
    if (reference instanceof UriType)
      return context.fetchResource(ValueSet.class, ((UriType) reference).getValue().toString());
    else if (reference instanceof Reference)
      return context.fetchResource(ValueSet.class, ((Reference) reference).getReference());
    else
      return null;
  }

  private WrapperElement resolveInBundle(List<WrapperElement> entries, String ref, String fullUrl, String type, String id) {
    if (Utilities.isAbsoluteUrl(ref)) {
      // if the reference is absolute, then you resolve by fullUrl. No other thinking is required. 
      for (WrapperElement entry : entries) {
        String fu = entry.getNamedChildValue("fullUrl");
        if (ref.equals(fu))
          return entry;
      }
      return null;
    } else {
      // split into base, type, and id
      String u = null;
      if (fullUrl != null && fullUrl.endsWith(type+"/"+id))
        // fullUrl = complex
        u = fullUrl.substring((type+"/"+id).length())+ref;
      String[] parts = ref.split("\\/");
      if (parts.length >= 2) {
        String t = parts[0];
        String i = parts[1];
        for (WrapperElement entry : entries) {
          String fu = entry.getNamedChildValue("fullUrl");
          if (u != null && fullUrl.equals(u))
            return entry;
          if (u == null) {
            WrapperElement res = entry.getNamedChild("resource");
            WrapperElement resource = res.getFirstChild();
            String et = resource.getResourceType();
            String eid = resource.getNamedChildValue("id");
            if (t.equals(et) && i.equals(eid))
              return entry;
          }
        }
      }
      return null;
    }
  }

  private ElementDefinition resolveNameReference(StructureDefinitionSnapshotComponent snapshot, String name) {
    for (ElementDefinition ed : snapshot.getElement())
      if (name.equals(ed.getName()))
        return ed;
    return null;
  }

  private StructureDefinition resolveProfile(StructureDefinition profile, String pr) throws EOperationOutcome, Exception {
    if (pr.startsWith("#")) {
      for (Resource r : profile.getContained()) {
        if (r.getId().equals(pr.substring(1)) && r instanceof StructureDefinition)
          return (StructureDefinition) r;
      }
      return null;
    } else
      return context.fetchResource(StructureDefinition.class, pr);
  }

  private ElementDefinition resolveType(String type) throws EOperationOutcome, Exception {
    String url = "http://hl7.org/fhir/StructureDefinition/" + type;
    StructureDefinition sd = context.fetchResource(StructureDefinition.class, url);
    if (sd == null || !sd.hasSnapshot())
      return null;
    else
      return sd.getSnapshot().getElement().get(0);
  }

  public void setAnyExtensionsAllowed(boolean anyExtensionsAllowed) {
    this.anyExtensionsAllowed = anyExtensionsAllowed;
  }

  public void setBestPracticeWarningLevel(BestPracticeWarningLevel value) {
    bpWarnings = value;
  }

  @Override
  public void setCheckDisplay(CheckDisplayOption checkDisplay) {
    this.checkDisplay = checkDisplay;
  }

  public void setRequireResourceId(boolean requiresResourceId) {
    this.requiresResourceId = requiresResourceId;
  }

  public void setSuppressLoincSnomedMessages(boolean suppressLoincSnomedMessages) {
    this.suppressLoincSnomedMessages = suppressLoincSnomedMessages;
  }

  /**
   * 
   * @param element
   *          - the candidate that might be in the slice
   * @param path
   *          - for reporting any errors. the XPath for the element
   * @param slice
   *          - the definition of how slicing is determined
   * @param ed
   *          - the slice for which to test membership
   * @return
   * @throws Exception
   */
  private boolean sliceMatches(WrapperElement element, String path, ElementDefinition slice, ElementDefinition ed, StructureDefinition profile) throws Exception {
    if (!slice.getSlicing().hasDiscriminator())
      return false; // cannot validate in this case
    for (StringType s : slice.getSlicing().getDiscriminator()) {
      String discriminator = s.getValue();
      ElementDefinition criteria = getCriteriaForDiscriminator(path, ed, discriminator, profile);
      if (discriminator.equals("url") && criteria.getPath().equals("Extension.url")) {
        if (!element.getAttribute("url").equals(((UriType) criteria.getFixed()).asStringValue()))
          return false;
      } else {
        Element value = getValueForDiscriminator(element, discriminator, criteria);
        if (!valueMatchesCriteria(value, criteria))
          return false;
      }
    }
    return true;
  }

  // we assume that the following things are true:
  // the instance at root is valid against the schema and schematron
  // the instance validator had no issues against the base resource profile
  private void start(List<ValidationMessage> errors, WrapperElement element, StructureDefinition profile, NodeStack stack) throws Exception {
    // profile is valid, and matches the resource name
    if (rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), profile.hasSnapshot(),
        "StructureDefinition has no snapshot - validation is against the snapshot, so it must be provided")) {
      validateElement(errors, profile, profile.getSnapshot().getElement().get(0), null, null, element, element.getName(), stack, false);

      checkDeclaredProfiles(errors, element, stack);

      // specific known special validations
      if (element.getResourceType().equals("Bundle"))
        validateBundle(errors, element, stack);
      if (element.getResourceType().equals("Observation"))
        validateObservation(errors, element, stack);
    }
  }

  private String tail(String path) {
    return path.substring(path.lastIndexOf(".") + 1);
  }

  private String tryParse(String ref) throws EOperationOutcome, Exception {
    String[] parts = ref.split("\\/");
    switch (parts.length) {
    case 1:
      return null;
    case 2:
      return checkResourceType(parts[0]);
    default:
      if (parts[parts.length - 2].equals("_history"))
        return checkResourceType(parts[parts.length - 4]);
      else
        return checkResourceType(parts[parts.length - 2]);
    }
  }

  private boolean typesAreAllReference(List<TypeRefComponent> theType) {
    for (TypeRefComponent typeRefComponent : theType) {
      if (typeRefComponent.getCode().equals("Reference") == false) {
        return false;
      }
    }
    return true;
  }

  @Override
  public List<ValidationMessage> validate(Document document) throws Exception {
    List<ValidationMessage> results = new ArrayList<ValidationMessage>();
    validate(results, document);
    return results;
  }

  @Override
  public List<ValidationMessage> validate(Document document, String profile) throws Exception {
    List<ValidationMessage> results = new ArrayList<ValidationMessage>();
    validate(results, document, profile);
    return results;
  }

  @Override
  public List<ValidationMessage> validate(Document document, StructureDefinition profile) throws Exception {
    List<ValidationMessage> results = new ArrayList<ValidationMessage>();
    validate(results, document, profile);
    return results;
  }       

  @Override
  public List<ValidationMessage> validate(Element element) throws Exception {
    List<ValidationMessage> results = new ArrayList<ValidationMessage>();
    validate(results, element);
    return results;
  }

  @Override
  public List<ValidationMessage> validate(Element element, String profile) throws Exception {
    List<ValidationMessage> results = new ArrayList<ValidationMessage>();
    validate(results, element, profile);
    return results;
  }

  @Override
  public List<ValidationMessage> validate(Element element, StructureDefinition profile) throws Exception {
    List<ValidationMessage> results = new ArrayList<ValidationMessage>();
    validate(results, element, profile);
    return results;
  }

  @Override
  public List<ValidationMessage> validate(JsonObject obj) throws Exception {
    List<ValidationMessage> results = new ArrayList<ValidationMessage>();
    validate(results, obj);
    return results;
  }

  @Override
  public List<ValidationMessage> validate(JsonObject obj, String profile) throws Exception {
    List<ValidationMessage> results = new ArrayList<ValidationMessage>();
    validate(results, obj, profile);
    return results;
  }

  @Override
  public List<ValidationMessage> validate(JsonObject obj, StructureDefinition profile) throws Exception {
    List<ValidationMessage> results = new ArrayList<ValidationMessage>();
    validate(results, obj, profile);
    return results;
  }

  @Override
  public void validate(List<ValidationMessage> errors, Document document) throws Exception {
    checkForProcessingInstruction(errors, document);
    validateResource(errors, new DOMWrapperElement(document.getDocumentElement()), null, requiresResourceId, null);
  } 

  @Override
  public void validate(List<ValidationMessage> errors, Document document, String profile) throws Exception {
    checkForProcessingInstruction(errors, document);
    StructureDefinition p = context.fetchResource(StructureDefinition.class, profile);
    if (p == null)
      throw new Exception("StructureDefinition '" + profile + "' not found");
    validateResource(errors, new DOMWrapperElement(document.getDocumentElement()), p, requiresResourceId, null);
  }

  @Override
  public void validate(List<ValidationMessage> errors, Document document, StructureDefinition profile) throws Exception {
    checkForProcessingInstruction(errors, document);
    validateResource(errors, new DOMWrapperElement(document.getDocumentElement()), profile, requiresResourceId, null);
  }

  @Override
  public void validate(List<ValidationMessage> errors, Element element) throws Exception {
    validateResource(errors, new DOMWrapperElement(element), null, requiresResourceId, null);
  }

  @Override
  public void validate(List<ValidationMessage> errors, Element element, String profile) throws Exception {
    StructureDefinition p = context.fetchResource(StructureDefinition.class, profile);
    if (p == null)
      throw new Exception("StructureDefinition '" + profile + "' not found");
    validateResource(errors, new DOMWrapperElement(element), p, requiresResourceId, null);
  }

  @Override
  public void validate(List<ValidationMessage> errors, Element element, StructureDefinition profile) throws Exception {
    validateResource(errors, new DOMWrapperElement(element), profile, requiresResourceId, null);
  }

  @Override
  public void validate(List<ValidationMessage> errors, JsonObject object) throws Exception {
    validateResource(errors, new JsonWrapperElement(object), null, requiresResourceId, null);
  }

  @Override
  public void validate(List<ValidationMessage> errors, JsonObject object, String profile) throws Exception {
    StructureDefinition p = context.fetchResource(StructureDefinition.class, profile);
    if (p == null)
      throw new Exception("StructureDefinition '" + profile + "' not found");
    validateResource(errors, new JsonWrapperElement(object), p, requiresResourceId, null);
  }

  @Override
  public void validate(List<ValidationMessage> errors, JsonObject object, StructureDefinition profile) throws Exception {
    validateResource(errors, new JsonWrapperElement(object), profile, requiresResourceId, null);
  }

  private void validateBundle(List<ValidationMessage> errors, WrapperElement bundle, NodeStack stack) {
    List<WrapperElement> entries = new ArrayList<WrapperElement>();
    bundle.getNamedChildren("entry", entries);
    String type = bundle.getNamedChildValue("type");
    if (entries.size() == 0) {
      rule(errors, IssueType.INVALID, stack.getLiteralPath(), !(type.equals("document") || type.equals("message")), "Documents or Messages must contain at least one entry");
    } else {
      WrapperElement firstEntry = entries.get(0);
      NodeStack firstStack = stack.push(firstEntry, 0, null, null);
      String fullUrl = firstEntry.getNamedChildValue("fullUrl");

      if (type.equals("document")) {
        WrapperElement res = firstEntry.getNamedChild("resource");
        NodeStack localStack = firstStack.push(res, -1, null, null);
        WrapperElement resource = res.getFirstChild();
        String id = resource.getNamedChildValue("id");
        if (rule(errors, IssueType.INVALID, firstEntry.line(), firstEntry.col(), stack.addToLiteralPath("entry", ":0"), res != null, "No resource on first entry")) {
          if (bundle.isXml())
            validateDocument(errors, entries, resource, localStack.push(resource, -1, null, null), fullUrl, id);
          else
            validateDocument(errors, entries, res, localStack, fullUrl, id);
        }
      }
      if (type.equals("message"))
        validateMessage(errors, bundle);
    }
  }

  // public class ProfileStructureIterator {
  //
  // private StructureDefinition profile;
  // private ElementDefinition elementDefn;
  // private List<String> names = new ArrayList<String>();
  // private Map<String, List<ElementDefinition>> children = new HashMap<String, List<ElementDefinition>>();
  // private int cursor;
  //
  // public ProfileStructureIterator(StructureDefinition profile, ElementDefinition elementDefn) {
  // this.profile = profile;
  // this.elementDefn = elementDefn;
  // loadMap();
  // cursor = -1;
  // }
  //
  // private void loadMap() {
  // int i = profile.getSnapshot().getElement().indexOf(elementDefn) + 1;
  // String lead = elementDefn.getPath();
  // while (i < profile.getSnapshot().getElement().size()) {
  // String name = profile.getSnapshot().getElement().get(i).getPath();
  // if (name.length() <= lead.length())
  // return; // cause we've got to the end of the possible matches
  // String tail = name.substring(lead.length()+1);
  // if (Utilities.isToken(tail) && name.substring(0, lead.length()).equals(lead)) {
  // List<ElementDefinition> list = children.get(tail);
  // if (list == null) {
  // list = new ArrayList<ElementDefinition>();
  // names.add(tail);
  // children.put(tail, list);
  // }
  // list.add(profile.getSnapshot().getElement().get(i));
  // }
  // i++;
  // }
  // }
  //
  // public boolean more() {
  // cursor++;
  // return cursor < names.size();
  // }
  //
  // public List<ElementDefinition> current() {
  // return children.get(name());
  // }
  //
  // public String name() {
  // return names.get(cursor);
  // }
  //
  // }
  //
  // private void checkByProfile(List<ValidationMessage> errors, String path, WrapperElement focus, StructureDefinition profile, ElementDefinition elementDefn)
  // throws Exception {
  // // we have an element, and the structure that describes it.
  // // we know that's it's valid against the underlying spec - is it valid against this one?
  // // in the instance validator above, we assume that schema or schmeatron has taken care of cardinalities, but here, we have no such reliance.
  // // so the walking algorithm is different: we're going to walk the definitions
  // String type;
  // if (elementDefn.getPath().endsWith("[x]")) {
  // String tail = elementDefn.getPath().substring(elementDefn.getPath().lastIndexOf(".")+1, elementDefn.getPath().length()-3);
  // type = focus.getName().substring(tail.length());
  // rule(errors, IssueType.STRUCTURE, focus.line(), focus.col(), path, typeAllowed(type, elementDefn.getType()), "The type '"+type+"' is not allowed at this
  // point (must be one of '"+typeSummary(elementDefn)+")");
  // } else {
  // if (elementDefn.getType().size() == 1) {
  // type = elementDefn.getType().size() == 0 ? null : elementDefn.getType().get(0).getCode();
  // } else
  // type = null;
  // }
  // // constraints:
  // for (ElementDefinitionConstraintComponent c : elementDefn.getConstraint())
  // checkConstraint(errors, path, focus, c);
  // if (elementDefn.hasBinding() && type != null)
  // checkBinding(errors, path, focus, profile, elementDefn, type);
  //
  // // type specific checking:
  // if (type != null && isPrimitiveType(type)) {
  // checkPrimitiveByProfile(errors, path, focus, elementDefn);
  // } else {
  // if (elementDefn.hasFixed())
  // checkFixedValue(errors, path, focus, elementDefn.getFixed(), "");
  //
  // ProfileStructureIterator walker = new ProfileStructureIterator(profile, elementDefn);
  // while (walker.more()) {
  // // collect all the slices for the path
  // List<ElementDefinition> childset = walker.current();
  // // collect all the elements that match it by name
  // List<WrapperElement> children = new ArrayList<WrapperElement>();
  // focus.getNamedChildrenWithWildcard(walker.name(), children);
  //
  // if (children.size() == 0) {
  // // well, there's no children - should there be?
  // for (ElementDefinition defn : childset) {
  // if (!rule(errors, IssueType.REQUIRED, focus.line(), focus.col(), path, defn.getMin() == 0, "Required Element '"+walker.name()+"' missing"))
  // break; // no point complaining about missing ones after the first one
  // }
  // } else if (childset.size() == 1) {
  // // simple case: one possible definition, and one or more children.
  // rule(errors, IssueType.STRUCTURE, focus.line(), focus.col(), path, childset.get(0).getMax().equals("*") || Integer.parseInt(childset.get(0).getMax()) >=
  // children.size(),
  // "Too many elements for '"+walker.name()+"'"); // todo: sort out structure
  // for (WrapperElement child : children) {
  // checkByProfile(errors, childset.get(0).getPath(), child, profile, childset.get(0));
  // }
  // } else {
  // // ok, this is the full case - we have a list of definitions, and a list of candidates for meeting those definitions.
  // // we need to decide *if* that match a given definition
  // }
  // }
  // }
  // }

  // private void checkBinding(List<ValidationMessage> errors, String path, WrapperElement focus, StructureDefinition profile, ElementDefinition elementDefn,
  // String type) throws EOperationOutcome, Exception {
  // ElementDefinitionBindingComponent bc = elementDefn.getBinding();
  //
  // if (bc != null && bc.hasValueSet() && bc.getValueSet() instanceof Reference) {
  // String url = ((Reference) bc.getValueSet()).getReference();
  // ValueSet vs = resolveValueSetReference(profile, (Reference) bc.getValueSet());
  // if (vs == null) {
  // rule(errors, IssueType.STRUCTURE, focus.line(), focus.col(), path, false, "Cannot check binding on type '"+type+"' as the value set '"+url+"' could not be
  // located");
  // } else if (type.equals("code"))
  // checkBindingCode(errors, path, focus, vs);
  // else if (type.equals("Coding"))
  // checkBindingCoding(errors, path, focus, vs);
  // else if (type.equals("CodeableConcept"))
  // checkBindingCodeableConcept(errors, path, focus, vs);
  // else
  // rule(errors, IssueType.STRUCTURE, focus.line(), focus.col(), path, false, "Cannot check binding on type '"+type+"'");
  // }
  // }
  //
  // private ValueSet resolveValueSetReference(StructureDefinition profile, Reference reference) throws EOperationOutcome, Exception {
  // if (reference.getReference().startsWith("#")) {
  // for (Resource r : profile.getContained()) {
  // if (r instanceof ValueSet && r.getId().equals(reference.getReference().substring(1)))
  // return (ValueSet) r;
  // }
  // return null;
  // } else
  // return resolveBindingReference(reference);
  //
  // }
  //
  // private void checkBindingCode(List<ValidationMessage> errors, String path, WrapperElement focus, ValueSet vs) {
  // // rule(errors, "exception", path, false, "checkBindingCode not done yet");
  // }
  //
  // private void checkBindingCoding(List<ValidationMessage> errors, String path, WrapperElement focus, ValueSet vs) {
  // // rule(errors, "exception", path, false, "checkBindingCoding not done yet");
  // }
  //
  // private void checkBindingCodeableConcept(List<ValidationMessage> errors, String path, WrapperElement focus, ValueSet vs) {
  // // rule(errors, "exception", path, false, "checkBindingCodeableConcept not done yet");
  // }
  //
  // private String typeSummary(ElementDefinition elementDefn) {
  // StringBuilder b = new StringBuilder();
  // for (TypeRefComponent t : elementDefn.getType()) {
  // b.append("|"+t.getCode());
  // }
  // return b.toString().substring(1);
  // }
  //
  // private boolean typeAllowed(String t, List<TypeRefComponent> types) {
  // for (TypeRefComponent type : types) {
  // if (t.equals(Utilities.capitalize(type.getCode())))
  // return true;
  // if (t.equals("Resource") && Utilities.capitalize(type.getCode()).equals("Reference"))
  // return true;
  // }
  // return false;
  // }
  //
  // private void checkConstraint(List<ValidationMessage> errors, String path, WrapperElement focus, ElementDefinitionConstraintComponent c) throws Exception {
  //
  //// try
  //// {
  //// XPathFactory xpf = new net.sf.saxon.xpath.XPathFactoryImpl();
  //// NamespaceContext context = new NamespaceContextMap("f", "http://hl7.org/fhir", "h", "http://www.w3.org/1999/xhtml");
  ////
  //// XPath xpath = xpf.newXPath();
  //// xpath.setNamespaceContext(context);
  //// Boolean ok = (Boolean) xpath.evaluate(c.getXpath(), focus, XPathConstants.BOOLEAN);
  //// if (ok == null || !ok) {
  //// if (c.getSeverity() == ConstraintSeverity.warning)
  //// warning(errors, "invariant", path, false, c.getHuman());
  //// else
  //// rule(errors, "invariant", path, false, c.getHuman());
  //// }
  //// }
  //// catch (XPathExpressionException e) {
  //// rule(errors, "invariant", path, false, "error executing invariant: "+e.getMessage());
  //// }
  // }
  //
  // private void checkPrimitiveByProfile(List<ValidationMessage> errors, String path, WrapperElement focus, ElementDefinition elementDefn) {
  // // two things to check - length, and fixed value
  // String value = focus.getAttribute("value");
  // if (elementDefn.hasMaxLengthElement()) {
  // rule(errors, IssueType.TOOLONG, focus.line(), focus.col(), path, value.length() <= elementDefn.getMaxLength(), "The value '"+value+"' exceeds the allow
  // length limit of "+Integer.toString(elementDefn.getMaxLength()));
  // }
  // if (elementDefn.hasFixed()) {
  // checkFixedValue(errors, path, focus, elementDefn.getFixed(), "");
  // }
  // }

  private void validateBundleReference(List<ValidationMessage> errors, List<WrapperElement> entries, WrapperElement ref, String name, NodeStack stack, String fullUrl, String type, String id) {
    if (ref != null && !Utilities.noString(ref.getNamedChildValue("reference"))) {
      WrapperElement target = resolveInBundle(entries, ref.getNamedChildValue("reference"), fullUrl, type, id);
      rule(errors, IssueType.INVALID, target.line(), target.col(), stack.addToLiteralPath("reference"), target != null, "Unable to resolve the target of the reference in the bundle (" + name + ")");
    }
  }

  private void validateContains(List<ValidationMessage> errors, String path, ElementDefinition child, ElementDefinition context, WrapperElement element, NodeStack stack, boolean needsId)
      throws Exception {
    WrapperElement e = element.isXml() ? element.getFirstChild() : element;
    String resourceName = e.getResourceType();
    StructureDefinition profile = this.context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + resourceName);
    if (rule(errors, IssueType.INVALID, element.line(), element.col(), stack.addToLiteralPath(resourceName), profile != null, "No profile found for contained resource of type '" + resourceName + "'"))
      validateResource(errors, e, profile, needsId, stack);
  }

  private void validateDocument(List<ValidationMessage> errors, List<WrapperElement> entries, WrapperElement composition, NodeStack stack, String fullUrl, String id) {
    // first entry must be a composition
    if (rule(errors, IssueType.INVALID, composition.line(), composition.col(), stack.getLiteralPath(), composition.getResourceType().equals("Composition"),
        "The first entry in a document must be a composition")) {
      // the composition subject and section references must resolve in the bundle
      validateBundleReference(errors, entries, composition.getNamedChild("subject"), "Composition Subject", stack.push(composition.getNamedChild("subject"), -1, null, null), fullUrl, "Composition",
          id);
      validateSections(errors, entries, composition, stack, fullUrl, id);
    }
  }
  // rule(errors, IssueType.INVALID, bundle.line(), bundle.col(), "Bundle", !"urn:guid:".equals(base), "The base 'urn:guid:' is not valid (use urn:uuid:)");
  // rule(errors, IssueType.INVALID, entry.line(), entry.col(), localStack.getLiteralPath(), !"urn:guid:".equals(ebase), "The base 'urn:guid:' is not valid");
  // rule(errors, IssueType.INVALID, entry.line(), entry.col(), localStack.getLiteralPath(), !Utilities.noString(base) || !Utilities.noString(ebase), "entry
  // does not have a base");
  // String firstBase = null;
  // firstBase = ebase == null ? base : ebase;

  private void validateElement(List<ValidationMessage> errors, StructureDefinition profile, ElementDefinition definition, StructureDefinition cprofile, ElementDefinition context,
      WrapperElement element, String actualType, NodeStack stack, boolean inCodeableConcept) throws Exception {
    // irrespective of what element it is, it cannot be empty
    if (element.isXml()) {
      rule(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), FormatUtilities.FHIR_NS.equals(element.getNamespace()),
          "Namespace mismatch - expected '" + FormatUtilities.FHIR_NS + "', found '" + element.getNamespace() + "'");
      rule(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), !element.hasNamespace("http://www.w3.org/2001/XMLSchema-instance"),
          "Schema Instance Namespace is not allowed in instances");
      rule(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), !element.hasProcessingInstruction(), "No Processing Instructions in resources");
    }
    rule(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), !empty(element), "Elements must have some content (@value, extensions, or children elements)");

    // get the list of direct defined children, including slices
    List<ElementDefinition> childDefinitions = ProfileUtilities.getChildMap(profile, definition.getName(), definition.getPath(), definition.getNameReference());

    // 1. List the children, and remember their exact path (convenience)
    List<ElementInfo> children = new ArrayList<InstanceValidator.ElementInfo>();
    ChildIterator iter = new ChildIterator(stack.getLiteralPath(), element);
    while (iter.next())
      children.add(new ElementInfo(iter.name(), iter.element(), iter.path(), iter.count()));

    // 2. assign children to a definition
    // for each definition, for each child, check whether it belongs in the slice
    ElementDefinition slice = null;
    for (ElementDefinition ed : childDefinitions) {
      boolean process = true;
      // where are we with slicing
      if (ed.hasSlicing()) {
        if (slice != null && slice.getPath().equals(ed.getPath()))
          throw new Exception("Slice encountered midway through path on " + slice.getPath());
        slice = ed;
        process = false;
      } else if (slice != null && !slice.getPath().equals(ed.getPath()))
        slice = null;

      if (process) {
        for (ElementInfo ei : children) {
          boolean match = false;
          if (slice == null) {
            match = nameMatches(ei.name, tail(ed.getPath()));
          } else {
            if (nameMatches(ei.name, tail(ed.getPath())))
              match = sliceMatches(ei.element, ei.path, slice, ed, profile);
          }
          if (match) {
            if (rule(errors, IssueType.INVALID, ei.line(), ei.col(), ei.path, ei.definition == null, "Element matches more than one slice"))
              ei.definition = ed;
          }
        }
      }
    }
    for (ElementInfo ei : children)
      if (ei.path.endsWith(".extension"))
        rule(errors, IssueType.INVALID, ei.line(), ei.col(), ei.path, ei.definition != null, "Element is unknown or does not match any slice (url=\"" + ei.element.getAttribute("url") + "\")");
      else
        rule(errors, IssueType.INVALID, ei.line(), ei.col(), ei.path, (ei.definition != null) || (!ei.element.isXml() && ei.element.getName().equals("fhir_comments")),
            "Element is unknown or does not match any slice");

    // 3. report any definitions that have a cardinality problem
    for (ElementDefinition ed : childDefinitions) {
      if (ed.getRepresentation().isEmpty()) { // ignore xml attributes
        int count = 0;
        for (ElementInfo ei : children)
          if (ei.definition == ed)
            count++;
        if (ed.getMin() > 0) {
          rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), count >= ed.getMin(),
              "Element '" + stack.getLiteralPath() + "." + tail(ed.getPath()) + "': minimum required = " + Integer.toString(ed.getMin()) + ", but only found " + Integer.toString(count));
        }
        if (ed.hasMax() && !ed.getMax().equals("*")) {
          rule(errors, IssueType.STRUCTURE, element.line(), element.col(), stack.getLiteralPath(), count <= Integer.parseInt(ed.getMax()),
              "Element " + tail(ed.getPath()) + " @ " + stack.getLiteralPath() + ": max allowed = " + ed.getMax() + ", but found " + Integer.toString(count));
        }

      }
    }
    // 4. check order if any slices are orderd. (todo)

    // 5. inspect each child for validity
    for (ElementInfo ei : children) {
      if (ei.definition != null) {
        String type = null;
        ElementDefinition typeDefn = null;
        if (ei.definition.getType().size() == 1 && !ei.definition.getType().get(0).getCode().equals("*") && !ei.definition.getType().get(0).getCode().equals("Element")
            && !ei.definition.getType().get(0).getCode().equals("BackboneElement"))
          type = ei.definition.getType().get(0).getCode();
        else if (ei.definition.getType().size() == 1 && ei.definition.getType().get(0).getCode().equals("*")) {
          String prefix = tail(ei.definition.getPath());
          assert prefix.endsWith("[x]");
          type = ei.name.substring(prefix.length() - 3);
          if (isPrimitiveType(type))
            type = Utilities.uncapitalize(type);
        } else if (ei.definition.getType().size() > 1) {

          String prefix = tail(ei.definition.getPath());
          assert typesAreAllReference(ei.definition.getType()) || prefix.endsWith("[x]") : prefix;

          prefix = prefix.substring(0, prefix.length() - 3);
          for (TypeRefComponent t : ei.definition.getType())
            if ((prefix + Utilities.capitalize(t.getCode())).equals(ei.name))
              type = t.getCode();
          if (type == null) {
            TypeRefComponent trc = ei.definition.getType().get(0);
            if (trc.getCode().equals("Reference"))
              type = "Reference";
            else 
              rule(errors, IssueType.STRUCTURE, ei.line(), ei.col(), stack.getLiteralPath(), false,
                  "The element " + ei.name + " is illegal. Valid types at this point are " + describeTypes(ei.definition.getType()));
          }
        } else if (ei.definition.getNameReference() != null) {
          typeDefn = resolveNameReference(profile.getSnapshot(), ei.definition.getNameReference());
        }

        if (type != null) {
          if (type.startsWith("@")) {
            ei.definition = findElement(profile, type.substring(1));
            type = null;
          }
        }
        NodeStack localStack = stack.push(ei.element, ei.count, ei.definition, type == null ? typeDefn : resolveType(type));
        String localStackLiterapPath = localStack.getLiteralPath();
        String eiPath = ei.path;
        assert(eiPath.equals(localStackLiterapPath)) : "ei.path: " + ei.path + "  -  localStack.getLiterapPath: " + localStackLiterapPath;
        boolean thisIsCodeableConcept = false;

        if (type != null) {
          if (isPrimitiveType(type))
            checkPrimitive(errors, ei.path, type, ei.definition, ei.element);
          else {
            if (type.equals("Identifier"))
              checkIdentifier(errors, ei.path, ei.element, ei.definition);
            else if (type.equals("Coding"))
              checkCoding(errors, ei.path, ei.element, profile, ei.definition, inCodeableConcept);
            else if (type.equals("CodeableConcept")) {
              checkCodeableConcept(errors, ei.path, ei.element, profile, ei.definition);
              thisIsCodeableConcept = true;
            } else if (type.equals("Reference"))
              checkReference(errors, ei.path, ei.element, profile, ei.definition, actualType, localStack);
            
            if (type.equals("Extension"))
              checkExtension(errors, ei.path, ei.element, ei.definition, profile, localStack);
            else if (type.equals("Resource"))
              validateContains(errors, ei.path, ei.definition, definition, ei.element, localStack, !isBundleEntry(ei.path)  && !isParametersEntry(ei.path)); // if
            // (str.matches(".*([.,/])work\\1$"))
            else {
              StructureDefinition p = getProfileForType(type);
              if (rule(errors, IssueType.STRUCTURE, ei.line(), ei.col(), ei.path, p != null, "Unknown type " + type)) {
                validateElement(errors, p, p.getSnapshot().getElement().get(0), profile, ei.definition, ei.element, type, localStack, thisIsCodeableConcept);
              }
            }
          }
        } else {
          if (rule(errors, IssueType.STRUCTURE, ei.line(), ei.col(), stack.getLiteralPath(), ei.definition != null, "Unrecognised Content " + ei.name))
            validateElement(errors, profile, ei.definition, null, null, ei.element, type, localStack, false);
        }
      }
    }
  }

  private void validateMessage(List<ValidationMessage> errors, WrapperElement bundle) {
    // TODO Auto-generated method stub

  }

  private void validateObservation(List<ValidationMessage> errors, WrapperElement element, NodeStack stack) {
    // all observations should have a subject, a performer, and a time

    bpCheck(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), element.getNamedChild("subject") != null, "All observations should have a subject");
    bpCheck(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), element.getNamedChild("performer") != null, "All observations should have a performer");
    bpCheck(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), element.getNamedChild("effectiveDateTime") != null || element.getNamedChild("effectivePeriod") != null,
        "All observations should have an effectiveDateTime or an effectivePeriod");
  }

  /*
   * The actual base entry point
   */
  private void validateResource(List<ValidationMessage> errors, WrapperElement element, StructureDefinition profile, boolean needsId, NodeStack stack) throws Exception {
    if (stack == null)
      stack = new NodeStack(element.isXml());

    // getting going - either we got a profile, or not.
    boolean ok = true;
    if (element.isXml()) {
      ok = rule(errors, IssueType.INVALID, element.line(), element.col(), "/", element.getNamespace().equals(FormatUtilities.FHIR_NS),
          "Namespace mismatch - expected '" + FormatUtilities.FHIR_NS + "', found '" + element.getNamespace() + "'");
    }
    if (ok) {
      String resourceName = element.getResourceType();
      if (profile == null) {
        profile = context.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/" + resourceName);
        ok = rule(errors, IssueType.INVALID, element.line(), element.col(), stack.addToLiteralPath(resourceName), profile != null, "No profile found for resource type '" + resourceName + "'");
      } else {
        String type = profile.hasConstrainedType() ? profile.getConstrainedType() : profile.getName();
        // special case: we have a bundle, and the profile is not for a bundle. We'll try the first entry instead 
        if (!type.equals(resourceName) && resourceName.equals("Bundle")) {
          WrapperElement first = getFirstEntry(element);
          if (first != null && first.getResourceType().equals(type)) {
            element = first;
            resourceName = element.getResourceType();
            needsId = false;
          }
        }
        ok = rule(errors, IssueType.INVALID, -1, -1, stack.addToLiteralPath(resourceName), type.equals(resourceName),
            "Specified profile type was '" + profile.getConstrainedType() + "', but resource type was '" + resourceName + "'");
      }
    }

    if (ok) {
      stack = stack.push(element, -1, profile.getSnapshot().getElement().get(0), profile.getSnapshot().getElement().get(0));
      if (needsId && (element.getNamedChild("id") == null))
        rule(errors, IssueType.INVALID, element.line(), element.col(), stack.getLiteralPath(), false, "Resource has no id");
      start(errors, element, profile, stack); // root is both definition and type
    }
  }

  private WrapperElement getFirstEntry(WrapperElement bundle) {
    List<WrapperElement> list = new ArrayList<WrapperElement>();
    bundle.getNamedChildren("entry", list);
    if (list.isEmpty())
      return null;
    WrapperElement resource = list.get(0).getNamedChild("resource");
    if (resource == null)
      return null;
    else
      return resource.getFirstChild();
  }

  private void validateSections(List<ValidationMessage> errors, List<WrapperElement> entries, WrapperElement focus, NodeStack stack, String fullUrl, String id) {
    List<WrapperElement> sections = new ArrayList<WrapperElement>();
    focus.getNamedChildren("entry", sections);
    int i = 0;
    for (WrapperElement section : sections) {
      NodeStack localStack = stack.push(section, 1, null, null);
      validateBundleReference(errors, entries, section.getNamedChild("content"), "Section Content", localStack, fullUrl, "Composition", id);
      validateSections(errors, entries, section, localStack, fullUrl, id);
      i++;
    }
  }

  private boolean valueMatchesCriteria(Element value, ElementDefinition criteria) {
    // throw new Error("validation of slices not done yet");
    return false;
  }

  private boolean yearIsValid(String v) {
    if (v == null) {
      return false;
    }
    try {
      int i = Integer.parseInt(v.substring(0, Math.min(4, v.length())));
      return i >= 1800 && i <= 2100;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  public class ChildIterator {
    private String basePath;
    private WrapperElement child;
    private int lastCount;
    private WrapperElement parent;

    public ChildIterator(String path, WrapperElement element) {
      parent = element;
      basePath = path;
    }

    public int count() {
      WrapperElement n = child.getNextSibling();
      if (n != null && n.getName().equals(child.getName())) {
        return lastCount + 1;
      } else
        return -1;
    }

    public WrapperElement element() {
      return child;
    }

    public String name() {
      return child.getName();
    }

    public boolean next() {
      if (child == null) {
        child = parent.getFirstChild();
        lastCount = 0;
      } else {
        String lastName = child.getName();
        child = child.getNextSibling();
        if (child != null && child.getName().equals(lastName))
          lastCount++;
        else
          lastCount = 0;
      }
      return child != null;
    }

    public String path() {
      WrapperElement n = child.getNextSibling();
      if (parent.isXml()) {
        String sfx = "";
        if (n != null && n.getName().equals(child.getName())) {
          sfx = "[" + Integer.toString(lastCount + 1) + "]";
        }
        if (FormatUtilities.XHTML_NS.equals(child.getNamespace()))
          return basePath + "/h:" + name() + sfx;
        else
          return basePath + "/f:" + name() + sfx;
      } else {
        String sfx = "";
        if (n != null && n.getName().equals(child.getName())) {
          sfx = "/" + Integer.toString(lastCount + 1);
        }
        return basePath + "/" + name() + sfx;
      }
    }
  }

  public class DOMWrapperElement extends WrapperElement {

    private int col;
    private Element element;
    private int line;

    public DOMWrapperElement(Element element) {
      super();
      this.element = element;
      XmlLocationData loc = (XmlLocationData) element.getUserData(XmlLocationData.LOCATION_DATA_KEY);
      if (loc != null) {
        line = loc.getStartLine();
        col = loc.getStartColumn();
      } else {
        line = -1;
        col = -1;
      }
    }

    @Override
    public int col() {
      return col;
    }

    @Override
    public String getAttribute(String name) {
      return element.getAttribute(name);
    }

    @Override
    public WrapperElement getFirstChild() {
      Element res = XMLUtil.getFirstChild(element);
      return res == null ? null : new DOMWrapperElement(res);
    }

    @Override
    public String getName() {
      return element.getLocalName();
    }

    @Override
    public WrapperElement getNamedChild(String name) {
      Element res = XMLUtil.getNamedChild(element, name);
      return res == null ? null : new DOMWrapperElement(res);
    }

    @Override
    public void getNamedChildren(String name, List<WrapperElement> list) {
      List<Element> el = new ArrayList<Element>();
      XMLUtil.getNamedChildren(element, name, el);
      for (Element e : el)
        list.add(new DOMWrapperElement(e));
    }

    @Override
    public void getNamedChildrenWithWildcard(String name, List<WrapperElement> list) {
      List<Element> el = new ArrayList<Element>();
      XMLUtil.getNamedChildrenWithWildcard(element, name, el);
      for (Element e : el)
        list.add(new DOMWrapperElement(e));
    }

    @Override
    public String getNamedChildValue(String name) {
      return XMLUtil.getNamedChildValue(element, name);
    }

    @Override
    public String getNamespace() {
      return element.getNamespaceURI();
    }

    @Override
    public WrapperElement getNextSibling() {
      Element res = XMLUtil.getNextSibling(element);
      return res == null ? null : new DOMWrapperElement(res);
    }

    @Override
    public String getResourceType() {
      return element.getLocalName();
    }

    @Override
    public String getText() {
      return element.getTextContent();
    }

    @Override
    public boolean hasAttribute(String name) {
      return element.hasAttribute(name);
    }

    @Override
    public boolean hasNamespace(String ns) {
      for (int i = 0; i < element.getAttributes().getLength(); i++) {
        Node a = element.getAttributes().item(i);
        if ((a.getNodeName().equals("xmlns") || a.getNodeName().startsWith("xmlns:")) && a.getNodeValue().equals(ns))
          return true;
      }
      return false;
    }

    @Override
    public boolean hasProcessingInstruction() {
      Node node = element.getFirstChild();
      while (node != null) {
        if (node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE)
          return true;
        node = node.getNextSibling();
      }
      return false;
    }

    @Override
    public boolean isXml() {
      return true;
    }

    @Override
    public int line() {
      return line;
    }
  }

  public class ElementInfo {

    public int count;
    public ElementDefinition definition;
    private WrapperElement element;
    private String name;
    private String path;

    public ElementInfo(String name, WrapperElement element, String path, int count) {
      this.name = name;
      this.element = element;
      this.path = path;
      this.count = count;
    }

    public int col() {
      return element.col();
    }

    public int line() {
      return element.line();
    }

  }

  public class JsonWrapperElement extends WrapperElement {

    private JsonElement _element;
    private List<JsonWrapperElement> children = new ArrayList<JsonWrapperElement>();
    private JsonElement element;
    private int index;
    private String name;
    private JsonWrapperElement parent;
    private String path;
    private String resourceType;

    public JsonWrapperElement(JsonElement element) {
      super();
      this.name = null;
      this.resourceType = ((JsonObject) element).get("resourceType").getAsString();
      this.element = element;
      this.path = "";
      createChildren();
    }

    public JsonWrapperElement(String path, String name, JsonElement element, JsonElement _element, JsonWrapperElement parent, int index) {
      super();
      this.path = path + "/" + name;
      this.name = name;
      this.element = element;
      if (element instanceof JsonObject && ((JsonObject) element).has("resourceType"))
        this.resourceType = ((JsonObject) element).get("resourceType").getAsString();
      this._element = _element;
      this.parent = parent;
      this.index = index;
      createChildren();
    }

    @Override
    public int col() {
      // TODO Auto-generated method stub
      return -1;
    }

    private void createChildren() {
      // System.out.println(" ..: "+path);
      // we're going to make this look like the XML
      if (element != null) {
        if (element instanceof JsonPrimitive) {
          // we may have an element_ too
          if (_element != null && _element instanceof JsonObject)
            for (Entry<String, JsonElement> t : ((JsonObject) _element).entrySet())
              processChild(t.getKey(), t.getValue());
        } else if (element instanceof JsonObject) {
          for (Entry<String, JsonElement> t : ((JsonObject) element).entrySet())
            if (!t.getKey().equals("resourceType")) {
              processChild(t.getKey(), t.getValue());
            }
        } else if (element instanceof JsonNull) {
          // nothing to do
        } else
          throw new Error("unexpected condition: " + element.getClass().getName());
      }
      if (_element != null) {

      }
    }

    @Override
    public String getAttribute(String name) {
      if (name.equals("value")) {
        if (element == null)
          return null;
        if (element instanceof JsonPrimitive)
          return ((JsonPrimitive) element).getAsString();
        return null;
      }
      if (name.equals("xml:id")) {
        WrapperElement c = getNamedChild("id");
        return c == null ? null : c.getAttribute("value");
      }
      if (name.equals("url")) {
        WrapperElement c = getNamedChild("url");
        return c == null ? null : c.getAttribute("value");
      }
      throw new Error("not done yet: " + name);
    }

    @Override
    public WrapperElement getFirstChild() {
      if (children.isEmpty())
        return null;
      else
        return children.get(0);
    }

    @Override
    public String getName() {
      return name;
    }

    @Override
    public WrapperElement getNamedChild(String name) {
      for (JsonWrapperElement j : children)
        if (j.name.equals(name))
          return j;
      return null;
    }

    @Override
    public void getNamedChildren(String name, List<WrapperElement> list) {
      for (JsonWrapperElement j : children)
        if (j.name.equals(name))
          list.add(j);
    }

    @Override
    public void getNamedChildrenWithWildcard(String name, List<WrapperElement> list) {
      throw new Error("not done yet");
    }

    @Override
    public String getNamedChildValue(String name) {
      WrapperElement c = getNamedChild(name);
      return c == null ? null : c.getAttribute("value");
    }

    @Override
    public String getNamespace() {
      // return element.getNamespaceURI();
      throw new Error("not done yet");
    }

    @Override
    public WrapperElement getNextSibling() {
      if (parent == null)
        return null;
      if (index >= parent.children.size() - 1)
        return null;
      return parent.children.get(index + 1);
    }

    @Override
    public String getResourceType() {
      return resourceType;
    }

    @Override
    public String getText() {
      throw new Error("not done yet");
    }

    @Override
    public boolean hasAttribute(String name) {
      if (name.equals("value")) {
        if (element == null)
          return false;
        if (element instanceof JsonPrimitive)
          return true;
        return false;
      }
      if (name.equals("xml:id")) {
        return getNamedChild("id") != null;
      }
      throw new Error("not done yet: " + name);
    }

    @Override
    public boolean hasNamespace(String ns) {
      throw new Error("not done");
    }

    @Override
    public boolean hasProcessingInstruction() {
      return false;
    }

    @Override
    public boolean isXml() {
      return false;
    }	  

    @Override
    public int line() {
      return -1;
    }

    private void processChild(String name, JsonElement e) throws Error {
      if (name.startsWith("_")) {
        name = name.substring(1);
        if (((JsonObject) element).has(name))
          return; // it will get processed anyway
        e = null;
      }
      JsonElement _e = element instanceof JsonObject ? ((JsonObject) element).get("_" + name) : null;

      if (e instanceof JsonPrimitive || (e == null && _e != null && !(_e instanceof JsonArray))) {
        children.add(new JsonWrapperElement(path, name, e, _e, this, children.size()));
      } else if (e instanceof JsonArray || (e == null && _e != null)) {
        JsonArray array = (JsonArray) e;
        JsonArray _array = (JsonArray) _e;
        int max = array != null ? array.size() : 0;
        if (_array != null && _array.size() > max)
          max = _array.size();
        for (int i = 0; i < max; i++) {
          JsonElement a = array == null || array.size() < i ? null : array.get(i);
          JsonElement _a = _array == null || _array.size() < i ? null : _array.get(i);
          children.add(new JsonWrapperElement(path, name, a, _a, this, children.size()));
        }
      } else if (e instanceof JsonObject) {
        children.add(new JsonWrapperElement(path, name, e, null, this, children.size()));
      } else
        throw new Error("not done yet: " + e.getClass().getName());
    }

  }

  private class NodeStack {
    private ElementDefinition definition;
    private WrapperElement element;
    private ElementDefinition extension;
    private String literalPath; // xpath format
    private List<String> logicalPaths; // dotted format, various entry points
    private NodeStack parent;
    private ElementDefinition type;
    private boolean xml;

    public NodeStack(boolean xml) {
      this.xml = xml;
    }	  

    public String addToLiteralPath(String... path) {
      StringBuilder b = new StringBuilder();
      b.append(getLiteralPath());
      if (xml) {
        for (String p : path) {
          if (p.startsWith(":")) {
            b.append("[");
            b.append(p.substring(1));
            b.append("]");
          } else {
            b.append("/f:");
            b.append(p);
          }	  
        }	  
      } else {
        for (String p : path) {
          b.append("/");
          if (p.startsWith(":")) {
            b.append(p.substring(1));
          } else {
            b.append(p);
          }	  
        }
      }	  
      return b.toString();
    }

    private ElementDefinition getDefinition() {
      return definition;
    }

    private WrapperElement getElement() {
      return element;
    }

    private String getLiteralPath() {
      return literalPath == null ? "" : literalPath;
    }

    private List<String> getLogicalPaths() {
      return logicalPaths == null ? new ArrayList<String>() : logicalPaths;
    }

    private ElementDefinition getType() {
      return type;
    }

    private NodeStack push(WrapperElement element, int count, ElementDefinition definition, ElementDefinition type) {
      NodeStack res = new NodeStack(element.isXml());
      res.parent = this;
      res.element = element;
      res.definition = definition;
      if (element.isXml()) {
        res.literalPath = getLiteralPath() + (element.getNamespace().equals(FormatUtilities.XHTML_NS) ? "/h:" : "/f:") + element.getName();
        if (count > -1)
          res.literalPath = res.literalPath + "[" + Integer.toString(count) + "]";
      } else {
        if (element.getName() == null)
          res.literalPath = "";
        else
          res.literalPath = getLiteralPath() + "/" + element.getName();
        if (count > -1)
          res.literalPath = res.literalPath + "/" + Integer.toString(count);
      }
      res.logicalPaths = new ArrayList<String>();
      if (type != null) {
        // type will be bull if we on a stitching point of a contained resource, or if....
        res.type = type;
        String t = tail(definition.getPath());
        for (String lp : getLogicalPaths()) {
          res.logicalPaths.add(lp + "." + t);
          if (t.endsWith("[x]"))
            res.logicalPaths.add(lp + "." + t.substring(0, t.length() - 3) + type.getPath());
        }
        res.logicalPaths.add(type.getPath());
      } else if (definition != null) {
        for (String lp : getLogicalPaths())
          res.logicalPaths.add(lp + "." + element.getName());
      } else
        res.logicalPaths.addAll(getLogicalPaths());
      // CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
      // for (String lp : res.logicalPaths)
      // b.append(lp);
      // System.out.println(res.literalPath+" : "+b.toString());
      return res;
    }

    private void setType(ElementDefinition type) {
      this.type = type;
    }
  }

  public abstract class WrapperElement {
    public abstract int col();

    public abstract String getAttribute(String name);

    public abstract WrapperElement getFirstChild();

    public abstract String getName();

    public abstract WrapperElement getNamedChild(String name);

    public abstract void getNamedChildren(String name, List<WrapperElement> list);

    public abstract void getNamedChildrenWithWildcard(String name, List<WrapperElement> list);

    public abstract String getNamedChildValue(String name);

    public abstract String getNamespace();

    public abstract WrapperElement getNextSibling();

    public abstract String getResourceType();

    public abstract String getText();

    public abstract boolean hasAttribute(String name);

    public abstract boolean hasNamespace(String string);

    public abstract boolean hasProcessingInstruction();

    public abstract boolean isXml();

    public abstract int line();
  }

}
