package consultoria.askyu.nf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories(basePackages = ["consultoria.askyu.nf.repository"])
class NfApplication

fun main(args: Array<String>) {
	 runApplication<NfApplication>(*args)
}
