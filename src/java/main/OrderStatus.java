package main;

public enum OrderStatus {
    PENDING("pending"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private String name;

    public String getName() {
        return name;
    }

     OrderStatus(String name) {
        this.name = name;
    }

}
