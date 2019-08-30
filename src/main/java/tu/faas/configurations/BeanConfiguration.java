package tu.faas.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tu.faas.domain.beans.StringManipulation;

@Configuration
public class BeanConfiguration {
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public StringManipulation getStringManipulation() {
        return new StringManipulation();
    }
}
