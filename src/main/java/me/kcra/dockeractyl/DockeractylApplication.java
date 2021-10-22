package me.kcra.dockeractyl;

import lombok.extern.slf4j.Slf4j;
import me.kcra.dockeractyl.utils.MiscUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@EnableAsync
@EnableScheduling
@SpringBootApplication(scanBasePackages = {"me.kcra.dockeractyl"})
public class DockeractylApplication {
    public static void main(String[] args) {
        if (!MiscUtils.hasExecutable("docker")) {
            log.error("Docker was not found on your machine, please install it!");
            System.exit(-1);
        }
        new SpringApplicationBuilder()
                .registerShutdownHook(true)
                .run(args);
    }
}
