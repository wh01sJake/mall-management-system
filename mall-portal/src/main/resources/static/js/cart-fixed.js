$(document).ready(function() {
    // 立即发起购物车列表请求
    
    $.ajax({
        url: '/cart/list',
        type: 'POST',
        dataType: 'json',
        timeout: 10000, // 10秒超时
        beforeSend: function() {
            console.log('Cart list request started');
        },
        success: function(result) {
            console.log('Cart list success:', result);
            
            if (result.code == 0) {
                console.log('Cart data received:', result.data);
                
                if (result.data && result.data.length > 0) {
                    console.log('Processing', result.data.length, 'cart items');
                    
                    $(result.data).each(function(index, item) {
                        console.log('Processing item', index + 1, ':', item);
                        
                        var html = '';
                        html += '<div id="' + item.id + '" class="list list-item">';
                        
                        if (item.checked) {
                            html += '    <div class="select"><i class="fa fa-check checked"></i></div>';
                        } else {
                            html += '    <div class="select"><i class="fa fa-check"></i></div>';
                        }
                        
                        html += '    <div class="good-img"><img src="' + item.productMainImage + '" alt=""></div>';
                        html += '    <div class="good-name">' + item.productName + '</div>';
                        html += '    <div class="good-price">' + item.productPrice + '元</div>';
                        html += '    <div class="good-num">';
                        html += '        <div class="num-input">';
                        html += '            <button class="minus">-</button>';
                        html += '            <input type="text" value="' + item.quantity + '" class="num-value" readonly>';
                        html += '            <button class="plus">+</button>';
                        html += '        </div>';
                        html += '    </div>';
                        
                        var totalPrice = (parseFloat(item.productPrice) * parseInt(item.quantity)).toFixed(2);
                        html += '    <div class="good-total-price">' + totalPrice + '元</div>';
                        html += '    <div class="operation"><i class="fa fa-times"></i></div>';
                        html += '</div>';
                        
                        $('#cartTitle').after(html);
                    });
                    
                    console.log('All cart items rendered');

                    // 初始化总价等
                    updateCartSummary();
                    checkAllStatus();
                    
                } else {
                    console.log('Cart is empty');
                    $('#cartTitle').after('<div class="empty-cart-message">购物车为空，快去选购商品吧！</div>');
                }
            } else {
                console.error('Cart API returned error:', result);
                alert('加载购物车失败: ' + (result.message || '未知错误'));
            }
        },
        error: function(xhr, status, error) {
            console.error('Cart list AJAX error:', {
                status: xhr.status,
                statusText: xhr.statusText,
                responseText: xhr.responseText,
                error: error,
                ajaxStatus: status
            });
            
            if (xhr.status === 401) {
                console.log('User not logged in, redirecting...');
                alert('用户未登录，即将跳转到登录页面');
                window.location.href = '/page/login';
            } else if (xhr.status === 0) {
                console.error('Network error or request cancelled');
                alert('网络错误，请检查网络连接');
            } else {
                alert('加载购物车失败，状态码: ' + xhr.status);
            }
        },
        complete: function() {
            console.log('Cart list request completed');
        }
    });
    
    // 事件处理函数
    setupEventHandlers();
});

function updateCartSummary() {
    console.log('Updating cart summary...');
    
    var items = $('.list-item i[class*="fa-check"]');
    var checkedItems = $('.list-item i[class$="checked"]').parents('.select').siblings('.good-total-price');
    
    var totalPrice = 0;
    for (var i = 0; i < checkedItems.length; i++) {
        var priceText = checkedItems[i].innerHTML.replace('元', '');
        totalPrice += parseFloat(priceText) || 0;
    }
    
    $('.sum-price').html(totalPrice.toFixed(2));
    $('.select-count').html(checkedItems.length);
    $('.all-count').html(items.length);
    
    console.log('Cart summary updated - Total:', totalPrice, 'Selected:', checkedItems.length, 'All:', items.length);
}

