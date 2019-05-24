package ca.uhn.fhir.jpa.searchparam.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ComponentScan(basePackages = {"ca.uhn.fhir.jpa.searchparam"})
abstract public class BaseSeachParamConfig {

}
