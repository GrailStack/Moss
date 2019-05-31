package org.xujin.moss;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.xujin.moss.filter.CorsFilter;

import javax.servlet.Filter;

@Configuration
@EnableAutoConfiguration
@EnableAdminServer
//@EnableScheduling
@EnableDiscoveryClient
@ComponentScan("org.xujin.moss.*")
@EnableAsync
public class MossApplication {

	@Bean
	public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer(){
		return new WebServerFactoryCustomizer<ConfigurableWebServerFactory>() {
			@Override
			public void customize(ConfigurableWebServerFactory factory) {
				factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/index.html"));
				//factory.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/index.html"));
			}
		};
	}

	@Bean
	public RestTemplate restTemplate(){
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(10000);
		requestFactory.setReadTimeout(10000);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
		return restTemplate;
	}


	@Bean
	public InternalResourceViewResolver defaultViewResolver() {
		return new InternalResourceViewResolver();
	}

	@Bean
	public Filter corsFilter() {
		return new CorsFilter();
	}

	public static void main(String[] args) {
		SpringApplication.run(MossApplication.class, args);
	}

}
