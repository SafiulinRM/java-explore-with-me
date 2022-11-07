package ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс запуска микросервиса ExploreWithMe
 *
 * @author safiulinrm
 */
@SpringBootApplication
public class ExploreWithMe {
    /**
     * метод запускающий программу
     *
     * @param args ()
     */
    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMe.class, args);
    }
}
