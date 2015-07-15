// Additional skin Javascript
// ++++++++++++++++++++++++++++++++++++++++++

!function ($) {

	$(function(){

		var $window = $(window)

		// Start carousel
		$(function() {
			$('.carousel').carousel();
		});

		// activate syntax higlighting with highlight.js
		// Note: only run if `hljs` exists
		if (typeof hljs != 'undefined')
		{
			// classic encoding with <div class="source"><pre></pre></div>
			// and HTML5 version with <pre><code></code></pre>
			$('div.source pre, pre code').each(function(i, e) {hljs.highlightBlock(e)});
		}

	})

}(window.jQuery)
