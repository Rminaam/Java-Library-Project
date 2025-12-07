import java.time.LocalDate;

public class BoardGame extends Media{
    private String publisher;

    public BoardGame(String id, String title, String publisher){
        super(id,title);
        this.publisher = publisher;
    }

    @Override
    public int getLoanPeriod(){
        return 2;
    }

    @Override
    public boolean canRenew(){
        return false;
    }

    public void renew(){

    }

    public String getType(){
        return "[Board Game]";
    }

    public String getID(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getPublisher(){
        return publisher;
    }

    public boolean getIsBorrowed(){
        return isBorrowed;
    }

    public LocalDate getDueDate(){
        return dueDate;
    }

    @Override
    public String toString(){
        return "[Board Game] " + title + " by " + publisher +
                " | ID: " + id + (isBorrowed ? " | Due: " + dueDate : " | Available\n");
    }
}
