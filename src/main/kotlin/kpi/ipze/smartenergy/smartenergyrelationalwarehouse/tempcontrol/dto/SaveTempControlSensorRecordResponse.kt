package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Result of saving a sensor record")
data class SaveTempControlSensorRecordResponse(
    @Schema(description = "Saved record")
    val record: TempControlSensorRecordDto
)
