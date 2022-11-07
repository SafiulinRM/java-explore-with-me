package stats.service;

import stats.model.Hit;
import stats.model.ViewStats;

import java.util.List;

/**
 * Интерфейс сервиса обработки просмотров событий
 */
public interface StatsService {
    /**
     * Сохранение просмотра событий
     *
     * @param hit данные просмотра событий {@link Hit}
     */
    void postHit(Hit hit);

    /**
     * Получение статистики просмотров
     *
     * @param start  дата и время не раньше которых должно просмотрено событие
     * @param end    текущее время и дата
     * @param uris   список путей событий
     * @param unique уникальное/полное количество просмотров
     * @return список статистик {@link List {@link ViewStats}}
     */
    List<ViewStats> getStats(String start,
                             String end,
                             List<String> uris,
                             Boolean unique);
}
