package com.gabriela.fabricadefumuri.review_api;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;

/**
 * @author Gabriela Spiescu
 */
public class TimestampConverter implements Converter<Date, Timestamp> {

	@Override
	public Timestamp convert(Date source) {
		if(source != null){
            return new Timestamp(source.getTime());
        }
        return null;
	}

}
