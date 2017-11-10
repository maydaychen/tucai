package tucai.wshoto.com.bean;

/**
 * Created by user on 2017/11/10.
 */

public class ImageBean {
    /**
     * statuscode : 1
     * msg : 上传成功！
     * result : {"upload_ok":true,"save_path":"1711/10/532cd90c5f4fab390318762cd5bd6458.jpg","show_path":"/data/upload/avatar/1711/10/532cd90c5f4fab390318762cd5bd6458.jpg?1510306012"}
     * dialog :
     */

    private int statuscode;
    private String msg;
    private ResultBean result;
    private String dialog;

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public String getDialog() {
        return dialog;
    }

    public void setDialog(String dialog) {
        this.dialog = dialog;
    }

    public static class ResultBean {
        /**
         * upload_ok : true
         * save_path : 1711/10/532cd90c5f4fab390318762cd5bd6458.jpg
         * show_path : /data/upload/avatar/1711/10/532cd90c5f4fab390318762cd5bd6458.jpg?1510306012
         */

        private boolean upload_ok;
        private String save_path;
        private String show_path;

        public boolean isUpload_ok() {
            return upload_ok;
        }

        public void setUpload_ok(boolean upload_ok) {
            this.upload_ok = upload_ok;
        }

        public String getSave_path() {
            return save_path;
        }

        public void setSave_path(String save_path) {
            this.save_path = save_path;
        }

        public String getShow_path() {
            return show_path;
        }

        public void setShow_path(String show_path) {
            this.show_path = show_path;
        }
    }
}
