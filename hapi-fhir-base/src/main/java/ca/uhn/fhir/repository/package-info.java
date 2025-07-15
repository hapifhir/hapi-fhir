/**
 * This package provides an interface and implementations abstracting
 * access to a FHIR repository.
 * <ul>
 *     <li>The InMemoryFhirRepository is a simple in-memory implementation suitable for testing.
 *     <li>The GenericClientRepository uses GenericClient to access a REST repository over http/https.
 * </ul>
 *
 *
 * Use the {@link ca.uhn.fhir.repository.Repositories} class to create instances.
 * Implementations are in the hapi-fhir-repositories module.
 */
package ca.uhn.fhir.repository;
