package org.example.stream.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;

import akka.Done;

@Singleton
public class StreamRepository {
  private final CassandraSession uninitialisedSession;

  // Will return the session when the Cassandra tables have been successfully created
  private volatile CompletableFuture<CassandraSession> initialisedSession;

  @Inject
  public StreamRepository(CassandraSession uninitialisedSession) {
    this.uninitialisedSession = uninitialisedSession;
    // Eagerly create the session
    session();
  }

  private CompletionStage<CassandraSession> session() {
    // If there's no initialised session, or if the initialised session future completed
    // with an exception, then reinitialise the session and attempt to create the tables
    if (initialisedSession == null || initialisedSession.isCompletedExceptionally()) {
      initialisedSession = uninitialisedSession.executeCreateTable(
          "CREATE TABLE IF NOT EXISTS weatherjsondata (  id timeuuid PRIMARY KEY, data text)"
      ).thenApply(done -> uninitialisedSession).toCompletableFuture();
    }
  
    
    return initialisedSession;
  }

  public CompletionStage<Done> updateMessage(String data) {
    return session().thenCompose(session ->
        session.executeWrite("INSERT INTO weatherjsondata (id , data ) VALUES ( now(),?)",
        		data)
    );
  }

	public CompletionStage<Map> getWeatherdata(String city) {
		return session()
				.thenCompose(session -> session.selectOne(
						"SELECT data FROM weatherjsondata WHERE city = city ALLOW FILTERING", city))
				.thenApply(maybeRow -> maybeRow.map(row -> {
					HashMap hash = new HashMap<String, String>();
					hash.put("city", row.getString("data"));
					//hash.put("WFID", row.getString("wfid"));
					return hash;
				}).orElse(new HashMap() {
					{
						put("city", "DEFAULT");
					//	put("WFID", "CobasWorkflow");
					}
				}));
	}
}
