package org.weather.message.impl;

import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.stream.weather.UnzipGZutil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.weather.api.ExternalService;
import org.weather.message.api.FileDownloadService;
import org.weather.message.impl.DownloadFileCommand.GetFileStatus;

import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import akka.util.ByteString;

/**
 * Implementation of the HelloService.
 */
public class FileDownloadServiceImpl implements FileDownloadService {

  private static final Logger LOGGER = LoggerFactory.getLogger(FileDownloadServiceImpl.class);

  private final PersistentEntityRegistry persistentEntityRegistry;
  private final ExternalService externalService;
  



  @Inject
  public FileDownloadServiceImpl(PersistentEntityRegistry persistentEntityRegistry,ExternalService externalService) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    this.externalService = externalService;

    persistentEntityRegistry.register(FileDownloadEntity.class);
    LOGGER.info("TestSampleServiceImpl started");
  }

  @Override
  public ServiceCall<NotUsed, String> getfilestatus(String filename) {
    return request -> {        
        // Look up the hello world entity for the given ID.
        PersistentEntityRef<DownloadFileCommand> ref = persistentEntityRegistry.refFor(FileDownloadEntity.class, filename);
        // Ask the entity the Hello command.
        return ref.ask(new GetFileStatus(filename));
    };
  }

  @Override
  public ServiceCall<NotUsed, Done> process(String filename) {
		return request -> {
			PersistentEntityRef<DownloadFileCommand> ref = persistentEntityRegistry.refFor(FileDownloadEntity.class,filename);

			LOGGER.info("Received request for Filename: {}", filename);
			LOGGER.info("Received request for Filename: {}", filename.split("\\.")[0]);


			String data = null;
			ByteString byteString =null;
			try {
				byteString = externalService.getWeather(filename).invoke().toCompletableFuture().get(15, TimeUnit.SECONDS);
				final Path file = (Path) Paths.get("target/"+filename.split("\\.")[0]+".gz");

				
				//http://localhost:9000/api/getweatherfile/weather_14.json.gz
				//http://bulk.openweathermap.org/sample/

				// Files.write(Paths.get("C:\\Users\\sundaramoorthim\\Desktop\\junkfiles.gz"),
				// bytes.iterator().getByte());
				// bytes.copyToBuffer(dest);
				ByteChannel byteChannel = Files.newByteChannel(file, StandardOpenOption.CREATE_NEW,
						StandardOpenOption.WRITE);
				byteChannel.write(byteString.toByteBuffer());
				byteChannel.close();

				

				data = UnzipGZutil.gunzipIt("target/"+filename.split("\\.")[0]+".gz", "target/"+filename.split("\\.")[0]+".txt");
				LOGGER.info(data);

			} catch (InterruptedException e) {
				LOGGER.error("Exception : " + ExceptionUtils.getStackTrace(e));
				data = null;
			} catch (ExecutionException e) {
				LOGGER.error("Exception : " + ExceptionUtils.getStackTrace(e));
				data = null;
				
			}catch (Exception e) {
				LOGGER.error("Exception : " + ExceptionUtils.getStackTrace(e));
				data = null;
				
			}

			/*
			 * String fileName = "target/weather2.txt";
			 * 
			 * //read file into stream, try-with-resources try (Stream<String> stream =
			 * Files.lines(Paths.get(fileName))) {
			 * 
			 * stream.forEach(System.out::println);
			 * 
			 * } catch (IOException e) { e.printStackTrace(); }
			 */
			
		//	CompletionStage<Done> ret =null;
			if(data==null)
			{
				return ref.ask(new DownloadFileCommand.DownloadFile("INVALID FILE"));
			}
			else
			{
				return ref.ask(new DownloadFileCommand.DownloadFile("DOWNLOADED"));
			}

			
			//return ref.ask(new DownloadFileCommand.DownloadFile("DOWNLOADED"));
		};

  }

  @Override
  public Topic<org.weather.message.api.FileDownloadedStatusEvent> fileDownloadEvents() {
    // We want to publish all the shards of the hello event
    return TopicProducer.taggedStreamWithOffset(FileDownloadedStatusEvent.TAG.allTags(), (tag, offset) ->

    // Load the event stream for the passed in shard tag
    persistentEntityRegistry.eventStream(tag, offset).map(eventAndOffset -> {

      // Now we want to convert from the persisted event to the published event.
      // Although these two events are currently identical, in future they may
      // change and need to evolve separately, by separating them now we save
      // a lot of potential trouble in future.
      org.weather.message.api.FileDownloadedStatusEvent eventToPublish;

      if (eventAndOffset.first() instanceof FileDownloadedStatusEvent.FileDownloadedStatusChanged) {
    	  FileDownloadedStatusEvent.FileDownloadedStatusChanged messageChanged = (FileDownloadedStatusEvent.FileDownloadedStatusChanged) eventAndOffset.first();
        eventToPublish = new org.weather.message.api.FileDownloadedStatusEvent.FileDownloadedStatusChanged(messageChanged.filename,messageChanged.filestatus);

      } else {
        throw new IllegalArgumentException("Unknown event: " + eventAndOffset.first());
      }

      // We return a pair of the translated event, and its offset, so that
      // Lagom can track which offsets have been published.
      return Pair.create(eventToPublish, eventAndOffset.second());
    }));
  }

}
