import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginManager {
    List<User> users;
    boolean approved = false;

    public LoginManager(){
        users = new ArrayList<>();
    }

    public User login(String userId, String userPass) throws IOException {
        for (User u : users) {
            if (u.getUserId().equalsIgnoreCase(userId)) {
                if (u.verifyPass(userPass)) {
                    return u;
                } else {
                    System.out.println("Incorrect Password.");
                    return null;
                }
            }
        }
        System.out.println("Incorrect ID.");
        return null;
    }

    public boolean register(User newUser) throws IOException {
        for (User u : users) {
            if (u.getUserName().equals(newUser.getUserName())) {
                System.out.println("User already exists.");
                return false;
            }
        }

        if (newUser.getUserName().isEmpty()){
            System.out.println("You must choose a Username!");
            return false;
        }

        if (newUser.getUserPass().isEmpty()){
            System.out.println("You must choose a password!");
            return false;
        } else {
            newUser.setUserName();
            newUser.setUserPass(newUser.getUserPass());
            users.add(newUser);
            return true;
        }
    }

    public String generateUserId (User newUser) {
        long countSameType = users.stream()
                .filter(u -> u.accountType.equals(newUser.accountType))
                .count();
        int nextNumber = (int) countSameType + 1;

        String prefix;
        switch (newUser.accountType) {
            case "Student": prefix = "S"; break;
            case "Regular": prefix = "R"; break;
            case "Admin": prefix = "A"; break;
            default: prefix = "U"; break;
        }

        return newUser.userId = String.format("%s%03d", prefix, nextNumber);
    }
}
