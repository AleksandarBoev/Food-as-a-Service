package tu.faas.configurations;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tu.faas.domain.beans.Calculation;
import tu.faas.domain.beans.RegionDateTime;
import tu.faas.domain.beans.StringManipulation;

@Configuration
public class BeanConfiguration {
    @Bean
    public ModelMapper getModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RegionDateTime getRegionDateTime() {
        return new RegionDateTime();
    }

    @Bean
    public StringManipulation getStringManipulation() {
        return new StringManipulation();
    }

    @Bean
    public Calculation getCalculation() {
        return new Calculation();
    }
}
