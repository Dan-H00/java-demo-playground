package dan_javademoplayground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
@EnableCaching
public class JavaDemoPlaygroundApplication {

	@Bean(name = "personCacheManager")
	public CacheManager personCacheManger() {
		return new ConcurrentMapCacheManager("person", "agency");
	}

	public static void main(String[] args) {
		SpringApplication.run(JavaDemoPlaygroundApplication.class, args);
	}

}
