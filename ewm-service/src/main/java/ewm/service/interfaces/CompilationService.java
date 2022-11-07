package ewm.service.interfaces;

import ewm.dto.compilation.CompilationDto;
import ewm.dto.compilation.NewCompilationDto;

import java.util.List;

/**
 * Интерфейс сервиса обработки запросов по подборкам от админа
 */
public interface CompilationService {
    /**
     * Получение подборок событий
     *
     * @param pinned Закрепленные или нет подборки
     * @param from   номер первого элемента
     * @param size   размер страницы
     * @return List of {@link CompilationDto}
     */
    List<CompilationDto> getCompilationsByPinned(Boolean pinned, int from, int size);

    /**
     * Получить подборку по id
     *
     * @param compId id подборки
     * @return {@link CompilationDto}
     */
    CompilationDto getCompilation(Long compId);

    /**
     * Создание подборки
     *
     * @param newCompilationDto {@link NewCompilationDto}
     * @return {@link CompilationDto}
     */
    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    /**
     * Удаление подборки
     *
     * @param compId id подборки
     */
    void deleteCompilation(Long compId);

    /**
     * Удаление события из подборки
     *
     * @param compId  id подборки
     * @param eventId id события
     */
    void deleteEventOfCompilation(Long compId, Long eventId);

    /**
     * Добавление события в подборку
     *
     * @param compId  id подборки
     * @param eventId id события
     */
    void addEventOfCompilation(Long compId, Long eventId);

    /**
     * Открепить подборку с главной страницы
     *
     * @param compId id подборки
     */
    void unpinCompilation(Long compId);

    /**
     * Закрепить подборку на главной странице
     *
     * @param compId id подборки
     */
    void pinCompilation(Long compId);
}
