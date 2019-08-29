const updateItemsCount = (() => {
    const spanElementCartCount = document.getElementById('shopping-cart-count');

    return {
        update: () => {
            fetch("/order/shopping-cart-items-count")
                .then(response => response.json())
                .then(jsonData => {
                    const itemsCount = jsonData.itemsCount;
                    spanElementCartCount.textContent = itemsCount;
                });
        }
    }
})();