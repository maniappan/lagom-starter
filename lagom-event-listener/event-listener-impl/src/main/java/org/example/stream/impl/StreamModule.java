package org.example.stream.impl;


import org.weather.api.ExternalService;
import org.weather.message.api.TestSampleService;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.api.ServiceInfo;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the StreamService so that it can be served.
 */
public class StreamModule extends AbstractModule implements ServiceGuiceSupport {

  @Override
  protected void configure() {
    bindServiceInfo(ServiceInfo.of("querylis"));
    // Bind the StreamService service
    // bindService(StreamService.class, StreamServiceImpl.class);
    // Bind the HelloService client
   // bindClient(PlannerService.class);
    bindClient(ExternalService.class);
    bindClient(TestSampleService.class);

    // Bind the subscriber eagerly to ensure it starts up
    bind(StreamSubscriber.class).asEagerSingleton();
  }
}
