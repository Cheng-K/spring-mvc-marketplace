function addClickListenerToProductCards() {
  const allProductCards = document.querySelectorAll(".product-card");
  allProductCards.forEach((card) => {
    card.addEventListener("click", (event) => {
      const productId = card.getAttribute("data-product-id");
      window.location.assign(`/products/${productId}`);
    });
  });
}

function addClickListenerToCategoryList() {
  const allCategoryListItems = document.querySelectorAll(".category-list-item");
  allCategoryListItems.forEach((category) => {
    category.addEventListener("click", (event) => {
      const categoryId = category.getAttribute("data-category-id");
      window.location.assign(`/products/categories/${categoryId}`);
    });
  });
}

addClickListenerToProductCards();
addClickListenerToCategoryList();
