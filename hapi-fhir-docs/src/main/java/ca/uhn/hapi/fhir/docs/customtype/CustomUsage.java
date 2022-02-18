package ca.uhn.hapi.fhir.docs.customtype;

/*-
 * #%L
 * HAPI FHIR - Docs
 * %%
 * Copyright (C) 2014 - 2022 Smile CDR, Inc.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import ca.uhn.fhir.context.FhirContext;
import org.hl7.fhir.dstu3.model.DateTimeType;
import org.hl7.fhir.dstu3.model.DateType;
import org.hl7.fhir.dstu3.model.StringType;

import java.util.Date;

public class CustomUsage {

   public static void main(String[] args) {
      
      // START SNIPPET: usage
      // Create a context. Note that we declare the custom types we'll be using
      // on the context before actually using them
      FhirContext ctx = FhirContext.forDstu3();
      ctx.registerCustomType(CustomResource.class);
      ctx.registerCustomType(CustomDatatype.class);
      
      // Now let's create an instance of our custom resource type
      // and populate it with some data
      CustomResource res = new CustomResource();
      
      // Add some values, including our custom datatype
      DateType value0 = new DateType("2015-01-01");
      res.getTelevision().add(value0);
      
      CustomDatatype value1 = new CustomDatatype();
      value1.setDate(new DateTimeType(new Date()));
      value1.setKittens(new StringType("FOO"));
      res.getTelevision().add(value1);
      
      res.setDogs(new StringType("Some Dogs"));
      
      // Now let's serialize our instance
      String output = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(res);
      System.out.println(output);
      // END SNIPPET: usage
      
   }
   
}
