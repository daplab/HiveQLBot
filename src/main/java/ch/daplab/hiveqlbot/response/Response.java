package ch.daplab.hiveqlbot.response;

import ch.daplab.hiveqlbot.utils.Context;
import io.evanwong.oss.hipchat.v2.HipChatClient;
import io.evanwong.oss.hipchat.v2.commons.NoContent;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import io.evanwong.oss.hipchat.v2.rooms.SendRoomNotificationRequestBuilder;

import java.util.concurrent.Future;

/**
 * Created by vgrivel on 4/28/16.
 */
public class Response {

    private String apitoken;

    public Response(Context context){
        this.apitoken = context.getString("apitoken");
    }



    public void hcNotify(String hiveRes, String hdfsPath){

        String defaultAccessToken = apitoken;
        HipChatClient client = new HipChatClient(defaultAccessToken);
        SendRoomNotificationRequestBuilder builder = client.prepareSendRoomNotificationRequestBuilder("HackyThursday", "The result of the query is store in: " +hdfsPath);
        Future<NoContent> future = builder.setColor(MessageColor.PURPLE).setNotify(true).build().execute();


    }

}
