package ca.uhn.fhir.ws.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface Optional {
    String name();
}
