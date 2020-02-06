package main.database;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class DBManager {
	private static DBManager instance = null;
	private MongoClient mongoClient;

	private DBManager() {
		try {
			mongoClient = new MongoClient();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	public static DBManager getInstance() {
		if (instance == null)
			instance = new DBManager();

		return instance;
	}

	public DB getDB(String database) {
		return mongoClient.getDB(database);
	}
	
	public DBCollection getCollection(String dbName, String collection) {
		return getDB(dbName).getCollection(collection);
	}

	public DBCollection addDocument(String dbName, String collection, DBObject object) {
		DBCollection col = getCollection(dbName, collection);
		col.insert(object);
		return col;
	}
}
