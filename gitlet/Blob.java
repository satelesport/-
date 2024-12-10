package gitlet;

import java.io.File;
import java.io.Serializable;
import static gitlet.Utils.*;

public class Blob implements Serializable {
    private String ID;
    private String fileName;
    private String filePath;
    public byte[] content;

    public Blob(String fn, byte[] c, String fp){
        fileName = fn;
        content = c;
        filePath = fp;
        ID = createID();
    }

    public void saveBlob(){
        File f = join(Repository.BLOB_DIR, ID);
        writeObject(f, this);
    }

    public static Blob readBlob(String wantedID){
        File f = join(Repository.BLOB_DIR, wantedID);
        return readObject(f, Blob.class);
    }

    public String getID(){
        return ID;
    }

    public String getFilePath(){
        return filePath;
    }

    private String createID(){
        return Utils.sha1(fileName, filePath, content);
    }

}
