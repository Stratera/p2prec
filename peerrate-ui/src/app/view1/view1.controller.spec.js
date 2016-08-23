(function() {
  'use strict';

  describe('controllers', function(){
    var vm;

    beforeEach(module('view1'));

    beforeEach(inject(function(_$controller_) {
      vm = _$controller_('MainController');
    }));

    it('should have a name', function() {
      expect(vm.name).toEqual("DHS Challenge");
    });

  });
})();
