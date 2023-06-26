package ca.uhn.fhirtest.interceptor;

import ca.uhn.fhir.i18n.Msg;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.*;

import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.*;

public class TdlSecurityInterceptor extends AuthorizationInterceptor {

	private HashSet<String> myTokens;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(TdlSecurityInterceptor.class);
	
	public TdlSecurityInterceptor() {
		String passwordsString = System.getProperty("fhir.tdlpass");
		String[] passwords = passwordsString.split(",");
		myTokens = new HashSet<String>(Arrays.asList(passwords));
		
		ourLog.info("We have {} valid security tokens", myTokens.size());
	}
	
	@Override
	public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
		String authHeader = theRequestDetails.getHeader("Authorization");
		
		if (isBlank(authHeader)) {
			return new RuleBuilder()
					.allow().read().allResources().withAnyId().andThen()
					.allow().metadata().andThen()
					.denyAll("Anonymous write access denied on this server")
					.build();
		}
		
		if (!authHeader.startsWith("Bearer ")) {
			throw new ForbiddenOperationException(Msg.code(1980) + "Invalid bearer token, must be in the form \"Authorization: Bearer [token]\"");
		}
		
		String token = authHeader.substring("Bearer ".length()).trim();
		if (!myTokens.contains(token)) {
			ourLog.error("Invalid token '{}' - Valid are: {}", token, myTokens);
			throw new ForbiddenOperationException(Msg.code(1981) + "Unknown/expired bearer token");
		}
		
		ourLog.info("User logged in with bearer token: " + token.substring(0, 4) + "...");

		return new RuleBuilder()
				.allowAll()
				.build();
	}

}
