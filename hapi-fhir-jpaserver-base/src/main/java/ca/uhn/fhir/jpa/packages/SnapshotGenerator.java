package ca.uhn.fhir.jpa.packages;

import org.hl7.fhir.instance.model.api.IBaseResource;

import java.util.Collection;

/**
 * For internal use only
 */
public interface SnapshotGenerator {
	/**
	 * Generates snapshots of StructureDefinitions that does not already have one
	 */
	Collection<IBaseResource> generateFrom(Collection<IBaseResource> structureDefinitions);
}
