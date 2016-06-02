package ch.daplab.hiveqlbot;

import ch.daplab.hiveqlbot.hive.Hive;
import ch.daplab.hiveqlbot.response.Response;

import static spark.Spark.post;

import com.codahale.metrics.CsvReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by vgrivel on 4/28/16.
 */
public class App {

	private static Logger LOG = LoggerFactory.getLogger(App.class);

	public static MetricRegistry registry = new MetricRegistry();


	public static void main(String args[]){

    Response response = new Response();
        
    ObjectMapper mapper = new ObjectMapper();
    Hive hive = new Hive(response);

		startReporter();

		Meter request = registry.meter("hivebot.requestPerSec");
		final Timer timer = registry.timer("hivebot.latency");

		post("/query", (req, res) -> {

			final Timer.Context context = timer.time();

			request.mark();

			String query = mapper.readTree(req.body()).at("/item/message/message").asText();

			LOG.info("Woot, getting a request, body is {}", req.body());
			LOG.info("Query is {}", query);

			hive.query(query);
			res.status(200);

			context.stop();

			return "Query : " + query;
		});
  }

	private static void startReporter() {
		CsvReporter reporter = CsvReporter.forRegistry(registry)
						.convertRatesTo(TimeUnit.SECONDS)
						.convertDurationsTo(TimeUnit.MILLISECONDS)
						.build(new File("/tmp/metrics.csv"));
		reporter.start(1, TimeUnit.SECONDS);
	}
}
