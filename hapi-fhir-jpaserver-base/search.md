


# Paging

Paging is complicated.

```mermaid
sequenceDiagram
    actor Client
    participant RestfulServer
    participant BaseResourceReturningMethodBinding
    participant ConsentService
    
    Client ->> RestfulServer: some search like Patient?code=active
    RestfulServer ->> RestfulServer : lookup binding
    
    RestfulServer ->>+ BaseResourceReturningMethodBinding: invoke doInvokeServer
    
    BaseResourceReturningMethodBinding ->>+ BaseResourceReturningMethodBinding :  invokeServer()
    BaseResourceReturningMethodBinding --> PatientResourceProvider: Call search()
    PatientResourceProvider ->> patientDao: delegate to search()
    patientDao ->> searchCoordinatorService: delegate to registerSearch()
    searchCoordinatorService ->> searchCoordinatorService : figure out kind of search ("sync" vs "async"), and IBundleProvider to return.  Could be offset, or cached.
    searchCoordinatorService -->  patientDao: reply with IBundleProvider
    PatientResourceProvider -->  BaseResourceReturningMethodBinding: reply with IBundleProvider
    BaseResourceReturningMethodBinding ->>- BaseResourceReturningMethodBinding: IBundleProvider


    BaseResourceReturningMethodBinding ->> BaseResourceReturningMethodBinding: build Bundle from IBundleProvider with ResponseBundleBuilder
%% we have fetched exactly page size resources for the bundle here, but we are about to invoke Consent
%% need to move consent into here somehow which will throw some away
    BaseResourceReturningMethodBinding ->> ConsentService: via callOutgoingResponseHook - nukes some entries from Bundle
    
    BaseResourceReturningMethodBinding ->- RestfulServer: return actual Bundle
    
    

```

What we need to do to avoid "blank spots" from the ConsentService is to move the call into consent
before we finalize the Bundle.
ca.uhn.fhir.rest.server.method.ResponseBundleBuilder#pagingBuildResourceList looks likely.


