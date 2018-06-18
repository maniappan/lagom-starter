package org.example.stream.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import javax.inject.Inject;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weather.api.ExternalService;
import org.weather.message.api.FileConstants;
import org.weather.message.api.FileDownloadService;

import akka.Done;
import akka.stream.javadsl.Flow;

/**
 * This subscribes to the HelloService event stream.
 */
public class StreamSubscriber {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamSubscriber.class);

	@Inject
	public StreamSubscriber(FileDownloadService testSampleService, ExternalService externalService,
			StreamRepository repository) {
		// Create a subscriber
		testSampleService.fileDownloadEvents().subscribe()
				// And subscribe to it with at least once processing semantics.
				.atLeastOnce(
						// Create a flow that emits a Done for each message it processes
						Flow.<org.weather.message.api.FileDownloadedStatusEvent>create().mapAsync(1, event -> {

							if (event instanceof org.weather.message.api.FileDownloadedStatusEvent) {
								org.weather.message.api.FileDownloadedStatusEvent.FileDownloadedStatusChanged messageChanged = (org.weather.message.api.FileDownloadedStatusEvent.FileDownloadedStatusChanged) event;
								// Update the message
								LOGGER.info(messageChanged.toString() + "  Event  ::: " + messageChanged
										+ "   Event Received ::: " + LocalDateTime.now().toString());

								if (messageChanged.filestatus.equals(FileConstants.DOWNLOADED)) {
									String fileName = "target/" + messageChanged.filename.split("\\.")[0] + ".txt";

									// read file into stream, try-with-resources
									try (Stream<String> stream = Files.lines(Paths.get(fileName))) {

										stream.forEach(repository::updateMessage);

									} catch (IOException e) {
										LOGGER.error("Exception : " + ExceptionUtils.getStackTrace(e));
									}
									LOGGER.info("Completed upload ..................................");
								}
								return CompletableFuture.completedFuture(Done.getInstance());

							} else {
								// Ignore all other events
								return CompletableFuture.completedFuture(Done.getInstance());
							}
						}));

	}

}
