package ca.uhn.fhir.rest.server.interceptor.auth;

import org.hl7.fhir.instance.model.api.IBaseResource;

import javax.annotation.Nonnull;

/**
 * Adapt our InMemoryMatcher to our auth rules.
 * fixme do we like this name, or this package?
 */
public interface IAuthorizationSearchParamMatcher {
	MatchResult match(String theCriteria, IBaseResource theResource);

	enum Match {
		MATCH,
		NO_MATCH,
		UNSUPPORTED
	}

	public static class MatchResult {
		private final Match myMatch;
		private final String myUnsupportedReason;

		public static MatchResult makeMatched() {
			return new MatchResult(Match.MATCH, null);
		}

		public static MatchResult makeUnmatched() {
			return new MatchResult(Match.NO_MATCH, null);
		}

		public static MatchResult makeUnsupported(@Nonnull String theReason) {
			return new MatchResult(Match.UNSUPPORTED, theReason);
		}

		private MatchResult(Match myMatch, String myUnsupportedReason) {
			this.myMatch = myMatch;
			this.myUnsupportedReason = myUnsupportedReason;
		}

		public Match getMatch() {
			return myMatch;
		}

		public String getUnsupportedReason() {
			return myUnsupportedReason;
		}
	}
}
