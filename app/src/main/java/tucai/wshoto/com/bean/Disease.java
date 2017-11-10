package tucai.wshoto.com.bean;

/**
 * Created by Weshine on 2017/5/15.
 */

public class Disease {
    String id;
    String title;
    public Disease(String id,String title){
        this.id = id;
        this.title = title;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return id;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
}
