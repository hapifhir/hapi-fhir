package ca.uhn.fhir.context;

import java.util.List;

import ca.uhn.fhir.model.api.IExtension;
import ca.uhn.fhir.model.api.IResource;
import ca.uhn.fhir.model.api.annotation.Block;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.Extension;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.dstu.composite.IdentifierDt;
import ca.uhn.fhir.model.primitive.DateDt;
import ca.uhn.fhir.model.primitive.StringDt;

@ResourceDef(name = "ResourceWithExtensionsA")
public class ResourceWithExtensionsA implements IResource {

	@Child(name = "bar1", type = Bar1.class, order = 2, min = 1, max = Child.MAX_UNLIMITED)
	@Extension(url = "http://bar/1")
	private List<Bar1> myBar1;

	@Child(name = "bar2", type = Bar1.class, order = 3, min = 1, max = Child.MAX_UNLIMITED)
	@Extension(url = "http://bar/2")
	private Bar1 myBar2;

	@Child(name = "foo1", type = StringDt.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
	@Extension(url = "http://foo/1")
	private List<StringDt> myFoo1;

	@Child(name = "foo2", type = StringDt.class, order = 1, min = 0, max = 1)
	@Extension(url = "http://foo/2")
	private StringDt myFoo2;

	@Child(name = "identifier", type = IdentifierDt.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
	private List<IdentifierDt> myIdentifier;

	public List<Bar1> getBar1() {
		return myBar1;
	}

	public Bar1 getBar2() {
		return myBar2;
	}

	public List<StringDt> getFoo1() {
		return myFoo1;
	}

	public StringDt getFoo2() {
		return myFoo2;
	}

	public List<IdentifierDt> getIdentifier() {
		return myIdentifier;
	}

	public void setBar1(List<Bar1> theBar1) {
		myBar1 = theBar1;
	}

	public void setBar2(Bar1 theBar2) {
		myBar2 = theBar2;
	}

	public void setFoo1(List<StringDt> theFoo1) {
		myFoo1 = theFoo1;
	}

	public void setFoo2(StringDt theFoo2) {
		myFoo2 = theFoo2;
	}

	public void setIdentifier(List<IdentifierDt> theValue) {
		myIdentifier = theValue;
	}

	@Block(name = "Bar1")
	public static class Bar1 implements IExtension {

		public Bar1() {
			super();
		}
		
		@Child(name = "bar11", type = DateDt.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
		@Extension(url = "http://bar/1/1")
		private List<DateDt> myBar11;

		@Child(name = "bar12", type = DateDt.class, order = 1, min = 0, max = Child.MAX_UNLIMITED)
		@Extension(url = "http://bar/1/2")
		private List<Bar2> myBar12;

		@Override
		public boolean isEmpty() {
			return false; // TODO: implement
		}

		public List<DateDt> getBar11() {
			return myBar11;
		}

		public List<Bar2> getBar12() {
			return myBar12;
		}

		public void setBar11(List<DateDt> theBar11) {
			myBar11 = theBar11;
		}

		public void setBar12(List<Bar2> theBar12) {
			myBar12 = theBar12;
		}

	}

	@Block(name = "Bar2")
	public static class Bar2 implements IExtension {

		@Child(name = "bar121", type = DateDt.class, order = 0, min = 0, max = Child.MAX_UNLIMITED)
		@Extension(url = "http://bar/1/2/1")
		private List<DateDt> myBar121;

		@Child(name = "bar122", type = DateDt.class, order = 1, min = 0, max = Child.MAX_UNLIMITED)
		@Extension(url = "http://bar/1/2/2")
		private List<DateDt> myBar122;

		@Override
		public boolean isEmpty() {
			return false; // TODO: implement
		}

		public List<DateDt> getBar121() {
			return myBar121;
		}

		public List<DateDt> getBar122() {
			return myBar122;
		}

		public void setBar121(List<DateDt> theBar121) {
			myBar121 = theBar121;
		}

		public void setBar122(List<DateDt> theBar122) {
			myBar122 = theBar122;
		}

	}

	@Override
	public boolean isEmpty() {
		return false; // TODO: implement
	}

}