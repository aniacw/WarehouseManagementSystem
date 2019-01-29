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
                return "D";
            case COMPLETED:
                return "H";
            case CANCELLED:
                return "P";
            default:
                throw new IllegalArgumentException("Unknown" + attribute);
        }
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "D":
                return PENDING;
            case "H":
                return COMPLETED;
            case "P":
                return CANCELLED;
            default:
                throw new IllegalArgumentException("Unknown" + dbData);
        }
    }
}

