package org.hl7.fhir.dstu3.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.dstu3.model.ExpressionNode;
import org.hl7.fhir.dstu3.model.ExpressionNode.CollectionStatus;
import org.hl7.fhir.dstu3.model.ExpressionNode.Function;
import org.hl7.fhir.dstu3.model.ExpressionNode.Kind;
import org.hl7.fhir.dstu3.model.ExpressionNode.Operation;
import org.hl7.fhir.dstu3.model.ExpressionNode.SourceLocation;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Property;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.StructureDefinition.StructureDefinitionKind;
import org.hl7.fhir.dstu3.model.StructureDefinition.TypeDerivationRule;
import org.hl7.fhir.dstu3.model.TemporalPrecisionEnum;
import org.hl7.fhir.dstu3.model.TimeType;
import org.hl7.fhir.dstu3.model.TypeDetails;
import org.hl7.fhir.dstu3.model.TypeDetails.ProfiledType;
import org.hl7.fhir.dstu3.utils.FHIRLexer.FHIRLexerException;
import org.hl7.fhir.dstu3.utils.FHIRPathEngine.IEvaluationContext.FunctionDetails;
import org.hl7.fhir.exceptions.DefinitionException;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.exceptions.PathEngineException;
import org.hl7.fhir.exceptions.UcumException;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.ucum.Decimal;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.util.ElementUtil;

/**
 * 
 * @author Grahame Grieve
 *
 */
public class FHIRPathEngine {
  private IWorkerContext worker;
  private IEvaluationContext hostServices;
  private StringBuilder log = new StringBuilder();
  private Set<String> primitiveTypes = new HashSet<String>();
  private Map<String, StructureDefinition> allTypes = new HashMap<String, StructureDefinition>();

  // if the fhir path expressions are allowed to use constants beyond those defined in the specification
  // the application can implement them by providing a constant resolver 
  public interface IEvaluationContext {
    public class FunctionDetails {
      private String description;
      private int minParameters;
      private int maxParameters;
      public FunctionDetails(String description, int minParameters, int maxParameters) {
        super();
        this.description = description;
        this.minParameters = minParameters;
        this.maxParameters = maxParameters;
      }
      public String getDescription() {
        return description;
      }
      public int getMinParameters() {
        return minParameters;
      }
      public int getMaxParameters() {
        return maxParameters;
      }

    }

    /**
     * A constant reference - e.g. a reference to a name that must be resolved in context.
     * The % will be removed from the constant name before this is invoked.
     * 
     * This will also be called if the host invokes the FluentPath engine with a context of null
     *  
     * @param appContext - content passed into the fluent path engine
     * @param name - name reference to resolve
     * @return the value of the reference (or null, if it's not valid, though can throw an exception if desired)
     */
    public Base resolveConstant(Object appContext, String name)  throws PathEngineException;
    public TypeDetails resolveConstantType(Object appContext, String name) throws PathEngineException;
    
    /**
     * when the .log() function is called
     * 
     * @param argument
     * @param focus
     * @return
     */
    public boolean log(String argument, List<Base> focus);

    // extensibility for functions
    /**
     * 
     * @param functionName
     * @return null if the function is not known
     */
    public FunctionDetails resolveFunction(String functionName);
    
    /**
     * Check the function parameters, and throw an error if they are incorrect, or return the type for the function
     * @param functionName
     * @param parameters
     * @return
     */
    public TypeDetails checkFunction(Object appContext, String functionName, List<TypeDetails> parameters) throws PathEngineException;
    
    /**
     * @param appContext
     * @param functionName
     * @param parameters
     * @return
     */
    public List<Base> executeFunction(Object appContext, String functionName, List<List<Base>> parameters);
    
    /**
     * Implementation of resolve() function. Passed a string, return matching resource, if one is known - else null
     * @param appInfo
     * @param url
     * @return
     */
    public Base resolveReference(Object appContext, String url);
  }


  /**
   * @param worker - used when validating paths (@check), and used doing value set membership when executing tests (once that's defined)
   */
  public FHIRPathEngine(IWorkerContext worker) {
    super();
    this.worker = worker;
    for (StructureDefinition sd : worker.allStructures()) {
      if (sd.getDerivation() == TypeDerivationRule.SPECIALIZATION)
        allTypes.put(sd.getName(), sd);
      if (sd.getDerivation() == TypeDerivationRule.SPECIALIZATION && sd.getKind() == StructureDefinitionKind.PRIMITIVETYPE) {
        primitiveTypes.add(sd.getName());
      }
    }
  }


  // --- 3 methods to override in children -------------------------------------------------------
  // if you don't override, it falls through to the using the base reference implementation 
  // HAPI overrides to these to support extending the base model

  public IEvaluationContext getHostServices() {
    return hostServices;
  }


  public void setHostServices(IEvaluationContext constantResolver) {
    this.hostServices = constantResolver;
  }


  /**
   * Given an item, return all the children that conform to the pattern described in name
   * 
   * Possible patterns:
   *  - a simple name (which may be the base of a name with [] e.g. value[x])
   *  - a name with a type replacement e.g. valueCodeableConcept
   *  - * which means all children
   *  - ** which means all descendants
   *  
   * @param item
   * @param name
   * @param result
	 * @throws FHIRException 
   */
  protected void getChildrenByName(Base item, String name, List<Base> result) throws FHIRException {
  	Base[] list = item.listChildrenByName(name, false);
  	if (list != null)
  		for (Base v : list)
      if (v != null)
        result.add(v);
  }

  // --- public API -------------------------------------------------------
  /**
   * Parse a path for later use using execute
   * 
   * @param path
   * @return
   * @throws PathEngineException 
   * @throws Exception
   */
  public ExpressionNode parse(String path) throws FHIRLexerException {
    FHIRLexer lexer = new FHIRLexer(path);
    if (lexer.done())
      throw lexer.error("Path cannot be empty");
    ExpressionNode result = parseExpression(lexer, true);
    if (!lexer.done())
      throw lexer.error("Premature ExpressionNode termination at unexpected token \""+lexer.getCurrent()+"\"");
    result.check();
    return result;    
  }

  /**
   * Parse a path that is part of some other syntax
   *  
   * @param path
   * @return
   * @throws PathEngineException 
   * @throws Exception
   */
  public ExpressionNode parse(FHIRLexer lexer) throws FHIRLexerException {
    ExpressionNode result = parseExpression(lexer, true);
    result.check();
    return result;    
  }

  /**
   * check that paths referred to in the ExpressionNode are valid
   * 
   * xPathStartsWithValueRef is a hack work around for the fact that FHIR Path sometimes needs a different starting point than the xpath
   * 
   * returns a list of the possible types that might be returned by executing the ExpressionNode against a particular context
   * 
   * @param context - the logical type against which this path is applied
   * @param path - the FHIR Path statement to check
   * @throws DefinitionException 
   * @throws PathEngineException 
   * @if the path is not valid
   */
  public TypeDetails check(Object appContext, String resourceType, String context, ExpressionNode expr) throws FHIRLexerException, PathEngineException, DefinitionException {
    // if context is a path that refers to a type, do that conversion now 
	TypeDetails types; 
	if (context == null) {
	  types = null; // this is a special case; the first path reference will have to resolve to something in the context
	} else if (!context.contains(".")) {
    StructureDefinition sd = worker.fetchResource(StructureDefinition.class, context);
	  types = new TypeDetails(CollectionStatus.SINGLETON, sd.getUrl());
	} else {
	  String ctxt = context.substring(0, context.indexOf('.'));
      if (Utilities.isAbsoluteUrl(resourceType)) {
        ctxt = resourceType.substring(0, resourceType.lastIndexOf("/")+1)+ctxt;
      }
	  StructureDefinition sd = worker.fetchResource(StructureDefinition.class, ctxt);
	  if (sd == null) 
	    throw new PathEngineException("Unknown context "+context);
	  ElementDefinitionMatch ed = getElementDefinition(sd, context, true);
	  if (ed == null) 
	    throw new PathEngineException("Unknown context element "+context);
	  if (ed.fixedType != null) 
	    types = new TypeDetails(CollectionStatus.SINGLETON, ed.fixedType);
	  else if (ed.getDefinition().getType().isEmpty() || isAbstractType(ed.getDefinition().getType())) 
	    types = new TypeDetails(CollectionStatus.SINGLETON, ctxt+"#"+context);
	  else {
	    types = new TypeDetails(CollectionStatus.SINGLETON);
		for (TypeRefComponent t : ed.getDefinition().getType()) 
		  types.addType(t.getCode());
	  }
	}

    return executeType(new ExecutionTypeContext(appContext, resourceType, context, types), types, expr, true);
  }

  public TypeDetails check(Object appContext, StructureDefinition sd, String context, ExpressionNode expr) throws FHIRLexerException, PathEngineException, DefinitionException {
    // if context is a path that refers to a type, do that conversion now 
    TypeDetails types; 
    if (!context.contains(".")) {
      types = new TypeDetails(CollectionStatus.SINGLETON, sd.getUrl());
    } else {
      ElementDefinitionMatch ed = getElementDefinition(sd, context, true);
      if (ed == null) 
        throw new PathEngineException("Unknown context element "+context);
      if (ed.fixedType != null) 
        types = new TypeDetails(CollectionStatus.SINGLETON, ed.fixedType);
      else if (ed.getDefinition().getType().isEmpty() || isAbstractType(ed.getDefinition().getType())) 
        types = new TypeDetails(CollectionStatus.SINGLETON, sd.getUrl()+"#"+context);
      else {
        types = new TypeDetails(CollectionStatus.SINGLETON);
        for (TypeRefComponent t : ed.getDefinition().getType()) 
          types.addType(t.getCode());
      }
    }

    return executeType(new ExecutionTypeContext(appContext, sd.getUrl(), context, types), types, expr, true);
  }

  public TypeDetails check(Object appContext, StructureDefinition sd, ExpressionNode expr) throws FHIRLexerException, PathEngineException, DefinitionException {
    // if context is a path that refers to a type, do that conversion now 
    TypeDetails types = null; // this is a special case; the first path reference will have to resolve to something in the context
    return executeType(new ExecutionTypeContext(appContext, sd == null ? null : sd.getUrl(), null, types), types, expr, true);
  }

  public TypeDetails check(Object appContext, String resourceType, String context, String expr) throws FHIRLexerException, PathEngineException, DefinitionException {
    return check(appContext, resourceType, context, parse(expr));
  }


  /**
   * evaluate a path and return the matching elements
   * 
   * @param base - the object against which the path is being evaluated
   * @param ExpressionNode - the parsed ExpressionNode statement to use
   * @return
   * @throws FHIRException 
   * @
   */
	public List<Base> evaluate(Base base, ExpressionNode ExpressionNode) throws FHIRException {
    List<Base> list = new ArrayList<Base>();
    if (base != null)
      list.add(base);
    log = new StringBuilder();
    return execute(new ExecutionContext(null, base != null && base.isResource() ? base : null, base, null, base), list, ExpressionNode, true);
  }

  /**
   * evaluate a path and return the matching elements
   * 
   * @param base - the object against which the path is being evaluated
   * @param path - the FHIR Path statement to use
   * @return
	 * @throws FHIRException 
   * @
   */
	public List<Base> evaluate(Base base, String path) throws FHIRException {
    ExpressionNode exp = parse(path);
    List<Base> list = new ArrayList<Base>();
    if (base != null)
      list.add(base);
    log = new StringBuilder();
    return execute(new ExecutionContext(null, base.isResource() ? base : null, base, null, base), list, exp, true);
  }

  /**
   * evaluate a path and return the matching elements
   * 
   * @param base - the object against which the path is being evaluated
   * @param ExpressionNode - the parsed ExpressionNode statement to use
   * @return
	 * @throws FHIRException 
   * @
   */
	public List<Base> evaluate(Object appContext, Resource resource, Base base, ExpressionNode ExpressionNode) throws FHIRException {
    List<Base> list = new ArrayList<Base>();
    if (base != null)
      list.add(base);
    log = new StringBuilder();
    return execute(new ExecutionContext(appContext, resource, base, null, base), list, ExpressionNode, true);
  }

