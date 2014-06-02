package ca.uhn.fhir.jpa.test;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.IGenericClient;

public class Upload {

	public static void main(String[] args) {

		String msg = "{\n" + 
				"  \"resourceType\": \"Patient\",\n" + 
				"  \"text\": {\n" + 
				"    \"status\": \"generated\",\n" + 
				"    \"div\": \"<div>\\n      <table>\\n        <tbody>\\n          <tr>\\n            <td>Name</td>\\n            <td>Peter James <b>Chalmers</b> (&quot;Jim&quot;)</td>\\n          </tr>\\n          <tr>\\n            <td>Address</td>\\n            <td>534 Erewhon, Pleasantville, Vic, 3999</td>\\n          </tr>\\n          <tr>\\n            <td>Contacts</td>\\n            <td>Home: unknown. Work: (03) 5555 6473</td>\\n          </tr>\\n          <tr>\\n            <td>Id</td>\\n            <td>MRN: 12345 (Acme Healthcare)</td>\\n          </tr>\\n        </tbody>\\n      </table>\\n    </div>\"\n" + 
				"  },\n" + 
				"  \"identifier\": [\n" + 
				"    {\n" + 
				"      \"use\": \"usual\",\n" + 
				"      \"label\": \"MRN\",\n" + 
				"      \"system\": \"urn:oid:1.2.36.146.595.217.0.1\",\n" + 
				"      \"value\": \"12345\",\n" + 
				"      \"period\": {\n" + 
				"        \"start\": \"2001-05-06\"\n" + 
				"      },\n" + 
				"      \"assigner\": {\n" + 
				"        \"display\": \"Acme Healthcare\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"name\": [\n" + 
				"    {\n" + 
				"      \"use\": \"official\",\n" + 
				"      \"family\": [\n" + 
				"        \"Chalmers\"\n" + 
				"      ],\n" + 
				"      \"given\": [\n" + 
				"        \"Peter\",\n" + 
				"        \"James\"\n" + 
				"      ]\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"use\": \"usual\",\n" + 
				"      \"given\": [\n" + 
				"        \"Jim\"\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"telecom\": [\n" + 
				"    {\n" + 
				"      \"use\": \"home\"\n" + 
				"    },\n" + 
				"    {\n" + 
				"      \"system\": \"phone\",\n" + 
				"      \"value\": \"(03) 5555 6473\",\n" + 
				"      \"use\": \"work\"\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"gender\": {\n" + 
				"    \"coding\": [\n" + 
				"      {\n" + 
				"        \"system\": \"http://hl7.org/fhir/v3/AdministrativeGender\",\n" + 
				"        \"code\": \"M\",\n" + 
				"        \"display\": \"Male\"\n" + 
				"      }\n" + 
				"    ]\n" + 
				"  },\n" + 
				"  \"birthDate\": \"1974-12-25\",\n" + 
				"  \"deceasedBoolean\": false,\n" + 
				"  \"address\": [\n" + 
				"    {\n" + 
				"      \"use\": \"home\",\n" + 
				"      \"line\": [\n" + 
				"        \"534 Erewhon St\"\n" + 
				"      ],\n" + 
				"      \"city\": \"PleasantVille\",\n" + 
				"      \"state\": \"Vic\",\n" + 
				"      \"zip\": \"3999\"\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"contact\": [\n" + 
				"    {\n" + 
				"      \"relationship\": [\n" + 
				"        {\n" + 
				"          \"coding\": [\n" + 
				"            {\n" + 
				"              \"system\": \"http://hl7.org/fhir/patient-contact-relationship\",\n" + 
				"              \"code\": \"partner\"\n" + 
				"            }\n" + 
				"          ]\n" + 
				"        }\n" + 
				"      ],\n" + 
				"      \"name\": {\n" + 
				"        \"family\": [\n" + 
				"          \"du\",\n" + 
				"          \"Marché\"\n" + 
				"        ],\n" + 
				"        \"_family\": [\n" + 
				"          {\n" + 
				"            \"extension\": [\n" + 
				"              {\n" + 
				"                \"url\": \"http://hl7.org/fhir/Profile/iso-21090#qualifier\",\n" + 
				"                \"valueCode\": \"VV\"\n" + 
				"              }\n" + 
				"            ]\n" + 
				"          },\n" + 
				"          null\n" + 
				"        ],\n" + 
				"        \"given\": [\n" + 
				"          \"Bénédicte\"\n" + 
				"        ]\n" + 
				"      },\n" + 
				"      \"telecom\": [\n" + 
				"        {\n" + 
				"          \"system\": \"phone\",\n" + 
				"          \"value\": \"+33 (237) 998327\"\n" + 
				"        }\n" + 
				"      ]\n" + 
				"    }\n" + 
				"  ],\n" + 
				"  \"active\": true\n" + 
				"}";
		
			FhirContext ctx = new FhirContext();
			IGenericClient client = ctx.newRestfulGenericClient("http://localhost:8888/fhir/context");
			
			MethodOutcome outcome = client.create(ctx.newJsonParser().parseResource(msg));
			System.out.println(outcome.getId());
			
			
	}

}
