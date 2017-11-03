package org.hl7.fhir.r4.utils.transform.deserializer;

import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.utils.FHIRLexer;
import org.hl7.fhir.r4.utils.FHIRPathEngine;
import org.hl7.fhir.utilities.Utilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hl7.fhir.r4.utils.transform.FhirTransformationEngine.*;

/**
 * @author Grahame Grieve
 */
public class DefaultStructureMapParser {

  private static final String AUTO_VAR_NAME = "vvv";

  private FHIRPathEngine fhirPathEngine;

  public DefaultStructureMapParser() {
  }

  public DefaultStructureMapParser(FHIRPathEngine fhirPathEngine) {
    this.fhirPathEngine = fhirPathEngine;
  }

  private FHIRPathEngine getFhirPathEngine() {
    return fhirPathEngine;
  }

  public void setFhirPathEngine(FHIRPathEngine fhirPathEngine) {
    this.fhirPathEngine = fhirPathEngine;
  }

  public StructureMap parse(String text) throws FHIRException {
    FHIRLexer lexer = new FHIRLexer(text);
    if (lexer.done())
      throw lexer.error("Map Input cannot be empty");
    lexer.skipComments();
    lexer.token("map");
    StructureMap result = new StructureMap();
    result.setUrl(lexer.readConstant("url"));
    lexer.token("=");
    result.setName(lexer.readConstant("name"));
    lexer.skipComments();

    while (lexer.hasToken("conceptmap"))
      parseConceptMap(result, lexer);

    while (lexer.hasToken("uses"))
      parseUses(result, lexer);
    while (lexer.hasToken("imports"))
      parseImports(result, lexer);

    parseGroup(result, lexer);

    while (!lexer.done()) {
      parseGroup(result, lexer);
    }

    return result;
  }

  private void parseConceptMap(StructureMap result, FHIRLexer lexer) throws FHIRLexer.FHIRLexerException {
    lexer.token("conceptmap");
    ConceptMap map = new ConceptMap();
    String id = lexer.readConstant("map id");
    if (!id.startsWith("#"))
      lexer.error("Concept Map identifier must start with #");
    map.setId(id);
    map.setStatus(Enumerations.PublicationStatus.DRAFT); // todo: how to add this to the text format
    result.getContained().add(map);
    lexer.token("{");
    lexer.skipComments();
    //	  lexer.token("source");
    //	  map.setSource(new UriType(lexer.readConstant("source")));
    //	  lexer.token("target");
    //	  map.setSource(new UriType(lexer.readConstant("target")));
    Map<String, String> prefixes = new HashMap<String, String>();
    while (lexer.hasToken("prefix")) {
      lexer.token("prefix");
      String n = lexer.take();
      lexer.token("=");
      String v = lexer.readConstant("prefix url");
      prefixes.put(n, v);
    }
    while (lexer.hasToken("unmapped")) {
      lexer.token("unmapped");
      lexer.token("for");
      String n = readPrefix(prefixes, lexer);
      ConceptMap.ConceptMapGroupComponent g = getGroup(map, n, null);
      lexer.token("=");
      String v = lexer.take();
      if (v.equals("provided")) {
        g.getUnmapped().setMode(ConceptMap.ConceptMapGroupUnmappedMode.PROVIDED);
      } else
        lexer.error("Only unmapped mode PROVIDED is supported at this time");
    }
    while (!lexer.hasToken("}")) {
      String srcs = readPrefix(prefixes, lexer);
      lexer.token(":");
      String sc = lexer.getCurrent().startsWith("\"") ? lexer.readConstant("code") : lexer.take();
      Enumerations.ConceptMapEquivalence eq = readEquivalence(lexer);
      String tgts = (eq != Enumerations.ConceptMapEquivalence.UNMATCHED) ? readPrefix(prefixes, lexer) : "";
      ConceptMap.ConceptMapGroupComponent g = getGroup(map, srcs, tgts);
      ConceptMap.SourceElementComponent e = g.addElement();
      e.setCode(sc);
      if (e.getCode().startsWith("\""))
        e.setCode(lexer.processConstant(e.getCode()));
      ConceptMap.TargetElementComponent tgt = e.addTarget();
      if (eq != Enumerations.ConceptMapEquivalence.EQUIVALENT)
        tgt.setEquivalence(eq);
      if (tgt.getEquivalence() != Enumerations.ConceptMapEquivalence.UNMATCHED) {
        lexer.token(":");
        tgt.setCode(lexer.take());
        if (tgt.getCode().startsWith("\""))
          tgt.setCode(lexer.processConstant(tgt.getCode()));
      }
      if (lexer.hasComment())
        tgt.setComment(lexer.take().substring(2).trim());
    }
    lexer.token("}");
  }


