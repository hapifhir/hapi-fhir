package org.hl7.fhir.dstu3.utils;

import org.hl7.fhir.dstu3.model.*;
import org.hl7.fhir.exceptions.FHIRException;
import org.hl7.fhir.dstu3.context.IWorkerContext;
import org.hl7.fhir.dstu3.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.dstu3.model.Bundle.BundleLinkComponent;
import org.hl7.fhir.utilities.Utilities;
import org.hl7.fhir.utilities.graphql.*;
import org.hl7.fhir.utilities.graphql.Operation.OperationType;
import org.hl7.fhir.utilities.graphql.Package;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphQLEngine {
  
  public class SearchEdge extends Base {

    private BundleEntryComponent be;
    private String type;
    
    public SearchEdge(String type, BundleEntryComponent be) {
      this.type = type;
      this.be = be;
    }
    @Override
    public String fhirType() {
      return type;
    }

    @Override
    protected void listChildren(List<Property> result) {
      throw new Error("Not Implemented");
    }

    @Override
    public String getIdBase() {
      throw new Error("Not Implemented");
    }

    @Override
    public void setIdBase(String value) {
      throw new Error("Not Implemented");
    }

//    @Override
//    public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
//      switch (_hash) {
//      case 3357091:    /*mode*/     return new Property(_name, "string",   "n/a", 0, 1, be.getSearch().hasMode() ? be.getSearch().getModeElement() : null);
//      case 109264530:  /*score*/    return new Property(_name, "string",   "n/a", 0, 1, be.getSearch().hasScore() ? be.getSearch().getScoreElement() : null);
//      case -341064690: /*resource*/ return new Property(_name, "resource",  "n/a", 0, 1, be.hasResource() ? be.getResource() : null);
//      default: return super.getNamedProperty(_hash, _name, _checkValid);
//      }
//    }
  }

  public class SearchWrapper extends Base {

    private Bundle bnd;
    private String type;
    private Map<String, String> map;

    public SearchWrapper(String type, Bundle bnd) throws FHIRException {
      this.type = type;
      this.bnd = bnd;
      for (BundleLinkComponent bl : bnd.getLink()) 
        if (bl.getRelation().equals("self"))
          map = parseURL(bl.getUrl());
    }

    @Override
    public String fhirType() {
      return type;
    }

    @Override
    protected void listChildren(List<Property> result) {
      throw new Error("Not Implemented");
    }

    @Override
    public String getIdBase() {
      throw new Error("Not Implemented");
    }

    @Override
    public void setIdBase(String value) {
      throw new Error("Not Implemented");
    }

//    @Override
//    public Property getNamedProperty(int _hash, String _name, boolean _checkValid) throws FHIRException {
//      switch (_hash) {
//      case 97440432:   /*first*/     return new Property(_name, "string",  "n/a", 0, 1, extractLink(_name));
//      case -1273775369: /*previous*/  return new Property(_name, "string",  "n/a", 0, 1, extractLink(_name));
//      case 3377907:    /*next*/      return new Property(_name, "string",  "n/a", 0, 1, extractLink(_name));
//      case 3314326:    /*last*/      return new Property(_name, "string",  "n/a", 0, 1, extractLink(_name));
//      case 94851343:   /*count*/     return new Property(_name, "integer", "n/a", 0, 1, bnd.getTotalElement());
//      case -1019779949:/*offset*/    return new Property(_name, "integer", "n/a", 0, 1, extractParam("search-offset"));
//      case 860381968:  /*pagesize*/  return new Property(_name, "integer", "n/a", 0, 1, extractParam("_count"));
//      case 96356950:  /*edges*/      return new Property(_name, "edge",    "n/a", 0, Integer.MAX_VALUE, getEdges());
//      default: return super.getNamedProperty(_hash, _name, _checkValid);
//      }
//    }

    private List<Base> getEdges() {
      List<Base> list = new ArrayList<>();
      for (BundleEntryComponent be : bnd.getEntry())
        list.add(new SearchEdge(type.substring(0, type.length()-10)+"Edge", be));
      return list;
    }

    private Base extractParam(String name) throws FHIRException {
      return map != null ? new IntegerType(map.get(name)) : null;
    }

    private Map<String, String> parseURL(String url) throws FHIRException {
      try {
        Map<String, String> map = new HashMap<String, String>();
        String[] pairs = url.split("&");
        for (String pair : pairs) {
          int idx = pair.indexOf("=");
          String key;
          key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
          String value = idx > 0 && pair.length() > idx + 1 ? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
          map.put(key, value);
        }
        return map;
      } catch (UnsupportedEncodingException e) {
        throw new FHIRException(e);
      }
    }

    private Base extractLink(String _name) throws FHIRException {
      for (BundleLinkComponent bl : bnd.getLink()) {
        if (bl.getRelation().equals(_name)) {
          Map<String, String> map = parseURL(bl.getUrl());
          return new StringType(map.get("search-id")+':'+map.get("search-offset"));
        }
      }
      return null;
    }

  }


  private IWorkerContext context;
  
  public GraphQLEngine(IWorkerContext context) {
    super();
    this.context = context;
  }

  /**
   *  for the host to pass context into and get back on the reference resolution interface
   */
  private Object appInfo;

  /**
   *  the focus resource - if (there instanceof one. if (there isn"t,) there instanceof no focus
   */
  private Resource focus;

  /**
   * The package that describes the graphQL to be executed, operation name, and variables
   */
  private Package graphQL;

  /**
   * where the output from executing the query instanceof going to go
   */
  private ObjectValue output;

  /** 
   * Application provided reference resolution services 
   */
  private IGraphQLStorageServices<Resource, Reference, Bundle> services;

  // internal stuff 
  private Map<String, Argument> workingVariables = new HashMap<String, Argument>();
  
  public void execute() throws EGraphEngine, EGraphQLException, FHIRException {
    if (graphQL == null)
      throw new EGraphEngine("Unable to process graphql - graphql document missing");

    output = new ObjectValue();

    Operation op = null;
    // todo: initial conditions
    if (!Utilities.noString(graphQL.getOperationName())) {
      op = graphQL.getDocument().operation(graphQL.getOperationName());
      if (op == null)
        throw new EGraphEngine("Unable to find operation \""+graphQL.getOperationName()+"\"");
    } else if ((graphQL.getDocument().getOperations().size() == 1))
      op = graphQL.getDocument().getOperations().get(0);
    else
      throw new EGraphQLException("No operation name provided, so expected to find a single operation");

    if (op.getOperationType() == OperationType.qglotMutation)
      throw new EGraphQLException("Mutation operations are not supported (yet)");

    checkNoDirectives(op.getDirectives());
    processVariables(op);
    if (focus == null)
      processSearch(output, op.getSelectionSet());
    else
      processObject(focus, focus, output, op.getSelectionSet());
  }

  private boolean checkBooleanDirective(Directive dir) throws EGraphQLException {
    if (dir.getArguments().size() != 1)
      throw new EGraphQLException("Unable to process @"+dir.getName()+": expected a single argument \"if\"");
    if (!dir.getArguments().get(0).getName().equals("if"))
      throw new EGraphQLException("Unable to process @"+dir.getName()+": expected a single argument \"if\"");
    List<Value> vl = resolveValues(dir.getArguments().get(0), 1);
    return vl.get(0).toString().equals("true");
  }

  private boolean checkDirectives(List<Directive> directives) throws EGraphQLException {
    Directive skip = null;
    Directive include = null;
    for (Directive dir : directives) {
      if (dir.getName().equals("skip")) {
        if ((skip == null))
          skip = dir;
        else
          throw new EGraphQLException("Duplicate @skip directives");
      } else if (dir.getName().equals("include")) {
        if ((include == null))
          include = dir;
        else
          throw new EGraphQLException("Duplicate @include directives");
      }
      else
        throw new EGraphQLException("Directive \""+dir.getName()+"\" instanceof not recognised");
    }
    if ((skip != null && include != null))
      throw new EGraphQLException("Cannot mix @skip and @include directives");
    if (skip != null)
      return !checkBooleanDirective(skip);
    else if (include != null)
      return checkBooleanDirective(include);
    else
      return true;
  }

  private void checkNoDirectives(List<Directive> directives) {

  }

  private boolean targetTypeOk(List<Argument> arguments, Resource dest) throws EGraphQLException {
    List<String> list = new ArrayList<String>();
    for (Argument arg : arguments) {
      if ((arg.getName().equals("type"))) {
        List<Value> vl = resolveValues(arg);
        for (Value v : vl)
          list.add(v.toString());
      }
    }
    if (list.size() == 0)
      return true;
    else
      return list.indexOf(dest.fhirType()) > -1;
  }

  private boolean hasExtensions(Base obj) {
    if (obj instanceof BackboneElement)
      return ((BackboneElement) obj).getExtension().size() > 0 || ((BackboneElement) obj).getModifierExtension().size() > 0;
      else if (obj instanceof DomainResource)
        return ((DomainResource)obj).getExtension().size() > 0 || ((DomainResource)obj).getModifierExtension().size() > 0;
        else if (obj instanceof Element)
          return ((Element)obj).getExtension().size() > 0;
          else
            return false;
  }

  private boolean passesExtensionMode(Base obj, boolean extensionMode) {
    if (!obj.isPrimitive())
      return !extensionMode;
    else if (extensionMode)
      return !Utilities.noString(obj.getIdBase()) || hasExtensions(obj);
    else
      return obj.primitiveValue() != "";
  }

  private List<Base> filter(Resource context, Property prop, List<Argument> arguments, List<Base> values, boolean extensionMode) throws FHIRException, EGraphQLException {
    List<Base> result = new ArrayList<Base>();
    if (values.size() > 0) {
      StringBuilder fp = new StringBuilder();
      for (Argument arg : arguments) {
        List<Value> vl = resolveValues(arg);
        if ((vl.size() != 1))
          throw new EGraphQLException("Incorrect number of arguments");
        if (values.get(0).isPrimitive())
          throw new EGraphQLException("Attempt to use a filter ("+arg.getName()+") on a primtive type ("+prop.getTypeCode()+")");
        if ((arg.getName().equals("fhirpath")))
          fp.append(" and "+vl.get(0).toString());
        else {
          Property p = values.get(0).getNamedProperty(arg.getName());
          if (p == null)
            throw new EGraphQLException("Attempt to use an unknown filter ("+arg.getName()+") on a type ("+prop.getTypeCode()+")");
          fp.append(" and "+arg.getName()+" = '"+vl.get(0).toString()+"'");
        }
      }
      if (fp.length() == 0)
        for (Base v : values) {
          if (v != null) {
            if (passesExtensionMode(v, extensionMode))
              result.add(v);
          }
        } else {
          FHIRPathEngine fpe = new FHIRPathEngine(this.context);
          ExpressionNode node = fpe.parse(fp.toString().substring(5));
          for (Base v : values)
            if (passesExtensionMode(v, extensionMode) && fpe.evaluateToBoolean(null, context, v, node))
              result.add(v);
        }
    }
    return result;
  }

  private List<Resource> filterResources(Argument fhirpath, Bundle bnd) throws EGraphQLException, FHIRException {
    List<Resource> result = new ArrayList<Resource>();
    if (bnd.getEntry().size() > 0) {
      if ((fhirpath == null))
        for (BundleEntryComponent be : bnd.getEntry())
          result.add(be.getResource());
      else {
        FHIRPathEngine fpe = new FHIRPathEngine(context);
        ExpressionNode node = fpe.parse(getSingleValue(fhirpath));
        for (BundleEntryComponent be : bnd.getEntry())
          if (fpe.evaluateToBoolean(null, be.getResource(), be.getResource(), node))
            result.add(be.getResource());
      }
    }
    return result;
  }

  private List<Resource> filterResources(Argument fhirpath, List<Resource> list) throws EGraphQLException, FHIRException {
    List<Resource> result = new ArrayList<Resource>();
    if (list.size() > 0) {
      if ((fhirpath == null))
        for (Resource v : list)
          result.add(v);
      else {
        FHIRPathEngine fpe = new FHIRPathEngine(context);
        ExpressionNode node = fpe.parse(getSingleValue(fhirpath));
        for (Resource v : list)
          if (fpe.evaluateToBoolean(null, v, v, node))
            result.add(v);
      }
    }
    return result;
  }

  private boolean hasArgument(List<Argument> arguments, String name, String value) {
    for (Argument arg : arguments)
      if ((arg.getName().equals(name)) && arg.hasValue(value))
        return true;
    return false;
  }

  private void processValues(Resource context, Selection sel, Property prop, ObjectValue target, List<Base> values, boolean extensionMode) throws EGraphQLException, FHIRException {
    Argument arg = target.addField(sel.getField().getAlias(), prop.isList());
    for (Base value : values) {
      if (value.isPrimitive() && !extensionMode) {
        if (!sel.getField().getSelectionSet().isEmpty())
          throw new EGraphQLException("Encountered a selection set on a scalar field type");
        processPrimitive(arg, value);
      } else {
        if (sel.getField().getSelectionSet().isEmpty())
          throw new EGraphQLException("No Fields selected on a complex object");
        ObjectValue n = new ObjectValue();
        arg.addValue(n);
        processObject(context, value, n, sel.getField().getSelectionSet());
      }
    }
  }

  private void processVariables(Operation op) throws EGraphQLException {
    for (Variable varRef : op.getVariables()) {
      Argument varDef = null;
      for (Argument v : graphQL.getVariables()) 
        if (v.getName().equals(varRef.getName()))
          varDef = v;
      if (varDef != null)
        workingVariables.put(varRef.getName(), varDef); // todo: check type?
      else if (varRef.getDefaultValue() != null)
        workingVariables.put(varRef.getName(), new Argument(varRef.getName(), varRef.getDefaultValue()));
      else
        throw new EGraphQLException("No value found for variable ");
    }
  }

  private boolean isPrimitive(String typename) {
    return Utilities.existsInList(typename, "boolean", "integer", "string", "decimal", "uri", "base64Binary", "instant", "date", "dateTime", "time", "code", "oid", "id", "markdown", "unsignedInt", "positiveInt");
  }

  private boolean isResourceName(String name, String suffix) {
    if (!name.endsWith(suffix))
      return false;
    name = name.substring(0, name.length()-suffix.length());
    return context.getResourceNames().contains(name);
  }

  private void processObject(Resource context, Base source, ObjectValue target, List<Selection> selection) throws EGraphQLException, FHIRException {
    for (Selection sel : selection) {
      if (sel.getField() != null) {
        if (checkDirectives(sel.getField().getDirectives())) {
          Property prop = source.getNamedProperty(sel.getField().getName());
          if ((prop == null) && sel.getField().getName().startsWith("_"))
            prop = source.getNamedProperty(sel.getField().getName().substring(1));
          if (prop == null) {
            if ((sel.getField().getName().equals("resourceType") && source instanceof Resource))
              target.addField("resourceType", false).addValue(new StringValue(source.fhirType()));
            else if ((sel.getField().getName().equals("id") && source instanceof Resource))
              target.addField("id", false).addValue(new StringValue(((Resource) source).getIdElement().getIdPart()));
            else if ((sel.getField().getName().equals("resource") && source.fhirType().equals("Reference")))
              processReference(context, source, sel.getField(), target);
            else if (isResourceName(sel.getField().getName(), "List") && (source instanceof Resource))
              processReverseReferenceList((Resource) source, sel.getField(), target);
            else if (isResourceName(sel.getField().getName(), "Connection") && (source instanceof Resource))
              processReverseReferenceSearch((Resource) source, sel.getField(), target);
            else
              throw new EGraphQLException("Unknown property "+sel.getField().getName()+" on "+source.fhirType());
          } else {
            if (!isPrimitive(prop.getTypeCode()) && sel.getField().getName().startsWith("_"))
              throw new EGraphQLException("Unknown property "+sel.getField().getName()+" on "+source.fhirType());

            List<Base> vl = filter(context, prop, sel.getField().getArguments(), prop.getValues(), sel.getField().getName().startsWith("_"));
            if (!vl.isEmpty())
              processValues(context, sel, prop, target, vl, sel.getField().getName().startsWith("_"));
          }
        }
      } else if (sel.getInlineFragment() != null) {
        if (checkDirectives(sel.getInlineFragment().getDirectives())) {
          if (Utilities.noString(sel.getInlineFragment().getTypeCondition()))
            throw new EGraphQLException("Not done yet - inline fragment with no type condition"); // cause why? why instanceof it even valid?
          if (source.fhirType().equals(sel.getInlineFragment().getTypeCondition())) 
            processObject(context, source, target, sel.getInlineFragment().getSelectionSet());
        }
      } else if (checkDirectives(sel.getFragmentSpread().getDirectives())) {
        Fragment fragment = graphQL.getDocument().fragment(sel.getFragmentSpread().getName());
        if (fragment == null)
          throw new EGraphQLException("Unable to resolve fragment "+sel.getFragmentSpread().getName());

        if (Utilities.noString(fragment.getTypeCondition()))
          throw new EGraphQLException("Not done yet - inline fragment with no type condition"); // cause why? why instanceof it even valid?
        if (source.fhirType().equals(fragment.getTypeCondition()))
          processObject(context, source, target, fragment.getSelectionSet());
      }
    }
  }

  private void processPrimitive(Argument arg, Base value) {
    String s = value.fhirType();
    if (s.equals("integer") || s.equals("decimal") || s.equals("unsignedInt") || s.equals("positiveInt"))
      arg.addValue(new NumberValue(value.primitiveValue()));
    else if (s.equals("boolean"))
      arg.addValue(new NameValue(value.primitiveValue()));
    else
      arg.addValue(new StringValue(value.primitiveValue()));
  }

  private void processReference(Resource context, Base source, Field field, ObjectValue target) throws EGraphQLException, FHIRException {
    if (!(source instanceof Reference))
      throw new EGraphQLException("Not done yet");
    if (services == null)
      throw new EGraphQLException("Resource Referencing services not provided");

    Reference ref = (Reference) source;
    ReferenceResolution<Resource> res = services.lookup(appInfo, context, ref);
    if (res != null) {
      if (targetTypeOk(field.getArguments(), res.getTarget())) {
        Argument arg = target.addField(field.getAlias(), false);
        ObjectValue obj = new ObjectValue();
        arg.addValue(obj);
        processObject(res.getTargetContext(), res.getTarget(), obj, field.getSelectionSet());
      }
    }
    else if (!hasArgument(field.getArguments(), "optional", "true"))
      throw new EGraphQLException("Unable to resolve reference to "+ref.getReference());
  }

  private void processReverseReferenceList(Resource source, Field field, ObjectValue target) throws EGraphQLException, FHIRException {
    if (services == null)
      throw new EGraphQLException("Resource Referencing services not provided");
    List<Resource> list = new ArrayList<Resource>();
    List<Argument> params = new ArrayList<Argument>();
    Argument parg = null;
    for (Argument a : field.getArguments())
      if (!(a.getName().equals("_reference")))
        params.add(a);
      else if ((parg == null))
        parg = a;
      else
        throw new EGraphQLException("Duplicate parameter _reference");
    if (parg == null)
      throw new EGraphQLException("Missing parameter _reference");
    Argument arg = new Argument();
    params.add(arg);
    arg.setName(getSingleValue(parg));
    arg.addValue(new StringValue(source.fhirType()+"/"+source.getId()));
    services.listResources(appInfo, field.getName().substring(0, field.getName().length() - 4), params, list);
    arg = null;
    ObjectValue obj = null;

    List<Resource> vl = filterResources(field.argument("fhirpath"), list);
    if (!vl.isEmpty()) {
      arg = target.addField(field.getAlias(), true);
      for (Resource v : vl) {
        obj = new ObjectValue();
        arg.addValue(obj);
        processObject(v, v, obj, field.getSelectionSet());
      }
    }
  }
  
  private void processReverseReferenceSearch(Resource source, Field field, ObjectValue target) throws EGraphQLException, FHIRException {
    if (services == null)
      throw new EGraphQLException("Resource Referencing services not provided");
    List<Argument> params = new ArrayList<Argument>();
    Argument parg = null;
    for (Argument a : field.getArguments())
      if (!(a.getName().equals("_reference")))
        params.add(a);
      else if ((parg == null))
        parg = a;
      else
        throw new EGraphQLException("Duplicate parameter _reference");
    if (parg == null)
      throw new EGraphQLException("Missing parameter _reference");
    Argument arg = new Argument();
    params.add(arg);
    arg.setName(getSingleValue(parg));
    arg.addValue(new StringValue(source.fhirType()+"/"+source.getId()));
    Bundle bnd = services.search(appInfo, field.getName().substring(0, field.getName().length()-10), params);
    Base bndWrapper = new SearchWrapper(field.getName(), bnd);
    arg = target.addField(field.getAlias(), false);
    ObjectValue obj = new ObjectValue();
    arg.addValue(obj);
    processObject(null, bndWrapper, obj, field.getSelectionSet());
  }

  private void processSearch(ObjectValue target, List<Selection> selection) throws EGraphQLException, FHIRException {
    for (Selection sel : selection) {
      if ((sel.getField() == null))
        throw new EGraphQLException("Only field selections are allowed in this context");
      checkNoDirectives(sel.getField().getDirectives());

      if ((isResourceName(sel.getField().getName(), "")))
        processSearchSingle(target, sel.getField());
      else if ((isResourceName(sel.getField().getName(), "List")))
        processSearchSimple(target, sel.getField());
      else if ((isResourceName(sel.getField().getName(), "Connection")))
        processSearchFull(target, sel.getField());
    }
  }

  private void processSearchSingle(ObjectValue target, Field field) throws EGraphQLException, FHIRException {
    if (services == null)
      throw new EGraphQLException("Resource Referencing services not provided");
    String id = "";
    for (Argument arg : field.getArguments())
      if ((arg.getName().equals("id")))
        id = getSingleValue(arg);
      else
        throw new EGraphQLException("Unknown/invalid parameter "+arg.getName());
    if (Utilities.noString(id))
      throw new EGraphQLException("No id found");
    Resource res = services.lookup(appInfo, field.getName(), id);
    if (res == null)
      throw new EGraphQLException("Resource "+field.getName()+"/"+id+" not found");
    Argument arg = target.addField(field.getAlias(), false);
    ObjectValue obj = new ObjectValue();
    arg.addValue(obj);
    processObject(res, res, obj, field.getSelectionSet());
  }

  private void processSearchSimple(ObjectValue target, Field field) throws EGraphQLException, FHIRException {
    if (services == null)
      throw new EGraphQLException("Resource Referencing services not provided");
    List<Resource> list = new ArrayList<Resource>();
    services.listResources(appInfo, field.getName().substring(0, field.getName().length() - 4), field.getArguments(), list);
    Argument arg = null;
    ObjectValue obj = null;

    List<Resource> vl = filterResources(field.argument("fhirpath"), list);
    if (!vl.isEmpty()) {
      arg = target.addField(field.getAlias(), true);
      for (Resource v : vl) {
        obj = new ObjectValue();
        arg.addValue(obj);
        processObject(v, v, obj, field.getSelectionSet());
      }
    }
  }
  
  private void processSearchFull(ObjectValue target, Field field) throws EGraphQLException, FHIRException {
    if (services == null)
      throw new EGraphQLException("Resource Referencing services not provided");
    List<Argument> params = new ArrayList<Argument>();
    Argument carg = null;
    for ( Argument arg : field.getArguments())
      if (arg.getName().equals("cursor"))
        carg = arg;
      else
        params.add(arg);
    if ((carg != null)) {
      params.clear();;
      String[] parts = getSingleValue(carg).split(":");
      params.add(new Argument("search-id", new StringValue(parts[0])));
      params.add(new Argument("search-offset", new StringValue(parts[1])));
    }

    Bundle bnd = services.search(appInfo, field.getName().substring(0, field.getName().length()-10), params);
    SearchWrapper bndWrapper = new SearchWrapper(field.getName(), bnd);
    Argument arg = target.addField(field.getAlias(), false);
    ObjectValue obj = new ObjectValue();
    arg.addValue(obj);
    processObject(null, bndWrapper, obj, field.getSelectionSet());
  }

  private String getSingleValue(Argument arg) throws EGraphQLException {
    List<Value> vl = resolveValues(arg, 1);
    if (vl.size() == 0)
      return "";
    return vl.get(0).toString();
  }

  private List<Value> resolveValues(Argument arg) throws EGraphQLException {
    return resolveValues(arg, -1, "");
  }
  
  private List<Value> resolveValues(Argument arg, int max) throws EGraphQLException {
    return resolveValues(arg, max, "");
  }
  
  private List<Value> resolveValues(Argument arg, int max, String vars) throws EGraphQLException {
    List<Value> result = new ArrayList<Value>();
    for (Value v : arg.getValues()) {
      if (! (v instanceof VariableValue))
        result.add(v);
      else {
        if (vars.contains(":"+v.toString()+":"))
          throw new EGraphQLException("Recursive reference to variable "+v.toString());
        Argument a = workingVariables.get(v.toString());
        if (a == null)
          throw new EGraphQLException("No value found for variable \""+v.toString()+"\" in \""+arg.getName()+"\"");
        List<Value> vl = resolveValues(a, -1, vars+":"+v.toString()+":");
        result.addAll(vl);
      }
    }
    if ((max != -1 && result.size() > max))
      throw new EGraphQLException("Only "+Integer.toString(max)+" values are allowed for \""+arg.getName()+"\", but "+Integer.toString(result.size())+" enoucntered");
    return result;
  }


  
  
  public Object getAppInfo() {
    return appInfo;
  }

  public void setAppInfo(Object appInfo) {
    this.appInfo = appInfo;
  }

  public Resource getFocus() {
    return focus;
  }

  public void setFocus(Resource focus) {
    this.focus = focus;
  }

  public Package getGraphQL() {
    return graphQL;
  }

  public void setGraphQL(Package graphQL) {
    this.graphQL = graphQL;
  }

  public ObjectValue getOutput() {
    return output;
  }

  public IGraphQLStorageServices getServices() {
    return services;
  }

  public void setServices(IGraphQLStorageServices services) {
    this.services = services;
  }


  //
//{ GraphQLSearchWrapper }
//
//constructor GraphQLSearchWrapper.Create(bundle : Bundle);
//var
//  s : String;
//{
//  inherited Create;
//  FBundle = bundle;
//  s = bundle_List.Matches["self"];
//  FParseMap = TParseMap.create(s.Substring(s.IndexOf("?")+1));
//}
//
//destructor GraphQLSearchWrapper.Destroy;
//{
//  FParseMap.free;
//  FBundle.Free;
//  inherited;
//}
//
//function GraphQLSearchWrapper.extractLink(name: String): String;
//var
//  s : String;
//  pm : TParseMap;
//{
//  s = FBundle_List.Matches[name];
//  if (s == "")
//    result = null
//  else
//  {
//    pm = TParseMap.create(s.Substring(s.IndexOf("?")+1));
//    try
//      result = String.Create(pm.GetVar("search-id")+":"+pm.GetVar("search-offset"));
//    finally
//      pm.Free;
//    }
//  }
//}
//
//function GraphQLSearchWrapper.extractParam(name: String; int : boolean): Base;
//var
//  s : String;
//{
//  s = FParseMap.GetVar(name);
//  if (s == "")
//    result = null
//  else if (int)
//    result = Integer.Create(s)
//  else
//    result = String.Create(s);
//}
//
//function GraphQLSearchWrapper.fhirType(): String;
//{
//  result = "*Connection";
//}
//
//  // http://test.fhir.org/r3/Patient?_format==text/xhtml&search-id==77c97e03-8a6c-415f-a63d-11c80cf73f&&active==true&_sort==_id&search-offset==50&_count==50
//
//function GraphQLSearchWrapper.getPropertyValue(propName: string): Property;
//var
//  list : List<GraphQLSearchEdge>;
//  be : BundleEntry;
//{
//  if (propName == "first")
//    result = Property.Create(self, propname, "string", false, String, extractLink("first"))
//  else if (propName == "previous")
//    result = Property.Create(self, propname, "string", false, String, extractLink("previous"))
//  else if (propName == "next")
//    result = Property.Create(self, propname, "string", false, String, extractLink("next"))
//  else if (propName == "last")
//    result = Property.Create(self, propname, "string", false, String, extractLink("last"))
//  else if (propName == "count")
//    result = Property.Create(self, propname, "integer", false, String, FBundle.totalElement)
//  else if (propName == "offset")
//    result = Property.Create(self, propname, "integer", false, Integer, extractParam("search-offset", true))
//  else if (propName == "pagesize")
//    result = Property.Create(self, propname, "integer", false, Integer, extractParam("_count", true))
//  else if (propName == "edges")
//  {
//    list = ArrayList<GraphQLSearchEdge>();
//    try
//      for be in FBundle.getEntry() do
//        list.add(GraphQLSearchEdge.create(be));
//      result = Property.Create(self, propname, "integer", true, Integer, List<Base>(list));
//    finally
//      list.Free;
//    }
//  }
//  else
//    result = null;
//}
//
//private void GraphQLSearchWrapper.SetBundle(const Value: Bundle);
//{
//  FBundle.Free;
//  FBundle = Value;
//}
//
//{ GraphQLSearchEdge }
//
//constructor GraphQLSearchEdge.Create(entry: BundleEntry);
//{
//  inherited Create;
//  FEntry = entry;
//}
//
//destructor GraphQLSearchEdge.Destroy;
//{
//  FEntry.Free;
//  inherited;
//}
//
//function GraphQLSearchEdge.fhirType(): String;
//{
//  result = "*Edge";
//}
//
//function GraphQLSearchEdge.getPropertyValue(propName: string): Property;
//{
//  if (propName == "mode")
//  {
//    if (FEntry.search != null)
//      result = Property.Create(self, propname, "code", false, Enum, FEntry.search.modeElement)
//    else
//      result = Property.Create(self, propname, "code", false, Enum, Base(null));
//  }
//  else if (propName == "score")
//  {
//    if (FEntry.search != null)
//      result = Property.Create(self, propname, "decimal", false, Decimal, FEntry.search.scoreElement)
//    else
//      result = Property.Create(self, propname, "decimal", false, Decimal, Base(null));
//  }
//  else if (propName == "resource")
//    result = Property.Create(self, propname, "resource", false, Resource, FEntry.getResource())
//  else
//    result = null;
//}
//
//private void GraphQLSearchEdge.SetEntry(const Value: BundleEntry);
//{
//  FEntry.Free;
//  FEntry = value;
//}
//
}
