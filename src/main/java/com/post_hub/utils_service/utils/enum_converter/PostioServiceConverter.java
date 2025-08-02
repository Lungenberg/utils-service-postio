package com.post_hub.utils_service.utils.enum_converter;

import com.post_hub.utils_service.model.enums.PostioService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PostioServiceConverter implements AttributeConverter<PostioService, String> {
    @Override
    public String convertToDatabaseColumn(PostioService attribute) {
        return attribute.getValue();
    }

    @Override
    public PostioService convertToEntityAttribute(String dbData) {
        return PostioService.fromValue(dbData);
    }
}
