
function backPage() {
	location.href = "../index.html";
}
function backPage2() {
	location.href = "../../index.html";
}

// function share(articalId){
// 	console.log("分享");
// 	$(".cd-popup").addClass('is-visible');
// //	var url = host + 'c=tcbk&a=shareOrZan';
// //	var parmas = {
// //		articalId: articalId,
// //		type: "share"
// //	}
// //	ajaxRun(url,parmas,true,'GET',function (data) {
// //		if(data.statuscode==1){
// //			console.log("分享");
// //			$(".cd-popup").addClass('is-visible');
// //
// //		} else {
// //		 	rzState=0;
// //			console.log('未认证');
// //			mui.toast('未认证',{ duration:1000});
// //		}
// // 	});
// }

function agree(articalId) {
	var url = host + 'c=tcbk&a=shareOrZan';
	var parmas = {
		articalId: articalId,
		type: "zan"
	}
	ajaxRun(url,parmas,true,'GET',function (data) {
		if(data.statuscode==1){
			console.log("点赞");
			$('#' + articalId).toggleClass("agreeState");
			$('#' + articalId).next().html(data.result);
		} else {
		 	rzState=0;
			mui.toast(data.msg,{ duration:1000}); 
		}					
 	});
}
function agreeArtical(articalId) {
	var url = host + 'c=tcbk&a=shareOrZan';
	var parmas = {
		articalId: articalId,
		type: "zan"
	}
	ajaxRun(url,parmas,true,'GET',function (data) {
		if(data.statuscode==1){
			console.log("点赞");
			$('.agreeBind').toggleClass("agreeState");
		} else {
		 	rzState=0;
			mui.toast(data.msg,{ duration:1000}); 
		}					
 	});
}

function topAgree(articalId) {
	var url = host + 'c=topline&a=collectAndZan';
	var parmas = {
		id: articalId,
		type: "zan"
	}
	ajaxRun(url,parmas,true,'GET',function (data) {
		if(data.statuscode==1){
        	$('.agreeBind').toggleClass("agreeState");	
           	articalvue._data.articalInfo.click = data.result;
		} else {
		 	rzState=0;
			mui.toast(data.msg,{ duration:1000}); 
		}					
 	});
}
function topCollect(articalId,state) {
	console.log(state)
	var method = '';
	if(state != 1) {
		state = 1;
		method = "add";		
	} else {
		state = 0;
		method = 'canlce';
	}
	var url = host + 'c=topline&a=collectAndZan';
	var parmas = {
		id: articalId,
		type: "collect",
		collect_type: method
	}
	ajaxRun(url,parmas,true,'GET',function (data) {
		if(data.statuscode==1){
        	$('.collectBind').toggleClass("collectState");
        	articalvue._data.articalInfo.collect = state;
		} else {
		 	rzState=0;
			mui.toast(data.msg,{ duration:1000}); 
		}					
 	});		
}
	
function checkPhone(mobile){ 
    if(!(/^1(3|4|5|7|8)\d{9}$/.test(mobile))){ 
    	mui.toast('手机号码有误，请重填',{ duration:1000}); 
//      alert("手机号码有误，请重填");  
        return false; 
    } 
}

function getCode(mobile) {
	var parmas = {mobile:mobile};
	var getCodeUrl = host + 'c=login&a=sendSms';
	if(!(/^1(3|4|5|7|8)\d{9}$/.test(mobile))){ 
    	mui.toast('手机号码有误，请重填',{ duration:1000});
        return false; 
	} else {
		var countdown=59;
	    var o=$(".getCode");
	    var iCount = setInterval (function () {
	        o.val(countdown-- +"s后重试");
	        o.attr('disabled',true);
	        if(countdown==0){
	            o.attr('disabled',false);
	            o.val("获取验证码");
	            clearInterval(iCount);//清除倒计时
	            countdown=60;//设置倒计时时间
	        };
	    }, 1000);
		ajaxRun(getCodeUrl,parmas,true,'GET',function (data) {					
			if(data.statuscode==1){
			}else{
				mui.toast(data.msg,{ duration:1000}); 
			}														
	 	});	
	}
}