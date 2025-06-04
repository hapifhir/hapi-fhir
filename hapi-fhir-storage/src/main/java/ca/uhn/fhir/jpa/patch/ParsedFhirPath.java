package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.util.RandomTextUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static net.sourceforge.plantuml.StringUtils.isNotEmpty;

/**
 * This class creates and contains a parsed fhirpath.
 * -
 * It does not *validate* said fhir path (it might not be a valid path at all).
 * It is only used for parsing convenience.
 */
public class ParsedFhirPath {
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(ParsedFhirPath.class);

	public static class FhirPathNode {
		/**
		 * The node before this one;
		 * if this is a head node, previous will be null
		 */
		private FhirPathNode myPrevious;

		/**
		 * The node after this one;
		 * if this is a tail node, next will be null
		 */
		private FhirPathNode myNext;

		/**
		 * The value of this node.
		 */
		private final String myValue;

		/**
		 * If this node is a filter node (ie, ends with [x] where x is some
		 * list index), this value will be an integer >= 0
		 */
		private int myListIndex = -1;

		private boolean myIsValueNode = false;

		public FhirPathNode(String theValue) {
			myValue = theValue;

			int open = theValue.indexOf("[");
			if (open != -1) {
				int close = RandomTextUtils.findMatchingClosingBrace(open, myValue, '[', ']');
				if (close != -1) {
					String val = theValue.substring(open + 1, close);
					try {
						myListIndex = Integer.parseInt(val);
					} catch (NumberFormatException ex) {
						// TODO - not a number, but an expression
						/*
						 * Our current implementation does not account for
						 * expressions, but only indices.
						 * so we'll just note this exception for now
						 */
						ourLog.warn("{} is not an integer", val);
					}
				}
			}
		}

		public String getValue() {
			return myValue;
		}

		/**
		 * Returns true if this is a value node (ie, non-function, non-path node)
		 */
		public boolean isValueNode() {
//			return !isFunction() && !isFilter() && myValue.contains("'");
			return myIsValueNode;
		}

		public void setAsValueNode(boolean theBool) {
			myIsValueNode = theBool;
		}

		public boolean hasListIndex() {
			return myListIndex >= 0;
		}

		public int getListIndex() {
			return myListIndex;
		}

		/**
		 * If this node is actually a function node, this will return true.
		 */
		public boolean isFunction() {
			return isFilter();
		}

		/**
		 * Filters are any of the "end function" or index
		 * as defined by http://hl7.org/fhirpath/N1/#subsetting
		 */
		public boolean isFilter() {
			return myListIndex >= 0;
		}

		void setNext(FhirPathNode theNextNode) {
			myNext = theNextNode;
			theNextNode.setPrevious(this);
		}

		public FhirPathNode getNext() {
			return myNext;
		}

		void setPrevious(FhirPathNode thePreviousNode) {
			myPrevious = thePreviousNode;
		}

		public FhirPathNode getPrevious() {
			return myPrevious;
		}

		public boolean hasPrevious() {
			return myPrevious != null;
		}

		public boolean hasNext() {
			return myNext != null;
		}
	}

	/**
	 * A fhirpath node that is actually a function
	 * see http://hl7.org/fhirpath/N1/#literals
	 */
	public static class FhirPathFunction extends FhirPathNode {

		/**
		 * The contained expression is, itself, a fhir path
		 * It could be a fhir path of a single element, but it is
		 * a fhir path all its own
		 */
		private ParsedFhirPath myContainedExp;

		// http://hl7.org/fhirpath/N1/#literals
		public FhirPathFunction(String theValue) {
			super(theValue);
		}

		public void setContainedExpression(String theContainedExpression) {
			myContainedExp = ParsedFhirPath.parse(theContainedExpression);
		}

		/**
		 * Whether or not this node contains a sub-fhir-path
		 */
		public boolean hasContainedExp() {
			return myContainedExp != null;
		}

		public ParsedFhirPath getContainedExp() {
			return myContainedExp;
		}

		@Override
		public boolean isFunction() {
			return true;
		}

		@Override
		public boolean isFilter() {
			return super.isFilter() || isFilteredFunction();
		}

