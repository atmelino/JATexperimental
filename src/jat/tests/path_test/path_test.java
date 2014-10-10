package jat.tests.path_test;

import jat.coreNOSA.util.FileUtil;

public class path_test
{
    public static void main (String [] args) throws Exception
    {
        System.out.println(FileUtil.getClassFilePath("jat.test.path_test","path_test"));
    }
}

