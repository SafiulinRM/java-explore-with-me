package ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Обработчик ошибок
 */
@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    /**
     * обработка EventDateException
     *
     * @param e {@link EventDateException}
     * @return {@link ApiError}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEventDateException(final EventDateException e) {
        log.warn("Не корректная дата. " + e.getMessage());
        return new ApiError(null,
                e.getMessage(),
                "Не корректная дата",
                "BAD_REQUEST");
    }

    /**
     * обработка MissingRequestValueException
     * исключение на пустой параметр контроллера
     *
     * @param e {@link EventDateException}
     * @return {@link ApiError}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingRequestValueException(final MissingRequestValueException e) {
        log.warn("Пустой параметр. " + e.getMessage());
        return new ApiError(null,
                e.getMessage(),
                "Пустой параметр",
                "BAD_REQUEST");
    }

    /**
     * обработка NotFoundException
     *
     * @param e {@link NotFoundException}
     * @return {@link ApiError}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        log.warn("Искомый объект не найден. " + e.getMessage());
        return new ApiError(null,
                e.getMessage(),
                "Искомый объект не найден",
                "NOT_FOUND"
        );
    }

    /**
     * обработка Throwable
     *
     * @param e {@link Throwable}
     * @return {@link ApiError}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        log.warn("Неизвестная ошибка. " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Неизвестная ошибка",
                "INTERNAL_SERVER_ERROR"
        );
    }

    /**
     * обработка MethodArgumentNotValidException
     *
     * @param e {@link MethodArgumentNotValidException}
     * @return {@link ApiError}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        log.warn("Неправильные поля у объекта. " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                e.getMessage(),
                "Неправильные поля у объекта",
                "BAD_REQUEST");
    }

    /**
     * обработка StateException
     *
     * @param e {@link StateEventException}
     * @return {@link ApiError}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleStateException(final StateEventException e) {
        log.warn("Не корректный статус. " + e.getMessage());
        return new ApiError(null,
                e.getMessage(),
                "Не корректный статус",
                "BAD_REQUEST"
        );
    }

    /**
     * обработка UserEventException
     *
     * @param e {@link UserEventException}
     * @return {@link ApiError}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleUserEventException(final UserEventException e) {
        log.warn("Не корректный id юзера. " + e.getMessage());
        return new ApiError(null,
                e.getMessage(),
                "Не корректный id юзера",
                "BAD_REQUEST"
        );
    }

    /**
     * обработка RequestException
     *
     * @param e {@link RequestException}
     * @return {@link ApiError}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleRequestException(final RequestException e) {
        log.warn("Неправильный запрос на участие. " + e.getMessage());
        return new ApiError(null,
                e.getMessage(),
                "Неправильный запрос на участие",
                "BAD_REQUEST"
        );
    }
}