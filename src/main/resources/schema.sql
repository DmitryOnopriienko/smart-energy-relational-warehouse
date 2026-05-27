create table if not exists temp_control_sensor_record (
    id bigserial primary key,
    sensor_id integer not null,
    parameter varchar(64) not null,
    value double precision not null,
    timestamp timestamp not null
);

create index if not exists idx_temp_control_sensor_record_sensor_id_ts_desc
    on temp_control_sensor_record (sensor_id, timestamp desc);

create index if not exists idx_temp_control_sensor_record_timestamp
    on temp_control_sensor_record (timestamp);
