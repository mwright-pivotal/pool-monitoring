package io.wrightcode.pool.persistence;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.wrightcode.pool.model.Status;

public interface WaterConditionRepository extends CrudRepository<Status, Long> {
	List<Status> findByMonitorUUID(String monitorUUID);
}
