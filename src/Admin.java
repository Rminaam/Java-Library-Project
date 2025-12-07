public class Admin extends User{
    public Admin(String userId, String userName, String userPass, String accountType) {
        super(userId, userName, userPass, accountType);
    }

    @Override
    public String getUserType() {
        return "Admin";
    }


}
