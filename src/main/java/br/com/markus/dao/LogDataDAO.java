package br.com.markus.dao;

import br.com.markus.converter.LogDataConverter;
import br.com.markus.enuns.LogTypeEnum;
import br.com.markus.model.LogData;
import br.com.markus.model.LogDataQuery;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.apache.commons.lang3.StringUtils;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Classe responsável por "persistir" solicitações de pagamento
 *
 * @author Markus Kopinits
 */
@Component
public class LogDataDAO {

    private static final String LOGDATA_COLLECTION_NAME = "logdata";
    @Autowired
    private MongoConnection mongoConnection;
    @Autowired
    private LogDataConverter converter;


    public void saveLogData(LogData logData) throws UnknownHostException {
        MongoCollection collection = getMongoCollection();
        collection.insertOne(converter.toBasicObject(logData));
    }


    public ArrayList<LogData> queryLogData(LogDataQuery logDataQuery) throws UnknownHostException {
        MongoCursor<Document> mongoCursor = findLogDatas(logDataQuery);
        return getResults(mongoCursor);
    }

    private ArrayList<LogData> getResults(MongoCursor<Document> mongoCursor) {
        ArrayList<LogData> result = new ArrayList<LogData>();
        while (mongoCursor.hasNext()) {
            Document doc = mongoCursor.next();
            LogData logData = new LogData();
            logData.setAppCode(doc.get(LogData.APP_CODE).toString());
            logData.setDataLogged(doc.get(LogData.DATA_LOGGED).toString());
            logData.setLogType(LogTypeEnum.from(doc.get(LogData.LOG_TYPE).toString()));
            logData.setTimestamp(new Date(Long.valueOf(doc.get(LogData.TIMESTAMP).toString())));
            if (doc.get(LogData.CUSTUMER_ID) != null) {
                logData.setCustumerID(doc.get(LogData.CUSTUMER_ID).toString());
            }
            result.add(logData);
        }
        return result;
    }

    private MongoCursor<Document> findLogDatas(LogDataQuery logDataQuery) throws UnknownHostException {
        MongoCollection collection = getMongoCollection();
        BasicDBObject query = new BasicDBObject();
        query.put(LogData.LOG_TYPE, new BasicDBObject("$eq", logDataQuery.getLogType().getDescription()));
        query.put(LogData.TIMESTAMP, new BasicDBObject("$gte", logDataQuery.getTimestampFrom().getTime()));
        query.put(LogData.TIMESTAMP, new BasicDBObject("$lte", logDataQuery.getTimestampTo().getTime()));
        if (StringUtils.isNotBlank(logDataQuery.getCustumerID())) {
            query.put(LogData.CUSTUMER_ID, new BasicDBObject("$eq", logDataQuery.getCustumerID()));
        }
        if (StringUtils.isNotBlank(logDataQuery.getAppCode())) {
            query.put(LogData.APP_CODE, new BasicDBObject("$eq", logDataQuery.getAppCode()));
        }
        BasicDBObject orderBy = new BasicDBObject(LogData.TIMESTAMP, 1);
        return collection.find(query).sort(orderBy).iterator();
    }

    private MongoCollection getMongoCollection() {
        MongoClient mongoClient = mongoConnection.getConnection();
        MongoDatabase mongoDatabase = mongoConnection.getMongoDatabase(mongoClient);
        return mongoDatabase.getCollection(LOGDATA_COLLECTION_NAME);
    }
}
