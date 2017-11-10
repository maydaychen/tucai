
var phoneReg = /^(((13[0-9]{1})|(15[0-9]{1})|(18[0-9]{1}))+\d{8})$/; 
var psdReg=/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,21}$/;
function timetrans(date) {
  var date = new Date(date*1000);
  var Y = date.getFullYear() + '-';
  var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1) + '-';
  var D = (date.getDate() < 10 ? '0' + (date.getDate()) : date.getDate()) + ' ';
  var h = (date.getHours() < 10 ? '0' + date.getHours() : date.getHours()) + ':';
  var m = (date.getMinutes() < 10 ? '0' + date.getMinutes() : date.getMinutes());
  return Y + M + D + h + m;  
}
//向上弹出
jQuery.fn.slideUp = function () {
	var flag=$(this).hasClass('animated slideUp speed');
	var ele=$(this);
	if(flag){
		ele.removeClass('animated slideUp speed');
	}else{
		ele.addClass('animated slideUp speed');
		setTimeout(function(){
			ele.css('transform','translateX(0%)');
			ele.removeClass('animated slideUp speed');
		},500);
	}
}
//向下收回
jQuery.fn.slideDown = function () {
	var flag=$(this).hasClass('animated slideDown speed');
	var ele=$(this);
	if(flag==true){
		ele.removeClass('animated slideDown speed');
	}else{
		ele.addClass('animated slideDown speed');
		setTimeout(function(){
			ele.css('transform','translateX(100%)');
			ele.removeClass('animated slideDown speed');
		},500);
	}
}
//向左边弹出
jQuery.fn.slideLeft = function () {
	var flag=$(this).hasClass('animated slideLeft speed');
	var ele=$(this);
	if(flag){
		ele.removeClass('animated slideLeft speed');
	}else{
		ele.addClass('animated slideLeft speed');
		setTimeout(function(){
			ele.css('transform','translateX(0%)');
			ele.removeClass('animated slideLeft speed');
		},500);
	}
}
//向右边弹回
jQuery.fn.slideRight = function () {
	var flag=$(this).hasClass('animated slideRight speed');
	var ele=$(this);
	if(flag){
		ele.removeClass('animated slideRight speed');
	}else{
		ele.addClass('animated slideRight speed');
		setTimeout(function(){
			ele.css('transform','translateX(100%)');
			ele.removeClass('animated slideRight speed');
		},500);
	}
}
//渐入效果
jQuery.fn.fadeInDown = function () {
	var flag=$(this).hasClass('animated fadeInDown speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated fadeInDown speed');
	}else{
		$(this).addClass('animated fadeInDown speed');
		setTimeout(function(){
			ele.removeClass('animated fadeInDown speed');
		},500);
	}
}
//渐入效果
jQuery.fn.fadeInUp = function () {
	var flag=$(this).hasClass('animated fadeInUp speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated fadeInUp speed');
	}else{
		$(this).addClass('animated fadeInUp speed');
		setTimeout(function(){
			ele.removeClass('animated fadeInUp speed');
		},500);
	}
}
//渐出效果
jQuery.fn.fadeOutUp = function () {
	var flag=$(this).hasClass('animated fadeOutUp speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated fadeOutUp speed');
	}else{
		$(this).addClass('animated fadeOutUp speed');
		setTimeout(function(){
			ele.hide().removeClass('animated fadeOutUp speed');
		},500);
	}
}
//渐入效果
jQuery.fn.fadeOutDown = function () {
	var flag=$(this).hasClass('animated fadeOutDown speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated fadeOutDown speed');
	}else{
		$(this).addClass('animated fadeOutDown speed');
		setTimeout(function(){
			ele.hide().removeClass('animated fadeOutDown speed');
		},500);
	}
}

//弹性放大效果
jQuery.fn.bounceIn = function () {
	var flag=$(this).hasClass('animated bounceIn speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated bounceIn speed');
	}else{
		$(this).addClass('animated bounceIn speed');
		setTimeout(function(){
			ele.removeClass('animated bounceIn speed');
		},500);
	}
}

//从右渐入效果
jQuery.fn.fadeInRight = function () {
	var flag=$(this).hasClass('animated fadeInRight speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated fadeInRight speed');
	}else{
		$(this).addClass('animated fadeInRight speed');
		setTimeout(function(){
			ele.removeClass('animated fadeInRight speed');
		},500);
	}
}
//向右渐出效果
jQuery.fn.fadeOutRight = function () {
	var flag=$(this).hasClass('animated fadeOutRight speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated fadeOutRight speed');
	}else{
		$(this).addClass('animated fadeOutRight speed');
		setTimeout(function(){
			ele.hide().removeClass('animated fadeOutRight speed');
		},500);
	}
}
//向左渐出效果
jQuery.fn.fadeOutLeft = function () {
	var flag=$(this).hasClass('animated fadeOutLeft speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated fadeOutLeft speed');
	}else{
		$(this).addClass('animated fadeOutLeft speed');
		setTimeout(function(){
			ele.hide().removeClass('animated fadeOutLeft speed');
		},500);
	}
}
//从左渐入效果
jQuery.fn.fadeInLeft = function () {
	var flag=$(this).hasClass('animated fadeInRight speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated fadeInLeft speed');
	}else{
		$(this).addClass('animated fadeInLeft speed');
		setTimeout(function(){
			ele.removeClass('animated fadeInLeft speed');
		},500);
	}
}

jQuery.fn.slideInLeft = function () {
	var flag=$(this).hasClass('animated slideInLeft speed');
	var ele=$(this);
	if(flag){
		ele.removeClass('animated slideInLeft speed');
	}else{
		ele.addClass('animated slideInLeft speed');
		setTimeout(function(){
			ele.removeClass('animated slideInLeft speed');
		},500);
	}
}
jQuery.fn.slideOutRight = function () {
	var flag=$(this).hasClass('animated slideOutRight speed');
	var ele=$(this);
	if(flag){
		$(this).removeClass('animated slideOutRight speed');
	}else{
		$(this).addClass('animated slideOutRight speed');
		setTimeout(function(){
			ele.hide().removeClass('animated slideOutRight speed');
		},500);
	}
}

			

					  