public class Computer extends OtherS
{
    private String major;

    public Computer(String name,double price,long number,long year,String major)
    {
        super(name, price, number, year);
        this.major = major;
    }

    @Override
    public void getMessage() {
        System.out.printf("%s %f %d %d %s%n",getName(),getPrice(),getNumber(),getYear(),major);
    }
}
