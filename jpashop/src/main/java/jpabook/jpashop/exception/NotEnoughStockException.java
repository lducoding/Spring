package jpabook.jpashop.exception;

import org.json.simple.JSONObject;

public class NotEnoughStockException extends RuntimeException{

    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public JSONObject NotEnoughStockException(String message) {
        JSONObject jsonObject = new JSONObject();
        return jsonObject;
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

}
