package SenderMain;

public class Command {

    private String method;
    private String data;


    public Command(String method, String data) {

        this.method = method;
        this.data = data;

    }

    public String build() {

        return "{\"method\":\"" + method + "\"," + data + "}";
    }

    public String shortBuild() {

        return "{\"" + method + "\":" + data + "}";
    }
}
