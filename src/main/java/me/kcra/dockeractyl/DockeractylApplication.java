package me.kcra.dockeractyl;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"me.kcra.dockeractyl"})
public class DockeractylApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .registerShutdownHook(true)
                .run(args);
    }
}
