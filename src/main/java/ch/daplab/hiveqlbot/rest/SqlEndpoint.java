package ch.daplab.hiveqlbot.rest;

import static spark.Spark.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.daplab.hiveqlbot.hive.Hive;

public class SqlEndpoint {
	private static ObjectMapper mapper = new ObjectMapper();
	private static Hive hive = new Hive();

	public static void main(String[] args) {
		post("/sql", (req, res) -> {
			String query = mapper.readValue(req.body(), JsonNode.class).path("message").asText();
			hive.query(query);
			res.status(200);
			return "Query : " + query;
		});
	}
}