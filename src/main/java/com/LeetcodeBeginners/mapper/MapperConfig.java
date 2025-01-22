package com.LeetcodeBeginners.mapper;

import org.bson.types.ObjectId;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Custom converter for ObjectId to String if needed
        modelMapper.addConverter(new Converter<ObjectId, String>() {
            public String convert(MappingContext<ObjectId, String> context) {
                return context.getSource() == null ? null : context.getSource().toString();
            }
        });

        modelMapper.addConverter(new Converter<String, ObjectId>() {
            public ObjectId convert(MappingContext<String, ObjectId> context) {
                return context.getSource() == null ? null : new ObjectId(context.getSource());
            }
        });

        return modelMapper;
    }
}


