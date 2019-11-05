let selectedLines = [];

function updateHighlightedLine() {
	updateHighlightedLineTo(window.location.hash);
}

function updateHighlightedLineTo(theNewHash) {

   let next;
   for (next in selectedLines) {
		document.getElementById('line' + selectedLines[next]).className = '';
		document.getElementById('anchor' + selectedLines[next]).className = 'lineAnchor';
	}
	selectedLines = [];

   let line = -1;
   if (theNewHash && theNewHash.match('L[0-9]+-L[0-9]+')) {
      const dashIndex = theNewHash.indexOf('-');
      const start = parseInt(theNewHash.substring(2, dashIndex));
      const end = parseInt(theNewHash.substring(dashIndex + 2));
      for (let i = start; i <= end; i++) {
			selectedLines.push(i);
		}
	} else if (theNewHash && theNewHash.match('L[0-9]+')) {
		line = parseInt(theNewHash.substring(2));
		selectedLines.push(line);
	}


	for (next in selectedLines) {
		// Prevent us from scrolling to the selected line
		document.getElementById('L' + selectedLines[next]).name = '';
		// Select the line number column
		document.getElementById('line' + selectedLines[next]).className = 'selectedLine';
		// Select the response body column
		document.getElementById('anchor' + selectedLines[next]).className = 'lineAnchor selectedLine';
	}
		
	selectedLine = line;
}

function updateHyperlinksAndStyles() {
    /* adds hyperlinks and CSS styles to dates and UUIDs (e.g. to enable user-select: all) */
    const logicalReferenceRegex = /^[A-Z][A-Za-z]+\/[0-9]+$/;
    const dateTimeRegex = /^-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\.[0-9]+)?(Z|([+\-])((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?$/; // from the spec - https://www.hl7.org/fhir/datatypes.html#datetime
    const uuidRegex = /^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$/;

    const allQuotes = document.querySelectorAll(".hlQuot");
    for (var i = 0; i < allQuotes.length; i++) {
        const quote = allQuotes[i];
        const text = quote.textContent.substr(1, quote.textContent.length - 2 /* remove quotes */);

        const absHyperlink = text.startsWith("http://") || text.startsWith("https://");
        const relHyperlink = text.match(logicalReferenceRegex);
        const uuid = text.match(uuidRegex);
        const dateTime = text.match(dateTimeRegex);

        if (absHyperlink || relHyperlink) {
            const link = document.createElement("a");
            const href = absHyperlink ? text : "FHIR_BASE/" + text;
            link.setAttribute("href", href);
            link.textContent = '"' + text + '"';
            quote.textContent = "";
            quote.appendChild(link);
        }

        if (uuid || dateTime) {
            const span = document.createElement("span");
            span.setAttribute("class", uuid ? "uuid" : "dateTime");
            span.textContent = text;
            quote.textContent = "";
            quote.appendChild(document.createTextNode('"'));
            quote.appendChild(span);
            quote.appendChild(document.createTextNode('"'));
        }
    }
}

(function() {
    'use strict';

    /* bail out if user is testing a version of this script via Greasemonkey or Tampermonkey */
    if (window.HAPI_ResponseHighlighter_userscript) {
        console.log("HAPI ResponseHighlighter: userscript detected - not executing embedded script");
        return;
    }

    console.time("updateHighlightedLine");
    updateHighlightedLine();
    console.timeEnd("updateHighlightedLine");
    window.onhashchange = updateHighlightedLine;

    console.time("updateHyperlinksAndStyles");
    updateHyperlinksAndStyles();
    console.timeEnd("updateHyperlinksAndStyles");

    window.addEventListener("load", function() {
      // https://developer.mozilla.org/en-US/docs/Web/API/Navigation_timing_API
       const now = new Date().getTime();
       const page_load_time = now - performance.timing.navigationStart;
       console.log("User-perceived page loading time: " + page_load_time + "ms");
    });

})();
