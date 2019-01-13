package org.hl7.fhir.validation.r4.tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.hl7.fhir.utilities.TextFile;
import org.hl7.fhir.utilities.Utilities;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JsonSchemaTests {

  static private org.everit.json.schema.Schema sFhir;
  static private org.everit.json.schema.Schema sTest;
  
  public static final String  TEST_SCHEMA = "{\r\n"+
      "  \"$schema\": \"http://json-schema.org/draft-06/schema#\",\r\n"+
      "  \"id\": \"http://hl7.org/fhir/test-json-schema/3.4\",\r\n"+
      "  \"description\": \"for unit tests\",\r\n"+
      "  \"discriminator\": {\r\n"+
      "    \"propertyName\": \"resourceType\",\r\n"+
      "    \"mapping\": {\r\n"+
      "      \"A\": \"#/definitions/A\",\r\n"+
      "      \"B\": \"#/definitions/B\"\r\n"+
      "    }\r\n"+
      "  },\r\n"+
      "  \"oneOf\": [\r\n"+
      "    {\r\n"+
      "      \"$ref\": \"#/definitions/A\"\r\n"+
      "    },\r\n"+
      "    {\r\n"+
      "      \"$ref\": \"#/definitions/B\"\r\n"+
      "    }\r\n"+
      "  ],\r\n"+
      "  \"definitions\": {\r\n"+
      "    \"boolean\": {\r\n"+
      "      \"pattern\": \"^true|false$\",\r\n"+
      "      \"type\": \"boolean\"\r\n"+
      "    },\r\n"+
      "    \"id\": {\r\n"+
      "      \"pattern\": \"^[A-Za-z0-9\\\\-\\\\.]{1,64}$\",\r\n"+
      "      \"type\": \"string\"\r\n"+
      "    },\r\n"+
      "    \"integer\": {\r\n"+
      "      \"pattern\": \"^-?([0]|([1-9][0-9]*))$\",\r\n"+
      "      \"type\": \"number\"\r\n"+
      "    },\r\n"+
      "    \"string\": {\r\n"+
      "      \"pattern\": \"^[ \\\\r\\\\n\\\\t\\\\S]+$\",\r\n"+
      "      \"type\": \"string\"\r\n"+
      "    },\r\n"+
      "    \"Element\": {\r\n"+
      "      \"properties\": {\r\n"+
      "        \"id\": {\r\n"+
      "          \"$ref\": \"#/definitions/string\"\r\n"+
      "        }\r\n"+
      "      },\r\n"+
      "      \"additionalProperties\": false\r\n"+
      "    },\r\n"+
      "    \"Coding\": {\r\n"+
      "      \"properties\": {\r\n"+
      "        \"id\": {\r\n"+
      "          \"$ref\": \"#/definitions/string\"\r\n"+
      "        },\r\n"+
      "        \"system\": {\r\n"+
      "          \"$ref\": \"#/definitions/string\"\r\n"+
      "        },\r\n"+
      "        \"version\": {\r\n"+
      "          \"$ref\": \"#/definitions/string\"\r\n"+
      "        },\r\n"+
      "        \"code\": {\r\n"+
      "          \"$ref\": \"#/definitions/string\"\r\n"+
      "        },\r\n"+
      "        \"display\": {\r\n"+
      "          \"$ref\": \"#/definitions/string\"\r\n"+
      "        },\r\n"+
      "        \"userSelected\": {\r\n"+
      "          \"$ref\": \"#/definitions/boolean\"\r\n"+
      "        }\r\n"+
      "      },\r\n"+
      "      \"additionalProperties\": false\r\n"+
      "    },\r\n"+
      "    \"A\": {\r\n"+
      "      \"properties\": {\r\n"+
      "        \"resourceType\": {\r\n"+
      "          \"const\": \"A\"\r\n"+
      "        },\r\n"+
      "        \"id\": {\r\n"+
      "          \"$ref\": \"#/definitions/id\"\r\n"+
      "        }\r\n"+
      "      },\r\n"+
      "      \"required\": [\r\n"+
      "        \"resourceType\"\r\n"+
      "      ]\r\n"+
      "    },\r\n"+
      "    \"B\": {\r\n"+
      "      \"properties\": {\r\n"+
      "        \"resourceType\": {\r\n"+
      "          \"const\": \"B\"\r\n"+
      "        },\r\n"+
      "        \"code\": {\r\n"+
      "          \"$ref\": \"#/definitions/id\"\r\n"+
      "        },\r\n"+
      "        \"string\": {\r\n"+
      "          \"$ref\": \"#/definitions/string\"\r\n"+
      "        },\r\n"+
      "        \"integer\": {\r\n"+
      "          \"$ref\": \"#/definitions/integer\"\r\n"+
      "        },\r\n"+
      "        \"boolean\": {\r\n"+
      "          \"$ref\": \"#/definitions/boolean\"\r\n"+
      "        }\r\n"+
      "         \r\n"+
      "      },\r\n"+
      "      \"additionalProperties\": false,\r\n"+
      "      \"required\": [\r\n"+
      "        \"resourceType\", \"code\"\r\n"+
      "      ]\r\n"+
      "    }\r\n"+
      "  }\r\n"+
      "}\r\n";

  @Before
  public void setUp() throws Exception {
    if (sFhir == null) {
      String path = Utilities.path("r:\\fhir\\publish", "fhir.schema.json"); // todo... what should this be?
      String source = TextFile.fileToString(path);
      JSONObject rawSchema = new JSONObject(new JSONTokener(source));
      sFhir = SchemaLoader.load(rawSchema);
      rawSchema = new JSONObject(new JSONTokener(TEST_SCHEMA));
      sTest = SchemaLoader.load(rawSchema);
    }
  }

  private boolean validateJson(String source, Schema schema) throws FileNotFoundException, IOException {
    JSONObject jo = new JSONObject(source); // jo = (JsonObject) new com.google.gson.JsonParser().parse(source);
    try {
      schema.validate(jo);
      return true;
    } catch (ValidationException e) {
      System.out.println(e.getMessage());
      return false;
    }
//        e.getCausingExceptions().stream()
////            .map(ValidationException::getMessage)
////            .forEach(System.out::println);
  }


  private void pass(String source, Schema schema) throws FileNotFoundException, IOException {
    Assert.assertTrue(validateJson(source, schema));    
  }

  private void fail(String source, Schema schema) throws FileNotFoundException, IOException {
    Assert.assertFalse(validateJson(source, schema));    
  }

  @Test
  public void testTestSchemaPass() throws FileNotFoundException, IOException {
    pass("{ \"resourceType\" : \"A\" }", sTest);
  }

//  
//  @Test
//  public void testEmptyPatient() throws FileNotFoundException, IOException {
//    pass("{ \"resourceType\" : \"Patient\", \"id\" : \"1\" }", sFhir);
//  }
//
//  @Test
//  public void testNonResource() throws FileNotFoundException, IOException {
//    fail("{ \"resourceType\" : \"Patient1\", \"id\" : \"1\" }", sFhir);
//  }
//
//  @Test
//  public void testSimpleInvalid() throws FileNotFoundException, IOException {
//    fail("{ \"resourceType\" : \"Patient\", \"n--id\" : \"1\" }", sFhir);
//  }


}
