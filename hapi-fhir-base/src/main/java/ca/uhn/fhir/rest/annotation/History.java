package ca.uhn.fhir.rest.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.dstu.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;

/**
 * RESTful method annotation to be used for the FHIR
 * <a href="http://hl7.org/implement/standards/fhir/http.html#history">history</a> method.
 * 
 * <p>
 * History returns a feed containing all versions (or a selected range of versions) of 
 * a resource or a specific set of resources.
 * </p>
 * <p>
 * The history command supports three usage patterns, as described in the
 * <a href="http://hl7.org/implement/standards/fhir/http.html#history">FHIR history</a> documentation:
 * <ul>
 * <li>
 *   A search for the history of all resources on a server. In this case, {@link #resourceType()} 
 *   should be set to {@link AllResources} (as is the default) and the method should not have an ID parameter.
 *   <ul><li>
 *   	To invoke this pattern: <code>GET [base]/_history{?[parameters]&_format=[mime-type]}</code>
 *   </li></ul>
 * </li>
 * <li>
 *   A search for the history of all instances of a specific resource type on a server. In this case, {@link #resourceType()} 
 *   should be set to the specific resource type (e.g. {@link Patient Patient.class} and the method should not have an ID parameter.
 *   <ul><li>
 *   	To invoke this pattern: <code>GET [base]/[type]/_history{?[parameters]&_format=[mime-type]}</code>
 *   </li></ul>
 * </li>
 * <li>
 *   A search for the history of a specific instances of a specific resource type on a server. In this case, {@link #resourceType()} 
 *   should be set to the specific resource type (e.g. {@link Patient Patient.class} and the method should 
 *   have one parameter of type {@link IdDt} annotated with the {@link IdParam} annotation. 
 *   <ul><li>
 *   	To invoke this pattern: <code>GET [base]/[type]/[id]/_history{?[parameters]&_format=[mime-type]}</code>
 *   </li></ul>
 * </li>
 * </ul>
 * </p>
 * 
 * @see Count
 * @see Since
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value=ElementType.METHOD)
public @interface History {
	
	/**
	 * The resource type that this method applies to. See the {@link History History annotation type documentation}
	 * for information on usage patterns.  
	 */
	Class<? extends IResource> type() default IResource.class;
	
}