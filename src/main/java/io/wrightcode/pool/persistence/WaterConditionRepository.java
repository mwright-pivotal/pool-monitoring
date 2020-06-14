package io.wrightcode.pool.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.wrightcode.pool.model.PoolTelemetry;

public interface WaterConditionRepository extends CrudRepository<PoolTelemetry, Long> {
	List<PoolTelemetry> findByMonitorUUID(String monitorUUID);
	List<PoolTelemetry> findTop100ByOrderByTimeUpdatedDesc();
}
