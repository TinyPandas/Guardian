package main.database;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class DBManager {
	private static MongoClient mongoClient;
	
	public DBManager() {
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static MongoClient getServer() {
		if (mongoClient == null) new DBManager();
		return mongoClient;
	}
	
	public static DB getDB(String database) {
		getServer();
		return mongoClient.getDB(database);
	}
	
	public static DBCollection getCollection(String dbName, String collection) {
		return getCollection(getDB(dbName), collection);
	}
	
	public static DBCollection getCollection(DB database, String collection) {
		return database.getCollection(collection);
	}
	
	public static long getCollectionSize(String dbName, String collection) {
		return getCollection(dbName, collection).count();
	}
	
	public static DBCollection addDocument(String dbName, String collection, DBObject object) {
		DBCollection col = getCollection(dbName, collection);
		col.insert(object);
		return col;
	}
}
