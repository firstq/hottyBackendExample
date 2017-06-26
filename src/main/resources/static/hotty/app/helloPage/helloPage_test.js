'use strict';

describe('myApp.helloPage module', function() {

  beforeEach(module('myApp.helloPage'));

  describe('helloPage controller', function(){

    it('should ....', inject(function($controller) {
      //spec body
      var helloPageCtrl = $controller('HelloPageCtrl');
      expect(helloPageCtrl).toBeDefined();
    }));

  });
});