package ewm.util.filter;

import ewm.util.status.EventState;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс фильтр, который собирает параметры для получения событий администратором в один класс
 *
 * @author safiulinrm
 * @see ewm.model.Event
 */
@Data
public class EventAdminFilter {
    /**
     * список id пользователей, чьи события нужно найти
     */
    private List<Long> users;
    /**
     * список состояний в которых находятся искомые события
     */
    private List<String> states;
    /**
     * список id категорий в которых будет вестись поиск
     */
    private List<Long> categories;
    /**
     * дата и время не раньше которых должно произойти событие
     */
    private LocalDateTime rangeStart;
    /**
     * дата и время не позже которых должно произойти событие
     */
    private LocalDateTime rangeEnd;
    /**
     * количество событий, которые нужно пропустить для формирования текущего набора
     * Default value : 0
     */
    private Integer from;
    /**
     * количество событий в наборе
     * Default value : 10
     */
    private Integer size;
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

    /**
     * Получение списка состояний путем преобразования строк в enum
     *
     * @return возвращает список состояний событий {@link List {@link EventState}}
     */
    public List<EventState> getStatesEnum() {
        return states.stream().map(EventState::valueOf).collect(Collectors.toList());
    }
}
