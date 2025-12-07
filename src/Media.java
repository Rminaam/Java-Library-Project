import java.time.LocalDate;

public abstract class Media {
    protected String id;
    protected String title;
    protected LocalDate dueDate;
    protected boolean isBorrowed = false;
    protected int loanPeriod;
    protected int maxRenewal;

    public Media (String id, String title){
    this.id = id;
    this.title = title;
    this.isBorrowed = false;
}

public abstract int getLoanPeriod();

public abstract boolean canRenew();

public abstract String getType();

public abstract void renew();

public void borrow(LocalDate currentDate){
    isBorrowed = true;
    dueDate = LocalDate.now().plusWeeks(getLoanPeriod());
}

public void returnItem(){
    isBorrowed = false;
    dueDate = null;
}

@Override
    public String toString(){
    return title + " | ID: " + id + " | Loaned: " + isBorrowed;
}

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed){
        this.isBorrowed = borrowed;
    }

    public String getTitle(){
        return title;
    }

    public String getId(){
        return id;
    }
}
