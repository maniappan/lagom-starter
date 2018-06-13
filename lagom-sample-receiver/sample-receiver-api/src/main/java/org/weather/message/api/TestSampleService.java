package org.weather.message.api;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;
import static com.lightbend.lagom.javadsl.api.Service.topic;

import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceAcl;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;
import com.lightbend.lagom.javadsl.api.transport.Method;

import akka.Done;
import akka.NotUsed;

/**
 * The TestSampleService interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the Sample.
 */
public interface TestSampleService extends Service {

  /**
   * Example: curl http://localhost:9000/api/hello/Alice
   */
  ServiceCall<NotUsed, Done> weatherapi(String city,String state);
  
  ServiceCall<NotUsed, String> sayHello(String id);


  /**
   * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
   * "Hi"}' http://localhost:9000/api/hello/Alice
   */

  /**
   * This gets published to Kafka.
   */
  Topic<TestSampleEvent> testSampleEvents();

  @Override
  default Descriptor descriptor() {

    return named("getweather").withCalls(pathCall("/api/getweather/:city/:state", this::weatherapi), pathCall("/api/sayHello/:id", this::sayHello))
        .withTopics(topic("weather-events", this::testSampleEvents)
            // Kafka partitions messages, messages within the same partition will
            // be delivered in order, to ensure that all messages for the same user
            // go to the same partition (and hence are delivered in order with respect
            // to that user), we configure a partition key strategy that extracts the
            // name as the partition key.
            .withProperty(KafkaProperties.partitionKeyStrategy(), TestSampleEvent::getcity))
        .withAutoAcl(true).withServiceAcls(ServiceAcl.methodAndPath(Method.OPTIONS, "/api/getweather/.*"));
  }
}
