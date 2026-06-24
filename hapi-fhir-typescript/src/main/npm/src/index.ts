// Top-level entry point for the @smile-cdr/fhirts package.
// Each FHIR version is exposed as its own namespace so that identically-named
// interfaces (e.g. IPatient in R4 and R5) do not collide.
export * as DSTU2 from './FHIR-DSTU2';
export * as DSTU3 from './FHIR-DSTU3';
export * as R4 from './FHIR-R4';
export * as R4B from './FHIR-R4B';
export * as R5 from './FHIR-R5';
