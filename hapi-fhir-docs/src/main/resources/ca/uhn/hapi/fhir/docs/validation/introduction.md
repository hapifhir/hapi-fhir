# Validation Introduction

This section contains details on several strategies for validating resources:

* **[Parser Error Handler](./parser_error_handler.html)** validation is validation at runtime during the parsing of a resource. It can be used to catch input data that is impossible to fit into the HAPI data model. For example, it can be used to throw exceptions or display error messages if a resource being parsed contains elements for which there are no appropriate fields in a HAPI data structure. This is useful in order to ensure that no data is being lost during parsing, but is less comprehensive than resource validation against raw text data.

  Parser Validation is extremely fast and lightweight since it happens within the parser and has no dependencies to outside resources.
  
  
* **[Profile Validation](./profile_validator.html) is validation of the raw or parsed resource against	the official FHIR validation rules (ie. the official FHIR definitions, expressed as profile resources such as [StructureDefinition](http://hl7.org/fhir/structuredefinition.html) and [ValueSet](http://hl7.org/fhir/valueset.html).

  Profile Validation can also be used to validate resources against individual Implementation Guides which derive from the core specification (e.g. the [US Core](http://hl7.com/uscore) implementation guide).
  
  

		<section name="Resource Validation Module: Schema/Schematron">

			<p>
				FHIR resource definitions are distributed with a set of XML schema files (XSD)
				as well as a set of XML Schematron (SCH) files. These two sets of files are
				complimentary to each other, meaning that in order to claim compliance to the
				FHIR specification, your resources must validate against both sets.
			</p>
			<p>
				The two sets of files are included with HAPI, and it uses them to perform
				validation.
			</p>

			<subsection name="Preparation">

				<p>
					In order to use HAPI's Schematron support, a libaray called
					<a href="https://github.com/phax/ph-schematron">Ph-Schematron</a>
					is used, so this library must be added to your classpath (or Maven POM file, Gradle
					file, etc.)
					Note that this library is specified as an optional dependency
					by HAPI FHIR
					so you need to explicitly include it if you want to use this
					functionality.
				</p>
				<p>
					See
					<a href="./download.html">Downloads</a>
					for more information on how
					to add it.
				</p>
			</subsection>

			<subsection name="Validating a Resource">

				<p>
					To validate a resource instance, a new validator instance is requested
					from the FHIR Context. This validator is then applied against
					a specific resource
					instance, as shown in the example below.
				</p>
				<macro name="snippet">
					<param name="id" value="basicValidation" />
					<param name="file" value="examples/src/main/java/example/ValidatorExamples.java" />
				</macro>

			</subsection>

			<subsection name="Validating a Set of Files">

				<p>
					The following example shows how to load a set of resources from files
					on disk and validate each one.
				</p>
				<macro name="snippet">
					<param name="id" value="validateFiles" />
					<param name="file" value="examples/src/main/java/example/ValidatorExamples.java" />
				</macro>

			</subsection>

			<a name="structure_definition_validation" />
		</section>

	</body>

</document>
