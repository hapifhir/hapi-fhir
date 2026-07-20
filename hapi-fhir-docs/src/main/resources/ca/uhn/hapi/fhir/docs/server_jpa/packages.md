# FHIR Package Registry

The HAPI FHIR JPA Server includes a built-in FHIR package registry that supports uploading and managing FHIR NPM packages, also known as FHIR Implementation Guides (IGs). Packages stored in the registry are automatically made available to the built-in [Instance Validator](../validation/instance_validator.html) so that resources can be validated against custom profiles without any additional configuration.

# Uploading Packages

Packages can be uploaded to the JPA server using the FHIR package registry REST API:

```http
PUT [base]/Package/[id]/$package
Content-Type: application/tar+gzip

[binary package content]
```

HAPI FHIR also supports uploading packages directly from the [FHIR NPM package registry](https://packages.fhir.org) using the `PackageInstallationSpec` API in Java:

```java
PackageInstallationSpec spec = new PackageInstallationSpec()
    .setName("hl7.fhir.us.core")
    .setVersion("7.0.0")
    .setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
    .setFetchDependencies(true);

myPackageInstallerSvc.install(spec);
```

HAPI FHIR also supports installing a package asynchronously using a batch process:

```java
myPackageInstallerSvc.installAsynchronously(spec);
```

# Install Mode

The `installMode` field on `PackageInstallationSpec` controls what is persisted during installation:

| Mode | Behaviour |
|---|---|
| `STORE_ONLY` | Conformance resources are stored in dedicated package tables and made available to the validator. Resources are **not** stored as queryable FHIR resources. Use this when the package is needed for validation only. |
| `STORE_AND_INSTALL` | Same as `STORE_ONLY`, plus resources are stored as individual FHIR resources queryable via the FHIR API. |
| `INSTALL_ONLY` | Resources are stored as individual FHIR resources (like `STORE_AND_INSTALL`), but the package itself is **not** retained in the local package cache. Use this when you want the resources available for FHIR operations but do not need the package for validation or a package registry endpoint. |

# Fetching Transitive Dependencies

When `setFetchDependencies(true)` is set on the `PackageInstallationSpec`, the installer recursively fetches and installs all transitive dependencies declared in each package's `package.json` file. Dependencies are resolved from the local package cache first, then fetched from the [packages.fhir.org](https://packages.fhir.org) registry if not found locally.

# Excluding Dependencies

When `fetchDependencies` is enabled, you can use the `dependencyExcludes` field to skip specific transitive dependencies. This field accepts a list of Java regular expressions. Each dependency's package ID is tested against every pattern, and if any pattern matches, that dependency is skipped.

This is useful for excluding large base packages that are already built into the server (e.g., `hl7.fhir.r4.core`) or packages that are not needed in your environment.

```java
PackageInstallationSpec spec = new PackageInstallationSpec()
    .setName("hl7.fhir.us.core")
    .setVersion("7.0.0")
    .setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
    .setFetchDependencies(true)
    .addDependencyExclude("^hl7\\.fhir\\.r4\\.core$")
    .addDependencyExclude("^hl7\\.terminology.*");
```

The equivalent JSON representation:

```json
{
  "name": "hl7.fhir.us.core",
  "version": "7.0.0",
  "installMode": "STORE_AND_INSTALL",
  "fetchDependencies": true,
  "dependencyExcludes": [
    "^hl7\\.fhir\\.r4\\.core$",
    "^hl7\\.terminology.*"
  ]
}
```

In the example above:

* `^hl7\.fhir\.r4\.core$` matches the exact package ID `hl7.fhir.r4.core`.
* `^hl7\.terminology.*` matches any package ID starting with `hl7.terminology`, such as `hl7.terminology.r4`.

<p class="doc_info_bubble">
<b>Note:</b> The values are Java regular expressions, not simple strings. Use <code>\.</code> to match a literal dot, since <code>.</code> in regex matches any character.
</p>

# Cross-Version Package Dependencies

Some FHIR packages are designed to work across multiple FHIR versions. For example, `hl7.fhir.uv.extensions` is a cross-version extensions package that declares FHIR version `5.0.0` in its package metadata, even though it is commonly used as a dependency of R4 Implementation Guides.

When the JPA server is running in R4 mode and `fetchDependencies=true` is enabled, the package installer performs **automatic version-specific substitution** for cross-version dependencies:

1. After loading a transitive dependency, the installer compares the dependency's declared FHIR version against the server's FHIR version.
2. If the versions are incompatible, the installer attempts to load a version-specific variant of the package by appending a FHIR version suffix to the package ID:

| Server FHIR Version | Suffix applied |
|---------------------|----------------|
| R4 or R4B           | `.r4`          |
| R5                  | `.r5`          |
| DSTU3               | `.r3`          |

3. For example, if an R4 server encounters a dependency on `hl7.fhir.uv.extensions` (which declares FHIR 5.0.0), the installer automatically attempts to load `hl7.fhir.uv.extensions.r4` at the same version.
4. If the version-specific variant is found in the local cache or on packages.fhir.org, it is substituted for the original dependency and installation continues.
5. If the variant cannot be found, a warning is logged and installation continues with the original package, which may produce a version compatibility error.

This behavior avoids installation failures caused by cross-version packages that are declared as FHIR 5.0.0 but have an R4-specific counterpart available on the package registry.

<p class="doc_info_bubble">
<b>Note:</b> The substitution is attempted silently. If a version-specific variant does not exist on the package registry, the server falls back to the original (incompatible-version) package and logs a warning. Installation may still succeed if the package content is otherwise compatible, but a version mismatch error will be raised if the contents are genuinely incompatible.
</p>

# Version Compatibility Checking

When a package is installed, the server verifies that the package's declared FHIR version is compatible with the server's running FHIR version. If the versions are incompatible and no version-specific variant is available, the installation will fail with an error (HAPI-1288).

The following version pairs are treated as compatible:

* R4 and R4B are treated as compatible with each other (both are considered `R4`-family).
* All other version combinations are treated as incompatible and will trigger the cross-version substitution logic described above.

# Version Policy

The `versionPolicy` parameter on `PackageInstallationSpec` controls how the installer matches existing canonical resources during installation. The default is `MULTI_VERSION`.

> **Note:** `versionPolicy` applies only to canonical resources (e.g. StructureDefinition, ValueSet, CodeSystem, ConceptMap, SearchParameter). Non-conformance instance resources (e.g. Patient, Organization) are always installed per-resource using their original IDs and are not affected by this setting.

| Policy | Resource Matching | Behavior |
|--------|------------------|----------|
| `MULTI_VERSION` (default) | Matches by canonical URL **and** version | Multiple versions of the same conformance resource can coexist in the repository. Server-assigned IDs are used. |
| `SINGLE_VERSION` | Matches by canonical URL only (ignoring version) | Only one version of each conformance resource exists. Installing a new version overwrites the previous one. Client-assigned IDs from the package are used. |

```java
PackageInstallationSpec spec = new PackageInstallationSpec()
    .setName("hl7.fhir.us.core")
    .setVersion("7.0.0")
    .setInstallMode(PackageInstallationSpec.InstallModeEnum.INSTALL_ONLY)
    .setVersionPolicy(PackageInstallationSpec.VersionPolicyEnum.SINGLE_VERSION);
```

## Redundant Dependency Handling

When `fetchDependencies` is enabled, a package's dependency tree may pull in multiple versions of the same transitive dependency. For example, installing `us.nlm.vsac` version `0.19.0` may depend on `hl7.terminology.r4` version `6.2.0`, while a transitive dependency may pull in `hl7.terminology.r4` version `5.4.0`. Without protection, the older version could silently overwrite canonical resources that were already installed by the newer version.

The installer detects redundant dependencies and skips them in both modes:

* **`SINGLE_VERSION` mode**: All canonical resources are affected since they are matched by URL only. The installer tracks the highest version of each dependency package encountered during the installation. If an older or already-installed version of a package is encountered later in the dependency tree, it is treated as redundant and skipped.
* **`MULTI_VERSION` mode**: Only `SearchParameter` resources are affected since they are matched by `code` and `base` rather than URL and version. Other canonical resources coexist by URL and version and are not at risk. The installer compares package versions at the resource level and treats updates from older packages as redundant. This relies on the `meta.source` field that the installer stamps on each resource. Resources created manually or installed before this stamping was in place are not protected.

## Concurrent Install Conflicts

When packages are installed in parallel under `SINGLE_VERSION` mode, two installs may search for the same canonical URL, find nothing, and race to create the same client-assigned resource ID. The second install logs an error at ERROR level and continues.

Two ways to avoid this:

* **Switch to `MULTI_VERSION`** (recommended): new resources receive server-assigned IDs, eliminating the ID collision.
* **Serialize installs**: eliminates the race, but the later install will overwrite any shared resource from the earlier one — last writer wins.

# Using Installed Packages for Validation

Once a package is installed, its conformance resources (StructureDefinitions, ValueSets, CodeSystems, etc.) are stored in the JPA server database and automatically included in the validation support chain. No additional configuration is needed to validate against profiles from installed packages.

See [Validating Using Packages](../validation/instance_validator.html#packages) for details on how validation uses package content.

# Resource Matching

During installation, the installer searches for an existing resource before deciding whether to create or update. The search criteria vary by resource type:

| Resource type | Matched by |
|---|---|
| Canonical resources (those with a `url` element, e.g. StructureDefinition, ValueSet, CodeSystem) | Canonical URL — see [Version Policy](#version-policy) for how `versionPolicy` further affects this |
| `SearchParameter` | Canonical form (code + base resource types), falling back to URL or identifier |
| `Subscription` | Resource ID |
| `NamingSystem` | Unique ID value |
| Non-conformance resources (e.g. Patient, Organization) | `identifier` — **must be present**; resources without an identifier will fail to install with `HAPI-1292` |

When an existing resource is found, the installer uses its server-assigned ID for the update, discarding any ID supplied in the package.

# Installed Resource Types

By default, the following resource types are installed from a package: `NamingSystem`, `CodeSystem`, `ValueSet`, `StructureDefinition`, `ConceptMap`, `SearchParameter`, `Subscription`. To install a different set of resource types, use `setInstallResourceTypes()` on the `PackageInstallationSpec`:

```java
PackageInstallationSpec spec = new PackageInstallationSpec()
    .setName("hl7.fhir.us.core")
    .setVersion("7.0.0")
    .setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
    .setInstallResourceTypes(List.of("StructureDefinition", "ValueSet", "CodeSystem"));
```

# Installing Resources from Additional Folders

FHIR NPM packages may contain resources in folders other than the standard `package` folder (e.g., `example`). To install resources from these additional folders, use `setAdditionalResourceFolders()` on the `PackageInstallationSpec`:

```java
PackageInstallationSpec spec = new PackageInstallationSpec()
    .setName("com.example.my-ig")
    .setVersion("1.0.0")
    .setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
    .setAdditionalResourceFolders(Set.of("example"));
```

Resources from additional folders are installed the same way as resources from the standard `package` folder.

Non-conformance resources in additional folders (e.g. `Patient`, `Organization`) may reference each other or resources not yet present in the repository. If referential integrity enforcement is enabled on the server, these writes will fail with `HAPI-1094` unless auto-creation of placeholder reference targets is enabled. When enabled, the server automatically creates an empty stub for any missing reference target; the stub is overwritten when the referenced resource is installed later.

# Resource Status Validation

By default, the installer accepts resources regardless of their `status` value. When resource status validation is enabled, resources are filtered during installation based on the following rules:

| Resource type | Accepted status values |
|---|---|
| `Subscription` | `requested` |
| `DocumentReference`, `Communication` | Any value except `?` |
| All other types | `active` |

Resources whose status does not match the accepted values for their type are skipped during installation.

To enable this behaviour, set `validateResourceStatusForPackageUpload` to `true` on `JpaStorageSettings`.
