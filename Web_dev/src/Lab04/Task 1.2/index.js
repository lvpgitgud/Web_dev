        // Product data
        const grid = document.getElementById("productsGrid");
            
        const products = [
            { id: 1, name: 'Laptop', price: 999.99, image: '💻' },

            { id: 2, name: 'Smartphone', price: 699.99, image: '📱' },

            { id: 3, name: 'Headphones', price: 199.99, image: '🎧' },

            { id: 4, name: 'Smartwatch', price: 299.99, image: '⌚' }
                
        ];

        // Cart array
        
        let cart = [];

        // TODO: Implement functions
        
        function addToCart(productId) {
            // TODO: Add product to cart or increase quantity
            const item = products.find(product => product.id == productId);
            const existingItem = cart.find(product => product.id == productId);

            if (existingItem) {
                existingItem.quantity++;
            } else {
                cart.push({ ...item, quantity: 1 });
            }

            renderCart();
        }
        
        function removeFromCart(itemId) {
            // TODO: Remove item from cart
            cart = cart.filter(item => item.id !== itemId);
            renderCart();
        }
        
        function updateQuantity(productId, change) {
            // TODO: Update item quantity (change is +1 or -1)
            const item = cart.find(product => product.id == productId);
            if (!item) return;

            item.quantity += change;
            if (item.quantity <= 0) {
                removeFromCart(productId);
            } else {
                renderCart();
            }

        }
        
        function calculateTotal() {
            let total = 0;
            cart.forEach(item => {
                total += item.price * item.quantity;
            });
            return total.toFixed(2);
        }
    
        
        function renderProducts() {

            products.forEach(p => {
                const div = document.createElement("div");
                div.className = "product-card";
                div.innerHTML = `
                    <div class="product-image">${p.image}</div>
                    <div class="product-name">${p.name}</div>
                    <div class="product-price">$${p.price.toFixed(2)}</div>
                    <button class="add-to-cart-btn" onclick="addToCart(${p.id})">Add to Cart</button>
                `;
                grid.appendChild(div);
            });
        }
        
        function renderCart() {
            
            const cartItems = document.getElementById("cartItems");
            const cartTotal = document.getElementById("cartTotal");
            const cartCount = document.getElementById("cartCount");

            cartItems.innerHTML = "";

            if (cart.length === 0) {
                cartItems.innerHTML = "<p>Your cart is empty.</p>";
                cartTotal.textContent = "0.00";
                cartCount.textContent = "0";
                return;
            }

            cart.forEach(item => {
                const div = document.createElement("div");
                div.className = "cart-item";
                div.innerHTML = `
                    <div>${item.name}</div>
                    <div>$${item.price.toFixed(2)}</div>
                    <div class="quantity-controls">
                        <button onclick="updateQuantity(${item.id}, -1)">-</button>
                        <div>${item.quantity}</div>
                        <button onclick="updateQuantity(${item.id}, 1)">+</button>
                    </div>
                    <button onclick="removeFromCart(${item.id})">Remove</button>
                `;
                cartItems.appendChild(div);
            });

            cartTotal.textContent = calculateTotal();
            let totalCount = 0;

            cart.forEach(item => {
            totalCount += item.quantity;
            });

            cartCount.textContent = totalCount;
    
        }

        function toggleCart() {
            const cartSection = document.getElementById("cartSection");
            cartSection.style.display = (cartSection.style.display === "none" || !cartSection.style.display)
                ? "block"
                : "none";
        }
            
        // Initialize
        renderProducts();