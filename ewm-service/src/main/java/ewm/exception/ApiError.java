package ewm.exception;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Класс информации об ошибке, содержит поля:
 * {@link ApiError#message},
 * {@link ApiError#reason},
 * {@link ApiError#status},
 * {@link ApiError#timestamp},
 * {@link ApiError#errors},
 */
@Getter
public class ApiError {
    /**
     * Стек-трейс ошибок
     */
    private final Object errors;
    /**
     * Сообщение ошибки
     */
    private final String message;
    /**
     * Общая причина
     */
    private final String reason;
    /**
     * HTTP статус
     */
    private final String status;
    /**
     * Дата и время возникновения
     */
    private final LocalDateTime timestamp = LocalDateTime.now();

    /**
     * Конструктор класса {@link ApiError}
     *
     * @param errors  Стек-трейс ошибок
     * @param message Сообщение ошибки
     * @param reason  HTTP статус
     * @param status  Дата и время возникновения
     */
    public ApiError(Object errors, String message, String reason, String status) {
        this.errors = errors;
        this.message = message;
        this.reason = reason;
        this.status = status;
    }
}
