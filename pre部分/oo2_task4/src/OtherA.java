public class OtherA extends Book
{
    private long age;

    public OtherA(String name,double price,long number,long age)
    {
        super(name,price,number);
        this.age = age;
    }

    public long getAge()
    {
        return age;
    }

    @Override
    public void getMessage() {
        System.out.println(getName() + " " + getPrice() + " " + getNumber() + " " + getAge());
    }
}
