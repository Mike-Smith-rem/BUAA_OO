public class Maths extends OtherS
{
    private int grade;

    public Maths(String name,double price,int number,int year,int grade)
    {
        super(name, price, number, year);
        this.grade = grade;
    }

    @Override
    public void getMessage() {
        System.out.printf("%s %f %d %d %d%n",getName(),getPrice(),getNumber(),getYear(),grade);
    }
}
