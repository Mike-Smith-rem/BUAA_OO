public class Novel extends OtherA
{
    private boolean finish;

    public Novel(String name,double price,long number,long age,boolean finish)
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

    @Override
    public boolean equalsOutName(Book book) {
        if (book.getClass().equals(this.getClass()))
        {
            if (Math.abs(this.getPrice() - book.getPrice()) < 1e-5)
            {
                if (((Novel) book).getAge() == this.getAge())
                {
                    return ((Novel) book).isFinish() == this.isFinish();
                }
            }
        }
        return false;
    }

    @Override
    public Book clone(Book book) {
        String name = book.getName();
        return new Novel(name,book.getPrice(),book.getNumber(),((Novel)book).getAge(),((Novel)book).isFinish());
    }
}
