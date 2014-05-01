
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

/** Create a tester form for the 'search' method */
function displaySearchType(expandoTr, resourceName) {
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Search by Type</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST' }).append(
				        $('<input />', { name: 'method', value: 'searchType', type: 'hidden' }),
				        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
				        $('<span>All Resources of Type</span><br />'),
				        $('<input />', { type: 'submit', value: 'Submit' })
				    )
				)
			)
		);					    
		conformance.rest.forEach(function(rest){
			rest.resource.forEach(function(restResource){
				if (restResource.type == resourceName) {
					if (restResource.searchParam) {
						restResource.searchParam.forEach(function(searchParam){
							var formElement = $('<form/>', { action: 'PublicTesterResult.html', method: 'POST' });
							formElement.append(
						        $('<input />', { name: 'method', value: 'searchType', type: 'hidden' }),
						        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' })
						    )
						    if (searchParam.documentation && searchParam.documentation.length > 0) {
						    	formElement.append(
						    		$('<span>' + searchParam.documentation + '<br /></span>')
						    	);
						    }
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
							formElement.append(
						        $('<br />'),
						        $('<input />', { type: 'submit', value: 'Submit' })
						    )
							$('#' + expandoTr).append(
								$('<tr class="testerNameRow" style="display: none;" />').append(
									$('<td class="testerNameCell">Search by Type</td>'),
									$('<td />').append(
										formElement
									)
								)
							);					    
						});
					}
				}				
			});
		});
		
		showNewForm();
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'read' method */
function displayRead(expandoTr, resourceName) {
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Read</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST' }).append(
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
function displayVRead(expandoTr, resourceName) {
	var postCompleteFunction = function() {
		$('#' + expandoTr).append(
			$('<tr class="testerNameRow" style="display: none;" />').append(
				$('<td class="testerNameCell">Read</td>'),
				$('<td />').append(
					$('<form/>', { action: 'PublicTesterResult.html', method: 'POST' }).append(
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

var uniqueIdSeed = 1;
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
