# @smile-cdr/fhirts

TypeScript model definitions for HL7 FHIR, covering DSTU2, DSTU3, R4, R4B and R5.

These types are **generated** from the HAPI FHIR runtime model by `hapi-fhir-tinder`, the same
machinery that generates the HAPI Java model classes. They are not hand-maintained — do not edit the
generated files directly.

## Installation

```bash
npm install @smile-cdr/fhirts
```

## Usage

Each FHIR version is exposed as a namespace to avoid collisions between identically named resources:

```typescript
import { R4, R5 } from '@smile-cdr/fhirts';

const patient: R4.Patient = {
  resourceType: 'Patient',
  gender: 'female',
};
```

## How it is built

`hapi-fhir-tinder`'s `generate-typescript` goal walks each version's runtime model and emits one
interface per resource, complex datatype and backbone element, plus string-literal union aliases for
bound codes. The aggregator module `hapi-fhir-typescript` assembles these into this package and
type-checks them with `tsc`.

Publication to npm happens via the Maven `DEPLOY_TYPESCRIPT_HINTS` profile.
