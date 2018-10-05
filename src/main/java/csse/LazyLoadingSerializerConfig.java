package csse;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;

/**
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 10/4/2018.
 */
@Configuration
public class LazyLoadingSerializerConfig {
    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper om = new ObjectMapper();
        final SimpleModule module = new SimpleModule("Item", new Version(1, 0, 0,null));

        module.addSerializer(LazyLoadingProxy.class, new LazyLoadingSerializer());
        om.registerModule(module);

        return om;
    }
}
