# Server Interceptors

There are many different Pointcuts available to server developers. In general, a server can be thought of as playing two roles: Server and Storage.

In the case of a Plain Server, HAPI FHIR itself performs the role of the Server and your [Resource Provider](/docs/server_plain/resource_providers.html) classes perform the role of Storage.

In the case of a JPA Server, HAPI FHIR itself performs both roles. This means that **SERVER_xxx** Pointcuts may be intercepted by interceptors on any HAPI FHIR server. However, if you want to intercept **STORAGE_xxx** Pointcuts on a plain server, you will need to trigger them yourself.

