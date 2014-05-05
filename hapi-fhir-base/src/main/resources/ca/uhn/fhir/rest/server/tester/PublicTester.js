var uniqueIdSeed = 1;

function addConfigElementsToForm(theForm) {
	var encoding = document.getElementById('configEncoding');
	var pretty = document.getElementById('configPretty');
	
	var newEncoding = document.createElement("input");
	newEncoding.type='hidden';
	newEncoding.name='configEncoding';
	newEncoding.value = encoding.value;
	theForm.appendChild(newEncoding);
	
	var newPretty = document.createElement("input");
	newPretty.type='hidden';
	newPretty.name='configPretty';
	if (pretty.checked) {
		newPretty.value='on';
	}
	newPretty.checked = pretty.checked;
	theForm.appendChild(newPretty);
}

function appendSearchParam(formElement, searchParam) {
    var inputId = newUniqueId();
    if (searchParam.type && searchParam.type == 'token') {
	    formElement.append(
	    	$('<input />', { name: 'param.token.1.' + searchParam.name, placeholder: 'system/namespace', type: 'text', id: inputId }),
	    	$('<input />', { name: 'param.token.2.' + searchParam.name, placeholder: 'value', type: 'text', id: inputId }),
	    	$('<label for="' + inputId + '">' + searchParam.name + '</input>')
	    );
    } else {
	    formElement.append(
	    	$('<input />', { name: 'param.string.' + searchParam.name, placeholder: searchParam.name, type: 'text', id: inputId }),
	    	$('<label for="' + inputId + '">' + searchParam.name + '</input>')
	    );
    }
}

function appendSearchParamExtension(extension) {
	
}

/** Hide any currently displayed tester form */
function clearCurrentForm(postCompleteFunction) {
/*	$('.testerNameRow').each().fadeOut(500).promise().then(function(){
		if (postCompleteFunction != null) {
			$('.testerNameRow').each().remove();
			postCompleteFunction();
			postCompleteFunction = null;
		}
	}); */
	var current = $('.testerNameRow');
	if (current.length == 0) {
		postCompleteFunction();
	} else {
		current.first().fadeOut(300, function() {
			current.first().remove();
			clearCurrentForm(postCompleteFunction);
		});
	}
	
}

/** Create a tester form for the 'read' method */
function displayConformance(button, expandoTr) {
	highlightSelectedLink(button);
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Conformance</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" }).append(
				        $('<input />', { name: 'method', value: 'conformance', type: 'hidden' }),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'read' method */
function displayCreate(button, expandoTr, resourceName) {
	highlightSelectedLink(button);
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Create</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" }).append(
				        $('<input />', { name: 'method', value: 'create', type: 'hidden' }),
				        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
				        $('<div class="textareaWrapper">').append(
				        	$('<textarea />', { name: 'resource', rows: 10, style: 'white-space: nowrap;' })
				        ),
				        $('<br />'),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'delete' method */
function displayDelete(button, expandoTr, resourceName) {
	highlightSelectedLink(button);
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Delete</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" }).append(
				        $('<input />', { name: 'method', value: 'delete', type: 'hidden' }),
				        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
				        $('<input />', { name: 'id', placeholder: 'Resource ID', type: 'text' }),
				        $('<br />'),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'read' method */
function displayUpdate(button, expandoTr, resourceName) {
	highlightSelectedLink(button);
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Update</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" }).append(
				        $('<input />', { name: 'method', value: 'update', type: 'hidden' }),
				        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
				        $('<input />', { name: 'id', placeholder: 'Resource ID', type: 'text' }),
				        $('<textarea />', { name: 'resource', cols: 100, rows: 10, style: 'white-space: nowrap;' }),
				        $('<br />'),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'search' method */
function displaySearchType(button, expandoTr, resourceName) {
	highlightSelectedLink(button);
	var postCompleteFunction = function() {
		
		// Add a search form for the default (no parameter) search, which should
		// return all resources of the given type
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Search by Type</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" }).append(
				        $('<input />', { name: 'method', value: 'searchType', type: 'hidden' }),
				        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
				        $('<span>All Resources of Type</span><br />'),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);					  

		// Loop through each supported search parameter and add a search form for it
		conformance.rest.forEach(function(rest){
			rest.resource.forEach(function(restResource){
				if (restResource.type == resourceName) {
					if (restResource.searchParam) {
						var paramIndex = 0;
						restResource.searchParam.forEach(function(searchParam){
							var formElement = $('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" });
							formElement.append(
						        $('<input />', { name: 'method', value: 'searchType', type: 'hidden' }),
						        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' })
						    )
						    if (searchParam.documentation && searchParam.documentation.length > 0) {
						    	formElement.append(
						    		$('<span>' + searchParam.documentation + '<br /></span>')
						    	);
						    }
							
							appendSearchParam(formElement, searchParam);
							
						    // http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParam
						    if (restResource._searchParam && restResource._searchParam[paramIndex] != null) {
						    	if (restResource._searchParam[paramIndex].extension) {
						    		appendSearchParamExtension(restResource._searchParam[paramIndex].extension);
						    	}
						    }
						    
							formElement.append(
						        $('<br />')
						    );
							formElement.append(
						        $('<input />', { type: 'submit', value: 'Submit' })
						    );
							$('#' + expandoTr).append(
								$('<tr class="testerNameRow" style="display: none;" />').append(
									$('<td class="testerNameCell">Search by Type</td>'),
									$('<td />').append(
										formElement
									)
								)
							);
							
							paramIndex++;
						});
					}
				}				
			});
		});
		
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'instance-history' method */
function displayHistoryInstance(button, expandoTr, resourceName) {
	highlightSelectedLink(button);
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Read</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" }).append(
				        $('<input />', { name: 'method', value: 'history-instance', type: 'hidden' }),
				        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
				        $('<input />', { name: 'id', placeholder: 'Resource ID', type: 'text' }),
				        $('<br />'),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'read' method */
function displayRead(button, expandoTr, resourceName) {
	highlightSelectedLink(button);
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Read</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" }).append(
				        $('<input />', { name: 'method', value: 'read', type: 'hidden' }),
				        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
				        $('<input />', { name: 'id', placeholder: 'Resource ID', type: 'text' }),
				        $('<br />'),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'read' method */
function displayValidate(button, expandoTr, resourceName) {
	highlightSelectedLink(button);
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Validate</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" }).append(
				        $('<input />', { name: 'method', value: 'validate', type: 'hidden' }),
				        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
				        $('<textarea />', { name: 'resource', cols: 100, rows: 10, style: 'white-space: nowrap;' }),
				        $('<br />'),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'read' method */
function displayVRead(button, expandoTr, resourceName) {
	highlightSelectedLink(button);
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Read</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST', onsubmit: "addConfigElementsToForm(this);" }).append(
				        $('<input />', { name: 'method', value: 'vread', type: 'hidden' }),
				        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
				        $('<input />', { name: 'id', placeholder: 'Resource ID', type: 'text' }),
				        $('<input />', { name: 'versionid', placeholder: 'Version ID', type: 'text' }),
				        $('<br />'),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

function highlightSelectedLink(button) {
	$('.selectedFunctionLink').each(function() {
	    $(this).removeClass('selectedFunctionLink');
	});		
	button.className = 'selectedFunctionLink';
}

/** Generate a unique ID */
function newUniqueId() {
    return "uid" + uniqueIdSeed++;
}

/** Show a newly created tester form */
function showNewForm() {
	var time = 0;
	$('.testerNameRow').each(function() {
	    $(this).delay(time).fadeIn(200);
	    time += 200;
	});		
}
