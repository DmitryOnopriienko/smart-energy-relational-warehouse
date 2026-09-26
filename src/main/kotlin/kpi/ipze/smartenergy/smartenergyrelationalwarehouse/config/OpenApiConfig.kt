package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.config

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Smart Energy Relational Warehouse API",
        version = "v1",
        description = "Storage and retrieval of relational data for smart energy department projects"
    )
)
class OpenApiConfig
