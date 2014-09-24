package ca.uhn.fhir.rest.server.security;

/*
 * #%L
 * HAPI FHIR - Core Library
 * %%
 * Copyright (C) 2014 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mitre.jwt.signer.service.JwtSigningAndValidationService;
import org.mitre.jwt.signer.service.impl.JWKSetCacheService;
import org.mitre.jwt.signer.service.impl.SymmetricCacheService;
import org.mitre.oauth2.model.RegisteredClient;
import org.mitre.openid.connect.client.service.ClientConfigurationService;
import org.mitre.openid.connect.client.service.ServerConfigurationService;
import org.mitre.openid.connect.config.ServerConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import ca.uhn.fhir.rest.method.OtherOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.ReadOnlyJWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public class OpenIdConnectBearerTokenServerInterceptor extends InterceptorAdapter {

	private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(OpenIdConnectBearerTokenServerInterceptor.class);

	@Autowired
	private ClientConfigurationService myClientConfigurationService;

	@Autowired
	private ServerConfigurationService myServerConfigurationService;

	private int myTimeSkewAllowance = 300;

	private SymmetricCacheService mySymmetricCacheService;
	private JWKSetCacheService myValidationServices;

	public OpenIdConnectBearerTokenServerInterceptor() {
		mySymmetricCacheService = new SymmetricCacheService();
		myValidationServices = new JWKSetCacheService();
	}
	
	
	@Override
	public boolean incomingRequestPostProcessed(RequestDetails theRequestDetails, HttpServletRequest theRequest, HttpServletResponse theResponse) throws AuthenticationException {
		if (theRequestDetails.getOtherOperationType() == OtherOperationTypeEnum.METADATA) {
			return true;
		}
		
		authenticate(theRequest);
		
		return true;
	}




	public void authenticate(HttpServletRequest theRequest) throws AuthenticationException {
		String token = theRequest.getHeader(Constants.HEADER_AUTHORIZATION);
		if (token == null) {
			throw new AuthenticationException("Not authorized (no authorization header found in request)");
		}
		if (!token.startsWith(Constants.HEADER_AUTHORIZATION_VALPREFIX_BEARER)) {
			throw new AuthenticationException("Not authorized (authorization header does not contain a bearer token)");
		}

		token = token.substring(Constants.HEADER_AUTHORIZATION_VALPREFIX_BEARER.length());

		SignedJWT idToken;
		try {
			idToken = SignedJWT.parse(token);
		} catch (ParseException e) {
			throw new AuthenticationException("Not authorized (bearer token could not be validated)", e);
		}

		// validate our ID Token over a number of tests
		ReadOnlyJWTClaimsSet idClaims;
		try {
			idClaims = idToken.getJWTClaimsSet();
		} catch (ParseException e) {
			throw new AuthenticationException("Not authorized (bearer token could not be validated)", e);
		}

		String issuer = idClaims.getIssuer();

		ServerConfiguration serverConfig = myServerConfigurationService.getServerConfiguration(issuer);
		if (serverConfig == null) {
			ourLog.error("No server configuration found for issuer: " + issuer);
			throw new AuthenticationException("Not authorized (no server configuration found for issuer " + issuer + ")");
		}

		RegisteredClient clientConfig = myClientConfigurationService.getClientConfiguration(serverConfig);
		if (clientConfig == null) {
			ourLog.error("No client configuration found for issuer: " + issuer);
			throw new AuthenticationException("Not authorized (no client configuration found for issuer " + issuer + ")");
		}

		// check the signature
		JwtSigningAndValidationService jwtValidator = null;

		JWSAlgorithm alg = idToken.getHeader().getAlgorithm();
		if (alg.equals(JWSAlgorithm.HS256) || alg.equals(JWSAlgorithm.HS384) || alg.equals(JWSAlgorithm.HS512)) {

			// generate one based on client secret
			jwtValidator = mySymmetricCacheService.getSymmetricValidtor(clientConfig.getClient());
		} else {
			// otherwise load from the server's public key
			jwtValidator = myValidationServices.getValidator(serverConfig.getJwksUri());
		}

		if (jwtValidator != null) {
			if (!jwtValidator.validateSignature(idToken)) {
				throw new AuthenticationException("Not authorized (signature validation failed)");
			}
		} else {
			ourLog.error("No validation service found. Skipping signature validation");
			throw new AuthenticationException("Not authorized (can't determine signature validator)");
		}

		// check expiration
		if (idClaims.getExpirationTime() == null) {
			throw new AuthenticationException("Id Token does not have required expiration claim");
		} else {
			// it's not null, see if it's expired
			Date minAllowableExpirationTime = new Date(System.currentTimeMillis() - (myTimeSkewAllowance * 1000L));
			Date expirationTime = idClaims.getExpirationTime();
			if (!expirationTime.after(minAllowableExpirationTime)) {
				throw new AuthenticationException("Id Token is expired: " + idClaims.getExpirationTime());
			}
		}

		// check not before
		if (idClaims.getNotBeforeTime() != null) {
			Date now = new Date(System.currentTimeMillis() + (myTimeSkewAllowance * 1000));
			if (now.before(idClaims.getNotBeforeTime())) {
				throw new AuthenticationException("Id Token not valid untill: " + idClaims.getNotBeforeTime());
			}
		}

		// check issued at
		if (idClaims.getIssueTime() == null) {
			throw new AuthenticationException("Id Token does not have required issued-at claim");
		} else {
			// since it's not null, see if it was issued in the future
			Date now = new Date(System.currentTimeMillis() + (myTimeSkewAllowance * 1000));
			if (now.before(idClaims.getIssueTime())) {
				throw new AuthenticationException("Id Token was issued in the future: " + idClaims.getIssueTime());
			}
		}

	}

	public int getTimeSkewAllowance() {
		return myTimeSkewAllowance;
	}

	public void setClientConfigurationService(ClientConfigurationService theClientConfigurationService) {
		myClientConfigurationService = theClientConfigurationService;
	}

	public void setServerConfigurationService(ServerConfigurationService theServerConfigurationService) {
		myServerConfigurationService = theServerConfigurationService;
	}

	public void setTimeSkewAllowance(int theTimeSkewAllowance) {
		myTimeSkewAllowance = theTimeSkewAllowance;
	}

	public void setValidationServices(JWKSetCacheService theValidationServices) {
		myValidationServices = theValidationServices;
	}

}
