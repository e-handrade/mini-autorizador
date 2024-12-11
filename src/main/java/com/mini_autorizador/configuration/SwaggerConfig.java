package com.mini_autorizador.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Mini Autorizador API")
                        .version("0.0.1")
                        .description("API para transações de cartões de benefícios")
                        .contact(new Contact()
                                .name("Emerson")
                                .email("emerson.deandrade@vr.com.br")
                                .url("http://www.vr.com.br")));

    }
}

