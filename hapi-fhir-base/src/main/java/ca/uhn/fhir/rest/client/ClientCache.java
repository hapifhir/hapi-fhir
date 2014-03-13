package ca.uhn.fhir.rest.client;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.api.IDatatype;
import ca.uhn.fhir.model.api.annotation.Child;
import ca.uhn.fhir.model.api.annotation.ResourceDef;
import ca.uhn.fhir.model.primitive.Base64BinaryDt;
import ca.uhn.fhir.model.primitive.InstantDt;
import ca.uhn.fhir.model.primitive.StringDt;
import ca.uhn.fhir.rest.client.api.IRestfulClient;

public class ClientCache {

	private FhirContext myPrivateContext;
	private FhirContext myPublicContext;

	private ClientCache(FhirContext theContext) {
		myPublicContext = theContext;
		myPrivateContext = new FhirContext(CacheEntry.class);
	}
	
	/**
	 * Accepts a restful client instance and decorates it 
	 */
	public <T extends IRestfulClient> T decorateClient(Class<T> theInterface, T theClientToDecorate) {
		for (Method nextMethod : theInterface.getMethods()){
			
			
			
		}
		
		return theClientToDecorate;
		
	}
	
	
	@ResourceDef(name="HapiClientCacheEntry", id="hapiclientcacheentry") 
	private static class CacheEntry {
		
		@Child(order=2, min=0,max=Child.MAX_UNLIMITED,name="arguments")
		private List<IDatatype> myArguments;

		@Child(order=1, min=0,max=Child.MAX_UNLIMITED,name="argumentTypes")
		private List<StringDt> myArgumentTypes;

		@Child(order=3, min=1,max=1,name="loaded")
		private InstantDt myLoaded;

		@Child(order=0, min=1, max=1, name="methodName")
		private StringDt myMethodName;

		@Child(order=4, min=1,max=1,name="resourceText")
		private Base64BinaryDt myResourceText;

		public List<IDatatype> getArguments() {
			if (myArguments==null) {
				myArguments=new ArrayList<IDatatype>();
			}
			return myArguments;
		}

		public List<StringDt> getArgumentTypes() {
			return myArgumentTypes;
		}

		public InstantDt getLoaded() {
			return myLoaded;
		}

		public StringDt getMethodName() {
			return myMethodName;
		}

		public Base64BinaryDt getResourceText() {
			return myResourceText;
		}

		public void setArguments(List<IDatatype> theArguments) {
			myArguments = theArguments;
		}

		public void setArgumentTypes(List<StringDt> theArgumentTypes) {
			myArgumentTypes = theArgumentTypes;
		}

		public void setLoaded(InstantDt theLoaded) {
			myLoaded = theLoaded;
		}

		public void setMethodName(StringDt theMethodName) {
			myMethodName = theMethodName;
		}

		public void setResourceText(Base64BinaryDt theResourceText) {
			myResourceText = theResourceText;
		}
		
	}
	
}
