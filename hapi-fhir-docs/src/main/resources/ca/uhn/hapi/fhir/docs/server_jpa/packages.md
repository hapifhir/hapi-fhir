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

# Using Installed Packages for Validation

Once a package is installed, its conformance resources (StructureDefinitions, ValueSets, CodeSystems, etc.) are stored in the JPA server database and automatically included in the validation support chain. No additional configuration is needed to validate against profiles from installed packages.

See [Validating Using Packages](../validation/instance_validator.html#packages) for details on how validation uses package content.

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

FHIR NPM packages may contain resources in folders other than the standard `package` folder (e.g., `examples`). To install resources from these additional folders, use `setAdditionalResourceFolders()` on the `PackageInstallationSpec`:

```java
PackageInstallationSpec spec = new PackageInstallationSpec()
    .setName("com.example.my-ig")
    .setVersion("1.0.0")
    .setInstallMode(PackageInstallationSpec.InstallModeEnum.STORE_AND_INSTALL)
    .setAdditionalResourceFolders(Set.of("examples"));
```

Resources from additional folders are installed the same way as resources from the standard `package` folder.
