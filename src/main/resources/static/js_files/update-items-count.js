const updateItemsCount = (() => {
    const spanElementCartCount = document.getElementById('shopping-cart-count');

    return {
        update: () => {
            fetch("/order/shopping-cart-items-count")
                .then(response => response.json())
                .then(jsonData => {
                    spanElementCartCount.textContent = jsonData; //just a number
                });
        }
    }
})();