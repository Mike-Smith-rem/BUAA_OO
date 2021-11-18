public class OtherS extends Book
{
    private long year;

    public OtherS(String name, double price,long number,long year)
    {
        super(name,price,number);
        this.year = year;
    }

    public long getYear()
    {
        return year;
    }

    public void getMessage()
    {
        System.out.println(getName() + " " + getPrice() + " " + getNumber() + " " + getYear());
    }
}
