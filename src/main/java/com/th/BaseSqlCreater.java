package com.th;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author tianheng
 */
public abstract class BaseSqlCreater implements ISqlCreater {

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public abstract String getFileName();

    public void save2File(String fileContent) {
        File file = new File(getFileName());
        FileWriter out = null;
        try {
            out = new FileWriter(file, true);
            out.write(fileContent);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        save2File(content);
    }
}
