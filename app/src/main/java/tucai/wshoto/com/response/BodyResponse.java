package tucai.wshoto.com.response;

import java.util.List;

/**
 * Created by Weshine on 2017/5/11.
 */

public class BodyResponse {

    /**
     * status : 1
     * message : [{"title":"头部","lists":[{"id":"1","uniacid":"1","title":"头疼","description":"我额济","body_type_id":"1","created_at":"2017-05-09 18:49:11","updated_at":"2017-05-09 18:49:14","status":"1","sex":"1"},{"id":"2","uniacid":"1","title":"脖子疼","description":"sdfa","body_type_id":"1","created_at":"2017-05-09 19:39:54","updated_at":"2017-05-09 19:39:56","status":"1","sex":"1"}]},{"title":"胸部","lists":[{"id":"3","uniacid":"1","title":"胸疼","description":"","body_type_id":"2","created_at":"2017-05-09 14:11:58","updated_at":"2017-05-10 14:12:01","status":"1","sex":"1"},{"id":"4","uniacid":"1","title":"肿胀","description":"sss","body_type_id":"2","created_at":"2017-05-10 14:12:25","updated_at":"2017-05-10 14:12:28","status":"1","sex":"1"}]},{"title":"四肢","lists":[]}]
     */

    private int status;
    private List<MessageBean> message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<MessageBean> getMessage() {
        return message;
    }

    public void setMessage(List<MessageBean> message) {
        this.message = message;
    }

    public static class MessageBean {
        /**
         * title : 头部
         * lists : [{"id":"1","uniacid":"1","title":"头疼","description":"我额济","body_type_id":"1","created_at":"2017-05-09 18:49:11","updated_at":"2017-05-09 18:49:14","status":"1","sex":"1"},{"id":"2","uniacid":"1","title":"脖子疼","description":"sdfa","body_type_id":"1","created_at":"2017-05-09 19:39:54","updated_at":"2017-05-09 19:39:56","status":"1","sex":"1"}]
         */

        private String title;
        private List<ListsBean> lists;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ListsBean> getLists() {
            return lists;
        }

        public void setLists(List<ListsBean> lists) {
            this.lists = lists;
        }

        public static class ListsBean {
            /**
             * id : 1
             * uniacid : 1
             * title : 头疼
             * description : 我额济
             * body_type_id : 1
             * created_at : 2017-05-09 18:49:11
             * updated_at : 2017-05-09 18:49:14
             * status : 1
             * sex : 1
             */

            private String id;
            private String uniacid;
            private String title;
            private String description;
            private String body_type_id;
            private String created_at;
            private String updated_at;
            private String status;
            private String sex;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
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

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public String getBody_type_id() {
                return body_type_id;
            }

            public void setBody_type_id(String body_type_id) {
                this.body_type_id = body_type_id;
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

            public String getSex() {
                return sex;
            }

            public void setSex(String sex) {
                this.sex = sex;
            }
        }
    }
}
