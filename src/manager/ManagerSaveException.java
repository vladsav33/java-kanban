package manager;

public class ManagerSaveException extends RuntimeException {
    ManagerSaveException() {
        super();
    }

    ManagerSaveException(String message) {
        super(message);
    }
}
