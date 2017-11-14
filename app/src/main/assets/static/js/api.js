//域名
var host = "http://tczp.wshoto.com/index.php?m=phone&";
var userid;
var utype;
var username = '';
//接口
var port = {
    //公司信息
    companyInfo: host + 'c=company&a=cominfo',
    //编辑公司
    editCompany: host + 'c=company&a=editCompany',
    //是否认证
    renzheng: host + 'c=company&a=isrenzheng',
    //认证
    renzhengs: host + 'c=company&a=renzheng',
    //招聘职位列表
    getJobsList: host + 'c=company&a=getJobsList',
    //招聘职位详情
    getJobsInfo: host + 'c=company&a=getJobsInfo',
    //配置信息
    category: host + 'c=company&a=category',
    //职位发布
    pubJobs: host + 'c=company&a=pubJobs',
    //关闭职位
    deleteJob: host + 'c=company&a=deleteJob',
    //编辑职位信息
    getJobsInfo: host + 'c=company&a=getJobsInfo',
    //投递简历列表
    getResumeList: host + 'c=company&a=getResumeList',
    //简历详情
    resumeInfo: host + 'c=company&a=resumeInfo',
    //收藏人才列表
    getFavoritesList: host + 'c=company&a=getFavoritesList',
    //收藏资讯列表
    collection_topline_list: host + 'c=favorites&a=collection_topline_list',
    //全部简历列表
    resumeList: host + 'c=company&a=resumeList',
    //面试
    isFace: host + 'c=company&a=isFace',
    //公告通知
    show_notice_list: host + 'c=news&a=show_notice_list',
    //谁看过我
    getSeeList: host + 'c=personal&a=getSeeList',
    //消息提醒
    show_message_list: host + 'c=news&a=show_message_list',
    //投递管理
    getRecicveList: host + 'c=company&a=getRecicveList',
    //上传企业认证
    certificate_img: host + 'c=upload&a=certificate_img',
    //修改手机
    phone: host + 'c=Personal&a=phone_update',
    //修改密码
    psd: host + 'c=personalinfo&a=save_password',

    //反馈
    feedback: host + 'c=Feedback&a=feedback',
    // 我的全部投递
    allDelivery: host + 'c=PersonalJobsend&a=send_total',
    //收藏人才
    favoritesresume: host + 'c=company&a=favoritesresume',
};

//请求
function ajaxRun(url, params, async, method, callbackfun, withloading) {
    //console.log('url:',url, 'data:',params, 'cb:',callback, 'async:',async, 'type:',method);
    if (withloading == undefined) withloading = -1;
    loading(withloading);
    $.ajax({
        url: url,
        type: method,
        data: params,
        async: async,
        dataType: 'jsonp',
        success: function (res) {
            toLogin(res);
            callbackfun(res);
            loading(0);
        },
        error: function (res) {
            loading(0);
            console.log(url + 'ajax-error');
        }
    });
}

function loading(act) {
    if (act !== 0 && act !== 1) return;
    var loadingDom = $('.loading').length;
    if (loadingDom == 0) {
        var html = '<style>.loading{display:none;position:fixed;top:40px;right:0;bottom:0px;left:0;background-color:rgba(255,255,255,0.8);z-index:9999;}.loading .loader{width:35px;height:35px;position:absolute;left:50%;top:50%;margin:-17px auto auto -17px;}@-webkit-keyframes spin-rotate{0%{-webkit-transform:rotate(0);transform:rotate(0)}50%{-webkit-transform:rotate(180deg);transform:rotate(180deg)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}@keyframes spin-rotate{0%{-webkit-transform:rotate(0);transform:rotate(0)}50%{-webkit-transform:rotate(180deg);transform:rotate(180deg)}100%{-webkit-transform:rotate(360deg);transform:rotate(360deg)}}.semi-circle-spin{position:relative;width:35px;height:35px;overflow:hidden}.semi-circle-spin>div{position:absolute;border-width:0;border-radius:100%;-webkit-animation:spin-rotate .6s 0s infinite linear;animation:spin-rotate .6s 0s infinite linear;background-image:-webkit-linear-gradient(transparent 0,transparent 70%,#41a6ff 30%,#41a6ff 100%);background-image:linear-gradient(transparent 0,transparent 70%,#41a6ff 30%,#41a6ff 100%);width:100%;height:100%}</style><div class="loading"><div class="loader"><div class="semi-circle-spin"><div></div></div></div></div>';
        $('body').append(html);
    }
    if (act === 1) {
        // window.loadingT=setTimeout(function(){
        $('.loading').show();
        // },600);
        setTimeout(function () {
            $('.loading').fadeOut('fast');
        }, 5000);
    }
    if (act === 0) {
        // clearTimeout(window.loadingT);
        $('.loading').fadeOut('fast');
    }
}

