public class Maths extends OtherS
{
    private long grade;

    public Maths(String name,double price,long number,long year,long grade)
    {
        super(name, price, number, year);
        this.grade = grade;
    }

    @Override
    public void getMessage() {
        System.out.printf("%s %f %d %d %d%n",getName(),getPrice(),getNumber(),getYear(),grade);
    }

    @Override
    public boolean equalsOutName(Book book) {
        if (book.getClass().equals(this.getClass()))
        {
            if (Math.abs(book.getPrice() - this.getPrice()) < 1e-5)
            {
                if (((Maths) book).getYear() == this.getYear())
                {
                    return this.grade == ((Maths) book).getGrade();
                }
            }
        }
        return false;
    }

    public long getGrade() {
        return grade;
    }

    @Override
    public Book clone(Book book) {
        String name = book.getName();
        return new Maths(name,book.getPrice(),book.getNumber(),((Maths)book).getYear(),((Maths)book).getGrade());
    }
}
