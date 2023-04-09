package org.example.service.enums;

/**
 * 枚举类型作为key value 状态集合
 * @author ldd
 */
public class EnumStatusTest {
    public static void main(String[] args) {
        Order order = new Order("123", OrderStatus.CREATED);
        System.out.println("Order status: " + order.getStatus().getDescription());

        order.setStatus(OrderStatus.PROCESSING);
        System.out.println("Order status: " + order.getStatus().getDescription());

        order.setStatus(OrderStatus.SHIPPED);
        System.out.println("Order status: " + order.getStatus().getDescription());

        order.setStatus(OrderStatus.COMPLETED);
        System.out.println("Order status: " + order.getStatus().getDescription());

        order.setStatus(OrderStatus.CANCELLED);
        System.out.println("Order status: " + order.getStatus().getDescription());

        System.out.println(OrderStatus.getDesByCode(1));
    }
}
enum OrderStatus {
    CREATED(1,"Order created"),
    PROCESSING(2,"Order processing"),
    SHIPPED(3,"Order shipped"),
    COMPLETED(4,"Order completed"),
    CANCELLED(5,"Order cancelled");

    private String description;
    private int code;

    OrderStatus(int code,String description) {
        this.code = code;
        this.description = description;
    }
    public static String getDesByCode(int code){
        for (OrderStatus value : OrderStatus.values()) {
            if(value.getCode() == value.getCode()){
                return value.description;
            }
        }
        return "";
    }
    public String getDescription() {
        return description;
    }
    public int getCode(){
        return code;
    }
}
class Order {
    private String id;
    private OrderStatus status;

    public Order(String id, OrderStatus status) {
        this.id = id;
        this.status = status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }
}