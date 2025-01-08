## Device membership in Patient Compartment

As of 7.6.1, versions of FHIR below R5 now consider the `Device` resource's `patient` Search Parameter to be in the Patient Compartment. The following features are affected:

- Patient Search with `_revInclude=*`
- Patient instance-level `$everything` operation
- Patient type-level `$everything` operation
- Automatic Search Narrowing
- Bulk Export

Previously, there were various shims in the code that permitted similar behaviour in these features. Those shims have been removed. The only remaining component is [Advanced Compartment Authorization](/hapi-fhir/docs/security/authorization_interceptor.html#advanced-compartment-authorization), which can still be used 
to add other Search Parameters into a given compartment.
