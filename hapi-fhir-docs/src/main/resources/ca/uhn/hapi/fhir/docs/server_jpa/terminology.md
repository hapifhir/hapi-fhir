# Terminology

HAPI FHIR JPA Server includes an `IValidationSupport` class, `JpaPersistedResourceValidationSupport`, which can be used to validate terminology using CodeSystem, ValueSet and ConceptMap resources provided by the JPA Server. Terminology can be loaded into the JPA Server using standard FHIR REST APIs (PUT and POST) as well as using the hapi-fhir-cli [upload-terminology](/hapi-fhir/docs/tools/hapi_fhir_cli.html#upload-terminology) command.

# Versioning of Terminology

CodeSystem resources can be versioned as described in the FHIR specification [here](http://hl7.org/fhir/codesystem.html#versioning). Similarly, for ValueSet and ConceptMap resources that are defined with a versioned CodeSystem can also be versioned.

Versions for CodeSystem, ValueSet and ConceptMap resources are differentiated from each other by the CodeSystem.version, ValueSet.version and ConceptMap.version properties respectively. Each version of a given CodeSystem, ValueSet and ConceptMap resource will have a separate resource entity.

When queries or operations are performed involving CodeSystem, ValueSet, or ConceptMap resources that are versioned and no version parameter is provided, the JPA Server will reference the most recently updated version.

Delta Add and Remove modes in hapi-fhir-cli upload-terminology command will only apply to most recently updated version. Import from csv and export to csv hapi-fhir-cli commands will only apply to most recently updated version.

# Terminology Schemas

This page provides schema for tables that are used to complement and to map relationships between the CodeSystem, ValueSet, and ConceptMap resources and the various properties associated with these resources that are used or referenced by terminology operations.

## CodeSystem Tables

<img src="/hapi-fhir/docs/images/termcodesystem_schema.svg" alt="Resources" style="width: 100%; max-width: 600px;"/>

The TRM_CODESYSTEM_VER table indicates a single CodeSystem resource with a specific version. It can be used to map terminology concepts, represented by various TRM_CONCEPT_* tables to a single CodeSystem version. The TRM_CODESYSTEM table is used to model the canonical representation of a single CodeSystem and maps to a single TRM_CODESYSTEM_VER row which is treated as the current version of the CodeSystem (i.e. the version selected if no version is specified). For example, two CodeSystem resources `CodeSystem/loinc-2.67` and `CodeSystem/loinc-2.68` might have the same CodeSystem.url, e.g. `http://loinc.org` but different CodeSystem.version values. In this case each will each have exactly one row in the TRM_CODESYSTEM_VER table, but there will be only one row in the TRM_CODESYSTEM table which will link only to the most recently updated TRM_CODESYSTEM_VER resource.

### Columns

The following list are the main key columns in the TRM_CODESYSTEM_VER table that are used to join to the TRM_CODESYSTEM table and TRM_CONCEPT_* tables.

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Persistent ID of the TRM_CODESYSTEM_VER row.
            </td>
        </tr>
        <tr>
            <td>RES_ID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Persistent ID of the CodeSystem resource in the HFJ_RESOURCE table.
            </td>
        </tr>
        <tr>
            <td>CODESYSTEM_PID</td>
            <td></td>
            <td>Long</td>
            <td>Nullable</td>
            <td>
                Persistent ID of the TRM_CODESYSTEM row for canonical CodeSystem.
            </td>
        </tr>
        <tr>
            <td>CS_VERSION_ID</td>
            <td></td>
            <td>Long</td>
            <td>Nullable</td>
            <td>
                This is the optional CodeSystem.version of the CodeSystem resource.
            </td>
        </tr>
    </tbody>
</table>

The TRM_CODESYSTEM_VER table will have exactly one row for each unique combination of CODESYSTEM_PID and CS_VERSION_ID.

The following list are the main key columns in the TRM_CODESYSTEM table.

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Persistent ID of the TRM_CODESYSTEM row.
            </td>
        </tr>
        <tr>
            <td>CODE_SYSTEM_URI</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                URL of the CodeSystem resource.
            </td>
        </tr>
        <tr>
            <td>CURRENT_VERSION_PID</td>
            <td></td>
            <td>Long</td>
            <td>Nullable</td>
            <td>
                Persistent ID of the TRM_CODESYSTEM_VER row for current version of the CodeSystem.
            </td>
        </tr>
        <tr>
            <td>CS_NAME</td>
            <td></td>
            <td>String</td>
            <td>Nullable</td>
            <td>
                CodeSystem.name value for this CodeSystem resource.
            </td>
        </tr>
        <tr>
            <td>RES_ID</td>
            <td></td>
            <td>Long</td>
            <td>Nullable</td>
            <td>
                Persistent ID of the current version CodeSystem resource in the HFJ_RESOURCE table.
            </td>
        </tr>
    </tbody>
</table>

The TRM_CODESYSTEM table will have exactly one row for each unique CODE_SYSTEM_URI value.

## ValueSet Tables

<img src="/hapi-fhir/docs/images/termvalueset_schema.svg" alt="Resources" style="width: 100%; max-width: 600px;"/>

The TRM_VALUESET table indicates a single ValueSet resource with a specific version. It can be used to map terminology concepts, represented by the TRM_VALUESET_CONCEPT and TRM_VALUESET_C_DESIGNATION tables to a single ValueSet resource.

### Columns

The following list are the main key columns in the TRM_VALUESET table that are used to join to the TRM_VALUESET_CONCEPT and TRM_VALUESET_C_DESIGNATION tables.

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Persistent ID of the TRM_VALUESET row.
            </td>
        </tr>
        <tr>
            <td>RES_ID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Persistent ID of the ValueSet resource in the HFJ_RESOURCE table.
            </td>
        </tr>
        <tr>
            <td>URL</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                Canonical URL for ValueSet.
            </td>
        </tr>
        <tr>
            <td>VER</td>
            <td></td>
            <td>String</td>
            <td>Nullable</td>
            <td>
                Version ID for this ValueSet resource.
            </td>
        </tr>
    </tbody>
</table>

The TRM_VALUESET table will have exactly one row for each unique combination of URL and VER.

## ConceptMap Tables

<img src="/hapi-fhir/docs/images/termconceptmap_schema.svg" alt="Resources" style="width: 100%; max-width: 600px;"/>

The TRM_CONCEPTMAP table indicates a single ConceptMap resource with a specific version. It can be used to map terminology concepts to one another in groups.

### Columns

The following list are the main key columns in the TRM_CONCEPTMAP table that are used to join to the TRM_CONCEPTMAP_* tables.

<table class="table table-striped table-condensed">
    <thead>
        <tr>
            <th>Name</th>
            <th>Relationships</th>
            <th>Datatype</th>
            <th>Nullable</th>
            <th>Description</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>PID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Persistent ID of the TRM_CONCEPTMAP row.
            </td>
        </tr>
        <tr>
            <td>RES_ID</td>
            <td></td>
            <td>Long</td>
            <td></td>
            <td>
                Persistent ID of the ConceptMap resource in the HFJ_RESOURCE table.
            </td>
        </tr>
        <tr>
            <td>SOURCE_URL</td>
            <td></td>
            <td>String</td>
            <td>Nullable</td>
            <td>
                URL of source ValueSet to be mapped.
            </td>
        </tr>
        <tr>
            <td>TARGET_URL</td>
            <td></td>
            <td>String</td>
            <td>Nullable</td>
            <td>
                URL of target ValueSet to be mapped.
            </td>
        </tr>
        <tr>
            <td>URL</td>
            <td></td>
            <td>String</td>
            <td></td>
            <td>
                Canonical URL for ConceptMap.
            </td>
        </tr>
        <tr>
            <td>VER</td>
            <td></td>
            <td>String</td>
            <td>Nullable</td>
            <td>
                Version ID for this ConceptMap resource.
            </td>
        </tr>
    </tbody>
</table>

The TRM_CONCEPTMAP table will have exactly one row for each unique combination of URL and VER.
