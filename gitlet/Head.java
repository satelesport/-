package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import static gitlet.Utils.*;

public class Head implements Serializable {
    private List<String> pointTo;
    private List<String> currentBranch;

    public Head(String ID) {
        pointTo = new ArrayList<>();
        pointTo.add(ID);
        currentBranch = new ArrayList<>();
    }

    public String getPointTo(){
        return pointTo.get(0);
    }

    public void saveHead(File DIR, String name){
        File f = join(DIR, name);
        writeObject(f, this);
    }

    public static Head readHead(File DIR, String name){
        File f = join(DIR, name);
        return readObject(f, Head.class);
    }

    public void changePointTo(String ID){
        pointTo.clear();
        pointTo.add(ID);
    }

    public void changeCurrentBranch(String brunch){
        currentBranch.clear();
        currentBranch.add(brunch);
    }

    public String getCurrentBranch(){
        return currentBranch.get(0);
    }

}