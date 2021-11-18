public class Poetry extends OtherA
{
    private String author;

    public Poetry(String name,double price,long number,long age,String author)
    {
        super(name,price,number,age);
        this.author = author;
    }

    public String getAuthor()
    {
        return author;
    }

    public void getMessage()
    {
        System.out.printf("%s %f %d %d ",getName(),getPrice(),getNumber(),getAge());
        System.out.println(author);
    }
}
