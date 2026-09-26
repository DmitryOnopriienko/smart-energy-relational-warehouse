package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.dto

import io.swagger.v3.oas.annotations.media.Schema
import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.enums.TempControlParameter
import java.time.Instant

@Schema(description = "Measurement reported by a sensor")
data class SaveTempControlSensorRecordRequest(
    @Schema(description = "Sensor identifier", example = "1")
    val sensorId: Int,
    @Schema(description = "Measured parameter")
    val parameter: TempControlParameter,
    @Schema(description = "Measured value in the unit of the parameter", example = "21")
    val value: Double,
    @Schema(description = "Moment of measurement, ISO-8601 date-time", example = "2026-05-27T12:00:00Z")
    val timestamp: Instant
)
