package main;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import static main.OrderStatus.CANCELLED;
import static main.OrderStatus.COMPLETED;
import static main.OrderStatus.PENDING;

@Converter
public class ConverterEnum implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus attribute) {
        switch (attribute) {
            case PENDING:
                return "PENDING";
            case COMPLETED:
                return "COMPLETED";
            case CANCELLED:
                return "CANCELLED";
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "PENDING":
                return PENDING;
            case "COMPLETED":
                return COMPLETED;
            case "CANCELLED":
                return CANCELLED;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }
}