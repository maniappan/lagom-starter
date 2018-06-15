package org.weather.message.impl;

import org.weather.api.ExternalService;
import org.weather.message.api.FileDownloadService;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

/**
 * The module that binds the HelloService so that it can be served.
 */
public class FileDownloadModule extends AbstractModule implements ServiceGuiceSupport {

  @Override
  protected void configure() {
    bindService(FileDownloadService.class, FileDownloadServiceImpl.class);
    bindClient(ExternalService.class);
  }
}
