package ca.uhn.fhir.ws;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)

public @interface ResourceName {
    String value();
}
