package ca.uhn.fhir.test.utilities.docker;


import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(DockerRequiredCondition.class)
@Target(ElementType.TYPE)
public @interface RequiresDocker {
}
