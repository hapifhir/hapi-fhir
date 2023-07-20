library HelloWorldPatientView version '1.0.0'

using FHIR version '3.0.0'

include FHIRHelpers version '3.0.0'

context Patient

define "Info":
  'info'

define "Warning":
  'warning'

define "Critical":
  'critical'

define "Main Action Condition Expression Is True":
  true

define "Get Title":
  'Hello World!'

define "Get Description":
  'The CDS Service is alive and communicating successfully!'

define "Get Indicator":
  'info'
