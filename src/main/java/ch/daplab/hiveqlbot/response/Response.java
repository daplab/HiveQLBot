package ch.daplab.hiveqlbot.response;

import ch.daplab.hiveqlbot.utils.Context;
import io.evanwong.oss.hipchat.v2.HipChatClient;
import io.evanwong.oss.hipchat.v2.commons.NoContent;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import io.evanwong.oss.hipchat.v2.rooms.SendRoomNotificationRequestBuilder;

import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by vgrivel on 4/28/16.
 */
public class Response {

    private String apitoken;

    public Response(){

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("application.conf");
        Context context = new Context(is);
        
        this.apitoken = context.getString("apitoken");
    }



    public void hcNotify(String resp, MessageColor color){

        String defaultAccessToken = apitoken;
        HipChatClient client = new HipChatClient(defaultAccessToken);
        SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder("HackyThursday", resp);
        Future<NoContent> future = builder.setColor(color).setNotify(true).build().execute();

        try {
            future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }

}
