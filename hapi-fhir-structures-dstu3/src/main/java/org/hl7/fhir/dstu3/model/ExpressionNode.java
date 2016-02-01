package org.hl7.fhir.dstu3.model;

import java.util.ArrayList;
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
		Empty, Item, Where, All, Any, First, Last, Tail, Count, AsInteger, StartsWith, Length, Matches, Substring, Contains, Distinct, Not, Resolve, Extension, Log;


		public static Function fromCode(String name) {
			if (name.equals("empty"))
				return Function.Empty;
			if (name.equals("item"))
				return Function.Item;
			if (name.equals("where"))
				return Function.Where;
			if (name.equals("all"))
				return Function.All;
			if (name.equals("any"))
				return Function.Any;
			if (name.equals("first"))
				return Function.First;
			if (name.equals("last"))
				return Function.Last;
			if (name.equals("tail"))
				return Function.Tail;
			if (name.equals("count"))
				return Function.Count;
			if (name.equals("asInteger"))
				return Function.AsInteger;
			if (name.equals("startsWith"))
				return Function.StartsWith;
			if (name.equals("length"))
				return Function.Length;
			if (name.equals("matches"))
				return Function.Matches;
			if (name.equals("contains"))
				return Function.Contains;
			if (name.equals("substring"))
				return Function.Substring;
			if (name.equals("distinct"))
				return Function.Distinct;
			if (name.equals("not"))
				return Function.Not;
			if (name.equals("resolve"))
				return Function.Resolve;
			if (name.equals("extension"))
				return Function.Extension;
			if (name.equals("log"))
				return Function.Log;

			return null;
		}
		public String toCode() {
			switch (this) {
			case Empty : return "empty";
			case Item : return "item";
			case Where : return "where";
			case All : return "all";
			case Any : return "any";
			case First : return "first";
			case Last : return "last";
			case Tail : return "tail";
			case Count : return "count";
			case AsInteger : return "asInteger";
			case StartsWith : return "startsWith";
			case Length : return "length";
			case Matches : return "matches";
			case Contains : return "contains";
			case Substring : return "substring";
			case Distinct : return "distinct";
			case Not : return "not";
			case Resolve : return "resolve";
			case Extension : return "extension";
			case Log : return "log";
			default: return "??";
			}
		}
	}

	public enum Operation {
		Equals, Equivalent, NotEquals, NotEquivalent, LessThen, Greater, LessOrEqual, GreaterOrEqual, Union, In, Or, And, Xor, Implies, Plus, Minus, Concatenate;

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
			if (name.equals("in"))
				return Operation.In;
			if (name.equals("or"))
				return Operation.Or;
			if (name.equals("and"))
				return Operation.And;
			if (name.equals("xor"))
				return Operation.Xor;
			if (name.equals("+"))
				return Operation.Plus;
			if (name.equals("-"))
				return Operation.Minus;
			if (name.equals("&"))
				return Operation.Concatenate;
			if (name.equals("implies"))
				return Operation.Implies;
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
			case In : return "in";
			case Or : return "or";
			case And : return "and";
			case Xor : return "xor";
			case Plus : return "+";
			case Minus : return "-";
			case Concatenate : return "&";
			case Implies : return "implies";
			default: return "??";
			}
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
	private Set<String> types;
	private Set<String> opTypes;


	public ExpressionNode(int uniqueId) {
		super();
		this.uniqueId = Integer.toString(uniqueId);
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
	public boolean checkName(boolean mappingExtensions) {
		if (!name.startsWith("$"))
			return true;
		else if (mappingExtensions && name.equals("$value"))
			return true;
		else
			return name.equals("$context") || name.equals("$resource") || name.equals("$parent");  
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

	public Set<String> getTypes() {
		return types;
	}

	public void setTypes(Set<String> types) {
		this.types = types;
	}

	public Set<String> getOpTypes() {
		return opTypes;
	}

	public void setOpTypes(Set<String> opTypes) {
		this.opTypes = opTypes;
	}
	
	
}
