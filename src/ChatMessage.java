import java.io.*;

public class ChatMessage implements Serializable {

    static final int USERSCONNECTED = 0, MESSAGE = 1, LOGOUT = 2, TERMINATE = 3, SEND = 4;
    private int type;
    private String chatMsg;

    ChatMessage(int type, String chatMsg) {
        this.type = type;
        this.chatMsg = chatMsg;
    }
    int getType() {
       return type;
    }
    String getMessage() {
        return chatMsg;
    }
}
