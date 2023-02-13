package http;

public enum HttpCode {
    OK(200),
    NO_CONTENT(204),
    BAD_REQUEST(400),
    FORBIDDEN(403),
    NOT_FOUND(404),
    NOT_ALLOWED(405);
    private int code;
    HttpCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }
}
