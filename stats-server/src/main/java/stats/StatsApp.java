package stats;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс запуска микросервиса stats-server
 *
 * @author safiulinrm
 */
@SpringBootApplication
public class StatsApp {
    /**
     * метод запускающий программу
     *
     * @param args ()
     */
    public static void main(String[] args) {
        SpringApplication.run(StatsApp.class, args);
    }
}