public class Computer extends OtherS
{
    private String major;

    public Computer(String name,double price,long number,long year,String major)
    {
        super(name, price, number, year);
        this.major = major;
    }

    public String getMajor()
    {
        return major;
    }


    @Override
    public void getMessage() {
        System.out.printf("%s %f %d %d %s%n",getName(),getPrice(),getNumber(),getYear(),major);
    }

    public boolean equalsOutName(Book book) {
        if (book.getClass().equals(this.getClass()))
        {
            if (Math.abs(book.getPrice() - this.getPrice()) < 1e-5)
            {
                if (((Computer) book).getYear() == this.getYear())
                {
                    return ((Computer) book).getMajor().equals(this.getMajor());
                }
            }
        }
        return false;
    }

    @Override
    public Book clone(Book book) {
        String name = book.getName();
        long number = book.getNumber();
        return new Computer(name,book.getPrice(),number,((Computer)book).getYear(),((Computer)book).getMajor());
    }
}
