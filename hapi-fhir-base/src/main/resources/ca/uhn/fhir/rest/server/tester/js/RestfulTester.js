
function addChildParam(searchParam, nextExt) {
	var childParam = new Object();
	for (var extIdx = 0; extIdx < nextExt.extension.length; extIdx++) {
		var childExt = nextExt.extension[extIdx];
		if (childExt.url == "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParamName") {
			childParam.name = childExt.valueString;
		}
		if (childExt.url == "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParamDescription") {
			childParam.documentation = childExt.valueString;
		}
		if (childExt.url == "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParamType") {
			childParam.type = childExt.valueCode;
		}
		if (childExt.url == "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParam") {
			addChildParam(childParam, childExt);
		}
	}
	searchParam.childParam = childParam;
}

var numRows = 0;
function addSearchParamRow() {
	var nextRow = numRows++;
	var select = $('<select/>', {/*style:'margin-left:30px;'*/});
	var plusBtn = $('<button />', {type:'button', 'class':'btn btn-success btn-block'});
	plusBtn.append($('<span />', {'class':'glyphicon glyphicon-plus'}));
	plusBtn.isAdd = true;

	var rowDiv = $('<div />', { 'class': 'row top-buffer', id: 'search-param-row-' + nextRow }).append(
			$('<div />', { 'class': 'col-sm-1' }).append(plusBtn),
			$('<div />', { 'class': 'col-sm-5' }).append(select),
			$('<div />', { id: 'search-param-rowopts-' + nextRow })
	);
	$("#search-param-rows").append(rowDiv);
	
	plusBtn.click(function() {
		addSearchParamRow();
	});
	
	var params = new Array();
	conformance.rest.forEach(function(rest){
		rest.resource.forEach(function(restResource){
			if (restResource.type == resourceName) {
				if (restResource.searchParam) {
					for (var i = 0; i < restResource.searchParam.length; i++) {
						var searchParam = restResource.searchParam[i];
						var nextName = searchParam.name + '_' + i; 
						params[nextName] = searchParam;
						select.append(
							$('<option />', { value: nextName }).text(searchParam.name + ' - ' + searchParam.documentation)														
						);
						
						if (restResource._searchParam && restResource._searchParam[i] != null) {
							if (restResource._searchParam[i].extension) {
								for (var j = 0; j < restResource._searchParam[i].extension.length; j++) {
									var nextExt = restResource._searchParam[i].extension[j];
									if (nextExt.url == "http://hl7api.sourceforge.net/hapi-fhir/extensions.xml#additionalParam") {
										addChildParam(searchParam, nextExt);
									}
								}
							}
						}
						
					}
					/*
					restResource.searchParam.forEach(function(searchParam){
						params[searchParam.name] = searchParam;
						select.append(
							$('<option />', { value: searchParam.name }).text(searchParam.name + ' - ' + searchParam.documentation)														
						);
					});
					*/
				}
			}				
		});
	});	
	select.select2();
	handleSearchParamTypeChange(select, params, nextRow);
	select.change(function(){ handleSearchParamTypeChange(select, params, nextRow); });
}

