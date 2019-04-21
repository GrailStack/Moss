package org.xujin.moss.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class Sb2xApplication {

	private static Logger logger = LoggerFactory.getLogger(Sb2xApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(Sb2xApplication.class, args);
		logger.info("我是启动日志");
	}
}
