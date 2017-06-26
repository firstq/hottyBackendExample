package su.hotty.editor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import su.hotty.editor.util.CatalogDataCreator;
import su.hotty.editor.util.TestDataCreator;

@SpringBootApplication
//@ComponentScan(basePackages = {"su.hotty.editor.service","su.hotty.editor.repository","su.hotty.editor.web","su.hotty.editor.domain","su.hotty.editor.config"})
//@EntityScan("su.hotty.editor.domain")
@EnableMongoRepositories(mongoTemplateRef = "mongoTemplate", basePackages = "su.hotty.editor.repository")
public class HottyServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(HottyServerApplication.class, args);
	}


	@Bean
	public CommandLineRunner dataPopulation(CatalogDataCreator catalogDataCreator,
											TestDataCreator testDataCreator) {
		return (args) -> {
			//catalogDataCreator.populateSocialNetTypes();
		};
	}
}
