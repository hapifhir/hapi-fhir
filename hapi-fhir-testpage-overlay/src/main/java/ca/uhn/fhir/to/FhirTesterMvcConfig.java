package ca.uhn.fhir.to;

import ca.uhn.fhir.to.mvc.AnnotationMethodHandlerAdapterConfigurer;
import ca.uhn.fhir.to.util.WebUtil;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "ca.uhn.fhir.to")
public class FhirTesterMvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(@Nonnull ResourceHandlerRegistry theRegistry) {
		WebUtil.webJarAddAceBuilds(theRegistry);
		WebUtil.webJarAddBoostrap(theRegistry);
		WebUtil.webJarAddJQuery(theRegistry);
		WebUtil.webJarAddFontAwesome(theRegistry);
		WebUtil.webJarAddJSTZ(theRegistry);
		WebUtil.webJarAddEonasdanBootstrapDatetimepicker(theRegistry);
		WebUtil.webJarAddMomentJS(theRegistry);
		WebUtil.webJarAddSelect2(theRegistry);
		WebUtil.webJarAddAwesomeCheckbox(theRegistry);
		WebUtil.webJarAddPopperJs(theRegistry);

		theRegistry.addResourceHandler("/css/**").addResourceLocations("/css/");
		theRegistry.addResourceHandler("/fa/**").addResourceLocations("/fa/");
		theRegistry.addResourceHandler("/fonts/**").addResourceLocations("/fonts/");
		theRegistry.addResourceHandler("/img/**").addResourceLocations("/img/");
		theRegistry.addResourceHandler("/js/**").addResourceLocations("/js/");
	}

	@Bean
	public SpringResourceTemplateResolver templateResolver(TesterConfig theTesterConfig) {
		SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
		resolver.setPrefix("/WEB-INF/templates/");
		resolver.setSuffix(".html");
		resolver.setTemplateMode(TemplateMode.HTML);
		resolver.setCharacterEncoding("UTF-8");

		if (theTesterConfig.getDebugTemplatesMode()) {
			resolver.setCacheable(false);
		}

		return resolver;
	}

	@Bean
	public AnnotationMethodHandlerAdapterConfigurer annotationMethodHandlerAdapterConfigurer(
			@Qualifier("requestMappingHandlerAdapter") RequestMappingHandlerAdapter theAdapter) {
		return new AnnotationMethodHandlerAdapterConfigurer(theAdapter);
	}

	@Bean
	public ThymeleafViewResolver viewResolver(SpringTemplateEngine theTemplateEngine) {
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(theTemplateEngine);
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}

	@Bean
	public SpringTemplateEngine templateEngine(SpringResourceTemplateResolver theTemplateResolver) {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(theTemplateResolver);

		return templateEngine;
	}
}
