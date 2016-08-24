UI Tooling Instructions
====================================

Getting started
---------------

Install node dependencies. Navigate to p2prec\peerrate-ui directory and run `npm install` and `bower install`

You must also install the Gulp CLI module, `npm install -g gulp-cli`

###You must also install the Gulp load plugins, - `npm install --save-dev gulp-load-plugins`
### Running tasks

Tasks are defined in gulpfile.js. The default task is invoked simply by running `gulp`.

To run a specific task, just enter the task name as the first argument to gulp. For example,

    grunt lint # runs just the linter task
    grunt test # run test cases

### Executing tests

Run `grunt test`

You can also generate an HTML reporter which has nicer formatting.

Run `grunt jasmine:mcc:build` then open up the generated `_SpecRunner.html` file

### Formatting

Before checking in code, run the jsbeautifier task to ensure consistent formatting. This will automatically format all the JS files in mcc.

### Starting local dev server
From inside the mcc directory, run `npm start`. By default this will start the dev server and proxy requests to CF dev environment. To use mock data instead, run `npm start -- -m`