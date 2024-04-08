package edu.java.domain.model.jpa;

import jakarta.persistence.AttributeConverter;

import java.net.URI;

public class URIConverter implements AttributeConverter<URI, String> {

    @Override
    public String convertToDatabaseColumn(URI attribute) {
        return (attribute == null) ? null : attribute.toString();
    }

    @Override
    public URI convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : URI.create(dbData);
    }
}
