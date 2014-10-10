package jat.tests.path_test;


public class Jwhich
{
    public static void main (String [] args) throws Exception
    {
        System.out.println("test");

        String arg="jat.test.path_test.Jwhich";
        
        try
        {
            Class c = Class.forName (arg);
            String classRes = "/"+arg.replace ('.', '/')+".class";
            System.out.println ("Class "+args+" URL: "+c.getResource (classRes));
        }
        catch (Throwable t)
        {
            System.out.println ("Unable to locate class "+arg);
        }
    }
}

