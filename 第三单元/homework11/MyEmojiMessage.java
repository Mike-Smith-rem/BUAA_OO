import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;

public class MyEmojiMessage extends MyMessage implements EmojiMessage {
    private int emojiId;

    public MyEmojiMessage(int messageId, int messageSocialValue,
                          Person messagePerson1, Person messagePerson2) {
        super(messageId, messageSocialValue, messagePerson1, messagePerson2);
        this.emojiId = messageSocialValue;
    }

    public MyEmojiMessage(int messageId, int messageSocialValue,
                          Person messagePerson1, Group messageGroup) {
        super(messageId, messageSocialValue, messagePerson1, messageGroup);
        this.emojiId = messageSocialValue;
    }

    @Override
    public int getEmojiId() {
        return emojiId;
    }
}
