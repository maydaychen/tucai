package tucai.wshoto.com.response;

/**
 * Created by Weshine on 2017/5/10.
 */

public class HospitalResponse {

    /**
     * status : 1
     * message : {"id":"5","name":"华山医院","thumb":"images/1/2017/05/HBpx64o2xz6z3Zx606Z6OAgXz6gB0x.jpg","description":"华山医院荣获货全国十大文明医院","special":"儿科","zhuanjia":"汪涵，韩寒","lng":"116.403851","lat":"39.915177","displayorder":"0","created_at":"2017-05-09 15:37:50","updated_at":"2017-05-14 16:48:57","uniacid":"1","deleted":"0","username":"","password":"ZjNhYjg5NTFlNWU2ZDRlZjFmYjgyYjU5ZDY5MjE5YzA=","salt":null,"type":"1","is_sanjia":"1"}
     */

    private int status;
    private MessageBean message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MessageBean getMessage() {
        return message;
    }

    public void setMessage(MessageBean message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * id : 5
         * name : 华山医院
         * thumb : images/1/2017/05/HBpx64o2xz6z3Zx606Z6OAgXz6gB0x.jpg
         * description : 华山医院荣获货全国十大文明医院
         * special : 儿科
         * zhuanjia : 汪涵，韩寒
         * lng : 116.403851
         * lat : 39.915177
         * displayorder : 0
         * created_at : 2017-05-09 15:37:50
         * updated_at : 2017-05-14 16:48:57
         * uniacid : 1
         * deleted : 0
         * username :
         * password : ZjNhYjg5NTFlNWU2ZDRlZjFmYjgyYjU5ZDY5MjE5YzA=
         * salt : null
         * type : 1
         * is_sanjia : 1
         */

        private String id;
        private String name;
        private String thumb;
        private String description;
        private String special;
        private String zhuanjia;
        private String lng;
        private String lat;
        private String displayorder;
        private String created_at;
        private String updated_at;
        private String uniacid;
        private String deleted;
        private String username;
        private String password;
        private Object salt;
        private String type;
        private String is_sanjia;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getSpecial() {
            return special;
        }

        public void setSpecial(String special) {
            this.special = special;
        }

        public String getZhuanjia() {
            return zhuanjia;
        }

        public void setZhuanjia(String zhuanjia) {
            this.zhuanjia = zhuanjia;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getDisplayorder() {
            return displayorder;
        }

        public void setDisplayorder(String displayorder) {
            this.displayorder = displayorder;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public String getUniacid() {
            return uniacid;
        }

        public void setUniacid(String uniacid) {
            this.uniacid = uniacid;
        }

        public String getDeleted() {
            return deleted;
        }

        public void setDeleted(String deleted) {
            this.deleted = deleted;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Object getSalt() {
            return salt;
        }

        public void setSalt(Object salt) {
            this.salt = salt;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getIs_sanjia() {
            return is_sanjia;
        }

        public void setIs_sanjia(String is_sanjia) {
            this.is_sanjia = is_sanjia;
        }
    }
}
