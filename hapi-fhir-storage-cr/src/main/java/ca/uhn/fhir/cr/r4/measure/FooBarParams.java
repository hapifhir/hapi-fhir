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
	// LUKETODO:  use OperationEmbeddedParameter instead
	@OperationParam(name = "doFoo")
	// LUKETODO:  do I always need to make it a BooleanType and not a Boolean?
	private BooleanType myDoFoo;

	@OperationParam(name = "count")
	private IntegerType myCount;

	public FooBarParams(BooleanType myDoFoo, IntegerType myCount) {
		this.myDoFoo = myDoFoo;
		this.myCount = myCount;
	}

	public BooleanType getDoFoo() {
		return myDoFoo;
	}

	// LUKETODO:  do I always need to make it a IntegerType and not an Integer?
	public IntegerType getCount() {
		return myCount;
	}

	@Override
	public String toString() {
		return new StringJoiner(", ", FooBarParams.class.getSimpleName() + "[", "]")
				.add("myDoFoo=" + myDoFoo.getValue())
				.add("myCount=" + myCount.asStringValue())
				.toString();
	}
}