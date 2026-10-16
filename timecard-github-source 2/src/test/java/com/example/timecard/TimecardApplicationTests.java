package com.example.timecard;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties="spring.datasource.url=jdbc:h2:mem:context;DB_CLOSE_DELAY=-1;MODE=MySQL")
class TimecardApplicationTests {

	@Test
	void contextLoads() {
	}

}
