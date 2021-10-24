package me.kcra.dockeractyl.serv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class DockeractylConfiguration {
    private final Environment env;

    @Autowired
    public DockeractylConfiguration(Environment env) {
        this.env = env;
    }

    @Bean("taskScheduler")
    public ThreadPoolTaskScheduler taskScheduler() {
        final ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(env.getProperty("dockeractyl.task.sched.pool", Integer.class, 2));
        scheduler.setThreadNamePrefix("da-sched");
        return scheduler;
    }

    @Bean("threadPool")
    public ThreadPoolTaskExecutor taskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(env.getProperty("dockeractyl.task.exec.pool", Integer.class, 2));
        executor.setThreadNamePrefix("da-exec");
        return executor;
    }

    @Bean("tempFolder")
    public Path tempFolder() {
        return Paths.get(env.getProperty("dockeractyl.temp", String.class, "<cwd>/dockeractyl/temp").replace("<cwd>", System.getProperty("user.dir")));
    }
}
