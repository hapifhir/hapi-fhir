package ca.uhn.hapi.fhir.sql.hibernatesvc;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This is a marker for hibernate @{@link jakarta.persistence.Entity} classes.
 * This annotation should be placed on any hibernate entity fields which should
 * be used as a part of the entity ID (i.e. the compound Primary Key) when
 * operating in Database Partition Mode, and should be filtered from the PK
 * when not operating in that mode.
 * <p>
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
 * </p>
 * <p>
 * This annotation is processed by the {@link DatabasePartitionModeIdFilteringMappingContributor}
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PartitionedIdProperty {}
