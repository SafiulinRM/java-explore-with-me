package ewm.exception;

/**
 * ошибка отсутствия объекта
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String s) {
        super(s);
    }
}