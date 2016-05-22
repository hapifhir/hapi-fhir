package example.customtype;

// START SNIPPET: resource
import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.dstu3.model.DomainResource;
import org.hl7.fhir.dstu3.model.ResourceType;
import org.hl7.fhir.dstu3.model.StringType;
import org.hl7.fhir.dstu3.model.Type;

import ca.uhn.fhir.context.FhirVersionEnum;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.util.ElementUtil;

/**
 * This is an example of a custom resource that also uses a custom
 * datatype.
 * 
 * Note that we are extensing DomainResource for an STU3
 * resource. For DSTU2 it would be BaseResource. 
 */
@ResourceDef(name = "CustomResource", profile = "http://hl7.org/fhir/profiles/custom-resource")
public class CustomResource extends DomainResource {

	private static final long serialVersionUID = 1L;

	/**
	 * We give the resource a field with name "television". This field has no
	 * specific type, so it's a choice[x] field for any type.
	 */
	@Child(name="television", min=1, max=Child.MAX_UNLIMITED, order=0)
	private List<Type> myTelevision;

	/**
	 * We'll give it one more field called "dogs"
	 */
   @Child(name = "dogs", min=0, max=1, order=1)
	private StringType myDogs;
	
	@Override
	public CustomResource copy() {
		CustomResource retVal = new CustomResource();
		super.copyValues(retVal);
		retVal.myTelevision = myTelevision;
      retVal.myDogs = myDogs;
		return retVal;
	}

   public List<Type> getTelevision() {
	   if (myTelevision == null) {
	      myTelevision = new ArrayList<Type>();
	   }
		return myTelevision;
	}

   public StringType getDogs() {
      return myDogs;
   }

	@Override
	public ResourceType getResourceType() {
		return null;
	}

	@Override
	public FhirVersionEnum getStructureFhirVersionEnum() {
		return FhirVersionEnum.DSTU3;
	}

	@Override
	public boolean isEmpty() {
		return ElementUtil.isEmpty(myTelevision, myDogs);
	}

	public void setTelevision(List<Type> theValue) {
		this.myTelevision = theValue;
	}

	public void setDogs(StringType theDogs) {
      myDogs = theDogs;
   }

}
// END SNIPPET: resource
