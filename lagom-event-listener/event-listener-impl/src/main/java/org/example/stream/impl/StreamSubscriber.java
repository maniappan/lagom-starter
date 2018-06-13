package org.example.stream.impl;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weather.api.ExternalService;
import org.weather.message.api.TestSampleService;

import com.lightbend.lagom.javadsl.api.ServiceCall;

import akka.Done;
import akka.NotUsed;
import akka.stream.javadsl.Flow;

/**
 * This subscribes to the HelloService event stream.
 */
public class StreamSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamSubscriber.class);

	@Inject
	public StreamSubscriber(TestSampleService testSampleService, ExternalService externalService,
			StreamRepository repository) {
		// Create a subscriber
		testSampleService.testSampleEvents().subscribe()
				// And subscribe to it with at least once processing semantics.
	      .atLeastOnce(
	    	        // Create a flow that emits a Done for each message it processes
	    	        Flow.<org.weather.message.api.TestSampleEvent>create().mapAsync(1, event -> {

	    	          if (event instanceof org.weather.message.api.TestSampleEvent) {
	    	        	  org.weather.message.api.TestSampleEvent.TestSampleEventMessage messageChanged = (org.weather.message.api.TestSampleEvent.TestSampleEventMessage) event;
	    	            // Update the message
	    	            System.out.println(messageChanged.toString() + "  Event  ::: " +messageChanged +"   Event Received ::: "+LocalDateTime.now().toString());
	    	            	
	    	           String data= externalService.getWeather(messageChanged.city, messageChanged.state).invoke().toCompletableFuture().get();
	    	           System.out.println("-----------------------------------------------------------");

	    	           System.out.println(data);
	    	            return CompletableFuture.completedFuture(Done.getInstance());


	    	          } else {
	    	            // Ignore all other events
	    	            return CompletableFuture.completedFuture(Done.getInstance());
	    	          }
	    	        })
	    	      );

	}

}
