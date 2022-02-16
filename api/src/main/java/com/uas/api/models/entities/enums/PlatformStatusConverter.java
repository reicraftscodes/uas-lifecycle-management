package com.uas.api.models.entities.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PlatformStatusConverter implements AttributeConverter<PlatformStatus, String> {

    @Override
    public String convertToDatabaseColumn(final PlatformStatus platformStatus) {
        if (platformStatus == null) {
            return null;
        }
        return platformStatus.getLabel();
    }

    @Override
    public PlatformStatus convertToEntityAttribute(final String name) {
        if (name == null) {
            return null;
        }

        return Stream.of(PlatformStatus.values())
                .filter(ps -> ps.getLabel().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
