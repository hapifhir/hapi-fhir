package org.hl7.fhir.instance.model;

/**
 * This interface is a simple marker for anything which is an HL7
 * structure of some kind. It is provided mostly to simplify convergence
 * between the HL7.org structures and the HAPI ones. 
 */
public interface IBase {

	boolean isEmpty();

}
