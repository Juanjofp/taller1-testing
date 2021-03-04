package es.centic.taller1.taller1.users;

public class UserBodyResponse {
    private String username;
    private String name;

    protected UserBodyResponse() {}

    public UserBodyResponse(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
    }

    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
}
