public class RegularUser extends User {
    public RegularUser(String userId, String userName, String userPass, String accountType) {
        super(userId, userName, userPass, accountType);
    }

    @Override
    public String getUserType() {
        return "Regular";
    }
}
