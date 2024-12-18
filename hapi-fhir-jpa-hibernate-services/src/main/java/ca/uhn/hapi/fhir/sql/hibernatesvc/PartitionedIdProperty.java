package ca.uhn.hapi.fhir.sql.hibernatesvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This is a marker for hibernate @{@link jakarta.persistence.Entity} classes.
 * This annotation should be placed on a field, where that field is
 * a @{@link jakarta.persistence.Column} field that is either:
 * <ul>
 *     <li>
 *         Marked with @{@link jakarta.persistence.Id}
 *     </li>
 *     <li>
 *         Is a part of an @{@link jakarta.persistence.Embeddable} class
 *         intended to be used as an @{@link jakarta.persistence.EmbeddedId}
 *     </li>
 * </ul>
 * This annotation is processed by the {@link PartitionedIdMappingContributor}
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PartitionedIdProperty {}
