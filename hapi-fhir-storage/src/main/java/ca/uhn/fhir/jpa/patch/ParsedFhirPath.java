package ca.uhn.fhir.jpa.patch;

import ca.uhn.fhir.jpa.util.RandomTextUtils;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;

import static net.sourceforge.plantuml.StringUtils.isNotEmpty;

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

		private int myListIndex = -1;

		public FhirPathNode(String theValue) {
			myValue = theValue;

			int open = myValue.indexOf("[");
			int close = myValue.indexOf("]");
			if (open > 0 && close > 0) {
				// this is a list element
				myListIndex = Integer.parseInt(
					myValue.substring(open + 1, close)
				);
			}
		}

		public String getValue() {
			return myValue;
		}

		public boolean isList() {
			return myListIndex > 0;
		}

		public int getListIndex() {
			return myListIndex;
		}

		/**
		 * If this node is actually a function node, this will return true.
		 */
		public boolean isFunction() {
			return false;
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
	}


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
	}

	/**
	 * The head node
	 */
	private final FhirPathNode myHead;

	/**
	 * The full path, unaltered
	 */
	private final String myRawPath;

	ParsedFhirPath(String theFullPath) {
		myRawPath = theFullPath;

		myHead = createNode(theFullPath);
	}

	public String getRawPath() {
		return myRawPath;
	}

	public FhirPathNode getHead() {
		return myHead;
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
	private static FhirPathNode createNode(String thePath) {
		int dotIndex = thePath.indexOf(".");
		int braceIndex = thePath.indexOf("(");

		FhirPathNode next;
		if (dotIndex == -1) {
			// we're at the end; no more elements

			if (braceIndex == -1) {
				// ending is just a path element, not a function
				next = new FhirPathNode(thePath);
			} else {
				// ending is a function
				String funcType = thePath.substring(0, braceIndex);
				// -1 because we don't care about the last bracket
				String containedExp = thePath.substring(braceIndex + 1, thePath.length() - 1);
				next = new FhirPathFunction(funcType);
				((FhirPathFunction)next).setContainedExpression(containedExp);

			}
		} else if (braceIndex  != -1 && braceIndex < dotIndex) {
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
			}
			String remaining = thePath.substring(remainingIndex + 1);

			FhirPathFunction func = new FhirPathFunction(funcType);
			if (isNotEmpty(containedExp)) {
				// function contains an expression; set it here
				func.setContainedExpression(containedExp);
			}
			next = func;

			if (isNotEmpty(remaining)) {
				next.setNext(createNode(remaining));
			}
		} else {
			// next element is a standard node element (not a function)
			String nextPart = thePath.substring(0, dotIndex);
			String remaining = thePath.substring(dotIndex + 1);

			next = new FhirPathNode(nextPart);
			next.setNext(createNode(remaining));
		}

		return next;
	}
}
