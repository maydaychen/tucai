package tucai.wshoto.com.response;

/**
 * Created by Weshine on 2017/5/16.
 */

public class StoreBean {

    /**
     * status : 1
     * message : {"id":"60","uid":null,"uniacid":"1","title":"九洲大药房","thumb":"images/1/2017/05/Awwww1PTNytpwSylYz6P16TYjS2LI7.jpg","content":"营业时间：8:00-21:00","phone":"1575555555","qq":"4156487785","province":"江苏省","city":"无锡市","dist":"北塘区","address":"江苏省无锡市南长区梁溪区金星街道红星路541号(无锡古罗马大酒店北)","lng":"120.297494","lat":"31.545493","created_at":"2017-05-13 14:13:07","updated_at":"2017-05-16 10:14:07","status":"1","deleted":"0","username":"admin","password":"NDRjODZhNDY3YTY5ZWE1MmJlY2E0YTc2NDM0NDJjYjk=","salt":"wecrN5JK"}
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
         * id : 60
         * uid : null
         * uniacid : 1
         * title : 九洲大药房
         * thumb : images/1/2017/05/Awwww1PTNytpwSylYz6P16TYjS2LI7.jpg
         * content : 营业时间：8:00-21:00
         * phone : 1575555555
         * qq : 4156487785
         * province : 江苏省
         * city : 无锡市
         * dist : 北塘区
         * address : 江苏省无锡市南长区梁溪区金星街道红星路541号(无锡古罗马大酒店北)
         * lng : 120.297494
         * lat : 31.545493
         * created_at : 2017-05-13 14:13:07
         * updated_at : 2017-05-16 10:14:07
         * status : 1
         * deleted : 0
         * username : admin
         * password : NDRjODZhNDY3YTY5ZWE1MmJlY2E0YTc2NDM0NDJjYjk=
         * salt : wecrN5JK
         */

        private String id;
        private Object uid;
        private String uniacid;
        private String title;
        private String thumb;
        private String content;
        private String phone;
        private String qq;
        private String province;
        private String city;
        private String dist;
        private String address;
        private String lng;
        private String lat;
        private String created_at;
        private String updated_at;
        private String status;
        private String deleted;
        private String username;
        private String password;
        private String salt;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Object getUid() {
            return uid;
        }

        public void setUid(Object uid) {
            this.uid = uid;
        }

        public String getUniacid() {
            return uniacid;
        }

        public void setUniacid(String uniacid) {
            this.uniacid = uniacid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDist() {
            return dist;
        }

        public void setDist(String dist) {
            this.dist = dist;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }
    }
}
