var heightOfToTop = 720;
var fadeDuration = 800;

//jQuery for page scrolling feature - requires jQuery Easing plugin
$(function() {
    $('a.page-scroll').bind('click', function (event) {
        var $anchor = $(this);
        $('html, body').stop().animate({
            scrollTop: $($anchor.attr('href')).offset().top
        }, 1500, 'easeInOutExpo');
        event.preventDefault();
    });
});

/* */
$(window).scroll(function () {
    var scrolledPixels = $(window).scrollTop();

    if (scrolledPixels > heightOfToTop) {
        $('#to-top').fadeIn(fadeDuration);
    } else {
        $('#to-top').fadeOut(fadeDuration);
    }
});
