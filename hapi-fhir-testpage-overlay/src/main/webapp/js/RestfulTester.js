

var numRows = 0;
function addSearchParamRow() {
	var nextRow = numRows++;
	var select = $('<select/>', {/*style:'margin-left:30px;'*/});
	var plusBtn = $('<button />', {type:'button', 'class':'btn btn-success btn-block'});
	plusBtn.append($('<i />', {'class':'fas fa-plus'}));
	plusBtn.isAdd = true;

	var rowDiv = $('<div />', { 'class': 'row top-buffer', id: 'search-param-row-' + nextRow }).append(
			$('<div />', { 'class': 'col-sm-1' }).append(plusBtn),
			$('<div />', { 'class': 'col-sm-5' }).append(select),
			$('<div />', { id: 'search-param-rowopts-' + nextRow, 'class': 'col-sm-6' })
	);
	$("#search-param-rows").append(rowDiv);
	
	plusBtn.click(function() {
		plusBtn.hide();
		addSearchParamRow();
	});
	
	var params = [];
	conformance.rest.forEach(function(rest){
		rest.resource.forEach(function(restResource){
			if (restResource.type === resourceName) {
				if (restResource.searchParam) {
					for (var i = 0; i < restResource.searchParam.length; i++) {
						var searchParam = restResource.searchParam[i];
						var nextName = searchParam.name + '_' + i; 
						params[nextName] = searchParam;
						select.append(
							$('<option />', { value: nextName }).text(searchParam.name + ' - ' + searchParam.documentation)
						);
						
					}
				}
			}				
		});
	});	
	select.select2();
	handleSearchParamTypeChange(select, params, nextRow, nextRow);
	select.change(function(){ handleSearchParamTypeChange(select, params, nextRow, nextRow); });
}

function updateSearchDateQualifier(qualifierBtn, qualifierInput, qualifier) {
	qualifierBtn.text(qualifier);
	if (qualifier == '=') {
		qualifierInput.val('');
	} else {
		qualifierInput.val(qualifier);
	}
}

