'use strict';

describe('myApp.newProject module', function() {

  beforeEach(module('myApp.newProject'));

  describe('newProject controller', function(){

    it('should ....', inject(function($controller) {
      //spec body
      var newProjectCtrl = $controller('NewProjectCtrl');
      expect(newProjectCtrl).toBeDefined();
    }));

  });
});