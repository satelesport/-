package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import static gitlet.Utils.*;

public class AddStage implements Serializable {
    private Map<String, String> addStage;
    //key is filePath, value is ID

    public AddStage(){
        addStage = new TreeMap<>();
    }

    public void saveAddStage(){
        File f = join(Repository.GITLET_DIR, "addstage");
        writeObject(f, this);
    }

    public static AddStage readAddStage(){
        File f = join(Repository.GITLET_DIR, "addstage");
        return readObject(f, AddStage.class);
    }

    public void add(Blob b){
        addStage.put(b.getFilePath(), b.getID());

        Head h = Head.readHead(Repository.GITLET_DIR, "HEAD");
        Commit currentCommit = Commit.readCommit(h.getPointTo());

        RemoveStage removeStage = RemoveStage.readRemoveStage();
        if(removeStage.checkBlob(b)){
            removeStage.remove(b);
        }
        removeStage.saveRemoveStage();

        if(currentCommit.checkBlob(b)){
            addStage.remove(b.getFilePath());
        }
    }

    public Map<String, String> getAddStage(){
        return addStage;
    }
}
