package org.weather.message.impl;

import org.weather.api.ExternalService;
import org.weather.message.api.TestSampleService;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class TestSampleModule extends AbstractModule implements ServiceGuiceSupport {

  @Override
  protected void configure() {
    bindService(TestSampleService.class, TestSampleServiceImpl.class);
    bindClient(ExternalService.class);
  }
}
