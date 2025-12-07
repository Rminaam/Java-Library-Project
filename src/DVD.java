import java.time.LocalDate;

public class DVD extends Media{
    private String yearOfPublish;
    private String Artist;

    public DVD (String id, String title, String Artist, String yearOfPublish){
        super(id, title);
        this.Artist = Artist;
        this.yearOfPublish = yearOfPublish;
    }

    @Override
    public int getLoanPeriod(){
        return 1;
    }

    @Override
    public boolean canRenew(){
        return false;
    }

    public void renew(){

    }

    public String getYearOfPublish(){
        return yearOfPublish;
    }

    public String getID(){
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getArtist(){
        return Artist;
    }

    public boolean getIsBorrowed(){
        return isBorrowed;
    }

    public LocalDate getDueDate(){
        return dueDate;
    }

    public String getType(){
        return "[DVD]";
    }

    @Override
    public String toString(){
        return "[DVD] " + title + " by " + Artist +
                " | ID: " + id + (isBorrowed ? " | Due: " + dueDate : " | Available\n");
    }
}
