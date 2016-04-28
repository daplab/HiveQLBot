package ch.daplab.hiveqlbot.hive;

import ch.daplab.hiveqlbot.response.Response;
import io.evanwong.oss.hipchat.v2.rooms.MessageColor;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * Created by vgrivel on 4/28/16.
 */
public class Hive {

    private static final String SELECT = "/SQL SELECT";

    private static Logger LOG = LoggerFactory.getLogger(Hive.class);


    private final Response response;
    private final Configuration conf;
    private final DataSource dataSource;


    public Hive(Response response) {

        this.response = response;
        this.conf = new Configuration();

        try {
            this.dataSource = HiveJDBCHelper.getDataSource(this.conf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void query(String query) {

        LOG.info("Woot, getting a query!! {}", query);

//        Query sanitization

        if (query == null) {
            return;
        }

        query = query.trim().toUpperCase();

        if (!query.startsWith(SELECT)) {
            return;
        }

        query = query.substring(5);


        MessageColor color = MessageColor.GREEN;
        String message = "";

        try (Connection connection = dataSource.getConnection(); Statement stmt = connection.createStatement()) {

            StringBuilder sb = new StringBuilder();

            UUID uuid = UUID.randomUUID();
            Path p = new Path("/tmp", uuid.toString());

            sb.append("INSERT OVERWRITE DIRECTORY '" + p.toUri().getPath() + "' ")
                    .append("ROW FORMAT DELIMITED ")
                    .append("FIELDS TERMINATED BY '\t' ")
                    .append("STORED AS TEXTFILE ")
                    .append(query);

            String queryStatement = sb.toString();

            LOG.info("Executing {}", queryStatement);
            boolean b = stmt.execute(queryStatement);

            if (b) {
                color = MessageColor.GREEN;
                message = "Query executed successfully, output stored into " + p.toString() + ", query was '" + query + "'";
            } else {
                color = MessageColor.YELLOW;
                message = "Ouups, something wrong happened, not sure why (no exception raised...), query was '" + query + "'";
            }

        } catch (SQLException e) {
            LOG.warn("Got an SQLException", e);
            color = MessageColor.RED;
            message = "Query failed with message " + e.getMessage() + ", query was '" + query + "'";

        }

        response.hcNotify(message, color);

    }
}