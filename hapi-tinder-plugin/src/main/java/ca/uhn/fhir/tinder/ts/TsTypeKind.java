package ca.uhn.fhir.tinder.ts;

// Created by Claude Opus 4.8

/**
 * Describes how a {@link TsProperty}'s type should be rendered and imported in TypeScript.
 */
public enum TsTypeKind {
	/**
	 * A native TypeScript type (string, number, boolean, any). Rendered verbatim, never imported and
	 * never prefixed with "I".
	 */
	PRIMITIVE,
	/**
	 * A reference to a generated FHIR interface (resource, datatype or backbone element). Rendered with
	 * an "I" prefix (e.g. {@code IHumanName}) and imported from a sibling {@code I*.ts} file.
	 */
	INTERFACE,
	/**
	 * A reference to a generated enumeration union alias (e.g. {@code AdministrativeGender}). Rendered
	 * verbatim and imported from a sibling {@code *.ts} file.
	 */
	ENUM
}
