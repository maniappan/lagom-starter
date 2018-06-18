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
 * The FileDownloadService interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the Sample.
 */
public interface FileDownloadService extends Service {

  /**
   * Example: curl http://localhost:9000/api/getweatherfile/filename.gz
   */
  ServiceCall<NotUsed, Done> process(String filename);
  
  ServiceCall<NotUsed, String> getfilestatus(String filename);


  /**
   * Example: curl -H "Content-Type: application/json" -X POST -d '{"message":
   * "Hi"}' http://localhost:9000/api/hello/Alice
   */

  /**
   * This gets published to Kafka.
   */
  Topic<FileDownloadedStatusEvent> fileDownloadEvents();

  @Override
  default Descriptor descriptor() {

    return named("getweatherfile").withCalls(pathCall("/api/getweatherfile/:filename", this::process), pathCall("/api/getfilestatus/:filename", this::getfilestatus))
        .withTopics(topic("file-events", this::fileDownloadEvents)
            // Kafka partitions messages, messages within the same partition will
            // be delivered in order, to ensure that all messages for the same user
            // go to the same partition (and hence are delivered in order with respect
            // to that user), we configure a partition key strategy that extracts the
            // name as the partition key.
            .withProperty(KafkaProperties.partitionKeyStrategy(), FileDownloadedStatusEvent::getFileStatus))
        .withAutoAcl(true).withServiceAcls(ServiceAcl.methodAndPath(Method.OPTIONS, "/api/getweatherfile/.*"));
  }
}
