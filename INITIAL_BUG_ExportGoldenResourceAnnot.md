There is a bug in group bulk export with mdm expansion enabled. Our goal is to reproduce the bug by writing a test
and then fix the bug, and verify the bug is fixed by seeing the test pass.
In Group type bulk export with mdm expansion, there is a feature where the exported resources related to a patient
are annotated with a hapi resource extension that contain the golden resource id that caused the resource to be
exported. For example, if we are exporting a group that contains a bunch of patients with mdm expansion enabled,
then the output of the export would contain all the patients from the group, plus all the patients mdm matching to any patient in the group,
and all the related resources like Observations that are tied to the exported patients. Each exported Observation in that case
would have an extension added which has a system ""https://hapifhir.org/associated-patient-golden-resource/" and a value of the extension
is the golden resource id that caused the Observation to be exported.

I noticed that the exported related resources don't have the extension added anymore. I noticed the problem caused by the cache implementation at
/Users/emredincturk/workspace/master/hapi-fhir/hapi-fhir-server-mdm/src/main/java/ca/uhn/fhir/mdm/svc/MdmExpansionCacheSvc.java.
There is a discrepancy between the population of this cache and how the values are retrieved from the cache.
I noticed the problem is when we populate the cache we populate it with an id like Patient/1234 but when we retrieve
it we just try to retrieve it with "1234" missing the Patient part. This happens in
/Users/emredincturk/workspace/master/hapi-fhir/hapi-fhir-server-mdm/src/main/java/ca/uhn/fhir/mdm/svc/BulkExportMdmResourceExpander.java in
getPatientReference function which returns the id part. but it should return the id with resource type included.

Now, our goal is to fix this bug and more importantly we should create a IT test for it first. Unfortunately we don't have any existing tests for it.
The package /Users/emredincturk/workspace/master/hapi-fhir/hapi-fhir-jpaserver-mdm has some IT tests for mdm services but it doesn't have any covering the bulk export scenario.
/Users/emredincturk/workspace/master/hapi-fhir/hapi-fhir-jpaserver-test-r4/src/test/java/ca/uhn/fhir/jpa/bulk/BulkExportUseCaseTest.java has some
bulk export tests but there isn't any that covers the bulk group export with mdm expansion is enabled with the full
mdm mode, there is only one which covers mdm eid match only mode, in which case this annotation feature is not
applicable, and we need the full mdm setup similar to the tests in
/Users/emredincturk/workspace/master/hapi-fhir/hapi-fhir-jpaserver-mdm.

Adding a test in /Users/emredincturk/workspace/master/hapi-fhir/hapi-fhir-jpaserver-mdm might be easier since there
are tests that already use mdm and some mdm rules json files we can use in the tests. We need to have a mdm rules
json rule and then run file and mdm settings and then run the group export.

So, what we will do is
- add the test for the bug case
- run the test to reproduce the bug
- fix the bug
- verify the test passes
