package com.gabriela.fabricadefumuri.review_api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

/**
 * @author Gabriela Spiescu
 */
@SuppressWarnings("deprecation")
@Configuration
public class Config {
	
	@Bean
    public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory,CustomConversions conversions) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
        mappingConverter.setCustomConversions(beanFactory.getBean(CustomConversions.class));
        mappingConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	@Bean
    public CustomConversions customConversions() {
        List list = new ArrayList();
        list.add(new TimestampConverter());
        list.add(new Converter<java.util.Date, java.sql.Date>() {

			@Override
			public java.sql.Date convert(java.util.Date source) {
				if(source != null){
		            return new java.sql.Date(source.getTime());
		        }
				return null;
			}
		});
        return new CustomConversions(list);
    }
    
}
