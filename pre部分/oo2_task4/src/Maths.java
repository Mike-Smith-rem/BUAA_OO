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
}
