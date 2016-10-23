package es.unizar.es.foodnet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
public class ThymeleafConfig {

    /**
     * Provee la informacion necesaria para resolver los recursos del sistema como paginas HTML, especificando
     * la ruta donde se van a encontrar, asi como extensiones para simplificar la labor de los controladores
     */
    @Bean
    public ServletContextTemplateResolver templateResolver() {
        ServletContextTemplateResolver resolver = new ServletContextTemplateResolver();
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode("LEGACYHTML5");
        resolver.setOrder(1);
        return resolver;
    }

    /**
     * Linkeamos a thymeleaf para que sea el resolver por defecto de spring
     */
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    /**
     * Configuracion de thymeleaf como resolver
     */
    @Bean
    public ThymeleafViewResolver thymeleafViewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());

        //Necesario para preveer fallos de codificacion en los html, como simbolos, acentos, etc.
        resolver.setCharacterEncoding("UTF-8");
        return resolver;
    }

    // Provides internationalization of messages
    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("messages");
        return source;
    }
}