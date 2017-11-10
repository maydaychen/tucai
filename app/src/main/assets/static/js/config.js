	mui('.mui-scroll-wrapper').scroll({
		 scrollY: true, //是否竖向滚动
		 scrollX: false, //是否横向滚动
		 startX: 0, //初始化时滚动至x
		 startY: 0, //初始化时滚动至y
		 indicators: true, //是否显示滚动条
		 deceleration:0.0006, //阻尼系数,系数越小滑动越灵敏
		 bounce: true //是否启用回弹
	});
	var aniShow = "pop-in";
	mui.plusReady(function() {
		//读取本地存储，检查是否为首次启动
		var showGuide = plus.storage.getItem("lauchFlag");
		//仅支持竖屏显示
		plus.screen.lockOrientation("portrait-primary");
		if(showGuide) {
			//有值，说明已经显示过了，无需显示；
			//关闭splash页面；
			plus.navigator.closeSplashscreen();
			plus.navigator.setFullscreen(false);
		} else {
			//显示启动导航
			location.href='';
			// mui.openWindow({
			// 	id: '',
			// 	url: '',
			// 	styles: {
			// 		popGesture: "none"
			// 	},
			// 	show: {
			// 		aniShow: 'none'
			// 	},
			// 	waiting: {
			// 		autoShow: false
			// 	}
			// });
		}
		_self = plus.webview.currentWebview();
		var titleView = _self.getNavigationbar();
		if(!titleView) {
			titleView = plus.webview.getLaunchWebview().getNavigationbar();
		}
		titleView.interceptTouchEvent(true);
	});			
	//只有ios支持的功能需要在Android平台隐藏；
	if(mui.os.android) {
		var list = document.querySelectorAll('.ios-only');
		if(list) {
			for(var i = 0; i < list.length; i++) {
				list[i].style.display = 'none';
			}
		}
		//Android平台暂时使用slide-in-right动画
		if(parseFloat(mui.os.version) < 4.4) {
			aniShow = "slide-in-right";
		}
	}
	
	//主列表点击事件
//	mui('#pages').on('tap', 'a', function() {
//		var self=this;
//		var id = this.getAttribute("data-wid");
//		if(!id) {
//			id = this.getAttribute('href');
//		}
//		var href = this.getAttribute('href');
//		if(href==""||href==null||href==undefined){
//			return;
//		}
//		//非plus环境，直接走href跳转
//		if(!mui.os.plus){
//			location.href = href;
//			return;
//		}
//		var titleType = this.getAttribute("data-title-type");
//		var webview_style = {
//			popGesture: "close"
//		}				
//		var extras = {};	
//		var webview = plus.webview.create(this.href,id,webview_style,extras);
//		webview.addEventListener("titleUpdate",function () {
//			setTimeout(function () {
//				webview.show(aniShow,400);
//			},50);
//		});	
//	});
	mui('body').on('tap', 'a', function() {
		var self=this;
		var href = this.getAttribute('data-href');
		if(href==""||href==null||href==undefined){
			return;
		}
		location.href=href+'.html';
		// mui.openWindow({
		// 	url: ''+href+'.html'
		// });
	})