package example;

import java.util.List;

import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.primitive.IdDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.client.api.IBasicClient;

//START SNIPPET: provider
/**
 * All RESTful clients must be an interface which extends IBasicClient
 */
public interface IRestfulClient extends IBasicClient {

	/**
	 * The "@Read" annotation indicates that this method supports the
	 * read operation. Read operations should return a single resource
	 * instance. 
	 * 
	 * @param theId
	 *    The read operation takes one parameter, which must be of type
	 *    IdDt and must be annotated with the "@Read.IdParam" annotation.
	 * @return 
	 *    Returns a resource matching this identifier, or null if none exists.
	 */
	@Read()
	public Patient getResourceById(@IdParam IdDt theId);

	/**
	 * The "@Search" annotation indicates that this method supports the 
	 * search operation. You may have many different method annotated with 
	 * this annotation, to support many different search criteria. This
	 * example searches by family name.
	 * 
	 * @param theIdentifier
	 *    This operation takes one parameter which is the search criteria. It is
	 *    annotated with the "@Required" annotation. This annotation takes one argument,
	 *    a string containing the name of the search criteria. The datatype here
	 *    is StringDt, but there are other possible parameter types depending on the
	 *    specific search criteria.
	 * @return
	 *    This method returns a list of Patients. This list may contain multiple
	 *    matching resources, or it may also be empty.
	 */
	@Search()
	public List<Patient> getPatient(@RequiredParam(name = Patient.SP_FAMILY) StringDt theFamilyName);

}
//END SNIPPET: provider


