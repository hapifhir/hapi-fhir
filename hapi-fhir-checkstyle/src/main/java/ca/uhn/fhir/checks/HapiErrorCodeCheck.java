package ca.uhn.fhir.checks;

import com.puppycrawl.tools.checkstyle.StatelessCheck;
import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

@StatelessCheck
public final class HapiErrorCodeCheck extends AbstractCheck {

	/**
	 * A key is pointing to the warning message text in "messages.properties"
	 * file.
	 */
	public static final String MSG_KEY = "throws.count";

	/** Default value of max property. */
	private static final int DEFAULT_MAX = 4;

	/** Allow private methods to be ignored. */
	private boolean ignorePrivateMethods = true;

	/** Specify maximum allowed number of throws statements. */
	private int max;

	/** Creates new instance of the check. */
	public HapiErrorCodeCheck() {
		max = DEFAULT_MAX;
	}

	@Override
	public int[] getDefaultTokens() {
		return getRequiredTokens();
	}

	@Override
	public int[] getRequiredTokens() {
		return new int[] {
			TokenTypes.LITERAL_THROWS,
		};
	}

	@Override
	public int[] getAcceptableTokens() {
		return getRequiredTokens();
	}

	/**
	 * Setter to allow private methods to be ignored.
	 *
	 * @param ignorePrivateMethods whether private methods must be ignored.
	 */
	public void setIgnorePrivateMethods(boolean ignorePrivateMethods) {
		this.ignorePrivateMethods = ignorePrivateMethods;
	}

	/**
	 * Setter to specify maximum allowed number of throws statements.
	 *
	 * @param max maximum allowed throws statements.
	 */
	public void setMax(int max) {
		this.max = max;
	}

	@Override
	public void visitToken(DetailAST ast) {
		if (ast.getType() == TokenTypes.LITERAL_THROWS) {
			visitLiteralThrows(ast);
		}
		else {
			throw new IllegalStateException(ast.toString());
		}
	}

	/**
	 * Checks number of throws statements.
	 *
	 * @param ast throws for check.
	 */
	private void visitLiteralThrows(DetailAST ast) {
		if ((!ignorePrivateMethods || !isInPrivateMethod(ast))
			&& !isOverriding(ast)) {
			// Account for all the commas!
			final int count = (ast.getChildCount() + 1) / 2;
			if (count > max) {
				log(ast, MSG_KEY, count, max);
			}
		}
	}

	/**
	 * Check if a method has annotation @Override.
	 *
	 * @param ast throws, which is being checked.
	 * @return true, if a method has annotation @Override.
	 */
	private static boolean isOverriding(DetailAST ast) {
		final DetailAST modifiers = ast.getParent().findFirstToken(TokenTypes.MODIFIERS);
		boolean isOverriding = false;
		DetailAST child = modifiers.getFirstChild();
		while (child != null) {
			if (child.getType() == TokenTypes.ANNOTATION
				&& "Override".equals(getAnnotationName(child))) {
				isOverriding = true;
				break;
			}
			child = child.getNextSibling();
		}
		return isOverriding;
	}

	/**
	 * Gets name of an annotation.
	 *
	 * @param annotation to get name of.
	 * @return name of an annotation.
	 */
	private static String getAnnotationName(DetailAST annotation) {
		final DetailAST dotAst = annotation.findFirstToken(TokenTypes.DOT);
		final String name;
		if (dotAst == null) {
			name = annotation.findFirstToken(TokenTypes.IDENT).getText();
		}
		else {
			name = dotAst.findFirstToken(TokenTypes.IDENT).getText();
		}
		return name;
	}

	/**
	 * Checks if method, which throws an exception is private.
	 *
	 * @param ast throws, which is being checked.
	 * @return true, if method, which throws an exception is private.
	 */
	private static boolean isInPrivateMethod(DetailAST ast) {
		final DetailAST methodModifiers = ast.getParent().findFirstToken(TokenTypes.MODIFIERS);
		return methodModifiers.findFirstToken(TokenTypes.LITERAL_PRIVATE) != null;
	}

}
