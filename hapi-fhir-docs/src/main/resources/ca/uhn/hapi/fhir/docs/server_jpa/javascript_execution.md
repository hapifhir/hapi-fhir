# JavaScript Execution Operation

The system-level `$execute-javascript` operation runs a **server-side** JavaScript file (via the embedded GraalJS engine) to transform FHIR resources. Callers do **not** send code - they reference a script by name that an administrator has placed in a configured directory. The script receives the input resources as a JavaScript array called `input` and returns a single resource object or an array of resource objects, which are returned as `return` parameters.

The operation is implemented by [JavaScriptExecutionProvider](/hapi-fhir/apidocs/hapi-fhir-jpaserver-base/ca/uhn/fhir/jpa/provider/JavaScriptExecutionProvider.html). Note that this provider is not wired into the JPA server by default - deployments which want to offer the operation need to add the `org.graalvm.js:js` dependency (declared as optional by `hapi-fhir-jpaserver-base`), construct the provider, and register it against their `RestfulServer`.

# Security Model

* The operation is unavailable unless a scripts directory has been configured via `JpaStorageSettings#setJavaScriptExecutionScriptsDirectory(String)`.
* The `script` parameter is resolved to a file inside the configured scripts directory only (bare file name, no path traversal).
* Each script runs in a GraalJS sandbox created with no host access - Java classes, the filesystem, the network and thread creation are all denied (no JVM/filesystem/network reach).
* Each invocation is bounded by an execution timeout (`JpaStorageSettings#setJavaScriptExecutionTimeoutSeconds(long)`, default `30`); a script that overruns is stopped and the call fails.

# Parameters

Inputs may be supplied two ways (combined into `input` in order - inline resources first, then resolved references):

* `script`: (*mandatory*) The name of the script to execute, with or without the `.js` suffix.
* `resource`: (*optional, repeatable*) An inline FHIR resource.
* `reference`: (*optional, repeatable*) A literal reference (e.g. `Patient/123`) that the server reads before the script runs.

# Example

An example script which sets `active=true` on every input resource and returns them:

```javascript
// add-active.js
input.map(function (resource) {
	resource.active = true;
	return resource;
});
```

Example request body to `POST [base]/$execute-javascript`:

```json
{
  "resourceType": "Parameters",
  "parameter": [
    { "name": "script", "valueString": "add-active" },
    { "name": "resource", "resource": { "resourceType": "Patient", "active": false, "name": [{ "family": "Doe" }] } },
    { "name": "reference", "valueReference": { "reference": "Patient/123" } }
  ]
}
```

The response is a `Parameters` resource whose `return` parameters hold the transformed resources.
