package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import static gitlet.Utils.*;

public class RemoveStage implements Serializable {
    private Map<String ,String> removeStage;

    public RemoveStage(){
        removeStage = new TreeMap<>();
    }

    public void saveRemoveStage(){
        File f = join(Repository.GITLET_DIR, "removestage");
        writeObject(f, this);
    }

    public static RemoveStage readRemoveStage(){
        File f = join(Repository.GITLET_DIR, "removestage");
        return readObject(f, RemoveStage.class);
    }

    public boolean checkBlob(Blob b){
        return removeStage.containsKey(b.getFilePath());
    }

    public void add(Blob b){
        removeStage.put(b.getFilePath(), b.getID());
    }

    public void remove(Blob b){
        removeStage.remove(b.getFilePath());
    }

    public Map<String, String> getRemoveStage(){
        return removeStage;
    }
}
