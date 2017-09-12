package org.hl7.fhir.r4.utils.transform.serializer;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.utilities.Utilities;

import java.util.HashMap;
import java.util.Map;

public class StructureMapSerializer {

  private static final boolean RENDER_MULTIPLE_TARGETS_ONELINE = true;

  public static String render(StructureMap map) {
    StringBuilder b = new StringBuilder();
    b.append("map \"");
    b.append(map.getUrl());
    b.append("\" = \"");
    b.append(Utilities.escapeJava(map.getName()));
    b.append("\"\r\n\r\n");

    renderConceptMaps(b, map);
    renderUses(b, map);
    renderImports(b, map);
    for (StructureMap.StructureMapGroupComponent g : map.getGroup())
      renderGroup(b, g);
    return b.toString();
  }

  private static void renderConceptMaps(StringBuilder b, StructureMap map) {
    for (Resource r : map.getContained()) {
      if (r instanceof ConceptMap) {
        produceConceptMap(b, (ConceptMap) r);
      }
    }
  }

  private static void produceConceptMap(StringBuilder b, ConceptMap cm) {
    b.append("conceptmap \"");
    b.append(cm.getId());
    b.append("\" {\r\n");
    Map<String, String> prefixesSrc = new HashMap<String, String>();
    Map<String, String> prefixesTgt = new HashMap<String, String>();
    char prefix = 's';
    for (ConceptMap.ConceptMapGroupComponent cg : cm.getGroup()) {
      if (!prefixesSrc.containsKey(cg.getSource())) {
        prefixesSrc.put(cg.getSource(), String.valueOf(prefix));
        b.append("  prefix ");
        b.append(prefix);
        b.append(" = \"");
        b.append(cg.getSource());
        b.append("\"\r\n");
        prefix++;
      }
      if (!prefixesTgt.containsKey(cg.getTarget())) {
        prefixesTgt.put(cg.getTarget(), String.valueOf(prefix));
        b.append("  prefix ");
        b.append(prefix);
        b.append(" = \"");
        b.append(cg.getTarget());
        b.append("\"\r\n");
        prefix++;
      }
    }
    b.append("\r\n");
    for (ConceptMap.ConceptMapGroupComponent cg : cm.getGroup()) {
      if (cg.hasUnmapped()) {
        b.append("  unmapped for ");
        b.append(prefix);
        b.append(" = ");
        b.append(cg.getUnmapped().getMode());
        b.append("\r\n");
      }
    }

    for (ConceptMap.ConceptMapGroupComponent cg : cm.getGroup()) {
      for (ConceptMap.SourceElementComponent ce : cg.getElement()) {
        b.append("  ");
        b.append(prefixesSrc.get(cg.getSource()));
        b.append(":");
        b.append(ce.getCode());
        b.append(" ");
        b.append(getChar(ce.getTargetFirstRep().getEquivalence()));
        b.append(" ");
        b.append(prefixesTgt.get(cg.getTarget()));
        b.append(":");
        b.append(ce.getTargetFirstRep().getCode());
        b.append("\r\n");
      }
    }
    b.append("}\r\n\r\n");
  }

  private static Object getChar(Enumerations.ConceptMapEquivalence equivalence) {
    switch (equivalence) {
      case RELATEDTO:
        return "-";
      case EQUAL:
        return "=";
      case EQUIVALENT:
        return "==";
      case DISJOINT:
        return "!=";
      case UNMATCHED:
        return "--";
      case WIDER:
        return "<=";
      case SUBSUMES:
        return "<-";
      case NARROWER:
        return ">=";
      case SPECIALIZES:
        return ">-";
      case INEXACT:
        return "~";
      default:
        return "??";
    }
  }

  private static void renderUses(StringBuilder b, StructureMap map) {
    for (StructureMap.StructureMapStructureComponent s : map.getStructure()) {
      b.append("uses \"");
      b.append(s.getUrl());
      b.append("\" ");
      if (s.hasAlias()) {
        b.append("alias ");
        b.append(s.getAlias());
        b.append(" ");
      }
      b.append("as ");
      b.append(s.getMode().toCode());
      b.append("\r\n");
      renderDoco(b, s.getDocumentation());
    }
    if (map.hasStructure())
      b.append("\r\n");
  }

  private static void renderImports(StringBuilder b, StructureMap map) {
    for (UriType s : map.getImport()) {
      b.append("imports \"");
      b.append(s.getValue());
      b.append("\"\r\n");
    }
    if (map.hasImport())
      b.append("\r\n");
  }

  public static String groupToString(StructureMap.StructureMapGroupComponent g) {
    StringBuilder b = new StringBuilder();
    renderGroup(b, g);
    return b.toString();
  }