function GetRequest() {
    var url = location.search;
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for (var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]] = unescape(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}

function uid(mobile, password) {
//	var mobile = '18021568897';
    var hostUrl = host + 'c=Login&a=login';
    var parmas = {mobile: mobile, password: password}
    ajaxRun(hostUrl, parmas, true, 'GET', function (data) {
        if (data.statuscode == 1) {
            var userid = data.result.uid;
            var utype = data.result.utype;
            localStorage.setItem('userid', userid);
            localStorage.setItem('utype', utype);
            location.href = '../../index.html';
        } else {
            rzState = 0;
            console.log(data.msg);
            $('.nojob').show();
            $('.jobsUl').hide();
            mui.toast(data.msg, {duration: 1000});
        }

    });
    return userid;
}

function wxUid(openid, nickname, avatar, outh_utype) {
//	var mobile = '18021568897';
    var hostUrl = host + 'c=Login&a=login';
    var parmas = {type: 'weixin', openid: openid, nickname: nickname, avatar: avatar, outh_utype: outh_utype}
    console.log(parmas);
    ajaxRun(hostUrl, parmas, true, 'GET', function (data) {
        if (data.statuscode == 1) {
            var userid = data.result.uid;
            var utype = data.result.utype;
            localStorage.setItem('userid', userid);
            localStorage.setItem('utype', utype);
            location.href = '../../index.html';
        } else {
            rzState = 0;
            console.log(data.msg);
            $('.nojob').show();
            $('.jobsUl').hide();
            mui.toast(data.msg, {duration: 1000});
        }

    });
    return userid;
}

function qqUid(qqnum, nickname, avatar, outh_utype) {
//	var mobile = '18021568897';
    var hostUrl = host + 'c=Login&a=login';
    var parmas = {type: 'qq', qqnum: qqnum, nickname: nickname, avatar: avatar, outh_utype: outh_utype}
    ajaxRun(hostUrl, parmas, true, 'GET', function (data) {
        if (data.statuscode == 1) {
            var userid = data.result.uid;
            var utype = data.result.utype;
            localStorage.setItem('userid', userid);
            localStorage.setItem('utype', utype);
            location.href = '../../index.html';
        } else {
            rzState = 0;
            console.log(data.msg);
            $('.nojob').show();
            $('.jobsUl').hide();
            mui.toast(data.msg, {duration: 1000});
        }

    });
    return userid;
}

console.log(localStorage);
if (localStorage.userid !== null || localStorage.userid != undefined) {
    userid = localStorage.userid;
    utype = localStorage.utype;
}

// var vConsole = new VConsole();
// console.log(vConsole);

function getJsPath(jsName) {
    var scriptArr = document.getElementsByTagName('script');
    for (var i = 0; i < scriptArr.length; i++) {
        if (scriptArr[i].src.split("/")[scriptArr[i].src.split('/').length - 1] == jsName) {
            return scriptArr[i].src.replace(jsName, '');
            break;
        }
    }
    return "";
}

function toLogin(data) {
    console.log(window);
    if (data.statuscode == -1) {
        var mypath = getJsPath('api.js');
        location.href = mypath + '../../personal/register/login.html';
        mui.toast('请先登录');
    }
}

function doLocalStorage(type, key, val) {
    //此方法暂时用localstorage方便web开发调试，app打包时改成jsbriage方法
    if (type == 'set') {
        localStorage.setItem(key, val);
    }
    if (type == 'get') {
        localStorage.getItem(key);
    }
    if (type == 'remove') {
        localStorage.removeItem(key);
    }
}