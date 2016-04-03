package org.hl7.fhir.dstu3.utils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hl7.fhir.dstu3.exceptions.DefinitionException;
import org.hl7.fhir.dstu3.exceptions.FHIRException;
import org.hl7.fhir.dstu3.exceptions.PathEngineException;
import org.hl7.fhir.dstu3.model.Base;
import org.hl7.fhir.dstu3.model.BooleanType;
import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.DecimalType;
import org.hl7.fhir.dstu3.model.Element;
import org.hl7.fhir.dstu3.model.ElementDefinition;
import org.hl7.fhir.dstu3.model.ElementDefinition.TypeRefComponent;
import org.hl7.fhir.dstu3.model.ExpressionNode;
import org.hl7.fhir.dstu3.model.ExpressionNode.Function;
import org.hl7.fhir.dstu3.model.ExpressionNode.Kind;
import org.hl7.fhir.dstu3.model.ExpressionNode.Operation;
import org.hl7.fhir.dstu3.model.ExpressionNode.SourceLocation;
import org.hl7.fhir.dstu3.model.Factory;
import org.hl7.fhir.dstu3.model.IntegerType;
import org.hl7.fhir.dstu3.model.Reference;
import org.hl7.fhir.dstu3.model.Resource;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.StructureDefinition;
import org.hl7.fhir.dstu3.model.Type;
import org.hl7.fhir.utilities.Table;
import org.hl7.fhir.utilities.Utilities;

/**
 * 
 * @author Grahame Grieve
 *
 */
public class FHIRPathEngine {

	private IWorkerContext worker;
	private IConstantResolver constantResolver;
	protected boolean mappingExtensions;
	private StringBuilder log = new StringBuilder();

	// if the fhir path expressions are allowed to use constants beyond those defined in the specification
	// the application can implement them by providing a constant resolver 
	public interface IConstantResolver {
		public Type resolveConstant(Object appContext, String name);
		public String resolveConstantType(Object appContext, String name);
	}


	/**
	 * @param worker - used when validating paths (@check), and used doing value set membership when executing tests (once that's defined)
	 */
	public FHIRPathEngine(IWorkerContext worker) {
		super();
		this.worker = worker;
	}


	// --- 3 methods to override in children -------------------------------------------------------
	// if you don't override, it falls through to the using the base reference implementation 
	// HAPI overrides to these to support extensing the base model

	public IConstantResolver getConstantResolver() {
		return constantResolver;
	}


	public void setConstantResolver(IConstantResolver constantResolver) {
		this.constantResolver = constantResolver;
	}


