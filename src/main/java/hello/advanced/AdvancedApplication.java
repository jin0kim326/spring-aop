package hello.advanced;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class AdvancedApplication {

	public static void main(String[] args) {

		SpringApplication.run(AdvancedApplication.class, args);
		log.info("✅ SERVER STARTED...");
	}

}
