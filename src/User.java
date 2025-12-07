import java.io.*;
import java.util.ArrayList;
import java.util.List;

public abstract class User {
    protected String userId;
    protected String userName;
    protected String userPass;
    protected String accountType;
    protected List<Media> borrowedItems;
    protected double totalFees;
    protected boolean approval;

    public User(){

    }

    public User(String userId, String userName, String userPass, String accountType) {
        this.userId = userId;
        this.userName = userName;
        this.userPass = userPass;
        this.accountType = accountType;
        this.borrowedItems = new ArrayList<>();
        this.totalFees = 0.0;
    }

    public boolean getApproval(){
        return approval;
    }

    public void setApproval(boolean approval){
        this.approval = approval;
    }

    public abstract String getUserType();

    public void setUserPass(String inputPass) throws IOException {
        BufferedWriter passWriter = new BufferedWriter(new FileWriter(this.userName + "Pass" + ".txt"));
        this.userPass = inputPass;
        passWriter.write(this.userPass);
        passWriter.close();
    }

    public boolean verifyPass (String inputPass) throws IOException {
        BufferedReader passReader = new BufferedReader(new FileReader(this.userName + "Pass" + ".txt"));
        String Password = passReader.readLine();
        if (Password != null && Password.equals(inputPass)){
            return true;
        } else {
            return false;
        }
    }

    public boolean hasPass (String userName) throws IOException{
        try (BufferedReader passReader = new BufferedReader(new FileReader(this.userName + "Pass" + ".txt"))){
            if (passReader.readLine() == null){
                return false;
            } else {
                return true;
            }
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getUserPass(){
        return userPass;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserName(){
        this.userName = userName;
    }

    public boolean checkPassword(String inputPass) {
        return userPass.equals(inputPass);
    }

    public List<Media> getBorrowedItems() {
        return borrowedItems;
    }

    public double getTotalFees() {
        return totalFees;
    }

    public void addFee(double fee) {
        totalFees += fee;
    }

    public void payFees() {
        totalFees = 0;
    }

    public void borrowItem(Media item) {
        borrowedItems.add(item);
    }

    public void returnItem(Media item) {
        borrowedItems.remove(item);
    }

    @Override
    public String toString() {
        return "User: " + userName + " (" + accountType + ") - ID: " + userId;
    }
}
