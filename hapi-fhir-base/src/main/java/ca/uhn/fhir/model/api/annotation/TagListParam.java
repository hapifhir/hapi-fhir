package ca.uhn.fhir.model.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uhn.fhir.model.api.TagList;
import ca.uhn.fhir.rest.annotation.AddTags;
import ca.uhn.fhir.rest.annotation.DeleteTags;
import ca.uhn.fhir.rest.annotation.GetTags;

/**
 * Parameter annotation for the {@link TagList} parameter in a {@link GetTags},
 * {@link AddTags}, or {@link DeleteTags} method.
 * 
 * @see GetTags
 * @see AddTags
 * @see DeleteTags
 */
@Target(value = ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface TagListParam {
	// nothing
}