  private static void renderGroup(StringBuilder b, StructureMap.StructureMapGroupComponent g) {
    b.append("group ");
    switch (g.getTypeMode()) {
      case TYPES:
        b.append("for types");
      case TYPEANDTYPES:
        b.append("for type+types ");
      default: // NONE, NULL
    }
    b.append("for types ");
    b.append(g.getName());
    if (g.hasExtends()) {
      b.append(" extends ");
      b.append(g.getExtends());
    }
    if (g.hasDocumentation())
      renderDoco(b, g.getDocumentation());
    b.append("\r\n");
    for (StructureMap.StructureMapGroupInputComponent gi : g.getInput()) {
      b.append("  input ");
      b.append(gi.getName());
      if (gi.hasType()) {
        b.append(" : ");
        b.append(gi.getType());
      }
      b.append(" as ");
      b.append(gi.getMode().toCode());
      b.append("\r\n");
    }
    if (g.hasInput())
      b.append("\r\n");
    for (StructureMap.StructureMapGroupRuleComponent r : g.getRule()) {
      renderRule(b, r, 2);
    }
    b.append("\r\nendgroup\r\n");
  }

  public static String ruleToString(StructureMap.StructureMapGroupRuleComponent r) {
    StringBuilder b = new StringBuilder();
    renderRule(b, r, 0);
    return b.toString();
  }

  private static void renderRule(StringBuilder b, StructureMap.StructureMapGroupRuleComponent r, int indent) {
    for (int i = 0; i < indent; i++)
      b.append(' ');
    b.append(r.getName());
    b.append(" : for ");
    boolean canBeAbbreviated = checkisSimple(r);

    boolean first = true;
    for (StructureMap.StructureMapGroupRuleSourceComponent rs : r.getSource()) {
      if (first)
        first = false;
      else
        b.append(", ");
      renderSource(b, rs, canBeAbbreviated);
    }
    if (r.getTarget().size() > 1) {
      b.append(" make ");
      first = true;
      for (StructureMap.StructureMapGroupRuleTargetComponent rt : r.getTarget()) {
        if (first)
          first = false;
        else
          b.append(", ");
        if (RENDER_MULTIPLE_TARGETS_ONELINE)
          b.append(' ');
        else {
          b.append("\r\n");
          for (int i = 0; i < indent + 4; i++)
            b.append(' ');
        }
        renderTarget(b, rt, false);
      }
    } else if (r.hasTarget()) {
      b.append(" make ");
      renderTarget(b, r.getTarget().get(0), canBeAbbreviated);
    }
    if (!canBeAbbreviated) {
      if (r.hasRule()) {
        b.append(" then {\r\n");
        renderDoco(b, r.getDocumentation());
        for (StructureMap.StructureMapGroupRuleComponent ir : r.getRule()) {
          renderRule(b, ir, indent + 2);
        }
        for (int i = 0; i < indent; i++)
          b.append(' ');
        b.append("}\r\n");
      } else {
        if (r.hasDependent()) {
          b.append(" then ");
          first = true;
          for (StructureMap.StructureMapGroupRuleDependentComponent rd : r.getDependent()) {
            if (first)
              first = false;
            else
              b.append(", ");
            b.append(rd.getName());
            b.append("(");
            boolean ifirst = true;
            for (StringType rdp : rd.getVariable()) {
              if (ifirst)
                ifirst = false;
              else
                b.append(", ");
              b.append(rdp.asStringValue());
            }
            b.append(")");
          }
        }
      }
    }
    renderDoco(b, r.getDocumentation());
    b.append("\r\n");
  }

  private static boolean checkisSimple(StructureMap.StructureMapGroupRuleComponent r) {
    return
      (r.getSource().size() == 1 && r.getSourceFirstRep().hasElement() && r.getSourceFirstRep().hasVariable()) &&
        (r.getTarget().size() == 1 && r.getTargetFirstRep().hasVariable() && (r.getTargetFirstRep().getTransform() == null || r.getTargetFirstRep().getTransform() == StructureMap.StructureMapTransform.CREATE) && r.getTargetFirstRep().getParameter().size() == 0) &&
        (r.getDependent().size() == 0);
  }

  public static String sourceToString(StructureMap.StructureMapGroupRuleSourceComponent r) {
    StringBuilder b = new StringBuilder();
    renderSource(b, r, false);
    return b.toString();
  }

