public abstract class Book
{
    private String name;
    private double price;
    private long number;

    public Book(String name,double price,long number)
    {
        this.name = name;
        this.price = price;
        this.number = number;
    }

    public String getName()
    {
        return name;
    }

    public double getPrice()
    {
        return price;
    }

    public long getNumber()
    {
        return number;
    }

    public double sum()
    {
        return number * price;
    }

    public abstract void getMessage();

}
