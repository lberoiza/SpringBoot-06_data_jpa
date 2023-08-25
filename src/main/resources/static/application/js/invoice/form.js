class InvoiceForm {

  URL_PRODUCT_LIST_BY_NAME_ID = "ajaxUrlProductList";
  FORM_INVOICE_ID = "form-new-invoice";
  INPUT_PRODUCT_ID = "product";
  DATALIST_PRODUCT_ID = "datalist-product";
  BUTTON_ADD_PRODUCT = "add-product";
  ITEM_ROW_TEMPLATE_ID = "item-invoice-model";
  TABLE_INVOICE_TBODY_ID = "table-invoice-tbody";
  TABLE_INVOICE_ITEM_ROW_ID_PREFIX = "item-row"
  INPUT_QUANTITY_ID_PREFIX = "item-quantity";
  SPAN_AMOUNT_ID_PREFIX = "item-total";
  SPAN_INVOICE_TOTAL = "invoice-total"

  constructor() {
    this.selectedProductMap = new Map();
    this.currentProductDataList = [];
    this.debounce = new Debounce(500, () => this.getProducts());
    this.initializeComponents();
    this.initializeListeners()
    this.ajaxUrlProdutcList = document.getElementById(this.URL_PRODUCT_LIST_BY_NAME_ID).value;
  }

  initializeComponents() {
    this.form = document.getElementById(this.FORM_INVOICE_ID);
    this.inputSearchProduct = document.getElementById(this.INPUT_PRODUCT_ID);
    this.datalistProduct = document.getElementById(this.DATALIST_PRODUCT_ID);
    this.buttonAddProduct = document.getElementById(this.BUTTON_ADD_PRODUCT);
    this.rowModel = document.getElementById(this.ITEM_ROW_TEMPLATE_ID);
    this.invoiceTableTbody = document.getElementById(this.TABLE_INVOICE_TBODY_ID);
    this.spanInvoiceTotal = document.getElementById(this.SPAN_INVOICE_TOTAL);
  }


  initializeListeners() {
    this.form.addEventListener('submit', (e) => this.submitInvoice(e));
    this.inputSearchProduct.addEventListener('input', () => this.debounce.start());
    this.buttonAddProduct.addEventListener('click', (e) => this.addProduct())
  }


  getProductSearchTerm() {
    return this.inputSearchProduct.value.trim();
  }


  getQuantityInput(productId) {
    return document.getElementById(`${this.INPUT_QUANTITY_ID_PREFIX}-${productId}`);
  }


  getAmountSpan(productId) {
    return document.getElementById(`${this.SPAN_AMOUNT_ID_PREFIX}-${productId}`);
  }

  getItemRow(productId) {
    return document.getElementById(`${this.TABLE_INVOICE_ITEM_ROW_ID_PREFIX}-${productId}`)
  }

  getProducts() {
    let productSearchTerm = this.getProductSearchTerm();
    if (this.productInputHasChanged()) {
      fetch(this.ajaxUrlProdutcList + productSearchTerm)
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
      let invoiceItem = new InvoiceItem();
      if (this.selectedProductMap.has(productId)) {
        invoiceItem = this.selectedProductMap.get(productId)
        this.quantityChangesAndUpdateAmount(productId, invoiceItem.quantity + 1);
      } else {
        invoiceItem.setProduct(selectedProduct);
        this.selectedProductMap.set(productId, invoiceItem);
        this.createNewRow(invoiceItem);
        this.updateInvoiceTotal();
      }
      Alert.showSuccess(`Product ${invoiceItem.product.name} added.`)
      this.clearDataListAndInput();
    } else {
      Alert.showWarning(`Product not in the list.`);
    }
    this.inputSearchProduct.focus();
  }


  createNewRow(invoiceItem) {
    let row = this.rowModel.innerHTML;
    row = row.replace(/{product_id}/g, invoiceItem.product.id);
    row = row.replace(/{product_name}/g, invoiceItem.product.name);
    row = row.replace(/{product_price}/g, invoiceItem.product.price);
    const rowFragment = document.createRange().createContextualFragment(row).querySelector('tbody tr');
    this.invoiceTableTbody.appendChild(rowFragment);
  }


  clearDataListAndInput() {
    this.inputSearchProduct.value = "";
  }


  quantityChangesAndUpdateAmount(productId, quantity) {
    Alert.clearMessages();
    const invoiceItem = this.selectedProductMap.get(productId)
    const quantityUpdated = invoiceItem.setQuantity(quantity);

    if (!quantityUpdated) {
      Alert.showWarning(`The Quantity of ${invoiceItem.product.name}  can not be less than 1.`);
    } else {
      Alert.showInfo(`The Quantity of ${invoiceItem.product.name} has changed.`)
    }

    this.getQuantityInput(productId).value = invoiceItem.quantity;
    this.updateAmount(invoiceItem)
  }

  updateAmount(invoiceItem) {
    const productId = invoiceItem.product.id;
    const quantity = invoiceItem.quantity;
    const price = invoiceItem.product.price;
    const span = this.getAmountSpan(productId)
    span.textContent = `${quantity * price}`;
    this.updateInvoiceTotal();
  }


  deleteRow(productId) {
    this.selectedProductMap.delete(productId);
    this.getItemRow(productId).remove();
    this.updateInvoiceTotal();
  }


  updateInvoiceTotal() {
    let total = 0;
    this.selectedProductMap.forEach((invoiceItem, productId,) => {
      total += invoiceItem.quantity * invoiceItem.product.price;
    })
    this.spanInvoiceTotal.textContent = total;
  }

  submitInvoice(event) {
    // event.preventDefault();
  }


}


class InvoiceItem {
  constructor() {
    this.quantity = 1;
    this.product = {
      id: 0,
      description: '',
      name: '',
      price: 0
    }
  }

  setQuantity(q) {
    const quantity = parseInt(q);
    if (quantity >= 1) {
      this.quantity = quantity
      return true;
    }
    return false;
  }

  setProduct(product = {}) {
    this.product.id = parseInt(product.id ?? 0);
    this.product.price = parseInt(product.price ?? 0);
    this.product.description = product.description ?? '';
    this.product.name = product.name ?? '';
  }

}
