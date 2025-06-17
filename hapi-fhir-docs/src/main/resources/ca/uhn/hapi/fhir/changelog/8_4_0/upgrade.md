## Breaking Changes

* FhirPath `PATCH` operations that match multiple elements will no longer replace these values, but throw an exception. This is in line with the <a href="https://www.hl7.org/fhir/R4/fhirpatch.html">spec</a>.
