package es.unizar.es.foodnet;

import com.mongodb.*;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

/**
 * Created by jorge on 20/10/2016.
 */
@Configuration
public class MongoDbConfig extends AbstractMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "foodnet";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new MongoClient("81.33.193.188:8000");
    }
}
