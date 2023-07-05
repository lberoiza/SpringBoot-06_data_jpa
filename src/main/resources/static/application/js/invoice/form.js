class InvoiceForm {

  FETCH_URL_PRODUCT_LIST_BY_NAME = "/product/find_by_name/";

  INPUT_PRODUCT_ID = "product";
  DATALIST_PRODUCT_ID = "datalist-product";

  constructor() {
    this.selectedProducts = [];
    this.currentProductDataList = [];
    this.debounce = new Debounce(500, () => this.getProducts());
    this.initializeListeners()

  }


  initializeListeners() {
    const inputProduct = document.getElementById(this.INPUT_PRODUCT_ID);
    inputProduct.addEventListener('input', () => this.debounce.start());
  }


  getProductSearchTerm() {
    return document.getElementById(this.INPUT_PRODUCT_ID).value.trim();
  }


  getProducts() {
    let productSearchTerm = this.getProductSearchTerm();
    if (this.productInputHasChanged()) {
      fetch(this.FETCH_URL_PRODUCT_LIST_BY_NAME + productSearchTerm)
        .then(res => res.json())
        .then(result => this.loadDatalistProduct(result));
    }
  }


  productInputHasChanged() {
    const productInputValue = this.getProductSearchTerm();
    return productInputValue.length > 0 && this.getSelectedProduct() === null;
  }


  getSelectedProduct() {
    const inputValue = this.getProductSearchTerm();
    return this.currentProductDataList.find(product => product.name === inputValue) ?? null;
  }


  loadDatalistProduct(result = []) {
    this.currentProductDataList = result;
    const datalistProduct = document.getElementById(this.DATALIST_PRODUCT_ID);
    datalistProduct.innerHTML = "";
    result.forEach(product => {
      const optionProduct = document.createElement("option");
      optionProduct.value = product.name;
      datalistProduct.appendChild(optionProduct);
    });
  }

}

