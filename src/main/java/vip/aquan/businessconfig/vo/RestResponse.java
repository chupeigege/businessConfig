package vip.aquan.businessconfig.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestResponse<T> {
    private static final String SUCCESS_MESSAGE = "Succeeded";
    private static final String FAIL_MESSAGE = "Failed";

    private static final String CODE_OK = "200";
    private static final String CODE_ERROR = "Error";

    private String code;
    private String message;
    private T data;

    public RestResponse(String message, T data) {
        this.code = CODE_OK;
        this.message = message;
        this.data = data;
    }

    public RestResponse(T data) {
        this.code = CODE_OK;
        this.message = SUCCESS_MESSAGE;
        this.data = data;
    }

    public static RestResponse<Object> ok() {
        RestResponse<Object> okResponse = new RestResponse<Object>(CODE_OK, SUCCESS_MESSAGE, null);
        return okResponse;
    }

    public static RestResponse<Object> ok(String message) {
        RestResponse<Object> okResponse = new RestResponse<Object>(CODE_OK, message, null);
        return okResponse;
    }

    public static RestResponse<Object> fail(String message) {
        RestResponse<Object> okResponse = new RestResponse<Object>(CODE_ERROR, message, null);
        return okResponse;
    }

    public ResponseEntity<RestResponse<T>> success() {
        return success(SUCCESS_MESSAGE);
    }

    public ResponseEntity<RestResponse<T>> success(String message) {
        this.setCode(CODE_OK);
        this.setMessage(message);
        return new ResponseEntity<>(this, HttpStatus.OK);
    }

    public String getCode() {
        return code;
    }

    public RestResponse<T> setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RestResponse<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public T getData() {
        return data;
    }

    public RestResponse<T> setData(T data) {
        this.data = data;
        return this;
    }
}
