package ca.uhn.fhir.checks;

import com.puppycrawl.tools.checkstyle.StatelessCheck;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * mvn -P CI,ALLMODULES checkstyle:check
 */
@StatelessCheck
public final class HapiErrorCodeCheck extends AbstractCheck {
	private static final Logger ourLog = LoggerFactory.getLogger(HapiErrorCodeCheck.class);

	private static final Map<Integer, String> ourCodesUsed = new HashMap<>();

	@Override
	public int[] getDefaultTokens() {
		return getRequiredTokens();
	}

	@Override
	public int[] getRequiredTokens() {
		return new int[]{
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
		// TODO KHS this should be done in the checkstyle plugin pom config, not here
		if (getFileContents().getFileName().contains("/generated-sources/")) {
			return;
		}
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
			DetailAST numberNode = msgNode.getParent().getNextSibling().getFirstChild().getFirstChild();
			if (TokenTypes.NUM_INT == numberNode.getType()) {
				Integer code = Integer.valueOf(numberNode.getText());
				if (ourCodesUsed.containsKey(code)) {
					log(theAst.getLineNo(), "Two different exception messages call Msg.code(" +
						code +
						").  Each thrown exception throw call Msg.code() with a different code. " +
						"Previously found at: " + ourCodesUsed.get(code));
				} else {
					String location = getFileContents().getFileName() + ":" + instantiation.getLineNo() + ":" + instantiation.getColumnNo() + "(" + code + ")";
					ourCodesUsed.put(code, location);
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
}

