package dumbledore.dev.depthanalyzer;

import dumbledore.dev.components.Runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {Runner.class})
public class DepthAnalyzerApplication {

	private static final Logger logger = LoggerFactory.getLogger(DepthAnalyzerApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DepthAnalyzerApplication.class, args);
	}
}
