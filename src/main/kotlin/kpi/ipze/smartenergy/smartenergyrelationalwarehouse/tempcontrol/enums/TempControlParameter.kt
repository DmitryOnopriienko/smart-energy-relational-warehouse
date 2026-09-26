package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.enums

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Parameter measured by a temperature control sensor. TEMPERATURE is in °C.")
enum class TempControlParameter(val unit: String) {
    TEMPERATURE("°C")
}
