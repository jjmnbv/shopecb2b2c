package net.shopec;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(basePackages = "net.shopec.dao")
@ComponentScan(basePackages = { "net.shopec.util", "net.shopec" })
public class ApplicationB2B2C {

	public static void main(String[] args) {
		SpringApplication.run(ApplicationB2B2C.class, args);

	}

}