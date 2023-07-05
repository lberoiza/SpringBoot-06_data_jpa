class Alert {

  static MESSAGE_DIV_ID = "message-bar";
  static CLASS_SUCCESS = "alert-success";
  static CLASS_WARNING = "alert-warning";
  static CLASS_ERROR = "alert-danger";
  static CLASS_INFO = 'alert-info';


  static createMessageElement(msg = "", clear = true, cssClass = '') {
    const messageBarContent = document.getElementById(this.MESSAGE_DIV_ID);
    if (clear) {
      messageBarContent.innerHTML = ""
    }
    const div = document.createElement('div');
    div.textContent = msg;
    div.classList.add('alert', cssClass);
    messageBarContent.appendChild(div);
  }


  static showSuccess(msg = "", clear = true) {
    this.createMessageElement(msg, clear, Alert.CLASS_SUCCESS)
  }


  static showWarning(msg = "", clear = true) {
    this.createMessageElement(msg, clear, Alert.CLASS_WARNING)
  }


  static showError(msg = "", clear = true) {
    this.createMessageElement(msg, clear, Alert.CLASS_ERROR)
  }


  static showInfo(msg = "", clear = true) {
    this.createMessageElement(msg, clear, Alert.CLASS_INFO)
  }


}