function addSearchControls(theConformance, theSearchParamType, theSearchParamName, theSearchParamChain, theSearchParamTarget, theContainerRowNum, theRowNum) {
	
	var addNameAndType = true;
	if (theSearchParamType === 'id') {
		$('#search-param-rowopts-' + theContainerRowNum).append(
			$('<div />', { }).append(
		    	$('<input />', { id: 'param.' + theRowNum + '.0', placeholder: 'ResourceType/nnn', type: 'text', 'class': 'form-control' })
		    )
		);
	} else if (theSearchParamType == 'token') {
		
		var tokenQualifiers = [];
		tokenQualifiers.push({});
		tokenQualifiers[0].name='=';
		tokenQualifiers[0].value='';
	
		tokenQualifiers.push({});
		tokenQualifiers[1].name=':text';
		tokenQualifiers[1].value=':text';
		tokenQualifiers[1].description='The search parameter is processed as a string that searches text associated with the code/value.';
		
		tokenQualifiers.push({});
		tokenQualifiers[2].name=':not';
		tokenQualifiers[2].value=':not';
		tokenQualifiers[2].description='Reverse the code matching described in the paragraph above. Note that this includes resources that have no value for the parameter.';
		
		tokenQualifiers.push({});
		tokenQualifiers[3].name=':above';
		tokenQualifiers[3].value=':above';
		tokenQualifiers[3].description='The search parameter is a concept with the form [system]|[code], and the search parameter tests whether the coding in a resource subsumes the specified search code. For example, the search concept has an is-a relationship with the coding in the resource, and this includes the coding itself.';

		tokenQualifiers.push({});
		tokenQualifiers[4].name=':below';
		tokenQualifiers[4].value=':below';
		tokenQualifiers[4].description='The search parameter is a concept with the form [system]|[code], and the search parameter tests whether the coding in a resource is subsumed by the specified search code. For example, the coding in the resource has an is-a relationship with the search concept, and this includes the coding itself.';

		tokenQualifiers.push({});
		tokenQualifiers[5].name=':in';
		tokenQualifiers[5].value=':in';
		tokenQualifiers[5].description='The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests whether the coding is in the specified value set. The reference may be literal (to an address where the value set can be found) or logical (a reference to ValueSet.url). If the server can treat the reference as a literal URL, it does, else it tries to match known logical ValueSet.url values.';

		tokenQualifiers.push({});
		tokenQualifiers[6].name=':not-in';
		tokenQualifiers[6].value=':not-in';
		tokenQualifiers[6].description='The search parameter is a URI (relative or absolute) that identifies a value set, and the search parameter tests whether the coding is not in the specified value set.';

		
		var tokenQualifierInput = $('<input />', { id: 'param.' + theRowNum + '.qualifier', type: 'hidden' });
		$('#search-param-rowopts-' + theContainerRowNum).append(
				tokenQualifierInput
		);
		
		function clickTokenFunction(value, name){
			return function(){
				tokenQualifierInput.val(value);
				tokenQualifierLabel.text(name);
				}
      }
      var tokenQualifierLabel = $('<span>' + tokenQualifiers[0].name + '</span>');
		var tokenQualifierDropdown = $('<div />', {'class':'dropdown-menu', role:'menu'});
		for (var i = 0; i < tokenQualifiers.length; i++) {
			var qualName = tokenQualifiers[i].name;
			var nextValue = tokenQualifiers[i].value;
			var	nextLink = $('<a>' + tokenQualifiers[i].name+'</a>');
			tokenQualifierDropdown.append($('<li />').append(nextLink));
			nextLink.click(clickTokenFunction(nextValue, qualName)); 
		}
		
		
		
		$('#search-param-rowopts-' + theContainerRowNum).append(
         $('<div />', { 'class':'input-group'}).append(
            $('<div />', {'class':'input-group-prepend'}).append(
                  $('<button />', {'class':'btn btn-default dropdown-toggle input-group-text', 'data-toggle':'dropdown'}).append(
                        tokenQualifierLabel,
                        $('<span class="caret" style="margin-left: 5px;"></span>')
                     ),
                     tokenQualifierDropdown
                  ),
            $('<div />', { 'class':'input-group-prepend'} ).append(
                  $('<div class="input-group-text">System</div>')
               ),
            $('<input />', { type:'text', 'class':'form-control', id: 'param.' + theRowNum + '.0', placeholder: "(opt)" }),
            $('<div />', { 'class':'input-group-prepend'} ).append(
               $('<div class="input-group-text">Code</div>')
            ),
            $('<input />', { type:'text', 'class':'form-control', id: 'param.' + theRowNum + '.1', placeholder: "(opt)" })
         )
		);
		
	} else if (theSearchParamType === 'string') {
		var placeholderText = 'value';
		var qualifiers = [];
		qualifiers.push({});
		qualifiers[0].name='Matches';
		qualifiers[0].value='';
		qualifiers.push({});
		qualifiers[1].name='Exactly';
		qualifiers[1].value=':exact';
		
		var qualifierInput = $('<input />', { id: 'param.' + theRowNum + '.qualifier', type: 'hidden' });
		$('#search-param-rowopts-' + theContainerRowNum).append(
			qualifierInput
		);
		
		var matchesLabel = $('<span>' + qualifiers[0].name + '</span>');
		var qualifierDropdown = $('<div />', {'class':'dropdown-menu', role:'menu'});
		
		function clickFunction(value, name){
			return function(){
				qualifierInput.val(value);
				matchesLabel.text(name);
				}
      }
      for (var i = 0; i < qualifiers.length; i++) {
			var nextLink = $('<a>' + qualifiers[i].name+'</a>');
			var qualName = qualifiers[i].name;
			var nextValue = qualifiers[i].value;
			qualifierDropdown.append($('<a />', {'class':'dropdown-item'}).append(nextLink));
			nextLink.click(clickFunction(nextValue, qualName));
		}
	
		$('#search-param-rowopts-' + theContainerRowNum).append(
			$('<div />', { 'class': 'input-group' }).append(
				$('<div />', {'class':'input-group-prepend btn-group'}).append(
					$('<button />', {'class':'btn btn-default dropdown-toggle input-group-text', 'data-toggle':'dropdown'}).append(
						matchesLabel,
						$('<span class="caret" style="margin-left: 5px;"></span>')
					),
					qualifierDropdown
				),
		    	$('<input />', { id: 'param.' + theRowNum + '.0', placeholder: placeholderText, type: 'text', 'class': 'form-control' })
	    	)
	    );
	} else if (theSearchParamType == 'number') {
		var placeholderText = 'Number';
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
	} else if (theSearchParamType == 'quantity') {
		addSearchControlQuantity(theSearchParamName, theContainerRowNum, theRowNum);
	} else if (theSearchParamType == 'reference' && (!theSearchParamChain || theSearchParamChain.length == 0)) {
		/*
		 * This is a reference parameter with no chain options, so just display a simple 
		 * text box for the ID of the referenced resource
		 */
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
			$('<div />', { }).append(
				$('<input />', { id: 'param.' + theRowNum + '.1', placeholder: placeholderText, type: 'text', 'class': 'form-control' })
			)
		);
	} else if (theSearchParamType == 'reference' && theSearchParamChain.length > 0) {
		/*
		 * This is a reference parameter with possible chain options, so we need
		 * to display a secondary combobox to let the user choose which chained
		 * parameter they are filling out
		 */
		var select = $('<select/>', {/*style:'margin-left:30px;'*/});
	
		var newContainerRowNum = theContainerRowNum + "-0";
		var newContainer = $('<div />', { id: 'search-param-rowopts-' + newContainerRowNum });
	
		$('#search-param-rowopts-' + theContainerRowNum).append(
			select,
		   newContainer
		);
	
		var params = [];
		{
		var param = {};
		param.type = 'id';
		param.chain = '';
		param.name = theSearchParamName;
		param.documentation = 'The resource identity';
		param.target = [];
		params[theSearchParamName] = param;
		select.append(
			$('<option />', { value: theSearchParamName }).text(param.name + ' - ' + param.documentation)														
		);
		}
		
		for (var chainIdx = 0; chainIdx < theSearchParamChain.length; chainIdx++) {
			var nextChain = theSearchParamChain[chainIdx];
			var found = false;
			for (var resIdx = 0; resIdx < theConformance.rest[0].resource.length && !found; resIdx++) {
				var nextRes = theConformance.rest[0].resource[resIdx];
				if (!(nextRes.searchParam)) {
					continue;
				}
				for (var paramIdx = 0; paramIdx < nextRes.searchParam.length && !found; paramIdx++) {
					var nextParam = nextRes.searchParam[paramIdx];
					if (nextParam.name == nextChain) {
						if (theSearchParamTarget.length == 0 || theSearchParamTarget.indexOf(nextRes.type) != -1) {
							var nextName = nextParam.name + '_' + i;
							nextParam = jQuery.extend({}, nextParam); // clone it so we can add the chain to the name
							nextParam.name = theSearchParamName + '.' + nextParam.name;
							params[nextName] = nextParam;
							select.append(
								$('<option />', { value: nextName }).text(nextParam.name + ' - ' + nextParam.documentation)														
							);
							found = true;
						}
					}
				}
			}
		}

		select.select2();
		handleSearchParamTypeChange(select, params, newContainerRowNum, theRowNum);
		select.change(function(){ handleSearchParamTypeChange(select, params, newContainerRowNum, theRowNum); });
		addNameAndType = false;

   } else if (theSearchParamType == 'uri') {
      var placeholderText = 'value';
      var qualifiers = [];
      qualifiers.push({});
      qualifiers[0].name = 'Equals';
      qualifiers[0].value = '';
      qualifiers.push({});
      qualifiers[1].name = 'Above';
      qualifiers[1].value = ':above';
      qualifiers.push({});
      qualifiers[2].name = 'Below';
      qualifiers[2].value = ':below';

      var qualifierInput = $('<input />', {id: 'param.' + theRowNum + '.qualifier', type: 'hidden'});
      $('#search-param-rowopts-' + theContainerRowNum).append(
         qualifierInput
      );


      var matchesLabel = $('<span>' + qualifiers[0].name + '</span>');
      var qualifierDropdown = $('<div />', {'class': 'dropdown-menu', role: 'menu'});

      function clickFunction(value, name) {
         return function () {
            qualifierInput.val(value);
            matchesLabel.text(name);
         }
      }
      for (var i = 0; i < qualifiers.length; i++) {
         var nextLink = $('<a>' + qualifiers[i].name + '</a>');
         var qualName = qualifiers[i].name;
         var nextValue = qualifiers[i].value;
         qualifierDropdown.append($('<li />').append(nextLink));
         nextLink.click(clickFunction(nextValue, qualName));
      }

      $('#search-param-rowopts-' + theContainerRowNum).append(
         $('<div />', {'class': 'input-group'}).append(
            $('<div />', {'class': 'input-group-prepend btn-group'}).append(
               $('<button />', {'class': 'btn btn-default dropdown-toggle input-group-text', 'data-toggle': 'dropdown'}).append(
                  matchesLabel,
                  $('<span class="caret" style="margin-left: 5px;"></span>')
               ),
               qualifierDropdown
            ),
            $('<input />', {
               id: 'param.' + theRowNum + '.0',
               placeholder: placeholderText,
               type: 'text',
               'class': 'form-control'
            })
         )
      );

      addNameAndType = true;
   }

	if (addNameAndType) {
		$('#search-param-rowopts-' + theContainerRowNum).append(
			$('<input />', { id: 'param.' + theRowNum + '.name', type: 'hidden', value: theSearchParamName }),
			$('<input />', { id: 'param.' + theRowNum + '.type', type: 'hidden', value: theSearchParamType })
		);
	}
	
}

