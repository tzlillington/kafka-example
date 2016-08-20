import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumerApplication extends Application<ConsumerConfiguration> {

    private static final Logger LOG = LoggerFactory.getLogger(ConsumerApplication.class);

    public static void main(String[] args) throws Exception {
        new ConsumerApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ConsumerConfiguration> bootstrap) {
        super.initialize(bootstrap);

        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
    }

    @Override
    public void run(ConsumerConfiguration configuration, Environment environment) throws Exception {
        LOG.info("Started, kafka: {}:{}", configuration.kafkaHost, configuration.kafkaPort);

        Runnable consume = () -> {
            try (TestTopicConsumer consumer = new TestTopicConsumer(configuration.kafkaHost, configuration.kafkaPort)) {
                while (true) {
                    consumer.receiveBatch().forEach(message ->
                            LOG.info("Received message {}", message)
                    );
                    consumer.commit();
                }
            } catch (Exception e) {
                LOG.error("Unexpected error", e);
            }
        };

        new Thread(consume).start();
    }
}