	/**
	 * Given an item, return all the children that conform to the pattern described in name
	 * 
	 * Possible patterns:
	 *  - a simple name (which may be the base of a name with [] e.g. value[x])
	 *  - a name with a type replacement e.g. valueCodeableConcept
	 *  - * which means all children
	 *  - ** which means all descendents
	 *  
	 * @param item
	 * @param name
	 * @param result
	 */
	protected void getChildrenByName(Base item, String name, List<Base> result) {
		for (Base v : item.listChildrenByName(name))
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
	public ExpressionNode parse(String path) throws PathEngineException {
		Lexer lexer = new Lexer(path);
		if (lexer.done())
			throw lexer.error("Path cannot be empty");
		ExpressionNode result = parseExpression(lexer, true);
		if (!lexer.done())
			throw lexer.error("Premature ExpressionNode termination at unexpected token \""+lexer.current+"\"");
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
	public Set<String> check(Object appContext, String resourceType, String context, String path, boolean xPathStartsWithValueRef) throws PathEngineException, DefinitionException {
		ExpressionNode expr = parse(path);
		Set<String> types = new HashSet<String>();
		if (xPathStartsWithValueRef && context.contains(".") && path.startsWith(context.substring(context.lastIndexOf(".")+1)))
			types.add(context.substring(0, context.lastIndexOf(".")));
		else 
			types.add(context);
		return executeType(new ExecutionTypeContext(appContext, resourceType, context), types, expr, true);
	}

	/**
	 * evaluate a path and return the matching elements
	 * 
	 * @param base - the object against which the path is being evaluated
	 * @param ExpressionNode - the parsed ExpressionNode statement to use
	 * @return
	 * @throws PathEngineException 
	 * @
	 */
	public List<Base> evaluate(Base base, ExpressionNode ExpressionNode) throws PathEngineException {
		List<Base> list = new ArrayList<Base>();
		if (base != null)
			list.add(base);
		log = new StringBuilder();
		return execute(new ExecutionContext(null, null, base), list, ExpressionNode, true);
	}

	/**
	 * evaluate a path and return the matching elements
	 * 
	 * @param base - the object against which the path is being evaluated
	 * @param path - the FHIR Path statement to use
	 * @return
	 * @throws PathEngineException 
	 * @
	 */
	public List<Base> evaluate(Base base, String path) throws PathEngineException {
		ExpressionNode exp = parse(path);
		List<Base> list = new ArrayList<Base>();
		if (base != null)
			list.add(base);
		log = new StringBuilder();
		return execute(new ExecutionContext(null, null, base), list, exp, true);
	}

	/**
	 * evaluate a path and return the matching elements
	 * 
	 * @param base - the object against which the path is being evaluated
	 * @param ExpressionNode - the parsed ExpressionNode statement to use
	 * @return
	 * @throws PathEngineException 
	 * @
	 */
	public List<Base> evaluate(Object appContext, Resource resource, Base base, ExpressionNode ExpressionNode) throws PathEngineException {
		List<Base> list = new ArrayList<Base>();
		if (base != null)
			list.add(base);
		log = new StringBuilder();
		return execute(new ExecutionContext(appContext, resource, base), list, ExpressionNode, true);
	}

	/**
	 * evaluate a path and return the matching elements
	 * 
	 * @param base - the object against which the path is being evaluated
	 * @param path - the FHIR Path statement to use
	 * @return
	 * @throws PathEngineException 
	 * @
	 */
	public List<Base> evaluate(Object appContext, Resource resource, Base base, String path) throws PathEngineException {
		ExpressionNode exp = parse(path);
		List<Base> list = new ArrayList<Base>();
		if (base != null)
			list.add(base);
		log = new StringBuilder();
		return execute(new ExecutionContext(appContext, resource, base), list, exp, true);
	}

	/**
	 * evaluate a path and return true or false (e.g. for an invariant)
	 * 
	 * @param base - the object against which the path is being evaluated
	 * @param path - the FHIR Path statement to use
	 * @return
	 * @throws PathEngineException 
	 * @
	 */
	public boolean evaluateToBoolean(Resource resource, Base base, String path) throws PathEngineException {
		return convertToBoolean(evaluate(null, resource, base, path));
	}

	/**
	 * evaluate a path and a string containing the outcome (for display)
	 * 
	 * @param base - the object against which the path is being evaluated
	 * @param path - the FHIR Path statement to use
	 * @return
	 * @throws PathEngineException 
	 * @
	 */
	public String evaluateToString(Base base, String path) throws PathEngineException {
		return convertToString(evaluate(base, path));
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
			return item.getClass().getName();
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


	private void log(String name, String contents) {
		if (log.length() > 0)
			log.append("; ");
		log.append(name);
		log.append(": ");
		log.append(contents);
	}
	
	public String forLog() {
		if (log.length() > 0)
			return " ("+log.toString()+")";
		else
		  return "";
		}
	
	private class Lexer {
		private String path;
		private int cursor;
		private int currentStart;
		private String current;
		private SourceLocation currentLocation;
		private SourceLocation currentStartLocation;
		private int id;

		public Lexer(String source) throws PathEngineException {
			this.path = source;
			currentLocation = new SourceLocation(1, 1);
			next();
		}
		public String getCurrent() {
			return current;
		}
		public SourceLocation getCurrentLocation() {
			return currentLocation;
		}

		public boolean isConstant() {
			return current.charAt(0) == '\'' || current.charAt(0) == '%' || (current.charAt(0) >= '0' && current.charAt(0) <= '9') || current.equals("true") || current.equals("false");
		}

		public String take() throws PathEngineException {
			String s = current;
			next();
			return s;
		}

		public boolean isToken() {
			if (Utilities.noString(current))
				return false;

			if (current.startsWith("$"))
				return true;

			if (current.equals("*") || current.equals("**"))
				return true;

			if ((current.charAt(0) >= 'A' && current.charAt(0) <= 'Z') || (current.charAt(0) >= 'a' && current.charAt(0) <= 'z')) {
				for (int i = 1; i < current.length(); i++) 
					if (!( (current.charAt(1) >= 'A' && current.charAt(1) <= 'Z') || (current.charAt(1) >= 'a' && current.charAt(1) <= 'z') ||
							(current.charAt(1) >= '0' && current.charAt(1) <= '9')) || current.charAt(1) == '[' || current.charAt(1) == ']' || (current.charAt(1) == '*') && (i == current.length()-1))
						return false;
				return true;
			}
			return false;
		}

		public PathEngineException error(String msg) {
			return error(msg, currentLocation.toString());
		}

		private PathEngineException error(String msg, String location) {
			return new PathEngineException("Error in "+path+" at "+location+": "+msg);
		}

		public void next() throws PathEngineException {
			current = null;
			boolean last13 = false;
			while (cursor < path.length() && Character.isWhitespace(path.charAt(cursor))) {
				if (path.charAt(cursor) == '\r') {
					currentLocation.setLine(currentLocation.getLine() + 1);
					currentLocation.setColumn(1);
					last13 = true;
				} else if (!last13 && (path.charAt(cursor) == '\n')) {
					currentLocation.setLine(currentLocation.getLine() + 1);
					currentLocation.setColumn(1);
					last13 = false;
				} else {
					last13 = false;
					currentLocation.setColumn(currentLocation.getColumn() + 1);
				}
				cursor++;
			}
			currentStart = cursor;
			currentStartLocation = currentLocation;
			if (cursor < path.length()) {
				char ch = path.charAt(cursor);
				if (ch == '!' || ch == '>' || ch == '<' || ch == ':')  {
					cursor++;
					if (cursor < path.length() && path.charAt(cursor) == '=') 
						cursor++;
					current = path.substring(currentStart, cursor);
				} else if (ch == '*') {
					cursor++;
					if (cursor < path.length() && path.charAt(cursor) == '*') 
						cursor++;
					current = path.substring(currentStart, cursor);
				} else if (ch == '-') {
					cursor++;
					if (cursor < path.length() && path.charAt(cursor) == '>') 
						cursor++;
					current = path.substring(currentStart, cursor);
				} else if (ch >= '0' && ch <= '9') {
					while (cursor < path.length() && ((path.charAt(cursor) >= '0' && path.charAt(cursor) <= '9') || path.charAt(cursor) == '.')) 
						cursor++;
					current = path.substring(currentStart, cursor);
				}  else if ((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
					while (cursor < path.length() && ((path.charAt(cursor) >= 'A' && path.charAt(cursor) <= 'Z') || (path.charAt(cursor) >= 'a' && path.charAt(cursor) <= 'z') || 
							(path.charAt(cursor) >= '0' && path.charAt(cursor) <= '9'))) 
						cursor++;
					current = path.substring(currentStart, cursor);
				} else if (ch == '%') {
					cursor++;
					while (cursor < path.length() && ((path.charAt(cursor) >= 'A' && path.charAt(cursor) <= 'Z') || (path.charAt(cursor) >= 'a' && path.charAt(cursor) <= 'z') || 
							(path.charAt(cursor) >= '0' && path.charAt(cursor) <= '9') || path.charAt(cursor) == ':' || path.charAt(cursor) == '-'))
						cursor++;
					current = path.substring(currentStart, cursor);
				} else if (ch == '$') {
					cursor++;
					while (cursor < path.length() && (path.charAt(cursor) >= 'a' && path.charAt(cursor) <= 'z'))
						cursor++;
					current = path.substring(currentStart, cursor);
				} else if (/*ch == '"' || */ ch == '\''){
					cursor++;
					char ech = ch;
					boolean escape = false;
					while (cursor < path.length() && (escape || path.charAt(cursor) != ech)) {
						if (escape)
							escape = false;
						else 
							escape = (path.charAt(cursor) == '\\');
						cursor++;
					}
					if (cursor == path.length())
						throw error("Unterminated string");
					cursor++;
					current = path.substring(currentStart, cursor);
					if (ech == '\'')
						current = "\'"+current.substring(1, current.length() - 1)+"\'";
				} else { // if CharInSet(ch, ['.', ',', '(', ')', '=', '$']) then
					cursor++;
					current = path.substring(currentStart, cursor);
				}
			}
		}


		public boolean isOp() {
			return ExpressionNode.Operation.fromCode(current) != null;
		}
		public boolean done() {
			return currentStart >= path.length();
		}
		public int nextId() {
			id++;
			return id;
		}

	}

	private class ExecutionContext {
		private Object appInfo;
		private Resource resource;
		private Base context;
		public ExecutionContext(Object appInfo, Resource resource, Base context) {
			this.appInfo = appInfo;
			this.resource = resource; 
			this.context = context;
		}
		public Resource getResource() {
			return resource;
		}
		public Base getContext() {
			return context;
		}
	}

	private class ExecutionTypeContext {
		private Object appInfo; 
		private String resource;
		private String context;
		private String value; // for mapping


		public ExecutionTypeContext(Object appInfo, String resource, String context) {
			super();
			this.appInfo = appInfo;
			this.resource = resource;
			this.context = context;
		}
		public String getResource() {
			return resource;
		}
		public String getContext() {
			return context;
		}
	}

	private ExpressionNode parseExpression(Lexer lexer, boolean proximal) throws PathEngineException {
		ExpressionNode result = new ExpressionNode(lexer.nextId());
		SourceLocation c = lexer.currentStartLocation;
		result.setStart(lexer.getCurrentLocation());
		if (lexer.isConstant()) {
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
			if (!lexer.isToken()) 
				throw lexer.error("Found "+lexer.getCurrent()+" expecting a token name");
			result.setName(lexer.take());
			result.setEnd(lexer.getCurrentLocation());
			if (!result.checkName(mappingExtensions))
				throw lexer.error("Found "+result.getName()+" expecting a valid token name");
			if ("(".equals(lexer.getCurrent())) {
				Function f = Function.fromCode(result.getName());  
				if (f == null)
					throw lexer.error("The name "+result.getName()+" is not a valid function name");
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
				checkParameters(lexer, c, result);
			} else
				result.setKind(Kind.Name);
		}
		ExpressionNode focus = result;
    if ("[".equals(lexer.current)) {
      lexer.next();
      ExpressionNode item = new ExpressionNode(lexer.nextId());
      item.setKind(Kind.Function);
      item.setFunction(ExpressionNode.Function.Item);
      item.getParameters().add(parseExpression(lexer, true));
      if (!lexer.current.equals("]"))
        throw lexer.error("The token "+lexer.getCurrent()+" is not expected here - either a \"]\" expected");
      lexer.next();
      result.setInner(item);
      focus = item;
    }
		if (".".equals(lexer.current)) {
			lexer.next();
			focus.setInner(parseExpression(lexer, false));
		}
		result.setProximal(proximal);
		if (proximal) {
			while (lexer.isOp()) {
				focus.setOperation(ExpressionNode.Operation.fromCode(lexer.getCurrent()));
				focus.setOpStart(lexer.currentStartLocation);
				focus.setOpEnd(lexer.currentLocation);
				lexer.next();
				focus.setOpNext(parseExpression(lexer, false));
				focus = focus.getOpNext();
			}
			result = organisePrecedence(lexer, result);
		}
		return result;
	}

	private ExpressionNode organisePrecedence(Lexer lexer, ExpressionNode node) {
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Times, Operation.DivideBy, Operation.Div, Operation.Mod)); 
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Plus, Operation.Minus)); 
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Union)); 
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.LessThen, Operation.Greater, Operation.LessOrEqual, Operation.GreaterOrEqual));
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Is));
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Equals, Operation.Equivalent, Operation.NotEquals, Operation.NotEquivalent));
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.And));
    node = gatherPrecedence(lexer, node, EnumSet.of(Operation.Xor, Operation.Or));
    // last: implies
		return node;
	}

	private ExpressionNode gatherPrecedence(Lexer lexer, ExpressionNode start, EnumSet<Operation> ops) {
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
					while (focus == null && !ops.contains(focus.getOperation())) {
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


	private ExpressionNode newGroup(Lexer lexer, ExpressionNode next) {
		ExpressionNode result = new ExpressionNode(lexer.nextId());
		result.setKind(Kind.Group);
		result.setGroup(next);
		result.getGroup().setProximal(true);
		return result;
	}

	private void checkConstant(String s, Lexer lexer) throws PathEngineException {
		if (s.startsWith("\'") && s.endsWith("\'")) {
			boolean inEscape = false;
			for (int i = 1; i < s.length()-1; i++) {
				char ch = s.charAt(i);
				if (inEscape) { 
					switch (ch) {
					case 't': inEscape = false; break;
					case 'r': inEscape = false; break;
					case 'n': inEscape = false; break;
					case '\'': inEscape = false; break;
					case '"': inEscape = false; break;
					case '\\': inEscape = false; break;
					default: throw lexer.error("Unknown character escape \\"+ch);
					}
					inEscape = false;
				} else if (ch == '\\')
					inEscape = true;
			}
		}
	}

	//  procedure CheckParamCount(c : integer);
	//  begin
	//    if exp.Parameters.Count <> c then
	//      raise lexer.error('The function "'+exp.name+'" requires '+inttostr(c)+' parameters', offset);
	//  end;

	private boolean checkParamCount(Lexer lexer, SourceLocation location, ExpressionNode exp, int count) throws PathEngineException {
		if (exp.getParameters().size() != count)
			throw lexer.error("The function \""+exp.getName()+"\" requires "+Integer.toString(count)+" parameters", location.toString());
		return true;
	}

	private boolean checkParamCount(Lexer lexer, SourceLocation location, ExpressionNode exp, int countMin, int countMax) throws PathEngineException {
		if (exp.getParameters().size() < countMin || exp.getParameters().size() > countMax)
			throw lexer.error("The function \""+exp.getName()+"\" requires between "+Integer.toString(countMin)+" and "+Integer.toString(countMax)+" parameters", location.toString());
		return true;
	}

	private boolean checkParameters(Lexer lexer, SourceLocation location, ExpressionNode exp) throws PathEngineException {
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
		case All: return checkParamCount(lexer, location, exp, 1);
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
    case Descendents: return checkParamCount(lexer, location, exp, 0);
    case MemberOf: return checkParamCount(lexer, location, exp, 1);
    case Trace: return checkParamCount(lexer, location, exp, 1);
    case Today: return checkParamCount(lexer, location, exp, 0);
    case Now: return checkParamCount(lexer, location, exp, 0);
    case Resolve: return checkParamCount(lexer, location, exp, 0);
    case Extension: return checkParamCount(lexer, location, exp, 0);
		}
		return false;
	}

	private List<Base> execute(ExecutionContext context, List<Base> focus, ExpressionNode exp, boolean atEntry) throws PathEngineException {
		List<Base> work = new ArrayList<Base>();
		switch (exp.getKind()) {
		case Name:
			if (exp.getName().equals("$resource"))
				work.add(context.getResource());
			else if (exp.getName().equals("$context"))
				work.add(context.getContext());
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
			work.add(processConstant(context.appInfo, exp.getConstant()));
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
				else {
					work2 = execute(context, focus, next, false);
					work = operate(work, last.getOperation(), work2);
				}
					last = next;
					next = next.getOpNext();
				}
			}
		return work;
	}

	private List<Base> preOperate(List<Base> left, Operation operation) {
		switch (operation) {
		case And:
			return convertToBoolean(left) ? null : makeBoolean(false);
		case Or:
			return convertToBoolean(left) ? makeBoolean(true) : null;
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

	private Set<String> executeType(ExecutionTypeContext context, Set<String> focus, ExpressionNode exp, boolean atEntry) throws PathEngineException, DefinitionException {
		Set<String> result = new HashSet<String>();
		switch (exp.getKind()) {
		case Name:
			if (exp.getName().equals("$context"))
				result.add(context.getContext());
			else if (exp.getName().equals("$resource")) {
				if (!Utilities.noString(context.getResource())) 
					result.add(context.getResource());
				else
					result.add("DomainResource");
			} else {
				for (String s : focus) {
					result.addAll(executeType(s, exp, atEntry));
				}
				if (result.isEmpty()) 
					throw new PathEngineException("The name "+exp.getName()+" is not valid for any of the possible types: "+focus.toString());
			}
			break;
		case Function:
			result.addAll(evaluateFunctionType(context, focus, exp));
			break;
		case Constant:
			result.add(readConstantType(context.appInfo, exp.getConstant()));
			break;
		case Group:
			result.addAll(executeType(context, focus, exp.getGroup(), atEntry));
		}
		exp.setTypes(result);

		if (exp.getInner() != null) {
			result = executeType(context, result, exp.getInner(), false);
		}

		if (exp.isProximal() && exp.getOperation() != null) {
			ExpressionNode next = exp.getOpNext();
			ExpressionNode last = exp;
			while (next != null) {
				Set<String> work = executeType(context, focus, next, atEntry);
				result = operateTypes(result, last.getOperation(), work);
				last = next;
				next = next.getOpNext();
			}
			exp.setOpTypes(result);
		}
		return result;
	}

	private Base processConstant(Object appInfo, String constant) throws PathEngineException {
		if (constant.equals("true")) {
			return new BooleanType(true);
		} else if (constant.equals("false")) {
			return new BooleanType(false);
		} else if (Utilities.isInteger(constant)) {
			return new IntegerType(constant);
		} if (Utilities.isDecimal(constant)) {
			return new DecimalType(constant);
		} if (constant.startsWith("\'")) {
			return new StringType(processConstantString(constant));
		} if (constant.startsWith("%")) {
			return resolveConstant(appInfo, constant);
		} else {
			return new StringType(constant);
		}
	}

	private Base resolveConstant(Object appInfo, String s) throws PathEngineException {
		if (s.equals("%sct"))
			return new StringType("\"http://snomed.info/sct\"");
		else if (s.equals("%loinc"))
			return new StringType("\"http://loinc.org\"");
		else if (s.equals("%ucum"))
			return new StringType("\"http://unitsofmeasure.org\"");
		else if (s.equals("%us-zip"))
			return new StringType("\"[0-9]{5}(-[0-9]{4}){0,1}\"");
		else if (s.startsWith("%vs-"))
			return new StringType("\"http://hl7.org/fhir/ValueSet/"+s.substring(4)+"\"");
		else if (s.startsWith("%ext-"))
			return new StringType("\"http://hl7.org/fhir/StructureDefinition/"+s.substring(5)+"\"");
		else if (constantResolver == null)
			throw new PathEngineException("Unknown fixed constant '"+s+"'");
		else
			return constantResolver.resolveConstant(appInfo, s);
	}


	private String processConstantString(String s) {
		StringBuilder b = new StringBuilder();
		boolean e = false;
		for (int i = 1; i < s.length()-1; i++) {
			char ch = s.charAt(i);
			if (e) {
				switch (ch) {
				case 't': b.append('\t');
				case 'r': b.append('\r');
				case 'n': b.append('\n');
				case '\\': b.append('\\');
				case '\'': b.append('\'');
				case '"': b.append('"');
				default:
					throw new Error("Unknown character escape \\"+ch); // this has already ben checked
				}
			} else if (ch == '\\') {
				e = true;
			} else
				b.append(ch);
		}
		return b.toString();
	}


	private List<Base> operate(List<Base> left, Operation operation, List<Base> right) {
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
		case Or:  return opOr(left, right);
		case And:  return opAnd(left, right);
		case Xor: return opXor(left, right);
		case Implies: return opImplies(left, right);
		case Plus: return opPlus(left, right);
		case Minus: return opMinus(left, right);
		default: 
			throw new Error("Not Done Yet: "+operation.toCode());
		}
	}

	private Set<String> operateTypes(Set<String> left, Operation operation, Set<String> right) {
		switch (operation) {
		case Equals: return typeSet("boolean");
		case Equivalent: return typeSet("boolean");
		case NotEquals: return typeSet("boolean");
		case NotEquivalent: return typeSet("boolean");
		case LessThen: return typeSet("boolean");
		case Greater: return typeSet("boolean");
		case LessOrEqual: return typeSet("boolean");
		case GreaterOrEqual: return typeSet("boolean");
    case Is: return typeSet("boolean");
    case As: throw new Error("not done yet 'as'");
    case Union: return union(left, right);
		case Or: return typeSet("boolean");
		case And: return typeSet("boolean");
		case Xor: return typeSet("boolean");
		case Implies : return typeSet("boolean");
    case Times: throw new Error("not done yet '*'");
    case DivideBy: throw new Error("not done yet '/'");
    case Plus: 
      if (typeIsInteger(left) && typeIsInteger(right))
        return typeSet("integer");
      else if (typeIsDecimal(left) && typeIsDecimal(right))
        return typeSet("decimal");
      else 
        return typeSet("string");
    case Minus: throw new Error("not done yet '-'");
    case Div: throw new Error("not done yet 'div'");
    case Mod: throw new Error("not done yet 'mod'");
    case In: return typeSet("boolean");
		default: 
			return null;
		}
	}

	private boolean typeIsDecimal(Set<String> left) {
    return left.size() == 1 && (left.contains("integer") || left.contains("decimal"));
  }


  private boolean typeIsInteger(Set<String> left) {
    return left.size() == 1 && left.contains("integer");
  }


	private Set<String> union(Set<String> left, Set<String> right) {
		Set<String> result = new HashSet<String>();
		result.addAll(left);
		result.addAll(right);
		return result;
	}

	private Set<String> typeSet(String string) {
		Set<String> result = new HashSet<String>();
		result.add(string);
		return result;
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

		boolean res = false;
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
			return left.primitiveValue().equals(right.primitiveValue());
		else
			return Base.compareDeep(left, right, false);
	}

	private List<Base> opEquivalent(List<Base> left, List<Base> right) {
		throw new Error("The operation Equivalent is not done yet");
	}

	private List<Base> opNotEquivalent(List<Base> left, List<Base> right) {
		throw new Error("The operation NotEquivalent is not done yet");
	}

	private List<Base> opLessThen(List<Base> left, List<Base> right) {
		if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
			Base l = left.get(0);
			Base r = right.get(0);
			if (l.hasType("string") && r.hasType("string")) 
				return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) < 0);
			else if ((l.hasType("integer") || l.hasType("decimal")) && (r.hasType("integer") || r.hasType("decimal"))) 
				return makeBoolean(new Double(l.primitiveValue()) < new Double(r.primitiveValue()));
			else if ((l.hasType("date") || l.hasType("dateTime") || l.hasType("instant")) && (r.hasType("date") || r.hasType("dateTime") || r.hasType("instant"))) 
				return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) < 0);
		} else if (left.size() == 1 && right.size() == 1 && left.get(0).fhirType().equals("Quantity") && right.get(0).fhirType().equals("Quantity") ) {
			List<Base> lUnit = left.get(0).listChildrenByName("unit");
			List<Base> rUnit = right.get(0).listChildrenByName("unit");
			if (Base.compareDeep(lUnit, rUnit, true)) {
				return opLessThen(left.get(0).listChildrenByName("value"), right.get(0).listChildrenByName("value"));
			} else {
				throw new Error("Canonical Comparison isn't done yet");
			}
		}
		return new ArrayList<Base>();
	}

	private List<Base> opGreater(List<Base> left, List<Base> right) {
		if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
			Base l = left.get(0);
			Base r = right.get(0);
			if (l.hasType("string") && r.hasType("string")) 
				return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) > 0);
			else if ((l.hasType("integer") || l.hasType("decimal")) && (r.hasType("integer") || r.hasType("decimal"))) 
				return makeBoolean(new Double(l.primitiveValue()) > new Double(r.primitiveValue()));
			else if ((l.hasType("date") || l.hasType("dateTime") || l.hasType("instant")) && (r.hasType("date") || r.hasType("dateTime") || r.hasType("instant"))) 
				return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) > 0);
		} else if (left.size() == 1 && right.size() == 1 && left.get(0).fhirType().equals("Quantity") && right.get(0).fhirType().equals("Quantity") ) {
			List<Base> lUnit = left.get(0).listChildrenByName("unit");
			List<Base> rUnit = right.get(0).listChildrenByName("unit");
			if (Base.compareDeep(lUnit, rUnit, true)) {
				return opGreater(left.get(0).listChildrenByName("value"), right.get(0).listChildrenByName("value"));
			} else {
				throw new Error("Canonical Comparison isn't done yet");
			}
		}
		return new ArrayList<Base>();
	}

	private List<Base> opLessOrEqual(List<Base> left, List<Base> right) {
		if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
			Base l = left.get(0);
			Base r = right.get(0);
			if (l.hasType("string") && r.hasType("string")) 
				return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) <= 0);
			else if ((l.hasType("integer") || l.hasType("decimal")) && (r.hasType("integer") || r.hasType("decimal"))) 
				return makeBoolean(new Double(l.primitiveValue()) <= new Double(r.primitiveValue()));
			else if ((l.hasType("date") || l.hasType("dateTime") || l.hasType("instant")) && (r.hasType("date") || r.hasType("dateTime") || r.hasType("instant"))) 
				return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) <= 0);
		} else if (left.size() == 1 && right.size() == 1 && left.get(0).fhirType().equals("Quantity") && right.get(0).fhirType().equals("Quantity") ) {
			List<Base> lUnits = left.get(0).listChildrenByName("unit");
			String lunit = lUnits.size() == 1 ? lUnits.get(0).primitiveValue() : null;
			List<Base> rUnits = right.get(0).listChildrenByName("unit");
			String runit = rUnits.size() == 1 ? rUnits.get(0).primitiveValue() : null;
			if ((lunit == null && runit == null) || lunit.equals(runit)) {
				return opLessOrEqual(left.get(0).listChildrenByName("value"), right.get(0).listChildrenByName("value"));
			} else {
				throw new Error("Canonical Comparison isn't done yet");
			}
		}
		return new ArrayList<Base>();
	}

	private List<Base> opGreaterOrEqual(List<Base> left, List<Base> right) {
		if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
			Base l = left.get(0);
			Base r = right.get(0);
			if (l.hasType("string") && r.hasType("string")) 
				return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) >= 0);
			else if ((l.hasType("integer") || l.hasType("decimal")) && (r.hasType("integer") || r.hasType("decimal"))) 
				return makeBoolean(new Double(l.primitiveValue()) >= new Double(r.primitiveValue()));
			else if ((l.hasType("date") || l.hasType("dateTime") || l.hasType("instant")) && (r.hasType("date") || r.hasType("dateTime") || r.hasType("instant"))) 
				return makeBoolean(l.primitiveValue().compareTo(r.primitiveValue()) >= 0);
		} else if (left.size() == 1 && right.size() == 1 && left.get(0).fhirType().equals("Quantity") && right.get(0).fhirType().equals("Quantity") ) {
			List<Base> lUnit = left.get(0).listChildrenByName("unit");
			List<Base> rUnit = right.get(0).listChildrenByName("unit");
			if (Base.compareDeep(lUnit, rUnit, true)) {
				return opGreaterOrEqual(left.get(0).listChildrenByName("value"), right.get(0).listChildrenByName("value"));
			} else {
				throw new Error("Canonical Comparison isn't done yet");
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

	private List<Base> opPlus(List<Base> left, List<Base> right) {
		List<Base> result = new ArrayList<Base>();
		if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
			Base l = left.get(0);
			Base r = right.get(0);
			if (l.hasType("string", "id", "code", "uri") && r.hasType("string", "id", "code", "uri")) 
				result.add(new StringType(l.primitiveValue() + r.primitiveValue()));
			else if ((l.hasType("integer") || l.hasType("decimal")) && (r.hasType("integer") || r.hasType("decimal"))) 
				if (Utilities.isInteger(l.primitiveValue()) && Utilities.isInteger(r.primitiveValue())) 
					result.add(new IntegerType(Integer.parseInt(l.primitiveValue()) + Integer.parseInt(r.primitiveValue())));
				else
					result.add(new DecimalType(new Double(l.primitiveValue()) + new Double(r.primitiveValue())));
		}
		return result;
	}

	private List<Base> opConcatenate(List<Base> left, List<Base> right) {
		List<Base> result = new ArrayList<Base>();
		if (left.size() == 1 && right.size() == 1 && left.get(0).isPrimitive() && right.get(0).isPrimitive()) {
			Base l = left.get(0);
			Base r = right.get(0);
  		result.add(new StringType(l.primitiveValue() + r.primitiveValue()));
		}
		return result;
	}

	private List<Base> opUnion(List<Base> left, List<Base> right) {
		List<Base> result = new ArrayList<Base>();
		result.addAll(left);
		result.addAll(right);
		return result;
	}

	private List<Base> opAnd(List<Base> left, List<Base> right) {
		return makeBoolean(convertToBoolean(left) && convertToBoolean(right));
	}

	private List<Base> opOr(List<Base> left, List<Base> right) {
		return makeBoolean(convertToBoolean(left) || convertToBoolean(right));
	}

	private List<Base> opXor(List<Base> left, List<Base> right) {
		return makeBoolean(convertToBoolean(left) ^ convertToBoolean(right));
	}

	private List<Base> opImplies(List<Base> left, List<Base> right) {
		if (convertToBoolean(left))
			return makeBoolean(convertToBoolean(right));
		else
			return makeBoolean(true);
	}


	private List<Base> opMinus(List<Base> left, List<Base> right) {
		throw new Error("The operation Minus is not done yet");
	}


	private String readConstantType(Object appContext, String constant) throws PathEngineException {
		if (constant.equals("true")) 
			return "boolean";
		else if (constant.equals("false")) 
			return "boolean";
		else if (Utilities.isInteger(constant))
			return "integer";
		else if (Utilities.isDecimal(constant))
			return "decimal";
		else if (constant.startsWith("%"))
			return resolveConstantType(appContext, constant);
		else
			return "string";
	}

	private String resolveConstantType(Object appContext, String s) throws PathEngineException {
		if (s.equals("%sct"))
			return "string";
		else if (s.equals("%loinc"))
			return "string";
		else if (s.equals("%ucum"))
			return "string";
		else if (s.equals("%map-codes"))
			return "string";
		else if (s.equals("%us-zip"))
			return "string";
		else if (s.startsWith("%vs-"))
			return "string";
		else if (s.startsWith("%ext-"))
			return "string";
		else if (constantResolver == null)
			throw new PathEngineException("Unknown fixed constant type for '"+s+"'");
		else
			return constantResolver.resolveConstantType(appContext, s);
	}

	private List<Base> execute(ExecutionContext context, Base item, ExpressionNode exp, boolean atEntry) {
		List<Base> result = new ArrayList<Base>(); 
		if (atEntry && Character.isUpperCase(exp.getName().charAt(0))) {// special case for start up
			if (item instanceof Resource && ((Resource) item).getResourceType().toString().equals(exp.getName()))  
				result.add(item);
		} else
			getChildrenByName(item, exp.getName(), result);
		return result;
	}	

	private Set<String> executeType(String type, ExpressionNode exp, boolean atEntry) throws PathEngineException, DefinitionException {
		Set<String> result = new HashSet<String>(); 
		if (atEntry && Character.isUpperCase(exp.getName().charAt(0))) {// special case for start up
			if (type.equals(exp.getName()))  
				result.add(type);
		} else
			getChildTypesByName(type, exp.getName(), result);
		return result;
	}


	@SuppressWarnings("unchecked")
	private Set<String> evaluateFunctionType(ExecutionTypeContext context, Set<String> focus, ExpressionNode exp) throws PathEngineException, DefinitionException {
		List<Set<String>> paramTypes = new ArrayList<Set<String>>();
		for (ExpressionNode expr : exp.getParameters()) {
			paramTypes.add(executeType(context, focus, expr, false));
		}
		switch (exp.getFunction()) {
		case Empty : return typeSet("boolean");
    case Not : return typeSet("boolean");
    case Exists : return typeSet("boolean");
    case SubsetOf : {checkParamTypes(exp.getFunction().toCode(), paramTypes, focus); return typeSet("boolean"); }
    case SupersetOf : {checkParamTypes(exp.getFunction().toCode(), paramTypes, focus); return typeSet("boolean"); }
    case IsDistinct : return typeSet("boolean");
    case Distinct : return focus;
    case Count : return  typeSet("integer");
		case Where : return focus;
    case Select : return anything();
		case All : return typeSet("boolean");
    case Repeat : return anything();
    case Item : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("integer")); return focus; }
    case As : throw new Error("not evaluated yet");
    case Is : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("identifier")); return typeSet("boolean"); }
    case Single : return focus;
		case First : return focus;
		case Last : return focus;
		case Tail : return focus;
    case Skip : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("integer")); return focus;}
    case Take : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("integer")); return focus;}
    case Iif : throw new Error("not evaluated yet");
    case ToInteger : return typeSet("integer");
    case ToDecimal : return typeSet("decimal");
    case ToString : return typeSet("string");
    case Substring : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("integer"), typeSet("integer")); return typeSet("string"); }
    case StartsWith : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string")); return typeSet("boolean"); }
    case EndsWith : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string")); return typeSet("boolean"); }
    case Matches : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string")); return typeSet("boolean"); }
    case ReplaceMatches : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string"), typeSet("string")); return typeSet("string"); }
    case Contains : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string")); return typeSet("boolean");}
    case Replace : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string"), typeSet("string")); return typeSet("string");}
		case Length : return typeSet("integer");
    case Children : return childTypes(focus, "*");
    case Descendents : return childTypes(focus, "**");
    case MemberOf : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string")); return typeSet("boolean");}
    case Trace : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string")); return focus; }
    case Today : return typeSet("datetime");
    case Now : return typeSet("datetime");
    case Resolve : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string")); return typeSet("DomainResource"); }
    case Extension : {checkParamTypes(exp.getFunction().toCode(), paramTypes, typeSet("string")); return typeSet("Extension"); }
		default:
			break;
		}
		throw new Error("not Implemented yet");
	}

	private Set<String> childTypes(Set<String> focus, String mask) throws PathEngineException, DefinitionException {
		Set<String> result = new HashSet<String>();
	  for (String f : focus) 
	    getChildTypesByName(f, mask, result);
		return result;
	}


  private void checkParamTypes(String funcName, List<Set<String>> paramTypes, Set<String>... typeSet) {
    int i = 0;
    for (Set<String> pt : typeSet) {
      if (i == paramTypes.size())
        return;
      Set<String> actual = paramTypes.get(i);
      i++;
      for (String a : actual) {
        if (!pt.contains(a))
          throw new Error("The parameter type '"+a+"' is not legal for "+funcName+" parameter "+Integer.toString(i)+". expecting "+pt.toString()); 
      }
    }
  }


  private Set<String> anything() {
		Set<String> result = new HashSet<String>();
		// todo....
		return result;
	}

