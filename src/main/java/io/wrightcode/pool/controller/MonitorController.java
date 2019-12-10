package io.wrightcode.pool.controller;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.wrightcode.pool.model.Status;

@RestController("MonitorController")
@RequestMapping("v1/api")
public class MonitorController {

	Logger log = Logger.getLogger(MonitorController.class.getName());
	
	@Autowired
	MeterRegistry meterRegistry;
	private AtomicLong ph = new AtomicLong(0);
	
	@RequestMapping(value="/status", method = RequestMethod.POST)
	public void sendStatus(@RequestBody Status stat) {
		stat.setTimeUpdated(new Date());
		log.info("Received update: " + stat);
		AtomicLong customGauge = meterRegistry.gauge("phValue", this.ph);
		customGauge.set(Long.parseLong(stat.getPhValue()));
		//customGauge.getAndSet(Long.parseLong(stat.getPhValue()));
	}
}
