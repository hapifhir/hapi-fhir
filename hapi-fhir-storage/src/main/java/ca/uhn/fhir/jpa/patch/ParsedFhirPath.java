package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.jpa.util.RandomTextUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
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
		private String myValue;

		private int myListIndex = -1;

		public FhirPathNode(String theValue) {
			myValue = theValue;

			int open = theValue.indexOf("[");
			if (open != -1) {
				int close = RandomTextUtils.findMatchingClosingBrace(open, myValue, '[', ']');
				if (close != -1) {
					String val = theValue.substring(open + 1, close);
					try {
						myListIndex = Integer.parseInt(val);
//						myValue = theValue.substring(0, open);
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

		public boolean hasListIndex() {
			return myListIndex > 0;
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
	 * The head node
	 */
	private final FhirPathNode myHead;

	private FhirPathNode myTail;

	/**
	 * The full path, unaltered
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
		while (current != null && current.getNext() != null && !current.getNext().isFunction()) {
			if (!sb.isEmpty()) {
				sb.append(".");
			}
			sb.append(current.getValue());

			current = current.getNext();
		}
		return sb.toString();
	}

	public List<String> getPathAsList() {
		List<String> parts = new ArrayList<>();
		FhirPathNode node = myHead;

		while (node != null) {
			parts.add(node.getValue());
			node = node.getNext();
		}
		return parts;
	}

	/**
	 * Returns the path as a string up until the condition
	 * (omitting the portion of the path that satisfies the condition).
	 *
	 * If the condition is never met, the entire path will be returned.
	 *
	 * @param theCondition the condition to evaluate at each node to determine
	 *                     if it should be included in the path
	 */
	public String getPathUntilPreCondition(Function<FhirPathNode, Boolean> theCondition) {
		FhirPathNode node = getHead();

		StringBuilder sb = new StringBuilder();
		while (node != null) {
			// check condition first
			if (theCondition.apply(node)) {
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

	public String getPathFromTo(Predicate<FhirPathNode> theFrom, Predicate<FhirPathNode> theTo) {
		FhirPathNode node = getHead();
		StringBuilder sb = new StringBuilder();
		boolean started = false;
		while (node != null) {
			if (theFrom.test(node)) {
				started = true;
			}
			if (started) {
				if (theTo.test(node)) {
					// reached the end
					break;
				}

				if (!sb.isEmpty()) {
					sb.append(".");
				}
				sb.append(node.getValue());
			}

			node = node.getNext();
		}
		return sb.toString();
	}

	/**
	 * Returns the final non-function node value
	 */
	public String getLastElementName() {
		FhirPathNode node = myTail;
		while (node != null && node.isFunction()) {
			node = node.getPrevious();
		}

		if (node == null) {
			// this shouldn't ever happen
			ourLog.error("No non-function nodes in path!");
			return null;
		}
		return node.getValue();
	}

	public FhirPathNode getFirstNode(Predicate<FhirPathNode> thePred) {
		FhirPathNode node = myHead;
		while (node != null && !thePred.test(node)) {
			node = node.getNext();
		}
		return node;
	}

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
			// base cases
			if (braceIndex == -1 && filterIndex == -1) {
				// base case 1 - a standard node (no braces () or dots . or filters [])
				// ending is just a path element
				next = new FhirPathNode(thePath);
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
				theParsedFhirPath.setEndsWithFilter(true);
			} else {
				// base case 3 - path contains a filter ([]).. and potentially a functions
				// ie, either path[n] or path()[n]
				int closingFilter = RandomTextUtils.findMatchingClosingBrace(filterIndex, thePath, '[', ']');

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
					// todo - error code
					throw new InvalidRequestException("Unexpected path after filter: " + thePath.substring(closingFilter + 1));
				} else {
					// the filter is the end node; nothing more to add
					theParsedFhirPath.setTail(filterNode);
				}
			}
			theParsedFhirPath.setEndsWithAnIndex(true);

			theParsedFhirPath.setTail(next);
		} else if (braceIndex != -1 && braceIndex < dotIndex) {
			// recursive case 1
			// next part is a function
			// could contain an expression or singular element
			int closingIndex = RandomTextUtils.findMatchingClosingBrace(braceIndex, thePath);

			if (closingIndex == -1) {
				String msg = String.format("Path %s contains an unmatched brace at %d",
					thePath, braceIndex);
				ourLog.error(msg);

				// todo - error code
				throw new InternalErrorException(msg);
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
			}

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
}
