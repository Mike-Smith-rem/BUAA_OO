public class OtherA extends Book
{
    private int age;

    public OtherA(String name,double price,int number,int age)
    {
        super(name,price,number);
        this.age = age;
    }

    public int getAge()
    {
        return age;
    }

    @Override
    public void getMessage()
    {
        System.out.printf("%s %f %d %d%n",getName(),getPrice(),getNumber(),getAge());
    }
}
