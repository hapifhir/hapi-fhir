package ca.uhn.fhirtest.interceptor;

import static org.apache.commons.lang3.StringUtils.isBlank;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider;
import ca.uhn.fhir.jpa.provider.dstu3.TerminologyUploaderProviderDstu3;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;

public class PublicSecurityInterceptor extends AuthorizationInterceptor {

	private HashSet<String> myTokens;
	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PublicSecurityInterceptor.class);
	
	public PublicSecurityInterceptor() {
		String passwordsString = System.getProperty("fhir.tdlpass");
		String[] passwords = passwordsString.split(",");
		myTokens = new HashSet<String>(Arrays.asList(passwords));
		
		ourLog.info("We have {} valid security tokens", myTokens.size());
	}
	
	@Override
	public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
		String authHeader = theRequestDetails.getHeader("Authorization");

		//@formatter:off
		if (isBlank(authHeader)) {
			return new RuleBuilder()
				.deny().operation().named(BaseJpaSystemProvider.MARK_ALL_RESOURCES_FOR_REINDEXING).onServer().andThen()
				.deny().operation().named(TerminologyUploaderProviderDstu3.UPLOAD_EXTERNAL_CODE_SYSTEM).onServer().andThen()
				.allowAll()
				.build();
		}
		//@formatter:off
		
		if (!authHeader.startsWith("Bearer ")) {
			throw new ForbiddenOperationException("Invalid bearer token, must be in the form \"Authorization: Bearer [token]\"");
		}
		
		String token = authHeader.substring("Bearer ".length()).trim();
		if (!myTokens.contains(token)) {
			ourLog.error("Invalid token '{}' - Valid are: {}", token, myTokens);
			throw new ForbiddenOperationException("Unknown/expired bearer token");
		}
		
		ourLog.info("User logged in with bearer token: " + token.substring(0, 4) + "...");

		return new RuleBuilder()
				.allowAll()
				.build();
	}

}
