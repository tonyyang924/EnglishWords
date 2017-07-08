package tw.tonyyang.englishwords.util;

import android.content.Context;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.io.FileInputStream;
import java.io.IOException;

import jxl.Sheet;
import jxl.Workbook;
import tw.tonyyang.englishwords.db.Words;
import tw.tonyyang.englishwords.db.WordsDao;

import static tw.tonyyang.englishwords.util.LoadTask.TMP_FILE_NAME;

@EBean(scope = EBean.Scope.Singleton)
public class Tool {

    @Bean
    protected WordsDao wordsDao;

    private Context context;

    private String fileUrl;

    public Tool(Context context) {
        this.context = context;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void readExcel() {
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
            System.out.println("當前工作表的名字:" + sheet.getName());
            System.out.println("總行數:" + Rows);
            System.out.println("總列數:" + Cols);

            for (int i = 0; i < Rows; i++) {
                if (String.valueOf(sheet.getCell(0, i).getContents().charAt(0)).equals("#"))
                    continue;

                Words words = new Words();
                words.setWord(sheet.getCell(0, i).getContents());
                words.setWordMean(sheet.getCell(1, i).getContents());
                words.setCategory(sheet.getCell(2, i).getContents());
                words.setWordStar(sheet.getCell(3, i).getContents());
                words.setWordSentence(sheet.getCell(4, i).getContents());

                wordsDao.createOrUpdate(words);
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
