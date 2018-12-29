/*
 * Copyright 2018 Christophe Friederich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
"use strict";

function getViewPort() {
  var e = window, a = 'inner';
  if (!('innerWidth' in window)) {
    a = 'client';
    e = document.documentElement || document.body;
  }

  return {
    width: e[a + 'Width'],
    height: e[a + 'Height']
  };
}

var timestampSideBar = 0;

var mReflow = function () {
  var $window = $(window);
  var $body = $(document.body);
  var TOC_SEPARATOR = '_toc_';


  function initCarousel() {
    $('.carousel').carousel();
  }

  function initTocTop() {
    if ($('#m-toc-topbar').length) {
      $body.scrollspy({
        target: '#m-toc-topbar',
        offset: $('#m-top-navbar').outerHeight() + $('#m-toc-topbar').outerHeight()
      });
    }
  }

  function initTocSidebar() {
    var tocSidebar = $('#m-toc-sidebar');
    if (!tocSidebar.length) {
      return;
    }


    // collapse all
    if (!tocSidebar.hasClass('m-toc-sidebar-expanded')) {
      tocSidebar.find('.nav-collapsible').addClass('collapse').attr('aria-expanded', 'false');
    }

    // apply scrollspy to #m-toc-sidebar
    $body.scrollspy({
      target: '#m-toc-sidebar',
      offset: 0
    });


    // add auto collapse on scrollspy
    if (tocSidebar.hasClass('m-toc-sidebar-autoexpandable')) {

      $window.on('activate.bs.scrollspy', function () {
        var active = $('#m-toc-sidebar a.active');
        var collapsePanel = active.parent().next('ul.nav.nav-collapsible');
        tocSidebar.find('ul.nav.nav-collapsible').each(function (index, element) {
          var el = $(element);
          if (el.is(collapsePanel))
            return;
          var children = el.find('a.active');
          if (children.length == 0) {
            el.collapse('hide');
          }
        });
        collapsePanel.collapse('show');
        active.parent().parent('ul.nav.nav-collapsible').collapse('show');
      });
    }

  }

  function initHighlight() {
    // activate syntax higlighting with highlight.js
    // Note: only run if `hljs` exists
    if (typeof hljs !== 'undefined') {
      // classic encoding with <div class="source"><pre></pre></div>
      // and HTML5 version with <pre><code></code></pre>
      $('pre.source, div.source pre, pre code').each(function (i, e) {
        hljs.highlightBlock(e);
      });
    }
  }

  function initTopNavBar() {
    function resizeTopNavBar() {
      var navbar = $('#m-top-navbar');
      var size = 0;
      if (navbar.length) {
        size = navbar.outerHeight();
      }
      $('body').css('padding-top', size);
      $('.navside-menu').css('top', size);
      $('.toc-sidebar-fixed').css('top', size);
      $('#m-toc-topbar').css('top', size);
    }
    $window.resize(resizeTopNavBar);
    // initialize size on start up
    resizeTopNavBar();
    // prevents the browser from opening a URL but allows that if tapped once
    // again in succession
    $('.dropdown-submenu').doubleTapToGo();

    // auto adjust placement of sub dropdown menu when out of window.
    $('.dropdown-submenu').on('mouseenter', function (event) {
      var ww = $(window).width();
      var menu = $(this);
      var $menuItem = menu.find('.dropdown-menu');
      var width = $menuItem.width();
      var mw = width + (menu.offset().left + menu.width());
      if (ww < mw) {
        $menuItem.css('left', -1 * (width), 'important');
      } else {
        $menuItem.css('left', '100%');
      }
    });

  }

  function scrollTo(el, offset, clbck) {
    var pos = (el && el.length > 0) ? el.offset().top : 0;
    pos = pos + (offset ? offset : 0);

    $('html,body').animate({
      scrollTop: pos
    }, 300, clbck);
  }

  function initScrollTop() {
    var el = $("#m-scroll-top");
    $window.scroll(function () {
      if ($(window).scrollTop() > 100) {
        el.fadeIn(500);
      } else {
        el.fadeOut(500);
      }
    });
    el.click(function () {
      if ($body.hasClass('scroll-top-smooth-enabled')) {
        scrollTo(0,0);
      } else {
        $window.scrollTo(0, 0);
      }
    });
  }

  var currentMenu = null;

  function loadFrame(href, slugName) {
    if (href === currentMenu) {
      return;
    }
    currentMenu = href;
    $('#m-doc-frame').load(href, function (evt) {
      // find li parent of 'href'
      href = href.replace(/\./g, "\\.");
      var item = $('.navside-menu a[slug-name="' + slugName + '"]').parent();
      // remove all active item
      $('.navside-menu li').removeClass('active');
      // activate current item
      item.addClass('active');

      scrollTo();
      initCarousel();
      initTocTop();
      initTocSidebar();
      initHighlight();
      initAnchorJs();
      refreshScrollSpy();

      var hash = window.location.hash;
      // scroll to anchor if toc separator exists
      if (hash && hash.indexOf(TOC_SEPARATOR)>0) {
        scrollTo($(hash));
      }
    });
  }

  function initAnchorJs() {
    if (anchors && $body.hasClass('m-toc-sidebar-enabled') || $body.hasClass('m-toc-top-enabled')
      || $body.hasClass('m-sidenav-enabled')) {
      anchors.options = {
        placement: 'left',
      };
      anchors.add('h1,h2, h3, h4, h5, h6');
    }
  }

  function initNavSidebar() {
    var navSidebar = $('.navside-menu');
    if (navSidebar.length == 0) {
      return;
    }

    /**
     * Gets the slug name of first &lt;a&gt; element in nav sidebar.
     */
    function findFirstMenu() {
      var href = $('.navside-menu a').first();
      return href.attr('slug-name');
    }

    /**
     * create a link
     * @param {*} slugName
     * @param {*} chapter
     */
    function hashes(slugName, chapter) {
      var hash = '#' + slugName;
      if (chapter) {
        hash += TOC_SEPARATOR + chapter;
      }
      return hash;
    }


    /**
     * Split the fragement of url and returns an array containing following info:
     * - the slugname of section
     * - the chapter in the section
     * @param {*} url the url the split
     */
    function splitUrl(url) {
      var index = url.indexOf(TOC_SEPARATOR, url.indexOf('#'));
      if (index >= 0) {
        return [url.substring(0, index), url.substring(index + TOC_SEPARATOR.length)];
      }
      return [url];
    }

    $window.bind('hashchange', function (evt) {

      var originalEvt = evt.originalEvent;
      var identicalPage = false;
      if (originalEvt) {
        var oldURL = splitUrl(originalEvt.oldURL);
        var newURL = splitUrl(originalEvt.newURL);
        identicalPage = oldURL[0] === newURL[0];
      }

      if (identicalPage) {
        return;
      }

      var item = null;
      var hash = window.location.hash;
      // set the first page in nav sidebar
      if (window.location.hash == '') {
        hash = hashes(findFirstMenu());
      }

      var chapter = '';
      var splittedUrl = splitUrl(hash);
      var section = splittedUrl[0].substring(1);
      if (splittedUrl.length > 1) {
        chapter = splittedUrl[1];
      } else {
        chapter = null;
      }

      // search the item in nav sidebar corresponding to section
      if (section.endsWith('html')) {
        item = $('.navside-menu a[href$="' + section + '"]');
      } else {
        item = $('.navside-menu a[slug-name$="' + section + '"]');
      }
      // expand the parent of item if it is sub-section menu.
      var collapsible = item.parents('ul.collapse');
      if (collapsible.length > 0) {
        collapsible.collapse('show');
      }
      var slugName = item.attr('slug-name');
      window.location.hash = hashes(slugName, chapter);
      var href = item.attr('href').substring(1);
      loadFrame(href, slugName);

    });


    // init fragment url part
    var fragment = window.location.hash;
    if (!fragment) {
      var href = findFirstMenu();
      window.location.hash = hashes(href);
    } else {
      // enforce load frame
      $window.trigger('hashchange');
    }

    // select first menu item on expand
    if ($body.hasClass('m-sidenav-select-first-on-select')) {
      navSidebar.on('shown.bs.collapse', function (ev) {
        var el = $(ev.target);
        // break if have already active item
        if (el.find('li.active').length > 0) {
          return;
        }

        var href = el.find('li a').first();
        window.location.hash = hashes(href.attr('slug-name'));
      });
    }

    // prevent event on collapse clik.
    navSidebar.find("a[href=\\#]").click(function (event) {
      event.preventDefault();
    });
  }


  function refreshScrollSpy() {
    $body.scrollspy('refresh');
  }

  return {
    init: function () {
      initCarousel();
      initTocSidebar();
      initTocTop();
      initNavSidebar();
      initScrollTop();
      initTopNavBar();
      initHighlight();
      initAnchorJs();
      refreshScrollSpy();
    }
  };

}();

$(document).ready(function () {
  mReflow.init();
});

/*
 * By Osvaldas Valutis, www.osvaldas.info Available for use under the MIT
 * License
 */
(function ($, window, document, undefined) {
  $.fn.doubleTapToGo = function (params) {
    if (!('ontouchstart' in window) && !navigator.msMaxTouchPoints
      && !navigator.userAgent.toLowerCase().match(/windows phone os 7/i))
      return false;

    this.each(function () {
      var curItem = false;

      $(this).on('click', function (e) {
        var item = $(this);
        if (item[0] != curItem[0]) {
          e.stopPropagation();
          e.preventDefault();
          curItem = item;
        }
      });

      $(document).on('click touchstart MSPointerDown', function (e) {
        var resetItem = true, parents = $(e.target).parents();

        for (var i = 0; i < parents.length; i++)
          if (parents[i] == curItem[0]) {
            resetItem = false;
            break;
          }

        if (resetItem)
          curItem = false;
      });
    });
    return this;
  };
})(jQuery, window, document);