package main;

import javafx.util.StringConverter;

public class EnumConverter extends StringConverter<OrderStatus> {

    @Override
    public String toString(OrderStatus status) {
        return status.getName();
    }

    @Override
    public OrderStatus fromString(String string) {
        return OrderStatus.valueOf(string);
    }
}