  private static void renderSource(StringBuilder b, StructureMap.StructureMapGroupRuleSourceComponent rs, boolean abbreviate) {
    b.append(rs.getContext());
    if (rs.getContext().equals("@search")) {
      b.append('(');
      b.append(rs.getElement());
      b.append(')');
    } else if (rs.hasElement()) {
      b.append('.');
      b.append(rs.getElement());
    }
    if (rs.hasType()) {
      b.append(" : ");
      b.append(rs.getType());
      if (rs.hasMin()) {
        b.append(" ");
        b.append(rs.getMin());
        b.append("..");
        b.append(rs.getMax());
      }
    }

    if (rs.hasListMode()) {
      b.append(" ");
      b.append(rs.getListMode().toCode());
    }
    if (rs.hasDefaultValue()) {
      b.append(" default ");
      assert rs.getDefaultValue() instanceof StringType;
      b.append("\"" + Utilities.escapeJson(((StringType) rs.getDefaultValue()).asStringValue()) + "\"");
    }
    if (!abbreviate && rs.hasVariable()) {
      b.append(" as ");
      b.append(rs.getVariable());
    }
    if (rs.hasCondition()) {
      b.append(" where ");
      b.append(rs.getCondition());
    }
    if (rs.hasCheck()) {
      b.append(" check ");
      b.append(rs.getCheck());
    }
  }

  public static String targetToString(StructureMap.StructureMapGroupRuleTargetComponent rt) {
    StringBuilder b = new StringBuilder();
    renderTarget(b, rt, false);
    return b.toString();
  }

  private static void renderTarget(StringBuilder b, StructureMap.StructureMapGroupRuleTargetComponent rt, boolean abbreviate) {
    if (rt.hasContext()) {
      if (rt.getContextType() == StructureMap.StructureMapContextType.TYPE)
        b.append("@");
      b.append(rt.getContext());
      if (rt.hasElement()) {
        b.append('.');
        b.append(rt.getElement());
      }
    }
    if (!abbreviate && rt.hasTransform()) {
      if (rt.hasContext())
        b.append(" = ");
      if (rt.getTransform() == StructureMap.StructureMapTransform.COPY && rt.getParameter().size() == 1) {
        renderTransformParam(b, rt.getParameter().get(0));
      } else if (rt.getTransform() == StructureMap.StructureMapTransform.EVALUATE && rt.getParameter().size() == 1) {
        b.append("(");
        b.append("\"" + ((StringType) rt.getParameter().get(0).getValue()).asStringValue() + "\"");
        b.append(")");
      } else if (rt.getTransform() == StructureMap.StructureMapTransform.EVALUATE && rt.getParameter().size() == 2) {
        b.append(rt.getTransform().toCode());
        b.append("(");
        b.append(((IdType) rt.getParameter().get(0).getValue()).asStringValue());
        b.append("\"" + ((StringType) rt.getParameter().get(1).getValue()).asStringValue() + "\"");
        b.append(")");
      } else {
        b.append(rt.getTransform().toCode());
        b.append("(");
        boolean first = true;
        for (StructureMap.StructureMapGroupRuleTargetParameterComponent rtp : rt.getParameter()) {
          if (first)
            first = false;
          else
            b.append(", ");
          renderTransformParam(b, rtp);
        }
        b.append(")");
      }
    }
    if (!abbreviate && rt.hasVariable()) {
      b.append(" as ");
      b.append(rt.getVariable());
    }
    for (Enumeration<StructureMap.StructureMapTargetListMode> lm : rt.getListMode()) {
      b.append(" ");
      b.append(lm.getValue().toCode());
      if (lm.getValue() == StructureMap.StructureMapTargetListMode.SHARE) {
        b.append(" ");
        b.append(rt.getListRuleId());
      }
    }
  }

  public static String paramToString(StructureMap.StructureMapGroupRuleTargetParameterComponent rtp) {
    StringBuilder b = new StringBuilder();
    renderTransformParam(b, rtp);
    return b.toString();
  }

  private static void renderTransformParam(StringBuilder b, StructureMap.StructureMapGroupRuleTargetParameterComponent rtp) {
    try {
      if (rtp.hasValueBooleanType())
        b.append(rtp.getValueBooleanType().asStringValue());
      else if (rtp.hasValueDecimalType())
        b.append(rtp.getValueDecimalType().asStringValue());
      else if (rtp.hasValueIdType())
        b.append(rtp.getValueIdType().asStringValue());
      else if (rtp.hasValueDecimalType())
        b.append(rtp.getValueDecimalType().asStringValue());
      else if (rtp.hasValueIntegerType())
        b.append(rtp.getValueIntegerType().asStringValue());
      else
        b.append("\"" + Utilities.escapeJava(rtp.getValueStringType().asStringValue()) + "\"");
    } catch (FHIRException e) {
      e.printStackTrace();
      b.append("error!");
    }
  }

  private static void renderDoco(StringBuilder b, String doco) {
    if (Utilities.noString(doco))
      return;
    b.append(" // ");
    b.append(doco.replace("\r\n", " ").replace("\r", " ").replace("\n", " "));
    b.append("\n");
  }

}
