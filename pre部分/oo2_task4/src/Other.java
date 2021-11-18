public class Other extends Book
{
    public Other(String name,double price,long number)
    {
        super(name,price,number);
    }

    public void getMessage()
    {
        System.out.println(getName() + " " + getPrice() + " " + getNumber());
    }
}
