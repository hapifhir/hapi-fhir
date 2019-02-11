package ca.uhn.fhir.jpa.dao;

import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchFilterParser {

	public enum CompareOperation {
		eq,
		ne,
		co,
		sw,
		ew,
		gt,
		lt,
		ge,
		le,
		pr,
		po,
		ss,
		sb,
		in,
		re
	};

	public enum FilterLogicalOperation {
		and,
		or,
		not
	};

	public enum FilterItemType {
		parameter,
		logical,
		parameterGroup
	};

	public enum FilterValueType {
		token,
		string,
		numberOrDate
	};

	abstract public class Filter {

		public FilterItemType itemType;
		abstract public FilterItemType getFilterItemType();
	}

	public class FilterParameterPath {

		private String FName;

		private Filter FFilter;

		private FilterParameterPath FNext;

		public String getName() {

			return FName;
		}

		public void setName(String value) {

			FName = value;
		}

		public Filter getFilter() {

			return FFilter;
		}

		public void setFilter(Filter value) {

			FFilter = value;
		}

		public FilterParameterPath getNext() {

			return FNext;
		}

		public void setNext(FilterParameterPath value) {

			FNext = value;
		}

		@Override
		public String toString() {
			String result = null;
			if (getFilter() != null) {
				result = getName() + "[" + getFilter().toString() + "]";
			}
			else {
				result = getName();
			}
			if (getNext() != null) {
				result += "." + getNext().toString();
			}
			return result;
		}
	}

	public class FilterParameterGroup extends Filter {

		private Filter FContained;

		public void setContained(Filter value) {

			FContained = value;
		}

		public Filter getContained() {

			return FContained;
		}

		public FilterItemType getFilterItemType() {

			return itemType;
		}

		@Override
		public String toString() {

			return "(" + FContained.toString() + ")";
		}
	}

	public class FilterParameter extends Filter {

		private FilterParameterPath FParamPath;

		private CompareOperation FOperation;

		private String FValue;

		private FilterValueType FValueType;

		public FilterParameterPath getParamPath() {

			return FParamPath;
		}

		public void setParamPath(FilterParameterPath value) {

			FParamPath = value;
		}

		public FilterItemType getFilterItemType() {

			return itemType;
		}

		public CompareOperation getOperation() {

			return FOperation;
		}

		public void setOperation(CompareOperation value) {

			FOperation = value;
		}

		public String getValue() {

			return FValue;
		}

		public void setValue(String value) {

			FValue = value;
		}

		public FilterValueType getValueType() {

			return FValueType;
		}

		public void setValueType(FilterValueType FValueType) {

			this.FValueType = FValueType;
		}

		@Override
		public String toString() {
			if (FValueType == FilterValueType.string) {
				return getParamPath().toString() + " " + CODES_CompareOperation.get(getOperation().ordinal()) + " \"" + getValue() + "\"";
			}
			else {
				return getParamPath().toString() + " " + CODES_CompareOperation.get(getOperation().ordinal()) + " " + getValue();
			}
		}
	}

	public class FilterLogical extends Filter {

		private Filter FFilter1;

		private FilterLogicalOperation FOperation;

		private Filter FFilter2;

		public FilterItemType getFilterItemType() {

			return itemType;
		}

		public Filter getFilter1() {

			return FFilter1;
		}

		public void setFilter1(Filter FFilter1) {

			this.FFilter1 = FFilter1;
		}

		public FilterLogicalOperation getOperation() {

			return FOperation;
		}

		public void setOperation(FilterLogicalOperation FOperation) {

			this.FOperation = FOperation;
		}

		public Filter getFilter2() {

			return FFilter2;
		}

		public void setFilter2(Filter FFilter2) {

			this.FFilter2 = FFilter2;
		}

		@Override
		public String toString() {
			return FFilter1.toString() + " " + CODES_LogicalOperation.get(getOperation().ordinal()) + " " + FFilter2.toString();
		}
	}

	public enum FilterLexType {
		fsltEnded,
		fsltName,
		fsltString,
		fsltNumber,
		fsltDot,
		fsltOpen,
		fsltClose,
		fsltOpenSq,
		fsltCloseSq };

		private final List<String> CODES_CompareOperation = Arrays.asList("eq", "ne", "co", "sw", "ew", "gt", "lt", "ge", "le", "pr", "po", "ss", "sb", "in", "re");

		//		private final String[] CODES_CompareOperation = { "eq", "ne", "co", "sw", "ew", "gt", "lt", "ge", "le", "pr", "po", "ss", "sb", "in", "re" };
		private final List<String> CODES_LogicalOperation = Arrays.asList("and", "or", "not");

		//		private final String[] CODES_LogicalOperation = { "and", "or", "not" };
		private final String XML_DATE_PATTERN = "[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:([0-5][0-9]|60)(\\.[0-9]+)?(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?)?)?)?";

		private String original = null;

		private int cursor;

		private boolean isDate(String s) {

			Pattern p = Pattern.compile(XML_DATE_PATTERN);
			Matcher m = p.matcher(s);
			return m.matches();
		}

		private FilterLexType peek() {

			FilterLexType result = null;
			while ((cursor < original.length()) && (original.charAt(cursor) == ' ')) {
				cursor++;
			}

			if (cursor >= original.length()) {
				result = FilterLexType.fsltEnded;
			} else {
				if (((original.charAt(cursor) >= 'a') && (original.charAt(cursor) <= 'z')) ||
					((original.charAt(cursor) >= 'A') && (original.charAt(cursor) <= 'Z')) ||
					(original.charAt(cursor) == '_')) {
					result = FilterLexType.fsltName;
				} else if ((original.charAt(cursor) >= '0') && (original.charAt(cursor) <= '9')) {
					result = FilterLexType.fsltNumber;
				} else if (original.charAt(cursor) == '"') {
					result = FilterLexType.fsltString;
				} else if (original.charAt(cursor) == '.') {
					result = FilterLexType.fsltDot;
				} else if (original.charAt(cursor) == '(') {
					result = FilterLexType.fsltOpen;
				} else if (original.charAt(cursor) == ')') {
					result = FilterLexType.fsltClose;
				} else if (original.charAt(cursor) == '[') {
					result = FilterLexType.fsltOpenSq;
				} else if (original.charAt(cursor) == ']') {
					result = FilterLexType.fsltCloseSq;
				} else {
					throw new InternalErrorException(String.format("Unknown Character \"%s\" at %d",
						peekCh(),
						cursor));
				}
			}
			return result;
		}

		private String peekCh() {

			String result = null;
			if (cursor > original.length()) {
				result = "[end!]";
			} else {
				result = original.substring(cursor, cursor + 1);
			}
			return result;
		}

		private String consumeName() {

			String result = null;
			int i = cursor;
			while (true) {
				i++;
				if ((i > original.length() - 1) ||
					!(((original.charAt(i) >= 'a') && (original.charAt(i) <= 'z')) ||
						((original.charAt(i) >= 'A') && (original.charAt(i) <= 'Z')) ||
						((original.charAt(i) >= '0') && (original.charAt(i) <= '9')) ||
						(original.charAt(i) == '-') ||
						(original.charAt(i) == '_') ||
						(original.charAt(i) == ':'))) {
					break;
				}
			}

			result = original.substring(cursor,
				i/* - cursor*/);
			cursor = i;
			return result;
		}

		private String consumeToken() {

			String result = null;
			int i = cursor;
			while (true) {
				i++;
				if ((i > original.length() - 1) ||
					(original.charAt(i) <= 32) ||
					(StringUtils.isWhitespace(original.substring(i, i + 1))) ||
					(original.charAt(i) == ')') ||
					(original.charAt(i) == ']')) {
					break;
				}
			}
			result = original.substring(cursor,
				i/* - cursor*/);
			cursor = i;
			return result;
		}

		private String consumeNumberOrDate() {

			String result = null;
			int i = cursor;
			while (true) {
				i++;
				if ((i > original.length() - 1) ||
					!(((original.charAt(i) >= '0') && (original.charAt(i) <= '9')) ||
						(original.charAt(i) == '.') ||
						(original.charAt(i) == '-') ||
						(original.charAt(i) == ':') ||
						(original.charAt(i) == '+') ||
						(original.charAt(i) == 'T'))) {
					break;
				}
			}
			result = original.substring(cursor,
				i/* - cursor*/);
			cursor = i;
			return result;
		}

		private String consumeString() {

//			int l = 0;
			cursor++;
			StringBuilder str = new StringBuilder(original.length());
//			setLength(result, length(original)); // can't be longer than that
			while ((cursor <= original.length()) && (original.charAt(cursor) != '"')) {
//				l++;
				if ((cursor < original.length()) && (original.charAt(cursor) != '\\')) {
					str.append(original.charAt(cursor));
//					str.setCharAt(l, original.charAt(cursor));
				} else {
					cursor++;
					if (original.charAt(cursor) == '"') {
						str.append('"');
//						str.setCharAt(l, '"');
					} else if (original.charAt(cursor) == 't') {
						str.append('\t');
//						str.setCharAt(l, '\t');
					} else if (original.charAt(cursor) == 'r') {
						str.append('\r');
//						str.setCharAt(l, '\r');
					} else if (original.charAt(cursor) == 'n') {
						str.append('\n');
//						str.setCharAt(l, '\n');
					} else {
						throw new InternalErrorException(String.format("Unknown escape sequence at %d",
							cursor));
					}
				}
				cursor++;
			}
//			SetLength(result, l);
			if ((cursor > original.length()) || (original.charAt(cursor) != '"')) {
				throw new InternalErrorException(String.format("Problem with string termination at %d",
					cursor));
			}

			if (str.length() == 0) {
				throw new InternalErrorException(String.format("Problem with string at %d cannot be empty",
					cursor));
			}

			cursor++;
			return str.toString();
		}

		private Filter parse() {

			Filter result = parseOpen();
			if (cursor < original.length()) {
				throw new InternalErrorException(String.format("Expression did not terminate at %d",
					cursor));
			}
			return result;
		}

		private Filter parseOpen() {

			Filter result = null;
			String s;
			FilterParameterGroup grp = null;
			if (peek() == FilterLexType.fsltOpen) {
				cursor++;
				grp = new FilterParameterGroup();
				grp.setContained(parseOpen());
				if (peek() != FilterLexType.fsltClose) {
					throw new InternalErrorException(String.format("Expected ')' at %d but found %c",
						cursor,
						peekCh()));
				}
				cursor++;
				FilterLexType lexType = peek();
				if (lexType == FilterLexType.fsltName) {
					result = parseLogical(grp);
				} else if ((lexType == FilterLexType.fsltEnded) || (lexType == FilterLexType.fsltClose) || (lexType == FilterLexType.fsltCloseSq)) {
					result = (Filter) grp;
				} else {
					throw new InternalErrorException(String.format("Unexpected Character %c at %d",
						peekCh(),
						cursor));
				}
			} else {
				s = consumeName();
				if (s.compareToIgnoreCase("not") == 0) {
					result = parseLogical(null);
				} else {
					result = parseParameter(s);
				}
			}
			return result;
		}

		private Filter parseLogical(Filter filter) {

			Filter result = null;
			String s = null;
			FilterLogical logical = null;
			if (filter == null) {
				s = "not";
			} else {
				s = consumeName();
				if ((!s.equals("or")) && (!s.equals("and")) && (!s.equals("not"))) {
					throw new InternalErrorException(String.format("Unexpected Name %s at %d",
						s,
						cursor));
				}

				logical = new FilterLogical();
				logical.setFilter1((Filter) filter);
				if (s.compareToIgnoreCase("or") == 0) {
					logical.setOperation(FilterLogicalOperation.or);
				} else if (s.compareToIgnoreCase("not") == 0) {
					logical.setOperation(FilterLogicalOperation.not);
				} else {
					logical.setOperation(FilterLogicalOperation.and);
				}

				logical.setFilter2(parseOpen());
				result = (Filter) logical;
			}
			return result;
		}

		private FilterParameterPath parsePath(String name) {

			FilterParameterPath result = new FilterParameterPath();
			result.setName(name);
			if (peek() == FilterLexType.fsltOpenSq) {
				cursor++;
				result.setFilter(parseOpen());
				if (peek() != FilterLexType.fsltCloseSq) {
					throw new InternalErrorException(String.format("Expected ']' at %d but found %c",
						cursor,
						peekCh()));
				}
				cursor++;
			}

			if (peek() == FilterLexType.fsltDot) {
				cursor++;
				if (peek() != FilterLexType.fsltName) {
					throw new InternalErrorException(String.format("Unexpected Character %c at %d",
						peekCh(),
						cursor));
				}
				result.setNext(parsePath(consumeName()));
			} else if (result.getFilter() != null) {
				throw new InternalErrorException(String.format("Expected '.' at %d but found %c",
					cursor,
					peekCh()));
			}

			return result;
		}

		private Filter parseParameter(String name) {

			Filter result = null;
			String s = null;
			int i;
			FilterParameter filter = new FilterParameter();

			// 1. the path
			filter.setParamPath(parsePath(name));

			if (peek() != FilterLexType.fsltName) {
				throw new InternalErrorException(String.format("Unexpected Character %c at %d",
					peekCh(),
					cursor));
			}
			s = consumeName();
			int index = CODES_CompareOperation.indexOf(s);
			if (index == -1) {
				throw new InternalErrorException(String.format("Unknown operation %s at %d",
					s,
					cursor));
			}
			filter.setOperation(CompareOperation.values()[index]);

			FilterLexType lexType = peek();
			if (lexType == FilterLexType.fsltName) {
				filter.setValue(consumeToken());
				filter.setValueType(FilterValueType.token);
			} else if (lexType == FilterLexType.fsltNumber) {
				filter.setValue(consumeNumberOrDate());
				filter.setValueType(FilterValueType.numberOrDate);
			} else if (lexType == FilterLexType.fsltString) {
				filter.setValue(consumeString());
				filter.setValueType(FilterValueType.string);
			} else {
				throw new InternalErrorException(String.format("Unexpected Character %c at %d",
					peekCh(),
					cursor));
			}

			// check operation / value type results
			if (filter.getOperation() == CompareOperation.pr) {
				if ((filter.getValue().compareToIgnoreCase("true") != 0) &&
					(filter.getValue().compareToIgnoreCase("false") != 0)) {
					throw new InternalErrorException(String.format("Value %s not valid for operation %s at %d",
						filter.getValue(),
						CODES_CompareOperation.get(filter.getOperation().ordinal()),
						cursor));
				}
			} else if (filter.getOperation() == CompareOperation.po) {
				if (!isDate(filter.getValue())) {
					throw new InternalErrorException(String.format("Value %s not valid for operation %s at %d",
						filter.getValue(),
						CODES_CompareOperation.get(filter.getOperation().ordinal()),
						cursor));
				}
			}

			lexType = peek();
			if (lexType == FilterLexType.fsltName) {
				result = parseLogical(filter);
			} else if ((lexType == FilterLexType.fsltEnded) || (lexType == FilterLexType.fsltClose) || (lexType == FilterLexType.fsltCloseSq)) {
				result = (Filter) filter;
			} else {
				throw new InternalErrorException(String.format("Unexpected Character %c at %d",
					peekCh(),
					cursor));
			}
			return result;
		}

		static SearchFilterParser parser = null;
		static public Filter parse(String expression) {

			if (parser == null) {
				parser = new SearchFilterParser();
			}
			parser.original = expression;
			parser.cursor = 0;
			return parser.parse();
		}
}
