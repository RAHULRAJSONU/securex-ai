package io.github.rahulrajsonu.securexai.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.stereotype.Component;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "intzdata.com",
                        email = "info@intzdata.com",
                        url = "https://intzdata.com/"
                ),
                description = "Api documentation for SecureX Ai A Zanzibar Authorization server",
                title = "OpenApi specification - bazaar.com",
                version = "1.0",
                license = @License(
                        name = "Licence name",
                        url = "https://intzdata.com/"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "Local ENV",
                        url = "http://localhost:8080/"
                )
        },
        security = {
                @SecurityRequirement(
                        name = "bearerAuth"
                )
        }
)
@SecurityScheme(
        name = "X-API-KEY",
        description = "X-API-KEY",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-API-KEY"
)
@SecurityScheme(
        name = "X-SECRET-KEY",
        description = "X-SECRET-KEY",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-SECRET-KEY"
)
@SecurityScheme(
        name = "X-TenantID",
        description = "X-TenantID",
        type = SecuritySchemeType.APIKEY,
        in = SecuritySchemeIn.HEADER,
        paramName = "X-TenantID"
)
@Component
public class OpenApiConfig {

}