package restAPI.messages;

public interface MessageFactoryInterface {
    public String create(MessageEnum type, String obj);
    public String create(MessageEnum type, String obj1, String obj2);
    public String create(MessageEnum type, String obj1, String obj2, String obj3);
}