		private boolean isFilteredFunction() {
			switch (getValue()) {
				case "first", "last", "tail", "skip", "take", "intersect", "exclude", "single" -> {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * The head node. Should never be null.
	 */
	private final FhirPathNode myHead;

	/**
	 * The tail node. Should never be null.
	 */
	private FhirPathNode myTail;

	/**
	 * The full path, unparsed, of this particular FhirPath
	 */
	private final String myRawPath;

	/**
	 * Whether or not this fhirpath ends with a filter.
	 * See http://hl7.org/fhirpath/N1/#filtering-and-projection for
	 * filter definitions
	 */
	private boolean myEndsWithFilter;

	/**
	 * Whether or not this fhirpath ends with an
	 * index (ie, a node with [n] where n is some number)
	 */
	private boolean myEndsWithAnIndex;

	ParsedFhirPath(String theFullPath) {
		myRawPath = theFullPath;

		myHead = createNode(this, theFullPath);
	}

	public String getRawPath() {
		return myRawPath;
	}

	public FhirPathNode getHead() {
		return myHead;
	}

	public void setTail(FhirPathNode theTail) {
		myTail = theTail;
	}

	public FhirPathNode getTail() {
		return myTail;
	}

	public void setEndsWithFilter(boolean theEndsWithFilter) {
		myEndsWithFilter = theEndsWithFilter;
	}

	public boolean endsWithAFilter() {
		return myEndsWithFilter;
	}

	public void setEndsWithAnIndex(boolean theEndsWithAnIndex) {
		myEndsWithAnIndex = theEndsWithAnIndex;
	}

	public boolean endsWithAnIndex() {
		return myEndsWithAnIndex;
	}

	public boolean endsWithFilterOrIndex() {
		return endsWithAFilter() || endsWithAnIndex();
	}

	/**
	 * Returns the fhir path up to the last element.
	 * If there is a filter, it will return the element before the last
	 * (filtered) element.
	 */
	public String getContainingPath() {
		StringBuilder sb = new StringBuilder();
		FhirPathNode current = myHead;
//		while (current != null && current.getNext() != null && !current.getNext().isFunction()) {
		while (current != null && !current.isFunction()) {
			if (!sb.isEmpty()) {
				sb.append(".");
			}
			sb.append(current.getValue());

			current = current.getNext();
		}
		return sb.toString();
	}

	/**
	 * Returns the path as a string up until the condition
	 * (omitting the portion of the path that satisfies the condition).
	 * -
	 * If the condition is never met, the entire path will be returned.
	 *
	 * @param theCondition the condition to evaluate at each node to determine
	 *                     if it should be included in the path
	 */
	public String getPathUntilPreCondition(Predicate<FhirPathNode> theCondition) {
		FhirPathNode node = getHead();

		StringBuilder sb = new StringBuilder();
		while (node != null) {
			// check condition first
			if (theCondition.test(node)) {
				break;
			}

			if (!sb.isEmpty()) {
				sb.append(".");
			}
			sb.append(node.getValue());

			node = node.getNext();
		}
		return sb.toString();
	}

	/**
	 * Returns the last non-function, non-value node
	 */
	public FhirPathNode getFinalPathNode() {
		FhirPathNode node = myTail;
		while (node != null) {
			if (node.isFunction()) {
				if (node.hasListIndex()) {
					node = node.getPrevious();
				} else {
					FhirPathFunction fn = (FhirPathFunction) node;
					if (!fn.hasContainedExp()) {
						node = node.getPrevious();
					} else {
						// recurse
						FhirPathNode subNode = fn.getContainedExp().getFinalPathNode();
						if (subNode == null || subNode.isValueNode()) {
							node = node.getPrevious();
						} else {
							return subNode;
						}
					}
				}
			} else if (!node.isValueNode()) {
				return node;
			} else {
				node = node.getPrevious();
			}
		}

		return null;
	}

	/**
	 * Returns the final non-function node value in the expression.
	 * Eg:
	 * Patient.name.given -> returns "given"
	 * Patient.name.where(given.startsWith("John")) -> returns "given"
	 */
	public String getLastElementName() {
		FhirPathNode finalNode = getFinalPathNode();
		if (finalNode == null) {
			ourLog.error("No path nodes is fhirpath");
			return null;
		}
		return finalNode.getValue();
	}

	/**
	 * Retrieve a list of nodes that meet the given predicate.
	 * These nodes may or may not be siblings, but it will not
	 * include children of nodes (ie, only top-level nodes).
	 */
	public List<FhirPathNode> getNodes(Predicate<FhirPathNode> thePred) {
		List<FhirPathNode> nodes = new ArrayList<>();
		FhirPathNode node = getHead();

		while (node != null) {
			if (thePred.test(node)) {
				nodes.add(node);
			}
			node = node.getNext();
		}

		return nodes;
	}

	/**
	 * Create a ParsedFhirPath from a raw string.
	 */
	public static ParsedFhirPath parse(String theFullPath) {
		return new ParsedFhirPath(theFullPath);
	}

	/**
	 * Create a FhirPathNode, recursively constructing all
	 * it's neighbours and/or children (if it's a function).
	 */
	private static FhirPathNode createNode(ParsedFhirPath theParsedFhirPath, String thePath) {
		int dotIndex = thePath.indexOf(".");
		int braceIndex = thePath.indexOf("(");

		FhirPathNode next;
		if (dotIndex == -1) {
			int filterIndex = thePath.indexOf("[");
			FhirPathNode tail;
			boolean endsInFilter = false;
			// base cases
			if (braceIndex == -1 && filterIndex == -1) {
				// base case 1 - a standard node (no braces () or dots . or filters [])
				// ending is just a path element
				next = new FhirPathNode(thePath);
				if (!next.isFilter() && !next.isFunction() && isValueNode(thePath)) {
					next.setAsValueNode(true);
				}
				tail = next;
			} else if (filterIndex == -1) {
				// base case 2 - a filter function (function ending in ())
				// ending is a function
				String funcType = thePath.substring(0, braceIndex);
				// -1 because we don't care about the last bracket
				String containedExp = thePath.substring(braceIndex + 1, thePath.length() - 1);
				// the function has parameters -> a contained fhirpath
				next = new FhirPathFunction(funcType);
				if (isNotEmpty(containedExp)) {
					((FhirPathFunction) next).setContainedExpression(containedExp);
				}

				// TODO - this is not technically correct
				/*
				 * Our current implementations do not distinguish between
				 * "filter" and "function", so we'll treat all functions as
				 * filters
				 */
				endsInFilter = true;
				tail = next;
			} else {
				// base case 3 - path contains a filter ([]).. and potentially a functions
				// ie, either path[n] or path()[n]
				int closingFilter = RandomTextUtils.findMatchingClosingBrace(filterIndex, thePath, '[', ']');
				endsInFilter = true;

				// part1 -> part before the opening filter brace [
				String part1 = thePath.substring(0, filterIndex);

				// the filter value (a number)
				String part2 = thePath.substring(filterIndex, closingFilter + 1);

				// there are parts of a fhirpath past the [] filter
				String part3 = thePath.substring(closingFilter + 1);

				// create part2 first - the filter node
				ParsedFhirPath.FhirPathNode filterNode = new ParsedFhirPath.FhirPathNode(part2);

				// if there's a part1 - create the first node
				if (isNotEmpty(part1)) {
					// create node chain for first part;
					ParsedFhirPath.FhirPathNode p1node = createNode(theParsedFhirPath, part1);

					ParsedFhirPath.FhirPathNode node = p1node;
					while (node.getNext() != null) {
						node = node.getNext();
					}
					// set the filter node as the last in the chain
					node.setNext(filterNode);
					// and the first in the chain as the node to return
					next = p1node;
				} else {
					// otherwise, our next node is the filter node
					// (ie, there is no "first part")
					next = filterNode;
				}

				// part3 - the part after the closing filter brace ]
				if (isNotEmpty(part3)) {
					throw new InvalidRequestException(Msg.code(2713) + " Unexpected path after filter: " + thePath.substring(closingFilter + 1));
				} else {
					// the filter is the end node; nothing more to add
					tail = filterNode;
				}
				theParsedFhirPath.setEndsWithAnIndex(true);
			}
			theParsedFhirPath.setEndsWithFilter(endsInFilter);

			theParsedFhirPath.setTail(tail);
		} else if (braceIndex != -1 && braceIndex < dotIndex) {
			// recursive case 1
			// next part is a function
			// could contain an expression or singular element
			int closingIndex = RandomTextUtils.findMatchingClosingBrace(braceIndex, thePath);

			if (closingIndex == -1) {
				String msg = String.format("Path %s contains an unmatched brace at %d",
					thePath, braceIndex);
				ourLog.error(msg);

				throw new InternalErrorException(Msg.code(2714) + " " + msg);
			}

			String funcType = thePath.substring(0, braceIndex);
			String containedExp = thePath.substring(braceIndex + 1, closingIndex);

			// an additional +1 for closing index, since
			int remainingIndex = dotIndex;
			if (closingIndex > dotIndex) {
				// we want the first dot after the closing brace
				remainingIndex = thePath.indexOf(".", closingIndex);

				if (remainingIndex == -1) {
					// there is no . after the closing brace...
					remainingIndex = closingIndex;
				}
			}
			String remaining = thePath.substring(remainingIndex + 1);

			FhirPathFunction func = new FhirPathFunction(funcType);
			if (isNotEmpty(containedExp)) {
				// function contains an expression; set it here
				func.setContainedExpression(containedExp);
			}
			next = func;

			if (isNotEmpty(remaining)) {
				next.setNext(createNode(theParsedFhirPath, remaining));
			} else {
				theParsedFhirPath.setTail(next);
			}

			// not technically true; but our code treats anything with a function
			// as having a filter
			theParsedFhirPath.setEndsWithFilter(true);
		} else {
			// recursive case 2
			// next element is a standard node element (not a function)
			String nextPart = thePath.substring(0, dotIndex);
			String remaining = thePath.substring(dotIndex + 1);

			next = new FhirPathNode(nextPart);
			next.setNext(createNode(theParsedFhirPath, remaining));
		}

		return next;
	}

	private static boolean isValueNode(String theValue) {
		if (theValue.contains("'")) {
			return true;
		}
		try {
			Integer.parseInt(theValue);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
}
