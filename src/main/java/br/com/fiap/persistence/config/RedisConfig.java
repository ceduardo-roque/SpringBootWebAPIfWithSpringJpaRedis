package br.com.fiap.persistence.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * Classe de configuração do Cache do Redis Server
 * @author Carlos Eduardo Roque da Silva
 *
 */
@Configuration
@EnableCaching
@PropertySource("classpath:redis.properties")
public class RedisConfig {
	@Autowired
	private Environment environment;

	/**
	 * Método chamado pelo Spring para recuperar a Connection Factory do Spring
	 * @return uma LettuceConnectionFactory configurada com os parametros do Spring
	 */
	@Bean
	public LettuceConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
		redisConf.setHostName(environment.getProperty("redis.host"));
		redisConf.setPort(Integer.parseInt(environment.getProperty("redis.port")));
		redisConf.setPassword(RedisPassword.of(environment.getProperty("redis.password")));
		return new LettuceConnectionFactory(redisConf);
	}

	/**
	 * Método chamado pelo Spring para recuperar o RedisCacheManager do Spring Data
	 * @return RedisCacheManager criando uma conexão
	 */
	@Bean
	public RedisCacheManager cacheManager() {
		RedisCacheManager rcm = RedisCacheManager.create(redisConnectionFactory());
		rcm.setTransactionAware(true);
		return rcm;
	}
}