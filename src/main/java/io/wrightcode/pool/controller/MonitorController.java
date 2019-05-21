package io.wrightcode.pool.controller;

import java.util.logging.Logger;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.wrightcode.pool.model.Status;

@RestController("MonitorController")
@RequestMapping("v1/api")
public class MonitorController {

	Logger log = Logger.getLogger(MonitorController.class.getName());
	
	@RequestMapping(value="/status", method = RequestMethod.POST)
	public void sendStatus(Status stat) {
		log.info("Received update: " + stat);
	}
}
