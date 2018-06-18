package org.example.stream.impl;


import org.weather.api.ExternalService;
import org.weather.message.api.FileDownloadService;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.api.ServiceInfo;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the StreamService so that it can be served.
 */
public class StreamModule extends AbstractModule implements ServiceGuiceSupport {

  @Override
  protected void configure() {
	  bindServiceInfo(ServiceInfo.of("weatherapi"));
    bindClient(ExternalService.class);
    bindClient(FileDownloadService.class);

    // Bind the subscriber eagerly to ensure it starts up
    bind(StreamSubscriber.class).asEagerSingleton();
  }
}
