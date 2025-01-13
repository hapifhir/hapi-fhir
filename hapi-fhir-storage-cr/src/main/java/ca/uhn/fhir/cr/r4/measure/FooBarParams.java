package ca.uhn.fhir.cr.r4.measure;

import ca.uhn.fhir.rest.annotation.OperationEmbeddedType;
import ca.uhn.fhir.rest.annotation.OperationParam;
import org.hl7.fhir.r4.model.BooleanType;
import org.hl7.fhir.r4.model.IntegerType;

import java.util.StringJoiner;

// LUKETODO:  new annotation
// LUKETODO:  look at embedded annotations in JPA and follow that pattern
/**
 *  {
 *      "resourceType": "OperationDefinition",
 *      "name": "fooBar",
 *      "url": "http://foo.bar",
 *      "parameters": [
 *         {
 *             "name": "doFoo",
 *             "type: "boolean",
 *             "use": "in"
 *         },
 *         {
 *         		"name": "count",
 *         	 	"type: "integer",
 *         	    "use": "in"
 *         },
 *         {
 *             "name": "return",
 *             "use": "out",
 *             "type": "OperationOutcome"
 *         }
 *      ]
 *  }
 **/
@OperationEmbeddedType
public class FooBarParams {
	@OperationParam(name = "doFoo")
	// LUKETODO:  do I always need to make it a BooleanType and not a Boolean?
	private BooleanType myDoFoo;

	@OperationParam(name = "count")
	private IntegerType myCount;

	public BooleanType getDoFoo() {
		return myDoFoo;
	}

	public void setDoFoo(BooleanType theDoFoo) {
		myDoFoo = theDoFoo;
	}

	// LUKETODO:  do I always need to make it a IntegerType and not an Integer?
	public IntegerType getCount() {
		return myCount;
	}

	public void setCount(IntegerType theCount) {
		myCount = theCount;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", FooBarParams.class.getSimpleName() + "[", "]")
				.add("myDoFoo=" + myDoFoo)
				.add("myCount=" + myCount)
				.toString();
	}
}
