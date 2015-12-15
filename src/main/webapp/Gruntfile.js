module.exports = function(grunt) {
    grunt.loadNpmTasks('grunt-wiredep');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-jshint');
    grunt.initConfig({
        jshint: {
            files: ['Gruntfile.js', 'javascript/*.js','test/javascript/*.js']
        },
        wiredep: {
          task: {
            src: ['index.html']
          }
        }
    });





    grunt.registerTask('default', ['wiredep', 'jshint']);
};