  /**
   * evaluate a path and return the matching elements
   * 
   * @param base - the object against which the path is being evaluated
   * @param ExpressionNode - the parsed ExpressionNode statement to use
   * @return
   * @throws FHIRException 
   * @
   */
  public List<Base> evaluate(Object appContext, Base resource, Base base, ExpressionNode ExpressionNode) throws FHIRException {
    List<Base> list = new ArrayList<Base>();
    if (base != null)
      list.add(base);
    log = new StringBuilder();
    return execute(new ExecutionContext(appContext, resource, base, null, base), list, ExpressionNode, true);
  }

  /**
   * evaluate a path and return the matching elements
   * 
   * @param base - the object against which the path is being evaluated
   * @param path - the FHIR Path statement to use
   * @return
	 * @throws FHIRException 
   * @
   */
	public List<Base> evaluate(Object appContext, Resource resource, Base base, String path) throws FHIRException {
    ExpressionNode exp = parse(path);
    List<Base> list = new ArrayList<Base>();
    if (base != null)
      list.add(base);
    log = new StringBuilder();
    return execute(new ExecutionContext(appContext, resource, base, null, base), list, exp, true);
  }

  /**
   * evaluate a path and return true or false (e.g. for an invariant)
   * 
   * @param base - the object against which the path is being evaluated
   * @param path - the FHIR Path statement to use
   * @return
	 * @throws FHIRException 
   * @
   */
	public boolean evaluateToBoolean(Resource resource, Base base, String path) throws FHIRException {
    return convertToBoolean(evaluate(null, resource, base, path));
  }

  /**
   * evaluate a path and return true or false (e.g. for an invariant)
   * 
   * @param base - the object against which the path is being evaluated
   * @param path - the FHIR Path statement to use
   * @return
   * @throws FHIRException 
   * @
   */
  public boolean evaluateToBoolean(Resource resource, Base base, ExpressionNode node) throws FHIRException {
    return convertToBoolean(evaluate(null, resource, base, node));
  }

  /**
   * evaluate a path and return true or false (e.g. for an invariant)
   * 
   * @param appinfo - application context
   * @param base - the object against which the path is being evaluated
   * @param path - the FHIR Path statement to use
   * @return
   * @throws FHIRException 
   * @
   */
  public boolean evaluateToBoolean(Object appInfo, Resource resource, Base base, ExpressionNode node) throws FHIRException {
    return convertToBoolean(evaluate(appInfo, resource, base, node));
  }

  /**
   * evaluate a path and return true or false (e.g. for an invariant)
   * 
   * @param base - the object against which the path is being evaluated
   * @param path - the FHIR Path statement to use
   * @return
   * @throws FHIRException 
   * @
   */
  public boolean evaluateToBoolean(Base resource, Base base, ExpressionNode node) throws FHIRException {
    return convertToBoolean(evaluate(null, resource, base, node));
  }

  /**
   * evaluate a path and a string containing the outcome (for display)
   * 
   * @param base - the object against which the path is being evaluated
   * @param path - the FHIR Path statement to use
   * @return
	 * @throws FHIRException 
   * @
   */
  public String evaluateToString(Base base, String path) throws FHIRException {
    return convertToString(evaluate(base, path));
  }

  public String evaluateToString(Object appInfo, Base resource, Base base, ExpressionNode node) throws FHIRException {
    return convertToString(evaluate(appInfo, resource, base, node));
  }

  /**
   * worker routine for converting a set of objects to a string representation
   * 
   * @param items - result from @evaluate
   * @return
   */
  public String convertToString(List<Base> items) {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (Base item : items) {
      if (first) 
        first = false;
      else
        b.append(',');

      b.append(convertToString(item));
    }
    return b.toString();
  }

  private String convertToString(Base item) {
    if (item.isPrimitive())
      return item.primitiveValue();
    else 
      return item.toString();
  }

  /**
   * worker routine for converting a set of objects to a boolean representation (for invariants)
   * 
   * @param items - result from @evaluate
   * @return
   */
  public boolean convertToBoolean(List<Base> items) {
    if (items == null)
      return false;
    else if (items.size() == 1 && items.get(0) instanceof BooleanType)
      return ((BooleanType) items.get(0)).getValue();
    else 
      return items.size() > 0;
  }


  private void log(String name, List<Base> contents) {
    if (hostServices == null || !hostServices.log(name, contents)) {
      if (log.length() > 0)
        log.append("; ");
      log.append(name);
      log.append(": ");
      boolean first = true;
      for (Base b : contents) {
        if (first)
          first = false;
        else
          log.append(",");
        log.append(convertToString(b));
      }
    }
  }

  public String forLog() {
    if (log.length() > 0)
      return " ("+log.toString()+")";
    else
      return "";
  }

  private class ExecutionContext {
    private Object appInfo;
    private Base resource;
    private Base context;
    private Base thisItem;
    private Map<String, Base> aliases;
    
    public ExecutionContext(Object appInfo, Base resource, Base context, Map<String, Base> aliases, Base thisItem) {
      this.appInfo = appInfo;
      this.context = context;
      this.resource = resource; 
      this.aliases = aliases;
      this.thisItem = thisItem;
    }
    public Base getResource() {
      return resource;
    }
    public Base getThisItem() {
      return thisItem;
    }
    public void addAlias(String name, List<Base> focus) throws FHIRException {
      if (aliases == null)
        aliases = new HashMap<String, Base>();
      else
        aliases = new HashMap<String, Base>(aliases); // clone it, since it's going to change 
      if (focus.size() > 1)
        throw new FHIRException("Attempt to alias a collection, not a singleton");
      aliases.put(name, focus.size() == 0 ? null : focus.get(0));      
    }
    public Base getAlias(String name) {
      return aliases == null ? null : aliases.get(name);
    }
  }

  private class ExecutionTypeContext {
    private Object appInfo; 
    private String resource;
    private String context;
    private TypeDetails thisItem;


    public ExecutionTypeContext(Object appInfo, String resource, String context, TypeDetails thisItem) {
      super();
      this.appInfo = appInfo;
      this.resource = resource;
      this.context = context;
      this.thisItem = thisItem;
      
    }
    public String getResource() {
      return resource;
    }
    public TypeDetails getThisItem() {
      return thisItem;
    }
  }

  private ExpressionNode parseExpression(FHIRLexer lexer, boolean proximal) throws FHIRLexerException {
    ExpressionNode result = new ExpressionNode(lexer.nextId());
    SourceLocation c = lexer.getCurrentStartLocation();
    result.setStart(lexer.getCurrentLocation());
    // special:
    if (lexer.getCurrent().equals("-")) {
      lexer.take();
      lexer.setCurrent("-"+lexer.getCurrent());
    }
    if (lexer.getCurrent().equals("+")) {
      lexer.take();
      lexer.setCurrent("+"+lexer.getCurrent());
    }
    if (lexer.isConstant(false)) {
      checkConstant(lexer.getCurrent(), lexer);
      result.setConstant(lexer.take());
      result.setKind(Kind.Constant);
      result.setEnd(lexer.getCurrentLocation());
    } else if ("(".equals(lexer.getCurrent())) {
      lexer.next();
      result.setKind(Kind.Group);
      result.setGroup(parseExpression(lexer, true));
      if (!")".equals(lexer.getCurrent())) 
        throw lexer.error("Found "+lexer.getCurrent()+" expecting a \")\"");
      result.setEnd(lexer.getCurrentLocation());
      lexer.next();
    } else {
      if (!lexer.isToken() && !lexer.getCurrent().startsWith("\"")) 
        throw lexer.error("Found "+lexer.getCurrent()+" expecting a token name");
      if (lexer.getCurrent().startsWith("\""))
        result.setName(lexer.readConstant("Path Name"));
      else
        result.setName(lexer.take());
      result.setEnd(lexer.getCurrentLocation());
      if (!result.checkName())
        throw lexer.error("Found "+result.getName()+" expecting a valid token name");
      if ("(".equals(lexer.getCurrent())) {
        Function f = Function.fromCode(result.getName());
        FunctionDetails details = null;
        if (f == null) {
          if (hostServices != null)
            details = hostServices.resolveFunction(result.getName());
          if (details == null)
            throw lexer.error("The name "+result.getName()+" is not a valid function name");
          f = Function.Custom;
        }
        result.setKind(Kind.Function);
        result.setFunction(f);
        lexer.next();
        while (!")".equals(lexer.getCurrent())) { 
          result.getParameters().add(parseExpression(lexer, true));
          if (",".equals(lexer.getCurrent()))
            lexer.next();
          else if (!")".equals(lexer.getCurrent()))
            throw lexer.error("The token "+lexer.getCurrent()+" is not expected here - either a \",\" or a \")\" expected");
        }
        result.setEnd(lexer.getCurrentLocation());
        lexer.next();
        checkParameters(lexer, c, result, details);
      } else
        result.setKind(Kind.Name);
    }
    ExpressionNode focus = result;
    if ("[".equals(lexer.getCurrent())) {
      lexer.next();
      ExpressionNode item = new ExpressionNode(lexer.nextId());
      item.setKind(Kind.Function);
      item.setFunction(ExpressionNode.Function.Item);
      item.getParameters().add(parseExpression(lexer, true));
      if (!lexer.getCurrent().equals("]"))
        throw lexer.error("The token "+lexer.getCurrent()+" is not expected here - a \"]\" expected");
      lexer.next();
      result.setInner(item);
      focus = item;
    }
    if (".".equals(lexer.getCurrent())) {
      lexer.next();
      focus.setInner(parseExpression(lexer, false));
    }
    result.setProximal(proximal);
    if (proximal) {
      while (lexer.isOp()) {
        focus.setOperation(ExpressionNode.Operation.fromCode(lexer.getCurrent()));
        focus.setOpStart(lexer.getCurrentStartLocation());
        focus.setOpEnd(lexer.getCurrentLocation());
        lexer.next();
        focus.setOpNext(parseExpression(lexer, false));
        focus = focus.getOpNext();
      }
      result = organisePrecedence(lexer, result);
    }
    return result;
  }

