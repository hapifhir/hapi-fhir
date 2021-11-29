package ca.uhn.fhir.checks;

import com.puppycrawl.tools.checkstyle.StatelessCheck;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

import java.util.HashSet;
import java.util.Set;

@StatelessCheck
public final class HapiErrorCodeCheck extends AbstractCheck {
	private final Set<Integer> ourCodesUsed = new HashSet<>();

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
				if (!ourCodesUsed.add(code)) {
					log(theAst.getLineNo(), "Two different exception messages call Msg.code(" +
						code +
						").  Each thrown exception throw call Msg.code() with a different code.");
				}
			} else {
				log(theAst.getLineNo(), "Called Msg.code() with a non-integer argument");
			}
		}
	}

	private DetailAST getMsgNodeOrNull(DetailAST theAst) {
		DetailAST child = theAst;
		for (; ; ) {
			if ("Msg".equals(child.getText())) {
				return child;
			}
			if (!child.hasChildren()) {
				return null;
			}
			child = child.getFirstChild();
		}
	}
}
