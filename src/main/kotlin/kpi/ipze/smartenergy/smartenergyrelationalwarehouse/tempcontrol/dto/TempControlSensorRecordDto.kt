package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.dto

import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.enums.TempControlParameter
import java.time.Instant

data class TempControlSensorRecordDto(
    val id: Long,
    val sensorId: Int,
    val parameter: TempControlParameter,
    val value: Double,
    val timestamp: Instant
)
