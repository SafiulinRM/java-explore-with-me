package ewm.util.filter;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Класс фильтр, который собирает параметры для получения событий пользователей в один класс
 *
 * @author safiulinrm
 * @see ewm.model.Event
 */
@Data
public class EventUserFilter {
    /**
     * текст для поиска в содержимом аннотации и подробном описании события
     */
    private String text;
    /**
     * список идентификаторов категорий в которых будет вестись поиск
     */
    private List<Long> categories;
    /**
     * поиск только платных/бесплатных событий
     */
    private Boolean paid;
    /**
     * дата и время не раньше которых должно произойти событи
     */
    private LocalDateTime rangeStart;
    /**
     * дата и время не позже которых должно произойти событие
     */
    private LocalDateTime rangeEnd;
    /**
     * только события у которых не исчерпан лимит запросов на участие
     * Default value : false
     */
    private boolean onlyAvailable;
    /**
     * Вариант сортировки: по дате события или по количеству просмотров
     * <p>
     * Available values : EVENT_DATE, VIEWS
     */
    private String sort;
    /**
     * количество событий, которые нужно пропустить для формирования текущего набора
     * Default value : 0
     */
    private int from;
    /**
     * количество событий в наборе
     * Default value : 10
     */
    private int size = 10;
    /**
     * Формат всех дат в данной программе
     */
    private static final String LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * Определение старта с преобразованием строки времени
     *
     * @param rangeStart дата и время не раньше которых должно произойти событие
     */
    public void setRangeStart(String rangeStart) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN);
        this.rangeStart = LocalDateTime.parse(rangeStart, formatter);
    }

    /**
     * Определение конца с преобразованием строки времени
     *
     * @param rangeEnd дата и время не позже которых должно произойти событие
     */
    public void setRangeEnd(String rangeEnd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_TIME_PATTERN);
        this.rangeEnd = LocalDateTime.parse(rangeEnd, formatter);
    }
}
