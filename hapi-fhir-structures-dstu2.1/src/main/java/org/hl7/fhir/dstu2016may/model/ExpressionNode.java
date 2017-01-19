package org.hl7.fhir.dstu2016may.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hl7.fhir.utilities.Utilities;

public class ExpressionNode {

	public enum Kind {
		Name, Function, Constant, Group
	}
	public static class SourceLocation {
		private int line;
		private int column;
		public  SourceLocation(int line, int column) {
			super();
			this.line = line;
			this.column = column;
		}
		public int getLine() {
			return line;
		}
		public int getColumn() {
			return column;
		}
		public void setLine(int line) {
			this.line = line;
		}
		public void setColumn(int column) {
			this.column = column;
		}

		public String toString() {
			return Integer.toString(line)+", "+Integer.toString(column);
		}
	}
  public enum Function {
    Custom, 
    
    Empty, Not, Exists, SubsetOf, SupersetOf, IsDistinct, Distinct, Count, Where, Select, All, Repeat, Item /*implicit from name[]*/, As, Is, Single,
    First, Last, Tail, Skip, Take, Iif, ToInteger, ToDecimal, ToString, Substring, StartsWith, EndsWith, Matches, ReplaceMatches, Contains, Replace, Length,  
    Children, Descendents, MemberOf, Trace, Today, Now, Resolve, Extension;

    public static Function fromCode(String name) {
      if (name.equals("empty")) return Function.Empty;
      if (name.equals("not")) return Function.Not;
      if (name.equals("exists")) return Function.Exists;
      if (name.equals("subsetOf")) return Function.SubsetOf;
      if (name.equals("supersetOf")) return Function.SupersetOf;
      if (name.equals("isDistinct")) return Function.IsDistinct;
      if (name.equals("distinct")) return Function.Distinct;
      if (name.equals("count")) return Function.Count;
      if (name.equals("where")) return Function.Where;
      if (name.equals("select")) return Function.Select;
      if (name.equals("all")) return Function.All;
      if (name.equals("repeat")) return Function.Repeat;
      if (name.equals("item")) return Function.Item;
      if (name.equals("as")) return Function.As;
      if (name.equals("is")) return Function.Is;
      if (name.equals("single")) return Function.Single;
      if (name.equals("first")) return Function.First;
      if (name.equals("last")) return Function.Last;
      if (name.equals("tail")) return Function.Tail;
      if (name.equals("skip")) return Function.Skip;
      if (name.equals("take")) return Function.Take;
      if (name.equals("iif")) return Function.Iif;
      if (name.equals("toInteger")) return Function.ToInteger;
      if (name.equals("toDecimal")) return Function.ToDecimal;
      if (name.equals("toString")) return Function.ToString;
      if (name.equals("substring")) return Function.Substring;
      if (name.equals("startsWith")) return Function.StartsWith;
      if (name.equals("endsWith")) return Function.EndsWith;
      if (name.equals("matches")) return Function.Matches;
      if (name.equals("replaceMatches")) return Function.ReplaceMatches;
      if (name.equals("contains")) return Function.Contains;
      if (name.equals("replace")) return Function.Replace;
      if (name.equals("length")) return Function.Length;
      if (name.equals("children")) return Function.Children;
      if (name.equals("descendents")) return Function.Descendents;
      if (name.equals("memberOf")) return Function.MemberOf;
      if (name.equals("trace")) return Function.Trace;
      if (name.equals("today")) return Function.Today;
      if (name.equals("now")) return Function.Now;
      if (name.equals("resolve")) return Function.Resolve;
      if (name.equals("extension")) return Function.Extension;
      return null;
    }
    public String toCode() {
      switch (this) {
      case Empty : return "empty";
      case Not : return "not";
      case Exists : return "exists";
      case SubsetOf : return "subsetOf";
      case SupersetOf : return "supersetOf";
      case IsDistinct : return "isDistinct";
      case Distinct : return "distinct";
      case Count : return "count";
      case Where : return "where";
      case Select : return "select";
      case All : return "all";
      case Repeat : return "repeat";
      case Item : return "item";
      case As : return "as";
      case Is : return "is";
      case Single : return "single";
      case First : return "first";
      case Last : return "last";
      case Tail : return "tail";
      case Skip : return "skip";
      case Take : return "take";
      case Iif : return "iif";
      case ToInteger : return "toInteger";
      case ToDecimal : return "toDecimal";
      case ToString : return "toString";
      case Substring : return "substring";
      case StartsWith : return "startsWith";
      case EndsWith : return "endsWith";
      case Matches : return "matches";
      case ReplaceMatches : return "replaceMatches";
      case Contains : return "contains";
      case Replace : return "replace";
      case Length : return "length";
      case Children : return "children";
      case Descendents : return "descendents";
      case MemberOf : return "memberOf";
      case Trace : return "trace";
      case Today : return "today";
      case Now : return "now";
      case Resolve : return "resolve";
      case Extension : return "extension";
      default: return "??";
      }
    }
  }

