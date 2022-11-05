package ewm.exception;

/**
 * ошибка нарушения работы если пользователь совпадает с инициатором или наоборот
 */
public class UserEventException extends RuntimeException {
    public UserEventException(String s) {
        super(s);
    }
}
