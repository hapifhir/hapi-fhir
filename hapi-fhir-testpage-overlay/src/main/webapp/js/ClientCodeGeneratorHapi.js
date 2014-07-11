	
function generateHapi(json, container) {
	if (json.action == 'search') {
		generateHapiSearch(json, container);
	}
}

function generateHapiSearch(json, container) {
	container.append($('<span />', {'class': 'clientCodeComment'}).text("// Create a client (only needed once)"));
	container.append($('<br/>'));
	container.append($('<span />', {'class': 'clientCodePreamble'}).text("FhirContext ctx = new FhirContext();"));
	container.append($('<br/>'));
	container.append($('<span />', {'class': 'clientCodePreamble'}).text("IGenericClient client = ctx.newRestfulGenericClient(\"" + json.base + "\");"));
	container.append($('<br/>'));
	container.append($('<br/>'));
	container.append($('<span />', {'class': 'clientCodeComment'}).text("// Invoke the client"));
	container.append($('<br/>'));
	
	var searchLine = 'Bundle bundle = client.search()';
	if (json.resource != null) {
		searchLine = searchLine + '.forResource(' + json.resource + '.class)';
	} else {
		searchLine = searchLine + '.forAllResources()';
	}
	container.append($('<span />', {'class': 'clientCodeMain'}).text(searchLine));
	
	var indented = $('<div />', {'class': 'clientCodeIndent'});
	container.append(indented);
	
	if (json.pretty) {
		indented.append($('<span />', {'class': 'clientCodeMain'}).text('.setPrettyPrint(' + json.pretty + ')'));
		indented.append($('<br/>'));
	}
	
	if (json.format) {
		indented.append($('<span />', {'class': 'clientCodeMain'}).text('.setEncoding(EncodingEnum.' + json.format.toUpperCase() + ')'));
		indented.append($('<br/>'));
	}
	
	for (var i = 0; i < json.params.length; i++) {
		var nextParam = json.params[i];
		var paramLine = null;
		if (nextParam.type == 'string') {
			paramLine = '.where(new StringParam("' + nextParam.name + '").matches().value("' + nextParam.value + '"))';
		} else if (nextParam.type == 'token') {
			var idx = nextParam.value.indexOf('|');
			if (idx == -1) {
				paramLine = '.where(new TokenParam("' + nextParam.name + '").exactly().code("' + nextParam.value + '"))';
			} else {
				paramLine = '.where(new TokenParam("' + nextParam.name + '").exactly().systemAndCode("' + nextParam.value.substring(0,idx) + '", "' + nextParam.value.substring(idx+1) + '"))';
			}
		} else if (nextParam.type == 'number') {
			paramLine = '.where(new NumberParam("' + nextParam.name + '").exactly().value("' + nextParam.value + '"))';
		} else if (nextParam.type == 'date') {
			if (nextParam.value.substring(0,2) == '>=') {
				paramLine = '.where(new DateParam("' + nextParam.name + '").afterOrEquals().value("' + nextParam.value.substring(2) + '"))';
			} else if (nextParam.value.substring(0,1) == '>') {
				paramLine = '.where(new DateParam("' + nextParam.name + '").after().value("' + nextParam.value.substring(1) + '"))';
			} else if (nextParam.value.substring(0,2) == '<=') {
				paramLine = '.where(new DateParam("' + nextParam.name + '").beforeOrEquals().value("' + nextParam.value.substring(2) + '"))';
			} else if (nextParam.value.substring(0,1) == '<') {
				paramLine = '.where(new DateParam("' + nextParam.name + '").before().value("' + nextParam.value.substring(1) + '"))';
			} 
		}
		if (paramLine != null) {
			indented.append($('<span />', {'class': 'clientCodeMain'}).text(paramLine));
			indented.append($('<br/>'));
		}
	}	
	
	for (var i = 0; i < json.includes.length; i++) {
		indented.append($('<span />', {'class': 'clientCodeMain'}).text('.include(new Include("' + json.includes[i] + '"))'));
		indented.append($('<br/>'));
	}	

	if (json.limit) {
		indented.append($('<span />', {'class': 'clientCodeMain'}).text('.limitTo(' + json.limit + ')'));
		indented.append($('<br/>'));
	}

	indented.append($('<span />', {'class': 'clientCodeMain'}).text('.execute();'));

}
