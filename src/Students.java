public class Students extends User{
    public Students(String userId, String userName, String userPass, String accountType) {
        super(userId, userName, userPass, accountType);
    }

    @Override
    public String getUserType() {
        return "Student";
    }

}
