package tw.tonyyang.englishwords.util;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import tw.tonyyang.englishwords.App_;
import tw.tonyyang.englishwords.database.Word;

import static tw.tonyyang.englishwords.util.LoadTask.TMP_FILE_NAME;

@EBean(scope = EBean.Scope.Singleton)
public class Tool {

    private static final Logger logger = LoggerFactory.getLogger(Tool.class);

    private Context context;

    private String fileUrl;

    Tool(Context context) {
        this.context = context;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    void readExcel() {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = context.openFileInput(TMP_FILE_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fileInputStream == null) {
            return;
        }

        try {
            Workbook book = Workbook.getWorkbook(fileInputStream);
            book.getNumberOfSheets();
            //獲得工作表對象
            Sheet sheet = book.getSheet(0);
            int Rows = sheet.getRows();
            int Cols = sheet.getColumns();
            logger.debug("當前工作表的名字:" + sheet.getName());
            logger.debug("總行數:" + Rows);
            logger.debug("總列數:" + Cols);

            for (int i = 0; i < Rows; i++) {
                if (String.valueOf(sheet.getCell(0, i).getContents().charAt(0)).equals("#"))
                    continue;

                Word word = new Word();
                word.setWord(sheet.getCell(0, i).getContents());
                word.setWordMean(sheet.getCell(1, i).getContents());
                word.setCategory(sheet.getCell(2, i).getContents());
                word.setWordStar(sheet.getCell(3, i).getContents());
                word.setWordSentence(sheet.getCell(4, i).getContents());

                App_.getDb().userDao().insertAll(word);
            }
            book.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
