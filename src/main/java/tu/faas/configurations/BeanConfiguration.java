package tu.faas.configurations;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tu.faas.domain.beans.SomeClass;

@Configuration
public class BeanConfiguration {
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder().create();
    }

//    @Bean
//    public SomeClass getSomeClass() {
//        return new SomeClass();
//    }
}
