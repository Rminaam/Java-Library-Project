import java.time.LocalDate;

public class CD extends Media{
    private String Artist;

    public CD (String id, String title, String Artist){
        super(id, title);
        this.Artist = Artist;
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

    public String getID(){
        return id;
    }

    public String getArtist(){
        return Artist;
    }

    public String getTitle(){
        return title;
    }

    public boolean getIsBorrowed(){
        return isBorrowed;
    }

    public LocalDate getDueDate(){
        return dueDate;
    }

    public String getType(){
        return "[CD]";
    }

    @Override
    public String toString(){
        return "[CD] " + title + " by " + Artist +
                " | ID: " + id + (isBorrowed ? " | Due: " + dueDate : " | Available\n");
    }
}
