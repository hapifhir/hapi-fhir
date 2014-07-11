
function generateHapi(json, container) {
	if (json.action == 'search') {
		generateHapiSearch(json, container);
	}
}

function generateHapiSearch(json, container) {
	container.append($('<span />', {'class': 'clientCodePreamble'}).text("FhirContext ctx = new FhirContext();"));
	container.append($('<br/>'));
	container.append($('<span />', {'class': 'clientCodePreamble'}).text("IGenericClient client = ctx.newRestfulGenericClient(\"" + json.base + "\");"));
	container.append($('<br/>'));
	
	var searchLine = 'client.search()';
	if (json.resource != null) {
		searchLine = searchLine + '.forResource(' + json.resource + '.class)';
	} else {
		searchLine = searchLine + '.forAllResources()';
	}
	container.append($('<span />', {'class': 'clientCodeMain'}).text(searchLine));
	
}
