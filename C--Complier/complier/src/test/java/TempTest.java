import lex.entity.DFA;
import lex.entity.NFA;
import log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * @projectName: complier
 * @package: PACKAGE_NAME
 * @className: TempTest
 * @author: Zhou xiang
 * @description: 调试用
 * @date: 2023/4/9 9:17
 * @version: 1.0
 */
public class TempTest {
    private static final Logger logger = LoggerFactory.getLogger(TempTest.class);
    public static void main(String[] args) throws FileNotFoundException {


        method1();
    }

    public static void method1(){
        Log.errorLog("test3", logger);
    }
}
