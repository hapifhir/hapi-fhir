package ca.uhn.fhir.rest.server.security;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.hamcrest.core.StringContains;
import org.junit.Test;
import org.mitre.jose.keystore.JWKSetKeyStore;
import org.mitre.jwt.signer.service.impl.DefaultJwtSigningAndValidationService;
import org.mitre.jwt.signer.service.impl.JWKSetCacheService;
import org.mitre.oauth2.model.RegisteredClient;
import org.mitre.openid.connect.client.service.impl.StaticClientConfigurationService;
import org.mitre.openid.connect.client.service.impl.StaticServerConfigurationService;
import org.mitre.openid.connect.config.ServerConfiguration;
import org.springframework.core.io.ClassPathResource;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.composite.CodingDt;
import ca.uhn.fhir.model.dstu.resource.Observation;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.param.ReferenceParam;
import ca.uhn.fhir.rest.param.TokenOrListParam;
import ca.uhn.fhir.rest.server.Constants;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.AuthenticationException;

public class OpenIdConnectBearerTokenServerInterceptorTest {

	@Test
	public void testValidateToken() throws Exception {

		StaticServerConfigurationService srv = new StaticServerConfigurationService();
		srv.setServers(new HashMap<String, ServerConfiguration>());
		ServerConfiguration srvCfg = new ServerConfiguration();
		srvCfg.setJwksUri("AAAAAA");
		srvCfg.setIssuer("http://localhost:8888/uhn-openid-connect/");
		srv.getServers().put("http://localhost:8888/uhn-openid-connect/", srvCfg);
		srv.afterPropertiesSet();

		StaticClientConfigurationService cli = new StaticClientConfigurationService();
		cli.setClients(new HashMap<String, RegisteredClient>());
		cli.getClients().put("http://localhost:8888/uhn-openid-connect/", new RegisteredClient());

		OpenIdConnectBearerTokenServerInterceptor mgr = new OpenIdConnectBearerTokenServerInterceptor();
		mgr.setClientConfigurationService(cli);
		mgr.setServerConfigurationService(srv);

		HttpServletRequest req = mock(HttpServletRequest.class);
		when(req.getHeader(Constants.HEADER_AUTHORIZATION))
				.thenReturn(
						"Bearer "
								+ "eyJhbGciOiJSUzI1NiJ9.eyJleHAiOjE0MDY4NDE4NTgsImF1ZCI6WyJjbGllbnQiXSwiaXNzIjoiaHR0cDpcL1wvbG9jYWxob3N0Ojg4ODhcL3Vobi1vcGVuaWQtY29ubmVjdFwvIiwianRpIjoiOTNiMzRjOTUtNTNiMC00YzZmLTkwYjEtYWVjODRjZTc3OGFhIiwiaWF0IjoxNDA2ODM4MjU4fQ.fYtwehPUulUYnDG_10bN6TNf7uw2FNUh_E40YagpITrVfXsV06pjU2YpNgy8nbSFmxY9IBH44UXTmMH9PLFiRn88WsPMSrUQbFCcvGIYwhqkRjGm_J1Y6oWIafUzCwZBCvk4Ne44p3DJRR6FSZRnnC850p55901DGQmNLe-rZJk3t0MHl6wySduqT3K1-Vbuq-7H6xLE10hKpLhSqBTghpQNKNjm48jm0sHcFa3ENWzyWPOmpNfzDKmJAYK2UnBtqNSJP6AJzVrJXqSu-uzasq0VOVcRU4n8b39vU1olbho1eKF0cfQlQwbrtvWipBJJSsRp_tmB9SV9BXhENxOFTw");

		JWKSetCacheService val = mock(JWKSetCacheService.class);

		JWKSetKeyStore keyStore = new JWKSetKeyStore();
		keyStore.setLocation(new ClassPathResource("/svr_keystore.jwks"));
		DefaultJwtSigningAndValidationService valSvc = new DefaultJwtSigningAndValidationService(keyStore);
		when(val.getValidator("AAAAAA")).thenReturn(valSvc);

		mgr.setValidationServices(val);

		try {
			mgr.authenticate(req);
			fail();
		} catch (AuthenticationException e) {
			assertThat(e.getMessage(), StringContains.containsString("expired"));
		}

		mgr.setTimeSkewAllowance(10 * 365 * 24 * 60 * 60);
		mgr.authenticate(req);

	}
	
	
}
