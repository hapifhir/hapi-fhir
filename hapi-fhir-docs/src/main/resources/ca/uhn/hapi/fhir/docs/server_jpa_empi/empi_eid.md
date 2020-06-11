# EMPI Enterprise Identifiers

An Enterprise Identifier(EID) is a unique identifier that can be attached to Patients or Practitioners. Each implementation is expected to use exactly one EID system for incoming resources, defined in the EMPI Rules file. If a Patient or Practitioner with a valid EID is submitted, that EID will be copied over to the Person that was matched. In the case that the incoming Patient or Practitioner had no EID assigned, an internal EID will be created for it. There are thus two classes of EID. Internal EIDs, created by HAPI-EMPI, and External EIDs, provided by the submitted resources.


