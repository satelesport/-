package gitlet;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    private List<String> parentsID;
    private Date date;
    private Map<String,String> BlobID;
    private String ID;

    private String createID(){
        return Utils.sha1(dateToTimeStamp(date), message, parentsID.toString(), BlobID.toString());
    }

    private static String dateToTimeStamp(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.US);
        return dateFormat.format(date);
    }

    public Commit(){
        message = "initial commit";
        date = new Date(0);
        BlobID = new TreeMap<>();
        parentsID = new ArrayList<>();
        ID = createID();
    }

    public Commit(String m, Commit parent){
        message = m;
        date = new Date();

        BlobID = new TreeMap<>();

        parentsID = new ArrayList<>();
        parentsID.add(parent.getID());

        Map<String, String> parentBlobID = parent.getBlobID();
        for(String key : parentBlobID.keySet()){
            String value = parentBlobID.get(key);
            BlobID.put(key, value);
        }

        RemoveStage removestage = RemoveStage.readRemoveStage();
        Map<String, String> removestageMap = removestage.getRemoveStage();
        for(String key : removestageMap.keySet()){ //key is the staged file path
            BlobID.remove(key);
        }
        removestage.getRemoveStage().clear();
        removestage.saveRemoveStage();

        AddStage addstage = AddStage.readAddStage();
        Map<String, String> addstageMap = addstage.getAddStage();
        for(String key : addstageMap.keySet()){ //key is the staged file path
            BlobID.put(key, addstageMap.get(key));
        }
        addstage.getAddStage().clear();
        addstage.saveAddStage();

        ID = createID();
    }

    public String getID(){
        return ID;
    }

    public void saveCommit(){
        File f = join(Repository.COMMIT_DIR, ID);
        writeObject(f, this);
    }

    public static Commit readCommit(String wantedID){
        File f = join(Repository.COMMIT_DIR, wantedID);
        return readObject(f, Commit.class);
    }

    public void addBlob(Blob b){
        BlobID.put(b.getFilePath(), b.getID());
    }

    public boolean checkBlob(Blob b){
        return BlobID.containsValue(b.getID());
    }

    public Map<String, String> getBlobID(){
        return BlobID;
    }

    public boolean hasParent(){
        return !parentsID.isEmpty();
    }

    public String findParentID(){
        return parentsID.get(0);
    }

    public void printLog(){
        System.out.println("===");
        System.out.println("commit " + ID);
        System.out.println("Date: " + dateToTimeStamp(date));
        System.out.println(message);
        System.out.println();
    }

    public String getMessage(){
        return message;
    }

    public void addParent(String ID){
        parentsID.add(ID);
    }

    public List<String> getParentsID(){
        return parentsID;
    }
}
