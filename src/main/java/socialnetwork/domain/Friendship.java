package socialnetwork.domain;



import java.time.LocalDate;



public class Friendship extends Entity<Tuple<Long,Long>> {

    LocalDate date;

    public Friendship(LocalDate  date) {
        this.date = date; //when we load the friendship from the friedship file
    }

    public Friendship(Tuple<Long,Long> ids){
        setId(ids);
        this.date = LocalDate.now();//used when we add a new friendship at run time
    }
    /**
     *
     * @return the date when the friendship was created
     */
    public LocalDate getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Prietenie{" +
                " date=" + date +
                " IdLeft = " + super.getId().getLeft() +
                " IdRight = " + super.getId().getRight()+
                '}';
    }
}
