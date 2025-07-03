// VapeHub Modern JavaScript

document.addEventListener('DOMContentLoaded', function() {
    // Initialize all components
    initMobileMenu();
    initDropdowns();
    initCartFunctionality();
    initSearch();
    initNewsletterForms();

    // Load cart count on page load
    updateCartCount();
});

// Mobile Menu Toggle
function initMobileMenu() {
    const mobileToggle = document.createElement('button');
    mobileToggle.className = 'mobile-menu-toggle';
    mobileToggle.innerHTML = '<i class="fas fa-bars"></i>';
    
    const mainNav = document.querySelector('.main-nav');
    const navMenu = document.querySelector('.nav-menu');
    
    if (mainNav && navMenu) {
        mainNav.insertBefore(mobileToggle, navMenu);
        
        mobileToggle.addEventListener('click', function() {
            navMenu.classList.toggle('active');
            const icon = this.querySelector('i');
            if (navMenu.classList.contains('active')) {
                icon.className = 'fas fa-times';
            } else {
                icon.className = 'fas fa-bars';
            }
        });
    }
}

// Dropdown Menus
function initDropdowns() {
    const dropdowns = document.querySelectorAll('.dropdown');
    
    dropdowns.forEach(dropdown => {
        const link = dropdown.querySelector('.nav-link');
        const menu = dropdown.querySelector('.dropdown-menu');
        
        if (window.innerWidth <= 768) {
            // Mobile dropdown behavior
            link.addEventListener('click', function(e) {
                e.preventDefault();
                dropdown.classList.toggle('active');
            });
        } else {
            // Desktop hover behavior
            dropdown.addEventListener('mouseenter', function() {
                if (menu) menu.style.display = 'block';
            });
            
            dropdown.addEventListener('mouseleave', function() {
                if (menu) menu.style.display = 'none';
            });
        }
    });
}

// Cart Functionality
function initCartFunctionality() {
    const cartCount = document.querySelector('.cart-count');
    const cartDropdown = document.querySelector('.cart-dropdown');
    
    // Update cart count from localStorage
    updateCartCount();
    
    // Cart hover functionality
    const cartMenu = document.querySelector('.cart-menu');
    if (cartMenu && cartDropdown) {
        cartMenu.addEventListener('mouseenter', function() {
            updateCartDropdown();
        });
    }
}

function updateCartCount() {
    // Try to get cart count from backend first
    $.get('/cart/list', function(result) {
        if (result.code === 0 && result.data) {
            const totalItems = result.data.reduce((sum, item) => sum + (item.quantity || 0), 0);
            updateCartCountDisplay(totalItems);
        }
    }).fail(function(xhr) {
        // If user not logged in or backend fails, fallback to localStorage
        if (xhr.status === 401) {
            updateCartCountDisplay(0);
        } else {
            // Fallback to localStorage for offline functionality
            const cart = JSON.parse(localStorage.getItem('cart') || '[]');
            const totalItems = cart.reduce((sum, item) => sum + (item.quantity || 1), 0);
            updateCartCountDisplay(totalItems);
        }
    });
}

function updateCartCountDisplay(count) {
    const cartCountElements = document.querySelectorAll('.cart-count');
    cartCountElements.forEach(element => {
        element.textContent = count;
    });
}

function updateCartCountFromBackend() {
    updateCartCount();
}

function updateCartDropdown() {
    const cartDropdown = document.querySelector('.cart-dropdown');
    if (!cartDropdown) return;
    
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    
    if (cart.length === 0) {
        cartDropdown.innerHTML = `
            <p>Your cart is empty</p>
            <a href="/product_list.html" class="btn btn-primary">Shop our products</a>
        `;
    } else {
        let html = '<div class="cart-items">';
        let total = 0;
        
        cart.forEach(item => {
            const itemTotal = item.price * (item.quantity || 1);
            total += itemTotal;
            
            html += `
                <div class="cart-item">
                    <img src="${item.image || '/images/default-product.jpg'}" alt="${item.name}" width="50" height="50">
                    <div class="cart-item-info">
                        <h4>${item.name}</h4>
                        <p>€${item.price} x ${item.quantity || 1}</p>
                    </div>
                </div>
            `;
        });
        
        html += `
            </div>
            <div class="cart-total">
                <strong>Total: €${total.toFixed(2)}</strong>
            </div>
            <div class="cart-actions">
                <a href="/cart_list.html" class="btn btn-secondary">View Cart</a>
                <a href="/order_confirm.html" class="btn btn-primary">Checkout</a>
            </div>
        `;
        
        cartDropdown.innerHTML = html;
    }
}

// Search Functionality
function initSearch() {
    const searchForm = document.querySelector('.search-bar');
    const searchInput = document.querySelector('.search-input');
    const searchBtn = document.querySelector('.search-btn');
    
    if (searchForm && searchInput && searchBtn) {
        searchBtn.addEventListener('click', function(e) {
            e.preventDefault();
            performSearch();
        });
        
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                performSearch();
            }
        });
    }
}

