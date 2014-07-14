

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
    } else if (theSearchParamType == 'string') {
    	var placeholderText = 'value';
    	var qualifiers = new Array();
    	qualifiers.push(new Object());
    	qualifiers[0].name='Matches';
    	qualifiers[0].value='';
    	qualifiers.push(new Object());
    	qualifiers[1].name='Exactly';
    	qualifiers[1].value=':exact';
    	
    	var qualifierInput = $('<input />', { id: 'param.' + theRowNum + '.qualifier', type: 'hidden' });
    	$('#search-param-rowopts-' + theContainerRowNum).append(
    		qualifierInput
    	);

    	var matchesLabel = $('<span>' + qualifiers[0].name + '</span>');
    	var qualifierDropdown = $('<ul />', {'class':'dropdown-menu', role:'menu'});
    	for (var i = 0; i < qualifiers.length; i++) {
    		var nextLink = $('<a>' + qualifiers[i].name+'</a>');
    		var nextName = qualifiers[i].name;
    		var nextValue = qualifiers[i].value;
    		qualifierDropdown.append($('<li />').append(nextLink));
    		nextLink.click(function(){ 
    			qualifierInput.val(nextValue);
    			matchesLabel.text(nextName);
    		});
    	}

    	$('#search-param-rowopts-' + theContainerRowNum).append(
    		$('<div />', { 'class': 'col-sm-5 input-group' }).append(
    			$('<div />', {'class':'input-group-btn'}).append(
    				$('<button />', {'class':'btn btn-default dropdown-toggle', 'data-toggle':'dropdown'}).append(
    					matchesLabel,
    					$('<span class="caret" style="margin-left: 5px;"></span>')
    				),
    				qualifierDropdown
    			),
		    	$('<input />', { id: 'param.' + theRowNum + '.0', placeholder: placeholderText, type: 'text', 'class': 'form-control' })
	    	)
	    );
    } else if (theSearchParamType == 'number' || theSearchParamType == 'reference') {
    	var placeholderText = 'value';
    	if (theSearchParamType == 'number') {
    		placeholderText = 'Number';
    	} else if (theSearchParamType == 'reference') {
    		placeholderText = 'Resource ID';
    	}
    	$('#search-param-rowopts-' + theContainerRowNum).append(
    			$('<input />', { id: 'param.' + theRowNum + '.0', placeholder: placeholderText, type: 'hidden' })
    	);
    	$('#search-param-rowopts-' + theContainerRowNum).append(
    		$('<div />', { 'class': 'col-sm-3' }).append(
		    	$('<input />', { id: 'param.' + theRowNum + '.1', placeholder: placeholderText, type: 'text', 'class': 'form-control' })
	    	)
	    );
    } else if (theSearchParamType == 'date') {
    	addSearchControlDate(theSearchParamName, theContainerRowNum, theRowNum, true);
    	addSearchControlDate(theSearchParamName, theContainerRowNum, theRowNum, false);
    }	
    
}

function addSearchControlDate(theSearchParamName, theContainerRowNum, theRowNum, theLower) {
	var inputId0 = theRowNum + '.' + (theLower ? 0 : 2);  
	var inputId1 = theRowNum + '.' + (theLower ? 1 : 3);  
	
	var qualifier = $('<input />', {type:'hidden', id:'param.'+inputId0, id:'param.'+inputId0});
	
	if (/date$/.test(theSearchParamName)) {
		var input = $('<div />', { 'class':'input-group date', 'data-date-format':'YYYY-MM-DD' });
	} else {
		var input = $('<div />', { 'class':'input-group date', 'data-date-format':'YYYY-MM-DDTHH:mm:ss' });
	}
	var qualifierDiv = $('<div />');
	
	input.append(
		qualifierDiv,
		$('<input />', { type:'text', 'class':'form-control', id: 'param.' + inputId1 }),
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
    var qualifierTooltip = "Set a qualifier and a date to specify a boundary date. Set two qualifiers and dates to specify a range.";
    var qualifierBtn = $('<button />', {type:'button', 'class':'btn btn-default dropdown-toggle', 'data-toggle':'dropdown', 'data-placement':'top', 'title':qualifierTooltip}).text('=');
    qualifierBtn.tooltip({
        'selector': '',
        'placement': 'top',
        'container':'body'
      });
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
		$('<div />', { 'class': 'col-sm-3' }).append(
	    	input
    	)
    );
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


/**
 * http://stackoverflow.com/a/10997390/11236
 */
function updateURLParameter(url, param, paramVal){
    var newAdditionalURL = "";
    var tempArray = url.split("?");
    var baseURL = tempArray[0];
    var additionalURL = tempArray[1];
    var temp = "";
    if (additionalURL) {
        tempArray = additionalURL.split("&");
        for (i=0; i<tempArray.length; i++){
            if(tempArray[i].split('=')[0] != param){
                newAdditionalURL += temp + tempArray[i];
                temp = "&";
            }
        }
    }

    var rows_txt = temp + "" + param + "=" + paramVal;
    return baseURL + "?" + newAdditionalURL + rows_txt;
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