  private ConceptMap.ConceptMapGroupComponent getGroup(ConceptMap map, String srcs, String tgts) {
    for (ConceptMap.ConceptMapGroupComponent grp : map.getGroup()) {
      if (grp.getSource().equals(srcs))
        if ((tgts == null && !grp.hasTarget()) || (tgts != null && tgts.equals(grp.getTarget())))
          return grp;
    }
    ConceptMap.ConceptMapGroupComponent grp = map.addGroup();
    grp.setSource(srcs);
    grp.setTarget(tgts);
    return grp;
  }


  private String readPrefix(Map<String, String> prefixes, FHIRLexer lexer) throws FHIRLexer.FHIRLexerException {
    String prefix = lexer.take();
    if (!prefixes.containsKey(prefix))
      throw lexer.error("Unknown prefix '" + prefix + "'");
    return prefixes.get(prefix);
  }


  private Enumerations.ConceptMapEquivalence readEquivalence(FHIRLexer lexer) throws FHIRLexer.FHIRLexerException {
    String token = lexer.take();
    if (token.equals("-"))
      return Enumerations.ConceptMapEquivalence.RELATEDTO;
    if (token.equals("="))
      return Enumerations.ConceptMapEquivalence.EQUAL;
    if (token.equals("=="))
      return Enumerations.ConceptMapEquivalence.EQUIVALENT;
    if (token.equals("!="))
      return Enumerations.ConceptMapEquivalence.DISJOINT;
    if (token.equals("--"))
      return Enumerations.ConceptMapEquivalence.UNMATCHED;
    if (token.equals("<="))
      return Enumerations.ConceptMapEquivalence.WIDER;
    if (token.equals("<-"))
      return Enumerations.ConceptMapEquivalence.SUBSUMES;
    if (token.equals(">="))
      return Enumerations.ConceptMapEquivalence.NARROWER;
    if (token.equals(">-"))
      return Enumerations.ConceptMapEquivalence.SPECIALIZES;
    if (token.equals("~"))
      return Enumerations.ConceptMapEquivalence.INEXACT;
    throw lexer.error("Unknown equivalence token '" + token + "'");
  }


  private void parseUses(StructureMap result, FHIRLexer lexer) throws FHIRException {
    lexer.token("uses");
    StructureMap.StructureMapStructureComponent st = result.addStructure();
    st.setUrl(lexer.readConstant("url"));
    if (lexer.hasToken("alias")) {
      lexer.token("alias");
      st.setAlias(lexer.take());
    }
    lexer.token("as");
    st.setMode(StructureMap.StructureMapModelMode.fromCode(lexer.take()));
    lexer.skipToken(";");
    if (lexer.hasComment()) {
      st.setDocumentation(lexer.take().substring(2).trim());
    }
    lexer.skipComments();
  }

  private void parseImports(StructureMap result, FHIRLexer lexer) throws FHIRException {
    lexer.token("imports");
    result.addImport(lexer.readConstant("url"));
    lexer.skipToken(";");
    if (lexer.hasComment()) {
      lexer.next();
    }
    lexer.skipComments();
  }

  private void parseGroup(StructureMap result, FHIRLexer lexer) throws FHIRException {
    lexer.token("group");
    StructureMap.StructureMapGroupComponent group = result.addGroup();
    if (lexer.hasToken("for")) {
      lexer.token("for");
      if ("type".equals(lexer.getCurrent())) {
        lexer.token("type");
        lexer.token("+");
        lexer.token("types");
        group.setTypeMode(StructureMap.StructureMapGroupTypeMode.TYPEANDTYPES);
      } else {
        lexer.token("types");
        group.setTypeMode(StructureMap.StructureMapGroupTypeMode.TYPES);
      }
    } else
      group.setTypeMode(StructureMap.StructureMapGroupTypeMode.NONE);
    group.setName(lexer.take());
    if (lexer.hasToken("extends")) {
      lexer.next();
      group.setExtends(lexer.take());
    }
    lexer.skipComments();
    while (lexer.hasToken("input"))
      parseInput(group, lexer);
    while (!lexer.hasToken("endgroup")) {
      if (lexer.done())
        throw lexer.error("premature termination expecting 'endgroup'");
      parseRule(result, group.getRule(), lexer);
    }
    lexer.next();
    lexer.skipComments();
  }