function performSearch() {
    const searchInput = document.querySelector('.search-input');
    const query = searchInput.value.trim();
    
    if (query) {
        // Redirect to product list with search query
        window.location.href = `/page/product/list?search=${encodeURIComponent(query)}`;
    }
}

// Newsletter Forms
function initNewsletterForms() {
    const newsletterForms = document.querySelectorAll('.newsletter-form, .footer-newsletter');
    
    newsletterForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            handleNewsletterSubmit(this);
        });
    });
}

function handleNewsletterSubmit(form) {
    const emailInput = form.querySelector('input[type="email"]');
    const email = emailInput.value.trim();
    
    if (!email) {
        showMessage('Please enter your email address', 'error');
        return;
    }
    
    if (!isValidEmail(email)) {
        showMessage('Please enter a valid email address', 'error');
        return;
    }
    
    // Simulate newsletter subscription
    showMessage('Thank you for subscribing to our newsletter!', 'success');
    emailInput.value = '';
}

// Utility Functions
function isValidEmail(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
}

function showMessage(message, type = 'info') {
    // Create message element
    const messageEl = document.createElement('div');
    messageEl.className = `message message-${type}`;
    messageEl.textContent = message;
    
    // Style the message
    Object.assign(messageEl.style, {
        position: 'fixed',
        top: '20px',
        right: '20px',
        padding: '15px 20px',
        borderRadius: '5px',
        color: 'white',
        fontWeight: '500',
        zIndex: '10000',
        maxWidth: '300px',
        boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
    });
    
    // Set background color based on type
    switch (type) {
        case 'success':
            messageEl.style.backgroundColor = '#28a745';
            break;
        case 'error':
            messageEl.style.backgroundColor = '#dc3545';
            break;
        case 'warning':
            messageEl.style.backgroundColor = '#ffc107';
            messageEl.style.color = '#333';
            break;
        default:
            messageEl.style.backgroundColor = '#17a2b8';
    }
    
    // Add to page
    document.body.appendChild(messageEl);
    
    // Remove after 5 seconds
    setTimeout(() => {
        if (messageEl.parentNode) {
            messageEl.parentNode.removeChild(messageEl);
        }
    }, 5000);
}

// Add to cart functionality - uses backend API
function addToCart(productId, productName, productPrice, productImage, quantity = 1) {
    // Ensure quantity is a valid number
    quantity = parseInt(quantity) || 1;

    // Use backend cart API
    $.ajax({
        url: '/cart/add',
        type: 'POST',
        data: {
            productId: productId,
            quantity: quantity
        },
        success: function(result) {
            console.log('Add to cart result:', result);
            if (result.code === 0) {
                // Show appropriate message based on quantity
                const message = quantity === 1 ?
                    `${productName} added to cart!` :
                    `${quantity} x ${productName} added to cart!`;
                showMessage(message, 'success');

                // Update cart count from backend
                updateCartCountFromBackend();
            } else {
                showMessage(result.msg || 'Failed to add to cart', 'error');
            }
        },
        error: function(xhr) {
            console.error('Add to cart failed:', xhr);
            if (xhr.status === 401 || (xhr.responseJSON && xhr.responseJSON.code === 401)) {
                showMessage('Please login to add items to cart', 'warning');
                // Redirect to login page with return URL
                setTimeout(() => {
                    window.location.href = '/page/login?redirect=' + encodeURIComponent(window.location.pathname);
                }, 1500);
            } else {
                showMessage('Failed to add to cart. Please try again.', 'error');
            }
        }
    });
}

// Remove from cart functionality
function removeFromCart(productId) {
    let cart = JSON.parse(localStorage.getItem('cart') || '[]');
    cart = cart.filter(item => item.id !== productId);
    
    localStorage.setItem('cart', JSON.stringify(cart));
    updateCartCount();
    updateCartDropdown();
    showMessage('Item removed from cart', 'info');
}

// Update cart item quantity
function updateCartQuantity(productId, newQuantity) {
    const cart = JSON.parse(localStorage.getItem('cart') || '[]');
    const item = cart.find(item => item.id === productId);
    
    if (item) {
        if (newQuantity <= 0) {
            removeFromCart(productId);
        } else {
            item.quantity = newQuantity;
            localStorage.setItem('cart', JSON.stringify(cart));
            updateCartCount();
            updateCartDropdown();
        }
    }
}

// Smooth scrolling for anchor links
document.addEventListener('click', function(e) {
    if (e.target.matches('a[href^="#"]')) {
        e.preventDefault();
        const target = document.querySelector(e.target.getAttribute('href'));
        if (target) {
            target.scrollIntoView({
                behavior: 'smooth',
                block: 'start'
            });
        }
    }
});

// Handle window resize
window.addEventListener('resize', function() {
    // Reinitialize dropdowns on resize
    initDropdowns();
    
    // Close mobile menu if window is resized to desktop
    if (window.innerWidth > 768) {
        const navMenu = document.querySelector('.nav-menu');
        const mobileToggle = document.querySelector('.mobile-menu-toggle');
        
        if (navMenu) navMenu.classList.remove('active');
        if (mobileToggle) {
            const icon = mobileToggle.querySelector('i');
            if (icon) icon.className = 'fas fa-bars';
        }
    }
});
