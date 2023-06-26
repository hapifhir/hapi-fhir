package ca.uhn.fhir.context;

import ca.uhn.fhir.model.api.BaseIdentifiableElement;
import ca.uhn.fhir.model.api.IElement;
import ca.uhn.fhir.model.api.IExtension;
import ca.uhn.fhir.model.api.annotation.*;
import ca.uhn.fhir.model.api.annotation.Extension;
import org.hl7.fhir.dstu3.model.*;

import java.util.List;

@ResourceDef(name = "ResourceWithExtensionsA", id="0001")
public class CustomDstu3ClassWithDstu2Base extends DomainResource {

	/*
	 * NB: several unit tests depend on the structure here
	 * so check the unit tests immediately after any changes 
	 */
	
	private static final long serialVersionUID = 1L;

	@Child(name = "foo1", type = StringType.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
	@Extension(url = "http://foo/#f1", definedLocally=true, isModifier=false)
	private List<StringType> myFoo1;

	@Child(name = "foo2", type = StringType.class, order = 1, min = 0, max = 1)
	@Extension(url = "http://foo/#f2", definedLocally=true, isModifier=true)
	private StringType myFoo2;

	@Child(name = "bar1", type = Bar1.class, order = 2, min = 1, max = Child.MAX_UNLIMITED)
	@Extension(url = "http://bar/#b1", definedLocally=true, isModifier=false)
	private List<Bar1> myBar1;
	
	@Child(name = "bar2", type = Bar1.class, order = 3, min = 1, max = Child.MAX_UNLIMITED)
	@Extension(url = "http://bar/#b2", definedLocally=true, isModifier=false)
	private Bar1 myBar2;
	
	@Child(name="baz", type = CodeableConcept.class, order = 4)
    @Extension(url= "http://baz/#baz", definedLocally=true, isModifier=false)
    @Description(shortDefinition = "Contains a codeable concept")
	private CodeableConcept myBaz;

	@Child(name = "identifier", type = Identifier.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
	private List<Identifier> myIdentifier;

	public List<Bar1> getBar1() {
		return myBar1;
	}

	public Bar1 getBar2() {
		return myBar2;
	}

	public List<StringType> getFoo1() {
		return myFoo1;
	}

	public StringType getFoo2() {
		return myFoo2;
	}

	public CodeableConcept getBaz() { return myBaz; }

	public List<Identifier> getIdentifier() {
		return myIdentifier;
	}

	public void setBar1(List<Bar1> theBar1) {
		myBar1 = theBar1;
	}

	public void setBar2(Bar1 theBar2) {
		myBar2 = theBar2;
	}

	public void setFoo1(List<StringType> theFoo1) {
		myFoo1 = theFoo1;
	}

	public void setFoo2(StringType theFoo2) {
		myFoo2 = theFoo2;
	}

	public void setBaz(CodeableConcept myBaz) { this.myBaz = myBaz; }

	public void setIdentifier(List<Identifier> theValue) {
		myIdentifier = theValue;
	}

	@Block(name = "Bar1")
	public static class Bar1 extends BaseIdentifiableElement implements IExtension {

		public Bar1() {
			super();
		}
		
		@Child(name = "bar11", type = DateType.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
		@Extension(url = "http://bar/#b1/1", definedLocally=true, isModifier=false)
		private List<DateType> myBar11;

		@Child(name = "bar12", type = DateType.class, order = 1, min = 0, max = Child.MAX_UNLIMITED)
		@Extension(url = "http://bar/#b1/2", definedLocally=true, isModifier=false)
		private List<Bar2> myBar12;

		private IdType myId;
		
		@Override
		public boolean isEmpty() {
			return false; // not implemented
		}

		@Override
		public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
			return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType ); // not implemented
		}


		public List<DateType> getBar11() {
			return myBar11;
		}

		public List<Bar2> getBar12() {
			return myBar12;
		}

		public void setBar11(List<DateType> theBar11) {
			myBar11 = theBar11;
		}

		public void setBar12(List<Bar2> theBar12) {
			myBar12 = theBar12;
		}
		
	

	}

	@Block(name = "Bar2")
	public static class Bar2 extends BaseIdentifiableElement implements IExtension {

		@Child(name = "bar121", type = DateType.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
		@Extension(url = "http://bar/#b1/2/1", definedLocally=true, isModifier=false)
		private List<DateType> myBar121;

		@Child(name = "bar122", type = DateType.class, order = 1, min = 0, max = Child.MAX_UNLIMITED)
		@Extension(url = "http://bar/#b1/2/2", definedLocally=true, isModifier=false)
		private List<DateType> myBar122;

		@Override
		public boolean isEmpty() {
			return false; // not implemented
		}

		@Override
		public <T extends IElement> List<T> getAllPopulatedChildElementsOfType(Class<T> theType) {
			return ca.uhn.fhir.util.ElementUtil.allPopulatedChildElements(theType ); // not implemented
		}


		public List<DateType> getBar121() {
			return myBar121;
		}

		public List<DateType> getBar122() {
			return myBar122;
		}

		public void setBar121(List<DateType> theBar121) {
			myBar121 = theBar121;
		}

		public void setBar122(List<DateType> theBar122) {
			myBar122 = theBar122;
		}



	}

	@Override
	public boolean isEmpty() {
		return false; // not implemented
	}

	
	
	@Override
	public String getId() {
		return null;
	}

	@Override
	public IdType getIdElement() {
		return null;
	}

	@Override
	public CodeType getLanguageElement() {
		return null;
	}

	@Override
	public Resource setId(String theId) {
		return null;
	}

	@Override
	public Meta getMeta() {
		return null;
	}

	@Override
	public Resource setIdElement(IdType theIdType) {
		return null;
	}

	@Override
	public String fhirType() {
		return null;
	}

	@Override
	protected void listChildren(List<Property> theResult) {
		// nothing
	}

	@Override
	public DomainResource copy() {
		return null;
	}

	@Override
	public ResourceType getResourceType() {
		return null;
	}


}
