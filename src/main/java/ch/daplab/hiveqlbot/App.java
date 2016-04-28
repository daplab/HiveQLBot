package ch.daplab.hiveqlbot;

import ch.daplab.hiveqlbot.response.Response;
import ch.daplab.hiveqlbot.utils.Context;

import java.io.File;

/**
 * Created by vgrivel on 4/28/16.
 */
public class App {

    public void main(String args[]){

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("configuration.conf").getFile());
        Context context = new Context(file.getAbsolutePath());
        Response response = new Response(context);
    }
}
