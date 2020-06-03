package com.dkm.file;

import com.dkm.aop.beans.Aspect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Administrator
 */
@EnableSwagger2
@SpringBootApplication
@EnableTransactionManagement
public class FileApplication extends Aspect {

    public static void main(String[] args) {
        SpringApplication.run(FileApplication.class, args);
    }
}
