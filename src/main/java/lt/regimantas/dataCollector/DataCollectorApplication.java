package lt.regimantas.dataCollector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class DataCollectorApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(DataCollectorApplication.class, args);
	}

}