function setupEventHandlers() {
    console.log('Setting up event handlers...');

    // 单个商品勾选/取消勾选
    $('#cartList').on('click', '.fa-check', function () {
        console.log('Individual check clicked');

        var $this = $(this);
        var currentClass = $this.attr('class');
        var checked = currentClass.includes('checked') ? 0 : 1;
        var cartItemId = $this.parents('.list-item').attr('id');

        console.log('Cart item ID:', cartItemId, 'New checked status:', checked);

        // 先更新UI
        if (checked === 1) {
            $this.attr('class', 'fa fa-check checked');
        } else {
            $this.attr('class', 'fa fa-check');
        }

        // 发送更新请求
        $.ajax({
            url: '/cart/update',
            type: 'POST',
            data: {
                'id': cartItemId,
                'isChecked': checked
            },
            dataType: 'json',
            success: function(result) {
                console.log('Update check status success:', result);
                if (result.code === 0) {
                    updateCartSummary();
                    checkAllStatus();
                } else {
                    console.error('Update failed:', result.message);
                    // 恢复原状态
                    if (checked === 1) {
                        $this.attr('class', 'fa fa-check');
                    } else {
                        $this.attr('class', 'fa fa-check checked');
                    }
                }
            },
            error: function(xhr, status, error) {
                console.error('Update check status error:', error);
                // 恢复原状态
                if (checked === 1) {
                    $this.attr('class', 'fa fa-check');
                } else {
                    $this.attr('class', 'fa fa-check checked');
                }
                alert('更新失败，请重试');
            }
        });
    });

    // 全选/取消全选
    $('#check-all').click(function () {
        console.log('Check all clicked');

        var $this = $(this);
        var currentClass = $this.attr('class');
        var checked = currentClass.includes('checked') ? 0 : 1;

        console.log('Check all status:', checked);

        // 先更新UI
        if (checked === 1) {
            $('.fa-check').attr('class', 'fa fa-check checked');
        } else {
            $('.fa-check').attr('class', 'fa fa-check');
        }

        // 发送批量更新请求
        $.ajax({
            url: '/cart/updateCheckedAll',
            type: 'POST',
            data: {
                'checked': checked
            },
            dataType: 'json',
            success: function(result) {
                console.log('Update all check status success:', result);
                if (result.code === 0) {
                    updateCartSummary();
                } else {
                    console.error('Update all failed:', result.message);
                    alert('批量更新失败: ' + result.message);
                }
            },
            error: function(xhr, status, error) {
                console.error('Update all check status error:', error);
                alert('批量更新失败，请重试');
            }
        });
    });

    // 删除商品
    $('#cartList').on('click', '.fa-times', function () {
        console.log('Delete item clicked');

        var $this = $(this);
        var cartItemId = $this.parents('.list-item').attr('id');

        console.log('Deleting cart item ID:', cartItemId);

        if (confirm('Are you sure you want to remove this item?')) {
            $.ajax({
                url: '/cart/deleteById',
                type: 'POST',
                data: {
                    'id': cartItemId
                },
                dataType: 'json',
                success: function(result) {
                    console.log('Delete item success:', result);
                    if (result.code === 0) {
                        $this.parents('.list-item').remove();
                        updateCartSummary();
                        checkAllStatus();

                        // Check if cart is empty
                        if ($('.list-item').length === 0) {
                            $('#cartTitle').after('<div class="empty-cart-message">Your cart is empty. Start shopping now!</div>');
                        }
                    } else {
                        console.error('Delete failed:', result.message);
                        alert('Delete failed: ' + result.message);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Delete item error:', error);
                    alert('Delete failed, please try again');
                }
            });
        }
    });

    // 数量减少
    $('#cartList').on('click', 'button.minus', function () {
        console.log('Minus button clicked');

        var $this = $(this);
        var $input = $this.siblings('input');
        var currentValue = parseInt($input.val()) || 1;
        var newValue = Math.max(1, currentValue - 1); // Minimum value is 1
        var cartItemId = $this.parents('.list-item').attr('id');

        console.log('Current quantity:', currentValue, 'New quantity:', newValue);

        if (newValue !== currentValue) {
            // Send update request
            $.ajax({
                url: '/cart/update',
                type: 'POST',
                data: {
                    'id': cartItemId,
                    'quantity': newValue
                },
                dataType: 'json',
                success: function(result) {
                    console.log('Update quantity success:', result);
                    if (result.code === 0) {
                        $input.val(newValue);
                        updateQuantityAndPrice($this, newValue);
                    } else {
                        console.error('Update quantity failed:', result.message);
                        alert('更新数量失败: ' + result.message);
                    }
                },
                error: function(xhr, status, error) {
                    console.error('Update quantity error:', error);
                    alert('更新数量失败，请重试');
                }
            });
        }
    });

    // 数量增加
    $('#cartList').on('click', 'button.plus', function () {
        console.log('Plus button clicked');

        var $this = $(this);
        var $input = $this.siblings('input');
        var currentValue = parseInt($input.val()) || 1;
        var newValue = currentValue + 1;
        var cartItemId = $this.parents('.list-item').attr('id');

        console.log('Current quantity:', currentValue, 'New quantity:', newValue);

        // Send update request
        $.ajax({
            url: '/cart/update',
            type: 'POST',
            data: {
                'id': cartItemId,
                'quantity': newValue
            },
            dataType: 'json',
            success: function(result) {
                console.log('Update quantity success:', result);
                if (result.code === 0) {
                    $input.val(newValue);
                    updateQuantityAndPrice($this, newValue);
                } else {
                    console.error('Update quantity failed:', result.message);
                    alert('更新数量失败: ' + result.message);
                }
            },
            error: function(xhr, status, error) {
                console.error('Update quantity error:', error);
                alert('更新数量失败，请重试');
            }
        });
    });

    // 鼠标悬停效果
    $('#cartList').on('mouseover', '.fa-check', function(){
        if (!$(this).attr('class').includes('checked')) {
            $(this).css('color', '#ff6a00');
        }
    });

    $('#cartList').on('mouseleave', '.fa-check', function(){
        $(this).css('color','#fff');
    });

    console.log('Event handlers setup completed');
}

function checkAllStatus() {
    console.log('Checking all status...');

    var allItems = $('.list-item i[class*="fa-check"]').length;
    var checkedItems = $('.list-item i[class$="checked"]').length;

    console.log('All items:', allItems, 'Checked items:', checkedItems);

    if (allItems > 0 && allItems === checkedItems) {
        $('#check-all').attr('class', 'fa fa-check checked');
        console.log('All items checked, updating check-all to checked');
    } else {
        $('#check-all').attr('class', 'fa fa-check');
        console.log('Not all items checked, updating check-all to unchecked');
    }
}

function updateQuantityAndPrice($button, newQuantity) {
    console.log('Updating quantity and price for quantity:', newQuantity);

    var $listItem = $button.parents('.list-item');
    var $priceElement = $listItem.find('.good-price');
    var $totalPriceElement = $listItem.find('.good-total-price');

    // Get unit price (remove currency symbol)
    var unitPriceText = $priceElement.text().replace('€', '').replace('元', '');
    var unitPrice = parseFloat(unitPriceText) || 0;

    // Calculate new total price
    var newTotalPrice = (unitPrice * newQuantity).toFixed(2);

    console.log('Unit price:', unitPrice, 'New total price:', newTotalPrice);

    // Update total price display
    $totalPriceElement.text('€' + newTotalPrice);

    // Update cart summary
    updateCartSummary();
}
