<!-- Created by Claude Opus 5.5 -->
# Maven dependency versioning in HAPI FHIR

How dependency versions are declared in this repository, so they stay in one place. Projects that use the HAPI root pom as their parent inherit all of this too, so every rule below also shapes what they resolve.

## Pom hierarchy

Legend: `▲ ┃` = has as parent (inherits) · `- - ▶` = imports as a BOM (`<scope>import</scope>`)

```
 ┌──────────────────────┐
 │ hapi-fhir (root pom) │ - - ▶ spring-data-bom, spring-framework-bom,
 │ big dependencyMgmt   │       testcontainers-bom, junit-bom,
 └──────────────────────┘       opentelemetry-instrumentation-bom
            ▲
            ┃  (all hapi-* modules inherit from root)
 ┌─────────────────────┐
 │ hapi-deployable-pom │
 └─────────────────────┘
            ▲
            ┃
 ┌─────────────────────┐
 │ hapi-fhir-bom       │  lists ~90 hapi-* artifacts
 └─────────────────────┘
```

All `hapi-*` modules inherit the root pom's `<dependencyManagement>` and `<properties>`. `hapi-fhir-bom` is what external projects import. Its entries are written as `${project.groupId}:…:${project.version}`, so they only resolve to HAPI's coordinates when the BOM is imported. Inheriting `hapi-fhir-bom` as a parent turns them into the child's own group and version.

## Rules

**1. Versions live in the root `<dependencyManagement>`.** A child pom declares a third-party dependency without `<version>`, and its exclusions follow rule 6. HAPI's own modules are the exception. When one module depends on another module of this repository, built in the same Maven run (for example `hapi-fhir-structures-r4` depending on `hapi-fhir-base`), it writes `<version>${project.version}</version>`, so both always come from the same build. The root doesn't manage HAPI's own artifacts. Only `hapi-fhir-bom` lists them, and HAPI's modules don't import it, so such a declaration without a version fails. Never hard-code a HAPI version on one of these. The one exception is `hapi-tinder-plugin`, which deliberately depends on an older HAPI version (rule 8).

**2. Every managed version is a property.** It's named `<artifact>_version` in snake case (`h2_version`, `jakarta_servlet_api_version`) and defined in the root `<properties>`, in the alphabetical block. Never write a literal version in a dependency.
- Artifacts released together share one property (`greenmail_version`, `maven_scm_version`, `assertj_version`, `json_lib_version`).
- Unrelated libraries get one property each, even from the same group. The webjars, for example, are `webjars_bootstrap_version`, `webjars_jquery_version` and so on.

**3. A module that needs another version overrides the property.** It sets the root's property in its own `<properties>`, and the dependency keeps no version of its own. It has no `<version>` if the root manages the artifact, or `<version>${…}</version>` if it's one of rule 4's property-only artifacts, such as `commons-lang` below. Comment the property, not the dependency, with when and why:

```xml
<properties>
	<!-- Pinned above the root's commons-lang 2.5 since 2021 to fix a vulnerability warning (0cea4038006, #2621) -->
	<commons_lang_version>2.6</commons_lang_version>
</properties>
```

Use "no reason given" when the history doesn't say. A reason that belongs to a version goes next to the property: the derby "Don't bump this, they drop support for JRE 17" note sits above `derby_version`.

**4. Don't manage an artifact if that changes what other modules get transitively.** A dependency-management entry also overrides every transitive occurrence of the artifact, in every HAPI module and in every project that inherits the root pom. If that would move someone else's version, define the root property but no entry, and let the declaring modules use `<version>${…}</version>`. Current examples:
- `snakeyaml`, `commons-net`, `commons-lang` and `cqf-fhir-utility`, commented "Not in dependencyManagement: …".
- The FHIR core `org.hl7.fhir.convertors`, `dstu2`, `dstu2016may`, `r4b`, `r5` and `validation` artifacts, on `${fhir_core_version}`.
- `reflections` in `hapi-fhir-server-cds-hooks`.

**5. Dependency management only matches its exact coordinates.**
- **`<plugin><dependencies>`** extend a plugin's own classpath, which Maven resolves without the project's `<dependencyManagement>`. They can be centralized through `<pluginManagement>`; otherwise they keep a `<version>` written as a property.
- **`<additionalDependencies>`** (`maven-javadoc-plugin` configuration) isn't a Maven dependency at all. The plugin resolves those coordinates itself, so neither `<dependencyManagement>` nor `<pluginManagement>` reaches them, and they always need a `<version>`, written as a property (see `jetbrains_annotations_javadoc_version` in `hapi-fhir-converter`).
- **Classifiers and types** are part of the key. A declaration with `<classifier>javadoc</classifier>` isn't covered by the plain jar's entry, and needs its own entry or its own `<version>${…}</version>`. One example is `org.hl7.fhir.r4:javadoc` in the `DIST` profile of `hapi-fhir-structures-r4`.

**6. Exclusions go on the managed entry only when every declaration has the same set.** Maven adds managed exclusions only to a declaration that lists none of its own. A child with any `<exclusions>` block keeps the full set itself.

Managed exclusions also reach transitive occurrences and every consumer of `hapi-fhir-bom`. Keep an exclusion in the child poms when it would surprise consumers. For example, `junit:junit` keeps its `hamcrest-core` exclusion in its three child poms, because on the managed entry it would break JUnit 4's `assertThat` for BOM users.

**7. Treat property names as part of the root pom's interface.** A project that inherits the root pom also inherits its properties, so any property name is an override point there. If such a project already defines the same name for something else, its value silently wins in its builds. Pick specific names, and don't rename existing properties casually.

**8. Deliberate exceptions stay local, behind a module property:**
- `hapi-tinder-plugin` builds against `${previous_hapi_fhir_version}` HAPI structures and its own Maven API (`maven_core_version`). The structures modules build with tinder, so tinder can't depend on the versions being built.
- `tests/hapi-fhir-base-test-mindeps-*` exist to test minimum supported versions.
- `kotlin.version` is local to `tests/hapi-fhir-base-test-jaxrsserver-kotlin`.

## Checking a pom change

A pom refactor must not change what anything resolves. Before and after the change, for every module (and for any project you build on top of this one, after installing HAPI):

```bash
mvn install -P FASTINSTALL,NOJAVADOCS
mvn dependency:list -DoutputFile=target/deps.txt -DexcludeTransitive=false -Dsort=true
mvn dependency:resolve-plugins -DoutputFile=target/plugins.txt -Dsort=true   # separate run: a shared -DoutputFile overwrites
mvn help:effective-pom -Doutput=effective.xml
```

`diff` the `deps.txt` and `plugins.txt` of each module, and the effective poms with comments and absolute paths stripped. The resolved lists only cover active profiles, and an unused managed version changes nothing in them. The effective pom shows both, which is how the `DIST` profile's javadoc dependency was caught.