//	private boolean isPrimitiveType(String s) {
//		return s.equals("boolean") || s.equals("integer") || s.equals("decimal") || s.equals("base64Binary") || s.equals("instant") || s.equals("string") || s.equals("uri") || s.equals("date") || s.equals("dateTime") || s.equals("time") || s.equals("code") || s.equals("oid") || s.equals("id") || s.equals("unsignedInt") || s.equals("positiveInt") || s.equals("markdown");
//	}

	private List<Base> evaluateFunction(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
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
    case Descendents : return funcDescendents(context, focus, exp);
    case MemberOf : return funcMemberOf(context, focus, exp);
    case Trace : return funcTrace(context, focus, exp);
    case Today : return funcToday(context, focus, exp);
    case Now : return funcNow(context, focus, exp);
		case Resolve: return funcResolve(context, focus, exp);
		case Extension: return funcExtension(context, focus, exp);
		default:
			throw new Error("not Implemented yet");
		}
	}

	private List<Base> funcAll(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
    List<Base> result = new ArrayList<Base>();
    List<Base> pc = new ArrayList<Base>();
    boolean all = true;
    for (Base item : focus) {
      pc.clear();
      pc.add(item);
      if (!convertToBoolean(execute(context, pc, exp.getParameters().get(0), false))) {
        all = false;
        break;
      }
    }
    result.add(new BooleanType(all));
    return result;
  }


  private List<Base> funcNow(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcToday(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcMemberOf(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcDescendents(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    for (Base b : focus)
      getChildrenByName(b, "**", result);
    return result;
  }


  private List<Base> funcChildren(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    for (Base b : focus)
      getChildrenByName(b, "*", result);
    return result;
  }


  private List<Base> funcReplace(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcReplaceMatches(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcEndsWith(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcToString(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcToDecimal(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcIif(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcTake(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcSkip(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcSingle(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcIs(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcAs(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcRepeat(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }



  private List<Base> funcIsDistinct(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcSupersetOf(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcSubsetOf(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    throw new Error("not Implemented yet");
  }


  private List<Base> funcExists(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
    List<Base> result = new ArrayList<Base>();
    result.add(new BooleanType(!focus.isEmpty()));
    return result;
  }


	private List<Base> funcResolve(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
		throw new Error("not Implemented yet");
	}

	private List<Base> funcExtension(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
		List<Base> result = new ArrayList<Base>();
		List<Base> nl = execute(context, focus, exp.getParameters().get(0), false);
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

	private List<Base> funcTrace(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
		List<Base> nl = execute(context, focus, exp.getParameters().get(0), false);
		String name = nl.get(0).primitiveValue();

		log(name, convertToString(focus));
		return focus;
	}

	private List<Base> funcDistinct(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
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

	private List<Base> funcMatches(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
		List<Base> result = new ArrayList<Base>();
		String p = convertToString(execute(context, focus, exp.getParameters().get(0), false));

		for (Base item : focus) {
			String s = convertToString(item);
			if (s.matches(p)) 
				result.add(item);
		}
		return result;
	}

	private List<Base> funcContains(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
		List<Base> result = new ArrayList<Base>();
		String sw = convertToString(execute(context, focus, exp.getParameters().get(0), false));

		for (Base item : focus) {
			String s = convertToString(item);
			if (s.contains(sw)) 
				result.add(item);
		}
		return result;
	}

	private List<Base> funcLength(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
		int l = 0;
		for (Base item : focus) {
			String s = convertToString(item);
			l = Math.max(l, s.length());
		}
		List<Base> result = new ArrayList<Base>();
		result.add(new IntegerType(l));
		return result;
	}

	private List<Base> funcStartsWith(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
		List<Base> result = new ArrayList<Base>();
		String sw = convertToString(execute(context, focus, exp.getParameters().get(0), false));

		for (Base item : focus) {
			String s = convertToString(item);
			if (s.startsWith(sw)) 
				result.add(item);
		}
		return result;
	}

	private List<Base> funcSubstring(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
		List<Base> result = new ArrayList<Base>();
		List<Base> n1 = execute(context, focus, exp.getParameters().get(0), false);
		int i1 = Integer.parseInt(n1.get(0).primitiveValue());
		int i2 = -1;
		if (exp.parameterCount() == 2) {
			List<Base> n2 = execute(context, focus, exp.getParameters().get(1), false);
			i2 = Integer.parseInt(n2.get(0).primitiveValue());
		}

		for (Base item : focus) {
			String sw = convertToString(item);
			String s;
			if (exp.parameterCount() == 2)
				s = sw.substring(i1, i2);
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


	private List<Base> funcWhere(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
		List<Base> result = new ArrayList<Base>();
		List<Base> pc = new ArrayList<Base>();
		for (Base item : focus) {
			pc.clear();
			pc.add(item);
			if (convertToBoolean(execute(context, pc, exp.getParameters().get(0), false)))
				result.add(item);
		}
		return result;
	}

  private List<Base> funcSelect(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
		List<Base> result = new ArrayList<Base>();
		List<Base> pc = new ArrayList<Base>();
		for (Base item : focus) {
			pc.clear();
			pc.add(item);
      result.addAll(execute(context, pc, exp.getParameters().get(0), false));
		}
		return result;
	}


	private List<Base> funcItem(ExecutionContext context, List<Base> focus, ExpressionNode exp) throws PathEngineException {
		List<Base> result = new ArrayList<Base>();
		String s = convertToString(execute(context, focus, exp.getParameters().get(0), false));
		if (Utilities.isInteger(s) && Integer.parseInt(s) < focus.size())
			result.add(focus.get(Integer.parseInt(s)));
		return result;
	}

	private List<Base> funcEmpty(ExecutionContext context, List<Base> focus, ExpressionNode exp) {
		List<Base> result = new ArrayList<Base>();
		result.add(new BooleanType(focus.isEmpty()));
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

	private void getChildTypesByName(String type, String name, Set<String> result) throws PathEngineException, DefinitionException {
		if (Utilities.noString(type))
			throw new PathEngineException("No type provided in BuildToolPathEvaluator.getChildTypesByName");
		if (type.equals("xhtml"))
			return;
		String url = null;
		if (type.contains(".")) {
			url = "http://hl7.org/fhir/StructureDefinition/"+type.substring(0, type.indexOf("."));
		} else {
			url = "http://hl7.org/fhir/StructureDefinition/"+type;
		}
		String tail = "";
		StructureDefinition sd = worker.fetchResource(StructureDefinition.class, url);
		if (sd == null)
			throw new DefinitionException("Unknown type "+type); // this really is an error, because we can only get to here if the internal infrastrucgture is wrong
		List<StructureDefinition> sdl = new ArrayList<StructureDefinition>();
		ElementDefinitionMatch m = null;
		if (type.contains("."))
			m = getElementDefinition(sd, type);
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
			if (type.contains("."))
				tail = type.substring(type.indexOf("."));
		}

		for (StructureDefinition sdi : sdl) {
			String path = sdi.getSnapshot().getElement().get(0).getPath()+tail+".";
			if (name.equals("**")) {
				for (ElementDefinition ed : sdi.getSnapshot().getElement()) {
					if (ed.getPath().startsWith(path))
						for (TypeRefComponent t : ed.getType()) {
							if (t.hasCode() && t.getCodeElement().hasValue()) {
								String tn = null;
								if (t.getCode().equals("Element") || t.getCode().equals("BackboneElement"))
									tn = ed.getPath();
								else
									tn = t.getCode();
								if (!result.contains(tn)) {
									result.add(tn);
									getChildTypesByName(tn, "**", result);
								}
							}
						}
				}      
			} else if (name.equals("*")) {
				for (ElementDefinition ed : sdi.getSnapshot().getElement()) {
					if (ed.getPath().startsWith(path) && !ed.getPath().substring(path.length()).contains("."))
						for (TypeRefComponent t : ed.getType()) {
							if (t.getCode().equals("Element") || t.getCode().equals("BackboneElement"))
								result.add(ed.getPath());
							else if (t.getCode().equals("Resource"))
								result.addAll(worker.getResourceNames());
							else
								result.add(t.getCode());
						}
				}
			} else {
				if (name.endsWith("*")) 
					path = sdi.getSnapshot().getElement().get(0).getPath()+tail+"."+name.substring(0, name.length()-1);
				else
					path = sdi.getSnapshot().getElement().get(0).getPath()+tail+"."+name;

				ElementDefinitionMatch ed = getElementDefinition(sdi, path);
				if (ed != null) {
					if (ed.getFixedType() != null)
						result.add(ed.getFixedType());
					else
						for (TypeRefComponent t : ed.getDefinition().getType()) {
							if (Utilities.noString(t.getCode()))
								throw new PathEngineException("Illegal reference to primative value attribute @ "+path);

							if (t.getCode().equals("Element") || t.getCode().equals("BackboneElement"))
								result.add(path);
							else if (t.getCode().equals("Resource"))
								result.addAll(worker.getResourceNames());
							else
								result.add(t.getCode());
						}
				}
			}
		}
	}

	private ElementDefinitionMatch getElementDefinition(StructureDefinition sd, String path) {
		for (ElementDefinition ed : sd.getSnapshot().getElement()) {
			if (ed.getPath().equals(path)) {
				if (ed.hasContentReference()) {
					return getElementDefinitionById(sd, ed.getContentReference());
				} else
					return new ElementDefinitionMatch(ed, null);
			}
			if (ed.getPath().endsWith("[x]") && path.startsWith(ed.getPath().substring(0, ed.getPath().length()-3)) && (path.length() == ed.getPath().length()-3 || hasType(ed, path.substring(ed.getPath().length()-3))))
				return new ElementDefinitionMatch(ed, path.substring(ed.getPath().length()-3));
			if (ed.hasContentReference() && path.startsWith(ed.getPath()+".")) {
				ElementDefinitionMatch m = getElementDefinitionById(sd, ed.getContentReference());
				return getElementDefinition(sd, m.definition.getPath()+path.substring(ed.getPath().length()));
			}
		}
		return null;
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

}