package ca.uhn.fhir.rest.server.parameters;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface Optional {
    String name();
}
