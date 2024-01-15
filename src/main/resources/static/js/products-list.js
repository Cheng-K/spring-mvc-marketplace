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

function calculateDaysAgoForProducts() {
  const allProductCards = document.querySelectorAll(".product-card");
  allProductCards.forEach((product) => {
    const daysago = product.querySelector(".card-body *[data-listed-on]");
    const listedDate = new Date(daysago.dataset.listedOn);
    const numberOfDaysAgo = Math.round(
      (Date.now() - listedDate) / (1000 * 60 * 60 * 24)
    );
    if (numberOfDaysAgo == 0) {
      daysago.textContent = `Listed today`;
    } else {
      daysago.textContent = `Listed ${numberOfDaysAgo} days ago`;
    }
  });
}

function sortProductsByLatest() {
  const currentURL = new URL(window.location.href);
  currentURL.searchParams.set("sortBy", "latest");
  currentURL.searchParams.delete("order");
  window.location.href = currentURL.toString();
}

function sortProductsByPrice(order) {
  const currentURL = new URL(window.location.href);
  currentURL.searchParams.set("sortBy", "price");
  currentURL.searchParams.set("order", order);
  window.location.href = currentURL.toString();
}

calculateDaysAgoForProducts();
addClickListenerToProductCards();
addClickListenerToCategoryList();
