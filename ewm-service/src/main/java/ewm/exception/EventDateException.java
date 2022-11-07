package ewm.exception;

/**
 * ошибка неправильной даты в событии
 */
public class EventDateException extends RuntimeException {
    public EventDateException(String s) {
        super(s);
    }
}
