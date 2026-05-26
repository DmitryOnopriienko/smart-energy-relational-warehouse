package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.controller

import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.dto.SaveTempControlSensorRecordRequest
import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.dto.SaveTempControlSensorRecordResponse
import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.dto.TempControlSensorRecordDto
import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.entity.TempControlSensorRecordEntity
import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.repository.TempControlSensorRecordRepository
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@RestController
@RequestMapping("/api/temp-control/records")
class TempControlController(
    private val repository: TempControlSensorRecordRepository
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun save(@RequestBody request: SaveTempControlSensorRecordRequest): SaveTempControlSensorRecordResponse {
        val entity = TempControlSensorRecordEntity(
            sensorId = request.sensorId,
            parameter = request.parameter,
            value = request.value,
            timestamp = request.timestamp
        )
        val saved = repository.save(entity)
        return SaveTempControlSensorRecordResponse(saved.toDto())
    }

    private fun TempControlSensorRecordEntity.toDto(): TempControlSensorRecordDto = TempControlSensorRecordDto(
        id = requireNotNull(id) { "Saved record id is null" },
        sensorId = sensorId,
        parameter = parameter,
        value = value,
        timestamp = timestamp
    )

    @GetMapping("/latest")
    fun findLatestBySensorId(@RequestParam sensorId: Int): TempControlSensorRecordDto {
        val record = repository.findLatestBySensorId(sensorId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found")
        return record.toDto()
    }

    @GetMapping
    fun findAfterAndBeforeTimestamps(
        @RequestParam("after") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) after: Instant,
        @RequestParam("before") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) before: Instant
    ): List<TempControlSensorRecordDto> =
        repository.findAfterAndBeforeTimestamps(after, before).map { it.toDto() }
}
