package consultoria.askyu.syntro

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["consultoria.askyu.syntro.repository"])
class SyntroApplication

fun main(args: Array<String>) {
	 runApplication<SyntroApplication>(*args)
}
