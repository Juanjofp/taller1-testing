package es.centic.taller1.taller1.users;

import java.util.List;

public class ListOfUsersBodyResponse {
    private List<User> users;

    public ListOfUsersBodyResponse(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
