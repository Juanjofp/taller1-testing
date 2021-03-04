package es.centic.taller1.taller1.users;

public class UserBodyRequest {
    private String username;
    private String name;

    protected UserBodyRequest() {}

    public UserBodyRequest(String username, String name) {
        this.username = username;
        this.name = name;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
