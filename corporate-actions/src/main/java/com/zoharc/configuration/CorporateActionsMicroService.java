package com.zoharc.configuration;

import java.util.Arrays;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.integration.config.EnableIntegration;

@EnableIntegration
@EnableBatchProcessing
@SpringBootApplication
@ComponentScan(basePackages = "com.zoharc")
public class CorporateActionsMicroService {

	public static void main(String[] args) {
		SpringApplication.run(CorporateActionsMicroService.class, args);
	}

	@Bean
	@ConditionalOnProperty(prefix = "corp.actions", name = "enable.bean.logging", matchIfMissing = false)
	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
		return args -> {

			System.out.println("The follwing beans were initilazed: ");

			String[] beanNames = ctx.getBeanDefinitionNames();
			Arrays.sort(beanNames);
			for (String beanName : beanNames) {
				System.out.println(beanName);
			}
		};
	}    

}
