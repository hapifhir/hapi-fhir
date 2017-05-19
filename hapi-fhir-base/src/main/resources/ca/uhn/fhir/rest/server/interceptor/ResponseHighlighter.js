
(function() {
    'use strict';

    /* bail out if user is testing a version of this script via Greasemonkey or Tampermonkey */
    if (window.HAPI_ResponseHighlighter_userscript) {
        console.log("HAPI ResponseHighlighter: userscript detected - not executing embedded script");
        return;
    }

    /* adds hyperlinks and CSS styles to dates and UUIDs (e.g. to enable user-select: all) */
    const logicalReferenceRegex = /^[A-Z][A-Za-z]+\/[0-9]+$/;
    const dateTimeRegex = /^-?[0-9]{4}(-(0[1-9]|1[0-2])(-(0[0-9]|[1-2][0-9]|3[0-1])(T([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\.[0-9]+)?(Z|(\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00)))?)?)?$/; // from the spec - https://www.hl7.org/fhir/datatypes.html#datetime
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
})();