package ca.uhn.fhir.checks;

import com.puppycrawl.tools.checkstyle.FileStatefulCheck;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@FileStatefulCheck
public final class HapiErrorCodeCheck extends AbstractCheck {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiErrorCodeCheck.class);

	private static final ErrorCodeCache ourCache = ErrorCodeCache.INSTANCE;

	@Override
	public int[] getDefaultTokens() {
		return getRequiredTokens();
	}

	@Override
	public int[] getRequiredTokens() {
		return new int[] {
			TokenTypes.LITERAL_THROW,
		};
	}

	@Override
	public int[] getAcceptableTokens() {
		return getRequiredTokens();
	}

	@Override
	public void visitToken(DetailAST ast) {
		validateMessageCode(ast);
	}

	private void validateMessageCode(DetailAST theAst) {
		DetailAST instantiation = theAst.getFirstChild().getFirstChild();
		// We only expect message codes on new exception instantiations
		if (TokenTypes.LITERAL_NEW != instantiation.getType()) {
			return;
		}
		DetailAST exceptionNode = instantiation.getFirstChild();
		if (exceptionNode == null) {
			log(theAst.getLineNo(), "Exception thrown that does not call Msg.code()");
			return;
		}
		DetailAST thirdSiblingOfException = exceptionNode.getNextSibling().getNextSibling();
		if (thirdSiblingOfException == null) {
			log(theAst.getLineNo(), "Exception thrown that does not call Msg.code()");
			return;
		}
		DetailAST msgNode = getMsgNodeOrNull(thirdSiblingOfException);
		if (msgNode == null) {
			log(theAst.getLineNo(), "Exception thrown that does not call Msg.code()");
		} else {
			DetailAST numberNode =
					msgNode.getParent().getNextSibling().getFirstChild().getFirstChild();
			if (TokenTypes.NUM_INT == numberNode.getType()) {
				Integer code = Integer.valueOf(numberNode.getText());
				if (ourCache.containsKey(code)) {
					log(
							theAst.getLineNo(),
							"Two different exception messages call Msg.code(" + code
									+ ").  \nEach thrown exception must call Msg.code() with a different code. "
									+ "\nPreviously found at: "
									+ ourCache.get(code));
				} else {
					String location = getFilePath() + ":" + instantiation.getLineNo() + ":"
							+ instantiation.getColumnNo() + "(" + code + ")";
					// Ignore errors thrown in test for duplicates, as some fake implementations are throwing the same
					// codes for test purpsoes.
					if (!location.contains("/test/")) {
						ourCache.put(code, location);
					}
				}
			} else {
				log(theAst.getLineNo(), "Called Msg.code() with a non-integer argument");
			}
		}
	}

	private DetailAST getMsgNodeOrNull(DetailAST theNode) {

		if (TokenTypes.IDENT == theNode.getType() && "Msg".equals(theNode.getText())) {
			return theNode;
		}

		DetailAST retval = null;
		// depth first
		if (theNode.hasChildren()) {
			retval = getMsgNodeOrNull(theNode.getFirstChild());
			if (retval != null) {
				return retval;
			}
		}

		// then breadth
		DetailAST next = theNode.getNextSibling();
		while (next != null) {
			retval = getMsgNodeOrNull(next);
			if (retval != null) {
				return retval;
			}
			next = next.getNextSibling();
		}
		return null;
	}

	public enum ErrorCodeCache {
		INSTANCE;

		private static final Map<Integer, String> ourCodesUsed = new HashMap<>();

		ErrorCodeCache() {}

		public boolean containsKey(Integer s) {
			return ourCodesUsed.containsKey(s);
		}

		public String get(Integer i) {
			return ourCodesUsed.get(i);
		}

		public String put(Integer code, String location) {
			return ourCodesUsed.put(code, location);
		}

		public Set<Integer> keySet() {
			return ourCodesUsed.keySet();
		}
	}
}
