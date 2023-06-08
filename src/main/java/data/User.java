package data;

import java.io.Serializable;

public class User implements Serializable {
    private final String username;
    private final String password;
    private final boolean isSignUp;

    public User(String username, String password, boolean isSignUp) {
        this.username = username;
        this.password = password;
        this.isSignUp = isSignUp;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isSignUp() {
        return isSignUp;
    }

    @Override
    public String toString() {
        return username + " : " + password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof User) {
            User userObj = (User) obj;
            return username.equals(userObj.getUsername()) && password.equals(userObj.getPassword());
        }
        return false;
    }
}