  private void parseInput(StructureMap.StructureMapGroupComponent group, FHIRLexer lexer) throws FHIRException {
    lexer.token("input");
    StructureMap.StructureMapGroupInputComponent input = group.addInput();
    input.setName(lexer.take());
    if (lexer.hasToken(":")) {
      lexer.token(":");
      input.setType(lexer.take());
    }
    lexer.token("as");
    input.setMode(StructureMap.StructureMapInputMode.fromCode(lexer.take()));
    if (lexer.hasComment()) {
      input.setDocumentation(lexer.take().substring(2).trim());
    }
    lexer.skipToken(";");
    lexer.skipComments();
  }

  private void parseRule(StructureMap map, List<StructureMap.StructureMapGroupRuleComponent> list, FHIRLexer lexer) throws FHIRException {
    StructureMap.StructureMapGroupRuleComponent rule = new StructureMap.StructureMapGroupRuleComponent();
    list.add(rule);
    rule.setName(lexer.takeDottedToken());
    lexer.token(":");
    lexer.token("for");
    boolean done = false;
    while (!done) {
      parseSource(rule, lexer);
      done = !lexer.hasToken(",");
      if (!done)
        lexer.next();
    }
    if (lexer.hasToken("make")) {
      lexer.token("make");
      done = false;
      while (!done) {
        parseTarget(rule, lexer);
        done = !lexer.hasToken(",");
        if (!done)
          lexer.next();
      }
    }
    if (lexer.hasToken("then")) {
      lexer.token("then");
      if (lexer.hasToken("{")) {
        lexer.token("{");
        if (lexer.hasComment()) {
          rule.setDocumentation(lexer.take().substring(2).trim());
        }
        lexer.skipComments();
        while (!lexer.hasToken("}")) {
          if (lexer.done())
            throw lexer.error("premature termination expecting '}' in nested group");
          parseRule(map, rule.getRule(), lexer);
        }
        lexer.token("}");
      } else {
        done = false;
        while (!done) {
          parseRuleReference(rule, lexer);
          done = !lexer.hasToken(",");
          if (!done)
            lexer.next();
        }
      }
    } else if (lexer.hasComment()) {
      rule.setDocumentation(lexer.take().substring(2).trim());
    }
    if (isSimpleSyntax(rule)) {
      rule.getSourceFirstRep().setVariable(AUTO_VAR_NAME);
      rule.getTargetFirstRep().setVariable(AUTO_VAR_NAME);
      rule.getTargetFirstRep().setTransform(StructureMap.StructureMapTransform.CREATE); // with no parameter - e.g. imply what is to be created
      // no dependencies - imply what is to be done based on types
    }
    lexer.skipComments();
  }

  private boolean isSimpleSyntax(StructureMap.StructureMapGroupRuleComponent rule) {
    return
      (rule.getSource().size() == 1 && rule.getSourceFirstRep().hasContext() && rule.getSourceFirstRep().hasElement() && !rule.getSourceFirstRep().hasVariable()) &&
        (rule.getTarget().size() == 1 && rule.getTargetFirstRep().hasContext() && rule.getTargetFirstRep().hasElement() && !rule.getTargetFirstRep().hasVariable() && !rule.getTargetFirstRep().hasParameter()) &&
        (rule.getDependent().size() == 0 && rule.getRule().size() == 0);
  }

  private void parseRuleReference(StructureMap.StructureMapGroupRuleComponent rule, FHIRLexer lexer) throws FHIRLexer.FHIRLexerException {
    StructureMap.StructureMapGroupRuleDependentComponent ref = rule.addDependent();
    ref.setName(lexer.take());
    lexer.token("(");
    boolean done = false;
    while (!done) {
      ref.addVariable(lexer.take());
      done = !lexer.hasToken(",");
      if (!done)
        lexer.next();
    }
    lexer.token(")");
  }

  private void parseSource(StructureMap.StructureMapGroupRuleComponent rule, FHIRLexer lexer) throws FHIRException {
    StructureMap.StructureMapGroupRuleSourceComponent source = rule.addSource();
    source.setContext(lexer.take());
    if (source.getContext().equals("search") && lexer.hasToken("(")) {
      source.setContext("@search");
      lexer.take();
      ExpressionNode node = getFhirPathEngine().parse(lexer);
      source.setUserData(MAP_SEARCH_EXPRESSION, node);
      source.setElement(node.toString());
      lexer.token(")");
    } else if (lexer.hasToken(".")) {
      lexer.token(".");
      source.setElement(lexer.take());
    }
    if (lexer.hasToken(":")) {
      // type and cardinality
      lexer.token(":");
      source.setType(lexer.takeDottedToken());
      if (!lexer.hasToken("as", "first", "last", "not_first", "not_last", "only_one", "default")) {
        source.setMin(lexer.takeInt());
        lexer.token("..");
        source.setMax(lexer.take());
      }
    }
    if (lexer.hasToken("default")) {
      lexer.token("default");
      source.setDefaultValue(new StringType(lexer.readConstant("default value")));
    }
    if (Utilities.existsInList(lexer.getCurrent(), "first", "last", "not_first", "not_last", "only_one"))
      source.setListMode(StructureMap.StructureMapSourceListMode.fromCode(lexer.take()));

    if (lexer.hasToken("as")) {
      lexer.take();
      source.setVariable(lexer.take());
    }
    if (lexer.hasToken("where")) {
      lexer.take();
      ExpressionNode node = getFhirPathEngine().parse(lexer);
      source.setUserData(MAP_WHERE_EXPRESSION, node);
      source.setCondition(node.toString());
    }
    if (lexer.hasToken("check")) {
      lexer.take();
      ExpressionNode node = getFhirPathEngine().parse(lexer);
      source.setUserData(MAP_WHERE_CHECK, node);
      source.setCheck(node.toString());
    }
  }

