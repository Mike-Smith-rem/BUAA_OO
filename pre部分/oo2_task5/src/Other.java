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

    @Override
    public boolean equalsOutName(Book book) {
        if (this.getClass().equals(book.getClass()))
        {
            return Math.abs(this.getPrice() - book.getPrice()) < 1e-5;
        }
        return false;
    }

    @Override
    public Book clone(Book book) {
        return new Other(book.getName(),book.getPrice(),book.getNumber());
    }
}
