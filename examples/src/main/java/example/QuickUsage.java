package example;

import java.io.IOException;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.valueset.AdministrativeGenderEnum;
import ca.uhn.fhir.model.dstu2.valueset.IdentifierUseEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.RequiredParam;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IRestfulClient;

public class QuickUsage {

@SuppressWarnings("unused")
public static void main(String[] args) throws DataFormatException, IOException {

Patient patient = new Patient();
patient.addIdentifier().setUse(IdentifierUseEnum.OFFICIAL).setSystem("urn:fake:mrns").setValue("7000135");
patient.addIdentifier().setUse(IdentifierUseEnum.SECONDARY).setSystem("urn:fake:otherids").setValue("3287486");

patient.addName().addFamily("Smith").addGiven("John").addGiven("Q").addSuffix("Junior");

patient.setGender(AdministrativeGenderEnum.MALE);


FhirContext ctx = FhirContext.forDstu2();
String xmlEncoded = ctx.newXmlParser().encodeResourceToString(patient);
String jsonEncoded = ctx.newJsonParser().encodeResourceToString(patient);

MyClientInterface client = ctx.newRestfulClient(MyClientInterface.class, "http://foo/fhir");
IdentifierDt searchParam = new IdentifierDt("urn:someidentifiers", "7000135");
List<Patient> clients = client.findPatientsByIdentifier(searchParam);
}
	
public interface MyClientInterface extends IRestfulClient
{
  /** A FHIR search */
  @Search
  public List<Patient> findPatientsByIdentifier(@RequiredParam(name="identifier") IdentifierDt theIdentifier);
	
  /** A FHIR create */
  @Create
  public MethodOutcome createPatient(@ResourceParam Patient thePatient);
	
}

}
