

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

function updateSearchDateQualifier(qualifierBtn, qualifierInput, qualifier) {
	qualifierBtn.text(qualifier);
	if (qualifier == '=') {
		qualifierInput.val('');
	} else {
		qualifierInput.val(qualifier);
	}
}

function addSearchControls(theSearchParamType, theSearchParamName, theSearchParamChain, theContainerRowNum, theRowNum) {
    
	if (theSearchParamChain && theSearchParamChain.length > 0) {
		$('#search-param-rowopts-' + theContainerRowNum).append(
				$('<input />', { id: 'param.' + theRowNum + '.qualifier', type: 'hidden', value: '.' + theSearchParamChain[0] })
		);
	}
	
	$('#search-param-rowopts-' + theContainerRowNum).append(
		$('<input />', { id: 'param.' + theRowNum + '.name', type: 'hidden', value: theSearchParamName }),
		$('<input />', { id: 'param.' + theRowNum + '.type', type: 'hidden', value: theSearchParamType })
	);

    if (theSearchParamType == 'token') {
    	$('#search-param-rowopts-' + theContainerRowNum).append(
    		$('<div />', { 'class': 'col-sm-3' }).append(
		    	$('<input />', { id: 'param.' + theRowNum + '.0', placeholder: 'system/namespace', type: 'text', 'class': 'form-control' })
		    ),
    		$('<div />', { 'class': 'col-sm-3' }).append(
		    	$('<input />', { id: 'param.' + theRowNum + '.1', type: 'hidden', value: '|' }),
		    	$('<input />', { id: 'param.' + theRowNum + '.2', placeholder: 'value', type: 'text', 'class': 'form-control' })
		    )
    	);
    } else if (theSearchParamType == 'string' || theSearchParamType == 'number') {
    	$('#search-param-rowopts-' + theContainerRowNum).append(
    		$('<div />', { 'class': 'col-sm-3' }).append(
		    	$('<input />', { id: 'param.' + theRowNum + '.0', placeholder: 'value', type: 'text', 'class': 'form-control' })
	    	)
	    );
    } else if (theSearchParamType == 'date') {
    	var qualifier = $('<input />', {type:'hidden', id:'param.'+theRowNum+'.0', id:'param.'+theRowNum+'.0'});
    	
    	if (/date$/.test(theSearchParamName)) {
			var input = $('<div />', { 'class':'input-group date', 'data-date-format':'YYYY-MM-DD' });
    	} else {
			var input = $('<div />', { 'class':'input-group date', 'data-date-format':'YYYY-MM-DDTHH:mm:ss' });
    	}
    	var qualifierDiv = $('<div />');
    	input.append(
    		qualifierDiv,
			$('<input />', { type:'text', 'class':'form-control', id: 'param.' + theRowNum + '.1' }),
			$('<div />', { 'class':'input-group-addon', 'style':'padding:6px;'} ).append(
				$('<i />', { 'class':'fa fa-chevron-circle-down'})
			)
		);
        input.datetimepicker({
        	pickTime: false,
        	showToday: true
        });
        // Set up the qualifier dropdown after we've initialized the datepicker, since it
        // overrides all addon buttons while it inits..
        qualifierDiv.addClass('input-group-btn');
        var qualifierBtn = $('<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">=</button>');
        var qualifierBtnEq = $('<a>=</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, '='); });
        var qualifierBtnGt = $('<a>&gt;</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, '>');  });
        var qualifierBtnGe = $('<a>&gt;=</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, '>=');  });
        var qualifierBtnLt = $('<a>&lt;</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, '<');  });
        var qualifierBtnLe = $('<a>&lt;=</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, '<=');  });
        qualifierDiv.append(
			qualifierBtn,
			$('<ul class="dropdown-menu" role="menu">').append(
				$('<li />').append(qualifierBtnEq),
				$('<li />').append(qualifierBtnGt),
				$('<li />').append(qualifierBtnGe),
				$('<li />').append(qualifierBtnLt),
				$('<li />').append(qualifierBtnLe)
			)
		);
    
    	$('#search-param-rowopts-' + theContainerRowNum).append(
    		qualifier,
    		$('<div />', { 'class': 'col-sm-4' }).append(
		    	input
	    	)
	    );
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
	/*
	$('#search-param-rowopts-' + rowNum).append(
		$('<input />', { name: 'param.' + rowNum + '.type', type: 'hidden', value: searchParam.type })
	);
	*/
	addSearchControls(searchParam.type, searchParam.name, searchParam.chain, rowNum, rowNum);
	
	select.prevVal = newVal;
}

/*
 * Handler for "read" button which appears on each entry in the
 * summary at the top of a Bundle response view 
 */
function readFromEntriesTable(source, type, id, vid) {
	var btn = $(source);
	btn.button('loading');
	var resId = source.resourceid;
	btn.append($('<input />', { type: 'hidden', name: 'id', value: id }));
	var resVid = source.resourcevid;
	if (resVid != '') {
		btn.append($('<input />', { type: 'hidden', name: 'vid', value: vid }));
	}
	setResource(btn, type);
	$("#outerForm").attr("action", "read").submit();
}															

/*
 * Handler for "update" button which appears on each entry in the
 * summary at the top of a Bundle response view 
 */
function updateFromEntriesTable(source, type, id, vid) {
	var btn = $(source);
	btn.button('loading');
	var resId = source.resourceid;
	btn.append($('<input />', { type: 'hidden', name: 'updateId', value: id }));
	var resVid = source.resourcevid;
	if (resVid != '') {
		btn.append($('<input />', { type: 'hidden', name: 'updateVid', value: vid }));
	}	
	setResource(btn, type);
	$("#outerForm").attr("action", "resource").submit();
}															


function selectServer(serverId) {
	$('#serverSelectorFhirIcon').removeClass();
	$('#serverSelectorFhirIcon').addClass('fa fa-spinner fa-spin');
	$('#serverSelectorName').text("Loading...");
	$('#serverId').val(serverId);
	$("#outerForm").attr("action", "home").submit();
}

function setResource(target, resourceName) {
	var resource = $('#resource');
	if (resourceName != null) {
		var input = $('#resource');
		if (resource.length) {
			resource.val(resourceName);
		} else {
			var rbtn = $('<input />', { type: 'hidden', name: 'resource', value: resourceName });
			target.append(rbtn);
		}
	} else {
		if (resource.length) {
			resource.val('');
		}
	}
}

$( document ).ready(function() {
	addSearchParamRow();
});	