$(function(){
    $('.mid_bar span').click(function(e){
        e.preventDefault();
        active=$(this);
        index= $('.mid_bar span').index($(this));
        getStyle(active);
        getShowHide(index);
    });
    $('.msg_container span').click(function(event){
        event.preventDefault();
        active=$(this);
        index= $('.msg_container span').index($(this));
        getStyle(active);
        getShowHide(index);
    });
    $('.chose_list span').click(function(e){
        e.preventDefault();
        $(this).parent().siblings().children().removeClass('active');
        $(this).addClass('active');
        var index= $('.chose_list span').index($(this));
        getShowHide(index);
    });
    function getActive(active){
        active.siblings().removeClass('active');
        active.addClass('active');
    }
    function getStyle(active){
        active.parent().siblings().children().removeClass('active');
        active.addClass('active');
    }
    function getShowHide(index){
        $('.msg').each(function(index1){
            if(index1==index){
                $(this).show().siblings().hide();
            }
        })
    }
    mui('.mui-scroll-wrapper').scroll({
        scrollY: true, //是否竖向滚动
        scrollX: false, //是否横向滚动
        startX: 0, //初始化时滚动至x
        startY: 0, //初始化时滚动至y
        indicators: true, //是否显示滚动条
        deceleration:0.0006, //阻尼系数,系数越小滑动越灵敏
        bounce: true //是否启用回弹
    });
    mui('.mui-scroll-wrapper').on('tap','a',function(){
        url=$(this).attr('data-href');
        if(url==""||url==null||url==undefined){
            return
        }

        location.href=url;
    });
    mui('.navBar').on('tap','a',function(){
        url=$(this).attr('data-href');
        if(url==""||url==null||url==undefined){
            return
        }
        i=url.lastIndexOf('/');
        idUrl=url.substring(i+1,url.length+1);
        location.href=url;
    });
}());