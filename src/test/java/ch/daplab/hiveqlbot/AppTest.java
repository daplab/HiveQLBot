package ch.daplab.hiveqlbot;

import io.evanwong.oss.hipchat.v2.HipChatClient;
import io.evanwong.oss.hipchat.v2.commons.NoContent;
import io.evanwong.oss.hipchat.v2.commons.Request;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import io.evanwong.oss.hipchat.v2.rooms.SendRoomNotificationRequestBuilder;
import org.junit.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by vgrivel on 4/26/16.
 */
public class AppTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {


        String defaultAccessToken = "yfpqpx0UksMn5qndkIHXz4VD9esEHfs8jix9oYY0";
        HipChatClient client = new HipChatClient(defaultAccessToken);
        SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder("HackyThursday", "Hi Dorian");
        Future<NoContent> future = builder.setColor(MessageColor.PURPLE).setNotify(true).build().execute();

//optional... if you want/need to inspect the result:
        NoContent noContent = future.get();
    }



}
