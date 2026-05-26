package kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import kpi.ipze.smartenergy.smartenergyrelationalwarehouse.tempcontrol.enums.TempControlParameter
import java.time.Instant

@Entity
@Table(name = "temp_control_sensor_record")
class TempControlSensorRecordEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "sensor_id", nullable = false)
    var sensorId: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(name = "parameter", nullable = false, length = 64)
    var parameter: TempControlParameter = TempControlParameter.TEMPERATURE,

    @Column(name = "value", nullable = false)
    var value: Double = 0.0,

    @Column(name = "timestamp", nullable = false)
    var timestamp: Instant = Instant.EPOCH
)
