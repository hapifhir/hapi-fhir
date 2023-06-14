package ca.uhn.fhir.jpa.nickname;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class NicknameSvcTest {
    @Test
    public void testReadfile() throws IOException {
        NicknameSvc nicknameSvc = new NicknameSvc();
        assertEquals(1082, nicknameSvc.size());
    }
}
