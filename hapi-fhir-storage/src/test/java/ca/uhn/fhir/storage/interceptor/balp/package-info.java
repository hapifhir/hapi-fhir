/**
 * Issues for discussion:
 * <li>
 *    In the BasicAudit.PatientRead profile, if the object being read is something
 *    in the patient compartment but not the patient (e.g. Observation) is this correct:
 *    the entity:data is the Observation ID, but the entity:patient is the Patient ID
 * </li>
 * <li>
 *    In the BasicAudit.PatientRead profile, what if the resource being read is
 *    a List with multiple patients referenced?
 *    Implementation currently creates multiple auditevent resources
 * </li>
 * <li>
 *   In the BasicAudit.PatientQuery profile, what if the search matched resources
 *   belonging to different patients? E.g. The user may have requested
 *   Observation?patient=123 but an MDM might implicitly widen that to include
 *   Patient/456 (or the client even explicitly request this behavior through
 *   additional parameters). Currently adding multiple entity:patient repetitions
 *   in a single auditevent to handle this case.
 * </li>
 */
 package ca.uhn.fhir.storage.interceptor.balp;