function addSearchControlDate(theSearchParamName, theContainerRowNum, theRowNum, theLower) {
	var inputId0 = theRowNum + '.' + (theLower ? 0 : 2);  
	var inputId1 = theRowNum + '.' + (theLower ? 1 : 3);  
	
	var qualifier = $('<input />', {type:'hidden', id:'param.'+inputId0});

   var input;
	if (/date$/.test(theSearchParamName)) {
		input = $('<div />', { 'class':'input-group date', 'data-date-format':'YYYY-MM-DD' });
	} else {
		input = $('<div />', { 'class':'input-group date', 'data-date-format':'YYYY-MM-DDTHH:mm:ss' });
	}
	var qualifierDiv = $('<div />', {'class':'input-group-prepend'});
	
	input.append(
		qualifierDiv,
		$('<input />', { type:'text', 'class':'form-control', id: 'param.' + inputId1 }),
		$('<div />', { 'class':'input-group-append input-group-addon'} ).append(
		   $('<span />', {'class':'input-group-text'}).append(
			   $('<i />', { 'class':'far fa-calendar-alt'})
         )
		)
	);
    input.datetimepicker({
    	format: "YYYY-MM-DD",
    	showTodayButton: true
    });
    // Set up the qualifier dropdown after we've initialized the datepicker, since it
    // overrides all addon buttons while it inits..
    qualifierDiv.addClass('input-group-btn');
    var qualifierTooltip = "Set a qualifier and a date to specify a boundary date. Set two qualifiers and dates to specify a range.";
    var qualifierBtn = $('<button />', {type:'button', 'class':'btn btn-default dropdown-toggle input-group-text', 'data-toggle':'dropdown', 'data-placement':'top', 'title':qualifierTooltip}).text('eq');
    qualifierBtn.tooltip({
        'selector': '',
        'placement': 'top',
        'container':'body'
      });
    var qualifierBtnEq = $('<a>eq</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'eq'); });
    var qualifierBtnGt = $('<a>gt</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'gt');  });
    var qualifierBtnGe = $('<a>ge</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'ge');  });
    var qualifierBtnLt = $('<a>lt</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'lt');  });
    var qualifierBtnLe = $('<a>le</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'le');  });
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
		$('<div />', { }).append(
	    	input
    	)
    );
}

function addSearchControlQuantity(theSearchParamName, theContainerRowNum, theRowNum) {
	var input = $('<div />', { 'class':'input-group'});
	var qualifier = $('<input />', {type:'hidden', id:'param.' + theRowNum + '.0'});
	var qualifierDiv = $('<div />', {'class':'input-group-prepend'});
	
	input.append(
		qualifierDiv,
		$('<input />', { type:'text', 'class':'form-control', id: 'param.' + theRowNum + '.1', placeholder: "value" }),
		$('<div />', { 'class':'input-group-append'} ).append(
			$('<span class="input-group-text">System</span>')
		),
		$('<input />', { type:'text', 'class':'form-control', id: 'param.' + theRowNum + '.2', placeholder: "(opt)" }),
		$('<div />', { 'class':'input-group-append'} ).append(
			$('<span class="input-group-text">Code</span>')
		),
		$('<input />', { type:'text', 'class':'form-control', id: 'param.' + theRowNum + '.3', placeholder: "(opt)" })
	);

    var qualifierTooltip = "You can optionally use a qualifier to specify a range.";
    var qualifierBtn = $('<button />', {type:'button', 'class':'btn btn-default dropdown-toggle input-group-text', 'data-toggle':'dropdown', 'data-placement':'top', 'title':qualifierTooltip}).text('=');
    qualifierBtn.tooltip({
        'selector': '',
        'placement': 'left',
        'container':'body'
      });
    var qualifierBtnEq = $('<a>=</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, '='); });
    var qualifierBtnAp = $('<a>ap (Approx)</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'ap'); });
    var qualifierBtnGt = $('<a>gt</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'gt');  });
    var qualifierBtnGe = $('<a>ge</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'ge');  });
    var qualifierBtnLt = $('<a>lt</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'lt');  });
    var qualifierBtnLe = $('<a>le</a>').click(function() { updateSearchDateQualifier(qualifierBtn, qualifier, 'le');  });
    qualifierDiv.append(
		qualifierBtn,
		$('<ul class="dropdown-menu" role="menu">').append(
			$('<li />').append(qualifierBtnEq),
			$('<li />').append(qualifierBtnAp),
			$('<li />').append(qualifierBtnGt),
			$('<li />').append(qualifierBtnGe),
			$('<li />').append(qualifierBtnLt),
			$('<li />').append(qualifierBtnLe)
		)
	);

	$('#search-param-rowopts-' + theContainerRowNum).append(
		qualifier,
		$('<div />', { }).append(
			input
		)
	);
}

function handleSearchParamTypeChange(select, params, theContainerRowNum, theParamRowNum) {
	let oldVal = select.prevVal;
	let newVal = select.val();
	if (oldVal == newVal || !(newVal)) {
		return;
	}
	$('#search-param-rowopts-' + theContainerRowNum).empty();
	var searchParam = params[newVal];
	/*
	$('#search-param-rowopts-' + rowNum).append(
		$('<input />', { name: 'param.' + rowNum + '.type', type: 'hidden', value: searchParam.type })
	);
	*/
	addSearchControls(conformance, searchParam.type, searchParam.name, searchParam.chain, searchParam.target, theContainerRowNum, theParamRowNum);
	
	select.prevVal = newVal;
}

/*
 * Handler for "read" button which appears on each entry in the
 * summary at the top of a Bundle response view 
 */
function readFromEntriesTable(source, type, id, vid) {
	var btn = $(source);
	btn.button('loading');
	btn.append($('<input />', { type: 'hidden', name: 'id', value: id }));
	var resVid = source.resourcevid;
	if (resVid !== '') {
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
	btn.append($('<input />', { type: 'hidden', name: 'updateId', value: id }));
	var resVid = source.resourcevid;
	if (resVid !== '') {
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
        for (var i=0; i<tempArray.length; i++){
            if(tempArray[i].split('=')[0] !== param){
                newAdditionalURL += temp + tempArray[i];
                temp = "&";
            }
        }
    }

    var rows_txt = temp + "" + param + "=" + paramVal;
    return baseURL + "?" + newAdditionalURL + rows_txt;
}


function selectServer(serverId) {
   let $serverSelectorFhirIcon = $('#serverSelectorFhirIcon');
   $serverSelectorFhirIcon.removeClass();
	$serverSelectorFhirIcon.addClass('fa fa-spinner fa-spin');
	$('#serverSelectorName').text("Loading...");
	$('#serverId').val(serverId);
	$("#outerForm").attr("action", "home").submit();
}

function setResource(target, resourceName) {
	var resource = $('#resource');
	if (resourceName != null) {
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

function updateSort(value) {
	$('#sort_by').val(value);
	if (value === '') {
		$('#search_sort_button').text('Default Sort');				
	} else {
		$('#search_sort_button').text(value);
	}
}

function updateSortDirection(value) {
	$('#sort_direction').val(value);
	if (value === '') {
		$('#search_sort_direction_button').text('Default');
	} else {
		$('#search_sort_direction_button').text(value);
	}
}

$( document ).ready(function() {
	if (conformance) {
		addSearchParamRow();
	}
});
