package br.com.fiap.persistence.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Classe para iniciar a aplicação web e mapear o Servlet Principal
 * @author Carlos Eduardo Roque da SIlva
 *
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	/**
	 * Metodo para configurar root de classs
	 * @return configuração ApplicationConfig configuração do aplicattion class
	 */
	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { ApplicationConfig.class };
	}

	/**
	 * Metodo para configurar servlet config class
	 * @return null
	 */

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	/**
	 * Metodo para retornar o mapeamento no servet 
	 * @return array String[]
	 */
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}