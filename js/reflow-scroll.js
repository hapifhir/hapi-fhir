// Support for smooth scrolling
// (simplified version, taken from http://stackoverflow.com/a/14805098/1173184)
$(window).load(function(){
  $('a[href^="#"]:not([href^="#carousel"]):not([data-toggle="dropdown"])').on('click', function(e) {

     // prevent default anchor click behavior
     e.preventDefault();

     // store hash
     var hash = this.hash;

     // animate
     $('html, body').animate({
         scrollTop: $(this.hash).offset().top
       }, 300, function(){

         // when done, add hash to url
         // (default click behaviour)
         window.location.hash = hash;
       });

  });
});
