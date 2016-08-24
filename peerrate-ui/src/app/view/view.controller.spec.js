(function() {
  'use strict';

  describe('controllers', function(){
    var vm;

    beforeEach(module('view'));

    beforeEach(inject(function(_$controller_) {
      vm = _$controller_('ViewController');
    }));

    it('should have a name', function() {
      expect(vm.name).toEqual("DHS Challenge");
    });

  });
})();
