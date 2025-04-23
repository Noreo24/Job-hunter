package vn.noreo.jobhunter.service.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import vn.noreo.jobhunter.domain.RestResponse;

@RestControllerAdvice
public class GlobalException {

    // Ném exception cho user khi id không tồn tại
    @ExceptionHandler(value = IdInvalidExeption.class)
    public ResponseEntity<RestResponse<Object>> handleIdInvalidExeption(IdInvalidExeption idInvalidExeption) {
        RestResponse<Object> restResponse = new RestResponse<Object>();
        restResponse.setStatusCode(HttpStatus.BAD_REQUEST.value());
        restResponse.setError(idInvalidExeption.getMessage());
        restResponse.setMessage("Invalid id");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(restResponse);
    }
}
