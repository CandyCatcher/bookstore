(function($) {
	"use strict";
	$(document).ready(function() {

        /*  [ Main Menu ]
         - - - - - - - - - - - - - - - - - - - - */
        $( '.sub-menu' ).each(function() {
            $( this ).parents('li').addClass( 'has-child' ).find( '> a' ).append( '<span class="arrow"></span>' );
        });
        $( '.main-menu .arrow' ).on( 'click', function(e) {
            e.preventDefault();
            $( this ).parents( 'li' ).find( '> .sub-menu' ).slideToggle( 'fast' );
        });
        $(document).on('mouseenter', '.main-menu .mega-menu-wrap',  function(){
            $( this).parents('.header-menu').addClass('open-mega');
        }).on('mouseleave', '.main-menu .mega-menu-wrap', function() {
            $( this).parents('.header-menu').removeClass('open-mega');
        });

        $( '.mobile-menu' ).on( 'click', function() {
            $( this ).parent().toggleClass('open');
        });

        /*  [ Cart Header ]
         - - - - - - - - - - - - - - - - - - - - */
        $( '.cart-control' ).on( 'click', function(e) {
            e.preventDefault();
            $(this).parent().find('.shop-item').toggleClass('open');
            $(this).toggleClass('open-cart');
        });
        /*  [ Menu setting ]
         - - - - - - - - - - - - - - - - - - - - */
        $('.left-header .icon_setting').on('click', function(e){
            e.preventDefault();
            $(this).parent().toggleClass('open');
        });
        /*  [ Burger Menu ]
         - - - - - - - - - - - - - - - - - - - - */
        $( '.burger-control' ).on( 'click', function(e) {
            e.preventDefault();
            $( '#popup-burger' ).addClass( 'popup-open' );
        });

        /*  [ Search box ]
        - - - - - - - - - - - - - - - - - - - - - - */
        $( '.searchbox > .icon' ).on( 'click', function() {
            $( '.search-popup' ).addClass( 'popup-open' );
        });

        /*  [ Back to top ]
         - - - - - - - - - - - - - - - - - - - - */
        $('.back-to-top a').on( 'click', function(e) {
            e.preventDefault();
            $("html, body").animate({
                scrollTop: 0
            }, 500);
        });

        /*  [ Style Switch ]
        - - - - - - - - - - - - - - - - - - - - */
        $( '.style-switch a.active' ).on( 'click', function(e) {
            e.preventDefault();
        });

        /*  [ jQuery Countdown ]
        - - - - - - - - - - - - - - - - - - - - */
        var endDate = 'November 15, 2016';
        $( '.countdown ul' ).countdown({
            date: endDate,
            render: function(data) {
                $(this.el).html(
                    '<li><span>' + this.leadingZeros(data.days, 2) + '</span> Days</li>'
                    + '<li><span>' + this.leadingZeros(data.hours, 2) + '</span>Hours</li>'
                    + '<li><span>' + this.leadingZeros(data.min, 2) + '</span>Minutes</li>'
                    + '<li><span>' + this.leadingZeros(data.sec, 2) + '</span>Seconds</li>'
                );
            }
        });        

        /*  [ prettyPhoto ]
        - - - - - - - - - - - - - - - - - - - - */
        $("a[data-gal^='prettyPhoto']").prettyPhoto({
            hook: 'data-gal',
            animation_speed:'normal',
            theme:'light_square',
            slideshow:3000,
            social_tools: false
        });

        /*  [ Single Product Slider ]
         - - - - - - - - - - - - - - - - - - - - */
        var preview_carousel = $( '.product-detail .images .p-preview' );
        var thumb_carousel = $( '.product-detail .images .p-thumb' );
        preview_carousel.owlCarousel({
            items: 1,
            dots: false,
            nav: false
        });
        preview_carousel.on('changed.owl.carousel', function(event) {
            var index_owl = event.item.index;
            thumb_carousel.find( 'li' ).removeClass( 'active' );
            thumb_carousel.find( 'li' ).eq( index_owl ).addClass( 'active' );
        });
        thumb_carousel.find( 'li a' ).each(function(index) {
            $( this ).on( 'click', function(e) {
                e.preventDefault();
                preview_carousel.trigger('to.owl.carousel', [index,300,true]);
            });
        });

        /*  [ Add minus and plus number quantity ]
         - - - - - - - - - - - - - - - - - - - - */
        if( $( '.quantity' ).length > 0 ) {
            var form_cart = $( 'form .quantity' );
            form_cart.prepend( '<span class="minus"><i class="fa fa-angle-left"></i></span>' );
            form_cart.append( '<span class="plus"><i class="fa fa-angle-right"></i></span>' );

            var minus = form_cart.find( $( '.minus' ) );
            var plus  = form_cart.find( $( '.plus' ) );

            minus.on( 'click', function(){
                var qty = $( this ).parent().find( '.qty' );
                if ( qty.val() <= 1 ) {
                    qty.val( 1 );
                } else {
                    qty.val( ( parseInt( qty.val(), 10 ) - 1 ) );
                }
            });
            plus.on( 'click', function(){
                var qty = $( this ).parent().find( '.qty' );
                qty.val( ( parseInt( qty.val(), 10 ) + 1 ) );
            });
        }


        /*  [ Masonry ]
         - - - - - - - - - - - - - - - - - - - - */
        $('.masonry').masonry({
            // options
            itemSelector: '.grid-item',
            columnWidth: '.grid-item:nth-child(2)'
        });
        // STICKY HEADER
        function bg_header() {
            if ( $(window).width() > 991 ){
                if( $(window).scrollTop() >  0) {
                    $( 'body' ).addClass('sticky-bg');
                } else {
                    $( 'body' ).removeClass('sticky-bg');
                }
            }
        }
        bg_header();
        $ (window ).scroll(function () {
            bg_header();
        });
        $( window ).resize(function() {
            bg_header();
            if( $( window).width() <= 991 ){
                $( 'body' ).removeClass('sticky-bg');
            }
        });

        /*  [ Add Animated ]
         - - - - - - - - - - - - - - - - - - - - */
        $('.our-team-item').hover(function(){
            $(this).find('.social-profile li').addClass('animated fadeInLeft');
        }, function(){
            $(this).find('.social-profile li').removeClass('animated fadeInLeft');
        });
        if( $(window).width() > 767 ){
            $('.grid .product-item, .list-products .product-item, .product-box-content .product-item, .featured-content .product-item, .best-seller-content .product-item').hover(function(){
                $(this).addClass('hover-p');
                $(this).find('.p-actions a').addClass('animated fadeInDown');
            }, function(){
                $(this).removeClass('hover-p');
                $(this).find('.p-actions a').removeClass('animated fadeInDown');
            });
        }
        
        /*  [ Tabs ]
         - - - - - - - - - - - - - - - - - - - - */
        var id = $('.tabs ul li a.active').attr('href');
        $('.tabs ul li a.active').parents('.tabs').find('.tab-ct').not(this).hide();
        $('.tabs').find('.tab-content').find(id).show().find('article').addClass('animated fadeInUp');
        $('.tabs ul li a').on('click', function(e){
            e.preventDefault();
            $('.tabs ul li a').not(this).removeClass('active');
            $(this).addClass('active');
            var id = $(this).attr('href');
            $(this).parents('.tabs').find('.tab-ct').not(this).hide().find('article').removeClass('animated fadeInUp');
            $(this).parents('.tabs').find('.tab-content').find(id).show().find('article').addClass('animated fadeInUp');
        });
        $( '.tabs-infor ul li:first-child').addClass('tab-open');
        $( '.tabs-infor ul li .tab-title' ).on( 'click' , function(e){
            e.preventDefault();
            $( '.tabs-infor ul li .tab-title').not(this).parent().removeClass('tab-open');
            $( this ).parent().toggleClass( 'tab-open' );
        });

        /*  [ Portfolio Slider ]
         - - - - - - - - - - - - - - - - - - - - */
        function PortfolioSlider() {
            $('#tb-portfolio-image-carousel').flexslider({
                animation: "slide",
                controlNav: false,
                animationLoop: true,
                slideshow: false,
                itemWidth: 170,
                itemMargin: 30,
                directionNav: false,
                asNavFor: '#tb-portfolio-image'
            });

            $('#tb-portfolio-image').flexslider({
                animation: "slide",
                controlNav: false,
                animationLoop: true,
                slideshow: false,
                directionNav:true,
                prevText:"PREV",
                nextText:"NEXT",
                sync: "#tb-portfolio-image-carousel"
            });
        }

        PortfolioSlider();

        /*  [ Filter Price ]
         - - - - - - - - - - - - - - - - - - - - */
        $('#slider-range').slider({
            range: true,
            min: 20,
            max: 300,
            values: [20, 100],
            slide: function (event, ui) {
                $('#amount').text('$' + ui.values[0] + ' - $' + ui.values[1]);
            }
        });

        $('#amount').text('$' + $('#slider-range').slider('values', 0) + ' - $' + $('#slider-range').slider('values', 1));

        /*  [ Featured Tab in Home page 1 ]
         - - - - - - - - - - - - - - - - - - - - */


        /*  [ Main Slider ]
         - - - - - - - - - - - - - - - - - - - - */
         var mainCarousel = $( ".home-slider .owl-carousel" );
        mainCarousel.owlCarousel({
            items: 1,
            loop:true,
            margin:0,
            nav:true,
            navText: ['', ''],
            navSpeed: 2000,
            dotsSpeed: 2000,
            autoplaySpeed: 2000,
            dragEndSpeed: 2000,
            onInitialized: function() {
                mainCarousel.find( '.owl-item.active' ).find( '.home-slider-item' ).addClass('active');
            },
            onTranslated: function(event) {
                mainCarousel.find( '.home-slider-item' ).removeClass('active');
                mainCarousel.find( '.owl-item.active' ).find( '.home-slider-item' ).addClass('active');
            }
        });

        /*  [ Home Deal Slider ]
         - - - - - - - - - - - - - - - - - - - - */
        $(".deal-slider .owl-carousel").owlCarousel({
            loop:true,
            margin:0,
            nav:true,
            dots: true,
            navText: ['<span></span>', '<span></span>'],
            responsive:{
                0:{
                    items:1
                },
                600:{
                    items:1
                },
                1000:{
                    items:1
                }
            }
        });

        /*  [ Product Slider ]
         - - - - - - - - - - - - - - - - - - - - */
        $(".slider-product .owl-carousel").owlCarousel({
            loop:true,
            margin:0,
            nav:true,
            dots: true,
            navText: ['', '<i class="fa fa-angle-right"></i>'],
            responsive:{
                0:{
                    items:1
                },
                600:{
                    items:1
                },
                1000:{
                    items:1
                }
            }
        });

        /*  [ Best Seller Product Slider ]
         - - - - - - - - - - - - - - - - - - - - */
        $(".best-seller .owl-carousel").owlCarousel({
            loop:true,
            margin:0,
            nav:true,
            dots: false,
            navText: ['', ''],
            responsive:{
                0:{
                    items:1
                },
                600:{
                    items:1
                },
                1000:{
                    items:1
                }
            }
        });

        /*  [ Testimonial Slider ]
         - - - - - - - - - - - - - - - - - - - - */
        $(".home-testimonial .owl-carousel").owlCarousel({
            loop:true,
            margin:0,
            nav:false,
            dots: true,
            navText: ['', '<i class="fa fa-angle-right"></i>'],
            responsive:{
                0:{
                    items:1
                },
                600:{
                    items:1
                },
                1000:{
                    items:1
                }
            }
        });

    });
})(jQuery);