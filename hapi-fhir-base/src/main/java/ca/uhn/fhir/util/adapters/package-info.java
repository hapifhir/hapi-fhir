/**
 * Implements the Adapter pattern to allow external classes to extend/adapt existing classes.
 * Useful for extending interfaces that are closed to modification, or restricted for classpath reasons.
 * <p>
 *     For clients, the main entry point is {@link ca.uhn.fhir.util.adapters.AdapterUtils#adapt(java.lang.Object, java.lang.Class)}
 *     which will attempt to cast to the target type, or build an adapter of the target type.
 * </p>
 * <p>
 *     For implementors, you can support adaptation via two mechanisms:
 *     <ul>
 *         <li>by implementing {@link ca.uhn.fhir.util.adapters.IAdaptable} directly on a class to provide supported adapters,
 *         <li>or when the class is closed to direct modification, you can implement
 *         an instance of {@link ca.uhn.fhir.util.adapters.IAdapterFactory} and register
 *         it with the public {@link ca.uhn.fhir.util.adapters.AdapterManager#INSTANCE}.</li>
 *     </ul>
 *     The AdapterUtils.adapt() supports both of these.
 * </p>
 * Inspired by the Eclipse runtime.
 */
package ca.uhn.fhir.util.adapters;
