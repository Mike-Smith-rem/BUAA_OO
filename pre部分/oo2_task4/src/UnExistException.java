public class UnExistException extends Exception{
    private String name;

    public UnExistException(String name)
    {
        this.name = name;
    }

    public String getReason()
    {
        return "Oh, no! We don't have " + name + ".";
    }
}
