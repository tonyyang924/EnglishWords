package tw.tonyyang.englishwords;

/**
 * Created by tonyyang on 2017/6/19.
 */

public class RealTimeUpdateEvent {
    public enum Type {
        UPDATE_WORD_LIST,
    }

    private Type type;

    private String message;

    public RealTimeUpdateEvent(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
