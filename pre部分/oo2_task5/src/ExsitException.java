public class ExsitException extends Exception{
    private String name;

    public ExsitException(String name)
    {
        this.name = name;
    }

    String getReason()
    {
        return "Oh, no! The " + name + " exist.";
    }

}
