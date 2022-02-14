package com.uas.api.models.entities.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;

public class PartNameConverter implements Converter<String, PartName> {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PartNameConverter.class);
    @Override
    public PartName convert(String source){
        try {
            return source.isEmpty() ? null : PartName.valueOf(source.toUpperCase());
        } catch(Exception e) {
            LOGGER.error("Failed to convert!");
            e.printStackTrace();
            return null; //
        }
//        return PartName.valueOf(source.toUpperCase());
    }
}
