package example;

import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Description;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu2.composite.ContactPointDt;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Patient;
import ca.uhn.fhir.model.dstu2.resource.Patient.Contact;
import example.Sliced.SlicingRuleEnum;

@ResourceDef(name="Patient", profile="http://acme.org/StructureDefinition/AwesomePatient")
public class AwesomePatient extends Patient {

   @Child(name="contact", order=11, min=0, max=Child.MAX_UNLIMITED, summary=false, modifier=false) 
   @Description(
      shortDefinition="",
      formalDefinition="A contact party (e.g. guardian, partner, friend) for the patient"
   )
   @Sliced(discriminator="relationship", rule=SlicingRuleEnum.OPEN, slices= {
         @Slice(fixedValue="{\"coding\":[{\"system\":\"foo\", \"value\":\"bar\"}]}")
      })
   private java.util.List<Contact> myContact;

   
   @Child(name="identifier", type=IdentifierDt.class, order=Child.REPLACE_PARENT, min=0, max=Child.MAX_UNLIMITED, summary=true, modifier=false)  
   @Description(
      shortDefinition="id",
      formalDefinition="An identifier for this patient"
   )
   @Sliced(discriminator="system", rule=SlicingRuleEnum.CLOSED, slices= {
      @Slice(fixedValue="http://acme.org/national_identifier"),
      @Slice(fixedValue="http://acme.org/local_identifier")
   })
   private java.util.List<IdentifierDt> myIdentifier;

   public java.util.List<Contact> getContact() {
      return myContact;
   }

   @Override
   public java.util.List<IdentifierDt> getIdentifier() {
      return myIdentifier;
   }
   
   
   
   public AwesomePatient setContact(java.util.List<Contact> theContact) {
      myContact = theContact;
      return this;
   }

   @Override
   public AwesomePatient setIdentifier(java.util.List<IdentifierDt> theIdentifier) {
      myIdentifier = theIdentifier;
      return this;
   }
      
}
