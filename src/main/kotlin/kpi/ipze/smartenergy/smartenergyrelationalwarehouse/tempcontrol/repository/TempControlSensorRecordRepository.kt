package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.repository

import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.entity.TempControlSensorRecordEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.Instant

interface TempControlSensorRecordRepository : JpaRepository<TempControlSensorRecordEntity, Long> {
    @Query(
        value = """
        select *
        from temp_control_sensor_record
        where sensor_id = :sensorId
        order by timestamp desc
        limit 1
        """,
        nativeQuery = true
    )
    fun findLatestBySensorId(@Param("sensorId") sensorId: Int): TempControlSensorRecordEntity?

    fun findTopBySensorIdOrderByTimestampDesc(sensorId: Int): TempControlSensorRecordEntity?

    @Query(
        """
        select r
        from TempControlSensorRecordEntity r
        where r.timestamp > :after and r.timestamp < :before
        """
    )
    fun findAfterAndBeforeTimestamps(
        @Param("after") after: Instant,
        @Param("before") before: Instant
    ): List<TempControlSensorRecordEntity>
}
