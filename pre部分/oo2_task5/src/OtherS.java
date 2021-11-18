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

    public boolean equalsOutName(Book book) {
        if (this.getClass().equals(book.getClass()))
        {
            if (Math.abs(book.getPrice() - this.getPrice()) < 1e-5)
            {
                return this.getYear() == ((OtherS) book).getYear();
            }
        }
        return false;
    }

    @Override
    public Book clone(Book book) {
        return new OtherS(book.getName(),book.getPrice(),book.getNumber(),((OtherS)book).getYear());
    }
}
