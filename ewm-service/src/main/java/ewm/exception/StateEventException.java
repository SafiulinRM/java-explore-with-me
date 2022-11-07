package ewm.exception;

/**
 * Ошибка нарушения работы с событием
 * в связи с установленными правилами для статуса события
 */
public class StateEventException extends RuntimeException {
    public StateEventException(String s) {
        super(s);
    }
}
