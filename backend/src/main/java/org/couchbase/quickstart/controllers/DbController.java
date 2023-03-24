package org.couchbase.quickstart.controllers;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.query.QueryOptions;
import com.couchbase.client.java.query.QueryScanConsistency;
import org.couchbase.quickstart.configs.DBProperties;
import org.couchbase.quickstart.models.Temperature;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.couchbase.quickstart.configs.CollectionNames.TEMPERATURE;

@Component

public class DbController {
    private Cluster cluster;
    private Collection temperatureCol;
    private DBProperties dbProperties;
    private Bucket bucket;

    public DbController(Cluster cluster, Bucket bucket, DBProperties dbProperties) {
        System.out.println("Initializing temperature controller, cluster: " + cluster + "; bucket: " + bucket);
        this.cluster = cluster;
        this.bucket = bucket;
        this.temperatureCol = bucket.collection(TEMPERATURE);
        this.dbProperties = dbProperties;
    }

    public void saveEntity(Temperature temperature) {
        temperatureCol.insert(temperature.getId(), temperature);
    }

    public Temperature getEntity(String id) {
        return temperatureCol.get(id.toString()).contentAs(Temperature.class);
    }

    public void updateEntity(String id, Temperature temperature) {
        temperatureCol.upsert(id.toString(), temperature);
    }

    public void deleteEntity(String id) {
        temperatureCol.remove(id);
    }

    public List<Temperature> getEntities(int limit, int skip, String search) {
        String qryString = "SELECT p.* FROM `" + dbProperties.getBucketName() + "`.`_default`.`" + TEMPERATURE + "` p " + "WHERE lower(p.firstName) LIKE '%" + search.toLowerCase() + "%' OR lower(p.lastName) LIKE '%" + search.toLowerCase() + "%'  LIMIT " + limit + " OFFSET " + skip;
        System.out.println("Query=" + qryString);
        //TBD with params: final List<temperature> temperatures = cluster.query("SELECT p.* FROM `$bucketName`.`_default`.`$collectionName` p WHERE lower(p.firstName) LIKE '$search' OR lower(p.lastName) LIKE '$search' LIMIT $limit OFFSET $skip",
        return cluster.query(qryString, QueryOptions.queryOptions().scanConsistency(QueryScanConsistency.REQUEST_PLUS)).rowsAs(Temperature.class);
    }
}