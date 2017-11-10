//判断浏览器
var _env = (function () {
  var f = navigator.userAgent,
    b = null,
    c = function (h, i) {
      var g = h.split(i);
      g = g.shift() + "." + g.join("");
      return g * 1
    },
    d = {
      ua: f,
      version: null,
      mobile: false,
      ios: false,
      android: false,
      windows: false,
      blackberry: false,
      meizu: false,
      weixin: false,
      wVersion: null,
      touchSupport: ("createTouch" in document),
      hashSupport: !!("onhashchange" in window),
      wshoto: false
    };
  b = f.match(/AppleWebKit.*Mobile.*\/([\.0-9]+)\s/);
  if (b !== null) {
    d.mobile = true;
    d.wVersion = c(b[1], ".")
  }
  b = f.match(/MicroMessenger\/([\.0-9]+)/);
  if (b !== null) {
    d.weixin = true;
    d.wVersion = c(b[1], ".")
  }
  b = f.indexOf("wshoto");
  if (b > 0) {
    d.wshoto = true;
  }
  b = f.match(/Android\s([\.0-9]+)/);
  if (b !== null) {
    d.android = true;
    d.version = c(b[1], ".");
    d.meizu = /M030|M031|M032|MEIZU/.test(f);
    return d
  }
  b = f.match(/i(Pod|Pad|Phone)\;.*\sOS\s([\_0-9]+)/);
  if (b !== null) {
    d.ios = true;
    d.version = c(b[2], "_");
    return d
  }
  b = f.match(/Windows\sPhone\sOS\s([\.0-9]+)/);
  if (b !== null) {
    d.windows = true;
    d.version = c(b[1], ".");
    return d
  }
  var e = {
    a: f.match(/\(BB1\d+\;\s.*\sVersion\/([\.0-9]+)\s/),
    b: f.match(/\(BlackBerry\;\s.*\sVersion\/([\.0-9]+)\s/),
    c: f.match(/^BlackBerry\d+\/([\.0-9]+)\s/),
    d: f.match(/\(PlayBook\;\s.*\sVersion\/([\.0-9]+)\s/)
  };
  for (var a in e) {
    if (e[a] !== null) {
      b = e[a];
      d.blackberry = true;
      d.version = c(b[1], ".");
      return d
    }
  }
  return d
}());

var _webapp = {

  debug : true,

  apiToken: {},

  sessionKey: {},

  log: function (logData) {

    if(_webapp.debug === true){
      if (!_env.wshoto) {
        console.log(logData);
        return ;
      }

      var str = '';
      switch (typeof logData) {
        case 'object' :
          str = JSON.stringify(logData);
          break;
        default :
          str = logData;
          break;
      }

      var $message = $('.messageApp');
      var html = '<li style=" width: 100%; padding: 0; margin: 0px; border-bottom: 1px solid #efefef;">' + str + '</li>';
      $message.append(html);
    }

    return true;
  },

  clearLog: function () {
    var $message = $('.message');
    $message.html('log clear :' + '<hr />');
  },

  checkApiToken: function () {

    //_webapp.log(_webapp.apiToken);
    //_webapp.log('typeof _webapp.apiToken');
    // _webapp.log(typeof _webapp.apiToken === 'object');
    // _webapp.log('typeof _webapp.apiToken.data');
    // _webapp.log(typeof _webapp.apiToken.data === 'object');
    // _webapp.log('_webapp.apiToken.statusCode == 1');
    // _webapp.log(_webapp.apiToken.statusCode / 1 === 1);

    if(_webapp.debug === true){
      _webapp.getApiTokenSync();
    }

    if (typeof _webapp.apiToken === 'object' && typeof _webapp.apiToken.data === 'object' && _webapp.apiToken.statusCode / 1 === 1) {
      //_webapp.log("typeof _webapp.apiToken.data.access_token !== 'undefined' ");
      //_webapp.log(typeof _webapp.apiToken.data.access_token !== 'undefined');
      if (typeof _webapp.apiToken.data.access_token !== 'undefined') {
        return true;
      }
    }

    return false;
  },

  //* eg. sessionKey = {data : {"sessionkey":"5a42277cbd1680e446e6e07ee78be19f", "timestamp":1500997527}, statusCode : '1'};
  checkSessionKey: function () {
    if(_webapp.debug === true){
      _webapp.getSessionKeySync();
    }
    // _webapp.log('_webapp.sessionKey');
    // _webapp.log(_webapp.sessionKey);
    // _webapp.log('typeof _webapp.sessionKey');
    // _webapp.log(typeof _webapp.sessionKey === 'object');
    // _webapp.log('typeof _webapp.sessionKey.data');
    // _webapp.log(typeof _webapp.sessionKey.data === 'object');
    // _webapp.log('_webapp.sessionKey.statusCode == 1');
    // _webapp.log(_webapp.sessionKey.statusCode / 1 === 1);

    if (typeof _webapp.sessionKey === 'object' && typeof _webapp.sessionKey.data === 'object' && _webapp.sessionKey.statusCode / 1 === 1) {
      // _webapp.log('typeof _webapp.sessionKey.data.sessionkey');
      // _webapp.log(typeof _webapp.sessionKey.data.sessionkey !== 'undefined');

      if (typeof _webapp.sessionKey.data.sessionkey !== 'undefined') {
        return true;
      }
    }

    return false;
  },


  init: false,    // jsBridge 初始化

  /**
   * 跳转至原生app登陆
   * @param callback
   */
  nativeLogin: function (callback) {
    var handler = 'nativeLogin';

    if (_env.ios) {
      _webapp.setupWebViewJavascriptBridge(function (bridge) {
        bridge.callHandler(handler, function (response) {
          return _webapp.callback(response, callback);
        });

        // bridge.registerHandler(handler, function (data, responseCallback) {
        //     responseCallback('nativeLogin back.');
        //     return _webapp.callback(data, callback);
        // });
      });
    }

    if (_env.android) {
      _webapp.connectWebViewJavascriptBridge(function (bridge) {
        if (_webapp.init === false) {
          //初始化
          _webapp.init = true;
          bridge.init(function (message, responseCallback) {
            var data = {
              'Javascript Responds': 'nativeLogin init!'
            };
            responseCallback(data);
          });
        }

        bridge.callHandler(handler, function (response, responseCallback) {
          // responseCallback('nativeLogin responseCallback.');
          // response = eval('(' + response + ')');
          // return _webapp.callback(response, callback);
        });

        // bridge.registerHandler(handler, function (response, responseCallback) {
        //     responseCallback('nativeLogin back.');
        //     response = eval('(' + response + ')');
        //     return _webapp.callback(response, callback);
        // });
      });
    }
  },

  /**
   * 第三方JS自动登陆调起
   * @param type wechat\qq
   * @param callback
   */
  authLogin: function (type, callback) {
    var handler = 'authLogin';

    if (_env.ios) {
      _webapp.setupWebViewJavascriptBridge(function (bridge) {
        bridge.callHandler(handler, {type: type}, function (response) {
          //return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (data) {
          return _webapp.callback(data, callback);
        });
      });
    }

    if (_env.android) {
      _webapp.connectWebViewJavascriptBridge(function (bridge) {
        if (_webapp.init === false) {
          //初始化
          _webapp.init = true;

          bridge.init(function (message, responseCallback) {
            var data = {
              'Javascript Responds': 'Wee!'
            };
            responseCallback(data);
          });
        }

        bridge.callHandler(handler, {type: type}, function (response) {
          // response = eval('(' + response + ')');
          // _webapp.callback(response, callback);
          // _webapp.log('call response', response);
        });

        bridge.registerHandler(handler, function (response, responseCallback) {
          responseCallback(response);
          response = eval('(' + response + ')');
          return _webapp.callback(response, callback);
        });

      });
    }
  },

  /**
   * 分享到
   * @param type wechat\qq\qzone\wechatMoments
   * @param callback
   */
  shareTo: function (type, callback) {
    var handler = 'shareTo';

    if (_env.ios) {
      _webapp.setupWebViewJavascriptBridge(function (bridge) {
        bridge.callHandler(handler, {type: type}, function (response) {
          //return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (data) {
          return _webapp.callback(data, callback);
        });
      });
    }

    if (_env.android) {
      _webapp.connectWebViewJavascriptBridge(function (bridge) {
        if (_webapp.init === false) {
          //初始化
          _webapp.init = true;

          bridge.init(function (message, responseCallback) {
            var data = {
              'Javascript Responds': 'Wee!'
            };
            responseCallback(data);
          });
        }

        bridge.callHandler(handler, {type: type}, function (response) {
          // response = eval('(' + response + ')');
          // _webapp.callback(response, callback);
          // _webapp.log('call response', response);
        });

        bridge.registerHandler(handler, function (response, responseCallback) {
          responseCallback(response);
          response = eval('(' + response + ')');
          return _webapp.callback(response, callback);
        });

      });
    }
  },

  /**
   * 同步获取apiToken
   * eg. apiToken = {data : {'access_token' : 'xx', 'auth_key' : 'abc123ac', 'timestamp' : '123123123'}, statusCode : '1'};
   * eg. apiToken = {data : "", statusCode : '-1'};
   * @returns {_webapp.apiToken|{}}
   */
  getApiTokenSync: function () {

    if(_webapp.debug === true){
      _webapp.apiToken = {data : {'access_token' : '30092bf76aa42b9c0708968842482ebf', 'auth_key' : 'FD10m5QQ', 'timestamp' : '123123123'}, statusCode : '1'}
    }

    // _webapp.log('_webapp getApiTokenSync running');
    // _webapp.log(_webapp.apiToken);
    return _webapp.apiToken;
  },

  /**
   * 异步回调获取apiToken
   * 将自动存放至当前成员变量_webapp.apiToken
   * eg. apiToken = {data : {'access_token' : 'xx', 'auth' : 'abc123ac', 'timestamp' : '123123123'}, statusCode : '1'};
   * @returns {_webapp.apiToken|{}}
   */
  getApiToken: function (callback) {

    // _webapp.log('_webapp getApiToken running');
    var handler = 'getApiToken';

    if (_env.ios) {
      _webapp.setupWebViewJavascriptBridge(function (bridge) {
        bridge.callHandler(handler, function (response) {
          // _webapp.log('_webapp callHandler back, response :');
          // _webapp.log(response);
          // _webapp.apiToken = response;
          // _webapp.log('_webapp callHandler back, apiToken :');
          // _webapp.log(_webapp.apiToken);
          // return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (response, responseCallback) {
          // _webapp.log('_webapp registerHandler back, response :');
          // _webapp.log(response);
          // responseCallback('getApiToken responseCallback.');
          _webapp.apiToken = response;

          // _webapp.log('_webapp registerHandler back, getSessionKey :');
          // _webapp.log(_webapp.apiToken);
          return _webapp.callback(response, callback);
        });
      });
    }

    if (_env.android) {
      _webapp.connectWebViewJavascriptBridge(function (bridge) {
        if (_webapp.init === false) {
          //初始化
          _webapp.init = true;
          bridge.init(function (message, responseCallback) {
            var data = {
              'Javascript Responds': 'apiToken init!'
            };
            responseCallback(data);
          });
        }

        bridge.callHandler(handler, function (response) {
          // response = eval('(' + response + ')');
          // _webapp.log('_webapp callHandler back, response :');
          // _webapp.log(response);
          // _webapp.apiToken = response;
          // _webapp.log('_webapp callHandler back, apiToken :');
          // _webapp.log(_webapp.apiToken);
          // return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (response, responseCallback) {
          // _webapp.log('_webapp registerHandler back, response :');
          // _webapp.log(response);
          // responseCallback('getApiToken responseCallback.');
          response = eval('(' + response + ')');
          _webapp.apiToken = response;
          // _webapp.log('_webapp registerHandler back, apiToken :');
          // _webapp.log(_webapp.apiToken);
          return _webapp.callback(response, callback);
        });
      });
    }

  },

  /**
   * 同步获取sessionKey
   * eg. sessionKey = {data : {"sessionkey":"5a42277cbd1680e446e6e07ee78be19f", "timestamp":1500997527}, statusCode : '1'};
   * eg. sessionKey = {data : "", statusCode : '-1'};
   * @returns {_webapp.sessionKey|{}}
   */
  getSessionKeySync: function () {
    console.log(_webapp.debug)
    if(_webapp.debug === true){
      _webapp.sessionKey = {data : {"sessionkey":"dceda6ed4a39b8e0609529efc95xxxxx", "timestamp":1500997527}, statusCode : '1'};
    }

    // _webapp.log('_webapp getSessionKeySync running');
    // _webapp.log(_webapp.sessionKey);
    return _webapp.sessionKey;
  },

  /**
   * 异步回调获取getSessionKey
   * 将自动存放至当前成员变量_webapp.sessionKey
   * eg. sessionKey = {data : {"sessionkey":"5a42277cbd1680e446e6e07ee78be19f", "timestamp":1500997527}, statusCode : '1'};
   * @returns {_webapp.apiToken|{}}
   */
  getSessionKey: function (callback) {
    var handler = 'getSessionKey';

    if (_env.ios) {
      return _webapp.setupWebViewJavascriptBridge(function (bridge) {
        bridge.callHandler(handler, function (response) {
          // _webapp.log('_webapp callHandler back, response :');
          // _webapp.log(response);
          // _webapp.sessionKey = response;
          // _webapp.log('_webapp callHandler back, getSessionKey :');
          // _webapp.log(_webapp.sessionKey);
          // return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (response, responseCallback) {
          // _webapp.log('_webapp registerHandler back, response :');
          // _webapp.log(response);
          // responseCallback('getSessionKey responseCallback.');
          _webapp.sessionKey = response;
          // _webapp.log('_webapp registerHandler back, getSessionKey :');
          // _webapp.log(_webapp.sessionKey);
          return _webapp.callback(response, callback);
        });
      });
    }

    if (_env.android) {
      return _webapp.connectWebViewJavascriptBridge(function (bridge) {
        if (_webapp.init === false) {
          //初始化
          _webapp.init = true;
          bridge.init(function (message, responseCallback) {
            var data = {
              'Javascript Responds': 'getSessionKey init!'
            };
            responseCallback(data);
          });
        }

        bridge.callHandler(handler, function (response) {
          // _webapp.log('_webapp callHandler back, response :');
          // _webapp.log(response);
          // response = eval('(' + response + ')');
          // _webapp.sessionKey = response;
          // _webapp.log('_webapp callHandler back, getSessionKey :');
          // _webapp.log(_webapp.sessionKey);
          // return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (response, responseCallback) {
          // _webapp.log('_webapp registerHandler back, response :');
          // _webapp.log(response);
          // responseCallback('getSessionKey responseCallback.');
          response = eval('(' + response + ')');
          _webapp.sessionKey = response;
          // _webapp.log('_webapp registerHandler back, getSessionKey :');
          // _webapp.log(_webapp.sessionKey);
          return _webapp.callback(response, callback);
        });
      });
    }
  },

  uploadImg: function (callback) {
    var handler = 'uploadImg';
    if (_env.ios) {
      _webapp.setupWebViewJavascriptBridge(function (bridge) {
        bridge.callHandler(handler, function (response) {
          //return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (data) {
          return _webapp.callback(data, callback);
        });
      });
    }

    if (_env.android) {
      _webapp.connectWebViewJavascriptBridge(function (bridge) {
        if (_webapp.init === false) {
          //初始化
          _webapp.init = true;
          bridge.init(function (message, responseCallback) {
            var data = {
              'Javascript Responds': 'Wee!'
            };
            responseCallback(data);

          });
        }

        bridge.callHandler(handler, function (response) {
          //response = eval('(' + response + ')');
          //return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (response) {
          response = eval('(' + response + ')');
          return _webapp.callback(response, callback);
        });
      });
    }

  },

  payment: function (type, params, callback) {
    var handler = 'payment';

    if (_env.ios) {
      _webapp.setupWebViewJavascriptBridge(function (bridge) {
        bridge.callHandler(handler, {type: type, params: params}, function (response) {
          //return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (data) {
          return _webapp.callback(data, callback);
        });
      });
    }

    if (_env.android) {
      _webapp.connectWebViewJavascriptBridge(function (bridge) {
        if (_webapp.init === false) {
          //初始化
          _webapp.init = true;
          bridge.init(function (message, responseCallback) {
            var data = {
              'Javascript Responds': 'Wee!'
            };
            responseCallback(data);
          });
        }

        bridge.callHandler(handler, {type: type, params: params}, function (response) {
          //return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (response, responseCallback) {
          // responseCallback('get payment callback');
          response = eval('(' + response + ')');
          return _webapp.callback(response, callback);
        });

      });
    }
  },

  shellQrcode: function (url) {
    var handler = 'shellQrcode';

    if (_env.ios) {
      _webapp.setupWebViewJavascriptBridge(function (bridge) {
        bridge.callHandler(handler, {params: url}, function (response) {
          //return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (data) {
          return _webapp.callback(data, callback);
        });
      });
    }

    if (_env.android) {
      _webapp.connectWebViewJavascriptBridge(function (bridge) {
        if (_webapp.init === false) {
          //初始化
          _webapp.init = true;
          bridge.init(function (message, responseCallback) {
            var data = {
              'Javascript Responds': 'Wee!'
            };
            responseCallback(data);
          });
        }

        bridge.callHandler(handler, {params: url}, function (response) {
          //response = eval('(' + response + ')');
          //return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (response) {
          response = eval('(' + response + ')');
          return _webapp.callback(response, callback);
        });
      });
    }
  },

  getLocation: function (data, callback) {
    var handler = 'getLocation';
    console.log('hook.js getLocation');
    if (_env.ios) {
      _webapp.setupWebViewJavascriptBridge(function (bridge) {
        bridge.callHandler(handler, data, function (response) {
          //return _webapp.callback(response, callback);
        });

        bridge.registerHandler(handler, function (data, responseCallback) {
          // responseCallback('gomap callback');

          return _webapp.callback(data, callback);
        });
      });
    }

    if (_env.android) {
      console.log('进入安卓端')
      _webapp.connectWebViewJavascriptBridge(function (bridge) {
        console.log('安卓端获取信息的回调')
        if (_webapp.init === false) {
          //初始化
          _webapp.init = true;
          bridge.init(function (message, responseCallback) {
            var data = {
              'Javascript Responds': 'Wee!'
            };
            responseCallback(data);
          });
        }

        bridge.callHandler(handler, function (response) {
          console.log('callHandler response')
          // return _webapp.callback(response, callback);
        });
        console.log('out registerHandler');
        bridge.registerHandler(handler, function (response, responseCallback) {
          console.log('in registerHandler')
          console.log(response)
          responseCallback('getLocation callback');
          response = eval('(' + response + ')');
          return _webapp.callback(response, callback);
        });
      });
    }
  },
  setupWebViewJavascriptBridge: function (callback) {
    if (window.WebViewJavascriptBridge) {
      return callback(WebViewJavascriptBridge);
    }
    if (window.WVJBCallbacks) {
      return window.WVJBCallbacks.push(callback);
    }
    window.WVJBCallbacks = [callback];
    var WVJBIframe = document.createElement('iframe');
    WVJBIframe.style.display = 'none';
    WVJBIframe.src = 'https://__bridge_loaded__';
    document.documentElement.appendChild(WVJBIframe);
    setTimeout(function () {
      document.documentElement.removeChild(WVJBIframe)
    }, 0)
  },

  connectWebViewJavascriptBridge: function (callback) {
    if (window.WebViewJavascriptBridge) {
      callback(WebViewJavascriptBridge)
    } else {
      document.addEventListener(
        'WebViewJavascriptBridgeReady'
        , function () {
          callback(WebViewJavascriptBridge)
        },
        false
      );
    }
  },

  callback: function (res, cb) {
    if (typeof cb === "object" || typeof cb === "function") {
      if (typeof cb === "function") {
        cb(res);
        return true;
      }

      if (res.statusCode === 0) {
        if (typeof cb.success === "function") {
          cb.success(res);
        }
      } else {
        if (typeof cb.fail === "function") {
          cb.fail(res);
        }
      }

      if (typeof cb.complete === 'function') {
        cb.complete(res);
      }
    }

    return res;
  }
};
