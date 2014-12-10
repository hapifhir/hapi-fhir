package org.hl7.fhir.instance.model;

/**
 * For now, this is a simple marker interface indicating that a class is a resource type. 
 * There are two concrete types of implementations of this interrface. The first are
 * HL7.org's Resource structures (e.g. 
 * <code>org.hl7.fhir.instance.model.Patient</code>) and
 * the second are HAPI's Resource structures, e.g. 
 * <code>ca.uhn.fhir.model.dstu.resource.Patient</code>)
 */
public interface IBaseResource extends IBase {
	// nothing here yet
}
