public class Factory {
    public Factory(String str1,String str2,String str3)
    {
        if (str1.equals("qutime"))
        {
            new Selection(str2, str3);
        }
        else if (str1.equals("del"))
        {
            new Deletion(str2, str3);
        }
    }

    public Factory(String str1,String str2)
    {
        if (str1.equals("qutime"))
        {
            new Selection(str2);
        }
        else if (str1.equals("del"))
        {
            new Deletion(str2);
        }
    }

}
