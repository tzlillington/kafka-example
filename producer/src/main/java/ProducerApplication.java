import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ProducerApplication extends Application<ProducerConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(ProducerApplication.class);

    public static void main(String[] args) throws Exception {
        new ProducerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ProducerConfiguration> bootstrap) {
        super.initialize(bootstrap);

        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
    }

    @Override
    public void run(ProducerConfiguration configuration, Environment environment) throws Exception {
        LOG.info("Started, kafka: {}:{}", configuration.kafkaHost, configuration.kafkaPort);

        Runnable produce = () -> {
           try (TestTopicProducer producer = new TestTopicProducer(configuration.kafkaHost, configuration.kafkaPort)) {
               int i = 0;
               while (true) {
                   String message = "message_" + i;
                   LOG.info("Sending message {}", message);
                   producer.send(message);
                   i++;
                   Thread.sleep(1000);
               }
           } catch (Exception e) {
               LOG.error("Unexpected error", e);
           }
       };

        new Thread(produce).start();
    }
}