function addSearchControls(searchParam, searchParamName, containerRowNum, rowNum, isChild) {
    if (searchParam.childParam || isChild) {
		$('#search-param-rowopts-' + containerRowNum).append(
			$('<br clear="all"/>'),
			$('<div class="searchParamSeparator"/>'),
			$('<div />', { 'class': 'col-sm-6' }).append(
				$('<div class="searchParamDescription"/>').append(
					$('<div />').text(searchParam.documentation)
				)
			)
		);
    }
    
	if (searchParam.chain && searchParam.chain.length > 0) {
		$('#search-param-rowopts-' + containerRowNum).append(
				$('<input />', { name: 'param.' + rowNum + '.qualifier', type: 'hidden', value: '.' + searchParam.chain[0] })
		);
	}
	
	$('#search-param-rowopts-' + containerRowNum).append(
		$('<input />', { name: 'param.' + rowNum + '.name', type: 'hidden', value: searchParam.name })
	);

    if (searchParam.type == 'token') {
    	$('#search-param-rowopts-' + containerRowNum).append(
    		$('<div />', { 'class': 'col-sm-3' }).append(
		    	$('<input />', { name: 'param.' + rowNum + '.0', placeholder: 'system/namespace', type: 'text', 'class': 'form-control' })
		    ),
    		$('<div />', { 'class': 'col-sm-3' }).append(
		    	$('<input />', { name: 'param.' + rowNum + '.1', type: 'hidden', value: '|' }),
		    	$('<input />', { name: 'param.' + rowNum + '.2', placeholder: 'value', type: 'text', 'class': 'form-control' })
		    )
    	);
    } else if (searchParam.type == 'string' || searchParam.type == 'number') {
    	$('#search-param-rowopts-' + containerRowNum).append(
    		$('<div />', { 'class': 'col-sm-3' }).append(
		    	$('<input />', { name: 'param.' + rowNum + '.0', placeholder: 'value', type: 'text', 'class': 'form-control' })
	    	)
	    );
    } else if (searchParam.type == 'date') {
    	if (/date$/.test(searchParam.name)) {
			var input = $('<div />', { 'class':'input-group date', 'data-date-format':'YYYY-MM-DD' }).append(
				$('<input />', { type:'text', 'class':'form-control', name: 'param.' + rowNum + '.1' }),
				$('<div />', { 'class':'input-group-addon'}).append(
					$('<span />', { 'class':'glyphicon glyphicon-calendar'})
				)
			);
            input.datetimepicker({
            	pickTime: false,
            	showToday: true
            });
    	} else {
			var input = $('<div />', { 'class':'input-group date', 'data-date-format':'YYYY-MM-DDTHH:mm:ss' }).append(
				$('<input />', { type:'text', 'class':'form-control', name: 'param.' + rowNum + '.1' }),
				$('<div />', { 'class':'input-group-addon'}).append(
					$('<span />', { 'class':'glyphicon glyphicon-calendar'})
				)
			);
            input.datetimepicker({
            	sideBySide: true,
            	use24hours: true,
            	showToday: true
            });
    	}

    	$('#search-param-rowopts-' + containerRowNum).append(
    		$('<div />', { 'class': 'col-sm-2 btn-group' }).append(
				$('<select />', {type:'text', name:'param.'+rowNum+'.0', 'class':'form-control', autocomplete:'off'}).append(
					$('<option value="">(qualifier)</option>'),
					$('<option value=">=">&gt;=</option>'),
					$('<option value=">">&gt;</option>'),
					$('<option value="&lt;=">&lt;=</option>'),
					$('<option value="&lt;">&lt;</option>')
				)
	    	),							    			
    		$('<div />', { 'class': 'col-sm-4' }).append(
		    	input
	    	)
	    );
    }	
    
    if (searchParam.childParam) {
    	$('#search-param-rowopts-' + containerRowNum).append(
    		/*
    		$('<br clear="all"/>'),
    		$('<div style="height: 5px;"/>'),
			$('<div />', { 'class': 'col-sm-6' }).append(
				$('<span>' + searchParam.childParam.documentation + '</span>')
			),
			*/
			$('<input />', { name: 'param.' + rowNum + '.0.type', type: 'hidden', value: searchParam.childParam.type })
    	);
    	addSearchControls(searchParam.childParam, searchParamName, containerRowNum, rowNum + '.0', true);
    }
    
    
}

function handleSearchParamTypeChange(select, params, rowNum) {
	var oldVal = select.prevVal;
	var newVal = select.val();
	if (oldVal == newVal) {
		return;
	}
	$('#search-param-rowopts-' + rowNum).empty();
	var searchParam = params[newVal];
	$('#search-param-rowopts-' + rowNum).append(
		$('<input />', { name: 'param.' + rowNum + '.type', type: 'hidden', value: searchParam.type })
	);
	
	addSearchControls(searchParam, searchParam.name, rowNum, rowNum, false);
	
	select.prevVal = newVal;
}

/*
 * Handler for "read" button which appears on each entry in the
 * summary at the top of a Bundle response view 
 */
function readFromEntriesTable(source, type, id, vid) {
	var btn = $(source);
	var resId = source.resourceid;
	btn.append($('<input />', { type: 'hidden', name: 'id', value: id }));
	var resVid = source.resourcevid;
	if (resVid != '') {
		btn.append($('<input />', { type: 'hidden', name: 'vid', value: vid }));
	}
	btn.append($('<input />', { type: 'hidden', name: 'resource', value: type }));
}															

/*
 * Handler for "update" button which appears on each entry in the
 * summary at the top of a Bundle response view 
 */
function updateFromEntriesTable(source, type, id, vid) {
	var btn = $(source);
	var resId = source.resourceid;
	btn.append($('<input />', { type: 'hidden', name: 'update-id', value: id }));
	var resVid = source.resourcevid;
	if (resVid != '') {
		btn.append($('<input />', { type: 'hidden', name: 'update-vid', value: vid }));
	}
	btn.append($('<input />', { type: 'hidden', name: 'resource', value: type }));
}															


function selectServer(serverId) {
	$('#server-id').val(serverId);
	$('#outerForm').append(
		$('<input type="hidden" name="action" value="home"/>')
	);
	$('#outerForm').submit();
}


$( document ).ready(function() {
	addSearchParamRow();
});	