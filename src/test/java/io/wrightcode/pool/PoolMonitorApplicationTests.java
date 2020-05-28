package io.wrightcode.pool;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.wrightcode.pool.model.PoolTelemetry;
import io.wrightcode.pool.persistence.WaterConditionRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class PoolMonitorApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	WaterConditionRepository repository;

	@Test
	public void contextLoads() {
	}

	@Test
	public void sendWaterCondition() throws Exception {
		PoolTelemetry waterCondition = new PoolTelemetry("integ-test", 7.0, 220.0, 120.0);

		mockMvc.perform(post("/v1/api/status", 42L).contentType("application/json")
				.content(objectMapper.writeValueAsString(waterCondition))).andExpect(status().isOk());
		
		assertThat(repository.findAll().iterator().hasNext());
	}

}
