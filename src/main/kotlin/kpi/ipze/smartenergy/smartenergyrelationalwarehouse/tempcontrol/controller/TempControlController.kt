package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
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
@Tag(name = "Temperature control", description = "Records reported by temperature control sensors")
class TempControlController(
    private val repository: TempControlSensorRecordRepository
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Save a sensor record", description = "Stores a single measurement reported by a sensor.")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Record saved"),
        ApiResponse(responseCode = "400", description = "Malformed request body", content = [Content()])
    )
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

    @GetMapping("/latest")
    @Operation(summary = "Get the latest record of a sensor", description = "Returns the record with the most recent timestamp for the given sensor.")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Latest record found"),
        ApiResponse(responseCode = "404", description = "The sensor has no records", content = [Content()])
    )
    fun findLatestBySensorId(
        @Parameter(description = "Sensor identifier", example = "1")
        @RequestParam sensorId: Int
    ): TempControlSensorRecordDto {
        val record = repository.findLatestBySensorId(sensorId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Record not found")
        return record.toDto()
    }

    @GetMapping
    @Operation(
        summary = "Get records in a time range",
        description = "Returns records of all sensors whose timestamp is strictly between `after` and `before`."
    )
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Records in the range (possibly empty)"),
        ApiResponse(responseCode = "400", description = "Missing or malformed timestamps", content = [Content()])
    )
    fun findAfterAndBeforeTimestamps(
        @Parameter(description = "Exclusive lower bound, ISO-8601 date-time", example = "2026-05-27T00:00:00Z")
        @RequestParam("after") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) after: Instant,
        @Parameter(description = "Exclusive upper bound, ISO-8601 date-time", example = "2026-05-28T00:00:00Z")
        @RequestParam("before") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) before: Instant
    ): List<TempControlSensorRecordDto> =
        repository.findAfterAndBeforeTimestamps(after, before).map { it.toDto() }

    private fun TempControlSensorRecordEntity.toDto(): TempControlSensorRecordDto = TempControlSensorRecordDto(
        id = requireNotNull(id) { "Saved record id is null" },
        sensorId = sensorId,
        parameter = parameter,
        value = value,
        timestamp = timestamp
    )
}
