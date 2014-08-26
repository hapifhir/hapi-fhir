	
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
		
	for (var i = 0; i < json.params.length; i++) {
		var nextParam = json.params[i];
		var paramLine = null;
		if (nextParam.type == 'string') {
			paramLine = '.where(new StringClientParam("' + nextParam.name + '")';
			paramLine += nextParam.qualifier == ':exact' ? '.matchesExactly()' : '.matches()';
			paramLine += '.value("' + nextParam.value + '"))';
		} else if (nextParam.type == 'id') {
				paramLine = '.where(new StringClientParam("' + nextParam.name + '")';
				paramLine += '.matches()';
				paramLine += '.value("' + nextParam.value + '"))';
		} else if (nextParam.type == 'token') {
			var idx = nextParam.value.indexOf('|');
			if (idx == -1) {
				paramLine = '.where(new TokenClientParam("' + nextParam.name + '").exactly().code("' + nextParam.value + '"))';
			} else {
				paramLine = '.where(new TokenClientParam("' + nextParam.name + '").exactly().systemAndCode("' + nextParam.value.substring(0,idx) + '", "' + nextParam.value.substring(idx+1) + '"))';
			}
		} else if (nextParam.type == 'number') {
			paramLine = '.where(new NumberClientParam("' + nextParam.name + '").exactly().value("' + nextParam.value + '"))';
		} else if (nextParam.type == 'reference') {
			if (nextParam.qualifier == '') {
				if (nextParam.name.indexOf('.') == -1) {
					paramLine = '.where(new ReferenceClientParam("' + nextParam.name + '").hasId("' + nextParam.value + '"))';
				}
			}
		} else if (nextParam.type == 'date') {
			var dateQual = nextParam.value.indexOf('T') == -1 ? 'day' : 'second';
			if (nextParam.value.substring(0,2) == '>=') {
				paramLine = '.where(new DateClientParam("' + nextParam.name + '").afterOrEquals().' + dateQual + '("' + nextParam.value.substring(2) + '"))';
			} else if (nextParam.value.substring(0,1) == '>') {
				paramLine = '.where(new DateClientParam("' + nextParam.name + '").after().' + dateQual + '("' + nextParam.value.substring(1) + '"))';
			} else if (nextParam.value.substring(0,2) == '<=') {
				paramLine = '.where(new DateClientParam("' + nextParam.name + '").beforeOrEquals().' + dateQual + '("' + nextParam.value.substring(2) + '"))';
			} else if (nextParam.value.substring(0,1) == '<') {
				paramLine = '.where(new DateClientParam("' + nextParam.name + '").before().' + dateQual + '("' + nextParam.value.substring(1) + '"))';
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

	if (json.pretty && json.pretty == 'true') {
		indented.append($('<span />', {'class': 'clientCodeMain'}).text('.prettyPrint()'));
		indented.append($('<br/>'));
	}
	
	if (json.format) {
		indented.append($('<span />', {'class': 'clientCodeMain'}).text('.encoded' + json.format.substring(0,1).toUpperCase() + json.format.substring(1).toLowerCase() + '()'));
		indented.append($('<br/>'));
	}

	if (json.limit) {
		indented.append($('<span />', {'class': 'clientCodeMain'}).text('.limitTo(' + json.limit + ')'));
		indented.append($('<br/>'));
	}

	indented.append($('<span />', {'class': 'clientCodeMain'}).text('.execute();'));

}
