package dan_javademoplayground;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class JavaDemoPlaygroundApplicationTests {

	@Test
	void contextLoads() {
	}

}
