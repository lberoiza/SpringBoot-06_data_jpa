class InvoiceForm {

  FETCH_URL_PRODUCT_LIST_BY_NAME = "/product/find_by_name/";

  FORM_INVOICE_ID = "form-new-invoice";
  INPUT_PRODUCT_ID = "product";
  DATALIST_PRODUCT_ID = "datalist-product";
  BUTTON_ADD_PRODUCT = "add-product";

  constructor() {
    this.selectedProductMap = new Map();
    this.currentProductDataList = [];
    this.debounce = new Debounce(500, () => this.getProducts());
    this.initializeComponents();
    this.initializeListeners()

  }

  initializeComponents() {
    this.form = document.getElementById(this.FORM_INVOICE_ID);
    this.inputSearchProduct = document.getElementById(this.INPUT_PRODUCT_ID);
    this.datalistProduct = document.getElementById(this.DATALIST_PRODUCT_ID);
    this.buttonAddProduct = document.getElementById(this.BUTTON_ADD_PRODUCT);
  }


  initializeListeners() {
    this.form.addEventListener('submit', (e) => this.submitInvoice(e));
    this.inputSearchProduct.addEventListener('input', () => this.debounce.start());
    this.buttonAddProduct.addEventListener('click', (e) => this.addProduct())
  }


  getProductSearchTerm() {
    return this.inputSearchProduct.value.trim();
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
    this.datalistProduct.innerHTML = "";
    result.forEach(product => {
      const optionProduct = document.createElement("option");
      optionProduct.value = product.name;
      this.datalistProduct.appendChild(optionProduct);
    });
  }


  addProduct() {
    const selectedProduct = this.getSelectedProduct();
    if (selectedProduct) {
      const productId = selectedProduct.id
      let invoiceItem = {};
      if (this.selectedProductMap.has(productId)) {
        invoiceItem = this.selectedProductMap.get(productId)
        invoiceItem.quantity += 1;
      } else {
        invoiceItem = {
          quantity: 1,
          product: selectedProduct
        }
        this.selectedProductMap.set(productId, invoiceItem);
      }
      Alert.showSuccess(`Product ${invoiceItem.product.name} added.`)
      this.clearDataListAndInput();
    } else {
      Alert.showWarning(`Product not in the list.`);
    }
    this.inputSearchProduct.focus();
  }


  clearDataListAndInput() {
    // this.datalistProduct.innerHTML = "";
    this.inputSearchProduct.value = "";
  }

  submitInvoice(event) {
    event.preventDefault();
    console.log("submit");
    console.log("list of products", this.selectedProductMap);

  }


}

