package ch.daplab.hiveqlbot.response;

import ch.daplab.hiveqlbot.utils.Context;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;

import org.junit.Test;

import java.io.File;
import java.util.concurrent.ExecutionException;

/**
 * Created by vgrivel on 4/28/16.
 */
public class ResponseTest {

    @Test
    public void test() throws ExecutionException, InterruptedException {

        Response resp = new Response();
        resp.hcNotify("I just build correctly the project and pass the tests. YEAAAAH", MessageColor.RANDOM);
    }
}
