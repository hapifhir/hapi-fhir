package org.hl7.fhir.convertors.misc;

/*-
 * #%L
 * org.hl7.fhir.convertors
 * %%
 * Copyright (C) 2014 - 2019 Health Level 7
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.convertors.R3ToR4Loader;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.r4.conformance.ProfileUtilities;
import org.hl7.fhir.r4.context.SimpleWorkerContext;
import org.hl7.fhir.r4.model.ElementDefinition;
import org.hl7.fhir.r4.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.r4.model.Parameters;
import org.hl7.fhir.r4.model.StructureDefinition;
import org.hl7.fhir.r4.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.r4.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.r4.model.ValueSet;
import org.hl7.fhir.r4.model.ValueSet.ValueSetExpansionContainsComponent;
import org.hl7.fhir.r4.terminologies.ValueSetExpander.ValueSetExpansionOutcome;
import org.hl7.fhir.utilities.CommaSeparatedStringBuilder;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.Node;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.comments.LineComment;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.MethodCallExpr;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.VariableDeclarationExpr;
import japa.parser.ast.stmt.ExpressionStmt;
import japa.parser.ast.stmt.ForeachStmt;
import japa.parser.ast.stmt.IfStmt;
import japa.parser.ast.stmt.ReturnStmt;
import japa.parser.ast.stmt.SwitchEntryStmt;
import japa.parser.ast.stmt.SwitchStmt;
import japa.parser.ast.visitor.VoidVisitorAdapter;

public class VersionTransformMapBuilder {

  public class ElementTask {

    private ElementDefinition element;
    private String grpName;

    public ElementTask(ElementDefinition element, String grpName) {
      this.element = element;
      this.grpName = grpName;
    }

    public ElementDefinition getElement() {
      return element;
    }

    public String getGrpName() {
      return grpName;
    }

  }
  public VersionTransformMapBuilder() {
    super();
  }

  public class IterContext {

    private String iterVariable;
    private String iterExpression;
    
    public IterContext() {
    }

    public IterContext(String var, String expr) {
      this.iterVariable = var;
      this.iterExpression = expr;
    }

    public String patch(String expr) {
      if (iterVariable == null)
        return expr;
      if (expr.equals(iterVariable))
        return iterExpression;
      if (expr.startsWith(iterVariable+".")) {
        String s = iterExpression+expr.substring(iterVariable.length());
        if (s.endsWith(".getValue()"))
            s = s.substring(0, s.length()-10);
        return s;
      }
      return expr;
    }

    public boolean scopeIs(String scopeName, String expression) {
      if (iterVariable == null)
        return scopeName.equals(expression);
      else
        return scopeName.equals(expression) || iterVariable.equals(expression); 
    }

    public String patch(String scope, String expr) {
      if (iterVariable == null)
        return expr;
      if (scope.equals(iterVariable)) {
        String s = iterExpression+"."+expr;
        if (s.endsWith(".getValue"))
          s = s.substring(0, s.length()-9);
        if (s.endsWith("()"))
          s = s.substring(0, s.length()-2);
        return tail(s);
      }
      return null;
    }

  }

  public class CodeMap {

    private ValueSetExpansionContainsComponent src;
    private ValueSetExpansionContainsComponent tgt;

    public CodeMap(ValueSetExpansionContainsComponent src, ValueSetExpansionContainsComponent tgt) {
      this.src = src;
      this.tgt = tgt;
    }

  }

  public class MapContext {

    public SimpleWorkerContext sourceContext;
    public String sourcePath;
    public String sourceType;
    public SimpleWorkerContext targetContext;
    public String targetType;
    public String targetPath;
    private boolean forwards;

    public MapContext(boolean forwards, SimpleWorkerContext sourceContext, String sourcePath, SimpleWorkerContext targetContext, String targetPath) {
      this.forwards = forwards;
      this.sourceContext = sourceContext;
      this.sourcePath = sourcePath;
      this.sourceType = sourcePath;
      this.targetContext = targetContext;
      this.targetPath = targetPath;
      this.targetType = targetPath;
    }

    public MapContext(boolean forwards, SimpleWorkerContext sourceContext, String sourceType, String sourcePath, SimpleWorkerContext targetContext, String targetType, String targetPath) {
      this.forwards = forwards;
      this.sourceContext = sourceContext;
      this.sourcePath = sourcePath;
      this.sourceType = sourceType;
      this.targetContext = targetContext;
      this.targetPath = targetPath;
      this.targetType = targetType;
    }

    public MapContext(MapContext context, String sourceElement, String targetElement) {
      this.forwards = context.forwards;
      this.sourceContext = context.sourceContext;
      this.sourcePath = context.sourcePath+'.'+sourceElement;
      this.sourceType = context.sourceType;
      this.targetContext = context.targetContext;
      this.targetPath = context.targetPath+'.'+targetElement;
      this.targetType = context.targetType;
    }
  }

  private class MapRoutines {
    private MethodDeclaration forwards;
    private MethodDeclaration backwards;
    private String type;
    private String oldType;
  }
  
  private List<MapRoutines> transforms = new ArrayList<MapRoutines>();
  private Map<String, MethodDeclaration> methodsFwds = new HashMap<String, MethodDeclaration>();
  private Map<String, MethodDeclaration> methodsBack = new HashMap<String, MethodDeclaration>();
  private SimpleWorkerContext contextR3;
  private SimpleWorkerContext contextR4;
  private String maps;
  
  public static void main(String[] args) throws Exception {
    new VersionTransformMapBuilder().execute();
  }
  
  private void execute() throws Exception {
    System.out.println("loading R3");
    R3ToR4Loader ldr = new R3ToR4Loader();
    contextR3 = new SimpleWorkerContext();
    contextR3.setAllowLoadingDuplicates(true);
    contextR3.loadFromFile("C:\\work\\org.hl7.fhir\\build\\source\\release3\\profiles-types.xml", ldr);
    contextR3.loadFromFile("C:\\work\\org.hl7.fhir\\build\\source\\release3\\profiles-resources.xml", ldr);
    contextR3.loadFromFile("C:\\work\\org.hl7.fhir\\build\\source\\release3\\valuesets.xml", ldr);
    contextR3.loadFromFile("C:\\work\\org.hl7.fhir\\build\\source\\release3\\expansions.xml", ldr);

    System.out.println("loading R4");
    contextR4 = new SimpleWorkerContext();
    contextR4.setAllowLoadingDuplicates(true);
    contextR4.loadFromFile("C:\\work\\org.hl7.fhir\\build\\publish\\profiles-types.xml", null);
    contextR4.loadFromFile("C:\\work\\org.hl7.fhir\\build\\publish\\profiles-resources.xml", null);
    contextR4.loadFromFile("C:\\work\\org.hl7.fhir\\build\\publish\\valuesets.xml", null);
    contextR4.loadFromFile("C:\\work\\org.hl7.fhir\\build\\publish\\expansions.xml", null);
    
    contextR3.setExpansionProfile(new Parameters());
    contextR4.setExpansionProfile(new Parameters());
    contextR3.setName("R3");
    contextR4.setName("R4");
    
    System.out.println("parsing transform.java");
    // creates an input stream for the file to be parsed
    FileInputStream in = new FileInputStream("C:\\work\\org.hl7.fhir\\build\\implementations\\java\\org.hl7.fhir.convertors\\src\\org\\hl7\\fhir\\convertors\\VersionConvertor_30_40.java");

    CompilationUnit cu;
    try {
      // parse the file
      cu = JavaParser.parse(in);
    } finally {
      in.close();
    }
    new MethodVisitor().visit(cu, null);
    checkConversions();
    System.out.println("Primitive Types");
    processSimpleTypes();
    System.out.println("Complex Types");
    for (StructureDefinition sd : contextR4.allStructures()) {
      if (sd.getKind() == StructureDefinitionKind.COMPLEXTYPE && sd.getDerivation() == TypeDerivationRule.SPECIALIZATION) {
        processComplexType(sd);
      }
    }
    System.out.println("Resources");
    for (StructureDefinition sd : contextR4.allStructures()) {
      if (sd.getKind() == StructureDefinitionKind.RESOURCE && sd.getDerivation() == TypeDerivationRule.SPECIALIZATION) {
        processComplexType(sd);
      }
    }
    String rt = "StructureDefinition";
    System.out.println(rt);
    StructureDefinition sd = contextR4.fetchTypeDefinition(rt);
    processComplexType(sd);
    
    System.out.println("All Done");
  }
  
  private void checkConversions() {
    for (MapRoutines mr : transforms) {
      if (mr.backwards == null)
        System.out.println("no backwards transform for "+mr.oldType+" --> " +mr.type);
      if (mr.forwards == null)
        System.out.println("no forwards transform for "+mr.oldType+" --> " +mr.type);
    }
  }

  private class MethodVisitor extends VoidVisitorAdapter {

    @Override
    public void visit(MethodDeclaration meth, Object arg) {
      String rt = meth.getType().toString();
      if (meth.getParameters().size() != 1)
        ; // System.out.println(rt+" "+meth.getName());
      else {
        String pt = meth.getParameters().get(0).getType().toString();
        if (pt.startsWith("org.hl7.fhir.dstu3.model.") && rt.startsWith("org.hl7.fhir.r4.model.")) {
          registerForwards(meth, pt.substring(25), rt.substring(22));
          methodsFwds.put(meth.getName(), meth);
        } else if (pt.startsWith("org.hl7.fhir.r4.model.") && rt.startsWith("org.hl7.fhir.dstu3.model.")) {
          registerBackwards(meth, pt.substring(22), rt.substring(25));
          methodsBack.put(meth.getName(), meth);
        } else
          ; // System.out.println(rt+" "+meth.getName()+"("+pt+")");
      }
      super.visit(meth, arg);
    }
  }
  private void registerForwards(MethodDeclaration meth, String oldType, String type) {
    MapRoutines mr = null;
    for (MapRoutines t  : transforms) {
      if (t.type.equals(type) && t.oldType.equals(oldType))
        mr = t;
    }
    if (mr == null) {
      mr = new MapRoutines();
      mr.type = type;
      mr.oldType = oldType;
      transforms.add(mr);
    }
    if (mr.forwards != null)
      throw new Error("Duplicate forward method for "+type+"/"+oldType+": "+meth.getName()+" (found "+mr.forwards.getName()+")");
    mr.forwards = meth;
  }

  public void registerBackwards(MethodDeclaration meth, String type, String oldType) {
    MapRoutines mr = null;
    for (MapRoutines t  : transforms) {
      if (t.type.equals(type) && t.oldType.equals(oldType))
        mr = t;
    }
    if (mr == null) {
      mr = new MapRoutines();
      mr.type = type;
      mr.oldType = oldType;
      transforms.add(mr);
    }
    if (mr.backwards != null)
      throw new Error("Duplicate backward method for "+type+"/"+oldType+": "+meth.getName()+" (found "+mr.backwards.getName()+")");
    mr.backwards = meth;
  }
  
  private List<MapRoutines> findRoutinesForType(String type) {
    List<MapRoutines> res = new ArrayList<MapRoutines>();
    for (MapRoutines t : transforms)
      if (t.type.equals(type))
        res.add(t);
    return res;
  }

  private void processSimpleTypes() throws IOException {
    StringBuilder f = new StringBuilder();
    f.append("map \"http://hl7.org/fhir/StructureMap/primitives3to4\" = \"R3 to R4 Primitive Conversions\"\r\n\r\n");
    StringBuilder b = new StringBuilder();
    b.append("map \"http://hl7.org/fhir/StructureMap/primitives4to3\" = \"R4 to R3 Primitive Conversions\"\r\n\r\n");
    List<String> types = new ArrayList<String>();
    for (StructureDefinition sd : contextR4.allStructures()) {
      if (sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE) 
        if (!types.contains(sd.getUrl()))
          types.add(sd.getUrl());
    }
    Collections.sort(types);
    
    for (String n : types) {
      StructureDefinition sd = contextR4.fetchResource(StructureDefinition.class, n);
      b.append("uses \"http://hl7.org/fhir/StructureDefinition/"+sd.getType()+"\" as source\r\n");
      b.append("uses \"http://hl7.org/fhir/3.0/StructureDefinition/"+sd.getType()+"\" as target\r\n");
      f.append("uses \"http://hl7.org/fhir/3.0/StructureDefinition/"+sd.getType()+"\" as source\r\n");
      f.append("uses \"http://hl7.org/fhir/StructureDefinition/"+sd.getType()+"\" as target\r\n");
    }
    f.append("\r\n");
    b.append("\r\n");
    f.append("imports \"http://hl7.org/fhir/StructureMap/Element3to4\"\r\n");
    b.append("imports \"http://hl7.org/fhir/StructureMap/Element4to3\"\r\n");
    f.append("\r\n");
    b.append("\r\n");
    for (String n : types) {
      StructureDefinition sd = contextR4.fetchResource(StructureDefinition.class, n);
      f.append("group "+sd.getType()+" extends Element\r\n");
      f.append("  input src : "+sd.getType()+"R3 as source\r\n");
      f.append("  input tgt : "+sd.getType()+" as target\r\n\r\n");
      f.append("  \""+sd.getType()+"-value\" : for src.value as v make tgt.value = v\r\n");
      f.append("endgroup\r\n\r\n");
      
      b.append("group "+sd.getType()+" extends Element\r\n");
      b.append("  input src : "+sd.getType()+" as source\r\n");
      b.append("  input tgt : "+sd.getType()+"R3 as target\r\n\r\n");
      b.append("  \""+sd.getType()+"-value\" : for src.value as v make tgt.value = v\r\n");
      b.append("endgroup\r\n\r\n");
    }
    TextFile.stringToFile(f.toString(), "C:\\work\\org.hl7.fhir\\build\\implementations\\r3maps\\R3toR4\\primitives.map");
    TextFile.stringToFile(b.toString(), "C:\\work\\org.hl7.fhir\\build\\implementations\\r3maps\\R4toR3\\primitives.map");
  }

  private void processComplexType(StructureDefinition sd) throws IOException, FHIRException {
    boolean doneF = false;
    boolean doneR = false;
    try {
      List<MapRoutines> mrs = findRoutinesForType(sd.getType());
      MapRoutines mr = null;
      for (MapRoutines t : mrs) {
        if (t.oldType != null && t.oldType.equals(sd.getType())) {
          if (mr != null)
            reportError("multiple transforms for "+sd.getType());
          mr = t;
        }
      }
      if (mr != null) {
        System.out.println("  ..."+sd.getType());
        StringBuilder f = new StringBuilder();
        f.append("map \"http://hl7.org/fhir/StructureMap/"+sd.getType()+"3to4\" = \"R3 to R4 Conversions for "+sd.getType()+"\"\r\n\r\n");
        StringBuilder b = new StringBuilder();
        b.append("map \"http://hl7.org/fhir/StructureMap/"+sd.getType()+"4to3\" = \"R4 to R3 Conversion for "+sd.getType()+"\"\r\n\r\n");
        f.append("$maps$\r\n");
        b.append("$maps$\r\n");
        b.append("uses \"http://hl7.org/fhir/StructureDefinition/"+sd.getType()+"\" alias "+sd.getType()+" as source\r\n");
        b.append("uses \"http://hl7.org/fhir/3.0/StructureDefinition/"+sd.getType()+"\" alias "+sd.getType()+"R3 as target\r\n");
        f.append("uses \"http://hl7.org/fhir/3.0/StructureDefinition/"+sd.getType()+"\" alias "+sd.getType()+"R3 as source\r\n");
        f.append("uses \"http://hl7.org/fhir/StructureDefinition/"+sd.getType()+"\" alias "+sd.getType()+" as target\r\n");

        String base = contextR4.fetchResource(StructureDefinition.class, sd.getBaseDefinition()).getType();
        f.append("\r\nimports \"http://hl7.org/fhir/StructureMap/*3to4\"\r\n");
        f.append("$imports$\r\n\r\n");
        b.append("\r\nimports \"http://hl7.org/fhir/StructureMap/*4to3\"\r\n");
        b.append("$imports$\r\n\r\n");

        f.append("\r\n");
        b.append("\r\n");
        f.append("group "+sd.getType()+" extends "+base+"\r\n");
        f.append("  input src : "+sd.getType()+"R3 as source\r\n");
        f.append("  input tgt : "+sd.getType()+" as target\r\n\r\n");
        maps = "";
        MapContext context = new MapContext(true, contextR3, mr.oldType, contextR4, mr.type); 
        processMethod(0, "src", "tgt", f, mr.forwards, context);
        f.append("endgroup\r\n\r\n");
        TextFile.stringToFile(f.toString().replace("$maps$", maps).replace("$imports$", imports(false)), "C:\\work\\org.hl7.fhir\\build\\implementations\\r3maps\\R3toR4\\"+sd.getType()+".map");
        doneF = true;

        maps = "";

        b.append("group "+sd.getType()+" extends "+base+"\r\n");
        b.append("  input src : "+sd.getType()+" as source\r\n");
        b.append("  input tgt : "+sd.getType()+"R3 as target\r\n\r\n");
        context = new MapContext(false, contextR4, mr.type, contextR3, mr.oldType); 
        processMethod(0, "src", "tgt", b, mr.backwards, context);
        b.append("endgroup\r\n\r\n");
        TextFile.stringToFile(b.toString().replace("$maps$", maps).replace("$imports$", imports(true)), "C:\\work\\org.hl7.fhir\\build\\implementations\\r3maps\\R4toR3\\"+sd.getType()+".map");
        doneR = true;
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }
    generateNative(sd, !doneR, !doneF);
  }

  private void generateNative(StructureDefinition sd, boolean doR, boolean doF) throws IOException {
    System.out.println("  ... native "+sd.getType());
    StringBuilder f = new StringBuilder();
    f.append("map \"http://hl7.org/fhir/StructureMap/"+sd.getType()+"2to3\" = \"R3 to R4 Conversions for "+sd.getType()+"\"\r\n\r\n");
    StringBuilder b = new StringBuilder();
    b.append("map \"http://hl7.org/fhir/StructureMap/"+sd.getType()+"3to2\" = \"R4 to R3 Conversion for "+sd.getType()+"\"\r\n\r\n");
    f.append("$maps$\r\n");
    b.append("$maps$\r\n");
    b.append("uses \"http://hl7.org/fhir/StructureDefinition/"+sd.getType()+"\" alias "+sd.getType()+" as source\r\n");
    b.append("uses \"http://hl7.org/fhir/3.0/StructureDefinition/"+sd.getType()+"\" alias "+sd.getType()+"R3 as target\r\n");
    f.append("uses \"http://hl7.org/fhir/3.0/StructureDefinition/"+sd.getType()+"\" alias "+sd.getType()+"R3 as source\r\n");
    f.append("uses \"http://hl7.org/fhir/StructureDefinition/"+sd.getType()+"\" alias "+sd.getType()+" as target\r\n");

    String base = contextR4.fetchResource(StructureDefinition.class, sd.getBaseDefinition()).getType();
    f.append("\r\nimports \"http://hl7.org/fhir/StructureMap/*3to4\"\r\n");
    f.append("$imports$\r\n\r\n");
    b.append("\r\nimports \"http://hl7.org/fhir/StructureMap/*4to3\"\r\n");
    b.append("$imports$\r\n\r\n");

    MapContext context = new MapContext(true, contextR3, sd.getType(), contextR4, sd.getType()); 
    processElement("src", "tgt", sd.getType(), sd.getBaseDefinition().substring(40), f, b, sd, sd.getSnapshot().getElementFirstRep(), context);
    if (doF)
      TextFile.stringToFile(f.toString().replace("$maps$", maps).replace("$imports$", imports(false)), "C:\\work\\org.hl7.fhir\\build\\implementations\\r3maps\\R3toR4\\"+sd.getType()+".map");
    if (doR)
      TextFile.stringToFile(b.toString().replace("$maps$", maps).replace("$imports$", imports(false)), "C:\\work\\org.hl7.fhir\\build\\implementations\\r3maps\\R4toR3\\"+sd.getType()+".map");
  }

  private void processElement(String src, String tgt, String grpName, String parent, StringBuilder f, StringBuilder b, StructureDefinition sd, ElementDefinition ed, MapContext context) {
    f.append("\r\n");
    f.append("group "+grpName+" extends "+parent+"\r\n");
    f.append("  input src : "+sd.getType()+"R3 as source\r\n");
    f.append("  input tgt : "+sd.getType()+" as target\r\n\r\n");
    b.append("\r\n");
    b.append("group "+grpName+" extends "+parent+"\r\n");
    b.append("  input src : "+sd.getType()+"R3 as source\r\n");
    b.append("  input tgt : "+sd.getType()+" as target\r\n\r\n");
    maps = "";

    List<ElementTask> tasks = new ArrayList<ElementTask>();
    
    List<ElementDefinition> children = ProfileUtilities.getChildList(sd,  ed);
    for (ElementDefinition child : children) {
      if (!isInherited(child)) {
        String n = tail(child.getPath());
        if (!isAbstractType(child)) {
          String s = "  \""+child.getPath()+"\": for "+src+"."+n+" make "+tgt+"."+n+"\r\n";
          b.append(s);
          f.append(s);
        } else {
          grpName = normalise(child.getPath());
          String s = "  \""+child.getPath()+"\": for "+src+"."+n+" as s make "+tgt+"."+n+" as t then "+grpName+"(s,t)\r\n";
          b.append(s);
          f.append(s);
          tasks.add(new ElementTask(child, grpName));
        }
      }
    }
    
    f.append("endgroup\r\n\r\n");

    for (ElementTask task : tasks) {
      processElement("src", "tgt", task.getGrpName(), task.getElement().getTypeFirstRep().getCode(), f, b, sd, task.getElement(), context);
    }

  }

  private boolean isInherited(ElementDefinition child) {
    return !child.getPath().equals(child.getBase().getPath());
  }

  private String normalise(String path) {
    StringBuilder b = new StringBuilder();
    boolean upcase = true;
    for (char c : path.toCharArray()) {
      if (c == '.')
        upcase = true;
      else if (upcase) {
        upcase = false;
        b.append(Character.toUpperCase(c));
      } else
        b.append(c);
    }
    return b.toString();
  }

  private boolean isAbstractType(ElementDefinition child) {
    return child.getType().size() == 1 && Utilities.existsInList(child.getType().get(0).getCode(), "Element", "BackboneElement");
  }

  private CharSequence imports(boolean bck) {
    StringBuilder b = new StringBuilder();
    return b.toString();
  }

  private void processMethod(int indent, String src, String tgt, StringBuilder f, MethodDeclaration meth, MapContext context) throws FHIRException {
    if (meth.getBody() == null)
      reportError("no body on method: "+meth.toString());
    else
      for (Node n : meth.getBody().getChildrenNodes()) {
        processExpression(indent, new IterContext(), src, tgt, f, context, n);
      }
  }

  private void processExpression(int indent, IterContext iter, String src, String tgt, StringBuilder f, MapContext context, Node n) throws FHIRException {
    if (n instanceof ExpressionStmt) {
      processExpressionStmt(indent, iter, src, tgt, f, (ExpressionStmt) n, context);
    } else if (n instanceof IfStmt && ns(n.toString()).equals("if (src == null || src.isEmpty()) return null;")) {
      // these we ignore
    } else if (n instanceof IfStmt && ns(n.toString()).startsWith("if (src.has")) {
      // ignore the .has and process the follow up
      IfStmt ifs = (IfStmt) n;
      for (Node t : ifs.getThenStmt().getChildrenNodes())
        processExpression(indent, iter, src, tgt, f, context, t);
    } else if (n instanceof VariableDeclarationExpr) {
      VariableDeclarationExpr v = (VariableDeclarationExpr) n;
      if (v.getVars().get(0).toString().equals("tgt")) {
      // these we ignore
      } else 
        reportError("Unhandled Variable Declaration: "+n.toString());
    } else if (n instanceof ReturnStmt) {
      // these we ignore
    } else if (n instanceof MethodCallExpr) {
      processMethodCallExpr(indent, iter, src, tgt, f, context, (MethodCallExpr) n);
    } else if (n instanceof ForeachStmt) {
      ForeachStmt fe = (ForeachStmt) n;
      if (fe.getIterable().toString().startsWith("src.") && fe.getBody().getChildrenNodes().size() == 1) {
        String var = fe.getVariable().getVars().get(0).getId().toString();
        processExpression(indent, new IterContext(var, fe.getIterable().toString()), src, tgt, f, context, fe.getBody().getChildrenNodes().get(0));
      } else
        reportError("Unhandled ForeachStmt of type "+n.getClass().getName()+": "+n.toString());
    } else if (!(n instanceof LineComment))
      reportError("Unhandled Node of type "+n.getClass().getName()+": "+n.toString());
  }

  private String ns(String s) {
    s = s.replace("\r", " ");
    s = s.replace("\n", " ");
    s = s.replace("\t", " ");
    while (s.contains("  "))
      s = s.replace("  ", " ");
    return s;
  }

  private void processExpressionStmt(int indent, IterContext iter, String src, String tgt, StringBuilder f, ExpressionStmt n, MapContext context) throws FHIRException {
    Expression expr = n.getExpression();
    if (expr instanceof VariableDeclarationExpr) {
      VariableDeclarationExpr v = (VariableDeclarationExpr) expr;
      if (v.getVars().get(0).getId().toString().equals("tgt")) {
        // these we ignore
      } else 
        reportError("Unhandled VariableDeclarationExpr: "+n.toString());
    } else if (expr instanceof MethodCallExpr) {
      MethodCallExpr me = (MethodCallExpr) expr;
      processMethodCallExpr(indent, iter, src, tgt, f, context, me);
    } else
      reportError("Unhandled Expression Node "+expr.getClass().getName()+": "+n.toString());
  }

  private void processMethodCallExpr(int indent, IterContext iter, String src, String tgt, StringBuilder f, MapContext context, MethodCallExpr me) throws FHIRException {
    if (me.getScope() == null && me.getName().startsWith("copy"))
      return;
    if (me.getScope() == null)
      reportError("Unhandled MethodCallExpr (no scope): "+me.toString());
    else if ("tgt".equals(me.getScope().toString()))
      processAssignment(indent, iter, src, tgt, f, me, context);
    else
      reportError("Unhandled MethodCallExpr "+me.getScope()+": "+me.toString());
  }

  private void processAssignment(int indent, IterContext iter, String src, String tgt, StringBuilder b, MethodCallExpr expr, MapContext context) throws FHIRException {
    String tv = expr.getName();
    
    if ((tv.startsWith("set") || tv.startsWith("add")) && expr.getArgs().size() == 1) {
      Expression p1 = expr.getArgs().get(0);
      if (p1 instanceof MethodCallExpr) {
        MethodCallExpr pm1 = (MethodCallExpr) p1;
        if (pm1.getScope() != null && iter.scopeIs("src", pm1.getScope().toString())) {
          String sv = iter.patch(pm1.getScope().toString(), pm1.getName());
          if (sv.startsWith("get")) {
            String srcType = getSpecifiedType(context.sourceContext, context.sourceType, context.sourcePath +"."+unPropertyise(sv), expr);
            String tgtType = getSpecifiedType(context.targetContext, context.targetType, context.targetPath +"."+unPropertyise(tv), expr);
            if (srcType.equals(tgtType)) {
              b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"\" : for "+src+"."+unPropertyise(sv)+" as vs make "+tgt+"."+unPropertyise(tv)+" as vt\r\n");
            } else if (comboIsOk(srcType, tgtType)) {
              b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"\" : for "+src+"."+unPropertyise(sv)+" as vs make "+tgt+"."+unPropertyise(tv)+" as vt then "+srcType+"To"+Utilities.capitalize(tgtType)+"(vs, vt)\r\n");
            } else 
              reportError("type mismatch: "+srcType+" != "+tgtType);
            return;
          }
        }
        if (pm1.getScope() == null && pm1.getName().startsWith("convert")) {
          if (pm1.getName().equals("convertType")) {
            String sv = findSrcGet(pm1.getArgs().get(0), iter);
            if (sv == null) {
              reportError("Unhandled source for assignment: "+expr.toString());
            } else {
              boolean fr = sv.endsWith("FirstRep");
              if (fr) sv = sv.substring(0, sv.length()-8);              
              List<String> srcTypes = getPossibleTypes(context.sourceContext, context.sourceType, context.sourcePath +"."+unPropertyise(sv), expr);
              List<String> tgtTypes = getPossibleTypes(context.targetContext, context.targetType, context.targetPath +"."+unPropertyise(tv), expr);
              for (String s : srcTypes) {
                if (tgtTypes.contains(s)) {
                  b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"-"+s+"\" : for "+src+"."+unPropertyise(sv)+" "+(fr ? "first " : "")+" : "+s+" as vs make "+tgt+"."+unPropertyise(tv)+" = create(\""+s+"\") as vt then "+s+"(vs,vt)\r\n");
                }
              }
            }
            return;
          } else if (isDataType(pm1.getName().substring(7)) && pm1.getArgs().size() == 1) {
            String type = pm1.getName().substring(7);
            String sv = findSrcGet(pm1.getArgs().get(0), iter);
            if (sv != null && sv.startsWith("get")) {
              b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"\" : for "+src+"."+unPropertyise(sv)+" as vs make "+tgt+"."+unPropertyise(tv)+" as vt\r\n");
              return;
            }
            reportError("Unhandled Assignment of other type: "+expr.toString());
            return;
          } else if (pm1.getName().equals("convertSimpleQuantity")) {
            String type = "Quantity";
            String sv = findSrcGet(pm1.getArgs().get(0), iter);
            if (sv != null && sv.startsWith("get")) {
              b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"\" : for "+src+"."+unPropertyise(sv)+" as vs make "+tgt+"."+unPropertyise(tv)+" as vt\r\n");
              return;
            }
            reportError("Unhandled Assignment of other type: "+expr.toString());
            return;
          } else if (pm1.getName().startsWith("convert"+context.targetPath)) {
            if (isEnumConversion(pm1.getName(), context)) {
              String sv = findSrcGet(pm1.getArgs().get(0), iter);
              if (sv != null && sv.startsWith("get")) {
                String url = processConceptMap(pm1.getName(), context, unPropertyise(sv), unPropertyise(tv), expr);
                if (url == null)
                  b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"\" : for "+src+"."+unPropertyise(sv)+" as vs make "+tgt+"."+unPropertyise(tv)+" as vt\r\n");
                else
                  b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"\" : for "+src+"."+unPropertyise(sv)+" as v make "+tgt+"."+unPropertyise(tv)+" = translate(v, \""+url+"\", \"code\")\r\n");
                return;
              }
              reportError("Unhandled Assignment of enum : "+expr.toString());
            } else {
              String type = pm1.getName().substring(7);
              String sv = findSrcGet(pm1.getArgs().get(0), iter);
              if (sv != null && sv.startsWith("get")) {
                String vs = "vs"+Integer.toString(indent);
                String vt = "vt"+Integer.toString(indent);
                b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"\" : for "+src+"."+unPropertyise(sv)+" as "+vs+" make "+tgt+"."+unPropertyise(tv)+" as "+vt+" then {\r\n"+processAnonymous(indent, vs, vt, pm1.getName(), 
                    makeMapContext(context, unPropertyise(sv), unPropertyise(tv)))+Utilities.padLeft("", ' ', indent*2)+"  }\r\n");
                return;
              }
              reportError("Unhandled Assignment of contained type: "+expr.toString());
            }
            return;
          } else {
            if (isEnumConversion(pm1.getName(), context)) {
              String sv = findSrcGet(pm1.getArgs().get(0), iter);
              if (sv != null && sv.startsWith("get")) {
                String url = processConceptMap(pm1.getName(), context, unPropertyise(sv), unPropertyise(tv), expr);
                if (url == null)
                  b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"\" : for "+src+"."+unPropertyise(sv)+" as vs make "+tgt+"."+unPropertyise(tv)+" as vt\r\n");
                else
                  b.append(Utilities.padLeft("", ' ', indent*2)+"  \""+context.targetPath+"."+unPropertyise(tv)+"\" : for "+src+"."+unPropertyise(sv)+" as v make "+tgt+"."+unPropertyise(tv)+" = translate(v, \""+url+"\", \"code\")\r\n");
                return;
              }
            } else
              reportError("Unhandled Assignment of something : "+expr.toString());
            return;
          }
        }
      }
    }
    reportError("Unhandled Assignment: "+expr.toString());
  }
 
  private boolean comboIsOk(String srcType, String tgtType) {
    if (srcType.equals("markdown") && tgtType.equals("string"))
      return true;
    if (srcType.equals("string") && tgtType.equals("markdown"))
      return true;
    return false;
  }

  private void reportError(String msg) {
    System.out.println(msg); 
  }

  private String unPropertyise(String t) {
    String s = Utilities.uncapitalize(t.substring(3));
    if (s.equals("class_"))
      return "class";
    return s;
  }

  private MapContext makeMapContext(MapContext context, String sourceName, String targetName) {
    ElementDefinition eds = getDefinition(context.sourceContext, context.sourceType, context.sourcePath+"."+sourceName, false, null);
    String stype = getType(eds);
    String sp = stype == null ? context.sourcePath+"."+sourceName : stype;
    ElementDefinition edt = getDefinition(context.targetContext, context.targetType, context.targetPath+"."+targetName, false, null);
    String ttype = getType(edt);
    String tp = ttype == null ? context.targetPath+"."+targetName : ttype;
    return new MapContext(context.forwards, context.sourceContext, stype == null ? context.sourceType : stype, sp, context.targetContext, ttype == null ? context.targetType : ttype, tp); 
  }

  private String getType(ElementDefinition edt) {
    if (edt.getType().size() != 1)
      return null;
    String t = edt.getType().get(0).getCode();
    if (Utilities.existsInList(t, "Element", "BackboneElement"))
      return null;
    return t;
  }

  private List<String> getPossibleTypes(SimpleWorkerContext context, String type, String path, Node n) {
    ElementDefinition eds = getDefinition(context, type, path, true, n);
    List<String> res = new ArrayList<String>();
    for (TypeRefComponent tr : eds.getType()) {
      if (!res.contains(tr.getCode()))
        res.add(tr.getCode());
    }
    return res;
  }

  private String getSpecifiedType(SimpleWorkerContext context, String type, String path, Node n) {
    ElementDefinition eds = getDefinition(context, type, path, true, n);
    List<String> res = new ArrayList<String>();
    for (TypeRefComponent tr : eds.getType()) {
      if (!res.contains(tr.getCode()))
        res.add(tr.getCode());
    }
    if (res.size() > 1)
      throw new Error("Multiple types");
    return res.get(0);
  }

  private String processConceptMap(String name, MapContext context, String srcProp, String tgtProp, Node ne) throws FHIRException {
    ElementDefinition eds = getDefinition(context.sourceContext, context.sourceType, context.sourcePath+"."+srcProp, false, ne);
    ElementDefinition edt = getDefinition(context.targetContext, context.targetType, context.targetPath+"."+tgtProp, false, ne);
    
    List<CodeMap> translations = new ArrayList<CodeMap>();
    ValueSet src = getValueSet(context.sourceContext, eds);    
    ValueSet tgt = getValueSet(context.targetContext, edt);
    boolean exact = true;
    MethodDeclaration meth = context.forwards ? methodsFwds.get(name) : methodsBack.get(name);
    for (Node n : meth.getBody().getChildrenNodes()) {
      if (n instanceof SwitchStmt) {
        SwitchStmt ss = (SwitchStmt) n;
        for (SwitchEntryStmt cs : ss.getEntries()) {
          if (cs.getStmts().size() == 1 && cs.getLabel() != null &&  cs.getStmts().get(0) instanceof ReturnStmt) {
            String lblSrc = cs.getLabel().toString();
            String lblTgt = tail(cs.getStmts().get(0).toString());
            ValueSetExpansionContainsComponent ccSrc = findLabel(lblSrc, src, context.sourceContext.getName());
            ValueSetExpansionContainsComponent ccTgt = findLabel(lblTgt, tgt, context.targetContext.getName());
            translations.add(new CodeMap(ccSrc, ccTgt));
            if (!ccSrc.getCode().equals(ccTgt.getCode()))
              exact = false;
          }
        }
      }
    }
    if (exact)
      return null;
    Set<String> mapPairs = new HashSet<String>();
    for (CodeMap cm : translations)
      mapPairs.add(cm.src.getSystem()+"|"+cm.tgt.getSystem());
    
    StringBuilder b = new StringBuilder();
    b.append("conceptmap \""+tgt.getName()+"\" {\r\n");
    for (String pair : mapPairs) {
      String[] urls = pair.split("\\|");
      b.append("  prefix s = \""+urls[0]+"\"\r\n");
      b.append("  prefix t = \""+urls[1]+"\"\r\n");
      b.append("\r\n");
      for (CodeMap cm : translations) {
        if (cm.src.getSystem().equals(urls[0]) && cm.tgt.getSystem().equals(urls[1])) {
          b.append("  s:"+cm.src.getCode()+" - t:"+cm.tgt.getCode()+"\r\n");
        }
      }
    }
    b.append("}\r\n");
    maps = maps + b.toString();
    return "#"+tgt.getName();    
  }

  private ValueSetExpansionContainsComponent findLabel(String label, ValueSet vs, String cn) {
    CommaSeparatedStringBuilder b = new CommaSeparatedStringBuilder();
    for (ValueSetExpansionContainsComponent cc : vs.getExpansion().getContains()) {
      b.append(cc.getCode());
      if (matches(cc.getCode(), label))
        return cc;
    }
    throw new Error("no match for "+label+" in "+b.toString()+" in "+cn);
  }

  
  private boolean matches(String code, String label) {
    if (code.equalsIgnoreCase(label))
      return true;
    if (code.replace("-", "").equalsIgnoreCase(label))
      return true;
    if (code.equals("<") && label.equals("LESS_THAN"))
      return true;
    if (code.equals("<=") && label.equals("LESS_OR_EQUAL"))
      return true;
    if (code.equals(">=") && label.equals("GREATER_OR_EQUAL"))
      return true;
    if (code.equals(">") && label.equals("GREATER_THAN"))
      return true;
    return false;
  }

  private String tail(String string) {
    if (string.endsWith(";"))
      string = string.substring(0,  string.length()-1);
    return string.substring(string.lastIndexOf(".")+1);
  }

  private String utail(String string) {
    return string.substring(string.lastIndexOf("/")+1);
  }

  private ValueSet getValueSet(SimpleWorkerContext ctxt, ElementDefinition ed) throws FHIRException {
    if (!ed.hasBinding())
      throw new Error("Attempt to get value set for element with no binding "+ed.getPath());
    ValueSet vs = ctxt.fetchResource(ValueSet.class, ed.getBinding().getValueSet());
    if (vs == null)
      throw new Error("Unable to get value set for element "+ed.getPath()+" for "+ed.getBinding().getValueSet());
    ValueSetExpansionOutcome vse = ctxt.expandVS(vs, true, false);
    if (vse.getValueset() == null)
      throw new Error("Unable to expand value set for element "+ed.getPath()+", url = "+vs.getUrl()+" in ctxt "+ctxt.getName());
    return vse.getValueset();
  }

  private ElementDefinition getDefinition(SimpleWorkerContext ctxt, String type, String path, boolean canBePolyMorphic, Node n) {
    StructureDefinition sd = ctxt.fetchTypeDefinition(type);
    if (sd == null)
      throw new Error("Unable to find type "+type);
    if (sd.getDerivation() == TypeDerivationRule.CONSTRAINT) {
      path = utail(sd.getBaseDefinition())+path.substring(type.length());
    }
      
    for (ElementDefinition ed : sd.getSnapshot().getElement()) {
      if (ed.getPath().equals(path) || (canBePolyMorphic && ed.getPath().equals(path+"[x]")))
        return ed;
    }
    throw new Error("Unable to find path "+path+" in "+sd.getType()+" in context "+ctxt.getName()+", called from "+n.toString());
  }

  private String processAnonymous(int indent, String src, String tgt, String name, MapContext context) throws FHIRException {
    StringBuilder b = new StringBuilder();
    MethodDeclaration meth = context.forwards ? methodsFwds.get(name) : methodsBack.get(name);
    if (meth != null)
      processMethod(indent+1, src, tgt, b, meth, context);
    
    return b.toString();
  }

  private String findSrcGet(Expression pp1, IterContext iter) {
    if (pp1 instanceof MethodCallExpr) {
      MethodCallExpr ppm1 = (MethodCallExpr) pp1;
      if (ppm1.getScope() != null && "src".equals(iter.patch(ppm1.getScope().toString()))) {
        return ppm1.getName();
      }
    }
    if (pp1 instanceof NameExpr && iter.iterVariable != null) {
      NameExpr ne = (NameExpr) pp1;
      if (ne.toString().equals(iter.iterVariable)) {
        String s = iter.iterExpression.substring(4);
        if (s.endsWith("()"))
          s = s.substring(0,  s.length()-2);
        return s;
      }
    }
    return null;
  }

  private boolean isEnumConversion(String name, MapContext context) {
    MethodDeclaration meth = context.forwards ? methodsFwds.get(name) : methodsBack.get(name);
    if (meth == null)
      return false;
    for (Node n : meth.getBody().getChildrenNodes()) {
      if (n instanceof SwitchStmt)
        return true;
    }
    return false;
  }

  private boolean isDataType(String type) {
    for (StructureDefinition sd : contextR4.allStructures()) {
      if (sd.getType().equals(type) && sd.getKind() == StructureDefinitionKind.COMPLEXTYPE && sd.getDerivation() == TypeDerivationRule.SPECIALIZATION) 
        return true;
    }
    return false;
  }

}

