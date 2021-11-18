public class OtherS extends Book
{
    private int year;

    public  OtherS(String name,double price,int number,int year)
    {
        super(name,price,number);
        this.year = year;
    }

    public int getYear()
    {
        return year;
    }

    public void getMessage()
    {
        System.out.printf("%s %f %d %d%n",getName(),getPrice(),getNumber(),year);
    }
}
