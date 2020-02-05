package io.wrightcode.pool.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmGcMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmMemoryMetrics;
import io.micrometer.core.instrument.binder.jvm.JvmThreadMetrics;
import io.micrometer.core.instrument.binder.system.ProcessorMetrics;
import io.wrightcode.pool.model.AtomicFloat;
import io.wrightcode.pool.model.Status;
import io.wrightcode.pool.persistence.WaterConditionRepository;

@RestController("MonitorController")
@RequestMapping("v1/api")
public class MonitorController {
	Counter counter = null;
	
	Logger log = Logger.getLogger(MonitorController.class.getName());
	
	MeterRegistry meterRegistry;
	private AtomicFloat ph = new AtomicFloat(0);
	private AtomicFloat tds = new AtomicFloat(0);
	private AtomicFloat orp = new AtomicFloat(0);
	
	@Autowired
	WaterConditionRepository repository;
	
	@Autowired
	public MonitorController(MeterRegistry meterRegistry) {
		this.meterRegistry=meterRegistry;
		counter = meterRegistry.counter("watercondition.updates", "dc", "WrightCode", "service", "pool-monitor");
	
		new ClassLoaderMetrics().bindTo(meterRegistry);
		new JvmMemoryMetrics().bindTo(meterRegistry);
		new JvmGcMetrics().bindTo(meterRegistry);
		new ProcessorMetrics().bindTo(meterRegistry);
		new JvmThreadMetrics().bindTo(meterRegistry);
	}
	
	@RequestMapping(value="/status", method = RequestMethod.POST)
	public void sendStatus(@RequestBody Status stat) {
		stat.setTimeUpdated(new Date());
		log.info("Received update: " + stat);
		AtomicFloat phGauge = meterRegistry.gauge("watercondition.phValue", this.ph);
		phGauge.set(Float.parseFloat(stat.getPhValue()));
		
		AtomicFloat orpGauge = meterRegistry.gauge("watercondition.orpValue", this.orp);
		orpGauge.set(Float.parseFloat(stat.getOrpValue()));
		
		AtomicFloat tdsGauge = meterRegistry.gauge("watercondition.tdsValue", this.tds);
		tdsGauge.set(Float.parseFloat(stat.getTdsValue()));
		//customGauge.getAndSet(Long.parseLong(stat.getPhValue()));
		counter.increment();
		
		repository.save(stat);
	}
}
