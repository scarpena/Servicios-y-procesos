package org.example;


import java.util.HashMap;

public class ResponseBody {

    private final HashMap<String, Object> args;
    private final String data;
    private final HashMap<String, Object> headers;
    private final String origin;

    public ResponseBody(HashMap<String, Object> args) {
        this.args = args;
        this.data = (String) args.get("data");
        this.headers = (HashMap<String, Object>) args.get("headers");
        this.origin = (String) args.get("origin");
    }
    public HashMap<String, Object> getArgs() {
        return args;
    }

    public String getData() {
        return data;
    }

    public HashMap<String, Object> getHeaders() {
        return headers;
    }

    public String getOrigin() {
        return origin;
    }
}
