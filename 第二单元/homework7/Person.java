import com.oocourse.elevator3.PersonRequest;

public class Person {
    private int fromFloor;
    private int toFloor;
    private int originToFloor;
    private int id;
    private String status;

    Person(PersonRequest p) {
        this.fromFloor = p.getFromFloor();
        this.originToFloor = p.getToFloor();
        this.toFloor = originToFloor;
        this.status = "NO-NEED-TRANSIT";
        this.id = p.getPersonId();
    }

    public synchronized int getFromFloor() {
        return fromFloor;
    }

    public synchronized int getToFloor() {
        return toFloor;
    }

    public synchronized int getOriginToFloor() {
        return originToFloor;
    }

    public synchronized int getId() {
        return id;
    }

    public synchronized String getStatus() {
        return status;
    }

    public synchronized void setStatus(String s) {
        status = s;
    }

    public synchronized void setToFloor(int toFloor) {
        this.toFloor = toFloor;
    }

    public synchronized void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
    }

}
