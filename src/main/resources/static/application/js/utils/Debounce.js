class Debounce {

  constructor(time = 300, fun = null) {
    this.time = time;
    this.callback = fun;
    this.run = false;
    this.timeOutId = null;
  }

    start() {
    if (this.isRunning()) {
      this.stop();
    }
    this.run = true;
    this.timeOutId = setTimeout(() => this.executeCallback(), this.time);
  }

  stop() {
    if (this.isRunning()) {
      clearTimeout(this.timeOutId);
      this.timeOutId = null;
      this.run = false;
    }
  }


  isRunning() {
    return this.timeOutId !== null && this.run === true;
  }


  isCallbackDefined() {
    return this.callback !== null && typeof this.callback === 'function';
  }

  executeCallback() {
    if (this.isCallbackDefined()) {
      this.callback();
      this.clear();
    }

  }

  clear() {
    this.run = false;
    this.timeOutId = null;
  }


}