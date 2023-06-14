package ca.uhn.fhir.rest.server.interceptor.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor.Verdict;
import org.junit.jupiter.api.Test;

public class VerdictTest {

    @Test
    public void testToString() {
        Verdict v = new AuthorizationInterceptor.Verdict(PolicyEnum.ALLOW, new RuleImplOp("foo"));
        assertEquals("AuthorizationInterceptor.Verdict[rule=foo,decision=ALLOW]", v.toString());
    }
}
