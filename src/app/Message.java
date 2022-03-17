package app;

public class Message {

    private String user;
    private String message;
    private long date;

    public Message() {
    }

    public Message(String user, String message, long date) {
        this.user = user;
        this.message = message;
        this.date = date;
    }

    public String construct (Message rawMessage){
        return user + "---" + date + "---" + message ;
    }

    public Message deconstruct (String messageConstructed){
        String []messageSplit = messageConstructed.split("---");
        System.out.println(messageSplit[0] + " " + messageSplit[1] + " " + messageSplit[2]);

        return new Message(messageSplit[0], messageSplit[1], Long.parseLong(messageSplit[3]));
    }

    @Override
    public String toString() {
        return "<" + date + "> <" + user + ">: " + message ;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
