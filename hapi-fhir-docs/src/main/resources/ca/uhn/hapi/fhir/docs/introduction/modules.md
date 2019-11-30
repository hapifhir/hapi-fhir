# HAPI FHIR Modules

The following table shows the modules that make up the HAPI FHIR library.

<table class="table table-condensed">
    <tbody>
    <tr>
        <td colspan="3" style="text-align: center; font-size: 1.2em; background: #DDE; padding: 3px;">
            Core Libraries
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-base</td>
        <td>
            <a href="../../apidocs/hapi-fhir-base/">JavaDoc</a>
        </td>
        <td>
            This is the core HAPI FHIR library and is always required in order to use
            the framework. It contains the context, parsers, and other support classes.
        </td>
    </tr>
    <tr>
        <td style="text-align: center; font-size: 1.2em; background: #DDE; padding: 3px;" colspan="3">
            Structures
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-structures-dstu (retired)</td>
        <td>
        </td>
        <td>
            This module contained FHIR DSTU1 model classes. It was retired in HAPI FHIR 3.0.0.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-structures-dstu2</td>
        <td>
            <a href="../../apidocs/hapi-fhir-structures-dstu2/">JavaDoc</a>
        </td>
        <td>
            This module contains FHIR DSTU2 model classes.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-structures-hl7org-dstu2</td>
        <td>
            <a href="../../apidocs/hapi-fhir-structures-hl7org-dstu2/">JavaDoc</a>
        </td>
        <td>
            This module contains alternate FHIR DSTU2 model classes. The HAPI FHIR and FHIR "Java Reference
            Implementation"
            libraries were merged in 2015, and at the time there were two parallel sets of DSTU2 model
            classes. This set more closely resembles the model classes for DSTU3+ where the other set
            more closely resembles the original DSTU1 model classes. The two DSTU2 model JARs are functionally
            identital, but the various utility methods on the classes are somewhat different.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-structures-dstu3</td>
        <td>
            <a href="../../apidocs/hapi-fhir-structures-dstu3/">JavaDoc</a>
        </td>
        <td>
            This module contains FHIR DSTU3 model classes.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-structures-r4</td>
        <td>
            <a href="../../apidocs/hapi-fhir-structures-r4/">JavaDoc</a>
        </td>
        <td>
            This module contains FHIR R4 model classes.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-structures-r5</td>
        <td>
            <a href="../../apidocs/hapi-fhir-structures-r5/">JavaDoc</a>
        </td>
        <td>
            This module contains FHIR R5 model classes.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-converter</td>
        <td>
            <a href="../../apidocs/hapi-fhir-converter/">JavaDoc</a>
        </td>
        <td>
            This module contains converters for converting between FHIR versions.
        </td>
    </tr>
    <tr>
        <td style="text-align: center; font-size: 1.2em; background: #DDE; padding: 3px;" colspan="3">Client
            Framework
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-client</td>
        <td>
            <a href="../../apidocs/hapi-fhir-client/">JavaDoc</a>
        </td>
        <td>
            This module contains the core FHIR client framework, including an
            HTTP implementation based on
            <a href="https://hc.apache.org/">Apache HttpClient</a>. It is required in order
            to use client functionality in HAPI.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-client-okhttp</td>
        <td>
        </td>
        <td>
            This module contains an alternate HTTP implementation based on
            <a href="http://square.github.io/okhttp/">OKHTTP</a>.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-android</td>
        <td>
        </td>
        <td>
            This module contains the Android HAPI FHIR framework, which is a FHIR
            client framework which has been tailored specifically to run on Android.
        </td>
    </tr>
    <tr>
        <td style="text-align: center; font-size: 1.2em; background: #DDE; padding: 3px;" colspan="3">
            Validation
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-validation</td>
        <td>
        </td>
        <td>
            This module contains the FHIR Profile Validator, which is used to
            validate resource instances against FHIR Profiles (StructureDefinitions,
            ValueSets, CodeSystems, etc.).
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-validation-resources-dstu2</td>
        <td>
        </td>
        <td>
            This module contains the StructureDefinitions, ValueSets, CodeSystems, Schemas,
            and Schematrons for FHIR DSTU2
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-validation-resources-dstu2.1</td>
        <td>
        </td>
        <td>
            This module contains the StructureDefinitions, ValueSets, CodeSystems, Schemas,
            and Schematrons for FHIR DSTU2.1
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-validation-resources-dstu3</td>
        <td>
        </td>
        <td>
            This module contains the StructureDefinitions, ValueSets, CodeSystems, Schemas,
            and Schematrons for FHIR DSTU3
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-validation-resources-r4</td>
        <td>
        </td>
        <td>
            This module contains the StructureDefinitions, ValueSets, CodeSystems, Schemas,
            and Schematrons for FHIR R4
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-validation-resources-r5</td>
        <td>
        </td>
        <td>
            This module contains the StructureDefinitions, ValueSets, CodeSystems for R5. As of FHIR
            R5, Schema and Schematron files are no longer distributed with HAPI FHIR.
        </td>
    </tr>
    <tr>
        <td style="text-align: center; font-size: 1.2em; background: #DDE; padding: 3px;" colspan="3">Server</td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-server</td>
        <td>
            <a href="../../apidocs/hapi-fhir-server/">JavaDoc</a>
        </td>
        <td>
            This module contains the HAPI FHIR Server framework, which can be used to
            develop FHIR compliant servers against your own data storage layer.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-jpaserver-base</td>
        <td>
            <a href="../../apidocs/hapi-fhir-jpaserver-base/">JavaDoc</a>
        </td>
        <td>
            This module contains the HAPI FHIR "JPA Server", which is a complete
            FHIR server solution including a database and implementations of many
            advanced FHIR server features.
        </td>
    </tr>
    <tr>
        <td style="font-weight: bold; white-space: nowrap;">hapi-fhir-testpage-overlay</td>
        <td>
        </td>
        <td>
            This module contains the web based "testpage overlay", which is the
            UI that powers our
            <a href="http://fhirtest.uhn.ca">Public Demo Server</a>
            and can also be added to your applications.
        </td>
    </tr>
    </tbody>
</table>
			
