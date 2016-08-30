package ca.uhn.fhir.to;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import ca.uhn.fhir.to.mvc.AnnotationMethodHandlerAdapterConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ca.uhn.fhir.to")
public class FhirTesterMvcConfig extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry theRegistry) {
		theRegistry.addResourceHandler("/css/**").addResourceLocations("/css/");
		theRegistry.addResourceHandler("/fa/**").addResourceLocations("/fa/");
		theRegistry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/");
		theRegistry.addResourceHandler("/img/**").addResourceLocations("/img/");
		theRegistry.addResourceHandler("/js/**").addResourceLocations("/js/");
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("/WEB-INF/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCharacterEncoding("UTF-8");
		return resolver;
	}

	@Bean
	public AnnotationMethodHandlerAdapterConfigurer annotationMethodHandlerAdapterConfigurer() {
		return new AnnotationMethodHandlerAdapterConfigurer();
	}

	@Bean
	public ThymeleafViewResolver viewResolver() {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());

		return templateEngine;
	}

}
