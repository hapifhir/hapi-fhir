
var currentForm;

/** Create a tester form for the 'read' method */
function displayRead(expandoTr) {
	
	$('#' + expandoTr).show();
	currentForm = $('#' + expandoTr).append(
		$('<td class="testerNameCell">Read</td>'),
		$('<td />').append(
			$('<form />', { action: '', method: 'POST' }).append(
		        $('<input />', { name: 'method', value: 'read', type: 'hidden' }),
		        $('<input />', { name: 'id', placeholder: 'Resource ID', type: 'text' }),
		        $('<br />'),
		        $('<input />', { type: 'submit', value: 'Submit' })
		    )
		)
	);
	
}