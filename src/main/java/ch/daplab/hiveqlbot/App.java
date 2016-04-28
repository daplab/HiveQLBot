package ch.daplab.hiveqlbot;

import ch.daplab.hiveqlbot.hive.Hive;
import ch.daplab.hiveqlbot.response.Response;

import static spark.Spark.post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by vgrivel on 4/28/16.
 */
public class App {

	private static Logger LOG = LoggerFactory.getLogger(App.class);

	public static void main(String args[]){

        Response response = new Response();
        
    	ObjectMapper mapper = new ObjectMapper();
    	Hive hive = new Hive(response);
    	
		post("/", (req, res) -> {
			String query = mapper.readTree(req.body()).at("/item/message/message").asText();

			LOG.info("Woot, getting a request, body is {}", req.body());
			LOG.info("Query is {}", query);

			hive.query(query);
			res.status(200);
			return "Query : " + query;
		});
    }
}
