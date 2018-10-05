package csse;

        import com.fasterxml.jackson.core.JsonGenerator;
        import com.fasterxml.jackson.core.JsonProcessingException;
        import com.fasterxml.jackson.databind.JsonSerializer;
        import com.fasterxml.jackson.databind.SerializerProvider;
        import org.springframework.data.mongodb.core.convert.LazyLoadingProxy;

        import java.io.IOException;

/**
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 10/4/2018.
 */
public class LazyLoadingSerializer extends JsonSerializer<LazyLoadingProxy> {

    @Override
    public void serialize(LazyLoadingProxy value, JsonGenerator jgen,
                          SerializerProvider provider) throws IOException,
            JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeStringField("id", value.toDBRef().getId().toString());
        jgen.writeEndObject();
    }
}
