package ca.uhn.fhirtest.interceptor;

import ca.uhn.fhir.i18n.Msg;
import ca.uhn.fhir.jpa.provider.BaseJpaSystemProvider;
import ca.uhn.fhir.jpa.model.util.JpaConstants;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.ForbiddenOperationException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import ca.uhn.fhir.rest.server.provider.ProviderConstants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class PublicSecurityInterceptor extends AuthorizationInterceptor {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(PublicSecurityInterceptor.class);
	private HashSet<String> myTokens;

	public PublicSecurityInterceptor() {
		String passwordsString = System.getProperty("fhir.tdlpass");
		String[] passwords = passwordsString.split(",");
		myTokens = new HashSet<>(Arrays.asList(passwords));
		
		ourLog.info("We have {} valid security tokens", myTokens.size());
	}
	
	@Override
	public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
		String authHeader = theRequestDetails.getHeader("Authorization");

		if (isBlank(authHeader)) {
			return new RuleBuilder()
				.deny().operation().named(BaseJpaSystemProvider.MARK_ALL_RESOURCES_FOR_REINDEXING).onServer().andAllowAllResponses().andThen()
				.deny().operation().named(JpaConstants.OPERATION_UPLOAD_EXTERNAL_CODE_SYSTEM).onServer().andAllowAllResponses().andThen()
				.deny().operation().named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_ADD).atAnyLevel().andAllowAllResponses().andThen()
				.deny().operation().named(JpaConstants.OPERATION_APPLY_CODESYSTEM_DELTA_REMOVE).atAnyLevel().andAllowAllResponses().andThen()
				.deny().operation().named(ProviderConstants.OPERATION_EXPUNGE).onServer().andAllowAllResponses().andThen()
				.deny().operation().named(ProviderConstants.OPERATION_EXPUNGE).onAnyType().andAllowAllResponses().andThen()
				.deny().operation().named(ProviderConstants.OPERATION_EXPUNGE).onAnyInstance().andAllowAllResponses().andThen()
				.allowAll()
				.build();
		}

		if (!authHeader.startsWith("Bearer ")) {
			throw new ForbiddenOperationException(Msg.code(1978) + "Invalid bearer token, must be in the form \"Authorization: Bearer [token]\"");
		}
		
		String token = authHeader.substring("Bearer ".length()).trim();
		if (!myTokens.contains(token)) {
			ourLog.error("Invalid token '{}' - Valid are: {}", token, myTokens);
			throw new ForbiddenOperationException(Msg.code(1979) + "Unknown/expired bearer token");
		}
		
		ourLog.info("User logged in with bearer token: " + token.substring(0, 4) + "...");

		return new RuleBuilder()
				.allowAll()
				.build();
	}

}
