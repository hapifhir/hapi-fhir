package ca.uhn.fhir.util;

import static org.mockito.Mockito.doThrow;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class IoUtilTest {

    @Mock private AutoCloseable myCloseable;

    @Test
    public void testCloseNull() {
        // Should throw no exception
        IoUtil.closeQuietly(null);
    }

    @Test
    public void testCloseWithException() throws Exception {
        doThrow(new Exception()).when(myCloseable).close();
        // Should throw no exception
        IoUtil.closeQuietly(myCloseable);
    }
}
