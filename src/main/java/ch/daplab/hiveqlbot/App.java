package ch.daplab.hiveqlbot;

import ch.daplab.hiveqlbot.hive.Hive;
import ch.daplab.hiveqlbot.response.Response;
import ch.daplab.hiveqlbot.utils.Context;

import static spark.Spark.post;

import java.io.File;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by vgrivel on 4/28/16.
 */
public class App {

    public void main(String args[]){

        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("configuration.conf").getFile());
        Context context = new Context(file.getAbsolutePath());
        Response response = new Response(context);
        
    	ObjectMapper mapper = new ObjectMapper();
    	Hive hive = new Hive(response);
    	
		post("/sql", (req, res) -> {
			String query = mapper.readValue(req.body(), JsonNode.class).path("message").asText();
			hive.query(query);
			res.status(200);
			return "Query : " + query;
		});
    }
}
