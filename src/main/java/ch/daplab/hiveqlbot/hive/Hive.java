package ch.daplab.hiveqlbot.hive;

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

    private static final String SELECT = "SELECT";

    private static Logger LOG = LoggerFactory.getLogger(Hive.class);


    private final Configuration conf;
    private final DataSource dataSource;

    public Hive() {
        this(new Configuration());
    }

    public Hive(Configuration conf) {
        this.conf = conf;

        try {
            this.dataSource = HiveJDBCHelper.getDataSource(this.conf);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void query(String query) {

//        Query sanitization

        if (query == null) {
            return;
        }

        query = query.trim().toUpperCase();

        if (!query.startsWith(SELECT)) {
            return;
        }

        String color = "GREEN";
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
                color = "GREEN";
                message = "Query executed successfully, output stored into " + p.toString() + ", query was '" + query + "'";
            } else {
                color = "YELLOW";
                message = "Ouups, something wrong happened, not sure why (no exception raised...), query was '" + query + "'";
            }

        } catch (SQLException e) {
            LOG.warn("Got an SQLException", e);
            color = "RED";
            message = "Query failed with message " + e.getMessage() + ", query was '" + query + "'";

        }


    }
}