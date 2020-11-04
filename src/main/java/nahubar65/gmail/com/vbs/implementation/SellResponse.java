package nahubar65.gmail.com.vbs.implementation;

import nahubar65.gmail.com.backpacksystem.core.message.Message;

public abstract class SellResponse {

    private final ResponseType responseType;

    public SellResponse(ResponseType responseType) {
        this.responseType = responseType;
    }

    public abstract Message<?> getMessage();

    public ResponseType getResponse() {
        return responseType;
    }

    public enum ResponseType {
        SUCCESS,
        FAILED;
    }
}