	public enum Operation {
		Equals, Equivalent, NotEquals, NotEquivalent, LessThen, Greater, LessOrEqual, GreaterOrEqual, Is, As, Union, Or, And, Xor, Implies, 
		Times, DivideBy, Plus, Minus, Concatenate, Div, Mod, In, Contains;

		public static Operation fromCode(String name) {
			if (Utilities.noString(name))
				return null;
			if (name.equals("="))
				return Operation.Equals;
			if (name.equals("~"))
				return Operation.Equivalent;
			if (name.equals("!="))
				return Operation.NotEquals;
			if (name.equals("!~"))
				return Operation.NotEquivalent;
			if (name.equals(">"))
				return Operation.Greater;
			if (name.equals("<"))
				return Operation.LessThen;
			if (name.equals(">="))
				return Operation.GreaterOrEqual;
			if (name.equals("<="))
				return Operation.LessOrEqual;
			if (name.equals("|"))
				return Operation.Union;
			if (name.equals("or"))
				return Operation.Or;
			if (name.equals("and"))
				return Operation.And;
			if (name.equals("xor"))
				return Operation.Xor;
      if (name.equals("is"))
        return Operation.Is;
      if (name.equals("as"))
        return Operation.As;
      if (name.equals("*"))
        return Operation.Times;
      if (name.equals("/"))
        return Operation.DivideBy;
			if (name.equals("+"))
				return Operation.Plus;
      if (name.equals("-"))
        return Operation.Minus;
      if (name.equals("&"))
        return Operation.Concatenate;
			if (name.equals("implies"))
				return Operation.Implies;
      if (name.equals("div"))
        return Operation.Div;
      if (name.equals("mod"))
        return Operation.Mod;
      if (name.equals("in"))
        return Operation.In;
      if (name.equals("contains"))
        return Operation.Contains;
			return null;

		}
		public String toCode() {
	    switch (this) {
			case Equals : return "=";
			case Equivalent : return "~";
			case NotEquals : return "!=";
			case NotEquivalent : return "!~";
			case Greater : return ">";
			case LessThen : return "<";
			case GreaterOrEqual : return ">=";
			case LessOrEqual : return "<=";
			case Union : return "|";
			case Or : return "or";
			case And : return "and";
			case Xor : return "xor";
      case Times : return "*";
      case DivideBy : return "/";
      case Plus : return "+";
      case Minus : return "-";
      case Concatenate : return "&";
			case Implies : return "implies";
      case Is : return "is";
      case As : return "as";
      case Div : return "div";
      case Mod : return "mod";
      case In : return "in";
      case Contains : return "contains";
			default: return "??";
			}
		}
	}

  public enum CollectionStatus {
    SINGLETON, ORDERED, UNORDERED
  }

