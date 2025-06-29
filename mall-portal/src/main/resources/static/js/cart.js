// 添加页面加载完成检查
console.log('cart.js loaded at:', new Date().toLocaleTimeString());

// 检查jQuery是否加载
if (typeof $ === 'undefined') {
    console.error('jQuery is not loaded!');
    alert('jQuery未加载，购物车功能无法正常工作');
} else {
    console.log('jQuery is loaded successfully');
}

// 检查layui是否加载
if (typeof layui === 'undefined') {
    console.error('layui is not loaded!');
    alert('layui未加载，部分功能可能无法正常工作');
} else {
    console.log('layui is loaded successfully');
}

// 使用更简单的方式，确保代码执行
$(document).ready(function() {
    console.log('Document ready, initializing cart...');

    layui.use(['layer'], function () {
        var layer = layui.layer;
        console.log('layui layer loaded, starting cart logic...');

    $.post(
        '/cart/list',
        function (result) {
            console.log('Cart list response:', result);
            if (result.code == 0) {
                console.log('Cart data:', result.data);
                if (result.data && result.data.length > 0) {
                    $(result.data).each(function () {
                        var html = '';
                        html+='<div id="'+this.id+'" class="list list-item">'
                        if (this.checked) {
                            html+='    <div class="select"><i class="fa fa-check checked"></i></div>'
                        } else {
                            html+='    <div class="select"><i class="fa fa-check"></i></div>'
                        }
                        html+='    <div class="good-img"><img src="'+this.productMainImage+'" alt=""></div>'
                        html+='    <div class="good-name">'+this.productName+'</div>'
                        html+='    <div class="good-price">'+this.productPrice+'元</div>'
                        html+='    <div class="good-num">'
                        html+='        <div class="num-input">'
                        html+='            <button class="minus">-</button>'
                        html+='            <input type="text" value="'+this.quantity+'" class="num-value">'
                        html+='                <button class="plus">+</button>'
                        html+='        </div>'
                        html+='    </div>'
                        html+='    <div class="good-total-price">3199元</div>'
                        html+='    <div class="operation"><i class="fa fa-times"></i></div>'
                        html+='</div>'
                        $('#cartTitle').after(html);

                    });
                    //初始化总价, 总选择数, 总条数;
                    doPrice();
                    doCheckAll();
                } else {
                    console.log('Cart is empty');
                }
            } else {
                console.error('Cart list error:', result);
            }

        },
        'json'
    ).fail(function(xhr, status, error) {
        console.error('AJAX request failed:', {
            status: xhr.status,
            statusText: xhr.statusText,
            responseText: xhr.responseText,
            error: error
        });
        if (xhr.status === 401) {
            console.log('User not logged in, redirecting to login page');
            location.href = '/page/login';
        }
    });



    //全选/选择框的鼠标移上变个颜色
    $('.fa-check').mouseover(function(){
        if($(this).attr('class')!='fa fa-check checked') {
            $(this).css('color', '#ff6a00');
        }
    })
    $('.fa-check').mouseleave(function(){
        $(this).css('color','#fff');
    })

    //普通勾选
    $('#cartList').on('click', '.fa-check', function () {
        var cla=$(this).attr('class');
        var checked;
        if(cla!='fa fa-check checked'){
            $(this).attr('class','fa fa-check checked');
            checked = 1;
        }else{
            $(this).attr('class','fa fa-check');
            checked = 0;
        }

        var id = $(this).parents('.list-item').attr('id');

        $.post(
            '/cart/update',
            {'id': id, 'checked': checked},
            function (result) {
                console.log(result);
                if (result.code == 0) {
                    mylayer.okMsg(result.msg);
                }
            },
            'json'
        );
        doPrice();
        doCheckAll();

    })

    //全选框勾选
    $('#check-all').click(function () {
        var checked;
        var cla=$(this).attr('class');
        if(cla!='fa fa-check checked'){
            $('.fa-check').attr('class','fa fa-check checked');
            checked = 1;
        }else{
            $('.fa-check').attr('class','fa fa-check');
            checked = 0;
        }
        $.post(
            '/cart/updateCheckedAll',
            {'checked': checked},
            function (result) {
                console.log(result);
            },
            'json'
        );

        doPrice();
    })

    //检查是否全选
    function doCheckAll(){
        var allitem=$('.list-item i[class*="fa-check"]').length;
        var checkeditem=$('.list-item i[class$="checked"]').length;
        if(allitem!=checkeditem){
            $('#check-all').attr('class','fa fa-check');
        }else{
            $('#check-all').attr('class','fa fa-check checked');
        }
    }

    //加减按钮
    $('#cartList').on('click', 'button.minus', function () {
        var nowvalue=$(this).siblings('input').val();
        nowvalue=parseInt(nowvalue);
        var currentvalue=0;
        nowvalue<=1?currentvalue=1:currentvalue=nowvalue-1;
        $(this).siblings('input').val(currentvalue);

        //计算当前的小计
        var danjia=parseFloat($(this).parents('.good-num').siblings('.good-price').html());
        var xiaoji=danjia*currentvalue;
        $(this).parents('.good-num').siblings('.good-total-price').html(xiaoji+'元');

        //更新总价
        doPrice();
    })

    $('#cartList').on('click', 'button.plus', function () {
        var nowvalue=$(this).siblings('input').val();
        nowvalue=parseInt(nowvalue);
        var currentvalue=nowvalue+1;

        var id = $(this).parents('.list-item').attr('id');
        var that = this;
        $.post(
            '/cart/update',
            {'id': id, 'quantity': currentvalue},
            function (result) {
                console.log(result);
                if (result.code == 0) {
                    $(that).siblings('input').val(currentvalue);
                    //计算当前的小计
                    var danjia=parseFloat($(that).parents('.good-num').siblings('.good-price').html());
                    var xiaoji=danjia*currentvalue;
                    $(that).parents('.good-num').siblings('.good-total-price').html(xiaoji+'元');

                    //更新总价
                    doPrice();
                }
            },
            'json'
        );
    })

    $('#cartList').on('click', 'i.fa-times', function () {
        var id = $(this).parents('.list-item').attr('id');
        var that = this;
        layer.confirm(
            '您确认要删除么?',
            {icon: 3},
            function (index) {
                $.post(
                    '/cart/deleteById',
                    {'id': id},
                    function (result) {
                        console.log(result);
                        if (result.code == 0) {
                            mylayer.okMsg(result.msg);
                            $(that).parents('.list-item').remove();
                            //更新总价
                            doPrice();
                        }
                    },
                    'json'
                );
            }
        );
    })

    function doPrice(){
        //统计所有勾选了的值;
        var items=$('.list-item i[class*="fa-check"]');
        var checkeditems=$('.list-item i[class$="checked"]').parents('.select').siblings('.good-total-price')
        var totalprice=0;
        for(var i=0;i<checkeditems.length;i++){
            totalprice+=parseFloat(checkeditems[i].innerHTML);
        }
        //改总价
        $('.sum-price').html(totalprice);
        //改选中数
        $('.select-count').html(checkeditems.length);
        //改总条数
        $('.all-count').html(items.length);

    }

    }); // end layui.use
}); // end document.ready