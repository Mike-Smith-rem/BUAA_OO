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

    @Override
    public boolean equalsOutName(Book book) {
        if (this.getClass().equals(book.getClass()))
        {
            if (Math.abs(book.getPrice() - this.getPrice()) < 1e-5)
            {
                return this.getAge() == ((OtherA) book).getAge();
            }
        }
        return false;
    }

    @Override
    public Book clone(Book book) {
        return new OtherA(book.getName(),book.getPrice(),book.getNumber(),((OtherA) book).getAge());
    }
}
