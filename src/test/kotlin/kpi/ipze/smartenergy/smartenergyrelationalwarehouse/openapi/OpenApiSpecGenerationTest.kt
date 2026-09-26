package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.openapi

import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.config.OpenApiConfig
import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.controller.TempControlController
import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.repository.TempControlSensorRecordRepository
import org.junit.jupiter.api.Test
import org.springdoc.core.configuration.SpringDocConfiguration
import org.springdoc.core.configuration.SpringDocJacksonKotlinModuleConfiguration
import org.springdoc.core.configuration.SpringDocKotlinConfiguration
import org.springdoc.core.configuration.SpringDocSpecPropertiesConfiguration
import org.springdoc.core.properties.SpringDocConfigProperties
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import java.nio.file.Files
import java.nio.file.Path
import kotlin.test.assertTrue

/**
 * Generates the OpenAPI spec from the web layer only, so no database is needed.
 * Output location is set by the `openapi.output.dir` system property (see `generateOpenApiSpec` in build.gradle.kts).
 */
@WebMvcTest(TempControlController::class)
@Import(OpenApiConfig::class)
@ImportAutoConfiguration(
    SpringDocConfiguration::class,
    SpringDocConfigProperties::class,
    SpringDocSpecPropertiesConfiguration::class,
    SpringDocKotlinConfiguration::class,
    SpringDocJacksonKotlinModuleConfiguration::class,
    SpringDocWebMvcConfiguration::class
)
class OpenApiSpecGenerationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var repository: TempControlSensorRecordRepository

    @Test
    fun generateSpec() {
        val outputDir = Path.of(System.getProperty("openapi.output.dir", "build/openapi"))
        Files.createDirectories(outputDir)

        val json = fetch("/v3/api-docs")
        assertTrue("/api/temp-control/records" in json, "Spec does not contain controller endpoints")
        Files.writeString(outputDir.resolve("openapi.json"), json)
        Files.writeString(outputDir.resolve("openapi.yaml"), fetch("/v3/api-docs.yaml"))
    }

    private fun fetch(path: String): String =
        mockMvc.get(path)
            .andExpect { status { isOk() } }
            .andReturn().response.getContentAsString(Charsets.UTF_8)
}
