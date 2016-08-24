(function() {
  'use strict';

  describe('controllers', function(){
    var vm;
    var $timeout;

    beforeEach(module('dhsChallenge'));
    beforeEach(inject(function(_$controller_, _$timeout_, _webDevTec_) {
      spyOn(_webDevTec_, 'getTec').and.returnValue([{}, {}, {}, {}, {}]);

      vm = _$controller_('MainController');
      $timeout = _$timeout_;
    }));

    it('should have a timestamp creation date', function() {
      expect(vm.creationDate).toEqual(jasmine.any(Number));
    });

    it('should define animate class after delaying timeout ', function() {
      $timeout.flush();
      expect(vm.classAnimation).toEqual('rubberBand');
    });

    it('should define more than 5 awesome things', function() {
      expect(angular.isArray(vm.awesomeThings)).toBeTruthy();
      expect(vm.awesomeThings.length === 5).toBeTruthy();
    });
  });
})();
