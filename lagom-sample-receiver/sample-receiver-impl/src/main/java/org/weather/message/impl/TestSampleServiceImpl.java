package org.weather.message.impl;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weather.api.ExternalService;
import org.weather.message.api.TestSampleMessage;
import org.weather.message.api.TestSampleService;
import org.weather.message.impl.TestSampleEvent.TestSampleMessageChanged;

import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;

/**
 * Implementation of the HelloService.
 */
public class TestSampleServiceImpl implements TestSampleService {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestSampleServiceImpl.class);

  private final PersistentEntityRegistry persistentEntityRegistry;
  private final ExternalService externalService;

  @Inject
  public TestSampleServiceImpl(PersistentEntityRegistry persistentEntityRegistry, ExternalService externalService) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    this.externalService = externalService;
    persistentEntityRegistry.register(TestSampleEntity.class);
    LOGGER.debug("TestSampleServiceImpl started");
  }

  @Override
  public ServiceCall<NotUsed, String> sayHello(String id) {
    //  Temperature
    return request -> {

      return CompletableFuture.completedFuture("TestHello");
    };
  }

  @Override
  public ServiceCall<NotUsed, Done> weatherapi(String city,String state) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<TestSampleCommand> ref = persistentEntityRegistry.refFor(TestSampleEntity.class, city);
      // Tell the entity to use the greeting message specified.

      LOGGER.info("Received request for sample id: {}", city);
     // externalService.updateroche().invoke(
     //     new SampleWorkflowProcessBean(1, 1, "", request.sampleid, OrderStatus.TEST_SAMPLE_RECEIVED.name(), OrderStatus.TEST_SAMPLE_RECEIVED.ordinal(), "",request.labid));

      return ref.ask(new TestSampleCommand.TestSampleMessage(city, state));
    };

  }

  @Override
  public Topic<org.weather.message.api.TestSampleEvent> testSampleEvents() {
    // We want to publish all the shards of the hello event
    return TopicProducer.taggedStreamWithOffset(org.weather.message.impl.TestSampleEvent.TAG.allTags(), (tag, offset) ->

    // Load the event stream for the passed in shard tag
    persistentEntityRegistry.eventStream(tag, offset).map(eventAndOffset -> {

      // Now we want to convert from the persisted event to the published event.
      // Although these two events are currently identical, in future they may
      // change and need to evolve separately, by separating them now we save
      // a lot of potential trouble in future.
      org.weather.message.api.TestSampleEvent eventToPublish;

      if (eventAndOffset.first() instanceof TestSampleMessageChanged) {
        TestSampleMessageChanged messageChanged = (TestSampleMessageChanged) eventAndOffset.first();
        eventToPublish = new org.weather.message.api.TestSampleEvent.TestSampleEventMessage(messageChanged.city, messageChanged.state);

      } else {
        throw new IllegalArgumentException("Unknown event: " + eventAndOffset.first());
      }

      // We return a pair of the translated event, and its offset, so that
      // Lagom can track which offsets have been published.
      return Pair.create(eventToPublish, eventAndOffset.second());
    }));
  }

}
