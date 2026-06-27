package sco3;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "sco3")

public class McpServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(McpServerApplication.class, args);
	}
}