  private ExpressionNode organisePrecedence(FHIRLexer lexer, ExpressionNode node) {
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Times, Operation.DivideBy, Operation.Div, Operation.Mod)); 
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Plus, Operation.Minus, Operation.Concatenate)); 
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Union)); 
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.LessThen, Operation.Greater, Operation.LessOrEqual, Operation.GreaterOrEqual));
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Is));
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Equals, Operation.Equivalent, Operation.NotEquals, Operation.NotEquivalent));
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.And));
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Xor, Operation.Or));
    // last: implies
    return node;
  }

  private ExpressionNode gatherPrecedence(FHIRLexer lexer, ExpressionNode start, EnumSet<Operation> ops) {
    //	  work : boolean;
    //	  focus, node, group : ExpressionNode;

    assert(start.isProximal());

    // is there anything to do?
    boolean work = false;
    ExpressionNode focus = start.getOpNext();
    if (ops.contains(start.getOperation())) {
      while (focus != null && focus.getOperation() != null) {
        work = work || !ops.contains(focus.getOperation());
        focus = focus.getOpNext();
      }
    } else {
      while (focus != null && focus.getOperation() != null) {
        work = work || ops.contains(focus.getOperation());
        focus = focus.getOpNext();
      }
    }  
    if (!work)
      return start;

    // entry point: tricky
    ExpressionNode group;
    if (ops.contains(start.getOperation())) {
      group = newGroup(lexer, start);
      group.setProximal(true);
      focus = start;
      start = group;
    } else {
      ExpressionNode node = start;

      focus = node.getOpNext();
      while (!ops.contains(focus.getOperation())) {
        node = focus;
        focus = focus.getOpNext();
      }
      group = newGroup(lexer, focus);
      node.setOpNext(group);
    }

    // now, at this point:
    //   group is the group we are adding to, it already has a .group property filled out.
    //   focus points at the group.group
    do {
      // run until we find the end of the sequence
      while (ops.contains(focus.getOperation()))
        focus = focus.getOpNext();
      if (focus.getOperation() != null) {
        group.setOperation(focus.getOperation());
        group.setOpNext(focus.getOpNext());
        focus.setOperation(null);
        focus.setOpNext(null);
        // now look for another sequence, and start it
        ExpressionNode node = group;
        focus = group.getOpNext();
        if (focus != null) { 
          while (focus != null && !ops.contains(focus.getOperation())) {
            node = focus;
            focus = focus.getOpNext();
          }
          if (focus != null) { // && (focus.Operation in Ops) - must be true 
            group = newGroup(lexer, focus);
            node.setOpNext(group);
          }
        }
      }
    }
    while (focus != null && focus.getOperation() != null);
    return start;
  }


  private ExpressionNode newGroup(FHIRLexer lexer, ExpressionNode next) {
    ExpressionNode result = new ExpressionNode(lexer.nextId());
    result.setKind(Kind.Group);
    result.setGroup(next);
    result.getGroup().setProximal(true);
    return result;
  }

  private void checkConstant(String s, FHIRLexer lexer) throws FHIRLexerException {
    if (s.startsWith("\'") && s.endsWith("\'")) {
      int i = 1;
      while (i < s.length()-1) {
        char ch = s.charAt(i);
        if (ch == '\\') {
          switch (ch) {
          case 't': 
          case 'r':
          case 'n': 
          case 'f': 
          case '\'':
          case '\\': 
          case '/': 
            i++; 
            break;
          case 'u':
            if (!Utilities.isHex("0x"+s.substring(i, i+4)))
              throw lexer.error("Improper unicode escape \\u"+s.substring(i, i+4));
            break;
          default:
            throw lexer.error("Unknown character escape \\"+ch);
          }
        } else
          i++;
      }
    }
  }

  //  procedure CheckParamCount(c : integer);
  //  begin
  //    if exp.Parameters.Count <> c then
  //      raise lexer.error('The function "'+exp.name+'" requires '+inttostr(c)+' parameters', offset);
  //  end;

  private boolean checkParamCount(FHIRLexer lexer, SourceLocation location, ExpressionNode exp, int count) throws FHIRLexerException {
    if (exp.getParameters().size() != count)
      throw lexer.error("The function \""+exp.getName()+"\" requires "+Integer.toString(count)+" parameters", location.toString());
    return true;
  }

  private boolean checkParamCount(FHIRLexer lexer, SourceLocation location, ExpressionNode exp, int countMin, int countMax) throws FHIRLexerException {
    if (exp.getParameters().size() < countMin || exp.getParameters().size() > countMax)
      throw lexer.error("The function \""+exp.getName()+"\" requires between "+Integer.toString(countMin)+" and "+Integer.toString(countMax)+" parameters", location.toString());
    return true;
  }

  private boolean checkParameters(FHIRLexer lexer, SourceLocation location, ExpressionNode exp, FunctionDetails details) throws FHIRLexerException {
    switch (exp.getFunction()) {
    case Empty: return checkParamCount(lexer, location, exp, 0);
    case Not: return checkParamCount(lexer, location, exp, 0);
    case Exists: return checkParamCount(lexer, location, exp, 0);
    case SubsetOf: return checkParamCount(lexer, location, exp, 1);
    case SupersetOf: return checkParamCount(lexer, location, exp, 1);
    case IsDistinct: return checkParamCount(lexer, location, exp, 0);
    case Distinct: return checkParamCount(lexer, location, exp, 0);
    case Count: return checkParamCount(lexer, location, exp, 0);
    case Where: return checkParamCount(lexer, location, exp, 1);
    case Select: return checkParamCount(lexer, location, exp, 1);
    case All: return checkParamCount(lexer, location, exp, 0, 1);
    case Repeat: return checkParamCount(lexer, location, exp, 1);
    case Item: return checkParamCount(lexer, location, exp, 1);
    case As: return checkParamCount(lexer, location, exp, 1);
    case Is: return checkParamCount(lexer, location, exp, 1);
    case Single: return checkParamCount(lexer, location, exp, 0);
    case First: return checkParamCount(lexer, location, exp, 0);
    case Last: return checkParamCount(lexer, location, exp, 0);
    case Tail: return checkParamCount(lexer, location, exp, 0);
    case Skip: return checkParamCount(lexer, location, exp, 1);
    case Take: return checkParamCount(lexer, location, exp, 1);
    case Iif: return checkParamCount(lexer, location, exp, 2,3);
    case ToInteger: return checkParamCount(lexer, location, exp, 0);
    case ToDecimal: return checkParamCount(lexer, location, exp, 0);
    case ToString: return checkParamCount(lexer, location, exp, 0);
    case Substring: return checkParamCount(lexer, location, exp, 1, 2);
    case StartsWith: return checkParamCount(lexer, location, exp, 1);
    case EndsWith: return checkParamCount(lexer, location, exp, 1);
    case Matches: return checkParamCount(lexer, location, exp, 1);
    case ReplaceMatches: return checkParamCount(lexer, location, exp, 2);
    case Contains: return checkParamCount(lexer, location, exp, 1);
    case Replace: return checkParamCount(lexer, location, exp, 2);
    case Length: return checkParamCount(lexer, location, exp, 0);
    case Children: return checkParamCount(lexer, location, exp, 0);
    case Descendants: return checkParamCount(lexer, location, exp, 0);
    case MemberOf: return checkParamCount(lexer, location, exp, 1);
    case Trace: return checkParamCount(lexer, location, exp, 1);
    case Today: return checkParamCount(lexer, location, exp, 0);
    case Now: return checkParamCount(lexer, location, exp, 0);
    case Resolve: return checkParamCount(lexer, location, exp, 0);
    case Extension: return checkParamCount(lexer, location, exp, 1);
    case HasValue: return checkParamCount(lexer, location, exp, 0);
    case Alias: return checkParamCount(lexer, location, exp, 1);
    case AliasAs: return checkParamCount(lexer, location, exp, 1);
    case Custom: return checkParamCount(lexer, location, exp, details.getMinParameters(), details.getMaxParameters());
    }
    return false;
  }

	private List<Base> execute(ExecutionContext context, List<Base> focus, ExpressionNode exp, boolean atEntry) throws FHIRException {
//    System.out.println("Evaluate {'"+exp.toString()+"'} on "+focus.toString());
    List<Base> work = new ArrayList<Base>();
    switch (exp.getKind()) {
    case Name:
      if (atEntry && exp.getName().equals("$this"))
        work.add(context.getThisItem());
      else
        for (Base item : focus) {
          List<Base> outcome = execute(context, item, exp, atEntry);
          for (Base base : outcome)
            if (base != null)
              work.add(base);
        }        		
      break;
    case Function:
      List<Base> work2 = evaluateFunction(context, focus, exp);
      work.addAll(work2);
      break;
    case Constant:
      Base b = processConstant(context, exp.getConstant());
      if (b != null)
        work.add(b);
      break;
    case Group:
      work2 = execute(context, focus, exp.getGroup(), atEntry);
      work.addAll(work2);
    }

    if (exp.getInner() != null)
      work = execute(context, work, exp.getInner(), false);

    if (exp.isProximal() && exp.getOperation() != null) {
      ExpressionNode next = exp.getOpNext();
      ExpressionNode last = exp;
      while (next != null) {
        List<Base> work2 = preOperate(work, last.getOperation());
        if (work2 != null)
          work = work2;
        else if (last.getOperation() == Operation.Is || last.getOperation() == Operation.As) {
          work2 = executeTypeName(context, focus, next, false);
          work = operate(work, last.getOperation(), work2);
        } else {
          work2 = execute(context, focus, next, true);
          work = operate(work, last.getOperation(), work2);
//          System.out.println("Result of {'"+last.toString()+" "+last.getOperation().toCode()+" "+next.toString()+"'}: "+focus.toString());
        }
        last = next;
        next = next.getOpNext();
      }
    }
//    System.out.println("Result of {'"+exp.toString()+"'}: "+work.toString());
    return work;
  }

  private List<Base> executeTypeName(ExecutionContext context, List<Base> focus, ExpressionNode next, boolean atEntry) {
    List<Base> result = new ArrayList<Base>();
    result.add(new StringType(next.getName()));
    return result;
  }


  private List<Base> preOperate(List<Base> left, Operation operation) {
    switch (operation) {
    case And:
      return isBoolean(left, false) ? makeBoolean(false) : null;
    case Or:
      return isBoolean(left, true) ? makeBoolean(true) : null;
    case Implies:
      return convertToBoolean(left) ? null : makeBoolean(true);
    default: 
      return null;
    }
  }

  private List<Base> makeBoolean(boolean b) {
    List<Base> res = new ArrayList<Base>();
    res.add(new BooleanType(b));
    return res;
  }

  private TypeDetails executeTypeName(ExecutionTypeContext context, TypeDetails focus, ExpressionNode exp, boolean atEntry) throws PathEngineException, DefinitionException {
    return new TypeDetails(CollectionStatus.SINGLETON, exp.getName());
  }

  private TypeDetails executeType(ExecutionTypeContext context, TypeDetails focus, ExpressionNode exp, boolean atEntry) throws PathEngineException, DefinitionException {
    TypeDetails result = new TypeDetails(null);
    switch (exp.getKind()) {
    case Name:
      if (atEntry && exp.getName().equals("$this"))
        result.update(context.getThisItem());
      else if (atEntry && focus == null)
        result.update(executeContextType(context, exp.getName()));
      else {
        for (String s : focus.getTypes()) {
          result.update(executeType(s, exp, atEntry));
        }
        if (result.hasNoTypes()) 
          throw new PathEngineException("The name "+exp.getName()+" is not valid for any of the possible types: "+focus.describe());
      }
      break;
    case Function:
      result.update(evaluateFunctionType(context, focus, exp));
      break;
    case Constant:
      result.update(readConstantType(context, exp.getConstant()));
      break;
    case Group:
      result.update(executeType(context, focus, exp.getGroup(), atEntry));
    }
    exp.setTypes(result);

    if (exp.getInner() != null) {
      result = executeType(context, result, exp.getInner(), false);
    }

    if (exp.isProximal() && exp.getOperation() != null) {
      ExpressionNode next = exp.getOpNext();
      ExpressionNode last = exp;
      while (next != null) {
        TypeDetails work;
        if (last.getOperation() == Operation.Is || last.getOperation() == Operation.As)
          work = executeTypeName(context, focus, next, atEntry);
        else
          work = executeType(context, focus, next, atEntry);
        result = operateTypes(result, last.getOperation(), work);
        last = next;
        next = next.getOpNext();
      }
      exp.setOpTypes(result);
    }
    return result;
  }

  private Base processConstant(ExecutionContext context, String constant) throws PathEngineException {
    if (constant.equals("true")) {
      return new BooleanType(true);
    } else if (constant.equals("false")) {
      return new BooleanType(false);
    } else if (constant.equals("{}")) {
      return null;
    } else if (Utilities.isInteger(constant)) {
      return new IntegerType(constant);
    } else if (Utilities.isDecimal(constant)) {
      return new DecimalType(constant);
    } else if (constant.startsWith("\'")) {
      return new StringType(processConstantString(constant));
    } else if (constant.startsWith("%")) {
      return resolveConstant(context, constant);
    } else if (constant.startsWith("@")) {
      return processDateConstant(context.appInfo, constant.substring(1));
    } else {
      return new StringType(constant);
    }
  }

  private Base processDateConstant(Object appInfo, String value) throws PathEngineException {
    if (value.startsWith("T"))
      return new TimeType(value.substring(1));
    String v = value;
    if (v.length() > 10) {
      int i = v.substring(10).indexOf("-");
      if (i == -1)
        i = v.substring(10).indexOf("+");
      if (i == -1)
        i = v.substring(10).indexOf("Z");
      v = i == -1 ? value : v.substring(0,  10+i);
    }
    if (v.length() > 10)
      return new DateTimeType(value);
    else 
      return new DateType(value);
  }


  private Base resolveConstant(ExecutionContext context, String s) throws PathEngineException {
    if (s.equals("%sct"))
      return new StringType("http://snomed.info/sct");
    else if (s.equals("%loinc"))
      return new StringType("http://loinc.org");
    else if (s.equals("%ucum"))
      return new StringType("http://unitsofmeasure.org");
    else if (s.equals("%resource")) {
      if (context.resource == null)
        throw new PathEngineException("Cannot use %resource in this context");
      return context.resource;
    } else if (s.equals("%context")) {
      return context.context;
    } else if (s.equals("%us-zip"))
      return new StringType("[0-9]{5}(-[0-9]{4}){0,1}");
    else if (s.startsWith("%\"vs-"))
      return new StringType("http://hl7.org/fhir/ValueSet/"+s.substring(5, s.length()-1)+"");
    else if (s.startsWith("%\"cs-"))
      return new StringType("http://hl7.org/fhir/"+s.substring(5, s.length()-1)+"");
    else if (s.startsWith("%\"ext-"))
      return new StringType("http://hl7.org/fhir/StructureDefinition/"+s.substring(6, s.length()-1));
    else if (hostServices == null)
      throw new PathEngineException("Unknown fixed constant '"+s+"'");
    else
      return hostServices.resolveConstant(context.appInfo, s.substring(1));
  }


  private String processConstantString(String s) throws PathEngineException {
    StringBuilder b = new StringBuilder();
    int i = 1;
    while (i < s.length()-1) {
      char ch = s.charAt(i);
      if (ch == '\\') {
        i++;
        switch (s.charAt(i)) {
        case 't': 
          b.append('\t');
          break;
        case 'r':
          b.append('\r');
          break;
        case 'n': 
          b.append('\n');
          break;
        case 'f': 
          b.append('\f');
          break;
        case '\'':
          b.append('\'');
          break;
        case '\\': 
          b.append('\\');
          break;
        case '/': 
          b.append('/');
          break;
        case 'u':
          i++;
          int uc = Integer.parseInt(s.substring(i, i+4), 16);
          b.append((char) uc);
          i = i + 3;
          break;
        default:
          throw new PathEngineException("Unknown character escape \\"+s.charAt(i));
        }
        i++;
      } else {
        b.append(ch);
        i++;
      }
    }
    return b.toString();
  }


  private List<Base> operate(List<Base> left, Operation operation, List<Base> right) throws FHIRException {
    switch (operation) {
    case Equals: return opEquals(left, right);
    case Equivalent: return opEquivalent(left, right);
    case NotEquals: return opNotEquals(left, right);
    case NotEquivalent: return opNotEquivalent(left, right);
    case LessThen: return opLessThen(left, right);
    case Greater: return opGreater(left, right);
    case LessOrEqual: return opLessOrEqual(left, right);
    case GreaterOrEqual: return opGreaterOrEqual(left, right);
    case Union: return opUnion(left, right);
    case In: return opIn(left, right);
    case Contains: return opContains(left, right);
    case Or:  return opOr(left, right);
    case And:  return opAnd(left, right);
    case Xor: return opXor(left, right);
    case Implies: return opImplies(left, right);
    case Plus: return opPlus(left, right);
    case Times: return opTimes(left, right);
    case Minus: return opMinus(left, right);
    case Concatenate: return opConcatenate(left, right);
    case DivideBy: return opDivideBy(left, right);
    case Div: return opDiv(left, right);
    case Mod: return opMod(left, right);
    case Is: return opIs(left, right);
    case As: return opAs(left, right);
    default: 
      throw new Error("Not Done Yet: "+operation.toCode());
    }
  }

  private List<Base> opAs(List<Base> left, List<Base> right) {
    List<Base> result = new ArrayList<Base>();
    if (left.size() != 1 || right.size() != 1)
      return result;
    else {
      String tn = convertToString(right);
      if (tn.equals(left.get(0).fhirType()))
        result.add(left.get(0));
    }
    return result;
  }


  private List<Base> opIs(List<Base> left, List<Base> right) {
    List<Base> result = new ArrayList<Base>();
    if (left.size() != 1 || right.size() != 1) 
      result.add(new BooleanType(false));
    else {
      String tn = convertToString(right);
      result.add(new BooleanType(left.get(0).hasType(tn)));
    }
    return result;
  }


  private TypeDetails operateTypes(TypeDetails left, Operation operation, TypeDetails right) {
    switch (operation) {
    case Equals: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Equivalent: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case NotEquals: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case NotEquivalent: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case LessThen: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Greater: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case LessOrEqual: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case GreaterOrEqual: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Is: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case As: return new TypeDetails(CollectionStatus.SINGLETON, right.getTypes());
    case Union: return left.union(right);
    case Or: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case And: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Xor: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Implies : return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Times: 
      TypeDetails result = new TypeDetails(CollectionStatus.SINGLETON);
      if (left.hasType(worker, "integer") && right.hasType(worker, "integer"))
        result.addType("integer");
      else if (left.hasType(worker, "integer", "decimal") && right.hasType(worker, "integer", "decimal"))
        result.addType("decimal");
      return result;
    case DivideBy: 
      result = new TypeDetails(CollectionStatus.SINGLETON);
      if (left.hasType(worker, "integer") && right.hasType(worker, "integer"))
        result.addType("decimal");
      else if (left.hasType(worker, "integer", "decimal") && right.hasType(worker, "integer", "decimal"))
        result.addType("decimal");
      return result;
    case Concatenate:
      result = new TypeDetails(CollectionStatus.SINGLETON, "");
    case Plus:
      result = new TypeDetails(CollectionStatus.SINGLETON);
      if (left.hasType(worker, "integer") && right.hasType(worker, "integer"))
        result.addType("integer");
      else if (left.hasType(worker, "integer", "decimal") && right.hasType(worker, "integer", "decimal"))
        result.addType("decimal");
      else if (left.hasType(worker, "string", "id", "code", "uri") && right.hasType(worker, "string", "id", "code", "uri"))
        result.addType("string");
      return result;
    case Minus:
      result = new TypeDetails(CollectionStatus.SINGLETON);
      if (left.hasType(worker, "integer") && right.hasType(worker, "integer"))
        result.addType("integer");
      else if (left.hasType(worker, "integer", "decimal") && right.hasType(worker, "integer", "decimal"))
        result.addType("decimal");
      return result;
    case Div: 
    case Mod: 
      result = new TypeDetails(CollectionStatus.SINGLETON);
      if (left.hasType(worker, "integer") && right.hasType(worker, "integer"))
        result.addType("integer");
      else if (left.hasType(worker, "integer", "decimal") && right.hasType(worker, "integer", "decimal"))
        result.addType("decimal");
      return result;
    case In: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Contains: return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    default: 
      return null;
    }
  }


  private List<Base> opEquals(List<Base> left, List<Base> right) {
    if (left.size() != right.size())
      return makeBoolean(false);

    boolean res = true;
    for (int i = 0; i < left.size(); i++) {
      if (!doEquals(left.get(i), right.get(i))) { 
        res = false;
        break;
      }
    }
    return makeBoolean(res);
  }

  private List<Base> opNotEquals(List<Base> left, List<Base> right) {
    if (left.size() != right.size())
      return makeBoolean(true);

    boolean res = true;
    for (int i = 0; i < left.size(); i++) {
      if (!doEquals(left.get(i), right.get(i))) { 
        res = false;
        break;
      }
    }
    return makeBoolean(!res);
  }

  private boolean doEquals(Base left, Base right) {
    if (left.isPrimitive() && right.isPrimitive())
			return Base.equals(left.primitiveValue(), right.primitiveValue());
    else
      return Base.compareDeep(left, right, false);
  }

  private boolean doEquivalent(Base left, Base right) throws PathEngineException {
    if (left.hasType("integer") && right.hasType("integer"))
      return doEquals(left, right);
    if (left.hasType("boolean") && right.hasType("boolean"))
      return doEquals(left, right);
    if (left.hasType("integer", "decimal", "unsignedInt", "positiveInt") && right.hasType("integer", "decimal", "unsignedInt", "positiveInt"))
      return Utilities.equivalentNumber(left.primitiveValue(), right.primitiveValue());
    if (left.hasType("date", "dateTime", "time", "instant") && right.hasType("date", "dateTime", "time", "instant"))
      return Utilities.equivalentNumber(left.primitiveValue(), right.primitiveValue());
    if (left.hasType("string", "id", "code", "uri") && right.hasType("string", "id", "code", "uri"))
      return Utilities.equivalent(convertToString(left), convertToString(right));

    throw new PathEngineException(String.format("Unable to determine equivalence between %s and %s", left.fhirType(), right.fhirType()));
  }

  private List<Base> opEquivalent(List<Base> left, List<Base> right) throws PathEngineException {
    if (left.size() != right.size())
      return makeBoolean(false);

    boolean res = true;
    for (int i = 0; i < left.size(); i++) {
      boolean found = false;
      for (int j = 0; j < right.size(); j++) {
        if (doEquivalent(left.get(i), right.get(j))) {
          found = true;
          break;
        }
      }
      if (!found) {
        res = false;
        break;
      }
    }
    return makeBoolean(res);
  }

  private List<Base> opNotEquivalent(List<Base> left, List<Base> right) throws PathEngineException {
    if (left.size() != right.size())
      return makeBoolean(true);

    boolean res = true;
    for (int i = 0; i < left.size(); i++) {
      boolean found = false;
      for (int j = 0; j < right.size(); j++) {
        if (doEquivalent(left.get(i), right.get(j))) {
          found = true;
          break;
        }
      }
      if (!found) {
        res = false;
        break;
      }
    }
    return makeBoolean(!res);
  }

	private List<Base> opLessThen(List<Base> left, List<Base> right) throws FHIRException {
    if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
      Base l = left.get(0);
      Base r = right.get(0);
      if (l.hasType("string") && r.hasType("string")) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) < 0);
      else if ((l.hasType("integer") || l.hasType("decimal")) && (r.hasType("integer") || r.hasType("decimal"))) 
        return makeBoolean(new Double(l.primitiveValue()) < new Double(r.primitiveValue()));
      else if ((l.hasType("date", "dateTime", "instant")) && (r.hasType("date", "dateTime", "instant"))) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) < 0);
      else if ((l.hasType("time")) && (r.hasType("time"))) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) < 0);
    } else if (left.size() == 1 && right.size() == 1 && left.get(0).fhirType().equals("Quantity") && right.get(0).fhirType().equals("Quantity") ) {
      List<Base> lUnit = left.get(0).listChildrenByName("unit");
      List<Base> rUnit = right.get(0).listChildrenByName("unit");
      if (Base.compareDeep(lUnit, rUnit, true)) {
        return opLessThen(left.get(0).listChildrenByName("value"), right.get(0).listChildrenByName("value"));
      } else {
				throw new InternalErrorException("Canonical Comparison isn't done yet");
      }
    }
    return new ArrayList<Base>();
  }

	private List<Base> opGreater(List<Base> left, List<Base> right) throws FHIRException {
    if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
      Base l = left.get(0);
      Base r = right.get(0);
      if (l.hasType("string") && r.hasType("string")) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) > 0);
      else if ((l.hasType("integer", "decimal", "unsignedInt", "positiveInt")) && (r.hasType("integer", "decimal", "unsignedInt", "positiveInt"))) 
        return makeBoolean(new Double(l.primitiveValue()) > new Double(r.primitiveValue()));
      else if ((l.hasType("date", "dateTime", "instant")) && (r.hasType("date", "dateTime", "instant"))) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) > 0);
      else if ((l.hasType("time")) && (r.hasType("time"))) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) > 0);
    } else if (left.size() == 1 && right.size() == 1 && left.get(0).fhirType().equals("Quantity") && right.get(0).fhirType().equals("Quantity") ) {
      List<Base> lUnit = left.get(0).listChildrenByName("unit");
      List<Base> rUnit = right.get(0).listChildrenByName("unit");
      if (Base.compareDeep(lUnit, rUnit, true)) {
        return opGreater(left.get(0).listChildrenByName("value"), right.get(0).listChildrenByName("value"));
      } else {
				throw new InternalErrorException("Canonical Comparison isn't done yet");
      }
    }
    return new ArrayList<Base>();
  }

	private List<Base> opLessOrEqual(List<Base> left, List<Base> right) throws FHIRException {
    if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
      Base l = left.get(0);
      Base r = right.get(0);
      if (l.hasType("string") && r.hasType("string")) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) <= 0);
      else if ((l.hasType("integer", "decimal", "unsignedInt", "positiveInt")) && (r.hasType("integer", "decimal", "unsignedInt", "positiveInt"))) 
        return makeBoolean(new Double(l.primitiveValue()) <= new Double(r.primitiveValue()));
      else if ((l.hasType("date", "dateTime", "instant")) && (r.hasType("date", "dateTime", "instant"))) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) <= 0);
      else if ((l.hasType("time")) && (r.hasType("time"))) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) <= 0);
    } else if (left.size() == 1 && right.size() == 1 && left.get(0).fhirType().equals("Quantity") && right.get(0).fhirType().equals("Quantity") ) {
      List<Base> lUnits = left.get(0).listChildrenByName("unit");
      String lunit = lUnits.size() == 1 ? lUnits.get(0).primitiveValue() : null;
      List<Base> rUnits = right.get(0).listChildrenByName("unit");
      String runit = rUnits.size() == 1 ? rUnits.get(0).primitiveValue() : null;
      if ((lunit == null && runit == null) || lunit.equals(runit)) {
        return opLessOrEqual(left.get(0).listChildrenByName("value"), right.get(0).listChildrenByName("value"));
      } else {
				throw new InternalErrorException("Canonical Comparison isn't done yet");
      }
    }
    return new ArrayList<Base>();
  }

	private List<Base> opGreaterOrEqual(List<Base> left, List<Base> right) throws FHIRException {
    if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
      Base l = left.get(0);
      Base r = right.get(0);
      if (l.hasType("string") && r.hasType("string")) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) >= 0);
      else if ((l.hasType("integer", "decimal", "unsignedInt", "positiveInt")) && (r.hasType("integer", "decimal", "unsignedInt", "positiveInt"))) 
        return makeBoolean(new Double(l.primitiveValue()) >= new Double(r.primitiveValue()));
      else if ((l.hasType("date", "dateTime", "instant")) && (r.hasType("date", "dateTime", "instant"))) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) >= 0);
      else if ((l.hasType("time")) && (r.hasType("time"))) 
        return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) >= 0);
    } else if (left.size() == 1 && right.size() == 1 && left.get(0).fhirType().equals("Quantity") && right.get(0).fhirType().equals("Quantity") ) {
      List<Base> lUnit = left.get(0).listChildrenByName("unit");
      List<Base> rUnit = right.get(0).listChildrenByName("unit");
      if (Base.compareDeep(lUnit, rUnit, true)) {
        return opGreaterOrEqual(left.get(0).listChildrenByName("value"), right.get(0).listChildrenByName("value"));
      } else {
				throw new InternalErrorException("Canonical Comparison isn't done yet");
      }
    }
    return new ArrayList<Base>();
  }

  private List<Base> opIn(List<Base> left, List<Base> right) {
    boolean ans = true;
    for (Base l : left) {
      boolean f = false;
      for (Base r : right)
        if (doEquals(l, r)) {
          f = true;
          break;
        }
      if (!f) {
        ans = false;
        break;
      }
    }
    return makeBoolean(ans);
  }

  private List<Base> opContains(List<Base> left, List<Base> right) {
    boolean ans = true;
    for (Base r : right) {
      boolean f = false;
      for (Base l : left)
        if (doEquals(l, r)) {
          f = true;
          break;
        }
      if (!f) {
        ans = false;
        break;
      }
    }
    return makeBoolean(ans);
  }

  private List<Base> opPlus(List<Base> left, List<Base> right) throws PathEngineException {
    if (left.size() == 0)
      throw new PathEngineException("Error performing +: left operand has no value");
    if (left.size() > 1)
      throw new PathEngineException("Error performing +: left operand has more than one value");
    if (!left.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing +: left operand has the wrong type (%s)", left.get(0).fhirType()));
    if (right.size() == 0)
      throw new PathEngineException("Error performing +: right operand has no value");
    if (right.size() > 1)
      throw new PathEngineException("Error performing +: right operand has more than one value");
    if (!right.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing +: right operand has the wrong type (%s)", right.get(0).fhirType()));

    List<Base> result = new ArrayList<Base>();
    Base l = left.get(0);
    Base r = right.get(0);
    if (l.hasType("string", "id", "code", "uri") && r.hasType("string", "id", "code", "uri")) 
      result.add(new StringType(l.primitiveValue() + r.primitiveValue()));
    else if (l.hasType("integer") && r.hasType("integer")) 
      result.add(new IntegerType(Integer.parseInt(l.primitiveValue()) + Integer.parseInt(r.primitiveValue())));
    else if (l.hasType("decimal", "integer") && r.hasType("decimal", "integer")) 
      result.add(new DecimalType(new BigDecimal(l.primitiveValue()).add(new BigDecimal(r.primitiveValue()))));
    else
      throw new PathEngineException(String.format("Error performing +: left and right operand have incompatible or illegal types (%s, %s)", left.get(0).fhirType(), right.get(0).fhirType()));
    return result;
  }

  private List<Base> opTimes(List<Base> left, List<Base> right) throws PathEngineException {
    if (left.size() == 0)
      throw new PathEngineException("Error performing *: left operand has no value");
    if (left.size() > 1)
      throw new PathEngineException("Error performing *: left operand has more than one value");
    if (!left.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing +: left operand has the wrong type (%s)", left.get(0).fhirType()));
    if (right.size() == 0)
      throw new PathEngineException("Error performing *: right operand has no value");
    if (right.size() > 1)
      throw new PathEngineException("Error performing *: right operand has more than one value");
    if (!right.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing *: right operand has the wrong type (%s)", right.get(0).fhirType()));

    List<Base> result = new ArrayList<Base>();
    Base l = left.get(0);
    Base r = right.get(0);

    if (l.hasType("integer") && r.hasType("integer")) 
      result.add(new IntegerType(Integer.parseInt(l.primitiveValue()) * Integer.parseInt(r.primitiveValue())));
    else if (l.hasType("decimal", "integer") && r.hasType("decimal", "integer")) 
      result.add(new DecimalType(new BigDecimal(l.primitiveValue()).multiply(new BigDecimal(r.primitiveValue()))));
    else
      throw new PathEngineException(String.format("Error performing *: left and right operand have incompatible or illegal types (%s, %s)", left.get(0).fhirType(), right.get(0).fhirType()));
    return result;
  }

  private List<Base> opConcatenate(List<Base> left, List<Base> right) {
    List<Base> result = new ArrayList<Base>();
    result.add(new StringType(convertToString(left) + convertToString((right))));
    return result;
  }

  private List<Base> opUnion(List<Base> left, List<Base> right) {
    List<Base> result = new ArrayList<Base>();
    for (Base item : left) {
      if (!doContains(result, item))
        result.add(item);
    }
    for (Base item : right) {
      if (!doContains(result, item))
        result.add(item);
    }
    return result;
  }

  private boolean doContains(List<Base> list, Base item) {
    for (Base test : list)
      if (doEquals(test, item))
        return true;
    return false;
  }


  private List<Base> opAnd(List<Base> left, List<Base> right) {
    if (left.isEmpty() && right.isEmpty())
      return new ArrayList<Base>();
    else if (isBoolean(left, false) || isBoolean(right, false))
      return makeBoolean(false);
    else if (left.isEmpty() || right.isEmpty())
      return new ArrayList<Base>();
    else if (convertToBoolean(left) && convertToBoolean(right))
      return makeBoolean(true);
    else 
      return makeBoolean(false);
  }

  private boolean isBoolean(List<Base> list, boolean b) {
    return list.size() == 1 && list.get(0) instanceof BooleanType && ((BooleanType) list.get(0)).booleanValue() == b;
  }

  private List<Base> opOr(List<Base> left, List<Base> right) {
    if (left.isEmpty() && right.isEmpty())
      return new ArrayList<Base>();
    else if (convertToBoolean(left) || convertToBoolean(right))
      return makeBoolean(true);
    else if (left.isEmpty() || right.isEmpty())
      return new ArrayList<Base>();
    else 
      return makeBoolean(false);
  }

  private List<Base> opXor(List<Base> left, List<Base> right) {
    if (left.isEmpty() || right.isEmpty())
      return new ArrayList<Base>();
    else 
      return makeBoolean(convertToBoolean(left) ^ convertToBoolean(right));
  }

  private List<Base> opImplies(List<Base> left, List<Base> right) {
    if (!convertToBoolean(left)) 
      return makeBoolean(true);
    else if (right.size() == 0)
      return new ArrayList<Base>();      
    else
      return makeBoolean(convertToBoolean(right));
  }


  private List<Base> opMinus(List<Base> left, List<Base> right) throws PathEngineException {
    if (left.size() == 0)
      throw new PathEngineException("Error performing -: left operand has no value");
    if (left.size() > 1)
      throw new PathEngineException("Error performing -: left operand has more than one value");
    if (!left.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing -: left operand has the wrong type (%s)", left.get(0).fhirType()));
    if (right.size() == 0)
      throw new PathEngineException("Error performing -: right operand has no value");
    if (right.size() > 1)
      throw new PathEngineException("Error performing -: right operand has more than one value");
    if (!right.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing -: right operand has the wrong type (%s)", right.get(0).fhirType()));

    List<Base> result = new ArrayList<Base>();
    Base l = left.get(0);
    Base r = right.get(0);

    if (l.hasType("integer") && r.hasType("integer")) 
      result.add(new IntegerType(Integer.parseInt(l.primitiveValue()) - Integer.parseInt(r.primitiveValue())));
    else if (l.hasType("decimal", "integer") && r.hasType("decimal", "integer")) 
      result.add(new DecimalType(new BigDecimal(l.primitiveValue()).subtract(new BigDecimal(r.primitiveValue()))));
    else
      throw new PathEngineException(String.format("Error performing -: left and right operand have incompatible or illegal types (%s, %s)", left.get(0).fhirType(), right.get(0).fhirType()));
    return result;
  }

  private List<Base> opDivideBy(List<Base> left, List<Base> right) throws PathEngineException {
    if (left.size() == 0)
      throw new PathEngineException("Error performing /: left operand has no value");
    if (left.size() > 1)
      throw new PathEngineException("Error performing /: left operand has more than one value");
    if (!left.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing -: left operand has the wrong type (%s)", left.get(0).fhirType()));
    if (right.size() == 0)
      throw new PathEngineException("Error performing /: right operand has no value");
    if (right.size() > 1)
      throw new PathEngineException("Error performing /: right operand has more than one value");
    if (!right.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing /: right operand has the wrong type (%s)", right.get(0).fhirType()));

    List<Base> result = new ArrayList<Base>();
    Base l = left.get(0);
    Base r = right.get(0);

    if (l.hasType("integer", "decimal", "unsignedInt", "positiveInt") && r.hasType("integer", "decimal", "unsignedInt", "positiveInt")) {
      Decimal d1;
      try {
        d1 = new Decimal(l.primitiveValue());
        Decimal d2 = new Decimal(r.primitiveValue());
        result.add(new DecimalType(d1.divide(d2).asDecimal()));
      } catch (UcumException e) {
        throw new PathEngineException(e);
      }
    }
    else
      throw new PathEngineException(String.format("Error performing /: left and right operand have incompatible or illegal types (%s, %s)", left.get(0).fhirType(), right.get(0).fhirType()));
    return result;
  }

  private List<Base> opDiv(List<Base> left, List<Base> right) throws PathEngineException {
    if (left.size() == 0)
      throw new PathEngineException("Error performing div: left operand has no value");
    if (left.size() > 1)
      throw new PathEngineException("Error performing div: left operand has more than one value");
    if (!left.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing div: left operand has the wrong type (%s)", left.get(0).fhirType()));
    if (right.size() == 0)
      throw new PathEngineException("Error performing div: right operand has no value");
    if (right.size() > 1)
      throw new PathEngineException("Error performing div: right operand has more than one value");
    if (!right.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing div: right operand has the wrong type (%s)", right.get(0).fhirType()));

    List<Base> result = new ArrayList<Base>();
    Base l = left.get(0);
    Base r = right.get(0);

    if (l.hasType("integer") && r.hasType("integer")) 
      result.add(new IntegerType(Integer.parseInt(l.primitiveValue()) / Integer.parseInt(r.primitiveValue())));
    else if (l.hasType("decimal", "integer") && r.hasType("decimal", "integer")) { 
      Decimal d1;
      try {
        d1 = new Decimal(l.primitiveValue());
        Decimal d2 = new Decimal(r.primitiveValue());
        result.add(new IntegerType(d1.divInt(d2).asDecimal()));
      } catch (UcumException e) {
        throw new PathEngineException(e);
      }
    }
    else
      throw new PathEngineException(String.format("Error performing div: left and right operand have incompatible or illegal types (%s, %s)", left.get(0).fhirType(), right.get(0).fhirType()));
    return result;
  }

  private List<Base> opMod(List<Base> left, List<Base> right) throws PathEngineException {
    if (left.size() == 0)
      throw new PathEngineException("Error performing mod: left operand has no value");
    if (left.size() > 1)
      throw new PathEngineException("Error performing mod: left operand has more than one value");
    if (!left.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing mod: left operand has the wrong type (%s)", left.get(0).fhirType()));
    if (right.size() == 0)
      throw new PathEngineException("Error performing mod: right operand has no value");
    if (right.size() > 1)
      throw new PathEngineException("Error performing mod: right operand has more than one value");
    if (!right.get(0).isPrimitive())
      throw new PathEngineException(String.format("Error performing mod: right operand has the wrong type (%s)", right.get(0).fhirType()));

    List<Base> result = new ArrayList<Base>();
    Base l = left.get(0);
    Base r = right.get(0);

    if (l.hasType("integer") && r.hasType("integer")) 
      result.add(new IntegerType(Integer.parseInt(l.primitiveValue()) % Integer.parseInt(r.primitiveValue())));
    else if (l.hasType("decimal", "integer") && r.hasType("decimal", "integer")) {
      Decimal d1;
      try {
        d1 = new Decimal(l.primitiveValue());
        Decimal d2 = new Decimal(r.primitiveValue());
        result.add(new DecimalType(d1.modulo(d2).asDecimal()));
      } catch (UcumException e) {
        throw new PathEngineException(e);
      }
    }
    else
      throw new PathEngineException(String.format("Error performing mod: left and right operand have incompatible or illegal types (%s, %s)", left.get(0).fhirType(), right.get(0).fhirType()));
    return result;
  }


  private TypeDetails readConstantType(ExecutionTypeContext context, String constant) throws PathEngineException {
    if (constant.equals("true")) 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    else if (constant.equals("false")) 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    else if (Utilities.isInteger(constant))
      return new TypeDetails(CollectionStatus.SINGLETON, "integer");
    else if (Utilities.isDecimal(constant))
      return new TypeDetails(CollectionStatus.SINGLETON, "decimal");
    else if (constant.startsWith("%"))
      return resolveConstantType(context, constant);
    else
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
  }

  private TypeDetails resolveConstantType(ExecutionTypeContext context, String s) throws PathEngineException {
    if (s.equals("%sct"))
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    else if (s.equals("%loinc"))
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    else if (s.equals("%ucum"))
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    else if (s.equals("%resource")) {
      if (context.resource == null)
        throw new PathEngineException("%resource cannot be used in this context");
      return new TypeDetails(CollectionStatus.SINGLETON, context.resource);
    } else if (s.equals("%context")) {
      return new TypeDetails(CollectionStatus.SINGLETON, context.context);
    } else if (s.equals("%map-codes"))
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    else if (s.equals("%us-zip"))
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    else if (s.startsWith("%\"vs-"))
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    else if (s.startsWith("%\"cs-"))
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    else if (s.startsWith("%\"ext-"))
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    else if (hostServices == null)
      throw new PathEngineException("Unknown fixed constant type for '"+s+"'");
    else
      return hostServices.resolveConstantType(context.appInfo, s);
  }

	private List<Base> execute(ExecutionContext context, Base item, ExpressionNode exp, boolean atEntry) throws FHIRException {
    List<Base> result = new ArrayList<Base>(); 
    if (atEntry && Character.isUpperCase(exp.getName().charAt(0))) {// special case for start up
      if (item.isResource() && item.fhirType().equals(exp.getName()))  
        result.add(item);
    } else 
      getChildrenByName(item, exp.getName(), result);
    if (result.size() == 0 && atEntry && context.appInfo != null) {
      Base temp = hostServices.resolveConstant(context.appInfo, exp.getName());
      if (temp != null) {
        result.add(temp);
      }
    }
    return result;
  }	

  private TypeDetails executeContextType(ExecutionTypeContext context, String name) throws PathEngineException, DefinitionException {
    if (hostServices == null)
      throw new PathEngineException("Unable to resolve context reference since no host services are provided");
    return hostServices.resolveConstantType(context.appInfo, name);
  }
  
  private TypeDetails executeType(String type, ExpressionNode exp, boolean atEntry) throws PathEngineException, DefinitionException {
    if (atEntry && Character.isUpperCase(exp.getName().charAt(0)) && tail(type).equals(exp.getName())) // special case for start up
      return new TypeDetails(CollectionStatus.SINGLETON, type);
    TypeDetails result = new TypeDetails(null);
    getChildTypesByName(type, exp.getName(), result);
    return result;
  }


  private String tail(String type) {
    return type.contains("#") ? "" : type.substring(type.lastIndexOf("/")+1);
  }


  @SuppressWarnings("unchecked")
  private TypeDetails evaluateFunctionType(ExecutionTypeContext context, TypeDetails focus, ExpressionNode exp) throws PathEngineException, DefinitionException {
    List<TypeDetails> paramTypes = new ArrayList<TypeDetails>();
    if (exp.getFunction() == Function.Is || exp.getFunction() == Function.As)
      paramTypes.add(new TypeDetails(CollectionStatus.SINGLETON, "string"));
    else
      for (ExpressionNode expr : exp.getParameters()) {
        if (exp.getFunction() == Function.Where || exp.getFunction() == Function.Select || exp.getFunction() == Function.Repeat)
          paramTypes.add(executeType(changeThis(context, focus), focus, expr, true));
        else
          paramTypes.add(executeType(context, focus, expr, true));
      }
    switch (exp.getFunction()) {
    case Empty : 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Not : 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Exists : 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case SubsetOf : {
      checkParamTypes(exp.getFunction().toCode(), paramTypes, focus); 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean"); 
    }
    case SupersetOf : {
      checkParamTypes(exp.getFunction().toCode(), paramTypes, focus); 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean"); 
    }
    case IsDistinct : 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Distinct : 
      return focus;
    case Count : 
      return new TypeDetails(CollectionStatus.SINGLETON, "integer");
    case Where : 
      return focus;
    case Select : 
      return anything(focus.getCollectionStatus());
    case All : 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Repeat : 
      return anything(focus.getCollectionStatus());
    case Item : {
      checkOrdered(focus, "item");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "integer")); 
      return focus; 
    }
    case As : {
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, exp.getParameters().get(0).getName());
    }
    case Is : {
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean"); 
    }
    case Single :
      return focus.toSingleton();
    case First : {
      checkOrdered(focus, "first");
      return focus.toSingleton();
    }
    case Last : {
      checkOrdered(focus, "last");
      return focus.toSingleton();
    }
    case Tail : {
      checkOrdered(focus, "tail");
      return focus;
    }
    case Skip : {
      checkOrdered(focus, "skip");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "integer")); 
      return focus;
    }
    case Take : {
      checkOrdered(focus, "take");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "integer")); 
      return focus;
    }
    case Iif : {
      TypeDetails types = new TypeDetails(null);
      types.update(paramTypes.get(0));
      if (paramTypes.size() > 1)
        types.update(paramTypes.get(1));
      return types;
    }
    case ToInteger : {
      checkContextPrimitive(focus, "toInteger");
      return new TypeDetails(CollectionStatus.SINGLETON, "integer");
    }
    case ToDecimal : {
      checkContextPrimitive(focus, "toDecimal");
      return new TypeDetails(CollectionStatus.SINGLETON, "decimal");
    }
    case ToString : {
      checkContextPrimitive(focus, "toString");
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    }
    case Substring : {
      checkContextString(focus, "subString");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "integer"), new TypeDetails(CollectionStatus.SINGLETON, "integer")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "string"); 
    }
    case StartsWith : {
      checkContextString(focus, "startsWith");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean"); 
    }
    case EndsWith : {
      checkContextString(focus, "endsWith");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean"); 
    }
    case Matches : {
      checkContextString(focus, "matches");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean"); 
    }
    case ReplaceMatches : {
      checkContextString(focus, "replaceMatches");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string"), new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "string"); 
    }
    case Contains : {
      checkContextString(focus, "contains");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    }
    case Replace : {
      checkContextString(focus, "replace");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string"), new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "string");
    }
    case Length : { 
      checkContextPrimitive(focus, "length");
      return new TypeDetails(CollectionStatus.SINGLETON, "integer");
    }
    case Children : 
      return childTypes(focus, "*");
    case Descendants : 
      return childTypes(focus, "**");
    case MemberOf : {
      checkContextCoded(focus, "memberOf");
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    }
    case Trace : {
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return focus; 
    }
    case Today : 
      return new TypeDetails(CollectionStatus.SINGLETON, "date");
    case Now : 
      return new TypeDetails(CollectionStatus.SINGLETON, "dateTime");
    case Resolve : {
      checkContextReference(focus, "resolve");
      return new TypeDetails(CollectionStatus.SINGLETON, "DomainResource"); 
    }
    case Extension : {
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return new TypeDetails(CollectionStatus.SINGLETON, "Extension"); 
    }
    case HasValue : 
      return new TypeDetails(CollectionStatus.SINGLETON, "boolean");
    case Alias : 
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return anything(CollectionStatus.SINGLETON); 
    case AliasAs : 
      checkParamTypes(exp.getFunction().toCode(), paramTypes, new TypeDetails(CollectionStatus.SINGLETON, "string")); 
      return focus; 
    case Custom : {
      return hostServices.checkFunction(context.appInfo, exp.getName(), paramTypes);
    }
    default:
      break;
    }
    throw new Error("not Implemented yet");
  }


  private void checkParamTypes(String funcName, List<TypeDetails> paramTypes, TypeDetails... typeSet) throws PathEngineException {
    int i = 0;
    for (TypeDetails pt : typeSet) {
      if (i == paramTypes.size())
        return;
      TypeDetails actual = paramTypes.get(i);
      i++;
      for (String a : actual.getTypes()) {
        if (!pt.hasType(worker, a))
          throw new PathEngineException("The parameter type '"+a+"' is not legal for "+funcName+" parameter "+Integer.toString(i)+". expecting "+pt.toString()); 
      }
    }
  }

  private void checkOrdered(TypeDetails focus, String name) throws PathEngineException {
    if (focus.getCollectionStatus() == CollectionStatus.UNORDERED)
      throw new PathEngineException("The function '"+name+"'() can only be used on ordered collections"); 
  }

  private void checkContextReference(TypeDetails focus, String name) throws PathEngineException {
    if (!focus.hasType(worker, "string") && !focus.hasType(worker, "uri") && !focus.hasType(worker, "Reference"))
      throw new PathEngineException("The function '"+name+"'() can only be used on string, uri, Reference"); 
  }


  private void checkContextCoded(TypeDetails focus, String name) throws PathEngineException {
    if (!focus.hasType(worker, "string") && !focus.hasType(worker, "code") && !focus.hasType(worker, "uri") && !focus.hasType(worker, "Coding") && !focus.hasType(worker, "CodeableConcept"))
      throw new PathEngineException("The function '"+name+"'() can only be used on string, code, uri, Coding, CodeableConcept");     
  }


  private void checkContextString(TypeDetails focus, String name) throws PathEngineException {
    if (!focus.hasType(worker, "string") && !focus.hasType(worker, "code") && !focus.hasType(worker, "uri") && !focus.hasType(worker, "id"))
      throw new PathEngineException("The function '"+name+"'() can only be used on string, uri, code, id, but found "+focus.describe()); 
  }


  private void checkContextPrimitive(TypeDetails focus, String name) throws PathEngineException {
    if (!focus.hasType(primitiveTypes))
      throw new PathEngineException("The function '"+name+"'() can only be used on "+primitiveTypes.toString()); 
  }


  private TypeDetails childTypes(TypeDetails focus, String mask) throws PathEngineException, DefinitionException {
    TypeDetails result = new TypeDetails(CollectionStatus.UNORDERED);
    for (String f : focus.getTypes()) 
      getChildTypesByName(f, mask, result);
    return result;
  }

  private TypeDetails anything(CollectionStatus status) {
    return new TypeDetails(status, allTypes.keySet());
  }

  //	private boolean isPrimitiveType(String s) {
  //		return s.equals("boolean") || s.equals("integer") || s.equals("decimal") || s.equals("base64Binary") || s.equals("instant") || s.equals("string") || s.equals("uri") || s.equals("date") || s.equals("dateTime") || s.equals("time") || s.equals("code") || s.equals("oid") || s.equals("id") || s.equals("unsignedInt") || s.equals("positiveInt") || s.equals("markdown");
  //	}

	private List<Base> evaluateFunction(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    switch (exp.getFunction()) {
    case Empty : return funcEmpty(context, focus, exp);
    case Not : return funcNot(context, focus, exp);
    case Exists : return funcExists(context, focus, exp);
    case SubsetOf : return funcSubsetOf(context, focus, exp);
    case SupersetOf : return funcSupersetOf(context, focus, exp);
    case IsDistinct : return funcIsDistinct(context, focus, exp);
    case Distinct : return funcDistinct(context, focus, exp);
    case Count : return funcCount(context, focus, exp);
    case Where : return funcWhere(context, focus, exp);
    case Select : return funcSelect(context, focus, exp);
    case All : return funcAll(context, focus, exp);
    case Repeat : return funcRepeat(context, focus, exp);
    case Item : return funcItem(context, focus, exp);
    case As : return funcAs(context, focus, exp);
    case Is : return funcIs(context, focus, exp);
    case Single : return funcSingle(context, focus, exp);
    case First : return funcFirst(context, focus, exp);
    case Last : return funcLast(context, focus, exp);
    case Tail : return funcTail(context, focus, exp);
    case Skip : return funcSkip(context, focus, exp);
    case Take : return funcTake(context, focus, exp);
    case Iif : return funcIif(context, focus, exp);
    case ToInteger : return funcToInteger(context, focus, exp);
    case ToDecimal : return funcToDecimal(context, focus, exp);
    case ToString : return funcToString(context, focus, exp);
    case Substring : return funcSubstring(context, focus, exp);
    case StartsWith : return funcStartsWith(context, focus, exp);
    case EndsWith : return funcEndsWith(context, focus, exp);
    case Matches : return funcMatches(context, focus, exp);
    case ReplaceMatches : return funcReplaceMatches(context, focus, exp);
    case Contains : return funcContains(context, focus, exp);
    case Replace : return funcReplace(context, focus, exp);
    case Length : return funcLength(context, focus, exp);
    case Children : return funcChildren(context, focus, exp);
    case Descendants : return funcDescendants(context, focus, exp);
    case MemberOf : return funcMemberOf(context, focus, exp);
    case Trace : return funcTrace(context, focus, exp);
    case Today : return funcToday(context, focus, exp);
    case Now : return funcNow(context, focus, exp);
    case Resolve : return funcResolve(context, focus, exp);
    case Extension : return funcExtension(context, focus, exp);
    case HasValue : return funcHasValue(context, focus, exp);
    case AliasAs : return funcAliasAs(context, focus, exp);
    case Alias : return funcAlias(context, focus, exp);
    case Custom: { 
      List<List<Base>> params = new ArrayList<List<Base>>();
      for (ExpressionNode p : exp.getParameters()) 
        params.add(execute(context, focus, p, true));
      return hostServices.executeFunction(context.appInfo, exp.getName(), params);
    }
    default:
      throw new Error("not Implemented yet");
    }
  }

	private List<Base> funcAliasAs(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> nl = execute(context, focus, exp.getParameters().get(0), true);
    String name = nl.get(0).primitiveValue();
    context.addAlias(name, focus);
    return focus;
  }

  private List<Base> funcAlias(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> nl = execute(context, focus, exp.getParameters().get(0), true);
    String name = nl.get(0).primitiveValue();
    List<Base> res = new ArrayList<Base>();
    Base b = context.getAlias(name);
    if (b != null)
      res.add(b);
    return res;
    
  }

	private List<Base> funcAll(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    if (exp.getParameters().size() == 1) {
      List<Base> result = new ArrayList<Base>();
      List<Base> pc = new ArrayList<Base>();
      boolean all = true;
      for (Base item : focus) {
        pc.clear();
        pc.add(item);
        if (!convertToBoolean(execute(changeThis(context, item), pc, exp.getParameters().get(0), true))) {
          all = false;
          break;
        }
      }
      result.add(new BooleanType(all));
      return result;
    } else {// (exp.getParameters().size() == 0) {
      List<Base> result = new ArrayList<Base>();
      boolean all = true;
      for (Base item : focus) {
        boolean v = false;
        if (item instanceof BooleanType) {
          v = ((BooleanType) item).booleanValue();
        } else 
          v = item != null;
        if (!v) {
          all = false;
          break;
        }
      }
      result.add(new BooleanType(all));
      return result;
    }
  }


  private ExecutionContext changeThis(ExecutionContext context, Base newThis) {
    return new ExecutionContext(context.appInfo, context.resource, context.context, context.aliases, newThis);
  }

  private ExecutionTypeContext changeThis(ExecutionTypeContext context, TypeDetails newThis) {
    return new ExecutionTypeContext(context.appInfo, context.resource, context.context, newThis);
  }


  private List<Base> funcNow(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    result.add(DateTimeType.now());
    return result;
  }


  private List<Base> funcToday(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    result.add(new DateType(new Date(), TemporalPrecisionEnum.DAY));
    return result;
  }


  private List<Base> funcMemberOf(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcDescendants(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    List<Base> current = new ArrayList<Base>();
    current.addAll(focus);
    List<Base> added = new ArrayList<Base>();
    boolean more = true;
    while (more) {
      added.clear();
      for (Base item : current) {
        getChildrenByName(item, "*", added);
      }
      more = !added.isEmpty();
      result.addAll(added);
      current.clear();
      current.addAll(added);
    }
    return result;
  }


  private List<Base> funcChildren(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    for (Base b : focus)
      getChildrenByName(b, "*", result);
    return result;
  }


  private List<Base> funcReplace(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcReplaceMatches(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    String sw = convertToString(execute(context, focus, exp.getParameters().get(0), true));

    if (focus.size() == 1 && !Utilities.noString(sw))
      result.add(new BooleanType(convertToString(focus.get(0)).contains(sw)));
    else
      result.add(new BooleanType(false));
    return result;
  }


  private List<Base> funcEndsWith(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    String sw = convertToString(execute(context, focus, exp.getParameters().get(0), true));

    if (focus.size() == 1 && !Utilities.noString(sw))
      result.add(new BooleanType(convertToString(focus.get(0)).endsWith(sw)));
    else
      result.add(new BooleanType(false));
    return result;
  }


  private List<Base> funcToString(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    result.add(new StringType(convertToString(focus)));
    return result;
  }


  private List<Base> funcToDecimal(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    String s = convertToString(focus);
    List<Base> result = new ArrayList<Base>();
    if (Utilities.isDecimal(s))
      result.add(new DecimalType(s));
    return result;
  }


  private List<Base> funcIif(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> n1 = execute(context, focus, exp.getParameters().get(0), true);
    Boolean v = convertToBoolean(n1);

    if (v)
      return execute(context, focus, exp.getParameters().get(1), true);
    else if (exp.getParameters().size() < 3)
      return new ArrayList<Base>();
    else
      return execute(context, focus, exp.getParameters().get(2), true);
  }


  private List<Base> funcTake(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> n1 = execute(context, focus, exp.getParameters().get(0), true);
    int i1 = Integer.parseInt(n1.get(0).primitiveValue());

    List<Base> result = new ArrayList<Base>();
    for (int i = 0; i < Math.min(focus.size(), i1); i++)
      result.add(focus.get(i));
    return result;
  }


  private List<Base> funcSingle(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
    if (focus.size() == 1)
      return focus;
    throw new PathEngineException(String.format("Single() : checking for 1 item but found %d items", focus.size()));
  }


  private List<Base> funcIs(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
    List<Base> result = new ArrayList<Base>();
    if (focus.size() == 0 || focus.size() > 1) 
      result.add(new BooleanType(false));
    else {
      String tn = exp.getParameters().get(0).getName();
      result.add(new BooleanType(focus.get(0).hasType(tn)));
    }
    return result;
  }


  private List<Base> funcAs(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    String tn = exp.getParameters().get(0).getName();
    for (Base b : focus)
      if (b.hasType(tn))
        result.add(b);
    return result;
  }


  private List<Base> funcRepeat(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    List<Base> current = new ArrayList<Base>();
    current.addAll(focus);
    List<Base> added = new ArrayList<Base>();
    boolean more = true;
    while (more) {
      added.clear();
      List<Base> pc = new ArrayList<Base>();
      for (Base item : current) {
        pc.clear();
        pc.add(item);
        added.addAll(execute(changeThis(context, item), pc, exp.getParameters().get(0), false));
      }
      more = !added.isEmpty();
      result.addAll(added);
      current.clear();
      current.addAll(added);
    }
    return result;
  }



  private List<Base> funcIsDistinct(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    if (focus.size() <= 1)
      return makeBoolean(true);

    boolean distinct = true;
    for (int i = 0; i < focus.size(); i++) {
      for (int j = i+1; j < focus.size(); j++) {
        if (doEquals(focus.get(j), focus.get(i))) {
          distinct = false;
          break;
        }
      }
    }
    return makeBoolean(distinct);
  }


  private List<Base> funcSupersetOf(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> target = execute(context, focus, exp.getParameters().get(0), true);

    boolean valid = true;
    for (Base item : target) {
      boolean found = false;
      for (Base t : focus) {
        if (Base.compareDeep(item, t, false)) {
          found = true;
          break;
        }
      }
      if (!found) {
        valid = false;
        break;
      }
    }
    List<Base> result = new ArrayList<Base>();
    result.add(new BooleanType(valid));
    return result;
  }


  private List<Base> funcSubsetOf(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> target = execute(context, focus, exp.getParameters().get(0), true);

    boolean valid = true;
    for (Base item : focus) {
      boolean found = false;
      for (Base t : target) {
        if (Base.compareDeep(item, t, false)) {
          found = true;
          break;
        }
      }
      if (!found) {
        valid = false;
        break;
      }
    }
    List<Base> result = new ArrayList<Base>();
    result.add(new BooleanType(valid));
    return result;
  }


  private List<Base> funcExists(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    result.add(new BooleanType(!ElementUtil.isEmpty(focus)));
    return result;
  }


  private List<Base> funcResolve(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    for (Base item : focus) {
      if (hostServices != null) {
        String s = convertToString(item);
        if (item.fhirType().equals("Reference")) {
          Property p = item.getChildByName("reference");
          if (p.hasValues())
            s = convertToString(p.getValues().get(0));
        }
        Base res = null;
        if (s.startsWith("#")) {
          String id = s.substring(1);
          Property p = context.resource.getChildByName("contained");
          for (Base c : p.getValues()) {
            if (id.equals(c.getIdBase()))
              res = c;
          }
        } else
         res = hostServices.resolveReference(context.appInfo, s);
        if (res != null)
          result.add(res);
      }
    }
    return result;
  }

	private List<Base> funcExtension(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    List<Base> nl = execute(context, focus, exp.getParameters().get(0), true);
    String url = nl.get(0).primitiveValue();

    for (Base item : focus) {
      List<Base> ext = new ArrayList<Base>();
      getChildrenByName(item, "extension", ext);
      getChildrenByName(item, "modifierExtension", ext);
      for (Base ex : ext) {
        List<Base> vl = new ArrayList<Base>();
        getChildrenByName(ex, "url", vl);
        if (convertToString(vl).equals(url))
          result.add(ex);
      }
    }
    return result;
  }

	private List<Base> funcTrace(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> nl = execute(context, focus, exp.getParameters().get(0), true);
    String name = nl.get(0).primitiveValue();

    log(name, focus);
    return focus;
  }

  private List<Base> funcDistinct(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    if (focus.size() <= 1)
      return focus;

    List<Base> result = new ArrayList<Base>();
    for (int i = 0; i < focus.size(); i++) {
      boolean found = false;
      for (int j = i+1; j < focus.size(); j++) {
        if (doEquals(focus.get(j), focus.get(i))) {
          found = true;
          break;
        }
      }
      if (!found)
        result.add(focus.get(i));
    }
    return result;
  }

	private List<Base> funcMatches(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    String sw = convertToString(execute(context, focus, exp.getParameters().get(0), true));

    if (focus.size() == 1 && !Utilities.noString(sw)) {
      String st = convertToString(focus.get(0));
      if (Utilities.noString(st))
        result.add(new BooleanType(false));
      else
        result.add(new BooleanType(st.matches(sw)));
    } else
      result.add(new BooleanType(false));
    return result;
  }

	private List<Base> funcContains(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    String sw = convertToString(execute(context, focus, exp.getParameters().get(0), true));

    if (focus.size() == 1 && !Utilities.noString(sw)) {
      String st = convertToString(focus.get(0));
      if (Utilities.noString(st))
        result.add(new BooleanType(false));
      else
        result.add(new BooleanType(st.contains(sw)));
    }  else
      result.add(new BooleanType(false));
    return result;
  }

  private List<Base> funcLength(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    if (focus.size() == 1) {
      String s = convertToString(focus.get(0));
      result.add(new IntegerType(s.length()));
    }
    return result;
  }

  private List<Base> funcHasValue(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    if (focus.size() == 1) {
      String s = convertToString(focus.get(0));
      result.add(new BooleanType(!Utilities.noString(s)));
    }
    return result;
  }

	private List<Base> funcStartsWith(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    String sw = convertToString(execute(context, focus, exp.getParameters().get(0), true));

    if (focus.size() == 1 && !Utilities.noString(sw))
      result.add(new BooleanType(convertToString(focus.get(0)).startsWith(sw)));
    else
      result.add(new BooleanType(false));
    return result;
  }

	private List<Base> funcSubstring(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    List<Base> n1 = execute(context, focus, exp.getParameters().get(0), true);
    int i1 = Integer.parseInt(n1.get(0).primitiveValue());
    int i2 = -1;
    if (exp.parameterCount() == 2) {
      List<Base> n2 = execute(context, focus, exp.getParameters().get(1), true);
      i2 = Integer.parseInt(n2.get(0).primitiveValue());
    }

    if (focus.size() == 1) {
      String sw = convertToString(focus.get(0));
      String s;
      if (i1 < 0 || i1 >= sw.length())
        return new ArrayList<Base>();
      if (exp.parameterCount() == 2)
        s = sw.substring(i1, Math.min(sw.length(), i1+i2));
      else
        s = sw.substring(i1);
      if (!Utilities.noString(s)) 
        result.add(new StringType(s));
    }
    return result;
  }

  private List<Base> funcToInteger(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    String s = convertToString(focus);
    List<Base> result = new ArrayList<Base>();
    if (Utilities.isInteger(s))
      result.add(new IntegerType(s));
    return result;
  }

  private List<Base> funcCount(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    result.add(new IntegerType(focus.size()));
    return result;
  }

  private List<Base> funcSkip(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> n1 = execute(context, focus, exp.getParameters().get(0), true);
    int i1 = Integer.parseInt(n1.get(0).primitiveValue());

    List<Base> result = new ArrayList<Base>();
    for (int i = i1; i < focus.size(); i++)
      result.add(focus.get(i));
    return result;
  }

  private List<Base> funcTail(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    for (int i = 1; i < focus.size(); i++)
      result.add(focus.get(i));
    return result;
  }

  private List<Base> funcLast(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    if (focus.size() > 0)
      result.add(focus.get(focus.size()-1));
    return result;
  }

  private List<Base> funcFirst(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    if (focus.size() > 0)
      result.add(focus.get(0));
    return result;
  }


	private List<Base> funcWhere(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    List<Base> pc = new ArrayList<Base>();
    for (Base item : focus) {
      pc.clear();
      pc.add(item);
      if (convertToBoolean(execute(changeThis(context, item), pc, exp.getParameters().get(0), true)))
        result.add(item);
    }
    return result;
  }

  private List<Base> funcSelect(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    List<Base> pc = new ArrayList<Base>();
    for (Base item : focus) {
      pc.clear();
      pc.add(item);
      result.addAll(execute(changeThis(context, item), pc, exp.getParameters().get(0), true));
    }
    return result;
  }


	private List<Base> funcItem(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws FHIRException {
    List<Base> result = new ArrayList<Base>();
    String s = convertToString(execute(context, focus, exp.getParameters().get(0), true));
    if (Utilities.isInteger(s) && Integer.parseInt(s) < focus.size())
      result.add(focus.get(Integer.parseInt(s)));
    return result;
  }

  private List<Base> funcEmpty(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
		result.add(new BooleanType(ElementUtil.isEmpty(focus)));
    return result;
  }

  private List<Base> funcNot(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    return makeBoolean(!convertToBoolean(focus));
  }

  public class ElementDefinitionMatch {
    private ElementDefinition definition;
    private String fixedType;
    public ElementDefinitionMatch(ElementDefinition definition, String fixedType) {
      super();
      this.definition = definition;
      this.fixedType = fixedType;
    }
    public ElementDefinition getDefinition() {
      return definition;
    }
    public String getFixedType() {
      return fixedType;
    }

  }

  private void getChildTypesByName(String type, String name, TypeDetails result) throws PathEngineException, DefinitionException {
    if (Utilities.noString(type))
      throw new PathEngineException("No type provided in BuildToolPathEvaluator.getChildTypesByName");
    if (type.equals("http://hl7.org/fhir/StructureDefinition/xhtml"))
      return;
    String url = null;
    if (type.contains("#")) {
      url = type.substring(0, type.indexOf("#"));
    } else {
      url = type;
    }
    String tail = "";
    StructureDefinition sd = worker.fetchResource(StructureDefinition.class, url);
    if (sd == null)
      throw new DefinitionException("Unknown type "+type); // this really is an error, because we can only get to here if the internal infrastrucgture is wrong
    List<StructureDefinition> sdl = new ArrayList<StructureDefinition>();
    ElementDefinitionMatch m = null;
    if (type.contains("#"))
      m = getElementDefinition(sd, type.substring(type.indexOf("#")+1), false);
    if (m != null && hasDataType(m.definition)) {
      if (m.fixedType != null)
      {
        StructureDefinition dt = worker.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+m.fixedType);
        if (dt == null)
          throw new DefinitionException("unknown data type "+m.fixedType);
        sdl.add(dt);
      } else
        for (TypeRefComponent t : m.definition.getType()) {
          StructureDefinition dt = worker.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+t.getCode());
          if (dt == null)
            throw new DefinitionException("unknown data type "+t.getCode());
          sdl.add(dt);
        }
    } else {
      sdl.add(sd);
      if (type.contains("#")) {
        tail = type.substring(type.indexOf("#")+1);
        tail = tail.substring(tail.indexOf("."));
      }
    }

    for (StructureDefinition sdi : sdl) {
      String path = sdi.getSnapshot().getElement().get(0).getPath()+tail+".";
      if (name.equals("**")) {
        assert(result.getCollectionStatus() == CollectionStatus.UNORDERED);
        for (ElementDefinition ed : sdi.getSnapshot().getElement()) {
          if (ed.getPath().startsWith(path))
            for (TypeRefComponent t : ed.getType()) {
              if (t.hasCode() && t.getCodeElement().hasValue()) {
                String tn = null;
                if (t.getCode().equals("Element") || t.getCode().equals("BackboneElement"))
                  tn = sdi.getType()+"#"+ed.getPath();
                else
                  tn = t.getCode();
                if (t.getCode().equals("Resource")) {
                  for (String rn : worker.getResourceNames()) {
                    if (!result.hasType(worker, rn)) {
                      getChildTypesByName(result.addType(rn), "**", result);
                    }                  
                  }
                } else if (!result.hasType(worker, tn)) {
                  getChildTypesByName(result.addType(tn), "**", result);
                }
              }
            }
        }      
      } else if (name.equals("*")) {
        assert(result.getCollectionStatus() == CollectionStatus.UNORDERED);
        for (ElementDefinition ed : sdi.getSnapshot().getElement()) {
          if (ed.getPath().startsWith(path) && !ed.getPath().substring(path.length()).contains("."))
            for (TypeRefComponent t : ed.getType()) {
              if (t.getCode().equals("Element") || t.getCode().equals("BackboneElement"))
                result.addType(sdi.getType()+"#"+ed.getPath());
              else if (t.getCode().equals("Resource"))
                result.addTypes(worker.getResourceNames());
              else
                result.addType(t.getCode());
            }
        }
      } else {
        path = sdi.getSnapshot().getElement().get(0).getPath()+tail+"."+name;

        ElementDefinitionMatch ed = getElementDefinition(sdi, path, false);
        if (ed != null) {
          if (!Utilities.noString(ed.getFixedType()))
            result.addType(ed.getFixedType());
          else
            for (TypeRefComponent t : ed.getDefinition().getType()) {
              if (Utilities.noString(t.getCode()))
                break; // throw new PathEngineException("Illegal reference to primitive value attribute @ "+path);

              ProfiledType pt = null;
              if (t.getCode().equals("Element") || t.getCode().equals("BackboneElement"))
                pt = new ProfiledType(sdi.getUrl()+"#"+path);
              else if (t.getCode().equals("Resource"))
                result.addTypes(worker.getResourceNames());
              else
                pt = new ProfiledType(t.getCode());
              if (pt != null) {
                if (t.hasProfile())
                  pt.addProfile(t.getProfile());
                if (ed.getDefinition().hasBinding())
                  pt.addBinding(ed.getDefinition().getBinding());
                result.addType(pt);
              }
            }
        }
      }
    }
  }

  private ElementDefinitionMatch getElementDefinition(StructureDefinition sd, String path, boolean allowTypedName) throws PathEngineException {
    for (ElementDefinition ed : sd.getSnapshot().getElement()) {
      if (ed.getPath().equals(path)) {
        if (ed.hasContentReference()) {
          return getElementDefinitionById(sd, ed.getContentReference());
        } else
          return new ElementDefinitionMatch(ed, null);
      }
      if (ed.getPath().endsWith("[x]") && path.startsWith(ed.getPath().substring(0, ed.getPath().length()-3)) && path.length() == ed.getPath().length()-3)
        return new ElementDefinitionMatch(ed, null);
      if (allowTypedName && ed.getPath().endsWith("[x]") && path.startsWith(ed.getPath().substring(0, ed.getPath().length()-3)) && path.length() > ed.getPath().length()-3) {
    	String s = Utilities.uncapitalize(path.substring(ed.getPath().length()-3));
    	if (primitiveTypes.contains(s))
          return new ElementDefinitionMatch(ed, s);
    	else
        return new ElementDefinitionMatch(ed, path.substring(ed.getPath().length()-3));
      }
      if (ed.getPath().contains(".") && path.startsWith(ed.getPath()+".") && (ed.getType().size() > 0) && !isAbstractType(ed.getType())) { 
        // now we walk into the type.
        if (ed.getType().size() > 1)  // if there's more than one type, the test above would fail this
          throw new PathEngineException("Internal typing issue....");
        StructureDefinition nsd = worker.fetchResource(StructureDefinition.class, "http://hl7.org/fhir/StructureDefinition/"+ed.getType().get(0).getCode());
  	    if (nsd == null) 
  	      throw new PathEngineException("Unknown type "+ed.getType().get(0).getCode());
        return getElementDefinition(nsd, nsd.getId()+path.substring(ed.getPath().length()), allowTypedName);
      }
      if (ed.hasContentReference() && path.startsWith(ed.getPath()+".")) {
        ElementDefinitionMatch m = getElementDefinitionById(sd, ed.getContentReference());
        return getElementDefinition(sd, m.definition.getPath()+path.substring(ed.getPath().length()), allowTypedName);
      }
    }
    return null;
  }

  private boolean isAbstractType(List<TypeRefComponent> list) {
	return list.size() != 1 ? true : Utilities.existsInList(list.get(0).getCode(), "Element", "BackboneElement", "Resource", "DomainResource");
}


  private boolean hasType(ElementDefinition ed, String s) {
    for (TypeRefComponent t : ed.getType()) 
      if (s.equalsIgnoreCase(t.getCode()))
        return true;
    return false;
  }

  private boolean hasDataType(ElementDefinition ed) {
    return ed.hasType() && !(ed.getType().get(0).getCode().equals("Element") || ed.getType().get(0).getCode().equals("BackboneElement"));
  }

  private ElementDefinitionMatch getElementDefinitionById(StructureDefinition sd, String ref) {
    for (ElementDefinition ed : sd.getSnapshot().getElement()) {
      if (ref.equals("#"+ed.getId())) 
        return new ElementDefinitionMatch(ed, null);
    }
    return null;
  }


  public boolean hasLog() {
    return log != null && log.length() > 0;
  }


  public String takeLog() {
    if (!hasLog())
      return "";
    String s = log.toString();
    log = new StringBuilder();
    return s;
  }

}
