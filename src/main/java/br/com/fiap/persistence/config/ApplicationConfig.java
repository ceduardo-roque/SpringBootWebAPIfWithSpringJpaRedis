package br.com.fiap.persistence.config;

import java.util.List;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Classe de configuração da aplicação WebMvc
 * @author Carlos Eduardo Roque da Silva
 *
 */
@Configuration 
@ComponentScan("br.com.fiap") 
@EnableWebMvc   
public class ApplicationConfig implements WebMvcConfigurer {  
	
	/**
	 * Método que configura o conversor do Jackson pra ele retornar o JSON no request HTTP
	 * @return void convertendo as as mensagens
	 */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true);
        converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
    }
} 