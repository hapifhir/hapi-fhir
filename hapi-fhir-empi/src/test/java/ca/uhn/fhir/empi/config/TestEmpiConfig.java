package ca.uhn.fhir.empi.config;

import ca.uhn.fhir.empi.api.IEmpiConfig;
import ca.uhn.fhir.empi.rules.config.EmpiConfigImpl;
import com.google.common.base.Charsets;
import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
@ComponentScan("ca.uhn.fhir.empi")
public class TestEmpiConfig {

}
