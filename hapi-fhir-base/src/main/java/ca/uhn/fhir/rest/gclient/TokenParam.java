package ca.uhn.fhir.rest.gclient;


/**
 * Token parameter type for use in fluent client interfaces
 */
public class TokenParam implements IParam {

	private String myParamName;

	@Override
	public String getParamName() {
		return myParamName;
	}

	public TokenParam(String theParamName) {
		myParamName = theParamName;
	}

	public IMatches exactly() {
		return new IMatches() {
			@Override
			public ICriterion systemAndCode(String theSystem, String theCode) {
				return new TokenCriterion(getParamName(), theSystem, theCode);
			}

			@Override
			public ICriterion systemAndIdentifier(String theSystem, String theCode) {
				return new TokenCriterion(getParamName(), theSystem, theCode);
			}

			@Override
			public ICriterion code(String theCode) {
				return new TokenCriterion(getParamName(), null, theCode);
			}

			@Override
			public ICriterion identifier(String theIdentifier) {
				return new TokenCriterion(getParamName(), null, theIdentifier);
			}
		};
	}

	public interface IMatches {
		/**
		 * Creates a search criterion that matches against the given code system and code
		 * 
		 * @param theSystem
		 *            The code system (should be a URI)
		 * @param theCode
		 *            The code
		 * @return A criterion
		 */
		ICriterion systemAndCode(String theSystem, String theCode);

		/**
		 * Creates a search criterion that matches against the given system and identifier
		 * 
		 * @param theSystem
		 *            The code system (should be a URI)
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion systemAndIdentifier(String theSystem, String theIdentifier);

		/**
		 * Creates a search criterion that matches against the given identifier, with no system specified
		 * 
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion identifier(String theIdentifier);

		/**
		 * Creates a search criterion that matches against the given code, with no code system specified
		 * 
		 * @param theIdentifier
		 *            The identifier
		 * @return A criterion
		 */
		ICriterion code(String theIdentifier);
	}

}
