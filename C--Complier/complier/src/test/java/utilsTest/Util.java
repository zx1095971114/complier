package utilsTest;

import java.io.*;

/**
 * @projectName: complier
 * @package: utils
 * @className: Util
 * @author: Zhou xiang
 * @description: 检测时使用的工具类
 * @date: 2023/4/29 14:28
 * @version: 1.0
 */
public class Util {
    /**
     * @param filePath:
     * @return String
     * @author ZhouXiang
     * @description 根据文件名，将整个文件以一个字符串的形式读取进来
     * @exception
     */
    public static String readFile(String filePath) throws IOException {
        //BufferedReader是可以按行读取文件
        FileInputStream inputStream = new FileInputStream(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String content = "";
        String str = null;
        while((str = bufferedReader.readLine()) != null)
        {
            content = content + str;
        }

        //close
        inputStream.close();
        bufferedReader.close();

        return content;
    }


    /**
     * @param filePath:
     * @param content:
     * @return void
     * @author ZhouXiang
     * @description 以追加的方式向文件中写入
     * @exception
     */
    public static void  writeFIle(String filePath, String content) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        bw.write(content);
        bw.close();
    }
}
