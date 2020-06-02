package com.son.SpringJPA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing
@SpringBootApplication
public class SpringJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringJpaApplication.class, args);
	}

	@Bean
	public AuditorAware<String> auditorProvider() {
		/*
		 BaseEntity의 등록자와 수정자를 불러오기 위한 메서드를 빈으로 등록하였음
		 보통의 경우에는 SecurityContext에서 꺼내서 리턴한다
		 */
		return () -> Optional.of(UUID.randomUUID().toString());
	}

}
