# FHIR R4B

FHIR R4B is an unusual FHIR release meant to help certain users take advantage of certain FHIR R5 models and features before the release and adoption of FHIR R5. Functionally, R4B is mainly identical to FHIR R4 but with specific resources backported from R5.

As of HAPI FHIR 6.2.0, this version is now supported. See [FHIR and HAPI FHIR Versions](versions.html) for a complete table of FHIR specification version support in HAPI FHIR software versions.

**Support for FHIR R4B is experimental** and may never be completely implemented. It has not been extensively tested and we are not currently aiming for feature parity with normal FHIR version support.

The following notes outline current support and limitations.

* **FHIR Parsers/Serializers** are well tested and should be fully functional, as there is no need for any specific R4B code in this part of the codebase.

* **FHIR Client** is well tested and should be fully functional, as there is no need for any specific R4B code in this part of the codebase.

* **Plain Server** is well tested and should be fully functional, as there is no need for any specific R4B code in this part of the codebase.

* **Validator** does not currently work with R4B and may crash or cause unexpected results. Note that the underlying InstanceValidator is able to validate R4B resources, so this could be fixed in a future release if demand is there. The [FHIR Validator Site](https://validator.fhir.org/) can also be used.

* **JPA Server** is able to store data, and supports all basic storage interactions (CRUD). Search also works, including built-in and custom search parameters. However most other functionality has not been implemented or tested, including Subscriptions, Terminology Services, etc.

