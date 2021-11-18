public class Novel extends OtherA
{
    private boolean finish;

    public Novel(String name,double price,int number,int age,boolean finish)
    {
        super(name,price,number,age);
        this.finish = finish;
    }

    public boolean isFinish()
    {
        return finish;
    }

    public void getMessage()
    {
        System.out.printf("%s %f %d %d ",getName(),getPrice(),getNumber(),getAge());
        System.out.println(finish);
    }
}
