package com.uas.api.models.entities.enums;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PartNameConverter implements AttributeConverter<PartName, String> {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PartNameConverter.class);

    @Override
    public String convertToDatabaseColumn(PartName partName) {
        if (partName == null) {
            return null;
        }
        return partName.getName();
    }

    @Override
    public PartName convertToEntityAttribute(String name) {
        if (name == null) {
            return null;
        }

        return Stream.of(PartName.values())
                .filter(pn -> pn.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