  public static class TypeDetails {
    private Set<String> types = new HashSet<String>();
    private CollectionStatus collectionStatus;
    public TypeDetails(CollectionStatus collectionStatus, String... names) {
      super();
      this.collectionStatus = collectionStatus;
      for (String n : names)
        this.types.add(n);
    }
    public TypeDetails(CollectionStatus collectionStatus, Set<String> names) {
      super();
      this.collectionStatus = collectionStatus;
      for (String n : names)
        this.types.add(n);
    }
    public void addType(String n) {
      this.types.add(n);      
    }
    public void addTypes(Collection<String> n) {
      this.types.addAll(n);      
    }
    public boolean hasType(String... tn) {
      for (String t: tn)
      if (types.contains(t))
        return true;
      return false;
    }
    public void update(TypeDetails source) {
      types.addAll(source.types);
      if (collectionStatus == null)
        collectionStatus = source.collectionStatus;
      else if (source.collectionStatus == CollectionStatus.UNORDERED)
        collectionStatus = source.collectionStatus;
      else
        collectionStatus = CollectionStatus.ORDERED;
    }
    public TypeDetails union(TypeDetails right) {
      TypeDetails result = new TypeDetails(null);
      if (right.collectionStatus == CollectionStatus.UNORDERED || collectionStatus == CollectionStatus.UNORDERED)
        result.collectionStatus = CollectionStatus.UNORDERED;
      else 
        result.collectionStatus = CollectionStatus.ORDERED;
      result.types.addAll(types);
      result.types.addAll(right.types);
      return result;
    }
    
    public boolean hasNoTypes() {
      return types.isEmpty();
    }
    public Set<String> getTypes() {
      return types;
    }
    public TypeDetails toSingleton() {
      TypeDetails result = new TypeDetails(CollectionStatus.SINGLETON);
      result.types.addAll(types);
      return result;
    }
    public CollectionStatus getCollectionStatus() {
      return collectionStatus;
    }
    public boolean hasType(Set<String> tn) {
      for (String t: tn)
      if (types.contains(t))
        return true;
      return false;
    }
    public String describe() {
      return types.toString();
    }
    public String getType() {
      for (String t : types)
        return t;
      return null;
    }
    
  }


	//the expression will have one of either name or constant
	private String uniqueId;
	private Kind kind;
	private String name;
	private String constant;
	private Function function;
	private List<ExpressionNode> parameters; // will be created if there is a function
	private ExpressionNode inner;
	private ExpressionNode group;
	private Operation operation;
	private boolean proximal; // a proximal operation is the first in the sequence of operations. This is significant when evaluating the outcomes
	private ExpressionNode opNext;
	private SourceLocation start;
	private SourceLocation end;
	private SourceLocation opStart;
	private SourceLocation opEnd;
	private TypeDetails types;
	private TypeDetails opTypes;


	public ExpressionNode(int uniqueId) {
		super();
		this.uniqueId = Integer.toString(uniqueId);
	}

