package com.aron;

import com.aron.factory.AronRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author aron
 */
@EnableJpaRepositories(repositoryFactoryBeanClass = AronRepositoryFactoryBean.class)
@EnableScheduling
@SpringBootApplication
public class AronTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AronTestApplication.class, args);
    }
}
