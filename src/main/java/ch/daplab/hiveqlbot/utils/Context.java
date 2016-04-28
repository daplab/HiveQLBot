package ch.daplab.hiveqlbot.utils;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by vgrivel on 4/28/16.
 */
public class Context {

    private static final Logger LOG = LoggerFactory.getLogger(Context.class);
    Properties prop;

    public Context(InputStream is) {
        prop = new Properties();
        try {
            prop.load(is);
        } catch (FileNotFoundException e) {
            LOG.error("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            LOG.error("IO Exception");
            e.printStackTrace();
        }

    }

    public String getString(String key) {
        return prop.getProperty(key);
    }

}