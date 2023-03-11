/**
 * Issues for discussion:
 * <li>
 *    In the BasicAudit.PatientRead profile, if the object being read is something
 *    in the patient compartment but not the patient (e.g. Observation) is this correct:
 *    the entity:data is the Observation ID, but the entity:patient is the Patient ID
 * </li>
 * <li>
 *    In the BasicAudit.PatientRead profile, what if the resource being read is
 *    a List with multiple patients referenced? Presumably this needs to be multiple
 *    auditevent instances created, right? Would it be better to allow the entity:patient
 *    slice to repeat?
 * </li>
 */
 package ca.uhn.fhir.jpa.interceptor.balp;

