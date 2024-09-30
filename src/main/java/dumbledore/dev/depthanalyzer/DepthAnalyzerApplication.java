package dumbledore.dev.depthanalyzer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DepthAnalyzerApplication {

	private static final Logger logger = LoggerFactory.getLogger(DepthAnalyzerApplication.class);

	public static void main(String[] args) {
		logger.info("APPLICATION EXECUTION - INITIATED");
		SpringApplication.run(DepthAnalyzerApplication.class, args);
		logger.info("APPLICATION EXECUTION - COMPLETED");
	}
}
