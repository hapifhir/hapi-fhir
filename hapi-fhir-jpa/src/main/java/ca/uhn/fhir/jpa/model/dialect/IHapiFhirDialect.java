package ca.uhn.fhir.jpa.model.dialect;

import ca.uhn.fhir.jpa.migrate.DriverTypeEnum;

public interface IHapiFhirDialect {
    DriverTypeEnum getDriverType();
}
