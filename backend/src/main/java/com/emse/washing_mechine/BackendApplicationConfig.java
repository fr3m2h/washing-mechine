package com.emse.washing_mechine;

import com.emse.washing_mechine.hello.GreetingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BackendApplicationConfig {

    @Bean
    public CommandLineRunner greetingCommandLine(GreetingService greetingService) {
        return args -> {
            greetingService.greet("Spring");
        };
    }
}