  private void parseTarget(StructureMap.StructureMapGroupRuleComponent rule, FHIRLexer lexer) throws FHIRException {
    StructureMap.StructureMapGroupRuleTargetComponent target = rule.addTarget();
    String start = lexer.take();
    if (lexer.hasToken(".")) {
      target.setContext(start);
      target.setContextType(StructureMap.StructureMapContextType.VARIABLE);
      start = null;
      lexer.token(".");
      target.setElement(lexer.take());
    }
    String name;
    boolean isConstant = false;
    if (lexer.hasToken("=")) {
      if (start != null)
        target.setContext(start);
      lexer.token("=");
      isConstant = lexer.isConstant(true);
      name = lexer.take();
    } else
      name = start;

    if ("(".equals(name)) {
      // inline fluentpath expression
      target.setTransform(StructureMap.StructureMapTransform.EVALUATE);
      ExpressionNode node = getFhirPathEngine().parse(lexer);
      target.setUserData(MAP_EXPRESSION, node);
      target.addParameter().setValue(new StringType(node.toString()));
      lexer.token(")");
    } else if (lexer.hasToken("(")) {
      target.setTransform(StructureMap.StructureMapTransform.fromCode(name));
      lexer.token("(");
      if (target.getTransform() == StructureMap.StructureMapTransform.EVALUATE) {
        parseParameter(target, lexer);
        lexer.token(",");
        ExpressionNode node = getFhirPathEngine().parse(lexer);
        target.setUserData(MAP_EXPRESSION, node);
        target.addParameter().setValue(new StringType(node.toString()));
      } else {
        while (!lexer.hasToken(")")) {
          parseParameter(target, lexer);
          if (!lexer.hasToken(")"))
            lexer.token(",");
        }
      }
      lexer.token(")");
    } else if (name != null) {
      target.setTransform(StructureMap.StructureMapTransform.COPY);
      if (!isConstant) {
        StringBuilder id = new StringBuilder(name);
        while (lexer.hasToken(".")) {
          id.append(lexer.take()).append(lexer.take());
        }
        target.addParameter().setValue(new IdType(id.toString()));
      } else
        target.addParameter().setValue(readConstant(name, lexer));
    }
    if (lexer.hasToken("as")) {
      lexer.take();
      target.setVariable(lexer.take());
    }
    while (Utilities.existsInList(lexer.getCurrent(), "first", "last", "share", "collate")) {
      switch (lexer.getCurrent()) {
        case "share":
          target.addListMode(StructureMap.StructureMapTargetListMode.SHARE);
          lexer.next();
          target.setListRuleId(lexer.take());
          break;
        case "first":
          target.addListMode(StructureMap.StructureMapTargetListMode.FIRST);
          break;
        default:
          target.addListMode(StructureMap.StructureMapTargetListMode.LAST);
          break;
      }
      lexer.next();
    }
  }


  private void parseParameter(StructureMap.StructureMapGroupRuleTargetComponent target, FHIRLexer lexer) throws FHIRLexer.FHIRLexerException {
    if (!lexer.isConstant(true)) {
      target.addParameter().setValue(new IdType(lexer.take()));
    } else if (lexer.isStringConstant())
      target.addParameter().setValue(new StringType(lexer.readConstant("??")));
    else {
      target.addParameter().setValue(readConstant(lexer.take(), lexer));
    }
  }

  private Type readConstant(String s, FHIRLexer lexer) throws FHIRLexer.FHIRLexerException {
    if (Utilities.isInteger(s))
      return new IntegerType(s);
    else if (Utilities.isDecimal(s))
      return new DecimalType(s);
    else if (Utilities.existsInList(s, "true", "false"))
      return new BooleanType(s.equals("true"));
    else
      return new StringType(lexer.processConstant(s));
  }
}
