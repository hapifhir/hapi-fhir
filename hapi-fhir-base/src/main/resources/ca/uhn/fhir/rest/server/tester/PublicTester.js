
var currentForm;

/** Hide any currently displayed tester form */
function clearCurrentForm(postCompleteFunction) {
	if (currentForm != null) {
		var holder = currentForm;
		holder.children().fadeOut(500).promise().then(function() {
			holder.empty();
			holder.hide();
			postCompleteFunction();
		});
		currentForm = null;
	} else {
		postCompleteFunction();
	}
}

/** Create a tester form for the 'search' method */
/*
function displayRead(expandoTr, resourceName) {
	var postCompleteFunction = function() {
		var contentCell = $('<td />');
		currentForm = $('#' + expandoTr).append(
			$('<td class="testerNameCell">Search</td>'),
			contentCell
		);
		
		conformance.rest.forEach(function(rest){
			rest.resource.forEach(function(restResource){
				if (restResource.type == 'Patient') {
					restResource.searchParam.forEach(function(searchParam){
						var formElement = $('<form/>', { action: 'PublicTesterResult.html', method: 'POST' });
						contentCell.append(
							formElement.append(
						        $('<input />', { name: 'method', value: 'read', type: 'hidden' }),
						        $('<input />', { name: 'resourceName', value: resourceName, type: 'hidden' }),
						        $('<input />', { name: 'id', placeholder: 'Resource ID', type: 'text' }),
						    )
						);						
						formElement.append(
					        $('<br />'),
					        $('<input />', { type: 'submit', value: 'Submit' })
					    )
					});
				}				
			});
		});
		
		$('#' + expandoTr).fadeIn(500);
	}
	clearCurrentForm(postCompleteFunction);
}
*/

/** Create a tester form for the 'read' method */
function displayRead(expandoTr, resourceName) {
	var postCompleteFunction = function() {
		//$('#' + expandoTr).show();
		currentForm = $('#' + expandoTr).append(
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
		);
		$('#' + expandoTr).fadeIn(500);
	}
	clearCurrentForm(postCompleteFunction);
}

/** Create a tester form for the 'read' method */
function displayVRead(expandoTr, resourceName) {
	var postCompleteFunction = function() {
		//$('#' + expandoTr).show();
		currentForm = $('#' + expandoTr).append(
			$('<td class="testerNameCell">VRead</td>'),
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
		);
		$('#' + expandoTr).fadeIn(500);
	}
	clearCurrentForm(postCompleteFunction);
}