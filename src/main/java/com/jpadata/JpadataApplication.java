package com.jpadata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class JpadataApplication {

	public static void main(String[] args) {

		SpringApplication.run(JpadataApplication.class, args);


	}

	// 등록자, 수정자를 처리해주는 AuditorAware 스프링 빈을 등록한다.
	@Bean
	public AuditorAware<String> auditorProvider() {
		// 실제로는 스프링 시큐리티 정보나 HTTP 세션에서 가져온다.
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
