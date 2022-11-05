package ewm.exception;

/**
 * Ошибка нарушения заданности полей запроса на участие в событии
 * в связи с установленными правилами для запроса
 */
public class RequestException extends RuntimeException {
    public RequestException(String s) {
        super(s);
    }
}
