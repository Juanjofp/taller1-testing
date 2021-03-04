package es.centic.taller1.taller1.users;

public class UserBodyResponse {
    private String username;
    private String name;
    private String avatar;

    protected UserBodyResponse() {}

    public UserBodyResponse(User user) {
        this.username = user.getUsername();
        this.name = user.getName();
        this.avatar = user.getAvatar();
    }

    public String getName() {
        return name;
    }
    public String getUsername() {
        return username;
    }
    public String getAvatar() {
        return avatar;
    }
}