	public String toString() {
		StringBuilder b = new StringBuilder();
		switch (kind) {
		case Name:
			b.append(name);
			break;
		case Function:
			if (function == Function.Item) 
				b.append("[");
			else {
				b.append(name);
				b.append("(");
			}
			boolean first = true;
			for (ExpressionNode n : parameters) {
				if (first)
					first = false;
				else
					b.append(", ");
				b.append(n.toString());
			}
			if (function == Function.Item) 
				b.append("]");
			else {
				b.append(")");
			}
			break;
		case Constant:
  	  b.append(Utilities.escapeJava(constant));
			break;
		case Group:
			b.append("(");
			b.append(group.toString());
			b.append(")");
		}
		if (inner != null) {
			b.append(".");
			b.append(inner.toString());
		}
		if (operation != null) {
			b.append(" ");
			b.append(operation.toCode());
			b.append(" ");
			b.append(opNext.toString());
		}
			
		return b.toString();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getConstant() {
		return constant;
	}
	public void setConstant(String constant) {
		this.constant = constant;
	}
	public Function getFunction() {
		return function;
	}
	public void setFunction(Function function) {
		this.function = function;
		if (parameters == null)
			parameters = new ArrayList<ExpressionNode>();
	}

	public boolean isProximal() {
		return proximal;
	}
	public void setProximal(boolean proximal) {
		this.proximal = proximal;
	}
	public Operation getOperation() {
		return operation;
	}
	public void setOperation(Operation operation) {
		this.operation = operation;
	}
	public ExpressionNode getInner() {
		return inner;
	}
	public void setInner(ExpressionNode value) {
		this.inner = value;
	}
	public ExpressionNode getOpNext() {
		return opNext;
	}
	public void setOpNext(ExpressionNode value) {
		this.opNext = value;
	}
	public List<ExpressionNode> getParameters() {
		return parameters;
	}
	public boolean checkName() {
		if (!name.startsWith("$"))
			return true;
		else
			return name.equals("$this");  
	}

	public Kind getKind() {
		return kind;
	}

	public void setKind(Kind kind) {
		this.kind = kind;
	}

	public ExpressionNode getGroup() {
		return group;
	}

	public void setGroup(ExpressionNode group) {
		this.group = group;
	}

	public SourceLocation getStart() {
		return start;
	}

	public void setStart(SourceLocation start) {
		this.start = start;
	}

	public SourceLocation getEnd() {
		return end;
	}

	public void setEnd(SourceLocation end) {
		this.end = end;
	}

	public SourceLocation getOpStart() {
		return opStart;
	}

	public void setOpStart(SourceLocation opStart) {
		this.opStart = opStart;
	}

	public SourceLocation getOpEnd() {
		return opEnd;
	}

	public void setOpEnd(SourceLocation opEnd) {
		this.opEnd = opEnd;
	}

	public String getUniqueId() {
		return uniqueId;
	}


	public int parameterCount() {
		if (parameters == null)
			return 0;
		else
			return parameters.size();
	}

	public String Canonical() {
		StringBuilder b = new StringBuilder();
		write(b);
		return b.toString();
	}

	public String summary() {
		switch (kind) {
		case Name: return uniqueId+": "+name;
		case Function: return uniqueId+": "+function.toString()+"()";
		case Constant: return uniqueId+": "+constant;
		case Group: return uniqueId+": (Group)";
		}
		return "??";
	}

	private void write(StringBuilder b) {

		switch (kind) {
		case Name:
			b.append(name);
			break;
		case Constant:
			b.append(constant);
			break;
		case Function:
			b.append(function.toCode());
			b.append('(');
			boolean f = true;
			for (ExpressionNode n : parameters) {
				if (f)
					f = false;
				else
					b.append(", ");
				n.write(b);
			}
			b.append(')');

			break;
		case Group:
			b.append('(');
			group.write(b);
			b.append(')');
		}

		if (inner != null) {
			b.append('.');
			inner.write(b);
		}
		if (operation != null) {
			b.append(' ');
			b.append(operation.toCode());
			b.append(' ');
			opNext.write(b);
		}
	}

	public String check() {

		switch (kind) {
		case Name:
			if (Utilities.noString(name)) 
				return "No Name provided @ "+location();
			break;

		case Function: 		
			if (function == null)
				return "No Function id provided @ "+location();
			for (ExpressionNode n : parameters) { 
				String msg = n.check();
				if (msg != null)
					return msg;
			}

			break;

		case Constant:
			if (Utilities.noString(constant)) 
				return "No Constant provided @ "+location();
			break;

		case Group:
			if (group == null)
				return "No Group provided @ "+location();
			else {
				String msg = group.check();
				if (msg != null)
					return msg;
			}
		}
		if (inner != null) { 
			String msg = inner.check();
			if (msg != null)
				return msg;
		}
		if (operation == null) {

			if (opNext != null)
				return "Next provided when it shouldn't be @ "+location();
		} 
		else {
			if (opNext == null)
				return "No Next provided @ "+location();
			else
				opNext.check();
		}
		return null;

	}

	private String location() {
		return Integer.toString(start.line)+", "+Integer.toString(start.column);
	}

	public TypeDetails getTypes() {
		return types;
	}

	public void setTypes(TypeDetails types) {
		this.types = types;
	}

	public TypeDetails getOpTypes() {
		return opTypes;
	}

	public void setOpTypes(TypeDetails opTypes) {
		this.opTypes = opTypes;
	}
		
}
