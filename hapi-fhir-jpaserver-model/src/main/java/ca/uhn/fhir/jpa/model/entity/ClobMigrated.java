package ca.uhn.fhir.jpa.model.entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is intended to be used on columns that use a clob type.
 * Clob columns (e.g. java fields annotated with {@link javax.persistence.Lob} with
 * a java field type of {@link String} or {@link java.sql.Clob}) are treated
 * badly by hibernate on Postgres. On PG, hibernate uses a column type of TEXT
 * for these columns, which is very dangerous since it means that the PG
 * utility VACUUMLO will delete values and leave the database in an inconsistent
 * state.
 *
 * So as a result, we are migrating any of these columns so that they
 * use {@literal byte[]} or {@link java.sql.Blob} instead.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value= ElementType.FIELD)
public @interface ClobMigrated {

	/**
	 * The software version in which the migration happened
	 */
	String migratedInVersion();

	/**
	 * The name of the field that this column was migrated to
	 */
	String migratedToColumn();
}
